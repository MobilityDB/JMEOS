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
 * A program that reads AIS data from a CSV file and constructs temporal values
 * using EXPANDABLE SEQUENCES instead of accumulating all instants in memory.
 *
 * Key difference from N10_AIS_Assemble_Full:
 * - N10: Accumulates ALL instants → Builds sequence at the END
 * - N11: Builds sequence INCREMENTALLY using temporal_append_tinstant()
 *
 * Advantages:
 * - Lower memory footprint (no ArrayList of instants)
 * - Sequence always available (no need to wait for end)
 * - Automatic capacity expansion
 * - Better for streaming scenarios
 *
 * This uses the MEOS expandable sequence API:
 * - Create initial sequence with one instant
 * - Append subsequent instants using temporal_append_tinstant()
 * - MEOS handles memory expansion automatically
 *
 * Input: data/aisdk-2026-02-13.csv (full day of AIS data)
 */
public class N11_AIS_Expand_Full {

    /* Configuration constants */
    static final int MAX_NO_RECS = 20_000_000;    // Max records to read
    static final int MAX_NO_SHIPS = 6500;         // Max ships to track
    static final int NO_RECS_BATCH = 100_000;     // Marker frequency

    /* Validation ranges for Denmark region */
    static final double LAT_MIN = 40.18;
    static final double LAT_MAX = 84.17;
    static final double LON_MIN = -16.1;
    static final double LON_MAX = 32.88;
    static final double SOG_MIN = 0.0;
    static final double SOG_MAX = 1022.0;  // Speed in 1/10 knot steps

    /* Date format parser for European format (DD/MM/YYYY HH:MM:SS) */
    static final DateTimeFormatter EUROPEAN_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
        int noTripInstants;                 /* Number of trip instants */
        int noSOGInstants;                  /* Number of SOG instants */
        Pointer trip;                       /* Expandable trip sequence */
        Pointer sog;                        /* Expandable SOG sequence */
        OffsetDateTime lastTripTimestamp;   /* Last trip timestamp (duplicate detection) */
        OffsetDateTime lastSOGTimestamp;    /* Last SOG timestamp (duplicate detection) */

        TripRecord(long mmsi) {
            this.MMSI = mmsi;
            this.noRecords = 0;
            this.noTripInstants = 0;
            this.noSOGInstants = 0;
            this.trip = null;
            this.sog = null;
            this.lastTripTimestamp = null;
            this.lastSOGTimestamp = null;
        }
    }

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

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        long startTime = System.currentTimeMillis();

        // Map to store trips by MMSI
        Map<Long, TripRecord> trips = new LinkedHashMap<>();

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

            Runtime runtime = Runtime.getSystemRuntime();

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

                // Process trip instant (if lat/lon present)
                if (!Double.isNaN(rec.Latitude) && !Double.isNaN(rec.Longitude)) {
                    try {
                        // Check for duplicate timestamp
                        if (trip.lastTripTimestamp != null && rec.T.equals(trip.lastTripTimestamp)) {
                            continue;  // Skip duplicate
                        }

                        Pointer gs = geogpoint_make2d(4326, rec.Longitude, rec.Latitude);
                        Pointer inst = tpointinst_make(gs, rec.T);

                        if (trip.trip == null) {
                            // Create initial sequence with first instant
                            Pointer instArray = Memory.allocate(runtime, Long.BYTES);
                            instArray.putPointer(0, inst);
                            trip.trip = tsequence_make(instArray, 1,
                                    true, true, TInterpolation.LINEAR.getValue(), true);

                            if (trip.trip == null) {
                                System.err.printf("\nMMSI: %d, error creating trip sequence\n", trip.MMSI);
                                continue;
                            }
                        } else {
                            // Append instant to existing sequence
                            Pointer newSeq = temporal_append_tinstant(trip.trip, inst, TInterpolation.LINEAR.getValue(),
                                    0.0, null, true);

                            if (newSeq == null) {
                                System.err.printf("\nMMSI: %d, error appending to trip\n", trip.MMSI);
                                continue;
                            }

                            trip.trip = newSeq;
                        }

                        trip.noTripInstants++;
                        trip.lastTripTimestamp = rec.T;

                    } catch (Exception e) {
                        System.err.printf("\nError processing trip instant for MMSI %d: %s\n",
                                trip.MMSI, e.getMessage());
                    }
                }

                // Process SOG instant (if SOG present)
                if (!Double.isNaN(rec.SOG)) {
                    try {
                        // Check for duplicate timestamp
                        if (trip.lastSOGTimestamp != null && rec.T.equals(trip.lastSOGTimestamp)) {
                            continue;  // Skip duplicate
                        }

                        Pointer inst = tfloatinst_make(rec.SOG, rec.T);

                        if (trip.sog == null) {
                            // Create initial sequence with first instant
                            Pointer instArray = Memory.allocate(runtime, Long.BYTES);
                            instArray.putPointer(0, inst);
                            trip.sog = tsequence_make(instArray, 1,
                                    true, true, TInterpolation.LINEAR.getValue(), true);

                            if (trip.sog == null) {
                                System.err.printf("\nMMSI: %d, error creating SOG sequence\n", trip.MMSI);
                                continue;
                            }
                        } else {
                            // Append instant to existing sequence
                            Pointer newSeq = temporal_append_tinstant(trip.sog, inst, TInterpolation.LINEAR.getValue(),
                                    0.0, null, true);

                            if (newSeq == null) {
                                System.err.printf("\nMMSI: %d, error appending to SOG\n", trip.MMSI);
                                continue;
                            }

                            trip.sog = newSeq;
                        }

                        trip.noSOGInstants++;
                        trip.lastSOGTimestamp = rec.T;

                    } catch (Exception e) {
                        System.err.printf("\nError processing SOG instant for MMSI %d: %s\n",
                                trip.MMSI, e.getMessage());
                    }
                }
            }

            reader.close();

            // Print results table
            System.out.println("\n-----------------------------------------------------------------------------");
            System.out.println("|   MMSI    |   #Rec  | #TrInst |  #SInst |     Distance    |     Speed     |");
            System.out.println("-----------------------------------------------------------------------------");

            for (TripRecord trip : trips.values()) {
                System.out.printf("| %9d |   %5d |   %5d |   %5d |",
                        trip.MMSI, trip.noRecords,
                        trip.noTripInstants, trip.noSOGInstants);

                // Calculate trip distance
                if (trip.trip != null) {
                    try {
                        double distance = tpoint_length(trip.trip);
                        System.out.printf(" %15.6f |", distance);
                    } catch (Exception e) {
                        System.out.print("        ---      |");
                    }
                } else {
                    System.out.print("        ---      |");
                }

                // Calculate time-weighted average SOG
                if (trip.sog != null) {
                    try {
                        double avgSOG = tnumber_twavg(trip.sog);
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
                    hasLatLon = hasLatLon && !Double.isNaN(rec.Latitude);
                } else {
                    hasLatLon = false;
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

        // Record is valid if it has timestamp, MMSI, and at least one of (lat/lon or SOG)
        if (hasT && hasMMSI && (hasLatLon || hasSOG)) {
            return rec;
        }

        return null;
    }
}