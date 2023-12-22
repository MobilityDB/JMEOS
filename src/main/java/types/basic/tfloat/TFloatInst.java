package types.basic.tfloat;

import functions.functions;
import jnr.ffi.Pointer;
import types.temporal.TInstant;
import types.temporal.TemporalType;

import java.time.OffsetDateTime;

public class TFloatInst extends TInstant<Float> implements TFloat{
	private Pointer inner;
	private String customType = "Float";
	private TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;

	public TFloatInst(Pointer inner){
		super(inner);
		this.inner = inner;
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TFloatInst value
	 */
	public TFloatInst(String value) {
		super(value);
		this.inner = functions.tfloatinst_in(value);
	}



	@Override
	public Pointer createStringInner(String str){
		return functions.tfloatinst_in(str);
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
