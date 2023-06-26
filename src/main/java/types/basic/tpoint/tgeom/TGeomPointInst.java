package types.basic.tpoint.tgeom;

import types.temporal.TInstant;
import net.postgis.jdbc.geometry.Point;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TGeomPointInst extends TInstant<Point> {

    /**
     * The string constructor
     * @param value - the string with the TGeomPointInst value
     * @throws SQLException
     */
    public TGeomPointInst(String value) throws SQLException {
        super(value, TGeomPoint::getSingleTemporalValue);
    }

    /**
     * The value and timestamp constructor
     * @param value - a Point
     * @param time - a timestamp
     * @throws SQLException
     */
    public TGeomPointInst(Point value, OffsetDateTime time) throws SQLException {
        super(value, time);
    }
}
