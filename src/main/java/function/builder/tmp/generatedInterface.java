public interface MeosLibrary {
	functions.MeosLibrary INSTANCE = LibraryLoader.create(functions.MeosLibrary.class).load("meos");
	functions.MeosLibrary meos = functions.MeosLibrary.INSTANCE;

	Pointer lwpoint_make(int32_t srid, int hasz, int hasm, Pointer p);

	Pointer lwgeom_from_gserialized(Pointer g);

	Pointer gserialized_from_lwgeom(Pointer geom, Pointer size);

	static inline Pointer lwgeom_as_lwpoint(Pointer lwgeom);

	int32_t lwgeom_get_srid(Pointer geom);

	double lwpoint_get_x(Pointer point);

	double lwpoint_get_y(Pointer point);

	double lwpoint_get_z(Pointer point);

	double lwpoint_get_m(Pointer point);

	int lwgeom_has_z(Pointer geom);

	int lwgeom_has_m(Pointer geom);

	void meos_initialize(byte[] tz_str);

	void meos_finish();

	boolean bool_in(String in_str);

	String bool_out(boolean b);

	DateADT pg_date_in(String str);

	String pg_date_out(DateADT date);

	int pg_interval_cmp(Pointer interval1, Pointer interval2);

	Pointer pg_interval_in(String str, int typmod);

	Pointer pg_interval_make(int years, int months, int weeks, int days, int hours, int mins, double secs);

	String pg_interval_out(Pointer span);

	Pointer pg_interval_mul(Pointer span, double factor);

	Pointer pg_interval_pl(Pointer span1, Pointer span2);

	TimeADT pg_time_in(String str, int typmod);

	String pg_time_out(TimeADT time);

	int pg_timestamp_in(String str, int typmod);

	Pointer pg_timestamp_mi(int dt1, int dt2);

	int pg_timestamp_mi_interval(int timestamp, Pointer span);

	String pg_timestamp_out(int dt);

	int pg_timestamp_pl_interval(int timestamp, Pointer span);

	int pg_timestamptz_in(String str, int typmod);

	String pg_timestamptz_out(int dt);

	Pointer gserialized_as_ewkb(Pointer geom, String type);

	String gserialized_as_geojson(Pointer geom, int option, int precision, String srs);

	String gserialized_as_hexewkb(Pointer geom, String type);

	String gserialized_as_text(Pointer geom, int precision);

	Pointer gserialized_from_ewkb(Pointer bytea_wkb, int srid);

	Pointer gserialized_from_geojson(String geojson);

	Pointer gserialized_from_hexewkb(String wkt);

	Pointer gserialized_from_text(String wkt, int srid);

	Pointer gserialized_in(String input, int geom_typmod);

	String gserialized_out(Pointer geom);

	boolean gserialized_same(Pointer geom1, Pointer geom2);

	Pointer floatspan_in(String str);

	String floatspan_out(Pointer s, int maxdd);

	Pointer intspan_in(String str);

	String intspan_out(Pointer s);

	Pointer period_in(String str);

	String period_out(Pointer s);

	String periodset_as_hexwkb(Pointer ps, int variant, Pointer size_out);

	Pointer periodset_as_wkb(Pointer ps, int variant, Pointer size_out);

	Pointer periodset_from_hexwkb(String hexwkb);

	Pointer periodset_from_wkb(Pointer wkb, int size);

	Pointer periodset_in(String str);

	String periodset_out(Pointer ps);

	String span_as_hexwkb(Pointer s, int variant, Pointer size_out);

	Pointer span_as_wkb(Pointer s, int variant, Pointer size_out);

	Pointer span_from_hexwkb(String hexwkb);

	Pointer span_from_wkb(Pointer wkb, int size);

	String span_out(Pointer s, int arg);

	String timestampset_as_hexwkb(Pointer ts, int variant, Pointer size_out);

	Pointer timestampset_as_wkb(Pointer ts, int variant, Pointer size_out);

	Pointer timestampset_from_hexwkb(String hexwkb);

	Pointer timestampset_from_wkb(Pointer wkb, int size);

	Pointer timestampset_in(String str);

	String timestampset_out(Pointer ts);

	Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc);

	Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc);

	Pointer period_make(int lower, int upper, boolean lower_inc, boolean upper_inc);

	Pointer periodset_copy(Pointer ps);

	Pointer periodset_make(Pointer[] periods, int count, boolean normalize);

	Pointer periodset_make_free(Pointer[] periods, int count, boolean normalize);

	Pointer span_copy(Pointer s);

	Pointer timestampset_copy(Pointer ts);

	Pointer timestampset_make(Pointer times, int count);

	Pointer timestampset_make_free(Pointer times, int count);

	Pointer float_to_floaspan(double d);

	Pointer int_to_intspan(int i);

	Pointer period_to_periodset(Pointer period);

	Pointer periodset_to_period(Pointer ps);

	Pointer timestamp_to_period(int t);

	Pointer timestamp_to_periodset(int t);

	Pointer timestamp_to_timestampset(int t);

	Pointer timestampset_to_periodset(Pointer ts);

	double floatspan_lower(Pointer s);

	double floatspan_upper(Pointer s);

	int intspan_lower(Pointer s);

	int intspan_upper(Pointer s);

	Pointer period_duration(Pointer s);

	int period_lower(Pointer p);

	int period_upper(Pointer p);

	Pointer periodset_duration(Pointer ps);

	Pointer periodset_end_period(Pointer ps);

	int periodset_end_timestamp(Pointer ps);

	uint periodset_hash(Pointer ps);

	uint64 periodset_hash_extended(Pointer ps, uint64 seed);

	int periodset_mem_size(Pointer ps);

	int periodset_num_periods(Pointer ps);

	int periodset_num_timestamps(Pointer ps);

	Pointer periodset_period_n(Pointer ps, int i);

	Pointer periodset_start_period(Pointer ps);

	int periodset_start_timestamp(Pointer ps);

	Pointer periodset_timespan(Pointer ps);

	boolean periodset_timestamp_n(Pointer ps, int n, Pointer result);

	Pointer periodset_timestamps(Pointer ps, Pointer count);

	uint span_hash(Pointer s);

	uint64 span_hash_extended(Pointer s, uint64 seed);

	boolean span_lower_inc(Pointer s);

	boolean span_upper_inc(Pointer s);

	double span_width(Pointer s);

	int timestampset_end_timestamp(Pointer ss);

	uint timestampset_hash(Pointer ss);

	uint64 timestampset_hash_extended(Pointer ss, uint64 seed);

	int timestampset_mem_size(Pointer ss);

	int timestampset_num_timestamps(Pointer ss);

	int timestampset_start_timestamp(Pointer ss);

	Pointer timestampset_timespan(Pointer ss);

	boolean timestampset_timestamp_n(Pointer ss, int n, Pointer result);

	Pointer timestampset_timestamps(Pointer ss);

	Pointer periodset_shift_tscale(Pointer ps, Pointer start, Pointer duration);

	void span_expand(Pointer s1, Pointer s2);

	void period_shift_tscale(Pointer start, Pointer duration, Pointer result);

	Pointer timestampset_shift_tscale(Pointer ss, Pointer start, Pointer duration);

	boolean adjacent_floatspan_float(Pointer s, double d);

	boolean adjacent_intspan_int(Pointer s, int i);

	boolean adjacent_period_periodset(Pointer p, Pointer ps);

	boolean adjacent_period_timestamp(Pointer p, int t);

	boolean adjacent_period_timestampset(Pointer p, Pointer ts);

	boolean adjacent_periodset_period(Pointer ps, Pointer p);

	boolean adjacent_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean adjacent_periodset_timestamp(Pointer ps, int t);

	boolean adjacent_periodset_timestampset(Pointer ps, Pointer ts);

	boolean adjacent_span_span(Pointer s1, Pointer s2);

	boolean adjacent_timestamp_period(int t, Pointer p);

	boolean adjacent_timestamp_periodset(int t, Pointer ps);

	boolean adjacent_timestampset_period(Pointer ts, Pointer p);

	boolean adjacent_timestampset_periodset(Pointer ts, Pointer ps);

	boolean contained_float_floatspan(double d, Pointer s);

	boolean contained_int_intspan(int i, Pointer s);

	boolean contained_period_periodset(Pointer p, Pointer ps);

	boolean contained_periodset_period(Pointer ps, Pointer p);

	boolean contained_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean contained_span_span(Pointer s1, Pointer s2);

	boolean contained_timestamp_period(int t, Pointer p);

	boolean contained_timestamp_periodset(int t, Pointer ps);

	boolean contained_timestamp_timestampset(int t, Pointer ts);

	boolean contained_timestampset_period(Pointer ts, Pointer p);

	boolean contained_timestampset_periodset(Pointer ts, Pointer ps);

	boolean contained_timestampset_timestampset(Pointer ts1, Pointer ts2);

	boolean contains_floatspan_float(Pointer s, double d);

	boolean contains_intspan_int(Pointer s, int i);

	boolean contains_period_periodset(Pointer p, Pointer ps);

	boolean contains_period_timestamp(Pointer p, int t);

	boolean contains_period_timestampset(Pointer p, Pointer ts);

	boolean contains_periodset_period(Pointer ps, Pointer p);

	boolean contains_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean contains_periodset_timestamp(Pointer ps, int t);

	boolean contains_periodset_timestampset(Pointer ps, Pointer ts);

	boolean contains_span_span(Pointer s1, Pointer s2);

	boolean contains_timestampset_timestamp(Pointer ts, int t);

	boolean contains_timestampset_timestampset(Pointer ts1, Pointer ts2);

	boolean overlaps_period_periodset(Pointer p, Pointer ps);

	boolean overlaps_period_timestampset(Pointer p, Pointer ts);

	boolean overlaps_periodset_period(Pointer ps, Pointer p);

	boolean overlaps_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean overlaps_periodset_timestampset(Pointer ps, Pointer ts);

	boolean overlaps_span_span(Pointer s1, Pointer s2);

	boolean overlaps_timestampset_period(Pointer ts, Pointer p);

	boolean overlaps_timestampset_periodset(Pointer ts, Pointer ps);

	boolean overlaps_timestampset_timestampset(Pointer ts1, Pointer ts2);

	boolean after_period_periodset(Pointer p, Pointer ps);

	boolean after_period_timestamp(Pointer p, int t);

	boolean after_period_timestampset(Pointer p, Pointer ts);

	boolean after_periodset_period(Pointer ps, Pointer p);

	boolean after_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean after_periodset_timestamp(Pointer ps, int t);

	boolean after_periodset_timestampset(Pointer ps, Pointer ts);

	boolean after_timestamp_period(int t, Pointer p);

	boolean after_timestamp_periodset(int t, Pointer ps);

	boolean after_timestamp_timestampset(int t, Pointer ts);

	boolean after_timestampset_period(Pointer ts, Pointer p);

	boolean after_timestampset_periodset(Pointer ts, Pointer ps);

	boolean after_timestampset_timestamp(Pointer ts, int t);

	boolean after_timestampset_timestampset(Pointer ts1, Pointer ts2);

	boolean before_period_periodset(Pointer p, Pointer ps);

	boolean before_period_timestamp(Pointer p, int t);

	boolean before_period_timestampset(Pointer p, Pointer ts);

	boolean before_periodset_period(Pointer ps, Pointer p);

	boolean before_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean before_periodset_timestamp(Pointer ps, int t);

	boolean before_periodset_timestampset(Pointer ps, Pointer ts);

	boolean before_timestamp_period(int t, Pointer p);

	boolean before_timestamp_periodset(int t, Pointer ps);

	boolean before_timestamp_timestampset(int t, Pointer ts);

	boolean before_timestampset_period(Pointer ts, Pointer p);

	boolean before_timestampset_periodset(Pointer ts, Pointer ps);

	boolean before_timestampset_timestamp(Pointer ts, int t);

	boolean before_timestampset_timestampset(Pointer ts1, Pointer ts2);

	boolean left_float_floatspan(double d, Pointer s);

	boolean left_floatspan_float(Pointer s, double d);

	boolean left_int_intspan(int i, Pointer s);

	boolean left_intspan_int(Pointer s, int i);

	boolean left_span_span(Pointer s1, Pointer s2);

	boolean overafter_period_periodset(Pointer p, Pointer ps);

	boolean overafter_period_timestamp(Pointer p, int t);

	boolean overafter_period_timestampset(Pointer p, Pointer ts);

	boolean overafter_periodset_period(Pointer ps, Pointer p);

	boolean overafter_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean overafter_periodset_timestamp(Pointer ps, int t);

	boolean overafter_periodset_timestampset(Pointer ps, Pointer ts);

	boolean overafter_timestamp_period(int t, Pointer p);

	boolean overafter_timestamp_periodset(int t, Pointer ps);

	boolean overafter_timestamp_timestampset(int t, Pointer ts);

	boolean overafter_timestampset_period(Pointer ts, Pointer p);

	boolean overafter_timestampset_periodset(Pointer ts, Pointer ps);

	boolean overafter_timestampset_timestamp(Pointer ts, int t);

	boolean overafter_timestampset_timestampset(Pointer ts1, Pointer ts2);

	boolean overbefore_period_periodset(Pointer p, Pointer ps);

	boolean overbefore_period_timestamp(Pointer p, int t);

	boolean overbefore_period_timestampset(Pointer p, Pointer ts);

	boolean overbefore_periodset_period(Pointer ps, Pointer p);

	boolean overbefore_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean overbefore_periodset_timestamp(Pointer ps, int t);

	boolean overbefore_periodset_timestampset(Pointer ps, Pointer ts);

	boolean overbefore_timestamp_period(int t, Pointer p);

	boolean overbefore_timestamp_periodset(int t, Pointer ps);

	boolean overbefore_timestamp_timestampset(int t, Pointer ts);

	boolean overbefore_timestampset_period(Pointer ts, Pointer p);

	boolean overbefore_timestampset_periodset(Pointer ts, Pointer ps);

	boolean overbefore_timestampset_timestamp(Pointer ts, int t);

	boolean overbefore_timestampset_timestampset(Pointer ts1, Pointer ts2);

	boolean overleft_float_floatspan(double d, Pointer s);

	boolean overleft_floatspan_float(Pointer s, double d);

	boolean overleft_int_intspan(int i, Pointer s);

	boolean overleft_intspan_int(Pointer s, int i);

	boolean overleft_span_span(Pointer s1, Pointer s2);

	boolean overright_float_floatspan(double d, Pointer s);

	boolean overright_floatspan_float(Pointer s, double d);

	boolean overright_int_intspan(int i, Pointer s);

	boolean overright_intspan_int(Pointer s, int i);

	boolean overright_span_span(Pointer s1, Pointer s2);

	boolean right_float_floatspan(double d, Pointer s);

	boolean right_floatspan_float(Pointer s, double d);

	boolean right_int_intspan(int i, Pointer s);

	boolean right_intspan_int(Pointer s, int i);

	boolean right_span_span(Pointer s1, Pointer s2);

	Pointer intersection_period_periodset(Pointer p, Pointer ps);

	boolean intersection_period_timestamp(Pointer p, int t, Pointer result);

	Pointer intersection_period_timestampset(Pointer ps, Pointer ts);

	Pointer intersection_periodset_period(Pointer ps, Pointer p);

	Pointer intersection_periodset_periodset(Pointer ps1, Pointer ps2);

	boolean intersection_periodset_timestamp(Pointer ps, int t, Pointer result);

	Pointer intersection_periodset_timestampset(Pointer ps, Pointer ts);

	Pointer intersection_span_span(Pointer s1, Pointer s2);

	boolean intersection_timestamp_period(int t, Pointer p, Pointer result);

	boolean intersection_timestamp_periodset(int t, Pointer ps, Pointer result);

	boolean intersection_timestamp_timestamp(int t1, int t2, Pointer result);

	boolean intersection_timestamp_timestampset(int t, Pointer ts, Pointer result);

	Pointer intersection_timestampset_period(Pointer ts, Pointer p);

	Pointer intersection_timestampset_periodset(Pointer ts, Pointer ps);

	boolean intersection_timestampset_timestamp(Pointer ts, int t, Pointer result);

	Pointer intersection_timestampset_timestampset(Pointer ts1, Pointer ts2);

	Pointer minus_period_period(Pointer p1, Pointer p2);

	Pointer minus_period_periodset(Pointer p, Pointer ps);

	Pointer minus_period_timestamp(Pointer p, int t);

	Pointer minus_period_timestampset(Pointer p, Pointer ts);

	Pointer minus_periodset_period(Pointer ps, Pointer p);

	Pointer minus_periodset_periodset(Pointer ps1, Pointer ps2);

	Pointer minus_periodset_timestamp(Pointer ps, int t);

	Pointer minus_periodset_timestampset(Pointer ps, Pointer ts);

	Pointer minus_span_span(Pointer s1, Pointer s2);

	boolean minus_timestamp_period(int t, Pointer p, Pointer result);

	boolean minus_timestamp_periodset(int t, Pointer ps, Pointer result);

	boolean minus_timestamp_timestamp(int t1, int t2, Pointer result);

	boolean minus_timestamp_timestampset(int t, Pointer ts, Pointer result);

	Pointer minus_timestampset_period(Pointer ts, Pointer p);

	Pointer minus_timestampset_periodset(Pointer ts, Pointer ps);

	Pointer minus_timestampset_timestamp(Pointer ts, int t);

	Pointer minus_timestampset_timestampset(Pointer ts1, Pointer ts2);

	Pointer union_period_period(Pointer p1, Pointer p2);

	Pointer union_period_periodset(Pointer p, Pointer ps);

	Pointer union_period_timestamp(Pointer p, int t);

	Pointer union_period_timestampset(Pointer p, Pointer ts);

	Pointer union_periodset_period(Pointer ps, Pointer p);

	Pointer union_periodset_periodset(Pointer ps1, Pointer ps2);

	Pointer union_periodset_timestamp(Pointer ps, int t);

	Pointer union_periodset_timestampset(Pointer ps, Pointer ts);

	Pointer union_span_span(Pointer s1, Pointer s2, boolean strict);

	Pointer union_timestamp_period(int t, Pointer p);

	Pointer union_timestamp_periodset(int t, Pointer ps);

	Pointer union_timestamp_timestamp(int t1, int t2);

	Pointer union_timestamp_timestampset(int t, Pointer ts);

	Pointer union_timestampset_period(Pointer ts, Pointer p);

	Pointer union_timestampset_periodset(Pointer ts, Pointer ps);

	Pointer union_timestampset_timestamp(Pointer ts, int t);

	Pointer union_timestampset_timestampset(Pointer ts1, Pointer ts2);

	double distance_floatspan_float(Pointer s, double d);

	double distance_intspan_int(Pointer s, int i);

	double distance_period_periodset(Pointer p, Pointer ps);

	double distance_period_timestamp(Pointer p, int t);

	double distance_period_timestampset(Pointer p, Pointer ts);

	double distance_periodset_period(Pointer ps, Pointer p);

	double distance_periodset_periodset(Pointer ps1, Pointer ps2);

	double distance_periodset_timestamp(Pointer ps, int t);

	double distance_periodset_timestampset(Pointer ps, Pointer ts);

	double distance_span_span(Pointer s1, Pointer s2);

	double distance_timestamp_period(int t, Pointer p);

	double distance_timestamp_periodset(int t, Pointer ps);

	double distance_timestamp_timestamp(int t1, int t2);

	double distance_timestamp_timestampset(int t, Pointer ts);

	double distance_timestampset_period(Pointer ts, Pointer p);

	double distance_timestampset_periodset(Pointer ts, Pointer ps);

	double distance_timestampset_timestamp(Pointer ts, int t);

	double distance_timestampset_timestampset(Pointer ts1, Pointer ts2);

	Pointer timestamp_extent_transfn(Pointer p, int t);

	Pointer timestampset_extent_transfn(Pointer p, Pointer ts);

	Pointer span_extent_transfn(Pointer p1, Pointer p2);

	Pointer periodset_extent_transfn(Pointer p, Pointer ps);

	Pointer timestamp_tunion_transfn(Pointer state, int t);

	Pointer timestampset_tunion_transfn(Pointer state, Pointer ts);

	Pointer period_tunion_transfn(Pointer state, Pointer p);

	Pointer periodset_tunion_transfn(Pointer state, Pointer ps);

	Pointer timestamp_tunion_finalfn(Pointer state);

	Pointer period_tunion_finalfn(Pointer state);

	Pointer timestamp_tcount_transfn(Pointer state, int t, Pointer interval, int origin);

	Pointer timestampset_tcount_transfn(Pointer state, Pointer ts, Pointer interval, int origin);

	Pointer period_tcount_transfn(Pointer state, Pointer p, Pointer interval, int origin);

	Pointer periodset_tcount_transfn(Pointer state, Pointer ps, Pointer interval, int origin);

	boolean periodset_eq(Pointer ps1, Pointer ps2);

	boolean periodset_ne(Pointer ps1, Pointer ps2);

	int periodset_cmp(Pointer ps1, Pointer ps2);

	boolean periodset_lt(Pointer ps1, Pointer ps2);

	boolean periodset_le(Pointer ps1, Pointer ps2);

	boolean periodset_ge(Pointer ps1, Pointer ps2);

	boolean periodset_gt(Pointer ps1, Pointer ps2);

	boolean span_eq(Pointer s1, Pointer s2);

	boolean span_ne(Pointer s1, Pointer s2);

	int span_cmp(Pointer s1, Pointer s2);

	boolean span_lt(Pointer s1, Pointer s2);

	boolean span_le(Pointer s1, Pointer s2);

	boolean span_ge(Pointer s1, Pointer s2);

	boolean span_gt(Pointer s1, Pointer s2);

	boolean timestampset_eq(Pointer ss1, Pointer ss2);

	boolean timestampset_ne(Pointer ss1, Pointer ss2);

	int timestampset_cmp(Pointer ss1, Pointer ss2);

	boolean timestampset_lt(Pointer ss1, Pointer ss2);

	boolean timestampset_le(Pointer ss1, Pointer ss2);

	boolean timestampset_ge(Pointer ss1, Pointer ss2);

	boolean timestampset_gt(Pointer ss1, Pointer ss2);

	Pointer tbox_in(String str);

	String tbox_out(Pointer box, int maxdd);

	Pointer tbox_from_wkb(Pointer wkb, int size);

	Pointer tbox_from_hexwkb(String hexwkb);

	Pointer stbox_from_wkb(Pointer wkb, int size);

	Pointer stbox_from_hexwkb(String hexwkb);

	Pointer tbox_as_wkb(Pointer box, int variant, Pointer size_out);

	String tbox_as_hexwkb(Pointer box, int variant, Pointer size);

	Pointer stbox_as_wkb(Pointer box, int variant, Pointer size_out);

	String stbox_as_hexwkb(Pointer box, int variant, Pointer size);

	Pointer stbox_in(String str);

	String stbox_out(Pointer box, int maxdd);

	Pointer tbox_make(Pointer p, Pointer s);

	void tbox_set(Pointer p, Pointer s, Pointer box);

	Pointer tbox_copy(Pointer box);

	Pointer stbox_copy(Pointer box);

	Pointer int_to_tbox(int i);

	Pointer float_to_tbox(double d);

	Pointer span_to_tbox(Pointer span);

	Pointer timestamp_to_tbox(int t);

	Pointer timestampset_to_tbox(Pointer ss);

	Pointer period_to_tbox(Pointer p);

	Pointer periodset_to_tbox(Pointer ps);

	Pointer int_timestamp_to_tbox(int i, int t);

	Pointer float_timestamp_to_tbox(double d, int t);

	Pointer int_period_to_tbox(int i, Pointer p);

	Pointer float_period_to_tbox(double d, Pointer p);

	Pointer span_timestamp_to_tbox(Pointer span, int t);

	Pointer span_period_to_tbox(Pointer span, Pointer p);

	Pointer tbox_to_floatspan(Pointer box);

	Pointer tbox_to_period(Pointer box);

	Pointer stbox_to_period(Pointer box);

	Pointer tnumber_to_tbox(Pointer temp);

	Pointer stbox_to_geo(Pointer box);

	Pointer tpoint_to_stbox(Pointer temp);

	Pointer geo_to_stbox(Pointer gs);

	Pointer timestamp_to_stbox(int t);

	Pointer timestampset_to_stbox(Pointer ts);

	Pointer period_to_stbox(Pointer p);

	Pointer periodset_to_stbox(Pointer ps);

	Pointer geo_timestamp_to_stbox(Pointer gs, int t);

	Pointer geo_period_to_stbox(Pointer gs, Pointer p);

	boolean tbox_hasx(Pointer box);

	boolean tbox_hast(Pointer box);

	boolean tbox_xmin(Pointer box, Pointer result);

	boolean tbox_xmax(Pointer box, Pointer result);

	boolean tbox_tmin(Pointer box, Pointer result);

	boolean tbox_tmax(Pointer box, Pointer result);

	boolean stbox_hasx(Pointer box);

	boolean stbox_hasz(Pointer box);

	boolean stbox_hast(Pointer box);

	boolean stbox_isgeodetic(Pointer box);

	boolean stbox_xmin(Pointer box, Pointer result);

	boolean stbox_xmax(Pointer box, Pointer result);

	boolean stbox_ymin(Pointer box, Pointer result);

	boolean stbox_ymax(Pointer box, Pointer result);

	boolean stbox_zmin(Pointer box, Pointer result);

	boolean stbox_zmax(Pointer box, Pointer result);

	boolean stbox_tmin(Pointer box, Pointer result);

	boolean stbox_tmax(Pointer box, Pointer result);

	int stbox_srid(Pointer box);

	void tbox_expand(Pointer box1, Pointer box2);

	void tbox_shift_tscale(Pointer start, Pointer duration, Pointer box);

	Pointer tbox_expand_value(Pointer box, double d);

	Pointer tbox_expand_temporal(Pointer box, Pointer interval);

	void stbox_expand(Pointer box1, Pointer box2);

	void stbox_shift_tscale(Pointer start, Pointer duration, Pointer box);

	Pointer stbox_set_srid(Pointer box, int srid);

	Pointer stbox_expand_spatial(Pointer box, double d);

	Pointer stbox_expand_temporal(Pointer box, Pointer interval);

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

	Pointer union_tbox_tbox(Pointer box1, Pointer box2);

	boolean inter_tbox_tbox(Pointer box1, Pointer box2, Pointer result);

	Pointer intersection_tbox_tbox(Pointer box1, Pointer box2);

	Pointer union_stbox_stbox(Pointer box1, Pointer box2, boolean strict);

	boolean inter_stbox_stbox(Pointer box1, Pointer box2, Pointer result);

	Pointer intersection_stbox_stbox(Pointer box1, Pointer box2);

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

	Pointer cstring2text(String cstring);

	String text2cstring(Pointer textptr);

	Pointer tbool_in(String str);

	String tbool_out(Pointer temp);

	String temporal_as_hexwkb(Pointer temp, int variant, Pointer size_out);

	String temporal_as_mfjson(Pointer temp, boolean with_bbox, int flags, int precision, String srs);

	Pointer temporal_as_wkb(Pointer temp, int variant, Pointer size_out);

	Pointer temporal_from_hexwkb(String hexwkb);

	Pointer temporal_from_mfjson(String mfjson);

	Pointer temporal_from_wkb(Pointer wkb, int size);

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

	Pointer tbool_from_base(boolean b, Pointer temp);

	Pointer tboolinst_make(boolean b, int t);

	Pointer tbooldiscseq_from_base_time(boolean b, Pointer ts);

	Pointer tboolseq_from_base(boolean b, Pointer seq);

	Pointer tboolseq_from_base_time(boolean b, Pointer p);

	Pointer tboolseqset_from_base(boolean b, Pointer ss);

	Pointer tboolseqset_from_base_time(boolean b, Pointer ps);

	Pointer temporal_copy(Pointer temp);

	Pointer tfloat_from_base(double d, Pointer temp, interpType interp);

	Pointer tfloatinst_make(double d, int t);

	Pointer tfloatdiscseq_from_base_time(double d, Pointer ts);

	Pointer tfloatseq_from_base(double d, Pointer seq, interpType interp);

	Pointer tfloatseq_from_base_time(double d, Pointer p, interpType interp);

	Pointer tfloatseqset_from_base(double d, Pointer ss, interpType interp);

	Pointer tfloatseqset_from_base_time(double d, Pointer ps, interpType interp);

	Pointer tgeogpoint_from_base(Pointer gs, Pointer temp, interpType interp);

	Pointer tgeogpointinst_make(Pointer gs, int t);

	Pointer tgeogpointdiscseq_from_base_time(Pointer gs, Pointer ts);

	Pointer tgeogpointseq_from_base(Pointer gs, Pointer seq, interpType interp);

	Pointer tgeogpointseq_from_base_time(Pointer gs, Pointer p, interpType interp);

	Pointer tgeogpointseqset_from_base(Pointer gs, Pointer ss, interpType interp);

	Pointer tgeogpointseqset_from_base_time(Pointer gs, Pointer ps, interpType interp);

	Pointer tgeompoint_from_base(Pointer gs, Pointer temp, interpType interp);

	Pointer tgeompointinst_make(Pointer gs, int t);

	Pointer tgeompointdiscseq_from_base_time(Pointer gs, Pointer ts);

	Pointer tgeompointseq_from_base(Pointer gs, Pointer seq, interpType interp);

	Pointer tgeompointseq_from_base_time(Pointer gs, Pointer p, interpType interp);

	Pointer tgeompointseqset_from_base(Pointer gs, Pointer ss, interpType interp);

	Pointer tgeompointseqset_from_base_time(Pointer gs, Pointer ps, interpType interp);

	Pointer tint_from_base(int i, Pointer temp);

	Pointer tintinst_make(int i, int t);

	Pointer tintdiscseq_from_base_time(int i, Pointer ts);

	Pointer tintseq_from_base(int i, Pointer seq);

	Pointer tintseq_from_base_time(int i, Pointer p);

	Pointer tintseqset_from_base(int i, Pointer ss);

	Pointer tintseqset_from_base_time(int i, Pointer ps);

	Pointer tsequence_make(Pointer[] instants, int count, boolean lower_inc, boolean upper_inc, interpType interp, boolean normalize);

	Pointer tsequence_make_exp(Pointer[] instants, int count, int maxcount, boolean lower_inc, boolean upper_inc, interpType interp, boolean normalize);

	Pointer tsequence_make_free(Pointer[] instants, int count, boolean lower_inc, boolean upper_inc, interpType interp, boolean normalize);

	Pointer tsequenceset_make(Pointer[] sequences, int count, boolean normalize);

	Pointer tsequenceset_make_free(Pointer[] sequences, int count, boolean normalize);

	Pointer tsequenceset_make_gaps(Pointer[] instants, int count, interpType interp, float maxdist, Pointer maxt);

	Pointer ttext_from_base(Pointer txt, Pointer temp);

	Pointer ttextinst_make(Pointer txt, int t);

	Pointer ttextdiscseq_from_base_time(Pointer txt, Pointer ts);

	Pointer ttextseq_from_base(Pointer txt, Pointer seq);

	Pointer ttextseq_from_base_time(Pointer txt, Pointer p);

	Pointer ttextseqset_from_base(Pointer txt, Pointer ss);

	Pointer ttextseqset_from_base_time(Pointer txt, Pointer ps);

	Pointer tfloat_to_tint(Pointer temp);

	Pointer tint_to_tfloat(Pointer temp);

	Pointer tnumber_to_span(Pointer temp);

	Pointer temporal_to_period(Pointer temp);

	boolean tbool_end_value(Pointer temp);

	boolean tbool_start_value(Pointer temp);

	Pointer tbool_values(Pointer temp, Pointer count);

	Pointer temporal_duration(Pointer temp);

	Pointer temporal_end_sequence(Pointer temp);

	int temporal_end_timestamp(Pointer temp);

	uint temporal_hash(Pointer temp);

	String temporal_interpolation(Pointer temp);

	int temporal_num_instants(Pointer temp);

	int temporal_num_sequences(Pointer temp);

	int temporal_num_timestamps(Pointer temp);

	Pointer[] temporal_segments(Pointer temp, Pointer count);

	Pointer temporal_sequence_n(Pointer temp, int i);

	Pointer[] temporal_sequences(Pointer temp, Pointer count);

	int temporal_size(Pointer temp);

	Pointer temporal_start_sequence(Pointer temp);

	int temporal_start_timestamp(Pointer temp);

	String temporal_subtype(Pointer temp);

	Pointer temporal_time(Pointer temp);

	Pointer temporal_timespan(Pointer temp);

	boolean temporal_timestamp_n(Pointer temp, int n, Pointer result);

	Pointer temporal_timestamps(Pointer temp, Pointer count);

	double tfloat_end_value(Pointer temp);

	double tfloat_max_value(Pointer temp);

	double tfloat_min_value(Pointer temp);

	Pointer[] tfloat_spans(Pointer temp, Pointer count);

	double tfloat_start_value(Pointer temp);

	Pointer tfloat_values(Pointer temp, Pointer count);

	int tint_end_value(Pointer temp);

	int tint_max_value(Pointer temp);

	int tint_min_value(Pointer temp);

	int tint_start_value(Pointer temp);

	Pointer tint_values(Pointer temp, Pointer count);

	Pointer tpoint_end_value(Pointer temp);

	Pointer tpoint_start_value(Pointer temp);

	Pointer[] tpoint_values(Pointer temp, Pointer count);

	Pointer ttext_end_value(Pointer temp);

	Pointer ttext_max_value(Pointer temp);

	Pointer ttext_min_value(Pointer temp);

	Pointer ttext_start_value(Pointer temp);

	Pointer[] ttext_values(Pointer temp, Pointer count);

	Pointer tsequence_compact(Pointer seq);

	Pointer temporal_append_tinstant(Pointer temp, Pointer inst, boolean expand);

	Pointer temporal_merge(Pointer temp1, Pointer temp2);

	Pointer temporal_merge_array(Pointer[] temparr, int count);

	Pointer temporal_shift(Pointer temp, Pointer shift);

	Pointer temporal_shift_tscale(Pointer temp, Pointer shift, Pointer duration);

	Pointer temporal_step_to_linear(Pointer temp);

	Pointer temporal_to_tinstant(Pointer temp);

	Pointer temporal_to_tdiscseq(Pointer temp);

	Pointer temporal_to_tsequence(Pointer temp);

	Pointer temporal_to_tsequenceset(Pointer temp);

	Pointer temporal_tscale(Pointer temp, Pointer duration);

	Pointer tbool_at_value(Pointer temp, boolean b);

	Pointer tbool_minus_value(Pointer temp, boolean b);

	boolean tbool_value_at_timestamp(Pointer temp, int t, boolean strict, Pointer value);

	Pointer temporal_at_max(Pointer temp);

	Pointer temporal_at_min(Pointer temp);

	Pointer temporal_at_period(Pointer temp, Pointer p);

	Pointer temporal_at_periodset(Pointer temp, Pointer ps);

	Pointer temporal_at_timestamp(Pointer temp, int t);

	Pointer temporal_at_timestampset(Pointer temp, Pointer ts);

	Pointer temporal_minus_max(Pointer temp);

	Pointer temporal_minus_min(Pointer temp);

	Pointer temporal_minus_period(Pointer temp, Pointer p);

	Pointer temporal_minus_periodset(Pointer temp, Pointer ps);

	Pointer temporal_minus_timestamp(Pointer temp, int t);

	Pointer temporal_minus_timestampset(Pointer temp, Pointer ts);

	Pointer tfloat_at_value(Pointer temp, double d);

	Pointer tfloat_at_values(Pointer temp, Pointer values, int count);

	Pointer tfloat_minus_value(Pointer temp, double d);

	Pointer tfloat_minus_values(Pointer temp, Pointer values, int count);

	boolean tfloat_value_at_timestamp(Pointer temp, int t, boolean strict, Pointer value);

	Pointer tint_at_value(Pointer temp, int i);

	Pointer tint_at_values(Pointer temp, Pointer values, int count);

	Pointer tint_minus_value(Pointer temp, int i);

	Pointer tint_minus_values(Pointer temp, Pointer values, int count);

	boolean tint_value_at_timestamp(Pointer temp, int t, boolean strict, Pointer value);

	Pointer tnumber_at_span(Pointer temp, Pointer span);

	Pointer tnumber_at_spans(Pointer temp, Pointer[] spans, int count);

	Pointer tnumber_at_tbox(Pointer temp, Pointer box);

	Pointer tnumber_minus_span(Pointer temp, Pointer span);

	Pointer tnumber_minus_spans(Pointer temp, Pointer[] spans, int count);

	Pointer tnumber_minus_tbox(Pointer temp, Pointer box);

	Pointer tpoint_at_geometry(Pointer temp, Pointer gs);

	Pointer tpoint_at_stbox(Pointer temp, Pointer box);

	Pointer tpoint_at_value(Pointer temp, Pointer gs);

	Pointer tpoint_at_values(Pointer temp, Pointer[] values, int count);

	Pointer tpoint_minus_geometry(Pointer temp, Pointer gs);

	Pointer tpoint_minus_stbox(Pointer temp, Pointer box);

	Pointer tpoint_minus_value(Pointer temp, Pointer gs);

	Pointer tpoint_minus_values(Pointer temp, Pointer[] values, int count);

	boolean tpoint_value_at_timestamp(Pointer temp, int t, boolean strict, Pointer[] value);

	Pointer ttext_at_value(Pointer temp, Pointer txt);

	Pointer ttext_at_values(Pointer temp, Pointer[] values, int count);

	Pointer ttext_minus_value(Pointer temp, Pointer txt);

	Pointer ttext_minus_values(Pointer temp, Pointer[] values, int count);

	boolean ttext_value_at_timestamp(Pointer temp, int t, boolean strict, Pointer[] value);

	Pointer tand_bool_tbool(boolean b, Pointer temp);

	Pointer tand_tbool_bool(Pointer temp, boolean b);

	Pointer tand_tbool_tbool(Pointer temp1, Pointer temp2);

	Pointer tnot_tbool(Pointer temp);

	Pointer tor_bool_tbool(boolean b, Pointer temp);

	Pointer tor_tbool_bool(Pointer temp, boolean b);

	Pointer tor_tbool_tbool(Pointer temp1, Pointer temp2);

	Pointer tbool_when_true(Pointer temp);

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

	Pointer tfloat_degrees(Pointer temp);

	Pointer tfloat_radians(Pointer temp);

	Pointer tfloat_derivative(Pointer temp);

	Pointer textcat_text_ttext(Pointer txt, Pointer temp);

	Pointer textcat_ttext_text(Pointer temp, Pointer txt);

	Pointer textcat_ttext_ttext(Pointer temp1, Pointer temp2);

	Pointer ttext_upper(Pointer temp);

	Pointer ttext_lower(Pointer temp);

	boolean adjacent_float_tfloat(double d, Pointer tnumber);

	boolean adjacent_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean adjacent_int_tint(int i, Pointer tnumber);

	boolean adjacent_period_temporal(Pointer p, Pointer temp);

	boolean adjacent_periodset_temporal(Pointer ps, Pointer temp);

	boolean adjacent_span_tnumber(Pointer span, Pointer tnumber);

	boolean adjacent_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean adjacent_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean adjacent_temporal_period(Pointer temp, Pointer p);

	boolean adjacent_temporal_periodset(Pointer temp, Pointer ps);

	boolean adjacent_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean adjacent_temporal_timestamp(Pointer temp, int t);

	boolean adjacent_temporal_timestampset(Pointer temp, Pointer ts);

	boolean adjacent_tfloat_float(Pointer tnumber, double d);

	boolean adjacent_timestamp_temporal(int t, Pointer temp);

	boolean adjacent_timestampset_temporal(Pointer ts, Pointer temp);

	boolean adjacent_tint_int(Pointer tnumber, int i);

	boolean adjacent_tnumber_span(Pointer tnumber, Pointer span);

	boolean adjacent_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean adjacent_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean adjacent_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean adjacent_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean adjacent_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean contained_float_tfloat(double d, Pointer tnumber);

	boolean contained_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean contained_int_tint(int i, Pointer tnumber);

	boolean contained_period_temporal(Pointer p, Pointer temp);

	boolean contained_periodset_temporal(Pointer ps, Pointer temp);

	boolean contained_span_tnumber(Pointer span, Pointer tnumber);

	boolean contained_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean contained_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean contained_temporal_period(Pointer temp, Pointer p);

	boolean contained_temporal_periodset(Pointer temp, Pointer ps);

	boolean contained_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean contained_temporal_timestamp(Pointer temp, int t);

	boolean contained_temporal_timestampset(Pointer temp, Pointer ts);

	boolean contained_tfloat_float(Pointer tnumber, double d);

	boolean contained_timestamp_temporal(int t, Pointer temp);

	boolean contained_timestampset_temporal(Pointer ts, Pointer temp);

	boolean contained_tint_int(Pointer tnumber, int i);

	boolean contained_tnumber_span(Pointer tnumber, Pointer span);

	boolean contained_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean contained_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean contained_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean contained_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean contained_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean contains_bbox_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean contains_float_tfloat(double d, Pointer tnumber);

	boolean contains_int_tint(int i, Pointer tnumber);

	boolean contains_period_temporal(Pointer p, Pointer temp);

	boolean contains_periodset_temporal(Pointer ps, Pointer temp);

	boolean contains_span_tnumber(Pointer span, Pointer tnumber);

	boolean contains_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean contains_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean contains_temporal_period(Pointer temp, Pointer p);

	boolean contains_temporal_periodset(Pointer temp, Pointer ps);

	boolean contains_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean contains_temporal_timestamp(Pointer temp, int t);

	boolean contains_temporal_timestampset(Pointer temp, Pointer ts);

	boolean contains_tfloat_float(Pointer tnumber, double d);

	boolean contains_timestamp_temporal(int t, Pointer temp);

	boolean contains_timestampset_temporal(Pointer ts, Pointer temp);

	boolean contains_tint_int(Pointer tnumber, int i);

	boolean contains_tnumber_span(Pointer tnumber, Pointer span);

	boolean contains_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean contains_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean contains_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean contains_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean contains_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean left_float_tfloat(double d, Pointer tnumber);

	boolean left_int_tint(int i, Pointer tnumber);

	boolean left_tfloat_float(Pointer tnumber, double d);

	boolean left_tint_int(Pointer tnumber, int i);

	boolean overlaps_float_tfloat(double d, Pointer tnumber);

	boolean overlaps_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean overlaps_int_tint(int i, Pointer tnumber);

	boolean overlaps_period_temporal(Pointer p, Pointer temp);

	boolean overlaps_periodset_temporal(Pointer ps, Pointer temp);

	boolean overlaps_span_tnumber(Pointer span, Pointer tnumber);

	boolean overlaps_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overlaps_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean overlaps_temporal_period(Pointer temp, Pointer p);

	boolean overlaps_temporal_periodset(Pointer temp, Pointer ps);

	boolean overlaps_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean overlaps_temporal_timestamp(Pointer temp, int t);

	boolean overlaps_temporal_timestampset(Pointer temp, Pointer ts);

	boolean overlaps_tfloat_float(Pointer tnumber, double d);

	boolean overlaps_timestamp_temporal(int t, Pointer temp);

	boolean overlaps_timestampset_temporal(Pointer ts, Pointer temp);

	boolean overlaps_tint_int(Pointer tnumber, int i);

	boolean overlaps_tnumber_span(Pointer tnumber, Pointer span);

	boolean overlaps_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean overlaps_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean overlaps_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean overlaps_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overlaps_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overleft_float_tfloat(double d, Pointer tnumber);

	boolean overleft_int_tint(int i, Pointer tnumber);

	boolean overleft_tfloat_float(Pointer tnumber, double d);

	boolean overleft_tint_int(Pointer tnumber, int i);

	boolean overright_float_tfloat(double d, Pointer tnumber);

	boolean overright_int_tint(int i, Pointer tnumber);

	boolean overright_tfloat_float(Pointer tnumber, double d);

	boolean overright_tint_int(Pointer tnumber, int i);

	boolean right_float_tfloat(double d, Pointer tnumber);

	boolean right_int_tint(int i, Pointer tnumber);

	boolean right_tfloat_float(Pointer tnumber, double d);

	boolean right_tint_int(Pointer tnumber, int i);

	boolean same_float_tfloat(double d, Pointer tnumber);

	boolean same_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean same_int_tint(int i, Pointer tnumber);

	boolean same_period_temporal(Pointer p, Pointer temp);

	boolean same_periodset_temporal(Pointer ps, Pointer temp);

	boolean same_span_tnumber(Pointer span, Pointer tnumber);

	boolean same_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean same_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean same_temporal_period(Pointer temp, Pointer p);

	boolean same_temporal_periodset(Pointer temp, Pointer ps);

	boolean same_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean same_temporal_timestamp(Pointer temp, int t);

	boolean same_temporal_timestampset(Pointer temp, Pointer ts);

	boolean same_tfloat_float(Pointer tnumber, double d);

	boolean same_timestamp_temporal(int t, Pointer temp);

	boolean same_timestampset_temporal(Pointer ts, Pointer temp);

	boolean same_tint_int(Pointer tnumber, int i);

	boolean same_tnumber_span(Pointer tnumber, Pointer span);

	boolean same_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean same_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean same_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean same_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean same_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean above_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean above_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean above_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean above_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean above_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean after_period_temporal(Pointer p, Pointer temp);

	boolean after_periodset_temporal(Pointer ps, Pointer temp);

	boolean after_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean after_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean after_temporal_period(Pointer temp, Pointer p);

	boolean after_temporal_periodset(Pointer temp, Pointer ps);

	boolean after_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean after_temporal_timestamp(Pointer temp, int t);

	boolean after_temporal_timestampset(Pointer temp, Pointer ts);

	boolean after_timestamp_temporal(int t, Pointer temp);

	boolean after_timestampset_temporal(Pointer ts, Pointer temp);

	boolean after_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean after_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean after_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean after_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean back_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean back_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean back_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean back_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean back_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean before_period_temporal(Pointer p, Pointer temp);

	boolean before_periodset_temporal(Pointer ps, Pointer temp);

	boolean before_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean before_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean before_temporal_period(Pointer temp, Pointer p);

	boolean before_temporal_periodset(Pointer temp, Pointer ps);

	boolean before_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean before_temporal_timestamp(Pointer temp, int t);

	boolean before_temporal_timestampset(Pointer temp, Pointer ts);

	boolean before_timestamp_temporal(int t, Pointer temp);

	boolean before_timestampset_temporal(Pointer ts, Pointer temp);

	boolean before_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean before_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean before_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean before_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean below_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean below_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean below_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean below_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean below_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean front_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean front_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean front_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean front_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean front_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean left_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean left_span_tnumber(Pointer span, Pointer tnumber);

	boolean left_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean left_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean left_tnumber_span(Pointer tnumber, Pointer span);

	boolean left_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean left_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean left_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean left_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean left_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overabove_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean overabove_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overabove_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean overabove_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overabove_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overafter_period_temporal(Pointer p, Pointer temp);

	boolean overafter_periodset_temporal(Pointer ps, Pointer temp);

	boolean overafter_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overafter_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean overafter_temporal_period(Pointer temp, Pointer p);

	boolean overafter_temporal_periodset(Pointer temp, Pointer ps);

	boolean overafter_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean overafter_temporal_timestamp(Pointer temp, int t);

	boolean overafter_temporal_timestampset(Pointer temp, Pointer ts);

	boolean overafter_timestamp_temporal(int t, Pointer temp);

	boolean overafter_timestampset_temporal(Pointer ts, Pointer temp);

	boolean overafter_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean overafter_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean overafter_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overafter_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overback_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean overback_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overback_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean overback_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overback_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overbefore_period_temporal(Pointer p, Pointer temp);

	boolean overbefore_periodset_temporal(Pointer ps, Pointer temp);

	boolean overbefore_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overbefore_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean overbefore_temporal_period(Pointer temp, Pointer p);

	boolean overbefore_temporal_periodset(Pointer temp, Pointer ps);

	boolean overbefore_temporal_temporal(Pointer temp1, Pointer temp2);

	boolean overbefore_temporal_timestamp(Pointer temp, int t);

	boolean overbefore_temporal_timestampset(Pointer temp, Pointer ts);

	boolean overbefore_timestamp_temporal(int t, Pointer temp);

	boolean overbefore_timestampset_temporal(Pointer ts, Pointer temp);

	boolean overbefore_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean overbefore_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean overbefore_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overbefore_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overbelow_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean overbelow_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overbelow_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean overbelow_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overbelow_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overfront_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean overfront_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overfront_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean overfront_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overfront_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overleft_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean overleft_span_tnumber(Pointer span, Pointer tnumber);

	boolean overleft_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overleft_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean overleft_tnumber_span(Pointer tnumber, Pointer span);

	boolean overleft_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean overleft_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean overleft_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean overleft_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overleft_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean overright_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean overright_span_tnumber(Pointer span, Pointer tnumber);

	boolean overright_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean overright_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean overright_tnumber_span(Pointer tnumber, Pointer span);

	boolean overright_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean overright_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean overright_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean overright_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean overright_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	boolean right_geo_tpoint(Pointer geo, Pointer tpoint);

	boolean right_span_tnumber(Pointer span, Pointer tnumber);

	boolean right_stbox_tpoint(Pointer stbox, Pointer tpoint);

	boolean right_tbox_tnumber(Pointer tbox, Pointer tnumber);

	boolean right_tnumber_span(Pointer tnumber, Pointer span);

	boolean right_tnumber_tbox(Pointer tnumber, Pointer tbox);

	boolean right_tnumber_tnumber(Pointer tnumber1, Pointer tnumber2);

	boolean right_tpoint_geo(Pointer tpoint, Pointer geo);

	boolean right_tpoint_stbox(Pointer tpoint, Pointer stbox);

	boolean right_tpoint_tpoint(Pointer tpoint1, Pointer tpoint2);

	Pointer distance_tfloat_float(Pointer temp, double d);

	Pointer distance_tint_int(Pointer temp, int i);

	Pointer distance_tnumber_tnumber(Pointer temp1, Pointer temp2);

	Pointer distance_tpoint_geo(Pointer temp, Pointer geo);

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

	boolean shortestline_tpoint_geo(Pointer temp, Pointer gs, Pointer[] result);

	boolean shortestline_tpoint_tpoint(Pointer temp1, Pointer temp2, Pointer[] result);

	boolean tbool_always_eq(Pointer temp, boolean b);

	boolean tbool_ever_eq(Pointer temp, boolean b);

	boolean tfloat_always_eq(Pointer temp, double d);

	boolean tfloat_always_le(Pointer temp, double d);

	boolean tfloat_always_lt(Pointer temp, double d);

	boolean tfloat_ever_eq(Pointer temp, double d);

	boolean tfloat_ever_le(Pointer temp, double d);

	boolean tfloat_ever_lt(Pointer temp, double d);

	boolean tgeogpoint_always_eq(Pointer temp, Pointer gs);

	boolean tgeogpoint_ever_eq(Pointer temp, Pointer gs);

	boolean tgeompoint_always_eq(Pointer temp, Pointer gs);

	boolean tgeompoint_ever_eq(Pointer temp, Pointer gs);

	boolean tint_always_eq(Pointer temp, int i);

	boolean tint_always_le(Pointer temp, int i);

	boolean tint_always_lt(Pointer temp, int i);

	boolean tint_ever_eq(Pointer temp, int i);

	boolean tint_ever_le(Pointer temp, int i);

	boolean tint_ever_lt(Pointer temp, int i);

	boolean ttext_always_eq(Pointer temp, Pointer txt);

	boolean ttext_always_le(Pointer temp, Pointer txt);

	boolean ttext_always_lt(Pointer temp, Pointer txt);

	boolean ttext_ever_eq(Pointer temp, Pointer txt);

	boolean ttext_ever_le(Pointer temp, Pointer txt);

	boolean ttext_ever_lt(Pointer temp, Pointer txt);

	int temporal_cmp(Pointer temp1, Pointer temp2);

	boolean temporal_eq(Pointer temp1, Pointer temp2);

	boolean temporal_ge(Pointer temp1, Pointer temp2);

	boolean temporal_gt(Pointer temp1, Pointer temp2);

	boolean temporal_le(Pointer temp1, Pointer temp2);

	boolean temporal_lt(Pointer temp1, Pointer temp2);

	boolean temporal_ne(Pointer temp1, Pointer temp2);

	Pointer teq_bool_tbool(boolean b, Pointer temp);

	Pointer teq_float_tfloat(double d, Pointer temp);

	Pointer teq_geo_tpoint(Pointer geo, Pointer tpoint);

	Pointer teq_int_tint(int i, Pointer temp);

	Pointer teq_point_tgeogpoint(Pointer gs, Pointer temp);

	Pointer teq_point_tgeompoint(Pointer gs, Pointer temp);

	Pointer teq_tbool_bool(Pointer temp, boolean b);

	Pointer teq_temporal_temporal(Pointer temp1, Pointer temp2);

	Pointer teq_text_ttext(Pointer txt, Pointer temp);

	Pointer teq_tfloat_float(Pointer temp, double d);

	Pointer teq_tgeogpoint_point(Pointer temp, Pointer gs);

	Pointer teq_tgeompoint_point(Pointer temp, Pointer gs);

	Pointer teq_tint_int(Pointer temp, int i);

	Pointer teq_tpoint_geo(Pointer tpoint, Pointer geo);

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

	Pointer tne_geo_tpoint(Pointer geo, Pointer tpoint);

	Pointer tne_int_tint(int i, Pointer temp);

	Pointer tne_point_tgeogpoint(Pointer gs, Pointer temp);

	Pointer tne_point_tgeompoint(Pointer gs, Pointer temp);

	Pointer tne_tbool_bool(Pointer temp, boolean b);

	Pointer tne_temporal_temporal(Pointer temp1, Pointer temp2);

	Pointer tne_text_ttext(Pointer txt, Pointer temp);

	Pointer tne_tfloat_float(Pointer temp, double d);

	Pointer tne_tgeogpoint_point(Pointer temp, Pointer gs);

	Pointer tne_tgeompoint_point(Pointer temp, Pointer gs);

	Pointer tne_tint_int(Pointer temp, int i);

	Pointer tne_tpoint_geo(Pointer tpoint, Pointer geo);

	Pointer tne_ttext_text(Pointer temp, Pointer txt);

	boolean bearing_point_point(Pointer geo1, Pointer geo2, Pointer result);

	Pointer bearing_tpoint_point(Pointer temp, Pointer gs, boolean invert);

	Pointer bearing_tpoint_tpoint(Pointer temp1, Pointer temp2);

	Pointer tpoint_azimuth(Pointer temp);

	Pointer tpoint_cumulative_length(Pointer temp);

	Pointer tpoint_get_coord(Pointer temp, int coord);

	boolean tpoint_is_simple(Pointer temp);

	double tpoint_length(Pointer temp);

	Pointer tpoint_speed(Pointer temp);

	int tpoint_srid(Pointer temp);

	Pointer tpoint_stboxes(Pointer temp, Pointer count);

	Pointer tpoint_trajectory(Pointer temp);

	Pointer geo_expand_spatial(Pointer gs, double d);

	Pointer tgeompoint_tgeogpoint(Pointer temp, boolean oper);

	Pointer tpoint_expand_spatial(Pointer temp, double d);

	Pointer[] tpoint_make_simple(Pointer temp, Pointer count);

	Pointer tpoint_set_srid(Pointer temp, int srid);

	int contains_geo_tpoint(Pointer geo, Pointer temp);

	int disjoint_tpoint_geo(Pointer temp, Pointer gs);

	int disjoint_tpoint_tpoint(Pointer temp1, Pointer temp2);

	int dwithin_tpoint_geo(Pointer temp, Pointer gs, double dist);

	int dwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist);

	int intersects_tpoint_geo(Pointer temp, Pointer gs);

	int intersects_tpoint_tpoint(Pointer temp1, Pointer temp2);

	Pointer tcontains_geo_tpoint(Pointer gs, Pointer temp, boolean restr, boolean atvalue);

	Pointer tdisjoint_tpoint_geo(Pointer temp, Pointer geo, boolean restr, boolean atvalue);

	Pointer tdwithin_tpoint_geo(Pointer temp, Pointer gs, double dist, boolean restr, boolean atvalue);

	Pointer tdwithin_tpoint_tpoint(Pointer temp1, Pointer temp2, double dist, boolean restr, boolean atvalue);

	Pointer tintersects_tpoint_geo(Pointer temp, Pointer geo, boolean restr, boolean atvalue);

	int touches_tpoint_geo(Pointer temp, Pointer gs);

	Pointer ttouches_tpoint_geo(Pointer temp, Pointer gs, boolean restr, boolean atvalue);

	Pointer temporal_insert(Pointer temp1, Pointer temp2, boolean connect);

	Pointer temporal_update(Pointer temp1, Pointer temp2, boolean connect);

	Pointer temporal_delete_timestamp(Pointer temp, int t, boolean connect);

	Pointer temporal_delete_timestampset(Pointer temp, Pointer ts, boolean connect);

	Pointer temporal_delete_period(Pointer temp, Pointer p, boolean connect);

	Pointer temporal_delete_periodset(Pointer temp, Pointer ps, boolean connect);

	boolean temporal_intersects_period(Pointer temp, Pointer p);

	boolean temporal_intersects_periodset(Pointer temp, Pointer ps);

	boolean temporal_intersects_timestamp(Pointer temp, int t);

	boolean temporal_intersects_timestampset(Pointer temp, Pointer ss);

	double tnumber_integral(Pointer temp);

	double tnumber_twavg(Pointer temp);

	Pointer tpoint_twcentroid(Pointer temp);

	void skiplist_free(Pointer list);

	Pointer temporal_extent_transfn(Pointer p, Pointer temp);

	Pointer tnumber_extent_transfn(Pointer box, Pointer temp);

	Pointer tpoint_extent_transfn(Pointer box, Pointer temp);

	Pointer temporal_tcount_transfn(Pointer state, Pointer temp, Pointer interval, int origin);

	Pointer tbool_tand_transfn(Pointer state, Pointer temp);

	Pointer tbool_tor_transfn(Pointer state, Pointer temp);

	Pointer tint_tmin_transfn(Pointer state, Pointer temp);

	Pointer tfloat_tmin_transfn(Pointer state, Pointer temp);

	Pointer tint_tmax_transfn(Pointer state, Pointer temp);

	Pointer tfloat_tmax_transfn(Pointer state, Pointer temp);

	Pointer tint_tsum_transfn(Pointer state, Pointer temp);

	Pointer tfloat_tsum_transfn(Pointer state, Pointer temp);

	Pointer tnumber_tavg_transfn(Pointer state, Pointer temp);

	Pointer ttext_tmin_transfn(Pointer state, Pointer temp);

	Pointer ttext_tmax_transfn(Pointer state, Pointer temp);

	Pointer temporal_tagg_finalfn(Pointer state);

	Pointer tnumber_tavg_finalfn(Pointer state);

	int int_bucket(int value, int size, int origin);

	double float_bucket(double value, double size, double origin);

	int timestamptz_bucket(int timestamp, Pointer duration, int origin);

	Pointer intspan_bucket_list(Pointer bounds, int size, int origin, Pointer newcount);

	Pointer floatspan_bucket_list(Pointer bounds, double size, double origin, Pointer newcount);

	Pointer period_bucket_list(Pointer bounds, Pointer duration, int origin, Pointer newcount);

	Pointer tbox_tile_list(Pointer bounds, double xsize, Pointer duration, double xorigin, int torigin, Pointer rows, Pointer columns);

	Pointer[] tint_value_split(Pointer temp, int size, int origin, Pointer newcount);

	Pointer[] tfloat_value_split(Pointer temp, double size, double origin, Pointer newcount);

	Pointer[] temporal_time_split(Pointer temp, Pointer duration, int torigin, Pointer newcount);

	Pointer[] tint_value_time_split(Pointer temp, int size, int vorigin, Pointer duration, int torigin, Pointer newcount);

	Pointer[] tfloat_value_time_split(Pointer temp, double size, double vorigin, Pointer duration, int torigin, Pointer newcount);

	Pointer stbox_tile_list(Pointer bounds, double size, Pointer duration, Pointer sorigin, int torigin, Pointer[] cellcount);

	double temporal_frechet_distance(Pointer temp1, Pointer temp2);

	double temporal_dyntimewarp_distance(Pointer temp1, Pointer temp2);

	Pointer temporal_frechet_path(Pointer temp1, Pointer temp2, Pointer count);

	Pointer temporal_dyntimewarp_path(Pointer temp1, Pointer temp2, Pointer count);

	Pointer geo_to_tpoint(Pointer geo);

	Pointer temporal_simplify(Pointer temp, double eps_dist, boolean synchronized);

	boolean tpoint_to_geo_measure(Pointer tpoint, Pointer measure, boolean segmentize, Pointer[] result);

}