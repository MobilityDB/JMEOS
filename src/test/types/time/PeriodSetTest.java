package types.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PeriodSetTest {
	static Stream<Arguments> periodSetTimezoneProvider() {
		return Stream.of(
				arguments("{[2021-09-08 00:00:00+01, 2021-09-10 00:00:00+01]}",
						"{[2021-09-08 01:00:00+02, 2021-09-10 02:00:00+03]}"),
				arguments("{[2021-09-08 00:00:00+01, 2021-09-10 00:00:00+01)}",
						"{[2021-09-08 03:00:00+04, 2021-09-10 04:00:00+05)}"),
				arguments("{(2021-09-08 00:00:00+01, 2021-09-10 00:00:00+01]}",
						"{(2021-09-07 22:00:00-01, 2021-09-09 21:00:00-02]}"),
				arguments("{(2021-09-08 00:00:00+01, 2021-09-10 00:00:00+01)}",
						"{(2021-09-07 20:00:00-03, 2021-09-09 19:00:00-04)}")
		);
	}
	
	static Stream<Arguments> periodSetNotEqualsProvider() {
		return Stream.of(
				arguments("{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}",
						"{[2019-09-07 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}"),
				arguments("{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}",
						"{[2019-09-07 00:00:00+01, 2019-09-10 00:00:00+01]}"),
				arguments("{(2021-09-08 00:00:00+01, 2021-09-10 01:00:00+01]}",
						"{(2021-09-07 22:00:00-01, 2021-09-09 21:00:00-02]}")
		);
	}
	
	@Test
	void testEquals() throws SQLException {
		String value = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}";
		PeriodSet periodSetA = new PeriodSet(value);
		PeriodSet periodSetB = new PeriodSet(value);
		assertEquals(periodSetA, periodSetB);
	}
	
	@ParameterizedTest
	@MethodSource("periodSetTimezoneProvider")
	void testEqualsTimeZone(String first, String second) throws SQLException {
		PeriodSet periodSetA = new PeriodSet(first);
		PeriodSet periodSetB = new PeriodSet(second);
		assertEquals(periodSetA, periodSetB);
	}
	
	@ParameterizedTest
	@MethodSource("periodSetNotEqualsProvider")
	void testNotEquals(String first, String second) throws SQLException {
		PeriodSet periodSetA = new PeriodSet(first);
		PeriodSet periodSetB = new PeriodSet(second);
		assertNotEquals(periodSetA, periodSetB);
	}
	
	@Test
	void testNotEqualsTimeZone() throws SQLException {
		String valueA = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}";
		String valueB = "{[2019-09-08 00:00:00+02, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}";
		PeriodSet periodSetA = new PeriodSet(valueA);
		PeriodSet periodSetB = new PeriodSet(valueB);
		assertNotEquals(periodSetA, periodSetB);
	}
	
	@Test
	void testNotEqualsDifferentType() throws SQLException {
		String value = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}";
		PeriodSet periodSet = new PeriodSet(value);
		assertNotEquals(periodSet, new Object());
	}
	
	
	@Test
	void testEmptyEquals() {
		PeriodSet periodSetA = new PeriodSet();
		PeriodSet periodSetB = new PeriodSet();
		assertEquals(periodSetA, periodSetB);
	}
	
	@Test
	void testGetValue() throws SQLException {
		String value = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}";
		PeriodSet periodSetA = new PeriodSet(value);
		PeriodSet periodSetB = new PeriodSet(periodSetA.getValue());
		assertEquals(periodSetA.getValue(), periodSetB.getValue());
	}
	
	@Test
	void testPeriodListConstructor() throws SQLException {
		String expectedValue = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}";
		PeriodSet periodSet = new PeriodSet(
				new Period("[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01]"),
				new Period("[2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]")
		);
		assertEquals(expectedValue, periodSet.getValue());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"[2019-09-08 00:00:00+01, 2019-09-12 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]",
			"{[2019-09-08 00:00:00+01, 2019-09-12 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]",
			"[2019-09-08 00:00:00+01, 2019-09-12 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}"
	})
	void testConstructorInvalidValue(String value) {
		SQLException thrown = assertThrows(
				SQLException.class,
				() -> new PeriodSet(value)
		);
		assertEquals("Could not parse period set value.", thrown.getMessage());
	}
	
	@Test
	void testPeriodWithoutValue() throws SQLException {
		Period[][] tests = {
				new Period[]{new Period()},
				new Period[]{null},
				new Period[]{new Period("[2019-09-08 00:00:00+01, 2019-09-12 00:00:00+01]"), new Period()}
		};
		
		for (Period[] periods : tests) {
			SQLException thrown = assertThrows(
					SQLException.class,
					() -> new PeriodSet(periods)
			);
			assertEquals("All periods should have a value.", thrown.getMessage());
		}
	}
	
	@Test
	void testPeriodSetEmpty() {
		SQLException thrown = assertThrows(
				SQLException.class,
				() -> new PeriodSet("{}")
		);
		assertEquals("Period set must contain at least one element.", thrown.getMessage());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"{[2019-09-08 00:00:00+01, 2019-09-12 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}",
			"{[2019-09-08 00:00:00+01, 2019-09-11 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}"
	})
	void testInvalidPeriods(String value) {
		SQLException thrown = assertThrows(
				SQLException.class,
				() -> new PeriodSet(value)
		);
		assertEquals("The periods of a period set cannot overlap.", thrown.getMessage());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"{[2019-09-08 00:00:00+01, 2019-09-11 00:00:00+01], (2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}",
			"{[2019-09-08 00:00:00+01, 2019-09-11 00:00:00+01), [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}"
	})
	void testValidPeriods(String value) throws SQLException {
		PeriodSet periodSet = new PeriodSet(value);
		assertEquals(value, periodSet.getValue());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"{[2019-09-08 00:00:00+01, 2019-09-11 00:00:00+01], (2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}",
			"{[2019-09-08 00:00:00+01, 2019-09-11 00:00:00+01), [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}",
			"{[2019-09-08 00:00:00+01, 2019-09-11 00:00:00+01]}",
			"{[2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}"
	})
	void testsGetPeriods(String value) throws SQLException {
		PeriodSet periodSet = new PeriodSet(value);
		Period[] periods = periodSet.periods();
		assertEquals(periodSet.numPeriods(), periods.length);
		assertEquals(periodSet.startPeriod(), periods[0]);
		assertEquals(periodSet.endPeriod(), periods[periods.length - 1]);
		assertEquals(periodSet.periodN(0), periods[0]);
	}
	
	@Test
	void testEmptyPeriodSet() throws SQLException {
		PeriodSet periodSet = new PeriodSet();
		assertEquals(Duration.ZERO, periodSet.duration());
		assertEquals(Duration.ZERO, periodSet.timespan());
		assertNull(periodSet.period());
		assertNull(periodSet.startTimestamp());
		assertNull(periodSet.endTimestamp());
		assertNull(periodSet.startPeriod());
		assertNull(periodSet.endPeriod());
	}
	
	@Test
	void testDuration() throws SQLException {
		Period periodA = new Period("[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01]");
		Period periodB = new Period("[2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]");
		PeriodSet periodSet = new PeriodSet(periodA, periodB);
		assertEquals(Duration.ofDays(3), periodSet.duration());
		assertEquals(periodA.duration().plus(periodB.duration()), periodSet.duration());
		assertEquals(Duration.ofDays(4), periodSet.timespan());
	}
	
	@Test
	void testTimespan() throws SQLException {
		String value = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01]}";
		PeriodSet periodSet = new PeriodSet(value);
		assertEquals(Duration.ofDays(4), periodSet.timespan());
	}
	
	@Test
	void testGetPeriod() throws SQLException {
		Period expected = new Period("[2019-09-08 00:00:00+01, 2019-09-12 00:00:00+01)");
		String value = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01)}";
		PeriodSet periodSet = new PeriodSet(value);
		assertEquals(expected, periodSet.period());
	}
	
	@Test
	void testGetTimestamps() throws SQLException {
		String value = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01), [2019-09-10 00:00:00+01, 2019-09-12 00:00:00+01)}";
		PeriodSet periodSet = new PeriodSet(value);
		OffsetDateTime[] timestamps = periodSet.timestamps();
		assertEquals(3, timestamps.length);
		assertEquals(periodSet.numTimestamps(), timestamps.length);
		assertEquals(periodSet.startTimestamp(), timestamps[0]);
		assertEquals(periodSet.timestampN(1), timestamps[1]);
		assertEquals(periodSet.endTimestamp(), timestamps[2]);
	}
	
	@Test
	void testShiftPeriod() throws SQLException {
		String expectedValue = "{[2019-09-09 00:00:00+01, 2019-09-11 00:00:00+01], [2019-09-12 00:00:00+01, 2019-09-13 00:00:00+01)}";
		PeriodSet expectedPeriodSet = new PeriodSet(expectedValue);
		String value = "{[2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01], [2019-09-11 00:00:00+01, 2019-09-12 00:00:00+01)}";
		PeriodSet periodSet = new PeriodSet(value);
		assertEquals(expectedPeriodSet, periodSet.shift(Duration.ofDays(1)));
	}
}
