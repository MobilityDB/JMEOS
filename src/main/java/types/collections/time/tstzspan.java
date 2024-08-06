package types.collections.time;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.boxes.Box;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import types.collections.base.Base;
import types.collections.base.Span;
import types.temporal.Temporal;
import utils.ConversionUtils;
import functions.functions;

import javax.naming.OperationNotSupportedException;

/**
 * Class for representing sets of contiguous timestamps between a lower and
 * an upper bound. The bounds may be inclusive or not.
 * <p>
 * ``tstzspan`` objects can be created with a single argument of type string
 * as in MobilityDB.
 * <p>
 * >>> new tstzspan("(2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01)")
 * <p>
 * Another possibility is to provide the ``lower`` and ``upper`` named parameters (of type str or datetime), and
 * optionally indicate whether the bounds are inclusive or exclusive (by default, the lower bound is inclusive and the
 * upper is exclusive):
 * <p>
 * >>> new tstzspan(lower='2019-09-08 00:00:00+01', upper='2019-09-10 00:00:00+01')
 * >>> new tstzspan(lower='2019-09-08 00:00:00+01', upper='2019-09-10 00:00:00+01', lower_inc=False, upper_inc=True)
 * >>> new tstzspan(lower=parse('2019-09-08 00:00:00+01'), upper=parse('2019-09-10 00:00:00+01'), upper_inc=True)
 * <p>
 * @author ARIJIT SAMAL
 *
 */
public class tstzspan extends Span<LocalDateTime> implements Time, TimeCollection{
	private static final String LOWER_INCLUSIVE = "[";
	private static final String LOWER_EXCLUSIVE = "(";
	private static final String UPPER_INCLUSIVE = "]";
	private static final String UPPER_EXCLUSIVE = ")";
	private LocalDateTime lower;
	private LocalDateTime upper;
	private boolean lowerInclusive = true;
	private boolean upperInclusive = false;
	private Pointer _inner;

	/*------------------------ Constructors ------------------------*/


	/**
	 * The default constructor
	 */
	public tstzspan() {
		super();
	}
	
	/**
	 * Constructor by copy
	 *
	 * @param other period instace
	 */
	public tstzspan(tstzspan other){
		this(other.get_inner());
	}
	
