package types.basic.tint;

import functions.functions;
import jnr.ffi.Pointer;
import types.temporal.TSequence;
import types.temporal.TemporalType;


/**
 * By Default Interpolation is stepwise
 */
public class TIntSeq extends TSequence<Integer> implements TInt {
	private Pointer inner;
	private String customType = "Integer";
	private TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE;


	public TIntSeq(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntSeq value
	 */
	public TIntSeq(String value, int interpolation) {
		super(value);
		this.inner = functions.tintseq_in(value, interpolation);
	}






	@Override
	public Pointer createStringInner(String str){
		return functions.tint_in(str);
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
