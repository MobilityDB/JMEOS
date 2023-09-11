package types.time;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import types.boxes.*;

import functions.functions;


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
@TypeName(name = "timestampset")
public class TimestampSet extends Time {
	private final List<OffsetDateTime> dateTimeList;


	/** ------------------------- Constructors ---------------------------------- */


	/**
	 * The default constructor
	 */
	public TimestampSet() {
		super();
		dateTimeList = new ArrayList<>();
	}
	
	public TimestampSet(Pointer _inner) throws SQLException {
		this();
		this._inner = _inner;
		String str = functions.timestampset_out(this._inner);
		setValue(str);
	}
	
	
	/**
	 * The string constructor
	 *
	 * @param value - a string with a TimestampSet value
	 * @throws SQLException
	 */
	public TimestampSet(String value) throws SQLException {
		this();
		setValue(value);
		this._inner = functions.timestampset_in(value);
	}
	
	/**
	 * The array of OffsetDateTime constructor
	 *
	 * @param dateTimes - an array of OffsetDateTime or OffsetDateTime separated by a comma
	 * @throws SQLException
	 */
	public TimestampSet(OffsetDateTime... dateTimes) throws SQLException {
		this();
		Collections.addAll(dateTimeList, dateTimes);
		validate();
	}


	/**
	 * Return a copy of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_copy</li>
	 * @return a new TimestampSet instance
	 * @throws SQLException
	 */
	public TimestampSet copy() throws SQLException {
		return new TimestampSet(functions.set_copy(this._inner));
	}

	/*
	public static TimestampSet from_wkb(byte[] wkb){
		return new TimestampSet(functions.set_from_)
	}
	 */


	/**
	 * Returns a "TimestampSet" from its WKB representation in hex-encoded ASCII.
	 *  <p>
	 *         MEOS Functions:
	 *             <li>set_from_hexwkb</li>
	 * @param hexwkb WKB representation in hex-encoded ASCII
	 * @return a new TimestampSet instance
	 * @throws SQLException
	 */
	public static TimestampSet from_hexwkb(String hexwkb) throws SQLException {
		Pointer result = functions.set_from_hexwkb(hexwkb);
		return new TimestampSet(result);
	}

	/** ------------------------- Output ---------------------------------------- */


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


	/** ------------------------- Conversions ----------------------------------- */


	/**
	 * Returns a PeriodSet that contains a Period for each Timestamp in "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_to_spanset</li>
	 * @return a new PeriodSet instance
	 * @throws SQLException
	 */
	public PeriodSet to_periodset() throws SQLException {
		return new PeriodSet(functions.set_to_spanset(this.get_inner()));
	}

	/** ------------------------- Accessors ------------------------------------- */

