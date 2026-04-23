package examples;

import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import types.enums.RTreeSearchOp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static functions.functions.*;

/**
 * RTree vs Brute Force Scalability Benchmark
 *
 * <p>Compares four spatial-query strategies as the number of indexed regions N
 * grows from 19 (real Brussels communes) to 10 000 (synthetic tiled boxes):
 *
 * <pre>
 *  Strategy       │ First pass                         │ Second pass
 *  ───────────────┼────────────────────────────────────┼─────────────────────────────
 *  BruteSTBox     │ overlaps_stbox_stbox on all N      │ —
 *  BruteExact     │ overlaps_stbox_stbox on all N      │ geom_intersects2d on hits
 *  RTreeSTBox     │ rtree_search (≈ log N candidates)  │ —
 *  RTreeExact     │ rtree_search (≈ log N candidates)  │ geom_intersects2d on hits
 * </pre>
 *
 * <p><strong>Tiling strategy</strong>: only 19 real commune bounding boxes exist, so for
 * N &gt; 19 we tile them in a grid: each new row is shifted 50 km northward on the
 * Y axis (SRID 3857, metres).  For exact checks, each synthetic box is paired
 * with the real commune polygon at index {@code j % 19}.  Because the polygon
 * stays in Brussels while the box is shifted north, {@code geom_intersects2d}
 * benchmarks the cost of the call without inflating the true match count.
 *
 * <pre>
 *   Row 2 (+100 km)  A B C … S
 *   Row 1  (+50 km)  A B C … S
 *   Row 0   (real)   A B C … S  ← Brussels
 * </pre>
 *
 * <p><strong>Input files</strong> (SRID 3857, Web Mercator):
 * <ul>
 *   <li>{@code data/brussels_communes.csv}: id, name, population, geom (EWKB hex)</li>
 *   <li>{@code data/berlinmod_instants.csv}: tripid, vehid, day, seqno, geom (WKT), t</li>
 * </ul>
 */
public class N21_BerlinMOD_RTree_Scalability_Benchmark {

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private static final String COMMUNES_FILE = "src/main/java/examples/data/brussels_communes.csv";
    private static final String VEHICLES_FILE  = "src/main/java/examples/data/berlinmod_instants.csv";

    /** Scales to benchmark: number of indexed regions for each run. */
    private static final int[] SCALES = {19, 50, 75, 100, 500, 1000, 2000, 10_000};

    /** Number of times each scale is benchmarked. Results are averaged. */
    private static final int BENCHMARK_RUNS = 3;

    /** Y-shift per tiling row in metres. */
    private static final double OFFSET_M = 50_000.0;

    // -------------------------------------------------------------------------
    // Data holders
    // -------------------------------------------------------------------------

    /**
     * Everything we need per Brussels commune.
     *
     * <p>{@code geom}: the full polygon Pointer, used by {@code geom_intersects2d}
     * for exact containment checks (second pass).
     * <br>{@code bbox}: the bounding STBox Pointer, used as the index entry and
     * as the first-pass filter.
     * <br>{@code xmin/ymin/xmax/ymax}: stored as Java doubles so we can build
     * shifted copies cheaply without re-calling MEOS.
     */
    private static class CommuneData {
        final int     id;
        final String  name;
        final int     population;
        final Pointer geom;
        final Pointer bbox;
        final double  xmin, ymin, xmax, ymax;

        CommuneData(int id, String name, int population,
                    Pointer geom, Pointer bbox,
                    double xmin, double ymin, double xmax, double ymax) {
            this.id         = id;
            this.name       = name;
            this.population = population;
            this.geom       = geom;
            this.bbox       = bbox;
            this.xmin = xmin; this.ymin = ymin;
            this.xmax = xmax; this.ymax = ymax;
        }
    }

    /**
     * Everything we need per vehicle position instant.
     *
     * <p>{@code bbox}: a degenerate point STBox (both corners identical),
     * used as the query key in {@code overlaps_stbox_stbox} / {@code rtree_search}.
     * <br>{@code geom}: the POINT geometry, used in {@code geom_intersects2d}.
     */
    private static class VehiclePos {
        final int     tripId, vehId;
        final Pointer bbox;
        final Pointer geom;

        VehiclePos(int tripId, int vehId, Pointer bbox, Pointer geom) {
            this.tripId = tripId;
            this.vehId  = vehId;
            this.bbox   = bbox;
            this.geom   = geom;
        }
    }

    // -------------------------------------------------------------------------
    // Helper: extract double from a stbox_x/ymin/max result Pointer
    // -------------------------------------------------------------------------

