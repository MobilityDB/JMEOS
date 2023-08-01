package jmeos.types.time;

import java.sql.SQLException;
import java.time.OffsetDateTime;

//import jmeos.functions.functions_old.*;
//import static jmeos.functions.functions.meos_initialize;
//import static jmeos.functions.functions.tgeompoint_in;
//import static jmeos.functions.functions_old.meos_finalize;
import static jmeos.functions.functions.*;
import jnr.ffi.Pointer;


public class ExamplePeriod {

    public static void main(String[] args) throws SQLException {
        meos_initialize("UTC");

        // From_hexwkb test
        Period period = Period.from_hexwkb("012100000040021FFE3402000000B15A26350200");
        OffsetDateTime offset = period.getLower();


        // Contains Period test
        Period test = new Period("[2021-04-08 05:04:45+01,2021-09-10 10:00:00+01]");
        Period test2 = new Period("[2021-04-09 05:04:45+01,2021-04-20 10:00:00+01]");
        boolean res = test.contains_Period(test2);
        System.out.println(res);


        meos_finalize();


    }

}
