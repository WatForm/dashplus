/*
    Apply test to each expression. If test is true, return result of "replace" function.
    Otherwise, spply visitors to subexpressions and return rebuild
	expressions.

	If nothing here is overwritten, it will return
	exactly the same expression.
*/

package ca.uwaterloo.watform.exprvisitor;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import java.util.*;
import java.util.function.Function;

public class ReplaceExprVis implements AlloyExprVis<AlloyExpr> {

    private Function<AlloyExpr, Boolean> test;
    private Function<AlloyExpr, AlloyExpr> replace;

    public ReplaceExprVis(
            Function<AlloyExpr, Boolean> test, Function<AlloyExpr, AlloyExpr> replace) {
        this.test = test;
        this.replace = replace;
    }

    @Override
    public AlloyExpr visit(DashRef dashRef) {
        if (this.test.apply(dashRef)) return this.replace.apply(dashRef);
        return dashRef;
    }

    /*
    @Override
    public AlloyExpr visit(DashParam dashParam) {
        if (this.test.apply(dashParam)) return this.replace.apply(dashParam);
        return dashParam;
    }
    */

    // all binary expressions
    @Override
    public AlloyExpr visit(AlloyBinaryExpr binExpr) {
        if (this.test.apply(binExpr)) return this.replace.apply(binExpr);
        else return binExpr.rebuild(this.visit(binExpr.left), this.visit(binExpr.right));
    }

    // all unary expressions
    @Override
    public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
        if (this.test.apply(unaryExpr)) return this.replace.apply(unaryExpr);
        else return unaryExpr.rebuild(this.visit(unaryExpr.sub));
    }

    @Override
    public AlloyExpr visit(AlloyVarExpr varExpr) {
        if (this.test.apply(varExpr)) return this.replace.apply(varExpr);
        else return varExpr;
    }

    // all the misc expr -------------------

    @Override
    public AlloyExpr visit(AlloyBlock block) {
        if (this.test.apply(block)) return this.replace.apply(block);
        else return block.rebuild(mapBy(block.exprs, i -> this.visit(i)));
    }

    @Override
    public AlloyExpr visit(AlloyBracketExpr bracketExpr) {
        if (this.test.apply(bracketExpr)) return this.replace.apply(bracketExpr);
        else {
            return bracketExpr.rebuild(
                    visit(bracketExpr.expr), mapBy(bracketExpr.exprs, i -> this.visit(i)));
        }
    }

    @Override
    public AlloyExpr visit(AlloyCphExpr comprehensionExpr) {
        if (this.test.apply(comprehensionExpr)) return this.replace.apply(comprehensionExpr);
        else {
            return comprehensionExpr.rebuild(
                    mapBy(comprehensionExpr.decls, i -> (AlloyDecl) this.visit(i)),
                    comprehensionExpr.body.map(value -> this.visit(value)).orElse(null));
        }
    }

    @Override
    public AlloyExpr visit(AlloyIteExpr iteExpr) {
        if (this.test.apply(iteExpr)) return this.replace.apply(iteExpr);
        else {
            return iteExpr.rebuild(
                    this.visit(iteExpr.cond), this.visit(iteExpr.conseq), this.visit(iteExpr.alt));
        }
    }

    @Override
    public AlloyExpr visit(AlloyLetExpr letExpr) {
        if (this.test.apply(letExpr)) return this.replace.apply(letExpr);
        // TODO: rule out var names that are bound
        else {
            return letExpr.rebuild(
                    mapBy(letExpr.asns, i -> i.rebuild(this.visit(i.expr))),
                    this.visit(letExpr.body));
        }
    }

    @Override
    public AlloyExpr visit(AlloyDecl decl) {
        if (this.test.apply(decl)) return this.replace.apply(decl);
        return decl.rebuild(this.visit(decl.expr));
    }

    @Override
    public AlloyExpr visit(AlloyQuantificationExpr quantificationExpr) {
        if (this.test.apply(quantificationExpr)) return this.replace.apply(quantificationExpr);
        else {
            return quantificationExpr.rebuild(
                    mapBy(quantificationExpr.decls, i -> i.rebuild(this.visit(i.expr))),
                    this.visit(quantificationExpr.body));
        }
    }

    @Override
    public AlloyExpr visit(AlloyParenExpr parenExpr) {
        if (this.test.apply(parenExpr)) return this.replace.apply(parenExpr);
        else {
            return parenExpr.rebuild(this.visit(parenExpr.sub));
        }
    }
}
