package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.utils.*;

public final class AlloySeqIntExpr extends AlloyVarExpr implements AlloySigRefExpr {
    public AlloySeqIntExpr(Pos pos) {
        super(pos, AlloyStrings.SEQ_INT);
    }
}
