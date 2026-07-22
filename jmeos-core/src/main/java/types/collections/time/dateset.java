package types.collections.time;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Memory;
import functions.GeneratedFunctions;
import jnr.ffi.annotations.In;
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

/**
 *  Class for representing lists of distinct dates.
 * <p>
 *     ``DateSet`` objects can be created with a single argument of type
 *     string as in MobilityDB.
 * <p>
 *         >>> DateSet(string='{2019-09-08, 2019-09-10, 2019-09-11}')
 * <p>
 *     Another possibility is to give a tuple or list of composing dates,
 *     which can be instances of ``str`` or ``date``. The composing dates
 *     must be given in increasing order.
 * <p>
 *         >>> DateSet(elements=['2019-09-08', '2019-09-10', '2019-09-11'])
 *         <br>
 *         >>> DateSet(elements=[parse('2019-09-08'), parse('2019-09-10'), parse('2019-09-11')])
 *
 *     @author ARIJIT SAMAL
 */

public class dateset extends Set<LocalDate> implements Time, TimeCollection{

    private final Pointer _inner;

    public dateset(Pointer inner){
        super(inner);
        _inner = inner;
    }

    public dateset(String value){
        super(value);
        _inner = functions.dateset_in(value);
    }

    public dateset(List<LocalDate> dates)  {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            System.out.println(date.toString());
            sb.append(date.toString());
            if (i < dates.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
//        System.out.println(sb);
        _inner = functions.dateset_in(sb.toString());
    }

    /**
            Returns the duration of the time ignoring gaps, i.e. the duration from
            the first timestamp to the last one.

            Returns:
                A :class:`datetime.timedelta` instance representing the duration of ``self``

            MEOS Functions:
                tstzspan_duration
    */
    public Duration duration() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.to_spanset(datespanset.class).duration(false);
    }

    @Override
    public Pointer createInner(Pointer inner) {
        return inner;
    }

    @Override
    public Pointer createStringInner(String str) {
        return functions.dateset_in(str);
    }

    @Override
    public Pointer get_inner() {
        return this._inner;
    }


    public String toString(){
        return functions.dateset_out(this.get_inner());
    }

/**
Function to convert the integer timestamp to LocalDate format so that it can be used by other libraries
*/

