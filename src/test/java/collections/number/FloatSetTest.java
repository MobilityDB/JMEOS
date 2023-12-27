package collections.number;


import functions.functions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.collections.number.FloatSet;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class FloatSetTest {
    public FloatSet floatset = new FloatSet("{1, 2, 3}");
    public float value = 5.0f ;
    public FloatSet other = new FloatSet("{5, 10}");
    public FloatSet other2 = new FloatSet("{1, 10}");


    static Stream<Arguments> FloatSet_sources() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(5.0f, false ),
                Arguments.of(new FloatSet("{5, 10}"), false )
          );
    }

    static Stream<Arguments> FloatSet_distances() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(5.0f, 2.0f ),
                Arguments.of(new FloatSet("{5, 10}"), 2.0f )
        );
    }

    public static void assertIntsetEquality(FloatSet floatset, List<Integer> values){
        assertEquals(floatset.num_elements(),values.size());
    }

    @Test
    public void testStringConstructor(){
        assertIntsetEquality(floatset, List.of(1,2,3));
    }

    @Test
    public void testListConstructor(){}

    @Test
    public void testToString(){
        String[] array = {"1", "2", "3"};
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        Arrays.stream(array).map(s -> "\"" + s + "\"").forEach(joiner::add);
        String result = joiner.toString();
        assertEquals(floatset.toString(15),"{1, 2, 3}");
    }

    /*
    @Test
    public void testToSpan(){
        FloatSpan tmp = new FloatSpan("[1, 3]");
        assertEquals(this.floatset.to_span().toString(15), tmp.toString(15));
    }

     */

    @Test
    public void testNumElements(){
        assertEquals(3,floatset.num_elements());
    }

    @Test
    public void testStartElements(){
        assertEquals(1.0f,floatset.start_element());
    }

    @Test
    public void testEndElements(){
        assertEquals(3.0f,floatset.end_element());
    }

    @Test
    public void testElementN() throws Exception {
        assertEquals(2.0f,floatset.element_n(2));
    }

    @Test
    public void testHash() throws Exception {
        assertEquals(2419122126l, floatset.hash());
    }


    @Test
    public void testIsContainedIn() throws Exception {
        assertFalse(this.floatset.is_contained_in(other));
    }


    @ParameterizedTest(name = "Test contains method")
    @MethodSource("FloatSet_sources")
    public void testContains(Object arg,boolean result) throws Exception {
        assertEquals(this.floatset.contains(arg),result);
    }

    @Test
    public void testOverlaps() throws Exception {
        assertFalse(this.floatset.overlaps(other));
    }

    @ParameterizedTest(name = "Test Is left method")
    @MethodSource("FloatSet_sources")
    public void testIsLeft(Object arg,boolean result) throws Exception {
        assertTrue(this.floatset.is_left(arg));
    }


    @ParameterizedTest(name = "Test Is Over Or left method")
    @MethodSource("FloatSet_sources")
    public void testIsOverOrLeft(Object arg,boolean result) throws Exception {
        assertTrue(this.floatset.is_over_or_left(arg));
    }

    @ParameterizedTest(name = "Test Is Right method")
    @MethodSource("FloatSet_sources")
    public void testIsRight(Object arg,boolean result) throws Exception {
        assertFalse(this.floatset.is_right(arg));
    }


    @ParameterizedTest(name = "Test Is Over Or left method")
    @MethodSource("FloatSet_sources")
    public void testIsOverOrRight(Object arg,boolean result) throws Exception {
        assertFalse(this.floatset.is_over_or_right(arg));
    }


    @ParameterizedTest(name = "Test Distance method")
    @MethodSource("FloatSet_distances")
    public void testDistance(Object arg, float result) throws Exception {
        assertEquals(this.floatset.distance(arg),result);
    }

    @Test
    public void testIntersection() throws Exception {
        FloatSet fl = new FloatSet("{1}");
        assertEquals(this.floatset.intersection(this.other2).toString(15),fl.toString(15));
    }

    @Test
    public void testUnion() throws Exception {
        FloatSet fl = new FloatSet("{1, 2, 3, 10}");
        assertEquals(this.floatset.union(this.other2).toString(15),fl.toString(15));
    }

    @Test
    public void testMinus() throws Exception {
        FloatSet fl = new FloatSet("{2, 3}");
        assertEquals(this.floatset.minus(this.other2).toString(15),fl.toString(15));
    }

    @Test
    public void testComparisons() throws Exception {
        assertFalse(floatset.eq(other));
        assertTrue(floatset.notEquals(other));
        assertTrue(floatset.lessThan(other));
        assertTrue(floatset.lessThanOrEqual(other));
        assertTrue(floatset.greaterThan(other));
        assertFalse(floatset.greaterThanOrEqual(other));
    }

}
