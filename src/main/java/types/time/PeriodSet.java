package types.time;

import functions.functions;
import jnr.ffi.Pointer;
import types.TemporalObject;
import types.core.TypeName;
import types.temporal.Temporal;
import utils.ConversionUtils;

import types.boxes.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/*
TODO: Add the last constructor
 */


/**
 * Class that represents the MobilityDB type PeriodSet
 */
@TypeName(name = "periodset")
public class PeriodSet extends Time {
	private final List<Period> periodList;
	
	/**
	 * The default constructor
	 */
	public PeriodSet() {
		super();
		periodList = new ArrayList<>();
	}
	
	public PeriodSet(Pointer _inner) throws SQLException {
		this();
		this._inner = _inner;
		String str = functions.periodset_out(this._inner);
		setValue(str);
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - a string with a PeriodSet value
	 * @throws SQLException
	 */
	public PeriodSet(String value) throws SQLException {
		this();
		setValue(value);
		this._inner = functions.periodset_in(value);
	}
	
	/**
	 * The array of Periods constructor
	 *
	 * @param periods - an array of Periods or Periods separated by a comma
	 * @throws SQLException
	 * TODO: create a string conversion method to integrate inner ?
	 */
	public PeriodSet(Period... periods) throws SQLException {
		this();
		Collections.addAll(periodList, periods);
		validate();
	}

	
	public static PeriodSet from_hexwkb(String str) throws SQLException {
		Pointer result = functions.spanset_from_hexwkb(str);
		return new PeriodSet(result);
	}

	public Period to_period() throws SQLException {
		return new Period(functions.spanset_span(this._inner));
	}
	
	/**
	 * Returns whether "this" is temporally adjacent to "other".
	 * That is, they share a bound but only one of them contains it.
	 * <pre>
	 * Examples:
	 * >>> PeriodSet('{[2012-01-01, 2012-01-02)}').is_adjacent(PeriodSet('{[2012-01-02, 2012-01-03]}'))
	 * >>> True
	 * >>> PeriodSet('{[2012-01-01, 2012-01-02]}').is_adjacent(PeriodSet('{[2012-01-02, 2012-01-03]}'))
	 * >>> False  # Both contain bound
	 * >>> PeriodSet('{[2012-01-01, 2012-01-02)}').is_adjacent(PeriodSet('{[(2012-01-02, 2012-01-03]]}'))
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
	public boolean isAdjacent(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.adjacent_spanset_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.adjacent_spanset_spanset(this._inner, ps.get_inner());
			//case DateTime dt -> returnValue = functions.adjacent_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			case TimestampSet ts -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.set_to_spanset(ts.get_inner()));
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			//case Box b -> returnValue = functions.adjacent_spanset_span(this._inner, b.to_period()._inner);
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}




	public boolean is_contained_in(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.contained_spanset_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.contained_spanset_spanset(this._inner, ps.get_inner());
			//case Temporal t -> returnValue = functions.contained_spanset_span(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.contained_spanset_span(this._inner, b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}





	public boolean contains(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.contains_spanset_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.contains_spanset_spanset(this._inner, ps.get_inner());
			case TimestampSet ts -> returnValue = functions.contains_spanset_spanset(this._inner, functions.set_to_spanset(ts.get_inner()));
			//case DateTime dt -> returnValue = functions.contains_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			//case Temporal t -> returnValue = functions.contains_spanset_spanset(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.contains_spanset_span(this._inner, b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}





	public boolean overlaps(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overlaps_spanset_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.overlaps_spanset_spanset(this._inner, ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overlaps_spanset_spanset(this._inner, functions.set_span(ts.get_inner()));
			//case DateTime dt -> returnValue = functions.overlaps_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			//case Temporal t -> returnValue = functions.overlaps_spanset_spanset(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.overlaps_spanset_span(this._inner, b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public boolean is_same(TemporalObject<?> other) throws SQLException {
		return this.to_period().is_same(other);
	}



	public boolean is_before(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.left_spanset_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.left_spanset_spanset(this._inner, ps.get_inner());
			case TimestampSet ts -> returnValue = functions.left_spanset_spanset(this._inner, functions.set_to_spanset(ts.get_inner()));
			//case DateTime dt -> returnValue = functions.left_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			//case Temporal t -> returnValue = functions.left_spanset_spanset(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.left_spanset_span(this._inner, b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public boolean is_over_or_before(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overleft_spanset_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.overleft_spanset_spanset(this._inner, ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overleft_spanset_span(this._inner, functions.set_span(ts.get_inner()));
			//case DateTime dt -> returnValue = functions.overleft_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			//case Temporal t -> returnValue = functions.overleft_spanset_spanset(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.overleft_spanset_span(this._inner, b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}




	public boolean is_after(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.right_spanset_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.right_spanset_spanset(this._inner, ps.get_inner());
			case TimestampSet ts -> returnValue = functions.right_spanset_span(this._inner, functions.set_span(ts.get_inner()));
			//case DateTime dt -> returnValue = functions.right_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			//case Temporal t -> returnValue = functions.right_spanset_spanset(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.right_spanset_span(this._inner, b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public boolean is_over_or_after(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.overright_spanset_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.overright_spanset_spanset(this._inner, ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overright_spanset_span(this._inner, functions.set_span(ts.get_inner()));
			//case DateTime dt -> returnValue = functions.overleft_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			//case Temporal t -> returnValue = functions.overleft_spanset_spanset(this._inner, t.period().get_inner());
			case Box b -> returnValue = functions.overright_spanset_span(this._inner, b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	//TODO: Distance operator



	public Time intersection(TemporalObject<?> other) throws SQLException {
		Time returnValue;
		switch (other) {
			case Period p -> returnValue = new Period(functions.intersection_spanset_span(this._inner,p.get_inner()));
			case PeriodSet ps -> returnValue = new Period(functions.intersection_spanset_spanset(this._inner,ps.get_inner()));
			case TimestampSet ts -> returnValue = new Period(functions.intersection_spanset_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
			//case DateTime dt -> returnValue = functions.overleft_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public PeriodSet minus(TemporalObject<?> other) throws SQLException {
		PeriodSet returnValue;
		switch (other) {
			case Period p -> returnValue = new PeriodSet(functions.minus_spanset_span(this._inner,p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.minus_spanset_spanset(this._inner,ps.get_inner()));
			case TimestampSet ts -> returnValue = new PeriodSet(functions.minus_spanset_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
			//case DateTime dt -> returnValue = functions.overleft_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public PeriodSet union(TemporalObject<?> other) throws SQLException {
		PeriodSet returnValue;
		switch (other) {
			case Period p -> returnValue = new PeriodSet(functions.union_spanset_span(this._inner,p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.union_spanset_spanset(this._inner,ps.get_inner()));
			case TimestampSet ts -> returnValue = new PeriodSet(functions.union_spanset_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
			//case DateTime dt -> returnValue = functions.overleft_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	
	@Override
	public String getValue() {
		return String.format("{%s}", periodList.stream().map(Period::toString).collect(Collectors.joining(", ")));
	}
	
	@Override
	public void setValue(String value) throws SQLException {
		String trimmed = value.trim();
		
		if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
			Matcher m = Pattern.compile("[\\[|\\(].*?[^\\]\\)][\\]|\\)]").matcher(trimmed);
			while (m.find()) {
				periodList.add(new Period(m.group()));
			}
		} else {
			throw new SQLException("Could not parse period set value.");
		}
		
		validate();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeriodSet fobj) {
			
			return periodList.size() == fobj.periodList.size() && periodList.equals(fobj.periodList);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		String value = getValue();
		return value != null ? value.hashCode() : 0;
	}
	
	/**
	 * Gets all the Periods
	 *
	 * @return an array of Periods
	 */
	public Period[] periods() {
		return periodList.toArray(new Period[0]);
	}
	
	/**
	 * Gets the interval on which the temporal value is defined
	 *
	 * @return a duration
	 */
	public Duration duration() {
		Duration d = Duration.ZERO;
		
		for (Period p : periodList) {
			d = d.plus(p.duration());
		}
		
		return d;
	}
	
	/**
	 * Gets the interval on which the temporal
	 * value is defined ignoring the potential time gaps
	 *
	 * @return a Duration
	 */
	public Duration timespan() {
		if (periodList.isEmpty()) {
			return Duration.ZERO;
		}
		
		return Duration.between(startTimestamp(), endTimestamp());
	}
	
	/**
	 * Gets the period
	 *
	 * @return a Period
	 * @throws SQLException
	 */
	public Period period() throws SQLException {
		if (periodList.isEmpty()) {
			return null;
		}
		
		Period first = periodList.get(0);
		Period last = periodList.get(periodList.size() - 1);
		
		return new Period(first.getLower(), last.getUpper(), first.isLowerInclusive(), last.isUpperInclusive());
	}
	
	/**
	 * Get all timestamps
	 *
	 * @return an array with the timestamps
	 */
	public OffsetDateTime[] timestamps() {
		LinkedHashSet<OffsetDateTime> timestamps = new LinkedHashSet<>();
		
		for (Period period : periodList) {
			timestamps.add(period.getLower());
			timestamps.add(period.getUpper());
		}
		
		return timestamps.toArray(new OffsetDateTime[0]);
	}
	
	/**
	 * Get the number of timestamps
	 *
	 * @return a number
	 */
	public int numTimestamps() {
		return timestamps().length;
	}
	
	/**
	 * Gets the first timestamp
	 *
	 * @return a timestamp
	 */
	public OffsetDateTime startTimestamp() {
		if (periodList.isEmpty()) {
			return null;
		}
		
		return periodList.get(0).getLower();
	}
	
	/**
	 * Gets the last timestamp
	 *
	 * @return a timestamp
	 */
	public OffsetDateTime endTimestamp() {
		if (periodList.isEmpty()) {
			return null;
		}
		
		return periodList.get(periodList.size() - 1).getUpper();
	}
	
	/**
	 * Gets the timestamp located at the index position
	 *
	 * @param n - the index
	 * @return a timestamp
	 */
	public OffsetDateTime timestampN(int n) {
		return timestamps()[n];
	}
	
	/**
	 * Gets the number of periods
	 *
	 * @return a number
	 */
	public int numPeriods() {
		return periodList.size();
	}
	
	/**
	 * Get the first Period
	 *
	 * @return a Period
	 */
	public Period startPeriod() {
		if (periodList.isEmpty()) {
			return null;
		}
		
		return periodList.get(0);
	}
	
	/**
	 * Get the last Period
	 *
	 * @return a Period
	 */
	public Period endPeriod() {
		if (periodList.isEmpty()) {
			return null;
		}
		
		return periodList.get(periodList.size() - 1);
	}
	
	
	/**
	 * Gets the Period located at the index position
	 *
	 * @param n - the index
	 * @return a Period
	 */
	public Period periodN(int n) {
		return periodList.get(n);
	}
	
	/**
	 * Shifts the duration sent
	 *
	 * @param duration - the duration to shift
	 */
	public PeriodSet shift(Duration duration) throws SQLException {
		ArrayList<Period> periods = new ArrayList<>();
		
		for (Period period : periodList) {
			periods.add(period.shift(duration));
		}
		
		return new PeriodSet(periods.toArray(new Period[0]));
	}
	
	/**
	 * Verifies that the received fields are valid
	 *
	 * @throws SQLException
	 */
	private void validate() throws SQLException {
		if (periodList == null || periodList.isEmpty()) {
			throw new SQLException("Period set must contain at least one element.");
		}
		
		for (int i = 0; i < periodList.size(); i++) {
			Period x = periodList.get(i);
			
			if (periodIsInvalid(x)) {
				throw new SQLException("All periods should have a value.");
			}
			
			if (i + 1 < periodList.size()) {
				Period y = periodList.get(i + 1);
				
				if (periodIsInvalid(y)) {
					throw new SQLException("All periods should have a value.");
				}
				
				if (x.getUpper().isAfter(y.getLower()) || (x.getUpper().isEqual(y.getLower()) && x.isUpperInclusive() && y.isLowerInclusive())) {
					throw new SQLException("The periods of a period set cannot overlap.");
				}
			}
		}
	}
	
	/**
	 * Checks if the Period is invalid
	 *
	 * @param period - a Period
	 * @return true if the Period is invalid; otherwise false
	 */
	private boolean periodIsInvalid(Period period) {
		return period == null || period.getValue() == null;
	}
}
