package collections.time;

import functions.error_handler;
import functions.error_handler_fn;
import functions.functions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import types.collections.time.Time;
import types.collections.time.datespan;
import types.collections.time.dateset;
import types.collections.time.datespanset;
import utils.TestLogger;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(TestLogger.class)
class DateSetOperationsTest {

    static error_handler_fn errorHandler = new error_handler();

    DateSetOperationsTest() throws SQLException {
        functions.meos_initialize_timezone("UTC");
    }

    @Test
    void spanIntersectionIsASpan() throws Exception {
        datespan a = new datespan("[2019-09-01, 2019-09-10]");
        datespan b = new datespan("[2019-09-05, 2019-09-15]");
        Time result = a.intersection(b);
        datespan span = assertInstanceOf(datespan.class, result);
        assertEquals(LocalDate.of(2019, 9, 5), span.lower());
        assertEquals(LocalDate.of(2019, 9, 11), span.upper());
    }

    @Test
    void setIntersectionIsASet() throws Exception {
        dateset a = new dateset("{2019-09-01, 2019-09-03, 2019-09-05}");
        dateset b = new dateset("{2019-09-03, 2019-09-05, 2019-09-07}");
        dateset result = assertInstanceOf(dateset.class, a.intersection(b));
        assertEquals(2, result.num_elements());
    }

    @Test
    void setMinusIsASet() throws Exception {
        dateset a = new dateset("{2019-09-01, 2019-09-03, 2019-09-05}");
        dateset b = new dateset("{2019-09-03, 2019-09-05, 2019-09-07}");
        dateset result = assertInstanceOf(dateset.class, a.minus(b));
        assertEquals(1, result.num_elements());
    }

    @Test
    void subtractDateFromSetIsASet() throws Exception {
        dateset a = new dateset("{2019-09-01, 2019-09-03, 2019-09-05}");
        dateset result = a.subtract_from(LocalDate.of(2019, 9, 2));
        assertEquals(1, result.num_elements());
    }

    @Test
    void subtractDateFromSpanIsASpanSet() throws Exception {
        datespan a = new datespan("[2019-09-01, 2019-09-10]");
        assertInstanceOf(datespanset.class, a.subtract_from(LocalDate.of(2019, 9, 20)));
    }

    @Test
    void subtractDateFromSpanSetIsASpanSet() throws Exception {
        datespanset a = new datespanset("{[2019-09-01, 2019-09-05], [2019-09-10, 2019-09-15]}");
        assertInstanceOf(datespanset.class, a.subtract_from(LocalDate.of(2019, 9, 20)));
    }
}
