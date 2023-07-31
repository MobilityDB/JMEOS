package jmeos.types.time;

import java.sql.SQLException;

//import jmeos.functions.functions_old.*;
//import static jmeos.functions.functions.meos_initialize;
//import static jmeos.functions.functions.tgeompoint_in;
//import static jmeos.functions.functions_old.meos_finalize;
import static jmeos.functions.functions.*;
import jnr.ffi.Pointer;


public class ExamplePeriod {

    public static void main(String[] args) throws SQLException {
        meos_initialize("UTC");
        Period test = new Period("[2021-04-08 05:04:45+01,2021-09-10 10:00:00+01]");
        Pointer inner = test.get_inner();
        System.out.println(inner);
        String result = period_out(inner);
        System.out.println(result);
        meos_finalize();


    }

}
