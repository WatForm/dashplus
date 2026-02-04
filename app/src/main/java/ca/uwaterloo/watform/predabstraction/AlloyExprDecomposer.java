package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import java.util.*;

public class AlloyExprDecomposer implements AlloyExprVis<AlloyExpr> {

    private Set<AlloyExpr> subexprs;

    public Set<AlloyExpr> decompose(AlloyExpr e) {
        subexprs = new HashSet<AlloyExpr>();
        this.visit(e);
        return subexprs;
    }

    @Override
    public AlloyExpr visit(DashRef dashRef) {
        subexprs.add(dashRef);
        return (AlloyExpr) dashRef;
    }

    @Override
    public AlloyExpr visit(AlloyPrimeExpr expr) {
        subexprs.add(expr);
        return expr;
    }

    // ones from Dash

    @Override
    public AlloyExpr visit(DashParam dashParam) {
        subexprs.add(dashParam.asAlloyVar());
        return dashParam.asAlloyVar();
    }
    ;

    @Override
    public AlloyExpr visit(AlloyVarExpr varExpr) {
        subexprs.add(varExpr);
        return varExpr;
    }
    ;

    // below this line are recursive ones

    @Override
    public AlloyExpr visit(AlloyBinaryExpr binExpr) {
        subexprs.add(this.visit(binExpr.left));
        subexprs.add(this.visit(binExpr.right));
        return binExpr.rebuild(binExpr.left, binExpr.right);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
        if (unaryExpr instanceof AlloyNegExpr) subexprs.add(this.visit(unaryExpr.sub));
        else subexprs.add(unaryExpr);
        return unaryExpr.rebuild(unaryExpr.sub);
    }
    ;

    // misc exprs

    @Override
    public AlloyExpr visit(AlloyBlock block) {
        subexprs.add(block);
        return block;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyBracketExpr bracketExpr) {
        subexprs.add(bracketExpr);
        return bracketExpr;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyCphExpr comprehensionExpr) {
        subexprs.add(comprehensionExpr);
        return comprehensionExpr;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyDecl decl) {
        subexprs.add(decl);
        return decl;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyIteExpr iteExpr) {
        subexprs.add(this.visit(iteExpr.cond));
        subexprs.add(this.visit(iteExpr.conseq));
        subexprs.add(this.visit(iteExpr.alt));
        return iteExpr;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyLetExpr letExpr) {
        subexprs.add(this.visit(letExpr.body));
        return letExpr;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyParenExpr parenExpr) {
        subexprs.add(this.visit(parenExpr.sub));
        return parenExpr;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyQuantificationExpr quantificationExpr) {
        subexprs.add(this.visit(quantificationExpr.body));
        return quantificationExpr;
    }
    ;
}
