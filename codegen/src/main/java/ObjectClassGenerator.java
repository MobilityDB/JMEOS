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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Generates a class of the JMEOS type layer for every class of the MEOS object model the hand layer
 * has none for, carrying the readers and writers of its type and its {@code toString}.
 *
 * <p>The projection is MEOS.NET's {@code tools/objectgen.py}: its {@code Model._build} gives each
 * class its parent (the {@code lattice}, the template subtypes, the {@code companions}), its C type
 * ({@code classes.<Class>.cType}) and its MEOS type; its {@code portable_methods} gives each
 * encoding of the type's {@code typeEncodings} a writer {@code as<Format>} and a static reader
 * {@code from<Format>}, calling the function the catalog names for the class's SQL type
 * ({@code writers}/{@code readers}), else for its C type ({@code encoders}/{@code decoders}); and its
 * {@code to_string} makes {@code toString} the text writer the catalog names, with the decimal
 * digits the catalog records (rules 1 and 2 of the bindings in the input and output notes of
 * MobilityDB, doc/contributing/io/00-INDEX.md). The arguments and returns are marshalled by the
 * helpers of the generator beside it, #ObjectLayerGenerator (#marshalArg, #isByteInput,
 * #isSizeOut, #scalarReturn), so the two layers cannot marshal one function two ways. A class whose
 * parent has a hand class, or none, extends the generated {@code MeosObject}, which holds the
 * pointer, as MEOS.NET's classes extend its {@code MEOSObject}. A class the catalog marks
 * {@code byValue} (a grid cell) is a record holding the value MEOS passes, its methods handing the
 * value over at each call; a parameter or result of its C type elsewhere is that record.
 *
 * <p>The classes of the families named as the fourth argument also carry the methods and operators
 * the object model gives them ({@code classes.<Class>.methods}/{@code operators}) and the aggregates
 * of the catalog over their values ({@code aggregates}), as PyMEOS's
 * {@code tools/oo_codegen/classgen.py} writes the classes of a family: #member, #aggregates.
 *
 * <p>Usage: {@code ObjectClassGenerator <meos-idl.json> <output dir> <hand types dir> [<family>,...]}
 */
public class ObjectClassGenerator {

    private static final String PACKAGE = "types.generated";

    /** The portable spelling of each encoding, as MEOS.NET's {@code FORMAT_SPELLING}. */
    static final Map<String, String> FORMAT_SPELLING = new LinkedHashMap<>();
    static {
        FORMAT_SPELLING.put("text", "Text");
        FORMAT_SPELLING.put("ewkt", "EWKT");
        FORMAT_SPELLING.put("wkb", "WKB");
        FORMAT_SPELLING.put("hexwkb", "HexWKB");
        FORMAT_SPELLING.put("ewkb", "EWKB");
        FORMAT_SPELLING.put("hexewkb", "HexEWKB");
        FORMAT_SPELLING.put("mfjson", "MFJSON");
    }

    /** The suffix of a concrete class of a template subtype, as MEOS.NET's {@code SUBTYPE_SUFFIX}. */
    private static final Map<String, String> SUBTYPE_SUFFIX = Map.of(
            "TInstant", "Inst", "TSequence", "Seq", "TSequenceSet", "SeqSet");

    /** One method of a generated class. */
    private record Method(String name, String fn, String javaReturn, boolean isStatic,
                          List<ObjectLayerGenerator.Arg> args, String body) {
        Method(String name, String fn, String javaReturn, boolean isStatic,
               List<ObjectLayerGenerator.Arg> args) {
            this(name, fn, javaReturn, isStatic, args, null);
        }
    }

    private final JsonNode root;
    private final Map<String, JsonNode> functions = new HashMap<>();
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, String> ctype = new HashMap<>();
    private final Map<String, String> meostype = new HashMap<>();
    private final Map<String, String> doc = new HashMap<>();
    /**
     * The class of each C type the catalog marks {@code byValue} ({@code classes.<Class>.byValue}): a
     * value MEOS passes as itself, never as a pointer, so the class holds the value rather than the
     * pointer #MeosObject holds. The by-value counterpart of #ctype, read the same way.
     */
    private final Map<String, String> valueClass = new HashMap<>();
    /** The Java type of the value a by-value class holds: the type its wrappers take and answer. */
    private final Map<String, String> valueJava = new HashMap<>();
    private final List<String> deferred = new ArrayList<>();

    ObjectClassGenerator(JsonNode root) {
        this.root = root;
        for (JsonNode f : root.path("functions")) {
            functions.put(f.path("name").asText(), f);
        }
        for (JsonNode e : root.path("enums")) {
            enumNames.add(e.path("name").asText());
        }
        build();
        checkBindingScope();
    }

    /**
     * Refuse a class over a type the catalog marks {@code bindingScope} ({@code host}: the time
     * types a binding casts to its language's own; {@code none}: a type no binding carries), as
     * {@code check_scope_classes} of MEOS-API refuses one in the model, so a class over one stops
     * the generation rather than reaching the object layer.
     */
    private void checkBindingScope() {
        JsonNode encodings = root.path("typeEncodings");
        List<String> scoped = new ArrayList<>();
        for (String cls : new java.util.TreeSet<>(parent.keySet())) {
            String ct = ctype.get(cls);
            if (ct != null && encodings.path(ct).hasNonNull("bindingScope")) {
                scoped.add(cls + " (" + ct + ", bindingScope "
                        + encodings.path(ct).path("bindingScope").asText() + ")");
            }
        }
        if (!scoped.isEmpty()) {
            throw new IllegalStateException(
                    "classes over types out of the object layer: " + String.join("; ", scoped));
        }
    }

