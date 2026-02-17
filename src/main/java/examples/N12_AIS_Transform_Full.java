package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Pointer;

import java.io.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static functions.functions.*;

/**
 * A program that reads AIS data and transforms coordinates from one
 * Coordinate Reference System (CRS) to another.
 *
 * Transformation:
 * - FROM: EPSG:4326 (WGS84 - latitude/longitude in degrees)
 * - TO:   EPSG:25832 (ETRS89 / UTM zone 32N - meters, suitable for Denmark)
 *
 * This is a STREAMING program:
 * - Reads one record
 * - Transforms coordinates
 * - Writes immediately
 * - No data kept in memory
 *
 * Use case: Convert geographic coordinates to a projected coordinate system
 * for accurate distance calculations and spatial analysis in Denmark region.
 *
 * Input:  data/aisdk-2026-02-13.csv (WGS84 coordinates)
 * Output: data/aisdk-2026-02-13_25832.csv (UTM coordinates)
 */
public class N12_AIS_Transform_Full {

    /* Configuration constants */
    static final int MAX_NO_RECS = 20_000_000;    // Max records to read
    static final int NO_RECS_BATCH = 100_000;     // Marker frequency

    /* Validation ranges for Denmark region */
    static final double LAT_MIN = 40.18;
    static final double LAT_MAX = 84.17;
    static final double LON_MIN = -16.1;
    static final double LON_MAX = 32.88;
    static final double SOG_MIN = 0.0;
    static final double SOG_MAX = 1022.0;

    /* Coordinate Reference Systems */
    static final int SRID_WGS84 = 4326;      // Input: Geographic (lat/lon)
    static final int SRID_UTM32N = 25832;    // Output: Projected (meters)

    /* Date format parser */
    static final DateTimeFormatter EUROPEAN_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /* Pattern to parse POINT(x y) from WKT */
    static final Pattern POINT_PATTERN = Pattern.compile("POINT\\(([\\d\\.\\-]+)\\s+([\\d\\.\\-]+)\\)");

    static class AISRecord {
        OffsetDateTime T;
        long MMSI;
        double Latitude;
        double Longitude;
        double SOG;
    }

