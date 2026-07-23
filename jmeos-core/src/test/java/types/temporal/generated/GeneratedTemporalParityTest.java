package types.temporal.generated;

import functions.GeneratedFunctions;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Test;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.temporal.Temporal;
import types.temporal.TemporalType;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Proves the generated {@link GeneratedTemporal} surface is wired correctly and behaves like the hand
 * layer it will replace. Every generated method is checked against a direct call to the underlying
 * {@code GeneratedFunctions} wrapper (correct function, receiver and result wrapping); the methods that
 * already have a hand twin are additionally checked against it.
 *
 * <p>The surface is exercised through an adapter over a hand {@link Temporal}, so the test needs no
 * change to the hand classes — the generated interface only requires the three contract methods the
 * hand layer already exposes.
 */
public class GeneratedTemporalParityTest {

    /** Wraps a hand temporal as the generated surface, supplying the three contract methods. */
    private static GeneratedTemporal gen(Temporal t) {
        return new GeneratedTemporal() {
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
    void scalarAndTemporalAccessorsMatchTheLibrary() {
        TFloatSeqSet ss = new TFloatSeqSet(
                "{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}");
        Pointer p = ss.getInner();
        GeneratedTemporal g = gen(ss);

        // Scalar and string accessors delegate straight to the wrapper.
        assertEquals(GeneratedFunctions.temporal_num_instants(p), g.numInstants());
        assertEquals(GeneratedFunctions.temporal_num_timestamps(p), g.numTimestamps());
        assertEquals(GeneratedFunctions.temporal_num_sequences(p), g.numSequences());
        assertEquals(GeneratedFunctions.temporal_hash(p), g.hash());
        assertEquals(GeneratedFunctions.temporal_hash_extended(p, 42L), g.hashExtended(42L));
        assertEquals(GeneratedFunctions.temporal_interp(p), g.interp());
        assertEquals(GeneratedFunctions.temporal_subtype(p), g.subtype());
        assertEquals(GeneratedFunctions.temporal_basetype_name(p), g.basetypeName());
        assertEquals(GeneratedFunctions.temporal_start_timestamptz(p), g.startTimestamptz());
        assertEquals(GeneratedFunctions.temporal_end_timestamptz(p), g.endTimestamptz());

        // Temporal-returning accessors are wrapped back into the object layer; compare by content.
        assertEquals(id(GeneratedFunctions.temporal_start_instant(p)), id(g.startInstant()));
        assertEquals(id(GeneratedFunctions.temporal_end_instant(p)), id(g.endInstant()));
        assertEquals(id(GeneratedFunctions.temporal_min_instant(p)), id(g.minInstant()));
        assertEquals(id(GeneratedFunctions.temporal_max_instant(p)), id(g.maxInstant()));
        assertEquals(id(GeneratedFunctions.temporal_start_sequence(p)), id(g.startSequence()));
        assertEquals(id(GeneratedFunctions.temporal_end_sequence(p)), id(g.endSequence()));
        assertEquals(id(GeneratedFunctions.temporal_round(p, 2)), id(g.round(2)));
    }

    @Test
    void sequenceOnlyAccessorsMatchTheLibrary() {
        TFloatSeq sq = new TFloatSeq("[1@2019-09-01, 2@2019-09-02, 4@2019-09-04]");
        Pointer p = sq.getInner();
        GeneratedTemporal g = gen(sq);

        assertEquals(GeneratedFunctions.temporal_lower_inc(p), g.lowerInc());
        assertEquals(GeneratedFunctions.temporal_upper_inc(p), g.upperInc());
        assertEquals(id(GeneratedFunctions.temporal_derivative(p)), id(g.derivative()));
        assertEquals(id(GeneratedFunctions.temporal_simplify_dp(p, 1.0, false)),
                id(g.simplifyDp(1.0, false)));
        assertEquals(id(GeneratedFunctions.temporal_simplify_max_dist(p, 1.0, false)),
                id(g.simplifyMaxDist(1.0, false)));
        assertEquals(id(GeneratedFunctions.temporal_simplify_min_dist(p, 1.0)),
                id(g.simplifyMinDist(1.0)));
        assertEquals(id(GeneratedFunctions.temporal_ext_kalman_filter(p, 1.0, 1.0, 1.0, false)),
                id(g.extKalmanFilter(1.0, 1.0, 1.0, false)));
    }

    @Test
    void instantConversionMatchesTheLibrary() {
        TFloatInst inst = new TFloatInst("1.5@2019-09-01");
        Pointer p = inst.getInner();
        GeneratedTemporal g = gen(inst);

        assertEquals(id(GeneratedFunctions.temporal_as_tinstant(p)), id(g.asTinstant()));
    }

    @Test
    void handTwinsAgreeWithTheGeneratedSurface() {
        TFloatSeqSet ss = new TFloatSeqSet(
                "{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}");
        GeneratedTemporal g = gen(ss);

        assertEquals(ss.num_instants(), g.numInstants());
        assertEquals(ss.num_timestamps(), g.numTimestamps());
        assertEquals(ss.hash(), g.hash());
        assertEquals(id(ss.start_instant()), id(g.startInstant()));
        assertEquals(id(ss.end_instant()), id(g.endInstant()));
        assertEquals(id(ss.min_instant()), id(g.minInstant()));
        assertEquals(id(ss.max_instant()), id(g.maxInstant()));
    }
}
