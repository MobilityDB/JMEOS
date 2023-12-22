package types.basic.tpoint;

import jnr.ffi.Pointer;
import types.basic.tpoint.helpers.SRIDParseResponse;
import types.basic.tpoint.helpers.SRIDParser;
import types.temporal.TSequenceSet;
import types.temporal.delegates.GetTemporalSequenceFunction;
import types.temporal.TSequence;
import types.temporal.TemporalConstants;
import net.postgis.jdbc.geometry.Point;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Base abstract class for TGeomPointSeqSet and TGeogPointSeqSet
 * Contains logic for handling SRID
 */
public abstract class TPointSeqSet extends TSequenceSet<Point> {
	private int srid;
	private Pointer _inner;
	


	protected TPointSeqSet(Pointer inner){
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
	
	/**
	 * Special logic to split the sequence values since point values contains parentheses
	 *
	 * @param value the sequence set value
	 * @return list of values
	 */
	@Override
	protected List<String> getSequenceValues(String value) {
		String delimiter = ",";
		String[] values = value.split(delimiter, -1);
		List<String> seqValues = new ArrayList<>();
		StringJoiner joiner = new StringJoiner(delimiter);
		
		for (String val : values) {
			joiner.add(val);
			if (val.endsWith(TemporalConstants.UPPER_EXCLUSIVE) || val.endsWith(TemporalConstants.UPPER_INCLUSIVE)) {
				seqValues.add(joiner.toString().trim());
				joiner = new StringJoiner(delimiter);
			}
		}
		
		return seqValues;
	}
}
