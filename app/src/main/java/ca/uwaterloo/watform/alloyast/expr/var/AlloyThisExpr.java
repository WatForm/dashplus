package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.*;

public final class AlloyThisExpr extends AlloyNameExpr {
	public AlloyThisExpr(Pos pos) {
		super(pos, AlloyStrings.THIS);
	}
}
