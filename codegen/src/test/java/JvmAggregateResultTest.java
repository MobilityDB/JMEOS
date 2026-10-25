import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The result of a Flink or Spark aggregate of tools/codegen_jvm.py (#_agg_result there), read
 * from the catalog: a value of a type the surface holds, a boolean where the final answers a C
 * bool, and a row of the fields of a structure of the catalog's structs where the final answers a
 * pointer to it, as the raster aggregates sameAlignmentAgg and summaryStatsAgg answer. The
 * generator runs over a catalog of one value type and two structures, as JvmValueFormTest runs
 * it, so no jar and no libmeos.
 */
class JvmAggregateResultTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** A final function over a state answering the C type ret. */
    private static String fin(String name, String ret) {
        return "{\"name\": \"" + name + "\", \"file\": \"meos.h\", \"api\": \"public\","
            + " \"returnType\": {\"c\": \"" + ret + "\", \"canonical\": \"" + ret + "\"},"
            + " \"params\": [{\"name\": \"state\", \"cType\": \"State *\","
            + " \"canonical\": \"State *\"}]}";
    }

    private static final String CATALOG = "{\"functions\": ["
        + "{\"name\": \"numInstants\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"numInstants\","
        + " \"returnType\": {\"c\": \"int\", \"canonical\": \"int\"},"
        + " \"params\": [{\"name\": \"v\", \"cType\": \"const Temporal *\", \"canonical\": \"const Temporal *\"}],"
        + " \"sqlSignatures\": [{\"args\": [\"tint\"], \"ret\": \"integer\"}]}, "
        + fin("same_finalfn", "bool") + ", "
        + fin("stats_finalfn", "BandStats *") + ", "
        + fin("odd_finalfn", "Odd *") + "],"
        + " \"typeEncodings\": {"
        + "  \"Temporal\": {\"bytes\": {\"decoder\": \"temporal_from_wkb\", \"encoder\": \"temporal_as_wkb\"}}},"
        + " \"structs\": ["
        + "  {\"name\": \"BandStats\", \"fields\": ["
        + "   {\"name\": \"count\", \"cType\": \"uint32_t\", \"offset_bits\": 0},"
        + "   {\"name\": \"sum\", \"cType\": \"double\", \"offset_bits\": 64},"
        + "   {\"name\": \"max\", \"cType\": \"double\", \"offset_bits\": 128}]},"
        + "  {\"name\": \"Odd\", \"fields\": ["
        + "   {\"name\": \"count\", \"cType\": \"uint32_t\", \"offset_bits\": 0},"
        + "   {\"name\": \"name\", \"cType\": \"char *\", \"offset_bits\": 64}]}],"
        + " \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"numInstants\": {\"ret\": \"int\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"temporal_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"temporal_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]}}";

    /** Prints {engine: {case: [class, make, type, encoder] or reason}}. */
    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_jvm as g",
        "cat = json.load(open(sys.argv[2]))",
        "jar = json.load(open(sys.argv[3]))",
        "out = {}",
        "for engine, m in (('flink', g.SqlModel(cat, jar)),",
        "                  ('spark', g.SqlModel(cat, jar, g.SPARK_SQL_PKG, 'spark'))):",
        "    cases = {'value': ('tint', None), 'boolean': ('boolean', 'same_finalfn'),",
        "             'row': ('summarystats', 'stats_finalfn'), 'odd': ('oddstats', 'odd_finalfn')}",
        "    out[engine] = {}",
        "    for case, (ret, final) in cases.items():",
        "        r, why = g._agg_result(m, ret, m.by_name.get(final))",
        "        out[engine][case] = list(r) if r else why",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("aggregate-result");
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
            results = new ObjectMapper().readTree(out);
        } finally {
            try (var stream = Files.walk(dir)) {
                stream.sorted(java.util.Comparator.reverseOrder()).forEach(q -> q.toFile().delete());
            }
        }
    }

    private static JsonNode result(String engine, String c) {
        JsonNode r = results.get(engine).get(c);
        assertNotNull(r, engine + " " + c);
        return r;
    }

    @Test
    void aValueResultIsEncodedAsAFunctionResultIs() {
        for (String engine : new String[] {"flink", "spark"}) {
            JsonNode r = result(engine, "value");
            assertTrue(r.isArray(), r.toString());
            assertTrue(r.get(0).asText().endsWith(".types.Tint"), r.toString());
            assertTrue(r.get(1).asText().endsWith(".types.Tint.encode((Pointer) r)"), r.toString());
        }
        assertEquals("Encoders.bean(org.mobilitydb.spark.sql.types.Tint.class)",
            result("spark", "value").get(3).asText());
    }

    @Test
    void aFinalAnsweringABoolGivesABoolean() {
        for (String engine : new String[] {"flink", "spark"}) {
            JsonNode r = result(engine, "boolean");
            assertEquals("Boolean", r.get(0).asText(), r.toString());
            assertEquals("r -> r", r.get(1).asText(), r.toString());
        }
        assertEquals("DataTypes.BOOLEAN()", result("flink", "boolean").get(2).asText());
        assertEquals("Encoders.BOOLEAN()", result("spark", "boolean").get(3).asText());
    }

    @Test
    void aFinalAnsweringAStructureGivesTheRowOfItsFieldsReadAtTheirOffsets() {
        String make = "Integer.toUnsignedLong(p.getInt(0L)), p.getDouble(8L), p.getDouble(16L)";
        JsonNode f = result("flink", "row");
        assertEquals("org.apache.flink.types.Row", f.get(0).asText());
        assertEquals("r -> { Pointer p = (Pointer) r; return org.apache.flink.types.Row.of(" + make + "); }",
            f.get(1).asText());
        assertEquals("DataTypes.ROW(DataTypes.FIELD(\"count\", DataTypes.BIGINT()), "
            + "DataTypes.FIELD(\"sum\", DataTypes.DOUBLE()), DataTypes.FIELD(\"max\", DataTypes.DOUBLE()))",
            f.get(2).asText());
        JsonNode s = result("spark", "row");
        assertEquals("org.apache.spark.sql.Row", s.get(0).asText());
        assertTrue(s.get(1).asText().contains("org.apache.spark.sql.RowFactory.create(" + make + ")"), s.toString());
        assertTrue(s.get(3).asText().startsWith("Encoders.row(DataTypes.createStructType("), s.toString());
    }

    @Test
    void aStructureWithAFieldOfNoScalarTypeIsNoResult() {
        for (String engine : new String[] {"flink", "spark"}) {
            assertEquals("result oddstats of no value type", result(engine, "odd").asText());
        }
    }
}
