package types.basic.tbool;

import types.temporal.TInstant;
import jnr.ffi.Pointer;
import java.time.OffsetDateTime;
import functions.functions;
import types.temporal.TemporalType;

public class TBoolInst extends TInstant<Boolean> implements TBool {
	private Pointer inner;
	private String customType = "Boolean";
	private TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;


	public TBoolInst(){}

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
	 * The value and timestamp constructor
	 *
	 */
	public TBoolInst(String str, boolean value)  {
		super();
	}

	public TBoolInst(boolean value, String timemstamp)  {
		super();
	}

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