package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.AlloyStrings;

public final class AlloyNumIntExpr extends AlloyUnaryExpr {
	public AlloyNumIntExpr(Pos pos, AlloyExpr sub) {
		super(pos, sub, AlloyStrings.INT);
	}
}
