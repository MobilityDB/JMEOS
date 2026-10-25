import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A SQL type standing for a structure of the catalog's structs that states no encoding and whose
 * fields are all scalars, on the Flink and Spark surfaces of tools/codegen_jvm.py
 * (#SqlModel._struct_rows there): a function answering a pointer to it answers the row of its
 * fields, read at their offsets as #_agg_result reads an aggregate's, and a function taking one
 * takes the row, laid out by MeosSqlRuntime.struct, as summaryStats answers PostGIS's
 * summarystats and summarystatsCount reads it. The generator runs over a catalog of two
 * structures, as JvmAggregateResultTest runs it, so no jar and no libmeos.
 */
class JvmStructRowTest {

    private static final Path TOOLS = Path.of("..", "tools").toAbsolutePath().normalize();

    /** A function of one parameter of C type par, answering ret, of the SQL signature sargs -> sret. */
    private static String fn(String name, String par, String ret, String sarg, String sret) {
        return "{\"name\": \"" + name + "\", \"file\": \"meos.h\", \"api\": \"public\", \"sqlfn\": \"" + name + "\","
            + " \"returnType\": {\"c\": \"" + ret + "\", \"canonical\": \"" + ret + "\"},"
            + " \"params\": [{\"name\": \"v\", \"cType\": \"" + par + "\", \"canonical\": \"" + par + "\"}],"
            + " \"sqlSignatures\": [{\"args\": [\"" + sarg + "\"], \"ret\": \"" + sret + "\"}]}";
    }

    private static final String CATALOG = "{\"functions\": ["
        + fn("summary", "const Temporal *", "BandStats *", "tint", "summarystats") + ", "
        + fn("stats_count", "const BandStats *", "int64_t", "summarystats", "bigint") + ", "
        + fn("odd_count", "const Odd *", "int64_t", "oddstats", "bigint") + "],"
        + " \"typeEncodings\": {"
        + "  \"Temporal\": {\"bytes\": {\"decoder\": \"temporal_from_wkb\", \"encoder\": \"temporal_as_wkb\"}}},"
        + " \"structs\": ["
        + "  {\"name\": \"BandStats\", \"fields\": ["
        + "   {\"name\": \"count\", \"cType\": \"uint32_t\", \"offset_bits\": 0},"
        + "   {\"name\": \"sum\", \"cType\": \"double\", \"offset_bits\": 64},"
        + "   {\"name\": \"max\", \"cType\": \"double\", \"offset_bits\": 128}]},"
        + "  {\"name\": \"Odd\", \"fields\": ["
        + "   {\"name\": \"count\", \"cType\": \"uint32_t\", \"offset_bits\": 0},"
        + "   {\"name\": \"name\", \"cType\": \"char *\", \"offset_bits\": 64}]}],"
        + " \"objectModel\": {\"classes\": {}}, \"enums\": [], \"macros\": []}";

    private static final String JAR = "{"
        + "\"summary\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"stats_count\": {\"ret\": \"long\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"odd_count\": {\"ret\": \"long\", \"arg_types\": [\"jnr.ffi.Pointer\"]},"
        + "\"temporal_from_wkb\": {\"ret\": \"jnr.ffi.Pointer\", \"arg_types\": [\"byte[]\"]},"
        + "\"temporal_as_wkb\": {\"ret\": \"byte[]\", \"arg_types\": [\"jnr.ffi.Pointer\", \"byte\"]}}";

    /** Prints {engine: {"rows": [SQL type], case: {params, temps, ret, body, type, eval} or reason}}. */
    private static final String DRIVER = String.join("\n",
        "import json, sys",
        "sys.path.insert(0, sys.argv[1])",
        "import codegen_jvm as g",
        "cat = json.load(open(sys.argv[2]))",
        "jar = json.load(open(sys.argv[3]))",
        "out = {}",
        "for engine, m in (('flink', g.SqlModel(cat, jar)),",
        "                  ('spark', g.SqlModel(cat, jar, g.SPARK_SQL_PKG, 'spark'))):",
        "    out[engine] = {'rows': sorted(m.struct_row)}",
        "    for case, (name, arg, ret) in {'result': ('summary', 'tint', 'summarystats'),",
        "                                   'argument': ('stats_count', 'summarystats', 'bigint'),",
        "                                   'odd': ('odd_count', 'oddstats', 'bigint')}.items():",
        "        ov, why = g._overload(m, m.by_name[name], [arg], ret, jar[name])",
        "        out[engine][case] = why if ov is None else {",
        "            'params': [c for c, _ in ov[0]], 'temps': [list(t) for t in ov[1]],",
        "            'ret': ov[2], 'body': ov[3], 'type': ov[4],",
        "            'argtypes': [m.datatype(c) for c, _ in ov[0]], 'eval': g._emit_eval(ov)}",
        "print(json.dumps(out))",
        "");

    private static JsonNode results;

    @BeforeAll
    static void generate() throws Exception {
        Path dir = Files.createTempDirectory("struct-row");
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

    private static JsonNode result(String engine, String c) {
        JsonNode r = results.get(engine).get(c);
        assertNotNull(r, engine + " " + c);
        return r;
    }

    private static final String ROW_TYPE = "DataTypes.ROW(DataTypes.FIELD(\"count\", DataTypes.BIGINT()), "
        + "DataTypes.FIELD(\"sum\", DataTypes.DOUBLE()), DataTypes.FIELD(\"max\", DataTypes.DOUBLE()))";

    @Test
    void aStructureOfScalarFieldsIsARowAndOneWithAPointerFieldIsNot() {
        for (String engine : new String[] {"flink", "spark"}) {
            assertEquals("[\"summarystats\"]", result(engine, "rows").toString());
            assertTrue(result(engine, "odd").isTextual(), result(engine, "odd").toString());
            assertEquals("sqltype:oddstats", result(engine, "odd").asText());
        }
    }

    @Test
    void aStructureAnsweredIsTheRowOfItsFieldsReadAtTheirOffsetsAndFreed() {
        String reads = "Integer.toUnsignedLong(p.getInt(0L)), p.getDouble(8L), p.getDouble(16L)";
        JsonNode f = result("flink", "result");
        assertEquals("org.apache.flink.types.Row#summarystats", f.get("ret").asText());
        assertEquals(ROW_TYPE, f.get("type").asText());
        String body = f.get("body").toString();
        assertTrue(body.contains("try { Pointer p = _r; return org.apache.flink.types.Row.of(" + reads + "); }"), body);
        assertTrue(body.contains("MeosSqlRuntime.freeResult(_r, _in)"), body);
        assertTrue(f.get("eval").get(0).asText().startsWith("    public org.apache.flink.types.Row eval("),
            f.get("eval").get(0).asText());
        JsonNode s = result("spark", "result");
        assertTrue(s.get("body").toString().contains("org.apache.spark.sql.RowFactory.create(" + reads + ")"),
            s.toString());
        assertTrue(s.get("type").asText().startsWith("DataTypes.createStructType("), s.toString());
    }

    @Test
    void aStructureTakenIsLaidOutFromTheRowAtTheOffsetsOfItsFields() {
        JsonNode f = result("flink", "argument");
        assertEquals("[\"org.apache.flink.types.Row#summarystats\"]", f.get("params").toString());
        assertEquals("[" + ROW_TYPE + "]", "[" + f.get("argtypes").get(0).asText() + "]");
        assertEquals("[[\"_p0\",\"MeosSqlRuntime.struct(24, new long[] {0L, 8L, 16L}, \\\"idd\\\", "
            + "new Object[] {a0.getField(0), a0.getField(1), a0.getField(2)})\",\"buffer\"]]",
            f.get("temps").toString());
        assertEquals("    public Long eval(org.apache.flink.types.Row a0) {", f.get("eval").get(0).asText());
        JsonNode s = result("spark", "argument");
        assertTrue(s.get("temps").toString().contains("new Object[] {a0.get(0), a0.get(1), a0.get(2)}"),
            s.toString());
        assertEquals("    public Long eval(org.apache.spark.sql.Row a0) {", s.get("eval").get(0).asText());
    }
}
