package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import types.temporal.TInterpolation;

import java.io.*;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.*;

import static functions.functions.*;

/**
 * A simple program that reads AIS data from a CSV file, accumulates the
 * observations in main memory and sends the temporal values to a MobilityDB
 * database when they reach a given number of instants in order to free
 * the memory and ingest the newest observations.
 *
 * This program illustrates streaming of temporal data to MobilityDB by:
 * 1. Accumulating instants in memory (up to NO_INSTS_BATCH per ship)
 * 2. Building a temporal sequence from accumulated instants
 * 3. Sending to database and keeping last instants for continuity
 *
 * Note: Unlike the C version which uses MEOS expandable sequences API,
 * this Java version uses a simpler list-based accumulation since the
 * expandable API is not exposed in the Java bindings.
 *
 * SETUP: Same as AIS_Store.java - use mobilitydb/mobilitydb Docker image
 */
public class AIS_Stream_DB {

    /* Number of instants to send in batch to the database */
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

    /**
     * Execute a SQL command
     */
    private static void executeSQL(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        // Get start time
        long startTime = System.currentTimeMillis();

        // Database connection
        String jdbcUrl = "jdbc:postgresql://host.docker.internal:5432/postgres?user=postgres&password=postgres";

        Connection conn = null;
        int noRecords = 0;
        int noNulls = 0;
        int noWrites = 0;

        // Map to store trips by MMSI
        Map<Long, TripRecord> trips = new HashMap<>();
        int noShips = 0;

        try {
            /***************************************************************************
             * Section 1: Connection to the database
             ***************************************************************************/

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(jdbcUrl);
            executeSQL(conn, "SET search_path = ''");
            System.out.println("Connected successfully!");

            // Create extensions if needed
            executeSQL(conn, "CREATE EXTENSION IF NOT EXISTS postgis");
            executeSQL(conn, "CREATE EXTENSION IF NOT EXISTS mobilitydb");

            // Create the table
            System.out.println("Creating the table AISTrips in the database");
            executeSQL(conn, "DROP TABLE IF EXISTS public.AISTrips");
            executeSQL(conn, "CREATE TABLE public.AISTrips(" +
                    "MMSI integer PRIMARY KEY, trip public.tgeogpoint)");

            /***************************************************************************
             * Section 2: Open the input AIS file
             ***************************************************************************/

            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/ais_instants.csv"));

            /***************************************************************************
             * Section 3: Read input file and stream to database
             ***************************************************************************/

            System.out.printf("Accumulating %d instants before sending them to the database%n",
                    NO_INSTS_BATCH);
            System.out.println("(one '*' marker every database update)");

            // Read the first line with headers
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty file");
                reader.close();
                meos_finalize();
                return;
            }

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

                    // Find or create trip record
                    TripRecord trip = trips.get(rec.MMSI);
                    if (trip == null) {
                        if (noShips >= MAX_TRIPS) {
                            System.out.printf("Maximum number of ships exceeded: %d%n", MAX_TRIPS);
                            reader.close();
                            meos_finalize();
                            return;
                        }
                        trip = new TripRecord();
                        trip.MMSI = rec.MMSI;
                        trips.put(rec.MMSI, trip);
                        noShips++;
                    }

                    // Send to database when batch size is reached
                    if (trip.instants.size() >= NO_INSTS_BATCH) {
                        // Build sequence from accumulated instants
                        Runtime runtime = Runtime.getSystemRuntime();
                        Pointer array = Memory.allocate(runtime, trip.instants.size() * Long.BYTES);
                        for (int i = 0; i < trip.instants.size(); i++) {
                            array.putPointer(i * Long.BYTES, trip.instants.get(i));
                        }

                        Pointer seqPtr = tsequence_make(array, trip.instants.size(),
                                true, true, TInterpolation.LINEAR.getValue(), true);

                        // Construct and execute query
                        String tempOut = tspatial_out(seqPtr, 15);
                        String query = String.format(
                                "INSERT INTO public.AISTrips(MMSI, trip) " +
                                        "VALUES (%d, '%s') ON CONFLICT (MMSI) DO " +
                                        "UPDATE SET trip = public.update(AISTrips.trip, EXCLUDED.trip, true)",
                                trip.MMSI, tempOut);

                        executeSQL(conn, query);
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

            // Close the file
            reader.close();

            System.out.printf("%n%d records read%n", noRecords);
            System.out.printf("%d incomplete records ignored%n", noNulls);
            System.out.printf("%d writes to the database%n", noWrites);

            // Query the results
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT MMSI, public.numInstants(trip) FROM public.AISTrips ORDER BY MMSI")) {

                System.out.println("\nResult of the query:\n");
                System.out.println("   mmsi    | numinstants");
                System.out.println("-----------+-------------");

                while (rs.next()) {
                    System.out.printf(" %9d | %11d%n",
                            rs.getInt(1), rs.getInt(2));
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Calculate elapsed time
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        System.out.printf("The program took %f seconds to execute%n", timeTaken);

        // Finalize MEOS
        meos_finalize();
    }
}