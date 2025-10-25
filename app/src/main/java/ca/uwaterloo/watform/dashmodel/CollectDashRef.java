package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.dashref.*;
import java.util.List;

public class CollectDashRef implements DashExprVis {

    @Override
    public List<DashRef> visit(AlloyBinaryExpr binExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyBlock block) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyBracketExpr bracketExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyComprehensionExpr comprehensionExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyIteExpr iteExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyLetExpr letExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyParenExpr parenExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyQuantificationExpr quantificationExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyUnaryExpr unaryExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyVarExpr varExpr) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyDecl decl) {
        return null;
    }
    ;

    @Override
    public List<DashRef> visit(DashRef d) {
        return null;
    }

    @Override
    public List<DashRef> visit(DashParam dashParam) {
        return null;
    }
    /*
    // might be primed or unprimed
    public List<DashRef> collectDashRef(DashExpr exp) {
    	assert(exp != null);
    	List<DashRef> x = new ArrayList<DashRef>();
    	if (DashRef.isDashRef(exp)) {
    		x.add((DashRef) exp);
    		return x;
    	} else if (isDashExprVar(exp)) {
    		return x;
    	} else if (isDashExprBinary(exp)) {
    		x.addAll(collectDashRef(getLeft(exp)));
    		x.addAll(collectDashRef(getRight(exp)));
    		return x;
    	} else if (isDashExprBadJoin(exp)) {
    		x.addAll(collectDashRef(getLeft(exp)));
    		x.addAll(collectDashRef(getRight(exp)));
    		return x;
    	} else if (exp instanceof DashExprCall) {
    		for (DashExpr e: ((DashExprCall) exp).args) x.addAll(collectDashRef(e));
    		return x;
    	} else if (exp instanceof DashExprChoice){
    		for (DashExpr e: ((DashExprChoice) exp).choices) x.addAll(collectDashRef(e));
    		return x;
    	} else if (exp instanceof DashExprITE){
    		x.addAll(collectDashRef(getCond(exp)));
    		x.addAll(collectDashRef(getLeft(exp)));
    		x.addAll(collectDashRef(getRight(exp)));
    		return x;
    	} else if (exp instanceof DashExprList){
    		for (DashExpr e: ((DashExprList) exp).args) x.addAll(collectDashRef(e));
    		return x;
    	} else if (exp instanceof DashExprUnary){
    		return collectDashRef(((DashExprUnary) exp).sub);
    	} else if (exp instanceof DashExprLet){
    		x.addAll(collectDashRef(((DashExprLet) exp).expr));
    		x.addAll(collectDashRef(((DashExprLet) exp).sub));
    		return x;
    	} else if (exp instanceof DashExprQt){
    		List<DashExpr> ll = ((DashExprQt) exp).decls.stream()
    			.map(i -> i.expr)
    			.collect(Collectors.toList());
    		for (DashExpr e: ll) x.addAll(collectDashRef(e));
    		x.addAll(collectDashRef(((DashExprQt) exp).sub));
    		return x;
    	} else if (exp instanceof DashExprConstant){
    		return new ArrayList<DashRef>();
    	} else {
    		DashErrors.UnsupportedDashExpr("collectDashRef", exp.toString());
    		return null;
    	}
    }

    // returns the primed variables in an exp (but w/o the primes)
    public List<DashRef> primedDashRef(DashExpr exp) {
    	List<DashRef> drs = collectDashRef(exp);
    	List<DashRef> o = new ArrayList<DashRef>();
    	String v;
    	List<DashExpr> paramValues;
    	for (DashRef e: drs) {
    		v = e.getName();
    		paramValues = e.getParamValues();
    		if (hasPrime(v)) {
    			o.add(DashRef.createVarDashRef(removePrime(v), paramValues));
    		}
    	}
    	return o;
    }
    */
}
