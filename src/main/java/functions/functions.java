package functions;

import jnr.ffi.Pointer;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;
import jnr.ffi.byref.PointerByReference;
import jnr.ffi.Struct;
import utils.JarLibraryLoader;
import utils.meosCatalog.MeosEnums.meosType;
import utils.meosCatalog.MeosEnums.meosOper;

import java.time.*;

public class functions {
	public interface MeosLibrary {

	    String libraryPath = "libmeos.so";

		MeosLibrary INSTANCE = JarLibraryLoader.create(MeosLibrary.class, libraryPath).getLibraryInstance();

		MeosLibrary meos = MeosLibrary.INSTANCE;

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

		double float_round(double d, int maxdd);

		Pointer minus_date_date(int d1, int d2);

		int minus_date_int(int d, int days);

		long minus_timestamptz_interval(long t, Pointer interv);

		Pointer minus_timestamptz_timestamptz(long t1, long t2);

		Pointer mult_interval_double(Pointer interv, double factor);

		String text2cstring(Pointer txt);

		int text_cmp(Pointer txt1, Pointer txt2);

		Pointer text_copy(Pointer txt);

		Pointer text_initcap(Pointer txt);

		Pointer text_lower(Pointer txt);

		String text_out(Pointer txt);

		Pointer text_upper(Pointer txt);

		Pointer textcat_text_text(Pointer txt1, Pointer txt2);

		int timestamp_to_date(long t);

		int timestamptz_to_date(long t);

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

		Pointer set_round(Pointer s, int maxdd);

		String span_as_hexwkb(Pointer s, byte variant, Pointer size_out);

		Pointer span_as_wkb(Pointer s, byte variant, Pointer size_out);

		Pointer span_from_hexwkb(String hexwkb);

		Pointer span_from_wkb(Pointer wkb, long size);

		String spanset_as_hexwkb(Pointer ss, byte variant, Pointer size_out);

		Pointer spanset_as_wkb(Pointer ss, byte variant, Pointer size_out);

		Pointer spanset_from_hexwkb(String hexwkb);

		Pointer spanset_from_wkb(Pointer wkb, long size);

		String spatialset_as_ewkt(Pointer set, int maxdd);

		String spatialset_as_text(Pointer set, int maxdd);

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

		Pointer bigint_set(long i);

		Pointer bigint_span(int i);

		Pointer bigint_spanset(int i);

		Pointer date_set(int d);

		Pointer date_span(int d);

		Pointer date_spanset(int d);

		Pointer dateset_tstzset(Pointer s);

		Pointer datespan_tstzspan(Pointer s);

		Pointer datespanset_tstzspanset(Pointer ss);

		Pointer float_set(double d);

		Pointer float_span(double d);

		Pointer float_spanset(double d);

		Pointer floatset_intset(Pointer s);

		Pointer floatspan_intspan(Pointer s);

		Pointer floatspanset_intspanset(Pointer ss);

		Pointer int_set(int i);

		Pointer int_span(int i);

		Pointer int_spanset(int i);

		Pointer intset_floatset(Pointer s);

		Pointer intspan_floatspan(Pointer s);

		Pointer intspanset_floatspanset(Pointer ss);

		Pointer set_spanset(Pointer s);

		Pointer span_spanset(Pointer s);

		Pointer text_set(Pointer txt);

		Pointer timestamptz_set(long t);

		Pointer timestamptz_span(long t);

		Pointer timestamptz_spanset(long t);

		Pointer tstzset_dateset(Pointer s);

		Pointer tstzspan_datespan(Pointer s);

		Pointer tstzspanset_datespanset(Pointer ss);

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

		Pointer set_span(Pointer s);

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

		Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer floatspan_ceil(Pointer s);

		Pointer floatspan_floor(Pointer s);

		Pointer floatspan_degrees(Pointer s, boolean normalize);

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

		Pointer set_spans(Pointer s);

		Pointer set_split_each_n_spans(Pointer s, int elem_count, Pointer count);

		Pointer set_split_n_spans(Pointer s, int span_count, Pointer count);

		Pointer spanset_spans(Pointer ss);

		Pointer spanset_split_each_n_spans(Pointer ss, int elem_count, Pointer count);

		Pointer spanset_split_n_spans(Pointer ss, int span_count, Pointer count);

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

		Pointer tbox_in(String str);

		String tbox_out(Pointer box, int maxdd);

		Pointer tbox_from_wkb(Pointer wkb, long size);

		Pointer tbox_from_hexwkb(String hexwkb);

		Pointer tbox_as_wkb(Pointer box, byte variant, Pointer size_out);

		String tbox_as_hexwkb(Pointer box, byte variant, Pointer size);

		Pointer float_tstzspan_to_tbox(double d, Pointer s);

		Pointer float_timestamptz_to_tbox(double d, long t);

		Pointer int_tstzspan_to_tbox(int i, Pointer s);

		Pointer int_timestamptz_to_tbox(int i, long t);

		Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s);

		Pointer numspan_timestamptz_to_tbox(Pointer span, long t);

		Pointer tbox_copy(Pointer box);

		Pointer tbox_make(Pointer s, Pointer p);

		Pointer float_tbox(double d);

		Pointer int_tbox(int i);

		Pointer set_tbox(Pointer s);

		Pointer span_tbox(Pointer s);

		Pointer spanset_tbox(Pointer ss);

		Pointer tbox_intspan(Pointer box);

		Pointer tbox_floatspan(Pointer box);

		Pointer tbox_tstzspan(Pointer box);

		Pointer timestamptz_tbox(long t);

		Pointer tnumber_tbox(Pointer temp);

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

		Pointer tbox_expand_float(Pointer box, double d);

		Pointer tbox_expand_int(Pointer box, int i);

		Pointer tbox_round(Pointer box, int maxdd);

		Pointer tbox_shift_scale_float(Pointer box, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer tbox_shift_scale_int(Pointer box, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);

		Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict);

		Pointer intersection_tbox_tbox(Pointer box1, Pointer box2);

		boolean adjacent_tbox_tbox(Pointer box1, Pointer box2);

		boolean contained_tbox_tbox(Pointer box1, Pointer box2);

		boolean contains_tbox_tbox(Pointer box1, Pointer box2);

		boolean overlaps_tbox_tbox(Pointer box1, Pointer box2);

		boolean same_tbox_tbox(Pointer box1, Pointer box2);

		boolean left_tbox_tbox(Pointer box1, Pointer box2);

		boolean overleft_tbox_tbox(Pointer box1, Pointer box2);

		boolean right_tbox_tbox(Pointer box1, Pointer box2);

		boolean overright_tbox_tbox(Pointer box1, Pointer box2);

		boolean before_tbox_tbox(Pointer box1, Pointer box2);

		boolean overbefore_tbox_tbox(Pointer box1, Pointer box2);

		boolean after_tbox_tbox(Pointer box1, Pointer box2);

		boolean overafter_tbox_tbox(Pointer box1, Pointer box2);

		boolean tbox_eq(Pointer box1, Pointer box2);

		boolean tbox_ne(Pointer box1, Pointer box2);

		int tbox_cmp(Pointer box1, Pointer box2);

		boolean tbox_lt(Pointer box1, Pointer box2);

		boolean tbox_le(Pointer box1, Pointer box2);

		boolean tbox_ge(Pointer box1, Pointer box2);

		boolean tbox_gt(Pointer box1, Pointer box2);

		Pointer tbool_in(String str);

		Pointer tint_in(String str);

		Pointer tfloat_in(String str);

		Pointer ttext_in(String str);

		Pointer tbool_from_mfjson(String str);

		Pointer tint_from_mfjson(String str);

		Pointer tfloat_from_mfjson(String str);

		Pointer ttext_from_mfjson(String str);

		Pointer temporal_from_wkb(Pointer wkb, long size);

		Pointer temporal_from_hexwkb(String hexwkb);

		String tbool_out(Pointer temp);

		String tint_out(Pointer temp);

		String tfloat_out(Pointer temp, int maxdd);

