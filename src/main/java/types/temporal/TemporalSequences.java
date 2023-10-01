package types.temporal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for temporal types that holds a collection of sequences
 *
 * @param <V>
 */
public interface TemporalSequences<V extends Serializable> {
	/**
	 * Gets the number of sequences
	 *
	 * @return a number
	 */
	int numSequences();
	
	/**
	 * Gets the first sequence
	 *
	 * @return first temporal sequence
	 */
	TSequence<V> startSequence();
	
	/**
	 * Gets the last sequence
	 *
	 * @return last temporal sequence
	 */
	TSequence<V> endSequence();
	
	/**
	 * Gets the sequence located at the index position
	 *
	 * @param n - the index
	 * @return the sequence at the index
	 * @throws SQLException if the index is out of range
	 */
	TSequence<V> sequenceN(int n) throws SQLException;
	
	/**
	 * Gets all sequences
	 *
	 * @return a list of all temporal sequences
	 */
	List<TSequence<V>> sequences();
}
