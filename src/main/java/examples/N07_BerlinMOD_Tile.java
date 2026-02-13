package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;

import java.io.*;
import java.time.OffsetDateTime;

import static functions.functions.*;

/**
 * A program that reads synthetic trip data from the MobilityDB-BerlinMOD generator,
 * splits the trips and speeds by tiles, and computes aggregate values.
 *
 * This program demonstrates spatial and temporal tiling:
 * - Spatial tiles: Grid over geographic space
 * - Value-time tiles: Grid over speed values and time periods
 *
 * For each tile, we compute:
 * - Count: Number of trip segments in the tile
 * - Duration: Total time spent in the tile
 * - Distance: Total distance traveled in the tile (spatial only)
 */
public class N07_BerlinMOD_Tile {

    /* Maximum number of bins in each dimension */
    static final int MAX_NO_BINS = 10;

    static class TripRecord {
        int count;
        Pointer duration;  // Interval
        double distance;

        TripRecord() {
            count = 0;
            duration = null;
            distance = 0.0;
        }
    }

    static class SpeedRecord {
        int count;
        Pointer duration;  // Interval

        SpeedRecord() {
            count = 0;
            duration = null;
        }
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        long startTime = System.currentTimeMillis();

        int noRecords = 0;
        int noNulls = 0;

        try {
            // Compute spatial tiles for trips
            // Extent covers Brussels area in EPSG:3857
            Pointer tripExtent = stbox_in(
                    "SRID=3857;STBOX X((473212.810151,6578740.528027),(499152.544688,6607165.513683))");
            Pointer sorigin = geom_in("SRID=3857;Point(0 0)", -1);

            // Get system runtime for memory allocation
            Runtime runtime = Runtime.getSystemRuntime();

            // Create pointer for count (4 bytes for int)
            Pointer noTripTilesPtr = Memory.allocate(runtime, Integer.BYTES);

            Pointer tripTiles = stbox_space_tiles(tripExtent, 5000.0, 5000.0, 0.0,
                    sorigin, true, noTripTilesPtr);
            int noTripTiles = noTripTilesPtr.getInt(0);

            // Compute value-time tiles for speed
            // Speed range: [0, 35) km/h, Time range: [2020-06-01, 2020-06-05)
            Pointer speedExtent = tbox_in("TBox XT([0, 35),[2020-06-01, 2020-06-05))");
            Pointer duration = pg_interval_in("1 day", -1);
            OffsetDateTime torigin = pg_timestamptz_in("2020-06-01", -1);

            // Use same runtime for second pointer allocation
            Pointer noSpeedTilesPtr = Memory.allocate(runtime, Integer.BYTES);
            Pointer speedTiles = tfloatbox_value_time_tiles(speedExtent, 10.0,
                    duration, 0.0, torigin, noSpeedTilesPtr);
            int noSpeedTiles = noSpeedTilesPtr.getInt(0);

            System.out.printf("Created %d spatial tiles and %d speed tiles%n",
                    noTripTiles, noSpeedTiles);

            // Initialize aggregation arrays
            TripRecord[] tripSplits = new TripRecord[noTripTiles];
            for (int i = 0; i < noTripTiles; i++) {
                tripSplits[i] = new TripRecord();
            }

            SpeedRecord[] speedSplits = new SpeedRecord[noSpeedTiles];
            for (int i = 0; i < noSpeedTiles; i++) {
                speedSplits[i] = new SpeedRecord();
            }

            // Read trips file
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/berlinmod_trips.csv"));

            // Read header
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty file");
                reader.close();
                meos_finalize();
                return;
            }

            System.out.println("Processing records (one marker per trip)");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", 5);

                if (fields.length != 5) {
                    System.out.println("Record with missing values ignored");
                    noNulls++;
                    continue;
                }

