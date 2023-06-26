package types.temporal;

import types.core.DataType;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Base class for Temporal Data Types (eg TInt, TFloat)
 * @param <V>
 */
public abstract class TemporalDataType<V extends Serializable> extends DataType {
    protected Temporal<V> temporal;

    protected TemporalDataType() {
        super();
    }

    protected TemporalDataType(final String value) throws SQLException {
        super();
        setValue(value);
    }

    /**
     * Returns the temporal subtype object.
     * @return Temporal
     */
    public Temporal<V> getTemporal() {
        return temporal;
    }

    /**
     * Gets the temporal subtype that can be used to cast the temporal to the correct subclass.
     * @return TemporalType
     */
    public TemporalType getTemporalType() {
        return temporal.getTemporalType();
    }

    /** {@inheritDoc} */
    @Override
    public String getValue() {
        return temporal.buildValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof TemporalDataType<?>) {
            TemporalDataType<?> other = (TemporalDataType<?>)obj;
            return temporal.equals(other.temporal);
        }

        return false;
    }

    @Override
    public int hashCode() {
        String value = toString();
        return value != null ? value.hashCode() : 0;
    }
}
