package examples;

import functions.error_handler;
import functions.error_handler_fn;
import jnr.ffi.Pointer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;

import static functions.functions.*;

/**
 * A simple program that reads AIS data from a CSV file and outputs
 * a few of these records converted into temporal values.
 *
 * In the input file located in the data/ subdirectory it is assumed that:
 * - The coordinates are given in the WGS84 geographic coordinate system
 * - The timestamps are given in the GMT time zone
 *
 * This simple program does not cope with erroneous inputs, such as missing
 * fields or invalid timestamp values.
 */
public class N02_AIS_Read {

    static class AISRecord {
        OffsetDateTime T;
        long MMSI;
        double Latitude;
        double Longitude;
        double SOG;
    }

    public static void main(String[] args) {
        error_handler_fn errorHandler = new error_handler();

        // Initialize MEOS
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        int noRecords = 0;
        int noNulls = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/examples/data/ais_instants.csv"))) {
            // Read the first line with headers
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Empty file");
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

                    // Print only 1 out of 1000 records
                    if (noRecords % 1000 == 0) {
                        // Create geographic point
                        Pointer gs = geogpoint_make2d(4326, rec.Longitude, rec.Latitude);

                        // Create temporal point instant
                        Pointer instPtr = tpointinst_make(gs, rec.T);
                        String inst1Out = tspatial_as_text(instPtr, 2);

                        // Create temporal float instant for SOG
                        Pointer inst2Ptr = tfloatinst_make(rec.SOG, rec.T);
                        String inst2Out = tfloat_out(inst2Ptr, 2);

                        System.out.printf("MMSI: %d, Location: %s SOG : %s%n",
                                rec.MMSI, inst1Out, inst2Out);
                    }

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

        // Finalize MEOS
        meos_finalize();
    }
}