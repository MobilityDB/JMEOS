package examples;

import jnr.ffi.Pointer;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;

import java.io.*;
import java.util.*;

import static functions.functions.*;

/**
 * Demonstrates K-means spatial clustering on populated places.
 *
 * K-means clustering groups geographic points into K clusters based on proximity.
 * Each cluster contains points that are close to each other spatially.
 *
 * This program:
 * 1. Reads populated places from CSV (name, population, location)
 * 2. Applies K-means clustering to group places into 10 clusters
 * 3. Writes results with cluster assignments to output CSV
 *
 * SQL Equivalent in PostGIS:
 * <pre>
 * SELECT name, pop_max, geom,
 *   ST_ClusterKMeans(geom, 10) OVER () AS cluster
 * FROM popplaces;
 * </pre>
 * https://postgis.net/docs/ST_ClusterKMeans.html
 *
 * Use Cases:
 * - Grouping cities by geographic region
 * - Optimizing location-based services
 *
 * Data Source: Natural Earth 1:10M Populated Places
 * https://www.naturalearthdata.com/downloads/10m-cultural-vectors/10m-populated-places/
 */
public class N16_Clustering_KMeans {

    // Configuration
    private static final int MAX_ROWS = 50000;
    private static final int BATCH_SIZE = 1000;
    private static final int NUM_CLUSTERS = 10;
    private static final int NUM_SAMPLES = 5;
    private static final String DATA_DIR = "src/main/java/examples/data/";
    private static final String INPUT_FILE = DATA_DIR + "popplaces.csv";
    private static final String OUTPUT_FILE = DATA_DIR + "popplaces_clustered.csv";

    /**
     * Record representing a populated place
     */
    static class PopulatedPlace {
        String name;
        int population;
        Pointer geom;
        int cluster;

        PopulatedPlace(String name, int population, Pointer geom) {
            this.name = name;
            this.population = population;
            this.geom = geom;
            this.cluster = -1;
        }
    }

