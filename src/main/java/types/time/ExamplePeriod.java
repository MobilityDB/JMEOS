package types.time;

import java.sql.SQLException;

import functions.functions;


public class ExamplePeriod {

    public static void main(String[] args) throws SQLException {
        functions.meos_initialize("UTC");

        // From_hexwkb test
        //Period period = Period.from_hexwkb("012100000040021FFE3402000000B15A26350200");
        //String str = functions.period_out(period.get_inner());
        //System.out.println(str);
        //OffsetDateTime offset = period.getLower();


        // Contains Period test
        Period test = new Period("[2021-04-08 05:04:45+06,2021-09-10 10:00:00+06]");
        //System.out.println(test.getValue());
        System.out.println(test.toString());
        //Period test2 = new Period("[2021-04-09 05:04:45+01,2021-04-20 10:00:00+01]");
        //PeriodSet pset = new PeriodSet("{(2020-01-01 00:00:00+00, 2020-01-31 00:00:00+00), (2021-01-01 00:00:00+00, 2021-10-31 00:00:00+00)}");

        //boolean val = test.is_contained_in(pset);
        //boolean secondval = pset.isAdjacent(test);
        //boolean thirdval = test2.isAdjacent(test);
        //boolean last = test2.isAdjacent(pset);

        //Period period_copy = new Period(test.get_inner());
        //System.out.println(period_copy.getValue());
        //System.out.println(secondval);
        //System.out.println(thirdval);
        //System.out.println(last);



        functions.meos_finalize();


    }

}
