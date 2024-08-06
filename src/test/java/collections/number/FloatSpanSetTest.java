package collections.number;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import types.collections.number.FloatSpan;
import types.collections.number.FloatSpanSet;
import types.collections.number.IntSpanSet;
import functions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FloatSpanSetTest {
    public FloatSpanSet floatSpanSet = new FloatSpanSet("{[8, 9], [11, 12]}");

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


    public void assert_floatspan_equality(FloatSpan floatsp, Float lower, Float upper, Boolean lower_inc, Boolean upper_inc){
        if (lower != null) {
            assertEquals(floatsp.lower(), lower.intValue());
        }
        if (upper != null) {
            assertEquals(floatsp.upper(), upper.intValue());
        }
        if (lower_inc != null) {
            assertEquals(floatsp.lower_inc(), lower_inc.booleanValue());
        }
        if (upper_inc != null) {
            assertEquals(floatsp.upper_inc(), upper_inc.booleanValue());
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

//    @ParameterizedTest(name = "Test Constructor method")
//    @MethodSource("IntSpan_sources")
//    public void testStringConstructor(String source, float lower, float upper, boolean lower_inc, boolean upper_inc){
//        FloatSpan floatsp = new FloatSpan(source);
//        assert_floatspan_equality(floatsp,lower,upper,lower_inc,upper_inc);
//    }

    @Test
    public void testToSpan(){
        FloatSpan floatSpan= floatSpanSet.to_span();
        System.out.println(floatSpan.toString(15));
        assert_floatspan_equality(floatSpan, 8.0f, 12.0f, true, true);
    }

    @Test
    public void testToIntSpanSet(){
        IntSpanSet intSpanSet= floatSpanSet.to_intspanset();
        System.out.println(intSpanSet.toString());
        System.out.println(intSpanSet.start_span().upper()==10);
        assert_intspanset_equality(intSpanSet, 8, 10, 11, 13, true, false, true, false);
    }

    @Test
    public void testWidthFloatSpanSet(){
        assertEquals(4.0f, floatSpanSet.width(true));
        assertEquals(2.0f, floatSpanSet.width(false));
    }

    @Test
    public void testStartSpan(){
        assertEquals("[8, 9]", floatSpanSet.start_span().toString(15));
    }

    @Test
    public void testEndSpan(){
        assertEquals("[11, 12]", floatSpanSet.end_span().toString(15));
    }

    @Test
    public void testSpanN(){
        assertEquals(floatSpanSet.start_span().toString(15), floatSpanSet.span_n(1).toString(15));
    }

    @Test
    public void testSpans(){
//        System.out.println(floatSpanSet.spans());
        List<FloatSpan> spanList= floatSpanSet.spans();
        String s= "{";
        for (FloatSpan i : spanList) {
//            System.out.println(i.toString(15));
            s= s + i.toString(15) + ", ";
        }
        s= s.substring(0, s.length()-2) + "}";
        FloatSpanSet floatSpanSet1= new FloatSpanSet(s);
        System.out.println(s);
        assert_floatspanset_equality(floatSpanSet1, 8.0f, 9.0f, 11.0f, 12.0f, true, true, true, true);
    }

    @Test
    public void testShift(){
        FloatSpanSet floatSpanSet1= floatSpanSet.shift(2);
        System.out.println(floatSpanSet1.toString(15));
        assert_floatspanset_equality(floatSpanSet1, 10.0f, 11.0f, 13.0f, 14.0f, true, true, true, true);
    }

    @Test
    public void testScale(){
        FloatSpanSet floatSpanSet1= floatSpanSet.scale(2);
        System.out.println(floatSpanSet1.toString(15));
        assert_floatspanset_equality(floatSpanSet1, 8.0f, 8.5f, 9.5f, 10.0f, true, true, true, true);
    }

    @Test
    public void testShiftScale(){
        FloatSpanSet floatSpanSet1= floatSpanSet.shift_scale(2, 2);
        System.out.println(floatSpanSet1.toString(15));
        assert_floatspanset_equality(floatSpanSet1, 10.0f, 10.5f, 11.5f, 12.0f, true, true, true, true);
    }

    @Test
    public void testIntSpanSetOutput(){
        assertEquals("{[8, 9], [11, 12]}",floatSpanSet.toString(15));
    }

    @Test
    public void IntSpanTopologicalPositionFunctions() throws Exception {
        float value = 10.0f;
        FloatSpan floatSpan1 = new FloatSpan("(12.0, 15.32]");
        FloatSpanSet floatSpanSet1 = new FloatSpanSet("{(1, 20), (31, 41)}");

        assertFalse(floatSpanSet.is_adjacent(value));
        assertTrue(floatSpanSet.is_adjacent(floatSpan1));
        assertFalse(floatSpanSet.is_adjacent(floatSpanSet1));

        assertFalse(floatSpanSet.is_contained_in(floatSpan1));
        assertTrue(floatSpanSet.is_contained_in(floatSpanSet1));

        assertFalse(floatSpanSet.contains(value));
        assertFalse(floatSpanSet.contains(floatSpan1));
        assertFalse(floatSpanSet.contains(floatSpanSet1));

        assertFalse(floatSpanSet.overlaps(floatSpan1));
        assertTrue(floatSpanSet.overlaps(floatSpanSet1));

        assertFalse(floatSpanSet.is_same(value));
        assertFalse(floatSpanSet.is_same(floatSpan1));
        assertFalse(floatSpanSet.is_same(floatSpanSet1));

        assertFalse(floatSpanSet.is_left(value));
        assertTrue(floatSpanSet.is_left(floatSpan1));
        assertFalse(floatSpanSet.is_left(floatSpanSet1));

        assertFalse(floatSpanSet.is_over_or_left(value));
        assertTrue(floatSpanSet.is_over_or_left(floatSpan1));
        assertTrue(floatSpanSet.is_over_or_left(floatSpanSet1));

        assertFalse(floatSpanSet.is_right(value));
        assertFalse(floatSpanSet.is_right(floatSpan1));
        assertFalse(floatSpanSet.is_right(floatSpanSet1));

        assertFalse(floatSpanSet.is_over_or_right(value));
        assertFalse(floatSpanSet.is_over_or_right(floatSpan1));
        assertTrue(floatSpanSet.is_over_or_right(floatSpanSet1));
    }

    @Test
    public void testIntSpanSetFunctions() throws Exception {
        Float value = 1.0f;
        FloatSpan floatSpan2 = new FloatSpan("(1.2, 20.2)");
        FloatSpanSet floatSpanSet2 = new FloatSpanSet("{(1.2, 20.2), (31.3, 41.32)}");

        floatSpanSet.union(value);
        floatSpanSet.union(floatSpan2);
        floatSpanSet.union(floatSpanSet2);

        floatSpanSet.minus(value);
        floatSpanSet.minus(floatSpan2);
        floatSpanSet.minus(floatSpanSet2);

        floatSpanSet.intersection(value);
        floatSpanSet.intersection(floatSpan2);
        floatSpanSet.intersection(floatSpanSet2);

    }
}

