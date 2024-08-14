package types.collections.time;

import jnr.ffi.Pointer;
import org.locationtech.jts.io.ParseException;
import types.collections.base.*;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import functions.functions;
import utils.ConversionUtils;

/*
    Class for representing sets of contiguous dates between a lower and
    an upper bound. The bounds may be inclusive or not.

    ``DateSpan`` objects can be created with a single argument of type string
    as in MobilityDB.

        >>> DateSpan('(2019-09-08, 2019-09-10)')

    Another possibility is to provide the ``lower`` and ``upper`` named
    parameters (of type str or date), and optionally indicate whether the
    bounds are inclusive or exclusive (by default, the lower bound is inclusive
    and the upper is exclusive):

        >>> DateSpan(lower='2019-09-08', upper='2019-09-10')
        >>> DateSpan(lower='2019-09-08', upper='2019-09-10', lower_inc=False, upper_inc=True)
        >>> DateSpan(lower=parse('2019-09-08'), upper=parse('2019-09-10'), upper_inc=True)
*/

public class datespan extends Span<LocalDate> implements Time, TimeCollection{

    private final Pointer _inner;

    public datespan(Pointer inner){
        super(inner);
        _inner = inner;
    }

    public datespan(String str) {
        super(str);
        _inner = functions.datespan_in(str);
    }

//    // Formatter for parsing date strings
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
//
//    // Constructor accepting String bounds with default inclusivity
//    public datespan(String lower, String upper) {
//        this(lower, upper, true, false);
//    }
//
//    // Constructor accepting String bounds with specified inclusivity
//    public datespan(String lower, String upper, boolean lowerInc, boolean upperInc) {
//        try {
//            this.lower = LocalDate.parse(lower, formatter);
//            this.upper = LocalDate.parse(upper, formatter);
//        } catch (DateTimeParseException e) {
//            throw new IllegalArgumentException("Invalid date format. Use 'YYYY-MM-DD'.", e);
//        }
//        this.lowerInc = lowerInc;
//        this.upperInc = upperInc;
//        _inner= functions.datespan_make(time)
//    }
//
//    // Constructor accepting LocalDate bounds with specified inclusivity
//    public datespan(LocalDate lower, LocalDate upper, boolean lowerInc, boolean upperInc) {
//        this.lower = lower;
//        this.upper = upper;
//        this.lowerInc = lowerInc;
//        this.upperInc = upperInc;
//    }


/*
        Returns a :class:`DateSpanSet` set containing ``self``.

        Returns:
            A new :class:`DateSpanSet` instance

        MEOS Functions:
            span_to_spanset
*/

    public datespanset to_spanset() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new datespanset(String.valueOf(super.to_spanset(datespan.class)));
    }

/*
        Returns a :class:`TsTzSpan equivalent to ``self``.

        Returns:
            A new :class:`TsTzSpan` instance

        MEOS Functions:
            datespan_to_tstzspan
*/

    public tstzspan to_tstzspan() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new tstzspan(functions.datespan_to_tstzspan(this._inner));
    }

/*
        Returns the duration of ``self``.

        Returns:
            A :class:`datetime.timedelta` instance representing the duration of
            the :class:`DateSpan`

        MEOS Functions:
            datespan_duration
*/
    public Duration duration() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return ConversionUtils.interval_to_timedelta(functions.datespan_duration(this._inner));
    }

/*
        Returns the duration of ``self``.

        Returns:
            Returns a `float` representing the duration of the :class:`DateSpan` in
            days

        MEOS Functions:
            span_width
*/
    public float duration_in_days(){
        return this.width();
    }

    @Override
    public Pointer createInner(Pointer inner) {
        return inner;
    }

    @Override
    public Pointer createStringInner(String str) {
        return functions.datespan_in(str);
    }

    @Override
    public Pointer createIntInt(Number lower, Number upper, boolean lower_inc, boolean upper_inc) {
        return null;
    }

    @Override
    public Pointer createIntStr(Number lower, String upper, boolean lower_inc, boolean upper_inc) {
        return null;
    }

    @Override
    public Pointer createStrStr(String lower, String upper, boolean lower_inc, boolean upper_inc) {
        return null;
    }

    @Override
    public Pointer createStrInt(String lower, Number upper, boolean lower_inc, boolean upper_inc) {
        return null;
    }

    @Override
    public Pointer createIntIntNb(Number lower, Number upper) {
        return null;
    }

    @Override
    public LocalDate lower() {
        return date_adt_to_date(functions.datespan_lower(this._inner));
    }

    @Override
    public LocalDate upper() {
        return date_adt_to_date(functions.datespan_lower(this._inner));
    }

    @Override
    public Pointer get_inner() {
        return this._inner;
    }


    public String toString(){
        return functions.datespan_out(this.get_inner());
    }

