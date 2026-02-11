package ca.uwaterloo.watform.alloytotla;

import  ca.uwaterloo.watform.tlaast.tlabinops.*;
import  ca.uwaterloo.watform.tlaast.tlaunops.*;
import  ca.uwaterloo.watform.tlaast.tlaquantops.*;
import  ca.uwaterloo.watform.tlaast.tlanaryops.*;
import  ca.uwaterloo.watform.tlaast.tlaliterals.*;
import  ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBracketExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyCphExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyIteExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyLetExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyQuantificationExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;

public class AlloyTlaExprVis implements AlloyExprVis<TlaExp> {

	@Override
	public TlaExp visit(DashRef dashRef) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(DashParam dashParam) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyBinaryExpr binExpr) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyUnaryExpr unaryExpr) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyVarExpr varExpr) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyBlock block) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyBracketExpr bracketExpr) {

		
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyCphExpr comprehensionExpr) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyIteExpr iteExpr) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyLetExpr letExpr) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyQuantificationExpr quantificationExpr) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public TlaExp visit(AlloyDecl decl) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}
	
}
