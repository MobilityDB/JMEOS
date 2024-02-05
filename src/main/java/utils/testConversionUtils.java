package utils;

import com.google.common.collect.Range;
import jnr.ffi.Pointer;
import java.sql.SQLException;

import static utils.ConversionUtils.intrange_to_intspan;
import static utils.ConversionUtils.intspan_to_intrange;

public class testConversionUtils {
    public static void main(String[] args) throws SQLException {
        Range<Integer> range1 = Range.open(0,9);
        Pointer ptr = intrange_to_intspan(range1);

        Range range = intspan_to_intrange(ptr);
        System.out.println(range);



    }
}
