package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.alloyevaluator.ThreeVal.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.firstElement;
import static ca.uwaterloo.watform.utils.GeneralUtil.setToList;

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
import java.util.function.BiPredicate;

public class FormulaEvaluator implements AlloyExprVis<ThreeVal> {
    private final AlloyExprVis<Set<List<Atom>>> setEvaluator;
    private final EvalLogger logger;

    public FormulaEvaluator(Instance instance, boolean debug) {
        logger = EvalLoggerFactory.make("evaluation", debug);
        setEvaluator = new SetEvaluator(instance, debug);
    }

    // These visit cases are unimplemented; just note the type and let the error carry the detail
    public ThreeVal visit(DashRef dashRef) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "DashRef: " + dashRef + " " + dashRef.getClass().getName());
    }

    public ThreeVal visit(AlloyBinaryExpr binExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyBinaryExpr: " + binExpr + " " + binExpr.getClass().getName());
    }

    public ThreeVal visit(AlloyUnaryExpr unaryExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyUnaryExpr: " + unaryExpr + " " + unaryExpr.getClass().getName());
    }

    public ThreeVal visit(AlloyVarExpr varExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyVarExpr: " + varExpr + " " + varExpr.getClass().getName());
    }

    public ThreeVal visit(AlloyBracketExpr bracketExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyBracketExpr: " + bracketExpr + " " + bracketExpr.getClass().getName());
    }

    public ThreeVal visit(AlloyCphExpr comprehensionExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyCphExpr: "
                        + comprehensionExpr
                        + " "
                        + comprehensionExpr.getClass().getName());
    }

    public ThreeVal visit(AlloyIteExpr iteExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyIteExpr: " + iteExpr + " " + iteExpr.getClass().getName());
    }

    public ThreeVal visit(AlloyLetExpr letExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyLetExpr: " + letExpr + " " + letExpr.getClass().getName());
    }

    public ThreeVal visit(AlloyQuantificationExpr quantificationExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyQuantificationExpr: "
                        + quantificationExpr
                        + " "
                        + quantificationExpr.getClass().getName());
    }

    public ThreeVal visit(AlloyDecl decl) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyDecl: " + decl + " " + decl.getClass().getName());
    }

    public ThreeVal visit(AlloyBlock block) {
        logger.enter("Block (" + block.exprs.size() + " exprs)");
        for (var expr : block.exprs) {
            var result = expr.accept(this);
            if (result != TRUE) {
                logger.exit("Expr evaluated to: " + result);
                return result;
            }
        }
        logger.exit("Block = " + TRUE);
        return TRUE;
    }

    // TODO: subset/set semantics for three values maybe (this one is probably fine)
    public ThreeVal visit(AlloyQtExpr qtExpr) {
        logger.enter("Multiplicity " + qtExpr.qt + ": " + qtExpr.sub);
        var set = qtExpr.sub.accept(setEvaluator);
        var result =
                convertThree(
                        switch (qtExpr.qt) {
                            case AlloyQtEnum.NO -> set.isEmpty();
                            case AlloyQtEnum.SOME -> !set.isEmpty();
                            case AlloyQtEnum.ONE -> set.size() == 1;
                            case AlloyQtEnum.LONE -> set.size() <= 1;
                            default ->
                                    throw AlloyEvaluatorImplError.missingVisitCase(
                                            "AlloyQtEnum multiplicity: " + qtExpr.qt);
                        });
        logger.exit("Multiplicity " + qtExpr.qt + " = " + result);
        return result;
    }

    // TODO: subset/set semantics for three values logic
    public ThreeVal visit(AlloyEqualsExpr expr) {
        logger.enter("EQ: " + expr);
        var result =
                convertThree(
                        expr.left.accept(setEvaluator).equals(expr.right.accept(setEvaluator)));
        logger.exit("EQ = " + result);
        return result;
    }

    // TODO: subset/set semantics for three values logic
    public ThreeVal visit(AlloyNotEqualsExpr expr) {
        logger.enter("NEQ: " + expr);
        var result =
                convertThree(
                        !expr.left.accept(setEvaluator).equals(expr.right.accept(setEvaluator)));
        logger.exit("NEQ = " + result);
        return result;
    }

    public ThreeVal visit(AlloyAndExpr expr) {
        logger.enter("AND: " + expr);
        var leftRes = expr.left.accept(this);
        if (leftRes.shortCircuitsAnd()) {
            logger.exit("AND = " + shortCircuitAndResult() + " (short-circuit)");
            return shortCircuitAndResult();
        }
        var result = leftRes.and(expr.right.accept(this));
        logger.exit("AND = " + result);
        return result;
    }

    public ThreeVal visit(AlloyOrExpr expr) {
        logger.enter("OR: " + expr);
        var leftRes = expr.left.accept(this);
        if (leftRes.shortCircuitsOr()) {
            logger.exit("OR = " + shortCircuitOrResult() + " (short-circuit)");
            return shortCircuitOrResult();
        }
        var result = leftRes.or(expr.right.accept(this));
        logger.exit("OR = " + result);
        return result;
    }

    public ThreeVal visit(AlloyImpliesExpr expr) {
        logger.enter("IMPL: " + expr);
        var leftRes = expr.left.accept(this);
        if (leftRes.shortCircuitImpl()) {
            logger.exit("IMPL = " + shortCircuitImplResult() + " (short-circuit)");
            return shortCircuitImplResult();
        }
        var result = leftRes.impl(expr.right.accept(this));
        logger.exit("IMPL = " + result);
        return result;
    }

    public ThreeVal visit(AlloyIffExpr expr) {
        logger.enter("IFF: " + expr);
        var result = expr.left.accept(this).iff(expr.right.accept(this));
        logger.exit("IFF = " + result);
        return result;
    }

    public ThreeVal visit(AlloyNegExpr expr) {
        logger.enter("NOT: " + expr);
        var result = expr.sub.accept(this).not();
        logger.exit("NOT = " + result);
        return result;
    }

    public ThreeVal visit(AlloyCmpExpr expr) {
        logger.enter("CMP " + expr.comp + ": " + expr);
        var left = expr.left.accept(setEvaluator);
        var right = expr.right.accept(setEvaluator);
        ThreeVal result = switch (expr.comp) { // TODO: subset/set semantics for three values logic
                    case AlloyCmpExpr.Comp.IN -> convertThree(right.containsAll(left));
                    case AlloyCmpExpr.Comp.LESS_THAN -> compareInts(left, right, (l, r) -> l < r);
                    case AlloyCmpExpr.Comp.GREATER_THAN ->
                            compareInts(left, right, (l, r) -> l > r);
                    case AlloyCmpExpr.Comp.LESS_EQUAL -> compareInts(left, right, (l, r) -> l <= r);
                    case AlloyCmpExpr.Comp.EQUAL_LESS -> compareInts(left, right, (l, r) -> l <= r);
                    case AlloyCmpExpr.Comp.GREATER_EQUAL ->
                            compareInts(left, right, (l, r) -> l >= r);
                    default ->
                            throw AlloyEvaluatorImplError.missingVisitCase(
                                    "AlloyCmp comp: " + expr.comp);
                };
        if (expr.neg) result = result.not();
        logger.exit("CMP " + expr.comp + " = " + result);
        return result;
    }

    private ThreeVal compareInts(
            Set<List<Atom>> scalar1, Set<List<Atom>> scalar2, BiPredicate<Integer, Integer> cmp) {
        var left = getIntScalar(scalar1);
        var right = getIntScalar(scalar2);

        if (left.isOverflowing() || right.isOverflowing()) return UNKNOWN;
        return convertThree(cmp.test(left.getValue(), right.getValue()));
    }

    private Atom getIntScalar(Set<List<Atom>> val) {
        var list = setToList(val);
        if (list.size() != 1) {
            throw AlloyEvaluatorImplError.cardinalityError("The cardinality of a scalar must be 1");
        } else if (firstElement(list).size() != 1) {
            throw AlloyEvaluatorImplError.arityError("The arity of a scalar must be 1");
        }
        var result = firstElement(firstElement(list));
        if (!result.isInteger()) {
            throw AlloyEvaluatorImplError.arityError("Scale comparison must be done on an integer");
        }
        return result;
    }

    /* TODO: subset/set semantics for three values logic
    private ThreeVal subsThreeVal(Set<List<Atom>> left, Set<List<Atom>> right) {
        if (left.size() > right.size()) return FALSE;

        return TRUE;
    }

    private boolean containsOverflow(Set<List<Atom>> val) {
        for (var tuple : val) {
            if (tuple.)
        }
    }
            */
}
