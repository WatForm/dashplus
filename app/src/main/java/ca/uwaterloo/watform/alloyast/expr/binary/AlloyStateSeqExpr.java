package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.AlloyStrings;

public final class AlloyStateSeqExpr extends AlloyBinaryExpr {
	public AlloyStateSeqExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
		super(pos, left, right, AlloyStrings.SEQUENCE_OP);
	}
}
