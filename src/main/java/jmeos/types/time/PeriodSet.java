package jmeos.types.time;

import jmeos.types.core.DataType;
import jmeos.types.core.TypeName;
import jnr.ffi.Pointer;

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

import static jmeos.functions.functions.*;


/*
TO DO:
Add the last constructor
 */



/**
 * Class that represents the MobilityDB type PeriodSet
 */
@TypeName(name = "periodset")
public class PeriodSet extends DataType {
	private final List<Period> periodList;
	private Pointer _inner = null;
	
	/**
	 * The default constructor
	 */
	public PeriodSet() {
		super();
		periodList = new ArrayList<>();
	}


	public PeriodSet(Pointer _inner){
		this();
		this._inner = _inner;
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
		this._inner = periodset_in(value);
	}
	
	/**
	 * The array of Periods constructor
	 *
	 * @param periods - an array of Periods or Periods separated by a comma
	 * @throws SQLException
	 */
	public PeriodSet(Period... periods) throws SQLException {
		this();
		Collections.addAll(periodList, periods);
		validate();
	}



	public PeriodSet from_hexwkb(String str) throws SQLException {
		Pointer result = periodset_from_hexwkb(str);
		return new PeriodSet(result);
	}



	public boolean is_adjacent_Period(Period other){

		return adjacent_periodset_period(this._inner,other.get_inner());
	}

    public boolean is_adjacent_Periodset(PeriodSet other){
        return adjacent_periodset_periodset(this._inner,other.get_inner());
    }


    /*
    public boolean is_adjacent_datetime(OffsetDateTime other){
        return adjacent_periodset_timestamp(this._inner,other);
    }

     */


    /*
    public boolean is_adjacent_timestampset(TimestampSet other){
        return adjacent_periodset_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean is_adjacent_temporal(Temporal other){
        return adjacent_periodset_temporal(this._inner,other._inner);
    }

     */





	public boolean is_contained_Period(Period other){

		return contained_periodset_period(this._inner,other.get_inner());
	}

	public boolean is_contained_Periodset(PeriodSet other){
		return contained_periodset_periodset(this._inner,other.get_inner());
	}

    /*
    public boolean is_contained_temporal(Temporal other){
        return contained_periodset_temporal(this._inner,other._inner);
    }

     */




	public boolean contains_Period(Period other){

		return contains_periodset_period(this._inner,other.get_inner());
	}

    public boolean contains_Periodset(PeriodSet other){
        return contains_periodset_periodset(this._inner,other.get_inner());
    }

    /*
    public boolean contains_datetime(OffsetDateTime other){
        return contains_periodset_timestamp(this._inner,other);
    }

     */

    /*
    public boolean contains_timestampset(TimestampSet other){
        return contains_periodset_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean contains_temporal(Temporal other){
        return contains_periodset_temporal(this._inner,other._inner);
    }

     */




	public boolean overlaps_Period(Period other){

		return overlaps_periodset_period(this._inner,other.get_inner());
	}

    public boolean overlaps_Periodset(PeriodSet other){
        return overlaps_periodset_periodset(this._inner,other.get_inner());
    }


    /*
    public boolean overlaps_timestampset(TimestampSet other){
        return overlaps_periodset_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean overlaps_temporal(Temporal other){
        return overlaps_periodset_temporal(this._inner,other._inner);
    }
     */





	public boolean isafter_Period(Period other){

		return after_periodset_period(this._inner,other.get_inner());
	}

    public boolean isafter_Periodset(PeriodSet other){
        return after_periodset_periodset(this._inner,other.get_inner());
    }


    /*
    public boolean isafter_datetime(OffsetDateTime other){
        return after_periodset_timestamp(this._inner,other);
    }

     */

    /*
    public boolean isafter_timestampset(TimestampSet other){
        return after_periodset_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean isafter_temporal(Temporal other){
        return after_periodset_temporal(this._inner,other._inner);
    }

     */






	public boolean isbefore_Period(Period other){

		return before_periodset_period(this._inner,other.get_inner());
	}

    public boolean isbefore_Periodset(PeriodSet other){
        return before_periodset_periodset(this._inner,other.get_inner());
    }


    /*
    public boolean isbefore_datetime(OffsetDateTime other){
        return before_periodset_timestamp(this._inner,other);
    }

     */

    /*
    public boolean isbefore_timestampset(TimestampSet other){
        return before_periodset_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean isbefore_temporal(Temporal other){
        return before_periodset_temporal(this._inner,other._inner);
    }

     */






	public boolean isover_or_after_Period(Period other){

		return overafter_periodset_period(this._inner,other.get_inner());
	}
    /*
    public boolean isover_or_after_Periodset(PeriodSet other){
        return overafter_periodset_periodset(this._inner,other.get_inner());
    }
     */

