package types.temporal;

import jnr.ffi.Pointer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class TSequence<V extends Serializable> extends Temporal<V> {
	protected boolean stepwise;
	private boolean lowerInclusive;
	private boolean upperInclusive;
	private Pointer _inner;


	public TSequence(){
		super();
	}


	public TSequence(Pointer inner){
		super(inner);
		this._inner = createInner(inner);
	}

	public TSequence(String str){
		super(str);
		this._inner = createStringInner(str);
	}

	public abstract Pointer createStringInner(String str);
	public abstract Pointer createInner(Pointer inner);

}
