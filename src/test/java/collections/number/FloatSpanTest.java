package collections.number;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.collections.number.FloatSpan;
import types.collections.number.FloatSpanSet;
import functions.*;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FloatSpanTest {
    public FloatSpan floatSpan = new FloatSpan("(2.5, 5.21]");

    static Stream<Arguments> IntSpan_sources() throws SQLException {
        error_handler_fn errorHandler = new error_handler();
        functions.meos_initialize_timezone("UTC");
        functions.meos_initialize_error_handler(errorHandler);
        return Stream.of(
                Arguments.of("(2.5, 5.21)", 2.5f, 5.21f, false, false),
                Arguments.of("[2.5, 5.21]", 2.5f, 5.21f, true, true)
        );
    }

    static Stream<Arguments> IntSpan_mulsources() throws SQLException {
        error_handler_fn errorHandler = new error_handler();
        functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
        return Stream.of(
                Arguments.of("2.5", "5.21", 2.5f, 5.21f),
                Arguments.of(2.5f, 5.21f, 2.5f, 5.21f),
                Arguments.of(2.5f, "5.21", 2.5f, 5.21f)
        );
    }

    static Stream<Arguments> Bound_sources() throws SQLException {
        error_handler_fn errorHandler = new error_handler();
        functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
        return Stream.of(
                Arguments.of(true,true),
                Arguments.of(true,false),
                Arguments.of(false,true),
                Arguments.of(false,false)
        );
    }


    public void assert_floatSpan_equality(FloatSpan floatsp, Float lower, Float upper, Boolean lower_inc, Boolean upper_inc){
        if (lower != null) {
            assertEquals((float)floatsp.lower(), lower.floatValue());
        }
        if (upper != null) {
            assertEquals((float)floatsp.upper(), upper.floatValue());
        }
        if (lower_inc != null) {
            assertEquals(floatsp.lower_inc(), lower_inc.booleanValue());
        }
        if (upper_inc != null) {
            assertEquals(floatsp.upper_inc(), upper_inc.booleanValue());
        }
    }

    @ParameterizedTest(name = "Test Constructor method")
    @MethodSource("IntSpan_sources")
    public void testStringConstructor(String source, Float lower, Float upper, boolean lower_inc, boolean upper_inc){
        FloatSpan floatsp = new FloatSpan(source);
        assert_floatSpan_equality(floatsp,lower,upper,lower_inc,upper_inc);
    }

    @Test
    public void testStrStrConstructor(){
        String input_lower = "2.5" ;
        String input_upper = "5.21";
        Float lower = 2.5f;
        Float upper = 5.21f;
        FloatSpan floatSpan1 = new FloatSpan(input_lower,input_upper,true,false);
        assert_floatSpan_equality(floatSpan1,lower,upper,true,false);
    }

    @Test
    public void testIntIntConstructor(){
        float input_lower = 2.5f;
        float input_upper = 5.21f;
        Float lower = 2.5f;
        Float upper = 5.21f;
        FloatSpan floatSpan1 = new FloatSpan(input_lower,input_upper,true,false);
        assert_floatSpan_equality(floatSpan1,lower,upper,true,false);
    }

    @Test
    public void testIntStrConstructor(){
        float input_lower = 2.5f;
        String input_upper = "5.21";
        float lower = 2.5f;
        float upper = 5.21f;
        FloatSpan floatSpan1 = new FloatSpan(input_lower,input_upper,false,true);
        assert_floatSpan_equality(floatSpan1,lower,upper,false,true);
    }

    @Test
    public void testConstructorBoundInclusivityDefaults(){
        FloatSpan floatSpan1 = new FloatSpan("2.5","5.21",true,false);
        assert_floatSpan_equality(floatSpan1,null,null,true,false);
    }

    @ParameterizedTest(name = "Test Constructor Bound method")
    @MethodSource("Bound_sources")
    public void testConstructorBoundInclusivity(boolean lower, boolean upper){
        FloatSpan floatSpan1 = new FloatSpan("2.5","5.21",lower,upper);
        assert_floatSpan_equality(floatSpan1,null,null,lower,upper);
    }


//    @Test
//    public void testCopyConstructor(){
//        FloatSpan tt = floatSpan.copy();
//        assertNotEquals(tt,floatSpan);
//        assertEquals(tt.toString(15),floatSpan.toString(15));
//    }


    @Test
    public void testIntSpanOutput(){
        assertEquals("(2.5, 5.21]",floatSpan.toString(15));
    }

    @Test
    public void testIntSpanConversions(){
        FloatSpanSet floatSpanSet = floatSpan.to_spanset();
        assertInstanceOf(FloatSpanSet.class, floatSpanSet);
        assertEquals(floatSpanSet.num_spans(), 1);
    }

    @Test
    public void testIntSpanAccessors(){
        FloatSpan floatSpan2  = new FloatSpan("[3.5, 11.23]");
        assertEquals(floatSpan.lower(),2.5f);
        assertEquals(floatSpan2.lower(),3.5f);
        assertEquals(floatSpan.upper(),5.21f);
        assertEquals(floatSpan2.upper(),11.23f);
        assertFalse(floatSpan.lower_inc());
        assertTrue(floatSpan2.lower_inc());
        assertTrue(floatSpan.upper_inc());
        assertTrue(floatSpan2.upper_inc());
        assertEquals((float)floatSpan.width(),2.71f);
        assertEquals((float)floatSpan2.width(),7.73f);
//        assertEquals(intspan.hash(),1519224342);
    }


    @Test
    public void IntSpanTopologicalPositionFunctions() throws Exception {
        float value = 2.5f;
        FloatSpan floatSpan2 = new FloatSpan("(5.21, 21.5]");
        FloatSpan floatSpan3 = new FloatSpan("[2.5, 21.5]");
        FloatSpanSet floatSpanSet = new FloatSpanSet("{(1.1, 5.5], (11.23, 41.56]}");

        assertTrue(floatSpan.is_adjacent(value));
        assertTrue(floatSpan.is_adjacent(floatSpan2));
        assertFalse(floatSpan.is_adjacent(floatSpanSet));

        assertTrue(floatSpan.is_contained_in(floatSpan3));
        assertTrue(floatSpan.is_contained_in(floatSpanSet));

        assertFalse(floatSpan.contains(value));
        assertFalse(floatSpan.contains(floatSpan2));
        assertFalse(floatSpan.contains(floatSpanSet));

        assertTrue(floatSpan.overlaps(floatSpan3));
        assertTrue(floatSpan.overlaps(floatSpanSet));

        assertFalse(floatSpan.is_same(value));
        assertFalse(floatSpan.is_same(floatSpan2));
        assertFalse(floatSpan.is_same(floatSpanSet));

        assertFalse(floatSpan.is_left(value));
        assertTrue(floatSpan.is_left(floatSpan2));
        assertFalse(floatSpan.is_left(floatSpanSet));

        assertFalse(floatSpan.is_over_or_left(value));
        assertTrue(floatSpan.is_over_or_left(floatSpan2));
        assertTrue(floatSpan.is_over_or_left(floatSpanSet));

        assertTrue(floatSpan.is_right(value));
        assertFalse(floatSpan.is_right(floatSpan2));
        assertFalse(floatSpan.is_right(floatSpanSet));

        assertTrue(floatSpan.is_over_or_right(value));
        assertFalse(floatSpan.is_over_or_right(floatSpan2));
        assertTrue(floatSpan.is_over_or_right(floatSpanSet));
    }

    @Test
    public void testIntSpanSetFunctions() throws Exception {
        Double value = 1.3;
        FloatSpan floatSpan2 = new FloatSpan("(1.1, 20.23)");
        FloatSpanSet floatSpanSet = new FloatSpanSet("{(1.1, 20.23), (31.23, 41.56)}");

        floatSpan.union(value);
        floatSpan.union(floatSpan2);
        floatSpan.union(floatSpanSet);

        floatSpan.minus(value);
        floatSpan.minus(floatSpan2);
        floatSpan.minus(floatSpanSet);

    }

    @Test
    public void testIntSpanComparisons() throws Exception {
        FloatSpan floatSpan2 = new FloatSpan("(1.1, 20.23)");
        FloatSpan other = new FloatSpan("[5.2, 21.35)");


        assertFalse(floatSpan2.eq(other));
        assertTrue(floatSpan2.notEquals(other));
        assertTrue(floatSpan2.lessThan(other));
        assertTrue(floatSpan2.lessThanOrEqual(other));
        assertTrue(floatSpan2.greaterThan(other));
        assertFalse(floatSpan2.greaterThanOrEqual(other));
    }

}
