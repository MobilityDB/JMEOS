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
import types.basic.tpoint.tgeog.TGeogPoint;
import types.basic.tpoint.tgeog.TGeogPointInst;
import types.basic.tpoint.tgeog.TGeogPointSeq;
import types.basic.tpoint.tgeog.TGeogPointSeqSet;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TGeogPointTest {




    private static Stream<Arguments> fromtemporal() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1.5 1.5)@2019-09-01"), "TGeogPointInst",TInterpolation.NONE, "POINT(1 1)@2019-09-01 00:00:00+00"),
                Arguments.of(new TGeogPointSeq("{Point(1.5 1.5)@2019-09-01, Point(2.5 2.5)@2019-09-02}"), "TGeogPointSeq",TInterpolation.DISCRETE, "{POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00}"),
                Arguments.of(new TGeogPointSeq("[Point(1.5 1.5)@2019-09-01, Point(2.5 2.5)@2019-09-02]"), "TGeogPointSeq",TInterpolation.LINEAR, "[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00]"),
                Arguments.of(new TGeogPointSeqSet("{[Point(1.5 1.5)@2019-09-01, Point(2.5 2.5)@2019-09-02], [Point(1.5 1.5)@2019-09-03, Point(1.5 1.5)@2019-09-05]}"), "TGeogPointSeqSet",TInterpolation.LINEAR, "{[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00], [POINT(1 1)@2019-09-03 00:00:00+00, POINT(1 1)@2019-09-05 00:00:00+00]}")
        );
    }



    static Stream<Arguments> from_time() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TimestampSet("{2019-09-01, 2019-09-02}"), "TGeogPointSeq", TInterpolation.DISCRETE),
                Arguments.of(new Period("[2019-09-01, 2019-09-02]"), "TGeogPointSeq", TInterpolation.STEPWISE),
                Arguments.of(new PeriodSet("{[2019-09-01, 2019-09-02],[2019-09-03, 2019-09-05]}"), "TGeogPointSeqSet", TInterpolation.STEPWISE)
        );
    }



    private static Stream<Arguments> fromstring() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst",TInterpolation.NONE, "POINT(1 1)@2019-09-01 00:00:00+00"),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq",TInterpolation.DISCRETE, "{POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00}"),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq",TInterpolation.LINEAR, "[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00]"),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet",TInterpolation.LINEAR, "{[POINT(1 1)@2019-09-01 00:00:00+00, POINT(2 2)@2019-09-02 00:00:00+00], [POINT(1 1)@2019-09-03 00:00:00+00, POINT(1 1)@2019-09-05 00:00:00+00]}")
        );
    }


    private static Stream<Arguments> bounding() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", new STBox("GEODSTBOX XT(((1, 1),(1, 1)),[2019-09-01, 2019-09-01])")    ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", new STBox("GEODSTBOX XT(((1, 1),(2, 2)),[2019-09-01, 2019-09-02])")  ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", new STBox("GEODSTBOX XT(((1, 1),(2, 2)),[2019-09-01, 2019-09-02])") ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", new STBox("GEODSTBOX XT(((1, 1),(2, 2)),[2019-09-01, 2019-09-05])")  )
        );
    }


    private static Stream<Arguments> fromstart() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", "POINT (1 1)"    ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", "POINT (1 1)" ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", "POINT (1 1)" ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", "POINT (1 1)"  )
        );
    }


    private static Stream<Arguments> endstart() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", "POINT (1 1)"    ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", "POINT (2 2)" ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", "POINT (2 2)" ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", "POINT (1 1)"  )
        );
    }


    private static Stream<Arguments> test_time() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", new PeriodSet("{[2019-09-01, 2019-09-01]}") ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", new PeriodSet("{[2019-09-01, 2019-09-01], [2019-09-02, 2019-09-02]}") ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", new PeriodSet("{[2019-09-01, 2019-09-02]}") ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", new PeriodSet("{[2019-09-01, 2019-09-02], [2019-09-03, 2019-09-05]}") )
        );
    }


    private static Stream<Arguments> period() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", new Period("[2019-09-01, 2019-09-01]") ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", new Period("[2019-09-01, 2019-09-02]") ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", new Period("[2019-09-01, 2019-09-02]") ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", new Period("[2019-09-01, 2019-09-05]") )
        );
    }

    private static Stream<Arguments> num_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", 1 ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", 2 ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", 2 ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", 4 )
        );
    }


    private static Stream<Arguments> start_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of("Point(1 1)@2019-09-01", "TGeogPointInst", new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}", "TGeogPointSeq", new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]", "TGeogPointSeq", new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}", "TGeogPointSeqSet", new TGeogPointInst("Point(1 1)@2019-09-01") )
        );
    }




    private static Stream<Arguments> end_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of("Point(1 1)@2019-09-01", "TGeogPointInst", new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}", "TGeogPointSeq", new TGeogPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]", "TGeogPointSeq", new TGeogPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}", "TGeogPointSeqSet", new TGeogPointInst("Point(1 1)@2019-09-05") )
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
                        "  \"type\": \"MovingGeogPoint\",\n" +
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
                "  \"type\": \"MovingGeogPoint\",\n" +
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
                "  \"type\": \"MovingGeogPoint\",\n" +
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
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", jsonString1 ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", jsonString2 ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", jsonString3 ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", jsonString4 )
        );
    }




    private static Stream<Arguments> min_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", new TGeogPointInst("Point(1 1)@2019-09-01") )
        );
    }


    private static Stream<Arguments> max_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq", new TGeogPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), "TGeogPointSeq", new TGeogPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", new TGeogPointInst("Point(2 2)@2019-09-02") )
        );
    }


    private static Stream<Arguments> instantn() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), 0, new TGeogPointInst("Point(1 1)@2019-09-01") ),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), 1, new TGeogPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), 1, new TGeogPointInst("Point(2 2)@2019-09-02") ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), 2, new TGeogPointInst("Point(1 1)@2019-09-03") )
        );
    }


    private static Stream<Arguments> num_timestamps() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), 1),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), 2),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), 2),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), 4)
        );
    }



    private static Stream<Arguments> start_timestamps() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), LocalDateTime.of(2019, 9, 1, 0, 0,0))
        );
    }


    private static Stream<Arguments> end_timestamps() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), LocalDateTime.of(2019, 9, 5, 0, 0,0))
        );
    }



    private static Stream<Arguments> hash() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), 382694564),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), 1545137628),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), 1545137628),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), 1008965061)
        );
    }


    private static Stream<Arguments> lower_inc() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), true),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), true)
        );
    }



    private static Stream<Arguments> length() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), 0),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), 0),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), 156876.15625),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), 156876.15625)
        );
    }


    private static Stream<Arguments> cumullength() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), new TFloatInst("0@2019-09-01")),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), new TFloatSeq("[0@2019-09-01, 156876,14940188668@2019-09-02]")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), new TFloatSeqSet("{[0@2019-09-01, 156876,14940188668@2019-09-02], [156876,14940188668@2019-09-03, 156876,14940188668@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> speed() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), null),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), new TFloatSeq("[1.8157@2019-09-01, 1.8157@2019-09-02]")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), new TFloatSeqSet("{[1.8157@2019-09-01, 1.8157@2019-09-02],[0@2019-09-03, 0@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> xy() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), new TFloatInst("1@2019-09-01")),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), new TFloatSeq("[1@2019-09-01, 2@2019-09-02]")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), new TFloatSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> xyz() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1 1)@2019-09-01"), new TFloatInst("1@2019-09-01")),
                Arguments.of(new TGeogPointSeq("[Point(1 1 1)@2019-09-01, Point(2 2 2)@2019-09-02]"), new TFloatSeq("[1@2019-09-01, 2@2019-09-02]")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1 1)@2019-09-01, Point(2 2 2)@2019-09-02], [Point(1 1 1)@2019-09-03, Point(1 1 1)@2019-09-05]}"), new TFloatSeqSet("{[1@2019-09-01, 2@2019-09-02],[1@2019-09-03, 1@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> hasz() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), false),
                Arguments.of(new TGeogPointSeq("[Point(1 1 1)@2019-09-01, Point(2 2 2)@2019-09-02]"), true),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1 1)@2019-09-01, Point(2 2 2)@2019-09-02], [Point(1 1 1)@2019-09-03, Point(1 1 1)@2019-09-05]}"), true)
        );
    }


    private static Stream<Arguments> is_simple() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), true),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), true),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), true)
        );
    }


    private static Stream<Arguments> angular_difference() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", new TFloatSeqSet("{0@2019-09-01,0@2019-09-02}"))
        );
    }



    private static Stream<Arguments> togeom() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), new TGeomPointInst("Point(1 1)@2019-09-01"))
                //Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), new TGeomPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}")),
                //Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), new TGeomPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]")),
                //Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), new TGeomPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> to_instant() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), new TGeogPointInst("Point(1 1)@2019-09-01")),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01}"), new TGeogPointInst("Point(1 1)@2019-09-01")),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01]"), new TGeogPointInst("Point(1 1)@2019-09-01")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01]}"), new TGeogPointInst("Point(1 1)@2019-09-01"))
        );
    }



    private static Stream<Arguments> to_sequence() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), TInterpolation.LINEAR, new TGeogPointSeq("[Point(1 1)@2019-09-01]")),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), TInterpolation.DISCRETE, new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}")),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), TInterpolation.LINEAR, new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]}"), TInterpolation.LINEAR, new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"))
        );
    }


    private static Stream<Arguments> to_sequenceset() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), TInterpolation.LINEAR, new TGeogPointSeqSet("{[Point(1 1)@2019-09-01]}")),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), TInterpolation.LINEAR, new TGeogPointSeqSet("{[Point(1 1)@2019-09-01], [Point(2 2)@2019-09-02]}")),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"), TInterpolation.LINEAR, new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]}")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]}"), TInterpolation.LINEAR, new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]}"))
        );
    }



    private static Stream<Arguments> set_interp() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1 1)@2019-09-01"), "TGeogPointInst", TInterpolation.DISCRETE, new TGeogPointSeq("{Point(1 1)@2019-09-01}")),
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"),"TGeogPointSeq", TInterpolation.DISCRETE, new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}")),
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01]"),"TGeogPointSeq", TInterpolation.DISCRETE, new TGeogPointSeq("{Point(1 1)@2019-09-01}")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01],[Point(2 2)@2019-09-03]}"), "TGeogPointSeqSet", TInterpolation.DISCRETE, new TGeogPointSeq("{Point(1 1)@2019-09-01,Point(2 2)@2019-09-03}"))
        );
    }


    private static Stream<Arguments> round() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointInst("Point(1.123456789 1.123456789)@2019-09-01"), "TGeogPointInst", new TGeogPointInst("Point(1.12 1.12)@2019-09-01")),
                Arguments.of(new TGeogPointSeq("{Point(1.123456789 1.123456789)@2019-09-01, Point(2.123456789 2.123456789)@2019-09-02}"),"TGeogPointSeq", new TGeogPointSeq("{Point(1.12 1.12)@2019-09-01,Point(2.12 2.12)@2019-09-02}")),
                Arguments.of(new TGeogPointSeq("[Point(1.123456789 1.123456789)@2019-09-01, Point(2.123456789 2.123456789)@2019-09-02]"),"TGeogPointSeq", new TGeogPointSeq("[Point(1.12 1.12)@2019-09-01,Point(2.12 2.12)@2019-09-02]")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1.123456789 1.123456789)@2019-09-01, Point(2.123456789 2.123456789)@2019-09-02], [Point(1.123456789 1.123456789)@2019-09-03, Point(1.123456789 1.123456789)@2019-09-05]}"), "TGeogPointSeqSet", new TGeogPointSeq("{[Point(1.12 1.12)@2019-09-01,Point(2.12 2.12)@2019-09-02], [Point(1.12 1.12)@2019-09-03,Point(1.12 1.12)@2019-09-05]}"))
        );
    }


    private static Stream<Arguments> insert() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02}"), "TGeogPointSeq",new TGeogPointSeq("{Point(1 1)@2019-09-03}"), new TGeogPointSeq("{Point(1 1)@2019-09-01, Point(2 2)@2019-09-02, Point(1 1)@2019-09-03}")  ),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", new TGeogPointSeq("[Point(1 1)@2019-09-06]"), new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05],[Point(1 1)@2019-09-06]}"))
        );
    }



    private static Stream<Arguments> append_sequence() {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TGeogPointSeq("[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02]"),"TGeogPointSeq", new TGeogPointSeq("[Point(1 1)@2019-09-03]"), new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02], [Point(1 1)@2019-09-03]}")),
                Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05]}"), "TGeogPointSeqSet", new TGeogPointSeq("[Point(1 1)@2019-09-06]"), new TGeogPointSeqSet("{[Point(1 1)@2019-09-01, Point(2 2)@2019-09-02],[Point(1 1)@2019-09-03, Point(1 1)@2019-09-05],[Point(1 1)@2019-09-06]}"))
        );
    }
















    /* ----------------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------------- */





    @ParameterizedTest(name="Test from temporal constructor")
    @MethodSource("fromtemporal")
    void testFromTemporalConstructor(TGeogPoint source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst") {
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point point = factory4326.createPoint(new Coordinate(1, 1));
            TGeogPointInst new_tg = (TGeogPointInst) source.from_base_temporal(point, (Temporal)source);
            assertEquals(new_tg.interpolation(),interpolation);
            assertTrue(new_tg instanceof TGeogPointInst);
            assertEquals(new_tg.interpolation(),interpolation);
        } else if (type == "TGeogPointSeq") {
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point point = factory4326.createPoint(new Coordinate(1, 1));
            TGeogPointSeq new_tg = (TGeogPointSeq) source.from_base_temporal(point, (Temporal)source);
            assertTrue(new_tg instanceof TGeogPointSeq);
        } else if (type == "TGeogPointSeqSet"){
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point point = factory4326.createPoint(new Coordinate(1, 1));
            TGeogPointSeqSet new_tg = (TGeogPointSeqSet) source.from_base_temporal(point, (Temporal)source);
            assertTrue(new_tg instanceof TGeogPointSeqSet);
        }
    }



    @ParameterizedTest(name="Test string constructor")
    @MethodSource("from_time")
    void testFromBaseTimeConstructor(Time source, String type, TInterpolation interpolation) {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst") {
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point p = factory4326.createPoint(new Coordinate(1, 1));
            TGeogPointInst tg = (TGeogPointInst) TGeogPoint.from_base_time(p,source,interpolation);
            assertEquals(tg.interpolation(),interpolation);
        } else if (type == "TGeogPointSeq") {
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point p = factory4326.createPoint(new Coordinate(1, 1));
            TGeogPointSeq tg = (TGeogPointSeq) TGeogPoint.from_base_time(p,source,interpolation);
        } else if (type == "TGeogPointSeqSet"){
            GeometryFactory factory4326 = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
            Point p = factory4326.createPoint(new Coordinate(1, 1));
            TGeogPointSeqSet tg = (TGeogPointSeqSet) TGeogPoint.from_base_time(p,source,interpolation);
        }
    }



    @ParameterizedTest(name="Test string constructor")
    @MethodSource("fromstring")
    void testFromStringConstructor(TGeogPoint source, String type, TInterpolation interpolation, String expected) {
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


    @ParameterizedTest(name="Test copy constructor")
    @MethodSource("fromstring")
    void testCopyConstructor(Temporal source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst") {
            TGeogPointInst ti = (TGeogPointInst) source.copy();
            assertTrue(ti instanceof TGeogPointInst);
            assertEquals(interpolation, ti.interpolation());
            assertEquals(ti.to_string(),((TGeogPointInst)source).to_string());
        } else if (type == "TGeogPointSeq") {
            TGeogPointSeq ti = (TGeogPointSeq) source.copy();
            assertTrue(ti instanceof TGeogPointSeq);
            assertEquals(ti.to_string(),expected);
            assertEquals(ti.to_string(),((TGeogPointSeq)source).to_string());
        } else if (type == "TGeogPointSeqSet"){
            TGeogPointSeqSet ti = (TGeogPointSeqSet) source.copy();
            assertTrue(ti instanceof TGeogPointSeqSet);
            assertEquals(ti.to_string(),expected);
            assertEquals(ti.to_string(),((TGeogPointSeqSet)source).to_string());
        }
    }


    @ParameterizedTest(name="Test bounding method")
    @MethodSource("bounding")
    void testBounding(TGeogPoint source, String type, STBox expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst") {
            //assertEquals(source.bounding_box_point().toString(15), expected.toString(15));
        } else if (type == "TGeogPointSeq") {
            assertEquals(source.bounding_box_point().toString(15), expected.toString(15));
        } else if (type == "TGeogPointSeqSet"){
            assertEquals(source.bounding_box_point().toString(15), expected.toString(15));
        }
    }



    @ParameterizedTest(name="Test interpolation method")
    @MethodSource("fromstring")
    void testInterpolation(Temporal source, String type, TInterpolation interpolation, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst") {
            assertEquals(source.interpolation(),interpolation);
        } else if (type == "TGeogPointSeq") {
            assertEquals(source.interpolation(),interpolation);
        } else if (type == "TGeogPointSeqSet"){
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
    void testStartvalue(TGeogPoint source, String type,  String expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.start_value(15).toString(), expected);
    }


    @ParameterizedTest(name="Test end value method")
    @MethodSource("endstart")
    void testEndvalue(TGeogPoint source, String type,  String expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.end_value(15).toString(), expected);
    }


    @ParameterizedTest(name="Test time method")
    @MethodSource("test_time")
    void testTime(TGeogPoint source, String type,  PeriodSet expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((Temporal)source).time().toString(), expected.toString());
    }



    @ParameterizedTest(name="Test period method")
    @MethodSource("period")
    void testPeriod(TGeogPoint source, String type,  Period expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((Temporal)source).period().toString(), expected.toString());
    }


    @ParameterizedTest(name="Test timespan method")
    @MethodSource("period")
    void testTimeSpan(TGeogPoint source, String type,  Period expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((Temporal)source).period().toString(), expected.toString());
    }

    @ParameterizedTest(name="Test num instant method")
    @MethodSource("num_instant")
    void testNumInst(TGeogPoint source, String type,  int expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((Temporal)source).num_instants(), expected);
    }


    @ParameterizedTest(name="Test start instant method")
    @MethodSource("start_instant")
    void testStartInstant(String source, String type,  TGeogPoint expected) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst") {
            TGeogPointInst tg = new TGeogPointInst(source);
            TGeogPointInst new_tg = (TGeogPointInst) tg.start_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        } else if (type == "TGeogPointSeq") {
            TGeogPointSeq tg = new TGeogPointSeq(source);
            TGeogPointInst new_tg = (TGeogPointInst) tg.start_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        } else if (type == "TGeogPointSeqSet"){
            TGeogPointSeqSet tg = new TGeogPointSeqSet(source);
            TGeogPointInst new_tg = (TGeogPointInst) tg.start_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        }
    }



    @ParameterizedTest(name="Test end instant method")
    @MethodSource("end_instant")
    void testEndInstant(String source, String type,  TGeogPoint expected) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst") {
            TGeogPointInst tg = new TGeogPointInst(source);
            TGeogPointInst new_tg = (TGeogPointInst) tg.end_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        } else if (type == "TGeogPointSeq") {
            TGeogPointSeq tg = new TGeogPointSeq(source);
            TGeogPointInst new_tg = (TGeogPointInst) tg.end_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        } else if (type == "TGeogPointSeqSet"){
            TGeogPointSeqSet tg = new TGeogPointSeqSet(source);
            TGeogPointInst new_tg = (TGeogPointInst) tg.end_instant();
            assertEquals(new_tg.to_string(),expected.to_string());
        }
    }


    @ParameterizedTest(name="Test min instant method")
    @MethodSource("min_instant")
    void testMinInst(Temporal source, String type,  TGeogPointInst expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((TGeogPointInst)source.min_instant()).to_string(), expected.to_string());
    }



    @ParameterizedTest(name="Test max instant method")
    @MethodSource("max_instant")
    void testMaxInst(Temporal source, String type,  TGeogPointInst expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((TGeogPointInst)source.max_instant()).to_string(), expected.to_string());
    }


    @ParameterizedTest(name="Test instant n method")
    @MethodSource("instantn")
    void testInstN(Temporal source, int n,  TGeogPointInst expected) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((TGeogPointInst)source.instant_n(n)).to_string(), expected.to_string());
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
    void testLength(TGeogPoint source, double hash) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(((TPoint)source).length(), hash);
    }


    @ParameterizedTest(name="Test cumulative length method")
    @MethodSource("cumullength")
    void testCumulLength(TGeogPoint source, TFloat tfloat) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals((source).cumulative_length().to_string(15), tfloat.to_string(15));
    }


    @ParameterizedTest(name="Test x y  method")
    @MethodSource("xy")
    void testXY(TGeogPoint source, TFloat tfloat) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.x().to_string(15), tfloat.to_string(15));
        assertEquals(source.y().to_string(15), tfloat.to_string(15));
    }


    @ParameterizedTest(name="Test x y z method")
    @MethodSource("xyz")
    void testXYZ(TGeogPoint source, TFloat tfloat) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.x().to_string(15), tfloat.to_string(15));
        assertEquals(source.y().to_string(15), tfloat.to_string(15));
        assertEquals(source.z().to_string(15), tfloat.to_string(15));
    }


    @ParameterizedTest(name="Test hasz method")
    @MethodSource("hasz")
    void testHasz(TGeogPoint source, boolean val) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.has_z(), val);
    }


    @ParameterizedTest(name="Test is simple method")
    @MethodSource("is_simple")
    void testIsSimple(TGeogPoint source, boolean val) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.is_simple(), val);
    }


    @ParameterizedTest(name="Test srid method")
    @MethodSource("is_simple")
    void testSRID(TGeogPoint source, boolean val) throws ParseException {
        functions.meos_initialize("UTC");
        assertEquals(source.srid(), 4326);
    }



    @ParameterizedTest(name="Test angular difference method")
    @MethodSource("angular_difference")
    void testAngula(TGeogPoint source, String type, TFloat val) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointSeq"){
            TFloatSeqSet tf = (TFloatSeqSet) source.angular_difference().to_degrees(true);
            assertEquals(tf.to_string(15), val.to_string(15));
        } else if (type == "TGeogPointSeqSet") {
            TFloatSeqSet tf = (TFloatSeqSet) source.angular_difference().to_degrees(true);
            assertEquals(tf.to_string(15), val.to_string(15));
        }

    }


    @ParameterizedTest(name="Test to instant method")
    @MethodSource("to_instant")
    void testToInstant(Temporal source, TGeogPointInst tgeog) throws ParseException {
        functions.meos_initialize("UTC");
        TGeogPointInst tmp = (TGeogPointInst) source.to_instant();
        assertTrue(tmp instanceof TGeogPointInst);
        assertEquals(tmp.to_string(),tgeog.to_string());
    }


    @ParameterizedTest(name="Test to sequence method")
    @MethodSource("to_sequence")
    void testToSequence(Temporal source, TInterpolation interpolation, TGeogPointSeq tgeog) throws ParseException {
        functions.meos_initialize("UTC");
        TGeogPointSeq tmp = (TGeogPointSeq) source.to_sequence(interpolation);
        assertTrue(tmp instanceof TGeogPointSeq);
        assertEquals(tmp.to_string(),tgeog.to_string());
    }


    @ParameterizedTest(name="Test to sequenceset method")
    @MethodSource("to_sequenceset")
    void testToSequenceSet(Temporal source, TInterpolation interpolation, TGeogPointSeqSet tgeog) throws ParseException {
        functions.meos_initialize("UTC");
        TGeogPointSeqSet tmp = (TGeogPointSeqSet) source.to_sequenceset(interpolation);
        assertTrue(tmp instanceof TGeogPointSeqSet);
        assertEquals(tmp.to_string(),tgeog.to_string());
    }


    @ParameterizedTest(name="Test set interpolation method")
    @MethodSource("set_interp")
    void testSetInterp(Temporal source, String type, TInterpolation interpolation, TGeogPointSeq tgeog) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointInst"){
            TGeogPointInst tmp = (TGeogPointInst) source.set_interpolation(interpolation);
            assertTrue(tmp instanceof TGeogPointInst);
            assertEquals(tmp.to_string(),tgeog.to_string());
        } else if (type == "TGeogPointSeq") {
            TGeogPointSeq tmp = (TGeogPointSeq) source.set_interpolation(interpolation);
            assertTrue(tmp instanceof TGeogPointSeq);
            assertEquals(tmp.to_string(),tgeog.to_string());
        } else if (type == "TGeogPointSeqSet"){
            TGeogPointSeqSet tmp = (TGeogPointSeqSet) source.set_interpolation(interpolation);
            assertTrue(tmp instanceof TGeogPointSeqSet);
            assertEquals(tmp.to_string(),tgeog.to_string());
        }

    }



    @ParameterizedTest(name="Test round method")
    @MethodSource("round")
    void testRound(TPoint source, String type, TPoint tgeog) throws ParseException {
        functions.meos_initialize("UTC");
        if(type == "TGeogPointInst" ){
            assertTrue(source instanceof TGeogPointInst);
            assertEquals(source.to_string(),tgeog.to_string());
        } else if (type == "TGeogPointSeq" ) {
            assertTrue(source instanceof TGeogPointSeq);
            assertEquals(source.to_string(),tgeog.to_string());
        } else if (type == "TGeogPointSeqSet" ) {
            assertTrue(source instanceof TGeogPointSeqSet);
            assertEquals(source.to_string(),tgeog.to_string());
        }
    }


    @ParameterizedTest(name="Test insert method")
    @MethodSource("insert")
    void testInsert(Temporal source, String type, Temporal add, Temporal expected) throws ParseException {
        functions.meos_initialize("UTC");
        if (type == "TGeogPointSeq"){
            TGeogPointSeq tgeog = (TGeogPointSeq) source.insert(add);
            assertEquals(tgeog.to_string(), ((TGeogPointSeq) expected).to_string());
        } else if (type == "TGeogPointSeqSet") {
            TGeogPointSeqSet tgeog = (TGeogPointSeqSet) source.insert(add);
            assertEquals(tgeog.to_string(), ((TGeogPointSeqSet) expected).to_string());
        }
    }


    @ParameterizedTest(name="Test append sequence method")
    @MethodSource("append_sequence")
    void testAppendSequence(Temporal source, String type, TGeogPointSeq tgeoseq, Temporal expected) throws ParseException {
        functions.meos_initialize("UTC");

        if (type == "TGeogPointSeq"){
            TGeogPointSeq tseq = (TGeogPointSeq) source.append_sequence(tgeoseq);
            assertEquals(tseq.to_string(), ((TGeogPointSeqSet)expected).to_string() );


        } else if (type == "TGeogPointSeqSet") {
            TGeogPointSeqSet tseq = (TGeogPointSeqSet) source.append_sequence(tgeoseq);
            assertEquals(tseq.to_string(),  ((TGeogPointSeqSet) expected).to_string() );
        }


    }












}
