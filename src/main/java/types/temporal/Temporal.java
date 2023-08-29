package types.temporal;

import net.postgis.jdbc.geometry.Point;
import types.time.Period;
import types.time.PeriodSet;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import functions.functions;


/**
 * Abstract class for Temporal sub types
 *
 * @param <V> - Base type of the temporal data type eg. Integer, Boolean
 */
public class Temporal<V extends Serializable> implements Serializable {
	protected TemporalType temporalType;
	private Class<V> mainType;
	protected String customType = this.getType(this.mainType);


	protected Temporal(TemporalType temporalType) {
		this.temporalType = temporalType;
	}
	
	protected void validate() throws SQLException {
		validateTemporalDataType();
	}
	public String get_customType(){
		return this.customType;
	}

	public Temporal from_hexwkb(String hexwkb){

		return Factory.create_temporal(functions.temporal_from_hexwkb(hexwkb), this.customType, this.temporalType);
	}




	public String getType(Class<V> value){
		String type = null;
		if (value == Integer.class){
			type = "Integer";
		} else if (value == Float.class) {
			type = "Float";
		} else if (value == Boolean.class) {
			type = "Boolean";
		} else if (value == String.class) {
			type = "String";
		} else if (value == Point.class) {
			type = "Point";
		}
		return type;
	}



	
	/**
	 * Builds the temporal value. It can be overridden in child classes to change the behavior.
	 *
	 * @param value data type value
	 * @param time  OffsetDateTime
	 * @return temporal value wrapper
	 */
	protected TemporalValue<V> buildTemporalValue(V value, OffsetDateTime time) {
		return new TemporalValue<>(value, time);
	}
	
	/**
	 * It will be called before parsing the value, so the child classes can preprocess the value.
	 *
	 * @param value - a string with the value
	 * @return a string
	 * @throws SQLException when the value is not valid, e.g when the value is not null or empty.
	 */
	protected String preprocessValue(String value) throws SQLException {
		if (value == null || value.isEmpty()) {
			throw new SQLException("Value cannot be empty.");
		}
		
		return value;
	}
	
	/**
	 * It will be called on validate and it should throw SQLException for any validation errors.
	 *
	 * @throws SQLException when the temporal data type is invalid
	 */
	protected void validateTemporalDataType()throws SQLException {
		;
	}


	/**
	 * Parses the object to string representation in the form required by org.postgresql.
	 *
	 * @return the value in string representation of this temporal sub type
	 */

	public String buildValue(){
		return "";
	}


	
	/**
	 * Gets all values
	 *
	 * @return a list of values
	 */
	/*
	public List<V> getValues();
	 */
	/**
	 * Gets the first value
	 *
	 * @return the first value
	 */
	/*
	public V startValue();


	 */
	/**
	 * Gets the last value
	 *
	 * @return the last value
	 */
	/*
	public V endValue();


	 */
	/**
	 * Gets the minimum value
	 *
	 * @return min value
	 */
	/*
	public V minValue();


	 */
	/**
	 * Gets the maximum value
	 *
	 * @return max value
	 */
	/*
	public V maxValue();


	 */
	/**
	 * Gets the value in the given timestamp
	 *
	 * @param timestamp - the timestamp
	 * @return value at the timestamp or null
	 */
	/*
	public V valueAtTimestamp(OffsetDateTime timestamp);


	 */
	/**
	 * Get the number of timestamps
	 *
	 * @return a number
	 */
	/*
	public int numTimestamps();


	 */
	/**
	 * Get all timestamps
	 *
	 * @return an array with the timestamps
	 */
	/*
	public OffsetDateTime[] timestamps();


	 */
	/**
	 * Gets the timestamp located at the index position
	 *
	 * @param n - the index
	 * @return a timestamp
	 * @throws SQLException if the index is out of range
	 */
	/*
	public OffsetDateTime timestampN(int n) throws SQLException;


	 */
	/**
	 * Gets the first timestamp
	 *
	 * @return a timestamp
	 */
	/*
	public OffsetDateTime startTimestamp();


	 */
	/**
	 * Gets the last timestamp
	 *
	 * @return a timestamp
	 */
	/*
	public OffsetDateTime endTimestamp();


	 */
	/**
	 * Gets the periodset on which the temporal value is defined
	 *
	 * @return a Periodset
	 * @throws SQLException
	 */
	/*
	public PeriodSet getTime() throws SQLException;


	 */
	/**
	 * Gets the period
	 *
	 * @return a Period
	 * @throws SQLException
	 */
	/*
	public Period period() throws SQLException;


	 */
	/**
	 * Gets the number of instants
	 *
	 * @return a number
	 */
	/*
	public int numInstants();


	 */
	/**
	 * Gets the first instant
	 *
	 * @return first temporal instant
	 */
	/*
	public TInstant<V> startInstant();


	 */
	/**
	 * Gets the last instant
	 *
	 * @return last temporal instant
	 */
	/*
	public TInstant<V> endInstant();


	 */
	/**
	 * Gets the instant in the given index
	 *
	 * @param n - the index
	 * @return the temporal instant at n
	 * @throws SQLException if the index is out of range
	 */
	/*
	public TInstant<V> instantN(int n) throws SQLException;


	 */
	/**
	 * Gets all temporal instants
	 *
	 * @return the list of all temporal instants
	 */
	/*
	public List<TInstant<V>> instants();


	 */
	/**
	 * Gets the interval on which the temporal value is defined
	 *
	 * @return a duration
	 */
	/*
	public Duration duration();


	 */
	/**
	 * Gets the interval on which the temporal value is defined ignoring the potential time gaps
	 *
	 * @return a duration
	 */
	/*
	public Duration timespan();


	 */
	/**
	 * Shifts the duration sent
	 *
	 * @param duration - the duration to shift
	 */
	/*
	public void shift(Duration duration);


	 */
	/**
	 * If the temporal value intersects the timestamp sent
	 *
	 * @param dateTime - the timestamp
	 * @return true if the timestamp intersects, otherwise false
	 */
	/*
	public boolean intersectsTimestamp(OffsetDateTime dateTime);


	 */
	/**
	 * If the temporal value intersects the Period sent
	 *
	 * @param period - the period
	 * @return true if the period intersects, otherwise false
	 */
	/*
	public boolean intersectsPeriod(Period period);


	 */
	/**
	 * Gets the temporal type
	 *
	 * @return a TemporalType
	 */

	public TemporalType getTemporalType() {
		return temporalType;
	}
	/*
	@Override
	public String toString() {
		return buildValue();
	}

	 */
}
