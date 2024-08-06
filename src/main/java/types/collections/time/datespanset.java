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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import functions.functions;
import utils.ConversionUtils;

/*
    Class for representing lists of disjoint tstzspans.

    :class:``DateSpanSet`` objects can be created with a single argument of type string
    as in MobilityDB.

        >>> DateSpanSet(string='{[2019-09-08, 2019-09-10], [2019-09-11, 2019-09-12]}')

    Another possibility is to give a list specifying the composing
    tstzspans, which can be instances  of :class:``str`` or :class:``DateSpan``.
    The composing datespans must be given in increasing order.

        >>> DateSpanSet(span_list=['[2019-09-08, 2019-09-10]', '[2019-09-11, 2019-09-12'])
        >>> DateSpanSet(span_list=[TsTzSpan('[2019-09-08, 2019-09-10]'), TsTzSpan('[2019-09-11, 2019-09-12]')])
    @author ARIJIT SAMAL
*/

public class datespanset extends SpanSet<LocalDate> implements Time, TimeCollection{

    private final Pointer _inner;

    public datespanset(Pointer inner){
        super(inner);
        _inner = inner;
    }

    public datespanset(String str) {
        super(str);
        _inner = functions.dateset_in(str);
    }

/*
        Returns a :class:`DateSpanSet` set containing ``self``.

        Returns:
            A new :class:`DateSpanSet` instance

        MEOS Functions:
            span_to_spanset
*/

    public datespanset to_span() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new datespanset(String.valueOf(super.to_span(datespanset.class)));
    }

/*
        Returns a :class:`TsTzSpan equivalent to ``self``.

        Returns:
            A new :class:`TsTzSpan` instance

        MEOS Functions:
            datespan_to_tstzspan
*/

    public tstzspanset to_tstzspanset() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new tstzspanset(functions.datespanset_to_tstzspanset(this._inner));
    }

    /*
            Returns the duration of ``self``.

            Returns:
                A :class:`datetime.timedelta` instance representing the duration of
                the :class:`DateSpan`

            MEOS Functions:
                datespan_duration
    */
    public Duration duration(boolean ignore_gaps) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return ConversionUtils.interval_to_timedelta(functions.datespanset_duration(this._inner, ignore_gaps));
    }

    public int num_dates(){
        return functions.datespanset_num_dates(this._inner);
    }

    @Override
    public Pointer createInner(Pointer inner) {
        return inner;
    }

    @Override
    public Pointer createStringInner(String str) {
        return functions.datespanset_in(str);
    }

    @Override
    public Pointer get_inner() {
        return this._inner;
    }

    public LocalDate start_date(){
        return date_adt_to_date(functions.datespanset_start_date(this._inner));
    }

    public LocalDate end_date(){
        return date_adt_to_date(functions.datespanset_end_date(this._inner));
    }

/*
        Returns the n-th date in ``self``.
        Returns:
            A :class:`date` instance

        MEOS Functions:
*/

    public LocalDate date_n(int n) throws Exception {
        if(n<0 || n>=this.num_dates()){
            throw new Exception("Index out of bounds");
        }
        else{
            Pointer resultPointer= functions.datespanset_date_n(this._inner, n+1);
            assert resultPointer != null;
            int ts = resultPointer.getInt(0);
            return date_adt_to_date(ts);
        }
    }

    public String toString(){
        return functions.datespanset_out(this.get_inner());
    }

/*
Function to convert the integer timestamp to LocalDate format so that it can be used by other libraries
*/

    public LocalDate date_adt_to_date(int ts){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.ofEpochDay(ts);
    }

    /*
            Returns the first date in ``self``.
            Returns:
                A :class:`date` instance

            MEOS Functions:
                dateset_start_value
    */

    public datespan start_span() throws ParseException {
        return new datespan(functions.spanset_start_span(this._inner));
    }

/*
        Returns the last date in ``self``.
        Returns:
            A :class:`date` instance

        MEOS Functions:
            dateset_end_value
*/

    public datespan end_element() throws ParseException {
        return new datespan(functions.spanset_end_span(this._inner));
    }

    public datespan span_n(int n) throws ParseException {
        return new datespan(functions.spanset_span_n(this._inner, n));
    }

    public List<datespan> elements() throws Exception {
        return super.spans(datespan.class);
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
        return new datespan(this.shift_scale(0, duration).get_inner());
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

    public datespanset shift(Integer shift, Integer duration){
        return new datespanset(this.shift_scale(shift, 0).get_inner());
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

    public datespanset shift_scale(Integer shift, Integer duration){
        return new datespanset(functions.datespanset_shift_scale(this._inner, shift, duration, shift!=0, duration!=0));
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
        return (int)date.toEpochDay() * (86400); // convert days t secoonds
    }

    public boolean contains(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.contains_spanset_date(this._inner, dateToTimestamp((LocalDate) other));
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
            return functions.adjacent_spanset_date(this._inner, dateToTimestamp((LocalDate) other));
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
            return functions.before_spanset_date(this._inner, dateToTimestamp(((LocalDate) other)));
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
            return functions.overbefore_spanset_date(this._inner, dateToTimestamp(((LocalDate) other)));
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
            return functions.overafter_spanset_date(this._inner, dateToTimestamp(((LocalDate) other)));
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
            return functions.after_spanset_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        else {
            return super.is_right((Base) other);
        }
    }

//    @Override
//    public boolean is_before(Object other) throws Exception {
//        return TimeCollection.super.is_before(other);
//    }

//    @Override
//    public boolean is_over_or_before(Object other) throws Exception {
//        return TimeCollection.super.is_over_or_before(other);
//    }
//
//    @Override
//    public boolean is_over_or_after(Object other) throws Exception {
//        return TimeCollection.super.is_over_or_after(other);
//    }
//
//    @Override
//    public boolean is_after(Object other) throws Exception {
//        return TimeCollection.super.is_after(other);
//    }

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
            answer= Duration.ofSeconds(functions.distance_spanset_date(this._inner, dateToTimestamp((LocalDate) other)));
        } else if (other instanceof dateset) {
            this.distance(((dateset) other).to_spanset(dateset.class));
        } else if (other instanceof datespan) {
            answer= Duration.ofSeconds(functions.distance_datespanset_datespan(this._inner, ((datespan) other).get_inner()));
        } else if (other instanceof datespanset) {
            answer= Duration.ofSeconds(functions.distance_datespanset_datespanset(this._inner, ((datespanset) other).get_inner()));
        } else {
            super.distance((Base) other);
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

    public datespanset intersection(Object other) throws Exception {
        datespanset result = null;
        if (other instanceof LocalDate){
            Pointer resultPointer= functions.intersection_spanset_date(this._inner, dateToTimestamp((LocalDate) other));
            result = new datespanset(resultPointer);
        }
        else{
            super.intersection((Base) other);
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
            Pointer resultPointer= functions.minus_spanset_date(this._inner, dateToTimestamp((LocalDate) other));
            result= new datespanset(resultPointer);
        }
        else{
            super.minus((Base) other);
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
            Pointer resultPointer= functions.union_spanset_date(this._inner, dateToTimestamp((LocalDate) other));
            result = new datespanset(resultPointer);
        }
        else{
            super.union((Base) other);
        }
        return result;
    }
}