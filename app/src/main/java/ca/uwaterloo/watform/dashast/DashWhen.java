package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashWhen  extends DashExpr {
	//public Expr when;
	public DashWhen(Pos pos,AlloyExpr w) {
		super(pos, w);
	}
	public String toString(Integer i) {
		return super.toString(DashStrings.whenName, i);
	}
	public AlloyExpr getWhen() {
		return super.getExp();
	}
}