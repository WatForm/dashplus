package ca.uwaterloo.watform.dashast.dashref;

import java.util.List;

import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public class EventDashRef extends DashRef {

	public EventDashRef(
		Pos p, 
		String n, 
		List<AlloyExpr> prmValues) {
		super(p, DashRefKind.EVENT,n, prmValues);
	}

	public EventDashRef( 
		String n, 
		List<AlloyExpr> prmValues) {
		super(DashRefKind.EVENT,n, prmValues);
	}
}