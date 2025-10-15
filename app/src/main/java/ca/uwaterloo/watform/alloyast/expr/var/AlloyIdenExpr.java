package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyIdenExpr extends AlloyVarExpr {
	public AlloyIdenExpr(Pos pos) {
		super(pos, AlloyStrings.IDEN);
	}
}
