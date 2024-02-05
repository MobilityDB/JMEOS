package types.temporal;

import jnr.ffi.Pointer;

import java.io.Serializable;
import java.util.*;


/**
 * Base class for all temporal sequence set
 *
 * @param <V> base classe such as integer, boolean, text
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class TSequenceSet<V extends Serializable> extends Temporal<V> {
	protected ArrayList<TSequence<V>> sequenceList = new ArrayList<>();
	protected boolean stepwise;
	private Pointer _inner;


	public TSequenceSet(){
		super();
	}

	protected TSequenceSet(Pointer inner){
		super(inner);
		this._inner = inner;

	}

	public TSequenceSet(String str){
		super(str);
		this._inner = createStringInner(str);
	}

	public abstract Pointer createStringInner(String str);
	public abstract Pointer createInner(Pointer inner);


	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		TSequenceSet<?> otherTemporal = (TSequenceSet<?>) obj;
		
		if (this.stepwise != otherTemporal.stepwise) {
			return false;
		}
		
		if (this.sequenceList.size() != otherTemporal.sequenceList.size()) {
			return false;
		}
		
		for (int i = 0; i < this.sequenceList.size(); i++) {
			TSequence<V> thisVal = sequenceList.get(i);
			TSequence<?> otherVal = otherTemporal.sequenceList.get(i);
			
			if (!thisVal.eq(otherVal)) {
				return false;
			}
		}
		
		return true;
	}
	

	public int hashCode() {
		String value = toString();
		return value != null ? value.hashCode() : 0;
	}
	

}
