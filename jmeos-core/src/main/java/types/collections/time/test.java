package types.collections.time;

import jnr.ffi.Pointer;
import org.locationtech.jts.io.ParseException;
import types.TemporalObject;
import types.boxes.Box;
import types.collections.base.Base;
import types.collections.base.Set;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import functions.functions;
import java.util.List;
import java.util.Objects;

//import types.boxes.*;

//import types.temporal.Temporal;
import types.collections.base.Span;
import types.collections.base.SpanSet;
import types.temporal.Temporal;
import utils.ConversionUtils;


/**
 * Class for representing lists of distinct timestamp values.
 *	<pre>
 *     ``tstzset`` objects can be created with a single argument of type string
 *     as in MobilityDB.
 *
 *         >>> tstzset(string='{2019-09-08 00:00:00+01, 2019-09-10 00:00:00+01, 2019-09-11 00:00:00+01}')
 *
 *     Another possibility is to give a tuple or list of composing timestamps,
 *     which can be instances of ``str`` or ``datetime``. The composing timestamps
 *     must be given in increasing order.
 *
 *         >>> tstzset(timestamp_list=['2019-09-08 00:00:00+01', '2019-09-10 00:00:00+01', '2019-09-11 00:00:00+01'])
 *         >>> tstzset(timestamp_list=[parse('2019-09-08 00:00:00+01'), parse('2019-09-10 00:00:00+01'), parse('2019-09-11 00:00:00+01')])
 * </pre>
 * @author Arijit Samal
 */
public class test extends Set<LocalDate> implements Time, TimeCollection {
    private Pointer _inner;


    /* ------------------------- Constructors ---------------------------------- */


    /**
     * The default constructor
     */
    public test() {
    }

    /**
     * Pointer constructor
     * @param _inner Pointer
     */
    public test(Pointer _inner)  {
        super(_inner);
        this._inner = _inner;
        //String str = functions.timestampset_out(this._inner);
    }


    /**
     * The string constructor
     *
     * @param value - a string with a tstzset value
     */
    public test(String value) {
        super(value);
        this._inner = functions.dateset_in(value);
    }


    @Override
    public Pointer createStringInner(String str){
        return functions.dateset_in(str);
    }

    @Override
    public Pointer createInner(Pointer inner){
        return inner;
    }


    /**
     * Return a copy of "this".
     *<p>
     *         MEOS Functions:
     *             <li>set_copy</li>
     * @return a new tstzset instance
     */
//	public tstzset copy() {
//		return new tstzset(functions.tstz(this._inner));
//	}


    /**
     * Returns a "tstzset" from its WKB representation in hex-encoded ASCII.
     *  <p>
     *         MEOS Functions:
     *             <li>set_from_hexwkb</li>
     * @param hexwkb WKB representation in hex-encoded ASCII
     * @return a new tstzset instance
     */
//	public static tstzset from_hexwkb(String hexwkb) {
//		Pointer result = functions.tstzset_(hexwkb);
//		return new tstzset(result);
//	}

    public static test from_hexwkb(String hexwkb) {
        Pointer result = functions.set_from_hexwkb(hexwkb);
        return new test(result);
    }

    /* ------------------------- Output ---------------------------------------- */


    /**
     * Return the string representation of the content of "this".
     *<p>
     *         MEOS Functions:
     *             <li>set_out</li>
     * @return a new String instance
     */
    public String toString(){
        return functions.dateset_out(this._inner);
    }


    /* ------------------------- Conversions ----------------------------------- */


    /**
     * Returns a tstzspanset that contains a tstzspan for each Timestamp in "this".
     *<p>
     *         MEOS Functions:
     *             <li>set_to_spanset</li>
     * @return a new tstzspanset instance
     */
//    public tstzspanset to_periodset() {
//        return new tstzspanset(functions.set_to_spanset(this.get_inner()));
//    }
//
//    /**
//     * Returns a period that encompasses "this".
//     *<p>
//     *         MEOS Functions:
//     *             <li>set_span</li>
//     * @return a new tstzspan instance
//     */
//    public tstzspan to_span() {
//        return new tstzspan(functions.set_to_span(this._inner));
//    }
//
//    public tstzspanset to_spanset() {
//        return new tstzspanset(functions.set_to_spanset(this._inner));
//    }

    /**
     * Returns a period that encompasses "this".
     *<p>
     *         MEOS Functions:
     *             <li>set_span</li>
     * @return a new tstzspan instance
     */
//    public tstzspan to_period() {
//        return this.to_span();
//    }
//
//    public Duration duration(){
//        return ConversionUtils.interval_to_timedelta(functions.tstzspan_duration(functions.set_to_span(this._inner)));
//    }





    /** ------------------------- Accessors ------------------------------------- */


    public Pointer get_inner(){
        return this._inner;
    }


    /**
     * Returns the number of timestamps in "this".
     * <p>
     *         MEOS Functions:
     *             <li>set_num_values</li>
     * @return a new Integer instance
     */
    public int num_timestamps(){
        return functions.set_num_values(this._inner);
    }

    /**
     * Returns the first timestamp in "this".
     *  <p>
     *
     *         MEOS Functions:
     *             <li>timestampset_start_timestamp</li>
     *
     * @return a {@link LocalDateTime instance}
     */

    public LocalDate date_adt_to_date(int ts){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = functions.pg_date_out(ts);
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public LocalDate start_element() throws ParseException {
        return date_adt_to_date(functions.dateset_start_value(this._inner));
    }

    @Override
    public LocalDate end_element() throws ParseException {
        return date_adt_to_date(functions.dateset_end_value(this._inner));
    }

    /**
     * Returns the last timestamp in "this".
     *  <p>
     *
     *         MEOS Functions:
     *             <li>timestampset_end_timestamp</li>
     *
     * @return a {@link LocalDateTime instance}
     */
}

