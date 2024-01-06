package utils.meosCatalog;


/**
 * Meos enumeration storing the type and operators through MEOS integer values.
 *
 * @author Nidhal Mareghni
 * @since 18/10/2023
 */
public class MeosEnums {
    public enum meosType {
        T_UNKNOWN(0),
        T_BOOL(1),
        T_DOUBLE2(2),
        T_DOUBLE3(3),
        T_DOUBLE4(4),
        T_FLOAT8(5),
        T_FLOATSET(6),
        T_FLOATSPAN(7),
        T_FLOATSPANSET(8),
        T_INT4(9),
        T_INT4RANGE(10),
        T_INT4MULTIRANGE(11),
        T_INTSET(12),
        T_INTSPAN(13),
        T_INTSPANSET(14),
        T_INT8(15),
        T_BIGINTSET(16),
        T_BIGINTSPAN(17),
        T_BIGINTSPANSET(18),
        T_STBOX(19),
        T_TBOOL(20),
        T_TBOX(21),
        T_TDOUBLE2(22),
        T_TDOUBLE3(23),
        T_TDOUBLE4(24),
        T_TEXT(25),
        T_TEXTSET(26),
        T_TFLOAT(27),
        T_TIMESTAMPTZ(28),
        T_TINT(29),
        T_TSTZMULTIRANGE(30),
        T_TSTZRANGE(31),
        T_TSTZSET(32),
        T_TSTZSPAN(33),
        T_TSTZSPANSET(34),
        T_TTEXT(35),
        T_GEOMETRY(36),
        T_GEOMSET(37),
        T_GEOGRAPHY(38),
        T_GEOGSET(39),
        T_TGEOMPOINT(40),
        T_TGEOGPOINT(41),
        T_NPOINT(42),
        T_NPOINTSET(43),
        T_NSEGMENT(44),
        T_TNPOINT(45);

        private final int value;

        meosType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }




    public enum meosOper {
        UNKNOWN_OP(0),
        EQ_OP(1),
        NE_OP(2),
        LT_OP(3),
        LE_OP(4),
        GT_OP(5),
        GE_OP(6),
        ADJACENT_OP(7),
        UNION_OP(8),
        MINUS_OP(9),
        INTERSECT_OP(10),
        OVERLAPS_OP(11),
        CONTAINS_OP(12),
        CONTAINED_OP(13),
        SAME_OP(14),
        LEFT_OP(15),
        OVERLEFT_OP(16),
        RIGHT_OP(17),
        OVERRIGHT_OP(18),
        BELOW_OP(19),
        OVERBELOW_OP(20),
        ABOVE_OP(21),
        OVERABOVE_OP(22),
        FRONT_OP(23),
        OVERFRONT_OP(24),
        BACK_OP(25),
        OVERBACK_OP(26),
        BEFORE_OP(27),
        OVERBEFORE_OP(28),
        AFTER_OP(29),
        OVERAFTER_OP(30),
        EVEREQ_OP(31),
        EVERNE_OP(32),
        EVERLT_OP(33),
        EVERLE_OP(34),
        EVERGT_OP(35),
        EVERGE_OP(36),
        ALWAYSEQ_OP(37),
        ALWAYSNE_OP(38),
        ALWAYSLT_OP(39),
        ALWAYSLE_OP(40),
        ALWAYSGT_OP(41),
        ALWAYSGE_OP(42);

        private final int value;

        meosOper(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


}
