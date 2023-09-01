package types.time;

import functions.functions;
import jnr.ffi.Pointer;
import types.TemporalObject;
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
public class Period extends Time {
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
	
	public PeriodSet to_periodset() throws SQLException {
		return new PeriodSet(functions.span_to_spanset(this._inner));
	}




	public boolean isAdjacent(TemporalObject<?> other) throws SQLException {
		boolean returnValue;
		switch (other) {
			case Period p -> returnValue = functions.adjacent_span_span(this._inner, p.get_inner());
			case PeriodSet ps -> returnValue = functions.adjacent_spanset_span(ps.get_inner(), this._inner);
			//case DateTime dt -> returnValue = functions.adjacent_periodset_timestamp(this._inner, ConversionUtils.datetimeToTimestampTz(dt.get_inner()));
			case TimestampSet ts -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.set_span(ts.get_inner()));
			// case Temporal t -> returnValue = functions.adjacent_spanset_spanset(this._inner, functions.temporal_time(t.period().get_inner()));
			//case Temporal t -> returnValue = functions.adjacent_spanset_span(this._inner, t.period().get_inner());
			//case Box b -> returnValue = functions.adjacent_spanset_span(this._inner, b.to_period()._inner);
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}

	public boolean is_contained_in(TemporalObject<?> other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.contained_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.contained_span_spanset(this._inner,ps.get_inner());
			//case Temporal t -> returnValue = functions.contained_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.contained_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}




	public boolean contains(TemporalObject<?> other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.contains_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.contains_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.contains_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.contains_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.contains_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	public boolean overlaps(TemporalObject<?> other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.overlaps_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.overlaps_spanset_span(ps.get_inner(),this._inner);
			case TimestampSet ts -> returnValue = functions.overlaps_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.overlaps_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.overlaps_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public boolean is_same(TemporalObject<?> other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.span_eq(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.span_eq(this._inner,functions.spanset_span(ps.get_inner()));
			case TimestampSet ts -> returnValue = functions.span_eq(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.span_eq(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.span_eq(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}





	public boolean is_before(TemporalObject<?> other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.left_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.left_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.left_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.left_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.left_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public boolean is_over_or_before(TemporalObject<?> other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.overleft_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.overleft_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overleft_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.overleft_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.overleft_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	public boolean is_after(TemporalObject<?> other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.right_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.right_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.right_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.right_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.right_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}



	public boolean is_over_or_after(TemporalObject<?> other) throws SQLException{
		boolean returnValue;
		switch (other){
			case Period p -> returnValue = functions.overright_span_span(this._inner,p.get_inner());
			case PeriodSet ps -> returnValue = functions.overright_span_spanset(this._inner,ps.get_inner());
			case TimestampSet ts -> returnValue = functions.overright_span_span(this._inner,functions.set_span(ts.get_inner()));
			//case Temporal t -> returnValue = functions.overright_span_span(this._inner,functions.temporal_to_period(t.get_inner()));
			//case TBox b -> returnValue = functions.overright_span_span(this._inner,b.to_period().get_inner());
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	//TODO: use Duration class from java.time instead

	public double distance(TemporalObject<?> other) throws SQLException{
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


	//TODO: Intersection but define Time first with extends



	public PeriodSet minus(TemporalObject<?> other) throws SQLException{
		PeriodSet returnValue;
		switch (other){
			case Period p -> returnValue = new PeriodSet(functions.minus_span_span(this._inner,p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.minus_span_spanset(this._inner,ps.get_inner()));
			case TimestampSet ts -> returnValue = new PeriodSet(functions.minus_span_spanset(this._inner,functions.set_to_spanset(ts.get_inner())));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	public PeriodSet union(TemporalObject<?> other) throws SQLException{
		PeriodSet returnValue;
		switch (other){
			case Period p -> returnValue = new PeriodSet(functions.union_span_span(this._inner,p.get_inner()));
			case PeriodSet ps -> returnValue = new PeriodSet(functions.union_spanset_span(ps.get_inner(),this._inner));
			case TimestampSet ts -> returnValue = new PeriodSet(functions.union_spanset_span(functions.set_to_spanset(ts.get_inner()),this._inner));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}


	public String to_string(){
		return functions.span_out(this._inner,10);
	}

	public PeriodSet add(TemporalObject<?> other) throws SQLException {
		return this.union(other);
	}

	public boolean equals(TemporalObject<?> other) throws SQLException{
		boolean result;
		result = other instanceof Period ? functions.span_eq(this._inner,((Period) other).get_inner()) : false;
		return result;
	}

	public boolean notEquals(TemporalObject<?> other) throws SQLException{
		boolean result;
		result = other instanceof Period ? functions.span_ne(this._inner,((Period) other).get_inner()) : true;
		return result;
	}

	public boolean lessThan(TemporalObject<?> other) throws SQLException{
		if (other instanceof Period){
			return functions.span_lt(this._inner,((Period) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	public boolean lessThanOrEqual(TemporalObject<?> other) throws SQLException{
		if (other instanceof Period){
			return functions.span_le(this._inner,((Period) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	public boolean greaterThan(TemporalObject<?> other) throws SQLException{
		if (other instanceof Period){
			return functions.span_gt(this._inner,((Period) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}


	public boolean greaterThanOrEqual(TemporalObject<?> other) throws SQLException{
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
	

	
	@Override
	public int hashCode() {
		String value = getValue();
		return value != null ? value.hashCode() : 0;
	}


	/**
	 * Gets the interval on which the temporal value is defined
	 * @return a duration
	 */
	public Duration duration(){
		return Duration.between(lower,upper);
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
