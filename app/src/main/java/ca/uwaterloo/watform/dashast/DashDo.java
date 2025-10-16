package ca.uwaterloo.watform.dashast;

import java.util.Collections;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashDo  extends DashExpr {

	public DashDo(Pos pos,AlloyExpr a) {
		super(pos,a);
	}
	public String toString(Integer i) {
        return super.toString(DashStrings.doName, i);
	}
	public AlloyExpr getDo() {
		return super.getExp();
	}
}
