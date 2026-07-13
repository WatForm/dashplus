package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.alloyevaluator.ThreeVal.*;

import ca.uwaterloo.watform.alloyevaluator.OverflowAtom.OverflowDirection;

public sealed interface Atom permits LabelAtom, IntegerAtom, OverflowAtom {

    // Use this method to check if values are equal. Standard equals does not handle overflows
    // correctly
    public static ThreeVal threeEqual(Atom a, Atom b) {
        if (a instanceof LabelAtom la) {
            if (b instanceof LabelAtom lb) {
                return convertThree(la.label().equals(lb.label()));
            }
            return FALSE;
        } else if (b instanceof LabelAtom) {
            return FALSE;
        }

        if (a instanceof IntegerAtom ia && b instanceof IntegerAtom ib) {
            return convertThree(ia.value() == ib.value());
        }

        // at least one overflows
        OverflowDirection da = directionOf(a);
        OverflowDirection db = directionOf(b);

        if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
            return UNKNOWN;
        }
        // both known directions, or one known-overflow vs. a plain int (null direction)
        return (da == db) ? UNKNOWN : FALSE;
    }

    // Use this method to compare atoms
    public static ThreeVal threeLessThan(Atom a, Atom b) {
        if (a instanceof LabelAtom || b instanceof LabelAtom) {
            throw AlloyEvaluatorImplError.comparisonError("Range-based comparison on a label");
        }

        if (a instanceof IntegerAtom ai) {
            if (b instanceof IntegerAtom bi) {
                return convertThree(ai.value() < bi.value());
            }
            // b must be OverflowAtom here, so directionOf(b) is never null
            OverflowDirection db = directionOf(b);
            return switch (db) {
                case OVERFLOW_DOWN -> FALSE;
                case OVERFLOW_UP -> TRUE;
                case OVERFLOW_UNKNOWN -> UNKNOWN;
                default ->
                        throw AlloyEvaluatorImplError.comparisonError("Unexpected atom b overflow");
            };
        } else if (a instanceof OverflowAtom ao) {
            OverflowDirection da = ao.direction();
            OverflowDirection db = directionOf(b); // null if b is a plain IntegerAtom

            if (da == OverflowDirection.OVERFLOW_UNKNOWN
                    || db == OverflowDirection.OVERFLOW_UNKNOWN) {
                return UNKNOWN;
            }
            if (da == db) {
                return UNKNOWN;
            }
            return convertThree(da == OverflowDirection.OVERFLOW_DOWN);
        } else {
            throw AlloyEvaluatorImplError.comparisonError(
                    "Unreachable: unexpected Atom a subtype " + a.getClass());
        }
    }

    public static ThreeVal threeGreaterEqual(Atom a, Atom b) {
        return threeLessThan(a, b).not();
    }

    public static ThreeVal threeLessEqual(Atom a, Atom b) {
        return threeLessThan(a, b).or(threeEqual(a, b));
    }

    public static ThreeVal threeGreater(Atom a, Atom b) {
        return threeLessThan(a, b).not().and(threeEqual(a, b).not());
    }

    // returns the overflow direction, or null if a is a plain (non-overflowing) IntegerAtom
    private static OverflowDirection directionOf(Atom a) {
        return (a instanceof OverflowAtom oa) ? oa.direction() : null;
    }
}
