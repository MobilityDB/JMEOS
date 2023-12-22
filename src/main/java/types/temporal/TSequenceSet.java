package types.temporal;

import jnr.ffi.Pointer;
import types.temporal.delegates.CompareValueFunction;
import types.temporal.delegates.GetTemporalSequenceFunction;
import types.collections.time.Period;
import types.collections.time.PeriodSet;

import java.io.Serializable;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TSequenceSet<V extends Serializable> extends Temporal<V> {
	private CompareValueFunction<V> compareValueFunction;
	protected ArrayList<TSequence<V>> sequenceList = new ArrayList<>();
	protected boolean stepwise;
	private Pointer _inner;


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


	
	/**
	 * Splits the value into a list of string representations of sequences
	 *
	 * @param value the sequence set value
	 * @return list of values
	 */
	protected List<String> getSequenceValues(String value) {
		Matcher m = Pattern.compile("[\\[|\\(].*?[^\\]\\)][\\]|\\)]")
				.matcher(value);
		List<String> seqValues = new ArrayList<>();
		while (m.find()) {
			seqValues.add(m.group());
		}
		return seqValues;
	}
	
	protected boolean explicitInterpolation() {
		return true;
	}
	



	
	/**
	 * {@inheritDoc}
	 */



	

	/**
	 * {@inheritDoc}
	 */
	/*
	public boolean intersectsTimestamp(OffsetDateTime dateTime) {
		for (TSequence<V> sequence : sequenceList) {
			if (sequence.intersectsTimestamp(dateTime)) {
				return true;
			}
		}
		return false;
	}

	 */
	
	/**
	 * {@inheritDoc}
	 */
	/*
	public boolean intersectsPeriod(Period period) {
		for (TSequence<V> sequence : sequenceList) {
			if (sequence.intersectsPeriod(period)) {
				return true;
			}
		}
		return false;
	}

	 */
	

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
			
			if (!thisVal.equals(otherVal)) {
				return false;
			}
		}
		
		return true;
	}
	

	public int hashCode() {
		String value = toString();
		return value != null ? value.hashCode() : 0;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public int numSequences() {
		return sequenceList.size();
	}
	
	/**
	 * {@inheritDoc}
	 */

	public TSequence<V> startSequence() {
		if (sequenceList.isEmpty()) {
			return null;
		}
		
		return sequenceList.get(0);
	}
	
	/**
	 * {@inheritDoc}
	 */

	public TSequence<V> endSequence() {
		if (sequenceList.isEmpty()) {
			return null;
		}
		
		return sequenceList.get(sequenceList.size() - 1);
	}

	
	/**
	 * {@inheritDoc}
	 */

	public List<TSequence<V>> sequences() {
		return new ArrayList<>(sequenceList);
	}
}
