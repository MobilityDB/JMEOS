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
 * An array of C strings a function answers, on the Flink and Spark surfaces of
 * tools/codegen_jvm.py (#_setret_column there): a writer of an array answers one string per
 * value of the array it reads, as asText(tint[], integer) answers text[] through temparr_out and
 * asText(geometry[], integer) through geoarr_as_text, the length of both the count the catalog's
 * lengthFrom names for the input array and the returned one. Each element reads as the text it
 * holds and is freed before the array, as MobilityDuck frees them; a constant array of constant
 * strings (const char **) is MEOS's own and freed by nobody. The generator runs over a catalog of
 * the functions, as JvmStructFieldColumnTest runs it, so no jar and no libmeos.
 */
class JvmStringArrayTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** A writer of an array of the C type elem, the SQL arguments sql: char **name(const elem **arr,
     * int count, int maxdd), answering one string per element. */
    private static String writer(String name, String elem, String sql) {
        return "{\"name\": \"" + name + "\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"asText\","
            + " \"returnType\": {\"c\": \"char **\", \"canonical\": \"char **\"},"
            + " \"params\": [{\"name\": \"arr\", \"cType\": \"const " + elem + " **\", \"canonical\": \"const " + elem + " **\"},"
            + "  {\"name\": \"count\", \"cType\": \"int\", \"canonical\": \"int\"},"
            + "  {\"name\": \"maxdd\", \"cType\": \"int\", \"canonical\": \"int\"}],"
            + " \"shape\": {\"inputArrays\": [{\"param\": \"arr\", \"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
            + "  \"element\": {\"c\": \"" + elem + " *\", \"canonical\": \"" + elem + " *\"}}],"
            + "  \"arrayReturn\": {\"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
            + "  \"element\": {\"c\": \"char *\", \"canonical\": \"char *\"}}},"
            + " \"sqlSignatures\": [{\"args\": [\"" + sql + "[]\", \"integer\"], \"ret\": \"text[]\"}]}";
    }

    /** asText(tint[], integer) and asText(geometry[], integer) -> text[]; names() -> text[], a
     * constant array of constant strings as long as its count; centroid(geometry) -> geometry and
     * tint itself make geometry and tint value types of the surface. */
    private static final String CATALOG = "{\"functions\": ["
        + writer("tints_text", "Temporal", "tint") + ", "
        + writer("geoms_text", "GSERIALIZED", "geometry") + ", "
        + "{\"name\": \"names\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"names\","
        + " \"returnType\": {\"c\": \"const char **\", \"canonical\": \"const char **\"},"
        + " \"params\": [{\"name\": \"count\", \"cType\": \"int *\", \"canonical\": \"int *\"}],"
        + " \"shape\": {\"arrayReturn\": {\"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
        + "  \"element\": {\"c\": \"const char *\", \"canonical\": \"const char *\"}}, \"outParams\": [\"count\"]},"
        + " \"sqlSignatures\": [{\"args\": [], \"ret\": \"text[]\"}]}, "
        + "{\"name\": \"centroid\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"centroid\","
        + " \"returnType\": {\"c\": \"GSERIALIZED *\", \"canonical\": \"GSERIALIZED *\"},"
        + " \"params\": [{\"name\": \"g\", \"cType\": \"const GSERIALIZED *\", \"canonical\": \"const GSERIALIZED *\"}],"
        + " \"sqlSignatures\": [{\"args\": [\"geometry\"], \"ret\": \"geometry\"}]}],"
        // the sqlTypes the catalog states (MEOS-API parser/sqltypes.py)
        + " \"sqlTypes\": {\"geometry\": {\"byValue\": false, \"cType\": \"GSERIALIZED\", \"codec\": {\"decoder\": \"geo_from_wkb\", \"decoderAux\": [], \"encoder\": \"geo_as_wkb\", \"encoderAux\": [], \"kind\": \"bytes\"}}, \"tint\": {\"byValue\": false, \"cType\": \"Temporal\", \"codec\": {\"decoder\": \"temporal_from_wkb\", \"decoderAux\": [], \"encoder\": \"temporal_as_wkb\", \"encoderAux\": [], \"kind\": \"bytes\"}}},"
        + " \"typeEncodings\": {"
        + "  \"Temporal\": {\"bytes\": {\"decoder\": \"temporal_from_wkb\", \"encoder\": \"temporal_as_wkb\"}},"
        + "  \"GSERIALIZED\": {\"bytes\": {\"decoder\": \"geo_from_wkb\", \"encoder\": \"geo_as_wkb\"}}},"
        + " \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"tints_text\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"int\", \"int\"]},"
        + "\"geoms_text\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"int\", \"int\"]},"
        + "\"names\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"centroid\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"temporal_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"temporal_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]},"
        + "\"geo_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"geo_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]}}";

    /** Prints {engine: {name: [{args, why, params, ret, type, body}]}}, one entry per overload the
     * engine builds, the C function it calls under `fn`. */
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
        "        rows[name].append({'args': sargs, 'why': why,",
        "                           'params': [] if ov is None else [c for c, _ in ov[0]],",
        "                           'ret': None if ov is None else ov[2],",
        "                           'type': None if ov is None else ov[4],",
        "                           'body': [] if ov is None else ov[3]})",
        "    out[engine] = rows",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("string-array");
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

    /** The one overload of `name` over the SQL arguments `args` the engine builds, which it does
     * not refuse. */
    private static JsonNode built(String engine, String name, String args) {
        JsonNode all = results.get(engine).get(name);
        assertNotNull(all, engine + " " + name + " " + results.get(engine));
        for (JsonNode one : all) {
            if (one.get("args").toString().equals(args)) {
                assertTrue(one.get("why").isNull(), engine + " " + one);
                return one;
            }
        }
        return fail(engine + " " + name + args + " " + all);
    }

    /** {engine, its package, the engine's type of an array of strings} */
    private static final String[][] ENGINES = {
        {"flink", "org.mobilitydb.flink.sql", "DataTypes.ARRAY(DataTypes.STRING())"},
        {"spark", "org.mobilitydb.spark.sql", "DataTypes.createArrayType(DataTypes.StringType)"}};

    private static final String READ = "MeosSqlRuntime.cstring(MeosSqlRuntime.at(_r, _i))";

    @Test
    void aWriterOfAnArrayAnswersAnArrayOfStringsAsLongAsTheArrayItReads() {
        for (String[] e : ENGINES) {
            // the value class of each SQL type, named as #_codecs names one the fixture's empty
            // object model states no class for
            for (String[] w : new String[][] {{"tints_text", "Tint"}, {"geoms_text", "Geometry"}}) {
                String sql = w[0].equals("tints_text") ? "tint" : "geometry";
                JsonNode one = built(e[0], "asText", "[\"" + sql + "[]\",\"integer\"]");
                String body = one.get("body").toString();
                assertEquals("[\"" + e[1] + ".types." + w[1] + "[]\",\"Integer\"]",
                    one.get("params").toString(), e[0] + " " + one);
                assertEquals("String[]", one.get("ret").asText(), e[0] + " " + one);
                assertEquals(e[2], one.get("type").asText(), e[0] + " " + one);
                assertTrue(body.contains("GeneratedFunctions." + w[0] + "(_p0, a0.length, a1)"), e[0] + " " + body);
                assertTrue(body.contains("_c = a0.length;"), e[0] + " " + body);
                assertTrue(body.contains("_rows[_i] = " + READ + ";"), e[0] + " " + body);
            }
        }
    }

    @Test
    void eachStringIsFreedBeforeTheArray() {
        for (String[] e : ENGINES) {
            String body = built(e[0], "asText", "[\"tint[]\",\"integer\"]").get("body").toString();
            String each = "MeosSqlRuntime.freeEach(_r, _c);";
            String array = "MeosSqlRuntime.free(_r);";
            assertTrue(body.contains(each) && body.indexOf(each) < body.indexOf(array), e[0] + " " + body);
        }
    }

    @Test
    void aConstantArrayOfConstantStringsIsFreedByNobody() {
        for (String[] e : ENGINES) {
            String body = built(e[0], "names", "[]").get("body").toString();
            assertTrue(body.contains("_rows[_i] = " + READ + ";"), e[0] + " " + body);
            assertFalse(body.contains("freeEach"), e[0] + " " + body);
            assertFalse(body.contains("MeosSqlRuntime.free(_r);"), e[0] + " " + body);
        }
    }
}
