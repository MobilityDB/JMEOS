package basic;
import functions.functions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.basic.tint.TInt;
import types.basic.tint.TIntInst;
import types.basic.tint.TIntSeq;
import types.basic.tint.TIntSeqSet;
import types.basic.tnumber.TNumber;
import types.boxes.Box;
import types.boxes.TBox;
import types.collections.number.IntSpan;
import types.collections.number.IntSpanSet;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import types.temporal.TInterpolation;
import types.temporal.Temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TIntTest {

    private static Stream<Arguments> frombasetemporal() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TFloatInst("1.5@2019-09-01"), "TIntInst", TInterpolation.NONE),
                Arguments.of(new TFloatSeq("[1.5@2019-09-01, 0.5@2019-09-02]"), "TIntSeq", TInterpolation.STEPWISE),
                Arguments.of(new TFloatSeqSet("{[1.5@2019-09-01, 0.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}"), "TIntSeqSet", TInterpolation.STEPWISE)
        );
    }


    private static Stream<Arguments> frombasetime() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TimestampSet("{2019-09-01, 2019-09-02}"), "TIntSeq", TInterpolation.DISCRETE),
                Arguments.of(new Period("[2019-09-01, 2019-09-02]"), "TIntSeq", TInterpolation.STEPWISE),
                Arguments.of(new PeriodSet("{[2019-09-01, 2019-09-02],[2019-09-03, 2019-09-05]}"), "TIntSeqSet", TInterpolation.STEPWISE)
        );
    }


    private static Stream<Arguments> fromstring() {
        return Stream.of(
                Arguments.of("1@2019-09-01", "TIntInst", TInterpolation.NONE, "1@2019-09-01 00:00:00+00"),
                Arguments.of("[1@2019-09-01, 2@2019-09-02]", "TIntSeq", TInterpolation.STEPWISE, "[1@2019-09-01 00:00:00+00, 2@2019-09-02 00:00:00+00]"),
                Arguments.of("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}", "TIntSeqSet", TInterpolation.STEPWISE, "{[1@2019-09-01 00:00:00+00, 2@2019-09-02 00:00:00+00], [1@2019-09-03 00:00:00+00, 1@2019-09-05 00:00:00+00]}")
        );
    }



    private static Stream<Arguments> fromcopy() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst"),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq"),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet")
        );
    }

    private static Stream<Arguments> totfloat() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", "1@2019-09-01 00:00:00+00"),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", "Interp=Step;[1@2019-09-01 00:00:00+00, 2@2019-09-02 00:00:00+00]"),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", "Interp=Step;{[1@2019-09-01 00:00:00+00, 2@2019-09-02 00:00:00+00], [1@2019-09-03 00:00:00+00, 1@2019-09-05 00:00:00+00]}")
        );
    }



    private static Stream<Arguments> bounding() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new TBox("TBOXINT XT([1,1],[2019-09-01, 2019-09-01])")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new TBox("TBOXINT XT([1,2],[2019-09-01, 2019-09-02])")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TBox("TBOXINT XT([1,2],[2019-09-01, 2019-09-05])"))
        );
    }

    private static Stream<Arguments> interp() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", TInterpolation.NONE),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", TInterpolation.STEPWISE),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", TInterpolation.STEPWISE)
        );
    }


    private static Stream<Arguments> value_span() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new IntSpan(1, 1, true, true)),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new IntSpan(1, 2, true, true)),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new IntSpan(1, 2, true, true))
        );
    }


    private static Stream<Arguments> value_spans() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new IntSpanSet("{[1,1]}")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new IntSpanSet("{[1,2]}")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new IntSpanSet("{[1,2]}"))
        );
    }


    private static Stream<Arguments> start_value() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 1),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 1),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 1)
        );
    }


    private static Stream<Arguments> end_value() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 1),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 2),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 1)
        );
    }


    private static Stream<Arguments> min_value() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 1),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 1),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 1)
        );
    }


    private static Stream<Arguments> max_value() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 1),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 2),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 2)
        );
    }


    private static Stream<Arguments> time() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new PeriodSet("{[2019-09-01, 2019-09-01]}")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new PeriodSet("{[2019-09-01, 2019-09-02]}")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new PeriodSet("{[2019-09-01, 2019-09-02], [2019-09-03, 2019-09-05]}"))
        );
    }


    private static Stream<Arguments> period() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new Period("[2019-09-01, 2019-09-01]")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new Period("[2019-09-01, 2019-09-02]")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new Period("[2019-09-01, 2019-09-05]"))
        );
    }

    private static Stream<Arguments> num_instant() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 1),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq",2),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 4)
        );
    }



    private static Stream<Arguments> start_instant() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new TIntInst("1@2019-09-01")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq",new TIntInst("1@2019-09-01")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TIntInst("1@2019-09-01"))
        );
    }



    private static Stream<Arguments> end_instant() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new TIntInst("1@2019-09-01")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq",new TIntInst("2@2019-09-02")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TIntInst("1@2019-09-05"))
        );
    }


    private static Stream<Arguments> max_instant() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new TIntInst("1@2019-09-01")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq",new TIntInst("2@2019-09-02")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TIntInst("2@2019-09-02"))
        );
    }


    private static Stream<Arguments> instant_n() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), 0, new TIntInst("1@2019-09-01")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), 1,new TIntInst("2@2019-09-02")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), 2, new TIntInst("1@2019-09-03"))
        );
    }



    private static Stream<Arguments> num_timestamps() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), 1, new TIntInst("1@2019-09-01")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), 2,new TIntInst("2@2019-09-02")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), 4, new TIntInst("1@2019-09-03"))
        );
    }



    private static Stream<Arguments> start_timestamps() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), 1, LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), 2, LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), 4, LocalDateTime.of(2019, 9, 1, 0, 0,0))
        );
    }


    private static Stream<Arguments> end_timestamps() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), 1, LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), 2, LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), 4, LocalDateTime.of(2019, 9, 5, 0, 0,0))
        );
    }


    private static Stream<Arguments> hash() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), 440045287, LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), 3589664982l, LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), 205124107l, LocalDateTime.of(2019, 9, 5, 0, 0,0))
        );
    }


    private static Stream<Arguments> toinstant() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new TIntInst("1@2019-09-01")),
                Arguments.of(new TIntSeq("[1@2019-09-01]"), "TIntSeq", new TIntInst("1@2019-09-01")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01]}"), "TIntSeqSet", new TIntInst("1@2019-09-01"))
        );
    }


    private static Stream<Arguments> tosequence() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", TInterpolation.STEPWISE, new TIntSeq("[1@2019-09-01]")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", TInterpolation.STEPWISE, new TIntSeq("[1@2019-09-01, 2@2019-09-02]")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02]}"), "TIntSeqSet", TInterpolation.STEPWISE, new TIntSeq("[1@2019-09-01, 2@2019-09-02]"))
        );
    }


    private static Stream<Arguments> tosequenceset() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", TInterpolation.STEPWISE, new TIntSeqSet("{[1@2019-09-01]}")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", TInterpolation.STEPWISE, new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02]}")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02]}"), "TIntSeqSet", TInterpolation.STEPWISE, new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02]}"))
        );
    }


    private static Stream<Arguments> insert() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new TIntSeq("{1@2019-09-03}"), new TIntSeq("{1@2019-09-01, 1@2019-09-03}")),
                Arguments.of(new TIntSeq("{[1@2019-09-01, 2@2019-09-02]}"), "TIntSeq", new TIntSeq("[1@2019-09-03]"), new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02], [1@2019-09-03]}")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TIntSeq("[1@2019-09-06]"), new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05],[1@2019-09-06]}"))
        );
    }


    private static Stream<Arguments> update() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new TIntInst("2@2019-09-01"), new TIntInst("2@2019-09-01")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new TIntInst("2@2019-09-01"), new TIntSeqSet("{[2@2019-09-01], (1@2019-09-01, 2@2019-09-02]}")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TIntInst("2@2019-09-01"), new TIntSeqSet("{[2@2019-09-01], (1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> append_sequence() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new TIntSeq("[1@2019-09-03]"), new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02], [1@2019-09-03]}")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TIntSeq("[1@2019-09-06]"), new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05],[1@2019-09-06]}"))
        );
    }


    private static Stream<Arguments> abs() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", new TIntInst("2@2019-09-01"), new TIntInst("2@2019-09-01")),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new TIntInst("2@2019-09-01"), new TIntSeqSet("{[2@2019-09-01], (1@2019-09-01, 2@2019-09-02]}")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TIntInst("2@2019-09-01"), new TIntSeqSet("{[2@2019-09-01], (1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> delta_value() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", new TIntInst("2@2019-09-01"), new TIntSeq("[1@2019-09-01, 1@2019-09-02)")),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", new TIntInst("2@2019-09-01"), new TIntSeqSet("{[1@2019-09-01, 1@2019-09-02),[0@2019-09-03, 0@2019-09-05)}"))
        );
    }



    private static Stream<Arguments> always_equal() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 1, true ),
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 2, true ),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 1, false ),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 2, false ),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 1, false),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 2, false)
        );
    }



    private static Stream<Arguments> ever_equal() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 1, true ),
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 2, true ),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 1, true ),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 2, true ),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 1, true),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 2, true)
        );
    }



    private static Stream<Arguments> ever_greater() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 1, false ),
                Arguments.of(new TIntInst("1@2019-09-01"), "TIntInst", 2, true ),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 1, false ),
                Arguments.of(new TIntSeq("[1@2019-09-01, 2@2019-09-02]"), "TIntSeq", 2, false ),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 1, false),
                Arguments.of(new TIntSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"), "TIntSeqSet", 2, false)
        );
    }










    /** ---------------------------------------------------------------------------------------------------- */
    /** ---------------------------------------------------------------------------------------------------- */
    /** ---------------------------------------------------------------------------------------------------- */
    /** ---------------------------------------------------------------------------------------------------- */






    @ParameterizedTest(name="Test from base time constructor")
    @MethodSource("frombasetime")
    void testFromBaseTimeConstructor(Time source, String type, TInterpolation interpolation) {
        functions.meos_initialize("UTC");
        if (type == "TIntSeq") {
            TIntSeq ti = (TIntSeq)TInt.from_base_time(1, source, interpolation);
            assertTrue(ti instanceof TIntSeq);
            assertEquals(interpolation, ti.interpolation());
        } else if (type == "TIntSeqSet") {
            TIntSeqSet ti = (TIntSeqSet)TInt.from_base_time(1, source, interpolation);
            assertTrue(ti instanceof TIntSeqSet);
            assertEquals(interpolation, ti.interpolation());
        }
    }



    @ParameterizedTest(name ="Test from base temporal constructor")
    @MethodSource("frombasetemporal")
    void testFromBaseTemporalConstructor(Temporal source, String type, TInterpolation interpolation) {
        //functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TIntInst ti = new TIntInst();
            TIntInst new_ti = (TIntInst) ti.from_base_temporal(1,source,interpolation);
            assertTrue(new_ti instanceof TIntInst);
            assertEquals(new_ti.interpolation(), interpolation);
        }
        else if (type == "TIntSeq") {
            TIntSeq ti = new TIntSeq();
            TIntSeq new_ti = (TIntSeq) ti.from_base_temporal(1,source,interpolation);
            assertTrue(new_ti instanceof TIntSeq);
            assertEquals(new_ti.interpolation(), interpolation);

        } else if (type == "TIntSeqSet") {
            TIntSeqSet ti = new TIntSeqSet();
            TIntSeqSet new_ti = (TIntSeqSet) ti.from_base_temporal(1,source,interpolation);
            assertTrue(new_ti instanceof TIntSeqSet);
            assertEquals(new_ti.interpolation(), interpolation);
        }
    }




    @ParameterizedTest(name ="Test from string constructor")
    @MethodSource("fromstring")
    void testStringConstructor(String source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TIntInst tinst = new TIntInst(source);
            assertTrue(tinst instanceof TIntInst);
            assertEquals(tinst.interpolation(), interpolation);
            assertEquals(tinst.to_string(),expected);
        }
        else if (type == "TIntSeq") {
            TIntSeq tinst = new TIntSeq(source);
            assertTrue(tinst instanceof TIntSeq);
            assertEquals(tinst.interpolation(), interpolation);
            assertEquals(tinst.to_string(),expected);

        } else if (type == "TIntSeqSet") {
            TIntSeqSet tinst = new TIntSeqSet(source);
            assertTrue(tinst instanceof TIntSeqSet);
            assertEquals(tinst.interpolation(), interpolation);
            assertEquals(tinst.to_string(),expected);
        }
    }




    @ParameterizedTest(name ="Test from copy constructor")
    @MethodSource("fromcopy")
    void testCopyConstructor(Temporal source, String type) {
        functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TIntInst tb = (TIntInst)source.copy();
            assertEquals(tb.to_string(),(((TIntInst) source).to_string()));
        }
        else if (type == "TIntSeq") {
            TIntSeq tb = (TIntSeq) source.copy();
            assertEquals(tb.to_string(),(((TIntSeq) source).to_string()));

        } else if (type == "TIntSeqSet") {
            TIntSeqSet tb = (TIntSeqSet) source.copy();
            assertEquals(tb.to_string(),(((TIntSeqSet) source).to_string()));
        }
    }



    @ParameterizedTest(name ="Test  string ")
    @MethodSource("fromstring")
    void testString(String source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TIntInst tinst = new TIntInst(source);
            assertEquals(tinst.to_string(),expected);
        }
        else if (type == "TIntSeq") {
            TIntSeq tinst = new TIntSeq(source);
            assertEquals(tinst.to_string(),expected);

        } else if (type == "TIntSeqSet") {
            TIntSeqSet tinst = new TIntSeqSet(source);
            assertEquals(tinst.to_string(),expected);
        }
    }


    @ParameterizedTest(name ="Test to tfloat ")
    @MethodSource("totfloat")
    void testToTfloat(TInt source, String type, String expected) {
        functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TFloatInst tinst = (TFloatInst) source.to_tfloat();
            assertEquals(tinst.to_string(2),expected);
        }
        else if (type == "TIntSeq") {
            TFloatSeq tinst = (TFloatSeq) source.to_tfloat();
            assertEquals(tinst.to_string(15),expected);

        } else if (type == "TIntSeqSet") {
            TFloatSeqSet tinst = (TFloatSeqSet) source.to_tfloat();
            assertEquals(tinst.to_string(15),expected);
        }
    }



    @ParameterizedTest(name ="Test bounding box ")
    @MethodSource("bounding")
    void testBoundingBox(Temporal source, String type, Box expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.bounding_box().toString(),expected.to_period().toString());
    }


    @ParameterizedTest(name ="Test interpolation ")
    @MethodSource("interp")
    void testInterpolation(Temporal source, String type, TInterpolation expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.interpolation(),expected);
    }


    @ParameterizedTest(name ="Test value span ")
    @MethodSource("value_span")
    void testValueSpan(TInt source, String type, IntSpan expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.value_span().toString(),expected.toString());
    }


    @ParameterizedTest(name ="Test values span ")
    @MethodSource("value_spans")
    void testValuesSpan(TInt source, String type, IntSpanSet expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.value_spans().toString(),expected.toString());
    }


    @ParameterizedTest(name ="Test start value")
    @MethodSource("start_value")
    void testStart_value(TInt source, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.start_value(),expected);
    }


    @ParameterizedTest(name ="Test end value")
    @MethodSource("end_value")
    void testEnd_value(TInt source, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.end_value(),expected);
    }


    @ParameterizedTest(name ="Test min value")
    @MethodSource("min_value")
    void testMin_value(TInt source, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.min_value(),expected);
    }


    @ParameterizedTest(name ="Test max value")
    @MethodSource("max_value")
    void testMax_value(TInt source, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.max_value(),expected);
    }


    @ParameterizedTest(name ="Test time method")
    @MethodSource("time")
    void testTime(Temporal source, String type, PeriodSet expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.time().toString(),expected.toString());
    }



    @ParameterizedTest(name ="Test period method")
    @MethodSource("period")
    void testPeriod(Temporal source, String type, Period expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.period().toString(),expected.toString());
    }


    @ParameterizedTest(name ="Test period method")
    @MethodSource("period")
    void testTimespan(Temporal source, String type, Period expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.timespan().toString(),expected.toString());
    }


    @ParameterizedTest(name ="Test num instant method")
    @MethodSource("num_instant")
    void testNumInstant(Temporal source, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.num_instants(),expected);
    }


    @ParameterizedTest(name ="Test start instant method")
    @MethodSource("start_instant")
    void testStartInstant(Temporal source, String type, Temporal expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TIntInst)source.start_instant()).to_string(),((TIntInst)expected.start_instant()).to_string());
    }


    @ParameterizedTest(name ="Test end instant method")
    @MethodSource("end_instant")
    void testEndInstant(Temporal source, String type, Temporal expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TIntInst)source.end_instant()).to_string(),((TIntInst)expected.end_instant()).to_string());
    }



    @ParameterizedTest(name ="Test min instant method")
    @MethodSource("start_instant")
    void testMinInstant(Temporal source, String type, Temporal expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TIntInst)source.min_instant()).to_string(),((TIntInst)expected.min_instant()).to_string());
    }


    @ParameterizedTest(name ="Test max instant method")
    @MethodSource("max_instant")
    void testMaxInstant(Temporal source, String type, Temporal expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TIntInst)source.max_instant()).to_string(),((TIntInst)expected.max_instant()).to_string());
    }



    @ParameterizedTest(name ="Test instant n method")
    @MethodSource("instant_n")
    void testInstant_n(Temporal source, int n, Temporal expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TIntInst)source.instant_n(n)).to_string(),((TIntInst)expected).to_string());
    }


    @ParameterizedTest(name ="Test num timestamps method")
    @MethodSource("num_timestamps")
    void testNumTimestamps(Temporal source, int n, Temporal expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.num_timestamps(),n);
    }


    @ParameterizedTest(name ="Test start timestamps method")
    @MethodSource("start_timestamps")
    void testStartTimestamps(Temporal source, int n, LocalDateTime expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.start_timestamp(),expected);
    }


    @ParameterizedTest(name ="Test end timestamps method")
    @MethodSource("end_timestamps")
    void testEndTimestamps(Temporal source, int n, LocalDateTime expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.end_timestamp(),expected);
    }


    @ParameterizedTest(name ="Test hash method")
    @MethodSource("hash")
    void testHash(Temporal source, long n, LocalDateTime expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.hash(),n);
    }


    @ParameterizedTest(name ="Test to instant method")
    @MethodSource("toinstant")
    void testToinstant(Temporal source, String type, TIntInst expected) {
        functions.meos_initialize("UTC");
        TIntInst tmp = (TIntInst) source.to_instant();
        assertTrue(tmp instanceof TIntInst);
        assertEquals(tmp.to_string(),expected.to_string());

    }



    @ParameterizedTest(name ="Test to sequence method")
    @MethodSource("tosequence")
    void testTosequence(Temporal source, String type, TInterpolation interp, TIntSeq expected) {
        functions.meos_initialize("UTC");
        TIntSeq tmp = (TIntSeq) source.to_sequence(interp);
        assertTrue(tmp instanceof TIntSeq);
        assertEquals(tmp.to_string(),expected.to_string());

    }


    @ParameterizedTest(name ="Test to sequenceset method")
    @MethodSource("tosequenceset")
    void testTosequenceset(Temporal source, String type, TInterpolation interp, TIntSeqSet expected) {
        functions.meos_initialize("UTC");
        TIntSeqSet tmp = (TIntSeqSet) source.to_sequenceset(interp);
        assertTrue(tmp instanceof TIntSeqSet);
        assertEquals(tmp.to_string(),expected.to_string());

    }



    @ParameterizedTest(name ="Test insert method")
    @MethodSource("insert")
    void testInsert(Temporal source, String type, TIntSeq tseq, Temporal expected) {
        functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TIntInst tmp = (TIntInst) source.insert(tseq);
            assertEquals(tmp.to_string(), ((TIntSeq)expected).to_string());
        } else if (type == "TIntSeq") {
            TIntSeq tmp = (TIntSeq) source.insert(tseq);
            assertEquals(tmp.to_string(), ((TIntSeqSet)expected).to_string());
        } else if (type == "TIntSeqSet") {
            TIntSeqSet tmp = (TIntSeqSet) source.insert(tseq);
            assertEquals(tmp.to_string(), ((TIntSeqSet)expected).to_string());
        }
    }




    @ParameterizedTest(name ="Test update method")
    @MethodSource("update")
    void testUpdate(Temporal source, String type, TIntInst tseq, Temporal expected) {
        functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TIntInst tmp = (TIntInst) source.update(tseq);
            assertEquals(tmp.to_string(), ((TIntInst)expected).to_string());
        } else if (type == "TIntSeq") {
            TIntSeq tmp = (TIntSeq) source.update(tseq);
            assertEquals(tmp.to_string(), ((TIntSeqSet)expected).to_string());
        } else if (type == "TIntSeqSet") {
            TIntSeqSet tmp = (TIntSeqSet) source.update(tseq);
            assertEquals(tmp.to_string(), ((TIntSeqSet)expected).to_string());
        }
    }


    @ParameterizedTest(name ="Test append sequence method")
    @MethodSource("append_sequence")
    void testAppendSequence(Temporal source, String type, TIntSeq tseq, Temporal expected) {
        functions.meos_initialize("UTC");
        if (type == "TIntSeq") {
            TIntSeq tmp = (TIntSeq) source.append_sequence(tseq);
            assertEquals(tmp.to_string(), ((TIntSeqSet)expected).to_string());
        } else if (type == "TIntSeqSet") {
            TIntSeqSet tmp = (TIntSeqSet) source.append_sequence(tseq);
            assertEquals(tmp.to_string(), ((TIntSeqSet)expected).to_string());
        }
    }





    @ParameterizedTest(name ="Test abs method")
    @MethodSource("abs")
    void testAbs(Temporal source, String type, TIntInst tseq, Temporal expected) {
        functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TNumber tmp = ((TNumber) source).abs();
            assertEquals(((TIntInst)tmp).to_string(), ((TIntInst)source).to_string());
        } else if (type == "TIntSeq") {
            TNumber tmp = ((TNumber) source).abs();
            assertEquals(((TIntSeq)tmp).to_string(), ((TIntSeq)source).to_string());
        } else if (type == "TIntSeqSet") {
            TNumber tmp = ((TNumber) source).abs();
            assertEquals(((TIntSeqSet)tmp).to_string(), ((TIntSeqSet)source).to_string());
        }
    }


    @ParameterizedTest(name ="Test delta value method")
    @MethodSource("delta_value")
    void testDeltaValue(Temporal source, String type, TIntInst tseq, Temporal expected) {
        functions.meos_initialize("UTC");
        if(type == "TIntInst"){
            TNumber tmp = ((TNumber) source).delta_value();
            assertEquals(((TIntInst)tmp).to_string(), ((TIntInst)expected).to_string());
        } else if (type == "TIntSeq") {
            TNumber tmp = ((TNumber) source).delta_value();
            assertEquals(((TIntSeq)tmp).to_string(), ((TIntSeq)expected).to_string());
        } else if (type == "TIntSeqSet") {
            TNumber tmp = ((TNumber) source).delta_value();
            assertEquals(((TIntSeqSet)tmp).to_string(), ((TIntSeqSet)expected).to_string());
        }
    }



    @ParameterizedTest(name ="Test always equal method")
    @MethodSource("always_equal")
    void testAlwaysEqual(Temporal source, String type, int arg, boolean expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TInt)source).always_equal(arg),expected);
        assertEquals(((TInt)source).never_not_equal(arg),expected);
        assertEquals(((TInt)source).ever_not_equal(arg),! expected);
    }




    @ParameterizedTest(name ="Test ever equal method")
    @MethodSource("ever_equal")
    void testEverEqual(Temporal source, String type, int arg, boolean expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TInt)source).ever_equal(arg),expected);
        assertEquals(((TInt)source).always_not_equal(arg),!expected);
        assertEquals(((TInt)source).never_equal(arg),! expected);
    }


    @ParameterizedTest(name ="Test ever greater method")
    @MethodSource("ever_greater")
    void testEverGreater(Temporal source, String type, int arg, boolean expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TInt)source).always_less(arg),expected);
        assertEquals(((TInt)source).never_greater_or_equal(arg),expected);
        assertEquals(((TInt)source).ever_greater_or_equal(arg),! expected);
    }



}
