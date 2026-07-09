import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Generates NewFunctions.java from the meos-idl.json file produced by the
 * C IDL extraction script.
 *
 * The output mirrors the structure of old_functions.txt so the two files can be
 * diff-compared:
 *   - an inner MeosLibrary interface with one declaration per MEOS function
 *   - a public static wrapper method per function that delegates to the interface
 *     and calls MeosErrorHandler.checkError()
 */
public class FunctionsGenerator {

    // Enum names extracted from the JSON "enums" section.
    // These are mapped to int in Java (JNR-FFI represents C enums as int).
    private final Set<String> enumNames = new HashSet<>();

    // DateADT      → typedef int32_t  → Java int
    // Timestamp    → typedef int64_t  → Java long  (no timezone)
    // TimestampTz  → typedef int64_t  → Java long  (with timezone)
    private static final Set<String> DATE_C_TYPES = Set.of("DateADT");
    private static final Set<String> TIMESTAMP_C_TYPES = Set.of("Timestamp", "TimestampTz");

    // The JSON sometimes encodes them as int32_t rather than size_t, producing
    // "int" in the interface.  old_functions.txt used "long" consistently for these,
    // so we force long when the interface type resolved to int for these names.
    private static final Set<String> SIZE_PARAM_NAMES = Set.of("size", "wkb_size");

    // -------------------------------------------------------------------------
    // Optional MEOS type families, gated by build flags mirroring the
    // MobilityDB/MEOS flag names and ON|OFF (also 1|0) values: -DCBUFFER=OFF,
    // -DNPOINT=OFF, -DPOSE=OFF, -DRGEO=OFF, -DH3=OFF. Each family maps to the
    // public headers that declare its functions; a function whose header belongs
    // to an excluded family is omitted from the generated binding, so a subset
    // jar ships without it. The shared binding jar includes every family by
    // default; pass -D<FAMILY>=OFF|0 to drop one (RGEO needs POSE).
    // -------------------------------------------------------------------------
    // The canonical family of a function is the IDL ``family`` field, which
    // MEOS-API derives from the declaring header's ``meos/include/<family>/``
    // subdirectory — the single source of truth. This basename map is only the
    // fallback for IDLs generated before that field existed, and covers the
    // public headers of the families present at the time.
    private static final Map<String, String> HEADER_FAMILY = Map.ofEntries(
            Map.entry("meos_cbuffer.h", "CBUFFER"),
            Map.entry("meos_npoint.h", "NPOINT"),
            Map.entry("meos_pose.h", "POSE"),
            Map.entry("meos_rgeo.h", "RGEO"),
            Map.entry("meos_h3.h", "H3"),
            Map.entry("th3index.h", "H3"),
            Map.entry("th3index_internal.h", "H3"),
            Map.entry("th3index_boxops.h", "H3"),
            Map.entry("h3index.h", "H3"),
            Map.entry("h3index_sets.h", "H3"),
            Map.entry("h3_generated.h", "H3"));
    private static final Set<String> OPTIONAL_FAMILIES =
            Set.of("CBUFFER", "NPOINT", "POSE", "RGEO", "H3",
                   "QUADBIN", "POINTCLOUD", "JSON", "ARROW", "RASTER");

    // Families enabled for this generation run; core headers are always emitted.
    private Set<String> enabledFamilies;

    private static Set<String> resolveEnabledFamilies() {
        Set<String> enabled = new LinkedHashSet<>();
        for (String family : OPTIONAL_FAMILIES) {
            String v = System.getProperty(family);
            boolean off = v != null
                    && (v.equalsIgnoreCase("OFF") || v.equals("0") || v.equalsIgnoreCase("false"));
            if (!off) {
                enabled.add(family); // included by default, dropped only by -D<FAMILY>=OFF|0
            }
        }
        return enabled;
    }

    // Resolve a function's family: the canonical IDL ``family`` field when
    // present, else the header-basename fallback for pre-field IDLs.
    private String familyOf(JsonNode fn) {
        JsonNode familyNode = fn.get("family");
        if (familyNode != null) {
            return familyNode.asText();
        }
        JsonNode fileNode = fn.get("file");
        return fileNode == null ? null : HEADER_FAMILY.get(fileNode.asText());
    }

    private boolean familyEnabled(String family) {
        // CORE (and any family not in the optional set) is always emitted; an
        // optional family is emitted only while it stays enabled.
        return family == null
                || !OPTIONAL_FAMILIES.contains(family)
                || enabledFamilies.contains(family);
    }

