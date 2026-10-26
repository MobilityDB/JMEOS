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
 * An argument declared DEFAULT NULL that feeds a C value and a flag stating whether it is
 * given, on the Flink and Spark surfaces of tools/codegen_jvm.py (#SqlModel.signatures there):
 * the call stating the argument passes it to the value and the catalog's presentBinds to the
 * flag, as reclass(rast, nband, reclassexpr, pixeltype, nodataval) passes its nodata value and
 * has_nodata true, and the call leaving it out passes the catalog's nullDefaultBinds to both.
 * The generator runs over a catalog of one function, as JvmShorterCallOrderTest runs it, so no
 * jar and no libmeos.
 */
class JvmPresenceFlagTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** given(a int, has_v bool, v double) in C, read by the SQL signature
     * given(a integer, v float DEFAULT NULL), whose wrapper passes has_v = ! PG_ARGISNULL(1)
     * and v = has_v ? PG_GETARG_FLOAT8(1) : 0.0. */
    private static final String CATALOG = "{\"functions\": ["
        + "{\"name\": \"given\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"given\","
        + " \"returnType\": {\"c\": \"int64_t\", \"canonical\": \"int64_t\"},"
        + " \"params\": [{\"name\": \"a\", \"cType\": \"int\", \"canonical\": \"int\"},"
        + "  {\"name\": \"has_v\", \"cType\": \"bool\", \"canonical\": \"bool\"},"
        + "  {\"name\": \"v\", \"cType\": \"double\", \"canonical\": \"double\"}],"
        + " \"sqlSignatures\": [{\"args\": [\"integer\", \"float\"], \"ret\": \"bigint\","
        + "  \"argDefaults\": [null, \"NULL\"],"
        + "  \"nullDefaultBinds\": {\"1\": {\"has_v\": \"false\", \"v\": \"0.0\"}},"
        + "  \"presentBinds\": {\"1\": {\"has_v\": \"true\"}}}]}],"
        + " \"typeEncodings\": {}, \"structs\": [],"
        + " \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"given\": {\"ret\": \"long\", \"arg_types\": [\"int\", \"boolean\", \"double\"]}}";

    /** Prints {engine: [{args, call}]}, one entry per overload of given the engine registers. */
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
        "        rows.append({'args': sargs, 'why': why,",
        "                     'call': [] if ov is None else [l for l in ov[3] if 'given(' in l]})",
        "    out[engine] = rows",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("presence-flag");
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
    void theCallStatingTheArgumentPassesItsValueAndTheFlagTheCatalogStates() {
        for (String engine : new String[] {"flink", "spark"}) {
            JsonNode full = results.get(engine).get(0);
            assertEquals("[\"integer\",\"float\"]", full.get("args").toString(), engine);
            assertTrue(full.get("call").toString().contains("GeneratedFunctions.given(a0, true, a1)"),
                engine + " " + full);
        }
    }

    @Test
    void theCallLeavingTheArgumentOutPassesWhatTheWrapperPassesForTheValueAndTheFlag() {
        for (String engine : new String[] {"flink", "spark"}) {
            assertEquals(2, results.get(engine).size(), engine + " " + results.get(engine));
            JsonNode shorter = results.get(engine).get(1);
            assertEquals("[\"integer\"]", shorter.get("args").toString(), engine);
            assertTrue(shorter.get("call").toString().contains("GeneratedFunctions.given(a0, false, 0.0)"),
                engine + " " + shorter);
        }
    }
}
