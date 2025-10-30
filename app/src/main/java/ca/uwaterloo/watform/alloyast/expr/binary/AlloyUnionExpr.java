package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyUnionExpr extends AlloyBinaryExpr {
    public AlloyUnionExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.PLUS);
    }

    public AlloyUnionExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.PLUS);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyUnionExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyUnionExpr(this.pos, left, right);
    }
}