    /** The class hierarchy and types of the object model, as MEOS.NET's {@code Model._build}. */
    private void build() {
        JsonNode om = root.path("objectModel");
        JsonNode lattice = om.path("lattice");
        lattice.fields().forEachRemaining(e -> {
            JsonNode node = e.getValue();
            parent.put(e.getKey(), node.path("parent").isNull() ? null : node.path("parent").asText(null));
            JsonNode temptypes = node.path("temptypes");
            if (node.path("kind").asText().equals("leaf") && temptypes.size() == 1) {
                meostype.put(e.getKey(), temptypes.get(0).asText());
            }
            if (node.hasNonNull("doc")) {
                doc.put(e.getKey(), node.path("doc").asText());
            }
        });
        for (JsonNode v : om.path("axes").path("subtype").path("values")) {
            if (v.hasNonNull("class")) {
                parent.put(v.path("class").asText(), "Temporal");
            }
        }
        lattice.fields().forEachRemaining(e -> {
            if (!e.getValue().path("kind").asText().equals("leaf")) {
                return;
            }
            for (Map.Entry<String, String> s : SUBTYPE_SUFFIX.entrySet()) {
                String concrete = e.getKey() + s.getValue();
                if (om.path("classes").has(concrete)) {
                    parent.put(concrete, e.getKey());
                    doc.putIfAbsent(concrete, "A " + e.getKey() + " of subtype " + s.getKey() + ".");
                }
            }
        });
        om.path("companions").fields().forEachRemaining(fam -> {
            if (fam.getKey().startsWith("_")) {
                return;
            }
            fam.getValue().path("nodes").fields().forEachRemaining(e -> {
                if (e.getKey().startsWith("_")) {
                    return;
                }
                JsonNode node = e.getValue();
                parent.put(e.getKey(), node.path("parent").isNull() ? null : node.path("parent").asText(null));
                if (node.hasNonNull("temptype")) {
                    meostype.put(e.getKey(), node.path("temptype").asText());
                }
                if (node.hasNonNull("doc")) {
                    doc.putIfAbsent(e.getKey(), node.path("doc").asText());
                }
            });
        });
        om.path("classes").fields().forEachRemaining(e -> {
            if (e.getValue().hasNonNull("cType")) {
                ctype.put(e.getKey(), e.getValue().path("cType").asText());
                if (e.getValue().path("byValue").asBoolean(false)) {
                    valueClass.put(e.getValue().path("cType").asText(), e.getKey());
                }
            }
        });
        // A slot of a by-value C type states that type as its typedef and its width as its
        // canonical (FunctionsGenerator#slotType), and the wrapper takes and answers the width.
        // The record holds it in the one Java type every such slot maps to.
        for (JsonNode f : root.path("functions")) {
            List<JsonNode> slots = new ArrayList<>();
            slots.add(f.path("returnType"));
            f.path("params").forEach(slots::add);
            for (JsonNode s : slots) {
                String cls = valueClassOf(s);
                if (cls == null) {
                    continue;
                }
                String java = ObjectLayerGenerator.scalarReturn(
                        ObjectLayerGenerator.cleanType(FunctionsGenerator.slotType(s)));
                String known = valueJava.putIfAbsent(cls, java);
                if (java == null || (known != null && !known.equals(java))) {
                    throw new IllegalStateException("the by-value class " + cls + " has no single width: "
                            + known + " and " + s);
                }
            }
        }
    }

    /**
     * The by-value class a catalog slot holds a value of, through its typedef, or {@code null}; a
     * slot pointing at values of the type (an array of cells) holds none.
     */
    private String valueClassOf(JsonNode slot) {
        String cls = valueClass.get(slot.path("typedef").asText(""));
        return cls == null || slot.path("canonical").asText("").contains("*") ? null : cls;
    }

    /** Whether the class holds its value (#valueClass) rather than a pointer. */
    private boolean isByValue(String cls) {
        return valueJava.containsKey(cls);
    }

    /** The encoding of the class's C type in the catalog's typeEncodings, or a missing node. */
    private JsonNode encoding(String cls) {
        String ct = ctype.get(cls);
        return ct == null ? root.path("typeEncodings").path("") : root.path("typeEncodings").path(ct);
    }

    /** The class's SQL type, its MEOS type's name without its {@code T_}, lower-cased. */
    private String sqlType(String cls) {
        String mt = meostype.get(cls);
        return mt == null ? null : mt.substring(2).toLowerCase(Locale.ROOT);
    }

    /**
     * The readers and writers of every encoding the catalog's typeEncodings gives the class's type,
     * as MEOS.NET's {@code portable_methods}: the function of the type's SQL name in
     * {@code readers}/{@code writers}, else the C type's {@code decoders}/{@code encoders}; none where
     * the class or a generated ancestor already has the name.
     */
    private List<Method> portableMethods(String cls, Set<String> taken) {
        List<Method> out = new ArrayList<>();
        String sql = sqlType(cls);
        JsonNode enc = encoding(cls);
        if (sql == null || enc.isMissingNode()) {
            return out;
        }
        for (JsonNode e : enc.path("encodings")) {
            String spelling = FORMAT_SPELLING.get(e.asText());
            if (spelling == null) {
                continue;
            }
            for (boolean reader : new boolean[] {false, true}) {
                String name = (reader ? "from" : "as") + spelling;
                String fn = enc.path(reader ? "readers" : "writers").path(e.asText()).path(sql).asText(null);
                if (fn == null) {
                    fn = enc.path(reader ? "decoders" : "encoders").path(e.asText()).asText(null);
                }
                if (fn == null || taken.contains(name)) {
                    continue;
                }
                Method m = reader ? reader(cls, name, fn) : writer(cls, name, fn);
                if (m != null) {
                    out.add(m);
                    taken.add(name);
                }
            }
        }
        return out;
    }

