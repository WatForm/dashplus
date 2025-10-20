package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyThisExpr extends AlloyNameExpr {
    public AlloyThisExpr(Pos pos) {
        super(pos, AlloyStrings.THIS);
    }

    public AlloyThisExpr() {
        super(AlloyStrings.THIS);
    }
}
