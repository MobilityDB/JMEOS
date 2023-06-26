package types.basic.tfloat;

import types.temporal.TInstant;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TFloatInst extends TInstant<Float> {

    /**
     * The string constructor
     * @param value - the string with the TFloatInst value
     * @throws SQLException
     */
    public TFloatInst(String value) throws SQLException {
        super(value, TFloat::getSingleTemporalValue);
    }

    /**
     * The value and timestamp constructor
     * @param value - a float
     * @param time - a timestamp
     * @throws SQLException
     */
    public TFloatInst(float value, OffsetDateTime time) throws SQLException {
        super(value, time);
    }
}
