package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.alloyevaluator.ThreeVal.*;
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

public class SetEvaluator implements AlloyExprVis<TupleSet> {
    private final Instance instance;
    private final EvalLogger logger;

    public SetEvaluator(Instance instance, boolean debug) {
        this.instance = instance;
        this.logger = EvalLoggerFactory.make("evaluation", debug);
    }

    // Unimplemented — error message carries all needed detail
    public TupleSet visit(DashRef dashRef) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "DashRef: " + dashRef + " " + dashRef.getClass().getName());
    }

    public TupleSet visit(AlloyBinaryExpr binExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyBinaryExpr: " + binExpr + " " + binExpr.getClass().getName());
    }

    public TupleSet visit(AlloyUnaryExpr unaryExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyUnaryExpr: " + unaryExpr + " " + unaryExpr.getClass().getName());
    }

    public TupleSet visit(AlloyVarExpr varExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyVarExpr: " + varExpr + " " + varExpr.getClass().getName());
    }

    public TupleSet visit(AlloyBlock block) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyBlock: " + block + " " + block.getClass().getName());
    }

    public TupleSet visit(AlloyCphExpr comprehensionExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyCphExpr: "
                        + comprehensionExpr
                        + " "
                        + comprehensionExpr.getClass().getName());
    }

    public TupleSet visit(AlloyIteExpr iteExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyIteExpr: " + iteExpr + " " + iteExpr.getClass().getName());
    }

    public TupleSet visit(AlloyLetExpr letExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyLetExpr: " + letExpr + " " + letExpr.getClass().getName());
    }

    public TupleSet visit(AlloyQuantificationExpr quantificationExpr) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyQuantificationExpr: "
                        + quantificationExpr
                        + " "
                        + quantificationExpr.getClass().getName());
    }

    public TupleSet visit(AlloyDecl decl) {
        throw AlloyEvaluatorImplError.missingVisitCase(
                "AlloyDecl: " + decl + " " + decl.getClass().getName());
    }

    public TupleSet visit(AlloyQnameExpr qName) {
        logger.enter("QName: " + qName);
        if (qName.vars.isEmpty())
            throw AlloyEvaluatorImplError.notSupported("A variable must exist to evaluate it");
        var result = instance.getRelation(qName.vars.getLast().label);
        if (result.isEmpty())
            throw AlloyEvaluatorImplError.relationNotInInstance(qName.vars.getLast().label);
        logger.exit("QName = " + result.get());
        return result.get();
    }

    public TupleSet visit(AlloyNoneExpr expr) {
        logger.enter("None");
        logger.exit("None = {}");
        return TupleSet.emptySet();
    }

    public TupleSet visit(AlloyIdenExpr expr) {
        logger.enter("Iden");
        var result = instance.getIden();
        logger.exit("Iden = " + result);
        return result;
    }

    public TupleSet visit(AlloyUnivExpr expr) {
        logger.enter("Univ");
        var result = instance.getUniv();
        logger.exit("Univ = " + result);
        return result;
    }

    public TupleSet visit(AlloyUnionExpr expr) {
        logger.enter("Union: " + expr);
        var result = TupleSet.union(expr.left.accept(this), expr.right.accept(this));
        logger.exit("Union = " + result);
        return result;
    }

    public TupleSet visit(AlloyIntersExpr expr) {
        logger.enter("Intersect: " + expr);
        var result = TupleSet.intersect(expr.left.accept(this), expr.right.accept(this));
        logger.exit("Intersect = " + result);
        return result;
    }

    public TupleSet visit(AlloyDiffExpr expr) {
        logger.enter("Diff: " + expr);
        var result = TupleSet.diff(expr.left.accept(this), expr.right.accept(this));
        logger.exit("Diff = " + result);
        return result;
    }

    public TupleSet visit(AlloyArrowExpr expr) {
        logger.enter("ArrowProduct: " + expr);
        var result = TupleSet.crossProduct(expr.left.accept(this), expr.right.accept(this));
        logger.exit("ArrowProduct = " + result);
        return result;
    }

    public TupleSet visit(AlloyDotExpr expr) {
        logger.enter("Dot: " + expr);
        var result = TupleSet.join(expr.left.accept(this), expr.right.accept(this));
        logger.exit("Dot = " + result);
        return result;
    }

    public TupleSet visit(AlloyBracketExpr bracketExpr) {
        logger.enter("BoxJoin: " + bracketExpr);
        List<TupleSet> args = mapBy(bracketExpr.exprs, e -> e.accept(this));
        var result = bracketExpr.expr.accept(this);
        for (var arg : args) {
            result = TupleSet.join(arg, result);
        }
        logger.exit("BoxJoin = " + result);
        return result;
    }

    public TupleSet visit(AlloyTransExpr expr) {
        logger.enter("Transpose: " + expr);
        var result = TupleSet.mapBy(expr.sub.accept(this), t -> AtomTuple.transpose(t));
        logger.exit("Transpose = " + result);
        return result;
    }

    public TupleSet visit(AlloyDomRestrExpr expr) {
        logger.enter("DomainRestrict: " + expr);
        var domain = expr.left.accept(this);
        var relation = expr.right.accept(this);
        TupleSet result =
                TupleSet.filterBy(
                        relation, t -> domain.contains(AtomTuple.tupleOfFirst(t)) == TRUE);
        logger.exit("DomainRestrict = " + result);
        return result;
    }

    public TupleSet visit(AlloyRngRestrExpr expr) {
        logger.enter("RangeRestrict: " + expr);
        var relation = expr.left.accept(this);
        var range = expr.right.accept(this);
        TupleSet result =
                TupleSet.filterBy(relation, t -> range.contains(AtomTuple.tupleOfLast(t)) == TRUE);
        logger.exit("RangeRestrict = " + result);
        return result;
    }

    public TupleSet visit(AlloyRelOvrdExpr expr) {
        logger.enter("RelOverride: " + expr);
        var left = expr.left.accept(this);
        var right = expr.right.accept(this);

        var domRight = TupleSet.mapBy(right, e -> AtomTuple.tupleOfFirst(e));
        TupleSet result =
                TupleSet.union(
                        TupleSet.filterBy(
                                left, t -> domRight.contains(AtomTuple.tupleOfFirst(t)) == FALSE),
                        right);
        logger.exit("RelOverride = " + result);
        return result;
    }

    private TupleSet evalTransClosure(TupleSet base) {
        var collect = base;
        var current = TupleSet.join(base, base);

        while (!current.isEmpty()) {
            collect = TupleSet.union(collect, current);
            current = TupleSet.join(current, base);
        }

        return collect;
    }

    public TupleSet visit(AlloyTransClosExpr expr) {
        logger.enter("TransClosure: " + expr);
        TupleSet result = evalTransClosure(expr.sub.accept(this));
        logger.exit("TransClosure = " + result);
        return result;
    }

    public TupleSet visit(AlloyReflTransClosExpr expr) {
        logger.enter("TransClosure: " + expr);
        TupleSet result = evalTransClosure(expr.sub.accept(this));
        result = TupleSet.union(result, instance.getIden());
        logger.exit("TransClosure = " + result);
        return result;
    }

    // TODO: Need to check for overflow in future
    public TupleSet visit(AlloyNumExpr expr) {
        logger.enter("NumExpr: " + expr);
        TupleSet result = instance.getIntScalar(expr.value);
        logger.exit("NumExpr = " + result);
        return result;
    }

    public TupleSet visit(AlloyCardExpr expr) {
        logger.enter("Cardinality: " + expr);
        TupleSet result = instance.getCardinality(expr.sub.accept(this));
        logger.exit("Cardinality = " + result);
        return result;
    }

    public TupleSet visit(AlloySigIntExpr expr) {
        logger.enter("Int set: " + expr);
        var result = instance.getIntSet();
        logger.exit("Int set = " + result);
        return result;
    }
}
