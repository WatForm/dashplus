package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

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
}
