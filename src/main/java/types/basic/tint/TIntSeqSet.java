package types.basic.tint;

import functions.functions;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.temporal.TSequenceSet;
import jnr.ffi.Pointer;
import types.temporal.TemporalType;



/**
 * Temporal integer sequence set class inherited from temporal sequence set.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TIntSeqSet extends TSequenceSet<Integer> implements TInt{
	private Pointer inner;
	private final String customType = "Integer";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE_SET;


	public TIntSeqSet(){
	}


	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TIntSeqSet(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntSeqSet value
	 */
	public TIntSeqSet(String value) {
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
