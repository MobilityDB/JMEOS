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
 * A simple program that reads synthetic trip data from the MobilityDB-BerlinMOD generator
 * and performs temporal count aggregation.
 *
 * This program computes:
 * 1. Spatial-temporal extent: The bounding box of all trips
 * 2. Temporal count: How many vehicles are active at each hour
 *
 * The temporal count is computed by:
 * - Extracting the time periods when each trip is active
 * - Grouping by hour (using tprecision)
 * - Counting how many trips overlap each hour
 */
public class N09_BerlinMOD_Aggregate {

    static class TripRecord {
        int tripId;
        int vehId;
        int day;  // DateADT
        int seq;
        Pointer trip;
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        long startTime = System.currentTimeMillis();

        int noRecords = 0;
        

        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/berlinmod_trips.csv"));

            // Read header
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Error opening input file");
                reader.close();
                meos_finalize();
                return;
            }

            // Variables for aggregation
            Pointer extent = null;  // STBox
            Pointer state = null;   // SkipList for tcount aggregation

            // Time granularity: 1 hour
            Pointer interval = pg_interval_in("1 hour", -1);
            OffsetDateTime origin = pg_timestamptz_in("2020-06-01", -1);

            System.out.println("Processing trips...");

            // Read and aggregate trips
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", 5);

                if (fields.length != 5) {
                    System.out.println("Trip record with missing values");
                    continue;
                }

                try {
                    TripRecord rec = new TripRecord();
                    rec.tripId = Integer.parseInt(fields[0].trim());
                    rec.vehId = Integer.parseInt(fields[1].trim());
                    rec.day = pg_date_in(fields[2].trim());
                    rec.seq = Integer.parseInt(fields[3].trim());

                    // Parse HexWKB trip
                    String hexwkb = fields[4].trim();
                    rec.trip = temporal_from_hexwkb(hexwkb);

                    if (rec.trip == null) {
                        System.out.println("Failed to parse trip " + rec.tripId);
                        continue;
                    }

                    noRecords++;

                    // Add to spatial-temporal extent
                    extent = tspatial_extent_transfn(extent, rec.trip);

                    // Get the time of the trip
                    Pointer tempTime = temporal_time(rec.trip);

                    // Apply time precision (group by hour)
                    Pointer ps = tstzspanset_tprecision(tempTime, interval, origin);

                    // Aggregate the temporal count
                    state = tstzspanset_tcount_transfn(state, ps);

                } catch (NumberFormatException e) {
                    System.out.println("Record with invalid values ignored");
                }
            }

            reader.close();

            System.out.printf("%n%d trip records read%n%n", noRecords);

            // Print extent
            System.out.println("Extent");
            System.out.println("------");
            System.out.println();

            if (extent != null) {
                String extentStr = stbox_out(extent, 6);
                System.out.println(extentStr);
                System.out.println();
            }

            // Print temporal count
            System.out.println("Temporal count");
            System.out.println("--------------");
            System.out.println();

            if (state != null) {
                // Finalize aggregation
                Pointer tcount = temporal_tagg_finalfn(state);

                if (tcount != null) {
                    // Extract sequences
                    Runtime runtime = Runtime.getSystemRuntime();
                    Pointer countPtr = Memory.allocate(runtime, Integer.BYTES);

                    Pointer tcountSeqs = temporal_sequences(tcount, countPtr);
                    int seqCount = countPtr.getInt(0);

                    // Print each sequence
                    for (int i = 0; i < seqCount; i++) {
                        Pointer seqPtr = tcountSeqs.getPointer(i * Long.BYTES);
                        String seqStr = tint_out(seqPtr);
                        System.out.println(seqStr);
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
        System.out.printf("%nThe program took %.3f seconds to execute%n", timeTaken);

        // Finalize MEOS
        meos_finalize();
    }
}