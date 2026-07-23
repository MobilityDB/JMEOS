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

    /**
     * One generated interface: the object-model class it projects, the interface name it emits, the
     * interface it extends (a subclass surface extends its superclass surface, so inherited methods are
     * not re-emitted), and whether a {@code Span}/{@code SpanSet} return is a time extent. A Temporal's
     * span is its time domain (a tstz span); a TNumber's is its value domain, whose concrete span type
     * is base-dependent, so those value-span returns belong to the concrete subclass surfaces.
     */
    private record ClassSpec(String objectKey, String interfaceName, String superInterface,
                             boolean spanReturnsAreTime) {}

    /** The interfaces this generator emits, superclass before subclass. */
    private static final List<ClassSpec> SPECS = List.of(
            new ClassSpec("Temporal", "GeneratedTemporal", null, true),
            new ClassSpec("TNumber", "GeneratedTNumber", "GeneratedTemporal", false),
            new ClassSpec("TInt", "GeneratedTInt", "GeneratedTNumber", false),
            new ClassSpec("TFloat", "GeneratedTFloat", "GeneratedTNumber", false),
            new ClassSpec("TBigint", "GeneratedTBigint", "GeneratedTNumber", false));

    /** The interface this run emits. */
    private ClassSpec spec;

    /** The object-model roles this surface generates. */
    private static final Set<String> ROLES =
            Set.of("accessor", "conversion", "predicate", "restriction", "output", "constructor");

    /** Enum type names from the catalog; a param of one of these maps to a Java int in the surface. */
    private final Set<String> enumNames = new HashSet<>();

    /** Functions keyed by name, for return type and parameter lookup. */
    private final Map<String, JsonNode> functions = new HashMap<>();

    /** Methods left out of this slice, with the reason, reported at the end. */
    private final List<String> deferred = new ArrayList<>();

    /** Size in bytes of the {@code Span} struct, the stride of a {@code Span *} array. */
    private int spanSize;

    /** The {@code Match} struct's fields and stride, folded into a record over a {@code Match *} array. */
    private List<StructField> matchFields;
    private int matchStride;

    /** Size in bytes of the {@code TBox} struct, the stride of a {@code TBox *} array. */
    private int tboxStride;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) throws IOException {
        String base = System.getProperty("user.dir");
        String inputPath  = args.length > 0 ? args[0]
                : base + "/codegen/input/meos-idl.json";
        // The second argument names the GeneratedTemporal.java output; its parent directory receives every
        // generated interface (one file per ClassSpec).
        Path outDir = args.length > 1 ? Paths.get(args[1]).getParent()
                : Paths.get(base, "jmeos-core/src/main/java/types/temporal/generated");
        System.out.println("=== ObjectLayerGenerator ===");
        System.out.println("Input : " + inputPath);
        System.out.println("Output: " + outDir);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(inputPath));
        ObjectLayerGenerator generator = new ObjectLayerGenerator();
        generator.init(root);
        for (ClassSpec s : SPECS) {
            generator.run(s, root, outDir.resolve(s.interfaceName() + ".java"), inputPath);
        }
    }

    /** Index the catalog once: enum names, functions, and the struct strides the folds read. */
    private void init(JsonNode root) {
        collectEnumNames(root);
        indexFunctions(root);
        spanSize = structSize(root, "Span");
        matchFields = structFields(root, "Match");
        matchStride = structSize(root, "Match");
        tboxStride = structSize(root, "TBox");
    }

    private void run(ClassSpec spec, JsonNode root, Path out, String inputPath) throws IOException {
        this.spec = spec;
        deferred.clear();
        System.out.println("--- " + spec.interfaceName() + " (objectModel.classes." + spec.objectKey() + ") ---");

        JsonNode classNode = root.path("objectModel").path("classes").path(spec.objectKey());
        if (classNode.isMissingNode()) {
            throw new IllegalStateException("objectModel.classes." + spec.objectKey() + " not found in " + inputPath);
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
        Files.createDirectories(out.getParent());
        Files.writeString(out, content);

        System.out.println("Methods emitted: " + methods.size());
        System.out.println("Deferred (" + deferred.size() + "):");
        deferred.stream().sorted().forEach(d -> System.out.println("   " + d));
        System.out.println("Written: " + out.toAbsolutePath());
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

    /**
     * Size in bytes of a catalog struct laid out by the C rules on the 64-bit targets the binding ships
     * for: each field sits at a multiple of its own alignment, and the whole is padded to a multiple of
     * its widest field. This is the stride of an array of that struct.
     */
    private static int structSize(JsonNode root, String name) {
        int offset = 0;
        int widest = 1;
        for (JsonNode f : structOf(root, name).path("fields")) {
            int[] sa = sizeAlign(root, f.path("cType").asText().trim());
            widest = Math.max(widest, sa[1]);
            offset = (offset + sa[1] - 1) / sa[1] * sa[1] + sa[0];
        }
        return (offset + widest - 1) / widest * widest;
    }

    /** The alignment in bytes of a catalog struct: the widest alignment among its fields. */
    private static int structAlign(JsonNode root, String name) {
        int widest = 1;
        for (JsonNode f : structOf(root, name).path("fields")) {
            widest = Math.max(widest, sizeAlign(root, f.path("cType").asText().trim())[1]);
        }
        return widest;
    }

    /**
     * The {@code {size, alignment}} in bytes of a struct field's C type: a scalar, a fixed-size array, or
     * a nested catalog struct (a TBox nests two Spans), resolved recursively.
     */
    private static int[] sizeAlign(JsonNode root, String cType) {
        cType = cType.replace("const ", "").trim();
        int bracket = cType.indexOf('[');
        if (bracket >= 0) { // a fixed-size array field, e.g. char[4]
            int[] base = sizeAlign(root, cType.substring(0, bracket).trim());
            int count = Integer.parseInt(cType.substring(bracket + 1, cType.indexOf(']')).trim());
            return new int[]{base[0] * count, base[1]};
        }
        if (isStruct(root, cType)) {
            return new int[]{structSize(root, cType), structAlign(root, cType)};
        }
        int size = scalarBytes(cType);
        return new int[]{size, size};
    }

    /** Whether the catalog defines a struct of this name. */
    private static boolean isStruct(JsonNode root, String name) {
        for (JsonNode s : root.path("structs")) {
            if (s.path("name").asText().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The scalar fields of a catalog struct with their byte offsets, for folding an array of that struct
     * into a record. A fixed-size array field (padding) contributes to the offsets but is not a record
     * field; a scalar field whose type the record cannot model is an error, never a silent skip.
     */
    private static List<StructField> structFields(JsonNode root, String name) {
        List<StructField> fields = new ArrayList<>();
        int offset = 0;
        for (JsonNode f : structOf(root, name).path("fields")) {
            String cType = f.path("cType").asText().trim();
            int bracket = cType.indexOf('[');
            if (bracket >= 0) { // a fixed-size array field (padding); contributes to the offset only
                int base = scalarBytes(cType.substring(0, bracket).trim());
                int count = Integer.parseInt(cType.substring(bracket + 1, cType.indexOf(']')).trim());
                offset = (offset + base - 1) / base * base + base * count;
                continue;
            }
            int align = scalarBytes(cType);
            offset = (offset + align - 1) / align * align;
            fields.add(new StructField(f.path("name").asText(), fieldJavaType(cType), fieldGetter(cType), offset));
            offset += align;
        }
        return fields;
    }

    private static JsonNode structOf(JsonNode root, String name) {
        for (JsonNode s : root.path("structs")) {
            if (s.path("name").asText().equals(name)) {
                return s;
            }
        }
        throw new IllegalStateException("struct " + name + " not found in the catalog");
    }

    /** One scalar struct field: its name, the Java type it maps to, its jnr reader, and its byte offset. */
    private record StructField(String name, String javaType, String getter, int offset) {}

    /** Size in bytes of a scalar C type; an unhandled type is an error. */
    private static int scalarBytes(String cType) {
        cType = cType.replace("const ", "").trim();
        if (cType.endsWith("*")) {
            return 8;
        }
        return switch (cType) {
            case "bool", "char", "int8", "int8_t", "uint8", "uint8_t" -> 1;
            case "short", "int16", "int16_t", "uint16", "uint16_t" -> 2;
            case "int", "int32", "int32_t", "uint32", "uint32_t", "float", "Oid", "DateADT" -> 4;
            case "long", "int64", "int64_t", "uint64", "uint64_t", "double", "float8",
                 "Datum", "Timestamp", "TimestampTz", "TimeADT", "size_t", "uintptr_t" -> 8;
            default -> throw new IllegalStateException("unhandled struct field type: " + cType);
        };
    }

    /** The Java type of a scalar struct field; an unhandled type is an error. */
    private static String fieldJavaType(String cType) {
        return switch (cType.replace("const ", "").trim()) {
            case "int", "int32", "int32_t", "uint32", "uint32_t" -> "int";
            case "long", "int64", "int64_t", "uint64", "uint64_t" -> "long";
            case "double", "float8" -> "double";
            case "float" -> "float";
            default -> throw new IllegalStateException("unmodelled record field type: " + cType);
        };
    }

    /** The jnr {@code Pointer} reader for a scalar struct field; an unhandled type is an error. */
    private static String fieldGetter(String cType) {
        return switch (cType.replace("const ", "").trim()) {
            case "int", "int32", "int32_t", "uint32", "uint32_t" -> "getInt";
            case "long", "int64", "int64_t", "uint64", "uint64_t" -> "getLong";
            case "double", "float8" -> "getDouble";
            case "float" -> "getFloat";
            default -> throw new IllegalStateException("unmodelled record field type: " + cType);
        };
    }

    // -------------------------------------------------------------------------
    // Classification: which methods this slice can emit, and how
    // -------------------------------------------------------------------------

    /** A method the surface emits: its canonical name, the backing function, and how to marshal it. */
    private record Method(String ooName, String fnName, String returnType, String returnKind,
                          String returnSubtype, List<Arg> args, List<SplitKey> splitKeys) {
        /** A method with no split-key out-parameters (every kind but {@code split}). */
        Method(String ooName, String fnName, String returnType, String returnKind,
               String returnSubtype, List<Arg> args) {
            this(ooName, fnName, returnType, returnKind, returnSubtype, args, List.of());
        }
    }

    /**
     * One argument of an emitted method: the Java type the caller passes, its name, and the
     * expression forwarded to the wrapper (the name itself for a pass-through, or a conversion).
     */
    private record Arg(String javaType, String name, String callExpr) {}

    /**
     * One bin dimension of a split. A split's backing function fills one bin-start out-parameter array
     * per grouping dimension (value, space, time), all parallel to the {@code Temporal **} fragment
     * array, so a split of D dimensions yields a record of D bin components plus the fragment.
     * {@code fieldName} and {@code javaType} are the record component's name and type — aligned to the
     * SQL composite the PostgreSQL table function returns ({@code time_tX(time, temp)},
     * {@code number_time_tX(number, time, tnumber)}, {@code point_tgeo(point, tgeo)}) — and
     * {@code readerExpr} reads element {@code _i} of the bin array whose pointer is spelled {@code $arr}.
     */
    private record SplitKey(String fieldName, String javaType, String readerExpr) {}

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
            // A constructor over an array of temporals (merge_array) takes a List, writes each element's
            // inner pointer into a buffer and passes the size as the count.
            if (recv.equals("Temporal **") && params.size() == 2
                    && params.get(1).path("name").asText().equals("count")
                    && cleanType(fn.path("returnType").path("c").asText()).equals("Temporal *")) {
                Arg list = new Arg("java.util.List<Temporal>",
                        sanitize(params.get(0).path("name").asText()), null);
                return new Method(ooName, fnName, "Temporal", "arrayInput", null, List.of(list));
            }
            // A constructor builds a Temporal from other inputs (a hex string, a WKB buffer) rather than
            // from a receiver, so every parameter is an argument, the call omits getInner(), and the
            // result is wrapped with the type contract — symmetric to the as* output methods. It stays
            // an instance method.
            if (cleanType(fn.path("returnType").path("c").asText()).equals("Temporal *")) {
                List<Arg> ctorArgs = new ArrayList<>();
                boolean marshalled = true;
                for (JsonNode p : params) {
                    Arg a = marshalArg(cleanType(p.path("cType").asText()),
                            sanitize(p.path("name").asText()), fnName);
                    if (a == null) {
                        marshalled = false;
                        break;
                    }
                    ctorArgs.add(a);
                }
                if (marshalled) {
                    return new Method(ooName, fnName, "Temporal", "factory", null, ctorArgs);
                }
            }
            defer(ooName, "receiver is " + recv + ", not Temporal *");
            return null;
        }
        List<String> outParams = new ArrayList<>();
        for (JsonNode o : fn.path("shape").path("outParams")) {
            outParams.add(o.asText());
        }
        String retC = cleanType(fn.path("returnType").path("c").asText());

        // Array-fold: a trailing `count` out-parameter alongside an array return folds to a List. The
        // fold forwards the receiver, the visible arguments and the count buffer, then reads each element
        // by the array's kind — a pointer to a temporal, a timestamp value, or a span struct at its
        // stride. A second out-parameter (timeSplit's bins) still stays for a later slice.
        String arrayElement = arrayElementSubtype(retC);
        String arrayKind = arrayElement != null ? "objectArray"
                : retC.equals("TimestampTz *") ? "scalarArray"
                : retC.equals("Span *") && spec.spanReturnsAreTime() ? "spanArray"
                : retC.equals("TBox *") ? "tboxArray"
                : retC.equals("Match *") ? "matchArray"
                : null;
        int last = params.size() - 1;
        if (arrayKind != null && outParams.equals(List.of("count"))
                && last >= 1 && params.get(last).path("name").asText().equals("count")) {
            List<Arg> foldArgs = new ArrayList<>();
            boolean marshalled = true;
            for (int i = 1; i < last; i++) {
                JsonNode p = params.get(i);
                Arg a = marshalArg(cleanType(p.path("cType").asText()),
                        sanitize(p.path("name").asText()), fnName);
                if (a == null) {
                    marshalled = false;
                    break;
                }
                foldArgs.add(a);
            }
            if (marshalled) {
                String rt = switch (arrayKind) {
                    case "objectArray" -> "java.util.List<Temporal>";
                    case "scalarArray" -> "java.util.List<java.time.OffsetDateTime>";
                    case "spanArray"   -> "java.util.List<types.collections.time.tstzspan>";
                    case "tboxArray"   -> "java.util.List<types.boxes.TBox>";
                    default            -> "java.util.List<Match>";
                };
                return new Method(ooName, fnName, rt, arrayKind, arrayElement, foldArgs);
            }
        }
        // Split-fold: a temporal-array return with one or more bin-start out-parameters before the
        // trailing `count` folds to a List of records, each pairing the per-fragment bin start(s) with
        // the fragment. This is the split family: the backing function fills one bin array per grouping
        // dimension (value, space, time), all parallel to the fragment array. This slice supports the
        // time dimension (timeSplit, the only split on the generic Temporal); the value and space
        // dimensions of the numeric/spatial subclasses extend the bin-type map in splitKey.
        if (arrayElement != null && outParams.size() >= 2
                && outParams.get(outParams.size() - 1).equals("count")) {
            List<String> binOut = outParams.subList(0, outParams.size() - 1);
            // The out-parameters must be the trailing parameters, so the wrapper call is receiver +
            // visible arguments + bin buffers + count, in order.
            int minOut = params.size();
            int maxVisible = 0;
            for (int i = 1; i < params.size(); i++) {
                if (outParams.contains(params.get(i).path("name").asText())) {
                    minOut = Math.min(minOut, i);
                } else {
                    maxVisible = Math.max(maxVisible, i);
                }
            }
            List<SplitKey> keys = new ArrayList<>();
            for (String o : binOut) {
                SplitKey k = splitKey(paramCType(params, o));
                if (k == null) {
                    keys = null;
                    break;
                }
                keys.add(k);
            }
            if (maxVisible < minOut && keys != null) {
                List<Arg> foldArgs = new ArrayList<>();
                boolean marshalled = true;
                for (int i = 1; i < minOut; i++) {
                    JsonNode p = params.get(i);
                    Arg a = marshalArg(cleanType(p.path("cType").asText()),
                            sanitize(p.path("name").asText()), fnName);
                    if (a == null) {
                        marshalled = false;
                        break;
                    }
                    foldArgs.add(a);
                }
                if (marshalled) {
                    return new Method(ooName, fnName, "java.util.List<" + capitalize(ooName) + ">",
                            "split", arrayElement, foldArgs, keys);
                }
            }
            defer(ooName, "split bins " + binOut + " — unsupported bin dimension / non-trailing / argument marshalling");
            return null;
        }
        // A size_t* out-parameter is the throwaway byte-count of the *_as_wkb / *_as_hexwkb family; the
        // wrapper allocates, forwards and discards it, so the generated method never passes it.
        List<String> unfolded = new ArrayList<>();
        for (String o : outParams) {
            if (!isSizeOut(params, o)) {
                unfolded.add(o);
            }
        }
        // bool+result: a boolean function with a single value out-parameter folds to that value (or null
        // when the boolean is false); the wrapper allocates the buffer, forwards it and returns it.
        String resultOut = retC.equals("bool") && unfolded.size() == 1 ? unfolded.get(0) : null;
        if (resultOut == null && !unfolded.isEmpty()) {
            defer(ooName, "out-parameter(s) " + unfolded + " — array/bool+result folding");
            return null;
        }

        String returnType;
        String returnKind;
        String returnSubtype = temporalReturn(retC);
        if (resultOut != null) {
            if (!paramCType(params, resultOut).equals("TimestampTz *")) {
                defer(ooName, "bool+result of " + paramCType(params, resultOut) + " needs a converter");
                return null;
            }
            returnType = "java.time.OffsetDateTime";
            returnKind = "boolResult";
            returnSubtype = "utils.TimestampTzConverter.toOffsetDateTime(_r.getLong(0))";
        } else if (returnSubtype != null) {
            returnType = "Temporal";
            returnKind = "temporal";
        } else if (retC.equals("Interval *")) {
            returnType = "java.time.Duration";
            returnKind = "interval";
        } else if (scalarReturn(retC) != null) {
            returnType = scalarReturn(retC);
            returnKind = "direct";
        } else if (retC.equals("Span *") && spec.spanReturnsAreTime()) {
            // A Temporal's span is its time extent, a tstzspan; a value span waits for the concrete subclass.
            returnType = "types.collections.time.tstzspan";
            returnKind = "tstzspan";
        } else if (retC.equals("SpanSet *") && spec.spanReturnsAreTime()) {
            // A Temporal's spanset is its time domain, a tstzspanset; a value spanset waits for the subclass.
            returnType = "types.collections.time.tstzspanset";
            returnKind = "tstzspanset";
        } else if (retC.equals("TBox *")) {
            // A number temporal's bounding box over its value and time extents.
            returnType = "types.boxes.TBox";
            returnKind = "tbox";
        } else if (retC.equals("uint8_t *")) {
            // The WKB byte buffer, returned as a raw pointer (its length is folded away by the wrapper).
            returnType = "Pointer";
            returnKind = "direct";
        } else {
            defer(ooName, "return type " + retC + " needs collection/box/struct wrapping");
            return null;
        }

        List<Arg> args = new ArrayList<>();
        for (int i = 1; i < params.size(); i++) {
            JsonNode p = params.get(i);
            if (outParams.contains(p.path("name").asText())) {
                continue; // a folded size_t* out-param the wrapper supplies itself
            }
            String pC = cleanType(p.path("cType").asText());
            String name = sanitize(p.path("name").asText());
            Arg arg = marshalArg(pC, name, fnName);
            if (arg == null) {
                defer(ooName, "argument " + name + " of type " + pC + " needs object/collection marshalling");
                return null;
            }
            // The *_n accessors index from one in MEOS; the surface exposes zero-based indexing, like
            // the hand layer, by forwarding the argument plus one.
            if ((name.equals("n") || name.equals("i")) && ooName.endsWith("N")) {
                arg = new Arg(arg.javaType(), arg.name(), arg.name() + " + 1");
            }
            args.add(arg);
        }
        return new Method(ooName, fnName, returnType, returnKind, returnSubtype, args);
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
     * The {@code TemporalType} constant for the elements of a temporal-array return ({@code T **}), or
     * {@code null} if the return is not such an array.
     */
    private static String arrayElementSubtype(String retC) {
        return switch (retC) {
            case "Temporal **"      -> "getTemporalType()";
            case "TInstant **"      -> "TEMPORAL_INSTANT";
            case "TSequence **"     -> "TEMPORAL_SEQUENCE";
            case "TSequenceSet **"  -> "TEMPORAL_SEQUENCE_SET";
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

    /**
     * How to marshal one argument to the wrapper, or {@code null} if it needs a pattern this slice does
     * not emit (an object/collection pointer). A scalar and a {@code TimestampTz} pass straight through
     * (the wrapper takes the primitive resp. the {@code OffsetDateTime}); an {@code interpType} and an
     * {@code Interval *} convert through the same helpers the hand layer uses. A set/span/spanset
     * argument is a time wrapper when the method restricts by time (its backing function names the tstz
     * type) and the generic collection over the base value otherwise.
     */
    private static Arg marshalArg(String pC, String name, String fnName) {
        String scalar = switch (pC) {
            case "bool"                              -> "boolean";
            case "int8", "int8_t",
                 "uint8", "uint8_t"                  -> "byte";
            case "int", "int32", "int32_t",
                 "uint32", "uint32_t"                -> "int";
            case "long", "int64", "int64_t",
                 "uint64", "uint64_t",
                 "size_t", "uintptr_t"               -> "long";
            case "double", "float8"                  -> "double";
            case "float"                             -> "float";
            default                                  -> null;
        };
        if (scalar != null) {
            return new Arg(scalar, name, name);
        }
        boolean time = fnName.contains("tstz");
        return switch (pC) {
            case "interpType"  -> new Arg("TInterpolation", name, name + ".getValue()");
            // A raw byte buffer (a WKB) is passed as the pointer it already is.
            case "uint8_t *"   -> new Arg("Pointer", name, name);
            case "Interval *"  -> new Arg("java.time.Duration", name,
                    "utils.ConversionUtils.timedelta_to_interval(" + name + ")");
            case "TimestampTz" -> new Arg("java.time.OffsetDateTime", name, name);
            case "char *"      -> new Arg("String", name, name);
            // A temporal object argument is passed as a Temporal and forwarded as its inner pointer;
            // MEOS validates the concrete subtype (a TInstant *, a TSequence *) at the boundary.
            case "Temporal *", "TInstant *", "TSequence *", "TSequenceSet *"
                               -> new Arg("Temporal", name, name + ".getInner()");
            // A number-box restriction takes the TBox wrapper, forwarded as its inner pointer.
            case "TBox *"      -> new Arg("types.boxes.TBox", name, name + ".get_inner()");
            // A time restriction takes the tstz wrapper; a value restriction takes the generic
            // collection over the base value. Both are forwarded as their inner pointer.
            case "Set *"       -> new Arg(time ? "types.collections.time.tstzset"
                                               : "types.collections.base.Set<?>", name, name + ".get_inner()");
            case "Span *"      -> new Arg(time ? "types.collections.time.tstzspan"
                                               : "types.collections.base.Span<?>", name, name + ".get_inner()");
            case "SpanSet *"   -> new Arg(time ? "types.collections.time.tstzspanset"
                                               : "types.collections.base.SpanSet<?>", name, name + ".get_inner()");
            default            -> null;
        };
    }

    private void defer(String ooName, String reason) {
        deferred.add(ooName + " — " + reason);
    }

    /** Whether the named parameter is a {@code size_t *} out-parameter the wrapper folds internally. */
    private static boolean isSizeOut(JsonNode params, String name) {
        for (JsonNode p : params) {
            if (p.path("name").asText().equals(name)) {
                return cleanType(p.path("cType").asText()).contains("size_t");
            }
        }
        return false;
    }

    /** The cleaned C type of the named parameter, or an empty string when it is absent. */
    private static String paramCType(JsonNode params, String name) {
        for (JsonNode p : params) {
            if (p.path("name").asText().equals(name)) {
                return cleanType(p.path("cType").asText());
            }
        }
        return "";
    }

    private static String cleanType(String c) {
        return c.replace("const ", "").trim();
    }

    /**
     * The record component for one split bin-start out-parameter, or {@code null} if this slice does not
     * model that dimension. A bin out-parameter is a pointer to an array of the bin-start values
     * ({@code T **}); the reader dereferences the array pointer ({@code $arr}) and reads element
     * {@code _i}. This slice supports the time dimension (a {@code TimestampTz} array → an
     * {@code OffsetDateTime}, the {@code time} column of the SQL composite) and the value dimension of the
     * numeric subclasses (an {@code int}/{@code double} array → the {@code number} column); the space
     * dimension of the spatial subclasses (a {@code GSERIALIZED} array → the {@code point} column) extends
     * this map when those classes are generated.
     */
    private static SplitKey splitKey(String outParamCType) {
        return switch (outParamCType) {
            case "TimestampTz **" -> new SplitKey("time", "java.time.OffsetDateTime",
                    "utils.TimestampTzConverter.toOffsetDateTime($arr.getLong((long) _i * Long.BYTES))");
            case "int **"    -> new SplitKey("number", "int", "$arr.getInt((long) _i * Integer.BYTES)");
            case "double **" -> new SplitKey("number", "double", "$arr.getDouble((long) _i * Double.BYTES)");
            default -> null;
        };
    }

    /** The first letter upper-cased, for deriving a split record name from its camelCase method name. */
    private static String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // -------------------------------------------------------------------------
    // Emission
    // -------------------------------------------------------------------------

    private String generateInterface(List<Method> methods) {
        // Body first, so the imports it needs can be read back from it. A superclass surface declares the
        // three contract methods; a subclass surface inherits them from the interface it extends.
        StringBuilder body = new StringBuilder();
        if (spec.superInterface() == null) {
            body.append("\tPointer getInner();\n\n");
            body.append("\tString getCustomType();\n\n");
            body.append("\tTemporalType getTemporalType();\n\n");
        }
        if (methods.stream().anyMatch(m -> m.returnKind().equals("matchArray"))) {
            body.append(matchRecord());
        }
        for (Method m : methods) {
            if (m.returnKind().equals("split")) {
                body.append(splitRecord(m));
            }
        }
        for (Method m : methods) {
            body.append(generateMethod(m));
        }
        String b = body.toString();

        StringBuilder sb = new StringBuilder();
        sb.append("package types.temporal.generated;\n\n");
        for (String[] imp : CANDIDATE_IMPORTS) {
            if (references(b, imp[0])) {
                sb.append(imp[1]).append("\n");
            }
        }
        if (b.contains("TEMPORAL_")) {
            sb.append("\nimport static types.temporal.TemporalType.*;\n");
        }
        sb.append("\n/**\n");
        sb.append(" * Generated OO surface for {@link ").append(spec.objectKey()).append("}.\n");
        sb.append(" *\n");
        sb.append(" * <p>Derived from the MEOS object model (objectModel.classes.").append(spec.objectKey());
        sb.append(") by ObjectLayerGenerator. Each method carries the canonical camelCase name from the\n");
        sb.append(" * object model and delegates to the matching GeneratedFunctions wrapper, supplying the\n");
        sb.append(" * receiver through getInner() and wrapping the result back into the object layer. A class\n");
        sb.append(" * adopts the surface by implementing this interface; the hand class provides the contract\n");
        sb.append(" * methods getInner, getCustomType and getTemporalType.\n");
        sb.append(" */\n");
        String ext = spec.superInterface() == null ? "" : " extends " + spec.superInterface();
        sb.append("public interface ").append(spec.interfaceName()).append(ext).append(" {\n\n");
        sb.append(b);
        sb.append("}\n");
        return sb.toString();
    }

    /** The candidate single-type imports, each emitted only when the interface body references the type. */
    private static final String[][] CANDIDATE_IMPORTS = {
            {"GeneratedFunctions", "import functions.GeneratedFunctions;"},
            {"Pointer",            "import jnr.ffi.Pointer;"},
            {"Factory",            "import types.temporal.Factory;"},
            {"Temporal",           "import types.temporal.Temporal;"},
            {"TemporalType",       "import types.temporal.TemporalType;"},
            {"TInterpolation",     "import types.temporal.TInterpolation;"},
    };

    /** Whether the body uses {@code name} as a whole word — the boundary keeps Temporal out of TemporalType. */
    private static boolean references(String body, String name) {
        return java.util.regex.Pattern.compile("\\b" + name + "\\b").matcher(body).find();
    }

    private String generateMethod(Method m) {
        StringJoiner sig = new StringJoiner(", ");
        StringJoiner call = new StringJoiner(", ");
        call.add("getInner()");
        for (Arg a : m.args) {
            sig.add(a.javaType + " " + a.name);
            call.add(a.callExpr);
        }
        String invocation = "GeneratedFunctions." + m.fnName + "(" + call + ")";
        // A factory has no receiver in the call; every argument is its own, and the result is wrapped
        // with the type contract.
        StringJoiner factoryCall = new StringJoiner(", ");
        for (Arg a : m.args) {
            factoryCall.add(a.callExpr);
        }
        String body = switch (m.returnKind) {
            case "factory" -> "\t\treturn Factory.create_temporal(GeneratedFunctions." + m.fnName
                    + "(" + factoryCall + "), getCustomType(), getTemporalType());\n";
            case "temporal" -> "\t\treturn Factory.create_temporal(" + invocation
                    + ", getCustomType(), " + m.returnSubtype + ");\n";
            case "boolResult" -> "\t\tPointer _r = " + invocation + ";\n"
                    + "\t\treturn _r == null ? null : " + m.returnSubtype + ";\n";
            case "arrayInput" -> arrayInputBody(m);
            case "interval" -> "\t\treturn utils.ConversionUtils.interval_to_timedelta(" + invocation + ");\n";
            case "tstzspan" -> "\t\treturn new types.collections.time.tstzspan(" + invocation + ");\n";
            case "tstzspanset" -> "\t\treturn new types.collections.time.tstzspanset(" + invocation + ");\n";
            case "tbox" -> "\t\treturn new types.boxes.TBox(" + invocation + ");\n";
            case "objectArray" -> arrayFoldBody(m, "Factory.create_temporal(_array.getPointer("
                    + "(long) _i * Long.BYTES), getCustomType(), " + m.returnSubtype + ")");
            case "scalarArray" -> arrayFoldBody(m, "utils.TimestampTzConverter.toOffsetDateTime("
                    + "_array.getLong((long) _i * Long.BYTES))");
            case "spanArray" -> arrayFoldBody(m, "new types.collections.time.tstzspan("
                    + "GeneratedFunctions.span_copy(_array.slice((long) _i * " + spanSize + ")))");
            case "tboxArray" -> arrayFoldBody(m, "new types.boxes.TBox("
                    + "GeneratedFunctions.tbox_copy(_array.slice((long) _i * " + tboxStride + ")))");
            case "matchArray" -> arrayFoldBody(m, matchElementExpr());
            case "split" -> splitFoldBody(m);
            default         -> "\t\treturn " + invocation + ";\n";
        };
        return "\tdefault " + m.returnType + " " + m.ooName + "(" + sig + ") {\n"
                + body
                + "\t}\n\n";
    }

    /** The expression that reads one {@code Match} struct at the array cursor into a record. */
    private String matchElementExpr() {
        StringJoiner reads = new StringJoiner(", ");
        for (StructField f : matchFields) {
            reads.add("_array." + f.getter() + "((long) _i * " + matchStride + " + " + f.offset() + ")");
        }
        return "new Match(" + reads + ")";
    }

    /** The {@code Match} record declaration, its components derived from the catalog struct fields. */
    private String matchRecord() {
        StringJoiner components = new StringJoiner(", ");
        for (StructField f : matchFields) {
            components.add(f.javaType() + " " + f.name());
        }
        return "\t/** A matched pair of instant positions on a similarity path. */\n"
                + "\trecord Match(" + components + ") {}\n\n";
    }

    /**
     * The record one split fragment folds into: one component per bin dimension (the bin-start value)
     * followed by the temporal fragment. The bin components and their types come from the split's
     * {@link SplitKey}s (aligned to the SQL composite the table function returns); the fragment is the
     * temporal piece restricted to that bin.
     */
    private String splitRecord(Method m) {
        StringJoiner components = new StringJoiner(", ");
        for (SplitKey k : m.splitKeys()) {
            components.add(k.javaType() + " " + k.fieldName());
        }
        components.add("Temporal fragment");
        String plural = m.splitKeys().size() > 1 ? "s" : "";
        return "\t/** One " + m.ooName() + " fragment with its bin start" + plural + ". */\n"
                + "\trecord " + capitalize(m.ooName()) + "(" + components + ") {}\n\n";
    }

    /**
     * The body of a split-fold: allocate the count buffer and one bin buffer per grouping dimension, call
     * the wrapper (which writes the count and each bin array and returns the fragment array), then read
     * each fragment and its parallel bin start(s) into a record. Each bin out-parameter is a {@code T **},
     * so its buffer holds the array pointer, dereferenced once before the loop.
     */
    private String splitFoldBody(Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\tjnr.ffi.Runtime _rt = jnr.ffi.Runtime.getSystemRuntime();\n");
        sb.append("\t\tPointer _count = jnr.ffi.Memory.allocate(_rt, Integer.BYTES);\n");
        for (int k = 0; k < m.splitKeys().size(); k++) {
            sb.append("\t\tPointer _bin").append(k).append(" = jnr.ffi.Memory.allocate(_rt, Long.BYTES);\n");
        }
        StringJoiner call = new StringJoiner(", ");
        call.add("getInner()");
        for (Arg a : m.args()) {
            call.add(a.callExpr());
        }
        for (int k = 0; k < m.splitKeys().size(); k++) {
            call.add("_bin" + k);
        }
        call.add("_count");
        sb.append("\t\tPointer _frags = GeneratedFunctions.").append(m.fnName()).append("(").append(call).append(");\n");
        sb.append("\t\tint _n = _count.getInt(0);\n");
        for (int k = 0; k < m.splitKeys().size(); k++) {
            sb.append("\t\tPointer _bin").append(k).append("Arr = _bin").append(k).append(".getPointer(0);\n");
        }
        String rec = capitalize(m.ooName());
        sb.append("\t\tjava.util.List<").append(rec).append("> _out = new java.util.ArrayList<>(_n);\n");
        sb.append("\t\tfor (int _i = 0; _i < _n; _i++) {\n");
        StringJoiner ctor = new StringJoiner(", ");
        for (int k = 0; k < m.splitKeys().size(); k++) {
            ctor.add(m.splitKeys().get(k).readerExpr().replace("$arr", "_bin" + k + "Arr"));
        }
        ctor.add("Factory.create_temporal(_frags.getPointer((long) _i * Long.BYTES), getCustomType(), "
                + m.returnSubtype() + ")");
        sb.append("\t\t\t_out.add(new ").append(rec).append("(").append(ctor).append("));\n");
        sb.append("\t\t}\n");
        sb.append("\t\treturn _out;\n");
        return sb.toString();
    }

    /**
     * The body of an array-input constructor: write each list element's inner pointer into a buffer and
     * pass the size as the count, then wrap the result with the type contract.
     */
    private static String arrayInputBody(Method m) {
        String list = m.args.get(0).name();
        return "\t\tjnr.ffi.Runtime _rt = jnr.ffi.Runtime.getSystemRuntime();\n"
                + "\t\tPointer _arr = jnr.ffi.Memory.allocate(_rt, " + list + ".size() * Long.BYTES);\n"
                + "\t\tfor (int _i = 0; _i < " + list + ".size(); _i++) {\n"
                + "\t\t\t_arr.putPointer((long) _i * Long.BYTES, " + list + ".get(_i).getInner());\n"
                + "\t\t}\n"
                + "\t\treturn Factory.create_temporal(GeneratedFunctions." + m.fnName + "(_arr, "
                + list + ".size()), getCustomType(), getTemporalType());\n";
    }

    /**
     * The body of an array-fold method: allocate the count buffer, call the wrapper (which writes the
     * count and returns the array), then read each element with {@code elementExpr} into a List. The
     * count out-parameter gives the length, so the loop never over-reads.
     */
    private static String arrayFoldBody(Method m, String elementExpr) {
        String elementType = m.returnType.substring(
                m.returnType.indexOf('<') + 1, m.returnType.lastIndexOf('>'));
        StringJoiner call = new StringJoiner(", ");
        call.add("getInner()");
        for (Arg a : m.args) {
            call.add(a.callExpr);
        }
        call.add("_count");
        return "\t\tjnr.ffi.Runtime _rt = jnr.ffi.Runtime.getSystemRuntime();\n"
                + "\t\tPointer _count = jnr.ffi.Memory.allocate(_rt, Integer.BYTES);\n"
                + "\t\tPointer _array = GeneratedFunctions." + m.fnName + "(" + call + ");\n"
                + "\t\tint _n = _count.getInt(0);\n"
                + "\t\tjava.util.List<" + elementType + "> _out = new java.util.ArrayList<>(_n);\n"
                + "\t\tfor (int _i = 0; _i < _n; _i++) {\n"
                + "\t\t\t_out.add(" + elementExpr + ");\n"
                + "\t\t}\n"
                + "\t\treturn _out;\n";
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
