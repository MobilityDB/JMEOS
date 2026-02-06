package types.temporal;


/**
 * Enumeration of the interpolation type associating it with the corresponding MEOS value.
 *
 * @author ARIJIT SAMAL
 */
public enum TInterpolation {
    NONE(0),
    DISCRETE(1),
    STEPWISE(2),
    LINEAR(3);

    private final int value;

    TInterpolation(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static TInterpolation fromString(String source){
        return fromString(source,true);
    }

    public static TInterpolation fromString(String source, boolean none) {
        String lowerCaseSource = source.toLowerCase();
        switch (lowerCaseSource) {
            case "discrete":
                return TInterpolation.DISCRETE;
            case "linear":
                return TInterpolation.LINEAR;
            case "stepwise", "step":
                return TInterpolation.STEPWISE;
            case "none":
                if (none) {
                    return TInterpolation.NONE;
                } else {
                    throw new IllegalArgumentException("Value " + source + " doesn't represent any valid interpolation");
                }
            default:
                throw new IllegalArgumentException("Value " + source + " doesn't represent any valid interpolation");
        }
    }

    /**
     * Returns the string representation of this interpolation type in the format expected by MEOS.
     * MEOS expects: "None", "Discrete", "Step", "Linear" (note: "Step" not "Stepwise")
     * @return the interpolation name in MEOS format
     */
    @Override
    public String toString() {
        switch (this) {
            case NONE:
                return "None";
            case DISCRETE:
                return "Discrete";
            case STEPWISE:
                return "Step";
            case LINEAR:
                return "Linear";
            default:
                throw new IllegalStateException("Unknown interpolation type: " + this.name());
        }
    }
}

