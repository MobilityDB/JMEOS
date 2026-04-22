package builder;

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
public class NewFunctionsGenerator {

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

    // Output-only size parameters that must not appear in the public
    // static wrapper signature.
    //
    // In the MEOS C API, functions like set_as_wkb() accept a "size_t *size_out"
    // pointer that is written by the callee to report the number of bytes it
    // produced.  The caller (our wrapper) does not need to expose this detail:
    // it allocates the pointer internally (Memory.allocateDirect) and discards
    // the value, exactly as old_functions.txt does.
    //
    // Without this fix the generator was forwarding size_out directly into the
    // wrapper signature.
    private static final Set<String> OUTPUT_SIZE_PARAMS = Set.of("size_out");

    // Output-only result parameters for boolean-returning interface
    // methods.
    //
    // Many MEOS functions follow the C idiom:
    //   bool stbox_xmax(STBox *box, double *result)
    // where the boolean signals success/failure and the actual value is written
    // into *result.  old_functions.txt wraps these so the caller sees a clean:
    //   public static Pointer stbox_xmax(Pointer box)
    // The wrapper allocates the result pointer internally, calls the native
    // method, dereferences the pointer, and returns either the Pointer or null:
    //   boolean out;
    //   Runtime runtime = Runtime.getSystemRuntime();
    //   Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
    //   out = MeosLibrary.meos.stbox_xmax(box, result);
    //   Pointer new_result = result.getPointer(0);
    //   MeosErrorHandler.checkError();
    //   return out ? new_result : null;
    //
    // This fix triggers when ALL of these conditions are true:
    //   1. The interface method returns boolean
    //   2. There is a parameter whose name is in OUTPUT_RESULT_PARAMS
    //   3. That parameter's Java type is Pointer
    private static final Set<String> OUTPUT_RESULT_PARAMS = Set.of("result");

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) throws IOException {
        String inputPath  = args.length > 0 ? args[0]
                : "src/main/java/builder/resources/meos-idl.json";
        String outputPath = args.length > 1 ? args[1]
                : "src/main/java/functions/functions.java";

        new NewFunctionsGenerator().run(inputPath, outputPath);
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

        for (JsonNode fn : functionsNode) {
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
        JsonNode paramsNode = fn.get("params");
        if (paramsNode != null && paramsNode.isArray()) {
            for (JsonNode p : paramsNode) {
                if (p != null && p.isObject()) {
                    String pName  = sanitizeParamName(p.get("name").asText());
                    String pCType = p.get("cType").asText();
                    String pJava  = mapCTypeToJava(pCType);

                    // Override int → long for known byte-count parameters.
                    // The JSON may emit int32_t for these; old_functions.txt used long.
                    if (SIZE_PARAM_NAMES.contains(pName) && pJava.equals("int")) {
                        pJava = "long";
                    }

                    params.add(new ParamDef(pName, pJava, pCType));
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
            case "char"                             -> "byte";
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

    private String generateFile(List<FunctionDef> functions) {
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

        sb.append("public class functions {\n");
        sb.append(generateInterface(functions));
        sb.append("\n\n");

        for (FunctionDef fn : functions) {
            sb.append(generateStaticMethod(fn));
            sb.append("\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    // ---- Interface ----------------------------------------------------------

    /**
     * The inner interface uses low-level JNR types (long for timestamps, int
     * for dates) so that JNR-FFI can marshal values directly to C without any
     * intermediate object allocation.
     */
    private String generateInterface(List<FunctionDef> functions) {
        StringBuilder sb = new StringBuilder();
        sb.append("\tpublic interface MeosLibrary {\n\n");
        sb.append("\t\tString libraryPath = \"libmeos.so\";\n\n");
        sb.append("\t\tMeosLibrary INSTANCE = JarLibraryLoader.create(MeosLibrary.class, libraryPath).getLibraryInstance();\n\n");
        sb.append("\t\tMeosLibrary meos = MeosLibrary.INSTANCE;\n\n");

        for (FunctionDef fn : functions) {
            // Interface uses mapCTypeToJava (int/long), NOT wrapper types.
            sb.append("\t\t")
                    .append(fn.returnType).append(" ")
                    .append(fn.name).append("(")
                    .append(buildInterfaceParamList(fn.params))
                    .append(");\n\n");
        }

        sb.append("\t}");
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
     * The original generator used the same type for both the
     * interface and the wrapper, so no conversion was possible.  We now use
     * mapCTypeToJavaWrapper() for the wrapper signature, and we emit explicit
     * epoch-conversion lines mirroring the pattern in old_functions.txt:
     *
     *   var t_new = t.toEpochSecond();        // OffsetDateTime → long
     *   var t_new = t.toEpochSecond(...);     // LocalDateTime → long
     *
     * For return types, we convert long → OffsetDateTime using
     *   OffsetDateTime.ofEpochSecond(_result, 0, ZoneOffset.UTC)
     */
    private String generateStaticMethod(FunctionDef fn) {
        StringBuilder sb = new StringBuilder();

        // ---------------------------------------------------------------
        // Detect the boolean+result → Pointer pattern.
        //
        // Condition: the interface returns boolean AND the param list
        // contains a Pointer param whose name is in OUTPUT_RESULT_PARAMS.
        //
        // When true, the wrapper:
        //   - returns Pointer instead of boolean
        //   - hides the "result" param from its signature
        //   - allocates result internally via Memory.allocateDirect
        //   - dereferences result.getPointer(0) after the call
        //   - returns  out ? new_result : null
        // ---------------------------------------------------------------
        boolean isBoolResultPattern = fn.returnType.equals("boolean")
                && fn.params.stream().anyMatch(p ->
                        OUTPUT_RESULT_PARAMS.contains(p.name)
                        && p.javaType().equals("Pointer"));

        // --- Separate visible params from internal hidden params ---
        // size_out  allocated internally, discarded after call
        // result    allocated internally, dereferenced and returned
        List<WrapperParam> wparams           = new ArrayList<>();
        List<String>       internalSizeParams = new ArrayList<>();
        boolean            hasInternalResult  = false;

        for (ParamDef p : fn.params) {
            if (OUTPUT_SIZE_PARAMS.contains(p.name)) {
                internalSizeParams.add(p.name);
                continue;
            }
            if (isBoolResultPattern && OUTPUT_RESULT_PARAMS.contains(p.name)
                    && p.javaType().equals("Pointer")) {
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
        // Override boolean → Pointer when the result pattern is active.
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
        if (hasInternalResult) {
            sb.append("\t\tPointer result = Memory.allocateDirect(runtime, Long.BYTES);\n");
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
                if (wp.wrapperType.equals("OffsetDateTime")) {
                    sb.append("\t\tvar ").append(wp.name).append("_new = ")
                            .append(wp.name).append(".toEpochSecond();\n");
                } else if (wp.wrapperType.equals("LocalDateTime")) {
                    sb.append("\t\tvar ").append(wp.name).append("_new = ")
                            .append(wp.name).append(".toInstant(java.time.ZoneOffset.UTC).getEpochSecond();\n");
                }
            }
        }

        // --- Build argument list for the interface call ---
        // Hidden params (size_out, result) are still forwarded by their local name.
        StringJoiner args = new StringJoiner(", ");
        for (ParamDef p : fn.params) {
            if (OUTPUT_SIZE_PARAMS.contains(p.name)
                    || (isBoolResultPattern && OUTPUT_RESULT_PARAMS.contains(p.name)
                        && p.javaType().equals("Pointer"))) {
                args.add(p.name); // pass locally-allocated pointer
            } else {
                boolean converted = isTemporalCType(p.cType());
                args.add(converted ? p.name + "_new" : p.name);
            }
        }
        String call = "MeosLibrary.meos." + fn.name + "(" + args + ");";

        // --- Delegate + error check + return ---
        if (isBoolResultPattern) {
            // boolean+result pattern:
            //   out = meos.fn(..., result);
            //   Pointer new_result = result.getPointer(0);
            //   MeosErrorHandler.checkError();
            //   return out ? new_result : null;
            sb.append("\t\tout = ").append(call).append("\n");
            sb.append("\t\tPointer new_result = result.getPointer(0);\n");
            sb.append("\t\tMeosErrorHandler.checkError();\n");
            sb.append("\t\treturn out ? new_result : null;\n");
        } else if (fn.returnType.equals("void")) {
            sb.append("\t\t").append(call).append("\n");
            sb.append("\t\tMeosErrorHandler.checkError();\n");
        } else {
            sb.append("\t\tvar _result = ").append(call).append("\n");
            sb.append("\t\tMeosErrorHandler.checkError();\n");

            // Convert long result back to OffsetDateTime/LocalDateTime.
            if (wrapperReturnType.equals("OffsetDateTime")) {
                sb.append("\t\treturn java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);\n");
            } else if (wrapperReturnType.equals("LocalDateTime")) {
                sb.append("\t\treturn java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(_result), java.time.ZoneOffset.UTC);\n");
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
    // available when generating conversion code in the static wrapper.
    private record ParamDef(String name, String javaType, String cType) {}

    // New record for wrapper-layer parameter info.
    private record WrapperParam(String name, String wrapperType, String interfaceType, boolean needsConversion) {}
}