    /** A static reader: every parameter marshalled, the returned pointer wrapped in the class. */
    private Method reader(String cls, String name, String fn) {
        JsonNode f = functions.get(fn);
        if (f == null || !f.path("api").asText().equals("public")) {
            deferred.add(cls + "." + name + " — " + fn + " is no public function");
            return null;
        }
        String retC = ObjectLayerGenerator.cleanType(FunctionsGenerator.slotType(f.path("returnType")));
        if (isByValue(cls) ? !cls.equals(valueClassOf(f.path("returnType"))) : !retC.endsWith("*")) {
            deferred.add(cls + "." + name + " — " + fn + " returns " + retC + ", no "
                    + (isByValue(cls) ? ctype.get(cls) : "pointer"));
            return null;
        }
        List<ObjectLayerGenerator.Arg> args = marshal(f, 0, cls + "." + name);
        return args == null ? null : new Method(name, fn, cls, true, args);
    }

    /** An instance writer: the receiver forwarded as the pointer, the other parameters marshalled. */
    private Method writer(String cls, String name, String fn) {
        JsonNode f = functions.get(fn);
        if (f == null || !f.path("api").asText().equals("public")) {
            deferred.add(cls + "." + name + " — " + fn + " is no public function");
            return null;
        }
        String retC = ObjectLayerGenerator.cleanType(FunctionsGenerator.slotType(f.path("returnType")));
        String ret = valueClassOf(f.path("returnType")) != null ? valueClassOf(f.path("returnType"))
                : retC.equals("uint8_t *") ? "byte[]" : ObjectLayerGenerator.scalarReturn(retC);
        if (ret == null) {
            deferred.add(cls + "." + name + " — " + fn + " returns " + retC);
            return null;
        }
        // The receiver of a by-value class is its value, which the first parameter must take.
        JsonNode recv = f.path("params").path(0);
        if (isByValue(cls) && !cls.equals(valueClassOf(recv))) {
            deferred.add(cls + "." + name + " — " + fn + " takes no " + ctype.get(cls) + " first");
            return null;
        }
        List<ObjectLayerGenerator.Arg> args = marshal(f, 1, cls + "." + name);
        return args == null ? null : new Method(name, fn, ret, false, args);
    }

    /** The marshalled parameters of f from index {@code from}, as #ObjectLayerGenerator marshals them. */
    private List<ObjectLayerGenerator.Arg> marshal(JsonNode f, int from, String where) {
        JsonNode params = f.path("params");
        Set<String> outs = new HashSet<>();
        for (JsonNode o : f.path("shape").path("outParams")) {
            outs.add(o.asText());
        }
        List<ObjectLayerGenerator.Arg> args = new ArrayList<>();
        for (int i = from; i < params.size(); i++) {
            JsonNode p = params.get(i);
            String pname = p.path("name").asText();
            if (outs.contains(pname) && ObjectLayerGenerator.isSizeOut(params, pname)) {
                continue;
            }
            String name = ObjectLayerGenerator.sanitize(pname);
            if (ObjectLayerGenerator.isByteInput(params, i)) {
                args.add(new ObjectLayerGenerator.Arg("byte[]", name, name));
                i++;
                continue;
            }
            String pC = ObjectLayerGenerator.cleanType(FunctionsGenerator.slotType(p));
            String vc = valueClassOf(p);
            if (vc != null && !outs.contains(pname)) {
                // A value of a by-value class crosses as the value it holds.
                args.add(new ObjectLayerGenerator.Arg(vc, name, name + ".value()"));
                continue;
            }
            ObjectLayerGenerator.Arg a = outs.contains(pname) ? null
                    : ObjectLayerGenerator.marshalArg(pC, name, f.path("name").asText());
            if (a == null || a.javaType().equals("Temporal") || a.javaType().startsWith("types.")) {
                deferred.add(where + " — argument " + name + " of type " + pC);
                return null;
            }
            args.add(a);
        }
        return args;
    }

    /**
     * The text writer of the class, as MEOS.NET's {@code text_writer}: the writer of its SQL type in
     * {@code writers.text}, else the text encoder of its C type.
     */
    private String textWriter(String cls) {
        JsonNode enc = encoding(cls);
        String sql = sqlType(cls);
        if (sql != null) {
            String w = enc.path("writers").path("text").path(sql).asText(null);
            if (w != null) {
                return w;
            }
        }
        return enc.path("encoders").path("text").asText(null);
    }

    /**
     * The decimal digits a text writer takes by default, as MEOS.NET's {@code to_string}: the text
     * encoding's own default, else MEOS's {@code OUT_DEFAULT_DECIMAL_DIGITS}.
     */
    private String defaultDigits(String cls) {
        JsonNode enc = encoding(cls);
        for (JsonNode aux : new JsonNode[] {enc.path("encoderAux").path("text"), enc.path("out_aux")}) {
            for (JsonNode a : aux) {
                if (a.path("name").asText().equals("maxdd") && a.has("default")) {
                    return a.path("default").asText();
                }
            }
        }
        for (JsonNode m : root.path("macros")) {
            if (m.path("name").asText().equals("OUT_DEFAULT_DECIMAL_DIGITS")) {
                return m.path("value").asText();
            }
        }
        return null;
    }

    /**
     * The families whose classes carry the members of the object model, as PyMEOS's
     * {@code tools/oo_codegen/classgen.py} takes the family it writes.
     */
    final Set<String> memberFamilies = new HashSet<>();

    /** The enums of the catalog, which the wrappers take and answer as an {@code int}. */
    private final Set<String> enumNames = new HashSet<>();

    /** The C types of the temporal values the hand {@code types.temporal.Temporal} holds. */
    private static final Set<String> TEMPORAL_C = Set.of("Temporal", "TInstant", "TSequence", "TSequenceSet");

    /** The parameters, the call and the statements before it of one member, as #member builds them. */
    private record Call(List<ObjectLayerGenerator.Arg> args, List<String> call, String prelude) {}

