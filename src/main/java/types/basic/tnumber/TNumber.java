package types.basic.tnumber;

import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import types.TemporalObject;
import types.basic.tfloat.TFloat;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.basic.tint.TInt;
import types.boxes.TBox;
import functions.functions;
import types.collections.number.*;
import types.collections.time.Time;
import types.collections.time.tstzset;
import types.collections.time.tstzspan;
import types.collections.time.tstzspanset;
import types.temporal.Factory;
import types.temporal.Temporal;
import types.temporal.TemporalType;
import utils.ConversionUtils;

import javax.naming.OperationNotSupportedException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Base interface that encompasses the Temporal float and Temporal integer interfaces.
 *
 * @author ARIJIT SAMAL
 */
public interface TNumber {
    Pointer getNumberInner();
    String getCustomType();
    TemporalType getTemporalType();

    /* ------------------------- Accessors ------------------------------------- */


    /**
     * Returns the bounding box of "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tbox_tnumber</li>
     * @return The bounding box of "this".
     */
    default TBox bounding_tbox() throws SQLException {
        return new TBox(functions.tnumber_to_tbox(getNumberInner()));
    }


    /**
     * Returns the integral of "this".
     *
     * <p>
     *         MEOS Function:
     *             <li>tnumber_integral</li>
     * @return The integral of "this".
     */
    default float integral(){
        return (float) functions.tnumber_integral(getNumberInner());
    }

    /**
     * Returns the time weighted average of "this".
     *
     * <p>
     *
     *         MEOS Function:
     *             <li>tnumber_twavg</li>
     * @return The time weighted average of "this".
     */
    default float time_weighted_average(){
        return (float) functions.tnumber_twavg(getNumberInner());
    }

    /* ------------------------- Transformations ---------------------------------- */

/**
        Returns a new :class:`TNumber` with the value dimension shifted by
        ``delta``.

        Args:
            delta: value to shift

        MEOS Functions:
            tint_shift_value, tfloat_shift_value
 */
    default TNumber shift_value(Object delta) throws Exception {
        Pointer shifted= null;
        if(this instanceof TInt){
            shifted= functions.tint_shift_value(this.getNumberInner(), (int) delta);
        }
        else if (this instanceof TFloat){
            shifted= functions.tfloat_shift_value(this.getNumberInner(), (double) delta);
        }
        else{
            throw new Exception("Operation not supported for this object");
        }
        return (TNumber) Factory.create_temporal(shifted, getCustomType(), getTemporalType());
    }

/**
        Returns a new :class:`TNumber` scaled so the value dimension has
        width ``width``.

        Args:
            width: value representing the width of the new temporal number

        MEOS Functions:
            tint_scale_value, tfloat_scale_value
*/
    default TNumber scale_value(Object width) throws Exception {
        Pointer scaled= null;
        if(this instanceof TInt){
            scaled= functions.tint_scale_value(this.getNumberInner(), (int) width);
        }
        else if (this instanceof TFloat){
            scaled= functions.tfloat_scale_value(this.getNumberInner(), (double) width);
        }
        else{
            throw new Exception("Operation not supported for this object");
        }
        return (TNumber) Factory.create_temporal(scaled, getCustomType(), getTemporalType());
    }

    /**
        Returns a new :class:`TNumber` with the value dimension shifted by
        ``shift`` and scaled so the value dimension has width ``width``.

        Args:
            shift: value to shift
            width: value representing the width of the new temporal number

        MEOS Functions:
            tint_shift_scale_value, tfloat_shift_scale_value
*/
    default TNumber shift_scale_value(Object shift, Object width) throws Exception {
        Pointer scaled= null;
        if(this instanceof TInt && shift!=null && width!=null){
            scaled= functions.tint_shift_scale_value(this.getNumberInner(), (int) shift, (int) width);
        }
        else if (this instanceof TFloat && shift!=null && width!=null){
            scaled= functions.tfloat_shift_scale_value(this.getNumberInner(), (double) shift, (double) width);
        }
        else{
            throw new Exception("Operation not supported for this object");
        }
        return (TNumber) Factory.create_temporal(scaled, getCustomType(), getTemporalType());
    }



