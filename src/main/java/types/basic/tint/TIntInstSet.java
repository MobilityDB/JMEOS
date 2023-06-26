package types.basic.tint;

import types.temporal.TInstantSet;

import java.sql.SQLException;

public class TIntInstSet extends TInstantSet<Integer> {

    /**
     * The string constructor
     * @param value - the string with the TIntInstSet value
     * @throws SQLException
     */
    public TIntInstSet(String value) throws SQLException {
        super(value, TIntInst::new, TInt::compareValue);
    }

    /**
     * The string array constructor
     * @param values - an array of strings
     * @throws SQLException
     */
    public TIntInstSet(String[] values) throws SQLException {
        super(values, TIntInst::new, TInt::compareValue);
    }

    /**
     * The TIntInst array constructor
     * @param values - an array of TIntInst
     * @throws SQLException
     */
    public TIntInstSet(TIntInst[] values) throws SQLException {
        super(values, TInt::compareValue);
    }
}
