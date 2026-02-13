package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Pointer;

import java.io.*;

import static functions.functions.*;

/**
 * A program that reads synthetic trip data from the MobilityDB-BerlinMOD generator
 * and generates statistics about the Brussels communes (municipalities) traversed
 * by the trips.
 *
 * Input files:
 * - brussels_communes.csv: Data from the 19 communes composing Brussels
 * - brussels_region.csv: Geometry of the Brussels region (union of communes)
 * - berlinmod_trips.csv: 154 trips from 5 cars during 11 days
 *
 * Output: Distance matrix showing kilometers traveled per vehicle per commune
 */
public class N06_BerlinMOD_Clip {

    /* Number of vehicles and communes */
    static final int NO_VEHICLES = 5;
    static final int NO_COMMUNES = 19;

    static class CommuneRecord {
        int id;
        String name;
        int population;
        Pointer geom;
    }

    static class RegionRecord {
        String name;
        Pointer geom;
    }

    static class TripRecord {
        int tripId;
        int vehId;
        int seq;
        Pointer trip;
    }

    /* Arrays to compute the results */
    static CommuneRecord[] communes = new CommuneRecord[NO_COMMUNES];
    // distance[vehicle][commune]: [0]=total, [1-19]=communes, [20]=inside Brussels, [21]=outside Brussels
    static double[][] distance = new double[NO_VEHICLES + 1][NO_COMMUNES + 3];

    static RegionRecord brusselsRegion = new RegionRecord();

    /**
     * Read communes from CSV file
     */
    private static boolean readCommunes() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/brussels_communes.csv"));

