package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyTriggeredExpr extends AlloyBinaryExpr {
    public AlloyTriggeredExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.TRIGGERED);
    }

    public AlloyTriggeredExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.TRIGGERED);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyTriggeredExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyTriggeredExpr(this.pos, left, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.BIN_TEMP;
    }
}
