package functions;

import jnr.ffi.Pointer;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;
import utils.JarLibraryLoader;
import utils.meosCatalog.MeosEnums.meosType;
import utils.meosCatalog.MeosEnums.meosOper;

import java.time.*;

public class functions {
	public interface MeosLibrary {

		MeosLibrary INSTANCE = JarLibraryLoader.create(MeosLibrary.class, "meos").getLibraryInstance();

		MeosLibrary meos = MeosLibrary.INSTANCE;

		Pointer lwpoint_make(int srid, int hasz, int hasm, Pointer p);

		Pointer lwgeom_from_gserialized(Pointer g);

		Pointer gserialized_from_lwgeom(Pointer geom, Pointer size);

		int lwgeom_get_srid(Pointer geom);

		double lwpoint_get_x(Pointer point);

		double lwpoint_get_y(Pointer point);

		double lwpoint_get_z(Pointer point);

		double lwpoint_get_m(Pointer point);

		int lwgeom_has_z(Pointer geom);

		int lwgeom_has_m(Pointer geom);

		int meos_errno();

		int meos_errno_set(int err);

		int meos_errno_restore(int err);

		int meos_errno_reset();

		void meos_initialize_timezone(String name);

		void meos_finalize_timezone();

		void meos_initialize(String tz_str);

		void meos_finalize();

		boolean bool_in(String in_str);

		String bool_out(boolean b);

		Pointer cstring2text(String cstring);

		int pg_date_in(String str);

		String pg_date_out(int date);

		int pg_interval_cmp(Pointer interval1, Pointer interval2);

		Pointer pg_interval_in(String str, int typmod);

		Pointer pg_interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs);

		Pointer pg_interval_mul(Pointer span, double factor);

		String pg_interval_out(Pointer span);

		Pointer pg_interval_to_char(Pointer it, Pointer fmt);

		Pointer pg_interval_pl(Pointer span1, Pointer span2);

		long pg_time_in(String str, int typmod);

		String pg_time_out(long time);

		long pg_timestamp_in(String str, int typmod);

		Pointer pg_timestamp_mi(long dt1, long dt2);

		long pg_timestamp_mi_interval(long timestamp, Pointer span);

		String pg_timestamp_out(long dt);

		long pg_timestamp_pl_interval(long timestamp, Pointer span);

		Pointer pg_timestamp_to_char(long dt, Pointer fmt);

		long pg_timestamptz_in(String str, int typmod);

		String pg_timestamptz_out(long dt);

		Pointer pg_timestamptz_to_char(long dt, Pointer fmt);

		int pg_to_date(Pointer date_txt, Pointer fmt);

		long pg_to_timestamp(Pointer date_txt, Pointer fmt);

		String text2cstring(Pointer textptr);

		Pointer geography_from_hexewkb(String wkt);

		Pointer geography_from_text(String wkt, int srid);

		Pointer geometry_from_hexewkb(String wkt);

		Pointer geometry_from_text(String wkt, int srid);

		Pointer gserialized_as_ewkb(Pointer gs, String type);

		String gserialized_as_ewkt(Pointer gs, int precision);

		String gserialized_as_geojson(Pointer gs, int option, int precision, String srs);

		String gserialized_as_hexewkb(Pointer gs, String type);

		String gserialized_as_text(Pointer gs, int precision);

		Pointer gserialized_from_ewkb(Pointer bytea_wkb, int srid);

		Pointer gserialized_from_geojson(String geojson);

		String gserialized_out(Pointer gs);

		Pointer pgis_geography_in(String input, int geom_typmod);

		Pointer pgis_geometry_in(String input, int geom_typmod);

		boolean pgis_gserialized_same(Pointer gs1, Pointer gs2);

		Pointer bigintset_in(String str);

		String bigintset_out(Pointer set);

		Pointer bigintspan_in(String str);

		String bigintspan_out(Pointer s);

		Pointer bigintspanset_in(String str);

		String bigintspanset_out(Pointer ss);

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

		Pointer period_in(String str);

		String period_out(Pointer s);

		Pointer periodset_in(String str);

		String periodset_out(Pointer ss);

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

		Pointer timestampset_in(String str);

		String timestampset_out(Pointer set);

		Pointer bigintset_make(Pointer values, int count);

		Pointer bigintspan_make(long lower, long upper, boolean lower_inc, boolean upper_inc);

