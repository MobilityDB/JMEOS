package types.temporal;

import java.io.Serializable;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import jnr.ffi.Pointer;

/**
 * Base class for temporal instant
 *
 * @param <V>
 */
public abstract class TInstant<V extends Serializable> extends Temporal<V> {
	private TemporalValue<V> temporalValue = null;
	private Pointer _inner = null;


	public TInstant(){
		super();
	}


	public TInstant(Pointer inner){
		super(inner);
		this._inner = createInner(inner);
	}

	public TInstant(String str){
		super(str);
		this._inner = createStringInner(str);
	}

	public abstract Pointer createStringInner(String str);
	public abstract Pointer createInner(Pointer inner);

	

	public String buildValue() {
		return temporalValue.toString();
	}
	
	/**
	 * Gets the temporal value
	 *
	 * @return the temporal value wrapper
	 */
	public TemporalValue<V> getTemporalValue() {
		return temporalValue;
	}
	
	/**
	 * Gets the value
	 *
	 * @return the value
	 */
	public V getValue() {
		return temporalValue.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<V> getValues() {
		List<V> values = new ArrayList<>();
		values.add(temporalValue.getValue());
		return values;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public V startValue() {
		return temporalValue.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public V endValue() {
		return temporalValue.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public V minValue() {
		return temporalValue.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public V maxValue() {
		return temporalValue.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public V valueAtTimestamp(OffsetDateTime timestamp) {
		if (timestamp.isEqual(temporalValue.getTime())) {
			return temporalValue.getValue();
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public int numTimestamps() {
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public OffsetDateTime[] timestamps() {
		return new OffsetDateTime[]{temporalValue.getTime()};
	}
	
	/**
	 * {@inheritDoc}
	 */
	public OffsetDateTime startTimestamp() {
		return temporalValue.getTime();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public OffsetDateTime endTimestamp() {
		return temporalValue.getTime();
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	public int numInstants() {
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TInstant<V> startInstant() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TInstant<V> endInstant() {
		return this;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public List<TInstant<V>> instants() {
		ArrayList<TInstant<V>> list = new ArrayList<>();
		list.add(this);
		return list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Duration duration() {
		return Duration.ZERO;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void shift(Duration duration) {
		temporalValue.setTime(temporalValue.getTime().plus(duration));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean intersectsTimestamp(OffsetDateTime dateTime) {
		return temporalValue.getTime().isEqual(dateTime);
	}
	
	/*
	  {@inheritDoc}
	 */
	/*
	public boolean intersectsPeriod(Period period) {
		if (period == null) {
			return false;
		}
		
		return period.contains(temporalValue.getTime());
	}

	 */
	
	/**
	 * Gets the timestamp
	 *
	 * @return a timestamp
	 */
	public OffsetDateTime getTimestamp() {
		return temporalValue.getTime();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() == obj.getClass()) {
			TInstant<?> otherTemporal = (TInstant<?>) obj;
			return this.temporalValue.equals(otherTemporal.temporalValue);
		}
		return false;
	}

	public int hashCode() {
		String value = toString();
		return value != null ? value.hashCode() : 0;
	}
	
}
