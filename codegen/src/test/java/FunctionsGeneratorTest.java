import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsGeneratorTest {

    private FunctionsGenerator generator;
    private Path tempDir;

    // Reflected private methods
    private Method mapCTypeToJava;
    private Method mapCTypeToJavaWrapper;
    private Method isTemporalCType;
    private Method resolveResultStrategy;
    private Method sanitizeParamName;
    private Method collectEnumNames;
    private Method run;

    @BeforeEach
    void setUp() throws Exception {
        generator = new FunctionsGenerator();
        tempDir = Files.createTempDirectory("codegen-test");

        mapCTypeToJava        = reflect("mapCTypeToJava",        String.class);
        mapCTypeToJavaWrapper = reflect("mapCTypeToJavaWrapper", String.class);
        isTemporalCType       = reflect("isTemporalCType",       String.class);
        sanitizeParamName     = reflect("sanitizeParamName",     String.class);
        run                   = reflect("run",                   String.class, String.class);

        // resolveResultStrategy takes a String (C type)
        resolveResultStrategy = reflect("resolveResultStrategy", String.class);

        // collectEnumNames takes a JsonNode: accessed separately in its own test
        collectEnumNames = FunctionsGenerator.class
                .getDeclaredMethod("collectEnumNames",
                        com.fasterxml.jackson.databind.JsonNode.class);
        collectEnumNames.setAccessible(true);
    }

    @AfterEach
    void tearDown() throws IOException {
        try (var stream = Files.walk(tempDir)) {
            stream.sorted(java.util.Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Method reflect(String name, Class<?>... params) throws Exception {
        Method m = FunctionsGenerator.class.getDeclaredMethod(name, params);
        m.setAccessible(true);
        return m;
    }

    private String mapJava(String cType) throws Exception {
        return (String) mapCTypeToJava.invoke(generator, cType);
    }

    private String mapWrapper(String cType) throws Exception {
        return (String) mapCTypeToJavaWrapper.invoke(generator, cType);
    }

    private boolean isTemporal(String cType) throws Exception {
        return (boolean) isTemporalCType.invoke(generator, cType);
    }

    private Object resolveStrategy(String cType) throws Exception {
        return resolveResultStrategy.invoke(generator, cType);
    }

    private String sanitize(String name) throws Exception {
        return (String) sanitizeParamName.invoke(generator, name);
    }

    /** Runs the generator with a JSON string and returns the generated file. */
    private String generateFromJson(String json) throws Exception {
        Path input  = tempDir.resolve("input.json");
        Path output = tempDir.resolve("out/GeneratedFunctions.java");
        Files.writeString(input, json);
        run.invoke(generator, input.toString(), output.toString());
        return Files.readString(output);
    }

    /** Returns the allocExpr field of a ResultStrategy record via reflection. */
    private String allocExpr(Object strategy) throws Exception {
        return (String) strategy.getClass().getMethod("allocExpr").invoke(strategy);
    }

    /** Returns the isPointer field of a ResultStrategy record via reflection. */
    private boolean isPointer(Object strategy) throws Exception {
        return (boolean) strategy.getClass().getMethod("isPointer").invoke(strategy);
    }

    // =========================================================================
    // mapCTypeToJava: interface-level type mapping
    // =========================================================================

    @Nested
    @DisplayName("mapCTypeToJava")
    class MapCTypeToJavaTests {

        @Test void void_() throws Exception        { assertEquals("void",    mapJava("void")); }
        @Test void bool_() throws Exception        { assertEquals("boolean", mapJava("bool")); }
        @Test void char_() throws Exception        { assertEquals("String",    mapJava("char")); }
        @Test void float_() throws Exception       { assertEquals("float",   mapJava("float")); }
        @Test void double_() throws Exception      { assertEquals("double",  mapJava("double")); }
        @Test void float8() throws Exception       { assertEquals("double",  mapJava("float8")); }
        @Test void int_() throws Exception         { assertEquals("int",     mapJava("int")); }
        @Test void int32_t() throws Exception      { assertEquals("int",     mapJava("int32_t")); }
        @Test void uint32_t() throws Exception     { assertEquals("int",     mapJava("uint32_t")); }
        @Test void long_() throws Exception        { assertEquals("long",    mapJava("long")); }
        @Test void int64_t() throws Exception      { assertEquals("long",    mapJava("int64_t")); }
        @Test void uint64_t() throws Exception     { assertEquals("long",    mapJava("uint64_t")); }
        @Test void size_t() throws Exception       { assertEquals("long",    mapJava("size_t")); }
        @Test void uintptr_t() throws Exception    { assertEquals("long",    mapJava("uintptr_t")); }
        @Test void int16_t() throws Exception      { assertEquals("short",   mapJava("int16_t")); }
        @Test void int8_t() throws Exception       { assertEquals("byte",    mapJava("int8_t")); }
        @Test void DateADT() throws Exception      { assertEquals("int",     mapJava("DateADT")); }
        @Test void Timestamp() throws Exception    { assertEquals("long",    mapJava("Timestamp")); }
        @Test void TimestampTz() throws Exception  { assertEquals("long",    mapJava("TimestampTz")); }
        @Test void H3Index() throws Exception      { assertEquals("long",    mapJava("H3Index")); }

        @Test void charPointer() throws Exception  { assertEquals("String",  mapJava("char *")); }
        @Test void structPointer() throws Exception { assertEquals("Pointer", mapJava("STBox *")); }
        @Test void doublePointer() throws Exception { assertEquals("Pointer", mapJava("int **")); }
        @Test void functionPointer() throws Exception { assertEquals("Pointer", mapJava("void (*)()")); }
        @Test void errorHandlerFn() throws Exception { assertEquals("error_handler_fn", mapJava("error_handler_fn")); }

        @Test
        @DisplayName("const prefix is stripped before mapping")
        void constPrefix() throws Exception {
            assertEquals("Pointer", mapJava("const STBox *"));
            assertEquals("long",    mapJava("const TimestampTz"));
        }

        @Test
        @DisplayName("unknown type falls through to Pointer")
        void unknownType() throws Exception {
            assertEquals("Pointer", mapJava("SomeUnknownStruct"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"interpType", "RTreeSearchOp", "tempSubtype", "spatialRel", "errorCode"})
        @DisplayName("explicit enum names → int")
        void explicitEnums(String cType) throws Exception {
            assertEquals("int", mapJava(cType));
        }

        @Test
        @DisplayName("enum name registered in enumNames set --> int")
        void registeredEnum() throws Exception {
            // Inject a custom enum name into the generator's enumNames set
            Field enumNamesField = FunctionsGenerator.class.getDeclaredField("enumNames");
            enumNamesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Set<String> names = (Set<String>) enumNamesField.get(generator);
            names.add("MyCustomEnum");

            assertEquals("int", mapJava("MyCustomEnum"));
        }
    }

    // =========================================================================
    // mapCTypeToJavaWrapper: user-friendly wrapper type mapping
    // =========================================================================

    @Nested
    @DisplayName("mapCTypeToJavaWrapper")
    class MapCTypeToJavaWrapperTests {

        @Test void TimestampTz_toOffsetDateTime() throws Exception {
            assertEquals("OffsetDateTime", mapWrapper("TimestampTz"));
        }

        @Test void Timestamp_toLocalDateTime() throws Exception {
            assertEquals("LocalDateTime", mapWrapper("Timestamp"));
        }

        @Test void constTimestampTz() throws Exception {
            assertEquals("OffsetDateTime", mapWrapper("const TimestampTz"));
        }

        @Test void int_unchanged() throws Exception {
            assertEquals("int", mapWrapper("int"));
        }

        @Test void pointer_unchanged() throws Exception {
            assertEquals("Pointer", mapWrapper("STBox *"));
        }

        @Test void DateADT_staysInt() throws Exception {
            assertEquals("int", mapWrapper("DateADT"));
        }

        @Test void bool_unchanged() throws Exception {
            assertEquals("boolean", mapWrapper("bool"));
        }
    }

    // =========================================================================
    // isTemporalCType
    // =========================================================================

    @Nested
    @DisplayName("isTemporalCType")
    class IsTemporalCTypeTests {

        @Test void TimestampTz_true() throws Exception  { assertTrue(isTemporal("TimestampTz")); }
        @Test void Timestamp_true() throws Exception    { assertTrue(isTemporal("Timestamp")); }
        @Test void constTimestampTz_true() throws Exception { assertTrue(isTemporal("const TimestampTz")); }

        @Test void int_false() throws Exception         { assertFalse(isTemporal("int")); }
        @Test void DateADT_false() throws Exception     { assertFalse(isTemporal("DateADT")); }
        @Test void Pointer_false() throws Exception     { assertFalse(isTemporal("STBox *")); }
        @Test void double_false() throws Exception      { assertFalse(isTemporal("double")); }
    }

    // =========================================================================
    // resolveResultStrategy
    // =========================================================================

    @Nested
    @DisplayName("resolveResultStrategy")
    class ResolveResultStrategyTests {

        @Test void double_scalar() throws Exception {
            Object s = resolveStrategy("double *");
            assertEquals("Double.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void float_scalar() throws Exception {
            Object s = resolveStrategy("float *");
            assertEquals("Float.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void int32_scalar() throws Exception {
            Object s = resolveStrategy("int32_t *");
            assertEquals("Integer.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void int16_scalar() throws Exception {
            Object s = resolveStrategy("int16_t *");
            assertEquals("Short.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void long_scalar() throws Exception {
            Object s = resolveStrategy("int64_t *");
            assertEquals("Long.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void bool_scalar() throws Exception {
            Object s = resolveStrategy("bool *");
            assertEquals("Byte.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void DateADT_scalar() throws Exception {
            Object s = resolveStrategy("DateADT *");
            assertEquals("Integer.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void TimestampTz_scalar() throws Exception {
            Object s = resolveStrategy("TimestampTz *");
            assertEquals("Long.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void structPointer_pointer() throws Exception {
            Object s = resolveStrategy("Span *");
            assertEquals("Long.BYTES", allocExpr(s));
            assertTrue(isPointer(s));
        }

        @Test void unknownType_defaultsToPointer() throws Exception {
            Object s = resolveStrategy("SomeStruct *");
            assertEquals("Long.BYTES", allocExpr(s));
            assertTrue(isPointer(s));
        }

        @Test void float8_scalar() throws Exception {
            Object s = resolveStrategy("float8 *");
            assertEquals("Double.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }
    }

    // =========================================================================
    // sanitizeParamName
    // =========================================================================

    @Nested
    @DisplayName("sanitizeParamName")
    class SanitizeParamNameTests {

        @Test void normalName_unchanged() throws Exception {
            assertEquals("box", sanitize("box"));
        }

        @Test void synchronized_specialCase() throws Exception {
            assertEquals("synchronize", sanitize("synchronized"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"int", "double", "class", "return", "void", "new", "for", "while", "if"})
        @DisplayName("Java keywords get _param suffix")
        void javaKeyword(String kw) throws Exception {
            assertEquals(kw + "_param", sanitize(kw));
        }

        @Test void nonKeyword_unchanged() throws Exception {
            assertEquals("count", sanitize("count"));
            assertEquals("rtree", sanitize("rtree"));
            assertEquals("stbox", sanitize("stbox"));
        }
    }

    // =========================================================================
    // collectEnumNames (via run integration)
    // =========================================================================

    @Nested
    @DisplayName("collectEnumNames")
    class CollectEnumNamesTests {

        @Test
        @DisplayName("enums section present → names collected")
        void enumsPresent() throws Exception {
            String json = """
                {
                  "enums": [
                    {"name": "RTreeSearchOp"},
                    {"name": "interpType"}
                  ],
                  "functions": []
                }
                """;
            Path input = tempDir.resolve("enums.json");
            Files.writeString(input, json);

            // Manually call collectEnumNames with parsed JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(input.toFile());

            collectEnumNames.invoke(generator, root);

            Field enumNamesField = FunctionsGenerator.class.getDeclaredField("enumNames");
            enumNamesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Set<String> names = (Set<String>) enumNamesField.get(generator);

            assertTrue(names.contains("RTreeSearchOp"));
            assertTrue(names.contains("interpType"));
            assertEquals(2, names.size());
        }

        @Test
        @DisplayName("no enums section → enumNames stays empty")
        void noEnumsSection() throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree("{\"functions\": []}");

            collectEnumNames.invoke(generator, root);

            Field enumNamesField = FunctionsGenerator.class.getDeclaredField("enumNames");
            enumNamesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Set<String> names = (Set<String>) enumNamesField.get(generator);

            assertTrue(names.isEmpty());
        }
    }

    // =========================================================================
    // run(): end-to-end / integration
    // =========================================================================

    @Nested
    @DisplayName("run(): end-to-end")
    class RunTests {

        @Test
        @DisplayName("missing functions array throws IllegalStateException")
        void missingFunctionsArray() {
            assertThrows(Exception.class, () ->
                    generateFromJson("{\"enums\": []}"));
        }

        @Test
        @DisplayName("non-array functions node throws IllegalStateException")
        void functionsNotArray() {
            assertThrows(Exception.class, () ->
                    generateFromJson("{\"functions\": {}}"));
        }

        @Test
        @DisplayName("empty functions array produces skeleton")
        void emptyFunctions() throws Exception {
            String out = generateFromJson("{\"functions\": []}");
            assertTrue(out.contains("package functions;"));
            assertTrue(out.contains("public class GeneratedFunctions {"));
            assertTrue(out.contains("static final MeosLibraryPart"));
        }

        @Test
        @DisplayName("void function generates correct interface and wrapper")
        void voidFunction() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "meos_initialize",
                    "returnType": {"c": "void"},
                    "params": []
                  }]
                }
                """;
            String out = generateFromJson(json);
            // Interface declaration
            assertTrue(out.contains("void meos_initialize();"));
            // Static wrapper
            assertTrue(out.contains("public static void meos_initialize()"));
            assertTrue(out.contains("_meos_a.meos_initialize()"));
            assertTrue(out.contains("MeosErrorHandler.checkError()"));
        }

        @Test
        @DisplayName("boolean-returning function generates correct wrapper")
        void boolFunction() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "overlaps_stbox_stbox",
                    "returnType": {"c": "bool"},
                    "params": [
                      {"name": "box1", "cType": "STBox *"},
                      {"name": "box2", "cType": "STBox *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("boolean overlaps_stbox_stbox(Pointer box1, Pointer box2)"));
            assertTrue(out.contains("public static boolean overlaps_stbox_stbox(Pointer box1, Pointer box2)"));
        }

        @Test
        @DisplayName("bool+result pattern hides result param from wrapper")
        void boolResultPattern() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "stbox_xmax",
                    "returnType": {"c": "bool"},
                    "params": [
                      {"name": "box",    "cType": "STBox *"},
                      {"name": "result", "cType": "double *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // Wrapper signature should NOT include result
            assertTrue(out.contains("public static Pointer stbox_xmax(Pointer box)"));
            // result is allocated internally
            assertTrue(out.contains("Memory.allocateDirect(runtime, Double.BYTES)"));
            // scalar strategy: no getPointer(0) dereference
            assertFalse(out.contains("result.getPointer(0)"));
            assertTrue(out.contains("return out ? result : null"));
        }

        @Test
        @DisplayName("bool+result with struct pointer result uses getPointer(0)")
        void boolResultPointerPattern() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "temporal_bbox",
                    "returnType": {"c": "bool"},
                    "params": [
                      {"name": "temp",   "cType": "Temporal *"},
                      {"name": "result", "cType": "STBox *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("Memory.allocateDirect(runtime, Long.BYTES)"));
            assertTrue(out.contains("result.getPointer(0)"));
            assertTrue(out.contains("return out ? new_result : null"));
        }

        @Test
        @DisplayName("size_out param is hidden and allocated internally")
        void sizeOutHidden() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "set_as_wkb",
                    "returnType": {"c": "uint8_t *"},
                    "params": [
                      {"name": "s",        "cType": "Set *"},
                      {"name": "size_out", "cType": "size_t *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // size_out must not appear in wrapper signature
            assertTrue(out.contains("public static Pointer set_as_wkb(Pointer s)"));
            // but is still allocated internally
            assertTrue(out.contains("Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES)"));
        }

        @Test
        @DisplayName("TimestampTz param generates epoch conversion in wrapper")
        void timestampTzConversion() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "timestamptz_to_stbox",
                    "returnType": {"c": "STBox *"},
                    "params": [
                      {"name": "t", "cType": "TimestampTz"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // Wrapper uses OffsetDateTime
            assertTrue(out.contains("public static Pointer timestamptz_to_stbox(OffsetDateTime t)"));
            // Conversion line emitted
            assertTrue(out.contains("var t_new = t.toEpochSecond()"));
            // Interface uses long
            assertTrue(out.contains("Pointer timestamptz_to_stbox(long t)"));
        }

        @Test
        @DisplayName("TimestampTz return type generates OffsetDateTime wrapper")
        void timestampTzReturnType() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "tinstant_timestamptz",
                    "returnType": {"c": "TimestampTz"},
                    "params": [
                      {"name": "inst", "cType": "TInstant *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("public static OffsetDateTime tinstant_timestamptz(Pointer inst)"));
            assertTrue(out.contains("Instant.ofEpochSecond(_result).atOffset"));
        }

        @Test
        @DisplayName("duplicate name#arity functions are deduplicated")
        void deduplication() throws Exception {
            String json = """
        {
          "functions": [
            {
              "name": "meos_initialize",
              "returnType": {"c": "void"},
              "params": []
            },
            {
              "name": "meos_initialize",
              "returnType": {"c": "void"},
              "params": []
            }
          ]
        }
        """;
            String out = generateFromJson(json);

            // Count only interface declarations (end with ';')
            int interfaceOccurrences = out.split("void meos_initialize\\(\\);").length - 1;
            assertEquals(1, interfaceOccurrences, "Interface declaration should appear exactly once");

            // Count only static wrapper declarations
            int wrapperOccurrences = out.split("public static void meos_initialize\\(\\)").length - 1;
            assertEquals(1, wrapperOccurrences, "Static wrapper should appear exactly once");
        }

        @Test
        @DisplayName("Java keyword as param name is sanitized")
        void javaKeywordParam() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "some_function",
                    "returnType": {"c": "void"},
                    "params": [
                      {"name": "int", "cType": "int32_t"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // 'int' should be renamed to 'int_param'
            assertTrue(out.contains("int_param"));
            assertFalse(out.contains("(int int)"));
        }

        @Test
        @DisplayName("size/wkb_size params use long in interface")
        void sizeLongOverride() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "tsequence_make",
                    "returnType": {"c": "TSequence *"},
                    "params": [
                      {"name": "instants", "cType": "TInstant **"},
                      {"name": "size",     "cType": "int32_t"},
                      {"name": "lower_inc","cType": "bool"},
                      {"name": "upper_inc","cType": "bool"},
                      {"name": "interp",   "cType": "interpType"},
                      {"name": "normalize","cType": "bool"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // 'size' must be long even though cType is int32_t
            assertTrue(out.contains("long size"));
        }

        @Test
        @DisplayName("output file is created and parent directories are created")
        void outputDirectoryCreated() throws Exception {
            Path deepOutput = tempDir.resolve("a/b/c/GeneratedFunctions.java");
            run.invoke(generator,
                    writeJson("{\"functions\": []}"),
                    deepOutput.toString());
            assertTrue(Files.exists(deepOutput));
        }

        @Test
        @DisplayName("generated file contains package declaration and imports")
        void fileHeaderCorrect() throws Exception {
            String out = generateFromJson("{\"functions\": []}");
            assertTrue(out.contains("package functions;"));
            assertTrue(out.contains("import jnr.ffi.Pointer;"));
            assertTrue(out.contains("import jnr.ffi.Memory;"));
            assertTrue(out.contains("import jnr.ffi.Runtime;"));
            assertTrue(out.contains("import java.time.*;"));
        }

        @Test
        @DisplayName("enum from JSON section is mapped to int in generated code")
        void enumFromJsonSection() throws Exception {
            String json = """
                {
                  "enums": [{"name": "MyOp"}],
                  "functions": [{
                    "name": "do_op",
                    "returnType": {"c": "void"},
                    "params": [
                      {"name": "op", "cType": "MyOp"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // MyOp should be mapped to int
            assertTrue(out.contains("int op"));
        }

        // Helper: writes JSON to a temp file and returns its path as String
        private String writeJson(String json) throws IOException {
            Path p = tempDir.resolve("h-" + System.nanoTime() + ".json");
            Files.writeString(p, json);
            return p.toString();
        }
    }
}