    /**
     * The classes of the member families, read off the catalog as {@code classgen.py}'s
     * {@code family_classes} reads them: the {@code Value} companion nodes the family conditions.
     */
    private Set<String> memberClasses() {
        Set<String> out = new TreeSet<>();
        root.path("objectModel").path("companions").path("Value").path("nodes").fields().forEachRemaining(e -> {
            if (memberFamilies.contains(e.getValue().path("conditional").asText(""))) {
                out.add(e.getKey());
            }
        });
        return out;
    }

    /** The C type a single pointer points at ({@code struct Raster *} gives {@code Raster}), or {@code null}. */
    private static String pointee(String c) {
        String t = ObjectLayerGenerator.cleanType(c);
        if (!t.endsWith("*") || t.endsWith("**")) {
            return null;
        }
        return t.substring(0, t.length() - 1).replace("struct ", "").trim();
    }

    /**
     * The generated class holding a pointer of the C type, as {@code classgen.py}'s {@code held}: the
     * class named as the C type, else the one class of that C type.
     */
    private String heldClass(String c, Set<String> generated) {
        String t = pointee(c);
        if (t == null) {
            return null;
        }
        // Over every class of the C type, hand or generated: a C type several classes share (a Span)
        // is held by the class of its own name only.
        List<String> of = new ArrayList<>();
        for (Map.Entry<String, String> e : ctype.entrySet()) {
            if (e.getValue().equals(t)) {
                of.add(e.getKey());
            }
        }
        String cls = of.contains(t) ? t : of.size() == 1 ? of.get(0) : null;
        return cls != null && generated.contains(cls) && !isByValue(cls) ? cls : null;
    }

    /**
     * How an element of an array of structs crosses, its value written as {@code _e}, or {@code null}
     * when the element is no struct of the catalog or has no class: a value of a generated class
     * through its pointer, else as #ObjectLayerGenerator#marshalArg passes one such struct.
     */
    private ObjectLayerGenerator.Arg structElement(String elem, String fn, Set<String> generated) {
        String t = ObjectLayerGenerator.cleanType(elem);
        boolean struct = false;
        for (JsonNode s : root.path("structs")) {
            struct |= s.path("name").asText().equals(t) && !s.path("fields").isEmpty();
        }
        if (t.contains("*") || !struct) {
            return null;
        }
        String held = heldClass(t + " *", generated);
        if (held != null) {
            return new ObjectLayerGenerator.Arg(held, "_e", "_e.getInner()");
        }
        ObjectLayerGenerator.Arg a = ObjectLayerGenerator.marshalArg(t + " *", "_e", fn);
        return a == null || a.javaType().equals("Temporal") ? null : a;
    }

    /** The parameter names of the out-parameters the catalog states for f. */
    private static Set<String> outParams(JsonNode f) {
        Set<String> outs = new HashSet<>();
        for (JsonNode o : f.path("shape").path("outParams")) {
            outs.add(o.asText());
        }
        return outs;
    }

    /**
     * The single value out-parameter a {@code bool} function folds to, as #ObjectLayerGenerator folds
     * the bool+result pattern, or {@code null}.
     */
    private static String resultOut(JsonNode f) {
        JsonNode params = f.path("params");
        List<String> unfolded = new ArrayList<>();
        for (String o : outParams(f)) {
            if (!ObjectLayerGenerator.isSizeOut(params, o)) {
                unfolded.add(o);
            }
        }
        String retC = ObjectLayerGenerator.cleanType(FunctionsGenerator.slotType(f.path("returnType")));
        return retC.equals("bool") && unfolded.size() == 1 ? unfolded.get(0) : null;
    }

