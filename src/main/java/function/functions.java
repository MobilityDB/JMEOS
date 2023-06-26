package function;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Struct;
import types.temporal.Temporal;
import types.temporal.TemporalValue;
import types.time.TimestampSet;

import java.time.OffsetDateTime;

public class functions {
    public interface MeosLibrary{
        functions.MeosLibrary INSTANCE = LibraryLoader.create(functions.MeosLibrary.class).load("meos");
        functions.MeosLibrary meos = functions.MeosLibrary.INSTANCE;
        Pointer timestamp_to_period(Integer t);
        Pointer tgeompoint_in(String str);
        Pointer tpoint_out(Pointer temp, int maxdd);
        Pointer tint_in(String string);
        Pointer temporal_merge(Pointer temp1, Pointer temp2);
        Pointer temporal_merge_array(Pointer[] temparr, int count);
        Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc);
        Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc);
        String span_out(Pointer s, int arg);
        Pointer tgeogpoint_in(String string);
        int int_bucket(int value, int size, int origin);
        double float_bucket(float value, float size, float origin);
        double distance_timestamp_timestamp(int t1, int t2);
        String temporal_as_mfjson(Pointer temp, boolean bool, int flags, int precision, String srs);
        Integer pg_timestamp_in(String str, Integer typmod);
        String pg_timestamp_out(String dt);
        String tpoint_as_text(Pointer temp, Integer maxdd);
        Pointer tfloatinst_make(float d, Integer t);
        String tfloat_out(Pointer temp, Integer maxdd);

        void  meos_initialize(byte[] str);
        void  meos_finalize();
    }

    public static Pointer timestamp_to_period(int t){
        return MeosLibrary.meos.timestamp_to_period(t);
    };
    public static Pointer tgeompoint_in(String str){
        return MeosLibrary.meos.tgeompoint_in(str);
    };
    public static Pointer tpoint_out(Pointer temp, int maxdd){
        return MeosLibrary.meos.tpoint_out(temp,maxdd);
    };
    public static Pointer tint_in(String string){
        return MeosLibrary.meos.tint_in(string);
    };
    public static Pointer temporal_merge(Pointer temp1, Pointer temp2){
        return MeosLibrary.meos.temporal_merge(temp1,temp2);
    };
    public static Pointer temporal_merge_array(Pointer[] temparr, int count){
        return MeosLibrary.meos.temporal_merge_array(temparr,count);
    };
    public static Pointer floatspan_make(double lower, double upper, boolean lower_inc, boolean upper_inc){
        return MeosLibrary.meos.floatspan_make(lower,upper,lower_inc,upper_inc);
    };
    public static Pointer intspan_make(int lower, int upper, boolean lower_inc, boolean upper_inc){
        return MeosLibrary.meos.intspan_make(lower,upper,lower_inc,upper_inc);
    };
    public static String span_out(Pointer s, int arg){
        return MeosLibrary.meos.span_out(s,arg);
    };
    public static Pointer tgeogpoint_in(String string){
        return MeosLibrary.meos.tgeogpoint_in(string);
    };
    public static int int_bucket(int value, int size, int origin){
        return MeosLibrary.meos.int_bucket(value,size,origin);
    };
    public static double float_bucket(float value, float size, float origin){
        return MeosLibrary.meos.float_bucket(value,size,origin);
    };
    public static double distance_timestamp_timestamp(int t1, int t2){
        return MeosLibrary.meos.distance_timestamp_timestamp(t1,t2);
    };
    public static void  meos_initialize(byte[] str){
        MeosLibrary.meos.meos_initialize(str);
    };
    public static void meos_finalize(){
        MeosLibrary.meos.meos_finalize();
    };

    public static String temporal_as_mfjson(Pointer temp, boolean bool, int flags, int precision, String srs){
        return MeosLibrary.meos.temporal_as_mfjson(temp,bool,flags,precision,srs);
    };


    public static Integer pg_timestamp_in(String str, Integer typmod){

        return MeosLibrary.meos.pg_timestamp_in(str,typmod);
    }

    public static String pg_timestamp_out(String dt){
        return MeosLibrary.meos.pg_timestamp_out(dt);
    }
    public static String tpoint_as_text(Pointer temp, Integer maxdd){
        return MeosLibrary.meos.tpoint_as_text(temp,maxdd);
    }

    public static Pointer tfloatinst_make(float d, Integer t){
        return MeosLibrary.meos.tfloatinst_make(d,t);
    }

    public static String tfloat_out(Pointer temp, Integer maxdd){
        return MeosLibrary.meos.tfloat_out(temp,maxdd);
    }

}
