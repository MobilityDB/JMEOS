package types.temporal.delegates;

import java.io.Serializable;

/**
 * Delegate to compare two values of the same time.
 * It is required since not all types used in temporal extends Comparable interface
 * @param <V>
 */
public interface CompareValueFunction<V extends Serializable> extends Serializable {
    int run(V first, V second);
}
