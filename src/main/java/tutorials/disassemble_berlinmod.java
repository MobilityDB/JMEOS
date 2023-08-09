package tutorials;

import functions.functions;
import jnr.ffi.Pointer;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;


public class disassemble_berlinmod {
	public static final int MAX_LENGTH_TRIP = 170001;
	public static final int MAX_LENGTH_HEADER = 1024;
	public static final int MAX_LENGTH_DATE = 12;
	public static final int MAX_NO_TRIPS = 64;
	
	
	public static void main(String[] args) {
		
		functions.meos_initialize("UTC");
		
		char[] header_buffer = new char[MAX_LENGTH_HEADER];
		char[] date_buffer = new char[MAX_LENGTH_DATE];
		char[] trip_buffer = new char[MAX_LENGTH_TRIP];
		
		trip_record[] trips = new trip_record[MAX_NO_TRIPS];
		Arrays.fill(trips, new trip_record());
		int[] curr_inst = new int[MAX_NO_TRIPS];
		
		
		//Reading the file
		String file_path = "/home/nidhal/IdeaProjects/JMEOS/src/main/java/tutorials/aisinput.csv";
		try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
		} catch (IOException e) {
			System.out.println("Error opening file");
		}
		
		int i = 0;
		// Read the first line with the header
		try (Scanner scanner = new Scanner(new File(file_path))) {
			header_buffer = scanner.next().toCharArray();
		} catch (FileNotFoundException e) {
			System.out.println("Error file not found");
		}
		
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
				
				int day = functions.pg_date_in(date_buffer_string);
				Pointer trip = functions.temporal_from_hexwkb(trip_buffer_string);
				
				trips[i].vehid = vehid;
				trips[i].tripid = tripid;
				trips[i].seq = seq;
				trips[i].day = day;
				trips[i].trip = trip;
				
				if (tokens.length == 5) {
					i++;
				}
				
				if (tokens.length != 5 && scanner.hasNextLine()) {
					System.out.println("Trip record with missing values \n");
				}
				
				
			} while (scanner.hasNextLine());
			
		} catch (FileNotFoundException e) {
			System.out.println("Error file not found");
		}
		
		int records_in = i;
		
		
		functions.meos_finalize();
		
	}
	
	
	public static class trip_record {
		public int tripid;
		public int vehid;
		public int day;
		public int seq;
		public Pointer trip;
		
	}
	
}


