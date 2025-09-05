package collections.time;

import functions.functions;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.locationtech.jts.io.ParseException;
import types.collections.base.Span;
import types.collections.time.*;
//import types.collections.time.DateSet;
import java.util.Arrays;
import java.util.List;

import functions.*;

import static org.junit.jupiter.api.Assertions.*;

class datespanTest {
    private final datespan dspan;
    private final datespan dspan2;

    datespanTest() throws SQLException {
        functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
        dspan = new datespan("[2019-09-25, 2019-09-27]");
        dspan2 = new datespan("[2019-09-08, 2019-09-10)");
    }

    static error_handler_fn errorHandler = new error_handler();

    public void assert_datespan_equality(dateset vset, List<LocalDate> timestamps){
        assertEquals(vset.num_elements(), timestamps.size());
    }

    @Test
    public void testStringConstructor(){
        System.out.println(dspan.toString());
        assertEquals("[2019-09-25, 2019-09-28)", dspan.toString());
    }


    @Test
    public void testFromAsConstructor() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals(dspan.toString(), new datespan(dspan.toString()).toString());
        String wkb= dspan.from_wkb(dspan.as_wkb(), dspan.toString().length(), datespan.class).toString();
        System.out.println(wkb);
        String hexwkb= Span.from_hexwkb(dspan.as_hexwkb(), datespan.class).toString();
        System.out.println(hexwkb);
        assertEquals(dspan.toString(), wkb);
        assertEquals(dspan.toString(), hexwkb);
    }

    // Collection Conversion Tests
    @Test
    public void testToSpanSet() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespanset expectedSpan = new datespanset("{[2019-09-25, 2019-09-28)}");
        System.out.println(dspan.to_spanset(datespanset.class).toString());
        assertEquals(expectedSpan.toString(), dspan.to_spanset(datespanset.class).toString());
    }

    @Test
    public void testTstzspan() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        tstzspan expectedSpan = new tstzspan("[2019-09-25 00:00:00+00, 2019-09-28 00:00:00+00)");
        System.out.println(dspan.to_tstzspan().toString());
        assertEquals(expectedSpan.toString(), dspan.to_tstzspan().toString());
    }

    // Accessor Tests
    @Test
    public void testDuration() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Duration expectedDuration = Duration.ofDays(3);  // Assuming duration counts days between the start and end date.
        assertEquals(expectedDuration, dspan.duration());
    }

    @Test
    public void testStartElement() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals(LocalDate.of(2019, 9, 25), dspan.start_element());
    }

    @Test
    public void testEndElement() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals(LocalDate.of(2019, 9, 28), dspan.end_element());
    }

    // Transformation tests
    @Test
    public void testScale() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespan expected_datespan = new datespan("[2019-09-25, 2019-10-11)");
        System.out.println(dspan.scale(15).toString());
        assertEquals(expected_datespan.toString(), dspan.scale(15).toString());
    }

    @Test
    public void testShift() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespan expected_datespan = new datespan("[2019-10-10, 2019-10-13)");
        System.out.println(dspan.shift(15).toString());
        assertEquals(expected_datespan.toString(), dspan.shift(15).toString());
    }

    @Test
    public void testShiftScale() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespan expected_datespan = new datespan("[2019-10-10, 2019-10-26)");
        System.out.println(dspan.shift_scale(15, 15).toString());
        assertEquals(expected_datespan.toString(), dspan.shift_scale(15, 15).toString());
    }


    // Position Functions Tests
    @ParameterizedTest
    @CsvSource({
            "'[2020-01-01, 2020-01-31)', false"
    })
    public void testIsContainedIn(String otherDateSetStr, boolean expected) throws Exception {
        datespan otherDateSpan = new datespan(otherDateSetStr);
        System.out.println(otherDateSpan.toString());
        assertEquals(expected, dspan.is_contained_in(otherDateSpan));
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-26, true",
            "'[2020-01-01, 2020-01-31]', false"
    })
    public void testContains(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            System.out.println(other);
            if (((String) other).length() <= 10) {
                LocalDate date = LocalDate.parse((CharSequence) other);
                assertEquals(expected, dspan.contains(date));
            } else {
                datespan otherDateSpan = new datespan(String.valueOf(other));
                assertEquals(expected, dspan.contains(otherDateSpan));
            }
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dspan.contains(other));
        }
    }

    @Test
    public void testAdjacent() throws Exception {
        String otherDateSpanStr= "[2019-09-28, 2020-01-31]";
        datespan otherDateSpan= new datespan(otherDateSpanStr);
        assertTrue(dspan.is_adjacent(otherDateSpan));
    }

    @Test
    public void testOverlaps() throws Exception {
        String otherDateSpanStr= "[2019-09-26, 2020-01-31]";
        datespan otherDateSpan= new datespan(otherDateSpanStr);
        assertTrue(dspan.overlaps(otherDateSpan));
    }

    @ParameterizedTest
    @CsvSource({
            "'[2018-01-01, 2018-01-31]', false",
            "'[2020-01-01, 2020-01-31]', true"
    })
    public void testIsBefore(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            datespan otherDateSpan = new datespan(String.valueOf(other));
            assertEquals(expected, dspan.is_before(otherDateSpan));
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dspan.is_before(other));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'[2018-01-01, 2018-01-31]', false",
            "'[2020-01-01, 2020-01-31]', true"
    })
    public void testIsOverOrBefore(Object other, boolean expected) throws Exception {
        if (other instanceof LocalDate) {
            assertEquals(expected, dspan.is_over_or_before((LocalDate) other));
        } else if (other instanceof String) {
            datespan otherDateSet = new datespan(String.valueOf(other));
            assertEquals(expected, dspan.is_over_or_before(otherDateSet));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'[2018-01-01, 2018-01-31]', true",
            "'[2020-01-01, 2020-01-31]', false"
    })
    public void testIsAfter(Object other, boolean expected) throws Exception {
        if (other instanceof LocalDate) {
            assertEquals(expected, dspan.is_after((LocalDate) other));
        } else if (other instanceof String) {
            datespan otherDateSet = new datespan(String.valueOf(other));
            assertEquals(expected, dspan.is_after(otherDateSet));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'[2018-01-01, 2018-01-31]', true",
            "'[2020-01-01, 2020-01-31]', false"
    })
    public void testIsOverOrAfter(Object other, boolean expected) throws Exception {
        if (other instanceof LocalDate) {
            assertEquals(expected, dspan.is_over_or_after((LocalDate) other));
        } else if (other instanceof String) {
            datespan otherDateSet = new datespan(String.valueOf(other));
            assertEquals(expected, dspan.is_over_or_after(otherDateSet));
        }
    }

    @Test
    public void testDistance() throws Exception {
        datespan datespan1 = new datespan("[2019-09-25, 2019-09-29]");
        datespanset datespanset1 = new datespanset("{(2019-01-01, 2019-04-28), (2019-07-05, 2019-12-31)}");
        dateset dateset1 = new dateset("{2019-09-25, 2019-09-26}");
        LocalDate dateValue = LocalDate.of(2019, 9, 26);

        System.out.println(dspan.distance(datespan1));
        dspan.distance(datespan1);
        System.out.println(dspan.distance(datespanset1));
        dspan.distance(datespanset1);
        System.out.println(dspan.distance(dateset1));
        dspan.distance(dateset1);
        System.out.println(dspan.distance(dateValue));
        dspan.distance(dateValue);
    }

    // Set Functions Tests
    @Test
    public void testIntersection() throws Exception {
        datespan datespan1 = new datespan("[2019-09-25, 2019-09-29]");
        datespanset datespanset1 = new datespanset("{(2019-01-01, 2019-04-28), (2019-07-05, 2019-12-31)}");
        dateset dateset1 = new dateset("{2019-09-25, 2019-09-26}");
        LocalDate dateValue = LocalDate.of(2019, 9, 25);

        System.out.println(dspan.intersection(datespan1).toString());
        dspan.intersection(datespan1);
        System.out.println(dspan.intersection(datespanset1).toString());
        dspan.intersection(datespanset1);
        System.out.println(dspan.intersection(dateset1).toString());
        dspan.intersection(dateset1);
        System.out.println(dspan.intersection(dateValue).toString());
        dspan.intersection(dateValue);
    }

    @Test
    public void testUnion() throws Exception {
        datespan datespan = new datespan("(2020-01-01, 2020-01-31)");
        datespanset datespanset = new datespanset("{(2020-01-01, 2020-01-31), (2021-01-01, 2021-01-31)}");
        dateset dateset = new dateset("{2020-01-01, 2020-01-31}");
        LocalDate dateValue = LocalDate.of(2020, 1, 1);

        dspan.union(datespan);
        dspan.union(datespanset);
        dspan.union(dateset);
        dspan.union(dateValue);
    }

    @Test
    public void testMinus() throws Exception {
        datespan datespan = new datespan("(2020-01-01, 2020-01-31)");
        datespanset datespanset = new datespanset("{(2020-01-01, 2020-01-31), (2021-01-01, 2021-01-31)}");
        dateset dateset = new dateset("{2020-01-01, 2020-01-31}");
        LocalDate dateValue = LocalDate.of(2020, 1, 1);

        System.out.println(dspan.minus(datespan));
        dspan.minus(datespan);
        System.out.println(dspan.minus(datespanset));
        dspan.minus(datespanset);
        System.out.println(dspan.minus(dateset));
        dspan.minus(dateset);
        System.out.println(dspan.minus(dateValue));
        dspan.minus(dateValue);
    }

    // Comparison Functions Tests
    @Test
    public void testCompareFunctions() throws Exception {
        datespan otherSet = new datespan("(2020-01-01, 2020-01-31)");

        assertTrue(dspan.is_before(otherSet));
        assertTrue(dspan.is_over_or_before(otherSet));
        assertFalse(dspan.is_over_or_after(otherSet));
        assertFalse(dspan.is_after(otherSet));
    }
}
