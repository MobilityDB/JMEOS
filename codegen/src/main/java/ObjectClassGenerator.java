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
 * <p>Usage: {@code ObjectClassGenerator <meos-idl.json> <output dir> <hand types dir>}
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
                          List<ObjectLayerGenerator.Arg> args) {}

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
        build();
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
                + " readers and writers, " + deferred.size() + " deferred");
        for (String d : deferred) {
            System.out.println("  deferred: " + d);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("usage: ObjectClassGenerator <meos-idl.json> <output dir> <hand types dir>");
            System.exit(2);
        }
        JsonNode root = new ObjectMapper().readTree(new File(args[0]));
        new ObjectClassGenerator(root).run(Paths.get(args[1]), Paths.get(args[2]));
    }
}
