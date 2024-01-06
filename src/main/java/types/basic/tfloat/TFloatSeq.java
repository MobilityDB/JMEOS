package types.basic.tfloat;

import functions.functions;
import jnr.ffi.Pointer;
import types.temporal.TSequence;
import types.temporal.TemporalType;

import java.util.List;


/**
 * Temporal float sequence class inherited from temporal sequence.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TFloatSeq extends TSequence<Float> implements TFloat {
	private Pointer inner;
	private final String customType = "Float";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE;


	public TFloatSeq(){}


	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TFloatSeq(Pointer inner){
		super(inner);
		this.inner = inner;
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TFloatSeq value
	 */
	public TFloatSeq(String value){
		this(value,3);
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TFloatSeq value
	 * @param interpolation - interpolation value
	 */
	public TFloatSeq(String value, int interpolation)   {
		super(value);
		this.inner = functions.tfloatseq_in(value, interpolation);
	}


	/**
	 * The string constructor
	 *
	 */
	public TFloatSeq(List<String> list, boolean lower_inc, boolean upper_inc, int interpolation, boolean normalize)  {
		super();
	}



	@Override
	public Pointer createStringInner(String str){
		return functions.tfloat_in(str);
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
