package tutorials;

import function.functions_old;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import net.postgis.jdbc.geometry.Point;
import types.basic.tpoint.TPoint;
import types.temporal.TemporalValue;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static function.functions.*;
import static function.functions_old.meos_finalize;

public class hello_world {
	
	
	public static void main(String[] args) throws SQLException {

		//Initialize meos
		meos_initialize("UTC");
		
		System.out.println("ICI2");
		
		//Test the int and float bucket function from meos
		int int_result = int_bucket(12, 32, 42);
		System.out.println(int_result);
		double double_result = float_bucket(1.7f, 1.93f, 1.79f);
		System.out.println(double_result);
		
		//Test hello world example in JMEOS
		String value = "Point(1 1)@2019-09-08 06:04:32+02";
		TemporalValue<Point> temporalValue = TPoint.getSingleTemporalValue(value);
		System.out.println(temporalValue.getValue());
		
		//Test to put a value in a JNR FFI pointer
		int test_val = 100;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer ptr = Memory.allocateDirect(runtime, Integer.BYTES);
		ptr.putInt(0, test_val);
		int what = ptr.getInt(0);
		System.out.println(what);
		
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
		
		Pointer tmstp = functions_old.pg_timestamp_in("2020-01-08 00:00:00", -1);
		String output = functions_old.pg_timestamp_out(tmstp);
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
		
		
		//var t1 = meos.tint_in("2@2000-01-02") ;
        /*
        Pointer t2 = meos.tint_in("2@2000-01-02");
        Pointer[] ta = new Pointer[2];
        ta[0] = t1;
        ta[1] = t2;
        int times = 1000;
        long s = Instant.now().getEpochSecond();
        Pointer t3;
        for (int a=0; a < times; a++){
            t3 = meos.temporal_merge(t1,t2);
        }
        long m = Instant.now().getEpochSecond();
        for (int b=0; b < times; b++){
            t3 = meos.temporal_merge_array(ta,2);
        }
        long e = Instant.now().getEpochSecond();

        long res1 = m - s;
        System.out.println(res1);

        */

        /*
        Book book = new Book();
        book.setIsbn("978-9730228236");
        book.setTitle("High-Performance Java Persistence");
        book.setPriceRange(
                Range.closed(
                        BigDecimal.valueOf(39.95d),
                        BigDecimal.valueOf(45.95d)
                )
        );
        book.setDiscountDateRange(
                Range.closedOpen(
                        LocalDate.of(2019, 11, 29),
                        LocalDate.of(2019, 12, 3)
                )
        );


        System.out.println(BigDecimal.valueOf(39.95d));
        System.out.println(book.getPriceRange().lower());

         */
		
		
	}
	
}