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
 * The rows of a set-returning function whose columns are fields of the structures it returns, on
 * the Flink and Spark surfaces of tools/codegen_jvm.py (#_setret_column there): each column reads
 * its field at its offset in the element of its row, and an unsigned 32-bit count in an integer
 * column reads as a 32-bit integer, as valueCount(raster, ...) answers the count of each
 * ValueCount. A pointer field is a value of its own, read through the value class of its column,
 * NULL for a null pointer, and freed in each element before the array, as dumpAsPolygons(raster,
 * ...) answers the geom of each GeomVal and geomval_arr_free frees it. The generator runs over a
 * catalog of two functions, as JvmStructRowTest runs it, so no jar and no libmeos. A constant
 * string field reads as the text it holds, and a constant array, as the static table
 * geoPoseFrames() answers, is freed by nobody.
 */
class JvmStructFieldColumnTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** A function of a tint and a row count, answering an array of the structure elem whose
     * fields the columns cols read. */
    private static String rows(String name, String elem, String cols) {
        return "{\"name\": \"" + name + "\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"" + name + "\","
            + " \"returnType\": {\"c\": \"" + elem + " *\", \"canonical\": \"" + elem + " *\"},"
            + " \"params\": [{\"name\": \"v\", \"cType\": \"const Temporal *\", \"canonical\": \"const Temporal *\"},"
            + "  {\"name\": \"count\", \"cType\": \"int *\", \"canonical\": \"int *\"}],"
            + " \"shape\": {\"arrayReturn\": {\"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
            + "  \"element\": {\"c\": \"" + elem + "\", \"canonical\": \"" + elem + "\"}}, \"outParams\": [\"count\"]},"
            + " \"sqlSignatures\": [{\"args\": [\"tint\"], \"ret\": \"record\", \"retSet\": true,"
            + "  \"columns\": [" + cols + "]}]}";
    }

    /** counts(tint) RETURNS SETOF record (value float, count integer) over the fields of a Tally;
     * shapes(tint) RETURNS SETOF record (geom geometry, val double precision) over those of a
     * Shape; centroid(geometry) -> geometry makes geometry a value type of the surface. */
    private static final String CATALOG = "{\"functions\": ["
        + rows("counts", "Tally",
               "{\"name\": \"value\", \"type\": \"float\", \"from\": \"return\", \"field\": \"value\"},"
               + " {\"name\": \"count\", \"type\": \"integer\", \"from\": \"return\", \"field\": \"count\"}") + ", "
        + rows("shapes", "Shape",
               "{\"name\": \"geom\", \"type\": \"geometry\", \"from\": \"return\", \"field\": \"geom\"},"
               + " {\"name\": \"val\", \"type\": \"double precision\", \"from\": \"return\", \"field\": \"val\"}") + ", "
        + rows("labels", "const Label",
               "{\"name\": \"code\", \"type\": \"integer\", \"from\": \"return\", \"field\": \"code\"},"
               + " {\"name\": \"name\", \"type\": \"text\", \"from\": \"return\", \"field\": \"name\"}") + ", "
        + "{\"name\": \"centroid\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"centroid\","
        + " \"returnType\": {\"c\": \"GSERIALIZED *\", \"canonical\": \"GSERIALIZED *\"},"
        + " \"params\": [{\"name\": \"g\", \"cType\": \"const GSERIALIZED *\", \"canonical\": \"const GSERIALIZED *\"}],"
        + " \"sqlSignatures\": [{\"args\": [\"geometry\"], \"ret\": \"geometry\"}]}],"
        + " \"typeEncodings\": {"
        + "  \"Temporal\": {\"bytes\": {\"decoder\": \"temporal_from_wkb\", \"encoder\": \"temporal_as_wkb\"}},"
        + "  \"GSERIALIZED\": {\"bytes\": {\"decoder\": \"geo_from_wkb\", \"encoder\": \"geo_as_wkb\"}}},"
        + " \"structs\": [{\"name\": \"Tally\", \"fields\": ["
        + "  {\"name\": \"value\", \"cType\": \"double\", \"offset_bits\": 0},"
        + "  {\"name\": \"count\", \"cType\": \"uint32_t\", \"offset_bits\": 64},"
        + "  {\"name\": \"percent\", \"cType\": \"double\", \"offset_bits\": 128}]},"
        + "  {\"name\": \"Shape\", \"fields\": ["
        + "  {\"name\": \"geom\", \"cType\": \"GSERIALIZED *\", \"offset_bits\": 0},"
        + "  {\"name\": \"val\", \"cType\": \"double\", \"offset_bits\": 64}]},"
        + "  {\"name\": \"Label\", \"fields\": ["
        + "  {\"name\": \"code\", \"cType\": \"int32_t\", \"offset_bits\": 0},"
        + "  {\"name\": \"name\", \"cType\": \"const char *\", \"offset_bits\": 64}]}],"
        + " \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"counts\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"shapes\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"labels\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"centroid\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"temporal_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"temporal_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]},"
        + "\"geo_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"geo_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]}}";

    /** Prints {engine: {name: [{args, why, body}]}}, one entry per overload the engine builds. */
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
        "    rows = defaultdict(list)",
        "    for name, sargs, sdef, ov, why, isset, arity in g._sql_overloads(",
        "            m, cat, jar, m.layout, defaultdict(int), lambda f: True):",
        "        rows[name].append({'args': sargs, 'why': why, 'body': [] if ov is None else ov[3]})",
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

    /** The one overload of `name` the engine builds, which it does not refuse. */
    private static JsonNode built(String engine, String name) {
        JsonNode all = results.get(engine).get(name);
        assertNotNull(all, engine + " " + name + " " + results.get(engine));
        assertEquals(1, all.size(), engine + " " + all);
        JsonNode one = all.get(0);
        assertEquals("[\"tint\"]", one.get("args").toString(), engine);
        assertTrue(one.get("why").isNull(), engine + " " + one);
        return one;
    }

    private static final String[][] ENGINES = {
        {"flink", "org.apache.flink.types.Row.of(", "org.mobilitydb.flink.sql"},
        {"spark", "org.apache.spark.sql.RowFactory.create(", "org.mobilitydb.spark.sql"}};

    @Test
    void eachColumnReadsItsFieldAndAnUnsignedCountReadsAsAnInteger() {
        String reads = "_r.slice((long) _i * 24L + 0L).getDouble((long) 0 * 8L), "
            + "_r.slice((long) _i * 24L + 8L).getInt((long) 0 * 4L)";
        for (String[] e : ENGINES) {
            JsonNode counts = built(e[0], "counts");
            assertTrue(counts.get("body").toString().contains(e[1] + reads + ")"), e[0] + " " + counts);
        }
    }

    @Test
    void aPointerFieldIsAValueReadThroughItsColumnAndFreedInEachElement() {
        for (String[] e : ENGINES) {
            String body = built(e[0], "shapes").get("body").toString();
            String geom = "MeosSqlRuntime.orNull(_r.getPointer((long) _i * 16L + 0L), "
                + e[2] + ".types.Geometry::encode)";
            assertTrue(body.contains(e[1] + geom + ", _r.slice((long) _i * 16L + 8L).getDouble((long) 0 * 8L))"),
                e[0] + " " + body);
            String fields = "MeosSqlRuntime.freeField(_r, _c, 16L, 0L);";
            String array = "MeosSqlRuntime.free(_r);";
            assertTrue(body.contains(fields) && body.indexOf(fields) < body.indexOf(array), e[0] + " " + body);
        }
    }

    @Test
    void aConstantStringFieldIsItsTextAndAConstantArrayIsNotFreed() {
        for (String[] e : ENGINES) {
            String body = built(e[0], "labels").get("body").toString();
            String name = "MeosSqlRuntime.cstring(_r.getPointer((long) _i * 16L + 8L))";
            assertTrue(body.contains(e[1] + "_r.slice((long) _i * 16L + 0L).getInt((long) 0 * 4L), " + name + ")"),
                e[0] + " " + body);
            assertFalse(body.contains("MeosSqlRuntime.free(_r);"), e[0] + " " + body);
        }
    }
}
