package types.time;

import types.core.DataType;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that represents the MobilityDB type TimestampSet
 */
@TypeName(name = "timestampset")
public class TimestampSet extends DataType {
    private final List<OffsetDateTime> dateTimeList;

    /**
     * The default constructor
     */
    public TimestampSet() {
        super();
        dateTimeList = new ArrayList<>();
    }

    /**
     * The string constructor
     * @param value - a string with a TimestampSet value
     * @throws SQLException
     */
    public TimestampSet(String value) throws SQLException {
        this();
        setValue(value);
    }

    /**
     * The array of OffsetDateTime constructor
     * @param dateTimes - an array of OffsetDateTime or OffsetDateTime separated by a comma
     * @throws SQLException
     */
    public TimestampSet(OffsetDateTime... dateTimes) throws SQLException {
        this();
        Collections.addAll(dateTimeList, dateTimes);
        validate();
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
    public boolean equals(Object obj) {
        if (obj instanceof TimestampSet) {
            TimestampSet fobj = (TimestampSet) obj;

            if (dateTimeList.size() == fobj.dateTimeList.size()) {
                for (int i = 0; i < dateTimeList.size(); i++) {
                    if (!dateTimeList.get(i).isEqual(fobj.dateTimeList.get(i))) {
                        return false;
                    }
                }
            } else {
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        String value = getValue();
        return value != null ? value.hashCode() : 0;
    }

    /**
     * Gets the interval on which the temporal
     * value is defined ignoring the potential time gaps
     * @return a Duration
     */
    public Duration timeSpan() {
        if (dateTimeList.isEmpty()) {
            return Duration.ZERO;
        }

        return Duration.between(dateTimeList.get(0), dateTimeList.get(dateTimeList.size() - 1));
    }

    /**
     * Gets the period
     * @return a Period
     * @throws SQLException
     */
    public Period period() throws SQLException {
        if (dateTimeList.isEmpty()) {
            return new Period();
        }

        return new Period(dateTimeList.get(0), dateTimeList.get(dateTimeList.size() - 1), true, true);
    }

    /**
     * Get the number of timestamps
     * @return a number
     */
    public int numTimestamps() {
        return dateTimeList.size();
    }

    /**
     * Gets the first timestamp
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
     * @return an array with the timestamps
     */
    public OffsetDateTime[] timestamps() {
        return dateTimeList.toArray(new OffsetDateTime[0]);
    }

    /**
     * Shifts the duration sent
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
     * @param timestamp
     * @throws SQLException
     */
    private void validateTimestamp(OffsetDateTime timestamp) throws SQLException {
        if (timestamp == null) {
            throw new SQLException("All timestamps should have a value.");
        }
    }
}
