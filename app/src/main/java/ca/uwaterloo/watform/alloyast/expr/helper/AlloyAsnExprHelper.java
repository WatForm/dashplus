package ca.uwaterloo.watform.alloyast.expr.helper;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;

public final class AlloyAsnExprHelper extends AlloyASTNode {
	public final AlloyNameExpr name;
	public final AlloyExpr expr;

	public AlloyAsnExprHelper(Pos pos, AlloyNameExpr name, AlloyExpr expr) {
		super(pos);
		this.name = name;
		this.expr = expr;
	}

	public AlloyNameExpr getName() {
		return name;
	}

	public AlloyExpr getExpr() {
		return expr;
	}
}
