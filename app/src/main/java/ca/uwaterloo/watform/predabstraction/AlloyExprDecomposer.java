package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import java.util.*;

public class AlloyExprDecomposer implements AlloyExprVis<Void> {

    private Set<AlloyExpr> subexprs;

    public AlloyExprDecomposer() {
        this.subexprs = new HashSet<AlloyExpr>();
    }

    public Set<AlloyExpr> decompose(AlloyExpr e) {
        if (e != null) {
            this.visit(e);
        }
        return this.subexprs;
    }

    @Override
    public Void visit(DashRef dashRef) {
        subexprs.add(dashRef);
        return null;
    }

    @Override
    public Void visit(AlloyPrimeExpr expr) {
        subexprs.add(expr);
        return null;
    }

    // ones from Dash

    @Override
    public Void visit(DashParam dashParam) {
        subexprs.add(dashParam.asAlloyVar());
        return null;
    }
    ;

    @Override
    public Void visit(AlloyVarExpr varExpr) {
        subexprs.add(varExpr);
        return null;
    }
    ;

    // below this line are recursive ones

    @Override
    public Void visit(AlloyBinaryExpr binExpr) {
        // List<String> binOps = List.of(AND, AND_AMP, OR, OR_BAR, RFATARROW, IMPLIES, IFF,
        // IFF_ARR);
        // if (binOps.contains(binExpr.op)) {
        //     subexprs.add(this.visit(binExpr.left));
        //     subexprs.add(this.visit(binExpr.right));
        //     return binExpr.rebuild(binExpr.left, binExpr.right);
        // } else {
        //     return binExpr;
        // }
        if (binExpr instanceof AlloyAndExpr
                || binExpr instanceof AlloyOrExpr
                || binExpr instanceof AlloyImpliesExpr
                || binExpr instanceof AlloyIffExpr) {
            this.visit(binExpr.left);
            this.visit(binExpr.right); // just do this.visit
        } else {
            subexprs.add(binExpr);
        }
        return null;
    }
    ;

    @Override
    public Void visit(AlloyUnaryExpr unaryExpr) {
        if (unaryExpr instanceof AlloyNegExpr) {
            this.visit(unaryExpr.sub);
        } else {
            subexprs.add(unaryExpr);
        }
        return null;
    }
    ;

    // misc exprs

    @Override
    public Void visit(AlloyBlock block) {
        for (AlloyExpr e : block.exprs) {
            this.visit(e);
        }
        return null;
    }
    ;

    @Override
    public Void visit(AlloyBracketExpr bracketExpr) {
        subexprs.add(bracketExpr);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyCphExpr comprehensionExpr) {
        subexprs.add(comprehensionExpr);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyDecl decl) {
        subexprs.add(decl);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyIteExpr iteExpr) {
        this.visit(iteExpr.cond);
        this.visit(iteExpr.conseq);
        this.visit(iteExpr.alt);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyLetExpr letExpr) {
        this.visit(letExpr.body);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyParenExpr parenExpr) {
        this.visit(parenExpr.sub);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyQuantificationExpr quantificationExpr) {
        this.visit(quantificationExpr.body);
        return null;
    }
    ;
}
