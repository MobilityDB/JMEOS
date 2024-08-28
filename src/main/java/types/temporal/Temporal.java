package types.temporal;

import functions.functions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import types.TemporalObject;
import types.collections.base.Base;
import types.collections.time.Time;
import types.collections.time.tstzset;
import types.collections.time.tstzspan;
import types.collections.time.tstzspanset;
import utils.ConversionUtils;

import java.awt.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;
import java.util.List;

import static types.temporal.TemporalType.*;

/**
 * Abstract class for Temporal sub types
 * @param <V> - Base type of the temporal data type eg. Integer, Boolean
 *
 * @author ARIJIT SAMAL
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


    public static Temporal _factory(Pointer inner, String customType, TemporalType temporalType){
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
        Returns a temporal object from WKB bytes.

        Args:
            wkb: The WKB string.

        Returns:
            A temporal object from WKB bytes.

        MEOS Functions:
            temporal_from_wkb
*/

    public Temporal from_wkb(Pointer wkb, long size){
        Pointer result= functions.temporal_from_wkb(wkb, size);
        return Factory.create_temporal(result, this.getCustomType(), this.getTemporalType());
    }

/**
        Returns the temporal object as a hex-encoded WKB string.

        Returns:
            The temporal object as a hex-encoded WKB string.

        MEOS Functions:
            temporal_as_hexwkb
*/
    public String as_hexwkb(Pointer wkb, long size){
        String[] result= new String[]{functions.temporal_as_hexwkb(this.inner, (byte) -1)};
//        System.out.println(result[0]);
        return result[0];
    }