    /**
     * The Java parameters and the wrapper call of f, the parameters before {@code from} and the one at
     * {@code receiver} being the call expressions given, as {@code classgen.py}'s {@code argument}
     * unwraps each one: a value of a generated class or of a hand class crosses as its pointer, an
     * array the catalog states ({@code shape.inputArrays}) as a list whose size is its count, a byte
     * buffer as a {@code byte[]}; {@code null} with the member deferred when one cannot cross.
     */
    private Call call(JsonNode f, List<String> given, int receiver, String self, Set<String> generated,
                      String where) {
        JsonNode params = f.path("params");
        Set<String> outs = outParams(f);
        String result = resultOut(f);
        Map<String, JsonNode> arrays = new HashMap<>();
        Map<String, String> countOf = new HashMap<>();
        for (JsonNode a : f.path("shape").path("inputArrays")) {
            arrays.put(a.path("param").asText(), a);
            String n = a.path("lengthFrom").path("name").asText(null);
            if (n == null) {
                deferred.add(where + " — array " + a.path("param").asText() + " of no count");
                return null;
            }
            // Arrays sharing a count (the values and their nodata flags) have the length of the first.
            countOf.putIfAbsent(n, a.path("param").asText());
        }
        List<ObjectLayerGenerator.Arg> args = new ArrayList<>();
        List<String> call = new ArrayList<>(given);
        StringBuilder prelude = new StringBuilder();
        for (int i = given.size(); i < params.size(); i++) {
            JsonNode p = params.get(i);
            String pname = p.path("name").asText();
            String name = ObjectLayerGenerator.sanitize(pname);
            String pC = ObjectLayerGenerator.cleanType(FunctionsGenerator.slotType(p));
            if (i == receiver) {
                call.add(self);
                continue;
            }
            if (outs.contains(pname)) {
                if (ObjectLayerGenerator.isSizeOut(params, pname) || pname.equals(result)) {
                    continue;
                }
                deferred.add(where + " — out-parameter " + pname + " of type " + pC);
                return null;
            }
            if (countOf.containsKey(pname)) {
                String arr = ObjectLayerGenerator.sanitize(countOf.get(pname));
                boolean list = !arrays.get(countOf.get(pname)).path("element").path("canonical").asText()
                        .matches("int|int32_t|double|bool");
                call.add(list ? "(" + arr + " == null ? 0 : " + arr + ".size())"
                        : "(" + arr + " == null ? 0 : " + arr + ".length)");
                continue;
            }
            if (ObjectLayerGenerator.isByteInput(params, i)) {
                args.add(new ObjectLayerGenerator.Arg("byte[]", name, name));
                call.add(name);
                i++;
                continue;
            }
            if (arrays.containsKey(pname)) {
                String elem = arrays.get(pname).path("element").path("canonical").asText();
                String held = heldClass(elem, generated);
                String buf = "_" + name;
                String fill;
                if (elem.replace("const ", "").trim().equals("text *")) {
                    args.add(new ObjectLayerGenerator.Arg("java.util.List<String>", name, buf));
                    fill = buf + ".putPointer((long) _i * Long.BYTES, GeneratedFunctions.cstring_to_text("
                            + name + ".get(_i)));";
                } else if (held != null) {
                    args.add(new ObjectLayerGenerator.Arg("java.util.List<" + held + ">", name, buf));
                    fill = buf + ".putPointer((long) _i * Long.BYTES, " + name + ".get(_i).getInner());";
                } else if (elem.matches("int|int32_t|double")) {
                    String jt = elem.equals("double") ? "double" : "int";
                    String width = elem.equals("double") ? "Double.BYTES" : "Integer.BYTES";
                    args.add(new ObjectLayerGenerator.Arg(jt + "[]", name, buf));
                    prelude.append("        Pointer ").append(buf).append(" = null;\n")
                           .append("        if (").append(name).append(" != null) {\n")
                           .append("            ").append(buf).append(" = jnr.ffi.Memory.allocateDirect(")
                           .append("jnr.ffi.Runtime.getSystemRuntime(), Math.max(1, ").append(name)
                           .append(".length) * ").append(width).append(");\n")
                           .append("            ").append(buf).append(".put(0, ").append(name).append(", 0, ")
                           .append(name).append(".length);\n        }\n");
                    call.add(buf);
                    continue;
                } else if (structElement(elem, f.path("name").asText(), generated) != null) {
                    // An array of structs (the validity periods of a raster series) holds each
                    // element's bytes at the stride the catalog's layout gives, as
                    // ObjectLayerGenerator#structSize reads an array of spans back.
                    ObjectLayerGenerator.Arg e = structElement(elem, f.path("name").asText(), generated);
                    int stride = ObjectLayerGenerator.structSize(root, elem);
                    String expr = e.callExpr().replace("_e", name + ".get(_i)");
                    args.add(new ObjectLayerGenerator.Arg("java.util.List<? extends " + e.javaType() + ">",
                            name, buf));
                    prelude.append("        Pointer ").append(buf).append(" = null;\n")
                           .append("        if (").append(name).append(" != null) {\n")
                           .append("            ").append(buf).append(" = jnr.ffi.Memory.allocateDirect(")
                           .append("jnr.ffi.Runtime.getSystemRuntime(), Math.max(1, ").append(name)
                           .append(".size()) * ").append(stride).append(");\n")
                           .append("            byte[] _bytes = new byte[").append(stride).append("];\n")
                           .append("            for (int _i = 0; _i < ").append(name).append(".size(); _i++) {\n")
                           .append("                ").append(expr).append(".get(0, _bytes, 0, ").append(stride)
                           .append(");\n")
                           .append("                ").append(buf).append(".put((long) _i * ").append(stride)
                           .append(", _bytes, 0, ").append(stride).append(");\n            }\n        }\n");
                    call.add(buf);
                    continue;
                } else if (elem.equals("bool")) {
                    // A C bool is one byte.
                    args.add(new ObjectLayerGenerator.Arg("boolean[]", name, buf));
                    prelude.append("        Pointer ").append(buf).append(" = null;\n")
                           .append("        if (").append(name).append(" != null) {\n")
                           .append("            ").append(buf).append(" = jnr.ffi.Memory.allocateDirect(")
                           .append("jnr.ffi.Runtime.getSystemRuntime(), Math.max(1, ").append(name)
                           .append(".length));\n")
                           .append("            for (int _i = 0; _i < ").append(name).append(".length; _i++) {\n")
                           .append("                ").append(buf).append(".putByte(_i, (byte) (").append(name)
                           .append("[_i] ? 1 : 0));\n            }\n        }\n");
                    call.add(buf);
                    continue;
                } else {
                    deferred.add(where + " — array " + pname + " of " + elem);
                    return null;
                }
                prelude.append("        Pointer ").append(buf).append(" = null;\n")
                       .append("        if (").append(name).append(" != null) {\n")
                       .append("            ").append(buf).append(" = jnr.ffi.Memory.allocateDirect(")
                       .append("jnr.ffi.Runtime.getSystemRuntime(), Math.max(1, ").append(name)
                       .append(".size()) * Long.BYTES);\n")
                       .append("            for (int _i = 0; _i < ").append(name).append(".size(); _i++) {\n")
                       .append("                ").append(fill).append("\n            }\n        }\n");
                call.add(buf);
                continue;
            }
            String vc = valueClassOf(p);
            String held = heldClass(pC, generated);
            String t = pointee(pC);
            ObjectLayerGenerator.Arg a;
            if (vc != null) {
                a = new ObjectLayerGenerator.Arg(vc, name, name + ".value()");
            } else if (held != null) {
                a = new ObjectLayerGenerator.Arg(held, name, name + ".getInner()");
            } else if (t != null && TEMPORAL_C.contains(t)) {
                a = new ObjectLayerGenerator.Arg("types.temporal.Temporal", name, name + ".getInner()");
            } else if ("STBox".equals(t)) {
                a = new ObjectLayerGenerator.Arg("types.boxes.STBox", name, name + ".get_inner()");
            } else if (enumNames.contains(pC)) {
                a = new ObjectLayerGenerator.Arg("int", name, name);
            } else {
                a = ObjectLayerGenerator.marshalArg(pC, name, f.path("name").asText());
                if (a != null && a.javaType().equals("Temporal")) {
                    a = null;
                }
            }
            if (a == null) {
                deferred.add(where + " — argument " + name + " of type " + pC);
                return null;
            }
            args.add(a);
            call.add(a.callExpr());
        }
        return new Call(args, call, prelude.toString());
    }