    // Out-parameters — the arguments a function writes to rather than reads — are declared
    // ONCE at the source by the Doxygen @param[out] tag, carried here as the catalog's
    // shape.outParams flag (cross-checked against the C signature in MEOS-API) on each
    // ParamDef.  A wrapper hides an out-param from its signature and folds it; the ROLE is
    // read from the C type, via isSizeOut()/isResultOut():
    //
    //   size_t* out-param  → the throwaway byte-count of the *_as_wkb/_as_hexwkb family:
    //                        allocated internally (Memory.allocateDirect), forwarded, discarded.
    //   other pointer out-param on a boolean-returning method → the VALUE (the C idiom
    //                        `bool fn(.., T *result)`): allocated, read back with the accessor
    //                        matching *result's C type, and returned.
    //
    // The flag is keyed by the source's @param[out] tag, so an out-param folds regardless of
    // how it is spelled in the signature (`size_out`, `size`, `result`, `value`, …).

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) throws IOException {
        String base = System.getProperty("user.dir");

        String inputPath  = args.length > 0 ? args[0]
                : base + "/codegen/input/meos-idl.json";
        String outputPath = args.length > 1 ? args[1]
                : base + "/jmeos-core/src/main/java/functions/GeneratedFunctions.java";

        new FunctionsGenerator().run(inputPath, outputPath);
    }

    // -------------------------------------------------------------------------
    // Core processing
    // -------------------------------------------------------------------------

    private void run(String inputPath, String outputPath) throws IOException {
        System.out.println("=== NewFunctionsGenerator ===");
        System.out.println("Input : " + inputPath);
        System.out.println("Output: " + outputPath);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(inputPath));

        // 1. Collect enum names so they can be mapped to int
        collectEnumNames(root);
        System.out.println("Enums collected: " + enumNames);

        // 2. Parse all functions
        JsonNode functionsNode = root.get("functions");
        if (functionsNode == null || !functionsNode.isArray()) {
            throw new IllegalStateException("No 'functions' array found in JSON.");
        }

        List<FunctionDef> functions = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>(); // deduplicate by name#arity

        enabledFamilies = resolveEnabledFamilies();
        System.out.println("Enabled optional families: " + enabledFamilies
                + " (core always included)");

        for (JsonNode fn : functionsNode) {
            if (!familyEnabled(familyOf(fn))) {
                continue; // function belongs to a disabled type family
            }
            FunctionDef def = parseFunctionDef(fn);
            String key = def.name + "#" + def.params.size();
            if (seen.add(key)) {
                functions.add(def);
            }
        }
        System.out.println("Functions parsed: " + functions.size());

        // 3. Generate file content
        String content = generateFile(functions);

        // 4. Write to disk
        Path out = Paths.get(outputPath);
        Files.createDirectories(out.getParent());
        Files.writeString(out, content);
        System.out.println("Written: " + out.toAbsolutePath());
        System.out.println("=== Done ===");
    }

    // -------------------------------------------------------------------------
    // JSON parsing helpers
    // -------------------------------------------------------------------------

    private void collectEnumNames(JsonNode root) {
        JsonNode enums = root.get("enums");
        if (enums != null && enums.isArray()) {
            for (JsonNode e : enums) {
                JsonNode nameNode = e.get("name");
                if (nameNode != null) {
                    enumNames.add(nameNode.asText());
                }
            }
        }
    }

    private FunctionDef parseFunctionDef(JsonNode fn) {
        String name = fn.get("name").asText();

        // Return type: prefer the "c" field over "canonical"
        JsonNode retNode = fn.get("returnType");

        if (retNode == null) {
            throw new IllegalStateException("Json node of the return type of function \"" + name + "\" is null");
        }

        String retCType = retNode.get("c").asText();

        if (retCType.equals("null")) {
            throw new IllegalStateException("Null return type:" + retNode.asText());
        }

        String retJava  = mapCTypeToJava(retCType);

        // Parameters
        // ParamDef carries the original C type so that
        // generateStaticMethod can decide whether a conversion is needed.
        List<ParamDef> params = new ArrayList<>();
        // Out-parameters are declared once, at the source, by the Doxygen @param[out] tag —
        // carried here as shape.outParams (cross-checked against the C signature in MEOS-API).
        // The wrapper folds them instead of relying on a hardcoded parameter-name whitelist.
        java.util.Set<String> outParams = new java.util.HashSet<>();
        JsonNode shapeNode = fn.get("shape");
        if (shapeNode != null && shapeNode.has("outParams")) {
            for (JsonNode o : shapeNode.get("outParams")) {
                outParams.add(o.asText());
            }
        }
        JsonNode paramsNode = fn.get("params");
        if (paramsNode != null && paramsNode.isArray()) {
            for (JsonNode p : paramsNode) {
                if (p != null && p.isObject()) {
                    String rawName = p.get("name").asText();
                    String pName  = sanitizeParamName(rawName);
                    String pCType = p.get("cType").asText();
                    String pJava  = mapCTypeToJava(pCType);

                    // Override int → long for known byte-count parameters.
                    // The JSON may emit int32_t for these; old_functions.txt used long.
                    if (SIZE_PARAM_NAMES.contains(pName) && pJava.equals("int")) {
                        pJava = "long";
                    }

                    params.add(new ParamDef(pName, pJava, pCType, outParams.contains(rawName)));
                }
            }
        }

        return new FunctionDef(name, retJava, retCType, params);
    }

    // -------------------------------------------------------------------------
    // C → Java type mapping (interface level)
    // -------------------------------------------------------------------------

    /**
     * Maps a C type to the JNR-FFI Java type used in the inner MeosLibrary
     * interface (the low-level native binding layer).
     *
     * Added cases for PostgreSQL temporal typedefs that previously
     * fell through to the default branch and became Pointer:
     *   DateADT     → int
     *   Timestamp   → long
     *   TimestampTz → long
     */
    private String mapCTypeToJava(String cType) {
        cType = cType.replace("const ", "").trim();

        // Function pointers
        if (cType.contains("(*)") || cType.contains("(*)(")) {
            return "Pointer";
        }
        if (cType.equals("error_handler_fn")) {
            return "error_handler_fn";
        }

        // Double pointer → always Pointer
        if (cType.contains("**")) {
            return "Pointer";
        }

        // Single pointer
        if (cType.endsWith("*")) {
            String base = cType.substring(0, cType.length() - 1).trim();
            if (base.equals("char")) {
                return "String";
            }
            return "Pointer";
        }

        // Enums defined in the JSON
        if (enumNames.contains(cType)) {
            return "int";
        }

        return switch (cType) {
            case "void"                             -> "void";
            case "bool"                             -> "boolean";
            case "char"                             -> "String";
            case "float"                            -> "float";
            case "double", "float8"                 -> "double";
            case "int", "int32", "int32_t",
                 "uint32", "uint32_t"               -> "int";
            case "short", "int16", "int16_t",
                 "uint16", "uint16_t"               -> "short";
            case "int8", "int8_t",
                 "uint8", "uint8_t"                 -> "byte";
            case "long", "int64", "int64_t",
                 "uint64", "uint64_t"               -> "long";
            case "size_t", "uintptr_t"              -> "long";

            // PostgreSQL temporal typedefs previously all hit
            // the default branch and became Pointer.
            // DateADT is int32 under the hood; Timestamp/TimestampTz are int64.
            case "DateADT"                          -> "int";
            case "Timestamp", "TimestampTz"         -> "long";
            // H3Index is a uint64 cell identifier (DGGRID/H3), not an opaque
            // struct pointer; without this it hits the default branch and
            // becomes Pointer, breaking every h3index/th3index binding.
            case "H3Index"                          -> "long";

            // Explicit enum names (in case not in JSON enums section)
            case "interpType", "RTreeSearchOp",
                 "tempSubtype", "spatialRel",
                 "errorCode"                        -> "int";
            default                                 -> "Pointer";
        };
    }

    // -------------------------------------------------------------------------
    // C → Java type mapping (public static wrapper level)
    // -------------------------------------------------------------------------

    /**
     * Maps a C type to the user-friendly Java type exposed by the public static
     * wrapper methods.  This is distinct from the interface-level mapping:
     *
     *   TimestampTz → OffsetDateTime
     *   Timestamp   → LocalDateTime
     *   DateADT     → int
     *
     * All other types delegate to mapCTypeToJava(), keeping a single source of
     * truth for primitive mappings.
     */
    private String mapCTypeToJavaWrapper(String cType) {
        String cleaned = cType.replace("const ", "").trim();
        return switch (cleaned) {
            case "TimestampTz" -> "OffsetDateTime";
            case "Timestamp"   -> "LocalDateTime";
            // DateADT stays int: no additional wrapper type needed
            default            -> mapCTypeToJava(cType);
        };
    }

    /**
     * Returns true if the C type is a temporal type that requires
     * epoch-conversion code in the static wrapper body.
     */
    private boolean isTemporalCType(String cType) {
        String cleaned = cType.replace("const ", "").trim();
        return TIMESTAMP_C_TYPES.contains(cleaned);
    }

    /**
     * Returns true if the C return type is an OWNED {@code char *} that the
     * caller must free: a non-const {@code char *}. {@code const char *}
     * returns are borrowed/static (e.g. temporal_interp, temporal_subtype,
     * geo_typename) and must NOT be freed. For an owned char* the interface
     * binds the return as Pointer and the wrapper frees it after copying the
     * string, instead of letting JNR-FFI copy the C string to a Java String
     * and leak the original allocation.
     */
    private boolean isOwnedCharReturn(String retCType) {
        if (retCType.contains("const")) return false;
        return retCType.replaceAll("\\s+", "").equals("char*");
    }

    // bool+result strategy: driven by the pointed-to C type
    //
    // The original version always generated:
    //   Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
    //   Pointer new_result = result.getPointer(0);   <-- ERROR for scalar types
    //   return out ? new_result : null;
    //
    // This is correct for pointer results (Span*, STBox*, …) but crashes
    // at runtime for scalar results (double*, TimestampTz*, int*, bool*, …)
    // because result.getPointer(0) interprets the scalar's bit-pattern as a
    // native memory address which is unmapped.
    //
    // Fix: inspect the C type of the result param, strip the trailing *, and:
    //   - allocate the correct size (Double.BYTES for double*, Long.BYTES for
    //     TimestampTz*, Integer.BYTES for int*, etc.)
    //   - for SCALAR types: return the buffer (result) directly so callers can
    //     do result.getDouble(0), result.getLong(0), etc. matching the existing
    //     call sites (e.g. functions.stbox_xmin(inner).getDouble(0))
    //   - for POINTER types: dereference with result.getPointer(0) to
    //     obtain the actual Span*, STBox*, etc.
    //
    // In both cases the wrapper return type stays Pointer for backward compatibility.

    /**
     * Describes how to generate the allocation and return for a hidden bool+result
     * parameter, driven by the pointed-to C type.
     *
     * @param allocExpr JNR-FFI allocation size  (e.g. {@code "Double.BYTES"})
     * @param isPointer {@code true}  → struct-pointer result: dereference with
     *                                  {@code result.getPointer(0)} and return that Pointer;
     *                  {@code false} → scalar result: return the buffer ({@code result})
     *                                  directly so the caller can read the value with
     *                                  {@code .getDouble(0)}, {@code .getLong(0)}, etc.
     */
    private record ResultStrategy(
            String  allocExpr,
            boolean isPointer
    ) {}

    /**
     * Derives the {@link ResultStrategy} from the C type of the output parameter
     * (e.g. {@code "double *"}, {@code "Span *"}, {@code "TimestampTz *"}).
     *
     * The base type (after stripping {@code const} and {@code *}) determines the
     * allocation size and whether the buffer should be dereferenced.
     * Unknown / struct-pointer types fall through to the default branch.
     */
    private ResultStrategy resolveResultStrategy(String resultCType) {
        String base = resultCType.replace("const ", "").trim();
        if (base.endsWith("*")) {
            base = base.substring(0, base.length() - 1).trim();
        }

        return switch (base) {
            // Scalar types: allocate the correct size, return the buffer directly.
            // Callers read the value themselves: .getDouble(0), .getLong(0), etc.
            case "double", "float8"
                    -> new ResultStrategy("Double.BYTES",   false);
            case "float"
                    -> new ResultStrategy("Float.BYTES",    false);
            case "int", "int32", "int32_t", "uint32", "uint32_t"
                    -> new ResultStrategy("Integer.BYTES",  false);
            case "short", "int16", "int16_t", "uint16", "uint16_t"
                    -> new ResultStrategy("Short.BYTES",    false);
            case "long", "int64", "int64_t", "uint64", "uint64_t", "size_t", "uintptr_t"
                    -> new ResultStrategy("Long.BYTES",     false);
            case "bool"
                    -> new ResultStrategy("Byte.BYTES",     false);
            case "DateADT"
                    -> new ResultStrategy("Integer.BYTES",  false);
            case "Timestamp", "TimestampTz"
                    -> new ResultStrategy("Long.BYTES",     false);
            // Struct pointers (Span*, STBox*, GSERIALIZED*, …): the buffer holds a
            // native address → dereference with getPointer(0) to get the actual pointer.
            default -> new ResultStrategy("Long.BYTES",     true);
        };
    }

    // -------------------------------------------------------------------------
    // Java-keyword-safe parameter names
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

    private String sanitizeParamName(String name) {
        if (name.equals("synchronized")) {
            return "synchronize";
        }
        if (JAVA_KEYWORDS.contains(name)) {
            return name + "_param";
        }
        return name;
    }

    // -------------------------------------------------------------------------
    // Code generation
    // -------------------------------------------------------------------------

    // Number of JNR-FFI proxy interfaces to split into.
    // Each proxy handles ≈ (totalMethods / PART_COUNT) declarations.
    private static final int PART_COUNT = 4;

    private String generateFile(List<FunctionDef> functions) {
        // Determine how many methods go into each part interface.
        int partSize = (functions.size() + PART_COUNT - 1) / PART_COUNT;

        StringBuilder sb = new StringBuilder();

        sb.append("""
                package functions;

                import jnr.ffi.Pointer;
                import jnr.ffi.Memory;
                import jnr.ffi.Runtime;
                import jnr.ffi.byref.PointerByReference;
                import jnr.ffi.Struct;
                import utils.JarLibraryLoader;
                import utils.meosCatalog.MeosEnums.meosType;
                import utils.meosCatalog.MeosEnums.meosOper;
                import functions.MeosErrorHandler;

                import java.time.*;

                """);

        sb.append("public class GeneratedFunctions {\n");
        // Native deallocator for char* returned by owning MEOS functions.
        // MEOS standalone allocates with the system malloc (palloc/pfree map to
        // malloc/free outside PostgreSQL); freeMemory() calls the system free
        // underneath. Uses sun.misc.Unsafe rather than a JNR-FFI libc binding to
        // avoid classloader-boundary issues, mirroring MobilitySpark MeosMemory.
        sb.append("""
                \tprivate static final sun.misc.Unsafe _UNSAFE;
                \tstatic {
                \t\ttry {
                \t\t\tjava.lang.reflect.Field _f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                \t\t\t_f.setAccessible(true);
                \t\t\t_UNSAFE = (sun.misc.Unsafe) _f.get(null);
                \t\t} catch (ReflectiveOperationException _e) {
                \t\t\tthrow new ExceptionInInitializerError(_e);
                \t\t}
                \t}

                \t/** Free a char* returned by an owning (non-const) MEOS function. Null-safe. */
                \tprivate static void _freeCStr(Pointer _p) {
                \t\tif (_p != null) _UNSAFE.freeMemory(_p.address());
                \t}

                """);
        sb.append(generateAllInterfaces(functions, partSize));
        sb.append("\n\n");

        for (int i = 0; i < functions.size(); i++) {
            int partIndex = Math.min(i / partSize, PART_COUNT - 1);
            sb.append(generateStaticMethod(functions.get(i), partIndex));
            sb.append("\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    // ---- Interface ----------------------------------------------------------

    /**
     * Generates the JNR-FFI proxy sub-interfaces (MeosLibraryPartA … D),
     * the static proxy fields (_meos_a … d), a dispatch HashMap, and the
     * backward-compatible MeosLibrary shim that routes calls via a lightweight
     * {@link java.lang.reflect.Proxy} instead of a direct JNR-FFI proxy.
     */
    private String generateAllInterfaces(List<FunctionDef> functions, int partSize) {
        StringBuilder sb = new StringBuilder();
        char[] letters = {'A', 'B', 'C', 'D'};

        // 1. Part interfaces (no INSTANCE field; JNR-FFI proxies only)
        for (int p = 0; p < PART_COUNT; p++) {
            int start = p * partSize;
            int end   = Math.min(start + partSize, functions.size());

            sb.append("\tpublic interface MeosLibraryPart").append(letters[p]).append(" {\n\n");
            for (int i = start; i < end; i++) {
                FunctionDef fn = functions.get(i);
                // Owned char* returns bind as Pointer so the wrapper can free
                // the native allocation after copying the string.
                String ifaceRet = isOwnedCharReturn(fn.retCType) ? "Pointer" : fn.returnType;
                sb.append("\t\t")
                        .append(ifaceRet).append(" ")
                        .append(fn.name).append("(")
                        .append(buildInterfaceParamList(fn.params))
                        .append(");\n\n");
            }
            sb.append("\t}\n\n");
        }

        // 2. Library path constant + JNR-FFI proxy singletons
        sb.append("\tprivate static final String _LIB = \"libmeos.so\";\n\n");
        for (char l : letters) {
            sb.append("\tstatic final MeosLibraryPart").append(l)
                    .append(" _meos_").append(Character.toLowerCase(l)).append(" =\n")
                    .append("\t\t\tJarLibraryLoader.create(MeosLibraryPart").append(l)
                    .append(".class, _LIB).getLibraryInstance();\n");
        }
        sb.append("\n");

        // 3. Dispatch map: method-name to the the proxy that owns it
        sb.append("\tprivate static final java.util.Map<String, Object> _dispatch;\n");
        sb.append("\tstatic {\n");
        sb.append("\t\t_dispatch = new java.util.HashMap<>(4096);\n");
        for (char l : letters) {
            sb.append("\t\tfor (java.lang.reflect.Method _m : MeosLibraryPart").append(l)
                    .append(".class.getMethods())\n");
            sb.append("\t\t\t_dispatch.put(_m.getName(), _meos_")
                    .append(Character.toLowerCase(l)).append(");\n");
        }
        sb.append("\t}\n\n");

        return sb.toString();
    }

    // ---- Static wrapper methods ---------------------------------------------

    /**
     * Generates a public static wrapper that:
     *   1. Accepts user-friendly types (OffsetDateTime for TimestampTz)
     *   2. Converts them to the low-level type expected by the interface
     *   3. Calls the interface method
     *   4. Converts the return value back to the user-friendly type if needed
     *   5. Checks for MEOS errors
     *
     * The wrapper signature uses mapCTypeToJavaWrapper(), so a temporal param or
     * return type differs between the interface (long TimestampTz) and the wrapper
     * (OffsetDateTime/LocalDateTime). The gap is bridged with utils.TimestampTzConverter:
     *
     *   var t_new = utils.TimestampTzConverter.toTimestampTz(t);   // param → long
     *   return utils.TimestampTzConverter.toOffsetDateTime(_result); // return → date
     */
    // An out-param (shape.outParams) plays one of two ROLES, read from its C type, not its
    // spelling: a size_t* out-param is the throwaway byte-count of the *_as_hexwkb/_as_wkb
    // family (allocated, forwarded, discarded); any other pointer out-param is the VALUE the
    // boolean+result pattern writes (allocated, dereferenced, returned).
    private static boolean isSizeOut(ParamDef p) {
        return p.out() && p.cType().contains("size_t");
    }

    private static boolean isResultOut(ParamDef p) {
        return p.out() && p.javaType().equals("Pointer") && !p.cType().contains("size_t");
    }

    private String generateStaticMethod(FunctionDef fn, int partIndex) {
        StringBuilder sb = new StringBuilder();

        // Detect the boolean+result pattern.
        //
        // Condition: the interface returns boolean AND the param list
        // contains a Pointer param flagged as an out-param (shape.outParams,
        // i.e. isResultOut) — the value written back through that pointer.
        //
        // When true, the wrapper:
        //   - hides the "result" param from its signature
        //   - allocates result internally via Memory.allocateDirect
        //   - reads the value with the accessor matching the C type (FIX G)
        //   - returns the typed value, or a typed default on failure
        //
        // The return type and generated code depend on the C type of *result:
        //   double* → public static double fn(...)  return result.getDouble(0)
        //   bool*   → public static boolean fn(...) return result.getByte(0) != 0
        //   Span*   → public static Pointer fn(...) return result.getPointer(0)
        // A boolean function with EXACTLY ONE value out-param folds to a returned
        // Pointer: the wrapper allocates the buffer, forwards it, reads it back, and
        // returns it. Functions with two-or-more result-out params
        // (intersection_*/synchronize_*: inter1/inter2, *_to_arrow: out_schema/out_array)
        // cannot collapse to a single returned buffer, so they keep those out-params as
        // caller-provided Pointer arguments.
        boolean isBoolResultPattern = fn.returnType.equals("boolean")
                && fn.params.stream().filter(FunctionsGenerator::isResultOut).count() == 1;

        // Resolve the ResultStrategy from the C type of the result param.
        // Determines allocation size, read expression, and wrapper return type.
        // Previously hardcoded to Pointer/Long.BYTES/getPointer(0), which caused
        // a SIGSEGV at runtime for scalar result types (double*, int*, bool*…).
        ResultStrategy resultStrategy = isBoolResultPattern
                ? fn.params.stream()
                .filter(FunctionsGenerator::isResultOut)
                .findFirst()
                .map(p -> resolveResultStrategy(p.cType))
                .orElse(new ResultStrategy("Long.BYTES", true))
                : null;

        // --- Separate visible params from internal hidden params ---
        // size_out  allocated internally, discarded after call
        // result    allocated internally, dereferenced and returned
        List<WrapperParam> wparams           = new ArrayList<>();
        List<String>       internalSizeParams = new ArrayList<>();
        boolean            hasInternalResult  = false;

        for (ParamDef p : fn.params) {
            if (isSizeOut(p)) {
                internalSizeParams.add(p.name);
                continue;
            }
            if (isBoolResultPattern && isResultOut(p)) {
                hasInternalResult = true;
                continue; // hide from signature; allocated below
            }
            String wrapperType = mapCTypeToJavaWrapper(p.cType);
            // Preserve the long override for size params
            if (SIZE_PARAM_NAMES.contains(p.name) && wrapperType.equals("int")) {
                wrapperType = "long";
            }
            boolean needsConversion = isTemporalCType(p.cType);
            wparams.add(new WrapperParam(p.name, wrapperType, p.javaType, needsConversion));
        }

        // --- Determine wrapper return type ---
        // The wrapper always returns Pointer for the bool+result pattern:
        // callers receive the buffer and read the typed value themselves
        // (.getDouble(0), .getLong(0), etc.), matching existing call sites.
        String wrapperReturnType = isBoolResultPattern
                ? "Pointer"
                : mapCTypeToJavaWrapper(fn.retCType);

        // --- Method signature (only visible params) ---
        sb.append("\t@SuppressWarnings(\"unused\")\n");
        sb.append("\tpublic static ").append(wrapperReturnType).append(" ")
                .append(fn.name).append("(")
                .append(buildWrapperParamList(wparams))
                .append(") {\n");

        // --- Internal allocations ---
        // Determine if we need a Runtime (needed for any Memory.allocateDirect call).
        boolean needsRuntime = !internalSizeParams.isEmpty() || hasInternalResult;

        if (isBoolResultPattern) {
            // Emit the "boolean out" sentinel variable first,
            // mirroring the exact pattern in old_functions.txt.
            sb.append("\t\tboolean out;\n");
        }

        if (needsRuntime) {
            sb.append("\t\tRuntime runtime = Runtime.getSystemRuntime();\n");
        }

        // Allocate the hidden result pointer.
        // Use strategy.allocExpr() instead of hardcoded Long.BYTES:
        //   double* → Double.BYTES, int* → Integer.BYTES, Span* → Long.BYTES, etc.
        if (hasInternalResult) {
            sb.append("\t\tPointer result = Memory.allocateDirect(runtime, ")
                    .append(resultStrategy.allocExpr()).append(");\n");
        }

        // Allocate hidden size_out pointer(s).
        for (String paramName : internalSizeParams) {
            sb.append("\t\tPointer ").append(paramName)
                    .append(" = Memory.allocateDirect(runtime, Long.BYTES);\n");
        }

        // --- Conversion variables (OffsetDateTime/LocalDateTime → long) ---
        // Emit epoch-second conversion for each temporal param.
        for (WrapperParam wp : wparams) {
            if (wp.needsConversion) {
                if (wp.wrapperType.equals("OffsetDateTime") || wp.wrapperType.equals("LocalDateTime")) {
                    sb.append("\t\tvar ").append(wp.name).append("_new = utils.TimestampTzConverter.toTimestampTz(")
                            .append(wp.name).append(");\n");
                }
            }
        }

        // --- Build argument list for the interface call ---
        // Hidden params (size_out, result) are still forwarded by their local name.
        StringJoiner args = new StringJoiner(", ");
        for (ParamDef p : fn.params) {
            if (isBoolResultPattern && isResultOut(p)) {
                args.add("result"); // the hidden buffer allocated as `result`
            } else if (isSizeOut(p)) {
                args.add(p.name); // size_out buffer is allocated under its own name
            } else {
                boolean converted = isTemporalCType(p.cType());
                args.add(converted ? p.name + "_new" : p.name);
            }
        }

        String partField = "_meos_" + (char) ('a' + partIndex);
        String call = partField + "." + fn.name + "(" + args + ");";

        // --- Delegate + error check + return ---
        if (isBoolResultPattern) {
            sb.append("\t\tout = ").append(call).append("\n");

            if (resultStrategy.isPointer()) {
                // pointer result (Span*, STBox*, …):
                // the buffer holds a native address --> dereference to get the actual pointer.
                sb.append("\t\tPointer new_result = result.getPointer(0);\n");
                sb.append("\t\tMeosErrorHandler.checkError();\n");
                sb.append("\t\treturn out ? new_result : null;\n");
            } else {
                // Scalar result (double*, TimestampTz*, int*, bool*, …):
                // the buffer holds the value itself and NOT a pointer address.
                // Return the buffer (result) directly so callers can read the typed
                // value: result.getDouble(0), result.getLong(0), etc.
                // Previously: result.getPointer(0) → interpreted scalar bits as an
                // address causing SIGSEGV.
                sb.append("\t\tMeosErrorHandler.checkError();\n");
                sb.append("\t\treturn out ? result : null;\n");
            }
        } else if (fn.returnType.equals("void")) {
            sb.append("\t\t").append(call).append("\n");
            sb.append("\t\tMeosErrorHandler.checkError();\n");
        } else if (isOwnedCharReturn(fn.retCType)) {
            // Interface returns Pointer (owned char*). Copy the string, free the
            // native allocation, and return the Java String — no leak.
            sb.append("\t\tPointer _result = ").append(call).append("\n");
            sb.append("\t\tMeosErrorHandler.checkError();\n");
            sb.append("\t\tif (_result == null) return null;\n");
            sb.append("\t\tString _str = _result.getString(0);\n");
            sb.append("\t\t_freeCStr(_result);\n");
            sb.append("\t\treturn _str;\n");
        } else {
            sb.append("\t\tvar _result = ").append(call).append("\n");
            sb.append("\t\tMeosErrorHandler.checkError();\n");

            // Convert long result back to OffsetDateTime/LocalDateTime.
            if (wrapperReturnType.equals("OffsetDateTime")) {
                sb.append("\t\treturn utils.TimestampTzConverter.toOffsetDateTime(_result);\n");
            } else if (wrapperReturnType.equals("LocalDateTime")) {
                sb.append("\t\treturn utils.TimestampTzConverter.toLocalDateTime(_result);\n");
            } else {
                sb.append("\t\treturn _result;\n");
            }
        }

        sb.append("\t}\n");
        return sb.toString();
    }

    // ---- Parameter list helpers ---------------------------------------------

    /**
     * Interface-level param list: uses the low-level JNR type (int/long).
     */
    private String buildInterfaceParamList(List<ParamDef> params) {
        if (params.isEmpty()) return "";
        StringJoiner sj = new StringJoiner(", ");
        for (ParamDef p : params) {
            // Restore long for size params even at interface level
            String type = SIZE_PARAM_NAMES.contains(p.name) && p.javaType.equals("int")
                    ? "long" : p.javaType;
            sj.add(type + " " + p.name);
        }
        return sj.toString();
    }

    /**
     * Wrapper-level param list: uses the user-friendly type (OffsetDateTime etc.).
     */
    private String buildWrapperParamList(List<WrapperParam> params) {
        if (params.isEmpty()) return "";
        StringJoiner sj = new StringJoiner(", ");
        for (WrapperParam p : params) {
            sj.add(p.wrapperType + " " + p.name);
        }
        return sj.toString();
    }

    // -------------------------------------------------------------------------
    // Data classes
    // -------------------------------------------------------------------------

    // retCType field so generateStaticMethod can decide the
    // wrapper return type independently of the interface return type.
    private record FunctionDef(String name, String returnType, String retCType, List<ParamDef> params) {}

    // Added cType field so each param's original C type is
    // available when generating conversion code in the static wrapper; `out` carries the
    // catalog's shape.outParams flag (Doxygen @param[out], cross-checked in MEOS-API).
    private record ParamDef(String name, String javaType, String cType, boolean out) {}

    // New record for wrapper-layer parameter info.
    private record WrapperParam(String name, String wrapperType, String interfaceType, boolean needsConversion) {}
}