    /**
     * <ul>
     *   <li>The C function writes a {@code double} into {@code *result} and returns bool.</li>
     *   <li>The generated wrapper allocates the result Pointer internally,
     *       passes it to the native call, and returns it (or {@code null} on failure).</li>
     * </ul>
     * So we dereference the Pointer as a double at byte offset 0.
     */
    private static double ptrToDouble(Pointer p) {
        return (p != null) ? p.getDouble(0) : 0.0;
    }

    // -------------------------------------------------------------------------
    // Data loading
    // -------------------------------------------------------------------------

    /**
     * Reads {@code brussels_communes.csv} and returns one {@link CommuneData} per row.
     *
     * <p>CSV columns: {@code id, name, population, geom}
     * where {@code geom} is an EWKB hex string (SRID 3857).
     *
     * <p>We use {@code geom_from_hexewkb} + {@code geo_to_stbox} + the four
     */
    private static List<CommuneData> loadCommunes() throws IOException {
        List<CommuneData> list = new ArrayList<>();
        int skipped = 0;

        System.out.println("avant le try");
        try (BufferedReader br = new BufferedReader(new FileReader(COMMUNES_FILE))) {
            System.out.println("dans le try");
            br.readLine(); // skip header
            System.out.println("après readLine");

            String line;
            System.out.println("avant le while");
            while ((line = br.readLine()) != null) {
                // Limit to 4 tokens: the name may contain commas (bilingual names)
                String[] f = line.split(",", 4);
                if (f.length < 4) { skipped++; continue; }
                System.out.println("tableau f à parser : " + f);

                try {
                    int    id   = Integer.parseInt(f[0].trim());
                    String name = f[1].trim();
                    int    pop  = Integer.parseInt(f[2].trim());
                    String hex  = f[3].trim();
                    System.out.println("4 valeurs: id=" + id + " | name=" + name + " | pop=" + pop + " | hex=" + hex);

                    // Parse the EWKB hex string --> polygon Pointer
                    Pointer geom = geom_from_hexewkb(hex);
                    System.out.println("geom: " + geom);
                    if (geom == null) { skipped++; continue; }

                    // Compute the bounding STBox from the polygon
                    Pointer bbox = geo_to_stbox(geom);
                    if (bbox == null) { skipped++; continue; }

                    System.out.println("avant les ptrToDouble");
                    // Read bbox corners via direct double accessors
                    double xmin = ptrToDouble(stbox_xmin(bbox));
                    double ymin = ptrToDouble(stbox_ymin(bbox));
                    double xmax = ptrToDouble(stbox_xmax(bbox));
                    double ymax = ptrToDouble(stbox_ymax(bbox));
                    System.out.println("après les ptrToDouble");

                    list.add(new CommuneData(id, name, pop, geom, bbox,
                            xmin, ymin, xmax, ymax));
                } catch (Exception e) {
                    skipped++;
                }
                System.out.println("après le while");
            }
        }
        System.out.println("après le try");

        System.out.printf("  Communes loaded : %d  (skipped: %d)%n", list.size(), skipped);
        list.forEach(c -> System.out.printf(
                "    [%2d] %-48s pop=%,7d  bbox=(%.0f,%.0f)-(%.0f,%.0f)%n",
                c.id, c.name, c.population, c.xmin, c.ymin, c.xmax, c.ymax));
        return list;
    }