    /* ------------------------- Restrictions ---------------------------------- */


    /**
     * Returns a new temporal object with the values of "this" restricted to
     *         the value or time "other".
     *
     *   <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>temporal_at_values</li>
     *             <li>tnumber_at_span</li>
     *             <li>tnumber_at_spanset</li>
     *             <li>tnumber_at_tbox</li>
     *             <li>temporal_at_timestamp</li>
     *             <li>temporal_at_timestampset</li>
     *             <li>temporal_at_period</li>
     *             <li>temporal_at_periodset</li>
     *        </ul>
     *
     * @param other A time or value object to restrict the values of "this" to.
     * @return A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber at(Object other) throws OperationNotSupportedException {
        if (other instanceof IntSet){
            return (TNumber) Factory.create_temporal(functions.temporal_at_values(getNumberInner(),((IntSet) other).get_inner()),getCustomType(),getTemporalType());
        } else if (other instanceof FloatSet) {
            return (TNumber) Factory.create_temporal(functions.temporal_at_values(getNumberInner(),((FloatSet) other).get_inner()),getCustomType(),getTemporalType());
        }
         else if (other instanceof IntSpan){
            return (TNumber) Factory.create_temporal(functions.tnumber_at_span(getNumberInner(),((IntSpan) other).get_inner()),getCustomType(),getTemporalType());
        } else if (other instanceof FloatSpan) {
            return (TNumber) Factory.create_temporal(functions.tnumber_at_span(getNumberInner(),((FloatSpan) other).get_inner()),getCustomType(),getTemporalType());
        }
        else if (other instanceof IntSpanSet){
            return (TNumber) Factory.create_temporal(functions.tnumber_at_spanset(getNumberInner(),((IntSpanSet) other).get_inner()),getCustomType(),getTemporalType());
        } else if (other instanceof FloatSpanSet) {
            return (TNumber) Factory.create_temporal(functions.tnumber_at_spanset(getNumberInner(),((FloatSpanSet) other).get_inner()),getCustomType(),getTemporalType());
        }
        else if (other instanceof TBox){
            return (TNumber) Factory.create_temporal(functions.tnumber_at_tbox(getNumberInner(),((TBox) other).get_inner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }


    /**
     * Returns a new temporal object with the values of "this" restricted to
     *         the complement of the value or time "other".
     *
     *  <p>
     *         MEOS Functions:
     *         <ul>
     *             <li>temporal_minus_values</li>
     *             <li>tnumber_minus_span</li>
     *             <li>tnumber_minus_spanset</li>
     *             <li>tnumber_minus_tbox</li>
     *             <li>temporal_minus_timestamp</li>
     *             <li>temporal_minus_timestampset</li>
     *             <li>temporal_minus_period</li>
     *             <li>temporal_minus_periodset</li>
     *         </ul>
     * @param other A time or value object to restrict the values of "this" to
     *      *             the complement of.
     * @return A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber minus(Object other) throws OperationNotSupportedException {
        if (other instanceof IntSet){
            return (TNumber) Factory.create_temporal(functions.temporal_minus_values(getNumberInner(),((IntSet) other).get_inner()),getCustomType(),getTemporalType());
        } else if (other instanceof FloatSet) {
            return (TNumber) Factory.create_temporal(functions.temporal_minus_values(getNumberInner(),((FloatSet) other).get_inner()),getCustomType(),getTemporalType());
        }
        else if (other instanceof IntSpan){
            return (TNumber) Factory.create_temporal(functions.tnumber_minus_span(getNumberInner(),((IntSpan) other).get_inner()),getCustomType(),getTemporalType());
        } else if (other instanceof FloatSpan) {
            return (TNumber) Factory.create_temporal(functions.tnumber_minus_span(getNumberInner(),((FloatSpan) other).get_inner()),getCustomType(),getTemporalType());
        }
        else if (other instanceof IntSpanSet){
            return (TNumber) Factory.create_temporal(functions.tnumber_minus_spanset(getNumberInner(),((IntSpanSet) other).get_inner()),getCustomType(),getTemporalType());
        } else if (other instanceof FloatSpanSet) {
            return (TNumber) Factory.create_temporal(functions.tnumber_minus_spanset(getNumberInner(),((FloatSpanSet) other).get_inner()),getCustomType(),getTemporalType());
        }
        else if (other instanceof TBox){
            return (TNumber) Factory.create_temporal(functions.tnumber_minus_tbox(getNumberInner(),((TBox) other).get_inner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }








    /* ------------------------- Position Operations --------------------------- */


