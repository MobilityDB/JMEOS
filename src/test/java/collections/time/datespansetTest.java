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
import types.collections.base.SpanSet;
import types.collections.time.*;
//import types.collections.time.DateSet;
import java.util.Arrays;
import java.util.List;

import functions.*;

import static org.junit.jupiter.api.Assertions.*;

class datespansetTest {
    private final datespanset dsset;
    private final datespanset dsset2;

    datespansetTest() throws SQLException {
        functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
        dsset = new datespanset("{[2019-09-08, 2019-09-10], [2019-09-11, 2019-09-12]}");
        dsset2 = new datespanset("{[2020-09-08, 2020-09-10], [2020-09-11, 2020-09-12]}");
    }

    static error_handler_fn errorHandler = new error_handler();

    public void assert_datespanset_equality(dateset vset, List<LocalDate> timestamps){
        assertEquals(vset.num_elements(), timestamps.size());
    }

    @Test
    public void testStringConstructor(){
        System.out.println(dsset.toString());
        assertEquals("{[2019-09-08, 2019-09-13)}", dsset.toString());
    }

    @Test
    public void testStringListConstructor(){
        System.out.println(dsset.toString());
        List<String> dspanStringList= new ArrayList<String>();
        dspanStringList.add("[2019-09-08, 2019-09-10]");
        dspanStringList.add("[2019-09-11, 2019-09-12]");
        datespanset dssetList= new datespanset(dspanStringList);
        System.out.println(dssetList.toString());
        assertEquals(dsset.toString(), dssetList.toString());
    }

    @Test
    public void testDateSpanListConstructor(){
        System.out.println(dsset.toString());
        List<datespan> dspanList= new ArrayList<datespan>();
        dspanList.add(new datespan("[2019-09-08, 2019-09-10]"));
        dspanList.add(new datespan("[2019-09-11, 2019-09-12]"));
        datespanset dssetList= new datespanset(dspanList);
        System.out.println(dssetList.toString());
        assertEquals(dsset.toString(), dssetList.toString());
    }


    @Test
    public void testFromAsConstructor() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals(dsset.toString(), new datespanset(dsset.toString()).toString());
        String wkb= dsset.from_wkb(dsset.as_wkb(), dsset.toString().length(), datespanset.class).toString();
        System.out.println(wkb);
        String hexwkb= SpanSet.from_hexwkb(dsset.as_hexwkb(), datespanset.class).toString();
        System.out.println(hexwkb);
        assertEquals(dsset.toString(), wkb);
        assertEquals(dsset.toString(), hexwkb);
    }

    // Collection Conversion Tests
    @Test
    public void testToSpan() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespan expectedSpan = new datespan("[2019-09-08, 2019-09-13)");
        System.out.println(dsset.to_span().toString());
        assertEquals(expectedSpan.toString(), dsset.to_span().toString());
    }

    @Test
    public void testToTsTzSpanSet() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        tstzspanset expectedSpan = new tstzspanset("{[2019-09-08 00:00:00+00, 2019-09-13 00:00:00+00)}");
        System.out.println(dsset.to_tstzspanset().toString());
        assertEquals(expectedSpan.toString(), dsset.to_tstzspanset().toString());
    }

    // Accessor Tests
    @Test
    public void testDuration() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Duration expectedDuration = Duration.ofDays(5);  // Assuming duration counts days between the start and end date.
        assertEquals(expectedDuration, dsset.duration(true));
        assertEquals(expectedDuration, dsset.duration(false));
    }

    @Test
    public void testNumDates() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals(2, dsset.num_dates());
    }

    @Test
    public void testStartElement() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println(dsset.start_span().toString());
//        assertEquals(LocalDate.of(2019, 9, 28), dsset.start_span());
    }

    @Test
    public void testEndElement() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println(dsset.end_element().toString());
//        assertEquals(LocalDate.of(2019, 9, 28), dsset.end_element());
    }

    @Test
    public void testStartDate() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println(dsset.start_date().toString());
        assertEquals(LocalDate.of(2019, 9, 8), dsset.start_date());
    }

    @Test
    public void testEndDate() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println(dsset.end_date());
        assertEquals(LocalDate.of(2019, 9, 13), dsset.end_date());
    }


    // element retrieval test
    @Test
    public void testSpanN() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String expected = "[2019-09-08, 2019-09-13)";
        System.out.println(dsset.span_n(1).toString());
        assertEquals(expected, dsset.span_n(1).toString());
    }

    @Test
    public void testElements() throws Exception {
        dsset.elements();
    }

    // Transformation tests
