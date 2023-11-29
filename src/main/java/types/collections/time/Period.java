package types.collections.time;

import functions.functions;
import jnr.ffi.Pointer;
import types.TemporalObject;
import types.boxes.Box;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import types.collections.base.Span;

/**
 * Class for representing sets of contiguous timestamps between a lower and
 * an upper bound. The bounds may be inclusive or not.
 * <p>
 * ``Period`` objects can be created with a single argument of type string
 * as in MobilityDB.
 * <p>
 * >>> new Period("(2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01)")
 * <p>
 * Another possibility is to provide the ``lower`` and ``upper`` named parameters (of type str or datetime), and
 * optionally indicate whether the bounds are inclusive or exclusive (by default, the lower bound is inclusive and the
 * upper is exclusive):
 * <p>
 * >>> new Period(lower='2019-09-08 00:00:00+01', upper='2019-09-10 00:00:00+01')
 * >>> new Period(lower='2019-09-08 00:00:00+01', upper='2019-09-10 00:00:00+01', lower_inc=False, upper_inc=True)
 * >>> new Period(lower=parse('2019-09-08 00:00:00+01'), upper=parse('2019-09-10 00:00:00+01'), upper_inc=True)
 * <p>
 * @author Nidhal Mareghni
 * @since 07/09/2023
 *
 * TODO: Add datetime in constructor, Modify the SQLException, Modify the timestampTZ
 */
@TypeName(name = "period")
public class Period extends Span<DateTime> implements Time, TimeCollection{
	private static final String LOWER_INCLUSIVE = "[";
	private static final String LOWER_EXCLUSIVE = "(";
	private static final String UPPER_INCLUSIVE = "]";
	private static final String UPPER_EXCLUSIVE = ")";
	private OffsetDateTime lower;
	private OffsetDateTime upper;
	private boolean lowerInclusive = true;
	private boolean upperInclusive = false;
	private Pointer _inner;

	/**
	 * ------------------------ Constructors ------------------------
	 */


	/**
	 * The default constructor
	 */
	public Period() {
		super();
	}
	
	/**
	 * Constructor by copy
	 *
	 * @param other period instace
	 * @throws SQLException sql exception
	 */
	public Period(Period other) throws SQLException {
		this(other.get_inner());
	}
	
