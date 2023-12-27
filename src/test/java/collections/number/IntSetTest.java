package collections.number;

import functions.functions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.collections.number.IntSet;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class IntSetTest {

    public IntSet intset = new IntSet("{1, 2, 3}");
    public int value = 5;
    public IntSet other = new IntSet("{5, 10}");
    public IntSet other2 = new IntSet("{1, 10}");


    static Stream<Arguments> IntSet_sources() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(5, false ),
                Arguments.of(new IntSet("{5, 10}"), false )
        );
    }

    static Stream<Arguments> IntSet_distances() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(5, 2 ),
                Arguments.of(new IntSet("{5, 10}"), 2 )
        );
    }

    public static void assertIntsetEquality(IntSet intset, List<Integer> values){
        assertEquals(intset.num_elements(),values.size());
    }

    @Test
    public void testStringConstructor(){
        assertIntsetEquality(intset, List.of(1,2,3));
    }

    @Test
    public void testListConstructor(){}

    @Test
    public void testToString(){
        assertEquals(intset.toString(),"{1, 2, 3}");
    }

    /*
    @Test
    public void testToSpan(){
        IntSpan tmp = new IntSpan("[1, 3]");
        assertEquals(this.intset.to_span().toString(), tmp.toString());
    }

     */

    @Test
    public void testNumElements(){
        assertEquals(3, intset.num_elements());
    }

    @Test
    public void testStartElements(){
        assertEquals(1, intset.start_element());
    }

    @Test
    public void testEndElements(){
        assertEquals(3, intset.end_element());
    }

    /*
    @Test
    public void testElementN() throws Exception {
        assertEquals(2, intset.element_n(2));
    }

     */

    @Test
    public void testHash() throws Exception {
        assertEquals(3969573766l, intset.hash());
    }


    @Test
    public void testIsContainedIn() throws Exception {
        assertFalse(this.intset.is_contained_in(other));
    }


    @ParameterizedTest(name = "Test contains method")
    @MethodSource("IntSet_sources")
    public void testContains(Object arg,boolean result) throws Exception {
        assertEquals(this.intset.contains(arg),result);
    }

    @Test
    public void testOverlaps() throws Exception {
        assertFalse(this.intset.overlaps(other));
    }

    @ParameterizedTest(name = "Test Is left method")
    @MethodSource("IntSet_sources")
    public void testIsLeft(Object arg,boolean result) throws Exception {
        assertTrue(this.intset.is_left(arg));
    }


    @ParameterizedTest(name = "Test Is Over Or left method")
    @MethodSource("IntSet_sources")
    public void testIsOverOrLeft(Object arg,boolean result) throws Exception {
        assertTrue(this.intset.is_over_or_left(arg));
    }

    @ParameterizedTest(name = "Test Is Right method")
    @MethodSource("IntSet_sources")
    public void testIsRight(Object arg,boolean result) throws Exception {
        assertFalse(this.intset.is_right(arg));
    }


    @ParameterizedTest(name = "Test Is Over Or left method")
    @MethodSource("IntSet_sources")
    public void testIsOverOrRight(Object arg,boolean result) throws Exception {
        assertFalse(this.intset.is_over_or_right(arg));
    }


    @ParameterizedTest(name = "Test Distance method")
    @MethodSource("IntSet_distances")
    public void testDistance(Object arg, float result) throws Exception {
        assertEquals(this.intset.distance(arg),result);
    }

    @Test
    public void testIntersection() throws Exception {
        IntSet fl = new IntSet("{1}");
        assertEquals(this.intset.intersection(this.other2).toString(),fl.toString());
    }

    @Test
    public void testUnion() throws Exception {
        IntSet fl = new IntSet("{1, 2, 3, 10}");
        assertEquals(this.intset.union(this.other2).toString(),fl.toString());
    }

    @Test
    public void testMinus() throws Exception {
        IntSet fl = new IntSet("{2, 3}");
        assertEquals(this.intset.minus(this.other2).toString(),fl.toString());
    }

    @Test
    public void testComparisons() throws Exception {
        assertFalse(intset.eq(other));
        assertTrue(intset.notEquals(other));
        assertTrue(intset.lessThan(other));
        assertTrue(intset.lessThanOrEqual(other));
        assertTrue(intset.greaterThan(other));
        assertFalse(intset.greaterThanOrEqual(other));
    }
}
