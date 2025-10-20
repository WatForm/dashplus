package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloySeqExpr extends AlloyNameExpr {
    public AlloySeqExpr(Pos pos) {
        super(pos, AlloyStrings.SEQ);
    }

    public AlloySeqExpr() {
        super(AlloyStrings.SEQ);
    }
}
