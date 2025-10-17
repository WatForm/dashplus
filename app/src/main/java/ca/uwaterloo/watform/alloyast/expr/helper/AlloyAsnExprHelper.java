package ca.uwaterloo.watform.alloyast.expr.helper;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

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

	@Override
	public void toString(StringBuilder sb, int indent) {
		sb.append(name.toString());
		sb.append(AlloyStrings.EQUAL);
		expr.toString(sb, indent);
	}
}