/*
Function to convert the integer timestamp to LocalDate format so that it can be used by other libraries
*/

    public LocalDate date_adt_to_date(int ts){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr= functions.pg_date_out(ts);
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /*
            Returns the first date in ``self``.
            Returns:
                A :class:`date` instance

            MEOS Functions:
                dateset_start_value
    */

    public LocalDate start_element() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespanset dss = this.to_spanset(datespanset.class);
        return date_adt_to_date(functions.datespanset_start_date(dss.get_inner()));
    }

/*
        Returns the last date in ``self``.
        Returns:
            A :class:`date` instance

        MEOS Functions:
            dateset_end_value
*/
    
    public LocalDate end_element() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        datespanset dss = this.to_spanset(datespanset.class);
        return date_adt_to_date(functions.datespanset_end_date(dss.get_inner()));
    }

/*
        Returns a new :class:`DateSpan` that starts as ``self`` but has
        duration ``duration``.

        Examples:
            >>> DateSpan('[2000-01-01, 2000-01-10]').scale(timedelta(days=2))
            >>> 'DateSpan([2000-01-01, 2000-01-03])'

        Args:
            duration: :class:`datetime.timedelta` instance representing the
            duration of the new dateSpan

        Returns:
            A new :class:`DateSpan` instance

        MEOS Functions:
            datespan_shift_scale
*/

    public datespan scale(Integer duration){
        return new datespan(this.shift_scale(0, duration)._inner);
    }

/*
        Returns a new :class:`DateSpan` that is the result of shifting ``self`` by
        ``delta``.

        Examples:
            >>> DateSpan('[2000-01-01, 2000-01-10]').shift(timedelta(days=2))
            >>> 'DateSpan([2000-01-03, 2000-01-12])'

        Args:
            delta: :class:`datetime.timedelta` instance to shift

        Returns:
            A new :class:`DateSpan` instance

        MEOS Functions:
            datespan_shift_scale
*/

    public datespan shift(Integer shift){
        return new datespan(this.shift_scale(shift, 0)._inner);
    }

/*
        Returns a new :class:`DateSpan` that starts at ``self`` shifted by ``shift`` and
        has duration ``duration``

        Examples:
            >>> DateSpan('[2000-01-01, 2000-01-10]').shift_scale(shift=timedelta(days=2), duration=timedelta(days=4))
            >>> 'DateSpan([2000-01-03, 2000-01-07])'

        Args:
            shift: :class:`datetime.timedelta` instance to shift
            duration: :class:`datetime.timedelta` instance representing the
            duration of the new dateSpan

        Returns:
            A new :class:`DateSpan` instance

        MEOS Functions:
            datespan_shift_scale
*/

    public datespan shift_scale(Integer shift, Integer duration){
        return new datespan(functions.datespan_shift_scale(this._inner, shift, duration, shift!=0, duration!=0));
    }

/*
        Returns whether ``self`` temporally contains ``content``.

        Examples:
            >>> DateSpan('[2012-01-01, 2012-01-04]').contains(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSpan('[2012-01-01, 2012-01-02]').contains(DateSpan('(2012-01-01, 2012-01-02)'))
            >>> True
            >>> DateSpan('(2012-01-01, 2012-01-02)').contains(DateSpan('[2012-01-01, 2012-01-02]'))
            >>> False

        Args:
            content: temporal object to compare with

        Returns:
            True if contains, False otherwise

        MEOS Functions:
            contains_span_span, contains_span_spanset, contains_span_date
*/
    public int dateToTimestamp(LocalDate date){
        return functions.pg_date_in(date.toString());
    }

    public boolean contains(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.contains_span_date(this._inner, dateToTimestamp((LocalDate) other));
        }
        else if (other instanceof LocalDateTime){
            return functions.contains_span_date(this._inner, dateToTimestamp(((LocalDateTime) other).toLocalDate()));
        }
        else {
            return super.contains((Base) other);
        }
    }

/*
        Returns whether ``self`` is adjacent to ``other``. That is, they share
        a bound but only one of them contains it.

        Args:
            other: object to compare with

        Returns:
            True if adjacent, False otherwise

        MEOS Functions:
            adjacent_span_span, adjacent_span_spanset, adjacent_span_date
*/
    
    public boolean is_adjacent(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.adjacent_span_date(this._inner, dateToTimestamp((LocalDate) other));
        }
        else{
            return super.is_adjacent((Base) other);
        }
    }
    

/*
        Returns whether ``self`` temporally overlaps ``other``. That is, both
        share at least an instant

        Examples:
            >>> DateSpan('[2012-01-01, 2012-01-02]').overlaps(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSpan('[2012-01-01, 2012-01-02)').overlaps(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> False
            >>> DateSpan('[2012-01-01, 2012-01-02)').overlaps(DateSpan('(2012-01-02, 2012-01-03]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if overlaps, False otherwise

        MEOS Functions:
            overlaps_span_span, overlaps_span_spanset
*/

    public boolean overlaps(Object other) throws Exception {
        if (other instanceof LocalDate){
            return this.contains(other);
        }
        else {
            return super.overlaps((Base) other);
        }
    }

