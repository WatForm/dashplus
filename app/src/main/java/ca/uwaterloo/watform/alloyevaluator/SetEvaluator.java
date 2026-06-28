package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

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
    private final EvalLogger logger;

    public SetEvaluator(Instance instance, boolean debug) {
        this.instance = instance;
        this.logger = EvalLoggerFactory.make("evaluation", debug);
    }

    // Unimplemented — error message carries all needed detail
    public Set<List<String>> visit(DashRef dashRef) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "DashRef: " + dashRef + " " + dashRef.getClass().getName());
    }

    public Set<List<String>> visit(AlloyBinaryExpr binExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyBinaryExpr: " + binExpr + " " + binExpr.getClass().getName());
    }

    public Set<List<String>> visit(AlloyUnaryExpr unaryExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyUnaryExpr: " + unaryExpr + " " + unaryExpr.getClass().getName());
    }

    public Set<List<String>> visit(AlloyVarExpr varExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyVarExpr: " + varExpr + " " + varExpr.getClass().getName());
    }

    public Set<List<String>> visit(AlloyBlock block) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyBlock: " + block + " " + block.getClass().getName());
    }

    public Set<List<String>> visit(AlloyCphExpr comprehensionExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyCphExpr: "
                        + comprehensionExpr
                        + " "
                        + comprehensionExpr.getClass().getName());
    }

    public Set<List<String>> visit(AlloyIteExpr iteExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyIteExpr: " + iteExpr + " " + iteExpr.getClass().getName());
    }

    public Set<List<String>> visit(AlloyLetExpr letExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyLetExpr: " + letExpr + " " + letExpr.getClass().getName());
    }

    public Set<List<String>> visit(AlloyQuantificationExpr quantificationExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyQuantificationExpr: "
                        + quantificationExpr
                        + " "
                        + quantificationExpr.getClass().getName());
    }

    public Set<List<String>> visit(AlloyDecl decl) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyDecl: " + decl + " " + decl.getClass().getName());
    }

    public Set<List<String>> visit(AlloyQnameExpr qName) {
        logger.enter("QName: " + qName);
        if (qName.vars.isEmpty())
            throw AlloyEvaluatorImplError.notSupported("A variable must exist to evaluate it");
        var result = instance.getRelation(qName.vars.getLast().label);
        if (result.isEmpty())
            throw AlloyEvaluatorImplError.relationNotInInstance(qName.vars.getLast().label);
        logger.exit("QName = " + result.get());
        return result.get();
    }

    public Set<List<String>> visit(AlloyNoneExpr expr) {
        logger.enter("None");
        logger.exit("None = {}");
        return emptySet();
    }

    public Set<List<String>> visit(AlloyIdenExpr expr) {
        logger.enter("Iden");
        var result = instance.getIden();
        logger.exit("Iden = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyUnivExpr expr) {
        logger.enter("Univ");
        var result = instance.getUniv();
        logger.exit("Univ = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyUnionExpr expr) {
        logger.enter("Union: " + expr);
        var result = mergeSets(expr.left.accept(this), expr.right.accept(this));
        logger.exit("Union = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyIntersExpr expr) {
        logger.enter("Intersect: " + expr);
        var result = intersectSets(expr.left.accept(this), expr.right.accept(this));
        logger.exit("Intersect = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyDiffExpr expr) {
        logger.enter("Diff: " + expr);
        var result = diffSets(expr.left.accept(this), expr.right.accept(this));
        logger.exit("Diff = " + result);
        return result;
    }

    private Set<List<String>> product(Set<List<String>> left, Set<List<String>> right) {
        Set<List<String>> result = emptySet();
        for (var l : left) {
            for (var r : right) {
                result.add(concat(l, r));
            }
        }
        return result;
    }

    private Set<List<String>> join(Set<List<String>> left, Set<List<String>> right) {
        Set<List<String>> result = emptySet();
        for (var l : left) {
            for (var r : right) {
                if (lastElement(l).equals(firstElement(r))) {
                    result.add(concat(allButLast(l), allButFirst(r)));
                }
            }
        }
        return result;
    }

    public Set<List<String>> visit(AlloyArrowExpr expr) {
        logger.enter("ArrowProduct: " + expr);
        var result = product(expr.left.accept(this), expr.right.accept(this));
        logger.exit("ArrowProduct = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyDotExpr expr) {
        logger.enter("Dot: " + expr);
        var left = expr.left.accept(this);
        var right = expr.right.accept(this);
        logger.log("Dot left = " + left + ", right = " + right);
        var result = join(left, right);
        logger.exit("Dot = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyBracketExpr bracketExpr) {
        logger.enter("BoxJoin: " + bracketExpr);
        List<Set<List<String>>> args = mapBy(bracketExpr.exprs, e -> e.accept(this));
        var left = bracketExpr.expr.accept(this);
        var argsProduct = foldLeft(args.subList(1, args.size()), this::product, args.get(0));
        logger.log("Box left = " + left + ", inner = " + argsProduct);
        var result = join(argsProduct, left);
        logger.exit("BoxJoin = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyTransExpr expr) {
        logger.enter("Transpose: " + expr);
        var inner = expr.sub.accept(this);
        Set<List<String>> result = emptySet();
        for (var tuple : inner) {
            result.add(reverse(tuple));
        }
        logger.exit("Transpose = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyDomRestrExpr expr) {
        logger.enter("DomainRestrict: " + expr);
        var domain = expr.left.accept(this);
        var relation = expr.right.accept(this);
        Set<List<String>> result = emptySet();
        for (var tuple : relation) {
            if (domain.contains(List.of(firstElement(tuple)))) {
                result.add(tuple);
            }
        }
        logger.exit("DomainRestrict = " + result);
        return result;
    }

    public Set<List<String>> visit(AlloyRngRestrExpr expr) {
        logger.enter("RangeRestrict: " + expr);
        var relation = expr.left.accept(this);
        var range = expr.right.accept(this);
        Set<List<String>> result = emptySet();
        for (var tuple : relation) {
            if (range.contains(List.of(lastElement(tuple)))) {
                result.add(tuple);
            }
        }
        logger.exit("RangeRestrict = " + result);
        return result;
    }
}
