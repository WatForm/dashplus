package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.utils.CommonStrings.*;

import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import java.util.Set;

public class SetEvaluator implements AlloyExprVis<Set<List<String>>> {
    private final Instance instance;

    public SetEvaluator(Instance instance) {
        this.instance = instance;
    }

    public Set<List<String>> visit(DashRef dashRef) {
        dashOutput("Visiting: " + dashRef.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("DashRef: " + dashRef);
    }

    public Set<List<String>> visit(AlloyBinaryExpr binExpr) {
        dashOutput("Visiting: " + binExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyBinaryExpr: " + binExpr);
    }

    public Set<List<String>> visit(AlloyUnaryExpr unaryExpr) {
        dashOutput("Visiting: " + unaryExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyUnaryExpr: " + unaryExpr);
    }

    public Set<List<String>> visit(AlloyVarExpr varExpr) {
        dashOutput("Visiting: " + varExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyVarExpr: " + varExpr + " " + varExpr.getClass().getName());
    }

    public Set<List<String>> visit(AlloyBlock block) {
        dashOutput("Visiting: " + block.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyBlock: " + block);
    }

    public Set<List<String>> visit(AlloyBracketExpr bracketExpr) {
        dashOutput("Visiting: " + bracketExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyBracketExpr: " + bracketExpr);
    }

    public Set<List<String>> visit(AlloyCphExpr comprehensionExpr) {
        dashOutput("Visiting: " + comprehensionExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyCphExpr: " + comprehensionExpr);
    }

    public Set<List<String>> visit(AlloyIteExpr iteExpr) {
        dashOutput("Visiting: " + iteExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyIteExpr: " + iteExpr);
    }

    public Set<List<String>> visit(AlloyLetExpr letExpr) {
        dashOutput("Visiting: " + letExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyLetExpr: " + letExpr);
    }

    public Set<List<String>> visit(AlloyQuantificationExpr quantificationExpr) {
        dashOutput("Visiting: " + quantificationExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyQuantificationExpr: " + quantificationExpr);
    }

    public Set<List<String>> visit(AlloyDecl decl) {
        dashOutput("Visiting: " + decl.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyDecl: " + decl);
    }

    public Set<List<String>> visit(AlloyQnameExpr qName) {
        dashOutput("Evaluating qname expr: " + qName + " " + qName.vars);
        if (qName.vars.isEmpty()) {
            throw AlloyEvaluatorImplError.notSupported("A variable must exist to evaluate it");
        } else {
            var result = instance.getRelation(qName.vars.getLast().label);
            if (result.isEmpty()) {
                throw AlloyEvaluatorImplError.relationNotInInstance(qName.vars.getLast().label);
            }
            dashOutput("AlloyQnameExpr evaluated to: " + result.get());
            return result.get();
        }
    }

    public Set<List<String>> visit(AlloyUnionExpr expr) {
        dashOutput("Evaluating union expr: " + expr);
        var left = expr.left.accept(this);
        var right = expr.right.accept(this);
        var result = GeneralUtil.mergeSets(left, right);
        dashOutput("Union Evaluated to: " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyNoneExpr expr) {
        dashOutput("Evaluating none expr: " + expr);
        return GeneralUtil.emptySet();
    }

    public Set<List<String>> visit(AlloyIntersExpr expr) {
        dashOutput("Evaluating intersect expr: " + expr);
        var left = expr.left.accept(this);
        var right = expr.right.accept(this);
        var result = GeneralUtil.intersectSets(left, right);
        dashOutput("Intersect Evaluated to: " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyDiffExpr expr) {
        dashOutput("Evaluating diff expr: " + expr);
        var left = expr.left.accept(this);
        var right = expr.right.accept(this);
        var result = GeneralUtil.diffSets(left, right);
        dashOutput("Diff Evaluated to: " + result);
        return result;
    }
}
