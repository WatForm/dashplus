package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.utils.CommonStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
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

public class FormulaEvaluator implements AlloyExprVis<Boolean> {
    private final AlloyExprVis<Set<List<String>>> setEvaluator;

    public FormulaEvaluator(Instance instance) {
        setEvaluator = new SetEvaluator(instance);
    }

    public Boolean visit(DashRef dashRef) {
        dashOutput("Visiting: " + dashRef.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("DashRef: " + dashRef);
    }

    public Boolean visit(AlloyBinaryExpr binExpr) {
        dashOutput("Visiting: " + binExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyBinaryExpr: " + binExpr);
    }

    public Boolean visit(AlloyUnaryExpr unaryExpr) {
        dashOutput("Visiting: " + unaryExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyUnaryExpr: " + unaryExpr);
    }

    public Boolean visit(AlloyVarExpr varExpr) {
        dashOutput("Visiting: " + varExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyVarExpr: " + varExpr);
    }

    public Boolean visit(AlloyBracketExpr bracketExpr) {
        dashOutput("Visiting: " + bracketExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyBracketExpr: " + bracketExpr);
    }

    public Boolean visit(AlloyCphExpr comprehensionExpr) {
        dashOutput("Visiting: " + comprehensionExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyCphExpr: " + comprehensionExpr);
    }

    public Boolean visit(AlloyIteExpr iteExpr) {
        dashOutput("Visiting: " + iteExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyIteExpr: " + iteExpr);
    }

    public Boolean visit(AlloyLetExpr letExpr) {
        dashOutput("Visiting: " + letExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyLetExpr: " + letExpr);
    }

    public Boolean visit(AlloyQuantificationExpr quantificationExpr) {
        dashOutput("Visiting: " + quantificationExpr.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyQuantificationExpr: " + quantificationExpr);
    }

    public Boolean visit(AlloyDecl decl) {
        dashOutput("Visiting: " + decl.getClass().getName());
        throw AlloyEvaluatorImplError.missingVisitCase("AlloyDecl: " + decl);
    }

    public Boolean visit(AlloyBlock block) {
        dashOutput("Evaluating block: " + block);
        for (var expr : block.exprs) {
            dashOutput("Evaluating expr: " + expr);
            if (!expr.accept(this)) {
                dashOutput("The expression evaluated to false: " + expr);
                return false;
            }
        }
        return true;
    }

    public Boolean visit(AlloyQtExpr qtExpr) {
        switch (qtExpr.qt) {
            case AlloyQtEnum.NO -> {
                dashOutput("Multiplicity No case: " + qtExpr.sub);
                return qtExpr.sub.accept(setEvaluator).isEmpty();
            }
            case AlloyQtEnum.SOME -> {
                dashOutput("Multiplicity Some case: " + qtExpr.sub);
                return !qtExpr.sub.accept(setEvaluator).isEmpty();
            }
            case AlloyQtEnum.ONE -> {
                dashOutput("Multiplicity One case: " + qtExpr.sub);
                return qtExpr.sub.accept(setEvaluator).size() == 1;
            }
            case AlloyQtEnum.LONE -> {
                dashOutput("Multiplicity Lone case: " + qtExpr.sub);
                return qtExpr.sub.accept(setEvaluator).size() <= 1;
            }
            default ->
                    throw AlloyEvaluatorImplError.missingVisitCase(
                            "AlloyQtEnum multiplicity: " + qtExpr.qt);
        }
    }

    public Boolean visit(AlloyEqualsExpr expr) {
        dashOutput("Evaluating equals: " + expr);
        var leftVal = expr.left.accept(setEvaluator);
        var rightVal = expr.right.accept(setEvaluator);
        var result = leftVal.equals(rightVal);
        dashOutput("Evalutated to " + String.valueOf(result));
        return result;
    }

    public Boolean visit(AlloyNotEqualsExpr expr) {
        dashOutput("Evaluating not equals: " + expr);
        var leftVal = expr.left.accept(setEvaluator);
        var rightVal = expr.right.accept(setEvaluator);
        var result = !leftVal.equals(rightVal);
        dashOutput("not equals Evalutated to " + String.valueOf(result));
        return result;
    }

    public Boolean visit(AlloyAndExpr expr) {
        dashOutput("Evaluating and: " + expr);
        var leftVal = expr.left.accept(this);
        if (!leftVal) {
            dashOutput("Short circuit and Evalutated to false");
            return false;
        }
        var rightVal = expr.right.accept(this);
        var result = leftVal && rightVal;
        dashOutput("and Evalutated to " + String.valueOf(result));
        return result;
    }

    public Boolean visit(AlloyOrExpr expr) {
        dashOutput("Evaluating or: " + expr);
        var leftVal = expr.left.accept(this);
        if (leftVal) {
            dashOutput("Short circuit or Evalutated to true");
            return true;
        }
        var rightVal = expr.right.accept(this);
        var result = leftVal || rightVal;
        dashOutput("or Evalutated to " + String.valueOf(result));
        return result;
    }

    public Boolean visit(AlloyImpliesExpr expr) {
        dashOutput("Evaluating =>: " + expr);
        var leftVal = expr.left.accept(this);
        if (!leftVal) {
            dashOutput("Short circuit => Evalutated to true");
            return true;
        }
        var rightVal = expr.right.accept(this);
        var result = !leftVal || rightVal;
        dashOutput("=> Evalutated to " + String.valueOf(result));
        return result;
    }

    public Boolean visit(AlloyIffExpr expr) {
        dashOutput("Evaluating <=>: " + expr);
        var leftVal = expr.left.accept(this);
        var rightVal = expr.right.accept(this);
        var result = leftVal == rightVal;
        dashOutput("<=> Evalutated to " + String.valueOf(result));
        return result;
    }

    public Boolean visit(AlloyNegExpr expr) {
        dashOutput("Evaluating neg: " + expr);
        var result = !expr.sub.accept(this);
        dashOutput("not Evalutated to " + String.valueOf(result));
        return result;
    }

    public Boolean visit(AlloyCmpExpr expr) {
        dashOutput("Evaluating cmp expr: " + expr);
        dashOutput("Evaluating cmp expr.neg: " + expr.neg);

        if (expr.neg) {
            throw new UnsupportedOperationException("Neg not implemented currently");
        }

        var left = expr.left.accept(setEvaluator);
        var right = expr.right.accept(setEvaluator);

        switch (expr.comp) {
            case AlloyCmpExpr.Comp.IN -> {
                var result = right.containsAll(left);
                dashOutput("Comp Evaluated to: " + result);
                return result;
            }
            case AlloyCmpExpr.Comp.LESS_THAN ->
                    throw new UnsupportedOperationException("Comp.LT not implemented");
            case AlloyCmpExpr.Comp.GREATER_THAN ->
                    throw new UnsupportedOperationException("Comp.GT not implemented");
            case AlloyCmpExpr.Comp.LESS_EQUAL ->
                    throw new UnsupportedOperationException("Comp.LE not implemented");
            case AlloyCmpExpr.Comp.EQUAL_LESS ->
                    throw new UnsupportedOperationException("Comp.EL not implemented");
            case AlloyCmpExpr.Comp.GREATER_EQUAL ->
                    throw new UnsupportedOperationException("Comp.GE not implemented");
            default ->
                    throw AlloyEvaluatorImplError.missingVisitCase("AlloyCmp comp: " + expr.comp);
        }
    }
}
