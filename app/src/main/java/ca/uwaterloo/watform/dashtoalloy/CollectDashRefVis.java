/*
    one instance of CollectDashRef
    for each expression that we need to collect
    DashRef from.

    implementation of DashExprVis

    The DashRef might be primed or unprimed.

    Can only be called after refs are resolved.
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
import java.util.ArrayList;
import java.util.List;

public class CollectDashRefVis implements AlloyExprVis<List<DashRef>> {

    List<DashRef> x = new ArrayList<DashRef>();

    @Override
    public List<DashRef> visit(AlloyBinaryExpr binExpr) {
        x.addAll(visit(binExpr.left));
        x.addAll(visit(binExpr.right));
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyBlock block) {
        for (AlloyExpr expr : block.exprs) {
            x.addAll(visit(expr));
        }
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyBracketExpr bracketExpr) {
        x.addAll(visit(bracketExpr.expr));
        for (AlloyExpr expr : bracketExpr.exprs) {
            x.addAll(visit(expr));
        }
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyCphExpr comprehensionExpr) {
        comprehensionExpr.body.ifPresent(value -> x.addAll(visit(value)));
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyIteExpr iteExpr) {
        x.addAll(visit(iteExpr.cond));
        x.addAll(visit(iteExpr.conseq));
        x.addAll(visit(iteExpr.alt));
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyLetExpr letExpr) {
        x.addAll(visit(letExpr.body));
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyParenExpr parenExpr) {
        x.addAll(visit(parenExpr.sub));
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyQuantificationExpr qtExpr) {
        x.addAll(visit(qtExpr.body));
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyUnaryExpr unaryExpr) {
        x.addAll(visit(unaryExpr.sub));
        return x;
    }
    ;

    @Override
    public List<DashRef> visit(AlloyVarExpr varExpr) {
        return emptyList();
    }
    ;

    @Override
    public List<DashRef> visit(AlloyDecl decl) {
        return emptyList();
    }
    ;

    public List<DashRef> visit(DashRef d) {
        x.add(d);
        return x;
    }

    public List<DashRef> visit(DashParam dashParam) {
        return x;
    }
}
