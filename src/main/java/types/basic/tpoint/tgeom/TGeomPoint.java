package types.basic.tpoint.tgeom;


import types.core.TypeName;
import types.temporal.Temporal;
import types.basic.tpoint.TPoint;
import types.temporal.TemporalType;
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
	public TGeomPoint() {
		super();
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void setValue(String value) throws SQLException {

	}
}
