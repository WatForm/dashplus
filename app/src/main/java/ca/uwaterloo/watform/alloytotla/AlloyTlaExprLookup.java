package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.Boilerplate.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyParenExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyCardExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyNegExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyQtExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyUnivExpr;
import ca.uwaterloo.watform.tlaast.CreateHelper;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlaast.TlaVar;
import java.util.*;
import java.util.function.*;

public class AlloyTlaExprLookup {

    private static final Map<
                    Class<? extends AlloyBinaryExpr>,
                    Function<AlloyBinaryExpr, BiFunction<TlaExp, TlaExp, TlaExp>>>
            binary;

    private static final Map<
                    Class<? extends AlloyUnaryExpr>,
                    Function<AlloyBinaryExpr, Function<TlaExp, TlaExp>>>
            unary;

    static {
        binary = new HashMap<>();

        binary.put(AlloyAndExpr.class, (exp) -> CreateHelper::TlaAnd);
        binary.put(
                AlloyCmpExpr.class,
                (exp) -> {
                    return switch (((AlloyCmpExpr) exp).comp) {
                        case AlloyCmpExpr.Comp.EQUAL_LESS -> CreateHelper::TlaLesserEq;
                        case AlloyCmpExpr.Comp.LESS_EQUAL -> CreateHelper::TlaLesserEq;
                        case AlloyCmpExpr.Comp.LESS_THAN -> CreateHelper::TlaLesser;
                        case AlloyCmpExpr.Comp.IN -> CreateHelper::TlaSubsetEq;
                        case AlloyCmpExpr.Comp.GREATER_EQUAL -> CreateHelper::TlaGreater;
                        case AlloyCmpExpr.Comp.GREATER_THAN -> CreateHelper::TlaGreaterEq;
                    };
                });
        binary.put(AlloyDiffExpr.class, (exp) -> CreateHelper::TlaDiffSet);
        binary.put(AlloyEqualsExpr.class, (exp) -> CreateHelper::TlaEquals);
        binary.put(AlloyOrExpr.class, (exp) -> CreateHelper::TlaOr);
        binary.put(AlloyIffExpr.class, (exp) -> CreateHelper::TlaEquivalence);
        binary.put(AlloyIntersExpr.class, (exp) -> CreateHelper::TlaIntersectionSet);
        binary.put(AlloyNotEqualsExpr.class, (exp) -> CreateHelper::TlaNotEq);
        binary.put(AlloyOrExpr.class, (exp) -> CreateHelper::TlaOr);
        binary.put(AlloyUnionExpr.class, (exp) -> CreateHelper::TlaUnionSet);
    }

    static {
        unary = new HashMap<>();

        unary.put(AlloyNegExpr.class, (exp) -> CreateHelper::TlaNot);
    }

    private static final TlaExp ERROR = new TlaVar("THIS_IS_NOT_SUPPORTED");

    public static TlaExp translate(AlloyExpr exp) {

        // sigRef
        if (exp instanceof AlloySigRefExpr) {
            return translateAlloySigRefExp((AlloySigRefExpr) exp);
        }

        if (exp instanceof AlloyParenExpr) return translate(((AlloyParenExpr) exp).sub);

        // if(exp instanceof AlloyBlock)
        // return repeatedAnd(mapBy(((AlloyBlock)exp).exprs, e -> translate(e)));

        // unary ops
        if (exp instanceof AlloyUnaryExpr) {
            return translateAlloyUnaryExpr((AlloyUnaryExpr) exp);
        }
        if (exp instanceof AlloyQtExpr) {
            return translateAlloyQtExpr((AlloyQtExpr) exp);
        }

        return ERROR;
    }

    public static TlaExp translateAlloyUnaryExpr(AlloyUnaryExpr exp) {
        if (exp instanceof AlloyQtExpr) return translateAlloyQtExpr((AlloyQtExpr) exp);

        if (exp instanceof AlloyCardExpr) return TlaStdLibs.Cardinality(translate(exp));

        if (exp instanceof AlloyNegExpr) return TlaNot(translate(exp));

        return ERROR;
    }

    public static TlaExp translateAlloyBinaryExpr(AlloyBinaryExpr exp) {
        return ERROR;
    }

    public static TlaExp translateAlloyQtExpr(AlloyQtExpr exp) {

        if (exp.qt == AlloyQtExpr.Quant.SOME) return _SOME(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.LONE) return _SOME(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.ONE) return _ONE(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.NO) return _NO(translate(exp.sub));
        return ERROR;
    }

    public static TlaExp translateAlloySigRefExp(AlloySigRefExpr exp) {
        if (exp instanceof AlloyQnameExpr) {
            String label = ((AlloyQnameExpr) exp).label;
            return TlaVar(label);
        }
        if (exp instanceof AlloyUnivExpr) {
            return _UNIV();
        }
        if (exp instanceof AlloyNoneExpr) {
            return _NONE();
        }
        return ERROR;
    }
}
