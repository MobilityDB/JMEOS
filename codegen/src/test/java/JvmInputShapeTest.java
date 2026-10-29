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
 * A signature taking two-dimensional arrays whose layout its wrapper reads, the catalog's
 * inputShape, on the Flink and Spark surfaces of tools/codegen_jvm.py (#_input_shape_overload
 * there): setValues(raster, integer, integer, integer, float[], boolean[], boolean) passes its
 * values to raster_set_values row by row with their NULL elements, its rows and columns and their
 * number, and its flags as a block of their own size, a NULL flag false and a NULL array of
 * flags none. An element type the surfaces do not lay out is refused.
 */
class JvmInputShapeTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    private static String param(String name, String c) {
        return "{\"name\": \"" + name + "\", \"cType\": \"" + c + "\", \"canonical\": \"" + c + "\"}";
    }

    /** raster_set_values with the setValues signature of the SQL types sql */
    private static String fn(String name, String values) {
        return "{\"name\": \"" + name + "\", \"file\": \"meos_raster.h\", \"api\": \"public\", \"sqlfn\": \"" + name + "\","
            + " \"returnType\": {\"c\": \"Raster *\", \"canonical\": \"Raster *\"},"
            + " \"params\": [" + String.join(", ", param("rast", "const Raster *"), param("band", "int"),
                param("x", "int"), param("y", "int"), param("width", "int"), param("height", "int"),
                param("values", "const double *"), param("nodata", "const bool *"), param("noset", "const bool *"),
                param("nosetwidth", "int"), param("nosetheight", "int"), param("count", "int"),
                param("keepnodata", "bool")) + "],"
            + " \"shape\": {\"nullable\": [\"nodata\", \"noset\"]},"
            + " \"sqlSignatures\": [{\"args\": [\"raster\", \"integer\", \"integer\", \"integer\", \"" + values + "\","
            + " \"boolean[]\", \"boolean\"], \"ret\": \"raster\","
            + " \"inputShape\": {\"4\": {\"values\": \"values\", \"nulls\": \"nodata\", \"rows\": \"height\","
            + " \"columns\": \"width\", \"count\": \"count\"}, \"5\": {\"values\": \"noset\", \"rows\": \"nosetheight\","
            + " \"columns\": \"nosetwidth\", \"nullElement\": \"false\"}}}]}";
    }

    private static final String CATALOG = "{\"functions\": ["
        + fn("setvalues", "float[]") + ", "
        // An element type the surfaces do not lay out
        + fn("settexts", "text[]")
        + "],"
        + " \"typeEncodings\": {"
        + "  \"Raster\": {\"bytes\": {\"decoder\": \"raster_from_wkb\", \"encoder\": \"raster_as_wkb\"}}},"
        + " \"structs\": [], \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String ARGS = "[\"jnr.ffi.Pointer\", \"int\", \"int\", \"int\", \"int\", \"int\","
        + " \"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\", \"jnr.ffi.Pointer\", \"int\", \"int\", \"int\", \"boolean\"]";

    private static final String JAR = "{"
        + "\"setvalues\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": " + ARGS + "},"
        + "\"settexts\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": " + ARGS + "},"
        + "\"raster_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"raster_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]}}";

    /** Prints {engine: {function: {params, temps, body} or reason}}, its one signature each. */
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
        "            out[engine].setdefault(name, why if ov is None else {",
        "                'params': ov[0], 'temps': ov[1], 'body': ov[3]})",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("input-shape");
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
    void theArraysAreTheirElementsTheirNullsAndTheirSize() {
        for (String engine : new String[] {"flink", "spark"}) {
            JsonNode one = results.get(engine).get("setvalues");
            assertTrue(one.isObject(), engine + " " + one);
            String params = one.get("params").toString();
            assertTrue(params.contains("[\"Double[][]\",\"g4\"]") && params.contains("[\"Boolean[][]\",\"g5\"]"),
                engine + " " + params);
            String temps = one.get("temps").toString();
            assertTrue(temps.contains("MeosSqlRuntime.gridDoubles(g4)"), engine + " " + temps);
            assertTrue(temps.contains("MeosSqlRuntime.gridNulls(g4)"), engine + " " + temps);
            assertTrue(temps.contains("g5 == null ? null : MeosSqlRuntime.gridBools(g5, false)"), engine + " " + temps);
            assertTrue(temps.contains("\"nullablebuffer:g5\""), engine + " " + temps);
            String body = one.get("body").toString();
            assertTrue(body.contains("GeneratedFunctions.setvalues(_p0, a1, a2, a3, MeosSqlRuntime.columns(g4), g4.length,"
                + " _g0, _g1, _g2, (g5 == null ? 0 : MeosSqlRuntime.columns(g5)), (g5 == null ? 0 : g5.length),"
                + " g4.length * MeosSqlRuntime.columns(g4), a4)"), engine + " " + body);
        }
    }

    @Test
    void anElementTypeTheSurfacesDoNotLayOutIsRefused() {
        for (String engine : new String[] {"flink", "spark"}) {
            assertEquals("inputShape:text[]", results.get(engine).get("settexts").asText(), engine);
        }
    }
}
