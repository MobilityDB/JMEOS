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

