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
 * A pointer argument the catalog states MEOS takes NULL for (`shape.nullable`), on the Flink
 * and Spark SQL surfaces of tools/codegen_jvm.py (#_emit_eval there) and the Spark UDFs of
 * tools/codegen_spark_udfs.py (#emit_single there): a NULL argument is passed to MEOS as NULL,
 * as MobilityDB's wrapper passes the value of jsonbSetLax, while any other NULL argument
 * answers NULL. The generators run over a catalog of two functions, as JvmPresenceFlagTest
 * runs them, so no jar and no libmeos.
 */
class JvmNullableArgumentTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** shift(a int, d const Interval *) states d nullable, shiftnn the same function does not;
     * the Spark UDFs read d as a temporal value, whose reader the catalog would state. */
    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_jvm as g",
        "import codegen_spark_udfs as s",
        "from collections import defaultdict",
        "def fn(name, ptr, nullable):",
        "    return {'name': name, 'file': 'meos.h', 'api': 'public', 'sqlfn': name,",
        "            'returnType': {'c': 'int64_t', 'canonical': 'int64_t'},",
        "            'params': [{'name': 'a', 'cType': 'int', 'canonical': 'int'},",
        "                       {'name': 'd', 'cType': ptr, 'canonical': ptr}],",
        "            'shape': {'nullable': ['d']} if nullable else {},",
        "            'sqlSignatures': [{'args': ['integer', 'interval'], 'ret': 'bigint'}]}",
        "cat = {'functions': [fn('shift', 'const Interval *', True),",
        "                     fn('shiftnn', 'const Interval *', False)],",
        "       'typeEncodings': {}, 'structs': [], 'objectModel': {'classes': {}},",
        "       'enums': [], 'macros': []}",
        "jar = {n: {'ret': 'long', 'arg_types': ['int', 'jnr.ffi.Pointer']}",
        "       for n in ('shift', 'shiftnn')}",
        "out = {}",
        "for engine, m in (('flink', g.SqlModel(cat, jar)),",
        "                  ('spark-sql', g.SqlModel(cat, jar, g.SPARK_SQL_PKG, 'spark'))):",
        "    out[engine] = {name: '\\n'.join(g._emit_eval(ov)) for name, sargs, sdef, ov, why,",
        "                   isset, arity in g._sql_overloads(m, cat, jar, m.layout,",
        "                   defaultdict(int), lambda f: True) if ov is not None}",
        "s.PARSE['Temporal'] = ('UdfMarshal.read(%s)', 'K_TEMPORAL')",
        "out['spark'] = {n: s.emit_single(n, fn(n, 'const Temporal *', nl))",
        "                for n, nl in (('shift', True), ('shiftnn', False))}",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("nullable-argument");
        try {
            Files.writeString(dir.resolve("driver.py"), DRIVER);
            Process p = new ProcessBuilder("python3", "-B", dir.resolve("driver.py").toString(),
                TOOLS.toString()).redirectErrorStream(true).start();
            String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(p.waitFor(120, TimeUnit.SECONDS), "the generators ran");
            assertEquals(0, p.exitValue(), out);
            results = new ObjectMapper().readTree(out);
        } finally {
            try (var stream = Files.walk(dir)) {
                stream.sorted(java.util.Comparator.reverseOrder()).forEach(q -> q.toFile().delete());
            }
        }
    }

    @Test
    void aNullableArgumentIsPassedAsNullOnTheSqlSurfaces() {
        for (String engine : new String[] {"flink", "spark-sql"}) {
            String code = results.get(engine).get("shift").asText();
            assertTrue(code.contains("if (a0 == null) return null;"), engine + "\n" + code);
            assertTrue(code.contains("Pointer _p0 = a1 == null ? null : MeosSqlRuntime.interval(a1);"),
                engine + "\n" + code);
        }
    }

    @Test
    void anArgumentOfNoNullableParameterAnswersNullOnTheSqlSurfaces() {
        for (String engine : new String[] {"flink", "spark-sql"}) {
            String code = results.get(engine).get("shiftnn").asText();
            assertTrue(code.contains("if (a0 == null || a1 == null) return null;"),
                engine + "\n" + code);
            assertTrue(code.contains("Pointer _p0 = MeosSqlRuntime.interval(a1);"),
                engine + "\n" + code);
        }
    }

    @Test
    void aNullableArgumentIsPassedAsNullByTheSparkUdf() {
        String code = results.get("spark").get("shift").asText();
        assertTrue(code.contains("if (a == null) return null;"), code);
        assertTrue(code.contains("jnr.ffi.Pointer p_d = d == null ? null : UdfMarshal.read(d);"), code);
        // a value given and unreadable still answers NULL
        assertTrue(code.contains("if (d != null && p_d == null) return null;"), code);
    }

    @Test
    void anArgumentOfNoNullableParameterAnswersNullInTheSparkUdf() {
        String code = results.get("spark").get("shiftnn").asText();
        assertTrue(code.contains("if (a == null || d == null) return null;"), code);
        assertTrue(code.contains("if (p_d == null) return null;"), code);
    }
}
