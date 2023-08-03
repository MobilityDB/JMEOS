package types.basic.tpoint;

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
public class TPointSeqSet extends TSequenceSet<Point> {
	private int srid;
	
	protected TPointSeqSet(String value,
						   GetTemporalSequenceFunction<Point> getTemporalSequenceFunction) throws SQLException {
		super(value, getTemporalSequenceFunction, TPoint::compareValue);
		applySRID();
	}
	
	protected TPointSeqSet(int srid,
						   boolean stepwise,
						   String[] values,
						   GetTemporalSequenceFunction<Point> getTemporalSequenceFunction) throws SQLException {
		super(stepwise, values, getTemporalSequenceFunction, TPoint::compareValue);
		this.srid = srid;
		applySRID();
	}
	
	protected TPointSeqSet(int srid,
						   boolean stepwise,
						   TSequence<Point>[] values) throws SQLException {
		super(stepwise, values, TPoint::compareValue);
		this.srid = srid;
		applySRID();
	}
	
	/**
	 * Parses the SRID value
	 *
	 * @param value - a string with the value
	 * @return the string without SRID
	 * @throws SQLException if it is invalid
	 */
	@Override
	protected String preprocessValue(String value) throws SQLException {
		String newString = super.preprocessValue(value);
		SRIDParseResponse response = SRIDParser.parseSRID(newString);
		srid = response.getSRID();
		return response.getParsedValue();
	}
	
	public void applySRID() throws SQLException {
		srid = SRIDParser.applySRID(srid, getValues());
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
