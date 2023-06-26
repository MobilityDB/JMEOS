package types.time;

import types.core.DataType;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class that represents the MobilityDB type PeriodSet
 */
@TypeName(name = "periodset")
public class PeriodSet extends DataType {
    private final List<Period> periodList;

    /**
     * The default constructor
     */
    public PeriodSet() {
        super();
        periodList = new ArrayList<>();
    }

    /**
     * The string constructor
     * @param value - a string with a PeriodSet value
     * @throws SQLException
     */
    public PeriodSet(String value) throws SQLException {
        this();
        setValue(value);
    }

    /**
     * The array of Periods constructor
     * @param periods - an array of Periods or Periods separated by a comma
     * @throws SQLException
     */
    public PeriodSet(Period... periods) throws SQLException {
        this();
        Collections.addAll(periodList, periods);
        validate();
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
     * @return an array of Periods
     */
    public Period[] periods() {
        return periodList.toArray(new Period[0]);
    }

    /**
     * Gets the interval on which the temporal value is defined
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
     * @return a number
     */
    public int numTimestamps() {
        return timestamps().length;
    }

    /**
     * Gets the first timestamp
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
     * @param n - the index
     * @return a timestamp
     */
    public OffsetDateTime timestampN(int n) {
        return timestamps()[n];
    }

    /**
     * Gets the number of periods
     * @return a number
     */
    public int numPeriods() {
        return periodList.size();
    }

    /**
     * Get the first Period
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
     * @param n - the index
     * @return a Period
     */
    public Period periodN(int n) {
        return periodList.get(n);
    }

    /**
     * Shifts the duration sent
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
     * @param period - a Period
     * @return true if the Period is invalid; otherwise false
     */
    private boolean periodIsInvalid(Period period) {
        return period == null || period.getValue() == null;
    }
}
