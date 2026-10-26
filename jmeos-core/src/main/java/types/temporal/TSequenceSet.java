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

package types.temporal;

import jnr.ffi.Pointer;

import java.io.Serializable;
import java.util.*;


/**
 * Base class for all temporal sequence set
 *
 * @param <V> base classe such as integer, boolean, text
 *
 * @author ARIJIT SAMAL
 */
public abstract class TSequenceSet<V extends Serializable> extends Temporal<V> {
	protected ArrayList<TSequence<V>> sequenceList = new ArrayList<>();
	protected boolean stepwise;
	private Pointer _inner;


	public TSequenceSet(){
		super();
	}

	protected TSequenceSet(Pointer inner){
		super(inner);
		this._inner = inner;

	}

	public TSequenceSet(String str){
		super(str);
		this._inner = createStringInner(str);
	}

	public abstract Pointer createStringInner(String str);
	public abstract Pointer createInner(Pointer inner);


	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		TSequenceSet<?> otherTemporal = (TSequenceSet<?>) obj;
		
		if (this.stepwise != otherTemporal.stepwise) {
			return false;
		}
		
		if (this.sequenceList.size() != otherTemporal.sequenceList.size()) {
			return false;
		}
		
		for (int i = 0; i < this.sequenceList.size(); i++) {
			TSequence<V> thisVal = sequenceList.get(i);
			TSequence<?> otherVal = otherTemporal.sequenceList.get(i);
			
			if (!thisVal.eq(otherVal)) {
				return false;
			}
		}
		
		return true;
	}
	

	public int hashCode() {
		String value = toString();
		return value != null ? value.hashCode() : 0;
	}
}
