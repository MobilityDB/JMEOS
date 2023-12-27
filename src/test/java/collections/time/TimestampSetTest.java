package collections.time;

import functions.functions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import types.collections.time.PeriodSet;
import types.collections.time.TimestampSet;
import types.collections.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class TimestampSetTest {
    private TimestampSet tset = new TimestampSet("{2019-09-01 00:00:00+0, 2019-09-02 00:00:00+0, 2019-09-03 00:00:00+0}");

    TimestampSetTest() throws SQLException {
    }



    public void assert_timestampset_equality(TimestampSet vset, List<LocalDateTime> timestamps){
        functions.meos_initialize("UTC");
        assertEquals(vset.num_elements(), timestamps.size());
    }



    @Test
    public void testStringConstructor(){
        functions.meos_initialize("UTC");
        List<LocalDateTime> list = new ArrayList<>();
        list.add(LocalDateTime.of(2019, 9, 1, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 2, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 3, 0, 0,0));
        assert_timestampset_equality(this.tset,list);
    }

    @Test
    public void testHexwkbConstructor() throws SQLException {
        functions.meos_initialize("UTC");
        TimestampSet tsett = TimestampSet.from_hexwkb("012000010300000000A01E4E713402000000F66B853402000060CD8999340200");
        List<LocalDateTime> list = new ArrayList<>();
        list.add(LocalDateTime.of(2019, 9, 1, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 2, 0, 0,0));
        list.add(LocalDateTime.of(2019, 9, 3, 0, 0,0));
        assert_timestampset_equality(tsett,list);
    }


    @Test
    public void testFromAsConstructor() throws SQLException {
        functions.meos_initialize("UTC");
        TimestampSet newtset = new TimestampSet("{2019-09-01 00:00:00+0, 2019-09-02 00:00:00+0, 2019-09-03 00:00:00+0}");
        assertEquals(tset.toString(), newtset.toString());
    }


    @Test
    public void testCopyConstructor() throws SQLException {
        functions.meos_initialize("UTC");
        TimestampSet tsett = tset.copy();
        assertEquals(tset.toString(),tsett.toString());
    }


    @Test
    public void testStrOutput() throws SQLException {
        functions.meos_initialize("UTC");
        assertEquals(tset.toString(),"{\"2019-09-01 00:00:00+00\", \"2019-09-02 00:00:00+00\", \"2019-09-03 00:00:00+00\"}");
    }


    @Test
    public void testTimestampConversion() throws SQLException {
        functions.meos_initialize("UTC");
        PeriodSet pset = new PeriodSet("{[2019-09-01 00:00:00+00, 2019-09-01 00:00:00+00], [2019-09-02 00:00:00+00, 2019-09-02 00:00:00+00], [2019-09-03 00:00:00+00, 2019-09-03 00:00:00+00]}");
        PeriodSet converted = tset.to_periodset();
        assertEquals(converted.toString(),pset.toString());
    }


    @Test
    public void testPeriodConversion() throws SQLException {
        functions.meos_initialize("UTC");
        Period p = new Period("[2019-09-01 00:00:00+00, 2019-09-03 00:00:00+00]");
        Period converted = tset.to_period();
        assertEquals(converted.toString(),p.toString());
    }


    @Test
    public void testNumTimestamps() throws SQLException {
        functions.meos_initialize("UTC");
        assertEquals(tset.num_elements(),3);
    }

    @Test
    public void testStartTimestamps() throws SQLException {
        functions.meos_initialize("UTC");
        assertEquals(tset.start_element(),LocalDateTime.of(2019, 9, 1, 0, 0,0));
    }


    @Test
    public void testEndTimestamps() throws SQLException {
        functions.meos_initialize("UTC");
        assertEquals(tset.end_element(),LocalDateTime.of(2019, 9, 3, 0, 0,0));
    }

    @Test
    public void testHash() throws SQLException {
        functions.meos_initialize("UTC");
        assertEquals(tset.hash(),527267058);
    }


    @Test
    public void testTimestampSetPositionFunction() throws Exception {
        functions.meos_initialize("UTC");
        TimestampSet tmp_set = new TimestampSet("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_contained_in(tmp_set));
    }


    @Test
    public void testOverlapsFunction() throws Exception {
        functions.meos_initialize("UTC");
        TimestampSet tmp_set = new TimestampSet("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.overlaps(tmp_set));
    }


    @Test
    public void testIsBeforeFunction() throws Exception {
        functions.meos_initialize("UTC");
        TimestampSet tmp_set = new TimestampSet("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertTrue(tset.is_before(tmp_set));
    }

    @Test
    public void testIsOverOrBeforeFunction() throws Exception {
        functions.meos_initialize("UTC");
        TimestampSet tmp_set = new TimestampSet("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertTrue(tset.is_over_or_before(tmp_set));
    }


    @Test
    public void testIsAfterFunction() throws Exception {
        functions.meos_initialize("UTC");
        TimestampSet tmp_set = new TimestampSet("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_after(tmp_set));
    }

    @Test
    public void testIsOverOrAfterFunction() throws Exception {
        functions.meos_initialize("UTC");
        TimestampSet tmp_set = new TimestampSet("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        assertFalse(tset.is_over_or_after(tmp_set));
    }

    @Test
    public void testDistanceFunction() throws Exception {
        functions.meos_initialize("UTC");
        TimestampSet tmp_set = new TimestampSet("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        tset.distance(tmp_set);
    }


    @Test
    public void testComparisons() throws Exception {
        TimestampSet first = new TimestampSet("{2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0}");
        TimestampSet second = new TimestampSet("{2020-01-02 00:00:00+0, 2020-03-31 00:00:00+0}");
        assertFalse(first.eq(second));
        assertTrue(first.notEquals(second));
        assertTrue(first.lessThan(second));
        assertTrue(first.lessThanOrEqual(second));
        assertTrue(first.greaterThan(second));
        assertFalse(first.greaterThanOrEqual(second));
    }














}
