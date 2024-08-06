package types.basic.tint;

import functions.functions;
import jnr.ffi.Pointer;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.temporal.TSequence;
import types.temporal.TemporalType;


/**
 * Temporal integer sequence class inherited from temporal sequence.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TIntSeq extends TSequence<Integer> implements TInt {
	private Pointer inner;
	private final String customType = "Integer";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE;


	public TIntSeq(){
	}


	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TIntSeq(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntSeq value
	 */
	public TIntSeq(String value){
		this(value,2);
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntSeq value
	 */
	public TIntSeq(String value, int interpolation) {
		super(value);
		this.inner = functions.tint_in(value);
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
