package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public final class AlloyNameExpr extends AlloyExpr {
	public final String label;

	public AlloyNameExpr(Pos pos, String label) {
		super(pos);
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
