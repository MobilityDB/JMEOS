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

package collections.time;

import functions.GeneratedFunctions;

import functions.error_handler;
import functions.error_handler_fn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import types.collections.time.Time;
import types.collections.time.datespan;
import types.collections.time.dateset;
import types.collections.time.datespanset;
import utils.TestLogger;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(TestLogger.class)
class DateSetOperationsTest {

    static error_handler_fn errorHandler = new error_handler();

    DateSetOperationsTest() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    @Test
    void spanIntersectionIsASpan() throws Exception {
        datespan a = new datespan("[2019-09-01, 2019-09-10]");
        datespan b = new datespan("[2019-09-05, 2019-09-15]");
        Time result = a.intersection(b);
        datespan span = assertInstanceOf(datespan.class, result);
        assertEquals(LocalDate.of(2019, 9, 5), span.lower());
        assertEquals(LocalDate.of(2019, 9, 11), span.upper());
    }

    @Test
    void setIntersectionIsASet() throws Exception {
        dateset a = new dateset("{2019-09-01, 2019-09-03, 2019-09-05}");
        dateset b = new dateset("{2019-09-03, 2019-09-05, 2019-09-07}");
        dateset result = assertInstanceOf(dateset.class, a.intersection(b));
        assertEquals(2, result.num_elements());
    }

    @Test
    void setMinusIsASet() throws Exception {
        dateset a = new dateset("{2019-09-01, 2019-09-03, 2019-09-05}");
        dateset b = new dateset("{2019-09-03, 2019-09-05, 2019-09-07}");
        dateset result = assertInstanceOf(dateset.class, a.minus(b));
        assertEquals(1, result.num_elements());
    }

    @Test
    void subtractDateFromSetIsASet() throws Exception {
        dateset a = new dateset("{2019-09-01, 2019-09-03, 2019-09-05}");
        dateset result = a.subtract_from(LocalDate.of(2019, 9, 2));
        assertEquals(1, result.num_elements());
    }

    @Test
    void subtractDateFromSpanIsASpanSet() throws Exception {
        datespan a = new datespan("[2019-09-01, 2019-09-10]");
        assertInstanceOf(datespanset.class, a.subtract_from(LocalDate.of(2019, 9, 20)));
    }

    @Test
    void subtractDateFromSpanSetIsASpanSet() throws Exception {
        datespanset a = new datespanset("{[2019-09-01, 2019-09-05], [2019-09-10, 2019-09-15]}");
        assertInstanceOf(datespanset.class, a.subtract_from(LocalDate.of(2019, 9, 20)));
    }
}
