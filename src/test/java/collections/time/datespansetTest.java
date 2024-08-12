package collections.time;

import functions.functions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import functions.*;
import types.collections.time.*;

import static org.junit.jupiter.api.Assertions.*;

class datespansetTest {
    private datespanset dspset = new datespanset("{[2019-09-08, 2019-09-10], [2019-09-11, 2019-09-12]}");

    datespansetTest() throws SQLException {
    }

    static error_handler_fn errorHandler = new error_handler();

    public void assert_dateset_equality(dateset vset, List<LocalDate> timestamps){
        functions.meos_initialize("UTC", errorHandler);
        assertEquals(vset.num_elements(), timestamps.size());
    }

    @Test
    public void testStringConstructor(){
        System.out.println(dspset.toString());
        functions.meos_initialize("UTC", errorHandler);
//        List<LocalDate> list = new ArrayList<>();
//        list.add(LocalDate.of(2019, 9, 25));
//        list.add(LocalDate.of(2019, 9, 26));
//        list.add(LocalDate.of(2019, 9, 27));
//        assert_dateset_equality(dset,list);
    }
}
