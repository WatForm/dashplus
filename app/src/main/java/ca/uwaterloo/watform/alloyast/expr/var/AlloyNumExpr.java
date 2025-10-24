package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyNumExpr extends AlloyVarExpr {
    public final boolean isPositive;
    public final int value;

    public AlloyNumExpr(Pos pos, boolean isPositve, String numLabel) {
        super(pos, isPositve ? numLabel : AlloyStrings.MINUS + numLabel);
        this.value = Integer.parseInt(numLabel);
        this.isPositive = isPositve;
    }

    public AlloyNumExpr(boolean isPositve, String numLabel) {
        super(isPositve ? numLabel : AlloyStrings.MINUS + numLabel);
        this.value = Integer.parseInt(numLabel);
        this.isPositive = isPositve;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
