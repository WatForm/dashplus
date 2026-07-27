/*
	Searches over an AlloyExpr and returns 'collect' at every node.
    All recursive calls are handled here.
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

public class CollectExprVis<T> implements AlloyExprVis<Set<T>> {

    private Function<AlloyExpr, Set<T>> collect;

    public CollectExprVis(Function<AlloyExpr, Set<T>> collect) {
        // collect cannot call visitor (so any recursive calls are here in visitor)
        this.collect = collect;
    }

    // expr.binary.*
    public Set<T> visit(AlloyBinaryExpr binExpr) {
        return mergeSets(this.collect.apply(binExpr), visit(binExpr.left), visit(binExpr.right));
    }

    // expr.unary.*
    public Set<T> visit(AlloyUnaryExpr unaryExpr) {
        return mergeSets(this.collect.apply(unaryExpr), visit(unaryExpr.sub));
    }

    /// expr.misc.*
    public Set<T> visit(AlloyBlock blockExpr) {
        return mergeSets(
                this.collect.apply(blockExpr),
                flatten(mapBy(listToSet(blockExpr.exprs), e -> this.visit(e))));
    }

    public Set<T> visit(AlloyBracketExpr bracketExpr) {
        return mergeSets(
                this.collect.apply(bracketExpr),
                this.visit(bracketExpr.expr),
                flatten(mapBy(listToSet(bracketExpr.exprs), e -> this.visit(e))));
    }

    public Set<T> visit(AlloyCphExpr comprehensionExpr) {
        if (comprehensionExpr.body.isPresent()) {
            return mergeSets(
                    this.collect.apply(comprehensionExpr),
                    flatten(mapBy(listToSet(comprehensionExpr.decls), i -> this.visit(i))),
                    this.visit(comprehensionExpr.body.get()));
        } else
            return mergeSets(
                    this.collect.apply(comprehensionExpr),
                    flatten(mapBy(listToSet(comprehensionExpr.decls), i -> this.visit(i))));
    }

    public Set<T> visit(AlloyIteExpr iteExpr) {
        return mergeSets(
                this.collect.apply(iteExpr),
                this.visit(iteExpr.cond),
                mergeSets(this.visit(iteExpr.conseq), this.visit(iteExpr.alt)));
    }

    public Set<T> visit(AlloyLetExpr letExpr) {
        return mergeSets(
                this.collect.apply(letExpr),
                flatten(mapBy(listToSet(letExpr.asns), a -> this.visit(a.expr))),
                this.visit(letExpr.body));
    }

    public Set<T> visit(AlloyDecl decl) {
        return mergeSets(this.collect.apply(decl), this.visit(decl.expr));
    }

    public Set<T> visit(AlloyQuantificationExpr quantificationExpr) {
        return mergeSets(
                this.collect.apply(quantificationExpr),
                flatten(mapBy(listToSet(quantificationExpr.decls), d -> this.visit(d))),
                this.visit(quantificationExpr.body));
    }

    public Set<T> visit(AlloyParenExpr parenExpr) {
        return mergeSets(this.collect.apply(parenExpr), this.visit(parenExpr.sub));
    }

    // expr.var.*
    // no recursion
    public Set<T> visit(AlloyVarExpr varExpr) {
        return this.collect.apply(varExpr);
    }

    public Set<T> visit(DashRef dashRef) {
        // System.out.println(this.toString());
        return this.collect.apply(dashRef);
    }
}
