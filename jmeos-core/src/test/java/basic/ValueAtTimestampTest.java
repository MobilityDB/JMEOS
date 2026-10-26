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

package basic;

import functions.GeneratedFunctions;
import functions.error_handler;
import functions.error_handler_fn;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import types.basic.tbool.TBoolSeq;
import types.basic.tfloat.TFloatSeq;
import types.basic.tint.TIntSeq;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks the value each temporal type reports at a timestamp against the value it was built from.
 *
 * <p>These accessors read the value out of the native memory MEOS writes, so the read has to agree
 * with the width and offset MEOS uses: an int occupies four bytes, a float is stored as a double,
 * and a bool occupies one byte. A read at the wrong offset or width returns whatever the
 * neighbouring bytes hold — a plausible number rather than an obviously wrong one, which compiles,
 * runs, and reports a value. Asserting the value is what covers that.
 */
@DisplayName("Value at timestamp")
class ValueAtTimestampTest {

    static error_handler_fn errorHandler = new error_handler();

    @BeforeAll
    static void init() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    private static LocalDateTime day(int d) {
        return LocalDateTime.of(2019, 9, d, 0, 0, 0);
    }

    @Test
    @DisplayName("tint reports the integer it holds")
    void tintValueAtTimestamp() {
        TIntSeq seq = new TIntSeq("[25@2019-09-01, 35@2019-09-02]");
        assertEquals(25, seq.value_at_timestamp(day(1)));
    }

    @Test
    @DisplayName("tfloat reports the double it holds, narrowed to float")
    void tfloatValueAtTimestamp() {
        TFloatSeq seq = new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]");
        assertEquals(1.5f, seq.value_at_timestamp(day(1)), 1e-6f);
    }

    @Test
    @DisplayName("tbool reports the boolean it holds")
    void tboolValueAtTimestamp() {
        TBoolSeq seq = new TBoolSeq("[true@2019-09-01, true@2019-09-02]");
        assertTrue(seq.value_at_timestamp(day(1)));
    }
}
