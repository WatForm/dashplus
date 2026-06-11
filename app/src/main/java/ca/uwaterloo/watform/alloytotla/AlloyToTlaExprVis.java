package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.BoilerplateA2T.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloytotla.AlloyToTlaExprVis.AlloyToTlaTranslationContext;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class AlloyToTlaExprVis implements AlloyExprVis<AlloyToTlaTranslationContext> {

    public final Logger l;
    public final AlloyModel am;

    public static sealed interface Result permits TlaExpResult, MacroResult {}
    public static record TlaExpResult(TlaExp e) implements Result {}
    public static record MacroResult(int numArgs, String name, List<TlaExp> args) implements Result {}

    public AlloyToTlaExprVis(AlloyModel am, Logger l) {
        this.l = l;
        this.am = am;
    }

    public void info(AlloyExpr e) {
        l.info("translating: " + e.toString() + " of type:" + e.getClass());
    }

    public sealed interface TranslationContext permits PredicateContext {}

    public record PredicateContext(int num_args, List<? extends TlaExp> args, AlloyQnameExpr pred)
            implements TranslationContext {}

    public static class AlloyToTlaTranslationContext {
        public final TlaExp core;
        public final List<TranslationContext> stack;

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

    public AlloyToTlaTranslationContext translateDot(AlloyDotExpr e) {

        l.info("Dot expr translation");
        info(e);
        /*
        p[x,y,z]
        x.p[y,z]
        x.y.p[z]
        x.y.z.p


        f[a,b,c]
        g[x,y,z]

        x.y.g[a.b.c.f] == x.y.(a.b.c.f).g

        NOTE: this works properly

        */

        /*
        in case of dot_expr:
        take the right context, if the core is not null then the interesting case happens
        if the stack is empty, then normal translation
        if the stack is not, pick an arbitrary predicate element of the stack, ep
        add left.core to the front of ep-args
        if ep-args length now matches the expected length, then convert
        */

        var left = visit(e.left);
        var right = visit(e.right);

        if (right.core != null) {
            return new AlloyToTlaTranslationContext(_INNER_PRODUCT(left.core, right.core));
        }

        try {

            l.info("entering try block");

            var predCtxt =
                    (PredicateContext)
                            (filterBy(right.stack, x -> x.getClass() == PredicateContext.class)
                                    .get(0));

            l.info("extracted predctxt: " + predCtxt);

            var newargs = new ArrayList<TlaExp>();
            newargs.add(left.core);
            l.info("instantiated newargs");
            try {
                for (var arg : predCtxt.args) newargs.add((TlaExp) arg);
            } catch (Exception ec) {
                l.info("isolated the problem");
            }
            l.info("new args is:" + newargs.toString());
            l.info("num_args is:" + predCtxt.num_args);

            if (newargs.size() == predCtxt.num_args) {

                l.info("collapsing case:");

                var answerCore = TlaAppl(predCtxt.pred.label, newargs);
                var answer = new AlloyToTlaTranslationContext(answerCore);
                answer.stack.addAll(left.stack);
                answer.stack.addAll(filterBy(right.stack, x -> !x.equals(predCtxt)));
                return answer;
            } else {

                l.info("non-collapsing case");

                var answer = new AlloyToTlaTranslationContext(null);

                l.info("answer made");

                answer.stack.addAll(left.stack);

                l.info("left added");

                answer.stack.addAll(filterBy(right.stack, x -> !x.equals(predCtxt)));

                l.info("right added");

                var newPredCtxt = new PredicateContext(predCtxt.num_args, newargs, predCtxt.pred);
                answer.stack.add(newPredCtxt);

                l.info("new predctxt added");

                l.info("new answer core: " + answer.core);
                l.info("new answer stack: " + answer.stack);

                return answer;
            }
        } catch (Exception exception) {

            l.info("there is an error here");
            l.info("trying to translate:" + e);
            l.info("e.right is " + e.right);
            l.info("e.left is: " + e.left);
            l.info("right core is: " + right.core);
            l.info("right stack is:" + right.stack);
            l.info("left core is: " + left.core);
            throw ImplementationError.notSupported(
                    "something went wrong with predicate arguments - "
                            + exception.toString()
                            + " in "
                            + e.toString());
        }
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyBinaryExpr binExpr) {

        info(binExpr);

        if (binExpr.getClass() == AlloyDotExpr.class) return translateDot((AlloyDotExpr) binExpr);

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

        info(unaryExpr);

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

    public AlloyToTlaTranslationContext translateQnameExpr(AlloyQnameExpr exp) {
        if (!am.allPreds().contains(exp.label))
            return new AlloyToTlaTranslationContext(TlaAppl(exp.label));

        int num_args = am.predArity(exp.label) - 1;

        l.info("translating " + exp.label + " of args " + num_args);
        // pred of arity 1 has zero args
        if (num_args == 0) return new AlloyToTlaTranslationContext(TlaAppl(exp.label));

        var answer = new AlloyToTlaTranslationContext(null);
        answer.stack.add(new PredicateContext(num_args, new ArrayList<>(), exp));

        return answer;
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyVarExpr varExpr) {

        info(varExpr);

        TlaExp answer =
                switch (varExpr) {
                    case AlloyUnivExpr _ -> _UNIV();
                    case AlloyIdenExpr _ -> _IDEN();
                    case AlloyNoneExpr _ -> _NONE();
                    default -> null;
                };

        if (varExpr.getClass() == AlloyQnameExpr.class) {
            return translateQnameExpr((AlloyQnameExpr) varExpr);
        }

        if (answer != null) {
            var context = new AlloyToTlaTranslationContext(answer);
            return context;
        } else {
            if (varExpr.getClass() == AlloyQnameExpr.class) {
                return translateQnameExpr((AlloyQnameExpr) varExpr);
            }
            throw ImplementationError.notSupported(
                    "non-translatable expression: " + varExpr.toString());
        }
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyBlock block) {

        info(block);

        List<AlloyToTlaTranslationContext> statementContexts =
                mapBy(block.exprs, e -> this.visit(e));
        TlaExp answer = CreateHelper.repeatedAnd(mapBy(statementContexts, c -> c.extract()));
        var answerContext = new AlloyToTlaTranslationContext(answer);
        for (var sc : statementContexts) answerContext.stack.addAll(sc.stack);
        return answerContext;
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyBracketExpr bracketExpr) {

        info(bracketExpr);

        throw ImplementationError.notSupported("Unimplemented method 'visit' for bracket");
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyCphExpr comprehensionExpr) {

        info(comprehensionExpr);

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

        info(iteExpr);

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

        info(letExpr);

        throw ImplementationError.notSupported("Unimplemented method 'visit' for let");
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyQuantificationExpr quantificationExpr) {
        info(quantificationExpr);
        throw ImplementationError.notSupported("Unimplemented method 'visit' for quantification");
    }

    @Override
    public AlloyToTlaTranslationContext visit(AlloyDecl decl) {

        info(decl);
        throw ImplementationError.notSupported("Unimplemented method 'visit' for decl");
    }
}
