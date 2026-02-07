package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyFunMulExpr extends AlloyBinaryExpr {
    public AlloyFunMulExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.FUNMUL);
    }

    public AlloyFunMulExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.FUNMUL);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyFunMulExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyFunMulExpr(this.pos, left, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.MUL_DIV_REM;
    }
}
