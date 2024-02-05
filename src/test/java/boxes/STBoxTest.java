package boxes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import functions.functions;
import java.sql.SQLException;
import java.util.stream.Stream;
import types.boxes.*;
import types.collections.time.*;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.ParseException;


import static org.junit.jupiter.api.Assertions.*;

public class STBoxTest {
	private STBox stbx;
	private STBox stbz;
	private STBox stbt;
	private STBox stbxt;
	private STBox stbzt;

    public STBoxTest() throws SQLException {
		functions.meos_initialize("UTC");
		stbx = new STBox("STBOX X((1, 1),(2, 2))");
		stbz = new STBox("STBOX Z((1, 1, 1),(2, 2, 2))");
		stbt = new STBox("STBOX T([2019-09-01,2019-09-02])");
		stbxt = new STBox("STBOX XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02])");
		stbzt = new STBox("STBOX ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02])");
    }

	static Stream<Arguments> STBox_sources() throws SQLException {
		functions.meos_initialize("UTC");
		return Stream.of(
				Arguments.of(new STBox("STBOX X((1, 1),(2, 2))"), "STBOX X((1, 1),(2, 2))" ),
				Arguments.of(new STBox("STBOX Z((1, 1, 1),(2, 2, 2))"), "STBOX Z((1, 1, 1),(2, 2, 2))" ),
				Arguments.of(new STBox("STBOX T([2019-09-01,2019-09-02])"), "STBOX T([2019-09-01,2019-09-02])" ),
				Arguments.of(new STBox("STBOX XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02])"), "STBOX XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02])" ),
				Arguments.of(new STBox("STBOX ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02])"),  "STBOX ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02])")
		);
	}



