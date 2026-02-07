package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyAndExpr extends AlloyBinaryExpr {
    public AlloyAndExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.AND);
    }

    public AlloyAndExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.AND);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyAndExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyAndExpr(this.pos, left, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.AND;
    }
}
