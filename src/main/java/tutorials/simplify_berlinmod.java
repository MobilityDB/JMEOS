package tutorials;


import functions.functions;
import jnr.ffi.Pointer;
import types.basic.tpoint.tgeog.TGeogPointInst;
import types.collections.time.TimestampSet;
import utils.ConversionUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

import static functions.functions.*;

public class simplify_berlinmod {
    public static final int MAX_LENGTH_TRIP = 170001;
    public static final int MAX_LENGTH_HEADER = 1024;
    public static final int MAX_LENGTH_DATE = 12;
    public static final int DELTA_DISTANCE = 2;
    public static final int MAX_NO_TRIPS = 64;


    public static void main(String[] args) {

        meos_initialize("UTC");

        String workingdir = System.getProperty("user.dir");
        System.out.println(workingdir);
        //Reading the file
        String file_path = workingdir + "/src/main/java/tutorials/trips.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
        } catch (IOException e) {
            System.out.println("Error opening file");
        }
        char[] trip_buffer = new char[MAX_LENGTH_TRIP];
        char[] header_buffer = new char[MAX_LENGTH_HEADER];
        char[] date_buffer = new char[MAX_LENGTH_DATE];
        trip_record[] trips = new trip_record[MAX_NO_TRIPS];
        for (int k = 0; k < trips.length; k++) {
            trips[k] = new trip_record();
        }
        Pointer[] trips_dp = new Pointer[MAX_NO_TRIPS];
        Pointer[] trips_sed = new Pointer[MAX_NO_TRIPS];


        int no_records = 0;
        int nulls = 0;
        int i = 0;
        int j;

        try (Scanner scanner = new Scanner(new File(file_path))) {
            header_buffer = scanner.next().toCharArray();
        } catch (FileNotFoundException e) {
            System.out.println("Error file not found");
        }

        long startTime = System.nanoTime();
        try (Scanner scanner = new Scanner(new File(file_path))) {
            String line;
            line = scanner.nextLine();
            do {

                int tripid, vehid, seq;
                line = scanner.nextLine();
                String[] tokens = line.split(",");
                tripid = Integer.parseInt(tokens[0].trim());
                vehid = Integer.parseInt(tokens[1].trim());
                String date_buffer_string = tokens[2];
                seq = Integer.parseInt(tokens[3].trim());
                String trip_buffer_string = tokens[4];



                trips[i].day = functions.pg_date_in(date_buffer_string);
                trips[i].trip = functions.temporal_from_hexwkb(trip_buffer_string);
                trips[i].vehId = vehid;
                trips[i].tripId = tripid;
                trips[i].seq = seq;

                if (tokens.length == 5) {
                    i++;
                }
                if (tokens.length != 5 && scanner.hasNextLine()) {
                    System.out.println("Record with missing values ignored \n");
                    nulls++;
                }

            } while (scanner.hasNextLine());

        } catch (FileNotFoundException e) {
            System.out.println("Error file not found");
        }





        no_records = i;
        for (int a=0; a < no_records; a++){
            trips_dp[a] = functions.temporal_simplify_dp(trips[a].trip,DELTA_DISTANCE,false);
            trips_sed[a] = functions.temporal_simplify_dp(trips[a].trip,DELTA_DISTANCE,true);
            String day_str = pg_date_out(trips[a].day);
            System.out.printf("Vehicle: %d, Date: %s, Seq: %d, No. of instants: %d, "
                            + "No. of instants DP: %d, No. of instants SED: %d\n",
                    trips[a].vehId, day_str, trips[a].seq,
                    functions.temporal_num_instants(trips[a].trip),
                    functions.temporal_num_instants(trips_dp[a]),
                    functions.temporal_num_instants(trips_sed[a]));
        }

        long stopTime = System.nanoTime();
        System.out.println("Time taken: ");
        System.out.println((double) (stopTime - startTime)/1000000000.0);

        meos_finalize();

    }

    public static class trip_record {
        public int tripId;
        int vehId;
        int day;
        int seq;
        Pointer trip;

    }


}
