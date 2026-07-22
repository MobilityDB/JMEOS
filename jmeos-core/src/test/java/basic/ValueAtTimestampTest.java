package basic;

import functions.GeneratedFunctions;
import functions.error_handler;
import functions.error_handler_fn;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import types.basic.tbool.TBoolSeq;
import types.basic.tfloat.TFloatSeq;
import types.basic.tint.TIntSeq;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks the value each temporal type reports at a timestamp against the value it was built from.
 *
 * <p>These accessors read the value out of the native memory MEOS writes, so the read has to agree
 * with the width and offset MEOS uses: an int occupies four bytes, a float is stored as a double,
 * and a bool occupies one byte. A read at the wrong offset or width returns whatever the
 * neighbouring bytes hold — a plausible number rather than an obviously wrong one, which compiles,
 * runs, and reports a value. Asserting the value is what covers that.
 */
@DisplayName("Value at timestamp")
class ValueAtTimestampTest {

    static error_handler_fn errorHandler = new error_handler();

    @BeforeAll
    static void init() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        GeneratedFunctions.meos_initialize_error_handler(errorHandler);
    }

    private static LocalDateTime day(int d) {
        return LocalDateTime.of(2019, 9, d, 0, 0, 0);
    }

    @Test
    @DisplayName("tint reports the integer it holds")
    void tintValueAtTimestamp() {
        TIntSeq seq = new TIntSeq("[25@2019-09-01, 35@2019-09-02]");
        assertEquals(25, seq.value_at_timestamp(day(1)));
    }

    @Test
    @DisplayName("tfloat reports the double it holds, narrowed to float")
    void tfloatValueAtTimestamp() {
        TFloatSeq seq = new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]");
        assertEquals(1.5f, seq.value_at_timestamp(day(1)), 1e-6f);
    }

    @Test
    @DisplayName("tbool reports the boolean it holds")
    void tboolValueAtTimestamp() {
        TBoolSeq seq = new TBoolSeq("[true@2019-09-01, true@2019-09-02]");
        assertTrue(seq.value_at_timestamp(day(1)));
    }
}
