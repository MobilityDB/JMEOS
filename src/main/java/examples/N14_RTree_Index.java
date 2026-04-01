package examples;

import jnr.ffi.Pointer;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;
import types.enums.RTreeSearchOp;

import java.util.Random;

import static functions.functions.*;

/**
 * Demonstrates RTree spatial indexing for efficient bounding box searches.
 *
 * RTree is a spatial data structure that organizes bounding boxes hierarchically
 * to enable fast search operations. Instead of checking ALL boxes (brute force),
 * RTree only checks boxes in relevant areas.
 *
 * This program:
 * 1. Creates 5,000,000 random STBox bounding boxes
 * 2. Inserts them into an RTree index
 * 3. Searches for overlapping boxes using:
 *    - RTree index (FAST)
 *    - Brute force (SLOW)
 * 4. Compares performance and validates correctness
 *
 * Note: RTree is not a silver bullet
 * For small datasets, Brute Force can outperform the R-Tree because:
 * 1. Initialization Cost
 * - Building the index and managing native memory pointers adds a fixed overhead.
 *  - For small datasets, this setup time can outpace the actual search gains, making Brute Force faster.
 * 2. Search Threshold
 * - The R-Tree only becomes profitable when the time saved by "pruning" the search space exceeds the time spent traversing the tree structure.
 *
 * STBox = Spatio-Temporal Box with:
 * - Spatial extent: (xmin, ymin) to (xmax, ymax)
 * - Temporal extent: [tmin, tmax]
 */
public class N14_RTree_Index {

    // Configuration
    private static final int NO_BBOX = 5_000_000;  // Number of boxes to index
    private static final int SEED = 1;         // Random seed (1 = reproducible results)

    // Results tracking
    private static boolean[] indexResult;
    private static boolean[] actualResult;
    private static Pointer[] boxes;  // Array of STBox pointers

    /**
     * Generate a random bounding box for testing
     */
    private static Pointer generateRandomBox(Random rand) {
        // Spatial dimensions (in meters, UTM coordinates)
        int xmin = rand.nextInt(1000) + 1;
        int xmax = xmin + rand.nextInt(10) + 1;
        int ymin = rand.nextInt(1000) + 1;
        int ymax = ymin + rand.nextInt(10) + 1;

        // Temporal dimensions (seconds)
        int timeMin = rand.nextInt(29) + 1;
        int timeMax = timeMin + rand.nextInt(29) + 1;

        // Create STBox string
        String boxStr = String.format(
                "SRID=25832;STBOX XT(((%d %d),(%d %d)),[2023-01-01 01:00:%02d+00, 2023-01-01 01:00:%02d+00])",
                xmin, ymin, xmax, ymax, timeMin, timeMax
        );

        return stbox_in(boxStr);
    }

    /**
     * Generate a search query box
     */
    private static Pointer generateQueryBox() {
        String boxStr = "SRID=25832;STBOX XT(((0 0),(100 100))," +
                "[2023-01-01 01:00:00+00, 2023-01-01 01:00:60+00])";
        return stbox_in(boxStr);
    }

    /**
     * SECTION 1: RTree Index Search (FAST)
     */
    private static void performIndexSearch(Pointer rtree, Pointer queryBox) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 1: RTree Index Search (OPTIMIZED)");
        System.out.println("=".repeat(60));

        long startTime = System.currentTimeMillis();

        // Create Pointer for count (output parameter)
        // rtree_search needs a Pointer to write the count into
        Runtime runtime = Runtime.getSystemRuntime();
        Pointer countPtr = Memory.allocate(runtime, Integer.BYTES);
        countPtr.putInt(0, 0);  // Initialize to 0

        // Search using RTree index
        // rtree_search will write the number of results into countPtr
        Pointer idsPtr = rtree_search(rtree, RTreeSearchOp.RTREE_OVERLAPS.getValue(), queryBox, countPtr);

