package tutorials;

import functions.functions;
import jnr.ffi.Pointer;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import static functions.functions.*;



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

		long startTime = System.nanoTime();

		String workingdir = System.getProperty("user.dir");
		//Reading the file
		String file_path = workingdir + "/src/main/java/tutorials/trips.csv";
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
				System.out.println(day);
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

				int records_in = i;

				BufferedWriter fileOut = new BufferedWriter(new FileWriter("trip_instants.csv"));
				fileOut.write(header_buffer);

				for(int a=0; i<MAX_NO_TRIPS;i++){
					curr_inst[a]=1;
				}

				int records_out = 0;

				while(true){
					int first = 0;
					while (first < records_in && curr_inst[first] < 0){
						first++;
					}
					if (first == records_in){
						break;
					}

					Pointer min_inst = functions.temporal_instant_n(trips[first].trip,curr_inst[first]);
					int min_trip = first;


					for (int a = first +1 ; a < records_in ; a++){
						if (curr_inst[a] < 0){
							continue;
						}
						Pointer inst = functions.temporal_instant_n(trips[i].trip,curr_inst[i]);
					}



				}


				
			} while (scanner.hasNextLine());
			
		} catch (FileNotFoundException e) {
			System.out.println("Error file not found");
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
        long stopTime = System.nanoTime();
		System.out.println("Time taken: ");
		System.out.println(stopTime - startTime);
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