	/**
	 * Constructor through Meos (C) inner object
	 *
	 * @param _inner Pointer to C object
	 * @throws SQLException sql exception
	 */
	public Period(Pointer _inner) throws SQLException {
		super(_inner);
		this._inner = _inner;
		String str = functions.period_out(this._inner);
		System.out.println(str);
		setValue(str);
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - a string with a Period value
	 * @throws SQLException sql exception
	 */
	public Period(final String value) throws SQLException {
		super(value);
		this.setValue(value);
		this._inner = functions.period_in(value);
	}
	
	/**
	 * The timestamps constructor
	 *
	 * @param lower - a timestamp for the lower bound
	 * @param upper - a timestamp for the upper bound
	 * @throws SQLException sql exception
	 */
	public Period(OffsetDateTime lower, OffsetDateTime upper) throws SQLException {
		super();
		this.lower = lower;
		this.upper = upper;
		this.lowerInclusive = true;
		this.upperInclusive = false;
		OffsetDateTime lower_ts = functions.pg_timestamptz_in(this.lower.toString(), -1);
		OffsetDateTime upper_ts = functions.pg_timestamptz_in(this.upper.toString(), -1);
		this._inner = functions.period_make(lower_ts, upper_ts, this.lowerInclusive, this.upperInclusive);
		validate();
		
	}
	
	/**
	 * The timestamps and bounds constructor
	 *
	 * @param lower          - a timestamp for the lower bound
	 * @param upper          - a timestamp for the upper bound
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException sql exception
	 */
	public Period(OffsetDateTime lower, OffsetDateTime upper, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super();
		this.lower = lower;
		this.upper = upper;
		this.lowerInclusive = lowerInclusive;
		this.upperInclusive = upperInclusive;
		OffsetDateTime lower_ts = functions.pg_timestamptz_in(this.lower.toString(), -1);
		OffsetDateTime upper_ts = functions.pg_timestamptz_in(this.upper.toString(), -1);
		this._inner = functions.period_make(lower_ts, upper_ts, this.lowerInclusive, this.upperInclusive);
		validate();
	}


	@Override
	public Pointer createStringInner(String str){
		return functions.period_in(str);
	}

	@Override
	public Pointer createInner(Pointer inner){
		return _inner;
	}

	@Override
	public Pointer createIntInt(int lower, int upper, boolean lower_inc, boolean upper_inc){
		return functions.intspan_make(lower,upper,lower_inc,upper_inc);
	}
	@Override
	public Pointer createIntStr(int lower, String upper, boolean lower_inc, boolean upper_inc){
		int new_upper = Integer.parseInt(upper);
		return functions.intspan_make(lower,new_upper,lower_inc,upper_inc);
	}
	@Override
	public Pointer createStrStr(String lower, String upper, boolean lower_inc, boolean upper_inc){
		int new_upper = Integer.parseInt(upper);
		int new_lower = Integer.parseInt(lower);
		return functions.intspan_make(new_lower,new_upper,lower_inc,upper_inc);
	}
	@Override
	public Pointer createStrInt(String lower, int upper, boolean lower_inc, boolean upper_inc){
		int new_lower = Integer.parseInt(lower);
		return functions.intspan_make(new_lower,upper,lower_inc,upper_inc);
	}
	@Override
	public Pointer createIntIntNb(int lower, int upper){
		return functions.intspan_make(lower,upper,true,false);
	}


	/**
	 * Return a copy of "this" object.
	 * <p>
	 * 			MEOS Functions:
	 * 		    <ul>
	 *             <li>span_copy</li>
	 *          </ul
	 * </p>
	 * @return Instance of Period class
	 * @throws SQLException
	 */
    public Period copy() throws SQLException {
        return new Period(functions.span_copy(this._inner));
    }


	/**
	 * Returns a Period from its WKB representation in hex-encoded ASCII.
	 *	<p>
	 *		MEOS Functions:
	 *			<ul>
	 *             <li>span_from_hexwkb</li>
	 *          </ul>
	 *  </p>
	 *
	 * @param hexwkb: WKB representation in hex-encoded ASCII
	 * @return Instance of Period class
	 * @throws SQLException
	 */
	public static Period from_hexwkb(String hexwkb) throws SQLException {
		Pointer result = functions.span_from_hexwkb(hexwkb);
		return new Period(result);
	}


	/**
	 * ------------------------ Output ------------------------
	 */


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
		return functions.period_out(this._inner);
	}


	/**
	 * ------------------------ Conversions ------------------------
	 */


	/**
	 * Returns a period set containing "this" object.
	 *	<p>
	 *		MEOS Functions:
	 *			<ul>
	 *             <li>span_to_spanset</li>
	 *          </ul>
	 *  </p>
	 * @return PeriodSet instance
	 * @throws SQLException
	 */
	public PeriodSet to_periodset() throws SQLException {
		return new PeriodSet(functions.span_to_spanset(this._inner));
	}



	/**
	 * ------------------------ Accessors ------------------------
	 */

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

	public OffsetDateTime getLower() {
		return lower;
	}

	public OffsetDateTime getUpper() {
		return upper;
	}

	public boolean isLowerInclusive() {
		return lowerInclusive;
	}

	public boolean isUpperInclusive() {
		return upperInclusive;
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
		return Duration.between(lower,upper);
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
		return (float) functions.span_width(this._inner);
	}

	/**
	 * Return the hash representation of "this".
	 * <p>
	 * MEOS Functions:
	 *             <li>span_hash</li>
	 * </p>
	 * @return integer instance
	 */
	public int hash(){
		return functions.span_hash(this._inner);
	}


	/**
	 * ------------------------ Transformations ------------------------
	 */


