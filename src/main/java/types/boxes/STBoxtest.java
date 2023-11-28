package types.boxes;

import com.google.common.collect.Range;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.time.Period;

import java.sql.SQLException;

import static utils.ConversionUtils.intrange_to_intspan;
import static utils.ConversionUtils.intspan_to_intrange;

public class STBoxtest {
    public static void main(String[] args) throws SQLException {
        functions.meos_initialize("UTC");
        STBox stb = new STBox("STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02])");
        //System.out.println(stb.toString(15));
    }
}