    /**
     * Reads {@code berlinmod_instants.csv} and returns one {@link VehiclePos} per row.
     *
     * <p>CSV columns: {@code tripid, vehid, day, seqno, geom, t}
     * where {@code geom} is {@code SRID=3857;POINT(x y)}.
     *
     * <p>We build two MEOS objects per position:
     * <ul>
     *   <li>A point STBox (both corners identical) via {@code stbox_in}: used as
     *       the RTree query and in {@code overlaps_stbox_stbox}.</li>
     *   <li>A POINT geometry via {@code geo_from_text}: used in
     *       {@code geom_intersects2d} for exact polygon containment.</li>
     * </ul>
     */
    private static List<VehiclePos> loadVehiclePositions() throws IOException {
        List<VehiclePos> list = new ArrayList<>();
        int skipped = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(VEHICLES_FILE))) {
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 6);
                if (f.length < 6) { skipped++; continue; }

                try {
                    int    tripId  = Integer.parseInt(f[0].trim());
                    int    vehId   = Integer.parseInt(f[1].trim());
                    // f[2]=day  f[3]=seqno are not needed for the benchmark
                    String geomStr = f[4].trim(); // "SRID=3857;POINT(x y)"

                    // Strip the "SRID=3857;" prefix and extract x, y from WKT
                    String wkt    = geomStr.contains(";") ? geomStr.split(";", 2)[1] : geomStr;
                    String inner  = wkt.substring(wkt.indexOf('(') + 1, wkt.indexOf(')')).trim();
                    String[] xy   = inner.split("\\s+");
                    double x = Double.parseDouble(xy[0]);
                    double y = Double.parseDouble(xy[1]);

                    // Point STBox: both SW and NE corners collapse to the same point.
                    // Locale.US ensures a dot decimal separator in the WKT string.
                    String boxStr = String.format(java.util.Locale.US,
                            "SRID=3857;STBOX X((%f %f),(%f %f))", x, y, x, y);
                    Pointer bbox = stbox_in(boxStr);

                    // Point geometry for exact containment check
                    String pointWkt = String.format(java.util.Locale.US,
                            "POINT(%f %f)", x, y);
                    Pointer geom = geo_from_text(pointWkt, 3857);

                    if (bbox != null && geom != null)
                        list.add(new VehiclePos(tripId, vehId, bbox, geom));
                    else
                        skipped++;

                } catch (Exception e) { skipped++; }
            }
        }

        System.out.printf("  Vehicle positions loaded: %,d  (skipped: %d)%n",
                list.size(), skipped);
        return list;
    }

    // -------------------------------------------------------------------------
    // Tiling helper
    // -------------------------------------------------------------------------

    /**
     * Builds a synthetic STBox that copies commune {@code c} shifted {@code shiftY}
     * metres northward on the Y axis.
     *
     * <p>SRID 3857 uses metres as its unit, so adding 50 000 to Y
     * moves the box exactly 50 km north, placing it well outside Brussels and ensuring
     * that the real commune polygon (which stays at its original position) does not
     * intersect the shifted box.  This lets {@code geom_intersects2d} measure the
     * pure call cost without inflating the true match count.
     */
    private static Pointer makeShiftedBox(CommuneData c, double shiftY) {
        String boxStr = String.format(java.util.Locale.US,
                "SRID=3857;STBOX X((%f %f),(%f %f))",
                c.xmin, c.ymin + shiftY,
                c.xmax, c.ymax + shiftY);
        return stbox_in(boxStr);
    }

    // -------------------------------------------------------------------------
    // Benchmark for one scale N
    // -------------------------------------------------------------------------

    /**
     * Runs all four strategies for a given number of indexed regions N and
     * returns an array of millisecond timings:
     * {@code [msBruteStbox, msBruteExact, msRtreeStbox, msRtreeExact]}.
     *
     * <p>Tiling: for each index j in [0, N):
     * <pre>
     *   base  = j % 19         → which of the 19 communes to copy
     *   row   = j / 19         → grid row (0-based)
     *   shift = row × 50 000   → Y offset in metres
     * </pre>
     *
     * <p>{@code srcIdx[j]} stores the real commune index for the exact check — the
     * same polygon is reused regardless of how far north the box was shifted.
     */
    private static long[] runBenchmark(int N,
                                       List<CommuneData> communes,
                                       List<VehiclePos>  positions) {
        final int nCommunes = communes.size(); // 19 for real data

        // Build N synthetic bounding boxes (tiled copies of the 19 communes)
        List<Pointer> synBoxes = new ArrayList<>(N);
        int[] srcIdx = new int[N]; // srcIdx[j] = real commune index for exact check

        for (int j = 0; j < N; j++) {
            int    base  = j % nCommunes;
            double shift = (j / nCommunes) * OFFSET_M;
            synBoxes.add(makeShiftedBox(communes.get(base), shift));
            srcIdx[j] = base;
        }

        // ── Strategy 1: Brute force - STBox only ─────────────────────────────
        // Check every vehicle position against every one of the N boxes.
        long t0 = System.currentTimeMillis();
        for (VehiclePos pos : positions)
            for (Pointer box : synBoxes)
                overlaps_stbox_stbox(pos.bbox, box);
        long msBruteStbox = System.currentTimeMillis() - t0;

        // ── Strategy 2: Brute force - STBox + exact check ────────────────────
        // Same first pass, but for each STBox hit also call geom_intersects2d.
        // The polygon is at Brussels while the box is shifted north, so the exact
        // check almost always returns false, but we still pay the call cost.
        t0 = System.currentTimeMillis();
        for (VehiclePos pos : positions) {
            for (int j = 0; j < N; j++) {
                if (overlaps_stbox_stbox(pos.bbox, synBoxes.get(j))) {
                    geom_intersects2d(pos.geom, communes.get(srcIdx[j]).geom);
                }
            }
        }
        long msBruteExact = System.currentTimeMillis() - t0;

        // ── Build RTree index for this scale ─────────────────────────────────
        // We insert all N synthetic boxes; the stored ID for each is its index j.
        // rtree_insert takes a long for the id.
        Pointer synTree = rtree_create_stbox();
        for (int j = 0; j < N; j++)
            rtree_insert(synTree, synBoxes.get(j), j);

        // countPtr is a native int-sized slot that rtree_search writes its
        // result count into.  We reset it to 0 before each search call.
        Runtime rt      = Runtime.getSystemRuntime();
        Pointer countPtr = Memory.allocate(rt, Integer.BYTES);

        // ── Strategy 3: RTree - STBox only ───────────────────────────────────
        // rtree_search traverses the index in O(log N) and returns only the
        // IDs of boxes that overlap the query.
        t0 = System.currentTimeMillis();
        for (VehiclePos pos : positions) {
            countPtr.putInt(0, 0); // reset before each call
            Pointer idsPtr = rtree_search(synTree, RTreeSearchOp.RTREE_OVERLAPS.getValue(), pos.bbox, countPtr);
            int cnt = countPtr.getInt(0);
            if (idsPtr != null && cnt > 0) {
                int[] ids = new int[cnt];
                idsPtr.get(0, ids, 0, cnt);
                // ids[] now holds the j-indices of overlapping boxes
            }
        }
        long msRtreeStbox = System.currentTimeMillis() - t0;

        // ── Strategy 4: RTree — STBox + exact check ──────────────────────────
        // Same RTree first pass, but also run geom_intersects2d on each candidate.
        t0 = System.currentTimeMillis();
        for (VehiclePos pos : positions) {
            countPtr.putInt(0, 0);
            Pointer idsPtr = rtree_search(synTree, RTreeSearchOp.RTREE_OVERLAPS.getValue(), pos.bbox, countPtr);
            int cnt = countPtr.getInt(0);
            if (idsPtr != null && cnt > 0) {
                int[] ids = new int[cnt];
                idsPtr.get(0, ids, 0, cnt);
                for (int id : ids) {
                    // srcIdx[id] = which real commune polygon to test against
                    geom_intersects2d(pos.geom, communes.get(srcIdx[id]).geom);
                }
            }
        }
        long msRtreeExact = System.currentTimeMillis() - t0;

        rtree_free(synTree);

        return new long[]{ msBruteStbox, msBruteExact, msRtreeStbox, msRtreeExact };
    }

    // -------------------------------------------------------------------------
    // Main
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        meos_initialize();
        meos_initialize_timezone("UTC");

        try {
            printHeader();

            // Load the two datasets
            System.out.println("\n── Loading Brussels communes ────────────────────────────────");
            List<CommuneData> communes = loadCommunes();

            System.out.println("\n── Loading BerlinMOD vehicle positions ──────────────────────");
            List<VehiclePos> positions = loadVehiclePositions();

            printExplanation(communes.size(), positions.size());

            String sep = "+--------+------------------+------------------+------------------+------------------+----------+----------+";

            // ── Per-run tables ───────────────────────────────────────────────
            // accumulated[scaleIdx][colIdx] accumulates the sum of each timing
            // across all runs, so we can compute averages at the end.
            // colIdx: 0=BruteSTBox, 1=BruteExact, 2=RTreeSTBox, 3=RTreeExact
            long[][] accumulated = new long[SCALES.length][4];

            for (int run = 1; run <= BENCHMARK_RUNS; run++) {
                System.out.printf("%n── Run %d / %d ──────────────────────────────────────────────%n",
                        run, BENCHMARK_RUNS);
                System.out.println(sep);
                System.out.println("|   N    | BruteSTBox  (ms) | BruteExact  (ms) | RTreeSTBox  (ms) | RTreeExact  (ms) | STBox×   | Exact×   |");
                System.out.println(sep);

                for (int i = 0; i < SCALES.length; i++) {
                    int N = SCALES[i];
                    System.out.printf("  Running N = %,d regions...%n", N);
                    long[] ms = runBenchmark(N, communes, positions);

                    accumulated[i][0] += ms[0];
                    accumulated[i][1] += ms[1];
                    accumulated[i][2] += ms[2];
                    accumulated[i][3] += ms[3];

                    double speedupStbox = (double) ms[0] / Math.max(ms[2], 1);
                    double speedupExact = (double) ms[1] / Math.max(ms[3], 1);

                    System.out.printf("| %6d | %16d | %16d | %16d | %16d | %7.1fx | %7.1fx |%n",
                            N, ms[0], ms[1], ms[2], ms[3], speedupStbox, speedupExact);
                }
                System.out.println(sep);
            }

            // ── Average table ────────────────────────────────────────────────
            System.out.printf("%n══ Average over %d runs %s%n", BENCHMARK_RUNS, "═".repeat(45));
            System.out.println(sep);
            System.out.println("|   N    | BruteSTBox  (ms) | BruteExact  (ms) | RTreeSTBox  (ms) | RTreeExact  (ms) | STBox×   | Exact×   |");
            System.out.println(sep);

            for (int i = 0; i < SCALES.length; i++) {
                long avgBruteStbox = accumulated[i][0] / BENCHMARK_RUNS;
                long avgBruteExact = accumulated[i][1] / BENCHMARK_RUNS;
                long avgRtreeStbox = accumulated[i][2] / BENCHMARK_RUNS;
                long avgRtreeExact = accumulated[i][3] / BENCHMARK_RUNS;

                double speedupStbox = (double) avgBruteStbox / Math.max(avgRtreeStbox, 1);
                double speedupExact = (double) avgBruteExact / Math.max(avgRtreeExact, 1);

                System.out.printf("| %6d | %16d | %16d | %16d | %16d | %7.1fx | %7.1fx |%n",
                        SCALES[i], avgBruteStbox, avgBruteExact, avgRtreeStbox, avgRtreeExact,
                        speedupStbox, speedupExact);
            }
            System.out.println(sep);

            printFooter(positions.size());

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            meos_finalize();
        }
    }

    // -------------------------------------------------------------------------
    // Display helpers
    // -------------------------------------------------------------------------

    private static void printHeader() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║    BerlinMOD — RTree vs Brute Force Scalability Benchmark          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Four strategies are timed at each scale N (number of indexed regions):");
        System.out.println();
        System.out.println("  BruteSTBox  │ overlaps_stbox_stbox on all N regions    O(pos × N)");
        System.out.println("  BruteExact  │ STBox first pass + geom_intersects2d     O(pos × N)");
        System.out.println("  RTreeSTBox  │ rtree_search (≈ log N candidates)       O(pos × log N)");
        System.out.println("  RTreeExact  │ RTree candidates + geom_intersects2d    O(pos × log N)");
        System.out.println();
        System.out.println("  STBox× / Exact× = speedup factor (> 1.0 means RTree is faster).");
    }

    private static void printExplanation(int nCommunes, int nPositions) {
        System.out.println("\n── Tiling strategy ──────────────────────────────────────────────");
        System.out.printf( "  %d real communes tiled northward (50 km per row) to reach N = %,d.%n",
                nCommunes, SCALES[SCALES.length - 1]);
        System.out.println("  Exact check uses the real polygon at (j % " + nCommunes + "):");
        System.out.println("  polygon stays in Brussels, box is shifted north → almost no true hits,");
        System.out.println("  but the call cost is fully measured.");
        System.out.println();
        System.out.println("── Expected scaling ─────────────────────────────────────────────");
        System.out.printf( "  %,d vehicle positions queried per scale run.%n", nPositions);
        System.out.println("  N ≈ 19   → RTree overhead dominates; brute force may be faster.");
        System.out.println("  N ≈ 100  → crossover point.");
        System.out.println("  N ≥ 500  → RTree clearly faster on the STBox pass.");
        System.out.println("  Exact column: cost grows with false-positive rate, not with N.");
    }

    private static void printFooter(int nPositions) {
        System.out.println();
        System.out.printf("  %,d vehicle positions were queried at each scale.%n", nPositions);
        System.out.println("  Brute = O(pos × N).   RTree ≈ O(pos × log N).");
        System.out.println("  STBox×  = BruteSTBox / RTreeSTBox");
        System.out.println("  Exact×  = BruteExact / RTreeExact");
        System.out.printf("  Average computed over %d run(s) — adjust BENCHMARK_RUNS to change.%n",
                BENCHMARK_RUNS);
        System.out.println();
        System.out.println("  Interpretation:");
        System.out.println("  • A speedup < 1.0 at small N is normal — the RTree has a fixed");
        System.out.println("    construction overhead that only pays off above ≈ 100 regions.");
        System.out.println("  • The Exact columns converge because geom_intersects2d is O(1)");
        System.out.println("    per candidate; once RTree reduces candidates, both strategies");
        System.out.println("    spend most of their time in the polygon check, not the filter.");
    }
}