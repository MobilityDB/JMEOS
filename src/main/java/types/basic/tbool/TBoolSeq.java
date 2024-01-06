package types.basic.tbool;

import functions.functions;
import jnr.ffi.Pointer;
import types.temporal.TSequence;
import types.temporal.TemporalType;

import java.util.List;

/**
 * Temporal boolean sequence class inherited from temporal sequence.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TBoolSeq extends TSequence<Boolean> implements TBool {
	private Pointer inner;
	private final String customType = "Boolean";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE;


	public TBoolSeq(){

	}

	/**
	 * Pointer constructor
	 *
	 * @param inner
	 */
	public TBoolSeq(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolSeq value
	 */
	public TBoolSeq(String value){
		this(value,2);
	}


	/**
	 * The string and interpolation constructor
	 *
	 * @param value - the string with the TBoolInst value
	 */
	public TBoolSeq(String value, int interpolation) {
		super(value);
		this.inner = functions.tbool_in(value);
	}


	/**
	 * The string constructor
	 *
	 */
	public TBoolSeq(List<String> list, boolean lower_inc, boolean upper_inc,int interpolation, boolean normalize) {
		super();
	}


	@Override
	public Pointer createStringInner(String str){
		return functions.tbool_in(str);
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
	public Pointer getBoolInner(){
		return inner;
	}





}
