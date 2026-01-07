package ca.uwaterloo.watform.alloyast.expr.var;


import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
public final class AlloyNumExpr extends AlloyVarExpr {
    public final int value;

    public AlloyNumExpr(Pos pos, String numLabel) {
        super(pos, numLabel);
        this.value = Integer.parseInt(numLabel);
    }

    public AlloyNumExpr(String numLabel) {
        this(Pos.UNKNOWN, numLabel);
    }

    public AlloyNumExpr(int num) {
        this(Pos.UNKNOWN, String.valueOf(num));
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyNumExpr rebuild(String label) {
        return new AlloyNumExpr(this.pos, label);
    }
}
