package types.temporal;


import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Wrapper class that consists in the value and a timestamp
 *
 * @param <V> Could be Integer, Boolean, Float, String, etc
 */
public class TemporalValue<V extends Serializable> implements Serializable {
	private V value;
	private OffsetDateTime time;
	
	public TemporalValue(V value, OffsetDateTime time) {
		this.value = value;
		this.time = time;
	}
	
	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
	
	public OffsetDateTime getTime() {
		return time;
	}
	
	public void setTime(OffsetDateTime time) {
		this.time = time;
	}
	
	/**
	 * Returns the temporal value in MobilityDB format
	 * eg: 10@2021-04-08 05:04:45+02
	 *
	 * @return String
	 */

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof TemporalValue<?> other) {
            return value.equals(other.value) && time.isEqual(other.time);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		String stringValue = toString();
		return stringValue != null ? stringValue.hashCode() : 0;
	}
}
