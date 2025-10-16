package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public final class AlloyAndExpr extends AlloyBinaryExpr {
	public AlloyAndExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
		super(pos, left, right, AlloyStrings.AND);
	}
}
