package types.basic.tpoint;

import jnr.ffi.Pointer;
import org.locationtech.jts.io.ParseException;
import types.temporal.TInstant;
import org.locationtech.jts.geom.Point;

/**
 * Base abstract class for TGeomPointInt and TGeogPointInt
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class TPointInst extends TInstant<Point> implements TPoint{

	public Pointer inner;

	public TPointInst(){
		super();

	}

	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TPointInst(Pointer inner){
		super(inner);
		this.inner = createInner(inner);
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntInst value
	 */
	public TPointInst(String value){
		super(value);
		this.inner = createStringInner(value);
	}

	public abstract Pointer createStringInner(String str);
	public abstract Pointer createInner(Pointer inner);


	/**
	 * Returns the value of the temporal point instant.
	 *
	 * <p>
	 * @param precision The precision of the returned geometry.
	 * @return A new {@link Point} representing the value of the temporal point instant.
	 * @throws ParseException
	 */
	public Point value(int precision) throws ParseException {
		return this.start_value(precision);
	}


}
