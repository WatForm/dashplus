package ca.uwaterloo.watform.tlaexpvisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaConst;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaIfThenElse;
import ca.uwaterloo.watform.tlaast.TlaLetBinding;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaRecord;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaBinOp;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaNaryOp;
import ca.uwaterloo.watform.tlaast.tlaquantops.TlaQuantOp;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaUnaryOp;


public class TlaApplCollector implements TlaExpVis<List<TlaAppl>> {

	
	@Override
	public List<TlaAppl> visit(TlaOperator OperatorExp) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public List<TlaAppl> visit(TlaAppl ApplExp) {
		List<TlaAppl> answer = new ArrayList<>();
		answer.add(ApplExp);
		for(var c : ApplExp.getChildren())
			answer.addAll(visit(c));
		return answer;
	}

	@Override
	public List<TlaAppl> visit(TlaConst ConstExp) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public List<TlaAppl> visit(TlaDefn DefnExp) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public List<TlaAppl> visit(TlaIfThenElse ifThenElseExp) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public List<TlaAppl> visit(TlaLetBinding letBindingExp) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public List<TlaAppl> visit(TlaRecord recordExp) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public List<TlaAppl> visit(TlaStdLibs stdLibExp) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public List<TlaAppl> visit(TlaVar varExp) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	/*
	when run on a TLA defn, returns a list of TlaAppls that the defn relies on
	*/
	
}
