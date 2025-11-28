package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloySinceExpr extends AlloyBinaryExpr {
    public AlloySinceExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.SINCE);
    }

    public AlloySinceExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.SINCE);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloySinceExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloySinceExpr(this.pos, left, right);
    }
}
