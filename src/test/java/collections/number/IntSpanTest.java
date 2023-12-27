package collections.number;

import functions.functions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.collections.number.IntSpan;
import types.collections.number.IntSpanSet;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntSpanTest {
    public IntSpan intspan = new IntSpan("[7, 10)");



    static Stream<Arguments> IntSpan_sources() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of("(7, 10)", 8, 10, true, false),
                Arguments.of("[7, 10]", 7, 11, true, false)
        );
    }

    static Stream<Arguments> IntSpan_mulsources() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of("7", "10", 7, 10),
                Arguments.of(7, 10, 7, 10),
                Arguments.of(7, "10", 7, 10)
        );
    }

    static Stream<Arguments> Bound_sources() throws SQLException {
        functions.meos_initialize("UTC");
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

    @ParameterizedTest(name = "Test Constructor method")
    @MethodSource("IntSpan_sources")
    public void testStringConstructor(String source, int lower, int upper, boolean lower_inc, boolean upper_inc){
        IntSpan intsp = new IntSpan(source);
        assert_intspan_equality(intsp,lower,upper,lower_inc,upper_inc);
    }

    @Test
    public void testStrStrConstructor(){
        String input_lower = "7" ;
        String input_upper = "10";
        int lower = 7;
        int upper = 10;
        IntSpan intsp = new IntSpan(input_lower,input_upper,true,false);
        assert_intspan_equality(intsp,lower,upper,true,false);
    }

    @Test
    public void testIntIntConstructor(){
        int input_lower = 7 ;
        int input_upper = 10;
        int lower = 7;
        int upper = 10;
        IntSpan intsp = new IntSpan(input_lower,input_upper,true,false);
        assert_intspan_equality(intsp,lower,upper,true,false);
    }

    @Test
    public void testIntStrConstructor(){
        int input_lower = 7 ;
        String input_upper = "10";
        int lower = 7;
        int upper = 10;
        IntSpan intsp = new IntSpan(input_lower,input_upper,true,false);
        assert_intspan_equality(intsp,lower,upper,true,false);
    }

    @Test
    public void testConstructorBoundInclusivityDefaults(){
        IntSpan intsp = new IntSpan("7","10",true,false);
        assert_intspan_equality(intsp,null,null,true,false);
    }

    @ParameterizedTest(name = "Test Constructor Bound method")
    @MethodSource("Bound_sources")
    public void testConstructorBoundInclusivity(boolean lower, boolean upper){
        IntSpan intsp = new IntSpan("7","10",lower,upper);
        assert_intspan_equality(intsp,null,null,true,false);
    }


    @Test
    public void testCopyConstructor(){
        IntSpan tt = intspan.copy();
        assertNotEquals(tt,intspan);
        assertEquals(tt.toString(),intspan.toString());
    }


    @Test
    public void testIntSpanOutput(){
        assertEquals("[7, 10)",intspan.toString());
    }

    /*
    @Test
    public void testIntSpanConversions(){
        IntSpanSet intset = intspan.to_spanset();
        assertTrue(intset instanceof IntSpanSet);
        assertEquals(intset.num_spans(), 1);
    }

     */

    @Test
    public void testIntSpanAccessors(){
        IntSpan intspan2 = new IntSpan("[8, 11]");
        assertEquals(intspan.lower(),7);
        assertEquals(intspan2.lower(),8);
        assertEquals(intspan.upper(),10);
        assertEquals(intspan2.upper(),12);
        assertTrue(intspan.lower_inc());
        assertTrue(intspan2.lower_inc());
        assertFalse(intspan.upper_inc());
        assertFalse(intspan2.upper_inc());
        assertEquals(intspan.width(),3);
        assertEquals(intspan2.width(),4);
        assertEquals(intspan.hash(),1519224342);

    }


    @Test
    public void IntSpanTopologicalPositionFunctions() throws Exception {
        int value = 5 ;
        IntSpan intspan2 = new IntSpan("(1, 20)");
        IntSpanSet intspanset = new IntSpanSet("{(1, 20), (31, 41)}");

        assertTrue(intspan.is_adjacent(value));
        assertTrue(intspan.is_adjacent(intspan2));
        assertFalse(intspan.is_adjacent(intspanset));

        assertTrue(intspan.is_contained_in(intspan2));
        assertTrue(intspan.is_contained_in(intspanset));

        assertFalse(intspan.contains(value));
        assertFalse(intspan.contains(intspan2));
        assertFalse(intspan.contains(intspanset));

        assertTrue(intspan.overlaps(intspan2));
        assertTrue(intspan.overlaps(intspanset));

        assertFalse(intspan.is_same(value));
        assertFalse(intspan.is_same(intspan2));
        assertFalse(intspan.is_same(intspanset));

        assertFalse(intspan.is_left(value));
        assertFalse(intspan.is_left(intspan2));
        assertFalse(intspan.is_left(intspanset));

        assertFalse(intspan.is_over_or_left(value));
        assertTrue(intspan.is_over_or_left(intspan2));
        assertTrue(intspan.is_over_or_left(intspanset));

        assertTrue(intspan.is_right(value));
        assertFalse(intspan.is_right(intspan2));
        assertFalse(intspan.is_right(intspanset));

        assertTrue(intspan.is_over_or_right(value));
        assertTrue(intspan.is_over_or_right(intspan2));
        assertTrue(intspan.is_over_or_right(intspanset));


    }

    @Test
    public void testIntSpanSetFunctions() throws Exception {
        int value = 1;
        IntSpan intspan2 = new IntSpan("(1, 20)");
        IntSpanSet intspanset = new IntSpanSet("{(1, 20), (31, 41)}");

        intspan.union(value);
        intspan.union(intspan2);
        intspan.union(intspanset);

        intspan.minus(value);
        intspan.minus(intspan2);
        intspan.minus(intspanset);

    }

    @Test
    public void testIntSpanComparisons() throws Exception {
        IntSpan intspan2 = new IntSpan("(1, 20)");
        IntSpan other = new IntSpan("[5, 20)");


        assertFalse(intspan2.eq(other));
        assertTrue(intspan2.notEquals(other));
        assertTrue(intspan2.lessThan(other));
        assertTrue(intspan2.lessThanOrEqual(other));
        assertTrue(intspan2.greaterThan(other));
        assertFalse(intspan2.greaterThanOrEqual(other));
    }

}
