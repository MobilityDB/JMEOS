package basic;

import functions.GeneratedFunctions;

import functions.error_handler;
import functions.error_handler_fn;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import types.basic.tint.TIntSeq;
import types.temporal.Temporal;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Temporal instants accessor")
class InstantsAccessorTest {

    static error_handler_fn errorHandler = new error_handler();

    @BeforeAll
    static void init() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    private static LocalDateTime day(int d) {
        return LocalDateTime.of(2019, 9, d, 0, 0, 0);
    }

    @Test
    @DisplayName("instants returns every instant in order, one pointer per element")
    void instants() throws Exception {
        TIntSeq seq = new TIntSeq("[1@2019-09-01, 2@2019-09-02, 3@2019-09-03]");
        List<Temporal> instants = seq.instants();
        assertEquals(3, instants.size());
        assertEquals(List.of(day(1)), instants.get(0).timestamps());
        assertEquals(List.of(day(2)), instants.get(1).timestamps());
        assertEquals(List.of(day(3)), instants.get(2).timestamps());
    }
}