    /**
     * The Java type and the statements answering what the wrapper call {@code invoke} of f returns, as
     * {@code classgen.py}'s {@code answer}: a pointer of a generated class in that class, a temporal
     * value through the hand {@code Factory}, an {@code STBox} in the hand box, the value of a single
     * out-parameter boxed ({@code null} when the function finds none); {@code null} when none fits.
     * With {@code boxed}, a primitive answer is boxed so an absent value can be stated.
     */
    private String[] answer(JsonNode f, String invoke, Set<String> generated, boolean boxed) {
        String retC = ObjectLayerGenerator.cleanType(FunctionsGenerator.slotType(f.path("returnType")));
        String result = resultOut(f);
        if (result != null) {
            String outT = "";
            for (JsonNode p : f.path("params")) {
                if (p.path("name").asText().equals(result)) {
                    outT = ObjectLayerGenerator.cleanType(FunctionsGenerator.slotType(p));
                }
            }
            String[] r = switch (outT) {
                case "int *", "int32_t *" -> new String[] {"Integer", "_r.getInt(0)"};
                case "double *" -> new String[] {"Double", "_r.getDouble(0)"};
                case "int64_t *", "uint64_t *" -> new String[] {"Long", "_r.getLong(0)"};
                default -> null;
            };
            return r == null ? null : new String[] {r[0],
                    "        Pointer _r = " + invoke + ";\n        return _r == null ? null : " + r[1] + ";\n"};
        }
        String vc = valueClassOf(f.path("returnType"));
        if (vc != null) {
            return new String[] {vc, "        return new " + vc + "(" + invoke + ");\n"};
        }
        String held = heldClass(retC, generated);
        String t = pointee(retC);
        if (held != null || "STBox".equals(t)) {
            String cls = held != null ? held : "types.boxes.STBox";
            return new String[] {cls, "        Pointer _r = " + invoke + ";\n        return _r == null ? null : new "
                    + cls + "(_r);\n"};
        }
        if (t != null && TEMPORAL_C.contains(t)) {
            return new String[] {"types.temporal.Temporal",
                    "        return types.temporal.Factory.create_temporal(" + invoke + ");\n"};
        }
        if (retC.equals("void")) {
            return new String[] {"void", "        " + invoke + ";\n"};
        }
        String java = retC.equals("uint8_t *") && !outParams(f).isEmpty() ? "byte[]"
                : retC.equals("size_t") ? "long"
                : enumNames.contains(retC) ? "int"
                : ObjectLayerGenerator.scalarReturn(retC);
        if (java == null) {
            return null;
        }
        String box = boxed ? switch (java) {
            case "boolean" -> "Boolean";
            case "int" -> "Integer";
            case "long" -> "Long";
            case "double" -> "Double";
            case "float" -> "Float";
            default -> java;
        } : java;
        return new String[] {box, "        return " + invoke + ";\n"};
    }

    /**
     * One method or operator of the object model, as {@code classgen.py}'s {@code member}: the
     * receiver is the parameter of the class's own type, the second one for an operator the model
     * reflects onto its right operand ({@code receiver: right}); a member with none is static.
     */
    private Method member(String cls, JsonNode entry, Set<String> generated) {
        String fn = entry.path("function").asText();
        String name = entry.path("ooName").asText();
        String where = cls + "." + name;
        JsonNode f = functions.get(fn);
        if (f == null || !f.path("api").asText().equals("public")) {
            deferred.add(where + " — " + fn + " is no public function");
            return null;
        }
        if (!ObjectLayerGenerator.sanitize(name).equals(name)) {
            deferred.add(where + " — a Java keyword");
            return null;
        }
        JsonNode params = f.path("params");
        int at = entry.path("receiver").asText("").equals("right") ? 1 : 0;
        JsonNode r = params.path(at);
        boolean own = !r.isMissingNode() && (isByValue(cls) ? cls.equals(valueClassOf(r))
                : cls.equals(heldClass(FunctionsGenerator.slotType(r), generated)));
        Call c = call(f, List.of(), own ? at : -1, isByValue(cls) ? "value" : "getInner()", generated, where);
        if (c == null) {
            return null;
        }
        String[] ans = answer(f, "GeneratedFunctions." + fn + "(" + String.join(", ", c.call()) + ")",
                generated, false);
        if (ans == null) {
            deferred.add(where + " — answer of " + fn);
            return null;
        }
        return new Method(name, fn, ans[0], !own, c.args(), c.prelude() + ans[1]);
    }

