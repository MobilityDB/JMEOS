package types.time;

import functions.functions;
import jnr.ffi.Pointer;
import types.TemporalObject;
import types.core.DataType;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * Class for representing sets of contiguous timestamps between a lower and
 * an upper bound. The bounds may be inclusive or not.
 * <p>
 * ``Period`` objects can be created with a single argument of type string
 * as in MobilityDB.
 * <p>
 * >>> Period('(2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01)')
 * <p>
 * Another possibility is to provide the ``lower`` and ``upper`` named parameters (of type str or datetime), and
 * optionally indicate whether the bounds are inclusive or exclusive (by default, the lower bound is inclusive and the
 * upper is exclusive):
 * <p>
 * >>> Period(lower='2019-09-08 00:00:00+01', upper='2019-09-10 00:00:00+01')
 * >>> Period(lower='2019-09-08 00:00:00+01', upper='2019-09-10 00:00:00+01', lower_inc=False, upper_inc=True)
 * >>> Period(lower=parse('2019-09-08 00:00:00+01'), upper=parse('2019-09-10 00:00:00+01'), upper_inc=True)
 * <p>
 * TODO: Add datetime in constructor, Modify the SQLException, Modify the timestampTZ
 */
@TypeName(name = "period")
public class Period extends TemporalObject<Pointer> {
	private static final String LOWER_INCLUSIVE = "[";
	private static final String LOWER_EXCLUSIVE = "(";
	private static final String UPPER_INCLUSIVE = "]";
	private static final String UPPER_EXCLUSIVE = ")";
	private OffsetDateTime lower;
	private OffsetDateTime upper;
	private boolean lowerInclusive = true;
	private boolean upperInclusive = false;
	
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
		super();
		this._inner = _inner;
		String str = functions.period_out(this._inner);
		setValue(str);
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - a string with a Period value
	 * @throws SQLException sql exception
	 */
	public Period(final String value) throws SQLException {
		super();
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
		long lower_ts = functions.pg_timestamptz_in(this.lower.toString(), -1);
		long upper_ts = functions.pg_timestamptz_in(this.upper.toString(), -1);
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
		long lower_ts = functions.pg_timestamptz_in(this.lower.toString(), -1);
		long upper_ts = functions.pg_timestamptz_in(this.upper.toString(), -1);
		this._inner = functions.period_make(lower_ts, upper_ts, this.lowerInclusive, this.upperInclusive);
		validate();
	}
	
	public static Period from_hexwkb(String str) throws SQLException {
		Pointer result = functions.span_from_hexwkb(str);
		return new Period(result);
	}
	
	public float width() {
		return (float) functions.span_width(this._inner);
	}
	
	public Period expand(Period other) throws SQLException {
		Pointer copy = functions.span_copy(this._inner);
		functions.span_expand(other._inner, copy);
		return new Period(copy);
	}
	
	public PeriodSet to_periodset() {
		return new PeriodSet(functions.span_to_spanset(this._inner));
	}
	
	public boolean is_adjacent_Period(Period other) {
		return functions.adjacent_span_span(this._inner, other._inner);
	}
	
	public boolean is_adjacent_Periodset(PeriodSet other) {
		return functions.adjacent_spanset_spanset(this._inner, other.get_inner());
	}
	
    public boolean is_adjacent_datetime(OffsetDateTime other){
        return functions.adjacent_period_timestamp(this._inner, functions.dat(other));
    }

    /*
    public boolean is_adjacent_timestampset(TimestampSet other){
        return adjacent_period_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean is_adjacent_temporal(Temporal other){
        return adjacent_period_temporal(this._inner,other._inner);
    }

     */
	
	public boolean is_contained_in_Period(Period other) {
		return functions.contained_span_span(this._inner, other._inner);
	}
	
	public boolean is_contained_in_Periodset(PeriodSet other) {
		return functions.contained_span_spanset(this._inner, other.get_inner());
	}

    /*
    public boolean is_contained_in_temporal(Temporal other){
        return contained_period_temporal(this._inner,other._inner);
    }
     */
	
	public boolean contains_Period(Period other) {
		
		return functions.contains_span_span(this._inner, other._inner);
	}
	
	public boolean contains_Periodset(PeriodSet other) {
		return functions.contains_span_spanset(this._inner, other.get_inner());
	}


