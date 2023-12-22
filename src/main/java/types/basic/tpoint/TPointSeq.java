package types.basic.tpoint;

import jnr.ffi.Pointer;
import types.basic.tpoint.helpers.SRIDParseResponse;
import types.basic.tpoint.helpers.SRIDParser;
import types.temporal.TInstant;
import types.temporal.delegates.GetTemporalInstantFunction;
import types.temporal.TSequence;
import net.postgis.jdbc.geometry.Point;

import java.sql.SQLException;

/**
 * Base abstract class for TGeomPointSeq and TGeogPointSeq
 * Contains logic for handling SRID
 */
public abstract class TPointSeq extends TSequence<Point> {
	private int srid;
	private Pointer _inner;


	protected TPointSeq(Pointer inner){
		super(inner);
		this._inner = inner;
	}
	
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
