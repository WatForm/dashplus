package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.Boilerplate.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.utils.ImplementationError;

public class AlloyArityVis implements AlloyExprVis<Integer> {

    public static final Integer BOOLEAN_ARITY = Integer.valueOf(0);
    public static final Integer UNKNOWN_ARITY = Integer.valueOf(-1);

    @Override
    public Integer visit(DashRef dashRef) {

        throw ImplementationError.notSupported("dashref inside pure AlloyVis");
    }

    @Override
    public Integer visit(DashParam dashParam) {

        throw ImplementationError.notSupported("dashParam inside pure AlloyVis");
    }

    @Override
    public Integer visit(AlloyBinaryExpr binExpr) {

        Integer el = this.visit(binExpr.left);
        Integer er = this.visit(binExpr.right);

        Integer answer =
                switch (binExpr) {
                    case AlloyAndExpr _ -> BOOLEAN_ARITY;
                    case AlloyArrowExpr _ -> el + er;
                    case AlloyCmpExpr exp -> BOOLEAN_ARITY;
                    case AlloyDiffExpr _ -> el;
                    case AlloyDomRestrExpr _ -> er - 1;
                    case AlloyDotExpr _ -> el + er - 2;
                    case AlloyEqualsExpr _ -> BOOLEAN_ARITY;
                    case AlloyIffExpr _ -> BOOLEAN_ARITY;
                    case AlloyImpliesExpr _ -> BOOLEAN_ARITY;
                    case AlloyIntersExpr _ -> el;
                    case AlloyNotEqualsExpr _ -> Integer.valueOf(0);
                    case AlloyOrExpr _ -> BOOLEAN_ARITY;
                    case AlloyRelOvrdExpr _ -> el;
                    case AlloyRngRestrExpr _ -> el - 1;
                    case AlloyUnionExpr _ -> el;
                    default -> null;
                };

        if (answer != null) return answer;

        throw ImplementationError.notSupported(
                "non-translatable expression: " + binExpr.toString());
    }

    @Override
    public Integer visit(AlloyUnaryExpr unaryExpr) {

        Integer e = visit(unaryExpr.sub);

        Integer answer =
                switch (unaryExpr) {
                    case AlloyCardExpr _ -> BOOLEAN_ARITY;
                    case AlloyNegExpr _ -> BOOLEAN_ARITY;
                    case AlloyQtExpr exp ->
                            switch (exp.qt) {
                                case AlloyQtEnum.SOME -> BOOLEAN_ARITY;
                                case AlloyQtEnum.LONE -> BOOLEAN_ARITY;
                                case AlloyQtEnum.NO -> BOOLEAN_ARITY;
                                case AlloyQtEnum.ONE -> BOOLEAN_ARITY;
                                case AlloyQtEnum.ALL -> BOOLEAN_ARITY;
                                default -> null;
                            };
                    case AlloyTransExpr _ -> e;
                    default -> null;
                };

        if (answer != null) return Integer.valueOf(0);

        throw ImplementationError.notSupported(
                "non-translatable expression: " + unaryExpr.toString());
    }

    @Override
    public Integer visit(AlloyVarExpr varExpr) {

        TlaExp answer =
                switch (varExpr) {
                    case AlloyUnivExpr _ -> _UNIV();
                    case AlloyIdenExpr _ -> _IDEN();
                    case AlloyNoneExpr _ -> _NONE();
                    default -> null;
                };

        if (answer != null) return Integer.valueOf(0);

        throw ImplementationError.notSupported(
                "non-translatable expression: " + varExpr.toString());
    }

    @Override
    public Integer visit(AlloyBlock block) {

        return Integer.valueOf(0);
    }

    @Override
    public Integer visit(AlloyBracketExpr bracketExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for bracket");
    }

    @Override
    public Integer visit(AlloyCphExpr comprehensionExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for cph");
    }

    @Override
    public Integer visit(AlloyIteExpr iteExpr) {

        return this.visit(iteExpr.conseq);
    }

    @Override
    public Integer visit(AlloyLetExpr letExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for let");
    }

    @Override
    public Integer visit(AlloyQuantificationExpr quantificationExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for quantification");
    }

    @Override
    public Integer visit(AlloyDecl decl) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for decl");
    }
}
