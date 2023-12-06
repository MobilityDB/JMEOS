package types.temporal;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.collections.base.Base;
import types.collections.time.Period;
import types.collections.time.PeriodSet;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import functions.functions;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import utils.ConversionUtils;

/**
 * Abstract class for Temporal sub types
 * @param <V> - Base type of the temporal data type eg. Integer, Boolean
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
    // To delete !
    public Temporal(String customType, TemporalType temporalType) {
        //this.customType = customType;
        //this.temporalType = temporalType;
    }

    public abstract Pointer createStringInner(String str);
    public abstract Pointer createInner(Pointer inner);
    public abstract String getCustomType();
    public abstract TemporalType getTemporalType();

    protected void validate() throws SQLException {
        validateTemporalDataType();
    }


    public Temporal _factory(Pointer inner, String customType, TemporalType temporalType){
        return Factory.create_temporal(inner, customType, temporalType);
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



    /** ------------------------- Output ---------------------------------------- */




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
     * @throws SQLException
     */
    public Period bounding_box() throws SQLException {
        return new Period(functions.temporal_to_period(this.inner));
    }

    /**
     * Returns the {@link PeriodSet} on which `self` is defined.
     *
     *         MEOS Functions:
     *             temporal_time
     * @return the {@link PeriodSet}  on which `self` is defined.
     * @throws SQLException
     */
    public PeriodSet time() throws SQLException {
        return new PeriodSet(functions.temporal_time(this.inner));
    }

    /**
     * Returns the {@link Period} on which "this" is defined ignoring
     *         potential time gaps.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_period</li>
     * @return
     * @throws SQLException
     */
    public Period period() throws SQLException {
        return this.timespan();
    }

    /**
     * Returns the {@link Period} on which "this" is defined ignoring
     *         potential time gaps.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_period</li>
     * @return
     * @throws SQLException
     */
    public Period timespan() throws SQLException {
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
        return Factory.create_temporal(functions.temporal_start_instant(this.inner), this.getCustomType(), this.getTemporalType());
    }

    /**
     * Returns the last instant in "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_end_instant</li>
     * @return Returns the last instant in "this".
     */
    public Temporal end_instant(){
        return Factory.create_temporal(functions.temporal_end_instant(this.inner), this.getCustomType(), this.getTemporalType());
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
        return Factory.create_temporal(functions.temporal_min_instant(this.inner), this.getCustomType(), this.getTemporalType());
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
        return Factory.create_temporal(functions.temporal_max_instant(this.inner), this.getCustomType(), this.getTemporalType());
    }

    /**
     * Returns the number of timestamps in "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_num_timestamps</li>
     * @return Returns the number of timestamps in "this".
     */
    public int num_timestamps(){
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


    /** ------------------------- Transformations ---------------------------------------- */

    /**
     * Returns "this" as a {@link TInstant}.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_tinstant</li>
     * @return Returns "this" as a {@link TInstant}.
     */
    public Temporal to_instant(){
        return Factory.create_temporal(functions.temporal_to_tinstant(this.inner),this.getCustomType(),this.getTemporalType());
    }


    /** ------------------------- Modifications ---------------------------------------- */

    /*
    public Temporal append_sequence(TSequence sequence){
        return Factory.create_temporal(functions.temporal_append_tsequence(this.inner, sequence.inn),)
    }

     */


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
     * @param connect wether to connect the updated elements with the
     *      *                 existing elements.
     * @return Returns a new {@link Temporal} object equal to "this" updated with
     *      *         "other".
     */
    public Temporal update(Temporal other, boolean connect){
        return Factory.create_temporal(functions.temporal_update(this.inner,other.inner,connect),this.getCustomType(),this.getTemporalType());
    }


    /** ------------------------- Restrictions ---------------------------------- */


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

    /** ------------------------- Topological Operations ------------------------ */


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
     * @throws SQLException
     */
    public boolean is_adjacent(TemporalObject other) throws SQLException {
        return this.bounding_box().is_adjacent((Time) other);
    }

    /**
     * Returns whether the bounding period of "this" is temporally adjacent
     *         to the bounding period of "other".
     *
     *   <p>
     *
     *         See Also:
     *             {@link Period#is_adjacent(Time)}
     * @param other A time or temporal object to compare to "this".
     * @return True if adjacent, False otherwise.
     * @throws SQLException
     */
    public boolean is_temporally_adjacent(TemporalObject other) throws SQLException {
        return this.period().is_adjacent((Time) other);
    }

    /**
     * Returns whether the bounding period of "this "is contained in the
     *         bounding period of "other". Temporal subclasses may override this
     *         method to provide more specific behavior related to their types
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#is_contained_in(Time)}
     * @param other A time or temporal object to compare to "this".
     * @return True if contained, False otherwise.
     * @throws SQLException
     */
    public boolean is_contained_in(TemporalObject other) throws SQLException {
        return this.bounding_box().is_contained_in((Time) other);
    }


    /**
     * Returns whether the bounding period of "this" is contained in the
     *         bounding period of "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_contained_in(Time)}
     * @param other A time or temporal object to compare to "this".
     * @return True if contained, False otherwise.
     * @throws SQLException
     */
    public boolean is_temporally_contained_in(TemporalObject other) throws SQLException {
        return this.period().is_contained_in((Time) other);
    }


    /**
     * Returns whether the bounding period of "this" contains the bounding
     *         period of "other". Temporal subclasses may override this method to
     *         provide more specific behavior related to their types
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#contains(Time)}
     * @param other A time or temporal object to compare to "this".
     * @return True if contains, False otherwise.
     * @throws SQLException
     */
    public boolean contains(TemporalObject other) throws SQLException {
        return this.bounding_box().contains((Time) other);
    }


    /**
     * Returns whether the bounding period of "this" contains the bounding
     *         period of "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#contains(Time)}
     * @param other A time or temporal object to compare to "this".
     * @return True if contains, False otherwise.
     * @throws SQLException
     */
    public boolean temporally_contains(TemporalObject other) throws SQLException {
        return this.period().contains((Time) other);
    }


    /**
     * Returns whether the bounding period of "this" overlaps the bounding
     *         period of "other". Temporal subclasses may override this method to
     *         provide more specific behavior related to their types
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#overlaps(Time)}
     * @param other A time or temporal object to compare to "this".
     * @return True if overlaps, False otherwise.
     * @throws SQLException
     */
    public boolean overlaps(TemporalObject other) throws SQLException {
        return this.bounding_box().overlaps((Time) other);
    }


    /**
     * Returns whether the bounding period of "this" overlaps the bounding
     *         period of "other".
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#overlaps(Time)}
     * @param other A time or temporal object to compare to "this".
     * @return True if overlaps, False otherwise.
     * @throws SQLException
     */
    public boolean temporally_overlaps(TemporalObject other) throws SQLException {
        return this.period().overlaps((Time) other);
    }


    /**
     * Returns whether the bounding period of "this" is the same as the
     *         bounding period of "other". Temporal subclasses may override this
     *         method to provide more specific behavior related to their types
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#is_same(Time)}
     * @param other A time or temporal object to compare to `self`.
     * @return True if same, False otherwise.
     * @throws SQLException
     */
    public boolean is_same(TemporalObject other) throws SQLException {
        return this.bounding_box().is_same((Time) other);
    }


    /** ------------------------- Position Operations --------------------------- */


    /**
     * Returns whether "this" is before "other".
     *
     *  <p>
     *
     *         See Also:
     *             {@link Period#is_before(Time)}
     * @param other A time or temporal object to compare "this" to.
     * @return True if "this" is before "other", False otherwise.
     * @throws SQLException
     */
    public boolean is_before(TemporalObject other) throws SQLException {
        return this.period().is_before((Time)other);
    }

    /**
     * Returns whether "this" is before "other" allowing overlap. That is,
     *         "this" doesn't extend after "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_over_or_before(Time)}
     * @param other A time or temporal object to compare `self` to.
     * @return True if `self` is before `other` allowing overlap, False otherwise.
     * @throws SQLException
     */
    public boolean is_over_or_before(TemporalObject other) throws SQLException {
        return this.period().is_over_or_before((Time)other);
    }

    /**
     * Returns whether "this" is after "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_after(Time)}
     * @param other A time or temporal object to compare "this" to.
     * @return True if "this" is after "other", False otherwise.
     * @throws SQLException
     */
    public boolean is_after(TemporalObject other) throws SQLException {
        return this.period().is_after((Time)other);
    }


    /**
     * Returns whether "this" is after "other" allowing overlap. That is,
     *         "this" doesn't extend before "other".
     *
     * <p>
     *
     *         See Also:
     *             {@link Period#is_over_or_after(Time)}
     * @param other A time or temporal object to compare "this" to.
     * @return True if "this" is after "other" allowing overlap, False otherwise.
     * @throws SQLException
     */
    public boolean is_over_or_after(TemporalObject other) throws SQLException {
        return this.period().is_over_or_after((Time)other);
    }



    /** ------------------------- Similarity Operations ------------------------- */

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



    /** ------------------------- Comparisons ----------------------------------- */


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
    public boolean equals(Temporal other){
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

























    /**
     * Builds the temporal value. It can be overridden in child classes to change the behavior.
     * @param value data type value
     * @param time OffsetDateTime
     * @return temporal value wrapper
     */
    protected TemporalValue<V> buildTemporalValue(V value, OffsetDateTime time) {
        return new TemporalValue<>(value, time);
    }

    /**
     * It will be called before parsing the value, so the child classes can preprocess the value.
     * @param value - a string with the value
     * @return a string
     * @throws SQLException when the value is not valid, e.g when the value is not null or empty.
     */
    protected String preprocessValue(String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            throw new SQLException("Value cannot be empty.");
        }

        return value;
    }

    /**
     * It will be called on validate and it should throw SQLException for any validation errors.
     * @throws SQLException when the temporal data type is invalid
     */
    protected abstract void validateTemporalDataType() throws SQLException;

    /**
     * Parses the object to string representation in the form required by org.postgresql.
     * @return the value in string representation of this temporal sub type
     */
    public abstract String buildValue();

    /**
     * Gets all values
     * @return a list of values
     */
    public abstract List<V> getValues();

    /**
     * Gets the first value
     * @return the first value
     */
    public abstract V startValue();

    /**
     * Gets the last value
     * @return the last value
     */
    public abstract V endValue();

    /**
     * Gets the minimum value
     * @return min value
     */
    public abstract V minValue();

    /**
     * Gets the maximum value
     * @return max value
     */
    public abstract V maxValue();

    /**
     * Gets the value in the given timestamp
     * @param timestamp - the timestamp
     * @return value at the timestamp or null
     */
    public abstract V valueAtTimestamp(OffsetDateTime timestamp);

    /**
     * Get the number of timestamps
     * @return a number
     */
    public abstract int numTimestamps();

    /**
     * Get all timestamps
     * @return an array with the timestamps
     */
    public abstract OffsetDateTime[] timestamps();

    /**
     * Gets the timestamp located at the index position
     * @param n - the index
     * @return a timestamp
     * @throws SQLException if the index is out of range
     */
    public abstract OffsetDateTime timestampN(int n) throws SQLException;

    /**
     * Gets the first timestamp
     * @return a timestamp
     */
    public abstract OffsetDateTime startTimestamp();

    /**
     * Gets the last timestamp
     * @return a timestamp
     */
    public abstract OffsetDateTime endTimestamp();

    /**
     * Gets the periodset on which the temporal value is defined
     * @return a Periodset
     * @throws SQLException
     */
    public abstract PeriodSet getTime() throws SQLException;


    /**
     * Gets the number of instants
     * @return a number
     */
    public abstract int numInstants();

    /**
     * Gets the first instant
     * @return first temporal instant
     */
    public abstract TInstant<V> startInstant();

    /**
     * Gets the last instant
     * @return last temporal instant
     */
    public abstract TInstant<V> endInstant();

    /**
     * Gets the instant in the given index
     * @param n - the index
     * @return the temporal instant at n
     * @throws SQLException if the index is out of range
     */
    public abstract TInstant<V> instantN(int n) throws SQLException;

    /**
     * Gets all temporal instants
     * @return the list of all temporal instants
     */
    public abstract List<TInstant<V>> instants();

    /**
     * Gets the interval on which the temporal value is defined
     * @return a duration
     */
    public abstract Duration duration();


    /**
     * Shifts the duration sent
     * @param duration - the duration to shift
     */
    public abstract void shift(Duration duration);

    /**
     * If the temporal value intersects the timestamp sent
     * @param dateTime - the timestamp
     * @return true if the timestamp intersects, otherwise false
     */
    /*
    public abstract boolean intersectsTimestamp(OffsetDateTime dateTime);


     */
    /**
     * If the temporal value intersects the Period sent
     * @return true if the period intersects, otherwise false
     */
    /*
    public abstract boolean intersectsPeriod(Period period);


     */

    @Override
    public String toString() {
        return buildValue();
    }
}
