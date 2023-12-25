package types.basic.tpoint;

import jnr.ffi.Pointer;
import org.locationtech.jts.io.ParseException;
import types.temporal.TInstant;
import org.locationtech.jts.geom.Point;

/**
 * Base abstract class for TGeomPointIntSet and TGeogPointIntSet
 * Contains logic for handling SRID
 */
public abstract class TPointInst extends TInstant<Point> implements TPoint{

	public Pointer inner;

	public TPointInst(){
		super();

	}
	public TPointInst(Pointer inner){
		super(inner);
		this.inner = createInner(inner);
	}

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
