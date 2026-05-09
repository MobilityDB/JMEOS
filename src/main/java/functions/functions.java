package functions;

import jnr.ffi.*;
import jnr.ffi.Runtime;
import org.w3c.dom.ls.LSOutput;
import utils.JarLibraryLoader;
import jnr.ffi.LibraryLoader;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

public class functions {
	// ── MeosLibrary interface split ─────────────────────────────────────────────────
	// The original single MeosLibrary interface (1486 methods) caused
	// MethodTooLargeException: JNR-FFI generates a proxy whose <clinit>()V initialises
	// native dispatch for every method, and 1486 methods exceeds the JVM 64 KB
	// bytecode limit. Fix: 4 private sub-interfaces of ≤400 methods each.
	// All public static wrappers keep their signatures — no API break.
	// ─────────────────────────────────────────────────────────────────────────────────

	/**
	 * Internal sub-interface MeosLibraryPartA — loaded by JNR-FFI as a separate proxy.
	 * Kept private; callers use the public static wrappers in this class.
	 */
	private interface MeosLibraryPartA {
		int geo_get_srid(Pointer g);
		void meos_error(int errlevel, int errcode, String format, Pointer args);
		int meos_errno();
		int meos_errno_set(int err);
		int meos_errno_restore(int err);
		int meos_errno_reset();
		void meos_initialize_timezone(String name);
		void meos_initialize_error_handler(error_handler_fn err_handler);
		void meos_finalize_timezone();
		boolean meos_set_datestyle(String newval, Pointer extra);
		boolean meos_set_intervalstyle(String newval, int extra);
		String meos_get_datestyle();
		String meos_get_intervalstyle();
		void meos_initialize(String tz_str, error_handler_fn err_handler);
		void meos_finalize();
		int add_date_int(int d, int days);
		Pointer add_interval_interval(Pointer interv1, Pointer interv2);
		long add_timestamptz_interval(long t, Pointer interv);
		boolean bool_in(String str);
		String bool_out(boolean b);
		Pointer cstring2text(String str);
		long date_to_timestamptz(int d);
		Pointer minus_date_date(int d1, int d2);
		int minus_date_int(int d, int days);
		long minus_timestamptz_interval(long t, Pointer interv);
		Pointer minus_timestamptz_timestamptz(long t1, long t2);
		Pointer mult_interval_double(Pointer interv, double factor);
		int pg_date_in(String str);
		String pg_date_out(int d);
		int pg_interval_cmp(Pointer interv1, Pointer interv2);
		Pointer pg_interval_in(String str, int typmod);
		Pointer pg_interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs);
		String pg_interval_out(Pointer interv);
		long pg_time_in(String str, int typmod);
		String pg_time_out(long t);
		long pg_timestamp_in(String str, int typmod);
		String pg_timestamp_out(long t);
		long pg_timestamptz_in(String str, int typmod);
		String pg_timestamptz_out(long t);
		String text2cstring(Pointer txt);
		int text_cmp(Pointer txt1, Pointer txt2);
		Pointer text_copy(Pointer txt);
		Pointer text_initcap(Pointer txt);
		Pointer text_lower(Pointer txt);
		String text_out(Pointer txt);
		Pointer text_upper(Pointer txt);
		Pointer textcat_text_text(Pointer txt1, Pointer txt2);
		int timestamptz_to_date(long t);
		Pointer geo_as_ewkb(Pointer gs, String endian);
		String geo_as_ewkt(Pointer gs, int precision);
		String geo_as_geojson(Pointer gs, int option, int precision, String srs);
		String geo_as_hexewkb(Pointer gs, String endian);
		String geo_as_text(Pointer gs, int precision);
		Pointer geo_from_ewkb(Pointer bytea_wkb, int srid);
		Pointer geo_from_geojson(String geojson);
		String geo_out(Pointer gs);
		boolean geo_same(Pointer gs1, Pointer gs2);
		Pointer geography_from_hexewkb(String wkt);
		Pointer geography_from_text(String wkt, int srid);
		Pointer geometry_from_hexewkb(String wkt);
		Pointer geometry_from_text(String wkt, int srid);
		Pointer pgis_geography_in(String str, int typmod);
		Pointer pgis_geometry_in(String str, int typmod);
		Pointer bigintset_in(String str);
		String bigintset_out(Pointer set);
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
		Pointer floatspan_in(String str);
		String floatspan_out(Pointer s, int maxdd);
		Pointer floatspanset_in(String str);
		String floatspanset_out(Pointer ss, int maxdd);
		Pointer geogset_in(String str);
		Pointer geomset_in(String str);
		String geoset_as_ewkt(Pointer set, int maxdd);
		String geoset_as_text(Pointer set, int maxdd);
		String geoset_out(Pointer set, int maxdd);
		Pointer intset_in(String str);
		String intset_out(Pointer set);
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
		Pointer geoset_make(Pointer values, int count);
		Pointer intset_make(Pointer values, int count);
		Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc);
		Pointer set_copy(Pointer s);
		Pointer span_copy(Pointer s);
		Pointer spanset_copy(Pointer ss);
		Pointer spanset_make(Pointer spans, int count, boolean normalize, boolean order);
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
		Pointer geo_to_set(Pointer gs);
		Pointer int_to_set(int i);
		Pointer int_to_span(int i);
		Pointer int_to_spanset(int i);
		Pointer intset_to_floatset(Pointer s);
		Pointer intspan_to_floatspan(Pointer s);
		Pointer intspanset_to_floatspanset(Pointer ss);
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
		Pointer bigintset_values(Pointer s);
		long bigintspan_lower(Pointer s);
		long bigintspan_upper(Pointer s);
		long bigintspan_width(Pointer s);
		long bigintspanset_lower(Pointer ss);
		long bigintspanset_upper(Pointer ss);
		long bigintspanset_width(Pointer ss, boolean boundspan);
		int dateset_end_value(Pointer s);
		int dateset_start_value(Pointer s);
		boolean dateset_value_n(Pointer s, int n, Pointer result);
		Pointer dateset_values(Pointer s);
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
		Pointer floatset_values(Pointer s);
		double floatspan_lower(Pointer s);
		double floatspan_upper(Pointer s);
		double floatspan_width(Pointer s);
		double floatspanset_lower(Pointer ss);
		double floatspanset_upper(Pointer ss);
		double floatspanset_width(Pointer ss, boolean boundspan);
		Pointer geoset_end_value(Pointer s);
		int geoset_srid(Pointer s);
		Pointer geoset_start_value(Pointer s);
		boolean geoset_value_n(Pointer s, int n, Pointer result);
		Pointer geoset_values(Pointer s);
		int intset_end_value(Pointer s);
		int intset_start_value(Pointer s);
		boolean intset_value_n(Pointer s, int n, Pointer result);
		Pointer intset_values(Pointer s);
		int intspan_lower(Pointer s);
		int intspan_upper(Pointer s);
		int intspan_width(Pointer s);
		int intspanset_lower(Pointer ss);
		int intspanset_upper(Pointer ss);
		int intspanset_width(Pointer ss, boolean boundspan);
		int set_hash(Pointer s);
		long set_hash_extended(Pointer s, long seed);
		int set_num_values(Pointer s);
		Pointer set_to_span(Pointer s);
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
		Pointer spanset_spans(Pointer ss);
		Pointer spanset_start_span(Pointer ss);
		boolean spanset_upper_inc(Pointer ss);
		Pointer textset_end_value(Pointer s);
		Pointer textset_start_value(Pointer s);
		boolean textset_value_n(Pointer s, int n, Pointer result);
		Pointer textset_values(Pointer s);
		long tstzset_end_value(Pointer s);
		long tstzset_start_value(Pointer s);
		boolean tstzset_value_n(Pointer s, int n, Pointer result);
		Pointer tstzset_values(Pointer s);
		Pointer tstzspan_duration(Pointer s);
		long tstzspan_lower(Pointer s);
		long tstzspan_upper(Pointer s);
		Pointer tstzspanset_duration(Pointer ss, boolean boundspan);
		long tstzspanset_end_timestamptz(Pointer ss);
		long tstzspanset_lower(Pointer ss);
		int tstzspanset_num_timestamps(Pointer ss);
		long tstzspanset_start_timestamptz(Pointer ss);
		boolean tstzspanset_timestamptz_n(Pointer ss, int n, Pointer result);
		Pointer tstzspanset_timestamps(Pointer ss);
		long tstzspanset_upper(Pointer ss);
		Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);
		Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);
		Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth);
		Pointer dateset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer datespan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer datespanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer floatset_ceil(Pointer s);
		Pointer floatset_floor(Pointer s);
		Pointer floatset_degrees(Pointer s, boolean normalize);
		Pointer floatset_radians(Pointer s);
		Pointer floatset_round(Pointer s, int maxdd);
		Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);
		Pointer floatspan_ceil(Pointer s);
		Pointer floatspan_floor(Pointer s);
		Pointer floatspan_round(Pointer s, int maxdd);
		Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);
		Pointer floatspanset_ceil(Pointer ss);
		Pointer floatspanset_floor(Pointer ss);
		Pointer floatspanset_round(Pointer ss, int maxdd);
		Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth);
		Pointer geoset_round(Pointer s, int maxdd);
		Pointer geoset_set_srid(Pointer s, int srid);
		Pointer geoset_transform(Pointer s, int srid);
		Pointer geoset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward);
		Pointer point_transform(Pointer gs, int srid);
		Pointer point_transform_pipeline(Pointer gs, String pipelinestr, int srid, boolean is_forward);
		Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer textset_initcap(Pointer s);
		Pointer textset_lower(Pointer s);
		Pointer textset_upper(Pointer s);
		Pointer textcat_textset_text(Pointer s, Pointer txt);
		Pointer textcat_text_textset(Pointer txt, Pointer s);
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
		boolean contained_geo_set(Pointer gs, Pointer s);
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
		boolean contains_set_geo(Pointer s, Pointer gs);
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
	}

	/**
	 * Internal sub-interface MeosLibraryPartB — loaded by JNR-FFI as a separate proxy.
	 * Kept private; callers use the public static wrappers in this class.
	 */
	private interface MeosLibraryPartB {
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
		Pointer intersection_geo_set(Pointer gs, Pointer s);
		Pointer intersection_int_set(int i, Pointer s);
		Pointer intersection_set_bigint(Pointer s, long i);
		Pointer intersection_set_date(Pointer s, int d);
		Pointer intersection_set_float(Pointer s, double d);
		Pointer intersection_set_geo(Pointer s, Pointer gs);
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
		Pointer minus_geo_set(Pointer gs, Pointer s);
		Pointer minus_int_set(int i, Pointer s);
		Pointer minus_int_span(int i, Pointer s);
		Pointer minus_int_spanset(int i, Pointer ss);
		Pointer minus_set_bigint(Pointer s, long i);
		Pointer minus_set_date(Pointer s, int d);
		Pointer minus_set_float(Pointer s, double d);
		Pointer minus_set_geo(Pointer s, Pointer gs);
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
		Pointer union_geo_set(Pointer gs, Pointer s);
		Pointer union_int_set(int i, Pointer s);
		Pointer union_int_span(int i, Pointer s);
		Pointer union_int_spanset(int i, Pointer ss);
		Pointer union_set_bigint(Pointer s, long i);
		Pointer union_set_date(Pointer s, int d);
		Pointer union_set_float(Pointer s, double d);
		Pointer union_set_geo(Pointer s, Pointer gs);
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
		Pointer tbox_in(String str);
		String tbox_out(Pointer box, int maxdd);
		Pointer tbox_from_wkb(Pointer wkb, long size);
		Pointer tbox_from_hexwkb(String hexwkb);
		Pointer stbox_from_wkb(Pointer wkb, long size);
		Pointer stbox_from_hexwkb(String hexwkb);
		Pointer tbox_as_wkb(Pointer box, byte variant, Pointer size_out);
		String tbox_as_hexwkb(Pointer box, byte variant, Pointer size);
		Pointer stbox_as_wkb(Pointer box, byte variant, Pointer size_out);
		String stbox_as_hexwkb(Pointer box, byte variant, Pointer size);
		Pointer stbox_in(String str);
		String stbox_out(Pointer box, int maxdd);
		Pointer float_tstzspan_to_tbox(double d, Pointer s);
		Pointer float_timestamptz_to_tbox(double d, long t);
		Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s);
		Pointer geo_timestamptz_to_stbox(Pointer gs, long t);
		Pointer int_tstzspan_to_tbox(int i, Pointer s);
		Pointer int_timestamptz_to_tbox(int i, long t);
		Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s);
		Pointer numspan_timestamptz_to_tbox(Pointer span, long t);
		Pointer stbox_copy(Pointer box);
		Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s);
		Pointer tbox_copy(Pointer box);
		Pointer tbox_make(Pointer s, Pointer p);
		Pointer float_to_tbox(double d);
		Pointer geo_to_stbox(Pointer gs);
		Pointer int_to_tbox(int i);
		Pointer set_to_tbox(Pointer s);
		Pointer span_to_tbox(Pointer s);
		Pointer spanset_to_tbox(Pointer ss);
		Pointer spatialset_to_stbox(Pointer s);
		Pointer stbox_to_gbox(Pointer box);
		Pointer stbox_to_box3d(Pointer box);
		Pointer stbox_to_geo(Pointer box);
		Pointer stbox_to_tstzspan(Pointer box);
		Pointer tbox_to_intspan(Pointer box);
		Pointer tbox_to_floatspan(Pointer box);
		Pointer tbox_to_tstzspan(Pointer box);
		Pointer timestamptz_to_stbox(long t);
		Pointer timestamptz_to_tbox(long t);
		Pointer tstzset_to_stbox(Pointer s);
		Pointer tstzspan_to_stbox(Pointer s);
		Pointer tstzspanset_to_stbox(Pointer ss);
		Pointer tnumber_to_tbox(Pointer temp);
		Pointer tpoint_to_stbox(Pointer temp);
		boolean stbox_hast(Pointer box);
		boolean stbox_hasx(Pointer box);
		boolean stbox_hasz(Pointer box);
		boolean stbox_isgeodetic(Pointer box);
		int stbox_srid(Pointer box);
		boolean stbox_tmax(Pointer box, Pointer result);
		boolean stbox_tmax_inc(Pointer box, Pointer result);
		boolean stbox_tmin(Pointer box, Pointer result);
		boolean stbox_tmin_inc(Pointer box, Pointer result);
		boolean stbox_xmax(Pointer box, Pointer result);
		boolean stbox_xmin(Pointer box, Pointer result);
		boolean stbox_ymax(Pointer box, Pointer result);
		boolean stbox_ymin(Pointer box, Pointer result);
		boolean stbox_zmax(Pointer box, Pointer result);
		boolean stbox_zmin(Pointer box, Pointer result);
		boolean tbox_hast(Pointer box);
		boolean tbox_hasx(Pointer box);
		boolean tbox_tmax(Pointer box, Pointer result);
		boolean tbox_tmax_inc(Pointer box, Pointer result);
		boolean tbox_tmin(Pointer box, Pointer result);
		boolean tbox_tmin_inc(Pointer box, Pointer result);
	}

	/**
	 * Internal sub-interface MeosLibraryPartC — loaded by JNR-FFI as a separate proxy.
	 * Kept private; callers use the public static wrappers in this class.
	 */
	private interface MeosLibraryPartC {
		boolean tbox_xmax(Pointer box, Pointer result);
		boolean tbox_xmax_inc(Pointer box, Pointer result);
		boolean tbox_xmin(Pointer box, Pointer result);
		boolean tbox_xmin_inc(Pointer box, Pointer result);
		boolean tboxfloat_xmax(Pointer box, Pointer result);
		boolean tboxfloat_xmin(Pointer box, Pointer result);
		boolean tboxint_xmax(Pointer box, Pointer result);
		boolean tboxint_xmin(Pointer box, Pointer result);
		Pointer stbox_expand_space(Pointer box, double d);
		Pointer stbox_expand_time(Pointer box, Pointer interv);
		Pointer stbox_get_space(Pointer box);
		Pointer stbox_quad_split(Pointer box, Pointer count);
		Pointer stbox_round(Pointer box, int maxdd);
		Pointer stbox_set_srid(Pointer box, int srid);
		Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);
		Pointer stbox_transform(Pointer box, int srid);
		Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward);
		Pointer tbox_expand_time(Pointer box, Pointer interv);
		Pointer tbox_expand_float(Pointer box, double d);
		Pointer tbox_expand_int(Pointer box, int i);
		Pointer tbox_round(Pointer box, int maxdd);
		Pointer tbox_shift_scale_float(Pointer box, double shift, double width, boolean hasshift, boolean haswidth);
		Pointer tbox_shift_scale_int(Pointer box, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);
		Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict);
		Pointer intersection_tbox_tbox(Pointer box1, Pointer box2);
		Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict);
		Pointer intersection_stbox_stbox(Pointer box1, Pointer box2);
		boolean adjacent_stbox_stbox(Pointer box1, Pointer box2);
		boolean adjacent_tbox_tbox(Pointer box1, Pointer box2);
		boolean contained_tbox_tbox(Pointer box1, Pointer box2);
		boolean contained_stbox_stbox(Pointer box1, Pointer box2);
		boolean contains_stbox_stbox(Pointer box1, Pointer box2);
		boolean contains_tbox_tbox(Pointer box1, Pointer box2);
		boolean overlaps_tbox_tbox(Pointer box1, Pointer box2);
		boolean overlaps_stbox_stbox(Pointer box1, Pointer box2);
		boolean same_tbox_tbox(Pointer box1, Pointer box2);
		boolean same_stbox_stbox(Pointer box1, Pointer box2);
		boolean left_tbox_tbox(Pointer box1, Pointer box2);
		boolean overleft_tbox_tbox(Pointer box1, Pointer box2);
		boolean right_tbox_tbox(Pointer box1, Pointer box2);
		boolean overright_tbox_tbox(Pointer box1, Pointer box2);
		boolean before_tbox_tbox(Pointer box1, Pointer box2);
		boolean overbefore_tbox_tbox(Pointer box1, Pointer box2);
		boolean after_tbox_tbox(Pointer box1, Pointer box2);
		boolean overafter_tbox_tbox(Pointer box1, Pointer box2);
		boolean left_stbox_stbox(Pointer box1, Pointer box2);
		boolean overleft_stbox_stbox(Pointer box1, Pointer box2);
		boolean right_stbox_stbox(Pointer box1, Pointer box2);
		boolean overright_stbox_stbox(Pointer box1, Pointer box2);
		boolean below_stbox_stbox(Pointer box1, Pointer box2);
		boolean overbelow_stbox_stbox(Pointer box1, Pointer box2);
		boolean above_stbox_stbox(Pointer box1, Pointer box2);
		boolean overabove_stbox_stbox(Pointer box1, Pointer box2);
		boolean front_stbox_stbox(Pointer box1, Pointer box2);
		boolean overfront_stbox_stbox(Pointer box1, Pointer box2);
		boolean back_stbox_stbox(Pointer box1, Pointer box2);
		boolean overback_stbox_stbox(Pointer box1, Pointer box2);
		boolean before_stbox_stbox(Pointer box1, Pointer box2);
		boolean overbefore_stbox_stbox(Pointer box1, Pointer box2);
		boolean after_stbox_stbox(Pointer box1, Pointer box2);
		boolean overafter_stbox_stbox(Pointer box1, Pointer box2);
		boolean tbox_eq(Pointer box1, Pointer box2);
		boolean tbox_ne(Pointer box1, Pointer box2);
		int tbox_cmp(Pointer box1, Pointer box2);
		boolean tbox_lt(Pointer box1, Pointer box2);
		boolean tbox_le(Pointer box1, Pointer box2);
		boolean tbox_ge(Pointer box1, Pointer box2);
		boolean tbox_gt(Pointer box1, Pointer box2);
		boolean stbox_eq(Pointer box1, Pointer box2);
		boolean stbox_ne(Pointer box1, Pointer box2);
		int stbox_cmp(Pointer box1, Pointer box2);
		boolean stbox_lt(Pointer box1, Pointer box2);
		boolean stbox_le(Pointer box1, Pointer box2);
		boolean stbox_ge(Pointer box1, Pointer box2);
		boolean stbox_gt(Pointer box1, Pointer box2);
		Pointer tbool_in(String str);
		Pointer tint_in(String str);
		Pointer tfloat_in(String str);
		Pointer ttext_in(String str);
		Pointer tgeompoint_in(String str);
		Pointer tgeogpoint_in(String str);
		Pointer tbool_from_mfjson(String str);
		Pointer tint_from_mfjson(String str);
		Pointer tfloat_from_mfjson(String str);
		Pointer ttext_from_mfjson(String str);
		Pointer tgeompoint_from_mfjson(String str);
		Pointer tgeogpoint_from_mfjson(String str);
		Pointer temporal_from_wkb(Pointer wkb, long size);
		Pointer temporal_from_hexwkb(String hexwkb);
		String tbool_out(Pointer temp);
		String tint_out(Pointer temp);
		String tfloat_out(Pointer temp, int maxdd);
		String ttext_out(Pointer temp);
		String tpoint_out(Pointer temp, int maxdd);
		String tpoint_as_text(Pointer temp, int maxdd);
		String tpoint_as_ewkt(Pointer temp, int maxdd);
		String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs);
		Pointer temporal_as_wkb(Pointer temp, byte variant, Pointer size_out);
		String temporal_as_hexwkb(Pointer temp, byte variant, Pointer size_out);
		Pointer tbool_from_base_temp(boolean b, Pointer temp);
		Pointer tboolinst_make(boolean b, long t);
		Pointer tboolseq_from_base_tstzset(boolean b, Pointer s);
		Pointer tboolseq_from_base_tstzspan(boolean b, Pointer s);
		Pointer tboolseqset_from_base_tstzspanset(boolean b, Pointer ss);
		Pointer temporal_copy(Pointer temp);
		Pointer tfloat_from_base_temp(double d, Pointer temp);
		Pointer tfloatinst_make(double d, long t);
		Pointer tfloatseq_from_base_tstzspan(double d, Pointer s, int interp);
		Pointer tfloatseq_from_base_tstzset(double d, Pointer s);
		Pointer tfloatseqset_from_base_tstzspanset(double d, Pointer ss, int interp);
		Pointer tint_from_base_temp(int i, Pointer temp);
		Pointer tintinst_make(int i, long t);
		Pointer tintseq_from_base_tstzspan(int i, Pointer s);
		Pointer tintseq_from_base_tstzset(int i, Pointer s);
		Pointer tintseqset_from_base_tstzspanset(int i, Pointer ss);
		Pointer tpoint_from_base_temp(Pointer gs, Pointer temp);
		Pointer tpointinst_make(Pointer gs, long t);
		Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp);
		Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s);
		Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp);
		Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);
		Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize);
		Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist);
		Pointer ttext_from_base_temp(Pointer txt, Pointer temp);
		Pointer ttextinst_make(Pointer txt, long t);
		Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s);
		Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s);
		Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss);
		Pointer temporal_to_tstzspan(Pointer temp);
		Pointer tfloat_to_tint(Pointer temp);
		Pointer tint_to_tfloat(Pointer temp);
		Pointer tnumber_to_span(Pointer temp);
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
		Pointer temporal_max_instant(Pointer temp);
		Pointer temporal_min_instant(Pointer temp);
		int temporal_num_instants(Pointer temp);
		int temporal_num_sequences(Pointer temp);
		int temporal_num_timestamps(Pointer temp);
		Pointer temporal_segments(Pointer temp, Pointer count);
		Pointer temporal_sequence_n(Pointer temp, int i);
		Pointer temporal_sequences(Pointer temp, Pointer count);
		int temporal_lower_inc(Pointer temp);
		int temporal_upper_inc(Pointer temp);
		Pointer temporal_start_instant(Pointer temp);
		Pointer temporal_start_sequence(Pointer temp);
		long temporal_start_timestamptz(Pointer temp);
		Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration);
		String temporal_subtype(Pointer temp);
		Pointer temporal_time(Pointer temp);
		boolean temporal_timestamptz_n(Pointer temp, int n, Pointer result);
		Pointer temporal_timestamps(Pointer temp, Pointer count);
		double tfloat_end_value(Pointer temp);
		double tfloat_max_value(Pointer temp);
		double tfloat_min_value(Pointer temp);
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
		double tnumber_integral(Pointer temp);
		double tnumber_twavg(Pointer temp);
		Pointer tnumber_valuespans(Pointer temp);
		Pointer tpoint_end_value(Pointer temp);
		Pointer tpoint_start_value(Pointer temp);
		boolean tpoint_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);
		boolean tpoint_value_n(Pointer temp, int n, Pointer result);
		Pointer tpoint_values(Pointer temp, Pointer count);
		Pointer ttext_end_value(Pointer temp);
		Pointer ttext_max_value(Pointer temp);
		Pointer ttext_min_value(Pointer temp);
		Pointer ttext_start_value(Pointer temp);
		boolean ttext_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);
		boolean ttext_value_n(Pointer temp, int n, Pointer result);
		Pointer ttext_values(Pointer temp, Pointer count);
		double float_degrees(double value, boolean normalize);
		Pointer temporal_scale_time(Pointer temp, Pointer duration);
		Pointer temporal_set_interp(Pointer temp, int interp);
		Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration);
		Pointer temporal_shift_time(Pointer temp, Pointer shift);
		Pointer temporal_to_tinstant(Pointer temp);
		Pointer temporal_to_tsequence(Pointer temp, String interp_str);
		Pointer temporal_to_tsequenceset(Pointer temp, String interp_str);
		Pointer tfloat_floor(Pointer temp);
		Pointer tfloat_ceil(Pointer temp);
		Pointer tfloat_degrees(Pointer temp, boolean normalize);
		Pointer tfloat_radians(Pointer temp);
		Pointer tfloat_round(Pointer temp, int maxdd);
		Pointer tfloat_scale_value(Pointer temp, double width);
		Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width);
		Pointer tfloat_shift_value(Pointer temp, double shift);
		Pointer tfloatarr_round(Pointer temp, int count, int maxdd);
		Pointer tint_scale_value(Pointer temp, int width);
		Pointer tint_shift_scale_value(Pointer temp, int shift, int width);
		Pointer tint_shift_value(Pointer temp, int shift);
		Pointer tpoint_round(Pointer temp, int maxdd);
		Pointer tpoint_transform(Pointer temp, int srid);
		Pointer tpoint_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward);
		Pointer tpoint_transform_pj(Pointer temp, int srid, Pointer pj);
		Pointer lwproj_transform(int srid_from, int srid_to);
		Pointer tpointarr_round(Pointer temp, int count, int maxdd);
		Pointer temporal_append_tinstant(Pointer temp, Pointer inst, double maxdist, Pointer maxt, boolean expand);
		Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand);
		Pointer temporal_delete_tstzspan(Pointer temp, Pointer s, boolean connect);
		Pointer temporal_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect);
		Pointer temporal_delete_timestamptz(Pointer temp, long t, boolean connect);
		Pointer temporal_delete_tstzset(Pointer temp, Pointer s, boolean connect);
		Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect);
		Pointer temporal_merge(Pointer temp1, Pointer temp2);
		Pointer temporal_merge_array(Pointer temparr, int count);
		Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect);
		Pointer tbool_at_value(Pointer temp, boolean b);
		Pointer tbool_minus_value(Pointer temp, boolean b);
		Pointer temporal_at_max(Pointer temp);
		Pointer temporal_at_min(Pointer temp);
		Pointer temporal_at_tstzspan(Pointer temp, Pointer s);
		Pointer temporal_at_tstzspanset(Pointer temp, Pointer ss);
		Pointer temporal_at_timestamptz(Pointer temp, long t);
		Pointer temporal_at_tstzset(Pointer temp, Pointer s);
		Pointer temporal_at_values(Pointer temp, Pointer set);
		Pointer temporal_minus_max(Pointer temp);
		Pointer temporal_minus_min(Pointer temp);
		Pointer temporal_minus_tstzspan(Pointer temp, Pointer s);
		Pointer temporal_minus_tstzspanset(Pointer temp, Pointer ss);
		Pointer temporal_minus_timestamptz(Pointer temp, long t);
		Pointer temporal_minus_tstzset(Pointer temp, Pointer s);
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
		Pointer tpoint_at_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period);
		Pointer tpoint_at_stbox(Pointer temp, Pointer box, boolean border_inc);
		Pointer tpoint_at_value(Pointer temp, Pointer gs);
		Pointer tpoint_minus_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period);
		Pointer tpoint_minus_stbox(Pointer temp, Pointer box, boolean border_inc);
		Pointer tpoint_minus_value(Pointer temp, Pointer gs);
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
		int always_eq_point_tpoint(Pointer gs, Pointer temp);
		int always_eq_tbool_bool(Pointer temp, boolean b);
		int always_eq_temporal_temporal(Pointer temp1, Pointer temp2);
		int always_eq_text_ttext(Pointer txt, Pointer temp);
		int always_eq_tfloat_float(Pointer temp, double d);
		int always_eq_tint_int(Pointer temp, int i);
		int always_eq_tpoint_point(Pointer temp, Pointer gs);
		int always_eq_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int always_eq_ttext_text(Pointer temp, Pointer txt);
		int always_ne_bool_tbool(boolean b, Pointer temp);
		int always_ne_float_tfloat(double d, Pointer temp);
		int always_ne_int_tint(int i, Pointer temp);
		int always_ne_point_tpoint(Pointer gs, Pointer temp);
		int always_ne_tbool_bool(Pointer temp, boolean b);
		int always_ne_temporal_temporal(Pointer temp1, Pointer temp2);
		int always_ne_text_ttext(Pointer txt, Pointer temp);
		int always_ne_tfloat_float(Pointer temp, double d);
		int always_ne_tint_int(Pointer temp, int i);
		int always_ne_tpoint_point(Pointer temp, Pointer gs);
		int always_ne_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int always_ne_ttext_text(Pointer temp, Pointer txt);
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
		int ever_eq_bool_tbool(boolean b, Pointer temp);
		int ever_eq_float_tfloat(double d, Pointer temp);
		int ever_eq_int_tint(int i, Pointer temp);
		int ever_eq_point_tpoint(Pointer gs, Pointer temp);
		int ever_eq_tbool_bool(Pointer temp, boolean b);
		int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2);
		int ever_eq_text_ttext(Pointer txt, Pointer temp);
		int ever_eq_tfloat_float(Pointer temp, double d);
		int ever_eq_tint_int(Pointer temp, int i);
		int ever_eq_tpoint_point(Pointer temp, Pointer gs);
		int ever_eq_tpoint_tpoint(Pointer temp1, Pointer temp2);
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
		int ever_ne_point_tpoint(Pointer gs, Pointer temp);
		int ever_ne_tbool_bool(Pointer temp, boolean b);
		int ever_ne_temporal_temporal(Pointer temp1, Pointer temp2);
		int ever_ne_text_ttext(Pointer txt, Pointer temp);
		int ever_ne_tfloat_float(Pointer temp, double d);
		int ever_ne_tint_int(Pointer temp, int i);
		int ever_ne_tpoint_point(Pointer temp, Pointer gs);
	}

	/**
	 * Internal sub-interface MeosLibraryPartD — loaded by JNR-FFI as a separate proxy.
	 * Kept private; callers use the public static wrappers in this class.
	 */
	private interface MeosLibraryPartD {
		int ever_ne_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int ever_ne_ttext_text(Pointer temp, Pointer txt);
		Pointer teq_bool_tbool(boolean b, Pointer temp);
		Pointer teq_float_tfloat(double d, Pointer temp);
		Pointer teq_int_tint(int i, Pointer temp);
		Pointer teq_point_tpoint(Pointer gs, Pointer temp);
		Pointer teq_tbool_bool(Pointer temp, boolean b);
		Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2);
		Pointer teq_text_ttext(Pointer txt, Pointer temp);
		Pointer teq_tfloat_float(Pointer temp, double d);
		Pointer teq_tpoint_point(Pointer temp, Pointer gs);
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
		Pointer tne_point_tpoint(Pointer gs, Pointer temp);
		Pointer tne_tbool_bool(Pointer temp, boolean b);
		Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2);
		Pointer tne_text_ttext(Pointer txt, Pointer temp);
		Pointer tne_tfloat_float(Pointer temp, double d);
		Pointer tne_tpoint_point(Pointer temp, Pointer gs);
		Pointer tne_tint_int(Pointer temp, int i);
		Pointer tne_ttext_text(Pointer temp, Pointer txt);
		boolean adjacent_numspan_tnumber(Pointer s, Pointer temp);
		boolean adjacent_stbox_tpoint(Pointer box, Pointer temp);
		boolean adjacent_tbox_tnumber(Pointer box, Pointer temp);
		boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean adjacent_temporal_tstzspan(Pointer temp, Pointer s);
		boolean adjacent_tnumber_numspan(Pointer temp, Pointer s);
		boolean adjacent_tnumber_tbox(Pointer temp, Pointer box);
		boolean adjacent_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean adjacent_tpoint_stbox(Pointer temp, Pointer box);
		boolean adjacent_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean adjacent_tstzspan_temporal(Pointer s, Pointer temp);
		boolean contained_numspan_tnumber(Pointer s, Pointer temp);
		boolean contained_stbox_tpoint(Pointer box, Pointer temp);
		boolean contained_tbox_tnumber(Pointer box, Pointer temp);
		boolean contained_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean contained_temporal_tstzspan(Pointer temp, Pointer s);
		boolean contained_tnumber_numspan(Pointer temp, Pointer s);
		boolean contained_tnumber_tbox(Pointer temp, Pointer box);
		boolean contained_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean contained_tpoint_stbox(Pointer temp, Pointer box);
		boolean contained_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean contained_tstzspan_temporal(Pointer s, Pointer temp);
		boolean contains_numspan_tnumber(Pointer s, Pointer temp);
		boolean contains_stbox_tpoint(Pointer box, Pointer temp);
		boolean contains_tbox_tnumber(Pointer box, Pointer temp);
		boolean contains_temporal_tstzspan(Pointer temp, Pointer s);
		boolean contains_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean contains_tnumber_numspan(Pointer temp, Pointer s);
		boolean contains_tnumber_tbox(Pointer temp, Pointer box);
		boolean contains_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean contains_tpoint_stbox(Pointer temp, Pointer box);
		boolean contains_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean contains_tstzspan_temporal(Pointer s, Pointer temp);
		boolean overlaps_numspan_tnumber(Pointer s, Pointer temp);
		boolean overlaps_stbox_tpoint(Pointer box, Pointer temp);
		boolean overlaps_tbox_tnumber(Pointer box, Pointer temp);
		boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean overlaps_temporal_tstzspan(Pointer temp, Pointer s);
		boolean overlaps_tnumber_numspan(Pointer temp, Pointer s);
		boolean overlaps_tnumber_tbox(Pointer temp, Pointer box);
		boolean overlaps_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overlaps_tpoint_stbox(Pointer temp, Pointer box);
		boolean overlaps_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overlaps_tstzspan_temporal(Pointer s, Pointer temp);
		boolean same_numspan_tnumber(Pointer s, Pointer temp);
		boolean same_stbox_tpoint(Pointer box, Pointer temp);
		boolean same_tbox_tnumber(Pointer box, Pointer temp);
		boolean same_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean same_temporal_tstzspan(Pointer temp, Pointer s);
		boolean same_tnumber_numspan(Pointer temp, Pointer s);
		boolean same_tnumber_tbox(Pointer temp, Pointer box);
		boolean same_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean same_tpoint_stbox(Pointer temp, Pointer box);
		boolean same_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean same_tstzspan_temporal(Pointer s, Pointer temp);
		boolean above_stbox_tpoint(Pointer box, Pointer temp);
		boolean above_tpoint_stbox(Pointer temp, Pointer box);
		boolean above_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean after_stbox_tpoint(Pointer box, Pointer temp);
		boolean after_tbox_tnumber(Pointer box, Pointer temp);
		boolean after_temporal_tstzspan(Pointer temp, Pointer s);
		boolean after_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean after_tnumber_tbox(Pointer temp, Pointer box);
		boolean after_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean after_tpoint_stbox(Pointer temp, Pointer box);
		boolean after_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean after_tstzspan_temporal(Pointer s, Pointer temp);
		boolean back_stbox_tpoint(Pointer box, Pointer temp);
		boolean back_tpoint_stbox(Pointer temp, Pointer box);
		boolean back_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean before_stbox_tpoint(Pointer box, Pointer temp);
		boolean before_tbox_tnumber(Pointer box, Pointer temp);
		boolean before_temporal_tstzspan(Pointer temp, Pointer s);
		boolean before_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean before_tnumber_tbox(Pointer temp, Pointer box);
		boolean before_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean before_tpoint_stbox(Pointer temp, Pointer box);
		boolean before_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean before_tstzspan_temporal(Pointer s, Pointer temp);
		boolean below_stbox_tpoint(Pointer box, Pointer temp);
		boolean below_tpoint_stbox(Pointer temp, Pointer box);
		boolean below_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean front_stbox_tpoint(Pointer box, Pointer temp);
		boolean front_tpoint_stbox(Pointer temp, Pointer box);
		boolean front_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean left_stbox_tpoint(Pointer box, Pointer temp);
		boolean left_tbox_tnumber(Pointer box, Pointer temp);
		boolean left_numspan_tnumber(Pointer s, Pointer temp);
		boolean left_tnumber_numspan(Pointer temp, Pointer s);
		boolean left_tnumber_tbox(Pointer temp, Pointer box);
		boolean left_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean left_tpoint_stbox(Pointer temp, Pointer box);
		boolean left_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overabove_stbox_tpoint(Pointer box, Pointer temp);
		boolean overabove_tpoint_stbox(Pointer temp, Pointer box);
		boolean overabove_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overafter_stbox_tpoint(Pointer box, Pointer temp);
		boolean overafter_tbox_tnumber(Pointer box, Pointer temp);
		boolean overafter_temporal_tstzspan(Pointer temp, Pointer s);
		boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean overafter_tnumber_tbox(Pointer temp, Pointer box);
		boolean overafter_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overafter_tpoint_stbox(Pointer temp, Pointer box);
		boolean overafter_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overafter_tstzspan_temporal(Pointer s, Pointer temp);
		boolean overback_stbox_tpoint(Pointer box, Pointer temp);
		boolean overback_tpoint_stbox(Pointer temp, Pointer box);
		boolean overback_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overbefore_stbox_tpoint(Pointer box, Pointer temp);
		boolean overbefore_tbox_tnumber(Pointer box, Pointer temp);
		boolean overbefore_temporal_tstzspan(Pointer temp, Pointer s);
		boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean overbefore_tnumber_tbox(Pointer temp, Pointer box);
		boolean overbefore_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overbefore_tpoint_stbox(Pointer temp, Pointer box);
		boolean overbefore_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overbefore_tstzspan_temporal(Pointer s, Pointer temp);
		boolean overbelow_stbox_tpoint(Pointer box, Pointer temp);
		boolean overbelow_tpoint_stbox(Pointer temp, Pointer box);
		boolean overbelow_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overfront_stbox_tpoint(Pointer box, Pointer temp);
		boolean overfront_tpoint_stbox(Pointer temp, Pointer box);
		boolean overfront_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overleft_numspan_tnumber(Pointer s, Pointer temp);
		boolean overleft_stbox_tpoint(Pointer box, Pointer temp);
		boolean overleft_tbox_tnumber(Pointer box, Pointer temp);
		boolean overleft_tnumber_numspan(Pointer temp, Pointer s);
		boolean overleft_tnumber_tbox(Pointer temp, Pointer box);
		boolean overleft_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overleft_tpoint_stbox(Pointer temp, Pointer box);
		boolean overleft_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overright_numspan_tnumber(Pointer s, Pointer temp);
		boolean overright_stbox_tpoint(Pointer box, Pointer temp);
		boolean overright_tbox_tnumber(Pointer box, Pointer temp);
		boolean overright_tnumber_numspan(Pointer temp, Pointer s);
		boolean overright_tnumber_tbox(Pointer temp, Pointer box);
		boolean overright_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overright_tpoint_stbox(Pointer temp, Pointer box);
		boolean overright_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean right_numspan_tnumber(Pointer s, Pointer temp);
		boolean right_stbox_tpoint(Pointer box, Pointer temp);
		boolean right_tbox_tnumber(Pointer box, Pointer temp);
		boolean right_tnumber_numspan(Pointer temp, Pointer s);
		boolean right_tnumber_tbox(Pointer temp, Pointer box);
		boolean right_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean right_tpoint_stbox(Pointer temp, Pointer box);
		boolean right_tpoint_tpoint(Pointer temp1, Pointer temp2);
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
		Pointer mult_float_tfloat(double d, Pointer tnumber);
		Pointer mult_int_tint(int i, Pointer tnumber);
		Pointer mult_tfloat_float(Pointer tnumber, double d);
		Pointer mult_tint_int(Pointer tnumber, int i);
		Pointer mult_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);
		Pointer sub_float_tfloat(double d, Pointer tnumber);
		Pointer sub_int_tint(int i, Pointer tnumber);
		Pointer sub_tfloat_float(Pointer tnumber, double d);
		Pointer sub_tint_int(Pointer tnumber, int i);
		Pointer sub_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);
		Pointer tfloat_derivative(Pointer temp);
		Pointer tnumber_abs(Pointer temp);
		Pointer tnumber_angular_difference(Pointer temp);
		Pointer tnumber_delta_value(Pointer temp);
		Pointer textcat_text_ttext(Pointer txt, Pointer temp);
		Pointer textcat_ttext_text(Pointer temp, Pointer txt);
		Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2);
		Pointer ttext_upper(Pointer temp);
		Pointer ttext_lower(Pointer temp);
		Pointer ttext_initcap(Pointer temp);
		Pointer distance_tfloat_float(Pointer temp, double d);
		Pointer distance_tint_int(Pointer temp, int i);
		Pointer distance_tnumber_tnumber(Pointer temp1, Pointer temp2);
		Pointer distance_tpoint_point(Pointer temp, Pointer gs);
		Pointer distance_tpoint_tpoint(Pointer temp1, Pointer temp2);
		double nad_stbox_geo(Pointer box, Pointer gs);
		double nad_stbox_stbox(Pointer box1, Pointer box2);
		int nad_tint_int(Pointer temp, int i);
		int nad_tint_tbox(Pointer temp, Pointer box);
		int nad_tint_tint(Pointer temp1, Pointer temp2);
		int nad_tboxint_tboxint(Pointer box1, Pointer box2);
		double nad_tfloat_float(Pointer temp, double d);
		double nad_tfloat_tfloat(Pointer temp1, Pointer temp2);
		double nad_tfloat_tbox(Pointer temp, Pointer box);
		double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2);
		double nad_tpoint_geo(Pointer temp, Pointer gs);
		double nad_tpoint_stbox(Pointer temp, Pointer box);
		double nad_tpoint_tpoint(Pointer temp1, Pointer temp2);
		Pointer nai_tpoint_geo(Pointer temp, Pointer gs);
		Pointer nai_tpoint_tpoint(Pointer temp1, Pointer temp2);
		Pointer shortestline_tpoint_geo(Pointer temp, Pointer gs);
		Pointer shortestline_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean bearing_point_point(Pointer gs1, Pointer gs2, Pointer result);
		Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert);
		Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2);
		Pointer tpoint_angular_difference(Pointer temp);
		Pointer tpoint_azimuth(Pointer temp);
		Pointer tpoint_convex_hull(Pointer temp);
		Pointer tpoint_cumulative_length(Pointer temp);
		boolean tpoint_direction(Pointer temp, Pointer result);
		Pointer tpoint_get_x(Pointer temp);
		Pointer tpoint_get_y(Pointer temp);
		Pointer tpoint_get_z(Pointer temp);
		boolean tpoint_is_simple(Pointer temp);
		double tpoint_length(Pointer temp);
		Pointer tpoint_speed(Pointer temp);
		int tpoint_srid(Pointer temp);
		Pointer tpoint_stboxes(Pointer temp, Pointer count);
		Pointer tpoint_trajectory(Pointer temp);
		Pointer tpoint_twcentroid(Pointer temp);
		Pointer geo_expand_space(Pointer gs, double d);
		Pointer geomeas_to_tpoint(Pointer gs);
		Pointer tgeogpoint_to_tgeompoint(Pointer temp);
		Pointer tgeompoint_to_tgeogpoint(Pointer temp);
		boolean tpoint_AsMVTGeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count);
		Pointer tpoint_expand_space(Pointer temp, double d);
		Pointer tpoint_make_simple(Pointer temp, Pointer count);
		Pointer tpoint_set_srid(Pointer temp, int srid);
		boolean tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize, Pointer result);
		int acontains_geo_tpoint(Pointer gs, Pointer temp);
		int adisjoint_tpoint_geo(Pointer temp, Pointer gs);
		int adisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int adwithin_tpoint_geo(Pointer temp, Pointer gs, double dist);
		int adwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist);
		int aintersects_tpoint_geo(Pointer temp, Pointer gs);
		int aintersects_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int atouches_tpoint_geo(Pointer temp, Pointer gs);
		int econtains_geo_tpoint(Pointer gs, Pointer temp);
		int edisjoint_tpoint_geo(Pointer temp, Pointer gs);
		int edisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int edwithin_tpoint_geo(Pointer temp, Pointer gs, double dist);
		int edwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist);
		int eintersects_tpoint_geo(Pointer temp, Pointer gs);
		int eintersects_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int etouches_tpoint_geo(Pointer temp, Pointer gs);
		Pointer tcontains_geo_tpoint(Pointer gs, Pointer temp, boolean restr, boolean atvalue);
		Pointer tdisjoint_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);
		Pointer tdisjoint_tpoint_tpoint (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue);
		Pointer tdwithin_tpoint_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue);
		Pointer tdwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue);
		Pointer tintersects_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);
		Pointer tintersects_tpoint_tpoint (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue);
		Pointer ttouches_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);
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
		Pointer tpoint_extent_transfn(Pointer box, Pointer temp);
		Pointer tpoint_tcentroid_finalfn(Pointer state);
		Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp);
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
		double float_bucket(double value, double size, double origin);
		Pointer floatspan_bucket_list(Pointer bounds, double size, double origin, Pointer count);
		int int_bucket(int value, int size, int origin);
		Pointer intspan_bucket_list(Pointer bounds, int size, int origin, Pointer count);
		Pointer stbox_tile(Pointer point, long t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean hast);
		Pointer stbox_tile_list(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean border_inc, Pointer count);
		Pointer temporal_time_split(Pointer temp, Pointer duration, long torigin, Pointer time_buckets, Pointer count);
		Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer value_buckets, Pointer count);
		Pointer tfloat_value_time_split(Pointer temp, double size, Pointer duration, double vorigin, long torigin, Pointer value_buckets, Pointer time_buckets, Pointer count);
		Pointer tfloatbox_tile(double value, long t, double vsize, Pointer duration, double vorigin, long torigin);
		Pointer tfloatbox_tile_list(Pointer box, double xsize, Pointer duration, double xorigin, long torigin, Pointer count);
		long timestamptz_bucket(long timestamp, Pointer duration, long origin);
		Pointer tint_value_split(Pointer temp, int size, int origin, Pointer value_buckets, Pointer count);
		Pointer tint_value_time_split(Pointer temp, int size, Pointer duration, int vorigin, long torigin, Pointer value_buckets, Pointer time_buckets, Pointer count);
		Pointer tintbox_tile(int value, long t, int vsize, Pointer duration, int vorigin, long torigin);
		Pointer tintbox_tile_list(Pointer box, int xsize, Pointer duration, int xorigin, long torigin, Pointer count);
		Pointer tpoint_space_split(Pointer temp, float xsize, float ysize, float zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_buckets, Pointer count);
		Pointer tpoint_space_time_split(Pointer temp, float xsize, float ysize, float zsize, Pointer duration, Pointer sorigin, long torigin, boolean bitmatrix, boolean border_inc, Pointer space_buckets, Pointer time_buckets, Pointer count);
		Pointer tstzspan_bucket_list(Pointer bounds, Pointer duration, long origin, Pointer count);
	}

	// Native library name
	private static final String _LIB = "meos";

	// One JNR-FFI proxy per sub-interface.
	// Each proxy <clinit>()V has ≤ 372 methods → well under the JVM 64 KB limit.
	static final MeosLibraryPartA _meos_a = JarLibraryLoader.create(MeosLibraryPartA.class, _LIB).getLibraryInstance();
	static final MeosLibraryPartB _meos_b = JarLibraryLoader.create(MeosLibraryPartB.class, _LIB).getLibraryInstance();
	static final MeosLibraryPartC _meos_c = JarLibraryLoader.create(MeosLibraryPartC.class, _LIB).getLibraryInstance();
	static final MeosLibraryPartD _meos_d = JarLibraryLoader.create(MeosLibraryPartD.class, _LIB).getLibraryInstance();

	/**
	 * @deprecated Use the public static wrappers directly.
	 *   This interface is kept for backward compatibility with generated code.
	 *   Do NOT pass MeosLibrary.class to JNR-FFI — it still
	 *   has 1486 methods and would recreate the MethodTooLargeException.
	 */
	@Deprecated
	public interface MeosLibrary {
		String gitLibraryPath= "/home/runner/work/JMEOS/JMEOS/src/lib";
		String libraryName= "meos";
		/** Use the public static wrappers instead. */
		MeosLibrary INSTANCE = new MeosLibraryDelegate();
		/** Use the public static wrappers instead. */
		MeosLibrary meos = INSTANCE;
		int geo_get_srid(Pointer g);
		void meos_error(int errlevel, int errcode, String format, Pointer args);
		int meos_errno();
		int meos_errno_set(int err);
		int meos_errno_restore(int err);
		int meos_errno_reset();
		void meos_initialize_timezone(String name);
		void meos_initialize_error_handler(error_handler_fn err_handler);
		void meos_finalize_timezone();
		boolean meos_set_datestyle(String newval, Pointer extra);
		boolean meos_set_intervalstyle(String newval, int extra);
		String meos_get_datestyle();
		String meos_get_intervalstyle();
		void meos_initialize(String tz_str, error_handler_fn err_handler);
		void meos_finalize();
		int add_date_int(int d, int days);
		Pointer add_interval_interval(Pointer interv1, Pointer interv2);
		long add_timestamptz_interval(long t, Pointer interv);
		boolean bool_in(String str);
		String bool_out(boolean b);
		Pointer cstring2text(String str);
		long date_to_timestamptz(int d);
		Pointer minus_date_date(int d1, int d2);
		int minus_date_int(int d, int days);
		long minus_timestamptz_interval(long t, Pointer interv);
		Pointer minus_timestamptz_timestamptz(long t1, long t2);
		Pointer mult_interval_double(Pointer interv, double factor);
		int pg_date_in(String str);
		String pg_date_out(int d);
		int pg_interval_cmp(Pointer interv1, Pointer interv2);
		Pointer pg_interval_in(String str, int typmod);
		Pointer pg_interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs);
		String pg_interval_out(Pointer interv);
		long pg_time_in(String str, int typmod);
		String pg_time_out(long t);
		long pg_timestamp_in(String str, int typmod);
		String pg_timestamp_out(long t);
		long pg_timestamptz_in(String str, int typmod);
		String pg_timestamptz_out(long t);
		String text2cstring(Pointer txt);
		int text_cmp(Pointer txt1, Pointer txt2);
		Pointer text_copy(Pointer txt);
		Pointer text_initcap(Pointer txt);
		Pointer text_lower(Pointer txt);
		String text_out(Pointer txt);
		Pointer text_upper(Pointer txt);
		Pointer textcat_text_text(Pointer txt1, Pointer txt2);
		int timestamptz_to_date(long t);
		Pointer geo_as_ewkb(Pointer gs, String endian);
		String geo_as_ewkt(Pointer gs, int precision);
		String geo_as_geojson(Pointer gs, int option, int precision, String srs);
		String geo_as_hexewkb(Pointer gs, String endian);
		String geo_as_text(Pointer gs, int precision);
		Pointer geo_from_ewkb(Pointer bytea_wkb, int srid);
		Pointer geo_from_geojson(String geojson);
		String geo_out(Pointer gs);
		boolean geo_same(Pointer gs1, Pointer gs2);
		Pointer geography_from_hexewkb(String wkt);
		Pointer geography_from_text(String wkt, int srid);
		Pointer geometry_from_hexewkb(String wkt);
		Pointer geometry_from_text(String wkt, int srid);
		Pointer pgis_geography_in(String str, int typmod);
		Pointer pgis_geometry_in(String str, int typmod);
		Pointer bigintset_in(String str);
		String bigintset_out(Pointer set);
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
		Pointer floatspan_in(String str);
		String floatspan_out(Pointer s, int maxdd);
		Pointer floatspanset_in(String str);
		String floatspanset_out(Pointer ss, int maxdd);
		Pointer geogset_in(String str);
		Pointer geomset_in(String str);
		String geoset_as_ewkt(Pointer set, int maxdd);
		String geoset_as_text(Pointer set, int maxdd);
		String geoset_out(Pointer set, int maxdd);
		Pointer intset_in(String str);
		String intset_out(Pointer set);
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
		Pointer geoset_make(Pointer values, int count);
		Pointer intset_make(Pointer values, int count);
		Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc);
		Pointer set_copy(Pointer s);
		Pointer span_copy(Pointer s);
		Pointer spanset_copy(Pointer ss);
		Pointer spanset_make(Pointer spans, int count, boolean normalize, boolean order);
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
		Pointer geo_to_set(Pointer gs);
		Pointer int_to_set(int i);
		Pointer int_to_span(int i);
		Pointer int_to_spanset(int i);
		Pointer intset_to_floatset(Pointer s);
		Pointer intspan_to_floatspan(Pointer s);
		Pointer intspanset_to_floatspanset(Pointer ss);
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
		Pointer bigintset_values(Pointer s);
		long bigintspan_lower(Pointer s);
		long bigintspan_upper(Pointer s);
		long bigintspan_width(Pointer s);
		long bigintspanset_lower(Pointer ss);
		long bigintspanset_upper(Pointer ss);
		long bigintspanset_width(Pointer ss, boolean boundspan);
		int dateset_end_value(Pointer s);
		int dateset_start_value(Pointer s);
		boolean dateset_value_n(Pointer s, int n, Pointer result);
		Pointer dateset_values(Pointer s);
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
		Pointer floatset_values(Pointer s);
		double floatspan_lower(Pointer s);
		double floatspan_upper(Pointer s);
		double floatspan_width(Pointer s);
		double floatspanset_lower(Pointer ss);
		double floatspanset_upper(Pointer ss);
		double floatspanset_width(Pointer ss, boolean boundspan);
		Pointer geoset_end_value(Pointer s);
		int geoset_srid(Pointer s);
		Pointer geoset_start_value(Pointer s);
		boolean geoset_value_n(Pointer s, int n, Pointer result);
		Pointer geoset_values(Pointer s);
		int intset_end_value(Pointer s);
		int intset_start_value(Pointer s);
		boolean intset_value_n(Pointer s, int n, Pointer result);
		Pointer intset_values(Pointer s);
		int intspan_lower(Pointer s);
		int intspan_upper(Pointer s);
		int intspan_width(Pointer s);
		int intspanset_lower(Pointer ss);
		int intspanset_upper(Pointer ss);
		int intspanset_width(Pointer ss, boolean boundspan);
		int set_hash(Pointer s);
		long set_hash_extended(Pointer s, long seed);
		int set_num_values(Pointer s);
		Pointer set_to_span(Pointer s);
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
		Pointer spanset_spans(Pointer ss);
		Pointer spanset_start_span(Pointer ss);
		boolean spanset_upper_inc(Pointer ss);
		Pointer textset_end_value(Pointer s);
		Pointer textset_start_value(Pointer s);
		boolean textset_value_n(Pointer s, int n, Pointer result);
		Pointer textset_values(Pointer s);
		long tstzset_end_value(Pointer s);
		long tstzset_start_value(Pointer s);
		boolean tstzset_value_n(Pointer s, int n, Pointer result);
		Pointer tstzset_values(Pointer s);
		Pointer tstzspan_duration(Pointer s);
		long tstzspan_lower(Pointer s);
		long tstzspan_upper(Pointer s);
		Pointer tstzspanset_duration(Pointer ss, boolean boundspan);
		long tstzspanset_end_timestamptz(Pointer ss);
		long tstzspanset_lower(Pointer ss);
		int tstzspanset_num_timestamps(Pointer ss);
		long tstzspanset_start_timestamptz(Pointer ss);
		boolean tstzspanset_timestamptz_n(Pointer ss, int n, Pointer result);
		Pointer tstzspanset_timestamps(Pointer ss);
		long tstzspanset_upper(Pointer ss);
		Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);
		Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);
		Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth);
		Pointer dateset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer datespan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer datespanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer floatset_ceil(Pointer s);
		Pointer floatset_floor(Pointer s);
		Pointer floatset_degrees(Pointer s, boolean normalize);
		Pointer floatset_radians(Pointer s);
		Pointer floatset_round(Pointer s, int maxdd);
		Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);
		Pointer floatspan_ceil(Pointer s);
		Pointer floatspan_floor(Pointer s);
		Pointer floatspan_round(Pointer s, int maxdd);
		Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);
		Pointer floatspanset_ceil(Pointer ss);
		Pointer floatspanset_floor(Pointer ss);
		Pointer floatspanset_round(Pointer ss, int maxdd);
		Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth);
		Pointer geoset_round(Pointer s, int maxdd);
		Pointer geoset_set_srid(Pointer s, int srid);
		Pointer geoset_transform(Pointer s, int srid);
		Pointer geoset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward);
		Pointer point_transform(Pointer gs, int srid);
		Pointer point_transform_pipeline(Pointer gs, String pipelinestr, int srid, boolean is_forward);
		Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer textset_initcap(Pointer s);
		Pointer textset_lower(Pointer s);
		Pointer textset_upper(Pointer s);
		Pointer textcat_textset_text(Pointer s, Pointer txt);
		Pointer textcat_text_textset(Pointer txt, Pointer s);
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
		boolean contained_geo_set(Pointer gs, Pointer s);
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
		boolean contains_set_geo(Pointer s, Pointer gs);
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
		Pointer intersection_geo_set(Pointer gs, Pointer s);
		Pointer intersection_int_set(int i, Pointer s);
		Pointer intersection_set_bigint(Pointer s, long i);
		Pointer intersection_set_date(Pointer s, int d);
		Pointer intersection_set_float(Pointer s, double d);
		Pointer intersection_set_geo(Pointer s, Pointer gs);
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
		Pointer minus_geo_set(Pointer gs, Pointer s);
		Pointer minus_int_set(int i, Pointer s);
		Pointer minus_int_span(int i, Pointer s);
		Pointer minus_int_spanset(int i, Pointer ss);
		Pointer minus_set_bigint(Pointer s, long i);
		Pointer minus_set_date(Pointer s, int d);
		Pointer minus_set_float(Pointer s, double d);
		Pointer minus_set_geo(Pointer s, Pointer gs);
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
		Pointer union_geo_set(Pointer gs, Pointer s);
		Pointer union_int_set(int i, Pointer s);
		Pointer union_int_span(int i, Pointer s);
		Pointer union_int_spanset(int i, Pointer ss);
		Pointer union_set_bigint(Pointer s, long i);
		Pointer union_set_date(Pointer s, int d);
		Pointer union_set_float(Pointer s, double d);
		Pointer union_set_geo(Pointer s, Pointer gs);
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
		Pointer tbox_in(String str);
		String tbox_out(Pointer box, int maxdd);
		Pointer tbox_from_wkb(Pointer wkb, long size);
		Pointer tbox_from_hexwkb(String hexwkb);
		Pointer stbox_from_wkb(Pointer wkb, long size);
		Pointer stbox_from_hexwkb(String hexwkb);
		Pointer tbox_as_wkb(Pointer box, byte variant, Pointer size_out);
		String tbox_as_hexwkb(Pointer box, byte variant, Pointer size);
		Pointer stbox_as_wkb(Pointer box, byte variant, Pointer size_out);
		String stbox_as_hexwkb(Pointer box, byte variant, Pointer size);
		Pointer stbox_in(String str);
		String stbox_out(Pointer box, int maxdd);
		Pointer float_tstzspan_to_tbox(double d, Pointer s);
		Pointer float_timestamptz_to_tbox(double d, long t);
		Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s);
		Pointer geo_timestamptz_to_stbox(Pointer gs, long t);
		Pointer int_tstzspan_to_tbox(int i, Pointer s);
		Pointer int_timestamptz_to_tbox(int i, long t);
		Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s);
		Pointer numspan_timestamptz_to_tbox(Pointer span, long t);
		Pointer stbox_copy(Pointer box);
		Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s);
		Pointer tbox_copy(Pointer box);
		Pointer tbox_make(Pointer s, Pointer p);
		Pointer float_to_tbox(double d);
		Pointer geo_to_stbox(Pointer gs);
		Pointer int_to_tbox(int i);
		Pointer set_to_tbox(Pointer s);
		Pointer span_to_tbox(Pointer s);
		Pointer spanset_to_tbox(Pointer ss);
		Pointer spatialset_to_stbox(Pointer s);
		Pointer stbox_to_gbox(Pointer box);
		Pointer stbox_to_box3d(Pointer box);
		Pointer stbox_to_geo(Pointer box);
		Pointer stbox_to_tstzspan(Pointer box);
		Pointer tbox_to_intspan(Pointer box);
		Pointer tbox_to_floatspan(Pointer box);
		Pointer tbox_to_tstzspan(Pointer box);
		Pointer timestamptz_to_stbox(long t);
		Pointer timestamptz_to_tbox(long t);
		Pointer tstzset_to_stbox(Pointer s);
		Pointer tstzspan_to_stbox(Pointer s);
		Pointer tstzspanset_to_stbox(Pointer ss);
		Pointer tnumber_to_tbox(Pointer temp);
		Pointer tpoint_to_stbox(Pointer temp);
		boolean stbox_hast(Pointer box);
		boolean stbox_hasx(Pointer box);
		boolean stbox_hasz(Pointer box);
		boolean stbox_isgeodetic(Pointer box);
		int stbox_srid(Pointer box);
		boolean stbox_tmax(Pointer box, Pointer result);
		boolean stbox_tmax_inc(Pointer box, Pointer result);
		boolean stbox_tmin(Pointer box, Pointer result);
		boolean stbox_tmin_inc(Pointer box, Pointer result);
		boolean stbox_xmax(Pointer box, Pointer result);
		boolean stbox_xmin(Pointer box, Pointer result);
		boolean stbox_ymax(Pointer box, Pointer result);
		boolean stbox_ymin(Pointer box, Pointer result);
		boolean stbox_zmax(Pointer box, Pointer result);
		boolean stbox_zmin(Pointer box, Pointer result);
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
		Pointer stbox_expand_space(Pointer box, double d);
		Pointer stbox_expand_time(Pointer box, Pointer interv);
		Pointer stbox_get_space(Pointer box);
		Pointer stbox_quad_split(Pointer box, Pointer count);
		Pointer stbox_round(Pointer box, int maxdd);
		Pointer stbox_set_srid(Pointer box, int srid);
		Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);
		Pointer stbox_transform(Pointer box, int srid);
		Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward);
		Pointer tbox_expand_time(Pointer box, Pointer interv);
		Pointer tbox_expand_float(Pointer box, double d);
		Pointer tbox_expand_int(Pointer box, int i);
		Pointer tbox_round(Pointer box, int maxdd);
		Pointer tbox_shift_scale_float(Pointer box, double shift, double width, boolean hasshift, boolean haswidth);
		Pointer tbox_shift_scale_int(Pointer box, int shift, int width, boolean hasshift, boolean haswidth);
		Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);
		Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict);
		Pointer intersection_tbox_tbox(Pointer box1, Pointer box2);
		Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict);
		Pointer intersection_stbox_stbox(Pointer box1, Pointer box2);
		boolean adjacent_stbox_stbox(Pointer box1, Pointer box2);
		boolean adjacent_tbox_tbox(Pointer box1, Pointer box2);
		boolean contained_tbox_tbox(Pointer box1, Pointer box2);
		boolean contained_stbox_stbox(Pointer box1, Pointer box2);
		boolean contains_stbox_stbox(Pointer box1, Pointer box2);
		boolean contains_tbox_tbox(Pointer box1, Pointer box2);
		boolean overlaps_tbox_tbox(Pointer box1, Pointer box2);
		boolean overlaps_stbox_stbox(Pointer box1, Pointer box2);
		boolean same_tbox_tbox(Pointer box1, Pointer box2);
		boolean same_stbox_stbox(Pointer box1, Pointer box2);
		boolean left_tbox_tbox(Pointer box1, Pointer box2);
		boolean overleft_tbox_tbox(Pointer box1, Pointer box2);
		boolean right_tbox_tbox(Pointer box1, Pointer box2);
		boolean overright_tbox_tbox(Pointer box1, Pointer box2);
		boolean before_tbox_tbox(Pointer box1, Pointer box2);
		boolean overbefore_tbox_tbox(Pointer box1, Pointer box2);
		boolean after_tbox_tbox(Pointer box1, Pointer box2);
		boolean overafter_tbox_tbox(Pointer box1, Pointer box2);
		boolean left_stbox_stbox(Pointer box1, Pointer box2);
		boolean overleft_stbox_stbox(Pointer box1, Pointer box2);
		boolean right_stbox_stbox(Pointer box1, Pointer box2);
		boolean overright_stbox_stbox(Pointer box1, Pointer box2);
		boolean below_stbox_stbox(Pointer box1, Pointer box2);
		boolean overbelow_stbox_stbox(Pointer box1, Pointer box2);
		boolean above_stbox_stbox(Pointer box1, Pointer box2);
		boolean overabove_stbox_stbox(Pointer box1, Pointer box2);
		boolean front_stbox_stbox(Pointer box1, Pointer box2);
		boolean overfront_stbox_stbox(Pointer box1, Pointer box2);
		boolean back_stbox_stbox(Pointer box1, Pointer box2);
		boolean overback_stbox_stbox(Pointer box1, Pointer box2);
		boolean before_stbox_stbox(Pointer box1, Pointer box2);
		boolean overbefore_stbox_stbox(Pointer box1, Pointer box2);
		boolean after_stbox_stbox(Pointer box1, Pointer box2);
		boolean overafter_stbox_stbox(Pointer box1, Pointer box2);
		boolean tbox_eq(Pointer box1, Pointer box2);
		boolean tbox_ne(Pointer box1, Pointer box2);
		int tbox_cmp(Pointer box1, Pointer box2);
		boolean tbox_lt(Pointer box1, Pointer box2);
		boolean tbox_le(Pointer box1, Pointer box2);
		boolean tbox_ge(Pointer box1, Pointer box2);
		boolean tbox_gt(Pointer box1, Pointer box2);
		boolean stbox_eq(Pointer box1, Pointer box2);
		boolean stbox_ne(Pointer box1, Pointer box2);
		int stbox_cmp(Pointer box1, Pointer box2);
		boolean stbox_lt(Pointer box1, Pointer box2);
		boolean stbox_le(Pointer box1, Pointer box2);
		boolean stbox_ge(Pointer box1, Pointer box2);
		boolean stbox_gt(Pointer box1, Pointer box2);
		Pointer tbool_in(String str);
		Pointer tint_in(String str);
		Pointer tfloat_in(String str);
		Pointer ttext_in(String str);
		Pointer tgeompoint_in(String str);
		Pointer tgeogpoint_in(String str);
		Pointer tbool_from_mfjson(String str);
		Pointer tint_from_mfjson(String str);
		Pointer tfloat_from_mfjson(String str);
		Pointer ttext_from_mfjson(String str);
		Pointer tgeompoint_from_mfjson(String str);
		Pointer tgeogpoint_from_mfjson(String str);
		Pointer temporal_from_wkb(Pointer wkb, long size);
		Pointer temporal_from_hexwkb(String hexwkb);
		String tbool_out(Pointer temp);
		String tint_out(Pointer temp);
		String tfloat_out(Pointer temp, int maxdd);
		String ttext_out(Pointer temp);
		String tpoint_out(Pointer temp, int maxdd);
		String tpoint_as_text(Pointer temp, int maxdd);
		String tpoint_as_ewkt(Pointer temp, int maxdd);
		String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs);
		Pointer temporal_as_wkb(Pointer temp, byte variant, Pointer size_out);
		String temporal_as_hexwkb(Pointer temp, byte variant, Pointer size_out);
		Pointer tbool_from_base_temp(boolean b, Pointer temp);
		Pointer tboolinst_make(boolean b, long t);
		Pointer tboolseq_from_base_tstzset(boolean b, Pointer s);
		Pointer tboolseq_from_base_tstzspan(boolean b, Pointer s);
		Pointer tboolseqset_from_base_tstzspanset(boolean b, Pointer ss);
		Pointer temporal_copy(Pointer temp);
		Pointer tfloat_from_base_temp(double d, Pointer temp);
		Pointer tfloatinst_make(double d, long t);
		Pointer tfloatseq_from_base_tstzspan(double d, Pointer s, int interp);
		Pointer tfloatseq_from_base_tstzset(double d, Pointer s);
		Pointer tfloatseqset_from_base_tstzspanset(double d, Pointer ss, int interp);
		Pointer tint_from_base_temp(int i, Pointer temp);
		Pointer tintinst_make(int i, long t);
		Pointer tintseq_from_base_tstzspan(int i, Pointer s);
		Pointer tintseq_from_base_tstzset(int i, Pointer s);
		Pointer tintseqset_from_base_tstzspanset(int i, Pointer ss);
		Pointer tpoint_from_base_temp(Pointer gs, Pointer temp);
		Pointer tpointinst_make(Pointer gs, long t);
		Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp);
		Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s);
		Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp);
		Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);
		Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize);
		Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist);
		Pointer ttext_from_base_temp(Pointer txt, Pointer temp);
		Pointer ttextinst_make(Pointer txt, long t);
		Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s);
		Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s);
		Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss);
		Pointer temporal_to_tstzspan(Pointer temp);
		Pointer tfloat_to_tint(Pointer temp);
		Pointer tint_to_tfloat(Pointer temp);
		Pointer tnumber_to_span(Pointer temp);
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
		Pointer temporal_max_instant(Pointer temp);
		Pointer temporal_min_instant(Pointer temp);
		int temporal_num_instants(Pointer temp);
		int temporal_num_sequences(Pointer temp);
		int temporal_num_timestamps(Pointer temp);
		Pointer temporal_segments(Pointer temp, Pointer count);
		Pointer temporal_sequence_n(Pointer temp, int i);
		Pointer temporal_sequences(Pointer temp, Pointer count);
		int temporal_lower_inc(Pointer temp);
		int temporal_upper_inc(Pointer temp);
		Pointer temporal_start_instant(Pointer temp);
		Pointer temporal_start_sequence(Pointer temp);
		long temporal_start_timestamptz(Pointer temp);
		Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration);
		String temporal_subtype(Pointer temp);
		Pointer temporal_time(Pointer temp);
		boolean temporal_timestamptz_n(Pointer temp, int n, Pointer result);
		Pointer temporal_timestamps(Pointer temp, Pointer count);
		double tfloat_end_value(Pointer temp);
		double tfloat_max_value(Pointer temp);
		double tfloat_min_value(Pointer temp);
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
		double tnumber_integral(Pointer temp);
		double tnumber_twavg(Pointer temp);
		Pointer tnumber_valuespans(Pointer temp);
		Pointer tpoint_end_value(Pointer temp);
		Pointer tpoint_start_value(Pointer temp);
		boolean tpoint_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);
		boolean tpoint_value_n(Pointer temp, int n, Pointer result);
		Pointer tpoint_values(Pointer temp, Pointer count);
		Pointer ttext_end_value(Pointer temp);
		Pointer ttext_max_value(Pointer temp);
		Pointer ttext_min_value(Pointer temp);
		Pointer ttext_start_value(Pointer temp);
		boolean ttext_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);
		boolean ttext_value_n(Pointer temp, int n, Pointer result);
		Pointer ttext_values(Pointer temp, Pointer count);
		double float_degrees(double value, boolean normalize);
		Pointer temporal_scale_time(Pointer temp, Pointer duration);
		Pointer temporal_set_interp(Pointer temp, int interp);
		Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration);
		Pointer temporal_shift_time(Pointer temp, Pointer shift);
		Pointer temporal_to_tinstant(Pointer temp);
		Pointer temporal_to_tsequence(Pointer temp, String interp_str);
		Pointer temporal_to_tsequenceset(Pointer temp, String interp_str);
		Pointer tfloat_floor(Pointer temp);
		Pointer tfloat_ceil(Pointer temp);
		Pointer tfloat_degrees(Pointer temp, boolean normalize);
		Pointer tfloat_radians(Pointer temp);
		Pointer tfloat_round(Pointer temp, int maxdd);
		Pointer tfloat_scale_value(Pointer temp, double width);
		Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width);
		Pointer tfloat_shift_value(Pointer temp, double shift);
		Pointer tfloatarr_round(Pointer temp, int count, int maxdd);
		Pointer tint_scale_value(Pointer temp, int width);
		Pointer tint_shift_scale_value(Pointer temp, int shift, int width);
		Pointer tint_shift_value(Pointer temp, int shift);
		Pointer tpoint_round(Pointer temp, int maxdd);
		Pointer tpoint_transform(Pointer temp, int srid);
		Pointer tpoint_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward);
		Pointer tpoint_transform_pj(Pointer temp, int srid, Pointer pj);
		Pointer lwproj_transform(int srid_from, int srid_to);
		Pointer tpointarr_round(Pointer temp, int count, int maxdd);
		Pointer temporal_append_tinstant(Pointer temp, Pointer inst, double maxdist, Pointer maxt, boolean expand);
		Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand);
		Pointer temporal_delete_tstzspan(Pointer temp, Pointer s, boolean connect);
		Pointer temporal_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect);
		Pointer temporal_delete_timestamptz(Pointer temp, long t, boolean connect);
		Pointer temporal_delete_tstzset(Pointer temp, Pointer s, boolean connect);
		Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect);
		Pointer temporal_merge(Pointer temp1, Pointer temp2);
		Pointer temporal_merge_array(Pointer temparr, int count);
		Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect);
		Pointer tbool_at_value(Pointer temp, boolean b);
		Pointer tbool_minus_value(Pointer temp, boolean b);
		Pointer temporal_at_max(Pointer temp);
		Pointer temporal_at_min(Pointer temp);
		Pointer temporal_at_tstzspan(Pointer temp, Pointer s);
		Pointer temporal_at_tstzspanset(Pointer temp, Pointer ss);
		Pointer temporal_at_timestamptz(Pointer temp, long t);
		Pointer temporal_at_tstzset(Pointer temp, Pointer s);
		Pointer temporal_at_values(Pointer temp, Pointer set);
		Pointer temporal_minus_max(Pointer temp);
		Pointer temporal_minus_min(Pointer temp);
		Pointer temporal_minus_tstzspan(Pointer temp, Pointer s);
		Pointer temporal_minus_tstzspanset(Pointer temp, Pointer ss);
		Pointer temporal_minus_timestamptz(Pointer temp, long t);
		Pointer temporal_minus_tstzset(Pointer temp, Pointer s);
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
		Pointer tpoint_at_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period);
		Pointer tpoint_at_stbox(Pointer temp, Pointer box, boolean border_inc);
		Pointer tpoint_at_value(Pointer temp, Pointer gs);
		Pointer tpoint_minus_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period);
		Pointer tpoint_minus_stbox(Pointer temp, Pointer box, boolean border_inc);
		Pointer tpoint_minus_value(Pointer temp, Pointer gs);
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
		int always_eq_point_tpoint(Pointer gs, Pointer temp);
		int always_eq_tbool_bool(Pointer temp, boolean b);
		int always_eq_temporal_temporal(Pointer temp1, Pointer temp2);
		int always_eq_text_ttext(Pointer txt, Pointer temp);
		int always_eq_tfloat_float(Pointer temp, double d);
		int always_eq_tint_int(Pointer temp, int i);
		int always_eq_tpoint_point(Pointer temp, Pointer gs);
		int always_eq_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int always_eq_ttext_text(Pointer temp, Pointer txt);
		int always_ne_bool_tbool(boolean b, Pointer temp);
		int always_ne_float_tfloat(double d, Pointer temp);
		int always_ne_int_tint(int i, Pointer temp);
		int always_ne_point_tpoint(Pointer gs, Pointer temp);
		int always_ne_tbool_bool(Pointer temp, boolean b);
		int always_ne_temporal_temporal(Pointer temp1, Pointer temp2);
		int always_ne_text_ttext(Pointer txt, Pointer temp);
		int always_ne_tfloat_float(Pointer temp, double d);
		int always_ne_tint_int(Pointer temp, int i);
		int always_ne_tpoint_point(Pointer temp, Pointer gs);
		int always_ne_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int always_ne_ttext_text(Pointer temp, Pointer txt);
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
		int ever_eq_bool_tbool(boolean b, Pointer temp);
		int ever_eq_float_tfloat(double d, Pointer temp);
		int ever_eq_int_tint(int i, Pointer temp);
		int ever_eq_point_tpoint(Pointer gs, Pointer temp);
		int ever_eq_tbool_bool(Pointer temp, boolean b);
		int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2);
		int ever_eq_text_ttext(Pointer txt, Pointer temp);
		int ever_eq_tfloat_float(Pointer temp, double d);
		int ever_eq_tint_int(Pointer temp, int i);
		int ever_eq_tpoint_point(Pointer temp, Pointer gs);
		int ever_eq_tpoint_tpoint(Pointer temp1, Pointer temp2);
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
		int ever_ne_point_tpoint(Pointer gs, Pointer temp);
		int ever_ne_tbool_bool(Pointer temp, boolean b);
		int ever_ne_temporal_temporal(Pointer temp1, Pointer temp2);
		int ever_ne_text_ttext(Pointer txt, Pointer temp);
		int ever_ne_tfloat_float(Pointer temp, double d);
		int ever_ne_tint_int(Pointer temp, int i);
		int ever_ne_tpoint_point(Pointer temp, Pointer gs);
		int ever_ne_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int ever_ne_ttext_text(Pointer temp, Pointer txt);
		Pointer teq_bool_tbool(boolean b, Pointer temp);
		Pointer teq_float_tfloat(double d, Pointer temp);
		Pointer teq_int_tint(int i, Pointer temp);
		Pointer teq_point_tpoint(Pointer gs, Pointer temp);
		Pointer teq_tbool_bool(Pointer temp, boolean b);
		Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2);
		Pointer teq_text_ttext(Pointer txt, Pointer temp);
		Pointer teq_tfloat_float(Pointer temp, double d);
		Pointer teq_tpoint_point(Pointer temp, Pointer gs);
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
		Pointer tne_point_tpoint(Pointer gs, Pointer temp);
		Pointer tne_tbool_bool(Pointer temp, boolean b);
		Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2);
		Pointer tne_text_ttext(Pointer txt, Pointer temp);
		Pointer tne_tfloat_float(Pointer temp, double d);
		Pointer tne_tpoint_point(Pointer temp, Pointer gs);
		Pointer tne_tint_int(Pointer temp, int i);
		Pointer tne_ttext_text(Pointer temp, Pointer txt);
		boolean adjacent_numspan_tnumber(Pointer s, Pointer temp);
		boolean adjacent_stbox_tpoint(Pointer box, Pointer temp);
		boolean adjacent_tbox_tnumber(Pointer box, Pointer temp);
		boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean adjacent_temporal_tstzspan(Pointer temp, Pointer s);
		boolean adjacent_tnumber_numspan(Pointer temp, Pointer s);
		boolean adjacent_tnumber_tbox(Pointer temp, Pointer box);
		boolean adjacent_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean adjacent_tpoint_stbox(Pointer temp, Pointer box);
		boolean adjacent_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean adjacent_tstzspan_temporal(Pointer s, Pointer temp);
		boolean contained_numspan_tnumber(Pointer s, Pointer temp);
		boolean contained_stbox_tpoint(Pointer box, Pointer temp);
		boolean contained_tbox_tnumber(Pointer box, Pointer temp);
		boolean contained_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean contained_temporal_tstzspan(Pointer temp, Pointer s);
		boolean contained_tnumber_numspan(Pointer temp, Pointer s);
		boolean contained_tnumber_tbox(Pointer temp, Pointer box);
		boolean contained_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean contained_tpoint_stbox(Pointer temp, Pointer box);
		boolean contained_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean contained_tstzspan_temporal(Pointer s, Pointer temp);
		boolean contains_numspan_tnumber(Pointer s, Pointer temp);
		boolean contains_stbox_tpoint(Pointer box, Pointer temp);
		boolean contains_tbox_tnumber(Pointer box, Pointer temp);
		boolean contains_temporal_tstzspan(Pointer temp, Pointer s);
		boolean contains_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean contains_tnumber_numspan(Pointer temp, Pointer s);
		boolean contains_tnumber_tbox(Pointer temp, Pointer box);
		boolean contains_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean contains_tpoint_stbox(Pointer temp, Pointer box);
		boolean contains_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean contains_tstzspan_temporal(Pointer s, Pointer temp);
		boolean overlaps_numspan_tnumber(Pointer s, Pointer temp);
		boolean overlaps_stbox_tpoint(Pointer box, Pointer temp);
		boolean overlaps_tbox_tnumber(Pointer box, Pointer temp);
		boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean overlaps_temporal_tstzspan(Pointer temp, Pointer s);
		boolean overlaps_tnumber_numspan(Pointer temp, Pointer s);
		boolean overlaps_tnumber_tbox(Pointer temp, Pointer box);
		boolean overlaps_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overlaps_tpoint_stbox(Pointer temp, Pointer box);
		boolean overlaps_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overlaps_tstzspan_temporal(Pointer s, Pointer temp);
		boolean same_numspan_tnumber(Pointer s, Pointer temp);
		boolean same_stbox_tpoint(Pointer box, Pointer temp);
		boolean same_tbox_tnumber(Pointer box, Pointer temp);
		boolean same_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean same_temporal_tstzspan(Pointer temp, Pointer s);
		boolean same_tnumber_numspan(Pointer temp, Pointer s);
		boolean same_tnumber_tbox(Pointer temp, Pointer box);
		boolean same_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean same_tpoint_stbox(Pointer temp, Pointer box);
		boolean same_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean same_tstzspan_temporal(Pointer s, Pointer temp);
		boolean above_stbox_tpoint(Pointer box, Pointer temp);
		boolean above_tpoint_stbox(Pointer temp, Pointer box);
		boolean above_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean after_stbox_tpoint(Pointer box, Pointer temp);
		boolean after_tbox_tnumber(Pointer box, Pointer temp);
		boolean after_temporal_tstzspan(Pointer temp, Pointer s);
		boolean after_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean after_tnumber_tbox(Pointer temp, Pointer box);
		boolean after_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean after_tpoint_stbox(Pointer temp, Pointer box);
		boolean after_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean after_tstzspan_temporal(Pointer s, Pointer temp);
		boolean back_stbox_tpoint(Pointer box, Pointer temp);
		boolean back_tpoint_stbox(Pointer temp, Pointer box);
		boolean back_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean before_stbox_tpoint(Pointer box, Pointer temp);
		boolean before_tbox_tnumber(Pointer box, Pointer temp);
		boolean before_temporal_tstzspan(Pointer temp, Pointer s);
		boolean before_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean before_tnumber_tbox(Pointer temp, Pointer box);
		boolean before_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean before_tpoint_stbox(Pointer temp, Pointer box);
		boolean before_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean before_tstzspan_temporal(Pointer s, Pointer temp);
		boolean below_stbox_tpoint(Pointer box, Pointer temp);
		boolean below_tpoint_stbox(Pointer temp, Pointer box);
		boolean below_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean front_stbox_tpoint(Pointer box, Pointer temp);
		boolean front_tpoint_stbox(Pointer temp, Pointer box);
		boolean front_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean left_stbox_tpoint(Pointer box, Pointer temp);
		boolean left_tbox_tnumber(Pointer box, Pointer temp);
		boolean left_numspan_tnumber(Pointer s, Pointer temp);
		boolean left_tnumber_numspan(Pointer temp, Pointer s);
		boolean left_tnumber_tbox(Pointer temp, Pointer box);
		boolean left_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean left_tpoint_stbox(Pointer temp, Pointer box);
		boolean left_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overabove_stbox_tpoint(Pointer box, Pointer temp);
		boolean overabove_tpoint_stbox(Pointer temp, Pointer box);
		boolean overabove_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overafter_stbox_tpoint(Pointer box, Pointer temp);
		boolean overafter_tbox_tnumber(Pointer box, Pointer temp);
		boolean overafter_temporal_tstzspan(Pointer temp, Pointer s);
		boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean overafter_tnumber_tbox(Pointer temp, Pointer box);
		boolean overafter_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overafter_tpoint_stbox(Pointer temp, Pointer box);
		boolean overafter_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overafter_tstzspan_temporal(Pointer s, Pointer temp);
		boolean overback_stbox_tpoint(Pointer box, Pointer temp);
		boolean overback_tpoint_stbox(Pointer temp, Pointer box);
		boolean overback_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overbefore_stbox_tpoint(Pointer box, Pointer temp);
		boolean overbefore_tbox_tnumber(Pointer box, Pointer temp);
		boolean overbefore_temporal_tstzspan(Pointer temp, Pointer s);
		boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2);
		boolean overbefore_tnumber_tbox(Pointer temp, Pointer box);
		boolean overbefore_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overbefore_tpoint_stbox(Pointer temp, Pointer box);
		boolean overbefore_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overbefore_tstzspan_temporal(Pointer s, Pointer temp);
		boolean overbelow_stbox_tpoint(Pointer box, Pointer temp);
		boolean overbelow_tpoint_stbox(Pointer temp, Pointer box);
		boolean overbelow_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overfront_stbox_tpoint(Pointer box, Pointer temp);
		boolean overfront_tpoint_stbox(Pointer temp, Pointer box);
		boolean overfront_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overleft_numspan_tnumber(Pointer s, Pointer temp);
		boolean overleft_stbox_tpoint(Pointer box, Pointer temp);
		boolean overleft_tbox_tnumber(Pointer box, Pointer temp);
		boolean overleft_tnumber_numspan(Pointer temp, Pointer s);
		boolean overleft_tnumber_tbox(Pointer temp, Pointer box);
		boolean overleft_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overleft_tpoint_stbox(Pointer temp, Pointer box);
		boolean overleft_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean overright_numspan_tnumber(Pointer s, Pointer temp);
		boolean overright_stbox_tpoint(Pointer box, Pointer temp);
		boolean overright_tbox_tnumber(Pointer box, Pointer temp);
		boolean overright_tnumber_numspan(Pointer temp, Pointer s);
		boolean overright_tnumber_tbox(Pointer temp, Pointer box);
		boolean overright_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean overright_tpoint_stbox(Pointer temp, Pointer box);
		boolean overright_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean right_numspan_tnumber(Pointer s, Pointer temp);
		boolean right_stbox_tpoint(Pointer box, Pointer temp);
		boolean right_tbox_tnumber(Pointer box, Pointer temp);
		boolean right_tnumber_numspan(Pointer temp, Pointer s);
		boolean right_tnumber_tbox(Pointer temp, Pointer box);
		boolean right_tnumber_tnumber(Pointer temp1, Pointer temp2);
		boolean right_tpoint_stbox(Pointer temp, Pointer box);
		boolean right_tpoint_tpoint(Pointer temp1, Pointer temp2);
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
		Pointer mult_float_tfloat(double d, Pointer tnumber);
		Pointer mult_int_tint(int i, Pointer tnumber);
		Pointer mult_tfloat_float(Pointer tnumber, double d);
		Pointer mult_tint_int(Pointer tnumber, int i);
		Pointer mult_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);
		Pointer sub_float_tfloat(double d, Pointer tnumber);
		Pointer sub_int_tint(int i, Pointer tnumber);
		Pointer sub_tfloat_float(Pointer tnumber, double d);
		Pointer sub_tint_int(Pointer tnumber, int i);
		Pointer sub_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);
		Pointer tfloat_derivative(Pointer temp);
		Pointer tnumber_abs(Pointer temp);
		Pointer tnumber_angular_difference(Pointer temp);
		Pointer tnumber_delta_value(Pointer temp);
		Pointer textcat_text_ttext(Pointer txt, Pointer temp);
		Pointer textcat_ttext_text(Pointer temp, Pointer txt);
		Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2);
		Pointer ttext_upper(Pointer temp);
		Pointer ttext_lower(Pointer temp);
		Pointer ttext_initcap(Pointer temp);
		Pointer distance_tfloat_float(Pointer temp, double d);
		Pointer distance_tint_int(Pointer temp, int i);
		Pointer distance_tnumber_tnumber(Pointer temp1, Pointer temp2);
		Pointer distance_tpoint_point(Pointer temp, Pointer gs);
		Pointer distance_tpoint_tpoint(Pointer temp1, Pointer temp2);
		double nad_stbox_geo(Pointer box, Pointer gs);
		double nad_stbox_stbox(Pointer box1, Pointer box2);
		int nad_tint_int(Pointer temp, int i);
		int nad_tint_tbox(Pointer temp, Pointer box);
		int nad_tint_tint(Pointer temp1, Pointer temp2);
		int nad_tboxint_tboxint(Pointer box1, Pointer box2);
		double nad_tfloat_float(Pointer temp, double d);
		double nad_tfloat_tfloat(Pointer temp1, Pointer temp2);
		double nad_tfloat_tbox(Pointer temp, Pointer box);
		double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2);
		double nad_tpoint_geo(Pointer temp, Pointer gs);
		double nad_tpoint_stbox(Pointer temp, Pointer box);
		double nad_tpoint_tpoint(Pointer temp1, Pointer temp2);
		Pointer nai_tpoint_geo(Pointer temp, Pointer gs);
		Pointer nai_tpoint_tpoint(Pointer temp1, Pointer temp2);
		Pointer shortestline_tpoint_geo(Pointer temp, Pointer gs);
		Pointer shortestline_tpoint_tpoint(Pointer temp1, Pointer temp2);
		boolean bearing_point_point(Pointer gs1, Pointer gs2, Pointer result);
		Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert);
		Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2);
		Pointer tpoint_angular_difference(Pointer temp);
		Pointer tpoint_azimuth(Pointer temp);
		Pointer tpoint_convex_hull(Pointer temp);
		Pointer tpoint_cumulative_length(Pointer temp);
		boolean tpoint_direction(Pointer temp, Pointer result);
		Pointer tpoint_get_x(Pointer temp);
		Pointer tpoint_get_y(Pointer temp);
		Pointer tpoint_get_z(Pointer temp);
		boolean tpoint_is_simple(Pointer temp);
		double tpoint_length(Pointer temp);
		Pointer tpoint_speed(Pointer temp);
		int tpoint_srid(Pointer temp);
		Pointer tpoint_stboxes(Pointer temp, Pointer count);
		Pointer tpoint_trajectory(Pointer temp);
		Pointer tpoint_twcentroid(Pointer temp);
		Pointer geo_expand_space(Pointer gs, double d);
		Pointer geomeas_to_tpoint(Pointer gs);
		Pointer tgeogpoint_to_tgeompoint(Pointer temp);
		Pointer tgeompoint_to_tgeogpoint(Pointer temp);
		boolean tpoint_AsMVTGeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count);
		Pointer tpoint_expand_space(Pointer temp, double d);
		Pointer tpoint_make_simple(Pointer temp, Pointer count);
		Pointer tpoint_set_srid(Pointer temp, int srid);
		boolean tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize, Pointer result);
		int acontains_geo_tpoint(Pointer gs, Pointer temp);
		int adisjoint_tpoint_geo(Pointer temp, Pointer gs);
		int adisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int adwithin_tpoint_geo(Pointer temp, Pointer gs, double dist);
		int adwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist);
		int aintersects_tpoint_geo(Pointer temp, Pointer gs);
		int aintersects_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int atouches_tpoint_geo(Pointer temp, Pointer gs);
		int econtains_geo_tpoint(Pointer gs, Pointer temp);
		int edisjoint_tpoint_geo(Pointer temp, Pointer gs);
		int edisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int edwithin_tpoint_geo(Pointer temp, Pointer gs, double dist);
		int edwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist);
		int eintersects_tpoint_geo(Pointer temp, Pointer gs);
		int eintersects_tpoint_tpoint(Pointer temp1, Pointer temp2);
		int etouches_tpoint_geo(Pointer temp, Pointer gs);
		Pointer tcontains_geo_tpoint(Pointer gs, Pointer temp, boolean restr, boolean atvalue);
		Pointer tdisjoint_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);
		Pointer tdisjoint_tpoint_tpoint (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue);
		Pointer tdwithin_tpoint_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue);
		Pointer tdwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue);
		Pointer tintersects_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);
		Pointer tintersects_tpoint_tpoint (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue);
		Pointer ttouches_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);
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
		Pointer tpoint_extent_transfn(Pointer box, Pointer temp);
		Pointer tpoint_tcentroid_finalfn(Pointer state);
		Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp);
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
		double float_bucket(double value, double size, double origin);
		Pointer floatspan_bucket_list(Pointer bounds, double size, double origin, Pointer count);
		int int_bucket(int value, int size, int origin);
		Pointer intspan_bucket_list(Pointer bounds, int size, int origin, Pointer count);
		Pointer stbox_tile(Pointer point, long t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean hast);
		Pointer stbox_tile_list(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean border_inc, Pointer count);
		Pointer temporal_time_split(Pointer temp, Pointer duration, long torigin, Pointer time_buckets, Pointer count);
		Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer value_buckets, Pointer count);
		Pointer tfloat_value_time_split(Pointer temp, double size, Pointer duration, double vorigin, long torigin, Pointer value_buckets, Pointer time_buckets, Pointer count);
		Pointer tfloatbox_tile(double value, long t, double vsize, Pointer duration, double vorigin, long torigin);
		Pointer tfloatbox_tile_list(Pointer box, double xsize, Pointer duration, double xorigin, long torigin, Pointer count);
		long timestamptz_bucket(long timestamp, Pointer duration, long origin);
		Pointer tint_value_split(Pointer temp, int size, int origin, Pointer value_buckets, Pointer count);
		Pointer tint_value_time_split(Pointer temp, int size, Pointer duration, int vorigin, long torigin, Pointer value_buckets, Pointer time_buckets, Pointer count);
		Pointer tintbox_tile(int value, long t, int vsize, Pointer duration, int vorigin, long torigin);
		Pointer tintbox_tile_list(Pointer box, int xsize, Pointer duration, int xorigin, long torigin, Pointer count);
		Pointer tpoint_space_split(Pointer temp, float xsize, float ysize, float zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_buckets, Pointer count);
		Pointer tpoint_space_time_split(Pointer temp, float xsize, float ysize, float zsize, Pointer duration, Pointer sorigin, long torigin, boolean bitmatrix, boolean border_inc, Pointer space_buckets, Pointer time_buckets, Pointer count);
		Pointer tstzspan_bucket_list(Pointer bounds, Pointer duration, long origin, Pointer count);
	}

	/** Delegates MeosLibrary calls to the appropriate sub-interface proxy. */
	@Deprecated
	private static final class MeosLibraryDelegate implements MeosLibrary {
		@Override public int geo_get_srid(Pointer g) { return _meos_a.geo_get_srid(g); }
		@Override public void meos_error(int errlevel, int errcode, String format, Pointer args) { _meos_a.meos_error(errlevel, errcode, format, args); }
		@Override public int meos_errno() { return _meos_a.meos_errno(); }
		@Override public int meos_errno_set(int err) { return _meos_a.meos_errno_set(err); }
		@Override public int meos_errno_restore(int err) { return _meos_a.meos_errno_restore(err); }
		@Override public int meos_errno_reset() { return _meos_a.meos_errno_reset(); }
		@Override public void meos_initialize_timezone(String name) { _meos_a.meos_initialize_timezone(name); }
		@Override public void meos_initialize_error_handler(error_handler_fn err_handler) { _meos_a.meos_initialize_error_handler(err_handler); }
		@Override public void meos_finalize_timezone() { _meos_a.meos_finalize_timezone(); }
		@Override public boolean meos_set_datestyle(String newval, Pointer extra) { return _meos_a.meos_set_datestyle(newval, extra); }
		@Override public boolean meos_set_intervalstyle(String newval, int extra) { return _meos_a.meos_set_intervalstyle(newval, extra); }
		@Override public String meos_get_datestyle() { return _meos_a.meos_get_datestyle(); }
		@Override public String meos_get_intervalstyle() { return _meos_a.meos_get_intervalstyle(); }
		@Override public void meos_initialize(String tz_str, error_handler_fn err_handler) { _meos_a.meos_initialize(tz_str, err_handler); }
		@Override public void meos_finalize() { _meos_a.meos_finalize(); }
		@Override public int add_date_int(int d, int days) { return _meos_a.add_date_int(d, days); }
		@Override public Pointer add_interval_interval(Pointer interv1, Pointer interv2) { return _meos_a.add_interval_interval(interv1, interv2); }
		@Override public long add_timestamptz_interval(long t, Pointer interv) { return _meos_a.add_timestamptz_interval(t, interv); }
		@Override public boolean bool_in(String str) { return _meos_a.bool_in(str); }
		@Override public String bool_out(boolean b) { return _meos_a.bool_out(b); }
		@Override public Pointer cstring2text(String str) { return _meos_a.cstring2text(str); }
		@Override public long date_to_timestamptz(int d) { return _meos_a.date_to_timestamptz(d); }
		@Override public Pointer minus_date_date(int d1, int d2) { return _meos_a.minus_date_date(d1, d2); }
		@Override public int minus_date_int(int d, int days) { return _meos_a.minus_date_int(d, days); }
		@Override public long minus_timestamptz_interval(long t, Pointer interv) { return _meos_a.minus_timestamptz_interval(t, interv); }
		@Override public Pointer minus_timestamptz_timestamptz(long t1, long t2) { return _meos_a.minus_timestamptz_timestamptz(t1, t2); }
		@Override public Pointer mult_interval_double(Pointer interv, double factor) { return _meos_a.mult_interval_double(interv, factor); }
		@Override public int pg_date_in(String str) { return _meos_a.pg_date_in(str); }
		@Override public String pg_date_out(int d) { return _meos_a.pg_date_out(d); }
		@Override public int pg_interval_cmp(Pointer interv1, Pointer interv2) { return _meos_a.pg_interval_cmp(interv1, interv2); }
		@Override public Pointer pg_interval_in(String str, int typmod) { return _meos_a.pg_interval_in(str, typmod); }
		@Override public Pointer pg_interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs) { return _meos_a.pg_interval_make(years, months, weeks, days, hours, mins, secs); }
		@Override public String pg_interval_out(Pointer interv) { return _meos_a.pg_interval_out(interv); }
		@Override public long pg_time_in(String str, int typmod) { return _meos_a.pg_time_in(str, typmod); }
		@Override public String pg_time_out(long t) { return _meos_a.pg_time_out(t); }
		@Override public long pg_timestamp_in(String str, int typmod) { return _meos_a.pg_timestamp_in(str, typmod); }
		@Override public String pg_timestamp_out(long t) { return _meos_a.pg_timestamp_out(t); }
		@Override public long pg_timestamptz_in(String str, int typmod) { return _meos_a.pg_timestamptz_in(str, typmod); }
		@Override public String pg_timestamptz_out(long t) { return _meos_a.pg_timestamptz_out(t); }
		@Override public String text2cstring(Pointer txt) { return _meos_a.text2cstring(txt); }
		@Override public int text_cmp(Pointer txt1, Pointer txt2) { return _meos_a.text_cmp(txt1, txt2); }
		@Override public Pointer text_copy(Pointer txt) { return _meos_a.text_copy(txt); }
		@Override public Pointer text_initcap(Pointer txt) { return _meos_a.text_initcap(txt); }
		@Override public Pointer text_lower(Pointer txt) { return _meos_a.text_lower(txt); }
		@Override public String text_out(Pointer txt) { return _meos_a.text_out(txt); }
		@Override public Pointer text_upper(Pointer txt) { return _meos_a.text_upper(txt); }
		@Override public Pointer textcat_text_text(Pointer txt1, Pointer txt2) { return _meos_a.textcat_text_text(txt1, txt2); }
		@Override public int timestamptz_to_date(long t) { return _meos_a.timestamptz_to_date(t); }
		@Override public Pointer geo_as_ewkb(Pointer gs, String endian) { return _meos_a.geo_as_ewkb(gs, endian); }
		@Override public String geo_as_ewkt(Pointer gs, int precision) { return _meos_a.geo_as_ewkt(gs, precision); }
		@Override public String geo_as_geojson(Pointer gs, int option, int precision, String srs) { return _meos_a.geo_as_geojson(gs, option, precision, srs); }
		@Override public String geo_as_hexewkb(Pointer gs, String endian) { return _meos_a.geo_as_hexewkb(gs, endian); }
		@Override public String geo_as_text(Pointer gs, int precision) { return _meos_a.geo_as_text(gs, precision); }
		@Override public Pointer geo_from_ewkb(Pointer bytea_wkb, int srid) { return _meos_a.geo_from_ewkb(bytea_wkb, srid); }
		@Override public Pointer geo_from_geojson(String geojson) { return _meos_a.geo_from_geojson(geojson); }
		@Override public String geo_out(Pointer gs) { return _meos_a.geo_out(gs); }
		@Override public boolean geo_same(Pointer gs1, Pointer gs2) { return _meos_a.geo_same(gs1, gs2); }
		@Override public Pointer geography_from_hexewkb(String wkt) { return _meos_a.geography_from_hexewkb(wkt); }
		@Override public Pointer geography_from_text(String wkt, int srid) { return _meos_a.geography_from_text(wkt, srid); }
		@Override public Pointer geometry_from_hexewkb(String wkt) { return _meos_a.geometry_from_hexewkb(wkt); }
		@Override public Pointer geometry_from_text(String wkt, int srid) { return _meos_a.geometry_from_text(wkt, srid); }
		@Override public Pointer pgis_geography_in(String str, int typmod) { return _meos_a.pgis_geography_in(str, typmod); }
		@Override public Pointer pgis_geometry_in(String str, int typmod) { return _meos_a.pgis_geometry_in(str, typmod); }
		@Override public Pointer bigintset_in(String str) { return _meos_a.bigintset_in(str); }
		@Override public String bigintset_out(Pointer set) { return _meos_a.bigintset_out(set); }
		@Override public Pointer bigintspan_in(String str) { return _meos_a.bigintspan_in(str); }
		@Override public String bigintspan_out(Pointer s) { return _meos_a.bigintspan_out(s); }
		@Override public Pointer bigintspanset_in(String str) { return _meos_a.bigintspanset_in(str); }
		@Override public String bigintspanset_out(Pointer ss) { return _meos_a.bigintspanset_out(ss); }
		@Override public Pointer dateset_in(String str) { return _meos_a.dateset_in(str); }
		@Override public String dateset_out(Pointer s) { return _meos_a.dateset_out(s); }
		@Override public Pointer datespan_in(String str) { return _meos_a.datespan_in(str); }
		@Override public String datespan_out(Pointer s) { return _meos_a.datespan_out(s); }
		@Override public Pointer datespanset_in(String str) { return _meos_a.datespanset_in(str); }
		@Override public String datespanset_out(Pointer ss) { return _meos_a.datespanset_out(ss); }
		@Override public Pointer floatset_in(String str) { return _meos_a.floatset_in(str); }
		@Override public String floatset_out(Pointer set, int maxdd) { return _meos_a.floatset_out(set, maxdd); }
		@Override public Pointer floatspan_in(String str) { return _meos_a.floatspan_in(str); }
		@Override public String floatspan_out(Pointer s, int maxdd) { return _meos_a.floatspan_out(s, maxdd); }
		@Override public Pointer floatspanset_in(String str) { return _meos_a.floatspanset_in(str); }
		@Override public String floatspanset_out(Pointer ss, int maxdd) { return _meos_a.floatspanset_out(ss, maxdd); }
		@Override public Pointer geogset_in(String str) { return _meos_a.geogset_in(str); }
		@Override public Pointer geomset_in(String str) { return _meos_a.geomset_in(str); }
		@Override public String geoset_as_ewkt(Pointer set, int maxdd) { return _meos_a.geoset_as_ewkt(set, maxdd); }
		@Override public String geoset_as_text(Pointer set, int maxdd) { return _meos_a.geoset_as_text(set, maxdd); }
		@Override public String geoset_out(Pointer set, int maxdd) { return _meos_a.geoset_out(set, maxdd); }
		@Override public Pointer intset_in(String str) { return _meos_a.intset_in(str); }
		@Override public String intset_out(Pointer set) { return _meos_a.intset_out(set); }
		@Override public Pointer intspan_in(String str) { return _meos_a.intspan_in(str); }
		@Override public String intspan_out(Pointer s) { return _meos_a.intspan_out(s); }
		@Override public Pointer intspanset_in(String str) { return _meos_a.intspanset_in(str); }
		@Override public String intspanset_out(Pointer ss) { return _meos_a.intspanset_out(ss); }
		@Override public String set_as_hexwkb(Pointer s, byte variant, Pointer size_out) { return _meos_a.set_as_hexwkb(s, variant, size_out); }
		@Override public Pointer set_as_wkb(Pointer s, byte variant, Pointer size_out) { return _meos_a.set_as_wkb(s, variant, size_out); }
		@Override public Pointer set_from_hexwkb(String hexwkb) { return _meos_a.set_from_hexwkb(hexwkb); }
		@Override public Pointer set_from_wkb(Pointer wkb, long size) { return _meos_a.set_from_wkb(wkb, size); }
		@Override public String span_as_hexwkb(Pointer s, byte variant, Pointer size_out) { return _meos_a.span_as_hexwkb(s, variant, size_out); }
		@Override public Pointer span_as_wkb(Pointer s, byte variant, Pointer size_out) { return _meos_a.span_as_wkb(s, variant, size_out); }
		@Override public Pointer span_from_hexwkb(String hexwkb) { return _meos_a.span_from_hexwkb(hexwkb); }
		@Override public Pointer span_from_wkb(Pointer wkb, long size) { return _meos_a.span_from_wkb(wkb, size); }
		@Override public String spanset_as_hexwkb(Pointer ss, byte variant, Pointer size_out) { return _meos_a.spanset_as_hexwkb(ss, variant, size_out); }
		@Override public Pointer spanset_as_wkb(Pointer ss, byte variant, Pointer size_out) { return _meos_a.spanset_as_wkb(ss, variant, size_out); }
		@Override public Pointer spanset_from_hexwkb(String hexwkb) { return _meos_a.spanset_from_hexwkb(hexwkb); }
		@Override public Pointer spanset_from_wkb(Pointer wkb, long size) { return _meos_a.spanset_from_wkb(wkb, size); }
		@Override public Pointer textset_in(String str) { return _meos_a.textset_in(str); }
		@Override public String textset_out(Pointer set) { return _meos_a.textset_out(set); }
		@Override public Pointer tstzset_in(String str) { return _meos_a.tstzset_in(str); }
		@Override public String tstzset_out(Pointer set) { return _meos_a.tstzset_out(set); }
		@Override public Pointer tstzspan_in(String str) { return _meos_a.tstzspan_in(str); }
		@Override public String tstzspan_out(Pointer s) { return _meos_a.tstzspan_out(s); }
		@Override public Pointer tstzspanset_in(String str) { return _meos_a.tstzspanset_in(str); }
		@Override public String tstzspanset_out(Pointer ss) { return _meos_a.tstzspanset_out(ss); }
		@Override public Pointer bigintset_make(Pointer values, int count) { return _meos_a.bigintset_make(values, count); }
		@Override public Pointer bigintspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc) { return _meos_a.bigintspan_make(lower, upper, lower_inc, upper_inc); }
		@Override public Pointer dateset_make(Pointer values, int count) { return _meos_a.dateset_make(values, count); }
		@Override public Pointer datespan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) { return _meos_a.datespan_make(lower, upper, lower_inc, upper_inc); }
		@Override public Pointer floatset_make(Pointer values, int count) { return _meos_a.floatset_make(values, count); }
		@Override public Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc) { return _meos_a.floatspan_make(lower, upper, lower_inc, upper_inc); }
		@Override public Pointer geoset_make(Pointer values, int count) { return _meos_a.geoset_make(values, count); }
		@Override public Pointer intset_make(Pointer values, int count) { return _meos_a.intset_make(values, count); }
		@Override public Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) { return _meos_a.intspan_make(lower, upper, lower_inc, upper_inc); }
		@Override public Pointer set_copy(Pointer s) { return _meos_a.set_copy(s); }
		@Override public Pointer span_copy(Pointer s) { return _meos_a.span_copy(s); }
		@Override public Pointer spanset_copy(Pointer ss) { return _meos_a.spanset_copy(ss); }
		@Override public Pointer spanset_make(Pointer spans, int count, boolean normalize, boolean order) { return _meos_a.spanset_make(spans, count, normalize, order); }
		@Override public Pointer textset_make(Pointer values, int count) { return _meos_a.textset_make(values, count); }
		@Override public Pointer tstzset_make(Pointer values, int count) { return _meos_a.tstzset_make(values, count); }
		@Override public Pointer tstzspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc) { return _meos_a.tstzspan_make(lower, upper, lower_inc, upper_inc); }
		@Override public Pointer bigint_to_set(long i) { return _meos_a.bigint_to_set(i); }
		@Override public Pointer bigint_to_span(int i) { return _meos_a.bigint_to_span(i); }
		@Override public Pointer bigint_to_spanset(int i) { return _meos_a.bigint_to_spanset(i); }
		@Override public Pointer date_to_set(int d) { return _meos_a.date_to_set(d); }
		@Override public Pointer date_to_span(int d) { return _meos_a.date_to_span(d); }
		@Override public Pointer date_to_spanset(int d) { return _meos_a.date_to_spanset(d); }
		@Override public Pointer dateset_to_tstzset(Pointer s) { return _meos_a.dateset_to_tstzset(s); }
		@Override public Pointer datespan_to_tstzspan(Pointer s) { return _meos_a.datespan_to_tstzspan(s); }
		@Override public Pointer datespanset_to_tstzspanset(Pointer ss) { return _meos_a.datespanset_to_tstzspanset(ss); }
		@Override public Pointer float_to_set(double d) { return _meos_a.float_to_set(d); }
		@Override public Pointer float_to_span(double d) { return _meos_a.float_to_span(d); }
		@Override public Pointer float_to_spanset(double d) { return _meos_a.float_to_spanset(d); }
		@Override public Pointer floatset_to_intset(Pointer s) { return _meos_a.floatset_to_intset(s); }
		@Override public Pointer floatspan_to_intspan(Pointer s) { return _meos_a.floatspan_to_intspan(s); }
		@Override public Pointer floatspanset_to_intspanset(Pointer ss) { return _meos_a.floatspanset_to_intspanset(ss); }
		@Override public Pointer geo_to_set(Pointer gs) { return _meos_a.geo_to_set(gs); }
		@Override public Pointer int_to_set(int i) { return _meos_a.int_to_set(i); }
		@Override public Pointer int_to_span(int i) { return _meos_a.int_to_span(i); }
		@Override public Pointer int_to_spanset(int i) { return _meos_a.int_to_spanset(i); }
		@Override public Pointer intset_to_floatset(Pointer s) { return _meos_a.intset_to_floatset(s); }
		@Override public Pointer intspan_to_floatspan(Pointer s) { return _meos_a.intspan_to_floatspan(s); }
		@Override public Pointer intspanset_to_floatspanset(Pointer ss) { return _meos_a.intspanset_to_floatspanset(ss); }
		@Override public Pointer set_to_spanset(Pointer s) { return _meos_a.set_to_spanset(s); }
		@Override public Pointer span_to_spanset(Pointer s) { return _meos_a.span_to_spanset(s); }
		@Override public Pointer text_to_set(Pointer txt) { return _meos_a.text_to_set(txt); }
		@Override public Pointer timestamptz_to_set(long t) { return _meos_a.timestamptz_to_set(t); }
		@Override public Pointer timestamptz_to_span(long t) { return _meos_a.timestamptz_to_span(t); }
		@Override public Pointer timestamptz_to_spanset(long t) { return _meos_a.timestamptz_to_spanset(t); }
		@Override public Pointer tstzset_to_dateset(Pointer s) { return _meos_a.tstzset_to_dateset(s); }
		@Override public Pointer tstzspan_to_datespan(Pointer s) { return _meos_a.tstzspan_to_datespan(s); }
		@Override public Pointer tstzspanset_to_datespanset(Pointer ss) { return _meos_a.tstzspanset_to_datespanset(ss); }
		@Override public long bigintset_end_value(Pointer s) { return _meos_a.bigintset_end_value(s); }
		@Override public long bigintset_start_value(Pointer s) { return _meos_a.bigintset_start_value(s); }
		@Override public boolean bigintset_value_n(Pointer s, int n, Pointer result) { return _meos_a.bigintset_value_n(s, n, result); }
		@Override public Pointer bigintset_values(Pointer s) { return _meos_a.bigintset_values(s); }
		@Override public long bigintspan_lower(Pointer s) { return _meos_a.bigintspan_lower(s); }
		@Override public long bigintspan_upper(Pointer s) { return _meos_a.bigintspan_upper(s); }
		@Override public long bigintspan_width(Pointer s) { return _meos_a.bigintspan_width(s); }
		@Override public long bigintspanset_lower(Pointer ss) { return _meos_a.bigintspanset_lower(ss); }
		@Override public long bigintspanset_upper(Pointer ss) { return _meos_a.bigintspanset_upper(ss); }
		@Override public long bigintspanset_width(Pointer ss, boolean boundspan) { return _meos_a.bigintspanset_width(ss, boundspan); }
		@Override public int dateset_end_value(Pointer s) { return _meos_a.dateset_end_value(s); }
		@Override public int dateset_start_value(Pointer s) { return _meos_a.dateset_start_value(s); }
		@Override public boolean dateset_value_n(Pointer s, int n, Pointer result) { return _meos_a.dateset_value_n(s, n, result); }
		@Override public Pointer dateset_values(Pointer s) { return _meos_a.dateset_values(s); }
		@Override public Pointer datespan_duration(Pointer s) { return _meos_a.datespan_duration(s); }
		@Override public int datespan_lower(Pointer s) { return _meos_a.datespan_lower(s); }
		@Override public int datespan_upper(Pointer s) { return _meos_a.datespan_upper(s); }
		@Override public boolean datespanset_date_n(Pointer ss, int n, Pointer result) { return _meos_a.datespanset_date_n(ss, n, result); }
		@Override public Pointer datespanset_dates(Pointer ss) { return _meos_a.datespanset_dates(ss); }
		@Override public Pointer datespanset_duration(Pointer ss, boolean boundspan) { return _meos_a.datespanset_duration(ss, boundspan); }
		@Override public int datespanset_end_date(Pointer ss) { return _meos_a.datespanset_end_date(ss); }
		@Override public int datespanset_num_dates(Pointer ss) { return _meos_a.datespanset_num_dates(ss); }
		@Override public int datespanset_start_date(Pointer ss) { return _meos_a.datespanset_start_date(ss); }
		@Override public double floatset_end_value(Pointer s) { return _meos_a.floatset_end_value(s); }
		@Override public double floatset_start_value(Pointer s) { return _meos_a.floatset_start_value(s); }
		@Override public boolean floatset_value_n(Pointer s, int n, Pointer result) { return _meos_a.floatset_value_n(s, n, result); }
		@Override public Pointer floatset_values(Pointer s) { return _meos_a.floatset_values(s); }
		@Override public double floatspan_lower(Pointer s) { return _meos_a.floatspan_lower(s); }
		@Override public double floatspan_upper(Pointer s) { return _meos_a.floatspan_upper(s); }
		@Override public double floatspan_width(Pointer s) { return _meos_a.floatspan_width(s); }
		@Override public double floatspanset_lower(Pointer ss) { return _meos_a.floatspanset_lower(ss); }
		@Override public double floatspanset_upper(Pointer ss) { return _meos_a.floatspanset_upper(ss); }
		@Override public double floatspanset_width(Pointer ss, boolean boundspan) { return _meos_a.floatspanset_width(ss, boundspan); }
		@Override public Pointer geoset_end_value(Pointer s) { return _meos_a.geoset_end_value(s); }
		@Override public int geoset_srid(Pointer s) { return _meos_a.geoset_srid(s); }
		@Override public Pointer geoset_start_value(Pointer s) { return _meos_a.geoset_start_value(s); }
		@Override public boolean geoset_value_n(Pointer s, int n, Pointer result) { return _meos_a.geoset_value_n(s, n, result); }
		@Override public Pointer geoset_values(Pointer s) { return _meos_a.geoset_values(s); }
		@Override public int intset_end_value(Pointer s) { return _meos_a.intset_end_value(s); }
		@Override public int intset_start_value(Pointer s) { return _meos_a.intset_start_value(s); }
		@Override public boolean intset_value_n(Pointer s, int n, Pointer result) { return _meos_a.intset_value_n(s, n, result); }
		@Override public Pointer intset_values(Pointer s) { return _meos_a.intset_values(s); }
		@Override public int intspan_lower(Pointer s) { return _meos_a.intspan_lower(s); }
		@Override public int intspan_upper(Pointer s) { return _meos_a.intspan_upper(s); }
		@Override public int intspan_width(Pointer s) { return _meos_a.intspan_width(s); }
		@Override public int intspanset_lower(Pointer ss) { return _meos_a.intspanset_lower(ss); }
		@Override public int intspanset_upper(Pointer ss) { return _meos_a.intspanset_upper(ss); }
		@Override public int intspanset_width(Pointer ss, boolean boundspan) { return _meos_a.intspanset_width(ss, boundspan); }
		@Override public int set_hash(Pointer s) { return _meos_a.set_hash(s); }
		@Override public long set_hash_extended(Pointer s, long seed) { return _meos_a.set_hash_extended(s, seed); }
		@Override public int set_num_values(Pointer s) { return _meos_a.set_num_values(s); }
		@Override public Pointer set_to_span(Pointer s) { return _meos_a.set_to_span(s); }
		@Override public int span_hash(Pointer s) { return _meos_a.span_hash(s); }
		@Override public long span_hash_extended(Pointer s, long seed) { return _meos_a.span_hash_extended(s, seed); }
		@Override public boolean span_lower_inc(Pointer s) { return _meos_a.span_lower_inc(s); }
		@Override public boolean span_upper_inc(Pointer s) { return _meos_a.span_upper_inc(s); }
		@Override public Pointer spanset_end_span(Pointer ss) { return _meos_a.spanset_end_span(ss); }
		@Override public int spanset_hash(Pointer ss) { return _meos_a.spanset_hash(ss); }
		@Override public long spanset_hash_extended(Pointer ss, long seed) { return _meos_a.spanset_hash_extended(ss, seed); }
		@Override public boolean spanset_lower_inc(Pointer ss) { return _meos_a.spanset_lower_inc(ss); }
		@Override public int spanset_num_spans(Pointer ss) { return _meos_a.spanset_num_spans(ss); }
		@Override public Pointer spanset_span(Pointer ss) { return _meos_a.spanset_span(ss); }
		@Override public Pointer spanset_span_n(Pointer ss, int i) { return _meos_a.spanset_span_n(ss, i); }
		@Override public Pointer spanset_spans(Pointer ss) { return _meos_a.spanset_spans(ss); }
		@Override public Pointer spanset_start_span(Pointer ss) { return _meos_a.spanset_start_span(ss); }
		@Override public boolean spanset_upper_inc(Pointer ss) { return _meos_a.spanset_upper_inc(ss); }
		@Override public Pointer textset_end_value(Pointer s) { return _meos_a.textset_end_value(s); }
		@Override public Pointer textset_start_value(Pointer s) { return _meos_a.textset_start_value(s); }
		@Override public boolean textset_value_n(Pointer s, int n, Pointer result) { return _meos_a.textset_value_n(s, n, result); }
		@Override public Pointer textset_values(Pointer s) { return _meos_a.textset_values(s); }
		@Override public long tstzset_end_value(Pointer s) { return _meos_a.tstzset_end_value(s); }
		@Override public long tstzset_start_value(Pointer s) { return _meos_a.tstzset_start_value(s); }
		@Override public boolean tstzset_value_n(Pointer s, int n, Pointer result) { return _meos_a.tstzset_value_n(s, n, result); }
		@Override public Pointer tstzset_values(Pointer s) { return _meos_a.tstzset_values(s); }
		@Override public Pointer tstzspan_duration(Pointer s) { return _meos_a.tstzspan_duration(s); }
		@Override public long tstzspan_lower(Pointer s) { return _meos_a.tstzspan_lower(s); }
		@Override public long tstzspan_upper(Pointer s) { return _meos_a.tstzspan_upper(s); }
		@Override public Pointer tstzspanset_duration(Pointer ss, boolean boundspan) { return _meos_a.tstzspanset_duration(ss, boundspan); }
		@Override public long tstzspanset_end_timestamptz(Pointer ss) { return _meos_a.tstzspanset_end_timestamptz(ss); }
		@Override public long tstzspanset_lower(Pointer ss) { return _meos_a.tstzspanset_lower(ss); }
		@Override public int tstzspanset_num_timestamps(Pointer ss) { return _meos_a.tstzspanset_num_timestamps(ss); }
		@Override public long tstzspanset_start_timestamptz(Pointer ss) { return _meos_a.tstzspanset_start_timestamptz(ss); }
		@Override public boolean tstzspanset_timestamptz_n(Pointer ss, int n, Pointer result) { return _meos_a.tstzspanset_timestamptz_n(ss, n, result); }
		@Override public Pointer tstzspanset_timestamps(Pointer ss) { return _meos_a.tstzspanset_timestamps(ss); }
		@Override public long tstzspanset_upper(Pointer ss) { return _meos_a.tstzspanset_upper(ss); }
		@Override public Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) { return _meos_a.bigintset_shift_scale(s, shift, width, hasshift, haswidth); }
		@Override public Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) { return _meos_a.bigintspan_shift_scale(s, shift, width, hasshift, haswidth); }
		@Override public Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth) { return _meos_a.bigintspanset_shift_scale(ss, shift, width, hasshift, haswidth); }
		@Override public Pointer dateset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) { return _meos_a.dateset_shift_scale(s, shift, width, hasshift, haswidth); }
		@Override public Pointer datespan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) { return _meos_a.datespan_shift_scale(s, shift, width, hasshift, haswidth); }
		@Override public Pointer datespanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) { return _meos_a.datespanset_shift_scale(ss, shift, width, hasshift, haswidth); }
		@Override public Pointer floatset_ceil(Pointer s) { return _meos_a.floatset_ceil(s); }
		@Override public Pointer floatset_floor(Pointer s) { return _meos_a.floatset_floor(s); }
		@Override public Pointer floatset_degrees(Pointer s, boolean normalize) { return _meos_a.floatset_degrees(s, normalize); }
		@Override public Pointer floatset_radians(Pointer s) { return _meos_a.floatset_radians(s); }
		@Override public Pointer floatset_round(Pointer s, int maxdd) { return _meos_a.floatset_round(s, maxdd); }
		@Override public Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) { return _meos_a.floatset_shift_scale(s, shift, width, hasshift, haswidth); }
		@Override public Pointer floatspan_ceil(Pointer s) { return _meos_a.floatspan_ceil(s); }
		@Override public Pointer floatspan_floor(Pointer s) { return _meos_a.floatspan_floor(s); }
		@Override public Pointer floatspan_round(Pointer s, int maxdd) { return _meos_a.floatspan_round(s, maxdd); }
		@Override public Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) { return _meos_a.floatspan_shift_scale(s, shift, width, hasshift, haswidth); }
		@Override public Pointer floatspanset_ceil(Pointer ss) { return _meos_a.floatspanset_ceil(ss); }
		@Override public Pointer floatspanset_floor(Pointer ss) { return _meos_a.floatspanset_floor(ss); }
		@Override public Pointer floatspanset_round(Pointer ss, int maxdd) { return _meos_a.floatspanset_round(ss, maxdd); }
		@Override public Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth) { return _meos_a.floatspanset_shift_scale(ss, shift, width, hasshift, haswidth); }
		@Override public Pointer geoset_round(Pointer s, int maxdd) { return _meos_a.geoset_round(s, maxdd); }
		@Override public Pointer geoset_set_srid(Pointer s, int srid) { return _meos_a.geoset_set_srid(s, srid); }
		@Override public Pointer geoset_transform(Pointer s, int srid) { return _meos_a.geoset_transform(s, srid); }
		@Override public Pointer geoset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward) { return _meos_a.geoset_transform_pipeline(s, pipelinestr, srid, is_forward); }
		@Override public Pointer point_transform(Pointer gs, int srid) { return _meos_a.point_transform(gs, srid); }
		@Override public Pointer point_transform_pipeline(Pointer gs, String pipelinestr, int srid, boolean is_forward) { return _meos_a.point_transform_pipeline(gs, pipelinestr, srid, is_forward); }
		@Override public Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) { return _meos_a.intset_shift_scale(s, shift, width, hasshift, haswidth); }
		@Override public Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) { return _meos_a.intspan_shift_scale(s, shift, width, hasshift, haswidth); }
		@Override public Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) { return _meos_a.intspanset_shift_scale(ss, shift, width, hasshift, haswidth); }
		@Override public Pointer textset_initcap(Pointer s) { return _meos_a.textset_initcap(s); }
		@Override public Pointer textset_lower(Pointer s) { return _meos_a.textset_lower(s); }
		@Override public Pointer textset_upper(Pointer s) { return _meos_a.textset_upper(s); }
		@Override public Pointer textcat_textset_text(Pointer s, Pointer txt) { return _meos_a.textcat_textset_text(s, txt); }
		@Override public Pointer textcat_text_textset(Pointer txt, Pointer s) { return _meos_a.textcat_text_textset(txt, s); }
		@Override public long timestamptz_tprecision(long t, Pointer duration, long torigin) { return _meos_a.timestamptz_tprecision(t, duration, torigin); }
		@Override public Pointer tstzset_shift_scale(Pointer s, Pointer shift, Pointer duration) { return _meos_a.tstzset_shift_scale(s, shift, duration); }
		@Override public Pointer tstzset_tprecision(Pointer s, Pointer duration, long torigin) { return _meos_a.tstzset_tprecision(s, duration, torigin); }
		@Override public Pointer tstzspan_shift_scale(Pointer s, Pointer shift, Pointer duration) { return _meos_a.tstzspan_shift_scale(s, shift, duration); }
		@Override public Pointer tstzspan_tprecision(Pointer s, Pointer duration, long torigin) { return _meos_a.tstzspan_tprecision(s, duration, torigin); }
		@Override public Pointer tstzspanset_shift_scale(Pointer ss, Pointer shift, Pointer duration) { return _meos_a.tstzspanset_shift_scale(ss, shift, duration); }
		@Override public Pointer tstzspanset_tprecision(Pointer ss, Pointer duration, long torigin) { return _meos_a.tstzspanset_tprecision(ss, duration, torigin); }
		@Override public int set_cmp(Pointer s1, Pointer s2) { return _meos_a.set_cmp(s1, s2); }
		@Override public boolean set_eq(Pointer s1, Pointer s2) { return _meos_a.set_eq(s1, s2); }
		@Override public boolean set_ge(Pointer s1, Pointer s2) { return _meos_a.set_ge(s1, s2); }
		@Override public boolean set_gt(Pointer s1, Pointer s2) { return _meos_a.set_gt(s1, s2); }
		@Override public boolean set_le(Pointer s1, Pointer s2) { return _meos_a.set_le(s1, s2); }
		@Override public boolean set_lt(Pointer s1, Pointer s2) { return _meos_a.set_lt(s1, s2); }
		@Override public boolean set_ne(Pointer s1, Pointer s2) { return _meos_a.set_ne(s1, s2); }
		@Override public int span_cmp(Pointer s1, Pointer s2) { return _meos_a.span_cmp(s1, s2); }
		@Override public boolean span_eq(Pointer s1, Pointer s2) { return _meos_a.span_eq(s1, s2); }
		@Override public boolean span_ge(Pointer s1, Pointer s2) { return _meos_a.span_ge(s1, s2); }
		@Override public boolean span_gt(Pointer s1, Pointer s2) { return _meos_a.span_gt(s1, s2); }
		@Override public boolean span_le(Pointer s1, Pointer s2) { return _meos_a.span_le(s1, s2); }
		@Override public boolean span_lt(Pointer s1, Pointer s2) { return _meos_a.span_lt(s1, s2); }
		@Override public boolean span_ne(Pointer s1, Pointer s2) { return _meos_a.span_ne(s1, s2); }
		@Override public int spanset_cmp(Pointer ss1, Pointer ss2) { return _meos_a.spanset_cmp(ss1, ss2); }
		@Override public boolean spanset_eq(Pointer ss1, Pointer ss2) { return _meos_a.spanset_eq(ss1, ss2); }
		@Override public boolean spanset_ge(Pointer ss1, Pointer ss2) { return _meos_a.spanset_ge(ss1, ss2); }
		@Override public boolean spanset_gt(Pointer ss1, Pointer ss2) { return _meos_a.spanset_gt(ss1, ss2); }
		@Override public boolean spanset_le(Pointer ss1, Pointer ss2) { return _meos_a.spanset_le(ss1, ss2); }
		@Override public boolean spanset_lt(Pointer ss1, Pointer ss2) { return _meos_a.spanset_lt(ss1, ss2); }
		@Override public boolean spanset_ne(Pointer ss1, Pointer ss2) { return _meos_a.spanset_ne(ss1, ss2); }
		@Override public boolean adjacent_span_bigint(Pointer s, long i) { return _meos_a.adjacent_span_bigint(s, i); }
		@Override public boolean adjacent_span_date(Pointer s, int d) { return _meos_a.adjacent_span_date(s, d); }
		@Override public boolean adjacent_span_float(Pointer s, double d) { return _meos_a.adjacent_span_float(s, d); }
		@Override public boolean adjacent_span_int(Pointer s, int i) { return _meos_a.adjacent_span_int(s, i); }
		@Override public boolean adjacent_span_span(Pointer s1, Pointer s2) { return _meos_a.adjacent_span_span(s1, s2); }
		@Override public boolean adjacent_span_spanset(Pointer s, Pointer ss) { return _meos_a.adjacent_span_spanset(s, ss); }
		@Override public boolean adjacent_span_timestamptz(Pointer s, long t) { return _meos_a.adjacent_span_timestamptz(s, t); }
		@Override public boolean adjacent_spanset_bigint(Pointer ss, long i) { return _meos_a.adjacent_spanset_bigint(ss, i); }
		@Override public boolean adjacent_spanset_date(Pointer ss, int d) { return _meos_a.adjacent_spanset_date(ss, d); }
		@Override public boolean adjacent_spanset_float(Pointer ss, double d) { return _meos_a.adjacent_spanset_float(ss, d); }
		@Override public boolean adjacent_spanset_int(Pointer ss, int i) { return _meos_a.adjacent_spanset_int(ss, i); }
		@Override public boolean adjacent_spanset_timestamptz(Pointer ss, long t) { return _meos_a.adjacent_spanset_timestamptz(ss, t); }
		@Override public boolean adjacent_spanset_span(Pointer ss, Pointer s) { return _meos_a.adjacent_spanset_span(ss, s); }
		@Override public boolean adjacent_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_a.adjacent_spanset_spanset(ss1, ss2); }
		@Override public boolean contained_bigint_set(long i, Pointer s) { return _meos_a.contained_bigint_set(i, s); }
		@Override public boolean contained_bigint_span(long i, Pointer s) { return _meos_a.contained_bigint_span(i, s); }
		@Override public boolean contained_bigint_spanset(long i, Pointer ss) { return _meos_a.contained_bigint_spanset(i, ss); }
		@Override public boolean contained_date_set(int d, Pointer s) { return _meos_a.contained_date_set(d, s); }
		@Override public boolean contained_date_span(int d, Pointer s) { return _meos_a.contained_date_span(d, s); }
		@Override public boolean contained_date_spanset(int d, Pointer ss) { return _meos_a.contained_date_spanset(d, ss); }
		@Override public boolean contained_float_set(double d, Pointer s) { return _meos_a.contained_float_set(d, s); }
		@Override public boolean contained_float_span(double d, Pointer s) { return _meos_a.contained_float_span(d, s); }
		@Override public boolean contained_float_spanset(double d, Pointer ss) { return _meos_a.contained_float_spanset(d, ss); }
		@Override public boolean contained_geo_set(Pointer gs, Pointer s) { return _meos_a.contained_geo_set(gs, s); }
		@Override public boolean contained_int_set(int i, Pointer s) { return _meos_a.contained_int_set(i, s); }
		@Override public boolean contained_int_span(int i, Pointer s) { return _meos_a.contained_int_span(i, s); }
		@Override public boolean contained_int_spanset(int i, Pointer ss) { return _meos_a.contained_int_spanset(i, ss); }
		@Override public boolean contained_set_set(Pointer s1, Pointer s2) { return _meos_a.contained_set_set(s1, s2); }
		@Override public boolean contained_span_span(Pointer s1, Pointer s2) { return _meos_a.contained_span_span(s1, s2); }
		@Override public boolean contained_span_spanset(Pointer s, Pointer ss) { return _meos_a.contained_span_spanset(s, ss); }
		@Override public boolean contained_spanset_span(Pointer ss, Pointer s) { return _meos_a.contained_spanset_span(ss, s); }
		@Override public boolean contained_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_a.contained_spanset_spanset(ss1, ss2); }
		@Override public boolean contained_text_set(Pointer txt, Pointer s) { return _meos_a.contained_text_set(txt, s); }
		@Override public boolean contained_timestamptz_set(long t, Pointer s) { return _meos_a.contained_timestamptz_set(t, s); }
		@Override public boolean contained_timestamptz_span(long t, Pointer s) { return _meos_a.contained_timestamptz_span(t, s); }
		@Override public boolean contained_timestamptz_spanset(long t, Pointer ss) { return _meos_a.contained_timestamptz_spanset(t, ss); }
		@Override public boolean contains_set_bigint(Pointer s, long i) { return _meos_a.contains_set_bigint(s, i); }
		@Override public boolean contains_set_date(Pointer s, int d) { return _meos_a.contains_set_date(s, d); }
		@Override public boolean contains_set_float(Pointer s, double d) { return _meos_a.contains_set_float(s, d); }
		@Override public boolean contains_set_geo(Pointer s, Pointer gs) { return _meos_a.contains_set_geo(s, gs); }
		@Override public boolean contains_set_int(Pointer s, int i) { return _meos_a.contains_set_int(s, i); }
		@Override public boolean contains_set_set(Pointer s1, Pointer s2) { return _meos_a.contains_set_set(s1, s2); }
		@Override public boolean contains_set_text(Pointer s, Pointer t) { return _meos_a.contains_set_text(s, t); }
		@Override public boolean contains_set_timestamptz(Pointer s, long t) { return _meos_a.contains_set_timestamptz(s, t); }
		@Override public boolean contains_span_bigint(Pointer s, long i) { return _meos_a.contains_span_bigint(s, i); }
		@Override public boolean contains_span_date(Pointer s, int d) { return _meos_a.contains_span_date(s, d); }
		@Override public boolean contains_span_float(Pointer s, double d) { return _meos_a.contains_span_float(s, d); }
		@Override public boolean contains_span_int(Pointer s, int i) { return _meos_a.contains_span_int(s, i); }
		@Override public boolean contains_span_span(Pointer s1, Pointer s2) { return _meos_a.contains_span_span(s1, s2); }
		@Override public boolean contains_span_spanset(Pointer s, Pointer ss) { return _meos_a.contains_span_spanset(s, ss); }
		@Override public boolean contains_span_timestamptz(Pointer s, long t) { return _meos_a.contains_span_timestamptz(s, t); }
		@Override public boolean contains_spanset_bigint(Pointer ss, long i) { return _meos_a.contains_spanset_bigint(ss, i); }
		@Override public boolean contains_spanset_date(Pointer ss, int d) { return _meos_a.contains_spanset_date(ss, d); }
		@Override public boolean contains_spanset_float(Pointer ss, double d) { return _meos_a.contains_spanset_float(ss, d); }
		@Override public boolean contains_spanset_int(Pointer ss, int i) { return _meos_a.contains_spanset_int(ss, i); }
		@Override public boolean contains_spanset_span(Pointer ss, Pointer s) { return _meos_a.contains_spanset_span(ss, s); }
		@Override public boolean contains_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_a.contains_spanset_spanset(ss1, ss2); }
		@Override public boolean contains_spanset_timestamptz(Pointer ss, long t) { return _meos_a.contains_spanset_timestamptz(ss, t); }
		@Override public boolean overlaps_set_set(Pointer s1, Pointer s2) { return _meos_a.overlaps_set_set(s1, s2); }
		@Override public boolean overlaps_span_span(Pointer s1, Pointer s2) { return _meos_a.overlaps_span_span(s1, s2); }
		@Override public boolean overlaps_span_spanset(Pointer s, Pointer ss) { return _meos_a.overlaps_span_spanset(s, ss); }
		@Override public boolean overlaps_spanset_span(Pointer ss, Pointer s) { return _meos_a.overlaps_spanset_span(ss, s); }
		@Override public boolean overlaps_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_a.overlaps_spanset_spanset(ss1, ss2); }
		@Override public boolean after_date_set(int d, Pointer s) { return _meos_a.after_date_set(d, s); }
		@Override public boolean after_date_span(int d, Pointer s) { return _meos_a.after_date_span(d, s); }
		@Override public boolean after_date_spanset(int d, Pointer ss) { return _meos_a.after_date_spanset(d, ss); }
		@Override public boolean after_set_date(Pointer s, int d) { return _meos_b.after_set_date(s, d); }
		@Override public boolean after_set_timestamptz(Pointer s, long t) { return _meos_b.after_set_timestamptz(s, t); }
		@Override public boolean after_span_date(Pointer s, int d) { return _meos_b.after_span_date(s, d); }
		@Override public boolean after_span_timestamptz(Pointer s, long t) { return _meos_b.after_span_timestamptz(s, t); }
		@Override public boolean after_spanset_date(Pointer ss, int d) { return _meos_b.after_spanset_date(ss, d); }
		@Override public boolean after_spanset_timestamptz(Pointer ss, long t) { return _meos_b.after_spanset_timestamptz(ss, t); }
		@Override public boolean after_timestamptz_set(long t, Pointer s) { return _meos_b.after_timestamptz_set(t, s); }
		@Override public boolean after_timestamptz_span(long t, Pointer s) { return _meos_b.after_timestamptz_span(t, s); }
		@Override public boolean after_timestamptz_spanset(long t, Pointer ss) { return _meos_b.after_timestamptz_spanset(t, ss); }
		@Override public boolean before_date_set(int d, Pointer s) { return _meos_b.before_date_set(d, s); }
		@Override public boolean before_date_span(int d, Pointer s) { return _meos_b.before_date_span(d, s); }
		@Override public boolean before_date_spanset(int d, Pointer ss) { return _meos_b.before_date_spanset(d, ss); }
		@Override public boolean before_set_date(Pointer s, int d) { return _meos_b.before_set_date(s, d); }
		@Override public boolean before_set_timestamptz(Pointer s, long t) { return _meos_b.before_set_timestamptz(s, t); }
		@Override public boolean before_span_date(Pointer s, int d) { return _meos_b.before_span_date(s, d); }
		@Override public boolean before_span_timestamptz(Pointer s, long t) { return _meos_b.before_span_timestamptz(s, t); }
		@Override public boolean before_spanset_date(Pointer ss, int d) { return _meos_b.before_spanset_date(ss, d); }
		@Override public boolean before_spanset_timestamptz(Pointer ss, long t) { return _meos_b.before_spanset_timestamptz(ss, t); }
		@Override public boolean before_timestamptz_set(long t, Pointer s) { return _meos_b.before_timestamptz_set(t, s); }
		@Override public boolean before_timestamptz_span(long t, Pointer s) { return _meos_b.before_timestamptz_span(t, s); }
		@Override public boolean before_timestamptz_spanset(long t, Pointer ss) { return _meos_b.before_timestamptz_spanset(t, ss); }
		@Override public boolean left_bigint_set(long i, Pointer s) { return _meos_b.left_bigint_set(i, s); }
		@Override public boolean left_bigint_span(long i, Pointer s) { return _meos_b.left_bigint_span(i, s); }
		@Override public boolean left_bigint_spanset(long i, Pointer ss) { return _meos_b.left_bigint_spanset(i, ss); }
		@Override public boolean left_float_set(double d, Pointer s) { return _meos_b.left_float_set(d, s); }
		@Override public boolean left_float_span(double d, Pointer s) { return _meos_b.left_float_span(d, s); }
		@Override public boolean left_float_spanset(double d, Pointer ss) { return _meos_b.left_float_spanset(d, ss); }
		@Override public boolean left_int_set(int i, Pointer s) { return _meos_b.left_int_set(i, s); }
		@Override public boolean left_int_span(int i, Pointer s) { return _meos_b.left_int_span(i, s); }
		@Override public boolean left_int_spanset(int i, Pointer ss) { return _meos_b.left_int_spanset(i, ss); }
		@Override public boolean left_set_bigint(Pointer s, long i) { return _meos_b.left_set_bigint(s, i); }
		@Override public boolean left_set_float(Pointer s, double d) { return _meos_b.left_set_float(s, d); }
		@Override public boolean left_set_int(Pointer s, int i) { return _meos_b.left_set_int(s, i); }
		@Override public boolean left_set_set(Pointer s1, Pointer s2) { return _meos_b.left_set_set(s1, s2); }
		@Override public boolean left_set_text(Pointer s, Pointer txt) { return _meos_b.left_set_text(s, txt); }
		@Override public boolean left_span_bigint(Pointer s, long i) { return _meos_b.left_span_bigint(s, i); }
		@Override public boolean left_span_float(Pointer s, double d) { return _meos_b.left_span_float(s, d); }
		@Override public boolean left_span_int(Pointer s, int i) { return _meos_b.left_span_int(s, i); }
		@Override public boolean left_span_span(Pointer s1, Pointer s2) { return _meos_b.left_span_span(s1, s2); }
		@Override public boolean left_span_spanset(Pointer s, Pointer ss) { return _meos_b.left_span_spanset(s, ss); }
		@Override public boolean left_spanset_bigint(Pointer ss, long i) { return _meos_b.left_spanset_bigint(ss, i); }
		@Override public boolean left_spanset_float(Pointer ss, double d) { return _meos_b.left_spanset_float(ss, d); }
		@Override public boolean left_spanset_int(Pointer ss, int i) { return _meos_b.left_spanset_int(ss, i); }
		@Override public boolean left_spanset_span(Pointer ss, Pointer s) { return _meos_b.left_spanset_span(ss, s); }
		@Override public boolean left_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_b.left_spanset_spanset(ss1, ss2); }
		@Override public boolean left_text_set(Pointer txt, Pointer s) { return _meos_b.left_text_set(txt, s); }
		@Override public boolean overafter_date_set(int d, Pointer s) { return _meos_b.overafter_date_set(d, s); }
		@Override public boolean overafter_date_span(int d, Pointer s) { return _meos_b.overafter_date_span(d, s); }
		@Override public boolean overafter_date_spanset(int d, Pointer ss) { return _meos_b.overafter_date_spanset(d, ss); }
		@Override public boolean overafter_set_date(Pointer s, int d) { return _meos_b.overafter_set_date(s, d); }
		@Override public boolean overafter_set_timestamptz(Pointer s, long t) { return _meos_b.overafter_set_timestamptz(s, t); }
		@Override public boolean overafter_span_date(Pointer s, int d) { return _meos_b.overafter_span_date(s, d); }
		@Override public boolean overafter_span_timestamptz(Pointer s, long t) { return _meos_b.overafter_span_timestamptz(s, t); }
		@Override public boolean overafter_spanset_date(Pointer ss, int d) { return _meos_b.overafter_spanset_date(ss, d); }
		@Override public boolean overafter_spanset_timestamptz(Pointer ss, long t) { return _meos_b.overafter_spanset_timestamptz(ss, t); }
		@Override public boolean overafter_timestamptz_set(long t, Pointer s) { return _meos_b.overafter_timestamptz_set(t, s); }
		@Override public boolean overafter_timestamptz_span(long t, Pointer s) { return _meos_b.overafter_timestamptz_span(t, s); }
		@Override public boolean overafter_timestamptz_spanset(long t, Pointer ss) { return _meos_b.overafter_timestamptz_spanset(t, ss); }
		@Override public boolean overbefore_date_set(int d, Pointer s) { return _meos_b.overbefore_date_set(d, s); }
		@Override public boolean overbefore_date_span(int d, Pointer s) { return _meos_b.overbefore_date_span(d, s); }
		@Override public boolean overbefore_date_spanset(int d, Pointer ss) { return _meos_b.overbefore_date_spanset(d, ss); }
		@Override public boolean overbefore_set_date(Pointer s, int d) { return _meos_b.overbefore_set_date(s, d); }
		@Override public boolean overbefore_set_timestamptz(Pointer s, long t) { return _meos_b.overbefore_set_timestamptz(s, t); }
		@Override public boolean overbefore_span_date(Pointer s, int d) { return _meos_b.overbefore_span_date(s, d); }
		@Override public boolean overbefore_span_timestamptz(Pointer s, long t) { return _meos_b.overbefore_span_timestamptz(s, t); }
		@Override public boolean overbefore_spanset_date(Pointer ss, int d) { return _meos_b.overbefore_spanset_date(ss, d); }
		@Override public boolean overbefore_spanset_timestamptz(Pointer ss, long t) { return _meos_b.overbefore_spanset_timestamptz(ss, t); }
		@Override public boolean overbefore_timestamptz_set(long t, Pointer s) { return _meos_b.overbefore_timestamptz_set(t, s); }
		@Override public boolean overbefore_timestamptz_span(long t, Pointer s) { return _meos_b.overbefore_timestamptz_span(t, s); }
		@Override public boolean overbefore_timestamptz_spanset(long t, Pointer ss) { return _meos_b.overbefore_timestamptz_spanset(t, ss); }
		@Override public boolean overleft_bigint_set(long i, Pointer s) { return _meos_b.overleft_bigint_set(i, s); }
		@Override public boolean overleft_bigint_span(long i, Pointer s) { return _meos_b.overleft_bigint_span(i, s); }
		@Override public boolean overleft_bigint_spanset(long i, Pointer ss) { return _meos_b.overleft_bigint_spanset(i, ss); }
		@Override public boolean overleft_float_set(double d, Pointer s) { return _meos_b.overleft_float_set(d, s); }
		@Override public boolean overleft_float_span(double d, Pointer s) { return _meos_b.overleft_float_span(d, s); }
		@Override public boolean overleft_float_spanset(double d, Pointer ss) { return _meos_b.overleft_float_spanset(d, ss); }
		@Override public boolean overleft_int_set(int i, Pointer s) { return _meos_b.overleft_int_set(i, s); }
		@Override public boolean overleft_int_span(int i, Pointer s) { return _meos_b.overleft_int_span(i, s); }
		@Override public boolean overleft_int_spanset(int i, Pointer ss) { return _meos_b.overleft_int_spanset(i, ss); }
		@Override public boolean overleft_set_bigint(Pointer s, long i) { return _meos_b.overleft_set_bigint(s, i); }
		@Override public boolean overleft_set_float(Pointer s, double d) { return _meos_b.overleft_set_float(s, d); }
		@Override public boolean overleft_set_int(Pointer s, int i) { return _meos_b.overleft_set_int(s, i); }
		@Override public boolean overleft_set_set(Pointer s1, Pointer s2) { return _meos_b.overleft_set_set(s1, s2); }
		@Override public boolean overleft_set_text(Pointer s, Pointer txt) { return _meos_b.overleft_set_text(s, txt); }
		@Override public boolean overleft_span_bigint(Pointer s, long i) { return _meos_b.overleft_span_bigint(s, i); }
		@Override public boolean overleft_span_float(Pointer s, double d) { return _meos_b.overleft_span_float(s, d); }
		@Override public boolean overleft_span_int(Pointer s, int i) { return _meos_b.overleft_span_int(s, i); }
		@Override public boolean overleft_span_span(Pointer s1, Pointer s2) { return _meos_b.overleft_span_span(s1, s2); }
		@Override public boolean overleft_span_spanset(Pointer s, Pointer ss) { return _meos_b.overleft_span_spanset(s, ss); }
		@Override public boolean overleft_spanset_bigint(Pointer ss, long i) { return _meos_b.overleft_spanset_bigint(ss, i); }
		@Override public boolean overleft_spanset_float(Pointer ss, double d) { return _meos_b.overleft_spanset_float(ss, d); }
		@Override public boolean overleft_spanset_int(Pointer ss, int i) { return _meos_b.overleft_spanset_int(ss, i); }
		@Override public boolean overleft_spanset_span(Pointer ss, Pointer s) { return _meos_b.overleft_spanset_span(ss, s); }
		@Override public boolean overleft_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_b.overleft_spanset_spanset(ss1, ss2); }
		@Override public boolean overleft_text_set(Pointer txt, Pointer s) { return _meos_b.overleft_text_set(txt, s); }
		@Override public boolean overright_bigint_set(long i, Pointer s) { return _meos_b.overright_bigint_set(i, s); }
		@Override public boolean overright_bigint_span(long i, Pointer s) { return _meos_b.overright_bigint_span(i, s); }
		@Override public boolean overright_bigint_spanset(long i, Pointer ss) { return _meos_b.overright_bigint_spanset(i, ss); }
		@Override public boolean overright_float_set(double d, Pointer s) { return _meos_b.overright_float_set(d, s); }
		@Override public boolean overright_float_span(double d, Pointer s) { return _meos_b.overright_float_span(d, s); }
		@Override public boolean overright_float_spanset(double d, Pointer ss) { return _meos_b.overright_float_spanset(d, ss); }
		@Override public boolean overright_int_set(int i, Pointer s) { return _meos_b.overright_int_set(i, s); }
		@Override public boolean overright_int_span(int i, Pointer s) { return _meos_b.overright_int_span(i, s); }
		@Override public boolean overright_int_spanset(int i, Pointer ss) { return _meos_b.overright_int_spanset(i, ss); }
		@Override public boolean overright_set_bigint(Pointer s, long i) { return _meos_b.overright_set_bigint(s, i); }
		@Override public boolean overright_set_float(Pointer s, double d) { return _meos_b.overright_set_float(s, d); }
		@Override public boolean overright_set_int(Pointer s, int i) { return _meos_b.overright_set_int(s, i); }
		@Override public boolean overright_set_set(Pointer s1, Pointer s2) { return _meos_b.overright_set_set(s1, s2); }
		@Override public boolean overright_set_text(Pointer s, Pointer txt) { return _meos_b.overright_set_text(s, txt); }
		@Override public boolean overright_span_bigint(Pointer s, long i) { return _meos_b.overright_span_bigint(s, i); }
		@Override public boolean overright_span_float(Pointer s, double d) { return _meos_b.overright_span_float(s, d); }
		@Override public boolean overright_span_int(Pointer s, int i) { return _meos_b.overright_span_int(s, i); }
		@Override public boolean overright_span_span(Pointer s1, Pointer s2) { return _meos_b.overright_span_span(s1, s2); }
		@Override public boolean overright_span_spanset(Pointer s, Pointer ss) { return _meos_b.overright_span_spanset(s, ss); }
		@Override public boolean overright_spanset_bigint(Pointer ss, long i) { return _meos_b.overright_spanset_bigint(ss, i); }
		@Override public boolean overright_spanset_float(Pointer ss, double d) { return _meos_b.overright_spanset_float(ss, d); }
		@Override public boolean overright_spanset_int(Pointer ss, int i) { return _meos_b.overright_spanset_int(ss, i); }
		@Override public boolean overright_spanset_span(Pointer ss, Pointer s) { return _meos_b.overright_spanset_span(ss, s); }
		@Override public boolean overright_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_b.overright_spanset_spanset(ss1, ss2); }
		@Override public boolean overright_text_set(Pointer txt, Pointer s) { return _meos_b.overright_text_set(txt, s); }
		@Override public boolean right_bigint_set(long i, Pointer s) { return _meos_b.right_bigint_set(i, s); }
		@Override public boolean right_bigint_span(long i, Pointer s) { return _meos_b.right_bigint_span(i, s); }
		@Override public boolean right_bigint_spanset(long i, Pointer ss) { return _meos_b.right_bigint_spanset(i, ss); }
		@Override public boolean right_float_set(double d, Pointer s) { return _meos_b.right_float_set(d, s); }
		@Override public boolean right_float_span(double d, Pointer s) { return _meos_b.right_float_span(d, s); }
		@Override public boolean right_float_spanset(double d, Pointer ss) { return _meos_b.right_float_spanset(d, ss); }
		@Override public boolean right_int_set(int i, Pointer s) { return _meos_b.right_int_set(i, s); }
		@Override public boolean right_int_span(int i, Pointer s) { return _meos_b.right_int_span(i, s); }
		@Override public boolean right_int_spanset(int i, Pointer ss) { return _meos_b.right_int_spanset(i, ss); }
		@Override public boolean right_set_bigint(Pointer s, long i) { return _meos_b.right_set_bigint(s, i); }
		@Override public boolean right_set_float(Pointer s, double d) { return _meos_b.right_set_float(s, d); }
		@Override public boolean right_set_int(Pointer s, int i) { return _meos_b.right_set_int(s, i); }
		@Override public boolean right_set_set(Pointer s1, Pointer s2) { return _meos_b.right_set_set(s1, s2); }
		@Override public boolean right_set_text(Pointer s, Pointer txt) { return _meos_b.right_set_text(s, txt); }
		@Override public boolean right_span_bigint(Pointer s, long i) { return _meos_b.right_span_bigint(s, i); }
		@Override public boolean right_span_float(Pointer s, double d) { return _meos_b.right_span_float(s, d); }
		@Override public boolean right_span_int(Pointer s, int i) { return _meos_b.right_span_int(s, i); }
		@Override public boolean right_span_span(Pointer s1, Pointer s2) { return _meos_b.right_span_span(s1, s2); }
		@Override public boolean right_span_spanset(Pointer s, Pointer ss) { return _meos_b.right_span_spanset(s, ss); }
		@Override public boolean right_spanset_bigint(Pointer ss, long i) { return _meos_b.right_spanset_bigint(ss, i); }
		@Override public boolean right_spanset_float(Pointer ss, double d) { return _meos_b.right_spanset_float(ss, d); }
		@Override public boolean right_spanset_int(Pointer ss, int i) { return _meos_b.right_spanset_int(ss, i); }
		@Override public boolean right_spanset_span(Pointer ss, Pointer s) { return _meos_b.right_spanset_span(ss, s); }
		@Override public boolean right_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_b.right_spanset_spanset(ss1, ss2); }
		@Override public boolean right_text_set(Pointer txt, Pointer s) { return _meos_b.right_text_set(txt, s); }
		@Override public Pointer intersection_bigint_set(long i, Pointer s) { return _meos_b.intersection_bigint_set(i, s); }
		@Override public Pointer intersection_date_set(int d, Pointer s) { return _meos_b.intersection_date_set(d, s); }
		@Override public Pointer intersection_float_set(double d, Pointer s) { return _meos_b.intersection_float_set(d, s); }
		@Override public Pointer intersection_geo_set(Pointer gs, Pointer s) { return _meos_b.intersection_geo_set(gs, s); }
		@Override public Pointer intersection_int_set(int i, Pointer s) { return _meos_b.intersection_int_set(i, s); }
		@Override public Pointer intersection_set_bigint(Pointer s, long i) { return _meos_b.intersection_set_bigint(s, i); }
		@Override public Pointer intersection_set_date(Pointer s, int d) { return _meos_b.intersection_set_date(s, d); }
		@Override public Pointer intersection_set_float(Pointer s, double d) { return _meos_b.intersection_set_float(s, d); }
		@Override public Pointer intersection_set_geo(Pointer s, Pointer gs) { return _meos_b.intersection_set_geo(s, gs); }
		@Override public Pointer intersection_set_int(Pointer s, int i) { return _meos_b.intersection_set_int(s, i); }
		@Override public Pointer intersection_set_set(Pointer s1, Pointer s2) { return _meos_b.intersection_set_set(s1, s2); }
		@Override public Pointer intersection_set_text(Pointer s, Pointer txt) { return _meos_b.intersection_set_text(s, txt); }
		@Override public Pointer intersection_set_timestamptz(Pointer s, long t) { return _meos_b.intersection_set_timestamptz(s, t); }
		@Override public Pointer intersection_span_bigint(Pointer s, long i) { return _meos_b.intersection_span_bigint(s, i); }
		@Override public Pointer intersection_span_date(Pointer s, int d) { return _meos_b.intersection_span_date(s, d); }
		@Override public Pointer intersection_span_float(Pointer s, double d) { return _meos_b.intersection_span_float(s, d); }
		@Override public Pointer intersection_span_int(Pointer s, int i) { return _meos_b.intersection_span_int(s, i); }
		@Override public Pointer intersection_span_span(Pointer s1, Pointer s2) { return _meos_b.intersection_span_span(s1, s2); }
		@Override public Pointer intersection_span_spanset(Pointer s, Pointer ss) { return _meos_b.intersection_span_spanset(s, ss); }
		@Override public Pointer intersection_span_timestamptz(Pointer s, long t) { return _meos_b.intersection_span_timestamptz(s, t); }
		@Override public Pointer intersection_spanset_bigint(Pointer ss, long i) { return _meos_b.intersection_spanset_bigint(ss, i); }
		@Override public Pointer intersection_spanset_date(Pointer ss, int d) { return _meos_b.intersection_spanset_date(ss, d); }
		@Override public Pointer intersection_spanset_float(Pointer ss, double d) { return _meos_b.intersection_spanset_float(ss, d); }
		@Override public Pointer intersection_spanset_int(Pointer ss, int i) { return _meos_b.intersection_spanset_int(ss, i); }
		@Override public Pointer intersection_spanset_span(Pointer ss, Pointer s) { return _meos_b.intersection_spanset_span(ss, s); }
		@Override public Pointer intersection_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_b.intersection_spanset_spanset(ss1, ss2); }
		@Override public Pointer intersection_spanset_timestamptz(Pointer ss, long t) { return _meos_b.intersection_spanset_timestamptz(ss, t); }
		@Override public Pointer intersection_text_set(Pointer txt, Pointer s) { return _meos_b.intersection_text_set(txt, s); }
		@Override public Pointer intersection_timestamptz_set(long t, Pointer s) { return _meos_b.intersection_timestamptz_set(t, s); }
		@Override public Pointer minus_bigint_set(long i, Pointer s) { return _meos_b.minus_bigint_set(i, s); }
		@Override public Pointer minus_bigint_span(long i, Pointer s) { return _meos_b.minus_bigint_span(i, s); }
		@Override public Pointer minus_bigint_spanset(long i, Pointer ss) { return _meos_b.minus_bigint_spanset(i, ss); }
		@Override public Pointer minus_date_set(int d, Pointer s) { return _meos_b.minus_date_set(d, s); }
		@Override public Pointer minus_date_span(int d, Pointer s) { return _meos_b.minus_date_span(d, s); }
		@Override public Pointer minus_date_spanset(int d, Pointer ss) { return _meos_b.minus_date_spanset(d, ss); }
		@Override public Pointer minus_float_set(double d, Pointer s) { return _meos_b.minus_float_set(d, s); }
		@Override public Pointer minus_float_span(double d, Pointer s) { return _meos_b.minus_float_span(d, s); }
		@Override public Pointer minus_float_spanset(double d, Pointer ss) { return _meos_b.minus_float_spanset(d, ss); }
		@Override public Pointer minus_geo_set(Pointer gs, Pointer s) { return _meos_b.minus_geo_set(gs, s); }
		@Override public Pointer minus_int_set(int i, Pointer s) { return _meos_b.minus_int_set(i, s); }
		@Override public Pointer minus_int_span(int i, Pointer s) { return _meos_b.minus_int_span(i, s); }
		@Override public Pointer minus_int_spanset(int i, Pointer ss) { return _meos_b.minus_int_spanset(i, ss); }
		@Override public Pointer minus_set_bigint(Pointer s, long i) { return _meos_b.minus_set_bigint(s, i); }
		@Override public Pointer minus_set_date(Pointer s, int d) { return _meos_b.minus_set_date(s, d); }
		@Override public Pointer minus_set_float(Pointer s, double d) { return _meos_b.minus_set_float(s, d); }
		@Override public Pointer minus_set_geo(Pointer s, Pointer gs) { return _meos_b.minus_set_geo(s, gs); }
		@Override public Pointer minus_set_int(Pointer s, int i) { return _meos_b.minus_set_int(s, i); }
		@Override public Pointer minus_set_set(Pointer s1, Pointer s2) { return _meos_b.minus_set_set(s1, s2); }
		@Override public Pointer minus_set_text(Pointer s, Pointer txt) { return _meos_b.minus_set_text(s, txt); }
		@Override public Pointer minus_set_timestamptz(Pointer s, long t) { return _meos_b.minus_set_timestamptz(s, t); }
		@Override public Pointer minus_span_bigint(Pointer s, long i) { return _meos_b.minus_span_bigint(s, i); }
		@Override public Pointer minus_span_date(Pointer s, int d) { return _meos_b.minus_span_date(s, d); }
		@Override public Pointer minus_span_float(Pointer s, double d) { return _meos_b.minus_span_float(s, d); }
		@Override public Pointer minus_span_int(Pointer s, int i) { return _meos_b.minus_span_int(s, i); }
		@Override public Pointer minus_span_span(Pointer s1, Pointer s2) { return _meos_b.minus_span_span(s1, s2); }
		@Override public Pointer minus_span_spanset(Pointer s, Pointer ss) { return _meos_b.minus_span_spanset(s, ss); }
		@Override public Pointer minus_span_timestamptz(Pointer s, long t) { return _meos_b.minus_span_timestamptz(s, t); }
		@Override public Pointer minus_spanset_bigint(Pointer ss, long i) { return _meos_b.minus_spanset_bigint(ss, i); }
		@Override public Pointer minus_spanset_date(Pointer ss, int d) { return _meos_b.minus_spanset_date(ss, d); }
		@Override public Pointer minus_spanset_float(Pointer ss, double d) { return _meos_b.minus_spanset_float(ss, d); }
		@Override public Pointer minus_spanset_int(Pointer ss, int i) { return _meos_b.minus_spanset_int(ss, i); }
		@Override public Pointer minus_spanset_span(Pointer ss, Pointer s) { return _meos_b.minus_spanset_span(ss, s); }
		@Override public Pointer minus_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_b.minus_spanset_spanset(ss1, ss2); }
		@Override public Pointer minus_spanset_timestamptz(Pointer ss, long t) { return _meos_b.minus_spanset_timestamptz(ss, t); }
		@Override public Pointer minus_text_set(Pointer txt, Pointer s) { return _meos_b.minus_text_set(txt, s); }
		@Override public Pointer minus_timestamptz_set(long t, Pointer s) { return _meos_b.minus_timestamptz_set(t, s); }
		@Override public Pointer minus_timestamptz_span(long t, Pointer s) { return _meos_b.minus_timestamptz_span(t, s); }
		@Override public Pointer minus_timestamptz_spanset(long t, Pointer ss) { return _meos_b.minus_timestamptz_spanset(t, ss); }
		@Override public Pointer union_bigint_set(long i, Pointer s) { return _meos_b.union_bigint_set(i, s); }
		@Override public Pointer union_bigint_span(Pointer s, long i) { return _meos_b.union_bigint_span(s, i); }
		@Override public Pointer union_bigint_spanset(long i, Pointer ss) { return _meos_b.union_bigint_spanset(i, ss); }
		@Override public Pointer union_date_set(int d, Pointer s) { return _meos_b.union_date_set(d, s); }
		@Override public Pointer union_date_span(Pointer s, int d) { return _meos_b.union_date_span(s, d); }
		@Override public Pointer union_date_spanset(int d, Pointer ss) { return _meos_b.union_date_spanset(d, ss); }
		@Override public Pointer union_float_set(double d, Pointer s) { return _meos_b.union_float_set(d, s); }
		@Override public Pointer union_float_span(Pointer s, double d) { return _meos_b.union_float_span(s, d); }
		@Override public Pointer union_float_spanset(double d, Pointer ss) { return _meos_b.union_float_spanset(d, ss); }
		@Override public Pointer union_geo_set(Pointer gs, Pointer s) { return _meos_b.union_geo_set(gs, s); }
		@Override public Pointer union_int_set(int i, Pointer s) { return _meos_b.union_int_set(i, s); }
		@Override public Pointer union_int_span(int i, Pointer s) { return _meos_b.union_int_span(i, s); }
		@Override public Pointer union_int_spanset(int i, Pointer ss) { return _meos_b.union_int_spanset(i, ss); }
		@Override public Pointer union_set_bigint(Pointer s, long i) { return _meos_b.union_set_bigint(s, i); }
		@Override public Pointer union_set_date(Pointer s, int d) { return _meos_b.union_set_date(s, d); }
		@Override public Pointer union_set_float(Pointer s, double d) { return _meos_b.union_set_float(s, d); }
		@Override public Pointer union_set_geo(Pointer s, Pointer gs) { return _meos_b.union_set_geo(s, gs); }
		@Override public Pointer union_set_int(Pointer s, int i) { return _meos_b.union_set_int(s, i); }
		@Override public Pointer union_set_set(Pointer s1, Pointer s2) { return _meos_b.union_set_set(s1, s2); }
		@Override public Pointer union_set_text(Pointer s, Pointer txt) { return _meos_b.union_set_text(s, txt); }
		@Override public Pointer union_set_timestamptz(Pointer s, long t) { return _meos_b.union_set_timestamptz(s, t); }
		@Override public Pointer union_span_bigint(Pointer s, long i) { return _meos_b.union_span_bigint(s, i); }
		@Override public Pointer union_span_date(Pointer s, int d) { return _meos_b.union_span_date(s, d); }
		@Override public Pointer union_span_float(Pointer s, double d) { return _meos_b.union_span_float(s, d); }
		@Override public Pointer union_span_int(Pointer s, int i) { return _meos_b.union_span_int(s, i); }
		@Override public Pointer union_span_span(Pointer s1, Pointer s2) { return _meos_b.union_span_span(s1, s2); }
		@Override public Pointer union_span_spanset(Pointer s, Pointer ss) { return _meos_b.union_span_spanset(s, ss); }
		@Override public Pointer union_span_timestamptz(Pointer s, long t) { return _meos_b.union_span_timestamptz(s, t); }
		@Override public Pointer union_spanset_bigint(Pointer ss, long i) { return _meos_b.union_spanset_bigint(ss, i); }
		@Override public Pointer union_spanset_date(Pointer ss, int d) { return _meos_b.union_spanset_date(ss, d); }
		@Override public Pointer union_spanset_float(Pointer ss, double d) { return _meos_b.union_spanset_float(ss, d); }
		@Override public Pointer union_spanset_int(Pointer ss, int i) { return _meos_b.union_spanset_int(ss, i); }
		@Override public Pointer union_spanset_span(Pointer ss, Pointer s) { return _meos_b.union_spanset_span(ss, s); }
		@Override public Pointer union_spanset_spanset(Pointer ss1, Pointer ss2) { return _meos_b.union_spanset_spanset(ss1, ss2); }
		@Override public Pointer union_spanset_timestamptz(Pointer ss, long t) { return _meos_b.union_spanset_timestamptz(ss, t); }
		@Override public Pointer union_text_set(Pointer txt, Pointer s) { return _meos_b.union_text_set(txt, s); }
		@Override public Pointer union_timestamptz_set(long t, Pointer s) { return _meos_b.union_timestamptz_set(t, s); }
		@Override public Pointer union_timestamptz_span(long t, Pointer s) { return _meos_b.union_timestamptz_span(t, s); }
		@Override public Pointer union_timestamptz_spanset(long t, Pointer ss) { return _meos_b.union_timestamptz_spanset(t, ss); }
		@Override public long distance_bigintset_bigintset(Pointer s1, Pointer s2) { return _meos_b.distance_bigintset_bigintset(s1, s2); }
		@Override public long distance_bigintspan_bigintspan(Pointer s1, Pointer s2) { return _meos_b.distance_bigintspan_bigintspan(s1, s2); }
		@Override public long distance_bigintspanset_bigintspan(Pointer ss, Pointer s) { return _meos_b.distance_bigintspanset_bigintspan(ss, s); }
		@Override public long distance_bigintspanset_bigintspanset(Pointer ss1, Pointer ss2) { return _meos_b.distance_bigintspanset_bigintspanset(ss1, ss2); }
		@Override public int distance_dateset_dateset(Pointer s1, Pointer s2) { return _meos_b.distance_dateset_dateset(s1, s2); }
		@Override public int distance_datespan_datespan(Pointer s1, Pointer s2) { return _meos_b.distance_datespan_datespan(s1, s2); }
		@Override public int distance_datespanset_datespan(Pointer ss, Pointer s) { return _meos_b.distance_datespanset_datespan(ss, s); }
		@Override public int distance_datespanset_datespanset(Pointer ss1, Pointer ss2) { return _meos_b.distance_datespanset_datespanset(ss1, ss2); }
		@Override public double distance_floatset_floatset(Pointer s1, Pointer s2) { return _meos_b.distance_floatset_floatset(s1, s2); }
		@Override public double distance_floatspan_floatspan(Pointer s1, Pointer s2) { return _meos_b.distance_floatspan_floatspan(s1, s2); }
		@Override public double distance_floatspanset_floatspan(Pointer ss, Pointer s) { return _meos_b.distance_floatspanset_floatspan(ss, s); }
		@Override public double distance_floatspanset_floatspanset(Pointer ss1, Pointer ss2) { return _meos_b.distance_floatspanset_floatspanset(ss1, ss2); }
		@Override public int distance_intset_intset(Pointer s1, Pointer s2) { return _meos_b.distance_intset_intset(s1, s2); }
		@Override public int distance_intspan_intspan(Pointer s1, Pointer s2) { return _meos_b.distance_intspan_intspan(s1, s2); }
		@Override public int distance_intspanset_intspan(Pointer ss, Pointer s) { return _meos_b.distance_intspanset_intspan(ss, s); }
		@Override public int distance_intspanset_intspanset(Pointer ss1, Pointer ss2) { return _meos_b.distance_intspanset_intspanset(ss1, ss2); }
		@Override public long distance_set_bigint(Pointer s, long i) { return _meos_b.distance_set_bigint(s, i); }
		@Override public int distance_set_date(Pointer s, int d) { return _meos_b.distance_set_date(s, d); }
		@Override public double distance_set_float(Pointer s, double d) { return _meos_b.distance_set_float(s, d); }
		@Override public int distance_set_int(Pointer s, int i) { return _meos_b.distance_set_int(s, i); }
		@Override public double distance_set_timestamptz(Pointer s, long t) { return _meos_b.distance_set_timestamptz(s, t); }
		@Override public long distance_span_bigint(Pointer s, long i) { return _meos_b.distance_span_bigint(s, i); }
		@Override public int distance_span_date(Pointer s, int d) { return _meos_b.distance_span_date(s, d); }
		@Override public double distance_span_float(Pointer s, double d) { return _meos_b.distance_span_float(s, d); }
		@Override public int distance_span_int(Pointer s, int i) { return _meos_b.distance_span_int(s, i); }
		@Override public double distance_span_timestamptz(Pointer s, long t) { return _meos_b.distance_span_timestamptz(s, t); }
		@Override public long distance_spanset_bigint(Pointer ss, long i) { return _meos_b.distance_spanset_bigint(ss, i); }
		@Override public int distance_spanset_date(Pointer ss, int d) { return _meos_b.distance_spanset_date(ss, d); }
		@Override public double distance_spanset_float(Pointer ss, double d) { return _meos_b.distance_spanset_float(ss, d); }
		@Override public int distance_spanset_int(Pointer ss, int i) { return _meos_b.distance_spanset_int(ss, i); }
		@Override public double distance_spanset_timestamptz(Pointer ss, long t) { return _meos_b.distance_spanset_timestamptz(ss, t); }
		@Override public double distance_tstzset_tstzset(Pointer s1, Pointer s2) { return _meos_b.distance_tstzset_tstzset(s1, s2); }
		@Override public double distance_tstzspan_tstzspan(Pointer s1, Pointer s2) { return _meos_b.distance_tstzspan_tstzspan(s1, s2); }
		@Override public double distance_tstzspanset_tstzspan(Pointer ss, Pointer s) { return _meos_b.distance_tstzspanset_tstzspan(ss, s); }
		@Override public double distance_tstzspanset_tstzspanset(Pointer ss1, Pointer ss2) { return _meos_b.distance_tstzspanset_tstzspanset(ss1, ss2); }
		@Override public Pointer bigint_extent_transfn(Pointer state, long i) { return _meos_b.bigint_extent_transfn(state, i); }
		@Override public Pointer bigint_union_transfn(Pointer state, long i) { return _meos_b.bigint_union_transfn(state, i); }
		@Override public Pointer date_extent_transfn(Pointer state, int d) { return _meos_b.date_extent_transfn(state, d); }
		@Override public Pointer date_union_transfn(Pointer state, int d) { return _meos_b.date_union_transfn(state, d); }
		@Override public Pointer float_extent_transfn(Pointer state, double d) { return _meos_b.float_extent_transfn(state, d); }
		@Override public Pointer float_union_transfn(Pointer state, double d) { return _meos_b.float_union_transfn(state, d); }
		@Override public Pointer int_extent_transfn(Pointer state, int i) { return _meos_b.int_extent_transfn(state, i); }
		@Override public Pointer int_union_transfn(Pointer state, int i) { return _meos_b.int_union_transfn(state, i); }
		@Override public Pointer set_extent_transfn(Pointer state, Pointer s) { return _meos_b.set_extent_transfn(state, s); }
		@Override public Pointer set_union_finalfn(Pointer state) { return _meos_b.set_union_finalfn(state); }
		@Override public Pointer set_union_transfn(Pointer state, Pointer s) { return _meos_b.set_union_transfn(state, s); }
		@Override public Pointer span_extent_transfn(Pointer state, Pointer s) { return _meos_b.span_extent_transfn(state, s); }
		@Override public Pointer span_union_transfn(Pointer state, Pointer s) { return _meos_b.span_union_transfn(state, s); }
		@Override public Pointer spanset_extent_transfn(Pointer state, Pointer ss) { return _meos_b.spanset_extent_transfn(state, ss); }
		@Override public Pointer spanset_union_finalfn(Pointer state) { return _meos_b.spanset_union_finalfn(state); }
		@Override public Pointer spanset_union_transfn(Pointer state, Pointer ss) { return _meos_b.spanset_union_transfn(state, ss); }
		@Override public Pointer text_union_transfn(Pointer state, Pointer txt) { return _meos_b.text_union_transfn(state, txt); }
		@Override public Pointer timestamptz_extent_transfn(Pointer state, long t) { return _meos_b.timestamptz_extent_transfn(state, t); }
		@Override public Pointer timestamptz_union_transfn(Pointer state, long t) { return _meos_b.timestamptz_union_transfn(state, t); }
		@Override public Pointer tbox_in(String str) { return _meos_b.tbox_in(str); }
		@Override public String tbox_out(Pointer box, int maxdd) { return _meos_b.tbox_out(box, maxdd); }
		@Override public Pointer tbox_from_wkb(Pointer wkb, long size) { return _meos_b.tbox_from_wkb(wkb, size); }
		@Override public Pointer tbox_from_hexwkb(String hexwkb) { return _meos_b.tbox_from_hexwkb(hexwkb); }
		@Override public Pointer stbox_from_wkb(Pointer wkb, long size) { return _meos_b.stbox_from_wkb(wkb, size); }
		@Override public Pointer stbox_from_hexwkb(String hexwkb) { return _meos_b.stbox_from_hexwkb(hexwkb); }
		@Override public Pointer tbox_as_wkb(Pointer box, byte variant, Pointer size_out) { return _meos_b.tbox_as_wkb(box, variant, size_out); }
		@Override public String tbox_as_hexwkb(Pointer box, byte variant, Pointer size) { return _meos_b.tbox_as_hexwkb(box, variant, size); }
		@Override public Pointer stbox_as_wkb(Pointer box, byte variant, Pointer size_out) { return _meos_b.stbox_as_wkb(box, variant, size_out); }
		@Override public String stbox_as_hexwkb(Pointer box, byte variant, Pointer size) { return _meos_b.stbox_as_hexwkb(box, variant, size); }
		@Override public Pointer stbox_in(String str) { return _meos_b.stbox_in(str); }
		@Override public String stbox_out(Pointer box, int maxdd) { return _meos_b.stbox_out(box, maxdd); }
		@Override public Pointer float_tstzspan_to_tbox(double d, Pointer s) { return _meos_b.float_tstzspan_to_tbox(d, s); }
		@Override public Pointer float_timestamptz_to_tbox(double d, long t) { return _meos_b.float_timestamptz_to_tbox(d, t); }
		@Override public Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s) { return _meos_b.geo_tstzspan_to_stbox(gs, s); }
		@Override public Pointer geo_timestamptz_to_stbox(Pointer gs, long t) { return _meos_b.geo_timestamptz_to_stbox(gs, t); }
		@Override public Pointer int_tstzspan_to_tbox(int i, Pointer s) { return _meos_b.int_tstzspan_to_tbox(i, s); }
		@Override public Pointer int_timestamptz_to_tbox(int i, long t) { return _meos_b.int_timestamptz_to_tbox(i, t); }
		@Override public Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s) { return _meos_b.numspan_tstzspan_to_tbox(span, s); }
		@Override public Pointer numspan_timestamptz_to_tbox(Pointer span, long t) { return _meos_b.numspan_timestamptz_to_tbox(span, t); }
		@Override public Pointer stbox_copy(Pointer box) { return _meos_b.stbox_copy(box); }
		@Override public Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s) { return _meos_b.stbox_make(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, s); }
		@Override public Pointer tbox_copy(Pointer box) { return _meos_b.tbox_copy(box); }
		@Override public Pointer tbox_make(Pointer s, Pointer p) { return _meos_b.tbox_make(s, p); }
		@Override public Pointer float_to_tbox(double d) { return _meos_b.float_to_tbox(d); }
		@Override public Pointer geo_to_stbox(Pointer gs) { return _meos_b.geo_to_stbox(gs); }
		@Override public Pointer int_to_tbox(int i) { return _meos_b.int_to_tbox(i); }
		@Override public Pointer set_to_tbox(Pointer s) { return _meos_b.set_to_tbox(s); }
		@Override public Pointer span_to_tbox(Pointer s) { return _meos_b.span_to_tbox(s); }
		@Override public Pointer spanset_to_tbox(Pointer ss) { return _meos_b.spanset_to_tbox(ss); }
		@Override public Pointer spatialset_to_stbox(Pointer s) { return _meos_b.spatialset_to_stbox(s); }
		@Override public Pointer stbox_to_gbox(Pointer box) { return _meos_b.stbox_to_gbox(box); }
		@Override public Pointer stbox_to_box3d(Pointer box) { return _meos_b.stbox_to_box3d(box); }
		@Override public Pointer stbox_to_geo(Pointer box) { return _meos_b.stbox_to_geo(box); }
		@Override public Pointer stbox_to_tstzspan(Pointer box) { return _meos_b.stbox_to_tstzspan(box); }
		@Override public Pointer tbox_to_intspan(Pointer box) { return _meos_b.tbox_to_intspan(box); }
		@Override public Pointer tbox_to_floatspan(Pointer box) { return _meos_b.tbox_to_floatspan(box); }
		@Override public Pointer tbox_to_tstzspan(Pointer box) { return _meos_b.tbox_to_tstzspan(box); }
		@Override public Pointer timestamptz_to_stbox(long t) { return _meos_b.timestamptz_to_stbox(t); }
		@Override public Pointer timestamptz_to_tbox(long t) { return _meos_b.timestamptz_to_tbox(t); }
		@Override public Pointer tstzset_to_stbox(Pointer s) { return _meos_b.tstzset_to_stbox(s); }
		@Override public Pointer tstzspan_to_stbox(Pointer s) { return _meos_b.tstzspan_to_stbox(s); }
		@Override public Pointer tstzspanset_to_stbox(Pointer ss) { return _meos_b.tstzspanset_to_stbox(ss); }
		@Override public Pointer tnumber_to_tbox(Pointer temp) { return _meos_b.tnumber_to_tbox(temp); }
		@Override public Pointer tpoint_to_stbox(Pointer temp) { return _meos_b.tpoint_to_stbox(temp); }
		@Override public boolean stbox_hast(Pointer box) { return _meos_b.stbox_hast(box); }
		@Override public boolean stbox_hasx(Pointer box) { return _meos_b.stbox_hasx(box); }
		@Override public boolean stbox_hasz(Pointer box) { return _meos_b.stbox_hasz(box); }
		@Override public boolean stbox_isgeodetic(Pointer box) { return _meos_b.stbox_isgeodetic(box); }
		@Override public int stbox_srid(Pointer box) { return _meos_b.stbox_srid(box); }
		@Override public boolean stbox_tmax(Pointer box, Pointer result) { return _meos_b.stbox_tmax(box, result); }
		@Override public boolean stbox_tmax_inc(Pointer box, Pointer result) { return _meos_b.stbox_tmax_inc(box, result); }
		@Override public boolean stbox_tmin(Pointer box, Pointer result) { return _meos_b.stbox_tmin(box, result); }
		@Override public boolean stbox_tmin_inc(Pointer box, Pointer result) { return _meos_b.stbox_tmin_inc(box, result); }
		@Override public boolean stbox_xmax(Pointer box, Pointer result) { return _meos_b.stbox_xmax(box, result); }
		@Override public boolean stbox_xmin(Pointer box, Pointer result) { return _meos_b.stbox_xmin(box, result); }
		@Override public boolean stbox_ymax(Pointer box, Pointer result) { return _meos_b.stbox_ymax(box, result); }
		@Override public boolean stbox_ymin(Pointer box, Pointer result) { return _meos_b.stbox_ymin(box, result); }
		@Override public boolean stbox_zmax(Pointer box, Pointer result) { return _meos_b.stbox_zmax(box, result); }
		@Override public boolean stbox_zmin(Pointer box, Pointer result) { return _meos_b.stbox_zmin(box, result); }
		@Override public boolean tbox_hast(Pointer box) { return _meos_b.tbox_hast(box); }
		@Override public boolean tbox_hasx(Pointer box) { return _meos_b.tbox_hasx(box); }
		@Override public boolean tbox_tmax(Pointer box, Pointer result) { return _meos_b.tbox_tmax(box, result); }
		@Override public boolean tbox_tmax_inc(Pointer box, Pointer result) { return _meos_b.tbox_tmax_inc(box, result); }
		@Override public boolean tbox_tmin(Pointer box, Pointer result) { return _meos_b.tbox_tmin(box, result); }
		@Override public boolean tbox_tmin_inc(Pointer box, Pointer result) { return _meos_b.tbox_tmin_inc(box, result); }
		@Override public boolean tbox_xmax(Pointer box, Pointer result) { return _meos_c.tbox_xmax(box, result); }
		@Override public boolean tbox_xmax_inc(Pointer box, Pointer result) { return _meos_c.tbox_xmax_inc(box, result); }
		@Override public boolean tbox_xmin(Pointer box, Pointer result) { return _meos_c.tbox_xmin(box, result); }
		@Override public boolean tbox_xmin_inc(Pointer box, Pointer result) { return _meos_c.tbox_xmin_inc(box, result); }
		@Override public boolean tboxfloat_xmax(Pointer box, Pointer result) { return _meos_c.tboxfloat_xmax(box, result); }
		@Override public boolean tboxfloat_xmin(Pointer box, Pointer result) { return _meos_c.tboxfloat_xmin(box, result); }
		@Override public boolean tboxint_xmax(Pointer box, Pointer result) { return _meos_c.tboxint_xmax(box, result); }
		@Override public boolean tboxint_xmin(Pointer box, Pointer result) { return _meos_c.tboxint_xmin(box, result); }
		@Override public Pointer stbox_expand_space(Pointer box, double d) { return _meos_c.stbox_expand_space(box, d); }
		@Override public Pointer stbox_expand_time(Pointer box, Pointer interv) { return _meos_c.stbox_expand_time(box, interv); }
		@Override public Pointer stbox_get_space(Pointer box) { return _meos_c.stbox_get_space(box); }
		@Override public Pointer stbox_quad_split(Pointer box, Pointer count) { return _meos_c.stbox_quad_split(box, count); }
		@Override public Pointer stbox_round(Pointer box, int maxdd) { return _meos_c.stbox_round(box, maxdd); }
		@Override public Pointer stbox_set_srid(Pointer box, int srid) { return _meos_c.stbox_set_srid(box, srid); }
		@Override public Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) { return _meos_c.stbox_shift_scale_time(box, shift, duration); }
		@Override public Pointer stbox_transform(Pointer box, int srid) { return _meos_c.stbox_transform(box, srid); }
		@Override public Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward) { return _meos_c.stbox_transform_pipeline(box, pipelinestr, srid, is_forward); }
		@Override public Pointer tbox_expand_time(Pointer box, Pointer interv) { return _meos_c.tbox_expand_time(box, interv); }
		@Override public Pointer tbox_expand_float(Pointer box, double d) { return _meos_c.tbox_expand_float(box, d); }
		@Override public Pointer tbox_expand_int(Pointer box, int i) { return _meos_c.tbox_expand_int(box, i); }
		@Override public Pointer tbox_round(Pointer box, int maxdd) { return _meos_c.tbox_round(box, maxdd); }
		@Override public Pointer tbox_shift_scale_float(Pointer box, double shift, double width, boolean hasshift, boolean haswidth) { return _meos_c.tbox_shift_scale_float(box, shift, width, hasshift, haswidth); }
		@Override public Pointer tbox_shift_scale_int(Pointer box, int shift, int width, boolean hasshift, boolean haswidth) { return _meos_c.tbox_shift_scale_int(box, shift, width, hasshift, haswidth); }
		@Override public Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) { return _meos_c.tbox_shift_scale_time(box, shift, duration); }
		@Override public Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict) { return _meos_c.union_tbox_tbox(box1, box2, strict); }
		@Override public Pointer intersection_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.intersection_tbox_tbox(box1, box2); }
		@Override public Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict) { return _meos_c.union_stbox_stbox(box1, box2, strict); }
		@Override public Pointer intersection_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.intersection_stbox_stbox(box1, box2); }
		@Override public boolean adjacent_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.adjacent_stbox_stbox(box1, box2); }
		@Override public boolean adjacent_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.adjacent_tbox_tbox(box1, box2); }
		@Override public boolean contained_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.contained_tbox_tbox(box1, box2); }
		@Override public boolean contained_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.contained_stbox_stbox(box1, box2); }
		@Override public boolean contains_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.contains_stbox_stbox(box1, box2); }
		@Override public boolean contains_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.contains_tbox_tbox(box1, box2); }
		@Override public boolean overlaps_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.overlaps_tbox_tbox(box1, box2); }
		@Override public boolean overlaps_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overlaps_stbox_stbox(box1, box2); }
		@Override public boolean same_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.same_tbox_tbox(box1, box2); }
		@Override public boolean same_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.same_stbox_stbox(box1, box2); }
		@Override public boolean left_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.left_tbox_tbox(box1, box2); }
		@Override public boolean overleft_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.overleft_tbox_tbox(box1, box2); }
		@Override public boolean right_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.right_tbox_tbox(box1, box2); }
		@Override public boolean overright_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.overright_tbox_tbox(box1, box2); }
		@Override public boolean before_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.before_tbox_tbox(box1, box2); }
		@Override public boolean overbefore_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.overbefore_tbox_tbox(box1, box2); }
		@Override public boolean after_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.after_tbox_tbox(box1, box2); }
		@Override public boolean overafter_tbox_tbox(Pointer box1, Pointer box2) { return _meos_c.overafter_tbox_tbox(box1, box2); }
		@Override public boolean left_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.left_stbox_stbox(box1, box2); }
		@Override public boolean overleft_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overleft_stbox_stbox(box1, box2); }
		@Override public boolean right_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.right_stbox_stbox(box1, box2); }
		@Override public boolean overright_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overright_stbox_stbox(box1, box2); }
		@Override public boolean below_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.below_stbox_stbox(box1, box2); }
		@Override public boolean overbelow_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overbelow_stbox_stbox(box1, box2); }
		@Override public boolean above_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.above_stbox_stbox(box1, box2); }
		@Override public boolean overabove_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overabove_stbox_stbox(box1, box2); }
		@Override public boolean front_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.front_stbox_stbox(box1, box2); }
		@Override public boolean overfront_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overfront_stbox_stbox(box1, box2); }
		@Override public boolean back_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.back_stbox_stbox(box1, box2); }
		@Override public boolean overback_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overback_stbox_stbox(box1, box2); }
		@Override public boolean before_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.before_stbox_stbox(box1, box2); }
		@Override public boolean overbefore_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overbefore_stbox_stbox(box1, box2); }
		@Override public boolean after_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.after_stbox_stbox(box1, box2); }
		@Override public boolean overafter_stbox_stbox(Pointer box1, Pointer box2) { return _meos_c.overafter_stbox_stbox(box1, box2); }
		@Override public boolean tbox_eq(Pointer box1, Pointer box2) { return _meos_c.tbox_eq(box1, box2); }
		@Override public boolean tbox_ne(Pointer box1, Pointer box2) { return _meos_c.tbox_ne(box1, box2); }
		@Override public int tbox_cmp(Pointer box1, Pointer box2) { return _meos_c.tbox_cmp(box1, box2); }
		@Override public boolean tbox_lt(Pointer box1, Pointer box2) { return _meos_c.tbox_lt(box1, box2); }
		@Override public boolean tbox_le(Pointer box1, Pointer box2) { return _meos_c.tbox_le(box1, box2); }
		@Override public boolean tbox_ge(Pointer box1, Pointer box2) { return _meos_c.tbox_ge(box1, box2); }
		@Override public boolean tbox_gt(Pointer box1, Pointer box2) { return _meos_c.tbox_gt(box1, box2); }
		@Override public boolean stbox_eq(Pointer box1, Pointer box2) { return _meos_c.stbox_eq(box1, box2); }
		@Override public boolean stbox_ne(Pointer box1, Pointer box2) { return _meos_c.stbox_ne(box1, box2); }
		@Override public int stbox_cmp(Pointer box1, Pointer box2) { return _meos_c.stbox_cmp(box1, box2); }
		@Override public boolean stbox_lt(Pointer box1, Pointer box2) { return _meos_c.stbox_lt(box1, box2); }
		@Override public boolean stbox_le(Pointer box1, Pointer box2) { return _meos_c.stbox_le(box1, box2); }
		@Override public boolean stbox_ge(Pointer box1, Pointer box2) { return _meos_c.stbox_ge(box1, box2); }
		@Override public boolean stbox_gt(Pointer box1, Pointer box2) { return _meos_c.stbox_gt(box1, box2); }
		@Override public Pointer tbool_in(String str) { return _meos_c.tbool_in(str); }
		@Override public Pointer tint_in(String str) { return _meos_c.tint_in(str); }
		@Override public Pointer tfloat_in(String str) { return _meos_c.tfloat_in(str); }
		@Override public Pointer ttext_in(String str) { return _meos_c.ttext_in(str); }
		@Override public Pointer tgeompoint_in(String str) { return _meos_c.tgeompoint_in(str); }
		@Override public Pointer tgeogpoint_in(String str) { return _meos_c.tgeogpoint_in(str); }
		@Override public Pointer tbool_from_mfjson(String str) { return _meos_c.tbool_from_mfjson(str); }
		@Override public Pointer tint_from_mfjson(String str) { return _meos_c.tint_from_mfjson(str); }
		@Override public Pointer tfloat_from_mfjson(String str) { return _meos_c.tfloat_from_mfjson(str); }
		@Override public Pointer ttext_from_mfjson(String str) { return _meos_c.ttext_from_mfjson(str); }
		@Override public Pointer tgeompoint_from_mfjson(String str) { return _meos_c.tgeompoint_from_mfjson(str); }
		@Override public Pointer tgeogpoint_from_mfjson(String str) { return _meos_c.tgeogpoint_from_mfjson(str); }
		@Override public Pointer temporal_from_wkb(Pointer wkb, long size) { return _meos_c.temporal_from_wkb(wkb, size); }
		@Override public Pointer temporal_from_hexwkb(String hexwkb) { return _meos_c.temporal_from_hexwkb(hexwkb); }
		@Override public String tbool_out(Pointer temp) { return _meos_c.tbool_out(temp); }
		@Override public String tint_out(Pointer temp) { return _meos_c.tint_out(temp); }
		@Override public String tfloat_out(Pointer temp, int maxdd) { return _meos_c.tfloat_out(temp, maxdd); }
		@Override public String ttext_out(Pointer temp) { return _meos_c.ttext_out(temp); }
		@Override public String tpoint_out(Pointer temp, int maxdd) { return _meos_c.tpoint_out(temp, maxdd); }
		@Override public String tpoint_as_text(Pointer temp, int maxdd) { return _meos_c.tpoint_as_text(temp, maxdd); }
		@Override public String tpoint_as_ewkt(Pointer temp, int maxdd) { return _meos_c.tpoint_as_ewkt(temp, maxdd); }
		@Override public String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs) { return _meos_c.temporal_as_mfjson(temp, with_bbox, flags, precision, srs); }
		@Override public Pointer temporal_as_wkb(Pointer temp, byte variant, Pointer size_out) { return _meos_c.temporal_as_wkb(temp, variant, size_out); }
		@Override public String temporal_as_hexwkb(Pointer temp, byte variant, Pointer size_out) { return _meos_c.temporal_as_hexwkb(temp, variant, size_out); }
		@Override public Pointer tbool_from_base_temp(boolean b, Pointer temp) { return _meos_c.tbool_from_base_temp(b, temp); }
		@Override public Pointer tboolinst_make(boolean b, long t) { return _meos_c.tboolinst_make(b, t); }
		@Override public Pointer tboolseq_from_base_tstzset(boolean b, Pointer s) { return _meos_c.tboolseq_from_base_tstzset(b, s); }
		@Override public Pointer tboolseq_from_base_tstzspan(boolean b, Pointer s) { return _meos_c.tboolseq_from_base_tstzspan(b, s); }
		@Override public Pointer tboolseqset_from_base_tstzspanset(boolean b, Pointer ss) { return _meos_c.tboolseqset_from_base_tstzspanset(b, ss); }
		@Override public Pointer temporal_copy(Pointer temp) { return _meos_c.temporal_copy(temp); }
		@Override public Pointer tfloat_from_base_temp(double d, Pointer temp) { return _meos_c.tfloat_from_base_temp(d, temp); }
		@Override public Pointer tfloatinst_make(double d, long t) { return _meos_c.tfloatinst_make(d, t); }
		@Override public Pointer tfloatseq_from_base_tstzspan(double d, Pointer s, int interp) { return _meos_c.tfloatseq_from_base_tstzspan(d, s, interp); }
		@Override public Pointer tfloatseq_from_base_tstzset(double d, Pointer s) { return _meos_c.tfloatseq_from_base_tstzset(d, s); }
		@Override public Pointer tfloatseqset_from_base_tstzspanset(double d, Pointer ss, int interp) { return _meos_c.tfloatseqset_from_base_tstzspanset(d, ss, interp); }
		@Override public Pointer tint_from_base_temp(int i, Pointer temp) { return _meos_c.tint_from_base_temp(i, temp); }
		@Override public Pointer tintinst_make(int i, long t) { return _meos_c.tintinst_make(i, t); }
		@Override public Pointer tintseq_from_base_tstzspan(int i, Pointer s) { return _meos_c.tintseq_from_base_tstzspan(i, s); }
		@Override public Pointer tintseq_from_base_tstzset(int i, Pointer s) { return _meos_c.tintseq_from_base_tstzset(i, s); }
		@Override public Pointer tintseqset_from_base_tstzspanset(int i, Pointer ss) { return _meos_c.tintseqset_from_base_tstzspanset(i, ss); }
		@Override public Pointer tpoint_from_base_temp(Pointer gs, Pointer temp) { return _meos_c.tpoint_from_base_temp(gs, temp); }
		@Override public Pointer tpointinst_make(Pointer gs, long t) { return _meos_c.tpointinst_make(gs, t); }
		@Override public Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp) { return _meos_c.tpointseq_from_base_tstzspan(gs, s, interp); }
		@Override public Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s) { return _meos_c.tpointseq_from_base_tstzset(gs, s); }
		@Override public Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp) { return _meos_c.tpointseqset_from_base_tstzspanset(gs, ss, interp); }
		@Override public Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) { return _meos_c.tsequence_make(instants, count, lower_inc, upper_inc, interp, normalize); }
		@Override public Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize) { return _meos_c.tsequenceset_make(sequences, count, normalize); }
		@Override public Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist) { return _meos_c.tsequenceset_make_gaps(instants, count, interp, maxt, maxdist); }
		@Override public Pointer ttext_from_base_temp(Pointer txt, Pointer temp) { return _meos_c.ttext_from_base_temp(txt, temp); }
		@Override public Pointer ttextinst_make(Pointer txt, long t) { return _meos_c.ttextinst_make(txt, t); }
		@Override public Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s) { return _meos_c.ttextseq_from_base_tstzspan(txt, s); }
		@Override public Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s) { return _meos_c.ttextseq_from_base_tstzset(txt, s); }
		@Override public Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss) { return _meos_c.ttextseqset_from_base_tstzspanset(txt, ss); }
		@Override public Pointer temporal_to_tstzspan(Pointer temp) { return _meos_c.temporal_to_tstzspan(temp); }
		@Override public Pointer tfloat_to_tint(Pointer temp) { return _meos_c.tfloat_to_tint(temp); }
		@Override public Pointer tint_to_tfloat(Pointer temp) { return _meos_c.tint_to_tfloat(temp); }
		@Override public Pointer tnumber_to_span(Pointer temp) { return _meos_c.tnumber_to_span(temp); }
		@Override public boolean tbool_end_value(Pointer temp) { return _meos_c.tbool_end_value(temp); }
		@Override public boolean tbool_start_value(Pointer temp) { return _meos_c.tbool_start_value(temp); }
		@Override public boolean tbool_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value) { return _meos_c.tbool_value_at_timestamptz(temp, t, strict, value); }
		@Override public boolean tbool_value_n(Pointer temp, int n, Pointer result) { return _meos_c.tbool_value_n(temp, n, result); }
		@Override public Pointer tbool_values(Pointer temp, Pointer count) { return _meos_c.tbool_values(temp, count); }
		@Override public Pointer temporal_duration(Pointer temp, boolean boundspan) { return _meos_c.temporal_duration(temp, boundspan); }
		@Override public Pointer temporal_end_instant(Pointer temp) { return _meos_c.temporal_end_instant(temp); }
		@Override public Pointer temporal_end_sequence(Pointer temp) { return _meos_c.temporal_end_sequence(temp); }
		@Override public long temporal_end_timestamptz(Pointer temp) { return _meos_c.temporal_end_timestamptz(temp); }
		@Override public int temporal_hash(Pointer temp) { return _meos_c.temporal_hash(temp); }
		@Override public Pointer temporal_instant_n(Pointer temp, int n) { return _meos_c.temporal_instant_n(temp, n); }
		@Override public Pointer temporal_instants(Pointer temp, Pointer count) { return _meos_c.temporal_instants(temp, count); }
		@Override public String temporal_interp(Pointer temp) { return _meos_c.temporal_interp(temp); }
		@Override public Pointer temporal_max_instant(Pointer temp) { return _meos_c.temporal_max_instant(temp); }
		@Override public Pointer temporal_min_instant(Pointer temp) { return _meos_c.temporal_min_instant(temp); }
		@Override public int temporal_num_instants(Pointer temp) { return _meos_c.temporal_num_instants(temp); }
		@Override public int temporal_num_sequences(Pointer temp) { return _meos_c.temporal_num_sequences(temp); }
		@Override public int temporal_num_timestamps(Pointer temp) { return _meos_c.temporal_num_timestamps(temp); }
		@Override public Pointer temporal_segments(Pointer temp, Pointer count) { return _meos_c.temporal_segments(temp, count); }
		@Override public Pointer temporal_sequence_n(Pointer temp, int i) { return _meos_c.temporal_sequence_n(temp, i); }
		@Override public Pointer temporal_sequences(Pointer temp, Pointer count) { return _meos_c.temporal_sequences(temp, count); }
		@Override public int temporal_lower_inc(Pointer temp) { return _meos_c.temporal_lower_inc(temp); }
		@Override public int temporal_upper_inc(Pointer temp) { return _meos_c.temporal_upper_inc(temp); }
		@Override public Pointer temporal_start_instant(Pointer temp) { return _meos_c.temporal_start_instant(temp); }
		@Override public Pointer temporal_start_sequence(Pointer temp) { return _meos_c.temporal_start_sequence(temp); }
		@Override public long temporal_start_timestamptz(Pointer temp) { return _meos_c.temporal_start_timestamptz(temp); }
		@Override public Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration) { return _meos_c.temporal_stops(temp, maxdist, minduration); }
		@Override public String temporal_subtype(Pointer temp) { return _meos_c.temporal_subtype(temp); }
		@Override public Pointer temporal_time(Pointer temp) { return _meos_c.temporal_time(temp); }
		@Override public boolean temporal_timestamptz_n(Pointer temp, int n, Pointer result) { return _meos_c.temporal_timestamptz_n(temp, n, result); }
		@Override public Pointer temporal_timestamps(Pointer temp, Pointer count) { return _meos_c.temporal_timestamps(temp, count); }
		@Override public double tfloat_end_value(Pointer temp) { return _meos_c.tfloat_end_value(temp); }
		@Override public double tfloat_max_value(Pointer temp) { return _meos_c.tfloat_max_value(temp); }
		@Override public double tfloat_min_value(Pointer temp) { return _meos_c.tfloat_min_value(temp); }
		@Override public double tfloat_start_value(Pointer temp) { return _meos_c.tfloat_start_value(temp); }
		@Override public boolean tfloat_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value) { return _meos_c.tfloat_value_at_timestamptz(temp, t, strict, value); }
		@Override public boolean tfloat_value_n(Pointer temp, int n, Pointer result) { return _meos_c.tfloat_value_n(temp, n, result); }
		@Override public Pointer tfloat_values(Pointer temp, Pointer count) { return _meos_c.tfloat_values(temp, count); }
		@Override public int tint_end_value(Pointer temp) { return _meos_c.tint_end_value(temp); }
		@Override public int tint_max_value(Pointer temp) { return _meos_c.tint_max_value(temp); }
		@Override public int tint_min_value(Pointer temp) { return _meos_c.tint_min_value(temp); }
		@Override public int tint_start_value(Pointer temp) { return _meos_c.tint_start_value(temp); }
		@Override public boolean tint_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value) { return _meos_c.tint_value_at_timestamptz(temp, t, strict, value); }
		@Override public boolean tint_value_n(Pointer temp, int n, Pointer result) { return _meos_c.tint_value_n(temp, n, result); }
		@Override public Pointer tint_values(Pointer temp, Pointer count) { return _meos_c.tint_values(temp, count); }
		@Override public double tnumber_integral(Pointer temp) { return _meos_c.tnumber_integral(temp); }
		@Override public double tnumber_twavg(Pointer temp) { return _meos_c.tnumber_twavg(temp); }
		@Override public Pointer tnumber_valuespans(Pointer temp) { return _meos_c.tnumber_valuespans(temp); }
		@Override public Pointer tpoint_end_value(Pointer temp) { return _meos_c.tpoint_end_value(temp); }
		@Override public Pointer tpoint_start_value(Pointer temp) { return _meos_c.tpoint_start_value(temp); }
		@Override public boolean tpoint_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value) { return _meos_c.tpoint_value_at_timestamptz(temp, t, strict, value); }
		@Override public boolean tpoint_value_n(Pointer temp, int n, Pointer result) { return _meos_c.tpoint_value_n(temp, n, result); }
		@Override public Pointer tpoint_values(Pointer temp, Pointer count) { return _meos_c.tpoint_values(temp, count); }
		@Override public Pointer ttext_end_value(Pointer temp) { return _meos_c.ttext_end_value(temp); }
		@Override public Pointer ttext_max_value(Pointer temp) { return _meos_c.ttext_max_value(temp); }
		@Override public Pointer ttext_min_value(Pointer temp) { return _meos_c.ttext_min_value(temp); }
		@Override public Pointer ttext_start_value(Pointer temp) { return _meos_c.ttext_start_value(temp); }
		@Override public boolean ttext_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value) { return _meos_c.ttext_value_at_timestamptz(temp, t, strict, value); }
		@Override public boolean ttext_value_n(Pointer temp, int n, Pointer result) { return _meos_c.ttext_value_n(temp, n, result); }
		@Override public Pointer ttext_values(Pointer temp, Pointer count) { return _meos_c.ttext_values(temp, count); }
		@Override public double float_degrees(double value, boolean normalize) { return _meos_c.float_degrees(value, normalize); }
		@Override public Pointer temporal_scale_time(Pointer temp, Pointer duration) { return _meos_c.temporal_scale_time(temp, duration); }
		@Override public Pointer temporal_set_interp(Pointer temp, int interp) { return _meos_c.temporal_set_interp(temp, interp); }
		@Override public Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration) { return _meos_c.temporal_shift_scale_time(temp, shift, duration); }
		@Override public Pointer temporal_shift_time(Pointer temp, Pointer shift) { return _meos_c.temporal_shift_time(temp, shift); }
		@Override public Pointer temporal_to_tinstant(Pointer temp) { return _meos_c.temporal_to_tinstant(temp); }
		@Override public Pointer temporal_to_tsequence(Pointer temp, String interp_str) { return _meos_c.temporal_to_tsequence(temp, interp_str); }
		@Override public Pointer temporal_to_tsequenceset(Pointer temp, String interp_str) { return _meos_c.temporal_to_tsequenceset(temp, interp_str); }
		@Override public Pointer tfloat_floor(Pointer temp) { return _meos_c.tfloat_floor(temp); }
		@Override public Pointer tfloat_ceil(Pointer temp) { return _meos_c.tfloat_ceil(temp); }
		@Override public Pointer tfloat_degrees(Pointer temp, boolean normalize) { return _meos_c.tfloat_degrees(temp, normalize); }
		@Override public Pointer tfloat_radians(Pointer temp) { return _meos_c.tfloat_radians(temp); }
		@Override public Pointer tfloat_round(Pointer temp, int maxdd) { return _meos_c.tfloat_round(temp, maxdd); }
		@Override public Pointer tfloat_scale_value(Pointer temp, double width) { return _meos_c.tfloat_scale_value(temp, width); }
		@Override public Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width) { return _meos_c.tfloat_shift_scale_value(temp, shift, width); }
		@Override public Pointer tfloat_shift_value(Pointer temp, double shift) { return _meos_c.tfloat_shift_value(temp, shift); }
		@Override public Pointer tfloatarr_round(Pointer temp, int count, int maxdd) { return _meos_c.tfloatarr_round(temp, count, maxdd); }
		@Override public Pointer tint_scale_value(Pointer temp, int width) { return _meos_c.tint_scale_value(temp, width); }
		@Override public Pointer tint_shift_scale_value(Pointer temp, int shift, int width) { return _meos_c.tint_shift_scale_value(temp, shift, width); }
		@Override public Pointer tint_shift_value(Pointer temp, int shift) { return _meos_c.tint_shift_value(temp, shift); }
		@Override public Pointer tpoint_round(Pointer temp, int maxdd) { return _meos_c.tpoint_round(temp, maxdd); }
		@Override public Pointer tpoint_transform(Pointer temp, int srid) { return _meos_c.tpoint_transform(temp, srid); }
		@Override public Pointer tpoint_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward) { return _meos_c.tpoint_transform_pipeline(temp, pipelinestr, srid, is_forward); }
		@Override public Pointer tpoint_transform_pj(Pointer temp, int srid, Pointer pj) { return _meos_c.tpoint_transform_pj(temp, srid, pj); }
		@Override public Pointer lwproj_transform(int srid_from, int srid_to) { return _meos_c.lwproj_transform(srid_from, srid_to); }
		@Override public Pointer tpointarr_round(Pointer temp, int count, int maxdd) { return _meos_c.tpointarr_round(temp, count, maxdd); }
		@Override public Pointer temporal_append_tinstant(Pointer temp, Pointer inst, double maxdist, Pointer maxt, boolean expand) { return _meos_c.temporal_append_tinstant(temp, inst, maxdist, maxt, expand); }
		@Override public Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand) { return _meos_c.temporal_append_tsequence(temp, seq, expand); }
		@Override public Pointer temporal_delete_tstzspan(Pointer temp, Pointer s, boolean connect) { return _meos_c.temporal_delete_tstzspan(temp, s, connect); }
		@Override public Pointer temporal_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect) { return _meos_c.temporal_delete_tstzspanset(temp, ss, connect); }
		@Override public Pointer temporal_delete_timestamptz(Pointer temp, long t, boolean connect) { return _meos_c.temporal_delete_timestamptz(temp, t, connect); }
		@Override public Pointer temporal_delete_tstzset(Pointer temp, Pointer s, boolean connect) { return _meos_c.temporal_delete_tstzset(temp, s, connect); }
		@Override public Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect) { return _meos_c.temporal_insert(temp1, temp2, connect); }
		@Override public Pointer temporal_merge(Pointer temp1, Pointer temp2) { return _meos_c.temporal_merge(temp1, temp2); }
		@Override public Pointer temporal_merge_array(Pointer temparr, int count) { return _meos_c.temporal_merge_array(temparr, count); }
		@Override public Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect) { return _meos_c.temporal_update(temp1, temp2, connect); }
		@Override public Pointer tbool_at_value(Pointer temp, boolean b) { return _meos_c.tbool_at_value(temp, b); }
		@Override public Pointer tbool_minus_value(Pointer temp, boolean b) { return _meos_c.tbool_minus_value(temp, b); }
		@Override public Pointer temporal_at_max(Pointer temp) { return _meos_c.temporal_at_max(temp); }
		@Override public Pointer temporal_at_min(Pointer temp) { return _meos_c.temporal_at_min(temp); }
		@Override public Pointer temporal_at_tstzspan(Pointer temp, Pointer s) { return _meos_c.temporal_at_tstzspan(temp, s); }
		@Override public Pointer temporal_at_tstzspanset(Pointer temp, Pointer ss) { return _meos_c.temporal_at_tstzspanset(temp, ss); }
		@Override public Pointer temporal_at_timestamptz(Pointer temp, long t) { return _meos_c.temporal_at_timestamptz(temp, t); }
		@Override public Pointer temporal_at_tstzset(Pointer temp, Pointer s) { return _meos_c.temporal_at_tstzset(temp, s); }
		@Override public Pointer temporal_at_values(Pointer temp, Pointer set) { return _meos_c.temporal_at_values(temp, set); }
		@Override public Pointer temporal_minus_max(Pointer temp) { return _meos_c.temporal_minus_max(temp); }
		@Override public Pointer temporal_minus_min(Pointer temp) { return _meos_c.temporal_minus_min(temp); }
		@Override public Pointer temporal_minus_tstzspan(Pointer temp, Pointer s) { return _meos_c.temporal_minus_tstzspan(temp, s); }
		@Override public Pointer temporal_minus_tstzspanset(Pointer temp, Pointer ss) { return _meos_c.temporal_minus_tstzspanset(temp, ss); }
		@Override public Pointer temporal_minus_timestamptz(Pointer temp, long t) { return _meos_c.temporal_minus_timestamptz(temp, t); }
		@Override public Pointer temporal_minus_tstzset(Pointer temp, Pointer s) { return _meos_c.temporal_minus_tstzset(temp, s); }
		@Override public Pointer temporal_minus_values(Pointer temp, Pointer set) { return _meos_c.temporal_minus_values(temp, set); }
		@Override public Pointer tfloat_at_value(Pointer temp, double d) { return _meos_c.tfloat_at_value(temp, d); }
		@Override public Pointer tfloat_minus_value(Pointer temp, double d) { return _meos_c.tfloat_minus_value(temp, d); }
		@Override public Pointer tint_at_value(Pointer temp, int i) { return _meos_c.tint_at_value(temp, i); }
		@Override public Pointer tint_minus_value(Pointer temp, int i) { return _meos_c.tint_minus_value(temp, i); }
		@Override public Pointer tnumber_at_span(Pointer temp, Pointer span) { return _meos_c.tnumber_at_span(temp, span); }
		@Override public Pointer tnumber_at_spanset(Pointer temp, Pointer ss) { return _meos_c.tnumber_at_spanset(temp, ss); }
		@Override public Pointer tnumber_at_tbox(Pointer temp, Pointer box) { return _meos_c.tnumber_at_tbox(temp, box); }
		@Override public Pointer tnumber_minus_span(Pointer temp, Pointer span) { return _meos_c.tnumber_minus_span(temp, span); }
		@Override public Pointer tnumber_minus_spanset(Pointer temp, Pointer ss) { return _meos_c.tnumber_minus_spanset(temp, ss); }
		@Override public Pointer tnumber_minus_tbox(Pointer temp, Pointer box) { return _meos_c.tnumber_minus_tbox(temp, box); }
		@Override public Pointer tpoint_at_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period) { return _meos_c.tpoint_at_geom_time(temp, gs, zspan, period); }
		@Override public Pointer tpoint_at_stbox(Pointer temp, Pointer box, boolean border_inc) { return _meos_c.tpoint_at_stbox(temp, box, border_inc); }
		@Override public Pointer tpoint_at_value(Pointer temp, Pointer gs) { return _meos_c.tpoint_at_value(temp, gs); }
		@Override public Pointer tpoint_minus_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period) { return _meos_c.tpoint_minus_geom_time(temp, gs, zspan, period); }
		@Override public Pointer tpoint_minus_stbox(Pointer temp, Pointer box, boolean border_inc) { return _meos_c.tpoint_minus_stbox(temp, box, border_inc); }
		@Override public Pointer tpoint_minus_value(Pointer temp, Pointer gs) { return _meos_c.tpoint_minus_value(temp, gs); }
		@Override public Pointer ttext_at_value(Pointer temp, Pointer txt) { return _meos_c.ttext_at_value(temp, txt); }
		@Override public Pointer ttext_minus_value(Pointer temp, Pointer txt) { return _meos_c.ttext_minus_value(temp, txt); }
		@Override public int temporal_cmp(Pointer temp1, Pointer temp2) { return _meos_c.temporal_cmp(temp1, temp2); }
		@Override public boolean temporal_eq(Pointer temp1, Pointer temp2) { return _meos_c.temporal_eq(temp1, temp2); }
		@Override public boolean temporal_ge(Pointer temp1, Pointer temp2) { return _meos_c.temporal_ge(temp1, temp2); }
		@Override public boolean temporal_gt(Pointer temp1, Pointer temp2) { return _meos_c.temporal_gt(temp1, temp2); }
		@Override public boolean temporal_le(Pointer temp1, Pointer temp2) { return _meos_c.temporal_le(temp1, temp2); }
		@Override public boolean temporal_lt(Pointer temp1, Pointer temp2) { return _meos_c.temporal_lt(temp1, temp2); }
		@Override public boolean temporal_ne(Pointer temp1, Pointer temp2) { return _meos_c.temporal_ne(temp1, temp2); }
		@Override public int always_eq_bool_tbool(boolean b, Pointer temp) { return _meos_c.always_eq_bool_tbool(b, temp); }
		@Override public int always_eq_float_tfloat(double d, Pointer temp) { return _meos_c.always_eq_float_tfloat(d, temp); }
		@Override public int always_eq_int_tint(int i, Pointer temp) { return _meos_c.always_eq_int_tint(i, temp); }
		@Override public int always_eq_point_tpoint(Pointer gs, Pointer temp) { return _meos_c.always_eq_point_tpoint(gs, temp); }
		@Override public int always_eq_tbool_bool(Pointer temp, boolean b) { return _meos_c.always_eq_tbool_bool(temp, b); }
		@Override public int always_eq_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.always_eq_temporal_temporal(temp1, temp2); }
		@Override public int always_eq_text_ttext(Pointer txt, Pointer temp) { return _meos_c.always_eq_text_ttext(txt, temp); }
		@Override public int always_eq_tfloat_float(Pointer temp, double d) { return _meos_c.always_eq_tfloat_float(temp, d); }
		@Override public int always_eq_tint_int(Pointer temp, int i) { return _meos_c.always_eq_tint_int(temp, i); }
		@Override public int always_eq_tpoint_point(Pointer temp, Pointer gs) { return _meos_c.always_eq_tpoint_point(temp, gs); }
		@Override public int always_eq_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_c.always_eq_tpoint_tpoint(temp1, temp2); }
		@Override public int always_eq_ttext_text(Pointer temp, Pointer txt) { return _meos_c.always_eq_ttext_text(temp, txt); }
		@Override public int always_ne_bool_tbool(boolean b, Pointer temp) { return _meos_c.always_ne_bool_tbool(b, temp); }
		@Override public int always_ne_float_tfloat(double d, Pointer temp) { return _meos_c.always_ne_float_tfloat(d, temp); }
		@Override public int always_ne_int_tint(int i, Pointer temp) { return _meos_c.always_ne_int_tint(i, temp); }
		@Override public int always_ne_point_tpoint(Pointer gs, Pointer temp) { return _meos_c.always_ne_point_tpoint(gs, temp); }
		@Override public int always_ne_tbool_bool(Pointer temp, boolean b) { return _meos_c.always_ne_tbool_bool(temp, b); }
		@Override public int always_ne_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.always_ne_temporal_temporal(temp1, temp2); }
		@Override public int always_ne_text_ttext(Pointer txt, Pointer temp) { return _meos_c.always_ne_text_ttext(txt, temp); }
		@Override public int always_ne_tfloat_float(Pointer temp, double d) { return _meos_c.always_ne_tfloat_float(temp, d); }
		@Override public int always_ne_tint_int(Pointer temp, int i) { return _meos_c.always_ne_tint_int(temp, i); }
		@Override public int always_ne_tpoint_point(Pointer temp, Pointer gs) { return _meos_c.always_ne_tpoint_point(temp, gs); }
		@Override public int always_ne_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_c.always_ne_tpoint_tpoint(temp1, temp2); }
		@Override public int always_ne_ttext_text(Pointer temp, Pointer txt) { return _meos_c.always_ne_ttext_text(temp, txt); }
		@Override public int always_ge_float_tfloat(double d, Pointer temp) { return _meos_c.always_ge_float_tfloat(d, temp); }
		@Override public int always_ge_int_tint(int i, Pointer temp) { return _meos_c.always_ge_int_tint(i, temp); }
		@Override public int always_ge_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.always_ge_temporal_temporal(temp1, temp2); }
		@Override public int always_ge_text_ttext(Pointer txt, Pointer temp) { return _meos_c.always_ge_text_ttext(txt, temp); }
		@Override public int always_ge_tfloat_float(Pointer temp, double d) { return _meos_c.always_ge_tfloat_float(temp, d); }
		@Override public int always_ge_tint_int(Pointer temp, int i) { return _meos_c.always_ge_tint_int(temp, i); }
		@Override public int always_ge_ttext_text(Pointer temp, Pointer txt) { return _meos_c.always_ge_ttext_text(temp, txt); }
		@Override public int always_gt_float_tfloat(double d, Pointer temp) { return _meos_c.always_gt_float_tfloat(d, temp); }
		@Override public int always_gt_int_tint(int i, Pointer temp) { return _meos_c.always_gt_int_tint(i, temp); }
		@Override public int always_gt_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.always_gt_temporal_temporal(temp1, temp2); }
		@Override public int always_gt_text_ttext(Pointer txt, Pointer temp) { return _meos_c.always_gt_text_ttext(txt, temp); }
		@Override public int always_gt_tfloat_float(Pointer temp, double d) { return _meos_c.always_gt_tfloat_float(temp, d); }
		@Override public int always_gt_tint_int(Pointer temp, int i) { return _meos_c.always_gt_tint_int(temp, i); }
		@Override public int always_gt_ttext_text(Pointer temp, Pointer txt) { return _meos_c.always_gt_ttext_text(temp, txt); }
		@Override public int always_le_float_tfloat(double d, Pointer temp) { return _meos_c.always_le_float_tfloat(d, temp); }
		@Override public int always_le_int_tint(int i, Pointer temp) { return _meos_c.always_le_int_tint(i, temp); }
		@Override public int always_le_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.always_le_temporal_temporal(temp1, temp2); }
		@Override public int always_le_text_ttext(Pointer txt, Pointer temp) { return _meos_c.always_le_text_ttext(txt, temp); }
		@Override public int always_le_tfloat_float(Pointer temp, double d) { return _meos_c.always_le_tfloat_float(temp, d); }
		@Override public int always_le_tint_int(Pointer temp, int i) { return _meos_c.always_le_tint_int(temp, i); }
		@Override public int always_le_ttext_text(Pointer temp, Pointer txt) { return _meos_c.always_le_ttext_text(temp, txt); }
		@Override public int always_lt_float_tfloat(double d, Pointer temp) { return _meos_c.always_lt_float_tfloat(d, temp); }
		@Override public int always_lt_int_tint(int i, Pointer temp) { return _meos_c.always_lt_int_tint(i, temp); }
		@Override public int always_lt_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.always_lt_temporal_temporal(temp1, temp2); }
		@Override public int always_lt_text_ttext(Pointer txt, Pointer temp) { return _meos_c.always_lt_text_ttext(txt, temp); }
		@Override public int always_lt_tfloat_float(Pointer temp, double d) { return _meos_c.always_lt_tfloat_float(temp, d); }
		@Override public int always_lt_tint_int(Pointer temp, int i) { return _meos_c.always_lt_tint_int(temp, i); }
		@Override public int always_lt_ttext_text(Pointer temp, Pointer txt) { return _meos_c.always_lt_ttext_text(temp, txt); }
		@Override public int ever_eq_bool_tbool(boolean b, Pointer temp) { return _meos_c.ever_eq_bool_tbool(b, temp); }
		@Override public int ever_eq_float_tfloat(double d, Pointer temp) { return _meos_c.ever_eq_float_tfloat(d, temp); }
		@Override public int ever_eq_int_tint(int i, Pointer temp) { return _meos_c.ever_eq_int_tint(i, temp); }
		@Override public int ever_eq_point_tpoint(Pointer gs, Pointer temp) { return _meos_c.ever_eq_point_tpoint(gs, temp); }
		@Override public int ever_eq_tbool_bool(Pointer temp, boolean b) { return _meos_c.ever_eq_tbool_bool(temp, b); }
		@Override public int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.ever_eq_temporal_temporal(temp1, temp2); }
		@Override public int ever_eq_text_ttext(Pointer txt, Pointer temp) { return _meos_c.ever_eq_text_ttext(txt, temp); }
		@Override public int ever_eq_tfloat_float(Pointer temp, double d) { return _meos_c.ever_eq_tfloat_float(temp, d); }
		@Override public int ever_eq_tint_int(Pointer temp, int i) { return _meos_c.ever_eq_tint_int(temp, i); }
		@Override public int ever_eq_tpoint_point(Pointer temp, Pointer gs) { return _meos_c.ever_eq_tpoint_point(temp, gs); }
		@Override public int ever_eq_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_c.ever_eq_tpoint_tpoint(temp1, temp2); }
		@Override public int ever_eq_ttext_text(Pointer temp, Pointer txt) { return _meos_c.ever_eq_ttext_text(temp, txt); }
		@Override public int ever_ge_float_tfloat(double d, Pointer temp) { return _meos_c.ever_ge_float_tfloat(d, temp); }
		@Override public int ever_ge_int_tint(int i, Pointer temp) { return _meos_c.ever_ge_int_tint(i, temp); }
		@Override public int ever_ge_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.ever_ge_temporal_temporal(temp1, temp2); }
		@Override public int ever_ge_text_ttext(Pointer txt, Pointer temp) { return _meos_c.ever_ge_text_ttext(txt, temp); }
		@Override public int ever_ge_tfloat_float(Pointer temp, double d) { return _meos_c.ever_ge_tfloat_float(temp, d); }
		@Override public int ever_ge_tint_int(Pointer temp, int i) { return _meos_c.ever_ge_tint_int(temp, i); }
		@Override public int ever_ge_ttext_text(Pointer temp, Pointer txt) { return _meos_c.ever_ge_ttext_text(temp, txt); }
		@Override public int ever_gt_float_tfloat(double d, Pointer temp) { return _meos_c.ever_gt_float_tfloat(d, temp); }
		@Override public int ever_gt_int_tint(int i, Pointer temp) { return _meos_c.ever_gt_int_tint(i, temp); }
		@Override public int ever_gt_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.ever_gt_temporal_temporal(temp1, temp2); }
		@Override public int ever_gt_text_ttext(Pointer txt, Pointer temp) { return _meos_c.ever_gt_text_ttext(txt, temp); }
		@Override public int ever_gt_tfloat_float(Pointer temp, double d) { return _meos_c.ever_gt_tfloat_float(temp, d); }
		@Override public int ever_gt_tint_int(Pointer temp, int i) { return _meos_c.ever_gt_tint_int(temp, i); }
		@Override public int ever_gt_ttext_text(Pointer temp, Pointer txt) { return _meos_c.ever_gt_ttext_text(temp, txt); }
		@Override public int ever_le_float_tfloat(double d, Pointer temp) { return _meos_c.ever_le_float_tfloat(d, temp); }
		@Override public int ever_le_int_tint(int i, Pointer temp) { return _meos_c.ever_le_int_tint(i, temp); }
		@Override public int ever_le_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.ever_le_temporal_temporal(temp1, temp2); }
		@Override public int ever_le_text_ttext(Pointer txt, Pointer temp) { return _meos_c.ever_le_text_ttext(txt, temp); }
		@Override public int ever_le_tfloat_float(Pointer temp, double d) { return _meos_c.ever_le_tfloat_float(temp, d); }
		@Override public int ever_le_tint_int(Pointer temp, int i) { return _meos_c.ever_le_tint_int(temp, i); }
		@Override public int ever_le_ttext_text(Pointer temp, Pointer txt) { return _meos_c.ever_le_ttext_text(temp, txt); }
		@Override public int ever_lt_float_tfloat(double d, Pointer temp) { return _meos_c.ever_lt_float_tfloat(d, temp); }
		@Override public int ever_lt_int_tint(int i, Pointer temp) { return _meos_c.ever_lt_int_tint(i, temp); }
		@Override public int ever_lt_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.ever_lt_temporal_temporal(temp1, temp2); }
		@Override public int ever_lt_text_ttext(Pointer txt, Pointer temp) { return _meos_c.ever_lt_text_ttext(txt, temp); }
		@Override public int ever_lt_tfloat_float(Pointer temp, double d) { return _meos_c.ever_lt_tfloat_float(temp, d); }
		@Override public int ever_lt_tint_int(Pointer temp, int i) { return _meos_c.ever_lt_tint_int(temp, i); }
		@Override public int ever_lt_ttext_text(Pointer temp, Pointer txt) { return _meos_c.ever_lt_ttext_text(temp, txt); }
		@Override public int ever_ne_bool_tbool(boolean b, Pointer temp) { return _meos_c.ever_ne_bool_tbool(b, temp); }
		@Override public int ever_ne_float_tfloat(double d, Pointer temp) { return _meos_c.ever_ne_float_tfloat(d, temp); }
		@Override public int ever_ne_int_tint(int i, Pointer temp) { return _meos_c.ever_ne_int_tint(i, temp); }
		@Override public int ever_ne_point_tpoint(Pointer gs, Pointer temp) { return _meos_c.ever_ne_point_tpoint(gs, temp); }
		@Override public int ever_ne_tbool_bool(Pointer temp, boolean b) { return _meos_c.ever_ne_tbool_bool(temp, b); }
		@Override public int ever_ne_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_c.ever_ne_temporal_temporal(temp1, temp2); }
		@Override public int ever_ne_text_ttext(Pointer txt, Pointer temp) { return _meos_c.ever_ne_text_ttext(txt, temp); }
		@Override public int ever_ne_tfloat_float(Pointer temp, double d) { return _meos_c.ever_ne_tfloat_float(temp, d); }
		@Override public int ever_ne_tint_int(Pointer temp, int i) { return _meos_c.ever_ne_tint_int(temp, i); }
		@Override public int ever_ne_tpoint_point(Pointer temp, Pointer gs) { return _meos_c.ever_ne_tpoint_point(temp, gs); }
		@Override public int ever_ne_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.ever_ne_tpoint_tpoint(temp1, temp2); }
		@Override public int ever_ne_ttext_text(Pointer temp, Pointer txt) { return _meos_d.ever_ne_ttext_text(temp, txt); }
		@Override public Pointer teq_bool_tbool(boolean b, Pointer temp) { return _meos_d.teq_bool_tbool(b, temp); }
		@Override public Pointer teq_float_tfloat(double d, Pointer temp) { return _meos_d.teq_float_tfloat(d, temp); }
		@Override public Pointer teq_int_tint(int i, Pointer temp) { return _meos_d.teq_int_tint(i, temp); }
		@Override public Pointer teq_point_tpoint(Pointer gs, Pointer temp) { return _meos_d.teq_point_tpoint(gs, temp); }
		@Override public Pointer teq_tbool_bool(Pointer temp, boolean b) { return _meos_d.teq_tbool_bool(temp, b); }
		@Override public Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.teq_temporal_temporal(temp1, temp2); }
		@Override public Pointer teq_text_ttext(Pointer txt, Pointer temp) { return _meos_d.teq_text_ttext(txt, temp); }
		@Override public Pointer teq_tfloat_float(Pointer temp, double d) { return _meos_d.teq_tfloat_float(temp, d); }
		@Override public Pointer teq_tpoint_point(Pointer temp, Pointer gs) { return _meos_d.teq_tpoint_point(temp, gs); }
		@Override public Pointer teq_tint_int(Pointer temp, int i) { return _meos_d.teq_tint_int(temp, i); }
		@Override public Pointer teq_ttext_text(Pointer temp, Pointer txt) { return _meos_d.teq_ttext_text(temp, txt); }
		@Override public Pointer tge_float_tfloat(double d, Pointer temp) { return _meos_d.tge_float_tfloat(d, temp); }
		@Override public Pointer tge_int_tint(int i, Pointer temp) { return _meos_d.tge_int_tint(i, temp); }
		@Override public Pointer tge_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.tge_temporal_temporal(temp1, temp2); }
		@Override public Pointer tge_text_ttext(Pointer txt, Pointer temp) { return _meos_d.tge_text_ttext(txt, temp); }
		@Override public Pointer tge_tfloat_float(Pointer temp, double d) { return _meos_d.tge_tfloat_float(temp, d); }
		@Override public Pointer tge_tint_int(Pointer temp, int i) { return _meos_d.tge_tint_int(temp, i); }
		@Override public Pointer tge_ttext_text(Pointer temp, Pointer txt) { return _meos_d.tge_ttext_text(temp, txt); }
		@Override public Pointer tgt_float_tfloat(double d, Pointer temp) { return _meos_d.tgt_float_tfloat(d, temp); }
		@Override public Pointer tgt_int_tint(int i, Pointer temp) { return _meos_d.tgt_int_tint(i, temp); }
		@Override public Pointer tgt_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.tgt_temporal_temporal(temp1, temp2); }
		@Override public Pointer tgt_text_ttext(Pointer txt, Pointer temp) { return _meos_d.tgt_text_ttext(txt, temp); }
		@Override public Pointer tgt_tfloat_float(Pointer temp, double d) { return _meos_d.tgt_tfloat_float(temp, d); }
		@Override public Pointer tgt_tint_int(Pointer temp, int i) { return _meos_d.tgt_tint_int(temp, i); }
		@Override public Pointer tgt_ttext_text(Pointer temp, Pointer txt) { return _meos_d.tgt_ttext_text(temp, txt); }
		@Override public Pointer tle_float_tfloat(double d, Pointer temp) { return _meos_d.tle_float_tfloat(d, temp); }
		@Override public Pointer tle_int_tint(int i, Pointer temp) { return _meos_d.tle_int_tint(i, temp); }
		@Override public Pointer tle_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.tle_temporal_temporal(temp1, temp2); }
		@Override public Pointer tle_text_ttext(Pointer txt, Pointer temp) { return _meos_d.tle_text_ttext(txt, temp); }
		@Override public Pointer tle_tfloat_float(Pointer temp, double d) { return _meos_d.tle_tfloat_float(temp, d); }
		@Override public Pointer tle_tint_int(Pointer temp, int i) { return _meos_d.tle_tint_int(temp, i); }
		@Override public Pointer tle_ttext_text(Pointer temp, Pointer txt) { return _meos_d.tle_ttext_text(temp, txt); }
		@Override public Pointer tlt_float_tfloat(double d, Pointer temp) { return _meos_d.tlt_float_tfloat(d, temp); }
		@Override public Pointer tlt_int_tint(int i, Pointer temp) { return _meos_d.tlt_int_tint(i, temp); }
		@Override public Pointer tlt_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.tlt_temporal_temporal(temp1, temp2); }
		@Override public Pointer tlt_text_ttext(Pointer txt, Pointer temp) { return _meos_d.tlt_text_ttext(txt, temp); }
		@Override public Pointer tlt_tfloat_float(Pointer temp, double d) { return _meos_d.tlt_tfloat_float(temp, d); }
		@Override public Pointer tlt_tint_int(Pointer temp, int i) { return _meos_d.tlt_tint_int(temp, i); }
		@Override public Pointer tlt_ttext_text(Pointer temp, Pointer txt) { return _meos_d.tlt_ttext_text(temp, txt); }
		@Override public Pointer tne_bool_tbool(boolean b, Pointer temp) { return _meos_d.tne_bool_tbool(b, temp); }
		@Override public Pointer tne_float_tfloat(double d, Pointer temp) { return _meos_d.tne_float_tfloat(d, temp); }
		@Override public Pointer tne_int_tint(int i, Pointer temp) { return _meos_d.tne_int_tint(i, temp); }
		@Override public Pointer tne_point_tpoint(Pointer gs, Pointer temp) { return _meos_d.tne_point_tpoint(gs, temp); }
		@Override public Pointer tne_tbool_bool(Pointer temp, boolean b) { return _meos_d.tne_tbool_bool(temp, b); }
		@Override public Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.tne_temporal_temporal(temp1, temp2); }
		@Override public Pointer tne_text_ttext(Pointer txt, Pointer temp) { return _meos_d.tne_text_ttext(txt, temp); }
		@Override public Pointer tne_tfloat_float(Pointer temp, double d) { return _meos_d.tne_tfloat_float(temp, d); }
		@Override public Pointer tne_tpoint_point(Pointer temp, Pointer gs) { return _meos_d.tne_tpoint_point(temp, gs); }
		@Override public Pointer tne_tint_int(Pointer temp, int i) { return _meos_d.tne_tint_int(temp, i); }
		@Override public Pointer tne_ttext_text(Pointer temp, Pointer txt) { return _meos_d.tne_ttext_text(temp, txt); }
		@Override public boolean adjacent_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.adjacent_numspan_tnumber(s, temp); }
		@Override public boolean adjacent_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.adjacent_stbox_tpoint(box, temp); }
		@Override public boolean adjacent_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.adjacent_tbox_tnumber(box, temp); }
		@Override public boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.adjacent_temporal_temporal(temp1, temp2); }
		@Override public boolean adjacent_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.adjacent_temporal_tstzspan(temp, s); }
		@Override public boolean adjacent_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.adjacent_tnumber_numspan(temp, s); }
		@Override public boolean adjacent_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.adjacent_tnumber_tbox(temp, box); }
		@Override public boolean adjacent_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.adjacent_tnumber_tnumber(temp1, temp2); }
		@Override public boolean adjacent_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.adjacent_tpoint_stbox(temp, box); }
		@Override public boolean adjacent_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.adjacent_tpoint_tpoint(temp1, temp2); }
		@Override public boolean adjacent_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.adjacent_tstzspan_temporal(s, temp); }
		@Override public boolean contained_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.contained_numspan_tnumber(s, temp); }
		@Override public boolean contained_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.contained_stbox_tpoint(box, temp); }
		@Override public boolean contained_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.contained_tbox_tnumber(box, temp); }
		@Override public boolean contained_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.contained_temporal_temporal(temp1, temp2); }
		@Override public boolean contained_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.contained_temporal_tstzspan(temp, s); }
		@Override public boolean contained_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.contained_tnumber_numspan(temp, s); }
		@Override public boolean contained_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.contained_tnumber_tbox(temp, box); }
		@Override public boolean contained_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.contained_tnumber_tnumber(temp1, temp2); }
		@Override public boolean contained_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.contained_tpoint_stbox(temp, box); }
		@Override public boolean contained_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.contained_tpoint_tpoint(temp1, temp2); }
		@Override public boolean contained_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.contained_tstzspan_temporal(s, temp); }
		@Override public boolean contains_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.contains_numspan_tnumber(s, temp); }
		@Override public boolean contains_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.contains_stbox_tpoint(box, temp); }
		@Override public boolean contains_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.contains_tbox_tnumber(box, temp); }
		@Override public boolean contains_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.contains_temporal_tstzspan(temp, s); }
		@Override public boolean contains_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.contains_temporal_temporal(temp1, temp2); }
		@Override public boolean contains_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.contains_tnumber_numspan(temp, s); }
		@Override public boolean contains_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.contains_tnumber_tbox(temp, box); }
		@Override public boolean contains_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.contains_tnumber_tnumber(temp1, temp2); }
		@Override public boolean contains_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.contains_tpoint_stbox(temp, box); }
		@Override public boolean contains_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.contains_tpoint_tpoint(temp1, temp2); }
		@Override public boolean contains_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.contains_tstzspan_temporal(s, temp); }
		@Override public boolean overlaps_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.overlaps_numspan_tnumber(s, temp); }
		@Override public boolean overlaps_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overlaps_stbox_tpoint(box, temp); }
		@Override public boolean overlaps_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.overlaps_tbox_tnumber(box, temp); }
		@Override public boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.overlaps_temporal_temporal(temp1, temp2); }
		@Override public boolean overlaps_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.overlaps_temporal_tstzspan(temp, s); }
		@Override public boolean overlaps_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.overlaps_tnumber_numspan(temp, s); }
		@Override public boolean overlaps_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.overlaps_tnumber_tbox(temp, box); }
		@Override public boolean overlaps_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.overlaps_tnumber_tnumber(temp1, temp2); }
		@Override public boolean overlaps_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overlaps_tpoint_stbox(temp, box); }
		@Override public boolean overlaps_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overlaps_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overlaps_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.overlaps_tstzspan_temporal(s, temp); }
		@Override public boolean same_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.same_numspan_tnumber(s, temp); }
		@Override public boolean same_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.same_stbox_tpoint(box, temp); }
		@Override public boolean same_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.same_tbox_tnumber(box, temp); }
		@Override public boolean same_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.same_temporal_temporal(temp1, temp2); }
		@Override public boolean same_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.same_temporal_tstzspan(temp, s); }
		@Override public boolean same_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.same_tnumber_numspan(temp, s); }
		@Override public boolean same_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.same_tnumber_tbox(temp, box); }
		@Override public boolean same_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.same_tnumber_tnumber(temp1, temp2); }
		@Override public boolean same_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.same_tpoint_stbox(temp, box); }
		@Override public boolean same_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.same_tpoint_tpoint(temp1, temp2); }
		@Override public boolean same_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.same_tstzspan_temporal(s, temp); }
		@Override public boolean above_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.above_stbox_tpoint(box, temp); }
		@Override public boolean above_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.above_tpoint_stbox(temp, box); }
		@Override public boolean above_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.above_tpoint_tpoint(temp1, temp2); }
		@Override public boolean after_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.after_stbox_tpoint(box, temp); }
		@Override public boolean after_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.after_tbox_tnumber(box, temp); }
		@Override public boolean after_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.after_temporal_tstzspan(temp, s); }
		@Override public boolean after_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.after_temporal_temporal(temp1, temp2); }
		@Override public boolean after_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.after_tnumber_tbox(temp, box); }
		@Override public boolean after_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.after_tnumber_tnumber(temp1, temp2); }
		@Override public boolean after_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.after_tpoint_stbox(temp, box); }
		@Override public boolean after_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.after_tpoint_tpoint(temp1, temp2); }
		@Override public boolean after_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.after_tstzspan_temporal(s, temp); }
		@Override public boolean back_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.back_stbox_tpoint(box, temp); }
		@Override public boolean back_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.back_tpoint_stbox(temp, box); }
		@Override public boolean back_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.back_tpoint_tpoint(temp1, temp2); }
		@Override public boolean before_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.before_stbox_tpoint(box, temp); }
		@Override public boolean before_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.before_tbox_tnumber(box, temp); }
		@Override public boolean before_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.before_temporal_tstzspan(temp, s); }
		@Override public boolean before_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.before_temporal_temporal(temp1, temp2); }
		@Override public boolean before_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.before_tnumber_tbox(temp, box); }
		@Override public boolean before_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.before_tnumber_tnumber(temp1, temp2); }
		@Override public boolean before_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.before_tpoint_stbox(temp, box); }
		@Override public boolean before_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.before_tpoint_tpoint(temp1, temp2); }
		@Override public boolean before_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.before_tstzspan_temporal(s, temp); }
		@Override public boolean below_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.below_stbox_tpoint(box, temp); }
		@Override public boolean below_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.below_tpoint_stbox(temp, box); }
		@Override public boolean below_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.below_tpoint_tpoint(temp1, temp2); }
		@Override public boolean front_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.front_stbox_tpoint(box, temp); }
		@Override public boolean front_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.front_tpoint_stbox(temp, box); }
		@Override public boolean front_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.front_tpoint_tpoint(temp1, temp2); }
		@Override public boolean left_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.left_stbox_tpoint(box, temp); }
		@Override public boolean left_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.left_tbox_tnumber(box, temp); }
		@Override public boolean left_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.left_numspan_tnumber(s, temp); }
		@Override public boolean left_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.left_tnumber_numspan(temp, s); }
		@Override public boolean left_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.left_tnumber_tbox(temp, box); }
		@Override public boolean left_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.left_tnumber_tnumber(temp1, temp2); }
		@Override public boolean left_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.left_tpoint_stbox(temp, box); }
		@Override public boolean left_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.left_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overabove_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overabove_stbox_tpoint(box, temp); }
		@Override public boolean overabove_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overabove_tpoint_stbox(temp, box); }
		@Override public boolean overabove_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overabove_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overafter_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overafter_stbox_tpoint(box, temp); }
		@Override public boolean overafter_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.overafter_tbox_tnumber(box, temp); }
		@Override public boolean overafter_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.overafter_temporal_tstzspan(temp, s); }
		@Override public boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.overafter_temporal_temporal(temp1, temp2); }
		@Override public boolean overafter_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.overafter_tnumber_tbox(temp, box); }
		@Override public boolean overafter_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.overafter_tnumber_tnumber(temp1, temp2); }
		@Override public boolean overafter_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overafter_tpoint_stbox(temp, box); }
		@Override public boolean overafter_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overafter_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overafter_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.overafter_tstzspan_temporal(s, temp); }
		@Override public boolean overback_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overback_stbox_tpoint(box, temp); }
		@Override public boolean overback_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overback_tpoint_stbox(temp, box); }
		@Override public boolean overback_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overback_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overbefore_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overbefore_stbox_tpoint(box, temp); }
		@Override public boolean overbefore_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.overbefore_tbox_tnumber(box, temp); }
		@Override public boolean overbefore_temporal_tstzspan(Pointer temp, Pointer s) { return _meos_d.overbefore_temporal_tstzspan(temp, s); }
		@Override public boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2) { return _meos_d.overbefore_temporal_temporal(temp1, temp2); }
		@Override public boolean overbefore_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.overbefore_tnumber_tbox(temp, box); }
		@Override public boolean overbefore_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.overbefore_tnumber_tnumber(temp1, temp2); }
		@Override public boolean overbefore_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overbefore_tpoint_stbox(temp, box); }
		@Override public boolean overbefore_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overbefore_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overbefore_tstzspan_temporal(Pointer s, Pointer temp) { return _meos_d.overbefore_tstzspan_temporal(s, temp); }
		@Override public boolean overbelow_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overbelow_stbox_tpoint(box, temp); }
		@Override public boolean overbelow_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overbelow_tpoint_stbox(temp, box); }
		@Override public boolean overbelow_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overbelow_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overfront_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overfront_stbox_tpoint(box, temp); }
		@Override public boolean overfront_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overfront_tpoint_stbox(temp, box); }
		@Override public boolean overfront_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overfront_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overleft_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.overleft_numspan_tnumber(s, temp); }
		@Override public boolean overleft_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overleft_stbox_tpoint(box, temp); }
		@Override public boolean overleft_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.overleft_tbox_tnumber(box, temp); }
		@Override public boolean overleft_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.overleft_tnumber_numspan(temp, s); }
		@Override public boolean overleft_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.overleft_tnumber_tbox(temp, box); }
		@Override public boolean overleft_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.overleft_tnumber_tnumber(temp1, temp2); }
		@Override public boolean overleft_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overleft_tpoint_stbox(temp, box); }
		@Override public boolean overleft_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overleft_tpoint_tpoint(temp1, temp2); }
		@Override public boolean overright_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.overright_numspan_tnumber(s, temp); }
		@Override public boolean overright_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.overright_stbox_tpoint(box, temp); }
		@Override public boolean overright_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.overright_tbox_tnumber(box, temp); }
		@Override public boolean overright_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.overright_tnumber_numspan(temp, s); }
		@Override public boolean overright_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.overright_tnumber_tbox(temp, box); }
		@Override public boolean overright_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.overright_tnumber_tnumber(temp1, temp2); }
		@Override public boolean overright_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.overright_tpoint_stbox(temp, box); }
		@Override public boolean overright_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.overright_tpoint_tpoint(temp1, temp2); }
		@Override public boolean right_numspan_tnumber(Pointer s, Pointer temp) { return _meos_d.right_numspan_tnumber(s, temp); }
		@Override public boolean right_stbox_tpoint(Pointer box, Pointer temp) { return _meos_d.right_stbox_tpoint(box, temp); }
		@Override public boolean right_tbox_tnumber(Pointer box, Pointer temp) { return _meos_d.right_tbox_tnumber(box, temp); }
		@Override public boolean right_tnumber_numspan(Pointer temp, Pointer s) { return _meos_d.right_tnumber_numspan(temp, s); }
		@Override public boolean right_tnumber_tbox(Pointer temp, Pointer box) { return _meos_d.right_tnumber_tbox(temp, box); }
		@Override public boolean right_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.right_tnumber_tnumber(temp1, temp2); }
		@Override public boolean right_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.right_tpoint_stbox(temp, box); }
		@Override public boolean right_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.right_tpoint_tpoint(temp1, temp2); }
		@Override public Pointer tand_bool_tbool(boolean b, Pointer temp) { return _meos_d.tand_bool_tbool(b, temp); }
		@Override public Pointer tand_tbool_bool(Pointer temp, boolean b) { return _meos_d.tand_tbool_bool(temp, b); }
		@Override public Pointer tand_tbool_tbool(Pointer temp1, Pointer temp2) { return _meos_d.tand_tbool_tbool(temp1, temp2); }
		@Override public Pointer tbool_when_true(Pointer temp) { return _meos_d.tbool_when_true(temp); }
		@Override public Pointer tnot_tbool(Pointer temp) { return _meos_d.tnot_tbool(temp); }
		@Override public Pointer tor_bool_tbool(boolean b, Pointer temp) { return _meos_d.tor_bool_tbool(b, temp); }
		@Override public Pointer tor_tbool_bool(Pointer temp, boolean b) { return _meos_d.tor_tbool_bool(temp, b); }
		@Override public Pointer tor_tbool_tbool(Pointer temp1, Pointer temp2) { return _meos_d.tor_tbool_tbool(temp1, temp2); }
		@Override public Pointer add_float_tfloat(double d, Pointer tnumber) { return _meos_d.add_float_tfloat(d, tnumber); }
		@Override public Pointer add_int_tint(int i, Pointer tnumber) { return _meos_d.add_int_tint(i, tnumber); }
		@Override public Pointer add_tfloat_float(Pointer tnumber, double d) { return _meos_d.add_tfloat_float(tnumber, d); }
		@Override public Pointer add_tint_int(Pointer tnumber, int i) { return _meos_d.add_tint_int(tnumber, i); }
		@Override public Pointer add_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) { return _meos_d.add_tnumber_tnumber(tnumber1, tnumber2); }
		@Override public Pointer div_float_tfloat(double d, Pointer tnumber) { return _meos_d.div_float_tfloat(d, tnumber); }
		@Override public Pointer div_int_tint(int i, Pointer tnumber) { return _meos_d.div_int_tint(i, tnumber); }
		@Override public Pointer div_tfloat_float(Pointer tnumber, double d) { return _meos_d.div_tfloat_float(tnumber, d); }
		@Override public Pointer div_tint_int(Pointer tnumber, int i) { return _meos_d.div_tint_int(tnumber, i); }
		@Override public Pointer div_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) { return _meos_d.div_tnumber_tnumber(tnumber1, tnumber2); }
		@Override public Pointer mult_float_tfloat(double d, Pointer tnumber) { return _meos_d.mult_float_tfloat(d, tnumber); }
		@Override public Pointer mult_int_tint(int i, Pointer tnumber) { return _meos_d.mult_int_tint(i, tnumber); }
		@Override public Pointer mult_tfloat_float(Pointer tnumber, double d) { return _meos_d.mult_tfloat_float(tnumber, d); }
		@Override public Pointer mult_tint_int(Pointer tnumber, int i) { return _meos_d.mult_tint_int(tnumber, i); }
		@Override public Pointer mult_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) { return _meos_d.mult_tnumber_tnumber(tnumber1, tnumber2); }
		@Override public Pointer sub_float_tfloat(double d, Pointer tnumber) { return _meos_d.sub_float_tfloat(d, tnumber); }
		@Override public Pointer sub_int_tint(int i, Pointer tnumber) { return _meos_d.sub_int_tint(i, tnumber); }
		@Override public Pointer sub_tfloat_float(Pointer tnumber, double d) { return _meos_d.sub_tfloat_float(tnumber, d); }
		@Override public Pointer sub_tint_int(Pointer tnumber, int i) { return _meos_d.sub_tint_int(tnumber, i); }
		@Override public Pointer sub_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) { return _meos_d.sub_tnumber_tnumber(tnumber1, tnumber2); }
		@Override public Pointer tfloat_derivative(Pointer temp) { return _meos_d.tfloat_derivative(temp); }
		@Override public Pointer tnumber_abs(Pointer temp) { return _meos_d.tnumber_abs(temp); }
		@Override public Pointer tnumber_angular_difference(Pointer temp) { return _meos_d.tnumber_angular_difference(temp); }
		@Override public Pointer tnumber_delta_value(Pointer temp) { return _meos_d.tnumber_delta_value(temp); }
		@Override public Pointer textcat_text_ttext(Pointer txt, Pointer temp) { return _meos_d.textcat_text_ttext(txt, temp); }
		@Override public Pointer textcat_ttext_text(Pointer temp, Pointer txt) { return _meos_d.textcat_ttext_text(temp, txt); }
		@Override public Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2) { return _meos_d.textcat_ttext_ttext(temp1, temp2); }
		@Override public Pointer ttext_upper(Pointer temp) { return _meos_d.ttext_upper(temp); }
		@Override public Pointer ttext_lower(Pointer temp) { return _meos_d.ttext_lower(temp); }
		@Override public Pointer ttext_initcap(Pointer temp) { return _meos_d.ttext_initcap(temp); }
		@Override public Pointer distance_tfloat_float(Pointer temp, double d) { return _meos_d.distance_tfloat_float(temp, d); }
		@Override public Pointer distance_tint_int(Pointer temp, int i) { return _meos_d.distance_tint_int(temp, i); }
		@Override public Pointer distance_tnumber_tnumber(Pointer temp1, Pointer temp2) { return _meos_d.distance_tnumber_tnumber(temp1, temp2); }
		@Override public Pointer distance_tpoint_point(Pointer temp, Pointer gs) { return _meos_d.distance_tpoint_point(temp, gs); }
		@Override public Pointer distance_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.distance_tpoint_tpoint(temp1, temp2); }
		@Override public double nad_stbox_geo(Pointer box, Pointer gs) { return _meos_d.nad_stbox_geo(box, gs); }
		@Override public double nad_stbox_stbox(Pointer box1, Pointer box2) { return _meos_d.nad_stbox_stbox(box1, box2); }
		@Override public int nad_tint_int(Pointer temp, int i) { return _meos_d.nad_tint_int(temp, i); }
		@Override public int nad_tint_tbox(Pointer temp, Pointer box) { return _meos_d.nad_tint_tbox(temp, box); }
		@Override public int nad_tint_tint(Pointer temp1, Pointer temp2) { return _meos_d.nad_tint_tint(temp1, temp2); }
		@Override public int nad_tboxint_tboxint(Pointer box1, Pointer box2) { return _meos_d.nad_tboxint_tboxint(box1, box2); }
		@Override public double nad_tfloat_float(Pointer temp, double d) { return _meos_d.nad_tfloat_float(temp, d); }
		@Override public double nad_tfloat_tfloat(Pointer temp1, Pointer temp2) { return _meos_d.nad_tfloat_tfloat(temp1, temp2); }
		@Override public double nad_tfloat_tbox(Pointer temp, Pointer box) { return _meos_d.nad_tfloat_tbox(temp, box); }
		@Override public double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2) { return _meos_d.nad_tboxfloat_tboxfloat(box1, box2); }
		@Override public double nad_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.nad_tpoint_geo(temp, gs); }
		@Override public double nad_tpoint_stbox(Pointer temp, Pointer box) { return _meos_d.nad_tpoint_stbox(temp, box); }
		@Override public double nad_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.nad_tpoint_tpoint(temp1, temp2); }
		@Override public Pointer nai_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.nai_tpoint_geo(temp, gs); }
		@Override public Pointer nai_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.nai_tpoint_tpoint(temp1, temp2); }
		@Override public Pointer shortestline_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.shortestline_tpoint_geo(temp, gs); }
		@Override public Pointer shortestline_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.shortestline_tpoint_tpoint(temp1, temp2); }
		@Override public boolean bearing_point_point(Pointer gs1, Pointer gs2, Pointer result) { return _meos_d.bearing_point_point(gs1, gs2, result); }
		@Override public Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert) { return _meos_d.bearing_tpoint_point(temp, gs, invert); }
		@Override public Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.bearing_tpoint_tpoint(temp1, temp2); }
		@Override public Pointer tpoint_angular_difference(Pointer temp) { return _meos_d.tpoint_angular_difference(temp); }
		@Override public Pointer tpoint_azimuth(Pointer temp) { return _meos_d.tpoint_azimuth(temp); }
		@Override public Pointer tpoint_convex_hull(Pointer temp) { return _meos_d.tpoint_convex_hull(temp); }
		@Override public Pointer tpoint_cumulative_length(Pointer temp) { return _meos_d.tpoint_cumulative_length(temp); }
		@Override public boolean tpoint_direction(Pointer temp, Pointer result) { return _meos_d.tpoint_direction(temp, result); }
		@Override public Pointer tpoint_get_x(Pointer temp) { return _meos_d.tpoint_get_x(temp); }
		@Override public Pointer tpoint_get_y(Pointer temp) { return _meos_d.tpoint_get_y(temp); }
		@Override public Pointer tpoint_get_z(Pointer temp) { return _meos_d.tpoint_get_z(temp); }
		@Override public boolean tpoint_is_simple(Pointer temp) { return _meos_d.tpoint_is_simple(temp); }
		@Override public double tpoint_length(Pointer temp) { return _meos_d.tpoint_length(temp); }
		@Override public Pointer tpoint_speed(Pointer temp) { return _meos_d.tpoint_speed(temp); }
		@Override public int tpoint_srid(Pointer temp) { return _meos_d.tpoint_srid(temp); }
		@Override public Pointer tpoint_stboxes(Pointer temp, Pointer count) { return _meos_d.tpoint_stboxes(temp, count); }
		@Override public Pointer tpoint_trajectory(Pointer temp) { return _meos_d.tpoint_trajectory(temp); }
		@Override public Pointer tpoint_twcentroid(Pointer temp) { return _meos_d.tpoint_twcentroid(temp); }
		@Override public Pointer geo_expand_space(Pointer gs, double d) { return _meos_d.geo_expand_space(gs, d); }
		@Override public Pointer geomeas_to_tpoint(Pointer gs) { return _meos_d.geomeas_to_tpoint(gs); }
		@Override public Pointer tgeogpoint_to_tgeompoint(Pointer temp) { return _meos_d.tgeogpoint_to_tgeompoint(temp); }
		@Override public Pointer tgeompoint_to_tgeogpoint(Pointer temp) { return _meos_d.tgeompoint_to_tgeogpoint(temp); }
		@Override public boolean tpoint_AsMVTGeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count) { return _meos_d.tpoint_AsMVTGeom(temp, bounds, extent, buffer, clip_geom, gsarr, timesarr, count); }
		@Override public Pointer tpoint_expand_space(Pointer temp, double d) { return _meos_d.tpoint_expand_space(temp, d); }
		@Override public Pointer tpoint_make_simple(Pointer temp, Pointer count) { return _meos_d.tpoint_make_simple(temp, count); }
		@Override public Pointer tpoint_set_srid(Pointer temp, int srid) { return _meos_d.tpoint_set_srid(temp, srid); }
		@Override public boolean tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize, Pointer result) { return _meos_d.tpoint_tfloat_to_geomeas(tpoint, measure, segmentize, result); }
		@Override public int acontains_geo_tpoint(Pointer gs, Pointer temp) { return _meos_d.acontains_geo_tpoint(gs, temp); }
		@Override public int adisjoint_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.adisjoint_tpoint_geo(temp, gs); }
		@Override public int adisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.adisjoint_tpoint_tpoint(temp1, temp2); }
		@Override public int adwithin_tpoint_geo(Pointer temp, Pointer gs, double dist) { return _meos_d.adwithin_tpoint_geo(temp, gs, dist); }
		@Override public int adwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist) { return _meos_d.adwithin_tpoint_tpoint(temp1, temp2, dist); }
		@Override public int aintersects_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.aintersects_tpoint_geo(temp, gs); }
		@Override public int aintersects_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.aintersects_tpoint_tpoint(temp1, temp2); }
		@Override public int atouches_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.atouches_tpoint_geo(temp, gs); }
		@Override public int econtains_geo_tpoint(Pointer gs, Pointer temp) { return _meos_d.econtains_geo_tpoint(gs, temp); }
		@Override public int edisjoint_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.edisjoint_tpoint_geo(temp, gs); }
		@Override public int edisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.edisjoint_tpoint_tpoint(temp1, temp2); }
		@Override public int edwithin_tpoint_geo(Pointer temp, Pointer gs, double dist) { return _meos_d.edwithin_tpoint_geo(temp, gs, dist); }
		@Override public int edwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist) { return _meos_d.edwithin_tpoint_tpoint(temp1, temp2, dist); }
		@Override public int eintersects_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.eintersects_tpoint_geo(temp, gs); }
		@Override public int eintersects_tpoint_tpoint(Pointer temp1, Pointer temp2) { return _meos_d.eintersects_tpoint_tpoint(temp1, temp2); }
		@Override public int etouches_tpoint_geo(Pointer temp, Pointer gs) { return _meos_d.etouches_tpoint_geo(temp, gs); }
		@Override public Pointer tcontains_geo_tpoint(Pointer gs, Pointer temp, boolean restr, boolean atvalue) { return _meos_d.tcontains_geo_tpoint(gs, temp, restr, atvalue); }
		@Override public Pointer tdisjoint_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) { return _meos_d.tdisjoint_tpoint_geo(temp, gs, restr, atvalue); }
		@Override public Pointer tdisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2, boolean restr, boolean atvalue) { return _meos_d.tdisjoint_tpoint_tpoint(temp1, temp2, restr, atvalue); }
		@Override public Pointer tdwithin_tpoint_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue) { return _meos_d.tdwithin_tpoint_geo(temp, gs, dist, restr, atvalue); }
		@Override public Pointer tdwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue) { return _meos_d.tdwithin_tpoint_tpoint(temp1, temp2, dist, restr, atvalue); }
		@Override public Pointer tintersects_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) { return _meos_d.tintersects_tpoint_geo(temp, gs, restr, atvalue); }
		@Override public Pointer tintersects_tpoint_tpoint(Pointer temp1, Pointer temp2, boolean restr, boolean atvalue) { return _meos_d.tintersects_tpoint_tpoint(temp1, temp2, restr, atvalue); }
		@Override public Pointer ttouches_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) { return _meos_d.ttouches_tpoint_geo(temp, gs, restr, atvalue); }
		@Override public Pointer tbool_tand_transfn(Pointer state, Pointer temp) { return _meos_d.tbool_tand_transfn(state, temp); }
		@Override public Pointer tbool_tor_transfn(Pointer state, Pointer temp) { return _meos_d.tbool_tor_transfn(state, temp); }
		@Override public Pointer temporal_extent_transfn(Pointer s, Pointer temp) { return _meos_d.temporal_extent_transfn(s, temp); }
		@Override public Pointer temporal_tagg_finalfn(Pointer state) { return _meos_d.temporal_tagg_finalfn(state); }
		@Override public Pointer temporal_tcount_transfn(Pointer state, Pointer temp) { return _meos_d.temporal_tcount_transfn(state, temp); }
		@Override public Pointer tfloat_tmax_transfn(Pointer state, Pointer temp) { return _meos_d.tfloat_tmax_transfn(state, temp); }
		@Override public Pointer tfloat_tmin_transfn(Pointer state, Pointer temp) { return _meos_d.tfloat_tmin_transfn(state, temp); }
		@Override public Pointer tfloat_tsum_transfn(Pointer state, Pointer temp) { return _meos_d.tfloat_tsum_transfn(state, temp); }
		@Override public Pointer tfloat_wmax_transfn(Pointer state, Pointer temp, Pointer interv) { return _meos_d.tfloat_wmax_transfn(state, temp, interv); }
		@Override public Pointer tfloat_wmin_transfn(Pointer state, Pointer temp, Pointer interv) { return _meos_d.tfloat_wmin_transfn(state, temp, interv); }
		@Override public Pointer tfloat_wsum_transfn(Pointer state, Pointer temp, Pointer interv) { return _meos_d.tfloat_wsum_transfn(state, temp, interv); }
		@Override public Pointer timestamptz_tcount_transfn(Pointer state, long t) { return _meos_d.timestamptz_tcount_transfn(state, t); }
		@Override public Pointer tint_tmax_transfn(Pointer state, Pointer temp) { return _meos_d.tint_tmax_transfn(state, temp); }
		@Override public Pointer tint_tmin_transfn(Pointer state, Pointer temp) { return _meos_d.tint_tmin_transfn(state, temp); }
		@Override public Pointer tint_tsum_transfn(Pointer state, Pointer temp) { return _meos_d.tint_tsum_transfn(state, temp); }
		@Override public Pointer tint_wmax_transfn(Pointer state, Pointer temp, Pointer interv) { return _meos_d.tint_wmax_transfn(state, temp, interv); }
		@Override public Pointer tint_wmin_transfn(Pointer state, Pointer temp, Pointer interv) { return _meos_d.tint_wmin_transfn(state, temp, interv); }
		@Override public Pointer tint_wsum_transfn(Pointer state, Pointer temp, Pointer interv) { return _meos_d.tint_wsum_transfn(state, temp, interv); }
		@Override public Pointer tnumber_extent_transfn(Pointer box, Pointer temp) { return _meos_d.tnumber_extent_transfn(box, temp); }
		@Override public Pointer tnumber_tavg_finalfn(Pointer state) { return _meos_d.tnumber_tavg_finalfn(state); }
		@Override public Pointer tnumber_tavg_transfn(Pointer state, Pointer temp) { return _meos_d.tnumber_tavg_transfn(state, temp); }
		@Override public Pointer tnumber_wavg_transfn(Pointer state, Pointer temp, Pointer interv) { return _meos_d.tnumber_wavg_transfn(state, temp, interv); }
		@Override public Pointer tpoint_extent_transfn(Pointer box, Pointer temp) { return _meos_d.tpoint_extent_transfn(box, temp); }
		@Override public Pointer tpoint_tcentroid_finalfn(Pointer state) { return _meos_d.tpoint_tcentroid_finalfn(state); }
		@Override public Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp) { return _meos_d.tpoint_tcentroid_transfn(state, temp); }
		@Override public Pointer tstzset_tcount_transfn(Pointer state, Pointer s) { return _meos_d.tstzset_tcount_transfn(state, s); }
		@Override public Pointer tstzspan_tcount_transfn(Pointer state, Pointer s) { return _meos_d.tstzspan_tcount_transfn(state, s); }
		@Override public Pointer tstzspanset_tcount_transfn(Pointer state, Pointer ss) { return _meos_d.tstzspanset_tcount_transfn(state, ss); }
		@Override public Pointer ttext_tmax_transfn(Pointer state, Pointer temp) { return _meos_d.ttext_tmax_transfn(state, temp); }
		@Override public Pointer ttext_tmin_transfn(Pointer state, Pointer temp) { return _meos_d.ttext_tmin_transfn(state, temp); }
		@Override public Pointer temporal_simplify_dp(Pointer temp, double eps_dist, boolean synchronize) { return _meos_d.temporal_simplify_dp(temp, eps_dist, synchronize); }
		@Override public Pointer temporal_simplify_max_dist(Pointer temp, double eps_dist, boolean synchronize) { return _meos_d.temporal_simplify_max_dist(temp, eps_dist, synchronize); }
		@Override public Pointer temporal_simplify_min_dist(Pointer temp, double dist) { return _meos_d.temporal_simplify_min_dist(temp, dist); }
		@Override public Pointer temporal_simplify_min_tdelta(Pointer temp, Pointer mint) { return _meos_d.temporal_simplify_min_tdelta(temp, mint); }
		@Override public Pointer temporal_tprecision(Pointer temp, Pointer duration, long origin) { return _meos_d.temporal_tprecision(temp, duration, origin); }
		@Override public Pointer temporal_tsample(Pointer temp, Pointer duration, long origin, int interp) { return _meos_d.temporal_tsample(temp, duration, origin, interp); }
		@Override public double temporal_dyntimewarp_distance(Pointer temp1, Pointer temp2) { return _meos_d.temporal_dyntimewarp_distance(temp1, temp2); }
		@Override public Pointer temporal_dyntimewarp_path(Pointer temp1, Pointer temp2, Pointer count) { return _meos_d.temporal_dyntimewarp_path(temp1, temp2, count); }
		@Override public double temporal_frechet_distance(Pointer temp1, Pointer temp2) { return _meos_d.temporal_frechet_distance(temp1, temp2); }
		@Override public Pointer temporal_frechet_path(Pointer temp1, Pointer temp2, Pointer count) { return _meos_d.temporal_frechet_path(temp1, temp2, count); }
		@Override public double temporal_hausdorff_distance(Pointer temp1, Pointer temp2) { return _meos_d.temporal_hausdorff_distance(temp1, temp2); }
		@Override public double float_bucket(double value, double size, double origin) { return _meos_d.float_bucket(value, size, origin); }
		@Override public Pointer floatspan_bucket_list(Pointer bounds, double size, double origin, Pointer count) { return _meos_d.floatspan_bucket_list(bounds, size, origin, count); }
		@Override public int int_bucket(int value, int size, int origin) { return _meos_d.int_bucket(value, size, origin); }
		@Override public Pointer intspan_bucket_list(Pointer bounds, int size, int origin, Pointer count) { return _meos_d.intspan_bucket_list(bounds, size, origin, count); }
		@Override public Pointer stbox_tile(Pointer point, long t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean hast) { return _meos_d.stbox_tile(point, t, xsize, ysize, zsize, duration, sorigin, torigin, hast); }
		@Override public Pointer stbox_tile_list(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean border_inc, Pointer count) { return _meos_d.stbox_tile_list(bounds, xsize, ysize, zsize, duration, sorigin, torigin, border_inc, count); }
		@Override public Pointer temporal_time_split(Pointer temp, Pointer duration, long torigin, Pointer time_buckets, Pointer count) { return _meos_d.temporal_time_split(temp, duration, torigin, time_buckets, count); }
		@Override public Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer value_buckets, Pointer count) { return _meos_d.tfloat_value_split(temp, size, origin, value_buckets, count); }
		@Override public Pointer tfloat_value_time_split(Pointer temp, double size, Pointer duration, double vorigin, long torigin, Pointer value_buckets, Pointer time_buckets, Pointer count) { return _meos_d.tfloat_value_time_split(temp, size, duration, vorigin, torigin, value_buckets, time_buckets, count); }
		@Override public Pointer tfloatbox_tile(double value, long t, double vsize, Pointer duration, double vorigin, long torigin) { return _meos_d.tfloatbox_tile(value, t, vsize, duration, vorigin, torigin); }
		@Override public Pointer tfloatbox_tile_list(Pointer box, double xsize, Pointer duration, double xorigin, long torigin, Pointer count) { return _meos_d.tfloatbox_tile_list(box, xsize, duration, xorigin, torigin, count); }
		@Override public long timestamptz_bucket(long timestamp, Pointer duration, long origin) { return _meos_d.timestamptz_bucket(timestamp, duration, origin); }
		@Override public Pointer tint_value_split(Pointer temp, int size, int origin, Pointer value_buckets, Pointer count) { return _meos_d.tint_value_split(temp, size, origin, value_buckets, count); }
		@Override public Pointer tint_value_time_split(Pointer temp, int size, Pointer duration, int vorigin, long torigin, Pointer value_buckets, Pointer time_buckets, Pointer count) { return _meos_d.tint_value_time_split(temp, size, duration, vorigin, torigin, value_buckets, time_buckets, count); }
		@Override public Pointer tintbox_tile(int value, long t, int vsize, Pointer duration, int vorigin, long torigin) { return _meos_d.tintbox_tile(value, t, vsize, duration, vorigin, torigin); }
		@Override public Pointer tintbox_tile_list(Pointer box, int xsize, Pointer duration, int xorigin, long torigin, Pointer count) { return _meos_d.tintbox_tile_list(box, xsize, duration, xorigin, torigin, count); }
		@Override public Pointer tpoint_space_split(Pointer temp, float xsize, float ysize, float zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_buckets, Pointer count) { return _meos_d.tpoint_space_split(temp, xsize, ysize, zsize, sorigin, bitmatrix, border_inc, space_buckets, count); }
		@Override public Pointer tpoint_space_time_split(Pointer temp, float xsize, float ysize, float zsize, Pointer duration, Pointer sorigin, long torigin, boolean bitmatrix, boolean border_inc, Pointer space_buckets, Pointer time_buckets, Pointer count) { return _meos_d.tpoint_space_time_split(temp, xsize, ysize, zsize, duration, sorigin, torigin, bitmatrix, border_inc, space_buckets, time_buckets, count); }
		@Override public Pointer tstzspan_bucket_list(Pointer bounds, Pointer duration, long origin, Pointer count) { return _meos_d.tstzspan_bucket_list(bounds, duration, origin, count); }
	}

	@SuppressWarnings("unused")
	public static int geo_get_srid(Pointer g) {
		return _meos_a.geo_get_srid(g);
	}
	
	@SuppressWarnings("unused")
	public static void meos_error(int errlevel, int errcode, String format, Pointer args) {
		_meos_a.meos_error(errlevel, errcode, format, args);
	}
	
	@SuppressWarnings("unused")
	public static int meos_errno() {
		return _meos_a.meos_errno();
	}
	
	@SuppressWarnings("unused")
	public static int meos_errno_set(int err) {
		return _meos_a.meos_errno_set(err);
	}
	
	@SuppressWarnings("unused")
	public static int meos_errno_restore(int err) {
		return _meos_a.meos_errno_restore(err);
	}
	
	@SuppressWarnings("unused")
	public static int meos_errno_reset() {
		return _meos_a.meos_errno_reset();
	}
	
	@SuppressWarnings("unused")
	public static void meos_initialize_timezone(String name) {
		_meos_a.meos_initialize_timezone(name);
	}
	
	@SuppressWarnings("unused")
	public static void meos_initialize_error_handler(error_handler_fn err_handler) {
		_meos_a.meos_initialize_error_handler(err_handler);
	}
	
	@SuppressWarnings("unused")
	public static void meos_finalize_timezone() {
		_meos_a.meos_finalize_timezone();
	}
	
	@SuppressWarnings("unused")
	public static boolean meos_set_datestyle(String newval, Pointer extra) {
		return _meos_a.meos_set_datestyle(newval, extra);
	}
	
	@SuppressWarnings("unused")
	public static boolean meos_set_intervalstyle(String newval, int extra) {
		return _meos_a.meos_set_intervalstyle(newval, extra);
	}
	
	@SuppressWarnings("unused")
	public static String meos_get_datestyle() {
		return _meos_a.meos_get_datestyle();
	}
	
	@SuppressWarnings("unused")
	public static String meos_get_intervalstyle() {
		return _meos_a.meos_get_intervalstyle();
	}
	
	@SuppressWarnings("unused")
	public static void meos_initialize(String tz_str, error_handler_fn err_handler) {
		_meos_a.meos_initialize(tz_str, err_handler);
	}
	
	@SuppressWarnings("unused")
	public static void meos_finalize() {
		_meos_a.meos_finalize();
	}
	
	@SuppressWarnings("unused")
	public static int add_date_int(int d, int days) {
		return _meos_a.add_date_int(d, days);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_interval_interval(Pointer interv1, Pointer interv2) {
		return _meos_a.add_interval_interval(interv1, interv2);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime add_timestamptz_interval(OffsetDateTime t, Pointer interv) {
		var t_new = t.toEpochSecond();
		var result = _meos_a.add_timestamptz_interval(t_new, interv);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static boolean bool_in(String str) {
		return _meos_a.bool_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String bool_out(boolean b) {
		return _meos_a.bool_out(b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer cstring2text(String str) {
		return _meos_a.cstring2text(str);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime date_to_timestamptz(int d) {
		var result = _meos_a.date_to_timestamptz(d);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_date_date(int d1, int d2) {
		return _meos_a.minus_date_date(d1, d2);
	}
	
	@SuppressWarnings("unused")
	public static int minus_date_int(int d, int days) {
		return _meos_a.minus_date_int(d, days);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime minus_timestamptz_interval(OffsetDateTime t, Pointer interv) {
		var t_new = t.toEpochSecond();
		var result = _meos_a.minus_timestamptz_interval(t_new, interv);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_timestamptz(OffsetDateTime t1, OffsetDateTime t2) {
		var t1_new = t1.toEpochSecond();
		var t2_new = t2.toEpochSecond();
		return _meos_a.minus_timestamptz_timestamptz(t1_new, t2_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_interval_double(Pointer interv, double factor) {
		return _meos_a.mult_interval_double(interv, factor);
	}
	
	@SuppressWarnings("unused")
	public static int pg_date_in(String str) {
		return _meos_a.pg_date_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String pg_date_out(int d) {
		return _meos_a.pg_date_out(d);
	}
	
	@SuppressWarnings("unused")
	public static int pg_interval_cmp(Pointer interv1, Pointer interv2) {
		return _meos_a.pg_interval_cmp(interv1, interv2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_in(String str, int typmod) {
		return _meos_a.pg_interval_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs) {
		return _meos_a.pg_interval_make(years, months, weeks, days, hours, mins, secs);
	}
	
	@SuppressWarnings("unused")
	public static String pg_interval_out(Pointer interv) {
		return _meos_a.pg_interval_out(interv);
	}
	
	@SuppressWarnings("unused")
	public static long pg_time_in(String str, int typmod) {
		return _meos_a.pg_time_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static String pg_time_out(long t) {
		return _meos_a.pg_time_out(t);
	}
	
	@SuppressWarnings("unused")
	public static LocalDateTime pg_timestamp_in(String str, int typmod) {
		var result = _meos_a.pg_timestamp_in(str, typmod);
		return LocalDateTime.ofEpochSecond(result, 0, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String pg_timestamp_out(LocalDateTime t) {
		var t_new = t.toEpochSecond(ZoneOffset.UTC);
		return _meos_a.pg_timestamp_out(t_new);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime pg_timestamptz_in(String str, int typmod) {
		var result = _meos_a.pg_timestamptz_in(str, typmod);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String pg_timestamptz_out(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.pg_timestamptz_out(t_new);
	}
	
	@SuppressWarnings("unused")
	public static String text2cstring(Pointer txt) {
		return _meos_a.text2cstring(txt);
	}
	
	@SuppressWarnings("unused")
	public static int text_cmp(Pointer txt1, Pointer txt2) {
		return _meos_a.text_cmp(txt1, txt2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_copy(Pointer txt) {
		return _meos_a.text_copy(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_initcap(Pointer txt) {
		return _meos_a.text_initcap(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_lower(Pointer txt) {
		return _meos_a.text_lower(txt);
	}
	
	@SuppressWarnings("unused")
	public static String text_out(Pointer txt) {
		return _meos_a.text_out(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_upper(Pointer txt) {
		return _meos_a.text_upper(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_text_text(Pointer txt1, Pointer txt2) {
		return _meos_a.textcat_text_text(txt1, txt2);
	}
	
	@SuppressWarnings("unused")
	public static int timestamptz_to_date(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.timestamptz_to_date(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_as_ewkb(Pointer gs, String endian) {
		return _meos_a.geo_as_ewkb(gs, endian);
	}
	
	@SuppressWarnings("unused")
	public static String geo_as_ewkt(Pointer gs, int precision) {
		return _meos_a.geo_as_ewkt(gs, precision);
	}
	
	@SuppressWarnings("unused")
	public static String geo_as_geojson(Pointer gs, int option, int precision, String srs) {
		return _meos_a.geo_as_geojson(gs, option, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static String geo_as_hexewkb(Pointer gs, String endian) {
		return _meos_a.geo_as_hexewkb(gs, endian);
	}
	
	@SuppressWarnings("unused")
	public static String geo_as_text(Pointer gs, int precision) {
		return _meos_a.geo_as_text(gs, precision);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_from_ewkb(Pointer bytea_wkb, int srid) {
		return _meos_a.geo_from_ewkb(bytea_wkb, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_from_geojson(String geojson) {
		return _meos_a.geo_from_geojson(geojson);
	}
	
	@SuppressWarnings("unused")
	public static String geo_out(Pointer gs) {
		return _meos_a.geo_out(gs);
	}
	
	@SuppressWarnings("unused")
	public static boolean geo_same(Pointer gs1, Pointer gs2) {
		return _meos_a.geo_same(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geography_from_hexewkb(String wkt) {
		return _meos_a.geography_from_hexewkb(wkt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geography_from_text(String wkt, int srid) {
		return _meos_a.geography_from_text(wkt, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geometry_from_hexewkb(String wkt) {
		return _meos_a.geometry_from_hexewkb(wkt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geometry_from_text(String wkt, int srid) {
		return _meos_a.geometry_from_text(wkt, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pgis_geography_in(String str, int typmod) {
		return _meos_a.pgis_geography_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pgis_geometry_in(String str, int typmod) {
		return _meos_a.pgis_geometry_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_in(String str) {
		return _meos_a.bigintset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String bigintset_out(Pointer set) {
		return _meos_a.bigintset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspan_in(String str) {
		return _meos_a.bigintspan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String bigintspan_out(Pointer s) {
		return _meos_a.bigintspan_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspanset_in(String str) {
		return _meos_a.bigintspanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String bigintspanset_out(Pointer ss) {
		return _meos_a.bigintspanset_out(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_in(String str) {
		return _meos_a.dateset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String dateset_out(Pointer s) {
		return _meos_a.dateset_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_in(String str) {
		return _meos_a.datespan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String datespan_out(Pointer s) {
		return _meos_a.datespan_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_in(String str) {
		return _meos_a.datespanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String datespanset_out(Pointer ss) {
		return _meos_a.datespanset_out(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_in(String str) {
		return _meos_a.floatset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String floatset_out(Pointer set, int maxdd) {
		return _meos_a.floatset_out(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_in(String str) {
		return _meos_a.floatspan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String floatspan_out(Pointer s, int maxdd) {
		return _meos_a.floatspan_out(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_in(String str) {
		return _meos_a.floatspanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String floatspanset_out(Pointer ss, int maxdd) {
		return _meos_a.floatspanset_out(ss, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geogset_in(String str) {
		return _meos_a.geogset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geomset_in(String str) {
		return _meos_a.geomset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String geoset_as_ewkt(Pointer set, int maxdd) {
		return _meos_a.geoset_as_ewkt(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String geoset_as_text(Pointer set, int maxdd) {
		return _meos_a.geoset_as_text(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String geoset_out(Pointer set, int maxdd) {
		return _meos_a.geoset_out(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_in(String str) {
		return _meos_a.intset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String intset_out(Pointer set) {
		return _meos_a.intset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_in(String str) {
		return _meos_a.intspan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String intspan_out(Pointer s) {
		return _meos_a.intspan_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_in(String str) {
		return _meos_a.intspanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String intspanset_out(Pointer ss) {
		return _meos_a.intspanset_out(ss);
	}
	
	@SuppressWarnings("unused")
	public static String set_as_hexwkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_a.set_as_hexwkb(s, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_as_wkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_a.set_as_wkb(s, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_from_hexwkb(String hexwkb) {
		return _meos_a.set_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_from_wkb(Pointer wkb, long size) {
		return _meos_a.set_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static String span_as_hexwkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_a.span_as_hexwkb(s, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_as_wkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_a.span_as_wkb(s, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_from_hexwkb(String hexwkb) {
		return _meos_a.span_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_from_wkb(Pointer wkb, long size) {
		return _meos_a.span_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static String spanset_as_hexwkb(Pointer ss, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_a.spanset_as_hexwkb(ss, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_as_wkb(Pointer ss, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_a.spanset_as_wkb(ss, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_from_hexwkb(String hexwkb) {
		return _meos_a.spanset_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_from_wkb(Pointer wkb, long size) {
		return _meos_a.spanset_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_in(String str) {
		return _meos_a.textset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String textset_out(Pointer set) {
		return _meos_a.textset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_in(String str) {
		return _meos_a.tstzset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tstzset_out(Pointer set) {
		return _meos_a.tstzset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_in(String str) {
		return _meos_a.tstzspan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tstzspan_out(Pointer s) {
		return _meos_a.tstzspan_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_in(String str) {
		return _meos_a.tstzspanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tstzspanset_out(Pointer ss) {
		return _meos_a.tstzspanset_out(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_make(Pointer values, int count) {
		return _meos_a.bigintset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc) {
		return _meos_a.bigintspan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_make(Pointer values, int count) {
		return _meos_a.dateset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) {
		return _meos_a.datespan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_make(Pointer values, int count) {
		return _meos_a.floatset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc) {
		return _meos_a.floatspan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_make(Pointer values, int count) {
		return _meos_a.geoset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_make(Pointer values, int count) {
		return _meos_a.intset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) {
		return _meos_a.intspan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_copy(Pointer s) {
		return _meos_a.set_copy(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_copy(Pointer s) {
		return _meos_a.span_copy(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_copy(Pointer ss) {
		return _meos_a.spanset_copy(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_make(Pointer spans, int count, boolean normalize, boolean order) {
		return _meos_a.spanset_make(spans, count, normalize, order);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_make(Pointer values, int count) {
		return _meos_a.textset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_make(Pointer values, int count) {
		return _meos_a.tstzset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_make(OffsetDateTime lower, OffsetDateTime upper, boolean lower_inc, boolean upper_inc) {
		var lower_new = lower.toEpochSecond();
		var upper_new = upper.toEpochSecond();
		return _meos_a.tstzspan_make(lower_new, upper_new, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_to_set(long i) {
		return _meos_a.bigint_to_set(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_to_span(int i) {
		return _meos_a.bigint_to_span(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_to_spanset(int i) {
		return _meos_a.bigint_to_spanset(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_to_set(int d) {
		return _meos_a.date_to_set(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_to_span(int d) {
		return _meos_a.date_to_span(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_to_spanset(int d) {
		return _meos_a.date_to_spanset(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_to_tstzset(Pointer s) {
		return _meos_a.dateset_to_tstzset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_to_tstzspan(Pointer s) {
		return _meos_a.datespan_to_tstzspan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_to_tstzspanset(Pointer ss) {
		return _meos_a.datespanset_to_tstzspanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_to_set(double d) {
		return _meos_a.float_to_set(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_to_span(double d) {
		return _meos_a.float_to_span(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_to_spanset(double d) {
		return _meos_a.float_to_spanset(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_to_intset(Pointer s) {
		return _meos_a.floatset_to_intset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_to_intspan(Pointer s) {
		return _meos_a.floatspan_to_intspan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_to_intspanset(Pointer ss) {
		return _meos_a.floatspanset_to_intspanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_to_set(Pointer gs) {
		return _meos_a.geo_to_set(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_to_set(int i) {
		return _meos_a.int_to_set(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_to_span(int i) {
		return _meos_a.int_to_span(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_to_spanset(int i) {
		return _meos_a.int_to_spanset(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_to_floatset(Pointer s) {
		return _meos_a.intset_to_floatset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_to_floatspan(Pointer s) {
		return _meos_a.intspan_to_floatspan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_to_floatspanset(Pointer ss) {
		return _meos_a.intspanset_to_floatspanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_to_spanset(Pointer s) {
		return _meos_a.set_to_spanset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_to_spanset(Pointer s) {
		return _meos_a.span_to_spanset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_to_set(Pointer txt) {
		return _meos_a.text_to_set(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_set(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.timestamptz_to_set(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_span(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.timestamptz_to_span(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_spanset(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.timestamptz_to_spanset(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_to_dateset(Pointer s) {
		return _meos_a.tstzset_to_dateset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_to_datespan(Pointer s) {
		return _meos_a.tstzspan_to_datespan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_to_datespanset(Pointer ss) {
		return _meos_a.tstzspanset_to_datespanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static long bigintset_end_value(Pointer s) {
		return _meos_a.bigintset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintset_start_value(Pointer s) {
		return _meos_a.bigintset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.bigintset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_values(Pointer s) {
		return _meos_a.bigintset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspan_lower(Pointer s) {
		return _meos_a.bigintspan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspan_upper(Pointer s) {
		return _meos_a.bigintspan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspan_width(Pointer s) {
		return _meos_a.bigintspan_width(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspanset_lower(Pointer ss) {
		return _meos_a.bigintspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspanset_upper(Pointer ss) {
		return _meos_a.bigintspanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspanset_width(Pointer ss, boolean boundspan) {
		return _meos_a.bigintspanset_width(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static int dateset_end_value(Pointer s) {
		return _meos_a.dateset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static int dateset_start_value(Pointer s) {
		return _meos_a.dateset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.dateset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_values(Pointer s) {
		return _meos_a.dateset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_duration(Pointer s) {
		return _meos_a.datespan_duration(s);
	}
	
	@SuppressWarnings("unused")
	public static int datespan_lower(Pointer s) {
		return _meos_a.datespan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static int datespan_upper(Pointer s) {
		return _meos_a.datespan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_date_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.datespanset_date_n(ss, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_dates(Pointer ss) {
		return _meos_a.datespanset_dates(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_duration(Pointer ss, boolean boundspan) {
		return _meos_a.datespanset_duration(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static int datespanset_end_date(Pointer ss) {
		return _meos_a.datespanset_end_date(ss);
	}
	
	@SuppressWarnings("unused")
	public static int datespanset_num_dates(Pointer ss) {
		return _meos_a.datespanset_num_dates(ss);
	}
	
	@SuppressWarnings("unused")
	public static int datespanset_start_date(Pointer ss) {
		return _meos_a.datespanset_start_date(ss);
	}
	
	@SuppressWarnings("unused")
	public static double floatset_end_value(Pointer s) {
		return _meos_a.floatset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatset_start_value(Pointer s) {
		return _meos_a.floatset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.floatset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_values(Pointer s) {
		return _meos_a.floatset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatspan_lower(Pointer s) {
		return _meos_a.floatspan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatspan_upper(Pointer s) {
		return _meos_a.floatspan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatspan_width(Pointer s) {
		return _meos_a.floatspan_width(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatspanset_lower(Pointer ss) {
		return _meos_a.floatspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static double floatspanset_upper(Pointer ss) {
		return _meos_a.floatspanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static double floatspanset_width(Pointer ss, boolean boundspan) {
		return _meos_a.floatspanset_width(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_end_value(Pointer s) {
		return _meos_a.geoset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static int geoset_srid(Pointer s) {
		return _meos_a.geoset_srid(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_start_value(Pointer s) {
		return _meos_a.geoset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.geoset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_values(Pointer s) {
		return _meos_a.geoset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static int intset_end_value(Pointer s) {
		return _meos_a.intset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static int intset_start_value(Pointer s) {
		return _meos_a.intset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.intset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_values(Pointer s) {
		return _meos_a.intset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static int intspan_lower(Pointer s) {
		return _meos_a.intspan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static int intspan_upper(Pointer s) {
		return _meos_a.intspan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static int intspan_width(Pointer s) {
		return _meos_a.intspan_width(s);
	}
	
	@SuppressWarnings("unused")
	public static int intspanset_lower(Pointer ss) {
		return _meos_a.intspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static int intspanset_upper(Pointer ss) {
		return _meos_a.intspanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static int intspanset_width(Pointer ss, boolean boundspan) {
		return _meos_a.intspanset_width(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static int set_hash(Pointer s) {
		return _meos_a.set_hash(s);
	}
	
	@SuppressWarnings("unused")
	public static long set_hash_extended(Pointer s, long seed) {
		return _meos_a.set_hash_extended(s, seed);
	}
	
	@SuppressWarnings("unused")
	public static int set_num_values(Pointer s) {
		return _meos_a.set_num_values(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_to_span(Pointer s) {
		return _meos_a.set_to_span(s);
	}
	
	@SuppressWarnings("unused")
	public static int span_hash(Pointer s) {
		return _meos_a.span_hash(s);
	}
	
	@SuppressWarnings("unused")
	public static long span_hash_extended(Pointer s, long seed) {
		return _meos_a.span_hash_extended(s, seed);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_lower_inc(Pointer s) {
		return _meos_a.span_lower_inc(s);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_upper_inc(Pointer s) {
		return _meos_a.span_upper_inc(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_end_span(Pointer ss) {
		return _meos_a.spanset_end_span(ss);
	}
	
	@SuppressWarnings("unused")
	public static int spanset_hash(Pointer ss) {
		return _meos_a.spanset_hash(ss);
	}
	
	@SuppressWarnings("unused")
	public static long spanset_hash_extended(Pointer ss, long seed) {
		return _meos_a.spanset_hash_extended(ss, seed);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_lower_inc(Pointer ss) {
		return _meos_a.spanset_lower_inc(ss);
	}
	
	@SuppressWarnings("unused")
	public static int spanset_num_spans(Pointer ss) {
		return _meos_a.spanset_num_spans(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_span(Pointer ss) {
		return _meos_a.spanset_span(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_span_n(Pointer ss, int i) {
		return _meos_a.spanset_span_n(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_spans(Pointer ss) {
		return _meos_a.spanset_spans(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_start_span(Pointer ss) {
		return _meos_a.spanset_start_span(ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_upper_inc(Pointer ss) {
		return _meos_a.spanset_upper_inc(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_end_value(Pointer s) {
		return _meos_a.textset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_start_value(Pointer s) {
		return _meos_a.textset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.textset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_values(Pointer s) {
		return _meos_a.textset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzset_end_value(Pointer s) {
		var result = _meos_a.tstzset_end_value(s);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzset_start_value(Pointer s) {
		var result = _meos_a.tstzset_start_value(s);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.tstzset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_values(Pointer s) {
		return _meos_a.tstzset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_duration(Pointer s) {
		return _meos_a.tstzspan_duration(s);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspan_lower(Pointer s) {
		var result = _meos_a.tstzspan_lower(s);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspan_upper(Pointer s) {
		var result = _meos_a.tstzspan_upper(s);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_duration(Pointer ss, boolean boundspan) {
		return _meos_a.tstzspanset_duration(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_end_timestamptz(Pointer ss) {
		var result = _meos_a.tstzspanset_end_timestamptz(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_lower(Pointer ss) {
		var result = _meos_a.tstzspanset_lower(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static int tstzspanset_num_timestamps(Pointer ss) {
		return _meos_a.tstzspanset_num_timestamps(ss);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_start_timestamptz(Pointer ss) {
		var result = _meos_a.tstzspanset_start_timestamptz(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_timestamptz_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.tstzspanset_timestamptz_n(ss, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_timestamps(Pointer ss) {
		return _meos_a.tstzspanset_timestamps(ss);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_upper(Pointer ss) {
		var result = _meos_a.tstzspanset_upper(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		return _meos_a.bigintset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		return _meos_a.bigintspan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth) {
		return _meos_a.bigintspanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return _meos_a.dateset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return _meos_a.datespan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		return _meos_a.datespanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_ceil(Pointer s) {
		return _meos_a.floatset_ceil(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_floor(Pointer s) {
		return _meos_a.floatset_floor(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_degrees(Pointer s, boolean normalize) {
		return _meos_a.floatset_degrees(s, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_radians(Pointer s) {
		return _meos_a.floatset_radians(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_round(Pointer s, int maxdd) {
		return _meos_a.floatset_round(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		return _meos_a.floatset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_ceil(Pointer s) {
		return _meos_a.floatspan_ceil(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_floor(Pointer s) {
		return _meos_a.floatspan_floor(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_round(Pointer s, int maxdd) {
		return _meos_a.floatspan_round(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		return _meos_a.floatspan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_ceil(Pointer ss) {
		return _meos_a.floatspanset_ceil(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_floor(Pointer ss) {
		return _meos_a.floatspanset_floor(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_round(Pointer ss, int maxdd) {
		return _meos_a.floatspanset_round(ss, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth) {
		return _meos_a.floatspanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_round(Pointer s, int maxdd) {
		return _meos_a.geoset_round(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_set_srid(Pointer s, int srid) {
		return _meos_a.geoset_set_srid(s, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_transform(Pointer s, int srid) {
		return _meos_a.geoset_transform(s, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward) {
		return _meos_a.geoset_transform_pipeline(s, pipelinestr, srid, is_forward);
	}
	
	@SuppressWarnings("unused")
	public static Pointer point_transform(Pointer gs, int srid) {
		return _meos_a.point_transform(gs, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer point_transform_pipeline(Pointer gs, String pipelinestr, int srid, boolean is_forward) {
		return _meos_a.point_transform_pipeline(gs, pipelinestr, srid, is_forward);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return _meos_a.intset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return _meos_a.intspan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		return _meos_a.intspanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_initcap(Pointer s) {
		return _meos_a.textset_initcap(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_lower(Pointer s) {
		return _meos_a.textset_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_upper(Pointer s) {
		return _meos_a.textset_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_textset_text(Pointer s, Pointer txt) {
		return _meos_a.textcat_textset_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_text_textset(Pointer txt, Pointer s) {
		return _meos_a.textcat_text_textset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_tprecision(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var result = _meos_a.timestamptz_tprecision(t_new, duration, torigin_new);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_shift_scale(Pointer s, Pointer shift, Pointer duration) {
		return _meos_a.tstzset_shift_scale(s, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_a.tstzset_tprecision(s, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_shift_scale(Pointer s, Pointer shift, Pointer duration) {
		return _meos_a.tstzspan_shift_scale(s, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_a.tstzspan_tprecision(s, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_shift_scale(Pointer ss, Pointer shift, Pointer duration) {
		return _meos_a.tstzspanset_shift_scale(ss, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_tprecision(Pointer ss, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_a.tstzspanset_tprecision(ss, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static int set_cmp(Pointer s1, Pointer s2) {
		return _meos_a.set_cmp(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_eq(Pointer s1, Pointer s2) {
		return _meos_a.set_eq(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_ge(Pointer s1, Pointer s2) {
		return _meos_a.set_ge(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_gt(Pointer s1, Pointer s2) {
		return _meos_a.set_gt(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_le(Pointer s1, Pointer s2) {
		return _meos_a.set_le(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_lt(Pointer s1, Pointer s2) {
		return _meos_a.set_lt(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_ne(Pointer s1, Pointer s2) {
		return _meos_a.set_ne(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int span_cmp(Pointer s1, Pointer s2) {
		return _meos_a.span_cmp(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_eq(Pointer s1, Pointer s2) {
		return _meos_a.span_eq(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_ge(Pointer s1, Pointer s2) {
		return _meos_a.span_ge(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_gt(Pointer s1, Pointer s2) {
		return _meos_a.span_gt(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_le(Pointer s1, Pointer s2) {
		return _meos_a.span_le(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_lt(Pointer s1, Pointer s2) {
		return _meos_a.span_lt(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_ne(Pointer s1, Pointer s2) {
		return _meos_a.span_ne(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int spanset_cmp(Pointer ss1, Pointer ss2) {
		return _meos_a.spanset_cmp(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_eq(Pointer ss1, Pointer ss2) {
		return _meos_a.spanset_eq(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_ge(Pointer ss1, Pointer ss2) {
		return _meos_a.spanset_ge(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_gt(Pointer ss1, Pointer ss2) {
		return _meos_a.spanset_gt(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_le(Pointer ss1, Pointer ss2) {
		return _meos_a.spanset_le(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_lt(Pointer ss1, Pointer ss2) {
		return _meos_a.spanset_lt(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_ne(Pointer ss1, Pointer ss2) {
		return _meos_a.spanset_ne(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_bigint(Pointer s, long i) {
		return _meos_a.adjacent_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_date(Pointer s, int d) {
		return _meos_a.adjacent_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_float(Pointer s, double d) {
		return _meos_a.adjacent_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_int(Pointer s, int i) {
		return _meos_a.adjacent_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_span(Pointer s1, Pointer s2) {
		return _meos_a.adjacent_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_spanset(Pointer s, Pointer ss) {
		return _meos_a.adjacent_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.adjacent_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_bigint(Pointer ss, long i) {
		return _meos_a.adjacent_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_date(Pointer ss, int d) {
		return _meos_a.adjacent_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_float(Pointer ss, double d) {
		return _meos_a.adjacent_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_int(Pointer ss, int i) {
		return _meos_a.adjacent_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.adjacent_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_span(Pointer ss, Pointer s) {
		return _meos_a.adjacent_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_a.adjacent_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_bigint_set(long i, Pointer s) {
		return _meos_a.contained_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_bigint_span(long i, Pointer s) {
		return _meos_a.contained_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_bigint_spanset(long i, Pointer ss) {
		return _meos_a.contained_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_date_set(int d, Pointer s) {
		return _meos_a.contained_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_date_span(int d, Pointer s) {
		return _meos_a.contained_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_date_spanset(int d, Pointer ss) {
		return _meos_a.contained_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_set(double d, Pointer s) {
		return _meos_a.contained_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_span(double d, Pointer s) {
		return _meos_a.contained_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_spanset(double d, Pointer ss) {
		return _meos_a.contained_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_geo_set(Pointer gs, Pointer s) {
		return _meos_a.contained_geo_set(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_set(int i, Pointer s) {
		return _meos_a.contained_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_span(int i, Pointer s) {
		return _meos_a.contained_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_spanset(int i, Pointer ss) {
		return _meos_a.contained_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_set_set(Pointer s1, Pointer s2) {
		return _meos_a.contained_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_span_span(Pointer s1, Pointer s2) {
		return _meos_a.contained_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_span_spanset(Pointer s, Pointer ss) {
		return _meos_a.contained_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_spanset_span(Pointer ss, Pointer s) {
		return _meos_a.contained_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_a.contained_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_text_set(Pointer txt, Pointer s) {
		return _meos_a.contained_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_a.contained_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_a.contained_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return _meos_a.contained_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_bigint(Pointer s, long i) {
		return _meos_a.contains_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_date(Pointer s, int d) {
		return _meos_a.contains_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_float(Pointer s, double d) {
		return _meos_a.contains_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_geo(Pointer s, Pointer gs) {
		return _meos_a.contains_set_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_int(Pointer s, int i) {
		return _meos_a.contains_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_set(Pointer s1, Pointer s2) {
		return _meos_a.contains_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_text(Pointer s, Pointer t) {
		return _meos_a.contains_set_text(s, t);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.contains_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_bigint(Pointer s, long i) {
		return _meos_a.contains_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_date(Pointer s, int d) {
		return _meos_a.contains_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_float(Pointer s, double d) {
		return _meos_a.contains_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_int(Pointer s, int i) {
		return _meos_a.contains_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_span(Pointer s1, Pointer s2) {
		return _meos_a.contains_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_spanset(Pointer s, Pointer ss) {
		return _meos_a.contains_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.contains_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_bigint(Pointer ss, long i) {
		return _meos_a.contains_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_date(Pointer ss, int d) {
		return _meos_a.contains_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_float(Pointer ss, double d) {
		return _meos_a.contains_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_int(Pointer ss, int i) {
		return _meos_a.contains_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_span(Pointer ss, Pointer s) {
		return _meos_a.contains_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_a.contains_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_a.contains_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_set_set(Pointer s1, Pointer s2) {
		return _meos_a.overlaps_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_span_span(Pointer s1, Pointer s2) {
		return _meos_a.overlaps_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_span_spanset(Pointer s, Pointer ss) {
		return _meos_a.overlaps_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_span(Pointer ss, Pointer s) {
		return _meos_a.overlaps_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_a.overlaps_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_date_set(int d, Pointer s) {
		return _meos_a.after_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_date_span(int d, Pointer s) {
		return _meos_a.after_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_date_spanset(int d, Pointer ss) {
		return _meos_a.after_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_set_date(Pointer s, int d) {
		return _meos_b.after_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.after_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_span_date(Pointer s, int d) {
		return _meos_b.after_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.after_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_spanset_date(Pointer ss, int d) {
		return _meos_b.after_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.after_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.after_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.after_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return _meos_b.after_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_date_set(int d, Pointer s) {
		return _meos_b.before_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_date_span(int d, Pointer s) {
		return _meos_b.before_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_date_spanset(int d, Pointer ss) {
		return _meos_b.before_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_set_date(Pointer s, int d) {
		return _meos_b.before_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.before_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_span_date(Pointer s, int d) {
		return _meos_b.before_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.before_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_spanset_date(Pointer ss, int d) {
		return _meos_b.before_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.before_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.before_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.before_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return _meos_b.before_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_set(long i, Pointer s) {
		return _meos_b.left_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_span(long i, Pointer s) {
		return _meos_b.left_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_spanset(long i, Pointer ss) {
		return _meos_b.left_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_set(double d, Pointer s) {
		return _meos_b.left_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_span(double d, Pointer s) {
		return _meos_b.left_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_spanset(double d, Pointer ss) {
		return _meos_b.left_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_set(int i, Pointer s) {
		return _meos_b.left_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_span(int i, Pointer s) {
		return _meos_b.left_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_spanset(int i, Pointer ss) {
		return _meos_b.left_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_bigint(Pointer s, long i) {
		return _meos_b.left_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_float(Pointer s, double d) {
		return _meos_b.left_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_int(Pointer s, int i) {
		return _meos_b.left_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_set(Pointer s1, Pointer s2) {
		return _meos_b.left_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_text(Pointer s, Pointer txt) {
		return _meos_b.left_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_bigint(Pointer s, long i) {
		return _meos_b.left_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_float(Pointer s, double d) {
		return _meos_b.left_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_int(Pointer s, int i) {
		return _meos_b.left_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_span(Pointer s1, Pointer s2) {
		return _meos_b.left_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_spanset(Pointer s, Pointer ss) {
		return _meos_b.left_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_bigint(Pointer ss, long i) {
		return _meos_b.left_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_float(Pointer ss, double d) {
		return _meos_b.left_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_int(Pointer ss, int i) {
		return _meos_b.left_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_span(Pointer ss, Pointer s) {
		return _meos_b.left_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_b.left_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_text_set(Pointer txt, Pointer s) {
		return _meos_b.left_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_date_set(int d, Pointer s) {
		return _meos_b.overafter_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_date_span(int d, Pointer s) {
		return _meos_b.overafter_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_date_spanset(int d, Pointer ss) {
		return _meos_b.overafter_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_set_date(Pointer s, int d) {
		return _meos_b.overafter_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.overafter_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_span_date(Pointer s, int d) {
		return _meos_b.overafter_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.overafter_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_spanset_date(Pointer ss, int d) {
		return _meos_b.overafter_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.overafter_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.overafter_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.overafter_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return _meos_b.overafter_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_date_set(int d, Pointer s) {
		return _meos_b.overbefore_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_date_span(int d, Pointer s) {
		return _meos_b.overbefore_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_date_spanset(int d, Pointer ss) {
		return _meos_b.overbefore_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_set_date(Pointer s, int d) {
		return _meos_b.overbefore_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.overbefore_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_span_date(Pointer s, int d) {
		return _meos_b.overbefore_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.overbefore_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_spanset_date(Pointer ss, int d) {
		return _meos_b.overbefore_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.overbefore_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.overbefore_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.overbefore_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return _meos_b.overbefore_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_set(long i, Pointer s) {
		return _meos_b.overleft_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_span(long i, Pointer s) {
		return _meos_b.overleft_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_spanset(long i, Pointer ss) {
		return _meos_b.overleft_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_set(double d, Pointer s) {
		return _meos_b.overleft_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_span(double d, Pointer s) {
		return _meos_b.overleft_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_spanset(double d, Pointer ss) {
		return _meos_b.overleft_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_set(int i, Pointer s) {
		return _meos_b.overleft_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_span(int i, Pointer s) {
		return _meos_b.overleft_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_spanset(int i, Pointer ss) {
		return _meos_b.overleft_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_bigint(Pointer s, long i) {
		return _meos_b.overleft_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_float(Pointer s, double d) {
		return _meos_b.overleft_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_int(Pointer s, int i) {
		return _meos_b.overleft_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_set(Pointer s1, Pointer s2) {
		return _meos_b.overleft_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_text(Pointer s, Pointer txt) {
		return _meos_b.overleft_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_bigint(Pointer s, long i) {
		return _meos_b.overleft_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_float(Pointer s, double d) {
		return _meos_b.overleft_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_int(Pointer s, int i) {
		return _meos_b.overleft_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_span(Pointer s1, Pointer s2) {
		return _meos_b.overleft_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_spanset(Pointer s, Pointer ss) {
		return _meos_b.overleft_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_bigint(Pointer ss, long i) {
		return _meos_b.overleft_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_float(Pointer ss, double d) {
		return _meos_b.overleft_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_int(Pointer ss, int i) {
		return _meos_b.overleft_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_span(Pointer ss, Pointer s) {
		return _meos_b.overleft_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_b.overleft_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_text_set(Pointer txt, Pointer s) {
		return _meos_b.overleft_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_set(long i, Pointer s) {
		return _meos_b.overright_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_span(long i, Pointer s) {
		return _meos_b.overright_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_spanset(long i, Pointer ss) {
		return _meos_b.overright_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_set(double d, Pointer s) {
		return _meos_b.overright_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_span(double d, Pointer s) {
		return _meos_b.overright_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_spanset(double d, Pointer ss) {
		return _meos_b.overright_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_set(int i, Pointer s) {
		return _meos_b.overright_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_span(int i, Pointer s) {
		return _meos_b.overright_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_spanset(int i, Pointer ss) {
		return _meos_b.overright_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_bigint(Pointer s, long i) {
		return _meos_b.overright_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_float(Pointer s, double d) {
		return _meos_b.overright_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_int(Pointer s, int i) {
		return _meos_b.overright_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_set(Pointer s1, Pointer s2) {
		return _meos_b.overright_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_text(Pointer s, Pointer txt) {
		return _meos_b.overright_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_bigint(Pointer s, long i) {
		return _meos_b.overright_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_float(Pointer s, double d) {
		return _meos_b.overright_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_int(Pointer s, int i) {
		return _meos_b.overright_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_span(Pointer s1, Pointer s2) {
		return _meos_b.overright_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_spanset(Pointer s, Pointer ss) {
		return _meos_b.overright_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_bigint(Pointer ss, long i) {
		return _meos_b.overright_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_float(Pointer ss, double d) {
		return _meos_b.overright_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_int(Pointer ss, int i) {
		return _meos_b.overright_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_span(Pointer ss, Pointer s) {
		return _meos_b.overright_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_b.overright_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_text_set(Pointer txt, Pointer s) {
		return _meos_b.overright_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_set(long i, Pointer s) {
		return _meos_b.right_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_span(long i, Pointer s) {
		return _meos_b.right_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_spanset(long i, Pointer ss) {
		return _meos_b.right_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_set(double d, Pointer s) {
		return _meos_b.right_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_span(double d, Pointer s) {
		return _meos_b.right_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_spanset(double d, Pointer ss) {
		return _meos_b.right_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_set(int i, Pointer s) {
		return _meos_b.right_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_span(int i, Pointer s) {
		return _meos_b.right_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_spanset(int i, Pointer ss) {
		return _meos_b.right_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_bigint(Pointer s, long i) {
		return _meos_b.right_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_float(Pointer s, double d) {
		return _meos_b.right_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_int(Pointer s, int i) {
		return _meos_b.right_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_set(Pointer s1, Pointer s2) {
		return _meos_b.right_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_text(Pointer s, Pointer txt) {
		return _meos_b.right_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_bigint(Pointer s, long i) {
		return _meos_b.right_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_float(Pointer s, double d) {
		return _meos_b.right_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_int(Pointer s, int i) {
		return _meos_b.right_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_span(Pointer s1, Pointer s2) {
		return _meos_b.right_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_spanset(Pointer s, Pointer ss) {
		return _meos_b.right_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_bigint(Pointer ss, long i) {
		return _meos_b.right_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_float(Pointer ss, double d) {
		return _meos_b.right_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_int(Pointer ss, int i) {
		return _meos_b.right_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_span(Pointer ss, Pointer s) {
		return _meos_b.right_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_b.right_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_text_set(Pointer txt, Pointer s) {
		return _meos_b.right_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_bigint_set(long i, Pointer s) {
		return _meos_b.intersection_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_date_set(int d, Pointer s) {
		return _meos_b.intersection_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_float_set(double d, Pointer s) {
		return _meos_b.intersection_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_geo_set(Pointer gs, Pointer s) {
		return _meos_b.intersection_geo_set(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_int_set(int i, Pointer s) {
		return _meos_b.intersection_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_bigint(Pointer s, long i) {
		return _meos_b.intersection_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_date(Pointer s, int d) {
		return _meos_b.intersection_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_float(Pointer s, double d) {
		return _meos_b.intersection_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_geo(Pointer s, Pointer gs) {
		return _meos_b.intersection_set_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_int(Pointer s, int i) {
		return _meos_b.intersection_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_set(Pointer s1, Pointer s2) {
		return _meos_b.intersection_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_text(Pointer s, Pointer txt) {
		return _meos_b.intersection_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.intersection_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_bigint(Pointer s, long i) {
		return _meos_b.intersection_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_date(Pointer s, int d) {
		return _meos_b.intersection_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_float(Pointer s, double d) {
		return _meos_b.intersection_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_int(Pointer s, int i) {
		return _meos_b.intersection_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_span(Pointer s1, Pointer s2) {
		return _meos_b.intersection_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_spanset(Pointer s, Pointer ss) {
		return _meos_b.intersection_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.intersection_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_bigint(Pointer ss, long i) {
		return _meos_b.intersection_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_date(Pointer ss, int d) {
		return _meos_b.intersection_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_float(Pointer ss, double d) {
		return _meos_b.intersection_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_int(Pointer ss, int i) {
		return _meos_b.intersection_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_span(Pointer ss, Pointer s) {
		return _meos_b.intersection_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_b.intersection_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.intersection_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_text_set(Pointer txt, Pointer s) {
		return _meos_b.intersection_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.intersection_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_set(long i, Pointer s) {
		return _meos_b.minus_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_span(long i, Pointer s) {
		return _meos_b.minus_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_spanset(long i, Pointer ss) {
		return _meos_b.minus_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_date_set(int d, Pointer s) {
		return _meos_b.minus_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_date_span(int d, Pointer s) {
		return _meos_b.minus_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_date_spanset(int d, Pointer ss) {
		return _meos_b.minus_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_set(double d, Pointer s) {
		return _meos_b.minus_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_span(double d, Pointer s) {
		return _meos_b.minus_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_spanset(double d, Pointer ss) {
		return _meos_b.minus_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_geo_set(Pointer gs, Pointer s) {
		return _meos_b.minus_geo_set(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_set(int i, Pointer s) {
		return _meos_b.minus_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_span(int i, Pointer s) {
		return _meos_b.minus_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_spanset(int i, Pointer ss) {
		return _meos_b.minus_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_bigint(Pointer s, long i) {
		return _meos_b.minus_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_date(Pointer s, int d) {
		return _meos_b.minus_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_float(Pointer s, double d) {
		return _meos_b.minus_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_geo(Pointer s, Pointer gs) {
		return _meos_b.minus_set_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_int(Pointer s, int i) {
		return _meos_b.minus_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_set(Pointer s1, Pointer s2) {
		return _meos_b.minus_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_text(Pointer s, Pointer txt) {
		return _meos_b.minus_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.minus_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_bigint(Pointer s, long i) {
		return _meos_b.minus_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_date(Pointer s, int d) {
		return _meos_b.minus_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_float(Pointer s, double d) {
		return _meos_b.minus_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_int(Pointer s, int i) {
		return _meos_b.minus_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_span(Pointer s1, Pointer s2) {
		return _meos_b.minus_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_spanset(Pointer s, Pointer ss) {
		return _meos_b.minus_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.minus_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_bigint(Pointer ss, long i) {
		return _meos_b.minus_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_date(Pointer ss, int d) {
		return _meos_b.minus_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_float(Pointer ss, double d) {
		return _meos_b.minus_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_int(Pointer ss, int i) {
		return _meos_b.minus_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_span(Pointer ss, Pointer s) {
		return _meos_b.minus_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_b.minus_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.minus_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_text_set(Pointer txt, Pointer s) {
		return _meos_b.minus_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.minus_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.minus_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return _meos_b.minus_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigint_set(long i, Pointer s) {
		return _meos_b.union_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigint_span(Pointer s, long i) {
		return _meos_b.union_bigint_span(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigint_spanset(long i, Pointer ss) {
		return _meos_b.union_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_date_set(int d, Pointer s) {
		return _meos_b.union_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_date_span(Pointer s, int d) {
		return _meos_b.union_date_span(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_date_spanset(int d, Pointer ss) {
		return _meos_b.union_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_float_set(double d, Pointer s) {
		return _meos_b.union_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_float_span(Pointer s, double d) {
		return _meos_b.union_float_span(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_float_spanset(double d, Pointer ss) {
		return _meos_b.union_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_geo_set(Pointer gs, Pointer s) {
		return _meos_b.union_geo_set(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_int_set(int i, Pointer s) {
		return _meos_b.union_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_int_span(int i, Pointer s) {
		return _meos_b.union_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_int_spanset(int i, Pointer ss) {
		return _meos_b.union_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_bigint(Pointer s, long i) {
		return _meos_b.union_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_date(Pointer s, int d) {
		return _meos_b.union_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_float(Pointer s, double d) {
		return _meos_b.union_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_geo(Pointer s, Pointer gs) {
		return _meos_b.union_set_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_int(Pointer s, int i) {
		return _meos_b.union_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_set(Pointer s1, Pointer s2) {
		return _meos_b.union_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_text(Pointer s, Pointer txt) {
		return _meos_b.union_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.union_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_bigint(Pointer s, long i) {
		return _meos_b.union_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_date(Pointer s, int d) {
		return _meos_b.union_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_float(Pointer s, double d) {
		return _meos_b.union_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_int(Pointer s, int i) {
		return _meos_b.union_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_span(Pointer s1, Pointer s2) {
		return _meos_b.union_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_spanset(Pointer s, Pointer ss) {
		return _meos_b.union_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.union_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_bigint(Pointer ss, long i) {
		return _meos_b.union_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_date(Pointer ss, int d) {
		return _meos_b.union_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_float(Pointer ss, double d) {
		return _meos_b.union_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_int(Pointer ss, int i) {
		return _meos_b.union_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_span(Pointer ss, Pointer s) {
		return _meos_b.union_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_spanset(Pointer ss1, Pointer ss2) {
		return _meos_b.union_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.union_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_text_set(Pointer txt, Pointer s) {
		return _meos_b.union_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.union_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return _meos_b.union_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return _meos_b.union_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static long distance_bigintset_bigintset(Pointer s1, Pointer s2) {
		return _meos_b.distance_bigintset_bigintset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_bigintspan_bigintspan(Pointer s1, Pointer s2) {
		return _meos_b.distance_bigintspan_bigintspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_bigintspanset_bigintspan(Pointer ss, Pointer s) {
		return _meos_b.distance_bigintspanset_bigintspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static long distance_bigintspanset_bigintspanset(Pointer ss1, Pointer ss2) {
		return _meos_b.distance_bigintspanset_bigintspanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_dateset_dateset(Pointer s1, Pointer s2) {
		return _meos_b.distance_dateset_dateset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_datespan_datespan(Pointer s1, Pointer s2) {
		return _meos_b.distance_datespan_datespan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_datespanset_datespan(Pointer ss, Pointer s) {
		return _meos_b.distance_datespanset_datespan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static int distance_datespanset_datespanset(Pointer ss1, Pointer ss2) {
		return _meos_b.distance_datespanset_datespanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatset_floatset(Pointer s1, Pointer s2) {
		return _meos_b.distance_floatset_floatset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatspan_floatspan(Pointer s1, Pointer s2) {
		return _meos_b.distance_floatspan_floatspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatspanset_floatspan(Pointer ss, Pointer s) {
		return _meos_b.distance_floatspanset_floatspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatspanset_floatspanset(Pointer ss1, Pointer ss2) {
		return _meos_b.distance_floatspanset_floatspanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_intset_intset(Pointer s1, Pointer s2) {
		return _meos_b.distance_intset_intset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_intspan_intspan(Pointer s1, Pointer s2) {
		return _meos_b.distance_intspan_intspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_intspanset_intspan(Pointer ss, Pointer s) {
		return _meos_b.distance_intspanset_intspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static int distance_intspanset_intspanset(Pointer ss1, Pointer ss2) {
		return _meos_b.distance_intspanset_intspanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_set_bigint(Pointer s, long i) {
		return _meos_b.distance_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static int distance_set_date(Pointer s, int d) {
		return _meos_b.distance_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_set_float(Pointer s, double d) {
		return _meos_b.distance_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static int distance_set_int(Pointer s, int i) {
		return _meos_b.distance_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.distance_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static long distance_span_bigint(Pointer s, long i) {
		return _meos_b.distance_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static int distance_span_date(Pointer s, int d) {
		return _meos_b.distance_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_span_float(Pointer s, double d) {
		return _meos_b.distance_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static int distance_span_int(Pointer s, int i) {
		return _meos_b.distance_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.distance_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static long distance_spanset_bigint(Pointer ss, long i) {
		return _meos_b.distance_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static int distance_spanset_date(Pointer ss, int d) {
		return _meos_b.distance_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_spanset_float(Pointer ss, double d) {
		return _meos_b.distance_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static int distance_spanset_int(Pointer ss, int i) {
		return _meos_b.distance_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.distance_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static double distance_tstzset_tstzset(Pointer s1, Pointer s2) {
		return _meos_b.distance_tstzset_tstzset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_tstzspan_tstzspan(Pointer s1, Pointer s2) {
		return _meos_b.distance_tstzspan_tstzspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_tstzspanset_tstzspan(Pointer ss, Pointer s) {
		return _meos_b.distance_tstzspanset_tstzspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static double distance_tstzspanset_tstzspanset(Pointer ss1, Pointer ss2) {
		return _meos_b.distance_tstzspanset_tstzspanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_extent_transfn(Pointer state, long i) {
		return _meos_b.bigint_extent_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_union_transfn(Pointer state, long i) {
		return _meos_b.bigint_union_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_extent_transfn(Pointer state, int d) {
		return _meos_b.date_extent_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_union_transfn(Pointer state, int d) {
		return _meos_b.date_union_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_extent_transfn(Pointer state, double d) {
		return _meos_b.float_extent_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_union_transfn(Pointer state, double d) {
		return _meos_b.float_union_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_extent_transfn(Pointer state, int i) {
		return _meos_b.int_extent_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_union_transfn(Pointer state, int i) {
		return _meos_b.int_union_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_extent_transfn(Pointer state, Pointer s) {
		return _meos_b.set_extent_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_union_finalfn(Pointer state) {
		return _meos_b.set_union_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_union_transfn(Pointer state, Pointer s) {
		return _meos_b.set_union_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_extent_transfn(Pointer state, Pointer s) {
		return _meos_b.span_extent_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_union_transfn(Pointer state, Pointer s) {
		return _meos_b.span_union_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_extent_transfn(Pointer state, Pointer ss) {
		return _meos_b.spanset_extent_transfn(state, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_union_finalfn(Pointer state) {
		return _meos_b.spanset_union_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_union_transfn(Pointer state, Pointer ss) {
		return _meos_b.spanset_union_transfn(state, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_union_transfn(Pointer state, Pointer txt) {
		return _meos_b.text_union_transfn(state, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_extent_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.timestamptz_extent_transfn(state, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_union_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.timestamptz_union_transfn(state, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_in(String str) {
		return _meos_b.tbox_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tbox_out(Pointer box, int maxdd) {
		return _meos_b.tbox_out(box, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_from_wkb(Pointer wkb, long size) {
		return _meos_b.tbox_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_from_hexwkb(String hexwkb) {
		return _meos_b.tbox_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_from_wkb(Pointer wkb, long size) {
		return _meos_b.stbox_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_from_hexwkb(String hexwkb) {
		return _meos_b.stbox_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_as_wkb(Pointer box, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_b.tbox_as_wkb(box, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static String tbox_as_hexwkb(Pointer box, byte variant, Pointer size) {
		return _meos_b.tbox_as_hexwkb(box, variant, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_as_wkb(Pointer box, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_b.stbox_as_wkb(box, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static String stbox_as_hexwkb(Pointer box, byte variant, Pointer size) {
		return _meos_b.stbox_as_hexwkb(box, variant, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_in(String str) {
		return _meos_b.stbox_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String stbox_out(Pointer box, int maxdd) {
		return _meos_b.stbox_out(box, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_tstzspan_to_tbox(double d, Pointer s) {
		return _meos_b.float_tstzspan_to_tbox(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_timestamptz_to_tbox(double d, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.float_timestamptz_to_tbox(d, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s) {
		return _meos_b.geo_tstzspan_to_stbox(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_timestamptz_to_stbox(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.geo_timestamptz_to_stbox(gs, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_tstzspan_to_tbox(int i, Pointer s) {
		return _meos_b.int_tstzspan_to_tbox(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_timestamptz_to_tbox(int i, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.int_timestamptz_to_tbox(i, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s) {
		return _meos_b.numspan_tstzspan_to_tbox(span, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspan_timestamptz_to_tbox(Pointer span, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.numspan_timestamptz_to_tbox(span, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_copy(Pointer box) {
		return _meos_b.stbox_copy(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s) {
		return _meos_b.stbox_make(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_copy(Pointer box) {
		return _meos_b.tbox_copy(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_make(Pointer s, Pointer p) {
		return _meos_b.tbox_make(s, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_to_tbox(double d) {
		return _meos_b.float_to_tbox(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_to_stbox(Pointer gs) {
		return _meos_b.geo_to_stbox(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_to_tbox(int i) {
		return _meos_b.int_to_tbox(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_to_tbox(Pointer s) {
		return _meos_b.set_to_tbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_to_tbox(Pointer s) {
		return _meos_b.span_to_tbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_to_tbox(Pointer ss) {
		return _meos_b.spanset_to_tbox(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spatialset_to_stbox(Pointer s) {
		return _meos_b.spatialset_to_stbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_to_gbox(Pointer box) {
		return _meos_b.stbox_to_gbox(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_to_box3d(Pointer box) {
		return _meos_b.stbox_to_box3d(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_to_geo(Pointer box) {
		return _meos_b.stbox_to_geo(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_to_tstzspan(Pointer box) {
		return _meos_b.stbox_to_tstzspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_to_intspan(Pointer box) {
		return _meos_b.tbox_to_intspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_to_floatspan(Pointer box) {
		return _meos_b.tbox_to_floatspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_to_tstzspan(Pointer box) {
		return _meos_b.tbox_to_tstzspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_stbox(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.timestamptz_to_stbox(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_tbox(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_b.timestamptz_to_tbox(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_to_stbox(Pointer s) {
		return _meos_b.tstzset_to_stbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_to_stbox(Pointer s) {
		return _meos_b.tstzspan_to_stbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_to_stbox(Pointer ss) {
		return _meos_b.tstzspanset_to_stbox(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_to_tbox(Pointer temp) {
		return _meos_b.tnumber_to_tbox(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_to_stbox(Pointer temp) {
		return _meos_b.tpoint_to_stbox(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_hast(Pointer box) {
		return _meos_b.stbox_hast(box);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_hasx(Pointer box) {
		return _meos_b.stbox_hasx(box);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_hasz(Pointer box) {
		return _meos_b.stbox_hasz(box);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_isgeodetic(Pointer box) {
		return _meos_b.stbox_isgeodetic(box);
	}
	
	@SuppressWarnings("unused")
	public static int stbox_srid(Pointer box) {
		return _meos_b.stbox_srid(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_tmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_tmax_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_tmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_tmin_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_xmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_xmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_ymax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_ymax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_ymin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_ymin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_zmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_zmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_zmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.stbox_zmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_hast(Pointer box) {
		return _meos_b.tbox_hast(box);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_hasx(Pointer box) {
		return _meos_b.tbox_hasx(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.tbox_tmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.tbox_tmax_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.tbox_tmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.tbox_tmin_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tbox_xmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_xmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tbox_xmax_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tbox_xmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_xmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tbox_xmin_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboxfloat_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tboxfloat_xmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboxfloat_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tboxfloat_xmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboxint_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tboxint_xmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboxint_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tboxint_xmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_expand_space(Pointer box, double d) {
		return _meos_c.stbox_expand_space(box, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_expand_time(Pointer box, Pointer interv) {
		return _meos_c.stbox_expand_time(box, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_get_space(Pointer box) {
		return _meos_c.stbox_get_space(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_quad_split(Pointer box, Pointer count) {
		return _meos_c.stbox_quad_split(box, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_round(Pointer box, int maxdd) {
		return _meos_c.stbox_round(box, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_set_srid(Pointer box, int srid) {
		return _meos_c.stbox_set_srid(box, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) {
		return _meos_c.stbox_shift_scale_time(box, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_transform(Pointer box, int srid) {
		return _meos_c.stbox_transform(box, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward) {
		return _meos_c.stbox_transform_pipeline(box, pipelinestr, srid, is_forward);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_expand_time(Pointer box, Pointer interv) {
		return _meos_c.tbox_expand_time(box, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_expand_float(Pointer box, double d) {
		return _meos_c.tbox_expand_float(box, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_expand_int(Pointer box, int i) {
		return _meos_c.tbox_expand_int(box, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_round(Pointer box, int maxdd) {
		return _meos_c.tbox_round(box, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_float(Pointer box, double shift, double width, boolean hasshift, boolean haswidth) {
		return _meos_c.tbox_shift_scale_float(box, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_int(Pointer box, int shift, int width, boolean hasshift, boolean haswidth) {
		return _meos_c.tbox_shift_scale_int(box, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) {
		return _meos_c.tbox_shift_scale_time(box, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict) {
		return _meos_c.union_tbox_tbox(box1, box2, strict);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.intersection_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict) {
		return _meos_c.union_stbox_stbox(box1, box2, strict);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.intersection_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.adjacent_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.adjacent_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.contained_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.contained_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.contains_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.contains_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.overlaps_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overlaps_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.same_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.same_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.left_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.overleft_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.right_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.overright_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.before_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.overbefore_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.after_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tbox_tbox(Pointer box1, Pointer box2) {
		return _meos_c.overafter_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.left_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overleft_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.right_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overright_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean below_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.below_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbelow_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overbelow_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean above_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.above_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overabove_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overabove_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean front_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.front_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overfront_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overfront_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean back_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.back_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overback_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overback_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.before_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overbefore_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.after_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_c.overafter_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_eq(Pointer box1, Pointer box2) {
		return _meos_c.tbox_eq(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_ne(Pointer box1, Pointer box2) {
		return _meos_c.tbox_ne(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static int tbox_cmp(Pointer box1, Pointer box2) {
		return _meos_c.tbox_cmp(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_lt(Pointer box1, Pointer box2) {
		return _meos_c.tbox_lt(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_le(Pointer box1, Pointer box2) {
		return _meos_c.tbox_le(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_ge(Pointer box1, Pointer box2) {
		return _meos_c.tbox_ge(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_gt(Pointer box1, Pointer box2) {
		return _meos_c.tbox_gt(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_eq(Pointer box1, Pointer box2) {
		return _meos_c.stbox_eq(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_ne(Pointer box1, Pointer box2) {
		return _meos_c.stbox_ne(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static int stbox_cmp(Pointer box1, Pointer box2) {
		return _meos_c.stbox_cmp(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_lt(Pointer box1, Pointer box2) {
		return _meos_c.stbox_lt(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_le(Pointer box1, Pointer box2) {
		return _meos_c.stbox_le(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_ge(Pointer box1, Pointer box2) {
		return _meos_c.stbox_ge(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_gt(Pointer box1, Pointer box2) {
		return _meos_c.stbox_gt(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_in(String str) {
		return _meos_c.tbool_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_in(String str) {
		return _meos_c.tint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_in(String str) {
		return _meos_c.tfloat_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_in(String str) {
		return _meos_c.ttext_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_in(String str) {
		return _meos_c.tgeompoint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_in(String str) {
		return _meos_c.tgeogpoint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_from_mfjson(String str) {
		return _meos_c.tbool_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_from_mfjson(String str) {
		return _meos_c.tint_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_from_mfjson(String str) {
		return _meos_c.tfloat_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_from_mfjson(String str) {
		return _meos_c.ttext_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_from_mfjson(String str) {
		return _meos_c.tgeompoint_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_from_mfjson(String str) {
		return _meos_c.tgeogpoint_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_from_wkb(Pointer wkb, long size) {
		return _meos_c.temporal_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_from_hexwkb(String hexwkb) {
		return _meos_c.temporal_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static String tbool_out(Pointer temp) {
		return _meos_c.tbool_out(temp);
	}
	
	@SuppressWarnings("unused")
	public static String tint_out(Pointer temp) {
		return _meos_c.tint_out(temp);
	}
	
	@SuppressWarnings("unused")
	public static String tfloat_out(Pointer temp, int maxdd) {
		return _meos_c.tfloat_out(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String ttext_out(Pointer temp) {
		return _meos_c.ttext_out(temp);
	}
	
	@SuppressWarnings("unused")
	public static String tpoint_out(Pointer temp, int maxdd) {
		return _meos_c.tpoint_out(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tpoint_as_text(Pointer temp, int maxdd) {
		return _meos_c.tpoint_as_text(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tpoint_as_ewkt(Pointer temp, int maxdd) {
		return _meos_c.tpoint_as_ewkt(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs) {
		return _meos_c.temporal_as_mfjson(temp, with_bbox, flags, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_as_wkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_c.temporal_as_wkb(temp, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_as_hexwkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return _meos_c.temporal_as_hexwkb(temp, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_from_base_temp(boolean b, Pointer temp) {
		return _meos_c.tbool_from_base_temp(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolinst_make(boolean b, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_c.tboolinst_make(b, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_tstzset(boolean b, Pointer s) {
		return _meos_c.tboolseq_from_base_tstzset(b, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_tstzspan(boolean b, Pointer s) {
		return _meos_c.tboolseq_from_base_tstzspan(b, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseqset_from_base_tstzspanset(boolean b, Pointer ss) {
		return _meos_c.tboolseqset_from_base_tstzspanset(b, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_copy(Pointer temp) {
		return _meos_c.temporal_copy(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_from_base_temp(double d, Pointer temp) {
		return _meos_c.tfloat_from_base_temp(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatinst_make(double d, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_c.tfloatinst_make(d, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_tstzspan(double d, Pointer s, int interp) {
		return _meos_c.tfloatseq_from_base_tstzspan(d, s, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_tstzset(double d, Pointer s) {
		return _meos_c.tfloatseq_from_base_tstzset(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_from_base_tstzspanset(double d, Pointer ss, int interp) {
		return _meos_c.tfloatseqset_from_base_tstzspanset(d, ss, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_from_base_temp(int i, Pointer temp) {
		return _meos_c.tint_from_base_temp(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintinst_make(int i, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_c.tintinst_make(i, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_tstzspan(int i, Pointer s) {
		return _meos_c.tintseq_from_base_tstzspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_tstzset(int i, Pointer s) {
		return _meos_c.tintseq_from_base_tstzset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseqset_from_base_tstzspanset(int i, Pointer ss) {
		return _meos_c.tintseqset_from_base_tstzspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_from_base_temp(Pointer gs, Pointer temp) {
		return _meos_c.tpoint_from_base_temp(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointinst_make(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_c.tpointinst_make(gs, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp) {
		return _meos_c.tpointseq_from_base_tstzspan(gs, s, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s) {
		return _meos_c.tpointseq_from_base_tstzset(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp) {
		return _meos_c.tpointseqset_from_base_tstzspanset(gs, ss, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		return _meos_c.tsequence_make(instants, count, lower_inc, upper_inc, interp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize) {
		return _meos_c.tsequenceset_make(sequences, count, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist) {
		return _meos_c.tsequenceset_make_gaps(instants, count, interp, maxt, maxdist);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_from_base_temp(Pointer txt, Pointer temp) {
		return _meos_c.ttext_from_base_temp(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextinst_make(Pointer txt, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_c.ttextinst_make(txt, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s) {
		return _meos_c.ttextseq_from_base_tstzspan(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s) {
		return _meos_c.ttextseq_from_base_tstzset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss) {
		return _meos_c.ttextseqset_from_base_tstzspanset(txt, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_tstzspan(Pointer temp) {
		return _meos_c.temporal_to_tstzspan(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_to_tint(Pointer temp) {
		return _meos_c.tfloat_to_tint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_to_tfloat(Pointer temp) {
		return _meos_c.tint_to_tfloat(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_to_span(Pointer temp) {
		return _meos_c.tnumber_to_span(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbool_end_value(Pointer temp) {
		return _meos_c.tbool_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbool_start_value(Pointer temp) {
		return _meos_c.tbool_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbool_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return _meos_c.tbool_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tbool_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_values(Pointer temp, Pointer count) {
		return _meos_c.tbool_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_duration(Pointer temp, boolean boundspan) {
		return _meos_c.temporal_duration(temp, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_end_instant(Pointer temp) {
		return _meos_c.temporal_end_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_end_sequence(Pointer temp) {
		return _meos_c.temporal_end_sequence(temp);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_end_timestamptz(Pointer temp) {
		var result = _meos_c.temporal_end_timestamptz(temp);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_hash(Pointer temp) {
		return _meos_c.temporal_hash(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_instant_n(Pointer temp, int n) {
		return _meos_c.temporal_instant_n(temp, n);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_instants(Pointer temp, Pointer count) {
		return _meos_c.temporal_instants(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_interp(Pointer temp) {
		return _meos_c.temporal_interp(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_max_instant(Pointer temp) {
		return _meos_c.temporal_max_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_min_instant(Pointer temp) {
		return _meos_c.temporal_min_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_num_instants(Pointer temp) {
		return _meos_c.temporal_num_instants(temp);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_num_sequences(Pointer temp) {
		return _meos_c.temporal_num_sequences(temp);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_num_timestamps(Pointer temp) {
		return _meos_c.temporal_num_timestamps(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_segments(Pointer temp, Pointer count) {
		return _meos_c.temporal_segments(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_sequence_n(Pointer temp, int i) {
		return _meos_c.temporal_sequence_n(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_sequences(Pointer temp, Pointer count) {
		return _meos_c.temporal_sequences(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_lower_inc(Pointer temp) {
		return _meos_c.temporal_lower_inc(temp);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_upper_inc(Pointer temp) {
		return _meos_c.temporal_upper_inc(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_start_instant(Pointer temp) {
		return _meos_c.temporal_start_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_start_sequence(Pointer temp) {
		return _meos_c.temporal_start_sequence(temp);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_start_timestamptz(Pointer temp) {
		var result = _meos_c.temporal_start_timestamptz(temp);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration) {
		return _meos_c.temporal_stops(temp, maxdist, minduration);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_subtype(Pointer temp) {
		return _meos_c.temporal_subtype(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_time(Pointer temp) {
		return _meos_c.temporal_time(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_timestamptz_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.temporal_timestamptz_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_timestamps(Pointer temp, Pointer count) {
		return _meos_c.temporal_timestamps(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static double tfloat_end_value(Pointer temp) {
		return _meos_c.tfloat_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tfloat_max_value(Pointer temp) {
		return _meos_c.tfloat_max_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tfloat_min_value(Pointer temp) {
		return _meos_c.tfloat_min_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tfloat_start_value(Pointer temp) {
		return _meos_c.tfloat_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tfloat_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return _meos_c.tfloat_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tfloat_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_values(Pointer temp, Pointer count) {
		return _meos_c.tfloat_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static int tint_end_value(Pointer temp) {
		return _meos_c.tint_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static int tint_max_value(Pointer temp) {
		return _meos_c.tint_max_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static int tint_min_value(Pointer temp) {
		return _meos_c.tint_min_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static int tint_start_value(Pointer temp) {
		return _meos_c.tint_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tint_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return _meos_c.tint_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tint_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_values(Pointer temp, Pointer count) {
		return _meos_c.tint_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static double tnumber_integral(Pointer temp) {
		return _meos_c.tnumber_integral(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tnumber_twavg(Pointer temp) {
		return _meos_c.tnumber_twavg(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_valuespans(Pointer temp) {
		return _meos_c.tnumber_valuespans(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_end_value(Pointer temp) {
		return _meos_c.tpoint_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_start_value(Pointer temp) {
		return _meos_c.tpoint_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpoint_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return _meos_c.tpoint_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tpoint_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_values(Pointer temp, Pointer count) {
		return _meos_c.tpoint_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_end_value(Pointer temp) {
		return _meos_c.ttext_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_max_value(Pointer temp) {
		return _meos_c.ttext_max_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_min_value(Pointer temp) {
		return _meos_c.ttext_min_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_start_value(Pointer temp) {
		return _meos_c.ttext_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean ttext_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return _meos_c.ttext_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.ttext_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_values(Pointer temp, Pointer count) {
		return _meos_c.ttext_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static double float_degrees(double value, boolean normalize) {
		return _meos_c.float_degrees(value, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_scale_time(Pointer temp, Pointer duration) {
		return _meos_c.temporal_scale_time(temp, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_set_interp(Pointer temp, int interp) {
		return _meos_c.temporal_set_interp(temp, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration) {
		return _meos_c.temporal_shift_scale_time(temp, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_shift_time(Pointer temp, Pointer shift) {
		return _meos_c.temporal_shift_time(temp, shift);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_tinstant(Pointer temp) {
		return _meos_c.temporal_to_tinstant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_tsequence(Pointer temp, String interp_str) {
		return _meos_c.temporal_to_tsequence(temp, interp_str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_tsequenceset(Pointer temp, String interp_str) {
		return _meos_c.temporal_to_tsequenceset(temp, interp_str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_floor(Pointer temp) {
		return _meos_c.tfloat_floor(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_ceil(Pointer temp) {
		return _meos_c.tfloat_ceil(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_degrees(Pointer temp, boolean normalize) {
		return _meos_c.tfloat_degrees(temp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_radians(Pointer temp) {
		return _meos_c.tfloat_radians(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_round(Pointer temp, int maxdd) {
		return _meos_c.tfloat_round(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_scale_value(Pointer temp, double width) {
		return _meos_c.tfloat_scale_value(temp, width);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width) {
		return _meos_c.tfloat_shift_scale_value(temp, shift, width);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_shift_value(Pointer temp, double shift) {
		return _meos_c.tfloat_shift_value(temp, shift);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatarr_round(Pointer temp, int count, int maxdd) {
		return _meos_c.tfloatarr_round(temp, count, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_scale_value(Pointer temp, int width) {
		return _meos_c.tint_scale_value(temp, width);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_shift_scale_value(Pointer temp, int shift, int width) {
		return _meos_c.tint_shift_scale_value(temp, shift, width);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_shift_value(Pointer temp, int shift) {
		return _meos_c.tint_shift_value(temp, shift);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_round(Pointer temp, int maxdd) {
		return _meos_c.tpoint_round(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_transform(Pointer temp, int srid) {
		return _meos_c.tpoint_transform(temp, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward) {
		return _meos_c.tpoint_transform_pipeline(temp, pipelinestr, srid, is_forward);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_transform_pj(Pointer temp, int srid, Pointer pj) {
		return _meos_c.tpoint_transform_pj(temp, srid, pj);
	}
	
	@SuppressWarnings("unused")
	public static Pointer lwproj_transform(int srid_from, int srid_to) {
		return _meos_c.lwproj_transform(srid_from, srid_to);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointarr_round(Pointer temp, int count, int maxdd) {
		return _meos_c.tpointarr_round(temp, count, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_append_tinstant(Pointer temp, Pointer inst, double maxdist, Pointer maxt, boolean expand) {
		return _meos_c.temporal_append_tinstant(temp, inst, maxdist, maxt, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand) {
		return _meos_c.temporal_append_tsequence(temp, seq, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzspan(Pointer temp, Pointer s, boolean connect) {
		return _meos_c.temporal_delete_tstzspan(temp, s, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect) {
		return _meos_c.temporal_delete_tstzspanset(temp, ss, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_timestamptz(Pointer temp, OffsetDateTime t, boolean connect) {
		var t_new = t.toEpochSecond();
		return _meos_c.temporal_delete_timestamptz(temp, t_new, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzset(Pointer temp, Pointer s, boolean connect) {
		return _meos_c.temporal_delete_tstzset(temp, s, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect) {
		return _meos_c.temporal_insert(temp1, temp2, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_merge(Pointer temp1, Pointer temp2) {
		return _meos_c.temporal_merge(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_merge_array(Pointer temparr, int count) {
		return _meos_c.temporal_merge_array(temparr, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect) {
		return _meos_c.temporal_update(temp1, temp2, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_at_value(Pointer temp, boolean b) {
		return _meos_c.tbool_at_value(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_minus_value(Pointer temp, boolean b) {
		return _meos_c.tbool_minus_value(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_max(Pointer temp) {
		return _meos_c.temporal_at_max(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_min(Pointer temp) {
		return _meos_c.temporal_at_min(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzspan(Pointer temp, Pointer s) {
		return _meos_c.temporal_at_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzspanset(Pointer temp, Pointer ss) {
		return _meos_c.temporal_at_tstzspanset(temp, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_timestamptz(Pointer temp, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_c.temporal_at_timestamptz(temp, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzset(Pointer temp, Pointer s) {
		return _meos_c.temporal_at_tstzset(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_values(Pointer temp, Pointer set) {
		return _meos_c.temporal_at_values(temp, set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_max(Pointer temp) {
		return _meos_c.temporal_minus_max(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_min(Pointer temp) {
		return _meos_c.temporal_minus_min(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzspan(Pointer temp, Pointer s) {
		return _meos_c.temporal_minus_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzspanset(Pointer temp, Pointer ss) {
		return _meos_c.temporal_minus_tstzspanset(temp, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_timestamptz(Pointer temp, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_c.temporal_minus_timestamptz(temp, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzset(Pointer temp, Pointer s) {
		return _meos_c.temporal_minus_tstzset(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_values(Pointer temp, Pointer set) {
		return _meos_c.temporal_minus_values(temp, set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_at_value(Pointer temp, double d) {
		return _meos_c.tfloat_at_value(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_minus_value(Pointer temp, double d) {
		return _meos_c.tfloat_minus_value(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_at_value(Pointer temp, int i) {
		return _meos_c.tint_at_value(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_minus_value(Pointer temp, int i) {
		return _meos_c.tint_minus_value(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_at_span(Pointer temp, Pointer span) {
		return _meos_c.tnumber_at_span(temp, span);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_at_spanset(Pointer temp, Pointer ss) {
		return _meos_c.tnumber_at_spanset(temp, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_at_tbox(Pointer temp, Pointer box) {
		return _meos_c.tnumber_at_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_span(Pointer temp, Pointer span) {
		return _meos_c.tnumber_minus_span(temp, span);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_spanset(Pointer temp, Pointer ss) {
		return _meos_c.tnumber_minus_spanset(temp, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_tbox(Pointer temp, Pointer box) {
		return _meos_c.tnumber_minus_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_at_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period) {
		return _meos_c.tpoint_at_geom_time(temp, gs, zspan, period);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_at_stbox(Pointer temp, Pointer box, boolean border_inc) {
		return _meos_c.tpoint_at_stbox(temp, box, border_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_at_value(Pointer temp, Pointer gs) {
		return _meos_c.tpoint_at_value(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period) {
		return _meos_c.tpoint_minus_geom_time(temp, gs, zspan, period);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_stbox(Pointer temp, Pointer box, boolean border_inc) {
		return _meos_c.tpoint_minus_stbox(temp, box, border_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_value(Pointer temp, Pointer gs) {
		return _meos_c.tpoint_minus_value(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_at_value(Pointer temp, Pointer txt) {
		return _meos_c.ttext_at_value(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_minus_value(Pointer temp, Pointer txt) {
		return _meos_c.ttext_minus_value(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_cmp(Pointer temp1, Pointer temp2) {
		return _meos_c.temporal_cmp(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_eq(Pointer temp1, Pointer temp2) {
		return _meos_c.temporal_eq(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_ge(Pointer temp1, Pointer temp2) {
		return _meos_c.temporal_ge(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_gt(Pointer temp1, Pointer temp2) {
		return _meos_c.temporal_gt(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_le(Pointer temp1, Pointer temp2) {
		return _meos_c.temporal_le(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_lt(Pointer temp1, Pointer temp2) {
		return _meos_c.temporal_lt(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_ne(Pointer temp1, Pointer temp2) {
		return _meos_c.temporal_ne(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_bool_tbool(boolean b, Pointer temp) {
		return _meos_c.always_eq_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_float_tfloat(double d, Pointer temp) {
		return _meos_c.always_eq_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_int_tint(int i, Pointer temp) {
		return _meos_c.always_eq_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_point_tpoint(Pointer gs, Pointer temp) {
		return _meos_c.always_eq_point_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tbool_bool(Pointer temp, boolean b) {
		return _meos_c.always_eq_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.always_eq_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.always_eq_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tfloat_float(Pointer temp, double d) {
		return _meos_c.always_eq_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tint_int(Pointer temp, int i) {
		return _meos_c.always_eq_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tpoint_point(Pointer temp, Pointer gs) {
		return _meos_c.always_eq_tpoint_point(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_c.always_eq_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.always_eq_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_bool_tbool(boolean b, Pointer temp) {
		return _meos_c.always_ne_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_float_tfloat(double d, Pointer temp) {
		return _meos_c.always_ne_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_int_tint(int i, Pointer temp) {
		return _meos_c.always_ne_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_point_tpoint(Pointer gs, Pointer temp) {
		return _meos_c.always_ne_point_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tbool_bool(Pointer temp, boolean b) {
		return _meos_c.always_ne_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.always_ne_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.always_ne_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tfloat_float(Pointer temp, double d) {
		return _meos_c.always_ne_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tint_int(Pointer temp, int i) {
		return _meos_c.always_ne_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tpoint_point(Pointer temp, Pointer gs) {
		return _meos_c.always_ne_tpoint_point(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_c.always_ne_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.always_ne_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_float_tfloat(double d, Pointer temp) {
		return _meos_c.always_ge_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_int_tint(int i, Pointer temp) {
		return _meos_c.always_ge_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.always_ge_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.always_ge_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_tfloat_float(Pointer temp, double d) {
		return _meos_c.always_ge_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_tint_int(Pointer temp, int i) {
		return _meos_c.always_ge_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.always_ge_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_float_tfloat(double d, Pointer temp) {
		return _meos_c.always_gt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_int_tint(int i, Pointer temp) {
		return _meos_c.always_gt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.always_gt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.always_gt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_tfloat_float(Pointer temp, double d) {
		return _meos_c.always_gt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_tint_int(Pointer temp, int i) {
		return _meos_c.always_gt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.always_gt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_float_tfloat(double d, Pointer temp) {
		return _meos_c.always_le_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_int_tint(int i, Pointer temp) {
		return _meos_c.always_le_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.always_le_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.always_le_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_tfloat_float(Pointer temp, double d) {
		return _meos_c.always_le_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_tint_int(Pointer temp, int i) {
		return _meos_c.always_le_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.always_le_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_float_tfloat(double d, Pointer temp) {
		return _meos_c.always_lt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_int_tint(int i, Pointer temp) {
		return _meos_c.always_lt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.always_lt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.always_lt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_tfloat_float(Pointer temp, double d) {
		return _meos_c.always_lt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_tint_int(Pointer temp, int i) {
		return _meos_c.always_lt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.always_lt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_bool_tbool(boolean b, Pointer temp) {
		return _meos_c.ever_eq_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_float_tfloat(double d, Pointer temp) {
		return _meos_c.ever_eq_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_int_tint(int i, Pointer temp) {
		return _meos_c.ever_eq_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_point_tpoint(Pointer gs, Pointer temp) {
		return _meos_c.ever_eq_point_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tbool_bool(Pointer temp, boolean b) {
		return _meos_c.ever_eq_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.ever_eq_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.ever_eq_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tfloat_float(Pointer temp, double d) {
		return _meos_c.ever_eq_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tint_int(Pointer temp, int i) {
		return _meos_c.ever_eq_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tpoint_point(Pointer temp, Pointer gs) {
		return _meos_c.ever_eq_tpoint_point(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_c.ever_eq_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.ever_eq_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_float_tfloat(double d, Pointer temp) {
		return _meos_c.ever_ge_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_int_tint(int i, Pointer temp) {
		return _meos_c.ever_ge_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.ever_ge_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.ever_ge_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_tfloat_float(Pointer temp, double d) {
		return _meos_c.ever_ge_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_tint_int(Pointer temp, int i) {
		return _meos_c.ever_ge_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.ever_ge_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_float_tfloat(double d, Pointer temp) {
		return _meos_c.ever_gt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_int_tint(int i, Pointer temp) {
		return _meos_c.ever_gt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.ever_gt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.ever_gt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_tfloat_float(Pointer temp, double d) {
		return _meos_c.ever_gt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_tint_int(Pointer temp, int i) {
		return _meos_c.ever_gt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.ever_gt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_float_tfloat(double d, Pointer temp) {
		return _meos_c.ever_le_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_int_tint(int i, Pointer temp) {
		return _meos_c.ever_le_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.ever_le_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.ever_le_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_tfloat_float(Pointer temp, double d) {
		return _meos_c.ever_le_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_tint_int(Pointer temp, int i) {
		return _meos_c.ever_le_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.ever_le_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_float_tfloat(double d, Pointer temp) {
		return _meos_c.ever_lt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_int_tint(int i, Pointer temp) {
		return _meos_c.ever_lt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.ever_lt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.ever_lt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_tfloat_float(Pointer temp, double d) {
		return _meos_c.ever_lt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_tint_int(Pointer temp, int i) {
		return _meos_c.ever_lt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_ttext_text(Pointer temp, Pointer txt) {
		return _meos_c.ever_lt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_bool_tbool(boolean b, Pointer temp) {
		return _meos_c.ever_ne_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_float_tfloat(double d, Pointer temp) {
		return _meos_c.ever_ne_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_int_tint(int i, Pointer temp) {
		return _meos_c.ever_ne_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_point_tpoint(Pointer gs, Pointer temp) {
		return _meos_c.ever_ne_point_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tbool_bool(Pointer temp, boolean b) {
		return _meos_c.ever_ne_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_c.ever_ne_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_text_ttext(Pointer txt, Pointer temp) {
		return _meos_c.ever_ne_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tfloat_float(Pointer temp, double d) {
		return _meos_c.ever_ne_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tint_int(Pointer temp, int i) {
		return _meos_c.ever_ne_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tpoint_point(Pointer temp, Pointer gs) {
		return _meos_c.ever_ne_tpoint_point(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.ever_ne_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_ttext_text(Pointer temp, Pointer txt) {
		return _meos_d.ever_ne_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_bool_tbool(boolean b, Pointer temp) {
		return _meos_d.teq_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_float_tfloat(double d, Pointer temp) {
		return _meos_d.teq_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_int_tint(int i, Pointer temp) {
		return _meos_d.teq_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_point_tpoint(Pointer gs, Pointer temp) {
		return _meos_d.teq_point_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_tbool_bool(Pointer temp, boolean b) {
		return _meos_d.teq_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.teq_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_text_ttext(Pointer txt, Pointer temp) {
		return _meos_d.teq_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_tfloat_float(Pointer temp, double d) {
		return _meos_d.teq_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_tpoint_point(Pointer temp, Pointer gs) {
		return _meos_d.teq_tpoint_point(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_tint_int(Pointer temp, int i) {
		return _meos_d.teq_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_ttext_text(Pointer temp, Pointer txt) {
		return _meos_d.teq_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_float_tfloat(double d, Pointer temp) {
		return _meos_d.tge_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_int_tint(int i, Pointer temp) {
		return _meos_d.tge_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.tge_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_text_ttext(Pointer txt, Pointer temp) {
		return _meos_d.tge_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_tfloat_float(Pointer temp, double d) {
		return _meos_d.tge_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_tint_int(Pointer temp, int i) {
		return _meos_d.tge_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_ttext_text(Pointer temp, Pointer txt) {
		return _meos_d.tge_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_float_tfloat(double d, Pointer temp) {
		return _meos_d.tgt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_int_tint(int i, Pointer temp) {
		return _meos_d.tgt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.tgt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_text_ttext(Pointer txt, Pointer temp) {
		return _meos_d.tgt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_tfloat_float(Pointer temp, double d) {
		return _meos_d.tgt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_tint_int(Pointer temp, int i) {
		return _meos_d.tgt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_ttext_text(Pointer temp, Pointer txt) {
		return _meos_d.tgt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_float_tfloat(double d, Pointer temp) {
		return _meos_d.tle_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_int_tint(int i, Pointer temp) {
		return _meos_d.tle_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.tle_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_text_ttext(Pointer txt, Pointer temp) {
		return _meos_d.tle_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_tfloat_float(Pointer temp, double d) {
		return _meos_d.tle_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_tint_int(Pointer temp, int i) {
		return _meos_d.tle_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_ttext_text(Pointer temp, Pointer txt) {
		return _meos_d.tle_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_float_tfloat(double d, Pointer temp) {
		return _meos_d.tlt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_int_tint(int i, Pointer temp) {
		return _meos_d.tlt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.tlt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_text_ttext(Pointer txt, Pointer temp) {
		return _meos_d.tlt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_tfloat_float(Pointer temp, double d) {
		return _meos_d.tlt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_tint_int(Pointer temp, int i) {
		return _meos_d.tlt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_ttext_text(Pointer temp, Pointer txt) {
		return _meos_d.tlt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_bool_tbool(boolean b, Pointer temp) {
		return _meos_d.tne_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_float_tfloat(double d, Pointer temp) {
		return _meos_d.tne_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_int_tint(int i, Pointer temp) {
		return _meos_d.tne_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_point_tpoint(Pointer gs, Pointer temp) {
		return _meos_d.tne_point_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_tbool_bool(Pointer temp, boolean b) {
		return _meos_d.tne_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.tne_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_text_ttext(Pointer txt, Pointer temp) {
		return _meos_d.tne_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_tfloat_float(Pointer temp, double d) {
		return _meos_d.tne_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_tpoint_point(Pointer temp, Pointer gs) {
		return _meos_d.tne_tpoint_point(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_tint_int(Pointer temp, int i) {
		return _meos_d.tne_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_ttext_text(Pointer temp, Pointer txt) {
		return _meos_d.tne_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.adjacent_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.adjacent_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.adjacent_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.adjacent_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.adjacent_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.adjacent_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.adjacent_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.adjacent_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.adjacent_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.adjacent_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.adjacent_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.contained_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.contained_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.contained_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.contained_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.contained_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.contained_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.contained_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.contained_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.contained_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.contained_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.contained_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.contains_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.contains_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.contains_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.contains_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.contains_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.contains_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.contains_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.contains_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.contains_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.contains_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.contains_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.overlaps_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overlaps_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.overlaps_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.overlaps_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.overlaps_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.overlaps_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.overlaps_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.overlaps_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overlaps_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overlaps_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.overlaps_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.same_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.same_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.same_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.same_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.same_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.same_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.same_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.same_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.same_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.same_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.same_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean above_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.above_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean above_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.above_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean above_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.above_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.after_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.after_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.after_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.after_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.after_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.after_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.after_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.after_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.after_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean back_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.back_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean back_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.back_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean back_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.back_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.before_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.before_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.before_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.before_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.before_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.before_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.before_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.before_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.before_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean below_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.below_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean below_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.below_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean below_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.below_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean front_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.front_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean front_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.front_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean front_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.front_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.left_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.left_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.left_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.left_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.left_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.left_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.left_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.left_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overabove_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overabove_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overabove_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overabove_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overabove_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overabove_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overafter_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.overafter_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.overafter_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.overafter_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.overafter_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.overafter_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overafter_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overafter_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.overafter_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overback_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overback_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overback_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overback_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overback_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overback_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overbefore_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.overbefore_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_temporal_tstzspan(Pointer temp, Pointer s) {
		return _meos_d.overbefore_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2) {
		return _meos_d.overbefore_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.overbefore_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.overbefore_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overbefore_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overbefore_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tstzspan_temporal(Pointer s, Pointer temp) {
		return _meos_d.overbefore_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbelow_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overbelow_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbelow_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overbelow_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbelow_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overbelow_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overfront_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overfront_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overfront_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overfront_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overfront_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overfront_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.overleft_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overleft_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.overleft_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.overleft_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.overleft_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.overleft_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overleft_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overleft_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.overright_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.overright_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.overright_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.overright_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.overright_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.overright_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.overright_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.overright_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_numspan_tnumber(Pointer s, Pointer temp) {
		return _meos_d.right_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_stbox_tpoint(Pointer box, Pointer temp) {
		return _meos_d.right_stbox_tpoint(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tbox_tnumber(Pointer box, Pointer temp) {
		return _meos_d.right_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tnumber_numspan(Pointer temp, Pointer s) {
		return _meos_d.right_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tnumber_tbox(Pointer temp, Pointer box) {
		return _meos_d.right_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.right_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.right_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.right_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tand_bool_tbool(boolean b, Pointer temp) {
		return _meos_d.tand_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tand_tbool_bool(Pointer temp, boolean b) {
		return _meos_d.tand_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tand_tbool_tbool(Pointer temp1, Pointer temp2) {
		return _meos_d.tand_tbool_tbool(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_when_true(Pointer temp) {
		return _meos_d.tbool_when_true(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnot_tbool(Pointer temp) {
		return _meos_d.tnot_tbool(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tor_bool_tbool(boolean b, Pointer temp) {
		return _meos_d.tor_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tor_tbool_bool(Pointer temp, boolean b) {
		return _meos_d.tor_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tor_tbool_tbool(Pointer temp1, Pointer temp2) {
		return _meos_d.tor_tbool_tbool(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_float_tfloat(double d, Pointer tnumber) {
		return _meos_d.add_float_tfloat(d, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_int_tint(int i, Pointer tnumber) {
		return _meos_d.add_int_tint(i, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_tfloat_float(Pointer tnumber, double d) {
		return _meos_d.add_tfloat_float(tnumber, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_tint_int(Pointer tnumber, int i) {
		return _meos_d.add_tint_int(tnumber, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		return _meos_d.add_tnumber_tnumber(tnumber1, tnumber2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_float_tfloat(double d, Pointer tnumber) {
		return _meos_d.div_float_tfloat(d, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_int_tint(int i, Pointer tnumber) {
		return _meos_d.div_int_tint(i, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_tfloat_float(Pointer tnumber, double d) {
		return _meos_d.div_tfloat_float(tnumber, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_tint_int(Pointer tnumber, int i) {
		return _meos_d.div_tint_int(tnumber, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		return _meos_d.div_tnumber_tnumber(tnumber1, tnumber2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_float_tfloat(double d, Pointer tnumber) {
		return _meos_d.mult_float_tfloat(d, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_int_tint(int i, Pointer tnumber) {
		return _meos_d.mult_int_tint(i, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_tfloat_float(Pointer tnumber, double d) {
		return _meos_d.mult_tfloat_float(tnumber, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_tint_int(Pointer tnumber, int i) {
		return _meos_d.mult_tint_int(tnumber, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		return _meos_d.mult_tnumber_tnumber(tnumber1, tnumber2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_float_tfloat(double d, Pointer tnumber) {
		return _meos_d.sub_float_tfloat(d, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_int_tint(int i, Pointer tnumber) {
		return _meos_d.sub_int_tint(i, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_tfloat_float(Pointer tnumber, double d) {
		return _meos_d.sub_tfloat_float(tnumber, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_tint_int(Pointer tnumber, int i) {
		return _meos_d.sub_tint_int(tnumber, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		return _meos_d.sub_tnumber_tnumber(tnumber1, tnumber2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_derivative(Pointer temp) {
		return _meos_d.tfloat_derivative(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_abs(Pointer temp) {
		return _meos_d.tnumber_abs(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_angular_difference(Pointer temp) {
		return _meos_d.tnumber_angular_difference(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_delta_value(Pointer temp) {
		return _meos_d.tnumber_delta_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_text_ttext(Pointer txt, Pointer temp) {
		return _meos_d.textcat_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_ttext_text(Pointer temp, Pointer txt) {
		return _meos_d.textcat_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2) {
		return _meos_d.textcat_ttext_ttext(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_upper(Pointer temp) {
		return _meos_d.ttext_upper(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_lower(Pointer temp) {
		return _meos_d.ttext_lower(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_initcap(Pointer temp) {
		return _meos_d.ttext_initcap(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tfloat_float(Pointer temp, double d) {
		return _meos_d.distance_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tint_int(Pointer temp, int i) {
		return _meos_d.distance_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return _meos_d.distance_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tpoint_point(Pointer temp, Pointer gs) {
		return _meos_d.distance_tpoint_point(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.distance_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static double nad_stbox_geo(Pointer box, Pointer gs) {
		return _meos_d.nad_stbox_geo(box, gs);
	}
	
	@SuppressWarnings("unused")
	public static double nad_stbox_stbox(Pointer box1, Pointer box2) {
		return _meos_d.nad_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tint_int(Pointer temp, int i) {
		return _meos_d.nad_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tint_tbox(Pointer temp, Pointer box) {
		return _meos_d.nad_tint_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tint_tint(Pointer temp1, Pointer temp2) {
		return _meos_d.nad_tint_tint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tboxint_tboxint(Pointer box1, Pointer box2) {
		return _meos_d.nad_tboxint_tboxint(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tfloat_float(Pointer temp, double d) {
		return _meos_d.nad_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tfloat_tfloat(Pointer temp1, Pointer temp2) {
		return _meos_d.nad_tfloat_tfloat(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tfloat_tbox(Pointer temp, Pointer box) {
		return _meos_d.nad_tfloat_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2) {
		return _meos_d.nad_tboxfloat_tboxfloat(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.nad_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tpoint_stbox(Pointer temp, Pointer box) {
		return _meos_d.nad_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.nad_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer nai_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.nai_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer nai_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.nai_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer shortestline_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.shortestline_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer shortestline_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.shortestline_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bearing_point_point(Pointer gs1, Pointer gs2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.bearing_point_point(gs1, gs2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert) {
		return _meos_d.bearing_tpoint_point(temp, gs, invert);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.bearing_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_angular_difference(Pointer temp) {
		return _meos_d.tpoint_angular_difference(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_azimuth(Pointer temp) {
		return _meos_d.tpoint_azimuth(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_convex_hull(Pointer temp) {
		return _meos_d.tpoint_convex_hull(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_cumulative_length(Pointer temp) {
		return _meos_d.tpoint_cumulative_length(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_direction(Pointer temp) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.tpoint_direction(temp, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_get_x(Pointer temp) {
		return _meos_d.tpoint_get_x(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_get_y(Pointer temp) {
		return _meos_d.tpoint_get_y(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_get_z(Pointer temp) {
		return _meos_d.tpoint_get_z(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpoint_is_simple(Pointer temp) {
		return _meos_d.tpoint_is_simple(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tpoint_length(Pointer temp) {
		return _meos_d.tpoint_length(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_speed(Pointer temp) {
		return _meos_d.tpoint_speed(temp);
	}
	
	@SuppressWarnings("unused")
	public static int tpoint_srid(Pointer temp) {
		return _meos_d.tpoint_srid(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_stboxes(Pointer temp, Pointer count) {
		return _meos_d.tpoint_stboxes(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_trajectory(Pointer temp) {
		return _meos_d.tpoint_trajectory(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_twcentroid(Pointer temp) {
		return _meos_d.tpoint_twcentroid(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_expand_space(Pointer gs, double d) {
		return _meos_d.geo_expand_space(gs, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geomeas_to_tpoint(Pointer gs) {
		return _meos_d.geomeas_to_tpoint(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_to_tgeompoint(Pointer temp) {
		return _meos_d.tgeogpoint_to_tgeompoint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_to_tgeogpoint(Pointer temp) {
		return _meos_d.tgeompoint_to_tgeogpoint(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpoint_AsMVTGeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count) {
		return _meos_d.tpoint_AsMVTGeom(temp, bounds, extent, buffer, clip_geom, gsarr, timesarr, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_expand_space(Pointer temp, double d) {
		return _meos_d.tpoint_expand_space(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_make_simple(Pointer temp, Pointer count) {
		return _meos_d.tpoint_make_simple(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_set_srid(Pointer temp, int srid) {
		return _meos_d.tpoint_set_srid(temp, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.tpoint_tfloat_to_geomeas(tpoint, measure, segmentize, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static int acontains_geo_tpoint(Pointer gs, Pointer temp) {
		return _meos_d.acontains_geo_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int adisjoint_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.adisjoint_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int adisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.adisjoint_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int adwithin_tpoint_geo(Pointer temp, Pointer gs, double dist) {
		return _meos_d.adwithin_tpoint_geo(temp, gs, dist);
	}
	
	@SuppressWarnings("unused")
	public static int adwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist) {
		return _meos_d.adwithin_tpoint_tpoint(temp1, temp2, dist);
	}
	
	@SuppressWarnings("unused")
	public static int aintersects_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.aintersects_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int aintersects_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.aintersects_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int atouches_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.atouches_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int econtains_geo_tpoint(Pointer gs, Pointer temp) {
		return _meos_d.econtains_geo_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int edisjoint_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.edisjoint_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int edisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.edisjoint_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int edwithin_tpoint_geo(Pointer temp, Pointer gs, double dist) {
		return _meos_d.edwithin_tpoint_geo(temp, gs, dist);
	}
	
	@SuppressWarnings("unused")
	public static int edwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist) {
		return _meos_d.edwithin_tpoint_tpoint(temp1, temp2, dist);
	}
	
	@SuppressWarnings("unused")
	public static int eintersects_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.eintersects_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int eintersects_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return _meos_d.eintersects_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int etouches_tpoint_geo(Pointer temp, Pointer gs) {
		return _meos_d.etouches_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tcontains_geo_tpoint(Pointer gs, Pointer temp, boolean restr, boolean atvalue) {
		return _meos_d.tcontains_geo_tpoint(gs, temp, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return _meos_d.tdisjoint_tpoint_geo(temp, gs, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tpoint_tpoint (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue) {
		return _meos_d.tdisjoint_tpoint_tpoint(temp1, temp2, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdwithin_tpoint_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue) {
		return _meos_d.tdwithin_tpoint_geo(temp, gs, dist, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue) {
		return _meos_d.tdwithin_tpoint_tpoint(temp1, temp2, dist, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintersects_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return _meos_d.tintersects_tpoint_geo(temp, gs, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintersects_tpoint_tpoint (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue) {
		return _meos_d.tintersects_tpoint_tpoint(temp1, temp2, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttouches_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return _meos_d.ttouches_tpoint_geo(temp, gs, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_tand_transfn(Pointer state, Pointer temp) {
		return _meos_d.tbool_tand_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_tor_transfn(Pointer state, Pointer temp) {
		return _meos_d.tbool_tor_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_extent_transfn(Pointer s, Pointer temp) {
		return _meos_d.temporal_extent_transfn(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tagg_finalfn(Pointer state) {
		return _meos_d.temporal_tagg_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tcount_transfn(Pointer state, Pointer temp) {
		return _meos_d.temporal_tcount_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_tmax_transfn(Pointer state, Pointer temp) {
		return _meos_d.tfloat_tmax_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_tmin_transfn(Pointer state, Pointer temp) {
		return _meos_d.tfloat_tmin_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_tsum_transfn(Pointer state, Pointer temp) {
		return _meos_d.tfloat_tsum_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_wmax_transfn(Pointer state, Pointer temp, Pointer interv) {
		return _meos_d.tfloat_wmax_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_wmin_transfn(Pointer state, Pointer temp, Pointer interv) {
		return _meos_d.tfloat_wmin_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_wsum_transfn(Pointer state, Pointer temp, Pointer interv) {
		return _meos_d.tfloat_wsum_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_tcount_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return _meos_d.timestamptz_tcount_transfn(state, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_tmax_transfn(Pointer state, Pointer temp) {
		return _meos_d.tint_tmax_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_tmin_transfn(Pointer state, Pointer temp) {
		return _meos_d.tint_tmin_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_tsum_transfn(Pointer state, Pointer temp) {
		return _meos_d.tint_tsum_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_wmax_transfn(Pointer state, Pointer temp, Pointer interv) {
		return _meos_d.tint_wmax_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_wmin_transfn(Pointer state, Pointer temp, Pointer interv) {
		return _meos_d.tint_wmin_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_wsum_transfn(Pointer state, Pointer temp, Pointer interv) {
		return _meos_d.tint_wsum_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_extent_transfn(Pointer box, Pointer temp) {
		return _meos_d.tnumber_extent_transfn(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_tavg_finalfn(Pointer state) {
		return _meos_d.tnumber_tavg_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_tavg_transfn(Pointer state, Pointer temp) {
		return _meos_d.tnumber_tavg_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_wavg_transfn(Pointer state, Pointer temp, Pointer interv) {
		return _meos_d.tnumber_wavg_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_extent_transfn(Pointer box, Pointer temp) {
		return _meos_d.tpoint_extent_transfn(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_tcentroid_finalfn(Pointer state) {
		return _meos_d.tpoint_tcentroid_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp) {
		return _meos_d.tpoint_tcentroid_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_tcount_transfn(Pointer state, Pointer s) {
		return _meos_d.tstzset_tcount_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_tcount_transfn(Pointer state, Pointer s) {
		return _meos_d.tstzspan_tcount_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_tcount_transfn(Pointer state, Pointer ss) {
		return _meos_d.tstzspanset_tcount_transfn(state, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_tmax_transfn(Pointer state, Pointer temp) {
		return _meos_d.ttext_tmax_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_tmin_transfn(Pointer state, Pointer temp) {
		return _meos_d.ttext_tmin_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_dp(Pointer temp, double eps_dist, boolean synchronize) {
		return _meos_d.temporal_simplify_dp(temp, eps_dist, synchronize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_max_dist(Pointer temp, double eps_dist, boolean synchronize) {
		return _meos_d.temporal_simplify_max_dist(temp, eps_dist, synchronize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_dist(Pointer temp, double dist) {
		return _meos_d.temporal_simplify_min_dist(temp, dist);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_tdelta(Pointer temp, Pointer mint) {
		return _meos_d.temporal_simplify_min_tdelta(temp, mint);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tprecision(Pointer temp, Pointer duration, OffsetDateTime origin) {
		var origin_new = origin.toEpochSecond();
		return _meos_d.temporal_tprecision(temp, duration, origin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tsample(Pointer temp, Pointer duration, OffsetDateTime origin, int interp) {
		var origin_new = origin.toEpochSecond();
		return _meos_d.temporal_tsample(temp, duration, origin_new, interp);
	}
	
	@SuppressWarnings("unused")
	public static double temporal_dyntimewarp_distance(Pointer temp1, Pointer temp2) {
		return _meos_d.temporal_dyntimewarp_distance(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_dyntimewarp_path(Pointer temp1, Pointer temp2, Pointer count) {
		return _meos_d.temporal_dyntimewarp_path(temp1, temp2, count);
	}
	
	@SuppressWarnings("unused")
	public static double temporal_frechet_distance(Pointer temp1, Pointer temp2) {
		return _meos_d.temporal_frechet_distance(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_frechet_path(Pointer temp1, Pointer temp2, Pointer count) {
		return _meos_d.temporal_frechet_path(temp1, temp2, count);
	}
	
	@SuppressWarnings("unused")
	public static double temporal_hausdorff_distance(Pointer temp1, Pointer temp2) {
		return _meos_d.temporal_hausdorff_distance(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static double float_bucket(double value, double size, double origin) {
		return _meos_d.float_bucket(value, size, origin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_bucket_list(Pointer bounds, double size, double origin, Pointer count) {
		return _meos_d.floatspan_bucket_list(bounds, size, origin, count);
	}
	
	@SuppressWarnings("unused")
	public static int int_bucket(int value, int size, int origin) {
		return _meos_d.int_bucket(value, size, origin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_bucket_list(Pointer bounds, int size, int origin, Pointer count) {
		return _meos_d.intspan_bucket_list(bounds, size, origin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tile(Pointer point, OffsetDateTime t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean hast) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.stbox_tile(point, t_new, xsize, ysize, zsize, duration, sorigin, torigin_new, hast);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tile_list(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean border_inc, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.stbox_tile_list(bounds, xsize, ysize, zsize, duration, sorigin, torigin_new, border_inc, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_time_split(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer time_buckets, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.temporal_time_split(temp, duration, torigin_new, time_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer value_buckets, Pointer count) {
		return _meos_d.tfloat_value_split(temp, size, origin, value_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_time_split(Pointer temp, double size, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer value_buckets, Pointer time_buckets, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.tfloat_value_time_split(temp, size, duration, vorigin, torigin_new, value_buckets, time_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_tile(double value, OffsetDateTime t, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.tfloatbox_tile(value, t_new, vsize, duration, vorigin, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_tile_list(Pointer box, double xsize, Pointer duration, double xorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.tfloatbox_tile_list(box, xsize, duration, xorigin, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_bucket(OffsetDateTime timestamp, Pointer duration, OffsetDateTime origin) {
		var timestamp_new = timestamp.toEpochSecond();
		var origin_new = origin.toEpochSecond();
		var result = _meos_d.timestamptz_bucket(timestamp_new, duration, origin_new);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_split(Pointer temp, int size, int origin, Pointer value_buckets, Pointer count) {
		return _meos_d.tint_value_split(temp, size, origin, value_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_time_split(Pointer temp, int size, Pointer duration, int vorigin, OffsetDateTime torigin, Pointer value_buckets, Pointer time_buckets, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.tint_value_time_split(temp, size, duration, vorigin, torigin_new, value_buckets, time_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_tile(int value, OffsetDateTime t, int vsize, Pointer duration, int vorigin, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.tintbox_tile(value, t_new, vsize, duration, vorigin, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_tile_list(Pointer box, int xsize, Pointer duration, int xorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.tintbox_tile_list(box, xsize, duration, xorigin, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_space_split(Pointer temp, float xsize, float ysize, float zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_buckets, Pointer count) {
		return _meos_d.tpoint_space_split(temp, xsize, ysize, zsize, sorigin, bitmatrix, border_inc, space_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_space_time_split(Pointer temp, float xsize, float ysize, float zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean bitmatrix, boolean border_inc, Pointer space_buckets, Pointer time_buckets, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return _meos_d.tpoint_space_time_split(temp, xsize, ysize, zsize, duration, sorigin, torigin_new, bitmatrix, border_inc, space_buckets, time_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_bucket_list(Pointer bounds, Pointer duration, OffsetDateTime origin, Pointer count) {
		var origin_new = origin.toEpochSecond();
		return _meos_d.tstzspan_bucket_list(bounds, duration, origin_new, count);
	}
}