    /*
    public boolean contains_datetime(OffsetDateTime other){
        return contains_period_timestamp(this._inner,other);
    }

     */

    /*
    public boolean contains_timestampset(TimestampSet other){
        return contains_period_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean contains_temporal(Temporal other){
        return contains_period_temporal(this._inner,other._inner);
    }

     */
	
	
	public boolean overlaps_Period(Period other) {
		
		return functions.overlaps_span_span(this._inner, other._inner);
	}
	
	//	public boolean overlaps_Periodset(PeriodSet other) {
	//		return functions.overlaps_span_spanset(this._inner, other.get_inner());
	//	}
	
	
	//	public boolean overlaps_timestampset(TimestampSet other) {
	//		return functions.overlaps_period_timestampset(this._inner, other.get_inner());
	//	}


    /*
    public boolean overlaps_temporal(Temporal other){
        return overlaps_period_temporal(this._inner,other._inner);
    }
     */
	
	public boolean isafter_Period(Period other) {
		
		return functions.right_span_span(this._inner, other._inner);
	}
	
	public boolean isafter_Periodset(PeriodSet other) {
		return functions.after(this._inner, other.get_inner());
	}


    /*
    public boolean isafter_datetime(OffsetDateTime other){
        return after_period_timestamp(this._inner,other);
    }

     */
	
	
	public boolean isafter_timestampset(TimestampSet other) {
		return functions.after_period_timestampset(this._inner, other.get_inner());
	}


    /*
    public boolean isafter_temporal(Temporal other){
        return after_period_temporal(this._inner,other._inner);
    }

     */
	
	
	public boolean isbefore_Period(Period other) {
		
		return functions.left_span_span(this._inner, other._inner);
	}
    /*
    public boolean isbefore_Periodset(PeriodSet other){
        return before_period_periodset(this._inner,other._inner);
    }
     */

    /*
    public boolean isbefore_datetime(OffsetDateTime other){
        return before_period_timestamp(this._inner,other);
    }

     */

    /*
    public boolean isbefore_timestampset(TimestampSet other){
        return before_period_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean isbefore_temporal(Temporal other){
        return before_period_temporal(this._inner,other._inner);
    }

     */
	
	
	public boolean isover_or_after_Period(Period other) {
		
		return functions.overright_span_span(this._inner, other._inner);
	}
    /*
    public boolean isover_or_after_Periodset(PeriodSet other){
        return overafter_period_periodset(this._inner,other._inner);
    }
     */

    /*
    public boolean isover_or_after_datetime(OffsetDateTime other){
        return overafter_period_timestamp(this._inner,other);
    }

     */

    /*
    public boolean isover_or_after_timestampset(TimestampSet other){
        return overafter_period_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean isover_or_after_temporal(Temporal other){
        return overafter_period_temporal(this._inner,other._inner);
    }

     */
	
	
	public boolean isover_or_before_Period(Period other) {
		
		return functions.overleft_span_span(this._inner, other._inner);
	}
    /*
    public boolean isover_or_before_Periodset(PeriodSet other){
        return overbefore_period_periodset(this._inner,other._inner);
    }
     */

    /*
    public boolean isover_or_before_datetime(OffsetDateTime other){
        return overbefore_period_timestamp(this._inner,other);
    }

     */

    /*
    public boolean isover_or_before_timestampset(TimestampSet other){
        return overbefore_period_timestampset(this._inner, other._inner);
    }

     */
    /*
    public boolean isover_or_before_temporal(Temporal other){
        return overbefore_period_temporal(this._inner,other._inner);
    }

     */

    /*
    public boolean is_same(Temporal other){
        return same_period_temporal(this._inner,other._inner);
    }

     */
	
	
	public float distance_Period(Period other) {
		
		return (float) functions.distance_span_span(this._inner, other._inner);
	}
    /*
    public float distance_Periodset(PeriodSet other){
        return (float)distance_period_periodset(this._inner,other._inner);
    }
     */

    /*
    public float distance_datetime(OffsetDateTime other){
        return (float)distance_period_timestamp(this._inner,other);
    }

     */

    /*
    public float distance_timestampset(TimestampSet other){
        return (float)distance_period_timestampset(this._inner, other._inner);
    }
     */
	
	
	public Period intersection_Period(Period other) throws SQLException {
		
		return new Period(functions.intersection_span_span(this._inner, other._inner));
	}
    /*
    public PeriodSet intersection_Periodset(PeriodSet other){
        return new PeriodSet(intersection_period_periodset(this._inner,other._inner));
    }

     */


