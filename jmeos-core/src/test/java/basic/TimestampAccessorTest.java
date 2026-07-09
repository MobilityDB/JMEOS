package basic;

import functions.error_handler;
import functions.error_handler_fn;
import functions.functions;
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
        functions.meos_initialize_timezone("UTC");
        functions.meos_initialize_error_handler(errorHandler);
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
