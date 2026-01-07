package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;

public final class AlloyUntilExpr extends AlloyBinaryExpr {
    public AlloyUntilExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.UNTIL);
    }

    public AlloyUntilExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.UNTIL);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyUntilExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyUntilExpr(this.pos, left, right);
    }
}
