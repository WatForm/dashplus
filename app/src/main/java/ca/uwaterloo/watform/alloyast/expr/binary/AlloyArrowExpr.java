package ca.uwaterloo.watform.alloyast.expr.binary;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.*;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyArrowExpr extends AlloyBinaryExpr {
    public enum Mul {
        LONE(AlloyStrings.LONE),
        ONE(AlloyStrings.ONE),
        SOME(AlloyStrings.SOME),
        SET(AlloyStrings.SET);

        public final String label;

        private Mul(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public final String toString() {
            return label;
        }
    }

    public final Mul mul1;
    public final Mul mul2;

    public AlloyArrowExpr(Pos pos, AlloyExpr left, Mul mul1, Mul mul2, AlloyExpr right) {
        super(
                pos,
                left,
                right,
                ((null != mul1) ? mul1.toString() + SPACE : "")
                        + RARROW
                        + ((null != mul2) ? SPACE + mul2.toString() : ""));
        // how handle the above if nulls are non longer required?
        this.mul1 = mul1;
        this.mul2 = mul2;
        reqNonNull(nullField(pos, this), this.mul1, this.mul2);
    }

    public AlloyArrowExpr(AlloyExpr left, Mul mul1, Mul mul2, AlloyExpr right) {
        this(Pos.UNKNOWN, left, mul1, mul2, right);
    }

    public AlloyArrowExpr(AlloyExpr left, AlloyExpr right) {
        this(Pos.UNKNOWN, left, Mul.SET, Mul.SET, right);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyArrowExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyArrowExpr(this.pos, left, this.mul1, this.mul2, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.ARROW_PREC;
    }

    @Override
    public boolean isLeftAssoc() {
        return false;
    }
}
