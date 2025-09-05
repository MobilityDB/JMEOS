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
import types.collections.time.dateset;
import types.collections.time.datespan;
import types.collections.time.datespanset;
import types.collections.time.test;
//import types.collections.time.DateSet;
import java.util.Arrays;
import java.util.List;

import functions.*;

import static org.junit.jupiter.api.Assertions.*;

class datesetTest {
    private final dateset dset;
    private final dateset dset2;

    datesetTest() throws SQLException {
        functions.meos_initialize_timezone("UTC");
        dset = new dateset("{2019-09-25, 2019-09-26, 2019-09-27}");
        dset2 = new dateset("{2019-09-08, 2019-09-10}");
    }

    static error_handler_fn errorHandler = new error_handler();

    public void assert_dateset_equality(dateset vset, List<LocalDate> timestamps){
        assertEquals(vset.num_elements(), timestamps.size());
    }

    @Test
    public void testStringConstructor(){
        System.out.println(dset.toString());
        List<LocalDate> list = new ArrayList<>();
        list.add(LocalDate.of(2019, 9, 25));
        list.add(LocalDate.of(2019, 9, 26));
        list.add(LocalDate.of(2019, 9, 27));
        assert_dateset_equality(dset,list);
    }

    @Test
    public void testListConstructor() {
        dateset dsetList = new dateset(Arrays.asList(
                LocalDate.of(2019, 9, 25),
                LocalDate.of(2019, 9, 26),
                LocalDate.of(2019, 9, 27)
        ));
        System.out.println(dsetList.toString());
        assertEquals(dset.toString(), dsetList.toString());
    }

