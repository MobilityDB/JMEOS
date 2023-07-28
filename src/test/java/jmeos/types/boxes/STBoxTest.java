package jmeos.types.boxes;

import jmeos.types.boxes.Point;
import jmeos.types.boxes.STBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class STBoxTest {

    static Stream<Arguments> stboxInvalidTimeProvider() {
        return Stream.of(
            arguments(null, null, null, OffsetDateTime.now(), null, null, null, null, 0,  false),
            arguments(null, null, null, null, null, null, null, OffsetDateTime.now(), 0,  false)
        );
    }

    static Stream<Arguments> stboxInvalidXYCoordinatesProvider() {
        return Stream.of(
            arguments(null, 1.0, null, OffsetDateTime.now(), null, null, null, OffsetDateTime.now(), 0,  false),
            arguments(1.0, 2.0, null, OffsetDateTime.now(), 3.0, null, null, OffsetDateTime.now(), 0,  false),
            arguments(null, 1.0, null, null, null, null, null, null, 1234,  false),
            arguments(1.0, 2.0, null, null, 3.0, null, null, null, 3456,  false),
            arguments(null, 1.0, 8.0, OffsetDateTime.now(), 7.0, 1.0, null, OffsetDateTime.now(), 5643,  true),
            arguments(1.0, 2.0, null, OffsetDateTime.now(), null, 6.0, 35.0, OffsetDateTime.now(), 2446,  true),
            arguments(null, 12.0, 27.0, null, 9.2, 13.6, null, null, 8654,  true),
            arguments(1.0, 2.0, null, null, 3.0, null, 10.0, null, 4566,  true)
        );
    }

    static Stream<Arguments> stboxInvalidZCoordinateProvider() {
        return Stream.of(
            arguments(5.0, 1.0, 8.0, OffsetDateTime.now(), 7.0, 1.0, null, OffsetDateTime.now(), 0,  false),
            arguments(1.0, 2.0, null, OffsetDateTime.now(), 3.0, 6.0, 35.0, OffsetDateTime.now(), 0,  false),
            arguments(9.0, 12.0, 27.0, null, 9.2, 13.6, null, null, 0,  false),
            arguments(1.0, 2.0, null, null, 3.0, 8.0, 10.0, null, 0,  false)
        );
    }

    @Test
    void testConstructorTime() throws SQLException {
        String value = "STBOX T(, 2001-01-03 00:00:00+03), (, 2001-01-03 00:00:00+03))";
        ZoneOffset tz = ZoneOffset.of("+03:00");
        OffsetDateTime ttmin = OffsetDateTime.of(2001,1, 3,
                0, 0, 0, 0, tz);
        OffsetDateTime ttmax = OffsetDateTime.of(2001, 1, 3,
                0, 0, 0, 0, tz);
        STBox stbox = new STBox(value);
        STBox other = new STBox(ttmin,ttmax,null);

        assertEquals(other, stbox);
    }

    @Test
    void testConstructorXYandTime() throws SQLException {
        String value = "SRID=5676;STBOX T((1.0, 2.0, 2001-01-04 00:00:00+01), (1.0, 2.0, 2001-01-04 00:00:00+01))";
        ZoneOffset tz = ZoneOffset.of("+01:00");
        OffsetDateTime ttmin = OffsetDateTime.of(2001,1, 4,
                0, 0, 0, 0, tz);
        OffsetDateTime ttmax = OffsetDateTime.of(2001, 1, 4,
                0, 0, 0, 0, tz);
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(1.0,2.0),ttmin, new Point(1.0,2.0), ttmax, 5676,null);

        assertAll("Constructor with XY coordinates and time dimension",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorXY() throws SQLException {
        String value = "STBOX ((1.0, 2.0), (1.0, 2.0))";
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(1.0,2.0), new Point(1.0,2.0), null);

        assertAll("Constructor with XY coordinates",
            () -> assertEquals(other.getValue(), stbox.getValue()),
            () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorXYZ() throws SQLException {
        String value = "STBOX Z((1.0, 2.0, 3.0), (1.0, 2.0, 3.0))";
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(1.0,2.0,3.0), new Point(1.0,2.0,3.0),null);

        assertAll("Constructor with XYZ coordinates",
            () -> assertEquals(other.getValue(), stbox.getValue()),
            () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorXYZAndTimeDimension() throws SQLException {
        String value = "STBOX ZT((1.0, 2.0, 3.0, 2001-01-04 00:00:00+01), (1.0, 2.0, 3.0, 2001-01-04 00:00:00+01))";
        ZoneOffset tz = ZoneOffset.of("+01:00");
        OffsetDateTime ttmin = OffsetDateTime.of(2001,1, 4,
                0, 0, 0, 0, tz);
        OffsetDateTime ttmax = OffsetDateTime.of(2001, 1, 4,
                0, 0, 0, 0, tz);
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(1.0,2.0,3.0), ttmin, new Point(1.0,2.0,3.0), ttmax,null);

        assertAll("Constructor with XY coordinates and time dimension",
            () -> assertEquals(other.getValue(), stbox.getValue()),
            () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorTimeGeodetic() throws SQLException {
        String value = "GEODSTBOX T((, 2021-01-03 00:00:00+01), (, 2021-01-03 00:00:00+01))";
        ZoneOffset tz = ZoneOffset.of("+01:00");
        OffsetDateTime ttmin = OffsetDateTime.of(2021,1, 3,
                0, 0, 0, 0, tz);
        OffsetDateTime ttmax = OffsetDateTime.of(2021, 1, 3,
                0, 0, 0, 0, tz);
        STBox stbox = new STBox(value);
        STBox other = new STBox(ttmin,ttmax, true,null);

        assertAll("Constructor with time dimension and geodetic",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorXYSRID() throws SQLException {
        String value = "SRID=4326;STBOX((11.0, 12.0), (11.0, 12.0))";
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(11.0,12.0), new Point(11.0,12.0), 4326,null);

        assertAll("Constructor with XY coordinates and SRID",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorXYZGeodeticSRID() throws SQLException {
        String value = "SRID=4326;GEODSTBOX((11.0, 12.0, 13.0), (11.0, 12.0, 13.0))";
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(11.0,12.0,13.0), new Point(11.0,12.0,13.0),
                true, 4326,null);

        assertAll("Constructor with XYZ coordinates and geodetic",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorXYZGeodetic() throws SQLException {
        String value = "GEODSTBOX((11.0, 12.0, 13.0), (11.0, 12.0, 13.0))";
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(11.0,12.0,13.0), new Point(11.0,12.0,13.0),
                true,null);

        assertAll("Constructor with XYZ coordinates and geodetic",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorXYZAndTimeDimensionGeodetic() throws SQLException {
        String value = "GEODSTBOX T((1.0, 2.0, 3.0, 2001-01-03 00:00:00+01), (1.0, 2.0, 3.0, 2001-01-04 00:00:00+01))";
        ZoneOffset tz = ZoneOffset.of("+01:00");
        OffsetDateTime ttmin = OffsetDateTime.of(2001,1, 3,
                0, 0, 0, 0, tz);
        OffsetDateTime ttmax = OffsetDateTime.of(2001, 1, 4,
                0, 0, 0, 0, tz);
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(1.0,2.0,3.0), ttmin, new Point(1.0,2.0,3.0), ttmax,
                true,null);

        assertAll("Constructor with XY coordinates and time dimension and geodetic",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorXYZAndTimeDimensionGeodeticSRID() throws SQLException {
        String value = "SRID=4326;GEODSTBOX T((1.0, 2.0, 3.0, 2001-01-03 00:00:00+01), (1.0, 2.0, 3.0, 2001-01-04 00:00:00+01))";
        ZoneOffset tz = ZoneOffset.of("+01:00");
        OffsetDateTime ttmin = OffsetDateTime.of(2001,1, 3,
                0, 0, 0, 0, tz);
        OffsetDateTime ttmax = OffsetDateTime.of(2001, 1, 4,
                0, 0, 0, 0, tz);
        STBox stbox = new STBox(value);
        STBox other = new STBox(new Point(1.0,2.0,3.0), ttmin, new Point(1.0,2.0,3.0), ttmax,
                true,4326,null);

        assertAll("Constructor with XY coordinates and time dimension and geodetic",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorTimeSRID() throws SQLException {
        String value = "SRID=5676;STBOX T(, 2001-01-03 00:00:00+03), (, 2001-01-03 00:00:00+03))";
        ZoneOffset tz = ZoneOffset.of("+03:00");
        OffsetDateTime ttmin = OffsetDateTime.of(2001,1, 3,
                0, 0, 0, 0, tz);
        OffsetDateTime ttmax = OffsetDateTime.of(2001, 1, 3,
                0, 0, 0, 0, tz);
        STBox stbox = new STBox(value);
        STBox other = new STBox(ttmin,ttmax,5676,null);

        assertAll("Constructor with time dimension and SRID",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testConstructorTimeGeodeticSRID() throws SQLException {
        String value = "SRID=5676;GEODSTBOX T(, 2001-01-03 00:00:00+03), (, 2001-01-03 00:00:00+03))";
        ZoneOffset tz = ZoneOffset.of("+03:00");
        OffsetDateTime ttmin = OffsetDateTime.of(2001,1, 3,
                0, 0, 0, 0, tz);
        OffsetDateTime ttmax = OffsetDateTime.of(2001, 1, 3,
                0, 0, 0, 0, tz);
        STBox stbox = new STBox(value);
        STBox other = new STBox(ttmin,ttmax, true,5676,null);

        assertAll("Constructor with time dimension and SRID",
                () -> assertEquals(other.getValue(), stbox.getValue()),
                () -> assertEquals(other, stbox)
        );
    }

    @Test
    void testEmptyEquals() {
        STBox stBoxA = new STBox();
        STBox stBoxB = new STBox();
        assertEquals(stBoxA, stBoxB);
        assertNull(stBoxA.getValue());
        assertNull(stBoxB.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "STBOX ((1.0, 2.0), (1.0, 2.0))",
        "STBOX Z((1.0, 2.0, 3.0), (1.0, 2.0, 3.0))",
        "STBOX T((1.0, 2.0, 2001-01-03 00:00:00+01), (1.0, 2.0, 2001-01-03 00:00:00+01))",
        "STBOX ZT((1.0, 2.0, 3.0, 2001-01-04 00:00:00+01), (1.0, 2.0, 3.0, 2001-01-04 00:00:00+01))",
        "STBOX T(, 2001-01-03 00:00:00+01), (, 2001-01-03 00:00:00+01))",
        "GEODSTBOX((11.0, 12.0, 13.0), (11.0, 12.0, 13.0))",
        "GEODSTBOX T((1.0, 2.0, 3.0, 2001-01-03 00:00:00+01), (1.0, 2.0, 3.0, 2001-01-04 00:00:00+01))",
        "GEODSTBOX T((, 2001-01-03 00:00:00+01), (, 2001-01-03 00:00:00+01))",
        "SRID=5676;STBOX T((1.0, 2.0, 2001-01-04 00:00:00+01), (1.0, 2.0, 2001-01-04 00:00:00+01))",
        "SRID=4326;GEODSTBOX((1.0, 2.0, 3.0), (1.0, 2.0, 3.0))"
    })
    void testStringConstructor(String value) throws SQLException {
        STBox stBoxA = new STBox(value);
        assertNotNull(stBoxA.getValue());
    }

    @ParameterizedTest
    @MethodSource("stboxInvalidTimeProvider")
    void testInvalidTime(Double xmin, Double ymin, Double zmin, OffsetDateTime tmin,
                         Double xmax, Double ymax, Double zmax, OffsetDateTime tmax,
                         int srid, boolean isGeodetic) {
        Throwable exceptionThrown = assertThrows(SQLException.class, () -> {
            new STBox(new Point(xmin, ymin, zmin), tmin, new Point(xmax, ymax, zmax), tmax, isGeodetic, srid,null);
        });
        assertTrue(exceptionThrown.getMessage().contains("Both tmin and tmax should have a value"));
    }

    @ParameterizedTest
    @MethodSource("stboxInvalidXYCoordinatesProvider")
    void testInvalidXYCoordinates(Double xmin, Double ymin, Double zmin, OffsetDateTime tmin,
                                  Double xmax, Double ymax, Double zmax, OffsetDateTime tmax,
                                  int srid, boolean isGeodetic) {
        Throwable exceptionThrown = assertThrows(SQLException.class, () -> {
            new STBox(new Point(xmin, ymin, zmin), tmin, new Point(xmax, ymax, zmax), tmax, isGeodetic, srid,null);
        });
        assertTrue(exceptionThrown.getMessage().contains("Both x and y coordinates should have a value"));
    }

    @ParameterizedTest
    @MethodSource("stboxInvalidZCoordinateProvider")
    void testInvalidZCoordinate(Double xmin, Double ymin, Double zmin, OffsetDateTime tmin,
                                  Double xmax, Double ymax, Double zmax, OffsetDateTime tmax,
                                  int srid, boolean isGeodetic) {
        Throwable exceptionThrown = assertThrows(SQLException.class, () -> {
            new STBox(new Point(xmin, ymin, zmin), tmin, new Point(xmax, ymax, zmax), tmax, isGeodetic, srid,null);
        });
        assertTrue(exceptionThrown.getMessage().contains("Both zmax and zmin should have a value"));
    }

    @Test
    void testInvalidArguments() {
        Throwable exceptionThrown = assertThrows(SQLException.class, () -> {
            new STBox(new Point(null, null, null), null, new Point(null, null, null), null,
                   0,null);
        });
        assertTrue(exceptionThrown.getMessage().contains("Could not parse STBox value, invalid number of arguments"));
    }

    @Test
    void testInvalidGeodetic() {
        Throwable exceptionThrown = assertThrows(SQLException.class, () -> {
            new STBox(new Point(1.0, 2.0), new Point(3.0, 4.0),true,null);
        });
        assertTrue(exceptionThrown.getMessage().contains("Geodetic coordinates need z value."));
    }

    @Test
    void testInvalidStringValue() {
        Throwable exceptionThrown = assertThrows(SQLException.class, () -> {
            new STBox("STBX ((1.0, 2.0), (1.0, 2.0))");
        });
        assertTrue(exceptionThrown.getMessage().contains("Could not parse STBox value"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "STBOX ((1.0, ), (1.0, 2.0))",
        "STBOX ((1.0, 3.0), (, 2.0))",
        "STBOX ((, 5.0), (1.0, 2.0))",
        "STBOX ((1.0, 3.0), (, 8.0))",
        "STBOX ((1.0, 3.0), (, 8.0))",
    })
    void testInvalidNumberOfParameters(String value) {
        Throwable exceptionThrown = assertThrows(SQLException.class, () -> {
            new STBox(value);
        });
        assertTrue(exceptionThrown.getMessage().contains("Could not parse STBox value, invalid number of parameters."));
    }

    @Test
    void testNotEqualsDifferentType() throws SQLException {
        String value = "STBOX ((1.0, 2.0), (1.0, 2.0))";
        STBox stBox = new STBox(value);
        assertNotEquals(stBox, new Object());
    }

    @Test
    void testNullGetters() throws SQLException {
        STBox stBox = new STBox("STBOX T(, 2001-01-03 00:00:00+01), (, 2001-01-03 00:00:00+01))");
        assertNull(stBox.getXmin());
        assertNull(stBox.getXmax());
        assertNull(stBox.getYmin());
        assertNull(stBox.getYmax());
        assertNull(stBox.getZmin());
        assertNull(stBox.getZmax());
    }

}
