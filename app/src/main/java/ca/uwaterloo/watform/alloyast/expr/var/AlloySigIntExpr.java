package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.utils.*;

public final class AlloySigIntExpr extends AlloyVarExpr implements AlloySigRefExpr {
    public AlloySigIntExpr(Pos pos) {
        super(pos, AlloyStrings.SIGINT);
    }

    public AlloySigIntExpr() {
        super(AlloyStrings.SIGINT);
    }
}
