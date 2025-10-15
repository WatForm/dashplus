package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.AlloyStrings;

public final class AlloyImpliesExpr extends AlloyBinaryExpr {
	public AlloyImpliesExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
		super(pos, left, right, AlloyStrings.RFATARROW);
	}
}
