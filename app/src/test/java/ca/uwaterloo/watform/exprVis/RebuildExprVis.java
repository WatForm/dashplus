package ca.uwaterloo.watform.exprVis;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;

public final class RebuildExprVis implements AlloyExprVis<AlloyExpr> {
    @Override
    public AlloyExpr visit(AlloyBinaryExpr binExpr) {
        AlloyExpr l = new AlloyQnameExpr("binLeft");
        AlloyExpr r = new AlloyQnameExpr("binRight");
        return binExpr.rebuild(l, r);
    }

    @Override
    public AlloyExpr visit(AlloyAndExpr andExpr) {
        AlloyExpr l = new AlloyQnameExpr("andLeft");
        AlloyExpr r = new AlloyQnameExpr("andRight");
        return andExpr.rebuild(l, r);
    }

    @Override
    public AlloyExpr visit(AlloyArrowExpr arrowExpr) {
        AlloyExpr l = new AlloyQnameExpr("arrowLeft");
        AlloyExpr r = new AlloyQnameExpr("arrowRight");
        return new AlloyArrowExpr(l, arrowExpr.mul1.orElse(null), AlloyArrowExpr.Mul.ONE, r);
    }

    @Override
    public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
        return null;
    }

    @Override
    public AlloyExpr visit(AlloyVarExpr varExpr) {
        return null;
    }

    @Override
    public AlloyExpr visit(AlloyBlock block) {
        return null;
    }

    @Override
    public AlloyExpr visit(AlloyBracketExpr bracketExpr) {
        return null;
    }

    @Override
    public AlloyExpr visit(AlloyCphExpr comprehensionExpr) {
        return null;
    }

    @Override
    public AlloyExpr visit(AlloyIteExpr iteExpr) {
        return null;
    }

    @Override
    public AlloyExpr visit(AlloyLetExpr letExpr) {
        return null;
    }

    @Override
    public AlloyExpr visit(AlloyQuantificationExpr quantificationExpr) {
        return null;
    }

    @Override
    public AlloyExpr visit(AlloyDecl decl) {
        return null;
    }

    @Override
    public AlloyExpr visit(DashRef dashRef) {
        return null;
    }

    @Override
    public AlloyExpr visit(DashParam dashParam) {
        return null;
    }
}
