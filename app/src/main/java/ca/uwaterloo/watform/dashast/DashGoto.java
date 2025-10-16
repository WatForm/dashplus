package ca.uwaterloo.watform.dashast;

import java.util.List;
import java.util.Collections;

import ca.uwaterloo.watform.util.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashGoto extends DashExpr {

	public DashGoto(Pos pos, AlloyExpr d) {
		super(pos,d);
	}
	public String toString(Integer i) {
		return super.toString(DashStrings.gotoName, i);
	}
	public AlloyExpr getDest() {
		return super.getExp();
	}
}