	@ParameterizedTest(name = "{index} - Parse \"{0}\" to {1} should result in \"{2}\"")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); STBox; STBOX X((1,1),(2,2))",
			"STBox T([2019-09-01,2019-09-02]); STBox; STBOX T([2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); STBox; STBOX XT(((1,1),(2,2)),[2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])"
	}, delimiter = ';')
	@DisplayName("Test String Constructor")
	public void testStringConstructor(String source, String type, String expected) throws SQLException {
		//functions.meos_initialize("UTC");
		STBox stb = new STBox(source);
		assertEquals(stb.toString(15),expected);
		assertEquals(type,stb.getClass().getSimpleName());
	}

	@ParameterizedTest(name = "Test from_geometry_constructor with geometry={0}, expected={1}")
	@CsvSource(value = {
			"POINT (1 1); 'STBOX X((1,1),(1,1))'",
			"LineString(1 1, 2 2); 'STBOX X((1,1),(2,2))'"
	}, delimiter = ';')
	public void testFromGeometryConstructor(String geometryString, String expected) throws ParseException, SQLException {
		GeometryFactory geometryFactory = new GeometryFactory();
		WKTReader wktReader = new WKTReader(geometryFactory);
		Geometry pointGeom = wktReader.read(geometryString);
		STBox stb = STBox.from_geometry(pointGeom);
		assertEquals("STBox", stb.getClass().getSimpleName());
		assertEquals(expected, stb.toString(15));
	}


	@ParameterizedTest(name = "Test from_geometry_constructor with geometry={0}, expected={1}")
	@CsvSource(value = {
			"TSet; {2019-09-01, 2019-09-02}; STBOX T([2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00]))",
			"P; [2019-09-01, 2019-09-02]; STBOX T([2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00]))",
			"PSet; {[2019-09-01, 2019-09-02],[2019-09-03, 2019-09-05]}; STBOX T([2019-09-01 00:00:00+00, 2019-09-05 00:00:00+00])"
	}, delimiter = ';')
	@DisplayName("Test Time Constructor")
	public void testFromTimeConstructor(String type, String source, String expected) throws SQLException {
		//functions.meos_initialize("UTC");
		if (type == "TSet"){
			STBox stb = STBox.from_time(new TimestampSet(source));
			assertEquals("STBox", stb.getClass().getSimpleName());
			assertEquals(stb.toString(15),expected);
		} else if (type == "P") {
			STBox stb = STBox.from_time(new Period(source));
			assertEquals("STBox", stb.getClass().getSimpleName());
			assertEquals(stb.toString(15),expected);
		} else if (type == "PSet") {
			STBox stb = STBox.from_time(new PeriodSet(source));
			assertEquals("STBox", stb.getClass().getSimpleName());
			assertEquals(stb.toString(15),expected);
		}
	}



	@ParameterizedTest(name = "Test from as constructor.")
	@MethodSource("STBox_sources")
	public void testFromAsConstructor(STBox box, String str) throws SQLException {
		functions.meos_initialize("UTC");
		STBox stb = new STBox(str);
		assertTrue(stb.eq(box));
	}


	@ParameterizedTest(name = "Test copy constructor.")
	@MethodSource("STBox_sources")
	public void testCopyConstructor(STBox box, String str) throws SQLException {
		functions.meos_initialize("UTC");
		STBox stb = box.copy();
		assertTrue(stb.eq(box));
		assertFalse(stb.get_inner() == box.get_inner());

	}



	@ParameterizedTest(name = "Test str method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); STBOX X((1,1),(2,2))",
			"STBox Z((1, 1, 1),(2, 2, 2)); STBOX Z((1,1,1),(2,2,2))",
			"STBox T([2019-09-01,2019-09-02]); STBOX T([2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); STBOX XT(((1,1),(2,2)),[2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); STBOX ZT(((1,1,1),(2,2,2)),[2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])",
	}, delimiter = ';')

	public void testToString(String stbox, String expected) throws SQLException {
		assertEquals(expected, new STBox(stbox).toString(15));
	}



	@ParameterizedTest(name = "Test toGeometry method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); Polygon((1 1, 1 2, 2 2, 2 1, 1 1))",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); Polygon((1 1, 1 2, 2 2, 2 1, 1 1))",
	}, delimiter = ';')
	public void testToGeometry(String stbox, String expected) throws SQLException, ParseException {
		STBox stb = new STBox(stbox);
		Geometry geom = stb.to_geometry(15);
		Polygon poly = (Polygon) geom;
		WKTReader wktReader = new WKTReader();
		Geometry geometry = wktReader.read(expected);
		Polygon poly2 = (Polygon) geometry;
		assertTrue(poly instanceof Polygon);
		assertEquals(poly,poly2);
	}



	@ParameterizedTest(name = "Test toPeriod method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); [2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00]",
	}, delimiter = ';')
	public void testToPeriod(String stbox, String expected) throws SQLException, ParseException {
		//functions.meos_initialize("UTC");
		STBox stb = new STBox(stbox);
		Period p = stb.to_period();
		assertTrue(p instanceof Period);
		assertEquals(expected, p.toString());
	}


	@ParameterizedTest(name = "Test hasXY method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); true",
			"STBox Z((1, 1, 1),(2, 2, 2)); true",
			"STBox T([2019-09-01,2019-09-02]); false",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); true",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); true",
	}, delimiter = ';')
	public void testHasXY(String stbox, String expected) throws SQLException {
		STBox stb = new STBox(stbox);
		String val = String.valueOf(stb.has_xy());
		assertEquals(expected,val);
	}


	@ParameterizedTest(name = "Test hasZ method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); false",
			"STBox Z((1, 1, 1),(2, 2, 2)); true",
			"STBox T([2019-09-01,2019-09-02]); false",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); false",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); true",
	}, delimiter = ';')
	public void testHasZ(String stbox, String expected) throws SQLException {
		STBox stb = new STBox(stbox);
		String val = String.valueOf(stb.has_z());
		assertEquals(expected,val);
	}


	@ParameterizedTest(name = "Test hasT method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); false",
			"STBox Z((1, 1, 1),(2, 2, 2)); false",
			"STBox T([2019-09-01,2019-09-02]); true",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); true",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); true",
	}, delimiter = ';')
	public void testHasT(String stbox, String expected) throws SQLException {
		STBox stb = new STBox(stbox);
		String val = String.valueOf(stb.has_t());
		assertEquals(expected,val);
	}


	@ParameterizedTest(name = "Test Geodetic method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); false",
			"STBox Z((1, 1, 1),(2, 2, 2)); false",
			"STBox T([2019-09-01,2019-09-02]); false",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); false",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); false",
			"GEODSTBOX X((1, 1),(2, 2)); true",
			"GEODSTBOX Z((1, 1, 1),(2, 2, 2)); true",
			"GEODSTBOX T([2019-09-01,2019-09-02]); true",
			"GEODSTBOX XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); true",
			"GEODSTBOX ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); true",
	}, delimiter = ';')
	public void testGeodetic(String stbox, String expected) throws SQLException {
		STBox stb = new STBox(stbox);
		String val = String.valueOf(stb.geodetic());
		assertEquals(expected,val);
	}



	@ParameterizedTest(name = "Test SRID method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); 0",
			"STBox Z((1, 1, 1),(2, 2, 2)); 0",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); 0",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); 0",
			"GEODSTBOX X((1, 1),(2, 2)); 4326",
			"GEODSTBOX Z((1, 1, 1),(2, 2, 2)); 4326",
			"GEODSTBOX XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); 4326",
			"GEODSTBOX ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); 4326",
	}, delimiter = ';')
	public void testSrid(String stbox, String expected) throws SQLException {
		STBox stb = new STBox(stbox);
		String val = String.valueOf(stb.srid());
		assertEquals(expected,val);
	}


	@ParameterizedTest(name = "Test Set SRID method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); 0",
			"STBox Z((1, 1, 1),(2, 2, 2)); 0",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); 0",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); 0",
	}, delimiter = ';')
	public void testSetSrid(String stbox, String expected) throws SQLException {
		STBox stb = new STBox(stbox);
		STBox new_stb = stb.set_srid(5676);
		assertEquals(5676, new_stb.srid());
	}



	@ParameterizedTest(name = "Test Expand Float method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); STBox X((0, 0),(3, 3))",
			"STBox Z((1, 1, 1),(2, 2, 2)); STBox Z((0, 0, 0),(3, 3, 3))",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); STBox XT(((0, 0),(3, 3)),[2019-09-01,2019-09-02])",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); STBox ZT(((0, 0, 0),(3, 3, 3)),[2019-09-01,2019-09-02])",
	}, delimiter = ';')
	public void testExpandFloat(String stbox, String expected) throws SQLException {
		STBox stb = new STBox(stbox);
		STBox new_stb = stb.expand_numerical(1);
		STBox res = new STBox(expected);
		assertEquals(new_stb.toString(15),res.toString(15));
	}



	@ParameterizedTest(name = "Test adjacent method with stbox={0}, expected={1}")
	@CsvSource(value = {
			"STBox X((1, 1),(2, 2)); STBox X((1, 1),(3, 3)); false",
			"STBox X((1, 1),(2, 2)); STBox X((2, 2),(3, 3)); true",
			"STBox Z((1, 1, 1),(2, 2, 2)); STBox Z((1, 1, 1),(3, 3, 3)); false",
			"STBox Z((1, 1, 1),(2, 2, 2)); STBox Z((2, 2, 2),(3, 3, 3)); true",
			"STBox T([2019-09-01,2019-09-02]); STBox T([2019-09-01,2019-09-03]); false",
			"STBox T([2019-09-01,2019-09-02]); STBox T([2019-09-02,2019-09-03]); true",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); STBox XT(((1, 1),(3, 3)),[2019-09-01,2019-09-03]); false",
			"STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02]); STBox XT(((2, 2),(3, 3)),[2019-09-02,2019-09-03]); true",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); STBox ZT(((1, 1, 1),(3, 3, 3)),[2019-09-01,2019-09-03]); false",
			"STBox ZT(((1, 1, 1),(2, 2, 2)),[2019-09-01,2019-09-02]); STBox ZT(((1, 1, 1),(3, 3, 3)),[2019-09-01,2019-09-03]); true",
	}, delimiter = ';')
	public void testAdjacent(String stbox, String stbox2, String expected) throws SQLException {
		STBox stb = new STBox(stbox);
		STBox stb2 = new STBox(stbox2);
		//assertEquals(stb.is_adjacent(stb2),expected);
	}










}
