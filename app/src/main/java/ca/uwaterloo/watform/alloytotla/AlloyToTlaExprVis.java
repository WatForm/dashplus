package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.Boilerplate.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

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

public class AlloyToTlaExprVis implements AlloyExprVis<TlaExp> {

    @Override
    public TlaExp visit(DashRef dashRef) {

        throw ImplementationError.notSupported("dashref inside pure AlloyVis");
    }

    @Override
    public TlaExp visit(DashParam dashParam) {

        throw ImplementationError.notSupported("dashParam inside pure AlloyVis");
    }

    @Override
    public TlaExp visit(AlloyBinaryExpr binExpr) {

        TlaExp el = this.visit(binExpr.left);
        TlaExp er = this.visit(binExpr.right);

        TlaExp answer =
                switch (binExpr) {
                    case AlloyAndExpr _ -> TlaAnd(el, er);
                    case AlloyArrowExpr _ -> TlaProductSet(el, er);
                    case AlloyCmpExpr exp ->
                            switch (exp.comp) {
                                case AlloyCmpExpr.Comp.EQUAL_LESS -> TlaLesserEq(el, er);
                                case AlloyCmpExpr.Comp.LESS_EQUAL -> TlaLesserEq(el, er);
                                case AlloyCmpExpr.Comp.LESS_THAN -> TlaLesser(el, er);
                                case AlloyCmpExpr.Comp.IN -> TlaSubsetEq(el, er);
                                case AlloyCmpExpr.Comp.GREATER_EQUAL -> TlaGreater(el, er);
                                case AlloyCmpExpr.Comp.GREATER_THAN -> TlaGreaterEq(el, er);
                            };
                    case AlloyDiffExpr _ -> TlaDiffSet(el, er);
                    case AlloyDomRestrExpr _ -> _DOMAIN_RESTRICTION(el, er);
                    case AlloyDotExpr _ -> _INNER_PRODUCT(el, er);
                    case AlloyEqualsExpr _ -> TlaEquals(el, er);
                    case AlloyIffExpr _ -> TlaEquivalence(el, er);
                    case AlloyImpliesExpr _ -> TlaImplies(el, er);
                    case AlloyIntersExpr _ -> TlaIntersectionSet(el, er);
                    case AlloyNotEqualsExpr _ -> TlaNotEq(el, er);
                    case AlloyOrExpr _ -> TlaOr(el, er);
                    case AlloyRelOvrdExpr _ -> _RELATIONAL_OVERRIDE(el, er);
                    case AlloyRngRestrExpr _ -> _RANGE_RESTRICTION(el, er);
                    case AlloyUnionExpr _ -> TlaUnionSet(el, er);
                    default -> null;  /* case  _ -> (el, er); */
                };

        if (answer != null) return answer;

        throw ImplementationError.notSupported(
                "non-translatable expression: " + binExpr.toString());
    }

    @Override
    public TlaExp visit(AlloyUnaryExpr unaryExpr) {

        TlaExp e = visit(unaryExpr.sub);

        TlaExp answer =
                switch (unaryExpr) {
                    case AlloyCardExpr _ -> TlaStdLibs.Cardinality(e);
                    case AlloyNegExpr _ -> TlaNot(e);
                    case AlloyQtExpr exp ->
                            switch (exp.qt) {
                                case AlloyQtEnum.SOME -> _SOME(e);
                                case AlloyQtEnum.LONE -> _LONE(e);
                                case AlloyQtEnum.NO -> _NO(e);
                                case AlloyQtEnum.ONE -> _ONE(e);
                                case AlloyQtEnum.ALL -> TlaTrue();
                                default -> null;
                            };
                    case AlloyTransExpr _ -> _TRANSPOSE(e);
                    default -> null;
                };

        if (answer != null) return answer;

        throw ImplementationError.notSupported(
                "non-translatable expression: " + unaryExpr.toString());
    }

    @Override
    public TlaExp visit(AlloyVarExpr varExpr) {

        TlaExp answer =
                switch (varExpr) {
                    case AlloyUnivExpr _ -> _UNIV();
                    case AlloyIdenExpr _ -> _IDEN();
                    case AlloyNoneExpr _ -> _NONE();
                    default -> null;
                };

        if (answer != null) return answer;

        throw ImplementationError.notSupported(
                "non-translatable expression: " + varExpr.toString());
    }

    @Override
    public TlaExp visit(AlloyBlock block) {

        return CreateHelper.repeatedAnd(mapBy(block.exprs, e -> this.visit(e)));
    }

    @Override
    public TlaExp visit(AlloyBracketExpr bracketExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for bracket");
    }

    @Override
    public TlaExp visit(AlloyCphExpr comprehensionExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for cph");
    }

    @Override
    public TlaExp visit(AlloyIteExpr iteExpr) {

        return CreateHelper.TlaIfThenElse(
                this.visit(iteExpr.cond), this.visit(iteExpr.conseq), this.visit(iteExpr.alt));
    }

    @Override
    public TlaExp visit(AlloyLetExpr letExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for let");
    }

    @Override
    public TlaExp visit(AlloyQuantificationExpr quantificationExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for quantification");
    }

    @Override
    public TlaExp visit(AlloyDecl decl) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for decl");
    }
}
