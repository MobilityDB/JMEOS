package types.basic.tbool;

import types.temporal.TInstant;
import jnr.ffi.Pointer;
import functions.functions;
import types.temporal.TemporalType;


/**
 * Temporal boolean instant class inherited from temporal instant.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TBoolInst extends TInstant<Boolean> implements TBool {
	private Pointer inner;
	private final String customType = "Boolean";
	private final TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;


	public TBoolInst(){}


	/**
	 * Pointer constructor
	 * @param inner Pointer
	 */
	public TBoolInst(Pointer inner){
		super(inner);
		this.inner = inner;
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolInst value
	 */
	public TBoolInst(String value) {
		super(value);
		this.inner = functions.tboolinst_in(value);
	}

	/**
	 * Value and timestamp constructor
	 *
	 * @param str timestamp
	 * @param value boolean value
	 */
	public TBoolInst(String str, boolean value)  {
		super();
	}

	/**
	 * Value and timestamp constructor
	 *
	 * @param timemstamp timestamp
	 * @param value boolean value
	 */
	public TBoolInst(boolean value, String timemstamp)  {
		super();
	}

	/**
	 * Mixed constructor
	 *
	 * @param str original String
	 * @param value boolean value
	 * @param timemstamp timestamp value
	 * @param inner Pointer
	 */
	public TBoolInst(String str, boolean value, String timemstamp, Pointer inner)  {
		super();
	}


	@Override
	public Pointer createStringInner(String str){
		return functions.tboolinst_in(str);
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