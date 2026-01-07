package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
public final class AlloyDisjExpr extends AlloyVarExpr {
    public AlloyDisjExpr(Pos pos) {
        super(pos, AlloyStrings.DISJ);
    }

    public AlloyDisjExpr() {
        super(AlloyStrings.DISJ);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyDisjExpr rebuild(String label) {
        return new AlloyDisjExpr(this.pos);
    }
}
