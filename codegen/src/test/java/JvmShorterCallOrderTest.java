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
 * The call leaving an argument to its NULL default, on the Flink and Spark surfaces of
 * tools/codegen_jvm.py (#_sql_overloads there), when the catalog states the order of the SQL
 * arguments for the whole function, in shape.sqlArgParams, rather than for the signature: the
 * shorter call drops the argument it leaves out from that order, and passes its arguments to the
 * C parameters they stand for, as raster(pixels, width, ...) passes its pixels to the last C
 * parameter whether or not it states a nodata value. The generator runs over a catalog of one
 * function, as JvmStructRowTest runs it, so no jar and no libmeos.
 */
class JvmShorterCallOrderTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** gaps(a int, b int, c double) in C, read by the SQL signature
     * gaps(c float, a integer, b integer DEFAULT NULL), whose wrapper passes 0 for a NULL b. */
    private static final String CATALOG = "{\"functions\": ["
        + "{\"name\": \"gaps\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"gaps\","
        + " \"returnType\": {\"c\": \"int64_t\", \"canonical\": \"int64_t\"},"
        + " \"params\": [{\"name\": \"a\", \"cType\": \"int\", \"canonical\": \"int\"},"
        + "  {\"name\": \"b\", \"cType\": \"int\", \"canonical\": \"int\"},"
        + "  {\"name\": \"c\", \"cType\": \"double\", \"canonical\": \"double\"}],"
        + " \"shape\": {\"sqlArgParams\": [\"c\", \"a\", \"b\"]},"
        + " \"sqlSignatures\": [{\"args\": [\"float\", \"integer\", \"integer\"], \"ret\": \"bigint\","
        + "  \"argDefaults\": [null, null, \"NULL\"], \"nullDefaultBinds\": {\"2\": {\"b\": \"0\"}}}]}],"
        + " \"typeEncodings\": {}, \"structs\": [],"
        + " \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"gaps\": {\"ret\": \"long\", \"arg_types\": [\"int\", \"int\", \"double\"]}}";

    /** Prints {engine: [{args, call}]}, one entry per overload of gaps the engine registers. */
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
        "        if ov is not None:",
        "            rows.append({'args': sargs, 'call': [l for l in ov[3] if 'gaps(' in l]})",
        "    out[engine] = rows",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("shorter-call");
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
    void theCallStatingEveryArgumentPassesThemInTheirCOrder() {
        for (String engine : new String[] {"flink", "spark"}) {
            JsonNode full = results.get(engine).get(0);
            assertEquals("[\"float\",\"integer\",\"integer\"]", full.get("args").toString(), engine);
            assertTrue(full.get("call").toString().contains("GeneratedFunctions.gaps(a1, a2, a0)"),
                engine + " " + full);
        }
    }

    @Test
    void theCallLeavingAnArgumentOutPassesWhatTheWrapperPassesForIt() {
        for (String engine : new String[] {"flink", "spark"}) {
            assertEquals(2, results.get(engine).size(), engine + " " + results.get(engine));
            JsonNode shorter = results.get(engine).get(1);
            assertEquals("[\"float\",\"integer\"]", shorter.get("args").toString(), engine);
            assertTrue(shorter.get("call").toString().contains("GeneratedFunctions.gaps(a1, 0, a0)"),
                engine + " " + shorter);
        }
    }
}