    @Test
    public void testFromAsConstructor() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals(dset.toString(), new dateset(dset.toString()).toString());
        String wkb= dset.from_wkb(dset.as_wkb(), dset.toString().length(), dateset.class).toString();
        String hexwkb= dset.from_hexwkb(dset.as_hexwkb(), dateset.class).toString();
        assertEquals(dset.toString(), wkb);
        System.out.println(wkb);
        assertEquals(dset.toString(), hexwkb);
        System.out.println(hexwkb);
    }

    // Collection Conversion Tests
    @Test
    public void testToSpan() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespan expectedSpan = new datespan("[2019-09-25, 2019-09-27]");
        assertEquals(expectedSpan.toString(), dset.to_span(datespan.class).toString());
    }

    @Test
    public void testToSpanSet() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespanset expectedSpanSet = new datespanset("{[2019-09-25, 2019-09-25], [2019-09-26, 2019-09-26], [2019-09-27, 2019-09-27]}");
        assertEquals(expectedSpanSet.toString(), dset.to_spanset(datespanset.class).toString());
    }

    // Accessor Tests
    @Test
    public void testDuration() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Duration expectedDuration = Duration.ofDays(3);  // Assuming duration counts days between the start and end date.
        assertEquals(expectedDuration, dset.duration());
    }

    @Test
    public void testNumElements() {
        assertEquals(3, dset.num_elements());
    }

    @Test
    public void testStartElement() throws ParseException {
        assertEquals(LocalDate.of(2019, 9, 25), dset.start_element());
    }

    @Test
    public void testEndElement() throws ParseException {
        assertEquals(LocalDate.of(2019, 9, 27), dset.end_element());
    }

    @Test
    public void testElementN() throws Exception {
        assertEquals(LocalDate.of(2019, 9, 26), dset.element_n(1));
    }

    @Test
    public void testElementNOutOfRange() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            dset.element_n(3);
        });
    }

    @Test
    public void testElements() throws Exception {
        List<LocalDate> expectedElements = Arrays.asList(
                LocalDate.of(2019, 9, 25),
                LocalDate.of(2019, 9, 26),
                LocalDate.of(2019, 9, 27)
        );
        assertEquals(expectedElements, dset.elements());
    }

    // Transformation tests
    @Test
    public void testScale() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        dateset expected_dateset = new dateset("{2019-09-25, 2019-10-03, 2019-10-11}");
        System.out.println(dset.scale(15).toString());
        assertEquals(expected_dateset.toString(), dset.scale(15).toString());
    }

    @Test
    public void testShift() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        dateset expected_dateset = new dateset("{2019-10-10, 2019-10-11, 2019-10-12}");
        System.out.println(dset.shift(15).toString());
        assertEquals(expected_dateset.toString(), dset.shift(15).toString());
    }

    @Test
    public void testShiftScale() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        dateset expected_dateset = new dateset("{2019-10-10, 2019-10-18, 2019-10-26}");
        System.out.println(dset.shift_scale(15, 15).toString());
        assertEquals(expected_dateset.toString(), dset.shift_scale(15, 15).toString());
    }



    // Position Functions Tests
    @ParameterizedTest
    @CsvSource({
            "'2020-01-01, 2020-01-31', false"
    })
    public void testIsContainedIn(String otherDateSetStr, boolean expected) throws Exception {
        dateset otherDateSet = new dateset("{" + otherDateSetStr + "}");
        assertEquals(expected, dset.is_contained_in(otherDateSet));
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-25, true",
            "'2020-01-01, 2020-01-31', false"
    })
    public void testContains(Object other, boolean expected) throws Exception {
        if (other instanceof LocalDate) {
            assertEquals(expected, dset.contains((LocalDate) other));
        } else if (other instanceof String) {
            dateset otherDateSet = new dateset("{" + other + "}");
            assertEquals(expected, dset.contains(otherDateSet));
        }
    }

    @Test
    public void testOverlaps() throws Exception {
        String otherDateSetStr= "{2020-01-01, 2020-01-31}";
        dateset otherDateSet= new dateset(otherDateSetStr);
        assertFalse(dset.overlaps(otherDateSet));  // Assuming overlaps returns boolean
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-25, false",
            "'2020-01-01, 2020-01-31', true"
    })
    public void testIsBefore(Object other, boolean expected) throws Exception {
        if (other instanceof LocalDate) {
            assertEquals(expected, dset.is_before((LocalDate) other));
        } else if (other instanceof String) {
            dateset otherDateSet = new dateset("{" + other + "}");
            assertEquals(expected, dset.is_before(otherDateSet));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-25, false",
            "'2020-01-01, 2020-01-31', true"
    })
    public void testIsOverOrBefore(Object other, boolean expected) throws Exception {
        if (other instanceof LocalDate) {
            assertEquals(expected, dset.is_over_or_before((LocalDate) other));
        } else if (other instanceof String) {
            dateset otherDateSet = new dateset("{" + other + "}");
            assertEquals(expected, dset.is_over_or_before(otherDateSet));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-24, true",
            "'2020-01-01, 2020-01-31', false"
    })
    public void testIsAfter(Object other, boolean expected) throws Exception {
        if (other instanceof LocalDate) {
            assertEquals(expected, dset.is_after((LocalDate) other));
        } else if (other instanceof String) {
            dateset otherDateSet = new dateset("{" + other + "}");
            assertEquals(expected, dset.is_after(otherDateSet));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2019-09-24, true",
            "'2020-01-01, 2020-01-31', false"
    })
    public void testIsOverOrAfter(Object other, boolean expected) throws Exception {
        if (other instanceof LocalDate) {
            assertEquals(expected, dset.is_over_or_after((LocalDate) other));
        } else if (other instanceof String) {
            dateset otherDateSet = new dateset("{" + other + "}");
            assertEquals(expected, dset.is_over_or_after(otherDateSet));
        }
    }

    @Test
    public void testDistance() throws Exception {
        dateset otherDateSet = new dateset("{2020-01-01, 2020-01-31}");
        System.out.println(dset.distance(otherDateSet).toSeconds());
        assertEquals(Duration.ofSeconds(96), dset.distance(otherDateSet));  // Assuming distance returns Duration
    }

    // Set Functions Tests
    @Test
    public void testIntersection() throws Exception {
        datespan datespan1 = new datespan("[2019-09-25, 2019-09-29]");
        datespanset datespanset1 = new datespanset("{(2019-01-01, 2019-04-28), (2019-07-05, 2019-12-31)}");
        dateset dateset1 = new dateset("{2019-09-25, 2019-09-26}");
        LocalDate dateValue = LocalDate.of(2019, 9, 25);

        System.out.println(dset.intersection(datespan1).toString());
        dset.intersection(datespan1);
        System.out.println(dset.intersection(datespanset1).toString());
        dset.intersection(datespanset1);
        System.out.println(dset.intersection(dateset1).toString());
        dset.intersection(dateset1);
        System.out.println(dset.intersection(dateValue).toString());
        dset.intersection(dateValue);
    }

    @Test
    public void testUnion() throws Exception {
        datespan datespan = new datespan("(2020-01-01, 2020-01-31)");
        datespanset datespanset = new datespanset("{(2020-01-01, 2020-01-31), (2021-01-01, 2021-01-31)}");
        dateset dateset = new dateset("{2020-01-01, 2020-01-31}");
        LocalDate dateValue = LocalDate.of(2020, 1, 1);

        dset.union(datespan);
        dset.union(datespanset);
        dset.union(dateset);
        dset.union(dateValue);
    }

    @Test
    public void testMinus() throws Exception {
        datespan datespan = new datespan("(2020-01-01, 2020-01-31)");
        datespanset datespanset = new datespanset("{(2020-01-01, 2020-01-31), (2021-01-01, 2021-01-31)}");
        dateset dateset = new dateset("{2020-01-01, 2020-01-31}");
        LocalDate dateValue = LocalDate.of(2020, 1, 1);

        System.out.println(dset.minus(datespan));
        dset.minus(datespan);
        System.out.println(dset.minus(datespanset));
        dset.minus(datespanset);
        System.out.println(dset.minus(dateset));
        dset.minus(dateset);
        System.out.println(dset.minus(dateValue));
        dset.minus(dateValue);
    }

    // Comparison Functions Tests
    @Test
    public void testCompareFunctions() throws Exception {
        dateset otherSet = new dateset("{2020-01-01, 2020-01-31}");

        assertTrue(dset.is_before(otherSet));
        assertTrue(dset.is_over_or_before(otherSet));
        assertFalse(dset.is_over_or_after(otherSet));
        assertFalse(dset.is_after(otherSet));
    }
}
