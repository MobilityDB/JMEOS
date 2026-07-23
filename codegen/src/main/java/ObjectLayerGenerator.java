import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Generates the object-oriented accessor surface of the JMEOS type layer from the
 * MEOS object model in {@code meos-idl.json}.
 *
 * <p>The object model ({@code objectModel.classes.<Class>.methods}) already classifies every MEOS
 * function against the class it belongs to and carries its canonical camelCase name ({@code ooName}).
 * This generator projects that classification onto Java: for one class it emits an interface of
 * {@code default} methods, each delegating to the matching {@code functions.GeneratedFunctions}
 * wrapper with the receiver supplied by the object's {@code getInner()} and the result wrapped back
 * into the object layer. The interface requires only the three contract methods the hand
 * {@code Temporal} already exposes ({@code getInner}, {@code getCustomType}, {@code getTemporalType}),
 * so a hand class adopts the generated surface by implementing the interface — the two coexist
 * because the generated names are the canonical camelCase spelling and the hand names are the older
 * snake_case spelling.
 *
 * <p>This first slice covers the {@code Temporal} superclass, roles {@code accessor} and
 * {@code conversion}, restricted to the methods whose marshalling is a pure receiver + scalar-argument
 * delegation returning a scalar, a string, a timestamp, or a temporal value. Methods needing the
 * marshalling patterns {@code FunctionsGenerator} already encodes elsewhere (array folding via a count
 * out-parameter, collection/box wrapping, object arguments, the {@code bool}+result pattern, the
 * 1-based index adjustment) are reported and left for the next slice rather than emitted half-formed.
 */
public class ObjectLayerGenerator {

    /** The class whose OO surface this run emits. */
    private static final String CLASS = "Temporal";

    /** Roles included in this slice. */
    private static final Set<String> ROLES = Set.of("accessor", "conversion");

    /** Enum type names from the catalog; a param of one of these maps to a Java int in the surface. */
    private final Set<String> enumNames = new HashSet<>();

    /** Functions keyed by name, for return type and parameter lookup. */
    private final Map<String, JsonNode> functions = new HashMap<>();

