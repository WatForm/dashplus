package ca.uwaterloo.watform.dashast;

import java.util.Collections;

import ca.uwaterloo.watform.util.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashOn extends DashExpr {

	public DashOn(Pos pos, AlloyExpr e) {
		super(pos,e);
	}
	public String toString(Integer i) {
		return super.toString(DashStrings.onName, i);
	}
}