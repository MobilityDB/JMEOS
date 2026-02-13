package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Pointer;

import java.io.*;
import java.util.*;

import static functions.functions.*;

/**
 * A simple program that reads synthetic trip data from the MobilityDB-BerlinMOD generator,
 * simplifies the trips using both Douglas-Peucker (DP) and Synchronized Euclidean Distance (SED),
 * and outputs for each trip the number of instants before and after simplification.
 *
 * Simplification algorithms:
 * - Douglas-Peucker (DP): Classic trajectory simplification
 * - Synchronized Euclidean Distance (SED): Also known as Top-Down Time Ratio simplification
 *
 * Both algorithms reduce the number of points while preserving trajectory shape.
 */
public class N08_BerlinMOD_Simplify {

    /* Epsilon distance used for simplification (meters) */
    static final double DELTA_DISTANCE = 2.0;

    /* Maximum number of trips */
    static final int MAX_NO_TRIPS = 256;

    static class TripRecord {
        int tripId;
        int vehId;
        int day;        // DateADT (stored as int)
        int seq;
        Pointer trip;
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        long startTime = System.currentTimeMillis();

        // Arrays to store trips and their simplified versions
        List<TripRecord> trips = new ArrayList<>();
        List<Pointer> tripsDP = new ArrayList<>();
        List<Pointer> tripsSED = new ArrayList<>();

        int noRecords = 0;
        int nulls = 0;

        try {
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

            System.out.println("Reading trips...");

            // Read all trips
            String line;
            while ((line = reader.readLine()) != null && trips.size() < MAX_NO_TRIPS) {
                String[] fields = line.split(",", 5);

                if (fields.length != 5) {
                    System.out.println("Record with missing values ignored");
                    nulls++;
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
                        nulls++;
                        continue;
                    }

                    trips.add(rec);
                    noRecords++;

                } catch (NumberFormatException e) {
                    System.out.println("Record with invalid values ignored");
                    nulls++;
                }
            }

            reader.close();

            System.out.printf("%n%d records read.%n%d incomplete records ignored.%n%n",
                    noRecords, nulls);

            // Simplify the trips
            System.out.println("Simplifying trips...");
            System.out.println("Delta distance: " + DELTA_DISTANCE + " meters");
            System.out.println();

            for (TripRecord trip : trips) {
                // Douglas-Peucker simplification (synchronize = false)
                Pointer tripDP = temporal_simplify_dp(trip.trip, DELTA_DISTANCE, false);
                tripsDP.add(tripDP);

                // Synchronized Euclidean Distance simplification (synchronize = true)
                Pointer tripSED = temporal_simplify_dp(trip.trip, DELTA_DISTANCE, true);
                tripsSED.add(tripSED);

                // Get counts
                int originalCount = temporal_num_instants(trip.trip);
                int dpCount = temporal_num_instants(tripDP);
                int sedCount = temporal_num_instants(tripSED);

                // Format date
                String dayStr = pg_date_out(trip.day);

                // Print results
                System.out.printf("Vehicle: %d, Date: %s, Seq: %d, " +
                                "No. of instants: %d, No. of instants DP: %d, No. of instants SED: %d%n",
                        trip.vehId, dayStr, trip.seq, originalCount, dpCount, sedCount);
            }

            // Print summary statistics
            System.out.println();
            System.out.println("Summary:");

            int totalOriginal = 0;
            int totalDP = 0;
            int totalSED = 0;

            for (int i = 0; i < trips.size(); i++) {
                totalOriginal += temporal_num_instants(trips.get(i).trip);
                totalDP += temporal_num_instants(tripsDP.get(i));
                totalSED += temporal_num_instants(tripsSED.get(i));
            }

            double dpReduction = 100.0 * (totalOriginal - totalDP) / totalOriginal;
            double sedReduction = 100.0 * (totalOriginal - totalSED) / totalOriginal;

            System.out.printf("Total original instants: %d%n", totalOriginal);
            System.out.printf("Total DP instants: %d (%.1f%% reduction)%n", totalDP, dpReduction);
            System.out.printf("Total SED instants: %d (%.1f%% reduction)%n", totalSED, sedReduction);

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