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
 * The cells of tools/codegen_spark_udfs.py (#CELL there): a 64-bit integer whose C typedef the
 * catalog's typeEncodings carry by value, and whose SQL type the membership predicate
 * wkb_header_states_type holds, travels as its WKB. A cell argument reads through
 * UdfMarshal.cellOf, a cell result is written by the cell's WKB writer, and asText of a quadbin
 * and of an h3index parse apart where the two integers did not. A 64-bit integer naming no
 * cell stays a Long. The generator runs over a catalog of a few entries, and the class runs it
 * as #SparkTypedWkbReaderTest does, so no jar and no libmeos.
 */
class SparkCellWkbTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_spark_udfs as s",
        "def cell(t, td): return {'name': 'cell', 'cType': 'uint64_t', 'canonical': 'uint64_t', 'typedef': td}",
        "def p(n, c): return {'name': n, 'cType': c, 'canonical': c}",
        "def fn(name, params, ret, sqlfn, extra=None, rtd=None):",
        "    r = {'c': ret, 'canonical': ret}",
        "    if rtd: r['typedef'] = rtd",
        "    f = {'name': name, 'file': 'meos.h', 'api': 'public', 'sqlfn': sqlfn,",
        "         'returnType': r, 'params': params}",
        "    f.update(extra or {})",
        "    return f",
        "def enc(t, td):",
        "    return fn(t + '_as_wkb', [cell(t, td), p('variant', 'uint8_t'), p('size_out', 'size_t *')],",
        "              'uint8_t *', 'asBinary',",
        "              {'sqlSignatures': [{'args': [t, 'text'], 'ret': 'bytea', 'sqlName': 'asBinary'}]})",
        "def ins(t, td): return fn(t + '_in', [p('str', 'const char *')], 'uint64_t', t + 'FromText', None, td)",
        "def codec(t):",
        "    return {'in': t + '_in', 'bytes': {'decoder': t + '_from_wkb', 'encoder': t + '_as_wkb'},",
        "            'decoders': {'hexwkb': t + '_from_hexwkb', 'text': t + '_in'}}",
        "s.TYPE_CODE.update({'quadbin': 71, 'h3index': 68})",
        "cat = {'functions': [enc('quadbin', 'Quadbin'), enc('h3index', 'H3Index'),",
        "                     ins('quadbin', 'Quadbin'), ins('h3index', 'H3Index')],",
        "       'typeEncodings': {'Quadbin': codec('quadbin'), 'H3Index': codec('h3index')},",
        "       'typeRelations': {'predicates': {'wkb_header_states_type': ['h3index', 'quadbin']}}}",
        "s.derive_codecs(cat, lambda n: bool(n))",
        "text = [fn('quadbin_out', [cell('quadbin', 'Quadbin')], 'char *', 'asText'),",
        "        fn('h3index_out', [cell('h3index', 'H3Index')], 'char *', 'asText')]",
        "make = fn('bigint_to_quadbin', [p('i', 'int64_t')], 'uint64_t', 'quadbin', None, 'Quadbin')",
        "hashx = fn('quadbin_hash_extended', [cell('quadbin', 'Quadbin'), p('seed', 'uint64_t')],",
        "           'uint64_t', 'quadbinHashExtended')",
        "out = {'cells': sorted(s.CELL),",
        "       'arg': s.arg_kind('uint64_t', 'Quadbin')[3],",
        "       'seed': s.arg_kind('uint64_t')[1],",
        "       'distinct': len({s._parsetuple(f) for f in text}) == 2,",
        "       'asText': s.emit_dispatch('asText', s._dispatchable(text)),",
        "       'make': s.emit_single('bigint_to_quadbin', make),",
        "       'hash': s.emit_single('quadbin_hash_extended', hashx)}",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("cell-wkb");
        try {
            Files.writeString(dir.resolve("driver.py"), DRIVER);
            Process p = new ProcessBuilder("python3", "-B", dir.resolve("driver.py").toString(),
                TOOLS.toString()).redirectErrorStream(true).start();
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
    void aCellTypeIsReadFromTheCatalog() {
        assertEquals("[\"H3Index\",\"Quadbin\"]", results.get("cells").toString());
        assertEquals("UdfMarshal.cellOf(%s, 71, GeneratedFunctions::quadbin_from_wkb, "
            + "GeneratedFunctions::quadbin_from_hexwkb)", results.get("arg").asText());
    }

    @Test
    void asTextOfAQuadbinAndOfAnH3IndexParseApart() {
        assertTrue(results.get("distinct").asBoolean());
        String code = results.get("asText").asText();
        int q = code.indexOf("Long C_a0 = UdfMarshal.cellFrom(a0, 71,");
        int h = code.indexOf("Long C_a0 = UdfMarshal.cellFrom(a0, 68,");
        assertTrue(q >= 0 && code.indexOf("GeneratedFunctions.quadbin_out(C_a0", q) > q, code);
        assertTrue(h >= 0 && code.indexOf("GeneratedFunctions.h3index_out(C_a0", h) > h, code);
    }

    @Test
    void aCellResultIsItsWkb() {
        String code = results.get("make").asText();
        assertTrue(code.contains("(UDF1<Long, byte[]>)"), code);
        assertTrue(code.contains("return GeneratedFunctions.quadbin_as_wkb("
            + "GeneratedFunctions.bigint_to_quadbin(i), (byte) 4);"), code);
    }

    @Test
    void anIntegerNamingNoCellStaysALong() {
        assertEquals("LongType", results.get("seed").asText());
        String code = results.get("hash").asText();
        assertTrue(code.contains("(UDF2<Object, Long, Long>)"), code);
    }
}
