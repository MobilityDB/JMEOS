package examples;

import jnr.ffi.Pointer;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;
import types.temporal.TInterpolation;

import java.time.OffsetDateTime;

import static functions.functions.*;

/**
 * Demonstrates creating temporal point sequences from coordinate arrays.
 *
 * Alternative Construction Method:
 * Instead of creating TPoint from individual instants, this approach uses
 * arrays of coordinates (x[], y[], z[], timestamps[]) to build sequences directly.
 *
 * This could be useful when data comes from:
 * - GPS logs (CSV with separate x, y, time columns)
 * - Sensor arrays (parallel data streams)
 * - Scientific instruments (time-series measurements)
 *
 * The program demonstrates three variants:
 * 1. 3D Point (x, y, z) - Projected coordinates (SRID 5676 - UTM 46N)
 * 2. 2D Point (x, y) - Projected coordinates (SRID 5676)
 * 3. 2D Point (x, y) - Geographic coordinates (SRID 4326 - WGS84)
 *
 * Function: tpointseq_make_coords(x[], y[], z[], times[], count, srid, ...)
 */
public class N15_TPoint_MakeCoords {

    // PostgreSQL epoch: 2000-01-01 00:00:00 UTC
    private static final long POSTGRES_EPOCH_MICROSECONDS = 946684800000000L;

    /**
     * Parse timestamp string to Unix timestamp (seconds since 1970-01-01)
     *
     * tpointseq_make_coords() expects Unix timestamps in seconds
     */
    private static long parseTimestamp(String timeStr) {
        // Parse string to OffsetDateTime
        OffsetDateTime odt = pg_timestamptz_in(timeStr, -1);

        // Convert to Java Instant
        java.time.Instant instant = odt.toInstant();

        // Return Unix epoch SECONDS (not microseconds, no PostgreSQL epoch adjustment)
        return instant.getEpochSecond();
    }


    /**
     * SECTION 1: 3D Point Sequence (Projected Coordinates)
     * Creates a 3D temporal point sequence with elevation (z coordinate)
     */
    private static void demonstrate3DProjected() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 1: 3D Point Sequence (Projected - UTM 46N)");
        System.out.println("=".repeat(60));

        // Coordinate arrays (UTM coordinates in meters)
        double[] xcoords = {691875.63, 691876.45, 691875.82, 691876.91};
        double[] ycoords = {6176943.21, 6176944.65, 6176943.54, 6176944.98};
        double[] zcoords = {10.5, 12.3, 11.8, 13.2};  // Elevation in meters

        // Time array
        String[] timeStrs = {
                "2000-01-01 00:00:00",
                "2000-01-02 00:00:00",
                "2000-01-03 00:00:00",
                "2000-01-04 00:00:00"
        };

        int count = 4;

        System.out.println("\nInput Data:");
        System.out.println("┌─ COORDINATE ARRAYS ──────────────────────");
        for (int i = 0; i < count; i++) {
            System.out.printf("│ Point %d: x=%.2f, y=%.2f, z=%.2f @ %s\n",
                    i + 1, xcoords[i], ycoords[i], zcoords[i], timeStrs[i]);
        }
        System.out.println("└──────────────────────────────────────────");
        Runtime runtime = Runtime.getSystemRuntime();

        // Allocate native memory for coordinate arrays
        Pointer xPtr = Memory.allocate(runtime, Double.BYTES * count);
        Pointer yPtr = Memory.allocate(runtime, Double.BYTES * count);
        Pointer zPtr = Memory.allocate(runtime, Double.BYTES * count);

        // Copy Java arrays to native memory
        for (int i = 0; i < count; i++) {
            xPtr.putDouble(i * Double.BYTES, xcoords[i]);
            yPtr.putDouble(i * Double.BYTES, ycoords[i]);
            zPtr.putDouble(i * Double.BYTES, zcoords[i]);
        }

        // Allocate native memory for timestamps array
        Pointer timesPtr = Memory.allocate(runtime, Long.BYTES * count);