    /**
     * Every aggregate of the catalog over the values of the class, as {@code classgen.py}'s
     * {@code aggregates}: a static method named as the catalog names the aggregate ({@code altSqlName},
     * else {@code sqlName}) that threads the transition over the values from a {@code NULL} state into
     * the final function, which frees the state; {@code null} for no values.
     */
    private List<Method> aggregates(String cls, Set<String> generated) {
        List<Method> out = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (JsonNode agg : root.path("aggregates")) {
            JsonNode f = functions.get(agg.path("transition").path("meos").asText(""));
            JsonNode fin = functions.get(agg.path("final").path("meos").asText(""));
            if (f == null || fin == null || f.path("params").size() < 2) {
                continue;
            }
            if (!cls.equals(aggregateClass(agg, f.path("params").get(1), generated))) {
                continue;
            }
            String name = agg.path("altSqlName").asText(agg.path("sqlName").asText());
            if (!seen.add(name)) {
                continue;
            }
            String where = cls + "." + name;
            String v = isByValue(cls) ? "_v.value()" : "_v.getInner()";
            Call c = call(f, List.of("_state", v), -1, null, generated, where);
            String[] ans = c == null ? null : answer(fin, "GeneratedFunctions." + fin.path("name").asText()
                    + "(_state)", generated, true);
            if (c == null || ans == null) {
                deferred.add(where + " — aggregate " + f.path("name").asText());
                continue;
            }
            List<ObjectLayerGenerator.Arg> args = new ArrayList<>();
            args.add(new ObjectLayerGenerator.Arg("java.util.List<" + cls + ">", "values", "values"));
            args.addAll(c.args());
            String body = c.prelude()
                    + "        Pointer _state = null;\n"
                    + "        for (" + cls + " _v : values) {\n"
                    + "            _state = GeneratedFunctions." + f.path("name").asText() + "("
                    + String.join(", ", c.call()) + ");\n"
                    + "        }\n"
                    + "        if (_state == null) {\n            return null;\n        }\n"
                    + ans[1];
            out.add(new Method(name, f.path("name").asText(), ans[0], true, args, body));
        }
        return out;
    }

