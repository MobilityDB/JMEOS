package collections.time;

import functions.functions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import functions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.collections.time.*;
import types.collections.time.tstzset;

import static org.junit.jupiter.api.Assertions.*;

class tstzsetTest {
    private tstzset tset = new tstzset("{2019-09-01 00:00:00+0, 2019-09-02 00:00:00+0, 2019-09-03 00:00:00+0}");

    tstzsetTest() throws SQLException {
    }

    static error_handler_fn errorHandler = new error_handler();


    private static Stream<Arguments> times() {
        functions.meos_initialize("UTC", errorHandler);
        return Stream.of(
                Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
                Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), true),
                Arguments.of(new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}"), false)
        );
    }




    public void assert_tstzset_equality(tstzset vset, List<LocalDateTime> timestamps){
        functions.meos_initialize("UTC", errorHandler);
        assertEquals(vset.num_elements(), timestamps.size());
    }



    @Test
    public void testStringConstructor(){
        functions.meos_initialize("UTC", errorHandler);
        List<LocalDateTime> list = new ArrayList<>();
        list.add(LocalDateTime.of(2019, 9, 1, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 2, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 3, 0, 0,0));
        assert_tstzset_equality(this.tset,list);
    }

    @Test
    public void testHexwkbConstructor() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
//        tstzset tsett = types.collections.time.tstzset.from_hexwkb("012100000040021FFE3402000000B15A26350200");
        String hexwkb_string= tset.as_hexwkb();
		System.out.println(hexwkb_string);
        tstzset p = types.collections.time.tstzset.from_hexwkb(hexwkb_string);
		System.out.println(p.toString());
        List<LocalDateTime> list = new ArrayList<>();
        list.add(LocalDateTime.of(2019, 9, 1, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 2, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 3, 0, 0,0));
        System.out.println(list);
        assert_tstzset_equality(p,list);
    }


    @Test
    public void testFromAsConstructor() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        tstzset newtset = new tstzset("{2019-09-01 00:00:00+0, 2019-09-02 00:00:00+0, 2019-09-03 00:00:00+0}");
        assertEquals(tset.toString(), newtset.toString());
    }


    @Test
    public void testCopyConstructor() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        tstzset tsett = tset;
        assertEquals(tset.toString(),tsett.toString());
    }


    @Test
    public void testStrOutput() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        assertEquals(tset.toString(),"{\"2019-09-01 00:00:00+00\", \"2019-09-02 00:00:00+00\", \"2019-09-03 00:00:00+00\"}");
    }


    @Test
    public void testTimestampConversion() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        tstzspanset pset = new tstzspanset("{[2019-09-01 00:00:00+00, 2019-09-01 00:00:00+00], [2019-09-02 00:00:00+00, 2019-09-02 00:00:00+00], [2019-09-03 00:00:00+00, 2019-09-03 00:00:00+00]}");
        tstzspanset converted = tset.to_spanset();
        System.out.println(converted.toString());
        assertEquals(converted.toString(),pset.toString());
    }


    @Test
    public void testtstzsetConversion() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        tstzspan p = new tstzspan("[2019-09-01 00:00:00+00, 2019-09-03 00:00:00+00]");
        tstzspan converted = tset.to_span();
        System.out.println(converted.toString());
        assertEquals(converted.toString(),p.toString());
    }


    @Test
    public void testNumTimestamps() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        assertEquals(tset.num_elements(),3);
    }

    @Test
    public void testStartTimestamps() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        assertEquals(tset.start_element(),LocalDateTime.of(2019, 9, 1, 0, 0,0));
    }


    @Test
    public void testEndTimestamps() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        assertEquals(tset.end_element(),LocalDateTime.of(2019, 9, 3, 0, 0,0));
    }

    @Test
    public void testHash() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        assertEquals(tset.hash(),527267058);
    }


    @Test
    public void testIsContainedInFunction() throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_contained_in(tmp_set));
    }


    @Test
    public void testOverlapsFunction() throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.overlaps(tmp_set));
    }


    @Test
    public void testIsBeforeFunction() throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertTrue(tset.is_before(tmp_set));
    }

    @Test
    public void testIsOverOrBeforeFunction() throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertTrue(tset.is_over_or_before(tmp_set));
    }


    @Test
    public void testIsAfterFunction() throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_after(tmp_set));
    }

    @Test
    public void testIsOverOrAfterFunction() throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_over_or_after(tmp_set));
    }

    @Test
    public void testDistanceFunction() throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        System.out.println(Duration.ofSeconds((long) functions.distance_tstzset_tstzset(tset.get_inner(), tmp_set.get_inner())));
        tset.distance(tmp_set);
    }


    @ParameterizedTest(name="Test intersection method")
    @MethodSource("times")
    public void testIntersection(Time other, boolean expected) throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        this.tset.intersection(other);
    }

    @ParameterizedTest(name="Test union method")
    @MethodSource("times")
    public void testUnion(Time other, boolean expected) throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        this.tset.union(other);
    }


    @ParameterizedTest(name="Test minus method")
    @MethodSource("times")
    public void testMinus(Time other, boolean expected) throws Exception {
        functions.meos_initialize("UTC", errorHandler);
        this.tset.minus(other);
    }



    @Test
    public void testComparisons() throws Exception {
        tstzset first = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        tstzset second = new tstzset("{2020-01-02 00:00:00+0, 2020-03-31 00:00:00+0}");
        assertFalse(first.eq(second));
        assertTrue(first.notEquals(second));
        assertTrue(first.lessThan(second));
        assertTrue(first.lessThanOrEqual(second));
        assertTrue(first.greaterThan(second));
        assertFalse(first.greaterThanOrEqual(second));
    }
}
