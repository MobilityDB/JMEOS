package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Pointer;

import java.io.*;
import java.time.OffsetDateTime;
import java.util.*;

import static functions.functions.*;

/**
 * A simple program that reads from a CSV file synthetic trip data generated
 * by the MobilityDB-BerlinMOD generator, disassembles the trips into individual
 * observations, and writes them ordered by timestamp.
 *
 * This version works directly with MEOS C pointers to avoid Java wrapper issues.
 */
public class N05_BerlinMOD_Disassemble {

    static class TripRecord {
        int tripId;
        int vehId;
        int day;
        int seq;
        Pointer tripPointer;
    }

    static class InstantRecord implements Comparable<InstantRecord> {
        int tripId;
        int vehId;
        int day;
        int seq;
        String geom;
        OffsetDateTime timestamp;

        @Override
        public int compareTo(InstantRecord other) {
            return this.timestamp.compareTo(other.timestamp);
        }
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        long startTime = System.currentTimeMillis();

        List<TripRecord> trips = new ArrayList<>();
        List<InstantRecord> allInstants = new ArrayList<>();

        try {
            System.out.println("Reading trips from berlinmod_trips.csv...");

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

            // Read all trips
            String line;
            int recordsIn = 0;
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

                    // Parse HexWKB - keep pointer directly
                    String hexwkb = fields[4].trim();
                    rec.tripPointer = temporal_from_hexwkb(hexwkb);

                    if (rec.tripPointer == null) {
                        System.out.println("Failed to parse HexWKB for trip " + rec.tripId);
                        continue;
                    }

                    trips.add(rec);
                    recordsIn++;

                } catch (NumberFormatException e) {
                    System.out.println("Trip with invalid values: " + e.getMessage());
                }
            }

            reader.close();
            System.out.printf("%d trip records read%n", recordsIn);

            /***************************************************************************
             * Section 2: Disassemble trips
             ***************************************************************************/

            System.out.println("Disassembling trips into instants...");

            for (TripRecord trip : trips) {
                if (trip.tripPointer == null) {
                    System.out.println("Null pointer for trip " + trip.tripId);
                    continue;
                }

                // Get number of instants
                int numInstants = temporal_num_instants(trip.tripPointer);

                if (numInstants <= 0) {
                    System.out.println("Trip " + trip.tripId + " has " + numInstants + " instants");
                    continue;
                }

                // Extract each instant
                for (int i = 0; i < numInstants; i++) {
                    try {
                        // Get the i-th instant
                        Pointer instPtr = temporal_instant_n(trip.tripPointer, i + 1);

                        if (instPtr == null) {
                            System.out.println("Null instant " + i + " for trip " + trip.tripId);
                            continue;
                        }

                        InstantRecord instRec = new InstantRecord();
                        instRec.tripId = trip.tripId;
                        instRec.vehId = trip.vehId;
                        instRec.day = trip.day;
                        instRec.seq = trip.seq;

                        // Get instant as string: "SRID=3857;POINT(x y)@timestamp"
                        String instStr = tspatial_out(instPtr, 6);

                        // Split by @ to separate geometry from timestamp
                        int atIndex = instStr.lastIndexOf('@');
                        if (atIndex > 0) {
                            instRec.geom = instStr.substring(0, atIndex);
                            String tsStr = instStr.substring(atIndex + 1);
                            instRec.timestamp = pg_timestamptz_in(tsStr, -1);
                        } else {
                            System.out.println("Invalid instant format: " + instStr);
                            continue;
                        }

                        allInstants.add(instRec);

                    } catch (Exception e) {
                        System.out.println("Error processing instant " + i + " of trip " + trip.tripId + ": " + e.getMessage());
                    }
                }
            }

            System.out.printf("%d instant records extracted%n", allInstants.size());

            /***************************************************************************
             * Section 3: Sort by timestamp
             ***************************************************************************/

            System.out.println("Sorting instants by timestamp...");
            Collections.sort(allInstants);

            /***************************************************************************
             * Section 4: Write to output file
             ***************************************************************************/

            System.out.println("Writing instants to berlinmod_instants_disassemble.csv...");

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("src/main/java/examples/data/berlinmod_instants_disassemble.csv"));

            // Write header
            writer.write("tripid,vehid,day,seqno,geom,t\n");

            // Write all instants
            for (InstantRecord inst : allInstants) {
                String dateStr = pg_date_out(inst.day);
                String timeStr = pg_timestamptz_out(inst.timestamp);

                writer.write(String.format("%d,%d,%s,%d,%s,%s%n",
                        inst.tripId, inst.vehId, dateStr, inst.seq,
                        inst.geom, timeStr));
            }

            writer.close();

            System.out.printf("%d trip records read from file 'berlinmod_trips.csv'.%n", recordsIn);
            System.out.printf("%d observation records written in file 'berlinmod_instants_disassemble.csv'.%n",
                    allInstants.size());

        } catch (IOException e) {
            System.err.println("Error with files: " + e.getMessage());
            e.printStackTrace();
        }

        // Calculate elapsed time
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        System.out.printf("The program took %f seconds to execute%n", timeTaken);

        // Finalize MEOS
        meos_finalize();
    }
}