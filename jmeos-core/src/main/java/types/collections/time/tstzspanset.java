package types.collections.time;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.collections.base.Base;
import types.collections.base.SpanSet;

import types.boxes.*;
import types.temporal.Temporal;
import utils.ConversionUtils;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.util.List;
import functions.functions;


/**
 * Class for representing lists of disjoint periods.
 *<p>
 *     ``tstzspanset`` objects can be created with a single argument of type string
 *     as in MobilityDB.
 *<p>
 *         >>> tstzspanset(string='{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}')
 *<p>
 *     Another possibility is to give a list specifying the composing
 *     periods, which can be instances  of ``str`` or ``tstzspan``. The composing
 *     periods must be given in increasing order.
 *<p>
 *         >>> tstzspanset(period_list=['[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01]', '[2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]'])
 *         >>> tstzspanset(period_list=[tstzspan('[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01]'), tstzspan('[2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]')])
 *
 * @author ARIJIT SAMAL
 */
public class tstzspanset extends SpanSet<LocalDateTime> implements Time, TimeCollection {
	private final List<tstzspan> periodList = null;
	private Pointer _inner;

	/* ------------------------- Constructors ---------------------------------- */
	/**
	 * The default constructor
	 */
	public tstzspanset() {
	}


	/**
	 * Pointer Constructor
	 * @param _inner Pointer
	 */
	public tstzspanset(Pointer _inner) {
		super(_inner);
		this._inner = _inner;
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - a string with a tstzspanset value
	 */
	public tstzspanset(String value) {
		super(value);
		this._inner = functions.tstzspanset_in(value);
	}
	
	/**
	 * The array of Periods constructor
	 *
	 * @param periods - an array of Periods separated by a comma
	 */
	public tstzspanset(List<tstzspan> periods)  {
//		super((Pointer) periods);
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		for (int i = 0; i < periods.size(); i++) {
			tstzspan period = periods.get(i);
			System.out.println(period.toString());
			sb.append(period.toString());
			if (i < periods.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("}");
		System.out.println(sb);
		this._inner = functions.tstzspanset_in(sb.toString());
	}

	@Override
	public Pointer createStringInner(String str){
		return functions.tstzspanset_in(str);
	}

	@Override
	public Pointer createInner(Pointer inner){
		return _inner;
	}

	public Pointer createListInner(List<tstzspan> periods){
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		for (int i = 0; i < periods.size(); i++) {
			tstzspan period = periods.get(i);
			sb.append(period.toString());
			if (i < periods.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return functions.tstzspanset_in(sb.toString());
	}

	/**
	 * Return a copy of "this".
	 * <p>
	 * Meos Functions:
	 *
	 * <li>spanset_copy
	 *
	 * @return a new tstzspanset instance
	 */
	public Pointer copy()  {
		return functions.spanset_copy(this._inner);
	}


	/**
	 * Returns a "tstzspanset" from its WKB representation in hex-encoded ASCII.
	 * <p>
	 * MEOS Functions:
	 * <li>spanset_from_hexwkb
	 *
	 * @param str WKB representation in hex-encoded ASCII
	 * @return a new tstzspanset instance
	 */
	public Pointer from_hexwkb(String str)  {
        return functions.spanset_from_hexwkb(str);
	}


    /* ------------------------- Output ---------------------------------------- */


	/**
	 * Return the string representation of the content of "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>periodset_out
	 * @return a new String instance
	 */
	public String toString(){
		return functions.tstzspanset_out(this._inner);
	}
    /* ------------------------- Conversions ----------------------------------- */

	/**
	 * Returns a period that encompasses "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>spanset_span
	 * @return a new tstzspan instance
	 */
	public tstzspan to_period() {
		return new tstzspan(functions.spanset_span(this._inner));
	}


	/**
	 * Returns a period that encompasses "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>spanset_span
	 * @return a new tstzspan instance
	 */
	public tstzspan to_span() {
		return this.to_period();
	}

	/* ------------------------- Accessors ------------------------------------- */

	/**
	 * Returns the C inner object
	 * @return the inner Pointer
	 */
	public Pointer get_inner(){
		return this._inner;
	}


	/**
	 * Returns the number of timestamps in "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>periodset_num_timestamps
	 * @return an Integer instance
	 */
	public int num_timestamps(){
		return functions.tstzspanset_num_timestamps(this._inner);
	}

	/**
	 * Returns the first timestamp in "this".
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>periodset_start_timestamp</li>
	 * @return A {@link LocalDateTime} instance
	 */
	public LocalDateTime start_timestamp(){
		return ConversionUtils.timestamptz_to_datetime(functions.tstzspanset_start_timestamptz(this._inner));
	}

	/**
	 * Returns the last timestamp in "this".
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>periodset_end_timestamp</li>
	 * @return A {@link LocalDateTime} instance
	 */
	public LocalDateTime end_timestamp(){
		return ConversionUtils.timestamptz_to_datetime(functions.tstzspanset_end_timestamptz(this._inner));
	}


	/**
	 * Returns the nth timestamp of the tstzspanset
	 * @param n the nth element
	 * @return Returns the nth timestamp of the tstzspanset
	 */
	public LocalDateTime timestamp_n(int n){
		if (n < 0 || n > this.num_timestamps()){
			throw new UnsupportedOperationException("Parameter not valid") ;
		}
		return null;
	}

	/**
	 * Returns the number of periods in "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>spanset_num_spans
	 * @return an Integer instance
	 */
	public int num_periods(){
		return functions.spanset_num_spans(this._inner);
	}


	/**
	 * Returns the first period in "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>periodset_lower
	 * @return a new tstzspan instance
	 */
	public tstzspan start_period()  {
		return new tstzspan(functions.spanset_start_span(this._inner));
	}


	/**
	 * Returns the first period in "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>periodset_lower
	 * @return a new tstzspan instance
	 */
	public tstzspan start_span()  {
		return this.start_period();
	}

	/**
	 * Returns the last period in "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>periodset_upper
	 * @return a new tstzspan instance
	 */
	public tstzspan end_period() {
		return new tstzspan(functions.spanset_end_span(this._inner));
	}


	/**
	 * Returns the last period in "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>periodset_upper
	 * @return a new tstzspan instance
	 */
	public tstzspan end_span() {
		return this.end_period();
	}


	/**
	 * Return the hash representation of "this".
	 * <p>
	 * MEOS Functions:
	 *             <li>spanset_hash
	 * @return a new Integer instance
	 */
	public long hash(){
		return functions.spanset_hash(this._inner);
	}


    /* ------------------------- Topological Operations ------------------------ */

	/**
	 * Returns whether "this" is temporally adjacent to "other".
	 * That is, they share a bound but only one of them contains it.
	 * <pre>
	 * Examples:
	 * >>> tstzspanset('{[2012-01-01, 2012-01-02)}').is_adjacent(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 * >>> True
	 * >>> tstzspanset('{[2012-01-01, 2012-01-02]}').is_adjacent(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 * >>> False  # Both contain bound
	 * >>> tstzspanset('{[2012-01-01, 2012-01-02)}').is_adjacent(tstzspanset('{[(2012-01-02, 2012-01-03]]}'))
	 * >>> False  # Neither contain bound
	 * </pre>
	 * MEOS Functions:
	 * <ul>
	 *     <li>adjacent_spanset_span</li>
	 *     <li>adjacent_spanset_spanset</li>
	 *     <li>adjacent_periodset_timestamp</li>
	 *     <li>adjacent_periodset_timestampset</li>
	 * </ul>
	 *
	 * @param other temporal object to compare with
	 * @return True if adjacent, False otherwise
	 */
	public boolean is_adjacent(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.adjacent_spanset_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.adjacent_spanset_spanset(this._inner, ps.get_inner());
			case tstzset ts -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.set_to_spanset(ts.get_inner()));
			case Temporal t -> returnValue = is_adjacent((TemporalObject)t.period());
			case Box b -> returnValue = functions.adjacent_spanset_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.is_adjacent((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is temporally contained in "other".
	 * <pre>
	 *     Examples:
	 *     >>> tstzspanset('{[2012-01-02, 2012-01-03]}').is_contained_in(tstzspan('{[2012-01-01, 2012-01-04]}'))
	 *     >>> True
	 *     >>> tstzspanset('{(2012-01-01, 2012-01-02)}').is_contained_in(tstzspan('{[2012-01-01, 2012-01-02]}'))
	 *     >>> True
	 *     >>> tstzspanset('{[2012-01-01, 2012-01-02]}').is_contained_in(tstzspan('{(2012-01-01, 2012-01-02)}'))
	 *     >>> False
	 * </pre>
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>contained_spanset_span </li>
	 *             <li>contained_spanset_spanset </li>
	 *             <li>contained_periodset_temporal</li>
	 *         </ul>
	 * @param other temporal object to compare with
	 * @return True if contained, False otherwise
	 */
	public boolean is_contained_in(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.contained_spanset_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.contained_spanset_spanset(this._inner, ps.get_inner());
			case Temporal t -> returnValue = is_contained_in((TemporalObject)t.period());
			case Box b -> returnValue = functions.contained_spanset_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.is_contained_in((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether ``self`` temporally contains ``content``.
	 * <pre>
	 *     Examples:
	 *     >>> tstzspanset('{[2012-01-01, 2012-01-04]}').contains(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 *     >>> True
	 *     >>> tstzspanset('{[2012-01-01, 2012-01-02]}').contains(tstzspanset('{(2012-01-01, 2012-01-02)}'))
	 *     >>> True
	 *     >>> tstzspanset('{(2012-01-01, 2012-01-02)}').contains(tstzspanset('{[2012-01-01, 2012-01-02]}'))
	 *     >>> False
	 * </pre>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *         		<li>contains_spanset_span </li>
	 *         		<li>contains_spanset_spanset </li>
	 *         		<li>contains_periodset_timestamp </li>
	 *         </ul>
	 * @param other temporal object to compare with
	 * @return True if contains, False otherwise
	 */
	public boolean contains(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.contains_spanset_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.contains_spanset_spanset(this._inner, ps.get_inner());
			case tstzset ts -> returnValue = functions.contains_spanset_spanset(this._inner, functions.set_to_spanset(ts.get_inner()));
			case Temporal t -> returnValue = contains((TemporalObject)t.period());
			case Box b -> returnValue = functions.contains_spanset_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.contains((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" temporally overlaps "other". That is, both share at least an instant
	 *<pre>
	 *    Examples:
	 *    >>> tstzspanset('{[2012-01-01, 2012-01-02]}').overlaps(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 *    >>> True
	 *    >>> tstzspanset('{[2012-01-01, 2012-01-02)}').overlaps(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 *    >>> False
	 *    >>> tstzspanset('{[2012-01-01, 2012-01-02)}').overlaps(tstzspanset('{(2012-01-02, 2012-01-03]}'))
	 *    >>> False
	 *</pre>
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>overlaps_spanset_span</li>
	 *             <li>overlaps_spanset_spanset</li>
	 *         </ul>
	 * @param other temporal object to compare with
	 * @return True if overlaps, False otherwise
	 */
	public boolean overlaps(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.overlaps_spanset_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.overlaps_spanset_spanset(this._inner, ps.get_inner());
			case tstzset ts -> returnValue = functions.overlaps_spanset_spanset(this._inner, functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = overlaps((TemporalObject)t.period());
			case Box b -> returnValue = functions.overlaps_spanset_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.overlaps((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether the bounding period of "this" is the same as the bounding period of "other".
	 *<p>
	 *
	 *         See Also:
	 * 				{@link tstzspan#is_same(TemporalObject)}
	 * @param other A time or temporal object to compare to "this".
	 * @return True if same, False otherwise.
	 */
	public boolean is_same(TemporalObject other) throws Exception {
		return this.to_period().is_same(other);
	}


    /* ------------------------- Position Operations --------------------------- */


	/**
	 * Returns whether "this" is strictly before "other". That is, "this" ends before "other" starts.
	 * <pre>
	 *    Examples:
	 *    >>> tstzspanset('{[2012-01-01, 2012-01-02)}').is_before(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 *    >>> True
	 *    >>> tstzspanset('{[2012-01-01, 2012-01-02)}').is_before(tstzspanset('{(2012-01-02, 2012-01-03]}'))
	 *    >>> True
	 *    >>> tstzspanset('{[2012-01-01, 2012-01-02]}').is_before(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 *    >>> False
	 *</pre>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *         <li>before_periodset_timestamp</li>
	 *         <li>left_spanset_span</li>
	 *         <li>left_spanset_spanset</li>
	 *         </ul>
	 * @param other temporal object to compare with
	 * @return True if before, False otherwise
	 */
	public boolean is_before(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.left_spanset_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.left_spanset_spanset(this._inner, ps.get_inner());
			case tstzset ts -> returnValue = functions.left_spanset_spanset(this._inner, functions.set_to_spanset(ts.get_inner()));
			case Temporal t -> returnValue = is_before((TemporalObject)t.period());
			case Box b -> returnValue = functions.left_spanset_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.is_left((Base) other);
		}
		return returnValue;
	}


	/**
	 *  Returns whether "this" is before "other" allowing overlap. That is, "this" ends before "other" ends (or
	 *         at the same time).
	 * <pre>
	 *    Examples:
	 *    >>> tstzspanset('{[2012-01-01, 2012-01-02)}').is_over_or_before(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 *    >>> True
	 *    >>> tstzspanset('{[2012-01-01, 2012-01-02]}').is_over_or_before(tstzspanset('{[2012-01-02, 2012-01-03]}'))
	 *    >>> True
	 *    >>> tstzspanset('{[2012-01-03, 2012-01-05]}').is_over_or_before(tstzspanset('{[2012-01-01, 2012-01-04]}'))
	 *    >>> False
	 *</pre>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>overleft_spanset_span</li>
	 *             <li>overleft_spanset_spanset</li>
	 *             <li>overbefore_periodset_timestamp</li>
	 *             <li>overbefore_periodset_timestampset</li>
	 *             <li>overbefore_periodset_temporal</li>
	 *         </ul>
	 * @param other temporal object to compare with
	 * @return True if before, False otherwise
	 */
	public boolean is_over_or_before(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.overleft_spanset_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.overleft_spanset_spanset(this._inner, ps.get_inner());
			case tstzset ts -> returnValue = functions.overleft_spanset_span(this._inner, functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = is_over_or_before((TemporalObject)t.period());
			case Box b -> returnValue = functions.overleft_spanset_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.is_over_or_left((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is strictly after "other".That is, "this" starts after "other" ends.
	 *<pre>
	 *    Examples:
	 *    >>> tstzspanset('{[2012-01-02, 2012-01-03]}').is_after(tstzspanset('{[2012-01-01, 2012-01-02)}'))
	 *    >>> True
	 *    >>> tstzspanset('{(2012-01-02, 2012-01-03]}').is_after(tstzspanset('{[2012-01-01, 2012-01-02)}'))
	 *    >>> True
	 *    >>> tstzspanset('{[2012-01-02, 2012-01-03]}').is_after(tstzspanset('{[2012-01-01, 2012-01-02]}'))
	 *    >>> False
	 *</pre>
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>right_spanset_span </li>
	 *             <li>right_spanset_spanset </li>
	 *             <li>overbefore_timestamp_periodset </li>
	 *          </ul>
	 * @param other temporal object to compare with
	 * @return True if after, False otherwise
	 */
	public boolean is_after(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.right_spanset_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.right_spanset_spanset(this._inner, ps.get_inner());
			case tstzset ts -> returnValue = functions.right_spanset_span(this._inner, functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = is_after((TemporalObject)t.period());
			case Box b -> returnValue = functions.right_spanset_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.is_right((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is after "other" allowing overlap. That is, "this" starts after "other" starts
	 *         (or at the same time).
	 *<pre>
	 *    Examples:
	 *    >>> tstzspanset('{[2012-01-02, 2012-01-03]}').is_over_or_after(tstzspanset('{[2012-01-01, 2012-01-02)}'))
	 *    >>> True
	 *    >>> tstzspanset('{[2012-01-02, 2012-01-03]}').is_over_or_after(tstzspanset('{[2012-01-01, 2012-01-02]}'))
	 *    >>> True
	 *    >>> tstzspanset('{[2012-01-02, 2012-01-03]}').is_over_or_after(tstzspanset('{[2012-01-01, 2012-01-03]}'))
	 *    >>> False
	 *</pre>
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>overright_spanset_span</li>
	 *             <li>overright_spanset_spanset</li>
	 *             <li>overafter_periodset_timestamp</li>
	 *             <li>overafter_periodset_timestampset</li>
	 *         </ul>
	 * @param other temporal object to compare with
	 * @return True if overlapping or after, False otherwise
	 */
	public boolean is_over_or_after(TemporalObject other) throws Exception {
		boolean returnValue;
		switch (other) {
			case tstzspan p -> returnValue = functions.overright_spanset_span(this._inner, p.get_inner());
			case tstzspanset ps -> returnValue = functions.overright_spanset_spanset(this._inner, ps.get_inner());
			case tstzset ts -> returnValue = functions.overright_spanset_span(this._inner, functions.set_to_span(ts.get_inner()));
			case Temporal t -> returnValue = is_over_or_after((TemporalObject)t.period());
			case Box b -> returnValue = functions.overright_spanset_span(this._inner, b.to_period().get_inner());
			default -> returnValue = super.is_over_or_right((Base) other);
		}
		return returnValue;
	}

    /* ------------------------- Distance Operations --------------------------- */


    /* ------------------------- Set Operations -------------------------------- */


	/**
	 * Returns the temporal intersection of "this" and "other".
	 *<p>
	 *         MEOS Functions:
	 *         <ul>
	 *         		<li>intersection_periodset_timestamp</li>
	 *         		<li>intersection_spanset_spanset</li>
	 *         		<li>intersection_spanset_span</li>
	 *         </ul>
	 * @param other temporal object to intersect with
	 * @return a Time instance. The actual class depends on "other"
	 */
	public Time intersection(Time other) {
		Time returnValue;
		switch (other) {
			case tstzspan p -> returnValue = new tstzspan(functions.intersection_spanset_span(this._inner,p.get_inner()));
			case tstzspanset ps -> returnValue = new tstzspan(functions.intersection_spanset_spanset(this._inner,ps.get_inner()));
			case tstzset ts -> returnValue = new tstzspan(functions.intersection_spanset_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}

	/**
	 * Returns the temporal intersection of "this" and "other".
	 *<p>
	 *         MEOS Functions:
	 *         <ul>
	 *         		<li>intersection_periodset_timestamp</li>
	 *         		<li>intersection_spanset_spanset</li>
	 *         		<li>intersection_spanset_span</li>
	 *         </ul>
	 * @param other temporal object to intersect with
	 * @return a Time instance. The actual class depends on "other"
	 */
	public Time mul(Time other) {
		return this.intersection(other);
	}


	/**
	 * Returns the temporal difference of "this" and "other".
	 *<p>
	 *         MEOS Functions:
	 *         <ul>
	 *         		<li>minus_spanset_span</li>
	 *         		<li>minus_spanset_spanset</li>
	 *         		<li>minus_periodset_timestamp</li>
	 *         </ul>
	 *
	 * @param other temporal object to diff with
	 * @return a tstzspanset instance
	 */
	public tstzspanset minus(Time other) {
		tstzspanset returnValue;
		switch (other) {
			case tstzspan p -> returnValue = new tstzspanset(functions.minus_spanset_span(this._inner,p.get_inner()));
			case tstzspanset ps -> returnValue = new tstzspanset(functions.minus_spanset_spanset(this._inner,ps.get_inner()));
			case tstzset ts -> returnValue = new tstzspanset(functions.minus_spanset_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	/**
	 * Returns the temporal difference of "this" and "other".
	 *<p>
	 *         MEOS Functions:
	 *         <ul>
	 *         		<li>minus_spanset_span</li>
	 *         		<li>minus_spanset_spanset</li>
	 *         		<li>minus_periodset_timestamp</li>
	 *         </ul>
	 *
	 * @param other temporal object to diff with
	 * @return a tstzspanset instance
	 */
	public tstzspanset sub(Time other) {
		return this.minus(other);
	}


	/**
	 * Returns the temporal union of "this" and "other".
	 * <p>
	 *         MEOS Functions:
	 *         <ul>
	 *         		<li>union_periodset_timestamp</li>
	 *         		<li>union_spanset_spanset</li>
	 *         		<li>union_spanset_span</li>
	 *         </ul>
	 *
	 * @param other temporal object to merge with
	 * @return a tstzspanset instance
	 */
	public tstzspanset union(Time other) {
		tstzspanset returnValue;
		switch (other) {
			case tstzspan p -> returnValue = new tstzspanset(functions.union_spanset_span(this._inner,p.get_inner()));
			case tstzspanset ps -> returnValue = new tstzspanset(functions.union_spanset_spanset(this._inner,ps.get_inner()));
			case tstzset ts -> returnValue = new tstzspanset(functions.union_spanset_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	/**
	 * Returns the temporal union of "this" and "other".
	 * <p>
	 *         MEOS Functions:
	 *         <ul>
	 *         		<li>union_periodset_timestamp</li>
	 *         		<li>union_spanset_spanset</li>
	 *         		<li>union_spanset_span</li>
	 *         </ul>
	 *
	 * @param other temporal object to merge with
	 * @return a tstzspanset instance
	 */
	public tstzspanset add(Time other) {
		return this.union(other);
	}




    /* ------------------------- Comparisons ----------------------------------- */


	/**
	 *  Return whether "this" and "other" are equal.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>spanset_eq </li>
	 * @param other temporal object to compare with
	 * @return True if equal, False otherwise
	 */
	public boolean eq(Time other) {
		boolean result;
		result = other instanceof tstzspanset && functions.spanset_eq(this._inner, ((tstzspanset) other).get_inner());
		return result;
	}

	/**
	 * Return whether "this" and "other" are not equal.
	 *
	 *<p>
	 *         MEOS Functions:
	 *             <li>spanset_ne </li>
	 *
	 * @param other temporal object to compare with
	 * @return True if not equal, False otherwise
	 */
	public boolean notEquals(Time other) {
		boolean result;
		result = !(other instanceof tstzspanset) || functions.spanset_ne(this._inner, ((tstzspanset) other).get_inner());
		return result;
	}


	/**
	 * Return whether "this" is less than "other".
	 *
	 *<p>
	 *         MEOS Functions:
	 *             <li>spanset_lt</li>
	 *
	 * @param other temporal object to compare with
	 * @return True if less than, False otherwise
	 */
	public boolean lessThan(Time other) throws OperationNotSupportedException {
		if (other instanceof tstzspanset){
			return functions.spanset_lt(this._inner,((tstzspanset) other).get_inner());
		}
		else{
			throw new OperationNotSupportedException("Operation not supported with this type.");
		}
	}


	/**
	 * Return whether "this" is less than or equal to "other.
	 *<p>
	 *         MEOS Functions:
	 *             <li>spanset_le</li>
	 * @param other temporal object to compare with
	 * @return True if less than or equal, False otherwise
	 */
	public boolean lessThanOrEqual(Time other) throws OperationNotSupportedException {
		if (other instanceof tstzspanset){
			return functions.spanset_le(this._inner,((tstzspanset) other).get_inner());
		}
		else{
			throw new OperationNotSupportedException("Operation not supported with this type.");
		}
	}


	/**
	 * Return whether "this" is greater than "other".
	 *<p>
	 *
	 *         MEOS Functions:
	 *             <li>spanset_gt</li>
	 * @param other temporal object to compare with
	 * @return True if greater than, False otherwise
	 */
	public boolean greaterThan(Time other) throws OperationNotSupportedException {
		if (other instanceof tstzspanset){
			return functions.spanset_gt(this._inner,((tstzspanset) other).get_inner());
		}
		else{
			throw new OperationNotSupportedException("Operation not supported with this type.");
		}
	}

	/**
	 * Return whether ``self`` is greater than or equal to ``other``.
	 * <p>
	 *         MEOS Functions:
	 *             <li>spanset_ge</li>
	 *
	 * @param other temporal object to compare with
	 * @return True if greater than or equal, False otherwise
	 */
	public boolean greaterThanOrEqual(Time other) throws OperationNotSupportedException {
		if (other instanceof tstzspanset){
			return functions.spanset_ge(this._inner,((tstzspanset) other).get_inner());
		}
		else{
			throw new OperationNotSupportedException("Operation not supported with this type.");
		}
	}


}
	


