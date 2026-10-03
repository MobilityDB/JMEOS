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
