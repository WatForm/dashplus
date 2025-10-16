package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public final class AlloyDomRestrExpr extends AlloyBinaryExpr {
	public AlloyDomRestrExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
		super(pos, left, right, AlloyStrings.DOMRESTR);
	}
}
