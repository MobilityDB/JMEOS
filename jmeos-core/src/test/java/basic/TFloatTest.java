package basic;

import org.junit.jupiter.api.extension.ExtendWith;
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
import functions.*;
import types.basic.tnumber.TNumber;
import types.boxes.Box;
import types.boxes.TBox;
import types.collections.number.FloatSpan;
import types.collections.number.IntSpanSet;
import types.collections.time.tstzspan;
import types.collections.time.tstzspanset;
import types.collections.time.Time;
import types.collections.time.tstzset;
import types.temporal.TInterpolation;
import types.temporal.Temporal;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestLogger.class)
public class TFloatTest {

    static error_handler_fn errorHandler = new error_handler();

    private static Stream<Arguments> frombasetemporal() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TFloatInst", TInterpolation.NONE),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TFloatSeq", TInterpolation.LINEAR),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TFloatSeqSet", TInterpolation.LINEAR)
        );
    }


    private static Stream<Arguments> frombasetime() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new tstzset("{2019-09-01, 2019-09-02}"), "TFloatSeq", TInterpolation.DISCRETE),
                Arguments.of(new tstzspan("[2019-09-01, 2019-09-02]"), "TFloatSeqSet", TInterpolation.LINEAR),
                Arguments.of(new tstzspanset("{[2019-09-01, 2019-09-02],[2019-09-03, 2019-09-05]}"), "TFloatSeq", TInterpolation.LINEAR)
        );
    }


    private static Stream<Arguments> fromstring() {
        return Stream.of(
                Arguments.of("1.5@2019-09-01", "TFloatInst", TInterpolation.NONE, "1.5@2019-09-01 00:00:00+00"),
                Arguments.of("[1.5@2019-09-01, 2.5@2019-09-02]", "TFloatSeq", TInterpolation.LINEAR, "[1.5@2019-09-01 00:00:00+00, 2.5@2019-09-02 00:00:00+00]"),
                Arguments.of("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}", "TFloatSeqSet", TInterpolation.LINEAR, "{[1.5@2019-09-01 00:00:00+00, 2.5@2019-09-02 00:00:00+00], [1.5@2019-09-03 00:00:00+00, 1.5@2019-09-05 00:00:00+00]}")
        );
    }



    private static Stream<Arguments> fromcopy() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst"),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq"),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet")
                //Arguments.of(new TFloatSeqSet("Interp=Step;{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]"), "TFloatInst")
        );
    }

    private static Stream<Arguments> totint() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", "1@2019-09-01 00:00:00+00")
                //Arguments.of(new TFloatSeq("{1.5@2019-09-01, 2.5@2019-09-02}"), "TFloatSeq", "[1@2019-09-01 00:00:00+00, 2@2019-09-02 00:00:00+00]"),
                //Arguments.of(new TFloatSeqSet("Interp=Step;{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", "{[1@2019-09-01 00:00:00+00, 2@2019-09-02 00:00:00+00], [1@2019-09-03 00:00:00+00, 1@2019-09-05 00:00:00+00]}")
        );
    }



    private static Stream<Arguments> bounding() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new TBox("TBOXFLOAT XT([1.5,1.5],[2019-09-01, 2019-09-01])")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new TBox("TBOXFLOAT XT([1.5,2.5],[2019-09-01, 2019-09-02])")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new TBox("TBOXFLOAT XT([1.5,2.5],[2019-09-01, 2019-09-05])"))
        );
    }



    private static Stream<Arguments> interp() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", TInterpolation.NONE),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", TInterpolation.LINEAR),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", TInterpolation.LINEAR)
        );
    }


    private static Stream<Arguments> value_span() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new FloatSpan(1.5f, 1.5f, true, true)),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new FloatSpan(1.5f, 2.5f, true, true)),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new FloatSpan(1.5f, 2.5f, true, true))
        );
    }


    private static Stream<Arguments> value_spans() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new IntSpanSet("{[1,1]}")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new IntSpanSet("{[1,2]}")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new IntSpanSet("{[1,2]}"))
        );
    }


    private static Stream<Arguments> start_value() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 1.5f),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 1.5f),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 1.5f)
        );
    }


    private static Stream<Arguments> end_value() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 1.5f),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 2.5f),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 1.5f)
        );
    }


    private static Stream<Arguments> min_value() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 1.5f),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 1.5f),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 1.5f)
        );
    }


    private static Stream<Arguments> max_value() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TIntInst", 1.5f),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TIntSeq", 2.5f),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TIntSeqSet", 2.5f)
        );
    }


    private static Stream<Arguments> time() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new tstzspanset("{[2019-09-01, 2019-09-01]}")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new tstzspanset("{[2019-09-01, 2019-09-02]}")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new tstzspanset("{[2019-09-01, 2019-09-02], [2019-09-03, 2019-09-05]}"))
        );
    }


    private static Stream<Arguments> period() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new tstzspan("[2019-09-01, 2019-09-01]")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new tstzspan("[2019-09-01, 2019-09-02]")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new tstzspan("[2019-09-01, 2019-09-05]"))
        );
    }

    private static Stream<Arguments> num_instant() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 1),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq",2),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 4)
        );
    }



    private static Stream<Arguments> start_instant() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TIntInst", new TFloatInst("1.5@2019-09-01")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TIntSeq",new TFloatInst("1.5@2019-09-01")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TIntSeqSet", new TFloatInst("1.5@2019-09-01"))
        );
    }



    private static Stream<Arguments> end_instant() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new TFloatInst("1.5@2019-09-01")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq",new TFloatInst("2.5@2019-09-02")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new TFloatInst("1.5@2019-09-05"))
        );
    }


    private static Stream<Arguments> max_instant() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new TFloatInst("1.5@2019-09-01")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq",new TFloatInst("2.5@2019-09-02")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new TFloatInst("2.5@2019-09-02"))
        );
    }


    private static Stream<Arguments> instant_n() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), 0, new TFloatInst("1.5@2019-09-01")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), 1,new TFloatInst("2.5@2019-09-02")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), 2, new TFloatInst("1.5@2019-09-03"))
        );
    }



    private static Stream<Arguments> num_timestamps() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), 1, new TIntInst("1@2019-09-01")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), 2,new TIntInst("2@2019-09-02")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), 4, new TIntInst("1@2019-09-03"))
        );
    }



    private static Stream<Arguments> start_timestamps() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), 1, LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), 2, LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), 4, LocalDateTime.of(2019, 9, 1, 0, 0,0))
        );
    }


    private static Stream<Arguments> end_timestamps() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), 1, LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), 2, LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), 4, LocalDateTime.of(2019, 9, 5, 0, 0,0))
        );
    }


    private static Stream<Arguments> hash() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), 1307112109, LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), -1130512577, LocalDateTime.of(2019, 9, 2, 0, 0,0))
