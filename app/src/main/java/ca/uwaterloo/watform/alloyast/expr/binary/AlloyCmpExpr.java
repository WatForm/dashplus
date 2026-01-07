package ca.uwaterloo.watform.alloyast.expr.binary;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

// AlloyComparisonExpr
public final class AlloyCmpExpr extends AlloyBinaryExpr {
    public static enum Comp {
        IN(AlloyStrings.IN),
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

    public final boolean neg;
    public final Comp comp;

    public AlloyCmpExpr(Pos pos, AlloyExpr left, boolean neg, Comp comp, AlloyExpr right) {
        super(pos, left, right, (neg ? NOT_EXCL : "") + comp.toString());
        this.neg = neg;
        this.comp = comp;
        reqNonNull(nullField(pos, this), this.comp);
    }

    public AlloyCmpExpr(AlloyExpr left, boolean neg, Comp comp, AlloyExpr right) {
        this(Pos.UNKNOWN, left, neg, comp, right);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyCmpExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyCmpExpr(this.pos, left, this.neg, this.comp, right);
    }
}
