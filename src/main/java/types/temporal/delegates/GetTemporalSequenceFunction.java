package types.temporal.delegates;

import types.temporal.TSequence;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Delegate to parse the string representation of an sequence.
 * Required since it is not possible to create a new instant of an abstract class and it needs to be constructed in the
 * child class
 *
 * @param <V>
 */
public interface GetTemporalSequenceFunction<V extends Serializable> {
	TSequence<V> run(String value) throws SQLException;
}
