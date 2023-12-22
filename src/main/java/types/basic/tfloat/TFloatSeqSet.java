package types.basic.tfloat;

import functions.functions;
import types.temporal.TSequenceSet;
import jnr.ffi.Pointer;
import types.temporal.TemporalType;

public class TFloatSeqSet extends TSequenceSet<Float> implements TFloat {
	private Pointer inner;
	private String customType = "Float";
	private TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE_SET;

	public TFloatSeqSet(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TFloatSeqSet value
	 */
	public TFloatSeqSet(String value)  {
		super(value);
		this.inner = functions.tfloat_in(value);
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