    /**
     * Returns whether the bounding box of "this" is left to the bounding box
     *         of "other".
     *
     *  <p>
     *
     * @param other A box or a temporal object to compare to "this".
     * @return True if left, False otherwise.
     * @throws SQLException
     */
    default boolean is_left(TemporalObject other) throws SQLException {
        return this.bounding_tbox().is_left(other);
    }


    /**
     * Returns whether the bounding box of "this" is over or left to the
     *         bounding box of "other".
     *
     *  <p>
     *
     * @param other A box or a temporal object to compare to "this".
     * @return True if over or left, False otherwise.
     * @throws SQLException
     */
    default boolean is_over_or_left(TemporalObject other) throws SQLException {
        return this.bounding_tbox().is_over_or_left(other);
    }


    /**
     * Returns whether the bounding box of "this" is right to the bounding
     *         box of "other".
     *
     *  <p>
     *
     * @param other A box or a temporal object to compare to "this".
     * @return True if right, False otherwise.
     * @throws SQLException
     */
    default boolean is_right(TemporalObject other) throws SQLException {
        return this.bounding_tbox().is_right(other);
    }

    /**
     * Returns whether the bounding box of "this" is over or right to the
     *         bounding box of "other".
     *
     * <p>
     *
     * @param other A box or a temporal object to compare to "this".
     * @return True if over or right, False otherwise.
     * @throws SQLException
     */
    default boolean is_over_or_right(TemporalObject other) throws SQLException {
        return this.bounding_tbox().is_over_or_right(other);
    }



    /* ------------------------- Mathematical Operations ------------------------- */


