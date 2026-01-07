package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyFunMinExpr extends AlloyVarExpr {
    public AlloyFunMinExpr(Pos pos) {
        super(pos, AlloyStrings.FUNMIN);
    }

    public AlloyFunMinExpr() {
        super(AlloyStrings.FUNMIN);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyFunMinExpr rebuild(String label) {
        return new AlloyFunMinExpr(this.pos);
    }
}
