package examples;

import functions.error_handler;
import functions.error_handler_fn;

import java.io.*;
import java.sql.*;
import java.time.OffsetDateTime;

import static functions.functions.*;

/**
 * A simple program that reads AIS data from a CSV file, converts them
 * into temporal values, and stores them in MobilityDB using JDBC.
 *
 * SETUP INSTRUCTIONS:
 *
 * 1. Add PostgreSQL JDBC driver to pom.xml (if running in another project):
 *    <dependency>
 *        <groupId>org.postgresql</groupId>
 *        <artifactId>postgresql</artifactId>
 *        <version>42.7.1</version>
 *    </dependency>
 *
 * 2. Install PostgreSQL with PostGIS (Docker recommended):
 *    docker run --name postgres-mobilitydb \
 *      -e POSTGRES_PASSWORD=postgres \
 *      -p 5432:5432 \
 *      -d mobilitydb/mobilitydb
 *
 *      If you're on Linux, take a look at the line n°77 before running the program
 *
 * 3. Run the program - it will automatically create MobilityDB extensions and tables
 *      If you're running JMEOS in a docker container:
 *          docker exec -it container_name /bin/bash
 *          mvn exec:java -Dexec.mainClass="examples.AIS_Store"
 */
public class N04_AIS_Store {

    /* Number of instants in a batch for printing a marker */
    static final int NO_INSTS_BATCH = 10000;
    /* Number of inserts that are sent in bulk */
    static final int NO_BULK_INSERT = 20;

    static class AISRecord {
        OffsetDateTime T;
        long MMSI;
        double Latitude;
        double Longitude;
        double SOG;
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
        // Get start time
        long startTime = System.currentTimeMillis();

        // Database connection parameters
        /*
        The parameters at line 87 should work on Windows/Mac

        On Linux you might have to create a docker network first
            - docker network create mobilitydb-network
            - docker run --name postgres-mobilitydb \
                --network mobilitydb-network \
                -e POSTGRES_PASSWORD=postgres \
                -p 5432:5432 \
                -d mobilitydb/mobilitydb
            - use the following url instead:
                jdbc:postgresql://postgres-mobilitydb:5432/postgres?user=postgres&password=postgres
        */
        String jdbcUrl = "jdbc:postgresql://host.docker.internal:5432/postgres?user=postgres&password=postgres";

        Connection conn = null;
        int noRecords = 0;
        int noNulls = 0;

        try {
            /***************************************************************************
             * Section 1: Connection to the database
             ***************************************************************************/

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(jdbcUrl);

            // Set always-secure search path, so malicious users can't take control.
            executeSQL(conn, "SET search_path = ''");

            System.out.println("Connected successfully!");

            /***************************************************************************
             * Section 2: Open the input AIS file
             ***************************************************************************/

            error_handler_fn errorHandler = new error_handler();

            // Initialize MEOS
            meos_initialize_timezone("UTC");
            meos_initialize_error_handler(errorHandler);

            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/ais_instants.csv"));

            /***************************************************************************
             * Section 3: Read input file line by line and save each observation as a
             * temporal point in MobilityDB
             ***************************************************************************/

            // Create extensions if they don't exist
            System.out.println("Creating PostGIS and MobilityDB extensions if needed...");
            executeSQL(conn, "CREATE EXTENSION IF NOT EXISTS postgis");
            executeSQL(conn, "CREATE EXTENSION IF NOT EXISTS mobilitydb");
            System.out.println("Extensions ready!");

            // Create the table that will hold the data
            System.out.println("Creating the table in the database");
            executeSQL(conn, "DROP TABLE IF EXISTS public.AISInstants");
            executeSQL(conn, "CREATE TABLE public.AISInstants(" +
                    "MMSI integer, " +
                    "location public.tgeogpoint, " +
                    "SOG public.tfloat)");

            // Start a transaction block
            conn.setAutoCommit(false);

            // Read the first line with headers
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty file");
                reader.close();
                meos_finalize();
                return;
            }

            System.out.printf("Reading the instants (one '*' marker every %d instants)%n",
                    NO_INSTS_BATCH);

            // Prepare the INSERT statement for batch processing
            StringBuilder insertBuffer = new StringBuilder();
            int batchCount = 0;

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

                    // Create the INSERT command with the values read
                    if (batchCount == 0) {
                        insertBuffer.setLength(0);
                        insertBuffer.append("INSERT INTO public.AISInstants(MMSI, location, SOG) VALUES ");
                    }

                    String tOut = pg_timestamptz_out(rec.T);
                    insertBuffer.append(String.format("(%d, 'SRID=4326;Point(%f %f)@%s', '%f@%s'),",
                            rec.MMSI, rec.Longitude, rec.Latitude, tOut, rec.SOG, tOut));

                    batchCount++;

                    if (batchCount == NO_BULK_INSERT) {
                        // Replace the last comma with a semicolon
                        insertBuffer.setLength(insertBuffer.length() - 1);
                        insertBuffer.append(";");
                        executeSQL(conn, insertBuffer.toString());
                        batchCount = 0;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Record with invalid values ignored");
                    noNulls++;
                }
            }

            // Close the file
            reader.close();

            // Execute remaining inserts if any
            if (batchCount > 0) {
                insertBuffer.setLength(insertBuffer.length() - 1);
                insertBuffer.append(";");
                executeSQL(conn, insertBuffer.toString());
            }

            System.out.printf("%n%d records read.%n%d incomplete records ignored.%n",
                    noRecords, noNulls);

            // Query the count
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM public.AISInstants")) {
                if (rs.next()) {
                    System.out.printf("Query 'SELECT COUNT(*) FROM public.AISInstants' returned %d%n",
                            rs.getInt(1));
                }
            }

            // Commit the transaction
            conn.commit();

            System.out.println("Data successfully stored in MobilityDB!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the database connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Calculate the elapsed time
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        System.out.printf("The program took %f seconds to execute%n", timeTaken);

        // Finalize MEOS
        meos_finalize();
    }
}