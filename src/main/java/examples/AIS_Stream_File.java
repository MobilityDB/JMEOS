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
 * A simple program that reads AIS data from a CSV file, accumulates the
 * observations in main memory and writes the temporal values to an output file
 * when they reach a given number of instants in order to free the memory and
 * ingest the newest observations.
 *
 * This program is similar to AIS_Stream_DB but writes to a file instead of a database.
 * The accumulated temporal values are appended to the output file regularly.
 */
public class AIS_Stream_File {

    /* Number of instants to send in batch to the file */
    static final int NO_INSTS_BATCH = 1000;
    /* Number of instants to keep when restarting a sequence */
    static final int NO_INSTS_KEEP = 2;
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
        long MMSI;                      /* Identifier of the trip */
        List<Pointer> instants;         /* Accumulated instants */

        TripRecord() {
            instants = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        int noRecords = 0;
        int noNulls = 0;
        int noWrites = 0;

        // Map to store trips by MMSI
        Map<Long, TripRecord> trips = new HashMap<>();
        int noShips = 0;

        BufferedWriter fileOut = null;
        BufferedReader fileIn = null;

        try {
            /***************************************************************************
             * Section 1: Open the output file
             ***************************************************************************/

            System.out.println("Creating output file...");
            fileOut = new BufferedWriter(
                    new FileWriter("src/main/java/examples/data/ais_trips_new_stream.csv"));

            /***************************************************************************
             * Section 2: Open the input AIS file
             ***************************************************************************/

            System.out.println("Opening input file...");
            fileIn = new BufferedReader(
                    new FileReader("src/main/java/examples/data/ais_instants.csv"));

            /***************************************************************************
             * Section 3: Read input file and stream to output file
             ***************************************************************************/

            System.out.printf("Accumulating %d instants before sending them to the output file%n",
                    NO_INSTS_BATCH);
            System.out.println("(one '*' marker every output file update)");

            // Read the first line with headers
            String headerLine = fileIn.readLine();
            if (headerLine == null) {
                System.out.println("Empty file");
                return;
            }

            // Continue reading the file
            String line;
            while ((line = fileIn.readLine()) != null) {
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

                    // Find or create trip record
                    TripRecord trip = trips.get(rec.MMSI);
                    if (trip == null) {
                        if (noShips >= MAX_TRIPS) {
                            System.out.printf("Maximum number of ships exceeded: %d%n", MAX_TRIPS);
                            return;
                        }
                        trip = new TripRecord();
                        trip.MMSI = rec.MMSI;
                        trips.put(rec.MMSI, trip);
                        noShips++;
                    }

                    // Write to file when batch size is reached
                    if (trip.instants.size() >= NO_INSTS_BATCH) {
                        // Build sequence from accumulated instants
                        Runtime runtime = Runtime.getSystemRuntime();
                        Pointer array = Memory.allocate(runtime, trip.instants.size() * Long.BYTES);
                        for (int i = 0; i < trip.instants.size(); i++) {
                            array.putPointer(i * Long.BYTES, trip.instants.get(i));
                        }

                        Pointer seqPtr = tsequence_make(array, trip.instants.size(),
                                true, true, TInterpolation.LINEAR.getValue(), true);

                        // Write to output file
                        String tempOut = tspatial_out(seqPtr, 15);
                        fileOut.write(String.format("%d, %s%n", trip.MMSI, tempOut));
                        fileOut.flush();

                        noWrites++;
                        System.out.print("*");
                        System.out.flush();

                        // Keep only the last instants for continuity
                        List<Pointer> kept = new ArrayList<>();
                        int startIdx = Math.max(0, trip.instants.size() - NO_INSTS_KEEP);
                        for (int i = startIdx; i < trip.instants.size(); i++) {
                            kept.add(trip.instants.get(i));
                        }
                        trip.instants = kept;
                    }

                    // Add the new observation to the list
                    Pointer gs = geogpoint_make2d(4326, rec.Longitude, rec.Latitude);
                    Pointer instPtr = tpointinst_make(gs, rec.T);
                    trip.instants.add(instPtr);

                } catch (NumberFormatException e) {
                    System.out.println("Record with invalid values ignored");
                    noNulls++;
                }
            }

            System.out.printf("%n%d records read%n", noRecords);
            System.out.printf("%d incomplete records ignored%n", noNulls);
            System.out.printf("%d writes to the output file%n", noWrites);

        } catch (IOException e) {
            System.err.println("Error with files: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close files
            try {
                if (fileIn != null) fileIn.close();
                if (fileOut != null) fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Output file created: src/main/java/examples/data/ais_trips_new_stream.csv");

        // Finalize MEOS
        meos_finalize();
    }
}