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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import functions.MeosException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifies the {@link TimestampTzConverter} casts against known MEOS {@code TimestampTz} values
 * (microseconds since the PostgreSQL epoch), which MEOS converts from and to the microseconds
 * since the Unix epoch, and the values a cast refuses.
 */
@DisplayName("TimestampTzConverter")
class TimestampTzConverterTest {

    private static OffsetDateTime utc(int y, int mo, int d, int h, int mi, int s, int nano) {
        return OffsetDateTime.of(y, mo, d, h, mi, s, nano, ZoneOffset.UTC);
    }

    @Test
    @DisplayName("OffsetDateTime → TimestampTz matches MEOS")
    void offsetDateTimeToTimestampTz() {
        assertEquals(0L, TimestampTzConverter.toTimestampTz(utc(2000, 1, 1, 0, 0, 0, 0)));
        assertEquals(31622400000000L, TimestampTzConverter.toTimestampTz(utc(2001, 1, 1, 0, 0, 0, 0)));
        assertEquals(-946684800000000L, TimestampTzConverter.toTimestampTz(utc(1970, 1, 1, 0, 0, 0, 0)));
        assertEquals(-1000000L, TimestampTzConverter.toTimestampTz(utc(1999, 12, 31, 23, 59, 59, 0)));
        assertEquals(123456L, TimestampTzConverter.toTimestampTz(utc(2000, 1, 1, 0, 0, 0, 123456000)));
    }

    @Test
    @DisplayName("TimestampTz → OffsetDateTime matches MEOS")
    void timestampTzToOffsetDateTime() {
        assertEquals(utc(2000, 1, 1, 0, 0, 0, 0), TimestampTzConverter.toOffsetDateTime(0L));
        assertEquals(utc(2001, 1, 1, 0, 0, 0, 0), TimestampTzConverter.toOffsetDateTime(31622400000000L));
        assertEquals(utc(1970, 1, 1, 0, 0, 0, 0), TimestampTzConverter.toOffsetDateTime(-946684800000000L));
        assertEquals(utc(1999, 12, 31, 23, 59, 59, 0), TimestampTzConverter.toOffsetDateTime(-1000000L));
        assertEquals(utc(2000, 1, 1, 0, 0, 0, 123456000), TimestampTzConverter.toOffsetDateTime(123456L));
    }

    @Test
    @DisplayName("Offset is applied, not ignored")
    void nonUtcOffsetIsResolved() {
        // A +02:00 offset at 02:00 resolves to the same instant as 00:00 UTC.
        OffsetDateTime plusTwo = OffsetDateTime.of(2001, 1, 1, 2, 0, 0, 0, ZoneOffset.ofHours(2));
        assertEquals(31622400000000L, TimestampTzConverter.toTimestampTz(plusTwo));
    }

    @Test
    @DisplayName("LocalDateTime is taken as UTC")
    void localDateTimeIsUtc() {
        assertEquals(31622400000000L,
                TimestampTzConverter.toTimestampTz(LocalDateTime.of(2001, 1, 1, 0, 0, 0)));
        assertEquals(LocalDateTime.of(2001, 1, 1, 0, 0, 0),
                TimestampTzConverter.toLocalDateTime(31622400000000L));
    }

    @ParameterizedTest
    @DisplayName("TimestampTz round-trips through OffsetDateTime")
    @ValueSource(longs = {0L, 31622400000000L, -946684800000000L, -1000000L, 123456L, 1718455000000000L})
    void roundTrip(long ts) {
        assertEquals(ts, TimestampTzConverter.toTimestampTz(TimestampTzConverter.toOffsetDateTime(ts)));
    }

    @Test
    @DisplayName("A fraction of a microsecond is refused, never truncated")
    void subMicrosecondIsRefused() {
        assertThrows(IllegalArgumentException.class,
                () -> TimestampTzConverter.toTimestampTz(utc(2001, 1, 1, 0, 0, 0, 500)));
        assertThrows(IllegalArgumentException.class,
                () -> TimestampTzConverter.toTimestampTz(utc(2001, 1, 1, 0, 0, 0, 1999)));
        assertThrows(IllegalArgumentException.class,
                () -> TimestampTzConverter.toTimestampTz(LocalDateTime.of(2001, 1, 1, 0, 0, 0, 1)));
        assertEquals(31622400000001L, TimestampTzConverter.toTimestampTz(utc(2001, 1, 1, 0, 0, 0, 1000)));
    }

    @Test
    @DisplayName("A value the other side cannot state raises the MEOS error")
    void outOfRangeIsRefused() {
        // infinity, which no instant states
        assertThrows(MeosException.class, () -> TimestampTzConverter.toOffsetDateTime(Long.MAX_VALUE));
        assertThrows(MeosException.class, () -> TimestampTzConverter.toLocalDateTime(Long.MIN_VALUE));
        // an instant before the first timestamp MEOS states, 4714-11-24 BC
        assertThrows(MeosException.class,
                () -> TimestampTzConverter.toTimestampTz(utc(-5000, 1, 1, 0, 0, 0, 0)));
        // an instant whose microseconds from the Unix epoch no long holds
        assertThrows(ArithmeticException.class,
                () -> TimestampTzConverter.toTimestampTz(utc(294277, 1, 1, 0, 0, 0, 0)));
    }
}