        // Convert and store timestamps
        for (int i = 0; i < count; i++) {
            long timestamp = parseTimestamp(timeStrs[i]);
            timesPtr.putLong(i * Long.BYTES, timestamp);
        }

        // Create 3D sequence from coordinate arrays
        Pointer seq = tpointseq_make_coords(
                xPtr,                               // Pointer to X coordinates
                yPtr,                               // Pointer to Y coordinates
                zPtr,                               // Pointer to Z coordinates (elevation)
                timesPtr,                           // Pointer to timestamps
                count,                              // Number of points
                5676,                               // SRID (UTM Zone 46N)
                false,                              // Not geodetic (projected)
                true,                               // Lower bound inclusive
                true,                               // Upper bound inclusive
                TInterpolation.LINEAR.getValue(),   // Interpolation: 3 = Linear
                true                                // Normalize
        );

        // Output as EWKT
        String ewkt = tspatial_as_ewkt(seq, 2);

        System.out.println("\n┌─ RESULT (3D SEQUENCE) ───────────────────");
        System.out.println("│ EWKT:");
        System.out.println("│ " + ewkt);
        System.out.println("│");
        System.out.println("│ Interpretation:");
        System.out.println("│ - SRID 5676 = UTM Zone 46N (projected)");
        System.out.println("│ - 3D points with elevation (Z coordinate)");
        System.out.println("│ - Linear interpolation between points");
        System.out.println("└──────────────────────────────────────────");
    }

    /**
     * SECTION 2: 2D Point Sequence (Projected Coordinates)
     * Creates a 2D temporal point sequence without elevation
     */
    private static void demonstrate2DProjected() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 2: 2D Point Sequence (Projected - UTM 46N)");
        System.out.println("=".repeat(60));

        // Coordinate arrays (same as 3D, but no Z)
        double[] xcoords = {691875.63, 691876.45, 691875.82, 691876.91};
        double[] ycoords = {6176943.21, 6176944.65, 6176943.54, 6176944.98};

        // Time array
        String[] timeStrs = {
                "2000-01-01 00:00:00",
                "2000-01-02 00:00:00",
                "2000-01-03 00:00:00",
                "2000-01-04 00:00:00"
        };

        int count = 4;

        System.out.println("\nInput Data:");
        System.out.println("┌─ COORDINATE ARRAYS ──────────────────────");
        for (int i = 0; i < count; i++) {
            System.out.printf("│ Point %d: x=%.2f, y=%.2f @ %s\n",
                    i + 1, xcoords[i], ycoords[i], timeStrs[i]);
        }
        System.out.println("└──────────────────────────────────────────");
        Runtime runtime = Runtime.getSystemRuntime();

        // Allocate native memory for coordinate arrays
        Pointer xPtr = Memory.allocate(runtime, Double.BYTES * count);
        Pointer yPtr = Memory.allocate(runtime, Double.BYTES * count);

        // Copy Java arrays to native memory
        for (int i = 0; i < count; i++) {
            xPtr.putDouble(i * Double.BYTES, xcoords[i]);
            yPtr.putDouble(i * Double.BYTES, ycoords[i]);
        }

        // Allocate native memory for timestamps array
        Pointer timesPtr = Memory.allocate(runtime, Long.BYTES * count);

        // Convert and store timestamps
        for (int i = 0; i < count; i++) {
            long timestamp = parseTimestamp(timeStrs[i]);
            timesPtr.putLong(i * Long.BYTES, timestamp);
        }

        // Create 2D sequence from coordinate arrays
        // Pass null for zPtr to create 2D points
        Pointer seq = tpointseq_make_coords(
                xPtr,                               // Pointer to X coordinates
                yPtr,                               // Pointer to Y coordinates
                null,                               // No Z coordinates (2D)
                timesPtr,                           // Pointer to timestamps
                count,                              // Number of points
                5676,                               // SRID (UTM Zone 46N)
                false,                              // Not geodetic (projected)
                true,                               // Lower bound inclusive
                true,                               // Upper bound inclusive
                TInterpolation.LINEAR.getValue(),   // Interpolation: 3 = Linear
                true                                // Normalize
        );

        // Output as EWKT
        String ewkt = tspatial_as_ewkt(seq, 2);

        System.out.println("\n┌─ RESULT (2D SEQUENCE) ───────────────────");
        System.out.println("│ EWKT:");
        System.out.println("│ " + ewkt);
        System.out.println("│");
        System.out.println("│ Interpretation:");
        System.out.println("│ - SRID 5676 = UTM Zone 46N (projected)");
        System.out.println("│ - 2D points (no elevation)");
        System.out.println("│ - Same coordinates as 3D, but Z omitted");
        System.out.println("└──────────────────────────────────────────");
    }

    /**
     * SECTION 3: 2D Point Sequence (Geographic Coordinates)
     * Creates a geographic temporal point sequence (latitude/longitude)
     */
    private static void demonstrate2DGeographic() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 3: 2D Point Sequence (Geographic - WGS84)");
        System.out.println("=".repeat(60));

        // Geographic coordinates (longitude, latitude)
        double[] xcoords = {2.349014, 2.349156, 2.349089, 2.349201};  // Longitude
        double[] ycoords = {48.852968, 48.853142, 48.853034, 48.853215};  // Latitude

        // Time array
        String[] timeStrs = {
                "2000-01-01 00:00:00",
                "2000-01-02 00:00:00",
                "2000-01-03 00:00:00",
                "2000-01-04 00:00:00"
        };

        int count = 4;

        System.out.println("\nInput Data (Paris, France):");
        System.out.println("┌─ COORDINATE ARRAYS ──────────────────────");
        for (int i = 0; i < count; i++) {
            System.out.printf("│ Point %d: lon=%.6f, lat=%.6f @ %s\n",
                    i + 1, xcoords[i], ycoords[i], timeStrs[i]);
        }
        System.out.println("└──────────────────────────────────────────");
        Runtime runtime = Runtime.getSystemRuntime();

        // Allocate native memory for coordinate arrays
        Pointer xPtr = Memory.allocate(runtime, Double.BYTES * count);
        Pointer yPtr = Memory.allocate(runtime, Double.BYTES * count);

        // Copy Java arrays to native memory
        for (int i = 0; i < count; i++) {
            xPtr.putDouble(i * Double.BYTES, xcoords[i]);
            yPtr.putDouble(i * Double.BYTES, ycoords[i]);
        }

        // Allocate native memory for timestamps array
        Pointer timesPtr = Memory.allocate(runtime, Long.BYTES * count);

        // Convert and store timestamps
        for (int i = 0; i < count; i++) {
            long timestamp = parseTimestamp(timeStrs[i]);
            timesPtr.putLong(i * Long.BYTES, timestamp);
        }

        // Create geographic sequence from coordinate arrays
        Pointer seq = tpointseq_make_coords(
                xPtr,                               // Longitude
                yPtr,                               // Latitude
                null,                               // No Z coordinates
                timesPtr,                           // Pointer to timestamps
                count,                              // Number of points
                4326,                               // SRID (WGS84 - standard GPS coordinates)
                true,                               // Geodetic (geographic)
                true,                               // Lower bound inclusive
                true,                               // Upper bound inclusive
                TInterpolation.LINEAR.getValue(),   // Interpolation: 3 = Linear
                true                                // Normalize
        );

        // Output as EWKT
        String ewkt = tspatial_as_ewkt(seq, 6);  // 6 decimals for geographic

        System.out.println("\n┌─ RESULT (GEOGRAPHIC SEQUENCE) ───────────");
        System.out.println("│ EWKT:");
        System.out.println("│ " + ewkt);
        System.out.println("│");
        System.out.println("│ Interpretation:");
        System.out.println("│ - SRID 4326 = WGS84 (standard GPS)");
        System.out.println("│ - Geographic coordinates (lon/lat)");
        System.out.println("│ - Geodetic = true (distances on sphere)");
        System.out.println("│ - Location: Near Eiffel Tower, Paris");
        System.out.println("└──────────────────────────────────────────");
    }

    /**
     * Explain the coordinate arrays approach
     */
    private static void explainCoordinateArrays() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║       COORDINATE ARRAYS CONSTRUCTION EXPLAINED           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Two Ways to Create Temporal Point Sequences:");
        System.out.println("───────────────────────────────────────────");
        System.out.println();
        System.out.println("┌─ METHOD 1: From Instants (Traditional) ──────────────────");
        System.out.println("│");
        System.out.println("│ TInstant inst1 = new TInstant(\"POINT(1 2)@2000-01-01\");");
        System.out.println("│ TInstant inst2 = new TInstant(\"POINT(2 3)@2000-01-02\");");
        System.out.println("│ TInstant inst3 = new TInstant(\"POINT(3 4)@2000-01-03\");");
        System.out.println("│");
        System.out.println("│ TSequence seq = new TSequence(instants, 3, ...);");
        System.out.println("│");
        System.out.println("│ Use when: Data comes as complete WKT strings");
        System.out.println("│");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("┌─ METHOD 2: From Coordinate Arrays (This Program) ────────");
        System.out.println("│");
        System.out.println("│ // Java arrays");
        System.out.println("│ double[] x = {1, 2, 3};");
        System.out.println("│ double[] y = {2, 3, 4};");
        System.out.println("│");
        System.out.println("│ // Allocate native memory and copy");
        System.out.println("│ Pointer xPtr = Memory.allocate(runtime, Double.BYTES * 3);");
        System.out.println("│ for (int i = 0; i < 3; i++)");
        System.out.println("│     xPtr.putDouble(i * Double.BYTES, x[i]);");
        System.out.println("│");
        System.out.println("│ // Same for y and timestamps...");
        System.out.println("│");
        System.out.println("│ Pointer seq = tpointseq_make_coords(xPtr, yPtr, null,");
        System.out.println("│                                     timesPtr, 3, ...);");
        System.out.println("│");
        System.out.println("│ Use when: Data comes as separate coordinate columns");
        System.out.println("│");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("When to Use Coordinate Arrays:");
        System.out.println("──────────────────────────────");
        System.out.println("✓ Sensor data: Separate streams for each dimension");
        System.out.println("✓ Database exports: Tabular format");
        System.out.println();
        System.out.println("Example CSV Data:");
        System.out.println("────────────────");
        System.out.println("timestamp,longitude,latitude,elevation");
        System.out.println("2000-01-01 00:00:00,2.349,48.853,10.5");
        System.out.println("2000-01-02 00:00:00,2.350,48.854,12.3");
        System.out.println("2000-01-03 00:00:00,2.351,48.855,11.8");
        System.out.println();
        System.out.println("→ Perfect for tpointseq_make_coords()!");
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args) {
        // Initialize MEOS
        meos_initialize();
        meos_initialize_timezone("UTC");

        try {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("Temporal Point Construction from Coordinate Arrays");
            System.out.println("=".repeat(60));

            // Explain the approach
            explainCoordinateArrays();

            // Run all demonstrations
            demonstrate3DProjected();
            demonstrate2DProjected();
            demonstrate2DGeographic();

            System.out.println("\n" + "=".repeat(60));
            System.out.println("DEMONSTRATION COMPLETED!");
            System.out.println("=".repeat(60));
            System.out.println();
            System.out.println("Key Takeaways:");
            System.out.println("──────────────");
            System.out.println("1. Use coordinate arrays when data is in tabular format");
            System.out.println("2. 3D points include elevation (z coordinate)");
            System.out.println("3. Geographic (4326) vs Projected (5676) coordinates");
            System.out.println("4. More efficient than creating individual instants");
            System.out.println();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Finalize MEOS
            meos_finalize();
        }
    }
}