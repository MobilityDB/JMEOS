package types.basic.ttext;

import functions.functions;
import types.temporal.TSequence;

import java.util.List;

import jnr.ffi.Pointer;
import types.temporal.TemporalType;

/**
 * Temporal text sequence class inherited from temporal sequence.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TTextSeq extends TSequence<String> implements TText{
	private Pointer inner;
	private final String customType = "String";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE;


	public TTextSeq(){}


	/**
	 * Pointer constructor
	 *
	 * @param inner
	 */
	public TTextSeq(Pointer inner){
		super(inner);
		this.inner = inner;
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolInst value
	 */
	public TTextSeq(String value){
		this(value,2);
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolInst value
	 */
	public TTextSeq(String value, int interpolation)  {
		super(value);
		this.inner = functions.ttext_in(value);
	}


	/**
	 * The string constructor
	 *
	 */
	public TTextSeq(List<String> list, boolean lower_inc, boolean upper_inc, int interpolation, boolean normalize) {
		super();
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
