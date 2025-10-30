package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyTransExpr extends AlloyUnaryExpr {
    public AlloyTransExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.TRANS);
    }

    public AlloyTransExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.TRANS);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyTransExpr rebuild(AlloyExpr sub) {
        return new AlloyTransExpr(this.pos, sub);
    }
}
