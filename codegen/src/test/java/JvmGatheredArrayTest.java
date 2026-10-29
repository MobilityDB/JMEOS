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
 * A composition gathering SQL arguments into the C array of one parameter, on the Flink and
 * Spark surfaces of tools/codegen_jvm.py (#_composition_inputs there): pcpoint(pcid, x, y)
 * passes x and y to pcpoint_make as its values and their number as its count, as
 * pcpoint(pcid, coordinates) passes the SQL array. An operand of a kind the generator does not
 * read is refused, the composition left unregistered.
 */
class JvmGatheredArrayTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** pcpoint_make(pcid, values, count), reading count values */
    private static final String MAKE = "{\"name\": \"make\", \"file\": \"meos.h\", \"api\": \"public\","
        + " \"returnType\": {\"c\": \"double\", \"canonical\": \"double\"},"
        + " \"params\": [{\"name\": \"pcid\", \"cType\": \"int\", \"canonical\": \"int\"},"
        + " {\"name\": \"values\", \"cType\": \"const double *\", \"canonical\": \"const double *\"},"
        + " {\"name\": \"count\", \"cType\": \"int\", \"canonical\": \"int\"}],"
        + " \"shape\": {\"inputArrays\": [{\"param\": \"values\", \"lengthFrom\": {\"kind\": \"param\", \"name\": \"count\"},"
        + " \"element\": {\"c\": \"double\", \"canonical\": \"double\"}}]}, \"sqlSignatures\": []}";

    private static String comp(String name, String args, String values) {
        return "{\"sqlName\": \"" + name + "\", \"args\": [" + args + "], \"ret\": \"float\", \"call\": \"make\","
            + " \"operands\": [{\"arg\": 0, \"param\": \"pcid\"}, " + values + ", {\"value\": \"2\", \"param\": \"count\"}]}";
    }

    private static final String CATALOG = "{\"functions\": [" + MAKE + "],"
        + " \"compositions\": ["
        + comp("gathered", "\"integer\", \"double precision\", \"double precision\"",
            "{\"gather\": [1, 2], \"param\": \"values\"}") + ", "
        // An operand naming no argument the generator reads
        + comp("unread", "\"integer\", \"double precision\", \"double precision\"",
            "{\"spread\": [1, 2], \"param\": \"values\"}") + ", "
        // The gathered arguments are of one type, the element of the C array
        + comp("mixed", "\"integer\", \"double precision\", \"integer\"",
            "{\"gather\": [1, 2], \"param\": \"values\"}")
        + "],"
        + " \"typeEncodings\": {}, \"structs\": [], \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"make\": {\"ret\": \"double\", \"arg_types\": [\"int\", \"jnr.ffi.Pointer\", \"int\"]}}";

    /** Prints {engine: {composition: {params, temps, body} or reason}}. */
    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_jvm as g",
        "cat = json.load(open(sys.argv[2]))",
        "jar = json.load(open(sys.argv[3]))",
        "out = {}",
        "for engine, m in (('flink', g.SqlModel(cat, jar)),",
        "                  ('spark', g.SqlModel(cat, jar, g.SPARK_SQL_PKG, 'spark'))):",
        "    out[engine] = {}",
        "    for comp in cat['compositions']:",
        "        ov, why = g._composition_overload(m, comp, jar, m.layout)",
        "        out[engine][comp['sqlName']] = why if ov is None else {",
        "            'params': ov[0], 'temps': ov[1], 'body': ov[3]}",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("gathered-array");
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
    void theGatheredArgumentsAreTheCArrayAndTheirNumberItsCount() {
        for (String engine : new String[] {"flink", "spark"}) {
            JsonNode one = results.get(engine).get("gathered");
            assertTrue(one.isObject(), engine + " " + one);
            // Every SQL argument is an argument of the eval
            assertEquals(3, one.get("params").size(), engine + " " + one);
            String temps = one.get("temps").toString();
            assertTrue(temps.contains("MeosSqlRuntime.doubles(new Double[] {a1, a2})"), engine + " " + temps);
            assertTrue(temps.contains("\"buffer\""), engine + " " + temps);
            String body = one.get("body").toString();
            assertTrue(body.contains("GeneratedFunctions.make(a0, _p0, 2)"), engine + " " + body);
        }
    }

    @Test
    void anOperandTheGeneratorDoesNotReadIsRefused() {
        for (String engine : new String[] {"flink", "spark"}) {
            assertEquals("composition:operand/values", results.get(engine).get("unread").asText(), engine);
            assertEquals("composition:operand/values", results.get(engine).get("mixed").asText(), engine);
        }
    }
}
