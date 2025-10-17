package ca.uwaterloo.watform.dashast;

import java.util.Collections;


import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashDo  extends DashExpr {

	public DashDo(Pos pos,AlloyExpr a) {
		super(pos,a);
	}
	@Override
	public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.doName, sb, indent);
	}
	public AlloyExpr getDo() {
		return super.getExp();
	}
}
