package utils;

import com.google.common.collect.BoundType;
import functions.functions;
import jnr.ffi.Pointer;
import com.google.common.collect.Range;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Point;
import types.temporal.TInstant;
import types.temporal.TSequence;
import types.temporal.TSequenceSet;
import types.temporal.Temporal;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Class based on the manually-defined functions from PyMeos.
 * This class contains conversions functions for MEOS/JMEOS types.
 *
 * @author Killian Monnier and Nidhal Mareghni
 * @see <a href="https://github.com/MobilityDB/PyMEOS/blob/9d84632df5a4b060d43421248c142af337ec4ddd/pymeos_cffi/pymeos_cffi/builder/build_pymeos_functions.py">...</a>
 * @since 09/08/2023
 */
public class ConversionUtils {
	
	/**
	 * Take a {@link LocalDateTime} and convert it to an {@link OffsetDateTime}.
	 *
	 * @param dt localDateTime
	 * @return offsetDateTime
	 */
	public static OffsetDateTime datetimeToTimestampTz(LocalDateTime dt) {
		functions.meos_initialize("UTC");
		String formattedDt = dt.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return functions.pg_timestamptz_in(formattedDt, -1);
	}

	
	/**
	 * Take a {@link OffsetDateTime} and convert it to an {@link LocalDateTime}.
	 *
	 * @param ts offsetDateTime
	 * @return localDateTime
	 */
	public static LocalDateTime timestamptz_to_datetime(OffsetDateTime ts) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
		// Parse the string to LocalDateTime
		return LocalDateTime.parse(functions.pg_timestamptz_out(ts), formatter);
	}


	public static Pointer timedelta_to_interval(Duration td){
		int years = 0;
		int month = 0;
		int weeks = 0;
		int days = (int)td.toDays();
		int hours = (int)td.toHours();
		int minutes = (int)td.toMinutes();
		double seconds = (double)td.toSeconds();
		return functions.pg_interval_make(years,month,weeks,days,hours,minutes,seconds);
	}

	public static Duration interval_to_timedelta(Pointer p){
		return Duration.parse(functions.pg_interval_out(p));
	}

	public static Pointer intrange_to_intspan(Range<Integer> intrange) throws SQLException {
		boolean lower_inc = intrange.lowerBoundType() == BoundType.CLOSED;
		boolean upper_inc = intrange.upperBoundType() == BoundType.CLOSED;
		return functions.intspan_make(intrange.lowerEndpoint(),intrange.upperEndpoint(),lower_inc, upper_inc);
	}

	public static Range intspan_to_intrange(Pointer span){
		BoundType lower_inc = functions.span_lower_inc(span) ? BoundType.CLOSED : BoundType.OPEN;
		BoundType upper_inc = functions.span_upper_inc(span) ? BoundType.CLOSED : BoundType.OPEN;
		return Range.range(functions.intspan_lower(span), lower_inc, functions.intspan_upper(span), upper_inc);
	}

	public static Pointer floatrange_to_floatspan(Range<Float> floatrange){
		boolean lower_inc = floatrange.lowerBoundType() == BoundType.CLOSED;
		boolean upper_inc = floatrange.upperBoundType() == BoundType.CLOSED;
		return functions.floatspan_make(floatrange.lowerEndpoint(),floatrange.upperEndpoint(),lower_inc, upper_inc);
	}

	public static Range floatspan_to_floatrange(Pointer span){
		BoundType lower_inc = functions.span_lower_inc(span) ? BoundType.CLOSED : BoundType.OPEN;
		BoundType upper_inc = functions.span_upper_inc(span) ? BoundType.CLOSED : BoundType.OPEN;
		return Range.range(functions.floatspan_lower(span), lower_inc, functions.floatspan_upper(span), upper_inc);
	}



	public static TInstant asTInstant(Temporal temporal) {
			return (TInstant) temporal;
		}

	public static TSequence asTSequence(Temporal temporal) {
			return (TSequence) temporal;
		}

	public static TSequenceSet asTSequenceSet(Temporal temporal) {
			return (TSequenceSet) temporal;
		}


	public static Pointer geo_to_gserialized(Geometry geom, boolean geodetic){
		if (geodetic){
			return geography_to_gserialized(geom);
		}
		else{
			return geometry_to_gserialized(geom);
		}
	}


	public static Pointer geometry_to_gserialized(Geometry geom){
		WKTWriter wktWriter = new WKTWriter();
		String text = wktWriter.write(geom);
		if (geom.getSRID() > 0){
			text = "SRID="+geom.getSRID()+";"+text;
		}
		Pointer ptr = functions.pgis_geometry_in(text,-1);
		return ptr;
	}


	public static Pointer geography_to_gserialized(Geometry geom){
		WKTWriter wktWriter = new WKTWriter();
		String text = wktWriter.write(geom);
		if (geom.getSRID() > 0){
			text = "SRID="+geom.getSRID()+";"+text;
		}
		Pointer ptr = functions.pgis_geography_in(text,-1);
		return ptr;
	}

	public static Point gserialized_to_shapely_point(Pointer geom, int precision) throws ParseException {
		String text = functions.gserialized_as_text(geom,precision);
		WKTReader wktReader = new WKTReader();
		Geometry geometry = wktReader.read(text);
		int srid = functions.lwgeom_get_srid(geom);
		if (srid > 0){
			geometry.setSRID(srid);
		}
		return (Point) geometry;
	}


	public static Geometry gserialized_to_shapely_geometry(Pointer geom, int precision) throws ParseException {
		String text = functions.gserialized_as_text(geom,precision);
		WKTReader wktReader = new WKTReader();
		Geometry geometry = wktReader.read(text);
		int srid = functions.lwgeom_get_srid(geom);
		if (srid > 0){
			geometry.setSRID(srid);
		}
		return geometry;
	}


	public static String hexWKBToWKB(String hexWKB) {
		if (hexWKB.length() % 2 != 0) {
			System.err.println("Invalid HexWKB format.");
			return null;
		}

		StringBuilder wkbStringBuilder = new StringBuilder();
		for (int i = 0; i < hexWKB.length(); i += 2) {
			String hexPair = hexWKB.substring(i, i + 2);
			int hexValue = Integer.parseInt(hexPair, 16);
			wkbStringBuilder.append((char) hexValue);
		}

		return wkbStringBuilder.toString();
	}


	public static LocalDateTime string_to_LocalDateTime(String value){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
		return dateTime;
	}


}
