package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public final class AlloySeqIntExpr extends AlloyVarExpr {
	public AlloySeqIntExpr(Pos pos) {
		super(pos, AlloyStrings.SEQ_INT);
	}
}
