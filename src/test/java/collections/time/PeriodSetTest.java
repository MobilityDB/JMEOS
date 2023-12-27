package collections.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import functions.functions;

import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;
import types.collections.time.PeriodSet;
import types.collections.time.Period;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PeriodSetTest {
	PeriodSet pset = new PeriodSet("{[2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00], [2019-09-03 00:00:00+00, 2019-09-04 00:00:00+00]}");

    PeriodSetTest() throws SQLException {
    }



	public void assert_periodset_equality(PeriodSet pset, List<Period> plist){
		assertEquals(pset.num_periods(), 2);
		//assertEquals(pset.periods(),plist);
	}

	@Test
	public void testStringConstructor(){
		functions.meos_initialize("UTC");
		assert_periodset_equality(this.pset,null);
	}

	/*
	@Test
	public void testFromAsConstructor() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.pset.toString(), new PeriodSet("{[2019-09-01 00:00:00+01, 2019-09-02 00:00:00+01], [2019-09-03 00:00:00+01, 2019-09-04 00:00:00+01]}").toString());
	}

	 */

	@Test
	public void testCopyConstructor() throws SQLException {
		functions.meos_initialize("UTC");
		PeriodSet new_pset = this.pset.copy();
		assertEquals(this.pset.toString(),new_pset.toString());
	}

	/*
	@Test
	public void testConversions() throws SQLException {
		functions.meos_initialize("UTC");
		Period p = new Period("[2019-09-01 00:00:00+01, 2019-09-04 00:00:00+01]");
		assertEquals(this.pset.to_period().toString(), p.toString());
	}

	 */


	@Test
	public void testNumPeriods() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.pset.num_periods(),2);
	}


	@Test
	public void testStartPeriod() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.pset.start_period().toString(),new Period("[2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00]").toString());
	}


	@Test
	public void testEndPeriod() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.pset.end_period().toString(),new Period("[2019-09-03 00:00:00+00, 2019-09-04 00:00:00+00]").toString());
	}



	@Test
	public void testHash() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.pset.hash(),552347465);
	}







}