/*
        Returns whether ``self`` is strictly before ``other``. That is,
        ``self`` ends before ``other`` starts.

        Examples:
            >>> DateSpan('[2012-01-01, 2012-01-02)').is_left(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSpan('[2012-01-01, 2012-01-02)').is_left(DateSpan('(2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSpan('[2012-01-01, 2012-01-02]').is_left(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if before, False otherwise

        MEOS Functions:
            left_span_span, left_span_spanset, before_span_date,
*/

    public boolean is_left(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.before_span_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        else {
            return super.is_left((Base) other);
        }
    }

/*
        Returns whether ``self`` is before ``other`` allowing overlap. That is,
        ``self`` ends before ``other`` ends (or at the same time).

        Examples:
            >>> DateSpan('[2012-01-01, 2012-01-02)').is_over_or_left(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSpan('[2012-01-01, 2012-01-02]').is_over_or_left(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSpan('[2012-01-03, 2012-01-05]').is_over_or_left(DateSpan('[2012-01-01, 2012-01-04]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if before, False otherwise

        MEOS Functions:
            overleft_span_span, overleft_span_spanset, overbefore_span_date,
*/

    public boolean is_over_or_left(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.overbefore_span_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        else {
            return super.is_over_or_left((Base) other);
        }
    }

/*
        Returns whether ``self`` is after ``other`` allowing overlap. That is,
        ``self`` starts after ``other`` starts (or at the same time).

        Examples:
            >>> DateSet('{2012-01-02, 2012-01-03}').is_over_or_right(DateSpan('[2012-01-01, 2012-01-02)'))
            >>> True
            >>> DateSet('{2012-01-02, 2012-01-03}').is_over_or_right(DateSpan('[2012-01-01, 2012-01-02]'))
            >>> True
            >>> DateSet('{2012-01-02, 2012-01-03}').is_over_or_right(DateSpan('[2012-01-01, 2012-01-03]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if overlapping or after, False otherwise

        MEOS Functions:
            overafter_set_date, overright_span_span, overright_span_spanset
*/

    public boolean is_over_or_right(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.overafter_span_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        else {
            return super.is_over_or_right((Base) other);
        }
    }


/*
        Returns whether ``self`` is strictly after ``other``. That is, ``self``
        starts after ``other`` ends.

        Examples:
            >>> DateSpan('[2012-01-02, 2012-01-03]').is_right(DateSpan('[2012-01-01, 2012-01-02)'))
            >>> True
            >>> DateSpan('(2012-01-02, 2012-01-03]').is_right(DateSpan('[2012-01-01, 2012-01-02)'))
            >>> True
            >>> DateSpan('[2012-01-02, 2012-01-03]').is_right(DateSpan('[2012-01-01, 2012-01-02]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if after, False otherwise

        MEOS Functions:
            right_span_span, right_span_spanset, after_span_date,
*/

    public boolean is_right(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.after_span_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        else {
            return super.is_right((Base) other);
        }
    }

    @Override
    public boolean is_before(Object other) throws Exception {
        return TimeCollection.super.is_before(other);
    }

    @Override
    public boolean is_over_or_before(Object other) throws Exception {
        return TimeCollection.super.is_over_or_before(other);
    }

    @Override
    public boolean is_over_or_after(Object other) throws Exception {
        return TimeCollection.super.is_over_or_after(other);
    }

    @Override
    public boolean is_after(Object other) throws Exception {
        return TimeCollection.super.is_after(other);
    }

    /*---------------Distance Operations-------------------*/


/*
        Returns the temporal distance between ``self`` and ``other``.

        Args:
            other: temporal object to compare with

        Returns:
            A :class:`datetime.timedelta` instance

        MEOS Functions:
            distance_set_date, distance_dateset_dateset,
            distance_datespanset_datespan, distance_datespanset_datespanset
*/

    public Duration distance(Object other) throws Exception {
        Duration answer = null;
        if (other instanceof LocalDate) {
            answer= Duration.ofSeconds(functions.distance_span_date(this._inner, dateToTimestamp((LocalDate) other)));
        } else if (other instanceof dateset) {
            datespan ds = ((dateset) other).to_span(datespan.class);
            answer= Duration.ofSeconds(functions.distance_datespan_datespan(this._inner, ds.get_inner()));
        } else if (other instanceof datespan) {
            answer= Duration.ofSeconds(functions.distance_datespan_datespan(this._inner, ((datespan)other)._inner));
        } else if (other instanceof datespanset) {
            answer= Duration.ofSeconds(functions.distance_datespanset_datespan(((datespanset) other).get_inner(), this._inner));
        } else {
            throw new Exception("Operation not supported with " + other + " type");
        }
        return answer;
    }


    /*---------------Set Operations-------------------*/

    // Convert timestamp (number of seconds since epoch) to LocalDateTime
    public static LocalDateTime timestampToLocalDateTime(int timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }

