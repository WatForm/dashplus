package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyPredTotOrdExpr extends AlloyVarExpr {
    public AlloyPredTotOrdExpr(Pos pos) {
        super(pos, AlloyStrings.PRED_TOTALORDER);
    }

    public AlloyPredTotOrdExpr() {
        super(AlloyStrings.PRED_TOTALORDER);
    }
}
