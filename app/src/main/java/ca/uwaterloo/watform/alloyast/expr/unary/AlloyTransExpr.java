package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public final class AlloyTransExpr extends AlloyUnaryExpr {
	public AlloyTransExpr(Pos pos, AlloyExpr sub) {
		super(pos, sub, AlloyStrings.TRANS);
	}
}
