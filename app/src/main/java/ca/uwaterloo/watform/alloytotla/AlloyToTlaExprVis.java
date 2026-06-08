package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.BoilerplateA2T.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloytotla.AlloyToTlaExprVis.AlloyToTlaTranslationContext;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AlloyToTlaExprVis implements AlloyExprVis<AlloyToTlaTranslationContext> {

    public static class AlloyToTlaTranslationContext {
        public final TlaExp core;
        public final List<AlloyASTNode> stack;

        public AlloyToTlaTranslationContext(TlaExp core) {
            this.core = core;
            this.stack = new ArrayList<>();
        }

        public TlaExp extract() {
            return this.core;
        }
    }

    @Override
    public AlloyToTlaTranslationContext visit(DashRef dashRef) {

        throw ImplementationError.notSupported("dashref inside pure AlloyVis");
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyBinaryExpr binExpr) {

        var leftContext = this.visit(binExpr.left);
        var rightContext = this.visit(binExpr.right);
        TlaExp el = leftContext.extract();
        TlaExp er = rightContext.extract();

        TlaExp answer =
                switch (binExpr) {
                    case AlloyAndExpr _ -> TlaAnd(el, er);
                    case AlloyArrowExpr _ -> _CROSS(el, er);
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
                    default -> null; /* case  _ -> (el, er); */
                };

        if (answer != null) {
            var answerContext = new AlloyToTlaTranslationContext(answer);
            answerContext.stack.addAll(leftContext.stack);
            answerContext.stack.addAll(rightContext.stack);
            return answerContext;
        } else
            throw ImplementationError.notSupported(
                    "non-translatable expression: " + binExpr.toString());
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyUnaryExpr unaryExpr) {

        var context = visit(unaryExpr.sub);
        TlaExp e = context.extract();

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

        if (answer != null) {
            return new AlloyToTlaTranslationContext(answer);
        } else
            throw ImplementationError.notSupported(
                    "non-translatable expression: " + unaryExpr.toString());
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyVarExpr varExpr) {

        TlaExp answer =
                switch (varExpr) {
                    case AlloyUnivExpr _ -> _UNIV();
                    case AlloyIdenExpr _ -> _IDEN();
                    case AlloyNoneExpr _ -> _NONE();
                    case AlloyQnameExpr e -> TlaAppl(e.label);
                    default -> null;
                };

        if (answer != null) {
            var context = new AlloyToTlaTranslationContext(answer);
            return context;
        } else
            throw ImplementationError.notSupported(
                    "non-translatable expression: " + varExpr.toString());
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyBlock block) {

        List<AlloyToTlaTranslationContext> statementContexts =
                mapBy(block.exprs, e -> this.visit(e));
        TlaExp answer = CreateHelper.repeatedAnd(mapBy(statementContexts, c -> c.extract()));
        var answerContext = new AlloyToTlaTranslationContext(answer);
        for (var sc : statementContexts) answerContext.stack.addAll(sc.stack);
        return answerContext;
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyBracketExpr bracketExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for bracket");
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyCphExpr comprehensionExpr) {

        /*
        alloy:
        {x1: e1, x2: e2, ... | F}
        F is optional, if it is null then it is a tautology
        F is a boolean condition
        it results in the set of tuples (x1,x2...) where x1 is drawn from e1, x2 from e2 and so on, such that F is true

        TLA:
        set map {exp : v \in S}
        set filter  {v \in S : exp}

        {x1: e1 | F} is translated into {x1 \in e1 : F}
        {x1: e1, x2 : e2 | F} translated into {<<x1,x2>> \in e1 \X e2 : F}
        */

        var vars = mapBy(comprehensionExpr.decls, d -> TlaVar(d.qnames.get(0).toString()));
        List<AlloyToTlaTranslationContext> expressionContexts =
                mapBy(comprehensionExpr.decls, d -> visit(d.expr));
        List<TlaExp> expressions = mapBy(expressionContexts, e -> e.extract());

        var product = repeatedProductSet(expressions);

        var head =
                vars.size() == 1
                        ? TlaQuantOpHeadFlat(vars, product)
                        : TlaQuantOpHeadTuple(vars, product);

        var condition = new AtomicReference<TlaExp>(TlaTrue());
        comprehensionExpr.body.ifPresent(e -> condition.set(visit(e).core));

        var answer = TlaSetFilter(head, condition.get());
        var answerContext = new AlloyToTlaTranslationContext(answer);

        for (var ec : expressionContexts) answerContext.stack.addAll(ec.stack);
        return answerContext;
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyIteExpr iteExpr) {

        var conditionContext = this.visit(iteExpr.cond);
        var conseqContext = this.visit(iteExpr.conseq);
        var altContext = this.visit(iteExpr.alt);

        TlaExp answer =
                CreateHelper.TlaIfThenElse(
                        conditionContext.core, conseqContext.core, altContext.core);

        var answerContext = new AlloyToTlaTranslationContext(answer);
        answerContext.stack.addAll(conditionContext.stack);
        answerContext.stack.addAll(conseqContext.stack);
        answerContext.stack.addAll(altContext.stack);
        return answerContext;
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyLetExpr letExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for let");
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyQuantificationExpr quantificationExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for quantification");
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyDecl decl) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for decl");
    }
}
