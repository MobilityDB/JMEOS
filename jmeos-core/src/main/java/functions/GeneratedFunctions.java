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

public class GeneratedFunctions {
	public interface MeosLibraryPartA {

		Pointer meos_array_create(int elem_size);

		void meos_array_add(Pointer array, Pointer value);

		Pointer meos_array_get(Pointer array, int n);

		int meos_array_count(Pointer array);

		void meos_array_reset(Pointer array);

		void meos_array_reset_free(Pointer array);

		void meos_array_destroy(Pointer array);

		void meos_array_destroy_free(Pointer array);

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

		int rtree_search(Pointer rtree, int op, Pointer query, Pointer result);

		int rtree_search_temporal(Pointer rtree, int op, Pointer temp, Pointer result);

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

		Pointer cstring2text(String str);

		long date_to_timestamp(int dateVal);

		long date_to_timestamptz(int d);

		double float_exp(double d);

		double float_ln(double d);

		double float_log10(double d);

		String float8_out(double d, int maxdd);

		double float_round(double d, int maxdd);

		int int32_cmp(int l, int r);

		int int64_cmp(long l, long r);

		Pointer interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs);

		int minus_date_date(int d1, int d2);

		int minus_date_int(int d, int days);

		long minus_timestamptz_interval(long t, Pointer interv);

		Pointer minus_timestamptz_timestamptz(long t1, long t2);

		Pointer mul_interval_double(Pointer interv, double factor);

		int pg_date_in(String str);

		String pg_date_out(int d);

		int pg_interval_cmp(Pointer interv1, Pointer interv2);

		Pointer pg_interval_in(String str, int typmod);

		String pg_interval_out(Pointer interv);

		long pg_timestamp_in(String str, int typmod);

		String pg_timestamp_out(long t);

		long pg_timestamptz_in(String str, int typmod);

		String pg_timestamptz_out(long t);

		String text2cstring(Pointer txt);

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

		Pointer bigint_to_span(long i);

		Pointer bigint_to_spanset(long i);

		Pointer bigintspan_to_intspan(Pointer s);

		Pointer bigintspan_to_floatspan(Pointer s);

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

		Pointer floatspan_to_bigintspan(Pointer s);

		Pointer floatspan_to_intspan(Pointer s);

		Pointer floatspanset_to_intspanset(Pointer ss);

		Pointer int_to_set(int i);

		Pointer int_to_span(int i);

		Pointer int_to_spanset(int i);

		Pointer intset_to_floatset(Pointer s);

		Pointer intspan_to_bigintspan(Pointer s);

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

		Pointer spanset_spanarr(Pointer ss);

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

		Pointer set_spans(Pointer s);

		Pointer set_split_each_n_spans(Pointer s, int elems_per_span, Pointer count);

		Pointer set_split_n_spans(Pointer s, int span_count, Pointer count);

		Pointer spanset_spans(Pointer ss);

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

		Pointer tbox_to_bigintspan(Pointer box);

		Pointer tbox_to_intspan(Pointer box);

		Pointer tbox_to_floatspan(Pointer box);

		Pointer tbox_to_tstzspan(Pointer box);

		Pointer timestamptz_to_tbox(long t);

		int tbox_hash(Pointer box);

		long tbox_hash_extended(Pointer box, long seed);

		boolean tbox_hast(Pointer box);

		boolean tbox_hasx(Pointer box);

	}

	public interface MeosLibraryPartB {

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

		Pointer tbigintbox_expand(Pointer box, long i);

		Pointer tbigintbox_shift_scale(Pointer box, long shift, long width, boolean hasshift, boolean haswidth);

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

		Pointer tbigint_from_mfjson(String str);

		Pointer tbigint_in(String str);

		String tbigint_out(Pointer temp);

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

		Pointer tbigint_from_base_temp(long i, Pointer temp);

		Pointer tbigintinst_make(long i, long t);

		Pointer tbigintseq_from_base_tstzset(long i, Pointer s);

		Pointer tbigintseq_from_base_tstzspan(long i, Pointer s);

		Pointer tbigintseqset_from_base_tstzspanset(long i, Pointer ss);

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

		Pointer tbigint_to_tfloat(Pointer temp);

		Pointer tbigint_to_tint(Pointer temp);

		Pointer tbool_to_tint(Pointer temp);

		Pointer temporal_to_tstzspan(Pointer temp);

		Pointer tfloat_to_tbigint(Pointer temp);

		Pointer tfloat_to_tint(Pointer temp);

		Pointer tint_to_tbigint(Pointer temp);

		Pointer tint_to_tfloat(Pointer temp);

		Pointer tnumber_to_span(Pointer temp);

		Pointer tnumber_to_tbox(Pointer temp);

		long tbigint_end_value(Pointer temp);

		long tbigint_max_value(Pointer temp);

		long tbigint_min_value(Pointer temp);

		long tbigint_start_value(Pointer temp);

		boolean tbigint_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean tbigint_value_n(Pointer temp, long n, Pointer result);

		Pointer tbigint_values(Pointer temp, Pointer count);

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

		double tfloat_avg_value(Pointer temp);

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

		Pointer tbigint_scale_value(Pointer temp, long width);

		Pointer tbigint_shift_scale_value(Pointer temp, long shift, long width);

		Pointer tbigint_shift_value(Pointer temp, long shift);

		Pointer temparr_round(Pointer temp, int count, int maxdd);

		Pointer temporal_round(Pointer temp, int maxdd);

		Pointer temporal_scale_time(Pointer temp, Pointer duration);

		Pointer temporal_set_interp(Pointer temp, int interp);

		Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration);

		Pointer temporal_shift_time(Pointer temp, Pointer shift);

		Pointer temporal_to_tinstant(Pointer temp);

		Pointer temporal_to_tsequence(Pointer temp, int interp);

		Pointer temporal_to_tsequenceset(Pointer temp, int interp);

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

		Pointer tbigint_at_value(Pointer temp, long i);

		Pointer tbigint_minus_value(Pointer temp, long i);

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

		int always_eq_bigint_tbigint(long i, Pointer temp);

		int always_eq_bool_tbool(boolean b, Pointer temp);

		int always_eq_float_tfloat(double d, Pointer temp);

		int always_eq_int_tint(int i, Pointer temp);

		int always_eq_tbool_bool(Pointer temp, boolean b);

		int always_eq_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_eq_text_ttext(Pointer txt, Pointer temp);

		int always_eq_tbigint_bigint(Pointer temp, long i);

		int always_eq_tfloat_float(Pointer temp, double d);

		int always_eq_tint_int(Pointer temp, int i);

		int always_eq_ttext_text(Pointer temp, Pointer txt);

		int always_ge_bigint_tbigint(long i, Pointer temp);

		int always_ge_float_tfloat(double d, Pointer temp);

		int always_ge_int_tint(int i, Pointer temp);

		int always_ge_tbigint_bigint(Pointer temp, long i);

		int always_ge_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_ge_text_ttext(Pointer txt, Pointer temp);

		int always_ge_tfloat_float(Pointer temp, double d);

		int always_ge_tint_int(Pointer temp, int i);

		int always_ge_ttext_text(Pointer temp, Pointer txt);

		int always_gt_bigint_tbigint(long i, Pointer temp);

		int always_gt_float_tfloat(double d, Pointer temp);

		int always_gt_int_tint(int i, Pointer temp);

		int always_gt_tbigint_bigint(Pointer temp, long i);

		int always_gt_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_gt_text_ttext(Pointer txt, Pointer temp);

		int always_gt_tfloat_float(Pointer temp, double d);

		int always_gt_tint_int(Pointer temp, int i);

		int always_gt_ttext_text(Pointer temp, Pointer txt);

		int always_le_bigint_tbigint(long i, Pointer temp);

		int always_le_float_tfloat(double d, Pointer temp);

		int always_le_int_tint(int i, Pointer temp);

		int always_le_tbigint_bigint(Pointer temp, long i);

		int always_le_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_le_text_ttext(Pointer txt, Pointer temp);

		int always_le_tfloat_float(Pointer temp, double d);

		int always_le_tint_int(Pointer temp, int i);

		int always_le_ttext_text(Pointer temp, Pointer txt);

		int always_lt_bigint_tbigint(long i, Pointer temp);

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

		int always_lt_tbigint_bigint(Pointer temp, long i);

		int always_ne_bigint_tbigint(long i, Pointer temp);

		int always_ne_tbool_bool(Pointer temp, boolean b);

		int always_ne_tbigint_bigint(Pointer temp, long i);

		int always_ne_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_ne_text_ttext(Pointer txt, Pointer temp);

		int always_ne_tfloat_float(Pointer temp, double d);

		int always_ne_tint_int(Pointer temp, int i);

		int always_ne_ttext_text(Pointer temp, Pointer txt);

		int ever_eq_bigint_tbigint(long i, Pointer temp);

		int ever_eq_bool_tbool(boolean b, Pointer temp);

		int ever_eq_float_tfloat(double d, Pointer temp);

		int ever_eq_int_tint(int i, Pointer temp);

		int ever_eq_tbigint_bigint(Pointer temp, long i);

		int ever_eq_tbool_bool(Pointer temp, boolean b);

		int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_eq_text_ttext(Pointer txt, Pointer temp);

		int ever_eq_tfloat_float(Pointer temp, double d);

		int ever_eq_tint_int(Pointer temp, int i);

		int ever_eq_ttext_text(Pointer temp, Pointer txt);

		int ever_ge_bigint_tbigint(long i, Pointer temp);

		int ever_ge_float_tfloat(double d, Pointer temp);

		int ever_ge_int_tint(int i, Pointer temp);

		int ever_ge_tbigint_bigint(Pointer temp, long i);

		int ever_ge_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_ge_text_ttext(Pointer txt, Pointer temp);

		int ever_ge_tfloat_float(Pointer temp, double d);

		int ever_ge_tint_int(Pointer temp, int i);

		int ever_ge_ttext_text(Pointer temp, Pointer txt);

		int ever_gt_bigint_tbigint(long i, Pointer temp);

		int ever_gt_float_tfloat(double d, Pointer temp);

		int ever_gt_int_tint(int i, Pointer temp);

		int ever_gt_tbigint_bigint(Pointer temp, long i);

		int ever_gt_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_gt_text_ttext(Pointer txt, Pointer temp);

		int ever_gt_tfloat_float(Pointer temp, double d);

		int ever_gt_tint_int(Pointer temp, int i);

		int ever_gt_ttext_text(Pointer temp, Pointer txt);

		int ever_le_bigint_tbigint(long i, Pointer temp);

		int ever_le_float_tfloat(double d, Pointer temp);

		int ever_le_int_tint(int i, Pointer temp);

		int ever_le_tbigint_bigint(Pointer temp, long i);

		int ever_le_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_le_text_ttext(Pointer txt, Pointer temp);

		int ever_le_tfloat_float(Pointer temp, double d);

		int ever_le_tint_int(Pointer temp, int i);

		int ever_le_ttext_text(Pointer temp, Pointer txt);

		int ever_lt_bigint_tbigint(long i, Pointer temp);

		int ever_lt_float_tfloat(double d, Pointer temp);

		int ever_lt_int_tint(int i, Pointer temp);

		int ever_lt_tbigint_bigint(Pointer temp, long i);

		int ever_lt_temporal_temporal(Pointer temp1, Pointer temp2);

		int ever_lt_text_ttext(Pointer txt, Pointer temp);

		int ever_lt_tfloat_float(Pointer temp, double d);

		int ever_lt_tint_int(Pointer temp, int i);

		int ever_lt_ttext_text(Pointer temp, Pointer txt);

		int ever_ne_bigint_tbigint(long i, Pointer temp);

		int ever_ne_bool_tbool(boolean b, Pointer temp);

		int ever_ne_float_tfloat(double d, Pointer temp);

		int ever_ne_int_tint(int i, Pointer temp);

		int ever_ne_tbigint_bigint(Pointer temp, long i);

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

		Pointer add_bigint_tbigint(long i, Pointer tnumber);

		Pointer add_float_tfloat(double d, Pointer tnumber);

		Pointer add_int_tint(int i, Pointer tnumber);

		Pointer add_tbigint_bigint(Pointer tnumber, long i);

		Pointer add_tfloat_float(Pointer tnumber, double d);

		Pointer add_tint_int(Pointer tnumber, int i);

		Pointer add_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

		Pointer div_bigint_tbigint(long i, Pointer tnumber);

		Pointer div_float_tfloat(double d, Pointer tnumber);

		Pointer div_int_tint(int i, Pointer tnumber);

		Pointer div_tbigint_bigint(Pointer tnumber, long i);

		Pointer div_tfloat_float(Pointer tnumber, double d);

		Pointer div_tint_int(Pointer tnumber, int i);

		Pointer div_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

		Pointer mul_bigint_tbigint(long i, Pointer tnumber);

		Pointer mul_float_tfloat(double d, Pointer tnumber);

		Pointer mul_int_tint(int i, Pointer tnumber);

		Pointer mul_tbigint_bigint(Pointer tnumber, long i);

		Pointer mul_tfloat_float(Pointer tnumber, double d);

		Pointer mul_tint_int(Pointer tnumber, int i);

		Pointer mul_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

		Pointer sub_bigint_tbigint(long i, Pointer tnumber);

		Pointer sub_float_tfloat(double d, Pointer tnumber);

		Pointer sub_int_tint(int i, Pointer tnumber);

		Pointer sub_tbigint_bigint(Pointer tnumber, long i);

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

		Pointer temporal_merge_transfn(Pointer state, Pointer temp);

		Pointer temporal_merge_combinefn(Pointer state1, Pointer state2);

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

		void meos_initialize_noexit_error_handler();

		boolean h3_are_neighbor_cells_meos(long origin, long destination);

		long h3_cells_to_directed_edge_meos(long origin, long destination);

		boolean h3_is_valid_directed_edge_meos(long edge);

		long h3_get_directed_edge_origin_meos(long edge);

		long h3_get_directed_edge_destination_meos(long edge);

		long h3_cell_to_parent_meos(long origin, int resolution);

		long h3_cell_to_center_child_meos(long origin, int resolution);

		long h3_cell_to_child_pos_meos(long child, int parentRes);

		long h3_child_pos_to_cell_meos(long childPos, long parent, int childRes);

		int h3_get_resolution_meos(long hex);

		int h3_get_base_cell_number_meos(long hex);

		boolean h3_is_valid_cell_meos(long hex);

		boolean h3_is_res_class_iii_meos(long hex);

		boolean h3_is_pentagon_meos(long hex);

		long h3_get_num_cells_meos(int resolution);

		long h3_grid_distance_meos(long originIndex, long h3Index);

		long h3_cell_to_vertex_meos(long cell, int vertexNum);

		boolean h3_is_valid_vertex_meos(long vertex);

		long h3index_parse(String str);

		String h3index_to_string(long cell);

		boolean h3index_eq(long a, long b);

		boolean h3index_ne(long a, long b);

		boolean h3index_lt(long a, long b);

		boolean h3index_le(long a, long b);

		boolean h3index_gt(long a, long b);

		boolean h3index_ge(long a, long b);

		int h3index_cmp(long a, long b);

		int h3index_hash(long cell);

		Pointer h3_grid_disk(long origin, int k);

		Pointer h3_grid_ring(long origin, int k);

		Pointer h3_grid_path_cells(long start, long end);

		Pointer h3_cell_to_children(long origin, int childRes);

		Pointer h3_compact_cells(Pointer cells);

		Pointer h3_uncompact_cells(Pointer cells, int res);

		Pointer h3_origin_to_directed_edges(long origin);

		Pointer h3_cell_to_vertexes(long cell);

		Pointer h3_get_icosahedron_faces(long cell);

		boolean ensure_valid_th3index_th3index(Pointer temp1, Pointer temp2);

		boolean ensure_valid_th3index_h3index(Pointer temp, long cell);

		boolean ensure_valid_th3index_tgeogpoint(Pointer temp1, Pointer temp2);

		int datum2_h3index_eq(int d1, int d2, int type);

		int datum2_h3index_ne(int d1, int d2, int type);

		void th3indexinst_set_stbox(Pointer inst, Pointer box);

		void th3indexinstarr_set_stbox(Pointer instants, int count, Pointer box);

		void th3indexseq_expand_stbox(Pointer seq, Pointer inst);

		long h3_gs_point_to_cell(Pointer point, int resolution);

		Pointer h3_cell_to_gs_point(long cell);

		Pointer h3_cell_to_gs_boundary(long cell);

		Pointer cell_boundary_to_gs(Pointer bnd);

		double h3_sample_step_deg(int resolution);

		long h3_latlng_deg_to_cell(double lat_deg, double lng_deg, int resolution);

		long h3_cell_to_parent_next_meos(long cell);

		long h3_cell_to_center_child_next_meos(long cell);

		Pointer h3_directed_edge_to_gs_boundary(long edge);

		Pointer h3_vertex_to_gs_point(long vertex);

		Pointer h3_cell_to_local_ij_meos(long origin, long cell);

		long h3_local_ij_to_cell_meos(long origin, Pointer coord);

		int h3_unit_from_cstring(String unit);

		double h3_cell_area_meos(long cell, int unit);

		double h3_edge_length_meos(long edge, int unit);

		double h3_gs_great_circle_distance_meos(Pointer a, Pointer b, int unit);

		int datum_h3_get_resolution(int d);

		int datum_h3_get_base_cell_number(int d);

		int datum_h3_is_valid_cell(int d);

		int datum_h3_is_res_class_iii(int d);

		int datum_h3_is_pentagon(int d);

		int datum_h3_cell_to_parent(int cell_d, int res_d);

		int datum_h3_cell_to_parent_next(int cell_d);

		int datum_h3_cell_to_center_child(int cell_d, int res_d);

		int datum_h3_cell_to_center_child_next(int cell_d);

		int datum_h3_cell_to_child_pos(int cell_d, int parent_res_d);

		int datum_h3_child_pos_to_cell(int pos_d, int parent_d, int child_res_d);

		int datum_h3_are_neighbor_cells(int origin_d, int dest_d);

		int datum_h3_cells_to_directed_edge(int origin_d, int dest_d);

		int datum_h3_is_valid_directed_edge(int d);

		int datum_h3_get_directed_edge_origin(int d);

		int datum_h3_get_directed_edge_destination(int d);

		int datum_h3_directed_edge_to_boundary(int d);

		int datum_h3_cell_to_vertex(int cell_d, int vnum_d);

		int datum_h3_vertex_to_latlng(int d);

		int datum_h3_is_valid_vertex(int d);

		int datum_h3_grid_distance(int origin_d, int dest_d);

		int datum_h3_cell_to_local_ij(int origin_d, int cell_d);

		int datum_h3_local_ij_to_cell(int origin_d, int coord_d);

		int datum_h3_latlng_to_cell(int point_d, int res_d);

		int datum_h3_cell_to_latlng(int d);

		int datum_h3_cell_to_boundary(int d);

		int datum_h3_cell_area(int cell_d, int unit_d);

		int datum_h3_edge_length(int edge_d, int unit_d);

		int datum_h3_great_circle_distance(int a_d, int b_d, int unit_d);

		Pointer geo_as_ewkb(Pointer gs, String endian, Pointer size);

		String geo_as_ewkt(Pointer gs, int precision);

		String geo_as_geojson(Pointer gs, int option, int precision, String srs);

		String geo_as_hexewkb(Pointer gs, String endian);

		String geo_as_text(Pointer gs, int precision);

		Pointer geo_from_ewkb(Pointer wkb, long wkb_size, int srid);

		Pointer geo_from_geojson(String geojson);

		Pointer geo_from_text(String wkt, int srid);

		String geo_out(Pointer gs);

		Pointer geog_from_binary(String wkb_bytea);

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

	}

	public interface MeosLibraryPartC {

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

		Pointer geoset_values(Pointer s);

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

		Pointer tpoint_at_elevation(Pointer temp, Pointer s);

		Pointer tpoint_at_geom(Pointer temp, Pointer gs);

		Pointer tpoint_at_value(Pointer temp, Pointer gs);

		Pointer tpoint_minus_elevation(Pointer temp, Pointer s);

		Pointer tpoint_minus_geom(Pointer temp, Pointer gs);

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

		Pointer tcontains_geo_tgeo(Pointer gs, Pointer temp);

		Pointer tcontains_tgeo_geo(Pointer temp, Pointer gs);

		Pointer tcontains_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer tcovers_geo_tgeo(Pointer gs, Pointer temp);

		Pointer tcovers_tgeo_geo(Pointer temp, Pointer gs);

		Pointer tcovers_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer tdisjoint_geo_tgeo(Pointer gs, Pointer temp);

		Pointer tdisjoint_tgeo_geo(Pointer temp, Pointer gs);

		Pointer tdisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer tdwithin_geo_tgeo(Pointer gs, Pointer temp, double dist);

		Pointer tdwithin_tgeo_geo(Pointer temp, Pointer gs, double dist);

		Pointer tdwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist);

		Pointer tintersects_geo_tgeo(Pointer gs, Pointer temp);

		Pointer tintersects_tgeo_geo(Pointer temp, Pointer gs);

		Pointer tintersects_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer ttouches_geo_tgeo(Pointer gs, Pointer temp);

		Pointer ttouches_tgeo_geo(Pointer temp, Pointer gs);

		Pointer ttouches_tgeo_tgeo(Pointer temp1, Pointer temp2);

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

		double tgeoarr_tgeoarr_mindist(Pointer arr1, int count1, Pointer arr2, int count2);

		double mindistance_tgeo_tgeo(Pointer temp1, Pointer temp2, double threshold);

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

		Pointer geo_cluster_kmeans(Pointer geoms, int ngeoms, int k);

		Pointer geo_cluster_dbscan(Pointer geoms, int ngeoms, double tolerance, int minpoints, Pointer count);

		Pointer geo_cluster_intersecting(Pointer geoms, int ngeoms, Pointer count);

		Pointer geo_cluster_within(Pointer geoms, int ngeoms, double tolerance, Pointer count);

		int acovers_geo_tgeo(Pointer gs, Pointer temp);

		int acovers_tgeo_geo(Pointer temp, Pointer gs);

		String cbuffer_as_ewkt(Pointer cb, int maxdd);

		String cbuffer_as_hexwkb(Pointer cb, byte variant, Pointer size);

		String cbuffer_as_text(Pointer cb, int maxdd);

		Pointer cbuffer_as_wkb(Pointer cb, byte variant, Pointer size_out);

		Pointer cbuffer_from_hexwkb(String hexwkb);

		Pointer cbuffer_from_wkb(Pointer wkb, long size);

		Pointer cbuffer_in(String str);

		String cbuffer_out(Pointer cb, int maxdd);

		Pointer cbuffer_copy(Pointer cb);

		Pointer cbuffer_make(Pointer point, double radius);

		Pointer cbuffer_to_geom(Pointer cb);

		Pointer cbuffer_to_stbox(Pointer cb);

		Pointer cbufferarr_to_geom(Pointer cbarr, int count);

		Pointer geom_to_cbuffer(Pointer gs);

		int cbuffer_hash(Pointer cb);

		long cbuffer_hash_extended(Pointer cb, long seed);

		Pointer cbuffer_point(Pointer cb);

		double cbuffer_radius(Pointer cb);

		Pointer cbuffer_round(Pointer cb, int maxdd);

		Pointer cbufferarr_round(Pointer cbarr, int count, int maxdd);

		void cbuffer_set_srid(Pointer cb, int srid);

		int cbuffer_srid(Pointer cb);

		Pointer cbuffer_transform(Pointer cb, int srid);

		Pointer cbuffer_transform_pipeline(Pointer cb, String pipelinestr, int srid, boolean is_forward);

		int contains_cbuffer_cbuffer(Pointer cb1, Pointer cb2);

		int covers_cbuffer_cbuffer(Pointer cb1, Pointer cb2);

		int disjoint_cbuffer_cbuffer(Pointer cb1, Pointer cb2);

		int dwithin_cbuffer_cbuffer(Pointer cb1, Pointer cb2, double dist);

		int intersects_cbuffer_cbuffer(Pointer cb1, Pointer cb2);

		int touches_cbuffer_cbuffer(Pointer cb1, Pointer cb2);

		Pointer cbuffer_tstzspan_to_stbox(Pointer cb, Pointer s);

		Pointer cbuffer_timestamptz_to_stbox(Pointer cb, long t);

		double distance_cbuffer_cbuffer(Pointer cb1, Pointer cb2);

		double distance_cbuffer_geo(Pointer cb, Pointer gs);

		double distance_cbuffer_stbox(Pointer cb, Pointer box);

		double nad_cbuffer_stbox(Pointer cb, Pointer box);

		int cbuffer_cmp(Pointer cb1, Pointer cb2);

		boolean cbuffer_eq(Pointer cb1, Pointer cb2);

		boolean cbuffer_ge(Pointer cb1, Pointer cb2);

		boolean cbuffer_gt(Pointer cb1, Pointer cb2);

		boolean cbuffer_le(Pointer cb1, Pointer cb2);

		boolean cbuffer_lt(Pointer cb1, Pointer cb2);

		boolean cbuffer_ne(Pointer cb1, Pointer cb2);

		boolean cbuffer_nsame(Pointer cb1, Pointer cb2);

		boolean cbuffer_same(Pointer cb1, Pointer cb2);

		Pointer cbufferset_in(String str);

		String cbufferset_out(Pointer s, int maxdd);

		Pointer cbufferset_make(Pointer values, int count);

		Pointer cbuffer_to_set(Pointer cb);

		Pointer cbufferset_end_value(Pointer s);

		Pointer cbufferset_start_value(Pointer s);

		boolean cbufferset_value_n(Pointer s, int n, Pointer result);

		Pointer cbufferset_values(Pointer s);

		Pointer cbuffer_union_transfn(Pointer state, Pointer cb);

		boolean contained_cbuffer_set(Pointer cb, Pointer s);

		boolean contains_set_cbuffer(Pointer s, Pointer cb);

		Pointer intersection_cbuffer_set(Pointer cb, Pointer s);

		Pointer intersection_set_cbuffer(Pointer s, Pointer cb);

		Pointer minus_cbuffer_set(Pointer cb, Pointer s);

		Pointer minus_set_cbuffer(Pointer s, Pointer cb);

		Pointer union_cbuffer_set(Pointer cb, Pointer s);

		Pointer union_set_cbuffer(Pointer s, Pointer cb);

		Pointer tcbuffer_in(String str);

		Pointer tcbuffer_from_mfjson(String mfjson);

		Pointer tcbuffer_make(Pointer tpoint, Pointer tfloat);

		Pointer tcbuffer_points(Pointer temp);

		Pointer tcbuffer_radius(Pointer temp);

		Pointer tcbuffer_trav_area(Pointer temp, boolean merge_union);

		Pointer tcbuffer_to_tfloat(Pointer temp);

		Pointer tcbuffer_to_tgeompoint(Pointer temp);

		Pointer tgeometry_to_tcbuffer(Pointer temp);

		Pointer tcbuffer_expand(Pointer temp, double dist);

		Pointer tcbuffer_at_cbuffer(Pointer temp, Pointer cb);

		Pointer tcbuffer_at_geom(Pointer temp, Pointer gs);

		Pointer tcbuffer_at_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tcbuffer_minus_cbuffer(Pointer temp, Pointer cb);

		Pointer tcbuffer_minus_geom(Pointer temp, Pointer gs);

		Pointer tcbuffer_minus_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tdistance_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer tdistance_tcbuffer_geo(Pointer temp, Pointer gs);

		Pointer tdistance_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		double nad_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		double nad_tcbuffer_geo(Pointer temp, Pointer gs);

		double nad_tcbuffer_stbox(Pointer temp, Pointer box);

		double nad_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		Pointer nai_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer nai_tcbuffer_geo(Pointer temp, Pointer gs);

		Pointer nai_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		Pointer shortestline_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer shortestline_tcbuffer_geo(Pointer temp, Pointer gs);

		Pointer shortestline_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int always_eq_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		int always_eq_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int always_eq_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int always_ne_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		int always_ne_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int always_ne_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int ever_eq_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		int ever_eq_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int ever_eq_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int ever_ne_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		int ever_ne_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int ever_ne_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		Pointer teq_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		Pointer teq_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer tne_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		Pointer tne_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int acontains_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		int acontains_geo_tcbuffer(Pointer gs, Pointer temp);

		int acontains_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int acontains_tcbuffer_geo(Pointer temp, Pointer gs);

		int acovers_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		int acovers_geo_tcbuffer(Pointer gs, Pointer temp);

		int acovers_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int acovers_tcbuffer_geo(Pointer temp, Pointer gs);

		int adisjoint_tcbuffer_geo(Pointer temp, Pointer gs);

		int adisjoint_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int adisjoint_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int adwithin_tcbuffer_geo(Pointer temp, Pointer gs, double dist);

		int adwithin_tcbuffer_cbuffer(Pointer temp, Pointer cb, double dist);

		int adwithin_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2, double dist);

		int aintersects_tcbuffer_geo(Pointer temp, Pointer gs);

		int aintersects_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int aintersects_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int atouches_tcbuffer_geo(Pointer temp, Pointer gs);

		int atouches_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int atouches_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int econtains_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		int econtains_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int econtains_tcbuffer_geo(Pointer temp, Pointer gs);

		int ecovers_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		int ecovers_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int ecovers_tcbuffer_geo(Pointer temp, Pointer gs);

		int ecovers_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int edisjoint_tcbuffer_geo(Pointer temp, Pointer gs);

		int edisjoint_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int edwithin_tcbuffer_geo(Pointer temp, Pointer gs, double dist);

		int edwithin_tcbuffer_cbuffer(Pointer temp, Pointer cb, double dist);

		int edwithin_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2, double dist);

		int eintersects_tcbuffer_geo(Pointer temp, Pointer gs);

		int eintersects_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int eintersects_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int etouches_tcbuffer_geo(Pointer temp, Pointer gs);

		int etouches_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		int etouches_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		Pointer tcontains_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		Pointer tcontains_geo_tcbuffer(Pointer gs, Pointer temp);

		Pointer tcontains_tcbuffer_geo(Pointer temp, Pointer gs);

		Pointer tcontains_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer tcontains_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		Pointer tcovers_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		Pointer tcovers_geo_tcbuffer(Pointer gs, Pointer temp);

		Pointer tcovers_tcbuffer_geo(Pointer temp, Pointer gs);

		Pointer tcovers_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer tcovers_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		Pointer tdwithin_geo_tcbuffer(Pointer gs, Pointer temp, double dist);

		Pointer tdwithin_tcbuffer_geo(Pointer temp, Pointer gs, double dist);

		Pointer tdwithin_tcbuffer_cbuffer(Pointer temp, Pointer cb, double dist);

		Pointer tdwithin_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2, double dist);

		Pointer tdisjoint_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		Pointer tdisjoint_geo_tcbuffer(Pointer gs, Pointer temp);

		Pointer tdisjoint_tcbuffer_geo(Pointer temp, Pointer gs);

		Pointer tdisjoint_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer tdisjoint_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		Pointer tintersects_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		Pointer tintersects_geo_tcbuffer(Pointer gs, Pointer temp);

		Pointer tintersects_tcbuffer_geo(Pointer temp, Pointer gs);

		Pointer tintersects_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer tintersects_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		Pointer ttouches_geo_tcbuffer(Pointer gs, Pointer temp);

		Pointer ttouches_tcbuffer_geo(Pointer temp, Pointer gs);

		Pointer ttouches_cbuffer_tcbuffer(Pointer cb, Pointer temp);

		Pointer ttouches_tcbuffer_cbuffer(Pointer temp, Pointer cb);

		Pointer ttouches_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		int acovers_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2);

		long h3index_in(String str);

		String h3index_out(long cell);

		Pointer th3index_in(String str);

		Pointer th3indexinst_in(String str);

		Pointer th3indexseq_in(String str, int interp);

		Pointer th3indexseqset_in(String str);

		Pointer th3index_make(long value, long t);

		Pointer th3indexinst_make(long value, long t);

		Pointer th3indexseq_make(Pointer values, Pointer times, int count, boolean lower_inc, boolean upper_inc);

		Pointer th3indexseqset_make(Pointer sequences, int count);

		long th3index_start_value(Pointer temp);

		long th3index_end_value(Pointer temp);

		boolean th3index_value_n(Pointer temp, int n, Pointer result);

		Pointer th3index_values(Pointer temp, Pointer count);

		boolean th3index_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer result);

		Pointer tbigint_to_th3index(Pointer temp);

		Pointer th3index_to_tbigint(Pointer temp);

		int ever_eq_h3index_th3index(long cell, Pointer temp);

		int ever_eq_th3index_h3index(Pointer temp, long cell);

		int ever_ne_h3index_th3index(long cell, Pointer temp);

		int ever_ne_th3index_h3index(Pointer temp, long cell);

		int always_eq_h3index_th3index(long cell, Pointer temp);

		int always_eq_th3index_h3index(Pointer temp, long cell);

		int always_ne_h3index_th3index(long cell, Pointer temp);

		int always_ne_th3index_h3index(Pointer temp, long cell);

		int ever_eq_th3index_th3index(Pointer temp1, Pointer temp2);

		int ever_ne_th3index_th3index(Pointer temp1, Pointer temp2);

		int always_eq_th3index_th3index(Pointer temp1, Pointer temp2);

		int always_ne_th3index_th3index(Pointer temp1, Pointer temp2);

		Pointer teq_h3index_th3index(long cell, Pointer temp);

		Pointer teq_th3index_h3index(Pointer temp, long cell);

		Pointer teq_th3index_th3index(Pointer temp1, Pointer temp2);

		Pointer tne_h3index_th3index(long cell, Pointer temp);

		Pointer tne_th3index_h3index(Pointer temp, long cell);

		Pointer tne_th3index_th3index(Pointer temp1, Pointer temp2);

		Pointer th3index_get_resolution(Pointer temp);

		Pointer th3index_get_base_cell_number(Pointer temp);

		Pointer th3index_is_valid_cell(Pointer temp);

		Pointer th3index_is_res_class_iii(Pointer temp);

		Pointer th3index_is_pentagon(Pointer temp);

		Pointer th3index_cell_to_parent(Pointer temp, int resolution);

		Pointer th3index_cell_to_parent_next(Pointer temp);

		Pointer th3index_cell_to_center_child(Pointer temp, int resolution);

		Pointer th3index_cell_to_center_child_next(Pointer temp);

		Pointer th3index_cell_to_child_pos(Pointer temp, int parent_res);

		Pointer th3index_child_pos_to_cell(Pointer child_pos, Pointer parent, int child_res);

		Pointer tgeogpoint_to_th3index(Pointer temp, int resolution);

		Pointer tgeompoint_to_th3index(Pointer temp, int resolution);

		Pointer th3index_to_tgeogpoint(Pointer temp);

		Pointer th3index_to_tgeompoint(Pointer temp);

		Pointer th3index_cell_to_boundary(Pointer temp);

		Pointer geo_to_h3index_set(Pointer gs, int resolution);

		int ever_eq_h3indexset_th3index(Pointer cells, Pointer th3idx);

		Pointer th3index_are_neighbor_cells(Pointer origin, Pointer dest);

		Pointer th3index_cells_to_directed_edge(Pointer origin, Pointer dest);

		Pointer th3index_is_valid_directed_edge(Pointer edge);

		Pointer th3index_get_directed_edge_origin(Pointer edge);

		Pointer th3index_get_directed_edge_destination(Pointer edge);

		Pointer th3index_directed_edge_to_boundary(Pointer edge);

		Pointer th3index_cell_to_vertex(Pointer temp, int vertex_num);

		Pointer th3index_vertex_to_latlng(Pointer temp);

		Pointer th3index_is_valid_vertex(Pointer temp);

		Pointer th3index_grid_distance(Pointer origin, Pointer dest);

		Pointer th3index_cell_to_local_ij(Pointer origin, Pointer cell);

		Pointer th3index_local_ij_to_cell(Pointer origin, Pointer coord);

		Pointer th3index_cell_area(Pointer temp, String unit);

		Pointer th3index_edge_length(Pointer temp, String unit);

		Pointer tgeogpoint_great_circle_distance(Pointer a, Pointer b, String unit);

		Pointer gsl_get_generation_rng();

		Pointer gsl_get_aggregation_rng();

		int datum_ceil(Pointer d);

		int datum_degrees(Pointer d, Pointer normalize);

		int datum_float_round(Pointer value, Pointer size);

		int datum_floor(Pointer d);

		int datum_hash(Pointer d, int basetype);

		long datum_hash_extended(Pointer d, int basetype, long seed);

		int datum_radians(Pointer d);

		void floatspan_round_set(Pointer s, int maxdd, Pointer result);

		Pointer set_in(String str, int basetype);

		String set_out(Pointer s, int maxdd);

		Pointer span_in(String str, int spantype);

		String span_out(Pointer s, int maxdd);

		Pointer spanset_in(String str, int spantype);

		String spanset_out(Pointer ss, int maxdd);

		Pointer set_make(Pointer values, int count, int basetype, boolean order);

		Pointer set_make_exp(Pointer values, int count, int maxcount, int basetype, boolean order);

		Pointer set_make_free(Pointer values, int count, int basetype, boolean order);

		Pointer span_make(Pointer lower, Pointer upper, boolean lower_inc, boolean upper_inc, int basetype);

		void span_set(Pointer lower, Pointer upper, boolean lower_inc, boolean upper_inc, int basetype, int spantype, Pointer s);

		Pointer spanset_make_exp(Pointer spans, int count, int maxcount, boolean normalize, boolean order);

		Pointer spanset_make_free(Pointer spans, int count, boolean normalize, boolean order);

		Pointer set_span(Pointer s);

		Pointer set_spanset(Pointer s);

		void value_set_span(Pointer value, int basetype, Pointer s);

		Pointer value_set(Pointer d, int basetype);

		Pointer value_span(Pointer d, int basetype);

		Pointer value_spanset(Pointer d, int basetype);

		int numspan_width(Pointer s);

		int numspanset_width(Pointer ss, boolean boundspan);

		int set_end_value(Pointer s);

		int set_mem_size(Pointer s);

		void set_set_subspan(Pointer s, int minidx, int maxidx, Pointer result);

		void set_set_span(Pointer s, Pointer result);

		int set_start_value(Pointer s);

		boolean set_value_n(Pointer s, int n, Pointer result);

		Pointer set_vals(Pointer s);

		Pointer set_values(Pointer s);

		int spanset_lower(Pointer ss);

		int spanset_mem_size(Pointer ss);

		Pointer spanset_sps(Pointer ss);

		int spanset_upper(Pointer ss);

		void bigintspan_set_floatspan(Pointer s1, Pointer s2);

		void bigintspan_set_intspan(Pointer s1, Pointer s2);

		void datespan_set_tstzspan(Pointer s1, Pointer s2);

		void floatspan_set_bigintspan(Pointer s1, Pointer s2);

		void floatspan_set_intspan(Pointer s1, Pointer s2);

		void intspan_set_bigintspan(Pointer s1, Pointer s2);

		void intspan_set_floatspan(Pointer s1, Pointer s2);

		Pointer numset_shift_scale(Pointer s, Pointer shift, Pointer width, boolean hasshift, boolean haswidth);

		Pointer numspan_expand(Pointer s, Pointer value);

		Pointer numspan_shift_scale(Pointer s, Pointer shift, Pointer width, boolean hasshift, boolean haswidth);

		Pointer numspanset_shift_scale(Pointer ss, Pointer shift, Pointer width, boolean hasshift, boolean haswidth);

		Pointer set_compact(Pointer s);

		void span_expand(Pointer s1, Pointer s2);

		Pointer spanset_compact(Pointer ss);

		Pointer tbox_expand_value(Pointer box, Pointer value, int basetyp);

		Pointer textcat_textset_text_common(Pointer s, Pointer txt, boolean invert);

		void tstzspan_set_datespan(Pointer s1, Pointer s2);

		boolean adjacent_span_value(Pointer s, Pointer value);

		boolean adjacent_spanset_value(Pointer ss, Pointer value);

		boolean adjacent_value_spanset(Pointer value, Pointer ss);

		boolean contained_value_set(Pointer value, Pointer s);

		boolean contained_value_span(Pointer value, Pointer s);

		boolean contained_value_spanset(Pointer value, Pointer ss);

		boolean contains_set_value(Pointer s, Pointer value);

		boolean contains_span_value(Pointer s, Pointer value);

		boolean contains_spanset_value(Pointer ss, Pointer value);

		boolean ovadj_span_span(Pointer s1, Pointer s2);

		boolean left_set_value(Pointer s, Pointer value);

		boolean left_span_value(Pointer s, Pointer value);

		boolean left_spanset_value(Pointer ss, Pointer value);

		boolean left_value_set(Pointer value, Pointer s);

		boolean left_value_span(Pointer value, Pointer s);

		boolean left_value_spanset(Pointer value, Pointer ss);

		boolean lfnadj_span_span(Pointer s1, Pointer s2);

		boolean overleft_set_value(Pointer s, Pointer value);

		boolean overleft_span_value(Pointer s, Pointer value);

		boolean overleft_spanset_value(Pointer ss, Pointer value);

		boolean overleft_value_set(Pointer value, Pointer s);

		boolean overleft_value_span(Pointer value, Pointer s);

		boolean overleft_value_spanset(Pointer value, Pointer ss);

		boolean overright_set_value(Pointer s, Pointer value);

		boolean overright_span_value(Pointer s, Pointer value);

		boolean overright_spanset_value(Pointer ss, Pointer value);

		boolean overright_value_set(Pointer value, Pointer s);

		boolean overright_value_span(Pointer value, Pointer s);

		boolean overright_value_spanset(Pointer value, Pointer ss);

	}

	public interface MeosLibraryPartD {

		boolean right_value_set(Pointer value, Pointer s);

		boolean right_set_value(Pointer s, Pointer value);

		boolean right_value_span(Pointer value, Pointer s);

		boolean right_value_spanset(Pointer value, Pointer ss);

		boolean right_span_value(Pointer s, Pointer value);

		boolean right_spanset_value(Pointer ss, Pointer value);

		boolean bbox_type(int bboxtype);

		long bbox_get_size(int bboxtype);

		int bbox_max_dims(int bboxtype);

		boolean temporal_bbox_eq(Pointer box1, Pointer box2, int temptype);

		int temporal_bbox_cmp(Pointer box1, Pointer box2, int temptype);

		void bbox_union_span_span(Pointer s1, Pointer s2, Pointer result);

		boolean inter_span_span(Pointer s1, Pointer s2, Pointer result);

		Pointer intersection_set_value(Pointer s, Pointer value);

		Pointer intersection_span_value(Pointer s, Pointer value);

		Pointer intersection_spanset_value(Pointer ss, Pointer value);

		Pointer intersection_value_set(Pointer value, Pointer s);

		Pointer intersection_value_span(Pointer value, Pointer s);

		Pointer intersection_value_spanset(Pointer value, Pointer ss);

		int mi_span_span(Pointer s1, Pointer s2, Pointer result);

		Pointer minus_set_value(Pointer s, Pointer value);

		Pointer minus_span_value(Pointer s, Pointer value);

		Pointer minus_spanset_value(Pointer ss, Pointer value);

		Pointer minus_value_set(Pointer value, Pointer s);

		Pointer minus_value_span(Pointer value, Pointer s);

		Pointer minus_value_spanset(Pointer value, Pointer ss);

		Pointer super_union_span_span(Pointer s1, Pointer s2);

		Pointer union_set_value(Pointer s, Pointer value);

		Pointer union_span_value(Pointer s, Pointer value);

		Pointer union_spanset_value(Pointer ss, Pointer value);

		Pointer union_value_set(Pointer value, Pointer s);

		Pointer union_value_span(Pointer value, Pointer s);

		Pointer union_value_spanset(Pointer value, Pointer ss);

		int distance_set_set(Pointer s1, Pointer s2);

		int distance_set_value(Pointer s, Pointer value);

		int distance_span_span(Pointer s1, Pointer s2);

		int distance_span_value(Pointer s, Pointer value);

		int distance_spanset_span(Pointer ss, Pointer s);

		int distance_spanset_spanset(Pointer ss1, Pointer ss2);

		int distance_spanset_value(Pointer ss, Pointer value);

		int distance_value_value(Pointer l, Pointer r, int basetype);

		Pointer spanbase_extent_transfn(Pointer state, Pointer value, int basetype);

		Pointer value_union_transfn(Pointer state, Pointer value, int basetype);

		Pointer number_tstzspan_to_tbox(Pointer d, int basetype, Pointer s);

		Pointer number_timestamptz_to_tbox(Pointer d, int basetype, long t);

		void tbox_set(Pointer s, Pointer p, Pointer box);

		void float_set_tbox(double d, Pointer box);

		void int_set_tbox(int i, Pointer box);

		void number_set_tbox(Pointer d, int basetype, Pointer box);

		Pointer number_tbox(Pointer value, int basetype);

		void numset_set_tbox(Pointer s, Pointer box);

		void numspan_set_tbox(Pointer span, Pointer box);

		void timestamptz_set_tbox(long t, Pointer box);

		void tstzset_set_tbox(Pointer s, Pointer box);

		void tstzspan_set_tbox(Pointer s, Pointer box);

		Pointer tbox_shift_scale_value(Pointer box, Pointer shift, Pointer width, boolean hasshift, boolean haswidth);

		void tbox_expand(Pointer box1, Pointer box2);

		boolean inter_tbox_tbox(Pointer box1, Pointer box2, Pointer result);

		Pointer tboolinst_from_mfjson(Pointer mfjson);

		Pointer tboolinst_in(String str);

		Pointer tboolseq_from_mfjson(Pointer mfjson);

		Pointer tboolseq_in(String str, int interp);

		Pointer tboolseqset_from_mfjson(Pointer mfjson);

		Pointer tboolseqset_in(String str);

		Pointer temporal_in(String str, int temptype);

		String temporal_out(Pointer temp, int maxdd);

		Pointer temparr_out(Pointer temparr, int count, int maxdd);

		Pointer tfloatinst_from_mfjson(Pointer mfjson);

		Pointer tfloatinst_in(String str);

		Pointer tfloatseq_from_mfjson(Pointer mfjson, int interp);

		Pointer tfloatseq_in(String str, int interp);

		Pointer tfloatseqset_from_mfjson(Pointer mfjson, int interp);

		Pointer tfloatseqset_in(String str);

		Pointer tinstant_from_mfjson(Pointer mfjson, boolean spatial, int srid, int temptype);

		Pointer tinstant_in(String str, int temptype);

		String tinstant_out(Pointer inst, int maxdd);

		Pointer tintinst_from_mfjson(Pointer mfjson);

		Pointer tintinst_in(String str);

		Pointer tintseq_from_mfjson(Pointer mfjson);

		Pointer tintseq_in(String str, int interp);

		Pointer tintseqset_from_mfjson(Pointer mfjson);

		Pointer tintseqset_in(String str);

		Pointer tsequence_from_mfjson(Pointer mfjson, boolean spatial, int srid, int temptype, int interp);

		Pointer tsequence_in(String str, int temptype, int interp);

		String tsequence_out(Pointer seq, int maxdd);

		Pointer tsequenceset_from_mfjson(Pointer mfjson, boolean spatial, int srid, int temptype, int interp);

		Pointer tsequenceset_in(String str, int temptype, int interp);

		String tsequenceset_out(Pointer ss, int maxdd);

		Pointer ttextinst_from_mfjson(Pointer mfjson);

		Pointer ttextinst_in(String str);

		Pointer ttextseq_from_mfjson(Pointer mfjson);

		Pointer ttextseq_in(String str, int interp);

		Pointer ttextseqset_from_mfjson(Pointer mfjson);

		Pointer ttextseqset_in(String str);

		Pointer temporal_from_mfjson(String mfjson, int temptype);

		Pointer temporal_from_base_temp(Pointer value, int temptype, Pointer temp);

		Pointer tinstant_copy(Pointer inst);

		Pointer tinstant_make(Pointer value, int temptype, long t);

		Pointer tinstant_make_free(Pointer value, int temptype, long t);

		Pointer tsequence_copy(Pointer seq);

		Pointer tsequence_from_base_temp(Pointer value, int temptype, Pointer seq);

		Pointer tsequence_from_base_tstzset(Pointer value, int temptype, Pointer s);

		Pointer tsequence_from_base_tstzspan(Pointer value, int temptype, Pointer s, int interp);

		Pointer tsequence_make_exp(Pointer instants, int count, int maxcount, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequence_make_free(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequenceset_copy(Pointer ss);

		Pointer tseqsetarr_to_tseqset(Pointer seqsets, int count, int totalseqs);

		Pointer tsequenceset_from_base_temp(Pointer value, int temptype, Pointer ss);

		Pointer tsequenceset_from_base_tstzspanset(Pointer value, int temptype, Pointer ss, int interp);

		Pointer tsequenceset_make_exp(Pointer sequences, int count, int maxcount, boolean normalize);

		Pointer tsequenceset_make_free(Pointer sequences, int count, boolean normalize);

		void temporal_set_tstzspan(Pointer temp, Pointer s);

		void tinstant_set_tstzspan(Pointer inst, Pointer s);

		void tnumber_set_tbox(Pointer temp, Pointer box);

		void tnumberinst_set_tbox(Pointer inst, Pointer box);

		void tnumberseq_set_tbox(Pointer seq, Pointer box);

		void tnumberseqset_set_tbox(Pointer ss, Pointer box);

		void tsequence_set_tstzspan(Pointer seq, Pointer s);

		void tsequenceset_set_tstzspan(Pointer ss, Pointer s);

		Pointer temporal_end_inst(Pointer temp);

		int temporal_end_value(Pointer temp);

		Pointer temporal_inst_n(Pointer temp, int n);

		Pointer temporal_insts_p(Pointer temp, Pointer count);

		Pointer temporal_max_inst_p(Pointer temp);

		int temporal_max_value(Pointer temp);

		long temporal_mem_size(Pointer temp);

		Pointer temporal_min_inst_p(Pointer temp);

		int temporal_min_value(Pointer temp);

		Pointer temporal_sequences_p(Pointer temp, Pointer count);

		void temporal_set_bbox(Pointer temp, Pointer box);

		Pointer temporal_start_inst(Pointer temp);

		int temporal_start_value(Pointer temp);

		Pointer temporal_values_p(Pointer temp, Pointer count);

		boolean temporal_value_n(Pointer temp, int n, Pointer result);

		Pointer temporal_values(Pointer temp, Pointer count);

		int tinstant_hash(Pointer inst);

		Pointer tinstant_insts(Pointer inst, Pointer count);

		void tinstant_set_bbox(Pointer inst, Pointer box);

		Pointer tinstant_time(Pointer inst);

		Pointer tinstant_timestamps(Pointer inst, Pointer count);

		int tinstant_value_p(Pointer inst);

		int tinstant_value(Pointer inst);

		boolean tinstant_value_at_timestamptz(Pointer inst, long t, Pointer result);

		Pointer tinstant_values_p(Pointer inst, Pointer count);

		void tnumber_set_span(Pointer temp, Pointer span);

		Pointer tnumberinst_valuespans(Pointer inst);

		double tnumberseq_avg_val(Pointer seq);

		Pointer tnumberseq_valuespans(Pointer seq);

		double tnumberseqset_avg_val(Pointer ss);

		Pointer tnumberseqset_valuespans(Pointer ss);

		Pointer tsequence_duration(Pointer seq);

		long tsequence_end_timestamptz(Pointer seq);

		int tsequence_hash(Pointer seq);

		Pointer tsequence_insts_p(Pointer seq);

		Pointer tsequence_max_inst_p(Pointer seq);

		int tsequence_max_val(Pointer seq);

		Pointer tsequence_min_inst_p(Pointer seq);

		int tsequence_min_val(Pointer seq);

		Pointer tsequence_segments(Pointer seq, Pointer count);

		Pointer tsequence_seqs(Pointer seq, Pointer count);

		long tsequence_start_timestamptz(Pointer seq);

		Pointer tsequence_time(Pointer seq);

		Pointer tsequence_timestamps(Pointer seq, Pointer count);

		boolean tsequence_value_at_timestamptz(Pointer seq, long t, boolean strict, Pointer result);

		Pointer tsequence_values_p(Pointer seq, Pointer count);

		Pointer tsequenceset_duration(Pointer ss, boolean boundspan);

		long tsequenceset_end_timestamptz(Pointer ss);

		int tsequenceset_hash(Pointer ss);

		Pointer tsequenceset_inst_n(Pointer ss, int n);

		Pointer tsequenceset_insts_p(Pointer ss);

		Pointer tsequenceset_max_inst_p(Pointer ss);

		int tsequenceset_max_val(Pointer ss);

		Pointer tsequenceset_min_inst_p(Pointer ss);

		int tsequenceset_min_val(Pointer ss);

		int tsequenceset_num_instants(Pointer ss);

		int tsequenceset_num_timestamps(Pointer ss);

		Pointer tsequenceset_segments(Pointer ss, Pointer count);

		Pointer tsequenceset_sequences_p(Pointer ss);

		long tsequenceset_start_timestamptz(Pointer ss);

		Pointer tsequenceset_time(Pointer ss);

		boolean tsequenceset_timestamptz_n(Pointer ss, int n, Pointer result);

		Pointer tsequenceset_timestamps(Pointer ss, Pointer count);

		boolean tsequenceset_value_at_timestamptz(Pointer ss, long t, boolean strict, Pointer result);

		boolean tsequenceset_value_n(Pointer ss, int n, Pointer result);

		Pointer tsequenceset_values_p(Pointer ss, Pointer count);

		void temporal_restart(Pointer temp, int count);

		Pointer temporal_tsequence(Pointer temp, int interp);

		Pointer temporal_tsequenceset(Pointer temp, int interp);

		Pointer tinstant_shift_time(Pointer inst, Pointer interv);

		Pointer tinstant_to_tsequence(Pointer inst, int interp);

		Pointer tinstant_to_tsequence_free(Pointer inst, int interp);

		Pointer tinstant_to_tsequenceset(Pointer inst, int interp);

		Pointer tnumber_shift_scale_value(Pointer temp, Pointer shift, Pointer width, boolean hasshift, boolean haswidth);

		Pointer tnumberinst_shift_value(Pointer inst, Pointer shift);

		Pointer tnumberseq_shift_scale_value(Pointer seq, Pointer shift, Pointer width, boolean hasshift, boolean haswidth);

		Pointer tnumberseqset_shift_scale_value(Pointer ss, Pointer start, Pointer width, boolean hasshift, boolean haswidth);

		void tsequence_restart(Pointer seq, int count);

		Pointer tsequence_set_interp(Pointer seq, int interp);

		Pointer tsequence_shift_scale_time(Pointer seq, Pointer shift, Pointer duration);

		Pointer tsequence_subseq(Pointer seq, int from, int to, boolean lower_inc, boolean upper_inc);

		Pointer tsequence_to_tinstant(Pointer seq);

		Pointer tsequence_to_tsequenceset(Pointer seq);

		Pointer tsequence_to_tsequenceset_free(Pointer seq);

		Pointer tsequence_to_tsequenceset_interp(Pointer seq, int interp);

		void tsequenceset_restart(Pointer ss, int count);

		Pointer tsequenceset_set_interp(Pointer ss, int interp);

		Pointer tsequenceset_shift_scale_time(Pointer ss, Pointer start, Pointer duration);

		Pointer tsequenceset_to_discrete(Pointer ss);

		Pointer tsequenceset_to_linear(Pointer ss);

		Pointer tsequenceset_to_step(Pointer ss);

		Pointer tsequenceset_to_tinstant(Pointer ss);

		Pointer tsequenceset_to_tsequence(Pointer ss);

		Pointer tinstant_merge(Pointer inst1, Pointer inst2);

		Pointer tinstant_merge_array(Pointer instants, int count);

		Pointer tsequence_append_tinstant(Pointer seq, Pointer inst, double maxdist, Pointer maxt, boolean expand);

		Pointer tsequence_append_tsequence(Pointer seq1, Pointer seq2, boolean expand);

		Pointer tsequence_delete_timestamptz(Pointer seq, long t, boolean connect);

		Pointer tsequence_delete_tstzset(Pointer seq, Pointer s, boolean connect);

		Pointer tsequence_delete_tstzspan(Pointer seq, Pointer s, boolean connect);

		Pointer tsequence_delete_tstzspanset(Pointer seq, Pointer ss, boolean connect);

		Pointer tsequence_insert(Pointer seq1, Pointer seq2, boolean connect);

		Pointer tsequence_merge(Pointer seq1, Pointer seq2);

		Pointer tsequence_merge_array(Pointer sequences, int count);

		Pointer tsequenceset_append_tinstant(Pointer ss, Pointer inst, double maxdist, Pointer maxt, boolean expand);

		Pointer tsequenceset_append_tsequence(Pointer ss, Pointer seq, boolean expand);

		Pointer tsequenceset_delete_timestamptz(Pointer ss, long t);

		Pointer tsequenceset_delete_tstzset(Pointer ss, Pointer s);

		Pointer tsequenceset_delete_tstzspan(Pointer ss, Pointer s);

		Pointer tsequenceset_delete_tstzspanset(Pointer ss, Pointer ps);

		Pointer tsequenceset_insert(Pointer ss1, Pointer ss2);

		Pointer tsequenceset_merge(Pointer ss1, Pointer ss2);

		Pointer tsequenceset_merge_array(Pointer seqsets, int count);

		void tsequence_expand_bbox(Pointer seq, Pointer inst);

		void tsequence_set_bbox(Pointer seq, Pointer box);

		void tsequenceset_expand_bbox(Pointer ss, Pointer seq);

		void tsequenceset_set_bbox(Pointer ss, Pointer box);

		Pointer tcontseq_after_timestamptz(Pointer seq, long t, boolean strict);

		Pointer tcontseq_before_timestamptz(Pointer seq, long t, boolean strict);

		Pointer tcontseq_restrict_minmax(Pointer seq, boolean min, boolean atfunc);

		Pointer tdiscseq_after_timestamptz(Pointer seq, long t, boolean strict);

		Pointer tdiscseq_before_timestamptz(Pointer seq, long t, boolean strict);

		Pointer tdiscseq_restrict_minmax(Pointer seq, boolean min, boolean atfunc);

		boolean temporal_bbox_restrict_set(Pointer temp, Pointer set);

		Pointer temporal_restrict_minmax(Pointer temp, boolean min, boolean atfunc);

		Pointer temporal_restrict_timestamptz(Pointer temp, long t, boolean atfunc);

		Pointer temporal_restrict_tstzset(Pointer temp, Pointer s, boolean atfunc);

		Pointer temporal_restrict_tstzspan(Pointer temp, Pointer s, boolean atfunc);

		Pointer temporal_restrict_tstzspanset(Pointer temp, Pointer ss, boolean atfunc);

		Pointer temporal_restrict_value(Pointer temp, Pointer value, boolean atfunc);

		Pointer temporal_restrict_values(Pointer temp, Pointer set, boolean atfunc);

		boolean temporal_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer result);

		Pointer tinstant_after_timestamptz(Pointer inst, long t, boolean strict);

		Pointer tinstant_before_timestamptz(Pointer inst, long t, boolean strict);

		Pointer tinstant_restrict_tstzspan(Pointer inst, Pointer period, boolean atfunc);

		Pointer tinstant_restrict_tstzspanset(Pointer inst, Pointer ss, boolean atfunc);

		Pointer tinstant_restrict_timestamptz(Pointer inst, long t, boolean atfunc);

		Pointer tinstant_restrict_tstzset(Pointer inst, Pointer s, boolean atfunc);

		Pointer tinstant_restrict_value(Pointer inst, Pointer value, boolean atfunc);

		Pointer tinstant_restrict_values(Pointer inst, Pointer set, boolean atfunc);

		Pointer tnumber_restrict_span(Pointer temp, Pointer span, boolean atfunc);

		Pointer tnumber_restrict_spanset(Pointer temp, Pointer ss, boolean atfunc);

		Pointer tnumberinst_restrict_span(Pointer inst, Pointer span, boolean atfunc);

		Pointer tnumberinst_restrict_spanset(Pointer inst, Pointer ss, boolean atfunc);

		Pointer tnumberseqset_restrict_span(Pointer ss, Pointer span, boolean atfunc);

		Pointer tnumberseqset_restrict_spanset(Pointer ss, Pointer spanset, boolean atfunc);

		Pointer tsequence_at_timestamptz(Pointer seq, long t);

		Pointer tsequence_restrict_tstzspan(Pointer seq, Pointer s, boolean atfunc);

		Pointer tsequence_restrict_tstzspanset(Pointer seq, Pointer ss, boolean atfunc);

		Pointer tsequenceset_after_timestamptz(Pointer ss, long t, boolean strict);

		Pointer tsequenceset_before_timestamptz(Pointer ss, long t, boolean strict);

		Pointer tsequenceset_restrict_minmax(Pointer ss, boolean min, boolean atfunc);

		Pointer tsequenceset_restrict_tstzspan(Pointer ss, Pointer s, boolean atfunc);

		Pointer tsequenceset_restrict_tstzspanset(Pointer ss, Pointer ps, boolean atfunc);

		Pointer tsequenceset_restrict_timestamptz(Pointer ss, long t, boolean atfunc);

		Pointer tsequenceset_restrict_tstzset(Pointer ss, Pointer s, boolean atfunc);

		Pointer tsequenceset_restrict_value(Pointer ss, Pointer value, boolean atfunc);

		Pointer tsequenceset_restrict_values(Pointer ss, Pointer s, boolean atfunc);

		int tinstant_cmp(Pointer inst1, Pointer inst2);

		boolean tinstant_eq(Pointer inst1, Pointer inst2);

		int tsequence_cmp(Pointer seq1, Pointer seq2);

		boolean tsequence_eq(Pointer seq1, Pointer seq2);

		int tsequenceset_cmp(Pointer ss1, Pointer ss2);

		boolean tsequenceset_eq(Pointer ss1, Pointer ss2);

		int always_eq_base_temporal(Pointer value, Pointer temp);

		int always_eq_temporal_base(Pointer temp, Pointer value);

		int always_ne_base_temporal(Pointer value, Pointer temp);

		int always_ne_temporal_base(Pointer temp, Pointer value);

		int always_ge_base_temporal(Pointer value, Pointer temp);

		int always_ge_temporal_base(Pointer temp, Pointer value);

		int always_gt_base_temporal(Pointer value, Pointer temp);

		int always_gt_temporal_base(Pointer temp, Pointer value);

		int always_le_base_temporal(Pointer value, Pointer temp);

		int always_le_temporal_base(Pointer temp, Pointer value);

		int always_lt_base_temporal(Pointer value, Pointer temp);

		int always_lt_temporal_base(Pointer temp, Pointer value);

		int ever_eq_base_temporal(Pointer value, Pointer temp);

		int ever_eq_temporal_base(Pointer temp, Pointer value);

		int ever_ne_base_temporal(Pointer value, Pointer temp);

		int ever_ne_temporal_base(Pointer temp, Pointer value);

		int ever_ge_base_temporal(Pointer value, Pointer temp);

		int ever_ge_temporal_base(Pointer temp, Pointer value);

		int ever_gt_base_temporal(Pointer value, Pointer temp);

		int ever_gt_temporal_base(Pointer temp, Pointer value);

		int ever_le_base_temporal(Pointer value, Pointer temp);

		int ever_le_temporal_base(Pointer temp, Pointer value);

		int ever_lt_base_temporal(Pointer value, Pointer temp);

		int ever_lt_temporal_base(Pointer temp, Pointer value);

		Pointer tnumberinst_abs(Pointer inst);

		Pointer tnumberseq_abs(Pointer seq);

		Pointer tnumberseq_angular_difference(Pointer seq);

		Pointer tnumberseq_delta_value(Pointer seq);

		Pointer tnumberseqset_abs(Pointer ss);

		Pointer tnumberseqset_angular_difference(Pointer ss);

		Pointer tnumberseqset_delta_value(Pointer ss);

		double distance_span_span_double(Pointer s1, Pointer s2);

		double nad_tbox_tbox(Pointer box1, Pointer box2);

		double nad_tnumber_number(Pointer temp, Pointer value);

		double nad_tnumber_tbox(Pointer temp, Pointer box);

		double nad_tnumber_tnumber(Pointer temp1, Pointer temp2);

		Pointer tdistance_tnumber_number(Pointer temp, Pointer value);

		double tnumberinst_distance(Pointer inst1, Pointer inst2);

		double tnumberseq_integral(Pointer seq);

		double tnumberseq_twavg(Pointer seq);

		double tnumberseqset_integral(Pointer ss);

		double tnumberseqset_twavg(Pointer ss);

		Pointer temporal_compact(Pointer temp);

		Pointer tsequence_compact(Pointer seq);

		Pointer tsequenceset_compact(Pointer ss);

		Pointer temporal_skiplist_make();

		Pointer skiplist_make(long key_size, long value_size, Pointer comp_fn, Pointer merge_fn);

		int skiplist_search(Pointer list, Pointer key, Pointer value);

		void skiplist_free(Pointer list);

		void skiplist_splice(Pointer list, Pointer keys, Pointer values, int count, Pointer func, boolean crossings, int sktype);

		void temporal_skiplist_splice(Pointer list, Pointer values, int count, Pointer func, boolean crossings);

		Pointer skiplist_values(Pointer list);

		Pointer skiplist_keys_values(Pointer list, Pointer values);

		Pointer temporal_app_tinst_transfn(Pointer state, Pointer inst, int interp, double maxdist, Pointer maxt);

		Pointer temporal_app_tseq_transfn(Pointer state, Pointer seq);

		Pointer span_bins(Pointer s, Pointer size, Pointer origin, Pointer count);

		Pointer spanset_bins(Pointer ss, Pointer size, Pointer origin, Pointer count);

		Pointer tnumber_value_bins(Pointer temp, Pointer size, Pointer origin, Pointer count);

		Pointer tnumber_value_time_boxes(Pointer temp, Pointer vsize, Pointer duration, Pointer vorigin, long torigin, Pointer count);

		Pointer tnumber_value_split(Pointer temp, Pointer vsize, Pointer vorigin, Pointer bins, Pointer count);

		Pointer tbox_get_value_time_tile(Pointer value, long t, Pointer vsize, Pointer duration, Pointer vorigin, long torigin, int basetype, int spantype);

		Pointer tnumber_value_time_split(Pointer temp, Pointer size, Pointer duration, Pointer vorigin, long torigin, Pointer value_bins, Pointer time_bins, Pointer count);

		Pointer proj_get_context();

		int datum_geo_round(Pointer value, Pointer size);

		Pointer point_round(Pointer gs, int maxdd);

		void stbox_set(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s, Pointer box);

		void gbox_set_stbox(Pointer box, int srid, Pointer result);

		boolean geo_set_stbox(Pointer gs, Pointer box);

		void geoarr_set_stbox(Pointer values, int count, Pointer box);

		boolean spatial_set_stbox(Pointer d, int basetype, Pointer box);

		void spatialset_set_stbox(Pointer set, Pointer box);

		void stbox_set_box3d(Pointer box, Pointer box3d);

		void stbox_set_gbox(Pointer box, Pointer gbox);

		void tstzset_set_stbox(Pointer s, Pointer box);

		void tstzspan_set_stbox(Pointer s, Pointer box);

		void tstzspanset_set_stbox(Pointer s, Pointer box);

		void stbox_expand(Pointer box1, Pointer box2);

		boolean inter_stbox_stbox(Pointer box1, Pointer box2, Pointer result);

		Pointer stbox_geo(Pointer box);

		Pointer tgeogpointinst_from_mfjson(Pointer mfjson, int srid);

		Pointer tgeogpointinst_in(String str);

		Pointer tgeogpointseq_from_mfjson(Pointer mfjson, int srid, int interp);

		Pointer tgeogpointseq_in(String str, int interp);

		Pointer tgeogpointseqset_from_mfjson(Pointer mfjson, int srid, int interp);

		Pointer tgeogpointseqset_in(String str);

		Pointer tgeompointinst_from_mfjson(Pointer mfjson, int srid);

		Pointer tgeompointinst_in(String str);

		Pointer tgeompointseq_from_mfjson(Pointer mfjson, int srid, int interp);

		Pointer tgeompointseq_in(String str, int interp);

		Pointer tgeompointseqset_from_mfjson(Pointer mfjson, int srid, int interp);

		Pointer tgeompointseqset_in(String str);

		Pointer tgeographyinst_from_mfjson(Pointer mfjson, int srid);

		Pointer tgeographyinst_in(String str);

		Pointer tgeographyseq_from_mfjson(Pointer mfjson, int srid, int interp);

		Pointer tgeographyseq_in(String str, int interp);

		Pointer tgeographyseqset_from_mfjson(Pointer mfjson, int srid, int interp);

		Pointer tgeographyseqset_in(String str);

		Pointer tgeometryinst_from_mfjson(Pointer mfjson, int srid);

		Pointer tgeometryinst_in(String str);

		Pointer tgeometryseq_from_mfjson(Pointer mfjson, int srid, int interp);

		Pointer tgeometryseq_in(String str, int interp);

		Pointer tgeometryseqset_from_mfjson(Pointer mfjson, int srid, int interp);

		Pointer tgeometryseqset_in(String str);

		void tspatial_set_stbox(Pointer temp, Pointer box);

		void tgeoinst_set_stbox(Pointer inst, Pointer box);

		void tspatialseq_set_stbox(Pointer seq, Pointer box);

		void tspatialseqset_set_stbox(Pointer ss, Pointer box);

		Pointer tgeo_restrict_elevation(Pointer temp, Pointer s, boolean atfunc);

		Pointer tgeo_restrict_geom(Pointer temp, Pointer gs, boolean atfunc);

		Pointer tgeo_restrict_stbox(Pointer temp, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tgeoinst_restrict_geom(Pointer inst, Pointer gs, boolean atfunc);

		Pointer tgeoinst_restrict_stbox(Pointer inst, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tgeoseq_restrict_geom(Pointer seq, Pointer gs, boolean atfunc);

		Pointer tgeoseq_restrict_stbox(Pointer seq, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tgeoseqset_restrict_geom(Pointer ss, Pointer gs, boolean atfunc);

		Pointer tgeoseqset_restrict_stbox(Pointer ss, Pointer box, boolean border_inc, boolean atfunc);

		int spatial_srid(Pointer d, int basetype);

		boolean spatial_set_srid(Pointer d, int basetype, int srid);

		int tspatialinst_srid(Pointer inst);

		Pointer tpointseq_azimuth(Pointer seq);

		Pointer tpointseq_cumulative_length(Pointer seq, double prevlength);

		boolean tpointseq_is_simple(Pointer seq);

		double tpointseq_length(Pointer seq);

		Pointer tpointseq_linear_trajectory(Pointer seq, boolean unary_union);

		Pointer tgeoseq_stboxes(Pointer seq, Pointer count);

		Pointer tgeoseq_split_n_stboxes(Pointer seq, int max_count, Pointer count);

		Pointer tpointseqset_azimuth(Pointer ss);

		Pointer tpointseqset_cumulative_length(Pointer ss);

		boolean tpointseqset_is_simple(Pointer ss);

		double tpointseqset_length(Pointer ss);

		Pointer tgeoseqset_stboxes(Pointer ss, Pointer count);

		Pointer tgeoseqset_split_n_stboxes(Pointer ss, int max_count, Pointer count);

		Pointer tpoint_get_coord(Pointer temp, int coord);

		Pointer tgeominst_tgeoginst(Pointer inst, boolean oper);

		Pointer tgeomseq_tgeogseq(Pointer seq, boolean oper);

		Pointer tgeomseqset_tgeogseqset(Pointer ss, boolean oper);

		Pointer tgeom_tgeog(Pointer temp, boolean oper);

		Pointer tgeo_tpoint(Pointer temp, boolean oper);

		void tspatialinst_set_srid(Pointer inst, int srid);

		Pointer tpointseq_make_simple(Pointer seq, Pointer count);

		void tspatialseq_set_srid(Pointer seq, int srid);

		Pointer tpointseqset_make_simple(Pointer ss, Pointer count);

		void tspatialseqset_set_srid(Pointer ss, int srid);

		Pointer tpointseq_twcentroid(Pointer seq);

		Pointer tpointseqset_twcentroid(Pointer ss);

		String npoint_as_ewkt(Pointer np, int maxdd);

		String npoint_as_hexwkb(Pointer np, byte variant, Pointer size_out);

		String npoint_as_text(Pointer np, int maxdd);

		Pointer npoint_as_wkb(Pointer np, byte variant, Pointer size_out);

		Pointer npoint_from_hexwkb(String hexwkb);

		Pointer npoint_from_wkb(Pointer wkb, long size);

		Pointer npoint_in(String str);

		String npoint_out(Pointer np, int maxdd);

		Pointer nsegment_in(String str);

		String nsegment_out(Pointer ns, int maxdd);

		Pointer npoint_make(long rid, double pos);

		Pointer nsegment_make(long rid, double pos1, double pos2);

		Pointer geompoint_to_npoint(Pointer gs);

		Pointer geom_to_nsegment(Pointer gs);

		Pointer npoint_to_geompoint(Pointer np);

		Pointer npoint_to_nsegment(Pointer np);

		Pointer npoint_to_stbox(Pointer np);

		Pointer nsegment_to_geom(Pointer ns);

		Pointer nsegment_to_stbox(Pointer np);

		int npoint_hash(Pointer np);

		long npoint_hash_extended(Pointer np, long seed);

		double npoint_position(Pointer np);

		long npoint_route(Pointer np);

		double nsegment_end_position(Pointer ns);

		long nsegment_route(Pointer ns);

		double nsegment_start_position(Pointer ns);

		boolean route_exists(long rid);

		Pointer route_geom(long rid);

		double route_length(long rid);

		Pointer npoint_round(Pointer np, int maxdd);

		Pointer nsegment_round(Pointer ns, int maxdd);

		int get_srid_ways();

		int npoint_srid(Pointer np);

		int nsegment_srid(Pointer ns);

		Pointer npoint_timestamptz_to_stbox(Pointer np, long t);

		Pointer npoint_tstzspan_to_stbox(Pointer np, Pointer s);

		int npoint_cmp(Pointer np1, Pointer np2);

		boolean npoint_eq(Pointer np1, Pointer np2);

		boolean npoint_ge(Pointer np1, Pointer np2);

		boolean npoint_gt(Pointer np1, Pointer np2);

		boolean npoint_le(Pointer np1, Pointer np2);

		boolean npoint_lt(Pointer np1, Pointer np2);

		boolean npoint_ne(Pointer np1, Pointer np2);

		boolean npoint_same(Pointer np1, Pointer np2);

		int nsegment_cmp(Pointer ns1, Pointer ns2);

		boolean nsegment_eq(Pointer ns1, Pointer ns2);

		boolean nsegment_ge(Pointer ns1, Pointer ns2);

		boolean nsegment_gt(Pointer ns1, Pointer ns2);

		boolean nsegment_le(Pointer ns1, Pointer ns2);

		boolean nsegment_lt(Pointer ns1, Pointer ns2);

		boolean nsegment_ne(Pointer ns1, Pointer ns2);

		Pointer npointset_in(String str);

		String npointset_out(Pointer s, int maxdd);

		Pointer npointset_make(Pointer values, int count);

		Pointer npoint_to_set(Pointer np);

		Pointer npointset_end_value(Pointer s);

		Pointer npointset_routes(Pointer s);

		Pointer npointset_start_value(Pointer s);

		boolean npointset_value_n(Pointer s, int n, Pointer result);

		Pointer npointset_values(Pointer s);

		boolean contained_npoint_set(Pointer np, Pointer s);

		boolean contains_set_npoint(Pointer s, Pointer np);

		Pointer intersection_npoint_set(Pointer np, Pointer s);

		Pointer intersection_set_npoint(Pointer s, Pointer np);

		Pointer minus_npoint_set(Pointer np, Pointer s);

		Pointer minus_set_npoint(Pointer s, Pointer np);

		Pointer npoint_union_transfn(Pointer state, Pointer np);

		Pointer union_npoint_set(Pointer np, Pointer s);

		Pointer union_set_npoint(Pointer s, Pointer np);

		Pointer tnpoint_in(String str);

		Pointer tnpoint_from_mfjson(String mfjson);

		String tnpoint_out(Pointer temp, int maxdd);

		Pointer tnpointinst_make(Pointer np, long t);

		Pointer tgeompoint_to_tnpoint(Pointer temp);

		Pointer tnpoint_to_tgeompoint(Pointer temp);

		Pointer tnpoint_cumulative_length(Pointer temp);

		double tnpoint_length(Pointer temp);

		Pointer tnpoint_positions(Pointer temp, Pointer count);

		long tnpoint_route(Pointer temp);

		Pointer tnpoint_routes(Pointer temp);

		Pointer tnpoint_speed(Pointer temp);

		Pointer tnpoint_trajectory(Pointer temp);

		Pointer tnpoint_twcentroid(Pointer temp);

		Pointer tnpoint_at_geom(Pointer temp, Pointer gs);

		Pointer tnpoint_at_npoint(Pointer temp, Pointer np);

		Pointer tnpoint_at_npointset(Pointer temp, Pointer s);

		Pointer tnpoint_at_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tnpoint_minus_geom(Pointer temp, Pointer gs);

		Pointer tnpoint_minus_npoint(Pointer temp, Pointer np);

		Pointer tnpoint_minus_npointset(Pointer temp, Pointer s);

		Pointer tnpoint_minus_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tdistance_tnpoint_npoint(Pointer temp, Pointer np);

		Pointer tdistance_tnpoint_point(Pointer temp, Pointer gs);

		Pointer tdistance_tnpoint_tnpoint(Pointer temp1, Pointer temp2);

		double nad_tnpoint_geo(Pointer temp, Pointer gs);

		double nad_tnpoint_npoint(Pointer temp, Pointer np);

		double nad_tnpoint_stbox(Pointer temp, Pointer box);

		double nad_tnpoint_tnpoint(Pointer temp1, Pointer temp2);

		Pointer nai_tnpoint_geo(Pointer temp, Pointer gs);

		Pointer nai_tnpoint_npoint(Pointer temp, Pointer np);

		Pointer nai_tnpoint_tnpoint(Pointer temp1, Pointer temp2);

		Pointer shortestline_tnpoint_geo(Pointer temp, Pointer gs);

		Pointer shortestline_tnpoint_npoint(Pointer temp, Pointer np);

		Pointer shortestline_tnpoint_tnpoint(Pointer temp1, Pointer temp2);

		Pointer tnpoint_tcentroid_transfn(Pointer state, Pointer temp);

		int always_eq_npoint_tnpoint(Pointer np, Pointer temp);

		int always_eq_tnpoint_npoint(Pointer temp, Pointer np);

		int always_eq_tnpoint_tnpoint(Pointer temp1, Pointer temp2);

		int always_ne_npoint_tnpoint(Pointer np, Pointer temp);

		int always_ne_tnpoint_npoint(Pointer temp, Pointer np);

		int always_ne_tnpoint_tnpoint(Pointer temp1, Pointer temp2);

		int ever_eq_npoint_tnpoint(Pointer np, Pointer temp);

		int ever_eq_tnpoint_npoint(Pointer temp, Pointer np);

		int ever_eq_tnpoint_tnpoint(Pointer temp1, Pointer temp2);

		int ever_ne_npoint_tnpoint(Pointer np, Pointer temp);

		int ever_ne_tnpoint_npoint(Pointer temp, Pointer np);

		int ever_ne_tnpoint_tnpoint(Pointer temp1, Pointer temp2);

		Pointer teq_tnpoint_npoint(Pointer temp, Pointer np);

		Pointer tne_tnpoint_npoint(Pointer temp, Pointer np);

		String pose_as_ewkt(Pointer pose, int maxdd);

		String pose_as_hexwkb(Pointer pose, byte variant, Pointer size);

		String pose_as_text(Pointer pose, int maxdd);

		Pointer pose_as_wkb(Pointer pose, byte variant, Pointer size_out);

		Pointer pose_from_wkb(Pointer wkb, long size);

		Pointer pose_from_hexwkb(String hexwkb);

		Pointer pose_in(String str);

		String pose_out(Pointer pose, int maxdd);

		Pointer pose_copy(Pointer pose);

		Pointer pose_make_2d(double x, double y, double theta, int srid);

		Pointer pose_make_3d(double x, double y, double z, double W, double X, double Y, double Z, int srid);

		Pointer pose_make_point2d(Pointer gs, double theta);

		Pointer pose_make_point3d(Pointer gs, double W, double X, double Y, double Z);

		Pointer pose_to_point(Pointer pose);

		Pointer pose_to_stbox(Pointer pose);

		int pose_hash(Pointer pose);

		long pose_hash_extended(Pointer pose, long seed);

		Pointer pose_orientation(Pointer pose);

		double pose_rotation(Pointer pose);

		Pointer pose_round(Pointer pose, int maxdd);

		Pointer posearr_round(Pointer posearr, int count, int maxdd);

		void pose_set_srid(Pointer pose, int srid);

		int pose_srid(Pointer pose);

		Pointer pose_transform(Pointer pose, int srid);

		Pointer pose_transform_pipeline(Pointer pose, String pipelinestr, int srid, boolean is_forward);

		Pointer pose_tstzspan_to_stbox(Pointer pose, Pointer s);

		Pointer pose_timestamptz_to_stbox(Pointer pose, long t);

		double distance_pose_geo(Pointer pose, Pointer gs);

		double distance_pose_pose(Pointer pose1, Pointer pose2);

		double distance_pose_stbox(Pointer pose, Pointer box);

		int pose_cmp(Pointer pose1, Pointer pose2);

		boolean pose_eq(Pointer pose1, Pointer pose2);

		boolean pose_ge(Pointer pose1, Pointer pose2);

		boolean pose_gt(Pointer pose1, Pointer pose2);

		boolean pose_le(Pointer pose1, Pointer pose2);

		boolean pose_lt(Pointer pose1, Pointer pose2);

		boolean pose_ne(Pointer pose1, Pointer pose2);

		boolean pose_nsame(Pointer pose1, Pointer pose2);

		boolean pose_same(Pointer pose1, Pointer pose2);

		Pointer poseset_in(String str);

		String poseset_out(Pointer s, int maxdd);

		Pointer poseset_make(Pointer values, int count);

		Pointer pose_to_set(Pointer pose);

		Pointer poseset_end_value(Pointer s);

		Pointer poseset_start_value(Pointer s);

		boolean poseset_value_n(Pointer s, int n, Pointer result);

		Pointer poseset_values(Pointer s);

		boolean contained_pose_set(Pointer pose, Pointer s);

		boolean contains_set_pose(Pointer s, Pointer pose);

		Pointer intersection_pose_set(Pointer pose, Pointer s);

		Pointer intersection_set_pose(Pointer s, Pointer pose);

		Pointer minus_pose_set(Pointer pose, Pointer s);

		Pointer minus_set_pose(Pointer s, Pointer pose);

		Pointer pose_union_transfn(Pointer state, Pointer pose);

		Pointer union_pose_set(Pointer pose, Pointer s);

		Pointer union_set_pose(Pointer s, Pointer pose);

		Pointer tpose_in(String str);

		Pointer tpose_make(Pointer tpoint, Pointer tradius);

		Pointer tpose_to_tpoint(Pointer temp);

		Pointer tpose_end_value(Pointer temp);

		Pointer tpose_points(Pointer temp);

		Pointer tpose_rotation(Pointer temp);

		Pointer tpose_start_value(Pointer temp);

		Pointer tpose_trajectory(Pointer temp);

		boolean tpose_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean tpose_value_n(Pointer temp, int n, Pointer result);

		Pointer tpose_values(Pointer temp, Pointer count);

		Pointer tpose_at_geom(Pointer temp, Pointer gs);

		Pointer tpose_at_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tpose_at_pose(Pointer temp, Pointer pose);

		Pointer tpose_minus_geom(Pointer temp, Pointer gs);

		Pointer tpose_minus_pose(Pointer temp, Pointer pose);

		Pointer tpose_minus_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tdistance_tpose_pose(Pointer temp, Pointer pose);

		Pointer tdistance_tpose_point(Pointer temp, Pointer gs);

		Pointer tdistance_tpose_tpose(Pointer temp1, Pointer temp2);

		double nad_tpose_geo(Pointer temp, Pointer gs);

		double nad_tpose_pose(Pointer temp, Pointer pose);

		double nad_tpose_stbox(Pointer temp, Pointer box);

		double nad_tpose_tpose(Pointer temp1, Pointer temp2);

		Pointer nai_tpose_geo(Pointer temp, Pointer gs);

		Pointer nai_tpose_pose(Pointer temp, Pointer pose);

		Pointer nai_tpose_tpose(Pointer temp1, Pointer temp2);

		Pointer shortestline_tpose_geo(Pointer temp, Pointer gs);

		Pointer shortestline_tpose_pose(Pointer temp, Pointer pose);

		Pointer shortestline_tpose_tpose(Pointer temp1, Pointer temp2);

		int always_eq_pose_tpose(Pointer pose, Pointer temp);

		int always_eq_tpose_pose(Pointer temp, Pointer pose);

		int always_eq_tpose_tpose(Pointer temp1, Pointer temp2);

		int always_ne_pose_tpose(Pointer pose, Pointer temp);

		int always_ne_tpose_pose(Pointer temp, Pointer pose);

		int always_ne_tpose_tpose(Pointer temp1, Pointer temp2);

		int ever_eq_pose_tpose(Pointer pose, Pointer temp);

		int ever_eq_tpose_pose(Pointer temp, Pointer pose);

		int ever_eq_tpose_tpose(Pointer temp1, Pointer temp2);

		int ever_ne_pose_tpose(Pointer pose, Pointer temp);

		int ever_ne_tpose_pose(Pointer temp, Pointer pose);

		int ever_ne_tpose_tpose(Pointer temp1, Pointer temp2);

		Pointer teq_pose_tpose(Pointer pose, Pointer temp);

		Pointer teq_tpose_pose(Pointer temp, Pointer pose);

		Pointer tne_pose_tpose(Pointer pose, Pointer temp);

		Pointer tne_tpose_pose(Pointer temp, Pointer pose);

		String trgeo_out(Pointer temp);

		Pointer trgeoinst_make(Pointer geom, Pointer pose, long t);

		Pointer geo_tpose_to_trgeo(Pointer gs, Pointer temp);

		Pointer trgeo_to_tpose(Pointer temp);

		Pointer trgeo_to_tpoint(Pointer temp);

		Pointer trgeo_end_instant(Pointer temp);

		Pointer trgeo_end_sequence(Pointer temp);

		Pointer trgeo_end_value(Pointer temp);

		Pointer trgeo_geom(Pointer temp);

		Pointer trgeo_instant_n(Pointer temp, int n);

		Pointer trgeo_instants(Pointer temp, Pointer count);

		Pointer trgeo_points(Pointer temp);

		Pointer trgeo_rotation(Pointer temp);

		Pointer trgeo_segments(Pointer temp, Pointer count);

		Pointer trgeo_sequence_n(Pointer temp, int i);

		Pointer trgeo_sequences(Pointer temp, Pointer count);

		Pointer trgeo_start_instant(Pointer temp);

		Pointer trgeo_start_sequence(Pointer temp);

		Pointer trgeo_start_value(Pointer temp);

		boolean trgeo_value_n(Pointer temp, int n, Pointer result);

		Pointer trgeo_traversed_area(Pointer temp, boolean unary_union);

		Pointer trgeo_append_tinstant(Pointer temp, Pointer inst, int interp, double maxdist, Pointer maxt, boolean expand);

		Pointer trgeo_append_tsequence(Pointer temp, Pointer seq, boolean expand);

		Pointer trgeo_delete_timestamptz(Pointer temp, long t, boolean connect);

		Pointer trgeo_delete_tstzset(Pointer temp, Pointer s, boolean connect);

		Pointer trgeo_delete_tstzspan(Pointer temp, Pointer s, boolean connect);

		Pointer trgeo_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect);

		Pointer trgeo_round(Pointer temp, int maxdd);

		Pointer trgeo_set_interp(Pointer temp, int interp);

		Pointer trgeo_to_tinstant(Pointer temp);

		Pointer trgeo_after_timestamptz(Pointer temp, long t, boolean strict);

		Pointer trgeo_before_timestamptz(Pointer temp, long t, boolean strict);

		Pointer trgeo_restrict_value(Pointer temp, Pointer value, boolean atfunc);

		Pointer trgeo_restrict_values(Pointer temp, Pointer s, boolean atfunc);

		Pointer trgeo_restrict_timestamptz(Pointer temp, long t, boolean atfunc);

		Pointer trgeo_restrict_tstzset(Pointer temp, Pointer s, boolean atfunc);

		Pointer trgeo_restrict_tstzspan(Pointer temp, Pointer s, boolean atfunc);

		Pointer trgeo_restrict_tstzspanset(Pointer temp, Pointer ss, boolean atfunc);

		Pointer tdistance_trgeo_geo(Pointer temp, Pointer gs);

		Pointer tdistance_trgeo_tpoint(Pointer temp1, Pointer temp2);

		Pointer tdistance_trgeo_trgeo(Pointer temp1, Pointer temp2);

		double nad_stbox_trgeo(Pointer box, Pointer temp);

		double nad_trgeo_geo(Pointer temp, Pointer gs);

		double nad_trgeo_stbox(Pointer temp, Pointer box);

		double nad_trgeo_tpoint(Pointer temp1, Pointer temp2);

		double nad_trgeo_trgeo(Pointer temp1, Pointer temp2);

		Pointer nai_trgeo_geo(Pointer temp, Pointer gs);

		Pointer nai_trgeo_tpoint(Pointer temp1, Pointer temp2);

		Pointer nai_trgeo_trgeo(Pointer temp1, Pointer temp2);

		Pointer shortestline_trgeo_geo(Pointer temp, Pointer gs);

		Pointer shortestline_trgeo_tpoint(Pointer temp1, Pointer temp2);

		Pointer shortestline_trgeo_trgeo(Pointer temp1, Pointer temp2);

		int always_eq_geo_trgeo(Pointer gs, Pointer temp);

		int always_eq_trgeo_geo(Pointer temp, Pointer gs);

		int always_eq_trgeo_trgeo(Pointer temp1, Pointer temp2);

		int always_ne_geo_trgeo(Pointer gs, Pointer temp);

		int always_ne_trgeo_geo(Pointer temp, Pointer gs);

		int always_ne_trgeo_trgeo(Pointer temp1, Pointer temp2);

		int ever_eq_geo_trgeo(Pointer gs, Pointer temp);

		int ever_eq_trgeo_geo(Pointer temp, Pointer gs);

		int ever_eq_trgeo_trgeo(Pointer temp1, Pointer temp2);

		int ever_ne_geo_trgeo(Pointer gs, Pointer temp);

		int ever_ne_trgeo_geo(Pointer temp, Pointer gs);

		int ever_ne_trgeo_trgeo(Pointer temp1, Pointer temp2);

		Pointer teq_geo_trgeo(Pointer gs, Pointer temp);

		Pointer teq_trgeo_geo(Pointer temp, Pointer gs);

		Pointer tne_geo_trgeo(Pointer gs, Pointer temp);

		Pointer tne_trgeo_geo(Pointer temp, Pointer gs);

		int geo_get_srid(Pointer g);

		int date_in(String str);

		String date_out(int d);

		int interval_cmp(Pointer interv1, Pointer interv2);

		Pointer interval_in(String str, int typmod);

		String interval_out(Pointer interv);

		Pointer time_in(String str, int typmod);

		String time_out(Pointer t);

		long timestamp_in(String str, int typmod);

		String timestamp_out(long t);

		long timestamptz_in(String str, int typmod);

		String timestamptz_out(long t);

	}

	private static final String _LIB = "libmeos.so";

	static final MeosLibraryPartA _meos_a =
			JarLibraryLoader.create(MeosLibraryPartA.class, _LIB).getLibraryInstance();
	static final MeosLibraryPartB _meos_b =
			JarLibraryLoader.create(MeosLibraryPartB.class, _LIB).getLibraryInstance();
	static final MeosLibraryPartC _meos_c =
			JarLibraryLoader.create(MeosLibraryPartC.class, _LIB).getLibraryInstance();
	static final MeosLibraryPartD _meos_d =
			JarLibraryLoader.create(MeosLibraryPartD.class, _LIB).getLibraryInstance();

	private static final java.util.Map<String, Object> _dispatch;
	static {
		_dispatch = new java.util.HashMap<>(4096);
		for (java.lang.reflect.Method _m : MeosLibraryPartA.class.getMethods())
			_dispatch.put(_m.getName(), _meos_a);
		for (java.lang.reflect.Method _m : MeosLibraryPartB.class.getMethods())
			_dispatch.put(_m.getName(), _meos_b);
		for (java.lang.reflect.Method _m : MeosLibraryPartC.class.getMethods())
			_dispatch.put(_m.getName(), _meos_c);
		for (java.lang.reflect.Method _m : MeosLibraryPartD.class.getMethods())
			_dispatch.put(_m.getName(), _meos_d);
	}



	@SuppressWarnings("unused")
	public static Pointer meos_array_create(int elem_size) {
		var _result = _meos_a.meos_array_create(elem_size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_array_add(Pointer array, Pointer value) {
		_meos_a.meos_array_add(array, value);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer meos_array_get(Pointer array, int n) {
		var _result = _meos_a.meos_array_get(array, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int meos_array_count(Pointer array) {
		var _result = _meos_a.meos_array_count(array);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_array_reset(Pointer array) {
		_meos_a.meos_array_reset(array);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_array_reset_free(Pointer array) {
		_meos_a.meos_array_reset_free(array);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_array_destroy(Pointer array) {
		_meos_a.meos_array_destroy(array);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_array_destroy_free(Pointer array) {
		_meos_a.meos_array_destroy_free(array);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_intspan() {
		var _result = _meos_a.rtree_create_intspan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_bigintspan() {
		var _result = _meos_a.rtree_create_bigintspan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_floatspan() {
		var _result = _meos_a.rtree_create_floatspan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_datespan() {
		var _result = _meos_a.rtree_create_datespan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_tstzspan() {
		var _result = _meos_a.rtree_create_tstzspan();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_tbox() {
		var _result = _meos_a.rtree_create_tbox();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer rtree_create_stbox() {
		var _result = _meos_a.rtree_create_stbox();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void rtree_free(Pointer rtree) {
		_meos_a.rtree_free(rtree);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void rtree_insert(Pointer rtree, Pointer box, int id) {
		_meos_a.rtree_insert(rtree, box, id);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void rtree_insert_temporal(Pointer rtree, Pointer temp, int id) {
		_meos_a.rtree_insert_temporal(rtree, temp, id);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static int rtree_search(Pointer rtree, int op, Pointer query, Pointer result) {
		var _result = _meos_a.rtree_search(rtree, op, query, result);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int rtree_search_temporal(Pointer rtree, int op, Pointer temp, Pointer result) {
		var _result = _meos_a.rtree_search_temporal(rtree, op, temp, result);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_error(int errlevel, int errcode, String format) {
		_meos_a.meos_error(errlevel, errcode, format);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static int meos_errno() {
		var _result = _meos_a.meos_errno();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int meos_errno_set(int err) {
		var _result = _meos_a.meos_errno_set(err);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int meos_errno_restore(int err) {
		var _result = _meos_a.meos_errno_restore(err);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int meos_errno_reset() {
		var _result = _meos_a.meos_errno_reset();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_initialize_timezone(String name) {
		_meos_a.meos_initialize_timezone(name);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_initialize_error_handler(error_handler_fn err_handler) {
		_meos_a.meos_initialize_error_handler(err_handler);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_finalize_timezone() {
		_meos_a.meos_finalize_timezone();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_finalize_projsrs() {
		_meos_a.meos_finalize_projsrs();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_finalize_ways() {
		_meos_a.meos_finalize_ways();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static boolean meos_set_datestyle(String newval, Pointer extra) {
		var _result = _meos_a.meos_set_datestyle(newval, extra);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean meos_set_intervalstyle(String newval, int extra) {
		var _result = _meos_a.meos_set_intervalstyle(newval, extra);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String meos_get_datestyle() {
		var _result = _meos_a.meos_get_datestyle();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String meos_get_intervalstyle() {
		var _result = _meos_a.meos_get_intervalstyle();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_set_spatial_ref_sys_csv(String path) {
		_meos_a.meos_set_spatial_ref_sys_csv(path);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_initialize() {
		_meos_a.meos_initialize();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void meos_finalize() {
		_meos_a.meos_finalize();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static int add_date_int(int d, int days) {
		var _result = _meos_a.add_date_int(d, days);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_interval_interval(Pointer interv1, Pointer interv2) {
		var _result = _meos_a.add_interval_interval(interv1, interv2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime add_timestamptz_interval(OffsetDateTime t, Pointer interv) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.add_timestamptz_interval(t_new, interv);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static boolean bool_in(String str) {
		var _result = _meos_a.bool_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String bool_out(boolean b) {
		var _result = _meos_a.bool_out(b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cstring2text(String str) {
		var _result = _meos_a.cstring2text(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static LocalDateTime date_to_timestamp(int dateVal) {
		var _result = _meos_a.date_to_timestamp(dateVal);
		MeosErrorHandler.checkError();
		return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(_result), java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime date_to_timestamptz(int d) {
		var _result = _meos_a.date_to_timestamptz(d);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static double float_exp(double d) {
		var _result = _meos_a.float_exp(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_ln(double d) {
		var _result = _meos_a.float_ln(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_log10(double d) {
		var _result = _meos_a.float_log10(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String float8_out(double d, int maxdd) {
		var _result = _meos_a.float8_out(d, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_round(double d, int maxdd) {
		var _result = _meos_a.float_round(d, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int int32_cmp(int l, int r) {
		var _result = _meos_a.int32_cmp(l, r);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int int64_cmp(long l, long r) {
		var _result = _meos_a.int64_cmp(l, r);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs) {
		var _result = _meos_a.interval_make(years, months, weeks, days, hours, mins, secs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int minus_date_date(int d1, int d2) {
		var _result = _meos_a.minus_date_date(d1, d2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int minus_date_int(int d, int days) {
		var _result = _meos_a.minus_date_int(d, days);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime minus_timestamptz_interval(OffsetDateTime t, Pointer interv) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.minus_timestamptz_interval(t_new, interv);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_timestamptz(OffsetDateTime t1, OffsetDateTime t2) {
		var t1_new = t1.toEpochSecond();
		var t2_new = t2.toEpochSecond();
		var _result = _meos_a.minus_timestamptz_timestamptz(t1_new, t2_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_interval_double(Pointer interv, double factor) {
		var _result = _meos_a.mul_interval_double(interv, factor);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int pg_date_in(String str) {
		var _result = _meos_a.pg_date_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String pg_date_out(int d) {
		var _result = _meos_a.pg_date_out(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int pg_interval_cmp(Pointer interv1, Pointer interv2) {
		var _result = _meos_a.pg_interval_cmp(interv1, interv2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pg_interval_in(String str, int typmod) {
		var _result = _meos_a.pg_interval_in(str, typmod);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String pg_interval_out(Pointer interv) {
		var _result = _meos_a.pg_interval_out(interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static LocalDateTime pg_timestamp_in(String str, int typmod) {
		var _result = _meos_a.pg_timestamp_in(str, typmod);
		MeosErrorHandler.checkError();
		return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(_result), java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static String pg_timestamp_out(LocalDateTime t) {
		var t_new = t.toInstant(java.time.ZoneOffset.UTC).getEpochSecond();
		var _result = _meos_a.pg_timestamp_out(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime pg_timestamptz_in(String str, int typmod) {
		var _result = _meos_a.pg_timestamptz_in(str, typmod);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static String pg_timestamptz_out(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.pg_timestamptz_out(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String text2cstring(Pointer txt) {
		var _result = _meos_a.text2cstring(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int text_cmp(Pointer txt1, Pointer txt2) {
		var _result = _meos_a.text_cmp(txt1, txt2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_copy(Pointer txt) {
		var _result = _meos_a.text_copy(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_in(String str) {
		var _result = _meos_a.text_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_initcap(Pointer txt) {
		var _result = _meos_a.text_initcap(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_lower(Pointer txt) {
		var _result = _meos_a.text_lower(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String text_out(Pointer txt) {
		var _result = _meos_a.text_out(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_upper(Pointer txt) {
		var _result = _meos_a.text_upper(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_text_text(Pointer txt1, Pointer txt2) {
		var _result = _meos_a.textcat_text_text(txt1, txt2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_shift(OffsetDateTime t, Pointer interv) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.timestamptz_shift(t_new, interv);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static int timestamp_to_date(LocalDateTime t) {
		var t_new = t.toInstant(java.time.ZoneOffset.UTC).getEpochSecond();
		var _result = _meos_a.timestamp_to_date(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int timestamptz_to_date(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.timestamptz_to_date(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_in(String str) {
		var _result = _meos_a.bigintset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String bigintset_out(Pointer set) {
		var _result = _meos_a.bigintset_out(set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_expand(Pointer s, long value) {
		var _result = _meos_a.bigintspan_expand(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_in(String str) {
		var _result = _meos_a.bigintspan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String bigintspan_out(Pointer s) {
		var _result = _meos_a.bigintspan_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspanset_in(String str) {
		var _result = _meos_a.bigintspanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String bigintspanset_out(Pointer ss) {
		var _result = _meos_a.bigintspanset_out(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_in(String str) {
		var _result = _meos_a.dateset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String dateset_out(Pointer s) {
		var _result = _meos_a.dateset_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_in(String str) {
		var _result = _meos_a.datespan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String datespan_out(Pointer s) {
		var _result = _meos_a.datespan_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_in(String str) {
		var _result = _meos_a.datespanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String datespanset_out(Pointer ss) {
		var _result = _meos_a.datespanset_out(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_in(String str) {
		var _result = _meos_a.floatset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String floatset_out(Pointer set, int maxdd) {
		var _result = _meos_a.floatset_out(set, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_expand(Pointer s, double value) {
		var _result = _meos_a.floatspan_expand(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_in(String str) {
		var _result = _meos_a.floatspan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String floatspan_out(Pointer s, int maxdd) {
		var _result = _meos_a.floatspan_out(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_in(String str) {
		var _result = _meos_a.floatspanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String floatspanset_out(Pointer ss, int maxdd) {
		var _result = _meos_a.floatspanset_out(ss, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_in(String str) {
		var _result = _meos_a.intset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String intset_out(Pointer set) {
		var _result = _meos_a.intset_out(set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_expand(Pointer s, int value) {
		var _result = _meos_a.intspan_expand(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_in(String str) {
		var _result = _meos_a.intspan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String intspan_out(Pointer s) {
		var _result = _meos_a.intspan_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspanset_in(String str) {
		var _result = _meos_a.intspanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String intspanset_out(Pointer ss) {
		var _result = _meos_a.intspanset_out(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String set_as_hexwkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_a.set_as_hexwkb(s, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_as_wkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_a.set_as_wkb(s, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_from_hexwkb(String hexwkb) {
		var _result = _meos_a.set_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_from_wkb(Pointer wkb, long size) {
		var _result = _meos_a.set_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String span_as_hexwkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_a.span_as_hexwkb(s, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_as_wkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_a.span_as_wkb(s, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_from_hexwkb(String hexwkb) {
		var _result = _meos_a.span_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_from_wkb(Pointer wkb, long size) {
		var _result = _meos_a.span_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String spanset_as_hexwkb(Pointer ss, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_a.spanset_as_hexwkb(ss, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_as_wkb(Pointer ss, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_a.spanset_as_wkb(ss, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_from_hexwkb(String hexwkb) {
		var _result = _meos_a.spanset_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_from_wkb(Pointer wkb, long size) {
		var _result = _meos_a.spanset_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_in(String str) {
		var _result = _meos_a.textset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String textset_out(Pointer set) {
		var _result = _meos_a.textset_out(set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_in(String str) {
		var _result = _meos_a.tstzset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tstzset_out(Pointer set) {
		var _result = _meos_a.tstzset_out(set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_in(String str) {
		var _result = _meos_a.tstzspan_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tstzspan_out(Pointer s) {
		var _result = _meos_a.tstzspan_out(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_in(String str) {
		var _result = _meos_a.tstzspanset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tstzspanset_out(Pointer ss) {
		var _result = _meos_a.tstzspanset_out(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_make(Pointer values, int count) {
		var _result = _meos_a.bigintset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc) {
		var _result = _meos_a.bigintspan_make(lower, upper, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_make(Pointer values, int count) {
		var _result = _meos_a.dateset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) {
		var _result = _meos_a.datespan_make(lower, upper, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_make(Pointer values, int count) {
		var _result = _meos_a.floatset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc) {
		var _result = _meos_a.floatspan_make(lower, upper, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_make(Pointer values, int count) {
		var _result = _meos_a.intset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) {
		var _result = _meos_a.intspan_make(lower, upper, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_copy(Pointer s) {
		var _result = _meos_a.set_copy(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_copy(Pointer s) {
		var _result = _meos_a.span_copy(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_copy(Pointer ss) {
		var _result = _meos_a.spanset_copy(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_make(Pointer spans, int count) {
		var _result = _meos_a.spanset_make(spans, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_make(Pointer values, int count) {
		var _result = _meos_a.textset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_make(Pointer values, int count) {
		var _result = _meos_a.tstzset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_make(OffsetDateTime lower, OffsetDateTime upper, boolean lower_inc, boolean upper_inc) {
		var lower_new = lower.toEpochSecond();
		var upper_new = upper.toEpochSecond();
		var _result = _meos_a.tstzspan_make(lower_new, upper_new, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_to_set(long i) {
		var _result = _meos_a.bigint_to_set(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_to_span(long i) {
		var _result = _meos_a.bigint_to_span(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_to_spanset(long i) {
		var _result = _meos_a.bigint_to_spanset(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_to_intspan(Pointer s) {
		var _result = _meos_a.bigintspan_to_intspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_to_floatspan(Pointer s) {
		var _result = _meos_a.bigintspan_to_floatspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_to_set(int d) {
		var _result = _meos_a.date_to_set(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_to_span(int d) {
		var _result = _meos_a.date_to_span(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_to_spanset(int d) {
		var _result = _meos_a.date_to_spanset(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_to_tstzset(Pointer s) {
		var _result = _meos_a.dateset_to_tstzset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_to_tstzspan(Pointer s) {
		var _result = _meos_a.datespan_to_tstzspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_to_tstzspanset(Pointer ss) {
		var _result = _meos_a.datespanset_to_tstzspanset(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_to_set(double d) {
		var _result = _meos_a.float_to_set(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_to_span(double d) {
		var _result = _meos_a.float_to_span(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_to_spanset(double d) {
		var _result = _meos_a.float_to_spanset(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_to_intset(Pointer s) {
		var _result = _meos_a.floatset_to_intset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_to_bigintspan(Pointer s) {
		var _result = _meos_a.floatspan_to_bigintspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_to_intspan(Pointer s) {
		var _result = _meos_a.floatspan_to_intspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_to_intspanset(Pointer ss) {
		var _result = _meos_a.floatspanset_to_intspanset(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_to_set(int i) {
		var _result = _meos_a.int_to_set(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_to_span(int i) {
		var _result = _meos_a.int_to_span(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_to_spanset(int i) {
		var _result = _meos_a.int_to_spanset(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_to_floatset(Pointer s) {
		var _result = _meos_a.intset_to_floatset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_to_bigintspan(Pointer s) {
		var _result = _meos_a.intspan_to_bigintspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_to_floatspan(Pointer s) {
		var _result = _meos_a.intspan_to_floatspan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspanset_to_floatspanset(Pointer ss) {
		var _result = _meos_a.intspanset_to_floatspanset(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_to_span(Pointer s) {
		var _result = _meos_a.set_to_span(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_to_spanset(Pointer s) {
		var _result = _meos_a.set_to_spanset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_to_spanset(Pointer s) {
		var _result = _meos_a.span_to_spanset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_to_set(Pointer txt) {
		var _result = _meos_a.text_to_set(txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_set(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.timestamptz_to_set(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_span(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.timestamptz_to_span(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_spanset(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.timestamptz_to_spanset(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_to_dateset(Pointer s) {
		var _result = _meos_a.tstzset_to_dateset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_to_datespan(Pointer s) {
		var _result = _meos_a.tstzspan_to_datespan(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_to_datespanset(Pointer ss) {
		var _result = _meos_a.tstzspanset_to_datespanset(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintset_end_value(Pointer s) {
		var _result = _meos_a.bigintset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintset_start_value(Pointer s) {
		var _result = _meos_a.bigintset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.bigintset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_values(Pointer s) {
		var _result = _meos_a.bigintset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspan_lower(Pointer s) {
		var _result = _meos_a.bigintspan_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspan_upper(Pointer s) {
		var _result = _meos_a.bigintspan_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspan_width(Pointer s) {
		var _result = _meos_a.bigintspan_width(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspanset_lower(Pointer ss) {
		var _result = _meos_a.bigintspanset_lower(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspanset_upper(Pointer ss) {
		var _result = _meos_a.bigintspanset_upper(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigintspanset_width(Pointer ss, boolean boundspan) {
		var _result = _meos_a.bigintspanset_width(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int dateset_end_value(Pointer s) {
		var _result = _meos_a.dateset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int dateset_start_value(Pointer s) {
		var _result = _meos_a.dateset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = _meos_a.dateset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_values(Pointer s) {
		var _result = _meos_a.dateset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_duration(Pointer s) {
		var _result = _meos_a.datespan_duration(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespan_lower(Pointer s) {
		var _result = _meos_a.datespan_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespan_upper(Pointer s) {
		var _result = _meos_a.datespan_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_date_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = _meos_a.datespanset_date_n(ss, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_dates(Pointer ss) {
		var _result = _meos_a.datespanset_dates(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_duration(Pointer ss, boolean boundspan) {
		var _result = _meos_a.datespanset_duration(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespanset_end_date(Pointer ss) {
		var _result = _meos_a.datespanset_end_date(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespanset_num_dates(Pointer ss) {
		var _result = _meos_a.datespanset_num_dates(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datespanset_start_date(Pointer ss) {
		var _result = _meos_a.datespanset_start_date(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatset_end_value(Pointer s) {
		var _result = _meos_a.floatset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatset_start_value(Pointer s) {
		var _result = _meos_a.floatset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_a.floatset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_values(Pointer s) {
		var _result = _meos_a.floatset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspan_lower(Pointer s) {
		var _result = _meos_a.floatspan_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspan_upper(Pointer s) {
		var _result = _meos_a.floatspan_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspan_width(Pointer s) {
		var _result = _meos_a.floatspan_width(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspanset_lower(Pointer ss) {
		var _result = _meos_a.floatspanset_lower(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspanset_upper(Pointer ss) {
		var _result = _meos_a.floatspanset_upper(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double floatspanset_width(Pointer ss, boolean boundspan) {
		var _result = _meos_a.floatspanset_width(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intset_end_value(Pointer s) {
		var _result = _meos_a.intset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intset_start_value(Pointer s) {
		var _result = _meos_a.intset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = _meos_a.intset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_values(Pointer s) {
		var _result = _meos_a.intset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspan_lower(Pointer s) {
		var _result = _meos_a.intspan_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspan_upper(Pointer s) {
		var _result = _meos_a.intspan_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspan_width(Pointer s) {
		var _result = _meos_a.intspan_width(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspanset_lower(Pointer ss) {
		var _result = _meos_a.intspanset_lower(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspanset_upper(Pointer ss) {
		var _result = _meos_a.intspanset_upper(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intspanset_width(Pointer ss, boolean boundspan) {
		var _result = _meos_a.intspanset_width(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int set_hash(Pointer s) {
		var _result = _meos_a.set_hash(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long set_hash_extended(Pointer s, long seed) {
		var _result = _meos_a.set_hash_extended(s, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int set_num_values(Pointer s) {
		var _result = _meos_a.set_num_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int span_hash(Pointer s) {
		var _result = _meos_a.span_hash(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long span_hash_extended(Pointer s, long seed) {
		var _result = _meos_a.span_hash_extended(s, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_lower_inc(Pointer s) {
		var _result = _meos_a.span_lower_inc(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_upper_inc(Pointer s) {
		var _result = _meos_a.span_upper_inc(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_end_span(Pointer ss) {
		var _result = _meos_a.spanset_end_span(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_hash(Pointer ss) {
		var _result = _meos_a.spanset_hash(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long spanset_hash_extended(Pointer ss, long seed) {
		var _result = _meos_a.spanset_hash_extended(ss, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_lower_inc(Pointer ss) {
		var _result = _meos_a.spanset_lower_inc(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_num_spans(Pointer ss) {
		var _result = _meos_a.spanset_num_spans(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_span(Pointer ss) {
		var _result = _meos_a.spanset_span(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_span_n(Pointer ss, int i) {
		var _result = _meos_a.spanset_span_n(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_spanarr(Pointer ss) {
		var _result = _meos_a.spanset_spanarr(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_start_span(Pointer ss) {
		var _result = _meos_a.spanset_start_span(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_upper_inc(Pointer ss) {
		var _result = _meos_a.spanset_upper_inc(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_end_value(Pointer s) {
		var _result = _meos_a.textset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_start_value(Pointer s) {
		var _result = _meos_a.textset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.textset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_values(Pointer s) {
		var _result = _meos_a.textset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzset_end_value(Pointer s) {
		var _result = _meos_a.tstzset_end_value(s);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzset_start_value(Pointer s) {
		var _result = _meos_a.tstzset_start_value(s);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.tstzset_value_n(s, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_values(Pointer s) {
		var _result = _meos_a.tstzset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_duration(Pointer s) {
		var _result = _meos_a.tstzspan_duration(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspan_lower(Pointer s) {
		var _result = _meos_a.tstzspan_lower(s);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspan_upper(Pointer s) {
		var _result = _meos_a.tstzspan_upper(s);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_duration(Pointer ss, boolean boundspan) {
		var _result = _meos_a.tstzspanset_duration(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_end_timestamptz(Pointer ss) {
		var _result = _meos_a.tstzspanset_end_timestamptz(ss);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_lower(Pointer ss) {
		var _result = _meos_a.tstzspanset_lower(ss);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static int tstzspanset_num_timestamps(Pointer ss) {
		var _result = _meos_a.tstzspanset_num_timestamps(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_start_timestamptz(Pointer ss) {
		var _result = _meos_a.tstzspanset_start_timestamptz(ss);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_timestamps(Pointer ss) {
		var _result = _meos_a.tstzspanset_timestamps(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_timestamptz_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_a.tstzspanset_timestamptz_n(ss, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_upper(Pointer ss) {
		var _result = _meos_a.tstzspanset_upper(ss);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.bigintset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.bigintspan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.bigintspanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer dateset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.dateset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.datespan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.datespanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_ceil(Pointer s) {
		var _result = _meos_a.floatset_ceil(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_degrees(Pointer s, boolean normalize) {
		var _result = _meos_a.floatset_degrees(s, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_floor(Pointer s) {
		var _result = _meos_a.floatset_floor(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_radians(Pointer s) {
		var _result = _meos_a.floatset_radians(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.floatset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_ceil(Pointer s) {
		var _result = _meos_a.floatspan_ceil(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_degrees(Pointer s, boolean normalize) {
		var _result = _meos_a.floatspan_degrees(s, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_floor(Pointer s) {
		var _result = _meos_a.floatspan_floor(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_radians(Pointer s) {
		var _result = _meos_a.floatspan_radians(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_round(Pointer s, int maxdd) {
		var _result = _meos_a.floatspan_round(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.floatspan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_ceil(Pointer ss) {
		var _result = _meos_a.floatspanset_ceil(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_floor(Pointer ss) {
		var _result = _meos_a.floatspanset_floor(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_degrees(Pointer ss, boolean normalize) {
		var _result = _meos_a.floatspanset_degrees(ss, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_radians(Pointer ss) {
		var _result = _meos_a.floatspanset_radians(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_round(Pointer ss, int maxdd) {
		var _result = _meos_a.floatspanset_round(ss, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.floatspanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.intset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.intspan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = _meos_a.intspanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_expand(Pointer s, Pointer interv) {
		var _result = _meos_a.tstzspan_expand(s, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_round(Pointer s, int maxdd) {
		var _result = _meos_a.set_round(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_text_textset(Pointer txt, Pointer s) {
		var _result = _meos_a.textcat_text_textset(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_textset_text(Pointer s, Pointer txt) {
		var _result = _meos_a.textcat_textset_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_initcap(Pointer s) {
		var _result = _meos_a.textset_initcap(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_lower(Pointer s) {
		var _result = _meos_a.textset_lower(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textset_upper(Pointer s) {
		var _result = _meos_a.textset_upper(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_tprecision(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_a.timestamptz_tprecision(t_new, duration, torigin_new);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_shift_scale(Pointer s, Pointer shift, Pointer duration) {
		var _result = _meos_a.tstzset_shift_scale(s, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_a.tstzset_tprecision(s, duration, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_shift_scale(Pointer s, Pointer shift, Pointer duration) {
		var _result = _meos_a.tstzspan_shift_scale(s, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_a.tstzspan_tprecision(s, duration, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_shift_scale(Pointer ss, Pointer shift, Pointer duration) {
		var _result = _meos_a.tstzspanset_shift_scale(ss, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_tprecision(Pointer ss, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_a.tstzspanset_tprecision(ss, duration, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int set_cmp(Pointer s1, Pointer s2) {
		var _result = _meos_a.set_cmp(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_eq(Pointer s1, Pointer s2) {
		var _result = _meos_a.set_eq(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_ge(Pointer s1, Pointer s2) {
		var _result = _meos_a.set_ge(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_gt(Pointer s1, Pointer s2) {
		var _result = _meos_a.set_gt(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_le(Pointer s1, Pointer s2) {
		var _result = _meos_a.set_le(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_lt(Pointer s1, Pointer s2) {
		var _result = _meos_a.set_lt(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean set_ne(Pointer s1, Pointer s2) {
		var _result = _meos_a.set_ne(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int span_cmp(Pointer s1, Pointer s2) {
		var _result = _meos_a.span_cmp(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_eq(Pointer s1, Pointer s2) {
		var _result = _meos_a.span_eq(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_ge(Pointer s1, Pointer s2) {
		var _result = _meos_a.span_ge(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_gt(Pointer s1, Pointer s2) {
		var _result = _meos_a.span_gt(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_le(Pointer s1, Pointer s2) {
		var _result = _meos_a.span_le(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_lt(Pointer s1, Pointer s2) {
		var _result = _meos_a.span_lt(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean span_ne(Pointer s1, Pointer s2) {
		var _result = _meos_a.span_ne(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_cmp(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.spanset_cmp(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_eq(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.spanset_eq(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_ge(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.spanset_ge(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_gt(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.spanset_gt(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_le(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.spanset_le(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_lt(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.spanset_lt(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spanset_ne(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.spanset_ne(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_spans(Pointer s) {
		var _result = _meos_a.set_spans(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_split_each_n_spans(Pointer s, int elems_per_span, Pointer count) {
		var _result = _meos_a.set_split_each_n_spans(s, elems_per_span, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_split_n_spans(Pointer s, int span_count, Pointer count) {
		var _result = _meos_a.set_split_n_spans(s, span_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_spans(Pointer ss) {
		var _result = _meos_a.spanset_spans(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_split_each_n_spans(Pointer ss, int elems_per_span, Pointer count) {
		var _result = _meos_a.spanset_split_each_n_spans(ss, elems_per_span, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_split_n_spans(Pointer ss, int span_count, Pointer count) {
		var _result = _meos_a.spanset_split_n_spans(ss, span_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_bigint(Pointer s, long i) {
		var _result = _meos_a.adjacent_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_date(Pointer s, int d) {
		var _result = _meos_a.adjacent_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_float(Pointer s, double d) {
		var _result = _meos_a.adjacent_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_int(Pointer s, int i) {
		var _result = _meos_a.adjacent_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.adjacent_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.adjacent_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.adjacent_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.adjacent_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.adjacent_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.adjacent_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.adjacent_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.adjacent_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.adjacent_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.adjacent_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_bigint_set(long i, Pointer s) {
		var _result = _meos_a.contained_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_bigint_span(long i, Pointer s) {
		var _result = _meos_a.contained_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_bigint_spanset(long i, Pointer ss) {
		var _result = _meos_a.contained_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_date_set(int d, Pointer s) {
		var _result = _meos_a.contained_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_date_span(int d, Pointer s) {
		var _result = _meos_a.contained_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_date_spanset(int d, Pointer ss) {
		var _result = _meos_a.contained_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_float_set(double d, Pointer s) {
		var _result = _meos_a.contained_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_float_span(double d, Pointer s) {
		var _result = _meos_a.contained_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_float_spanset(double d, Pointer ss) {
		var _result = _meos_a.contained_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_int_set(int i, Pointer s) {
		var _result = _meos_a.contained_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_int_span(int i, Pointer s) {
		var _result = _meos_a.contained_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_int_spanset(int i, Pointer ss) {
		var _result = _meos_a.contained_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.contained_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.contained_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.contained_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.contained_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.contained_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_text_set(Pointer txt, Pointer s) {
		var _result = _meos_a.contained_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.contained_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.contained_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.contained_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_bigint(Pointer s, long i) {
		var _result = _meos_a.contains_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_date(Pointer s, int d) {
		var _result = _meos_a.contains_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_float(Pointer s, double d) {
		var _result = _meos_a.contains_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_int(Pointer s, int i) {
		var _result = _meos_a.contains_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.contains_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_text(Pointer s, Pointer t) {
		var _result = _meos_a.contains_set_text(s, t);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.contains_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_bigint(Pointer s, long i) {
		var _result = _meos_a.contains_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_date(Pointer s, int d) {
		var _result = _meos_a.contains_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_float(Pointer s, double d) {
		var _result = _meos_a.contains_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_int(Pointer s, int i) {
		var _result = _meos_a.contains_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.contains_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.contains_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.contains_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.contains_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.contains_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.contains_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.contains_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.contains_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.contains_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.contains_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.overlaps_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.overlaps_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.overlaps_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.overlaps_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.overlaps_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_date_set(int d, Pointer s) {
		var _result = _meos_a.after_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_date_span(int d, Pointer s) {
		var _result = _meos_a.after_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_date_spanset(int d, Pointer ss) {
		var _result = _meos_a.after_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_set_date(Pointer s, int d) {
		var _result = _meos_a.after_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.after_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_span_date(Pointer s, int d) {
		var _result = _meos_a.after_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.after_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.after_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.after_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.after_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.after_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.after_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_date_set(int d, Pointer s) {
		var _result = _meos_a.before_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_date_span(int d, Pointer s) {
		var _result = _meos_a.before_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_date_spanset(int d, Pointer ss) {
		var _result = _meos_a.before_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_set_date(Pointer s, int d) {
		var _result = _meos_a.before_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.before_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_span_date(Pointer s, int d) {
		var _result = _meos_a.before_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.before_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.before_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.before_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.before_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.before_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.before_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_bigint_set(long i, Pointer s) {
		var _result = _meos_a.left_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_bigint_span(long i, Pointer s) {
		var _result = _meos_a.left_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_bigint_spanset(long i, Pointer ss) {
		var _result = _meos_a.left_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_float_set(double d, Pointer s) {
		var _result = _meos_a.left_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_float_span(double d, Pointer s) {
		var _result = _meos_a.left_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_float_spanset(double d, Pointer ss) {
		var _result = _meos_a.left_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_int_set(int i, Pointer s) {
		var _result = _meos_a.left_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_int_span(int i, Pointer s) {
		var _result = _meos_a.left_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_int_spanset(int i, Pointer ss) {
		var _result = _meos_a.left_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_bigint(Pointer s, long i) {
		var _result = _meos_a.left_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_float(Pointer s, double d) {
		var _result = _meos_a.left_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_int(Pointer s, int i) {
		var _result = _meos_a.left_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.left_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_text(Pointer s, Pointer txt) {
		var _result = _meos_a.left_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_bigint(Pointer s, long i) {
		var _result = _meos_a.left_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_float(Pointer s, double d) {
		var _result = _meos_a.left_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_int(Pointer s, int i) {
		var _result = _meos_a.left_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.left_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.left_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.left_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.left_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.left_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.left_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.left_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_text_set(Pointer txt, Pointer s) {
		var _result = _meos_a.left_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_date_set(int d, Pointer s) {
		var _result = _meos_a.overafter_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_date_span(int d, Pointer s) {
		var _result = _meos_a.overafter_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_date_spanset(int d, Pointer ss) {
		var _result = _meos_a.overafter_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_set_date(Pointer s, int d) {
		var _result = _meos_a.overafter_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overafter_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_span_date(Pointer s, int d) {
		var _result = _meos_a.overafter_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overafter_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.overafter_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overafter_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overafter_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overafter_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overafter_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_date_set(int d, Pointer s) {
		var _result = _meos_a.overbefore_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_date_span(int d, Pointer s) {
		var _result = _meos_a.overbefore_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_date_spanset(int d, Pointer ss) {
		var _result = _meos_a.overbefore_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_set_date(Pointer s, int d) {
		var _result = _meos_a.overbefore_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overbefore_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_span_date(Pointer s, int d) {
		var _result = _meos_a.overbefore_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overbefore_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.overbefore_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overbefore_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overbefore_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overbefore_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.overbefore_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_bigint_set(long i, Pointer s) {
		var _result = _meos_a.overleft_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_bigint_span(long i, Pointer s) {
		var _result = _meos_a.overleft_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_bigint_spanset(long i, Pointer ss) {
		var _result = _meos_a.overleft_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_float_set(double d, Pointer s) {
		var _result = _meos_a.overleft_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_float_span(double d, Pointer s) {
		var _result = _meos_a.overleft_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_float_spanset(double d, Pointer ss) {
		var _result = _meos_a.overleft_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_int_set(int i, Pointer s) {
		var _result = _meos_a.overleft_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_int_span(int i, Pointer s) {
		var _result = _meos_a.overleft_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_int_spanset(int i, Pointer ss) {
		var _result = _meos_a.overleft_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_bigint(Pointer s, long i) {
		var _result = _meos_a.overleft_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_float(Pointer s, double d) {
		var _result = _meos_a.overleft_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_int(Pointer s, int i) {
		var _result = _meos_a.overleft_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.overleft_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_text(Pointer s, Pointer txt) {
		var _result = _meos_a.overleft_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_bigint(Pointer s, long i) {
		var _result = _meos_a.overleft_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_float(Pointer s, double d) {
		var _result = _meos_a.overleft_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_int(Pointer s, int i) {
		var _result = _meos_a.overleft_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.overleft_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.overleft_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.overleft_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.overleft_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.overleft_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.overleft_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.overleft_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_text_set(Pointer txt, Pointer s) {
		var _result = _meos_a.overleft_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_bigint_set(long i, Pointer s) {
		var _result = _meos_a.overright_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_bigint_span(long i, Pointer s) {
		var _result = _meos_a.overright_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_bigint_spanset(long i, Pointer ss) {
		var _result = _meos_a.overright_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_float_set(double d, Pointer s) {
		var _result = _meos_a.overright_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_float_span(double d, Pointer s) {
		var _result = _meos_a.overright_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_float_spanset(double d, Pointer ss) {
		var _result = _meos_a.overright_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_int_set(int i, Pointer s) {
		var _result = _meos_a.overright_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_int_span(int i, Pointer s) {
		var _result = _meos_a.overright_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_int_spanset(int i, Pointer ss) {
		var _result = _meos_a.overright_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_bigint(Pointer s, long i) {
		var _result = _meos_a.overright_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_float(Pointer s, double d) {
		var _result = _meos_a.overright_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_int(Pointer s, int i) {
		var _result = _meos_a.overright_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.overright_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_text(Pointer s, Pointer txt) {
		var _result = _meos_a.overright_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_bigint(Pointer s, long i) {
		var _result = _meos_a.overright_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_float(Pointer s, double d) {
		var _result = _meos_a.overright_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_int(Pointer s, int i) {
		var _result = _meos_a.overright_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.overright_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.overright_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.overright_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.overright_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.overright_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.overright_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.overright_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_text_set(Pointer txt, Pointer s) {
		var _result = _meos_a.overright_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_bigint_set(long i, Pointer s) {
		var _result = _meos_a.right_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_bigint_span(long i, Pointer s) {
		var _result = _meos_a.right_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_bigint_spanset(long i, Pointer ss) {
		var _result = _meos_a.right_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_float_set(double d, Pointer s) {
		var _result = _meos_a.right_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_float_span(double d, Pointer s) {
		var _result = _meos_a.right_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_float_spanset(double d, Pointer ss) {
		var _result = _meos_a.right_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_int_set(int i, Pointer s) {
		var _result = _meos_a.right_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_int_span(int i, Pointer s) {
		var _result = _meos_a.right_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_int_spanset(int i, Pointer ss) {
		var _result = _meos_a.right_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_bigint(Pointer s, long i) {
		var _result = _meos_a.right_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_float(Pointer s, double d) {
		var _result = _meos_a.right_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_int(Pointer s, int i) {
		var _result = _meos_a.right_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.right_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_text(Pointer s, Pointer txt) {
		var _result = _meos_a.right_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_bigint(Pointer s, long i) {
		var _result = _meos_a.right_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_float(Pointer s, double d) {
		var _result = _meos_a.right_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_int(Pointer s, int i) {
		var _result = _meos_a.right_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.right_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.right_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.right_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.right_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.right_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.right_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.right_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_text_set(Pointer txt, Pointer s) {
		var _result = _meos_a.right_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_bigint_set(long i, Pointer s) {
		var _result = _meos_a.intersection_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_date_set(int d, Pointer s) {
		var _result = _meos_a.intersection_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_float_set(double d, Pointer s) {
		var _result = _meos_a.intersection_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_int_set(int i, Pointer s) {
		var _result = _meos_a.intersection_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_bigint(Pointer s, long i) {
		var _result = _meos_a.intersection_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_date(Pointer s, int d) {
		var _result = _meos_a.intersection_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_float(Pointer s, double d) {
		var _result = _meos_a.intersection_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_int(Pointer s, int i) {
		var _result = _meos_a.intersection_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.intersection_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_text(Pointer s, Pointer txt) {
		var _result = _meos_a.intersection_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.intersection_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_bigint(Pointer s, long i) {
		var _result = _meos_a.intersection_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_date(Pointer s, int d) {
		var _result = _meos_a.intersection_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_float(Pointer s, double d) {
		var _result = _meos_a.intersection_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_int(Pointer s, int i) {
		var _result = _meos_a.intersection_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.intersection_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.intersection_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.intersection_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.intersection_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.intersection_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.intersection_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.intersection_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.intersection_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.intersection_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.intersection_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_text_set(Pointer txt, Pointer s) {
		var _result = _meos_a.intersection_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.intersection_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_bigint_set(long i, Pointer s) {
		var _result = _meos_a.minus_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_bigint_span(long i, Pointer s) {
		var _result = _meos_a.minus_bigint_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_bigint_spanset(long i, Pointer ss) {
		var _result = _meos_a.minus_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_date_set(int d, Pointer s) {
		var _result = _meos_a.minus_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_date_span(int d, Pointer s) {
		var _result = _meos_a.minus_date_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_date_spanset(int d, Pointer ss) {
		var _result = _meos_a.minus_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_float_set(double d, Pointer s) {
		var _result = _meos_a.minus_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_float_span(double d, Pointer s) {
		var _result = _meos_a.minus_float_span(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_float_spanset(double d, Pointer ss) {
		var _result = _meos_a.minus_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_int_set(int i, Pointer s) {
		var _result = _meos_a.minus_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_int_span(int i, Pointer s) {
		var _result = _meos_a.minus_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_int_spanset(int i, Pointer ss) {
		var _result = _meos_a.minus_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_bigint(Pointer s, long i) {
		var _result = _meos_a.minus_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_date(Pointer s, int d) {
		var _result = _meos_a.minus_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_float(Pointer s, double d) {
		var _result = _meos_a.minus_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_int(Pointer s, int i) {
		var _result = _meos_a.minus_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.minus_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_text(Pointer s, Pointer txt) {
		var _result = _meos_a.minus_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.minus_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_bigint(Pointer s, long i) {
		var _result = _meos_a.minus_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_date(Pointer s, int d) {
		var _result = _meos_a.minus_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_float(Pointer s, double d) {
		var _result = _meos_a.minus_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_int(Pointer s, int i) {
		var _result = _meos_a.minus_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.minus_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.minus_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.minus_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.minus_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.minus_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.minus_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.minus_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.minus_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.minus_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.minus_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_text_set(Pointer txt, Pointer s) {
		var _result = _meos_a.minus_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.minus_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.minus_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.minus_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_bigint_set(long i, Pointer s) {
		var _result = _meos_a.union_bigint_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_bigint_span(Pointer s, long i) {
		var _result = _meos_a.union_bigint_span(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_bigint_spanset(long i, Pointer ss) {
		var _result = _meos_a.union_bigint_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_date_set(int d, Pointer s) {
		var _result = _meos_a.union_date_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_date_span(Pointer s, int d) {
		var _result = _meos_a.union_date_span(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_date_spanset(int d, Pointer ss) {
		var _result = _meos_a.union_date_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_float_set(double d, Pointer s) {
		var _result = _meos_a.union_float_set(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_float_span(Pointer s, double d) {
		var _result = _meos_a.union_float_span(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_float_spanset(double d, Pointer ss) {
		var _result = _meos_a.union_float_spanset(d, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_int_set(int i, Pointer s) {
		var _result = _meos_a.union_int_set(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_int_span(int i, Pointer s) {
		var _result = _meos_a.union_int_span(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_int_spanset(int i, Pointer ss) {
		var _result = _meos_a.union_int_spanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_bigint(Pointer s, long i) {
		var _result = _meos_a.union_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_date(Pointer s, int d) {
		var _result = _meos_a.union_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_float(Pointer s, double d) {
		var _result = _meos_a.union_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_int(Pointer s, int i) {
		var _result = _meos_a.union_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_a.union_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_text(Pointer s, Pointer txt) {
		var _result = _meos_a.union_set_text(s, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.union_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_bigint(Pointer s, long i) {
		var _result = _meos_a.union_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_date(Pointer s, int d) {
		var _result = _meos_a.union_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_float(Pointer s, double d) {
		var _result = _meos_a.union_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_int(Pointer s, int i) {
		var _result = _meos_a.union_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_a.union_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_spanset(Pointer s, Pointer ss) {
		var _result = _meos_a.union_span_spanset(s, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.union_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.union_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.union_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.union_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.union_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_a.union_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.union_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.union_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_text_set(Pointer txt, Pointer s) {
		var _result = _meos_a.union_text_set(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.union_timestamptz_set(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.union_timestamptz_span(t_new, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.union_timestamptz_spanset(t_new, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_bigintset_bigintset(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_bigintset_bigintset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_bigintspan_bigintspan(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_bigintspan_bigintspan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_bigintspanset_bigintspan(Pointer ss, Pointer s) {
		var _result = _meos_a.distance_bigintspanset_bigintspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_bigintspanset_bigintspanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.distance_bigintspanset_bigintspanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_dateset_dateset(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_dateset_dateset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_datespan_datespan(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_datespan_datespan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_datespanset_datespan(Pointer ss, Pointer s) {
		var _result = _meos_a.distance_datespanset_datespan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_datespanset_datespanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.distance_datespanset_datespanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_floatset_floatset(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_floatset_floatset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_floatspan_floatspan(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_floatspan_floatspan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_floatspanset_floatspan(Pointer ss, Pointer s) {
		var _result = _meos_a.distance_floatspanset_floatspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_floatspanset_floatspanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.distance_floatspanset_floatspanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_intset_intset(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_intset_intset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_intspan_intspan(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_intspan_intspan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_intspanset_intspan(Pointer ss, Pointer s) {
		var _result = _meos_a.distance_intspanset_intspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_intspanset_intspanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.distance_intspanset_intspanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_set_bigint(Pointer s, long i) {
		var _result = _meos_a.distance_set_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_set_date(Pointer s, int d) {
		var _result = _meos_a.distance_set_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_set_float(Pointer s, double d) {
		var _result = _meos_a.distance_set_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_set_int(Pointer s, int i) {
		var _result = _meos_a.distance_set_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.distance_set_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_span_bigint(Pointer s, long i) {
		var _result = _meos_a.distance_span_bigint(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_span_date(Pointer s, int d) {
		var _result = _meos_a.distance_span_date(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_span_float(Pointer s, double d) {
		var _result = _meos_a.distance_span_float(s, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_span_int(Pointer s, int i) {
		var _result = _meos_a.distance_span_int(s, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.distance_span_timestamptz(s, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long distance_spanset_bigint(Pointer ss, long i) {
		var _result = _meos_a.distance_spanset_bigint(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_spanset_date(Pointer ss, int d) {
		var _result = _meos_a.distance_spanset_date(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_spanset_float(Pointer ss, double d) {
		var _result = _meos_a.distance_spanset_float(ss, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_spanset_int(Pointer ss, int i) {
		var _result = _meos_a.distance_spanset_int(ss, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.distance_spanset_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_tstzset_tstzset(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_tstzset_tstzset(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_tstzspan_tstzspan(Pointer s1, Pointer s2) {
		var _result = _meos_a.distance_tstzspan_tstzspan(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_tstzspanset_tstzspan(Pointer ss, Pointer s) {
		var _result = _meos_a.distance_tstzspanset_tstzspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_tstzspanset_tstzspanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_a.distance_tstzspanset_tstzspanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_extent_transfn(Pointer state, long i) {
		var _result = _meos_a.bigint_extent_transfn(state, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigint_union_transfn(Pointer state, long i) {
		var _result = _meos_a.bigint_union_transfn(state, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_extent_transfn(Pointer state, int d) {
		var _result = _meos_a.date_extent_transfn(state, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer date_union_transfn(Pointer state, int d) {
		var _result = _meos_a.date_union_transfn(state, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_extent_transfn(Pointer state, double d) {
		var _result = _meos_a.float_extent_transfn(state, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_union_transfn(Pointer state, double d) {
		var _result = _meos_a.float_union_transfn(state, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_extent_transfn(Pointer state, int i) {
		var _result = _meos_a.int_extent_transfn(state, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_union_transfn(Pointer state, int i) {
		var _result = _meos_a.int_union_transfn(state, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_extent_transfn(Pointer state, Pointer s) {
		var _result = _meos_a.set_extent_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_union_finalfn(Pointer state) {
		var _result = _meos_a.set_union_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_union_transfn(Pointer state, Pointer s) {
		var _result = _meos_a.set_union_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_extent_transfn(Pointer state, Pointer s) {
		var _result = _meos_a.span_extent_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_union_transfn(Pointer state, Pointer s) {
		var _result = _meos_a.span_union_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_extent_transfn(Pointer state, Pointer ss) {
		var _result = _meos_a.spanset_extent_transfn(state, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_union_finalfn(Pointer state) {
		var _result = _meos_a.spanset_union_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_union_transfn(Pointer state, Pointer ss) {
		var _result = _meos_a.spanset_union_transfn(state, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer text_union_transfn(Pointer state, Pointer txt) {
		var _result = _meos_a.text_union_transfn(state, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_extent_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.timestamptz_extent_transfn(state, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_union_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.timestamptz_union_transfn(state, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bigint_get_bin(long value, long vsize, long vorigin) {
		var _result = _meos_a.bigint_get_bin(value, vsize, vorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspan_bins(Pointer s, long vsize, long vorigin, Pointer count) {
		var _result = _meos_a.bigintspan_bins(s, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bigintspanset_bins(Pointer ss, long vsize, long vorigin, Pointer count) {
		var _result = _meos_a.bigintspanset_bins(ss, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int date_get_bin(int d, Pointer duration, int torigin) {
		var _result = _meos_a.date_get_bin(d, duration, torigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespan_bins(Pointer s, Pointer duration, int torigin, Pointer count) {
		var _result = _meos_a.datespan_bins(s, duration, torigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer datespanset_bins(Pointer ss, Pointer duration, int torigin, Pointer count) {
		var _result = _meos_a.datespanset_bins(ss, duration, torigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_get_bin(double value, double vsize, double vorigin) {
		var _result = _meos_a.float_get_bin(value, vsize, vorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspan_bins(Pointer s, double vsize, double vorigin, Pointer count) {
		var _result = _meos_a.floatspan_bins(s, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer floatspanset_bins(Pointer ss, double vsize, double vorigin, Pointer count) {
		var _result = _meos_a.floatspanset_bins(ss, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int int_get_bin(int value, int vsize, int vorigin) {
		var _result = _meos_a.int_get_bin(value, vsize, vorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspan_bins(Pointer s, int vsize, int vorigin, Pointer count) {
		var _result = _meos_a.intspan_bins(s, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intspanset_bins(Pointer ss, int vsize, int vorigin, Pointer count) {
		var _result = _meos_a.intspanset_bins(ss, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_get_bin(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_a.timestamptz_get_bin(t_new, duration, torigin_new);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_bins(Pointer s, Pointer duration, OffsetDateTime origin, Pointer count) {
		var origin_new = origin.toEpochSecond();
		var _result = _meos_a.tstzspan_bins(s, duration, origin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_bins(Pointer ss, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_a.tstzspanset_bins(ss, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tbox_as_hexwkb(Pointer box, byte variant, Pointer size) {
		var _result = _meos_a.tbox_as_hexwkb(box, variant, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_as_wkb(Pointer box, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_a.tbox_as_wkb(box, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_from_hexwkb(String hexwkb) {
		var _result = _meos_a.tbox_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_from_wkb(Pointer wkb, long size) {
		var _result = _meos_a.tbox_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_in(String str) {
		var _result = _meos_a.tbox_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tbox_out(Pointer box, int maxdd) {
		var _result = _meos_a.tbox_out(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_timestamptz_to_tbox(double d, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.float_timestamptz_to_tbox(d, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_tstzspan_to_tbox(double d, Pointer s) {
		var _result = _meos_a.float_tstzspan_to_tbox(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_timestamptz_to_tbox(int i, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.int_timestamptz_to_tbox(i, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_tstzspan_to_tbox(int i, Pointer s) {
		var _result = _meos_a.int_tstzspan_to_tbox(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s) {
		var _result = _meos_a.numspan_tstzspan_to_tbox(span, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer numspan_timestamptz_to_tbox(Pointer span, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.numspan_timestamptz_to_tbox(span, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_copy(Pointer box) {
		var _result = _meos_a.tbox_copy(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_make(Pointer s, Pointer p) {
		var _result = _meos_a.tbox_make(s, p);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer float_to_tbox(double d) {
		var _result = _meos_a.float_to_tbox(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer int_to_tbox(int i) {
		var _result = _meos_a.int_to_tbox(i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_to_tbox(Pointer s) {
		var _result = _meos_a.set_to_tbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_to_tbox(Pointer s) {
		var _result = _meos_a.span_to_tbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_to_tbox(Pointer ss) {
		var _result = _meos_a.spanset_to_tbox(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_to_bigintspan(Pointer box) {
		var _result = _meos_a.tbox_to_bigintspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_to_intspan(Pointer box) {
		var _result = _meos_a.tbox_to_intspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_to_floatspan(Pointer box) {
		var _result = _meos_a.tbox_to_floatspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_to_tstzspan(Pointer box) {
		var _result = _meos_a.tbox_to_tstzspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_tbox(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_a.timestamptz_to_tbox(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tbox_hash(Pointer box) {
		var _result = _meos_a.tbox_hash(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long tbox_hash_extended(Pointer box, long seed) {
		var _result = _meos_a.tbox_hash_extended(box, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_hast(Pointer box) {
		var _result = _meos_a.tbox_hast(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_hasx(Pointer box) {
		var _result = _meos_a.tbox_hasx(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_tmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.tbox_tmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_tmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = _meos_b.tbox_tmax_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_tmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.tbox_tmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_tmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = _meos_b.tbox_tmin_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_b.tbox_xmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_xmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = _meos_b.tbox_xmax_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_b.tbox_xmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_xmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = _meos_b.tbox_xmin_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboxfloat_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_b.tboxfloat_xmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboxfloat_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_b.tboxfloat_xmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboxint_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = _meos_b.tboxint_xmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboxint_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = _meos_b.tboxint_xmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigintbox_expand(Pointer box, long i) {
		var _result = _meos_b.tbigintbox_expand(box, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigintbox_shift_scale(Pointer box, long shift, long width, boolean hasshift, boolean haswidth) {
		var _result = _meos_b.tbigintbox_shift_scale(box, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_expand_time(Pointer box, Pointer interv) {
		var _result = _meos_b.tbox_expand_time(box, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_round(Pointer box, int maxdd) {
		var _result = _meos_b.tbox_round(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) {
		var _result = _meos_b.tbox_shift_scale_time(box, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_expand(Pointer box, double d) {
		var _result = _meos_b.tfloatbox_expand(box, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_shift_scale(Pointer box, double shift, double width, boolean hasshift, boolean haswidth) {
		var _result = _meos_b.tfloatbox_shift_scale(box, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_expand(Pointer box, int i) {
		var _result = _meos_b.tintbox_expand(box, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_shift_scale(Pointer box, int shift, int width, boolean hasshift, boolean haswidth) {
		var _result = _meos_b.tintbox_shift_scale(box, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict) {
		var _result = _meos_b.union_tbox_tbox(box1, box2, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.intersection_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.adjacent_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.contained_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.contains_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.overlaps_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.same_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.after_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.before_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.left_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.overafter_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.overbefore_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.overleft_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.overright_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_b.right_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tbox_cmp(Pointer box1, Pointer box2) {
		var _result = _meos_b.tbox_cmp(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_eq(Pointer box1, Pointer box2) {
		var _result = _meos_b.tbox_eq(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_ge(Pointer box1, Pointer box2) {
		var _result = _meos_b.tbox_ge(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_gt(Pointer box1, Pointer box2) {
		var _result = _meos_b.tbox_gt(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_le(Pointer box1, Pointer box2) {
		var _result = _meos_b.tbox_le(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_lt(Pointer box1, Pointer box2) {
		var _result = _meos_b.tbox_lt(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbox_ne(Pointer box1, Pointer box2) {
		var _result = _meos_b.tbox_ne(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_from_mfjson(String str) {
		var _result = _meos_b.tbigint_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_in(String str) {
		var _result = _meos_b.tbigint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tbigint_out(Pointer temp) {
		var _result = _meos_b.tbigint_out(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_from_mfjson(String str) {
		var _result = _meos_b.tbool_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_in(String str) {
		var _result = _meos_b.tbool_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tbool_out(Pointer temp) {
		var _result = _meos_b.tbool_out(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_as_hexwkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_b.temporal_as_hexwkb(temp, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs) {
		var _result = _meos_b.temporal_as_mfjson(temp, with_bbox, flags, precision, srs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_as_wkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_b.temporal_as_wkb(temp, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_from_hexwkb(String hexwkb) {
		var _result = _meos_b.temporal_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_from_wkb(Pointer wkb, long size) {
		var _result = _meos_b.temporal_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_from_mfjson(String str) {
		var _result = _meos_b.tfloat_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_in(String str) {
		var _result = _meos_b.tfloat_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tfloat_out(Pointer temp, int maxdd) {
		var _result = _meos_b.tfloat_out(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_from_mfjson(String str) {
		var _result = _meos_b.tint_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_in(String str) {
		var _result = _meos_b.tint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tint_out(Pointer temp) {
		var _result = _meos_b.tint_out(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_from_mfjson(String str) {
		var _result = _meos_b.ttext_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_in(String str) {
		var _result = _meos_b.ttext_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String ttext_out(Pointer temp) {
		var _result = _meos_b.ttext_out(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_from_base_temp(long i, Pointer temp) {
		var _result = _meos_b.tbigint_from_base_temp(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigintinst_make(long i, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.tbigintinst_make(i, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigintseq_from_base_tstzset(long i, Pointer s) {
		var _result = _meos_b.tbigintseq_from_base_tstzset(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigintseq_from_base_tstzspan(long i, Pointer s) {
		var _result = _meos_b.tbigintseq_from_base_tstzspan(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigintseqset_from_base_tstzspanset(long i, Pointer ss) {
		var _result = _meos_b.tbigintseqset_from_base_tstzspanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_from_base_temp(boolean b, Pointer temp) {
		var _result = _meos_b.tbool_from_base_temp(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolinst_make(boolean b, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.tboolinst_make(b, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_tstzset(boolean b, Pointer s) {
		var _result = _meos_b.tboolseq_from_base_tstzset(b, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_tstzspan(boolean b, Pointer s) {
		var _result = _meos_b.tboolseq_from_base_tstzspan(b, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseqset_from_base_tstzspanset(boolean b, Pointer ss) {
		var _result = _meos_b.tboolseqset_from_base_tstzspanset(b, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_copy(Pointer temp) {
		var _result = _meos_b.temporal_copy(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_from_base_temp(double d, Pointer temp) {
		var _result = _meos_b.tfloat_from_base_temp(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatinst_make(double d, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.tfloatinst_make(d, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_tstzset(double d, Pointer s) {
		var _result = _meos_b.tfloatseq_from_base_tstzset(d, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_tstzspan(double d, Pointer s, int interp) {
		var _result = _meos_b.tfloatseq_from_base_tstzspan(d, s, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_from_base_tstzspanset(double d, Pointer ss, int interp) {
		var _result = _meos_b.tfloatseqset_from_base_tstzspanset(d, ss, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_from_base_temp(int i, Pointer temp) {
		var _result = _meos_b.tint_from_base_temp(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintinst_make(int i, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.tintinst_make(i, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_tstzset(int i, Pointer s) {
		var _result = _meos_b.tintseq_from_base_tstzset(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_tstzspan(int i, Pointer s) {
		var _result = _meos_b.tintseq_from_base_tstzspan(i, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseqset_from_base_tstzspanset(int i, Pointer ss) {
		var _result = _meos_b.tintseqset_from_base_tstzspanset(i, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		var _result = _meos_b.tsequence_make(instants, count, lower_inc, upper_inc, interp, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize) {
		var _result = _meos_b.tsequenceset_make(sequences, count, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist) {
		var _result = _meos_b.tsequenceset_make_gaps(instants, count, interp, maxt, maxdist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_from_base_temp(Pointer txt, Pointer temp) {
		var _result = _meos_b.ttext_from_base_temp(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextinst_make(Pointer txt, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.ttextinst_make(txt, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s) {
		var _result = _meos_b.ttextseq_from_base_tstzset(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s) {
		var _result = _meos_b.ttextseq_from_base_tstzspan(txt, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss) {
		var _result = _meos_b.ttextseqset_from_base_tstzspanset(txt, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_to_tfloat(Pointer temp) {
		var _result = _meos_b.tbigint_to_tfloat(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_to_tint(Pointer temp) {
		var _result = _meos_b.tbigint_to_tint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_to_tint(Pointer temp) {
		var _result = _meos_b.tbool_to_tint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_to_tstzspan(Pointer temp) {
		var _result = _meos_b.temporal_to_tstzspan(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_to_tbigint(Pointer temp) {
		var _result = _meos_b.tfloat_to_tbigint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_to_tint(Pointer temp) {
		var _result = _meos_b.tfloat_to_tint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_to_tbigint(Pointer temp) {
		var _result = _meos_b.tint_to_tbigint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_to_tfloat(Pointer temp) {
		var _result = _meos_b.tint_to_tfloat(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_to_span(Pointer temp) {
		var _result = _meos_b.tnumber_to_span(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_to_tbox(Pointer temp) {
		var _result = _meos_b.tnumber_to_tbox(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long tbigint_end_value(Pointer temp) {
		var _result = _meos_b.tbigint_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long tbigint_max_value(Pointer temp) {
		var _result = _meos_b.tbigint_max_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long tbigint_min_value(Pointer temp) {
		var _result = _meos_b.tbigint_min_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long tbigint_start_value(Pointer temp) {
		var _result = _meos_b.tbigint_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbigint_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.tbigint_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_value_n(Pointer temp, long n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.tbigint_value_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_values(Pointer temp, Pointer count) {
		var _result = _meos_b.tbigint_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbool_end_value(Pointer temp) {
		var _result = _meos_b.tbool_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbool_start_value(Pointer temp) {
		var _result = _meos_b.tbool_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tbool_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.tbool_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = _meos_b.tbool_value_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_values(Pointer temp, Pointer count) {
		var _result = _meos_b.tbool_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_duration(Pointer temp, boolean boundspan) {
		var _result = _meos_b.temporal_duration(temp, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_end_instant(Pointer temp) {
		var _result = _meos_b.temporal_end_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_end_sequence(Pointer temp) {
		var _result = _meos_b.temporal_end_sequence(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_end_timestamptz(Pointer temp) {
		var _result = _meos_b.temporal_end_timestamptz(temp);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static int temporal_hash(Pointer temp) {
		var _result = _meos_b.temporal_hash(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_instant_n(Pointer temp, int n) {
		var _result = _meos_b.temporal_instant_n(temp, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_instants(Pointer temp, Pointer count) {
		var _result = _meos_b.temporal_instants(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_interp(Pointer temp) {
		var _result = _meos_b.temporal_interp(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_lower_inc(Pointer temp) {
		var _result = _meos_b.temporal_lower_inc(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_max_instant(Pointer temp) {
		var _result = _meos_b.temporal_max_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_min_instant(Pointer temp) {
		var _result = _meos_b.temporal_min_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_num_instants(Pointer temp) {
		var _result = _meos_b.temporal_num_instants(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_num_sequences(Pointer temp) {
		var _result = _meos_b.temporal_num_sequences(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_num_timestamps(Pointer temp) {
		var _result = _meos_b.temporal_num_timestamps(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_segm_duration(Pointer temp, Pointer duration, boolean atleast, boolean strict) {
		var _result = _meos_b.temporal_segm_duration(temp, duration, atleast, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_segments(Pointer temp, Pointer count) {
		var _result = _meos_b.temporal_segments(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_sequence_n(Pointer temp, int i) {
		var _result = _meos_b.temporal_sequence_n(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_sequences(Pointer temp, Pointer count) {
		var _result = _meos_b.temporal_sequences(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_start_instant(Pointer temp) {
		var _result = _meos_b.temporal_start_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_start_sequence(Pointer temp) {
		var _result = _meos_b.temporal_start_sequence(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_start_timestamptz(Pointer temp) {
		var _result = _meos_b.temporal_start_timestamptz(temp);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration) {
		var _result = _meos_b.temporal_stops(temp, maxdist, minduration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_subtype(Pointer temp) {
		var _result = _meos_b.temporal_subtype(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_time(Pointer temp) {
		var _result = _meos_b.temporal_time(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_timestamps(Pointer temp, Pointer count) {
		var _result = _meos_b.temporal_timestamps(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_timestamptz_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.temporal_timestamptz_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_upper_inc(Pointer temp) {
		var _result = _meos_b.temporal_upper_inc(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_avg_value(Pointer temp) {
		var _result = _meos_b.tfloat_avg_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_end_value(Pointer temp) {
		var _result = _meos_b.tfloat_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_min_value(Pointer temp) {
		var _result = _meos_b.tfloat_min_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_max_value(Pointer temp) {
		var _result = _meos_b.tfloat_max_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tfloat_start_value(Pointer temp) {
		var _result = _meos_b.tfloat_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tfloat_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.tfloat_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_b.tfloat_value_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_values(Pointer temp, Pointer count) {
		var _result = _meos_b.tfloat_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tint_end_value(Pointer temp) {
		var _result = _meos_b.tint_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tint_max_value(Pointer temp) {
		var _result = _meos_b.tint_max_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tint_min_value(Pointer temp) {
		var _result = _meos_b.tint_min_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tint_start_value(Pointer temp) {
		var _result = _meos_b.tint_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tint_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.tint_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);
		out = _meos_b.tint_value_n(temp, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_values(Pointer temp, Pointer count) {
		var _result = _meos_b.tint_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumber_avg_value(Pointer temp) {
		var _result = _meos_b.tnumber_avg_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumber_integral(Pointer temp) {
		var _result = _meos_b.tnumber_integral(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumber_twavg(Pointer temp) {
		var _result = _meos_b.tnumber_twavg(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_valuespans(Pointer temp) {
		var _result = _meos_b.tnumber_valuespans(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_end_value(Pointer temp) {
		var _result = _meos_b.ttext_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_max_value(Pointer temp) {
		var _result = _meos_b.ttext_max_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_min_value(Pointer temp) {
		var _result = _meos_b.ttext_min_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_start_value(Pointer temp) {
		var _result = _meos_b.ttext_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean ttext_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.ttext_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_b.ttext_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_values(Pointer temp, Pointer count) {
		var _result = _meos_b.ttext_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_degrees(double value, boolean normalize) {
		var _result = _meos_b.float_degrees(value, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_scale_value(Pointer temp, long width) {
		var _result = _meos_b.tbigint_scale_value(temp, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_shift_scale_value(Pointer temp, long shift, long width) {
		var _result = _meos_b.tbigint_shift_scale_value(temp, shift, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_shift_value(Pointer temp, long shift) {
		var _result = _meos_b.tbigint_shift_value(temp, shift);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temparr_round(Pointer temp, int count, int maxdd) {
		var _result = _meos_b.temparr_round(temp, count, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_round(Pointer temp, int maxdd) {
		var _result = _meos_b.temporal_round(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_scale_time(Pointer temp, Pointer duration) {
		var _result = _meos_b.temporal_scale_time(temp, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_set_interp(Pointer temp, int interp) {
		var _result = _meos_b.temporal_set_interp(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration) {
		var _result = _meos_b.temporal_shift_scale_time(temp, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_shift_time(Pointer temp, Pointer shift) {
		var _result = _meos_b.temporal_shift_time(temp, shift);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_to_tinstant(Pointer temp) {
		var _result = _meos_b.temporal_to_tinstant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_to_tsequence(Pointer temp, int interp) {
		var _result = _meos_b.temporal_to_tsequence(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_to_tsequenceset(Pointer temp, int interp) {
		var _result = _meos_b.temporal_to_tsequenceset(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_ceil(Pointer temp) {
		var _result = _meos_b.tfloat_ceil(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_degrees(Pointer temp, boolean normalize) {
		var _result = _meos_b.tfloat_degrees(temp, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_floor(Pointer temp) {
		var _result = _meos_b.tfloat_floor(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_radians(Pointer temp) {
		var _result = _meos_b.tfloat_radians(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_scale_value(Pointer temp, double width) {
		var _result = _meos_b.tfloat_scale_value(temp, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width) {
		var _result = _meos_b.tfloat_shift_scale_value(temp, shift, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_shift_value(Pointer temp, double shift) {
		var _result = _meos_b.tfloat_shift_value(temp, shift);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_scale_value(Pointer temp, int width) {
		var _result = _meos_b.tint_scale_value(temp, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_shift_scale_value(Pointer temp, int shift, int width) {
		var _result = _meos_b.tint_shift_scale_value(temp, shift, width);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_shift_value(Pointer temp, int shift) {
		var _result = _meos_b.tint_shift_value(temp, shift);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_append_tinstant(Pointer temp, Pointer inst, int interp, double maxdist, Pointer maxt, boolean expand) {
		var _result = _meos_b.temporal_append_tinstant(temp, inst, interp, maxdist, maxt, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand) {
		var _result = _meos_b.temporal_append_tsequence(temp, seq, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_delete_timestamptz(Pointer temp, OffsetDateTime t, boolean connect) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.temporal_delete_timestamptz(temp, t_new, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzset(Pointer temp, Pointer s, boolean connect) {
		var _result = _meos_b.temporal_delete_tstzset(temp, s, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzspan(Pointer temp, Pointer s, boolean connect) {
		var _result = _meos_b.temporal_delete_tstzspan(temp, s, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect) {
		var _result = _meos_b.temporal_delete_tstzspanset(temp, ss, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect) {
		var _result = _meos_b.temporal_insert(temp1, temp2, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_merge(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_merge(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_merge_array(Pointer temparr, int count) {
		var _result = _meos_b.temporal_merge_array(temparr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect) {
		var _result = _meos_b.temporal_update(temp1, temp2, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_at_value(Pointer temp, long i) {
		var _result = _meos_b.tbigint_at_value(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_minus_value(Pointer temp, long i) {
		var _result = _meos_b.tbigint_minus_value(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_at_value(Pointer temp, boolean b) {
		var _result = _meos_b.tbool_at_value(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_minus_value(Pointer temp, boolean b) {
		var _result = _meos_b.tbool_minus_value(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_after_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.temporal_after_timestamptz(temp, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_max(Pointer temp) {
		var _result = _meos_b.temporal_at_max(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_min(Pointer temp) {
		var _result = _meos_b.temporal_at_min(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_timestamptz(Pointer temp, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.temporal_at_timestamptz(temp, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzset(Pointer temp, Pointer s) {
		var _result = _meos_b.temporal_at_tstzset(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.temporal_at_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzspanset(Pointer temp, Pointer ss) {
		var _result = _meos_b.temporal_at_tstzspanset(temp, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_at_values(Pointer temp, Pointer set) {
		var _result = _meos_b.temporal_at_values(temp, set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_before_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.temporal_before_timestamptz(temp, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_max(Pointer temp) {
		var _result = _meos_b.temporal_minus_max(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_min(Pointer temp) {
		var _result = _meos_b.temporal_minus_min(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_timestamptz(Pointer temp, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.temporal_minus_timestamptz(temp, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzset(Pointer temp, Pointer s) {
		var _result = _meos_b.temporal_minus_tstzset(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.temporal_minus_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzspanset(Pointer temp, Pointer ss) {
		var _result = _meos_b.temporal_minus_tstzspanset(temp, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_minus_values(Pointer temp, Pointer set) {
		var _result = _meos_b.temporal_minus_values(temp, set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_at_value(Pointer temp, double d) {
		var _result = _meos_b.tfloat_at_value(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_minus_value(Pointer temp, double d) {
		var _result = _meos_b.tfloat_minus_value(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_at_value(Pointer temp, int i) {
		var _result = _meos_b.tint_at_value(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_minus_value(Pointer temp, int i) {
		var _result = _meos_b.tint_minus_value(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_at_span(Pointer temp, Pointer span) {
		var _result = _meos_b.tnumber_at_span(temp, span);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_at_spanset(Pointer temp, Pointer ss) {
		var _result = _meos_b.tnumber_at_spanset(temp, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_at_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.tnumber_at_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_span(Pointer temp, Pointer span) {
		var _result = _meos_b.tnumber_minus_span(temp, span);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_spanset(Pointer temp, Pointer ss) {
		var _result = _meos_b.tnumber_minus_spanset(temp, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.tnumber_minus_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_at_value(Pointer temp, Pointer txt) {
		var _result = _meos_b.ttext_at_value(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_minus_value(Pointer temp, Pointer txt) {
		var _result = _meos_b.ttext_minus_value(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_cmp(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_cmp(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_eq(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_eq(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_ge(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_ge(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_gt(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_gt(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_le(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_le(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_lt(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_lt(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_ne(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_ne(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.always_eq_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_bool_tbool(boolean b, Pointer temp) {
		var _result = _meos_b.always_eq_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.always_eq_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_int_tint(int i, Pointer temp) {
		var _result = _meos_b.always_eq_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tbool_bool(Pointer temp, boolean b) {
		var _result = _meos_b.always_eq_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.always_eq_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.always_eq_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.always_eq_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.always_eq_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tint_int(Pointer temp, int i) {
		var _result = _meos_b.always_eq_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.always_eq_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.always_ge_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.always_ge_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_int_tint(int i, Pointer temp) {
		var _result = _meos_b.always_ge_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.always_ge_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.always_ge_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.always_ge_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.always_ge_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_tint_int(Pointer temp, int i) {
		var _result = _meos_b.always_ge_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.always_ge_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.always_gt_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.always_gt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_int_tint(int i, Pointer temp) {
		var _result = _meos_b.always_gt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.always_gt_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.always_gt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.always_gt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.always_gt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_tint_int(Pointer temp, int i) {
		var _result = _meos_b.always_gt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.always_gt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.always_le_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.always_le_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_int_tint(int i, Pointer temp) {
		var _result = _meos_b.always_le_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.always_le_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.always_le_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.always_le_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.always_le_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_tint_int(Pointer temp, int i) {
		var _result = _meos_b.always_le_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.always_le_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.always_lt_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.always_lt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_int_tint(int i, Pointer temp) {
		var _result = _meos_b.always_lt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.always_lt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.always_lt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.always_lt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_tint_int(Pointer temp, int i) {
		var _result = _meos_b.always_lt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.always_lt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_bool_tbool(boolean b, Pointer temp) {
		var _result = _meos_b.always_ne_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.always_ne_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_int_tint(int i, Pointer temp) {
		var _result = _meos_b.always_ne_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.always_lt_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.always_ne_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tbool_bool(Pointer temp, boolean b) {
		var _result = _meos_b.always_ne_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.always_ne_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.always_ne_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.always_ne_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.always_ne_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tint_int(Pointer temp, int i) {
		var _result = _meos_b.always_ne_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.always_ne_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.ever_eq_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_bool_tbool(boolean b, Pointer temp) {
		var _result = _meos_b.ever_eq_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.ever_eq_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_int_tint(int i, Pointer temp) {
		var _result = _meos_b.ever_eq_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.ever_eq_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tbool_bool(Pointer temp, boolean b) {
		var _result = _meos_b.ever_eq_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.ever_eq_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.ever_eq_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.ever_eq_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tint_int(Pointer temp, int i) {
		var _result = _meos_b.ever_eq_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.ever_eq_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.ever_ge_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.ever_ge_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_int_tint(int i, Pointer temp) {
		var _result = _meos_b.ever_ge_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.ever_ge_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.ever_ge_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.ever_ge_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.ever_ge_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_tint_int(Pointer temp, int i) {
		var _result = _meos_b.ever_ge_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.ever_ge_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.ever_gt_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.ever_gt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_int_tint(int i, Pointer temp) {
		var _result = _meos_b.ever_gt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.ever_gt_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.ever_gt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.ever_gt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.ever_gt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_tint_int(Pointer temp, int i) {
		var _result = _meos_b.ever_gt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.ever_gt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.ever_le_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.ever_le_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_int_tint(int i, Pointer temp) {
		var _result = _meos_b.ever_le_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.ever_le_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.ever_le_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.ever_le_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.ever_le_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_tint_int(Pointer temp, int i) {
		var _result = _meos_b.ever_le_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.ever_le_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.ever_lt_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.ever_lt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_int_tint(int i, Pointer temp) {
		var _result = _meos_b.ever_lt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.ever_lt_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.ever_lt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.ever_lt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.ever_lt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_tint_int(Pointer temp, int i) {
		var _result = _meos_b.ever_lt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.ever_lt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_bigint_tbigint(long i, Pointer temp) {
		var _result = _meos_b.ever_ne_bigint_tbigint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_bool_tbool(boolean b, Pointer temp) {
		var _result = _meos_b.ever_ne_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.ever_ne_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_int_tint(int i, Pointer temp) {
		var _result = _meos_b.ever_ne_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tbigint_bigint(Pointer temp, long i) {
		var _result = _meos_b.ever_ne_tbigint_bigint(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tbool_bool(Pointer temp, boolean b) {
		var _result = _meos_b.ever_ne_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.ever_ne_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.ever_ne_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.ever_ne_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tint_int(Pointer temp, int i) {
		var _result = _meos_b.ever_ne_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.ever_ne_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_bool_tbool(boolean b, Pointer temp) {
		var _result = _meos_b.teq_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.teq_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_int_tint(int i, Pointer temp) {
		var _result = _meos_b.teq_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tbool_bool(Pointer temp, boolean b) {
		var _result = _meos_b.teq_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.teq_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.teq_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.teq_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tint_int(Pointer temp, int i) {
		var _result = _meos_b.teq_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.teq_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.tge_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_int_tint(int i, Pointer temp) {
		var _result = _meos_b.tge_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.tge_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.tge_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.tge_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_tint_int(Pointer temp, int i) {
		var _result = _meos_b.tge_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tge_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.tge_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.tgt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_int_tint(int i, Pointer temp) {
		var _result = _meos_b.tgt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.tgt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.tgt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.tgt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_tint_int(Pointer temp, int i) {
		var _result = _meos_b.tgt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgt_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.tgt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.tle_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_int_tint(int i, Pointer temp) {
		var _result = _meos_b.tle_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.tle_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.tle_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.tle_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_tint_int(Pointer temp, int i) {
		var _result = _meos_b.tle_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tle_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.tle_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.tlt_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_int_tint(int i, Pointer temp) {
		var _result = _meos_b.tlt_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.tlt_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.tlt_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.tlt_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_tint_int(Pointer temp, int i) {
		var _result = _meos_b.tlt_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tlt_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.tlt_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_bool_tbool(boolean b, Pointer temp) {
		var _result = _meos_b.tne_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_float_tfloat(double d, Pointer temp) {
		var _result = _meos_b.tne_float_tfloat(d, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_int_tint(int i, Pointer temp) {
		var _result = _meos_b.tne_int_tint(i, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tbool_bool(Pointer temp, boolean b) {
		var _result = _meos_b.tne_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.tne_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.tne_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.tne_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tint_int(Pointer temp, int i) {
		var _result = _meos_b.tne_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.tne_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_spans(Pointer temp, Pointer count) {
		var _result = _meos_b.temporal_spans(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_split_each_n_spans(Pointer temp, int elem_count, Pointer count) {
		var _result = _meos_b.temporal_split_each_n_spans(temp, elem_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_split_n_spans(Pointer temp, int span_count, Pointer count) {
		var _result = _meos_b.temporal_split_n_spans(temp, span_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_split_each_n_tboxes(Pointer temp, int elem_count, Pointer count) {
		var _result = _meos_b.tnumber_split_each_n_tboxes(temp, elem_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_split_n_tboxes(Pointer temp, int box_count, Pointer count) {
		var _result = _meos_b.tnumber_split_n_tboxes(temp, box_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_tboxes(Pointer temp, Pointer count) {
		var _result = _meos_b.tnumber_tboxes(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.adjacent_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.adjacent_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.adjacent_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.adjacent_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.adjacent_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.adjacent_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.adjacent_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.adjacent_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.contained_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.contained_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.contained_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.contained_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.contained_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.contained_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.contained_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.contained_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.contains_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.contains_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.contains_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.contains_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.contains_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.contains_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.contains_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.contains_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.overlaps_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.overlaps_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.overlaps_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.overlaps_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.overlaps_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.overlaps_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.overlaps_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.overlaps_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.same_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.same_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.same_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.same_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.same_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.same_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.same_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.same_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.after_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.after_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.after_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.after_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.after_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.after_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.before_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.before_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.before_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.before_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.before_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.before_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.left_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.left_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.left_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.left_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.left_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.overafter_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.overafter_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.overafter_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.overafter_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.overafter_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.overafter_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.overbefore_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_temporal_tstzspan(Pointer temp, Pointer s) {
		var _result = _meos_b.overbefore_temporal_tstzspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.overbefore_temporal_temporal(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.overbefore_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.overbefore_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tstzspan_temporal(Pointer s, Pointer temp) {
		var _result = _meos_b.overbefore_tstzspan_temporal(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.overleft_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.overleft_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.overleft_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.overleft_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.overleft_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.overright_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.overright_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.overright_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.overright_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.overright_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_numspan_tnumber(Pointer s, Pointer temp) {
		var _result = _meos_b.right_numspan_tnumber(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tbox_tnumber(Pointer box, Pointer temp) {
		var _result = _meos_b.right_tbox_tnumber(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tnumber_numspan(Pointer temp, Pointer s) {
		var _result = _meos_b.right_tnumber_numspan(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.right_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.right_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tand_bool_tbool(boolean b, Pointer temp) {
		var _result = _meos_b.tand_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tand_tbool_bool(Pointer temp, boolean b) {
		var _result = _meos_b.tand_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tand_tbool_tbool(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.tand_tbool_tbool(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_when_true(Pointer temp) {
		var _result = _meos_b.tbool_when_true(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnot_tbool(Pointer temp) {
		var _result = _meos_b.tnot_tbool(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tor_bool_tbool(boolean b, Pointer temp) {
		var _result = _meos_b.tor_bool_tbool(b, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tor_tbool_bool(Pointer temp, boolean b) {
		var _result = _meos_b.tor_tbool_bool(temp, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tor_tbool_tbool(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.tor_tbool_tbool(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_bigint_tbigint(long i, Pointer tnumber) {
		var _result = _meos_b.add_bigint_tbigint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_float_tfloat(double d, Pointer tnumber) {
		var _result = _meos_b.add_float_tfloat(d, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_int_tint(int i, Pointer tnumber) {
		var _result = _meos_b.add_int_tint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_tbigint_bigint(Pointer tnumber, long i) {
		var _result = _meos_b.add_tbigint_bigint(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_tfloat_float(Pointer tnumber, double d) {
		var _result = _meos_b.add_tfloat_float(tnumber, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_tint_int(Pointer tnumber, int i) {
		var _result = _meos_b.add_tint_int(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer add_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		var _result = _meos_b.add_tnumber_tnumber(tnumber1, tnumber2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_bigint_tbigint(long i, Pointer tnumber) {
		var _result = _meos_b.div_bigint_tbigint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_float_tfloat(double d, Pointer tnumber) {
		var _result = _meos_b.div_float_tfloat(d, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_int_tint(int i, Pointer tnumber) {
		var _result = _meos_b.div_int_tint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_tbigint_bigint(Pointer tnumber, long i) {
		var _result = _meos_b.div_tbigint_bigint(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_tfloat_float(Pointer tnumber, double d) {
		var _result = _meos_b.div_tfloat_float(tnumber, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_tint_int(Pointer tnumber, int i) {
		var _result = _meos_b.div_tint_int(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer div_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		var _result = _meos_b.div_tnumber_tnumber(tnumber1, tnumber2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_bigint_tbigint(long i, Pointer tnumber) {
		var _result = _meos_b.mul_bigint_tbigint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_float_tfloat(double d, Pointer tnumber) {
		var _result = _meos_b.mul_float_tfloat(d, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_int_tint(int i, Pointer tnumber) {
		var _result = _meos_b.mul_int_tint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_tbigint_bigint(Pointer tnumber, long i) {
		var _result = _meos_b.mul_tbigint_bigint(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_tfloat_float(Pointer tnumber, double d) {
		var _result = _meos_b.mul_tfloat_float(tnumber, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_tint_int(Pointer tnumber, int i) {
		var _result = _meos_b.mul_tint_int(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer mul_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		var _result = _meos_b.mul_tnumber_tnumber(tnumber1, tnumber2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_bigint_tbigint(long i, Pointer tnumber) {
		var _result = _meos_b.sub_bigint_tbigint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_float_tfloat(double d, Pointer tnumber) {
		var _result = _meos_b.sub_float_tfloat(d, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_int_tint(int i, Pointer tnumber) {
		var _result = _meos_b.sub_int_tint(i, tnumber);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_tbigint_bigint(Pointer tnumber, long i) {
		var _result = _meos_b.sub_tbigint_bigint(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_tfloat_float(Pointer tnumber, double d) {
		var _result = _meos_b.sub_tfloat_float(tnumber, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_tint_int(Pointer tnumber, int i) {
		var _result = _meos_b.sub_tint_int(tnumber, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer sub_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		var _result = _meos_b.sub_tnumber_tnumber(tnumber1, tnumber2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_derivative(Pointer temp) {
		var _result = _meos_b.temporal_derivative(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_exp(Pointer temp) {
		var _result = _meos_b.tfloat_exp(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_ln(Pointer temp) {
		var _result = _meos_b.tfloat_ln(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_log10(Pointer temp) {
		var _result = _meos_b.tfloat_log10(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_abs(Pointer temp) {
		var _result = _meos_b.tnumber_abs(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_trend(Pointer temp) {
		var _result = _meos_b.tnumber_trend(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double float_angular_difference(double degrees1, double degrees2) {
		var _result = _meos_b.float_angular_difference(degrees1, degrees2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_angular_difference(Pointer temp) {
		var _result = _meos_b.tnumber_angular_difference(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_delta_value(Pointer temp) {
		var _result = _meos_b.tnumber_delta_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_text_ttext(Pointer txt, Pointer temp) {
		var _result = _meos_b.textcat_text_ttext(txt, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_ttext_text(Pointer temp, Pointer txt) {
		var _result = _meos_b.textcat_ttext_text(temp, txt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.textcat_ttext_ttext(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_initcap(Pointer temp) {
		var _result = _meos_b.ttext_initcap(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_upper(Pointer temp) {
		var _result = _meos_b.ttext_upper(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_lower(Pointer temp) {
		var _result = _meos_b.ttext_lower(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.tdistance_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tint_int(Pointer temp, int i) {
		var _result = _meos_b.tdistance_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.tdistance_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2) {
		var _result = _meos_b.nad_tboxfloat_tboxfloat(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nad_tboxint_tboxint(Pointer box1, Pointer box2) {
		var _result = _meos_b.nad_tboxint_tboxint(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tfloat_float(Pointer temp, double d) {
		var _result = _meos_b.nad_tfloat_float(temp, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tfloat_tfloat(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.nad_tfloat_tfloat(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tfloat_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.nad_tfloat_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nad_tint_int(Pointer temp, int i) {
		var _result = _meos_b.nad_tint_int(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nad_tint_tbox(Pointer temp, Pointer box) {
		var _result = _meos_b.nad_tint_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nad_tint_tint(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.nad_tint_tint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_tand_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tbool_tand_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbool_tor_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tbool_tor_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_extent_transfn(Pointer s, Pointer temp) {
		var _result = _meos_b.temporal_extent_transfn(s, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_merge_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.temporal_merge_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_merge_combinefn(Pointer state1, Pointer state2) {
		var _result = _meos_b.temporal_merge_combinefn(state1, state2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tagg_finalfn(Pointer state) {
		var _result = _meos_b.temporal_tagg_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tcount_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.temporal_tcount_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_tmax_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tfloat_tmax_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_tmin_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tfloat_tmin_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_tsum_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tfloat_tsum_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_wmax_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = _meos_b.tfloat_wmax_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_wmin_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = _meos_b.tfloat_wmin_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_wsum_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = _meos_b.tfloat_wsum_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_tcount_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_b.timestamptz_tcount_transfn(state, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_tmax_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tint_tmax_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_tmin_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tint_tmin_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_tsum_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tint_tsum_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_wmax_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = _meos_b.tint_wmax_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_wmin_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = _meos_b.tint_wmin_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_wsum_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = _meos_b.tint_wsum_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_extent_transfn(Pointer box, Pointer temp) {
		var _result = _meos_b.tnumber_extent_transfn(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_tavg_finalfn(Pointer state) {
		var _result = _meos_b.tnumber_tavg_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_tavg_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.tnumber_tavg_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_wavg_transfn(Pointer state, Pointer temp, Pointer interv) {
		var _result = _meos_b.tnumber_wavg_transfn(state, temp, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_tcount_transfn(Pointer state, Pointer s) {
		var _result = _meos_b.tstzset_tcount_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_tcount_transfn(Pointer state, Pointer s) {
		var _result = _meos_b.tstzspan_tcount_transfn(state, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_tcount_transfn(Pointer state, Pointer ss) {
		var _result = _meos_b.tstzspanset_tcount_transfn(state, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_tmax_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.ttext_tmax_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttext_tmin_transfn(Pointer state, Pointer temp) {
		var _result = _meos_b.ttext_tmin_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_dp(Pointer temp, double eps_dist, boolean synchronize) {
		var _result = _meos_b.temporal_simplify_dp(temp, eps_dist, synchronize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_max_dist(Pointer temp, double eps_dist, boolean synchronize) {
		var _result = _meos_b.temporal_simplify_max_dist(temp, eps_dist, synchronize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_dist(Pointer temp, double dist) {
		var _result = _meos_b.temporal_simplify_min_dist(temp, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_tdelta(Pointer temp, Pointer mint) {
		var _result = _meos_b.temporal_simplify_min_tdelta(temp, mint);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tprecision(Pointer temp, Pointer duration, OffsetDateTime origin) {
		var origin_new = origin.toEpochSecond();
		var _result = _meos_b.temporal_tprecision(temp, duration, origin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tsample(Pointer temp, Pointer duration, OffsetDateTime origin, int interp) {
		var origin_new = origin.toEpochSecond();
		var _result = _meos_b.temporal_tsample(temp, duration, origin_new, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double temporal_dyntimewarp_distance(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_dyntimewarp_distance(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_dyntimewarp_path(Pointer temp1, Pointer temp2, Pointer count) {
		var _result = _meos_b.temporal_dyntimewarp_path(temp1, temp2, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double temporal_frechet_distance(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_frechet_distance(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_frechet_path(Pointer temp1, Pointer temp2, Pointer count) {
		var _result = _meos_b.temporal_frechet_path(temp1, temp2, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double temporal_hausdorff_distance(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.temporal_hausdorff_distance(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_time_bins(Pointer temp, Pointer duration, OffsetDateTime origin, Pointer count) {
		var origin_new = origin.toEpochSecond();
		var _result = _meos_b.temporal_time_bins(temp, duration, origin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_time_split(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.temporal_time_split(temp, duration, torigin_new, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_time_boxes(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tfloat_time_boxes(temp, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_bins(Pointer temp, double vsize, double vorigin, Pointer count) {
		var _result = _meos_b.tfloat_value_bins(temp, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_boxes(Pointer temp, double vsize, double vorigin, Pointer count) {
		var _result = _meos_b.tfloat_value_boxes(temp, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer bins, Pointer count) {
		var _result = _meos_b.tfloat_value_split(temp, size, origin, bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_time_boxes(Pointer temp, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tfloat_value_time_boxes(temp, vsize, duration, vorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloat_value_time_split(Pointer temp, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer value_bins, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tfloat_value_time_split(temp, vsize, duration, vorigin, torigin_new, value_bins, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_time_tiles(Pointer box, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tfloatbox_time_tiles(box, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_value_tiles(Pointer box, double vsize, double vorigin, Pointer count) {
		var _result = _meos_b.tfloatbox_value_tiles(box, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatbox_value_time_tiles(Pointer box, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tfloatbox_value_time_tiles(box, vsize, duration, vorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_time_boxes(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tint_time_boxes(temp, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_bins(Pointer temp, int vsize, int vorigin, Pointer count) {
		var _result = _meos_b.tint_value_bins(temp, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_boxes(Pointer temp, int vsize, int vorigin, Pointer count) {
		var _result = _meos_b.tint_value_boxes(temp, vsize, vorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_split(Pointer temp, int vsize, int vorigin, Pointer bins, Pointer count) {
		var _result = _meos_b.tint_value_split(temp, vsize, vorigin, bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_time_boxes(Pointer temp, int vsize, Pointer duration, int vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tint_value_time_boxes(temp, vsize, duration, vorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tint_value_time_split(Pointer temp, long size, Pointer duration, int vorigin, OffsetDateTime torigin, Pointer value_bins, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tint_value_time_split(temp, size, duration, vorigin, torigin_new, value_bins, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_time_tiles(Pointer box, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tintbox_time_tiles(box, duration, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_value_tiles(Pointer box, int xsize, int xorigin, Pointer count) {
		var _result = _meos_b.tintbox_value_tiles(box, xsize, xorigin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintbox_value_time_tiles(Pointer box, int xsize, Pointer duration, int xorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_b.tintbox_value_time_tiles(box, xsize, duration, xorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void meos_initialize_noexit_error_handler() {
		_meos_b.meos_initialize_noexit_error_handler();
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static boolean h3_are_neighbor_cells_meos(long origin, long destination) {
		var _result = _meos_b.h3_are_neighbor_cells_meos(origin, destination);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_cells_to_directed_edge_meos(long origin, long destination) {
		var _result = _meos_b.h3_cells_to_directed_edge_meos(origin, destination);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3_is_valid_directed_edge_meos(long edge) {
		var _result = _meos_b.h3_is_valid_directed_edge_meos(edge);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_get_directed_edge_origin_meos(long edge) {
		var _result = _meos_b.h3_get_directed_edge_origin_meos(edge);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_get_directed_edge_destination_meos(long edge) {
		var _result = _meos_b.h3_get_directed_edge_destination_meos(edge);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_cell_to_parent_meos(long origin, int resolution) {
		var _result = _meos_b.h3_cell_to_parent_meos(origin, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_cell_to_center_child_meos(long origin, int resolution) {
		var _result = _meos_b.h3_cell_to_center_child_meos(origin, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_cell_to_child_pos_meos(long child, int parentRes) {
		var _result = _meos_b.h3_cell_to_child_pos_meos(child, parentRes);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_child_pos_to_cell_meos(long childPos, long parent, int childRes) {
		var _result = _meos_b.h3_child_pos_to_cell_meos(childPos, parent, childRes);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int h3_get_resolution_meos(long hex) {
		var _result = _meos_b.h3_get_resolution_meos(hex);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int h3_get_base_cell_number_meos(long hex) {
		var _result = _meos_b.h3_get_base_cell_number_meos(hex);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3_is_valid_cell_meos(long hex) {
		var _result = _meos_b.h3_is_valid_cell_meos(hex);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3_is_res_class_iii_meos(long hex) {
		var _result = _meos_b.h3_is_res_class_iii_meos(hex);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3_is_pentagon_meos(long hex) {
		var _result = _meos_b.h3_is_pentagon_meos(hex);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_get_num_cells_meos(int resolution) {
		var _result = _meos_b.h3_get_num_cells_meos(resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_grid_distance_meos(long originIndex, long h3Index) {
		var _result = _meos_b.h3_grid_distance_meos(originIndex, h3Index);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_cell_to_vertex_meos(long cell, int vertexNum) {
		var _result = _meos_b.h3_cell_to_vertex_meos(cell, vertexNum);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3_is_valid_vertex_meos(long vertex) {
		var _result = _meos_b.h3_is_valid_vertex_meos(vertex);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3index_parse(String str) {
		var _result = _meos_b.h3index_parse(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String h3index_to_string(long cell) {
		var _result = _meos_b.h3index_to_string(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3index_eq(long a, long b) {
		var _result = _meos_b.h3index_eq(a, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3index_ne(long a, long b) {
		var _result = _meos_b.h3index_ne(a, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3index_lt(long a, long b) {
		var _result = _meos_b.h3index_lt(a, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3index_le(long a, long b) {
		var _result = _meos_b.h3index_le(a, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3index_gt(long a, long b) {
		var _result = _meos_b.h3index_gt(a, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean h3index_ge(long a, long b) {
		var _result = _meos_b.h3index_ge(a, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int h3index_cmp(long a, long b) {
		var _result = _meos_b.h3index_cmp(a, b);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int h3index_hash(long cell) {
		var _result = _meos_b.h3index_hash(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_grid_disk(long origin, int k) {
		var _result = _meos_b.h3_grid_disk(origin, k);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_grid_ring(long origin, int k) {
		var _result = _meos_b.h3_grid_ring(origin, k);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_grid_path_cells(long start, long end) {
		var _result = _meos_b.h3_grid_path_cells(start, end);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_cell_to_children(long origin, int childRes) {
		var _result = _meos_b.h3_cell_to_children(origin, childRes);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_compact_cells(Pointer cells) {
		var _result = _meos_b.h3_compact_cells(cells);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_uncompact_cells(Pointer cells, int res) {
		var _result = _meos_b.h3_uncompact_cells(cells, res);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_origin_to_directed_edges(long origin) {
		var _result = _meos_b.h3_origin_to_directed_edges(origin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_cell_to_vertexes(long cell) {
		var _result = _meos_b.h3_cell_to_vertexes(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_get_icosahedron_faces(long cell) {
		var _result = _meos_b.h3_get_icosahedron_faces(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean ensure_valid_th3index_th3index(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.ensure_valid_th3index_th3index(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean ensure_valid_th3index_h3index(Pointer temp, long cell) {
		var _result = _meos_b.ensure_valid_th3index_h3index(temp, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean ensure_valid_th3index_tgeogpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_b.ensure_valid_th3index_tgeogpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum2_h3index_eq(int d1, int d2, int type) {
		var _result = _meos_b.datum2_h3index_eq(d1, d2, type);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum2_h3index_ne(int d1, int d2, int type) {
		var _result = _meos_b.datum2_h3index_ne(d1, d2, type);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void th3indexinst_set_stbox(Pointer inst, Pointer box) {
		_meos_b.th3indexinst_set_stbox(inst, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void th3indexinstarr_set_stbox(Pointer instants, int count, Pointer box) {
		_meos_b.th3indexinstarr_set_stbox(instants, count, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void th3indexseq_expand_stbox(Pointer seq, Pointer inst) {
		_meos_b.th3indexseq_expand_stbox(seq, inst);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static long h3_gs_point_to_cell(Pointer point, int resolution) {
		var _result = _meos_b.h3_gs_point_to_cell(point, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_cell_to_gs_point(long cell) {
		var _result = _meos_b.h3_cell_to_gs_point(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_cell_to_gs_boundary(long cell) {
		var _result = _meos_b.h3_cell_to_gs_boundary(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cell_boundary_to_gs(Pointer bnd) {
		var _result = _meos_b.cell_boundary_to_gs(bnd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double h3_sample_step_deg(int resolution) {
		var _result = _meos_b.h3_sample_step_deg(resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_latlng_deg_to_cell(double lat_deg, double lng_deg, int resolution) {
		var _result = _meos_b.h3_latlng_deg_to_cell(lat_deg, lng_deg, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_cell_to_parent_next_meos(long cell) {
		var _result = _meos_b.h3_cell_to_parent_next_meos(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_cell_to_center_child_next_meos(long cell) {
		var _result = _meos_b.h3_cell_to_center_child_next_meos(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_directed_edge_to_gs_boundary(long edge) {
		var _result = _meos_b.h3_directed_edge_to_gs_boundary(edge);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_vertex_to_gs_point(long vertex) {
		var _result = _meos_b.h3_vertex_to_gs_point(vertex);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer h3_cell_to_local_ij_meos(long origin, long cell) {
		var _result = _meos_b.h3_cell_to_local_ij_meos(origin, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3_local_ij_to_cell_meos(long origin, Pointer coord) {
		var _result = _meos_b.h3_local_ij_to_cell_meos(origin, coord);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int h3_unit_from_cstring(String unit) {
		var _result = _meos_b.h3_unit_from_cstring(unit);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double h3_cell_area_meos(long cell, int unit) {
		var _result = _meos_b.h3_cell_area_meos(cell, unit);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double h3_edge_length_meos(long edge, int unit) {
		var _result = _meos_b.h3_edge_length_meos(edge, unit);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double h3_gs_great_circle_distance_meos(Pointer a, Pointer b, int unit) {
		var _result = _meos_b.h3_gs_great_circle_distance_meos(a, b, unit);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_get_resolution(int d) {
		var _result = _meos_b.datum_h3_get_resolution(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_get_base_cell_number(int d) {
		var _result = _meos_b.datum_h3_get_base_cell_number(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_is_valid_cell(int d) {
		var _result = _meos_b.datum_h3_is_valid_cell(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_is_res_class_iii(int d) {
		var _result = _meos_b.datum_h3_is_res_class_iii(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_is_pentagon(int d) {
		var _result = _meos_b.datum_h3_is_pentagon(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_parent(int cell_d, int res_d) {
		var _result = _meos_b.datum_h3_cell_to_parent(cell_d, res_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_parent_next(int cell_d) {
		var _result = _meos_b.datum_h3_cell_to_parent_next(cell_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_center_child(int cell_d, int res_d) {
		var _result = _meos_b.datum_h3_cell_to_center_child(cell_d, res_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_center_child_next(int cell_d) {
		var _result = _meos_b.datum_h3_cell_to_center_child_next(cell_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_child_pos(int cell_d, int parent_res_d) {
		var _result = _meos_b.datum_h3_cell_to_child_pos(cell_d, parent_res_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_child_pos_to_cell(int pos_d, int parent_d, int child_res_d) {
		var _result = _meos_b.datum_h3_child_pos_to_cell(pos_d, parent_d, child_res_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_are_neighbor_cells(int origin_d, int dest_d) {
		var _result = _meos_b.datum_h3_are_neighbor_cells(origin_d, dest_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cells_to_directed_edge(int origin_d, int dest_d) {
		var _result = _meos_b.datum_h3_cells_to_directed_edge(origin_d, dest_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_is_valid_directed_edge(int d) {
		var _result = _meos_b.datum_h3_is_valid_directed_edge(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_get_directed_edge_origin(int d) {
		var _result = _meos_b.datum_h3_get_directed_edge_origin(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_get_directed_edge_destination(int d) {
		var _result = _meos_b.datum_h3_get_directed_edge_destination(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_directed_edge_to_boundary(int d) {
		var _result = _meos_b.datum_h3_directed_edge_to_boundary(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_vertex(int cell_d, int vnum_d) {
		var _result = _meos_b.datum_h3_cell_to_vertex(cell_d, vnum_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_vertex_to_latlng(int d) {
		var _result = _meos_b.datum_h3_vertex_to_latlng(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_is_valid_vertex(int d) {
		var _result = _meos_b.datum_h3_is_valid_vertex(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_grid_distance(int origin_d, int dest_d) {
		var _result = _meos_b.datum_h3_grid_distance(origin_d, dest_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_local_ij(int origin_d, int cell_d) {
		var _result = _meos_b.datum_h3_cell_to_local_ij(origin_d, cell_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_local_ij_to_cell(int origin_d, int coord_d) {
		var _result = _meos_b.datum_h3_local_ij_to_cell(origin_d, coord_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_latlng_to_cell(int point_d, int res_d) {
		var _result = _meos_b.datum_h3_latlng_to_cell(point_d, res_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_latlng(int d) {
		var _result = _meos_b.datum_h3_cell_to_latlng(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_to_boundary(int d) {
		var _result = _meos_b.datum_h3_cell_to_boundary(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_cell_area(int cell_d, int unit_d) {
		var _result = _meos_b.datum_h3_cell_area(cell_d, unit_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_edge_length(int edge_d, int unit_d) {
		var _result = _meos_b.datum_h3_edge_length(edge_d, unit_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_h3_great_circle_distance(int a_d, int b_d, int unit_d) {
		var _result = _meos_b.datum_h3_great_circle_distance(a_d, b_d, unit_d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_as_ewkb(Pointer gs, String endian, Pointer size) {
		var _result = _meos_b.geo_as_ewkb(gs, endian, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_as_ewkt(Pointer gs, int precision) {
		var _result = _meos_b.geo_as_ewkt(gs, precision);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_as_geojson(Pointer gs, int option, int precision, String srs) {
		var _result = _meos_b.geo_as_geojson(gs, option, precision, srs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_as_hexewkb(Pointer gs, String endian) {
		var _result = _meos_b.geo_as_hexewkb(gs, endian);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_as_text(Pointer gs, int precision) {
		var _result = _meos_b.geo_as_text(gs, precision);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_from_ewkb(Pointer wkb, long wkb_size, int srid) {
		var _result = _meos_b.geo_from_ewkb(wkb, wkb_size, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_from_geojson(String geojson) {
		var _result = _meos_b.geo_from_geojson(geojson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_from_text(String wkt, int srid) {
		var _result = _meos_b.geo_from_text(wkt, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_out(Pointer gs) {
		var _result = _meos_b.geo_out(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_from_binary(String wkb_bytea) {
		var _result = _meos_b.geog_from_binary(wkb_bytea);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_from_hexewkb(String wkt) {
		var _result = _meos_b.geog_from_hexewkb(wkt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_in(String str, int typmod) {
		var _result = _meos_b.geog_in(str, typmod);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_from_hexewkb(String wkt) {
		var _result = _meos_b.geom_from_hexewkb(wkt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_in(String str, int typmod) {
		var _result = _meos_b.geom_in(str, typmod);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer box3d_make(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, int srid) {
		var _result = _meos_b.box3d_make(xmin, xmax, ymin, ymax, zmin, zmax, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String box3d_out(Pointer box, int maxdd) {
		var _result = _meos_b.box3d_out(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer gbox_make(boolean hasz, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
		var _result = _meos_b.gbox_make(hasz, xmin, xmax, ymin, ymax, zmin, zmax);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String gbox_out(Pointer box, int maxdd) {
		var _result = _meos_b.gbox_out(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_copy(Pointer g) {
		var _result = _meos_b.geo_copy(g);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geogpoint_make2d(int srid, double x, double y) {
		var _result = _meos_b.geogpoint_make2d(srid, x, y);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geogpoint_make3dz(int srid, double x, double y, double z) {
		var _result = _meos_b.geogpoint_make3dz(srid, x, y, z);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geompoint_make2d(int srid, double x, double y) {
		var _result = _meos_b.geompoint_make2d(srid, x, y);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geompoint_make3dz(int srid, double x, double y, double z) {
		var _result = _meos_b.geompoint_make3dz(srid, x, y, z);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_to_geog(Pointer geom) {
		var _result = _meos_b.geom_to_geog(geom);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_to_geom(Pointer geog) {
		var _result = _meos_b.geog_to_geom(geog);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geo_is_empty(Pointer g) {
		var _result = _meos_c.geo_is_empty(g);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geo_is_unitary(Pointer gs) {
		var _result = _meos_c.geo_is_unitary(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String geo_typename(int type) {
		var _result = _meos_c.geo_typename(type);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geog_area(Pointer g, boolean use_spheroid) {
		var _result = _meos_c.geog_area(g, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geog_centroid(Pointer g, boolean use_spheroid) {
		var _result = _meos_c.geog_centroid(g, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geog_length(Pointer g, boolean use_spheroid) {
		var _result = _meos_c.geog_length(g, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geog_perimeter(Pointer g, boolean use_spheroid) {
		var _result = _meos_c.geog_perimeter(g, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_azimuth(Pointer gs1, Pointer gs2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.geom_azimuth(gs1, gs2, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static double geom_length(Pointer gs) {
		var _result = _meos_c.geom_length(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geom_perimeter(Pointer gs) {
		var _result = _meos_c.geom_perimeter(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int line_numpoints(Pointer gs) {
		var _result = _meos_c.line_numpoints(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer line_point_n(Pointer geom, int n) {
		var _result = _meos_c.line_point_n(geom, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_reverse(Pointer gs) {
		var _result = _meos_c.geo_reverse(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_round(Pointer gs, int maxdd) {
		var _result = _meos_c.geo_round(gs, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_set_srid(Pointer gs, int srid) {
		var _result = _meos_c.geo_set_srid(gs, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_srid(Pointer gs) {
		var _result = _meos_c.geo_srid(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_transform(Pointer geom, int srid_to) {
		var _result = _meos_c.geo_transform(geom, srid_to);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_transform_pipeline(Pointer gs, String pipeline, int srid_to, boolean is_forward) {
		var _result = _meos_c.geo_transform_pipeline(gs, pipeline, srid_to, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_collect_garray(Pointer gsarr, int count) {
		var _result = _meos_c.geo_collect_garray(gsarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_makeline_garray(Pointer gsarr, int count) {
		var _result = _meos_c.geo_makeline_garray(gsarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_num_points(Pointer gs) {
		var _result = _meos_c.geo_num_points(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_num_geos(Pointer gs) {
		var _result = _meos_c.geo_num_geos(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_geo_n(Pointer geom, int n) {
		var _result = _meos_c.geo_geo_n(geom, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_pointarr(Pointer gs, Pointer count) {
		var _result = _meos_c.geo_pointarr(gs, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_points(Pointer gs) {
		var _result = _meos_c.geo_points(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_array_union(Pointer gsarr, int count) {
		var _result = _meos_c.geom_array_union(gsarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_boundary(Pointer gs) {
		var _result = _meos_c.geom_boundary(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_buffer(Pointer gs, double size, String params) {
		var _result = _meos_c.geom_buffer(gs, size, params);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_centroid(Pointer gs) {
		var _result = _meos_c.geom_centroid(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_convex_hull(Pointer gs) {
		var _result = _meos_c.geom_convex_hull(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_difference2d(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_difference2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_intersection2d(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_intersection2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_intersection2d_coll(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_intersection2d_coll(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_min_bounding_radius(Pointer geom, Pointer radius) {
		var _result = _meos_c.geom_min_bounding_radius(geom, radius);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_shortestline2d(Pointer gs1, Pointer s2) {
		var _result = _meos_c.geom_shortestline2d(gs1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_shortestline3d(Pointer gs1, Pointer s2) {
		var _result = _meos_c.geom_shortestline3d(gs1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_unary_union(Pointer gs, double prec) {
		var _result = _meos_c.geom_unary_union(gs, prec);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer line_interpolate_point(Pointer gs, double distance_fraction, boolean repeat) {
		var _result = _meos_c.line_interpolate_point(gs, distance_fraction, repeat);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double line_locate_point(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.line_locate_point(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer line_substring(Pointer gs, double from, double to) {
		var _result = _meos_c.line_substring(gs, from, to);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geog_dwithin(Pointer g1, Pointer g2, double tolerance, boolean use_spheroid) {
		var _result = _meos_c.geog_dwithin(g1, g2, tolerance, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geog_intersects(Pointer gs1, Pointer gs2, boolean use_spheroid) {
		var _result = _meos_c.geog_intersects(gs1, gs2, use_spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_contains(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_contains(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_covers(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_covers(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_disjoint2d(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_disjoint2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_dwithin2d(Pointer gs1, Pointer gs2, double tolerance) {
		var _result = _meos_c.geom_dwithin2d(gs1, gs2, tolerance);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_dwithin3d(Pointer gs1, Pointer gs2, double tolerance) {
		var _result = _meos_c.geom_dwithin3d(gs1, gs2, tolerance);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_intersects2d(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_intersects2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_intersects3d(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_intersects3d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_relate_pattern(Pointer gs1, Pointer gs2, String patt) {
		var _result = _meos_c.geom_relate_pattern(gs1, gs2, patt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geom_touches(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_touches(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_stboxes(Pointer gs, Pointer count) {
		var _result = _meos_c.geo_stboxes(gs, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_split_each_n_stboxes(Pointer gs, int elem_count, Pointer count) {
		var _result = _meos_c.geo_split_each_n_stboxes(gs, elem_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_split_n_stboxes(Pointer gs, int box_count, Pointer count) {
		var _result = _meos_c.geo_split_n_stboxes(gs, box_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geog_distance(Pointer g1, Pointer g2) {
		var _result = _meos_c.geog_distance(g1, g2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geom_distance2d(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_distance2d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double geom_distance3d(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geom_distance3d(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_equals(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geo_equals(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean geo_same(Pointer gs1, Pointer gs2) {
		var _result = _meos_c.geo_same(gs1, gs2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geogset_in(String str) {
		var _result = _meos_c.geogset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geomset_in(String str) {
		var _result = _meos_c.geomset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String spatialset_as_text(Pointer set, int maxdd) {
		var _result = _meos_c.spatialset_as_text(set, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String spatialset_as_ewkt(Pointer set, int maxdd) {
		var _result = _meos_c.spatialset_as_ewkt(set, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_make(Pointer values, int count) {
		var _result = _meos_c.geoset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_to_set(Pointer gs) {
		var _result = _meos_c.geo_to_set(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_end_value(Pointer s) {
		var _result = _meos_c.geoset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_start_value(Pointer s) {
		var _result = _meos_c.geoset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.geoset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer geoset_values(Pointer s) {
		var _result = _meos_c.geoset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_geo_set(Pointer gs, Pointer s) {
		var _result = _meos_c.contained_geo_set(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_geo(Pointer s, Pointer gs) {
		var _result = _meos_c.contains_set_geo(s, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_union_transfn(Pointer state, Pointer gs) {
		var _result = _meos_c.geo_union_transfn(state, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_geo_set(Pointer gs, Pointer s) {
		var _result = _meos_c.intersection_geo_set(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_geo(Pointer s, Pointer gs) {
		var _result = _meos_c.intersection_set_geo(s, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_geo_set(Pointer gs, Pointer s) {
		var _result = _meos_c.minus_geo_set(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_geo(Pointer s, Pointer gs) {
		var _result = _meos_c.minus_set_geo(s, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_geo_set(Pointer gs, Pointer s) {
		var _result = _meos_c.union_geo_set(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_geo(Pointer s, Pointer gs) {
		var _result = _meos_c.union_set_geo(s, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spatialset_set_srid(Pointer s, int srid) {
		var _result = _meos_c.spatialset_set_srid(s, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spatialset_srid(Pointer s) {
		var _result = _meos_c.spatialset_srid(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spatialset_transform(Pointer s, int srid) {
		var _result = _meos_c.spatialset_transform(s, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spatialset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward) {
		var _result = _meos_c.spatialset_transform_pipeline(s, pipelinestr, srid, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String stbox_as_hexwkb(Pointer box, byte variant, Pointer size) {
		var _result = _meos_c.stbox_as_hexwkb(box, variant, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_as_wkb(Pointer box, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_c.stbox_as_wkb(box, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_from_hexwkb(String hexwkb) {
		var _result = _meos_c.stbox_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_from_wkb(Pointer wkb, long size) {
		var _result = _meos_c.stbox_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_in(String str) {
		var _result = _meos_c.stbox_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String stbox_out(Pointer box, int maxdd) {
		var _result = _meos_c.stbox_out(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_timestamptz_to_stbox(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_c.geo_timestamptz_to_stbox(gs, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s) {
		var _result = _meos_c.geo_tstzspan_to_stbox(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_copy(Pointer box) {
		var _result = _meos_c.stbox_copy(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s) {
		var _result = _meos_c.stbox_make(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_to_stbox(Pointer gs) {
		var _result = _meos_c.geo_to_stbox(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spatialset_to_stbox(Pointer s) {
		var _result = _meos_c.spatialset_to_stbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_to_box3d(Pointer box) {
		var _result = _meos_c.stbox_to_box3d(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_to_gbox(Pointer box) {
		var _result = _meos_c.stbox_to_gbox(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_to_geo(Pointer box) {
		var _result = _meos_c.stbox_to_geo(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_to_tstzspan(Pointer box) {
		var _result = _meos_c.stbox_to_tstzspan(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer timestamptz_to_stbox(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_c.timestamptz_to_stbox(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzset_to_stbox(Pointer s) {
		var _result = _meos_c.tstzset_to_stbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspan_to_stbox(Pointer s) {
		var _result = _meos_c.tstzspan_to_stbox(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tstzspanset_to_stbox(Pointer ss) {
		var _result = _meos_c.tstzspanset_to_stbox(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double stbox_area(Pointer box, boolean spheroid) {
		var _result = _meos_c.stbox_area(box, spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int stbox_hash(Pointer box) {
		var _result = _meos_c.stbox_hash(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long stbox_hash_extended(Pointer box, long seed) {
		var _result = _meos_c.stbox_hash_extended(box, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_hast(Pointer box) {
		var _result = _meos_c.stbox_hast(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_hasx(Pointer box) {
		var _result = _meos_c.stbox_hasx(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_hasz(Pointer box) {
		var _result = _meos_c.stbox_hasz(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_isgeodetic(Pointer box) {
		var _result = _meos_c.stbox_isgeodetic(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double stbox_perimeter(Pointer box, boolean spheroid) {
		var _result = _meos_c.stbox_perimeter(box, spheroid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_tmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.stbox_tmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_tmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = _meos_c.stbox_tmax_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_tmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.stbox_tmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_tmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Byte.BYTES);
		out = _meos_c.stbox_tmin_inc(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static double stbox_volume(Pointer box) {
		var _result = _meos_c.stbox_volume(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.stbox_xmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.stbox_xmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_ymax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.stbox_ymax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_ymin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.stbox_ymin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_zmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.stbox_zmax(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_zmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.stbox_zmin(box, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_expand_space(Pointer box, double d) {
		var _result = _meos_c.stbox_expand_space(box, d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_expand_time(Pointer box, Pointer interv) {
		var _result = _meos_c.stbox_expand_time(box, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_get_space(Pointer box) {
		var _result = _meos_c.stbox_get_space(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_quad_split(Pointer box, Pointer count) {
		var _result = _meos_c.stbox_quad_split(box, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_round(Pointer box, int maxdd) {
		var _result = _meos_c.stbox_round(box, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) {
		var _result = _meos_c.stbox_shift_scale_time(box, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stboxarr_round(Pointer boxarr, int count, int maxdd) {
		var _result = _meos_c.stboxarr_round(boxarr, count, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_set_srid(Pointer box, int srid) {
		var _result = _meos_c.stbox_set_srid(box, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int stbox_srid(Pointer box) {
		var _result = _meos_c.stbox_srid(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_transform(Pointer box, int srid) {
		var _result = _meos_c.stbox_transform(box, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward) {
		var _result = _meos_c.stbox_transform_pipeline(box, pipelinestr, srid, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.adjacent_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.contained_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.contains_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overlaps_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.same_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean above_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.above_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.after_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean back_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.back_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.before_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean below_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.below_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean front_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.front_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.left_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overabove_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overabove_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overafter_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overback_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overback_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overbefore_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbelow_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overbelow_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overfront_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overfront_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overleft_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.overright_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.right_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict) {
		var _result = _meos_c.union_stbox_stbox(box1, box2, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.intersection_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int stbox_cmp(Pointer box1, Pointer box2) {
		var _result = _meos_c.stbox_cmp(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_eq(Pointer box1, Pointer box2) {
		var _result = _meos_c.stbox_eq(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_ge(Pointer box1, Pointer box2) {
		var _result = _meos_c.stbox_ge(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_gt(Pointer box1, Pointer box2) {
		var _result = _meos_c.stbox_gt(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_le(Pointer box1, Pointer box2) {
		var _result = _meos_c.stbox_le(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_lt(Pointer box1, Pointer box2) {
		var _result = _meos_c.stbox_lt(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean stbox_ne(Pointer box1, Pointer box2) {
		var _result = _meos_c.stbox_ne(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_from_mfjson(String str) {
		var _result = _meos_c.tgeogpoint_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_in(String str) {
		var _result = _meos_c.tgeogpoint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeography_from_mfjson(String mfjson) {
		var _result = _meos_c.tgeography_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeography_in(String str) {
		var _result = _meos_c.tgeography_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_from_mfjson(String str) {
		var _result = _meos_c.tgeometry_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_in(String str) {
		var _result = _meos_c.tgeometry_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompoint_from_mfjson(String str) {
		var _result = _meos_c.tgeompoint_from_mfjson(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompoint_in(String str) {
		var _result = _meos_c.tgeompoint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tspatial_as_ewkt(Pointer temp, int maxdd) {
		var _result = _meos_c.tspatial_as_ewkt(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tspatial_as_text(Pointer temp, int maxdd) {
		var _result = _meos_c.tspatial_as_text(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tspatial_out(Pointer temp, int maxdd) {
		var _result = _meos_c.tspatial_out(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_from_base_temp(Pointer gs, Pointer temp) {
		var _result = _meos_c.tgeo_from_base_temp(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoinst_make(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_c.tgeoinst_make(gs, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseq_from_base_tstzset(Pointer gs, Pointer s) {
		var _result = _meos_c.tgeoseq_from_base_tstzset(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseq_from_base_tstzspan(Pointer gs, Pointer s, int interp) {
		var _result = _meos_c.tgeoseq_from_base_tstzspan(gs, s, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp) {
		var _result = _meos_c.tgeoseqset_from_base_tstzspanset(gs, ss, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_from_base_temp(Pointer gs, Pointer temp) {
		var _result = _meos_c.tpoint_from_base_temp(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointinst_make(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_c.tpointinst_make(gs, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s) {
		var _result = _meos_c.tpointseq_from_base_tstzset(gs, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp) {
		var _result = _meos_c.tpointseq_from_base_tstzspan(gs, s, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_make_coords(Pointer xcoords, Pointer ycoords, Pointer zcoords, Pointer times, int count, int srid, boolean geodetic, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		var _result = _meos_c.tpointseq_make_coords(xcoords, ycoords, zcoords, times, count, srid, geodetic, lower_inc, upper_inc, interp, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp) {
		var _result = _meos_c.tpointseqset_from_base_tstzspanset(gs, ss, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer box3d_to_stbox(Pointer box) {
		var _result = _meos_c.box3d_to_stbox(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer gbox_to_stbox(Pointer box) {
		var _result = _meos_c.gbox_to_stbox(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geomeas_to_tpoint(Pointer gs) {
		var _result = _meos_c.geomeas_to_tpoint(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_to_tgeography(Pointer temp) {
		var _result = _meos_c.tgeogpoint_to_tgeography(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeography_to_tgeogpoint(Pointer temp) {
		var _result = _meos_c.tgeography_to_tgeogpoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeography_to_tgeometry(Pointer temp) {
		var _result = _meos_c.tgeography_to_tgeometry(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_to_tgeography(Pointer temp) {
		var _result = _meos_c.tgeometry_to_tgeography(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_to_tgeompoint(Pointer temp) {
		var _result = _meos_c.tgeometry_to_tgeompoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompoint_to_tgeometry(Pointer temp) {
		var _result = _meos_c.tgeompoint_to_tgeometry(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tpoint_as_mvtgeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count) {
		var _result = _meos_c.tpoint_as_mvtgeom(temp, bounds, extent, buffer, clip_geom, gsarr, timesarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tpoint_tfloat_to_geomeas(tpoint, measure, segmentize, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_to_stbox(Pointer temp) {
		var _result = _meos_c.tspatial_to_stbox(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bearing_point_point(Pointer gs1, Pointer gs2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.bearing_point_point(gs1, gs2, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert) {
		var _result = _meos_c.bearing_tpoint_point(temp, gs, invert);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.bearing_tpoint_tpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_centroid(Pointer temp) {
		var _result = _meos_c.tgeo_centroid(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_convex_hull(Pointer temp) {
		var _result = _meos_c.tgeo_convex_hull(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_end_value(Pointer temp) {
		var _result = _meos_c.tgeo_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_start_value(Pointer temp) {
		var _result = _meos_c.tgeo_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_traversed_area(Pointer temp, boolean unary_union) {
		var _result = _meos_c.tgeo_traversed_area(temp, unary_union);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tgeo_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		var _result = _meos_c.tgeo_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.tgeo_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_values(Pointer temp, Pointer count) {
		var _result = _meos_c.tgeo_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_angular_difference(Pointer temp) {
		var _result = _meos_c.tpoint_angular_difference(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_azimuth(Pointer temp) {
		var _result = _meos_c.tpoint_azimuth(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_cumulative_length(Pointer temp) {
		var _result = _meos_c.tpoint_cumulative_length(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_direction(Pointer temp) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Double.BYTES);
		out = _meos_c.tpoint_direction(temp, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_get_x(Pointer temp) {
		var _result = _meos_c.tpoint_get_x(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_get_y(Pointer temp) {
		var _result = _meos_c.tpoint_get_y(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_get_z(Pointer temp) {
		var _result = _meos_c.tpoint_get_z(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tpoint_is_simple(Pointer temp) {
		var _result = _meos_c.tpoint_is_simple(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tpoint_length(Pointer temp) {
		var _result = _meos_c.tpoint_length(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_speed(Pointer temp) {
		var _result = _meos_c.tpoint_speed(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_trajectory(Pointer temp, boolean unary_union) {
		var _result = _meos_c.tpoint_trajectory(temp, unary_union);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_twcentroid(Pointer temp) {
		var _result = _meos_c.tpoint_twcentroid(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_affine(Pointer temp, Pointer a) {
		var _result = _meos_c.tgeo_affine(temp, a);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_scale(Pointer temp, Pointer scale, Pointer sorigin) {
		var _result = _meos_c.tgeo_scale(temp, scale, sorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_make_simple(Pointer temp, Pointer count) {
		var _result = _meos_c.tpoint_make_simple(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tspatial_srid(Pointer temp) {
		var _result = _meos_c.tspatial_srid(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_set_srid(Pointer temp, int srid) {
		var _result = _meos_c.tspatial_set_srid(temp, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_transform(Pointer temp, int srid) {
		var _result = _meos_c.tspatial_transform(temp, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward) {
		var _result = _meos_c.tspatial_transform_pipeline(temp, pipelinestr, srid, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_at_geom(Pointer temp, Pointer gs) {
		var _result = _meos_c.tgeo_at_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_at_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = _meos_c.tgeo_at_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_at_value(Pointer temp, Pointer gs) {
		var _result = _meos_c.tgeo_at_value(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_minus_geom(Pointer temp, Pointer gs) {
		var _result = _meos_c.tgeo_minus_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_minus_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = _meos_c.tgeo_minus_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_minus_value(Pointer temp, Pointer gs) {
		var _result = _meos_c.tgeo_minus_value(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_at_elevation(Pointer temp, Pointer s) {
		var _result = _meos_c.tpoint_at_elevation(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_at_geom(Pointer temp, Pointer gs) {
		var _result = _meos_c.tpoint_at_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_at_value(Pointer temp, Pointer gs) {
		var _result = _meos_c.tpoint_at_value(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_elevation(Pointer temp, Pointer s) {
		var _result = _meos_c.tpoint_minus_elevation(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_geom(Pointer temp, Pointer gs) {
		var _result = _meos_c.tpoint_minus_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_value(Pointer temp, Pointer gs) {
		var _result = _meos_c.tpoint_minus_value(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.always_eq_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.always_eq_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.always_eq_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.always_ne_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.always_ne_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.always_ne_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.ever_eq_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.ever_eq_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ever_eq_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.ever_ne_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.ever_ne_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ever_ne_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.teq_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.teq_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.tne_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tne_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_stboxes(Pointer temp, Pointer count) {
		var _result = _meos_c.tgeo_stboxes(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_space_boxes(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer count) {
		var _result = _meos_c.tgeo_space_boxes(temp, xsize, ysize, zsize, sorigin, bitmatrix, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_space_time_boxes(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean bitmatrix, boolean border_inc, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_c.tgeo_space_time_boxes(temp, xsize, ysize, zsize, duration, sorigin, torigin_new, bitmatrix, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_split_each_n_stboxes(Pointer temp, int elem_count, Pointer count) {
		var _result = _meos_c.tgeo_split_each_n_stboxes(temp, elem_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_split_n_stboxes(Pointer temp, int box_count, Pointer count) {
		var _result = _meos_c.tgeo_split_n_stboxes(temp, box_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.adjacent_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.adjacent_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.adjacent_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.contained_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.contained_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.contained_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.contains_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.contains_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.contains_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overlaps_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overlaps_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overlaps_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overlaps_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.same_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.same_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean same_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.same_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean above_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.above_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean above_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.above_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean above_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.above_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.after_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.after_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean after_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.after_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean back_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.back_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean back_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.back_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean back_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.back_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.before_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.before_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean before_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.before_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean below_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.below_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean below_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.below_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean below_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.below_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean front_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.front_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean front_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.front_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean front_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.front_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.left_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.left_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.left_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overabove_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overabove_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overabove_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overabove_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overabove_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overabove_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overafter_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overafter_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overafter_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overafter_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overback_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overback_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overback_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overback_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overback_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overback_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overbefore_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overbefore_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbefore_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overbefore_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbelow_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overbelow_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbelow_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overbelow_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overbelow_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overbelow_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overfront_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overfront_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overfront_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overfront_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overfront_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overfront_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overleft_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overleft_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overleft_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.overright_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.overright_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.overright_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_stbox_tspatial(Pointer box, Pointer temp) {
		var _result = _meos_c.right_stbox_tspatial(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tspatial_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.right_tspatial_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.right_tspatial_tspatial(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.acontains_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.acontains_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.acontains_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adisjoint_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.adisjoint_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.adisjoint_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adwithin_tgeo_geo(Pointer temp, Pointer gs, double dist) {
		var _result = _meos_c.adwithin_tgeo_geo(temp, gs, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist) {
		var _result = _meos_c.adwithin_tgeo_tgeo(temp1, temp2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int aintersects_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.aintersects_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int aintersects_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.aintersects_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.atouches_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.atouches_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tpoint_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.atouches_tpoint_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.econtains_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.econtains_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.econtains_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.ecovers_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.ecovers_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ecovers_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edisjoint_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.edisjoint_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.edisjoint_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edwithin_tgeo_geo(Pointer temp, Pointer gs, double dist) {
		var _result = _meos_c.edwithin_tgeo_geo(temp, gs, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist) {
		var _result = _meos_c.edwithin_tgeo_tgeo(temp1, temp2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int eintersects_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.eintersects_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int eintersects_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.eintersects_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.etouches_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.etouches_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tpoint_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.etouches_tpoint_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontains_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.tcontains_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontains_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tcontains_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontains_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tcontains_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcovers_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.tcovers_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcovers_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tcovers_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcovers_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tcovers_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdisjoint_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.tdisjoint_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tdisjoint_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tdisjoint_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdwithin_geo_tgeo(Pointer gs, Pointer temp, double dist) {
		var _result = _meos_c.tdwithin_geo_tgeo(gs, temp, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdwithin_tgeo_geo(Pointer temp, Pointer gs, double dist) {
		var _result = _meos_c.tdwithin_tgeo_geo(temp, gs, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist) {
		var _result = _meos_c.tdwithin_tgeo_tgeo(temp1, temp2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintersects_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.tintersects_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintersects_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tintersects_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintersects_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tintersects_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttouches_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.ttouches_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttouches_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.ttouches_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttouches_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ttouches_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tdistance_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tdistance_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_stbox_geo(Pointer box, Pointer gs) {
		var _result = _meos_c.nad_stbox_geo(box, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_stbox_stbox(Pointer box1, Pointer box2) {
		var _result = _meos_c.nad_stbox_stbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.nad_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tgeo_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.nad_tgeo_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.nad_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.nai_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.nai_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.shortestline_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.shortestline_tgeo_tgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tgeoarr_tgeoarr_mindist(Pointer arr1, int count1, Pointer arr2, int count2) {
		var _result = _meos_c.tgeoarr_tgeoarr_mindist(arr1, count1, arr2, count2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double mindistance_tgeo_tgeo(Pointer temp1, Pointer temp2, double threshold) {
		var _result = _meos_c.mindistance_tgeo_tgeo(temp1, temp2, threshold);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_tcentroid_finalfn(Pointer state) {
		var _result = _meos_c.tpoint_tcentroid_finalfn(state);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp) {
		var _result = _meos_c.tpoint_tcentroid_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tspatial_extent_transfn(Pointer box, Pointer temp) {
		var _result = _meos_c.tspatial_extent_transfn(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_get_space_tile(Pointer point, double xsize, double ysize, double zsize, Pointer sorigin) {
		var _result = _meos_c.stbox_get_space_tile(point, xsize, ysize, zsize, sorigin);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_get_space_time_tile(Pointer point, OffsetDateTime t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_c.stbox_get_space_time_tile(point, t_new, xsize, ysize, zsize, duration, sorigin, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_get_time_tile(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_c.stbox_get_time_tile(t_new, duration, torigin_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_space_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer sorigin, boolean border_inc, Pointer count) {
		var _result = _meos_c.stbox_space_tiles(bounds, xsize, ysize, zsize, sorigin, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_space_time_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean border_inc, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_c.stbox_space_time_tiles(bounds, xsize, ysize, zsize, duration, sorigin, torigin_new, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_time_tiles(Pointer bounds, Pointer duration, OffsetDateTime torigin, boolean border_inc, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_c.stbox_time_tiles(bounds, duration, torigin_new, border_inc, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_space_split(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer count) {
		var _result = _meos_c.tgeo_space_split(temp, xsize, ysize, zsize, sorigin, bitmatrix, border_inc, space_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_space_time_split(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_c.tgeo_space_time_split(temp, xsize, ysize, zsize, duration, sorigin, torigin_new, bitmatrix, border_inc, space_bins, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_cluster_kmeans(Pointer geoms, int ngeoms, int k) {
		var _result = _meos_c.geo_cluster_kmeans(geoms, ngeoms, k);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_cluster_dbscan(Pointer geoms, int ngeoms, double tolerance, int minpoints, Pointer count) {
		var _result = _meos_c.geo_cluster_dbscan(geoms, ngeoms, tolerance, minpoints, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_cluster_intersecting(Pointer geoms, int ngeoms, Pointer count) {
		var _result = _meos_c.geo_cluster_intersecting(geoms, ngeoms, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_cluster_within(Pointer geoms, int ngeoms, double tolerance, Pointer count) {
		var _result = _meos_c.geo_cluster_within(geoms, ngeoms, tolerance, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acovers_geo_tgeo(Pointer gs, Pointer temp) {
		var _result = _meos_c.acovers_geo_tgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acovers_tgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.acovers_tgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String cbuffer_as_ewkt(Pointer cb, int maxdd) {
		var _result = _meos_c.cbuffer_as_ewkt(cb, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String cbuffer_as_hexwkb(Pointer cb, byte variant, Pointer size) {
		var _result = _meos_c.cbuffer_as_hexwkb(cb, variant, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String cbuffer_as_text(Pointer cb, int maxdd) {
		var _result = _meos_c.cbuffer_as_text(cb, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_as_wkb(Pointer cb, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_c.cbuffer_as_wkb(cb, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_from_hexwkb(String hexwkb) {
		var _result = _meos_c.cbuffer_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_from_wkb(Pointer wkb, long size) {
		var _result = _meos_c.cbuffer_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_in(String str) {
		var _result = _meos_c.cbuffer_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String cbuffer_out(Pointer cb, int maxdd) {
		var _result = _meos_c.cbuffer_out(cb, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_copy(Pointer cb) {
		var _result = _meos_c.cbuffer_copy(cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_make(Pointer point, double radius) {
		var _result = _meos_c.cbuffer_make(point, radius);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_to_geom(Pointer cb) {
		var _result = _meos_c.cbuffer_to_geom(cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_to_stbox(Pointer cb) {
		var _result = _meos_c.cbuffer_to_stbox(cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbufferarr_to_geom(Pointer cbarr, int count) {
		var _result = _meos_c.cbufferarr_to_geom(cbarr, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_to_cbuffer(Pointer gs) {
		var _result = _meos_c.geom_to_cbuffer(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int cbuffer_hash(Pointer cb) {
		var _result = _meos_c.cbuffer_hash(cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long cbuffer_hash_extended(Pointer cb, long seed) {
		var _result = _meos_c.cbuffer_hash_extended(cb, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_point(Pointer cb) {
		var _result = _meos_c.cbuffer_point(cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double cbuffer_radius(Pointer cb) {
		var _result = _meos_c.cbuffer_radius(cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_round(Pointer cb, int maxdd) {
		var _result = _meos_c.cbuffer_round(cb, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbufferarr_round(Pointer cbarr, int count, int maxdd) {
		var _result = _meos_c.cbufferarr_round(cbarr, count, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void cbuffer_set_srid(Pointer cb, int srid) {
		_meos_c.cbuffer_set_srid(cb, srid);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static int cbuffer_srid(Pointer cb) {
		var _result = _meos_c.cbuffer_srid(cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_transform(Pointer cb, int srid) {
		var _result = _meos_c.cbuffer_transform(cb, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_transform_pipeline(Pointer cb, String pipelinestr, int srid, boolean is_forward) {
		var _result = _meos_c.cbuffer_transform_pipeline(cb, pipelinestr, srid, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int contains_cbuffer_cbuffer(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.contains_cbuffer_cbuffer(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int covers_cbuffer_cbuffer(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.covers_cbuffer_cbuffer(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int disjoint_cbuffer_cbuffer(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.disjoint_cbuffer_cbuffer(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int dwithin_cbuffer_cbuffer(Pointer cb1, Pointer cb2, double dist) {
		var _result = _meos_c.dwithin_cbuffer_cbuffer(cb1, cb2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int intersects_cbuffer_cbuffer(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.intersects_cbuffer_cbuffer(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int touches_cbuffer_cbuffer(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.touches_cbuffer_cbuffer(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_tstzspan_to_stbox(Pointer cb, Pointer s) {
		var _result = _meos_c.cbuffer_tstzspan_to_stbox(cb, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_timestamptz_to_stbox(Pointer cb, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_c.cbuffer_timestamptz_to_stbox(cb, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_cbuffer_cbuffer(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.distance_cbuffer_cbuffer(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_cbuffer_geo(Pointer cb, Pointer gs) {
		var _result = _meos_c.distance_cbuffer_geo(cb, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_cbuffer_stbox(Pointer cb, Pointer box) {
		var _result = _meos_c.distance_cbuffer_stbox(cb, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_cbuffer_stbox(Pointer cb, Pointer box) {
		var _result = _meos_c.nad_cbuffer_stbox(cb, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int cbuffer_cmp(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_cmp(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean cbuffer_eq(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_eq(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean cbuffer_ge(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_ge(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean cbuffer_gt(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_gt(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean cbuffer_le(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_le(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean cbuffer_lt(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_lt(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean cbuffer_ne(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_ne(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean cbuffer_nsame(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_nsame(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean cbuffer_same(Pointer cb1, Pointer cb2) {
		var _result = _meos_c.cbuffer_same(cb1, cb2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbufferset_in(String str) {
		var _result = _meos_c.cbufferset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String cbufferset_out(Pointer s, int maxdd) {
		var _result = _meos_c.cbufferset_out(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbufferset_make(Pointer values, int count) {
		var _result = _meos_c.cbufferset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_to_set(Pointer cb) {
		var _result = _meos_c.cbuffer_to_set(cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbufferset_end_value(Pointer s) {
		var _result = _meos_c.cbufferset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbufferset_start_value(Pointer s) {
		var _result = _meos_c.cbufferset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbufferset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.cbufferset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer cbufferset_values(Pointer s) {
		var _result = _meos_c.cbufferset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer cbuffer_union_transfn(Pointer state, Pointer cb) {
		var _result = _meos_c.cbuffer_union_transfn(state, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_cbuffer_set(Pointer cb, Pointer s) {
		var _result = _meos_c.contained_cbuffer_set(cb, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_cbuffer(Pointer s, Pointer cb) {
		var _result = _meos_c.contains_set_cbuffer(s, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_cbuffer_set(Pointer cb, Pointer s) {
		var _result = _meos_c.intersection_cbuffer_set(cb, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_cbuffer(Pointer s, Pointer cb) {
		var _result = _meos_c.intersection_set_cbuffer(s, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_cbuffer_set(Pointer cb, Pointer s) {
		var _result = _meos_c.minus_cbuffer_set(cb, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_cbuffer(Pointer s, Pointer cb) {
		var _result = _meos_c.minus_set_cbuffer(s, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_cbuffer_set(Pointer cb, Pointer s) {
		var _result = _meos_c.union_cbuffer_set(cb, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_cbuffer(Pointer s, Pointer cb) {
		var _result = _meos_c.union_set_cbuffer(s, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_in(String str) {
		var _result = _meos_c.tcbuffer_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_from_mfjson(String mfjson) {
		var _result = _meos_c.tcbuffer_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_make(Pointer tpoint, Pointer tfloat) {
		var _result = _meos_c.tcbuffer_make(tpoint, tfloat);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_points(Pointer temp) {
		var _result = _meos_c.tcbuffer_points(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_radius(Pointer temp) {
		var _result = _meos_c.tcbuffer_radius(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_trav_area(Pointer temp, boolean merge_union) {
		var _result = _meos_c.tcbuffer_trav_area(temp, merge_union);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_to_tfloat(Pointer temp) {
		var _result = _meos_c.tcbuffer_to_tfloat(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_to_tgeompoint(Pointer temp) {
		var _result = _meos_c.tcbuffer_to_tgeompoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometry_to_tcbuffer(Pointer temp) {
		var _result = _meos_c.tgeometry_to_tcbuffer(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_expand(Pointer temp, double dist) {
		var _result = _meos_c.tcbuffer_expand(temp, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_at_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.tcbuffer_at_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_at_geom(Pointer temp, Pointer gs) {
		var _result = _meos_c.tcbuffer_at_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_at_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = _meos_c.tcbuffer_at_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_minus_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.tcbuffer_minus_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_minus_geom(Pointer temp, Pointer gs) {
		var _result = _meos_c.tcbuffer_minus_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcbuffer_minus_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = _meos_c.tcbuffer_minus_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.tdistance_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tdistance_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tdistance_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.nad_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.nad_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tcbuffer_stbox(Pointer temp, Pointer box) {
		var _result = _meos_c.nad_tcbuffer_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.nad_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.nai_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.nai_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.nai_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.shortestline_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.shortestline_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.shortestline_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.always_eq_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.always_eq_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.always_eq_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.always_ne_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.always_ne_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.always_ne_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.ever_eq_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.ever_eq_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ever_eq_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.ever_ne_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.ever_ne_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ever_ne_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.teq_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.teq_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.tne_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.tne_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.acontains_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_geo_tcbuffer(Pointer gs, Pointer temp) {
		var _result = _meos_c.acontains_geo_tcbuffer(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.acontains_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acontains_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.acontains_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acovers_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.acovers_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acovers_geo_tcbuffer(Pointer gs, Pointer temp) {
		var _result = _meos_c.acovers_geo_tcbuffer(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acovers_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.acovers_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acovers_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.acovers_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adisjoint_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.adisjoint_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adisjoint_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.adisjoint_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adisjoint_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.adisjoint_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adwithin_tcbuffer_geo(Pointer temp, Pointer gs, double dist) {
		var _result = _meos_c.adwithin_tcbuffer_geo(temp, gs, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adwithin_tcbuffer_cbuffer(Pointer temp, Pointer cb, double dist) {
		var _result = _meos_c.adwithin_tcbuffer_cbuffer(temp, cb, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int adwithin_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2, double dist) {
		var _result = _meos_c.adwithin_tcbuffer_tcbuffer(temp1, temp2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int aintersects_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.aintersects_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int aintersects_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.aintersects_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int aintersects_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.aintersects_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.atouches_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.atouches_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int atouches_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.atouches_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.econtains_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.econtains_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int econtains_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.econtains_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.ecovers_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.ecovers_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.ecovers_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ecovers_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ecovers_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edisjoint_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.edisjoint_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edisjoint_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.edisjoint_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edwithin_tcbuffer_geo(Pointer temp, Pointer gs, double dist) {
		var _result = _meos_c.edwithin_tcbuffer_geo(temp, gs, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edwithin_tcbuffer_cbuffer(Pointer temp, Pointer cb, double dist) {
		var _result = _meos_c.edwithin_tcbuffer_cbuffer(temp, cb, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int edwithin_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2, double dist) {
		var _result = _meos_c.edwithin_tcbuffer_tcbuffer(temp1, temp2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int eintersects_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.eintersects_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int eintersects_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.eintersects_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int eintersects_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.eintersects_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.etouches_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.etouches_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int etouches_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.etouches_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontains_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.tcontains_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontains_geo_tcbuffer(Pointer gs, Pointer temp) {
		var _result = _meos_c.tcontains_geo_tcbuffer(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontains_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tcontains_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontains_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.tcontains_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontains_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tcontains_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcovers_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.tcovers_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcovers_geo_tcbuffer(Pointer gs, Pointer temp) {
		var _result = _meos_c.tcovers_geo_tcbuffer(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcovers_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tcovers_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcovers_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.tcovers_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcovers_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tcovers_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdwithin_geo_tcbuffer(Pointer gs, Pointer temp, double dist) {
		var _result = _meos_c.tdwithin_geo_tcbuffer(gs, temp, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdwithin_tcbuffer_geo(Pointer temp, Pointer gs, double dist) {
		var _result = _meos_c.tdwithin_tcbuffer_geo(temp, gs, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdwithin_tcbuffer_cbuffer(Pointer temp, Pointer cb, double dist) {
		var _result = _meos_c.tdwithin_tcbuffer_cbuffer(temp, cb, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdwithin_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2, double dist) {
		var _result = _meos_c.tdwithin_tcbuffer_tcbuffer(temp1, temp2, dist);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdisjoint_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.tdisjoint_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdisjoint_geo_tcbuffer(Pointer gs, Pointer temp) {
		var _result = _meos_c.tdisjoint_geo_tcbuffer(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tdisjoint_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.tdisjoint_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tdisjoint_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintersects_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.tintersects_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintersects_geo_tcbuffer(Pointer gs, Pointer temp) {
		var _result = _meos_c.tintersects_geo_tcbuffer(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintersects_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.tintersects_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintersects_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.tintersects_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintersects_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tintersects_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttouches_geo_tcbuffer(Pointer gs, Pointer temp) {
		var _result = _meos_c.ttouches_geo_tcbuffer(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttouches_tcbuffer_geo(Pointer temp, Pointer gs) {
		var _result = _meos_c.ttouches_tcbuffer_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttouches_cbuffer_tcbuffer(Pointer cb, Pointer temp) {
		var _result = _meos_c.ttouches_cbuffer_tcbuffer(cb, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttouches_tcbuffer_cbuffer(Pointer temp, Pointer cb) {
		var _result = _meos_c.ttouches_tcbuffer_cbuffer(temp, cb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttouches_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ttouches_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int acovers_tcbuffer_tcbuffer(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.acovers_tcbuffer_tcbuffer(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long h3index_in(String str) {
		var _result = _meos_c.h3index_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String h3index_out(long cell) {
		var _result = _meos_c.h3index_out(cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_in(String str) {
		var _result = _meos_c.th3index_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3indexinst_in(String str) {
		var _result = _meos_c.th3indexinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3indexseq_in(String str, int interp) {
		var _result = _meos_c.th3indexseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3indexseqset_in(String str) {
		var _result = _meos_c.th3indexseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_make(long value, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_c.th3index_make(value, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3indexinst_make(long value, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_c.th3indexinst_make(value, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3indexseq_make(Pointer values, Pointer times, int count, boolean lower_inc, boolean upper_inc) {
		var _result = _meos_c.th3indexseq_make(values, times, count, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3indexseqset_make(Pointer sequences, int count) {
		var _result = _meos_c.th3indexseqset_make(sequences, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long th3index_start_value(Pointer temp) {
		var _result = _meos_c.th3index_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long th3index_end_value(Pointer temp) {
		var _result = _meos_c.th3index_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.th3index_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_values(Pointer temp, Pointer count) {
		var _result = _meos_c.th3index_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		var t_new = t.toEpochSecond();
		out = _meos_c.th3index_value_at_timestamptz(temp, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tbigint_to_th3index(Pointer temp) {
		var _result = _meos_c.tbigint_to_th3index(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_to_tbigint(Pointer temp) {
		var _result = _meos_c.th3index_to_tbigint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_h3index_th3index(long cell, Pointer temp) {
		var _result = _meos_c.ever_eq_h3index_th3index(cell, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_th3index_h3index(Pointer temp, long cell) {
		var _result = _meos_c.ever_eq_th3index_h3index(temp, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_h3index_th3index(long cell, Pointer temp) {
		var _result = _meos_c.ever_ne_h3index_th3index(cell, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_th3index_h3index(Pointer temp, long cell) {
		var _result = _meos_c.ever_ne_th3index_h3index(temp, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_h3index_th3index(long cell, Pointer temp) {
		var _result = _meos_c.always_eq_h3index_th3index(cell, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_th3index_h3index(Pointer temp, long cell) {
		var _result = _meos_c.always_eq_th3index_h3index(temp, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_h3index_th3index(long cell, Pointer temp) {
		var _result = _meos_c.always_ne_h3index_th3index(cell, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_th3index_h3index(Pointer temp, long cell) {
		var _result = _meos_c.always_ne_th3index_h3index(temp, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_th3index_th3index(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ever_eq_th3index_th3index(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_th3index_th3index(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.ever_ne_th3index_th3index(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_th3index_th3index(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.always_eq_th3index_th3index(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_th3index_th3index(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.always_ne_th3index_th3index(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_h3index_th3index(long cell, Pointer temp) {
		var _result = _meos_c.teq_h3index_th3index(cell, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_th3index_h3index(Pointer temp, long cell) {
		var _result = _meos_c.teq_th3index_h3index(temp, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_th3index_th3index(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.teq_th3index_th3index(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_h3index_th3index(long cell, Pointer temp) {
		var _result = _meos_c.tne_h3index_th3index(cell, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_th3index_h3index(Pointer temp, long cell) {
		var _result = _meos_c.tne_th3index_h3index(temp, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_th3index_th3index(Pointer temp1, Pointer temp2) {
		var _result = _meos_c.tne_th3index_th3index(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_get_resolution(Pointer temp) {
		var _result = _meos_c.th3index_get_resolution(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_get_base_cell_number(Pointer temp) {
		var _result = _meos_c.th3index_get_base_cell_number(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_is_valid_cell(Pointer temp) {
		var _result = _meos_c.th3index_is_valid_cell(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_is_res_class_iii(Pointer temp) {
		var _result = _meos_c.th3index_is_res_class_iii(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_is_pentagon(Pointer temp) {
		var _result = _meos_c.th3index_is_pentagon(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_to_parent(Pointer temp, int resolution) {
		var _result = _meos_c.th3index_cell_to_parent(temp, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_to_parent_next(Pointer temp) {
		var _result = _meos_c.th3index_cell_to_parent_next(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_to_center_child(Pointer temp, int resolution) {
		var _result = _meos_c.th3index_cell_to_center_child(temp, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_to_center_child_next(Pointer temp) {
		var _result = _meos_c.th3index_cell_to_center_child_next(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_to_child_pos(Pointer temp, int parent_res) {
		var _result = _meos_c.th3index_cell_to_child_pos(temp, parent_res);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_child_pos_to_cell(Pointer child_pos, Pointer parent, int child_res) {
		var _result = _meos_c.th3index_child_pos_to_cell(child_pos, parent, child_res);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_to_th3index(Pointer temp, int resolution) {
		var _result = _meos_c.tgeogpoint_to_th3index(temp, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompoint_to_th3index(Pointer temp, int resolution) {
		var _result = _meos_c.tgeompoint_to_th3index(temp, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_to_tgeogpoint(Pointer temp) {
		var _result = _meos_c.th3index_to_tgeogpoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_to_tgeompoint(Pointer temp) {
		var _result = _meos_c.th3index_to_tgeompoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_to_boundary(Pointer temp) {
		var _result = _meos_c.th3index_cell_to_boundary(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_to_h3index_set(Pointer gs, int resolution) {
		var _result = _meos_c.geo_to_h3index_set(gs, resolution);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_h3indexset_th3index(Pointer cells, Pointer th3idx) {
		var _result = _meos_c.ever_eq_h3indexset_th3index(cells, th3idx);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_are_neighbor_cells(Pointer origin, Pointer dest) {
		var _result = _meos_c.th3index_are_neighbor_cells(origin, dest);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cells_to_directed_edge(Pointer origin, Pointer dest) {
		var _result = _meos_c.th3index_cells_to_directed_edge(origin, dest);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_is_valid_directed_edge(Pointer edge) {
		var _result = _meos_c.th3index_is_valid_directed_edge(edge);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_get_directed_edge_origin(Pointer edge) {
		var _result = _meos_c.th3index_get_directed_edge_origin(edge);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_get_directed_edge_destination(Pointer edge) {
		var _result = _meos_c.th3index_get_directed_edge_destination(edge);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_directed_edge_to_boundary(Pointer edge) {
		var _result = _meos_c.th3index_directed_edge_to_boundary(edge);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_to_vertex(Pointer temp, int vertex_num) {
		var _result = _meos_c.th3index_cell_to_vertex(temp, vertex_num);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_vertex_to_latlng(Pointer temp) {
		var _result = _meos_c.th3index_vertex_to_latlng(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_is_valid_vertex(Pointer temp) {
		var _result = _meos_c.th3index_is_valid_vertex(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_grid_distance(Pointer origin, Pointer dest) {
		var _result = _meos_c.th3index_grid_distance(origin, dest);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_to_local_ij(Pointer origin, Pointer cell) {
		var _result = _meos_c.th3index_cell_to_local_ij(origin, cell);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_local_ij_to_cell(Pointer origin, Pointer coord) {
		var _result = _meos_c.th3index_local_ij_to_cell(origin, coord);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_cell_area(Pointer temp, String unit) {
		var _result = _meos_c.th3index_cell_area(temp, unit);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer th3index_edge_length(Pointer temp, String unit) {
		var _result = _meos_c.th3index_edge_length(temp, unit);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_great_circle_distance(Pointer a, Pointer b, String unit) {
		var _result = _meos_c.tgeogpoint_great_circle_distance(a, b, unit);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer gsl_get_generation_rng() {
		var _result = _meos_c.gsl_get_generation_rng();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer gsl_get_aggregation_rng() {
		var _result = _meos_c.gsl_get_aggregation_rng();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_ceil(Pointer d) {
		var _result = _meos_c.datum_ceil(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_degrees(Pointer d, Pointer normalize) {
		var _result = _meos_c.datum_degrees(d, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_float_round(Pointer value, Pointer size) {
		var _result = _meos_c.datum_float_round(value, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_floor(Pointer d) {
		var _result = _meos_c.datum_floor(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_hash(Pointer d, int basetype) {
		var _result = _meos_c.datum_hash(d, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long datum_hash_extended(Pointer d, int basetype, long seed) {
		var _result = _meos_c.datum_hash_extended(d, basetype, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_radians(Pointer d) {
		var _result = _meos_c.datum_radians(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void floatspan_round_set(Pointer s, int maxdd, Pointer result) {
		_meos_c.floatspan_round_set(s, maxdd, result);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer set_in(String str, int basetype) {
		var _result = _meos_c.set_in(str, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String set_out(Pointer s, int maxdd) {
		var _result = _meos_c.set_out(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_in(String str, int spantype) {
		var _result = _meos_c.span_in(str, spantype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String span_out(Pointer s, int maxdd) {
		var _result = _meos_c.span_out(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_in(String str, int spantype) {
		var _result = _meos_c.spanset_in(str, spantype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String spanset_out(Pointer ss, int maxdd) {
		var _result = _meos_c.spanset_out(ss, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_make(Pointer values, int count, int basetype, boolean order) {
		var _result = _meos_c.set_make(values, count, basetype, order);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_make_exp(Pointer values, int count, int maxcount, int basetype, boolean order) {
		var _result = _meos_c.set_make_exp(values, count, maxcount, basetype, order);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_make_free(Pointer values, int count, int basetype, boolean order) {
		var _result = _meos_c.set_make_free(values, count, basetype, order);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_make(Pointer lower, Pointer upper, boolean lower_inc, boolean upper_inc, int basetype) {
		var _result = _meos_c.span_make(lower, upper, lower_inc, upper_inc, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void span_set(Pointer lower, Pointer upper, boolean lower_inc, boolean upper_inc, int basetype, int spantype, Pointer s) {
		_meos_c.span_set(lower, upper, lower_inc, upper_inc, basetype, spantype, s);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_make_exp(Pointer spans, int count, int maxcount, boolean normalize, boolean order) {
		var _result = _meos_c.spanset_make_exp(spans, count, maxcount, normalize, order);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_make_free(Pointer spans, int count, boolean normalize, boolean order) {
		var _result = _meos_c.spanset_make_free(spans, count, normalize, order);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_span(Pointer s) {
		var _result = _meos_c.set_span(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_spanset(Pointer s) {
		var _result = _meos_c.set_spanset(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void value_set_span(Pointer value, int basetype, Pointer s) {
		_meos_c.value_set_span(value, basetype, s);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer value_set(Pointer d, int basetype) {
		var _result = _meos_c.value_set(d, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer value_span(Pointer d, int basetype) {
		var _result = _meos_c.value_span(d, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer value_spanset(Pointer d, int basetype) {
		var _result = _meos_c.value_spanset(d, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int numspan_width(Pointer s) {
		var _result = _meos_c.numspan_width(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int numspanset_width(Pointer ss, boolean boundspan) {
		var _result = _meos_c.numspanset_width(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int set_end_value(Pointer s) {
		var _result = _meos_c.set_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int set_mem_size(Pointer s) {
		var _result = _meos_c.set_mem_size(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void set_set_subspan(Pointer s, int minidx, int maxidx, Pointer result) {
		_meos_c.set_set_subspan(s, minidx, maxidx, result);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void set_set_span(Pointer s, Pointer result) {
		_meos_c.set_set_span(s, result);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static int set_start_value(Pointer s) {
		var _result = _meos_c.set_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_c.set_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer set_vals(Pointer s) {
		var _result = _meos_c.set_vals(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_values(Pointer s) {
		var _result = _meos_c.set_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_lower(Pointer ss) {
		var _result = _meos_c.spanset_lower(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_mem_size(Pointer ss) {
		var _result = _meos_c.spanset_mem_size(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_sps(Pointer ss) {
		var _result = _meos_c.spanset_sps(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spanset_upper(Pointer ss) {
		var _result = _meos_c.spanset_upper(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void bigintspan_set_floatspan(Pointer s1, Pointer s2) {
		_meos_c.bigintspan_set_floatspan(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void bigintspan_set_intspan(Pointer s1, Pointer s2) {
		_meos_c.bigintspan_set_intspan(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void datespan_set_tstzspan(Pointer s1, Pointer s2) {
		_meos_c.datespan_set_tstzspan(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void floatspan_set_bigintspan(Pointer s1, Pointer s2) {
		_meos_c.floatspan_set_bigintspan(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void floatspan_set_intspan(Pointer s1, Pointer s2) {
		_meos_c.floatspan_set_intspan(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void intspan_set_bigintspan(Pointer s1, Pointer s2) {
		_meos_c.intspan_set_bigintspan(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void intspan_set_floatspan(Pointer s1, Pointer s2) {
		_meos_c.intspan_set_floatspan(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer numset_shift_scale(Pointer s, Pointer shift, Pointer width, boolean hasshift, boolean haswidth) {
		var _result = _meos_c.numset_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer numspan_expand(Pointer s, Pointer value) {
		var _result = _meos_c.numspan_expand(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer numspan_shift_scale(Pointer s, Pointer shift, Pointer width, boolean hasshift, boolean haswidth) {
		var _result = _meos_c.numspan_shift_scale(s, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer numspanset_shift_scale(Pointer ss, Pointer shift, Pointer width, boolean hasshift, boolean haswidth) {
		var _result = _meos_c.numspanset_shift_scale(ss, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer set_compact(Pointer s) {
		var _result = _meos_c.set_compact(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void span_expand(Pointer s1, Pointer s2) {
		_meos_c.span_expand(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_compact(Pointer ss) {
		var _result = _meos_c.spanset_compact(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_expand_value(Pointer box, Pointer value, int basetyp) {
		var _result = _meos_c.tbox_expand_value(box, value, basetyp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer textcat_textset_text_common(Pointer s, Pointer txt, boolean invert) {
		var _result = _meos_c.textcat_textset_text_common(s, txt, invert);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tstzspan_set_datespan(Pointer s1, Pointer s2) {
		_meos_c.tstzspan_set_datespan(s1, s2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_span_value(Pointer s, Pointer value) {
		var _result = _meos_c.adjacent_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_c.adjacent_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean adjacent_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_c.adjacent_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_value_set(Pointer value, Pointer s) {
		var _result = _meos_c.contained_value_set(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_value_span(Pointer value, Pointer s) {
		var _result = _meos_c.contained_value_span(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_c.contained_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_value(Pointer s, Pointer value) {
		var _result = _meos_c.contains_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_span_value(Pointer s, Pointer value) {
		var _result = _meos_c.contains_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_c.contains_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean ovadj_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_c.ovadj_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_set_value(Pointer s, Pointer value) {
		var _result = _meos_c.left_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_span_value(Pointer s, Pointer value) {
		var _result = _meos_c.left_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_c.left_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_value_set(Pointer value, Pointer s) {
		var _result = _meos_c.left_value_set(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_value_span(Pointer value, Pointer s) {
		var _result = _meos_c.left_value_span(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean left_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_c.left_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean lfnadj_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_c.lfnadj_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_set_value(Pointer s, Pointer value) {
		var _result = _meos_c.overleft_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_span_value(Pointer s, Pointer value) {
		var _result = _meos_c.overleft_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_c.overleft_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_value_set(Pointer value, Pointer s) {
		var _result = _meos_c.overleft_value_set(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_value_span(Pointer value, Pointer s) {
		var _result = _meos_c.overleft_value_span(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overleft_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_c.overleft_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_set_value(Pointer s, Pointer value) {
		var _result = _meos_c.overright_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_span_value(Pointer s, Pointer value) {
		var _result = _meos_c.overright_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_c.overright_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_value_set(Pointer value, Pointer s) {
		var _result = _meos_c.overright_value_set(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_value_span(Pointer value, Pointer s) {
		var _result = _meos_c.overright_value_span(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean overright_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_c.overright_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_value_set(Pointer value, Pointer s) {
		var _result = _meos_d.right_value_set(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_set_value(Pointer s, Pointer value) {
		var _result = _meos_d.right_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_value_span(Pointer value, Pointer s) {
		var _result = _meos_d.right_value_span(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_d.right_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_span_value(Pointer s, Pointer value) {
		var _result = _meos_d.right_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean right_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_d.right_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean bbox_type(int bboxtype) {
		var _result = _meos_d.bbox_type(bboxtype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long bbox_get_size(int bboxtype) {
		var _result = _meos_d.bbox_get_size(bboxtype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int bbox_max_dims(int bboxtype) {
		var _result = _meos_d.bbox_max_dims(bboxtype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_bbox_eq(Pointer box1, Pointer box2, int temptype) {
		var _result = _meos_d.temporal_bbox_eq(box1, box2, temptype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_bbox_cmp(Pointer box1, Pointer box2, int temptype) {
		var _result = _meos_d.temporal_bbox_cmp(box1, box2, temptype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void bbox_union_span_span(Pointer s1, Pointer s2, Pointer result) {
		_meos_d.bbox_union_span_span(s1, s2, result);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer inter_span_span(Pointer s1, Pointer s2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.inter_span_span(s1, s2, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_value(Pointer s, Pointer value) {
		var _result = _meos_d.intersection_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_span_value(Pointer s, Pointer value) {
		var _result = _meos_d.intersection_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_d.intersection_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_value_set(Pointer value, Pointer s) {
		var _result = _meos_d.intersection_value_set(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_value_span(Pointer value, Pointer s) {
		var _result = _meos_d.intersection_value_span(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_d.intersection_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int mi_span_span(Pointer s1, Pointer s2, Pointer result) {
		var _result = _meos_d.mi_span_span(s1, s2, result);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_value(Pointer s, Pointer value) {
		var _result = _meos_d.minus_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_span_value(Pointer s, Pointer value) {
		var _result = _meos_d.minus_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_d.minus_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_value_set(Pointer value, Pointer s) {
		var _result = _meos_d.minus_value_set(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_value_span(Pointer value, Pointer s) {
		var _result = _meos_d.minus_value_span(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_d.minus_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer super_union_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_d.super_union_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_value(Pointer s, Pointer value) {
		var _result = _meos_d.union_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_span_value(Pointer s, Pointer value) {
		var _result = _meos_d.union_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_d.union_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_value_set(Pointer value, Pointer s) {
		var _result = _meos_d.union_value_set(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_value_span(Pointer value, Pointer s) {
		var _result = _meos_d.union_value_span(value, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_value_spanset(Pointer value, Pointer ss) {
		var _result = _meos_d.union_value_spanset(value, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_set_set(Pointer s1, Pointer s2) {
		var _result = _meos_d.distance_set_set(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_set_value(Pointer s, Pointer value) {
		var _result = _meos_d.distance_set_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_span_span(Pointer s1, Pointer s2) {
		var _result = _meos_d.distance_span_span(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_span_value(Pointer s, Pointer value) {
		var _result = _meos_d.distance_span_value(s, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_spanset_span(Pointer ss, Pointer s) {
		var _result = _meos_d.distance_spanset_span(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_spanset_spanset(Pointer ss1, Pointer ss2) {
		var _result = _meos_d.distance_spanset_spanset(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_spanset_value(Pointer ss, Pointer value) {
		var _result = _meos_d.distance_spanset_value(ss, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int distance_value_value(Pointer l, Pointer r, int basetype) {
		var _result = _meos_d.distance_value_value(l, r, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanbase_extent_transfn(Pointer state, Pointer value, int basetype) {
		var _result = _meos_d.spanbase_extent_transfn(state, value, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer value_union_transfn(Pointer state, Pointer value, int basetype) {
		var _result = _meos_d.value_union_transfn(state, value, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer number_tstzspan_to_tbox(Pointer d, int basetype, Pointer s) {
		var _result = _meos_d.number_tstzspan_to_tbox(d, basetype, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer number_timestamptz_to_tbox(Pointer d, int basetype, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.number_timestamptz_to_tbox(d, basetype, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tbox_set(Pointer s, Pointer p, Pointer box) {
		_meos_d.tbox_set(s, p, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void float_set_tbox(double d, Pointer box) {
		_meos_d.float_set_tbox(d, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void int_set_tbox(int i, Pointer box) {
		_meos_d.int_set_tbox(i, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void number_set_tbox(Pointer d, int basetype, Pointer box) {
		_meos_d.number_set_tbox(d, basetype, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer number_tbox(Pointer value, int basetype) {
		var _result = _meos_d.number_tbox(value, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void numset_set_tbox(Pointer s, Pointer box) {
		_meos_d.numset_set_tbox(s, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void numspan_set_tbox(Pointer span, Pointer box) {
		_meos_d.numspan_set_tbox(span, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void timestamptz_set_tbox(OffsetDateTime t, Pointer box) {
		var t_new = t.toEpochSecond();
		_meos_d.timestamptz_set_tbox(t_new, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tstzset_set_tbox(Pointer s, Pointer box) {
		_meos_d.tstzset_set_tbox(s, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tstzspan_set_tbox(Pointer s, Pointer box) {
		_meos_d.tstzspan_set_tbox(s, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_value(Pointer box, Pointer shift, Pointer width, boolean hasshift, boolean haswidth) {
		var _result = _meos_d.tbox_shift_scale_value(box, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tbox_expand(Pointer box1, Pointer box2) {
		_meos_d.tbox_expand(box1, box2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer inter_tbox_tbox(Pointer box1, Pointer box2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.inter_tbox_tbox(box1, box2, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolinst_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.tboolinst_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolinst_in(String str) {
		var _result = _meos_d.tboolinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.tboolseq_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseq_in(String str, int interp) {
		var _result = _meos_d.tboolseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseqset_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.tboolseqset_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tboolseqset_in(String str) {
		var _result = _meos_d.tboolseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_in(String str, int temptype) {
		var _result = _meos_d.temporal_in(str, temptype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String temporal_out(Pointer temp, int maxdd) {
		var _result = _meos_d.temporal_out(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temparr_out(Pointer temparr, int count, int maxdd) {
		var _result = _meos_d.temparr_out(temparr, count, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatinst_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.tfloatinst_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatinst_in(String str) {
		var _result = _meos_d.tfloatinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_mfjson(Pointer mfjson, int interp) {
		var _result = _meos_d.tfloatseq_from_mfjson(mfjson, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseq_in(String str, int interp) {
		var _result = _meos_d.tfloatseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_from_mfjson(Pointer mfjson, int interp) {
		var _result = _meos_d.tfloatseqset_from_mfjson(mfjson, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_in(String str) {
		var _result = _meos_d.tfloatseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_from_mfjson(Pointer mfjson, boolean spatial, int srid, int temptype) {
		var _result = _meos_d.tinstant_from_mfjson(mfjson, spatial, srid, temptype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_in(String str, int temptype) {
		var _result = _meos_d.tinstant_in(str, temptype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tinstant_out(Pointer inst, int maxdd) {
		var _result = _meos_d.tinstant_out(inst, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintinst_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.tintinst_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintinst_in(String str) {
		var _result = _meos_d.tintinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseq_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.tintseq_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseq_in(String str, int interp) {
		var _result = _meos_d.tintseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseqset_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.tintseqset_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tintseqset_in(String str) {
		var _result = _meos_d.tintseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_from_mfjson(Pointer mfjson, boolean spatial, int srid, int temptype, int interp) {
		var _result = _meos_d.tsequence_from_mfjson(mfjson, spatial, srid, temptype, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_in(String str, int temptype, int interp) {
		var _result = _meos_d.tsequence_in(str, temptype, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tsequence_out(Pointer seq, int maxdd) {
		var _result = _meos_d.tsequence_out(seq, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_from_mfjson(Pointer mfjson, boolean spatial, int srid, int temptype, int interp) {
		var _result = _meos_d.tsequenceset_from_mfjson(mfjson, spatial, srid, temptype, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_in(String str, int temptype, int interp) {
		var _result = _meos_d.tsequenceset_in(str, temptype, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tsequenceset_out(Pointer ss, int maxdd) {
		var _result = _meos_d.tsequenceset_out(ss, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextinst_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.ttextinst_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextinst_in(String str) {
		var _result = _meos_d.ttextinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.ttextseq_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseq_in(String str, int interp) {
		var _result = _meos_d.ttextseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseqset_from_mfjson(Pointer mfjson) {
		var _result = _meos_d.ttextseqset_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer ttextseqset_in(String str) {
		var _result = _meos_d.ttextseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_from_mfjson(String mfjson, int temptype) {
		var _result = _meos_d.temporal_from_mfjson(mfjson, temptype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_from_base_temp(Pointer value, int temptype, Pointer temp) {
		var _result = _meos_d.temporal_from_base_temp(value, temptype, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_copy(Pointer inst) {
		var _result = _meos_d.tinstant_copy(inst);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_make(Pointer value, int temptype, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tinstant_make(value, temptype, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_make_free(Pointer value, int temptype, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tinstant_make_free(value, temptype, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_copy(Pointer seq) {
		var _result = _meos_d.tsequence_copy(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_from_base_temp(Pointer value, int temptype, Pointer seq) {
		var _result = _meos_d.tsequence_from_base_temp(value, temptype, seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_from_base_tstzset(Pointer value, int temptype, Pointer s) {
		var _result = _meos_d.tsequence_from_base_tstzset(value, temptype, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_from_base_tstzspan(Pointer value, int temptype, Pointer s, int interp) {
		var _result = _meos_d.tsequence_from_base_tstzspan(value, temptype, s, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_make_exp(Pointer instants, int count, int maxcount, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		var _result = _meos_d.tsequence_make_exp(instants, count, maxcount, lower_inc, upper_inc, interp, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_make_free(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		var _result = _meos_d.tsequence_make_free(instants, count, lower_inc, upper_inc, interp, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_copy(Pointer ss) {
		var _result = _meos_d.tsequenceset_copy(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tseqsetarr_to_tseqset(Pointer seqsets, int count, int totalseqs) {
		var _result = _meos_d.tseqsetarr_to_tseqset(seqsets, count, totalseqs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_from_base_temp(Pointer value, int temptype, Pointer ss) {
		var _result = _meos_d.tsequenceset_from_base_temp(value, temptype, ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_from_base_tstzspanset(Pointer value, int temptype, Pointer ss, int interp) {
		var _result = _meos_d.tsequenceset_from_base_tstzspanset(value, temptype, ss, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_exp(Pointer sequences, int count, int maxcount, boolean normalize) {
		var _result = _meos_d.tsequenceset_make_exp(sequences, count, maxcount, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_free(Pointer sequences, int count, boolean normalize) {
		var _result = _meos_d.tsequenceset_make_free(sequences, count, normalize);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void temporal_set_tstzspan(Pointer temp, Pointer s) {
		_meos_d.temporal_set_tstzspan(temp, s);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tinstant_set_tstzspan(Pointer inst, Pointer s) {
		_meos_d.tinstant_set_tstzspan(inst, s);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tnumber_set_tbox(Pointer temp, Pointer box) {
		_meos_d.tnumber_set_tbox(temp, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tnumberinst_set_tbox(Pointer inst, Pointer box) {
		_meos_d.tnumberinst_set_tbox(inst, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tnumberseq_set_tbox(Pointer seq, Pointer box) {
		_meos_d.tnumberseq_set_tbox(seq, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tnumberseqset_set_tbox(Pointer ss, Pointer box) {
		_meos_d.tnumberseqset_set_tbox(ss, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tsequence_set_tstzspan(Pointer seq, Pointer s) {
		_meos_d.tsequence_set_tstzspan(seq, s);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tsequenceset_set_tstzspan(Pointer ss, Pointer s) {
		_meos_d.tsequenceset_set_tstzspan(ss, s);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_end_inst(Pointer temp) {
		var _result = _meos_d.temporal_end_inst(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_end_value(Pointer temp) {
		var _result = _meos_d.temporal_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_inst_n(Pointer temp, int n) {
		var _result = _meos_d.temporal_inst_n(temp, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_insts_p(Pointer temp, Pointer count) {
		var _result = _meos_d.temporal_insts_p(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_max_inst_p(Pointer temp) {
		var _result = _meos_d.temporal_max_inst_p(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_max_value(Pointer temp) {
		var _result = _meos_d.temporal_max_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long temporal_mem_size(Pointer temp) {
		var _result = _meos_d.temporal_mem_size(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_min_inst_p(Pointer temp) {
		var _result = _meos_d.temporal_min_inst_p(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_min_value(Pointer temp) {
		var _result = _meos_d.temporal_min_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_sequences_p(Pointer temp, Pointer count) {
		var _result = _meos_d.temporal_sequences_p(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void temporal_set_bbox(Pointer temp, Pointer box) {
		_meos_d.temporal_set_bbox(temp, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_start_inst(Pointer temp) {
		var _result = _meos_d.temporal_start_inst(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int temporal_start_value(Pointer temp) {
		var _result = _meos_d.temporal_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_values_p(Pointer temp, Pointer count) {
		var _result = _meos_d.temporal_values_p(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.temporal_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_values(Pointer temp, Pointer count) {
		var _result = _meos_d.temporal_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tinstant_hash(Pointer inst) {
		var _result = _meos_d.tinstant_hash(inst);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_insts(Pointer inst, Pointer count) {
		var _result = _meos_d.tinstant_insts(inst, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tinstant_set_bbox(Pointer inst, Pointer box) {
		_meos_d.tinstant_set_bbox(inst, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_time(Pointer inst) {
		var _result = _meos_d.tinstant_time(inst);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_timestamps(Pointer inst, Pointer count) {
		var _result = _meos_d.tinstant_timestamps(inst, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tinstant_value_p(Pointer inst) {
		var _result = _meos_d.tinstant_value_p(inst);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tinstant_value(Pointer inst) {
		var _result = _meos_d.tinstant_value(inst);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_value_at_timestamptz(Pointer inst, OffsetDateTime t) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		var t_new = t.toEpochSecond();
		out = _meos_d.tinstant_value_at_timestamptz(inst, t_new, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_values_p(Pointer inst, Pointer count) {
		var _result = _meos_d.tinstant_values_p(inst, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tnumber_set_span(Pointer temp, Pointer span) {
		_meos_d.tnumber_set_span(temp, span);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberinst_valuespans(Pointer inst) {
		var _result = _meos_d.tnumberinst_valuespans(inst);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumberseq_avg_val(Pointer seq) {
		var _result = _meos_d.tnumberseq_avg_val(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseq_valuespans(Pointer seq) {
		var _result = _meos_d.tnumberseq_valuespans(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumberseqset_avg_val(Pointer ss) {
		var _result = _meos_d.tnumberseqset_avg_val(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_valuespans(Pointer ss) {
		var _result = _meos_d.tnumberseqset_valuespans(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_duration(Pointer seq) {
		var _result = _meos_d.tsequence_duration(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tsequence_end_timestamptz(Pointer seq) {
		var _result = _meos_d.tsequence_end_timestamptz(seq);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static int tsequence_hash(Pointer seq) {
		var _result = _meos_d.tsequence_hash(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_insts_p(Pointer seq) {
		var _result = _meos_d.tsequence_insts_p(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_max_inst_p(Pointer seq) {
		var _result = _meos_d.tsequence_max_inst_p(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tsequence_max_val(Pointer seq) {
		var _result = _meos_d.tsequence_max_val(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_min_inst_p(Pointer seq) {
		var _result = _meos_d.tsequence_min_inst_p(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tsequence_min_val(Pointer seq) {
		var _result = _meos_d.tsequence_min_val(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_segments(Pointer seq, Pointer count) {
		var _result = _meos_d.tsequence_segments(seq, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_seqs(Pointer seq, Pointer count) {
		var _result = _meos_d.tsequence_seqs(seq, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tsequence_start_timestamptz(Pointer seq) {
		var _result = _meos_d.tsequence_start_timestamptz(seq);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_time(Pointer seq) {
		var _result = _meos_d.tsequence_time(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_timestamps(Pointer seq, Pointer count) {
		var _result = _meos_d.tsequence_timestamps(seq, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_value_at_timestamptz(Pointer seq, OffsetDateTime t, boolean strict) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		var t_new = t.toEpochSecond();
		out = _meos_d.tsequence_value_at_timestamptz(seq, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_values_p(Pointer seq, Pointer count) {
		var _result = _meos_d.tsequence_values_p(seq, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_duration(Pointer ss, boolean boundspan) {
		var _result = _meos_d.tsequenceset_duration(ss, boundspan);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tsequenceset_end_timestamptz(Pointer ss) {
		var _result = _meos_d.tsequenceset_end_timestamptz(ss);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static int tsequenceset_hash(Pointer ss) {
		var _result = _meos_d.tsequenceset_hash(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_inst_n(Pointer ss, int n) {
		var _result = _meos_d.tsequenceset_inst_n(ss, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_insts_p(Pointer ss) {
		var _result = _meos_d.tsequenceset_insts_p(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_max_inst_p(Pointer ss) {
		var _result = _meos_d.tsequenceset_max_inst_p(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tsequenceset_max_val(Pointer ss) {
		var _result = _meos_d.tsequenceset_max_val(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_min_inst_p(Pointer ss) {
		var _result = _meos_d.tsequenceset_min_inst_p(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tsequenceset_min_val(Pointer ss) {
		var _result = _meos_d.tsequenceset_min_val(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tsequenceset_num_instants(Pointer ss) {
		var _result = _meos_d.tsequenceset_num_instants(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tsequenceset_num_timestamps(Pointer ss) {
		var _result = _meos_d.tsequenceset_num_timestamps(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_segments(Pointer ss, Pointer count) {
		var _result = _meos_d.tsequenceset_segments(ss, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_sequences_p(Pointer ss) {
		var _result = _meos_d.tsequenceset_sequences_p(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime tsequenceset_start_timestamptz(Pointer ss) {
		var _result = _meos_d.tsequenceset_start_timestamptz(ss);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_time(Pointer ss) {
		var _result = _meos_d.tsequenceset_time(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_timestamptz_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.tsequenceset_timestamptz_n(ss, n, result);
		MeosErrorHandler.checkError();
		return out ? result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_timestamps(Pointer ss, Pointer count) {
		var _result = _meos_d.tsequenceset_timestamps(ss, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_value_at_timestamptz(Pointer ss, OffsetDateTime t, boolean strict) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		var t_new = t.toEpochSecond();
		out = _meos_d.tsequenceset_value_at_timestamptz(ss, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_value_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.tsequenceset_value_n(ss, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_values_p(Pointer ss, Pointer count) {
		var _result = _meos_d.tsequenceset_values_p(ss, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void temporal_restart(Pointer temp, int count) {
		_meos_d.temporal_restart(temp, count);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tsequence(Pointer temp, int interp) {
		var _result = _meos_d.temporal_tsequence(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_tsequenceset(Pointer temp, int interp) {
		var _result = _meos_d.temporal_tsequenceset(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_shift_time(Pointer inst, Pointer interv) {
		var _result = _meos_d.tinstant_shift_time(inst, interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_to_tsequence(Pointer inst, int interp) {
		var _result = _meos_d.tinstant_to_tsequence(inst, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_to_tsequence_free(Pointer inst, int interp) {
		var _result = _meos_d.tinstant_to_tsequence_free(inst, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_to_tsequenceset(Pointer inst, int interp) {
		var _result = _meos_d.tinstant_to_tsequenceset(inst, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_shift_scale_value(Pointer temp, Pointer shift, Pointer width, boolean hasshift, boolean haswidth) {
		var _result = _meos_d.tnumber_shift_scale_value(temp, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberinst_shift_value(Pointer inst, Pointer shift) {
		var _result = _meos_d.tnumberinst_shift_value(inst, shift);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseq_shift_scale_value(Pointer seq, Pointer shift, Pointer width, boolean hasshift, boolean haswidth) {
		var _result = _meos_d.tnumberseq_shift_scale_value(seq, shift, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_shift_scale_value(Pointer ss, Pointer start, Pointer width, boolean hasshift, boolean haswidth) {
		var _result = _meos_d.tnumberseqset_shift_scale_value(ss, start, width, hasshift, haswidth);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tsequence_restart(Pointer seq, int count) {
		_meos_d.tsequence_restart(seq, count);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_set_interp(Pointer seq, int interp) {
		var _result = _meos_d.tsequence_set_interp(seq, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_shift_scale_time(Pointer seq, Pointer shift, Pointer duration) {
		var _result = _meos_d.tsequence_shift_scale_time(seq, shift, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_subseq(Pointer seq, int from, int to, boolean lower_inc, boolean upper_inc) {
		var _result = _meos_d.tsequence_subseq(seq, from, to, lower_inc, upper_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tinstant(Pointer seq) {
		var _result = _meos_d.tsequence_to_tinstant(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tsequenceset(Pointer seq) {
		var _result = _meos_d.tsequence_to_tsequenceset(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tsequenceset_free(Pointer seq) {
		var _result = _meos_d.tsequence_to_tsequenceset_free(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tsequenceset_interp(Pointer seq, int interp) {
		var _result = _meos_d.tsequence_to_tsequenceset_interp(seq, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tsequenceset_restart(Pointer ss, int count) {
		_meos_d.tsequenceset_restart(ss, count);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_set_interp(Pointer ss, int interp) {
		var _result = _meos_d.tsequenceset_set_interp(ss, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_shift_scale_time(Pointer ss, Pointer start, Pointer duration) {
		var _result = _meos_d.tsequenceset_shift_scale_time(ss, start, duration);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_discrete(Pointer ss) {
		var _result = _meos_d.tsequenceset_to_discrete(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_linear(Pointer ss) {
		var _result = _meos_d.tsequenceset_to_linear(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_step(Pointer ss) {
		var _result = _meos_d.tsequenceset_to_step(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_tinstant(Pointer ss) {
		var _result = _meos_d.tsequenceset_to_tinstant(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_tsequence(Pointer ss) {
		var _result = _meos_d.tsequenceset_to_tsequence(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_merge(Pointer inst1, Pointer inst2) {
		var _result = _meos_d.tinstant_merge(inst1, inst2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_merge_array(Pointer instants, int count) {
		var _result = _meos_d.tinstant_merge_array(instants, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_append_tinstant(Pointer seq, Pointer inst, double maxdist, Pointer maxt, boolean expand) {
		var _result = _meos_d.tsequence_append_tinstant(seq, inst, maxdist, maxt, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_append_tsequence(Pointer seq1, Pointer seq2, boolean expand) {
		var _result = _meos_d.tsequence_append_tsequence(seq1, seq2, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_timestamptz(Pointer seq, OffsetDateTime t, boolean connect) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tsequence_delete_timestamptz(seq, t_new, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_tstzset(Pointer seq, Pointer s, boolean connect) {
		var _result = _meos_d.tsequence_delete_tstzset(seq, s, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_tstzspan(Pointer seq, Pointer s, boolean connect) {
		var _result = _meos_d.tsequence_delete_tstzspan(seq, s, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_tstzspanset(Pointer seq, Pointer ss, boolean connect) {
		var _result = _meos_d.tsequence_delete_tstzspanset(seq, ss, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_insert(Pointer seq1, Pointer seq2, boolean connect) {
		var _result = _meos_d.tsequence_insert(seq1, seq2, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_merge(Pointer seq1, Pointer seq2) {
		var _result = _meos_d.tsequence_merge(seq1, seq2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_merge_array(Pointer sequences, int count) {
		var _result = _meos_d.tsequence_merge_array(sequences, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_append_tinstant(Pointer ss, Pointer inst, double maxdist, Pointer maxt, boolean expand) {
		var _result = _meos_d.tsequenceset_append_tinstant(ss, inst, maxdist, maxt, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_append_tsequence(Pointer ss, Pointer seq, boolean expand) {
		var _result = _meos_d.tsequenceset_append_tsequence(ss, seq, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tsequenceset_delete_timestamptz(ss, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_tstzset(Pointer ss, Pointer s) {
		var _result = _meos_d.tsequenceset_delete_tstzset(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_tstzspan(Pointer ss, Pointer s) {
		var _result = _meos_d.tsequenceset_delete_tstzspan(ss, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_tstzspanset(Pointer ss, Pointer ps) {
		var _result = _meos_d.tsequenceset_delete_tstzspanset(ss, ps);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_insert(Pointer ss1, Pointer ss2) {
		var _result = _meos_d.tsequenceset_insert(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_merge(Pointer ss1, Pointer ss2) {
		var _result = _meos_d.tsequenceset_merge(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_merge_array(Pointer seqsets, int count) {
		var _result = _meos_d.tsequenceset_merge_array(seqsets, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tsequence_expand_bbox(Pointer seq, Pointer inst) {
		_meos_d.tsequence_expand_bbox(seq, inst);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tsequence_set_bbox(Pointer seq, Pointer box) {
		_meos_d.tsequence_set_bbox(seq, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tsequenceset_expand_bbox(Pointer ss, Pointer seq) {
		_meos_d.tsequenceset_expand_bbox(ss, seq);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tsequenceset_set_bbox(Pointer ss, Pointer box) {
		_meos_d.tsequenceset_set_bbox(ss, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tcontseq_after_timestamptz(Pointer seq, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tcontseq_after_timestamptz(seq, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontseq_before_timestamptz(Pointer seq, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tcontseq_before_timestamptz(seq, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tcontseq_restrict_minmax(Pointer seq, boolean min, boolean atfunc) {
		var _result = _meos_d.tcontseq_restrict_minmax(seq, min, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdiscseq_after_timestamptz(Pointer seq, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tdiscseq_after_timestamptz(seq, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdiscseq_before_timestamptz(Pointer seq, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tdiscseq_before_timestamptz(seq, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdiscseq_restrict_minmax(Pointer seq, boolean min, boolean atfunc) {
		var _result = _meos_d.tdiscseq_restrict_minmax(seq, min, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean temporal_bbox_restrict_set(Pointer temp, Pointer set) {
		var _result = _meos_d.temporal_bbox_restrict_set(temp, set);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_minmax(Pointer temp, boolean min, boolean atfunc) {
		var _result = _meos_d.temporal_restrict_minmax(temp, min, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_timestamptz(Pointer temp, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.temporal_restrict_timestamptz(temp, t_new, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_tstzset(Pointer temp, Pointer s, boolean atfunc) {
		var _result = _meos_d.temporal_restrict_tstzset(temp, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_tstzspan(Pointer temp, Pointer s, boolean atfunc) {
		var _result = _meos_d.temporal_restrict_tstzspan(temp, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_tstzspanset(Pointer temp, Pointer ss, boolean atfunc) {
		var _result = _meos_d.temporal_restrict_tstzspanset(temp, ss, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_value(Pointer temp, Pointer value, boolean atfunc) {
		var _result = _meos_d.temporal_restrict_value(temp, value, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_values(Pointer temp, Pointer set, boolean atfunc) {
		var _result = _meos_d.temporal_restrict_values(temp, set, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		var t_new = t.toEpochSecond();
		out = _meos_d.temporal_value_at_timestamptz(temp, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_after_timestamptz(Pointer inst, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tinstant_after_timestamptz(inst, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_before_timestamptz(Pointer inst, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tinstant_before_timestamptz(inst, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_tstzspan(Pointer inst, Pointer period, boolean atfunc) {
		var _result = _meos_d.tinstant_restrict_tstzspan(inst, period, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_tstzspanset(Pointer inst, Pointer ss, boolean atfunc) {
		var _result = _meos_d.tinstant_restrict_tstzspanset(inst, ss, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_timestamptz(Pointer inst, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tinstant_restrict_timestamptz(inst, t_new, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_tstzset(Pointer inst, Pointer s, boolean atfunc) {
		var _result = _meos_d.tinstant_restrict_tstzset(inst, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_value(Pointer inst, Pointer value, boolean atfunc) {
		var _result = _meos_d.tinstant_restrict_value(inst, value, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_values(Pointer inst, Pointer set, boolean atfunc) {
		var _result = _meos_d.tinstant_restrict_values(inst, set, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_restrict_span(Pointer temp, Pointer span, boolean atfunc) {
		var _result = _meos_d.tnumber_restrict_span(temp, span, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_restrict_spanset(Pointer temp, Pointer ss, boolean atfunc) {
		var _result = _meos_d.tnumber_restrict_spanset(temp, ss, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberinst_restrict_span(Pointer inst, Pointer span, boolean atfunc) {
		var _result = _meos_d.tnumberinst_restrict_span(inst, span, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberinst_restrict_spanset(Pointer inst, Pointer ss, boolean atfunc) {
		var _result = _meos_d.tnumberinst_restrict_spanset(inst, ss, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_restrict_span(Pointer ss, Pointer span, boolean atfunc) {
		var _result = _meos_d.tnumberseqset_restrict_span(ss, span, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_restrict_spanset(Pointer ss, Pointer spanset, boolean atfunc) {
		var _result = _meos_d.tnumberseqset_restrict_spanset(ss, spanset, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_at_timestamptz(Pointer seq, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tsequence_at_timestamptz(seq, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_restrict_tstzspan(Pointer seq, Pointer s, boolean atfunc) {
		var _result = _meos_d.tsequence_restrict_tstzspan(seq, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_restrict_tstzspanset(Pointer seq, Pointer ss, boolean atfunc) {
		var _result = _meos_d.tsequence_restrict_tstzspanset(seq, ss, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_after_timestamptz(Pointer ss, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tsequenceset_after_timestamptz(ss, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_before_timestamptz(Pointer ss, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tsequenceset_before_timestamptz(ss, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_minmax(Pointer ss, boolean min, boolean atfunc) {
		var _result = _meos_d.tsequenceset_restrict_minmax(ss, min, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_tstzspan(Pointer ss, Pointer s, boolean atfunc) {
		var _result = _meos_d.tsequenceset_restrict_tstzspan(ss, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_tstzspanset(Pointer ss, Pointer ps, boolean atfunc) {
		var _result = _meos_d.tsequenceset_restrict_tstzspanset(ss, ps, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_timestamptz(Pointer ss, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tsequenceset_restrict_timestamptz(ss, t_new, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_tstzset(Pointer ss, Pointer s, boolean atfunc) {
		var _result = _meos_d.tsequenceset_restrict_tstzset(ss, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_value(Pointer ss, Pointer value, boolean atfunc) {
		var _result = _meos_d.tsequenceset_restrict_value(ss, value, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_values(Pointer ss, Pointer s, boolean atfunc) {
		var _result = _meos_d.tsequenceset_restrict_values(ss, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tinstant_cmp(Pointer inst1, Pointer inst2) {
		var _result = _meos_d.tinstant_cmp(inst1, inst2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tinstant_eq(Pointer inst1, Pointer inst2) {
		var _result = _meos_d.tinstant_eq(inst1, inst2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tsequence_cmp(Pointer seq1, Pointer seq2) {
		var _result = _meos_d.tsequence_cmp(seq1, seq2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tsequence_eq(Pointer seq1, Pointer seq2) {
		var _result = _meos_d.tsequence_eq(seq1, seq2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tsequenceset_cmp(Pointer ss1, Pointer ss2) {
		var _result = _meos_d.tsequenceset_cmp(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tsequenceset_eq(Pointer ss1, Pointer ss2) {
		var _result = _meos_d.tsequenceset_eq(ss1, ss2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.always_eq_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.always_eq_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.always_ne_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.always_ne_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.always_ge_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ge_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.always_ge_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.always_gt_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_gt_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.always_gt_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.always_le_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_le_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.always_le_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.always_lt_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_lt_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.always_lt_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.ever_eq_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.ever_eq_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.ever_ne_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.ever_ne_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.ever_ge_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ge_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.ever_ge_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.ever_gt_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_gt_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.ever_gt_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.ever_le_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_le_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.ever_le_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_base_temporal(Pointer value, Pointer temp) {
		var _result = _meos_d.ever_lt_base_temporal(value, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_lt_temporal_base(Pointer temp, Pointer value) {
		var _result = _meos_d.ever_lt_temporal_base(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberinst_abs(Pointer inst) {
		var _result = _meos_d.tnumberinst_abs(inst);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseq_abs(Pointer seq) {
		var _result = _meos_d.tnumberseq_abs(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseq_angular_difference(Pointer seq) {
		var _result = _meos_d.tnumberseq_angular_difference(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseq_delta_value(Pointer seq) {
		var _result = _meos_d.tnumberseq_delta_value(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_abs(Pointer ss) {
		var _result = _meos_d.tnumberseqset_abs(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_angular_difference(Pointer ss) {
		var _result = _meos_d.tnumberseqset_angular_difference(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_delta_value(Pointer ss) {
		var _result = _meos_d.tnumberseqset_delta_value(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_span_span_double(Pointer s1, Pointer s2) {
		var _result = _meos_d.distance_span_span_double(s1, s2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tbox_tbox(Pointer box1, Pointer box2) {
		var _result = _meos_d.nad_tbox_tbox(box1, box2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tnumber_number(Pointer temp, Pointer value) {
		var _result = _meos_d.nad_tnumber_number(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tnumber_tbox(Pointer temp, Pointer box) {
		var _result = _meos_d.nad_tnumber_tbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nad_tnumber_tnumber(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tnumber_number(Pointer temp, Pointer value) {
		var _result = _meos_d.tdistance_tnumber_number(temp, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumberinst_distance(Pointer inst1, Pointer inst2) {
		var _result = _meos_d.tnumberinst_distance(inst1, inst2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumberseq_integral(Pointer seq) {
		var _result = _meos_d.tnumberseq_integral(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumberseq_twavg(Pointer seq) {
		var _result = _meos_d.tnumberseq_twavg(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumberseqset_integral(Pointer ss) {
		var _result = _meos_d.tnumberseqset_integral(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnumberseqset_twavg(Pointer ss) {
		var _result = _meos_d.tnumberseqset_twavg(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_compact(Pointer temp) {
		var _result = _meos_d.temporal_compact(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequence_compact(Pointer seq) {
		var _result = _meos_d.tsequence_compact(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tsequenceset_compact(Pointer ss) {
		var _result = _meos_d.tsequenceset_compact(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_skiplist_make() {
		var _result = _meos_d.temporal_skiplist_make();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer skiplist_make(long key_size, long value_size, Pointer comp_fn, Pointer merge_fn) {
		var _result = _meos_d.skiplist_make(key_size, value_size, comp_fn, merge_fn);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int skiplist_search(Pointer list, Pointer key, Pointer value) {
		var _result = _meos_d.skiplist_search(list, key, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void skiplist_free(Pointer list) {
		_meos_d.skiplist_free(list);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void skiplist_splice(Pointer list, Pointer keys, Pointer values, int count, Pointer func, boolean crossings, int sktype) {
		_meos_d.skiplist_splice(list, keys, values, count, func, crossings, sktype);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void temporal_skiplist_splice(Pointer list, Pointer values, int count, Pointer func, boolean crossings) {
		_meos_d.temporal_skiplist_splice(list, values, count, func, crossings);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer skiplist_values(Pointer list) {
		var _result = _meos_d.skiplist_values(list);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer skiplist_keys_values(Pointer list, Pointer values) {
		var _result = _meos_d.skiplist_keys_values(list, values);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_app_tinst_transfn(Pointer state, Pointer inst, int interp, double maxdist, Pointer maxt) {
		var _result = _meos_d.temporal_app_tinst_transfn(state, inst, interp, maxdist, maxt);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer temporal_app_tseq_transfn(Pointer state, Pointer seq) {
		var _result = _meos_d.temporal_app_tseq_transfn(state, seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer span_bins(Pointer s, Pointer size, Pointer origin, Pointer count) {
		var _result = _meos_d.span_bins(s, size, origin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer spanset_bins(Pointer ss, Pointer size, Pointer origin, Pointer count) {
		var _result = _meos_d.spanset_bins(ss, size, origin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_value_bins(Pointer temp, Pointer size, Pointer origin, Pointer count) {
		var _result = _meos_d.tnumber_value_bins(temp, size, origin, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_value_time_boxes(Pointer temp, Pointer vsize, Pointer duration, Pointer vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_d.tnumber_value_time_boxes(temp, vsize, duration, vorigin, torigin_new, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_value_split(Pointer temp, Pointer vsize, Pointer vorigin, Pointer bins, Pointer count) {
		var _result = _meos_d.tnumber_value_split(temp, vsize, vorigin, bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tbox_get_value_time_tile(Pointer value, OffsetDateTime t, Pointer vsize, Pointer duration, Pointer vorigin, OffsetDateTime torigin, int basetype, int spantype) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_d.tbox_get_value_time_tile(value, t_new, vsize, duration, vorigin, torigin_new, basetype, spantype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnumber_value_time_split(Pointer temp, Pointer size, Pointer duration, Pointer vorigin, OffsetDateTime torigin, Pointer value_bins, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		var _result = _meos_d.tnumber_value_time_split(temp, size, duration, vorigin, torigin_new, value_bins, time_bins, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer proj_get_context() {
		var _result = _meos_d.proj_get_context();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int datum_geo_round(Pointer value, Pointer size) {
		var _result = _meos_d.datum_geo_round(value, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer point_round(Pointer gs, int maxdd) {
		var _result = _meos_d.point_round(gs, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void stbox_set(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s, Pointer box) {
		_meos_d.stbox_set(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, s, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void gbox_set_stbox(Pointer box, int srid, Pointer result) {
		_meos_d.gbox_set_stbox(box, srid, result);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static boolean geo_set_stbox(Pointer gs, Pointer box) {
		var _result = _meos_d.geo_set_stbox(gs, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void geoarr_set_stbox(Pointer values, int count, Pointer box) {
		_meos_d.geoarr_set_stbox(values, count, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static boolean spatial_set_stbox(Pointer d, int basetype, Pointer box) {
		var _result = _meos_d.spatial_set_stbox(d, basetype, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void spatialset_set_stbox(Pointer set, Pointer box) {
		_meos_d.spatialset_set_stbox(set, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void stbox_set_box3d(Pointer box, Pointer box3d) {
		_meos_d.stbox_set_box3d(box, box3d);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void stbox_set_gbox(Pointer box, Pointer gbox) {
		_meos_d.stbox_set_gbox(box, gbox);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tstzset_set_stbox(Pointer s, Pointer box) {
		_meos_d.tstzset_set_stbox(s, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tstzspan_set_stbox(Pointer s, Pointer box) {
		_meos_d.tstzspan_set_stbox(s, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tstzspanset_set_stbox(Pointer s, Pointer box) {
		_meos_d.tstzspanset_set_stbox(s, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void stbox_expand(Pointer box1, Pointer box2) {
		_meos_d.stbox_expand(box1, box2);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer inter_stbox_stbox(Pointer box1, Pointer box2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.inter_stbox_stbox(box1, box2, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer stbox_geo(Pointer box) {
		var _result = _meos_d.stbox_geo(box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpointinst_from_mfjson(Pointer mfjson, int srid) {
		var _result = _meos_d.tgeogpointinst_from_mfjson(mfjson, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpointinst_in(String str) {
		var _result = _meos_d.tgeogpointinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpointseq_from_mfjson(Pointer mfjson, int srid, int interp) {
		var _result = _meos_d.tgeogpointseq_from_mfjson(mfjson, srid, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpointseq_in(String str, int interp) {
		var _result = _meos_d.tgeogpointseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpointseqset_from_mfjson(Pointer mfjson, int srid, int interp) {
		var _result = _meos_d.tgeogpointseqset_from_mfjson(mfjson, srid, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeogpointseqset_in(String str) {
		var _result = _meos_d.tgeogpointseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompointinst_from_mfjson(Pointer mfjson, int srid) {
		var _result = _meos_d.tgeompointinst_from_mfjson(mfjson, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompointinst_in(String str) {
		var _result = _meos_d.tgeompointinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompointseq_from_mfjson(Pointer mfjson, int srid, int interp) {
		var _result = _meos_d.tgeompointseq_from_mfjson(mfjson, srid, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompointseq_in(String str, int interp) {
		var _result = _meos_d.tgeompointseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompointseqset_from_mfjson(Pointer mfjson, int srid, int interp) {
		var _result = _meos_d.tgeompointseqset_from_mfjson(mfjson, srid, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompointseqset_in(String str) {
		var _result = _meos_d.tgeompointseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeographyinst_from_mfjson(Pointer mfjson, int srid) {
		var _result = _meos_d.tgeographyinst_from_mfjson(mfjson, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeographyinst_in(String str) {
		var _result = _meos_d.tgeographyinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeographyseq_from_mfjson(Pointer mfjson, int srid, int interp) {
		var _result = _meos_d.tgeographyseq_from_mfjson(mfjson, srid, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeographyseq_in(String str, int interp) {
		var _result = _meos_d.tgeographyseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeographyseqset_from_mfjson(Pointer mfjson, int srid, int interp) {
		var _result = _meos_d.tgeographyseqset_from_mfjson(mfjson, srid, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeographyseqset_in(String str) {
		var _result = _meos_d.tgeographyseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometryinst_from_mfjson(Pointer mfjson, int srid) {
		var _result = _meos_d.tgeometryinst_from_mfjson(mfjson, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometryinst_in(String str) {
		var _result = _meos_d.tgeometryinst_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometryseq_from_mfjson(Pointer mfjson, int srid, int interp) {
		var _result = _meos_d.tgeometryseq_from_mfjson(mfjson, srid, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometryseq_in(String str, int interp) {
		var _result = _meos_d.tgeometryseq_in(str, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometryseqset_from_mfjson(Pointer mfjson, int srid, int interp) {
		var _result = _meos_d.tgeometryseqset_from_mfjson(mfjson, srid, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeometryseqset_in(String str) {
		var _result = _meos_d.tgeometryseqset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tspatial_set_stbox(Pointer temp, Pointer box) {
		_meos_d.tspatial_set_stbox(temp, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tgeoinst_set_stbox(Pointer inst, Pointer box) {
		_meos_d.tgeoinst_set_stbox(inst, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tspatialseq_set_stbox(Pointer seq, Pointer box) {
		_meos_d.tspatialseq_set_stbox(seq, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static void tspatialseqset_set_stbox(Pointer ss, Pointer box) {
		_meos_d.tspatialseqset_set_stbox(ss, box);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_restrict_elevation(Pointer temp, Pointer s, boolean atfunc) {
		var _result = _meos_d.tgeo_restrict_elevation(temp, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_restrict_geom(Pointer temp, Pointer gs, boolean atfunc) {
		var _result = _meos_d.tgeo_restrict_geom(temp, gs, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_restrict_stbox(Pointer temp, Pointer box, boolean border_inc, boolean atfunc) {
		var _result = _meos_d.tgeo_restrict_stbox(temp, box, border_inc, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoinst_restrict_geom(Pointer inst, Pointer gs, boolean atfunc) {
		var _result = _meos_d.tgeoinst_restrict_geom(inst, gs, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoinst_restrict_stbox(Pointer inst, Pointer box, boolean border_inc, boolean atfunc) {
		var _result = _meos_d.tgeoinst_restrict_stbox(inst, box, border_inc, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseq_restrict_geom(Pointer seq, Pointer gs, boolean atfunc) {
		var _result = _meos_d.tgeoseq_restrict_geom(seq, gs, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseq_restrict_stbox(Pointer seq, Pointer box, boolean border_inc, boolean atfunc) {
		var _result = _meos_d.tgeoseq_restrict_stbox(seq, box, border_inc, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_restrict_geom(Pointer ss, Pointer gs, boolean atfunc) {
		var _result = _meos_d.tgeoseqset_restrict_geom(ss, gs, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_restrict_stbox(Pointer ss, Pointer box, boolean border_inc, boolean atfunc) {
		var _result = _meos_d.tgeoseqset_restrict_stbox(ss, box, border_inc, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int spatial_srid(Pointer d, int basetype) {
		var _result = _meos_d.spatial_srid(d, basetype);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean spatial_set_srid(Pointer d, int basetype, int srid) {
		var _result = _meos_d.spatial_set_srid(d, basetype, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int tspatialinst_srid(Pointer inst) {
		var _result = _meos_d.tspatialinst_srid(inst);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_azimuth(Pointer seq) {
		var _result = _meos_d.tpointseq_azimuth(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_cumulative_length(Pointer seq, double prevlength) {
		var _result = _meos_d.tpointseq_cumulative_length(seq, prevlength);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tpointseq_is_simple(Pointer seq) {
		var _result = _meos_d.tpointseq_is_simple(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tpointseq_length(Pointer seq) {
		var _result = _meos_d.tpointseq_length(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_linear_trajectory(Pointer seq, boolean unary_union) {
		var _result = _meos_d.tpointseq_linear_trajectory(seq, unary_union);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseq_stboxes(Pointer seq, Pointer count) {
		var _result = _meos_d.tgeoseq_stboxes(seq, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseq_split_n_stboxes(Pointer seq, int max_count, Pointer count) {
		var _result = _meos_d.tgeoseq_split_n_stboxes(seq, max_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseqset_azimuth(Pointer ss) {
		var _result = _meos_d.tpointseqset_azimuth(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseqset_cumulative_length(Pointer ss) {
		var _result = _meos_d.tpointseqset_cumulative_length(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tpointseqset_is_simple(Pointer ss) {
		var _result = _meos_d.tpointseqset_is_simple(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tpointseqset_length(Pointer ss) {
		var _result = _meos_d.tpointseqset_length(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_stboxes(Pointer ss, Pointer count) {
		var _result = _meos_d.tgeoseqset_stboxes(ss, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_split_n_stboxes(Pointer ss, int max_count, Pointer count) {
		var _result = _meos_d.tgeoseqset_split_n_stboxes(ss, max_count, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpoint_get_coord(Pointer temp, int coord) {
		var _result = _meos_d.tpoint_get_coord(temp, coord);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeominst_tgeoginst(Pointer inst, boolean oper) {
		var _result = _meos_d.tgeominst_tgeoginst(inst, oper);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeomseq_tgeogseq(Pointer seq, boolean oper) {
		var _result = _meos_d.tgeomseq_tgeogseq(seq, oper);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeomseqset_tgeogseqset(Pointer ss, boolean oper) {
		var _result = _meos_d.tgeomseqset_tgeogseqset(ss, oper);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeom_tgeog(Pointer temp, boolean oper) {
		var _result = _meos_d.tgeom_tgeog(temp, oper);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeo_tpoint(Pointer temp, boolean oper) {
		var _result = _meos_d.tgeo_tpoint(temp, oper);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tspatialinst_set_srid(Pointer inst, int srid) {
		_meos_d.tspatialinst_set_srid(inst, srid);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_make_simple(Pointer seq, Pointer count) {
		var _result = _meos_d.tpointseq_make_simple(seq, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tspatialseq_set_srid(Pointer seq, int srid) {
		_meos_d.tspatialseq_set_srid(seq, srid);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseqset_make_simple(Pointer ss, Pointer count) {
		var _result = _meos_d.tpointseqset_make_simple(ss, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void tspatialseqset_set_srid(Pointer ss, int srid) {
		_meos_d.tspatialseqset_set_srid(ss, srid);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseq_twcentroid(Pointer seq) {
		var _result = _meos_d.tpointseq_twcentroid(seq);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpointseqset_twcentroid(Pointer ss) {
		var _result = _meos_d.tpointseqset_twcentroid(ss);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String npoint_as_ewkt(Pointer np, int maxdd) {
		var _result = _meos_d.npoint_as_ewkt(np, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String npoint_as_hexwkb(Pointer np, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_d.npoint_as_hexwkb(np, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String npoint_as_text(Pointer np, int maxdd) {
		var _result = _meos_d.npoint_as_text(np, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_as_wkb(Pointer np, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_d.npoint_as_wkb(np, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_from_hexwkb(String hexwkb) {
		var _result = _meos_d.npoint_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_from_wkb(Pointer wkb, long size) {
		var _result = _meos_d.npoint_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_in(String str) {
		var _result = _meos_d.npoint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String npoint_out(Pointer np, int maxdd) {
		var _result = _meos_d.npoint_out(np, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nsegment_in(String str) {
		var _result = _meos_d.nsegment_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String nsegment_out(Pointer ns, int maxdd) {
		var _result = _meos_d.nsegment_out(ns, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_make(long rid, double pos) {
		var _result = _meos_d.npoint_make(rid, pos);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nsegment_make(long rid, double pos1, double pos2) {
		var _result = _meos_d.nsegment_make(rid, pos1, pos2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geompoint_to_npoint(Pointer gs) {
		var _result = _meos_d.geompoint_to_npoint(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geom_to_nsegment(Pointer gs) {
		var _result = _meos_d.geom_to_nsegment(gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_to_geompoint(Pointer np) {
		var _result = _meos_d.npoint_to_geompoint(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_to_nsegment(Pointer np) {
		var _result = _meos_d.npoint_to_nsegment(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_to_stbox(Pointer np) {
		var _result = _meos_d.npoint_to_stbox(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nsegment_to_geom(Pointer ns) {
		var _result = _meos_d.nsegment_to_geom(ns);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nsegment_to_stbox(Pointer np) {
		var _result = _meos_d.nsegment_to_stbox(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int npoint_hash(Pointer np) {
		var _result = _meos_d.npoint_hash(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long npoint_hash_extended(Pointer np, long seed) {
		var _result = _meos_d.npoint_hash_extended(np, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double npoint_position(Pointer np) {
		var _result = _meos_d.npoint_position(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long npoint_route(Pointer np) {
		var _result = _meos_d.npoint_route(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nsegment_end_position(Pointer ns) {
		var _result = _meos_d.nsegment_end_position(ns);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long nsegment_route(Pointer ns) {
		var _result = _meos_d.nsegment_route(ns);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nsegment_start_position(Pointer ns) {
		var _result = _meos_d.nsegment_start_position(ns);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean route_exists(long rid) {
		var _result = _meos_d.route_exists(rid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer route_geom(long rid) {
		var _result = _meos_d.route_geom(rid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double route_length(long rid) {
		var _result = _meos_d.route_length(rid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_round(Pointer np, int maxdd) {
		var _result = _meos_d.npoint_round(np, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nsegment_round(Pointer ns, int maxdd) {
		var _result = _meos_d.nsegment_round(ns, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int get_srid_ways() {
		var _result = _meos_d.get_srid_ways();
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int npoint_srid(Pointer np) {
		var _result = _meos_d.npoint_srid(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nsegment_srid(Pointer ns) {
		var _result = _meos_d.nsegment_srid(ns);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_timestamptz_to_stbox(Pointer np, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.npoint_timestamptz_to_stbox(np, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_tstzspan_to_stbox(Pointer np, Pointer s) {
		var _result = _meos_d.npoint_tstzspan_to_stbox(np, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int npoint_cmp(Pointer np1, Pointer np2) {
		var _result = _meos_d.npoint_cmp(np1, np2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean npoint_eq(Pointer np1, Pointer np2) {
		var _result = _meos_d.npoint_eq(np1, np2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean npoint_ge(Pointer np1, Pointer np2) {
		var _result = _meos_d.npoint_ge(np1, np2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean npoint_gt(Pointer np1, Pointer np2) {
		var _result = _meos_d.npoint_gt(np1, np2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean npoint_le(Pointer np1, Pointer np2) {
		var _result = _meos_d.npoint_le(np1, np2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean npoint_lt(Pointer np1, Pointer np2) {
		var _result = _meos_d.npoint_lt(np1, np2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean npoint_ne(Pointer np1, Pointer np2) {
		var _result = _meos_d.npoint_ne(np1, np2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean npoint_same(Pointer np1, Pointer np2) {
		var _result = _meos_d.npoint_same(np1, np2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int nsegment_cmp(Pointer ns1, Pointer ns2) {
		var _result = _meos_d.nsegment_cmp(ns1, ns2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean nsegment_eq(Pointer ns1, Pointer ns2) {
		var _result = _meos_d.nsegment_eq(ns1, ns2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean nsegment_ge(Pointer ns1, Pointer ns2) {
		var _result = _meos_d.nsegment_ge(ns1, ns2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean nsegment_gt(Pointer ns1, Pointer ns2) {
		var _result = _meos_d.nsegment_gt(ns1, ns2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean nsegment_le(Pointer ns1, Pointer ns2) {
		var _result = _meos_d.nsegment_le(ns1, ns2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean nsegment_lt(Pointer ns1, Pointer ns2) {
		var _result = _meos_d.nsegment_lt(ns1, ns2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean nsegment_ne(Pointer ns1, Pointer ns2) {
		var _result = _meos_d.nsegment_ne(ns1, ns2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npointset_in(String str) {
		var _result = _meos_d.npointset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String npointset_out(Pointer s, int maxdd) {
		var _result = _meos_d.npointset_out(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npointset_make(Pointer values, int count) {
		var _result = _meos_d.npointset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_to_set(Pointer np) {
		var _result = _meos_d.npoint_to_set(np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npointset_end_value(Pointer s) {
		var _result = _meos_d.npointset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npointset_routes(Pointer s) {
		var _result = _meos_d.npointset_routes(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npointset_start_value(Pointer s) {
		var _result = _meos_d.npointset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npointset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.npointset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer npointset_values(Pointer s) {
		var _result = _meos_d.npointset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_npoint_set(Pointer np, Pointer s) {
		var _result = _meos_d.contained_npoint_set(np, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_npoint(Pointer s, Pointer np) {
		var _result = _meos_d.contains_set_npoint(s, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_npoint_set(Pointer np, Pointer s) {
		var _result = _meos_d.intersection_npoint_set(np, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_npoint(Pointer s, Pointer np) {
		var _result = _meos_d.intersection_set_npoint(s, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_npoint_set(Pointer np, Pointer s) {
		var _result = _meos_d.minus_npoint_set(np, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_npoint(Pointer s, Pointer np) {
		var _result = _meos_d.minus_set_npoint(s, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer npoint_union_transfn(Pointer state, Pointer np) {
		var _result = _meos_d.npoint_union_transfn(state, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_npoint_set(Pointer np, Pointer s) {
		var _result = _meos_d.union_npoint_set(np, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_npoint(Pointer s, Pointer np) {
		var _result = _meos_d.union_set_npoint(s, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_in(String str) {
		var _result = _meos_d.tnpoint_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_from_mfjson(String mfjson) {
		var _result = _meos_d.tnpoint_from_mfjson(mfjson);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String tnpoint_out(Pointer temp, int maxdd) {
		var _result = _meos_d.tnpoint_out(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpointinst_make(Pointer np, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tnpointinst_make(np, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tgeompoint_to_tnpoint(Pointer temp) {
		var _result = _meos_d.tgeompoint_to_tnpoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_to_tgeompoint(Pointer temp) {
		var _result = _meos_d.tnpoint_to_tgeompoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_cumulative_length(Pointer temp) {
		var _result = _meos_d.tnpoint_cumulative_length(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double tnpoint_length(Pointer temp) {
		var _result = _meos_d.tnpoint_length(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_positions(Pointer temp, Pointer count) {
		var _result = _meos_d.tnpoint_positions(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long tnpoint_route(Pointer temp) {
		var _result = _meos_d.tnpoint_route(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_routes(Pointer temp) {
		var _result = _meos_d.tnpoint_routes(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_speed(Pointer temp) {
		var _result = _meos_d.tnpoint_speed(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_trajectory(Pointer temp) {
		var _result = _meos_d.tnpoint_trajectory(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_twcentroid(Pointer temp) {
		var _result = _meos_d.tnpoint_twcentroid(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_at_geom(Pointer temp, Pointer gs) {
		var _result = _meos_d.tnpoint_at_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_at_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.tnpoint_at_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_at_npointset(Pointer temp, Pointer s) {
		var _result = _meos_d.tnpoint_at_npointset(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_at_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = _meos_d.tnpoint_at_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_minus_geom(Pointer temp, Pointer gs) {
		var _result = _meos_d.tnpoint_minus_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_minus_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.tnpoint_minus_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_minus_npointset(Pointer temp, Pointer s) {
		var _result = _meos_d.tnpoint_minus_npointset(temp, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_minus_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = _meos_d.tnpoint_minus_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.tdistance_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tnpoint_point(Pointer temp, Pointer gs) {
		var _result = _meos_d.tdistance_tnpoint_point(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tnpoint_tnpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.tdistance_tnpoint_tnpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tnpoint_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.nad_tnpoint_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.nad_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tnpoint_stbox(Pointer temp, Pointer box) {
		var _result = _meos_d.nad_tnpoint_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tnpoint_tnpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nad_tnpoint_tnpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tnpoint_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.nai_tnpoint_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.nai_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tnpoint_tnpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nai_tnpoint_tnpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tnpoint_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.shortestline_tnpoint_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.shortestline_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tnpoint_tnpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.shortestline_tnpoint_tnpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tnpoint_tcentroid_transfn(Pointer state, Pointer temp) {
		var _result = _meos_d.tnpoint_tcentroid_transfn(state, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_npoint_tnpoint(Pointer np, Pointer temp) {
		var _result = _meos_d.always_eq_npoint_tnpoint(np, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.always_eq_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tnpoint_tnpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.always_eq_tnpoint_tnpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_npoint_tnpoint(Pointer np, Pointer temp) {
		var _result = _meos_d.always_ne_npoint_tnpoint(np, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.always_ne_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tnpoint_tnpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.always_ne_tnpoint_tnpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_npoint_tnpoint(Pointer np, Pointer temp) {
		var _result = _meos_d.ever_eq_npoint_tnpoint(np, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.ever_eq_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tnpoint_tnpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.ever_eq_tnpoint_tnpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_npoint_tnpoint(Pointer np, Pointer temp) {
		var _result = _meos_d.ever_ne_npoint_tnpoint(np, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.ever_ne_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tnpoint_tnpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.ever_ne_tnpoint_tnpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.teq_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tnpoint_npoint(Pointer temp, Pointer np) {
		var _result = _meos_d.tne_tnpoint_npoint(temp, np);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String pose_as_ewkt(Pointer pose, int maxdd) {
		var _result = _meos_d.pose_as_ewkt(pose, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String pose_as_hexwkb(Pointer pose, byte variant, Pointer size) {
		var _result = _meos_d.pose_as_hexwkb(pose, variant, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String pose_as_text(Pointer pose, int maxdd) {
		var _result = _meos_d.pose_as_text(pose, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_as_wkb(Pointer pose, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		var _result = _meos_d.pose_as_wkb(pose, variant, size_out);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_from_wkb(Pointer wkb, long size) {
		var _result = _meos_d.pose_from_wkb(wkb, size);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_from_hexwkb(String hexwkb) {
		var _result = _meos_d.pose_from_hexwkb(hexwkb);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_in(String str) {
		var _result = _meos_d.pose_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String pose_out(Pointer pose, int maxdd) {
		var _result = _meos_d.pose_out(pose, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_copy(Pointer pose) {
		var _result = _meos_d.pose_copy(pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_make_2d(double x, double y, double theta, int srid) {
		var _result = _meos_d.pose_make_2d(x, y, theta, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_make_3d(double x, double y, double z, double W, double X, double Y, double Z, int srid) {
		var _result = _meos_d.pose_make_3d(x, y, z, W, X, Y, Z, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_make_point2d(Pointer gs, double theta) {
		var _result = _meos_d.pose_make_point2d(gs, theta);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_make_point3d(Pointer gs, double W, double X, double Y, double Z) {
		var _result = _meos_d.pose_make_point3d(gs, W, X, Y, Z);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_to_point(Pointer pose) {
		var _result = _meos_d.pose_to_point(pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_to_stbox(Pointer pose) {
		var _result = _meos_d.pose_to_stbox(pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int pose_hash(Pointer pose) {
		var _result = _meos_d.pose_hash(pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static long pose_hash_extended(Pointer pose, long seed) {
		var _result = _meos_d.pose_hash_extended(pose, seed);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_orientation(Pointer pose) {
		var _result = _meos_d.pose_orientation(pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double pose_rotation(Pointer pose) {
		var _result = _meos_d.pose_rotation(pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_round(Pointer pose, int maxdd) {
		var _result = _meos_d.pose_round(pose, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer posearr_round(Pointer posearr, int count, int maxdd) {
		var _result = _meos_d.posearr_round(posearr, count, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static void pose_set_srid(Pointer pose, int srid) {
		_meos_d.pose_set_srid(pose, srid);
		MeosErrorHandler.checkError();
	}

	@SuppressWarnings("unused")
	public static int pose_srid(Pointer pose) {
		var _result = _meos_d.pose_srid(pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_transform(Pointer pose, int srid) {
		var _result = _meos_d.pose_transform(pose, srid);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_transform_pipeline(Pointer pose, String pipelinestr, int srid, boolean is_forward) {
		var _result = _meos_d.pose_transform_pipeline(pose, pipelinestr, srid, is_forward);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_tstzspan_to_stbox(Pointer pose, Pointer s) {
		var _result = _meos_d.pose_tstzspan_to_stbox(pose, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_timestamptz_to_stbox(Pointer pose, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.pose_timestamptz_to_stbox(pose, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_pose_geo(Pointer pose, Pointer gs) {
		var _result = _meos_d.distance_pose_geo(pose, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_pose_pose(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.distance_pose_pose(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double distance_pose_stbox(Pointer pose, Pointer box) {
		var _result = _meos_d.distance_pose_stbox(pose, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int pose_cmp(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_cmp(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean pose_eq(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_eq(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean pose_ge(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_ge(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean pose_gt(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_gt(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean pose_le(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_le(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean pose_lt(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_lt(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean pose_ne(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_ne(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean pose_nsame(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_nsame(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean pose_same(Pointer pose1, Pointer pose2) {
		var _result = _meos_d.pose_same(pose1, pose2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer poseset_in(String str) {
		var _result = _meos_d.poseset_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String poseset_out(Pointer s, int maxdd) {
		var _result = _meos_d.poseset_out(s, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer poseset_make(Pointer values, int count) {
		var _result = _meos_d.poseset_make(values, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_to_set(Pointer pose) {
		var _result = _meos_d.pose_to_set(pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer poseset_end_value(Pointer s) {
		var _result = _meos_d.poseset_end_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer poseset_start_value(Pointer s) {
		var _result = _meos_d.poseset_start_value(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer poseset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.poseset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer poseset_values(Pointer s) {
		var _result = _meos_d.poseset_values(s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contained_pose_set(Pointer pose, Pointer s) {
		var _result = _meos_d.contained_pose_set(pose, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean contains_set_pose(Pointer s, Pointer pose) {
		var _result = _meos_d.contains_set_pose(s, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_pose_set(Pointer pose, Pointer s) {
		var _result = _meos_d.intersection_pose_set(pose, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer intersection_set_pose(Pointer s, Pointer pose) {
		var _result = _meos_d.intersection_set_pose(s, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_pose_set(Pointer pose, Pointer s) {
		var _result = _meos_d.minus_pose_set(pose, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer minus_set_pose(Pointer s, Pointer pose) {
		var _result = _meos_d.minus_set_pose(s, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer pose_union_transfn(Pointer state, Pointer pose) {
		var _result = _meos_d.pose_union_transfn(state, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_pose_set(Pointer pose, Pointer s) {
		var _result = _meos_d.union_pose_set(pose, s);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer union_set_pose(Pointer s, Pointer pose) {
		var _result = _meos_d.union_set_pose(s, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_in(String str) {
		var _result = _meos_d.tpose_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_make(Pointer tpoint, Pointer tradius) {
		var _result = _meos_d.tpose_make(tpoint, tradius);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_to_tpoint(Pointer temp) {
		var _result = _meos_d.tpose_to_tpoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_end_value(Pointer temp) {
		var _result = _meos_d.tpose_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_points(Pointer temp) {
		var _result = _meos_d.tpose_points(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_rotation(Pointer temp) {
		var _result = _meos_d.tpose_rotation(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_start_value(Pointer temp) {
		var _result = _meos_d.tpose_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_trajectory(Pointer temp) {
		var _result = _meos_d.tpose_trajectory(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static boolean tpose_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.tpose_value_at_timestamptz(temp, t_new, strict, value);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.tpose_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_values(Pointer temp, Pointer count) {
		var _result = _meos_d.tpose_values(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_at_geom(Pointer temp, Pointer gs) {
		var _result = _meos_d.tpose_at_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_at_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = _meos_d.tpose_at_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_at_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.tpose_at_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_minus_geom(Pointer temp, Pointer gs) {
		var _result = _meos_d.tpose_minus_geom(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_minus_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.tpose_minus_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tpose_minus_stbox(Pointer temp, Pointer box, boolean border_inc) {
		var _result = _meos_d.tpose_minus_stbox(temp, box, border_inc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.tdistance_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tpose_point(Pointer temp, Pointer gs) {
		var _result = _meos_d.tdistance_tpose_point(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_tpose_tpose(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.tdistance_tpose_tpose(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tpose_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.nad_tpose_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.nad_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tpose_stbox(Pointer temp, Pointer box) {
		var _result = _meos_d.nad_tpose_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_tpose_tpose(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nad_tpose_tpose(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tpose_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.nai_tpose_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.nai_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_tpose_tpose(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nai_tpose_tpose(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tpose_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.shortestline_tpose_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.shortestline_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_tpose_tpose(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.shortestline_tpose_tpose(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_pose_tpose(Pointer pose, Pointer temp) {
		var _result = _meos_d.always_eq_pose_tpose(pose, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.always_eq_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_tpose_tpose(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.always_eq_tpose_tpose(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_pose_tpose(Pointer pose, Pointer temp) {
		var _result = _meos_d.always_ne_pose_tpose(pose, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.always_ne_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_tpose_tpose(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.always_ne_tpose_tpose(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_pose_tpose(Pointer pose, Pointer temp) {
		var _result = _meos_d.ever_eq_pose_tpose(pose, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.ever_eq_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_tpose_tpose(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.ever_eq_tpose_tpose(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_pose_tpose(Pointer pose, Pointer temp) {
		var _result = _meos_d.ever_ne_pose_tpose(pose, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.ever_ne_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_tpose_tpose(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.ever_ne_tpose_tpose(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_pose_tpose(Pointer pose, Pointer temp) {
		var _result = _meos_d.teq_pose_tpose(pose, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.teq_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_pose_tpose(Pointer pose, Pointer temp) {
		var _result = _meos_d.tne_pose_tpose(pose, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_tpose_pose(Pointer temp, Pointer pose) {
		var _result = _meos_d.tne_tpose_pose(temp, pose);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String trgeo_out(Pointer temp) {
		var _result = _meos_d.trgeo_out(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeoinst_make(Pointer geom, Pointer pose, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.trgeoinst_make(geom, pose, t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer geo_tpose_to_trgeo(Pointer gs, Pointer temp) {
		var _result = _meos_d.geo_tpose_to_trgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_to_tpose(Pointer temp) {
		var _result = _meos_d.trgeo_to_tpose(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_to_tpoint(Pointer temp) {
		var _result = _meos_d.trgeo_to_tpoint(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_end_instant(Pointer temp) {
		var _result = _meos_d.trgeo_end_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_end_sequence(Pointer temp) {
		var _result = _meos_d.trgeo_end_sequence(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_end_value(Pointer temp) {
		var _result = _meos_d.trgeo_end_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_geom(Pointer temp) {
		var _result = _meos_d.trgeo_geom(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_instant_n(Pointer temp, int n) {
		var _result = _meos_d.trgeo_instant_n(temp, n);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_instants(Pointer temp, Pointer count) {
		var _result = _meos_d.trgeo_instants(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_points(Pointer temp) {
		var _result = _meos_d.trgeo_points(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_rotation(Pointer temp) {
		var _result = _meos_d.trgeo_rotation(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_segments(Pointer temp, Pointer count) {
		var _result = _meos_d.trgeo_segments(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_sequence_n(Pointer temp, int i) {
		var _result = _meos_d.trgeo_sequence_n(temp, i);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_sequences(Pointer temp, Pointer count) {
		var _result = _meos_d.trgeo_sequences(temp, count);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_start_instant(Pointer temp) {
		var _result = _meos_d.trgeo_start_instant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_start_sequence(Pointer temp) {
		var _result = _meos_d.trgeo_start_sequence(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_start_value(Pointer temp) {
		var _result = _meos_d.trgeo_start_value(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = _meos_d.trgeo_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		MeosErrorHandler.checkError();
		return out ? new_result : null;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_traversed_area(Pointer temp, boolean unary_union) {
		var _result = _meos_d.trgeo_traversed_area(temp, unary_union);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_append_tinstant(Pointer temp, Pointer inst, int interp, double maxdist, Pointer maxt, boolean expand) {
		var _result = _meos_d.trgeo_append_tinstant(temp, inst, interp, maxdist, maxt, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_append_tsequence(Pointer temp, Pointer seq, boolean expand) {
		var _result = _meos_d.trgeo_append_tsequence(temp, seq, expand);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_delete_timestamptz(Pointer temp, OffsetDateTime t, boolean connect) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.trgeo_delete_timestamptz(temp, t_new, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_delete_tstzset(Pointer temp, Pointer s, boolean connect) {
		var _result = _meos_d.trgeo_delete_tstzset(temp, s, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_delete_tstzspan(Pointer temp, Pointer s, boolean connect) {
		var _result = _meos_d.trgeo_delete_tstzspan(temp, s, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect) {
		var _result = _meos_d.trgeo_delete_tstzspanset(temp, ss, connect);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_round(Pointer temp, int maxdd) {
		var _result = _meos_d.trgeo_round(temp, maxdd);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_set_interp(Pointer temp, int interp) {
		var _result = _meos_d.trgeo_set_interp(temp, interp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_to_tinstant(Pointer temp) {
		var _result = _meos_d.trgeo_to_tinstant(temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_after_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.trgeo_after_timestamptz(temp, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_before_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.trgeo_before_timestamptz(temp, t_new, strict);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_restrict_value(Pointer temp, Pointer value, boolean atfunc) {
		var _result = _meos_d.trgeo_restrict_value(temp, value, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_restrict_values(Pointer temp, Pointer s, boolean atfunc) {
		var _result = _meos_d.trgeo_restrict_values(temp, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_restrict_timestamptz(Pointer temp, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.trgeo_restrict_timestamptz(temp, t_new, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_restrict_tstzset(Pointer temp, Pointer s, boolean atfunc) {
		var _result = _meos_d.trgeo_restrict_tstzset(temp, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_restrict_tstzspan(Pointer temp, Pointer s, boolean atfunc) {
		var _result = _meos_d.trgeo_restrict_tstzspan(temp, s, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer trgeo_restrict_tstzspanset(Pointer temp, Pointer ss, boolean atfunc) {
		var _result = _meos_d.trgeo_restrict_tstzspanset(temp, ss, atfunc);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.tdistance_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_trgeo_tpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.tdistance_trgeo_tpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tdistance_trgeo_trgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.tdistance_trgeo_trgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_stbox_trgeo(Pointer box, Pointer temp) {
		var _result = _meos_d.nad_stbox_trgeo(box, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.nad_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_trgeo_stbox(Pointer temp, Pointer box) {
		var _result = _meos_d.nad_trgeo_stbox(temp, box);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_trgeo_tpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nad_trgeo_tpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static double nad_trgeo_trgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nad_trgeo_trgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.nai_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_trgeo_tpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nai_trgeo_tpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer nai_trgeo_trgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.nai_trgeo_trgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.shortestline_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_trgeo_tpoint(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.shortestline_trgeo_tpoint(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer shortestline_trgeo_trgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.shortestline_trgeo_trgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_geo_trgeo(Pointer gs, Pointer temp) {
		var _result = _meos_d.always_eq_geo_trgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.always_eq_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_eq_trgeo_trgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.always_eq_trgeo_trgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_geo_trgeo(Pointer gs, Pointer temp) {
		var _result = _meos_d.always_ne_geo_trgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.always_ne_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int always_ne_trgeo_trgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.always_ne_trgeo_trgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_geo_trgeo(Pointer gs, Pointer temp) {
		var _result = _meos_d.ever_eq_geo_trgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.ever_eq_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_eq_trgeo_trgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.ever_eq_trgeo_trgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_geo_trgeo(Pointer gs, Pointer temp) {
		var _result = _meos_d.ever_ne_geo_trgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.ever_ne_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int ever_ne_trgeo_trgeo(Pointer temp1, Pointer temp2) {
		var _result = _meos_d.ever_ne_trgeo_trgeo(temp1, temp2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_geo_trgeo(Pointer gs, Pointer temp) {
		var _result = _meos_d.teq_geo_trgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer teq_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.teq_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_geo_trgeo(Pointer gs, Pointer temp) {
		var _result = _meos_d.tne_geo_trgeo(gs, temp);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer tne_trgeo_geo(Pointer temp, Pointer gs) {
		var _result = _meos_d.tne_trgeo_geo(temp, gs);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int geo_get_srid(Pointer g) {
		var _result = _meos_d.geo_get_srid(g);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int date_in(String str) {
		var _result = _meos_d.date_in(str);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String date_out(int d) {
		var _result = _meos_d.date_out(d);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static int interval_cmp(Pointer interv1, Pointer interv2) {
		var _result = _meos_d.interval_cmp(interv1, interv2);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer interval_in(String str, int typmod) {
		var _result = _meos_d.interval_in(str, typmod);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String interval_out(Pointer interv) {
		var _result = _meos_d.interval_out(interv);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static Pointer time_in(String str, int typmod) {
		var _result = _meos_d.time_in(str, typmod);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static String time_out(Pointer t) {
		var _result = _meos_d.time_out(t);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static LocalDateTime timestamp_in(String str, int typmod) {
		var _result = _meos_d.timestamp_in(str, typmod);
		MeosErrorHandler.checkError();
		return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(_result), java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static String timestamp_out(LocalDateTime t) {
		var t_new = t.toInstant(java.time.ZoneOffset.UTC).getEpochSecond();
		var _result = _meos_d.timestamp_out(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_in(String str, int typmod) {
		var _result = _meos_d.timestamptz_in(str, typmod);
		MeosErrorHandler.checkError();
		return java.time.Instant.ofEpochSecond(_result).atOffset(java.time.ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public static String timestamptz_out(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		var _result = _meos_d.timestamptz_out(t_new);
		MeosErrorHandler.checkError();
		return _result;
	}

}
