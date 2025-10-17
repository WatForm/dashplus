package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public final class AlloyTransClosExpr extends AlloyUnaryExpr {
	public AlloyTransClosExpr(Pos pos, AlloyExpr sub) {
		super(pos, sub, AlloyStrings.TRANS_CLOS);
	}
}