		String ttext_out(Pointer temp);

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

		Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize);

		Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist);

		Pointer ttext_from_base_temp(Pointer txt, Pointer temp);

		Pointer ttextinst_make(Pointer txt, long t);

		Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s);

		Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s);

		Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss);

		Pointer temporal_tstzspan(Pointer temp);

		Pointer tbool_tint(Pointer temp);

		Pointer tfloat_tint(Pointer temp);

		Pointer tint_tfloat(Pointer temp);

		Pointer tnumber_span(Pointer temp);

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

		boolean temporal_lower_inc(Pointer temp);

		boolean temporal_upper_inc(Pointer temp);

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

		Pointer ttext_end_value(Pointer temp);

		Pointer ttext_max_value(Pointer temp);

		Pointer ttext_min_value(Pointer temp);

		Pointer ttext_start_value(Pointer temp);

		boolean ttext_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean ttext_value_n(Pointer temp, int n, Pointer result);

		Pointer ttext_values(Pointer temp, Pointer count);

		int spheroid_init_from_srid(int srid, Pointer s);

		boolean ensure_srid_is_latlong(int srid);

		double float_degrees(double value, boolean normalize);

		boolean srid_is_latlong(int srid);

		Pointer temparr_round(Pointer temp, int count, int maxdd);

		Pointer temporal_round(Pointer temp, int maxdd);

		Pointer temporal_scale_time(Pointer temp, Pointer duration);

		Pointer temporal_set_interp(Pointer temp, String interp_str);

		Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration);

		Pointer temporal_shift_time(Pointer temp, Pointer shift);

		Pointer temporal_to_tinstant(Pointer temp);

		Pointer temporal_to_tsequence(Pointer temp, String interp_str);

		Pointer temporal_to_tsequenceset(Pointer temp, String interp_str);

		Pointer tfloat_floor(Pointer temp);

		Pointer tfloat_ceil(Pointer temp);

		Pointer tfloat_degrees(Pointer temp, boolean normalize);

		Pointer tfloat_radians(Pointer temp);

		Pointer tfloat_scale_value(Pointer temp, double width);

		Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width);

		Pointer tfloat_shift_value(Pointer temp, double shift);

		Pointer tint_scale_value(Pointer temp, int width);

		Pointer tint_shift_scale_value(Pointer temp, int shift, int width);

		Pointer tint_shift_value(Pointer temp, int shift);

		Pointer temporal_append_tinstant(Pointer temp, Pointer inst, int interp, double maxdist, Pointer maxt, boolean expand);

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

		int always_ne_bool_tbool(boolean b, Pointer temp);

		int always_ne_float_tfloat(double d, Pointer temp);

		int always_ne_int_tint(int i, Pointer temp);

		int always_ne_tbool_bool(Pointer temp, boolean b);

		int always_ne_temporal_temporal(Pointer temp1, Pointer temp2);

		int always_ne_text_ttext(Pointer txt, Pointer temp);

		int always_ne_tfloat_float(Pointer temp, double d);

		int always_ne_tint_int(Pointer temp, int i);

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

		Pointer tnumber_tboxes(Pointer temp, Pointer count);

		Pointer tnumber_split_each_n_tboxes(Pointer temp, int elem_count, Pointer count);

		Pointer tnumber_split_n_tboxes(Pointer temp, int box_count, Pointer count);

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

		Pointer tfloat_exp(Pointer temp);

		Pointer tfloat_ln(Pointer temp);

		Pointer tfloat_log10(Pointer temp);

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

		int nad_tint_int(Pointer temp, int i);

		int nad_tint_tbox(Pointer temp, Pointer box);

		int nad_tint_tint(Pointer temp1, Pointer temp2);

		int nad_tboxint_tboxint(Pointer box1, Pointer box2);

		double nad_tfloat_float(Pointer temp, double d);

		double nad_tfloat_tfloat(Pointer temp1, Pointer temp2);

		double nad_tfloat_tbox(Pointer temp, Pointer box);

		double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2);

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

		long bigint_get_bin(long value, long vsize, long vorigin);

		Pointer bigintspan_bins(Pointer s, long vsize, long vorigin, Pointer count);

		Pointer bigintspanset_bins(Pointer ss, long vsize, long vorigin, Pointer count);

		Pointer bigintspanset_value_spans(Pointer ss, long vsize, long vorigin, Pointer count);

		int date_get_bin(int d, Pointer duration, int torigin);

		Pointer datespan_bins(Pointer s, Pointer duration, int torigin, Pointer count);

		Pointer datespanset_bins(Pointer ss, Pointer duration, int torigin, Pointer count);

		Pointer datespanset_time_spans(Pointer ss, Pointer duration, int torigin, Pointer count);

		double float_get_bin(double value, double vsize, double vorigin);

		Pointer floatspan_bins(Pointer s, double vsize, double vorigin, Pointer count);

		Pointer floatspanset_value_spans(Pointer ss, double vsize, double vorigin, Pointer count);

		int int_get_bin(int value, int vsize, int vorigin);

		Pointer intspan_bins(Pointer s, int vsize, int vorigin, Pointer count);

		Pointer intspanset_bins(Pointer ss, int vsize, int vorigin, Pointer count);

		Pointer intspanset_value_spans(Pointer ss, int vsize, int vorigin, Pointer count);

		long timestamptz_get_bin(long t, Pointer duration, long torigin);

		Pointer tstzspan_bins(Pointer s, Pointer duration, long origin, Pointer count);

		Pointer tstzspanset_bins(Pointer ss, Pointer duration, long torigin, Pointer count);

		Pointer tstzspanset_time_spans(Pointer ss, Pointer duration, long torigin, Pointer count);

		Pointer temporal_time_spans(Pointer temp, Pointer duration, long origin, Pointer count);

		Pointer temporal_time_split(Pointer temp, Pointer duration, long torigin, Pointer time_bins, Pointer count);

		Pointer tfloat_value_spans(Pointer temp, double vsize, double vorigin, Pointer count);

		Pointer tfloat_value_split(Pointer temp, double vsize, double vorigin, Pointer value_bins, Pointer count);

		Pointer tfloat_value_time_split(Pointer temp, double vsize, Pointer duration, double vorigin, long torigin, Pointer value_bins, Pointer time_bins, Pointer count);

		Pointer tfloatbox_get_time_tile(long t, Pointer duration, long torigin);

		Pointer tfloatbox_get_value_tile(double value, double vsize, double vorigin);

		Pointer tfloatbox_get_value_time_tile(double value, long t, double vsize, Pointer duration, double vorigin, long torigin);

		Pointer tfloatbox_time_tiles(Pointer box, Pointer duration, long torigin, Pointer count);

		Pointer tfloatbox_value_tiles(Pointer box, double vsize, double vorigin, Pointer count);

		Pointer tfloatbox_value_time_tiles(Pointer box, double vsize, Pointer duration, double vorigin, long torigin, Pointer count);

		Pointer tint_value_spans(Pointer temp, int vsize, int vorigin, Pointer count);

		Pointer tint_value_split(Pointer temp, int vsize, int vorigin, Pointer value_bins, Pointer count);

		Pointer tint_value_time_split(Pointer temp, int size, Pointer duration, int vorigin, long torigin, Pointer value_bins, Pointer time_bins, Pointer count);

		Pointer tintbox_get_time_tile(long t, Pointer duration, long torigin);

		Pointer tintbox_get_value_tile(int value, int vsize, int vorigin);

		Pointer tintbox_get_value_time_tile(int value, long t, int vsize, Pointer duration, int vorigin, long torigin);

		Pointer tintbox_time_tiles(Pointer box, Pointer duration, long torigin, Pointer count);

		Pointer tintbox_value_tiles(Pointer box, int xsize, int xorigin, Pointer count);

		Pointer tintbox_value_time_tiles(Pointer box, int xsize, Pointer duration, int xorigin, long torigin, Pointer count);

		Pointer geo_as_ewkb(Pointer gs, String endian, Pointer size);

		String geo_as_ewkt(Pointer gs, int precision);

		String geo_as_geojson(Pointer gs, int option, int precision, String srs);

		String geo_as_hexewkb(Pointer gs, String endian);

		String geo_as_text(Pointer gs, int precision);

		Pointer geo_collect_garray(Pointer gsarr, int count);

		Pointer geo_copy(Pointer g);

		int geo_equals(Pointer gs1, Pointer gs2);

		Pointer geo_from_ewkb(Pointer wkb, long wkb_size, int srid);

		Pointer geo_from_geojson(String geojson);

		Pointer geo_from_text(String wkt, int srid);

		boolean geo_is_empty(Pointer g);

		Pointer geo_makeline_garray(Pointer gsarr, int count);

		String geo_out(Pointer gs);

		Pointer geo_reverse(Pointer gs);

		Pointer geo_round(Pointer gs, int maxdd);

		boolean geo_same(Pointer gs1, Pointer gs2);

		Pointer  geo_set_srid(Pointer gs, int srid);

		int geo_srid(Pointer gs);

		Pointer geo_transform(Pointer geom, int srid_to);

		Pointer geo_transform_pipeline(Pointer gs, String pipeline, int srid_to, boolean is_forward);

		double geog_area(Pointer g, boolean use_spheroid);

		Pointer geog_centroid(Pointer g, boolean use_spheroid);

		double geog_distance(Pointer g1, Pointer g2);

		boolean geog_dwithin(Pointer g1, Pointer g2, double tolerance, boolean use_spheroid);

		Pointer geog_from_binary(String wkb_bytea);

		Pointer geog_from_geom(Pointer geom);

		Pointer geog_from_hexewkb(String wkt);

		Pointer geog_in(String str, int typmod);

		boolean geog_intersects(Pointer gs1, Pointer gs2, boolean use_spheroid);

		double geog_length(Pointer g, boolean use_spheroid);

		double geog_perimeter(Pointer g, boolean use_spheroid);

		Pointer geom_array_union(Pointer gsarr, int count);

		boolean geom_azimuth(Pointer gs1, Pointer gs2, Pointer result);

		Pointer geom_boundary(Pointer gs);

		Pointer geom_buffer(Pointer gs, double size, String params);

		Pointer geom_centroid(Pointer gs);

		boolean geom_contains(Pointer gs1, Pointer gs2);

		Pointer geom_convex_hull(Pointer gs);

		boolean geom_covers(Pointer gs1, Pointer gs2);

		Pointer geom_difference2d(Pointer gs1, Pointer gs2);

		boolean geom_disjoint2d(Pointer gs1, Pointer gs2);

		double geom_distance2d(Pointer gs1, Pointer gs2);

		double geom_distance3d(Pointer gs1, Pointer gs2);

		boolean geom_dwithin2d(Pointer gs1, Pointer gs2, double tolerance);

		boolean geom_dwithin3d(Pointer gs1, Pointer gs2, double tolerance);

		Pointer geom_from_geog(Pointer geog);

		Pointer geom_from_hexewkb(String wkt);

		Pointer geom_from_text(String wkt, int srid);

		Pointer geom_in(String str, int typmod);

		Pointer geom_intersection2d(Pointer gs1, Pointer gs2);

		boolean geom_intersects2d(Pointer gs1, Pointer gs2);

		boolean geom_intersects3d(Pointer gs1, Pointer gs2);

		double geom_length(Pointer gs);

		double geom_perimeter(Pointer gs);

		boolean geom_relate_pattern(Pointer gs1, Pointer gs2, String patt);

		Pointer geom_shortestline2d(Pointer gs1, Pointer s2);

		Pointer geom_shortestline3d(Pointer gs1, Pointer s2);

		boolean geom_touches(Pointer gs1, Pointer gs2);

		Pointer geom_unary_union(Pointer gs, double prec);

		Pointer line_interpolate_point(Pointer gs, double distance_fraction, boolean repeat);

		int line_numpoints(Pointer gs);

		double line_locate_point(Pointer gs1, Pointer gs2);

		Pointer line_point_n(Pointer geom, int n);

		Pointer line_substring(Pointer gs, double from, double to);

		Pointer geo_stboxes(Pointer gs, Pointer count);

		Pointer geo_split_n_stboxes(Pointer gs, int box_count, Pointer count);

		boolean contained_geo_set(Pointer gs, Pointer s);

		boolean contains_set_geo(Pointer s, Pointer gs);

		Pointer geo_set(Pointer gs);

		Pointer geoset_end_value(Pointer s);

		Pointer geoset_make(Pointer values, int count);

		Pointer geoset_start_value(Pointer s);

		boolean geoset_value_n(Pointer s, int n, Pointer result);

		Pointer geoset_values(Pointer s);

		Pointer intersection_geo_set(Pointer gs, Pointer s);

		Pointer intersection_set_geo(Pointer s, Pointer gs);

		Pointer minus_geo_set(Pointer gs, Pointer s);

		Pointer minus_set_geo(Pointer s, Pointer gs);

		int spatialset_srid(Pointer s);

		Pointer spatialset_set_srid(Pointer s, int srid);

		Pointer spatialset_transform(Pointer s, int srid);

		Pointer spatialset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward);

		Pointer union_geo_set(Pointer gs, Pointer s);

		Pointer union_set_geo(Pointer s, Pointer gs);

		Pointer stbox_from_wkb(Pointer wkb, long size);

		Pointer stbox_from_hexwkb(String hexwkb);

		Pointer stbox_as_wkb(Pointer box, byte variant, Pointer size_out);

		String stbox_as_hexwkb(Pointer box, byte variant, Pointer size);

		Pointer stbox_in(String str);

		String stbox_out(Pointer box, int maxdd);

		Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s);

		Pointer geo_timestamptz_to_stbox(Pointer gs, long t);

		Pointer stbox_copy(Pointer box);

		Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s);

		Pointer geo_stbox(Pointer gs);

		Pointer stbox_geo(Pointer box);

		Pointer spatialset_stbox(Pointer s);

		Pointer stbox_gbox(Pointer box);

		Pointer stbox_box3d(Pointer box);

		Pointer stbox_tstzspan(Pointer box);

		Pointer timestamptz_stbox(long t);

		Pointer tstzset_stbox(Pointer s);

		Pointer tstzspan_stbox(Pointer s);

		Pointer tstzspanset_stbox(Pointer ss);

		Pointer tspatial_stbox(Pointer temp);

		double stbox_area(Pointer box, boolean spheroid);

		boolean stbox_hast(Pointer box);

		boolean stbox_hasx(Pointer box);

		boolean stbox_hasz(Pointer box);

		boolean stbox_isgeodetic(Pointer box);

		double stbox_perimeter(Pointer box, boolean spheroid);

		int stbox_srid(Pointer box);

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

		Pointer stbox_set_srid(Pointer box, int srid);

		Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);

		Pointer stbox_transform(Pointer box, int srid);

		Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward);

		Pointer stboxarr_round(Pointer boxarr, int count, int maxdd);

		Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict);

		Pointer intersection_stbox_stbox(Pointer box1, Pointer box2);

		boolean adjacent_stbox_stbox(Pointer box1, Pointer box2);

		boolean contained_stbox_stbox(Pointer box1, Pointer box2);

		boolean contains_stbox_stbox(Pointer box1, Pointer box2);

		boolean overlaps_stbox_stbox(Pointer box1, Pointer box2);

		boolean same_stbox_stbox(Pointer box1, Pointer box2);

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

		boolean stbox_eq(Pointer box1, Pointer box2);

		boolean stbox_ne(Pointer box1, Pointer box2);

		int stbox_cmp(Pointer box1, Pointer box2);

		boolean stbox_lt(Pointer box1, Pointer box2);

		boolean stbox_le(Pointer box1, Pointer box2);

		boolean stbox_ge(Pointer box1, Pointer box2);

		boolean stbox_gt(Pointer box1, Pointer box2);

		Pointer rtree_create_stbox();

		void rtree_insert(Pointer rtree, Pointer box, long id);

		Pointer rtree_search(Pointer rtree,Pointer query, Pointer count);

		void rtree_free(Pointer rtree);

		String tgeo_out(Pointer temp, int maxdd);

		String tspatial_as_text(Pointer temp, int maxdd);

		String tspatial_as_ewkt(Pointer temp, int maxdd);

		Pointer tgeogpoint_in(String str);

		Pointer tgeogpoint_from_mfjson(String str);

		Pointer tgeography_in(String str);

		Pointer tgeometry_in(String str);

		Pointer tgeompoint_in(String str);

		Pointer tgeompoint_from_mfjson(String str);

		Pointer geogset_in(String str);

		Pointer geomset_in(String str);

		Pointer tgeo_from_base_temp(Pointer gs, Pointer temp);

		Pointer tgeoinst_make(Pointer gs, long t);

		Pointer tgeoseq_from_base_tstzset(Pointer gs, Pointer s);

		Pointer tgeoseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp);

		Pointer tpoint_from_base_temp(Pointer gs, Pointer temp);

		Pointer tpointinst_make(Pointer gs, long t);

		Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp);

		Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s);

		Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp);

		Pointer tgeo_end_value(Pointer temp);

		Pointer tgeo_start_value(Pointer temp);

		boolean tgeo_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer value);

		boolean tgeo_value_n(Pointer temp, int n, Pointer result);

		Pointer tgeo_values(Pointer temp, Pointer count);

		Pointer tgeo_traversed_area(Pointer temp);

		Pointer tgeo_centroid(Pointer temp);

		int tspatial_srid(Pointer temp);

		Pointer tspatial_set_srid(Pointer temp, int srid);

		Pointer tspatial_transform(Pointer temp, int srid);

		Pointer tspatial_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward);

		Pointer tgeo_at_geom(Pointer temp, Pointer gs);

		Pointer tgeo_at_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tgeo_minus_geom(Pointer temp, Pointer gs);

		Pointer tgeo_minus_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tpoint_at_geom(Pointer temp, Pointer gs, Pointer zspan);

		Pointer tpoint_at_stbox(Pointer temp, Pointer box, boolean border_inc);

		Pointer tpoint_at_value(Pointer temp, Pointer gs);

		Pointer tpoint_minus_geom(Pointer temp, Pointer gs, Pointer zspan);

		Pointer tpoint_minus_stbox(Pointer temp, Pointer box, boolean border_inc);

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

		Pointer geo_split_each_n_stboxes(Pointer gs, int elem_count, Pointer count);

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

		boolean bearing_point_point(Pointer gs1, Pointer gs2, Pointer result);

		Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert);

		Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2);

		Pointer geo_gboxes(Pointer gs, Pointer count);

		Pointer tpoint_angular_difference(Pointer temp);

		Pointer tpoint_azimuth(Pointer temp);

		Pointer tgeo_convex_hull(Pointer temp);

		Pointer tpoint_cumulative_length(Pointer temp);

		boolean tpoint_direction(Pointer temp, Pointer result);

		Pointer tpoint_get_x(Pointer temp);

		Pointer tpoint_get_y(Pointer temp);

		Pointer tpoint_get_z(Pointer temp);

		boolean tpoint_is_simple(Pointer temp);

		double tpoint_length(Pointer temp);

		Pointer tpoint_speed(Pointer temp);

		Pointer tpoint_trajectory(Pointer temp);

		Pointer tpoint_twcentroid(Pointer temp);

		Pointer geo_expand_space(Pointer gs, double d);

		Pointer geomeas_tpoint(Pointer gs);

		Pointer tgeogpoint_tgeography(Pointer temp);

		Pointer tgeography_tgeometry(Pointer temp);

		Pointer tgeography_tgeogpoint(Pointer temp);

		Pointer tgeometry_tgeography(Pointer temp);

		Pointer tgeometry_tgeompoint(Pointer temp);

		Pointer tgeompoint_tgeometry(Pointer temp);

		Pointer tgeo_affine(Pointer temp, Pointer a);

		boolean tpoint_AsMVTGeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count);

		Pointer tspatial_expand_space(Pointer temp, double d);

		Pointer tpoint_make_simple(Pointer temp, Pointer count);

		Pointer tgeo_scale(Pointer temp, Pointer scale, Pointer sorigin);

		boolean tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize, Pointer result);

		int acontains_geo_tgeo(Pointer gs, Pointer temp);

		int adisjoint_tgeo_geo(Pointer temp, Pointer gs);

		int adisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int adwithin_tgeo_geo(Pointer temp, Pointer gs, double dist);

		int adwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist);

		int aintersects_tgeo_geo(Pointer temp, Pointer gs);

		int aintersects_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int atouches_tgeo_geo(Pointer temp, Pointer gs);

		int econtains_geo_tgeo(Pointer gs, Pointer temp);

		int edisjoint_tgeo_geo(Pointer temp, Pointer gs);

		int edisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int edwithin_tgeo_geo(Pointer temp, Pointer gs, double dist);

		int edwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist);

		int eintersects_tgeo_geo(Pointer temp, Pointer gs);

		int eintersects_tgeo_tgeo(Pointer temp1, Pointer temp2);

		int etouches_tgeo_geo(Pointer temp, Pointer gs);

		Pointer tcontains_geo_tgeo(Pointer gs, Pointer temp, boolean restr, boolean atvalue);

		Pointer tdisjoint_tgeo_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);

		Pointer tdisjoint_tgeo_tgeo (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue);

		Pointer tdwithin_tgeo_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue);

		Pointer tdwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue);

		Pointer tintersects_tgeo_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);

		Pointer tintersects_tgeo_tgeo (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue);

		Pointer ttouches_tgeo_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);

		double nad_stbox_geo(Pointer box, Pointer gs);

		double nad_stbox_stbox(Pointer box1, Pointer box2);

		double nad_tgeo_geo(Pointer temp, Pointer gs);

		double nad_tgeo_stbox(Pointer temp, Pointer box);

		double nad_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer nai_tgeo_geo(Pointer temp, Pointer gs);

		Pointer nai_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer shortestline_tgeo_geo(Pointer temp, Pointer gs);

		Pointer shortestline_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer distance_tgeo_geo(Pointer temp, Pointer gs);

		Pointer distance_tgeo_tgeo(Pointer temp1, Pointer temp2);

		Pointer tpoint_tcentroid_finalfn(Pointer state);

		Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp);

		Pointer tspatial_extent_transfn(Pointer box, Pointer temp);

		Pointer stbox_get_space_tile(Pointer point, double xsize, double ysize, double zsize, Pointer sorigin);

		Pointer stbox_get_space_time_tile(Pointer point, long t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin);

		Pointer stbox_get_time_tile(long t, Pointer duration, long torigin);

		Pointer stbox_space_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer sorigin, boolean border_inc, Pointer count);

		Pointer stbox_space_time_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean border_inc, Pointer count);

		Pointer stbox_time_tiles(Pointer bounds, Pointer duration, long torigin, boolean border_inc, Pointer count);

		Pointer tpoint_space_split(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer count);

		Pointer tgeo_space_time_split(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer time_bins, Pointer count);

		Pointer tgeo_time_split(Pointer temp, Pointer duration, long torigin, boolean border_inc, Pointer time_bins, Pointer count);

		int int4_in(String str);

		String int4_out(int val);

		long int8_in(String str);

		String int8_out(long val);

		String float8_out(double num, int maxdd);

		double pg_dsin(double arg1);

		double pg_dcos(double arg1);

		double pg_datan(double arg1);

		double pg_datan2(double arg1, double arg2);

		int pg_date_in(String str);

		String pg_date_out(int d);

		int pg_interval_cmp(Pointer interv1, Pointer interv2);

		Pointer pg_interval_in(String str, int prec);

		Pointer pg_interval_justify_hours(Pointer span);

		String pg_interval_out(Pointer interv);

		long pg_time_in(String str, int typmod);

		String pg_time_out(long t);

		long pg_timestamp_in(String str, int typmod);

		String pg_timestamp_out(long t);

		long pg_timestamptz_in(String str, int prec);

		String pg_timestamptz_out(long t);

		int hash_bytes_uint32(int k);

		int pg_hashint8(long val);

		int pg_hashfloat8(double key);

		long hash_bytes_uint32_extended(int k, long seed);

		long pg_hashint8extended(long val, long seed);

		long pg_hashfloat8extended(double key, long seed);

		int pg_hashtext(Pointer key);

		long pg_hashtextextended(Pointer key, long seed);

		Pointer gsl_get_generation_rng();

		Pointer gsl_get_aggregation_rng();

		Pointer proj_get_context();

		long datum_floor(long d);

		long datum_ceil(long d);

		long datum_degrees(long d, long normalize);

		long datum_radians(long d);

		int datum_hash(long d, meosType basetype);

		long datum_hash_extended(long d, meosType basetype, long seed);

		long datum_float_round(long value, long size);

		long datum_geo_round(long value, long size);

		Pointer point_round(Pointer gs, int maxdd);

		void floatspan_round_set(Pointer s, int maxdd, Pointer result);

		Pointer SET_BBOX_PTR(Pointer s);

		Pointer SET_OFFSETS_PTR(Pointer s);

		long SET_VAL_N(Pointer s, int index);

		Pointer SPANSET_SP_N(Pointer ss, int index);

		Pointer TSEQUENCE_OFFSETS_PTR(Pointer seq);

		Pointer TSEQUENCE_INST_N(Pointer seq, int index);

		Pointer TSEQUENCESET_OFFSETS_PTR(Pointer ss);

		Pointer TSEQUENCESET_SEQ_N(Pointer ss, int index);

		Pointer set_in(String str, meosType basetype);

		String set_out(Pointer s, int maxdd);

		Pointer span_in(String str, meosType spantype);

		String span_out(Pointer s, int maxdd);

		Pointer spanset_in(String str, meosType spantype);

		String spanset_out(Pointer ss, int maxdd);

		Pointer set_make(Pointer values, int count, meosType basetype, boolean order);

		Pointer set_make_exp(Pointer values, int count, int maxcount, meosType basetype, boolean order);

		Pointer set_make_free(Pointer values, int count, meosType basetype, boolean order);

		Pointer span_make(long lower, long upper, boolean lower_inc, boolean upper_inc, meosType basetype);

		void span_set(long lower, long upper, boolean lower_inc, boolean upper_inc, meosType basetype, meosType spantype, Pointer s);

		Pointer spanset_make_exp(Pointer spans, int count, int maxcount, boolean normalize, boolean order);

		Pointer spanset_make_free(Pointer spans, int count, boolean normalize, boolean order);

		void value_set_span(long value, meosType basetype, Pointer s);

		Pointer value_set(long d, meosType basetype);

		Pointer value_span(long d, meosType basetype);

		Pointer value_spanset(long d, meosType basetype);

		long numspan_width(Pointer s);

		long numspanset_width(Pointer ss, boolean boundspan);

		long set_end_value(Pointer s);

		int set_mem_size(Pointer s);

		void set_set_subspan(Pointer s, int minidx, int maxidx, Pointer result);

		void set_set_span(Pointer s, Pointer result);

		long set_start_value(Pointer s);

		boolean set_value_n(Pointer s, int n, Pointer result);

		Pointer set_vals(Pointer s);

		Pointer set_values(Pointer s);

		long spanset_lower(Pointer ss);

		int spanset_mem_size(Pointer ss);

		Pointer spanset_sps(Pointer ss);

		long spanset_upper(Pointer ss);

		void datespan_set_tstzspan(Pointer s1, Pointer s2);

		void floatspan_set_intspan(Pointer s1, Pointer s2);

		void intspan_set_floatspan(Pointer s1, Pointer s2);

		Pointer numset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer numspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer numspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer set_compact(Pointer s);

		void span_expand(Pointer s1, Pointer s2);

		Pointer spanset_compact(Pointer ss);

		Pointer textcat_textset_text_int(Pointer s, Pointer txt, boolean invert);

		void tstzspan_set_datespan(Pointer s1, Pointer s2);

		boolean adjacent_span_value(Pointer s, long value);

		boolean adjacent_spanset_value(Pointer ss, long value);

		boolean adjacent_value_spanset(long value, Pointer ss);

		boolean contained_value_set(long value, Pointer s);

		boolean contained_value_span(long value, Pointer s);

		boolean contained_value_spanset(long value, Pointer ss);

		boolean contains_set_value(Pointer s, long value);

		boolean contains_span_value(Pointer s, long value);

		boolean contains_spanset_value(Pointer ss, long value);

		boolean ovadj_span_span(Pointer s1, Pointer s2);

		boolean left_set_value(Pointer s, long value);

		boolean left_span_value(Pointer s, long value);

		boolean left_spanset_value(Pointer ss, long value);

		boolean left_value_set(long value, Pointer s);

		boolean left_value_span(long value, Pointer s);

		boolean left_value_spanset(long value, Pointer ss);

		boolean lfnadj_span_span(Pointer s1, Pointer s2);

		boolean overleft_set_value(Pointer s, long value);

		boolean overleft_span_value(Pointer s, long value);

		boolean overleft_spanset_value(Pointer ss, long value);

		boolean overleft_value_set(long value, Pointer s);

		boolean overleft_value_span(long value, Pointer s);

		boolean overleft_value_spanset(long value, Pointer ss);

		boolean overright_set_value(Pointer s, long value);

		boolean overright_span_value(Pointer s, long value);

		boolean overright_spanset_value(Pointer ss, long value);

		boolean overright_value_set(long value, Pointer s);

		boolean overright_value_span(long value, Pointer s);

		boolean overright_value_spanset(long value, Pointer ss);

		boolean right_value_set(long value, Pointer s);

		boolean right_set_value(Pointer s, long value);

		boolean right_value_span(long value, Pointer s);

		boolean right_value_spanset(long value, Pointer ss);

		boolean right_span_value(Pointer s, long value);

		boolean right_spanset_value(Pointer ss, long value);

		void bbox_union_span_span(Pointer s1, Pointer s2, Pointer result);

		boolean inter_span_span(Pointer s1, Pointer s2, Pointer result);

		Pointer intersection_set_value(Pointer s, long value);

		Pointer intersection_span_value(Pointer s, long value);

		Pointer intersection_spanset_value(Pointer ss, long value);

		Pointer intersection_value_set(long value, Pointer s);

		Pointer intersection_value_span(long value, Pointer s);

		Pointer intersection_value_spanset(long value, Pointer ss);

		int mi_span_span(Pointer s1, Pointer s2, Pointer result);

		Pointer minus_set_value(Pointer s, long value);

		Pointer minus_span_value(Pointer s, long value);

		Pointer minus_spanset_value(Pointer ss, long value);

		Pointer minus_value_set(long value, Pointer s);

		Pointer minus_value_span(long value, Pointer s);

		Pointer minus_value_spanset(long value, Pointer ss);

		Pointer super_union_span_span(Pointer s1, Pointer s2);

		Pointer union_set_value(Pointer s, long value);

		Pointer union_span_value(Pointer s, long value);

		Pointer union_spanset_value(Pointer ss, long value);

		Pointer union_value_set(long value, Pointer s);

		Pointer union_value_span(long value, Pointer s);

		Pointer union_value_spanset(long value, Pointer ss);

		long distance_set_set(Pointer s1, Pointer s2);

		long distance_set_value(Pointer s, long value);

		long distance_span_span(Pointer s1, Pointer s2);

		long distance_span_value(Pointer s, long value);

		long distance_spanset_span(Pointer ss, Pointer s);

		long distance_spanset_spanset(Pointer ss1, Pointer ss2);

		long distance_spanset_value(Pointer ss, long value);

		long distance_value_value(long l, long r, meosType basetype);

		Pointer spanbase_extent_transfn(Pointer state, long value, meosType basetype);

		Pointer value_union_transfn(Pointer state, long value, meosType basetype);

		Pointer number_tstzspan_to_tbox(long d, meosType basetype, Pointer s);

		Pointer number_timestamptz_to_tbox(long d, meosType basetype, long t);

		void stbox_set(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s, Pointer box);

		void tbox_set(Pointer s, Pointer p, Pointer box);

		Pointer box3d_stbox(Pointer box);

		Pointer gbox_stbox(Pointer box);

		void float_set_tbox(double d, Pointer box);

		void gbox_set_stbox(Pointer box, int srid, Pointer result);

		boolean geo_set_stbox(Pointer gs, Pointer box);

		void geoarr_set_stbox(Pointer values, int count, Pointer box);

		void int_set_tbox(int i, Pointer box);

		void number_set_tbox(long d, meosType basetype, Pointer box);

		Pointer number_tbox(long value, meosType basetype);

		void numset_set_tbox(Pointer s, Pointer box);

		void numspan_set_tbox(Pointer span, Pointer box);

		void numspanset_set_tbox(Pointer ss, Pointer box);

		boolean spatial_set_stbox(long d, meosType basetype, Pointer box);

		void spatialset_set_stbox(Pointer set, Pointer box);

		void stbox_set_box3d(Pointer box, Pointer box3d);

		void stbox_set_gbox(Pointer box, Pointer gbox);

		void timestamptz_set_stbox(long t, Pointer box);

		void timestamptz_set_tbox(long t, Pointer box);

		void tstzset_set_stbox(Pointer s, Pointer box);

		void tstzset_set_tbox(Pointer s, Pointer box);

		void tstzspan_set_stbox(Pointer s, Pointer box);

		void tstzspan_set_tbox(Pointer s, Pointer box);

		void tstzspanset_set_stbox(Pointer ss, Pointer box);

		void tstzspanset_set_tbox(Pointer ss, Pointer box);

		Pointer tbox_shift_scale_value(Pointer box, long shift, long width, boolean hasshift, boolean haswidth);

		void stbox_expand(Pointer box1, Pointer box2);

		void tbox_expand(Pointer box1, Pointer box2);

		boolean inter_stbox_stbox(Pointer box1, Pointer box2, Pointer result);

		boolean inter_tbox_tbox(Pointer box1, Pointer box2, Pointer result);

		String tboolinst_as_mfjson(Pointer inst, boolean with_bbox);

		Pointer tboolinst_from_mfjson(Pointer mfjson);

		Pointer tboolinst_in(String str);

		String tboolseq_as_mfjson(Pointer seq, boolean with_bbox);

		Pointer tboolseq_from_mfjson(Pointer mfjson);

		Pointer tboolseq_in(String str, int interp);

		String tboolseqset_as_mfjson(Pointer ss, boolean with_bbox);

		Pointer tboolseqset_from_mfjson(Pointer mfjson);

		Pointer tboolseqset_in(String str);

		Pointer temporal_in(String str, meosType temptype);

		String temporal_out(Pointer temp, int maxdd);

		Pointer temparr_out(Pointer temparr, int count, int maxdd);

		String tfloatinst_as_mfjson(Pointer inst, boolean with_bbox, int precision);

		Pointer tfloatinst_from_mfjson(Pointer mfjson);

		Pointer tfloatinst_in(String str);

		String tfloatseq_as_mfjson(Pointer seq, boolean with_bbox, int precision);

		Pointer tfloatseq_from_mfjson(Pointer mfjson, int interp);

		Pointer tfloatseq_in(String str, int interp);

		String tfloatseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision);

		Pointer tfloatseqset_from_mfjson(Pointer mfjson, int interp);

		Pointer tfloatseqset_in(String str);

		String tgeoinst_as_mfjson(Pointer inst, boolean with_bbox, int precision, String srs);

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

		String tgeoseq_as_mfjson(Pointer seq, boolean with_bbox, int precision, String srs);

		String tgeoseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision, String srs);

		Pointer tinstant_from_mfjson(Pointer mfjson, boolean isgeo, int srid, meosType temptype);

		Pointer tinstant_in(String str, meosType temptype);

		String tinstant_out(Pointer inst, int maxdd);

		String tintinst_as_mfjson(Pointer inst, boolean with_bbox);

		Pointer tintinst_from_mfjson(Pointer mfjson);

		Pointer tintinst_in(String str);

		String tintseq_as_mfjson(Pointer seq, boolean with_bbox);

		Pointer tintseq_from_mfjson(Pointer mfjson);

		Pointer tintseq_in(String str, int interp);

		String tintseqset_as_mfjson(Pointer ss, boolean with_bbox);

		Pointer tintseqset_from_mfjson(Pointer mfjson);

		Pointer tintseqset_in(String str);

		String tpointinst_as_mfjson(Pointer inst, boolean with_bbox, int precision, String srs);

		String tpointseq_as_mfjson(Pointer seq, boolean with_bbox, int precision, String srs);

		String tpointseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision, String srs);

		Pointer tsequence_from_mfjson(Pointer mfjson, boolean isgeo, int srid, meosType temptype, int interp);

		Pointer tsequence_in(String str, meosType temptype, int interp);

		String tsequence_out(Pointer seq, int maxdd);

		Pointer tsequenceset_from_mfjson(Pointer mfjson, boolean isgeo, int srid, meosType temptype, int interp);

		Pointer tsequenceset_in(String str, meosType temptype, int interp);

		String tsequenceset_out(Pointer ss, int maxdd);

		String ttextinst_as_mfjson(Pointer inst, boolean with_bbox);

		Pointer ttextinst_from_mfjson(Pointer mfjson);

		Pointer ttextinst_in(String str);

		String ttextseq_as_mfjson(Pointer seq, boolean with_bbox);

		Pointer ttextseq_from_mfjson(Pointer mfjson);

		Pointer ttextseq_in(String str, int interp);

		String ttextseqset_as_mfjson(Pointer ss, boolean with_bbox);

		Pointer ttextseqset_from_mfjson(Pointer mfjson);

		Pointer ttextseqset_in(String str);

		Pointer temporal_from_mfjson(String mfjson, meosType temptype);

		Pointer temporal_from_base_temp(long value, meosType temptype, Pointer temp);

		Pointer tinstant_copy(Pointer inst);

		Pointer tinstant_make(long value, meosType temptype, long t);

		Pointer tinstant_make_free(long value, meosType temptype, long t);

		Pointer tpointseq_make_coords(Pointer xcoords, Pointer ycoords, Pointer zcoords, Pointer times, int count, int srid, boolean geodetic, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequence_copy(Pointer seq);

		Pointer tsequence_from_base_tstzset(long value, meosType temptype, Pointer ss);

		Pointer tsequence_from_base_tstzspan(long value, meosType temptype, Pointer s, int interp);

		Pointer tsequence_make_exp(Pointer instants, int count, int maxcount, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequence_make_free(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequenceset_copy(Pointer ss);

		Pointer tseqsetarr_to_tseqset(Pointer seqsets, int count, int totalseqs);

		Pointer tsequenceset_from_base_tstzspanset(long value, meosType temptype, Pointer ss, int interp);

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

		long temporal_end_value(Pointer temp);

		Pointer temporal_inst_n(Pointer temp, int n);

		Pointer temporal_instants_p(Pointer temp, Pointer count);

		long temporal_max_value(Pointer temp);

		long temporal_mem_size(Pointer temp);

		long temporal_min_value(Pointer temp);

		Pointer temporal_sequences_p(Pointer temp, Pointer count);

		void temporal_set_bbox(Pointer temp, Pointer box);

		Pointer temporal_start_inst(Pointer temp);

		long temporal_start_value(Pointer temp);

		Pointer temporal_values_p(Pointer temp, Pointer count);

		boolean temporal_value_n(Pointer temp, int n, Pointer result);

		Pointer temporal_values(Pointer temp, Pointer count);

		int tinstant_hash(Pointer inst);

		Pointer tinstant_insts(Pointer inst, Pointer count);

		void tinstant_set_bbox(Pointer inst, Pointer box);

		Pointer tinstant_time(Pointer inst);

		Pointer tinstant_timestamps(Pointer inst, Pointer count);

		long tinstant_value_p(Pointer inst);

		long tinstant_value(Pointer inst);

		boolean tinstant_value_at_timestamptz(Pointer inst, long t, Pointer result);

		Pointer tinstant_values_p(Pointer inst, Pointer count);

		void tnumber_set_span(Pointer temp, Pointer span);

		Pointer tnumberinst_valuespans(Pointer inst);

		Pointer tnumberseq_valuespans(Pointer seq);

		Pointer tnumberseqset_valuespans(Pointer ss);

		Pointer tsequence_duration(Pointer seq);

		long tsequence_end_timestamptz(Pointer seq);

		int tsequence_hash(Pointer seq);

		Pointer tsequence_instants_p(Pointer seq);

		Pointer tsequence_max_inst(Pointer seq);

		long tsequence_max_val(Pointer seq);

		Pointer tsequence_min_inst(Pointer seq);

		long tsequence_min_val(Pointer seq);

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

		Pointer tsequenceset_insts(Pointer ss);

		Pointer tsequenceset_max_inst(Pointer ss);

		long tsequenceset_max_val(Pointer ss);

		Pointer tsequenceset_min_inst(Pointer ss);

		long tsequenceset_min_val(Pointer ss);

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

		Pointer tnumber_shift_scale_value(Pointer temp, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer tnumberinst_shift_value(Pointer inst, long shift);

		Pointer tnumberseq_shift_scale_value(Pointer seq, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer tnumberseqset_shift_scale_value(Pointer ss, long start, long width, boolean hasshift, boolean haswidth);

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

		void tspatial_set_stbox(Pointer temp, Pointer box);

		void tgeoinst_set_stbox(Pointer inst, Pointer box);

		void tspatialseq_set_stbox(Pointer seq, Pointer box);

		void tspatialseqset_set_stbox(Pointer ss, Pointer box);

		void tsequence_expand_bbox(Pointer seq, Pointer inst);

		void tsequence_set_bbox(Pointer seq, Pointer box);

		void tsequenceset_expand_bbox(Pointer ss, Pointer seq);

		void tsequenceset_set_bbox(Pointer ss, Pointer box);

		Pointer tdiscseq_restrict_minmax(Pointer seq, boolean min, boolean atfunc);

		Pointer tcontseq_restrict_minmax(Pointer seq, boolean min, boolean atfunc);

		boolean temporal_bbox_restrict_set(Pointer temp, Pointer set);

		Pointer temporal_restrict_minmax(Pointer temp, boolean min, boolean atfunc);

		Pointer temporal_restrict_timestamptz(Pointer temp, long t, boolean atfunc);

		Pointer temporal_restrict_tstzset(Pointer temp, Pointer s, boolean atfunc);

		Pointer temporal_restrict_tstzspan(Pointer temp, Pointer s, boolean atfunc);

		Pointer temporal_restrict_tstzspanset(Pointer temp, Pointer ss, boolean atfunc);

		Pointer temporal_restrict_value(Pointer temp, long value, boolean atfunc);

		Pointer temporal_restrict_values(Pointer temp, Pointer set, boolean atfunc);

		boolean temporal_value_at_timestamptz(Pointer temp, long t, boolean strict, Pointer result);

		Pointer tinstant_restrict_tstzspan(Pointer inst, Pointer period, boolean atfunc);

		Pointer tinstant_restrict_tstzspanset(Pointer inst, Pointer ss, boolean atfunc);

		Pointer tinstant_restrict_timestamptz(Pointer inst, long t, boolean atfunc);

		Pointer tinstant_restrict_tstzset(Pointer inst, Pointer s, boolean atfunc);

		Pointer tinstant_restrict_value(Pointer inst, long value, boolean atfunc);

		Pointer tinstant_restrict_values(Pointer inst, Pointer set, boolean atfunc);

		Pointer tgeo_restrict_geom(Pointer temp, Pointer gs, Pointer zspan, boolean atfunc);

		Pointer tgeo_restrict_stbox(Pointer temp, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tgeoinst_restrict_geom(Pointer inst, Pointer gs, Pointer zspan, boolean atfunc);

		Pointer tgeoinst_restrict_stbox(Pointer inst, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tgeoseq_restrict_geom(Pointer seq, Pointer gs, Pointer zspan, boolean atfunc);

		Pointer tgeotseq_restrict_stbox(Pointer seq, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tgeoseqset_restrict_geom(Pointer ss, Pointer gs, Pointer zspan, boolean atfunc);

		Pointer tgeoseqset_restrict_stbox(Pointer ss, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tnumber_restrict_span(Pointer temp, Pointer span, boolean atfunc);

		Pointer tnumber_restrict_spanset(Pointer temp, Pointer ss, boolean atfunc);

		Pointer tnumberinst_restrict_span(Pointer inst, Pointer span, boolean atfunc);

		Pointer tnumberinst_restrict_spanset(Pointer inst, Pointer ss, boolean atfunc);

		Pointer tnumberseqset_restrict_span(Pointer ss, Pointer span, boolean atfunc);

		Pointer tnumberseqset_restrict_spanset(Pointer ss, Pointer spanset, boolean atfunc);

		Pointer tsequence_at_timestamptz(Pointer seq, long t);

		Pointer tsequence_restrict_tstzspan(Pointer seq, Pointer s, boolean atfunc);

		Pointer tsequence_restrict_tstzspanset(Pointer seq, Pointer ss, boolean atfunc);

		Pointer tsequenceset_restrict_minmax(Pointer ss, boolean min, boolean atfunc);

		Pointer tsequenceset_restrict_tstzspan(Pointer ss, Pointer s, boolean atfunc);

		Pointer tsequenceset_restrict_tstzspanset(Pointer ss, Pointer ps, boolean atfunc);

		Pointer tsequenceset_restrict_timestamptz(Pointer ss, long t, boolean atfunc);

		Pointer tsequenceset_restrict_tstzset(Pointer ss, Pointer s, boolean atfunc);

		Pointer tsequenceset_restrict_value(Pointer ss, long value, boolean atfunc);

		Pointer tsequenceset_restrict_values(Pointer ss, Pointer s, boolean atfunc);

		int tinstant_cmp(Pointer inst1, Pointer inst2);

		boolean tinstant_eq(Pointer inst1, Pointer inst2);

		int tsequence_cmp(Pointer seq1, Pointer seq2);

		boolean tsequence_eq(Pointer seq1, Pointer seq2);

		int tsequenceset_cmp(Pointer ss1, Pointer ss2);

		boolean tsequenceset_eq(Pointer ss1, Pointer ss2);

		int always_eq_base_temporal(long value, Pointer temp);

		int always_eq_temporal_base(Pointer temp, long value);

		int always_ne_base_temporal(long value, Pointer temp);

		int always_ne_temporal_base(Pointer temp, long value);

		int always_ge_base_temporal(long value, Pointer temp);

		int always_ge_temporal_base(Pointer temp, long value);

		int always_gt_base_temporal(long value, Pointer temp);

		int always_gt_temporal_base(Pointer temp, long value);

		int always_le_base_temporal(long value, Pointer temp);

		int always_le_temporal_base(Pointer temp, long value);

		int always_lt_base_temporal(long value, Pointer temp);

		int always_lt_temporal_base(Pointer temp, long value);

		int ever_eq_base_temporal(long value, Pointer temp);

		int ever_eq_temporal_base(Pointer temp, long value);

		int ever_ne_base_temporal(long value, Pointer temp);

		int ever_ne_temporal_base(Pointer temp, long value);

		int ever_ge_base_temporal(long value, Pointer temp);

		int ever_ge_temporal_base(Pointer temp, long value);

		int ever_gt_base_temporal(long value, Pointer temp);

		int ever_gt_temporal_base(Pointer temp, long value);

		int ever_le_base_temporal(long value, Pointer temp);

		int ever_le_temporal_base(Pointer temp, long value);

		int ever_lt_base_temporal(long value, Pointer temp);

		int ever_lt_temporal_base(Pointer temp, long value);

		Pointer tfloatseq_derivative(Pointer seq);

		Pointer tfloatseqset_derivative(Pointer ss);

		Pointer tnumberinst_abs(Pointer inst);

		Pointer tnumberseq_abs(Pointer seq);

		Pointer tnumberseq_angular_difference(Pointer seq);

		Pointer tnumberseq_delta_value(Pointer seq);

		Pointer tnumberseqset_abs(Pointer ss);

		Pointer tnumberseqset_angular_difference(Pointer ss);

		Pointer tnumberseqset_delta_value(Pointer ss);

		Pointer distance_tnumber_number(Pointer temp, long value);

		long nad_tbox_tbox(Pointer box1, Pointer box2);

		long nad_tnumber_number(Pointer temp, long value);

		long nad_tnumber_tbox(Pointer temp, Pointer box);

		long nad_tnumber_tnumber(Pointer temp1, Pointer temp2);

		int spatial_srid(long d, meosType basetype);

		boolean spatial_has_z(long d, meosType basetype);

		boolean spatial_is_geodetic(long d, meosType basetype);

		boolean spatial_set_srid(long d, meosType basetype, int srid);

		int tspatialinst_srid(Pointer inst);

		Pointer tpointseq_azimuth(Pointer seq);

		Pointer tpointseq_cumulative_length(Pointer seq, double prevlength);

		boolean tpointseq_is_simple(Pointer seq);

		double tpointseq_length(Pointer seq);

		Pointer tpointseq_linear_trajectory(Pointer seq, boolean unary_union);

		Pointer tpointseq_speed(Pointer seq);

		Pointer tgeoseq_stboxes(Pointer seq, Pointer count);

		Pointer tpointseq_split_n_stboxes(Pointer seq, int max_count, Pointer count);

		Pointer tpointseqset_azimuth(Pointer ss);

		Pointer tpointseqset_cumulative_length(Pointer ss);

		boolean tpointseqset_is_simple(Pointer ss);

		double tpointseqset_length(Pointer ss);

		Pointer tpointseqset_speed(Pointer ss);

		Pointer tgeoseqset_stboxes(Pointer ss, Pointer count);

		Pointer tpointseqset_split_n_stboxes(Pointer ss, int max_count, Pointer count);

		Pointer tpointseqset_trajectory(Pointer ss);

		Pointer tpoint_get_coord(Pointer temp, int coord);

		Pointer tgeominst_tgeoginst(Pointer inst, boolean oper);

		Pointer tgeomseq_tgeogseq(Pointer seq, boolean oper);

		Pointer tgeomseqset_tgeogseqset(Pointer ss, boolean oper);

		Pointer tgeom_tgeog(Pointer temp, boolean oper);

		Pointer tgeo_tpoint(Pointer temp, boolean oper);

		Pointer tgeompoint_tnpoint(Pointer temp);

		Pointer tnpoint_tgeompoint(Pointer temp);

		void tspatialinst_set_srid(Pointer inst, int srid);

		Pointer tpointseq_make_simple(Pointer seq, Pointer count);

		void tpointseq_set_srid(Pointer seq, int srid);

		Pointer tpointseqset_make_simple(Pointer ss, Pointer count);

		void tpointseqset_set_srid(Pointer ss, int srid);

		double tnumberseq_integral(Pointer seq);

		double tnumberseq_twavg(Pointer seq);

		double tnumberseqset_integral(Pointer ss);

		double tnumberseqset_twavg(Pointer ss);

		Pointer tpointseq_twcentroid(Pointer seq);

		Pointer tpointseqset_twcentroid(Pointer ss);

		Pointer temporal_compact(Pointer temp);

		Pointer tsequence_compact(Pointer seq);

		Pointer tsequenceset_compact(Pointer ss);

		void skiplist_free(Pointer list);

		Pointer temporal_app_tinst_transfn(Pointer state, Pointer inst, int interp, double maxdist, Pointer maxt);

		Pointer temporal_app_tseq_transfn(Pointer state, Pointer seq);

		Pointer numspanset_spans(Pointer ss, long vsize, long vorigin, Pointer count);

		Pointer spanset_time_spans(Pointer ss, Pointer duration, long torigin, Pointer count);

		Pointer spanset_value_spans(Pointer ss, long vsize, long vorigin, Pointer count);

		Pointer timespanset_spans(Pointer ss, Pointer duration, long torigin, Pointer count);

		Pointer tnumber_value_spans(Pointer temp, long size, long origin, Pointer count);

		Pointer tnumber_value_boxes(Pointer temp, long vsize, long vorigin, Pointer count);

		Pointer tnumber_time_boxes(Pointer temp, Pointer duration, long torigin, Pointer count);

		Pointer tnumber_value_time_boxes(Pointer temp, long vsize, Pointer duration, long vorigin, long torigin, Pointer count);

		Pointer tnumber_value_split(Pointer temp, long vsize, long vorigin, Pointer bins, Pointer count);

		Pointer tbox_get_value_time_tile(long value, long t, long vsize, Pointer duration, long vorigin, long torigin, meosType basetype, meosType spantype);

		Pointer tnumber_value_time_split(Pointer temp, long size, Pointer duration, long vorigin, long torigin, Pointer value_bins, Pointer time_bins, Pointer count);

		int date_in(String str);

		String date_out(int d);

		int interval_cmp(Pointer interv1, Pointer interv2);

		Pointer interval_in(String str, int typmod);

		Pointer interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs);

		String interval_out(Pointer interv);

		double pg_exp(double arg1);

		double pg_ln(double arg1);

		double pg_log10(double arg1);

		long time_in(String str, int typmod);

		String time_out(long t);

		long timestamp_in(String str, int typmod);

		String timestamp_out(long t);

		long timestamptz_in(String str, int typmod);

		String timestamptz_out(long t);

	}

	@SuppressWarnings("unused")
	public static void meos_error(int errlevel, int errcode, String format, Pointer args) {
		MeosLibrary.meos.meos_error(errlevel, errcode, format, args);
	}
	
	@SuppressWarnings("unused")
	public static int meos_errno() {
		return MeosLibrary.meos.meos_errno();
	}
	
	@SuppressWarnings("unused")
	public static int meos_errno_set(int err) {
		return MeosLibrary.meos.meos_errno_set(err);
	}
	
	@SuppressWarnings("unused")
	public static int meos_errno_restore(int err) {
		return MeosLibrary.meos.meos_errno_restore(err);
	}
	
	@SuppressWarnings("unused")
	public static int meos_errno_reset() {
		return MeosLibrary.meos.meos_errno_reset();
	}
	
	@SuppressWarnings("unused")
	public static void meos_initialize_timezone(String name) {
		MeosLibrary.meos.meos_initialize_timezone(name);
	}
	
	@SuppressWarnings("unused")
	public static void meos_initialize_error_handler(error_handler_fn err_handler) {
		MeosLibrary.meos.meos_initialize_error_handler(err_handler);
	}
	
	@SuppressWarnings("unused")
	public static void meos_finalize_timezone() {
		MeosLibrary.meos.meos_finalize_timezone();
	}
	
	@SuppressWarnings("unused")
	public static boolean meos_set_datestyle(String newval, Pointer extra) {
		return MeosLibrary.meos.meos_set_datestyle(newval, extra);
	}
	
	@SuppressWarnings("unused")
	public static boolean meos_set_intervalstyle(String newval, int extra) {
		return MeosLibrary.meos.meos_set_intervalstyle(newval, extra);
	}
	
	@SuppressWarnings("unused")
	public static String meos_get_datestyle() {
		return MeosLibrary.meos.meos_get_datestyle();
	}
	
	@SuppressWarnings("unused")
	public static String meos_get_intervalstyle() {
		return MeosLibrary.meos.meos_get_intervalstyle();
	}
	
	@SuppressWarnings("unused")
	public static void meos_initialize() {
		MeosLibrary.meos.meos_initialize();
	}
	
	@SuppressWarnings("unused")
	public static void meos_finalize() {
		MeosLibrary.meos.meos_finalize();
	}
	
	@SuppressWarnings("unused")
	public static int add_date_int(int d, int days) {
		return MeosLibrary.meos.add_date_int(d, days);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_interval_interval(Pointer interv1, Pointer interv2) {
		return MeosLibrary.meos.add_interval_interval(interv1, interv2);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime add_timestamptz_interval(OffsetDateTime t, Pointer interv) {
		var t_new = t.toEpochSecond();
		var result = MeosLibrary.meos.add_timestamptz_interval(t_new, interv);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static boolean bool_in(String str) {
		return MeosLibrary.meos.bool_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String bool_out(boolean b) {
		return MeosLibrary.meos.bool_out(b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer cstring2text(String str) {
		return MeosLibrary.meos.cstring2text(str);
	}
	
	@SuppressWarnings("unused")
	public static LocalDateTime date_to_timestamp(int dateVal) {
		var result = MeosLibrary.meos.date_to_timestamp(dateVal);
		return LocalDateTime.ofEpochSecond(result, 0, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime date_to_timestamptz(int d) {
		var result = MeosLibrary.meos.date_to_timestamptz(d);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static double float_round(double d, int maxdd) {
		return MeosLibrary.meos.float_round(d, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_date_date(int d1, int d2) {
		return MeosLibrary.meos.minus_date_date(d1, d2);
	}
	
	@SuppressWarnings("unused")
	public static int minus_date_int(int d, int days) {
		return MeosLibrary.meos.minus_date_int(d, days);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime minus_timestamptz_interval(OffsetDateTime t, Pointer interv) {
		var t_new = t.toEpochSecond();
		var result = MeosLibrary.meos.minus_timestamptz_interval(t_new, interv);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_timestamptz(OffsetDateTime t1, OffsetDateTime t2) {
		var t1_new = t1.toEpochSecond();
		var t2_new = t2.toEpochSecond();
		return MeosLibrary.meos.minus_timestamptz_timestamptz(t1_new, t2_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_interval_double(Pointer interv, double factor) {
		return MeosLibrary.meos.mult_interval_double(interv, factor);
	}
	
	@SuppressWarnings("unused")
	public static String text2cstring(Pointer txt) {
		return MeosLibrary.meos.text2cstring(txt);
	}
	
	@SuppressWarnings("unused")
	public static int text_cmp(Pointer txt1, Pointer txt2) {
		return MeosLibrary.meos.text_cmp(txt1, txt2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_copy(Pointer txt) {
		return MeosLibrary.meos.text_copy(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_initcap(Pointer txt) {
		return MeosLibrary.meos.text_initcap(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_lower(Pointer txt) {
		return MeosLibrary.meos.text_lower(txt);
	}
	
	@SuppressWarnings("unused")
	public static String text_out(Pointer txt) {
		return MeosLibrary.meos.text_out(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_upper(Pointer txt) {
		return MeosLibrary.meos.text_upper(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_text_text(Pointer txt1, Pointer txt2) {
		return MeosLibrary.meos.textcat_text_text(txt1, txt2);
	}
	
	@SuppressWarnings("unused")
	public static int timestamp_to_date(LocalDateTime t) {
		var t_new = t.toEpochSecond(ZoneOffset.UTC);
		return MeosLibrary.meos.timestamp_to_date(t_new);
	}
	
	@SuppressWarnings("unused")
	public static int timestamptz_to_date(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_to_date(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_in(String str) {
		return MeosLibrary.meos.bigintset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String bigintset_out(Pointer set) {
		return MeosLibrary.meos.bigintset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspan_in(String str) {
		return MeosLibrary.meos.bigintspan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String bigintspan_out(Pointer s) {
		return MeosLibrary.meos.bigintspan_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspanset_in(String str) {
		return MeosLibrary.meos.bigintspanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String bigintspanset_out(Pointer ss) {
		return MeosLibrary.meos.bigintspanset_out(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_in(String str) {
		return MeosLibrary.meos.dateset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String dateset_out(Pointer s) {
		return MeosLibrary.meos.dateset_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_in(String str) {
		return MeosLibrary.meos.datespan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String datespan_out(Pointer s) {
		return MeosLibrary.meos.datespan_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_in(String str) {
		return MeosLibrary.meos.datespanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String datespanset_out(Pointer ss) {
		return MeosLibrary.meos.datespanset_out(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_in(String str) {
		return MeosLibrary.meos.floatset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String floatset_out(Pointer set, int maxdd) {
		return MeosLibrary.meos.floatset_out(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_in(String str) {
		return MeosLibrary.meos.floatspan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String floatspan_out(Pointer s, int maxdd) {
		return MeosLibrary.meos.floatspan_out(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_in(String str) {
		return MeosLibrary.meos.floatspanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String floatspanset_out(Pointer ss, int maxdd) {
		return MeosLibrary.meos.floatspanset_out(ss, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_in(String str) {
		return MeosLibrary.meos.intset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String intset_out(Pointer set) {
		return MeosLibrary.meos.intset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_in(String str) {
		return MeosLibrary.meos.intspan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String intspan_out(Pointer s) {
		return MeosLibrary.meos.intspan_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_in(String str) {
		return MeosLibrary.meos.intspanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String intspanset_out(Pointer ss) {
		return MeosLibrary.meos.intspanset_out(ss);
	}
	
	@SuppressWarnings("unused")
	public static String set_as_hexwkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.set_as_hexwkb(s, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_as_wkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.set_as_wkb(s, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_from_hexwkb(String hexwkb) {
		return MeosLibrary.meos.set_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_from_wkb(Pointer wkb, long size) {
		return MeosLibrary.meos.set_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_round(Pointer s, int maxdd) {
		return MeosLibrary.meos.set_round(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String span_as_hexwkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.span_as_hexwkb(s, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_as_wkb(Pointer s, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.span_as_wkb(s, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_from_hexwkb(String hexwkb) {
		return MeosLibrary.meos.span_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_from_wkb(Pointer wkb, long size) {
		return MeosLibrary.meos.span_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static String spanset_as_hexwkb(Pointer ss, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.spanset_as_hexwkb(ss, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_as_wkb(Pointer ss, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.spanset_as_wkb(ss, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_from_hexwkb(String hexwkb) {
		return MeosLibrary.meos.spanset_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_from_wkb(Pointer wkb, long size) {
		return MeosLibrary.meos.spanset_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static String spatialset_as_ewkt(Pointer set, int maxdd) {
		return MeosLibrary.meos.spatialset_as_ewkt(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String spatialset_as_text(Pointer set, int maxdd) {
		return MeosLibrary.meos.spatialset_as_text(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_in(String str) {
		return MeosLibrary.meos.textset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String textset_out(Pointer set) {
		return MeosLibrary.meos.textset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_in(String str) {
		return MeosLibrary.meos.tstzset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tstzset_out(Pointer set) {
		return MeosLibrary.meos.tstzset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_in(String str) {
		return MeosLibrary.meos.tstzspan_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tstzspan_out(Pointer s) {
		return MeosLibrary.meos.tstzspan_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_in(String str) {
		return MeosLibrary.meos.tstzspanset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tstzspanset_out(Pointer ss) {
		return MeosLibrary.meos.tstzspanset_out(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_make(Pointer values, int count) {
		return MeosLibrary.meos.bigintset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc) {
		return MeosLibrary.meos.bigintspan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_make(Pointer values, int count) {
		return MeosLibrary.meos.dateset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) {
		return MeosLibrary.meos.datespan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_make(Pointer values, int count) {
		return MeosLibrary.meos.floatset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc) {
		return MeosLibrary.meos.floatspan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_make(Pointer values, int count) {
		return MeosLibrary.meos.intset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc) {
		return MeosLibrary.meos.intspan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_copy(Pointer s) {
		return MeosLibrary.meos.set_copy(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_copy(Pointer s) {
		return MeosLibrary.meos.span_copy(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_copy(Pointer ss) {
		return MeosLibrary.meos.spanset_copy(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_make(Pointer spans, int count) {
		return MeosLibrary.meos.spanset_make(spans, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_make(Pointer values, int count) {
		return MeosLibrary.meos.textset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_make(Pointer values, int count) {
		return MeosLibrary.meos.tstzset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_make(OffsetDateTime lower, OffsetDateTime upper, boolean lower_inc, boolean upper_inc) {
		var lower_new = lower.toEpochSecond();
		var upper_new = upper.toEpochSecond();
		return MeosLibrary.meos.tstzspan_make(lower_new, upper_new, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_set(long i) {
		return MeosLibrary.meos.bigint_set(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_span(int i) {
		return MeosLibrary.meos.bigint_span(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_spanset(int i) {
		return MeosLibrary.meos.bigint_spanset(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_set(int d) {
		return MeosLibrary.meos.date_set(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_span(int d) {
		return MeosLibrary.meos.date_span(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_spanset(int d) {
		return MeosLibrary.meos.date_spanset(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_tstzset(Pointer s) {
		return MeosLibrary.meos.dateset_tstzset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_tstzspan(Pointer s) {
		return MeosLibrary.meos.datespan_tstzspan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_tstzspanset(Pointer ss) {
		return MeosLibrary.meos.datespanset_tstzspanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_set(double d) {
		return MeosLibrary.meos.float_set(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_span(double d) {
		return MeosLibrary.meos.float_span(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_spanset(double d) {
		return MeosLibrary.meos.float_spanset(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_intset(Pointer s) {
		return MeosLibrary.meos.floatset_intset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_intspan(Pointer s) {
		return MeosLibrary.meos.floatspan_intspan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_intspanset(Pointer ss) {
		return MeosLibrary.meos.floatspanset_intspanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_set(int i) {
		return MeosLibrary.meos.int_set(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_span(int i) {
		return MeosLibrary.meos.int_span(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_spanset(int i) {
		return MeosLibrary.meos.int_spanset(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_floatset(Pointer s) {
		return MeosLibrary.meos.intset_floatset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_floatspan(Pointer s) {
		return MeosLibrary.meos.intspan_floatspan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_floatspanset(Pointer ss) {
		return MeosLibrary.meos.intspanset_floatspanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_spanset(Pointer s) {
		return MeosLibrary.meos.set_spanset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_spanset(Pointer s) {
		return MeosLibrary.meos.span_spanset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_set(Pointer txt) {
		return MeosLibrary.meos.text_set(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_set(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_set(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_span(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_span(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_spanset(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_spanset(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_dateset(Pointer s) {
		return MeosLibrary.meos.tstzset_dateset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_datespan(Pointer s) {
		return MeosLibrary.meos.tstzspan_datespan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_datespanset(Pointer ss) {
		return MeosLibrary.meos.tstzspanset_datespanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static long bigintset_end_value(Pointer s) {
		return MeosLibrary.meos.bigintset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintset_start_value(Pointer s) {
		return MeosLibrary.meos.bigintset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.bigintset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_values(Pointer s) {
		return MeosLibrary.meos.bigintset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspan_lower(Pointer s) {
		return MeosLibrary.meos.bigintspan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspan_upper(Pointer s) {
		return MeosLibrary.meos.bigintspan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspan_width(Pointer s) {
		return MeosLibrary.meos.bigintspan_width(s);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspanset_lower(Pointer ss) {
		return MeosLibrary.meos.bigintspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspanset_upper(Pointer ss) {
		return MeosLibrary.meos.bigintspanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static long bigintspanset_width(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.bigintspanset_width(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static int dateset_end_value(Pointer s) {
		return MeosLibrary.meos.dateset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static int dateset_start_value(Pointer s) {
		return MeosLibrary.meos.dateset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.dateset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_values(Pointer s) {
		return MeosLibrary.meos.dateset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_duration(Pointer s) {
		return MeosLibrary.meos.datespan_duration(s);
	}
	
	@SuppressWarnings("unused")
	public static int datespan_lower(Pointer s) {
		return MeosLibrary.meos.datespan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static int datespan_upper(Pointer s) {
		return MeosLibrary.meos.datespan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_date_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.datespanset_date_n(ss, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_dates(Pointer ss) {
		return MeosLibrary.meos.datespanset_dates(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_duration(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.datespanset_duration(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static int datespanset_end_date(Pointer ss) {
		return MeosLibrary.meos.datespanset_end_date(ss);
	}
	
	@SuppressWarnings("unused")
	public static int datespanset_num_dates(Pointer ss) {
		return MeosLibrary.meos.datespanset_num_dates(ss);
	}
	
	@SuppressWarnings("unused")
	public static int datespanset_start_date(Pointer ss) {
		return MeosLibrary.meos.datespanset_start_date(ss);
	}
	
	@SuppressWarnings("unused")
	public static double floatset_end_value(Pointer s) {
		return MeosLibrary.meos.floatset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatset_start_value(Pointer s) {
		return MeosLibrary.meos.floatset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.floatset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_values(Pointer s) {
		return MeosLibrary.meos.floatset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatspan_lower(Pointer s) {
		return MeosLibrary.meos.floatspan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatspan_upper(Pointer s) {
		return MeosLibrary.meos.floatspan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatspan_width(Pointer s) {
		return MeosLibrary.meos.floatspan_width(s);
	}
	
	@SuppressWarnings("unused")
	public static double floatspanset_lower(Pointer ss) {
		return MeosLibrary.meos.floatspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static double floatspanset_upper(Pointer ss) {
		return MeosLibrary.meos.floatspanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static double floatspanset_width(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.floatspanset_width(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static int intset_end_value(Pointer s) {
		return MeosLibrary.meos.intset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static int intset_start_value(Pointer s) {
		return MeosLibrary.meos.intset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_values(Pointer s) {
		return MeosLibrary.meos.intset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static int intspan_lower(Pointer s) {
		return MeosLibrary.meos.intspan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static int intspan_upper(Pointer s) {
		return MeosLibrary.meos.intspan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static int intspan_width(Pointer s) {
		return MeosLibrary.meos.intspan_width(s);
	}
	
	@SuppressWarnings("unused")
	public static int intspanset_lower(Pointer ss) {
		return MeosLibrary.meos.intspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static int intspanset_upper(Pointer ss) {
		return MeosLibrary.meos.intspanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static int intspanset_width(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.intspanset_width(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static int set_hash(Pointer s) {
		return MeosLibrary.meos.set_hash(s);
	}
	
	@SuppressWarnings("unused")
	public static long set_hash_extended(Pointer s, long seed) {
		return MeosLibrary.meos.set_hash_extended(s, seed);
	}
	
	@SuppressWarnings("unused")
	public static int set_num_values(Pointer s) {
		return MeosLibrary.meos.set_num_values(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_span(Pointer s) {
		return MeosLibrary.meos.set_span(s);
	}
	
	@SuppressWarnings("unused")
	public static int span_hash(Pointer s) {
		return MeosLibrary.meos.span_hash(s);
	}
	
	@SuppressWarnings("unused")
	public static long span_hash_extended(Pointer s, long seed) {
		return MeosLibrary.meos.span_hash_extended(s, seed);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_lower_inc(Pointer s) {
		return MeosLibrary.meos.span_lower_inc(s);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_upper_inc(Pointer s) {
		return MeosLibrary.meos.span_upper_inc(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_end_span(Pointer ss) {
		return MeosLibrary.meos.spanset_end_span(ss);
	}
	
	@SuppressWarnings("unused")
	public static int spanset_hash(Pointer ss) {
		return MeosLibrary.meos.spanset_hash(ss);
	}
	
	@SuppressWarnings("unused")
	public static long spanset_hash_extended(Pointer ss, long seed) {
		return MeosLibrary.meos.spanset_hash_extended(ss, seed);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_lower_inc(Pointer ss) {
		return MeosLibrary.meos.spanset_lower_inc(ss);
	}
	
	@SuppressWarnings("unused")
	public static int spanset_num_spans(Pointer ss) {
		return MeosLibrary.meos.spanset_num_spans(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_span(Pointer ss) {
		return MeosLibrary.meos.spanset_span(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_span_n(Pointer ss, int i) {
		return MeosLibrary.meos.spanset_span_n(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_spanarr(Pointer ss) {
		return MeosLibrary.meos.spanset_spanarr(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_start_span(Pointer ss) {
		return MeosLibrary.meos.spanset_start_span(ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_upper_inc(Pointer ss) {
		return MeosLibrary.meos.spanset_upper_inc(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_end_value(Pointer s) {
		return MeosLibrary.meos.textset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_start_value(Pointer s) {
		return MeosLibrary.meos.textset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.textset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_values(Pointer s) {
		return MeosLibrary.meos.textset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzset_end_value(Pointer s) {
		var result = MeosLibrary.meos.tstzset_end_value(s);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzset_start_value(Pointer s) {
		var result = MeosLibrary.meos.tstzset_start_value(s);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tstzset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_values(Pointer s) {
		return MeosLibrary.meos.tstzset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_duration(Pointer s) {
		return MeosLibrary.meos.tstzspan_duration(s);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspan_lower(Pointer s) {
		var result = MeosLibrary.meos.tstzspan_lower(s);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspan_upper(Pointer s) {
		var result = MeosLibrary.meos.tstzspan_upper(s);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_duration(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.tstzspanset_duration(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_end_timestamptz(Pointer ss) {
		var result = MeosLibrary.meos.tstzspanset_end_timestamptz(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_lower(Pointer ss) {
		var result = MeosLibrary.meos.tstzspanset_lower(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static int tstzspanset_num_timestamps(Pointer ss) {
		return MeosLibrary.meos.tstzspanset_num_timestamps(ss);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_start_timestamptz(Pointer ss) {
		var result = MeosLibrary.meos.tstzspanset_start_timestamptz(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_timestamptz_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tstzspanset_timestamptz_n(ss, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_timestamps(Pointer ss) {
		return MeosLibrary.meos.tstzspanset_timestamps(ss);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tstzspanset_upper(Pointer ss) {
		var result = MeosLibrary.meos.tstzspanset_upper(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.bigintset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.bigintspan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.bigintspanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer dateset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.dateset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.datespan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.datespanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_ceil(Pointer s) {
		return MeosLibrary.meos.floatset_ceil(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_floor(Pointer s) {
		return MeosLibrary.meos.floatset_floor(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_degrees(Pointer s, boolean normalize) {
		return MeosLibrary.meos.floatset_degrees(s, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_radians(Pointer s) {
		return MeosLibrary.meos.floatset_radians(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.floatset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_ceil(Pointer s) {
		return MeosLibrary.meos.floatspan_ceil(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_floor(Pointer s) {
		return MeosLibrary.meos.floatspan_floor(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_degrees(Pointer s, boolean normalize) {
		return MeosLibrary.meos.floatspan_degrees(s, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_radians(Pointer s) {
		return MeosLibrary.meos.floatspan_radians(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_round(Pointer s, int maxdd) {
		return MeosLibrary.meos.floatspan_round(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.floatspan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_ceil(Pointer ss) {
		return MeosLibrary.meos.floatspanset_ceil(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_floor(Pointer ss) {
		return MeosLibrary.meos.floatspanset_floor(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_degrees(Pointer ss, boolean normalize) {
		return MeosLibrary.meos.floatspanset_degrees(ss, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_radians(Pointer ss) {
		return MeosLibrary.meos.floatspanset_radians(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_round(Pointer ss, int maxdd) {
		return MeosLibrary.meos.floatspanset_round(ss, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.floatspanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.intset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.intspan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.intspanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_spans(Pointer s) {
		return MeosLibrary.meos.set_spans(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_split_each_n_spans(Pointer s, int elem_count, Pointer count) {
		return MeosLibrary.meos.set_split_each_n_spans(s, elem_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_split_n_spans(Pointer s, int span_count, Pointer count) {
		return MeosLibrary.meos.set_split_n_spans(s, span_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_spans(Pointer ss) {
		return MeosLibrary.meos.spanset_spans(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_split_each_n_spans(Pointer ss, int elem_count, Pointer count) {
		return MeosLibrary.meos.spanset_split_each_n_spans(ss, elem_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_split_n_spans(Pointer ss, int span_count, Pointer count) {
		return MeosLibrary.meos.spanset_split_n_spans(ss, span_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_initcap(Pointer s) {
		return MeosLibrary.meos.textset_initcap(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_lower(Pointer s) {
		return MeosLibrary.meos.textset_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_upper(Pointer s) {
		return MeosLibrary.meos.textset_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_textset_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.textcat_textset_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_text_textset(Pointer txt, Pointer s) {
		return MeosLibrary.meos.textcat_text_textset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_tprecision(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var result = MeosLibrary.meos.timestamptz_tprecision(t_new, duration, torigin_new);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_shift_scale(Pointer s, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.tstzset_shift_scale(s, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tstzset_tprecision(s, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_shift_scale(Pointer s, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.tstzspan_shift_scale(s, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tstzspan_tprecision(s, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_shift_scale(Pointer ss, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.tstzspanset_shift_scale(ss, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_tprecision(Pointer ss, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tstzspanset_tprecision(ss, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static int set_cmp(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.set_cmp(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_eq(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.set_eq(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_ge(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.set_ge(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_gt(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.set_gt(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_le(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.set_le(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_lt(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.set_lt(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_ne(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.set_ne(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int span_cmp(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.span_cmp(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_eq(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.span_eq(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_ge(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.span_ge(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_gt(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.span_gt(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_le(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.span_le(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_lt(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.span_lt(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_ne(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.span_ne(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int spanset_cmp(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.spanset_cmp(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_eq(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.spanset_eq(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_ge(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.spanset_ge(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_gt(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.spanset_gt(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_le(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.spanset_le(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_lt(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.spanset_lt(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_ne(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.spanset_ne(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.adjacent_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_date(Pointer s, int d) {
		return MeosLibrary.meos.adjacent_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_float(Pointer s, double d) {
		return MeosLibrary.meos.adjacent_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_int(Pointer s, int i) {
		return MeosLibrary.meos.adjacent_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.adjacent_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.adjacent_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.adjacent_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.adjacent_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.adjacent_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.adjacent_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.adjacent_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.adjacent_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.adjacent_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.adjacent_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_bigint_set(long i, Pointer s) {
		return MeosLibrary.meos.contained_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_bigint_span(long i, Pointer s) {
		return MeosLibrary.meos.contained_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_bigint_spanset(long i, Pointer ss) {
		return MeosLibrary.meos.contained_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_date_set(int d, Pointer s) {
		return MeosLibrary.meos.contained_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_date_span(int d, Pointer s) {
		return MeosLibrary.meos.contained_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_date_spanset(int d, Pointer ss) {
		return MeosLibrary.meos.contained_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_set(double d, Pointer s) {
		return MeosLibrary.meos.contained_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_span(double d, Pointer s) {
		return MeosLibrary.meos.contained_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_spanset(double d, Pointer ss) {
		return MeosLibrary.meos.contained_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_set(int i, Pointer s) {
		return MeosLibrary.meos.contained_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_span(int i, Pointer s) {
		return MeosLibrary.meos.contained_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_spanset(int i, Pointer ss) {
		return MeosLibrary.meos.contained_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.contained_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.contained_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.contained_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.contained_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.contained_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_text_set(Pointer txt, Pointer s) {
		return MeosLibrary.meos.contained_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contained_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contained_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contained_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.contains_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_date(Pointer s, int d) {
		return MeosLibrary.meos.contains_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_float(Pointer s, double d) {
		return MeosLibrary.meos.contains_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_int(Pointer s, int i) {
		return MeosLibrary.meos.contains_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.contains_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_text(Pointer s, Pointer t) {
		return MeosLibrary.meos.contains_set_text(s, t);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contains_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.contains_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_date(Pointer s, int d) {
		return MeosLibrary.meos.contains_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_float(Pointer s, double d) {
		return MeosLibrary.meos.contains_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_int(Pointer s, int i) {
		return MeosLibrary.meos.contains_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.contains_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.contains_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contains_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.contains_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.contains_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.contains_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.contains_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.contains_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.contains_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contains_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.overlaps_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.overlaps_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.overlaps_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.overlaps_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.overlaps_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_date_set(int d, Pointer s) {
		return MeosLibrary.meos.after_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_date_span(int d, Pointer s) {
		return MeosLibrary.meos.after_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_date_spanset(int d, Pointer ss) {
		return MeosLibrary.meos.after_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_set_date(Pointer s, int d) {
		return MeosLibrary.meos.after_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_span_date(Pointer s, int d) {
		return MeosLibrary.meos.after_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.after_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_date_set(int d, Pointer s) {
		return MeosLibrary.meos.before_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_date_span(int d, Pointer s) {
		return MeosLibrary.meos.before_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_date_spanset(int d, Pointer ss) {
		return MeosLibrary.meos.before_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_set_date(Pointer s, int d) {
		return MeosLibrary.meos.before_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_span_date(Pointer s, int d) {
		return MeosLibrary.meos.before_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.before_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_set(long i, Pointer s) {
		return MeosLibrary.meos.left_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_span(long i, Pointer s) {
		return MeosLibrary.meos.left_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_spanset(long i, Pointer ss) {
		return MeosLibrary.meos.left_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_set(double d, Pointer s) {
		return MeosLibrary.meos.left_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_span(double d, Pointer s) {
		return MeosLibrary.meos.left_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_spanset(double d, Pointer ss) {
		return MeosLibrary.meos.left_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_set(int i, Pointer s) {
		return MeosLibrary.meos.left_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_span(int i, Pointer s) {
		return MeosLibrary.meos.left_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_spanset(int i, Pointer ss) {
		return MeosLibrary.meos.left_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.left_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_float(Pointer s, double d) {
		return MeosLibrary.meos.left_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_int(Pointer s, int i) {
		return MeosLibrary.meos.left_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.left_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.left_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.left_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_float(Pointer s, double d) {
		return MeosLibrary.meos.left_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_int(Pointer s, int i) {
		return MeosLibrary.meos.left_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.left_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.left_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.left_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.left_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.left_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.left_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.left_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_text_set(Pointer txt, Pointer s) {
		return MeosLibrary.meos.left_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_date_set(int d, Pointer s) {
		return MeosLibrary.meos.overafter_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_date_span(int d, Pointer s) {
		return MeosLibrary.meos.overafter_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_date_spanset(int d, Pointer ss) {
		return MeosLibrary.meos.overafter_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_set_date(Pointer s, int d) {
		return MeosLibrary.meos.overafter_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_span_date(Pointer s, int d) {
		return MeosLibrary.meos.overafter_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.overafter_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_date_set(int d, Pointer s) {
		return MeosLibrary.meos.overbefore_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_date_span(int d, Pointer s) {
		return MeosLibrary.meos.overbefore_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_date_spanset(int d, Pointer ss) {
		return MeosLibrary.meos.overbefore_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_set_date(Pointer s, int d) {
		return MeosLibrary.meos.overbefore_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_span_date(Pointer s, int d) {
		return MeosLibrary.meos.overbefore_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.overbefore_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_set(long i, Pointer s) {
		return MeosLibrary.meos.overleft_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_span(long i, Pointer s) {
		return MeosLibrary.meos.overleft_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_spanset(long i, Pointer ss) {
		return MeosLibrary.meos.overleft_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_set(double d, Pointer s) {
		return MeosLibrary.meos.overleft_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_span(double d, Pointer s) {
		return MeosLibrary.meos.overleft_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_spanset(double d, Pointer ss) {
		return MeosLibrary.meos.overleft_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_set(int i, Pointer s) {
		return MeosLibrary.meos.overleft_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_span(int i, Pointer s) {
		return MeosLibrary.meos.overleft_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_spanset(int i, Pointer ss) {
		return MeosLibrary.meos.overleft_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.overleft_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_float(Pointer s, double d) {
		return MeosLibrary.meos.overleft_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_int(Pointer s, int i) {
		return MeosLibrary.meos.overleft_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.overleft_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.overleft_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.overleft_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_float(Pointer s, double d) {
		return MeosLibrary.meos.overleft_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_int(Pointer s, int i) {
		return MeosLibrary.meos.overleft_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.overleft_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.overleft_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.overleft_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.overleft_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.overleft_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.overleft_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.overleft_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_text_set(Pointer txt, Pointer s) {
		return MeosLibrary.meos.overleft_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_set(long i, Pointer s) {
		return MeosLibrary.meos.overright_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_span(long i, Pointer s) {
		return MeosLibrary.meos.overright_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_spanset(long i, Pointer ss) {
		return MeosLibrary.meos.overright_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_set(double d, Pointer s) {
		return MeosLibrary.meos.overright_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_span(double d, Pointer s) {
		return MeosLibrary.meos.overright_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_spanset(double d, Pointer ss) {
		return MeosLibrary.meos.overright_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_set(int i, Pointer s) {
		return MeosLibrary.meos.overright_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_span(int i, Pointer s) {
		return MeosLibrary.meos.overright_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_spanset(int i, Pointer ss) {
		return MeosLibrary.meos.overright_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.overright_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_float(Pointer s, double d) {
		return MeosLibrary.meos.overright_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_int(Pointer s, int i) {
		return MeosLibrary.meos.overright_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.overright_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.overright_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.overright_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_float(Pointer s, double d) {
		return MeosLibrary.meos.overright_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_int(Pointer s, int i) {
		return MeosLibrary.meos.overright_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.overright_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.overright_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.overright_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.overright_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.overright_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.overright_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.overright_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_text_set(Pointer txt, Pointer s) {
		return MeosLibrary.meos.overright_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_set(long i, Pointer s) {
		return MeosLibrary.meos.right_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_span(long i, Pointer s) {
		return MeosLibrary.meos.right_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_spanset(long i, Pointer ss) {
		return MeosLibrary.meos.right_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_set(double d, Pointer s) {
		return MeosLibrary.meos.right_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_span(double d, Pointer s) {
		return MeosLibrary.meos.right_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_spanset(double d, Pointer ss) {
		return MeosLibrary.meos.right_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_set(int i, Pointer s) {
		return MeosLibrary.meos.right_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_span(int i, Pointer s) {
		return MeosLibrary.meos.right_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_spanset(int i, Pointer ss) {
		return MeosLibrary.meos.right_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.right_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_float(Pointer s, double d) {
		return MeosLibrary.meos.right_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_int(Pointer s, int i) {
		return MeosLibrary.meos.right_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.right_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.right_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.right_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_float(Pointer s, double d) {
		return MeosLibrary.meos.right_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_int(Pointer s, int i) {
		return MeosLibrary.meos.right_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.right_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.right_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.right_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.right_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.right_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.right_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.right_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_text_set(Pointer txt, Pointer s) {
		return MeosLibrary.meos.right_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_bigint_set(long i, Pointer s) {
		return MeosLibrary.meos.intersection_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_date_set(int d, Pointer s) {
		return MeosLibrary.meos.intersection_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_float_set(double d, Pointer s) {
		return MeosLibrary.meos.intersection_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_int_set(int i, Pointer s) {
		return MeosLibrary.meos.intersection_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.intersection_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_date(Pointer s, int d) {
		return MeosLibrary.meos.intersection_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_float(Pointer s, double d) {
		return MeosLibrary.meos.intersection_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_int(Pointer s, int i) {
		return MeosLibrary.meos.intersection_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.intersection_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.intersection_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.intersection_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.intersection_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_date(Pointer s, int d) {
		return MeosLibrary.meos.intersection_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_float(Pointer s, double d) {
		return MeosLibrary.meos.intersection_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_int(Pointer s, int i) {
		return MeosLibrary.meos.intersection_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.intersection_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.intersection_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.intersection_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.intersection_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.intersection_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.intersection_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.intersection_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.intersection_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.intersection_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.intersection_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_text_set(Pointer txt, Pointer s) {
		return MeosLibrary.meos.intersection_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.intersection_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_set(long i, Pointer s) {
		return MeosLibrary.meos.minus_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_span(long i, Pointer s) {
		return MeosLibrary.meos.minus_bigint_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_spanset(long i, Pointer ss) {
		return MeosLibrary.meos.minus_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_date_set(int d, Pointer s) {
		return MeosLibrary.meos.minus_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_date_span(int d, Pointer s) {
		return MeosLibrary.meos.minus_date_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_date_spanset(int d, Pointer ss) {
		return MeosLibrary.meos.minus_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_set(double d, Pointer s) {
		return MeosLibrary.meos.minus_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_span(double d, Pointer s) {
		return MeosLibrary.meos.minus_float_span(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_spanset(double d, Pointer ss) {
		return MeosLibrary.meos.minus_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_set(int i, Pointer s) {
		return MeosLibrary.meos.minus_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_span(int i, Pointer s) {
		return MeosLibrary.meos.minus_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_spanset(int i, Pointer ss) {
		return MeosLibrary.meos.minus_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.minus_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_date(Pointer s, int d) {
		return MeosLibrary.meos.minus_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_float(Pointer s, double d) {
		return MeosLibrary.meos.minus_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_int(Pointer s, int i) {
		return MeosLibrary.meos.minus_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.minus_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.minus_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.minus_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_date(Pointer s, int d) {
		return MeosLibrary.meos.minus_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_float(Pointer s, double d) {
		return MeosLibrary.meos.minus_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_int(Pointer s, int i) {
		return MeosLibrary.meos.minus_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.minus_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.minus_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.minus_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.minus_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.minus_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.minus_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.minus_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.minus_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_text_set(Pointer txt, Pointer s) {
		return MeosLibrary.meos.minus_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigint_set(long i, Pointer s) {
		return MeosLibrary.meos.union_bigint_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigint_span(Pointer s, long i) {
		return MeosLibrary.meos.union_bigint_span(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigint_spanset(long i, Pointer ss) {
		return MeosLibrary.meos.union_bigint_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_date_set(int d, Pointer s) {
		return MeosLibrary.meos.union_date_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_date_span(Pointer s, int d) {
		return MeosLibrary.meos.union_date_span(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_date_spanset(int d, Pointer ss) {
		return MeosLibrary.meos.union_date_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_float_set(double d, Pointer s) {
		return MeosLibrary.meos.union_float_set(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_float_span(Pointer s, double d) {
		return MeosLibrary.meos.union_float_span(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_float_spanset(double d, Pointer ss) {
		return MeosLibrary.meos.union_float_spanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_int_set(int i, Pointer s) {
		return MeosLibrary.meos.union_int_set(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_int_span(int i, Pointer s) {
		return MeosLibrary.meos.union_int_span(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_int_spanset(int i, Pointer ss) {
		return MeosLibrary.meos.union_int_spanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.union_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_date(Pointer s, int d) {
		return MeosLibrary.meos.union_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_float(Pointer s, double d) {
		return MeosLibrary.meos.union_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_int(Pointer s, int i) {
		return MeosLibrary.meos.union_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.union_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.union_set_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.union_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_date(Pointer s, int d) {
		return MeosLibrary.meos.union_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_float(Pointer s, double d) {
		return MeosLibrary.meos.union_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_int(Pointer s, int i) {
		return MeosLibrary.meos.union_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.union_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_spanset(Pointer s, Pointer ss) {
		return MeosLibrary.meos.union_span_spanset(s, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.union_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.union_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.union_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.union_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.union_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.union_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_text_set(Pointer txt, Pointer s) {
		return MeosLibrary.meos.union_text_set(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_set(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_timestamptz_set(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_span(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_timestamptz_span(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_timestamptz_spanset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_timestamptz_spanset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static long distance_bigintset_bigintset(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_bigintset_bigintset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_bigintspan_bigintspan(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_bigintspan_bigintspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_bigintspanset_bigintspan(Pointer ss, Pointer s) {
		return MeosLibrary.meos.distance_bigintspanset_bigintspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static long distance_bigintspanset_bigintspanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.distance_bigintspanset_bigintspanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_dateset_dateset(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_dateset_dateset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_datespan_datespan(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_datespan_datespan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_datespanset_datespan(Pointer ss, Pointer s) {
		return MeosLibrary.meos.distance_datespanset_datespan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static int distance_datespanset_datespanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.distance_datespanset_datespanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatset_floatset(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_floatset_floatset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatspan_floatspan(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_floatspan_floatspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatspanset_floatspan(Pointer ss, Pointer s) {
		return MeosLibrary.meos.distance_floatspanset_floatspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatspanset_floatspanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.distance_floatspanset_floatspanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_intset_intset(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_intset_intset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_intspan_intspan(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_intspan_intspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static int distance_intspanset_intspan(Pointer ss, Pointer s) {
		return MeosLibrary.meos.distance_intspanset_intspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static int distance_intspanset_intspanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.distance_intspanset_intspanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_set_bigint(Pointer s, long i) {
		return MeosLibrary.meos.distance_set_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static int distance_set_date(Pointer s, int d) {
		return MeosLibrary.meos.distance_set_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_set_float(Pointer s, double d) {
		return MeosLibrary.meos.distance_set_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static int distance_set_int(Pointer s, int i) {
		return MeosLibrary.meos.distance_set_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_set_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.distance_set_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static long distance_span_bigint(Pointer s, long i) {
		return MeosLibrary.meos.distance_span_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static int distance_span_date(Pointer s, int d) {
		return MeosLibrary.meos.distance_span_date(s, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_span_float(Pointer s, double d) {
		return MeosLibrary.meos.distance_span_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static int distance_span_int(Pointer s, int i) {
		return MeosLibrary.meos.distance_span_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_span_timestamptz(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.distance_span_timestamptz(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static long distance_spanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.distance_spanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static int distance_spanset_date(Pointer ss, int d) {
		return MeosLibrary.meos.distance_spanset_date(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_spanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.distance_spanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static int distance_spanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.distance_spanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_spanset_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.distance_spanset_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static double distance_tstzset_tstzset(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_tstzset_tstzset(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_tstzspan_tstzspan(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_tstzspan_tstzspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_tstzspanset_tstzspan(Pointer ss, Pointer s) {
		return MeosLibrary.meos.distance_tstzspanset_tstzspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static double distance_tstzspanset_tstzspanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.distance_tstzspanset_tstzspanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_extent_transfn(Pointer state, long i) {
		return MeosLibrary.meos.bigint_extent_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_union_transfn(Pointer state, long i) {
		return MeosLibrary.meos.bigint_union_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_extent_transfn(Pointer state, int d) {
		return MeosLibrary.meos.date_extent_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer date_union_transfn(Pointer state, int d) {
		return MeosLibrary.meos.date_union_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_extent_transfn(Pointer state, double d) {
		return MeosLibrary.meos.float_extent_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_union_transfn(Pointer state, double d) {
		return MeosLibrary.meos.float_union_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_extent_transfn(Pointer state, int i) {
		return MeosLibrary.meos.int_extent_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_union_transfn(Pointer state, int i) {
		return MeosLibrary.meos.int_union_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_extent_transfn(Pointer state, Pointer s) {
		return MeosLibrary.meos.set_extent_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_union_finalfn(Pointer state) {
		return MeosLibrary.meos.set_union_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_union_transfn(Pointer state, Pointer s) {
		return MeosLibrary.meos.set_union_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_extent_transfn(Pointer state, Pointer s) {
		return MeosLibrary.meos.span_extent_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_union_transfn(Pointer state, Pointer s) {
		return MeosLibrary.meos.span_union_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_extent_transfn(Pointer state, Pointer ss) {
		return MeosLibrary.meos.spanset_extent_transfn(state, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_union_finalfn(Pointer state) {
		return MeosLibrary.meos.spanset_union_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_union_transfn(Pointer state, Pointer ss) {
		return MeosLibrary.meos.spanset_union_transfn(state, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_union_transfn(Pointer state, Pointer txt) {
		return MeosLibrary.meos.text_union_transfn(state, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_extent_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_extent_transfn(state, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_union_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_union_transfn(state, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_in(String str) {
		return MeosLibrary.meos.tbox_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tbox_out(Pointer box, int maxdd) {
		return MeosLibrary.meos.tbox_out(box, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_from_wkb(Pointer wkb, long size) {
		return MeosLibrary.meos.tbox_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_from_hexwkb(String hexwkb) {
		return MeosLibrary.meos.tbox_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_as_wkb(Pointer box, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.tbox_as_wkb(box, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static String tbox_as_hexwkb(Pointer box, byte variant, Pointer size) {
		return MeosLibrary.meos.tbox_as_hexwkb(box, variant, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_tstzspan_to_tbox(double d, Pointer s) {
		return MeosLibrary.meos.float_tstzspan_to_tbox(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_timestamptz_to_tbox(double d, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.float_timestamptz_to_tbox(d, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_tstzspan_to_tbox(int i, Pointer s) {
		return MeosLibrary.meos.int_tstzspan_to_tbox(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_timestamptz_to_tbox(int i, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.int_timestamptz_to_tbox(i, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspan_tstzspan_to_tbox(Pointer span, Pointer s) {
		return MeosLibrary.meos.numspan_tstzspan_to_tbox(span, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspan_timestamptz_to_tbox(Pointer span, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.numspan_timestamptz_to_tbox(span, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_copy(Pointer box) {
		return MeosLibrary.meos.tbox_copy(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_make(Pointer s, Pointer p) {
		return MeosLibrary.meos.tbox_make(s, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_tbox(double d) {
		return MeosLibrary.meos.float_tbox(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_tbox(int i) {
		return MeosLibrary.meos.int_tbox(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_tbox(Pointer s) {
		return MeosLibrary.meos.set_tbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_tbox(Pointer s) {
		return MeosLibrary.meos.span_tbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_tbox(Pointer ss) {
		return MeosLibrary.meos.spanset_tbox(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_intspan(Pointer box) {
		return MeosLibrary.meos.tbox_intspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_floatspan(Pointer box) {
		return MeosLibrary.meos.tbox_floatspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tstzspan(Pointer box) {
		return MeosLibrary.meos.tbox_tstzspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_tbox(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_tbox(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_tbox(Pointer temp) {
		return MeosLibrary.meos.tnumber_tbox(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_hast(Pointer box) {
		return MeosLibrary.meos.tbox_hast(box);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_hasx(Pointer box) {
		return MeosLibrary.meos.tbox_hasx(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_tmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_tmax_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_tmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_tmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_tmin_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_xmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_xmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_xmax_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_xmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_xmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbox_xmin_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboxfloat_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tboxfloat_xmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboxfloat_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tboxfloat_xmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboxint_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tboxint_xmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboxint_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tboxint_xmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_expand_time(Pointer box, Pointer interv) {
		return MeosLibrary.meos.tbox_expand_time(box, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_expand_float(Pointer box, double d) {
		return MeosLibrary.meos.tbox_expand_float(box, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_expand_int(Pointer box, int i) {
		return MeosLibrary.meos.tbox_expand_int(box, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_round(Pointer box, int maxdd) {
		return MeosLibrary.meos.tbox_round(box, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_float(Pointer box, double shift, double width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.tbox_shift_scale_float(box, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_int(Pointer box, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.tbox_shift_scale_int(box, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.tbox_shift_scale_time(box, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict) {
		return MeosLibrary.meos.union_tbox_tbox(box1, box2, strict);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.intersection_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.adjacent_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.contained_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.contains_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overlaps_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.same_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.left_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overleft_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.right_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overright_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.before_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overbefore_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.after_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overafter_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_eq(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.tbox_eq(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_ne(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.tbox_ne(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static int tbox_cmp(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.tbox_cmp(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_lt(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.tbox_lt(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_le(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.tbox_le(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_ge(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.tbox_ge(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbox_gt(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.tbox_gt(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_in(String str) {
		return MeosLibrary.meos.tbool_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_in(String str) {
		return MeosLibrary.meos.tint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_in(String str) {
		return MeosLibrary.meos.tfloat_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_in(String str) {
		return MeosLibrary.meos.ttext_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_from_mfjson(String str) {
		return MeosLibrary.meos.tbool_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_from_mfjson(String str) {
		return MeosLibrary.meos.tint_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_from_mfjson(String str) {
		return MeosLibrary.meos.tfloat_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_from_mfjson(String str) {
		return MeosLibrary.meos.ttext_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_from_wkb(Pointer wkb, long size) {
		return MeosLibrary.meos.temporal_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_from_hexwkb(String hexwkb) {
		return MeosLibrary.meos.temporal_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static String tbool_out(Pointer temp) {
		return MeosLibrary.meos.tbool_out(temp);
	}
	
	@SuppressWarnings("unused")
	public static String tint_out(Pointer temp) {
		return MeosLibrary.meos.tint_out(temp);
	}
	
	@SuppressWarnings("unused")
	public static String tfloat_out(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tfloat_out(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String ttext_out(Pointer temp) {
		return MeosLibrary.meos.ttext_out(temp);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs) {
		return MeosLibrary.meos.temporal_as_mfjson(temp, with_bbox, flags, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_as_wkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.temporal_as_wkb(temp, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_as_hexwkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.temporal_as_hexwkb(temp, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_from_base_temp(boolean b, Pointer temp) {
		return MeosLibrary.meos.tbool_from_base_temp(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolinst_make(boolean b, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tboolinst_make(b, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_tstzset(boolean b, Pointer s) {
		return MeosLibrary.meos.tboolseq_from_base_tstzset(b, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_tstzspan(boolean b, Pointer s) {
		return MeosLibrary.meos.tboolseq_from_base_tstzspan(b, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseqset_from_base_tstzspanset(boolean b, Pointer ss) {
		return MeosLibrary.meos.tboolseqset_from_base_tstzspanset(b, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_copy(Pointer temp) {
		return MeosLibrary.meos.temporal_copy(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_from_base_temp(double d, Pointer temp) {
		return MeosLibrary.meos.tfloat_from_base_temp(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatinst_make(double d, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tfloatinst_make(d, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_tstzspan(double d, Pointer s, int interp) {
		return MeosLibrary.meos.tfloatseq_from_base_tstzspan(d, s, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_tstzset(double d, Pointer s) {
		return MeosLibrary.meos.tfloatseq_from_base_tstzset(d, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_from_base_tstzspanset(double d, Pointer ss, int interp) {
		return MeosLibrary.meos.tfloatseqset_from_base_tstzspanset(d, ss, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_from_base_temp(int i, Pointer temp) {
		return MeosLibrary.meos.tint_from_base_temp(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintinst_make(int i, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tintinst_make(i, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_tstzspan(int i, Pointer s) {
		return MeosLibrary.meos.tintseq_from_base_tstzspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_tstzset(int i, Pointer s) {
		return MeosLibrary.meos.tintseq_from_base_tstzset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseqset_from_base_tstzspanset(int i, Pointer ss) {
		return MeosLibrary.meos.tintseqset_from_base_tstzspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		return MeosLibrary.meos.tsequence_make(instants, count, lower_inc, upper_inc, interp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize) {
		return MeosLibrary.meos.tsequenceset_make(sequences, count, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist) {
		return MeosLibrary.meos.tsequenceset_make_gaps(instants, count, interp, maxt, maxdist);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_from_base_temp(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.ttext_from_base_temp(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextinst_make(Pointer txt, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.ttextinst_make(txt, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_tstzspan(Pointer txt, Pointer s) {
		return MeosLibrary.meos.ttextseq_from_base_tstzspan(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_tstzset(Pointer txt, Pointer s) {
		return MeosLibrary.meos.ttextseq_from_base_tstzset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseqset_from_base_tstzspanset(Pointer txt, Pointer ss) {
		return MeosLibrary.meos.ttextseqset_from_base_tstzspanset(txt, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tstzspan(Pointer temp) {
		return MeosLibrary.meos.temporal_tstzspan(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_tint(Pointer temp) {
		return MeosLibrary.meos.tbool_tint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_tint(Pointer temp) {
		return MeosLibrary.meos.tfloat_tint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_tfloat(Pointer temp) {
		return MeosLibrary.meos.tint_tfloat(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_span(Pointer temp) {
		return MeosLibrary.meos.tnumber_span(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbool_end_value(Pointer temp) {
		return MeosLibrary.meos.tbool_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbool_start_value(Pointer temp) {
		return MeosLibrary.meos.tbool_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbool_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tbool_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tbool_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tbool_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_duration(Pointer temp, boolean boundspan) {
		return MeosLibrary.meos.temporal_duration(temp, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_end_instant(Pointer temp) {
		return MeosLibrary.meos.temporal_end_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_end_sequence(Pointer temp) {
		return MeosLibrary.meos.temporal_end_sequence(temp);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_end_timestamptz(Pointer temp) {
		var result = MeosLibrary.meos.temporal_end_timestamptz(temp);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_hash(Pointer temp) {
		return MeosLibrary.meos.temporal_hash(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_instant_n(Pointer temp, int n) {
		return MeosLibrary.meos.temporal_instant_n(temp, n);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_instants(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_instants(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_interp(Pointer temp) {
		return MeosLibrary.meos.temporal_interp(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_max_instant(Pointer temp) {
		return MeosLibrary.meos.temporal_max_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_min_instant(Pointer temp) {
		return MeosLibrary.meos.temporal_min_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_num_instants(Pointer temp) {
		return MeosLibrary.meos.temporal_num_instants(temp);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_num_sequences(Pointer temp) {
		return MeosLibrary.meos.temporal_num_sequences(temp);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_num_timestamps(Pointer temp) {
		return MeosLibrary.meos.temporal_num_timestamps(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_segments(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_segments(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_sequence_n(Pointer temp, int i) {
		return MeosLibrary.meos.temporal_sequence_n(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_sequences(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_sequences(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_lower_inc(Pointer temp) {
		return MeosLibrary.meos.temporal_lower_inc(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_upper_inc(Pointer temp) {
		return MeosLibrary.meos.temporal_upper_inc(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_start_instant(Pointer temp) {
		return MeosLibrary.meos.temporal_start_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_start_sequence(Pointer temp) {
		return MeosLibrary.meos.temporal_start_sequence(temp);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_start_timestamptz(Pointer temp) {
		var result = MeosLibrary.meos.temporal_start_timestamptz(temp);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration) {
		return MeosLibrary.meos.temporal_stops(temp, maxdist, minduration);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_subtype(Pointer temp) {
		return MeosLibrary.meos.temporal_subtype(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_time(Pointer temp) {
		return MeosLibrary.meos.temporal_time(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_timestamptz_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.temporal_timestamptz_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_timestamps(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_timestamps(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static double tfloat_end_value(Pointer temp) {
		return MeosLibrary.meos.tfloat_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tfloat_max_value(Pointer temp) {
		return MeosLibrary.meos.tfloat_max_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tfloat_min_value(Pointer temp) {
		return MeosLibrary.meos.tfloat_min_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tfloat_start_value(Pointer temp) {
		return MeosLibrary.meos.tfloat_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tfloat_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tfloat_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tfloat_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tfloat_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static int tint_end_value(Pointer temp) {
		return MeosLibrary.meos.tint_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static int tint_max_value(Pointer temp) {
		return MeosLibrary.meos.tint_max_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static int tint_min_value(Pointer temp) {
		return MeosLibrary.meos.tint_min_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static int tint_start_value(Pointer temp) {
		return MeosLibrary.meos.tint_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tint_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tint_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tint_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tint_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static double tnumber_integral(Pointer temp) {
		return MeosLibrary.meos.tnumber_integral(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tnumber_twavg(Pointer temp) {
		return MeosLibrary.meos.tnumber_twavg(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_valuespans(Pointer temp) {
		return MeosLibrary.meos.tnumber_valuespans(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_end_value(Pointer temp) {
		return MeosLibrary.meos.ttext_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_max_value(Pointer temp) {
		return MeosLibrary.meos.ttext_max_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_min_value(Pointer temp) {
		return MeosLibrary.meos.ttext_min_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_start_value(Pointer temp) {
		return MeosLibrary.meos.ttext_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean ttext_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.ttext_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.ttext_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.ttext_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static int spheroid_init_from_srid(int srid, Pointer s) {
		return MeosLibrary.meos.spheroid_init_from_srid(srid, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_srid_is_latlong(int srid) {
		return MeosLibrary.meos.ensure_srid_is_latlong(srid);
	}
	
	@SuppressWarnings("unused")
	public static double float_degrees(double value, boolean normalize) {
		return MeosLibrary.meos.float_degrees(value, normalize);
	}
	
	@SuppressWarnings("unused")
	public static boolean srid_is_latlong(int srid) {
		return MeosLibrary.meos.srid_is_latlong(srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temparr_round(Pointer temp, int count, int maxdd) {
		return MeosLibrary.meos.temparr_round(temp, count, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_round(Pointer temp, int maxdd) {
		return MeosLibrary.meos.temporal_round(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_scale_time(Pointer temp, Pointer duration) {
		return MeosLibrary.meos.temporal_scale_time(temp, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_set_interp(Pointer temp, String interp_str) {
		return MeosLibrary.meos.temporal_set_interp(temp, interp_str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.temporal_shift_scale_time(temp, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_shift_time(Pointer temp, Pointer shift) {
		return MeosLibrary.meos.temporal_shift_time(temp, shift);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_tinstant(Pointer temp) {
		return MeosLibrary.meos.temporal_to_tinstant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_tsequence(Pointer temp, String interp_str) {
		return MeosLibrary.meos.temporal_to_tsequence(temp, interp_str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_tsequenceset(Pointer temp, String interp_str) {
		return MeosLibrary.meos.temporal_to_tsequenceset(temp, interp_str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_floor(Pointer temp) {
		return MeosLibrary.meos.tfloat_floor(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_ceil(Pointer temp) {
		return MeosLibrary.meos.tfloat_ceil(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_degrees(Pointer temp, boolean normalize) {
		return MeosLibrary.meos.tfloat_degrees(temp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_radians(Pointer temp) {
		return MeosLibrary.meos.tfloat_radians(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_scale_value(Pointer temp, double width) {
		return MeosLibrary.meos.tfloat_scale_value(temp, width);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width) {
		return MeosLibrary.meos.tfloat_shift_scale_value(temp, shift, width);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_shift_value(Pointer temp, double shift) {
		return MeosLibrary.meos.tfloat_shift_value(temp, shift);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_scale_value(Pointer temp, int width) {
		return MeosLibrary.meos.tint_scale_value(temp, width);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_shift_scale_value(Pointer temp, int shift, int width) {
		return MeosLibrary.meos.tint_shift_scale_value(temp, shift, width);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_shift_value(Pointer temp, int shift) {
		return MeosLibrary.meos.tint_shift_value(temp, shift);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_append_tinstant(Pointer temp, Pointer inst, int interp, double maxdist, Pointer maxt, boolean expand) {
		return MeosLibrary.meos.temporal_append_tinstant(temp, inst, interp, maxdist, maxt, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand) {
		return MeosLibrary.meos.temporal_append_tsequence(temp, seq, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzspan(Pointer temp, Pointer s, boolean connect) {
		return MeosLibrary.meos.temporal_delete_tstzspan(temp, s, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzspanset(Pointer temp, Pointer ss, boolean connect) {
		return MeosLibrary.meos.temporal_delete_tstzspanset(temp, ss, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_timestamptz(Pointer temp, OffsetDateTime t, boolean connect) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.temporal_delete_timestamptz(temp, t_new, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_tstzset(Pointer temp, Pointer s, boolean connect) {
		return MeosLibrary.meos.temporal_delete_tstzset(temp, s, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect) {
		return MeosLibrary.meos.temporal_insert(temp1, temp2, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_merge(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_merge(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_merge_array(Pointer temparr, int count) {
		return MeosLibrary.meos.temporal_merge_array(temparr, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect) {
		return MeosLibrary.meos.temporal_update(temp1, temp2, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_at_value(Pointer temp, boolean b) {
		return MeosLibrary.meos.tbool_at_value(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_minus_value(Pointer temp, boolean b) {
		return MeosLibrary.meos.tbool_minus_value(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_max(Pointer temp) {
		return MeosLibrary.meos.temporal_at_max(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_min(Pointer temp) {
		return MeosLibrary.meos.temporal_at_min(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.temporal_at_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzspanset(Pointer temp, Pointer ss) {
		return MeosLibrary.meos.temporal_at_tstzspanset(temp, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_timestamptz(Pointer temp, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.temporal_at_timestamptz(temp, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_tstzset(Pointer temp, Pointer s) {
		return MeosLibrary.meos.temporal_at_tstzset(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_values(Pointer temp, Pointer set) {
		return MeosLibrary.meos.temporal_at_values(temp, set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_max(Pointer temp) {
		return MeosLibrary.meos.temporal_minus_max(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_min(Pointer temp) {
		return MeosLibrary.meos.temporal_minus_min(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.temporal_minus_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzspanset(Pointer temp, Pointer ss) {
		return MeosLibrary.meos.temporal_minus_tstzspanset(temp, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_timestamptz(Pointer temp, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.temporal_minus_timestamptz(temp, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_tstzset(Pointer temp, Pointer s) {
		return MeosLibrary.meos.temporal_minus_tstzset(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_values(Pointer temp, Pointer set) {
		return MeosLibrary.meos.temporal_minus_values(temp, set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_at_value(Pointer temp, double d) {
		return MeosLibrary.meos.tfloat_at_value(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_minus_value(Pointer temp, double d) {
		return MeosLibrary.meos.tfloat_minus_value(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_at_value(Pointer temp, int i) {
		return MeosLibrary.meos.tint_at_value(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_minus_value(Pointer temp, int i) {
		return MeosLibrary.meos.tint_minus_value(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_at_span(Pointer temp, Pointer span) {
		return MeosLibrary.meos.tnumber_at_span(temp, span);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_at_spanset(Pointer temp, Pointer ss) {
		return MeosLibrary.meos.tnumber_at_spanset(temp, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_at_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.tnumber_at_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_span(Pointer temp, Pointer span) {
		return MeosLibrary.meos.tnumber_minus_span(temp, span);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_spanset(Pointer temp, Pointer ss) {
		return MeosLibrary.meos.tnumber_minus_spanset(temp, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_minus_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.tnumber_minus_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_at_value(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ttext_at_value(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_minus_value(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ttext_minus_value(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int temporal_cmp(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_cmp(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_eq(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_eq(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_ge(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_ge(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_gt(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_gt(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_le(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_le(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_lt(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_lt(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_ne(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_ne(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_bool_tbool(boolean b, Pointer temp) {
		return MeosLibrary.meos.always_eq_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.always_eq_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.always_eq_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tbool_bool(Pointer temp, boolean b) {
		return MeosLibrary.meos.always_eq_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.always_eq_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.always_eq_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.always_eq_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.always_eq_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.always_eq_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_bool_tbool(boolean b, Pointer temp) {
		return MeosLibrary.meos.always_ne_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.always_ne_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.always_ne_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tbool_bool(Pointer temp, boolean b) {
		return MeosLibrary.meos.always_ne_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.always_ne_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.always_ne_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.always_ne_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.always_ne_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.always_ne_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.always_ge_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.always_ge_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.always_ge_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.always_ge_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.always_ge_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.always_ge_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.always_ge_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.always_gt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.always_gt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.always_gt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.always_gt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.always_gt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.always_gt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.always_gt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.always_le_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.always_le_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.always_le_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.always_le_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.always_le_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.always_le_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.always_le_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.always_lt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.always_lt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.always_lt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.always_lt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.always_lt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.always_lt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.always_lt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_bool_tbool(boolean b, Pointer temp) {
		return MeosLibrary.meos.ever_eq_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.ever_eq_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.ever_eq_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tbool_bool(Pointer temp, boolean b) {
		return MeosLibrary.meos.ever_eq_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.ever_eq_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.ever_eq_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.ever_eq_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.ever_eq_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ever_eq_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.ever_ge_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.ever_ge_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.ever_ge_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.ever_ge_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.ever_ge_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.ever_ge_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ever_ge_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.ever_gt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.ever_gt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.ever_gt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.ever_gt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.ever_gt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.ever_gt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ever_gt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.ever_le_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.ever_le_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.ever_le_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.ever_le_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.ever_le_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.ever_le_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ever_le_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.ever_lt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.ever_lt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.ever_lt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.ever_lt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.ever_lt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.ever_lt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ever_lt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_bool_tbool(boolean b, Pointer temp) {
		return MeosLibrary.meos.ever_ne_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.ever_ne_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.ever_ne_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tbool_bool(Pointer temp, boolean b) {
		return MeosLibrary.meos.ever_ne_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.ever_ne_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.ever_ne_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.ever_ne_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.ever_ne_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ever_ne_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_bool_tbool(boolean b, Pointer temp) {
		return MeosLibrary.meos.teq_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.teq_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.teq_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_tbool_bool(Pointer temp, boolean b) {
		return MeosLibrary.meos.teq_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.teq_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.teq_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.teq_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.teq_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.teq_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.tge_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.tge_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.tge_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.tge_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.tge_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.tge_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tge_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.tge_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.tgt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.tgt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.tgt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.tgt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.tgt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.tgt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgt_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.tgt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.tle_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.tle_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.tle_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.tle_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.tle_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.tle_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tle_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.tle_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.tlt_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.tlt_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.tlt_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.tlt_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.tlt_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.tlt_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tlt_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.tlt_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_bool_tbool(boolean b, Pointer temp) {
		return MeosLibrary.meos.tne_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_float_tfloat(double d, Pointer temp) {
		return MeosLibrary.meos.tne_float_tfloat(d, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_int_tint(int i, Pointer temp) {
		return MeosLibrary.meos.tne_int_tint(i, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_tbool_bool(Pointer temp, boolean b) {
		return MeosLibrary.meos.tne_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.tne_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.tne_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.tne_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.tne_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.tne_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_spans(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_spans(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_split_each_n_spans(Pointer temp, int elem_count, Pointer count) {
		return MeosLibrary.meos.temporal_split_each_n_spans(temp, elem_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_split_n_spans(Pointer temp, int span_count, Pointer count) {
		return MeosLibrary.meos.temporal_split_n_spans(temp, span_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_tboxes(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tnumber_tboxes(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_split_each_n_tboxes(Pointer temp, int elem_count, Pointer count) {
		return MeosLibrary.meos.tnumber_split_each_n_tboxes(temp, elem_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_split_n_tboxes(Pointer temp, int box_count, Pointer count) {
		return MeosLibrary.meos.tnumber_split_n_tboxes(temp, box_count, count);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.adjacent_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.adjacent_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.adjacent_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.adjacent_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.adjacent_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.adjacent_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.adjacent_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.adjacent_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.contained_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.contained_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.contained_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.contained_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.contained_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.contained_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.contained_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.contained_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.contains_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.contains_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.contains_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.contains_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.contains_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.contains_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.contains_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.contains_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.overlaps_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overlaps_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overlaps_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.overlaps_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.overlaps_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overlaps_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overlaps_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.overlaps_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.same_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.same_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.same_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.same_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.same_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.same_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.same_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.same_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.after_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.after_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.after_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.after_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.after_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.after_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.before_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.before_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.before_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.before_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.before_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.before_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.left_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.left_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.left_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.left_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.left_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overafter_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.overafter_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overafter_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overafter_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overafter_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.overafter_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overbefore_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_temporal_tstzspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.overbefore_temporal_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overbefore_temporal_temporal(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overbefore_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overbefore_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tstzspan_temporal(Pointer s, Pointer temp) {
		return MeosLibrary.meos.overbefore_tstzspan_temporal(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.overleft_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overleft_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.overleft_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overleft_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overleft_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.overright_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overright_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.overright_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overright_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overright_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_numspan_tnumber(Pointer s, Pointer temp) {
		return MeosLibrary.meos.right_numspan_tnumber(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tbox_tnumber(Pointer box, Pointer temp) {
		return MeosLibrary.meos.right_tbox_tnumber(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tnumber_numspan(Pointer temp, Pointer s) {
		return MeosLibrary.meos.right_tnumber_numspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.right_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.right_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tand_bool_tbool(boolean b, Pointer temp) {
		return MeosLibrary.meos.tand_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tand_tbool_bool(Pointer temp, boolean b) {
		return MeosLibrary.meos.tand_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tand_tbool_tbool(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.tand_tbool_tbool(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_when_true(Pointer temp) {
		return MeosLibrary.meos.tbool_when_true(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnot_tbool(Pointer temp) {
		return MeosLibrary.meos.tnot_tbool(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tor_bool_tbool(boolean b, Pointer temp) {
		return MeosLibrary.meos.tor_bool_tbool(b, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tor_tbool_bool(Pointer temp, boolean b) {
		return MeosLibrary.meos.tor_tbool_bool(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tor_tbool_tbool(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.tor_tbool_tbool(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_float_tfloat(double d, Pointer tnumber) {
		return MeosLibrary.meos.add_float_tfloat(d, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_int_tint(int i, Pointer tnumber) {
		return MeosLibrary.meos.add_int_tint(i, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_tfloat_float(Pointer tnumber, double d) {
		return MeosLibrary.meos.add_tfloat_float(tnumber, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_tint_int(Pointer tnumber, int i) {
		return MeosLibrary.meos.add_tint_int(tnumber, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer add_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		return MeosLibrary.meos.add_tnumber_tnumber(tnumber1, tnumber2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_float_tfloat(double d, Pointer tnumber) {
		return MeosLibrary.meos.div_float_tfloat(d, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_int_tint(int i, Pointer tnumber) {
		return MeosLibrary.meos.div_int_tint(i, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_tfloat_float(Pointer tnumber, double d) {
		return MeosLibrary.meos.div_tfloat_float(tnumber, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_tint_int(Pointer tnumber, int i) {
		return MeosLibrary.meos.div_tint_int(tnumber, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer div_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		return MeosLibrary.meos.div_tnumber_tnumber(tnumber1, tnumber2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_float_tfloat(double d, Pointer tnumber) {
		return MeosLibrary.meos.mult_float_tfloat(d, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_int_tint(int i, Pointer tnumber) {
		return MeosLibrary.meos.mult_int_tint(i, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_tfloat_float(Pointer tnumber, double d) {
		return MeosLibrary.meos.mult_tfloat_float(tnumber, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_tint_int(Pointer tnumber, int i) {
		return MeosLibrary.meos.mult_tint_int(tnumber, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer mult_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		return MeosLibrary.meos.mult_tnumber_tnumber(tnumber1, tnumber2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_float_tfloat(double d, Pointer tnumber) {
		return MeosLibrary.meos.sub_float_tfloat(d, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_int_tint(int i, Pointer tnumber) {
		return MeosLibrary.meos.sub_int_tint(i, tnumber);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_tfloat_float(Pointer tnumber, double d) {
		return MeosLibrary.meos.sub_tfloat_float(tnumber, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_tint_int(Pointer tnumber, int i) {
		return MeosLibrary.meos.sub_tint_int(tnumber, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer sub_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2) {
		return MeosLibrary.meos.sub_tnumber_tnumber(tnumber1, tnumber2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_derivative(Pointer temp) {
		return MeosLibrary.meos.tfloat_derivative(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_exp(Pointer temp) {
		return MeosLibrary.meos.tfloat_exp(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_ln(Pointer temp) {
		return MeosLibrary.meos.tfloat_ln(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_log10(Pointer temp) {
		return MeosLibrary.meos.tfloat_log10(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_abs(Pointer temp) {
		return MeosLibrary.meos.tnumber_abs(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_angular_difference(Pointer temp) {
		return MeosLibrary.meos.tnumber_angular_difference(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_delta_value(Pointer temp) {
		return MeosLibrary.meos.tnumber_delta_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_text_ttext(Pointer txt, Pointer temp) {
		return MeosLibrary.meos.textcat_text_ttext(txt, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_ttext_text(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.textcat_ttext_text(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.textcat_ttext_ttext(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_upper(Pointer temp) {
		return MeosLibrary.meos.ttext_upper(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_lower(Pointer temp) {
		return MeosLibrary.meos.ttext_lower(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_initcap(Pointer temp) {
		return MeosLibrary.meos.ttext_initcap(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.distance_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.distance_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.distance_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.nad_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tint_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.nad_tint_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tint_tint(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.nad_tint_tint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tboxint_tboxint(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.nad_tboxint_tboxint(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tfloat_float(Pointer temp, double d) {
		return MeosLibrary.meos.nad_tfloat_float(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tfloat_tfloat(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.nad_tfloat_tfloat(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tfloat_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.nad_tfloat_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tboxfloat_tboxfloat(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.nad_tboxfloat_tboxfloat(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_tand_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tbool_tand_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbool_tor_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tbool_tor_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_extent_transfn(Pointer s, Pointer temp) {
		return MeosLibrary.meos.temporal_extent_transfn(s, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tagg_finalfn(Pointer state) {
		return MeosLibrary.meos.temporal_tagg_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tcount_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.temporal_tcount_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_tmax_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tfloat_tmax_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_tmin_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tfloat_tmin_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_tsum_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tfloat_tsum_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_wmax_transfn(Pointer state, Pointer temp, Pointer interv) {
		return MeosLibrary.meos.tfloat_wmax_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_wmin_transfn(Pointer state, Pointer temp, Pointer interv) {
		return MeosLibrary.meos.tfloat_wmin_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_wsum_transfn(Pointer state, Pointer temp, Pointer interv) {
		return MeosLibrary.meos.tfloat_wsum_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_tcount_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_tcount_transfn(state, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_tmax_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tint_tmax_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_tmin_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tint_tmin_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_tsum_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tint_tsum_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_wmax_transfn(Pointer state, Pointer temp, Pointer interv) {
		return MeosLibrary.meos.tint_wmax_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_wmin_transfn(Pointer state, Pointer temp, Pointer interv) {
		return MeosLibrary.meos.tint_wmin_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_wsum_transfn(Pointer state, Pointer temp, Pointer interv) {
		return MeosLibrary.meos.tint_wsum_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_extent_transfn(Pointer box, Pointer temp) {
		return MeosLibrary.meos.tnumber_extent_transfn(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_tavg_finalfn(Pointer state) {
		return MeosLibrary.meos.tnumber_tavg_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_tavg_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tnumber_tavg_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_wavg_transfn(Pointer state, Pointer temp, Pointer interv) {
		return MeosLibrary.meos.tnumber_wavg_transfn(state, temp, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_tcount_transfn(Pointer state, Pointer s) {
		return MeosLibrary.meos.tstzset_tcount_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_tcount_transfn(Pointer state, Pointer s) {
		return MeosLibrary.meos.tstzspan_tcount_transfn(state, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_tcount_transfn(Pointer state, Pointer ss) {
		return MeosLibrary.meos.tstzspanset_tcount_transfn(state, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_tmax_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.ttext_tmax_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_tmin_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.ttext_tmin_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_dp(Pointer temp, double eps_dist, boolean synchronize) {
		return MeosLibrary.meos.temporal_simplify_dp(temp, eps_dist, synchronize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_max_dist(Pointer temp, double eps_dist, boolean synchronize) {
		return MeosLibrary.meos.temporal_simplify_max_dist(temp, eps_dist, synchronize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_dist(Pointer temp, double dist) {
		return MeosLibrary.meos.temporal_simplify_min_dist(temp, dist);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_tdelta(Pointer temp, Pointer mint) {
		return MeosLibrary.meos.temporal_simplify_min_tdelta(temp, mint);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tprecision(Pointer temp, Pointer duration, OffsetDateTime origin) {
		var origin_new = origin.toEpochSecond();
		return MeosLibrary.meos.temporal_tprecision(temp, duration, origin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tsample(Pointer temp, Pointer duration, OffsetDateTime origin, int interp) {
		var origin_new = origin.toEpochSecond();
		return MeosLibrary.meos.temporal_tsample(temp, duration, origin_new, interp);
	}
	
	@SuppressWarnings("unused")
	public static double temporal_dyntimewarp_distance(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_dyntimewarp_distance(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_dyntimewarp_path(Pointer temp1, Pointer temp2, Pointer count) {
		return MeosLibrary.meos.temporal_dyntimewarp_path(temp1, temp2, count);
	}
	
	@SuppressWarnings("unused")
	public static double temporal_frechet_distance(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_frechet_distance(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_frechet_path(Pointer temp1, Pointer temp2, Pointer count) {
		return MeosLibrary.meos.temporal_frechet_path(temp1, temp2, count);
	}
	
	@SuppressWarnings("unused")
	public static double temporal_hausdorff_distance(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.temporal_hausdorff_distance(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static long bigint_get_bin(long value, long vsize, long vorigin) {
		return MeosLibrary.meos.bigint_get_bin(value, vsize, vorigin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspan_bins(Pointer s, long vsize, long vorigin, Pointer count) {
		return MeosLibrary.meos.bigintspan_bins(s, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspanset_bins(Pointer ss, long vsize, long vorigin, Pointer count) {
		return MeosLibrary.meos.bigintspanset_bins(ss, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigintspanset_value_spans(Pointer ss, long vsize, long vorigin, Pointer count) {
		return MeosLibrary.meos.bigintspanset_value_spans(ss, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static int date_get_bin(int d, Pointer duration, int torigin) {
		return MeosLibrary.meos.date_get_bin(d, duration, torigin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespan_bins(Pointer s, Pointer duration, int torigin, Pointer count) {
		return MeosLibrary.meos.datespan_bins(s, duration, torigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_bins(Pointer ss, Pointer duration, int torigin, Pointer count) {
		return MeosLibrary.meos.datespanset_bins(ss, duration, torigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer datespanset_time_spans(Pointer ss, Pointer duration, int torigin, Pointer count) {
		return MeosLibrary.meos.datespanset_time_spans(ss, duration, torigin, count);
	}
	
	@SuppressWarnings("unused")
	public static double float_get_bin(double value, double vsize, double vorigin) {
		return MeosLibrary.meos.float_get_bin(value, vsize, vorigin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_bins(Pointer s, double vsize, double vorigin, Pointer count) {
		return MeosLibrary.meos.floatspan_bins(s, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspanset_value_spans(Pointer ss, double vsize, double vorigin, Pointer count) {
		return MeosLibrary.meos.floatspanset_value_spans(ss, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static int int_get_bin(int value, int vsize, int vorigin) {
		return MeosLibrary.meos.int_get_bin(value, vsize, vorigin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_bins(Pointer s, int vsize, int vorigin, Pointer count) {
		return MeosLibrary.meos.intspan_bins(s, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_bins(Pointer ss, int vsize, int vorigin, Pointer count) {
		return MeosLibrary.meos.intspanset_bins(ss, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_value_spans(Pointer ss, int vsize, int vorigin, Pointer count) {
		return MeosLibrary.meos.intspanset_value_spans(ss, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_get_bin(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var result = MeosLibrary.meos.timestamptz_get_bin(t_new, duration, torigin_new);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_bins(Pointer s, Pointer duration, OffsetDateTime origin, Pointer count) {
		var origin_new = origin.toEpochSecond();
		return MeosLibrary.meos.tstzspan_bins(s, duration, origin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_bins(Pointer ss, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tstzspanset_bins(ss, duration, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_time_spans(Pointer ss, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tstzspanset_time_spans(ss, duration, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_time_spans(Pointer temp, Pointer duration, OffsetDateTime origin, Pointer count) {
		var origin_new = origin.toEpochSecond();
		return MeosLibrary.meos.temporal_time_spans(temp, duration, origin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_time_split(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.temporal_time_split(temp, duration, torigin_new, time_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_spans(Pointer temp, double vsize, double vorigin, Pointer count) {
		return MeosLibrary.meos.tfloat_value_spans(temp, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_split(Pointer temp, double vsize, double vorigin, Pointer value_bins, Pointer count) {
		return MeosLibrary.meos.tfloat_value_split(temp, vsize, vorigin, value_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_time_split(Pointer temp, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer value_bins, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tfloat_value_time_split(temp, vsize, duration, vorigin, torigin_new, value_bins, time_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_get_time_tile(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tfloatbox_get_time_tile(t_new, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_get_value_tile(double value, double vsize, double vorigin) {
		return MeosLibrary.meos.tfloatbox_get_value_tile(value, vsize, vorigin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_get_value_time_tile(double value, OffsetDateTime t, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tfloatbox_get_value_time_tile(value, t_new, vsize, duration, vorigin, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_time_tiles(Pointer box, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tfloatbox_time_tiles(box, duration, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_value_tiles(Pointer box, double vsize, double vorigin, Pointer count) {
		return MeosLibrary.meos.tfloatbox_value_tiles(box, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_value_time_tiles(Pointer box, double vsize, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tfloatbox_value_time_tiles(box, vsize, duration, vorigin, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_spans(Pointer temp, int vsize, int vorigin, Pointer count) {
		return MeosLibrary.meos.tint_value_spans(temp, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_split(Pointer temp, int vsize, int vorigin, Pointer value_bins, Pointer count) {
		return MeosLibrary.meos.tint_value_split(temp, vsize, vorigin, value_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_time_split(Pointer temp, int size, Pointer duration, int vorigin, OffsetDateTime torigin, Pointer value_bins, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tint_value_time_split(temp, size, duration, vorigin, torigin_new, value_bins, time_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_get_time_tile(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tintbox_get_time_tile(t_new, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_get_value_tile(int value, int vsize, int vorigin) {
		return MeosLibrary.meos.tintbox_get_value_tile(value, vsize, vorigin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_get_value_time_tile(int value, OffsetDateTime t, int vsize, Pointer duration, int vorigin, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tintbox_get_value_time_tile(value, t_new, vsize, duration, vorigin, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_time_tiles(Pointer box, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tintbox_time_tiles(box, duration, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_value_tiles(Pointer box, int xsize, int xorigin, Pointer count) {
		return MeosLibrary.meos.tintbox_value_tiles(box, xsize, xorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_value_time_tiles(Pointer box, int xsize, Pointer duration, int xorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tintbox_value_time_tiles(box, xsize, duration, xorigin, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_as_ewkb(Pointer gs, String endian, Pointer size) {
		return MeosLibrary.meos.geo_as_ewkb(gs, endian, size);
	}
	
	@SuppressWarnings("unused")
	public static String geo_as_ewkt(Pointer gs, int precision) {
		return MeosLibrary.meos.geo_as_ewkt(gs, precision);
	}
	
	@SuppressWarnings("unused")
	public static String geo_as_geojson(Pointer gs, int option, int precision, String srs) {
		return MeosLibrary.meos.geo_as_geojson(gs, option, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static String geo_as_hexewkb(Pointer gs, String endian) {
		return MeosLibrary.meos.geo_as_hexewkb(gs, endian);
	}
	
	@SuppressWarnings("unused")
	public static String geo_as_text(Pointer gs, int precision) {
		return MeosLibrary.meos.geo_as_text(gs, precision);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_collect_garray(Pointer gsarr, int count) {
		return MeosLibrary.meos.geo_collect_garray(gsarr, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_copy(Pointer g) {
		return MeosLibrary.meos.geo_copy(g);
	}
	
	@SuppressWarnings("unused")
	public static int geo_equals(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geo_equals(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_from_ewkb(Pointer wkb, long wkb_size, int srid) {
		return MeosLibrary.meos.geo_from_ewkb(wkb, wkb_size, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_from_geojson(String geojson) {
		return MeosLibrary.meos.geo_from_geojson(geojson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_from_text(String wkt, int srid) {
		return MeosLibrary.meos.geo_from_text(wkt, srid);
	}
	
	@SuppressWarnings("unused")
	public static boolean geo_is_empty(Pointer g) {
		return MeosLibrary.meos.geo_is_empty(g);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_makeline_garray(Pointer gsarr, int count) {
		return MeosLibrary.meos.geo_makeline_garray(gsarr, count);
	}
	
	@SuppressWarnings("unused")
	public static String geo_out(Pointer gs) {
		return MeosLibrary.meos.geo_out(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_reverse(Pointer gs) {
		return MeosLibrary.meos.geo_reverse(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_round(Pointer gs, int maxdd) {
		return MeosLibrary.meos.geo_round(gs, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static boolean geo_same(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geo_same(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer  geo_set_srid(Pointer gs, int srid) {
		return MeosLibrary.meos.geo_set_srid(gs, srid);
	}
	
	@SuppressWarnings("unused")
	public static int geo_srid(Pointer gs) {
		return MeosLibrary.meos.geo_srid(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_transform(Pointer geom, int srid_to) {
		return MeosLibrary.meos.geo_transform(geom, srid_to);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_transform_pipeline(Pointer gs, String pipeline, int srid_to, boolean is_forward) {
		return MeosLibrary.meos.geo_transform_pipeline(gs, pipeline, srid_to, is_forward);
	}
	
	@SuppressWarnings("unused")
	public static double geog_area(Pointer g, boolean use_spheroid) {
		return MeosLibrary.meos.geog_area(g, use_spheroid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geog_centroid(Pointer g, boolean use_spheroid) {
		return MeosLibrary.meos.geog_centroid(g, use_spheroid);
	}
	
	@SuppressWarnings("unused")
	public static double geog_distance(Pointer g1, Pointer g2) {
		return MeosLibrary.meos.geog_distance(g1, g2);
	}
	
	@SuppressWarnings("unused")
	public static boolean geog_dwithin(Pointer g1, Pointer g2, double tolerance, boolean use_spheroid) {
		return MeosLibrary.meos.geog_dwithin(g1, g2, tolerance, use_spheroid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geog_from_binary(String wkb_bytea) {
		return MeosLibrary.meos.geog_from_binary(wkb_bytea);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geog_from_geom(Pointer geom) {
		return MeosLibrary.meos.geog_from_geom(geom);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geog_from_hexewkb(String wkt) {
		return MeosLibrary.meos.geog_from_hexewkb(wkt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geog_in(String str, int typmod) {
		return MeosLibrary.meos.geog_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static boolean geog_intersects(Pointer gs1, Pointer gs2, boolean use_spheroid) {
		return MeosLibrary.meos.geog_intersects(gs1, gs2, use_spheroid);
	}
	
	@SuppressWarnings("unused")
	public static double geog_length(Pointer g, boolean use_spheroid) {
		return MeosLibrary.meos.geog_length(g, use_spheroid);
	}
	
	@SuppressWarnings("unused")
	public static double geog_perimeter(Pointer g, boolean use_spheroid) {
		return MeosLibrary.meos.geog_perimeter(g, use_spheroid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_array_union(Pointer gsarr, int count) {
		return MeosLibrary.meos.geom_array_union(gsarr, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_azimuth(Pointer gs1, Pointer gs2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.geom_azimuth(gs1, gs2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_boundary(Pointer gs) {
		return MeosLibrary.meos.geom_boundary(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_buffer(Pointer gs, double size, String params) {
		return MeosLibrary.meos.geom_buffer(gs, size, params);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_centroid(Pointer gs) {
		return MeosLibrary.meos.geom_centroid(gs);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_contains(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_contains(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_convex_hull(Pointer gs) {
		return MeosLibrary.meos.geom_convex_hull(gs);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_covers(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_covers(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_difference2d(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_difference2d(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_disjoint2d(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_disjoint2d(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static double geom_distance2d(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_distance2d(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static double geom_distance3d(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_distance3d(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_dwithin2d(Pointer gs1, Pointer gs2, double tolerance) {
		return MeosLibrary.meos.geom_dwithin2d(gs1, gs2, tolerance);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_dwithin3d(Pointer gs1, Pointer gs2, double tolerance) {
		return MeosLibrary.meos.geom_dwithin3d(gs1, gs2, tolerance);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_from_geog(Pointer geog) {
		return MeosLibrary.meos.geom_from_geog(geog);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_from_hexewkb(String wkt) {
		return MeosLibrary.meos.geom_from_hexewkb(wkt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_from_text(String wkt, int srid) {
		return MeosLibrary.meos.geom_from_text(wkt, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_in(String str, int typmod) {
		return MeosLibrary.meos.geom_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_intersection2d(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_intersection2d(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_intersects2d(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_intersects2d(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_intersects3d(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_intersects3d(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static double geom_length(Pointer gs) {
		return MeosLibrary.meos.geom_length(gs);
	}
	
	@SuppressWarnings("unused")
	public static double geom_perimeter(Pointer gs) {
		return MeosLibrary.meos.geom_perimeter(gs);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_relate_pattern(Pointer gs1, Pointer gs2, String patt) {
		return MeosLibrary.meos.geom_relate_pattern(gs1, gs2, patt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_shortestline2d(Pointer gs1, Pointer s2) {
		return MeosLibrary.meos.geom_shortestline2d(gs1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_shortestline3d(Pointer gs1, Pointer s2) {
		return MeosLibrary.meos.geom_shortestline3d(gs1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean geom_touches(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.geom_touches(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geom_unary_union(Pointer gs, double prec) {
		return MeosLibrary.meos.geom_unary_union(gs, prec);
	}
	
	@SuppressWarnings("unused")
	public static Pointer line_interpolate_point(Pointer gs, double distance_fraction, boolean repeat) {
		return MeosLibrary.meos.line_interpolate_point(gs, distance_fraction, repeat);
	}
	
	@SuppressWarnings("unused")
	public static int line_numpoints(Pointer gs) {
		return MeosLibrary.meos.line_numpoints(gs);
	}
	
	@SuppressWarnings("unused")
	public static double line_locate_point(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.line_locate_point(gs1, gs2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer line_point_n(Pointer geom, int n) {
		return MeosLibrary.meos.line_point_n(geom, n);
	}
	
	@SuppressWarnings("unused")
	public static Pointer line_substring(Pointer gs, double from, double to) {
		return MeosLibrary.meos.line_substring(gs, from, to);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_stboxes(Pointer gs, Pointer count) {
		return MeosLibrary.meos.geo_stboxes(gs, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_split_n_stboxes(Pointer gs, int box_count, Pointer count) {
		return MeosLibrary.meos.geo_split_n_stboxes(gs, box_count, count);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_geo_set(Pointer gs, Pointer s) {
		return MeosLibrary.meos.contained_geo_set(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_geo(Pointer s, Pointer gs) {
		return MeosLibrary.meos.contains_set_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_set(Pointer gs) {
		return MeosLibrary.meos.geo_set(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_end_value(Pointer s) {
		return MeosLibrary.meos.geoset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_make(Pointer values, int count) {
		return MeosLibrary.meos.geoset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_start_value(Pointer s) {
		return MeosLibrary.meos.geoset_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.geoset_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_values(Pointer s) {
		return MeosLibrary.meos.geoset_values(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_geo_set(Pointer gs, Pointer s) {
		return MeosLibrary.meos.intersection_geo_set(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_geo(Pointer s, Pointer gs) {
		return MeosLibrary.meos.intersection_set_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_geo_set(Pointer gs, Pointer s) {
		return MeosLibrary.meos.minus_geo_set(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_geo(Pointer s, Pointer gs) {
		return MeosLibrary.meos.minus_set_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static int spatialset_srid(Pointer s) {
		return MeosLibrary.meos.spatialset_srid(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spatialset_set_srid(Pointer s, int srid) {
		return MeosLibrary.meos.spatialset_set_srid(s, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spatialset_transform(Pointer s, int srid) {
		return MeosLibrary.meos.spatialset_transform(s, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spatialset_transform_pipeline(Pointer s, String pipelinestr, int srid, boolean is_forward) {
		return MeosLibrary.meos.spatialset_transform_pipeline(s, pipelinestr, srid, is_forward);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_geo_set(Pointer gs, Pointer s) {
		return MeosLibrary.meos.union_geo_set(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_geo(Pointer s, Pointer gs) {
		return MeosLibrary.meos.union_set_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_from_wkb(Pointer wkb, long size) {
		return MeosLibrary.meos.stbox_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_from_hexwkb(String hexwkb) {
		return MeosLibrary.meos.stbox_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_as_wkb(Pointer box, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.stbox_as_wkb(box, variant, size_out);
	}
	
	@SuppressWarnings("unused")
	public static String stbox_as_hexwkb(Pointer box, byte variant, Pointer size) {
		return MeosLibrary.meos.stbox_as_hexwkb(box, variant, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_in(String str) {
		return MeosLibrary.meos.stbox_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String stbox_out(Pointer box, int maxdd) {
		return MeosLibrary.meos.stbox_out(box, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_tstzspan_to_stbox(Pointer gs, Pointer s) {
		return MeosLibrary.meos.geo_tstzspan_to_stbox(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_timestamptz_to_stbox(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.geo_timestamptz_to_stbox(gs, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_copy(Pointer box) {
		return MeosLibrary.meos.stbox_copy(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s) {
		return MeosLibrary.meos.stbox_make(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_stbox(Pointer gs) {
		return MeosLibrary.meos.geo_stbox(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_geo(Pointer box) {
		return MeosLibrary.meos.stbox_geo(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spatialset_stbox(Pointer s) {
		return MeosLibrary.meos.spatialset_stbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_gbox(Pointer box) {
		return MeosLibrary.meos.stbox_gbox(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_box3d(Pointer box) {
		return MeosLibrary.meos.stbox_box3d(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tstzspan(Pointer box) {
		return MeosLibrary.meos.stbox_tstzspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamptz_stbox(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_stbox(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzset_stbox(Pointer s) {
		return MeosLibrary.meos.tstzset_stbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspan_stbox(Pointer s) {
		return MeosLibrary.meos.tstzspan_stbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tstzspanset_stbox(Pointer ss) {
		return MeosLibrary.meos.tstzspanset_stbox(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tspatial_stbox(Pointer temp) {
		return MeosLibrary.meos.tspatial_stbox(temp);
	}
	
	@SuppressWarnings("unused")
	public static double stbox_area(Pointer box, boolean spheroid) {
		return MeosLibrary.meos.stbox_area(box, spheroid);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_hast(Pointer box) {
		return MeosLibrary.meos.stbox_hast(box);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_hasx(Pointer box) {
		return MeosLibrary.meos.stbox_hasx(box);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_hasz(Pointer box) {
		return MeosLibrary.meos.stbox_hasz(box);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_isgeodetic(Pointer box) {
		return MeosLibrary.meos.stbox_isgeodetic(box);
	}
	
	@SuppressWarnings("unused")
	public static double stbox_perimeter(Pointer box, boolean spheroid) {
		return MeosLibrary.meos.stbox_perimeter(box, spheroid);
	}
	
	@SuppressWarnings("unused")
	public static int stbox_srid(Pointer box) {
		return MeosLibrary.meos.stbox_srid(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_tmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tmax_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_tmax_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_tmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tmin_inc(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_tmin_inc(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static double stbox_volume(Pointer box) {
		return MeosLibrary.meos.stbox_volume(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_xmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_xmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_xmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_xmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_ymax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_ymax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_ymin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_ymin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_zmax(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_zmax(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_zmin(Pointer box) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.stbox_zmin(box, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_expand_space(Pointer box, double d) {
		return MeosLibrary.meos.stbox_expand_space(box, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_expand_time(Pointer box, Pointer interv) {
		return MeosLibrary.meos.stbox_expand_time(box, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_get_space(Pointer box) {
		return MeosLibrary.meos.stbox_get_space(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_quad_split(Pointer box, Pointer count) {
		return MeosLibrary.meos.stbox_quad_split(box, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_round(Pointer box, int maxdd) {
		return MeosLibrary.meos.stbox_round(box, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_set_srid(Pointer box, int srid) {
		return MeosLibrary.meos.stbox_set_srid(box, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.stbox_shift_scale_time(box, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_transform(Pointer box, int srid) {
		return MeosLibrary.meos.stbox_transform(box, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_transform_pipeline(Pointer box, String pipelinestr, int srid, boolean is_forward) {
		return MeosLibrary.meos.stbox_transform_pipeline(box, pipelinestr, srid, is_forward);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stboxarr_round(Pointer boxarr, int count, int maxdd) {
		return MeosLibrary.meos.stboxarr_round(boxarr, count, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict) {
		return MeosLibrary.meos.union_stbox_stbox(box1, box2, strict);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.intersection_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.adjacent_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.contained_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.contains_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overlaps_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.same_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.left_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overleft_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.right_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overright_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean below_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.below_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbelow_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overbelow_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean above_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.above_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overabove_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overabove_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean front_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.front_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overfront_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overfront_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean back_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.back_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overback_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overback_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.before_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overbefore_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.after_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.overafter_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_eq(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.stbox_eq(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_ne(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.stbox_ne(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static int stbox_cmp(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.stbox_cmp(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_lt(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.stbox_lt(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_le(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.stbox_le(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_ge(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.stbox_ge(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean stbox_gt(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.stbox_gt(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer rtree_create_stbox() {
		return MeosLibrary.meos.rtree_create_stbox();
	}
	
	@SuppressWarnings("unused")
	public static void rtree_insert(Pointer rtree, Pointer box, long id) {
		MeosLibrary.meos.rtree_insert(rtree, box, id);
	}
	
	@SuppressWarnings("unused")
	public static Pointer rtree_search(Pointer rtree,Pointer query, Pointer count) {
		return MeosLibrary.meos.rtree_search(rtree, query, count);
	}
	
	@SuppressWarnings("unused")
	public static void rtree_free(Pointer rtree) {
		MeosLibrary.meos.rtree_free(rtree);
	}
	
	@SuppressWarnings("unused")
	public static String tgeo_out(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tgeo_out(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tspatial_as_text(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tspatial_as_text(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tspatial_as_ewkt(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tspatial_as_ewkt(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_in(String str) {
		return MeosLibrary.meos.tgeogpoint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_from_mfjson(String str) {
		return MeosLibrary.meos.tgeogpoint_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeography_in(String str) {
		return MeosLibrary.meos.tgeography_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometry_in(String str) {
		return MeosLibrary.meos.tgeometry_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_in(String str) {
		return MeosLibrary.meos.tgeompoint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_from_mfjson(String str) {
		return MeosLibrary.meos.tgeompoint_from_mfjson(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geogset_in(String str) {
		return MeosLibrary.meos.geogset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geomset_in(String str) {
		return MeosLibrary.meos.geomset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_from_base_temp(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.tgeo_from_base_temp(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoinst_make(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tgeoinst_make(gs, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoseq_from_base_tstzset(Pointer gs, Pointer s) {
		return MeosLibrary.meos.tgeoseq_from_base_tstzset(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp) {
		return MeosLibrary.meos.tgeoseqset_from_base_tstzspanset(gs, ss, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_from_base_temp(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.tpoint_from_base_temp(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointinst_make(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tpointinst_make(gs, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_tstzspan(Pointer gs, Pointer s, int interp) {
		return MeosLibrary.meos.tpointseq_from_base_tstzspan(gs, s, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_tstzset(Pointer gs, Pointer s) {
		return MeosLibrary.meos.tpointseq_from_base_tstzset(gs, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_from_base_tstzspanset(Pointer gs, Pointer ss, int interp) {
		return MeosLibrary.meos.tpointseqset_from_base_tstzspanset(gs, ss, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_end_value(Pointer temp) {
		return MeosLibrary.meos.tgeo_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_start_value(Pointer temp) {
		return MeosLibrary.meos.tgeo_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tgeo_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tgeo_value_at_timestamptz(temp, t_new, strict, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tgeo_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tgeo_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_traversed_area(Pointer temp) {
		return MeosLibrary.meos.tgeo_traversed_area(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_centroid(Pointer temp) {
		return MeosLibrary.meos.tgeo_centroid(temp);
	}
	
	@SuppressWarnings("unused")
	public static int tspatial_srid(Pointer temp) {
		return MeosLibrary.meos.tspatial_srid(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tspatial_set_srid(Pointer temp, int srid) {
		return MeosLibrary.meos.tspatial_set_srid(temp, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tspatial_transform(Pointer temp, int srid) {
		return MeosLibrary.meos.tspatial_transform(temp, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tspatial_transform_pipeline(Pointer temp, String pipelinestr, int srid, boolean is_forward) {
		return MeosLibrary.meos.tspatial_transform_pipeline(temp, pipelinestr, srid, is_forward);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_at_geom(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.tgeo_at_geom(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_at_stbox(Pointer temp, Pointer box, boolean border_inc) {
		return MeosLibrary.meos.tgeo_at_stbox(temp, box, border_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_minus_geom(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.tgeo_minus_geom(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_minus_stbox(Pointer temp, Pointer box, boolean border_inc) {
		return MeosLibrary.meos.tgeo_minus_stbox(temp, box, border_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_at_geom(Pointer temp, Pointer gs, Pointer zspan) {
		return MeosLibrary.meos.tpoint_at_geom(temp, gs, zspan);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_at_stbox(Pointer temp, Pointer box, boolean border_inc) {
		return MeosLibrary.meos.tpoint_at_stbox(temp, box, border_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_at_value(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.tpoint_at_value(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_geom(Pointer temp, Pointer gs, Pointer zspan) {
		return MeosLibrary.meos.tpoint_minus_geom(temp, gs, zspan);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_stbox(Pointer temp, Pointer box, boolean border_inc) {
		return MeosLibrary.meos.tpoint_minus_stbox(temp, box, border_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_minus_value(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.tpoint_minus_value(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_geo_tgeo(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.always_eq_geo_tgeo(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.always_eq_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.always_eq_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_geo_tgeo(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.always_ne_geo_tgeo(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.always_ne_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.always_ne_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_geo_tgeo(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.ever_eq_geo_tgeo(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.ever_eq_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.ever_eq_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_geo_tgeo(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.ever_ne_geo_tgeo(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.ever_ne_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.ever_ne_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_geo_tgeo(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.teq_geo_tgeo(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer teq_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.teq_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_geo_tgeo(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.tne_geo_tgeo(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tne_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.tne_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_split_each_n_stboxes(Pointer gs, int elem_count, Pointer count) {
		return MeosLibrary.meos.geo_split_each_n_stboxes(gs, elem_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_stboxes(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tgeo_stboxes(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_space_boxes(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer count) {
		return MeosLibrary.meos.tgeo_space_boxes(temp, xsize, ysize, zsize, sorigin, bitmatrix, border_inc, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_space_time_boxes(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean bitmatrix, boolean border_inc, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tgeo_space_time_boxes(temp, xsize, ysize, zsize, duration, sorigin, torigin_new, bitmatrix, border_inc, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_split_each_n_stboxes(Pointer temp, int elem_count, Pointer count) {
		return MeosLibrary.meos.tgeo_split_each_n_stboxes(temp, elem_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_split_n_stboxes(Pointer temp, int box_count, Pointer count) {
		return MeosLibrary.meos.tgeo_split_n_stboxes(temp, box_count, count);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.adjacent_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.adjacent_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.adjacent_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.contained_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.contained_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.contained_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.contains_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.contains_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.contains_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overlaps_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overlaps_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overlaps_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.same_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.same_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean same_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.same_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean above_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.above_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean above_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.above_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean above_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.above_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.after_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.after_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.after_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean back_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.back_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean back_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.back_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean back_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.back_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.before_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.before_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.before_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean below_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.below_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean below_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.below_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean below_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.below_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean front_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.front_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean front_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.front_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean front_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.front_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.left_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.left_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.left_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overabove_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overabove_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overabove_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overabove_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overabove_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overabove_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overafter_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overafter_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overafter_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overback_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overback_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overback_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overback_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overback_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overback_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overbefore_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overbefore_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overbefore_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbelow_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overbelow_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbelow_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overbelow_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbelow_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overbelow_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overfront_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overfront_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overfront_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overfront_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overfront_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overfront_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overleft_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overleft_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overleft_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.overright_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.overright_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.overright_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_stbox_tspatial(Pointer box, Pointer temp) {
		return MeosLibrary.meos.right_stbox_tspatial(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tspatial_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.right_tspatial_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_tspatial_tspatial(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.right_tspatial_tspatial(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bearing_point_point(Pointer gs1, Pointer gs2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.bearing_point_point(gs1, gs2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert) {
		return MeosLibrary.meos.bearing_tpoint_point(temp, gs, invert);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.bearing_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_gboxes(Pointer gs, Pointer count) {
		return MeosLibrary.meos.geo_gboxes(gs, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_angular_difference(Pointer temp) {
		return MeosLibrary.meos.tpoint_angular_difference(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_azimuth(Pointer temp) {
		return MeosLibrary.meos.tpoint_azimuth(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_convex_hull(Pointer temp) {
		return MeosLibrary.meos.tgeo_convex_hull(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_cumulative_length(Pointer temp) {
		return MeosLibrary.meos.tpoint_cumulative_length(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_direction(Pointer temp) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tpoint_direction(temp, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_get_x(Pointer temp) {
		return MeosLibrary.meos.tpoint_get_x(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_get_y(Pointer temp) {
		return MeosLibrary.meos.tpoint_get_y(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_get_z(Pointer temp) {
		return MeosLibrary.meos.tpoint_get_z(temp);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpoint_is_simple(Pointer temp) {
		return MeosLibrary.meos.tpoint_is_simple(temp);
	}
	
	@SuppressWarnings("unused")
	public static double tpoint_length(Pointer temp) {
		return MeosLibrary.meos.tpoint_length(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_speed(Pointer temp) {
		return MeosLibrary.meos.tpoint_speed(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_trajectory(Pointer temp) {
		return MeosLibrary.meos.tpoint_trajectory(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_twcentroid(Pointer temp) {
		return MeosLibrary.meos.tpoint_twcentroid(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_expand_space(Pointer gs, double d) {
		return MeosLibrary.meos.geo_expand_space(gs, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geomeas_tpoint(Pointer gs) {
		return MeosLibrary.meos.geomeas_tpoint(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_tgeography(Pointer temp) {
		return MeosLibrary.meos.tgeogpoint_tgeography(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeography_tgeometry(Pointer temp) {
		return MeosLibrary.meos.tgeography_tgeometry(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeography_tgeogpoint(Pointer temp) {
		return MeosLibrary.meos.tgeography_tgeogpoint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometry_tgeography(Pointer temp) {
		return MeosLibrary.meos.tgeometry_tgeography(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometry_tgeompoint(Pointer temp) {
		return MeosLibrary.meos.tgeometry_tgeompoint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_tgeometry(Pointer temp) {
		return MeosLibrary.meos.tgeompoint_tgeometry(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_affine(Pointer temp, Pointer a) {
		return MeosLibrary.meos.tgeo_affine(temp, a);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpoint_AsMVTGeom(Pointer temp, Pointer bounds, int extent, int buffer, boolean clip_geom, Pointer gsarr, Pointer timesarr, Pointer count) {
		return MeosLibrary.meos.tpoint_AsMVTGeom(temp, bounds, extent, buffer, clip_geom, gsarr, timesarr, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tspatial_expand_space(Pointer temp, double d) {
		return MeosLibrary.meos.tspatial_expand_space(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_make_simple(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tpoint_make_simple(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_scale(Pointer temp, Pointer scale, Pointer sorigin) {
		return MeosLibrary.meos.tgeo_scale(temp, scale, sorigin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_tfloat_to_geomeas(Pointer tpoint, Pointer measure, boolean segmentize) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tpoint_tfloat_to_geomeas(tpoint, measure, segmentize, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static int acontains_geo_tgeo(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.acontains_geo_tgeo(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int adisjoint_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.adisjoint_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int adisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.adisjoint_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int adwithin_tgeo_geo(Pointer temp, Pointer gs, double dist) {
		return MeosLibrary.meos.adwithin_tgeo_geo(temp, gs, dist);
	}
	
	@SuppressWarnings("unused")
	public static int adwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist) {
		return MeosLibrary.meos.adwithin_tgeo_tgeo(temp1, temp2, dist);
	}
	
	@SuppressWarnings("unused")
	public static int aintersects_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.aintersects_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int aintersects_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.aintersects_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int atouches_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.atouches_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int econtains_geo_tgeo(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.econtains_geo_tgeo(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int edisjoint_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.edisjoint_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int edisjoint_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.edisjoint_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int edwithin_tgeo_geo(Pointer temp, Pointer gs, double dist) {
		return MeosLibrary.meos.edwithin_tgeo_geo(temp, gs, dist);
	}
	
	@SuppressWarnings("unused")
	public static int edwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist) {
		return MeosLibrary.meos.edwithin_tgeo_tgeo(temp1, temp2, dist);
	}
	
	@SuppressWarnings("unused")
	public static int eintersects_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.eintersects_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int eintersects_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.eintersects_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int etouches_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.etouches_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tcontains_geo_tgeo(Pointer gs, Pointer temp, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tcontains_geo_tgeo(gs, temp, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tgeo_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tdisjoint_tgeo_geo(temp, gs, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tgeo_tgeo (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tdisjoint_tgeo_tgeo(temp1, temp2, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdwithin_tgeo_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tdwithin_tgeo_geo(temp, gs, dist, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdwithin_tgeo_tgeo(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tdwithin_tgeo_tgeo(temp1, temp2, dist, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintersects_tgeo_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tintersects_tgeo_geo(temp, gs, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintersects_tgeo_tgeo (Pointer temp1, Pointer temp2, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tintersects_tgeo_tgeo(temp1, temp2, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttouches_tgeo_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.ttouches_tgeo_geo(temp, gs, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static double nad_stbox_geo(Pointer box, Pointer gs) {
		return MeosLibrary.meos.nad_stbox_geo(box, gs);
	}
	
	@SuppressWarnings("unused")
	public static double nad_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.nad_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.nad_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tgeo_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.nad_tgeo_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.nad_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer nai_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.nai_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer nai_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.nai_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer shortestline_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.shortestline_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer shortestline_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.shortestline_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tgeo_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.distance_tgeo_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tgeo_tgeo(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.distance_tgeo_tgeo(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_tcentroid_finalfn(Pointer state) {
		return MeosLibrary.meos.tpoint_tcentroid_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp) {
		return MeosLibrary.meos.tpoint_tcentroid_transfn(state, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tspatial_extent_transfn(Pointer box, Pointer temp) {
		return MeosLibrary.meos.tspatial_extent_transfn(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_get_space_tile(Pointer point, double xsize, double ysize, double zsize, Pointer sorigin) {
		return MeosLibrary.meos.stbox_get_space_tile(point, xsize, ysize, zsize, sorigin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_get_space_time_tile(Pointer point, OffsetDateTime t, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.stbox_get_space_time_tile(point, t_new, xsize, ysize, zsize, duration, sorigin, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_get_time_tile(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.stbox_get_time_tile(t_new, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_space_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer sorigin, boolean border_inc, Pointer count) {
		return MeosLibrary.meos.stbox_space_tiles(bounds, xsize, ysize, zsize, sorigin, border_inc, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_space_time_tiles(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean border_inc, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.stbox_space_time_tiles(bounds, xsize, ysize, zsize, duration, sorigin, torigin_new, border_inc, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_time_tiles(Pointer bounds, Pointer duration, OffsetDateTime torigin, boolean border_inc, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.stbox_time_tiles(bounds, duration, torigin_new, border_inc, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_space_split(Pointer temp, double xsize, double ysize, double zsize, Pointer sorigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer count) {
		return MeosLibrary.meos.tpoint_space_split(temp, xsize, ysize, zsize, sorigin, bitmatrix, border_inc, space_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_space_time_split(Pointer temp, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, boolean bitmatrix, boolean border_inc, Pointer space_bins, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tgeo_space_time_split(temp, xsize, ysize, zsize, duration, sorigin, torigin_new, bitmatrix, border_inc, space_bins, time_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_time_split(Pointer temp, Pointer duration, OffsetDateTime torigin, boolean border_inc, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tgeo_time_split(temp, duration, torigin_new, border_inc, time_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static int int4_in(String str) {
		return MeosLibrary.meos.int4_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String int4_out(int val) {
		return MeosLibrary.meos.int4_out(val);
	}
	
	@SuppressWarnings("unused")
	public static long int8_in(String str) {
		return MeosLibrary.meos.int8_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String int8_out(long val) {
		return MeosLibrary.meos.int8_out(val);
	}
	
	@SuppressWarnings("unused")
	public static String float8_out(double num, int maxdd) {
		return MeosLibrary.meos.float8_out(num, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static double pg_dsin(double arg1) {
		return MeosLibrary.meos.pg_dsin(arg1);
	}
	
	@SuppressWarnings("unused")
	public static double pg_dcos(double arg1) {
		return MeosLibrary.meos.pg_dcos(arg1);
	}
	
	@SuppressWarnings("unused")
	public static double pg_datan(double arg1) {
		return MeosLibrary.meos.pg_datan(arg1);
	}
	
	@SuppressWarnings("unused")
	public static double pg_datan2(double arg1, double arg2) {
		return MeosLibrary.meos.pg_datan2(arg1, arg2);
	}
	
	@SuppressWarnings("unused")
	public static int pg_date_in(String str) {
		return MeosLibrary.meos.pg_date_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String pg_date_out(int d) {
		return MeosLibrary.meos.pg_date_out(d);
	}
	
	@SuppressWarnings("unused")
	public static int pg_interval_cmp(Pointer interv1, Pointer interv2) {
		return MeosLibrary.meos.pg_interval_cmp(interv1, interv2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_in(String str, int prec) {
		return MeosLibrary.meos.pg_interval_in(str, prec);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_justify_hours(Pointer span) {
		return MeosLibrary.meos.pg_interval_justify_hours(span);
	}
	
	@SuppressWarnings("unused")
	public static String pg_interval_out(Pointer interv) {
		return MeosLibrary.meos.pg_interval_out(interv);
	}
	
	@SuppressWarnings("unused")
	public static long pg_time_in(String str, int typmod) {
		return MeosLibrary.meos.pg_time_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static String pg_time_out(long t) {
		return MeosLibrary.meos.pg_time_out(t);
	}
	
	@SuppressWarnings("unused")
	public static LocalDateTime pg_timestamp_in(String str, int typmod) {
		var result = MeosLibrary.meos.pg_timestamp_in(str, typmod);
		return LocalDateTime.ofEpochSecond(result, 0, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String pg_timestamp_out(LocalDateTime t) {
		var t_new = t.toEpochSecond(ZoneOffset.UTC);
		return MeosLibrary.meos.pg_timestamp_out(t_new);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime pg_timestamptz_in(String str, int prec) {
		var result = MeosLibrary.meos.pg_timestamptz_in(str, prec);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String pg_timestamptz_out(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.pg_timestamptz_out(t_new);
	}
	
	@SuppressWarnings("unused")
	public static int hash_bytes_uint32(int k) {
		return MeosLibrary.meos.hash_bytes_uint32(k);
	}
	
	@SuppressWarnings("unused")
	public static int pg_hashint8(long val) {
		return MeosLibrary.meos.pg_hashint8(val);
	}
	
	@SuppressWarnings("unused")
	public static int pg_hashfloat8(double key) {
		return MeosLibrary.meos.pg_hashfloat8(key);
	}
	
	@SuppressWarnings("unused")
	public static long hash_bytes_uint32_extended(int k, long seed) {
		return MeosLibrary.meos.hash_bytes_uint32_extended(k, seed);
	}
	
	@SuppressWarnings("unused")
	public static long pg_hashint8extended(long val, long seed) {
		return MeosLibrary.meos.pg_hashint8extended(val, seed);
	}
	
	@SuppressWarnings("unused")
	public static long pg_hashfloat8extended(double key, long seed) {
		return MeosLibrary.meos.pg_hashfloat8extended(key, seed);
	}
	
	@SuppressWarnings("unused")
	public static int pg_hashtext(Pointer key) {
		return MeosLibrary.meos.pg_hashtext(key);
	}
	
	@SuppressWarnings("unused")
	public static long pg_hashtextextended(Pointer key, long seed) {
		return MeosLibrary.meos.pg_hashtextextended(key, seed);
	}
	
	@SuppressWarnings("unused")
	public static Pointer gsl_get_generation_rng() {
		return MeosLibrary.meos.gsl_get_generation_rng();
	}
	
	@SuppressWarnings("unused")
	public static Pointer gsl_get_aggregation_rng() {
		return MeosLibrary.meos.gsl_get_aggregation_rng();
	}
	
	@SuppressWarnings("unused")
	public static Pointer proj_get_context() {
		return MeosLibrary.meos.proj_get_context();
	}
	
	@SuppressWarnings("unused")
	public static long datum_floor(long d) {
		return MeosLibrary.meos.datum_floor(d);
	}
	
	@SuppressWarnings("unused")
	public static long datum_ceil(long d) {
		return MeosLibrary.meos.datum_ceil(d);
	}
	
	@SuppressWarnings("unused")
	public static long datum_degrees(long d, long normalize) {
		return MeosLibrary.meos.datum_degrees(d, normalize);
	}
	
	@SuppressWarnings("unused")
	public static long datum_radians(long d) {
		return MeosLibrary.meos.datum_radians(d);
	}
	
	@SuppressWarnings("unused")
	public static int datum_hash(long d, meosType basetype) {
		return MeosLibrary.meos.datum_hash(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static long datum_hash_extended(long d, meosType basetype, long seed) {
		return MeosLibrary.meos.datum_hash_extended(d, basetype, seed);
	}
	
	@SuppressWarnings("unused")
	public static long datum_float_round(long value, long size) {
		return MeosLibrary.meos.datum_float_round(value, size);
	}
	
	@SuppressWarnings("unused")
	public static long datum_geo_round(long value, long size) {
		return MeosLibrary.meos.datum_geo_round(value, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer point_round(Pointer gs, int maxdd) {
		return MeosLibrary.meos.point_round(gs, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static void floatspan_round_set(Pointer s, int maxdd) {
	}
	
	@SuppressWarnings("unused")
	public static Pointer SET_BBOX_PTR(Pointer s) {
		return MeosLibrary.meos.SET_BBOX_PTR(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer SET_OFFSETS_PTR(Pointer s) {
		return MeosLibrary.meos.SET_OFFSETS_PTR(s);
	}
	
	@SuppressWarnings("unused")
	public static long SET_VAL_N(Pointer s, int index) {
		return MeosLibrary.meos.SET_VAL_N(s, index);
	}
	
	@SuppressWarnings("unused")
	public static Pointer SPANSET_SP_N(Pointer ss, int index) {
		return MeosLibrary.meos.SPANSET_SP_N(ss, index);
	}
	
	@SuppressWarnings("unused")
	public static Pointer TSEQUENCE_OFFSETS_PTR(Pointer seq) {
		return MeosLibrary.meos.TSEQUENCE_OFFSETS_PTR(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer TSEQUENCE_INST_N(Pointer seq, int index) {
		return MeosLibrary.meos.TSEQUENCE_INST_N(seq, index);
	}
	
	@SuppressWarnings("unused")
	public static Pointer TSEQUENCESET_OFFSETS_PTR(Pointer ss) {
		return MeosLibrary.meos.TSEQUENCESET_OFFSETS_PTR(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer TSEQUENCESET_SEQ_N(Pointer ss, int index) {
		return MeosLibrary.meos.TSEQUENCESET_SEQ_N(ss, index);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_in(String str, meosType basetype) {
		return MeosLibrary.meos.set_in(str, basetype);
	}
	
	@SuppressWarnings("unused")
	public static String set_out(Pointer s, int maxdd) {
		return MeosLibrary.meos.set_out(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_in(String str, meosType spantype) {
		return MeosLibrary.meos.span_in(str, spantype);
	}
	
	@SuppressWarnings("unused")
	public static String span_out(Pointer s, int maxdd) {
		return MeosLibrary.meos.span_out(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_in(String str, meosType spantype) {
		return MeosLibrary.meos.spanset_in(str, spantype);
	}
	
	@SuppressWarnings("unused")
	public static String spanset_out(Pointer ss, int maxdd) {
		return MeosLibrary.meos.spanset_out(ss, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_make(Pointer values, int count, meosType basetype, boolean order) {
		return MeosLibrary.meos.set_make(values, count, basetype, order);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_make_exp(Pointer values, int count, int maxcount, meosType basetype, boolean order) {
		return MeosLibrary.meos.set_make_exp(values, count, maxcount, basetype, order);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_make_free(Pointer values, int count, meosType basetype, boolean order) {
		return MeosLibrary.meos.set_make_free(values, count, basetype, order);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_make(long lower, long upper, boolean lower_inc, boolean upper_inc, meosType basetype) {
		return MeosLibrary.meos.span_make(lower, upper, lower_inc, upper_inc, basetype);
	}
	
	@SuppressWarnings("unused")
	public static void span_set(long lower, long upper, boolean lower_inc, boolean upper_inc, meosType basetype, meosType spantype, Pointer s) {
		MeosLibrary.meos.span_set(lower, upper, lower_inc, upper_inc, basetype, spantype, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_make_exp(Pointer spans, int count, int maxcount, boolean normalize, boolean order) {
		return MeosLibrary.meos.spanset_make_exp(spans, count, maxcount, normalize, order);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_make_free(Pointer spans, int count, boolean normalize, boolean order) {
		return MeosLibrary.meos.spanset_make_free(spans, count, normalize, order);
	}
	
	@SuppressWarnings("unused")
	public static void value_set_span(long value, meosType basetype, Pointer s) {
		MeosLibrary.meos.value_set_span(value, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer value_set(long d, meosType basetype) {
		return MeosLibrary.meos.value_set(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer value_span(long d, meosType basetype) {
		return MeosLibrary.meos.value_span(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer value_spanset(long d, meosType basetype) {
		return MeosLibrary.meos.value_spanset(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static long numspan_width(Pointer s) {
		return MeosLibrary.meos.numspan_width(s);
	}
	
	@SuppressWarnings("unused")
	public static long numspanset_width(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.numspanset_width(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static long set_end_value(Pointer s) {
		return MeosLibrary.meos.set_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static int set_mem_size(Pointer s) {
		return MeosLibrary.meos.set_mem_size(s);
	}
	
	@SuppressWarnings("unused")
	public static void set_set_subspan(Pointer s, int minidx, int maxidx) {
	}
	
	@SuppressWarnings("unused")
	public static void set_set_span(Pointer s) {
	}
	
	@SuppressWarnings("unused")
	public static long set_start_value(Pointer s) {
		return MeosLibrary.meos.set_start_value(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_value_n(Pointer s, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.set_value_n(s, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_vals(Pointer s) {
		return MeosLibrary.meos.set_vals(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_values(Pointer s) {
		return MeosLibrary.meos.set_values(s);
	}
	
	@SuppressWarnings("unused")
	public static long spanset_lower(Pointer ss) {
		return MeosLibrary.meos.spanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static int spanset_mem_size(Pointer ss) {
		return MeosLibrary.meos.spanset_mem_size(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_sps(Pointer ss) {
		return MeosLibrary.meos.spanset_sps(ss);
	}
	
	@SuppressWarnings("unused")
	public static long spanset_upper(Pointer ss) {
		return MeosLibrary.meos.spanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static void datespan_set_tstzspan(Pointer s1, Pointer s2) {
		MeosLibrary.meos.datespan_set_tstzspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static void floatspan_set_intspan(Pointer s1, Pointer s2) {
		MeosLibrary.meos.floatspan_set_intspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static void intspan_set_floatspan(Pointer s1, Pointer s2) {
		MeosLibrary.meos.intspan_set_floatspan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.numset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.numspan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.numspanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_compact(Pointer s) {
		return MeosLibrary.meos.set_compact(s);
	}
	
	@SuppressWarnings("unused")
	public static void span_expand(Pointer s1, Pointer s2) {
		MeosLibrary.meos.span_expand(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_compact(Pointer ss) {
		return MeosLibrary.meos.spanset_compact(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textcat_textset_text_int(Pointer s, Pointer txt, boolean invert) {
		return MeosLibrary.meos.textcat_textset_text_int(s, txt, invert);
	}
	
	@SuppressWarnings("unused")
	public static void tstzspan_set_datespan(Pointer s1, Pointer s2) {
		MeosLibrary.meos.tstzspan_set_datespan(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_value(Pointer s, long value) {
		return MeosLibrary.meos.adjacent_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.adjacent_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.adjacent_value_spanset(value, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_value_set(long value, Pointer s) {
		return MeosLibrary.meos.contained_value_set(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_value_span(long value, Pointer s) {
		return MeosLibrary.meos.contained_value_span(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.contained_value_spanset(value, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_value(Pointer s, long value) {
		return MeosLibrary.meos.contains_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_value(Pointer s, long value) {
		return MeosLibrary.meos.contains_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.contains_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean ovadj_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.ovadj_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_value(Pointer s, long value) {
		return MeosLibrary.meos.left_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_value(Pointer s, long value) {
		return MeosLibrary.meos.left_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.left_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_value_set(long value, Pointer s) {
		return MeosLibrary.meos.left_value_set(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_value_span(long value, Pointer s) {
		return MeosLibrary.meos.left_value_span(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.left_value_spanset(value, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean lfnadj_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.lfnadj_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_value(Pointer s, long value) {
		return MeosLibrary.meos.overleft_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_value(Pointer s, long value) {
		return MeosLibrary.meos.overleft_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.overleft_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_value_set(long value, Pointer s) {
		return MeosLibrary.meos.overleft_value_set(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_value_span(long value, Pointer s) {
		return MeosLibrary.meos.overleft_value_span(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.overleft_value_spanset(value, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_value(Pointer s, long value) {
		return MeosLibrary.meos.overright_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_value(Pointer s, long value) {
		return MeosLibrary.meos.overright_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.overright_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_value_set(long value, Pointer s) {
		return MeosLibrary.meos.overright_value_set(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_value_span(long value, Pointer s) {
		return MeosLibrary.meos.overright_value_span(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.overright_value_spanset(value, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_value_set(long value, Pointer s) {
		return MeosLibrary.meos.right_value_set(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_value(Pointer s, long value) {
		return MeosLibrary.meos.right_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_value_span(long value, Pointer s) {
		return MeosLibrary.meos.right_value_span(value, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.right_value_spanset(value, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_value(Pointer s, long value) {
		return MeosLibrary.meos.right_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.right_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static void bbox_union_span_span(Pointer s1, Pointer s2) {
	}
	
	@SuppressWarnings("unused")
	public static Pointer inter_span_span(Pointer s1, Pointer s2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.inter_span_span(s1, s2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_value(Pointer s, long value) {
		return MeosLibrary.meos.intersection_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_value(Pointer s, long value) {
		return MeosLibrary.meos.intersection_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.intersection_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_value_set(long value, Pointer s) {
		return MeosLibrary.meos.intersection_value_set(value, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_value_span(long value, Pointer s) {
		return MeosLibrary.meos.intersection_value_span(value, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.intersection_value_spanset(value, ss);
	}
	/** 
	@SuppressWarnings("unused")
	public static int mi_span_span(Pointer s1, Pointer s2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.mi_span_span(s1, s2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	*/
	@SuppressWarnings("unused")
	public static Pointer minus_set_value(Pointer s, long value) {
		return MeosLibrary.meos.minus_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_value(Pointer s, long value) {
		return MeosLibrary.meos.minus_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.minus_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_value_set(long value, Pointer s) {
		return MeosLibrary.meos.minus_value_set(value, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_value_span(long value, Pointer s) {
		return MeosLibrary.meos.minus_value_span(value, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.minus_value_spanset(value, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer super_union_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.super_union_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_value(Pointer s, long value) {
		return MeosLibrary.meos.union_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_value(Pointer s, long value) {
		return MeosLibrary.meos.union_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.union_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_value_set(long value, Pointer s) {
		return MeosLibrary.meos.union_value_set(value, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_value_span(long value, Pointer s) {
		return MeosLibrary.meos.union_value_span(value, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_value_spanset(long value, Pointer ss) {
		return MeosLibrary.meos.union_value_spanset(value, ss);
	}
	
	@SuppressWarnings("unused")
	public static long distance_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_set_value(Pointer s, long value) {
		return MeosLibrary.meos.distance_set_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static long distance_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_span_value(Pointer s, long value) {
		return MeosLibrary.meos.distance_span_value(s, value);
	}
	
	@SuppressWarnings("unused")
	public static long distance_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.distance_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static long distance_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.distance_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static long distance_spanset_value(Pointer ss, long value) {
		return MeosLibrary.meos.distance_spanset_value(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static long distance_value_value(long l, long r, meosType basetype) {
		return MeosLibrary.meos.distance_value_value(l, r, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanbase_extent_transfn(Pointer state, long value, meosType basetype) {
		return MeosLibrary.meos.spanbase_extent_transfn(state, value, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer value_union_transfn(Pointer state, long value, meosType basetype) {
		return MeosLibrary.meos.value_union_transfn(state, value, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer number_tstzspan_to_tbox(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.number_tstzspan_to_tbox(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer number_timestamptz_to_tbox(long d, meosType basetype, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.number_timestamptz_to_tbox(d, basetype, t_new);
	}
	
	@SuppressWarnings("unused")
	public static void stbox_set(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer s, Pointer box) {
		MeosLibrary.meos.stbox_set(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, s, box);
	}
	
	@SuppressWarnings("unused")
	public static void tbox_set(Pointer s, Pointer p, Pointer box) {
		MeosLibrary.meos.tbox_set(s, p, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer box3d_stbox(Pointer box) {
		return MeosLibrary.meos.box3d_stbox(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer gbox_stbox(Pointer box) {
		return MeosLibrary.meos.gbox_stbox(box);
	}
	
	@SuppressWarnings("unused")
	public static void float_set_tbox(double d, Pointer box) {
		MeosLibrary.meos.float_set_tbox(d, box);
	}
	
	@SuppressWarnings("unused")
	public static void gbox_set_stbox(Pointer box, int srid) {
	}
	
	@SuppressWarnings("unused")
	public static boolean geo_set_stbox(Pointer gs, Pointer box) {
		return MeosLibrary.meos.geo_set_stbox(gs, box);
	}
	
	@SuppressWarnings("unused")
	public static void geoarr_set_stbox(Pointer values, int count, Pointer box) {
		MeosLibrary.meos.geoarr_set_stbox(values, count, box);
	}
	
	@SuppressWarnings("unused")
	public static void int_set_tbox(int i, Pointer box) {
		MeosLibrary.meos.int_set_tbox(i, box);
	}
	
	@SuppressWarnings("unused")
	public static void number_set_tbox(long d, meosType basetype, Pointer box) {
		MeosLibrary.meos.number_set_tbox(d, basetype, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer number_tbox(long value, meosType basetype) {
		return MeosLibrary.meos.number_tbox(value, basetype);
	}
	
	@SuppressWarnings("unused")
	public static void numset_set_tbox(Pointer s, Pointer box) {
		MeosLibrary.meos.numset_set_tbox(s, box);
	}
	
	@SuppressWarnings("unused")
	public static void numspan_set_tbox(Pointer span, Pointer box) {
		MeosLibrary.meos.numspan_set_tbox(span, box);
	}
	
	@SuppressWarnings("unused")
	public static void numspanset_set_tbox(Pointer ss, Pointer box) {
		MeosLibrary.meos.numspanset_set_tbox(ss, box);
	}
	
	@SuppressWarnings("unused")
	public static boolean spatial_set_stbox(long d, meosType basetype, Pointer box) {
		return MeosLibrary.meos.spatial_set_stbox(d, basetype, box);
	}
	
	@SuppressWarnings("unused")
	public static void spatialset_set_stbox(Pointer set, Pointer box) {
		MeosLibrary.meos.spatialset_set_stbox(set, box);
	}
	
	@SuppressWarnings("unused")
	public static void stbox_set_box3d(Pointer box, Pointer box3d) {
		MeosLibrary.meos.stbox_set_box3d(box, box3d);
	}
	
	@SuppressWarnings("unused")
	public static void stbox_set_gbox(Pointer box, Pointer gbox) {
		MeosLibrary.meos.stbox_set_gbox(box, gbox);
	}
	
	@SuppressWarnings("unused")
	public static void timestamptz_set_stbox(OffsetDateTime t, Pointer box) {
		var t_new = t.toEpochSecond();
		MeosLibrary.meos.timestamptz_set_stbox(t_new, box);
	}
	
	@SuppressWarnings("unused")
	public static void timestamptz_set_tbox(OffsetDateTime t, Pointer box) {
		var t_new = t.toEpochSecond();
		MeosLibrary.meos.timestamptz_set_tbox(t_new, box);
	}
	
	@SuppressWarnings("unused")
	public static void tstzset_set_stbox(Pointer s, Pointer box) {
		MeosLibrary.meos.tstzset_set_stbox(s, box);
	}
	
	@SuppressWarnings("unused")
	public static void tstzset_set_tbox(Pointer s, Pointer box) {
		MeosLibrary.meos.tstzset_set_tbox(s, box);
	}
	
	@SuppressWarnings("unused")
	public static void tstzspan_set_stbox(Pointer s, Pointer box) {
		MeosLibrary.meos.tstzspan_set_stbox(s, box);
	}
	
	@SuppressWarnings("unused")
	public static void tstzspan_set_tbox(Pointer s, Pointer box) {
		MeosLibrary.meos.tstzspan_set_tbox(s, box);
	}
	
	@SuppressWarnings("unused")
	public static void tstzspanset_set_stbox(Pointer ss, Pointer box) {
		MeosLibrary.meos.tstzspanset_set_stbox(ss, box);
	}
	
	@SuppressWarnings("unused")
	public static void tstzspanset_set_tbox(Pointer ss, Pointer box) {
		MeosLibrary.meos.tstzspanset_set_tbox(ss, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_shift_scale_value(Pointer box, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.tbox_shift_scale_value(box, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static void stbox_expand(Pointer box1, Pointer box2) {
		MeosLibrary.meos.stbox_expand(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static void tbox_expand(Pointer box1, Pointer box2) {
		MeosLibrary.meos.tbox_expand(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer inter_stbox_stbox(Pointer box1, Pointer box2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.inter_stbox_stbox(box1, box2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer inter_tbox_tbox(Pointer box1, Pointer box2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.inter_tbox_tbox(box1, box2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static String tboolinst_as_mfjson(Pointer inst, boolean with_bbox) {
		return MeosLibrary.meos.tboolinst_as_mfjson(inst, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolinst_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.tboolinst_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolinst_in(String str) {
		return MeosLibrary.meos.tboolinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tboolseq_as_mfjson(Pointer seq, boolean with_bbox) {
		return MeosLibrary.meos.tboolseq_as_mfjson(seq, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.tboolseq_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseq_in(String str, int interp) {
		return MeosLibrary.meos.tboolseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static String tboolseqset_as_mfjson(Pointer ss, boolean with_bbox) {
		return MeosLibrary.meos.tboolseqset_as_mfjson(ss, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseqset_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.tboolseqset_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseqset_in(String str) {
		return MeosLibrary.meos.tboolseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_in(String str, meosType temptype) {
		return MeosLibrary.meos.temporal_in(str, temptype);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_out(Pointer temp, int maxdd) {
		return MeosLibrary.meos.temporal_out(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temparr_out(Pointer temparr, int count, int maxdd) {
		return MeosLibrary.meos.temparr_out(temparr, count, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tfloatinst_as_mfjson(Pointer inst, boolean with_bbox, int precision) {
		return MeosLibrary.meos.tfloatinst_as_mfjson(inst, with_bbox, precision);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatinst_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.tfloatinst_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatinst_in(String str) {
		return MeosLibrary.meos.tfloatinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tfloatseq_as_mfjson(Pointer seq, boolean with_bbox, int precision) {
		return MeosLibrary.meos.tfloatseq_as_mfjson(seq, with_bbox, precision);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_mfjson(Pointer mfjson, int interp) {
		return MeosLibrary.meos.tfloatseq_from_mfjson(mfjson, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseq_in(String str, int interp) {
		return MeosLibrary.meos.tfloatseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static String tfloatseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision) {
		return MeosLibrary.meos.tfloatseqset_as_mfjson(ss, with_bbox, precision);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_from_mfjson(Pointer mfjson, int interp) {
		return MeosLibrary.meos.tfloatseqset_from_mfjson(mfjson, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_in(String str) {
		return MeosLibrary.meos.tfloatseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tgeoinst_as_mfjson(Pointer inst, boolean with_bbox, int precision, String srs) {
		return MeosLibrary.meos.tgeoinst_as_mfjson(inst, with_bbox, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointinst_from_mfjson(Pointer mfjson, int srid) {
		return MeosLibrary.meos.tgeogpointinst_from_mfjson(mfjson, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointinst_in(String str) {
		return MeosLibrary.meos.tgeogpointinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointseq_from_mfjson(Pointer mfjson, int srid, int interp) {
		return MeosLibrary.meos.tgeogpointseq_from_mfjson(mfjson, srid, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointseq_in(String str, int interp) {
		return MeosLibrary.meos.tgeogpointseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointseqset_from_mfjson(Pointer mfjson, int srid, int interp) {
		return MeosLibrary.meos.tgeogpointseqset_from_mfjson(mfjson, srid, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointseqset_in(String str) {
		return MeosLibrary.meos.tgeogpointseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointinst_from_mfjson(Pointer mfjson, int srid) {
		return MeosLibrary.meos.tgeompointinst_from_mfjson(mfjson, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointinst_in(String str) {
		return MeosLibrary.meos.tgeompointinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointseq_from_mfjson(Pointer mfjson, int srid, int interp) {
		return MeosLibrary.meos.tgeompointseq_from_mfjson(mfjson, srid, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointseq_in(String str, int interp) {
		return MeosLibrary.meos.tgeompointseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointseqset_from_mfjson(Pointer mfjson, int srid, int interp) {
		return MeosLibrary.meos.tgeompointseqset_from_mfjson(mfjson, srid, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointseqset_in(String str) {
		return MeosLibrary.meos.tgeompointseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeographyinst_from_mfjson(Pointer mfjson, int srid) {
		return MeosLibrary.meos.tgeographyinst_from_mfjson(mfjson, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeographyinst_in(String str) {
		return MeosLibrary.meos.tgeographyinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeographyseq_from_mfjson(Pointer mfjson, int srid, int interp) {
		return MeosLibrary.meos.tgeographyseq_from_mfjson(mfjson, srid, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeographyseq_in(String str, int interp) {
		return MeosLibrary.meos.tgeographyseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeographyseqset_from_mfjson(Pointer mfjson, int srid, int interp) {
		return MeosLibrary.meos.tgeographyseqset_from_mfjson(mfjson, srid, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeographyseqset_in(String str) {
		return MeosLibrary.meos.tgeographyseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometryinst_from_mfjson(Pointer mfjson, int srid) {
		return MeosLibrary.meos.tgeometryinst_from_mfjson(mfjson, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometryinst_in(String str) {
		return MeosLibrary.meos.tgeometryinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometryseq_from_mfjson(Pointer mfjson, int srid, int interp) {
		return MeosLibrary.meos.tgeometryseq_from_mfjson(mfjson, srid, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometryseq_in(String str, int interp) {
		return MeosLibrary.meos.tgeometryseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometryseqset_from_mfjson(Pointer mfjson, int srid, int interp) {
		return MeosLibrary.meos.tgeometryseqset_from_mfjson(mfjson, srid, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeometryseqset_in(String str) {
		return MeosLibrary.meos.tgeometryseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tgeoseq_as_mfjson(Pointer seq, boolean with_bbox, int precision, String srs) {
		return MeosLibrary.meos.tgeoseq_as_mfjson(seq, with_bbox, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static String tgeoseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision, String srs) {
		return MeosLibrary.meos.tgeoseqset_as_mfjson(ss, with_bbox, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_from_mfjson(Pointer mfjson, boolean isgeo, int srid, meosType temptype) {
		return MeosLibrary.meos.tinstant_from_mfjson(mfjson, isgeo, srid, temptype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_in(String str, meosType temptype) {
		return MeosLibrary.meos.tinstant_in(str, temptype);
	}
	
	@SuppressWarnings("unused")
	public static String tinstant_out(Pointer inst, int maxdd) {
		return MeosLibrary.meos.tinstant_out(inst, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tintinst_as_mfjson(Pointer inst, boolean with_bbox) {
		return MeosLibrary.meos.tintinst_as_mfjson(inst, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintinst_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.tintinst_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintinst_in(String str) {
		return MeosLibrary.meos.tintinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tintseq_as_mfjson(Pointer seq, boolean with_bbox) {
		return MeosLibrary.meos.tintseq_as_mfjson(seq, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseq_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.tintseq_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseq_in(String str, int interp) {
		return MeosLibrary.meos.tintseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static String tintseqset_as_mfjson(Pointer ss, boolean with_bbox) {
		return MeosLibrary.meos.tintseqset_as_mfjson(ss, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseqset_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.tintseqset_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseqset_in(String str) {
		return MeosLibrary.meos.tintseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tpointinst_as_mfjson(Pointer inst, boolean with_bbox, int precision, String srs) {
		return MeosLibrary.meos.tpointinst_as_mfjson(inst, with_bbox, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static String tpointseq_as_mfjson(Pointer seq, boolean with_bbox, int precision, String srs) {
		return MeosLibrary.meos.tpointseq_as_mfjson(seq, with_bbox, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static String tpointseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision, String srs) {
		return MeosLibrary.meos.tpointseqset_as_mfjson(ss, with_bbox, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_from_mfjson(Pointer mfjson, boolean isgeo, int srid, meosType temptype, int interp) {
		return MeosLibrary.meos.tsequence_from_mfjson(mfjson, isgeo, srid, temptype, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_in(String str, meosType temptype, int interp) {
		return MeosLibrary.meos.tsequence_in(str, temptype, interp);
	}
	
	@SuppressWarnings("unused")
	public static String tsequence_out(Pointer seq, int maxdd) {
		return MeosLibrary.meos.tsequence_out(seq, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_from_mfjson(Pointer mfjson, boolean isgeo, int srid, meosType temptype, int interp) {
		return MeosLibrary.meos.tsequenceset_from_mfjson(mfjson, isgeo, srid, temptype, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_in(String str, meosType temptype, int interp) {
		return MeosLibrary.meos.tsequenceset_in(str, temptype, interp);
	}
	
	@SuppressWarnings("unused")
	public static String tsequenceset_out(Pointer ss, int maxdd) {
		return MeosLibrary.meos.tsequenceset_out(ss, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String ttextinst_as_mfjson(Pointer inst, boolean with_bbox) {
		return MeosLibrary.meos.ttextinst_as_mfjson(inst, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextinst_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.ttextinst_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextinst_in(String str) {
		return MeosLibrary.meos.ttextinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String ttextseq_as_mfjson(Pointer seq, boolean with_bbox) {
		return MeosLibrary.meos.ttextseq_as_mfjson(seq, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.ttextseq_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseq_in(String str, int interp) {
		return MeosLibrary.meos.ttextseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static String ttextseqset_as_mfjson(Pointer ss, boolean with_bbox) {
		return MeosLibrary.meos.ttextseqset_as_mfjson(ss, with_bbox);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseqset_from_mfjson(Pointer mfjson) {
		return MeosLibrary.meos.ttextseqset_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseqset_in(String str) {
		return MeosLibrary.meos.ttextseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_from_mfjson(String mfjson, meosType temptype) {
		return MeosLibrary.meos.temporal_from_mfjson(mfjson, temptype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_from_base_temp(long value, meosType temptype, Pointer temp) {
		return MeosLibrary.meos.temporal_from_base_temp(value, temptype, temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_copy(Pointer inst) {
		return MeosLibrary.meos.tinstant_copy(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_make(long value, meosType temptype, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tinstant_make(value, temptype, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_make_free(long value, meosType temptype, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tinstant_make_free(value, temptype, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_make_coords(Pointer xcoords, Pointer ycoords, Pointer zcoords, Pointer times, int count, int srid, boolean geodetic, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		return MeosLibrary.meos.tpointseq_make_coords(xcoords, ycoords, zcoords, times, count, srid, geodetic, lower_inc, upper_inc, interp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_copy(Pointer seq) {
		return MeosLibrary.meos.tsequence_copy(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_from_base_tstzset(long value, meosType temptype, Pointer ss) {
		return MeosLibrary.meos.tsequence_from_base_tstzset(value, temptype, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_from_base_tstzspan(long value, meosType temptype, Pointer s, int interp) {
		return MeosLibrary.meos.tsequence_from_base_tstzspan(value, temptype, s, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_make_exp(Pointer instants, int count, int maxcount, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		return MeosLibrary.meos.tsequence_make_exp(instants, count, maxcount, lower_inc, upper_inc, interp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_make_free(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		return MeosLibrary.meos.tsequence_make_free(instants, count, lower_inc, upper_inc, interp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_copy(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_copy(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tseqsetarr_to_tseqset(Pointer seqsets, int count, int totalseqs) {
		return MeosLibrary.meos.tseqsetarr_to_tseqset(seqsets, count, totalseqs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_from_base_tstzspanset(long value, meosType temptype, Pointer ss, int interp) {
		return MeosLibrary.meos.tsequenceset_from_base_tstzspanset(value, temptype, ss, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_exp(Pointer sequences, int count, int maxcount, boolean normalize) {
		return MeosLibrary.meos.tsequenceset_make_exp(sequences, count, maxcount, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_free(Pointer sequences, int count, boolean normalize) {
		return MeosLibrary.meos.tsequenceset_make_free(sequences, count, normalize);
	}
	
	@SuppressWarnings("unused")
	public static void temporal_set_tstzspan(Pointer temp, Pointer s) {
		MeosLibrary.meos.temporal_set_tstzspan(temp, s);
	}
	
	@SuppressWarnings("unused")
	public static void tinstant_set_tstzspan(Pointer inst, Pointer s) {
		MeosLibrary.meos.tinstant_set_tstzspan(inst, s);
	}
	
	@SuppressWarnings("unused")
	public static void tnumber_set_tbox(Pointer temp, Pointer box) {
		MeosLibrary.meos.tnumber_set_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static void tnumberinst_set_tbox(Pointer inst, Pointer box) {
		MeosLibrary.meos.tnumberinst_set_tbox(inst, box);
	}
	
	@SuppressWarnings("unused")
	public static void tnumberseq_set_tbox(Pointer seq, Pointer box) {
		MeosLibrary.meos.tnumberseq_set_tbox(seq, box);
	}
	
	@SuppressWarnings("unused")
	public static void tnumberseqset_set_tbox(Pointer ss, Pointer box) {
		MeosLibrary.meos.tnumberseqset_set_tbox(ss, box);
	}
	
	@SuppressWarnings("unused")
	public static void tsequence_set_tstzspan(Pointer seq, Pointer s) {
		MeosLibrary.meos.tsequence_set_tstzspan(seq, s);
	}
	
	@SuppressWarnings("unused")
	public static void tsequenceset_set_tstzspan(Pointer ss, Pointer s) {
		MeosLibrary.meos.tsequenceset_set_tstzspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_end_inst(Pointer temp) {
		return MeosLibrary.meos.temporal_end_inst(temp);
	}
	
	@SuppressWarnings("unused")
	public static long temporal_end_value(Pointer temp) {
		return MeosLibrary.meos.temporal_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_inst_n(Pointer temp, int n) {
		return MeosLibrary.meos.temporal_inst_n(temp, n);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_instants_p(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_instants_p(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static long temporal_max_value(Pointer temp) {
		return MeosLibrary.meos.temporal_max_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static long temporal_mem_size(Pointer temp) {
		return MeosLibrary.meos.temporal_mem_size(temp);
	}
	
	@SuppressWarnings("unused")
	public static long temporal_min_value(Pointer temp) {
		return MeosLibrary.meos.temporal_min_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_sequences_p(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_sequences_p(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static void temporal_set_bbox(Pointer temp, Pointer box) {
		MeosLibrary.meos.temporal_set_bbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_start_inst(Pointer temp) {
		return MeosLibrary.meos.temporal_start_inst(temp);
	}
	
	@SuppressWarnings("unused")
	public static long temporal_start_value(Pointer temp) {
		return MeosLibrary.meos.temporal_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_values_p(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_values_p(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_value_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.temporal_value_n(temp, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static int tinstant_hash(Pointer inst) {
		return MeosLibrary.meos.tinstant_hash(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_insts(Pointer inst, Pointer count) {
		return MeosLibrary.meos.tinstant_insts(inst, count);
	}
	
	@SuppressWarnings("unused")
	public static void tinstant_set_bbox(Pointer inst, Pointer box) {
		MeosLibrary.meos.tinstant_set_bbox(inst, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_time(Pointer inst) {
		return MeosLibrary.meos.tinstant_time(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_timestamps(Pointer inst, Pointer count) {
		return MeosLibrary.meos.tinstant_timestamps(inst, count);
	}
	
	@SuppressWarnings("unused")
	public static long tinstant_value_p(Pointer inst) {
		return MeosLibrary.meos.tinstant_value_p(inst);
	}
	
	@SuppressWarnings("unused")
	public static long tinstant_value(Pointer inst) {
		return MeosLibrary.meos.tinstant_value(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_value_at_timestamptz(Pointer inst, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tinstant_value_at_timestamptz(inst, t_new, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_values_p(Pointer inst, Pointer count) {
		return MeosLibrary.meos.tinstant_values_p(inst, count);
	}
	
	@SuppressWarnings("unused")
	public static void tnumber_set_span(Pointer temp, Pointer span) {
		MeosLibrary.meos.tnumber_set_span(temp, span);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberinst_valuespans(Pointer inst) {
		return MeosLibrary.meos.tnumberinst_valuespans(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseq_valuespans(Pointer seq) {
		return MeosLibrary.meos.tnumberseq_valuespans(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_valuespans(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_valuespans(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_duration(Pointer seq) {
		return MeosLibrary.meos.tsequence_duration(seq);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tsequence_end_timestamptz(Pointer seq) {
		var result = MeosLibrary.meos.tsequence_end_timestamptz(seq);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static int tsequence_hash(Pointer seq) {
		return MeosLibrary.meos.tsequence_hash(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_instants_p(Pointer seq) {
		return MeosLibrary.meos.tsequence_instants_p(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_max_inst(Pointer seq) {
		return MeosLibrary.meos.tsequence_max_inst(seq);
	}
	
	@SuppressWarnings("unused")
	public static long tsequence_max_val(Pointer seq) {
		return MeosLibrary.meos.tsequence_max_val(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_min_inst(Pointer seq) {
		return MeosLibrary.meos.tsequence_min_inst(seq);
	}
	
	@SuppressWarnings("unused")
	public static long tsequence_min_val(Pointer seq) {
		return MeosLibrary.meos.tsequence_min_val(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_segments(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tsequence_segments(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_seqs(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tsequence_seqs(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tsequence_start_timestamptz(Pointer seq) {
		var result = MeosLibrary.meos.tsequence_start_timestamptz(seq);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_time(Pointer seq) {
		return MeosLibrary.meos.tsequence_time(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_timestamps(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tsequence_timestamps(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_value_at_timestamptz(Pointer seq, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tsequence_value_at_timestamptz(seq, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_values_p(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tsequence_values_p(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_duration(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.tsequenceset_duration(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tsequenceset_end_timestamptz(Pointer ss) {
		var result = MeosLibrary.meos.tsequenceset_end_timestamptz(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static int tsequenceset_hash(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_hash(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_inst_n(Pointer ss, int n) {
		return MeosLibrary.meos.tsequenceset_inst_n(ss, n);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_insts(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_insts(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_max_inst(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_max_inst(ss);
	}
	
	@SuppressWarnings("unused")
	public static long tsequenceset_max_val(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_max_val(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_min_inst(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_min_inst(ss);
	}
	
	@SuppressWarnings("unused")
	public static long tsequenceset_min_val(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_min_val(ss);
	}
	
	@SuppressWarnings("unused")
	public static int tsequenceset_num_instants(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_num_instants(ss);
	}
	
	@SuppressWarnings("unused")
	public static int tsequenceset_num_timestamps(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_num_timestamps(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_segments(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tsequenceset_segments(ss, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_sequences_p(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_sequences_p(ss);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tsequenceset_start_timestamptz(Pointer ss) {
		var result = MeosLibrary.meos.tsequenceset_start_timestamptz(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_time(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_time(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_timestamptz_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tsequenceset_timestamptz_n(ss, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_timestamps(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tsequenceset_timestamps(ss, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_value_at_timestamptz(Pointer ss, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tsequenceset_value_at_timestamptz(ss, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_value_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tsequenceset_value_n(ss, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_values_p(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tsequenceset_values_p(ss, count);
	}
	
	@SuppressWarnings("unused")
	public static void temporal_restart(Pointer temp, int count) {
		MeosLibrary.meos.temporal_restart(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tsequence(Pointer temp, int interp) {
		return MeosLibrary.meos.temporal_tsequence(temp, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tsequenceset(Pointer temp, int interp) {
		return MeosLibrary.meos.temporal_tsequenceset(temp, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_shift_time(Pointer inst, Pointer interv) {
		return MeosLibrary.meos.tinstant_shift_time(inst, interv);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_to_tsequence(Pointer inst, int interp) {
		return MeosLibrary.meos.tinstant_to_tsequence(inst, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_to_tsequence_free(Pointer inst, int interp) {
		return MeosLibrary.meos.tinstant_to_tsequence_free(inst, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_to_tsequenceset(Pointer inst, int interp) {
		return MeosLibrary.meos.tinstant_to_tsequenceset(inst, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_shift_scale_value(Pointer temp, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.tnumber_shift_scale_value(temp, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberinst_shift_value(Pointer inst, long shift) {
		return MeosLibrary.meos.tnumberinst_shift_value(inst, shift);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseq_shift_scale_value(Pointer seq, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.tnumberseq_shift_scale_value(seq, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_shift_scale_value(Pointer ss, long start, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.tnumberseqset_shift_scale_value(ss, start, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static void tsequence_restart(Pointer seq, int count) {
		MeosLibrary.meos.tsequence_restart(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_set_interp(Pointer seq, int interp) {
		return MeosLibrary.meos.tsequence_set_interp(seq, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_shift_scale_time(Pointer seq, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.tsequence_shift_scale_time(seq, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_subseq(Pointer seq, int from, int to, boolean lower_inc, boolean upper_inc) {
		return MeosLibrary.meos.tsequence_subseq(seq, from, to, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tinstant(Pointer seq) {
		return MeosLibrary.meos.tsequence_to_tinstant(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tsequenceset(Pointer seq) {
		return MeosLibrary.meos.tsequence_to_tsequenceset(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tsequenceset_free(Pointer seq) {
		return MeosLibrary.meos.tsequence_to_tsequenceset_free(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tsequenceset_interp(Pointer seq, int interp) {
		return MeosLibrary.meos.tsequence_to_tsequenceset_interp(seq, interp);
	}
	
	@SuppressWarnings("unused")
	public static void tsequenceset_restart(Pointer ss, int count) {
		MeosLibrary.meos.tsequenceset_restart(ss, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_set_interp(Pointer ss, int interp) {
		return MeosLibrary.meos.tsequenceset_set_interp(ss, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_shift_scale_time(Pointer ss, Pointer start, Pointer duration) {
		return MeosLibrary.meos.tsequenceset_shift_scale_time(ss, start, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_discrete(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_discrete(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_linear(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_linear(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_step(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_step(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_tinstant(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_tinstant(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_tsequence(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_tsequence(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_merge(Pointer inst1, Pointer inst2) {
		return MeosLibrary.meos.tinstant_merge(inst1, inst2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_merge_array(Pointer instants, int count) {
		return MeosLibrary.meos.tinstant_merge_array(instants, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_append_tinstant(Pointer seq, Pointer inst, double maxdist, Pointer maxt, boolean expand) {
		return MeosLibrary.meos.tsequence_append_tinstant(seq, inst, maxdist, maxt, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_append_tsequence(Pointer seq1, Pointer seq2, boolean expand) {
		return MeosLibrary.meos.tsequence_append_tsequence(seq1, seq2, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_timestamptz(Pointer seq, OffsetDateTime t, boolean connect) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tsequence_delete_timestamptz(seq, t_new, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_tstzset(Pointer seq, Pointer s, boolean connect) {
		return MeosLibrary.meos.tsequence_delete_tstzset(seq, s, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_tstzspan(Pointer seq, Pointer s, boolean connect) {
		return MeosLibrary.meos.tsequence_delete_tstzspan(seq, s, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_tstzspanset(Pointer seq, Pointer ss, boolean connect) {
		return MeosLibrary.meos.tsequence_delete_tstzspanset(seq, ss, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_insert(Pointer seq1, Pointer seq2, boolean connect) {
		return MeosLibrary.meos.tsequence_insert(seq1, seq2, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_merge(Pointer seq1, Pointer seq2) {
		return MeosLibrary.meos.tsequence_merge(seq1, seq2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_merge_array(Pointer sequences, int count) {
		return MeosLibrary.meos.tsequence_merge_array(sequences, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_append_tinstant(Pointer ss, Pointer inst, double maxdist, Pointer maxt, boolean expand) {
		return MeosLibrary.meos.tsequenceset_append_tinstant(ss, inst, maxdist, maxt, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_append_tsequence(Pointer ss, Pointer seq, boolean expand) {
		return MeosLibrary.meos.tsequenceset_append_tsequence(ss, seq, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_timestamptz(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tsequenceset_delete_timestamptz(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_tstzset(Pointer ss, Pointer s) {
		return MeosLibrary.meos.tsequenceset_delete_tstzset(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_tstzspan(Pointer ss, Pointer s) {
		return MeosLibrary.meos.tsequenceset_delete_tstzspan(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_tstzspanset(Pointer ss, Pointer ps) {
		return MeosLibrary.meos.tsequenceset_delete_tstzspanset(ss, ps);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_insert(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.tsequenceset_insert(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_merge(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.tsequenceset_merge(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_merge_array(Pointer seqsets, int count) {
		return MeosLibrary.meos.tsequenceset_merge_array(seqsets, count);
	}
	
	@SuppressWarnings("unused")
	public static void tspatial_set_stbox(Pointer temp, Pointer box) {
		MeosLibrary.meos.tspatial_set_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static void tgeoinst_set_stbox(Pointer inst, Pointer box) {
		MeosLibrary.meos.tgeoinst_set_stbox(inst, box);
	}
	
	@SuppressWarnings("unused")
	public static void tspatialseq_set_stbox(Pointer seq, Pointer box) {
		MeosLibrary.meos.tspatialseq_set_stbox(seq, box);
	}
	
	@SuppressWarnings("unused")
	public static void tspatialseqset_set_stbox(Pointer ss, Pointer box) {
		MeosLibrary.meos.tspatialseqset_set_stbox(ss, box);
	}
	
	@SuppressWarnings("unused")
	public static void tsequence_expand_bbox(Pointer seq, Pointer inst) {
		MeosLibrary.meos.tsequence_expand_bbox(seq, inst);
	}
	
	@SuppressWarnings("unused")
	public static void tsequence_set_bbox(Pointer seq, Pointer box) {
		MeosLibrary.meos.tsequence_set_bbox(seq, box);
	}
	
	@SuppressWarnings("unused")
	public static void tsequenceset_expand_bbox(Pointer ss, Pointer seq) {
		MeosLibrary.meos.tsequenceset_expand_bbox(ss, seq);
	}
	
	@SuppressWarnings("unused")
	public static void tsequenceset_set_bbox(Pointer ss, Pointer box) {
		MeosLibrary.meos.tsequenceset_set_bbox(ss, box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdiscseq_restrict_minmax(Pointer seq, boolean min, boolean atfunc) {
		return MeosLibrary.meos.tdiscseq_restrict_minmax(seq, min, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tcontseq_restrict_minmax(Pointer seq, boolean min, boolean atfunc) {
		return MeosLibrary.meos.tcontseq_restrict_minmax(seq, min, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_bbox_restrict_set(Pointer temp, Pointer set) {
		return MeosLibrary.meos.temporal_bbox_restrict_set(temp, set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_minmax(Pointer temp, boolean min, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_minmax(temp, min, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_timestamptz(Pointer temp, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.temporal_restrict_timestamptz(temp, t_new, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_tstzset(Pointer temp, Pointer s, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_tstzset(temp, s, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_tstzspan(Pointer temp, Pointer s, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_tstzspan(temp, s, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_tstzspanset(Pointer temp, Pointer ss, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_tstzspanset(temp, ss, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_value(Pointer temp, long value, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_value(temp, value, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_values(Pointer temp, Pointer set, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_values(temp, set, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_value_at_timestamptz(Pointer temp, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.temporal_value_at_timestamptz(temp, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_tstzspan(Pointer inst, Pointer period, boolean atfunc) {
		return MeosLibrary.meos.tinstant_restrict_tstzspan(inst, period, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_tstzspanset(Pointer inst, Pointer ss, boolean atfunc) {
		return MeosLibrary.meos.tinstant_restrict_tstzspanset(inst, ss, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_timestamptz(Pointer inst, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tinstant_restrict_timestamptz(inst, t_new, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_tstzset(Pointer inst, Pointer s, boolean atfunc) {
		return MeosLibrary.meos.tinstant_restrict_tstzset(inst, s, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_value(Pointer inst, long value, boolean atfunc) {
		return MeosLibrary.meos.tinstant_restrict_value(inst, value, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_values(Pointer inst, Pointer set, boolean atfunc) {
		return MeosLibrary.meos.tinstant_restrict_values(inst, set, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_restrict_geom(Pointer temp, Pointer gs, Pointer zspan, boolean atfunc) {
		return MeosLibrary.meos.tgeo_restrict_geom(temp, gs, zspan, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_restrict_stbox(Pointer temp, Pointer box, boolean border_inc, boolean atfunc) {
		return MeosLibrary.meos.tgeo_restrict_stbox(temp, box, border_inc, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoinst_restrict_geom(Pointer inst, Pointer gs, Pointer zspan, boolean atfunc) {
		return MeosLibrary.meos.tgeoinst_restrict_geom(inst, gs, zspan, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoinst_restrict_stbox(Pointer inst, Pointer box, boolean border_inc, boolean atfunc) {
		return MeosLibrary.meos.tgeoinst_restrict_stbox(inst, box, border_inc, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoseq_restrict_geom(Pointer seq, Pointer gs, Pointer zspan, boolean atfunc) {
		return MeosLibrary.meos.tgeoseq_restrict_geom(seq, gs, zspan, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeotseq_restrict_stbox(Pointer seq, Pointer box, boolean border_inc, boolean atfunc) {
		return MeosLibrary.meos.tgeotseq_restrict_stbox(seq, box, border_inc, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_restrict_geom(Pointer ss, Pointer gs, Pointer zspan, boolean atfunc) {
		return MeosLibrary.meos.tgeoseqset_restrict_geom(ss, gs, zspan, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_restrict_stbox(Pointer ss, Pointer box, boolean border_inc, boolean atfunc) {
		return MeosLibrary.meos.tgeoseqset_restrict_stbox(ss, box, border_inc, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_restrict_span(Pointer temp, Pointer span, boolean atfunc) {
		return MeosLibrary.meos.tnumber_restrict_span(temp, span, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_restrict_spanset(Pointer temp, Pointer ss, boolean atfunc) {
		return MeosLibrary.meos.tnumber_restrict_spanset(temp, ss, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberinst_restrict_span(Pointer inst, Pointer span, boolean atfunc) {
		return MeosLibrary.meos.tnumberinst_restrict_span(inst, span, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberinst_restrict_spanset(Pointer inst, Pointer ss, boolean atfunc) {
		return MeosLibrary.meos.tnumberinst_restrict_spanset(inst, ss, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_restrict_span(Pointer ss, Pointer span, boolean atfunc) {
		return MeosLibrary.meos.tnumberseqset_restrict_span(ss, span, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_restrict_spanset(Pointer ss, Pointer spanset, boolean atfunc) {
		return MeosLibrary.meos.tnumberseqset_restrict_spanset(ss, spanset, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_at_timestamptz(Pointer seq, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tsequence_at_timestamptz(seq, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_restrict_tstzspan(Pointer seq, Pointer s, boolean atfunc) {
		return MeosLibrary.meos.tsequence_restrict_tstzspan(seq, s, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_restrict_tstzspanset(Pointer seq, Pointer ss, boolean atfunc) {
		return MeosLibrary.meos.tsequence_restrict_tstzspanset(seq, ss, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_minmax(Pointer ss, boolean min, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_minmax(ss, min, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_tstzspan(Pointer ss, Pointer s, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_tstzspan(ss, s, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_tstzspanset(Pointer ss, Pointer ps, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_tstzspanset(ss, ps, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_timestamptz(Pointer ss, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tsequenceset_restrict_timestamptz(ss, t_new, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_tstzset(Pointer ss, Pointer s, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_tstzset(ss, s, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_value(Pointer ss, long value, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_value(ss, value, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_values(Pointer ss, Pointer s, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_values(ss, s, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static int tinstant_cmp(Pointer inst1, Pointer inst2) {
		return MeosLibrary.meos.tinstant_cmp(inst1, inst2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tinstant_eq(Pointer inst1, Pointer inst2) {
		return MeosLibrary.meos.tinstant_eq(inst1, inst2);
	}
	
	@SuppressWarnings("unused")
	public static int tsequence_cmp(Pointer seq1, Pointer seq2) {
		return MeosLibrary.meos.tsequence_cmp(seq1, seq2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequence_eq(Pointer seq1, Pointer seq2) {
		return MeosLibrary.meos.tsequence_eq(seq1, seq2);
	}
	
	@SuppressWarnings("unused")
	public static int tsequenceset_cmp(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.tsequenceset_cmp(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequenceset_eq(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.tsequenceset_eq(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.always_eq_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_eq_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.always_eq_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.always_ne_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ne_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.always_ne_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.always_ge_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_ge_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.always_ge_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.always_gt_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_gt_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.always_gt_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.always_le_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_le_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.always_le_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.always_lt_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int always_lt_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.always_lt_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.ever_eq_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_eq_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.ever_eq_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.ever_ne_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ne_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.ever_ne_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.ever_ge_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_ge_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.ever_ge_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.ever_gt_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_gt_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.ever_gt_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.ever_le_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_le_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.ever_le_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_base_temporal(long value, Pointer temp) {
		return MeosLibrary.meos.ever_lt_base_temporal(value, temp);
	}
	
	@SuppressWarnings("unused")
	public static int ever_lt_temporal_base(Pointer temp, long value) {
		return MeosLibrary.meos.ever_lt_temporal_base(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseq_derivative(Pointer seq) {
		return MeosLibrary.meos.tfloatseq_derivative(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_derivative(Pointer ss) {
		return MeosLibrary.meos.tfloatseqset_derivative(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberinst_abs(Pointer inst) {
		return MeosLibrary.meos.tnumberinst_abs(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseq_abs(Pointer seq) {
		return MeosLibrary.meos.tnumberseq_abs(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseq_angular_difference(Pointer seq) {
		return MeosLibrary.meos.tnumberseq_angular_difference(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseq_delta_value(Pointer seq) {
		return MeosLibrary.meos.tnumberseq_delta_value(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_abs(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_abs(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_angular_difference(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_angular_difference(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_delta_value(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_delta_value(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tnumber_number(Pointer temp, long value) {
		return MeosLibrary.meos.distance_tnumber_number(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static long nad_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.nad_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static long nad_tnumber_number(Pointer temp, long value) {
		return MeosLibrary.meos.nad_tnumber_number(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static long nad_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.nad_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static long nad_tnumber_tnumber(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.nad_tnumber_tnumber(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int spatial_srid(long d, meosType basetype) {
		return MeosLibrary.meos.spatial_srid(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean spatial_has_z(long d, meosType basetype) {
		return MeosLibrary.meos.spatial_has_z(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean spatial_is_geodetic(long d, meosType basetype) {
		return MeosLibrary.meos.spatial_is_geodetic(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean spatial_set_srid(long d, meosType basetype, int srid) {
		return MeosLibrary.meos.spatial_set_srid(d, basetype, srid);
	}
	
	@SuppressWarnings("unused")
	public static int tspatialinst_srid(Pointer inst) {
		return MeosLibrary.meos.tspatialinst_srid(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_azimuth(Pointer seq) {
		return MeosLibrary.meos.tpointseq_azimuth(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_cumulative_length(Pointer seq, double prevlength) {
		return MeosLibrary.meos.tpointseq_cumulative_length(seq, prevlength);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpointseq_is_simple(Pointer seq) {
		return MeosLibrary.meos.tpointseq_is_simple(seq);
	}
	
	@SuppressWarnings("unused")
	public static double tpointseq_length(Pointer seq) {
		return MeosLibrary.meos.tpointseq_length(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_linear_trajectory(Pointer seq, boolean unary_union) {
		return MeosLibrary.meos.tpointseq_linear_trajectory(seq, unary_union);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_speed(Pointer seq) {
		return MeosLibrary.meos.tpointseq_speed(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoseq_stboxes(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tgeoseq_stboxes(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_split_n_stboxes(Pointer seq, int max_count, Pointer count) {
		return MeosLibrary.meos.tpointseq_split_n_stboxes(seq, max_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_azimuth(Pointer ss) {
		return MeosLibrary.meos.tpointseqset_azimuth(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_cumulative_length(Pointer ss) {
		return MeosLibrary.meos.tpointseqset_cumulative_length(ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpointseqset_is_simple(Pointer ss) {
		return MeosLibrary.meos.tpointseqset_is_simple(ss);
	}
	
	@SuppressWarnings("unused")
	public static double tpointseqset_length(Pointer ss) {
		return MeosLibrary.meos.tpointseqset_length(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_speed(Pointer ss) {
		return MeosLibrary.meos.tpointseqset_speed(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeoseqset_stboxes(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tgeoseqset_stboxes(ss, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_split_n_stboxes(Pointer ss, int max_count, Pointer count) {
		return MeosLibrary.meos.tpointseqset_split_n_stboxes(ss, max_count, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_trajectory(Pointer ss) {
		return MeosLibrary.meos.tpointseqset_trajectory(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_get_coord(Pointer temp, int coord) {
		return MeosLibrary.meos.tpoint_get_coord(temp, coord);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeominst_tgeoginst(Pointer inst, boolean oper) {
		return MeosLibrary.meos.tgeominst_tgeoginst(inst, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeomseq_tgeogseq(Pointer seq, boolean oper) {
		return MeosLibrary.meos.tgeomseq_tgeogseq(seq, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeomseqset_tgeogseqset(Pointer ss, boolean oper) {
		return MeosLibrary.meos.tgeomseqset_tgeogseqset(ss, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeom_tgeog(Pointer temp, boolean oper) {
		return MeosLibrary.meos.tgeom_tgeog(temp, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeo_tpoint(Pointer temp, boolean oper) {
		return MeosLibrary.meos.tgeo_tpoint(temp, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_tnpoint(Pointer temp) {
		return MeosLibrary.meos.tgeompoint_tnpoint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnpoint_tgeompoint(Pointer temp) {
		return MeosLibrary.meos.tnpoint_tgeompoint(temp);
	}
	
	@SuppressWarnings("unused")
	public static void tspatialinst_set_srid(Pointer inst, int srid) {
		MeosLibrary.meos.tspatialinst_set_srid(inst, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_make_simple(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tpointseq_make_simple(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static void tpointseq_set_srid(Pointer seq, int srid) {
		MeosLibrary.meos.tpointseq_set_srid(seq, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_make_simple(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tpointseqset_make_simple(ss, count);
	}
	
	@SuppressWarnings("unused")
	public static void tpointseqset_set_srid(Pointer ss, int srid) {
		MeosLibrary.meos.tpointseqset_set_srid(ss, srid);
	}
	
	@SuppressWarnings("unused")
	public static double tnumberseq_integral(Pointer seq) {
		return MeosLibrary.meos.tnumberseq_integral(seq);
	}
	
	@SuppressWarnings("unused")
	public static double tnumberseq_twavg(Pointer seq) {
		return MeosLibrary.meos.tnumberseq_twavg(seq);
	}
	
	@SuppressWarnings("unused")
	public static double tnumberseqset_integral(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_integral(ss);
	}
	
	@SuppressWarnings("unused")
	public static double tnumberseqset_twavg(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_twavg(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_twcentroid(Pointer seq) {
		return MeosLibrary.meos.tpointseq_twcentroid(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_twcentroid(Pointer ss) {
		return MeosLibrary.meos.tpointseqset_twcentroid(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_compact(Pointer temp) {
		return MeosLibrary.meos.temporal_compact(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_compact(Pointer seq) {
		return MeosLibrary.meos.tsequence_compact(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_compact(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_compact(ss);
	}
	
	@SuppressWarnings("unused")
	public static void skiplist_free(Pointer list) {
		MeosLibrary.meos.skiplist_free(list);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_app_tinst_transfn(Pointer state, Pointer inst, int interp, double maxdist, Pointer maxt) {
		return MeosLibrary.meos.temporal_app_tinst_transfn(state, inst, interp, maxdist, maxt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_app_tseq_transfn(Pointer state, Pointer seq) {
		return MeosLibrary.meos.temporal_app_tseq_transfn(state, seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspanset_spans(Pointer ss, long vsize, long vorigin, Pointer count) {
		return MeosLibrary.meos.numspanset_spans(ss, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_time_spans(Pointer ss, Pointer duration, long torigin, Pointer count) {
		return MeosLibrary.meos.spanset_time_spans(ss, duration, torigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_value_spans(Pointer ss, long vsize, long vorigin, Pointer count) {
		return MeosLibrary.meos.spanset_value_spans(ss, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timespanset_spans(Pointer ss, Pointer duration, long torigin, Pointer count) {
		return MeosLibrary.meos.timespanset_spans(ss, duration, torigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_value_spans(Pointer temp, long size, long origin, Pointer count) {
		return MeosLibrary.meos.tnumber_value_spans(temp, size, origin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_value_boxes(Pointer temp, long vsize, long vorigin, Pointer count) {
		return MeosLibrary.meos.tnumber_value_boxes(temp, vsize, vorigin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_time_boxes(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tnumber_time_boxes(temp, duration, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_value_time_boxes(Pointer temp, long vsize, Pointer duration, long vorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tnumber_value_time_boxes(temp, vsize, duration, vorigin, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_value_split(Pointer temp, long vsize, long vorigin, Pointer bins, Pointer count) {
		return MeosLibrary.meos.tnumber_value_split(temp, vsize, vorigin, bins, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_get_value_time_tile(long value, OffsetDateTime t, long vsize, Pointer duration, long vorigin, OffsetDateTime torigin, meosType basetype, meosType spantype) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tbox_get_value_time_tile(value, t_new, vsize, duration, vorigin, torigin_new, basetype, spantype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_value_time_split(Pointer temp, long size, Pointer duration, long vorigin, OffsetDateTime torigin, Pointer value_bins, Pointer time_bins, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tnumber_value_time_split(temp, size, duration, vorigin, torigin_new, value_bins, time_bins, count);
	}
	
	@SuppressWarnings("unused")
	public static int date_in(String str) {
		return MeosLibrary.meos.date_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String date_out(int d) {
		return MeosLibrary.meos.date_out(d);
	}
	
	@SuppressWarnings("unused")
	public static int interval_cmp(Pointer interv1, Pointer interv2) {
		return MeosLibrary.meos.interval_cmp(interv1, interv2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer interval_in(String str, int typmod) {
		return MeosLibrary.meos.interval_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static Pointer interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs) {
		return MeosLibrary.meos.interval_make(years, months, weeks, days, hours, mins, secs);
	}
	
	@SuppressWarnings("unused")
	public static String interval_out(Pointer interv) {
		return MeosLibrary.meos.interval_out(interv);
	}
	
	@SuppressWarnings("unused")
	public static double pg_exp(double arg1) {
		return MeosLibrary.meos.pg_exp(arg1);
	}
	
	@SuppressWarnings("unused")
	public static double pg_ln(double arg1) {
		return MeosLibrary.meos.pg_ln(arg1);
	}
	
	@SuppressWarnings("unused")
	public static double pg_log10(double arg1) {
		return MeosLibrary.meos.pg_log10(arg1);
	}
	
	@SuppressWarnings("unused")
	public static long time_in(String str, int typmod) {
		return MeosLibrary.meos.time_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static String time_out(long t) {
		return MeosLibrary.meos.time_out(t);
	}
	
	@SuppressWarnings("unused")
	public static LocalDateTime timestamp_in(String str, int typmod) {
		var result = MeosLibrary.meos.timestamp_in(str, typmod);
		return LocalDateTime.ofEpochSecond(result, 0, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String timestamp_out(LocalDateTime t) {
		var t_new = t.toEpochSecond(ZoneOffset.UTC);
		return MeosLibrary.meos.timestamp_out(t_new);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_in(String str, int typmod) {
		var result = MeosLibrary.meos.timestamptz_in(str, typmod);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String timestamptz_out(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamptz_out(t_new);
	}
}