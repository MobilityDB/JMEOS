package types.temporal;

import jnr.ffi.Pointer;
import types.temporal.delegates.CompareValueFunction;
import types.temporal.delegates.GetTemporalInstantFunction;
import types.time.Period;
import types.time.PeriodSet;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public abstract class TSequence<V extends Serializable> extends TemporalInstants<V> implements TemporalSequences<V> {
	protected boolean stepwise;
	private boolean lowerInclusive;
	private boolean upperInclusive;
	private Pointer _inner;
	
	protected TSequence(String value,
						GetTemporalInstantFunction<V> getTemporalInstantFunction,
						CompareValueFunction<V> compareValueFunction) throws SQLException {
		super(TemporalType.TEMPORAL_SEQUENCE, compareValueFunction);
		parseValue(value, getTemporalInstantFunction);
		validate();
	}
	
	protected TSequence(boolean stepwise,
						String value,
						GetTemporalInstantFunction<V> getTemporalInstantFunction,
						CompareValueFunction<V> compareValueFunction) throws SQLException {
		super(TemporalType.TEMPORAL_SEQUENCE, compareValueFunction);
		parseValue(value, getTemporalInstantFunction);
		this.stepwise = stepwise;
		validate();
	}
	
	protected TSequence(boolean stepwise,
						String[] values,
						GetTemporalInstantFunction<V> getTemporalInstantFunction,
						CompareValueFunction<V> compareValueFunction) throws SQLException {
		this(stepwise, values, true, false, getTemporalInstantFunction, compareValueFunction);
	}
	
	protected TSequence(boolean stepwise, String[] values, boolean lowerInclusive, boolean upperInclusive,
						GetTemporalInstantFunction<V> getTemporalInstantFunction,
						CompareValueFunction<V> compareValueFunction) throws SQLException {
		super(TemporalType.TEMPORAL_SEQUENCE, compareValueFunction);
		for (String val : values) {
			instantList.add(getTemporalInstantFunction.run(val.trim()));
		}
		this.lowerInclusive = lowerInclusive;
		this.upperInclusive = upperInclusive;
		this.stepwise = stepwise;
		validate();
	}
	
	protected TSequence(boolean stepwise, TInstant<V>[] values, CompareValueFunction<V> compareValueFunction)
			throws SQLException {
		this(stepwise, values, true, false, compareValueFunction);
	}
	
	protected TSequence(boolean stepwise, TInstant<V>[] values, boolean lowerInclusive, boolean upperInclusive,
						CompareValueFunction<V> compareValueFunction) throws SQLException {
		super(TemporalType.TEMPORAL_SEQUENCE, compareValueFunction);
		instantList.addAll(Arrays.asList(values));
		this.lowerInclusive = lowerInclusive;
		this.upperInclusive = upperInclusive;
		this.stepwise = stepwise;
		validate();
	}


	protected TSequence(Pointer inner){
		super(TemporalType.TEMPORAL_SEQUENCE);
		this._inner = inner;
	}
	
	/**
	 * Parse the string representation of the value
	 *
	 * @param value                      string representation
	 * @param getTemporalInstantFunction delegate used to create a new temporal instant
	 * @throws SQLException if it is invalid
	 */
	private void parseValue(String value, GetTemporalInstantFunction<V> getTemporalInstantFunction)
			throws SQLException {
		String[] values = preprocessValue(value).split(",");
		
		if (values[0].startsWith(TemporalConstants.STEPWISE)) {
			stepwise = true;
			values[0] = values[0].substring(TemporalConstants.STEPWISE.length());
		}
		
		if (values[0].startsWith(TemporalConstants.LOWER_INCLUSIVE)) {
			this.lowerInclusive = true;
		} else if (values[0].startsWith(TemporalConstants.LOWER_EXCLUSIVE)) {
			this.lowerInclusive = false;
		} else {
			throw new SQLException("Lower bound flag must be either '[' or '('.");
		}
		
		if (values[values.length - 1].endsWith(TemporalConstants.UPPER_INCLUSIVE)) {
			this.upperInclusive = true;
		} else if (values[values.length - 1].endsWith(TemporalConstants.UPPER_EXCLUSIVE)) {
			this.upperInclusive = false;
		} else {
			throw new SQLException("Upper bound flag must be either ']' or ')'.");
		}
		
		for (int i = 0; i < values.length; i++) {
			String val = values[i];
			if (i == 0) {
				val = val.substring(1);
			}
			if (i == values.length - 1) {
				val = val.substring(0, val.length() - 1);
			}
			instantList.add(getTemporalInstantFunction.run(val.trim()));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */

	protected void validateTemporalDataType() throws SQLException {
		validateInstantList("Temporal sequence");
		
		if (instantList.size() == 1 && (!lowerInclusive || !upperInclusive)) {
			throw new SQLException("The lower and upper bounds must be inclusive for an instant temporal sequence.");
		}
		
		if (stepwise && instantList.size() > 1 && !upperInclusive &&
				!instantList.get(instantList.size() - 1).getValue().equals(instantList.get(instantList.size() - 2).getValue())) {
			throw new SQLException("The last two values of a temporal sequence with exclusive upper bound and stepwise interpolation must be equal.");
		}
	}
	
	/**
	 * If the interpolation is explicit or implicit
	 *
	 * @return true if the interpolation is explicit; otherwise false
	 */
	protected boolean explicitInterpolation() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public String buildValue() {
		return buildValue(false);
	}
	
	String buildValue(boolean skipInterpolation) {
		StringJoiner sj = new StringJoiner(", ");
		
		for (TInstant<V> temp : instantList) {
			sj.add(temp.toString());
		}
		
		return String.format("%s%s%s%s",
				!skipInterpolation && stepwise && explicitInterpolation() ? TemporalConstants.STEPWISE : "",
				lowerInclusive ? TemporalConstants.LOWER_INCLUSIVE : TemporalConstants.LOWER_EXCLUSIVE,
				sj.toString(),
				upperInclusive ? TemporalConstants.UPPER_INCLUSIVE : TemporalConstants.UPPER_EXCLUSIVE);
	}
	
	/**
	 * {@inheritDoc}
	 */

	public Period period() throws SQLException {
		return new Period(instantList.get(0).getTimestamp(),
				instantList.get(instantList.size() - 1).getTimestamp(),
				lowerInclusive, upperInclusive);
	}
	
	/**
	 * {@inheritDoc}
	 */

	public PeriodSet getTime() throws SQLException {
		return new PeriodSet(period());
	}
	
	/**
	 * {@inheritDoc}
	 */

	public Duration duration() {
		try {
			return period().duration();
		} catch (SQLException ex) {
			return Duration.ZERO;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */

	public Duration timespan() {
		try {
			return period().duration();
		} catch (SQLException ex) {
			return Duration.ZERO;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */

	public boolean intersectsTimestamp(OffsetDateTime dateTime) {
		try {
			return period().contains(dateTime);
		} catch (SQLException ex) {
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */

	public boolean intersectsPeriod(Period period) {
		try {
			return period().overlap(period);
		} catch (SQLException ex) {
			return false;
		}
	}
	

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() == obj.getClass()) {
			TSequence<?> otherTemporal = (TSequence<?>) obj;
			
			if (stepwise != otherTemporal.stepwise) {
				return false;
			}
			
			if (lowerInclusive != otherTemporal.lowerInclusive) {
				return false;
			}
			
			if (upperInclusive != otherTemporal.upperInclusive) {
				return false;
			}
			
			return super.equals(obj);
		}
		return false;
	}
	

	public int hashCode() {
		String value = toString();
		return value != null ? value.hashCode() : 0;
	}
	
	public boolean isStepwise() {
		return stepwise;
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

	public int numSequences() {
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public TSequence<V> startSequence() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public TSequence<V> endSequence() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public TSequence<V> sequenceN(int n) throws SQLException {
		if (n == 0) {
			return this;
		}
		
		throw new SQLException("There is no sequence at this index.");
	}
	
	/**
	 * {@inheritDoc}
	 */

	public List<TSequence<V>> sequences() {
		ArrayList<TSequence<V>> list = new ArrayList<>();
		list.add(this);
		return list;
	}
}
