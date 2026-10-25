import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The rows of a set-returning function whose columns are fields of the structures it returns, on
 * the Flink and Spark surfaces of tools/codegen_jvm.py (#_setret_column there): each column reads
 * its field at its offset in the element of its row, and an unsigned 32-bit count in an integer
 * column reads as a 32-bit integer, as valueCount(raster, ...) answers the count of each
 * ValueCount. The generator runs over a catalog of one function, as JvmStructRowTest runs it, so
 * no jar and no libmeos.
 */
class JvmStructFieldColumnTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** counts(v const Temporal *, count int *) -> Tally *, of the SQL signature
     * counts(tint) RETURNS SETOF record (value float, count integer). */
    private static final String CATALOG = "{\"functions\": [{\"name\": \"counts\", \"file\": \"meos.h\","
        + " \"api\": \"public\", \"sqlfn\": \"counts\","
        + " \"returnType\": {\"c\": \"Tally *\", \"canonical\": \"Tally *\"},"
        + " \"params\": [{\"name\": \"v\", \"cType\": \"const Temporal *\", \"canonical\": \"const Temporal *\"},"
        + "  {\"name\": \"count\", \"cType\": \"int *\", \"canonical\": \"int *\"}],"
        + " \"shape\": {\"arrayReturn\": {\"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
        + "  \"element\": {\"c\": \"Tally\", \"canonical\": \"Tally\"}}, \"outParams\": [\"count\"]},"
        + " \"sqlSignatures\": [{\"args\": [\"tint\"], \"ret\": \"record\", \"retSet\": true,"
        + "  \"columns\": [{\"name\": \"value\", \"type\": \"float\", \"from\": \"return\", \"field\": \"value\"},"
        + "   {\"name\": \"count\", \"type\": \"integer\", \"from\": \"return\", \"field\": \"count\"}]}]}],"
        + " \"typeEncodings\": {"
        + "  \"Temporal\": {\"bytes\": {\"decoder\": \"temporal_from_wkb\", \"encoder\": \"temporal_as_wkb\"}}},"
        + " \"structs\": [{\"name\": \"Tally\", \"fields\": ["
        + "  {\"name\": \"value\", \"cType\": \"double\", \"offset_bits\": 0},"
        + "  {\"name\": \"count\", \"cType\": \"uint32_t\", \"offset_bits\": 64},"
        + "  {\"name\": \"percent\", \"cType\": \"double\", \"offset_bits\": 128}]}],"
        + " \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"counts\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"temporal_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"temporal_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]}}";

    /** Prints {engine: [{args, why, body}]}, one entry per overload of counts the engine builds. */
    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_jvm as g",
        "from collections import defaultdict",
        "cat = json.load(open(sys.argv[2]))",
        "jar = json.load(open(sys.argv[3]))",
        "out = {}",
        "for engine, m in (('flink', g.SqlModel(cat, jar)),",
        "                  ('spark', g.SqlModel(cat, jar, g.SPARK_SQL_PKG, 'spark'))):",
        "    rows = []",
        "    for name, sargs, sdef, ov, why, isset, arity in g._sql_overloads(",
        "            m, cat, jar, m.layout, defaultdict(int), lambda f: True):",
        "        rows.append({'args': sargs, 'why': why, 'body': [] if ov is None else ov[3]})",
        "    out[engine] = rows",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("struct-field-column");
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

    @Test
    void eachColumnReadsItsFieldAndAnUnsignedCountReadsAsAnInteger() {
        String reads = "_r.slice((long) _i * 24L + 0L).getDouble((long) 0 * 8L), "
            + "_r.slice((long) _i * 24L + 8L).getInt((long) 0 * 4L)";
        for (String[] e : new String[][] {{"flink", "org.apache.flink.types.Row.of("},
                                          {"spark", "org.apache.spark.sql.RowFactory.create("}}) {
            assertEquals(1, results.get(e[0]).size(), e[0] + " " + results.get(e[0]));
            JsonNode counts = results.get(e[0]).get(0);
            assertEquals("[\"tint\"]", counts.get("args").toString(), e[0]);
            assertTrue(counts.get("why").isNull(), e[0] + " " + counts);
            assertTrue(counts.get("body").toString().contains(e[1] + reads + ")"), e[0] + " " + counts);
        }
    }
}
