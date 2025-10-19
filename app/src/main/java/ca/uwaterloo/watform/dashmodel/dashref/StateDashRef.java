package ca.uwaterloo.watform.dashmodel.dashref;

import java.util.List;

import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public class StateDashRef extends DashRef {

	public StateDashRef(
		Pos p, 
		String n, 
		List<AlloyExpr> prmValues) {
		super(p, DashRefKind.STATE,n, prmValues);
	}

	public StateDashRef( 
		String n, 
		List<AlloyExpr> prmValues) {
		super(DashRefKind.STATE,n, prmValues);
	}
}