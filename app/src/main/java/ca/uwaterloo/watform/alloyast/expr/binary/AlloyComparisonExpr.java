package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyComparisonExpr extends AlloyBinaryExpr {
    public static enum Negation {
        NONE(""),
        NOT(AlloyStrings.NOT),
        NOT_EXCL(AlloyStrings.NOT_EXCL);

        private final String label;

        Negation(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    public static enum Comp {
        IN(AlloyStrings.IN),
        EQUAL(AlloyStrings.EQUAL),
        LESS_THAN(AlloyStrings.LT),
        GREATER_THAN(AlloyStrings.GT),
        LESS_EQUAL(AlloyStrings.LE),
        EQUAL_LESS(AlloyStrings.EL),
        GREATER_EQUAL(AlloyStrings.GE);

        private final String label;

        Comp(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    public final Negation neg;
    public final Comp comp;

    public AlloyComparisonExpr(Pos pos, AlloyExpr left, Negation neg, Comp comp, AlloyExpr right) {
        super(
                pos,
                left,
                right,
                (neg == Negation.NONE)
                        ? comp.toString()
                        : neg.toString() + AlloyStrings.SPACE + comp.toString());
        this.neg = neg;
        this.comp = comp;
    }

    public AlloyComparisonExpr(AlloyExpr left, Negation neg, Comp comp, AlloyExpr right) {
        super(
                left,
                right,
                (neg == Negation.NONE)
                        ? comp.toString()
                        : neg.toString() + AlloyStrings.SPACE + comp.toString());
        this.neg = neg;
        this.comp = comp;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyComparisonExpr rebuild(Pos pos, AlloyExpr left, AlloyExpr right) {
        return new AlloyComparisonExpr(pos, left, this.neg, this.comp, right);
    }
}