    /** Methods left out of this slice, with the reason, reported at the end. */
    private final List<String> deferred = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) throws IOException {
        String base = System.getProperty("user.dir");
        String inputPath  = args.length > 0 ? args[0]
                : base + "/codegen/input/meos-idl.json";
        String outputPath = args.length > 1 ? args[1]
                : base + "/jmeos-core/src/main/java/types/temporal/generated/GeneratedTemporal.java";
        new ObjectLayerGenerator().run(inputPath, outputPath);
    }

    private void run(String inputPath, String outputPath) throws IOException {
        System.out.println("=== ObjectLayerGenerator (" + CLASS + ") ===");
        System.out.println("Input : " + inputPath);
        System.out.println("Output: " + outputPath);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(inputPath));
        collectEnumNames(root);
        indexFunctions(root);

        JsonNode classNode = root.path("objectModel").path("classes").path(CLASS);
        if (classNode.isMissingNode()) {
            throw new IllegalStateException("objectModel.classes." + CLASS + " not found in " + inputPath);
        }

        List<Method> methods = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (JsonNode m : classNode.path("methods")) {
            if (m.path("ooExclude").asBoolean(false)) {
                continue; // internal-API function, not part of the public OO surface
            }
            if (!ROLES.contains(m.path("role").asText(""))) {
                continue;
            }
            Method method = classify(m);
            if (method == null) {
                continue; // deferred, already recorded
            }
            if (seen.add(method.ooName)) {
                methods.add(method);
            }
        }
        methods.sort(Comparator.comparing(x -> x.ooName));

        String content = generateInterface(methods);
        Path out = Paths.get(outputPath);
        Files.createDirectories(out.getParent());
        Files.writeString(out, content);

        System.out.println("Methods emitted: " + methods.size());
        System.out.println("Deferred to the next slice (" + deferred.size() + "):");
        deferred.stream().sorted().forEach(d -> System.out.println("   " + d));
        System.out.println("Written: " + out.toAbsolutePath());
        System.out.println("=== Done ===");
    }

    // -------------------------------------------------------------------------
    // Catalog indexing
    // -------------------------------------------------------------------------

    private void collectEnumNames(JsonNode root) {
        for (JsonNode e : root.path("enums")) {
            JsonNode n = e.get("name");
            if (n != null) {
                enumNames.add(n.asText());
            }
        }
    }

    private void indexFunctions(JsonNode root) {
        for (JsonNode f : root.path("functions")) {
            JsonNode n = f.get("name");
            if (n != null) {
                functions.putIfAbsent(n.asText(), f);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Classification: which methods this slice can emit, and how
    // -------------------------------------------------------------------------

    /** A method the surface emits: its canonical name, the backing function, and how to marshal it. */
    private record Method(String ooName, String fnName, String returnType, String returnSubtype,
                          List<Arg> args) {}

    /** One argument passed straight through to the wrapper (scalar only in this slice). */
    private record Arg(String javaType, String name) {}

    /**
     * Classifies one object-model method. Returns a {@link Method} the slice can emit, or {@code null}
     * after recording why it was deferred. The rules keep the emitted set to pure receiver + scalar
     * delegation: the receiver must be the single-pointer class type ({@code getInner()} supplies it),
     * the return must be a scalar/string/timestamp/temporal value, and every remaining argument must be
     * a scalar. Anything else is a marshalling pattern for a later slice.
     */
    private Method classify(JsonNode m) {
        String ooName = m.path("ooName").asText();
        String fnName = m.path("function").asText();
        JsonNode fn = functions.get(fnName);
        if (fn == null) {
            defer(ooName, "no catalog function " + fnName);
            return null;
        }
        JsonNode params = fn.path("params");
        if (!params.isArray() || params.size() == 0) {
            defer(ooName, "no receiver parameter");
            return null;
        }
        // Receiver: the first parameter must be a single pointer to the class type, so getInner()
        // (a Temporal*) is the argument. A double pointer (e.g. temporal_merge_array's Temporal**)
        // is an array, not a receiver.
        String recv = cleanType(params.get(0).path("cType").asText());
        if (!recv.equals("Temporal *")) {
            defer(ooName, "receiver is " + recv + ", not Temporal *");
            return null;
        }
        JsonNode shape = fn.path("shape");
        if (shape.path("outParams").isArray() && shape.path("outParams").size() > 0) {
            defer(ooName, "has out-parameter(s) — array/bool+result folding");
            return null;
        }

        String retC = cleanType(fn.path("returnType").path("c").asText());
        String returnType = temporalReturn(retC) != null ? "Temporal" : scalarReturn(retC);
        if (returnType == null) {
            defer(ooName, "return type " + retC + " needs collection/box/struct wrapping");
            return null;
        }
        String returnSubtype = temporalReturn(retC);

        // 1-based index adjustment (instant_n/sequence_n): the hand layer presents 0-based indexing
        // by adding one before the call. That is a semantic decision, not a mechanical marshal, so it
        // is deferred rather than guessed here.
        boolean indexed = false;

        List<Arg> args = new ArrayList<>();
        for (int i = 1; i < params.size(); i++) {
            JsonNode p = params.get(i);
            String pC = cleanType(p.path("cType").asText());
            String name = sanitize(p.path("name").asText());
            if ((name.equals("n") || name.equals("i")) && ooName.endsWith("N")) {
                indexed = true;
            }
            String javaType = scalarArg(pC);
            if (javaType == null) {
                defer(ooName, "argument " + name + " of type " + pC + " is not a scalar");
                return null;
            }
            args.add(new Arg(javaType, name));
        }
        if (indexed) {
            defer(ooName, "1-based index argument needs a base decision");
            return null;
        }
        return new Method(ooName, fnName, returnType, returnSubtype, args);
    }

    /** The {@code TemporalType} constant for a temporal return, or {@code null} if not a temporal. */
    private static String temporalReturn(String retC) {
        return switch (retC) {
            case "Temporal *"       -> "getTemporalType()";
            case "TInstant *"       -> "TEMPORAL_INSTANT";
            case "TSequence *"      -> "TEMPORAL_SEQUENCE";
            case "TSequenceSet *"   -> "TEMPORAL_SEQUENCE_SET";
            default                 -> null;
        };
    }

    /**
     * The Java return type of the {@code GeneratedFunctions} wrapper for a scalar/string/timestamp
     * return, matching {@code FunctionsGenerator.mapCTypeToJavaWrapper}, or {@code null} if the return
     * is not one this slice returns directly.
     */
    private static String scalarReturn(String retC) {
        return switch (retC) {
            case "bool"                              -> "boolean";
            case "int", "int32", "int32_t",
                 "uint32", "uint32_t"                -> "int";
            case "long", "int64", "int64_t",
                 "uint64", "uint64_t"                -> "long";
            case "double", "float8"                  -> "double";
            case "float"                             -> "float";
            case "char *"                            -> "String";
            case "TimestampTz"                       -> "java.time.OffsetDateTime";
            default                                  -> null;
        };
    }

    /** The Java type of a scalar argument passed straight to the wrapper, or {@code null} otherwise. */
    private static String scalarArg(String pC) {
        return switch (pC) {
            case "bool"                              -> "boolean";
            case "int", "int32", "int32_t",
                 "uint32", "uint32_t"                -> "int";
            case "long", "int64", "int64_t",
                 "uint64", "uint64_t"                -> "long";
            case "double", "float8"                  -> "double";
            case "float"                             -> "float";
            default                                  -> null;
        };
    }

    private void defer(String ooName, String reason) {
        deferred.add(ooName + " — " + reason);
    }

    private static String cleanType(String c) {
        return c.replace("const ", "").trim();
    }

    // -------------------------------------------------------------------------
    // Emission
    // -------------------------------------------------------------------------

    private String generateInterface(List<Method> methods) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                package types.temporal.generated;

                import functions.GeneratedFunctions;
                import jnr.ffi.Pointer;
                import types.temporal.Factory;
                import types.temporal.Temporal;
                import types.temporal.TemporalType;

                import static types.temporal.TemporalType.*;

                /**
                 * Generated OO accessor/conversion surface for {@link Temporal}.
                 *
                 * <p>Derived from the MEOS object model (objectModel.classes.Temporal, roles accessor and
                 * conversion) by ObjectLayerGenerator — do not edit by hand. Each method carries the
                 * canonical camelCase name from the object model and delegates to the matching
                 * GeneratedFunctions wrapper, supplying the receiver through getInner() and wrapping a
                 * temporal result back through Factory. A class adopts the surface by implementing this
                 * interface; the hand Temporal already provides the three contract methods.
                 */
                public interface GeneratedTemporal {

                \tPointer getInner();

                \tString getCustomType();

                \tTemporalType getTemporalType();

                """);
        for (Method m : methods) {
            sb.append(generateMethod(m));
        }
        sb.append("}\n");
        return sb.toString();
    }

    private String generateMethod(Method m) {
        StringJoiner sig = new StringJoiner(", ");
        StringJoiner call = new StringJoiner(", ");
        call.add("getInner()");
        for (Arg a : m.args) {
            sig.add(a.javaType + " " + a.name);
            call.add(a.name);
        }
        String invocation = "GeneratedFunctions." + m.fnName + "(" + call + ")";
        String body;
        if (m.returnType.equals("Temporal")) {
            body = "\t\treturn Factory.create_temporal(" + invocation
                    + ", getCustomType(), " + m.returnSubtype + ");\n";
        } else {
            body = "\t\treturn " + invocation + ";\n";
        }
        return "\tdefault " + m.returnType + " " + m.ooName + "(" + sig + ") {\n"
                + body
                + "\t}\n\n";
    }

    // -------------------------------------------------------------------------
    // Java-keyword-safe parameter names (mirrors FunctionsGenerator)
    // -------------------------------------------------------------------------

    private static final Set<String> JAVA_KEYWORDS = Set.of(
            "abstract","assert","boolean","break","byte","case","catch","char",
            "class","const","continue","default","do","double","else","enum",
            "extends","final","finally","float","for","goto","if","implements",
            "import","instanceof","int","interface","long","native","new",
            "package","private","protected","public","return","short","static",
            "strictfp","super","switch","synchronized","this","throw","throws",
            "transient","try","void","volatile","while"
    );

    private static String sanitize(String name) {
        if (name.equals("synchronized")) {
            return "synchronize";
        }
        return JAVA_KEYWORDS.contains(name) ? name + "_param" : name;
    }
}
