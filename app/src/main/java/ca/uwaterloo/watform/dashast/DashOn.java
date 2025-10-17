package ca.uwaterloo.watform.dashast;

import java.util.Collections;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashOn extends DashExpr {

	public DashOn(Pos pos, AlloyExpr e) {
		super(pos,e);
	}
	@Override
	public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.onName, sb, indent);
	}

}