    /**
     * Read populated places from CSV file
     */
    private static List<PopulatedPlace> readPlaces() throws IOException {
        List<PopulatedPlace> places = new ArrayList<>();
        int nullCount = 0;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Reading populated places from CSV");
        System.out.println("=".repeat(60));
        System.out.println("Reading (one '*' every " + BATCH_SIZE + " records):");
        System.out.print("  ");

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            // Skip header
            String header = br.readLine();

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null && places.size() < MAX_ROWS) {
                lineNumber++;

                // Parse CSV: name,pop_max,geom
                String[] parts = line.split(",", 3);

                if (parts.length != 3) {
                    System.out.println("Record nb. " + lineNumber + " with missing values ignored");
                    nullCount++;
                    continue;
                }

                try {
                    String name = parts[0].trim();
                    int population = Integer.parseInt(parts[1].trim());
                    String geomWKT = parts[2].trim();

                    // Parse geometry
                    Pointer geom = geom_in(geomWKT, -1);

                    if (geom != null) {
                        places.add(new PopulatedPlace(name, population, geom));

                        // Print progress marker
                        if (places.size() % BATCH_SIZE == 0) {
                            System.out.print("*");
                            System.out.flush();
                        }
                    } else {
                        nullCount++;
                    }
                } catch (NumberFormatException e) {
                    nullCount++;
                }
            }
        }

        System.out.println("\n\n┌─ READING COMPLETE ───────────────────────");
        System.out.println("│ Records read: " + places.size());
        System.out.println("│ Incomplete records ignored: " + nullCount);
        System.out.println("└──────────────────────────────────────────");

        return places;
    }

    /**
     * Apply K-means clustering to the places
     */
    private static void applyKMeansClustering(List<PopulatedPlace> places) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Applying K-means Clustering");
        System.out.println("=".repeat(60));
        System.out.println("Algorithm: K-means");
        System.out.println("Number of clusters: " + NUM_CLUSTERS);
        System.out.println("Number of points: " + places.size());

        long startTime = System.currentTimeMillis();

        // Create array of geometry pointers
        int count = places.size();
        Runtime runtime = Runtime.getSystemRuntime();
        Pointer geometriesPtr = Memory.allocate(runtime, count * 8); // 8 bytes per pointer

        // Fill array with geometry pointers
        for (int i = 0; i < count; i++) {
            geometriesPtr.putPointer(i * 8, places.get(i).geom);
        }

        // Apply K-means clustering
        Pointer clustersPtr = geo_cluster_kmeans(geometriesPtr, count, NUM_CLUSTERS);

        long elapsed = System.currentTimeMillis() - startTime;

        // Read cluster assignments
        int[] clusters = new int[count];
        clustersPtr.get(0, clusters, 0, count);

        // Assign clusters to places
        for (int i = 0; i < count; i++) {
            places.get(i).cluster = clusters[i];
        }

        System.out.println("\n┌─ CLUSTERING COMPLETE ────────────────────");
        System.out.println("│ Time taken: " + elapsed + " ms");
        System.out.println("│ Clusters created: " + NUM_CLUSTERS);
        System.out.println("└──────────────────────────────────────────");
    }


    /**
     * Display sample places from each cluster
     */
    private static void displaySamplePlaces(List<PopulatedPlace> places) {
        System.out.println("\n┌─ SAMPLE PLACES PER CLUSTER ──────────────");

        for (int cluster = 0; cluster < NUM_CLUSTERS; cluster++) {
            System.out.println("│");
            System.out.println("│ Cluster " + cluster + ":");

            final int currentCluster = cluster;
            List<PopulatedPlace> clusterSamples = places.stream()
                    .filter(p -> p.cluster == currentCluster)
                    .limit(NUM_SAMPLES) // 5 samples
                    .toList();

            for (PopulatedPlace place : clusterSamples) {
                String geomStr = geo_out(place.geom);
                System.out.printf("│   - %s (pop: %,d) %s\n",
                        place.name, place.population, geomStr);
            }

            if (clusterSamples.isEmpty()) {
                System.out.println("│   (No places assigned to this cluster)");
            }
        }

        System.out.println("└──────────────────────────────────────────");
    }

    /**
     * Write clustered results to CSV file
     */
    private static void writeResults(List<PopulatedPlace> places) throws IOException {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Writing Results to CSV");
        System.out.println("=".repeat(60));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            // Write header
            writer.write("name,pop_max,geom,cluster\n");

            // Write data
            for (PopulatedPlace place : places) {
                String geomStr = geo_out(place.geom);
                writer.write(String.format("%s,%d,%s,%d\n",
                        place.name, place.population, geomStr, place.cluster));
            }
        }

        System.out.println("\n┌─ OUTPUT COMPLETE ────────────────────────");
        System.out.println("│ File: " + OUTPUT_FILE);
        System.out.println("│ Records written: " + places.size());
        System.out.println("└──────────────────────────────────────────");
    }

    /**
     * Explain K-means clustering concept
     */
    private static void explainKMeans() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              K-MEANS CLUSTERING EXPLAINED                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("What is K-means Clustering?");
        System.out.println("──────────────────────────");
        System.out.println("K-means groups geographic points into K clusters where each");
        System.out.println("point belongs to the cluster with the nearest center (centroid).");
        System.out.println();
        System.out.println("How it works:");
        System.out.println("─────────────");
        System.out.println("1. Choose K initial cluster centers (randomly or smart placement)");
        System.out.println("2. Assign each point to the nearest cluster center");
        System.out.println("3. Recalculate cluster centers (average position of all points)");
        System.out.println("4. Repeat steps 2-3 until centers don't move significantly");
        System.out.println();
        System.out.println("Example with cities:");
        System.out.println("───────────────────");
        System.out.println();
        System.out.println("  Before clustering:          After K-means (K=3):");
        System.out.println("  ┌──────────────┐            ┌──────────────┐");
        System.out.println("  │ • • •  •  •  │            │ A A A  B  B  │");
        System.out.println("  │  •   •    •  │            │  A   A    B  │");
        System.out.println("  │   •  •  •    │            │   A  A  B    │");
        System.out.println("  │ •  •   •   • │            │ C  C   C   C │");
        System.out.println("  │  •  •  •  •  │            │  C  C  C  C  │");
        System.out.println("  └──────────────┘            └──────────────┘");
        System.out.println();
        System.out.println("  Random points                3 clear clusters!");
        System.out.println();
        System.out.println("Use Cases:");
        System.out.println("──────────");
        System.out.println("✓ Delivery zones - Group customers by proximity");
        System.out.println("✓ Sales territories - Divide regions for sales teams");
        System.out.println("✓ Service areas - Emergency services coverage zones");
        System.out.println("✓ Urban planning - Identify city districts");
        System.out.println("✓ Retail analysis - Store location optimization");
        System.out.println();
        System.out.println("Advantages:");
        System.out.println("───────────");
        System.out.println("• Fast and efficient");
        System.out.println("• Simple to understand");
        System.out.println("• Works well with large datasets");
        System.out.println("• Produces compact, spherical clusters");
        System.out.println();
        System.out.println("Limitations:");
        System.out.println("────────────");
        System.out.println("• Must specify K (number of clusters) in advance");
        System.out.println("• Assumes roughly equal-sized, spherical clusters");
        System.out.println("• Sensitive to initial center placement");
        System.out.println("• May not work well with irregularly shaped regions");
        System.out.println();
    }

    public static void main(String[] args) {
        // Initialize MEOS
        meos_initialize();
        meos_initialize_timezone("UTC");

        try {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("K-MEANS SPATIAL CLUSTERING DEMONSTRATION");
            System.out.println("Populated Places Dataset");
            System.out.println("=".repeat(60));

            // Explain the concept
            explainKMeans();

            // Read data
            List<PopulatedPlace> places = readPlaces();

            if (places.isEmpty()) {
                System.err.println("No data loaded. Please ensure " + INPUT_FILE + " exists.");
                return;
            }

            // Apply clustering
            applyKMeansClustering(places);

            // Display samples
            displaySamplePlaces(places);

            // Write results
            writeResults(places);

            System.out.println("\n" + "=".repeat(60));
            System.out.println("CLUSTERING DEMONSTRATION COMPLETED!");
            System.out.println("=".repeat(60));
            System.out.println();
            System.out.println("Key Takeaways:");
            System.out.println("──────────────");
            System.out.println("1. K-means groups points by proximity into K clusters");
            System.out.println("2. Useful for creating geographic zones and territories");
            System.out.println("3. Fast algorithm suitable for large datasets");
            System.out.println("4. Results depend on K value and initial placement");
            System.out.println();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Finalize MEOS
            meos_finalize();
        }
    }
}