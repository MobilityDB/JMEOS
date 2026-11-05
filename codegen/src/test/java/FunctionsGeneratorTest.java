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
    private Method isOwnedCharReturn;
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
        isOwnedCharReturn     = reflect("isOwnedCharReturn",     String.class);
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

    private boolean isOwnedChar(String retCType) throws Exception {
        return (boolean) isOwnedCharReturn.invoke(generator, retCType);
    }

    private Object resolveStrategy(String cType) throws Exception {
        return resolveResultStrategy.invoke(generator, cType);
    }

    /** The PostgreSQL names MEOS keeps, defined as the catalog's {@code typedefs} defines them. */
    private static final String TYPEDEFS = """
            "typedefs": [{"name": "DateADT", "canonical": "int32_t"},
                         {"name": "Datum", "canonical": "uintptr_t"},
                         {"name": "Oid", "canonical": "unsigned int"},
                         {"name": "TimeADT", "canonical": "int64_t"},
                         {"name": "Timestamp", "canonical": "int64_t"},
                         {"name": "TimestampTz", "canonical": "int64_t"},
                         {"name": "text", "canonical": "struct varlena"}]
            """;

    /** Reads {@link #TYPEDEFS} and the named structures into the generator, as a run does. */
    private void readCatalogNames(String... structs) throws Exception {
        StringBuilder json = new StringBuilder("{").append(TYPEDEFS).append(", \"structs\": [");
        for (int i = 0; i < structs.length; i++) {
            json.append(i == 0 ? "" : ", ").append("{\"name\": \"").append(structs[i]).append("\"}");
        }
        JsonNode root = new ObjectMapper().readTree(json.append("]}").toString());
        reflect("collectTypedefs", JsonNode.class).invoke(generator, root);
        reflect("collectStructNames", JsonNode.class).invoke(generator, root);
    }

    /** The message of what a reflected call throws, which the generation stops with. */
    private static String thrownBy(org.junit.jupiter.api.function.Executable call) {
        Throwable t = assertThrows(java.lang.reflect.InvocationTargetException.class, call).getCause();
        return t.getMessage();
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
        /** A char passed by value is one byte; only a char * is a String. */
        @Test void char_() throws Exception        { assertEquals("byte",    mapJava("char")); }
        /** An array parameter is passed as a pointer to its first element, a char[N] included. */
        @Test void charArray() throws Exception    { assertEquals("Pointer", mapJava("char[10]")); }
        @Test void constArray() throws Exception   { assertEquals("Pointer", mapJava("const double[3]")); }
        @Test void nestedArray() throws Exception  { assertEquals("Pointer", mapJava("double[11][2]")); }
        @Test void float_() throws Exception       { assertEquals("float",   mapJava("float")); }
        @Test void double_() throws Exception      { assertEquals("double",  mapJava("double")); }
        @Test void int_() throws Exception         { assertEquals("int",     mapJava("int")); }
        @Test void int32_t() throws Exception      { assertEquals("int",     mapJava("int32_t")); }
        @Test void uint32_t() throws Exception     { assertEquals("int",     mapJava("uint32_t")); }
        @Test void unsignedInt() throws Exception  { assertEquals("int",     mapJava("unsigned int")); }
        @Test void long_() throws Exception        { assertEquals("long",    mapJava("long")); }
        @Test void int64_t() throws Exception      { assertEquals("long",    mapJava("int64_t")); }
        @Test void uint64_t() throws Exception     { assertEquals("long",    mapJava("uint64_t")); }
        @Test void size_t() throws Exception       { assertEquals("long",    mapJava("size_t")); }
        @Test void uintptr_t() throws Exception    { assertEquals("long",    mapJava("uintptr_t")); }
        @Test void int16_t() throws Exception      { assertEquals("short",   mapJava("int16_t")); }
        @Test void int8_t() throws Exception       { assertEquals("byte",    mapJava("int8_t")); }

        @Test
        @DisplayName("a name MEOS keeps from PostgreSQL is mapped by the type the catalog defines it as")
        void keptNamesResolveThroughTypedefs() throws Exception {
            readCatalogNames();
            assertEquals("int",  mapJava("DateADT"));
            assertEquals("long", mapJava("Timestamp"));
            assertEquals("long", mapJava("TimestampTz"));
            assertEquals("long", mapJava("const TimestampTz"));
            assertEquals("long", mapJava("TimeADT"));
            assertEquals("long", mapJava("Datum"));
            assertEquals("int",  mapJava("Oid"));
            assertEquals("Pointer", mapJava("text *"));
        }

        @Test
        @DisplayName("a kept name the catalog defines no type for stops the mapping")
        void keptNameWithoutTypedefStops() {
            assertTrue(thrownBy(() -> mapJava("TimestampTz")).contains("'TimestampTz'"));
        }

        @Test
        @DisplayName("a slot is mapped by its canonical type, whatever its declared spelling")
        void slotTypeIsTheCanonical() throws Exception {
            ObjectMapper m = new ObjectMapper();
            JsonNode quadbin = m.readTree(
                    "{\"cType\": \"uint64_t\", \"canonical\": \"uint64_t\", \"typedef\": \"Quadbin\"}");
            JsonNode cells = m.readTree(
                    "{\"c\": \"uint64_t *\", \"canonical\": \"uint64_t *\", \"typedef\": \"S2CellId\"}");
            JsonNode bigint = m.readTree("{\"cType\": \"int64\", \"canonical\": \"int64_t\"}");
            JsonNode real = m.readTree("{\"cType\": \"float4\", \"canonical\": \"float\"}");
            assertEquals("long",       mapJava(FunctionsGenerator.slotType(quadbin)));
            assertEquals("uint64_t *", FunctionsGenerator.slotType(cells));
            assertEquals("int64_t",    FunctionsGenerator.slotType(bigint));
            assertEquals("long",       mapJava(FunctionsGenerator.slotType(bigint)));
            assertEquals("float",      mapJava(FunctionsGenerator.slotType(real)));
        }

        @Test
        @DisplayName("a slot stating no canonical type stops the generation")
        void slotWithoutCanonicalStops() throws Exception {
            JsonNode slot = new ObjectMapper().readTree("{\"cType\": \"float4\"}");
            IllegalStateException e = assertThrows(IllegalStateException.class,
                    () -> FunctionsGenerator.slotType(slot));
            assertTrue(e.getMessage().contains("states no canonical type"));
        }

        @Test void charPointer() throws Exception  { assertEquals("String",  mapJava("char *")); }
        @Test void structPointer() throws Exception { assertEquals("Pointer", mapJava("STBox *")); }
        @Test void doublePointer() throws Exception { assertEquals("Pointer", mapJava("int **")); }
        @Test void functionPointer() throws Exception { assertEquals("Pointer", mapJava("void (*)()")); }
        @Test void errorHandlerFn() throws Exception { assertEquals("error_handler_fn", mapJava("error_handler_fn")); }

        @Test
        @DisplayName("a callback of the catalog not bound to an interface is the address of a function")
        void catalogCallback() throws Exception {
            JsonNode root = new ObjectMapper().readTree("{\"callbacks\": [{\"name\": \"meos_free_fn\"}]}");
            reflect("collectTypedefs", JsonNode.class).invoke(generator, root);
            assertEquals("Pointer", mapJava("meos_free_fn"));
        }

        @Test
        @DisplayName("const prefix is stripped before mapping")
        void constPrefix() throws Exception {
            assertEquals("Pointer", mapJava("const STBox *"));
            assertEquals("double",  mapJava("const double"));
        }

        @Test
        @DisplayName("a type with no Java type stops the mapping, naming it")
        void unknownType() {
            assertTrue(thrownBy(() -> mapJava("SomeUnknownStruct"))
                    .contains("the catalog type 'SomeUnknownStruct' maps to no Java type"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"interpType", "IndexSearchOp", "tempSubtype", "spatialRel", "errorCode"})
        @DisplayName("a name the catalog states no enum of is not an enum")
        void enumOnlyFromCatalog(String cType) {
            assertTrue(thrownBy(() -> mapJava(cType)).contains("'" + cType + "'"));
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
            readCatalogNames();
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
    // isOwnedCharReturn
    // =========================================================================

    @Nested
    @DisplayName("isOwnedCharReturn")
    class IsOwnedCharReturnTests {

        @Test void ownedCharPtr_true() throws Exception      { assertTrue(isOwnedChar("char *")); }
        @Test void ownedCharPtrNoSpace_true() throws Exception { assertTrue(isOwnedChar("char*")); }

        @Test void constCharPtr_false() throws Exception     { assertFalse(isOwnedChar("const char *")); }
        @Test void doubleCharPtr_false() throws Exception    { assertFalse(isOwnedChar("char **")); }
        @Test void text_false() throws Exception             { assertFalse(isOwnedChar("text *")); }
        @Test void Pointer_false() throws Exception          { assertFalse(isOwnedChar("Temporal *")); }
        @Test void void_false() throws Exception             { assertFalse(isOwnedChar("void")); }
        @Test void int_false() throws Exception              { assertFalse(isOwnedChar("int")); }
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
            readCatalogNames();
            Object s = resolveStrategy("DateADT *");
            assertEquals("Integer.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void TimestampTz_scalar() throws Exception {
            readCatalogNames();
            Object s = resolveStrategy("TimestampTz *");
            assertEquals("Long.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        /** A Datum is a uintptr_t, a scalar read at its width, never dereferenced as an address. */
        @Test void Datum_scalar() throws Exception {
            readCatalogNames();
            Object s = resolveStrategy("Datum *");
            assertEquals("Long.BYTES", allocExpr(s));
            assertFalse(isPointer(s));
        }

        @Test void structPointer_pointer() throws Exception {
            readCatalogNames("Span");
            Object s = resolveStrategy("Span *");
            assertEquals("Long.BYTES", allocExpr(s));
            assertTrue(isPointer(s));
        }

        @Test void structTag_pointer() throws Exception {
            Object s = resolveStrategy("struct Pose **");
            assertEquals("Long.BYTES", allocExpr(s));
            assertTrue(isPointer(s));
        }

        @Test void unknownType_stops() {
            assertTrue(thrownBy(() -> resolveStrategy("SomeStruct *")).contains("'SomeStruct'"));
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
                    {"name": "IndexSearchOp"},
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

            assertTrue(names.contains("IndexSearchOp"));
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
    // The Java enums of the catalog
    // =========================================================================

    @Nested
    @DisplayName("enum classes")
    class EnumClassTests {

        private JsonNode enumNode(String json) throws Exception {
            return new ObjectMapper().readTree(json);
        }

        private boolean emits(JsonNode e) throws Exception {
            Method m = FunctionsGenerator.class.getDeclaredMethod("emitsEnum", JsonNode.class);
            m.setAccessible(true);
            return (boolean) m.invoke(generator, e);
        }

        @Test
        @DisplayName("an enum keeps its C name and the value MEOS gives each constant")
        void enumSourceKeepsNamesAndValues() throws Exception {
            String src = FunctionsGenerator.enumSource(enumNode("""
                {"name": "MeosType", "file": "meos_catalog.h", "family": "CORE",
                 "values": [{"name": "T_UNKNOWN", "value": 0}, {"name": "T_DOUBLE2", "value": 8},
                            {"name": "T_FLOAT8", "value": 11}]}
                """));
            assertTrue(src.startsWith("package functions;"));
            assertTrue(src.contains("public enum MeosType {"));
            assertTrue(src.contains("\tT_UNKNOWN(0),\n\tT_DOUBLE2(8),\n\tT_FLOAT8(11);\n"));
            assertTrue(src.contains("public int getValue()"));
        }

        @Test
        @DisplayName("the enums of a public header are written, internal and vendored ones are not")
        void emitsPublicEnumsOnly() throws Exception {
            assertTrue(emits(enumNode("""
                {"name": "IndexSearchOp", "file": "meos.h", "family": "CORE", "vendored": false,
                 "values": [{"name": "INDEX_OVERLAPS", "value": 0}]}
                """)));
            assertFalse(emits(enumNode("""
                {"name": "SkipListType", "file": "meos_internal.h", "family": "CORE", "vendored": false,
                 "values": [{"name": "TIMESTAMPTZ", "value": 0}]}
                """)));
            assertFalse(emits(enumNode("""
                {"name": "ENDIANS", "file": "pc_api.h", "family": "POINTCLOUD", "vendored": true,
                 "values": [{"name": "PC_XDR", "value": 0}]}
                """)));
        }

        @Test
        @DisplayName("run writes each enum beside the generated functions")
        void runWritesEnumFiles() throws Exception {
            Path input = tempDir.resolve("idl.json");
            Files.writeString(input, """
                {"enums": [{"name": "IndexSearchOp", "file": "meos.h", "family": "CORE",
                            "values": [{"name": "INDEX_OVERLAPS", "value": 0},
                                       {"name": "INDEX_ADJACENT", "value": 20}]},
                           {"name": "SkipListType", "file": "meos_internal.h", "family": "CORE",
                            "values": [{"name": "TIMESTAMPTZ", "value": 0}]}],
                 "functions": []}
                """);
            Path out = tempDir.resolve("functions/GeneratedFunctions.java");
            run.invoke(generator, input.toString(), out.toString());
            Path written = tempDir.resolve("functions/IndexSearchOp.java");
            assertTrue(Files.exists(written));
            assertTrue(Files.readString(written).contains("INDEX_ADJACENT(20);"));
            assertFalse(Files.exists(tempDir.resolve("functions/SkipListType.java")));
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
                    "returnType": {"c": "void", "canonical": "void"},
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
                    "returnType": {"c": "bool", "canonical": "bool"},
                    "params": [
                      {"name": "box1", "cType": "STBox *", "canonical": "STBox *"},
                      {"name": "box2", "cType": "STBox *", "canonical": "STBox *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("boolean overlaps_stbox_stbox(Pointer box1, Pointer box2)"));
            assertTrue(out.contains("public static boolean overlaps_stbox_stbox(Pointer box1, Pointer box2)"));
        }

        @Test
        @DisplayName("a callback handed to MEOS is held in a static field before the call")
        void callbackIsHeldBeforeTheCall() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "meos_initialize_error_handler",
                    "returnType": {"c": "void", "canonical": "void"},
                    "params": [
                      {"name": "err_handler", "cType": "error_handler_fn", "canonical": "error_handler_fn"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            String field = "_callback_meos_initialize_error_handler_err_handler";
            assertTrue(out.contains("private static volatile error_handler_fn " + field + ";"));
            String wrapper = out.substring(
                    out.indexOf("public static void meos_initialize_error_handler(error_handler_fn err_handler)"));
            int held = wrapper.indexOf(field + " = err_handler;");
            int call = wrapper.indexOf("_meos_a.meos_initialize_error_handler(err_handler);");
            assertTrue(held >= 0, "the wrapper holds the callback");
            assertTrue(call > held, "the callback is held before MEOS receives it");
        }

        @Test
        @DisplayName("a function pointer bound as Pointer is not held")
        void pointerCallbackIsNotHeld() throws Exception {
            String json = """
                {
                  "callbacks": [{"name": "interrupt_handler_fn"}],
                  "functions": [{
                    "name": "meos_initialize_interrupt_handler",
                    "returnType": {"c": "void", "canonical": "void"},
                    "params": [
                      {"name": "handler", "cType": "interrupt_handler_fn", "canonical": "interrupt_handler_fn"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertFalse(out.contains("_callback_meos_initialize_interrupt_handler"));
        }

        @Test
        @DisplayName("meos_initialize installs the binding's error handler after MEOS returns")
        void initializeInstallsBindingHandlerAfterTheCall() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "meos_initialize",
                    "returnType": {"c": "void", "canonical": "void"},
                    "params": []
                  }]
                }
                """;
            String out = generateFromJson(json);
            String wrapper = out.substring(out.indexOf("public static void meos_initialize()"));
            wrapper = wrapper.substring(0, wrapper.indexOf("\n\t}\n"));
            int call = wrapper.indexOf("_meos_a.meos_initialize();");
            int install = wrapper.indexOf("meos_initialize_error_handler(ERROR_HANDLER);");
            int check = wrapper.indexOf("MeosErrorHandler.checkError();");
            assertTrue(call >= 0, "the wrapper calls MEOS");
            assertTrue(install > call, "the binding's handler is installed after MEOS returns");
            assertTrue(check > install, "the error check follows the installation");
        }

        @Test
        @DisplayName("a function leaving the error handler alone does not install it")
        void otherFunctionDoesNotInstallBindingHandler() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "meos_finalize",
                    "returnType": {"c": "void", "canonical": "void"},
                    "params": []
                  }]
                }
                """;
            String out = generateFromJson(json);
            String wrapper = out.substring(out.indexOf("public static void meos_finalize()"));
            wrapper = wrapper.substring(0, wrapper.indexOf("\n\t}\n"));
            assertFalse(wrapper.contains("meos_initialize_error_handler(ERROR_HANDLER);"));
        }

        @Test
        @DisplayName("bool+result pattern hides result param from wrapper")
        void boolResultPattern() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "stbox_xmax",
                    "returnType": {"c": "bool", "canonical": "bool"},
                    "shape": {"outParams": ["result"]},
                    "params": [
                      {"name": "box",    "cType": "STBox *", "canonical": "STBox *"},
                      {"name": "result", "cType": "double *", "canonical": "double *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // Wrapper signature should NOT include result
            assertTrue(out.contains("public static Pointer stbox_xmax(Pointer box)"));
            // result is allocated internally
            assertTrue(out.contains("Memory.allocateDirect(_runtime, Double.BYTES)"));
            // scalar strategy: no getPointer(0) dereference
            assertFalse(out.contains("_buffer.getPointer(0)"));
            assertTrue(out.contains("return _found ? _buffer : null"));
        }

        @Test
        @DisplayName("bool+result with struct pointer result uses getPointer(0)")
        void boolResultPointerPattern() throws Exception {
            String json = """
                {
                  "structs": [{"name": "STBox"}],
                  "functions": [{
                    "name": "temporal_bbox",
                    "returnType": {"c": "bool", "canonical": "bool"},
                    "shape": {"outParams": ["result"]},
                    "params": [
                      {"name": "temp",   "cType": "Temporal *", "canonical": "Temporal *"},
                      {"name": "result", "cType": "STBox *", "canonical": "STBox *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("Memory.allocateDirect(_runtime, Long.BYTES)"));
            assertTrue(out.contains("_buffer.getPointer(0)"));
            assertTrue(out.contains("return _found ? _value : null"));
        }

        @Test
        @DisplayName("size_out param is hidden and allocated internally")
        void sizeOutHidden() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "set_as_wkb",
                    "returnType": {"c": "uint8_t *", "canonical": "uint8_t *"},
                    "shape": {"outParams": ["size_out"]},
                    "params": [
                      {"name": "s",        "cType": "Set *", "canonical": "Set *"},
                      {"name": "size_out", "cType": "size_t *", "canonical": "size_t *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // size_out must not appear in wrapper signature, and the bytes come back as byte[]
            assertTrue(out.contains("public static byte[] set_as_wkb(Pointer s)"));
            // but is still allocated internally
            assertTrue(out.contains("Pointer size_out = Memory.allocateDirect(_runtime, Long.BYTES)"));
        }

        @Test
        @DisplayName("a returned WKB buffer comes back as byte[] sized by size_out")
        void byteResultReturnsBytes() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "temporal_as_wkb",
                    "returnType": {"c": "uint8_t *", "canonical": "uint8_t *"},
                    "shape": {"outParams": ["size_out"]},
                    "params": [
                      {"name": "temp",     "cType": "const Temporal *", "canonical": "const Temporal *"},
                      {"name": "variant",  "cType": "uint8_t", "canonical": "uint8_t"},
                      {"name": "size_out", "cType": "size_t *", "canonical": "size_t *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("public static byte[] temporal_as_wkb(Pointer temp, byte variant)"));
            assertTrue(out.contains("byte[] _bytes = new byte[(int) size_out.getLong(0)];"));
            assertTrue(out.contains("_result.get(0, _bytes, 0, _bytes.length);"));
            assertTrue(out.contains("return _bytes;"));
        }

        @Test
        @DisplayName("a returned string is read for the size MEOS writes, less its terminator")
        void sizedStringIsReadForItsSize() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "ttext_out",
                    "returnType": {"c": "char *", "canonical": "char *"},
                    "shape": {"outParams": ["size_out"]},
                    "params": [
                      {"name": "temp",     "cType": "const Temporal *", "canonical": "const Temporal *"},
                      {"name": "size_out", "cType": "size_t *", "canonical": "size_t *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("public static String ttext_out(Pointer temp)"));
            assertTrue(out.contains("byte[] _bytes = new byte[(int) size_out.getLong(0) - 1];"));
            assertTrue(out.contains("new String(_bytes, java.nio.charset.StandardCharsets.UTF_8)"));
            assertTrue(out.contains("_freeCStr(_result);"));
            assertFalse(out.contains("getString"));
        }

        @Test
        @DisplayName("a returned string with no size is scanned for its terminator")
        void unsizedStringIsScanned() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "some_name",
                    "returnType": {"c": "char *", "canonical": "char *"},
                    "params": [
                      {"name": "temp", "cType": "const Temporal *", "canonical": "const Temporal *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("String _str = _result.getString(0);"));
        }

        @Test
        @DisplayName("an input WKB buffer and its length are one byte[] argument")
        void byteInputTakesBytes() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "temporal_from_wkb",
                    "returnType": {"c": "Temporal *", "canonical": "Temporal *"},
                    "params": [
                      {"name": "wkb",  "cType": "const uint8_t *", "canonical": "const uint8_t *"},
                      {"name": "size", "cType": "size_t", "canonical": "size_t"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("public static Pointer temporal_from_wkb(byte[] wkb)"));
            assertTrue(out.contains("wkb_buf.put(0, wkb, 0, wkb.length);"));
            int call = out.indexOf("_meos_a.temporal_from_wkb(wkb_buf, (long) wkb.length);");
            int fence = out.indexOf("java.lang.ref.Reference.reachabilityFence(wkb_buf);");
            assertTrue(call > 0, "the wrapper passes the copied buffer and its length");
            assertTrue(fence > call, "the copied buffer stays reachable across the call");
        }

        @Test
        @DisplayName("size_out stays reachable until MEOS has written the size into it")
        void sizeOutFencedAfterCall() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "temporal_as_hexwkb",
                    "returnType": {"c": "char *", "canonical": "char *"},
                    "shape": {"outParams": ["size_out"]},
                    "params": [
                      {"name": "temp",     "cType": "const Temporal *", "canonical": "const Temporal *"},
                      {"name": "variant",  "cType": "uint8_t", "canonical": "uint8_t"},
                      {"name": "size_out", "cType": "size_t *", "canonical": "size_t *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            int call = out.indexOf("_meos_a.temporal_as_hexwkb(temp, variant, size_out);");
            int fence = out.indexOf("java.lang.ref.Reference.reachabilityFence(size_out);");
            int check = out.indexOf("MeosErrorHandler.checkError();", call);
            assertTrue(call > 0, "the wrapper calls MEOS with size_out");
            assertTrue(fence > call, "the fence follows the call");
            assertTrue(fence < check, "the fence precedes the first statement after the call");
        }


        @Test
        @DisplayName("shape.outParams folds by the flag regardless of the param name")
        void outParamFoldsByFlagNotName() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "tint_value_at_timestamptz",
                    "returnType": {"c": "bool", "canonical": "bool"},
                    "shape": {"outParams": ["value"]},
                    "params": [
                      {"name": "temp",   "cType": "Temporal *", "canonical": "Temporal *"},
                      {"name": "strict", "cType": "bool", "canonical": "bool"},
                      {"name": "value",  "cType": "int *", "canonical": "int *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // `value` is flagged, so it folds even though it is not named "result":
            // hidden from the signature, allocated internally, and returned.
            assertTrue(out.contains(
                "public static Pointer tint_value_at_timestamptz(Pointer temp, boolean strict)"));
            assertTrue(out.contains("Pointer _buffer = Memory.allocateDirect(_runtime, Integer.BYTES)"));
            assertTrue(out.contains("return _found ? _buffer : null"));
        }

        @Test
        @DisplayName("a visible parameter named like a wrapper local keeps its name and its value")
        void visibleParamNamedResultIsNotShadowed() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "index_result_id",
                    "returnType": {"c": "bool", "canonical": "bool"},
                    "shape": {"outParams": ["id"]},
                    "params": [
                      {"name": "result", "cType": "const MeosArray *", "canonical": "const MeosArray *"},
                      {"name": "n",      "cType": "int", "canonical": "int"},
                      {"name": "id",     "cType": "int64_t *", "canonical": "int64_t *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // `result` is an input here: it stays in the signature and reaches MEOS unchanged,
            // while the folded `id` travels in the wrapper's own `_buffer`.
            assertTrue(out.contains("public static Pointer index_result_id(Pointer result, int n)"));
            assertTrue(out.contains("index_result_id(result, n, _buffer)"));
            assertFalse(out.contains("Pointer result ="));
            assertFalse(out.contains("boolean out;"));
            assertFalse(out.contains("Runtime runtime ="));
        }

        @Test
        @DisplayName("an unflagged trailing pointer stays a visible parameter")
        void unflaggedTrailingPointerIsNotFolded() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "contains_set_geo",
                    "returnType": {"c": "bool", "canonical": "bool"},
                    "params": [
                      {"name": "s",  "cType": "Set *", "canonical": "Set *"},
                      {"name": "gs", "cType": "GSERIALIZED *", "canonical": "GSERIALIZED *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // No shape.outParams: gs is an input, so it stays in the wrapper signature
            // and the wrapper keeps the boolean return.
            assertTrue(out.contains(
                "public static boolean contains_set_geo(Pointer s, Pointer gs)"));
        }

        @Test
        @DisplayName("TimestampTz param generates epoch conversion in wrapper")
        void timestampTzConversion() throws Exception {
            String json = """
                {
                  "typedefs": [{"name": "TimestampTz", "canonical": "int64_t"}],
                  "functions": [{
                    "name": "timestamptz_to_stbox",
                    "returnType": {"c": "STBox *", "canonical": "STBox *"},
                    "params": [
                      {"name": "t", "cType": "TimestampTz", "canonical": "TimestampTz"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            // Wrapper uses OffsetDateTime
            assertTrue(out.contains("public static Pointer timestamptz_to_stbox(OffsetDateTime t)"));
            // Conversion routed through the converter, not inline epoch math
            assertTrue(out.contains("var t_new = utils.TimestampTzConverter.toTimestampTz(t)"));
            assertFalse(out.contains(".toEpochSecond()"));
            // Interface uses long
            assertTrue(out.contains("Pointer timestamptz_to_stbox(long t)"));
        }

        @Test
        @DisplayName("TimestampTz return type generates OffsetDateTime wrapper")
        void timestampTzReturnType() throws Exception {
            String json = """
                {
                  "typedefs": [{"name": "TimestampTz", "canonical": "int64_t"}],
                  "functions": [{
                    "name": "tinstant_timestamptz",
                    "returnType": {"c": "TimestampTz", "canonical": "TimestampTz"},
                    "params": [
                      {"name": "inst", "cType": "TInstant *", "canonical": "TInstant *"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("public static OffsetDateTime tinstant_timestamptz(Pointer inst)"));
            assertTrue(out.contains("return utils.TimestampTzConverter.toOffsetDateTime(_result);"));
            assertFalse(out.contains("Instant.ofEpochSecond"));
        }

        @Test
        @DisplayName("a float4 slot passes a float, the width its canonical states")
        void float4SlotPassesAFloat() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "add_float4_float4",
                    "returnType": {"c": "float4", "canonical": "float"},
                    "params": [
                      {"name": "num1", "cType": "float4", "canonical": "float"},
                      {"name": "num2", "cType": "float4", "canonical": "float"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("float add_float4_float4(float num1, float num2);"));
            assertTrue(out.contains("public static float add_float4_float4(float num1, float num2)"));
        }

        @Test
        @DisplayName("a Datum is passed as the uintptr_t the catalog defines it as")
        void datumSlotPassesALong() throws Exception {
            String json = """
                {
                  "typedefs": [{"name": "Datum", "canonical": "uintptr_t"}],
                  "functions": [{
                    "name": "datum_floor",
                    "returnType": {"c": "Datum", "canonical": "Datum"},
                    "params": [{"name": "d", "cType": "Datum", "canonical": "Datum"}]
                  }]
                }
                """;
            assertTrue(generateFromJson(json).contains("public static long datum_floor(long d)"));
        }

        @Test
        @DisplayName("a slot whose type maps to no Java type stops the generation, naming type and function")
        void unmappedTypeStopsTheGeneration() {
            String json = """
                {
                  "functions": [{
                    "name": "time_make",
                    "returnType": {"c": "TimeADT", "canonical": "TimeADT"},
                    "params": []
                  }]
                }
                """;
            Throwable t = assertThrows(java.lang.reflect.InvocationTargetException.class,
                    () -> generateFromJson(json)).getCause();
            assertEquals("function time_make: the catalog type 'TimeADT' maps to no Java type",
                    t.getMessage());
        }

        @Test
        @DisplayName("a function with a long double slot is left out and the others are generated")
        void longDoubleSlotLeavesTheFunctionOut() throws Exception {
            String json = """
                {
                  "functions": [
                    {"name": "cbuffersegm_interpolate",
                     "returnType": {"c": "Cbuffer *", "canonical": "Cbuffer *"},
                     "params": [{"name": "ratio", "cType": "long double", "canonical": "long double"}]},
                    {"name": "cbuffersegm_locate",
                     "returnType": {"c": "long double", "canonical": "long double"},
                     "params": [{"name": "start", "cType": "const Cbuffer *",
                                 "canonical": "const Cbuffer *"}]},
                    {"name": "meos_initialize",
                     "returnType": {"c": "void", "canonical": "void"},
                     "params": []}
                  ]
                }
                """;
            String out = generateFromJson(json);
            assertFalse(out.contains("cbuffersegm_interpolate"));
            assertFalse(out.contains("cbuffersegm_locate"));
            assertTrue(out.contains("public static void meos_initialize()"));
        }

        @Test
        @DisplayName("duplicate name#arity functions are deduplicated")
        void deduplication() throws Exception {
            String json = """
        {
          "functions": [
            {
              "name": "meos_initialize",
              "returnType": {"c": "void", "canonical": "void"},
              "params": []
            },
            {
              "name": "meos_initialize",
              "returnType": {"c": "void", "canonical": "void"},
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
                    "returnType": {"c": "void", "canonical": "void"},
                    "params": [
                      {"name": "int", "cType": "int32_t", "canonical": "int32_t"}
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
        @DisplayName("a parameter takes the Java type of its C type whatever its name")
        void paramTypeFollowsCTypeNotName() throws Exception {
            String json = """
                {
                  "functions": [{
                    "name": "tint_value_split",
                    "returnType": {"c": "Temporal **", "canonical": "Temporal **"},
                    "params": [
                      {"name": "temp",     "cType": "const Temporal *", "canonical": "const Temporal *"},
                      {"name": "size",     "cType": "int", "canonical": "int"},
                      {"name": "wkb_size", "cType": "int32_t", "canonical": "int32_t"},
                      {"name": "vsize",    "cType": "size_t", "canonical": "size_t"}
                    ]
                  }]
                }
                """;
            String out = generateFromJson(json);
            assertTrue(out.contains("int size"));
            assertTrue(out.contains("int wkb_size"));
            assertTrue(out.contains("long vsize"));
            assertFalse(out.contains("long size"));
            assertFalse(out.contains("long wkb_size"));
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
                  "enums": [{"name": "MyOp", "file": "meos.h", "family": "CORE", "values": [{"name": "MY_OP", "value": 0}]}],
                  "functions": [{
                    "name": "do_op",
                    "returnType": {"c": "void", "canonical": "void"},
                    "params": [
                      {"name": "op", "cType": "MyOp", "canonical": "MyOp"}
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

    // =========================================================================
    // Family gating: -D<FAMILY>=OFF drops an optional family
    // =========================================================================

    @Nested
    @DisplayName("family gating")
    class FamilyGatingTests {

        // A core function and a pointcloud function, tagged by the IDL family field.
        private static final String FIELD_JSON = """
            {
              "functions": [
                {"name": "temporal_num_instants", "family": "CORE",
                 "returnType": {"c": "int", "canonical": "int"},
                 "params": [{"name": "temp", "cType": "Temporal *", "canonical": "Temporal *"}]},
                {"name": "pcpatch_num_points", "family": "POINTCLOUD",
                 "returnType": {"c": "int", "canonical": "int"},
                 "params": [{"name": "patch", "cType": "const Pcpatch *", "canonical": "const struct Pcpatch *"}]}
              ]
            }
            """;

        @Test
        @DisplayName("family field: all families emitted by default")
        void fieldDefaultEmitsAll() throws Exception {
            String out = generateFromJson(FIELD_JSON);
            assertTrue(out.contains("temporal_num_instants"));
            assertTrue(out.contains("pcpatch_num_points"));
        }

        @Test
        @DisplayName("family field: -DPOINTCLOUD=OFF drops pointcloud, keeps core")
        void fieldPointcloudOff() throws Exception {
            System.setProperty("POINTCLOUD", "OFF");
            try {
                String out = generateFromJson(FIELD_JSON);
                assertTrue(out.contains("temporal_num_instants"), "core stays");
                assertFalse(out.contains("pcpatch_num_points"), "pointcloud dropped");
            } finally {
                System.clearProperty("POINTCLOUD");
            }
        }

        @Test
        @DisplayName("fallback: header basename gates when the family field is absent")
        void headerFallbackGates() throws Exception {
            String json = """
                {
                  "functions": [
                    {"name": "cbuffer_out", "file": "meos_cbuffer.h",
                     "returnType": {"c": "char *", "canonical": "char *"},
                     "params": [{"name": "cb", "cType": "const Cbuffer *", "canonical": "const struct Cbuffer *"}]}
                  ]
                }
                """;
            System.setProperty("CBUFFER", "OFF");
            try {
                assertFalse(generateFromJson(json).contains("cbuffer_out"));
            } finally {
                System.clearProperty("CBUFFER");
            }
        }
    }
}