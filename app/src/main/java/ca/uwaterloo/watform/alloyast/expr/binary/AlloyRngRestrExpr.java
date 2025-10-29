package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyRngRestrExpr extends AlloyBinaryExpr {
    public AlloyRngRestrExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.RNGRESTR);
    }

    public AlloyRngRestrExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.RNGRESTR);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyRngRestrExpr rebuild(Pos pos, AlloyExpr left, AlloyExpr right) {
        return new AlloyRngRestrExpr(pos, left, right);
    }
}