    /*
    public OffsetDateTime intersection_datetime(OffsetDateTime other){
        return timestamptz_to_datetime(intersection_period_timestamp(this._inner,datetime_to_timestamptz(other)));
    }

     */

    /*
    public TimestampSet intersection_timestampset(TimestampSet other){
        return new TimestampSet(intersection_period_timestampset(this._inner,other._inner));
    }
     */


    /*
    public PeriodSet minus_Period(Period other){
        return new PeriodSet(minus_period_period(this._inner,other._inner));
    }

     */

    /*
    public PeriodSet minus_PeriodSet(PeriodSet other){
        return new PeriodSet(minus_period_periodset(this._inner,other._inner));
    }

     */

    /*
    public PeriodSet minus_datetime(OffsetDateTime other){
        return new PeriodSet(minus_period_timestamp(this._inner,other));
    }

     */

    /*
    public PeriodSet minus_timestampset(TimestampSet other){
        return new PeriodSet(minus_period_timestampset(this._inner,other._inner));
    }

     */





    /*
    public PeriodSet union_Period(Period other){
        return new PeriodSet(union_period_period(this._inner,other._inner));
    }

     */

    /*
    public PeriodSet union_PeriodSet(PeriodSet other){
        return new PeriodSet(union_period_periodset(this._inner,other._inner));
    }

     */

    /*
    public PeriodSet union_datetime(OffsetDateTime other){
        return new PeriodSet(union_period_timestamp(this._inner,other));
    }

     */

    /*
    public PeriodSet union_timestampset(TimestampSet other){
        return new PeriodSet(union_period_timestampset(this._inner,other._inner));
    }

     */
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	@Override
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
		
		this.lower = DateTimeFormatHelper.getDateTimeFormat(values[0].substring(1).trim());
		this.upper = DateTimeFormatHelper.getDateTimeFormat(values[1].substring(0, values[1].length() - 1).trim());
		validate();
	}
	
	public OffsetDateTime getLower() {
		return lower;
	}
	
	public OffsetDateTime getUpper() {
		return upper;
	}
	
	public boolean getUpper_inc() {
		return upperInclusive;
	}
	
	public boolean getLower_inc() {
		return lowerInclusive;
	}
	
	public boolean isLowerInclusive() {
		return lowerInclusive;
	}
	
	public boolean isUpperInclusive() {
		return upperInclusive;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Period) {
			Period other = (Period) obj;
			
			if (lowerInclusive != other.isLowerInclusive()) {
				return false;
			}
			
			if (upperInclusive != other.isUpperInclusive()) {
				return false;
			}
			
			boolean lowerAreEqual;
			boolean upperAreEqual;
			
			if (lower != null && other.getLower() != null) {
				lowerAreEqual = lower.isEqual(other.getLower());
			} else {
				lowerAreEqual = lower == other.getLower();
			}
			
			
			if (upper != null && other.getUpper() != null) {
				upperAreEqual = upper.isEqual(other.getUpper());
			} else {
				upperAreEqual = upper == other.getUpper();
			}
			
			return lowerAreEqual && upperAreEqual;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		String value = getValue();
		return value != null ? value.hashCode() : 0;
	}
	
	/**
	 * Gets the interval on which the temporal value is defined
	 *
	 * @return a duration
	 */
	public Duration duration() {
		return Duration.between(lower, upper);
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
	 * Checks if the OffsetDatetime is contained in the Period
	 *
	 * @param dateTime - a datetime
	 * @return true if the received datetime is contained in the current Period; otherwise false
	 */
	public boolean contains(OffsetDateTime dateTime) {
		return (lower.isBefore(dateTime) && upper.isAfter(dateTime)) ||
				(lowerInclusive && lower.isEqual(dateTime)) ||
				(upperInclusive && upper.isEqual(dateTime));
	}
	
	/**
	 * Checks if there any timestamp in common
	 *
	 * @param period - a Period
	 * @return true if the received period overlaps the current Period; otherwise false
	 */
	public boolean overlap(Period period) {
		return contains(period.lower) || contains(period.upper);
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
	
	@Override
	public boolean isAdjacent(TemporalObject other) {
		return false;
	}
}
