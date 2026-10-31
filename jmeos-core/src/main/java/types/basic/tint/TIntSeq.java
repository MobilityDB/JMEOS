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

package types.basic.tint;

import functions.GeneratedFunctions;
import jnr.ffi.Pointer;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.temporal.TSequence;
import types.temporal.TemporalType;


/**
 * Temporal integer sequence class inherited from temporal sequence.
 *
 * @author ARIJIT SAMAL
 */
public class TIntSeq extends TSequence<Integer> implements TInt {
	private Pointer inner;
	private final String customType = "Integer";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE;


	public TIntSeq(){
	}


	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TIntSeq(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntSeq value
	 */
	public TIntSeq(String value){
		this(value,2);
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntSeq value
	 */
	public TIntSeq(String value, int interpolation) {
		super(value);
		this.inner = GeneratedFunctions.tint_in(value);
	}






	@Override
	public Pointer createStringInner(String str){
		return GeneratedFunctions.tint_in(str);
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

	/**
	 * Returns the text of "this", its {@code as_text}.
	 * @return A new {@link String} instance
	 */
	@Override
	public String toString() {
		return this.as_text();
	}
}
