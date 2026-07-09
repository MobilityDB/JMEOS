package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifies the {@link TimestampTzConverter} arithmetic against known MEOS
 * {@code TimestampTz} values (microseconds since the PostgreSQL epoch). Runs
 * offline: no libmeos, no FFI.
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
}
