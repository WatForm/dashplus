package ca.uwaterloo.watform.dashast;

import java.util.ArrayList;
import java.util.StringJoiner;

import ca.uwaterloo.watform.util.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

// send P[x]/ev1
// send ev1
// send y.x.ev1

public class DashSend  extends DashExpr {

	public DashSend(Pos pos,AlloyExpr e) {
		super(pos,e);
	}
	public String toString(Integer i) {
		return super.toString(DashStrings.sendName, i);
	}
}

