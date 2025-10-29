package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyDotExpr extends AlloyBinaryExpr {
    public AlloyDotExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.DOT);
    }

    public AlloyDotExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.DOT);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyDotExpr rebuild(Pos pos, AlloyExpr left, AlloyExpr right) {
        return new AlloyDotExpr(pos, left, right);
    }
}
