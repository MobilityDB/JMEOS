/*****************************************************************************
 *
 * This MobilityDB code is provided under The PostgreSQL License.
 * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB
 * contributors
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without a written
 * agreement is hereby granted, provided that the above copyright notice and
 * this paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON
 * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *****************************************************************************/

package collections.text;

import functions.GeneratedFunctions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import types.collections.text.TextSet;
import static org.junit.jupiter.api.Assertions.*;
import utils.TestLogger;

import java.util.List;
import java.util.Arrays;
import java.util.StringJoiner;

@ExtendWith(TestLogger.class)
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
        TextSet ttp = new TextSet(GeneratedFunctions.set_from_hexwkb(pr));
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

//    @Test
//    public void testHash() throws Exception {
//        assertEquals(3145376687l, tset.hash());
//    }

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
    public void testUnion() throws Exception {
        assertEquals(tset.union(new TextSet("{A}")).toString(),"{\"A\", \"BB\", \"ccc\"}");
        assertEquals(tset.union(new TextSet("{a, BB, ccc}")).toString(),"{\"A\", \"BB\", \"a\", \"ccc\"}");
    }

    /** A set built from a pointer is the set it points to, also to the methods of Set. */
    @Test
    public void testFromPointer(){
        TextSet copy = new TextSet(tset.get_inner());
        assertEquals(tset.toString(), copy.toString());
        assertEquals(tset.num_elements(), copy.num_elements());
        assertArrayEquals(tset.as_wkb(), copy.as_wkb());
    }
}
