package types.basic.ttext;

import functions.functions;
import types.temporal.TSequenceSet;
import jnr.ffi.Pointer;
import types.temporal.TemporalType;

/**
 * Temporal text sequence set class inherited from temporal sequence set.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
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
		this.inner = functions.ttext_in(value);
	}



	@Override
	public Pointer createStringInner(String str){
		return functions.ttext_in(str);
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
