package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;

public final class AlloyStrLiteralExpr extends AlloyVarExpr {
	public AlloyStrLiteralExpr(Pos pos, String strLiteral) {
		super(pos, strLiteral);
	}
}
