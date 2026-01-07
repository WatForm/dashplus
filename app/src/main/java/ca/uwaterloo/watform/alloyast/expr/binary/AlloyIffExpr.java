package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;

public final class AlloyIffExpr extends AlloyBinaryExpr {
    public AlloyIffExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.IFF_ARR);
    }

    public AlloyIffExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.IFF_ARR);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyIffExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyIffExpr(this.pos, left, right);
    }
}
