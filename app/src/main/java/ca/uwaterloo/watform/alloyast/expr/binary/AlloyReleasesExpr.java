package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyReleasesExpr extends AlloyBinaryExpr {
    public AlloyReleasesExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.RELEASES);
    }

    public AlloyReleasesExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.RELEASES);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyReleasesExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyReleasesExpr(this.pos, left, right);
    }
}
