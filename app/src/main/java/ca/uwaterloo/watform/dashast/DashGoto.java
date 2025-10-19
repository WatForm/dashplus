package ca.uwaterloo.watform.dashast;

import java.util.List;
import java.util.Collections;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashGoto extends DashExpr {

	public DashGoto(Pos pos, AlloyExpr d) {
		super(pos,d);
	}
	@Override
	public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.gotoName, sb, indent);
	}

}
