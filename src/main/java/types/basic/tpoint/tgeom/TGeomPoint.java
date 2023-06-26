package types.basic.tpoint.tgeom;




import types.core.TypeName;
import types.temporal.Temporal;
import types.temporal.TemporalType;
import types.basic.tpoint.TPoint;
import net.postgis.jdbc.geometry.Point;


import java.sql.SQLException;

/**
 * Class that represents the MobilityDB type TGeomPoint
 */
@TypeName(name = "tgeompoint")
public class TGeomPoint extends TPoint {

    /**
     * The default constructor
     */
    public TGeomPoint() { super(); }

    /**
     * The string constructor
     * @param value - the string with the TGeomPoint value
     * @throws SQLException
     */
    public TGeomPoint(String value) throws SQLException {
        super(value);
    }

    /**
     * The constructor for temporal types
     * @param temporal - a TGeomPointInst, TGeomPointInstSet, TGeomPointSeq or a TGeomPointSeqSet
     */
    public TGeomPoint(Temporal<Point> temporal) {
        super(temporal);
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(String value) throws SQLException {
        TemporalType temporalType = getTemporalType(value);
        switch (temporalType) {
            case TEMPORAL_INSTANT:
                temporal = new TGeomPointInst(value);
                break;
            case TEMPORAL_INSTANT_SET:
                temporal = new TGeomPointInstSet(value);
                break;
            case TEMPORAL_SEQUENCE:
                temporal = new TGeomPointSeq(value);
                break;
            case TEMPORAL_SEQUENCE_SET:
                temporal = new TGeomPointSeqSet(value);
                break;
        }
    }
}
