package collections.time;

import functions.functions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import types.collections.number.IntSpan;
import types.collections.time.Period;
import types.collections.time.PeriodSet;

import static functions.functions.meos_finalize;
import static functions.functions.meos_initialize;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PeriodTest {
	private Period period = new Period("(2019-09-08 00:00:00+00, 2019-09-10 00:00:00+00)");
	private Period period2 = new Period("[2019-09-08 02:03:00+0, 2019-09-10 02:03:00+0]");
	
	PeriodTest() throws SQLException {
	}

	static Stream<Arguments> period_constructor() throws SQLException {
		functions.meos_initialize("UTC");
		return Stream.of(
				Arguments.of("(2019-09-08 00:00:00+0, 2019-09-10 00:00:00+0)",LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0), false,false),
				Arguments.of("[2019-09-08 00:00:00+0, 2019-09-10 00:00:00+0]", LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0), true,true)
		);
	}

	static Stream<Arguments> period_constructor2() throws SQLException {
		functions.meos_initialize("UTC");
		return Stream.of(
				Arguments.of("2019-09-08 00:00:00+0", "2019-09-10 00:00:00+0",LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0))
				);
	}

	static Stream<Arguments> period_constructor3() throws SQLException {
		functions.meos_initialize("UTC");
		return Stream.of(
				Arguments.of(LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0), LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0))
		);
	}

	static Stream<Arguments> period_constructor4() throws SQLException {
		functions.meos_initialize("UTC");
		return Stream.of(
				Arguments.of("2019-09-08 00:00:00+0", LocalDateTime.of(2019, 9, 10, 0, 0),LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0))
		);
	}


	static Stream<Arguments> period_constructor5() throws SQLException {
		functions.meos_initialize("UTC");
		return Stream.of(
				Arguments.of(true,true),
				Arguments.of(true,false),
				Arguments.of(false,true),
				Arguments.of(false,false)
		);
	}




	public void assert_period_equality(Period intsp, LocalDateTime lower, LocalDateTime upper, Boolean lower_inc, Boolean upper_inc){
		if (lower != null) {
			assertEquals(intsp.lower(), lower);
		}
		if (upper != null) {
			assertEquals(intsp.upper(), upper);
		}
		if (lower_inc != null) {
			assertEquals(intsp.lower_inc(), lower_inc.booleanValue());
		}
		if (upper_inc != null) {
			assertEquals(intsp.upper_inc(), upper_inc.booleanValue());
		}
	}


	@ParameterizedTest(name = "Test Constructor method")
	@MethodSource("period_constructor")
	public void testPeriodConstructor(String source, LocalDateTime lower, LocalDateTime upper, boolean lower_inc, boolean upper_inc) throws SQLException {
		functions.meos_initialize("UTC");
		Period p = new Period(source);
		assert_period_equality(p,lower,upper,lower_inc,upper_inc);
	}

	@ParameterizedTest(name = "Test Constructor method")
	@MethodSource("period_constructor2")
	public void testPeriodConstructor2(String lower, String upper, LocalDateTime lowerv, LocalDateTime upperv) throws SQLException {
		functions.meos_initialize("UTC");
		Period p = new Period(lower,upper);
		assert_period_equality(p,lowerv,upperv,true,false);
	}


	@ParameterizedTest(name = "Test Constructor method")
	@MethodSource("period_constructor3")
	public void testPeriodConstructor3(LocalDateTime lower, LocalDateTime upper, LocalDateTime lowerv, LocalDateTime upperv) throws SQLException {
		functions.meos_initialize("UTC");
		Period p = new Period(lower,upper);
		assert_period_equality(p,lowerv,upperv,true,false);
	}


	@ParameterizedTest(name = "Test Constructor method")
	@MethodSource("period_constructor4")
	public void testPeriodConstructor4(String lower, LocalDateTime upper, LocalDateTime lowerv, LocalDateTime upperv) throws SQLException {
		functions.meos_initialize("UTC");
		Period p = new Period(lower,upper);
		assert_period_equality(p,lowerv,upperv,true,false);
	}

	@Test
	public void testPeriodBounds() throws SQLException {
		functions.meos_initialize("UTC");
		Period p = new Period("2019-09-08 00:00:00+0", "2019-09-10 00:00:00+0");
		assert_period_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),true,false);
	}


	@ParameterizedTest(name="Test Bound Inclusivity")
	@MethodSource("period_constructor5")
	public void testPeriodIncluBounds(boolean lower,boolean upper) throws SQLException {
		functions.meos_initialize("UTC");
		Period p = new Period("2019-09-08 00:00:00+0", "2019-09-10 00:00:00+0",lower,upper);
		assert_period_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),lower,upper);
	}


	@Test
	public void testHexwkbConstructor() throws SQLException {
		functions.meos_initialize("UTC");
		Period p = Period.from_hexwkb("012100000040021FFE3402000000B15A26350200");
		assert_period_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),false,false);
	}

	@Test
	public void testFromAsConstructor() throws SQLException {
		functions.meos_initialize("UTC");
		assertNotEquals(this.period,new Period("(2019-09-08 00:00:00+00, 2019-09-10 00:00:00+00)"));
		//assert_period_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),false,false);
	}

	@Test
	public void testCopyConstructor() throws SQLException {
		functions.meos_initialize("UTC");
		Period other = this.period.copy();
		assertNotEquals(this.period, other);
		//assert_period_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),false,false);
	}

	@Test
	public void testPeriodOut() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.period.toString(), "(2019-09-08 00:00:00+00, 2019-09-10 00:00:00+00)");
		//assert_period_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),false,false);
	}


	@Test
	public void testToPeriodSet() throws SQLException {
		functions.meos_initialize("UTC");
		PeriodSet pset = this.period.to_periodset();
		assertTrue(pset instanceof PeriodSet);
		assertEquals(pset.num_periods(),1);
		assertEquals(pset.start_period().toString(),this.period.toString());
		//assert_period_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),false,false);
	}

	@Test
	public void testUpperAccessors() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.period.lower(), LocalDateTime.of(2019, 9, 8, 0, 0));
		assertEquals(this.period2.lower(), LocalDateTime.of(2019, 9, 8, 2, 3));
	}


	@Test
	public void testLowerAccessors() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.period.upper(), LocalDateTime.of(2019, 9, 10, 0, 0));
		assertEquals(this.period2.upper(), LocalDateTime.of(2019, 9, 10, 2, 3));
	}

	@Test
	public void testLowerIncAccessors() throws SQLException {
		functions.meos_initialize("UTC");
		assertFalse(this.period.lower_inc());
		assertTrue(this.period2.lower_inc());
	}

	@Test
	public void testUpperIncAccessors() throws SQLException {
		functions.meos_initialize("UTC");
		assertFalse(this.period.upper_inc());
		assertTrue(this.period2.upper_inc());
	}


	@Test
	public void testDurationInSeconds() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.period.duration_in_second(), 172800);
		assertEquals(this.period2.duration_in_second(), 172800);
	}


	@Test
	public void testHash() throws SQLException {
		functions.meos_initialize("UTC");
		assertEquals(this.period.hash(), 1164402929);
	}


}
