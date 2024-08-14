package collections.number;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.collections.number.FloatSpanSet;
import types.collections.number.IntSpan;
import types.collections.number.IntSpanSet;
import functions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntSpanSetTest {
    public IntSpanSet intSpanSet = new IntSpanSet("{[8, 9], [11, 12]}");

    static Stream<Arguments> IntSpan_sources() throws SQLException {
        error_handler_fn errorHandler = new error_handler();
        functions.meos_initialize("UTC", errorHandler);
        return Stream.of(
                Arguments.of("(7, 10)", 8, 10, true, false),
                Arguments.of("[7, 10]", 7, 11, true, false)
        );
    }

    static Stream<Arguments> IntSpan_mulsources() throws SQLException {
        error_handler_fn errorHandler = new error_handler();
        functions.meos_initialize("UTC", errorHandler);
        return Stream.of(
                Arguments.of("7", "10", 7, 10),
                Arguments.of(7, 10, 7, 10),
                Arguments.of(7, "10", 7, 10)
        );
    }

    static Stream<Arguments> Bound_sources() throws SQLException {
        error_handler_fn errorHandler = new error_handler();
        functions.meos_initialize("UTC", errorHandler);
        return Stream.of(
                Arguments.of(true,true),
                Arguments.of(true,false),
                Arguments.of(false,true),
                Arguments.of(false,false)
        );
    }


    public void assert_intspan_equality(IntSpan intsp, Integer lower, Integer upper, Boolean lower_inc, Boolean upper_inc){
        if (lower != null) {
            assertEquals(intsp.lower(), lower.intValue());
        }
        if (upper != null) {
            assertEquals(intsp.upper(), upper.intValue());
        }
        if (lower_inc != null) {
            assertEquals(intsp.lower_inc(), lower_inc.booleanValue());
        }
        if (upper_inc != null) {
            assertEquals(intsp.upper_inc(), upper_inc.booleanValue());
        }
    }

    public void assert_floatspanset_equality(FloatSpanSet fss, Float start_lower, Float start_upper, Float end_lower, Float end_upper, Boolean start_lower_inc, Boolean start_upper_inc, Boolean end_lower_inc, Boolean end_upper_inc){
        if (start_lower != null) {
            assertEquals(fss.start_span().lower(), start_lower.floatValue());
        }
        if (start_upper != null) {
            assertEquals(fss.start_span().upper(), start_upper.floatValue());
        }
        if (end_lower != null) {
            assertEquals(fss.end_span().lower(), end_lower.floatValue());
        }
        if (end_upper != null) {
            assertEquals(fss.end_span().upper(), end_upper.floatValue());
        }
        if (start_lower_inc != null) {
            assertEquals(fss.start_span().lower_inc(), start_lower_inc.booleanValue());
        }
        if (start_upper_inc != null) {
            assertEquals(fss.start_span().upper_inc(), start_upper_inc.booleanValue());
        }
        if (end_lower_inc != null) {
            assertEquals(fss.end_span().lower_inc(), end_lower_inc.booleanValue());
        }
        if (end_upper_inc != null) {
            assertEquals(fss.end_span().upper_inc(), end_upper_inc.booleanValue());
        }
    }

    public void assert_intspanset_equality(IntSpanSet fss, Integer start_lower, Integer start_upper, Integer end_lower, Integer end_upper, Boolean start_lower_inc, Boolean start_upper_inc, Boolean end_lower_inc, Boolean end_upper_inc){
        if (start_lower != null) {
            assertEquals(fss.start_span().lower(), start_lower.intValue());
        }
        if (start_upper != null) {
            assertEquals(fss.start_span().upper(), start_upper.intValue());
        }
        if (end_lower != null) {
            assertEquals(fss.end_span().lower(), end_lower.intValue());
        }
        if (end_upper != null) {
            assertEquals(fss.end_span().upper(), end_upper.intValue());
        }
        if (start_lower_inc != null) {
            assertEquals(fss.start_span().lower_inc(), start_lower_inc.booleanValue());
        }
        if (start_upper_inc != null) {
            assertEquals(fss.start_span().upper_inc(), start_upper_inc.booleanValue());
        }
        if (end_lower_inc != null) {
            assertEquals(fss.end_span().lower_inc(), end_lower_inc.booleanValue());
        }
        if (end_upper_inc != null) {
            assertEquals(fss.end_span().upper_inc(), end_upper_inc.booleanValue());
        }
    }

    @ParameterizedTest(name = "Test Constructor method")
    @MethodSource("IntSpan_sources")
    public void testStringConstructor(String source, int lower, int upper, boolean lower_inc, boolean upper_inc){
        IntSpan intsp = new IntSpan(source);
        assert_intspan_equality(intsp,lower,upper,lower_inc,upper_inc);
    }

    @Test
    public void testToSpan(){
        IntSpan intSpan= intSpanSet.to_span();
        System.out.println(intSpan.toString());
        assert_intspan_equality(intSpan, 8, 13, true, false);
    }

    @Test
    public void testToFloatSpanSet(){
        FloatSpanSet floatSpanSet= intSpanSet.to_floatspanset();
        System.out.println(floatSpanSet.toString(15));
        System.out.println(floatSpanSet.start_span().upper()==9.0f);
        assert_floatspanset_equality(floatSpanSet, 8.0f, 9.0f, 11.0f, 12.0f, true, true, true, true);
    }

    @Test
    public void testWidthFloatSpanSet(){
        assertEquals(5, intSpanSet.width(true));
        assertEquals(4, intSpanSet.width(false));
    }

    @Test
    public void testStartSpan(){
        assertEquals("[8, 10)", intSpanSet.start_span().toString());
    }

    @Test
    public void testEndSpan(){
        assertEquals("[11, 13)", intSpanSet.end_span().toString());
    }

    @Test
    public void testSpanN(){
        assertEquals(intSpanSet.start_span().toString(), intSpanSet.span_n(1).toString());
    }

    @Test
    public void testSpans(){
        System.out.println(intSpanSet.spans());
        List<IntSpan> spanList= intSpanSet.spans();
        String s= "{";
        for (IntSpan i : spanList) {
            s= s + i.toString() + ", ";
        }
        s= s.substring(0, s.length()-2) + "}";
        System.out.println(s);
        IntSpanSet intSpanSet1= new IntSpanSet(s);
        assert_intspanset_equality(intSpanSet1, 8, 10, 11, 13, true, false, true, false);
    }

    @Test
    public void testShift(){
        IntSpanSet intSpanSet1= intSpanSet.shift(2);
        assert_intspanset_equality(intSpanSet1, 10, 12, 13, 15, true, false, true, false);
    }

    @Test
    public void testScale(){
        IntSpanSet intSpanSet1= intSpanSet.scale(2);
        System.out.println(intSpanSet1.toString());
        assert_intspanset_equality(intSpanSet1, 8, 9, 9, 11, true, false, true, false);
    }

    @Test
    public void testShiftScale(){
        IntSpanSet intSpanSet1= intSpanSet.shift_scale(2, 2);
        System.out.println(intSpanSet1.toString());
        assert_intspanset_equality(intSpanSet1, 10, 11, 11, 13, true, false, true, false);
    }

    @Test
    public void testIntSpanSetOutput(){
        assertEquals("{[8, 10), [11, 13)}",intSpanSet.toString());
    }

    @Test
    public void IntSpanTopologicalPositionFunctions() throws Exception {
        int value = 10 ;
        IntSpan intspan2 = new IntSpan("[13, 15]");
        IntSpanSet intspanset = new IntSpanSet("{(1, 20), (31, 41)}");

        assertTrue(intSpanSet.is_adjacent(value));
        assertTrue(intSpanSet.is_adjacent(intspan2));
        assertFalse(intSpanSet.is_adjacent(intspanset));

        assertFalse(intSpanSet.is_contained_in(intspan2));
        assertTrue(intSpanSet.is_contained_in(intspanset));

        assertFalse(intSpanSet.contains(value));
        assertFalse(intSpanSet.contains(intspan2));
        assertFalse(intSpanSet.contains(intspanset));

        assertFalse(intSpanSet.overlaps(intspan2));
        assertTrue(intSpanSet.overlaps(intspanset));

        assertFalse(intSpanSet.is_same(value));
        assertFalse(intSpanSet.is_same(intspan2));
        assertFalse(intSpanSet.is_same(intspanset));

        assertFalse(intSpanSet.is_left(value));
        assertTrue(intSpanSet.is_left(intspan2));
        assertFalse(intSpanSet.is_left(intspanset));

        assertFalse(intSpanSet.is_over_or_left(value));
        assertTrue(intSpanSet.is_over_or_left(intspan2));
        assertTrue(intSpanSet.is_over_or_left(intspanset));

        assertFalse(intSpanSet.is_right(value));
        assertFalse(intSpanSet.is_right(intspan2));
        assertFalse(intSpanSet.is_right(intspanset));

        assertFalse(intSpanSet.is_over_or_right(value));
        assertFalse(intSpanSet.is_over_or_right(intspan2));
        assertTrue(intSpanSet.is_over_or_right(intspanset));
    }

    @Test
    public void testIntSpanSetFunctions() throws Exception {
        int value = 1;
        IntSpan intspan2 = new IntSpan("(1, 20)");
        IntSpanSet intspanset = new IntSpanSet("{(1, 20), (31, 41)}");

        intSpanSet.union(value);
        intSpanSet.union(intspan2);
        intSpanSet.union(intspanset);

        intSpanSet.minus(value);
        intSpanSet.minus(intspan2);
        intSpanSet.minus(intspanset);

        intSpanSet.intersection(value);
        intSpanSet.intersection(intspan2);
        intSpanSet.intersection(intspanset);

    }
}

