package types.basic.tpoint.tgeog;

import types.core.TypeName;
import types.temporal.Temporal;
import types.basic.tpoint.TPoint;
import types.basic.tpoint.helpers.TPointConstants;
import types.temporal.TemporalType;
import types.temporal.TemporalValue;
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

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void setValue(String value) throws SQLException {

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
	 * {@link types.temporal.delegates.GetSingleTemporalValueFunction}
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

}