    /**
     * The class of an aggregate, as {@code classgen.py}'s {@code aggregate_class}: the leaf whose
     * prefix is the SQL type of its values, else the class of the C type its transition takes them as.
     */
    private String aggregateClass(JsonNode agg, JsonNode valueSlot, Set<String> generated) {
        String sql = agg.path("args").path(0).asText(null);
        Iterator<Map.Entry<String, JsonNode>> it = root.path("objectModel").path("lattice").fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> e = it.next();
            if (e.getValue().path("kind").asText().equals("leaf")) {
                for (JsonNode p : e.getValue().path("prefixes")) {
                    if (p.asText().equals(sql)) {
                        return e.getKey();
                    }
                }
            }
        }
        String vc = valueClassOf(valueSlot);
        return vc != null ? vc : heldClass(FunctionsGenerator.slotType(valueSlot), generated);
    }

    private static String header() {
        return """
            /* AUTO-GENERATED by codegen/src/main/java/ObjectClassGenerator.java from the MEOS object
             * model of meos-idl.json: do not edit by hand. */
            package %s;

            import functions.GeneratedFunctions;
            import jnr.ffi.Pointer;

            """.formatted(PACKAGE);
    }

    private static String base() {
        return header() + """
            /**
             * A handle on a value MEOS owns: the pointer every generated method forwards to the C API.
             */
            public abstract class MeosObject {

                private final Pointer inner;

                protected MeosObject(Pointer inner) {
                    this.inner = inner;
                }

                /** The pointer to the MEOS value. */
                public Pointer getInner() {
                    return inner;
                }
            }
            """;
    }

    private String classSource(String cls, String superClass, List<Method> methods, String toString) {
        StringBuilder sb = new StringBuilder(header());
        String d = doc.getOrDefault(cls, "The " + cls + " class of the MEOS object model.");
        sb.append("/**\n * ").append(d.replace("*/", "* /")).append("\n");
        if (isByValue(cls)) {
            // A value MEOS passes by value is held as that value, with no pointer to wrap: a record
            // over the type its wrappers take, whose methods hand the value over at each call.
            sb.append(" *\n * <p>MEOS passes it by value, as its C type {@code ").append(ctype.get(cls))
              .append("}: the record holds that value.\n */\n");
            sb.append("public record ").append(cls).append('(').append(valueJava.get(cls))
              .append(" value) {\n");
        } else {
            sb.append(" */\n");
            sb.append("public class ").append(cls).append(" extends ").append(superClass).append(" {\n\n");
            sb.append("    public ").append(cls).append("(Pointer inner) {\n        super(inner);\n    }\n");
        }
        for (Method m : methods) {
            if (m.body() != null) {
                // A member of the object model carries the body #member or #aggregate wrote for it.
                String params = String.join(", ", m.args().stream().map(a -> a.javaType() + " " + a.name()).toList());
                sb.append("\n    /** {@code ").append(m.name()).append("}, through MEOS {@code ").append(m.fn())
                  .append("}. */\n    public ").append(m.isStatic() ? "static " : "").append(m.javaReturn())
                  .append(' ').append(m.name()).append('(').append(params).append(") {\n")
                  .append(m.body()).append("    }\n");
                continue;
            }
            String params = String.join(", ", m.args().stream().map(a -> a.javaType() + " " + a.name()).toList());
            List<String> call = new ArrayList<>();
            if (!m.isStatic()) {
                call.add(isByValue(cls) ? "value" : "getInner()");
            }
            m.args().forEach(a -> call.add(a.callExpr()));
            String invoke = "GeneratedFunctions." + m.fn() + "(" + String.join(", ", call) + ")";
            sb.append("\n    /** {@code ").append(m.name()).append("}, through MEOS {@code ").append(m.fn())
              .append("}. */\n");
            if (isByValue(m.javaReturn())) {
                // A by-value result is the value itself, which no absent result stands for.
                sb.append("    public ").append(m.isStatic() ? "static " : "").append(m.javaReturn()).append(' ')
                  .append(m.name()).append('(').append(params).append(") {\n        return new ")
                  .append(m.javaReturn()).append('(').append(invoke).append(");\n    }\n");
            } else if (m.isStatic()) {
                sb.append("    public static ").append(cls).append(' ').append(m.name()).append('(').append(params)
                  .append(") {\n        Pointer _r = ").append(invoke).append(";\n")
                  .append("        return _r == null ? null : new ").append(cls).append("(_r);\n    }\n");
            } else {
                sb.append("    public ").append(m.javaReturn()).append(' ').append(m.name()).append('(')
                  .append(params).append(") {\n        return ").append(invoke).append(";\n    }\n");
            }
        }
        if (toString != null) {
            sb.append("\n    /** The text MEOS writes this value as. */\n")
              .append("    @Override\n    public String toString() {\n        return ").append(toString)
              .append(";\n    }\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    /** The classes the hand layer defines, by their simple names, lower-cased. */
    private static Set<String> handClasses(Path handDir) throws IOException {
        Set<String> out = new HashSet<>();
        if (!Files.isDirectory(handDir)) {
            return out;
        }
        try (Stream<Path> s = Files.walk(handDir)) {
            s.filter(p -> p.toString().endsWith(".java"))
             .forEach(p -> {
                 String n = p.getFileName().toString();
                 out.add(n.substring(0, n.length() - 5).toLowerCase(Locale.ROOT));
             });
        }
        return out;
    }

    /** The generated ancestors of the class, nearest first. */
    private List<String> generatedAncestors(String cls, Set<String> generated) {
        List<String> out = new ArrayList<>();
        String p = parent.get(cls);
        while (p != null && generated.contains(p) && !out.contains(p)) {
            out.add(p);
            p = parent.get(p);
        }
        return out;
    }

    /** The name and the Java parameter types of a method, which no two methods of a class share. */
    private static String signature(Method m) {
        return m.name() + m.args().stream().map(ObjectLayerGenerator.Arg::javaType).toList();
    }

    void run(Path outDir, Path handDir) throws IOException {
        Set<String> hand = handClasses(handDir);
        Set<String> generated = new TreeSet<>();
        root.path("objectModel").path("classes").fieldNames().forEachRemaining(c -> {
            if (!hand.contains(c.toLowerCase(Locale.ROOT))) {
                generated.add(c);
            }
        });
        Files.createDirectories(outDir);
        try (Stream<Path> s = Files.list(outDir)) {
            for (Path p : s.filter(p -> p.toString().endsWith(".java")).toList()) {
                Files.delete(p);
            }
        }
        Files.writeString(outDir.resolve("MeosObject.java"), base());
        Set<String> members = memberClasses();
        members.retainAll(generated);

        // Ancestors first, so a class sees the names its ancestors already carry.
        Map<String, Set<String>> names = new HashMap<>();
        Map<String, String> writerOf = new HashMap<>();
        List<String> order = new ArrayList<>(generated);
        order.sort(Comparator.comparingInt(c -> generatedAncestors(c, generated).size()));
        int methodCount = 0;
        for (String cls : order) {
            List<String> anc = generatedAncestors(cls, generated);
            String superClass = anc.isEmpty() ? "MeosObject" : anc.get(0);
            Set<String> taken = new HashSet<>(anc.isEmpty() ? Set.of() : names.get(anc.get(0)));
            List<Method> methods = portableMethods(cls, taken);
            if (members.contains(cls)) {
                // The methods, operators and aggregates of the object model, one per Java signature.
                Set<String> signatures = new HashSet<>();
                for (Method m : methods) {
                    signatures.add(signature(m));
                }
                List<Method> more = new ArrayList<>();
                JsonNode spec = root.path("objectModel").path("classes").path(cls);
                for (String kind : new String[] {"methods", "operators"}) {
                    for (JsonNode entry : spec.path(kind)) {
                        if (entry.path("ooExclude").asBoolean(false)) {
                            continue;
                        }
                        Method m = member(cls, entry, generated);
                        if (m != null) {
                            more.add(m);
                        }
                    }
                }
                more.addAll(aggregates(cls, generated));
                for (Method m : more) {
                    if (signatures.add(signature(m))) {
                        methods.add(m);
                        taken.add(m.name());
                    } else if (methods.stream().noneMatch(o -> o.fn().equals(m.fn()))) {
                        deferred.add(cls + "." + m.name() + " — " + m.fn() + " has the Java signature of another member");
                    }
                }
            }
            names.put(cls, taken);
            methodCount += methods.size();

            String inherited = anc.isEmpty() ? null : writerOf.get(anc.get(0));
            String writer = textWriter(cls);
            if (writer == null) {
                writer = inherited;
            }
            writerOf.put(cls, writer);
            String toString = null;
            String w = writer;
            Optional<Method> asText = methods.stream()
                    .filter(m -> !m.isStatic() && m.fn().equals(w)).findFirst();
            if (asText.isPresent() && !Objects.equals(writer, inherited)) {
                Method m = asText.get();
                if (m.args().isEmpty()) {
                    toString = m.name() + "()";
                } else if (m.args().size() == 1 && m.args().get(0).javaType().equals("int")
                        && defaultDigits(cls) != null) {
                    toString = m.name() + "(" + defaultDigits(cls) + ")";
                }
            }
            Files.writeString(outDir.resolve(cls + ".java"), classSource(cls, superClass, methods, toString));
        }
        System.out.println("ObjectClassGenerator: " + generated.size() + " classes, " + methodCount
                + " methods, " + deferred.size() + " deferred");
        for (String d : deferred) {
            System.out.println("  deferred: " + d);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("usage: ObjectClassGenerator <meos-idl.json> <output dir> <hand types dir>"
                    + " [<family>,...]");
            System.exit(2);
        }
        JsonNode root = new ObjectMapper().readTree(new File(args[0]));
        ObjectClassGenerator g = new ObjectClassGenerator(root);
        if (args.length > 3) {
            g.memberFamilies.addAll(Arrays.asList(args[3].split(",")));
        }
        g.run(Paths.get(args[1]), Paths.get(args[2]));
    }
}
