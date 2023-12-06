package types.basic.tbool;

import jnr.ffi.Pointer;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;
import types.temporal.*;
import functions.functions;

import java.sql.SQLException;


/**
 * Class that represents the MobilityDB type TBool
 */
@TypeName(name = "tbool")
public interface TBool {
    public String customType = "Boolean";
    public Pointer getInner();
	/** ------------------------- Constructors ---------------------------------- */


    public default TBool from_base_temporal(boolean value, Temporal base){
        return (TBool) Factory.create_temporal(functions.tbool_from_base_temp(value, base.getInner()),customType,base.getTemporalType());
    }


    public default Temporal from_base_time(boolean value, Time base){
        if (base instanceof TimestampSet){
            return new TBoolSeq(functions.tboolseq_from_base_timestampset(value,((TimestampSet) base).get_inner()));

        } else if (base instanceof Period) {
            return new TBoolSeq(functions.tboolseq_from_base_period(value,((Period) base).get_inner()));

        } else if (base instanceof PeriodSet) {
            return new TBoolSeqSet(functions.tboolseqset_from_base_periodset(value,((PeriodSet) base).get_inner()));
        }

        return null;
    }

    /*
    public default String tostring(){
        return functions.tbool_out();
    }

     */

    /** ------------------------- Output ---------------------------------- */

    public default String toString(Pointer inner){
        return functions.tbool_out(inner);
    }


    /** ------------------------- Accessors ---------------------------------- */




	
}