    /*
    public boolean isover_or_after_datetime(OffsetDateTime other){
        return overafter_periodset_timestamp(this._inner,other);
    }

     */

    /*
    public boolean isover_or_after_timestampset(TimestampSet other){
        return overafter_periodset_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean isover_or_after_temporal(Temporal other){
        return overafter_periodset_temporal(this._inner,other._inner);
    }

     */





	public boolean isover_or_before_Period(Period other){

		return overbefore_periodset_period(this._inner,other.get_inner());
	}

    public boolean isover_or_before_Periodset(PeriodSet other){
        return overbefore_periodset_periodset(this._inner,other.get_inner());
    }


    /*
    public boolean isover_or_before_datetime(OffsetDateTime other){
        return overbefore_periodset_timestamp(this._inner,other);
    }

     */

    /*
    public boolean isover_or_before_timestampset(TimestampSet other){
        return overbefore_periodset_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean isover_or_before_temporal(Temporal other){
        return overbefore_periodset_temporal(this._inner,other._inner);
    }

     */

    /*
    public boolean is_same(Temporal other){
        return same_periodset_temporal(this._inner,other._inner);
    }

     */





	public float distance_Period(Period other){

		return (float)distance_periodset_period(this._inner,other.get_inner());
	}

    public float distance_Periodset(PeriodSet other){
        return (float)distance_periodset_periodset(this._inner,other.get_inner());
    }


    /*
    public float distance_datetime(OffsetDateTime other){
        return (float)distance_periodset_timestamp(this._inner,other);
    }

     */

    /*
    public float distance_timestampset(TimestampSet other){
        return (float)distance_periodset_timestampset(this._inner, other._inner);
    }
     */





	public PeriodSet intersection_Period(Period other) throws SQLException {

		return new PeriodSet(intersection_periodset_period(this._inner, other.get_inner()));
	}

    public PeriodSet intersection_Periodset(PeriodSet other){
        return new PeriodSet(intersection_periodset_periodset(this._inner,other._inner));
    }




    /*
    public OffsetDateTime intersection_datetime(OffsetDateTime other){
        return timestamptz_to_datetime(intersection_periodset_timestamp(this._inner,datetime_to_timestamptz(other)));
    }

     */

    /*
    public TimestampSet intersection_timestampset(TimestampSet other){
        return new TimestampSet(intersection_periodset_timestampset(this._inner,other._inner));
    }
     */





    public PeriodSet minus_Period(Period other){
        return new PeriodSet(minus_periodset_period(this._inner,other.get_inner()));
    }

    public PeriodSet minus_PeriodSet(PeriodSet other){
        return new PeriodSet(minus_periodset_periodset(this._inner,other.get_inner()));
    }
    /*
    public PeriodSet minus_datetime(OffsetDateTime other){
        return new PeriodSet(minus_periodset_timestamp(this._inner,other));
    }
     */
    /*
    public PeriodSet minus_timestampset(TimestampSet other){
        return new PeriodSet(minus_period_timestampset(this._inner,other));
    }
     */




    public PeriodSet union_Period(Period other){
        return new PeriodSet(union_periodset_period(this._inner,other.get_inner()));
    }

    public PeriodSet union_PeriodSet(PeriodSet other){
        return new PeriodSet(union_periodset_periodset(this._inner,other.get_inner()));
    }


    /*
    public PeriodSet union_datetime(OffsetDateTime other){
        return new PeriodSet(union_periodset_timestamp(this._inner,other));
    }
     */

    /*
    public PeriodSet union_timestampset(TimestampSet other){
        return new PeriodSet(union_periodset_timestampset(this._inner,other._inner));
    }
     */







	public Pointer get_inner(){
		return this._inner;
	}

	
	@Override
	public String getValue() {
		return String.format("{%s}", periodList.stream()
				.map(Period::toString)
				.collect(Collectors.joining(", ")));
	}
	
	@Override
	public void setValue(String value) throws SQLException {
		String trimmed = value.trim();
		
		if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
			Matcher m = Pattern.compile("[\\[|\\(].*?[^\\]\\)][\\]|\\)]")
					.matcher(trimmed);
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
		if (obj instanceof PeriodSet) {
			PeriodSet fobj = (PeriodSet) obj;
			
			return periodList.size() == fobj.periodList.size() &&
					periodList.equals(fobj.periodList);
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
				
				if (x.getUpper().isAfter(y.getLower()) ||
						(x.getUpper().isEqual(y.getLower()) && x.isUpperInclusive() && y.isLowerInclusive())) {
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