/**
        Returns the temporal object as a hex-encoded WKB string.

        Returns:
            The temporal object as a hex-encoded WKB string.

        MEOS Functions:
            temporal_as_wkb
*/
    public Pointer as_wkb(){
        Pointer result= functions.temporal_as_wkb(this.inner, (byte) 4);
        return result;
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
//    public Temporal from_mfjson(String str){
//        Pointer result = functions.temporal_as_(str);
//        return Factory.create_temporal(result, this.getCustomType(), this.getTemporalType());
//    }



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

    /**
            Returns a temporal object that is the result of merging the given
            temporal objects.

            Args:
                *temporals: The temporal objects to merge.

            Returns:
                A temporal object that is the result of merging the given temporal
                objects.

            MEOS Functions:
                temporal_merge_array
    */
    public Temporal from_merge(Pointer temporals){
        List<Temporal> temporal_list = new ArrayList<>();
        for(int i=0; i<this.num_instants(); i++){
            Pointer p = temporals.getPointer((long) i *Long.BYTES);
            if (p!=null){
                Temporal t = Factory.create_temporal(p, this.getCustomType(), this.getTemporalType());
                temporal_list.add(t);
            }
        }
        int length_list= temporal_list.size();
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer temporalP = Memory.allocate(Runtime.getRuntime(runtime), Long.BYTES);
        // Copy the array elements into the allocated memory
        for (int i = 0; i < length_list; i++) {
            temporalP.putPointer((long) i * Long.BYTES, temporal_list.get(i).getInner());
        }
        Pointer result= functions.temporal_merge_array(temporalP, length_list);
        return Factory.create_temporal(result, this.getCustomType(), this.getTemporalType());
    }


    /**
            Returns a temporal object that is the result of merging the given
            temporal objects.

            Args:
                temporals: The temporal objects to merge.

            Returns:
                A temporal object that is the result of merging the given temporal
                objects.
    */
    public Temporal from_merge_array(List<Temporal> temporals) {
        List<Temporal> temporal_list = new ArrayList<>();
        for (int i = 0; i < temporals.size(); i++) {
            Pointer p = temporals.get(i).getInner();
            if (p != null) {
                Temporal t = Factory.create_temporal(p, this.getCustomType(), this.getTemporalType());
                temporal_list.add(t);
            }
        }
        int length = temporal_list.size();

        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer temporalPointer = Memory.allocate(Runtime.getRuntime(runtime), Long.BYTES);
        // Copy the array elements into the allocated memory
        for (int i = 0; i < length; i++) {
            temporalPointer.putPointer((long) i * Long.BYTES, temporal_list.get(i).getInner());
        }
        Pointer result = functions.temporal_merge_array(temporalPointer, length);
        return Factory.create_temporal(result, this.getCustomType(), this.getTemporalType());
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
    public tstzspan bounding_box(){
        return new tstzspan(functions.temporal_to_tstzspan(this.inner));
    }

    /**
     * Returns the {@link tstzspanset} on which `self` is defined.
     *
     *         MEOS Functions:
     *             temporal_time
     * @return the {@link tstzspanset}  on which `self` is defined.
     */
    public tstzspanset time(){
        return new tstzspanset(functions.temporal_time(this.inner));
    }



    public TInterpolation interpolation(){
        return TInterpolation.fromString(functions.temporal_interp(this.inner),true);
    }


    /**
     * Returns the {@link tstzset} on which "this" is defined ignoring
     *         potential time gaps.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_period</li>
     * @return
     */
    public tstzspan period(){
        return this.timespan();
    }

    /**
     * Returns the {@link tstzspan} on which "this" is defined ignoring
     *         potential time gaps.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_to_period</li>
     * @return
     */
    public tstzspan timespan(){
        return new tstzspan(functions.temporal_to_tstzspan(this.inner));
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

//    public List<Temporal> instants(){
//        functions.temporal_instants(this.inner);
//    }

//    public abstract Temporal value_at_timestamp();

    public Duration duration(boolean ignore_gaps){
        ignore_gaps = false;
        return ConversionUtils.interval_to_timedelta(functions.temporal_duration(this.inner, ignore_gaps));
    }

    public tstzspan tstzspan(){
        return this.timespan();
    }

    /**
     * Returns the number of timestamps in "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_num_timestamps</li>
     * @return Returns the number of timestamps in "this".
     */
    public int num_timestamps(){
//        System.out.println("ici");
//        System.out.println(this.toString());
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
        return ConversionUtils.timestamptz_to_datetime(functions.temporal_start_timestamptz(this.inner));
    }


    /**
     * Returns the last timestamp in "this".
     * <p>
     *         MEOS Functions:
     *             <li>temporal_end_timestamps</li>
     * @return Returns the last timestamp in "this".
     */
    public LocalDateTime end_timestamp(){
        return ConversionUtils.timestamptz_to_datetime(functions.temporal_end_timestamptz(this.inner));
    }

    // Convert timestamp (number of seconds since epoch) to LocalDateTime
    public static LocalDateTime timestampToLocalDateTime(int timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }

    public LocalDateTime timestamp_n(int n){
        return timestampToLocalDateTime(Objects.requireNonNull(functions.temporal_timestamptz_n(this.inner, n + 1)).getInt(Integer.BYTES));
    }

/*
        Returns the timestamps in `self`.

        MEOS Functions:
            temporal_timestamps
*/

    public List<LocalDateTime> timestamps(){
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
        Pointer array= functions.temporal_timestamps(this.inner, intPointer);
        List<LocalDateTime> datetimeList= new ArrayList<LocalDateTime>();
        for(int i=0;i<this.num_timestamps(); i++){
            int p= array.getInt((long) i *Integer.BYTES);
            LocalDateTime ldt= timestampToLocalDateTime(p);
            datetimeList.add(ldt);
        }
        return datetimeList;
    }


    /*
            Returns the instants in `self`.

            MEOS Functions:
                temporal_instants
    */
    public List<Temporal> instants(){
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
        Pointer array= functions.temporal_instants(this.inner, intPointer);
        List<Temporal> instantList= new ArrayList<Temporal>();
        for(int i=0; i<this.num_instants(); i++){
            Pointer p= array.getPointer((long) i *Integer.BYTES);
            Temporal t= Factory.create_temporal(p, this.getCustomType(),this.getTemporalType());
            instantList.add(t);
        }
        return instantList;
    }

    /*
            Returns the list of values taken by `self`.
    */
    public List<Temporal> values(){
        List<Temporal> temporalList = new ArrayList<>();
        temporalList= this.instants();
        return temporalList;
    }

    /*
            Returns the temporal segments in `self`.

            MEOS Functions:
                temporal_segments
    */
    public List<Temporal> segments(){
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
        Pointer array= functions.temporal_segments(this.inner, intPointer);
        List<Temporal> segmentList= new ArrayList<Temporal>();
        int num_segments= functions.temporal_num_sequences(this.inner);
        for(int i=0;i<num_segments; i++){
            Pointer p= array.getPointer((long) i *Long.BYTES);
            Temporal t= Factory.create_temporal(p, this.getCustomType(),this.getTemporalType());
            segmentList.add(t);
        }
        return segmentList;
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

    /*
        Returns a new :class:`Temporal` with the temporal dimension shifted by
        ``delta``.

        Args:
            delta: :class:`datetime.timedelta` instance to shift

        MEOS Functions:
            temporal_shift_time
*/

    public Temporal shift_time(Duration duration){
        Pointer shifted= functions.temporal_shift_time(this.inner, ConversionUtils.timedelta_to_interval(duration));
        return Factory.create_temporal(shifted,this.getCustomType(),this.getTemporalType());
    }

    /*
            Returns a new :class:`Temporal` scaled so the temporal dimension has
            duration ``duration``.

            Args:
                duration: :class:`datetime.timedelta` instance representing the
                duration of the new temporal

            MEOS Functions:
                temporal_scale_time
    */
    public Temporal scale_time(Duration duration){
        Pointer scaled= functions.temporal_scale_time(this.inner, ConversionUtils.timedelta_to_interval(duration));
        return Factory.create_temporal(scaled,this.getCustomType(),this.getTemporalType());
    }

    /*
            Returns a new :class:`Temporal` with the time dimension shifted by
            ``shift`` and scaled so the temporal dimension has duration
            ``duration``.

            Args:
                shift: :class:`datetime.timedelta` instance to shift
                duration: :class:`datetime.timedelta` instance representing the
                duration of the new temporal

            MEOS Functions:
                temporal_shift_scale_time
    */
    public Temporal shift_scale_time(Duration shift, Duration scale){
        Pointer scaled= functions.temporal_shift_scale_time(this.inner, ConversionUtils.timedelta_to_interval(shift), ConversionUtils.timedelta_to_interval(scale));
        return Factory.create_temporal(scaled,this.getCustomType(),this.getTemporalType());
    }

    /*
            Returns a new :class:`Temporal` downsampled with respect to ``duration``.

            Args:
                duration: A :class:`str` or :class:`timedelta` with the duration of
                    the temporal tiles.
                start: A :class:`str` or :class:`datetime` with the start time of
                    the temporal tiles. If None, the start time used by default is
                    Monday, January 3, 2000.
                interpolation: Interpolation of the resulting temporal object. If None,
                    defaults to the interpolation of ``self``.
            MEOS Functions:
                temporal_tsample
    */
    public Temporal temporal_sample(Object duration, Object start, TInterpolation interpolation){
        OffsetDateTime st= null;
        Pointer dt= null;
        TInterpolation intrp= null;
        if (start == null){
            st= functions.pg_timestamptz_in("2000-01-03", -1);
        }
        else if (start instanceof LocalDateTime){
            st= ConversionUtils.datetimeToTimestampTz((LocalDateTime)start);
        }
        else{
            st= functions.pg_timestamptz_in(start.toString(), -1);
        }

        if(duration instanceof Duration){
            dt= ConversionUtils.timedelta_to_interval((Duration) duration);
        }
        else{
            dt= functions.pg_interval_in(duration.toString(), -1);
        }

        if(interpolation == null){
            intrp= this.interpolation();
        }
        else{
            intrp= interpolation;
        }
        int intrp_val= intrp.getValue();
        Pointer result= functions.temporal_tsample(this.inner, dt, st, intrp_val);
        return Factory.create_temporal(result, this.getCustomType(), this.getTemporalType());
    }

    /*
            Returns a new :class:`Temporal` with precision reduced to ``duration``.

            Args:
                duration: A :class:`str` or :class:`timedelta` with the duration
                    of the temporal tiles.
                start: A :class:`str` or :class:`datetime` with the start time of
                    the temporal tiles. If None, the start time used by default is
                    Monday, January 3, 2000.

            MEOS Functions:
                temporal_tprecision
    */
    public Temporal temporal_precision(Object duration, Object start){
        OffsetDateTime st= null;
        Pointer dt= null;
        if (start == null){
            st= functions.pg_timestamptz_in("2000-01-03", -1);
        }
        else if (start instanceof LocalDateTime){
            st= ConversionUtils.datetimeToTimestampTz((LocalDateTime)start);
        }
        else{
            st= functions.pg_timestamptz_in(start.toString(), -1);
        }

        if(duration instanceof Duration){
            dt= ConversionUtils.timedelta_to_interval((Duration) duration);
        }
        else{
            dt= functions.pg_interval_in(duration.toString(), -1);
        }
        Pointer result= functions.temporal_tprecision(this.inner, dt, st);
        return Factory.create_temporal(result, this.getCustomType(), this.getTemporalType());
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
        System.out.println(interpolation.toString());
        return Factory.create_temporal(functions.temporal_to_tsequence(this.inner, interpolation.toString()),this.getCustomType(),TEMPORAL_SEQUENCE);
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
        return Factory.create_temporal(functions.temporal_to_tsequenceset(this.inner, interpolation.toString()),this.getCustomType(),TEMPORAL_SEQUENCE_SET);

    }

/*
        Returns `self` as a :class:`pd.DataFrame` with two columns: `time`
            and `value`.
*/
    public List<Map<String, Object>> toDataFrame() {
        List<Map<String, Object>> dataFrame = new ArrayList<>();
        for (int i = 0; i < this.timestamps().size(); i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("time", this.timestamps().get(i));
            row.put("value", this.values().get(i));
            dataFrame.add(row);
        }
        return dataFrame;
    }




    /* ------------------------- Modifications ---------------------------------------- */

/*
        Returns a new :class:`Temporal` object equal to `self` with `instant`
        appended.

        Args:
            instant: :class:`TInstant` to append
            max_dist: Maximum distance for defining a gap
            max_time: Maximum time for defining a gap

        MEOS Functions:
            temporal_append_tinstant
*/

    public Temporal append_instant(TInstant instant, float max_dist, Duration max_time){
        Pointer interv= null;
        if (max_time==null){
            interv=null;
        }
        else{
            interv= ConversionUtils.timedelta_to_interval(max_time);
        }
        Pointer resultPointer= functions.temporal_append_tinstant(this.inner, instant.getInner(), (double) max_dist, interv, false);
        return Factory.create_temporal(resultPointer, this.getCustomType(), this.getTemporalType());
    }

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


/* Utility fucntion to crearte a pointer array*/
    private Pointer createPointerArray(List<?> temporalList) throws Exception {
//        Pointer pointerArray = Runtime.memoryManager().allocateDirect((temporalList.size() + 1) * Pointer.SIZE);

        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer pointerArray = Memory.allocate(Runtime.getRuntime(runtime), (temporalList.size() + 1) * Long.BYTES);
        pointerArray.putPointer(0, this.inner); // Add the current instance's inner pointer

        for (int i = 0; i < temporalList.size(); i++) {
            Object item = temporalList.get(i);
            if (item instanceof Temporal) {
                pointerArray.putPointer((long) (i + 1) * Long.SIZE, ((Temporal<?>) item).inner);
            } else {
                throw new Exception("List contains an unsupported type.");
            }
        }
        return pointerArray;
    }

    /*
            Returns a new :class:`Temporal` object that is the result of merging
            `self` with `other`.

            MEOS Functions:
                temporal_merge, temporal_merge_array
    */
    public Temporal merge(Object other) throws Exception {
        Pointer newTemp;
        if (other == null) {
            return this;
        }
        else if (other instanceof Temporal) {
            Temporal<?> temporalOther = (Temporal<?>) other;
            newTemp = functions.temporal_merge(this.inner, temporalOther.inner);
        }
        else if (other instanceof List<?>) {
            List<?> otherList = (List<?>) other;
            Pointer pointers = createPointerArray(otherList);

            newTemp = functions.temporal_merge_array(pointers, otherList.size() + 1);
        } else {
            throw new Exception("Operation not supported with type " + other.getClass().getName());
        }
        return Factory.create_temporal(newTemp, this.getCustomType(), this.getTemporalType());
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

    /*
        Returns a new :class:`Temporal` object equal to `self` with elements at
        `other` removed.

        Args:
            other: :class:`Time` object to remove from `self`
            connect: whether to connect the potential gaps generated by the
                deletions.

        MEOS Functions:
            temporal_update
*/
    public Temporal delete(Object other, Boolean connect) throws Exception {
        Pointer new_inner=null;
        if(other instanceof LocalDateTime){
            new_inner= functions.temporal_delete_timestamptz(this.inner, ConversionUtils.datetimeToTimestampTz((LocalDateTime) other), connect);
        }
        else if(other instanceof tstzset){
            new_inner= functions.temporal_delete_tstzset(this.inner, ((tstzset) other).get_inner(), connect);
        }
        else if(other instanceof tstzspan){
            new_inner= functions.temporal_delete_tstzspan(this.inner, ((tstzspan) other).get_inner(), connect);
        }
        else if (other instanceof tstzspanset){
            new_inner= functions.temporal_delete_tstzspanset(this.inner, ((tstzspanset) other).get_inner(), connect);
        }
        else{
            throw new Exception("Operation not supported with type " + other.getClass().getName());
        }
        if(new_inner==this.inner){
            return this;
        }
        return Factory.create_temporal(new_inner, this.getCustomType(), this.getTemporalType());
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
        if (other instanceof tstzset){
            result = functions.temporal_at_tstzset(this.inner,((tstzset) other).get_inner());

        } else if (other instanceof tstzspan) {
            result = functions.temporal_at_tstzspan(this.inner,((tstzspan) other).get_inner());

        } else if (other instanceof tstzspanset) {
            result = functions.temporal_at_tstzspanset(this.inner,((tstzspanset) other).get_inner());
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
        if (other instanceof tstzset){
            result = functions.temporal_minus_tstzset(this.inner,((tstzset) other).get_inner());

        } else if (other instanceof tstzspan) {
            result = functions.temporal_minus_tstzspan(this.inner,((tstzspan) other).get_inner());

        } else if (other instanceof tstzspanset) {
            result = functions.temporal_minus_tstzspanset(this.inner,((tstzspanset) other).get_inner());
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
     *             {@link tstzset#is_adjacent(Base)}
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
     *             {@link tstzset#is_adjacent(TemporalObject)}
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
     *             {@link tstzset#is_contained_in(TemporalObject)}
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
     *             {@link tstzset#is_contained_in(TemporalObject)}
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
     *             {@link tstzset#contains(TemporalObject)}
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
     *             {@link tstzset#contains(TemporalObject)}
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
     *             {@link tstzset#overlaps(TemporalObject)}
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
     *             {@link tstzset#overlaps(TemporalObject)}
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
     *             {@link tstzset#is_same(Time)} (TemporalObject)}
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
     *             {@link tstzset#is_before(TemporalObject)}
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
     *             {@link tstzset#is_over_or_before(TemporalObject)}
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
     *             {@link tstzset#is_after(TemporalObject)}
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
     *             {@link tstzset#is_over_or_after(TemporalObject)}
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

    /* ------------------------- Split Operations ----------------------------------- */

/*
        Returns a list of temporal objects of the same subtype as `self` with
        the same values as `self` but split in temporal tiles of duration
        `duration` starting at `start`.

        Args:
            duration: A :class:`str` or :class:`timedelta` with the duration
                of the temporal tiles.
            start: A :class:`str` or :class:`datetime` with the start time of
                the temporal tiles. If None, the start time used by default is
                Monday, January 3, 2000.

        Returns:
            A list of temporal objects of the same subtype as `self`.

        MEOS Functions:
            temporal_time_split
*/

    private Pointer createEmptyPointerArray(Runtime runtime) {
        // Allocate memory for a list of integers (let's assume a fixed size, e.g., 10 elements)
        Pointer listPointer = Memory.allocate(Runtime.getRuntime(runtime), this.num_instants()*Long.BYTES); // Adjust size as needed
        return listPointer;
    }

    public List<Temporal> time_split(Object duration, Object start){
        OffsetDateTime st= null;
        Pointer dt= null;
        if(start == null){
            st= functions.pg_timestamptz_in("2000-01-03", -1);
        }
        else{
            if(start instanceof LocalDateTime){
                st= ConversionUtils.datetimeToTimestampTz((LocalDateTime) start);
            }
            else{
                st= functions.pg_timestamptz_in(start.toString(), -1);
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
        Pointer listPointer = createEmptyPointerArray(runtime);
        Pointer p= functions.temporal_time_split(this.inner, dt, st, listPointer, intPointer);
        List<Temporal> tempList= new ArrayList<>();
        int count= intPointer.getInt(Integer.BYTES);
        for(int i=0;i<count;i++){
            Pointer res= p.getPointer((long) i *Long.BYTES);
            Temporal t= Factory.create_temporal(res, this.getCustomType(), this.getTemporalType());
            tempList.add(t);
        }
        return tempList;
    }

    /*
            Returns a list of temporal objects of the same subtype as `self` with
            the same values as `self` but split in n temporal tiles of equal
            duration.

            Args:
                n: An :class:`int` with the number of temporal tiles.

            Returns:
                A list of temporal objects of the same subtype as `self`.

            MEOS Functions:
                temporal_time_split
    */
    public static long convertToTimestamp(LocalDateTime dateTime) {
        // Converts LocalDateTime to a timestamp (seconds since the Unix epoch)
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }
    public static Duration calculateDifference(LocalDateTime start, LocalDateTime end) {
        // Calculate the duration between two LocalDateTime objects
        return Duration.between(start, end);
    }

    public static Duration calculateIntermediateDuration(LocalDateTime start, LocalDateTime end, int n) {
        // Convert LocalDateTime to epoch seconds (timestamp)
        long startTimestamp = start.toEpochSecond(ZoneOffset.UTC);
        long endTimestamp = end.toEpochSecond(ZoneOffset.UTC);

        // Calculate the difference in seconds
        long timestampDifference = endTimestamp - startTimestamp;

        // Divide the difference by the given integer n
        long dividedTimestampDifference = timestampDifference / n;

        // Convert the divided difference back to a Duration
        return Duration.ofSeconds(dividedTimestampDifference);
    }

    public List<Temporal> time_split_n(int n){
        OffsetDateTime st= null;
        Pointer dt= null;
        if(this.start_timestamp() == this.end_timestamp()){
            return Collections.singletonList(this);
        }
        st= functions.temporal_start_timestamptz(this.inner);
        LocalDateTime start= this.start_timestamp();
        LocalDateTime end= this.end_timestamp();
        Duration dur= calculateIntermediateDuration(start, end, n);
        dt = ConversionUtils.timedelta_to_interval(dur);
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
        Pointer listPointer = createEmptyPointerArray(runtime);
        Pointer p= functions.temporal_time_split(this.inner, dt, st, listPointer, intPointer);
        List<Temporal> tempList= new ArrayList<>();
        int count= intPointer.getInt(Integer.BYTES);
        for(int i=0;i<count;i++){
            Pointer res= p.getPointer((long) i *Long.BYTES);
            Temporal t= Factory.create_temporal(res, this.getCustomType(), this.getTemporalType());
            tempList.add(t);
        }
        return tempList;
    }

/*
        Return the subsequences where the objects stay within an area with a
        given maximum size for at least the specified duration.

        Args:
            max_distance: A :class:`float` with the maximum distance of a stop.
            min_duration: A :class:`timedelta` with the minimum duration of
                a stop.

        Returns:
            A :class:`SequenceSet` of the same subtype as `self` with the stops.

        MEOS Functions:
            temporal_stops
*/


    public Temporal stops(double max_distance, Duration max_duration){
        Pointer new_inner= null;
        new_inner= functions.temporal_stops(this.inner, max_distance, ConversionUtils.timedelta_to_interval(max_duration));
        return Factory.create_temporal(new_inner, this.getCustomType(), this.getTemporalType());
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
