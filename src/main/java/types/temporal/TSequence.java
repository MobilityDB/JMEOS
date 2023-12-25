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















    /* To delete */




	

	/**
	 * If the interpolation is explicit or implicit
	 *
	 * @return true if the interpolation is explicit; otherwise false
	 */
	protected boolean explicitInterpolation() {
		return true;
	}


	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() == obj.getClass()) {
			TSequence<?> otherTemporal = (TSequence<?>) obj;
			
			if (stepwise != otherTemporal.stepwise) {
				return false;
			}
			
			if (lowerInclusive != otherTemporal.lowerInclusive) {
				return false;
			}
			
			if (upperInclusive != otherTemporal.upperInclusive) {
				return false;
			}
			
			return super.equals(obj);
		}
		return false;
	}
	

	public int hashCode() {
		String value = toString();
		return value != null ? value.hashCode() : 0;
	}
	
	public boolean isStepwise() {
		return stepwise;
	}
	
	public boolean isLowerInclusive() {
		return lowerInclusive;
	}
	
	public boolean isUpperInclusive() {
		return upperInclusive;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public int numSequences() {
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public TSequence<V> startSequence() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public TSequence<V> endSequence() {
		return this;
	}

	
	/**
	 * {@inheritDoc}
	 */

	public List<TSequence<V>> sequences() {
		ArrayList<TSequence<V>> list = new ArrayList<>();
		list.add(this);
		return list;
	}
}
