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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The text and binary writers and readers of the Spark UDFs of tools/codegen_spark_udfs.py:
 * asText dispatches a span, a span set and a set by the type its WKB states (#_argkinds there),
 * so an intspan reaches intspan_out, a tstzspan tstzspan_out and an intset intset_out, and a
 * set of no concrete overload spatialset_as_text, tried last; asHexWKB writes plain WKB,
 * variant 0, as MobilityDB's asHexWKB with its endian left to its default; a WKB reader takes
 * the bytes of a BinaryType column and asBinary answers them; the input, output, receive and
 * send of a type, which CREATE TYPE binds, are told from the portable reader and writer and
 * from an aggregate's support function by the catalog's typeSupport (#type_support there),
 * and a function marked alone is its type's plumbing (#only_type_support). The generator runs over the
 * functions alone, as JvmNullableArgumentTest runs it, so no jar and no libmeos.
 */
class SparkTextWriterDispatchTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_spark_udfs as s",
        "def p(n, c): return {'name': n, 'cType': c, 'canonical': c}",
        "def fn(name, params, ret, sqlfn, extra=None):",
        "    f = {'name': name, 'file': 'meos.h', 'api': 'public', 'sqlfn': sqlfn,",
        "         'returnType': {'c': ret, 'canonical': ret}, 'params': params}",
        "    f.update(extra or {})",
        "    return f",
        "for k in ('Span', 'SpanSet', 'Set', 'Temporal'):",
        "    s.PARSE[k] = s.TYPED_READER[k]",
        "s.SERIAL['Span'] = ('BinaryType', 'GeneratedFunctions.span_as_wkb(%s, (byte) 4)')",
        "s.TYPE_CODE.update({'intspan': 19, 'tstzspan': 39, 'intspanset': 20, 'intset': 18})",
        "text = [fn('intspan_out', [p('s', 'const Span *')], 'char *', 'asText'),",
        "        fn('tstzspan_out', [p('s', 'const Span *')], 'char *', 'asText'),",
        "        fn('intspanset_out', [p('ss', 'const SpanSet *')], 'char *', 'asText'),",
        "        fn('intset_out', [p('s', 'const Set *')], 'char *', 'asText'),",
        "        fn('spatialset_as_text', [p('s', 'const Set *')], 'char *', 'asText')]",
        "hexw = fn('span_as_hexwkb', [p('s', 'const Span *'), p('variant', 'uint8_t'),",
        "          p('size_out', 'size_t *')], 'char *', 'asHexWKB',",
        "          {'sqlArity': 1, 'shape': {'outParams': ['size_out']}})",
        "binw = fn('span_as_wkb', [p('s', 'const Span *'), p('variant', 'uint8_t'),",
        "          p('size_out', 'size_t *')], 'uint8_t *', 'asBinary',",
        "          {'sqlArity': 1, 'shape': {'outParams': ['size_out']}})",
        "binr = fn('span_from_wkb', [p('wkb', 'const uint8_t *'), p('size', 'size_t')],",
        "          'Span *', 'intspanFromBinary')",
        "keys = {f['name']: s._parsetuple(f) for f in text}",
        "out = {'asText': s.emit_dispatch('asText', text),",
        "       'distinct': len(set(keys.values())) == len(text),",
        "       'asHexWKB': s.emit_single('asHexWKB', hexw, vis_arity=1),",
        "       'asBinary': s.emit_single('asBinary', binw, vis_arity=1),",
        "       'intspanFromBinary': s.emit_single('intspanFromBinary', binr)}",
        "recv = fn('span_from_wkb', [], 'Span *', 'intspan_recv')",
        "agg = fn('span_union_finalfn', [], 'Span *', 'spanUnionFinal')",
        "send_sig = {'args': ['intspan'], 'ret': 'bytea', 'sqlName': 'intspan_send', 'typeSupport': 'send'}",
        "recv_sig = {'args': ['internal'], 'ret': 'intspan', 'sqlName': 'intspan_recv', 'typeSupport': 'receive'}",
        "in_sig = {'args': ['cstring'], 'ret': 'intspan', 'sqlName': 'intspan_in', 'typeSupport': 'input'}",
        "out_sig = {'args': ['intspan'], 'ret': 'cstring', 'sqlName': 'intspan_out', 'typeSupport': 'output'}",
        "text_sig = {'args': ['intspan'], 'ret': 'text', 'sqlName': 'asText'}",
        "out['support'] = {",
        "    'send': s.type_support(binw, send_sig),",
        "    'asBinary': s.type_support(binw, {'args': ['intspan', 'text'], 'ret': 'bytea', 'sqlName': 'asBinary'}),",
        "    'recv': s.type_support(recv, recv_sig),",
        "    'fromBinary': s.type_support(recv, {'args': ['bytea'], 'ret': 'intspan', 'sqlName': 'intspanFromBinary'}),",
        "    'aggregate': s.type_support(agg, {'args': ['internal'], 'ret': 'intspan', 'sqlName': 'spanUnionFinal'}),",
        "    'input': s.type_support(text[0], in_sig),",
        "    'output': s.type_support(text[0], out_sig),",
        "    'asText': s.type_support(text[0], text_sig),",
        "    'onlyPlumbing': s.only_type_support(fn('pose_out', [], 'char *', 'pose_out',",
        "        {'sqlSignatures': [dict(out_sig, sqlName='pose_out')]})),",
        "    'alsoPortable': s.only_type_support(fn('intspan_out', [], 'char *', 'asText',",
        "        {'sqlSignatures': [out_sig, text_sig]}))}",
        "s.TYPE_CODE.update({'floatset': 12, 'geomset': 45, 'geogset': 43})",
        "templ = [fn('set_out', [p('s', 'const Set *')], 'char *', 'intset_out',",
        "            {'sqlSignatures': [{'args': ['intset'], 'ret': 'text', 'sqlName': 'asText'},",
        "                               {'args': ['floatset'], 'ret': 'text', 'sqlName': 'asText'},",
        "                               {'args': ['intset'], 'ret': 'cstring', 'sqlName': 'intset_out',",
        "                                'typeSupport': 'output'}]}),",
        "         fn('spatialset_as_text', [p('s', 'const Set *')], 'char *', 'asText',",
        "            {'sqlSignatures': [{'args': ['geomset'], 'ret': 'text', 'sqlName': 'asText'},",
        "                               {'args': ['geogset'], 'ret': 'text', 'sqlName': 'asText'}]})]",
        "templ = s._declared_receivers(templ, 'asText')",
        "out['template'] = s.emit_dispatch('asText', templ)",
        "out['templateDistinct'] = len({s._parsetuple(f) for f in templ}) == len(templ)",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("text-writer-dispatch");
        try {
            Files.writeString(dir.resolve("driver.py"), DRIVER);
            Process p = new ProcessBuilder("python3", "-B", dir.resolve("driver.py").toString(),
                TOOLS.toString()).redirectErrorStream(true).start();
            String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(p.waitFor(120, TimeUnit.SECONDS), "the generator ran");
            assertEquals(0, p.exitValue(), out);
            results = new ObjectMapper().readTree(out);
        } finally {
            try (var stream = Files.walk(dir)) {
                stream.sorted(java.util.Comparator.reverseOrder()).forEach(q -> q.toFile().delete());
            }
        }
    }

    /** The position in `code` of the reader `reader` followed by the call of `writer`. */
    private static int route(String code, String reader, String writer) {
        int r = code.indexOf("Pointer P_a0 = " + reader + ";");
        assertTrue(r >= 0, reader + "\n" + code);
        int w = code.indexOf("GeneratedFunctions." + writer + "(P_a0", r);
        int next = code.indexOf("Pointer P_a0 =", r + 1);
        assertTrue(w >= 0 && (next < 0 || w < next), reader + " -> " + writer + "\n" + code);
        return r;
    }

    @Test
    void everySpanSpanSetAndSetTypeReachesItsOwnWriter() {
        assertTrue(results.get("distinct").asBoolean(), "the overloads parse apart");
        String code = results.get("asText").asText();
        route(code, "UdfMarshal.spanFromOf(a0, 19)", "intspan_out");
        route(code, "UdfMarshal.spanFromOf(a0, 39)", "tstzspan_out");
        route(code, "UdfMarshal.spansetFromOf(a0, 20)", "intspanset_out");
        route(code, "UdfMarshal.setFromOf(a0, 18)", "intset_out");
    }

    @Test
    void twoWritersOfOneTemplateEachReadTheTypesTheyDeclare() {
        assertTrue(results.get("templateDistinct").asBoolean(), "the two writers parse apart");
        String code = results.get("template").asText();
        route(code, "UdfMarshal.setFromOf(a0, 12, 18)", "set_out");
        route(code, "UdfMarshal.setFromOf(a0, 43, 45)", "spatialset_as_text");
    }

    @Test
    void aSetOfNoConcreteOverloadIsWrittenBySpatialsetAsTextLast() {
        String code = results.get("asText").asText();
        int generic = route(code, "UdfMarshal.setFrom(a0)", "spatialset_as_text");
        assertTrue(generic > code.indexOf("UdfMarshal.setFromOf(a0, 18)"), code);
    }

    @Test
    void asHexWkbWritesPlainWkb() {
        String code = results.get("asHexWKB").asText();
        assertTrue(code.contains("GeneratedFunctions.span_as_hexwkb(p_s, (byte) 0)"), code);
    }

    @Test
    void asBinaryAnswersTheBytes() {
        String code = results.get("asBinary").asText();
        assertTrue(code.contains("(UDF1<Object, byte[]>)"), code);
        assertTrue(code.contains("return GeneratedFunctions.span_as_wkb(p_s, (byte) 0);"), code);
        assertTrue(code.contains("DataTypes.BinaryType"), code);
    }

    @Test
    void theFourFunctionsOfATypeAreTypeSupportAsTheCatalogMarksThem() {
        JsonNode support = results.get("support");
        assertTrue(support.get("send").asBoolean(), "intspan_send");
        assertTrue(support.get("recv").asBoolean(), "intspan_recv");
        assertTrue(support.get("input").asBoolean(), "intspan_in");
        assertTrue(support.get("output").asBoolean(), "intspan_out");
        assertFalse(support.get("asBinary").asBoolean(), "asBinary");
        assertFalse(support.get("fromBinary").asBoolean(), "intspanFromBinary");
        assertFalse(support.get("asText").asBoolean(), "asText");
        assertFalse(support.get("aggregate").asBoolean(), "an aggregate's final function");
    }

    @Test
    void aFunctionOfTypeSupportAloneIsThePlumbingOfItsType() {
        JsonNode support = results.get("support");
        assertTrue(support.get("onlyPlumbing").asBoolean(), "pose_out, marked alone");
        assertFalse(support.get("alsoPortable").asBoolean(), "intspan_out, also asText");
    }

    @Test
    void aWkbReaderTakesTheBytes() {
        String code = results.get("intspanFromBinary").asText();
        assertTrue(code.contains("(UDF1<byte[], byte[]>) (wkb) ->"), code);
        assertTrue(code.contains("GeneratedFunctions.span_from_wkb(wkb)"), code);
    }
}
