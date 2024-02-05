package types.collections.time;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.collections.base.Base;
import types.collections.base.Set;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import types.boxes.*;

import functions.functions;
import types.temporal.Temporal;
import utils.ConversionUtils;


/**
 * Class for representing lists of distinct timestamp values.
 *	<pre>
 *     ``TimestampSet`` objects can be created with a single argument of type string
 *     as in MobilityDB.
 *
 *         >>> TimestampSet(string='{2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01, 2019-09-11 00:00:00+01}')
 *
 *     Another possibility is to give a tuple or list of composing timestamps,
 *     which can be instances of ``str`` or ``datetime``. The composing timestamps
 *     must be given in increasing order.
 *
 *         >>> TimestampSet(timestamp_list=['2019-09-08 00:00:00+01', '2019-09-10 00:00:00+01', '2019-09-11 00:00:00+01'])
 *         >>> TimestampSet(timestamp_list=[parse('2019-09-08 00:00:00+01'), parse('2019-09-10 00:00:00+01'), parse('2019-09-11 00:00:00+01')])
 * </pre>
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TimestampSet extends Set<LocalDateTime> implements Time, TimeCollection {
	private final List<OffsetDateTime> dateTimeList = null;
	private Pointer _inner;


    /* ------------------------- Constructors ---------------------------------- */


	/**
	 * The default constructor
	 */
	public TimestampSet() {
	}

	/**
	 * Pointer constructor
	 * @param _inner Pointer
	 */
	public TimestampSet(Pointer _inner)  {
		super(_inner);
		this._inner = _inner;
		//String str = functions.timestampset_out(this._inner);
	}
	
	
	/**
	 * The string constructor
	 *
	 * @param value - a string with a TimestampSet value
	 */
	public TimestampSet(String value) {
		super(value);
		this._inner = functions.timestampset_in(value);
	}


	@Override
	public Pointer createStringInner(String str){
		return functions.timestampset_in(str);
	}

	@Override
	public Pointer createInner(Pointer inner){
		return inner;
	}


	/**
	 * Return a copy of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_copy</li>
	 * @return a new TimestampSet instance
	 */
	public TimestampSet copy() {
		return new TimestampSet(functions.set_copy(this._inner));
	}


	/**
	 * Returns a "TimestampSet" from its WKB representation in hex-encoded ASCII.
	 *  <p>
	 *         MEOS Functions:
	 *             <li>set_from_hexwkb</li>
	 * @param hexwkb WKB representation in hex-encoded ASCII
	 * @return a new TimestampSet instance
	 */
	public static TimestampSet from_hexwkb(String hexwkb) {
		Pointer result = functions.set_from_hexwkb(hexwkb);
		return new TimestampSet(result);
	}

    /* ------------------------- Output ---------------------------------------- */


	/**
	 * Return the string representation of the content of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_out</li>
	 * @return a new String instance
	 */
	public String toString(){
		return functions.set_out(this._inner,15);
	}


    /* ------------------------- Conversions ----------------------------------- */


	/**
	 * Returns a PeriodSet that contains a Period for each Timestamp in "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_to_spanset</li>
	 * @return a new PeriodSet instance
	 */
	public PeriodSet to_periodset() {
		return new PeriodSet(functions.set_to_spanset(this.get_inner()));
	}

	/**
	 * Returns a period that encompasses "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_span</li>
	 * @return a new Period instance
	 */
	public Period to_span() {
		return new Period(functions.set_span(this._inner));
	}

	/**
	 * Returns a period that encompasses "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_span</li>
	 * @return a new Period instance
	 */
	public Period to_period() {
		return this.to_span();
	}





	/** ------------------------- Accessors ------------------------------------- */


	public Pointer get_inner(){
		return this._inner;
	}


	/**
	 * Returns the number of timestamps in "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>set_num_values</li>
	 * @return a new Integer instance
	 */
	public int num_timestamps(){
		return functions.set_num_values(this._inner);
	}

	/**
	 * Returns the first timestamp in "this".
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>timestampset_start_timestamp</li>
	 *
	 * @return a {@link LocalDateTime instance}
	 */
	public LocalDateTime start_element(){
		return ConversionUtils.timestamptz_to_datetime(functions.timestampset_start_timestamp(this._inner));
	}

	/**
	 * Returns the last timestamp in "this".
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>timestampset_end_timestamp</li>
	 *
	 * @return a {@link LocalDateTime instance}
	 */
	public LocalDateTime end_element(){
		return ConversionUtils.timestamptz_to_datetime(functions.timestampset_end_timestamp(this._inner));
	}


	/**
	 * Return the hash representation of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_hash</li>
	 * @return a new Integer instance
	 */
	public long hash(){
		return functions.set_hash(this._inner);
	}


    /* ------------------------- Topological Operations ------------------------ */


    /**
     * Returns whether "this" is temporally adjacent to "other". That is, they share a bound but only one of them
     *         contains it.
     * <pre>
     *         Examples:
     *             >>> TimestampSet('{2012-01-01, 2012-01-02}').is_adjacent(Period('[2012-01-02, 2012-01-03]'))
     *             >>> True
     *             >>> TimestampSet('{2012-01-01, 2012-01-02}').is_adjacent(Period('[2012-01-02, 2012-01-03]'))
     *             >>> False  # Both contain bound
     *             >>> TimestampSet('{2012-01-01, 2012-01-02}').is_adjacent(Period('(2012-01-02, 2012-01-03]'))
     *             >>> False  # Neither contain bound
     *</pre>
     *         MEOS Functions:
     *         <ul>
     *              <li>adjacent_span_span</li>
     *              <li>adjacent_spanset_span</li>
     *         </ul>
     * @param other temporal object to compare with
     * @return true if adjacent, false otherwise
     */
	public boolean is_adjacent(TemporalObject other) {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.adjacent_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.adjacent_spanset_spanset(ps.get_inner(), functions.set_to_spanset(this._inner));
			case Temporal t -> returnValue = this.is_adjacent(t.time());
			case Box b -> returnValue = functions.adjacent_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


    /**
      Returns whether "this" is temporally contained in "other".
     <pre>
              Examples:
                  >>> TimestampSet('{2012-01-02, 2012-01-03}').is_contained_in(Period('[2012-01-01, 2012-01-04]'))
                  >>> True
                  >>> TimestampSet('{2012-01-01, 2012-01-02}').is_contained_in(Period('[2012-01-01, 2012-01-02]'))
                  >>> True
                  >>> TimestampSet('{2012-01-01, 2012-01-02}').is_contained_in(Period('(2012-01-01, 2012-01-02)'))
                  >>> False
     </pre>
              MEOS Functions:
              <ul>
                   <li>contained_span_span</li>
                   <li>contained_span_spanset</li>
                   <li>contained_set_set</li>
                   <li>contained_spanset_spanset</li>
              </ul>

      @param other temporal object to compare with
     * @return true if contained, false otherwise
     * @throws Exception
     */
	public boolean is_contained_in(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.contained_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.contained_spanset_spanset(functions.set_span(this._inner), ps.get_inner());
			case Temporal t -> returnValue = this.is_contained_in(t.time());
			case Box b -> returnValue = functions.contained_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_contained_in((Base) other);
		}
		return returnValue;
	}




    /**
     * Returns whether "this" temporally contains "other".
     *<pre>
     *         Examples:
     *             >>> TimestampSet('{2012-01-01, 2012-01-04}').contains(parse('2012-01-01]'))
     *             >>> True
     *             >>> TimestampSet('{2012-01-01, 2012-01-02}').contains(TimestampSet('{2012-01-01}'))
     *             >>> True
     *             >>> TimestampSet('{2012-01-01, 2012-01-02}').contains(TimestampSet('{2012-01-01, 2012-01-03}'))
     *             >>> False
     *</pre>
     *         MEOS Functions:
     *         <ul>
     *             <li>contains_timestampset_timestamp</li>
     *             <li>contains_set_set</li>
     *             <li>contains_spanset_spanset</li>
     *         </ul>
     * @param other temporal object to compare with
     * @return true if contains, false otherwise
	 * @throws Exception
     */
	public boolean contains(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case TimestampSet ts -> returnValue = functions.contains_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.contains(t.time());
			default -> returnValue = super.contains((Base) other);
		}
		return returnValue;
	}


    /**
      Returns whether "this" temporally overlaps "other". That is, both share at least an instant
      <pre>
              Examples:
                  >>> TimestampSet('{2012-01-01, 2012-01-02}').overlaps(TimestampSet('{2012-01-02, 2012-01-03}'))
                  >>> True
                  >>> TimestampSet('{2012-01-01, 2012-01-02}').overlaps(Period('[2012-01-02, 2012-01-03]'))
                  >>> True
                  >>> TimestampSet('{2012-01-01, 2012-01-02}').overlaps(Period('(2012-01-02, 2012-01-03]'))
                  >>> False
     </pre>

              MEOS Functions:
              <ul>
                   <li>overlaps_set_set</li>
                   <li>overlaps_span_span</li>
                   <li>overlaps_spanset_spanset</li>
              </ul>

      @param other temporal object to compare with
     * @return true if overlaps, false otherwise
	 * @throws  Exception
     */
	public boolean overlaps(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overlaps_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.overlaps_spanset_spanset(functions.set_to_spanset(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overlaps_set_set(this._inner, ts.get_inner());
			case Box b -> returnValue = functions.overlaps_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.overlaps((Base) other);
		}
		return returnValue;
	}




    /**
     * Returns whether the bounding period of "this" is the same as the bounding period of "other".
     *
     *         See Also:
     *             {@link Period#is_same(TemporalObject)}
     * @param other A time or temporal object to compare to `self`.
     * @return true if same, false otherwise.
     */
	public boolean is_same(Time other) throws Exception {
		return this.to_period().is_same(other);
	}


    /* ------------------------- Position Operations --------------------------- */


    /**
     * Returns whether "this" is strictly after "other". That is, the first timestamp in "this"
     *         is after "other".
     * <pre>
     *         Examples:
     *             >>> TimestampSet('{2012-01-02, 2012-01-03}').is_after(Period('[2012-01-01, 2012-01-02)'))
     *             >>> True
     *             >>> TimestampSet('{2012-01-02, 2012-01-03}').is_after(TimestampSet('{2012-01-01}'))
     *             >>> True
     *             >>> TimestampSet('{2012-01-02, 2012-01-03}').is_after(Period('[2012-01-01, 2012-01-02]'))
     *             >>> False
     *</pre>
     *
     *         MEOS Functions:
     *         <ul>
     *         <li> overbefore_timestamp_timestampset</li>
     *         <li> right_set_set</li>
     *         <li> right_span_span</li>
     *         <li> right_span_spanset</li>
     *         </ul>
     *
     *
     *
     * @param other temporal object to compare with
     * @return true if after, false otherwise
	 * @throws  Exception
     */
	public boolean is_after(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.right_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.right_span_spanset(functions.set_span(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.right_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.to_period().is_after(other);
			case Box b -> returnValue = functions.right_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_left((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is strictly before "other". That is, "this" ends before "other" starts.
	 *
	 * <pre>
	 *         Examples:
	 *             >>> TimestampSet('{2012-01-01, 2012-01-02}').is_before(TimestampSet('{2012-01-03}'))
	 *             >>> True
	 *             >>> TimestampSet('{2012-01-01, 2012-01-02}').is_before(Period('(2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> TimestampSet('{2012-01-01, 2012-01-02}').is_before(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> False
	 * </pre>
	 *         MEOS Functions:
	 *         	   <ul>
	 *             <li>overafter_timestamp_period</li>
	 *             <li>left_span_span</li>
	 *             <li>left_span_spanset</li>
	 *             </ul>
	 *
	 * @param other: temporal object to compare with
	 * @return true if before, false otherwise
	 * @throws  Exception
	 */
	public boolean is_before(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.left_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.left_span_spanset(functions.set_span(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.left_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.to_period().is_before(other);
			case Box b -> returnValue = functions.left_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_left((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is after "other" allowing overlap. That is, "this" starts after "other" starts
	 *         (or at the same time).
	 * <pre>
	 *         Examples:
	 *             >>> TimestampSet('{2012-01-02, 2012-01-03}').is_over_or_after(Period('[2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> TimestampSet('{2012-01-02, 2012-01-03}').is_over_or_after(Period('[2012-01-01, 2012-01-02]'))
	 *             >>> True
	 *             >>> TimestampSet('{2012-01-02, 2012-01-03}').is_over_or_after(Period('[2012-01-01, 2012-01-03]'))
	 *             >>> False
	 *</pre>
	 *         MEOS Functions:
	 *         <ul>
	 *         <li>overafter_period_timestamp</li>
	 *         <li>overright_span_span</li>
	 *         <li>overright_span_spanset</li>
	 *         </ul>
	 *
	 * @param other temporal object to compare with
	 * @return true if overlapping or after, false otherwise
	 */
	public boolean is_over_or_after(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overright_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.overright_span_spanset(functions.set_span(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overright_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.to_period().is_over_or_after(other);
			case Box b -> returnValue = functions.overright_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_over_or_right((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is before "other" allowing overlap. That is, "this" ends before "other" ends (or
	 *         at the same time).
	 * <pre>
	 *         Examples:
	 *             >>> TimestampSet('{2012-01-01, 2012-01-02}').is_over_or_before(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> TimestampSet('{2012-01-01, 2012-01-02}').is_over_or_before(Period('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> TimestampSet('{2012-01-03, 2012-01-05}').is_over_or_before(Period('[2012-01-01, 2012-01-04]'))
	 *             >>> False
	 *</pre>
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>overbefore_period_timestamp</li>
	 *             <li>overleft_span_span</li>
	 *             <li>overleft_span_spanset</li>
	 *             </ul>
	 *
	 * @param other temporal object to compare with
	 * @return true if before, false otherwise
	 * @throws  Exception
	 */
	public boolean is_over_or_before(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overleft_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.overleft_span_spanset(functions.set_span(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overleft_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.to_period().is_over_or_before(other);
			case Box b -> returnValue = functions.overleft_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_over_or_left((Base) other);
		}
		return returnValue;
	}

    /* ------------------------- Distance Operations --------------------------- */






    /* ------------------------- Set Operations -------------------------------- */

	/**
	 * Returns the temporal intersection of "this" and "other".
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *         <li>intersection_set_set</li>
	 *         <li>intersection_spanset_span</li>
	 *         <li>intersection_spanset_spanset</li>
	 *         </ul>
	 *
	 * @param other temporal object to intersect with
	 * @return a Time instance. The actual class depends on "other".
	 */
	public Time intersection(TemporalObject other) {
		Time returnValue = null;
		switch (other) {
			case Period p -> returnValue = new PeriodSet(functions.intersection_spanset_span(functions.set_to_spanset(this._inner), p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.intersection_spanset_spanset(functions.set_to_spanset(this._inner),ps.get_inner()));
			case TimestampSet ts -> returnValue = new TimestampSet(functions.intersection_set_set(this._inner,ts.get_inner()));
			case Temporal t -> returnValue = this.intersection(t.time());
			case Box b -> returnValue = this.intersection(b.to_period());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}

	/**
	 * Returns the temporal intersection of "this" and "other".
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *         <li>intersection_set_set</li>
	 *         <li>intersection_spanset_span</li>
	 *         <li>intersection_spanset_spanset</li>
	 *         </ul>
	 *
	 * @param other temporal object to intersect with
	 * @return a Time instance. The actual class depends on "other".
	 */
	public Time mul(TemporalObject other) {
		return this.intersection(other);
	}


	/**
	 * Returns the temporal difference of "this" and "other".
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>minus_timestampset_timestamp</li>
	 *             <li>minus_set_set</li>
	 *             <li>minus_spanset_span</li>
	 *             <li>minus_spanset_spanset</li>
	 *         </ul>
	 *
	 *
	 * @param other temporal object to diff with
	 * @return a Time instance. The actual class depends on "other".
	 */
	public Time minus(TemporalObject other) {
		Time returnValue = null;
		switch (other) {
			case Period p -> returnValue = new PeriodSet(functions.minus_spanset_span(functions.set_to_spanset(this._inner), p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.minus_spanset_spanset(functions.set_to_spanset(this._inner),ps.get_inner()));
			case TimestampSet ts -> returnValue = new TimestampSet(functions.minus_set_set(this._inner,ts.get_inner()));
			case Temporal t -> returnValue = this.minus(t.time());
			case Box b -> returnValue = this.minus(b.to_period());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns the temporal difference of "this" and "other".
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>minus_timestampset_timestamp</li>
	 *             <li>minus_set_set</li>
	 *             <li>minus_spanset_span</li>
	 *             <li>minus_spanset_spanset</li>
	 *         </ul>
	 *
	 *
	 * @param other temporal object to diff with
	 * @return a Time instance. The actual class depends on "other".
	 */
	public Time sub(Time other) {
		return this.minus(other);
	}

	/**
	 * Returns the temporal union of "this" and "other".
	 *
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>union_timestampset_timestamp</li>
	 *             <li>union_set_set</li>
	 *             <li>union_spanset_span</li>
	 *             <li>union_spanset_spanset</li>
	 *         </ul>
	 * @param other temporal object to merge with
	 * @return a Time instance. The actual class depends on "other".
	 */
	public Time union(TemporalObject other) {
		Time returnValue = null;
		switch (other) {
			case Period p -> returnValue = new Period(functions.union_spanset_span(functions.set_to_spanset(this._inner),p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.union_spanset_spanset(functions.set_to_spanset(this._inner),ps.get_inner()));
			case TimestampSet ts -> returnValue =  new TimestampSet(functions.union_set_set(this._inner,ts.get_inner()));
			case Temporal t -> returnValue = this.union(t.time());
			case Box b -> returnValue = this.union(b.to_period());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns the temporal union of "this" and "other".
	 *
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>union_timestampset_timestamp</li>
	 *             <li>union_set_set</li>
	 *             <li>union_spanset_span</li>
	 *             <li>union_spanset_spanset</li>
	 *         </ul>
	 * @param other temporal object to merge with
	 * @return a Time instance. The actual class depends on "other".
	 */
	public Time add(Time other) {
		return this.union(other);
	}


    /* ------------------------- Comparisons ----------------------------------- */

	/*
	  Returns whether "this" and "other" are equal.

	          MEOS Functions:
	          <ul>
	              <li>set_eq</li>
	         </ul>
	  @param other temporal object to compare with
	 * @return true if equal, false otherwise
	 * @throws SQLException
	 */
	/*
	public boolean eq(Time other) throws SQLException{
		boolean result;
		result = other instanceof TimestampSet ? functions.set_eq(this._inner,((TimestampSet) other).get_inner()) : false;
		return result;
	}

	 */

	/*
	  Returns whether "this" and "other" are not equal.

	  <p>
	          MEOS Functions:
	          <ul>
	              <li>set_ne</li>
	          </ul>
	  </p>

	  @param other temporal object to compare with
	 * @return true if not equal, false otherwise
	 * @throws SQLException
	 */
	/*
	public boolean notEquals(Time other) throws SQLException{
		boolean result;
		result = other instanceof TimestampSet ? functions.set_ne(this._inner,((TimestampSet) other).get_inner()) : true;
		return result;
	}

	 */


	/*
	  Return whether "this" is less than "other".

	  <p>
	          MEOS Functions:
	          <ul>
	              <li>set_lt</li>
	          </ul>
	  </p>

	  @param other temporal object to compare with
	 * @return true if less than, false otherwise
	 * @throws SQLException
	 */
	/*
	public boolean lessThan(Time other) throws SQLException{
		if (other instanceof TimestampSet){
			return functions.set_lt(this._inner,((TimestampSet) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	 */


	/*
	  Return whether "this" is less than or equal to "other".

	  <p>
	          MEOS Functions:
	          <ul>
	              <li>set_le</li>
	          </ul>

	  </p>

	  @param other temporal object to compare with
	 * @return true if less than or equal, false otherwise
	 * @throws SQLException
	 */
	/*
	public boolean lessThanOrEqual(Time other) throws SQLException{
		if (other instanceof TimestampSet){
			return functions.set_le(this._inner,((TimestampSet) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	 */


	/*
	  Return whether "this" is greater than "other".

	  <p>
	          MEOS Functions:
	          <ul>
	              <li>set_gt</li>
	          </ul>
	  </p>

	  @param other temporal object to compare with
	 * @return true if greater than, false otherwise
	 * @throws SQLException
	 */
	/*
	public boolean greaterThan(Time other) throws SQLException{
		if (other instanceof TimestampSet){
			return functions.set_gt(this._inner,((TimestampSet) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	 */


	/**
	 * Return whether "this" is greater than or equal to "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>set_ge</li>
	 *         </ul>
	 * </p>
	 *
	 * @return true if greater than or equal, false otherwise
	 */
	/*
	public boolean greaterThanOrEqual(Time other) throws SQLException{
		if (other instanceof TimestampSet){
			return functions.set_ge(this._inner,((TimestampSet) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	 */

}
