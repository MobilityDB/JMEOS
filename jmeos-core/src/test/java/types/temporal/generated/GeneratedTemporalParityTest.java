package types.temporal.generated;

import functions.GeneratedFunctions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import org.junit.jupiter.api.Test;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.collections.number.FloatSet;
import types.collections.time.tstzspan;
import types.temporal.Temporal;
import types.temporal.TemporalType;
import types.temporal.TInterpolation;
import utils.ConversionUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void transformArgumentsAndIntervalReturnMatchTheLibrary() {
        TFloatSeq sq = new TFloatSeq("[1@2019-09-01, 2@2019-09-02, 4@2019-09-04]");
        Pointer p = sq.getInner();
        GeneratedTemporal g = gen(sq);

        Duration d1 = Duration.ofDays(1);
        Duration d2 = Duration.ofDays(2);
        OffsetDateTime origin = OffsetDateTime.parse("2019-09-01T00:00:00Z");

        // Interval return → Duration.
        assertEquals(ConversionUtils.interval_to_timedelta(GeneratedFunctions.temporal_duration(p, false)),
                g.duration(false));

        // Interval arguments → Duration, converted through the shared helper.
        assertEquals(id(GeneratedFunctions.temporal_shift_time(p, ConversionUtils.timedelta_to_interval(d1))),
                id(g.shiftTime(d1)));
        assertEquals(id(GeneratedFunctions.temporal_scale_time(p, ConversionUtils.timedelta_to_interval(d2))),
                id(g.scaleTime(d2)));
        assertEquals(id(GeneratedFunctions.temporal_shift_scale_time(p,
                        ConversionUtils.timedelta_to_interval(d1), ConversionUtils.timedelta_to_interval(d2))),
                id(g.shiftScaleTime(d1, d2)));

        // interpType argument → TInterpolation.
        assertEquals(id(GeneratedFunctions.temporal_set_interp(p, TInterpolation.LINEAR.getValue())),
                id(g.setInterp(TInterpolation.LINEAR)));
        assertEquals(id(GeneratedFunctions.temporal_as_tsequenceset(p, TInterpolation.LINEAR.getValue())),
                id(g.asTsequenceset(TInterpolation.LINEAR)));

        // TimestampTz argument → OffsetDateTime, alongside the interval and interpolation arguments.
        assertEquals(id(GeneratedFunctions.temporal_tsample(p,
                        ConversionUtils.timedelta_to_interval(d1), origin, TInterpolation.LINEAR.getValue())),
                id(g.tsample(d1, origin, TInterpolation.LINEAR)));
    }

    /** The length the count out-parameter reports for one of the array accessors. */
    private static int directCount(Pointer array, Pointer count) {
        return array == null ? 0 : count.getInt(0);
    }

    @Test
    void arrayAccessorsMatchTheLibrary() {
        TFloatSeqSet ss = new TFloatSeqSet(
                "{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}");
        Pointer p = ss.getInner();
        GeneratedTemporal g = gen(ss);
        Runtime rt = Runtime.getSystemRuntime();

        // Object array (TInstant **) folds to List<Temporal>; compare length and element identity.
        Pointer c = Memory.allocate(rt, Integer.BYTES);
        Pointer arr = GeneratedFunctions.temporal_instants(p, c);
        List<Temporal> instants = g.instants();
        assertEquals(directCount(arr, c), instants.size());
        for (int i = 0; i < instants.size(); i++) {
            assertEquals(id(arr.getPointer((long) i * Long.BYTES)), id(instants.get(i)));
        }

        Pointer c2 = Memory.allocate(rt, Integer.BYTES);
        assertEquals(directCount(GeneratedFunctions.temporal_segments(p, c2), c2), g.segments().size());
        Pointer c3 = Memory.allocate(rt, Integer.BYTES);
        assertEquals(directCount(GeneratedFunctions.temporal_sequences(p, c3), c3), g.sequences().size());

        // Scalar array (TimestampTz *) folds to List<OffsetDateTime>; the values are the converted int64s.
        Pointer c4 = Memory.allocate(rt, Integer.BYTES);
        Pointer tsArr = GeneratedFunctions.temporal_timestamps(p, c4);
        List<OffsetDateTime> timestamps = g.timestamps();
        assertEquals(directCount(tsArr, c4), timestamps.size());
        for (int i = 0; i < timestamps.size(); i++) {
            assertEquals(utils.TimestampTzConverter.toOffsetDateTime(tsArr.getLong((long) i * Long.BYTES)),
                    timestamps.get(i));
        }
    }

    @Test
    void objectArgumentsMatchTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 2@2019-09-02]");
        TFloatSeq b = new TFloatSeq("[3@2019-09-03, 4@2019-09-04]");
        TFloatInst inst = new TFloatInst("5@2019-09-05");
        Pointer pa = a.getInner();
        GeneratedTemporal g = gen(a);

        // A temporal object argument is forwarded as its inner pointer.
        assertEquals(id(GeneratedFunctions.temporal_merge(pa, b.getInner())), id(g.merge(b)));
        assertEquals(GeneratedFunctions.temporal_frechet_distance(pa, b.getInner()), g.frechetDistance(b));
        assertEquals(id(GeneratedFunctions.temporal_insert(pa, b.getInner(), true)), id(g.insert(b, true)));
        assertEquals(id(GeneratedFunctions.temporal_append_tsequence(pa, b.getInner(), false)),
                id(g.appendTsequence(b, false)));

        // appendTinstant mixes an object, an interpolation, an interval and scalar arguments.
        assertEquals(
                id(GeneratedFunctions.temporal_append_tinstant(pa, inst.getInner(),
                        TInterpolation.LINEAR.getValue(), 0.0,
                        ConversionUtils.timedelta_to_interval(Duration.ofDays(1)), false)),
                id(g.appendTinstant(inst, TInterpolation.LINEAR, 0.0, Duration.ofDays(1), false)));
    }

    @Test
    void timeCollectionsMatchTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 2@2019-09-02, 3@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTemporal g = gen(a);

        // Collection returns are wrapped in the time types; compare their output text to the direct call.
        assertEquals(GeneratedFunctions.span_out(GeneratedFunctions.temporal_to_tstzspan(p), 15),
                GeneratedFunctions.span_out(g.toTstzspan().get_inner(), 15));
        assertEquals(GeneratedFunctions.spanset_out(GeneratedFunctions.temporal_time(p), 15),
                GeneratedFunctions.spanset_out(g.time().get_inner(), 15));

        // A collection argument is forwarded as its inner pointer.
        tstzspan period = new tstzspan("[2019-09-02 00:00:00+00, 2019-09-03 00:00:00+00]");
        assertEquals(id(GeneratedFunctions.temporal_delete_tstzspan(p, period.get_inner(), false)),
                id(g.deleteTstzspan(period, false)));
    }

    @Test
    void comparisonPredicatesMatchTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 2@2019-09-02]");
        TFloatSeq b = new TFloatSeq("[3@2019-09-03, 4@2019-09-04]");
        TFloatSeq aCopy = new TFloatSeq("[1@2019-09-01, 2@2019-09-02]");
        Pointer pa = a.getInner();
        Pointer pb = b.getInner();
        GeneratedTemporal g = gen(a);

        assertEquals(GeneratedFunctions.temporal_cmp(pa, pb), g.cmp(b));
        assertEquals(GeneratedFunctions.temporal_eq(pa, pb), g.eq(b));
        assertEquals(GeneratedFunctions.temporal_ne(pa, pb), g.ne(b));
        assertEquals(GeneratedFunctions.temporal_lt(pa, pb), g.lt(b));
        assertEquals(GeneratedFunctions.temporal_le(pa, pb), g.le(b));
        assertEquals(GeneratedFunctions.temporal_gt(pa, pb), g.gt(b));
        assertEquals(GeneratedFunctions.temporal_ge(pa, pb), g.ge(b));

        // Equal temporals compare equal; ordering is strict against a greater one.
        assertEquals(true, g.eq(aCopy));
        assertEquals(true, g.lt(b));
    }

    @Test
    void restrictionsMatchTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 2@2019-09-02, 3@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTemporal g = gen(a);

        // No-argument and timestamp restrictions.
        assertEquals(id(GeneratedFunctions.temporal_at_max(p)), id(g.atMax()));
        assertEquals(id(GeneratedFunctions.temporal_at_min(p)), id(g.atMin()));
        OffsetDateTime t = OffsetDateTime.parse("2019-09-02T00:00:00Z");
        assertEquals(id(GeneratedFunctions.temporal_at_timestamptz(p, t)), id(g.atTimestamptz(t)));

        // Time-collection restriction (tstz wrapper).
        tstzspan period = new tstzspan("[2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00]");
        assertEquals(id(GeneratedFunctions.temporal_at_tstzspan(p, period.get_inner())),
                id(g.atTstzspan(period)));

        // Value-collection restriction (a value set of floats, marshalled through the generic base Set).
        FloatSet values = new FloatSet("{1, 2}");
        assertEquals(id(GeneratedFunctions.temporal_at_values(p, values.get_inner())),
                id(g.atValues(values)));
        assertEquals(id(GeneratedFunctions.temporal_minus_values(p, values.get_inner())),
                id(g.minusValues(values)));
    }

    @Test
    void outputMatchesTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 2@2019-09-02]");
        Pointer p = a.getInner();
        GeneratedTemporal g = gen(a);

        // The hex-WKB and MF-JSON strings match the direct call; the size_out is folded by the wrapper.
        assertEquals(GeneratedFunctions.temporal_as_hexwkb(p, (byte) 4), g.asHEXWKB((byte) 4));
        assertEquals(GeneratedFunctions.temporal_as_mfjson(p, true, 3, 6, null),
                g.asMFJSON(true, 3, 6, null));
        // The WKB buffer is returned as a raw pointer.
        assertNotNull(g.asWKB((byte) 4));
    }

    @Test
    void indexedAccessorsAndCopyMatchTheLibrary() {
        TFloatSeqSet ss = new TFloatSeqSet(
                "{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}");
        Pointer p = ss.getInner();
        GeneratedTemporal g = gen(ss);

        assertEquals(id(GeneratedFunctions.temporal_copy(p)), id(g.copy()));
        // Zero-based indexing maps to the one-based MEOS accessor.
        assertEquals(id(GeneratedFunctions.temporal_instant_n(p, 1)), id(g.instantN(0)));
        assertEquals(id(GeneratedFunctions.temporal_instant_n(p, 2)), id(g.instantN(1)));
        assertEquals(id(GeneratedFunctions.temporal_sequence_n(p, 1)), id(g.sequenceN(0)));
    }

    @Test
    void spanArraysMatchTheLibrary() {
        TFloatSeqSet ss = new TFloatSeqSet(
                "{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}");
        Pointer p = ss.getInner();
        GeneratedTemporal g = gen(ss);
        Runtime rt = Runtime.getSystemRuntime();
        int spanBytes = 24; // sizeof(Span) on the 64-bit targets

        // spans() folds the Span array into tstzspan wrappers; compare each to the raw array element.
        Pointer c = Memory.allocate(rt, Integer.BYTES);
        Pointer arr = GeneratedFunctions.temporal_spans(p, c);
        List<tstzspan> spans = g.spans();
        assertEquals(c.getInt(0), spans.size());
        for (int i = 0; i < spans.size(); i++) {
            assertEquals(GeneratedFunctions.span_out(arr.slice((long) i * spanBytes), 15),
                    GeneratedFunctions.span_out(spans.get(i).get_inner(), 15));
        }

        // The split and time-bin variants fold with their extra arguments.
        Pointer c2 = Memory.allocate(rt, Integer.BYTES);
        GeneratedFunctions.temporal_split_n_spans(p, 2, c2);
        assertEquals(c2.getInt(0), g.splitNSpans(2).size());

        OffsetDateTime origin = OffsetDateTime.parse("2019-09-01T00:00:00Z");
        Pointer c3 = Memory.allocate(rt, Integer.BYTES);
        GeneratedFunctions.temporal_time_bins(p,
                ConversionUtils.timedelta_to_interval(Duration.ofDays(1)), origin, c3);
        assertEquals(c3.getInt(0), g.timeBins(Duration.ofDays(1), origin).size());
    }

    @Test
    void inputConstructorsMatchTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 2@2019-09-02]");
        Pointer p = a.getInner();
        GeneratedTemporal g = gen(a);

        // fromHEXWKB is the input constructor symmetric to asHEXWKB; it round-trips the output.
        String hex = g.asHEXWKB((byte) 4);
        assertEquals(id(GeneratedFunctions.temporal_from_hexwkb(hex)), id(g.fromHEXWKB(hex)));
        assertEquals(id(p), id(g.fromHEXWKB(hex)));

        // fromWKB round-trips the WKB buffer; its byte length is half the hex string's length.
        Pointer wkb = g.asWKB((byte) 4);
        assertEquals(id(p), id(g.fromWKB(wkb, hex.length() / 2)));
    }

    @Test
    void timestamptzNMatchesTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 2@2019-09-02, 3@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTemporal g = gen(a);

        // The bool+result accessor folds to the timestamp; zero-based n reaches the one-based accessor.
        Pointer r = GeneratedFunctions.temporal_timestamptz_n(p, 1);
        assertEquals(utils.TimestampTzConverter.toOffsetDateTime(r.getLong(0)), g.timestamptzN(0));
        assertEquals(g.startTimestamptz(), g.timestamptzN(0));
    }
}
