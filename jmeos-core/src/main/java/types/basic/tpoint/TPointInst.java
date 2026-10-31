/*****************************************************************************
 *
 * This MobilityDB code is provided under The PostgreSQL License.
 * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB
 * contributors
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without a written
 * agreement is hereby granted, provided that the above copyright notice and
 * this paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON
 * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *****************************************************************************/

package types.basic.tpoint;

import jnr.ffi.Pointer;
import org.locationtech.jts.io.ParseException;
import types.temporal.TInstant;
import org.locationtech.jts.geom.Point;

/**
 * Base abstract class for TGeomPointInt and TGeogPointInt
 *
 * @author ARIJIT SAMAL
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

	/**
	 * Returns the text of "this", its {@code as_text}.
	 * @return A new {@link String} instance
	 */
	@Override
	public String toString() {
		return this.as_text();
	}
}
