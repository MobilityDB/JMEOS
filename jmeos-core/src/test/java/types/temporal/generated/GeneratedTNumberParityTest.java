package types.temporal.generated;

import functions.GeneratedFunctions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import org.junit.jupiter.api.Test;
import types.basic.tfloat.TFloatSeq;
import types.boxes.TBox;
import types.collections.number.FloatSpan;
import types.collections.number.FloatSpanSet;
import types.temporal.Temporal;
import types.temporal.TemporalType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Proves the generated {@link GeneratedTNumber} surface delegates correctly, each method checked against
 * a direct call to the underlying {@code GeneratedFunctions} wrapper.
 *
 * <p>The surface is exercised through an adapter over a hand {@link TFloatSeq}, so the test needs no
 * change to the hand classes. {@code GeneratedTNumber} extends {@code GeneratedTemporal}, so the adapter
 * supplies the same three contract methods.
 */
public class GeneratedTNumberParityTest {

    /** Wraps a hand temporal as the generated number surface, supplying the three contract methods. */
    private static GeneratedTNumber gen(Temporal t) {
        return new GeneratedTNumber() {
            @Override public Pointer getInner() { return t.getInner(); }
            @Override public String getCustomType() { return t.getCustomType(); }
            @Override public TemporalType getTemporalType() { return t.getTemporalType(); }
        };
    }

    /** Identity of a temporal value, used to compare temporal results by content. */
    private static long id(Pointer inner) {
        return Integer.toUnsignedLong(GeneratedFunctions.temporal_hash(inner));
    }

    private static long id(Temporal t) {
        return id(t.getInner());
    }

    @Test
    void scalarTemporalAndBoxAccessorsMatchTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 3@2019-09-02, 2@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTNumber g = gen(a);

        // Scalar accessors delegate straight to the wrapper.
        assertEquals(GeneratedFunctions.tnumber_integral(p), g.integral());
        assertEquals(GeneratedFunctions.tnumber_twavg(p), g.twavg());

        // Temporal-returning accessors are wrapped back into the object layer; compare by content.
        assertEquals(id(GeneratedFunctions.tnumber_abs(p)), id(g.abs()));
        assertEquals(id(GeneratedFunctions.tnumber_trend(p)), id(g.trend()));
        assertEquals(id(GeneratedFunctions.tnumber_angular_difference(p)), id(g.angularDifference()));
        assertEquals(id(GeneratedFunctions.tnumber_delta_value(p)), id(g.deltaValue()));

        // The value-and-time bounding box is wrapped as a TBox; compare its output text.
        assertEquals(GeneratedFunctions.tbox_out(GeneratedFunctions.tnumber_to_tbox(p), 15),
                GeneratedFunctions.tbox_out(g.toTbox().get_inner(), 15));
    }

    @Test
    void boxAndValueSpanRestrictionsMatchTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 3@2019-09-02, 2@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTNumber g = gen(a);

        // TBox restriction over a value sub-box, so both at and minus keep a non-empty result.
        TBox box = new TBox("TBOXFLOAT X([1.5, 2.5])");
        assertEquals(id(GeneratedFunctions.tnumber_at_tbox(p, box.get_inner())), id(g.atTbox(box)));
        assertEquals(id(GeneratedFunctions.tnumber_minus_tbox(p, box.get_inner())), id(g.minusTbox(box)));

        // Value-span restriction over the float value domain (a base span, not a time span).
        FloatSpan span = new FloatSpan("[1.5, 2.5]");
        assertEquals(id(GeneratedFunctions.tnumber_at_span(p, span.get_inner())), id(g.atSpan(span)));
        assertEquals(id(GeneratedFunctions.tnumber_minus_span(p, span.get_inner())), id(g.minusSpan(span)));

        FloatSpanSet spanset = new FloatSpanSet("{[1.5, 2.5], [2.8, 3.2]}");
        assertEquals(id(GeneratedFunctions.tnumber_at_spanset(p, spanset.get_inner())), id(g.atSpanset(spanset)));
        assertEquals(id(GeneratedFunctions.tnumber_minus_spanset(p, spanset.get_inner())),
                id(g.minusSpanset(spanset)));
    }

    @Test
    void tboxArraysMatchTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 3@2019-09-02, 2@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTNumber g = gen(a);
        Runtime rt = Runtime.getSystemRuntime();
        int tboxBytes = 56; // sizeof(TBox) on the 64-bit targets: two 24-byte Spans plus a padded int16

        // tboxes() folds the contiguous TBox array into TBox wrappers; compare each to the raw element.
        Pointer c = Memory.allocate(rt, Integer.BYTES);
        Pointer arr = GeneratedFunctions.tnumber_tboxes(p, c);
        List<TBox> boxes = g.tboxes();
        assertEquals(c.getInt(0), boxes.size());
        for (int i = 0; i < boxes.size(); i++) {
            assertEquals(GeneratedFunctions.tbox_out(arr.slice((long) i * tboxBytes), 15),
                    GeneratedFunctions.tbox_out(boxes.get(i).get_inner(), 15));
        }

        // The split-into-N and split-each-N variants fold the same way.
        Pointer c2 = Memory.allocate(rt, Integer.BYTES);
        GeneratedFunctions.tnumber_split_n_tboxes(p, 2, c2);
        assertEquals(c2.getInt(0), g.splitNTboxes(2).size());

        Pointer c3 = Memory.allocate(rt, Integer.BYTES);
        GeneratedFunctions.tnumber_split_each_n_tboxes(p, 2, c3);
        assertEquals(c3.getInt(0), g.splitEachNTboxes(2).size());
    }
}
