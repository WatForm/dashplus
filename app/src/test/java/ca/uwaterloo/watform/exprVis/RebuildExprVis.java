package ca.uwaterloo.watform.exprVis;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;

public final class RebuildExprVis implements AlloyExprVis<AlloyExpr> {
    @Override
    public AlloyExpr visit(AlloyBinaryExpr binExpr) {
        AlloyExpr l = new AlloyNameExpr("binLeft");
        AlloyExpr r = new AlloyNameExpr("binRight");
        return binExpr.rebuild(binExpr.pos, l, r);
    }

    @Override
    public AlloyExpr visit(AlloyAndExpr andExpr) {
        AlloyExpr l = new AlloyNameExpr("andLeft");
        AlloyExpr r = new AlloyNameExpr("andRight");
        return andExpr.rebuild(andExpr.pos, l, r);
    }

    @Override
    public AlloyExpr visit(AlloyArrowExpr arrowExpr) {
        AlloyExpr l = new AlloyNameExpr("arrowLeft");
        AlloyExpr r = new AlloyNameExpr("arrowRight");
        return new AlloyArrowExpr(l, arrowExpr.mul1, AlloyArrowExpr.Mul.ONE, r);
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
    public AlloyExpr visit(AlloyComprehensionExpr comprehensionExpr) {
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
}
