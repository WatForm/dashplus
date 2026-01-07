package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyCardExpr extends AlloyUnaryExpr {
    public AlloyCardExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.CARDINALITY);
    }

    public AlloyCardExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.CARDINALITY);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyCardExpr rebuild(AlloyExpr sub) {
        return new AlloyCardExpr(this.pos, sub);
    }
}
