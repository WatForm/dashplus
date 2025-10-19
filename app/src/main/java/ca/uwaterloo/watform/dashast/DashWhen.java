package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashWhen  extends DashExpr {
	//public Expr when;
	public DashWhen(Pos pos,AlloyExpr w) {
		super(pos, w);
	}
	@Override
	public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.whenName, sb, indent);
	}
}
