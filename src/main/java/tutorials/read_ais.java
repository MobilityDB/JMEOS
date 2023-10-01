package tutorials;

import functions.functions;
import jnr.ffi.Pointer;

import java.io.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Scanner;

import static functions.functions.meos_finalize;
import static functions.functions.meos_initialize;


public class read_ais {
	public static final int MAX_LENGTH_HEADER = 1024;
	public static final int MAX_LENGTH_POINT = 64;
	public static final int MAX_LENGTH_TIMESTAMP = 32;
	
	
	public static void main(String[] args) {
		
		meos_initialize("UTC");
		
		String workingdir = System.getProperty("user.dir");
		System.out.println(workingdir);
		//Reading the file
		String file_path = workingdir + "/src/main/java/tutorials/aisinput.csv";
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
				rec.SOG = Float.parseFloat(tokens[4].trim());
				String temp = tokens[0];
				rec.T = functions.pg_timestamp_in(tokens[0].trim(), -1);
				
				if (tokens.length == 5) {
					no_records++;
				}
				if (tokens.length != 5 && scanner.hasNextLine()) {
					System.out.println("Record with missing values ignored \n");
					no_nulls++;
				}
				if (no_records % 1000 == 0) {
					
					String t_out = functions.pg_timestamp_out(rec.T);
					System.out.println(t_out);
					String str_pointbuffer;
					str_pointbuffer = String.format("SRID=4326;Point(%f %f)@%s+00", rec.Longitude, rec.Latitude, t_out);
					str_pointbuffer = str_pointbuffer.replaceAll(",", ".");
					
					Pointer inst1 = functions.tgeogpoint_in(str_pointbuffer);
					String inst1_out = functions.tpoint_as_text(inst1, 2);
					
					float rec_tmp = (float) rec.SOG;
					
					Pointer inst2 = functions.tfloatinst_make(rec_tmp, OffsetDateTime.from(rec.T));
					String inst2_out = functions.tfloat_out(inst2, 2);
					
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
	
}


