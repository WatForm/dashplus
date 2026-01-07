package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.AlloyStrings;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
public final class AlloyUnivExpr extends AlloyVarExpr implements AlloySigRefExpr {
    public AlloyUnivExpr(Pos pos) {
        super(pos, AlloyStrings.UNIV);
    }

    public AlloyUnivExpr() {
        super(AlloyStrings.UNIV);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyUnivExpr rebuild(String label) {
        return new AlloyUnivExpr(this.pos);
    }
}
