package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyFunNextExpr extends AlloyVarExpr {
    public AlloyFunNextExpr(Pos pos) {
        super(pos, AlloyStrings.FUNNEXT);
    }

    public AlloyFunNextExpr() {
        super(AlloyStrings.FUNNEXT);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyFunNextExpr rebuild(String label) {
        return new AlloyFunNextExpr(this.pos);
    }
}
