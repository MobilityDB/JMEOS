package types.basic.tpoint;

import types.basic.tpoint.helpers.SRIDParseResponse;
import types.basic.tpoint.helpers.SRIDParser;
import types.temporal.TInstant;
import types.temporal.delegates.GetTemporalInstantFunction;
import types.temporal.TInstantSet;
import net.postgis.jdbc.geometry.Point;

import java.sql.SQLException;

/**
 * Base abstract class for TGeomPointIntSet and TGeogPointIntSet
 * Contains logic for handling SRID
 */
public abstract class TPointInstSet extends TInstantSet<Point> {
	private int srid;
	

	
	@Override
	public boolean equals(Object obj) {
		// It is not required to verify if the SRID are equals since it is applied to the temporal values
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		// It is not required to include the SRID since it is applied to the temporal values
		return super.hashCode();
	}
}
