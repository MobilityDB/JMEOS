/*****************************************************************************
 *
 * This MobilityDB code is provided under The PostgreSQL License.
 * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB
 * contributors
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without a written
 * agreement is hereby granted, provided that the above copyright notice and
 * this paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON
 * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *****************************************************************************/

package types.temporal;
import functions.GeneratedFunctions;
import functions.MeosType;
import jnr.ffi.Pointer;
import types.basic.tbool.TBoolInst;
import types.basic.tbool.TBoolSeq;
import types.basic.tbool.TBoolSeqSet;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.basic.tint.TIntInst;
import types.basic.tint.TIntSeq;
import types.basic.tint.TIntSeqSet;
import types.basic.tpoint.tgeog.TGeogPointInst;
import types.basic.tpoint.tgeog.TGeogPointSeq;
import types.basic.tpoint.tgeog.TGeogPointSeqSet;
import types.basic.tpoint.tgeom.TGeomPointInst;
import types.basic.tpoint.tgeom.TGeomPointSeq;
import types.basic.tpoint.tgeom.TGeomPointSeqSet;
import types.basic.ttext.TTextInst;
import types.basic.ttext.TTextSeq;
import types.basic.ttext.TTextSeqSet;

import static types.temporal.TemporalType.*;

/**
 * Final class for assigning temporal types
 * @author ARIJIT SAMAL
 **/

public final class Factory{

    private Factory(){
    }

    /**
     * The byte offset of the temporal type in a MEOS Temporal, after its int32 varlena header,
     * as the catalog's structure Temporal states it (temptype at bit 32).
     */
    private static final long TEMPTYPE_OFFSET = 4;

    /**
     * Returns the object of the class of a temporal value MEOS returns, chosen by the temporal
     * type and the subtype the value states, so a reader needs no value of that class.
     * <p>
     *         MEOS Functions:
     *             <li>temporal_subtype</li>
     *
     * @param inner The temporal value, or null
     * @return The object of the class of the value, or null for a null pointer
     * @throws IllegalArgumentException for a temporal type JMEOS has no class of
     */
    public static Temporal create_temporal(Pointer inner){
        if (inner == null)
            return null;
        int temptype = inner.getByte(TEMPTYPE_OFFSET) & 0xFF;
        String customType;
        if (temptype == MeosType.T_TBOOL.getValue()) customType = "Boolean";
        else if (temptype == MeosType.T_TINT.getValue()) customType = "Integer";
        else if (temptype == MeosType.T_TFLOAT.getValue()) customType = "Float";
        else if (temptype == MeosType.T_TTEXT.getValue()) customType = "String";
        else if (temptype == MeosType.T_TGEOMPOINT.getValue()) customType = "Geom";
        else if (temptype == MeosType.T_TGEOGPOINT.getValue()) customType = "Geog";
        else
            throw new IllegalArgumentException("JMEOS has no class of the temporal type " + temptype);
        TemporalType temporalType = switch (GeneratedFunctions.temporal_subtype(inner)) {
            case "Instant" -> TEMPORAL_INSTANT;
            case "Sequence" -> TEMPORAL_SEQUENCE;
            case "SequenceSet" -> TEMPORAL_SEQUENCE_SET;
            default -> throw new IllegalArgumentException("unknown temporal subtype");
        };
        return create_temporal(inner, customType, temporalType);
    }

    public static Temporal create_temporal(Pointer inner, String customType, TemporalType temporalType){

        Temporal tmp = null;
        if(inner == null){
            tmp = null;
        }
        else{
            if (temporalType == TEMPORAL_INSTANT){
                tmp = create_instant(inner, customType);

            } else if (temporalType == TEMPORAL_SEQUENCE) {
                tmp = create_sequence(inner, customType);
            }

            else if (temporalType == TEMPORAL_SEQUENCE_SET){
                 tmp = create_sequenceset(inner, customType);
            }
        }

        return tmp;
    }

    private static Temporal create_instant(Pointer inner, String customType){
        TInstant instant = null;
        switch (customType){
            case "Integer" -> instant =  new TIntInst(inner);
            case "Float" -> instant = new TFloatInst(inner);
            case "Boolean" -> instant = new TBoolInst(inner);
            case "Geom" -> instant = new TGeomPointInst(inner);
            case "Geog" -> instant = new TGeogPointInst(inner);
            case "String" -> instant = new TTextInst(inner);
        }
        return instant;
    }

    private static Temporal create_sequence(Pointer inner, String customType){
        TSequence sequence = null;
        switch (customType){
            case "Integer" -> sequence =  new TIntSeq(inner);
            case "Float" -> sequence = new TFloatSeq(inner);
            case "Boolean" -> sequence = new TBoolSeq(inner);
            case "Geom" -> sequence = new TGeomPointSeq(inner);
            case "Geog" -> sequence = new TGeogPointSeq(inner);
            case "String" -> sequence = new TTextSeq(inner);
        }
        return sequence;
    }

    private static Temporal create_sequenceset(Pointer inner, String customType) {
        TSequenceSet sequenceset = null;
        switch (customType) {
            case "Integer" -> sequenceset = new TIntSeqSet(inner);
            case "Float" -> sequenceset = new TFloatSeqSet(inner);
            case "Boolean" -> sequenceset = new TBoolSeqSet(inner);
            case "Geom" -> sequenceset = new TGeomPointSeqSet(inner);
            case "Geog" -> sequenceset = new TGeogPointSeqSet(inner);
            case "String" -> sequenceset = new TTextSeqSet(inner);
        }
        return sequenceset;
    }
}
