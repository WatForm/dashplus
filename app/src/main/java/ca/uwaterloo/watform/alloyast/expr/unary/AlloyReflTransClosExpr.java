package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public final class AlloyReflTransClosExpr extends AlloyUnaryExpr {
	public AlloyReflTransClosExpr(Pos pos, AlloyExpr sub) {
		super(pos, sub, AlloyStrings.REFL_TRANS_CLOS);
	}
}
