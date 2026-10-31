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
 * The text and binary writers and readers of the Spark UDFs of tools/codegen_spark_udfs.py:
 * asText dispatches a span, a span set and a set by the type its WKB states (#_argkinds there),
 * so an intspan reaches intspan_out, a tstzspan tstzspan_out and an intset intset_out, and a
 * set of no concrete overload spatialset_as_text, tried last; asHexWKB writes plain WKB,
 * variant 0, as MobilityDB's asHexWKB with its endian left to its default; a WKB reader takes
 * the bytes of a BinaryType column and asBinary answers them; the receive and the send of a
 * type, which CREATE TYPE binds, are told from the portable reader and writer and from an
 * aggregate's support function (#type_support there). The generator runs over the
 * functions alone, as JvmNullableArgumentTest runs it, so no jar and no libmeos.
 */
class SparkTextWriterDispatchTest {

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
        "for k in ('Span', 'SpanSet', 'Set', 'Temporal'):",
        "    s.PARSE[k] = s.TYPED_READER[k]",
        "s.SERIAL['Span'] = ('BinaryType', 'GeneratedFunctions.span_as_wkb(%s, (byte) 4)')",
        "s.TYPE_CODE.update({'intspan': 19, 'tstzspan': 39, 'intspanset': 20, 'intset': 18})",
        "text = [fn('intspan_out', [p('s', 'const Span *')], 'char *', 'asText'),",
        "        fn('tstzspan_out', [p('s', 'const Span *')], 'char *', 'asText'),",
        "        fn('intspanset_out', [p('ss', 'const SpanSet *')], 'char *', 'asText'),",
        "        fn('intset_out', [p('s', 'const Set *')], 'char *', 'asText'),",
        "        fn('spatialset_as_text', [p('s', 'const Set *')], 'char *', 'asText')]",
        "hexw = fn('span_as_hexwkb', [p('s', 'const Span *'), p('variant', 'uint8_t'),",
        "          p('size_out', 'size_t *')], 'char *', 'asHexWKB',",
        "          {'sqlArity': 1, 'shape': {'outParams': ['size_out']}})",
        "binw = fn('span_as_wkb', [p('s', 'const Span *'), p('variant', 'uint8_t'),",
        "          p('size_out', 'size_t *')], 'uint8_t *', 'asBinary',",
        "          {'sqlArity': 1, 'shape': {'outParams': ['size_out']}})",
        "binr = fn('span_from_wkb', [p('wkb', 'const uint8_t *'), p('size', 'size_t')],",
        "          'Span *', 'intspanFromBinary')",
        "keys = {f['name']: s._parsetuple(f) for f in text}",
        "out = {'asText': s.emit_dispatch('asText', text),",
        "       'distinct': len(set(keys.values())) == len(text),",
        "       'asHexWKB': s.emit_single('asHexWKB', hexw, vis_arity=1),",
        "       'asBinary': s.emit_single('asBinary', binw, vis_arity=1),",
        "       'intspanFromBinary': s.emit_single('intspanFromBinary', binr)}",
        "s.BYTES_ENCODERS.add('span_as_wkb')",
        "s.AGGREGATE_ROLES.add('spanUnionFinal')",
        "recv = fn('span_from_wkb', [], 'Span *', 'intspan_recv')",
        "agg = fn('span_union_finalfn', [], 'Span *', 'spanUnionFinal')",
        "out['support'] = {",
        "    'send': s.type_support(binw, {'args': ['intspan'], 'ret': 'bytea', 'sqlName': 'intspan_send'}),",
        "    'asBinary': s.type_support(binw, {'args': ['intspan', 'text'], 'ret': 'bytea', 'sqlName': 'asBinary'}),",
        "    'recv': s.type_support(recv, {'args': ['internal'], 'ret': 'intspan', 'sqlName': 'intspan_recv'}),",
        "    'fromBinary': s.type_support(recv, {'args': ['bytea'], 'ret': 'intspan', 'sqlName': 'intspanFromBinary'}),",
        "    'aggregate': s.type_support(agg, {'args': ['internal'], 'ret': 'intspan', 'sqlName': 'spanUnionFinal'})}",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("text-writer-dispatch");
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

    /** The position in `code` of the reader `reader` followed by the call of `writer`. */
    private static int route(String code, String reader, String writer) {
        int r = code.indexOf("Pointer P_a0 = " + reader + ";");
        assertTrue(r >= 0, reader + "\n" + code);
        int w = code.indexOf("GeneratedFunctions." + writer + "(P_a0", r);
        int next = code.indexOf("Pointer P_a0 =", r + 1);
        assertTrue(w >= 0 && (next < 0 || w < next), reader + " -> " + writer + "\n" + code);
        return r;
    }

    @Test
    void everySpanSpanSetAndSetTypeReachesItsOwnWriter() {
        assertTrue(results.get("distinct").asBoolean(), "the overloads parse apart");
        String code = results.get("asText").asText();
        route(code, "UdfMarshal.spanFromOf(a0, 19)", "intspan_out");
        route(code, "UdfMarshal.spanFromOf(a0, 39)", "tstzspan_out");
        route(code, "UdfMarshal.spansetFromOf(a0, 20)", "intspanset_out");
        route(code, "UdfMarshal.setFromOf(a0, 18)", "intset_out");
    }

    @Test
    void aSetOfNoConcreteOverloadIsWrittenBySpatialsetAsTextLast() {
        String code = results.get("asText").asText();
        int generic = route(code, "UdfMarshal.setFrom(a0)", "spatialset_as_text");
        assertTrue(generic > code.indexOf("UdfMarshal.setFromOf(a0, 18)"), code);
    }

    @Test
    void asHexWkbWritesPlainWkb() {
        String code = results.get("asHexWKB").asText();
        assertTrue(code.contains("GeneratedFunctions.span_as_hexwkb(p_s, (byte) 0)"), code);
    }

    @Test
    void asBinaryAnswersTheBytes() {
        String code = results.get("asBinary").asText();
        assertTrue(code.contains("(UDF1<Object, byte[]>)"), code);
        assertTrue(code.contains("return GeneratedFunctions.span_as_wkb(p_s, (byte) 0);"), code);
        assertTrue(code.contains("DataTypes.BinaryType"), code);
    }

    @Test
    void theReceiveAndTheSendOfATypeAreTypeSupport() {
        JsonNode support = results.get("support");
        assertTrue(support.get("send").asBoolean(), "intspan_send");
        assertTrue(support.get("recv").asBoolean(), "intspan_recv");
        assertFalse(support.get("asBinary").asBoolean(), "asBinary");
        assertFalse(support.get("fromBinary").asBoolean(), "intspanFromBinary");
        assertFalse(support.get("aggregate").asBoolean(), "an aggregate's final function");
    }

    @Test
    void aWkbReaderTakesTheBytes() {
        String code = results.get("intspanFromBinary").asText();
        assertTrue(code.contains("(UDF1<byte[], byte[]>) (wkb) ->"), code);
        assertTrue(code.contains("GeneratedFunctions.span_from_wkb(wkb)"), code);
    }
}
