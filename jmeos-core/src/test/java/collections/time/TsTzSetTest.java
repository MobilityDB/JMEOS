package collections.time;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import functions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.collections.time.*;
import types.collections.time.tstzset;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestLogger.class)
class TsTzSetTest {
    private tstzset tset = new tstzset("{2019-09-01 00:00:00+0, 2019-09-02 00:00:00+0, 2019-09-03 00:00:00+0}");

    TsTzSetTest() throws SQLException {
    }

    static error_handler_fn errorHandler = new error_handler();


    private static Stream<Arguments> times() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
                Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), true),
                Arguments.of(new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}"), false)
        );
    }




    public void assert_tstzset_equality(tstzset vset, List<LocalDateTime> timestamps){
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(vset.num_elements(), timestamps.size());
    }



    @Test
    public void testStringConstructor(){
        GeneratedFunctions.meos_initialize_timezone("UTC");
        List<LocalDateTime> list = new ArrayList<>();
        list.add(LocalDateTime.of(2019, 9, 1, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 2, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 3, 0, 0,0));
        assert_tstzset_equality(this.tset,list);
    }

    @Test
    public void testHexwkbConstructor() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
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
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset newtset = new tstzset("{2019-09-01 00:00:00+0, 2019-09-02 00:00:00+0, 2019-09-03 00:00:00+0}");
        assertEquals(tset.toString(), newtset.toString());
    }


    @Test
    public void testCopyConstructor() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset tsett = tset;
        assertEquals(tset.toString(),tsett.toString());
    }


    @Test
    public void testStrOutput() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(tset.toString(),"{\"2019-09-01 00:00:00+00\", \"2019-09-02 00:00:00+00\", \"2019-09-03 00:00:00+00\"}");
    }


    @Test
    public void testTimestampConversion() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzspanset pset = new tstzspanset("{[2019-09-01 00:00:00+00, 2019-09-01 00:00:00+00], [2019-09-02 00:00:00+00, 2019-09-02 00:00:00+00], [2019-09-03 00:00:00+00, 2019-09-03 00:00:00+00]}");
        tstzspanset converted = tset.to_spanset();
        System.out.println(converted.toString());
        assertEquals(converted.toString(),pset.toString());
    }


    @Test
    public void testtstzsetConversion() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzspan p = new tstzspan("[2019-09-01 00:00:00+00, 2019-09-03 00:00:00+00]");
        tstzspan converted = tset.to_span();
        System.out.println(converted.toString());
        assertEquals(converted.toString(),p.toString());
    }


    @Test
    public void testNumTimestamps() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(tset.num_elements(),3);
    }

    @Test
    public void testStartTimestamps() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(tset.start_element(),LocalDateTime.of(2019, 9, 1, 0, 0,0));
    }


    @Test
    public void testEndTimestamps() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(tset.end_element(),LocalDateTime.of(2019, 9, 3, 0, 0,0));
    }

    @Test
    public void testHash() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(tset.hash(),527267058);
    }


    @Test
    public void testIsContainedInFunction() throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_contained_in(tmp_set));
    }


    @Test
    public void testOverlapsFunction() throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.overlaps(tmp_set));
    }


    @Test
    public void testIsBeforeFunction() throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertTrue(tset.is_before(tmp_set));
    }

    @Test
    public void testIsOverOrBeforeFunction() throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertTrue(tset.is_over_or_before(tmp_set));
    }


    @Test
    public void testIsAfterFunction() throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_after(tmp_set));
    }

    @Test
    public void testIsOverOrAfterFunction() throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_over_or_after(tmp_set));
    }

    @Test
    public void testDistanceFunction() throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        tstzset tmp_set = new tstzset("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        System.out.println(Duration.ofSeconds((long) GeneratedFunctions.distance_tstzset_tstzset(tset.get_inner(), tmp_set.get_inner())));
        tset.distance(tmp_set);
    }


    @ParameterizedTest(name="other={0}, expected={1}")
    @MethodSource("times")
    public void testIntersection(Time other, boolean expected) throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        this.tset.intersection(other);
    }

    @ParameterizedTest(name="other={0}, expected={1}")
    @MethodSource("times")
    public void testUnion(Time other, boolean expected) throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        this.tset.union(other);
    }


    @ParameterizedTest(name="other={0}, expected={1}")
    @MethodSource("times")
    public void testMinus(Time other, boolean expected) throws Exception {
        GeneratedFunctions.meos_initialize_timezone("UTC");
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