/*
        Returns the temporal intersection of ``self`` and ``other``.

        Args:
            other: temporal object to intersect with

        Returns:
            A :class:`TimeDate` instance. The actual class depends on ``other``.

        MEOS Functions:
            intersection_set_date, intersection_set_set, intersection_spanset_span,
            intersection_spanset_spanset
*/

    public LocalDateTime intersection(Object other) throws Exception {
        LocalDateTime result = null;
        if (other instanceof LocalDate){
            Pointer resultPointer= functions.intersection_span_date(this._inner, dateToTimestamp((LocalDate) other));
            int resultTimestamp= resultPointer.getInt(Integer.BYTES);
            result = timestampToLocalDateTime(resultTimestamp);
        }
        else if (other instanceof dateset){
            datespan ds = ((dateset) other).to_span(datespan.class);
            Pointer resultPointer= functions.intersection_span_span(this._inner, ds.get_inner());
            int resultTimestamp= resultPointer.getInt(Integer.BYTES);
            result = timestampToLocalDateTime(resultTimestamp);
        }
        else if (other instanceof datespan){
            Pointer resultPointer = functions.intersection_span_span(this._inner, ((datespan) other)._inner);
            int resultTimestamp= resultPointer.getInt(Integer.BYTES);
            result = timestampToLocalDateTime(resultTimestamp);
        }
        else if (other instanceof datespanset){
            Pointer resultPointer = functions.intersection_spanset_span(((datespanset) other).get_inner(), this._inner);
            int resultTimestamp= resultPointer.getInt(Integer.BYTES);
            result = timestampToLocalDateTime(resultTimestamp);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
        return result;
    }

/*
        Returns the temporal difference of ``self`` and ``other``.

        Args:
            other: temporal object to diff with

        Returns:
            A :class:`DateSpanSet` instance.

        MEOS Functions:
            minus_span_date, minus_span_spanset, minus_span_span
*/

    public datespanset minus(Object other) throws Exception{
        datespanset result = null;
        if (other instanceof LocalDate){
            Pointer resultPointer= functions.minus_span_date(this._inner, dateToTimestamp((LocalDate) other));
            result = new datespanset(resultPointer);
        }
        else if (other instanceof dateset){
            datespan ds = ((dateset) other).to_span(datespan.class);
            Pointer resultPointer= functions.minus_span_span(this._inner, (ds).get_inner());
            result = new datespanset(resultPointer);
        }
        else if (other instanceof datespan){
            Pointer resultPointer= functions.minus_span_span(this._inner, ((datespan) other)._inner);
            result = new datespanset(resultPointer);
        }
        else if (other instanceof datespanset){
            Pointer resultPointer= functions.minus_span_spanset(this._inner, ((datespanset) other).get_inner());
            result = new datespanset(resultPointer);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
        return result;
    }

    // Convert timestamp (number of seconds since epoch) to LocalDate
    public static LocalDate timestampToLocalDate(int timestamp) {
        return LocalDate.ofEpochDay(timestamp / 86400); // Convert seconds back to days
    }

    public LocalDate subtract_from(Object other) throws Exception {
        int ts= dateToTimestamp((LocalDate) other);
        Pointer resultPointer= functions.minus_date_set(ts, this._inner);
        int resultTimestamp= resultPointer.getInt(0);
        return timestampToLocalDate(resultTimestamp);
    }

/*
        Returns the temporal union of ``self`` and ``other``.

        Args:
            other: temporal object to merge with

        Returns:
            A :class:`DateSpanSet` instance.

        MEOS Functions:
            union_span_date, union_spanset_span, union_span_span
*/

    public datespanset union(Object other) throws Exception{
        datespanset result = null;
        if (other instanceof LocalDate){
            Pointer resultPointer= functions.union_span_date(this._inner, dateToTimestamp((LocalDate) other));
            result = new datespanset(resultPointer);
        }
        else if (other instanceof dateset){
            datespan ds = ((dateset) other).to_span(datespan.class);
            Pointer resultPointer= functions.union_span_span(this._inner, (ds).get_inner());
            result = new datespanset(resultPointer);
        }
        else if (other instanceof datespan){
            Pointer resultPointer= functions.union_span_span(this._inner, ((datespan) other).get_inner());
            result = new datespanset(resultPointer);
        }
        else if (other instanceof datespanset){
            Pointer resultPointer= functions.union_spanset_span(((datespanset) other).get_inner(), this._inner);
            result = new datespanset(resultPointer);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
        return result;
    }
}
