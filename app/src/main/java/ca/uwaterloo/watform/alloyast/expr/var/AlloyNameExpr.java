package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyNameExpr extends AlloyVarExpr {
    public AlloyNameExpr(Pos pos, String label) {
        super(pos, label);
    }

    public AlloyNameExpr(String label) {
        this(Pos.UNKNOWN, label);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyNameExpr rebuild(String label) {
        return new AlloyNameExpr(this.pos, label);
    }
}
