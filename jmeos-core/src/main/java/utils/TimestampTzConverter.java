package utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Bridges MEOS {@code TimestampTz} values and {@code java.time} types.
 *
 * <p>A MEOS {@code TimestampTz} is a signed 64-bit count of microseconds since the
 * PostgreSQL epoch. This class converts that count to and from {@link OffsetDateTime}
 * and {@link LocalDateTime}, applying both the epoch offset and the microsecond unit.
 *
 * <p>Timezone resolution stays in MEOS: a timestamp string is parsed by
 * {@code timestamptz_in}; this class only converts the numeric {@code TimestampTz}.
 */
public final class TimestampTzConverter {

    /** Unix seconds at the PostgreSQL epoch. */
    private static final long EPOCH_UNIX_SECONDS = 946684800L;
    private static final long MICROS_PER_SECOND = 1_000_000L;
    private static final long NANOS_PER_MICRO = 1_000L;

    private TimestampTzConverter() {
    }

    /** Converts an {@link OffsetDateTime} to a MEOS {@code TimestampTz} (microseconds since the PostgreSQL epoch). */
    public static long toTimestampTz(OffsetDateTime t) {
        return (t.toEpochSecond() - EPOCH_UNIX_SECONDS) * MICROS_PER_SECOND
                + t.getNano() / NANOS_PER_MICRO;
    }

    /** Converts a {@link LocalDateTime}, taken as UTC, to a MEOS {@code TimestampTz}. */
    public static long toTimestampTz(LocalDateTime t) {
        return (t.toEpochSecond(ZoneOffset.UTC) - EPOCH_UNIX_SECONDS) * MICROS_PER_SECOND
                + t.getNano() / NANOS_PER_MICRO;
    }

    /** Converts a MEOS {@code TimestampTz} to an {@link OffsetDateTime} in UTC. */
    public static OffsetDateTime toOffsetDateTime(long ts) {
        return toInstant(ts).atOffset(ZoneOffset.UTC);
    }

    /** Converts a MEOS {@code TimestampTz} to a {@link LocalDateTime} in UTC. */
    public static LocalDateTime toLocalDateTime(long ts) {
        return LocalDateTime.ofInstant(toInstant(ts), ZoneOffset.UTC);
    }

    private static Instant toInstant(long ts) {
        // Split the count into seconds and microseconds at the PostgreSQL epoch, then
        // shift the origin to the Unix epoch. floorDiv/floorMod keep the sub-second
        // part in [0, MICROS_PER_SECOND) for values before the epoch (negative ts).
        long second = Math.floorDiv(ts, MICROS_PER_SECOND) + EPOCH_UNIX_SECONDS;
        long micro = Math.floorMod(ts, MICROS_PER_SECOND);
        return Instant.ofEpochSecond(second, micro * NANOS_PER_MICRO);
    }
}
