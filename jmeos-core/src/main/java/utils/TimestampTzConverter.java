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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Casts MEOS {@code TimestampTz} and {@code Timestamp} values to and from {@code java.time} types.
 *
 * <p>A cast converts through MEOS and is exact or an error (MobilityDB
 * {@code doc/specs/meos-bindings-algebra.md}, section 5): Java states an instant as the
 * microseconds from the Unix epoch, which MEOS converts to and from its own count with
 * {@code unix_us_to_timestamptz} and {@code timestamptz_to_unix_us}, a {@code Timestamp} with
 * {@code unix_us_to_timestamp} and {@code timestamp_to_unix_us}. An instant MEOS cannot state
 * raises the MEOS error of the function; an instant with a fraction of a microsecond, which no
 * MEOS value holds, raises an {@link IllegalArgumentException}.
 */
public final class TimestampTzConverter {

    private static final long MICROS_PER_SECOND = 1_000_000L;
    private static final long NANOS_PER_MICRO = 1_000L;

    private TimestampTzConverter() {
    }

    /** Casts an {@link OffsetDateTime} to a MEOS {@code TimestampTz}. */
    public static long toTimestampTz(OffsetDateTime t) {
        return GeneratedFunctions.unixUsToTimestamptz(unixMicros(t.toInstant()));
    }

    /** Casts a {@link LocalDateTime} to a MEOS {@code Timestamp}. */
    public static long toTimestampTz(LocalDateTime t) {
        return GeneratedFunctions.unixUsToTimestamp(unixMicros(t.toInstant(ZoneOffset.UTC)));
    }

    /** Casts a MEOS {@code TimestampTz} to an {@link OffsetDateTime} in UTC. */
    public static OffsetDateTime toOffsetDateTime(long ts) {
        return instant(GeneratedFunctions.timestamptzToUnixUs(ts)).atOffset(ZoneOffset.UTC);
    }

    /** Casts a MEOS {@code Timestamp} to a {@link LocalDateTime}. */
    public static LocalDateTime toLocalDateTime(long ts) {
        return LocalDateTime.ofInstant(instant(GeneratedFunctions.timestampToUnixUs(ts)),
                ZoneOffset.UTC);
    }

    /** The microseconds from the Unix epoch of an instant, which must be a whole number of them. */
    private static long unixMicros(Instant t) {
        if (t.getNano() % NANOS_PER_MICRO != 0) {
            throw new IllegalArgumentException("A MEOS timestamp holds whole microseconds: " + t);
        }
        return Math.addExact(Math.multiplyExact(t.getEpochSecond(), MICROS_PER_SECOND),
                t.getNano() / NANOS_PER_MICRO);
    }

    /** The instant of a count of microseconds from the Unix epoch. */
    private static Instant instant(long us) {
        return Instant.ofEpochSecond(Math.floorDiv(us, MICROS_PER_SECOND),
                Math.floorMod(us, MICROS_PER_SECOND) * NANOS_PER_MICRO);
    }
}