		Pointer floatset_make(Pointer values, int count);

		Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc);

		Pointer geoset_make(Pointer values, int count);

		Pointer intset_make(Pointer values, int count);

		Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc);

		Pointer period_make(long lower, long upper, boolean lower_inc, boolean upper_inc);

		Pointer set_copy(Pointer s);

		Pointer span_copy(Pointer s);

		Pointer spanset_copy(Pointer ps);

		Pointer spanset_make(Pointer spans, int count, boolean normalize);

		Pointer textset_make(Pointer values, int count);

		Pointer timestampset_make(Pointer values, int count);

		Pointer bigint_to_bigintset(long i);

		Pointer bigint_to_bigintspan(int i);

		Pointer bigint_to_bigintspanset(int i);

		Pointer float_to_floatset(double d);

		Pointer float_to_floatspan(double d);

		Pointer float_to_floatspanset(double d);

		Pointer geo_to_geoset(Pointer gs);

		Pointer int_to_intset(int i);

		Pointer int_to_intspan(int i);

		Pointer int_to_intspanset(int i);

		Pointer set_to_spanset(Pointer s);

		Pointer span_to_spanset(Pointer s);

		Pointer text_to_textset(Pointer txt);

		Pointer timestamp_to_period(long t);

		Pointer timestamp_to_periodset(long t);

		Pointer timestamp_to_tstzset(long t);

		long bigintset_end_value(Pointer s);

		long bigintset_start_value(Pointer s);

		boolean bigintset_value_n(Pointer s, int n, Pointer result);

		Pointer bigintset_values(Pointer s);

		int bigintspan_lower(Pointer s);

		int bigintspan_upper(Pointer s);

		int bigintspanset_lower(Pointer ss);

		int bigintspanset_upper(Pointer ss);

		double floatset_end_value(Pointer s);

		double floatset_start_value(Pointer s);

		boolean floatset_value_n(Pointer s, int n, Pointer result);

		Pointer floatset_values(Pointer s);

		double floatspan_lower(Pointer s);

		double floatspan_upper(Pointer s);

		double floatspanset_lower(Pointer ss);

		double floatspanset_upper(Pointer ss);

		Pointer geoset_end_value(Pointer s);

		int geoset_srid(Pointer set);

		Pointer geoset_start_value(Pointer s);

		boolean geoset_value_n(Pointer s, int n, Pointer result);

		Pointer geoset_values(Pointer s);

		int intset_end_value(Pointer s);

		int intset_start_value(Pointer s);

		boolean intset_value_n(Pointer s, int n, Pointer result);

		Pointer intset_values(Pointer s);

		int intspan_lower(Pointer s);

		int intspan_upper(Pointer s);

		int intspanset_lower(Pointer ss);

		int intspanset_upper(Pointer ss);

		Pointer period_duration(Pointer s);

		long period_lower(Pointer p);

		long period_upper(Pointer p);

		Pointer periodset_duration(Pointer ps, boolean boundspan);

		long periodset_end_timestamp(Pointer ps);

		long periodset_lower(Pointer ps);

		int periodset_num_timestamps(Pointer ps);

		long periodset_start_timestamp(Pointer ps);

		boolean periodset_timestamp_n(Pointer ps, int n, Pointer result);

		Pointer periodset_timestamps(Pointer ps, Pointer count);

		long periodset_upper(Pointer ps);

		long set_hash(Pointer s);

		long set_hash_extended(Pointer s, long seed);

		int set_num_values(Pointer s);

		Pointer set_span(Pointer s);

		long span_hash(Pointer s);

		long span_hash_extended(Pointer s, long seed);

		boolean span_lower_inc(Pointer s);

		boolean span_upper_inc(Pointer s);

		double span_width(Pointer s);

		Pointer spanset_end_span(Pointer ss);

		long spanset_hash(Pointer ps);

		long spanset_hash_extended(Pointer ps, long seed);

		boolean spanset_lower_inc(Pointer ss);

		int spanset_num_spans(Pointer ss);

		Pointer spanset_span(Pointer ss);

		Pointer spanset_span_n(Pointer ss, int i);

		Pointer spanset_spans(Pointer ss);

		Pointer spanset_start_span(Pointer ss);

		boolean spanset_upper_inc(Pointer ss);

		double spanset_width(Pointer ss, boolean boundspan);

		Pointer spatialset_stbox(Pointer s);

		Pointer textset_end_value(Pointer s);

		Pointer textset_start_value(Pointer s);

		boolean textset_value_n(Pointer s, int n, Pointer result);

		Pointer textset_values(Pointer s);

		long timestampset_end_timestamp(Pointer ts);

		long timestampset_start_timestamp(Pointer ts);

		boolean timestampset_timestamp_n(Pointer ts, int n, Pointer result);

		Pointer timestampset_values(Pointer ts);

		Pointer bigintset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer bigintspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer bigintspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer floatset_round(Pointer s, int maxdd);

		Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer floatspan_intspan(Pointer s);

		Pointer floatspan_round(Pointer s, int maxdd);

		Pointer floatspan_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer floatspanset_intspanset(Pointer ss);

		Pointer floatspanset_round(Pointer ss, int maxdd);

		Pointer floatspanset_shift_scale(Pointer ss, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer geoset_round(Pointer s, int maxdd);

		Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer intspan_floatspan(Pointer s);

		Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer intspanset_floatspanset(Pointer ss);

		Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer period_shift_scale(Pointer p, Pointer shift, Pointer duration);

		Pointer period_tprecision(Pointer s, Pointer duration, long torigin);

		Pointer periodset_shift_scale(Pointer ss, Pointer shift, Pointer duration);

		Pointer periodset_tprecision(Pointer ss, Pointer duration, long torigin);

		Pointer textset_lower(Pointer s);

		Pointer textset_upper(Pointer s);

		long timestamp_tprecision(long t, Pointer duration, long torigin);

		Pointer timestampset_shift_scale(Pointer ts, Pointer shift, Pointer duration);

		boolean intersection_bigintset_bigint(Pointer s, long i, Pointer result);

		boolean intersection_bigintspan_bigint(Pointer s, long i, Pointer result);

		boolean intersection_bigintspanset_bigint(Pointer ss, long i, Pointer result);

		boolean intersection_floatset_float(Pointer s, double d, Pointer result);

		boolean intersection_floatspan_float(Pointer s, double d, Pointer result);

		boolean intersection_floatspanset_float(Pointer ss, double d, Pointer result);

		boolean intersection_geoset_geo(Pointer s, Pointer gs, Pointer result);

		boolean intersection_intset_int(Pointer s, int i, Pointer result);

		boolean intersection_intspan_int(Pointer s, int i, Pointer result);

		boolean intersection_intspanset_int(Pointer ss, int i, Pointer result);

		boolean intersection_period_timestamp(Pointer s, long t, Pointer result);

		boolean intersection_periodset_timestamp(Pointer ss, long t, Pointer result);

		Pointer intersection_set_set(Pointer s1, Pointer s2);

		Pointer intersection_span_span(Pointer s1, Pointer s2);

		Pointer intersection_spanset_span(Pointer ss, Pointer s);

		Pointer intersection_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean intersection_textset_text(Pointer s, Pointer txt, Pointer result);

		boolean intersection_timestampset_timestamp(Pointer s, long t, Pointer result);

		boolean minus_bigint_bigintset(long i, Pointer s, Pointer result);

		boolean minus_bigint_bigintspan(long i, Pointer s, Pointer result);

		boolean minus_bigint_bigintspanset(long i, Pointer ss, Pointer result);

		Pointer minus_bigintset_bigint(Pointer s, long i);

		Pointer minus_bigintspan_bigint(Pointer s, long i);

		Pointer minus_bigintspanset_bigint(Pointer ss, long i);

		boolean minus_float_floatset(double d, Pointer s, Pointer result);

		boolean minus_float_floatspan(double d, Pointer s, Pointer result);

		boolean minus_float_floatspanset(double d, Pointer ss, Pointer result);

		Pointer minus_floatset_float(Pointer s, double d);

		Pointer minus_floatspan_float(Pointer s, double d);

		Pointer minus_floatspanset_float(Pointer ss, double d);

		boolean minus_geo_geoset(Pointer gs, Pointer s, Pointer result);

		Pointer minus_geoset_geo(Pointer s, Pointer gs);

		boolean minus_int_intset(int i, Pointer s, Pointer result);

		boolean minus_int_intspan(int i, Pointer s, Pointer result);

		boolean minus_int_intspanset(int i, Pointer ss, Pointer result);

		Pointer minus_intset_int(Pointer s, int i);

		Pointer minus_intspan_int(Pointer s, int i);

		Pointer minus_intspanset_int(Pointer ss, int i);

		Pointer minus_period_timestamp(Pointer s, long t);

		Pointer minus_periodset_timestamp(Pointer ss, long t);

		Pointer minus_set_set(Pointer s1, Pointer s2);

		Pointer minus_span_span(Pointer s1, Pointer s2);

		Pointer minus_span_spanset(Pointer s, Pointer ss);

		Pointer minus_spanset_span(Pointer ss, Pointer s);

		Pointer minus_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean minus_text_textset(Pointer txt, Pointer s, Pointer result);

		Pointer minus_textset_text(Pointer s, Pointer txt);

		boolean minus_timestamp_period(long t, Pointer s, Pointer result);

		boolean minus_timestamp_periodset(long t, Pointer ss, Pointer result);

		boolean minus_timestamp_timestampset(long t, Pointer s, Pointer result);

		Pointer minus_timestampset_timestamp(Pointer s, long t);

		Pointer union_bigintset_bigint(Pointer s, long i);

		Pointer union_bigintspan_bigint(Pointer s, long i);

		Pointer union_bigintspanset_bigint(Pointer ss, long i);

		Pointer union_floatset_float(Pointer s, double d);

		Pointer union_floatspan_float(Pointer s, double d);

		Pointer union_floatspanset_float(Pointer ss, double d);

		Pointer union_geoset_geo(Pointer s, Pointer gs);

		Pointer union_intset_int(Pointer s, int i);

		Pointer union_intspan_int(Pointer s, int i);

		Pointer union_intspanset_int(Pointer ss, int i);

		Pointer union_period_timestamp(Pointer s, long t);

		Pointer union_periodset_timestamp(Pointer ss, long t);

		Pointer union_set_set(Pointer s1, Pointer s2);

		Pointer union_span_span(Pointer s1, Pointer s2);

		Pointer union_spanset_span(Pointer ss, Pointer s);

		Pointer union_spanset_spanset(Pointer ss1, Pointer ss2);

		Pointer union_textset_text(Pointer s, Pointer txt);

		Pointer union_timestampset_timestamp(Pointer s, long t);

		boolean adjacent_bigintspan_bigint(Pointer s, long i);

		boolean adjacent_bigintspanset_bigint(Pointer ss, long i);

		boolean adjacent_floatspan_float(Pointer s, double d);

		boolean adjacent_floatspanset_float(Pointer ss, double d);

		boolean adjacent_intspan_int(Pointer s, int i);

		boolean adjacent_intspanset_int(Pointer ss, int i);

		boolean adjacent_period_timestamp(Pointer p, long t);

		boolean adjacent_periodset_timestamp(Pointer ps, long t);

		boolean adjacent_span_span(Pointer s1, Pointer s2);

		boolean adjacent_spanset_span(Pointer ss, Pointer s);

		boolean adjacent_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean contained_bigint_bigintset(long i, Pointer s);

		boolean contained_bigint_bigintspan(long i, Pointer s);

		boolean contained_bigint_bigintspanset(long i, Pointer ss);

		boolean contained_float_floatset(double d, Pointer s);

		boolean contained_float_floatspan(double d, Pointer s);

		boolean contained_float_floatspanset(double d, Pointer ss);

		boolean contained_int_intset(int i, Pointer s);

		boolean contained_int_intspan(int i, Pointer s);

		boolean contained_int_intspanset(int i, Pointer ss);

		boolean contained_set_set(Pointer s1, Pointer s2);

		boolean contained_span_span(Pointer s1, Pointer s2);

		boolean contained_span_spanset(Pointer s, Pointer ss);

		boolean contained_spanset_span(Pointer ss, Pointer s);

		boolean contained_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean contained_text_textset(Pointer txt, Pointer s);

		boolean contained_timestamp_period(long t, Pointer p);

		boolean contained_timestamp_periodset(long t, Pointer ss);

		boolean contained_timestamp_timestampset(long t, Pointer ts);

		boolean contains_bigintset_bigint(Pointer s, long i);

		boolean contains_bigintspan_bigint(Pointer s, long i);

		boolean contains_bigintspanset_bigint(Pointer ss, long i);

		boolean contains_floatset_float(Pointer s, double d);

		boolean contains_floatspan_float(Pointer s, double d);

		boolean contains_floatspanset_float(Pointer ss, double d);

		boolean contains_intset_int(Pointer s, int i);

		boolean contains_intspan_int(Pointer s, int i);

		boolean contains_intspanset_int(Pointer ss, int i);

		boolean contains_period_timestamp(Pointer p, long t);

		boolean contains_periodset_timestamp(Pointer ps, long t);

		boolean contains_set_set(Pointer s1, Pointer s2);

		boolean contains_span_span(Pointer s1, Pointer s2);

		boolean contains_span_spanset(Pointer s, Pointer ss);

		boolean contains_spanset_span(Pointer ss, Pointer s);

		boolean contains_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean contains_textset_text(Pointer s, Pointer t);

		boolean contains_timestampset_timestamp(Pointer s, long t);

		boolean overlaps_set_set(Pointer s1, Pointer s2);

		boolean overlaps_span_span(Pointer s1, Pointer s2);

		boolean overlaps_spanset_span(Pointer ss, Pointer s);

		boolean overlaps_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean after_period_timestamp(Pointer s, long t);

		boolean after_periodset_timestamp(Pointer ss, long t);

		boolean after_timestamp_period(long t, Pointer s);

		boolean after_timestamp_periodset(long t, Pointer ss);

		boolean after_timestamp_timestampset(long t, Pointer ts);

		boolean after_timestampset_timestamp(Pointer s, long t);

		boolean before_period_timestamp(Pointer s, long t);

		boolean before_periodset_timestamp(Pointer ss, long t);

		boolean before_timestamp_period(long t, Pointer s);

		boolean before_timestamp_periodset(long t, Pointer ss);

		boolean before_timestamp_timestampset(long t, Pointer ts);

		boolean before_timestampset_timestamp(Pointer s, long t);

		boolean left_bigint_bigintset(long i, Pointer s);

		boolean left_bigint_bigintspan(long i, Pointer s);

		boolean left_bigint_bigintspanset(long i, Pointer ss);

		boolean left_bigintset_bigint(Pointer s, long i);

		boolean left_bigintspan_bigint(Pointer s, long i);

		boolean left_bigintspanset_bigint(Pointer ss, long i);

		boolean left_float_floatset(double d, Pointer s);

		boolean left_float_floatspan(double d, Pointer s);

		boolean left_float_floatspanset(double d, Pointer ss);

		boolean left_floatset_float(Pointer s, double d);

		boolean left_floatspan_float(Pointer s, double d);

		boolean left_floatspanset_float(Pointer ss, double d);

		boolean left_int_intset(int i, Pointer s);

		boolean left_int_intspan(int i, Pointer s);

		boolean left_int_intspanset(int i, Pointer ss);

		boolean left_intset_int(Pointer s, int i);

		boolean left_intspan_int(Pointer s, int i);

		boolean left_intspanset_int(Pointer ss, int i);

		boolean left_set_set(Pointer s1, Pointer s2);

		boolean left_span_span(Pointer s1, Pointer s2);

		boolean left_span_spanset(Pointer s, Pointer ss);

		boolean left_spanset_span(Pointer ss, Pointer s);

		boolean left_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean left_text_textset(Pointer txt, Pointer s);

		boolean left_textset_text(Pointer s, Pointer txt);

		boolean overafter_period_timestamp(Pointer s, long t);

		boolean overafter_periodset_timestamp(Pointer ss, long t);

		boolean overafter_timestamp_period(long t, Pointer s);

		boolean overafter_timestamp_periodset(long t, Pointer ss);

		boolean overafter_timestamp_timestampset(long t, Pointer ts);

		boolean overafter_timestampset_timestamp(Pointer s, long t);

		boolean overbefore_period_timestamp(Pointer s, long t);

		boolean overbefore_periodset_timestamp(Pointer ss, long t);

		boolean overbefore_timestamp_period(long t, Pointer s);

		boolean overbefore_timestamp_periodset(long t, Pointer ss);

		boolean overbefore_timestamp_timestampset(long t, Pointer ts);

		boolean overbefore_timestampset_timestamp(Pointer s, long t);

		boolean overleft_bigint_bigintset(long i, Pointer s);

		boolean overleft_bigint_bigintspan(long i, Pointer s);

		boolean overleft_bigint_bigintspanset(long i, Pointer ss);

		boolean overleft_bigintset_bigint(Pointer s, long i);

		boolean overleft_bigintspan_bigint(Pointer s, long i);

		boolean overleft_bigintspanset_bigint(Pointer ss, long i);

		boolean overleft_float_floatset(double d, Pointer s);

		boolean overleft_float_floatspan(double d, Pointer s);

		boolean overleft_float_floatspanset(double d, Pointer ss);

		boolean overleft_floatset_float(Pointer s, double d);

		boolean overleft_floatspan_float(Pointer s, double d);

		boolean overleft_floatspanset_float(Pointer ss, double d);

		boolean overleft_int_intset(int i, Pointer s);

		boolean overleft_int_intspan(int i, Pointer s);

		boolean overleft_int_intspanset(int i, Pointer ss);

		boolean overleft_intset_int(Pointer s, int i);

		boolean overleft_intspan_int(Pointer s, int i);

		boolean overleft_intspanset_int(Pointer ss, int i);

		boolean overleft_set_set(Pointer s1, Pointer s2);

		boolean overleft_span_span(Pointer s1, Pointer s2);

		boolean overleft_span_spanset(Pointer s, Pointer ss);

		boolean overleft_spanset_span(Pointer ss, Pointer s);

		boolean overleft_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean overleft_text_textset(Pointer txt, Pointer s);

		boolean overleft_textset_text(Pointer s, Pointer txt);

		boolean overright_bigint_bigintset(long i, Pointer s);

		boolean overright_bigint_bigintspan(long i, Pointer s);

		boolean overright_bigint_bigintspanset(long i, Pointer ss);

		boolean overright_bigintset_bigint(Pointer s, long i);

		boolean overright_bigintspan_bigint(Pointer s, long i);

		boolean overright_bigintspanset_bigint(Pointer ss, long i);

		boolean overright_float_floatset(double d, Pointer s);

		boolean overright_float_floatspan(double d, Pointer s);

		boolean overright_float_floatspanset(double d, Pointer ss);

		boolean overright_floatset_float(Pointer s, double d);

		boolean overright_floatspan_float(Pointer s, double d);

		boolean overright_floatspanset_float(Pointer ss, double d);

		boolean overright_int_intset(int i, Pointer s);

		boolean overright_int_intspan(int i, Pointer s);

		boolean overright_int_intspanset(int i, Pointer ss);

		boolean overright_intset_int(Pointer s, int i);

		boolean overright_intspan_int(Pointer s, int i);

		boolean overright_intspanset_int(Pointer ss, int i);

		boolean overright_set_set(Pointer s1, Pointer s2);

		boolean overright_span_span(Pointer s1, Pointer s2);

		boolean overright_span_spanset(Pointer s, Pointer ss);

		boolean overright_spanset_span(Pointer ss, Pointer s);

		boolean overright_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean overright_text_textset(Pointer txt, Pointer s);

		boolean overright_textset_text(Pointer s, Pointer txt);

		boolean right_bigint_bigintset(long i, Pointer s);

		boolean right_bigint_bigintspan(long i, Pointer s);

		boolean right_bigint_bigintspanset(long i, Pointer ss);

		boolean right_bigintset_bigint(Pointer s, long i);

		boolean right_bigintspan_bigint(Pointer s, long i);

		boolean right_bigintspanset_bigint(Pointer ss, long i);

		boolean right_float_floatset(double d, Pointer s);

		boolean right_float_floatspan(double d, Pointer s);

		boolean right_float_floatspanset(double d, Pointer ss);

		boolean right_floatset_float(Pointer s, double d);

		boolean right_floatspan_float(Pointer s, double d);

		boolean right_floatspanset_float(Pointer ss, double d);

		boolean right_int_intset(int i, Pointer s);

		boolean right_int_intspan(int i, Pointer s);

		boolean right_int_intspanset(int i, Pointer ss);

		boolean right_intset_int(Pointer s, int i);

		boolean right_intspan_int(Pointer s, int i);

		boolean right_intspanset_int(Pointer ss, int i);

		boolean right_set_set(Pointer s1, Pointer s2);

		boolean right_span_span(Pointer s1, Pointer s2);

		boolean right_span_spanset(Pointer s, Pointer ss);

		boolean right_spanset_span(Pointer ss, Pointer s);

		boolean right_spanset_spanset(Pointer ss1, Pointer ss2);

		boolean right_text_textset(Pointer txt, Pointer s);

		boolean right_textset_text(Pointer s, Pointer txt);

		double distance_bigintset_bigint(Pointer s, long i);

		double distance_bigintspan_bigint(Pointer s, long i);

		double distance_bigintspanset_bigint(Pointer ss, long i);

		double distance_floatset_float(Pointer s, double d);

		double distance_floatspan_float(Pointer s, double d);

		double distance_floatspanset_float(Pointer ss, double d);

		double distance_intset_int(Pointer s, int i);

		double distance_intspan_int(Pointer s, int i);

		double distance_intspanset_int(Pointer ss, int i);

		double distance_period_timestamp(Pointer s, long t);

		double distance_periodset_timestamp(Pointer ss, long t);

		double distance_set_set(Pointer s1, Pointer s2);

		double distance_span_span(Pointer s1, Pointer s2);

		double distance_spanset_span(Pointer ss, Pointer s);

		double distance_spanset_spanset(Pointer ss1, Pointer ss2);

		double distance_timestampset_timestamp(Pointer s, long t);

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

		Pointer bigint_extent_transfn(Pointer s, long i);

		Pointer bigint_union_transfn(Pointer state, long i);

		Pointer float_extent_transfn(Pointer s, double d);

		Pointer float_union_transfn(Pointer state, double d);

		Pointer int_extent_transfn(Pointer s, int i);

		Pointer int_union_transfn(Pointer state, int i);

		Pointer period_tcount_transfn(Pointer state, Pointer p);

		Pointer periodset_tcount_transfn(Pointer state, Pointer ps);

		Pointer set_extent_transfn(Pointer span, Pointer set);

		Pointer set_union_finalfn(Pointer state);

		Pointer set_union_transfn(Pointer state, Pointer set);

		Pointer span_extent_transfn(Pointer s1, Pointer s2);

		Pointer span_union_transfn(Pointer state, Pointer span);

		Pointer spanset_extent_transfn(Pointer s, Pointer ss);

		Pointer spanset_union_finalfn(Pointer state);

		Pointer spanset_union_transfn(Pointer state, Pointer ss);

		Pointer text_union_transfn(Pointer state, Pointer txt);

		Pointer timestamp_extent_transfn(Pointer p, long t);

		Pointer timestamp_tcount_transfn(Pointer state, long t);

		Pointer timestamp_union_transfn(Pointer state, long t);

		Pointer timestampset_tcount_transfn(Pointer state, Pointer ts);

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

		Pointer float_period_to_tbox(double d, Pointer p);

		Pointer float_timestamp_to_tbox(double d, long t);

		Pointer geo_period_to_stbox(Pointer gs, Pointer p);

		Pointer geo_timestamp_to_stbox(Pointer gs, long t);

		Pointer int_period_to_tbox(int i, Pointer p);

		Pointer int_timestamp_to_tbox(int i, long t);

		Pointer span_period_to_tbox(Pointer span, Pointer p);

		Pointer span_timestamp_to_tbox(Pointer span, long t);

		Pointer stbox_copy(Pointer box);

		Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer p);

		Pointer tbox_copy(Pointer box);

		Pointer tbox_make(Pointer s, Pointer p);

		Pointer float_to_tbox(double d);

		Pointer geo_to_stbox(Pointer gs);

		Pointer int_to_tbox(int i);

		Pointer numset_to_tbox(Pointer s);

		Pointer numspan_to_tbox(Pointer s);

		Pointer numspanset_to_tbox(Pointer ss);

		Pointer period_to_stbox(Pointer p);

		Pointer period_to_tbox(Pointer p);

		Pointer periodset_to_stbox(Pointer ps);

		Pointer periodset_to_tbox(Pointer ps);

		Pointer stbox_to_geo(Pointer box);

		Pointer stbox_to_period(Pointer box);

		Pointer tbox_to_floatspan(Pointer box);

		Pointer tbox_to_period(Pointer box);

		Pointer timestamp_to_stbox(long t);

		Pointer timestamp_to_tbox(long t);

		Pointer timestampset_to_stbox(Pointer ts);

		Pointer timestampset_to_tbox(Pointer ss);

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

		Pointer stbox_expand_space(Pointer box, double d);

		Pointer stbox_expand_time(Pointer box, Pointer interval);

		Pointer stbox_get_space(Pointer box);

		Pointer stbox_round(Pointer box, int maxdd);

		Pointer stbox_set_srid(Pointer box, int srid);

		Pointer stbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);

		Pointer tbox_expand_time(Pointer box, Pointer interval);

		Pointer tbox_expand_value(Pointer box, double d);

		Pointer tbox_round(Pointer box, int maxdd);

		Pointer tbox_shift_scale_float(Pointer box, double shift, double width, boolean hasshift, boolean haswidth);

		Pointer tbox_shift_scale_int(Pointer box, int shift, int width, boolean hasshift, boolean haswidth);

		Pointer tbox_shift_scale_time(Pointer box, Pointer shift, Pointer duration);

		Pointer union_tbox_tbox(Pointer box1, Pointer box2, boolean strict);

		boolean inter_tbox_tbox(Pointer box1, Pointer box2, Pointer result);

		Pointer intersection_tbox_tbox(Pointer box1, Pointer box2);

		Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict);

		boolean inter_stbox_stbox(Pointer box1, Pointer box2, Pointer result);

		Pointer intersection_stbox_stbox(Pointer box1, Pointer box2);

		boolean contains_tbox_tbox(Pointer box1, Pointer box2);

		boolean contained_tbox_tbox(Pointer box1, Pointer box2);

		boolean overlaps_tbox_tbox(Pointer box1, Pointer box2);

		boolean same_tbox_tbox(Pointer box1, Pointer box2);

		boolean adjacent_tbox_tbox(Pointer box1, Pointer box2);

		boolean contains_stbox_stbox(Pointer box1, Pointer box2);

		boolean contained_stbox_stbox(Pointer box1, Pointer box2);

		boolean overlaps_stbox_stbox(Pointer box1, Pointer box2);

		boolean same_stbox_stbox(Pointer box1, Pointer box2);

		boolean adjacent_stbox_stbox(Pointer box1, Pointer box2);

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

		Pointer stbox_quad_split(Pointer box, Pointer count);

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

		String tbool_out(Pointer temp);

		String temporal_as_hexwkb(Pointer temp, byte variant, Pointer size_out);

		String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs);

		Pointer temporal_as_wkb(Pointer temp, byte variant, Pointer size_out);

		Pointer temporal_from_hexwkb(String hexwkb);

		Pointer temporal_from_mfjson(String mfjson);

		Pointer temporal_from_wkb(Pointer wkb, long size);

		Pointer tfloat_in(String str);

		String tfloat_out(Pointer temp, int maxdd);

		Pointer tgeogpoint_in(String str);

		Pointer tgeompoint_in(String str);

		Pointer tint_in(String str);

		String tint_out(Pointer temp);

		String tpoint_as_ewkt(Pointer temp, int maxdd);

		String tpoint_as_text(Pointer temp, int maxdd);

		String tpoint_out(Pointer temp, int maxdd);

		Pointer ttext_in(String str);

		String ttext_out(Pointer temp);

		Pointer tbool_from_base_temp(boolean b, Pointer temp);

		Pointer tboolinst_make(boolean b, long t);

		Pointer tboolseq_from_base_period(boolean b, Pointer p);

		Pointer tboolseq_from_base_timestampset(boolean b, Pointer ts);

		Pointer tboolseqset_from_base_periodset(boolean b, Pointer ps);

		Pointer temporal_copy(Pointer temp);

		Pointer tfloat_from_base_temp(double d, Pointer temp);

		Pointer tfloatinst_make(double d, long t);

		Pointer tfloatseq_from_base_period(double d, Pointer p, int interp);

		Pointer tfloatseq_from_base_timestampset(double d, Pointer ts);

		Pointer tfloatseqset_from_base_periodset(double d, Pointer ps, int interp);

		Pointer tint_from_base_temp(int i, Pointer temp);

		Pointer tintinst_make(int i, long t);

		Pointer tintseq_from_base_period(int i, Pointer p);

		Pointer tintseq_from_base_timestampset(int i, Pointer ts);

		Pointer tintseqset_from_base_periodset(int i, Pointer ps);

		Pointer tpoint_from_base_temp(Pointer gs, Pointer temp);

		Pointer tpointinst_make(Pointer gs, long t);

		Pointer tpointseq_from_base_period(Pointer gs, Pointer p, int interp);

		Pointer tpointseq_from_base_timestampset(Pointer gs, Pointer ts);

		Pointer tpointseqset_from_base_periodset(Pointer gs, Pointer ps, int interp);

		Pointer tsequence_make(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequenceset_make(Pointer sequences, int count, boolean normalize);

		Pointer tsequenceset_make_gaps(Pointer instants, int count, int interp, Pointer maxt, double maxdist);

		Pointer ttext_from_base_temp(Pointer txt, Pointer temp);

		Pointer ttextinst_make(Pointer txt, long t);

		Pointer ttextseq_from_base_period(Pointer txt, Pointer p);

		Pointer ttextseq_from_base_timestampset(Pointer txt, Pointer ts);

		Pointer ttextseqset_from_base_periodset(Pointer txt, Pointer ps);

		Pointer temporal_to_period(Pointer temp);

		Pointer tfloat_to_tint(Pointer temp);

		Pointer tint_to_tfloat(Pointer temp);

		Pointer tnumber_to_span(Pointer temp);

		boolean tbool_end_value(Pointer temp);

		boolean tbool_start_value(Pointer temp);

		Pointer tbool_values(Pointer temp, Pointer count);

		Pointer temporal_duration(Pointer temp, boolean boundspan);

		Pointer temporal_end_instant(Pointer temp);

		Pointer temporal_end_sequence(Pointer temp);

		long temporal_end_timestamp(Pointer temp);

		long temporal_hash(Pointer temp);

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

		Pointer temporal_start_instant(Pointer temp);

		Pointer temporal_start_sequence(Pointer temp);

		long temporal_start_timestamp(Pointer temp);

		Pointer temporal_stops(Pointer temp, double maxdist, Pointer minduration);

		String temporal_subtype(Pointer temp);

		Pointer temporal_time(Pointer temp);

		boolean temporal_timestamp_n(Pointer temp, int n, Pointer result);

		Pointer temporal_timestamps(Pointer temp, Pointer count);

		double tfloat_end_value(Pointer temp);

		double tfloat_max_value(Pointer temp);

		double tfloat_min_value(Pointer temp);

		double tfloat_start_value(Pointer temp);

		Pointer tfloat_values(Pointer temp, Pointer count);

		int tint_end_value(Pointer temp);

		int tint_max_value(Pointer temp);

		int tint_min_value(Pointer temp);

		int tint_start_value(Pointer temp);

		Pointer tint_values(Pointer temp, Pointer count);

		Pointer tnumber_valuespans(Pointer temp);

		Pointer tpoint_end_value(Pointer temp);

		Pointer tpoint_start_value(Pointer temp);

		Pointer tpoint_values(Pointer temp, Pointer count);

		Pointer ttext_end_value(Pointer temp);

		Pointer ttext_max_value(Pointer temp);

		Pointer ttext_min_value(Pointer temp);

		Pointer ttext_start_value(Pointer temp);

		Pointer ttext_values(Pointer temp, Pointer count);

		Pointer temporal_scale_time(Pointer temp, Pointer duration);

		Pointer temporal_set_interp(Pointer temp, int interp);

		Pointer temporal_shift_scale_time(Pointer temp, Pointer shift, Pointer duration);

		Pointer temporal_shift_time(Pointer temp, Pointer shift);

		Pointer temporal_to_tinstant(Pointer temp);

		Pointer temporal_to_tsequence(Pointer temp, int interp);

		Pointer temporal_to_tsequenceset(Pointer temp, int interp);

		Pointer tfloat_scale_value(Pointer temp, double width);

		Pointer tfloat_shift_scale_value(Pointer temp, double shift, double width);

		Pointer tfloat_shift_value(Pointer temp, double shift);

		Pointer tint_scale_value(Pointer temp, int width);

		Pointer tint_shift_scale_value(Pointer temp, int shift, int width);

		Pointer tint_shift_value(Pointer temp, int shift);

		Pointer temporal_append_tinstant(Pointer temp, Pointer inst, double maxdist, Pointer maxt, boolean expand);

		Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand);

		Pointer temporal_delete_period(Pointer temp, Pointer p, boolean connect);

		Pointer temporal_delete_periodset(Pointer temp, Pointer ps, boolean connect);

		Pointer temporal_delete_timestamp(Pointer temp, long t, boolean connect);

		Pointer temporal_delete_timestampset(Pointer temp, Pointer ts, boolean connect);

		Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect);

		Pointer temporal_merge(Pointer temp1, Pointer temp2);

		Pointer temporal_merge_array(Pointer temparr, int count);

		Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect);

		Pointer tbool_at_value(Pointer temp, boolean b);

		Pointer tbool_minus_value(Pointer temp, boolean b);

		boolean tbool_value_at_timestamp(Pointer temp, long t, boolean strict, Pointer value);

		Pointer temporal_at_max(Pointer temp);

		Pointer temporal_at_min(Pointer temp);

		Pointer temporal_at_period(Pointer temp, Pointer p);

		Pointer temporal_at_periodset(Pointer temp, Pointer ps);

		Pointer temporal_at_timestamp(Pointer temp, long t);

		Pointer temporal_at_timestampset(Pointer temp, Pointer ts);

		Pointer temporal_at_values(Pointer temp, Pointer set);

		Pointer temporal_minus_max(Pointer temp);

		Pointer temporal_minus_min(Pointer temp);

		Pointer temporal_minus_period(Pointer temp, Pointer p);

		Pointer temporal_minus_periodset(Pointer temp, Pointer ps);

		Pointer temporal_minus_timestamp(Pointer temp, long t);

		Pointer temporal_minus_timestampset(Pointer temp, Pointer ts);

		Pointer temporal_minus_values(Pointer temp, Pointer set);

		Pointer tfloat_at_value(Pointer temp, double d);

		Pointer tfloat_minus_value(Pointer temp, double d);

		boolean tfloat_value_at_timestamp(Pointer temp, long t, boolean strict, Pointer value);

		Pointer tint_at_value(Pointer temp, int i);

		Pointer tint_minus_value(Pointer temp, int i);

		boolean tint_value_at_timestamp(Pointer temp, long t, boolean strict, Pointer value);

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

		boolean tpoint_value_at_timestamp(Pointer temp, long t, boolean strict, Pointer value);

		Pointer ttext_at_value(Pointer temp, Pointer txt);

		Pointer ttext_minus_value(Pointer temp, Pointer txt);

		boolean ttext_value_at_timestamp(Pointer temp, long t, boolean strict, Pointer value);

		int temporal_cmp(Pointer temp1, Pointer temp2);

		boolean temporal_eq(Pointer temp1, Pointer temp2);

		boolean temporal_ge(Pointer temp1, Pointer temp2);

		boolean temporal_gt(Pointer temp1, Pointer temp2);

		boolean temporal_le(Pointer temp1, Pointer temp2);

		boolean temporal_lt(Pointer temp1, Pointer temp2);

		boolean temporal_ne(Pointer temp1, Pointer temp2);

		boolean tbool_always_eq(Pointer temp, boolean b);

		boolean tbool_ever_eq(Pointer temp, boolean b);

		boolean tfloat_always_eq(Pointer temp, double d);

		boolean tfloat_always_le(Pointer temp, double d);

		boolean tfloat_always_lt(Pointer temp, double d);

		boolean tfloat_ever_eq(Pointer temp, double d);

		boolean tfloat_ever_le(Pointer temp, double d);

		boolean tfloat_ever_lt(Pointer temp, double d);

		boolean tint_always_eq(Pointer temp, int i);

		boolean tint_always_le(Pointer temp, int i);

		boolean tint_always_lt(Pointer temp, int i);

		boolean tint_ever_eq(Pointer temp, int i);

		boolean tint_ever_le(Pointer temp, int i);

		boolean tint_ever_lt(Pointer temp, int i);

		boolean tpoint_always_eq(Pointer temp, Pointer gs);

		boolean tpoint_ever_eq(Pointer temp, Pointer gs);

		boolean ttext_always_eq(Pointer temp, Pointer txt);

		boolean ttext_always_le(Pointer temp, Pointer txt);

		boolean ttext_always_lt(Pointer temp, Pointer txt);

		boolean ttext_ever_eq(Pointer temp, Pointer txt);

		boolean ttext_ever_le(Pointer temp, Pointer txt);

		boolean ttext_ever_lt(Pointer temp, Pointer txt);

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

		double float_degrees(double value, boolean normalize);

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

		Pointer tfloat_round(Pointer temp, int maxdd);

		Pointer tfloatarr_round(Pointer temp, int count, int maxdd);

		Pointer tfloat_degrees(Pointer temp, boolean normalize);

		Pointer tfloat_derivative(Pointer temp);

		Pointer tfloat_radians(Pointer temp);

		Pointer tnumber_abs(Pointer temp);

		Pointer tnumber_angular_difference(Pointer temp);

		Pointer tnumber_delta_value(Pointer temp);

		Pointer textcat_text_ttext(Pointer txt, Pointer temp);

		Pointer textcat_ttext_text(Pointer temp, Pointer txt);

		Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2);

		Pointer ttext_upper(Pointer temp);

		Pointer ttext_lower(Pointer temp);

		Pointer distance_tfloat_float(Pointer temp, double d);

		Pointer distance_tint_int(Pointer temp, int i);

		Pointer distance_tnumber_tnumber(Pointer temp1, Pointer temp2);

		Pointer distance_tpoint_point(Pointer temp, Pointer gs);

		Pointer distance_tpoint_tpoint(Pointer temp1, Pointer temp2);

		double nad_stbox_geo(Pointer box, Pointer gs);

		double nad_stbox_stbox(Pointer box1, Pointer box2);

		double nad_tbox_tbox(Pointer box1, Pointer box2);

		double nad_tfloat_float(Pointer temp, double d);

		double nad_tfloat_tfloat(Pointer temp1, Pointer temp2);

		int nad_tint_int(Pointer temp, int i);

		int nad_tint_tint(Pointer temp1, Pointer temp2);

		double nad_tnumber_tbox(Pointer temp, Pointer box);

		double nad_tpoint_geo(Pointer temp, Pointer gs);

		double nad_tpoint_stbox(Pointer temp, Pointer box);

		double nad_tpoint_tpoint(Pointer temp1, Pointer temp2);

		Pointer nai_tpoint_geo(Pointer temp, Pointer gs);

		Pointer nai_tpoint_tpoint(Pointer temp1, Pointer temp2);

		boolean shortestline_tpoint_geo(Pointer temp, Pointer gs, Pointer result);

		boolean shortestline_tpoint_tpoint(Pointer temp1, Pointer temp2, Pointer result);

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

		Pointer geo_expand_space(Pointer gs, double d);

		Pointer tgeogpoint_to_tgeompoint(Pointer temp);

		Pointer tgeompoint_to_tgeogpoint(Pointer temp);

		Pointer tpoint_expand_space(Pointer temp, double d);

		Pointer tpoint_make_simple(Pointer temp, Pointer count);

		Pointer tpoint_round(Pointer temp, int maxdd);

		Pointer tpointarr_round(Pointer temp, int count, int maxdd);

		Pointer tpoint_set_srid(Pointer temp, int srid);

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

		Pointer tdwithin_tpoint_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue);

		Pointer tdwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue);

		Pointer tintersects_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);

		Pointer ttouches_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);

		Pointer tbool_tand_transfn(Pointer state, Pointer temp);

		Pointer tbool_tor_transfn(Pointer state, Pointer temp);

		Pointer temporal_extent_transfn(Pointer p, Pointer temp);

		Pointer temporal_tagg_finalfn(Pointer state);

		Pointer temporal_tcount_transfn(Pointer state, Pointer temp);

		Pointer tfloat_tmax_transfn(Pointer state, Pointer temp);

		Pointer tfloat_tmin_transfn(Pointer state, Pointer temp);

		Pointer tfloat_tsum_transfn(Pointer state, Pointer temp);

		Pointer tint_tmax_transfn(Pointer state, Pointer temp);

		Pointer tint_tmin_transfn(Pointer state, Pointer temp);

		Pointer tint_tsum_transfn(Pointer state, Pointer temp);

		Pointer tnumber_extent_transfn(Pointer box, Pointer temp);

		double tnumber_integral(Pointer temp);

		Pointer tnumber_tavg_finalfn(Pointer state);

		Pointer tnumber_tavg_transfn(Pointer state, Pointer temp);

		double tnumber_twavg(Pointer temp);

		Pointer tpoint_extent_transfn(Pointer box, Pointer temp);

		Pointer tpoint_tcentroid_finalfn(Pointer state);

		Pointer tpoint_tcentroid_transfn(Pointer state, Pointer temp);

		Pointer tpoint_twcentroid(Pointer temp);

		Pointer ttext_tmax_transfn(Pointer state, Pointer temp);

		Pointer ttext_tmin_transfn(Pointer state, Pointer temp);

		Pointer temporal_simplify_min_dist(Pointer temp, double dist);

		Pointer temporal_simplify_min_tdelta(Pointer temp, Pointer mint);

		Pointer temporal_simplify_dp(Pointer temp, double eps_dist, boolean synchronize);

		Pointer temporal_simplify_max_dist(Pointer temp, double eps_dist, boolean synchronize);

		Pointer temporal_tprecision(Pointer temp, Pointer duration, long origin);

		Pointer temporal_tsample(Pointer temp, Pointer duration, long origin);

		double temporal_dyntimewarp_distance(Pointer temp1, Pointer temp2);

		Pointer temporal_dyntimewarp_path(Pointer temp1, Pointer temp2, Pointer count);

		double temporal_frechet_distance(Pointer temp1, Pointer temp2);

		Pointer temporal_frechet_path(Pointer temp1, Pointer temp2, Pointer count);

		double temporal_hausdorff_distance(Pointer temp1, Pointer temp2);

		double float_bucket(double value, double size, double origin);

		Pointer floatspan_bucket_list(Pointer bounds, double size, double origin, Pointer count);

		int int_bucket(int value, int size, int origin);

		Pointer intspan_bucket_list(Pointer bounds, int size, int origin, Pointer count);

		Pointer period_bucket_list(Pointer bounds, Pointer duration, long origin, Pointer count);

		Pointer stbox_tile_list(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, long torigin, Pointer count);

		Pointer tintbox_tile_list(Pointer box, int xsize, Pointer duration, int xorigin, long torigin, Pointer count);

		Pointer tfloatbox_tile_list(Pointer box, double xsize, Pointer duration, double xorigin, long torigin, Pointer count);

		Pointer temporal_time_split(Pointer temp, Pointer duration, long torigin, Pointer time_buckets, Pointer count);

		Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer value_buckets, Pointer count);

		Pointer tfloat_value_time_split(Pointer temp, double size, Pointer duration, double vorigin, long torigin, Pointer value_buckets, Pointer time_buckets, Pointer count);

		long timestamptz_bucket(long timestamp, Pointer duration, long origin);

		Pointer tint_value_split(Pointer temp, int size, int origin, Pointer value_buckets, Pointer count);

		Pointer tint_value_time_split(Pointer temp, int size, Pointer duration, int vorigin, long torigin, Pointer value_buckets, Pointer time_buckets, Pointer count);

		String meostype_name(meosType temptype);

		meosType temptype_basetype(meosType temptype);

		meosType settype_basetype(meosType settype);

		meosType spantype_basetype(meosType spantype);

		meosType spantype_spansettype(meosType spantype);

		meosType spansettype_spantype(meosType spansettype);

		meosType basetype_spantype(meosType basetype);

		meosType basetype_settype(meosType basetype);

		boolean geo_basetype(meosType basetype);

		boolean spatial_basetype(meosType basetype);

		boolean time_type(meosType timetype);

		boolean set_type(meosType type);

		boolean numset_type(meosType type);

		boolean ensure_numset_type(meosType type);

		boolean timeset_type(meosType type);

		boolean set_spantype(meosType type);

		boolean ensure_set_spantype(meosType type);

		boolean alphanumset_type(meosType settype);

		boolean geoset_type(meosType type);

		boolean ensure_geoset_type(meosType type);

		boolean spatialset_type(meosType type);

		boolean ensure_spatialset_type(meosType type);

		boolean span_basetype(meosType type);

		boolean span_canon_basetype(meosType type);

		boolean span_type(meosType type);

		boolean numspan_basetype(meosType type);

		boolean numspan_type(meosType type);

		boolean ensure_numspan_type(meosType type);

		boolean timespan_basetype(meosType type);

		boolean timespan_type(meosType type);

		boolean spanset_type(meosType type);

		boolean timespanset_type(meosType type);

		boolean temporal_type(meosType temptype);

		boolean temptype_continuous(meosType temptype);

		boolean basetype_byvalue(meosType type);

		boolean basetype_varlength(meosType type);

		short basetype_length(meosType basetype);

		boolean talpha_type(meosType temptype);

		boolean tnumber_type(meosType temptype);

		boolean ensure_tnumber_type(meosType temptype);

		boolean tnumber_basetype(meosType basetype);

		boolean tnumber_spantype(meosType settype);

		boolean tnumber_spansettype(meosType spansettype);

		boolean tspatial_type(meosType temptype);

		boolean ensure_tspatial_type(meosType temptype);

		boolean tspatial_basetype(meosType basetype);

		boolean tgeo_type(meosType type);

		boolean ensure_tgeo_type(meosType type);

		boolean ensure_tnumber_tgeo_type(meosType type);

		long datum_hash(long d, meosType basetype);

		long datum_hash_extended(long d, meosType basetype, long seed);

		Pointer set_in(String str, meosType basetype);

		String set_out(Pointer s, int maxdd);

		Pointer span_in(String str, meosType spantype);

		String span_out(Pointer s, int maxdd);

		Pointer spanset_in(String str, meosType spantype);

		String spanset_out(Pointer ss, int maxdd);

		Pointer set_compact(Pointer s);

		Pointer set_make(Pointer values, int count, meosType basetype, boolean ordered);

		Pointer set_make_exp(Pointer values, int count, int maxcount, meosType basetype, boolean ordered);

		Pointer set_make_free(Pointer values, int count, meosType basetype, boolean ordered);

		Pointer span_make(long lower, long upper, boolean lower_inc, boolean upper_inc, meosType basetype);

		void span_set(long lower, long upper, boolean lower_inc, boolean upper_inc, meosType basetype, Pointer s);

		Pointer spanset_compact(Pointer ss);

		Pointer spanset_make_exp(Pointer spans, int count, int maxcount, boolean normalize, boolean ordered);

		Pointer spanset_make_free(Pointer spans, int count, boolean normalize);

		Pointer value_to_set(long d, meosType basetype);

		Pointer value_to_span(long d, meosType basetype);

		Pointer value_to_spanset(long d, meosType basetype);

		long set_end_value(Pointer s);

		int set_mem_size(Pointer s);

		void set_set_span(Pointer os, Pointer s);

		long set_start_value(Pointer s);

		boolean set_value_n(Pointer s, int n, Pointer result);

		Pointer set_values(Pointer s);

		int spanset_mem_size(Pointer ss);

		void spatialset_set_stbox(Pointer set, Pointer box);

		void value_set_span(long d, meosType basetype, Pointer s);

		void floatspan_round_int(Pointer span, long size, Pointer result);

		void floatspan_set_intspan(Pointer s1, Pointer s2);

		void intspan_set_floatspan(Pointer s1, Pointer s2);

		Pointer numset_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer numspan_shift_scale(Pointer s, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer numspanset_shift_scale(Pointer ss, long shift, long width, boolean hasshift, boolean haswidth);

		void span_expand(Pointer s1, Pointer s2);

		void span_shift(Pointer s, long value);

		void spanset_shift(Pointer s, long value);

		Pointer spanbase_extent_transfn(Pointer s, long d, meosType basetype);

		Pointer value_union_transfn(Pointer state, long d, meosType basetype);

		boolean adjacent_span_value(Pointer s, long d, meosType basetype);

		boolean adjacent_spanset_value(Pointer ss, long d, meosType basetype);

		boolean contains_span_value(Pointer s, long d, meosType basetype);

		boolean contains_spanset_value(Pointer ss, long d, meosType basetype);

		boolean contains_set_value(Pointer s, long d, meosType basetype);

		boolean contained_value_span(long d, meosType basetype, Pointer s);

		boolean contained_value_set(long d, meosType basetype, Pointer s);

		boolean contained_value_spanset(long d, meosType basetype, Pointer ss);

		boolean left_set_value(Pointer s, long d, meosType basetype);

		boolean left_span_value(Pointer s, long d, meosType basetype);

		boolean left_spanset_value(Pointer ss, long d, meosType basetype);

		boolean left_value_set(long d, meosType basetype, Pointer s);

		boolean left_value_span(long d, meosType basetype, Pointer s);

		boolean left_value_spanset(long d, meosType basetype, Pointer ss);

		boolean right_value_set(long d, meosType basetype, Pointer s);

		boolean right_set_value(Pointer s, long d, meosType basetype);

		boolean right_value_span(long d, meosType basetype, Pointer s);

		boolean right_value_spanset(long d, meosType basetype, Pointer ss);

		boolean right_span_value(Pointer s, long d, meosType basetype);

		boolean right_spanset_value(Pointer ss, long d, meosType basetype);

		boolean overleft_value_set(long d, meosType basetype, Pointer s);

		boolean overleft_set_value(Pointer s, long d, meosType basetype);

		boolean overleft_value_span(long d, meosType basetype, Pointer s);

		boolean overleft_value_spanset(long d, meosType basetype, Pointer ss);

		boolean overleft_span_value(Pointer s, long d, meosType basetype);

		boolean overleft_spanset_value(Pointer ss, long d, meosType basetype);

		boolean overright_value_set(long d, meosType basetype, Pointer s);

		boolean overright_set_value(Pointer s, long d, meosType basetype);

		boolean overright_value_span(long d, meosType basetype, Pointer s);

		boolean overright_value_spanset(long d, meosType basetype, Pointer ss);

		boolean overright_span_value(Pointer s, long d, meosType basetype);

		boolean overright_spanset_value(Pointer ss, long d, meosType basetype);

		boolean inter_span_span(Pointer s1, Pointer s2, Pointer result);

		boolean intersection_set_value(Pointer s, long d, meosType basetype, Pointer result);

		boolean intersection_span_value(Pointer s, long d, meosType basetype, Pointer result);

		boolean intersection_spanset_value(Pointer ss, long d, meosType basetype, Pointer result);

		Pointer minus_set_value(Pointer s, long d, meosType basetype);

		Pointer minus_span_value(Pointer s, long d, meosType basetype);

		Pointer minus_spanset_value(Pointer ss, long d, meosType basetype);

		boolean minus_value_set(long d, meosType basetype, Pointer s, Pointer result);

		boolean minus_value_span(long d, meosType basetype, Pointer s, Pointer result);

		boolean minus_value_spanset(long d, meosType basetype, Pointer ss, Pointer result);

		Pointer union_set_value(Pointer s, long d, meosType basetype);

		Pointer union_span_value(Pointer s, long v, meosType basetype);

		Pointer union_spanset_value(Pointer ss, long d, meosType basetype);

		double distance_value_value(long l, long r, meosType basetype);

		double distance_span_value(Pointer s, long d, meosType basetype);

		double distance_spanset_value(Pointer ss, long d, meosType basetype);

		double distance_set_value(Pointer s, long d, meosType basetype);

		Pointer number_period_to_tbox(long d, meosType basetype, Pointer p);

		Pointer number_timestamp_to_tbox(long d, meosType basetype, long t);

		void stbox_set(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer p, Pointer box);

		void tbox_set(Pointer s, Pointer p, Pointer box);

		void float_set_tbox(double d, Pointer box);

		boolean geo_set_stbox(Pointer gs, Pointer box);

		void geoarr_set_stbox(Pointer values, int count, Pointer box);

		void int_set_tbox(int i, Pointer box);

		void number_set_tbox(long d, meosType basetype, Pointer box);

		void numset_set_tbox(Pointer s, Pointer box);

		void numspan_set_tbox(Pointer span, Pointer box);

		void numspanset_set_tbox(Pointer ss, Pointer box);

		void period_set_stbox(Pointer p, Pointer box);

		void period_set_tbox(Pointer p, Pointer box);

		void periodset_set_stbox(Pointer ps, Pointer box);

		void periodset_set_tbox(Pointer ps, Pointer box);

		void stbox_set_box3d(Pointer box, Pointer box3d);

		void stbox_set_gbox(Pointer box, Pointer gbox);

		void timestamp_set_stbox(long t, Pointer box);

		void timestamp_set_tbox(long t, Pointer box);

		void timestampset_set_stbox(Pointer ts, Pointer box);

		void timestampset_set_tbox(Pointer ts, Pointer box);

		Pointer tbox_shift_scale_value(Pointer box, long shift, long width, boolean hasshift, boolean haswidth);

		void stbox_expand(Pointer box1, Pointer box2);

		void tbox_expand(Pointer box1, Pointer box2);

		void bbox_union_span_span(Pointer s1, Pointer s2, Pointer result);

		Pointer geoarr_as_text(Pointer geoarr, int count, int maxdd, boolean extended);

		String tboolinst_as_mfjson(Pointer inst, boolean with_bbox);

		Pointer tboolinst_in(String str);

		String tboolseq_as_mfjson(Pointer seq, boolean with_bbox);

		Pointer tboolseq_in(String str, int interp);

		String tboolseqset_as_mfjson(Pointer ss, boolean with_bbox);

		Pointer tboolseqset_in(String str);

		Pointer temporal_in(String str, meosType temptype);

		String temporal_out(Pointer temp, int maxdd);

		Pointer temporal_values(Pointer temp, Pointer count);

		Pointer temporalarr_out(Pointer temparr, int count, int maxdd);

		String tfloatinst_as_mfjson(Pointer inst, boolean with_bbox, int precision);

		Pointer tfloatinst_in(String str);

		String tfloatseq_as_mfjson(Pointer seq, boolean with_bbox, int precision);

		Pointer tfloatseq_in(String str, int interp);

		String tfloatseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision);

		Pointer tfloatseqset_in(String str);

		Pointer tgeogpointinst_in(String str);

		Pointer tgeogpointseq_in(String str, int interp);

		Pointer tgeogpointseqset_in(String str);

		Pointer tgeompointinst_in(String str);

		Pointer tgeompointseq_in(String str, int interp);

		Pointer tgeompointseqset_in(String str);

		String tinstant_as_mfjson(Pointer inst, int precision, boolean with_bbox, String srs);

		Pointer tinstant_in(String str, meosType temptype);

		String tinstant_out(Pointer inst, int maxdd);

		String tintinst_as_mfjson(Pointer inst, boolean with_bbox);

		Pointer tintinst_in(String str);

		String tintseq_as_mfjson(Pointer seq, boolean with_bbox);

		Pointer tintseq_in(String str, int interp);

		String tintseqset_as_mfjson(Pointer ss, boolean with_bbox);

		Pointer tintseqset_in(String str);

		Pointer tpointarr_as_text(Pointer temparr, int count, int maxdd, boolean extended);

		String tpointinst_as_mfjson(Pointer inst, boolean with_bbox, int precision, String srs);

		String tpointseq_as_mfjson(Pointer seq, boolean with_bbox, int precision, String srs);

		String tpointseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision, String srs);

		String tsequence_as_mfjson(Pointer seq, int precision, boolean with_bbox, String srs);

		Pointer tsequence_in(String str, meosType temptype, int interp);

		String tsequence_out(Pointer seq, int maxdd);

		String tsequenceset_as_mfjson(Pointer ss, int precision, boolean with_bbox, String srs);

		Pointer tsequenceset_in(String str, meosType temptype, int interp);

		String tsequenceset_out(Pointer ss, int maxdd);

		String ttextinst_as_mfjson(Pointer inst, boolean with_bbox);

		Pointer ttextinst_in(String str);

		String ttextseq_as_mfjson(Pointer seq, boolean with_bbox);

		Pointer ttextseq_in(String str, int interp);

		String ttextseqset_as_mfjson(Pointer ss, boolean with_bbox);

		Pointer ttextseqset_in(String str);

		Pointer temporal_from_base_temp(long value, meosType temptype, Pointer temp);

		Pointer tinstant_copy(Pointer inst);

		Pointer tinstant_make(long value, meosType temptype, long t);

		Pointer tpointseq_make_coords(Pointer xcoords, Pointer ycoords, Pointer zcoords, Pointer times, int count, int srid, boolean geodetic, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequence_from_base_timestampset(long value, meosType temptype, Pointer ss);

		Pointer tsequence_make_exp(Pointer instants, int count, int maxcount, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequence_compact(Pointer seq);

		void tsequence_restart(Pointer seq, int last);

		Pointer tsequence_subseq(Pointer seq, int from, int to, boolean lower_inc, boolean upper_inc);

		Pointer tsequence_copy(Pointer seq);

		Pointer tsequence_from_base_period(long value, meosType temptype, Pointer p, int interp);

		Pointer tsequence_make_free(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize);

		Pointer tsequenceset_make_exp(Pointer sequences, int count, int maxcount, boolean normalize);

		Pointer tsequenceset_compact(Pointer ss);

		Pointer tsequenceset_make_free(Pointer sequences, int count, boolean normalize);

		void tsequenceset_restart(Pointer ss, int last);

		Pointer tsequenceset_copy(Pointer ss);

		Pointer tseqsetarr_to_tseqset(Pointer seqsets, int count, int totalseqs);

		Pointer tsequenceset_from_base_periodset(long value, meosType temptype, Pointer ps, int interp);

		void temporal_set_period(Pointer temp, Pointer p);

		void tinstant_set_period(Pointer inst, Pointer p);

		void tsequence_set_period(Pointer seq, Pointer p);

		void tsequenceset_set_period(Pointer ss, Pointer p);

		long temporal_end_value(Pointer temp);

		long temporal_max_value(Pointer temp);

		long temporal_mem_size(Pointer temp);

		long temporal_min_value(Pointer temp);

		void temporal_set_bbox(Pointer temp, Pointer box);

		Pointer tfloatseq_derivative(Pointer seq);

		Pointer tfloatseqset_derivative(Pointer ss);

		void tnumber_set_span(Pointer temp, Pointer span);

		long temporal_start_value(Pointer temp);

		Pointer tnumberinst_abs(Pointer inst);

		Pointer tnumberseq_abs(Pointer seq);

		Pointer tnumberseqset_abs(Pointer ss);

		Pointer tnumberseq_angular_difference(Pointer seq);

		Pointer tnumberseqset_angular_difference(Pointer ss);

		Pointer tnumberseq_delta_value(Pointer seq);

		Pointer tnumberseqset_delta_value(Pointer ss);

		Pointer tnumberinst_valuespans(Pointer inst);

		Pointer tnumberseq_valuespans(Pointer seq);

		Pointer tnumberseqset_valuespans(Pointer ss);

		long tinstant_hash(Pointer inst);

		Pointer tinstant_instants(Pointer inst, Pointer count);

		void tinstant_set_bbox(Pointer inst, Pointer box);

		Pointer tinstant_time(Pointer inst);

		Pointer tinstant_timestamps(Pointer inst, Pointer count);

		long tinstant_value(Pointer inst);

		boolean tinstant_value_at_timestamp(Pointer inst, long t, Pointer result);

		long tinstant_value_copy(Pointer inst);

		Pointer tinstant_values(Pointer inst, Pointer count);

		Pointer tsequence_duration(Pointer seq);

		long tsequence_end_timestamp(Pointer seq);

		long tsequence_hash(Pointer seq);

		Pointer tsequence_instants(Pointer seq);

		Pointer tsequence_max_instant(Pointer seq);

		long tsequence_max_value(Pointer seq);

		Pointer tsequence_min_instant(Pointer seq);

		long tsequence_min_value(Pointer seq);

		Pointer tsequence_segments(Pointer seq, Pointer count);

		Pointer tsequence_sequences(Pointer seq, Pointer count);

		void tsequence_set_bbox(Pointer seq, Pointer box);

		void tsequence_expand_bbox(Pointer seq, Pointer inst);

		void tsequenceset_expand_bbox(Pointer ss, Pointer seq);

		long tsequence_start_timestamp(Pointer seq);

		Pointer tsequence_time(Pointer seq);

		Pointer tsequence_timestamps(Pointer seq, Pointer count);

		boolean tsequence_value_at_timestamp(Pointer seq, long t, boolean strict, Pointer result);

		Pointer tsequence_values(Pointer seq, Pointer count);

		Pointer tsequenceset_duration(Pointer ss, boolean boundspan);

		long tsequenceset_end_timestamp(Pointer ss);

		long tsequenceset_hash(Pointer ss);

		Pointer tsequenceset_inst_n(Pointer ss, int n);

		Pointer tsequenceset_instants(Pointer ss);

		Pointer tsequenceset_max_instant(Pointer ss);

		long tsequenceset_max_value(Pointer ss);

		Pointer tsequenceset_min_instant(Pointer ss);

		long tsequenceset_min_value(Pointer ss);

		int tsequenceset_num_instants(Pointer ss);

		int tsequenceset_num_timestamps(Pointer ss);

		Pointer tsequenceset_segments(Pointer ss, Pointer count);

		Pointer tsequenceset_sequences(Pointer ss);

		Pointer tsequenceset_sequences_p(Pointer ss);

		void tsequenceset_set_bbox(Pointer ss, Pointer box);

		long tsequenceset_start_timestamp(Pointer ss);

		Pointer tsequenceset_time(Pointer ss);

		boolean tsequenceset_timestamp_n(Pointer ss, int n, Pointer result);

		Pointer tsequenceset_timestamps(Pointer ss, Pointer count);

		boolean tsequenceset_value_at_timestamp(Pointer ss, long t, boolean strict, Pointer result);

		Pointer tsequenceset_values(Pointer ss, Pointer count);

		Pointer tinstant_merge(Pointer inst1, Pointer inst2);

		Pointer tinstant_merge_array(Pointer instants, int count);

		Pointer tinstant_shift_time(Pointer inst, Pointer interval);

		Pointer tinstant_to_tsequence(Pointer inst, int interp);

		Pointer tinstant_to_tsequenceset(Pointer inst, int interp);

		Pointer tnumber_shift_scale_value(Pointer temp, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer tnuminst_shift_value(Pointer inst, long shift);

		Pointer tnumberseq_shift_scale_value(Pointer seq, long shift, long width, boolean hasshift, boolean haswidth);

		Pointer tsequence_append_tinstant(Pointer seq, Pointer inst, double maxdist, Pointer maxt, boolean expand);

		Pointer tsequence_append_tsequence(Pointer seq1, Pointer seq2, boolean expand);

		Pointer tsequence_merge(Pointer seq1, Pointer seq2);

		Pointer tsequence_merge_array(Pointer sequences, int count);

		Pointer tsequence_set_interp(Pointer seq, int interp);

		Pointer tsequence_shift_scale_time(Pointer seq, Pointer shift, Pointer duration);

		Pointer tsequence_to_tinstant(Pointer seq);

		Pointer tsequence_to_tsequenceset(Pointer seq);

		Pointer tsequence_to_tsequenceset_interp(Pointer seq, int interp);

		Pointer tsequenceset_append_tinstant(Pointer ss, Pointer inst, double maxdist, Pointer maxt, boolean expand);

		Pointer tsequenceset_append_tsequence(Pointer ss, Pointer seq, boolean expand);

		Pointer tsequenceset_merge(Pointer ss1, Pointer ss2);

		Pointer tsequenceset_merge_array(Pointer seqsets, int count);

		Pointer tsequenceset_set_interp(Pointer ss, int interp);

		Pointer tnumberseqset_shift_scale_value(Pointer ss, long start, long width, boolean hasshift, boolean haswidth);

		Pointer tsequenceset_shift_scale_time(Pointer ss, Pointer start, Pointer duration);

		Pointer tsequenceset_to_tinstant(Pointer ss);

		Pointer tsequenceset_to_discrete(Pointer ss);

		Pointer tsequenceset_to_step(Pointer ss);

		Pointer tsequenceset_to_linear(Pointer ss);

		Pointer tsequenceset_to_tsequence(Pointer ss);

		boolean temporal_bbox_restrict_set(Pointer temp, Pointer set);

		Pointer temporal_restrict_minmax(Pointer temp, boolean min, boolean atfunc);

		Pointer temporal_restrict_period(Pointer temp, Pointer p, boolean atfunc);

		Pointer temporal_restrict_periodset(Pointer temp, Pointer ps, boolean atfunc);

		Pointer temporal_restrict_timestamp(Pointer temp, long t, boolean atfunc);

		Pointer temporal_restrict_timestampset(Pointer temp, Pointer ts, boolean atfunc);

		Pointer temporal_restrict_value(Pointer temp, long value, boolean atfunc);

		Pointer temporal_restrict_values(Pointer temp, Pointer set, boolean atfunc);

		boolean temporal_value_at_timestamp(Pointer temp, long t, boolean strict, Pointer result);

		Pointer tinstant_restrict_period(Pointer inst, Pointer period, boolean atfunc);

		Pointer tinstant_restrict_periodset(Pointer inst, Pointer ps, boolean atfunc);

		Pointer tinstant_restrict_timestamp(Pointer inst, long t, boolean atfunc);

		Pointer tinstant_restrict_timestampset(Pointer inst, Pointer ts, boolean atfunc);

		Pointer tinstant_restrict_value(Pointer inst, long value, boolean atfunc);

		Pointer tinstant_restrict_values(Pointer inst, Pointer set, boolean atfunc);

		Pointer tnumber_restrict_span(Pointer temp, Pointer span, boolean atfunc);

		Pointer tnumber_restrict_spanset(Pointer temp, Pointer ss, boolean atfunc);

		Pointer tnumberinst_restrict_span(Pointer inst, Pointer span, boolean atfunc);

		Pointer tnumberinst_restrict_spanset(Pointer inst, Pointer ss, boolean atfunc);

		Pointer tnumberseqset_restrict_span(Pointer ss, Pointer span, boolean atfunc);

		Pointer tnumberseqset_restrict_spanset(Pointer ss, Pointer spanset, boolean atfunc);

		Pointer tpoint_restrict_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period, boolean atfunc);

		Pointer tpoint_restrict_stbox(Pointer temp, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tpointinst_restrict_geom_time(Pointer inst, Pointer gs, Pointer zspan, Pointer period, boolean atfunc);

		Pointer tpointinst_restrict_stbox(Pointer inst, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tpointseq_restrict_geom_time(Pointer seq, Pointer gs, Pointer zspan, Pointer period, boolean atfunc);

		Pointer tpointseq_restrict_stbox(Pointer seq, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tpointseqset_restrict_geom_time(Pointer ss, Pointer gs, Pointer zspan, Pointer period, boolean atfunc);

		Pointer tpointseqset_restrict_stbox(Pointer ss, Pointer box, boolean border_inc, boolean atfunc);

		Pointer tsequence_at_period(Pointer seq, Pointer p);

		Pointer tsequence_at_timestamp(Pointer seq, long t);

		Pointer tsequence_restrict_period(Pointer seq, Pointer p, boolean atfunc);

		Pointer tsequence_restrict_periodset(Pointer seq, Pointer ps, boolean atfunc);

		Pointer tsequenceset_restrict_minmax(Pointer ss, boolean min, boolean atfunc);

		Pointer tsequenceset_restrict_period(Pointer ss, Pointer p, boolean atfunc);

		Pointer tsequenceset_restrict_periodset(Pointer ss, Pointer ps, boolean atfunc);

		Pointer tsequenceset_restrict_timestamp(Pointer ss, long t, boolean atfunc);

		Pointer tsequenceset_restrict_timestampset(Pointer ss, Pointer ts, boolean atfunc);

		Pointer tsequenceset_restrict_value(Pointer ss, long value, boolean atfunc);

		Pointer tsequenceset_restrict_values(Pointer ss, Pointer set, boolean atfunc);

		Pointer distance_tnumber_number(Pointer temp, long value, meosType valuetype, meosType restype);

		double nad_tnumber_number(Pointer temp, long value, meosType basetype);

		boolean temporal_always_eq(Pointer temp, long value);

		boolean temporal_always_le(Pointer temp, long value);

		boolean temporal_always_lt(Pointer temp, long value);

		boolean temporal_ever_eq(Pointer temp, long value);

		boolean temporal_ever_le(Pointer temp, long value);

		boolean temporal_ever_lt(Pointer temp, long value);

		boolean tinstant_always_eq(Pointer inst, long value);

		boolean tinstant_always_le(Pointer inst, long value);

		boolean tinstant_always_lt(Pointer inst, long value);

		boolean tinstant_ever_eq(Pointer inst, long value);

		boolean tinstant_ever_le(Pointer inst, long value);

		boolean tinstant_ever_lt(Pointer inst, long value);

		boolean tpointinst_always_eq(Pointer inst, long value);

		boolean tpointinst_ever_eq(Pointer inst, long value);

		boolean tpointseq_always_eq(Pointer seq, long value);

		boolean tpointseq_ever_eq(Pointer seq, long value);

		boolean tpointseqset_always_eq(Pointer ss, long value);

		boolean tpointseqset_ever_eq(Pointer ss, long value);

		boolean tsequence_always_eq(Pointer seq, long value);

		boolean tsequence_always_le(Pointer seq, long value);

		boolean tsequence_always_lt(Pointer seq, long value);

		boolean tsequence_ever_eq(Pointer seq, long value);

		boolean tsequence_ever_le(Pointer seq, long value);

		boolean tsequence_ever_lt(Pointer seq, long value);

		boolean tsequenceset_always_eq(Pointer ss, long value);

		boolean tsequenceset_always_le(Pointer ss, long value);

		boolean tsequenceset_always_lt(Pointer ss, long value);

		boolean tsequenceset_ever_eq(Pointer ss, long value);

		boolean tsequenceset_ever_le(Pointer ss, long value);

		boolean tsequenceset_ever_lt(Pointer ss, long value);

		int tinstant_cmp(Pointer inst1, Pointer inst2);

		boolean tinstant_eq(Pointer inst1, Pointer inst2);

		int tsequence_cmp(Pointer seq1, Pointer seq2);

		boolean tsequence_eq(Pointer seq1, Pointer seq2);

		int tsequenceset_cmp(Pointer ss1, Pointer ss2);

		boolean tsequenceset_eq(Pointer ss1, Pointer ss2);

		int tpointinst_srid(Pointer inst);

		Pointer tpointseq_trajectory(Pointer seq);

		Pointer tpointseq_azimuth(Pointer seq);

		Pointer tpointseq_cumulative_length(Pointer seq, double prevlength);

		boolean tpointseq_is_simple(Pointer seq);

		double tpointseq_length(Pointer seq);

		Pointer tpointseq_speed(Pointer seq);

		int tpointseq_srid(Pointer seq);

		Pointer tpointseq_stboxes(Pointer seq, Pointer count);

		Pointer tpointseqset_azimuth(Pointer ss);

		Pointer tpointseqset_cumulative_length(Pointer ss);

		boolean tpointseqset_is_simple(Pointer ss);

		double tpointseqset_length(Pointer ss);

		Pointer tpointseqset_speed(Pointer ss);

		int tpointseqset_srid(Pointer ss);

		Pointer tpointseqset_stboxes(Pointer ss, Pointer count);

		Pointer tpointseqset_trajectory(Pointer ss);

		Pointer tpoint_get_coord(Pointer temp, int coord);

		Pointer tgeompointinst_tgeogpointinst(Pointer inst, boolean oper);

		Pointer tgeompointseq_tgeogpointseq(Pointer seq, boolean oper);

		Pointer tgeompointseqset_tgeogpointseqset(Pointer ss, boolean oper);

		Pointer tgeompoint_tgeogpoint(Pointer temp, boolean oper);

		Pointer tpointinst_set_srid(Pointer inst, int srid);

		Pointer tpointseq_make_simple(Pointer seq, Pointer count);

		Pointer tpointseq_set_srid(Pointer seq, int srid);

		Pointer tpointseqset_make_simple(Pointer ss, Pointer count);

		Pointer tpointseqset_set_srid(Pointer ss, int srid);

		Pointer tsequence_insert(Pointer seq1, Pointer seq2, boolean connect);

		Pointer tsequenceset_insert(Pointer ss1, Pointer ss2);

		Pointer tsequence_delete_timestamp(Pointer seq, long t, boolean connect);

		Pointer tsequence_delete_timestampset(Pointer seq, Pointer ts, boolean connect);

		Pointer tsequence_delete_period(Pointer seq, Pointer p, boolean connect);

		Pointer tsequence_delete_periodset(Pointer seq, Pointer ps, boolean connect);

		Pointer tsequenceset_delete_timestamp(Pointer ss, long t);

		Pointer tsequenceset_delete_timestampset(Pointer ss, Pointer ts);

		Pointer tsequenceset_delete_period(Pointer ss, Pointer p);

		Pointer tsequenceset_delete_periodset(Pointer ss, Pointer ps);

		double tnumberseq_integral(Pointer seq);

		double tnumberseq_twavg(Pointer seq);

		double tnumberseqset_integral(Pointer ss);

		double tnumberseqset_twavg(Pointer ss);

		Pointer tpointseq_twcentroid(Pointer seq);

		Pointer tpointseqset_twcentroid(Pointer ss);

		Pointer temporal_compact(Pointer temp);

	}

	@SuppressWarnings("unused")
	public static Pointer lwpoint_make(int srid, int hasz, int hasm, Pointer p) {
		return MeosLibrary.meos.lwpoint_make(srid, hasz, hasm, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer lwgeom_from_gserialized(Pointer g) {
		return MeosLibrary.meos.lwgeom_from_gserialized(g);
	}
	
	@SuppressWarnings("unused")
	public static Pointer gserialized_from_lwgeom(Pointer geom, Pointer size) {
		return MeosLibrary.meos.gserialized_from_lwgeom(geom, size);
	}
	
	@SuppressWarnings("unused")
	public static int lwgeom_get_srid(Pointer geom) {
		return MeosLibrary.meos.lwgeom_get_srid(geom);
	}
	
	@SuppressWarnings("unused")
	public static double lwpoint_get_x(Pointer point) {
		return MeosLibrary.meos.lwpoint_get_x(point);
	}
	
	@SuppressWarnings("unused")
	public static double lwpoint_get_y(Pointer point) {
		return MeosLibrary.meos.lwpoint_get_y(point);
	}
	
	@SuppressWarnings("unused")
	public static double lwpoint_get_z(Pointer point) {
		return MeosLibrary.meos.lwpoint_get_z(point);
	}
	
	@SuppressWarnings("unused")
	public static double lwpoint_get_m(Pointer point) {
		return MeosLibrary.meos.lwpoint_get_m(point);
	}
	
	@SuppressWarnings("unused")
	public static int lwgeom_has_z(Pointer geom) {
		return MeosLibrary.meos.lwgeom_has_z(geom);
	}
	
	@SuppressWarnings("unused")
	public static int lwgeom_has_m(Pointer geom) {
		return MeosLibrary.meos.lwgeom_has_m(geom);
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
	public static void meos_finalize_timezone() {
		MeosLibrary.meos.meos_finalize_timezone();
	}
	
	@SuppressWarnings("unused")
	public static void meos_initialize(String tz_str) {
		MeosLibrary.meos.meos_initialize(tz_str);
	}
	
	@SuppressWarnings("unused")
	public static void meos_finalize() {
		MeosLibrary.meos.meos_finalize();
	}
	
	@SuppressWarnings("unused")
	public static boolean bool_in(String in_str) {
		return MeosLibrary.meos.bool_in(in_str);
	}
	
	@SuppressWarnings("unused")
	public static String bool_out(boolean b) {
		return MeosLibrary.meos.bool_out(b);
	}
	
	@SuppressWarnings("unused")
	public static Pointer cstring2text(String cstring) {
		return MeosLibrary.meos.cstring2text(cstring);
	}
	
	@SuppressWarnings("unused")
	public static int pg_date_in(String str) {
		return MeosLibrary.meos.pg_date_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String pg_date_out(int date) {
		return MeosLibrary.meos.pg_date_out(date);
	}
	
	@SuppressWarnings("unused")
	public static int pg_interval_cmp(Pointer interval1, Pointer interval2) {
		return MeosLibrary.meos.pg_interval_cmp(interval1, interval2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_in(String str, int typmod) {
		return MeosLibrary.meos.pg_interval_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs) {
		return MeosLibrary.meos.pg_interval_make(years, months, weeks, days, hours, mins, secs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_mul(Pointer span, double factor) {
		return MeosLibrary.meos.pg_interval_mul(span, factor);
	}
	
	@SuppressWarnings("unused")
	public static String pg_interval_out(Pointer span) {
		return MeosLibrary.meos.pg_interval_out(span);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_to_char(Pointer it, Pointer fmt) {
		return MeosLibrary.meos.pg_interval_to_char(it, fmt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_interval_pl(Pointer span1, Pointer span2) {
		return MeosLibrary.meos.pg_interval_pl(span1, span2);
	}
	
	@SuppressWarnings("unused")
	public static long pg_time_in(String str, int typmod) {
		return MeosLibrary.meos.pg_time_in(str, typmod);
	}
	
	@SuppressWarnings("unused")
	public static String pg_time_out(long time) {
		return MeosLibrary.meos.pg_time_out(time);
	}
	
	@SuppressWarnings("unused")
	public static LocalDateTime pg_timestamp_in(String str, int typmod) {
		var result = MeosLibrary.meos.pg_timestamp_in(str, typmod);
		return LocalDateTime.ofEpochSecond(result, 0, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_timestamp_mi(OffsetDateTime dt1, OffsetDateTime dt2) {
		var dt1_new = dt1.toEpochSecond();
		var dt2_new = dt2.toEpochSecond();
		return MeosLibrary.meos.pg_timestamp_mi(dt1_new, dt2_new);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime pg_timestamp_mi_interval(OffsetDateTime timestamp, Pointer span) {
		var timestamp_new = timestamp.toEpochSecond();
		var result = MeosLibrary.meos.pg_timestamp_mi_interval(timestamp_new, span);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String pg_timestamp_out(LocalDateTime dt) {
		var dt_new = dt.toEpochSecond(ZoneOffset.UTC);
		return MeosLibrary.meos.pg_timestamp_out(dt_new);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime pg_timestamp_pl_interval(OffsetDateTime timestamp, Pointer span) {
		var timestamp_new = timestamp.toEpochSecond();
		var result = MeosLibrary.meos.pg_timestamp_pl_interval(timestamp_new, span);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_timestamp_to_char(LocalDateTime dt, Pointer fmt) {
		var dt_new = dt.toEpochSecond(ZoneOffset.UTC);
		return MeosLibrary.meos.pg_timestamp_to_char(dt_new, fmt);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime pg_timestamptz_in(String str, int typmod) {
		var result = MeosLibrary.meos.pg_timestamptz_in(str, typmod);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String pg_timestamptz_out(OffsetDateTime dt) {
		var dt_new = dt.toEpochSecond();
		return MeosLibrary.meos.pg_timestamptz_out(dt_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pg_timestamptz_to_char(OffsetDateTime dt, Pointer fmt) {
		var dt_new = dt.toEpochSecond();
		return MeosLibrary.meos.pg_timestamptz_to_char(dt_new, fmt);
	}
	
	@SuppressWarnings("unused")
	public static int pg_to_date(Pointer date_txt, Pointer fmt) {
		return MeosLibrary.meos.pg_to_date(date_txt, fmt);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime pg_to_timestamp(Pointer date_txt, Pointer fmt) {
		var result = MeosLibrary.meos.pg_to_timestamp(date_txt, fmt);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static String text2cstring(Pointer textptr) {
		return MeosLibrary.meos.text2cstring(textptr);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geography_from_hexewkb(String wkt) {
		return MeosLibrary.meos.geography_from_hexewkb(wkt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geography_from_text(String wkt, int srid) {
		return MeosLibrary.meos.geography_from_text(wkt, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geometry_from_hexewkb(String wkt) {
		return MeosLibrary.meos.geometry_from_hexewkb(wkt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geometry_from_text(String wkt, int srid) {
		return MeosLibrary.meos.geometry_from_text(wkt, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer gserialized_as_ewkb(Pointer gs, String type) {
		return MeosLibrary.meos.gserialized_as_ewkb(gs, type);
	}
	
	@SuppressWarnings("unused")
	public static String gserialized_as_ewkt(Pointer gs, int precision) {
		return MeosLibrary.meos.gserialized_as_ewkt(gs, precision);
	}
	
	@SuppressWarnings("unused")
	public static String gserialized_as_geojson(Pointer gs, int option, int precision, String srs) {
		return MeosLibrary.meos.gserialized_as_geojson(gs, option, precision, srs);
	}
	
	@SuppressWarnings("unused")
	public static String gserialized_as_hexewkb(Pointer gs, String type) {
		return MeosLibrary.meos.gserialized_as_hexewkb(gs, type);
	}
	
	@SuppressWarnings("unused")
	public static String gserialized_as_text(Pointer gs, int precision) {
		return MeosLibrary.meos.gserialized_as_text(gs, precision);
	}
	
	@SuppressWarnings("unused")
	public static Pointer gserialized_from_ewkb(Pointer bytea_wkb, int srid) {
		return MeosLibrary.meos.gserialized_from_ewkb(bytea_wkb, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer gserialized_from_geojson(String geojson) {
		return MeosLibrary.meos.gserialized_from_geojson(geojson);
	}
	
	@SuppressWarnings("unused")
	public static String gserialized_out(Pointer gs) {
		return MeosLibrary.meos.gserialized_out(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pgis_geography_in(String input, int geom_typmod) {
		return MeosLibrary.meos.pgis_geography_in(input, geom_typmod);
	}
	
	@SuppressWarnings("unused")
	public static Pointer pgis_geometry_in(String input, int geom_typmod) {
		return MeosLibrary.meos.pgis_geometry_in(input, geom_typmod);
	}
	
	@SuppressWarnings("unused")
	public static boolean pgis_gserialized_same(Pointer gs1, Pointer gs2) {
		return MeosLibrary.meos.pgis_gserialized_same(gs1, gs2);
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
	public static Pointer geogset_in(String str) {
		return MeosLibrary.meos.geogset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geomset_in(String str) {
		return MeosLibrary.meos.geomset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String geoset_as_ewkt(Pointer set, int maxdd) {
		return MeosLibrary.meos.geoset_as_ewkt(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String geoset_as_text(Pointer set, int maxdd) {
		return MeosLibrary.meos.geoset_as_text(set, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String geoset_out(Pointer set, int maxdd) {
		return MeosLibrary.meos.geoset_out(set, maxdd);
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
	public static Pointer period_in(String str) {
		return MeosLibrary.meos.period_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String period_out(Pointer s) {
		return MeosLibrary.meos.period_out(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_in(String str) {
		return MeosLibrary.meos.periodset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String periodset_out(Pointer ss) {
		return MeosLibrary.meos.periodset_out(ss);
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
	public static Pointer textset_in(String str) {
		return MeosLibrary.meos.textset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String textset_out(Pointer set) {
		return MeosLibrary.meos.textset_out(set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestampset_in(String str) {
		return MeosLibrary.meos.timestampset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String timestampset_out(Pointer set) {
		return MeosLibrary.meos.timestampset_out(set);
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
	public static Pointer floatset_make(Pointer values, int count) {
		return MeosLibrary.meos.floatset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc) {
		return MeosLibrary.meos.floatspan_make(lower, upper, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_make(Pointer values, int count) {
		return MeosLibrary.meos.geoset_make(values, count);
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
	public static Pointer period_make(OffsetDateTime lower, OffsetDateTime upper, boolean lower_inc, boolean upper_inc) {
		var lower_new = lower.toEpochSecond();
		var upper_new = upper.toEpochSecond();
		return MeosLibrary.meos.period_make(lower_new, upper_new, lower_inc, upper_inc);
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
	public static Pointer spanset_copy(Pointer ps) {
		return MeosLibrary.meos.spanset_copy(ps);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_make(Pointer spans, int count, boolean normalize) {
		return MeosLibrary.meos.spanset_make(spans, count, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer textset_make(Pointer values, int count) {
		return MeosLibrary.meos.textset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestampset_make(Pointer values, int count) {
		return MeosLibrary.meos.timestampset_make(values, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_to_bigintset(long i) {
		return MeosLibrary.meos.bigint_to_bigintset(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_to_bigintspan(int i) {
		return MeosLibrary.meos.bigint_to_bigintspan(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_to_bigintspanset(int i) {
		return MeosLibrary.meos.bigint_to_bigintspanset(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_to_floatset(double d) {
		return MeosLibrary.meos.float_to_floatset(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_to_floatspan(double d) {
		return MeosLibrary.meos.float_to_floatspan(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_to_floatspanset(double d) {
		return MeosLibrary.meos.float_to_floatspanset(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_to_geoset(Pointer gs) {
		return MeosLibrary.meos.geo_to_geoset(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_to_intset(int i) {
		return MeosLibrary.meos.int_to_intset(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_to_intspan(int i) {
		return MeosLibrary.meos.int_to_intspan(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_to_intspanset(int i) {
		return MeosLibrary.meos.int_to_intspanset(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_to_spanset(Pointer s) {
		return MeosLibrary.meos.set_to_spanset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_to_spanset(Pointer s) {
		return MeosLibrary.meos.span_to_spanset(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer text_to_textset(Pointer txt) {
		return MeosLibrary.meos.text_to_textset(txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamp_to_period(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamp_to_period(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamp_to_periodset(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamp_to_periodset(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamp_to_tstzset(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamp_to_tstzset(t_new);
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
	public static int bigintspan_lower(Pointer s) {
		return MeosLibrary.meos.bigintspan_lower(s);
	}
	
	@SuppressWarnings("unused")
	public static int bigintspan_upper(Pointer s) {
		return MeosLibrary.meos.bigintspan_upper(s);
	}
	
	@SuppressWarnings("unused")
	public static int bigintspanset_lower(Pointer ss) {
		return MeosLibrary.meos.bigintspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static int bigintspanset_upper(Pointer ss) {
		return MeosLibrary.meos.bigintspanset_upper(ss);
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
	public static double floatspanset_lower(Pointer ss) {
		return MeosLibrary.meos.floatspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static double floatspanset_upper(Pointer ss) {
		return MeosLibrary.meos.floatspanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoset_end_value(Pointer s) {
		return MeosLibrary.meos.geoset_end_value(s);
	}
	
	@SuppressWarnings("unused")
	public static int geoset_srid(Pointer set) {
		return MeosLibrary.meos.geoset_srid(set);
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
	public static int intspanset_lower(Pointer ss) {
		return MeosLibrary.meos.intspanset_lower(ss);
	}
	
	@SuppressWarnings("unused")
	public static int intspanset_upper(Pointer ss) {
		return MeosLibrary.meos.intspanset_upper(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer period_duration(Pointer s) {
		return MeosLibrary.meos.period_duration(s);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime period_lower(Pointer p) {
		var result = MeosLibrary.meos.period_lower(p);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime period_upper(Pointer p) {
		var result = MeosLibrary.meos.period_upper(p);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_duration(Pointer ps, boolean boundspan) {
		return MeosLibrary.meos.periodset_duration(ps, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime periodset_end_timestamp(Pointer ps) {
		var result = MeosLibrary.meos.periodset_end_timestamp(ps);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime periodset_lower(Pointer ps) {
		var result = MeosLibrary.meos.periodset_lower(ps);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static int periodset_num_timestamps(Pointer ps) {
		return MeosLibrary.meos.periodset_num_timestamps(ps);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime periodset_start_timestamp(Pointer ps) {
		var result = MeosLibrary.meos.periodset_start_timestamp(ps);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_timestamp_n(Pointer ps, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.periodset_timestamp_n(ps, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_timestamps(Pointer ps, Pointer count) {
		return MeosLibrary.meos.periodset_timestamps(ps, count);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime periodset_upper(Pointer ps) {
		var result = MeosLibrary.meos.periodset_upper(ps);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static long set_hash(Pointer s) {
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
	public static long span_hash(Pointer s) {
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
	public static double span_width(Pointer s) {
		return MeosLibrary.meos.span_width(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_end_span(Pointer ss) {
		return MeosLibrary.meos.spanset_end_span(ss);
	}
	
	@SuppressWarnings("unused")
	public static long spanset_hash(Pointer ps) {
		return MeosLibrary.meos.spanset_hash(ps);
	}
	
	@SuppressWarnings("unused")
	public static long spanset_hash_extended(Pointer ps, long seed) {
		return MeosLibrary.meos.spanset_hash_extended(ps, seed);
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
	public static Pointer spanset_spans(Pointer ss) {
		return MeosLibrary.meos.spanset_spans(ss);
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
	public static double spanset_width(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.spanset_width(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spatialset_stbox(Pointer s) {
		return MeosLibrary.meos.spatialset_stbox(s);
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
	public static OffsetDateTime timestampset_end_timestamp(Pointer ts) {
		var result = MeosLibrary.meos.timestampset_end_timestamp(ts);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime timestampset_start_timestamp(Pointer ts) {
		var result = MeosLibrary.meos.timestampset_start_timestamp(ts);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestampset_timestamp_n(Pointer ts, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.timestampset_timestamp_n(ts, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestampset_values(Pointer ts) {
		return MeosLibrary.meos.timestampset_values(ts);
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
	public static Pointer floatset_round(Pointer s, int maxdd) {
		return MeosLibrary.meos.floatset_round(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatset_shift_scale(Pointer s, double shift, double width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.floatset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_intspan(Pointer s) {
		return MeosLibrary.meos.floatspan_intspan(s);
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
	public static Pointer floatspanset_intspanset(Pointer ss) {
		return MeosLibrary.meos.floatspanset_intspanset(ss);
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
	public static Pointer geoset_round(Pointer s, int maxdd) {
		return MeosLibrary.meos.geoset_round(s, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intset_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.intset_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_floatspan(Pointer s) {
		return MeosLibrary.meos.intspan_floatspan(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_shift_scale(Pointer s, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.intspan_shift_scale(s, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_floatspanset(Pointer ss) {
		return MeosLibrary.meos.intspanset_floatspanset(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspanset_shift_scale(Pointer ss, int shift, int width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.intspanset_shift_scale(ss, shift, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer period_shift_scale(Pointer p, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.period_shift_scale(p, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer period_tprecision(Pointer s, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.period_tprecision(s, duration, torigin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_shift_scale(Pointer ss, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.periodset_shift_scale(ss, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_tprecision(Pointer ss, Pointer duration, OffsetDateTime torigin) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.periodset_tprecision(ss, duration, torigin_new);
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
	public static OffsetDateTime timestamp_tprecision(OffsetDateTime t, Pointer duration, OffsetDateTime torigin) {
		var t_new = t.toEpochSecond();
		var torigin_new = torigin.toEpochSecond();
		var result = MeosLibrary.meos.timestamp_tprecision(t_new, duration, torigin_new);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestampset_shift_scale(Pointer ts, Pointer shift, Pointer duration) {
		return MeosLibrary.meos.timestampset_shift_scale(ts, shift, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_bigintset_bigint(Pointer s, long i) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_bigintset_bigint(s, i, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_bigintspan_bigint(Pointer s, long i) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_bigintspan_bigint(s, i, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_bigintspanset_bigint(Pointer ss, long i) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_bigintspanset_bigint(ss, i, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_floatset_float(Pointer s, double d) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_floatset_float(s, d, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_floatspan_float(Pointer s, double d) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_floatspan_float(s, d, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_floatspanset_float(Pointer ss, double d) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_floatspanset_float(ss, d, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_geoset_geo(Pointer s, Pointer gs) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_geoset_geo(s, gs, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_intset_int(Pointer s, int i) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_intset_int(s, i, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_intspan_int(Pointer s, int i) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_intspan_int(s, i, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_intspanset_int(Pointer ss, int i) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_intspanset_int(ss, i, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_period_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_period_timestamp(s, t_new, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_periodset_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_periodset_timestamp(ss, t_new, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.intersection_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.intersection_span_span(s1, s2);
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
	public static Pointer intersection_textset_text(Pointer s, Pointer txt) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_textset_text(s, txt, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_timestampset_timestamp(s, t_new, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_bigintset(long i, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_bigint_bigintset(i, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_bigintspan(long i, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_bigint_bigintspan(i, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigint_bigintspanset(long i, Pointer ss) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_bigint_bigintspanset(i, ss, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigintset_bigint(Pointer s, long i) {
		return MeosLibrary.meos.minus_bigintset_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.minus_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.minus_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_floatset(double d, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_float_floatset(d, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_floatspan(double d, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_float_floatspan(d, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_float_floatspanset(double d, Pointer ss) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_float_floatspanset(d, ss, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_floatset_float(Pointer s, double d) {
		return MeosLibrary.meos.minus_floatset_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.minus_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.minus_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_geo_geoset(Pointer gs, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_geo_geoset(gs, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_geoset_geo(Pointer s, Pointer gs) {
		return MeosLibrary.meos.minus_geoset_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_intset(int i, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_int_intset(i, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_intspan(int i, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_int_intspan(i, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_int_intspanset(int i, Pointer ss) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_int_intspanset(i, ss, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_intset_int(Pointer s, int i) {
		return MeosLibrary.meos.minus_intset_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.minus_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.minus_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_period_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_period_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_periodset_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_periodset_timestamp(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.minus_set_set(s1, s2);
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
	public static Pointer minus_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.minus_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.minus_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_text_textset(Pointer txt, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_text_textset(txt, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_textset_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.minus_textset_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamp_period(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_timestamp_period(t_new, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamp_periodset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_timestamp_periodset(t_new, ss, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestamp_timestampset(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_timestamp_timestampset(t_new, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.minus_timestampset_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigintset_bigint(Pointer s, long i) {
		return MeosLibrary.meos.union_bigintset_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.union_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.union_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_floatset_float(Pointer s, double d) {
		return MeosLibrary.meos.union_floatset_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.union_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.union_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_geoset_geo(Pointer s, Pointer gs) {
		return MeosLibrary.meos.union_geoset_geo(s, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_intset_int(Pointer s, int i) {
		return MeosLibrary.meos.union_intset_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.union_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.union_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_period_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_period_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_periodset_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_periodset_timestamp(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.union_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.union_span_span(s1, s2);
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
	public static Pointer union_textset_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.union_textset_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.union_timestampset_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.adjacent_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.adjacent_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.adjacent_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.adjacent_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.adjacent_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.adjacent_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_period_timestamp(Pointer p, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.adjacent_period_timestamp(p, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_periodset_timestamp(Pointer ps, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.adjacent_periodset_timestamp(ps, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.adjacent_span_span(s1, s2);
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
	public static boolean contained_bigint_bigintset(long i, Pointer s) {
		return MeosLibrary.meos.contained_bigint_bigintset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_bigint_bigintspan(long i, Pointer s) {
		return MeosLibrary.meos.contained_bigint_bigintspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_bigint_bigintspanset(long i, Pointer ss) {
		return MeosLibrary.meos.contained_bigint_bigintspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_floatset(double d, Pointer s) {
		return MeosLibrary.meos.contained_float_floatset(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_floatspan(double d, Pointer s) {
		return MeosLibrary.meos.contained_float_floatspan(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_float_floatspanset(double d, Pointer ss) {
		return MeosLibrary.meos.contained_float_floatspanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_intset(int i, Pointer s) {
		return MeosLibrary.meos.contained_int_intset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_intspan(int i, Pointer s) {
		return MeosLibrary.meos.contained_int_intspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_int_intspanset(int i, Pointer ss) {
		return MeosLibrary.meos.contained_int_intspanset(i, ss);
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
	public static boolean contained_text_textset(Pointer txt, Pointer s) {
		return MeosLibrary.meos.contained_text_textset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamp_period(OffsetDateTime t, Pointer p) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contained_timestamp_period(t_new, p);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamp_periodset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contained_timestamp_periodset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_timestamp_timestampset(OffsetDateTime t, Pointer ts) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contained_timestamp_timestampset(t_new, ts);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_bigintset_bigint(Pointer s, long i) {
		return MeosLibrary.meos.contains_bigintset_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.contains_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.contains_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_floatset_float(Pointer s, double d) {
		return MeosLibrary.meos.contains_floatset_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.contains_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.contains_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_intset_int(Pointer s, int i) {
		return MeosLibrary.meos.contains_intset_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.contains_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.contains_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_period_timestamp(Pointer p, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contains_period_timestamp(p, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_periodset_timestamp(Pointer ps, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contains_periodset_timestamp(ps, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.contains_set_set(s1, s2);
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
	public static boolean contains_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.contains_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.contains_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_textset_text(Pointer s, Pointer t) {
		return MeosLibrary.meos.contains_textset_text(s, t);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.contains_timestampset_timestamp(s, t_new);
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
	public static boolean overlaps_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.overlaps_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overlaps_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.overlaps_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_period_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_period_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_periodset_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_periodset_timestamp(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamp_period(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_timestamp_period(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamp_periodset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_timestamp_periodset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestamp_timestampset(OffsetDateTime t, Pointer ts) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_timestamp_timestampset(t_new, ts);
	}
	
	@SuppressWarnings("unused")
	public static boolean after_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.after_timestampset_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_period_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_period_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_periodset_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_periodset_timestamp(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamp_period(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_timestamp_period(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamp_periodset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_timestamp_periodset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestamp_timestampset(OffsetDateTime t, Pointer ts) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_timestamp_timestampset(t_new, ts);
	}
	
	@SuppressWarnings("unused")
	public static boolean before_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.before_timestampset_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_bigintset(long i, Pointer s) {
		return MeosLibrary.meos.left_bigint_bigintset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_bigintspan(long i, Pointer s) {
		return MeosLibrary.meos.left_bigint_bigintspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigint_bigintspanset(long i, Pointer ss) {
		return MeosLibrary.meos.left_bigint_bigintspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigintset_bigint(Pointer s, long i) {
		return MeosLibrary.meos.left_bigintset_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.left_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.left_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_floatset(double d, Pointer s) {
		return MeosLibrary.meos.left_float_floatset(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_floatspan(double d, Pointer s) {
		return MeosLibrary.meos.left_float_floatspan(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_float_floatspanset(double d, Pointer ss) {
		return MeosLibrary.meos.left_float_floatspanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_floatset_float(Pointer s, double d) {
		return MeosLibrary.meos.left_floatset_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.left_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.left_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_intset(int i, Pointer s) {
		return MeosLibrary.meos.left_int_intset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_intspan(int i, Pointer s) {
		return MeosLibrary.meos.left_int_intspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_int_intspanset(int i, Pointer ss) {
		return MeosLibrary.meos.left_int_intspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_intset_int(Pointer s, int i) {
		return MeosLibrary.meos.left_intset_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.left_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.left_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.left_set_set(s1, s2);
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
	public static boolean left_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.left_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.left_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_text_textset(Pointer txt, Pointer s) {
		return MeosLibrary.meos.left_text_textset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_textset_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.left_textset_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_period_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_period_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_periodset_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_periodset_timestamp(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamp_period(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_timestamp_period(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamp_periodset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_timestamp_periodset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestamp_timestampset(OffsetDateTime t, Pointer ts) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_timestamp_timestampset(t_new, ts);
	}
	
	@SuppressWarnings("unused")
	public static boolean overafter_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overafter_timestampset_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_period_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_period_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_periodset_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_periodset_timestamp(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamp_period(OffsetDateTime t, Pointer s) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_timestamp_period(t_new, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamp_periodset(OffsetDateTime t, Pointer ss) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_timestamp_periodset(t_new, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestamp_timestampset(OffsetDateTime t, Pointer ts) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_timestamp_timestampset(t_new, ts);
	}
	
	@SuppressWarnings("unused")
	public static boolean overbefore_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.overbefore_timestampset_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_bigintset(long i, Pointer s) {
		return MeosLibrary.meos.overleft_bigint_bigintset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_bigintspan(long i, Pointer s) {
		return MeosLibrary.meos.overleft_bigint_bigintspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigint_bigintspanset(long i, Pointer ss) {
		return MeosLibrary.meos.overleft_bigint_bigintspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigintset_bigint(Pointer s, long i) {
		return MeosLibrary.meos.overleft_bigintset_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.overleft_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.overleft_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_floatset(double d, Pointer s) {
		return MeosLibrary.meos.overleft_float_floatset(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_floatspan(double d, Pointer s) {
		return MeosLibrary.meos.overleft_float_floatspan(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_float_floatspanset(double d, Pointer ss) {
		return MeosLibrary.meos.overleft_float_floatspanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_floatset_float(Pointer s, double d) {
		return MeosLibrary.meos.overleft_floatset_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.overleft_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.overleft_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_intset(int i, Pointer s) {
		return MeosLibrary.meos.overleft_int_intset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_intspan(int i, Pointer s) {
		return MeosLibrary.meos.overleft_int_intspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_int_intspanset(int i, Pointer ss) {
		return MeosLibrary.meos.overleft_int_intspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_intset_int(Pointer s, int i) {
		return MeosLibrary.meos.overleft_intset_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.overleft_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.overleft_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.overleft_set_set(s1, s2);
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
	public static boolean overleft_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.overleft_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.overleft_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_text_textset(Pointer txt, Pointer s) {
		return MeosLibrary.meos.overleft_text_textset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_textset_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.overleft_textset_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_bigintset(long i, Pointer s) {
		return MeosLibrary.meos.overright_bigint_bigintset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_bigintspan(long i, Pointer s) {
		return MeosLibrary.meos.overright_bigint_bigintspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigint_bigintspanset(long i, Pointer ss) {
		return MeosLibrary.meos.overright_bigint_bigintspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigintset_bigint(Pointer s, long i) {
		return MeosLibrary.meos.overright_bigintset_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.overright_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.overright_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_floatset(double d, Pointer s) {
		return MeosLibrary.meos.overright_float_floatset(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_floatspan(double d, Pointer s) {
		return MeosLibrary.meos.overright_float_floatspan(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_float_floatspanset(double d, Pointer ss) {
		return MeosLibrary.meos.overright_float_floatspanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_floatset_float(Pointer s, double d) {
		return MeosLibrary.meos.overright_floatset_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.overright_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.overright_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_intset(int i, Pointer s) {
		return MeosLibrary.meos.overright_int_intset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_intspan(int i, Pointer s) {
		return MeosLibrary.meos.overright_int_intspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_int_intspanset(int i, Pointer ss) {
		return MeosLibrary.meos.overright_int_intspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_intset_int(Pointer s, int i) {
		return MeosLibrary.meos.overright_intset_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.overright_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.overright_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.overright_set_set(s1, s2);
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
	public static boolean overright_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.overright_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.overright_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_text_textset(Pointer txt, Pointer s) {
		return MeosLibrary.meos.overright_text_textset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_textset_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.overright_textset_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_bigintset(long i, Pointer s) {
		return MeosLibrary.meos.right_bigint_bigintset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_bigintspan(long i, Pointer s) {
		return MeosLibrary.meos.right_bigint_bigintspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigint_bigintspanset(long i, Pointer ss) {
		return MeosLibrary.meos.right_bigint_bigintspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigintset_bigint(Pointer s, long i) {
		return MeosLibrary.meos.right_bigintset_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.right_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.right_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_floatset(double d, Pointer s) {
		return MeosLibrary.meos.right_float_floatset(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_floatspan(double d, Pointer s) {
		return MeosLibrary.meos.right_float_floatspan(d, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_float_floatspanset(double d, Pointer ss) {
		return MeosLibrary.meos.right_float_floatspanset(d, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_floatset_float(Pointer s, double d) {
		return MeosLibrary.meos.right_floatset_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.right_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.right_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_intset(int i, Pointer s) {
		return MeosLibrary.meos.right_int_intset(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_intspan(int i, Pointer s) {
		return MeosLibrary.meos.right_int_intspan(i, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_int_intspanset(int i, Pointer ss) {
		return MeosLibrary.meos.right_int_intspanset(i, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_intset_int(Pointer s, int i) {
		return MeosLibrary.meos.right_intset_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.right_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.right_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.right_set_set(s1, s2);
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
	public static boolean right_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.right_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.right_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_text_textset(Pointer txt, Pointer s) {
		return MeosLibrary.meos.right_text_textset(txt, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_textset_text(Pointer s, Pointer txt) {
		return MeosLibrary.meos.right_textset_text(s, txt);
	}
	
	@SuppressWarnings("unused")
	public static double distance_bigintset_bigint(Pointer s, long i) {
		return MeosLibrary.meos.distance_bigintset_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_bigintspan_bigint(Pointer s, long i) {
		return MeosLibrary.meos.distance_bigintspan_bigint(s, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_bigintspanset_bigint(Pointer ss, long i) {
		return MeosLibrary.meos.distance_bigintspanset_bigint(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatset_float(Pointer s, double d) {
		return MeosLibrary.meos.distance_floatset_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatspan_float(Pointer s, double d) {
		return MeosLibrary.meos.distance_floatspan_float(s, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_floatspanset_float(Pointer ss, double d) {
		return MeosLibrary.meos.distance_floatspanset_float(ss, d);
	}
	
	@SuppressWarnings("unused")
	public static double distance_intset_int(Pointer s, int i) {
		return MeosLibrary.meos.distance_intset_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_intspan_int(Pointer s, int i) {
		return MeosLibrary.meos.distance_intspan_int(s, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_intspanset_int(Pointer ss, int i) {
		return MeosLibrary.meos.distance_intspanset_int(ss, i);
	}
	
	@SuppressWarnings("unused")
	public static double distance_period_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.distance_period_timestamp(s, t_new);
	}
	
	@SuppressWarnings("unused")
	public static double distance_periodset_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.distance_periodset_timestamp(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static double distance_set_set(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_set_set(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_span_span(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.distance_span_span(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_spanset_span(Pointer ss, Pointer s) {
		return MeosLibrary.meos.distance_spanset_span(ss, s);
	}
	
	@SuppressWarnings("unused")
	public static double distance_spanset_spanset(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.distance_spanset_spanset(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static double distance_timestampset_timestamp(Pointer s, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.distance_timestampset_timestamp(s, t_new);
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
	public static Pointer bigint_extent_transfn(Pointer s, long i) {
		return MeosLibrary.meos.bigint_extent_transfn(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer bigint_union_transfn(Pointer state, long i) {
		return MeosLibrary.meos.bigint_union_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_extent_transfn(Pointer s, double d) {
		return MeosLibrary.meos.float_extent_transfn(s, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_union_transfn(Pointer state, double d) {
		return MeosLibrary.meos.float_union_transfn(state, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_extent_transfn(Pointer s, int i) {
		return MeosLibrary.meos.int_extent_transfn(s, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_union_transfn(Pointer state, int i) {
		return MeosLibrary.meos.int_union_transfn(state, i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer period_tcount_transfn(Pointer state, Pointer p) {
		return MeosLibrary.meos.period_tcount_transfn(state, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_tcount_transfn(Pointer state, Pointer ps) {
		return MeosLibrary.meos.periodset_tcount_transfn(state, ps);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_extent_transfn(Pointer span, Pointer set) {
		return MeosLibrary.meos.set_extent_transfn(span, set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_union_finalfn(Pointer state) {
		return MeosLibrary.meos.set_union_finalfn(state);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_union_transfn(Pointer state, Pointer set) {
		return MeosLibrary.meos.set_union_transfn(state, set);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_extent_transfn(Pointer s1, Pointer s2) {
		return MeosLibrary.meos.span_extent_transfn(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_union_transfn(Pointer state, Pointer span) {
		return MeosLibrary.meos.span_union_transfn(state, span);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_extent_transfn(Pointer s, Pointer ss) {
		return MeosLibrary.meos.spanset_extent_transfn(s, ss);
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
	public static Pointer timestamp_extent_transfn(Pointer p, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamp_extent_transfn(p, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamp_tcount_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamp_tcount_transfn(state, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamp_union_transfn(Pointer state, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamp_union_transfn(state, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestampset_tcount_transfn(Pointer state, Pointer ts) {
		return MeosLibrary.meos.timestampset_tcount_transfn(state, ts);
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
	public static Pointer stbox_from_wkb(Pointer wkb, long size) {
		return MeosLibrary.meos.stbox_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_from_hexwkb(String hexwkb) {
		return MeosLibrary.meos.stbox_from_hexwkb(hexwkb);
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
	public static Pointer float_period_to_tbox(double d, Pointer p) {
		return MeosLibrary.meos.float_period_to_tbox(d, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer float_timestamp_to_tbox(double d, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.float_timestamp_to_tbox(d, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_period_to_stbox(Pointer gs, Pointer p) {
		return MeosLibrary.meos.geo_period_to_stbox(gs, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_timestamp_to_stbox(Pointer gs, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.geo_timestamp_to_stbox(gs, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_period_to_tbox(int i, Pointer p) {
		return MeosLibrary.meos.int_period_to_tbox(i, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_timestamp_to_tbox(int i, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.int_timestamp_to_tbox(i, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_period_to_tbox(Pointer span, Pointer p) {
		return MeosLibrary.meos.span_period_to_tbox(span, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_timestamp_to_tbox(Pointer span, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.span_timestamp_to_tbox(span, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_copy(Pointer box) {
		return MeosLibrary.meos.stbox_copy(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_make(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer p) {
		return MeosLibrary.meos.stbox_make(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, p);
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
	public static Pointer float_to_tbox(double d) {
		return MeosLibrary.meos.float_to_tbox(d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_to_stbox(Pointer gs) {
		return MeosLibrary.meos.geo_to_stbox(gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer int_to_tbox(int i) {
		return MeosLibrary.meos.int_to_tbox(i);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numset_to_tbox(Pointer s) {
		return MeosLibrary.meos.numset_to_tbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspan_to_tbox(Pointer s) {
		return MeosLibrary.meos.numspan_to_tbox(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer numspanset_to_tbox(Pointer ss) {
		return MeosLibrary.meos.numspanset_to_tbox(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer period_to_stbox(Pointer p) {
		return MeosLibrary.meos.period_to_stbox(p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer period_to_tbox(Pointer p) {
		return MeosLibrary.meos.period_to_tbox(p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_to_stbox(Pointer ps) {
		return MeosLibrary.meos.periodset_to_stbox(ps);
	}
	
	@SuppressWarnings("unused")
	public static Pointer periodset_to_tbox(Pointer ps) {
		return MeosLibrary.meos.periodset_to_tbox(ps);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_to_geo(Pointer box) {
		return MeosLibrary.meos.stbox_to_geo(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_to_period(Pointer box) {
		return MeosLibrary.meos.stbox_to_period(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_to_floatspan(Pointer box) {
		return MeosLibrary.meos.tbox_to_floatspan(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_to_period(Pointer box) {
		return MeosLibrary.meos.tbox_to_period(box);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamp_to_stbox(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamp_to_stbox(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestamp_to_tbox(OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.timestamp_to_tbox(t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestampset_to_stbox(Pointer ts) {
		return MeosLibrary.meos.timestampset_to_stbox(ts);
	}
	
	@SuppressWarnings("unused")
	public static Pointer timestampset_to_tbox(Pointer ss) {
		return MeosLibrary.meos.timestampset_to_tbox(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_to_tbox(Pointer temp) {
		return MeosLibrary.meos.tnumber_to_tbox(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_to_stbox(Pointer temp) {
		return MeosLibrary.meos.tpoint_to_stbox(temp);
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
	public static Pointer stbox_expand_space(Pointer box, double d) {
		return MeosLibrary.meos.stbox_expand_space(box, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_expand_time(Pointer box, Pointer interval) {
		return MeosLibrary.meos.stbox_expand_time(box, interval);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_get_space(Pointer box) {
		return MeosLibrary.meos.stbox_get_space(box);
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
	public static Pointer tbox_expand_time(Pointer box, Pointer interval) {
		return MeosLibrary.meos.tbox_expand_time(box, interval);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tbox_expand_value(Pointer box, double d) {
		return MeosLibrary.meos.tbox_expand_value(box, d);
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
	public static Pointer inter_tbox_tbox(Pointer box1, Pointer box2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.inter_tbox_tbox(box1, box2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.intersection_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict) {
		return MeosLibrary.meos.union_stbox_stbox(box1, box2, strict);
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
	public static Pointer intersection_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.intersection_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.contains_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.contained_tbox_tbox(box1, box2);
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
	public static boolean adjacent_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.adjacent_tbox_tbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.contains_stbox_stbox(box1, box2);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.contained_stbox_stbox(box1, box2);
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
	public static boolean adjacent_stbox_stbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.adjacent_stbox_stbox(box1, box2);
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
	public static Pointer stbox_quad_split(Pointer box, Pointer count) {
		return MeosLibrary.meos.stbox_quad_split(box, count);
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
	public static Pointer tbool_in(String str) {
		return MeosLibrary.meos.tbool_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tbool_out(Pointer temp) {
		return MeosLibrary.meos.tbool_out(temp);
	}
	
	@SuppressWarnings("unused")
	public static String temporal_as_hexwkb(Pointer temp, byte variant) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);
		return MeosLibrary.meos.temporal_as_hexwkb(temp, variant, size_out);
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
	public static Pointer temporal_from_hexwkb(String hexwkb) {
		return MeosLibrary.meos.temporal_from_hexwkb(hexwkb);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_from_mfjson(String mfjson) {
		return MeosLibrary.meos.temporal_from_mfjson(mfjson);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_from_wkb(Pointer wkb, long size) {
		return MeosLibrary.meos.temporal_from_wkb(wkb, size);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_in(String str) {
		return MeosLibrary.meos.tfloat_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tfloat_out(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tfloat_out(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_in(String str) {
		return MeosLibrary.meos.tgeogpoint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_in(String str) {
		return MeosLibrary.meos.tgeompoint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_in(String str) {
		return MeosLibrary.meos.tint_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tint_out(Pointer temp) {
		return MeosLibrary.meos.tint_out(temp);
	}
	
	@SuppressWarnings("unused")
	public static String tpoint_as_ewkt(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tpoint_as_ewkt(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tpoint_as_text(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tpoint_as_text(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tpoint_out(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tpoint_out(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttext_in(String str) {
		return MeosLibrary.meos.ttext_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String ttext_out(Pointer temp) {
		return MeosLibrary.meos.ttext_out(temp);
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
	public static Pointer tboolseq_from_base_period(boolean b, Pointer p) {
		return MeosLibrary.meos.tboolseq_from_base_period(b, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseq_from_base_timestampset(boolean b, Pointer ts) {
		return MeosLibrary.meos.tboolseq_from_base_timestampset(b, ts);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tboolseqset_from_base_periodset(boolean b, Pointer ps) {
		return MeosLibrary.meos.tboolseqset_from_base_periodset(b, ps);
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
	public static Pointer tfloatseq_from_base_period(double d, Pointer p, int interp) {
		return MeosLibrary.meos.tfloatseq_from_base_period(d, p, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseq_from_base_timestampset(double d, Pointer ts) {
		return MeosLibrary.meos.tfloatseq_from_base_timestampset(d, ts);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_from_base_periodset(double d, Pointer ps, int interp) {
		return MeosLibrary.meos.tfloatseqset_from_base_periodset(d, ps, interp);
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
	public static Pointer tintseq_from_base_period(int i, Pointer p) {
		return MeosLibrary.meos.tintseq_from_base_period(i, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseq_from_base_timestampset(int i, Pointer ts) {
		return MeosLibrary.meos.tintseq_from_base_timestampset(i, ts);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintseqset_from_base_periodset(int i, Pointer ps) {
		return MeosLibrary.meos.tintseqset_from_base_periodset(i, ps);
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
	public static Pointer tpointseq_from_base_period(Pointer gs, Pointer p, int interp) {
		return MeosLibrary.meos.tpointseq_from_base_period(gs, p, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_from_base_timestampset(Pointer gs, Pointer ts) {
		return MeosLibrary.meos.tpointseq_from_base_timestampset(gs, ts);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_from_base_periodset(Pointer gs, Pointer ps, int interp) {
		return MeosLibrary.meos.tpointseqset_from_base_periodset(gs, ps, interp);
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
	public static Pointer ttextseq_from_base_period(Pointer txt, Pointer p) {
		return MeosLibrary.meos.ttextseq_from_base_period(txt, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseq_from_base_timestampset(Pointer txt, Pointer ts) {
		return MeosLibrary.meos.ttextseq_from_base_timestampset(txt, ts);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttextseqset_from_base_periodset(Pointer txt, Pointer ps) {
		return MeosLibrary.meos.ttextseqset_from_base_periodset(txt, ps);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_period(Pointer temp) {
		return MeosLibrary.meos.temporal_to_period(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_to_tint(Pointer temp) {
		return MeosLibrary.meos.tfloat_to_tint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_to_tfloat(Pointer temp) {
		return MeosLibrary.meos.tint_to_tfloat(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_to_span(Pointer temp) {
		return MeosLibrary.meos.tnumber_to_span(temp);
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
	public static OffsetDateTime temporal_end_timestamp(Pointer temp) {
		var result = MeosLibrary.meos.temporal_end_timestamp(temp);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static long temporal_hash(Pointer temp) {
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
	public static Pointer temporal_start_instant(Pointer temp) {
		return MeosLibrary.meos.temporal_start_instant(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_start_sequence(Pointer temp) {
		return MeosLibrary.meos.temporal_start_sequence(temp);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime temporal_start_timestamp(Pointer temp) {
		var result = MeosLibrary.meos.temporal_start_timestamp(temp);
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
	public static Pointer temporal_timestamp_n(Pointer temp, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.temporal_timestamp_n(temp, n, result);
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
	public static Pointer tint_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tint_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumber_valuespans(Pointer temp) {
		return MeosLibrary.meos.tnumber_valuespans(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_end_value(Pointer temp) {
		return MeosLibrary.meos.tpoint_end_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_start_value(Pointer temp) {
		return MeosLibrary.meos.tpoint_start_value(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tpoint_values(temp, count);
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
	public static Pointer ttext_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.ttext_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_scale_time(Pointer temp, Pointer duration) {
		return MeosLibrary.meos.temporal_scale_time(temp, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_set_interp(Pointer temp, int interp) {
		return MeosLibrary.meos.temporal_set_interp(temp, interp);
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
	public static Pointer temporal_to_tsequence(Pointer temp, int interp) {
		return MeosLibrary.meos.temporal_to_tsequence(temp, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_to_tsequenceset(Pointer temp, int interp) {
		return MeosLibrary.meos.temporal_to_tsequenceset(temp, interp);
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
	public static Pointer temporal_append_tinstant(Pointer temp, Pointer inst, double maxdist, Pointer maxt, boolean expand) {
		return MeosLibrary.meos.temporal_append_tinstant(temp, inst, maxdist, maxt, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_append_tsequence(Pointer temp, Pointer seq, boolean expand) {
		return MeosLibrary.meos.temporal_append_tsequence(temp, seq, expand);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_period(Pointer temp, Pointer p, boolean connect) {
		return MeosLibrary.meos.temporal_delete_period(temp, p, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_periodset(Pointer temp, Pointer ps, boolean connect) {
		return MeosLibrary.meos.temporal_delete_periodset(temp, ps, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_timestamp(Pointer temp, OffsetDateTime t, boolean connect) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.temporal_delete_timestamp(temp, t_new, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_delete_timestampset(Pointer temp, Pointer ts, boolean connect) {
		return MeosLibrary.meos.temporal_delete_timestampset(temp, ts, connect);
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
	public static boolean tbool_value_at_timestamp(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tbool_value_at_timestamp(temp, t_new, strict, value);
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
	public static Pointer temporal_at_period(Pointer temp, Pointer p) {
		return MeosLibrary.meos.temporal_at_period(temp, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_periodset(Pointer temp, Pointer ps) {
		return MeosLibrary.meos.temporal_at_periodset(temp, ps);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_timestamp(Pointer temp, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.temporal_at_timestamp(temp, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_at_timestampset(Pointer temp, Pointer ts) {
		return MeosLibrary.meos.temporal_at_timestampset(temp, ts);
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
	public static Pointer temporal_minus_period(Pointer temp, Pointer p) {
		return MeosLibrary.meos.temporal_minus_period(temp, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_periodset(Pointer temp, Pointer ps) {
		return MeosLibrary.meos.temporal_minus_periodset(temp, ps);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_timestamp(Pointer temp, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.temporal_minus_timestamp(temp, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_minus_timestampset(Pointer temp, Pointer ts) {
		return MeosLibrary.meos.temporal_minus_timestampset(temp, ts);
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
	public static boolean tfloat_value_at_timestamp(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tfloat_value_at_timestamp(temp, t_new, strict, value);
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
	public static boolean tint_value_at_timestamp(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tint_value_at_timestamp(temp, t_new, strict, value);
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
	public static Pointer tpoint_at_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period) {
		return MeosLibrary.meos.tpoint_at_geom_time(temp, gs, zspan, period);
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
	public static Pointer tpoint_minus_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period) {
		return MeosLibrary.meos.tpoint_minus_geom_time(temp, gs, zspan, period);
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
	public static boolean tpoint_value_at_timestamp(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tpoint_value_at_timestamp(temp, t_new, strict, value);
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
	public static boolean ttext_value_at_timestamp(Pointer temp, OffsetDateTime t, boolean strict, Pointer value) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.ttext_value_at_timestamp(temp, t_new, strict, value);
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
	public static boolean tbool_always_eq(Pointer temp, boolean b) {
		return MeosLibrary.meos.tbool_always_eq(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static boolean tbool_ever_eq(Pointer temp, boolean b) {
		return MeosLibrary.meos.tbool_ever_eq(temp, b);
	}
	
	@SuppressWarnings("unused")
	public static boolean tfloat_always_eq(Pointer temp, double d) {
		return MeosLibrary.meos.tfloat_always_eq(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean tfloat_always_le(Pointer temp, double d) {
		return MeosLibrary.meos.tfloat_always_le(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean tfloat_always_lt(Pointer temp, double d) {
		return MeosLibrary.meos.tfloat_always_lt(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean tfloat_ever_eq(Pointer temp, double d) {
		return MeosLibrary.meos.tfloat_ever_eq(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean tfloat_ever_le(Pointer temp, double d) {
		return MeosLibrary.meos.tfloat_ever_le(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean tfloat_ever_lt(Pointer temp, double d) {
		return MeosLibrary.meos.tfloat_ever_lt(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static boolean tint_always_eq(Pointer temp, int i) {
		return MeosLibrary.meos.tint_always_eq(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean tint_always_le(Pointer temp, int i) {
		return MeosLibrary.meos.tint_always_le(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean tint_always_lt(Pointer temp, int i) {
		return MeosLibrary.meos.tint_always_lt(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean tint_ever_eq(Pointer temp, int i) {
		return MeosLibrary.meos.tint_ever_eq(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean tint_ever_le(Pointer temp, int i) {
		return MeosLibrary.meos.tint_ever_le(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean tint_ever_lt(Pointer temp, int i) {
		return MeosLibrary.meos.tint_ever_lt(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpoint_always_eq(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.tpoint_always_eq(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpoint_ever_eq(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.tpoint_ever_eq(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static boolean ttext_always_eq(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ttext_always_eq(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean ttext_always_le(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ttext_always_le(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean ttext_always_lt(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ttext_always_lt(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean ttext_ever_eq(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ttext_ever_eq(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean ttext_ever_le(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ttext_ever_le(temp, txt);
	}
	
	@SuppressWarnings("unused")
	public static boolean ttext_ever_lt(Pointer temp, Pointer txt) {
		return MeosLibrary.meos.ttext_ever_lt(temp, txt);
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
	public static Pointer teq_point_tpoint(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.teq_point_tpoint(gs, temp);
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
	public static Pointer teq_tpoint_point(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.teq_tpoint_point(temp, gs);
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
	public static Pointer tne_point_tpoint(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.tne_point_tpoint(gs, temp);
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
	public static Pointer tne_tpoint_point(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.tne_tpoint_point(temp, gs);
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
	public static double float_degrees(double value, boolean normalize) {
		return MeosLibrary.meos.float_degrees(value, normalize);
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
	public static Pointer tfloat_round(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tfloat_round(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatarr_round(Pointer temp, int count, int maxdd) {
		return MeosLibrary.meos.tfloatarr_round(temp, count, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_degrees(Pointer temp, boolean normalize) {
		return MeosLibrary.meos.tfloat_degrees(temp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_derivative(Pointer temp) {
		return MeosLibrary.meos.tfloat_derivative(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_radians(Pointer temp) {
		return MeosLibrary.meos.tfloat_radians(temp);
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
	public static Pointer distance_tpoint_point(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.distance_tpoint_point(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.distance_tpoint_tpoint(temp1, temp2);
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
	public static double nad_tbox_tbox(Pointer box1, Pointer box2) {
		return MeosLibrary.meos.nad_tbox_tbox(box1, box2);
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
	public static int nad_tint_int(Pointer temp, int i) {
		return MeosLibrary.meos.nad_tint_int(temp, i);
	}
	
	@SuppressWarnings("unused")
	public static int nad_tint_tint(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.nad_tint_tint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tnumber_tbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.nad_tnumber_tbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tpoint_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.nad_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tpoint_stbox(Pointer temp, Pointer box) {
		return MeosLibrary.meos.nad_tpoint_stbox(temp, box);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.nad_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer nai_tpoint_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.nai_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer nai_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.nai_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer shortestline_tpoint_geo(Pointer temp, Pointer gs) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.shortestline_tpoint_geo(temp, gs, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer shortestline_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.shortestline_tpoint_tpoint(temp1, temp2, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
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
	public static Pointer tpoint_angular_difference(Pointer temp) {
		return MeosLibrary.meos.tpoint_angular_difference(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_azimuth(Pointer temp) {
		return MeosLibrary.meos.tpoint_azimuth(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_convex_hull(Pointer temp) {
		return MeosLibrary.meos.tpoint_convex_hull(temp);
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
	public static int tpoint_srid(Pointer temp) {
		return MeosLibrary.meos.tpoint_srid(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_stboxes(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tpoint_stboxes(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_trajectory(Pointer temp) {
		return MeosLibrary.meos.tpoint_trajectory(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer geo_expand_space(Pointer gs, double d) {
		return MeosLibrary.meos.geo_expand_space(gs, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpoint_to_tgeompoint(Pointer temp) {
		return MeosLibrary.meos.tgeogpoint_to_tgeompoint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_to_tgeogpoint(Pointer temp) {
		return MeosLibrary.meos.tgeompoint_to_tgeogpoint(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_expand_space(Pointer temp, double d) {
		return MeosLibrary.meos.tpoint_expand_space(temp, d);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_make_simple(Pointer temp, Pointer count) {
		return MeosLibrary.meos.tpoint_make_simple(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_round(Pointer temp, int maxdd) {
		return MeosLibrary.meos.tpoint_round(temp, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointarr_round(Pointer temp, int count, int maxdd) {
		return MeosLibrary.meos.tpointarr_round(temp, count, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_set_srid(Pointer temp, int srid) {
		return MeosLibrary.meos.tpoint_set_srid(temp, srid);
	}
	
	@SuppressWarnings("unused")
	public static int econtains_geo_tpoint(Pointer gs, Pointer temp) {
		return MeosLibrary.meos.econtains_geo_tpoint(gs, temp);
	}
	
	@SuppressWarnings("unused")
	public static int edisjoint_tpoint_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.edisjoint_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int edisjoint_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.edisjoint_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int edwithin_tpoint_geo(Pointer temp, Pointer gs, double dist) {
		return MeosLibrary.meos.edwithin_tpoint_geo(temp, gs, dist);
	}
	
	@SuppressWarnings("unused")
	public static int edwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist) {
		return MeosLibrary.meos.edwithin_tpoint_tpoint(temp1, temp2, dist);
	}
	
	@SuppressWarnings("unused")
	public static int eintersects_tpoint_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.eintersects_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static int eintersects_tpoint_tpoint(Pointer temp1, Pointer temp2) {
		return MeosLibrary.meos.eintersects_tpoint_tpoint(temp1, temp2);
	}
	
	@SuppressWarnings("unused")
	public static int etouches_tpoint_geo(Pointer temp, Pointer gs) {
		return MeosLibrary.meos.etouches_tpoint_geo(temp, gs);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tcontains_geo_tpoint(Pointer gs, Pointer temp, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tcontains_geo_tpoint(gs, temp, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdisjoint_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tdisjoint_tpoint_geo(temp, gs, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdwithin_tpoint_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tdwithin_tpoint_geo(temp, gs, dist, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tdwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tdwithin_tpoint_tpoint(temp1, temp2, dist, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintersects_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.tintersects_tpoint_geo(temp, gs, restr, atvalue);
	}
	
	@SuppressWarnings("unused")
	public static Pointer ttouches_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue) {
		return MeosLibrary.meos.ttouches_tpoint_geo(temp, gs, restr, atvalue);
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
	public static Pointer temporal_extent_transfn(Pointer p, Pointer temp) {
		return MeosLibrary.meos.temporal_extent_transfn(p, temp);
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
	public static Pointer tnumber_extent_transfn(Pointer box, Pointer temp) {
		return MeosLibrary.meos.tnumber_extent_transfn(box, temp);
	}
	
	@SuppressWarnings("unused")
	public static double tnumber_integral(Pointer temp) {
		return MeosLibrary.meos.tnumber_integral(temp);
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
	public static double tnumber_twavg(Pointer temp) {
		return MeosLibrary.meos.tnumber_twavg(temp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_extent_transfn(Pointer box, Pointer temp) {
		return MeosLibrary.meos.tpoint_extent_transfn(box, temp);
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
	public static Pointer tpoint_twcentroid(Pointer temp) {
		return MeosLibrary.meos.tpoint_twcentroid(temp);
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
	public static Pointer temporal_simplify_min_dist(Pointer temp, double dist) {
		return MeosLibrary.meos.temporal_simplify_min_dist(temp, dist);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_simplify_min_tdelta(Pointer temp, Pointer mint) {
		return MeosLibrary.meos.temporal_simplify_min_tdelta(temp, mint);
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
	public static Pointer temporal_tprecision(Pointer temp, Pointer duration, OffsetDateTime origin) {
		var origin_new = origin.toEpochSecond();
		return MeosLibrary.meos.temporal_tprecision(temp, duration, origin_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_tsample(Pointer temp, Pointer duration, OffsetDateTime origin) {
		var origin_new = origin.toEpochSecond();
		return MeosLibrary.meos.temporal_tsample(temp, duration, origin_new);
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
	public static double float_bucket(double value, double size, double origin) {
		return MeosLibrary.meos.float_bucket(value, size, origin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer floatspan_bucket_list(Pointer bounds, double size, double origin, Pointer count) {
		return MeosLibrary.meos.floatspan_bucket_list(bounds, size, origin, count);
	}
	
	@SuppressWarnings("unused")
	public static int int_bucket(int value, int size, int origin) {
		return MeosLibrary.meos.int_bucket(value, size, origin);
	}
	
	@SuppressWarnings("unused")
	public static Pointer intspan_bucket_list(Pointer bounds, int size, int origin, Pointer count) {
		return MeosLibrary.meos.intspan_bucket_list(bounds, size, origin, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer period_bucket_list(Pointer bounds, Pointer duration, OffsetDateTime origin, Pointer count) {
		var origin_new = origin.toEpochSecond();
		return MeosLibrary.meos.period_bucket_list(bounds, duration, origin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer stbox_tile_list(Pointer bounds, double xsize, double ysize, double zsize, Pointer duration, Pointer sorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.stbox_tile_list(bounds, xsize, ysize, zsize, duration, sorigin, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tintbox_tile_list(Pointer box, int xsize, Pointer duration, int xorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tintbox_tile_list(box, xsize, duration, xorigin, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatbox_tile_list(Pointer box, double xsize, Pointer duration, double xorigin, OffsetDateTime torigin, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tfloatbox_tile_list(box, xsize, duration, xorigin, torigin_new, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_time_split(Pointer temp, Pointer duration, OffsetDateTime torigin, Pointer time_buckets, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.temporal_time_split(temp, duration, torigin_new, time_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_split(Pointer temp, double size, double origin, Pointer value_buckets, Pointer count) {
		return MeosLibrary.meos.tfloat_value_split(temp, size, origin, value_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloat_value_time_split(Pointer temp, double size, Pointer duration, double vorigin, OffsetDateTime torigin, Pointer value_buckets, Pointer time_buckets, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tfloat_value_time_split(temp, size, duration, vorigin, torigin_new, value_buckets, time_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime timestamptz_bucket(OffsetDateTime timestamp, Pointer duration, OffsetDateTime origin) {
		var timestamp_new = timestamp.toEpochSecond();
		var origin_new = origin.toEpochSecond();
		var result = MeosLibrary.meos.timestamptz_bucket(timestamp_new, duration, origin_new);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_split(Pointer temp, int size, int origin, Pointer value_buckets, Pointer count) {
		return MeosLibrary.meos.tint_value_split(temp, size, origin, value_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tint_value_time_split(Pointer temp, int size, Pointer duration, int vorigin, OffsetDateTime torigin, Pointer value_buckets, Pointer time_buckets, Pointer count) {
		var torigin_new = torigin.toEpochSecond();
		return MeosLibrary.meos.tint_value_time_split(temp, size, duration, vorigin, torigin_new, value_buckets, time_buckets, count);
	}
	
	@SuppressWarnings("unused")
	public static String meostype_name(meosType temptype) {
		return MeosLibrary.meos.meostype_name(temptype);
	}
	
	@SuppressWarnings("unused")
	public static meosType temptype_basetype(meosType temptype) {
		return MeosLibrary.meos.temptype_basetype(temptype);
	}
	
	@SuppressWarnings("unused")
	public static meosType settype_basetype(meosType settype) {
		return MeosLibrary.meos.settype_basetype(settype);
	}
	
	@SuppressWarnings("unused")
	public static meosType spantype_basetype(meosType spantype) {
		return MeosLibrary.meos.spantype_basetype(spantype);
	}
	
	@SuppressWarnings("unused")
	public static meosType spantype_spansettype(meosType spantype) {
		return MeosLibrary.meos.spantype_spansettype(spantype);
	}
	
	@SuppressWarnings("unused")
	public static meosType spansettype_spantype(meosType spansettype) {
		return MeosLibrary.meos.spansettype_spantype(spansettype);
	}
	
	@SuppressWarnings("unused")
	public static meosType basetype_spantype(meosType basetype) {
		return MeosLibrary.meos.basetype_spantype(basetype);
	}
	
	@SuppressWarnings("unused")
	public static meosType basetype_settype(meosType basetype) {
		return MeosLibrary.meos.basetype_settype(basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean geo_basetype(meosType basetype) {
		return MeosLibrary.meos.geo_basetype(basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean spatial_basetype(meosType basetype) {
		return MeosLibrary.meos.spatial_basetype(basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean time_type(meosType timetype) {
		return MeosLibrary.meos.time_type(timetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_type(meosType type) {
		return MeosLibrary.meos.set_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean numset_type(meosType type) {
		return MeosLibrary.meos.numset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_numset_type(meosType type) {
		return MeosLibrary.meos.ensure_numset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean timeset_type(meosType type) {
		return MeosLibrary.meos.timeset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean set_spantype(meosType type) {
		return MeosLibrary.meos.set_spantype(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_set_spantype(meosType type) {
		return MeosLibrary.meos.ensure_set_spantype(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean alphanumset_type(meosType settype) {
		return MeosLibrary.meos.alphanumset_type(settype);
	}
	
	@SuppressWarnings("unused")
	public static boolean geoset_type(meosType type) {
		return MeosLibrary.meos.geoset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_geoset_type(meosType type) {
		return MeosLibrary.meos.ensure_geoset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean spatialset_type(meosType type) {
		return MeosLibrary.meos.spatialset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_spatialset_type(meosType type) {
		return MeosLibrary.meos.ensure_spatialset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_basetype(meosType type) {
		return MeosLibrary.meos.span_basetype(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_canon_basetype(meosType type) {
		return MeosLibrary.meos.span_canon_basetype(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean span_type(meosType type) {
		return MeosLibrary.meos.span_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean numspan_basetype(meosType type) {
		return MeosLibrary.meos.numspan_basetype(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean numspan_type(meosType type) {
		return MeosLibrary.meos.numspan_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_numspan_type(meosType type) {
		return MeosLibrary.meos.ensure_numspan_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean timespan_basetype(meosType type) {
		return MeosLibrary.meos.timespan_basetype(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean timespan_type(meosType type) {
		return MeosLibrary.meos.timespan_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean spanset_type(meosType type) {
		return MeosLibrary.meos.spanset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean timespanset_type(meosType type) {
		return MeosLibrary.meos.timespanset_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_type(meosType temptype) {
		return MeosLibrary.meos.temporal_type(temptype);
	}
	
	@SuppressWarnings("unused")
	public static boolean temptype_continuous(meosType temptype) {
		return MeosLibrary.meos.temptype_continuous(temptype);
	}
	
	@SuppressWarnings("unused")
	public static boolean basetype_byvalue(meosType type) {
		return MeosLibrary.meos.basetype_byvalue(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean basetype_varlength(meosType type) {
		return MeosLibrary.meos.basetype_varlength(type);
	}
	
	@SuppressWarnings("unused")
	public static short basetype_length(meosType basetype) {
		return MeosLibrary.meos.basetype_length(basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean talpha_type(meosType temptype) {
		return MeosLibrary.meos.talpha_type(temptype);
	}
	
	@SuppressWarnings("unused")
	public static boolean tnumber_type(meosType temptype) {
		return MeosLibrary.meos.tnumber_type(temptype);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_tnumber_type(meosType temptype) {
		return MeosLibrary.meos.ensure_tnumber_type(temptype);
	}
	
	@SuppressWarnings("unused")
	public static boolean tnumber_basetype(meosType basetype) {
		return MeosLibrary.meos.tnumber_basetype(basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean tnumber_spantype(meosType settype) {
		return MeosLibrary.meos.tnumber_spantype(settype);
	}
	
	@SuppressWarnings("unused")
	public static boolean tnumber_spansettype(meosType spansettype) {
		return MeosLibrary.meos.tnumber_spansettype(spansettype);
	}
	
	@SuppressWarnings("unused")
	public static boolean tspatial_type(meosType temptype) {
		return MeosLibrary.meos.tspatial_type(temptype);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_tspatial_type(meosType temptype) {
		return MeosLibrary.meos.ensure_tspatial_type(temptype);
	}
	
	@SuppressWarnings("unused")
	public static boolean tspatial_basetype(meosType basetype) {
		return MeosLibrary.meos.tspatial_basetype(basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean tgeo_type(meosType type) {
		return MeosLibrary.meos.tgeo_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_tgeo_type(meosType type) {
		return MeosLibrary.meos.ensure_tgeo_type(type);
	}
	
	@SuppressWarnings("unused")
	public static boolean ensure_tnumber_tgeo_type(meosType type) {
		return MeosLibrary.meos.ensure_tnumber_tgeo_type(type);
	}
	
	@SuppressWarnings("unused")
	public static long datum_hash(long d, meosType basetype) {
		return MeosLibrary.meos.datum_hash(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static long datum_hash_extended(long d, meosType basetype, long seed) {
		return MeosLibrary.meos.datum_hash_extended(d, basetype, seed);
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
	public static Pointer set_compact(Pointer s) {
		return MeosLibrary.meos.set_compact(s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_make(Pointer values, int count, meosType basetype, boolean ordered) {
		return MeosLibrary.meos.set_make(values, count, basetype, ordered);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_make_exp(Pointer values, int count, int maxcount, meosType basetype, boolean ordered) {
		return MeosLibrary.meos.set_make_exp(values, count, maxcount, basetype, ordered);
	}
	
	@SuppressWarnings("unused")
	public static Pointer set_make_free(Pointer values, int count, meosType basetype, boolean ordered) {
		return MeosLibrary.meos.set_make_free(values, count, basetype, ordered);
	}
	
	@SuppressWarnings("unused")
	public static Pointer span_make(long lower, long upper, boolean lower_inc, boolean upper_inc, meosType basetype) {
		return MeosLibrary.meos.span_make(lower, upper, lower_inc, upper_inc, basetype);
	}
	
	@SuppressWarnings("unused")
	public static void span_set(long lower, long upper, boolean lower_inc, boolean upper_inc, meosType basetype, Pointer s) {
		MeosLibrary.meos.span_set(lower, upper, lower_inc, upper_inc, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_compact(Pointer ss) {
		return MeosLibrary.meos.spanset_compact(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_make_exp(Pointer spans, int count, int maxcount, boolean normalize, boolean ordered) {
		return MeosLibrary.meos.spanset_make_exp(spans, count, maxcount, normalize, ordered);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanset_make_free(Pointer spans, int count, boolean normalize) {
		return MeosLibrary.meos.spanset_make_free(spans, count, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer value_to_set(long d, meosType basetype) {
		return MeosLibrary.meos.value_to_set(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer value_to_span(long d, meosType basetype) {
		return MeosLibrary.meos.value_to_span(d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer value_to_spanset(long d, meosType basetype) {
		return MeosLibrary.meos.value_to_spanset(d, basetype);
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
	public static void set_set_span(Pointer os, Pointer s) {
		MeosLibrary.meos.set_set_span(os, s);
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
	public static Pointer set_values(Pointer s) {
		return MeosLibrary.meos.set_values(s);
	}
	
	@SuppressWarnings("unused")
	public static int spanset_mem_size(Pointer ss) {
		return MeosLibrary.meos.spanset_mem_size(ss);
	}
	
	@SuppressWarnings("unused")
	public static void spatialset_set_stbox(Pointer set, Pointer box) {
		MeosLibrary.meos.spatialset_set_stbox(set, box);
	}
	
	@SuppressWarnings("unused")
	public static void value_set_span(long d, meosType basetype, Pointer s) {
		MeosLibrary.meos.value_set_span(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static void floatspan_round_int(Pointer span, long size) {
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
	public static void span_expand(Pointer s1, Pointer s2) {
		MeosLibrary.meos.span_expand(s1, s2);
	}
	
	@SuppressWarnings("unused")
	public static void span_shift(Pointer s, long value) {
		MeosLibrary.meos.span_shift(s, value);
	}
	
	@SuppressWarnings("unused")
	public static void spanset_shift(Pointer s, long value) {
		MeosLibrary.meos.spanset_shift(s, value);
	}
	
	@SuppressWarnings("unused")
	public static Pointer spanbase_extent_transfn(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.spanbase_extent_transfn(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer value_union_transfn(Pointer state, long d, meosType basetype) {
		return MeosLibrary.meos.value_union_transfn(state, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_span_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.adjacent_span_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean adjacent_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.adjacent_spanset_value(ss, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_span_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.contains_span_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.contains_spanset_value(ss, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean contains_set_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.contains_set_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_value_span(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.contained_value_span(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_value_set(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.contained_value_set(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean contained_value_spanset(long d, meosType basetype, Pointer ss) {
		return MeosLibrary.meos.contained_value_spanset(d, basetype, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_set_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.left_set_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_span_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.left_span_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.left_spanset_value(ss, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_value_set(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.left_value_set(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_value_span(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.left_value_span(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean left_value_spanset(long d, meosType basetype, Pointer ss) {
		return MeosLibrary.meos.left_value_spanset(d, basetype, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_value_set(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.right_value_set(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_set_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.right_set_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_value_span(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.right_value_span(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_value_spanset(long d, meosType basetype, Pointer ss) {
		return MeosLibrary.meos.right_value_spanset(d, basetype, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_span_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.right_span_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean right_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.right_spanset_value(ss, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_value_set(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.overleft_value_set(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_set_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.overleft_set_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_value_span(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.overleft_value_span(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_value_spanset(long d, meosType basetype, Pointer ss) {
		return MeosLibrary.meos.overleft_value_spanset(d, basetype, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_span_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.overleft_span_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean overleft_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.overleft_spanset_value(ss, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_value_set(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.overright_value_set(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_set_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.overright_set_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_value_span(long d, meosType basetype, Pointer s) {
		return MeosLibrary.meos.overright_value_span(d, basetype, s);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_value_spanset(long d, meosType basetype, Pointer ss) {
		return MeosLibrary.meos.overright_value_spanset(d, basetype, ss);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_span_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.overright_span_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean overright_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.overright_spanset_value(ss, d, basetype);
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
	public static Pointer intersection_set_value(Pointer s, long d, meosType basetype) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_set_value(s, d, basetype, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_span_value(Pointer s, long d, meosType basetype) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_span_value(s, d, basetype, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer intersection_spanset_value(Pointer ss, long d, meosType basetype) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.intersection_spanset_value(ss, d, basetype, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_set_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.minus_set_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_span_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.minus_span_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.minus_spanset_value(ss, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_value_set(long d, meosType basetype, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_value_set(d, basetype, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_value_span(long d, meosType basetype, Pointer s) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_value_span(d, basetype, s, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer minus_value_spanset(long d, meosType basetype, Pointer ss) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.minus_value_spanset(d, basetype, ss, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_set_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.union_set_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_span_value(Pointer s, long v, meosType basetype) {
		return MeosLibrary.meos.union_span_value(s, v, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer union_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.union_spanset_value(ss, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static double distance_value_value(long l, long r, meosType basetype) {
		return MeosLibrary.meos.distance_value_value(l, r, basetype);
	}
	
	@SuppressWarnings("unused")
	public static double distance_span_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.distance_span_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static double distance_spanset_value(Pointer ss, long d, meosType basetype) {
		return MeosLibrary.meos.distance_spanset_value(ss, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static double distance_set_value(Pointer s, long d, meosType basetype) {
		return MeosLibrary.meos.distance_set_value(s, d, basetype);
	}
	
	@SuppressWarnings("unused")
	public static Pointer number_period_to_tbox(long d, meosType basetype, Pointer p) {
		return MeosLibrary.meos.number_period_to_tbox(d, basetype, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer number_timestamp_to_tbox(long d, meosType basetype, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.number_timestamp_to_tbox(d, basetype, t_new);
	}
	
	@SuppressWarnings("unused")
	public static void stbox_set(boolean hasx, boolean hasz, boolean geodetic, int srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, Pointer p, Pointer box) {
		MeosLibrary.meos.stbox_set(hasx, hasz, geodetic, srid, xmin, xmax, ymin, ymax, zmin, zmax, p, box);
	}
	
	@SuppressWarnings("unused")
	public static void tbox_set(Pointer s, Pointer p, Pointer box) {
		MeosLibrary.meos.tbox_set(s, p, box);
	}
	
	@SuppressWarnings("unused")
	public static void float_set_tbox(double d, Pointer box) {
		MeosLibrary.meos.float_set_tbox(d, box);
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
	public static void period_set_stbox(Pointer p, Pointer box) {
		MeosLibrary.meos.period_set_stbox(p, box);
	}
	
	@SuppressWarnings("unused")
	public static void period_set_tbox(Pointer p, Pointer box) {
		MeosLibrary.meos.period_set_tbox(p, box);
	}
	
	@SuppressWarnings("unused")
	public static void periodset_set_stbox(Pointer ps, Pointer box) {
		MeosLibrary.meos.periodset_set_stbox(ps, box);
	}
	
	@SuppressWarnings("unused")
	public static void periodset_set_tbox(Pointer ps, Pointer box) {
		MeosLibrary.meos.periodset_set_tbox(ps, box);
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
	public static void timestamp_set_stbox(OffsetDateTime t, Pointer box) {
		var t_new = t.toEpochSecond();
		MeosLibrary.meos.timestamp_set_stbox(t_new, box);
	}
	
	@SuppressWarnings("unused")
	public static void timestamp_set_tbox(OffsetDateTime t, Pointer box) {
		var t_new = t.toEpochSecond();
		MeosLibrary.meos.timestamp_set_tbox(t_new, box);
	}
	
	@SuppressWarnings("unused")
	public static void timestampset_set_stbox(Pointer ts, Pointer box) {
		MeosLibrary.meos.timestampset_set_stbox(ts, box);
	}
	
	@SuppressWarnings("unused")
	public static void timestampset_set_tbox(Pointer ts, Pointer box) {
		MeosLibrary.meos.timestampset_set_tbox(ts, box);
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
	public static void bbox_union_span_span(Pointer s1, Pointer s2) {
	}
	
	@SuppressWarnings("unused")
	public static Pointer geoarr_as_text(Pointer geoarr, int count, int maxdd, boolean extended) {
		return MeosLibrary.meos.geoarr_as_text(geoarr, count, maxdd, extended);
	}
	
	@SuppressWarnings("unused")
	public static String tboolinst_as_mfjson(Pointer inst, boolean with_bbox) {
		return MeosLibrary.meos.tboolinst_as_mfjson(inst, with_bbox);
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
	public static Pointer tboolseq_in(String str, int interp) {
		return MeosLibrary.meos.tboolseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static String tboolseqset_as_mfjson(Pointer ss, boolean with_bbox) {
		return MeosLibrary.meos.tboolseqset_as_mfjson(ss, with_bbox);
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
	public static Pointer temporal_values(Pointer temp, Pointer count) {
		return MeosLibrary.meos.temporal_values(temp, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporalarr_out(Pointer temparr, int count, int maxdd) {
		return MeosLibrary.meos.temporalarr_out(temparr, count, maxdd);
	}
	
	@SuppressWarnings("unused")
	public static String tfloatinst_as_mfjson(Pointer inst, boolean with_bbox, int precision) {
		return MeosLibrary.meos.tfloatinst_as_mfjson(inst, with_bbox, precision);
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
	public static Pointer tfloatseq_in(String str, int interp) {
		return MeosLibrary.meos.tfloatseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static String tfloatseqset_as_mfjson(Pointer ss, boolean with_bbox, int precision) {
		return MeosLibrary.meos.tfloatseqset_as_mfjson(ss, with_bbox, precision);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tfloatseqset_in(String str) {
		return MeosLibrary.meos.tfloatseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointinst_in(String str) {
		return MeosLibrary.meos.tgeogpointinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointseq_in(String str, int interp) {
		return MeosLibrary.meos.tgeogpointseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeogpointseqset_in(String str) {
		return MeosLibrary.meos.tgeogpointseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointinst_in(String str) {
		return MeosLibrary.meos.tgeompointinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointseq_in(String str, int interp) {
		return MeosLibrary.meos.tgeompointseq_in(str, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointseqset_in(String str) {
		return MeosLibrary.meos.tgeompointseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tinstant_as_mfjson(Pointer inst, int precision, boolean with_bbox, String srs) {
		return MeosLibrary.meos.tinstant_as_mfjson(inst, precision, with_bbox, srs);
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
	public static Pointer tintinst_in(String str) {
		return MeosLibrary.meos.tintinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String tintseq_as_mfjson(Pointer seq, boolean with_bbox) {
		return MeosLibrary.meos.tintseq_as_mfjson(seq, with_bbox);
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
	public static Pointer tintseqset_in(String str) {
		return MeosLibrary.meos.tintseqset_in(str);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointarr_as_text(Pointer temparr, int count, int maxdd, boolean extended) {
		return MeosLibrary.meos.tpointarr_as_text(temparr, count, maxdd, extended);
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
	public static String tsequence_as_mfjson(Pointer seq, int precision, boolean with_bbox, String srs) {
		return MeosLibrary.meos.tsequence_as_mfjson(seq, precision, with_bbox, srs);
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
	public static String tsequenceset_as_mfjson(Pointer ss, int precision, boolean with_bbox, String srs) {
		return MeosLibrary.meos.tsequenceset_as_mfjson(ss, precision, with_bbox, srs);
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
	public static Pointer ttextinst_in(String str) {
		return MeosLibrary.meos.ttextinst_in(str);
	}
	
	@SuppressWarnings("unused")
	public static String ttextseq_as_mfjson(Pointer seq, boolean with_bbox) {
		return MeosLibrary.meos.ttextseq_as_mfjson(seq, with_bbox);
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
	public static Pointer ttextseqset_in(String str) {
		return MeosLibrary.meos.ttextseqset_in(str);
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
	public static Pointer tpointseq_make_coords(Pointer xcoords, Pointer ycoords, Pointer zcoords, Pointer times, int count, int srid, boolean geodetic, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		return MeosLibrary.meos.tpointseq_make_coords(xcoords, ycoords, zcoords, times, count, srid, geodetic, lower_inc, upper_inc, interp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_from_base_timestampset(long value, meosType temptype, Pointer ss) {
		return MeosLibrary.meos.tsequence_from_base_timestampset(value, temptype, ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_make_exp(Pointer instants, int count, int maxcount, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		return MeosLibrary.meos.tsequence_make_exp(instants, count, maxcount, lower_inc, upper_inc, interp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_compact(Pointer seq) {
		return MeosLibrary.meos.tsequence_compact(seq);
	}
	
	@SuppressWarnings("unused")
	public static void tsequence_restart(Pointer seq, int last) {
		MeosLibrary.meos.tsequence_restart(seq, last);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_subseq(Pointer seq, int from, int to, boolean lower_inc, boolean upper_inc) {
		return MeosLibrary.meos.tsequence_subseq(seq, from, to, lower_inc, upper_inc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_copy(Pointer seq) {
		return MeosLibrary.meos.tsequence_copy(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_from_base_period(long value, meosType temptype, Pointer p, int interp) {
		return MeosLibrary.meos.tsequence_from_base_period(value, temptype, p, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_make_free(Pointer instants, int count, boolean lower_inc, boolean upper_inc, int interp, boolean normalize) {
		return MeosLibrary.meos.tsequence_make_free(instants, count, lower_inc, upper_inc, interp, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_exp(Pointer sequences, int count, int maxcount, boolean normalize) {
		return MeosLibrary.meos.tsequenceset_make_exp(sequences, count, maxcount, normalize);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_compact(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_compact(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_make_free(Pointer sequences, int count, boolean normalize) {
		return MeosLibrary.meos.tsequenceset_make_free(sequences, count, normalize);
	}
	
	@SuppressWarnings("unused")
	public static void tsequenceset_restart(Pointer ss, int last) {
		MeosLibrary.meos.tsequenceset_restart(ss, last);
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
	public static Pointer tsequenceset_from_base_periodset(long value, meosType temptype, Pointer ps, int interp) {
		return MeosLibrary.meos.tsequenceset_from_base_periodset(value, temptype, ps, interp);
	}
	
	@SuppressWarnings("unused")
	public static void temporal_set_period(Pointer temp, Pointer p) {
		MeosLibrary.meos.temporal_set_period(temp, p);
	}
	
	@SuppressWarnings("unused")
	public static void tinstant_set_period(Pointer inst, Pointer p) {
		MeosLibrary.meos.tinstant_set_period(inst, p);
	}
	
	@SuppressWarnings("unused")
	public static void tsequence_set_period(Pointer seq, Pointer p) {
		MeosLibrary.meos.tsequence_set_period(seq, p);
	}
	
	@SuppressWarnings("unused")
	public static void tsequenceset_set_period(Pointer ss, Pointer p) {
		MeosLibrary.meos.tsequenceset_set_period(ss, p);
	}
	
	@SuppressWarnings("unused")
	public static long temporal_end_value(Pointer temp) {
		return MeosLibrary.meos.temporal_end_value(temp);
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
	public static void temporal_set_bbox(Pointer temp, Pointer box) {
		MeosLibrary.meos.temporal_set_bbox(temp, box);
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
	public static void tnumber_set_span(Pointer temp, Pointer span) {
		MeosLibrary.meos.tnumber_set_span(temp, span);
	}
	
	@SuppressWarnings("unused")
	public static long temporal_start_value(Pointer temp) {
		return MeosLibrary.meos.temporal_start_value(temp);
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
	public static Pointer tnumberseqset_abs(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_abs(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseq_angular_difference(Pointer seq) {
		return MeosLibrary.meos.tnumberseq_angular_difference(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_angular_difference(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_angular_difference(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseq_delta_value(Pointer seq) {
		return MeosLibrary.meos.tnumberseq_delta_value(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_delta_value(Pointer ss) {
		return MeosLibrary.meos.tnumberseqset_delta_value(ss);
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
	public static long tinstant_hash(Pointer inst) {
		return MeosLibrary.meos.tinstant_hash(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_instants(Pointer inst, Pointer count) {
		return MeosLibrary.meos.tinstant_instants(inst, count);
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
	public static long tinstant_value(Pointer inst) {
		return MeosLibrary.meos.tinstant_value(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_value_at_timestamp(Pointer inst, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tinstant_value_at_timestamp(inst, t_new, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static long tinstant_value_copy(Pointer inst) {
		return MeosLibrary.meos.tinstant_value_copy(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_values(Pointer inst, Pointer count) {
		return MeosLibrary.meos.tinstant_values(inst, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_duration(Pointer seq) {
		return MeosLibrary.meos.tsequence_duration(seq);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tsequence_end_timestamp(Pointer seq) {
		var result = MeosLibrary.meos.tsequence_end_timestamp(seq);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static long tsequence_hash(Pointer seq) {
		return MeosLibrary.meos.tsequence_hash(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_instants(Pointer seq) {
		return MeosLibrary.meos.tsequence_instants(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_max_instant(Pointer seq) {
		return MeosLibrary.meos.tsequence_max_instant(seq);
	}
	
	@SuppressWarnings("unused")
	public static long tsequence_max_value(Pointer seq) {
		return MeosLibrary.meos.tsequence_max_value(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_min_instant(Pointer seq) {
		return MeosLibrary.meos.tsequence_min_instant(seq);
	}
	
	@SuppressWarnings("unused")
	public static long tsequence_min_value(Pointer seq) {
		return MeosLibrary.meos.tsequence_min_value(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_segments(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tsequence_segments(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_sequences(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tsequence_sequences(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static void tsequence_set_bbox(Pointer seq, Pointer box) {
		MeosLibrary.meos.tsequence_set_bbox(seq, box);
	}
	
	@SuppressWarnings("unused")
	public static void tsequence_expand_bbox(Pointer seq, Pointer inst) {
		MeosLibrary.meos.tsequence_expand_bbox(seq, inst);
	}
	
	@SuppressWarnings("unused")
	public static void tsequenceset_expand_bbox(Pointer ss, Pointer seq) {
		MeosLibrary.meos.tsequenceset_expand_bbox(ss, seq);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tsequence_start_timestamp(Pointer seq) {
		var result = MeosLibrary.meos.tsequence_start_timestamp(seq);
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
	public static Pointer tsequence_value_at_timestamp(Pointer seq, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tsequence_value_at_timestamp(seq, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_values(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tsequence_values(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_duration(Pointer ss, boolean boundspan) {
		return MeosLibrary.meos.tsequenceset_duration(ss, boundspan);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tsequenceset_end_timestamp(Pointer ss) {
		var result = MeosLibrary.meos.tsequenceset_end_timestamp(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static long tsequenceset_hash(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_hash(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_inst_n(Pointer ss, int n) {
		return MeosLibrary.meos.tsequenceset_inst_n(ss, n);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_instants(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_instants(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_max_instant(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_max_instant(ss);
	}
	
	@SuppressWarnings("unused")
	public static long tsequenceset_max_value(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_max_value(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_min_instant(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_min_instant(ss);
	}
	
	@SuppressWarnings("unused")
	public static long tsequenceset_min_value(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_min_value(ss);
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
	public static Pointer tsequenceset_sequences(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_sequences(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_sequences_p(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_sequences_p(ss);
	}
	
	@SuppressWarnings("unused")
	public static void tsequenceset_set_bbox(Pointer ss, Pointer box) {
		MeosLibrary.meos.tsequenceset_set_bbox(ss, box);
	}
	
	@SuppressWarnings("unused")
	public static OffsetDateTime tsequenceset_start_timestamp(Pointer ss) {
		var result = MeosLibrary.meos.tsequenceset_start_timestamp(ss);
		Instant instant = Instant.ofEpochSecond(result);
		return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_time(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_time(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_timestamp_n(Pointer ss, int n) {
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tsequenceset_timestamp_n(ss, n, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_timestamps(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tsequenceset_timestamps(ss, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_value_at_timestamp(Pointer ss, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.tsequenceset_value_at_timestamp(ss, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_values(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tsequenceset_values(ss, count);
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
	public static Pointer tinstant_shift_time(Pointer inst, Pointer interval) {
		return MeosLibrary.meos.tinstant_shift_time(inst, interval);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_to_tsequence(Pointer inst, int interp) {
		return MeosLibrary.meos.tinstant_to_tsequence(inst, interp);
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
	public static Pointer tnuminst_shift_value(Pointer inst, long shift) {
		return MeosLibrary.meos.tnuminst_shift_value(inst, shift);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseq_shift_scale_value(Pointer seq, long shift, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.tnumberseq_shift_scale_value(seq, shift, width, hasshift, haswidth);
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
	public static Pointer tsequence_merge(Pointer seq1, Pointer seq2) {
		return MeosLibrary.meos.tsequence_merge(seq1, seq2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_merge_array(Pointer sequences, int count) {
		return MeosLibrary.meos.tsequence_merge_array(sequences, count);
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
	public static Pointer tsequence_to_tinstant(Pointer seq) {
		return MeosLibrary.meos.tsequence_to_tinstant(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tsequenceset(Pointer seq) {
		return MeosLibrary.meos.tsequence_to_tsequenceset(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_to_tsequenceset_interp(Pointer seq, int interp) {
		return MeosLibrary.meos.tsequence_to_tsequenceset_interp(seq, interp);
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
	public static Pointer tsequenceset_merge(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.tsequenceset_merge(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_merge_array(Pointer seqsets, int count) {
		return MeosLibrary.meos.tsequenceset_merge_array(seqsets, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_set_interp(Pointer ss, int interp) {
		return MeosLibrary.meos.tsequenceset_set_interp(ss, interp);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tnumberseqset_shift_scale_value(Pointer ss, long start, long width, boolean hasshift, boolean haswidth) {
		return MeosLibrary.meos.tnumberseqset_shift_scale_value(ss, start, width, hasshift, haswidth);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_shift_scale_time(Pointer ss, Pointer start, Pointer duration) {
		return MeosLibrary.meos.tsequenceset_shift_scale_time(ss, start, duration);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_tinstant(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_tinstant(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_discrete(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_discrete(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_step(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_step(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_linear(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_linear(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_to_tsequence(Pointer ss) {
		return MeosLibrary.meos.tsequenceset_to_tsequence(ss);
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
	public static Pointer temporal_restrict_period(Pointer temp, Pointer p, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_period(temp, p, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_periodset(Pointer temp, Pointer ps, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_periodset(temp, ps, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_timestamp(Pointer temp, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.temporal_restrict_timestamp(temp, t_new, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer temporal_restrict_timestampset(Pointer temp, Pointer ts, boolean atfunc) {
		return MeosLibrary.meos.temporal_restrict_timestampset(temp, ts, atfunc);
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
	public static Pointer temporal_value_at_timestamp(Pointer temp, OffsetDateTime t, boolean strict) {
		var t_new = t.toEpochSecond();
		boolean out;
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
		out = MeosLibrary.meos.temporal_value_at_timestamp(temp, t_new, strict, result);
		Pointer new_result = result.getPointer(0);
		return out ? new_result : null ;
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_period(Pointer inst, Pointer period, boolean atfunc) {
		return MeosLibrary.meos.tinstant_restrict_period(inst, period, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_periodset(Pointer inst, Pointer ps, boolean atfunc) {
		return MeosLibrary.meos.tinstant_restrict_periodset(inst, ps, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_timestamp(Pointer inst, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tinstant_restrict_timestamp(inst, t_new, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tinstant_restrict_timestampset(Pointer inst, Pointer ts, boolean atfunc) {
		return MeosLibrary.meos.tinstant_restrict_timestampset(inst, ts, atfunc);
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
	public static Pointer tpoint_restrict_geom_time(Pointer temp, Pointer gs, Pointer zspan, Pointer period, boolean atfunc) {
		return MeosLibrary.meos.tpoint_restrict_geom_time(temp, gs, zspan, period, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpoint_restrict_stbox(Pointer temp, Pointer box, boolean border_inc, boolean atfunc) {
		return MeosLibrary.meos.tpoint_restrict_stbox(temp, box, border_inc, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointinst_restrict_geom_time(Pointer inst, Pointer gs, Pointer zspan, Pointer period, boolean atfunc) {
		return MeosLibrary.meos.tpointinst_restrict_geom_time(inst, gs, zspan, period, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointinst_restrict_stbox(Pointer inst, Pointer box, boolean border_inc, boolean atfunc) {
		return MeosLibrary.meos.tpointinst_restrict_stbox(inst, box, border_inc, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_restrict_geom_time(Pointer seq, Pointer gs, Pointer zspan, Pointer period, boolean atfunc) {
		return MeosLibrary.meos.tpointseq_restrict_geom_time(seq, gs, zspan, period, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_restrict_stbox(Pointer seq, Pointer box, boolean border_inc, boolean atfunc) {
		return MeosLibrary.meos.tpointseq_restrict_stbox(seq, box, border_inc, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_restrict_geom_time(Pointer ss, Pointer gs, Pointer zspan, Pointer period, boolean atfunc) {
		return MeosLibrary.meos.tpointseqset_restrict_geom_time(ss, gs, zspan, period, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_restrict_stbox(Pointer ss, Pointer box, boolean border_inc, boolean atfunc) {
		return MeosLibrary.meos.tpointseqset_restrict_stbox(ss, box, border_inc, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_at_period(Pointer seq, Pointer p) {
		return MeosLibrary.meos.tsequence_at_period(seq, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_at_timestamp(Pointer seq, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tsequence_at_timestamp(seq, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_restrict_period(Pointer seq, Pointer p, boolean atfunc) {
		return MeosLibrary.meos.tsequence_restrict_period(seq, p, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_restrict_periodset(Pointer seq, Pointer ps, boolean atfunc) {
		return MeosLibrary.meos.tsequence_restrict_periodset(seq, ps, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_minmax(Pointer ss, boolean min, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_minmax(ss, min, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_period(Pointer ss, Pointer p, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_period(ss, p, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_periodset(Pointer ss, Pointer ps, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_periodset(ss, ps, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_timestamp(Pointer ss, OffsetDateTime t, boolean atfunc) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tsequenceset_restrict_timestamp(ss, t_new, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_timestampset(Pointer ss, Pointer ts, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_timestampset(ss, ts, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_value(Pointer ss, long value, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_value(ss, value, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_restrict_values(Pointer ss, Pointer set, boolean atfunc) {
		return MeosLibrary.meos.tsequenceset_restrict_values(ss, set, atfunc);
	}
	
	@SuppressWarnings("unused")
	public static Pointer distance_tnumber_number(Pointer temp, long value, meosType valuetype, meosType restype) {
		return MeosLibrary.meos.distance_tnumber_number(temp, value, valuetype, restype);
	}
	
	@SuppressWarnings("unused")
	public static double nad_tnumber_number(Pointer temp, long value, meosType basetype) {
		return MeosLibrary.meos.nad_tnumber_number(temp, value, basetype);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_always_eq(Pointer temp, long value) {
		return MeosLibrary.meos.temporal_always_eq(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_always_le(Pointer temp, long value) {
		return MeosLibrary.meos.temporal_always_le(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_always_lt(Pointer temp, long value) {
		return MeosLibrary.meos.temporal_always_lt(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_ever_eq(Pointer temp, long value) {
		return MeosLibrary.meos.temporal_ever_eq(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_ever_le(Pointer temp, long value) {
		return MeosLibrary.meos.temporal_ever_le(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean temporal_ever_lt(Pointer temp, long value) {
		return MeosLibrary.meos.temporal_ever_lt(temp, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tinstant_always_eq(Pointer inst, long value) {
		return MeosLibrary.meos.tinstant_always_eq(inst, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tinstant_always_le(Pointer inst, long value) {
		return MeosLibrary.meos.tinstant_always_le(inst, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tinstant_always_lt(Pointer inst, long value) {
		return MeosLibrary.meos.tinstant_always_lt(inst, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tinstant_ever_eq(Pointer inst, long value) {
		return MeosLibrary.meos.tinstant_ever_eq(inst, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tinstant_ever_le(Pointer inst, long value) {
		return MeosLibrary.meos.tinstant_ever_le(inst, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tinstant_ever_lt(Pointer inst, long value) {
		return MeosLibrary.meos.tinstant_ever_lt(inst, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpointinst_always_eq(Pointer inst, long value) {
		return MeosLibrary.meos.tpointinst_always_eq(inst, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpointinst_ever_eq(Pointer inst, long value) {
		return MeosLibrary.meos.tpointinst_ever_eq(inst, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpointseq_always_eq(Pointer seq, long value) {
		return MeosLibrary.meos.tpointseq_always_eq(seq, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpointseq_ever_eq(Pointer seq, long value) {
		return MeosLibrary.meos.tpointseq_ever_eq(seq, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpointseqset_always_eq(Pointer ss, long value) {
		return MeosLibrary.meos.tpointseqset_always_eq(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tpointseqset_ever_eq(Pointer ss, long value) {
		return MeosLibrary.meos.tpointseqset_ever_eq(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequence_always_eq(Pointer seq, long value) {
		return MeosLibrary.meos.tsequence_always_eq(seq, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequence_always_le(Pointer seq, long value) {
		return MeosLibrary.meos.tsequence_always_le(seq, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequence_always_lt(Pointer seq, long value) {
		return MeosLibrary.meos.tsequence_always_lt(seq, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequence_ever_eq(Pointer seq, long value) {
		return MeosLibrary.meos.tsequence_ever_eq(seq, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequence_ever_le(Pointer seq, long value) {
		return MeosLibrary.meos.tsequence_ever_le(seq, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequence_ever_lt(Pointer seq, long value) {
		return MeosLibrary.meos.tsequence_ever_lt(seq, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequenceset_always_eq(Pointer ss, long value) {
		return MeosLibrary.meos.tsequenceset_always_eq(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequenceset_always_le(Pointer ss, long value) {
		return MeosLibrary.meos.tsequenceset_always_le(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequenceset_always_lt(Pointer ss, long value) {
		return MeosLibrary.meos.tsequenceset_always_lt(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequenceset_ever_eq(Pointer ss, long value) {
		return MeosLibrary.meos.tsequenceset_ever_eq(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequenceset_ever_le(Pointer ss, long value) {
		return MeosLibrary.meos.tsequenceset_ever_le(ss, value);
	}
	
	@SuppressWarnings("unused")
	public static boolean tsequenceset_ever_lt(Pointer ss, long value) {
		return MeosLibrary.meos.tsequenceset_ever_lt(ss, value);
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
	public static int tpointinst_srid(Pointer inst) {
		return MeosLibrary.meos.tpointinst_srid(inst);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_trajectory(Pointer seq) {
		return MeosLibrary.meos.tpointseq_trajectory(seq);
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
	public static Pointer tpointseq_speed(Pointer seq) {
		return MeosLibrary.meos.tpointseq_speed(seq);
	}
	
	@SuppressWarnings("unused")
	public static int tpointseq_srid(Pointer seq) {
		return MeosLibrary.meos.tpointseq_srid(seq);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_stboxes(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tpointseq_stboxes(seq, count);
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
	public static int tpointseqset_srid(Pointer ss) {
		return MeosLibrary.meos.tpointseqset_srid(ss);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_stboxes(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tpointseqset_stboxes(ss, count);
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
	public static Pointer tgeompointinst_tgeogpointinst(Pointer inst, boolean oper) {
		return MeosLibrary.meos.tgeompointinst_tgeogpointinst(inst, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointseq_tgeogpointseq(Pointer seq, boolean oper) {
		return MeosLibrary.meos.tgeompointseq_tgeogpointseq(seq, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompointseqset_tgeogpointseqset(Pointer ss, boolean oper) {
		return MeosLibrary.meos.tgeompointseqset_tgeogpointseqset(ss, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tgeompoint_tgeogpoint(Pointer temp, boolean oper) {
		return MeosLibrary.meos.tgeompoint_tgeogpoint(temp, oper);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointinst_set_srid(Pointer inst, int srid) {
		return MeosLibrary.meos.tpointinst_set_srid(inst, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_make_simple(Pointer seq, Pointer count) {
		return MeosLibrary.meos.tpointseq_make_simple(seq, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseq_set_srid(Pointer seq, int srid) {
		return MeosLibrary.meos.tpointseq_set_srid(seq, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_make_simple(Pointer ss, Pointer count) {
		return MeosLibrary.meos.tpointseqset_make_simple(ss, count);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tpointseqset_set_srid(Pointer ss, int srid) {
		return MeosLibrary.meos.tpointseqset_set_srid(ss, srid);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_insert(Pointer seq1, Pointer seq2, boolean connect) {
		return MeosLibrary.meos.tsequence_insert(seq1, seq2, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_insert(Pointer ss1, Pointer ss2) {
		return MeosLibrary.meos.tsequenceset_insert(ss1, ss2);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_timestamp(Pointer seq, OffsetDateTime t, boolean connect) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tsequence_delete_timestamp(seq, t_new, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_timestampset(Pointer seq, Pointer ts, boolean connect) {
		return MeosLibrary.meos.tsequence_delete_timestampset(seq, ts, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_period(Pointer seq, Pointer p, boolean connect) {
		return MeosLibrary.meos.tsequence_delete_period(seq, p, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequence_delete_periodset(Pointer seq, Pointer ps, boolean connect) {
		return MeosLibrary.meos.tsequence_delete_periodset(seq, ps, connect);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_timestamp(Pointer ss, OffsetDateTime t) {
		var t_new = t.toEpochSecond();
		return MeosLibrary.meos.tsequenceset_delete_timestamp(ss, t_new);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_timestampset(Pointer ss, Pointer ts) {
		return MeosLibrary.meos.tsequenceset_delete_timestampset(ss, ts);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_period(Pointer ss, Pointer p) {
		return MeosLibrary.meos.tsequenceset_delete_period(ss, p);
	}
	
	@SuppressWarnings("unused")
	public static Pointer tsequenceset_delete_periodset(Pointer ss, Pointer ps) {
		return MeosLibrary.meos.tsequenceset_delete_periodset(ss, ps);
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
}