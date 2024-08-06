package types.basic.ttext;

import functions.functions;
import jnr.ffi.Pointer;
import types.temporal.TInstant;
import types.temporal.TemporalType;


/**
 * Temporal text instant class inherited from temporal instant.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TTextInst extends TInstant<String> implements TText {


	private Pointer inner;
	private final String customType = "String";
	private final TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;


	public TTextInst(){}


	/**
	 * Pointer constructor
	 * @param inner Pointer
	 */
	public TTextInst(Pointer inner){
		super(inner);
		this.inner = inner;
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolInst value
	 */
	public TTextInst(String value)  {
		super(value);
		this.inner = functions.ttextinst_in(value);
	}

	/**
	 * The value and timestamp constructor
	 *
	 */
	public TTextInst(String str, boolean value) {
		super();
	}

	public TTextInst(boolean value, String timemstamp)  {
		super();
	}

	public TTextInst(String str, boolean value, String timemstamp, Pointer inner) {
		super();
	}


	@Override
	public Pointer createStringInner(String str){
		return functions.ttextinst_in(str);
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
