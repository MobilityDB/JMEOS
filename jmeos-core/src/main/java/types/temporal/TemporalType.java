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

import java.sql.SQLException;

/**
 * Classification of temporal types
 * @author ARIJIT SAMAL
 */
public enum TemporalType {
	TEMPORAL_INSTANT,
	TEMPORAL_INSTANT_SET,
	TEMPORAL_SEQUENCE,
	TEMPORAL_SEQUENCE_SET;
	
	/**
	 * Gets the temporal type based on the string received.
	 *
	 * @param value                - the string to be analyzed
	 * @param temporalDataTypeName - the name of the calling class
	 * @return a TemporalType
	 * @throws SQLException if the value does not have a valid format
	 */
	public static TemporalType getTemporalType(String value, String temporalDataTypeName) throws SQLException {
		if (value.startsWith("Interp=Stepwise;")) {
			if (value.startsWith("{", 16)) {
				return TemporalType.TEMPORAL_SEQUENCE_SET;
			} else {
				return TemporalType.TEMPORAL_SEQUENCE;
			}
		} else if (!value.startsWith("{") && !value.startsWith("[") && !value.startsWith("(")) {
			return TemporalType.TEMPORAL_INSTANT;
		} else if (value.startsWith("[") || value.startsWith("(")) {
			return TemporalType.TEMPORAL_SEQUENCE;
		} else if (value.startsWith("{")) {
			if (value.startsWith("[", 1) || value.startsWith("(", 1)) {
				return TemporalType.TEMPORAL_SEQUENCE_SET;
			} else {
				return TemporalType.TEMPORAL_INSTANT_SET;
			}
		} else {
			throw new SQLException(String.format("Could not parse %s value.", temporalDataTypeName));
		}
	}
}
