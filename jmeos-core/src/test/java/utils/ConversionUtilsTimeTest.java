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

package utils;

import functions.GeneratedFunctions;
import functions.MeosException;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifies the casts of {@link ConversionUtils} against the values of MobilityDB
 * {@code doc/specs/meos-bindings-algebra.md}, section 5, read back through the MEOS text
 * functions, and the values each cast refuses.
 */
@DisplayName("ConversionUtils time casts")
class ConversionUtilsTimeTest {

    @Test
    @DisplayName("a timestamp keeps its fraction of a second both ways")
    void timestampKeepsItsFraction() {
        LocalDateTime dt = LocalDateTime.of(2025, 3, 1, 8, 0, 0, 123_456_000);
        assertEquals(OffsetDateTime.of(dt, ZoneOffset.UTC), ConversionUtils.datetimeToTimestampTz(dt));
        assertEquals(dt, ConversionUtils.timestamptz_to_datetime(
                OffsetDateTime.of(2025, 3, 1, 9, 0, 0, 123_456_000, ZoneOffset.ofHours(1))));
        assertThrows(IllegalArgumentException.class,
                () -> ConversionUtils.datetimeToTimestampTz(dt.plusNanos(1)));
    }

    @Test
    @DisplayName("a date casts through the days from the Unix epoch")
    void dateCasts() {
        String[] texts = {"2001-01-01", "1970-01-01", "1969-12-31", "2025-03-01", "0044-03-15 BC"};
        LocalDate[] dates = {LocalDate.of(2001, 1, 1), LocalDate.of(1970, 1, 1),
                LocalDate.of(1969, 12, 31), LocalDate.of(2025, 3, 1), LocalDate.of(-43, 3, 15)};
        for (int i = 0; i < texts.length; i++) {
            int d = GeneratedFunctions.date_in(texts[i]);
            assertEquals(dates[i], ConversionUtils.date_adt_to_date(d), texts[i]);
            assertEquals(d, ConversionUtils.date_to_date_adt(dates[i]), texts[i]);
        }
        assertThrows(MeosException.class,
                () -> ConversionUtils.date_adt_to_date(GeneratedFunctions.date_in("infinity")));
        assertThrows(ArithmeticException.class,
                () -> ConversionUtils.date_to_date_adt(LocalDate.MAX));
    }

    @Test
    @DisplayName("an interval casts through its parts, a fraction of a microsecond refused")
    void intervalCasts() {
        assertEquals("1 day 12:00:00.000001", GeneratedFunctions.interval_out(
                ConversionUtils.timedelta_to_interval(Duration.ofHours(36).plusNanos(1000))));
        assertEquals("-1 days -00:00:01", GeneratedFunctions.interval_out(
                ConversionUtils.timedelta_to_interval(Duration.ofHours(-24).minusSeconds(1))));
        assertEquals(Duration.ofHours(36), ConversionUtils.interval_to_timedelta(
                GeneratedFunctions.interval_in("36 hours", -1)));
        assertThrows(IllegalArgumentException.class,
                () -> ConversionUtils.timedelta_to_interval(Duration.ofNanos(1500)));
        assertThrows(IllegalArgumentException.class, () -> ConversionUtils.interval_to_timedelta(
                GeneratedFunctions.interval_in("-1 year", -1)));
    }

    @Test
    @DisplayName("a time casts through its microseconds from midnight")
    void timeCasts() {
        String[] texts = {"00:00:00", "00:00:00.000001", "12:34:56.789012", "23:59:59.999999"};
        LocalTime[] times = {LocalTime.MIDNIGHT, LocalTime.of(0, 0, 0, 1000),
                LocalTime.of(12, 34, 56, 789_012_000), LocalTime.of(23, 59, 59, 999_999_000)};
        for (int i = 0; i < texts.length; i++) {
            long t = GeneratedFunctions.time_in(texts[i], -1);
            assertEquals(times[i], ConversionUtils.time_adt_to_time(t), texts[i]);
            assertEquals(t, ConversionUtils.time_to_time_adt(times[i]), texts[i]);
        }
        // a LocalTime ends its day before 24:00:00
        assertThrows(java.time.DateTimeException.class, () -> ConversionUtils.time_adt_to_time(
                GeneratedFunctions.time_in("24:00:00", -1)));
        assertThrows(IllegalArgumentException.class,
                () -> ConversionUtils.time_to_time_adt(LocalTime.of(0, 0, 0, 1)));
    }

    @Test
    @DisplayName("a time with time zone casts through its microseconds and its offset east of UTC")
    void timetzCasts() {
        String[] texts = {"12:00:00+01", "12:00:00-05", "23:59:59.999999+15:59"};
        OffsetTime[] times = {OffsetTime.of(12, 0, 0, 0, ZoneOffset.ofHours(1)),
                OffsetTime.of(12, 0, 0, 0, ZoneOffset.ofHours(-5)),
                OffsetTime.of(23, 59, 59, 999_999_000, ZoneOffset.ofHoursMinutes(15, 59))};
        for (int i = 0; i < texts.length; i++) {
            Pointer t = GeneratedFunctions.timetz_in(texts[i], -1);
            assertEquals(times[i], ConversionUtils.timetz_adt_to_timetz(t), texts[i]);
            assertEquals(texts[i], GeneratedFunctions.timetz_out(
                    ConversionUtils.timetz_to_timetz_adt(times[i])), texts[i]);
        }
        // an offset beyond 16:00:00, which MEOS cannot state
        assertThrows(MeosException.class, () -> ConversionUtils.timetz_to_timetz_adt(
                OffsetTime.of(12, 0, 0, 0, ZoneOffset.ofHoursMinutesSeconds(16, 0, 1))));
        assertThrows(java.time.DateTimeException.class, () -> ConversionUtils.timetz_adt_to_timetz(
                GeneratedFunctions.timetz_in("24:00:00+00", -1)));
    }
}
