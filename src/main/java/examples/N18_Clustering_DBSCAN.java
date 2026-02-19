package examples;

import jnr.ffi.Pointer;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;

import java.io.*;
import java.util.*;

import static functions.functions.*;

/**
 * A simple program that reads from a CSV file obtained from geonames
 * https://download.geonames.org/export/dump/US.zip
 * and applies the PostGIS function ST_ClusterDBScan to the entire file.
 *
 * This corresponds to the following SQL query:
 * <pre>
 * SELECT *, ST_ClusterDBScan(geom, 2000, 5)
 *   OVER (PARTITION BY admin1) AS cluster
 * FROM geonames
 * WHERE fcode = 'SCH';
 * </pre>
 *
 * The program expects the file data/US.txt extracted from geonames
 * and produces a new file data/geonames_new.csv with cluster assignments.
 */
public class N18_Clustering_DBSCAN {

    // Maximum number of input rows
    private static final int MAX_ROWS = 250000;
    // Maximum number of rows per window (per state)
    private static final int MAX_ROWS_WIN = 15000;
    // Batch size for progress markers
    private static final int NO_INSTS_BATCH = 10000;

    /**
     * Display explanation of DBSCAN clustering
     */
    private static void displayExplanation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DBSCAN CLUSTERING - Geonames Schools (USA)");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("What this program does:");
        System.out.println("─────────────────────");
        System.out.println("1. Reads geonames data (US.txt)");
        System.out.println("2. Filters schools (fcode='SCH')");
        System.out.println("3. Groups schools by state (admin1)");
        System.out.println("4. Applies DBSCAN clustering per state");
        System.out.println("5. Writes results to geonames_new.csv");
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                  DBSCAN CLUSTERING                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("What is DBSCAN?");
        System.out.println("───────────────");
        System.out.println("DBSCAN = Density-Based Spatial Clustering of Applications");
        System.out.println("         with Noise");
        System.out.println();
        System.out.println("Unlike K-means:");
        System.out.println("• No need to specify K (number of clusters)");
        System.out.println("• Finds clusters of arbitrary shape");
        System.out.println("• Identifies outliers as 'noise'");
        System.out.println();
        System.out.println("Parameters:");
        System.out.println("───────────");
        System.out.println("• eps (epsilon):  2000 meters");
        System.out.println("  → Maximum distance between two points in same cluster");
        System.out.println();
        System.out.println("• minpoints:      5");
        System.out.println("  → Minimum points needed to form a dense region");
        System.out.println();
        System.out.println("How it works:");
        System.out.println("─────────────");
        System.out.println();
        System.out.println("1. CORE POINTS");
        System.out.println("   Point with ≥ minpoints neighbors within eps distance");
        System.out.println("   ");
        System.out.println("   Example: Point A has 6 neighbors within 2000m");
        System.out.println("   → A is a CORE point (6 ≥ 5)");
        System.out.println();
        System.out.println("2. BORDER POINTS");
        System.out.println("   Point within eps of a core point, but not core itself");
        System.out.println("   ");
        System.out.println("   Example: Point B has 3 neighbors, but near core A");
        System.out.println("   → B is a BORDER point");
        System.out.println();
        System.out.println("3. NOISE POINTS");
        System.out.println("   Point not core and not near any core point");
        System.out.println("   ");
        System.out.println("   Example: Point C has 2 neighbors, far from all cores");
        System.out.println("   → C is NOISE (outlier)");
        System.out.println();
        System.out.println("Visual Example:");
        System.out.println("───────────────");
        System.out.println();
        System.out.println("  Schools in a region:");
        System.out.println();
        System.out.println("    [A][B][C]              Cluster 0 (urban area)");
        System.out.println("    [D][E][F]              6 schools close together");
        System.out.println();
        System.out.println("        [G][H]             Cluster 1 (suburb)");
        System.out.println("        [I][J]             4 schools grouped");
        System.out.println();
        System.out.println("           [K]             NOISE (isolated school)");
        System.out.println();
        System.out.println("Advantages:");
        System.out.println("───────────");
        System.out.println("✓ Automatic cluster count");
        System.out.println("✓ Identifies outliers");
        System.out.println("✓ Works well with spatial data");
        System.out.println();
        System.out.println("Use Cases:");
        System.out.println("──────────");
        System.out.println("• Finding urban clusters vs isolated areas");
        System.out.println("• Identifying service gaps (noise = underserved)");
        System.out.println("• Analyzing spatial density patterns");
        System.out.println("• Hot spot detection");
        System.out.println();
        System.out.println("Comparison:");
        System.out.println("───────────");
        System.out.println("┌────────────┬──────────┬────────────┬─────────┐");
        System.out.println("│ Method     │ # Clust. │ Shape      │ Outliers│");
        System.out.println("├────────────┼──────────┼────────────┼─────────┤");
        System.out.println("│ K-means    │ Fixed K  │ Spherical  │ No      │");
        System.out.println("│ DBSCAN     │ Auto     │ Arbitrary  │ Yes     │");
        System.out.println("│ Intersect. │ Auto     │ Connected  │ No      │");
        System.out.println("└────────────┴──────────┴────────────┴─────────┘");
        System.out.println();
    }

    public static void main(String[] args) {
        // Display explanation
        displayExplanation();

        // Get start time
        long startTime = System.currentTimeMillis();

        // Initialize MEOS
        meos_initialize();
        meos_initialize_timezone("UTC");

        // Allocate arrays to save data
        int[] geonameid = new int[MAX_ROWS];
        String[] name = new String[MAX_ROWS];
        String[] admin1 = new String[MAX_ROWS];
        Pointer[] geom = new Pointer[MAX_ROWS];
        int[] cluster = new int[MAX_ROWS];

        // Track distinct admin1 values (states)
        List<String> admin1List = new ArrayList<>();
        Map<String, Integer> admin1Counts = new HashMap<>();

        int no_records = 0;
        int no_nulls = 0;
        int count = 0;

        try {
            System.out.println("=".repeat(60));
            System.out.println("READING GEONAMES DATA");
            System.out.println("=".repeat(60));
            System.out.println("Reading (one '*' every " + NO_INSTS_BATCH + " records):");
            System.out.print("  ");

            // Open the input file
            BufferedReader br = new BufferedReader(
                    new FileReader("src/main/java/examples/data/US.txt"));

            // Read the file
            String line;
            while ((line = br.readLine()) != null && count < MAX_ROWS) {
                no_records++;

                if (no_records % NO_INSTS_BATCH == 0) {
                    System.out.print("*");
                    System.out.flush();
                }

                // Split TSV line
                String[] fields = line.split("\t");

                if (fields.length >= 11) {
                    // Check if fcode = 'SCH' (school)
                    if (fields.length > 7 && "SCH".equals(fields[7])) {
                        // Save the data
                        geonameid[count] = Integer.parseInt(fields[0]);
                        name[count] = fields[1];
                        admin1[count] = fields[10]; // State code

                        // Parse coordinates
                        double lon = Double.parseDouble(fields[5]);
                        double lat = Double.parseDouble(fields[4]);

                        // Create point in 4326 (WGS84)
                        Pointer gs = geompoint_make2d(4326, lon, lat);
                        // Transform to 5070 (projected)
                        geom[count] = geo_transform(gs, 5070);

                        // Track admin1 values
                        if (!admin1List.contains(admin1[count])) {
                            admin1List.add(admin1[count]);
                        }

                        count++;
                    }
                }
            }

            br.close();

            System.out.println("\n");
            System.out.println("=".repeat(60));
            System.out.println("CLUSTERING PER STATE");
            System.out.println("=".repeat(60));

            // Iterate for each admin1 value (state) and perform clustering
            for (int i = 0; i < admin1List.size(); i++) {
                String state = admin1List.get(i);

                // Collect geometries for this state
                List<Integer> ids = new ArrayList<>();
                List<Pointer> geoms = new ArrayList<>();

                for (int j = 0; j < count; j++) {
                    if (state.equals(admin1[j])) {
                        ids.add(j);
                        geoms.add(geom[j]);
                    }
                }

                int stateCount = geoms.size();
                System.out.printf("%d: %s, %d schools\n", i, state, stateCount);

                if (stateCount > 0) {
                    Runtime runtime = Runtime.getSystemRuntime();
                    // Create array of geometry pointers
                    Pointer geometriesPtr = Memory.allocate(runtime, stateCount * 8);
                    for (int j = 0; j < stateCount; j++) {
                        geometriesPtr.putPointer(j * 8, geoms.get(j));
                    }

                    // Create output parameter for count
                    Pointer countPtr = Memory.allocate(runtime, Integer.BYTES);

                    // Apply DBSCAN clustering
                    // eps = 2000 meters, minpoints = 5
                    Pointer clustersPtr = geo_cluster_dbscan(
                            geometriesPtr, stateCount, 2000.0, 5, countPtr);

                    // Read cluster assignments
                    int[] stateClusters = new int[stateCount];
                    clustersPtr.get(0, stateClusters, 0, stateCount);

                    // Assign clusters back to original records
                    for (int j = 0; j < stateCount; j++) {
                        cluster[ids.get(j)] = stateClusters[j];
                    }
                }
            }

            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("WRITING RESULTS");
            System.out.println("=".repeat(60));

            // Open the output file
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("src/main/java/examples/data/geonames_new.csv"));

            // Write header
            writer.write("geonameid,name,admin1,geom,cluster\n");

            // Write records
            for (int j = 0; j < count; j++) {
                String geomStr = geo_out(geom[j]);
                writer.write(String.format("%d,%s,%s,%s,%d\n",
                        geonameid[j], name[j], admin1[j], geomStr, cluster[j]));
            }

            writer.close();

            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("RESULTS");
            System.out.println("=".repeat(60));
            System.out.printf("Total records read:     %d\n", no_records);
            System.out.printf("Incomplete records:     %d\n", no_nulls);
            System.out.printf("Schools clustered:      %d\n", count);
            System.out.printf("States processed:       %d\n", admin1List.size());
            System.out.printf("Output file:            geonames_new.csv\n");
            System.out.println("=".repeat(60));

            // Calculate elapsed time
            long elapsed = System.currentTimeMillis() - startTime;
            double seconds = elapsed / 1000.0;
            System.out.printf("\nThe program took %.2f seconds to execute\n", seconds);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Finalize MEOS
            meos_finalize();
        }
    }
}