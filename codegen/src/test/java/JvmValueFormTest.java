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
 * The form a Flink or Spark value of tools/codegen_jvm.py carries (#_value_methods there), read
 * from the catalog: the memory image where typeEncodings[C].image states one for its C type and
 * the jar binds the functions it names, else the form its codec writes. The generator runs over
 * a catalog of four value types and the jar signatures it reads, so no jar and no libmeos. Its
 * input directory is removed as #tearDown of FunctionsGeneratorTest removes its own.
 */
class JvmValueFormTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** A function over the SQL type sql whose only parameter is a pointer to the C type c: the
     * pairing the generator reads the C type of a SQL type from. */
    private static String fn(String name, String c, String sql) {
        return "{\"name\": \"" + name + "\", \"file\": \"meos.h\", \"api\": \"public\","
            + " \"sqlfn\": \"" + name + "\","
            + " \"returnType\": {\"c\": \"int\", \"canonical\": \"int\"},"
            + " \"params\": [{\"name\": \"v\", \"cType\": \"const " + c + " *\","
            + " \"canonical\": \"const " + c + " *\"}],"
            + " \"sqlSignatures\": [{\"args\": [\"" + sql + "\"], \"ret\": \"integer\"}]}";
    }

    private static final String CATALOG = "{\"functions\": ["
        + fn("numInstants", "Temporal", "tgeompoint") + ", "
        + fn("spanWidth", "Span", "tstzspan") + ", "
        + fn("boxArea", "GBOX", "box2d") + ", "
        + fn("setCount", "Set", "intset") + "],"
        + " \"typeEncodings\": {"
        + "  \"Temporal\": {\"bytes\": {\"decoder\": \"temporal_from_wkb\", \"encoder\": \"temporal_as_wkb\"},"
        + "    \"image\": {\"copy\": \"temporal_copy\", \"size\": \"temporal_mem_size\"}},"
        + "  \"Span\": {\"bytes\": {\"decoder\": \"span_from_wkb\", \"encoder\": \"span_as_wkb\"},"
        + "    \"image\": {\"copy\": \"span_copy\", \"sizeBytes\": 24}},"
        + "  \"GBOX\": {\"decoders\": {\"text\": \"gbox_in\"}, \"encoders\": {\"text\": \"gbox_out\"}},"
        + "  \"Set\": {\"bytes\": {\"decoder\": \"set_from_wkb\", \"encoder\": \"set_as_wkb\"},"
        + "    \"image\": {\"copy\": \"set_copy_absent\", \"size\": \"set_mem_size\"}}},"
        + " \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": [], \"structs\": []}";

    /** The jar signatures: every function the catalog names, but set_copy_absent. */
    private static final String JAR = "{"
        + "\"numInstants\": {\"ret\": \"int\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"spanWidth\": {\"ret\": \"int\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"boxArea\": {\"ret\": \"int\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"setCount\": {\"ret\": \"int\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"temporal_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"temporal_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]},"
        + "\"temporal_copy\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"temporal_mem_size\": {\"ret\": \"int\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"span_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"span_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]},"
        + "\"span_copy\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"gbox_in\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"java.lang.String\"]},"
        + "\"gbox_out\": {\"ret\": \"java.lang.String\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"set_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"set_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]},"
        + "\"set_mem_size\": {\"ret\": \"int\", \"arg_types\": [\"jnr.ffi.Pointer\"]}}";

    /** Prints {sql: {flink, spark}}: the Flink and the Spark value class of each value type. */
    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_jvm as g",
        "cat = json.load(open(sys.argv[2]))",
        "jar = json.load(open(sys.argv[3]))",
        "f = g.SqlModel(cat, jar)",
        "s = g.SqlModel(cat, jar, g.SPARK_SQL_PKG, 'spark')",
        "print(json.dumps({sql: {'flink': g._value_class_src(f, sql),",
        "                        'spark': g._spark_value_class_src(s, sql)}",
        "                  for sql in sorted(f.codec)}))",
        "");

    private static JsonNode classes;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("value-form");
        try {
            Files.writeString(dir.resolve("catalog.json"), CATALOG);
            Files.writeString(dir.resolve("jar.json"), JAR);
            Files.writeString(dir.resolve("driver.py"), DRIVER);
            Process p = new ProcessBuilder("python3", "-B", dir.resolve("driver.py").toString(),
                TOOLS.toString(), dir.resolve("catalog.json").toString(),
                dir.resolve("jar.json").toString()).redirectErrorStream(true).start();
            String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(p.waitFor(120, TimeUnit.SECONDS), "the generator ran");
            assertEquals(0, p.exitValue(), out);
            classes = new ObjectMapper().readTree(out);
        } finally {
            try (var stream = Files.walk(dir)) {
                stream.sorted(java.util.Comparator.reverseOrder()).forEach(q -> q.toFile().delete());
            }
        }
    }

    private static String src(String sql, String engine) {
        JsonNode c = classes.get(sql);
        assertNotNull(c, sql + " is a value type");
        return c.get(engine).asText();
    }

    @Test
    void allFourTypesAreValueTypes() {
        assertEquals(4, classes.size(), classes.toString());
    }

    @Test
    void anImageCarriedValueDecodesThroughItsCopyAndEncodesByItsSize() {
        for (String engine : new String[] {"flink", "spark"}) {
            String s = src("tgeompoint", engine);
            assertTrue(s.contains("return MeosSqlRuntime.fromImage(form, GeneratedFunctions::temporal_copy);"), s);
            assertTrue(s.contains("return new Tgeompoint(MeosSqlRuntime.image(p, GeneratedFunctions.temporal_mem_size(p)));"), s);
            assertTrue(s.contains("carried as its memory image (temporal_copy / temporal_mem_size)"), s);
            assertTrue(s.contains("MeosSqlRuntime;"), "the class imports the run-time class: " + s);
            assertFalse(s.contains("temporal_from_wkb"), "the image is no WKB: " + s);
        }
    }

    @Test
    void aFixedSizeImageEncodesTheBytesItsStructureIsLaidIn() {
        for (String engine : new String[] {"flink", "spark"}) {
            String s = src("tstzspan", engine);
            assertTrue(s.contains("return MeosSqlRuntime.fromImage(form, GeneratedFunctions::span_copy);"), s);
            assertTrue(s.contains("return new Tstzspan(MeosSqlRuntime.image(p, 24));"), s);
            assertTrue(s.contains("carried as its memory image (span_copy / 24 bytes)"), s);
        }
    }

    @Test
    void anImageCarriedValueRendersTheFormItsCodecWrites() {
        for (String engine : new String[] {"flink", "spark"}) {
            String s = src("tgeompoint", engine);
            String render = s.substring(s.indexOf("public String toString()"));
            assertTrue(render.contains("Pointer p = decode();"), render);
            assertTrue(render.contains("byte[] b = GeneratedFunctions.temporal_as_wkb(p, (byte) 4);"), render);
            assertTrue(render.contains("return b == null ? null : MeosValue.hex(b);"), render);
            assertTrue(render.contains("MeosSqlRuntime.free(p);"), "the decoded value is freed: " + render);
        }
    }

    @Test
    void aValueWhoseTypeStatesNoImageKeepsItsCodec() {
        for (String engine : new String[] {"flink", "spark"}) {
            String s = src("box2d", engine);
            assertTrue(s.contains("return GeneratedFunctions.gbox_in(new String(form, StandardCharsets.UTF_8));"), s);
            assertTrue(s.contains("String s = GeneratedFunctions.gbox_out(p);"), s);
            assertTrue(s.contains("return new String(form, StandardCharsets.UTF_8);"), s);
            assertTrue(s.contains("carried as its text form (gbox_in / gbox_out)"), s);
            assertFalse(s.contains("MeosSqlRuntime"), "a codec-carried value needs no run-time class: " + s);
        }
    }

    @Test
    void anImageWhoseCopyTheJarDoesNotBindKeepsTheCodec() {
        for (String engine : new String[] {"flink", "spark"}) {
            String s = src("intset", engine);
            assertTrue(s.contains("return GeneratedFunctions.set_from_wkb(form);"), s);
            assertTrue(s.contains("byte[] b = GeneratedFunctions.set_as_wkb(p, (byte) 4);"), s);
            assertFalse(s.contains("set_copy_absent"), s);
        }
    }
}