	/**
	 * Constructor through Meos (C) inner object
	 *
	 * @param _inner Pointer to C object
	 */
	public tstzspan(Pointer _inner) {
		super(_inner);
		this._inner = _inner;
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - a string with a tstzspan value
	 */
	public tstzspan(final String value){
		super(value);
		this._inner = functions.tstzspan_in(value);
	}



	/**
	 * The timestamps without bounds constructor
	 *
	 * @param lower          - a timestamp for the lower bound
	 * @param upper          - a timestamp for the upper bound
	 */
	public tstzspan(String lower, String upper) {
		super(lower,upper,true,false);
		this.lowerInclusive = true;
		this.upperInclusive = false;
		OffsetDateTime lower_ts = functions.pg_timestamptz_in(lower, -1);
		OffsetDateTime upper_ts = functions.pg_timestamptz_in(upper, -1);
		this._inner = functions.tstzspan_make(lower_ts, upper_ts, this.lowerInclusive, this.upperInclusive);
	}
	
	/**
	 * The timestamps and bounds constructor
	 *
	 * @param lower          - a timestamp for the lower bound
	 * @param upper          - a timestamp for the upper bound
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 */
	public tstzspan(String lower, String upper, boolean lowerInclusive, boolean upperInclusive) {
		super(lower,upper,lowerInclusive,upperInclusive);
		OffsetDateTime lower_ts = functions.pg_timestamptz_in(lower, -1);
		OffsetDateTime upper_ts = functions.pg_timestamptz_in(upper, -1);
		this._inner = functions.tstzspan_make(lower_ts, upper_ts, lowerInclusive, upperInclusive);
	}


	/**
	 * The timestamps without bounds constructor
	 *
	 * @param lower          - a timestamp for the lower bound
	 * @param upper          - a timestamp for the upper bound
	 */
	public tstzspan(LocalDateTime lower, LocalDateTime upper) {
		super(lower.toString(),upper.toString(),true,false);
		this.lowerInclusive = true;
		this.upperInclusive = false;
		OffsetDateTime lower_ts = ConversionUtils.datetimeToTimestampTz(lower);
		OffsetDateTime upper_ts = ConversionUtils.datetimeToTimestampTz(upper);
		this._inner = functions.tstzspan_make(lower_ts, upper_ts, this.lowerInclusive, this.upperInclusive);
	}


	/**
	 * The timestamps and bounds constructor
	 *
	 * @param lower          - a timestamp for the lower bound
	 * @param upper          - a timestamp for the upper bound
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 */
	public tstzspan(LocalDateTime lower, LocalDateTime upper, boolean lowerInclusive, boolean upperInclusive) {
		super(lower.toString(),upper.toString(),lowerInclusive,upperInclusive);
		OffsetDateTime lower_ts = ConversionUtils.datetimeToTimestampTz(lower);
		OffsetDateTime upper_ts = ConversionUtils.datetimeToTimestampTz(upper);
		this._inner = functions.tstzspan_make(lower_ts, upper_ts, this.lowerInclusive, this.upperInclusive);
	}


	/**
	 * The timestamps without bounds constructor
	 *
	 * @param lower          - a timestamp for the lower bound
	 * @param upper          - a timestamp for the upper bound
	 */
	public tstzspan(String lower, LocalDateTime upper) {
		super(lower,upper.toString(),true,false);
		this.lowerInclusive = true;
		this.upperInclusive = false;
		OffsetDateTime lower_ts = functions.pg_timestamptz_in(lower,-1);
		OffsetDateTime upper_ts = ConversionUtils.datetimeToTimestampTz(upper);
		this._inner = functions.tstzspan_make(lower_ts, upper_ts, this.lowerInclusive, this.upperInclusive);
	}

	/**
	 * The timestamps without bounds constructor
	 *
	 * @param lower          - a timestamp for the lower bound
	 * @param upper          - a timestamp for the upper bound
	 */
	public tstzspan(LocalDateTime lower, String upper){
		super(lower.toString(),upper,true,false);
		this.lowerInclusive = true;
		this.upperInclusive = false;
		OffsetDateTime lower_ts = ConversionUtils.datetimeToTimestampTz(lower);
		OffsetDateTime upper_ts = functions.pg_timestamptz_in(upper,-1);
		this._inner = functions.tstzspan_make(lower_ts, upper_ts, this.lowerInclusive, this.upperInclusive);
	}


	@Override
	public Pointer createStringInner(String str){
		return functions.tstzspan_in(str);
	}

	@Override
	public Pointer createInner(Pointer inner){
		return _inner;
	}

	@Override
	public Pointer createIntInt(java.lang.Number lower, java.lang.Number upper, boolean lower_inc, boolean upper_inc){
		return functions.intspan_make(lower.intValue(),upper.intValue(),lower_inc,upper_inc);
	}
	@Override
	public Pointer createIntStr(java.lang.Number lower, String upper, boolean lower_inc, boolean upper_inc){
		int new_upper = Integer.parseInt(upper);
		return functions.intspan_make(lower.intValue(),new_upper,lower_inc,upper_inc);
	}
	@Override
	public Pointer createStrStr(String lower, String upper, boolean lower_inc, boolean upper_inc){
		return functions.tstzspan_make(functions.pg_timestamptz_in(lower,-1),functions.pg_timestamptz_in(upper,-1),lower_inc,upper_inc);
	}
	@Override
	public Pointer createStrInt(String lower, java.lang.Number upper, boolean lower_inc, boolean upper_inc){
		int new_lower = Integer.parseInt(lower);
		return functions.intspan_make(new_lower,upper.intValue(),lower_inc,upper_inc);
	}
	@Override
	public Pointer createIntIntNb(java.lang.Number lower, java.lang.Number upper){
		return functions.intspan_make(lower.intValue(),upper.intValue(),true,false);
	}


	/**
	 * Return a copy of "this" object.
	 * <p>
	 * 			MEOS Functions:
	 * 		    <ul>
	 *             <li>span_copy</li>
	 *          </ul>
	 * </p>
	 * @return Instance of tstzspan class
	 */
    public tstzspan copy(){
        return new tstzspan(functions.span_copy(this._inner));
    }


	/**
	 * Returns a tstzspan from its WKB representation in hex-encoded ASCII.
	 *	<p>
	 *		MEOS Functions:
	 *			<ul>
	 *             <li>span_from_hexwkb</li>
	 *          </ul>
	 *  </p>
	 *
	 * @param hexwkb: WKB representation in hex-encoded ASCII
	 * @return Instance of tstzspan class
	 */
	public static tstzspan from_hexwkb(String hexwkb) {
		Pointer result = functions.span_from_hexwkb(hexwkb);
		return new tstzspan(result);
	}


	/*------------------------ Output ------------------------*/


	/**
	 * Return the string representation of the content of "this" object.
	 * <p>
	 *		MEOS Functions:
	 *			<ul>
	 *             <li>period_out</li>
	 *          </ul>
	 *  </p>
	 *
	 * @return string instance
	 */
	public String toString(){
		return functions.tstzspan_out(this._inner);
	}


	/*------------------------ Conversions ------------------------*/


	/**
	 * Returns a period set containing "this" object.
	 *	<p>
	 *		MEOS Functions:
	 *			<ul>
	 *             <li>span_to_spanset</li>
	 *          </ul>
	 *  </p>
	 * @return tstzspanset instance
	 */
	public tstzspanset to_spanset(){
		return new tstzspanset(functions.span_to_spanset(functions.span_to_spanset(this._inner)));
	}


	/**
	 * Returns a period set containing "this" object.
	 *	<p>
	 *		MEOS Functions:
	 *			<ul>
	 *             <li>span_to_spanset</li>
	 *          </ul>
	 *  </p>
	 * @return tstzspanset instance
	 */
	public tstzspanset to_periodset() {
		return new tstzspanset(functions.span_to_spanset(this._inner));
	}



	/*------------------------ Accessors ------------------------*/

	@Override
	public Pointer get_inner(){
		return _inner;
	}



	/**
	 * Returns whether the lower bound of the period is inclusive or not
	 * <p>
	 * 		MEOS Functions:
	 * 			<ul>
	 *             <li>span_lower_inc</li>
	 *          </ul>
	 * </p>
	 * @return true if the lower bound of the period is inclusive and false otherwise
	 */
	public boolean lower_inc(){
		return functions.span_lower_inc(this._inner);
	}

	/**
	 * Returns whether the upper bound of the period is inclusive or not
	 * <p>
	 * 		MEOS Functions:
	 *             <li>span_upper_inc</li>
	 * </p>
	 * @return True if the upper bound of the period is inclusive and False otherwise
	 */
	public boolean upper_inc(){
		return functions.span_upper_inc(this._inner);
	}


	/**
	 * Returns the lower bound of a period
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>period_lower</li>
	 * @return The lower bound of the period as a {@link LocalDateTime}
	 */
	public LocalDateTime lower() {
		return ConversionUtils.timestamptz_to_datetime(functions.tstzspan_lower(this._inner));
	}


	/**
	 * Returns the upper bound of a period
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>period_upper</li>
	 * @return The upper bound of the period as a {@link LocalDateTime}
	 */
	public LocalDateTime upper() {
		return ConversionUtils.timestamptz_to_datetime(functions.tstzspan_upper(this._inner));
	}


	/**
	 *  Returns the duration of the period.
	 *	<p>
	 *	MEOS Functions:
	 *             <li>period_duration</li>
	 *  </p>
	 * @return timedelta instance representing the duration of the period
	 */
	public Duration duration(){
		return ConversionUtils.interval_to_timedelta(functions.tstzspan_duration(this._inner));
	}


	/**
	 * Returns the duration of the period.
	 *<p>
	 * MEOS Functions:
	 *             <li>span_width</li>
	 * </p>
	 * @return a float representing the duration of the period in seconds
	 */
	public float duration_in_second(){
		return (float) functions.floatspan_width(this._inner);
	}

	/**
	 * Return the hash representation of "this".
	 * <p>
	 * MEOS Functions:
	 *             <li>span_hash</li>
	 * </p>
	 * @return integer instance
	 */
	public long hash(){
		return functions.span_hash(this._inner);
	}


	/*------------------------ Transformations ------------------------*/


	/**
	 * Returns a new period that includes both "this" and "other"
	 * <pre>
	 * Examples:
	 *     >>> tstzspan('[2000-01-01, 2000-01-04)').expand(tstzspan('[2000-01-05, 2000-01-10]'))
	 *     >>> tstzspan([2000-01-01 00:00:00+01, 2000-01-10 00:00:00+01])
	 * </pre>
	 *
	 * 		MEOS Functions:
	 * 			<ul>
	 *             <li>span_expand</li>
	 *          </ul>
	 *
	 * @param other period instance needed to expand the object
	 * @return tstzspan instance
	 */
//	public tstzspan expand(tstzspan other) {
//		Pointer copy = functions.span_copy(this._inner);
//		functions.span_expand(other._inner, copy);
//		return new tstzspan(copy);
//	}


	/*------------------------ Topological Operations ------------------------*/


	/**
	 * Returns whether "this" is temporally adjacent to "other". That is, they share a bound but only one of them
	 *         contains it.
	 * <pre>
	 *         Examples:
	 *             >>> tstzspan('[2012-01-01, 2012-01-02)').is_adjacent(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-01, 2012-01-02]').is_adjacent(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> False  # Both contain bound
	 *             >>> tstzspan('[2012-01-01, 2012-01-02)').is_adjacent(tstzspan('(2012-01-02, 2012-01-03]'))
	 *             >>> False  # Neither contain bound
	 *</pre>
	 *
	 *         MEOS Functions:
	 *         	 <ul>
	 *             <li>adjacent_span_span</li>
	 *             <li>adjacent_span_spanset</li>
	 *             <li>adjacent_period_timestamp</li>
	 *             <li>adjacent_period_timestampset</li>
	 *             <li>adjacent_period_temporal</li>
	 *          </ul>
	 *
	 * @param other temporal object to compare with
	 * @return true if adjacent, false otherwise
	 */
	public boolean is_adjacent(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.adjacent_span_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.adjacent_spanset_span(ps.get_inner(), this._inner);
			//case Time dt -> returnValue = functions.adjacent_period_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt));
			case tstzset ts -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = functions.adjacent_span_span(this._inner,functions.temporal_to_tstzspan(t.getInner()));
			case Box b -> returnValue = functions.adjacent_span_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.is_adjacent((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is temporally contained in "container".
	 * <pre>
	 *         Examples:
	 *             >>> tstzspan('[2012-01-02, 2012-01-03]').is_contained_in(tstzspan('[2012-01-01, 2012-01-04]'))
	 *             >>> True
	 *             >>> tstzspan('(2012-01-01, 2012-01-02)').is_contained_in(tstzspan('[2012-01-01, 2012-01-02]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-01, 2012-01-02]').is_contained_in(tstzspan('(2012-01-01, 2012-01-02)'))
	 *             >>> False
	 *</pre>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>contained_span_span</li>
	 *             <li>contained_span_spanset</li>
	 *             <li>contained_period_temporal</li>
	 *         </ul>
	 *
	 * @param other temporal object to compare with
	 * @return true if contained, false otherwise
	 */
	public boolean is_contained_in(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.contained_span_span(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.contained_span_spanset(this._inner,ps.get_inner());
			case Temporal t -> returnValue = this.is_contained_in((TemporalObject)t.period());
			case Box b -> returnValue = functions.contained_span_span(this._inner,b.to_period().get_inner());
			default -> returnValue = super.is_contained_in((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" temporally contains "other".
	 *	<pre>
	 *         Examples:
	 *             >>> tstzspan('[2012-01-01, 2012-01-04]').contains(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-01, 2012-01-02]').contains(tstzspan('(2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> tstzspan('(2012-01-01, 2012-01-02)').contains(tstzspan('[2012-01-01, 2012-01-02]'))
	 *             >>> False
	 *  </pre>
	 *
	 *         MEOS Functions:
	 *         	   <ul>
	 *             		<li>contains_span_span</li>
	 *             		<li>contains_span_spanset</li>
	 *             		<li>contains_period_timestamp</li>
	 *             		<li>contains_period_timestampset</li>
	 *             		<li>contains_period_temporal</li>
	 *             </ul>
	 *
	 * @param other temporal object to compare with
	 * @return true if contains, false otherwise
	 */
	public boolean contains(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.contains_span_span(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.contains_span_spanset(this._inner,ps.get_inner());
			case tstzset ts -> returnValue = functions.contains_span_span(this._inner,functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = contains((TemporalObject) t.period());
			case Box b -> returnValue = functions.contains_span_span(this._inner,b.to_period().get_inner());
			default -> returnValue = super.contains((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" temporally overlaps "other". That is, both share at least an instant
	 * <pre>
	 *         Examples:
	 *             >>> tstzspan('[2012-01-01, 2012-01-02]').overlaps(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-01, 2012-01-02)').overlaps(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> False
	 *             >>> tstzspan('[2012-01-01, 2012-01-02)').overlaps(tstzspan('(2012-01-02, 2012-01-03]'))
	 *             >>> False
	 *</pre>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>overlaps_span_span</li>
	 *             <li>overlaps_span_spanset</li>
	 *             <li>overlaps_period_timestampset</li>
	 *             <li>overlaps_period_temporal</li>
	 *          </ul>
	 * @param other temporal object to compare with
	 * @return true if overlaps, false otherwise
	 */
	public boolean overlaps(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.overlaps_span_span(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.overlaps_spanset_span(ps.get_inner(),this._inner);
			case tstzset ts -> returnValue = functions.overlaps_span_span(this._inner,functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = this.overlaps((TemporalObject)t.period());
			case Box b -> returnValue = functions.overlaps_span_span(this._inner,b.to_period().get_inner());
			default -> returnValue = super.overlaps((Base) other);
		}

		return returnValue;
	}


	/**
	 * Returns whether "this" and the bounding period of "other" is the same.
	 *
	 * <p>
	 *         MEOS Functions:
	 *         	<ul>
	 *             <li>same_period_temporal</li>
	 *          </ul>
	 * </p>
	 *
	 *
	 * @param other temporal object to compare with
	 * @return true if equal, false otherwise
	 */
	public boolean is_same(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.span_eq(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.span_eq(this._inner,functions.spanset_span(ps.get_inner()));
			case tstzset ts -> returnValue = functions.span_eq(this._inner,functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = this.is_same((TemporalObject)t.period());
			case Box b -> returnValue = functions.span_eq(this._inner,b.to_period().get_inner());
			default -> returnValue = super.is_same((Base) other);
		}
		return returnValue;
	}



	/*------------------------ Position Operations ------------------------*/

	/**
	 * Returns whether "this" is strictly before "other". That is, "this" ends before "other" starts.
	 * <pre>
	 *         Examples:
	 *             >>> tstzspan('[2012-01-01, 2012-01-02)').is_before(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-01, 2012-01-02)').is_before(tstzspan('(2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-01, 2012-01-02]').is_before(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> False
	 *</pre>
	 *
	 *         MEOS Functions:
	 *         	 <ul>
	 *             <li>left_span_span</li>
	 *             <li>left_span_spanset</li>
	 *             <li>before_period_timestamp</li>
	 *             <li>before_period_timestampset</li>
	 *             <li>before_period_temporal</li>
	 *           </ul>
	 *
	 * @param other temporal object to compare with
	 * @return true if before, false otherwise
	 */
	public boolean is_before(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.left_span_span(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.left_span_spanset(this._inner,ps.get_inner());
			case tstzset ts -> returnValue = functions.left_span_span(this._inner,functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = is_before(t.period());
			case Box b -> returnValue = functions.left_span_span(this._inner,b.to_period().get_inner());
			default -> returnValue = super.is_left((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is before "other" allowing overlap. That is, "this" ends before "other" ends (or
	 *         at the same time).
	 * <pre>
	 *         Examples:
	 *             >>> tstzspan('[2012-01-01, 2012-01-02)').is_over_or_before(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-01, 2012-01-02]').is_over_or_before(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-03, 2012-01-05]').is_over_or_before(tstzspan('[2012-01-01, 2012-01-04]'))
	 *             >>> False
	 * </pre>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>overleft_span_span</li>
	 *             <li>overleft_span_spanset</li>
	 *             <li>overbefore_period_timestamp</li>
	 *             <li>overbefore_period_timestampset</li>
	 *             <li>overbefore_period_temporal</li>
	 *			</ul>
	 * @param other temporal object to compare with
	 * @return true if before, false otherwise
	 */
	public boolean is_over_or_before(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.overleft_span_span(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.overleft_span_spanset(this._inner,ps.get_inner());
			case tstzset ts -> returnValue = functions.overleft_span_span(this._inner,functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = is_over_or_before(t.period());
			case Box b -> returnValue = functions.overleft_span_span(this._inner,b.to_period().get_inner());
			default -> returnValue = super.is_over_or_left((Base) other);
		}
		return returnValue;
	}

	/**
	 * Returns whether "this" is strictly after "other". That is, "this" starts after "other" ends.
	 * <pre>
	 *         Examples:
	 *             >>> tstzspan('[2012-01-02, 2012-01-03]').is_after(tstzspan('[2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> tstzspan('(2012-01-02, 2012-01-03]').is_after(tstzspan('[2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-02, 2012-01-03]').is_after(tstzspan('[2012-01-01, 2012-01-02]'))
	 *             >>> False
	 * </pre>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>right_span_span</li>
	 *             <li>right_span_spanset</li>
	 *             <li>after_period_timestamp</li>
	 *             <li>after_period_timestampset</li>
	 *             <li>after_period_temporal</li>
	 *         </ul>
	 *
	 * @param other temporal object to compare with
	 * @return true if after, false otherwise
	 */
	public boolean is_after(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.right_span_span(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.right_span_spanset(this._inner,ps.get_inner());
			case tstzset ts -> returnValue = functions.right_span_span(this._inner,functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = is_after(t.period());
			case Box b -> returnValue = functions.right_span_span(this._inner,b.to_period().get_inner());
			default -> returnValue = super.is_right((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is after "other" allowing overlap. That is, "this" starts after "other" starts
	 *         (or at the same time).
	 *	<pre>
	 *         Examples:
	 *             >>> tstzspan('[2012-01-02, 2012-01-03]').is_over_or_after(tstzspan('[2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-02, 2012-01-03]').is_over_or_after(tstzspan('[2012-01-01, 2012-01-02]'))
	 *             >>> True
	 *             >>> tstzspan('[2012-01-02, 2012-01-03]').is_over_or_after(tstzspan('[2012-01-01, 2012-01-03]'))
	 *             >>> False
	 *  </pre>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>overright_span_span</li>
	 *             <li>overright_span_spanset</li>
	 *             <li>overafter_period_timestamp</li>
	 *             <li>overafter_period_timestampset</li>
	 *             <li>overafter_period_temporal</li>
	 *         </ul>
	 *
	 *
	 * @param other temporal object to compare with
	 * @return true if overlapping or after, false otherwise
	 */
	public boolean is_over_or_after(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.overright_span_span(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.overright_span_spanset(this._inner,ps.get_inner());
			case tstzset ts -> returnValue = functions.overright_span_span(this._inner,functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = is_over_or_after(t.period());
			case Box b -> returnValue = functions.overright_span_span(this._inner,b.to_period().get_inner());
			default -> returnValue = super.is_over_or_right((Base) other);
		}
		return returnValue;
	}


	/*------------------------ Distance Operations ------------------------*/


	/**
	 * Returns the temporal distance between "this" and "other".
	 *
	 *
	 * <p>
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>distance_span_span</li>
	 *             <li>distance_spanset_span</li>
	 *             <li>distance_period_timestamp</li>
	 *         </ul>
	 *  </p>
	 *
	 * @param other temporal object to compare with
	 * @return a timedelta instance
	 */
	public double distance(TemporalObject other) throws Exception {
		double returnValue;
		switch (other){
			case tstzspan p -> returnValue = functions.distance_tstzspan_tstzspan(this._inner,p.get_inner());
			case tstzspanset ps -> returnValue = functions.distance_tstzspanset_tstzspan(ps.get_inner(),this._inner);
			case tstzset ts -> returnValue = ts.to_spanset().distance(other);
			case Box b -> returnValue = this.distance((Time) b.to_period());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}

	/*------------------------ Set Operations ------------------------*/

	/**
	 * Returns the temporal intersection of "this" and "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *         	 <ul>
	 *         		<li>intersection_span_span</li>
	 *         		<li>intersection_spanset_span</li>
	 *         		<li>intersection_period_timestamp</li>
	 *           </ul>
	 *   </p>
	 *
	 *
	 * @param other temporal object to intersect with
	 * @return {@link Time} instance. The actual class depends on "other".
	 */
    public Time intersection(TemporalObject other) throws Exception {
        Time returnValue = null;
        switch (other){
            case tstzspan p -> returnValue = new tstzspan(functions.intersection_span_span(this._inner,p.get_inner()));
            case tstzspanset ps -> returnValue = new tstzspanset(functions.intersection_spanset_span(ps.get_inner(), this._inner));
            case tstzset ts -> returnValue = new tstzset(functions.intersection_spanset_span(functions.set_to_spanset(ts.get_inner()),this._inner));
            default -> returnValue = (Time) super.intersection((Base) other);
        }
        return returnValue;
    }

	/**
	 * Returns the temporal intersection of "this" and "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *         	 <ul>
	 *         		<li>intersection_span_span</li>
	 *         		<li>intersection_spanset_span</li>
	 *         		<li>intersection_period_timestamp</li>
	 *           </ul>
	 *   </p>
	 *
	 *
	 * @param other temporal object to intersect with
	 * @return {@link Time} instance. The actual class depends on "other".
	 */
    public Time mul(Time other) throws Exception {
        return this.intersection(other);
    }

	/**
	 * Returns the temporal difference of "this" and "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *         	<ul>
	 *         			<li>minus_period_timestamp</li>
	 *         			<li>minus_span_spanset</li>
	 *         			<li>minus_span_span</li>
	 *          </ul>
	 * </p>
	 *
	 * @param other temporal object to diff with
	 * @return {@link tstzspanset} instance
	 */
	public tstzspanset minus(Time other)  {
		tstzspanset returnValue;
		switch (other){
			case tstzspan p -> returnValue = new tstzspanset(functions.minus_span_span(this._inner,p.get_inner()));
			case tstzspanset ps -> returnValue = new tstzspanset(functions.minus_span_spanset(this._inner,ps.get_inner()));
			case tstzset ts -> returnValue = new tstzspanset(functions.minus_span_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns the temporal difference of "this" and "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *         	<ul>
	 *         			<li>minus_period_timestamp</li>
	 *         			<li>minus_span_spanset</li>
	 *         			<li>minus_span_span</li>
	 *          </ul>
	 * </p>
	 *
	 * @param other temporal object to diff with
	 * @return {@link tstzspanset} instance
	 */
    public tstzspanset sub(Time other)  {
        return this.minus(other);
    }

	/**
	 * Returns the temporal union of "this" and "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *         	<ul>
	 *         			<li>union_period_timestamp</li>
	 *         			<li>union_spanset_span</li>
	 *         			<li>union_span_span</li>
	 *          </ul>
	 *  </p>
	 *
	 * @param other temporal object to merge with
	 * @return a {@link tstzspanset} instance.
	 */
	public tstzspanset union(Time other)  {
		tstzspanset returnValue;
		switch (other){
			case tstzspan p -> returnValue = new tstzspanset(functions.union_span_span(this._inner,p.get_inner()));
			case tstzspanset ps -> returnValue = new tstzspanset(functions.union_spanset_span(ps.get_inner(),this._inner));
			case tstzset ts -> returnValue = new tstzspanset(functions.union_spanset_span(functions.set_to_spanset(ts.get_inner()),this._inner));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}

	/**
	 * Returns the temporal union of "this" and "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *         	<ul>
	 *         			<li>union_period_timestamp</li>
	 *         			<li>union_spanset_span</li>
	 *         			<li>union_span_span</li>
	 *          </ul>
	 *  </p>
	 *
	 * @param other temporal object to merge with
	 * @return a {@link tstzspanset} instance.
	 */
	public tstzspanset add(Time other)  {
		return this.union(other);
	}

	/*------------------------ Comparisons ------------------------*/


	/**
	 * Return whether "this" and "other" are equal.
	 *
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>span_eq</li>
	 *         </ul>
	 *
	 * @param other temporal object to compare with
	 * @return true if equal, false otherwise
	 */
	public boolean eq(Time other) {
		boolean result;
		result = other instanceof tstzspan && functions.span_eq(this._inner, ((tstzspan) other).get_inner());
		return result;
	}

	/**
	 * Return whether "this" and "other" are not equal.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>span_neq</li>
	 * </p>
	 * @param other temporal object to compare with
	 * @return true if not equal, false otherwise
	 */
	public boolean notEquals(Time other) {
		boolean result;
		result = !(other instanceof tstzspan) || functions.span_ne(this._inner, ((tstzspan) other).get_inner());
		return result;
	}


	/**
	 * Return whether "this" is less than "other".
	 * <p>
	 *         MEOS Functions:
	 *          <ul>
	 *             <li>span_lt</li>
	 *           </ul>
	 *
	 * </p>
	 *
	 * @param other temporal object to compare with
	 * @return true if less than, false otherwise
	 */
	public boolean lessThan(Time other) throws OperationNotSupportedException {
		if (other instanceof tstzspan){
			return functions.span_lt(this._inner,((tstzspan) other).get_inner());
		}
		else{
			throw new OperationNotSupportedException("Operation not supported with this type.");
		}
	}


	/**
	 * Return whether "this" is less than or equal to "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>span_le</li>
	 * </p>
	 *
	 * @param other temporal object to compare with
	 * @return true if less than or equal, false otherwise
	 */
	public boolean lessThanOrEqual(Time other) throws OperationNotSupportedException {
		if (other instanceof tstzspan){
			return functions.span_le(this._inner,((tstzspan) other).get_inner());
		}
		else{
			throw new OperationNotSupportedException("Operation not supported with this type.");
		}
	}


	/**
	 * Return whether "this" is greater than "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>span_gt</li>
	 * </p>
	 * @param other temporal object to compare with
	 * @return true if greater than, false otherwise
	 */
	public boolean greaterThan(Time other) throws OperationNotSupportedException {
		if (other instanceof tstzspan){
			return functions.span_gt(this._inner,((tstzspan) other).get_inner());
		}
		else{
			throw new OperationNotSupportedException("Operation not supported with this type.");
		}
	}

	/**
	 * Return whether "this" is greater than or equal to "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>span_ge</li>
	 *
	 * </p>
	 * @param other temporal object to compare with
	 * @return true if greater than or equal, false otherwise
	 */
	public boolean greaterThanOrEqual(Time other) throws OperationNotSupportedException {
		if (other instanceof tstzspan){
			return functions.span_ge(this._inner,((tstzspan) other).get_inner());
		}
		else{
			throw new OperationNotSupportedException("Operation not supported with this type.");
		}
	}

}
