package types.collections.time;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Memory;
import functions.GeneratedFunctions;
import types.TemporalObject;
import types.boxes.Box;
import types.collections.base.Base;
import types.collections.base.Set;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import functions.GeneratedFunctions;
import java.util.List;
import java.util.Objects;

//import types.boxes.*;

//import types.temporal.Temporal;
import types.collections.base.Span;
import types.collections.base.SpanSet;
import types.temporal.Temporal;
import utils.ConversionUtils;


/**
 * Class for representing lists of distinct timestamp values.
 *	<pre>
 *     ``tstzset`` objects can be created with a single argument of type string
 *     as in MobilityDB.
 *
 *         >>> tstzset(string='{2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01, 2019-09-11 00:00:00+01}')
 *
 *     Another possibility is to give a tuple or list of composing timestamps,
 *     which can be instances of ``str`` or ``datetime``. The composing timestamps
 *     must be given in increasing order.
 *
 *         >>> tstzset(timestamp_list=['2019-09-08 00:00:00+01', '2019-09-10 00:00:00+01', '2019-09-11 00:00:00+01'])
 *         >>> tstzset(timestamp_list=[parse('2019-09-08 00:00:00+01'), parse('2019-09-10 00:00:00+01'), parse('2019-09-11 00:00:00+01')])
 * </pre>
 * @author Arijit Samal
 */
public class tstzset extends Set<LocalDateTime> implements Time, TimeCollection {
	private final List<OffsetDateTime> dateTimeList = null;
	private Pointer _inner;


    /* ------------------------- Constructors ---------------------------------- */


	/**
	 * The default constructor
	 */
	public tstzset() {
	}

	/**
	 * Pointer constructor
	 * @param _inner Pointer
	 */
	public tstzset(Pointer _inner)  {
		super(_inner);
		this._inner = _inner;
		//String str = GeneratedFunctions.timestampset_out(this._inner);
	}
	
	
	/**
	 * The string constructor
	 *
	 * @param value - a string with a tstzset value
	 */
	public tstzset(String value) {
		super(value);
		this._inner = GeneratedFunctions.tstzset_in(value);
	}