	/**
	 * Returns a period that encompasses "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_span</li>
	 * @return a new Period instance
	 * @throws SQLException
	 */
	public Period period() throws SQLException {
		return new Period(functions.set_span(this._inner));
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
	 * Return the hash representation of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>set_hash</li>
	 * @return a new Integer instance
	 */
	public int hash(){
		return functions.set_hash(this._inner);
	}


	/** ------------------------- Topological Operations ------------------------ */


	public boolean isAdjacent(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.adjacent_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.adjacent_spanset_spanset(ps.get_inner(), functions.set_to_spanset(this._inner));
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.adjacent_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	public boolean is_contained_in(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.contained_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.contained_spanset_spanset(functions.set_span(this._inner), ps.get_inner());
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.contained_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public boolean contains(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case TimestampSet ts -> returnValue = functions.contains_set_set(this._inner, ts.get_inner());
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}




	public boolean overlaps(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overlaps_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.overlaps_spanset_spanset(functions.set_to_spanset(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overlaps_set_set(this._inner, ts.get_inner());
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.overlaps_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}

	public boolean is_same(TemporalObject<?> other) throws SQLException {
		return this.period().is_same(other);
	}




	public boolean is_after(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.right_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.right_span_spanset(functions.set_span(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.right_set_set(this._inner, ts.get_inner());
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.right_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public boolean is_before(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.left_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.left_span_spanset(functions.set_span(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.left_set_set(this._inner, ts.get_inner());
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.left_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	public boolean is_over_or_after(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overright_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.overright_span_spanset(functions.set_span(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overright_set_set(this._inner, ts.get_inner());
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.overright_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}




	public boolean is_over_or_before(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overleft_span_span(functions.set_span(this._inner), p.get_inner());
			case PeriodSet ps -> returnValue = functions.overleft_span_spanset(functions.set_span(this._inner), ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overleft_set_set(this._inner, ts.get_inner());
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.overleft_span_span(functions.set_span(this._inner), b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	public Time intersection(TemporalObject<?> other) throws SQLException {
		Time returnValue = null;
		switch (other) {
			case Period p -> returnValue = new PeriodSet(functions.intersection_spanset_span(functions.set_to_spanset(this._inner), p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.intersection_spanset_spanset(functions.set_to_spanset(this._inner),ps.get_inner()));
			case TimestampSet ts -> returnValue = new TimestampSet(functions.intersection_set_set(this._inner,ts.get_inner()));
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	public Time mul(TemporalObject<?> other) throws SQLException {
		return this.intersection(other);
	}



	public Time minus(TemporalObject<?> other) throws SQLException {
		Time returnValue = null;
		switch (other) {
			case Period p -> returnValue = new PeriodSet(functions.minus_spanset_span(functions.set_to_spanset(this._inner), p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.minus_spanset_spanset(functions.set_to_spanset(this._inner),ps.get_inner()));
			case TimestampSet ts -> returnValue = new TimestampSet(functions.minus_set_set(this._inner,ts.get_inner()));
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public Time sub(TemporalObject<?> other) throws SQLException {
		return this.minus(other);
	}


	public Time union(TemporalObject<?> other) throws SQLException {
		Time returnValue = null;
		switch (other) {
			case Period p -> returnValue = new Period(functions.union_spanset_span(functions.set_to_spanset(this._inner),p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.union_spanset_spanset(functions.set_to_spanset(this._inner),ps.get_inner()));
			case TimestampSet ts -> returnValue =  new TimestampSet(functions.union_set_set(this._inner,ts.get_inner()));
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}






	public TemporalObject<?> add(TemporalObject<?> other) throws SQLException {
		return this.union(other);
	}



	public boolean equals(TemporalObject<?> other) throws SQLException{
		boolean result;
		result = other instanceof TimestampSet ? functions.set_eq(this._inner,((TimestampSet) other).get_inner()) : false;
		return result;
	}

	public boolean notEquals(TemporalObject<?> other) throws SQLException{
		boolean result;
		result = other instanceof TimestampSet ? functions.set_ne(this._inner,((TimestampSet) other).get_inner()) : true;
		return result;
	}

	public boolean lessThan(TemporalObject<?> other) throws SQLException{
		if (other instanceof TimestampSet){
			return functions.set_lt(this._inner,((TimestampSet) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	public boolean lessThanOrEqual(TemporalObject<?> other) throws SQLException{
		if (other instanceof TimestampSet){
			return functions.set_le(this._inner,((TimestampSet) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	public boolean greaterThan(TemporalObject<?> other) throws SQLException{
		if (other instanceof TimestampSet){
			return functions.set_gt(this._inner,((TimestampSet) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}


	public boolean greaterThanOrEqual(TemporalObject<?> other) throws SQLException{
		if (other instanceof TimestampSet){
			return functions.set_ge(this._inner,((TimestampSet) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}




	
	@Override
	public String getValue() {
		return String.format("{%s}", dateTimeList.stream()
				.map(DateTimeFormatHelper::getStringFormat)
				.collect(Collectors.joining(", ")));
	}
	
	@Override
	public void setValue(String value) throws SQLException {
		String trimmed = value.trim();
		
		if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
			throw new SQLException("Could not parse timestamp set value.");
		}
		
		trimmed = trimmed.substring(1, trimmed.length() - 1);
		
		if (!trimmed.isEmpty()) {
			String[] values = trimmed.split(",");
			
			for (String v : values) {
				OffsetDateTime date = DateTimeFormatHelper.getDateTimeFormat(v.trim());
				dateTimeList.add(date);
			}
		}
		
		validate();
	}

	
	@Override
	public int hashCode() {
		String value = getValue();
		return value != null ? value.hashCode() : 0;
	}
	
	/**
	 * Gets the interval on which the temporal
	 * value is defined ignoring the potential time gaps
	 *
	 * @return a Duration
	 */
	public Duration timeSpan() {
		if (dateTimeList.isEmpty()) {
			return Duration.ZERO;
		}
		
		return Duration.between(dateTimeList.get(0), dateTimeList.get(dateTimeList.size() - 1));
	}

	
	/**
	 * Get the number of timestamps
	 *
	 * @return a number
	 */
	public int numTimestamps() {
		return dateTimeList.size();
	}
	
	/**
	 * Gets the first timestamp
	 *
	 * @return a timestamp
	 */
	public OffsetDateTime startTimestamp() {
		if (dateTimeList.isEmpty()) {
			return null;
		}
		
		return dateTimeList.get(0);
	}
	
	/**
	 * Gets the last timestamp
	 *
	 * @return a timestamp
	 */
	public OffsetDateTime endTimestamp() {
		if (dateTimeList.isEmpty()) {
			return null;
		}
		
		return dateTimeList.get(dateTimeList.size() - 1);
	}
	
	/**
	 * Gets the timestamp located at the index position
	 *
	 * @param n - the index
	 * @return a timestamp
	 * @throws SQLException
	 */
	public OffsetDateTime timestampN(int n) throws SQLException {
		if (n >= dateTimeList.size()) {
			throw new SQLException("There is no value at this index.");
		}
		
		return dateTimeList.get(n);
	}
	
	/**
	 * Get all timestamps
	 *
	 * @return an array with the timestamps
	 */
	public OffsetDateTime[] timestamps() {
		return dateTimeList.toArray(new OffsetDateTime[0]);
	}
	
	/**
	 * Shifts the duration sent
	 *
	 * @param duration - the duration to shift
	 */
	public TimestampSet shift(Duration duration) throws SQLException {
		ArrayList<OffsetDateTime> shifted = new ArrayList<>();
		for (OffsetDateTime dateTime : dateTimeList) {
			shifted.add(dateTime.plus(duration));
		}
		return new TimestampSet(shifted.toArray(new OffsetDateTime[0]));
	}
	
	/**
	 * Verifies that the received fields are valid
	 *
	 * @throws SQLException
	 */
	private void validate() throws SQLException {
		if (dateTimeList == null || dateTimeList.isEmpty()) {
			throw new SQLException("Timestamp set must contain at least one element.");
		}
		
		for (int i = 0; i < dateTimeList.size(); i++) {
			OffsetDateTime x = dateTimeList.get(i);
			validateTimestamp(x);
			
			if (i + 1 < dateTimeList.size()) {
				OffsetDateTime y = dateTimeList.get(i + 1);
				validateTimestamp(y);
				
				if (x.isAfter(y) || x.isEqual(y)) {
					throw new SQLException("The timestamps of a timestamp set must be in increasing order.");
				}
			}
		}
	}
	
	/**
	 * Checks if the timestamp is valid
	 *
	 * @param timestamp
	 * @throws SQLException
	 */
	private void validateTimestamp(OffsetDateTime timestamp) throws SQLException {
		if (timestamp == null) {
			throw new SQLException("All timestamps should have a value.");
		}
	}
}
