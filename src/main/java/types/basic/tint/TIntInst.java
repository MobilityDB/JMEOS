package types.basic.tint;

import functions.functions;
import jnr.ffi.Pointer;
import types.temporal.TInstant;
import types.temporal.TemporalType;


/**
 * Temporal integer instant class inherited from temporal instant.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TIntInst extends TInstant<Integer> implements TInt{
	private Pointer inner;
	private final String customType = "Integer";
	private final TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;

	public TIntInst(){}


	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TIntInst(Pointer inner){
		super(inner);
		this.inner = inner;
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntInst value
	 */
	public TIntInst(String value) {
		super(value);
		this.inner = functions.tintinst_in(value);
	}



	@Override
	public Pointer createStringInner(String str){
		return functions.tintinst_in(str);
	}

	@Override
	public Pointer createInner(Pointer inner){
		return inner;
	}

	@Override
	public String getCustomType(){
		return this.customType;
	}

	@Override
	public TemporalType getTemporalType(){
		return this.temporalType;
	}

	@Override
	public Pointer getNumberInner(){
		return inner;
	}


}
