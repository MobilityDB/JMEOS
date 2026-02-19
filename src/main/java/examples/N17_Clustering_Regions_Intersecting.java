package examples;

import jnr.ffi.Pointer;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;

import java.io.*;

import static functions.functions.*;

/**
 * A simple program that reads from the regions.csv file obtained from
 * the BerlinMOD benchmark and applies the PostGIS function
 * ST_ClusterIntersecting or ST_ClusterWithin to create clusters.
 *
 * This corresponds to one of the following SQL queries:
 * <pre>
 * SELECT unnest(ST_ClusterIntersecting(geom)) FROM Regions;
 * -- or
 * SELECT unnest(ST_ClusterWithin(geom, 1000)) FROM Regions;
 * </pre>
 *
 * The program expects the file data/regions.csv and produces a new file
 * data/regions_new.csv with a new column cluster added to each geometry.
 */
public class N17_Clustering_Regions_Intersecting {

    // Maximum number of input rows
    private static final int MAX_ROWS = 100;
    // Define whether ST_ClusterIntersecting or ST_ClusterWithin is applied
    private static final boolean CLUSTER_INTERSECTING = false;

    /**
     * Display explanation of what the program does and the clustering methods
     */
    private static void displayExplanation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TOPOLOGICAL CLUSTERING - BerlinMOD Regions");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("What this program does:");
        System.out.println("─────────────────────");
        System.out.println("1. Reads regions from regions.csv");
        System.out.println("2. Groups regions based on spatial relationships");
        System.out.println("3. Assigns each region to a cluster");
        System.out.println("4. Writes results to regions_new.csv");
        System.out.println();
        System.out.println("Current method: " +
                (CLUSTER_INTERSECTING ? "ClusterIntersecting" : "ClusterWithin(1000m)"));
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              TWO CLUSTERING METHODS                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("┌─ METHOD 1: ClusterIntersecting ──────────────────────────");
        System.out.println("│");
        System.out.println("│ Rule: Group geometries that TOUCH or OVERLAP");
        System.out.println("│");
        System.out.println("│ Example - Land parcels:");
        System.out.println("│   ┌──┬──┐  ┌──┐        ┌──┬──┐  ┌──┐");
        System.out.println("│   │A │B │  │C │   →    │ 0  0 │  │ 1 │");
        System.out.println("│   ├──┼──┤  └──┘        ├──┼──┤  └──┘");
        System.out.println("│   │D │E │              │ 0  0 │");
        System.out.println("│   └──┴──┘              └──┴──┘");
        System.out.println("│");
        System.out.println("│   A, B, D, E touch → Cluster 0 (connected)");
        System.out.println("│   C is isolated    → Cluster 1");
        System.out.println("│");
        System.out.println("│ Use cases:");
        System.out.println("│   • Find connected road networks");
        System.out.println("│   • Identify contiguous territories");
        System.out.println("│");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("┌─ METHOD 2: ClusterWithin(distance) ──────────────────────");
        System.out.println("│");
        System.out.println("│ Rule: Group geometries within a DISTANCE threshold");
        System.out.println("│");
        System.out.println("│ Example - Buildings within 1000 meters:");
        System.out.println("│   [A]     [B]        [C]");
        System.out.println("│    └─500m─┘  └─1500m─┘");
        System.out.println("│");
        System.out.println("│   [0]     [0]        [1]");
        System.out.println("│");
        System.out.println("│   A and B are 500m apart  → Cluster 0 (within 1000m)");
        System.out.println("│   C is 1500m from B       → Cluster 1 (beyond 1000m)");
        System.out.println("│");
        System.out.println("│ Use cases:");
        System.out.println("│   • Group nearby buildings (same neighborhood)");
        System.out.println("│   • Create service zones (delivery areas)");
        System.out.println("│   • Find proximity clusters");
        System.out.println("│");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("Key Difference from K-means:");
        System.out.println("────────────────────────────");
        System.out.println("• K-means: You choose K (e.g., \"create 10 clusters\")");
        System.out.println("• Topological: Number of clusters emerges from data");
        System.out.println();
        System.out.println("Comparison:");
        System.out.println("───────────");
        System.out.println("┌──────────────┬───────────────┬──────────────────────┐");
        System.out.println("│ Method       │ Based on      │ Utility              │");
        System.out.println("├──────────────┼───────────────┼──────────────────────┤");
        System.out.println("│ Intersecting │ Touching      │ Connected regions    │");
        System.out.println("│ Within       │ Distance      │ Proximity groups     │");
        System.out.println("│ K-means      │ Centroids     │ Fixed # territories  │");
        System.out.println("└──────────────┴───────────────┴──────────────────────┘");
        System.out.println();
        System.out.println("To switch methods:");
        System.out.println("──────────────────");
        System.out.println("Change: CLUSTER_INTERSECTING = " + !CLUSTER_INTERSECTING);
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

