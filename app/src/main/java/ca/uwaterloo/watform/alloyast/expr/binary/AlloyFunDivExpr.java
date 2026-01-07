package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;

public final class AlloyFunDivExpr extends AlloyBinaryExpr {
    public AlloyFunDivExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.FUNDIV);
    }

    public AlloyFunDivExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.FUNDIV);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyFunDivExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyFunDivExpr(this.pos, left, right);
    }
}
