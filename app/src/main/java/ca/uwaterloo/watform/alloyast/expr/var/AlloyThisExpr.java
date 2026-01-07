package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
public final class AlloyThisExpr extends AlloyVarExpr {
    public AlloyThisExpr(Pos pos) {
        super(pos, AlloyStrings.THIS);
    }

    public AlloyThisExpr() {
        super(AlloyStrings.THIS);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyThisExpr rebuild(String label) {
        return new AlloyThisExpr(this.pos);
    }
}
