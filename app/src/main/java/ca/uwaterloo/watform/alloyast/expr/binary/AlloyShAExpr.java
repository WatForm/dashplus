package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyShAExpr extends AlloyBinaryExpr {
    public AlloyShAExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.SHA);
    }

    public AlloyShAExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.SHA);
    }
}
