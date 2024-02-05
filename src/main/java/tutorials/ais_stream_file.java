package tutorials;

import types.basic.tpoint.tgeog.TGeogPointInst;
import types.basic.tpoint.tgeog.TGeogPointSeq;
import types.collections.time.TimestampSet;
import utils.ConversionUtils;
import functions.functions;

import java.io.*;
import java.time.LocalDateTime;

import static functions.functions.*;

public class ais_stream_file {
    private static final int NO_INSTANTS_BATCH = 1000;
    private static final int NO_INSTANTS_KEEP = 5;
    private static final int MAX_TRIPS = 5;
    private static final int MAX_LENGTH_POINT = 64;
    private static final int MAX_LENGTH_HEADER = 1024;
    private static int i;
    private static int j;

    static class AISRecord {
        LocalDateTime T;
        long MMSI;
        double Latitude;
        double Longitude;
        double SOG;
    }

    static class TripRecord {
        long MMSI = 0;
        TGeogPointSeq trip; // Equivalent class for TSequence in Java
    }

    public static void main(String[] args) {
        AISRecord rec = new AISRecord();
        int noRecords = 0;
        int noNulls = 0;
        int noWrites = 0;
        int check = 0;

        TripRecord[] trips = new TripRecord[MAX_TRIPS];
        for (int ia = 0; ia < trips.length; ia++) {
            trips[ia] = new TripRecord();
        }
        int noShips = 0;
        String workingdir = System.getProperty("user.dir");
        System.out.println(workingdir);
        try (BufferedReader fileIn = new BufferedReader(new FileReader(workingdir + "/src/main/java/tutorials/aisinput.csv"));
             BufferedWriter fileOut = new BufferedWriter(new FileWriter("ais_trips_new.csv"))) {
             meos_initialize("UTC");

            String line;
            fileIn.readLine(); // Read header line

            while ((line = fileIn.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 5) {
                    noNulls++;
                    continue;
                }

                // Parse data and fill AISRecord
                rec.MMSI = Long.parseLong(data[1]);
                rec.Latitude = Double.parseDouble(data[2]);
                rec.Longitude = Double.parseDouble(data[3]);
                rec.SOG = Double.parseDouble(data[4]);
                String temp = data[0];
                String new_temp = "{" + temp + "+00" + "}";
                TimestampSet per = new TimestampSet(new_temp);
                rec.T = ConversionUtils.string_to_LocalDateTime(temp);


                noRecords++;
                System.out.println("Valeur de j avant le -1");
                System.out.println(j);
                j = -1;
                for (int i=0; i < noShips; i++){
                    if (trips[i].MMSI == rec.MMSI){
                        j = i;
                        break;
                    }
                }
                if (j < 0){
                    j = noShips++;
                    if(j == MAX_TRIPS){
                        break;
                    }
                    trips[j].MMSI = rec.MMSI;
                }

                System.out.println(j);
                String t_out = per.toString();
                String str_pointbuffer;
                str_pointbuffer = String.format("POINT(%f %f)@%s", rec.Longitude, rec.Latitude, t_out);
                str_pointbuffer = str_pointbuffer.replaceAll(",", ".");
                str_pointbuffer = str_pointbuffer.replaceAll("\\{", "");
                str_pointbuffer = str_pointbuffer.replaceAll("\\}", "");
                System.out.println(str_pointbuffer);
                TGeogPointInst inst1 = new TGeogPointInst(str_pointbuffer);
                String inst1_out = inst1.to_string();
                System.out.println(trips[j]);
                if (trips[j].trip!=null){
                    System.out.println(trips[j].trip.getPointInner());
                    check = functions.temporal_num_timestamps(trips[j].trip.getPointInner());
                }

                if(trips[j].trip != null && check == NO_INSTANTS_BATCH){
                    System.out.println("instant batch");
                    String temp_out = trips[j].trip.to_string();
                    fileOut.write(String.format("%d, %s%n", trips[j].MMSI,temp_out));
                    noWrites++;
                    System.out.print("*");
                }
                TGeogPointInst geoginst = new TGeogPointInst(inst1_out);
                if (trips[j].trip == null){
                    System.out.println("interieur null");
                    System.out.println(geoginst.getPointInner());
                    TGeogPointSeq tseq = new TGeogPointSeq(functions.tsequence_make_exp(geoginst.getPointInner(),1,NO_INSTANTS_BATCH,true,false,3,true));
                    trips[j].trip = tseq;

                }
                else{
                    System.out.println("add");
                    functions.tsequence_append_tinstant(trips[j].trip.getPointInner(),geoginst.getPointInner(),0.0,null,true);
                    //trips[j].trip = new TGeogPointSeq(trips[j].trip.insert(geoginst).getInner());
                }
            }

            System.out.println("\n" + noRecords + " records read\n" + noNulls + " incomplete records ignored\n" + noWrites + " writes to the output file");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            meos_finalize();
        }
    }

}