            // Read header
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty communes file");
                reader.close();
                return false;
            }

            int noRecords = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", 4);

                if (fields.length != 4) {
                    System.out.println("Commune record with missing values");
                    continue;
                }

                CommuneRecord commune = new CommuneRecord();
                commune.id = Integer.parseInt(fields[0].trim());
                commune.name = fields[1].trim();
                commune.population = Integer.parseInt(fields[2].trim());

                // Parse geometry (WKT or EWKT format)
                commune.geom = geom_in(fields[3].trim(), -1);

                if (commune.geom == null) {
                    System.out.println("Failed to parse geometry for commune " + commune.name);
                    continue;
                }

                communes[noRecords++] = commune;
            }

            reader.close();
            System.out.printf("%d commune records read%n", noRecords);
            return true;

        } catch (IOException e) {
            System.err.println("Error reading brussels_communes.csv: " + e.getMessage());
            return false;
        }
    }

    /**
     * Read Brussels region from CSV file
     */
    private static boolean readBrusselsRegion() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/brussels_region.csv"));

            // Read header
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty region file");
                reader.close();
                return false;
            }

            // Read region record
            String line = reader.readLine();
            if (line == null) {
                System.out.println("No region data");
                reader.close();
                return false;
            }

            String[] fields = line.split(",", 2);
            if (fields.length != 2) {
                System.out.println("Region record with missing values");
                reader.close();
                return false;
            }

            brusselsRegion.name = fields[0].trim();
            brusselsRegion.geom = geom_in(fields[1].trim(), -1);

            if (brusselsRegion.geom == null) {
                System.out.println("Failed to parse Brussels region geometry");
                reader.close();
                return false;
            }

            reader.close();
            System.out.println("Brussels region record read");
            return true;

        } catch (IOException e) {
            System.err.println("Error reading brussels_region.csv: " + e.getMessage());
            return false;
        }
    }

    /**
     * Print distance matrix in tabular form
     */
    private static void printMatrix(boolean allCommunes) {
        StringBuilder buf = new StringBuilder();

        // Print table header
        buf.append("\n                --");
        for (int j = 1; j < NO_COMMUNES + 2; j++) {
            if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                buf.append("---------");
            }
        }
        buf.append("\n                | Communes");
        buf.append("\n    --------------");
        for (int j = 1; j < NO_COMMUNES + 2; j++) {
            if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                buf.append("---------");
            }
        }
        buf.append("\nVeh | Distance | ");
        for (int j = 1; j <= NO_COMMUNES; j++) {
            if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                buf.append(String.format("   %2d   ", j));
            }
        }
        buf.append("|  Inside | Outside\n");
        for (int j = 0; j < NO_COMMUNES + 3; j++) {
            if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                buf.append("---------");
            }
        }
        buf.append("\n");

        // Print for each vehicle
        for (int i = 0; i < NO_VEHICLES; i++) {
            buf.append(String.format(" %2d | %8.3f |", i + 1, distance[i][0]));
            for (int j = 1; j <= NO_COMMUNES; j++) {
                if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                    buf.append(String.format(" %7.3f", distance[i][j]));
                }
            }
            for (int j = NO_COMMUNES + 1; j < NO_COMMUNES + 3; j++) {
                buf.append(String.format(" | %7.3f", distance[i][j]));
            }
            buf.append("\n");
        }

        // Print total row
        for (int j = 0; j < NO_COMMUNES + 3; j++) {
            if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                buf.append("---------");
            }
        }
        buf.append(String.format("\n    | %8.3f |", distance[NO_VEHICLES][0]));
        for (int j = 1; j <= NO_COMMUNES; j++) {
            if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                buf.append(String.format(" %7.3f", distance[NO_VEHICLES][j]));
            }
        }
        for (int j = NO_COMMUNES + 1; j < NO_COMMUNES + 3; j++) {
            if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                buf.append(String.format(" | %7.3f", distance[NO_VEHICLES][j]));
            }
        }
        buf.append("\n");
        for (int j = 0; j < NO_COMMUNES + 3; j++) {
            if (allCommunes || distance[NO_VEHICLES][j] != 0) {
                buf.append("---------");
            }
        }
        buf.append("\n\n");

        System.out.print(buf.toString());
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        long startTime = System.currentTimeMillis();

        // Read communes
        if (!readCommunes()) {
            meos_finalize();
            return;
        }

        // Read Brussels region
        if (!readBrusselsRegion()) {
            meos_finalize();
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/berlinmod_trips.csv"));

            // Read header
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty trips file");
                reader.close();
                meos_finalize();
                return;
            }

            System.out.println("Processing trip records (one '*' marker per trip)");

            int noRecords = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", 5);

                if (fields.length != 5) {
                    System.out.println("Trip record with missing values");
                    continue;
                }

                try {
                    TripRecord tripRec = new TripRecord();
                    tripRec.tripId = Integer.parseInt(fields[0].trim());
                    tripRec.vehId = Integer.parseInt(fields[1].trim());
                    // Skip day field (fields[2])
                    tripRec.seq = Integer.parseInt(fields[3].trim());

                    // Parse HexWKB trip
                    String hexwkb = fields[4].trim();
                    tripRec.trip = temporal_from_hexwkb(hexwkb);

                    if (tripRec.trip == null) {
                        System.out.println("Failed to parse trip " + tripRec.tripId);
                        continue;
                    }

                    noRecords++;
                    System.out.print("*");
                    if (noRecords % 50 == 0) {
                        System.out.flush();
                    }

                    // Compute total distance (convert from meters to kilometers)
                    double d = tpoint_length(tripRec.trip) / 1000.0;
                    distance[tripRec.vehId - 1][0] += d;
                    distance[NO_VEHICLES][0] += d;

                    // Loop for each commune
                    for (int i = 0; i < NO_COMMUNES; i++) {
                        if (communes[i] == null) continue;

                        // Clip trip to commune geometry
                        Pointer atGeom = tgeo_at_geom(tripRec.trip, communes[i].geom);

                        if (atGeom != null) {
                            // Compute length of trip within commune
                            d = tpoint_length(atGeom) / 1000.0;
                            distance[tripRec.vehId - 1][i + 1] += d;
                            distance[tripRec.vehId - 1][NO_COMMUNES + 1] += d;
                            distance[NO_VEHICLES][i + 1] += d;
                            distance[NO_VEHICLES][NO_COMMUNES + 1] += d;
                        }
                    }

                    // Compute distance outside Brussels region
                    Pointer minusGeom = tpoint_minus_geom(tripRec.trip, brusselsRegion.geom, null);

                    if (minusGeom != null) {
                        d = tpoint_length(minusGeom) / 1000.0;
                        distance[tripRec.vehId - 1][NO_COMMUNES + 2] += d;
                        distance[NO_VEHICLES][NO_COMMUNES + 2] += d;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("\nTrip with invalid values: " + e.getMessage());
                }
            }

            reader.close();
            System.out.printf("%n%d trip records read.%n%n", noRecords);

            // Print matrix (false = only show communes with non-zero values)
            printMatrix(false);

        } catch (IOException e) {
            System.err.println("Error reading berlinmod_trips.csv: " + e.getMessage());
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