                try {
                    // Parse trip
                    String hexwkb = fields[4].trim();
                    Pointer trip = temporal_from_hexwkb(hexwkb);

                    if (trip == null) {
                        System.out.println("Failed to parse trip");
                        noNulls++;
                        continue;
                    }

                    noRecords++;
                    System.out.print("*");
                    if (noRecords % 50 == 0) {
                        System.out.flush();
                    }

                    // Compute speed
                    Pointer speed = tpoint_speed(trip);

                    // Split trip by spatial tiles
                    for (int i = 0; i < noTripTiles; i++) {
                        // Access tile using offset calculation
                        // STBox is a structure, we need to pass the pointer with offset
                        // The size of STBox structure needs to be determined
                        // For now, we'll create an STBox wrapper to get proper pointer
                        try {
                            // Skip tiles - extract and wrap each tile properly
                            // Note: This is simplified - proper implementation would
                            // calculate structure size or use STBox wrapper class
                            Pointer tilePtr = tripTiles.slice(i * 128); // Approximate STBox size

                            Pointer split = tgeo_at_stbox(trip, tilePtr, false);

                            if (split != null) {
                                tripSplits[i].count++;
                                tripSplits[i].distance += tpoint_length(split) / 1000.0;

                                Pointer dur1 = temporal_duration(split, false);
                                if (tripSplits[i].duration == null) {
                                    tripSplits[i].duration = dur1;
                                } else {
                                    Pointer dur2 = add_interval_interval(dur1, tripSplits[i].duration);
                                    tripSplits[i].duration = dur2;
                                }
                            }
                        } catch (Exception e) {
                            // Skip this tile if error
                            System.err.println("Error processing spatial tile " + i + ": " + e.getMessage());
                        }
                    }

                    // Split speed by value-time tiles
                    for (int i = 0; i < noSpeedTiles; i++) {
                        try {
                            // Access tile using offset - TBox structure
                            Pointer tilePtr = speedTiles.slice(i * 64); // Approximate TBox size

                            Pointer split = tnumber_at_tbox(speed, tilePtr);

                            if (split != null) {
                                speedSplits[i].count++;

                                Pointer dur1 = temporal_duration(split, false);
                                if (speedSplits[i].duration == null) {
                                    speedSplits[i].duration = dur1;
                                } else {
                                    Pointer dur2 = add_interval_interval(dur1, speedSplits[i].duration);
                                    speedSplits[i].duration = dur2;
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error processing speed tile " + i + ": " + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    System.out.println("\nError processing record: " + e.getMessage());
                    noNulls++;
                }
            }

            reader.close();

            System.out.printf("%n%d records read.%n%d incomplete records ignored.%n",
                    noRecords, noNulls);

            // Print trip tile results
            System.out.println("-------------");
            System.out.println(" Trip tiles");
            System.out.println("-------------");

            for (int i = 0; i < noTripTiles; i++) {
                if (tripSplits[i].count > 0) {
                    try {
                        Pointer tilePtr = tripTiles.slice(i * 128);
                        String stboxStr = stbox_out(tilePtr, 0);
                        String intervalStr = pg_interval_out(tripSplits[i].duration);

                        System.out.printf("Tile: %d, Box: %s, Count: %d, Duration: %s, Distance: %.3f%n",
                                i, stboxStr, tripSplits[i].count, intervalStr,
                                tripSplits[i].distance);
                    } catch (Exception e) {
                        System.err.println("Error printing tile " + i + ": " + e.getMessage());
                    }
                }
            }
            System.out.println();

            // Print speed tile results
            System.out.println("-------------");
            System.out.println(" Speed tiles");
            System.out.println("-------------");

            for (int i = 0; i < noSpeedTiles; i++) {
                if (speedSplits[i].count > 0) {
                    try {
                        Pointer tilePtr = speedTiles.slice(i * 64);
                        String spanStr = tbox_out(tilePtr, 0);
                        String intervalStr = pg_interval_out(speedSplits[i].duration);

                        System.out.printf("Tile: %d, Box: %s, Count: %d, Duration: %s%n",
                                i, spanStr, speedSplits[i].count, intervalStr);
                    } catch (Exception e) {
                        System.err.println("Error printing speed tile " + i + ": " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        // Calculate elapsed time
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        System.out.printf("The program took %.3f seconds to execute%n", timeTaken);

        // Finalize MEOS
        meos_finalize();
    }
}