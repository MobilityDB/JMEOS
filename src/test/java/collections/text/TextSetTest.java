package collections.text;

import org.junit.jupiter.api.Test;
import types.collections.text.TextSet;
import static org.junit.jupiter.api.Assertions.*;
import functions.functions;

import java.util.List;
import java.util.Arrays;
import java.util.StringJoiner;

public class TextSetTest {
    public TextSet tset = new TextSet("{A, BB, ccc}");
    public TextSet other = new TextSet("{2020-01-02 00:00:00+0, 2020-03-31 00:00:00+0}");
    public TextSet other2 = new TextSet("{2020-05-02 00:00:00+0, 2020-08-28 00:00:00+0}");

    public static void assertTextsetEquality(TextSet tset, List<String> elements){
        assertEquals(tset.num_elements(),elements.size());
        //assertEquals(set.element(),elements);
    }

    @Test
    public void testStringConstructor(){
        //TextSet ttset = new TextSet("{A, BB, ccc}");
        assertTextsetEquality(tset, List.of("Hello", "World", "Java"));
    }

    @Test
    public void testAsHex(){
        TextSet tset = new TextSet("{A, BB, ccc}");
        String pr = tset.as_hexwkb();
        TextSet ttp = new TextSet(functions.set_from_hexwkb(pr));
    }



    @Test
    public void testListConstructor(){}

    @Test
    public void testToString(){
        String[] array = {"A", "BB", "ccc"};
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        Arrays.stream(array).map(s -> "\"" + s + "\"").forEach(joiner::add);
        String result = joiner.toString();
        assertEquals(tset.toString(),result);
    }


    @Test
    public void testNumElements(){
        assertEquals(3,tset.num_elements());
    }

    @Test
    public void testStartElements(){
        assertEquals("A",tset.start_element());
    }

    @Test
    public void testEndElements(){
        assertEquals("ccc",tset.end_element());
    }

    @Test
    public void testElementN() throws Exception {
        assertEquals("BB",tset.element_n(2));
    }

    @Test
    public void testHash() throws Exception {
        assertEquals(3145376687l, tset.hash());
    }

    @Test
    public void testComparisons() throws Exception {
        assertFalse(other2.eq(other));
        assertTrue(other2.notEquals(other));
        assertFalse(other2.lessThan(other));
        assertFalse(other2.lessThanOrEqual(other));
        assertTrue(other2.greaterThan(other));
        assertTrue(other2.greaterThanOrEqual(other));
    }


    @Test
    public void testIntersection() {
        assertEquals(tset.intersection(new TextSet("{A}")).toString(),"{\"A\"}");
        assertEquals(tset.intersection(new TextSet("{a, BB, ccc}")).toString(),"{\"BB\", \"ccc\"}");
    }


    @Test
    public void testUnion() {
        assertEquals(tset.union(new TextSet("{A}")).toString(),"{\"A\", \"BB\", \"ccc\"}");
        assertEquals(tset.union(new TextSet("{a, BB, ccc}")).toString(),"{\"A\", \"BB\", \"a\", \"ccc\"}");
    }


}