    public LocalDate date_adt_to_date(int ts){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr= functions.date_out(ts);
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
            Returns the first date in ``self``.
            Returns:
                A :class:`date` instance

            MEOS Functions:
                dateset_start_value
    */

    @Override
    public LocalDate start_element() throws ParseException {
        return date_adt_to_date(functions.dateset_start_value(this._inner));
    }

/**
        Returns the last date in ``self``.
        Returns:
            A :class:`date` instance

        MEOS Functions:
            dateset_end_value
*/

    @Override
    public LocalDate end_element() throws ParseException {
        return date_adt_to_date(functions.dateset_end_value(this._inner));
    }

    /**
            Returns the n-th date in ``self``.
            Returns:
                A :class:`date` instance

            MEOS Functions:
                dateset_value_n
    */
    public LocalDate element_n(int n) throws Exception {
        return this.elements().get(n);
    }

/**
        Returns the list of distinct dates in ``self``.
        Returns:
            A :class:`list[date]` instance

        MEOS Functions:
            dateset_values
*/

    public List<LocalDate> elements() throws Exception {
        Runtime runtime = Runtime.getSystemRuntime();
        Pointer count = Memory.allocate(runtime, Integer.BYTES);
        Pointer dp= GeneratedFunctions.dateset_values(this._inner, count);
        long size= count.getInt(0);
        List<LocalDate> datelist= new ArrayList<LocalDate>();
        for(int i=0; i<size; i++) {
            int res= dp.getInt((long) i *Integer.BYTES);
            datelist.add(date_adt_to_date(res));
        }
        return datelist;
    }

/**
        Returns a new :class:`DateSet` that with the scaled so that the span of
        ``self`` is ``duration``.

        Examples:
            >>> DateSet('{2000-01-01, 2000-01-10}').scale(timedelta(days=2))
            >>> 'DateSet({2000-01-01, 2000-01-03})'

        Args:
            duration: :class:`datetime.timedelta` instance representing the
            span of the new set

        Returns:
            A new :class:`DateSet` instance

        MEOS Functions:
            dateset_shift_scale
*/

    public dateset scale(Integer duration){
        return new dateset(this.shift_scale(0, duration)._inner);
    }

/**
        Returns a new :class:`DateSpanSet` that is the result of shifting ``self`` by
        ``delta``

        Examples:
            >>> DateSet('{2000-01-01, 2000-01-10}').shift(timedelta(days=2))
            >>> 'DateSet({2000-01-03, 2000-01-12})'

        Args:
            delta: :class:`datetime.timedelta` instance to shift

        Returns:
            A new :class:`DateSpanSet` instance

        MEOS Functions:
            dateset_shift_scale
*/

    public dateset shift(Integer shift){
        return new dateset(this.shift_scale(shift, 0)._inner);
    }

    /**
            Returns a new :class:`DateSet` that is the result of shifting and scaling
            ``self``.

            Examples:
                >>> DateSet('{2000-01-01, 2000-01-10}').shift_scale(shift=timedelta(days=2), duration=timedelta(days=4))
                >>> 'DateSet({2000-01-03, 2000-01-07})'

            Args:
                shift: :class:`datetime.timedelta` instance to shift
                duration: :class:`datetime.timedelta` instance representing the
                span of the new set

            Returns:
                A new :class:`DateSet` instance

            MEOS Functions:
                dateset_shift_scale
    */

    public dateset shift_scale(Integer shift, Integer duration){
        return new dateset(functions.dateset_shift_scale(this._inner, shift, duration, shift!=0, duration!=0));
    }

    /**
            Returns whether ``self`` temporally contains ``content``.

            Examples:
                >>> DateSet('{2012-01-01, 2012-01-04}').contains(parse('2012-01-01').date())
                >>> True
                >>> DateSet('{2012-01-01, 2012-01-02}').contains(DateSet('{2012-01-01}'))
                >>> True
                >>> DateSet('{2012-01-01, 2012-01-02}').contains(DateSet('{2012-01-01, 2012-01-03}'))
                >>> False

            Args:
                content: temporal object to compare with

            Returns:
                True if contains, False otherwise

            MEOS Functions:
                contains_set_date, contains_set_set, contains_spanset_spanset
    */
    public int dateToTimestamp(LocalDate date){
       return functions.date_in(date.toString());
    }

    public boolean contains(Object other) throws Exception {
        if (other instanceof LocalDateTime){
            return functions.contains_set_date(this._inner, dateToTimestamp(((LocalDateTime) other).toLocalDate()));
        }
        else if (other instanceof LocalDate){
            return functions.contains_set_date(this._inner, dateToTimestamp((LocalDate) other));
        }
        else {
            return super.contains((Base) other);
        }
    }

/**
        Returns whether ``self`` temporally overlaps ``other``. That is, both
        share at least an instant

        Examples:
            >>> DateSet('{2012-01-01, 2012-01-02}').overlaps(DateSet('{2012-01-02, 2012-01-03}'))
            >>> True
            >>> DateSet('{2012-01-01, 2012-01-02}').overlaps(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSet('{2012-01-01, 2012-01-02}').overlaps(DateSpan('(2012-01-02, 2012-01-03]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if overlaps, False otherwise

        MEOS Functions:
            overlaps_set_set, overlaps_span_span, overlaps_spanset_spanset
*/

    public boolean overlaps(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.contains_set_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        if (other instanceof datespan){
            return this.to_span(datespan.class).is_adjacent((Base) other);
        }
        if (other instanceof datespanset){
            return this.to_spanset(datespanset.class).is_adjacent((Base) other);
        }
        else {
            return super.overlaps((Base) other);
        }
    }

/**
        Returns whether ``self`` is strictly before ``other``. That is,
        ``self`` ends before ``other`` starts.

        Examples:
            >>> DateSet('{2012-01-01, 2012-01-02}').is_left(DateSet('{2012-01-03}'))
            >>> True
            >>> DateSet('{2012-01-01, 2012-01-02}').is_left(DateSpan('(2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSet('{2012-01-01, 2012-01-02}').is_left(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if before, False otherwise

        MEOS Functions:
            before_set_date, left_span_span
*/

    public boolean is_left(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.before_set_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        if (other instanceof datespan){
            return this.to_span(datespan.class).is_left(other);
        }
        if (other instanceof datespanset){
            return this.to_spanset(datespanset.class).is_left(other);
        }
        else {
            return super.is_left((Base) other);
        }
    }

/**
        Returns whether ``self`` is before ``other`` allowing overlap. That is,
        ``self`` ends before ``other`` ends (or at the same time).

        Examples:
            >>> DateSet('{2012-01-01, 2012-01-02}').is_over_or_left(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSet('{2012-01-01, 2012-01-02}').is_over_or_left(DateSpan('[2012-01-02, 2012-01-03]'))
            >>> True
            >>> DateSet('{2012-01-03, 2012-01-05}').is_over_or_left(DateSpan('[2012-01-01, 2012-01-04]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if before, False otherwise

        MEOS Functions:
            overbefore_set_date, overleft_span_span, overleft_span_spanset
*/

    public boolean is_over_or_left(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.overbefore_set_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        if (other instanceof datespan){
            return this.to_span(datespan.class).is_over_or_left(other);
        }
        if (other instanceof datespanset){
            return this.to_spanset(datespanset.class).is_over_or_left(other);
        }
        else {
            return super.is_over_or_left((Base) other);
        }
    }

/**
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
            return functions.overafter_set_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        if (other instanceof datespan){
            return this.to_span(datespan.class).is_over_or_right(other);
        }
        if (other instanceof datespanset){
            return this.to_spanset(datespanset.class).is_over_or_right(other);
        }
        else {
            return super.is_over_or_right((Base) other);
        }
    }


/**
        Returns whether ``self`` is strictly after ``other``. That is, the
        first timestamp in ``self`` is after ``other``.

        Examples:
            >>> DateSet('{2012-01-02, 2012-01-03}').is_right(DateSpan('[2012-01-01, 2012-01-02)'))
            >>> True
            >>> DateSet('{2012-01-02, 2012-01-03}').is_right(DateSet('{2012-01-01}'))
            >>> True
            >>> DateSet('{2012-01-02, 2012-01-03}').is_right(DateSpan('[2012-01-01, 2012-01-02]'))
            >>> False

        Args:
            other: temporal object to compare with

        Returns:
            True if after, False otherwise

        MEOS Functions:
            after_set_date, right_span_span, right_span_spanset
*/

    public boolean is_right(Object other) throws Exception {
        if (other instanceof LocalDate){
            return functions.after_set_date(this._inner, dateToTimestamp(((LocalDate) other)));
        }
        if (other instanceof datespan){
            return this.to_span(datespan.class).is_over_or_left(other);
        }
        if (other instanceof datespanset){
            return this.to_spanset(datespanset.class).is_over_or_left(other);
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



/**
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
            answer= Duration.ofSeconds(functions.distance_set_date(this._inner, dateToTimestamp((LocalDate) other)));
        } else if (other instanceof dateset) {
            answer= Duration.ofSeconds(functions.distance_dateset_dateset(this._inner, ((dateset) other)._inner));
        } else if (other instanceof datespan) {
            answer= this.to_spanset(datespan.class).distance(other);
        } else if (other instanceof datespanset) {
            answer= this.to_spanset(datespanset.class).distance(other);
        } else {
            throw new Exception("Operation not supported with "+ other + " type");
        }
        return answer;
    }


    /*---------------Set Operations-------------------*/

/**
        Returns the temporal intersection of ``self`` and ``other``.

        Args:
            other: temporal object to intersect with

        Returns:
            A :class:`Time` instance. The actual class depends on ``other``:
            a :class:`dateset` for a date or a set, a :class:`datespan` for a span,
            a :class:`datespanset` for a span set.

        MEOS Functions:
            intersection_set_date, intersection_set_set, intersection_span_span,
            intersection_spanset_spanset
*/

    public Time intersection(Object other) throws Exception {
        Time result;
        if (other instanceof LocalDate){
            result = new dateset(functions.intersection_set_date(this._inner, dateToTimestamp((LocalDate) other)));
        }
        else if (other instanceof dateset){
            result = new dateset(functions.intersection_set_set(this._inner, ((dateset) other)._inner));
        }
        else if (other instanceof datespan){
            datespan ds = this.to_span(datespan.class);
            result = new datespan(functions.intersection_span_span(ds.get_inner(), ((datespan) other).get_inner()));
        }
        else if (other instanceof datespanset){
            datespanset dss = this.to_spanset(datespanset.class);
            result = new datespanset(functions.intersection_spanset_spanset(dss.get_inner(), ((datespanset) other).get_inner()));
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
        return result;
    }

/**
        Returns the temporal difference of ``self`` and ``other``.

        Args:
            other: temporal object to diff with

        Returns:
            A :class:`TimeDate` instance. The actual class depends on ``other``.

        MEOS Functions:
            minus_set_date, minus_set_set, minus_spanset_span,
            minus_spanset_spanset
*/

    public Time minus(Object other) throws Exception{
        Time result;
        if (other instanceof LocalDate){
            result = new dateset(functions.minus_set_date(this._inner, dateToTimestamp((LocalDate) other)));
        }
        else if (other instanceof dateset){
            result = new dateset(functions.minus_set_set(this._inner, ((dateset) other)._inner));
        }
        else if (other instanceof datespan){
            datespan ds = this.to_span(datespan.class);
            result = new datespanset(functions.minus_span_span(ds.get_inner(), ((datespan) other).get_inner()));
        }
        else if (other instanceof datespanset){
            datespanset dss = this.to_spanset(datespanset.class);
            result = new datespanset(functions.minus_spanset_spanset(dss.get_inner(), ((datespanset) other).get_inner()));
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
        return result;
    }

/**
        Returns the temporal difference of ``other`` and ``self``.

        Args:
            other: the date to subtract this set from

        Returns:
            A :class:`dateset` instance.

        MEOS Functions:
            minus_date_set
*/

    public dateset subtract_from(LocalDate other) throws Exception {
        return new dateset(functions.minus_date_set(dateToTimestamp(other), this._inner));
    }

/**
        Returns the temporal union of ``self`` and ``other``.

        Args:
            other: temporal object to merge with

        Returns:
            A :class:`TimeDate` instance. The actual class depends on ``other``.

        MEOS Functions:
            union_set_date, union_set_set, union_spanset_span,
            union_spanset_spanset
*/

    public dateset union(Object other) throws Exception{
        dateset result = null;
        if (other instanceof LocalDate){
            Pointer resultPointer= functions.union_set_date(this._inner, dateToTimestamp((LocalDate) other));
            result = new dateset(resultPointer);
        }
        else if (other instanceof dateset){
            Pointer resultPointer= functions.union_set_set(this._inner, ((dateset) other)._inner);
            result = new dateset(resultPointer);
        }
        else if (other instanceof datespan){
            datespan ds = this.to_span(datespan.class);
            Pointer resultPointer= functions.union_span_span(ds.get_inner(), ((datespan) other).get_inner());
            result = new dateset(resultPointer);
        }
        else if (other instanceof datespanset){
            datespanset dss = this.to_spanset(datespanset.class);
            Pointer resultPointer= functions.union_spanset_spanset(dss.get_inner(), ((datespanset) other).get_inner());
            result = new dateset(resultPointer);
        }
        else{
            throw new Exception("Operation not supported with " + other + " type");
        }
        return result;
    }
}