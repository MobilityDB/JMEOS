package examples;

import functions.error_handler;
import functions.error_handler_fn;
import types.basic.tpoint.tgeog.TGeogPointInst;
import types.basic.tpoint.tgeog.TGeogPointSeq;
import types.basic.tpoint.tgeog.TGeogPointSeqSet;

import java.sql.SQLException;

import static functions.functions.*;

public class N01_Hello_World_Geodetic {


    public static void main(String[] args) throws SQLException {

        error_handler_fn errorHandler = new error_handler();

        // Initialize meos
        meos_initialize_timezone("UTC");
        meos_initialize_error_handler(errorHandler);

        String srs = "EPSG:4326";
        String inst_wkt = "POINT(1 1)@2000-01-01";
        String seq_disc_wkt = "{POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02}";
        String seq_linear_wkt = "[POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02]";
        String seq_step_wkt = "Interp=Step;[POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02]";
        String ss_linear_wkt = "{[POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02],"
                + "[POINT(3 3)@2000-01-03, POINT(3 3)@2000-01-04]}";
        String ss_step_wkt = "Interp=Step;{[POINT(1 1)@2000-01-01, POINT(2 2)@2000-01-02],"
                + "[POINT(3 3)@2000-01-03, POINT(3 3)@2000-01-04]}";


        TGeogPointInst inst = new TGeogPointInst(inst_wkt);
        TGeogPointSeq seq_disc = new TGeogPointSeq(seq_disc_wkt);
        TGeogPointSeq seq_linear = new TGeogPointSeq(seq_linear_wkt);
        TGeogPointSeq seq_step = new TGeogPointSeq(seq_step_wkt);
        TGeogPointSeqSet ss_linear = new TGeogPointSeqSet(ss_linear_wkt);
        TGeogPointSeqSet ss_step = new TGeogPointSeqSet(ss_step_wkt);


        /* Convert result to MF-JSON */
        String inst_mfjson = inst.as_mfjson( true, 3, 6, srs);
        System.out.printf("\n" +
                "--------------------\n" +
                "| Temporal Instant |\n" +
                "--------------------\n\n" +
                "WKT:\n" +
                "----\n%s\n\n" +
                "MF-JSON:\n" +
                "--------\n%s\n", inst_wkt, inst_mfjson);
        String seq_disc_mfjson = seq_disc.as_mfjson( true, 3, 6, srs);
        System.out.printf("\n" +
                "-------------------------------------------------\n" +
                "| Temporal Sequence with Discrete Interpolation |\n" +
                "-------------------------------------------------\n" +
                "WKT:\n" +
                "----\n%s\n\n" +
                "MF-JSON:\n" +
                "--------\n%s\n", seq_disc_wkt, seq_disc_mfjson);
        String seq_linear_mfjson = seq_linear.as_mfjson( true, 3, 6, srs);
        System.out.printf("\n" +
                "-----------------------------------------------\n" +
                "| Temporal Sequence with Linear Interpolation |\n" +
                "-----------------------------------------------\n" +
                "WKT:\n" +
                "----\n%s\n\n" +
                "MF-JSON:\n" +
                "--------\n%s\n", seq_linear_wkt, seq_linear_mfjson);
        String seq_step_mfjson = seq_step.as_mfjson( true, 3, 6, srs);
        System.out.printf("\n" +
                "--------------------------------------------\n" +
                "| Temporal Sequence with Step Interpolation |\n" +
                "--------------------------------------------\n" +
                "WKT:\n" +
                "----\n%s\n\n" +
                "MF-JSON:\n" +
                "--------\n%s\n", seq_step_wkt, seq_step_mfjson);
        String ss_linear_mfjson = ss_linear.as_mfjson( true, 3, 6, srs);
        System.out.printf("\n" +
                "---------------------------------------------------\n" +
                "| Temporal Sequence Set with Linear Interpolation |\n" +
                "---------------------------------------------------\n" +
                "WKT:\n" +
                "----\n%s\n\n" +
                "MF-JSON:\n" +
                "--------\n%s\n", ss_linear_wkt, ss_linear_mfjson);
        String ss_step_mfjson = ss_step.as_mfjson( true, 3, 6, srs);
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