	@Override
	public Pointer createStringInner(String str){
		return GeneratedFunctions.tstzset_in(str);
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
	 * @return a new tstzset instance
	 */
//	public tstzset copy() {
//		return new tstzset(GeneratedFunctions.tstz(this._inner));
//	}


	/**
	 * Returns a "tstzset" from its WKB representation in hex-encoded ASCII.
	 *  <p>
	 *         MEOS Functions:
	 *             <li>set_from_hexwkb</li>
	 * @param hexwkb WKB representation in hex-encoded ASCII
	 * @return a new tstzset instance
	 */
//	public static tstzset from_hexwkb(String hexwkb) {
//		Pointer result = GeneratedFunctions.tstzset_(hexwkb);
//		return new tstzset(result);
//	}

	public static tstzset from_hexwkb(String hexwkb) {
		Pointer result = GeneratedFunctions.set_from_hexwkb(hexwkb);
		return new tstzset(result);
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
		return GeneratedFunctions.tstzset_out(this._inner);
	}


    /* ------------------------- Conversions ----------------------------------- */


	/**
	 * Returns a tstzspanset that contains a tstzspan for each Timestamp in "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_to_spanset</li>
	 * @return a new tstzspanset instance
	 */
	public tstzspanset to_periodset() {
		return new tstzspanset(GeneratedFunctions.set_to_spanset(this.get_inner()));
	}

	/**
	 * Returns a period that encompasses "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_span</li>
	 * @return a new tstzspan instance
	 */
	public tstzspan to_span() {
		return new tstzspan(GeneratedFunctions.set_to_span(this._inner));
	}

	public tstzspanset to_spanset() {
		return new tstzspanset(GeneratedFunctions.set_to_spanset(this._inner));
	}

	/**
	 * Returns a period that encompasses "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_span</li>
	 * @return a new tstzspan instance
	 */
	public tstzspan to_period() {
		return this.to_span();
	}

	public Duration duration(){
		return ConversionUtils.interval_to_timedelta(GeneratedFunctions.tstzspan_duration(GeneratedFunctions.set_to_span(this._inner)));
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
		return GeneratedFunctions.set_num_values(this._inner);
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
		return ConversionUtils.timestamptz_to_datetime(GeneratedFunctions.tstzset_start_value(this._inner));
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
		return ConversionUtils.timestamptz_to_datetime(GeneratedFunctions.tstzset_end_value(this._inner));
	}

	public LocalDateTime element_n(int n) throws Exception {
		super.element_n(n);
		return ConversionUtils.timestamptz_to_datetime(OffsetDateTime.parse(Objects.requireNonNull(GeneratedFunctions.tstzset_value_n(this._inner, n + 1)).toString()));
	}

	public List<LocalDateTime> elements() throws Exception {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer count = Memory.allocate(runtime, Integer.BYTES);
		Pointer dp= GeneratedFunctions.tstzset_values(this._inner, count);
		long size= count.getInt(0);
		List<LocalDateTime> dateTimeList= new ArrayList<LocalDateTime>();
		for(int i=0; i<size; i++) {
			dateTimeList.add(ConversionUtils.timestamptz_to_datetime(OffsetDateTime.parse(dp.getPointer((long) i *Long.BYTES).toString())));
		}
		return dateTimeList;
	}



	public tstzset scale(Integer duration){
		return new tstzset(this.shift_scale(0, duration)._inner);
	}

	public tstzset shift(Integer shift, Integer duration){
		return new tstzset(this.shift_scale(shift, 0)._inner);
	}

	public tstzset shift_scale(Integer shift, Integer duration){
		return new tstzset(GeneratedFunctions.tstzset_shift_scale(this._inner, ConversionUtils.timedelta_to_interval(Duration.ofDays(shift)), ConversionUtils.timedelta_to_interval(Duration.ofDays(duration))));
	}

	/**
	 * Return the hash representation of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_hash</li>
	 * @return a new Integer instance
	 */
	public long hash(){
		return GeneratedFunctions.set_hash(this._inner);
	}


    /* ------------------------- Topological Operations ------------------------ */


    /**
     * Returns whether "this" is temporally adjacent to "other". That is, they share a bound but only one of them
     *         contains it.
     * <pre>
     *         Examples:
     *             >>> tstzset('{2012-01-01, 2012-01-02}').is_adjacent(tstzspan('[2012-01-02, 2012-01-03]'))
     *             >>> True
     *             >>> tstzset('{2012-01-01, 2012-01-02}').is_adjacent(tstzspan('[2012-01-02, 2012-01-03]'))
     *             >>> False  # Both contain bound
     *             >>> tstzset('{2012-01-01, 2012-01-02}').is_adjacent(tstzspan('(2012-01-02, 2012-01-03]'))
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
	public boolean is_adjacent(TemporalObject other) throws Exception {
		if(other instanceof Span){
			return super.is_adjacent((Base) other);
		}
		else if (other instanceof SpanSet){
			return super.is_adjacent((Base) other);
		}
		else if (other instanceof Temporal){
			return this.is_adjacent(((Temporal<?>) other).time());
		}
		else if (other instanceof Box){
			return GeneratedFunctions.adjacent_span_span(GeneratedFunctions.set_to_span(this._inner), ((Box) other).to_period().get_inner());
		}
		else{
			return super.is_adjacent((Base) other);
		}
	}


    /**
      Returns whether "this" is temporally contained in "other".
     <pre>
              Examples:
                  >>> tstzset('{2012-01-02, 2012-01-03}').is_contained_in(tstzspan('[2012-01-01, 2012-01-04]'))
                  >>> True
                  >>> tstzset('{2012-01-01, 2012-01-02}').is_contained_in(tstzspan('[2012-01-01, 2012-01-02]'))
                  >>> True
                  >>> tstzset('{2012-01-01, 2012-01-02}').is_contained_in(tstzspan('(2012-01-01, 2012-01-02)'))
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
			case tstzspan p -> returnValue = GeneratedFunctions.contained_span_span(GeneratedFunctions.set_to_span(this._inner), p.get_inner());
			case tstzspanset ps -> returnValue = GeneratedFunctions.contained_spanset_spanset(GeneratedFunctions.set_to_span(this._inner), ps.get_inner());
			case Temporal t -> returnValue = this.is_contained_in(t.time());
			case Box b -> returnValue = GeneratedFunctions.contained_span_span(GeneratedFunctions.set_to_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_contained_in((Base) other);
		}
		return returnValue;
	}




    /**
     * Returns whether "this" temporally contains "other".
     *<pre>
     *         Examples:
     *             >>> tstzset('{2012-01-01, 2012-01-04}').contains(parse('2012-01-01]'))
     *             >>> True
     *             >>> tstzset('{2012-01-01, 2012-01-02}').contains(tstzset('{2012-01-01}'))
     *             >>> True
     *             >>> tstzset('{2012-01-01, 2012-01-02}').contains(tstzset('{2012-01-01, 2012-01-03}'))
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
			case tstzset ts -> returnValue = GeneratedFunctions.contains_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.contains(t.time());
			default -> returnValue = super.contains((Base) other);
		}
		return returnValue;
	}


    /**
      Returns whether "this" temporally overlaps "other". That is, both share at least an instant
      <pre>
              Examples:
                  >>> tstzset('{2012-01-01, 2012-01-02}').overlaps(tstzset('{2012-01-02, 2012-01-03}'))
                  >>> True
                  >>> tstzset('{2012-01-01, 2012-01-02}').overlaps(tstzspan('[2012-01-02, 2012-01-03]'))
                  >>> True
                  >>> tstzset('{2012-01-01, 2012-01-02}').overlaps(tstzspan('(2012-01-02, 2012-01-03]'))
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
			case tstzspan p -> returnValue = GeneratedFunctions.overlaps_span_span(GeneratedFunctions.set_to_span(this._inner), p.get_inner());
			case tstzspanset ps -> returnValue = GeneratedFunctions.overlaps_spanset_spanset(GeneratedFunctions.set_to_spanset(this._inner), ps.get_inner());
			case tstzset ts -> returnValue = GeneratedFunctions.overlaps_set_set(this._inner, ts.get_inner());
			case Box b -> returnValue = GeneratedFunctions.overlaps_span_span(GeneratedFunctions.set_to_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.overlaps((Base) other);
		}
		return returnValue;
	}




    /**
     * Returns whether the bounding period of "this" is the same as the bounding period of "other".
     *
     *         See Also:
     *             {@link tstzspan#is_same(TemporalObject)}
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
     *             >>> tstzset('{2012-01-02, 2012-01-03}').is_after(tstzspan('[2012-01-01, 2012-01-02)'))
     *             >>> True
     *             >>> tstzset('{2012-01-02, 2012-01-03}').is_after(tstzset('{2012-01-01}'))
     *             >>> True
     *             >>> tstzset('{2012-01-02, 2012-01-03}').is_after(tstzspan('[2012-01-01, 2012-01-02]'))
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
			case tstzspan p -> returnValue = GeneratedFunctions.right_span_span(GeneratedFunctions.set_to_span(this._inner), p.get_inner());
			case tstzspanset ps -> returnValue = GeneratedFunctions.right_span_spanset(GeneratedFunctions.set_to_span(this._inner), ps.get_inner());
			case tstzset ts -> returnValue = GeneratedFunctions.right_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.to_period().is_after(other);
			case Box b -> returnValue = GeneratedFunctions.right_span_span(GeneratedFunctions.set_to_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_left((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is strictly before "other". That is, "this" ends before "other" starts.
	 *
	 * <pre>
	 *         Examples:
	 *             >>> tstzset('{2012-01-01, 2012-01-02}').is_before(tstzset('{2012-01-03}'))
	 *             >>> True
	 *             >>> tstzset('{2012-01-01, 2012-01-02}').is_before(tstzspan('(2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzset('{2012-01-01, 2012-01-02}').is_before(tstzspan('[2012-01-02, 2012-01-03]'))
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
			case tstzspan p -> returnValue = GeneratedFunctions.left_span_span(GeneratedFunctions.set_to_span(this._inner), p.get_inner());
			case tstzspanset ps -> returnValue = GeneratedFunctions.left_span_spanset(GeneratedFunctions.set_to_span(this._inner), ps.get_inner());
			case tstzset ts -> returnValue = GeneratedFunctions.left_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.to_period().is_before(other);
			case Box b -> returnValue = GeneratedFunctions.left_span_span(GeneratedFunctions.set_to_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_left((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is after "other" allowing overlap. That is, "this" starts after "other" starts
	 *         (or at the same time).
	 * <pre>
	 *         Examples:
	 *             >>> tstzset('{2012-01-02, 2012-01-03}').is_over_or_after(tstzspan('[2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> tstzset('{2012-01-02, 2012-01-03}').is_over_or_after(tstzspan('[2012-01-01, 2012-01-02]'))
	 *             >>> True
	 *             >>> tstzset('{2012-01-02, 2012-01-03}').is_over_or_after(tstzspan('[2012-01-01, 2012-01-03]'))
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
			case tstzspan p -> returnValue = GeneratedFunctions.overright_span_span(GeneratedFunctions.set_to_span(this._inner), p.get_inner());
			case tstzspanset ps -> returnValue = GeneratedFunctions.overright_span_spanset(GeneratedFunctions.set_to_span(this._inner), ps.get_inner());
			case tstzset ts -> returnValue = GeneratedFunctions.overright_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.to_period().is_over_or_after(other);
			case Box b -> returnValue = GeneratedFunctions.overright_span_span(GeneratedFunctions.set_to_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_over_or_right((Base) other);
		}
		return returnValue;
	}


	/**
	 * Returns whether "this" is before "other" allowing overlap. That is, "this" ends before "other" ends (or
	 *         at the same time).
	 * <pre>
	 *         Examples:
	 *             >>> tstzset('{2012-01-01, 2012-01-02}').is_over_or_before(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzset('{2012-01-01, 2012-01-02}').is_over_or_before(tstzspan('[2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> tstzset('{2012-01-03, 2012-01-05}').is_over_or_before(tstzspan('[2012-01-01, 2012-01-04]'))
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
			case tstzspan p -> returnValue = GeneratedFunctions.overleft_span_span(GeneratedFunctions.set_to_span(this._inner), p.get_inner());
			case tstzspanset ps -> returnValue = GeneratedFunctions.overleft_span_spanset(GeneratedFunctions.set_to_span(this._inner), ps.get_inner());
			case tstzset ts -> returnValue = GeneratedFunctions.overleft_set_set(this._inner, ts.get_inner());
			case Temporal t -> returnValue = this.to_period().is_over_or_before(other);
			case Box b -> returnValue = GeneratedFunctions.overleft_span_span(GeneratedFunctions.set_to_span(this._inner), b.to_period().get_inner());
			default -> returnValue = super.is_over_or_left((Base) other);
		}
		return returnValue;
	}

    /* ------------------------- Distance Operations --------------------------- */
	public Duration distance(Object other) throws Exception {
		Duration answer = null;
		if (other instanceof LocalDateTime) {
			answer= Duration.ofSeconds((long)GeneratedFunctions.distance_set_timestamptz(this._inner, ConversionUtils.datetimeToTimestampTz((LocalDateTime) other)));
		} else if (other instanceof tstzset) {
			answer= Duration.ofSeconds((long)GeneratedFunctions.distance_tstzset_tstzset(this._inner, ((tstzset) other)._inner));
		} else if (other instanceof tstzspan) {
			answer= Duration.ofSeconds((long)this.to_span().distance((TemporalObject) other));
//					Duration.ofSeconds((long)GeneratedFunctions.distance_tstzspanset_tstzspan(this.to_spanset(tstzspan.class).get_inner(), ((tstzspan) other).get_inner()));
		} else if (other instanceof tstzspanset) {
			answer= Duration.ofSeconds((long)this.to_span().distance((TemporalObject) other));
//					Duration.ofSeconds((long)GeneratedFunctions.distance_tstzspanset_tstzspanset(this.to_spanset(tstzspan.class).get_inner(), ((tstzspanset) other).get_inner()));
		} else if (other instanceof Temporal) {
			answer= Duration.ofSeconds((long)this.to_span().distance((TemporalObject) other));
		} else if (other instanceof Box) {
			answer= Duration.ofSeconds((long)this.to_span().distance((TemporalObject) other));
		} else {
			throw new Exception("Operation not supported with "+ other + " type");
		}
        return answer;
    }

//	public Duration distance(Object other) throws Exception {
//		Duration answer = switch (other) {
//            case LocalDateTime localDateTime -> Duration.ofSeconds((long) GeneratedFunctions.distance_set_timestamptz(this._inner, ConversionUtils.datetimeToTimestampTz(localDateTime)));
//            case tstzset tstzset -> Duration.ofSeconds((long) GeneratedFunctions.distance_tstzset_tstzset(this._inner, tstzset._inner));
//            case tstzspan tstzspan -> Duration.ofSeconds((long) tstzspan.distance((TemporalObject) other));
//            case tstzspanset tstzspanset -> Duration.ofSeconds((long) tstzspanset.to_span().distance((TemporalObject) other));
//            case Temporal ts -> Duration.ofSeconds((long) this.to_span().distance((TemporalObject) other));
//            case Box box -> Duration.ofSeconds((long) this.to_span().distance((TemporalObject) other));
//            case null, default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
//        };
//        return answer;
//	}


//	public double distance(TemporalObject other) throws Exception {
//		double returnValue;
//		switch (other){
//			case tstzspan p -> returnValue = this.to_span().distance(other);
//			case tstzset ts -> returnValue = GeneratedFunctions.distance_tstzspanset_tstzspan(ts.get_inner(),this._inner);
//			case Box b -> returnValue = GeneratedFunctions.distance_tstzspan_tstzspan(this._inner, b.to_period().get_inner());
//			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
//		}
//		return returnValue;
//	}




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
	public Time intersection(TemporalObject other) throws Exception {
		Time returnValue = null;
		switch (other) {
			case tstzspan p -> returnValue = new tstzspanset(GeneratedFunctions.intersection_spanset_span(GeneratedFunctions.set_to_spanset(this._inner), p.get_inner()));
			case tstzspanset ps -> returnValue = new tstzspanset(GeneratedFunctions.intersection_spanset_spanset(GeneratedFunctions.set_to_spanset(this._inner),ps.get_inner()));
			case tstzset ts -> returnValue = new tstzset(GeneratedFunctions.intersection_set_set(this._inner,ts.get_inner()));
			case Temporal t -> returnValue = (Time) this.intersection(t.time());
			case Box b -> returnValue = (Time) this.intersection(b.to_period());
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
	public Time mul(TemporalObject other) throws Exception {
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
	public Time minus(TemporalObject other) throws Exception {
		Time returnValue = null;
		switch (other) {
			case tstzspan p -> returnValue = new tstzspanset(GeneratedFunctions.minus_spanset_span(GeneratedFunctions.set_to_spanset(this._inner), p.get_inner()));
			case tstzspanset ps -> returnValue = new tstzspanset(GeneratedFunctions.minus_spanset_spanset(GeneratedFunctions.set_to_spanset(this._inner),ps.get_inner()));
			case tstzset ts -> returnValue = new tstzset(GeneratedFunctions.minus_set_set(this._inner,ts.get_inner()));
			case Temporal t -> returnValue = (Time) this.minus(t.time());
			case Box b -> returnValue = (Time) this.minus(b.to_period());
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
	public Time sub(Time other) throws Exception {
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
	public Time union(TemporalObject other) throws Exception {
		Time returnValue = null;
		switch (other) {
			case tstzspan p -> returnValue = new tstzspan(GeneratedFunctions.union_spanset_span(GeneratedFunctions.set_to_spanset(this._inner),p.get_inner()));
			case tstzspanset ps -> returnValue = new tstzspanset(GeneratedFunctions.union_spanset_spanset(GeneratedFunctions.set_to_spanset(this._inner),ps.get_inner()));
			case tstzset ts -> returnValue =  new tstzset(GeneratedFunctions.union_set_set(this._inner,ts.get_inner()));
			case Temporal t -> returnValue = (Time) this.union(t.time());
			case Box b -> returnValue = (Time) this.union(b.to_period());
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
	public Time add(Time other) throws Exception {
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
		result = other instanceof tstzset ? GeneratedFunctions.set_eq(this._inner,((tstzset) other).get_inner()) : false;
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
		result = other instanceof tstzset ? GeneratedFunctions.set_ne(this._inner,((tstzset) other).get_inner()) : true;
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
		if (other instanceof tstzset){
			return GeneratedFunctions.set_lt(this._inner,((tstzset) other).get_inner());
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
		if (other instanceof tstzset){
			return GeneratedFunctions.set_le(this._inner,((tstzset) other).get_inner());
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
		if (other instanceof tstzset){
			return GeneratedFunctions.set_gt(this._inner,((tstzset) other).get_inner());
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
		if (other instanceof tstzset){
			return GeneratedFunctions.set_ge(this._inner,((tstzset) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	 */

}
