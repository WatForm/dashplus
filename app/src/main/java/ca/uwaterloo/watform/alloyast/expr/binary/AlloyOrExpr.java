package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyOrExpr extends AlloyBinaryExpr {
    public AlloyOrExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.OR);
    }

    public AlloyOrExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.OR);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyOrExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyOrExpr(this.pos, left, right);
    }
}
