package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyArrowExpr extends AlloyBinaryExpr {
    public enum Mul {
        LONE(AlloyStrings.LONE),
        ONE(AlloyStrings.ONE),
        SOME(AlloyStrings.SOME),
        SET(AlloyStrings.SET),
        DEFAULTSET("");

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
        super(pos, left, right, mul1.toString() + AlloyStrings.RARROW + mul2.toString());
        this.mul1 = mul1;
        this.mul2 = mul2;
    }

    public AlloyArrowExpr(AlloyExpr left, Mul mul1, Mul mul2, AlloyExpr right) {
        super(left, right, mul1.toString() + AlloyStrings.RARROW + mul2.toString());
        this.mul1 = mul1;
        this.mul2 = mul2;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyArrowExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyArrowExpr(this.pos, left, this.mul1, this.mul2, right);
    }
}
