package types.basic.ttext;

import functions.GeneratedFunctions;
import types.temporal.TSequenceSet;
import jnr.ffi.Pointer;
import types.temporal.TemporalType;

/**
 * Temporal text sequence set class inherited from temporal sequence set.
 *
 * @author ARIJIT SAMAL
 */
public class TTextSeqSet extends TSequenceSet<String> implements TText{

	private Pointer inner;
	private final String customType = "String";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE_SET;


	public TTextSeqSet(){}


	/**
	 * Pointer constructor
	 * @param inner Pointer
	 */
	public TTextSeqSet(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolInst value
	 */
	public TTextSeqSet(String value) {
		super(value);
		this.inner = GeneratedFunctions.ttext_in(value);
	}



	@Override
	public Pointer createStringInner(String str){
		return GeneratedFunctions.ttext_in(str);
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
	public Pointer getTextInner(){
		return inner;
	}
}
