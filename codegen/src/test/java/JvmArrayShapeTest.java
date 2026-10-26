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
 * A signature returning a SQL array whose layout its wrapper gives beyond the C array, the
 * catalog's arrayShape, on the Flink and Spark surfaces of tools/codegen_jvm.py
 * (#_array_shape_overload there): each element is read from the C array, NULL where the output
 * array the shape names as nulls is true, and with a rowLength the elements are cut into rows of
 * the length the function it names answers, an array of arrays, as dumpValues(raster, integer,
 * boolean) answers a height by width array of the values of a band, NULL for a nodata pixel.
 * The generator runs over a catalog of two functions, as JvmStructFieldColumnTest runs it, so no
 * jar and no libmeos.
 */
class JvmArrayShapeTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** values(v const Temporal *, band int, nodata bool **, count int *) -> double *, of the SQL
     * signature values(tint, integer) RETURNS float[] stating its null mask and its rows, and
     * width(v const Temporal *) -> int answering the length of a row. */
    private static final String CATALOG = "{\"functions\": ["
        + "{\"name\": \"values\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"values\","
        + " \"returnType\": {\"c\": \"double *\", \"canonical\": \"double *\"},"
        + " \"params\": [{\"name\": \"v\", \"cType\": \"const Temporal *\", \"canonical\": \"const Temporal *\"},"
        + "  {\"name\": \"band\", \"cType\": \"int\", \"canonical\": \"int\"},"
        + "  {\"name\": \"nodata\", \"cType\": \"bool **\", \"canonical\": \"bool **\"},"
        + "  {\"name\": \"count\", \"cType\": \"int *\", \"canonical\": \"int *\"}],"
        + " \"shape\": {\"arrayReturn\": {\"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
        + "  \"element\": {\"c\": \"double\", \"canonical\": \"double\"}},"
        + "  \"outputArrays\": [{\"param\": \"nodata\"}], \"outParams\": [\"nodata\", \"count\"]},"
        + " \"sqlSignatures\": [{\"args\": [\"tint\", \"integer\"], \"ret\": \"float[]\","
        + "  \"arrayShape\": {\"nulls\": \"nodata\", \"rowLength\": {\"call\": \"width\", \"args\": [\"v\"]}}}]},"
        + "{\"name\": \"width\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"width\","
        + " \"returnType\": {\"c\": \"int\", \"canonical\": \"int\"},"
        + " \"params\": [{\"name\": \"v\", \"cType\": \"const Temporal *\", \"canonical\": \"const Temporal *\"}],"
        + " \"sqlSignatures\": [{\"args\": [\"tint\"], \"ret\": \"integer\"}]}],"
        + " \"typeEncodings\": {"
        + "  \"Temporal\": {\"bytes\": {\"decoder\": \"temporal_from_wkb\", \"encoder\": \"temporal_as_wkb\"}}},"
        + " \"structs\": [], \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"values\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"int\", \"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"width\": {\"ret\": \"int\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"temporal_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"temporal_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]}}";

    /** Prints {engine: [{args, why, body, eval}]}, one entry per overload of values. */
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
        "            m, cat, jar, m.layout, defaultdict(int), lambda f: f['name'] == 'values'):",
        "        if name == 'values':",
        "            rows.append({'args': sargs, 'why': why, 'body': [] if ov is None else ov[3],",
        "                         'cls': None if ov is None else ov[2],",
        "                         'type': None if ov is None else ov[4]})",
        "    out[engine] = rows",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("array-shape");
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

    private static JsonNode built(String engine) {
        JsonNode all = results.get(engine);
        assertEquals(1, all.size(), engine + " " + all);
        JsonNode one = all.get(0);
        assertEquals("[\"tint\",\"integer\"]", one.get("args").toString(), engine);
        assertTrue(one.get("why").isNull(), engine + " " + one);
        return one;
    }

    @Test
    void theArrayIsAnArrayOfRowsOfTheLengthTheShapeNames() {
        assertEquals("DataTypes.ARRAY(DataTypes.ARRAY(DataTypes.DOUBLE()))",
            built("flink").get("type").asText());
        assertEquals("DataTypes.createArrayType(DataTypes.createArrayType(DataTypes.DoubleType))",
            built("spark").get("type").asText());
        for (String engine : new String[] {"flink", "spark"}) {
            JsonNode one = built(engine);
            assertEquals("Double[][]", one.get("cls").asText(), engine);
            String body = one.get("body").toString();
            assertTrue(body.contains("_r = GeneratedFunctions.values(_p0, a1, _n, _cnt);"), engine + " " + body);
            assertTrue(body.contains("int _w = GeneratedFunctions.width(_p0);"), engine + " " + body);
            assertTrue(body.contains("Double[][] _rows = new Double[_c / _w][];"), engine + " " + body);
        }
    }

    @Test
    void anElementTheMaskStatesIsNullAndTheMaskIsFreedWithTheArray() {
        for (String engine : new String[] {"flink", "spark"}) {
            String body = built(engine).get("body").toString();
            assertTrue(body.contains("_rows[_y][_x] = (_m.getByte((long) _i) != 0 ? null : "
                + "_r.getDouble((long) _i * 8L));"), engine + " " + body);
            assertTrue(body.contains("MeosSqlRuntime.free(_m);") && body.contains("MeosSqlRuntime.free(_r);"),
                engine + " " + body);
        }
    }
}