        // Arrays to save the id and the geometries
        int[] ids = new int[MAX_ROWS];
        Pointer[] geoms = new Pointer[MAX_ROWS];
        int[] clusterNos = new int[MAX_ROWS];

        int no_records = 0;
        int no_nulls = 0;

        try {
            // Open the input file
            BufferedReader br = new BufferedReader(
                    new FileReader("src/main/java/examples/data/regions.csv"));

            // Read the first line of the file with the headers
            br.readLine();

            // Continue reading the file
            String line;
            while ((line = br.readLine()) != null && no_records < MAX_ROWS) {
                // Splitting the data
                String[] parts = line.split(",", 2);

                if (parts.length == 2) {
                    ids[no_records] = Integer.parseInt(parts[0]);
                    geoms[no_records] = geom_in(parts[1], -1);
                    no_records++;
                }
            }

            br.close();

            System.out.println("=".repeat(60));
            System.out.println("CLUSTERING IN PROGRESS");
            System.out.println("=".repeat(60));
            System.out.println("Method: " +
                    (CLUSTER_INTERSECTING ? "ClusterIntersecting" : "ClusterWithin(1000m)"));
            System.out.println("Regions to cluster: " + no_records);
            System.out.println();

            // Perform the clustering
            Runtime runtime = Runtime.getSystemRuntime();
            Pointer geometriesPtr = Memory.allocate(runtime, no_records * 8);
            for (int i = 0; i < no_records; i++) {
                geometriesPtr.putPointer(i * 8, geoms[i]);
            }

            Pointer numClustersPtr = Memory.allocate(runtime, Integer.BYTES);

            Pointer clustersPtr;
            if (CLUSTER_INTERSECTING) {
                clustersPtr = geo_cluster_intersecting(geometriesPtr, no_records,
                        numClustersPtr);
            } else {
                clustersPtr = geo_cluster_within(geometriesPtr, no_records, 1000.0,
                        numClustersPtr);
            }

            int no_clusters = numClustersPtr.getInt(0);

            // Fill the array of cluster numbers
            for (int i = 0; i < no_clusters; i++) {
                Pointer clusterGeom = clustersPtr.getPointer(i * 8);
                int numGeoms = geo_num_geos(clusterGeom);

                for (int j = 0; j < numGeoms; j++) {
                    Pointer subgeo = geo_geo_n(clusterGeom, j + 1);

                    for (int k = 0; k < no_records; k++) {
                        // -1 if there's an error (null value etc.)
                        // 1 for true
                        // 0 for false
                        if (geo_equals(subgeo, geoms[k]) == 1) {
                            clusterNos[k] = i;
                        }
                    }
                }
            }

            // Open the output file
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("src/main/java/examples/data/regions_new.csv"));

            // Write the header line in the output file
            writer.write("id,geom,cluster\n");

            // Write the records in the output file
            for (int i = 0; i < no_records; i++) {
                String geomStr = geo_out(geoms[i]);
                writer.write(String.format("%d,%s,%d\n", ids[i], geomStr, clusterNos[i]));
            }

            writer.close();

            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("RESULTS");
            System.out.println("=".repeat(60));
            System.out.printf("Records read:       %d\n", no_records);
            System.out.printf("Records ignored:    %d\n", no_nulls);
            System.out.printf("Clusters obtained:  %d\n", no_clusters);
            System.out.printf("Output file:        regions_new.csv\n");
            System.out.println("=".repeat(60));

            // Calculate the elapsed time
            long elapsed = System.currentTimeMillis() - startTime;
            double seconds = elapsed / 1000.0;
            System.out.printf("The program took %.2f seconds to execute\n", seconds);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Finalize MEOS
            meos_finalize();
        }
    }
}