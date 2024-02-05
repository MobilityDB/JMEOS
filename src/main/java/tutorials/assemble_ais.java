package tutorials;

import jnr.ffi.Pointer;

import java.io.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Scanner;

import static functions.functions.*;


public class assemble_ais {
	public static final int MAX_INSTANTS = 50000;
	public static final int NO_INSTANTS_BATCH = 1000;
	public static final int MAX_LENGTH_HEADER = 1024;
	public static final int MAX_LENGTH_POINT = 64;
	public static final int MAX_LENGTH_TIMESTAMP = 32;
	public static final int MAX_TRIPS = 5;
	
	
	public static void main(String[] args) {
		
		meos_initialize("UTC");
		
		long t = System.nanoTime() / 1000000;
		
		trip_record[] trips = new trip_record[MAX_TRIPS];
		Arrays.fill(trips, new trip_record());
		int numships = 0;
		int i, j;
		int return_value = 0;
		
		
		//Reading the file
		String file_path = "/home/nidhal/IdeaProjects/JMEOS/src/main/java/tutorials/aisinput.csv";
		try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
		} catch (IOException e) {
			System.out.println("Error opening file");
		}
		
		AIS_record rec = new AIS_record();
		int no_records = 0;
		int no_nulls = 0;
		char[] header_buffer = new char[MAX_LENGTH_HEADER];
		char[] point_buffer = new char[MAX_LENGTH_POINT];
		char[] timestamp_buffer = new char[MAX_LENGTH_TIMESTAMP];
		
		
		try (Scanner scanner = new Scanner(new File(file_path))) {
			header_buffer = scanner.next().toCharArray();
		} catch (FileNotFoundException e) {
			System.out.println("Error file not found");
		}
		
		
		try (Scanner scanner = new Scanner(new File(file_path))) {
			String line;
			line = scanner.nextLine();
			do {
				
				
				line = scanner.nextLine();
				String[] tokens = line.split(",");
				rec.MMSI = Integer.parseInt(tokens[1]);
				rec.Latitude = Double.parseDouble(tokens[2].trim());
				rec.Longitude = Double.parseDouble(tokens[3].trim());
				rec.SOG = Double.parseDouble(tokens[4].trim());
				String temp = tokens[0];
				rec.T = pg_timestamp_in(tokens[0], -1);
				
				if (tokens.length == 5) {
					no_records++;
					if (no_records % NO_INSTANTS_BATCH == 0) {
						System.out.print("*");
						System.out.flush();
					}
				}
				if (tokens.length != 5 && scanner.hasNextLine()) {
					System.out.println("Record with missing values ignored \n");
					no_nulls++;
				}
				
				
				int ship = -1;
				for (i = 0; i < MAX_TRIPS; i++) {
					if (trips[i].MMSI == rec.MMSI) {
						ship = i;
						break;
					}
					
				}
				
				if (ship < 0) {
					ship = numships++;
					if (ship == MAX_TRIPS) {
						System.out.printf("The maximum number of ships in the input file is bigger than %d", MAX_TRIPS);
						//return_value = 1;
					}
					trips[ship].MMSI = rec.MMSI;
				}
				
				
				if (no_records % 1000 == 0) {
					
					String t_out = pg_timestamp_out(rec.T);
					String str_pointbuffer;
					str_pointbuffer = String.format("SRID=4326;Point(%f %f)@%s+00", rec.Longitude, rec.Latitude, t_out);
					
					String test = "Point(4.617660 55.573682)@2004-06-15 07:13:32+00";
					Pointer inst1 = tgeogpoint_in(test);
					String inst1_out = tpoint_as_text(inst1, 2);
					
					
					Pointer inst2 = tfloatinst_make((float) rec.SOG, OffsetDateTime.from(rec.T));
					String inst2_out = tfloat_out(inst2, 2);
					
					System.out.printf("MMSI:%d, Location: %s SOG:%s\n", rec.MMSI, inst1_out, inst2_out);
					
				}
				
			} while (scanner.hasNextLine());
			
		} catch (FileNotFoundException e) {
			System.out.println("Error file not found");
		}
		
		System.out.printf("\n%d no_records read.\n%d incomplete records ignored.\n", no_records, no_nulls);
		
		meos_finalize();
		
	}
	
	public static class AIS_record {
		public LocalDateTime T;
		public long MMSI;
		public double Latitude;
		public double Longitude;
		public double SOG;
		
	}
	
	public static class trip_record {
		public long MMSI;
		public Integer numinstants;
		public Pointer[] trip_instants = new Pointer[MAX_INSTANTS];
		public Pointer[] SOG_instants = new Pointer[MAX_INSTANTS];
		public Pointer trip;
		public Pointer SOG;
		
	}
	
}


