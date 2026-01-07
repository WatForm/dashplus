package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.AlloyStrings;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
public final class AlloyStepsExpr extends AlloyVarExpr
        implements AlloySigRefExpr, AlloyScopableExpr {
    public AlloyStepsExpr(Pos pos) {
        super(pos, AlloyStrings.STEPS);
    }

    public AlloyStepsExpr() {
        super(AlloyStrings.STEPS);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyStepsExpr rebuild(String label) {
        return new AlloyStepsExpr(this.pos);
    }
}
