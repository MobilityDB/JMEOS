package types.basic.tpoint.tgeom;

import types.basic.tpoint.helpers.TPointConstants;
import types.basic.tpoint.TPointInstSet;

import java.sql.SQLException;

public class TGeomPointInstSet extends TPointInstSet {

    /**
     * The string constructor
     * @param value - the string with the TGeomPointInstSet value
     * @throws SQLException
     */
    public TGeomPointInstSet(String value) throws SQLException {
        super(value, TGeomPointInst::new);
    }

    /**
     * The string array constructor
     * @param values - an array of strings
     * @throws SQLException
     */
    public TGeomPointInstSet(String[] values) throws SQLException {
        super(TPointConstants.EMPTY_SRID, values, TGeomPointInst::new);
    }

    /**
     * The string array and SRID constructor
     * @param srid - spatial reference identifier
     * @param values - an array of strings
     * @throws SQLException
     */
    public TGeomPointInstSet(int srid, String[] values) throws SQLException {
        super(srid, values, TGeomPointInst::new);
    }

    /**
     * The TGeomPointInstSet array constructor
     * @param values - an array of TGeomPointInstSet
     * @throws SQLException
     */
    public TGeomPointInstSet(TGeomPointInst[] values) throws SQLException {
        super(TPointConstants.EMPTY_SRID, values);
    }

    /**
     * The TGeomPointInst array and SRID constructor
     * @param srid - spatial reference identifier
     * @param values - an array of TGeomPointInst
     * @throws SQLException
     */
    public TGeomPointInstSet(int srid, TGeomPointInst[] values) throws SQLException {
        super(srid, values);
    }
}
