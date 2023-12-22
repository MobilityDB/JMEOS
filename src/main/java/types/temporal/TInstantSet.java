package types.temporal;

import types.temporal.delegates.CompareValueFunction;
import types.temporal.delegates.GetTemporalInstantFunction;
import types.collections.time.Period;
import types.collections.time.PeriodSet;

import java.io.Serializable;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Base class for temporal instant set
 *
 * @param <V>
 */
public abstract class TInstantSet<V extends Serializable> {

	public TInstantSet(){

	}


	
	/**
	 * {@inheritDoc}
	 */

	public Duration duration() {
		return Duration.ZERO;
	}

	
	/**
	 * {@inheritDoc}
	 */
	/*
	public boolean intersectsPeriod(Period period) {
		for (TInstant<V> instant : instantList) {
			if (instant.intersectsPeriod(period)) {
				return true;
			}
		}
		return false;
	}


	 */

}
