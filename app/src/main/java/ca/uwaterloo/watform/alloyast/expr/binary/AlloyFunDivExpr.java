package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyFunDivExpr extends AlloyBinaryExpr {
    public AlloyFunDivExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.FUNDIV);
    }

    public AlloyFunDivExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.FUNDIV);
    }
}
