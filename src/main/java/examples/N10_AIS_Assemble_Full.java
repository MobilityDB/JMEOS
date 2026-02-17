package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import types.temporal.TInterpolation;

import java.io.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static functions.functions.*;

/**
 * A program that reads AIS data from a CSV file containing one full day of
 * observations from the Danish Maritime Authority (http://aisdata.ais.dk/),
 * constructs temporal values for trips and SOG, and outputs statistics.
 *
 * This is a robust version that:
 * - Handles large volumes dynamically (millions of records, thousands of ships)
 * - Validates data (lat/lon ranges, SOG validity)
 * - Filters duplicate timestamps
 * - Uses dynamic memory allocation (ArrayList expansion)
 *
 * Input: data/aisdk-2026-02-13.csv (full day of AIS data)
 *
 * CSV Format:
 * Timestamp,Type A,MMSI,Latitude,Longitude,Navigational status,ROT,SOG,...
 */
public class N10_AIS_Assemble_Full {

    /* Configuration constants */
    static final int MAX_NO_RECS = 20_000_000;    // Max records to read
    static final int MAX_NO_SHIPS = 6500;         // Max ships to track
    static final int NO_RECS_BATCH = 100_000;     // Marker frequency
    static final int INITIAL_CAPACITY = 64;       // Initial ArrayList capacity

    /* Validation ranges for Denmark region */
    static final double LAT_MIN = 40.18;
    static final double LAT_MAX = 84.17;
    static final double LON_MIN = -16.1;
    static final double LON_MAX = 32.88;
    static final double SOG_MIN = 0.0;
    static final double SOG_MAX = 1022.0;  // Speed in 1/10 knot steps (0-102.2 knots)

