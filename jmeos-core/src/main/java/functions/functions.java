package functions;

import jnr.ffi.Pointer;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;
import jnr.ffi.byref.PointerByReference;
import jnr.ffi.Struct;
import utils.JarLibraryLoader;
import utils.meosCatalog.MeosEnums.meosType;
import utils.meosCatalog.MeosEnums.meosOper;
import functions.MeosErrorHandler;

import java.time.*;

public class functions {

	static {
		// Fully initialize MEOS before any binding call. MEOS splits startup into
		// granular steps (allocator, error handler, timezone, collation, PROJ/GEOS/
		// GSL); meos_initialize() runs them in the required order. Installing the
		// allocator and collation is load-bearing: without collation, text
		// comparisons route into varstr_cmp with an uninitialized collation and
		// crash. Callers may still override individual steps afterwards (e.g.
		// meos_initialize_timezone("UTC")).
		MeosLibrary.meos.meos_initialize();
	}

	public interface MeosLibrary {

		String libraryPath = "libmeos.so";

		MeosLibrary INSTANCE = JarLibraryLoader.create(MeosLibrary.class, libraryPath).getLibraryInstance();

		MeosLibrary meos = MeosLibrary.INSTANCE;

		Pointer rtree_create_intspan();

		Pointer rtree_create_bigintspan();

		Pointer rtree_create_floatspan();

		Pointer rtree_create_datespan();

		Pointer rtree_create_tstzspan();

		Pointer rtree_create_tbox();

		Pointer rtree_create_stbox();

		void rtree_free(Pointer rtree);

		void rtree_insert(Pointer rtree, Pointer box, int id);

		void rtree_insert_temporal(Pointer rtree, Pointer temp, int id);

		Pointer rtree_search(Pointer rtree, int op, Pointer query, Pointer count);

		Pointer rtree_search_temporal(Pointer rtree, int op, Pointer temp, Pointer count);

		void meos_error(int errlevel, int errcode, String format);

		int meos_errno();

		int meos_errno_set(int err);

		int meos_errno_restore(int err);

		int meos_errno_reset();

		void meos_initialize_timezone(String name);

		void meos_initialize_error_handler(error_handler_fn err_handler);

		void meos_finalize_timezone();

		void meos_finalize_projsrs();

		void meos_finalize_ways();

		boolean meos_set_datestyle(String newval, Pointer extra);

		boolean meos_set_intervalstyle(String newval, int extra);

		String meos_get_datestyle();

		String meos_get_intervalstyle();

		void meos_set_spatial_ref_sys_csv(String path);

		void meos_initialize();

		void meos_finalize();

		int add_date_int(int d, int days);

		Pointer add_interval_interval(Pointer interv1, Pointer interv2);

		long add_timestamptz_interval(long t, Pointer interv);

		boolean bool_in(String str);

		String bool_out(boolean b);

		Pointer cstring_to_text(String str);

		long date_to_timestamp(int dateVal);

		long date_to_timestamptz(int d);

		double float8_exp(double d);

		double float8_ln(double d);

		double float8_log10(double d);

		String float8_out(double d, int maxdd);

		double float_round(double d, int maxdd);

		int int32_cmp(int l, int r);

		int int64_cmp(long l, long r);

		Pointer interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs);

		int minus_date_date(int d1, int d2);

		int minus_date_int(int d, int days);

		long minus_timestamptz_interval(long t, Pointer interv);

		Pointer minus_timestamptz_timestamptz(long t1, long t2);

		Pointer mul_interval_float8(Pointer interv, double factor);

		int date_in(String str);

		String date_out(int d);

		int interval_cmp(Pointer interv1, Pointer interv2);

		Pointer interval_in(String str, int typmod);

		String interval_out(Pointer interv);

		long timestamp_in(String str, int typmod);

		String timestamp_out(long t);

		long timestamptz_in(String str, int typmod);

		String timestamptz_out(long t);

		String text_to_cstring(Pointer txt);

		int text_cmp(Pointer txt1, Pointer txt2);

		Pointer text_copy(Pointer txt);

		Pointer text_in(String str);

		Pointer text_initcap(Pointer txt);

		Pointer text_lower(Pointer txt);

		String text_out(Pointer txt);

		Pointer text_upper(Pointer txt);

		Pointer textcat_text_text(Pointer txt1, Pointer txt2);

		long timestamptz_shift(long t, Pointer interv);

		int timestamp_to_date(long t);

		int timestamptz_to_date(long t);

		Pointer bigintset_in(String str);

		String bigintset_out(Pointer set);

		Pointer bigintspan_expand(Pointer s, long value);

		Pointer bigintspan_in(String str);

		String bigintspan_out(Pointer s);

		Pointer bigintspanset_in(String str);

		String bigintspanset_out(Pointer ss);

		Pointer dateset_in(String str);

		String dateset_out(Pointer s);

		Pointer datespan_in(String str);

		String datespan_out(Pointer s);

		Pointer datespanset_in(String str);

		String datespanset_out(Pointer ss);

		Pointer floatset_in(String str);

		String floatset_out(Pointer set, int maxdd);

		Pointer floatspan_expand(Pointer s, double value);

		Pointer floatspan_in(String str);

		String floatspan_out(Pointer s, int maxdd);

		Pointer floatspanset_in(String str);

		String floatspanset_out(Pointer ss, int maxdd);

		Pointer intset_in(String str);

		String intset_out(Pointer set);

		Pointer intspan_expand(Pointer s, int value);

		Pointer intspan_in(String str);

		String intspan_out(Pointer s);

		Pointer intspanset_in(String str);

		String intspanset_out(Pointer ss);

		String set_as_hexwkb(Pointer s, byte variant, Pointer size_out);

		Pointer set_as_wkb(Pointer s, byte variant, Pointer size_out);

		Pointer set_from_hexwkb(String hexwkb);

		Pointer set_from_wkb(Pointer wkb, long size);

		String span_as_hexwkb(Pointer s, byte variant, Pointer size_out);

		Pointer span_as_wkb(Pointer s, byte variant, Pointer size_out);

		Pointer span_from_hexwkb(String hexwkb);

		Pointer span_from_wkb(Pointer wkb, long size);

		String spanset_as_hexwkb(Pointer ss, byte variant, Pointer size_out);

		Pointer spanset_as_wkb(Pointer ss, byte variant, Pointer size_out);

		Pointer spanset_from_hexwkb(String hexwkb);

		Pointer spanset_from_wkb(Pointer wkb, long size);

		Pointer textset_in(String str);

		String textset_out(Pointer set);

		Pointer tstzset_in(String str);

		String tstzset_out(Pointer set);

		Pointer tstzspan_in(String str);

		String tstzspan_out(Pointer s);

		Pointer tstzspanset_in(String str);

		String tstzspanset_out(Pointer ss);

		Pointer bigintset_make(Pointer values, int count);

