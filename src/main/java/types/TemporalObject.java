package types;

import jnr.ffi.Pointer;
import types.core.DataType;

/**
 * Interface used to link multiple types to a temporal object.
 *
 * @author Killian Monnier
 * @since 09/08/2023
 */
public abstract class TemporalObject<T> extends DataType {
	protected T _inner = null;
	public T get_inner() {
		return this._inner;
	}
}