    /* Date format parser for European format (DD/MM/YYYY HH:MM:SS) */
    static final DateTimeFormatter EUROPEAN_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Parse European date format (DD/MM/YYYY HH:MM:SS) to OffsetDateTime in UTC
     */
    private static OffsetDateTime parseEuropeanDateTime(String dateStr) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr.trim(), EUROPEAN_DATETIME_FORMATTER);
            return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }

    static class AISRecord {
        OffsetDateTime T;
        long MMSI;
        double Latitude;
        double Longitude;
        double SOG;
    }

    static class TripRecord {
        long MMSI;                          /* Ship identifier */
        int noRecords;                      /* Number of input records */
        List<Pointer> tripInstants;         /* Trip instants (geographic points) */
        List<Pointer> sogInstants;          /* SOG instants (speed over ground) */
        OffsetDateTime lastTripTimestamp;   /* Last timestamp for trip (duplicate detection) */
        OffsetDateTime lastSOGTimestamp;    /* Last timestamp for SOG (duplicate detection) */

        TripRecord(long mmsi) {
            this.MMSI = mmsi;
            this.noRecords = 0;
            this.tripInstants = new ArrayList<>(INITIAL_CAPACITY);
            this.sogInstants = new ArrayList<>(INITIAL_CAPACITY);
            this.lastTripTimestamp = null;
            this.lastSOGTimestamp = null;
        }
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        long startTime = System.currentTimeMillis();

        // Map to store trips by MMSI
        Map<Long, TripRecord> trips = new LinkedHashMap<>();  // Preserve insertion order

        int noRecords = 0;
        int noErrRecords = 0;

        try {
            System.out.println("Opening input file: data/aisdk-2026-02-13.csv");
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/examples/data/aisdk-2026-02-13.csv"));

            // Read header line
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty file");
                reader.close();
                meos_finalize();
                return;
            }

            System.out.println("Processing records");
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
                if (rec == null) {
                    noErrRecords++;
                    continue;
                }

                // Check max ships limit
                if (trips.size() >= MAX_NO_SHIPS && !trips.containsKey(rec.MMSI)) {
                    continue;
                }

                // Get or create trip record
                TripRecord trip = trips.computeIfAbsent(rec.MMSI, TripRecord::new);
                trip.noRecords++;

                // Create and add trip instant (if lat/lon present)
                if (!Double.isNaN(rec.Latitude) && !Double.isNaN(rec.Longitude)) {
                    try {
                        // Check for duplicate timestamp before creating instant
                        if (trip.lastTripTimestamp == null || !rec.T.equals(trip.lastTripTimestamp)) {
                            Pointer gs = geogpoint_make2d(4326, rec.Longitude, rec.Latitude);
                            Pointer inst = tpointinst_make(gs, rec.T);
                            trip.tripInstants.add(inst);
                            trip.lastTripTimestamp = rec.T;
                        }
                    } catch (Exception e) {
                        // Ignore invalid point
                    }
                }

                // Create and add SOG instant (if SOG present)
                if (!Double.isNaN(rec.SOG)) {
                    try {
                        // Check for duplicate timestamp before creating instant
                        if (trip.lastSOGTimestamp == null || !rec.T.equals(trip.lastSOGTimestamp)) {
                            Pointer inst = tfloatinst_make(rec.SOG, rec.T);
                            trip.sogInstants.add(inst);
                            trip.lastSOGTimestamp = rec.T;
                        }
                    } catch (Exception e) {
                        // Ignore invalid SOG
                    }
                }
            }

            reader.close();

            // Print results table
            System.out.println("\n-----------------------------------------------------------------------------");
            System.out.println("|   MMSI    |   #Rec  | #TrInst |  #SInst |     Distance    |     Speed     |");
            System.out.println("-----------------------------------------------------------------------------");

            Runtime runtime = Runtime.getSystemRuntime();

            for (TripRecord trip : trips.values()) {
                System.out.printf("| %9d |   %5d |   %5d |   %5d |",
                        trip.MMSI, trip.noRecords,
                        trip.tripInstants.size(), trip.sogInstants.size());

                // Calculate trip distance
                if (!trip.tripInstants.isEmpty()) {
                    try {
                        Pointer array = createPointerArray(runtime, trip.tripInstants);
                        Pointer tripSeq = tsequence_make(array, trip.tripInstants.size(),
                                true, true, TInterpolation.LINEAR.getValue(), true);

                        double distance = tpoint_length(tripSeq);
                        System.out.printf(" %15.6f |", distance);
                    } catch (Exception e) {
                        System.out.print("        ---      |");
                    }
                } else {
                    System.out.print("        ---      |");
                }

                // Calculate time-weighted average SOG
                if (!trip.sogInstants.isEmpty()) {
                    try {
                        Pointer array = createPointerArray(runtime, trip.sogInstants);
                        Pointer sogSeq = tsequence_make(array, trip.sogInstants.size(),
                                true, true, TInterpolation.LINEAR.getValue(), true);

                        double avgSOG = tnumber_twavg(sogSeq);
                        System.out.printf(" %13.6f |\n", avgSOG);
                    } catch (Exception e) {
                        System.out.println("       ---     |");
                    }
                } else {
                    System.out.println("       ---     |");
                }
            }

            System.out.println("-----------------------------------------------------------------------------");
            System.out.printf("\n%d records read.\n", noRecords);
            System.out.printf("%d erroneous records ignored.\n", noErrRecords);
            System.out.printf("%d trips read.\n", trips.size());

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        // Calculate elapsed time
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        System.out.printf("The program took %.3f seconds to execute\n", timeTaken);

        // Finalize MEOS
        meos_finalize();
    }

    /**
     * Parse a CSV line into an AISRecord.
     * Returns null if the line is invalid or incomplete.
     */
    private static AISRecord parseLine(String line) {
        String[] fields = line.split(",", -1);  // -1 to keep empty fields

        if (fields.length < 8) {
            return null;
        }

        AISRecord rec = new AISRecord();
        boolean hasT = false, hasMMSI = false, hasLatLon = false, hasSOG = false;

        try {
            // Field 0: Timestamp (European format: DD/MM/YYYY HH:MM:SS)
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
                    hasLatLon = true;
                }
            }

            // Field 4: Longitude
            rec.Longitude = Double.NaN;
            if (!fields[4].isEmpty() && !fields[4].equals("Unknown")) {
                double lon = Double.parseDouble(fields[4].trim());
                if (lon >= LON_MIN && lon <= LON_MAX) {
                    rec.Longitude = lon;
                    // hasLatLon is only true if BOTH lat and lon are valid
                    hasLatLon = hasLatLon && !Double.isNaN(rec.Latitude);
                } else {
                    hasLatLon = false;
                }
            }

            // Field 7: SOG (Speed Over Ground)
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

        // Record is valid if it has timestamp, MMSI, and at least one of (lat/lon or SOG)
        if (hasT && hasMMSI && (hasLatLon || hasSOG)) {
            return rec;
        }

        return null;
    }

    /**
     * Create a pointer array from a list of pointers.
     */
    private static Pointer createPointerArray(Runtime runtime, List<Pointer> pointers) {
        Pointer array = Memory.allocate(runtime, pointers.size() * Long.BYTES);
        for (int i = 0; i < pointers.size(); i++) {
            array.putPointer(i * Long.BYTES, pointers.get(i));
        }
        return array;
    }
}