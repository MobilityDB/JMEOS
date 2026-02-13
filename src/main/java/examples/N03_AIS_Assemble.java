package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import types.temporal.TInterpolation;

import java.io.*;
import java.time.OffsetDateTime;
import java.util.*;

import static functions.functions.*;

/**
 * A simple program that reads AIS data from a CSV file, constructs
 * trips from these records, and outputs for each trip the MMSI, the number of
 * instants, and the distance traveled. The program also stores in a CSV file
 * the assembled trips.
 *
 * Please read the assumptions made about the input file in the file
 * `02_ais_read.c` in the same directory. Furthermore, the program assumes the
 * input file contains less than 50K observations for at most five ships.
 * Also, the program does not cope with erroneous inputs, such as two or more
 * observations for the same ship with equal timestamp values and supposes that
 * the observations are in increasing timestamp value.
 */
public class N03_AIS_Assemble {
    /* Number of instants in a batch for printing a marker */
    static final int NO_INSTS_BATCH = 10000;
    /* Maximum number of trips */
    static final int MAX_TRIPS = 5;

    static class AISRecord {
        OffsetDateTime T;
        long MMSI;
        double Latitude;
        double Longitude;
        double SOG;
    }

    static class TripRecord {
        long MMSI;   /* Identifier of the trip */
        int numInstants; /* Number of input instants */
        List<Pointer> tripInstants; /* List of instants for the trip */
        List<Pointer> sogInstants;  /* List of instants for the SOG */
        Pointer trip;  /* Trip constructed from the input instants */
        Pointer sog;   /* SOG constructed from the input instants */

        TripRecord() {
            tripInstants = new ArrayList<>();
            sogInstants = new ArrayList<>();
            numInstants = 0;
        }
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        // Get start time
        long startTime = System.currentTimeMillis();

        // Map to store trips by MMSI
        Map<Long, TripRecord> trips = new HashMap<>();
        int numShips = 0;

        int noRecords = 0;
        int noNulls = 0;

        try (BufferedReader reader = new BufferedReader(
                new FileReader("src/main/java/examples/data/ais_instants.csv"))) {

            // Read the first line with headers
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty file");
                meos_finalize();
                return;
            }

            System.out.printf("Reading the instants (one '*' marker every %d instants)%n",
                    NO_INSTS_BATCH);

            // Continue reading the file
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length != 5) {
                    System.out.println("Record with missing values ignored");
                    noNulls++;
                    continue;
                }

                try {
                    AISRecord rec = new AISRecord();
                    rec.T = pg_timestamptz_in(fields[0].trim(), -1);
                    rec.MMSI = Long.parseLong(fields[1].trim());
                    rec.Latitude = Double.parseDouble(fields[2].trim());
                    rec.Longitude = Double.parseDouble(fields[3].trim());
                    rec.SOG = Double.parseDouble(fields[4].trim());

                    noRecords++;
                    if (noRecords % NO_INSTS_BATCH == 0) {
                        System.out.print("*");
                        System.out.flush();
                    }

                    // Find or create the trip record for this MMSI
                    TripRecord trip = trips.get(rec.MMSI);
                    if (trip == null) {
                        if (numShips >= MAX_TRIPS) {
                            System.out.printf("The maximum number of ships in the input file is bigger than %d%n",
                                    MAX_TRIPS);
                            meos_finalize();
                            return;
                        }
                        trip = new TripRecord();
                        trip.MMSI = rec.MMSI;
                        trips.put(rec.MMSI, trip);
                        numShips++;
                    }

                    /*
                     * Create the instant and store it in the array of the corresponding ship.
                     * In the input file it is assumed that
                     * - The coordinates are given in the WGS84 geographic coordinate system
                     * - The timestamps are given in GMT time zone
                    */
                    Pointer gs = geogpoint_make2d(4326, rec.Longitude, rec.Latitude);
                    Pointer inst1 = tpointinst_make(gs, rec.T);
                    trip.tripInstants.add(inst1);

                    Pointer inst2 = tfloatinst_make(rec.SOG, rec.T);
                    trip.sogInstants.add(inst2);
                    trip.numInstants++;

                } catch (NumberFormatException e) {
                    System.out.println("Record with invalid values ignored");
                    noNulls++;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
            meos_finalize();
            return;
        }

        System.out.printf("%n%d records read.%n%d incomplete records ignored.%n",
                noRecords, noNulls);
        System.out.printf("%d trips read.%n", numShips);

        // Construct the trips
        Runtime runtime = Runtime.getSystemRuntime();

        for (TripRecord trip : trips.values()) {
            // Allocate memory for array of pointers for trip instants
            Pointer tripInstantsArray = Memory.allocate(runtime, trip.numInstants * Long.BYTES);
            for (int i = 0; i < trip.numInstants; i++) {
                tripInstantsArray.putPointer(i * Long.BYTES, trip.tripInstants.get(i));
            }

            // Create the trip sequence
            trip.trip = tsequence_make(tripInstantsArray, trip.numInstants,
                    true, true, TInterpolation.LINEAR.getValue(), true);

            // Allocate memory for array of pointers for SOG instants
            Pointer sogInstantsArray = Memory.allocate(runtime, trip.numInstants * Long.BYTES);
            for (int i = 0; i < trip.numInstants; i++) {
                sogInstantsArray.putPointer(i * Long.BYTES, trip.sogInstants.get(i));
            }

            // Create the SOG sequence
            trip.sog = tsequence_make(sogInstantsArray, trip.numInstants,
                    true, true, TInterpolation.LINEAR.getValue(), true);

            System.out.printf("MMSI: %d, Number of input instants: %d%n",
                    trip.MMSI, trip.numInstants);
            System.out.printf("  Trip -> Number of instants: %d, Distance travelled %f%n",
                    temporal_num_instants(trip.trip), tpoint_length(trip.trip));
            System.out.printf("  SOG -> Number of instants: %d, Time-weighted average %f%n",
                    temporal_num_instants(trip.sog), tnumber_twavg(trip.sog));
        }

        // Open the output file
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/main/java/examples/data/ais_trips_new_assemble.csv"))) {

            // Write the header line
            writer.write("mmsi,trip,sog\n");

            // Loop for each trip
            for (TripRecord trip : trips.values()) {
                // Write line in the CSV file
                String tripStr = tspatial_out(trip.trip, 6);
                String sogStr = tfloat_out(trip.sog, 6);
                writer.write(String.format("%d,%s,%s%n", trip.MMSI, tripStr, sogStr));
            }

        } catch (IOException e) {
            System.out.println("Error writing output file: " + e.getMessage());
        }

        // Calculate the elapsed time
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        System.out.printf("The program took %f seconds to execute%n", timeTaken);

        // Finalize MEOS
        meos_finalize();
    }
}