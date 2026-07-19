package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.alloyevaluator.ThreeVal.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyevaluator.OverflowAtom.OverflowDirection;
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
        if (qName.vars.isEmpty()) {
            throw AlloyEvaluatorImplError.notSupported("A variable must exist to evaluate it");
        }
        var result = instance.getRelation(qName.label);
        if (result.isEmpty()) {
            throw AlloyEvaluatorImplError.relationNotInInstance(qName.label);
        }
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

    // TODO: redo function calls
    public TupleSet visit(AlloyBracketExpr bracketExpr) {
        if (bracketExpr.expr instanceof AlloyQnameExpr qExpr && isFuncionName(qExpr)) {
            return processFunctionCall(qExpr, bracketExpr);
        } else {
            return processBoxJoin(bracketExpr);
        }
    }

    // TODO: redo function calls
    private boolean isFuncionName(AlloyQnameExpr expr) {
        return expr.vars.size() == 1
                && (expr.label.equals("plus")
                        || expr.label.equals("minus")
                        || expr.label.equals("mul")
                        || expr.label.equals("div")
                        || expr.label.equals("rem"));
    }

    // TODO: redo function calls
    private TupleSet processFunctionCall(AlloyQnameExpr fun, AlloyBracketExpr bracketExpr) {
        logger.enter("FunctionCall: " + bracketExpr);
        var result =
                switch (fun.label) {
                    case "plus" -> processPlus(bracketExpr);
                    case "minus" -> processMinus(bracketExpr);
                    case "mul" -> processMul(bracketExpr);
                    case "div" -> processDiv(bracketExpr);
                    case "rem" -> processRem(bracketExpr);
                    default ->
                            throw AlloyEvaluatorImplError.relationNotInInstance(
                                    "Function " + fun.label + " not found");
                };
        logger.exit("FunctionCall = " + result);
        return result;
    }

    // TODO: cleanup
    private TupleSet processPlus(AlloyBracketExpr bracketExpr) {
        List<TupleSet> args = mapBy(bracketExpr.exprs, e -> e.accept(this));
        if (args.size() != 2)
            throw AlloyEvaluatorImplError.comparisonError("plus accepts 2 arguments only");
        var first = args.getFirst().getScalar();
        var second = args.getLast().getScalar();

        if (first instanceof LabelAtom || second instanceof LabelAtom)
            throw AlloyEvaluatorImplError.typeError("Must be integers");

        if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
            return instance.getIntScalar(fi.value() + si.value(), bracketExpr.pos);
        }

        // at least one side has overflowed, so no concrete sum is available
        OverflowDirection da = Atom.directionOf(first);
        OverflowDirection db = Atom.directionOf(second);

        if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        }

        if (da != null && db != null) {
            // both sides overflowed with a known direction
            OverflowDirection result = (da == db) ? da : OverflowDirection.OVERFLOW_UNKNOWN;
            return instance.getOverflowScalar(result, bracketExpr.pos);
        }

        // exactly one side overflowed with a known direction; the other is a concrete integer
        OverflowAtom overflowed = (OverflowAtom) (da != null ? first : second);
        int concreteVal = ((IntegerAtom) (da != null ? second : first)).value();
        OverflowDirection dir = overflowed.direction();

        boolean sameDirection =
                (dir == OverflowDirection.OVERFLOW_UP && concreteVal >= 0)
                        || (dir == OverflowDirection.OVERFLOW_DOWN && concreteVal <= 0);

        return instance.getOverflowScalar(
                sameDirection ? dir : OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
    }

    private static OverflowDirection flip(OverflowDirection d) {
        return switch (d) {
            case OVERFLOW_UP -> OverflowDirection.OVERFLOW_DOWN;
            case OVERFLOW_DOWN -> OverflowDirection.OVERFLOW_UP;
            case OVERFLOW_UNKNOWN -> OverflowDirection.OVERFLOW_UNKNOWN;
        };
    }

    private TupleSet processMinus(AlloyBracketExpr bracketExpr) {
        List<TupleSet> args = mapBy(bracketExpr.exprs, e -> e.accept(this));
        if (args.size() != 2)
            throw AlloyEvaluatorImplError.comparisonError("minus accepts 2 arguments only");
        var first = args.getFirst().getScalar();
        var second = args.getLast().getScalar();

        if (first instanceof LabelAtom || second instanceof LabelAtom)
            throw AlloyEvaluatorImplError.typeError("Must be integers");

        if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
            return instance.getIntScalar(fi.value() - si.value(), bracketExpr.pos);
        }

        OverflowDirection da = Atom.directionOf(first);
        OverflowDirection dbRaw = Atom.directionOf(second);

        if (da == OverflowDirection.OVERFLOW_UNKNOWN || dbRaw == OverflowDirection.OVERFLOW_UNKNOWN) {
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        }

        if (da != null && dbRaw != null) {
            // both overflowed. Similar to addition, if direction is the same (after negation) - we know the result overflows in that direction
            OverflowDirection db = flip(dbRaw);
            OverflowDirection result = (da == db) ? da : OverflowDirection.OVERFLOW_UNKNOWN;
            return instance.getOverflowScalar(result, bracketExpr.pos);
        }

        if (da != null) {
            // a overflowed, b concrete: negating a concrete, finite-magnitude value is
            // exact, so this is just plus's single-overflow rule with b's sign flipped.
            int negSecond = -((IntegerAtom) second).value();
            boolean sameDirection = (da == OverflowDirection.OVERFLOW_UP && negSecond >= 0)
                    || (da == OverflowDirection.OVERFLOW_DOWN && negSecond <= 0);
            return instance.getOverflowScalar(
                    sameDirection ? da : OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        } else {
            // b overflowed, a concrete: a - b == a + (-b), and b's magnitude is unbounded
            // here -- this is where the asymmetric two's-complement range actually bites.
            int firstVal = ((IntegerAtom) first).value();

            if (dbRaw == OverflowDirection.OVERFLOW_DOWN) {
                // -b > maxInt unconditionally: minimal-magnitude down clears maxInt by a
                // full unit of cushion (|minInt| = |maxInt| + 1) -- no boundary risk.
                boolean sameDirection = firstVal >= 0;
                return instance.getOverflowScalar(
                        sameDirection ? OverflowDirection.OVERFLOW_UP : OverflowDirection.OVERFLOW_UNKNOWN,
                        bracketExpr.pos);
            } else {
                // dbRaw == OVERFLOW_UP: -b <= minInt, and the minimal-magnitude up value
                // negates to EXACTLY minInt -- in range, not overflow. Needs a strictly
                // negative concrete addend to guarantee clearing that boundary.
                boolean sameDirection = firstVal < 0;
                return instance.getOverflowScalar(
                        sameDirection ? OverflowDirection.OVERFLOW_DOWN : OverflowDirection.OVERFLOW_UNKNOWN,
                        bracketExpr.pos);
            }
        }
    }

    // TODO: cleanup
    private TupleSet processMul(AlloyBracketExpr bracketExpr) {
        List<TupleSet> args = mapBy(bracketExpr.exprs, e -> e.accept(this));
        if (args.size() != 2)
            throw AlloyEvaluatorImplError.comparisonError("mul accepts 2 arguments only");
        var first = args.getFirst().getScalar();
        var second = args.getLast().getScalar();

        if (first instanceof LabelAtom || second instanceof LabelAtom)
            throw AlloyEvaluatorImplError.typeError("Must be integers");

        if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
            return instance.getIntScalar(fi.value() * si.value(), bracketExpr.pos);
        }

        // 0 * anything is exactly 0, even if "anything" is out of representable range --
        // this holds regardless of the other side's direction, even OVERFLOW_UNKNOWN
        if (first instanceof IntegerAtom fi0 && fi0.value() == 0)
            return instance.getIntScalar(0, bracketExpr.pos);
        if (second instanceof IntegerAtom si0 && si0.value() == 0)
            return instance.getIntScalar(0, bracketExpr.pos);

        OverflowDirection da = Atom.directionOf(first);
        OverflowDirection db = Atom.directionOf(second);

        if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        }

        // unlike plus, a product's sign is fully determined by the two factors' signs alone,
        // regardless of unknown magnitude -- so once neither factor is zero and both signs
        // are known, the result direction is always definite (never UNKNOWN)
        boolean firstPositive =
                (da != null)
                        ? da == OverflowDirection.OVERFLOW_UP
                        : ((IntegerAtom) first).value() > 0;
        boolean secondPositive =
                (db != null)
                        ? db == OverflowDirection.OVERFLOW_UP
                        : ((IntegerAtom) second).value() > 0;

        if (firstPositive == secondPositive) {
            // negative * negative, or positive * positive: magnitude only grows,
            // always lands strictly past maxInt -- never ambiguous, due to the
            // asymmetric two's-complement range (|minInt| = |maxInt| + 1)
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UP, bracketExpr.pos);
        } else {
            // one positive, one negative: the minimal-magnitude overflow-up value,
            // negated, lands exactly on minInt -- in range, not overflow -- so a
            // "down" result can never be asserted from direction alone
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        }
    }

    // TODO: cleanup
    private TupleSet processDiv(AlloyBracketExpr bracketExpr) {
        List<TupleSet> args = mapBy(bracketExpr.exprs, e -> e.accept(this));
        if (args.size() != 2)
            throw AlloyEvaluatorImplError.comparisonError("div accepts 2 arguments only");
        var first = args.getFirst().getScalar();
        var second = args.getLast().getScalar();

        if (first instanceof LabelAtom || second instanceof LabelAtom)
            throw AlloyEvaluatorImplError.typeError("Must be integers");

        if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
            if (si.value() == 0) {
                throw AlloyEvaluatorImplError.notSupported("Division by 0");
            }
            return instance.getIntScalar(fi.value() / si.value(), bracketExpr.pos);
        }

        // Unless first in minInt, the resulting value must be 0
        if (first instanceof IntegerAtom fi) {
            OverflowDirection secondDir = Atom.directionOf(second);
            if (secondDir == OverflowDirection.OVERFLOW_UP || secondDir == OverflowDirection.OVERFLOW_DOWN) {
                // boundary collision: |minInt| == minimal OVERFLOW_UP magnitude (maxInt + 1),
                // so minInt / (an UP value) could be exactly -1, not 0 -- unresolvable without
                // knowing the divisor's exact magnitude. OVERFLOW_DOWN's minimal magnitude
                // (maxInt + 2) never collides, so it's unaffected.
                boolean boundaryRisk = secondDir == OverflowDirection.OVERFLOW_UP && fi.value() == instance.minInt();
                if (boundaryRisk) {
                    return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
                }
                return instance.getIntScalar(0, bracketExpr.pos);
            }
        }

        OverflowDirection da = Atom.directionOf(first);
        OverflowDirection db = Atom.directionOf(second);

        if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        }

        if (da != null && db != null) {
            // both out of range: the ratio of two unbounded magnitudes is unconstrained --
            // could land back in representable range -- so not even a direction is safe to assert
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        }

        // numerator overflowed, divisor concrete
        if (second instanceof IntegerAtom si) {
            if (si.value() == 1) {
                // identity: same direction, unchanged
                return instance.getOverflowScalar(da, bracketExpr.pos);
            }
            if (si.value() == -1) {
                // negation -- same boundary issue as mul: negating the minimal OVERFLOW_UP
                // value can land exactly on minInt (in range), so UP is never safe to assert;
                // negating OVERFLOW_DOWN always exceeds maxInt, so DOWN -> UP is always safe
                return instance.getOverflowScalar(
                        da == OverflowDirection.OVERFLOW_DOWN
                                ? OverflowDirection.OVERFLOW_UP
                                : OverflowDirection.OVERFLOW_UNKNOWN,
                        bracketExpr.pos);
            }

            // may or may not end up within the range
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        }

        // numerator overflowed, divisor concrete nonzero: sign is determined, but magnitude
        // depends on the numerator's unknown exact value, so it may or may not still overflow
        return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
    }

    // TODO: cleanup
    private TupleSet processRem(AlloyBracketExpr bracketExpr) {
        List<TupleSet> args = mapBy(bracketExpr.exprs, e -> e.accept(this));
        if (args.size() != 2)
            throw AlloyEvaluatorImplError.comparisonError("rem accepts 2 arguments only");
        var first = args.getFirst().getScalar();
        var second = args.getLast().getScalar();

        if (first instanceof LabelAtom || second instanceof LabelAtom)
            throw AlloyEvaluatorImplError.typeError("Must be integers");

        if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
            if (si.value() == 0) {
                throw AlloyEvaluatorImplError.notSupported("Division by 0");
            }
            return instance.getIntScalar(fi.value() % si.value(), bracketExpr.pos);
        }

        if (first instanceof IntegerAtom fi) {
            OverflowDirection secondDir = Atom.directionOf(second);
            if (secondDir == OverflowDirection.OVERFLOW_UP || secondDir == OverflowDirection.OVERFLOW_DOWN) {
                // same boundary collision as div: at minInt / (minimal UP value), quotient is
                // -1 and remainder is 0, not minInt -- unresolvable, same reasoning as above.
                boolean boundaryRisk = secondDir == OverflowDirection.OVERFLOW_UP && fi.value() == instance.minInt();
                if (boundaryRisk) {
                    return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
                }
                return instance.getIntScalar(fi.value(), bracketExpr.pos);
            }
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos); // second overflow is unknown
        }

        if (second instanceof IntegerAtom si) {
            // a % 1 == 0 and a % -1 == 0 for every integer a, regardless of first's
            // magnitude or direction -- must be checked ahead of any direction dispatch
            if (si.value() == 1 || si.value() == -1) {
                return instance.getIntScalar(0, bracketExpr.pos);
            }
            // numerator overflowed, |divisor| > 1: |remainder| < |divisor| so the true
            // exact value depends on the numerator's unresolved true value
            return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
        }

        // both overflowed. Cannot determine the exact value or if concretely overflows
        return instance.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, bracketExpr.pos);
    }

    private TupleSet processBoxJoin(AlloyBracketExpr bracketExpr) {
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
        TupleSet result = instance.getIntScalar(expr.value, expr.pos);
        logger.exit("NumExpr = " + result);
        return result;
    }

    public TupleSet visit(AlloyCardExpr expr) {
        logger.enter("Cardinality: " + expr);
        TupleSet result = instance.getCardinality(expr.sub.accept(this), expr.pos);
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
