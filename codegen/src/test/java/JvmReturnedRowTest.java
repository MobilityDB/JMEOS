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
 * A signature returning one value of a composite type, its members the catalog's columns, on the
 * Flink and Spark surfaces of tools/codegen_jvm.py (#_row_overload there): the members are the
 * elements of the C array the function returns, as quaternion(pose) answers the W, X, Y and Z
 * pose_quaternion returns, or the values it writes through its out-parameters, an array of the
 * count it writes beside them when shape.outputArrays lists it, NULL when it answers false, as
 * asMVTGeom answers the geometry and the times tpoint_as_mvtgeom writes. The generator runs over
 * a catalog of four functions, as JvmArrayShapeTest runs it, so no jar and no libmeos.
 */
class JvmReturnedRowTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    private static final String V = "{\"name\": \"v\", \"cType\": \"const Temporal *\", \"canonical\": \"const Temporal *\"}";
    private static final String COUNT = "{\"name\": \"count\", \"cType\": \"int *\", \"canonical\": \"int *\"}";

    /** A function of the parameters params answering ret, of the SQL signature (tint) -> sret,
     * the columns its members. */
    private static String fn(String name, String ret, String params, String shape, String sret,
                             String columns) {
        return "{\"name\": \"" + name + "\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"" + name + "\","
            + " \"returnType\": {\"c\": \"" + ret + "\", \"canonical\": \"" + ret + "\"},"
            + " \"params\": [" + params + "], \"shape\": {" + shape + "},"
            + " \"sqlSignatures\": [{\"args\": [\"tint\"], \"ret\": \"" + sret + "\", \"columns\": [" + columns + "]}]}";
    }

    private static String col(String name, String type, String from, String more) {
        return "{\"name\": \"" + name + "\", \"type\": \"" + type + "\", \"from\": \"" + from + "\"" + more + "}";
    }

    private static final String ELEMENTS = "\"arrayReturn\": {\"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
        + " \"element\": {\"c\": \"double\", \"canonical\": \"double\"}}, \"outParams\": [\"count\"]";

    private static final String CATALOG = "{\"functions\": ["
        // pose_quaternion: the members are the elements of the returned array
        + fn("pair", "double *", V + ", " + COUNT, ELEMENTS, "pair",
            col("a", "float", "return", ", \"element\": 0") + ", " + col("b", "float", "return", ", \"element\": 1")) + ", "
        // tpoint_as_mvtgeom: a value and an array of the count, written when the function answers true
        + fn("found", "bool", V + ", {\"name\": \"value\", \"cType\": \"Temporal **\", \"canonical\": \"Temporal **\"},"
            + " {\"name\": \"times\", \"cType\": \"int64_t **\", \"canonical\": \"int64_t **\"}, " + COUNT,
            "\"outputArrays\": [{\"param\": \"times\"}], \"outParams\": [\"value\", \"times\", \"count\"]", "valtimes",
            col("val", "tint", "value", "") + ", " + col("ts", "bigint[]", "times", "")) + ", "
        // A function answering only whether it found the row cannot answer a member from its result
        + fn("flag", "bool", V + ", " + COUNT, ELEMENTS, "pair", col("a", "float", "return", ", \"element\": 0")) + ", "
        // A member from an out-parameter the function does not have
        + fn("absent", "double *", V + ", " + COUNT, ELEMENTS, "pair", col("a", "float", "nothing", ""))
        + "],"
        + " \"typeEncodings\": {"
        + "  \"Temporal\": {\"bytes\": {\"decoder\": \"temporal_from_wkb\", \"encoder\": \"temporal_as_wkb\"}}},"
        + " \"structs\": [], \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"pair\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"found\": {\"ret\": \"boolean\", \"arg_types\": [\"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"flag\": {\"ret\": \"boolean\", \"arg_types\": [\"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"absent\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\"]},"
        + "\"temporal_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"temporal_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]}}";

    /** Prints {engine: {function: {cls, type, body} or reason}}, its one signature each. */
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
        "    out[engine] = {}",
        "    for f in cat['functions']:",
        "        for name, sargs, sdef, ov, why, isset, arity in g._sql_overloads(",
        "                m, cat, jar, m.layout, defaultdict(int), lambda h: h is f):",
        "            out[engine][name] = why if ov is None else {",
        "                'cls': ov[2], 'type': ov[4], 'body': ov[3], 'set': isset}",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("returned-row");
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

    private static JsonNode built(String engine, String name) {
        JsonNode one = results.get(engine).get(name);
        assertNotNull(one, engine + " " + name + " " + results);
        assertTrue(one.isObject(), engine + " " + name + " " + one);
        assertFalse(one.get("set").asBoolean(), engine + " " + name);
        return one;
    }

    @Test
    void theElementsOfTheReturnedArrayAreTheMembersOfOneRow() {
        JsonNode f = built("flink", "pair");
        assertEquals("org.apache.flink.types.Row", f.get("cls").asText());
        assertEquals("DataTypes.ROW(DataTypes.FIELD(\"a\", DataTypes.DOUBLE()), "
            + "DataTypes.FIELD(\"b\", DataTypes.DOUBLE()))", f.get("type").asText());
        String body = f.get("body").toString();
        assertTrue(body.contains("_r = GeneratedFunctions.pair(_p0, _cnt);"), body);
        assertTrue(body.contains("if (_r == null) return null;"), body);
        assertTrue(body.contains("return org.apache.flink.types.Row.of(_r.getDouble((long) 0 * 8L), "
            + "_r.getDouble((long) 1 * 8L));"), body);
        assertTrue(body.contains("MeosSqlRuntime.free(_r);"), body);
        // No member reads the count, so none is read
        assertFalse(body.contains("_cnt.getInt"), body);
        JsonNode s = built("spark", "pair");
        assertEquals("org.apache.spark.sql.Row", s.get("cls").asText());
        assertTrue(s.get("type").asText().startsWith("DataTypes.createStructType("), s.toString());
        assertTrue(s.get("body").toString().contains("return org.apache.spark.sql.RowFactory.create("
            + "_r.getDouble((long) 0 * 8L), _r.getDouble((long) 1 * 8L));"), s.toString());
    }

    @Test
    void theOutParametersAreTheMembersOfOneRowAndAFalseAnswerIsNull() {
        for (String engine : new String[] {"flink", "spark"}) {
            String body = built(engine, "found").get("body").toString();
            assertTrue(body.contains("if (!GeneratedFunctions.found(_p0, _o0, _o1, _cnt)) return null;"),
                engine + " " + body);
            assertTrue(body.contains("int _c = _cnt.getInt(0L);"), engine + " " + body);
            assertTrue(body.contains("Long[] _v1 = new Long[_c];"), engine + " " + body);
            assertTrue(body.contains("for (int _i = 0; _i < _c; _i++) _v1[_i] = _a1.getLongLong((long) _i * 8L);"),
                engine + " " + body);
            assertTrue(body.contains("MeosSqlRuntime.orNull(_a0, "), engine + " " + body);
            assertTrue(body.contains("MeosSqlRuntime.free(_o0.getPointer(0L));")
                && body.contains("MeosSqlRuntime.free(_o1.getPointer(0L));"), engine + " " + body);
            assertFalse(body.contains("_r"), engine + " " + body);
        }
        assertTrue(built("flink", "found").get("type").asText()
            .endsWith("DataTypes.FIELD(\"ts\", DataTypes.ARRAY(DataTypes.BIGINT())))"),
            built("flink", "found").toString());
    }

    @Test
    void aMemberTheFunctionDoesNotAnswerIsRefused() {
        for (String engine : new String[] {"flink", "spark"}) {
            assertEquals("row:ret", results.get(engine).get("flag").asText(), engine);
            assertEquals("row:column/nothing:float", results.get(engine).get("absent").asText(), engine);
        }
    }
}
