package collections.time;

import functions.*;
import org.junit.jupiter.api.Test;
import types.collections.time.datespan;

import java.sql.SQLException;

class datespanTest {
    private datespan datespan1= new datespan("(2019-09-08, 2019-09-10)");
//    private dateset otherdateset= new dateset("{2020-01-01, 2020-01-31}");
//    private LocalDate datevalue= LocalDate.of(2019, 9, 25);


    datespanTest() throws SQLException {
    }

    static error_handler_fn errorHandler = new error_handler();

    // Constructors Tests
    @Test
    public void testStringConstructor() throws Exception {
        functions.meos_initialize("UTC", errorHandler);
//        Pointer p= functions.dateset_in("{2019-09-25, 2019-09-26}");
//        System.out.println(functions.dateset_out(p));
//        System.out.println(dateset1.toString());
//        assertTrue(dateSet instanceof dateset);
//        List<LocalDate> expectedElements = Arrays.asList(
//                LocalDate.of(2019, 9, 25),
//                LocalDate.of(2019, 9, 26),
//                LocalDate.of(2019, 9, 27)
//        );
//        assertEquals(expectedElements, dateset1.elements());
    }
}
