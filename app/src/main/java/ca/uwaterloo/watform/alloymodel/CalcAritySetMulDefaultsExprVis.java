/*

Calculate arities and
set default multiplicities in any expr
* every visit function returns a Result that contains both an arity and an expr with mul defaults set within it
* if there are arities errors or we can't calc arity enough to figure out mul defaults, an AlloyModelError exception is thrown

1) Calculate arities
* a certain about of UNKNOWN is tolerated; we can't yet know the arity of bracketExpr (p[a,b,c])
so errors are only thrown if we NEED to know that arity to set mul defaults.
* Booleans are treated as arity 1 (no typechecking is done) so that predicates can be processed as both p[a,b,c] and c.b.a.p

2) Mul Defaults:
* if an arrow doesn't have a mul, it becomes SET
* in a decl, if expr is unary and no mul, mul becomes ONE
* in a decl, if expr is not unary and no mul, mul becomes SET and issues a Warning

Errors detected
    - decls of a comprehension expr must be unary sets
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.Builtins.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.Reporter.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Reporter;
import java.util.*;
import java.util.function.BiFunction;

public class CalcAritySetMulDefaultsExprVis
        implements AlloyExprVis<CalcAritySetMulDefaultsExprVis.Result> {

    // provided function on initialization
    // gets the arity for an individual name
    private BiFunction<String, Optional<String>, Optional<Integer>> symbolArity;
    private Optional<String> sigParent = Optional.empty();

    // ---------
    // need a context of arities for let expressions, quantified variables, etc.
    // needs to be a stack to pop vars on and off

    private Deque<Map.Entry<String, Optional<Integer>>> localArities = new ArrayDeque<>();

    private void localPush(String key, Optional<Integer> value) {
        localArities.push(new AbstractMap.SimpleEntry<>(key, value));
    }

    private void localPop() {
        localArities.pop();
    }

    private Optional<Integer> localLookup(String key) {
        for (Map.Entry<String, Optional<Integer>> entry : localArities) {
            if (entry.getKey().equals(key)) {
                return entry.getValue(); // first match = most recent
            }
        }
        return Optional.empty();
    }

    // visit() returns a Result
    // instead of using Optional<Integer>, we used arity of UNKNOWN
    // if arity is UNKNOWN then exp is empty

    class Result {
        final Optional<Integer> arity;
        final AlloyExpr exp;

        Result(Optional<Integer> arity, AlloyExpr exp) {
            this.arity = arity;
            this.exp = exp;
        }

        Result(AlloyExpr exp) {
            this.arity = UNKNOWN_ARITY;
            this.exp = exp;
        }
    }

    // -------

    // constructor
    public CalcAritySetMulDefaultsExprVis(
            BiFunction<String, Optional<String>, Optional<Integer>> symbolArity) {
        // set up empty stack for localArities
        this.localArities = new ArrayDeque<>();
        // this is the symbol table for arities of symbols
        // some of it may not be filled in yet
        this.symbolArity = symbolArity;
    }

    // one kind of top-level call
    public Result fieldArityAndSetMul(AlloyExpr e, String sigParent) {
        this.sigParent = Optional.of(sigParent);
        return this.visit(e);
    }

    // other top-level call
    public Result setMul(AlloyExpr e) {
        this.sigParent = Optional.empty();
        return this.visit(e);
    }

    private Optional<Integer> arity(String n) {
        Optional<Integer> x = localLookup(n);
        return x.isPresent() ? x : this.symbolArity.apply(n, this.sigParent);
    }

    // for use by a predicate or function paragraphs in Alloy
    // it has local decls
    public void localEnvPush(List<AlloyDecl> decls) {
        for (AlloyDecl d : decls) {
            Result dResult = this.visit(d.expr);
            localPush(d.getName(), dResult.arity);
        }
    }

    public void localEnvPop(List<AlloyDecl> decls) {
        for (AlloyDecl d : decls) {
            localPop();
        }
    }

    @Override
    public Result visit(DashRef dashRef) {
        throw ImplementationError.notSupported(
                "dashref inside ArityVis yet: " + dashRef.toString());
    }

    /*
    @Override
    public Result visit(DashParam dashParam) {
        throw ImplementationError.notSupported(
                "dashParam inside ArityVis yet: " + dashParam.toString());
    }
    */

    // expr.binary ----------------------------------------

    @Override
    public Result visit(AlloyBinaryExpr binExpr) {

        Set<Class<?>> returnArityOfArg =
                Set.of(
                        AlloyDiffExpr.class,
                        AlloyIntersExpr.class,
                        AlloyRelOvrdExpr.class,
                        AlloyUnionExpr.class,
                        AlloyFunAddExpr.class,
                        AlloyFunMulExpr.class,
                        AlloyFunRemExpr.class,
                        AlloyFunSubExpr.class);

        Set<Class<?>> argsMustHaveEqualArity = new HashSet<>(returnArityOfArg);
        argsMustHaveEqualArity.add(AlloyEqualsExpr.class);
        argsMustHaveEqualArity.add(AlloyNotEqualsExpr.class);
        argsMustHaveEqualArity.add(AlloyCmpExpr.class);

        Set<Class<?>> mustHaveBooleanArgs =
                Set.of(
                        AlloyAndExpr.class,
                        AlloyOrExpr.class,
                        AlloyIffExpr.class,
                        AlloyImpliesExpr.class,
                        AlloyReleasesExpr.class,
                        AlloyShAExpr.class,
                        AlloyShLExpr.class,
                        AlloyShRExpr.class,
                        AlloySinceExpr.class,
                        AlloyStateSeqExpr.class,
                        AlloyTriggeredExpr.class,
                        AlloyUntilExpr.class);

        Set<Class<?>> returnBooleanArity = new HashSet<>(mustHaveBooleanArgs);
        returnBooleanArity.add(AlloyEqualsExpr.class);
        returnBooleanArity.add(AlloyNotEqualsExpr.class);
        returnBooleanArity.add(AlloyCmpExpr.class);

        Result leftResult = this.visit(binExpr.left);
        // System.out.println(leftResult.exp.toString() + leftResult.arity.toString());
        Result rightResult = this.visit(binExpr.right);
        // System.out.println("right in:");
        // System.out.println(rightResult.exp);
        // System.out.println(rightResult.arity);
        // System.out.println("---");

        // System.out.println("LEFT " + binExpr.left.toString() + " " + leftResult.arity);
        // System.out.println("RIGHT " + binExpr.right.toString() + " " + rightResult.arity);
        // System.out.println(rightResult.exp.toString() + rightResult.arity.toString());
        if (binExpr.getClass().equals(AlloyArrowExpr.class)) {
            // this one needs default multiplicities added
            // if there is no mul provided, default is SET regardless of arity of args
            Optional<AlloyQtEnum> mul1 = ((AlloyArrowExpr) binExpr).mul1;
            if (mul1.isEmpty()) {
                mul1 = Optional.of(AlloyQtEnum.SET);
            }
            Optional<AlloyQtEnum> mul2 = ((AlloyArrowExpr) binExpr).mul2;
            if (mul2.isEmpty()) {
                mul2 = Optional.of(AlloyQtEnum.SET);
            }
            AlloyExpr resultExp =
                    new AlloyArrowExpr(
                            binExpr.pos,
                            leftResult.exp,
                            mul1.orElse(null),
                            mul2.orElse(null),
                            rightResult.exp);

            if (leftResult.arity.equals(UNKNOWN_ARITY)) {
                // System.out.println("Unknown arity: " + binExpr.left.toString());
                return new Result(resultExp);
            }
            if (rightResult.arity.equals(UNKNOWN_ARITY)) {
                // System.out.println("Unknown arity: " + binExpr.right.toString());
                return new Result(resultExp);
            } else {
                // System.out.println("out:");
                // System.out.println(resultExp);
                // System.out.println(leftResult.arity.get() + rightResult.arity.get());
                // System.out.println("---");
                return new Result(
                        Optional.of(leftResult.arity.get() + rightResult.arity.get()), resultExp);
            }
        } else {
            // no other binExpr need default multiplicities added
            AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
            if (mustHaveBooleanArgs.contains(binExpr.getClass())) {
                if (!leftResult.arity.equals(ONE_ARITY)
                        && !leftResult.arity.equals(UNKNOWN_ARITY)) {
                    // System.out.println(leftResult.arity);
                    throw AlloyModelError.mustBeFormula(binExpr.left.pos, binExpr.left.toString());
                } else if (!rightResult.arity.equals(ONE_ARITY)
                        && !rightResult.arity.equals(UNKNOWN_ARITY)) {
                    throw AlloyModelError.mustBeFormula(
                            binExpr.right.pos, binExpr.right.toString());
                }
            }
            if (argsMustHaveEqualArity.contains(binExpr.getClass())) {
                if (!leftResult.arity.equals(rightResult.arity)
                        && !leftResult.arity.equals(UNKNOWN_ARITY)
                        && !rightResult.arity.equals(UNKNOWN_ARITY)) {
                    // arity mismatch error
                    throw AlloyModelError.arityMismatch(
                            binExpr.pos,
                            binExpr.left.toString()
                                    + "("
                                    + Integer.toString(leftResult.arity.get())
                                    + ")",
                            binExpr.right.toString()
                                    + "("
                                    + Integer.toString(rightResult.arity.get())
                                    + ")");
                }
            }
            Optional<Integer> returnArity;
            if (returnBooleanArity.contains(binExpr.getClass())) {
                returnArity = ONE_ARITY;
                // System.out.println(binExpr.getClass().getName());
                // System.out.println(returnArity);

            } else if (returnArityOfArg.contains(binExpr.getClass()))
                returnArity = leftResult.arity;
            else {
                // System.out.println(leftResult.exp);
                // System.out.println(leftResult.arity.get());
                // System.out.println(rightResult.exp);
                // System.out.println(rightResult.arity.get());

                // non-standard binary operators
                switch (binExpr) {
                    case AlloyDomRestrExpr ignored -> {
                        returnArity = Optional.of(rightResult.arity.get());
                    }
                    case AlloyDotExpr ignored -> {
                        if (leftResult.arity.equals(UNKNOWN_ARITY)) {
                            throw AlloyModelError.unknownArity(
                                    binExpr.left.pos, binExpr.left.toString());
                        } else if (rightResult.arity.equals(UNKNOWN_ARITY)) {
                            throw AlloyModelError.unknownArity(
                                    binExpr.right.pos, binExpr.right.toString());
                        }
                        returnArity =
                                Optional.of(leftResult.arity.get() + rightResult.arity.get() - 2);
                    }
                    case AlloyRngRestrExpr ignored -> {
                        returnArity = Optional.of(leftResult.arity.get());
                    }
                    default -> {
                        throw ImplementationError.missingCase(
                                "unknown expression for arity visitor: " + binExpr.toString());
                    }
                }
            }

            return new Result(returnArity, resultExpr);
        }
    }

    // expr.misc ------------------------

    @Override
    public Result visit(AlloyBlock block) {
        List<AlloyExpr> newExprs = emptyList();
        if (block.exprs.size() == 1) {
            // if it is a block of size 1 then it could be a singleton set expr
            Result oneR = this.visit(block.exprs.get(0));
            return new Result(oneR.arity, new AlloyBlock(block.pos, List.of(oneR.exp)));
        } else {
            for (AlloyExpr e : block.exprs) {
                Result r = this.visit(e);
                // let it pass if it is UNKNOWN_ARITY
                if (!r.arity.equals(UNKNOWN_ARITY) && !r.arity.equals(ONE_ARITY)) {
                    // System.out.println(r.arity);
                    throw AlloyModelError.mustBeFormula(e.pos, e.toString());
                }
                newExprs.add(r.exp);
            }
            return new Result(ONE_ARITY, new AlloyBlock(block.pos, newExprs));
        }
    }

    @Override
    public Result visit(AlloyBracketExpr bracketExpr) {
        // p[a,b,c]
        Result exprResult = visit(bracketExpr.expr);
        List<Result> exprsResult = mapBy(bracketExpr.exprs, i -> this.visit(i));
        // System.out.println(bracketExpr.expr);
        // System.out.println(exprResult.arity.get());
        // just counting number of arguments
        if (!exprResult.arity.equals(UNKNOWN_ARITY)) {
            // System.out.println(bracketExpr.expr);
            // System.out.println(exprResult.arity.get());
            // System.out.println(exprsResult.size());
            // TODO: this won't generalize to functions which
            // could have a return arity
            if (exprsResult.size() <= exprResult.arity.get() - 1) {
                /*
                System.out.println(
                        "Return arity: "
                                + Integer.toString(exprResult.arity.get() - exprsResult.size()));
                */
                return new Result(
                        Optional.of(exprResult.arity.get() - exprsResult.size()),
                        new AlloyBracketExpr(
                                bracketExpr.pos, exprResult.exp, mapBy(exprsResult, r -> r.exp)));
            } else {
                throw AlloyModelError.wrongNumberArgs(bracketExpr.pos, bracketExpr.toString());
            }
        } else {
            return new Result(
                    UNKNOWN_ARITY,
                    new AlloyBracketExpr(
                            bracketExpr.pos, exprResult.exp, mapBy(exprsResult, r -> r.exp)));
        }
    }

    @Override
    public Result visit(AlloyCphExpr comprehensionExpr) {
        // {x:X, y: Y} always means {x:one X, y: one Y}
        // no other multiplicities are allowed in a set comprehension Expr
        // each decl must be unary
        Integer declsArity = 0;
        List<AlloyDecl> newDecls = new ArrayList<AlloyDecl>();
        // these decls can never be an empty list
        for (AlloyDecl d : comprehensionExpr.decls) {
            Result dResult = this.visit(d.expr);
            // System.out.println(d.expr.getClass().getName());
            // System.out.println(dResult.exp.getClass().getName());
            if (!dResult.arity.equals(ONE_ARITY)) {
                throw AlloyModelError.mustBeUnary(d.pos, d.toString());
            }
            if (d.mul.isPresent() && !d.mul.get().equals(AlloyQtEnum.ONE)) {
                throw AlloyModelError.mulOfDeclMustBeOne(d.pos, d.toString());
            }
            declsArity += 1;
            newDecls.add(d.rebuild(AlloyQtEnum.ONE, dResult.exp));
            localPush(d.getName(), dResult.arity);
        }

        AlloyExpr bodyExpr;
        if (comprehensionExpr.body.isPresent()) {

            Result bodyResult = this.visit(comprehensionExpr.body.get());
            bodyExpr = bodyResult.exp;
            if (!bodyResult.arity.equals(UNKNOWN_ARITY) && !bodyResult.arity.equals(ONE_ARITY))
                throw AlloyModelError.mustBeFormula(
                        comprehensionExpr.pos,
                        comprehensionExpr.body.toString()
                                + " of arity "
                                + bodyResult.arity.toString());

        } else {
            bodyExpr = null;
        }

        // take them off the stack
        for (AlloyDecl d : comprehensionExpr.decls) {
            localPop();
        }
        return new Result(
                Optional.of(declsArity),
                new AlloyCphExpr(comprehensionExpr.pos, newDecls, bodyExpr));
    }

    @Override
    public Result visit(AlloyDecl declExpr) {
        Result typeResult = this.visit(declExpr.expr);
        Optional<Integer> typeArity = typeResult.arity;
        AlloyExpr newExpr = typeResult.exp;
        AlloyDecl newDecl = null;
        // mul will be empty if it is a x: seq A
        if (declExpr.mul.isEmpty() && !isSeq(newExpr)) {
            if (typeArity.equals(UNKNOWN_ARITY)) {
                throw AlloyModelError.noMulGivenAndCannotBeCalculated(
                        declExpr.pos, declExpr.toString());
            } else if (typeArity.equals(ONE_ARITY)) {
                newDecl = declExpr.rebuild(AlloyQtEnum.ONE, newExpr);
            } else {
                newDecl = declExpr.rebuild(AlloyQtEnum.SET, newExpr);
            }
        } else {
            // mul has already been set or it is x: seq A
            newDecl = declExpr.rebuild(newExpr);
        }
        if (!newDecl.mul.isEmpty()) {
            if (newDecl.mul.get().equals(AlloyQtEnum.ONE) && !typeArity.equals(ONE_ARITY)) {
                throw AlloyModelError.mustBeUnary(declExpr.pos, declExpr.toString());
            }
        }

        return new Result(typeArity, newDecl);
    }

    @Override
    public Result visit(AlloyIteExpr iteExpr) {

        Result condResult = this.visit(iteExpr.cond);
        Result conseqResult = this.visit(iteExpr.conseq);
        Result altResult = this.visit(iteExpr.alt);
        if (!condResult.arity.equals(UNKNOWN_ARITY) && !condResult.arity.equals(ONE_ARITY)) {
            throw AlloyModelError.mustBeFormula(iteExpr.pos, condResult.exp.toString());
        }
        if (!conseqResult.arity.equals(UNKNOWN_ARITY)
                && !altResult.arity.equals(UNKNOWN_ARITY)
                && !conseqResult.arity.equals(altResult.arity)) {
            throw AlloyModelError.arityMismatch(
                    iteExpr.pos, conseqResult.exp.toString(), altResult.exp.toString());
        }
        Optional<Integer> returnArity;
        if (!conseqResult.arity.equals(UNKNOWN_ARITY)) returnArity = conseqResult.arity;
        else
            // could be UNKNOWN_ARITY
            returnArity = conseqResult.arity;
        return new Result(
                returnArity, iteExpr.rebuild(condResult.exp, conseqResult.exp, altResult.exp));
    }

    @Override
    public Result visit(AlloyLetExpr letExpr) {

        List<AlloyLetExpr.AlloyLetAsn> newAsns = new ArrayList<AlloyLetExpr.AlloyLetAsn>();
        for (AlloyLetExpr.AlloyLetAsn l : letExpr.asns) {
            Result lResult = this.visit(l.expr);
            newAsns.add(new AlloyLetExpr.AlloyLetAsn(l.pos, l.qname, lResult.exp));
            localPush(l.getName(), lResult.arity);
        }
        Result bodyResult = this.visit(letExpr.body);
        // take them off the stack
        for (AlloyLetExpr.AlloyLetAsn l : letExpr.asns) {
            localPop();
        }
        return new Result(bodyResult.arity, new AlloyLetExpr(letExpr.pos, newAsns, bodyResult.exp));
    }

    @Override
    public Result visit(AlloyQuantificationExpr quantificationExpr) {

        List<AlloyDecl> newDecls = new ArrayList<AlloyDecl>();
        for (AlloyDecl ds : quantificationExpr.decls) {
            for (AlloyDecl d : ds.expand()) {
                Result dResult = this.visit(d.expr);
                if (!(dResult.arity.equals(UNKNOWN_ARITY) || dResult.arity.equals(ONE_ARITY))) {
                    // higher order warning
                    Reporter.INSTANCE.addWarning(
                            new WarningUser(
                                    d.pos,
                                    ONE_ARITY.toString()
                                            + "Declaration is of arity "
                                            + dResult.arity.toString()
                                            + ", which is greater than 1: "
                                            + d.toString()));
                }
                newDecls.add(((AlloyDecl) d.rebuild(dResult.exp)));
                localPush(d.getName(), dResult.arity);
            }
        }

        Result bodyResult = this.visit(quantificationExpr.body);
        if (!bodyResult.arity.equals(UNKNOWN_ARITY) && !bodyResult.arity.equals(ONE_ARITY))
            throw AlloyModelError.mustBeFormula(
                    quantificationExpr.pos, quantificationExpr.body.toString());

        // take them off the stack
        for (AlloyDecl ds : quantificationExpr.decls) {
            for (AlloyDecl d : ds.expand()) {
                localPop();
            }
        }
        return new Result(
                ONE_ARITY,
                new AlloyQuantificationExpr(
                        quantificationExpr.pos,
                        quantificationExpr.quant,
                        newDecls,
                        bodyResult.exp));
    }

    @Override
    public Result visit(AlloyParenExpr parenExpr) {
        Result subResult = visit(parenExpr.sub);
        return new Result(subResult.arity, new AlloyParenExpr(parenExpr.pos, subResult.exp));
    }

    // expr.unary ----------------------------------

    @Override
    public Result visit(AlloyUnaryExpr unaryExpr) {

        Set<Class> argArityBoolean =
                Set.of(
                        AlloyAfterExpr.class,
                        AlloyAlwaysExpr.class,
                        AlloyBeforeExpr.class,
                        AlloyEventuallyExpr.class,
                        AlloyHistoricallyExpr.class,
                        AlloyNegExpr.class,
                        AlloyOnceExpr.class);

        Set<Class> argArityOne = Set.of(AlloyNumIntExpr.class);

        Set<Class> argArityTwo =
                Set.of(
                        AlloyReflTransClosExpr.class,
                        AlloyTransClosExpr.class,
                        AlloyTransExpr.class);

        // otherwise we don't care about the arg arity

        Set<Class> returnBooleanArity =
                Set.of(
                        AlloyAfterExpr.class,
                        AlloyAlwaysExpr.class,
                        AlloyBeforeExpr.class,
                        AlloyEventuallyExpr.class,
                        AlloyHistoricallyExpr.class,
                        AlloyNegExpr.class,
                        AlloyOnceExpr.class,
                        AlloyQtExpr.class); // all except Seq, which is handled specially below

        Set<Class> returnArityOne =
                Set.of(AlloyCardExpr.class, AlloyNumIntExpr.class, AlloyNumSumExpr.class);

        Set<Class> returnArityTwo =
                Set.of(
                        AlloyReflTransClosExpr.class,
                        AlloyTransClosExpr.class,
                        AlloyTransExpr.class);

        Set<Class> returnArityOfArg = Set.of(AlloyPrimeExpr.class);

        Result subResult = visit(unaryExpr.sub);

        Optional<Integer> subArity = subResult.arity;
        AlloyExpr subExp = subResult.exp;

        // seq is weird
        if (isSeq(unaryExpr)) {
            if (!subArity.equals(UNKNOWN_ARITY)) {
                return new Result(Optional.of(subArity.get() + 1), unaryExpr.rebuild(subExp));
            } else {
                throw AlloyModelError.unknownArity(unaryExpr.pos, unaryExpr.toString());
            }
        }
        // possible arity mismatch errors
        if (argArityBoolean.contains(unaryExpr.getClass()) && !subArity.equals(ONE_ARITY)) {
            // System.out.println(subExp);
            // System.out.println(subArity);
            throw AlloyModelError.mustBeFormula(unaryExpr.sub.pos, subExp.toString());
        }
        if (argArityOne.contains(unaryExpr.getClass()) && !subArity.equals(ONE_ARITY))
            throw AlloyModelError.mustBeUnary(unaryExpr.sub.pos, subExp.toString());

        if (argArityTwo.contains(unaryExpr.getClass()) && !subArity.equals(TWO_ARITY))
            throw AlloyModelError.mustBeBinary(unaryExpr.sub.pos, subExp.toString());

        AlloyExpr newExpr = unaryExpr.rebuild(subExp);

        if (returnBooleanArity.contains(unaryExpr.getClass()))
            return new Result(ONE_ARITY, newExpr);
        else if (returnArityOne.contains(unaryExpr.getClass()))
            return new Result(ONE_ARITY, newExpr);
        else if (returnArityTwo.contains(unaryExpr.getClass()))
            return new Result(TWO_ARITY, newExpr);
        else if (returnArityOfArg.contains(unaryExpr.getClass()))
            return new Result(subArity, newExpr);
        else throw ImplementationError.missingCase("arity checker: " + unaryExpr.toString());
    }

    // expr.var ----------------------------

    @Override
    public Result visit(AlloyVarExpr varExpr) {
        if (varExpr instanceof AlloyQnameExpr) {
            // System.out.println(varExpr.toString());
            // see AMSigTable.symbolArity for table of builtin strings
            String varName = varExpr.getName();
            // throws an error if not found
            return new Result(arity(varName), varExpr);
        } else {
            // other AlloyVarExpr types
            // System.out.println(varExpr.toString());
            return switch (varExpr) {
                case AlloyIdenExpr ignored -> new Result(TWO_ARITY, varExpr);
                case AlloyFunNextExpr q -> new Result(TWO_ARITY, varExpr);
                case AlloySumExpr q -> new Result(UNKNOWN_ARITY, varExpr); // can be any arity
                // most vars/literals are arity 1
                default -> new Result(ONE_ARITY, varExpr);
            };
        }
    }
}
