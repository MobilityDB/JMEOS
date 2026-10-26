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
import types.basic.tint.TIntSeq;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Temporal timestamp accessors")
class TimestampAccessorTest {

    static error_handler_fn errorHandler = new error_handler();

    @BeforeAll
    static void init() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    private static LocalDateTime day(int d) {
        return LocalDateTime.of(2019, 9, d, 0, 0, 0);
    }

    @Test
    @DisplayName("timestamp_n returns the n-th timestamp as a calendar value")
    void timestampN() throws Exception {
        TIntSeq seq = new TIntSeq("[1@2019-09-01, 2@2019-09-02, 3@2019-09-03]");
        assertEquals(day(1), seq.timestamp_n(0));
        assertEquals(day(2), seq.timestamp_n(1));
        assertEquals(day(3), seq.timestamp_n(2));
    }

    @Test
    @DisplayName("timestamps returns every timestamp in order")
    void timestamps() throws Exception {
        TIntSeq seq = new TIntSeq("[1@2019-09-01, 2@2019-09-02, 3@2019-09-03]");
        assertEquals(List.of(day(1), day(2), day(3)), seq.timestamps());
    }
}
