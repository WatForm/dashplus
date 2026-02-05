/*
    one instance of CollectDashRef
    for each expression that we need to collect
    DashRef from.

    implementation of AlloyExprVis

    The DashRef might be "next" or not.

*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import java.util.Set;

// Void (rather than void) because Void is a class, but then all methods must return null

public class CollectDashRefVis implements AlloyExprVis<Void> {

    Set<DashRef> x = emptySet();

    public Set<DashRef> collect(AlloyExpr expr) {
        this.x = emptySet();
        this.visit(expr);
        return x;
    }

    @Override
    public Void visit(AlloyBinaryExpr binExpr) {
        visit(binExpr.left);
        visit(binExpr.right);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyBlock block) {
        for (AlloyExpr expr : block.exprs) {
            visit(expr);
        }
        return null;
    }
    ;

    @Override
    public Void visit(AlloyBracketExpr bracketExpr) {
        visit(bracketExpr.expr);
        for (AlloyExpr expr : bracketExpr.exprs) {
            visit(expr);
        }
        return null;
    }
    ;

    @Override
    public Void visit(AlloyCphExpr comprehensionExpr) {
        comprehensionExpr.body.ifPresent(value -> visit(value));
        return null;
    }
    ;

    @Override
    public Void visit(AlloyIteExpr iteExpr) {
        visit(iteExpr.cond);
        visit(iteExpr.conseq);
        visit(iteExpr.alt);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyLetExpr letExpr) {
        visit(letExpr.body);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyParenExpr parenExpr) {
        visit(parenExpr.sub);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyQuantificationExpr qtExpr) {
        visit(qtExpr.body);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyUnaryExpr unaryExpr) {
        visit(unaryExpr.sub);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyVarExpr varExpr) {
        return null;
    }
    ;

    @Override
    public Void visit(AlloyDecl decl) {
        visit(decl.expr);
        return null;
    }
    ;

    public Void visit(DashRef d) {
        x.add(d);
        return null;
    }

    public Void visit(DashParam dashParam) {
        return null;
    }
}
