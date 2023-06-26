package types.time;

import types.core.DataType;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * Class that represents the MobilityDB type Period
 */
@TypeName(name = "period")
public class Period extends DataType {
    private OffsetDateTime lower;
    private OffsetDateTime upper;
    private boolean lowerInclusive;
    private boolean upperInclusive;

    private static final String LOWER_INCLUSIVE = "[";
    private static final String LOWER_EXCLUSIVE = "(";
    private static final String UPPER_INCLUSIVE = "]";
    private static final String UPPER_EXCLUSIVE = ")";

    /**
     * The default constructor
     */
    public Period() {
        super();
    }

    /**
     * The string constructor
     * @param value - a string with a Period value
     * @throws SQLException
     */
    public Period(final String value) throws SQLException {
        super();
        setValue(value);
    }

    /**
     * The timestamps constructor
     * @param lower - a timestamp for the lower bound
     * @param upper - a timestamp for the upper bound
     * @throws SQLException
     */
    public Period(OffsetDateTime lower, OffsetDateTime upper) throws SQLException {
        super();
        this.lower = lower;
        this.upper = upper;
        this.lowerInclusive = true;
        this.upperInclusive = false;
        validate();
    }

    /**
     * The timestamps and bounds constructor
     * @param lower - a timestamp for the lower bound
     * @param upper - a timestamp for the upper bound
     * @param lowerInclusive - if the lower bound is inclusive
     * @param upperInclusive - if the upper bound is inclusive
     * @throws SQLException
     */
    public Period(OffsetDateTime lower, OffsetDateTime upper, boolean lowerInclusive, boolean upperInclusive)
            throws SQLException {
        super();
        this.lower = lower;
        this.upper = upper;
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
        validate();
    }

    /** {@inheritDoc} */
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

    public OffsetDateTime getLower() {
        return lower;
    }

    public OffsetDateTime getUpper() {
        return upper;
    }

    public boolean isLowerInclusive() {
        return lowerInclusive;
    }

    public boolean isUpperInclusive() {
        return upperInclusive;
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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
     * @return a duration
     */
    public Duration duration() {
        return Duration.between(lower, upper);
    }

    /**
     * Shifts the duration sent
     * @param duration - the duration to shift
     */
    public Period shift(Duration duration) throws SQLException {
        return new Period(lower.plus(duration), upper.plus(duration), lowerInclusive, upperInclusive);
    }

    /**
     * Checks if the OffsetDatetime is contained in the Period
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
     * @param period - a Period
     * @return true if the received period overlaps the current Period; otherwise false
     */
    public boolean overlap(Period period) {
        return contains(period.lower) || contains(period.upper);
    }

    /**
     * Verifies that the received fields are valid
     * @throws SQLException
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