    /**
     * Returns a new temporal object with the values of "this" plus "other".
     *
     *  <p>
     *         MEOS FUNCTIONS:
     *          <ul>
     *              <li>add_tin_int</li>
     *              <li>add_tfloat_float</li>
     *              <li>add_tnumber_tnumber</li>
     *          </ul>
     * @param other A {@link Integer}, {@link Float} or {@link TNumber} to add
     *      *             to "this".
     * @return A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber add(Object other) throws OperationNotSupportedException {
        if ((this instanceof TInt) && (other instanceof Integer)){
            return (TNumber) Factory.create_temporal(functions.add_tint_int(getNumberInner(),((Integer) other).intValue()),getCustomType(),getTemporalType());
        } else if ((this instanceof TFloat) && (other instanceof Float)) {
            return (TNumber) Factory.create_temporal(functions.add_tfloat_float(getNumberInner(),((Float) other).floatValue()),getCustomType(),getTemporalType());
        } else if (other instanceof TNumber) {
            return (TNumber) Factory.create_temporal(functions.add_tnumber_tnumber(getNumberInner(),((TNumber) other).getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }


    /**
     * Returns a new temporal object with the values of "this" plus "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>add_int_tint</li>
     *             <li>add_float_tfloat</li>
     *         </ul>
     * @param other A {@link Integer} or {@link Float} to add to "this".
     * @return A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber radd(Object other) throws OperationNotSupportedException {
        if ((this instanceof TInt) && (other instanceof Integer)){
            return (TNumber) Factory.create_temporal(functions.add_int_tint(((Integer) other).intValue(),getNumberInner()),getCustomType(),getTemporalType());
        } else if ((this instanceof TFloat) && (other instanceof Float)) {
            return (TNumber) Factory.create_temporal(functions.add_float_tfloat(((Float) other).floatValue(),getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }


    /**
     * Returns a new temporal object with the values of "this" minus "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>sub_tint_int</li>
     *             <li>sub_tfloat_float</li>
     *             <li>sub_tnumber_tnumber</li>
     *         </ul>
     * @param other A {@link Integer}, {@link Float} or {@link TNumber} to add
     *      *      *             to "this".
     * @return  A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber sub(Object other) throws OperationNotSupportedException {
        if ((this instanceof TInt) && (other instanceof Integer)){
            return (TNumber) Factory.create_temporal(functions.sub_tint_int(getNumberInner(),((Integer) other).intValue()),getCustomType(),getTemporalType());
        } else if ((this instanceof TFloat) && (other instanceof Float)) {
            return (TNumber) Factory.create_temporal(functions.sub_tfloat_float(getNumberInner(),((Float) other).floatValue()),getCustomType(),getTemporalType());
        } else if (other instanceof TNumber) {
            return (TNumber) Factory.create_temporal(functions.sub_tnumber_tnumber(getNumberInner(),((TNumber) other).getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }


    /**
     * Returns a new temporal object with the values of "this" minus "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>sub_int_tint</li>
     *             <li>sub_float_tfloat</li>
     *         </ul>
     * @param other A {@link Integer} or {@link Float} to add to "this".
     * @return A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber rsub(Object other) throws OperationNotSupportedException {
        if ((this instanceof TInt) && (other instanceof Integer)){
            return (TNumber) Factory.create_temporal(functions.sub_int_tint(((Integer) other).intValue(),getNumberInner()),getCustomType(),getTemporalType());
        } else if ((this instanceof TFloat) && (other instanceof Float)) {
            return (TNumber) Factory.create_temporal(functions.sub_float_tfloat(((Float) other).floatValue(),getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }


    /**
     * Returns a new temporal object with the values of "this" multiplied by "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>mult_tint_int</li>
     *             <li>mult_tfloat_float</li>
     *             <li>mult_tnumber_tnumber</li>
     *         </ul>
     * @param other A {@link Integer}, {@link Float} or {@link TNumber} to add
     *      *      *             to "this".
     * @return  A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber mul(Object other) throws OperationNotSupportedException {
        if ((this instanceof TInt) && (other instanceof Integer)){
            return (TNumber) Factory.create_temporal(functions.mult_tint_int(getNumberInner(),((Integer) other).intValue()),getCustomType(),getTemporalType());
        } else if ((this instanceof TFloat) && (other instanceof Float)) {
            return (TNumber) Factory.create_temporal(functions.mult_tfloat_float(getNumberInner(),((Float) other).floatValue()),getCustomType(),getTemporalType());
        } else if (other instanceof TNumber) {
            return (TNumber) Factory.create_temporal(functions.mult_tnumber_tnumber(getNumberInner(),((TNumber) other).getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }



    /**
     * Returns a new temporal object with the values of "this" multiplied "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>mult_int_tint</li>
     *             <li>mult_float_tfloat</li>
     *         </ul>
     * @param other A {@link Integer} or {@link Float} to add to "this".
     * @return A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber rmul(Object other) throws OperationNotSupportedException {
        if ((this instanceof TInt) && (other instanceof Integer)){
            return (TNumber) Factory.create_temporal(functions.mult_int_tint(((Integer) other).intValue(),getNumberInner()),getCustomType(),getTemporalType());
        } else if ((this instanceof TFloat) && (other instanceof Float)) {
            return (TNumber) Factory.create_temporal(functions.mult_float_tfloat(((Float) other).floatValue(),getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }




    /**
     * Returns a new temporal object with the values of "this" divided by "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>div_tint_int</li>
     *             <li>div_tfloat_float</li>
     *             <li>div_tnumber_tnumber</li>
     *         </ul>
     * @param other A {@link Integer}, {@link Float} or {@link TNumber} to add
     *      *      *             to "this".
     * @return  A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber div(Object other) throws OperationNotSupportedException {
        if ((this instanceof TInt) && (other instanceof Integer)){
            return (TNumber) Factory.create_temporal(functions.div_tint_int(getNumberInner(),((Integer) other).intValue()),getCustomType(),getTemporalType());
        } else if ((this instanceof TFloat) && (other instanceof Float)) {
            return (TNumber) Factory.create_temporal(functions.div_tfloat_float(getNumberInner(),((Float) other).floatValue()),getCustomType(),getTemporalType());
        } else if (other instanceof TNumber) {
            return (TNumber) Factory.create_temporal(functions.div_tnumber_tnumber(getNumberInner(),((TNumber) other).getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }





    /**
     * Returns a new temporal object with the values of "this" divided by "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>div_int_tint</li>
     *             <li>div_float_tfloat</li>
     *         </ul>
     * @param other A {@link Integer} or {@link Float} to add to "this".
     * @return A new temporal object of the same subtype as "this".
     * @throws OperationNotSupportedException
     */
    default TNumber rdiv(Object other) throws OperationNotSupportedException {
        if ((this instanceof TInt) && (other instanceof Integer)){
            return (TNumber) Factory.create_temporal(functions.div_int_tint(((Integer) other).intValue(),getNumberInner()),getCustomType(),getTemporalType());
        } else if ((this instanceof TFloat) && (other instanceof Float)) {
            return (TNumber) Factory.create_temporal(functions.div_float_tfloat(((Float) other).floatValue(),getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }

/**
        Returns a new temporal object with the values of `self` plus `other`.

        Args:
            other: A :class:`int`, :class:`float` or :class:`TNumber` to add to
            `self`.

        Returns:
            A new temporal object of the same subtype as `self`.
 */
    default TNumber _add(Object other) throws OperationNotSupportedException {
        return this.add(other);
    }


/**
        Returns a new temporal object with the values of `self` plus `other`.

        Args:
            other: A :class:`int` or :class:`float` to add to `self`.

        Returns:
            A new temporal object of the same subtype as `self`.

        MEOS Functions:
            add_int_tint, add_float_tfloat
*/
    default TNumber _radd(Object other) throws OperationNotSupportedException {
        return this.radd(other);
    }

/**
        Returns a new temporal object with the values of `self` minus `other`.

        Args:
            other: A :class:`int`, :class:`float` or :class:`TNumber` to
            subtract from `self`.

        Returns:
            A new temporal object of the same subtype as `self`.

        MEOS Functions:
            sub_tint_int, sub_tfloat_float, sub_tnumber_tnumber
*/
    default TNumber _sub(Object other) throws OperationNotSupportedException {
        return this.sub(other);
    }

/**
        Returns a new temporal object with the values of `other` minus `self`.

        Args:
            other: A :class:`int` or :class:`float` to subtract `self` to.

        Returns:
            A new temporal object of the same subtype as `self`.

        MEOS Functions:
            sub_int_tint, sub_float_tfloat
*/
    default TNumber _rsub(Object other) throws OperationNotSupportedException {
        return this.rsub(other);
    }

/**
        Returns a new temporal object with the values of `self` multiplied by
        `other`.

        Args:
            other: A :class:`int`, :class:`float` or :class:`TNumber` to
            multiply `self` by.

        Returns:
            A new temporal object of the same subtype as `self`.

        MEOS Functions:
            mult_tint_int, mult_tfloat_float, mult_tnumber_tnumber
*/
    default TNumber _mul(Object other) throws OperationNotSupportedException {
        return this.mul(other);
    }

/**
        Returns a new temporal object with the values of `self` multiplied
        by `other`.

        Args:
            other: A :class:`int` or :class:`float` to multiply by `self`.

        Returns:
            A new temporal object of the same subtype as `self`.

        MEOS Functions:
            mult_int_tint, mult_float_tfloat
*/
    default  TNumber _rmul(Object other) throws OperationNotSupportedException {
        return this.rmul(other);
    }

/**
        Returns a new temporal object with the values of `self` divided by
        `other`.

        Args:
            other: A :class:`int`, :class:`float` or :class:`TNumber` to divide
            `self` by.

        Returns:
            A new temporal object of the same subtype as `self`.

        MEOS Functions:
            div_tint_int, div_tfloat_float, div_tnumber_tnumber
*/
    default TNumber _trueDiv(Object other) throws OperationNotSupportedException {
        return this.div(other);
    }

/**
        Returns a new temporal object with the values of `other` divided by
        `self`.

        Args:
            other: A :class:`int` or :class:`float` to divide by `self`.

        Returns:
            A new temporal object of the same subtype as `self`.

        MEOS Functions:
            div_int_tint, div_float_tfloat
*/
    default TNumber _rTrueDiv(Object other) throws OperationNotSupportedException {
        return this.rdiv(other);
    }


    /**
     * Returns the absolute value of "this".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>tnumber_abs</li>
     * @return A new {@link TNumber} instance.
     */
    default TNumber abs(){
        return (TNumber) Factory.create_temporal(functions.tnumber_abs(getNumberInner()),getCustomType(),getTemporalType());
    }

    /**
     * Returns the value difference between consecutive instants of "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tnumber_delta_value</li>
     * @return A new {@link TNumber} instance.
     */
    default TNumber delta_value(){
        return (TNumber) Factory.create_temporal(functions.tnumber_delta_value(getNumberInner()),getCustomType(),getTemporalType());
    }


    /* ------------------------- Distance Operations -------------------------- */


    /**
     * Returns the temporal distance between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>distance_tfloat_float</li>
     *             <li>distance_tnumber_tnumber</li>
     *         </ul>
     * @param other A {@link Integer}, {@link Float} or {@link TNumber} to
     *      *             compare to "this".
     * @return A {@link TFloat} with the distance between "this" and "other".
     * @throws OperationNotSupportedException
     */
    default TFloat distance(Object other) throws OperationNotSupportedException {
        if ( (other instanceof Integer)){
            return (TFloat) Factory.create_temporal(functions.distance_tfloat_float(getNumberInner(),(float)((Integer) other).intValue()),getCustomType(),getTemporalType());
        } else if ((other instanceof Float)) {
            return (TFloat) Factory.create_temporal(functions.distance_tfloat_float(getNumberInner(),((Float) other).floatValue()),getCustomType(),getTemporalType());
        } else if (other instanceof TNumber) {
            return (TFloat) Factory.create_temporal(functions.distance_tnumber_tnumber(getNumberInner(),((TNumber) other).getNumberInner()),getCustomType(),getTemporalType());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }


    /**
     * Returns the nearest approach distance between "this" and "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             nad_tfloat_float
     *             nad_tfloat_tfloat
     *             nad_tnumber_tbox
     * @param other A {@link Integer}, {@link Float} or {@link TNumber} or {@link TBox}to
     *      *      *             compare to "this".
     * @return A {@link Float} with the nearest approach distance between "this"
     *      *             and "other".
     * @throws OperationNotSupportedException
     */
    default float nearest_approach_distance(Object other) throws OperationNotSupportedException {
        if ( (other instanceof Integer)){
            return (float) functions.nad_tfloat_float(getNumberInner(),(float)((Integer) other).intValue());
        } else if ((other instanceof Float)) {
            return (float) functions.nad_tfloat_float(getNumberInner(),((Float) other).floatValue());
        } else if (other instanceof TNumber) {
            return (float) functions.nad_tfloat_tfloat(getNumberInner(),((TNumber) other).getNumberInner());
        } else if (other instanceof TBox) {
            return (float) functions.nad_tfloat_tfloat(getNumberInner(),((TBox) other).get_inner());
        }
        else{
            throw new OperationNotSupportedException("Operand not supported");
        }
    }

    /* --------------------------------------------- Split Operations ---------------------------------------------- */

/**
        Splits `self` into fragments with respect to value buckets

        Args:
            start: Start value of the first value bucket.
            size: Size of the value buckets.

        Returns:
            A list of temporal ints.

        MEOS Functions:
            tint_value_split
*/
//    private Pointer createEmptyPointerArray(Runtime runtime) {
//        // Allocate memory for a list of integers (let's assume a fixed size, e.g., 10 elements)
//        Pointer listPointer = Memory.allocate(Runtime.getRuntime(runtime), *Long.BYTES); // Adjust size as needed
//        return listPointer;
//    }

    private Pointer createEmptyPointerArray(Runtime runtime, int size) {
        // Allocate memory for a list of integers (let's assume a fixed size, e.g., 10 elements)
        Pointer listPointer = Memory.allocate(Runtime.getRuntime(runtime), size*Long.BYTES); // Adjust size as needed
        return listPointer;
    }

    default List<TNumber> value_split(int size, int start){
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
        Pointer listPointer = createEmptyPointerArray(runtime, size);
        Pointer result= functions.tint_value_split(this.getNumberInner(), size, start, listPointer, intPointer);
        List<TNumber> tempList= new ArrayList<>();
        int count= intPointer.getInt(Integer.BYTES);
        for(int i=0;i<count;i++){
            Pointer res= result.getPointer((long) i *Long.BYTES);
            TNumber t= (TNumber) Factory.create_temporal(res, this.getCustomType(), this.getTemporalType());
            tempList.add(t);
        }
        return tempList;
    }

/**
        Splits `self` into fragments with respect to value and tstzspan buckets.

        Args:
            value_size: Size of the value buckets.
            duration: Duration of the tstzspan buckets.
            value_start: Start value of the first value bucket. If None, the
                start value used by default is 0
            time_start: Start time of the first tstzspan bucket. If None, the
                start time used by default is Monday, January 3, 2000.

        Returns:
            A list of temporal integers.

        MEOS Functions:
*/

    default List<TNumber> value_time_split(Object duration, int value_size, int value_start, Object time_start){
        OffsetDateTime st= null;
        Pointer dt= null;
        if(time_start != null){
            st= functions.pg_timestamptz_in("2000-01-03", -1);
        }
        else{
            if(time_start instanceof LocalDateTime){
                st= ConversionUtils.datetimeToTimestampTz((LocalDateTime) time_start);
            }
            else{
                st= functions.pg_timestamptz_in(time_start.toString(), -1);
            }

            if(duration instanceof Duration){
                dt= ConversionUtils.timedelta_to_interval((Duration) duration);
            }
            else{
                dt= functions.pg_interval_in(duration.toString(), -1);
            }
        }
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
        Pointer valueListPointer = createEmptyPointerArray(runtime, value_size);
        Pointer timeListPointer = createEmptyPointerArray(runtime, value_size);
        Pointer p= functions.tint_value_time_split(this.getNumberInner(), value_size, dt, value_start, st, valueListPointer, timeListPointer, intPointer);
        List<TNumber> tempList= new ArrayList<>();
        int count= intPointer.getInt(Integer.BYTES);
        for(int i=0;i<count;i++){
            Pointer res= p.getPointer((long) i *Long.BYTES);
            TNumber t= (TNumber) Factory.create_temporal(res, this.getCustomType(), this.getTemporalType());
            tempList.add(t);
        }
        return tempList;
    }
}
