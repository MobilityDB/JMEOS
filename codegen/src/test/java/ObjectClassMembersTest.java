/*****************************************************************************
 *
 * This MobilityDB code is provided under The PostgreSQL License.
 * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB
 * contributors
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without a written
 * agreement is hereby granted, provided that the above copyright notice and
 * this paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON
 * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *****************************************************************************/

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The members ObjectClassGenerator gives the classes of a family it is named (#member,
 * #aggregates), as PyMEOS's tools/oo_codegen/classgen.py writes them. The generator runs over a
 * catalog of two classes, as JvmNullableArgumentTest runs the SQL generators over two functions, so
 * no jar and no libmeos: Widget, of the family FAM, and Gadget, of no family named.
 */
class ObjectClassMembersTest {

    private static String slot(String c) {
        return "{\"c\": \"" + c + "\", \"canonical\": \"" + c + "\"}";
    }

    private static String param(String name, String c) {
        return "{\"name\": \"" + name + "\", \"c\": \"" + c + "\", \"canonical\": \"" + c + "\"}";
    }

    private static String fn(String name, String ret, String shape, String... params) {
        return "{\"name\": \"" + name + "\", \"api\": \"public\", \"returnType\": " + slot(ret)
                + ", \"params\": [" + String.join(", ", params) + "], \"shape\": " + shape + "}";
    }

    private static final String CATALOG = "{\"functions\": ["
            + fn("widget_scale", "struct Widget *", "{}", param("w", "const struct Widget *"),
                 param("d", "double")) + ", "
            + fn("add_widget_widget", "struct Widget *", "{}", param("w1", "const struct Widget *"),
                 param("w2", "const struct Widget *")) + ", "
            + fn("sub_float_widget", "struct Widget *", "{}", param("d", "double"),
                 param("w", "const struct Widget *")) + ", "
            + fn("widget_sum", "bool", "{\"outParams\": [\"result\"]}", param("w", "const struct Widget *"),
                 param("result", "double *")) + ", "
            + fn("widget_tag", "struct Widget *",
                 "{\"inputArrays\": [{\"param\": \"tags\", \"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
                 + " \"element\": {\"c\": \"text *\", \"canonical\": \"text *\"}}]}",
                 param("w", "const struct Widget *"), param("tags", "text **"), param("count", "int")) + ", "
            + fn("widget_over", "struct Widget *",
                 "{\"inputArrays\": [{\"param\": \"spans\", \"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
                 + " \"element\": {\"c\": \"Span\", \"canonical\": \"Span\"}}]}",
                 param("w", "const struct Widget *"), param("spans", "const Span *"), param("count", "int")) + ", "
            + fn("widget_union_transfn", "WidgetState *", "{}", param("state", "WidgetState *"),
                 param("w", "const struct Widget *"), param("band", "int")) + ", "
            + fn("widget_union_finalfn", "struct Widget *", "{}", param("state", "WidgetState *")) + ", "
            + fn("gadget_scale", "struct Gadget *", "{}", param("g", "const struct Gadget *"),
                 param("d", "double"))
            + "], \"aggregates\": [{\"sqlName\": \"widgetUnion\", \"args\": [\"widget\", \"integer\"],"
            + " \"transition\": {\"meos\": \"widget_union_transfn\"}, \"final\": {\"meos\": \"widget_union_finalfn\"}},"
            + " {\"sqlName\": \"widgetMerge\", \"altSqlName\": \"widgetMergeAgg\", \"args\": [\"widget\"],"
            + " \"transition\": {\"meos\": \"widget_union_transfn\"}, \"final\": {\"meos\": \"widget_union_finalfn\"}}],"
            + " \"typeEncodings\": {}, \"enums\": [], \"macros\": [],"
            + " \"structs\": [{\"name\": \"Span\", \"fields\": [" + param("lower", "double") + ", "
            + param("upper", "double") + "]}],"
            + " \"objectModel\": {\"lattice\": {}, \"companions\": {\"Value\": {\"nodes\": {"
            + "\"Widget\": {\"parent\": null, \"conditional\": \"FAM\"},"
            + " \"Gadget\": {\"parent\": null, \"conditional\": \"OTHER\"}}}},"
            + " \"classes\": {\"Widget\": {\"cType\": \"Widget\", \"methods\": ["
            + "{\"function\": \"widget_scale\", \"ooName\": \"scale\"},"
            + " {\"function\": \"widget_sum\", \"ooName\": \"sum\"},"
            + " {\"function\": \"widget_tag\", \"ooName\": \"tag\"},"
            + " {\"function\": \"widget_over\", \"ooName\": \"over\"}], \"operators\": ["
            + "{\"function\": \"add_widget_widget\", \"ooName\": \"add\", \"receiver\": \"left\"},"
            + " {\"function\": \"sub_float_widget\", \"ooName\": \"rsub\", \"receiver\": \"right\"}]},"
            + " \"Gadget\": {\"cType\": \"Gadget\", \"methods\": ["
            + "{\"function\": \"gadget_scale\", \"ooName\": \"scale\"}]}}}}";

    private static String widget;
    private static String gadget;

    @BeforeAll
    static void generate() throws Exception {
        Path out = Files.createTempDirectory("object-class-members");
        try {
            ObjectClassGenerator g = new ObjectClassGenerator(new ObjectMapper().readTree(CATALOG));
            g.memberFamilies.add("FAM");
            g.run(out, out.resolve("hand"));
            widget = Files.readString(out.resolve("Widget.java"));
            gadget = Files.readString(out.resolve("Gadget.java"));
        } finally {
            try (var stream = Files.walk(out)) {
                stream.sorted(java.util.Comparator.reverseOrder()).forEach(q -> q.toFile().delete());
            }
        }
    }

    @Test
    void aMethodTakesItsReceiverAsTheObject() {
        assertTrue(widget.contains("public Widget scale(double d) {"), widget);
        assertTrue(widget.contains("GeneratedFunctions.widget_scale(getInner(), d)"), widget);
    }

    @Test
    void anOperatorReflectedOntoItsRightOperandTakesItThere() {
        assertTrue(widget.contains("public Widget add(Widget w2) {"), widget);
        assertTrue(widget.contains("GeneratedFunctions.add_widget_widget(getInner(), w2.getInner())"), widget);
        assertTrue(widget.contains("public Widget rsub(double d) {"), widget);
        assertTrue(widget.contains("GeneratedFunctions.sub_float_widget(d, getInner())"), widget);
    }

    @Test
    void aSingleOutParameterIsAnsweredBoxed() {
        assertTrue(widget.contains("public Double sum() {"), widget);
        assertTrue(widget.contains("return _r == null ? null : _r.getDouble(0);"), widget);
    }

    @Test
    void anArrayTheCatalogStatesIsAListWhoseSizeIsItsCount() {
        assertTrue(widget.contains("public Widget tag(java.util.List<String> tags) {"), widget);
        assertTrue(widget.contains("GeneratedFunctions.cstring_to_text(tags.get(_i))"), widget);
        assertTrue(widget.contains("(tags == null ? 0 : tags.size())"), widget);
    }

    @Test
    void anArrayOfStructsCopiesEachElementAtItsStride() {
        // As #anArrayTheCatalogStatesIsAListWhoseSizeIsItsCount, over a struct of two doubles.
        assertTrue(widget.contains(
                "public Widget over(java.util.List<? extends types.collections.base.Span<?>> spans) {"), widget);
        assertTrue(widget.contains("spans.get(_i).get_inner().get(0, _bytes, 0, 16);"), widget);
        assertTrue(widget.contains("_spans.put((long) _i * 16, _bytes, 0, 16);"), widget);
    }

    @Test
    void anAggregateIsNamedAsTheCatalogNamesIt() {
        assertTrue(widget.contains("public static Widget widgetUnion(java.util.List<Widget> values, int band) {"),
                widget);
        assertTrue(widget.contains("_state = GeneratedFunctions.widget_union_transfn(_state, _v.getInner(), band);"),
                widget);
        assertTrue(widget.contains("public static Widget widgetMergeAgg("), widget);
        assertFalse(widget.contains(" widgetMerge("), widget);
    }

    @Test
    void aClassOfAFamilyNotNamedHasNoMembers() {
        assertFalse(gadget.contains("scale("), gadget);
    }
}
