package types.temporal;

import types.time.Period;
import types.time.PeriodSet;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Abstract class for Temporal sub types
 * @param <V> - Base type of the temporal data type eg. Integer, Boolean
 */
public abstract class Temporal<V extends Serializable> implements Serializable {
    protected TemporalType temporalType;

    protected Temporal(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    protected void validate() throws SQLException {
        validateTemporalDataType();
    }

    /**
     * Builds the temporal value. It can be overridden in child classes to change the behavior.
     * @param value data type value
     * @param time OffsetDateTime
     * @return temporal value wrapper
     */
    protected TemporalValue<V> buildTemporalValue(V value, OffsetDateTime time) {
        return new TemporalValue<>(value, time);
    }

    /**
     * It will be called before parsing the value, so the child classes can preprocess the value.
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
     * @throws SQLException when the temporal data type is invalid
     */
    protected abstract void validateTemporalDataType() throws SQLException;

    /**
     * Parses the object to string representation in the form required by org.postgresql.
     * @return the value in string representation of this temporal sub type
     */
    public abstract String buildValue();

    /**
     * Gets all values
     * @return a list of values
     */
    public abstract List<V> getValues();

    /**
     * Gets the first value
     * @return the first value
     */
    public abstract V startValue();

    /**
     * Gets the last value
     * @return the last value
     */
    public abstract V endValue();

    /**
     * Gets the minimum value
     * @return min value
     */
    public abstract V minValue();

    /**
     * Gets the maximum value
     * @return max value
     */
    public abstract V maxValue();

    /**
     * Gets the value in the given timestamp
     * @param timestamp - the timestamp
     * @return value at the timestamp or null
     */
    public abstract V valueAtTimestamp(OffsetDateTime timestamp);

    /**
     * Get the number of timestamps
     * @return a number
     */
    public abstract int numTimestamps();

    /**
     * Get all timestamps
     * @return an array with the timestamps
     */
    public abstract OffsetDateTime[] timestamps();

    /**
     * Gets the timestamp located at the index position
     * @param n - the index
     * @return a timestamp
     * @throws SQLException if the index is out of range
     */
    public abstract OffsetDateTime timestampN(int n) throws SQLException;

    /**
     * Gets the first timestamp
     * @return a timestamp
     */
    public abstract OffsetDateTime startTimestamp();

    /**
     * Gets the last timestamp
     * @return a timestamp
     */
    public abstract OffsetDateTime endTimestamp();

    /**
     * Gets the periodset on which the temporal value is defined
     * @return a Periodset
     * @throws SQLException
     */
    public abstract PeriodSet getTime() throws SQLException;

    /**
     * Gets the period
     * @return a Period
     * @throws SQLException
     */
    public abstract Period period() throws SQLException;

    /**
     * Gets the number of instants
     * @return a number
     */
    public abstract int numInstants();

    /**
     * Gets the first instant
     * @return first temporal instant
     */
    public abstract TInstant<V> startInstant();

    /**
     * Gets the last instant
     * @return last temporal instant
     */
    public abstract TInstant<V> endInstant();

    /**
     * Gets the instant in the given index
     * @param n - the index
     * @return the temporal instant at n
     * @throws SQLException if the index is out of range
     */
    public abstract TInstant<V> instantN(int n) throws SQLException;

    /**
     * Gets all temporal instants
     * @return the list of all temporal instants
     */
    public abstract List<TInstant<V>> instants();

    /**
     * Gets the interval on which the temporal value is defined
     * @return a duration
     */
    public abstract Duration duration();

    /**
     * Gets the interval on which the temporal value is defined ignoring the potential time gaps
     * @return a duration
     */
    public abstract Duration timespan();

    /**
     * Shifts the duration sent
     * @param duration - the duration to shift
     */
    public abstract void shift(Duration duration);

    /**
     * If the temporal value intersects the timestamp sent
     * @param dateTime - the timestamp
     * @return true if the timestamp intersects, otherwise false
     */
    public abstract boolean intersectsTimestamp(OffsetDateTime dateTime);

    /**
     * If the temporal value intersects the Period sent
     * @param period - the period
     * @return true if the period intersects, otherwise false
     */
    public abstract boolean intersectsPeriod(Period period);

    /**
     * Gets the temporal type
     * @return a TemporalType
     */
    public TemporalType getTemporalType() {
        return temporalType;
    }

    @Override
    public String toString() {
        return buildValue();
    }
}
