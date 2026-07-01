package ca.uwaterloo.watform.alloyevaluator;

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
    private final EvalLogger logger;

    public FormulaEvaluator(Instance instance, boolean debug) {
        logger = EvalLoggerFactory.make("evaluation", debug);
        setEvaluator = new SetEvaluator(instance, debug);
    }

    // These visit cases are unimplemented; just note the type and let the error carry the detail
    public Boolean visit(DashRef dashRef) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "DashRef: " + dashRef + " " + dashRef.getClass().getName());
    }

    public Boolean visit(AlloyBinaryExpr binExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyBinaryExpr: " + binExpr + " " + binExpr.getClass().getName());
    }

    public Boolean visit(AlloyUnaryExpr unaryExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyUnaryExpr: " + unaryExpr + " " + unaryExpr.getClass().getName());
    }

    public Boolean visit(AlloyVarExpr varExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyVarExpr: " + varExpr + " " + varExpr.getClass().getName());
    }

    public Boolean visit(AlloyBracketExpr bracketExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyBracketExpr: " + bracketExpr + " " + bracketExpr.getClass().getName());
    }

    public Boolean visit(AlloyCphExpr comprehensionExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyCphExpr: "
                        + comprehensionExpr
                        + " "
                        + comprehensionExpr.getClass().getName());
    }

    public Boolean visit(AlloyIteExpr iteExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyIteExpr: " + iteExpr + " " + iteExpr.getClass().getName());
    }

    public Boolean visit(AlloyLetExpr letExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyLetExpr: " + letExpr + " " + letExpr.getClass().getName());
    }

    public Boolean visit(AlloyQuantificationExpr quantificationExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyQuantificationExpr: "
                        + quantificationExpr
                        + " "
                        + quantificationExpr.getClass().getName());
    }

    public Boolean visit(AlloyDecl decl) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyDecl: " + decl + " " + decl.getClass().getName());
    }

    public Boolean visit(AlloyBlock block) {
        logger.enter("Block (" + block.exprs.size() + " exprs)");
        for (var expr : block.exprs) {
            if (!expr.accept(this)) {
                logger.exit("Block = false, failed on: " + expr);
                return false;
            }
        }
        logger.exit("Block = true");
        return true;
    }

    public Boolean visit(AlloyQtExpr qtExpr) {
        logger.enter("Multiplicity " + qtExpr.qt + ": " + qtExpr.sub);
        var set = qtExpr.sub.accept(setEvaluator);
        boolean result =
                switch (qtExpr.qt) {
                    case AlloyQtEnum.NO -> set.isEmpty();
                    case AlloyQtEnum.SOME -> !set.isEmpty();
                    case AlloyQtEnum.ONE -> set.size() == 1;
                    case AlloyQtEnum.LONE -> set.size() <= 1;
                    default ->
                            throw AlloyEvaluatorImplError.missingVisitCase(
                                    "AlloyQtEnum multiplicity: " + qtExpr.qt);
                };
        logger.exit("Multiplicity " + qtExpr.qt + " = " + result);
        return result;
    }

    public Boolean visit(AlloyEqualsExpr expr) {
        logger.enter("EQ: " + expr);
        var result = expr.left.accept(setEvaluator).equals(expr.right.accept(setEvaluator));
        logger.exit("EQ = " + result);
        return result;
    }

    public Boolean visit(AlloyNotEqualsExpr expr) {
        logger.enter("NEQ: " + expr);
        var result = !expr.left.accept(setEvaluator).equals(expr.right.accept(setEvaluator));
        logger.exit("NEQ = " + result);
        return result;
    }

    public Boolean visit(AlloyAndExpr expr) {
        logger.enter("AND: " + expr);
        if (!expr.left.accept(this)) {
            logger.exit("AND = false (short-circuit)");
            return false;
        }
        var result = expr.right.accept(this);
        logger.exit("AND = " + result);
        return result;
    }

    public Boolean visit(AlloyOrExpr expr) {
        logger.enter("OR: " + expr);
        if (expr.left.accept(this)) {
            logger.exit("OR = true (short-circuit)");
            return true;
        }
        var result = expr.right.accept(this);
        logger.exit("OR = " + result);
        return result;
    }

    public Boolean visit(AlloyImpliesExpr expr) {
        logger.enter("=>: " + expr);
        if (!expr.left.accept(this)) {
            logger.exit("=> = true (left false, short-circuit)");
            return true;
        }
        var result = expr.right.accept(this);
        logger.exit("=> = " + result);
        return result;
    }

    public Boolean visit(AlloyIffExpr expr) {
        logger.enter("<=>: " + expr);
        var result = expr.left.accept(this) == expr.right.accept(this);
        logger.exit("<=> = " + result);
        return result;
    }

    public Boolean visit(AlloyNegExpr expr) {
        logger.enter("NOT: " + expr);
        var result = !expr.sub.accept(this);
        logger.exit("NOT = " + result);
        return result;
    }

    public Boolean visit(AlloyCmpExpr expr) {
        logger.enter("CMP " + expr.comp + ": " + expr);
        var left = expr.left.accept(setEvaluator);
        var right = expr.right.accept(setEvaluator);
        boolean result =
                switch (expr.comp) {
                    case AlloyCmpExpr.Comp.IN -> right.containsAll(left);
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
                            throw AlloyEvaluatorImplError.missingVisitCase(
                                    "AlloyCmp comp: " + expr.comp);
                };
        if (expr.neg) result = !result;
        logger.exit("CMP " + expr.comp + " = " + result);
        return result;
    }
}
