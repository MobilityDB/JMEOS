package tutorials;

import jnr.ffi.Pointer;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static functions.functions.*;

public class hello_world {
	
	
	public static void main(String[] args) throws SQLException {
		
		//Initialize meos hash table
		meos_initialize("UTC");
		
		String inst_wkt = "POINT(1 1)@2000-01-01";
		String seq_disc_wkt = "{POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02}";
		String seq_linear_wkt = "[POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02]";
		String seq_step_wkt = "Interp=Step;[POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02]";
		String ss_linear_wkt = "{[POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02],"
				+ "[POINT(3 3)@2000-01-03, POINT(3 3)@2000-01-04]}";
		String ss_step_wkt = "Interp=Step;{[POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02],"
				+ "[POINT(3 3)@2000-01-03, POINT(3 3)@2000-01-04]}";
		
		
		Pointer inst = tgeompoint_in(inst_wkt);
		Pointer seq_disc = tgeompoint_in(seq_disc_wkt);
		Pointer seq_linear = tgeompoint_in(seq_linear_wkt);
		Pointer seq_step = tgeompoint_in(seq_step_wkt);
		Pointer ss_linear = tgeompoint_in(ss_linear_wkt);
		Pointer ss_step = tgeompoint_in(ss_step_wkt);
		
		//runtime.getMemoryManager().free
		
		LocalDateTime tmstp = pg_timestamp_in("2020-01-08 00:00:00", -1);
		String output = pg_timestamp_out(tmstp);
		System.out.println(output);
		
		
		/* Convert result to MF-JSON */
		String inst_mfjson = temporal_as_mfjson(inst, true, 3, 6, null);
		System.out.printf("\n" +
				"--------------------\n" +
				"| Temporal Instant |\n" +
				"--------------------\n\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", inst_wkt, inst_mfjson);
		String seq_disc_mfjson = temporal_as_mfjson(seq_disc, true, 3, 6, null);
		System.out.printf("\n" +
				"-------------------------------------------------\n" +
				"| Temporal Sequence with Discrete Interpolation |\n" +
				"-------------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", seq_disc_wkt, seq_disc_mfjson);
		String seq_linear_mfjson = temporal_as_mfjson(seq_linear, true, 3, 6, null);
		System.out.printf("\n" +
				"-----------------------------------------------\n" +
				"| Temporal Sequence with Linear Interpolation |\n" +
				"-----------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", seq_linear_wkt, seq_linear_mfjson);
		String seq_step_mfjson = temporal_as_mfjson(seq_step, true, 3, 6, null);
		System.out.printf("\n" +
				"--------------------------------------------\n" +
				"| Temporal Sequence with Step Interpolation |\n" +
				"--------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", seq_step_wkt, seq_step_mfjson);
		String ss_linear_mfjson = temporal_as_mfjson(ss_linear, true, 3, 6, null);
		System.out.printf("\n" +
				"---------------------------------------------------\n" +
				"| Temporal Sequence Set with Linear Interpolation |\n" +
				"---------------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", ss_linear_wkt, ss_linear_mfjson);
		String ss_step_mfjson = temporal_as_mfjson(ss_step, true, 3, 6, null);
		System.out.printf("\n" +
				"------------------------------------------------\n" +
				"| Temporal Sequence Set with Step Interpolation |\n" +
				"------------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", ss_step_wkt, ss_step_mfjson);
		
		meos_finalize();
	}
	
}