package basic;

import functions.functions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import types.basic.tfloat.TFloat;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.basic.tint.TInt;
import types.basic.tint.TIntInst;
import types.basic.tint.TIntSeq;
import types.basic.tint.TIntSeqSet;
import types.basic.tnumber.TNumber;
import types.basic.tpoint.tgeog.TGeogPoint;
import types.basic.tpoint.tgeog.TGeogPointInst;
import types.basic.tpoint.tgeog.TGeogPointSeq;
import types.basic.tpoint.tgeog.TGeogPointSeqSet;
import types.boxes.Box;
import types.boxes.TBox;
import types.collections.number.FloatSpan;
import types.collections.number.IntSpanSet;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import types.temporal.TInterpolation;
import types.temporal.Temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TGeogPointTest {

    private static Stream<Arguments> fromstring() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst",TInterpolation.NONE, "POINT(1 1)@2019-09-01 00:00:00+00"),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq",TInterpolation.DISCRETE, "{POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00}"),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq",TInterpolation.LINEAR, "[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00]"),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet",TInterpolation.LINEAR, "{[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00], [POINT(1 1)@2019-09-03 00:00:00+00, POINT(1 1)@2019-09-05 00:00:00+00]}")
        );
    }



    @ParameterizedTest(name="Test string constructor")
    @MethodSource("fromstring")
    void testFromBaseTimeConstructor(TGeogPoint source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst") {
            TGeogPointInst ti = new TGeogPointInst(expected);
            assertTrue(ti instanceof TGeogPointInst);
            assertEquals(interpolation, ti.interpolation());
            assertEquals(ti.to_string(),expected);
        } else if (type == "TGeogPointSeq") {
            TGeogPointSeq ti = new TGeogPointSeq(expected);
            assertTrue(ti instanceof TGeogPointSeq);
            assertEquals(interpolation, ti.interpolation());
            assertEquals(ti.to_string(),expected);
        } else if (type == "TGeogPointSeqSet"){
            TGeogPointSeqSet ti = new TGeogPointSeqSet(expected);
            assertTrue(ti instanceof TGeogPointSeqSet);
            assertEquals(interpolation, ti.interpolation());
            assertEquals(ti.to_string(),expected);
        }
    }



}
