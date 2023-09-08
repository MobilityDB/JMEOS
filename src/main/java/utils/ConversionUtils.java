package utils;

//import org.postgresql.geometric.PGgeometry;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.Point;
//import org.locationtech.jts.io.ParseException;
//import org.locationtech.jts.io.WKTReader;

import com.google.common.collect.BoundType;
import functions.functions;
import jnr.ffi.Pointer;
import com.google.common.collect.Range;
import types.temporal.TInstant;
import types.temporal.TSequence;
import types.temporal.TSequenceSet;
import types.temporal.Temporal;
import types.time.Period;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Class based on the manually-defined functions from PyMeos.
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
	 * //FIXME copy of the pymeos function but do it has a real purpose ? Maybe need a refactor
	 */
	public static OffsetDateTime datetimeToTimestampTz(LocalDateTime dt) {
		String formattedDt = dt.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"));
		return functions.pg_timestamptz_in(formattedDt, -1);
	}

	
	/**
	 * Take a {@link OffsetDateTime} and convert it to an {@link LocalDateTime}.
	 *
	 * @param ts offsetDateTime
	 * @return localDateTime
	 * //FIXME copy of the pymeos function but do it has a real purpose ? Maybe need a refactor
	 */
	public static LocalDateTime timestamptz_to_datetime(OffsetDateTime ts) {
		return ZonedDateTime.parse(functions.pg_timestamptz_out(ts)).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
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

	public static String interval_to_timedelta(Pointer p){
		//TODO: parse the string and transform it in duration
		return functions.pg_interval_out(p);
	}

	public static Pointer intrange_to_intspan(Range<Integer> intrange){
		boolean lower_inc = intrange.lowerBoundType() == BoundType.CLOSED ? true : false;
		boolean upper_inc = intrange.upperBoundType() == BoundType.CLOSED ? true : false;
		return functions.intspan_make(intrange.lowerEndpoint(),intrange.upperEndpoint(),lower_inc, upper_inc);
	}

	public static Range intspan_to_intrange(Period span){
		BoundType lower_inc = span.isLowerInclusive() ? BoundType.CLOSED : BoundType.OPEN;
		BoundType upper_inc = span.isUpperInclusive() ? BoundType.CLOSED : BoundType.OPEN;
		return Range.range(functions.intspan_lower(span.get_inner()), lower_inc, functions.intspan_upper(span.get_inner()), upper_inc);
	}

	public static Pointer floatrange_to_floatspan(Range<Float> floatrange){
		boolean lower_inc = floatrange.lowerBoundType() == BoundType.CLOSED ? true : false;
		boolean upper_inc = floatrange.upperBoundType() == BoundType.CLOSED ? true : false;
		return functions.floatspan_make(floatrange.lowerEndpoint(),floatrange.upperEndpoint(),lower_inc, upper_inc);
	}

	public static Range floatspan_to_floatrange(Period span){
		BoundType lower_inc = span.isLowerInclusive() ? BoundType.CLOSED : BoundType.OPEN;
		BoundType upper_inc = span.isUpperInclusive() ? BoundType.CLOSED : BoundType.OPEN;
		return Range.range(functions.floatspan_lower(span.get_inner()), lower_inc, functions.floatspan_upper(span.get_inner()), upper_inc);
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



	
	//	public static Pointer geometryToGserialized(Geometry geom, Boolean geodetic) {
	//		String text;
	//		if (geom instanceof PGgeometry) {
	//			text = ((PGgeometry) geom).getValue();
	//			if (geom.getSRID() > 0) {
	//				text = "SRID=" + geom.getSRID() + ";" + text;
	//			}
	//		} else if (geom instanceof Geometry) {
	//			text = geom.toText();
	//			if (geom.getSRID() > 0) {
	//				text = "SRID=" + geom.getSRID() + ";" + text;
	//			}
	//		} else {
	//			throw new IllegalArgumentException("Parameter geom must be either a PostGIS Geometry or a JTS Geometry");
	//		}
	//		GSERIALIZED gs = gserialized_in(text.getBytes(), -1);
	//		if (geodetic != null) {
	//			int geodeticFlag = geodetic ? 0x08 : 0x00;
	//			gs.gflags(gs.gflags() | geodeticFlag);
	//		}
	//		return gs;
	//	}
	
	//	public static Point gserializedToShapelyPoint(Pointer geom, int precision) throws ParseException {
	//		String wktText = gserialized_as_text(geom, precision);
	//		WKTReader wktReader = new WKTReader();
	//		return (Point) wktReader.read(wktText);
	//	}
	
	//	public static Geometry gserializedToShapelyGeometry(Pointer geom, int precision) throws ParseException {
	//		String wktText = gserialized_as_text(geom, precision);
	//		WKTReader wktReader = new WKTReader();
	//		return wktReader.read(wktText);
	//	}

}