	/**
	 * Returns a new period that includes both "this" and "other"
	 * <pre>
	 * Examples:
	 *     >>> Period('[2000-01-01, 2000-01-04)').expand(Period('[2000-01-05, 2000-01-10]'))
	 *     >>> Period([2000-01-01 00:00:00+01, 2000-01-10 00:00:00+01])
	 * </pre>
	 *
	 * 		MEOS Functions:
	 * 			<ul>
	 *             <li>span_expand</li>
	 *          </ul>
	 *
	 * @param other period instance needed to expand the object
	 * @return Period instance
	 * @throws SQLException
	 */
	public Period expand(Period other) throws SQLException {
		Pointer copy = functions.span_copy(this._inner);
		functions.span_expand(other._inner, copy);
		return new Period(copy);
	}


	/**
	 * ------------------------ Topological Operations ------------------------
	 */


	/**
	 * Returns whether "this" is temporally adjacent to "other". That is, they share a bound but only one of them
	 *         contains it.
	 * <pre>
	 *         Examples:
	 *             >>> Period('[2012-01-01, 2012-01-02)').is_adjacent(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> Period('[2012-01-01, 2012-01-02]').is_adjacent(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> False  # Both contain bound
	 *             >>> Period('[2012-01-01, 2012-01-02)').is_adjacent(Period('(2012-01-02, 2012-01-03]'))
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
	 * @throws SQLException
	 */
	public boolean is_adjacent(Time other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.adjacent_span_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.adjacent_spanset_span(ps.get_inner(), this._inner);
			//case DateTime dt -> returnValue = functions.adjacent_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			case TimestampSet ts -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.set_span(ts.get_inner()));
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.adjacent_span_span(this._inner, b.to_period()._inner);
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is temporally contained in "container".
	 * <pre>
	 *         Examples:
	 *             >>> Period('[2012-01-02, 2012-01-03]').is_contained_in(Period('[2012-01-01, 2012-01-04]'))
	 *             >>> True
	 *             >>> Period('(2012-01-01, 2012-01-02)').is_contained_in(Period('[2012-01-01, 2012-01-02]'))
	 *             >>> True
	 *             >>> Period('[2012-01-01, 2012-01-02]').is_contained_in(Period('(2012-01-01, 2012-01-02)'))
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
	 * @throws SQLException
	 */
	public boolean is_contained_in(Time other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.contained_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.contained_span_spanset(this._inner,ps.get_inner());
			//case Temporal t -> returnValue = functions.contained_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			case Box b -> returnValue = functions.contained_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" temporally contains "other".
	 *	<pre>
	 *         Examples:
	 *             >>> Period('[2012-01-01, 2012-01-04]').contains(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> Period('[2012-01-01, 2012-01-02]').contains(Period('(2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> Period('(2012-01-01, 2012-01-02)').contains(Period('[2012-01-01, 2012-01-02]'))
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
	 * @throws SQLException
	 */
	public boolean contains(Time other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.contains_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.contains_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.contains_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.contains_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			case Box b -> returnValue = functions.contains_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" temporally overlaps "other". That is, both share at least an instant
	 * <pre>
	 *         Examples:
	 *             >>> Period('[2012-01-01, 2012-01-02]').overlaps(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> Period('[2012-01-01, 2012-01-02)').overlaps(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> False
	 *             >>> Period('[2012-01-01, 2012-01-02)').overlaps(Period('(2012-01-02, 2012-01-03]'))
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
	 * @throws SQLException
	 */
	public boolean overlaps(Time other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.overlaps_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.overlaps_spanset_span(ps.get_inner(),this._inner);
			case TimestampSet ts -> returnValue = functions.overlaps_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.overlaps_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			case Box b -> returnValue = functions.overlaps_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
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
	 * @throws SQLException
	 */
	public boolean is_same(Time other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.span_eq(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.span_eq(this._inner,functions.spanset_span(ps.get_inner()));
			case TimestampSet ts -> returnValue = functions.span_eq(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.span_eq(this._inner,functions.temporal_to_period(t.get_inner()));
			case Box b -> returnValue = functions.span_eq(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	/**
	 * ------------------------ Position Operations ------------------------
	 */

	/**
	 * Returns whether "this" is strictly before "other". That is, "this" ends before "other" starts.
	 * <pre>
	 *         Examples:
	 *             >>> Period('[2012-01-01, 2012-01-02)').is_before(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> Period('[2012-01-01, 2012-01-02)').is_before(Period('(2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> Period('[2012-01-01, 2012-01-02]').is_before(Period('[2012-01-02, 2012-01-03]'))
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
	 * @throws SQLException
	 */
	public boolean is_before(Time other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.left_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.left_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.left_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.left_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			case Box b -> returnValue = functions.left_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is before "other" allowing overlap. That is, "this" ends before "other" ends (or
	 *         at the same time).
	 * <pre>
	 *         Examples:
	 *             >>> Period('[2012-01-01, 2012-01-02)').is_over_or_before(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> Period('[2012-01-01, 2012-01-02]').is_over_or_before(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> Period('[2012-01-03, 2012-01-05]').is_over_or_before(Period('[2012-01-01, 2012-01-04]'))
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
	 * @throws SQLException
	 */
	public boolean is_over_or_before(Time other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.overleft_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.overleft_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overleft_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.overleft_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			case Box b -> returnValue = functions.overleft_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}

	/**
	 * Returns whether "this" is strictly after "other". That is, "this" starts after "other" ends.
	 * <pre>
	 *         Examples:
	 *             >>> Period('[2012-01-02, 2012-01-03]').is_after(Period('[2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> Period('(2012-01-02, 2012-01-03]').is_after(Period('[2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> Period('[2012-01-02, 2012-01-03]').is_after(Period('[2012-01-01, 2012-01-02]'))
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
	 * @throws SQLException
	 */
	public boolean is_after(Time other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.right_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.right_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.right_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.right_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			case Box b -> returnValue = functions.right_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is after "other" allowing overlap. That is, "this" starts after "other" starts
	 *         (or at the same time).
	 *	<pre>
	 *         Examples:
	 *             >>> Period('[2012-01-02, 2012-01-03]').is_over_or_after(Period('[2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> Period('[2012-01-02, 2012-01-03]').is_over_or_after(Period('[2012-01-01, 2012-01-02]'))
	 *             >>> True
	 *             >>> Period('[2012-01-02, 2012-01-03]').is_over_or_after(Period('[2012-01-01, 2012-01-03]'))
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
	 * @throws SQLException
	 */
	public boolean is_over_or_after(Time other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.overright_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.overright_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overright_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.overright_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			case Box b -> returnValue = functions.overright_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	//TODO: use Duration class from java.time instead

	/**
	 * ------------------------ Distance Operations ------------------------
	 */


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
	 * @throws SQLException
	 */
	public double distance(Time other) throws SQLException{
		double returnValue;
		switch (other){
			case Period p -> returnValue = functions.distance_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.distance_spanset_span(ps.get_inner(),this._inner);
			case TimestampSet ts -> returnValue = functions.distance_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.distance_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.distance_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}

	/**
	 * ------------------------ Set Operations ------------------------
	 */

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
	 * @throws SQLException
	 */
    public Time intersection(Time other) throws SQLException{
        Time returnValue = null;
        switch (other){
            case Period p -> returnValue = new Period(functions.intersection_span_span(this._inner,p.get_inner()));
            case PeriodSet ps -> returnValue = new PeriodSet(functions.intersection_spanset_span(ps.get_inner(), this._inner));
            case TimestampSet ts -> returnValue = new TimestampSet(functions.intersection_spanset_span(functions.set_to_spanset(ts.get_inner()),this._inner));
            default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
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
	 * @throws SQLException
	 */
    public Time mul(Time other) throws SQLException {
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
	 * @return {@link PeriodSet} instance
	 * @throws SQLException
	 */
	public PeriodSet minus(Time other) throws SQLException{
		PeriodSet returnValue;
		switch (other){
			case Period p -> returnValue = new PeriodSet(functions.minus_span_span(this._inner,p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.minus_span_spanset(this._inner,ps.get_inner()));
			case TimestampSet ts -> returnValue = new PeriodSet(functions.minus_span_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
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
	 * @return {@link PeriodSet} instance
	 * @throws SQLException
	 */
    public PeriodSet sub(Time other) throws SQLException {
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
	 * @return a {@link PeriodSet} instance.
	 * @throws SQLException
	 */
	public PeriodSet union(Time other) throws SQLException{
		PeriodSet returnValue;
		switch (other){
			case Period p -> returnValue = new PeriodSet(functions.union_span_span(this._inner,p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.union_spanset_span(ps.get_inner(),this._inner));
			case TimestampSet ts -> returnValue = new PeriodSet(functions.union_spanset_span(functions.set_to_spanset(ts.get_inner()),this._inner));
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
	 * @return a {@link PeriodSet} instance.
	 * @throws SQLException
	 */
	public PeriodSet add(Time other) throws SQLException {
		return this.union(other);
	}

	/**
	 * ------------------------ Comparisons ------------------------
	 */


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
	 * @throws SQLException
	 */
	public boolean equals(Time other) throws SQLException{
		boolean result;
		result = other instanceof Period ? functions.span_eq(this._inner,((Period) other).get_inner()) : false;
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
	 * @throws SQLException
	 */
	public boolean notEquals(Time other) throws SQLException{
		boolean result;
		result = other instanceof Period ? functions.span_ne(this._inner,((Period) other).get_inner()) : true;
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
	 * @throws SQLException
	 */
	public boolean lessThan(Time other) throws SQLException{
		if (other instanceof Period){
			return functions.span_lt(this._inner,((Period) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
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
	 * @throws SQLException
	 */
	public boolean lessThanOrEqual(Time other) throws SQLException{
		if (other instanceof Period){
			return functions.span_le(this._inner,((Period) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
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
	 * @throws SQLException
	 */
	public boolean greaterThan(Time other) throws SQLException{
		if (other instanceof Period){
			return functions.span_gt(this._inner,((Period) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
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
	 * @throws SQLException
	 */
	public boolean greaterThanOrEqual(Time other) throws SQLException{
		if (other instanceof Period){
			return functions.span_ge(this._inner,((Period) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getValue() {
		if (lower == null || upper == null) {
			return null;
		}

		return String.format("%s%s, %s%s",
				lowerInclusive ? LOWER_INCLUSIVE : LOWER_EXCLUSIVE,
				DateTimeFormatHelper.getStringFormat(lower),
				DateTimeFormatHelper.getStringFormat(upper),
				upperInclusive ? UPPER_INCLUSIVE : UPPER_EXCLUSIVE);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setValue(final String value) throws SQLException {
		String[] values = value.split(",");


		if (values.length != 2) {
			throw new SQLException("Could not parse period value.");
		}
		
		if (values[0].startsWith(LOWER_INCLUSIVE)) {
			this.lowerInclusive = true;
		} else if (values[0].startsWith(LOWER_EXCLUSIVE)) {
			this.lowerInclusive = false;
		} else {
			throw new SQLException("Lower bound flag must be either '[' or '('.");
		}
		
		if (values[1].endsWith(UPPER_INCLUSIVE)) {
			this.upperInclusive = true;
		} else if (values[1].endsWith(UPPER_EXCLUSIVE)) {
			this.upperInclusive = false;
		} else {
			throw new SQLException("Upper bound flag must be either ']' or ')'.");
		}
		System.out.println(values[0].substring(1).trim());
		this.lower = DateTimeFormatHelper.getDateTimeFormat(values[0].substring(1).trim());
		this.upper = DateTimeFormatHelper.getDateTimeFormat(values[1].substring(0, values[1].length() - 1).trim());
		validate();
	}

	
	/**
	 * Shifts the duration sent
	 *
	 * @param duration - the duration to shift
	 */
	public Period shift(Duration duration) throws SQLException {
		return new Period(lower.plus(duration), upper.plus(duration), lowerInclusive, upperInclusive);
	}

	/**
	 * Verifies that the received fields are valid
	 *
	 * @throws SQLException sql exception
	 */
	private void validate() throws SQLException {
		if (lower == null || upper == null) {
			throw new SQLException("The lower and upper bounds must be defined.");
		}
		
		if (lower.isAfter(upper)) {
			throw new SQLException("The lower bound must be less than or equal to the upper bound.");
		}
		
		if (lower.isEqual(upper) && (!lowerInclusive || !upperInclusive)) {
			throw new SQLException("The lower and upper bounds must be inclusive for an instant period.");
		}
	}
}