        // Read the count value written by rtree_search
        int count = countPtr.getInt(0);

        long elapsed = System.currentTimeMillis() - startTime;

        // Mark results
        if (idsPtr != null && count > 0) {
            int[] ids = new int[count];
            idsPtr.get(0, ids, 0, count);

            for (int id : ids) {
                indexResult[id] = true;
            }
        }

        System.out.println("┌─ RTREE INDEX RESULTS ────────────────────");
        System.out.println("│ Boxes found: " + count);
        System.out.println("│ Search time: " + elapsed + " ms");
        System.out.println("│ Method: RTree spatial index");
        System.out.println("└──────────────────────────────────────────");
    }

    /**
     * SECTION 2: Brute Force Search (SLOW)
     */
    private static void performBruteForceSearch(Pointer queryBox) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 2: Brute Force Search (UNOPTIMIZED)");
        System.out.println("=".repeat(60));

        long startTime = System.currentTimeMillis();

        int count = 0;

        // Check every single box (no index!)
        for (int i = 0; i < NO_BBOX; i++) {
            if (overlaps_stbox_stbox(boxes[i], queryBox)) {
                actualResult[i] = true;
                count++;
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;

        System.out.println("┌─ BRUTE FORCE RESULTS ────────────────────");
        System.out.println("│ Boxes found: " + count);
        System.out.println("│ Search time: " + elapsed + " ms");
        System.out.println("│ Method: Check every box sequentially");
        System.out.println("└──────────────────────────────────────────");
    }

    /**
     * SECTION 3: Validate and Compare Results
     */
    private static void validateResults() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 3: Validation & Performance Comparison");
        System.out.println("=".repeat(60));

        int indexCount = 0;
        int bruteCount = 0;
        int errors = 0;

        for (int i = 0; i < NO_BBOX; i++) {
            if (indexResult[i]) indexCount++;
            if (actualResult[i]) bruteCount++;

            // Check for discrepancies
            if (indexResult[i] != actualResult[i]) {
                errors++;
                if (errors <= 5) {  // Show first 5 errors only
                    System.err.println("ERROR: Box " + i +
                            " - Index: " + indexResult[i] +
                            ", Brute: " + actualResult[i]);
                }
            }
        }

        System.out.println("\n┌─ COMPARISON ─────────────────────────────");
        System.out.println("│ Total boxes indexed: " + NO_BBOX);
        System.out.println("│");
        System.out.println("│ RTree index found:   " + indexCount + " boxes");
        System.out.println("│ Brute force found:   " + bruteCount + " boxes");
        System.out.println("│");

        if (errors == 0) {
            System.out.println("│ ✓ Results match perfectly!");
            System.out.println("│ ✓ RTree index is CORRECT and FAST");
        } else {
            System.out.println("│ ✗ Errors found: " + errors);
        }

        System.out.println("└──────────────────────────────────────────");
    }

    /**
     * Explain RTree concept
     */
    private static void explainRTree() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              RTREE SPATIAL INDEX EXPLAINED               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("What is an RTree?");
        System.out.println("─────────────────");
        System.out.println("An RTree is like a table of contents for spatial data.");
        System.out.println("Instead of checking EVERY box, it organizes them hierarchically");
        System.out.println("so you only check relevant areas.");
        System.out.println();
        System.out.println("┌─ BRUTE FORCE (No Index) ─────────────────────────────────");
        System.out.println("│");
        System.out.println("│ Query: Find all boxes overlapping (0,0) to (100,100)");
        System.out.println("│");
        System.out.println("│ Method: Check ALL 10,000 boxes one by one");
        System.out.println("│         → 10,000 overlap checks!");
        System.out.println("│");
        System.out.println("│ for (each of 10,000 boxes) {");
        System.out.println("│     if (box overlaps query) → found!");
        System.out.println("│ }");
        System.out.println("│");
        System.out.println("│ Speed: SLOW (O(n) - linear)");
        System.out.println("│");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("┌─ RTREE INDEX (Spatial Index) ────────────────────────────");
        System.out.println("│");
        System.out.println("│ Query: Find all boxes overlapping (0,0) to (100,100)");
        System.out.println("│");
        System.out.println("│ Method: Navigate tree, skip irrelevant branches");
        System.out.println("│         → Only ~10-100 checks needed!");
        System.out.println("│");
        System.out.println("│ RTree Structure (example):");
        System.out.println("│");
        System.out.println("│          [ROOT: All boxes (0-1000, 0-1000)]");
        System.out.println("│                    /              \\");
        System.out.println("│       [Left quadrant]      [Right quadrant]");
        System.out.println("│       (0-500, 0-500)          (500-1000)");
        System.out.println("│          /     \\                  ↓");
        System.out.println("│     [Boxes]  [Boxes]        SKIP! (outside query)");
        System.out.println("│     (check)  (check)");
        System.out.println("│");
        System.out.println("│ Speed: FAST (O(log n) - logarithmic)");
        System.out.println("│");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("Use cases:");
        System.out.println("──────────");
        System.out.println("• Find all ships in a specific ocean region");
        System.out.println("• Find all events during a time period in a location");
        System.out.println("• Spatial queries in databases (PostGIS, MobilityDB)");
        System.out.println("• Game engines (collision detection)");
        System.out.println("• Map applications (finding nearby places)");
        System.out.println();
    }

    public static void main(String[] args) {
        // Initialize MEOS
        meos_initialize();
        meos_initialize_timezone("UTC");

        try {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("RTree Spatial Index Demonstration");
            System.out.println("Processing " + NO_BBOX + " STBox bounding boxes");
            System.out.println("=".repeat(60));

            // Explain concept first
            explainRTree();

            // Initialize results tracking
            indexResult = new boolean[NO_BBOX];
            actualResult = new boolean[NO_BBOX];
            boxes = new Pointer[NO_BBOX];

            // Initialize random generator (fixed seed for reproducibility)
            Random rand = new Random(SEED);

            // Create RTree index
            System.out.println("\n" + "=".repeat(60));
            System.out.println("Building RTree Index...");
            System.out.println("=".repeat(60));

            long buildStart = System.currentTimeMillis();
            Pointer rtree = rtree_create_stbox();

            // Generate and insert random boxes
            System.out.println("Inserting " + NO_BBOX + " boxes: ");
            System.out.println("One '*' marker every 1000 boxes inserted!");
            for (int i = 0; i < NO_BBOX; i++) {
                if (i % 1000 == 0) {
                    System.out.print("*");
                    System.out.flush();
                }

                boxes[i] = generateRandomBox(rand);
                rtree_insert(rtree, boxes[i], i);
            }

            long buildElapsed = System.currentTimeMillis() - buildStart;

            System.out.println("\n\n┌─ INDEX BUILD COMPLETE ───────────────────");
            System.out.println("│ Boxes indexed: " + NO_BBOX);
            System.out.println("│ Build time: " + buildElapsed + " ms");
            System.out.println("│ Index type: RTree (STBox)");
            System.out.println("└──────────────────────────────────────────");

            // Generate query box
            Pointer queryBox = generateQueryBox();
            String queryStr = stbox_out(queryBox, 3);
            System.out.println("\n┌─ QUERY BOX ──────────────────────────────");
            System.out.println("│ " + queryStr);
            System.out.println("│ Coverage: (0,0) to (100,100) meters");
            System.out.println("└──────────────────────────────────────────");

            // Perform searches
            performIndexSearch(rtree, queryBox);
            performBruteForceSearch(queryBox);

            // Validate and compare
            validateResults();

            System.out.println("\n" + "=".repeat(60));
            System.out.println("DEMONSTRATION COMPLETED!");
            System.out.println("=".repeat(60));

            // Cleanup
            rtree_free(rtree);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Finalize MEOS
            meos_finalize();
        }
    }
}