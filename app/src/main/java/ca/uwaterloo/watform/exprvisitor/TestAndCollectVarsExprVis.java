/*
	Searches over an AlloyExpr and returns the names of all leaf AlloyQnameExpr that satisfy a test.

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

public class TestAndCollectVarsExprVis implements AlloyExprVis<Set<String>> {

    private Function<AlloyExpr, Boolean> test;

    public TestAndCollectVarsExprVis(Function<AlloyExpr, Boolean> test) {
        this.test = test;
    }

    // common instance: get all Qnames in an expression
    public static Set<String> getAlloyQnameExprNames(AlloyExpr expr) {
        TestAndCollectVarsExprVis vis = new TestAndCollectVarsExprVis(e -> true);
        return vis.visit(expr);
    }

    public Set<String> visit(DashRef dashRef) {
        // System.out.println(this.toString());
        return flatten(mapBy(listToSet(dashRef.paramValues), p -> this.visit(p)));
    }

    /*
    public Set<String> visit(DashParam dashParam) {
        return visit(dashParam.asAlloyVar());
    }
    */

    public Set<String> visit(AlloyBinaryExpr binExpr) {
        return mergeSets(visit(binExpr.left), visit(binExpr.right));
    }

    public Set<String> visit(AlloyUnaryExpr unaryExpr) {
        return visit(unaryExpr.sub);
    }

    public Set<String> visit(AlloyVarExpr varExpr) {
        // TODO: not sure this is correct
        // since we are looking for a parameter variable
        // this is probably sufficient
        // but it is not general enough for other cases
        // System.out.println(varExpr.toString());
        if (varExpr instanceof AlloyQnameExpr) {

            // System.out.println(this.test.apply(((AlloyQnameExpr) varExpr)));
            if (this.test.apply(((AlloyQnameExpr) varExpr))) return Set.of(varExpr.getName());
            else return emptySet();
        } else return emptySet();
    }

    public Set<String> visit(AlloyBlock blockExpr) {
        return flatten(mapBy(listToSet(blockExpr.exprs), e -> this.visit(e)));
    }

    public Set<String> visit(AlloyBracketExpr bracketExpr) {
        for (AlloyExpr e : bracketExpr.exprs) {
            // System.out.println("brackeExpr");
            // System.out.println(e);
            // System.out.println(e.getClass());
            // System.out.println("---");
        }
        return mergeSets(
                this.visit(bracketExpr.expr),
                flatten(mapBy(listToSet(bracketExpr.exprs), e -> this.visit(e))));
    }

    public Set<String> visit(AlloyCphExpr comprehensionExpr) {
        if (comprehensionExpr.body.isPresent()) {
            return mergeSets(
                    flatten(mapBy(listToSet(comprehensionExpr.decls), i -> this.visit(i))),
                    this.visit(comprehensionExpr.body.get()));
        } else return flatten(mapBy(listToSet(comprehensionExpr.decls), i -> this.visit(i)));
    }

    public Set<String> visit(AlloyIteExpr iteExpr) {
        return mergeSets(
                this.visit(iteExpr.cond),
                mergeSets(this.visit(iteExpr.conseq), this.visit(iteExpr.alt)));
    }

    public Set<String> visit(AlloyLetExpr letExpr) {
        return mergeSets(
                flatten(mapBy(listToSet(letExpr.asns), a -> this.visit(a.expr))),
                this.visit(letExpr.body));
    }

    public Set<String> visit(AlloyDecl decl) {
        return this.visit(decl.expr);
    }

    public Set<String> visit(AlloyQuantificationExpr quantificationExpr) {
        return mergeSets(
                flatten(mapBy(listToSet(quantificationExpr.decls), d -> this.visit(d))),
                this.visit(quantificationExpr.body));
    }

    public Set<String> visit(AlloyParenExpr parenExpr) {
        return this.visit(parenExpr.sub);
    }
}