    /**
     * Coordinate pair (X, Y) or (Easting, Northing)
     */
    static class Coordinates {
        double x;
        double y;

        Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Parse European date format (DD/MM/YYYY HH:MM:SS)
     */
    private static OffsetDateTime parseEuropeanDateTime(String dateStr) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr.trim(), EUROPEAN_DATETIME_FORMATTER);
            return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extract X and Y coordinates from a geometry pointer.
     * Uses EWKT (Extended Well-Known Text) output.
     */
    private static Coordinates extractCoordinates(Pointer geom) {
        try {
            // Get EWKT representation (text format with SRID)
            // geo_as_ewkt returns: "SRID=25832;POINT(x y)"
            String ewkt = geo_as_ewkt(geom, 6);  // 6 decimal places

            // Parse "POINT(x y)"
            Matcher matcher = POINT_PATTERN.matcher(ewkt);
            if (matcher.find()) {
                double x = Double.parseDouble(matcher.group(1));
                double y = Double.parseDouble(matcher.group(2));
                return new Coordinates(x, y);
            } else {
                System.err.println("Failed to parse EWKT: " + ewkt);
            }
        } catch (Exception e) {
            System.err.println("Error extracting coordinates: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        long startTime = System.currentTimeMillis();

        int noRecords = 0;
        int noErrRecords = 0;
        int noWrites = 0;

        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            // Open input file
            System.out.println("Opening input file: data/aisdk-2026-02-13.csv");
            reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/aisdk-2026-02-13.csv"));

            // Open output file
            System.out.println("Creating output file: data/aisdk-2026-02-13_25832.csv");
            writer = new BufferedWriter(
                    new FileWriter("src/main/java/examples/data/aisdk-2026-02-13_25832.csv"));

            // Read and skip header line
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty file");
                return;
            }

            // Write output header
            writer.write("# Timestamp,MMSI,X(Easting),Y(Northing),SOG\n");

            System.out.println("Processing records (transforming EPSG:4326 → EPSG:25832)");
            System.out.println("  one '*' marker every " + NO_RECS_BATCH + " records");

            // Process each line
            String line;
            while ((line = reader.readLine()) != null && noRecords < MAX_NO_RECS) {
                noRecords++;

                // Print progress marker
                if (noRecords % NO_RECS_BATCH == 0) {
                    System.out.print("*");
                    System.out.flush();
                }

                // Parse the line
                AISRecord rec = parseLine(line);

                // Validate record
                if (rec == null || Double.isNaN(rec.Latitude) || Double.isNaN(rec.Longitude)) {
                    noErrRecords++;
                    continue;
                }

                try {
                    // Create geographic point (WGS84)
                    Pointer geogPoint = geogpoint_make2d(SRID_WGS84, rec.Longitude, rec.Latitude);

                    // Transform to UTM Zone 32N (EPSG:25832)
                    Pointer transformedPoint = geo_transform(geogPoint, SRID_UTM32N);

                    if (transformedPoint == null) {
                        System.err.printf("\nTransformation failed for MMSI %d\n", rec.MMSI);
                        noErrRecords++;
                        continue;
                    }

                    // Extract X/Y coordinates from transformed point
                    Coordinates coords = extractCoordinates(transformedPoint);

                    if (coords == null) {
                        System.err.printf("\nFailed to extract coordinates for MMSI %d\n", rec.MMSI);
                        noErrRecords++;
                        continue;
                    }

                    // Write transformed record to output file
                    String timestamp = rec.T.format(EUROPEAN_DATETIME_FORMATTER);
                    writer.write(String.format("%s,%d,%.6f,%.6f,%.1f\n",
                            timestamp, rec.MMSI, coords.x, coords.y, rec.SOG));

                    noWrites++;

                } catch (Exception e) {
                    System.err.printf("\nError processing record for MMSI %d: %s\n",
                            rec.MMSI, e.getMessage());
                    noErrRecords++;
                }
            }

            System.out.printf("\n\n%d records read\n", noRecords);
            System.out.printf("%d incomplete records ignored\n", noErrRecords);
            System.out.printf("%d writes to the output file\n", noWrites);

        } catch (IOException e) {
            System.err.println("Error with files: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close files
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Calculate elapsed time
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        System.out.printf("The program took %.3f seconds to execute\n", timeTaken);

        System.out.println("\nOutput file created: data/aisdk-2026-02-13_25832.csv");
        System.out.println("  Format: Timestamp,MMSI,X(Easting),Y(Northing),SOG");
        System.out.println("  Coordinate system: EPSG:25832 (ETRS89 / UTM zone 32N)");

        // Finalize MEOS
        meos_finalize();
    }

    /**
     * Parse a CSV line into an AISRecord.
     */
    private static AISRecord parseLine(String line) {
        String[] fields = line.split(",", -1);

        if (fields.length < 8) {
            return null;
        }

        AISRecord rec = new AISRecord();
        boolean hasT = false, hasMMSI = false, hasLat = false, hasLon = false, hasSOG = false;

        try {
            // Field 0: Timestamp
            if (!fields[0].isEmpty() && !fields[0].equals("Unknown")) {
                rec.T = parseEuropeanDateTime(fields[0]);
                hasT = (rec.T != null);
            }

            // Field 2: MMSI
            if (!fields[2].isEmpty() && !fields[2].equals("Unknown")) {
                rec.MMSI = Long.parseLong(fields[2].trim());
                hasMMSI = (rec.MMSI != 0);
            }

            // Field 3: Latitude
            rec.Latitude = Double.NaN;
            if (!fields[3].isEmpty() && !fields[3].equals("Unknown")) {
                double lat = Double.parseDouble(fields[3].trim());
                if (lat >= LAT_MIN && lat <= LAT_MAX) {
                    rec.Latitude = lat;
                    hasLat = true;
                }
            }

            // Field 4: Longitude
            rec.Longitude = Double.NaN;
            if (!fields[4].isEmpty() && !fields[4].equals("Unknown")) {
                double lon = Double.parseDouble(fields[4].trim());
                if (lon >= LON_MIN && lon <= LON_MAX) {
                    rec.Longitude = lon;
                    hasLon = true;
                }
            }

            // Field 7: SOG
            rec.SOG = Double.NaN;
            if (!fields[7].isEmpty() && !fields[7].equals("Unknown")) {
                double sog = Double.parseDouble(fields[7].trim());
                if (sog >= SOG_MIN && sog <= SOG_MAX) {
                    rec.SOG = sog;
                    hasSOG = true;
                }
            }

        } catch (NumberFormatException e) {
            return null;
        }

        // Record is valid if it has all required fields
        if (hasT && hasMMSI && hasLat && hasLon && hasSOG) {
            return rec;
        }

        return null;
    }
}