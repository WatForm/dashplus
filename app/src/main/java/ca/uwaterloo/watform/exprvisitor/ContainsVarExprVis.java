/*
	Searches generically over all vars
	(could be a var derived from a param)

    Satisfying criteria is:
    varExpr instanceof AlloyQnameExpr
    && ((AlloyQnameExpr) varExpr).vars.equals(varToFind.vars)

*/

package ca.uwaterloo.watform.exprvisitor;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;

public class ContainsVarExprVis implements AlloyExprVis<Boolean> {

    private AlloyQnameExpr varToFind;

    public ContainsVarExprVis(AlloyQnameExpr v) {
        this.varToFind = v;
    }

    // ones from Dash
    public Boolean visit(DashRef dashRef) {

        return (dashRef.name == varToFind.label
                || !allFalse(mapBy(dashRef.paramValues, p -> this.visit(p))));
    }

    // ones from Dash
    public Boolean visit(DashParam dashParam) {
        return visit(dashParam.asAlloyVar());
    }

    public Boolean visit(AlloyBinaryExpr binExpr) {
        return visit(binExpr.left) || visit(binExpr.right);
    }

    public Boolean visit(AlloyUnaryExpr unaryExpr) {
        return visit(unaryExpr.sub);
    }

    public Boolean visit(AlloyVarExpr varExpr) {
        // TODO: not sure this is correct
        // since we are looking for a parameter variable
        // this is probably sufficient
        // but it is not general enough for other cases
        return (varExpr instanceof AlloyQnameExpr && varExpr.label.equals(this.varToFind.label));
    }

    public Boolean visit(AlloyBlock blockExpr) {
        return someTrue(mapBy(blockExpr.exprs, e -> this.visit(e)));
    }

    public Boolean visit(AlloyBracketExpr bracketExpr) {
        return someTrue(mapBy(bracketExpr.exprs, e -> this.visit(e)));
    }

    public Boolean visit(AlloyCphExpr comprehensionExpr) {
        return someTrue(mapBy(comprehensionExpr.decls, i -> this.visit(i)))
                || comprehensionExpr.body.map(b -> this.visit(b)).orElse(false);
    }

    public Boolean visit(AlloyIteExpr iteExpr) {
        return this.visit(iteExpr.cond) || this.visit(iteExpr.conseq) || this.visit(iteExpr.alt);
    }

    public Boolean visit(AlloyLetExpr letExpr) {
        return this.visit(letExpr.body);
    }

    public Boolean visit(AlloyQuantificationExpr quantificationExpr) {
        return this.visit(quantificationExpr.body);
    }

    public Boolean visit(AlloyDecl decl) {
        return this.visit(decl.expr);
    }
}