//                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), 4247071962l, LocalDateTime.of(2019, 9, 5, 0, 0,0))
        );
    }


    private static Stream<Arguments> toinstant() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new TFloatInst("1.5@2019-09-01")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01]"), "TFloatSeq", new TFloatInst("1.5@2019-09-01")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01]}"), "TFloatSeqSet", new TFloatInst("1.5@2019-09-01"))
        );
    }


    private static Stream<Arguments> tosequence() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", TInterpolation.LINEAR, new TFloatSeq("[1.5@2019-09-01]")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", TInterpolation.LINEAR, new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02]}"), "TFloatSeqSet", TInterpolation.LINEAR, new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"))
        );
    }


    private static Stream<Arguments> tosequenceset() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", TInterpolation.LINEAR, new TFloatSeqSet("{[1.5@2019-09-01]}")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", TInterpolation.LINEAR, new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02]}")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02]}"), "TFloatSeqSet", TInterpolation.LINEAR, new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02]}"))
        );
    }


    private static Stream<Arguments> insert() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                //Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new TFloatSeq("{1.5@2019-09-03}"), new TFloatSeq("{1.5@2019-09-01, 1.5@2019-09-03}"))
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new TFloatSeq("[1.5@2019-09-03]"), new TFloatSeqSet("[1.5@2019-09-01, 2.5@2019-09-02, 1.5@2019-09-03]")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TIntSeqSet", new TFloatSeq("[1.5@2019-09-06]"), new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05],[1.5@2019-09-06]}"))
        );
    }


    private static Stream<Arguments> update() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new TFloatInst("2.5@2019-09-01"), new TFloatInst("2.5@2019-09-01")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new TFloatInst("2.5@2019-09-01"), new TFloatSeqSet("{[2.5@2019-09-01], (1.5@2019-09-01, 2.5@2019-09-02]}")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new TFloatInst("2.5@2019-09-01"), new TFloatSeqSet("{[2.5@2019-09-01], (1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> append_sequence() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new TFloatSeq("[1.5@2019-09-03]"), new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02], [1.5@2019-09-03]}")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new TFloatSeq("[1.5@2019-09-06]"), new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05],[1.5@2019-09-06]}"))
        );
    }


    private static Stream<Arguments> abs() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", new TFloatInst("2.5@2019-09-01"), new TFloatInst("2.5@2019-09-01")),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new TFloatInst("2.5@2019-09-01"), new TFloatSeqSet("{[2.5@2019-09-01], (1.5@2019-09-01, 2.5@2019-09-02]}")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new TFloatInst("2.5@2019-09-01"), new TFloatSeqSet("{[2.5@2019-09-01], (1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"))
        );
    }

    /*
    private static Stream<Arguments> delta_value() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", new TFloatSeq("Interp=Step;[1@2019-09-01, 1@2019-09-02)")),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", new TFloatSeqSet("Interp=Step;{[1@2019-09-01, 1@2019-09-02),[0@2019-09-03, 0@2019-09-05)}"))
        );
    }

     */



    private static Stream<Arguments> always_equal() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 1.5f, true ),
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 2.5f, false ),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 1.5f, false ),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 2.5f, false ),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 1.5f, false),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 2.5f, false)
        );
    }



    private static Stream<Arguments> ever_equal() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 1.5f, true ),
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 2.5f, false ),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 1.5f, true ),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 2.5f, true ),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 1.5f, true),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 2.5f, true)
        );
    }



    private static Stream<Arguments> ever_greater() throws SQLException {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 1.5f, false ),
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TFloatInst", 2.5f, true ),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 1.5f, false ),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 2.5@2019-09-02]"), "TFloatSeq", 2.5f, false ),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 1.5f, false),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 2.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TFloatSeqSet", 2.5f, false)
        );
    }










    /** ---------------------------------------------------------------------------------------------------- */
    /** ---------------------------------------------------------------------------------------------------- */
    /** ---------------------------------------------------------------------------------------------------- */
    /** ---------------------------------------------------------------------------------------------------- */






    @ParameterizedTest(name="source={0}, type={1}, interpolation={2}")
    @MethodSource("frombasetime")
    void testFromBaseTimeConstructor(Time source, String type, TInterpolation interpolation) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if (type == "TFloatSeq") {
            TFloatSeq ti = (TFloatSeq) TFloat.from_base_time(1.5f, source, interpolation);
            assertTrue(ti instanceof TFloatSeq);
            System.out.println(ti.to_string(15));
            assertEquals(interpolation, ti.interpolation());
        } else if (type == "TFloatSeqSet") {
            TFloatSeqSet ti = (TFloatSeqSet)TFloat.from_base_time(1.5f, source, interpolation);
            assertTrue(ti instanceof TFloatSeqSet);
            System.out.println(ti.to_string(15));
            assertEquals(interpolation, ti.interpolation());
        }
    }



    @ParameterizedTest(name ="source={0}, type={1}, interpolation={2}")
    @MethodSource("frombasetemporal")
    void testFromBaseTemporalConstructor(Temporal source, String type, TInterpolation interpolation) {
        //GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TFloatInst ti = new TFloatInst();
            TFloatInst new_ti = (TFloatInst) ti.from_base_temporal(1.5f,source,interpolation);
            assertTrue(new_ti instanceof TFloatInst);
            assertEquals(new_ti.interpolation(), interpolation);
        }
        else if (type == "TFloatSeq") {
            TFloatSeq ti = new TFloatSeq();
            TFloatSeq new_ti = (TFloatSeq) ti.from_base_temporal(1.5f,source,interpolation);
            assertTrue(new_ti instanceof TFloatSeq);
            assertEquals(new_ti.interpolation(), interpolation);

        } else if (type == "TFloatSeqSet") {
            TFloatSeqSet ti = new TFloatSeqSet();
            TFloatSeqSet new_ti = (TFloatSeqSet) ti.from_base_temporal(1.5f,source,interpolation);
            assertTrue(new_ti instanceof TFloatSeqSet);
            assertEquals(new_ti.interpolation(), interpolation);
        }
    }




    @ParameterizedTest(name ="source={0}, type={1}, interpolation={2}, expected={3}")
    @MethodSource("fromstring")
    void testStringConstructor(String source, String type, TInterpolation interpolation, String expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TFloatInst tinst = new TFloatInst(source);
            assertTrue(tinst instanceof TFloatInst);
            assertEquals(tinst.interpolation(), interpolation);
            assertEquals(tinst.to_string(15),expected);
        }
        else if (type == "TFloatSeq") {
            TFloatSeq tinst = new TFloatSeq(source);
            assertTrue(tinst instanceof TFloatSeq);
            assertEquals(tinst.interpolation(), interpolation);
            assertEquals(tinst.to_string(15),expected);

        } else if (type == "TFloatSeqSet") {
            TFloatSeqSet tinst = new TFloatSeqSet(source);
            assertTrue(tinst instanceof TFloatSeqSet);
            assertEquals(tinst.interpolation(), interpolation);
            assertEquals(tinst.to_string(15),expected);
        }
    }




    @ParameterizedTest(name ="source={0}, type={1}")
    @MethodSource("fromcopy")
    void testCopyConstructor(Temporal source, String type) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TFloatInst tb = (TFloatInst)source.copy();
            assertEquals(tb.to_string(15),(((TFloatInst) source).to_string(15)));
        }
        else if (type == "TFloatSeq") {
            TFloatSeq tb = (TFloatSeq) source.copy();
            assertEquals(tb.to_string(15),(((TFloatSeq) source).to_string(15)));

        } else if (type == "TFloatSeqSet") {
            TFloatSeqSet tb = (TFloatSeqSet) source.copy();
            assertEquals(tb.to_string(15),(((TFloatSeqSet) source).to_string(15)));
        }
    }



    @ParameterizedTest(name ="source={0}, type={1}, interpolation={2}, expected={3}")
    @MethodSource("fromstring")
    void testString(String source, String type, TInterpolation interpolation, String expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TFloatInst tinst = new TFloatInst(source);
            assertEquals(tinst.to_string(15),expected);
        }
        else if (type == "TFloatSeq") {
            TFloatSeq tinst = new TFloatSeq(source);
            assertEquals(tinst.to_string(15),expected);

        } else if (type == "TFloatSeqSet") {
            TFloatSeqSet tinst = new TFloatSeqSet(source);
            assertEquals(tinst.to_string(15),expected);
        }
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("totint")
    void testToTInt(TFloat source, String type, String expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TInt tinst = ((TFloatInst) source).to_tint();
            assertEquals(tinst.to_string(),expected);
        }
        else if (type == "TFloatSeq") {
            TInt tinst = ((TFloatSeq) source).to_tint();
            System.out.println("here");
            assertEquals(tinst.to_string(),expected);

        } else if (type == "TFloatSeqSet") {
            TInt tinst = ((TFloatSeqSet) source).to_tint();
            System.out.println("here");
            assertEquals(tinst.to_string(),expected);
        }
    }



    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("bounding")
    void testBoundingBox(Temporal source, String type, Box expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.bounding_box().toString(),expected.to_period().toString());
    }




    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("interp")
    void testInterpolation(Temporal source, String type, TInterpolation expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.interpolation(),expected);
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("value_span")
    void testValueSpan(TFloat source, String type, FloatSpan expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.value_span().toString(15),expected.toString(15));
    }

    /*
    @ParameterizedTest(name ="Test values span ")
    @MethodSource("value_spans")
    void testValuesSpan(TInt source, String type, IntSpanSet expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.value_spans().toString(),expected.toString());
    }

     */


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("start_value")
    void testStart_value(TFloat source, String type, float expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.start_value(),expected);
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("end_value")
    void testEnd_value(TFloat source, String type, float expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.end_value(),expected);
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("min_value")
    void testMin_value(TFloat source, String type, float expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.min_value(),expected);
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("max_value")
    void testMax_value(TFloat source, String type, float expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.max_value(),expected);
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("time")
    void testTime(Temporal source, String type, tstzspanset expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.time().toString(),expected.toString());
    }



    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("period")
    void testtstzspan(Temporal source, String type, tstzspan expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.period().toString(),expected.toString());
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("period")
    void testTimespan(Temporal source, String type, tstzspan expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.timespan().toString(),expected.toString());
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("num_instant")
    void testNumInstant(Temporal source, String type, int expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.num_instants(),expected);
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("start_instant")
    void testStartInstant(Temporal source, String type, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(((TFloatInst)source.start_instant()).to_string(15),((TFloatInst)expected.start_instant()).to_string(15));
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("end_instant")
    void testEndInstant(Temporal source, String type, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(((TFloatInst)source.end_instant()).to_string(15),((TFloatInst)expected.end_instant()).to_string(15));
    }



    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("start_instant")
    void testMinInstant(Temporal source, String type, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(((TFloatInst)source.min_instant()).to_string(15),((TFloatInst)expected.min_instant()).to_string(15));
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("max_instant")
    void testMaxInstant(Temporal source, String type, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(((TFloatInst)source.max_instant()).to_string(15),((TFloatInst)expected.max_instant()).to_string(15));
    }



    @ParameterizedTest(name ="source={0}, n={1}, expected={2}")
    @MethodSource("instant_n")
    void testInstant_n(Temporal source, int n, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(((TFloatInst)source.instant_n(n)).to_string(15),((TFloatInst)expected).to_string(15));
    }


    @ParameterizedTest(name ="source={0}, n={1}, expected={2}")
    @MethodSource("num_timestamps")
    void testNumTimestamps(Temporal source, int n, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.num_timestamps(),n);
    }


    @ParameterizedTest(name ="source={0}, n={1}, expected={2}")
    @MethodSource("start_timestamps")
    void testStartTimestamps(Temporal source, int n, LocalDateTime expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.start_timestamp(),expected);
    }


    @ParameterizedTest(name ="source={0}, n={1}, expected={2}")
    @MethodSource("end_timestamps")
    void testEndTimestamps(Temporal source, int n, LocalDateTime expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.end_timestamp(),expected);
    }


    @ParameterizedTest(name ="source={0}, n={1}, expected={2}")
    @MethodSource("hash")
    void testHash(Temporal source, long n, LocalDateTime expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(source.hash(),n);
    }


    @ParameterizedTest(name ="source={0}, type={1}, expected={2}")
    @MethodSource("toinstant")
    void testToinstant(Temporal source, String type, TFloatInst expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        TFloatInst tmp = (TFloatInst) source.to_instant();
        assertTrue(tmp instanceof TFloatInst);
        assertEquals(tmp.to_string(15),expected.to_string(15));

    }



    @ParameterizedTest(name ="source={0}, type={1}, interp={2}, expected={3}")
    @MethodSource("tosequence")
    void testTosequence(Temporal source, String type, TInterpolation interp, TFloatSeq expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        TFloatSeq tmp = (TFloatSeq) source.to_sequence(interp);
        assertTrue(tmp instanceof TFloatSeq);
        assertEquals(tmp.to_string(15),expected.to_string(15));

    }


    @ParameterizedTest(name ="source={0}, type={1}, interp={2}, expected={3}")
    @MethodSource("tosequenceset")
    void testTosequenceset(Temporal source, String type, TInterpolation interp, TFloatSeqSet expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        TFloatSeqSet tmp = (TFloatSeqSet) source.to_sequenceset(interp);
        assertTrue(tmp instanceof TFloatSeqSet);
        assertEquals(interp, tmp.interpolation());
        assertEquals(tmp.to_string(15),expected.to_string(15));
    }



    @ParameterizedTest(name ="source={0}, type={1}, tseq={2}, expected={3}")
    @MethodSource("insert")
    void testInsert(Temporal source, String type, TFloatSeq tseq, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TFloatInst tmp = (TFloatInst) source.insert(tseq);
            assertEquals(tmp.to_string(15), ((TFloatSeq)expected).to_string(15));
        } else if (type == "TFloatSeq") {
            TFloatSeq tmp = (TFloatSeq) source.insert(tseq);
            assertEquals(tmp.to_string(15), ((TFloatSeqSet)expected).to_string(15));
        } else if (type == "TFloatSeqSet") {
            TFloatSeqSet tmp = (TFloatSeqSet) source.insert(tseq);
            assertEquals(tmp.to_string(15), ((TFloatSeqSet)expected).to_string(15));
        }
    }




    @ParameterizedTest(name ="source={0}, type={1}, tseq={2}, expected={3}")
    @MethodSource("update")
    void testUpdate(Temporal source, String type, TFloatInst tseq, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TFloatInst tmp = (TFloatInst) source.update(tseq);
            assertEquals(tmp.to_string(15), ((TFloatInst)expected).to_string(15));
        } else if (type == "TFloatSeq") {
            TFloatSeq tmp = (TFloatSeq) source.update(tseq);
            assertEquals(tmp.to_string(15), ((TFloatSeqSet)expected).to_string(15));
        } else if (type == "TFloatSeqSet") {
            TFloatSeqSet tmp = (TFloatSeqSet) source.update(tseq);
            assertEquals(tmp.to_string(15), ((TFloatSeqSet)expected).to_string(15));
        }
    }


    @ParameterizedTest(name ="source={0}, type={1}, tseq={2}, expected={3}")
    @MethodSource("append_sequence")
    void testAppendSequence(Temporal source, String type, TFloatSeq tseq, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if (type == "TFloatSeq") {
            TFloatSeq tmp = (TFloatSeq) source.append_sequence(tseq);
            assertEquals(tmp.to_string(15), ((TFloatSeqSet)expected).to_string(15));
        } else if (type == "TFloatSeqSet") {
            TFloatSeqSet tmp = (TFloatSeqSet) source.append_sequence(tseq);
            assertEquals(tmp.to_string(15), ((TFloatSeqSet)expected).to_string(15));
        }
    }





    @ParameterizedTest(name ="source={0}, type={1}, tseq={2}, expected={3}")
    @MethodSource("abs")
    void testAbs(Temporal source, String type, TFloatInst tseq, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TNumber tmp = ((TNumber) source).abs();
            assertEquals(((TFloatInst)tmp).to_string(15), ((TFloatInst)source).to_string(15));
        } else if (type == "TFloatSeq") {
            TNumber tmp = ((TNumber) source).abs();
            assertEquals(((TFloatSeq)tmp).to_string(15), ((TFloatSeq)source).to_string(15));
        } else if (type == "TFloatSeqSet") {
            TNumber tmp = ((TNumber) source).abs();
            assertEquals(((TFloatSeqSet)tmp).to_string(15), ((TFloatSeqSet)source).to_string(15));
        }
    }

    /*
    @ParameterizedTest(name ="Test delta value method")
    @MethodSource("delta_value")
    void testDeltaValue(Temporal source, String type, Temporal expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        if(type == "TFloatInst"){
            TNumber tmp = ((TNumber) source).delta_value();
            assertEquals(((TFloatInst)tmp).tostring(15), ((TFloatInst)expected).tostring(15));
        } else if (type == "TFloatSeq") {
            TNumber tmp = ((TNumber) source).delta_value();
            assertEquals(((TFloatSeq)tmp).tostring(15), ((TFloatSeq)expected).tostring(15));
        } else if (type == "TFloatSeqSet") {
            TNumber tmp = ((TNumber) source).delta_value();
            assertEquals(((TFloatSeqSet)tmp).tostring(15), ((TFloatSeqSet)expected).tostring(15));
        }
    }

     */



    @ParameterizedTest(name ="source={0}, type={1}, arg={2}, expected={3}")
    @MethodSource("always_equal")
    void testAlwaysEqual(Temporal source, String type, float arg, boolean expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(((TFloat)source).always_equal(arg),expected);
        assertEquals(((TFloat)source).never_not_equal(arg),expected);
        assertEquals(((TFloat)source).ever_not_equal(arg),! expected);
    }




    @ParameterizedTest(name ="source={0}, type={1}, arg={2}, expected={3}")
    @MethodSource("ever_equal")
    void testEverEqual(Temporal source, String type, float arg, boolean expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(((TFloat)source).ever_equal(arg),expected);
        assertEquals(((TFloat)source).always_not_equal(arg),!expected);
        assertEquals(((TFloat)source).never_equal(arg),! expected);
    }


    @ParameterizedTest(name ="source={0}, type={1}, arg={2}, expected={3}")
    @MethodSource("ever_greater")
    void testEverGreater(Temporal source, String type, float arg, boolean expected) {
        GeneratedFunctions.meos_initialize_timezone("UTC");
        assertEquals(((TFloat)source).always_less(arg),expected);
        assertEquals(((TFloat)source).never_greater_or_equal(arg),expected);
        assertEquals(((TFloat)source).ever_greater_or_equal(arg),! expected);
    }


}