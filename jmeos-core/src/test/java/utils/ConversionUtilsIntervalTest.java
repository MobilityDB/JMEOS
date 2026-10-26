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
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifies that a {@link Duration} survives conversion to a MEOS interval and back.
 *
 * <p>Before the fix, {@code timedelta_to_interval} forwarded the total hours, minutes and seconds
 * alongside the whole days, so a multi-day duration was counted several times over and inflated into
 * an out-of-range interval — a later use of that interval (e.g. scaling a temporal) then failed with
 * "the interval must be positive".
 */
public class ConversionUtilsIntervalTest {

    @Test
    void durationRoundTripsThroughAMeosInterval() {
        Duration[] durations = {
                Duration.ofDays(2),
                Duration.ofDays(3).plusHours(4).plusMinutes(5).plusSeconds(6),
                Duration.ofDays(10).plusHours(23).plusMinutes(59).plusSeconds(59),
        };
        for (Duration d : durations) {
            Pointer interval = ConversionUtils.timedelta_to_interval(d);
            assertEquals(d, ConversionUtils.interval_to_timedelta(interval), "round-trip of " + d);
        }
    }

    @Test
    void everyIntervalWithoutMonthsIsItsDuration() {
        // One day, a time below a day, a fraction of a second, and days with a time
        String[] texts = {"1 day", "03:04:05", "00:00:01.25", "2 days 00:00:00.000001"};
        Duration[] durations = {Duration.ofDays(1), Duration.ofSeconds(3 * 3600 + 4 * 60 + 5),
                Duration.ofMillis(1250), Duration.ofDays(2).plusNanos(1000)};
        for (int i = 0; i < texts.length; i++)
            assertEquals(durations[i], ConversionUtils.interval_to_timedelta(
                    GeneratedFunctions.interval_in(texts[i], -1)), texts[i]);
    }

    @Test
    void anIntervalOfMonthsHasNoDuration() {
        Pointer interval = GeneratedFunctions.interval_in("1 mon", -1);
        assertThrows(IllegalArgumentException.class,
                () -> ConversionUtils.interval_to_timedelta(interval));
    }

    @Test
    void wholeDaysProduceTheExpectedIntervalText() {
        Pointer interval = ConversionUtils.timedelta_to_interval(Duration.ofDays(2));
        assertEquals("2 days", GeneratedFunctions.interval_out(interval));
    }
}
