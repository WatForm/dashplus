package ca.uwaterloo.watform.dashast;

import java.util.List;
import java.util.Collections;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashFrom extends DashExpr {

	public DashFrom(Pos pos, AlloyExpr d) {
		super(pos,d);
	}
	public String toString(Integer i) {
		return super.toString(DashStrings.fromName, i);
	}
	public AlloyExpr getSrc() {
		return super.getExp();
	}

}
