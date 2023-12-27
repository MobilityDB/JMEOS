package boxes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import types.boxes.TBox;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TBoxTest {
	/*
	static Stream<Arguments> notEqualsTBoxProvider() {
		return Stream.of(
				arguments("TBOX((30.0, 2021-07-08 06:04:32+02), (40.0, 2021-07-09 11:02:00+02))",
						"TBOX((50.0, 2021-07-08 06:04:32+02), (40.0, 2021-07-09 11:02:00+02))"),
				arguments("TBOX((30.0, 2021-07-08 06:04:32+02), (90.0, 2021-07-09 11:02:00+02))",
						"TBOX((30.0, 2021-07-08 06:04:32+02), (40.0, 2021-07-09 11:02:00+02))"),
				arguments("TBOX((60.0, 2021-07-10 06:04:32+02), (70.0, 2021-07-09 11:02:00+02))",
						"TBOX((60.0, 2021-07-08 06:04:32+02), (70.0, 2021-07-09 11:02:00+02))"),
				arguments("TBOX((80.0, 2021-07-08 06:04:32+02), (40.0, 2021-07-09 11:02:00+02))",
						"TBOX((80.0, 2021-07-08 06:04:32+02), (40.0, 2021-07-11 11:02:00+02))")
		);
	}
	
	@Test
	void testConstructor() throws SQLException {
		String value = "TBOX((50.0, 2021-07-08 06:04:32+02), (40.0, 2021-07-09 11:02:00+02))";
		ZoneOffset tz = ZoneOffset.of("+02:00");
		OffsetDateTime expectedTmin = OffsetDateTime.of(2021, 7, 8,
				6, 4, 32, 0, tz);
		OffsetDateTime expectedTmax = OffsetDateTime.of(2021, 7, 9,
				11, 2, 0, 0, tz);
		TBox tbox = new TBox(value);
		TBox other = new TBox(50.0, expectedTmin, 40.0, expectedTmax);
		
		assertAll("Constructor with all arguments",
				() -> assertEquals(expectedTmin, tbox.getTmin()),
				() -> assertEquals(expectedTmax, tbox.getTmax()),
				() -> assertEquals(other.getValue(), tbox.getValue()),
				() -> assertEquals(other, tbox)
		);
	}
	
	@Test
	void testConstructorOnlyX() throws SQLException {
		String value = "TBOX((3.0, ), (7.0, ))";
		TBox tbox = new TBox(value);
		TBox other = new TBox(3.0, 7.0);
		
		assertAll("Constructor with only x coordinates",
				() -> assertEquals(other.getXmin(), tbox.getXmin()),
				() -> assertEquals(other.getXmax(), tbox.getXmax()),
				() -> assertEquals(other.getValue(), tbox.getValue()),
				() -> assertEquals(other, tbox)
		);
	}
	
	@Test
	void testConstructorOnlyT() throws SQLException {
		String value = "TBOX((, 2021-07-08 06:04:32+02), (, 2021-07-09 11:02:00+02))";
		ZoneOffset tz = ZoneOffset.of("+02:00");
		OffsetDateTime expectedTmin = OffsetDateTime.of(2021, 7, 8,
				6, 4, 32, 0, tz);
		OffsetDateTime expectedTmax = OffsetDateTime.of(2021, 7, 9,
				11, 2, 0, 0, tz);
		TBox tbox = new TBox(value);
		TBox other = new TBox(expectedTmin, expectedTmax);
		
		assertAll("Constructor with only times",
				() -> assertEquals(expectedTmin, tbox.getTmin()),
				() -> assertEquals(expectedTmax, tbox.getTmax()),
				() -> assertEquals(other.getValue(), tbox.getValue()),
				() -> assertEquals(other, tbox)
		);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"(20, 2021-09-08 00:00:00+01, 2021-09-08 00:00:00+01)",
			"(20, 2021-09-08 00:00:00+01, 40)",
			"( 2021-09-08 00:00:00+01, 60, 2021-09-08 00:00:00+01)",
	})
	void testSetValueInvalidValue(String value) {
		TBox tBox = new TBox();
		SQLException thrown = assertThrows(
				SQLException.class,
				() -> tBox.setValue(value)
		);
		assertTrue(thrown.getMessage().contains("Could not parse TBox value, invalid number of arguments."));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"((10.0, 2019-09-08 00:00:00+02), (, 2019-09-10 00:00:00+02))",
			"TBOX((10.0, 2019-04-18 00:03:00+02), (, 2019-04-20 00:04:00+02))",
			"((10.0,), (,))",
			"TBOX((8.0,), (,))"
	})
	void testSetValueMissingXmax(String value) {
		TBox tBox = new TBox();
		SQLException thrown = assertThrows(
				SQLException.class,
				() -> tBox.setValue(value)
		);
		assertTrue(thrown.getMessage().contains("Xmax should have a value."));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"((, 2019-09-08 00:00:00+02), (32.0, 2019-09-10 00:00:00+02))",
			"TBOX((, 2019-09-08 00:00:00+02), (15, 2019-09-10 00:00:00+02))",
			"((,), (33.0, ))",
			"TBOX((,), (87, ))"
	})
	void testSetValueMissingXmin(String value) {
		TBox tBox = new TBox();
		SQLException thrown = assertThrows(
				SQLException.class,
				() -> tBox.setValue(value)
		);
		assertTrue(thrown.getMessage().contains("Xmin should have a value."));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"((10.0, 2019-09-08 00:00:00+02), (22.0, ))",
			"TBOX((10.0, 2019-04-18 00:03:00+02), (22.0, ))",
			"((10.0,2019-04-18 00:11:02+02), (18,))",
			"TBOX((8.0,2019-04-19 00:03:15+02), (28,))"
	})
	void testSetValueMissingTmax(String value) {
		TBox tBox = new TBox();
		SQLException thrown = assertThrows(
				SQLException.class,
				() -> tBox.setValue(value)
		);
		assertTrue(thrown.getMessage().contains("Tmax should have a value."));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"((10.0,), (22.0, 2019-09-08 00:00:00+02))",
			"TBOX((10.0,), (22.0, 2019-04-18 00:03:00+02))",
			"((10.0,), (18, 2019-04-18 00:11:02+02))",
			"TBOX((8.0,), (28,2019-04-19 00:03:15+02))"
	})
	void testSetValueMissingTmin(String value) {
		TBox tBox = new TBox();
		SQLException thrown = assertThrows(
				SQLException.class,
				() -> tBox.setValue(value)
		);
		assertTrue(thrown.getMessage().contains("Tmin should have a value."));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"TBOX((, -09-08 00:00:00+01), (, 2021-07-09 11:02:00+02))",
			"TBOX((4, 2021-07-09 11:02:00+02), (9, 2021-09-08))",
			"((4, 2021-07-0912:02:00+02), (9, 2021-07-09 12:09:08+02))"
	})
	void testSetValueInvalidDateFormat(String value) {
		TBox tBox = new TBox();
		DateTimeParseException thrown = assertThrows(
				DateTimeParseException.class,
				() -> tBox.setValue(value)
		);
		assertTrue(thrown.getMessage().contains("could not be parsed"));
	}
	
	@Test
	void testEmptyEquals() {
		TBox tBoxA = new TBox();
		TBox tBoxB = new TBox();
		assertEquals(tBoxA, tBoxB);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"TBOX((30.0, 2021-07-13 06:04:32+02), (98.0, 2021-07-16 11:02:00+02))",
			"TBOX((3.0, ), (7.0, ))",
			"TBOX((, 2021-07-11 08:04:32+02), (, 2021-07-12 11:02:00+02))"
	})
	void testEquals(String value) throws SQLException {
		TBox tBoxA = new TBox(value);
		TBox tBoxB = new TBox(value);
		assertEquals(tBoxA, tBoxB);
	}
	
	@Test
	void testEqualsOnlyX() {
		TBox tBoxA = new TBox(3.0, 7.0);
		TBox tBoxB = new TBox(3.0, 7.0);
		
		assertAll("Equals on x coordinate",
				() -> assertEquals(tBoxA.getXmin(), tBoxB.getXmin()),
				() -> assertEquals(tBoxA.getXmax(), tBoxB.getXmax()),
				() -> assertEquals(tBoxA.getValue(), tBoxB.getValue())
		);
	}
	
	@Test
	void testEqualsOnlyT() {
		ZoneOffset tz = ZoneOffset.of("+02:00");
		OffsetDateTime tmin = OffsetDateTime.of(2021, 7, 8,
				6, 4, 32, 0, tz);
		OffsetDateTime tmax = OffsetDateTime.of(2021, 7, 9,
				11, 2, 0, 0, tz);
		TBox tBoxA = new TBox(tmin, tmax);
		TBox tBoxB = new TBox(tmin, tmax);
		
		assertAll("Equals on time dimension",
				() -> assertEquals(tBoxA.getTmin(), tBoxB.getTmin()),
				() -> assertEquals(tBoxA.getTmax(), tBoxB.getTmax()),
				() -> assertEquals(tBoxA.getValue(), tBoxB.getValue()),
				() -> assertEquals(tBoxA, tBoxB)
		);
	}
	
	@Test
	void testNullEquals() {
		TBox tBox = new TBox();
		assertNull(tBox.getValue());
	}
	
	@Test
	void testNotEqualsDifferentType() throws SQLException {
		String value = "TBOX((30.0, 2021-07-13 06:04:32+02), (98.0, 2021-07-16 11:02:00+02))";
		TBox tBox = new TBox(value);
		assertNotEquals(tBox, new Object());
	}
	
	@ParameterizedTest
	@MethodSource("notEqualsTBoxProvider")
	void testNotEquals(String first, String second) throws SQLException {
		TBox tBoxA = new TBox(first);
		TBox tBoxB = new TBox(second);
		assertNotEquals(tBoxA, tBoxB);
	}

	 */
}
