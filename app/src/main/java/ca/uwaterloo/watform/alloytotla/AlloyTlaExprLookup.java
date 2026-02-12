package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.tlaast.*;
import java.util.*;
import java.util.function.*;

public class AlloyTlaExprLookup {

    public static interface VarArgsFunction<T, R> {
        R apply(T... args);
    }

    private static final Map<
                    Class<? extends AlloyExpr>,
                    Function<AlloyExpr, VarArgsFunction<TlaExp, TlaExp>>>
            table;

    private static final TlaExp ERROR = new TlaVar("THIS_IS_NOT_SUPPORTED");
    private static final Function<TlaExp, TlaExp> ERROR_UNARY = (e) -> ERROR;
    private static final BiFunction<TlaExp, TlaExp, TlaExp> ERROR_BINARY = (e1, e2) -> ERROR;

    public static VarArgsFunction<TlaExp, TlaExp> lookup(AlloyExpr expr) {
        if (table.keySet().contains(expr.getClass())) return table.get(expr.getClass()).apply(expr);
        return null;
    }

    private static Function<AlloyExpr, VarArgsFunction<TlaExp, TlaExp>> simple(
            VarArgsFunction<TlaExp, TlaExp> f) {
        return (exp) -> f;
    }

    private static VarArgsFunction<TlaExp, TlaExp> empty(Supplier<TlaExp> f) {
        return (exp) -> {
            return f.get();
        };
    }

    private static VarArgsFunction<TlaExp, TlaExp> unary(Function<TlaExp, TlaExp> f) {
        return (exp) -> {
            return f.apply(exp[0]);
        };
    }

    private static VarArgsFunction<TlaExp, TlaExp> binary(BiFunction<TlaExp, TlaExp, TlaExp> f) {
        return (exp) -> {
            return f.apply(exp[0], exp[1]);
        };
    }

    static {
        table = new HashMap<>();

        // binary
        table.put(AlloyAndExpr.class, simple(binary(CreateHelper::TlaAnd)));
        // arrow exp
        table.put(
                AlloyCmpExpr.class,
                (exp) -> {
                    return binary(
                            switch (((AlloyCmpExpr) exp).comp) {
                                case AlloyCmpExpr.Comp.EQUAL_LESS -> CreateHelper::TlaAdd;
                                case AlloyCmpExpr.Comp.LESS_EQUAL -> CreateHelper::TlaLesserEq;
                                case AlloyCmpExpr.Comp.LESS_THAN -> CreateHelper::TlaLesser;
                                case AlloyCmpExpr.Comp.IN -> CreateHelper::TlaSubsetEq;
                                case AlloyCmpExpr.Comp.GREATER_EQUAL -> CreateHelper::TlaGreater;
                                case AlloyCmpExpr.Comp.GREATER_THAN -> CreateHelper::TlaGreaterEq;
                            });
                });
        table.put(AlloyDiffExpr.class, simple(binary(CreateHelper::TlaDiffSet)));
        // domain restriction
        // dot join
        table.put(AlloyEqualsExpr.class, simple(binary(CreateHelper::TlaEquals)));
        // add, mult, rem, sub
        table.put(AlloyIffExpr.class, simple(binary(CreateHelper::TlaEquivalence)));
        table.put(AlloyImpliesExpr.class, simple(binary(CreateHelper::TlaImplies)));
        table.put(AlloyIntersExpr.class, simple(binary(CreateHelper::TlaIntersectionSet)));
        table.put(AlloyNotEqualsExpr.class, simple(binary(CreateHelper::TlaNotEq)));
        table.put(AlloyOrExpr.class, simple(binary(CreateHelper::TlaOr)));
        // relational override
        // range restriction
        // shift left, shift right logical, shift right arithmetic
        // ; join (opposite of . join)
        table.put(AlloyUnionExpr.class, simple(binary(CreateHelper::TlaUnionSet)));

        // misc
        table.put(AlloyBlock.class, simple(AlloyToTlaHelpers::repeatedAnd));
        // brackets
        // cph (comprehension)
        // decl
        // if-then-else
        // let
        table.put(AlloyParenExpr.class, simple(unary((e) -> e)));
        // quantification

        // unary
        table.put(AlloyCardExpr.class, simple(unary(TlaStdLibs::Cardinality)));
        table.put(AlloyNegExpr.class, simple(unary(CreateHelper::TlaNot)));
        table.put(
                AlloyQtExpr.class,
                (exp) -> {
                    return unary(
                            switch (((AlloyQtExpr) exp).qt) {
                                case AlloyQtExpr.Quant.SOME -> Boilerplate::_SOME;
                                case AlloyQtExpr.Quant.LONE -> Boilerplate::_LONE;
                                case AlloyQtExpr.Quant.NO -> Boilerplate::_NO;
                                case AlloyQtExpr.Quant.ONE -> Boilerplate::_ONE;
                                // TODO look into this
                                case AlloyQtExpr.Quant.ALL -> ERROR_UNARY;
                                case AlloyQtExpr.Quant.SET -> ERROR_UNARY;
                                case AlloyQtExpr.Quant.SEQ -> ERROR_UNARY;
                            });
                });
        // sum, int, transpose, transitive closure, reflexive transitive closure

        // var
        // @
        // disj
        // max, min, next
        table.put(AlloyUnivExpr.class, simple(empty(Boilerplate::_IDEN)));
        // Int
        // Name
        table.put(AlloyUnivExpr.class, simple(empty(Boilerplate::_NONE)));
        // Num
        // pred to ord
        // Qname
        // scopable
        // seq
        // seqint
        // sigint
        // sigref
        // steps
        // string
        // string literal
        // sum
        // this
        table.put(AlloyUnivExpr.class, simple(empty(Boilerplate::_UNIV)));
    }
}
