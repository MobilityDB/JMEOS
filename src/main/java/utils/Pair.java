package utils;

/**
 * Key value pair
 *
 * @param <K> key
 * @param <V> value
 * @author Killian Monnier
 * @since 31/07/2023
 */
public record Pair<K, V>(K key, V value) {
	// intentionally empty
}
