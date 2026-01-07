package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.AlloyStrings;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
public final class AlloySigIntExpr extends AlloyVarExpr
        implements AlloySigRefExpr, AlloyScopableExpr {
    public AlloySigIntExpr(Pos pos) {
        super(pos, AlloyStrings.SIGINT);
    }

    public AlloySigIntExpr() {
        super(AlloyStrings.SIGINT);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloySigIntExpr rebuild(String label) {
        return new AlloySigIntExpr(this.pos);
    }
}
