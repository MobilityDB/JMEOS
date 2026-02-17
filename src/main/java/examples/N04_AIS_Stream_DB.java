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
 * observations using MEOS expandable sequences, and sends the temporal values
 * to a MobilityDB database when they reach a given number of instants.
 *
 * This program illustrates streaming of temporal data to MobilityDB using
 * the MEOS expandable sequences API:
 * 1. Create expandable sequence with first instant
 * 2. Append subsequent instants using temporal_append_tinstant()
 * 3. When batch size reached: send to database and restart sequence
 *
 * Key difference from AIS_Assemble:
 * - AIS_Assemble: Accumulate ALL data → Build once at end
 * - AIS_Stream_DB: Build incrementally → Send batches → Free memory
 *
 * This version is FAITHFUL to the C implementation using expandable sequences,
 * unlike earlier versions that used ArrayList accumulation.
 *
 * SETUP: Use mobilitydb/mobilitydb Docker image
 * docker run --name postgres-mobilitydb -e POSTGRES_PASSWORD=postgres \
 *   -p 5432:5432 -d mobilitydb/mobilitydb
 */
public class N04_AIS_Stream_DB {

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
        long MMSI;              /* Identifier of the trip */
        Pointer trip;           /* Expandable sequence of observations */

        TripRecord(long mmsi) {
            this.MMSI = mmsi;
            this.trip = null;
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

    /**
     * Get the number of instants in a temporal sequence.
     * Simulates accessing seq->count in C.
     */
    private static int getSequenceCount(Pointer seq) {
        return temporal_num_instants(seq);
    }

    /**
     * Restart sequence by keeping only the last NO_INSTS_KEEP instants.
     * Simulates tsequence_restart() from C.
     */
    private static Pointer restartSequence(Pointer seq, int keepCount) {
        int totalCount = getSequenceCount(seq);

        if (totalCount <= keepCount) {
            return seq;  // Nothing to restart
        }

        // Extract last keepCount instants
        Runtime runtime = Runtime.getSystemRuntime();
        Pointer[] keptInsts = new Pointer[keepCount];

        for (int i = 0; i < keepCount; i++) {
            int idx = totalCount - keepCount + i + 1;  // 1-indexed!
            keptInsts[i] = temporal_instant_n(seq, idx);
        }

        // Create new sequence with kept instants
        Pointer instArray = Memory.allocate(runtime, keepCount * Long.BYTES);
        for (int i = 0; i < keepCount; i++) {
            instArray.putPointer(i * Long.BYTES, keptInsts[i]);
        }

        return tsequence_make(instArray, keepCount,
                true, true, TInterpolation.LINEAR.getValue(), true);
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
        Map<Long, TripRecord> trips = new LinkedHashMap<>();
        int noShips = 0;

        Runtime runtime = Runtime.getSystemRuntime();

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
             * Section 3: Read input file and stream to database using expandable sequences
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
                        trip = new TripRecord(rec.MMSI);
                        trips.put(rec.MMSI, trip);
                        noShips++;
                    }

                    // Send to database when batch size is reached
                    if (trip.trip != null && getSequenceCount(trip.trip) == NO_INSTS_BATCH) {
                        // Construct and execute query
                        String tempOut = tspatial_out(trip.trip, 15);
                        String query = String.format(
                                "INSERT INTO public.AISTrips(MMSI, trip) " +
                                        "VALUES (%d, '%s') ON CONFLICT (MMSI) DO " +
                                        "UPDATE SET trip = public.update(AISTrips.trip, EXCLUDED.trip, true)",
                                trip.MMSI, tempOut);

                        executeSQL(conn, query);
                        noWrites++;
                        System.out.print("*");
                        System.out.flush();

                        // Restart the sequence by only keeping the last instants
                        trip.trip = restartSequence(trip.trip, NO_INSTS_KEEP);
                    }

                    // Append the new observation to the expandable sequence
                    Pointer gs = geogpoint_make2d(4326, rec.Longitude, rec.Latitude);
                    Pointer inst = tpointinst_make(gs, rec.T);

                    if (trip.trip == null) {
                        // Create initial expandable sequence with first instant
                        Pointer instArray = Memory.allocate(runtime, Long.BYTES);
                        instArray.putPointer(0, inst);
                        trip.trip = tsequence_make(instArray, 1,
                                true, true, TInterpolation.LINEAR.getValue(), true);
                    } else {
                        // Append instant to existing sequence (expandable!)
                        Pointer newSeq = temporal_append_tinstant(trip.trip, inst, TInterpolation.LINEAR.getValue(),
                                0.0, null, true);

                        if (newSeq == null) {
                            System.err.printf("\nError appending instant for MMSI %d\n", trip.MMSI);
                            continue;
                        }

                        trip.trip = newSeq;
                    }

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
        System.out.printf("The program took %.3f seconds to execute%n", timeTaken);

        // Finalize MEOS
        meos_finalize();
    }
}