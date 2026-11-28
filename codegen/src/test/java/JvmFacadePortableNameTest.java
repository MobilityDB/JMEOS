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
 * The portable input and output names of the MeosOps* facades of tools/codegen_jvm.py
 * (#portable_io_names there): a method under each reader or writer name the catalog signatures
 * of a public function state, the altSqlName where one is stated; none for a type support
 * signature, a name that is no reader or writer, or an internal function. A type support
 * function (#is_type_support) has no C-named method: its portable methods call MEOS under the
 * MEOS_AVAILABLE guard. Any other function keeps its C-named method, which its portable methods
 * forward to. The generator runs over hand-built rows, as #JvmNullableArgumentTest runs it, so
 * no jar and no libmeos.
 */
class JvmFacadePortableNameTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_jvm as j",
        "def row(name, ret, params, cat):",
        "    return {'name': name, 'java_ret': ret, 'java_params': [(t, 'arg%d' % i) for i, t in enumerate(params)],",
        "            'oo_class': 'Set', 'role': None, 'header': 'meos.h', 'seq': False, 'cat': cat}",
        "def fn(name, sigs, api='public'):",
        "    return {'name': name, 'api': api, 'sqlfn': sigs[0]['sqlName'], 'sqlSignatures': sigs}",
        "out_ = fn('intset_out', [{'sqlName': 'intset_out', 'typeSupport': 'output'},",
        "                          {'sqlName': 'asText'}])",
        "wkb = fn('set_as_hexwkb', [{'sqlName': 'asHexWKB'}, {'sqlName': 'asHexWKB'}])",
        "alt = fn('tbox_as_text', [{'sqlName': 'asText', 'altSqlName': 'boxAsText'}])",
        "other = fn('set_num_values', [{'sqlName': 'numValues'}])",
        "inner = fn('set_out_internal', [{'sqlName': 'asText'}], api='internal')",
        "rows = {'text': row('intset_out', 'java.lang.String', ['jnr.ffi.Pointer', 'int'], out_),",
        "        'hex': row('set_as_hexwkb', 'java.lang.String', ['jnr.ffi.Pointer', 'byte'], wkb),",
        "        'alt': row('tbox_as_text', 'java.lang.String', ['jnr.ffi.Pointer', 'int'], alt),",
        "        'other': row('set_num_values', 'int', ['jnr.ffi.Pointer'], other),",
        "        'inner': row('set_out_internal', 'java.lang.String', ['jnr.ffi.Pointer'], inner)}",
        "out = {k: '\\n'.join(j.emit_method(r, 'mobilitykafka.meos.enabled')) for k, r in rows.items()}",
        // two writers of an array whose C pointers both erase to Pointer, and one taking other
        // Java parameters, in one class
        "geo = fn('geoarr_as_text', [{'sqlName': 'asText'}])",
        "tsp = fn('tspatialarr_as_text', [{'sqlName': 'asText'}])",
        "one = fn('geo_as_text', [{'sqlName': 'asText'}])",
        "free = [dict(row('geoarr_as_text', 'jnr.ffi.Pointer', ['jnr.ffi.Pointer', 'int', 'int'], geo), oo_class=None),",
        "        dict(row('tspatialarr_as_text', 'jnr.ffi.Pointer', ['jnr.ffi.Pointer', 'int', 'int'], tsp), oo_class=None),",
        "        dict(row('geo_as_text', 'java.lang.String', ['jnr.ffi.Pointer', 'int'], one), oo_class=None)]",
        "out['erased'] = j.emit_class('MeosOpsFreeGeo', free, 'org.mobilitydb.meos', 'mobilitykafka.meos.enabled', 'b')",
        // the ledger of the names the facades do not carry (#check_facade_gaps), over the class
        // just emitted: rewritten, checked as written, then with its one line removed, with an
        // entry the facades carry, and over a class one of whose names is lost otherwise
        "import io, os, pathlib, contextlib, tempfile",
        "d = pathlib.Path(tempfile.mkdtemp())",
        "(d / 'MeosOpsFreeGeo.java').write_text(out['erased'])",
        "class A: pass",
        "def run(rebaseline, classes=None):",
        "    a = A(); a.gaps = str(d / 'gaps.txt'); a.rebaseline = rebaseline",
        "    buf = io.StringIO()",
        "    with contextlib.redirect_stdout(buf):",
        "        try:",
        "            j.check_facade_gaps(a, classes or {'MeosOpsFreeGeo': free}, d); code = 0",
        "        except SystemExit as e:",
        "            code = e.code",
        "    return {'exit': code, 'out': buf.getvalue()}",
        "res = {'rebaseline': run(True)}",
        "res['ledger'] = (d / 'gaps.txt').read_text()",
        "res['listed'] = run(False)",
        "(d / 'gaps.txt').write_text(j.FACADE_GAPS_HEADER)",
        "res['unlisted'] = run(False)",
        "(d / 'gaps.txt').write_text(res['ledger'] + 'MeosOpsFreeGeo.asText(Pointer, int)\\tnot emitted: geo_as_text\\n')",
        "res['stale'] = run(False)",
        "(d / 'gaps.txt').write_text(res['ledger'])",
        "(d / 'MeosOpsFreeGeo.java').write_text(out['erased'].replace('{@code asText}, the portable name of MEOS {@code geo_as_text}.', ''))",
        "res['lost'] = run(False)",
        "out['ledger'] = json.dumps(res)",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("facade-portable-names");
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

    /** As #aTypeSupportSignatureTakesNoPortableName reads the row of `intset_out`. */
    @Test
    void aTypeSupportFunctionHasNoCNamedMethod() {
        String code = results.get("text").asText();
        assertFalse(code.contains(" intset_out("), code);
        assertTrue(code.contains("public static String asText(Pointer arg0, int arg1) {\n"
            + "        if (!MEOS_AVAILABLE) {"), code);
        assertTrue(code.contains("return GeneratedFunctions.intset_out(arg0, arg1);"), code);
    }

    /** As #aNameTwoSignaturesShareIsEmittedOnce reads the row of `set_as_hexwkb`. */
    @Test
    void anyOtherFunctionKeepsItsCNamedMethodItsPortableNameForwardsTo() {
        String code = results.get("hex").asText();
        assertTrue(code.contains("public static String set_as_hexwkb(Pointer arg0, byte arg1) {"),
            code);
        assertTrue(code.contains("public static String asHexWKB(Pointer arg0, byte arg1) {\n"
            + "        return set_as_hexwkb(arg0, arg1);"), code);
    }

    @Test
    void aTypeSupportSignatureTakesNoPortableName() {
        String code = results.get("text").asText();
        assertEquals(1, code.split("public static String asText\\(").length - 1, code);
        assertFalse(code.contains("public static String intset_out(Pointer arg0, int arg1) {\n"
            + "        return"), code);
    }

    @Test
    void aNameTwoSignaturesShareIsEmittedOnce() {
        String code = results.get("hex").asText();
        assertEquals(1, code.split("public static String asHexWKB\\(").length - 1, code);
    }

    @Test
    void theAltSqlNameWins() {
        String code = results.get("alt").asText();
        assertFalse(code.contains(" asText("), code);
        assertFalse(code.contains(" boxAsText("), code);
    }

    /** A pointer of every C type is a Pointer in Java, so a portable name two functions of one
     * class carry with the same Java parameters cannot say which it calls (#ambiguous_portable):
     * it is emitted for neither, and each keeps its C-named method. */
    @Test
    void aPortableNameTwoFunctionsWouldTakeWithOneJavaSignatureIsEmittedForNeither() {
        String code = results.get("erased").asText();
        assertFalse(code.contains(" asText(Pointer arg0, int arg1, int arg2)"), code);
        assertTrue(code.contains("public static Pointer geoarr_as_text(Pointer arg0, int arg1, int arg2) {"), code);
        assertTrue(code.contains("public static Pointer tspatialarr_as_text(Pointer arg0, int arg1, int arg2) {"), code);
        assertTrue(code.contains("public static String asText(Pointer arg0, int arg1) {\n"
            + "        return geo_as_text(arg0, arg1);"), code);
    }

    /** The runs of #check_facade_gaps over the class of the test above. */
    private static JsonNode ledger() throws Exception {
        return new ObjectMapper().readTree(results.get("ledger").asText());
    }

    /** The ledger states the name the facade does not carry, its Java signature and the two
     * functions it stands for, and a run over the class as emitted passes. */
    @Test
    void theLedgerRecordsEachPortableNameTheFacadeDoesNotCarry() throws Exception {
        JsonNode l = ledger();
        assertEquals(0, l.get("rebaseline").get("exit").asInt(), l.toString());
        String ledger = l.get("ledger").asText();
        assertTrue(ledger.startsWith("# "), ledger);
        assertEquals(java.util.List.of(
                "MeosOpsFreeGeo.asText(Pointer, int, int)\tjava-signature: geoarr_as_text, tspatialarr_as_text"),
            ledger.lines().filter(s -> !s.startsWith("#")).toList(), ledger);
        assertEquals(0, l.get("listed").get("exit").asInt(), l.toString());
        assertTrue(l.get("listed").get("out").asText().contains("gaps ledger: 1 portable names, none new"),
            l.toString());
    }

    /** A name the facade does not carry and the ledger does not list fails the run, whatever
     * lost it, and so does a ledger entry the facade carries. */
    @Test
    void anUnlistedDropAndAStaleEntryEachFailTheRun() throws Exception {
        JsonNode l = ledger();
        assertEquals(1, l.get("unlisted").get("exit").asInt(), l.toString());
        assertTrue(l.get("unlisted").get("out").asText().contains(
            "MeosOpsFreeGeo.asText(Pointer, int, int)\tjava-signature: geoarr_as_text, tspatialarr_as_text"),
            l.toString());
        assertEquals(1, l.get("stale").get("exit").asInt(), l.toString());
        assertTrue(l.get("stale").get("out").asText().contains("ERROR: 1 ledger portable names are carried or gone"),
            l.toString());
        assertEquals(1, l.get("lost").get("exit").asInt(), l.toString());
        assertTrue(l.get("lost").get("out").asText().contains(
            "MeosOpsFreeGeo.asText(Pointer, int)\tnot emitted: geo_as_text"), l.toString());
    }

    @Test
    void noPortableNameForANonIoNameOrAnInternalFunction() {
        assertFalse(results.get("other").asText().contains(" numValues("));
        assertFalse(results.get("inner").asText().contains(" asText("));
    }
}
