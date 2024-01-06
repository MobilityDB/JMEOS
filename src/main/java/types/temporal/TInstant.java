package types.temporal;

import java.io.Serializable;

import jnr.ffi.Pointer;

/**
 * Base class for all temporal instant
 *
 * @param <V> base classe such as integer, boolean, text
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class TInstant<V extends Serializable> extends Temporal<V> {
	private final TemporalValue<V> temporalValue = null;
	private Pointer _inner = null;


	public TInstant(){
		super();
	}


	public TInstant(Pointer inner){
		super(inner);
		this._inner = createInner(inner);
	}

	public TInstant(String str){
		super(str);
		this._inner = createStringInner(str);
	}

	public abstract Pointer createStringInner(String str);
	public abstract Pointer createInner(Pointer inner);
	
}
