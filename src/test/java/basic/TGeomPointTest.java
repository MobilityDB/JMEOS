package basic;

import functions.functions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import types.basic.tfloat.TFloat;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.basic.tint.TInt;
import types.basic.tint.TIntInst;
import types.basic.tint.TIntSeq;
import types.basic.tint.TIntSeqSet;
import types.basic.tnumber.TNumber;
import types.basic.tpoint.TPoint;
import types.basic.tpoint.tgeom.TGeomPoint;
import types.basic.tpoint.tgeom.TGeomPointInst;
import types.basic.tpoint.tgeom.TGeomPointSeq;
import types.basic.tpoint.tgeom.TGeomPointSeqSet;
import types.boxes.Box;
import types.boxes.STBox;
import types.boxes.TBox;
import types.collections.number.FloatSpan;
import types.collections.number.IntSpanSet;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import types.temporal.TInterpolation;
import types.temporal.Temporal;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TGeomPointTest {




    private static Stream<Arguments> fromtemporal() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1.5 1.5)@2019-09-01"), "TGeomPointInst",TInterpolation.NONE, "POINT(1 1)@2019-09-01 00:00:00+00"),
                Arguments.of(new TGeomPointSeq("{Point(1.5 1.5)@2019-09-01, Point(2.5 2.5)@2019-09-02}"), "TGeomPointSeq",TInterpolation.DISCRETE, "{POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00}"),
                Arguments.of(new TGeomPointSeq("[Point(1.5 1.5)@2019-09-01, Point(2.5 2.5)@2019-09-02]"), "TGeomPointSeq",TInterpolation.LINEAR, "[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00]"),
                Arguments.of(new TGeomPointSeqSet("{[Point(1.5 1.5)@2019-09-01, Point(2.5 2.5)@2019-09-02], [Point(1.5 1.5)@2019-09-03, Point(1.5 1.5)@2019-09-05]}"), "TGeomPointSeqSet",TInterpolation.LINEAR, "{[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00], [POINT(1 1)@2019-09-03 00:00:00+00, POINT(1 1)@2019-09-05 00:00:00+00]}")
        );
    }



    static Stream<Arguments> from_time() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TimestampSet("{2019-09-01, 2019-09-02}"), "TGeomPointSeq", TInterpolation.DISCRETE),
                Arguments.of(new Period("[2019-09-01, 2019-09-02]"), "TGeomPointSeq", TInterpolation.LINEAR),
                Arguments.of(new PeriodSet("{[2019-09-01, 2019-09-02],[2019-09-03, 2019-09-05]}"), "TGeomPointSeqSet", TInterpolation.LINEAR)
        );
    }



    private static Stream<Arguments> fromstring() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst",TInterpolation.NONE, "POINT(1 1)@2019-09-01 00:00:00+00"),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq",TInterpolation.DISCRETE, "{POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00}"),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq",TInterpolation.LINEAR, "[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00]"),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet",TInterpolation.LINEAR, "{[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00], [POINT(1 1)@2019-09-03 00:00:00+00, POINT(1 1)@2019-09-05 00:00:00+00]}")
        );
    }


    private static Stream<Arguments> bounding() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", new STBox("STBOX XT(((1, 1),(1, 1)),[2019-09-01, 2019-09-01])")    ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", new STBox("STBOX XT(((1, 1),(2, 2)),[2019-09-01, 2019-09-02])")  ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", new STBox("STBOX XT(((1, 1),(2, 2)),[2019-09-01, 2019-09-02])") ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", new STBox("STBOX XT(((1, 1),(2, 2)),[2019-09-01, 2019-09-05])")  )
        );
    }


    private static Stream<Arguments> fromstart() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", "POINT (1 1)"    ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", "POINT (1 1)" ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", "POINT (1 1)" ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", "POINT (1 1)"  )
        );
    }


    private static Stream<Arguments> endstart() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", "POINT (1 1)"    ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", "POINT (2 2)" ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", "POINT (2 2)" ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", "POINT (1 1)"  )
        );
    }


    private static Stream<Arguments> test_time() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", new PeriodSet("{[2019-09-01, 2019-09-01]}") ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", new PeriodSet("{[2019-09-01, 2019-09-01], [2019-09-02, 2019-09-02]}") ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", new PeriodSet("{[2019-09-01, 2019-09-02]}") ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", new PeriodSet("{[2019-09-01, 2019-09-02], [2019-09-03, 2019-09-05]}") )
        );
    }


    private static Stream<Arguments> period() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", new Period("[2019-09-01, 2019-09-01]") ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", new Period("[2019-09-01, 2019-09-02]") ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", new Period("[2019-09-01, 2019-09-02]") ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", new Period("[2019-09-01, 2019-09-05]") )
        );
    }

    private static Stream<Arguments> num_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", 1 ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", 2 ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", 2 ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", 4 )
        );
    }


    private static Stream<Arguments> start_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of("Point(1 1)@2019-09-01", "TGeomPointInst", new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}", "TGeomPointSeq", new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]", "TGeomPointSeq", new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}", "TGeomPointSeqSet", new TGeomPointInst("Point(1 1)@2019-09-01") )
        );
    }




    private static Stream<Arguments> end_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of("Point(1 1)@2019-09-01", "TGeomPointInst", new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}", "TGeomPointSeq", new TGeomPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]", "TGeomPointSeq", new TGeomPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}", "TGeomPointSeqSet", new TGeomPointInst("Point(1 1)@2019-09-05") )
        );
    }


    private static Stream<Arguments> asmfjson() {
        functions.meos_initialize("UTC");
        String jsonString1 = "{\n" +
                "  \"type\": \"MovingGeomPoint\",\n" +
                "  \"bbox\": [\n" +
                "    [\n" +
                "      1,\n" +
                "      1\n" +
                "    ],\n" +
                "    [\n" +
                "      1,\n" +
                "      1\n" +
                "    ]\n" +
                "  ],\n" +
                "  \"period\": {\n" +
                "    \"begin\": \"2019-09-01T00:00:00+00\",\n" +
                "    \"end\": \"2019-09-01T00:00:00+00\",\n" +
                "    \"lower_inc\": true,\n" +
                "    \"upper_inc\": true\n" +
                "  },\n" +
                "  \"coordinates\": [\n" +
                "    [\n" +
                "      1,\n" +
                "      1\n" +
                "    ]\n" +
                "  ],\n" +
                "  \"datetimes\": [\n" +
                "    \"2019-09-01T00:00:00+00\"\n" +
                "  ],\n" +
                "  \"interpolation\": \"None\"\n" +
                "}";


        String jsonString2 =
                "{\n" +
                        "  \"type\": \"MovingGeomPoint\",\n" +
                        "  \"bbox\": [\n" +
                        "    [\n" +
                        "      1,\n" +
                        "      1\n" +
                        "    ],\n" +
                        "    [\n" +
                        "      2,\n" +
                        "      2\n" +
                        "    ]\n" +
                        "  ],\n" +
                        "  \"period\": {\n" +
                        "    \"begin\": \"2019-09-01T00:00:00+00\",\n" +
                        "    \"end\": \"2019-09-02T00:00:00+00\",\n" +
                        "    \"lower_inc\": true,\n" +
                        "    \"upper_inc\": true\n" +
                        "  },\n" +
                        "  \"coordinates\": [\n" +
                        "    [\n" +
                        "      1,\n" +
                        "      1\n" +
                        "    ],\n" +
                        "    [\n" +
                        "      2,\n" +
                        "      2\n" +
                        "    ]\n" +
                        "  ],\n" +
                        "  \"datetimes\": [\n" +
                        "    \"2019-09-01T00:00:00+00\",\n" +
                        "    \"2019-09-02T00:00:00+00\"\n" +
                        "  ],\n" +
                        "  \"lower_inc\": true,\n" +
                        "  \"upper_inc\": true,\n" +
                        "  \"interpolation\": \"Discrete\"\n" +
                        "}";

        String jsonString3 = "{\n" +
                "  \"type\": \"MovingGeomPoint\",\n" +
                "  \"bbox\": [\n" +
                "    [\n" +
                "      1,\n" +
                "      1\n" +
                "    ],\n" +
                "    [\n" +
                "      2,\n" +
                "      2\n" +
                "    ]\n" +
                "  ],\n" +
                "  \"period\": {\n" +
                "    \"begin\": \"2019-09-01T00:00:00+00\",\n" +
                "    \"end\": \"2019-09-02T00:00:00+00\",\n" +
                "    \"lower_inc\": true,\n" +
                "    \"upper_inc\": true\n" +
                "  },\n" +
                "  \"coordinates\": [\n" +
                "    [\n" +
                "      1,\n" +
                "      1\n" +
                "    ],\n" +
                "    [\n" +
                "      2,\n" +
                "      2\n" +
                "    ]\n" +
                "  ],\n" +
                "  \"datetimes\": [\n" +
                "    \"2019-09-01T00:00:00+00\",\n" +
                "    \"2019-09-02T00:00:00+00\"\n" +
                "  ],\n" +
                "  \"lower_inc\": true,\n" +
                "  \"upper_inc\": true,\n" +
                "  \"interpolation\": \"Linear\"\n" +
                "}";

        String jsonString4 = "{\n" +
                "  \"type\": \"MovingGeomPoint\",\n" +
                "  \"bbox\": [\n" +
                "    [\n" +
                "      1,\n" +
                "      1\n" +
                "    ],\n" +
                "    [\n" +
                "      2,\n" +
                "      2\n" +
                "    ]\n" +
                "  ],\n" +
                "  \"period\": {\n" +
                "    \"begin\": \"2019-09-01T00:00:00+00\",\n" +
                "    \"end\": \"2019-09-05T00:00:00+00\",\n" +
                "    \"lower_inc\": true,\n" +
                "    \"upper_inc\": true\n" +
                "  },\n" +
                "  \"sequences\": [\n" +
                "    {\n" +
                "      \"coordinates\": [\n" +
                "        [\n" +
                "          1,\n" +
                "          1\n" +
                "        ],\n" +
                "        [\n" +
                "          2,\n" +
                "          2\n" +
                "        ]\n" +
                "      ],\n" +
                "      \"datetimes\": [\n" +
                "        \"2019-09-01T00:00:00+00\",\n" +
                "        \"2019-09-02T00:00:00+00\"\n" +
                "      ],\n" +
                "      \"lower_inc\": true,\n" +
                "      \"upper_inc\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"coordinates\": [\n" +
                "        [\n" +
                "          1,\n" +
                "          1\n" +
                "        ],\n" +
                "        [\n" +
                "          1,\n" +
                "          1\n" +
                "        ]\n" +
                "      ],\n" +
                "      \"datetimes\": [\n" +
                "        \"2019-09-03T00:00:00+00\",\n" +
                "        \"2019-09-05T00:00:00+00\"\n" +
                "      ],\n" +
                "      \"lower_inc\": true,\n" +
                "      \"upper_inc\": true\n" +
                "    }\n" +
                "  ],\n" +
                "  \"interpolation\": \"Linear\"\n" +
                "}";


        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", jsonString1 ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", jsonString2 ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", jsonString3 ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", jsonString4 )
        );
    }




    private static Stream<Arguments> min_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", new TGeomPointInst("Point(1 1)@2019-09-01") )
        );
    }


    private static Stream<Arguments> max_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq", new TGeomPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeomPointSeq", new TGeomPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", new TGeomPointInst("Point(2 2)@2019-09-02") )
        );
    }


    private static Stream<Arguments> instantn() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), 0, new TGeomPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), 1, new TGeomPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), 1, new TGeomPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), 2, new TGeomPointInst("Point(1 1)@2019-09-03") )
        );
    }


    private static Stream<Arguments> num_timestamps() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), 1),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), 2),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), 2),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), 4)
        );
    }



    private static Stream<Arguments> start_timestamps() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), LocalDateTime.of(2019, 9, 1, 0, 0,0))
        );
    }


    private static Stream<Arguments> end_timestamps() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), LocalDateTime.of(2019, 9, 5, 0, 0,0))
        );
    }



    private static Stream<Arguments> hash() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), 382694564),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), 1664033448),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), 1664033448),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), 2878566103l)
        );
    }


    private static Stream<Arguments> lower_inc() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), true),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), true)
        );
    }



    private static Stream<Arguments> length() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), 0),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), 0),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), 1.4142135381698608),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), 1.4142135381698608)
        );
    }


    private static Stream<Arguments> cumullength() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), new TFloatInst("0@2019-09-01")),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), new TFloatSeq("[0@2019-09-01, 1,4142135623730951@2019-09-02]")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), new TFloatSeqSet("{[0@2019-09-01, 1,4142135623730951@2019-09-02], [1,4142135623730951@2019-09-03, 1,4142135623730951@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> speed() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), null),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), new TFloatSeq("[1.8157@2019-09-01, 1.8157@2019-09-02]")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), new TFloatSeqSet("{[1.8157@2019-09-01, 1.8157@2019-09-02],[0@2019-09-03, 0@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> xy() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), new TFloatInst("1@2019-09-01")),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), new TFloatSeq("[1@2019-09-01, 2@2019-09-02]")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), new TFloatSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> xyz() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1 1)@2019-09-01"), new TFloatInst("1@2019-09-01")),
                Arguments.of(new TGeomPointSeq("[Point(1 1 1)@2019-09-01, Point(2 2 2)@2019-09-02]"), new TFloatSeq("[1@2019-09-01, 2@2019-09-02]")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1 1)@2019-09-01, Point(2 2 2)@2019-09-02], [Point(1 1 1)@2019-09-03, Point(1 1 1)@2019-09-05]}"), new TFloatSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> hasz() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), false),
                Arguments.of(new TGeomPointSeq("[Point(1 1 1)@2019-09-01, Point(2 2 2)@2019-09-02]"), true),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1 1)@2019-09-01, Point(2 2 2)@2019-09-02], [Point(1 1 1)@2019-09-03, Point(1 1 1)@2019-09-05]}"), true)
        );
    }


    private static Stream<Arguments> is_simple() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), true),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), true),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), true)
        );
    }


    private static Stream<Arguments> angular_difference() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", new TFloatSeqSet("{0@2019-09-01,0@2019-09-02}"))
        );
    }



    private static Stream<Arguments> togeom() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), new TGeomPointInst("Point(1 1)@2019-09-01"))
                //Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}")),
                //Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]")),
                //Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> to_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), new TGeomPointInst("Point(1 1)@2019-09-01")),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01}"), new TGeomPointInst("Point(1 1)@2019-09-01")),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01]"), new TGeomPointInst("Point(1 1)@2019-09-01")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01]}"), new TGeomPointInst("Point(1 1)@2019-09-01"))
        );
    }



    private static Stream<Arguments> to_sequence() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), TInterpolation.LINEAR, new TGeomPointSeq("[Point(1 1)@2019-09-01]")),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), TInterpolation.DISCRETE, new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}")),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), TInterpolation.LINEAR, new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]}"), TInterpolation.LINEAR, new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"))
        );
    }


    private static Stream<Arguments> to_sequenceset() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), TInterpolation.LINEAR, new TGeomPointSeqSet("{[Point(1 1)@2019-09-01]}")),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), TInterpolation.LINEAR, new TGeomPointSeqSet("{[Point(1 1)@2019-09-01], [Point(2 2)@2019-09-02]}")),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), TInterpolation.LINEAR, new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]}")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]}"), TInterpolation.LINEAR, new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]}"))
        );
    }



    private static Stream<Arguments> set_interp() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1 1)@2019-09-01"), "TGeomPointInst", TInterpolation.DISCRETE, new TGeomPointSeq("{Point(1 1)@2019-09-01}")),
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"),"TGeomPointSeq", TInterpolation.DISCRETE, new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}")),
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01]"),"TGeomPointSeq", TInterpolation.DISCRETE, new TGeomPointSeq("{Point(1 1)@2019-09-01}")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01],[Point(2 2)@2019-09-03]}"), "TGeomPointSeqSet", TInterpolation.DISCRETE, new TGeomPointSeq("{Point(1 1)@2019-09-01,Point(2 2)@2019-09-03}"))
        );
    }


    private static Stream<Arguments> round() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointInst("Point(1.123456789 1.123456789)@2019-09-01"), "TGeomPointInst", new TGeomPointInst("Point(1.12 1.12)@2019-09-01")),
                Arguments.of(new TGeomPointSeq("{Point(1.123456789 1.123456789)@2019-09-01, Point(2.123456789 2.123456789)@2019-09-02}"),"TGeomPointSeq", new TGeomPointSeq("{Point(1.12 1.12)@2019-09-01,Point(2.12 2.12)@2019-09-02}")),
                Arguments.of(new TGeomPointSeq("[Point(1.123456789 1.123456789)@2019-09-01, Point(2.123456789 2.123456789)@2019-09-02]"),"TGeomPointSeq", new TGeomPointSeq("[Point(1.12 1.12)@2019-09-01,Point(2.12 2.12)@2019-09-02]")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1.123456789 1.123456789)@2019-09-01, Point(2.123456789 2.123456789)@2019-09-02], [Point(1.123456789 1.123456789)@2019-09-03, Point(1.123456789 1.123456789)@2019-09-05]}"), "TGeomPointSeqSet", new TGeomPointSeq("{[Point(1.12 1.12)@2019-09-01,Point(2.12 2.12)@2019-09-02], [Point(1.12 1.12)@2019-09-03,Point(1.12 1.12)@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> insert() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeomPointSeq",new TGeomPointSeq("{Point(1 1)@2019-09-03}"), new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02, Point(1 1)@2019-09-03}")  ),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", new TGeomPointSeq("[Point(1 1)@2019-09-06]"), new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05],[Point(1 1)@2019-09-06]}"))
        );
    }



    private static Stream<Arguments> append_sequence() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"),"TGeomPointSeq", new TGeomPointSeq("[Point(1 1)@2019-09-03]"), new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03]}")),
                Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeomPointSeqSet", new TGeomPointSeq("[Point(1 1)@2019-09-06]"), new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05],[Point(1 1)@2019-09-06]}"))
        );
    }
















    /* ----------------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------------- */





    @ParameterizedTest(name="Test from temporal constructor")
    @MethodSource("fromtemporal")
    void testFromTemporalConstructor(TGeomPoint source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst") {
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point point = factory4326.createPoint(new Coordinate(1, 1));
            TGeomPointInst new_tg = (TGeomPointInst) source.from_base_temporal(point, (Temporal)source);
            assertEquals(new_tg.interpolation(),interpolation);
            assertTrue(new_tg instanceof TGeomPointInst);
            assertEquals(new_tg.interpolation(),interpolation);
        } else if (type == "TGeomPointSeq") {
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point point = factory4326.createPoint(new Coordinate(1, 1));
            TGeomPointSeq new_tg = (TGeomPointSeq) source.from_base_temporal(point, (Temporal)source);
            assertTrue(new_tg instanceof TGeomPointSeq);
        } else if (type == "TGeomPointSeqSet"){
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point point = factory4326.createPoint(new Coordinate(1, 1));
            TGeomPointSeqSet new_tg = (TGeomPointSeqSet) source.from_base_temporal(point, (Temporal)source);
            assertTrue(new_tg instanceof TGeomPointSeqSet);
        }
    }



    @ParameterizedTest(name="Test from time constructor")
    @MethodSource("from_time")
    void testFromBaseTimeConstructor(Time source, String type, TInterpolation interpolation) {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst") {
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));
            Point p = factory4326.createPoint(new Coordinate(1, 1));
            TGeomPointInst tg = (TGeomPointInst) TGeomPoint.from_base_time(p,source,interpolation);
            assertEquals(tg.interpolation(),interpolation);
        } else if (type == "TGeomPointSeq") {
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));
            Point p = factory4326.createPoint(new Coordinate(1, 1));
            TGeomPointSeq tg = (TGeomPointSeq) TGeomPoint.from_base_time(p,source,interpolation);
        } else if (type == "TGeomPointSeqSet"){
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));
            Point p = factory4326.createPoint(new Coordinate(1, 1));
            TGeomPointSeqSet tg = (TGeomPointSeqSet) TGeomPoint.from_base_time(p,source,interpolation);
        }
    }



    @ParameterizedTest(name="Test string constructor")
    @MethodSource("fromstring")
    void testFromStringConstructor(TGeomPoint source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst") {
            TGeomPointInst ti = new TGeomPointInst(expected);
            assertTrue(ti instanceof TGeomPointInst);
            assertEquals(interpolation, ti.interpolation());
            assertEquals(ti.to_string(),expected);
        } else if (type == "TGeomPointSeq") {
            TGeomPointSeq ti = new TGeomPointSeq(expected);
            assertTrue(ti instanceof TGeomPointSeq);
            assertEquals(interpolation, ti.interpolation());
            assertEquals(ti.to_string(),expected);
        } else if (type == "TGeomPointSeqSet"){
            TGeomPointSeqSet ti = new TGeomPointSeqSet(expected);
            assertTrue(ti instanceof TGeomPointSeqSet);
            assertEquals(interpolation, ti.interpolation());
            assertEquals(ti.to_string(),expected);
        }
    }


    @ParameterizedTest(name="Test copy constructor")
    @MethodSource("fromstring")
    void testCopyConstructor(Temporal source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst") {
            TGeomPointInst ti = (TGeomPointInst) source.copy();
            assertTrue(ti instanceof TGeomPointInst);
            assertEquals(interpolation, ti.interpolation());
            assertEquals(ti.to_string(),((TGeomPointInst)source).to_string());
        } else if (type == "TGeomPointSeq") {
            TGeomPointSeq ti = (TGeomPointSeq) source.copy();
            assertTrue(ti instanceof TGeomPointSeq);
            assertEquals(ti.to_string(),expected);
            assertEquals(ti.to_string(),((TGeomPointSeq)source).to_string());
        } else if (type == "TGeomPointSeqSet"){
            TGeomPointSeqSet ti = (TGeomPointSeqSet) source.copy();
            assertTrue(ti instanceof TGeomPointSeqSet);
            assertEquals(ti.to_string(),expected);
            assertEquals(ti.to_string(),((TGeomPointSeqSet)source).to_string());
        }
    }


    @ParameterizedTest(name="Test bounding method")
    @MethodSource("bounding")
    void testBounding(TGeomPoint source, String type, STBox expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst") {
            //assertEquals(source.bounding_box_point().toString(15), expected.toString(15));
        } else if (type == "TGeomPointSeq") {
            assertEquals(source.bounding_box_point().toString(15), expected.toString(15));
        } else if (type == "TGeomPointSeqSet"){
            assertEquals(source.bounding_box_point().toString(15), expected.toString(15));
        }
    }



    @ParameterizedTest(name="Test interpolation method")
    @MethodSource("fromstring")
    void testInterpolation(Temporal source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst") {
            assertEquals(source.interpolation(),interpolation);
        } else if (type == "TGeomPointSeq") {
            assertEquals(source.interpolation(),interpolation);
        } else if (type == "TGeomPointSeqSet"){
            assertEquals(source.interpolation(),interpolation);
        }
    }



    @ParameterizedTest(name="Test as mfjson method")
    @MethodSource("asmfjson")
    void testAsmfjson(Temporal source, String type, String expected) {
        functions.meos_initialize("UTC");
        assertEquals(source.as_mfjson(), expected);
    }


    @ParameterizedTest(name="Test start value method")
    @MethodSource("fromstart")
    void testStartvalue(TGeomPoint source, String type,  String expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.start_value(15).toString(), expected);
    }


    @ParameterizedTest(name="Test end value method")
    @MethodSource("endstart")
    void testEndvalue(TGeomPoint source, String type,  String expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.end_value(15).toString(), expected);
    }


    @ParameterizedTest(name="Test time method")
    @MethodSource("test_time")
    void testTime(TGeomPoint source, String type,  PeriodSet expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((Temporal)source).time().toString(), expected.toString());
    }



    @ParameterizedTest(name="Test period method")
    @MethodSource("period")
    void testPeriod(TGeomPoint source, String type,  Period expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((Temporal)source).period().toString(), expected.toString());
    }


    @ParameterizedTest(name="Test timespan method")
    @MethodSource("period")
    void testTimeSpan(TGeomPoint source, String type,  Period expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((Temporal)source).period().toString(), expected.toString());
    }

    @ParameterizedTest(name="Test num instant method")
    @MethodSource("num_instant")
    void testNumInst(TGeomPoint source, String type,  int expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((Temporal)source).num_instants(), expected);
    }


    @ParameterizedTest(name="Test start instant method")
    @MethodSource("start_instant")
    void testStartInstant(String source, String type,  TGeomPoint expected) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst") {
            TGeomPointInst tg = new TGeomPointInst(source);
            TGeomPointInst new_tg = (TGeomPointInst) tg.start_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        } else if (type == "TGeomPointSeq") {
            TGeomPointSeq tg = new TGeomPointSeq(source);
            TGeomPointInst new_tg = (TGeomPointInst) tg.start_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        } else if (type == "TGeomPointSeqSet"){
            TGeomPointSeqSet tg = new TGeomPointSeqSet(source);
            TGeomPointInst new_tg = (TGeomPointInst) tg.start_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        }
    }



    @ParameterizedTest(name="Test end instant method")
    @MethodSource("end_instant")
    void testEndInstant(String source, String type,  TGeomPoint expected) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst") {
            TGeomPointInst tg = new TGeomPointInst(source);
            TGeomPointInst new_tg = (TGeomPointInst) tg.end_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        } else if (type == "TGeomPointSeq") {
            TGeomPointSeq tg = new TGeomPointSeq(source);
            TGeomPointInst new_tg = (TGeomPointInst) tg.end_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        } else if (type == "TGeomPointSeqSet"){
            TGeomPointSeqSet tg = new TGeomPointSeqSet(source);
            TGeomPointInst new_tg = (TGeomPointInst) tg.end_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        }
    }


    @ParameterizedTest(name="Test min instant method")
    @MethodSource("min_instant")
    void testMinInst(Temporal source, String type,  TGeomPointInst expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((TGeomPointInst)source.min_instant()).to_string(), expected.to_string());
    }



    @ParameterizedTest(name="Test max instant method")
    @MethodSource("max_instant")
    void testMaxInst(Temporal source, String type,  TGeomPointInst expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((TGeomPointInst)source.max_instant()).to_string(), expected.to_string());
    }


    @ParameterizedTest(name="Test instant n method")
    @MethodSource("instantn")
    void testInstN(Temporal source, int n,  TGeomPointInst expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((TGeomPointInst)source.instant_n(n)).to_string(), expected.to_string());
    }


    @ParameterizedTest(name="Test num timestamps method")
    @MethodSource("num_timestamps")
    void testNumTimestamps(Temporal source, int n) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.num_timestamps(), n);
    }


    @ParameterizedTest(name="Test start timestamps method")
    @MethodSource("start_timestamps")
    void testStartTimestamps(Temporal source, LocalDateTime local) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.start_timestamp(), local);
    }


    @ParameterizedTest(name="Test end timestamps method")
    @MethodSource("end_timestamps")
    void testEndTimestamps(Temporal source, LocalDateTime local) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.end_timestamp(), local);
    }


    @ParameterizedTest(name="Test Hash method")
    @MethodSource("hash")
    void testHash(Temporal source, long hash) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.hash(), hash);
    }


    @ParameterizedTest(name="Test length method")
    @MethodSource("length")
    void testLength(TGeomPoint source, double hash) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((TPoint)source).length(), hash);
    }


    @ParameterizedTest(name="Test cumulative length method")
    @MethodSource("cumullength")
    void testCumulLength(TGeomPoint source, TFloat tfloat) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals((source).cumulative_length().to_string(15), tfloat.to_string(15));
    }


    @ParameterizedTest(name="Test x y  method")
    @MethodSource("xy")
    void testXY(TGeomPoint source, TFloat tfloat) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.x().to_string(15), tfloat.to_string(15));
        assertEquals(source.y().to_string(15), tfloat.to_string(15));
    }


    @ParameterizedTest(name="Test x y z method")
    @MethodSource("xyz")
    void testXYZ(TGeomPoint source, TFloat tfloat) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.x().to_string(15), tfloat.to_string(15));
        assertEquals(source.y().to_string(15), tfloat.to_string(15));
        assertEquals(source.z().to_string(15), tfloat.to_string(15));
    }


    @ParameterizedTest(name="Test hasz method")
    @MethodSource("hasz")
    void testHasz(TGeomPoint source, boolean val) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.has_z(), val);
    }


    @ParameterizedTest(name="Test is simple method")
    @MethodSource("is_simple")
    void testIsSimple(TGeomPoint source, boolean val) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.is_simple(), val);
    }


    @ParameterizedTest(name="Test srid method")
    @MethodSource("is_simple")
    void testSRID(TGeomPoint source, boolean val) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.srid(), 0);
    }



    @ParameterizedTest(name="Test angular difference method")
    @MethodSource("angular_difference")
    void testAngula(TGeomPoint source, String type, TFloat val) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointSeq"){
            TFloatSeqSet tf = (TFloatSeqSet) source.angular_difference().to_degrees(true);
            assertEquals(tf.to_string(15), val.to_string(15));
        } else if (type == "TGeomPointSeqSet") {
            TFloatSeqSet tf = (TFloatSeqSet) source.angular_difference().to_degrees(true);
            assertEquals(tf.to_string(15), val.to_string(15));
        }

    }


    @ParameterizedTest(name="Test to instant method")
    @MethodSource("to_instant")
    void testToInstant(Temporal source, TGeomPointInst TGeom) throws ParseException {
        functions.meos_initialize("UTC");
        TGeomPointInst tmp = (TGeomPointInst) source.to_instant();
        assertTrue(tmp instanceof TGeomPointInst);
        assertEquals(tmp.to_string(),TGeom.to_string());
    }


    @ParameterizedTest(name="Test to sequence method")
    @MethodSource("to_sequence")
    void testToSequence(Temporal source, TInterpolation interpolation, TGeomPointSeq TGeom) throws ParseException {
        functions.meos_initialize("UTC");
        TGeomPointSeq tmp = (TGeomPointSeq) source.to_sequence(interpolation);
        assertTrue(tmp instanceof TGeomPointSeq);
        assertEquals(tmp.to_string(),TGeom.to_string());
    }


    @ParameterizedTest(name="Test to sequenceset method")
    @MethodSource("to_sequenceset")
    void testToSequenceSet(Temporal source, TInterpolation interpolation, TGeomPointSeqSet TGeom) throws ParseException {
        functions.meos_initialize("UTC");
        TGeomPointSeqSet tmp = (TGeomPointSeqSet) source.to_sequenceset(interpolation);
        assertTrue(tmp instanceof TGeomPointSeqSet);
        assertEquals(tmp.to_string(),TGeom.to_string());
    }


    @ParameterizedTest(name="Test set interpolation method")
    @MethodSource("set_interp")
    void testSetInterp(Temporal source, String type, TInterpolation interpolation, TGeomPointSeq TGeom) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointInst"){
            TGeomPointInst tmp = (TGeomPointInst) source.set_interpolation(interpolation);
            assertTrue(tmp instanceof TGeomPointInst);
            assertEquals(tmp.to_string(),TGeom.to_string());
        } else if (type == "TGeomPointSeq") {
            TGeomPointSeq tmp = (TGeomPointSeq) source.set_interpolation(interpolation);
            assertTrue(tmp instanceof TGeomPointSeq);
            assertEquals(tmp.to_string(),TGeom.to_string());
        } else if (type == "TGeomPointSeqSet"){
            TGeomPointSeqSet tmp = (TGeomPointSeqSet) source.set_interpolation(interpolation);
            assertTrue(tmp instanceof TGeomPointSeqSet);
            assertEquals(tmp.to_string(),TGeom.to_string());
        }

    }



    @ParameterizedTest(name="Test round method")
    @MethodSource("round")
    void testRound(TPoint source, String type, TPoint TGeom) throws ParseException {
        functions.meos_initialize("UTC");
        if(type == "TGeomPointInst" ){
            assertTrue(source instanceof TGeomPointInst);
            assertEquals(source.to_string(),TGeom.to_string());
        } else if (type == "TGeomPointSeq" ) {
            assertTrue(source instanceof TGeomPointSeq);
            assertEquals(source.to_string(),TGeom.to_string());
        } else if (type == "TGeomPointSeqSet" ) {
            assertTrue(source instanceof TGeomPointSeqSet);
            assertEquals(source.to_string(),TGeom.to_string());
        }
    }


    @ParameterizedTest(name="Test insert method")
    @MethodSource("insert")
    void testInsert(Temporal source, String type, Temporal add, Temporal expected) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeomPointSeq"){
            TGeomPointSeq TGeom = (TGeomPointSeq) source.insert(add);
            assertEquals(TGeom.to_string(), ((TGeomPointSeq) expected).to_string());
        } else if (type == "TGeomPointSeqSet") {
            TGeomPointSeqSet TGeom = (TGeomPointSeqSet) source.insert(add);
            assertEquals(TGeom.to_string(), ((TGeomPointSeqSet) expected).to_string());
        }
    }


    @ParameterizedTest(name="Test append sequence method")
    @MethodSource("append_sequence")
    void testAppendSequence(Temporal source, String type, TGeomPointSeq tgeoseq, Temporal expected) throws ParseException {
        functions.meos_initialize("UTC");

        if (type == "TGeomPointSeq"){
            TGeomPointSeq tseq = (TGeomPointSeq) source.append_sequence(tgeoseq);
            assertEquals(tseq.to_string(), ((TGeomPointSeqSet)expected).to_string() );


        } else if (type == "TGeomPointSeqSet") {
            TGeomPointSeqSet tseq = (TGeomPointSeqSet) source.append_sequence(tgeoseq);
            assertEquals(tseq.to_string(),  ((TGeomPointSeqSet) expected).to_string() );
        }


    }












}
