package jmeos.types.basic.tpoint.tgeog;

import jmeos.types.core.TypeName;
import jmeos.types.temporal.Temporal;
import jmeos.types.basic.tpoint.TPoint;
import jmeos.types.basic.tpoint.helpers.TPointConstants;
import jmeos.types.temporal.TemporalType;
import jmeos.types.temporal.TemporalValue;
import net.postgis.jdbc.geometry.Point;

import java.sql.SQLException;


/**
 * Class that represents the MobilityDB type TGeogPoint
 */
@TypeName(name = "tgeogpoint")
public class TGeogPoint extends TPoint {
	
	/**
	 * The default constructor
	 */
	public TGeogPoint() {
		super();
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TGeogPoint value
	 * @throws SQLException
	 */
	public TGeogPoint(String value) throws SQLException {
		super(value);
	}
	
	/**
	 * The constructor for temporal types
	 *
	 * @param temporal - a TGeogPointInst, TGeogPointInstSet, TGeogPointSeq or a TGeogPointSeqSet
	 */
	public TGeogPoint(Temporal<Point> temporal) {
		super(temporal);
	}
	
	/**
	 * Method with compatible signature for delegate
	 * {@link com.mobilitydb.jdbc.temporal.delegates.GetSingleTemporalValueFunction}
	 *
	 * @param value string representation of the value
	 * @return Temporal value wrapper with the value parsed
	 */
	public static TemporalValue<Point> getSingleTemporalValue(String value) throws SQLException {
		TemporalValue<Point> temporalValue = TPoint.getSingleTemporalValue(value);
		
		if (temporalValue.getValue().getSrid() == TPointConstants.EMPTY_SRID) {
			temporalValue.getValue().setSrid(TPointConstants.DEFAULT_SRID);
		}
		
		return temporalValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String value) throws SQLException {
		TemporalType temporalType = getTemporalType(value);
		switch (temporalType) {
			case TEMPORAL_INSTANT:
				temporal = new TGeogPointInst(value);
				break;
			case TEMPORAL_INSTANT_SET:
				temporal = new TGeogPointInstSet(value);
				break;
			case TEMPORAL_SEQUENCE:
				temporal = new TGeogPointSeq(value);
				break;
			case TEMPORAL_SEQUENCE_SET:
				temporal = new TGeogPointSeqSet(value);
				break;
		}
	}
}
