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
 * The readers of the types whose WKB states the type in its header, in
 * tools/codegen_spark_udfs.py: a type whose SQL type the catalog's membership predicate
 * wkb_header_states_type holds is read through UdfMarshal.typedFrom with its MeosType value,
 * so the overloads of one SQL name taking a tbox and an stbox parse apart and asText reaches
 * tbox_out and stbox_as_text; a type the predicate does not hold keeps the reader that checks
 * nothing, which no dispatcher may hold. The generator runs over a catalog of a few entries,
 * and the class runs it as #SparkTextWriterDispatchTest does, so no jar and no libmeos.
 */
class SparkTypedWkbReaderTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_spark_udfs as s",
        "def p(n, c): return {'name': n, 'cType': c, 'canonical': c}",
        "def fn(name, params, ret, sqlfn, extra=None):",
        "    f = {'name': name, 'file': 'meos.h', 'api': 'public', 'sqlfn': sqlfn,",
        "         'returnType': {'c': ret, 'canonical': ret}, 'params': params}",
        "    f.update(extra or {})",
        "    return f",
        "def enc(t, ctype):",
        "    return fn(t + '_as_wkb', [p('b', 'const ' + ctype + ' *'), p('variant', 'uint8_t'),",
        "              p('size_out', 'size_t *')], 'uint8_t *', 'asBinary',",
        "              {'sqlSignatures': [{'args': [t, 'text'], 'ret': 'bytea', 'sqlName': 'asBinary'}]})",
        "def codec(t):",
        "    return {'bytes': {'decoder': t + '_from_wkb', 'encoder': t + '_as_wkb'},",
        "            'decoders': {'hexwkb': t + '_from_hexwkb', 'text': t + '_in'}}",
        "s.TYPE_CODE.update({'tbox': 27, 'stbox': 25, 'raquet': 81})",
        "cat = {'functions': [enc('tbox', 'TBox'), enc('stbox', 'STBox'), enc('raquet', 'Raquet')],",
        "       'typeEncodings': {'TBox': codec('tbox'), 'STBox': codec('stbox'),",
        "                         'Raquet': codec('raquet')},",
        "       'typeRelations': {'predicates': {'wkb_header_states_type': ['stbox', 'tbox']}}}",
        "s.derive_codecs(cat, lambda n: bool(n))",
        "text = [fn('tbox_out', [p('box', 'const TBox *')], 'char *', 'asText'),",
        "        fn('stbox_as_text', [p('box', 'const STBox *')], 'char *', 'asText')]",
        "out = {'parse': {k: s.PARSE[k][0] for k in ('TBox', 'STBox', 'Raquet')},",
        "       'checked': {k: s.type_checked(s.PARSE[k][0]) for k in ('TBox', 'STBox', 'Raquet')},",
        "       'kept': [f['name'] for f in s._dispatchable(text)],",
        "       'asText': s.emit_dispatch('asText', s._dispatchable(text))}",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("typed-wkb-reader");
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
    void aTypeWhoseWkbStatesItIsReadForItsType() {
        assertEquals("UdfMarshal.typedFrom(%s, 27, GeneratedFunctions::tbox_from_wkb, "
            + "GeneratedFunctions::tbox_from_hexwkb)", results.get("parse").get("TBox").asText());
        assertEquals("UdfMarshal.typedFrom(%s, 25, GeneratedFunctions::stbox_from_wkb, "
            + "GeneratedFunctions::stbox_from_hexwkb)", results.get("parse").get("STBox").asText());
        assertTrue(results.get("checked").get("TBox").asBoolean());
        assertTrue(results.get("checked").get("STBox").asBoolean());
    }

    @Test
    void aTypeWhoseWkbDoesNotStateItKeepsTheReaderThatChecksNothing() {
        String raquet = results.get("parse").get("Raquet").asText();
        assertTrue(raquet.startsWith("UdfMarshal.read(%s, null, GeneratedFunctions::raquet_from_wkb"),
            raquet);
        assertFalse(results.get("checked").get("Raquet").asBoolean());
    }

    @Test
    void asTextReachesTheWriterOfEachBox() {
        assertEquals(2, results.get("kept").size(), results.get("kept").toString());
        String code = results.get("asText").asText();
        int t = code.indexOf("UdfMarshal.typedFrom(a0, 27,");
        int s = code.indexOf("UdfMarshal.typedFrom(a0, 25,");
        assertTrue(t >= 0 && code.indexOf("GeneratedFunctions.tbox_out(", t) > t, code);
        assertTrue(s >= 0 && code.indexOf("GeneratedFunctions.stbox_as_text(", s) > s, code);
    }
}
