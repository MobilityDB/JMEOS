package tutorials;

import types.basic.tpoint.tgeom.TGeomPointInst;
import types.basic.tpoint.tgeom.TGeomPointSeq;
import types.basic.tpoint.tgeom.TGeomPointSeqSet;

import java.sql.SQLException;

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
		
		
		TGeomPointInst inst = new TGeomPointInst(inst_wkt);
		TGeomPointSeq seq_disc = new TGeomPointSeq(seq_disc_wkt);
		TGeomPointSeq seq_linear = new TGeomPointSeq(seq_linear_wkt);
		TGeomPointSeq seq_step = new TGeomPointSeq(seq_step_wkt);
		TGeomPointSeqSet ss_linear = new TGeomPointSeqSet(ss_linear_wkt);
		TGeomPointSeqSet ss_step = new TGeomPointSeqSet(ss_step_wkt);
		
		
		/* Convert result to MF-JSON */
		String inst_mfjson = inst.as_mfjson( true, 3, 6, null);
		System.out.printf("\n" +
				"--------------------\n" +
				"| Temporal Instant |\n" +
				"--------------------\n\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", inst_wkt, inst_mfjson);
		String seq_disc_mfjson = seq_disc.as_mfjson( true, 3, 6, null);
		System.out.printf("\n" +
				"-------------------------------------------------\n" +
				"| Temporal Sequence with Discrete Interpolation |\n" +
				"-------------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", seq_disc_wkt, seq_disc_mfjson);
		String seq_linear_mfjson = seq_linear.as_mfjson( true, 3, 6, null);
		System.out.printf("\n" +
				"-----------------------------------------------\n" +
				"| Temporal Sequence with Linear Interpolation |\n" +
				"-----------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", seq_linear_wkt, seq_linear_mfjson);
		String seq_step_mfjson = seq_step.as_mfjson( true, 3, 6, null);
		System.out.printf("\n" +
				"--------------------------------------------\n" +
				"| Temporal Sequence with Step Interpolation |\n" +
				"--------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", seq_step_wkt, seq_step_mfjson);
		String ss_linear_mfjson = ss_linear.as_mfjson( true, 3, 6, null);
		System.out.printf("\n" +
				"---------------------------------------------------\n" +
				"| Temporal Sequence Set with Linear Interpolation |\n" +
				"---------------------------------------------------\n" +
				"WKT:\n" +
				"----\n%s\n\n" +
				"MF-JSON:\n" +
				"--------\n%s\n", ss_linear_wkt, ss_linear_mfjson);
		String ss_step_mfjson = ss_step.as_mfjson( true, 3, 6, null);
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