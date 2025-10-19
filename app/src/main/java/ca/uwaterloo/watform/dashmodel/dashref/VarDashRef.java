package ca.uwaterloo.watform.dashmodel.dashref;

import java.util.List;

import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public class VarDashRef extends DashRef {

	public VarDashRef(
		Pos p, 
		String n, 
		List<AlloyExpr> prmValues) {
		super(p, DashRefKind.VAR,n, prmValues);
	}

	public VarDashRef( 
		String n, 
		List<AlloyExpr> prmValues) {
		super(DashRefKind.VAR,n, prmValues);
	}
}