//    @Test
//    public void testScale() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
////        datespan expected_datespan = new datespan("[2019-09-25, 2019-10-11)");
//        System.out.println(dsset.scale(5).toString());
////        assertEquals(expected_datespan.toString(), dsset.scale(15).toString());
//    }

    @Test
    public void testShift() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespanset expected_datespan = new datespanset("{[2019-09-23, 2019-09-28)}");
        System.out.println(dsset.shift(15).toString());
        assertEquals(expected_datespan.toString(), dsset.shift(15).toString());
    }

    @Test
    public void testShiftScale() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespanset expected_datespan = new datespanset("{[2019-09-23, 2019-10-09)}");
        System.out.println(dsset.shift_scale(15, 15).toString());
        assertEquals(expected_datespan.toString(), dsset.shift_scale(15, 15).toString());
    }


    // Position Functions Tests
    @ParameterizedTest
    @CsvSource({
            "'{[2019-03-23, 2019-05-09), [2019-09-23, 2019-10-09]}', false",
            "'{[2019-03-23, 2019-05-09), [2019-09-07, 2019-10-09]}', true"
    })
    public void testIsContainedIn(String otherDateSetStr, boolean expected) throws Exception {
        datespanset otherDateSpan = new datespanset(otherDateSetStr);
        System.out.println(otherDateSpan.toString());
        assertEquals(expected, dsset.is_contained_in(otherDateSpan));
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-26, false",
            "'[2020-01-01, 2020-01-31]', false",
            "'{[2019-03-23, 2019-05-09), [2019-09-07, 2019-10-09]}', false"
    })
    public void testContains(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            System.out.println(other);
            if (((String) other).length() <= 10) {
                LocalDate date = LocalDate.parse((CharSequence) other);
                assertEquals(expected, dsset.contains(date));
            }
            else if (((String) other).length() <=25){
                datespan otherDateSpan = new datespan(String.valueOf(other));
                assertEquals(expected, dsset.contains(otherDateSpan));
            }
            else {
                datespanset otherDateSpan = new datespanset(String.valueOf(other));
                assertEquals(expected, dsset.contains(otherDateSpan));
            }
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dsset.contains(other));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-13, true",
            "'[2020-01-01, 2020-01-31]', true",
            "'{[2019-03-23, 2019-05-09), [2019-09-07, 2019-10-09]}', false"
    })
    public void testAdjacent(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            System.out.println(other);
            if (((String) other).length() <= 10) {
                LocalDate date = LocalDate.parse((CharSequence) other);
                assertEquals(expected, dsset.is_adjacent(date));
            }
            else if (((String) other).length() <=25){
                datespan otherDateSpan = new datespan(String.valueOf(other));
                assertEquals(expected, dsset.is_adjacent(otherDateSpan));
            }
            else {
                datespanset otherDateSpan = new datespanset(String.valueOf(other));
                assertEquals(expected, dsset.is_adjacent(otherDateSpan));
            }
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dsset.is_adjacent(other));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-13, false",
            "'[2020-01-01, 2020-01-31]', false",
            "'{[2019-03-23, 2019-05-09), [2019-09-07, 2019-10-09]}', true"
    })
    public void testOverlaps(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            System.out.println(other);
            if (((String) other).length() <= 10) {
                LocalDate date = LocalDate.parse((CharSequence) other);
                assertEquals(expected, dsset.overlaps(date));
            }
            else if (((String) other).length() <=25){
                datespan otherDateSpan = new datespan(String.valueOf(other));
                assertEquals(expected, dsset.overlaps(otherDateSpan));
            }
            else {
                datespanset otherDateSpan = new datespanset(String.valueOf(other));
                assertEquals(expected, dsset.overlaps(otherDateSpan));
            }
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dsset.overlaps(other));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'[2020-01-01, 2020-01-31]', true",
            "'{[2019-03-23, 2019-05-09), [2019-09-07, 2019-10-09]}', false"
    })
    public void testIsBefore(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            System.out.println(other);
            if (((String) other).length() <= 10) {
                LocalDate date = LocalDate.parse((CharSequence) other);
                assertEquals(expected, dsset.is_before(date));
            }
            else if (((String) other).length() <=25){
                datespan otherDateSpan = new datespan(String.valueOf(other));
                assertEquals(expected, dsset.is_before(otherDateSpan));
            }
            else {
                datespanset otherDateSpan = new datespanset(String.valueOf(other));
                assertEquals(expected, dsset.is_before(otherDateSpan));
            }
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dsset.is_before(other));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'[2020-01-01, 2020-01-31]', true",
            "'{[2019-03-23, 2019-05-09), [2019-09-07, 2019-10-09]}', true"
    })
    public void testIsOverBefore(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            System.out.println(other);
            if (((String) other).length() <= 10) {
                LocalDate date = LocalDate.parse((CharSequence) other);
                assertEquals(expected, dsset.is_over_or_before(date));
            }
            else if (((String) other).length() <=25){
                datespan otherDateSpan = new datespan(String.valueOf(other));
                assertEquals(expected, dsset.is_over_or_before(otherDateSpan));
            }
            else {
                datespanset otherDateSpan = new datespanset(String.valueOf(other));
                assertEquals(expected, dsset.is_over_or_before(otherDateSpan));
            }
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dsset.is_over_or_before(other));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'[2020-01-01, 2020-01-31]', false",
            "'{[2019-03-23, 2019-05-09), [2019-09-07, 2019-10-09]}', false"
    })
    public void testIsAfter(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            System.out.println(other);
            if (((String) other).length() <= 10) {
                LocalDate date = LocalDate.parse((CharSequence) other);
                assertEquals(expected, dsset.is_after(date));
            }
            else if (((String) other).length() <=25){
                datespan otherDateSpan = new datespan(String.valueOf(other));
                assertEquals(expected, dsset.is_after(otherDateSpan));
            }
            else {
                datespanset otherDateSpan = new datespanset(String.valueOf(other));
                assertEquals(expected, dsset.is_after(otherDateSpan));
            }
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dsset.is_after(other));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'[2020-01-01, 2020-01-31]', false",
            "'{[2019-03-23, 2019-05-09), [2019-09-07, 2019-10-09]}', true"
    })
    public void testIsOverAfter(Object other, boolean expected) throws Exception {
        if (other instanceof String) {
            System.out.println(other);
            if (((String) other).length() <= 10) {
                LocalDate date = LocalDate.parse((CharSequence) other);
                assertEquals(expected, dsset.is_over_or_after(date));
            }
            else if (((String) other).length() <=25){
                datespan otherDateSpan = new datespan(String.valueOf(other));
                assertEquals(expected, dsset.is_over_or_after(otherDateSpan));
            }
            else {
                datespanset otherDateSpan = new datespanset(String.valueOf(other));
                assertEquals(expected, dsset.is_over_or_after(otherDateSpan));
            }
        } else if (other instanceof LocalDate) {
            assertEquals(expected, dsset.is_over_or_after(other));
        }
    }

    @Test
    public void testDistance() throws Exception {
        datespan datespan1 = new datespan("[2019-09-25, 2019-09-29]");
        datespanset datespanset1 = new datespanset("{(2019-01-01, 2019-04-28), (2019-07-05, 2019-12-31)}");
        dateset dateset1 = new dateset("{2019-09-25, 2019-09-26}");
        LocalDate dateValue = LocalDate.of(2019, 9, 26);

        System.out.println(dsset.distance(datespan1));
        dsset.distance(datespan1);
        System.out.println(dsset.distance(datespanset1));
        dsset.distance(datespanset1);
        System.out.println(dsset.distance(dateset1));
        dsset.distance(dateset1);
        System.out.println(dsset.distance(dateValue));
        dsset.distance(dateValue);
    }

    // Set Functions Tests
    @Test
    public void testIntersection() throws Exception {
        datespan datespan1 = new datespan("[2019-09-09, 2019-09-13]");
        datespanset datespanset1 = new datespanset("{(2019-01-01, 2019-04-28), (2019-07-05, 2019-12-31)}");
        dateset dateset1 = new dateset("{2019-09-09, 2019-09-13}");
        LocalDate dateValue = LocalDate.of(2019, 9, 9);

        System.out.println(dsset.intersection(datespan1).toString());
        dsset.intersection(datespan1);
        System.out.println(dsset.intersection(datespanset1).toString());
        dsset.intersection(datespanset1);
        System.out.println(dsset.intersection(dateset1).toString());
        dsset.intersection(dateset1);
        System.out.println(dsset.intersection(dateValue).toString());
        dsset.intersection(dateValue);
    }

    @Test
    public void testUnion() throws Exception {
        datespan datespan = new datespan("(2020-01-01, 2020-01-31)");
        datespanset datespanset = new datespanset("{(2020-01-01, 2020-01-31), (2021-01-01, 2021-01-31)}");
        dateset dateset = new dateset("{2020-01-01, 2020-01-31}");
        LocalDate dateValue = LocalDate.of(2020, 1, 1);

        dsset.union(datespan);
        dsset.union(datespanset);
        dsset.union(dateset);
        dsset.union(dateValue);
    }

    @Test
    public void testMinus() throws Exception {
        datespan datespan = new datespan("(2020-01-01, 2020-01-31)");
        datespanset datespanset = new datespanset("{(2020-01-01, 2020-01-31), (2021-01-01, 2021-01-31)}");
        dateset dateset = new dateset("{2019-09-09, 2019-09-11}");
        LocalDate dateValue = LocalDate.of(2019, 9, 9);

        System.out.println(dsset.minus(datespan));
        dsset.minus(datespan);
        System.out.println(dsset.minus(datespanset));
        dsset.minus(datespanset);
        System.out.println(dsset.minus(dateset));
        dsset.minus(dateset);
        System.out.println(dsset.minus(dateValue));
        dsset.minus(dateValue);
    }

    // Comparison Functions Tests
    @Test
    public void testCompareFunctions() throws Exception {
        datespan otherSet = new datespan("(2020-01-01, 2020-01-31)");

        assertTrue(dsset.is_before(otherSet));
        assertTrue(dsset.is_over_or_before(otherSet));
        assertFalse(dsset.is_over_or_after(otherSet));
        assertFalse(dsset.is_after(otherSet));
    }
}