		Pointer bigintspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc);

		Pointer dateset_make(Pointer values, int count);

		Pointer datespan_make(int lower, int upper, boolean lower_inc, boolean upper_inc);

		Pointer floatset_make(Pointer values, int count);

		Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc);

		Pointer intset_make(Pointer values, int count);

		Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc);

		Pointer set_copy(Pointer s);

		Pointer span_copy(Pointer s);

		Pointer spanset_copy(Pointer ss);

		Pointer spanset_make(Pointer spans, int count);

		Pointer textset_make(Pointer values, int count);

		Pointer tstzset_make(Pointer values, int count);

		Pointer tstzspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc);

		Pointer bigint_to_set(long i);

		Pointer bigint_to_span(int i);

		Pointer bigint_to_spanset(int i);

		Pointer date_to_set(int d);

		Pointer date_to_span(int d);

		Pointer date_to_spanset(int d);

		Pointer dateset_to_tstzset(Pointer s);

		Pointer datespan_to_tstzspan(Pointer s);

		Pointer datespanset_to_tstzspanset(Pointer ss);

		Pointer float_to_set(double d);

		Pointer float_to_span(double d);

		Pointer float_to_spanset(double d);

		Pointer floatset_to_intset(Pointer s);

		Pointer floatspan_to_intspan(Pointer s);

		Pointer floatspanset_to_intspanset(Pointer ss);

		Pointer int_to_set(int i);

		Pointer int_to_span(int i);

		Pointer int_to_spanset(int i);

		Pointer intset_to_floatset(Pointer s);

		Pointer intspan_to_floatspan(Pointer s);

		Pointer intspanset_to_floatspanset(Pointer ss);

		Pointer set_to_span(Pointer s);

		Pointer set_to_spanset(Pointer s);

		Pointer span_to_spanset(Pointer s);

		Pointer text_to_set(Pointer txt);

		Pointer timestamptz_to_set(long t);

		Pointer timestamptz_to_span(long t);

		Pointer timestamptz_to_spanset(long t);

		Pointer tstzset_to_dateset(Pointer s);

		Pointer tstzspan_to_datespan(Pointer s);

		Pointer tstzspanset_to_datespanset(Pointer ss);

		long bigintset_end_value(Pointer s);

		long bigintset_start_value(Pointer s);

		boolean bigintset_value_n(Pointer s, int n, Pointer result);


		long bigintspan_lower(Pointer s);

		long bigintspan_upper(Pointer s);

		long bigintspan_width(Pointer s);

		long bigintspanset_lower(Pointer ss);

		long bigintspanset_upper(Pointer ss);

		long bigintspanset_width(Pointer ss, boolean boundspan);

		int dateset_end_value(Pointer s);

		int dateset_start_value(Pointer s);

		boolean dateset_value_n(Pointer s, int n, Pointer result);


		Pointer datespan_duration(Pointer s);

		int datespan_lower(Pointer s);

		int datespan_upper(Pointer s);

		boolean datespanset_date_n(Pointer ss, int n, Pointer result);

		Pointer datespanset_dates(Pointer ss);

		Pointer datespanset_duration(Pointer ss, boolean boundspan);

		int datespanset_end_date(Pointer ss);

		int datespanset_num_dates(Pointer ss);

		int datespanset_start_date(Pointer ss);

		double floatset_end_value(Pointer s);

		double floatset_start_value(Pointer s);

		boolean floatset_value_n(Pointer s, int n, Pointer result);


		double floatspan_lower(Pointer s);

		double floatspan_upper(Pointer s);

		double floatspan_width(Pointer s);

		double floatspanset_lower(Pointer ss);

		double floatspanset_upper(Pointer ss);

		double floatspanset_width(Pointer ss, boolean boundspan);

		int intset_end_value(Pointer s);

		int intset_start_value(Pointer s);

		boolean intset_value_n(Pointer s, int n, Pointer result);


		int intspan_lower(Pointer s);

		int intspan_upper(Pointer s);

		int intspan_width(Pointer s);

		int intspanset_lower(Pointer ss);

		int intspanset_upper(Pointer ss);

		int intspanset_width(Pointer ss, boolean boundspan);

		int set_hash(Pointer s);

		long set_hash_extended(Pointer s, long seed);

		int set_num_values(Pointer s);

		int span_hash(Pointer s);

		long span_hash_extended(Pointer s, long seed);

		boolean span_lower_inc(Pointer s);

		boolean span_upper_inc(Pointer s);

		Pointer spanset_end_span(Pointer ss);

		int spanset_hash(Pointer ss);

		long spanset_hash_extended(Pointer ss, long seed);

		boolean spanset_lower_inc(Pointer ss);

		int spanset_num_spans(Pointer ss);

		Pointer spanset_span(Pointer ss);

		Pointer spanset_span_n(Pointer ss, int i);


		Pointer spanset_start_span(Pointer ss);

		boolean spanset_upper_inc(Pointer ss);

		Pointer textset_end_value(Pointer s);

		Pointer textset_start_value(Pointer s);

		boolean textset_value_n(Pointer s, int n, Pointer result);


		long tstzset_end_value(Pointer s);

		long tstzset_start_value(Pointer s);

		boolean tstzset_value_n(Pointer s, int n, Pointer result);


		Pointer tstzspan_duration(Pointer s);

		long tstzspan_lower(Pointer s);

		long tstzspan_upper(Pointer s);

		Pointer tstzspanset_duration(Pointer ss, boolean boundspan);

		long tstzspanset_end_timestamptz(Pointer ss);

		long tstzspanset_lower(Pointer ss);

		int tstzspanset_num_timestamps(Pointer ss);

		long tstzspanset_start_timestamptz(Pointer ss);

		Pointer tstzspanset_timestamps(Pointer ss);

		boolean tstzspanset_timestamptz_n(Pointer ss, int n, Pointer result);

		long tstzspanset_upper(Pointer ss);

		Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer dateset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer datespan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer datespanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer floatset_ceil(Pointer s);

		Pointer floatset_degrees(Pointer s, boolean normalize);

		Pointer floatset_floor(Pointer s);

		Pointer floatset_radians(Pointer s);

		Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer floatspan_ceil(Pointer s);

		Pointer floatspan_degrees(Pointer s, boolean normalize);

		Pointer floatspan_floor(Pointer s);

		Pointer floatspan_radians(Pointer s);

		Pointer floatspan_round(Pointer s, int maxdd);

		Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer floatspanset_ceil(Pointer ss);

		Pointer floatspanset_floor(Pointer ss);

		Pointer floatspanset_degrees(Pointer ss, boolean normalize);

		Pointer floatspanset_radians(Pointer ss);

		Pointer floatspanset_round(Pointer ss, int maxdd);

		Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer tstzspan_expand(Pointer s, Pointer interv);

		Pointer set_round(Pointer s, int maxdd);

		Pointer textcat_text_textset(Pointer txt, Pointer s);

		Pointer textcat_textset_text(Pointer s, Pointer txt);

		Pointer textset_initcap(Pointer s);

		Pointer textset_lower(Pointer s);

		Pointer textset_upper(Pointer s);

		long timestamptz_tprecision(long t, Pointer duration, long torigin);

		Pointer tstzset_shift_scale(Pointer s, Pointer shift, Pointer duration);

		Pointer tstzset_tprecision(Pointer s, Pointer duration, long torigin);

		Pointer tstzspan_shift_scale(Pointer s, Pointer shift, Pointer duration);

		Pointer tstzspan_tprecision(Pointer s, Pointer duration, long torigin);

		Pointer tstzspanset_shift_scale(Pointer ss, Pointer shift, Pointer duration);

		Pointer tstzspanset_tprecision(Pointer ss, Pointer duration, long torigin);

		int set_cmp(Pointer s1, Pointer s2);

		boolean set_eq(Pointer s1, Pointer s2);

		boolean set_ge(Pointer s1, Pointer s2);

		boolean set_gt(Pointer s1, Pointer s2);

		boolean set_le(Pointer s1, Pointer s2);

		boolean set_lt(Pointer s1, Pointer s2);

		boolean set_ne(Pointer s1, Pointer s2);

		int span_cmp(Pointer s1, Pointer s2);

		boolean span_eq(Pointer s1, Pointer s2);

		boolean span_ge(Pointer s1, Pointer s2);

		boolean span_gt(Pointer s1, Pointer s2);

		boolean span_le(Pointer s1, Pointer s2);

		boolean span_lt(Pointer s1, Pointer s2);

		boolean span_ne(Pointer s1, Pointer s2);

		int spanset_cmp(Pointer ss1, Pointer ss2);

		boolean spanset_eq(Pointer ss1, Pointer ss2);

		boolean spanset_ge(Pointer ss1, Pointer ss2);

		boolean spanset_gt(Pointer ss1, Pointer ss2);

		boolean spanset_le(Pointer ss1, Pointer ss2);

		boolean spanset_lt(Pointer ss1, Pointer ss2);

		boolean spanset_ne(Pointer ss1, Pointer ss2);


		Pointer set_split_each_n_spans(Pointer s, int elems_per_span, Pointer count);

		Pointer set_split_n_spans(Pointer s, int span_count, Pointer count);


		Pointer spanset_split_each_n_spans(Pointer ss, int elems_per_span, Pointer count);

		Pointer spanset_split_n_spans(Pointer ss, int span_count, Pointer count);

		boolean adjacent_span_bigint(Pointer s, long i);

		boolean adjacent_span_date(Pointer s, int d);

		boolean adjacent_span_float(Pointer s, double d);

		boolean adjacent_span_int(Pointer s, int i);

		boolean adjacent_span_span(Pointer s1, Pointer s2);

		boolean adjacent_span_spanset(Pointer s, Pointer ss);

		boolean adjacent_span_timestamptz(Pointer s, long t);

		boolean adjacent_spanset_bigint(Pointer ss, long i);

		boolean adjacent_spanset_date(Pointer ss, int d);

		boolean adjacent_spanset_float(Pointer ss, double d);

		boolean adjacent_spanset_int(Pointer ss, int i);

		boolean adjacent_spanset_timestamptz(Pointer ss, long t);

		boolean adjacent_spanset_span(Pointer ss, Pointer s);

		boolean adjacent_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean contained_bigint_set(long i, Pointer s);

		boolean contained_bigint_span(long i, Pointer s);

		boolean contained_bigint_spanset(long i, Pointer ss);

		boolean contained_date_set(int d, Pointer s);

		boolean contained_date_span(int d, Pointer s);

		boolean contained_date_spanset(int d, Pointer ss);

		boolean contained_float_set(double d, Pointer s);

		boolean contained_float_span(double d, Pointer s);

		boolean contained_float_spanset(double d, Pointer ss);

		boolean contained_int_set(int i, Pointer s);

		boolean contained_int_span(int i, Pointer s);

		boolean contained_int_spanset(int i, Pointer ss);

		boolean contained_set_set(Pointer s1, Pointer s2);

		boolean contained_span_span(Pointer s1, Pointer s2);

		boolean contained_span_spanset(Pointer s, Pointer ss);

		boolean contained_spanset_span(Pointer ss, Pointer s);

		boolean contained_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean contained_text_set(Pointer txt, Pointer s);

		boolean contained_timestamptz_set(long t, Pointer s);

		boolean contained_timestamptz_span(long t, Pointer s);

		boolean contained_timestamptz_spanset(long t, Pointer ss);

		boolean contains_set_bigint(Pointer s, long i);

		boolean contains_set_date(Pointer s, int d);

		boolean contains_set_float(Pointer s, double d);

		boolean contains_set_int(Pointer s, int i);

		boolean contains_set_set(Pointer s1, Pointer s2);

		boolean contains_set_text(Pointer s, Pointer t);

		boolean contains_set_timestamptz(Pointer s, long t);

		boolean contains_span_bigint(Pointer s, long i);

		boolean contains_span_date(Pointer s, int d);

		boolean contains_span_float(Pointer s, double d);

		boolean contains_span_int(Pointer s, int i);

		boolean contains_span_span(Pointer s1, Pointer s2);

		boolean contains_span_spanset(Pointer s, Pointer ss);

		boolean contains_span_timestamptz(Pointer s, long t);

		boolean contains_spanset_bigint(Pointer ss, long i);

		boolean contains_spanset_date(Pointer ss, int d);

		boolean contains_spanset_float(Pointer ss, double d);

		boolean contains_spanset_int(Pointer ss, int i);

		boolean contains_spanset_span(Pointer ss, Pointer s);

		boolean contains_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean contains_spanset_timestamptz(Pointer ss, long t);

		boolean overlaps_set_set(Pointer s1, Pointer s2);

		boolean overlaps_span_span(Pointer s1, Pointer s2);

		boolean overlaps_span_spanset(Pointer s, Pointer ss);

		boolean overlaps_spanset_span(Pointer ss, Pointer s);

		boolean overlaps_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean after_date_set(int d, Pointer s);

		boolean after_date_span(int d, Pointer s);

		boolean after_date_spanset(int d, Pointer ss);

		boolean after_set_date(Pointer s, int d);

		boolean after_set_timestamptz(Pointer s, long t);

		boolean after_span_date(Pointer s, int d);

		boolean after_span_timestamptz(Pointer s, long t);

		boolean after_spanset_date(Pointer ss, int d);

		boolean after_spanset_timestamptz(Pointer ss, long t);

		boolean after_timestamptz_set(long t, Pointer s);

		boolean after_timestamptz_span(long t, Pointer s);

		boolean after_timestamptz_spanset(long t, Pointer ss);

		boolean before_date_set(int d, Pointer s);

		boolean before_date_span(int d, Pointer s);

		boolean before_date_spanset(int d, Pointer ss);

		boolean before_set_date(Pointer s, int d);

		boolean before_set_timestamptz(Pointer s, long t);

		boolean before_span_date(Pointer s, int d);

		boolean before_span_timestamptz(Pointer s, long t);

		boolean before_spanset_date(Pointer ss, int d);

		boolean before_spanset_timestamptz(Pointer ss, long t);

		boolean before_timestamptz_set(long t, Pointer s);

		boolean before_timestamptz_span(long t, Pointer s);

		boolean before_timestamptz_spanset(long t, Pointer ss);

		boolean left_bigint_set(long i, Pointer s);

		boolean left_bigint_span(long i, Pointer s);

		boolean left_bigint_spanset(long i, Pointer ss);

		boolean left_float_set(double d, Pointer s);

		boolean left_float_span(double d, Pointer s);

		boolean left_float_spanset(double d, Pointer ss);

		boolean left_int_set(int i, Pointer s);

		boolean left_int_span(int i, Pointer s);

		boolean left_int_spanset(int i, Pointer ss);

		boolean left_set_bigint(Pointer s, long i);

		boolean left_set_float(Pointer s, double d);

		boolean left_set_int(Pointer s, int i);

		boolean left_set_set(Pointer s1, Pointer s2);

		boolean left_set_text(Pointer s, Pointer txt);

		boolean left_span_bigint(Pointer s, long i);

		boolean left_span_float(Pointer s, double d);

		boolean left_span_int(Pointer s, int i);

		boolean left_span_span(Pointer s1, Pointer s2);

		boolean left_span_spanset(Pointer s, Pointer ss);

		boolean left_spanset_bigint(Pointer ss, long i);

		boolean left_spanset_float(Pointer ss, double d);

		boolean left_spanset_int(Pointer ss, int i);

		boolean left_spanset_span(Pointer ss, Pointer s);

		boolean left_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean left_text_set(Pointer txt, Pointer s);

		boolean overafter_date_set(int d, Pointer s);

		boolean overafter_date_span(int d, Pointer s);

		boolean overafter_date_spanset(int d, Pointer ss);

		boolean overafter_set_date(Pointer s, int d);

		boolean overafter_set_timestamptz(Pointer s, long t);

		boolean overafter_span_date(Pointer s, int d);

		boolean overafter_span_timestamptz(Pointer s, long t);

		boolean overafter_spanset_date(Pointer ss, int d);

		boolean overafter_spanset_timestamptz(Pointer ss, long t);

		boolean overafter_timestamptz_set(long t, Pointer s);

		boolean overafter_timestamptz_span(long t, Pointer s);

		boolean overafter_timestamptz_spanset(long t, Pointer ss);

		boolean overbefore_date_set(int d, Pointer s);

		boolean overbefore_date_span(int d, Pointer s);

		boolean overbefore_date_spanset(int d, Pointer ss);

		boolean overbefore_set_date(Pointer s, int d);

		boolean overbefore_set_timestamptz(Pointer s, long t);

		boolean overbefore_span_date(Pointer s, int d);

		boolean overbefore_span_timestamptz(Pointer s, long t);

		boolean overbefore_spanset_date(Pointer ss, int d);

		boolean overbefore_spanset_timestamptz(Pointer ss, long t);

		boolean overbefore_timestamptz_set(long t, Pointer s);

		boolean overbefore_timestamptz_span(long t, Pointer s);

		boolean overbefore_timestamptz_spanset(long t, Pointer ss);

		boolean overleft_bigint_set(long i, Pointer s);

		boolean overleft_bigint_span(long i, Pointer s);

		boolean overleft_bigint_spanset(long i, Pointer ss);

		boolean overleft_float_set(double d, Pointer s);

		boolean overleft_float_span(double d, Pointer s);

		boolean overleft_float_spanset(double d, Pointer ss);

		boolean overleft_int_set(int i, Pointer s);

		boolean overleft_int_span(int i, Pointer s);

		boolean overleft_int_spanset(int i, Pointer ss);

		boolean overleft_set_bigint(Pointer s, long i);

		boolean overleft_set_float(Pointer s, double d);

		boolean overleft_set_int(Pointer s, int i);

		boolean overleft_set_set(Pointer s1, Pointer s2);

		boolean overleft_set_text(Pointer s, Pointer txt);

		boolean overleft_span_bigint(Pointer s, long i);

		boolean overleft_span_float(Pointer s, double d);

		boolean overleft_span_int(Pointer s, int i);

		boolean overleft_span_span(Pointer s1, Pointer s2);

		boolean overleft_span_spanset(Pointer s, Pointer ss);

		boolean overleft_spanset_bigint(Pointer ss, long i);

		boolean overleft_spanset_float(Pointer ss, double d);

		boolean overleft_spanset_int(Pointer ss, int i);

		boolean overleft_spanset_span(Pointer ss, Pointer s);

		boolean overleft_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean overleft_text_set(Pointer txt, Pointer s);

		boolean overright_bigint_set(long i, Pointer s);

		boolean overright_bigint_span(long i, Pointer s);

		boolean overright_bigint_spanset(long i, Pointer ss);

		boolean overright_float_set(double d, Pointer s);

		boolean overright_float_span(double d, Pointer s);

		boolean overright_float_spanset(double d, Pointer ss);

		boolean overright_int_set(int i, Pointer s);

		boolean overright_int_span(int i, Pointer s);

		boolean overright_int_spanset(int i, Pointer ss);

		boolean overright_set_bigint(Pointer s, long i);

		boolean overright_set_float(Pointer s, double d);

		boolean overright_set_int(Pointer s, int i);

		boolean overright_set_set(Pointer s1, Pointer s2);

		boolean overright_set_text(Pointer s, Pointer txt);

		boolean overright_span_bigint(Pointer s, long i);

		boolean overright_span_float(Pointer s, double d);

		boolean overright_span_int(Pointer s, int i);

		boolean overright_span_span(Pointer s1, Pointer s2);

		boolean overright_span_spanset(Pointer s, Pointer ss);

		boolean overright_spanset_bigint(Pointer ss, long i);

		boolean overright_spanset_float(Pointer ss, double d);

		boolean overright_spanset_int(Pointer ss, int i);

		boolean overright_spanset_span(Pointer ss, Pointer s);

		boolean overright_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean overright_text_set(Pointer txt, Pointer s);

		boolean right_bigint_set(long i, Pointer s);

		boolean right_bigint_span(long i, Pointer s);

		boolean right_bigint_spanset(long i, Pointer ss);

		boolean right_float_set(double d, Pointer s);

		boolean right_float_span(double d, Pointer s);

		boolean right_float_spanset(double d, Pointer ss);

		boolean right_int_set(int i, Pointer s);

		boolean right_int_span(int i, Pointer s);

		boolean right_int_spanset(int i, Pointer ss);

		boolean right_set_bigint(Pointer s, long i);

		boolean right_set_float(Pointer s, double d);

		boolean right_set_int(Pointer s, int i);

		boolean right_set_set(Pointer s1, Pointer s2);

		boolean right_set_text(Pointer s, Pointer txt);

		boolean right_span_bigint(Pointer s, long i);

		boolean right_span_float(Pointer s, double d);

		boolean right_span_int(Pointer s, int i);

		boolean right_span_span(Pointer s1, Pointer s2);

		boolean right_span_spanset(Pointer s, Pointer ss);

		boolean right_spanset_bigint(Pointer ss, long i);

		boolean right_spanset_float(Pointer ss, double d);

		boolean right_spanset_int(Pointer ss, int i);

		boolean right_spanset_span(Pointer ss, Pointer s);

		boolean right_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean right_text_set(Pointer txt, Pointer s);

		Pointer intersection_bigint_set(long i, Pointer s);

		Pointer intersection_date_set(int d, Pointer s);

		Pointer intersection_float_set(double d, Pointer s);

		Pointer intersection_int_set(int i, Pointer s);

		Pointer intersection_set_bigint(Pointer s, long i);

		Pointer intersection_set_date(Pointer s, int d);

		Pointer intersection_set_float(Pointer s, double d);

		Pointer intersection_set_int(Pointer s, int i);

		Pointer intersection_set_set(Pointer s1, Pointer s2);

		Pointer intersection_set_text(Pointer s, Pointer txt);

		Pointer intersection_set_timestamptz(Pointer s, long t);

		Pointer intersection_span_bigint(Pointer s, long i);

		Pointer intersection_span_date(Pointer s, int d);

		Pointer intersection_span_float(Pointer s, double d);

		Pointer intersection_span_int(Pointer s, int i);

		Pointer intersection_span_span(Pointer s1, Pointer s2);

		Pointer intersection_span_spanset(Pointer s, Pointer ss);

		Pointer intersection_span_timestamptz(Pointer s, long t);

		Pointer intersection_spanset_bigint(Pointer ss, long i);

		Pointer intersection_spanset_date(Pointer ss, int d);

		Pointer intersection_spanset_float(Pointer ss, double d);

		Pointer intersection_spanset_int(Pointer ss, int i);

		Pointer intersection_spanset_span(Pointer ss, Pointer s);

		Pointer intersection_spanset_spanset(Pointer ss1, Pointer ss2);

		Pointer intersection_spanset_timestamptz(Pointer ss, long t);

		Pointer intersection_text_set(Pointer txt, Pointer s);

		Pointer intersection_timestamptz_set(long t, Pointer s);

		Pointer minus_bigint_set(long i, Pointer s);

		Pointer minus_bigint_span(long i, Pointer s);

		Pointer minus_bigint_spanset(long i, Pointer ss);

		Pointer minus_date_set(int d, Pointer s);

		Pointer minus_date_span(int d, Pointer s);

		Pointer minus_date_spanset(int d, Pointer ss);

		Pointer minus_float_set(double d, Pointer s);

		Pointer minus_float_span(double d, Pointer s);

		Pointer minus_float_spanset(double d, Pointer ss);

		Pointer minus_int_set(int i, Pointer s);

		Pointer minus_int_span(int i, Pointer s);

		Pointer minus_int_spanset(int i, Pointer ss);

		Pointer minus_set_bigint(Pointer s, long i);

		Pointer minus_set_date(Pointer s, int d);

		Pointer minus_set_float(Pointer s, double d);

		Pointer minus_set_int(Pointer s, int i);

		Pointer minus_set_set(Pointer s1, Pointer s2);

		Pointer minus_set_text(Pointer s, Pointer txt);

		Pointer minus_set_timestamptz(Pointer s, long t);

		Pointer minus_span_bigint(Pointer s, long i);

		Pointer minus_span_date(Pointer s, int d);

		Pointer minus_span_float(Pointer s, double d);

		Pointer minus_span_int(Pointer s, int i);

		Pointer minus_span_span(Pointer s1, Pointer s2);

		Pointer minus_span_spanset(Pointer s, Pointer ss);

		Pointer minus_span_timestamptz(Pointer s, long t);

		Pointer minus_spanset_bigint(Pointer ss, long i);

		Pointer minus_spanset_date(Pointer ss, int d);

		Pointer minus_spanset_float(Pointer ss, double d);

		Pointer minus_spanset_int(Pointer ss, int i);

		Pointer minus_spanset_span(Pointer ss, Pointer s);

		Pointer minus_spanset_spanset(Pointer ss1, Pointer ss2);

		Pointer minus_spanset_timestamptz(Pointer ss, long t);

		Pointer minus_text_set(Pointer txt, Pointer s);

		Pointer minus_timestamptz_set(long t, Pointer s);

		Pointer minus_timestamptz_span(long t, Pointer s);

		Pointer minus_timestamptz_spanset(long t, Pointer ss);

		Pointer union_bigint_set(long i, Pointer s);

		Pointer union_bigint_span(Pointer s, long i);

		Pointer union_bigint_spanset(long i, Pointer ss);

		Pointer union_date_set(int d, Pointer s);

		Pointer union_date_span(Pointer s, int d);

		Pointer union_date_spanset(int d, Pointer ss);

		Pointer union_float_set(double d, Pointer s);

		Pointer union_float_span(Pointer s, double d);

		Pointer union_float_spanset(double d, Pointer ss);

		Pointer union_int_set(int i, Pointer s);

		Pointer union_int_span(int i, Pointer s);

		Pointer union_int_spanset(int i, Pointer ss);

		Pointer union_set_bigint(Pointer s, long i);

		Pointer union_set_date(Pointer s, int d);

		Pointer union_set_float(Pointer s, double d);

		Pointer union_set_int(Pointer s, int i);

		Pointer union_set_set(Pointer s1, Pointer s2);

		Pointer union_set_text(Pointer s, Pointer txt);

		Pointer union_set_timestamptz(Pointer s, long t);

		Pointer union_span_bigint(Pointer s, long i);

		Pointer union_span_date(Pointer s, int d);

		Pointer union_span_float(Pointer s, double d);

		Pointer union_span_int(Pointer s, int i);

		Pointer union_span_span(Pointer s1, Pointer s2);

		Pointer union_span_spanset(Pointer s, Pointer ss);

		Pointer union_span_timestamptz(Pointer s, long t);

		Pointer union_spanset_bigint(Pointer ss, long i);

		Pointer union_spanset_date(Pointer ss, int d);

		Pointer union_spanset_float(Pointer ss, double d);

		Pointer union_spanset_int(Pointer ss, int i);

		Pointer union_spanset_span(Pointer ss, Pointer s);

		Pointer union_spanset_spanset(Pointer ss1, Pointer ss2);

		Pointer union_spanset_timestamptz(Pointer ss, long t);

		Pointer union_text_set(Pointer txt, Pointer s);

		Pointer union_timestamptz_set(long t, Pointer s);

		Pointer union_timestamptz_span(long t, Pointer s);

		Pointer union_timestamptz_spanset(long t, Pointer ss);

		long distance_bigintset_bigintset(Pointer s1, Pointer s2);

		long distance_bigintspan_bigintspan(Pointer s1, Pointer s2);

		long distance_bigintspanset_bigintspan(Pointer ss, Pointer s);

		long distance_bigintspanset_bigintspanset(Pointer ss1, Pointer ss2);

		int distance_dateset_dateset(Pointer s1, Pointer s2);

		int distance_datespan_datespan(Pointer s1, Pointer s2);

		int distance_datespanset_datespan(Pointer ss, Pointer s);

		int distance_datespanset_datespanset(Pointer ss1, Pointer ss2);

		double distance_floatset_floatset(Pointer s1, Pointer s2);

		double distance_floatspan_floatspan(Pointer s1, Pointer s2);

		double distance_floatspanset_floatspan(Pointer ss, Pointer s);

		double distance_floatspanset_floatspanset(Pointer ss1, Pointer ss2);

		int distance_intset_intset(Pointer s1, Pointer s2);

		int distance_intspan_intspan(Pointer s1, Pointer s2);

		int distance_intspanset_intspan(Pointer ss, Pointer s);

		int distance_intspanset_intspanset(Pointer ss1, Pointer ss2);

		long distance_set_bigint(Pointer s, long i);

		int distance_set_date(Pointer s, int d);

		double distance_set_float(Pointer s, double d);

		int distance_set_int(Pointer s, int i);

		double distance_set_timestamptz(Pointer s, long t);

		long distance_span_bigint(Pointer s, long i);

		int distance_span_date(Pointer s, int d);

		double distance_span_float(Pointer s, double d);

		int distance_span_int(Pointer s, int i);

		double distance_span_timestamptz(Pointer s, long t);

		long distance_spanset_bigint(Pointer ss, long i);

		int distance_spanset_date(Pointer ss, int d);

		double distance_spanset_float(Pointer ss, double d);

		int distance_spanset_int(Pointer ss, int i);

		double distance_spanset_timestamptz(Pointer ss, long t);

		double distance_tstzset_tstzset(Pointer s1, Pointer s2);

		double distance_tstzspan_tstzspan(Pointer s1, Pointer s2);

		double distance_tstzspanset_tstzspan(Pointer ss, Pointer s);

		double distance_tstzspanset_tstzspanset(Pointer ss1, Pointer ss2);

		Pointer bigint_extent_transfn(Pointer state, long i);

		Pointer bigint_union_transfn(Pointer state, long i);

		Pointer date_extent_transfn(Pointer state, int d);

		Pointer date_union_transfn(Pointer state, int d);

		Pointer float_extent_transfn(Pointer state, double d);

		Pointer float_union_transfn(Pointer state, double d);

		Pointer int_extent_transfn(Pointer state, int i);

		Pointer int_union_transfn(Pointer state, int i);

		Pointer set_extent_transfn(Pointer state, Pointer s);

		Pointer set_union_finalfn(Pointer state);

		Pointer set_union_transfn(Pointer state, Pointer s);

		Pointer span_extent_transfn(Pointer state, Pointer s);

		Pointer span_union_transfn(Pointer state, Pointer s);

		Pointer spanset_extent_transfn(Pointer state, Pointer ss);

		Pointer spanset_union_finalfn(Pointer state);

		Pointer spanset_union_transfn(Pointer state, Pointer ss);

		Pointer text_union_transfn(Pointer state, Pointer txt);

		Pointer timestamptz_extent_transfn(Pointer state, long t);

		Pointer timestamptz_union_transfn(Pointer state, long t);

		long bigint_get_bin(long value, long vsize, long vorigin);

		Pointer bigintspan_bins(Pointer s, long vsize, long vorigin, Pointer count);

		Pointer bigintspanset_bins(Pointer ss, long vsize, long vorigin, Pointer count);

		int date_get_bin(int d, Pointer duration, int torigin);

		Pointer datespan_bins(Pointer s, Pointer duration, int torigin, Pointer count);

		Pointer datespanset_bins(Pointer ss, Pointer duration, int torigin, Pointer count);

		double float_get_bin(double value, double vsize, double vorigin);

		Pointer floatspan_bins(Pointer s, double vsize, double vorigin, Pointer count);

		Pointer floatspanset_bins(Pointer ss, double vsize, double vorigin, Pointer count);

		int int_get_bin(int value, int vsize, int vorigin);

		Pointer intspan_bins(Pointer s, int vsize, int vorigin, Pointer count);

		Pointer intspanset_bins(Pointer ss, int vsize, int vorigin, Pointer count);

		long timestamptz_get_bin(long t, Pointer duration, long torigin);

		Pointer tstzspan_bins(Pointer s, Pointer duration, long origin, Pointer count);

		Pointer tstzspanset_bins(Pointer ss, Pointer duration, long torigin, Pointer count);

		String tbox_as_hexwkb(Pointer box, byte variant, Pointer size);

		Pointer tbox_as_wkb(Pointer box, byte variant, Pointer size_out);

		Pointer tbox_from_hexwkb(String hexwkb);

		Pointer tbox_from_wkb(Pointer wkb, long size);

		Pointer tbox_in(String str);

		String tbox_out(Pointer box, int maxdd);

		Pointer float_timestamptz_to_tbox(double d, long t);

		Pointer float_tstzspan_to_tbox(double d, Pointer s);

		Pointer int_timestamptz_to_tbox(int i, long t);

		Pointer int_tstzspan_to_tbox(int i, Pointer s);

		Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s);

		Pointer numspan_timestamptz_to_tbox(Pointer span, long t);

		Pointer tbox_copy(Pointer box);

		Pointer tbox_make(Pointer s, Pointer p);

		Pointer float_to_tbox(double d);

		Pointer int_to_tbox(int i);

		Pointer set_to_tbox(Pointer s);

		Pointer span_to_tbox(Pointer s);

		Pointer spanset_to_tbox(Pointer ss);

		Pointer tbox_to_intspan(Pointer box);

		Pointer tbox_to_floatspan(Pointer box);

		Pointer tbox_to_tstzspan(Pointer box);

		Pointer timestamptz_to_tbox(long t);

		int tbox_hash(Pointer box);

		long tbox_hash_extended(Pointer box, long seed);

		boolean tbox_hast(Pointer box);

		boolean tbox_hasx(Pointer box);

		boolean tbox_tmax(Pointer box, Pointer result);

		boolean tbox_tmax_inc(Pointer box, Pointer result);

		boolean tbox_tmin(Pointer box, Pointer result);

		boolean tbox_tmin_inc(Pointer box, Pointer result);

		boolean tbox_xmax(Pointer box, Pointer result);

		boolean tbox_xmax_inc(Pointer box, Pointer result);

		boolean tbox_xmin(Pointer box, Pointer result);

		boolean tbox_xmin_inc(Pointer box, Pointer result);

		boolean tboxfloat_xmax(Pointer box, Pointer result);

		boolean tboxfloat_xmin(Pointer box, Pointer result);

		boolean tboxint_xmax(Pointer box, Pointer result);

		boolean tboxint_xmin(Pointer box, Pointer result);

		Pointer tbox_expand_time(Pointer box, Pointer interv);

		Pointer tbox_round(Pointer box, int maxdd);

		Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);

		Pointer tfloatbox_expand(Pointer box, double d);

		Pointer tfloatbox_shift_scale(Pointer box, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer tintbox_expand(Pointer box, int i);

		Pointer tintbox_shift_scale(Pointer box, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict);

		Pointer intersection_tbox_tbox(Pointer box1, Pointer box2);

		boolean adjacent_tbox_tbox(Pointer box1, Pointer box2);

		boolean contained_tbox_tbox(Pointer box1, Pointer box2);

		boolean contains_tbox_tbox(Pointer box1, Pointer box2);

		boolean overlaps_tbox_tbox(Pointer box1, Pointer box2);

		boolean same_tbox_tbox(Pointer box1, Pointer box2);

		boolean after_tbox_tbox(Pointer box1, Pointer box2);

		boolean before_tbox_tbox(Pointer box1, Pointer box2);

		boolean left_tbox_tbox(Pointer box1, Pointer box2);

		boolean overafter_tbox_tbox(Pointer box1, Pointer box2);

		boolean overbefore_tbox_tbox(Pointer box1, Pointer box2);

		boolean overleft_tbox_tbox(Pointer box1, Pointer box2);

		boolean overright_tbox_tbox(Pointer box1, Pointer box2);

		boolean right_tbox_tbox(Pointer box1, Pointer box2);

		int tbox_cmp(Pointer box1, Pointer box2);

		boolean tbox_eq(Pointer box1, Pointer box2);

		boolean tbox_ge(Pointer box1, Pointer box2);

		boolean tbox_gt(Pointer box1, Pointer box2);

		boolean tbox_le(Pointer box1, Pointer box2);

		boolean tbox_lt(Pointer box1, Pointer box2);

		boolean tbox_ne(Pointer box1, Pointer box2);

		Pointer tbool_from_mfjson(String str);

		Pointer tbool_in(String str);

		String tbool_out(Pointer temp);

		String temporal_as_hexwkb(Pointer temp, byte variant, Pointer size_out);

		String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs);

		Pointer temporal_as_wkb(Pointer temp, byte variant, Pointer size_out);

		Pointer temporal_from_hexwkb(String hexwkb);

		Pointer temporal_from_wkb(Pointer wkb, long size);

		Pointer tfloat_from_mfjson(String str);

		Pointer tfloat_in(String str);

		String tfloat_out(Pointer temp, int maxdd);

		Pointer tint_from_mfjson(String str);

		Pointer tint_in(String str);

		String tint_out(Pointer temp);

		Pointer ttext_from_mfjson(String str);

		Pointer ttext_in(String str);

		String ttext_out(Pointer temp);

		Pointer tbool_from_base_temp(boolean b, Pointer temp);

		Pointer tboolinst_make(boolean b, long t);

		Pointer tboolseq_from_base_tstzset(boolean b, Pointer s);

		Pointer tboolseq_from_base_tstzspan(boolean b, Pointer s);

		Pointer tboolseqset_from_base_tstzspanset(boolean b, Pointer ss);

		Pointer temporal_copy(Pointer temp);

		Pointer tfloat_from_base_temp(double d, Pointer temp);

		Pointer tfloatinst_make(double d, long t);

		Pointer tfloatseq_from_base_tstzset(double d, Pointer s);

		Pointer tfloatseq_from_base_tstzspan(double d, Pointer s, int interp);

		Pointer tfloatseqset_from_base_tstzspanset(double d, Pointer ss, int interp);

		Pointer tint_from_base_temp(int i, Pointer temp);

		Pointer tintinst_make(int i, long t);

		Pointer tintseq_from_base_tstzset(int i, Pointer s);

		Pointer tintseq_from_base_tstzspan(int i, Pointer s);

		Pointer tintseqset_from_base_tstzspanset(int i, Pointer ss);

		Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize);

		Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist);

		Pointer ttext_from_base_temp(Pointer txt, Pointer temp);

		Pointer ttextinst_make(Pointer txt, long t);

		Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s);

		Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s);

		Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss);

		Pointer tbool_to_tint(Pointer temp);

		Pointer temporal_to_tstzspan(Pointer temp);

		Pointer tfloat_to_tint(Pointer temp);

		Pointer tint_to_tfloat(Pointer temp);

		Pointer tnumber_to_span(Pointer temp);

		Pointer tnumber_to_tbox(Pointer temp);

		boolean tbool_end_value(Pointer temp);

		boolean tbool_start_value(Pointer temp);

		boolean tbool_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean tbool_value_n(Pointer temp, int n, Pointer result);

		Pointer tbool_values(Pointer temp, Pointer count);

		Pointer temporal_duration(Pointer temp, boolean boundspan);

		Pointer temporal_end_instant(Pointer temp);

		Pointer temporal_end_sequence(Pointer temp);

		long temporal_end_timestamptz(Pointer temp);

		int temporal_hash(Pointer temp);

		Pointer temporal_instant_n(Pointer temp, int n);

		Pointer temporal_instants(Pointer temp, Pointer count);

		String temporal_interp(Pointer temp);

		boolean temporal_lower_inc(Pointer temp);

		Pointer temporal_max_instant(Pointer temp);

		Pointer temporal_min_instant(Pointer temp);

		int temporal_num_instants(Pointer temp);

		int temporal_num_sequences(Pointer temp);

		int temporal_num_timestamps(Pointer temp);

		Pointer temporal_segm_duration(Pointer temp, Pointer duration, boolean atleast, boolean strict);

		Pointer temporal_segments(Pointer temp, Pointer count);

		Pointer temporal_sequence_n(Pointer temp, int i);

		Pointer temporal_sequences(Pointer temp, Pointer count);

		Pointer temporal_start_instant(Pointer temp);

		Pointer temporal_start_sequence(Pointer temp);

		long temporal_start_timestamptz(Pointer temp);

		Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration);

		String temporal_subtype(Pointer temp);

		Pointer temporal_time(Pointer temp);

		Pointer temporal_timestamps(Pointer temp, Pointer count);

		boolean temporal_timestamptz_n(Pointer temp, int n, Pointer result);

		boolean temporal_upper_inc(Pointer temp);


		double tfloat_end_value(Pointer temp);

		double tfloat_min_value(Pointer temp);

		double tfloat_max_value(Pointer temp);

		double tfloat_start_value(Pointer temp);

		boolean tfloat_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean tfloat_value_n(Pointer temp, int n, Pointer result);

		Pointer tfloat_values(Pointer temp, Pointer count);

		int tint_end_value(Pointer temp);

		int tint_max_value(Pointer temp);

		int tint_min_value(Pointer temp);

		int tint_start_value(Pointer temp);

		boolean tint_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean tint_value_n(Pointer temp, int n, Pointer result);

		Pointer tint_values(Pointer temp, Pointer count);

		double tnumber_avg_value(Pointer temp);

		double tnumber_integral(Pointer temp);

		double tnumber_twavg(Pointer temp);

		Pointer tnumber_valuespans(Pointer temp);

		Pointer ttext_end_value(Pointer temp);

		Pointer ttext_max_value(Pointer temp);

		Pointer ttext_min_value(Pointer temp);

		Pointer ttext_start_value(Pointer temp);

		boolean ttext_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean ttext_value_n(Pointer temp, int n, Pointer result);

		Pointer ttext_values(Pointer temp, Pointer count);

		double float_degrees(double value, boolean normalize);

		Pointer temparr_round(Pointer temp, int count, int maxdd);

		Pointer temporal_round(Pointer temp, int maxdd);

		Pointer temporal_scale_time(Pointer temp, Pointer duration);

		Pointer temporal_set_interp(Pointer temp, int interp);

		Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration);

		Pointer temporal_shift_time(Pointer temp, Pointer shift);

		Pointer temporal_as_tinstant(Pointer temp);

		Pointer temporal_tsequence(Pointer temp, int interp);

		Pointer temporal_tsequenceset(Pointer temp, int interp);

		Pointer tfloat_ceil(Pointer temp);

		Pointer tfloat_degrees(Pointer temp, boolean normalize);

		Pointer tfloat_floor(Pointer temp);

		Pointer tfloat_radians(Pointer temp);

		Pointer tfloat_scale_value(Pointer temp, double width);

		Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width);

		Pointer tfloat_shift_value(Pointer temp, double shift);

		Pointer tint_scale_value(Pointer temp, int width);

		Pointer tint_shift_scale_value(Pointer temp, int shift, int width);

		Pointer tint_shift_value(Pointer temp, int shift);

		Pointer temporal_append_tinstant(Pointer temp, Pointer inst, int interp, double maxdist, Pointer maxt, boolean expand);

		Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand);

		Pointer temporal_delete_timestamptz(Pointer temp, long t, boolean connect);

		Pointer temporal_delete_tstzset(Pointer temp, Pointer s, boolean connect);

		Pointer temporal_delete_tstzspan(Pointer temp, Pointer s, boolean connect);

		Pointer temporal_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect);

		Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect);

		Pointer temporal_merge(Pointer temp1, Pointer temp2);

		Pointer temporal_merge_array(Pointer temparr, int count);

		Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect);

		Pointer tbool_at_value(Pointer temp, boolean b);

		Pointer tbool_minus_value(Pointer temp, boolean b);

		Pointer temporal_after_timestamptz(Pointer temp, long t, boolean strict);

		Pointer temporal_at_max(Pointer temp);

		Pointer temporal_at_min(Pointer temp);

		Pointer temporal_at_timestamptz(Pointer temp, long t);

		Pointer temporal_at_tstzset(Pointer temp, Pointer s);

		Pointer temporal_at_tstzspan(Pointer temp, Pointer s);

		Pointer temporal_at_tstzspanset(Pointer temp, Pointer ss);

		Pointer temporal_at_values(Pointer temp, Pointer set);

		Pointer temporal_before_timestamptz(Pointer temp, long t, boolean strict);

		Pointer temporal_minus_max(Pointer temp);

		Pointer temporal_minus_min(Pointer temp);

		Pointer temporal_minus_timestamptz(Pointer temp, long t);

		Pointer temporal_minus_tstzset(Pointer temp, Pointer s);

		Pointer temporal_minus_tstzspan(Pointer temp, Pointer s);

		Pointer temporal_minus_tstzspanset(Pointer temp, Pointer ss);

		Pointer temporal_minus_values(Pointer temp, Pointer set);

		Pointer tfloat_at_value(Pointer temp, double d);

		Pointer tfloat_minus_value(Pointer temp, double d);

		Pointer tint_at_value(Pointer temp, int i);

		Pointer tint_minus_value(Pointer temp, int i);

		Pointer tnumber_at_span(Pointer temp, Pointer span);

		Pointer tnumber_at_spanset(Pointer temp, Pointer ss);

		Pointer tnumber_at_tbox(Pointer temp, Pointer box);

		Pointer tnumber_minus_span(Pointer temp, Pointer span);

		Pointer tnumber_minus_spanset(Pointer temp, Pointer ss);

		Pointer tnumber_minus_tbox(Pointer temp, Pointer box);

		Pointer ttext_at_value(Pointer temp, Pointer txt);

		Pointer ttext_minus_value(Pointer temp, Pointer txt);

		int temporal_cmp(Pointer temp1, Pointer temp2);

		boolean temporal_eq(Pointer temp1, Pointer temp2);

		boolean temporal_ge(Pointer temp1, Pointer temp2);

		boolean temporal_gt(Pointer temp1, Pointer temp2);

		boolean temporal_le(Pointer temp1, Pointer temp2);

		boolean temporal_lt(Pointer temp1, Pointer temp2);

		boolean temporal_ne(Pointer temp1, Pointer temp2);

		int always_eq_bool_tbool(boolean b, Pointer temp);

		int always_eq_float_tfloat(double d, Pointer temp);

		int always_eq_int_tint(int i, Pointer temp);

		int always_eq_tbool_bool(Pointer temp, boolean b);

		int always_eq_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_eq_text_ttext(Pointer txt, Pointer temp);

		int always_eq_tfloat_float(Pointer temp, double d);

		int always_eq_tint_int(Pointer temp, int i);

		int always_eq_ttext_text(Pointer temp, Pointer txt);

		int always_ge_float_tfloat(double d, Pointer temp);

		int always_ge_int_tint(int i, Pointer temp);

		int always_ge_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_ge_text_ttext(Pointer txt, Pointer temp);

		int always_ge_tfloat_float(Pointer temp, double d);

		int always_ge_tint_int(Pointer temp, int i);

		int always_ge_ttext_text(Pointer temp, Pointer txt);

		int always_gt_float_tfloat(double d, Pointer temp);

		int always_gt_int_tint(int i, Pointer temp);

		int always_gt_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_gt_text_ttext(Pointer txt, Pointer temp);

		int always_gt_tfloat_float(Pointer temp, double d);

		int always_gt_tint_int(Pointer temp, int i);

		int always_gt_ttext_text(Pointer temp, Pointer txt);

		int always_le_float_tfloat(double d, Pointer temp);

		int always_le_int_tint(int i, Pointer temp);

		int always_le_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_le_text_ttext(Pointer txt, Pointer temp);

		int always_le_tfloat_float(Pointer temp, double d);

		int always_le_tint_int(Pointer temp, int i);

		int always_le_ttext_text(Pointer temp, Pointer txt);

		int always_lt_float_tfloat(double d, Pointer temp);

		int always_lt_int_tint(int i, Pointer temp);

		int always_lt_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_lt_text_ttext(Pointer txt, Pointer temp);

		int always_lt_tfloat_float(Pointer temp, double d);

		int always_lt_tint_int(Pointer temp, int i);

		int always_lt_ttext_text(Pointer temp, Pointer txt);

		int always_ne_bool_tbool(boolean b, Pointer temp);

		int always_ne_float_tfloat(double d, Pointer temp);

		int always_ne_int_tint(int i, Pointer temp);

		int always_ne_tbool_bool(Pointer temp, boolean b);

		int always_ne_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_ne_text_ttext(Pointer txt, Pointer temp);

		int always_ne_tfloat_float(Pointer temp, double d);

		int always_ne_tint_int(Pointer temp, int i);

		int always_ne_ttext_text(Pointer temp, Pointer txt);

		int ever_eq_bool_tbool(boolean b, Pointer temp);

		int ever_eq_float_tfloat(double d, Pointer temp);

		int ever_eq_int_tint(int i, Pointer temp);

		int ever_eq_tbool_bool(Pointer temp, boolean b);

		int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_eq_text_ttext(Pointer txt, Pointer temp);

		int ever_eq_tfloat_float(Pointer temp, double d);

		int ever_eq_tint_int(Pointer temp, int i);

		int ever_eq_ttext_text(Pointer temp, Pointer txt);

		int ever_ge_float_tfloat(double d, Pointer temp);

		int ever_ge_int_tint(int i, Pointer temp);

		int ever_ge_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_ge_text_ttext(Pointer txt, Pointer temp);

		int ever_ge_tfloat_float(Pointer temp, double d);

		int ever_ge_tint_int(Pointer temp, int i);

		int ever_ge_ttext_text(Pointer temp, Pointer txt);

		int ever_gt_float_tfloat(double d, Pointer temp);

		int ever_gt_int_tint(int i, Pointer temp);

		int ever_gt_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_gt_text_ttext(Pointer txt, Pointer temp);

		int ever_gt_tfloat_float(Pointer temp, double d);

		int ever_gt_tint_int(Pointer temp, int i);

		int ever_gt_ttext_text(Pointer temp, Pointer txt);

		int ever_le_float_tfloat(double d, Pointer temp);

		int ever_le_int_tint(int i, Pointer temp);

		int ever_le_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_le_text_ttext(Pointer txt, Pointer temp);

		int ever_le_tfloat_float(Pointer temp, double d);

		int ever_le_tint_int(Pointer temp, int i);

		int ever_le_ttext_text(Pointer temp, Pointer txt);

		int ever_lt_float_tfloat(double d, Pointer temp);

		int ever_lt_int_tint(int i, Pointer temp);

		int ever_lt_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_lt_text_ttext(Pointer txt, Pointer temp);

		int ever_lt_tfloat_float(Pointer temp, double d);

		int ever_lt_tint_int(Pointer temp, int i);

		int ever_lt_ttext_text(Pointer temp, Pointer txt);

		int ever_ne_bool_tbool(boolean b, Pointer temp);

		int ever_ne_float_tfloat(double d, Pointer temp);

		int ever_ne_int_tint(int i, Pointer temp);

		int ever_ne_tbool_bool(Pointer temp, boolean b);

		int ever_ne_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_ne_text_ttext(Pointer txt, Pointer temp);

		int ever_ne_tfloat_float(Pointer temp, double d);

		int ever_ne_tint_int(Pointer temp, int i);

		int ever_ne_ttext_text(Pointer temp, Pointer txt);

		Pointer teq_bool_tbool(boolean b, Pointer temp);

		Pointer teq_float_tfloat(double d, Pointer temp);

		Pointer teq_int_tint(int i, Pointer temp);

		Pointer teq_tbool_bool(Pointer temp, boolean b);

		Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2);

		Pointer teq_text_ttext(Pointer txt, Pointer temp);

		Pointer teq_tfloat_float(Pointer temp, double d);

		Pointer teq_tint_int(Pointer temp, int i);

		Pointer teq_ttext_text(Pointer temp, Pointer txt);

		Pointer tge_float_tfloat(double d, Pointer temp);

		Pointer tge_int_tint(int i, Pointer temp);

		Pointer tge_temporal_temporal(Pointer temp1, Pointer temp2);

		Pointer tge_text_ttext(Pointer txt, Pointer temp);

		Pointer tge_tfloat_float(Pointer temp, double d);

		Pointer tge_tint_int(Pointer temp, int i);

		Pointer tge_ttext_text(Pointer temp, Pointer txt);

		Pointer tgt_float_tfloat(double d, Pointer temp);

		Pointer tgt_int_tint(int i, Pointer temp);

		Pointer tgt_temporal_temporal(Pointer temp1, Pointer temp2);

		Pointer tgt_text_ttext(Pointer txt, Pointer temp);

		Pointer tgt_tfloat_float(Pointer temp, double d);

		Pointer tgt_tint_int(Pointer temp, int i);

		Pointer tgt_ttext_text(Pointer temp, Pointer txt);

		Pointer tle_float_tfloat(double d, Pointer temp);

		Pointer tle_int_tint(int i, Pointer temp);

		Pointer tle_temporal_temporal(Pointer temp1, Pointer temp2);

		Pointer tle_text_ttext(Pointer txt, Pointer temp);

		Pointer tle_tfloat_float(Pointer temp, double d);

		Pointer tle_tint_int(Pointer temp, int i);

		Pointer tle_ttext_text(Pointer temp, Pointer txt);

		Pointer tlt_float_tfloat(double d, Pointer temp);

		Pointer tlt_int_tint(int i, Pointer temp);

		Pointer tlt_temporal_temporal(Pointer temp1, Pointer temp2);

		Pointer tlt_text_ttext(Pointer txt, Pointer temp);

		Pointer tlt_tfloat_float(Pointer temp, double d);

		Pointer tlt_tint_int(Pointer temp, int i);

		Pointer tlt_ttext_text(Pointer temp, Pointer txt);

		Pointer tne_bool_tbool(boolean b, Pointer temp);

		Pointer tne_float_tfloat(double d, Pointer temp);

		Pointer tne_int_tint(int i, Pointer temp);

		Pointer tne_tbool_bool(Pointer temp, boolean b);

		Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2);

		Pointer tne_text_ttext(Pointer txt, Pointer temp);

		Pointer tne_tfloat_float(Pointer temp, double d);

		Pointer tne_tint_int(Pointer temp, int i);

		Pointer tne_ttext_text(Pointer temp, Pointer txt);

		Pointer temporal_spans(Pointer temp, Pointer count);

		Pointer temporal_split_each_n_spans(Pointer temp, int elem_count, Pointer count);

		Pointer temporal_split_n_spans(Pointer temp, int span_count, Pointer count);

		Pointer tnumber_split_each_n_tboxes(Pointer temp, int elem_count, Pointer count);

		Pointer tnumber_split_n_tboxes(Pointer temp, int box_count, Pointer count);

		Pointer tnumber_tboxes(Pointer temp, Pointer count);

		boolean adjacent_numspan_tnumber(Pointer s, Pointer temp);

		boolean adjacent_tbox_tnumber(Pointer box, Pointer temp);

		boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean adjacent_temporal_tstzspan(Pointer temp, Pointer s);

		boolean adjacent_tnumber_numspan(Pointer temp, Pointer s);

		boolean adjacent_tnumber_tbox(Pointer temp, Pointer box);

		boolean adjacent_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean adjacent_tstzspan_temporal(Pointer s, Pointer temp);

		boolean contained_numspan_tnumber(Pointer s, Pointer temp);

		boolean contained_tbox_tnumber(Pointer box, Pointer temp);

		boolean contained_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean contained_temporal_tstzspan(Pointer temp, Pointer s);

		boolean contained_tnumber_numspan(Pointer temp, Pointer s);

		boolean contained_tnumber_tbox(Pointer temp, Pointer box);

		boolean contained_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean contained_tstzspan_temporal(Pointer s, Pointer temp);

		boolean contains_numspan_tnumber(Pointer s, Pointer temp);

		boolean contains_tbox_tnumber(Pointer box, Pointer temp);

		boolean contains_temporal_tstzspan(Pointer temp, Pointer s);

		boolean contains_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean contains_tnumber_numspan(Pointer temp, Pointer s);

		boolean contains_tnumber_tbox(Pointer temp, Pointer box);

		boolean contains_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean contains_tstzspan_temporal(Pointer s, Pointer temp);

		boolean overlaps_numspan_tnumber(Pointer s, Pointer temp);

		boolean overlaps_tbox_tnumber(Pointer box, Pointer temp);

		boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean overlaps_temporal_tstzspan(Pointer temp, Pointer s);

		boolean overlaps_tnumber_numspan(Pointer temp, Pointer s);

		boolean overlaps_tnumber_tbox(Pointer temp, Pointer box);

		boolean overlaps_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean overlaps_tstzspan_temporal(Pointer s, Pointer temp);

		boolean same_numspan_tnumber(Pointer s, Pointer temp);

		boolean same_tbox_tnumber(Pointer box, Pointer temp);

		boolean same_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean same_temporal_tstzspan(Pointer temp, Pointer s);

		boolean same_tnumber_numspan(Pointer temp, Pointer s);

		boolean same_tnumber_tbox(Pointer temp, Pointer box);

		boolean same_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean same_tstzspan_temporal(Pointer s, Pointer temp);

		boolean after_tbox_tnumber(Pointer box, Pointer temp);

		boolean after_temporal_tstzspan(Pointer temp, Pointer s);

		boolean after_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean after_tnumber_tbox(Pointer temp, Pointer box);

		boolean after_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean after_tstzspan_temporal(Pointer s, Pointer temp);

		boolean before_tbox_tnumber(Pointer box, Pointer temp);

		boolean before_temporal_tstzspan(Pointer temp, Pointer s);

		boolean before_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean before_tnumber_tbox(Pointer temp, Pointer box);

		boolean before_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean before_tstzspan_temporal(Pointer s, Pointer temp);

		boolean left_tbox_tnumber(Pointer box, Pointer temp);

		boolean left_numspan_tnumber(Pointer s, Pointer temp);

		boolean left_tnumber_numspan(Pointer temp, Pointer s);

		boolean left_tnumber_tbox(Pointer temp, Pointer box);

		boolean left_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean overafter_tbox_tnumber(Pointer box, Pointer temp);

		boolean overafter_temporal_tstzspan(Pointer temp, Pointer s);

		boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean overafter_tnumber_tbox(Pointer temp, Pointer box);

		boolean overafter_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean overafter_tstzspan_temporal(Pointer s, Pointer temp);

		boolean overbefore_tbox_tnumber(Pointer box, Pointer temp);

		boolean overbefore_temporal_tstzspan(Pointer temp, Pointer s);

		boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2);

		boolean overbefore_tnumber_tbox(Pointer temp, Pointer box);

		boolean overbefore_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean overbefore_tstzspan_temporal(Pointer s, Pointer temp);

		boolean overleft_numspan_tnumber(Pointer s, Pointer temp);

		boolean overleft_tbox_tnumber(Pointer box, Pointer temp);

		boolean overleft_tnumber_numspan(Pointer temp, Pointer s);

		boolean overleft_tnumber_tbox(Pointer temp, Pointer box);

		boolean overleft_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean overright_numspan_tnumber(Pointer s, Pointer temp);

		boolean overright_tbox_tnumber(Pointer box, Pointer temp);

		boolean overright_tnumber_numspan(Pointer temp, Pointer s);

		boolean overright_tnumber_tbox(Pointer temp, Pointer box);

		boolean overright_tnumber_tnumber(Pointer temp1, Pointer temp2);

		boolean right_numspan_tnumber(Pointer s, Pointer temp);

		boolean right_tbox_tnumber(Pointer box, Pointer temp);

		boolean right_tnumber_numspan(Pointer temp, Pointer s);

		boolean right_tnumber_tbox(Pointer temp, Pointer box);

		boolean right_tnumber_tnumber(Pointer temp1, Pointer temp2);

		Pointer tand_bool_tbool(boolean b, Pointer temp);

		Pointer tand_tbool_bool(Pointer temp, boolean b);

		Pointer tand_tbool_tbool(Pointer temp1, Pointer temp2);

		Pointer tbool_when_true(Pointer temp);

		Pointer tnot_tbool(Pointer temp);

		Pointer tor_bool_tbool(boolean b, Pointer temp);

		Pointer tor_tbool_bool(Pointer temp, boolean b);

		Pointer tor_tbool_tbool(Pointer temp1, Pointer temp2);

		Pointer add_float_tfloat(double d, Pointer tnumber);

		Pointer add_int_tint(int i, Pointer tnumber);

		Pointer add_tfloat_float(Pointer tnumber, double d);

		Pointer add_tint_int(Pointer tnumber, int i);

		Pointer add_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

		Pointer div_float_tfloat(double d, Pointer tnumber);

		Pointer div_int_tint(int i, Pointer tnumber);

		Pointer div_tfloat_float(Pointer tnumber, double d);

		Pointer div_tint_int(Pointer tnumber, int i);

		Pointer div_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

		Pointer mul_float_tfloat(double d, Pointer tnumber);

		Pointer mul_int_tint(int i, Pointer tnumber);

		Pointer mul_tfloat_float(Pointer tnumber, double d);

		Pointer mul_tint_int(Pointer tnumber, int i);

		Pointer mul_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

		Pointer sub_float_tfloat(double d, Pointer tnumber);

		Pointer sub_int_tint(int i, Pointer tnumber);

		Pointer sub_tfloat_float(Pointer tnumber, double d);

		Pointer sub_tint_int(Pointer tnumber, int i);

		Pointer sub_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

		Pointer temporal_derivative(Pointer temp);

		Pointer tfloat_exp(Pointer temp);

		Pointer tfloat_ln(Pointer temp);

		Pointer tfloat_log10(Pointer temp);

		Pointer tnumber_abs(Pointer temp);

		Pointer tnumber_trend(Pointer temp);

		double float_angular_difference(double degrees1, double degrees2);

		Pointer tnumber_angular_difference(Pointer temp);

		Pointer tnumber_delta_value(Pointer temp);

		Pointer textcat_text_ttext(Pointer txt, Pointer temp);

		Pointer textcat_ttext_text(Pointer temp, Pointer txt);

		Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2);

		Pointer ttext_initcap(Pointer temp);

		Pointer ttext_upper(Pointer temp);

		Pointer ttext_lower(Pointer temp);

		Pointer tdistance_tfloat_float(Pointer temp, double d);

		Pointer tdistance_tint_int(Pointer temp, int i);

		Pointer tdistance_tnumber_tnumber(Pointer temp1, Pointer temp2);

		double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2);

		int nad_tboxint_tboxint(Pointer box1, Pointer box2);

		double nad_tfloat_float(Pointer temp, double d);

		double nad_tfloat_tfloat(Pointer temp1, Pointer temp2);

		double nad_tfloat_tbox(Pointer temp, Pointer box);

		int nad_tint_int(Pointer temp, int i);

		int nad_tint_tbox(Pointer temp, Pointer box);

		int nad_tint_tint(Pointer temp1, Pointer temp2);

		Pointer tbool_tand_transfn(Pointer state, Pointer temp);

		Pointer tbool_tor_transfn(Pointer state, Pointer temp);

		Pointer temporal_extent_transfn(Pointer s, Pointer temp);

		Pointer temporal_tagg_finalfn(Pointer state);

		Pointer temporal_tcount_transfn(Pointer state, Pointer temp);

		Pointer tfloat_tmax_transfn(Pointer state, Pointer temp);

		Pointer tfloat_tmin_transfn(Pointer state, Pointer temp);

		Pointer tfloat_tsum_transfn(Pointer state, Pointer temp);

		Pointer tfloat_wmax_transfn(Pointer state, Pointer temp, Pointer interv);

		Pointer tfloat_wmin_transfn(Pointer state, Pointer temp, Pointer interv);

		Pointer tfloat_wsum_transfn(Pointer state, Pointer temp, Pointer interv);

		Pointer timestamptz_tcount_transfn(Pointer state, long t);

		Pointer tint_tmax_transfn(Pointer state, Pointer temp);

		Pointer tint_tmin_transfn(Pointer state, Pointer temp);

		Pointer tint_tsum_transfn(Pointer state, Pointer temp);

		Pointer tint_wmax_transfn(Pointer state, Pointer temp, Pointer interv);

		Pointer tint_wmin_transfn(Pointer state, Pointer temp, Pointer interv);

		Pointer tint_wsum_transfn(Pointer state, Pointer temp, Pointer interv);

		Pointer tnumber_extent_transfn(Pointer box, Pointer temp);

		Pointer tnumber_tavg_finalfn(Pointer state);

		Pointer tnumber_tavg_transfn(Pointer state, Pointer temp);

		Pointer tnumber_wavg_transfn(Pointer state, Pointer temp, Pointer interv);

		Pointer tstzset_tcount_transfn(Pointer state, Pointer s);

		Pointer tstzspan_tcount_transfn(Pointer state, Pointer s);

		Pointer tstzspanset_tcount_transfn(Pointer state, Pointer ss);

		Pointer ttext_tmax_transfn(Pointer state, Pointer temp);

		Pointer ttext_tmin_transfn(Pointer state, Pointer temp);

		Pointer temporal_simplify_dp(Pointer temp, double eps_dist, boolean synchronize);

		Pointer temporal_simplify_max_dist(Pointer temp, double eps_dist, boolean synchronize);

		Pointer temporal_simplify_min_dist(Pointer temp, double dist);

		Pointer temporal_simplify_min_tdelta(Pointer temp, Pointer mint);

		Pointer temporal_tprecision(Pointer temp, Pointer duration, long origin);

		Pointer temporal_tsample(Pointer temp, Pointer duration, long origin, int interp);

		double temporal_dyntimewarp_distance(Pointer temp1, Pointer temp2);

		Pointer temporal_dyntimewarp_path(Pointer temp1, Pointer temp2, Pointer count);

		double temporal_frechet_distance(Pointer temp1, Pointer temp2);

		Pointer temporal_frechet_path(Pointer temp1, Pointer temp2, Pointer count);

		double temporal_hausdorff_distance(Pointer temp1, Pointer temp2);

		Pointer temporal_time_bins(Pointer temp, Pointer duration, long origin, Pointer count);

		Pointer temporal_time_split(Pointer temp, Pointer duration, long torigin, Pointer time_bins, Pointer count);

		Pointer tfloat_time_boxes(Pointer temp, Pointer duration, long torigin, Pointer count);

		Pointer tfloat_value_bins(Pointer temp, double vsize, double vorigin, Pointer count);

		Pointer tfloat_value_boxes(Pointer temp, double vsize, double vorigin, Pointer count);

		Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer bins, Pointer count);

		Pointer tfloat_value_time_boxes(Pointer temp, double vsize, Pointer duration, double vorigin, long torigin, Pointer count);

		Pointer tfloat_value_time_split(Pointer temp, double vsize, Pointer duration, double vorigin, long torigin, Pointer value_bins, Pointer time_bins, Pointer count);

		Pointer tfloatbox_time_tiles(Pointer box, Pointer duration, long torigin, Pointer count);

		Pointer tfloatbox_value_tiles(Pointer box, double vsize, double vorigin, Pointer count);

		Pointer tfloatbox_value_time_tiles(Pointer box, double vsize, Pointer duration, double vorigin, long torigin, Pointer count);

		Pointer tint_time_boxes(Pointer temp, Pointer duration, long torigin, Pointer count);

		Pointer tint_value_bins(Pointer temp, int vsize, int vorigin, Pointer count);

		Pointer tint_value_boxes(Pointer temp, int vsize, int vorigin, Pointer count);

		Pointer tint_value_split(Pointer temp, int vsize, int vorigin, Pointer bins, Pointer count);

		Pointer tint_value_time_boxes(Pointer temp, int vsize, Pointer duration, int vorigin, long torigin, Pointer count);

		Pointer tint_value_time_split(Pointer temp, long size, Pointer duration, int vorigin, long torigin, Pointer value_bins, Pointer time_bins, Pointer count);

		Pointer tintbox_time_tiles(Pointer box, Pointer duration, long torigin, Pointer count);

		Pointer tintbox_value_tiles(Pointer box, int xsize, int xorigin, Pointer count);

		Pointer tintbox_value_time_tiles(Pointer box, int xsize, Pointer duration, int xorigin, long torigin, Pointer count);

		Pointer geo_as_ewkb(Pointer gs, String endian, Pointer size);

		String geo_as_ewkt(Pointer gs, int precision);

		String geo_as_geojson(Pointer gs, int option, int precision, String srs);

		String geo_as_hexewkb(Pointer gs, String endian);

		String geo_as_text(Pointer gs, int precision);

		Pointer geo_from_ewkb(Pointer wkb, long wkb_size, int srid);

		Pointer geo_from_geojson(String geojson);

		Pointer geo_from_text(String wkt, int srid);

		String geo_out(Pointer gs);


		Pointer geog_from_hexewkb(String wkt);

		Pointer geog_in(String str, int typmod);

		Pointer geom_from_hexewkb(String wkt);

		Pointer geom_in(String str, int typmod);

		Pointer box3d_make(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, int srid);

		String box3d_out(Pointer box, int maxdd);

		Pointer gbox_make(boolean hasz, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax);

		String gbox_out(Pointer box, int maxdd);

		Pointer geo_copy(Pointer g);

		Pointer geogpoint_make2d(int srid, double x, double y);

		Pointer geogpoint_make3dz(int srid, double x, double y, double z);

		Pointer geompoint_make2d(int srid, double x, double y);

		Pointer geompoint_make3dz(int srid, double x, double y, double z);

		Pointer geom_to_geog(Pointer geom);

		Pointer geog_to_geom(Pointer geog);

		boolean geo_is_empty(Pointer g);

		boolean geo_is_unitary(Pointer gs);

		String geo_typename(int type);

		double geog_area(Pointer g, boolean use_spheroid);

		Pointer geog_centroid(Pointer g, boolean use_spheroid);

		double geog_length(Pointer g, boolean use_spheroid);

		double geog_perimeter(Pointer g, boolean use_spheroid);

		boolean geom_azimuth(Pointer gs1, Pointer gs2, Pointer result);

		double geom_length(Pointer gs);

		double geom_perimeter(Pointer gs);

		int line_numpoints(Pointer gs);

		Pointer line_point_n(Pointer geom, int n);

		Pointer geo_reverse(Pointer gs);

		Pointer geo_round(Pointer gs, int maxdd);

		Pointer geo_set_srid(Pointer gs, int srid);

		int geo_srid(Pointer gs);

		Pointer geo_transform(Pointer geom, int srid_to);

		Pointer geo_transform_pipeline(Pointer gs, String pipeline, int srid_to, boolean is_forward);

		Pointer geo_collect_garray(Pointer gsarr, int count);

		Pointer geo_makeline_garray(Pointer gsarr, int count);

		int geo_num_points(Pointer gs);

		int geo_num_geos(Pointer gs);

		Pointer geo_geo_n(Pointer geom, int n);

		Pointer geo_pointarr(Pointer gs, Pointer count);

		Pointer geo_points(Pointer gs);

		Pointer geom_array_union(Pointer gsarr, int count);

		Pointer geom_boundary(Pointer gs);

		Pointer geom_buffer(Pointer gs, double size, String params);

		Pointer geom_centroid(Pointer gs);

		Pointer geom_convex_hull(Pointer gs);

		Pointer geom_difference2d(Pointer gs1, Pointer gs2);

		Pointer geom_intersection2d(Pointer gs1, Pointer gs2);

		Pointer geom_intersection2d_coll(Pointer gs1, Pointer gs2);

		Pointer geom_min_bounding_radius(Pointer geom, Pointer radius);

		Pointer geom_shortestline2d(Pointer gs1, Pointer s2);

		Pointer geom_shortestline3d(Pointer gs1, Pointer s2);

		Pointer geom_unary_union(Pointer gs, double prec);

		Pointer line_interpolate_point(Pointer gs, double distance_fraction, boolean repeat);

		double line_locate_point(Pointer gs1, Pointer gs2);

		Pointer line_substring(Pointer gs, double from, double to);

		boolean geog_dwithin(Pointer g1, Pointer g2, double tolerance, boolean use_spheroid);

		boolean geog_intersects(Pointer gs1, Pointer gs2, boolean use_spheroid);

		boolean geom_contains(Pointer gs1, Pointer gs2);

		boolean geom_covers(Pointer gs1, Pointer gs2);

		boolean geom_disjoint2d(Pointer gs1, Pointer gs2);

		boolean geom_dwithin2d(Pointer gs1, Pointer gs2, double tolerance);

		boolean geom_dwithin3d(Pointer gs1, Pointer gs2, double tolerance);

		boolean geom_intersects2d(Pointer gs1, Pointer gs2);

		boolean geom_intersects3d(Pointer gs1, Pointer gs2);

		boolean geom_relate_pattern(Pointer gs1, Pointer gs2, String patt);

		boolean geom_touches(Pointer gs1, Pointer gs2);

		Pointer geo_stboxes(Pointer gs, Pointer count);

		Pointer geo_split_each_n_stboxes(Pointer gs, int elem_count, Pointer count);

		Pointer geo_split_n_stboxes(Pointer gs, int box_count, Pointer count);

		double geog_distance(Pointer g1, Pointer g2);

		double geom_distance2d(Pointer gs1, Pointer gs2);

		double geom_distance3d(Pointer gs1, Pointer gs2);

		int geo_equals(Pointer gs1, Pointer gs2);

		boolean geo_same(Pointer gs1, Pointer gs2);

		Pointer geogset_in(String str);

		Pointer geomset_in(String str);

		String spatialset_as_text(Pointer set, int maxdd);

		String spatialset_as_ewkt(Pointer set, int maxdd);

		Pointer geoset_make(Pointer values, int count);

		Pointer geo_to_set(Pointer gs);

		Pointer geoset_end_value(Pointer s);

		Pointer geoset_start_value(Pointer s);

		boolean geoset_value_n(Pointer s, int n, Pointer result);


		boolean contained_geo_set(Pointer gs, Pointer s);

		boolean contains_set_geo(Pointer s, Pointer gs);

		Pointer geo_union_transfn(Pointer state, Pointer gs);

		Pointer intersection_geo_set(Pointer gs, Pointer s);

		Pointer intersection_set_geo(Pointer s, Pointer gs);

		Pointer minus_geo_set(Pointer gs, Pointer s);

		Pointer minus_set_geo(Pointer s, Pointer gs);

		Pointer union_geo_set(Pointer gs, Pointer s);

		Pointer union_set_geo(Pointer s, Pointer gs);

		Pointer spatialset_set_srid(Pointer s, int srid);

		int spatialset_srid(Pointer s);

		Pointer spatialset_transform(Pointer s, int srid);

		Pointer spatialset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward);

		String stbox_as_hexwkb(Pointer box, byte variant, Pointer size);

		Pointer stbox_as_wkb(Pointer box, byte variant, Pointer size_out);

		Pointer stbox_from_hexwkb(String hexwkb);

		Pointer stbox_from_wkb(Pointer wkb, long size);

		Pointer stbox_in(String str);

		String stbox_out(Pointer box, int maxdd);

		Pointer geo_timestamptz_to_stbox(Pointer gs, long t);

		Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s);

		Pointer stbox_copy(Pointer box);

		Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s);

		Pointer geo_to_stbox(Pointer gs);

		Pointer spatialset_to_stbox(Pointer s);

		Pointer stbox_to_box3d(Pointer box);

		Pointer stbox_to_gbox(Pointer box);

		Pointer stbox_to_geo(Pointer box);

		Pointer stbox_to_tstzspan(Pointer box);

		Pointer timestamptz_to_stbox(long t);

		Pointer tstzset_to_stbox(Pointer s);

		Pointer tstzspan_to_stbox(Pointer s);

		Pointer tstzspanset_to_stbox(Pointer ss);

		double stbox_area(Pointer box, boolean spheroid);

		int stbox_hash(Pointer box);

		long stbox_hash_extended(Pointer box, long seed);

		boolean stbox_hast(Pointer box);

		boolean stbox_hasx(Pointer box);

		boolean stbox_hasz(Pointer box);

		boolean stbox_isgeodetic(Pointer box);

		double stbox_perimeter(Pointer box, boolean spheroid);

		boolean stbox_tmax(Pointer box, Pointer result);

		boolean stbox_tmax_inc(Pointer box, Pointer result);

		boolean stbox_tmin(Pointer box, Pointer result);

		boolean stbox_tmin_inc(Pointer box, Pointer result);

		double stbox_volume(Pointer box);

		boolean stbox_xmax(Pointer box, Pointer result);

		boolean stbox_xmin(Pointer box, Pointer result);

		boolean stbox_ymax(Pointer box, Pointer result);

		boolean stbox_ymin(Pointer box, Pointer result);

		boolean stbox_zmax(Pointer box, Pointer result);

		boolean stbox_zmin(Pointer box, Pointer result);

		Pointer stbox_expand_space(Pointer box, double d);

		Pointer stbox_expand_time(Pointer box, Pointer interv);

		Pointer stbox_get_space(Pointer box);

		Pointer stbox_quad_split(Pointer box, Pointer count);

		Pointer stbox_round(Pointer box, int maxdd);

		Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);

		Pointer stboxarr_round(Pointer boxarr, int count, int maxdd);

		Pointer stbox_set_srid(Pointer box, int srid);

		int stbox_srid(Pointer box);

		Pointer stbox_transform(Pointer box, int srid);

		Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward);

		boolean adjacent_stbox_stbox(Pointer box1, Pointer box2);

		boolean contained_stbox_stbox(Pointer box1, Pointer box2);

		boolean contains_stbox_stbox(Pointer box1, Pointer box2);

		boolean overlaps_stbox_stbox(Pointer box1, Pointer box2);

		boolean same_stbox_stbox(Pointer box1, Pointer box2);

		boolean above_stbox_stbox(Pointer box1, Pointer box2);

		boolean after_stbox_stbox(Pointer box1, Pointer box2);

		boolean back_stbox_stbox(Pointer box1, Pointer box2);

		boolean before_stbox_stbox(Pointer box1, Pointer box2);

		boolean below_stbox_stbox(Pointer box1, Pointer box2);

		boolean front_stbox_stbox(Pointer box1, Pointer box2);

		boolean left_stbox_stbox(Pointer box1, Pointer box2);

		boolean overabove_stbox_stbox(Pointer box1, Pointer box2);

		boolean overafter_stbox_stbox(Pointer box1, Pointer box2);

		boolean overback_stbox_stbox(Pointer box1, Pointer box2);

		boolean overbefore_stbox_stbox(Pointer box1, Pointer box2);

		boolean overbelow_stbox_stbox(Pointer box1, Pointer box2);

		boolean overfront_stbox_stbox(Pointer box1, Pointer box2);

		boolean overleft_stbox_stbox(Pointer box1, Pointer box2);

		boolean overright_stbox_stbox(Pointer box1, Pointer box2);

		boolean right_stbox_stbox(Pointer box1, Pointer box2);

		Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict);

		Pointer intersection_stbox_stbox(Pointer box1, Pointer box2);

		int stbox_cmp(Pointer box1, Pointer box2);

		boolean stbox_eq(Pointer box1, Pointer box2);

		boolean stbox_ge(Pointer box1, Pointer box2);

		boolean stbox_gt(Pointer box1, Pointer box2);

		boolean stbox_le(Pointer box1, Pointer box2);

		boolean stbox_lt(Pointer box1, Pointer box2);

		boolean stbox_ne(Pointer box1, Pointer box2);

		Pointer tgeogpoint_from_mfjson(String str);

		Pointer tgeogpoint_in(String str);

		Pointer tgeography_from_mfjson(String mfjson);

		Pointer tgeography_in(String str);

		Pointer tgeometry_from_mfjson(String str);

		Pointer tgeometry_in(String str);

		Pointer tgeompoint_from_mfjson(String str);

		Pointer tgeompoint_in(String str);

		String tspatial_as_ewkt(Pointer temp, int maxdd);

		String tspatial_as_text(Pointer temp, int maxdd);

		String tspatial_out(Pointer temp, int maxdd);

		Pointer tgeo_from_base_temp(Pointer gs, Pointer temp);

		Pointer tgeoinst_make(Pointer gs, long t);

		Pointer tgeoseq_from_base_tstzset(Pointer gs, Pointer s);

		Pointer tgeoseq_from_base_tstzspan(Pointer gs, Pointer s, int interp);

		Pointer tgeoseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp);

		Pointer tpoint_from_base_temp(Pointer gs, Pointer temp);

		Pointer tpointinst_make(Pointer gs, long t);

		Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s);

		Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp);

		Pointer tpointseq_make_coords(Pointer xcoords, Pointer ycoords, Pointer zcoords, Pointer times, int count, int srid, boolean geodetic, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp);

		Pointer box3d_to_stbox(Pointer box);

		Pointer gbox_to_stbox(Pointer box);

		Pointer geomeas_to_tpoint(Pointer gs);

		Pointer tgeogpoint_to_tgeography(Pointer temp);

		Pointer tgeography_to_tgeogpoint(Pointer temp);

		Pointer tgeography_to_tgeometry(Pointer temp);

		Pointer tgeometry_to_tgeography(Pointer temp);

		Pointer tgeometry_to_tgeompoint(Pointer temp);

		Pointer tgeompoint_to_tgeometry(Pointer temp);

		boolean tpoint_as_mvtgeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count);

		boolean tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize, Pointer result);

		Pointer tspatial_to_stbox(Pointer temp);

		boolean bearing_point_point(Pointer gs1, Pointer gs2, Pointer result);

		Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert);

		Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2);

		Pointer tgeo_centroid(Pointer temp);

		Pointer tgeo_convex_hull(Pointer temp);

		Pointer tgeo_end_value(Pointer temp);

		Pointer tgeo_start_value(Pointer temp);

		Pointer tgeo_traversed_area(Pointer temp, boolean unary_union);

		boolean tgeo_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean tgeo_value_n(Pointer temp, int n, Pointer result);

		Pointer tgeo_values(Pointer temp, Pointer count);

		Pointer tpoint_angular_difference(Pointer temp);

		Pointer tpoint_azimuth(Pointer temp);

		Pointer tpoint_cumulative_length(Pointer temp);

		boolean tpoint_direction(Pointer temp, Pointer result);

		Pointer tpoint_get_x(Pointer temp);

		Pointer tpoint_get_y(Pointer temp);

		Pointer tpoint_get_z(Pointer temp);

		boolean tpoint_is_simple(Pointer temp);

		double tpoint_length(Pointer temp);

		Pointer tpoint_speed(Pointer temp);

		Pointer tpoint_trajectory(Pointer temp, boolean unary_union);

		Pointer tpoint_twcentroid(Pointer temp);

		Pointer tgeo_affine(Pointer temp, Pointer a);

		Pointer tgeo_scale(Pointer temp, Pointer scale, Pointer sorigin);

		Pointer tpoint_make_simple(Pointer temp, Pointer count);

		int tspatial_srid(Pointer temp);

		Pointer tspatial_set_srid(Pointer temp, int srid);

		Pointer tspatial_transform(Pointer temp, int srid);

		Pointer tspatial_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward);

		Pointer tgeo_at_geom(Pointer temp, Pointer gs);

		Pointer tgeo_at_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tgeo_at_value(Pointer temp, Pointer gs);

		Pointer tgeo_minus_geom(Pointer temp, Pointer gs);

		Pointer tgeo_minus_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tgeo_minus_value(Pointer temp, Pointer gs);


		Pointer tpoint_at_value(Pointer temp, Pointer gs);


		Pointer tpoint_minus_value(Pointer temp, Pointer gs);

		int always_eq_geo_tgeo(Pointer gs, Pointer temp);

		int always_eq_tgeo_geo(Pointer temp, Pointer gs);

		int always_eq_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int always_ne_geo_tgeo(Pointer gs, Pointer temp);

		int always_ne_tgeo_geo(Pointer temp, Pointer gs);

		int always_ne_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int ever_eq_geo_tgeo(Pointer gs, Pointer temp);

		int ever_eq_tgeo_geo(Pointer temp, Pointer gs);

		int ever_eq_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int ever_ne_geo_tgeo(Pointer gs, Pointer temp);

		int ever_ne_tgeo_geo(Pointer temp, Pointer gs);

		int ever_ne_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer teq_geo_tgeo(Pointer gs, Pointer temp);

		Pointer teq_tgeo_geo(Pointer temp, Pointer gs);

		Pointer tne_geo_tgeo(Pointer gs, Pointer temp);

		Pointer tne_tgeo_geo(Pointer temp, Pointer gs);

		Pointer tgeo_stboxes(Pointer temp, Pointer count);

		Pointer tgeo_space_boxes(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer count);

		Pointer tgeo_space_time_boxes(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean bitmatrix, boolean border_inc, Pointer count);

		Pointer tgeo_split_each_n_stboxes(Pointer temp, int elem_count, Pointer count);

		Pointer tgeo_split_n_stboxes(Pointer temp, int box_count, Pointer count);

		boolean adjacent_stbox_tspatial(Pointer box, Pointer temp);

		boolean adjacent_tspatial_stbox(Pointer temp, Pointer box);

		boolean adjacent_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean contained_stbox_tspatial(Pointer box, Pointer temp);

		boolean contained_tspatial_stbox(Pointer temp, Pointer box);

		boolean contained_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean contains_stbox_tspatial(Pointer box, Pointer temp);

		boolean contains_tspatial_stbox(Pointer temp, Pointer box);

		boolean contains_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overlaps_stbox_tspatial(Pointer box, Pointer temp);

		boolean overlaps_tspatial_stbox(Pointer temp, Pointer box);

		boolean overlaps_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean same_stbox_tspatial(Pointer box, Pointer temp);

		boolean same_tspatial_stbox(Pointer temp, Pointer box);

		boolean same_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean above_stbox_tspatial(Pointer box, Pointer temp);

		boolean above_tspatial_stbox(Pointer temp, Pointer box);

		boolean above_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean after_stbox_tspatial(Pointer box, Pointer temp);

		boolean after_tspatial_stbox(Pointer temp, Pointer box);

		boolean after_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean back_stbox_tspatial(Pointer box, Pointer temp);

		boolean back_tspatial_stbox(Pointer temp, Pointer box);

		boolean back_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean before_stbox_tspatial(Pointer box, Pointer temp);

		boolean before_tspatial_stbox(Pointer temp, Pointer box);

		boolean before_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean below_stbox_tspatial(Pointer box, Pointer temp);

		boolean below_tspatial_stbox(Pointer temp, Pointer box);

		boolean below_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean front_stbox_tspatial(Pointer box, Pointer temp);

		boolean front_tspatial_stbox(Pointer temp, Pointer box);

		boolean front_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean left_stbox_tspatial(Pointer box, Pointer temp);

		boolean left_tspatial_stbox(Pointer temp, Pointer box);

		boolean left_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overabove_stbox_tspatial(Pointer box, Pointer temp);

		boolean overabove_tspatial_stbox(Pointer temp, Pointer box);

		boolean overabove_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overafter_stbox_tspatial(Pointer box, Pointer temp);

		boolean overafter_tspatial_stbox(Pointer temp, Pointer box);

		boolean overafter_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overback_stbox_tspatial(Pointer box, Pointer temp);

		boolean overback_tspatial_stbox(Pointer temp, Pointer box);

		boolean overback_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overbefore_stbox_tspatial(Pointer box, Pointer temp);

		boolean overbefore_tspatial_stbox(Pointer temp, Pointer box);

		boolean overbefore_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overbelow_stbox_tspatial(Pointer box, Pointer temp);

		boolean overbelow_tspatial_stbox(Pointer temp, Pointer box);

		boolean overbelow_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overfront_stbox_tspatial(Pointer box, Pointer temp);

		boolean overfront_tspatial_stbox(Pointer temp, Pointer box);

		boolean overfront_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overleft_stbox_tspatial(Pointer box, Pointer temp);

		boolean overleft_tspatial_stbox(Pointer temp, Pointer box);

		boolean overleft_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean overright_stbox_tspatial(Pointer box, Pointer temp);

		boolean overright_tspatial_stbox(Pointer temp, Pointer box);

		boolean overright_tspatial_tspatial(Pointer temp1, Pointer temp2);

		boolean right_stbox_tspatial(Pointer box, Pointer temp);

		boolean right_tspatial_stbox(Pointer temp, Pointer box);

		boolean right_tspatial_tspatial(Pointer temp1, Pointer temp2);

		int acontains_geo_tgeo(Pointer gs, Pointer temp);

		int acontains_tgeo_geo(Pointer temp, Pointer gs);

		int acontains_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int adisjoint_tgeo_geo(Pointer temp, Pointer gs);

		int adisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int adwithin_tgeo_geo(Pointer temp, Pointer gs, double dist);

		int adwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist);

		int aintersects_tgeo_geo(Pointer temp, Pointer gs);

		int aintersects_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int atouches_tgeo_geo(Pointer temp, Pointer gs);

		int atouches_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int atouches_tpoint_geo(Pointer temp, Pointer gs);

		int econtains_geo_tgeo(Pointer gs, Pointer temp);

		int econtains_tgeo_geo(Pointer temp, Pointer gs);

		int econtains_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int ecovers_geo_tgeo(Pointer gs, Pointer temp);

		int ecovers_tgeo_geo(Pointer temp, Pointer gs);

		int ecovers_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int edisjoint_tgeo_geo(Pointer temp, Pointer gs);

		int edisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int edwithin_tgeo_geo(Pointer temp, Pointer gs, double dist);

		int edwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist);

		int eintersects_tgeo_geo(Pointer temp, Pointer gs);

		int eintersects_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int etouches_tgeo_geo(Pointer temp, Pointer gs);

		int etouches_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int etouches_tpoint_geo(Pointer temp, Pointer gs);



















		Pointer tdistance_tgeo_geo(Pointer temp, Pointer gs);

		Pointer tdistance_tgeo_tgeo(Pointer temp1, Pointer temp2);

		double nad_stbox_geo(Pointer box, Pointer gs);

		double nad_stbox_stbox(Pointer box1, Pointer box2);

		double nad_tgeo_geo(Pointer temp, Pointer gs);

		double nad_tgeo_stbox(Pointer temp, Pointer box);

		double nad_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer nai_tgeo_geo(Pointer temp, Pointer gs);

		Pointer nai_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer shortestline_tgeo_geo(Pointer temp, Pointer gs);

		Pointer shortestline_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer tpoint_tcentroid_finalfn(Pointer state);

		Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp);

		Pointer tspatial_extent_transfn(Pointer box, Pointer temp);

		Pointer stbox_get_space_tile(Pointer point, double xsize, double ysize, double zsize, Pointer sorigin);

		Pointer stbox_get_space_time_tile(Pointer point, long t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin);

		Pointer stbox_get_time_tile(long t, Pointer duration, long torigin);

		Pointer stbox_space_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer sorigin, boolean border_inc, Pointer count);

		Pointer stbox_space_time_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean border_inc, Pointer count);

		Pointer stbox_time_tiles(Pointer bounds, Pointer duration, long torigin, boolean border_inc, Pointer count);

		Pointer tgeo_space_split(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer count);

		Pointer tgeo_space_time_split(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer time_bins, Pointer count);


		Pointer geo_cluster_dbscan(Pointer geoms, int ngeoms, double tolerance, int minpoints, Pointer count);

		Pointer geo_cluster_intersecting(Pointer geoms, int ngeoms, Pointer count);

		Pointer geo_cluster_within(Pointer geoms, int ngeoms, double tolerance, Pointer count);

	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_intspan() {
		var _result = MeosLibrary.meos.rtree_create_intspan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_bigintspan() {
		var _result = MeosLibrary.meos.rtree_create_bigintspan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_floatspan() {
		var _result = MeosLibrary.meos.rtree_create_floatspan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_datespan() {
		var _result = MeosLibrary.meos.rtree_create_datespan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_tstzspan() {
		var _result = MeosLibrary.meos.rtree_create_tstzspan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_tbox() {
		var _result = MeosLibrary.meos.rtree_create_tbox();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_stbox() {
		var _result = MeosLibrary.meos.rtree_create_stbox();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void rtree_free(Pointer rtree) {
		MeosLibrary.meos.rtree_free(rtree);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void rtree_insert(Pointer rtree, Pointer box, int id) {
		MeosLibrary.meos.rtree_insert(rtree, box, id);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void rtree_insert_temporal(Pointer rtree, Pointer temp, int id) {
		MeosLibrary.meos.rtree_insert_temporal(rtree, temp, id);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_search(Pointer rtree, int op, Pointer query, Pointer count) {
		var _result = MeosLibrary.meos.rtree_search(rtree, op, query, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_search_temporal(Pointer rtree, int op, Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.rtree_search_temporal(rtree, op, temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_error(int errlevel, int errcode, String format) {
		MeosLibrary.meos.meos_error(errlevel, errcode, format);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static int meos_errno() {
		var _result = MeosLibrary.meos.meos_errno();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int meos_errno_set(int err) {
		var _result = MeosLibrary.meos.meos_errno_set(err);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int meos_errno_restore(int err) {
		var _result = MeosLibrary.meos.meos_errno_restore(err);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int meos_errno_reset() {
		var _result = MeosLibrary.meos.meos_errno_reset();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_initialize_timezone(String name) {
		MeosLibrary.meos.meos_initialize_timezone(name);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_initialize_error_handler(error_handler_fn err_handler) {
		MeosLibrary.meos.meos_initialize_error_handler(err_handler);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_finalize_timezone() {
		MeosLibrary.meos.meos_finalize_timezone();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_finalize_projsrs() {
		MeosLibrary.meos.meos_finalize_projsrs();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_finalize_ways() {
		MeosLibrary.meos.meos_finalize_ways();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static boolean meos_set_datestyle(String newval, Pointer extra) {
		var _result = MeosLibrary.meos.meos_set_datestyle(newval, extra);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean meos_set_intervalstyle(String newval, int extra) {
		var _result = MeosLibrary.meos.meos_set_intervalstyle(newval, extra);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String meos_get_datestyle() {
		var _result = MeosLibrary.meos.meos_get_datestyle();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String meos_get_intervalstyle() {
		var _result = MeosLibrary.meos.meos_get_intervalstyle();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_set_spatial_ref_sys_csv(String path) {
		MeosLibrary.meos.meos_set_spatial_ref_sys_csv(path);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_initialize() {
		MeosLibrary.meos.meos_initialize();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_finalize() {
		MeosLibrary.meos.meos_finalize();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static int add_date_int(int d, int days) {
		var _result = MeosLibrary.meos.add_date_int(d, days);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_interval_interval(Pointer interv1, Pointer interv2) {
		var _result = MeosLibrary.meos.add_interval_interval(interv1, interv2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime add_timestamptz_interval(OffsetDateTime t, Pointer interv) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.add_timestamptz_interval(t_new, interv);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static boolean bool_in(String str) {
		var _result = MeosLibrary.meos.bool_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String bool_out(boolean b) {
		var _result = MeosLibrary.meos.bool_out(b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cstring_to_text(String str) {
		var _result = MeosLibrary.meos.cstring_to_text(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static LocalDateTime date_to_timestamp(int dateVal) {
		var _result = MeosLibrary.meos.date_to_timestamp(dateVal);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toLocalDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime date_to_timestamptz(int d) {
		var _result = MeosLibrary.meos.date_to_timestamptz(d);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static double float8_exp(double d) {
		var _result = MeosLibrary.meos.float8_exp(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float8_ln(double d) {
		var _result = MeosLibrary.meos.float8_ln(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float8_log10(double d) {
		var _result = MeosLibrary.meos.float8_log10(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String float8_out(double d, int maxdd) {
		var _result = MeosLibrary.meos.float8_out(d, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_round(double d, int maxdd) {
		var _result = MeosLibrary.meos.float_round(d, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int int32_cmp(int l, int r) {
		var _result = MeosLibrary.meos.int32_cmp(l, r);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int int64_cmp(long l, long r) {
		var _result = MeosLibrary.meos.int64_cmp(l, r);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs) {
		var _result = MeosLibrary.meos.interval_make(years, months, weeks, days, hours, mins, secs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int minus_date_date(int d1, int d2) {
		var _result = MeosLibrary.meos.minus_date_date(d1, d2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int minus_date_int(int d, int days) {
		var _result = MeosLibrary.meos.minus_date_int(d, days);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime minus_timestamptz_interval(OffsetDateTime t, Pointer interv) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.minus_timestamptz_interval(t_new, interv);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_timestamptz(OffsetDateTime t1, OffsetDateTime t2) {
		var t1_new = utils.TimestampTzConverter.toTimestampTz(t1);
		var t2_new = utils.TimestampTzConverter.toTimestampTz(t2);
		var _result = MeosLibrary.meos.minus_timestamptz_timestamptz(t1_new, t2_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_interval_float8(Pointer interv, double factor) {
		var _result = MeosLibrary.meos.mul_interval_float8(interv, factor);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int date_in(String str) {
		var _result = MeosLibrary.meos.date_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String date_out(int d) {
		var _result = MeosLibrary.meos.date_out(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int interval_cmp(Pointer interv1, Pointer interv2) {
		var _result = MeosLibrary.meos.interval_cmp(interv1, interv2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer interval_in(String str, int typmod) {
		var _result = MeosLibrary.meos.interval_in(str, typmod);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String interval_out(Pointer interv) {
		var _result = MeosLibrary.meos.interval_out(interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static LocalDateTime timestamp_in(String str, int typmod) {
		var _result = MeosLibrary.meos.timestamp_in(str, typmod);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toLocalDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static String timestamp_out(LocalDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamp_out(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_in(String str, int typmod) {
		var _result = MeosLibrary.meos.timestamptz_in(str, typmod);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static String timestamptz_out(OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_out(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String text_to_cstring(Pointer txt) {
		var _result = MeosLibrary.meos.text_to_cstring(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int text_cmp(Pointer txt1, Pointer txt2) {
		var _result = MeosLibrary.meos.text_cmp(txt1, txt2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_copy(Pointer txt) {
		var _result = MeosLibrary.meos.text_copy(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_in(String str) {
		var _result = MeosLibrary.meos.text_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_initcap(Pointer txt) {
		var _result = MeosLibrary.meos.text_initcap(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_lower(Pointer txt) {
		var _result = MeosLibrary.meos.text_lower(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String text_out(Pointer txt) {
		var _result = MeosLibrary.meos.text_out(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_upper(Pointer txt) {
		var _result = MeosLibrary.meos.text_upper(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_text_text(Pointer txt1, Pointer txt2) {
		var _result = MeosLibrary.meos.textcat_text_text(txt1, txt2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_shift(OffsetDateTime t, Pointer interv) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_shift(t_new, interv);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static int timestamp_to_date(LocalDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamp_to_date(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int timestamptz_to_date(OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_to_date(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_in(String str) {
		var _result = MeosLibrary.meos.bigintset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String bigintset_out(Pointer set) {
		var _result = MeosLibrary.meos.bigintset_out(set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_expand(Pointer s, long value) {
		var _result = MeosLibrary.meos.bigintspan_expand(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_in(String str) {
		var _result = MeosLibrary.meos.bigintspan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String bigintspan_out(Pointer s) {
		var _result = MeosLibrary.meos.bigintspan_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspanset_in(String str) {
		var _result = MeosLibrary.meos.bigintspanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String bigintspanset_out(Pointer ss) {
		var _result = MeosLibrary.meos.bigintspanset_out(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_in(String str) {
		var _result = MeosLibrary.meos.dateset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String dateset_out(Pointer s) {
		var _result = MeosLibrary.meos.dateset_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_in(String str) {
		var _result = MeosLibrary.meos.datespan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String datespan_out(Pointer s) {
		var _result = MeosLibrary.meos.datespan_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_in(String str) {
		var _result = MeosLibrary.meos.datespanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String datespanset_out(Pointer ss) {
		var _result = MeosLibrary.meos.datespanset_out(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_in(String str) {
		var _result = MeosLibrary.meos.floatset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String floatset_out(Pointer set, int maxdd) {
		var _result = MeosLibrary.meos.floatset_out(set, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_expand(Pointer s, double value) {
		var _result = MeosLibrary.meos.floatspan_expand(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_in(String str) {
		var _result = MeosLibrary.meos.floatspan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String floatspan_out(Pointer s, int maxdd) {
		var _result = MeosLibrary.meos.floatspan_out(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_in(String str) {
		var _result = MeosLibrary.meos.floatspanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String floatspanset_out(Pointer ss, int maxdd) {
		var _result = MeosLibrary.meos.floatspanset_out(ss, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_in(String str) {
		var _result = MeosLibrary.meos.intset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String intset_out(Pointer set) {
		var _result = MeosLibrary.meos.intset_out(set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_expand(Pointer s, int value) {
		var _result = MeosLibrary.meos.intspan_expand(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_in(String str) {
		var _result = MeosLibrary.meos.intspan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String intspan_out(Pointer s) {
		var _result = MeosLibrary.meos.intspan_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspanset_in(String str) {
		var _result = MeosLibrary.meos.intspanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String intspanset_out(Pointer ss) {
		var _result = MeosLibrary.meos.intspanset_out(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String set_as_hexwkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.set_as_hexwkb(s, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_as_wkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.set_as_wkb(s, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_from_hexwkb(String hexwkb) {
		var _result = MeosLibrary.meos.set_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_from_wkb(Pointer wkb, long size) {
		var _result = MeosLibrary.meos.set_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String span_as_hexwkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.span_as_hexwkb(s, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_as_wkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.span_as_wkb(s, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_from_hexwkb(String hexwkb) {
		var _result = MeosLibrary.meos.span_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_from_wkb(Pointer wkb, long size) {
		var _result = MeosLibrary.meos.span_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String spanset_as_hexwkb(Pointer ss, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.spanset_as_hexwkb(ss, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_as_wkb(Pointer ss, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.spanset_as_wkb(ss, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_from_hexwkb(String hexwkb) {
		var _result = MeosLibrary.meos.spanset_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_from_wkb(Pointer wkb, long size) {
		var _result = MeosLibrary.meos.spanset_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_in(String str) {
		var _result = MeosLibrary.meos.textset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String textset_out(Pointer set) {
		var _result = MeosLibrary.meos.textset_out(set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_in(String str) {
		var _result = MeosLibrary.meos.tstzset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tstzset_out(Pointer set) {
		var _result = MeosLibrary.meos.tstzset_out(set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_in(String str) {
		var _result = MeosLibrary.meos.tstzspan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tstzspan_out(Pointer s) {
		var _result = MeosLibrary.meos.tstzspan_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_in(String str) {
		var _result = MeosLibrary.meos.tstzspanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tstzspanset_out(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_out(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_make(Pointer values, int count) {
		var _result = MeosLibrary.meos.bigintset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc) {
		var _result = MeosLibrary.meos.bigintspan_make(lower, upper, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_make(Pointer values, int count) {
		var _result = MeosLibrary.meos.dateset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) {
		var _result = MeosLibrary.meos.datespan_make(lower, upper, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_make(Pointer values, int count) {
		var _result = MeosLibrary.meos.floatset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc) {
		var _result = MeosLibrary.meos.floatspan_make(lower, upper, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_make(Pointer values, int count) {
		var _result = MeosLibrary.meos.intset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) {
		var _result = MeosLibrary.meos.intspan_make(lower, upper, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_copy(Pointer s) {
		var _result = MeosLibrary.meos.set_copy(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_copy(Pointer s) {
		var _result = MeosLibrary.meos.span_copy(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_copy(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_copy(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_make(Pointer spans, int count) {
		var _result = MeosLibrary.meos.spanset_make(spans, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_make(Pointer values, int count) {
		var _result = MeosLibrary.meos.textset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_make(Pointer values, int count) {
		var _result = MeosLibrary.meos.tstzset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_make(OffsetDateTime lower, OffsetDateTime upper, boolean lower_inc, boolean upper_inc) {
		var lower_new = utils.TimestampTzConverter.toTimestampTz(lower);
		var upper_new = utils.TimestampTzConverter.toTimestampTz(upper);
		var _result = MeosLibrary.meos.tstzspan_make(lower_new, upper_new, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_to_set(long i) {
		var _result = MeosLibrary.meos.bigint_to_set(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_to_span(int i) {
		var _result = MeosLibrary.meos.bigint_to_span(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_to_spanset(int i) {
		var _result = MeosLibrary.meos.bigint_to_spanset(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_to_set(int d) {
		var _result = MeosLibrary.meos.date_to_set(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_to_span(int d) {
		var _result = MeosLibrary.meos.date_to_span(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_to_spanset(int d) {
		var _result = MeosLibrary.meos.date_to_spanset(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_to_tstzset(Pointer s) {
		var _result = MeosLibrary.meos.dateset_to_tstzset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_to_tstzspan(Pointer s) {
		var _result = MeosLibrary.meos.datespan_to_tstzspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_to_tstzspanset(Pointer ss) {
		var _result = MeosLibrary.meos.datespanset_to_tstzspanset(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_to_set(double d) {
		var _result = MeosLibrary.meos.float_to_set(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_to_span(double d) {
		var _result = MeosLibrary.meos.float_to_span(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_to_spanset(double d) {
		var _result = MeosLibrary.meos.float_to_spanset(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_to_intset(Pointer s) {
		var _result = MeosLibrary.meos.floatset_to_intset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_to_intspan(Pointer s) {
		var _result = MeosLibrary.meos.floatspan_to_intspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_to_intspanset(Pointer ss) {
		var _result = MeosLibrary.meos.floatspanset_to_intspanset(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_to_set(int i) {
		var _result = MeosLibrary.meos.int_to_set(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_to_span(int i) {
		var _result = MeosLibrary.meos.int_to_span(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_to_spanset(int i) {
		var _result = MeosLibrary.meos.int_to_spanset(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_to_floatset(Pointer s) {
		var _result = MeosLibrary.meos.intset_to_floatset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_to_floatspan(Pointer s) {
		var _result = MeosLibrary.meos.intspan_to_floatspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspanset_to_floatspanset(Pointer ss) {
		var _result = MeosLibrary.meos.intspanset_to_floatspanset(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_to_span(Pointer s) {
		var _result = MeosLibrary.meos.set_to_span(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_to_spanset(Pointer s) {
		var _result = MeosLibrary.meos.set_to_spanset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_to_spanset(Pointer s) {
		var _result = MeosLibrary.meos.span_to_spanset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_to_set(Pointer txt) {
		var _result = MeosLibrary.meos.text_to_set(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_set(OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_to_set(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_span(OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_to_span(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_spanset(OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_to_spanset(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_to_dateset(Pointer s) {
		var _result = MeosLibrary.meos.tstzset_to_dateset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_to_datespan(Pointer s) {
		var _result = MeosLibrary.meos.tstzspan_to_datespan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_to_datespanset(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_to_datespanset(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintset_end_value(Pointer s) {
		var _result = MeosLibrary.meos.bigintset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintset_start_value(Pointer s) {
		var _result = MeosLibrary.meos.bigintset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.bigintset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}


	@SuppressWarnings("unused")
	public static long bigintspan_lower(Pointer s) {
		var _result = MeosLibrary.meos.bigintspan_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspan_upper(Pointer s) {
		var _result = MeosLibrary.meos.bigintspan_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspan_width(Pointer s) {
		var _result = MeosLibrary.meos.bigintspan_width(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspanset_lower(Pointer ss) {
		var _result = MeosLibrary.meos.bigintspanset_lower(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspanset_upper(Pointer ss) {
		var _result = MeosLibrary.meos.bigintspanset_upper(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspanset_width(Pointer ss, boolean boundspan) {
		var _result = MeosLibrary.meos.bigintspanset_width(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int dateset_end_value(Pointer s) {
		var _result = MeosLibrary.meos.dateset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int dateset_start_value(Pointer s) {
		var _result = MeosLibrary.meos.dateset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = MeosLibrary.meos.dateset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}


	@SuppressWarnings("unused")
	public static Pointer datespan_duration(Pointer s) {
		var _result = MeosLibrary.meos.datespan_duration(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespan_lower(Pointer s) {
		var _result = MeosLibrary.meos.datespan_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespan_upper(Pointer s) {
		var _result = MeosLibrary.meos.datespan_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_date_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = MeosLibrary.meos.datespanset_date_n(ss, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_dates(Pointer ss) {
		var _result = MeosLibrary.meos.datespanset_dates(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_duration(Pointer ss, boolean boundspan) {
		var _result = MeosLibrary.meos.datespanset_duration(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespanset_end_date(Pointer ss) {
		var _result = MeosLibrary.meos.datespanset_end_date(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespanset_num_dates(Pointer ss) {
		var _result = MeosLibrary.meos.datespanset_num_dates(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespanset_start_date(Pointer ss) {
		var _result = MeosLibrary.meos.datespanset_start_date(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatset_end_value(Pointer s) {
		var _result = MeosLibrary.meos.floatset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatset_start_value(Pointer s) {
		var _result = MeosLibrary.meos.floatset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.floatset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}


	@SuppressWarnings("unused")
	public static double floatspan_lower(Pointer s) {
		var _result = MeosLibrary.meos.floatspan_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspan_upper(Pointer s) {
		var _result = MeosLibrary.meos.floatspan_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspan_width(Pointer s) {
		var _result = MeosLibrary.meos.floatspan_width(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspanset_lower(Pointer ss) {
		var _result = MeosLibrary.meos.floatspanset_lower(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspanset_upper(Pointer ss) {
		var _result = MeosLibrary.meos.floatspanset_upper(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspanset_width(Pointer ss, boolean boundspan) {
		var _result = MeosLibrary.meos.floatspanset_width(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intset_end_value(Pointer s) {
		var _result = MeosLibrary.meos.intset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intset_start_value(Pointer s) {
		var _result = MeosLibrary.meos.intset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = MeosLibrary.meos.intset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}


	@SuppressWarnings("unused")
	public static int intspan_lower(Pointer s) {
		var _result = MeosLibrary.meos.intspan_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspan_upper(Pointer s) {
		var _result = MeosLibrary.meos.intspan_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspan_width(Pointer s) {
		var _result = MeosLibrary.meos.intspan_width(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspanset_lower(Pointer ss) {
		var _result = MeosLibrary.meos.intspanset_lower(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspanset_upper(Pointer ss) {
		var _result = MeosLibrary.meos.intspanset_upper(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspanset_width(Pointer ss, boolean boundspan) {
		var _result = MeosLibrary.meos.intspanset_width(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int set_hash(Pointer s) {
		var _result = MeosLibrary.meos.set_hash(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long set_hash_extended(Pointer s, long seed) {
		var _result = MeosLibrary.meos.set_hash_extended(s, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int set_num_values(Pointer s) {
		var _result = MeosLibrary.meos.set_num_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int span_hash(Pointer s) {
		var _result = MeosLibrary.meos.span_hash(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long span_hash_extended(Pointer s, long seed) {
		var _result = MeosLibrary.meos.span_hash_extended(s, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_lower_inc(Pointer s) {
		var _result = MeosLibrary.meos.span_lower_inc(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_upper_inc(Pointer s) {
		var _result = MeosLibrary.meos.span_upper_inc(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_end_span(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_end_span(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_hash(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_hash(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long spanset_hash_extended(Pointer ss, long seed) {
		var _result = MeosLibrary.meos.spanset_hash_extended(ss, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_lower_inc(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_lower_inc(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_num_spans(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_num_spans(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_span(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_span(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_span_n(Pointer ss, int i) {
		var _result = MeosLibrary.meos.spanset_span_n(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}


	@SuppressWarnings("unused")
	public static Pointer spanset_start_span(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_start_span(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_upper_inc(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_upper_inc(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_end_value(Pointer s) {
		var _result = MeosLibrary.meos.textset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_start_value(Pointer s) {
		var _result = MeosLibrary.meos.textset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.textset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}


	@SuppressWarnings("unused")
	public static OffsetDateTime tstzset_end_value(Pointer s) {
		var _result = MeosLibrary.meos.tstzset_end_value(s);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzset_start_value(Pointer s) {
		var _result = MeosLibrary.meos.tstzset_start_value(s);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tstzset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}


	@SuppressWarnings("unused")
	public static Pointer tstzspan_duration(Pointer s) {
		var _result = MeosLibrary.meos.tstzspan_duration(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspan_lower(Pointer s) {
		var _result = MeosLibrary.meos.tstzspan_lower(s);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspan_upper(Pointer s) {
		var _result = MeosLibrary.meos.tstzspan_upper(s);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_duration(Pointer ss, boolean boundspan) {
		var _result = MeosLibrary.meos.tstzspanset_duration(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_end_timestamptz(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_end_timestamptz(ss);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_lower(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_lower(ss);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static int tstzspanset_num_timestamps(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_num_timestamps(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_start_timestamptz(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_start_timestamptz(ss);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_timestamps(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_timestamps(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_timestamptz_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tstzspanset_timestamptz_n(ss, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_upper(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_upper(ss);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.bigintset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.bigintspan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.bigintspanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.dateset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.datespan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.datespanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_ceil(Pointer s) {
		var _result = MeosLibrary.meos.floatset_ceil(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_degrees(Pointer s, boolean normalize) {
		var _result = MeosLibrary.meos.floatset_degrees(s, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_floor(Pointer s) {
		var _result = MeosLibrary.meos.floatset_floor(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_radians(Pointer s) {
		var _result = MeosLibrary.meos.floatset_radians(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.floatset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_ceil(Pointer s) {
		var _result = MeosLibrary.meos.floatspan_ceil(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_degrees(Pointer s, boolean normalize) {
		var _result = MeosLibrary.meos.floatspan_degrees(s, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_floor(Pointer s) {
		var _result = MeosLibrary.meos.floatspan_floor(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_radians(Pointer s) {
		var _result = MeosLibrary.meos.floatspan_radians(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_round(Pointer s, int maxdd) {
		var _result = MeosLibrary.meos.floatspan_round(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.floatspan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_ceil(Pointer ss) {
		var _result = MeosLibrary.meos.floatspanset_ceil(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_floor(Pointer ss) {
		var _result = MeosLibrary.meos.floatspanset_floor(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_degrees(Pointer ss, boolean normalize) {
		var _result = MeosLibrary.meos.floatspanset_degrees(ss, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_radians(Pointer ss) {
		var _result = MeosLibrary.meos.floatspanset_radians(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_round(Pointer ss, int maxdd) {
		var _result = MeosLibrary.meos.floatspanset_round(ss, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.floatspanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.intset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.intspan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.intspanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_expand(Pointer s, Pointer interv) {
		var _result = MeosLibrary.meos.tstzspan_expand(s, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_round(Pointer s, int maxdd) {
		var _result = MeosLibrary.meos.set_round(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_text_textset(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.textcat_text_textset(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_textset_text(Pointer s, Pointer txt) {
		var _result = MeosLibrary.meos.textcat_textset_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_initcap(Pointer s) {
		var _result = MeosLibrary.meos.textset_initcap(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_lower(Pointer s) {
		var _result = MeosLibrary.meos.textset_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_upper(Pointer s) {
		var _result = MeosLibrary.meos.textset_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_tprecision(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.timestamptz_tprecision(t_new, duration, torigin_new);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_shift_scale(Pointer s, Pointer shift, Pointer duration) {
		var _result = MeosLibrary.meos.tstzset_shift_scale(s, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tstzset_tprecision(s, duration, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_shift_scale(Pointer s, Pointer shift, Pointer duration) {
		var _result = MeosLibrary.meos.tstzspan_shift_scale(s, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tstzspan_tprecision(s, duration, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_shift_scale(Pointer ss, Pointer shift, Pointer duration) {
		var _result = MeosLibrary.meos.tstzspanset_shift_scale(ss, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_tprecision(Pointer ss, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tstzspanset_tprecision(ss, duration, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int set_cmp(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.set_cmp(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_eq(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.set_eq(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_ge(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.set_ge(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_gt(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.set_gt(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_le(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.set_le(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_lt(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.set_lt(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_ne(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.set_ne(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int span_cmp(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.span_cmp(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_eq(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.span_eq(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_ge(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.span_ge(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_gt(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.span_gt(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_le(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.span_le(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_lt(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.span_lt(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_ne(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.span_ne(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_cmp(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.spanset_cmp(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_eq(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.spanset_eq(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_ge(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.spanset_ge(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_gt(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.spanset_gt(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_le(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.spanset_le(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_lt(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.spanset_lt(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_ne(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.spanset_ne(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}


	@SuppressWarnings("unused")
	public static Pointer set_split_each_n_spans(Pointer s, int elems_per_span, Pointer count) {
		var _result = MeosLibrary.meos.set_split_each_n_spans(s, elems_per_span, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_split_n_spans(Pointer s, int span_count, Pointer count) {
		var _result = MeosLibrary.meos.set_split_n_spans(s, span_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}


	@SuppressWarnings("unused")
	public static Pointer spanset_split_each_n_spans(Pointer ss, int elems_per_span, Pointer count) {
		var _result = MeosLibrary.meos.spanset_split_each_n_spans(ss, elems_per_span, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_split_n_spans(Pointer ss, int span_count, Pointer count) {
		var _result = MeosLibrary.meos.spanset_split_n_spans(ss, span_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.adjacent_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.adjacent_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.adjacent_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.adjacent_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.adjacent_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.adjacent_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.adjacent_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.adjacent_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.adjacent_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.adjacent_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.adjacent_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.adjacent_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.adjacent_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.adjacent_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_bigint_set(long i, Pointer s) {
		var _result = MeosLibrary.meos.contained_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_bigint_span(long i, Pointer s) {
		var _result = MeosLibrary.meos.contained_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_bigint_spanset(long i, Pointer ss) {
		var _result = MeosLibrary.meos.contained_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_date_set(int d, Pointer s) {
		var _result = MeosLibrary.meos.contained_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_date_span(int d, Pointer s) {
		var _result = MeosLibrary.meos.contained_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_date_spanset(int d, Pointer ss) {
		var _result = MeosLibrary.meos.contained_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_float_set(double d, Pointer s) {
		var _result = MeosLibrary.meos.contained_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_float_span(double d, Pointer s) {
		var _result = MeosLibrary.meos.contained_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_float_spanset(double d, Pointer ss) {
		var _result = MeosLibrary.meos.contained_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_int_set(int i, Pointer s) {
		var _result = MeosLibrary.meos.contained_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_int_span(int i, Pointer s) {
		var _result = MeosLibrary.meos.contained_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_int_spanset(int i, Pointer ss) {
		var _result = MeosLibrary.meos.contained_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.contained_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.contained_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.contained_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.contained_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.contained_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_text_set(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.contained_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.contained_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.contained_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.contained_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.contains_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.contains_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.contains_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.contains_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.contains_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_text(Pointer s, Pointer t) {
		var _result = MeosLibrary.meos.contains_set_text(s, t);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.contains_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.contains_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.contains_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.contains_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.contains_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.contains_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.contains_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.contains_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.contains_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.contains_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.contains_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.contains_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.contains_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.contains_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.contains_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.overlaps_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.overlaps_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.overlaps_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.overlaps_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.overlaps_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_date_set(int d, Pointer s) {
		var _result = MeosLibrary.meos.after_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_date_span(int d, Pointer s) {
		var _result = MeosLibrary.meos.after_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_date_spanset(int d, Pointer ss) {
		var _result = MeosLibrary.meos.after_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.after_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.after_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.after_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.after_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.after_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.after_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.after_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.after_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.after_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_date_set(int d, Pointer s) {
		var _result = MeosLibrary.meos.before_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_date_span(int d, Pointer s) {
		var _result = MeosLibrary.meos.before_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_date_spanset(int d, Pointer ss) {
		var _result = MeosLibrary.meos.before_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.before_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.before_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.before_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.before_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.before_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.before_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.before_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.before_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.before_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_bigint_set(long i, Pointer s) {
		var _result = MeosLibrary.meos.left_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_bigint_span(long i, Pointer s) {
		var _result = MeosLibrary.meos.left_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_bigint_spanset(long i, Pointer ss) {
		var _result = MeosLibrary.meos.left_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_float_set(double d, Pointer s) {
		var _result = MeosLibrary.meos.left_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_float_span(double d, Pointer s) {
		var _result = MeosLibrary.meos.left_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_float_spanset(double d, Pointer ss) {
		var _result = MeosLibrary.meos.left_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_int_set(int i, Pointer s) {
		var _result = MeosLibrary.meos.left_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_int_span(int i, Pointer s) {
		var _result = MeosLibrary.meos.left_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_int_spanset(int i, Pointer ss) {
		var _result = MeosLibrary.meos.left_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.left_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.left_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.left_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.left_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_text(Pointer s, Pointer txt) {
		var _result = MeosLibrary.meos.left_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.left_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.left_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.left_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.left_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.left_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.left_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.left_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.left_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.left_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.left_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_text_set(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.left_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_date_set(int d, Pointer s) {
		var _result = MeosLibrary.meos.overafter_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_date_span(int d, Pointer s) {
		var _result = MeosLibrary.meos.overafter_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_date_spanset(int d, Pointer ss) {
		var _result = MeosLibrary.meos.overafter_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.overafter_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overafter_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.overafter_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overafter_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.overafter_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overafter_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overafter_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overafter_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overafter_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_date_set(int d, Pointer s) {
		var _result = MeosLibrary.meos.overbefore_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_date_span(int d, Pointer s) {
		var _result = MeosLibrary.meos.overbefore_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_date_spanset(int d, Pointer ss) {
		var _result = MeosLibrary.meos.overbefore_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.overbefore_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overbefore_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.overbefore_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overbefore_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.overbefore_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overbefore_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overbefore_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overbefore_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.overbefore_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_bigint_set(long i, Pointer s) {
		var _result = MeosLibrary.meos.overleft_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_bigint_span(long i, Pointer s) {
		var _result = MeosLibrary.meos.overleft_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_bigint_spanset(long i, Pointer ss) {
		var _result = MeosLibrary.meos.overleft_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_float_set(double d, Pointer s) {
		var _result = MeosLibrary.meos.overleft_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_float_span(double d, Pointer s) {
		var _result = MeosLibrary.meos.overleft_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_float_spanset(double d, Pointer ss) {
		var _result = MeosLibrary.meos.overleft_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_int_set(int i, Pointer s) {
		var _result = MeosLibrary.meos.overleft_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_int_span(int i, Pointer s) {
		var _result = MeosLibrary.meos.overleft_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_int_spanset(int i, Pointer ss) {
		var _result = MeosLibrary.meos.overleft_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.overleft_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.overleft_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.overleft_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.overleft_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_text(Pointer s, Pointer txt) {
		var _result = MeosLibrary.meos.overleft_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.overleft_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.overleft_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.overleft_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.overleft_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.overleft_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.overleft_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.overleft_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.overleft_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.overleft_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.overleft_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_text_set(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.overleft_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_bigint_set(long i, Pointer s) {
		var _result = MeosLibrary.meos.overright_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_bigint_span(long i, Pointer s) {
		var _result = MeosLibrary.meos.overright_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_bigint_spanset(long i, Pointer ss) {
		var _result = MeosLibrary.meos.overright_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_float_set(double d, Pointer s) {
		var _result = MeosLibrary.meos.overright_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_float_span(double d, Pointer s) {
		var _result = MeosLibrary.meos.overright_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_float_spanset(double d, Pointer ss) {
		var _result = MeosLibrary.meos.overright_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_int_set(int i, Pointer s) {
		var _result = MeosLibrary.meos.overright_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_int_span(int i, Pointer s) {
		var _result = MeosLibrary.meos.overright_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_int_spanset(int i, Pointer ss) {
		var _result = MeosLibrary.meos.overright_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.overright_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.overright_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.overright_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.overright_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_text(Pointer s, Pointer txt) {
		var _result = MeosLibrary.meos.overright_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.overright_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.overright_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.overright_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.overright_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.overright_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.overright_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.overright_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.overright_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.overright_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.overright_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_text_set(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.overright_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_bigint_set(long i, Pointer s) {
		var _result = MeosLibrary.meos.right_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_bigint_span(long i, Pointer s) {
		var _result = MeosLibrary.meos.right_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_bigint_spanset(long i, Pointer ss) {
		var _result = MeosLibrary.meos.right_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_float_set(double d, Pointer s) {
		var _result = MeosLibrary.meos.right_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_float_span(double d, Pointer s) {
		var _result = MeosLibrary.meos.right_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_float_spanset(double d, Pointer ss) {
		var _result = MeosLibrary.meos.right_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_int_set(int i, Pointer s) {
		var _result = MeosLibrary.meos.right_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_int_span(int i, Pointer s) {
		var _result = MeosLibrary.meos.right_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_int_spanset(int i, Pointer ss) {
		var _result = MeosLibrary.meos.right_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.right_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.right_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.right_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.right_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_text(Pointer s, Pointer txt) {
		var _result = MeosLibrary.meos.right_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.right_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.right_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.right_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.right_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.right_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.right_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.right_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.right_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.right_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.right_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_text_set(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.right_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_bigint_set(long i, Pointer s) {
		var _result = MeosLibrary.meos.intersection_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_date_set(int d, Pointer s) {
		var _result = MeosLibrary.meos.intersection_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_float_set(double d, Pointer s) {
		var _result = MeosLibrary.meos.intersection_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_int_set(int i, Pointer s) {
		var _result = MeosLibrary.meos.intersection_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.intersection_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.intersection_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.intersection_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.intersection_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.intersection_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_text(Pointer s, Pointer txt) {
		var _result = MeosLibrary.meos.intersection_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.intersection_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.intersection_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.intersection_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.intersection_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.intersection_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.intersection_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.intersection_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.intersection_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.intersection_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.intersection_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.intersection_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.intersection_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.intersection_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.intersection_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.intersection_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_text_set(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.intersection_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.intersection_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_bigint_set(long i, Pointer s) {
		var _result = MeosLibrary.meos.minus_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_bigint_span(long i, Pointer s) {
		var _result = MeosLibrary.meos.minus_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_bigint_spanset(long i, Pointer ss) {
		var _result = MeosLibrary.meos.minus_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_date_set(int d, Pointer s) {
		var _result = MeosLibrary.meos.minus_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_date_span(int d, Pointer s) {
		var _result = MeosLibrary.meos.minus_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_date_spanset(int d, Pointer ss) {
		var _result = MeosLibrary.meos.minus_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_float_set(double d, Pointer s) {
		var _result = MeosLibrary.meos.minus_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_float_span(double d, Pointer s) {
		var _result = MeosLibrary.meos.minus_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_float_spanset(double d, Pointer ss) {
		var _result = MeosLibrary.meos.minus_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_int_set(int i, Pointer s) {
		var _result = MeosLibrary.meos.minus_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_int_span(int i, Pointer s) {
		var _result = MeosLibrary.meos.minus_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_int_spanset(int i, Pointer ss) {
		var _result = MeosLibrary.meos.minus_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.minus_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.minus_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.minus_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.minus_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.minus_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_text(Pointer s, Pointer txt) {
		var _result = MeosLibrary.meos.minus_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.minus_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.minus_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.minus_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.minus_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.minus_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.minus_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.minus_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.minus_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.minus_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.minus_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.minus_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.minus_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.minus_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.minus_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.minus_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_text_set(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.minus_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.minus_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.minus_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.minus_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_bigint_set(long i, Pointer s) {
		var _result = MeosLibrary.meos.union_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_bigint_span(Pointer s, long i) {
		var _result = MeosLibrary.meos.union_bigint_span(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_bigint_spanset(long i, Pointer ss) {
		var _result = MeosLibrary.meos.union_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_date_set(int d, Pointer s) {
		var _result = MeosLibrary.meos.union_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_date_span(Pointer s, int d) {
		var _result = MeosLibrary.meos.union_date_span(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_date_spanset(int d, Pointer ss) {
		var _result = MeosLibrary.meos.union_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_float_set(double d, Pointer s) {
		var _result = MeosLibrary.meos.union_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_float_span(Pointer s, double d) {
		var _result = MeosLibrary.meos.union_float_span(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_float_spanset(double d, Pointer ss) {
		var _result = MeosLibrary.meos.union_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_int_set(int i, Pointer s) {
		var _result = MeosLibrary.meos.union_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_int_span(int i, Pointer s) {
		var _result = MeosLibrary.meos.union_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_int_spanset(int i, Pointer ss) {
		var _result = MeosLibrary.meos.union_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.union_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.union_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.union_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.union_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_set(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.union_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_text(Pointer s, Pointer txt) {
		var _result = MeosLibrary.meos.union_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.union_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.union_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.union_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.union_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.union_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_span(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.union_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_spanset(Pointer s, Pointer ss) {
		var _result = MeosLibrary.meos.union_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.union_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.union_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.union_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.union_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.union_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_span(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.union_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.union_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.union_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_text_set(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.union_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.union_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.union_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.union_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_bigintset_bigintset(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_bigintset_bigintset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_bigintspan_bigintspan(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_bigintspan_bigintspan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_bigintspanset_bigintspan(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.distance_bigintspanset_bigintspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_bigintspanset_bigintspanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.distance_bigintspanset_bigintspanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_dateset_dateset(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_dateset_dateset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_datespan_datespan(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_datespan_datespan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_datespanset_datespan(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.distance_datespanset_datespan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_datespanset_datespanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.distance_datespanset_datespanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_floatset_floatset(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_floatset_floatset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_floatspan_floatspan(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_floatspan_floatspan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_floatspanset_floatspan(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.distance_floatspanset_floatspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_floatspanset_floatspanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.distance_floatspanset_floatspanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_intset_intset(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_intset_intset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_intspan_intspan(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_intspan_intspan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_intspanset_intspan(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.distance_intspanset_intspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_intspanset_intspanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.distance_intspanset_intspanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_set_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.distance_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_set_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.distance_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_set_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.distance_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_set_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.distance_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.distance_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_span_bigint(Pointer s, long i) {
		var _result = MeosLibrary.meos.distance_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_span_date(Pointer s, int d) {
		var _result = MeosLibrary.meos.distance_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_span_float(Pointer s, double d) {
		var _result = MeosLibrary.meos.distance_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_span_int(Pointer s, int i) {
		var _result = MeosLibrary.meos.distance_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.distance_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_spanset_bigint(Pointer ss, long i) {
		var _result = MeosLibrary.meos.distance_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_spanset_date(Pointer ss, int d) {
		var _result = MeosLibrary.meos.distance_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_spanset_float(Pointer ss, double d) {
		var _result = MeosLibrary.meos.distance_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_spanset_int(Pointer ss, int i) {
		var _result = MeosLibrary.meos.distance_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.distance_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_tstzset_tstzset(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_tstzset_tstzset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_tstzspan_tstzspan(Pointer s1, Pointer s2) {
		var _result = MeosLibrary.meos.distance_tstzspan_tstzspan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_tstzspanset_tstzspan(Pointer ss, Pointer s) {
		var _result = MeosLibrary.meos.distance_tstzspanset_tstzspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_tstzspanset_tstzspanset(Pointer ss1, Pointer ss2) {
		var _result = MeosLibrary.meos.distance_tstzspanset_tstzspanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_extent_transfn(Pointer state, long i) {
		var _result = MeosLibrary.meos.bigint_extent_transfn(state, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_union_transfn(Pointer state, long i) {
		var _result = MeosLibrary.meos.bigint_union_transfn(state, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_extent_transfn(Pointer state, int d) {
		var _result = MeosLibrary.meos.date_extent_transfn(state, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_union_transfn(Pointer state, int d) {
		var _result = MeosLibrary.meos.date_union_transfn(state, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_extent_transfn(Pointer state, double d) {
		var _result = MeosLibrary.meos.float_extent_transfn(state, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_union_transfn(Pointer state, double d) {
		var _result = MeosLibrary.meos.float_union_transfn(state, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_extent_transfn(Pointer state, int i) {
		var _result = MeosLibrary.meos.int_extent_transfn(state, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_union_transfn(Pointer state, int i) {
		var _result = MeosLibrary.meos.int_union_transfn(state, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_extent_transfn(Pointer state, Pointer s) {
		var _result = MeosLibrary.meos.set_extent_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_union_finalfn(Pointer state) {
		var _result = MeosLibrary.meos.set_union_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_union_transfn(Pointer state, Pointer s) {
		var _result = MeosLibrary.meos.set_union_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_extent_transfn(Pointer state, Pointer s) {
		var _result = MeosLibrary.meos.span_extent_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_union_transfn(Pointer state, Pointer s) {
		var _result = MeosLibrary.meos.span_union_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_extent_transfn(Pointer state, Pointer ss) {
		var _result = MeosLibrary.meos.spanset_extent_transfn(state, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_union_finalfn(Pointer state) {
		var _result = MeosLibrary.meos.spanset_union_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_union_transfn(Pointer state, Pointer ss) {
		var _result = MeosLibrary.meos.spanset_union_transfn(state, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_union_transfn(Pointer state, Pointer txt) {
		var _result = MeosLibrary.meos.text_union_transfn(state, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_extent_transfn(Pointer state, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_extent_transfn(state, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_union_transfn(Pointer state, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_union_transfn(state, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigint_get_bin(long value, long vsize, long vorigin) {
		var _result = MeosLibrary.meos.bigint_get_bin(value, vsize, vorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_bins(Pointer s, long vsize, long vorigin, Pointer count) {
		var _result = MeosLibrary.meos.bigintspan_bins(s, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspanset_bins(Pointer ss, long vsize, long vorigin, Pointer count) {
		var _result = MeosLibrary.meos.bigintspanset_bins(ss, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int date_get_bin(int d, Pointer duration, int torigin) {
		var _result = MeosLibrary.meos.date_get_bin(d, duration, torigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_bins(Pointer s, Pointer duration, int torigin, Pointer count) {
		var _result = MeosLibrary.meos.datespan_bins(s, duration, torigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_bins(Pointer ss, Pointer duration, int torigin, Pointer count) {
		var _result = MeosLibrary.meos.datespanset_bins(ss, duration, torigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_get_bin(double value, double vsize, double vorigin) {
		var _result = MeosLibrary.meos.float_get_bin(value, vsize, vorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_bins(Pointer s, double vsize, double vorigin, Pointer count) {
		var _result = MeosLibrary.meos.floatspan_bins(s, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_bins(Pointer ss, double vsize, double vorigin, Pointer count) {
		var _result = MeosLibrary.meos.floatspanset_bins(ss, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int int_get_bin(int value, int vsize, int vorigin) {
		var _result = MeosLibrary.meos.int_get_bin(value, vsize, vorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_bins(Pointer s, int vsize, int vorigin, Pointer count) {
		var _result = MeosLibrary.meos.intspan_bins(s, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspanset_bins(Pointer ss, int vsize, int vorigin, Pointer count) {
		var _result = MeosLibrary.meos.intspanset_bins(ss, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_get_bin(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.timestamptz_get_bin(t_new, duration, torigin_new);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_bins(Pointer s, Pointer duration, OffsetDateTime origin, Pointer count) {
		var origin_new = utils.TimestampTzConverter.toTimestampTz(origin);
		var _result = MeosLibrary.meos.tstzspan_bins(s, duration, origin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_bins(Pointer ss, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tstzspanset_bins(ss, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tbox_as_hexwkb(Pointer box, byte variant, Pointer size) {
		var _result = MeosLibrary.meos.tbox_as_hexwkb(box, variant, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_as_wkb(Pointer box, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.tbox_as_wkb(box, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_from_hexwkb(String hexwkb) {
		var _result = MeosLibrary.meos.tbox_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_from_wkb(Pointer wkb, long size) {
		var _result = MeosLibrary.meos.tbox_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_in(String str) {
		var _result = MeosLibrary.meos.tbox_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tbox_out(Pointer box, int maxdd) {
		var _result = MeosLibrary.meos.tbox_out(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_timestamptz_to_tbox(double d, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.float_timestamptz_to_tbox(d, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_tstzspan_to_tbox(double d, Pointer s) {
		var _result = MeosLibrary.meos.float_tstzspan_to_tbox(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_timestamptz_to_tbox(int i, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.int_timestamptz_to_tbox(i, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_tstzspan_to_tbox(int i, Pointer s) {
		var _result = MeosLibrary.meos.int_tstzspan_to_tbox(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s) {
		var _result = MeosLibrary.meos.numspan_tstzspan_to_tbox(span, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer numspan_timestamptz_to_tbox(Pointer span, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.numspan_timestamptz_to_tbox(span, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_copy(Pointer box) {
		var _result = MeosLibrary.meos.tbox_copy(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_make(Pointer s, Pointer p) {
		var _result = MeosLibrary.meos.tbox_make(s, p);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_to_tbox(double d) {
		var _result = MeosLibrary.meos.float_to_tbox(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_to_tbox(int i) {
		var _result = MeosLibrary.meos.int_to_tbox(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_to_tbox(Pointer s) {
		var _result = MeosLibrary.meos.set_to_tbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_to_tbox(Pointer s) {
		var _result = MeosLibrary.meos.span_to_tbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_to_tbox(Pointer ss) {
		var _result = MeosLibrary.meos.spanset_to_tbox(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_to_intspan(Pointer box) {
		var _result = MeosLibrary.meos.tbox_to_intspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_to_floatspan(Pointer box) {
		var _result = MeosLibrary.meos.tbox_to_floatspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_to_tstzspan(Pointer box) {
		var _result = MeosLibrary.meos.tbox_to_tstzspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_tbox(OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_to_tbox(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tbox_hash(Pointer box) {
		var _result = MeosLibrary.meos.tbox_hash(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long tbox_hash_extended(Pointer box, long seed) {
		var _result = MeosLibrary.meos.tbox_hash_extended(box, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_hast(Pointer box) {
		var _result = MeosLibrary.meos.tbox_hast(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_hasx(Pointer box) {
		var _result = MeosLibrary.meos.tbox_hasx(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_tmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_tmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_tmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = MeosLibrary.meos.tbox_tmax_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_tmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_tmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_tmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = MeosLibrary.meos.tbox_tmin_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.tbox_xmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_xmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = MeosLibrary.meos.tbox_xmax_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.tbox_xmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_xmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = MeosLibrary.meos.tbox_xmin_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboxfloat_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.tboxfloat_xmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboxfloat_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.tboxfloat_xmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboxint_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = MeosLibrary.meos.tboxint_xmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboxint_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = MeosLibrary.meos.tboxint_xmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_expand_time(Pointer box, Pointer interv) {
		var _result = MeosLibrary.meos.tbox_expand_time(box, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_round(Pointer box, int maxdd) {
		var _result = MeosLibrary.meos.tbox_round(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) {
		var _result = MeosLibrary.meos.tbox_shift_scale_time(box, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_expand(Pointer box, double d) {
		var _result = MeosLibrary.meos.tfloatbox_expand(box, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_shift_scale(Pointer box, double shift, double width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.tfloatbox_shift_scale(box, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_expand(Pointer box, int i) {
		var _result = MeosLibrary.meos.tintbox_expand(box, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_shift_scale(Pointer box, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = MeosLibrary.meos.tintbox_shift_scale(box, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict) {
		var _result = MeosLibrary.meos.union_tbox_tbox(box1, box2, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.intersection_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.adjacent_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.contained_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.contains_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overlaps_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.same_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.after_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.before_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.left_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overafter_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overbefore_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overleft_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overright_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.right_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tbox_cmp(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.tbox_cmp(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_eq(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.tbox_eq(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_ge(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.tbox_ge(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_gt(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.tbox_gt(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_le(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.tbox_le(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_lt(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.tbox_lt(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_ne(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.tbox_ne(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_from_mfjson(String str) {
		var _result = MeosLibrary.meos.tbool_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_in(String str) {
		var _result = MeosLibrary.meos.tbool_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tbool_out(Pointer temp) {
		var _result = MeosLibrary.meos.tbool_out(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_as_hexwkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.temporal_as_hexwkb(temp, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs) {
		var _result = MeosLibrary.meos.temporal_as_mfjson(temp, with_bbox, flags, precision, srs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_as_wkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.temporal_as_wkb(temp, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_from_hexwkb(String hexwkb) {
		var _result = MeosLibrary.meos.temporal_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_from_wkb(Pointer wkb, long size) {
		var _result = MeosLibrary.meos.temporal_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_from_mfjson(String str) {
		var _result = MeosLibrary.meos.tfloat_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_in(String str) {
		var _result = MeosLibrary.meos.tfloat_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tfloat_out(Pointer temp, int maxdd) {
		var _result = MeosLibrary.meos.tfloat_out(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_from_mfjson(String str) {
		var _result = MeosLibrary.meos.tint_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_in(String str) {
		var _result = MeosLibrary.meos.tint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tint_out(Pointer temp) {
		var _result = MeosLibrary.meos.tint_out(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_from_mfjson(String str) {
		var _result = MeosLibrary.meos.ttext_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_in(String str) {
		var _result = MeosLibrary.meos.ttext_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String ttext_out(Pointer temp) {
		var _result = MeosLibrary.meos.ttext_out(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_from_base_temp(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.tbool_from_base_temp(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolinst_make(boolean b, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tboolinst_make(b, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_tstzset(boolean b, Pointer s) {
		var _result = MeosLibrary.meos.tboolseq_from_base_tstzset(b, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_tstzspan(boolean b, Pointer s) {
		var _result = MeosLibrary.meos.tboolseq_from_base_tstzspan(b, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseqset_from_base_tstzspanset(boolean b, Pointer ss) {
		var _result = MeosLibrary.meos.tboolseqset_from_base_tstzspanset(b, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_copy(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_copy(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_from_base_temp(double d, Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_from_base_temp(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatinst_make(double d, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tfloatinst_make(d, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_tstzset(double d, Pointer s) {
		var _result = MeosLibrary.meos.tfloatseq_from_base_tstzset(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_tstzspan(double d, Pointer s, int interp) {
		var _result = MeosLibrary.meos.tfloatseq_from_base_tstzspan(d, s, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_from_base_tstzspanset(double d, Pointer ss, int interp) {
		var _result = MeosLibrary.meos.tfloatseqset_from_base_tstzspanset(d, ss, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_from_base_temp(int i, Pointer temp) {
		var _result = MeosLibrary.meos.tint_from_base_temp(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintinst_make(int i, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tintinst_make(i, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_tstzset(int i, Pointer s) {
		var _result = MeosLibrary.meos.tintseq_from_base_tstzset(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_tstzspan(int i, Pointer s) {
		var _result = MeosLibrary.meos.tintseq_from_base_tstzspan(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseqset_from_base_tstzspanset(int i, Pointer ss) {
		var _result = MeosLibrary.meos.tintseqset_from_base_tstzspanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		var _result = MeosLibrary.meos.tsequence_make(instants, count, lower_inc, upper_inc, interp, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize) {
		var _result = MeosLibrary.meos.tsequenceset_make(sequences, count, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist) {
		var _result = MeosLibrary.meos.tsequenceset_make_gaps(instants, count, interp, maxt, maxdist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_from_base_temp(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.ttext_from_base_temp(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextinst_make(Pointer txt, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.ttextinst_make(txt, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.ttextseq_from_base_tstzset(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s) {
		var _result = MeosLibrary.meos.ttextseq_from_base_tstzspan(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss) {
		var _result = MeosLibrary.meos.ttextseqset_from_base_tstzspanset(txt, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_to_tint(Pointer temp) {
		var _result = MeosLibrary.meos.tbool_to_tint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_to_tstzspan(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_to_tstzspan(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_to_tint(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_to_tint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_to_tfloat(Pointer temp) {
		var _result = MeosLibrary.meos.tint_to_tfloat(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_to_span(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_to_span(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_to_tbox(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_to_tbox(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbool_end_value(Pointer temp) {
		var _result = MeosLibrary.meos.tbool_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbool_start_value(Pointer temp) {
		var _result = MeosLibrary.meos.tbool_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbool_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tbool_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = MeosLibrary.meos.tbool_value_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_values(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.tbool_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_duration(Pointer temp, boolean boundspan) {
		var _result = MeosLibrary.meos.temporal_duration(temp, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_end_instant(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_end_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_end_sequence(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_end_sequence(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_end_timestamptz(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_end_timestamptz(temp);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static int temporal_hash(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_hash(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_instant_n(Pointer temp, int n) {
		var _result = MeosLibrary.meos.temporal_instant_n(temp, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_instants(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.temporal_instants(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_interp(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_interp(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_lower_inc(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_lower_inc(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_max_instant(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_max_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_min_instant(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_min_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_num_instants(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_num_instants(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_num_sequences(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_num_sequences(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_num_timestamps(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_num_timestamps(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_segm_duration(Pointer temp, Pointer duration, boolean atleast, boolean strict) {
		var _result = MeosLibrary.meos.temporal_segm_duration(temp, duration, atleast, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_segments(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.temporal_segments(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_sequence_n(Pointer temp, int i) {
		var _result = MeosLibrary.meos.temporal_sequence_n(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_sequences(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.temporal_sequences(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_start_instant(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_start_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_start_sequence(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_start_sequence(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_start_timestamptz(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_start_timestamptz(temp);
		MeosErrorHandler.checkError();
		return utils.TimestampTzConverter.toOffsetDateTime(_result);
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration) {
		var _result = MeosLibrary.meos.temporal_stops(temp, maxdist, minduration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_subtype(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_subtype(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_time(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_time(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_timestamps(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.temporal_timestamps(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_timestamptz_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.temporal_timestamptz_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_upper_inc(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_upper_inc(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_end_value(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_min_value(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_min_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_max_value(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_max_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_start_value(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tfloat_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tfloat_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.tfloat_value_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_values(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.tfloat_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tint_end_value(Pointer temp) {
		var _result = MeosLibrary.meos.tint_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tint_max_value(Pointer temp) {
		var _result = MeosLibrary.meos.tint_max_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tint_min_value(Pointer temp) {
		var _result = MeosLibrary.meos.tint_min_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tint_start_value(Pointer temp) {
		var _result = MeosLibrary.meos.tint_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tint_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tint_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = MeosLibrary.meos.tint_value_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_values(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.tint_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumber_avg_value(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_avg_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumber_integral(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_integral(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumber_twavg(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_twavg(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_valuespans(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_valuespans(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_end_value(Pointer temp) {
		var _result = MeosLibrary.meos.ttext_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_max_value(Pointer temp) {
		var _result = MeosLibrary.meos.ttext_max_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_min_value(Pointer temp) {
		var _result = MeosLibrary.meos.ttext_min_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_start_value(Pointer temp) {
		var _result = MeosLibrary.meos.ttext_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean ttext_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.ttext_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.ttext_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_values(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.ttext_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_degrees(double value, boolean normalize) {
		var _result = MeosLibrary.meos.float_degrees(value, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temparr_round(Pointer temp, int count, int maxdd) {
		var _result = MeosLibrary.meos.temparr_round(temp, count, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_round(Pointer temp, int maxdd) {
		var _result = MeosLibrary.meos.temporal_round(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_scale_time(Pointer temp, Pointer duration) {
		var _result = MeosLibrary.meos.temporal_scale_time(temp, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_set_interp(Pointer temp, int interp) {
		var _result = MeosLibrary.meos.temporal_set_interp(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration) {
		var _result = MeosLibrary.meos.temporal_shift_scale_time(temp, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_shift_time(Pointer temp, Pointer shift) {
		var _result = MeosLibrary.meos.temporal_shift_time(temp, shift);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_as_tinstant(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_as_tinstant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tsequence(Pointer temp, int interp) {
		var _result = MeosLibrary.meos.temporal_tsequence(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tsequenceset(Pointer temp, int interp) {
		var _result = MeosLibrary.meos.temporal_tsequenceset(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_ceil(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_ceil(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_degrees(Pointer temp, boolean normalize) {
		var _result = MeosLibrary.meos.tfloat_degrees(temp, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_floor(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_floor(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_radians(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_radians(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_scale_value(Pointer temp, double width) {
		var _result = MeosLibrary.meos.tfloat_scale_value(temp, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width) {
		var _result = MeosLibrary.meos.tfloat_shift_scale_value(temp, shift, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_shift_value(Pointer temp, double shift) {
		var _result = MeosLibrary.meos.tfloat_shift_value(temp, shift);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_scale_value(Pointer temp, int width) {
		var _result = MeosLibrary.meos.tint_scale_value(temp, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_shift_scale_value(Pointer temp, int shift, int width) {
		var _result = MeosLibrary.meos.tint_shift_scale_value(temp, shift, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_shift_value(Pointer temp, int shift) {
		var _result = MeosLibrary.meos.tint_shift_value(temp, shift);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_append_tinstant(Pointer temp, Pointer inst, int interp, double maxdist, Pointer maxt, boolean expand) {
		var _result = MeosLibrary.meos.temporal_append_tinstant(temp, inst, interp, maxdist, maxt, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand) {
		var _result = MeosLibrary.meos.temporal_append_tsequence(temp, seq, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_delete_timestamptz(Pointer temp, OffsetDateTime t, boolean connect) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.temporal_delete_timestamptz(temp, t_new, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzset(Pointer temp, Pointer s, boolean connect) {
		var _result = MeosLibrary.meos.temporal_delete_tstzset(temp, s, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzspan(Pointer temp, Pointer s, boolean connect) {
		var _result = MeosLibrary.meos.temporal_delete_tstzspan(temp, s, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect) {
		var _result = MeosLibrary.meos.temporal_delete_tstzspanset(temp, ss, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect) {
		var _result = MeosLibrary.meos.temporal_insert(temp1, temp2, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_merge(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_merge(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_merge_array(Pointer temparr, int count) {
		var _result = MeosLibrary.meos.temporal_merge_array(temparr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect) {
		var _result = MeosLibrary.meos.temporal_update(temp1, temp2, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_at_value(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.tbool_at_value(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_minus_value(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.tbool_minus_value(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_after_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.temporal_after_timestamptz(temp, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_max(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_at_max(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_min(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_at_min(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_timestamptz(Pointer temp, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.temporal_at_timestamptz(temp, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzset(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.temporal_at_tstzset(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.temporal_at_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzspanset(Pointer temp, Pointer ss) {
		var _result = MeosLibrary.meos.temporal_at_tstzspanset(temp, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_values(Pointer temp, Pointer set) {
		var _result = MeosLibrary.meos.temporal_at_values(temp, set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_before_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.temporal_before_timestamptz(temp, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_max(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_minus_max(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_min(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_minus_min(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_timestamptz(Pointer temp, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.temporal_minus_timestamptz(temp, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzset(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.temporal_minus_tstzset(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.temporal_minus_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzspanset(Pointer temp, Pointer ss) {
		var _result = MeosLibrary.meos.temporal_minus_tstzspanset(temp, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_values(Pointer temp, Pointer set) {
		var _result = MeosLibrary.meos.temporal_minus_values(temp, set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_at_value(Pointer temp, double d) {
		var _result = MeosLibrary.meos.tfloat_at_value(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_minus_value(Pointer temp, double d) {
		var _result = MeosLibrary.meos.tfloat_minus_value(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_at_value(Pointer temp, int i) {
		var _result = MeosLibrary.meos.tint_at_value(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_minus_value(Pointer temp, int i) {
		var _result = MeosLibrary.meos.tint_minus_value(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_at_span(Pointer temp, Pointer span) {
		var _result = MeosLibrary.meos.tnumber_at_span(temp, span);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_at_spanset(Pointer temp, Pointer ss) {
		var _result = MeosLibrary.meos.tnumber_at_spanset(temp, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_at_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.tnumber_at_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_span(Pointer temp, Pointer span) {
		var _result = MeosLibrary.meos.tnumber_minus_span(temp, span);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_spanset(Pointer temp, Pointer ss) {
		var _result = MeosLibrary.meos.tnumber_minus_spanset(temp, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.tnumber_minus_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_at_value(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.ttext_at_value(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_minus_value(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.ttext_minus_value(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_cmp(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_cmp(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_eq(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_eq(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_ge(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_ge(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_gt(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_gt(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_le(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_le(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_lt(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_lt(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_ne(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_ne(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_bool_tbool(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.always_eq_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.always_eq_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.always_eq_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tbool_bool(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.always_eq_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.always_eq_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.always_eq_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.always_eq_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.always_eq_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.always_eq_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.always_ge_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.always_ge_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.always_ge_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.always_ge_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.always_ge_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.always_ge_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.always_ge_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.always_gt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.always_gt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.always_gt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.always_gt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.always_gt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.always_gt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.always_gt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.always_le_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.always_le_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.always_le_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.always_le_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.always_le_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.always_le_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.always_le_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.always_lt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.always_lt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.always_lt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.always_lt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.always_lt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.always_lt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.always_lt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_bool_tbool(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.always_ne_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.always_ne_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.always_ne_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tbool_bool(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.always_ne_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.always_ne_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.always_ne_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.always_ne_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.always_ne_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.always_ne_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_bool_tbool(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.ever_eq_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.ever_eq_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.ever_eq_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tbool_bool(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.ever_eq_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ever_eq_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.ever_eq_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.ever_eq_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.ever_eq_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.ever_eq_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.ever_ge_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.ever_ge_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ever_ge_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.ever_ge_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.ever_ge_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.ever_ge_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.ever_ge_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.ever_gt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.ever_gt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ever_gt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.ever_gt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.ever_gt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.ever_gt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.ever_gt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.ever_le_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.ever_le_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ever_le_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.ever_le_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.ever_le_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.ever_le_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.ever_le_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.ever_lt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.ever_lt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ever_lt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.ever_lt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.ever_lt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.ever_lt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.ever_lt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_bool_tbool(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.ever_ne_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.ever_ne_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.ever_ne_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tbool_bool(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.ever_ne_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ever_ne_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.ever_ne_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.ever_ne_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.ever_ne_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.ever_ne_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_bool_tbool(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.teq_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.teq_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.teq_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tbool_bool(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.teq_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.teq_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.teq_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.teq_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.teq_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.teq_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.tge_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.tge_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tge_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.tge_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.tge_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.tge_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.tge_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.tgt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.tgt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tgt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.tgt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.tgt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.tgt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.tgt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.tle_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.tle_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tle_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.tle_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.tle_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.tle_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.tle_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.tlt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.tlt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tlt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.tlt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.tlt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.tlt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.tlt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_bool_tbool(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.tne_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_float_tfloat(double d, Pointer temp) {
		var _result = MeosLibrary.meos.tne_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_int_tint(int i, Pointer temp) {
		var _result = MeosLibrary.meos.tne_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tbool_bool(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.tne_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tne_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.tne_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.tne_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.tne_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.tne_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_spans(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.temporal_spans(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_split_each_n_spans(Pointer temp, int elem_count, Pointer count) {
		var _result = MeosLibrary.meos.temporal_split_each_n_spans(temp, elem_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_split_n_spans(Pointer temp, int span_count, Pointer count) {
		var _result = MeosLibrary.meos.temporal_split_n_spans(temp, span_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_split_each_n_tboxes(Pointer temp, int elem_count, Pointer count) {
		var _result = MeosLibrary.meos.tnumber_split_each_n_tboxes(temp, elem_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_split_n_tboxes(Pointer temp, int box_count, Pointer count) {
		var _result = MeosLibrary.meos.tnumber_split_n_tboxes(temp, box_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_tboxes(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.tnumber_tboxes(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.adjacent_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.adjacent_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.adjacent_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.adjacent_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.adjacent_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.adjacent_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.adjacent_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.adjacent_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.contained_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.contained_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.contained_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.contained_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.contained_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.contained_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.contained_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.contained_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.contains_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.contains_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.contains_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.contains_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.contains_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.contains_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.contains_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.contains_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.overlaps_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overlaps_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overlaps_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.overlaps_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.overlaps_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overlaps_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overlaps_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.overlaps_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.same_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.same_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.same_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.same_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.same_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.same_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.same_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.same_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.after_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.after_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.after_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.after_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.after_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.after_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.before_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.before_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.before_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.before_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.before_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.before_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.left_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.left_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.left_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.left_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.left_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overafter_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.overafter_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overafter_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overafter_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overafter_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.overafter_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overbefore_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.overbefore_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overbefore_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overbefore_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overbefore_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.overbefore_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.overleft_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overleft_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.overleft_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overleft_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overleft_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.overright_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overright_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.overright_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overright_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overright_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.right_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.right_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = MeosLibrary.meos.right_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.right_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.right_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tand_bool_tbool(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.tand_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tand_tbool_bool(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.tand_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tand_tbool_tbool(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tand_tbool_tbool(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_when_true(Pointer temp) {
		var _result = MeosLibrary.meos.tbool_when_true(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnot_tbool(Pointer temp) {
		var _result = MeosLibrary.meos.tnot_tbool(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tor_bool_tbool(boolean b, Pointer temp) {
		var _result = MeosLibrary.meos.tor_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tor_tbool_bool(Pointer temp, boolean b) {
		var _result = MeosLibrary.meos.tor_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tor_tbool_tbool(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tor_tbool_tbool(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_float_tfloat(double d, Pointer tnumber) {
		var _result = MeosLibrary.meos.add_float_tfloat(d, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_int_tint(int i, Pointer tnumber) {
		var _result = MeosLibrary.meos.add_int_tint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_tfloat_float(Pointer tnumber, double d) {
		var _result = MeosLibrary.meos.add_tfloat_float(tnumber, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_tint_int(Pointer tnumber, int i) {
		var _result = MeosLibrary.meos.add_tint_int(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		var _result = MeosLibrary.meos.add_tnumber_tnumber(tnumber1, tnumber2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_float_tfloat(double d, Pointer tnumber) {
		var _result = MeosLibrary.meos.div_float_tfloat(d, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_int_tint(int i, Pointer tnumber) {
		var _result = MeosLibrary.meos.div_int_tint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_tfloat_float(Pointer tnumber, double d) {
		var _result = MeosLibrary.meos.div_tfloat_float(tnumber, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_tint_int(Pointer tnumber, int i) {
		var _result = MeosLibrary.meos.div_tint_int(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		var _result = MeosLibrary.meos.div_tnumber_tnumber(tnumber1, tnumber2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_float_tfloat(double d, Pointer tnumber) {
		var _result = MeosLibrary.meos.mul_float_tfloat(d, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_int_tint(int i, Pointer tnumber) {
		var _result = MeosLibrary.meos.mul_int_tint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_tfloat_float(Pointer tnumber, double d) {
		var _result = MeosLibrary.meos.mul_tfloat_float(tnumber, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_tint_int(Pointer tnumber, int i) {
		var _result = MeosLibrary.meos.mul_tint_int(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		var _result = MeosLibrary.meos.mul_tnumber_tnumber(tnumber1, tnumber2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_float_tfloat(double d, Pointer tnumber) {
		var _result = MeosLibrary.meos.sub_float_tfloat(d, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_int_tint(int i, Pointer tnumber) {
		var _result = MeosLibrary.meos.sub_int_tint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_tfloat_float(Pointer tnumber, double d) {
		var _result = MeosLibrary.meos.sub_tfloat_float(tnumber, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_tint_int(Pointer tnumber, int i) {
		var _result = MeosLibrary.meos.sub_tint_int(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		var _result = MeosLibrary.meos.sub_tnumber_tnumber(tnumber1, tnumber2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_derivative(Pointer temp) {
		var _result = MeosLibrary.meos.temporal_derivative(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_exp(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_exp(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_ln(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_ln(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_log10(Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_log10(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_abs(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_abs(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_trend(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_trend(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_angular_difference(double degrees1, double degrees2) {
		var _result = MeosLibrary.meos.float_angular_difference(degrees1, degrees2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_angular_difference(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_angular_difference(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_delta_value(Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_delta_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_text_ttext(Pointer txt, Pointer temp) {
		var _result = MeosLibrary.meos.textcat_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_ttext_text(Pointer temp, Pointer txt) {
		var _result = MeosLibrary.meos.textcat_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.textcat_ttext_ttext(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_initcap(Pointer temp) {
		var _result = MeosLibrary.meos.ttext_initcap(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_upper(Pointer temp) {
		var _result = MeosLibrary.meos.ttext_upper(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_lower(Pointer temp) {
		var _result = MeosLibrary.meos.ttext_lower(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.tdistance_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.tdistance_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tdistance_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.nad_tboxfloat_tboxfloat(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nad_tboxint_tboxint(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.nad_tboxint_tboxint(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tfloat_float(Pointer temp, double d) {
		var _result = MeosLibrary.meos.nad_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tfloat_tfloat(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.nad_tfloat_tfloat(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tfloat_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.nad_tfloat_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nad_tint_int(Pointer temp, int i) {
		var _result = MeosLibrary.meos.nad_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nad_tint_tbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.nad_tint_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nad_tint_tint(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.nad_tint_tint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_tand_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tbool_tand_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_tor_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tbool_tor_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_extent_transfn(Pointer s, Pointer temp) {
		var _result = MeosLibrary.meos.temporal_extent_transfn(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tagg_finalfn(Pointer state) {
		var _result = MeosLibrary.meos.temporal_tagg_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tcount_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.temporal_tcount_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_tmax_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_tmax_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_tmin_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_tmin_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_tsum_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tfloat_tsum_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_wmax_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = MeosLibrary.meos.tfloat_wmax_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_wmin_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = MeosLibrary.meos.tfloat_wmin_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_wsum_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = MeosLibrary.meos.tfloat_wsum_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_tcount_transfn(Pointer state, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_tcount_transfn(state, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_tmax_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tint_tmax_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_tmin_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tint_tmin_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_tsum_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tint_tsum_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_wmax_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = MeosLibrary.meos.tint_wmax_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_wmin_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = MeosLibrary.meos.tint_wmin_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_wsum_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = MeosLibrary.meos.tint_wsum_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_extent_transfn(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_extent_transfn(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_tavg_finalfn(Pointer state) {
		var _result = MeosLibrary.meos.tnumber_tavg_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_tavg_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tnumber_tavg_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_wavg_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = MeosLibrary.meos.tnumber_wavg_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_tcount_transfn(Pointer state, Pointer s) {
		var _result = MeosLibrary.meos.tstzset_tcount_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_tcount_transfn(Pointer state, Pointer s) {
		var _result = MeosLibrary.meos.tstzspan_tcount_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_tcount_transfn(Pointer state, Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_tcount_transfn(state, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_tmax_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.ttext_tmax_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_tmin_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.ttext_tmin_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_dp(Pointer temp, double eps_dist, boolean synchronize) {
		var _result = MeosLibrary.meos.temporal_simplify_dp(temp, eps_dist, synchronize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_max_dist(Pointer temp, double eps_dist, boolean synchronize) {
		var _result = MeosLibrary.meos.temporal_simplify_max_dist(temp, eps_dist, synchronize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_dist(Pointer temp, double dist) {
		var _result = MeosLibrary.meos.temporal_simplify_min_dist(temp, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_tdelta(Pointer temp, Pointer mint) {
		var _result = MeosLibrary.meos.temporal_simplify_min_tdelta(temp, mint);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tprecision(Pointer temp, Pointer duration, OffsetDateTime origin) {
		var origin_new = utils.TimestampTzConverter.toTimestampTz(origin);
		var _result = MeosLibrary.meos.temporal_tprecision(temp, duration, origin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tsample(Pointer temp, Pointer duration, OffsetDateTime origin, int interp) {
		var origin_new = utils.TimestampTzConverter.toTimestampTz(origin);
		var _result = MeosLibrary.meos.temporal_tsample(temp, duration, origin_new, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double temporal_dyntimewarp_distance(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_dyntimewarp_distance(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_dyntimewarp_path(Pointer temp1, Pointer temp2, Pointer count) {
		var _result = MeosLibrary.meos.temporal_dyntimewarp_path(temp1, temp2, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double temporal_frechet_distance(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_frechet_distance(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_frechet_path(Pointer temp1, Pointer temp2, Pointer count) {
		var _result = MeosLibrary.meos.temporal_frechet_path(temp1, temp2, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double temporal_hausdorff_distance(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.temporal_hausdorff_distance(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_time_bins(Pointer temp, Pointer duration, OffsetDateTime origin, Pointer count) {
		var origin_new = utils.TimestampTzConverter.toTimestampTz(origin);
		var _result = MeosLibrary.meos.temporal_time_bins(temp, duration, origin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_time_split(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer time_bins, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.temporal_time_split(temp, duration, torigin_new, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_time_boxes(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tfloat_time_boxes(temp, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_bins(Pointer temp, double vsize, double vorigin, Pointer count) {
		var _result = MeosLibrary.meos.tfloat_value_bins(temp, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_boxes(Pointer temp, double vsize, double vorigin, Pointer count) {
		var _result = MeosLibrary.meos.tfloat_value_boxes(temp, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer bins, Pointer count) {
		var _result = MeosLibrary.meos.tfloat_value_split(temp, size, origin, bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_time_boxes(Pointer temp, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tfloat_value_time_boxes(temp, vsize, duration, vorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_time_split(Pointer temp, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer value_bins, Pointer time_bins, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tfloat_value_time_split(temp, vsize, duration, vorigin, torigin_new, value_bins, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_time_tiles(Pointer box, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tfloatbox_time_tiles(box, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_value_tiles(Pointer box, double vsize, double vorigin, Pointer count) {
		var _result = MeosLibrary.meos.tfloatbox_value_tiles(box, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_value_time_tiles(Pointer box, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tfloatbox_value_time_tiles(box, vsize, duration, vorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_time_boxes(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tint_time_boxes(temp, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_bins(Pointer temp, int vsize, int vorigin, Pointer count) {
		var _result = MeosLibrary.meos.tint_value_bins(temp, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_boxes(Pointer temp, int vsize, int vorigin, Pointer count) {
		var _result = MeosLibrary.meos.tint_value_boxes(temp, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_split(Pointer temp, int vsize, int vorigin, Pointer bins, Pointer count) {
		var _result = MeosLibrary.meos.tint_value_split(temp, vsize, vorigin, bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_time_boxes(Pointer temp, int vsize, Pointer duration, int vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tint_value_time_boxes(temp, vsize, duration, vorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_time_split(Pointer temp, long size, Pointer duration, int vorigin, OffsetDateTime torigin, Pointer value_bins, Pointer time_bins, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tint_value_time_split(temp, size, duration, vorigin, torigin_new, value_bins, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_time_tiles(Pointer box, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tintbox_time_tiles(box, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_value_tiles(Pointer box, int xsize, int xorigin, Pointer count) {
		var _result = MeosLibrary.meos.tintbox_value_tiles(box, xsize, xorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_value_time_tiles(Pointer box, int xsize, Pointer duration, int xorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tintbox_value_time_tiles(box, xsize, duration, xorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_as_ewkb(Pointer gs, String endian, Pointer size) {
		var _result = MeosLibrary.meos.geo_as_ewkb(gs, endian, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_as_ewkt(Pointer gs, int precision) {
		var _result = MeosLibrary.meos.geo_as_ewkt(gs, precision);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_as_geojson(Pointer gs, int option, int precision, String srs) {
		var _result = MeosLibrary.meos.geo_as_geojson(gs, option, precision, srs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_as_hexewkb(Pointer gs, String endian) {
		var _result = MeosLibrary.meos.geo_as_hexewkb(gs, endian);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_as_text(Pointer gs, int precision) {
		var _result = MeosLibrary.meos.geo_as_text(gs, precision);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_from_ewkb(Pointer wkb, long wkb_size, int srid) {
		var _result = MeosLibrary.meos.geo_from_ewkb(wkb, wkb_size, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_from_geojson(String geojson) {
		var _result = MeosLibrary.meos.geo_from_geojson(geojson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_from_text(String wkt, int srid) {
		var _result = MeosLibrary.meos.geo_from_text(wkt, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_out(Pointer gs) {
		var _result = MeosLibrary.meos.geo_out(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_from_hexewkb(String wkt) {
		var _result = MeosLibrary.meos.geog_from_hexewkb(wkt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_in(String str, int typmod) {
		var _result = MeosLibrary.meos.geog_in(str, typmod);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_from_hexewkb(String wkt) {
		var _result = MeosLibrary.meos.geom_from_hexewkb(wkt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_in(String str, int typmod) {
		var _result = MeosLibrary.meos.geom_in(str, typmod);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer box3d_make(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, int srid) {
		var _result = MeosLibrary.meos.box3d_make(xmin, xmax, ymin, ymax, zmin, zmax, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String box3d_out(Pointer box, int maxdd) {
		var _result = MeosLibrary.meos.box3d_out(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer gbox_make(boolean hasz, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
		var _result = MeosLibrary.meos.gbox_make(hasz, xmin, xmax, ymin, ymax, zmin, zmax);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String gbox_out(Pointer box, int maxdd) {
		var _result = MeosLibrary.meos.gbox_out(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_copy(Pointer g) {
		var _result = MeosLibrary.meos.geo_copy(g);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geogpoint_make2d(int srid, double x, double y) {
		var _result = MeosLibrary.meos.geogpoint_make2d(srid, x, y);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geogpoint_make3dz(int srid, double x, double y, double z) {
		var _result = MeosLibrary.meos.geogpoint_make3dz(srid, x, y, z);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geompoint_make2d(int srid, double x, double y) {
		var _result = MeosLibrary.meos.geompoint_make2d(srid, x, y);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geompoint_make3dz(int srid, double x, double y, double z) {
		var _result = MeosLibrary.meos.geompoint_make3dz(srid, x, y, z);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_to_geog(Pointer geom) {
		var _result = MeosLibrary.meos.geom_to_geog(geom);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_to_geom(Pointer geog) {
		var _result = MeosLibrary.meos.geog_to_geom(geog);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geo_is_empty(Pointer g) {
		var _result = MeosLibrary.meos.geo_is_empty(g);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geo_is_unitary(Pointer gs) {
		var _result = MeosLibrary.meos.geo_is_unitary(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_typename(int type) {
		var _result = MeosLibrary.meos.geo_typename(type);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geog_area(Pointer g, boolean use_spheroid) {
		var _result = MeosLibrary.meos.geog_area(g, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_centroid(Pointer g, boolean use_spheroid) {
		var _result = MeosLibrary.meos.geog_centroid(g, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geog_length(Pointer g, boolean use_spheroid) {
		var _result = MeosLibrary.meos.geog_length(g, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geog_perimeter(Pointer g, boolean use_spheroid) {
		var _result = MeosLibrary.meos.geog_perimeter(g, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_azimuth(Pointer gs1, Pointer gs2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.geom_azimuth(gs1, gs2, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static double geom_length(Pointer gs) {
		var _result = MeosLibrary.meos.geom_length(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geom_perimeter(Pointer gs) {
		var _result = MeosLibrary.meos.geom_perimeter(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int line_numpoints(Pointer gs) {
		var _result = MeosLibrary.meos.line_numpoints(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer line_point_n(Pointer geom, int n) {
		var _result = MeosLibrary.meos.line_point_n(geom, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_reverse(Pointer gs) {
		var _result = MeosLibrary.meos.geo_reverse(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_round(Pointer gs, int maxdd) {
		var _result = MeosLibrary.meos.geo_round(gs, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_set_srid(Pointer gs, int srid) {
		var _result = MeosLibrary.meos.geo_set_srid(gs, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_srid(Pointer gs) {
		var _result = MeosLibrary.meos.geo_srid(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_transform(Pointer geom, int srid_to) {
		var _result = MeosLibrary.meos.geo_transform(geom, srid_to);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_transform_pipeline(Pointer gs, String pipeline, int srid_to, boolean is_forward) {
		var _result = MeosLibrary.meos.geo_transform_pipeline(gs, pipeline, srid_to, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_collect_garray(Pointer gsarr, int count) {
		var _result = MeosLibrary.meos.geo_collect_garray(gsarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_makeline_garray(Pointer gsarr, int count) {
		var _result = MeosLibrary.meos.geo_makeline_garray(gsarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_num_points(Pointer gs) {
		var _result = MeosLibrary.meos.geo_num_points(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_num_geos(Pointer gs) {
		var _result = MeosLibrary.meos.geo_num_geos(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_geo_n(Pointer geom, int n) {
		var _result = MeosLibrary.meos.geo_geo_n(geom, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_pointarr(Pointer gs, Pointer count) {
		var _result = MeosLibrary.meos.geo_pointarr(gs, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_points(Pointer gs) {
		var _result = MeosLibrary.meos.geo_points(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_array_union(Pointer gsarr, int count) {
		var _result = MeosLibrary.meos.geom_array_union(gsarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_boundary(Pointer gs) {
		var _result = MeosLibrary.meos.geom_boundary(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_buffer(Pointer gs, double size, String params) {
		var _result = MeosLibrary.meos.geom_buffer(gs, size, params);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_centroid(Pointer gs) {
		var _result = MeosLibrary.meos.geom_centroid(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_convex_hull(Pointer gs) {
		var _result = MeosLibrary.meos.geom_convex_hull(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_difference2d(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_difference2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_intersection2d(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_intersection2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_intersection2d_coll(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_intersection2d_coll(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_min_bounding_radius(Pointer geom, Pointer radius) {
		var _result = MeosLibrary.meos.geom_min_bounding_radius(geom, radius);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_shortestline2d(Pointer gs1, Pointer s2) {
		var _result = MeosLibrary.meos.geom_shortestline2d(gs1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_shortestline3d(Pointer gs1, Pointer s2) {
		var _result = MeosLibrary.meos.geom_shortestline3d(gs1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_unary_union(Pointer gs, double prec) {
		var _result = MeosLibrary.meos.geom_unary_union(gs, prec);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer line_interpolate_point(Pointer gs, double distance_fraction, boolean repeat) {
		var _result = MeosLibrary.meos.line_interpolate_point(gs, distance_fraction, repeat);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double line_locate_point(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.line_locate_point(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer line_substring(Pointer gs, double from, double to) {
		var _result = MeosLibrary.meos.line_substring(gs, from, to);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geog_dwithin(Pointer g1, Pointer g2, double tolerance, boolean use_spheroid) {
		var _result = MeosLibrary.meos.geog_dwithin(g1, g2, tolerance, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geog_intersects(Pointer gs1, Pointer gs2, boolean use_spheroid) {
		var _result = MeosLibrary.meos.geog_intersects(gs1, gs2, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_contains(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_contains(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_covers(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_covers(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_disjoint2d(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_disjoint2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_dwithin2d(Pointer gs1, Pointer gs2, double tolerance) {
		var _result = MeosLibrary.meos.geom_dwithin2d(gs1, gs2, tolerance);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_dwithin3d(Pointer gs1, Pointer gs2, double tolerance) {
		var _result = MeosLibrary.meos.geom_dwithin3d(gs1, gs2, tolerance);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_intersects2d(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_intersects2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_intersects3d(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_intersects3d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_relate_pattern(Pointer gs1, Pointer gs2, String patt) {
		var _result = MeosLibrary.meos.geom_relate_pattern(gs1, gs2, patt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_touches(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_touches(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_stboxes(Pointer gs, Pointer count) {
		var _result = MeosLibrary.meos.geo_stboxes(gs, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_split_each_n_stboxes(Pointer gs, int elem_count, Pointer count) {
		var _result = MeosLibrary.meos.geo_split_each_n_stboxes(gs, elem_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_split_n_stboxes(Pointer gs, int box_count, Pointer count) {
		var _result = MeosLibrary.meos.geo_split_n_stboxes(gs, box_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geog_distance(Pointer g1, Pointer g2) {
		var _result = MeosLibrary.meos.geog_distance(g1, g2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geom_distance2d(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_distance2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geom_distance3d(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geom_distance3d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_equals(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geo_equals(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geo_same(Pointer gs1, Pointer gs2) {
		var _result = MeosLibrary.meos.geo_same(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geogset_in(String str) {
		var _result = MeosLibrary.meos.geogset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geomset_in(String str) {
		var _result = MeosLibrary.meos.geomset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String spatialset_as_text(Pointer set, int maxdd) {
		var _result = MeosLibrary.meos.spatialset_as_text(set, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String spatialset_as_ewkt(Pointer set, int maxdd) {
		var _result = MeosLibrary.meos.spatialset_as_ewkt(set, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_make(Pointer values, int count) {
		var _result = MeosLibrary.meos.geoset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_to_set(Pointer gs) {
		var _result = MeosLibrary.meos.geo_to_set(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_end_value(Pointer s) {
		var _result = MeosLibrary.meos.geoset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_start_value(Pointer s) {
		var _result = MeosLibrary.meos.geoset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.geoset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}


	@SuppressWarnings("unused")
	public static boolean contained_geo_set(Pointer gs, Pointer s) {
		var _result = MeosLibrary.meos.contained_geo_set(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_geo(Pointer s, Pointer gs) {
		var _result = MeosLibrary.meos.contains_set_geo(s, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_union_transfn(Pointer state, Pointer gs) {
		var _result = MeosLibrary.meos.geo_union_transfn(state, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_geo_set(Pointer gs, Pointer s) {
		var _result = MeosLibrary.meos.intersection_geo_set(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_geo(Pointer s, Pointer gs) {
		var _result = MeosLibrary.meos.intersection_set_geo(s, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_geo_set(Pointer gs, Pointer s) {
		var _result = MeosLibrary.meos.minus_geo_set(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_geo(Pointer s, Pointer gs) {
		var _result = MeosLibrary.meos.minus_set_geo(s, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_geo_set(Pointer gs, Pointer s) {
		var _result = MeosLibrary.meos.union_geo_set(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_geo(Pointer s, Pointer gs) {
		var _result = MeosLibrary.meos.union_set_geo(s, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spatialset_set_srid(Pointer s, int srid) {
		var _result = MeosLibrary.meos.spatialset_set_srid(s, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spatialset_srid(Pointer s) {
		var _result = MeosLibrary.meos.spatialset_srid(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spatialset_transform(Pointer s, int srid) {
		var _result = MeosLibrary.meos.spatialset_transform(s, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spatialset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward) {
		var _result = MeosLibrary.meos.spatialset_transform_pipeline(s, pipelinestr, srid, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String stbox_as_hexwkb(Pointer box, byte variant, Pointer size) {
		var _result = MeosLibrary.meos.stbox_as_hexwkb(box, variant, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_as_wkb(Pointer box, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = MeosLibrary.meos.stbox_as_wkb(box, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_from_hexwkb(String hexwkb) {
		var _result = MeosLibrary.meos.stbox_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_from_wkb(Pointer wkb, long size) {
		var _result = MeosLibrary.meos.stbox_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_in(String str) {
		var _result = MeosLibrary.meos.stbox_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String stbox_out(Pointer box, int maxdd) {
		var _result = MeosLibrary.meos.stbox_out(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_timestamptz_to_stbox(Pointer gs, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.geo_timestamptz_to_stbox(gs, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s) {
		var _result = MeosLibrary.meos.geo_tstzspan_to_stbox(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_copy(Pointer box) {
		var _result = MeosLibrary.meos.stbox_copy(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s) {
		var _result = MeosLibrary.meos.stbox_make(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_to_stbox(Pointer gs) {
		var _result = MeosLibrary.meos.geo_to_stbox(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spatialset_to_stbox(Pointer s) {
		var _result = MeosLibrary.meos.spatialset_to_stbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_to_box3d(Pointer box) {
		var _result = MeosLibrary.meos.stbox_to_box3d(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_to_gbox(Pointer box) {
		var _result = MeosLibrary.meos.stbox_to_gbox(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_to_geo(Pointer box) {
		var _result = MeosLibrary.meos.stbox_to_geo(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_to_tstzspan(Pointer box) {
		var _result = MeosLibrary.meos.stbox_to_tstzspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_stbox(OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.timestamptz_to_stbox(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_to_stbox(Pointer s) {
		var _result = MeosLibrary.meos.tstzset_to_stbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_to_stbox(Pointer s) {
		var _result = MeosLibrary.meos.tstzspan_to_stbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_to_stbox(Pointer ss) {
		var _result = MeosLibrary.meos.tstzspanset_to_stbox(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double stbox_area(Pointer box, boolean spheroid) {
		var _result = MeosLibrary.meos.stbox_area(box, spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int stbox_hash(Pointer box) {
		var _result = MeosLibrary.meos.stbox_hash(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long stbox_hash_extended(Pointer box, long seed) {
		var _result = MeosLibrary.meos.stbox_hash_extended(box, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_hast(Pointer box) {
		var _result = MeosLibrary.meos.stbox_hast(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_hasx(Pointer box) {
		var _result = MeosLibrary.meos.stbox_hasx(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_hasz(Pointer box) {
		var _result = MeosLibrary.meos.stbox_hasz(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_isgeodetic(Pointer box) {
		var _result = MeosLibrary.meos.stbox_isgeodetic(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double stbox_perimeter(Pointer box, boolean spheroid) {
		var _result = MeosLibrary.meos.stbox_perimeter(box, spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_tmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_tmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_tmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = MeosLibrary.meos.stbox_tmax_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_tmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_tmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_tmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = MeosLibrary.meos.stbox_tmin_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static double stbox_volume(Pointer box) {
		var _result = MeosLibrary.meos.stbox_volume(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.stbox_xmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.stbox_xmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_ymax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.stbox_ymax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_ymin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.stbox_ymin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_zmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.stbox_zmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_zmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.stbox_zmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_expand_space(Pointer box, double d) {
		var _result = MeosLibrary.meos.stbox_expand_space(box, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_expand_time(Pointer box, Pointer interv) {
		var _result = MeosLibrary.meos.stbox_expand_time(box, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_get_space(Pointer box) {
		var _result = MeosLibrary.meos.stbox_get_space(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_quad_split(Pointer box, Pointer count) {
		var _result = MeosLibrary.meos.stbox_quad_split(box, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_round(Pointer box, int maxdd) {
		var _result = MeosLibrary.meos.stbox_round(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) {
		var _result = MeosLibrary.meos.stbox_shift_scale_time(box, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stboxarr_round(Pointer boxarr, int count, int maxdd) {
		var _result = MeosLibrary.meos.stboxarr_round(boxarr, count, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_set_srid(Pointer box, int srid) {
		var _result = MeosLibrary.meos.stbox_set_srid(box, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int stbox_srid(Pointer box) {
		var _result = MeosLibrary.meos.stbox_srid(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_transform(Pointer box, int srid) {
		var _result = MeosLibrary.meos.stbox_transform(box, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward) {
		var _result = MeosLibrary.meos.stbox_transform_pipeline(box, pipelinestr, srid, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.adjacent_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.contained_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.contains_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overlaps_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.same_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean above_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.above_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.after_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean back_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.back_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.before_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean below_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.below_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean front_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.front_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.left_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overabove_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overabove_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overafter_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overback_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overback_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overbefore_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbelow_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overbelow_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overfront_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overfront_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overleft_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.overright_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.right_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict) {
		var _result = MeosLibrary.meos.union_stbox_stbox(box1, box2, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.intersection_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int stbox_cmp(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.stbox_cmp(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_eq(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.stbox_eq(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_ge(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.stbox_ge(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_gt(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.stbox_gt(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_le(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.stbox_le(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_lt(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.stbox_lt(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_ne(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.stbox_ne(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_from_mfjson(String str) {
		var _result = MeosLibrary.meos.tgeogpoint_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_in(String str) {
		var _result = MeosLibrary.meos.tgeogpoint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeography_from_mfjson(String mfjson) {
		var _result = MeosLibrary.meos.tgeography_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeography_in(String str) {
		var _result = MeosLibrary.meos.tgeography_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_from_mfjson(String str) {
		var _result = MeosLibrary.meos.tgeometry_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_in(String str) {
		var _result = MeosLibrary.meos.tgeometry_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompoint_from_mfjson(String str) {
		var _result = MeosLibrary.meos.tgeompoint_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompoint_in(String str) {
		var _result = MeosLibrary.meos.tgeompoint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tspatial_as_ewkt(Pointer temp, int maxdd) {
		var _result = MeosLibrary.meos.tspatial_as_ewkt(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tspatial_as_text(Pointer temp, int maxdd) {
		var _result = MeosLibrary.meos.tspatial_as_text(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tspatial_out(Pointer temp, int maxdd) {
		var _result = MeosLibrary.meos.tspatial_out(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_from_base_temp(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.tgeo_from_base_temp(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoinst_make(Pointer gs, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tgeoinst_make(gs, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseq_from_base_tstzset(Pointer gs, Pointer s) {
		var _result = MeosLibrary.meos.tgeoseq_from_base_tstzset(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseq_from_base_tstzspan(Pointer gs, Pointer s, int interp) {
		var _result = MeosLibrary.meos.tgeoseq_from_base_tstzspan(gs, s, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp) {
		var _result = MeosLibrary.meos.tgeoseqset_from_base_tstzspanset(gs, ss, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_from_base_temp(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_from_base_temp(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointinst_make(Pointer gs, OffsetDateTime t) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tpointinst_make(gs, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s) {
		var _result = MeosLibrary.meos.tpointseq_from_base_tstzset(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp) {
		var _result = MeosLibrary.meos.tpointseq_from_base_tstzspan(gs, s, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_make_coords(Pointer xcoords, Pointer ycoords, Pointer zcoords, Pointer times, int count, int srid, boolean geodetic, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		var _result = MeosLibrary.meos.tpointseq_make_coords(xcoords, ycoords, zcoords, times, count, srid, geodetic, lower_inc, upper_inc, interp, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp) {
		var _result = MeosLibrary.meos.tpointseqset_from_base_tstzspanset(gs, ss, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer box3d_to_stbox(Pointer box) {
		var _result = MeosLibrary.meos.box3d_to_stbox(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer gbox_to_stbox(Pointer box) {
		var _result = MeosLibrary.meos.gbox_to_stbox(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geomeas_to_tpoint(Pointer gs) {
		var _result = MeosLibrary.meos.geomeas_to_tpoint(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_to_tgeography(Pointer temp) {
		var _result = MeosLibrary.meos.tgeogpoint_to_tgeography(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeography_to_tgeogpoint(Pointer temp) {
		var _result = MeosLibrary.meos.tgeography_to_tgeogpoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeography_to_tgeometry(Pointer temp) {
		var _result = MeosLibrary.meos.tgeography_to_tgeometry(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_to_tgeography(Pointer temp) {
		var _result = MeosLibrary.meos.tgeometry_to_tgeography(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_to_tgeompoint(Pointer temp) {
		var _result = MeosLibrary.meos.tgeometry_to_tgeompoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompoint_to_tgeometry(Pointer temp) {
		var _result = MeosLibrary.meos.tgeompoint_to_tgeometry(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tpoint_as_mvtgeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count) {
		var _result = MeosLibrary.meos.tpoint_as_mvtgeom(temp, bounds, extent, buffer, clip_geom, gsarr, timesarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tpoint_tfloat_to_geomeas(tpoint, measure, segmentize, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_to_stbox(Pointer temp) {
		var _result = MeosLibrary.meos.tspatial_to_stbox(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bearing_point_point(Pointer gs1, Pointer gs2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.bearing_point_point(gs1, gs2, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert) {
		var _result = MeosLibrary.meos.bearing_tpoint_point(temp, gs, invert);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.bearing_tpoint_tpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_centroid(Pointer temp) {
		var _result = MeosLibrary.meos.tgeo_centroid(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_convex_hull(Pointer temp) {
		var _result = MeosLibrary.meos.tgeo_convex_hull(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_end_value(Pointer temp) {
		var _result = MeosLibrary.meos.tgeo_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_start_value(Pointer temp) {
		var _result = MeosLibrary.meos.tgeo_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_traversed_area(Pointer temp, boolean unary_union) {
		var _result = MeosLibrary.meos.tgeo_traversed_area(temp, unary_union);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tgeo_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var _result = MeosLibrary.meos.tgeo_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tgeo_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_values(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.tgeo_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_angular_difference(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_angular_difference(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_azimuth(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_azimuth(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_cumulative_length(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_cumulative_length(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_direction(Pointer temp) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = MeosLibrary.meos.tpoint_direction(temp, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_get_x(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_get_x(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_get_y(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_get_y(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_get_z(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_get_z(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tpoint_is_simple(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_is_simple(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tpoint_length(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_length(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_speed(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_speed(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_trajectory(Pointer temp, boolean unary_union) {
		var _result = MeosLibrary.meos.tpoint_trajectory(temp, unary_union);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_twcentroid(Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_twcentroid(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_affine(Pointer temp, Pointer a) {
		var _result = MeosLibrary.meos.tgeo_affine(temp, a);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_scale(Pointer temp, Pointer scale, Pointer sorigin) {
		var _result = MeosLibrary.meos.tgeo_scale(temp, scale, sorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_make_simple(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.tpoint_make_simple(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tspatial_srid(Pointer temp) {
		var _result = MeosLibrary.meos.tspatial_srid(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_set_srid(Pointer temp, int srid) {
		var _result = MeosLibrary.meos.tspatial_set_srid(temp, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_transform(Pointer temp, int srid) {
		var _result = MeosLibrary.meos.tspatial_transform(temp, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward) {
		var _result = MeosLibrary.meos.tspatial_transform_pipeline(temp, pipelinestr, srid, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_at_geom(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.tgeo_at_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_at_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = MeosLibrary.meos.tgeo_at_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_at_value(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.tgeo_at_value(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_minus_geom(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.tgeo_minus_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_minus_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = MeosLibrary.meos.tgeo_minus_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_minus_value(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.tgeo_minus_value(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}


	@SuppressWarnings("unused")
	public static Pointer tpoint_at_value(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.tpoint_at_value(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}


	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_value(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.tpoint_minus_value(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.always_eq_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.always_eq_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.always_eq_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.always_ne_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.always_ne_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.always_ne_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.ever_eq_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.ever_eq_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ever_eq_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.ever_ne_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.ever_ne_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ever_ne_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.teq_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.teq_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.tne_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.tne_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_stboxes(Pointer temp, Pointer count) {
		var _result = MeosLibrary.meos.tgeo_stboxes(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_space_boxes(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer count) {
		var _result = MeosLibrary.meos.tgeo_space_boxes(temp, xsize, ysize, zsize, sorigin, bitmatrix, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_space_time_boxes(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean bitmatrix, boolean border_inc, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tgeo_space_time_boxes(temp, xsize, ysize, zsize, duration, sorigin, torigin_new, bitmatrix, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_split_each_n_stboxes(Pointer temp, int elem_count, Pointer count) {
		var _result = MeosLibrary.meos.tgeo_split_each_n_stboxes(temp, elem_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_split_n_stboxes(Pointer temp, int box_count, Pointer count) {
		var _result = MeosLibrary.meos.tgeo_split_n_stboxes(temp, box_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.adjacent_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.adjacent_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.adjacent_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.contained_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.contained_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.contained_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.contains_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.contains_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.contains_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overlaps_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overlaps_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overlaps_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.same_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.same_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.same_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean above_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.above_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean above_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.above_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean above_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.above_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.after_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.after_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.after_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean back_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.back_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean back_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.back_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean back_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.back_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.before_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.before_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.before_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean below_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.below_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean below_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.below_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean below_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.below_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean front_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.front_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean front_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.front_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean front_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.front_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.left_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.left_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.left_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overabove_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overabove_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overabove_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overabove_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overabove_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overabove_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overafter_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overafter_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overafter_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overback_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overback_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overback_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overback_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overback_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overback_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overbefore_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overbefore_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overbefore_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbelow_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overbelow_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbelow_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overbelow_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbelow_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overbelow_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overfront_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overfront_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overfront_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overfront_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overfront_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overfront_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overleft_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overleft_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overleft_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.overright_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.overright_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.overright_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.right_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.right_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.right_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.acontains_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.acontains_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.acontains_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adisjoint_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.adisjoint_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.adisjoint_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adwithin_tgeo_geo(Pointer temp, Pointer gs, double dist) {
		var _result = MeosLibrary.meos.adwithin_tgeo_geo(temp, gs, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist) {
		var _result = MeosLibrary.meos.adwithin_tgeo_tgeo(temp1, temp2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int aintersects_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.aintersects_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int aintersects_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.aintersects_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.atouches_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.atouches_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tpoint_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.atouches_tpoint_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.econtains_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.econtains_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.econtains_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = MeosLibrary.meos.ecovers_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.ecovers_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.ecovers_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edisjoint_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.edisjoint_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.edisjoint_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edwithin_tgeo_geo(Pointer temp, Pointer gs, double dist) {
		var _result = MeosLibrary.meos.edwithin_tgeo_geo(temp, gs, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist) {
		var _result = MeosLibrary.meos.edwithin_tgeo_tgeo(temp1, temp2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int eintersects_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.eintersects_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int eintersects_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.eintersects_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.etouches_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.etouches_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tpoint_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.etouches_tpoint_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}



















	@SuppressWarnings("unused")
	public static Pointer tdistance_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.tdistance_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.tdistance_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_stbox_geo(Pointer box, Pointer gs) {
		var _result = MeosLibrary.meos.nad_stbox_geo(box, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = MeosLibrary.meos.nad_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.nad_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tgeo_stbox(Pointer temp, Pointer box) {
		var _result = MeosLibrary.meos.nad_tgeo_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.nad_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.nai_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.nai_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = MeosLibrary.meos.shortestline_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = MeosLibrary.meos.shortestline_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_tcentroid_finalfn(Pointer state) {
		var _result = MeosLibrary.meos.tpoint_tcentroid_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp) {
		var _result = MeosLibrary.meos.tpoint_tcentroid_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_extent_transfn(Pointer box, Pointer temp) {
		var _result = MeosLibrary.meos.tspatial_extent_transfn(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_get_space_tile(Pointer point, double xsize, double ysize, double zsize, Pointer sorigin) {
		var _result = MeosLibrary.meos.stbox_get_space_tile(point, xsize, ysize, zsize, sorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_get_space_time_tile(Pointer point, OffsetDateTime t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.stbox_get_space_time_tile(point, t_new, xsize, ysize, zsize, duration, sorigin, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_get_time_tile(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = utils.TimestampTzConverter.toTimestampTz(t);
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.stbox_get_time_tile(t_new, duration, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_space_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer sorigin, boolean border_inc, Pointer count) {
		var _result = MeosLibrary.meos.stbox_space_tiles(bounds, xsize, ysize, zsize, sorigin, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_space_time_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean border_inc, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.stbox_space_time_tiles(bounds, xsize, ysize, zsize, duration, sorigin, torigin_new, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_time_tiles(Pointer bounds, Pointer duration, OffsetDateTime torigin, boolean border_inc, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.stbox_time_tiles(bounds, duration, torigin_new, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_space_split(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer count) {
		var _result = MeosLibrary.meos.tgeo_space_split(temp, xsize, ysize, zsize, sorigin, bitmatrix, border_inc, space_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_space_time_split(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer time_bins, Pointer count) {
		var torigin_new = utils.TimestampTzConverter.toTimestampTz(torigin);
		var _result = MeosLibrary.meos.tgeo_space_time_split(temp, xsize, ysize, zsize, duration, sorigin, torigin_new, bitmatrix, border_inc, space_bins, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}


	@SuppressWarnings("unused")
	public static Pointer geo_cluster_dbscan(Pointer geoms, int ngeoms, double tolerance, int minpoints, Pointer count) {
		var _result = MeosLibrary.meos.geo_cluster_dbscan(geoms, ngeoms, tolerance, minpoints, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_cluster_intersecting(Pointer geoms, int ngeoms, Pointer count) {
		var _result = MeosLibrary.meos.geo_cluster_intersecting(geoms, ngeoms, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_cluster_within(Pointer geoms, int ngeoms, double tolerance, Pointer count) {
		var _result = MeosLibrary.meos.geo_cluster_within(geoms, ngeoms, tolerance, count);
		MeosErrorHandler.checkError();
		return _result;
	}

}
