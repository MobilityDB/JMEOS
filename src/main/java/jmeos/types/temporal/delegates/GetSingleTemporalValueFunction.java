package jmeos.types.temporal.delegates;

import jmeos.types.temporal.TemporalValue;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Delegate to parse the string representation of an instant to a Temporal Value wrapper.
 * Required since it is not possible to create a new instant of the generic type
 *
 * @param <V>
 */
public interface GetSingleTemporalValueFunction<V extends Serializable> {
	TemporalValue<V> run(String value) throws SQLException;
}
