package types.temporal;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.collections.base.Base;
import types.collections.time.Period;
import types.collections.time.PeriodSet;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import functions.functions;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import utils.ConversionUtils;

import static types.temporal.TemporalType.*;

/**
 * Abstract class for Temporal sub types
 * @param <V> - Base type of the temporal data type eg. Integer, Boolean
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class Temporal<V extends Serializable> implements Serializable, TemporalObject {
    private Pointer inner;

    /** ------------------------- Constructors ---------------------------------------- */

    public Temporal(){
    }

    public Temporal(String str){
        this.inner = createStringInner(str);
    }

    public Temporal(Pointer inner){
        this.inner = createInner(inner);
    }

    public Temporal(String customType, TemporalType temporalType) {
        //this.customType = customType;
        //this.temporalType = temporalType;
    }

    public abstract Pointer createStringInner(String str);
    public abstract Pointer createInner(Pointer inner);
    public abstract String getCustomType();
    public abstract TemporalType getTemporalType();


    public Temporal _factory(Pointer inner, String customType, TemporalType temporalType){
        return Factory.create_temporal(inner, customType, temporalType);
    }

    /**
     * Returns a copy of the temporal object.
     *
     * @return a copy of the object
     */
    public Temporal copy(){
        return Factory.create_temporal(functions.temporal_copy(this.inner),this.getCustomType(),this.getTemporalType());
    }

    /**
     * Returns a temporal object from a hex-encoded WKB string.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_from_hexwkb</li>
     *
     * @param str The hex-encoded WKB string.
     * @return  A temporal object from a hex-encoded WKB string.
     */
    public Temporal from_hexwkb(String str){
        Pointer result = functions.temporal_from_hexwkb(str);
        return Factory.create_temporal(result, this.getCustomType(), this.getTemporalType());
    }

    /**
     * Returns a temporal object from a MF-JSON string.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_from_mfjson</li>
     *
     * @param str The MF-JSON string.
     * @return A temporal object from a MF-JSON string.
     */
    public Temporal from_mfjson(String str){
        Pointer result = functions.temporal_from_mfjson(str);
        return Factory.create_temporal(result, this.getCustomType(), this.getTemporalType());
    }



    /* ------------------------- Output ---------------------------------------- */


    /**
     * Returns the temporal object as a MF-JSON string.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_as_mfjson</li>
     *
     * @param with_bbox Whether to include the bounding box in the output.
     * @param flags The flags to use for the output.
     * @param precision The precision to use for the output.
     * @param srs The SRS to use for the output.
     * @return The temporal object as a MF-JSON string.
     */
    public String as_mfjson(boolean with_bbox, int flags, int precision, String srs){
        return functions.temporal_as_mfjson(this.inner,with_bbox,flags,precision,srs);
    }


    /**
     * Returns the temporal object as a MF-JSON string.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_as_mfjson</li>
     *
     * @return The temporal object as a MF-JSON string.
     */
    public String as_mfjson(){
        return this.as_mfjson(true,3,6,null);
    }



    /** ------------------------- Accessors ---------------------------------------- */


    public Pointer getInner(){
        return this.inner;
    }

    /**
     * Returns the bounding box of "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_period</li>
     *
     * @return The bounding box of `self`.
     */
    public Period bounding_box(){
        return new Period(functions.temporal_to_period(this.inner));
    }

    /**
     * Returns the {@link PeriodSet} on which `self` is defined.
     *
     *         MEOS Functions:
     *             temporal_time
     * @return the {@link PeriodSet}  on which `self` is defined.
     */
    public PeriodSet time(){
        return new PeriodSet(functions.temporal_time(this.inner));
    }



    public TInterpolation interpolation(){
        return TInterpolation.fromString(functions.temporal_interp(this.inner),true);
    }


    /**
     * Returns the {@link Period} on which "this" is defined ignoring
     *         potential time gaps.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_period</li>
     * @return
     */
    public Period period(){
        return this.timespan();
    }

    /**
     * Returns the {@link Period} on which "this" is defined ignoring
     *         potential time gaps.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_period</li>
     * @return
     */
    public Period timespan(){
        return new Period(functions.temporal_to_period(this.inner));
    }


    /**
     * Returns the number of instants in "this".
     *
     *         MEOS Functions:
     *             temporal_num_instants
     * @return Returns the number of instants in "this".
     */
    public int num_instants(){
        return functions.temporal_num_instants(this.inner);
    }


    /**
     * Returns the first instant in "this".
     *  <p>
     *         MEOS Functions:
     *             <li>temporal_start_instant</li>
     * @return Returns the first instant in "this".
     */
    public Temporal start_instant(){
        return Factory.create_temporal(functions.temporal_start_instant(this.inner), this.getCustomType(), TEMPORAL_INSTANT);
    }

    /**
     * Returns the last instant in "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_end_instant</li>
     * @return Returns the last instant in "this".
     */
    public Temporal end_instant(){
        return Factory.create_temporal(functions.temporal_end_instant(this.inner), this.getCustomType(), TEMPORAL_INSTANT);
    }


    /**
     * Returns the instant in "this" with the minimum value.
     *         If multiple instants have the minimum value, the first one is returned.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_min_instant</li>
     * @return Returns the instant in "this" with the minimum value.
     */
    public Temporal min_instant(){
        return Factory.create_temporal(functions.temporal_min_instant(this.inner), this.getCustomType(), TEMPORAL_INSTANT);
    }

    /**
     * Returns the instant in "this" with the maximum value.
     *         If multiple instants have the maximum value, the first one is returned.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_max_instant</li>
     * @return Returns the instant in "this" with the maximum value.
     */
    public Temporal max_instant(){
        return Factory.create_temporal(functions.temporal_max_instant(this.inner), this.getCustomType(), TEMPORAL_INSTANT);
    }

    /**
     * Returns the n-th instant in "this". (0-based)
     * <p>
     *         MEOS Functions:
     *             <li>temporal_instant_n</li>
     * @param n instant
     * @return a new Temporal
     */
    public Temporal instant_n(int n){
        return Factory.create_temporal(functions.temporal_instant_n(this.inner, n+1), this.getCustomType(), TEMPORAL_INSTANT);
    }

    /**
     * Returns the number of timestamps in "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_num_timestamps</li>
     * @return Returns the number of timestamps in "this".
     */
    public int num_timestamps(){
        System.out.println("ici");
        System.out.println(this.inner);
        return functions.temporal_num_timestamps(this.inner);
    }

    /**
     * Returns the first timestamp in "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_start_timestamps</li>
     * @return Returns the first timestamp in "this".
     */
    public LocalDateTime start_timestamp(){
        return ConversionUtils.timestamptz_to_datetime(functions.temporal_start_timestamp(this.inner));
    }


    /**
     * Returns the last timestamp in "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_end_timestamps</li>
     * @return Returns the last timestamp in "this".
     */
    public LocalDateTime end_timestamp(){
        return ConversionUtils.timestamptz_to_datetime(functions.temporal_end_timestamp(this.inner));
    }

    /**
     * Returns the hash of the temporal object.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_hash</li>
     * @return The hash of the temporal object.
     */
    public long hash(){
        return functions.temporal_hash(this.inner);
    }

    /* ------------------------- Transformations ---------------------------------------- */

    /**
     * Returns a new {@link Temporal} object equal to "this" with the given
     *         interpolation.
     *
     *         MEOS Functions:
     *             <li>temporal_set_interpolation</li>
     * @param interpolation int value
     * @return Returns a new {@link Temporal} object equal to "this" with the given
     *      *         interpolation.
     */
    public Temporal set_interpolation(TInterpolation interpolation){
        return Factory.create_temporal(functions.temporal_set_interp(this.inner, interpolation.getValue()),this.getCustomType(),this.getTemporalType());

    }


    /**
     * Returns "this" as a {@link TInstant}.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_tinstant</li>
     * @return Returns "this" as a {@link TInstant}.
     */
    public Temporal to_instant(){
        return Factory.create_temporal(functions.temporal_to_tinstant(this.inner),this.getCustomType(),TEMPORAL_INSTANT);
    }


    /**
     * Converts "this" into a {@link TSequence}.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_sequence</li>
     * @param interpolation int value
     * @return a new {@link TSequence}.
     */
    public Temporal to_sequence(TInterpolation interpolation){
        return Factory.create_temporal(functions.temporal_to_tsequence(this.inner, interpolation.getValue()),this.getCustomType(),TEMPORAL_SEQUENCE);

    }

    /**
     * Returns "this" as a new {@link TSequenceSet}.
     *
     *         MEOS Functions:
     *             <li>temporal_to_tsequenceset</li>
     * @param interpolation int value
     * @return a new {@link TSequenceSet}
     */
    public Temporal to_sequenceset(TInterpolation interpolation){
        return Factory.create_temporal(functions.temporal_to_tsequenceset(this.inner, interpolation.getValue()),this.getCustomType(),TEMPORAL_SEQUENCE_SET);

    }




    /* ------------------------- Modifications ---------------------------------------- */


    /**
     * Returns a new {@link Temporal} object equal to "this" with "sequence"
     *         appended.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_append_tsequence</li>
     * @param sequence sequence: a {@link TSequence} to append
     * @return a new {@link Temporal} object
     */
    public Temporal append_sequence(TSequence sequence){
        return Factory.create_temporal(functions.temporal_append_tsequence(this.inner, sequence.getInner(), false), this.getCustomType(), this.getTemporalType());
    }




    /**
     * Returns a new {@link Temporal} object equal to "this" with "other"
     *         inserted.
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_insert</li>
     *
     * @param other {@link Temporal} object to insert in "this"
     * @return Returns a new {@link Temporal} object equal to "this" with "other"
     *      *         inserted.
     */
    public Temporal insert(Temporal other){
        return insert(other,true);
    }



    /**
     * Returns a new {@link Temporal} object equal to "this" with "other"
     *         inserted.
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_insert</li>
     *
     * @param other {@link Temporal} object to insert in "this"
     * @param connect wether to connect the inserted elements with the existing
     *      *                 elements.
     * @return Returns a new {@link Temporal} object equal to "this" with "other"
     *      *         inserted.
     */
    public Temporal insert(Temporal other, boolean connect){
        return Factory.create_temporal(functions.temporal_insert(this.inner,other.inner,connect),this.getCustomType(),this.getTemporalType());
    }


    /**
     * Returns a new {@link Temporal} object equal to "this" updated with
     *         "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>temporal_update</li>
     * @param other {@link Temporal} object to update "this" with
     * @return Returns a new {@link Temporal} object equal to "this" updated with
     *      *         "other".
     */
    public Temporal update(Temporal other){
        return update(other, true);
    }

    /**
     * Returns a new {@link Temporal} object equal to "this" updated with
     *         "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>temporal_update</li>
     * @param other {@link Temporal} object to update "this" with
     * @param connect wether to connect the updated elements with the
     *      *                 existing elements.
     * @return Returns a new {@link Temporal} object equal to "this" updated with
     *      *         "other".
     */
    public Temporal update(Temporal other, boolean connect){
        return Factory.create_temporal(functions.temporal_update(this.inner,other.inner,connect),this.getCustomType(),this.getTemporalType());
    }


    /* ------------------------- Restrictions ---------------------------------- */


    /**
     * Returns a new temporal object with the values of "this" restricted to
     *         the time "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>temporal_at_timestamp</li>
     *             <li>temporal_at_timestampset</li>
     *             <li>temporal_at_period</li>
     *             <li>temporal_at_periodset</li>
     *        </ul>
     *
     * @param other A time object to restrict the values of "this" to.
     * @return A new temporal object of the same subtype as `self`
     */
    public Temporal at(Time other){
        Pointer result = null;
        if (other instanceof Period){
            result = functions.temporal_at_period(this.inner,((Period) other).get_inner());

        } else if (other instanceof PeriodSet) {
            result = functions.temporal_at_periodset(this.inner,((PeriodSet) other).get_inner());

        } else if (other instanceof TimestampSet) {
            result = functions.temporal_at_timestampset(this.inner,((TimestampSet) other).get_inner());
        }
        return Factory.create_temporal(result, this.getCustomType(),this.getTemporalType());
    }


    /**
     * Returns a new temporal object containing the times "this" is at its
     *         minimum value.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>temporal_at_min</li>
     *
     * @return A new temporal object of the same subtype as `self`.
     */
    public Temporal at_min(){
        return Factory.create_temporal(functions.temporal_at_min(this.inner),this.getCustomType(),this.getTemporalType());
    }


    /**
     * Returns a new temporal object containing the times "this" is at its
     *         maximum value.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>temporal_at_max</li>
     *
     * @return A new temporal object of the same subtype as `self`.
     */
    public Temporal at_max(){
        return Factory.create_temporal(functions.temporal_at_max(this.inner),this.getCustomType(),this.getTemporalType());
    }


    /**
     * Returns a new temporal object with the values of "this" removing those
     *         happening at "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>temporal_minus_timestamp</li>
     *             <li>temporal_minus_timestampset</li>
     *             <li>temporal_minus_period</li>
     *             <li>temporal_minus_periodset</li>
     *         </ul>
     *
     * @param other A time object to remove from "this".
     * @return A new temporal object of the same subtype as "this".
     */
    public Temporal minus(Time other){
        Pointer result = null;
        if (other instanceof Period){
            result = functions.temporal_minus_period(this.inner,((Period) other).get_inner());

        } else if (other instanceof PeriodSet) {
            result = functions.temporal_minus_periodset(this.inner,((PeriodSet) other).get_inner());

        } else if (other instanceof TimestampSet) {
            result = functions.temporal_minus_timestampset(this.inner,((TimestampSet) other).get_inner());
        }
        return Factory.create_temporal(result, this.getCustomType(),this.getTemporalType());
    }

    /**
     * Returns a new temporal object containing the times "this" is not at
     *         its minimum value.
     *
     *  <p>
     *         MEOS Functions:
     *             <li>temporal_minus_min</li>
     *
     * @return A new temporal object of the same subtype as "this".
     */
    public Temporal minus_min(){
        return Factory.create_temporal(functions.temporal_minus_min(this.inner),this.getCustomType(),this.getTemporalType());
    }

    /**
     * Returns a new temporal object containing the times "this" is not at
     *         its maximum value.
     *
     *  <p>
     *         MEOS Functions:
     *             <li>temporal_minus_max</li>
     *
     * @return A new temporal object of the same subtype as "this".
     */
    public Temporal minus_max(){
        return Factory.create_temporal(functions.temporal_minus_max(this.inner),this.getCustomType(),this.getTemporalType());
    }

    /* ------------------------- Topological Operations ------------------------ */


    /**
     * Returns whether the bounding box of "this" is adjacent to the bounding
     *         box of "other". Temporal subclasses may override this method to provide
     *         more specific behavior related to their types a check adjacency over
     *         more dimensions.
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_adjacent(Base)}
     * @param other A time or temporal object to compare to "this".
     * @return True if adjacent, False otherwise.
     */
    public boolean is_adjacent(TemporalObject other) throws Exception {
        return this.bounding_box().is_adjacent(other);
    }

    /**
     * Returns whether the bounding period of "this" is temporally adjacent
     *         to the bounding period of "other".
     *
     *   <p>
     *
     *         See Also:
     *             {@link Period#is_adjacent(TemporalObject)}
     * @param other A time or temporal object to compare to "this".
     * @return True if adjacent, False otherwise.
     */
    public boolean is_temporally_adjacent(TemporalObject other) throws Exception {
        return this.period().is_adjacent(other);
    }

    /**
     * Returns whether the bounding period of "this "is contained in the
     *         bounding period of "other". Temporal subclasses may override this
     *         method to provide more specific behavior related to their types
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#is_contained_in(TemporalObject)}
     * @param other A time or temporal object to compare to "this".
     * @return True if contained, False otherwise.
     */
    public boolean is_contained_in(TemporalObject other) throws Exception {
        return this.bounding_box().is_contained_in(other);
    }


    /**
     * Returns whether the bounding period of "this" is contained in the
     *         bounding period of "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_contained_in(TemporalObject)}
     * @param other A time or temporal object to compare to "this".
     * @return True if contained, False otherwise.
     */
    public boolean is_temporally_contained_in(TemporalObject other) throws Exception {
        return this.period().is_contained_in(other);
    }


    /**
     * Returns whether the bounding period of "this" contains the bounding
     *         period of "other". Temporal subclasses may override this method to
     *         provide more specific behavior related to their types
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#contains(TemporalObject)}
     * @param other A time or temporal object to compare to "this".
     * @return True if contains, False otherwise.
     */
    public boolean contains(TemporalObject other) throws Exception {
        return this.bounding_box().contains(other);
    }


    /**
     * Returns whether the bounding period of "this" contains the bounding
     *         period of "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#contains(TemporalObject)}
     * @param other A time or temporal object to compare to "this".
     * @return True if contains, False otherwise.
     */
    public boolean temporally_contains(TemporalObject other) throws Exception {
        return this.period().contains(other);
    }


    /**
     * Returns whether the bounding period of "this" overlaps the bounding
     *         period of "other". Temporal subclasses may override this method to
     *         provide more specific behavior related to their types
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#overlaps(TemporalObject)}
     * @param other A time or temporal object to compare to "this".
     * @return True if overlaps, False otherwise.
     */
    public boolean overlaps(TemporalObject other) throws Exception {
        return this.bounding_box().overlaps(other);
    }


    /**
     * Returns whether the bounding period of "this" overlaps the bounding
     *         period of "other".
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#overlaps(TemporalObject)}
     * @param other A time or temporal object to compare to "this".
     * @return True if overlaps, False otherwise.
     */
    public boolean temporally_overlaps(TemporalObject other) throws Exception {
        return this.period().overlaps(other);
    }


    /**
     * Returns whether the bounding period of "this" is the same as the
     *         bounding period of "other". Temporal subclasses may override this
     *         method to provide more specific behavior related to their types
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#is_same(TemporalObject)}
     * @param other A time or temporal object to compare to `self`.
     * @return True if same, False otherwise.
     */
    public boolean is_same(TemporalObject other) throws Exception {
        return this.bounding_box().is_same(other);
    }


    /* ------------------------- Position Operations --------------------------- */


    /**
     * Returns whether "this" is before "other".
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#is_before(TemporalObject)}
     * @param other A time or temporal object to compare "this" to.
     * @return True if "this" is before "other", False otherwise.
     */
    public boolean is_before(TemporalObject other) throws Exception {
        return this.period().is_before((Time)other);
    }

    /**
     * Returns whether "this" is before "other" allowing overlap. That is,
     *         "this" doesn't extend after "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_over_or_before(TemporalObject)}
     * @param other A time or temporal object to compare `self` to.
     * @return True if `self` is before `other` allowing overlap, False otherwise.
     */
    public boolean is_over_or_before(TemporalObject other) throws Exception {
        return this.period().is_over_or_before((Time)other);
    }

    /**
     * Returns whether "this" is after "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_after(TemporalObject)}
     * @param other A time or temporal object to compare "this" to.
     * @return True if "this" is after "other", False otherwise.
     */
    public boolean is_after(TemporalObject other) throws Exception {
        return this.period().is_after((Time)other);
    }


    /**
     * Returns whether "this" is after "other" allowing overlap. That is,
     *         "this" doesn't extend before "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_over_or_after(TemporalObject)}
     * @param other A time or temporal object to compare "this" to.
     * @return True if "this" is after "other" allowing overlap, False otherwise.
     */
    public boolean is_over_or_after(TemporalObject other) throws Exception {
        return this.period().is_over_or_after((Time)other);
    }



    /* ------------------------- Similarity Operations ------------------------- */

    /**
     * Returns the Frechet distance between "this" and "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_frechet_distance</li>
     * @param other A temporal object to compare to "this".
     * @return A {@link Float} with the Frechet distance.
     */
    public float frechet_distance(Temporal other){
        return (float) functions.temporal_frechet_distance(this.inner,other.getInner());
    }

    /**
     * Returns the Dynamic Time Warp distance between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>temporal_dyntimewarp_distance</li>
     *
     * @param other A temporal object to compare to "this".
     * @return A {@link Float} with the Dynamic Time Warp distance.
     */
    public float dyntimewarp_distance(Temporal other){
        return (float) functions.temporal_dyntimewarp_distance(this.inner,other.getInner());
    }

    /**
     * Returns the Hausdorff distance between "this" and "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_hausdorff_distance</li>
     *
     * @param other A temporal object to compare to "this".
     * @return A {@link Float} with the Hausdorff distance.
     */
    public float hausdorff_distance(Temporal other){
        return (float) functions.temporal_hausdorff_distance(this.inner,other.getInner());
    }


    /**
     * Return the simplified trip using Douglas-Peucker algorithm
     * @param temp Pointer object
     * @param dist distance
     * @param sync synchronization
     * @return a new Pointer object
     */
    public Pointer temporal_simplify_dp(Pointer temp, double dist, boolean sync){
        return functions.temporal_simplify_dp(temp,dist,sync);
    }


    /* ------------------------- Comparisons ----------------------------------- */


    /**
     * Returns whether "this" is equal to "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             temporal_eq
     * @param other A temporal object to compare to "this".
     * @return A {@link Boolean} with the result of the equality relation.
     */
    public boolean eq(Temporal other){
        return functions.temporal_eq(this.inner,other.getInner());
    }

    /**
     * Returns whether "this" is not equal to "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>temporal_ne</li>
     * @param other A temporal object to compare to "this".
     * @return A {@link Boolean} with the result of the not equal relation.
     */
    public boolean notEquals(Temporal other){
        return functions.temporal_ne(this.inner,other.getInner());
    }

    /**
     * Returns whether "this" is less than "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_lt</li>
     * @param other A temporal object to compare to "this".
     * @return A {@link Boolean} with the result of the less than relation.
     */
    public boolean lessThan(Temporal other){
        return functions.temporal_lt(this.inner,other.getInner());
    }


    /**
     * Returns whether "this" is less or equal than "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>temporal_le</li>
     * @param other A temporal object to compare to "this".
     * @return A {@link Boolean} with the result of the less or equal than relation.
     */
    public boolean lessThanOrEqual(Temporal other){
        return functions.temporal_le(this.inner,other.getInner());
    }


    /**
     * Returns whether "this" is greater than "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>temporal_gt</li>
     * @param other A temporal object to compare to "this".
     * @return A {@link Boolean} with the result of the greater than relation.
     */
    public boolean greaterThan(Temporal other){
        return functions.temporal_gt(this.inner,other.getInner());
    }


    /**
     * Returns whether "this" is greater or equal than "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>temporal_ge</li>
     * @param other A temporal object to compare to "this".
     * @return A {@link Boolean} with the result of the greater or equal than
     *      *             relation.
     */
    public boolean greaterThanOrEqual(Temporal other){
        return functions.temporal_ge(this.inner,other.getInner());
    }


}
