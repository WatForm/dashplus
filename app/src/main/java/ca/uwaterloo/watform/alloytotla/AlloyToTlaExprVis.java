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
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AlloyToTlaExprVis implements AlloyExprVis<AlloyToTlaExprVis.Result> {

    public final Logger l;
    public final AlloyModel am;

    public static sealed interface Result permits TlaExpResult, MacroResult {}

    public static record TlaExpResult(TlaExp exp) implements Result {}

    public static record MacroResult(int numArgs, String name, List<TlaExp> args)
            implements Result {}

    public TlaExp extract(Result r) {
        switch (r) {
            case TlaExpResult e:
                return e.exp;
            default:
                throw new ImplementationError(
                        "error: unexpected result " + r.toString() + " of type " + r.getClass());
        }
    }

    public AlloyToTlaExprVis(AlloyModel am, Logger l) {
        this.l = l;
        this.am = am;
    }

    public void info(AlloyExpr e) {
        l.info("translating: " + e.toString() + " of type:" + e.getClass());
    }

    @Override
    public Result visit(DashRef dashRef) {

        throw ImplementationError.notSupported("dashref inside pure AlloyVis");
    }

    public Result translateDot(AlloyDotExpr e) {

        var left = extract(visit(e.left));
        var rightResult = visit(e.right);
        switch (rightResult) {
            case TlaExpResult tlaExpResultRight:
                {
                    var answer = _INNER_PRODUCT(left, extract(tlaExpResultRight));
                    return new TlaExpResult(answer);
                }
            case MacroResult macroResultRight:
                {
                    List<TlaExp> args = new ArrayList<>();
                    args.addAll(macroResultRight.args);
                    args.add(left);

                    if (args.size() == macroResultRight.numArgs) {
                        var answer = TlaAppl(macroResultRight.name, args);
                        return new TlaExpResult(answer);
                    } else
                        return new MacroResult(
                                macroResultRight.numArgs, macroResultRight.name, args);
                }
        }
    }

    @Override
    public Result visit(AlloyBinaryExpr binExpr) {

        info(binExpr);

        if (binExpr.getClass() == AlloyDotExpr.class) return translateDot((AlloyDotExpr) binExpr);

        TlaExp el = extract(this.visit(binExpr.left));
        TlaExp er = extract(this.visit(binExpr.right));

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

        if (answer != null) return new TlaExpResult(answer);

        throw ImplementationError.notSupported(
                "non-translatable expression: " + binExpr.toString());
    }

    @Override
    public Result visit(AlloyUnaryExpr unaryExpr) {

        info(unaryExpr);

        TlaExp e = extract(visit(unaryExpr.sub));

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

        if (answer != null) return new TlaExpResult(answer);

        throw ImplementationError.notSupported(
                "non-translatable expression: " + unaryExpr.toString());
    }

    public Result translateQnameExpr(AlloyQnameExpr exp) {

        if (!am.allPreds().contains(exp.label)) return new TlaExpResult(TlaAppl(exp.label));

        int numArgs = am.numArgs(exp.label);

        l.info("translating macro " + exp.label + " with " + numArgs + " args ");

        if (numArgs == 0) return new TlaExpResult(TlaAppl(exp.label));

        return new MacroResult(numArgs, exp.label, new ArrayList<TlaExp>());
    }

    @Override
    public Result visit(AlloyVarExpr varExpr) {

        info(varExpr);

        TlaExp answer =
                switch (varExpr) {
                    case AlloyUnivExpr _ -> _UNIV();
                    case AlloyIdenExpr _ -> _IDEN();
                    case AlloyNoneExpr _ -> _NONE();
                    default -> null;
                };

        if (answer != null) return new TlaExpResult(answer);

        if (varExpr.getClass() == AlloyQnameExpr.class)
            return translateQnameExpr((AlloyQnameExpr) varExpr);

        throw ImplementationError.notSupported(
                "non-translatable expression: " + varExpr.toString());
    }

    @Override
    public Result visit(AlloyBlock block) {

        info(block);

        var answer = CreateHelper.repeatedAnd(mapBy(block.exprs, e -> extract(visit(e))));
        return new TlaExpResult(answer);
    }

    @Override
    public Result visit(AlloyBracketExpr bracketExpr) {

        info(bracketExpr);

        /*
        a[b] = b.a
        */

        l.info("expr " + bracketExpr.expr);
        l.info("exprs " + bracketExpr.exprs);

        var leftResult = visit(bracketExpr.expr);

        switch (leftResult) {
            case TlaExpResult leftTlaExpResult:
                {
                    if (bracketExpr.exprs.size() != 1)
                        throw new ImplementationError(
                                "malformed bracket expression: " + bracketExpr);
                    var right = bracketExpr.exprs.get(0);
                    var answer = _INNER_PRODUCT(extract(visit(right)), leftTlaExpResult.exp);
                    return new TlaExpResult(answer);
                }
            case MacroResult leftMacroResult:
                {
                    var args = new ArrayList<TlaExp>();
                    args.addAll(leftMacroResult.args);
                    args.addAll(mapBy(bracketExpr.exprs, e -> extract(visit(e))));
                    if (args.size() == leftMacroResult.numArgs) {
                        var answer = TlaAppl(leftMacroResult.name, args);
                        return new TlaExpResult(answer);
                    } else {
                        return new MacroResult(leftMacroResult.numArgs, leftMacroResult.name, args);
                    }
                }
        }
    }

    @Override
    public Result visit(AlloyCphExpr comprehensionExpr) {

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
        List<TlaExp> expressions = mapBy(comprehensionExpr.decls, d -> extract(visit(d.expr)));

        var product = repeatedProductSet(expressions);

        var head =
                vars.size() == 1
                        ? TlaQuantOpHeadFlat(vars, product)
                        : TlaQuantOpHeadTuple(vars, product);

        var condition = comprehensionExpr.body.map(e -> extract(visit(e))).orElse(TlaTrue());

        var answer = TlaSetFilter(head, condition);
        return new TlaExpResult((TlaExp) answer);
    }

    @Override
    public Result visit(AlloyIteExpr iteExpr) {

        info(iteExpr);

        var condition = extract(this.visit(iteExpr.cond));
        var conseq = extract(this.visit(iteExpr.conseq));
        var alt = extract(this.visit(iteExpr.alt));

        var answer = TlaIfThenElse(condition, conseq, alt);

        return new TlaExpResult(answer);
    }

    @Override
    public Result visit(AlloyLetExpr letExpr) {

        info(letExpr);
        /*
        note that let expressions in TLA+ can have params, but cannot in ALloy
        let expressions are translated directly, since TLA+ has a more expressive system for let expressions
        no de-sugaring-via-substitution occurs
        */

        var asns =
                mapBy(
                        letExpr.asns,
                        asn -> TlaDefn(TlaDecl(asn.qname.label), extract(visit(asn.expr))));
        var body = extract(visit(letExpr.body));
        var answer = new TlaLetBinding(asns, body);
        return new TlaExpResult(answer);
        // throw ImplementationError.notSupported("Unimplemented method 'visit' for let");
    }

    @Override
    public Result visit(AlloyQuantificationExpr quantificationExpr) {
        info(quantificationExpr);

        l.info("quant: " + quantificationExpr.quant);
        l.info("body:" + quantificationExpr.body);
        l.info("decls:" + quantificationExpr.decls);
        /*

        all x : T | expr
        -> \A x \in T : expr

        all x: A, y: B | expr
        -> \A x \in A : \A y \in B : expr

        no x : A | expr
        -> ~(\E x \in A : expr)

        some x : A | expr
        -> _some({x \in A : expr})

        some x : A, y : B | expr
        -> _some({x \in A, y \in B : expr })
        and so on, no and all are special cases

        decl visitor not called
        */
        TlaExp answer = extract(visit(quantificationExpr.body));
        switch (quantificationExpr.quant) {
            case AlloyQuantificationExpr.Quant.ALL:
                {
                    for (var decl : quantificationExpr.decls) {
                        var head =
                                TlaQuantOpHead(TlaVar(decl.getName()), extract(visit(decl.expr)));
                        answer = TlaForAll(head, answer);
                    }
                    break;
                }
            case AlloyQuantificationExpr.Quant.NO:
                {
                    for (var decl : quantificationExpr.decls) {
                        var head =
                                TlaQuantOpHead(TlaVar(decl.getName()), extract(visit(decl.expr)));
                        answer = TlaExists(head, answer);
                    }
                    answer = TlaNot(answer);
                    break;
                }
            case AlloyQuantificationExpr.Quant.SUM:
                {
                    throw ImplementationError.notSupported(
                            "Unsupported translation for sum quantification");
                }
            default:
                {
                    List<TlaVar> headVars =
                            mapBy(quantificationExpr.decls, decl -> TlaVar(decl.getName()));
                    List<TlaExp> headExps =
                            mapBy(quantificationExpr.decls, decl -> extract(visit(decl.expr)));
                    // todo fold right and switch
                    // var set = TlaSetFilter(TlaQuantOpHeadTuple(headVars,), answer)
                    throw ImplementationError.notSupported(
                            "Unimplemented method 'visit' for quantification");
                }
        }
        return new TlaExpResult(answer);
    }

    @Override
    public Result visit(AlloyDecl decl) {

        info(decl);

        l.info("expr " + decl.expr);
        l.info("isDisj2 " + decl.isDisj1);
        l.info("isDisj1 " + decl.isDisj2);
        l.info("qnames " + decl.qnames);
        l.info("isVar " + decl.isVar);
        l.info("isprivate " + decl.isPrivate);
        l.info("mul " + decl.mul);

        throw ImplementationError.notSupported("Unimplemented method 'visit' for decl");
    }
}
