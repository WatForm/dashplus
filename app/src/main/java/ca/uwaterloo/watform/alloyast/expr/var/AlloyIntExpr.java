package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

// an actually number such as 1
public final class AlloyIntExpr extends AlloyVarExpr implements AlloyScopableExpr {
    public AlloyIntExpr(Pos pos) {
        super(pos, AlloyStrings.INT);
    }

    public AlloyIntExpr() {
        super(AlloyStrings.INT);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyIntExpr rebuild(String label) {
        return new AlloyIntExpr(this.pos);
    }
}
