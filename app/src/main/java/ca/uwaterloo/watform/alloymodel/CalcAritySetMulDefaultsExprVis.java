/*

Calculate arities and
set default multiplicities in any expr
* every visit function returns a Result that contains both an arity and an expr with mul defaults set within it; it may include a list of arities for arguments to a pred/fun
* if there are arities errors or we can't calc arity enough to figure out mul defaults, an AlloyModelError exception is thrown

1) Calculate arities
* a certain about of UNKNOWN is tolerated, but in the future we may want to remove this
so errors are only thrown if we NEED to know that arity to set mul defaults.
* Booleans are treated as arity 1 (no typechecking is done) so that predicates can be processed as both p[a,b,c] and c.b.a.p

2) Mul Defaults:
* if an arrow doesn't have a mul at an end, it becomes SET
* in a decl, if expr is unary and no mul, mul becomes ONE
* in a decl, if expr is not unary and no mul, mul becomes SET and issues a Warning


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
import java.util.function.Function;

public class CalcAritySetMulDefaultsExprVis
        implements AlloyExprVis<CalcAritySetMulDefaultsExprVis.Result> {

    // provided function on initialization
    // gets the arity for an individual name
    private BiFunction<String, Optional<String>, Optional<Integer>> sigFieldArity;
    private Function<String, Optional<Integer>> predFunReturnArity;
    private Function<String, List<Optional<Integer>>> predFunArgsArities;
    private Optional<String> sigParent = Optional.empty();

    // constructor
    public CalcAritySetMulDefaultsExprVis(
            BiFunction<String, Optional<String>, Optional<Integer>> sigFieldArity,
            Function<String, Optional<Integer>> predFunReturnArity,
            Function<String, List<Optional<Integer>>> predFunArgsArities) {

        // set up empty stack for localArities
        this.localArities = new ArrayDeque<>();
        // these functions may change their answers
        // as field arities are determined
        // during resolve, predicate paragraphs are resolved etc.
        this.sigFieldArity = sigFieldArity;
        this.predFunReturnArity = predFunReturnArity;
        this.predFunArgsArities = predFunArgsArities;
    }

    // one kind of top-level call
    // used when walking over Tables and we want to store arities
    // because they will be used in the future
    public Result fieldArityAndSetMul(AlloyExpr e, Optional<String> sigParent) {
        this.sigParent = sigParent;
        return this.visit(e);
    }

    // another top-level call
    // used for walking over paragraphs where we don't need to know
    // the resulting multiplicities
    public Result setMul(AlloyExpr e) {
        this.sigParent = Optional.empty();
        return this.visit(e);
    }

    // ---------------------------------------------------------
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

    // the arity of a symbol is a combination of anything in the local context
    // and the passed in sigFieldArity function
    private Optional<Integer> symbolArity(String n) {
        Optional<Integer> x = localLookup(n);
        return x.isPresent() ? x : this.sigFieldArity.apply(n, this.sigParent);
    }

    // the following are used outside this arity checker for fun/pred paragraphs
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

    // ----------------------------------------------------

    // visit() returns a Result

    class Result {
        final Optional<Integer> arity;
        final AlloyExpr exp;
        // these are the arities of expected arguments to a pred/fun
        final List<Optional<Integer>> argArities;

        Result(Optional<Integer> arity, AlloyExpr exp) {
            this.arity = arity;
            this.exp = exp;
            this.argArities = emptyList();
        }

        Result(AlloyExpr exp) {
            this.arity = UNKNOWN_ARITY;
            this.exp = exp;
            this.argArities = emptyList();
        }

        Result(List<Optional<Integer>> argArities, Optional<Integer> arity, AlloyExpr exp) {
            this.argArities = argArities;
            this.arity = arity;
            this.exp = exp;
        }
    }

    // helper functions for conditions that should throw errors in arity checking
    private void notUnknown(Result r) {
        if (r.arity.equals(UNKNOWN_ARITY)) {
            throw AlloyModelError.unknownArity(r.exp.pos, r.exp.toString());
        }
    }

    private void noArgArities(Result leftResult, Result rightResult) {
        if (!leftResult.argArities.isEmpty() || (!rightResult.argArities.isEmpty()))
            throw AlloyModelError.mustBeDotOrBoxJoin(leftResult.exp.pos, leftResult.exp.toString());
    }

    private void noArgArities(Result result) {
        if (!result.argArities.isEmpty())
            throw AlloyModelError.mustBeDotOrBoxJoin(result.exp.pos, result.exp.toString());
    }

    private void equalArities(Result leftResult, Result rightResult) {
        if (!leftResult.arity.equals(rightResult.arity)
                && !leftResult.arity.equals(UNKNOWN_ARITY)
                && !rightResult.arity.equals(UNKNOWN_ARITY)) {
            // arity mismatch error
            throw AlloyModelError.arityMismatch(
                    leftResult.exp.pos,
                    leftResult.exp.toString()
                            + "("
                            + Integer.toString(leftResult.arity.get())
                            + ")",
                    rightResult.exp.toString()
                            + "("
                            + Integer.toString(rightResult.arity.get())
                            + ")");
        }
    }

    // --------------------------------------------------------
    // an instance of AlloyExprVis follows

    @Override
    public Result visit(DashRef dashRef) {
        // TODO: when we resolve expressions in Dash
        throw ImplementationError.notSupported(
                "dashref inside ArityVis yet: " + dashRef.toString());
    }

    // expr.binary ----------------------------------------

    // helper functions for binary expressions
    Result binaryBooleanInOut(AlloyBinaryExpr binExpr) {
        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);

        // throws an error
        noArgArities(leftResult, rightResult);
        // must have Boolean arity args
        if (!leftResult.arity.equals(ONE_ARITY) && !leftResult.arity.equals(UNKNOWN_ARITY)) {
            throw AlloyModelError.mustBeFormula(binExpr.left.pos, binExpr.left.toString());
        } else if (!rightResult.arity.equals(ONE_ARITY)
                && !rightResult.arity.equals(UNKNOWN_ARITY)) {
            throw AlloyModelError.mustBeFormula(binExpr.right.pos, binExpr.right.toString());
        }

        AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
        return new Result(ONE_ARITY, resultExpr);
    }

    Result binaryEqualArityArgsBooleanOut(AlloyBinaryExpr binExpr) {
        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);

        // throws an error
        noArgArities(leftResult, rightResult);
        equalArities(leftResult, rightResult);
        AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
        return new Result(ONE_ARITY, resultExpr);
    }

    Result binaryEqualArityArgsSameOut(AlloyBinaryExpr binExpr) {
        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);

        // throws an error
        noArgArities(leftResult, rightResult);
        equalArities(leftResult, rightResult);
        AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
        return new Result(leftResult.arity, resultExpr);
    }

    Result binaryNumOp(AlloyBinaryExpr binExpr) {
        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);
        AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
        if (!leftResult.arity.equals(ONE_ARITY))
            throw AlloyModelError.mustBeUnary(binExpr.left.pos, binExpr.left.toString());
        else if (!rightResult.arity.equals(ONE_ARITY))
            throw AlloyModelError.mustBeUnary(binExpr.right.pos, binExpr.right.toString());
        else return new Result(ONE_ARITY, resultExpr);
    }

    // binary expression visitors

    @Override
    public Result visit(AlloyBinaryExpr binExpr) {
        System.out.println(binExpr.getClass().toString());
        throw AlloyModelImplError.shouldNotReach();
    }

    @Override
    public Result visit(AlloyAndExpr binExpr) {
        return binaryBooleanInOut(binExpr);
    }

    @Override
    public Result visit(AlloyArrowExpr binExpr) {
        // this is the only binExpr that needs default multiplicities added
        // if there is no mul provided, default is SET regardless of arity of args

        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);

        // throws an error
        noArgArities(leftResult, rightResult);

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
            return new Result(resultExp);
        } else if (rightResult.arity.equals(UNKNOWN_ARITY)) {
            return new Result(resultExp);
        } else {
            return new Result(
                    Optional.of(leftResult.arity.get() + rightResult.arity.get()), resultExp);
        }
    }

    @Override
    public Result visit(AlloyCmpExpr binExpr) {
        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);

        // throws an error
        noArgArities(leftResult, rightResult);
        AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
        if (binExpr.comp.equals(AlloyCmpExpr.Comp.IN)) {
            // subset
            equalArities(leftResult, rightResult);
            return new Result(ONE_ARITY, resultExpr); // Boolean
        } else {
            // must be numbers
            if (!leftResult.arity.equals(ONE_ARITY))
                throw AlloyModelError.mustBeUnary(binExpr.left.pos, binExpr.left.toString());
            else if (!rightResult.arity.equals(ONE_ARITY))
                throw AlloyModelError.mustBeUnary(binExpr.right.pos, binExpr.right.toString());
            else return new Result(ONE_ARITY, resultExpr); // Boolean
        }
    }

    @Override
    public Result visit(AlloyDiffExpr binExpr) {
        // R - S
        return binaryEqualArityArgsSameOut(binExpr);
    }

    @Override
    public Result visit(AlloyDomRestrExpr binExpr) {
        // s <: r
        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);
        // throws an error
        noArgArities(leftResult, rightResult);
        if (!leftResult.arity.equals(ONE_ARITY)) {
            throw AlloyModelError.mustBeUnary(binExpr.left.pos, binExpr.left.toString());
        }
        Optional<Integer> returnArity = Optional.of(rightResult.arity.get());
        AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
        return new Result(returnArity, resultExpr);
    }

    @Override
    public Result visit(AlloyDotExpr binExpr) {
        // a.b
        // b could be a pred/fun call means b[a]
        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);
        AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
        if (!leftResult.argArities.isEmpty())
            throw AlloyModelError.missingArgsToPredFunCall(
                    binExpr.left.pos, binExpr.left.toString());
        if (rightResult.argArities.isEmpty()) {
            // b is not a pred/fun call
            notUnknown(leftResult);
            notUnknown(rightResult);
            Optional<Integer> returnArity =
                    Optional.of(leftResult.arity.get() + rightResult.arity.get() - 2);
            return new Result(returnArity, resultExpr);
        } else {
            // a.b where b is a fun or pred so "a" is the arg to the fun/pred
            if (rightResult.argArities.get(0).isPresent()) {
                if (leftResult.arity.isPresent()) {
                    if (leftResult.arity.equals(rightResult.argArities.get(0))) {
                        // go up to look for another argument if needed
                        return new Result(
                                tail(rightResult.argArities), rightResult.arity, resultExpr);
                    } else {
                        throw AlloyModelError.arityMismatchPredFunCall(
                                binExpr.left.pos,
                                binExpr.right.toString(),
                                binExpr.left.toString(),
                                rightResult.argArities.get(0).get(),
                                leftResult.arity.get());
                    }
                } else {
                    throw AlloyModelError.unknownArity(binExpr.left.pos, binExpr.left.toString());
                }
            } else {
                // lack of arity for an arg
                // this should have been caught when resolving the PredFunTable
                throw AlloyModelImplError.shouldNotReach();
            }
        }
    }

    @Override
    public Result visit(AlloyEqualsExpr binExpr) {
        return binaryEqualArityArgsBooleanOut(binExpr);
    }

    @Override
    public Result visit(AlloyFunAddExpr binExpr) {
        // I think this is a + b
        return binaryNumOp(binExpr);
    }

    @Override
    public Result visit(AlloyFunDivExpr binExpr) {
        // I think this is a / b
        return binaryNumOp(binExpr);
    }

    @Override
    public Result visit(AlloyFunMulExpr binExpr) {
        // I think this is a * b
        return binaryNumOp(binExpr);
    }

    @Override
    public Result visit(AlloyFunRemExpr binExpr) {
        // I think this is a % b
        return binaryNumOp(binExpr);
    }

    @Override
    public Result visit(AlloyFunSubExpr binExpr) {
        // I think this is a - b
        return binaryNumOp(binExpr);
    }

    @Override
    public Result visit(AlloyIffExpr binExpr) {
        return binaryBooleanInOut(binExpr);
    }

    @Override
    public Result visit(AlloyImpliesExpr binExpr) {
        return binaryBooleanInOut(binExpr);
    }

    @Override
    public Result visit(AlloyIntersExpr binExpr) {
        return binaryEqualArityArgsSameOut(binExpr);
    }

    @Override
    public Result visit(AlloyNotEqualsExpr binExpr) {
        return binaryEqualArityArgsBooleanOut(binExpr);
    }

    @Override
    public Result visit(AlloyOrExpr binExpr) {
        return binaryBooleanInOut(binExpr);
    }

    @Override
    public Result visit(AlloyRelOvrdExpr binExpr) {
        return binaryEqualArityArgsSameOut(binExpr);
    }

    @Override
    public Result visit(AlloyReleasesExpr binExpr) {
        return binaryBooleanInOut(binExpr);
    }

    @Override
    public Result visit(AlloyShAExpr binExpr) {
        throw ImplementationError.notSupported(
                "AlloyShA inside ArityVis yet: " + binExpr.toString());
    }

    @Override
    public Result visit(AlloyShLExpr binExpr) {
        throw ImplementationError.notSupported(
                "AlloyShL inside ArityVis yet: " + binExpr.toString());
    }

    @Override
    public Result visit(AlloyShRExpr binExpr) {
        throw ImplementationError.notSupported(
                "AlloyShR inside ArityVis yet: " + binExpr.toString());
    }

    @Override
    public Result visit(AlloySinceExpr binExpr) {
        return binaryBooleanInOut(binExpr);
    }

    @Override
    public Result visit(AlloyRngRestrExpr binExpr) {
        // r :> s
        Result leftResult = this.visit(binExpr.left);
        Result rightResult = this.visit(binExpr.right);
        // throws an error
        noArgArities(leftResult, rightResult);
        if (!rightResult.arity.equals(ONE_ARITY)) {
            throw AlloyModelError.mustBeUnary(binExpr.left.pos, binExpr.left.toString());
        }
        Optional<Integer> returnArity = Optional.of(leftResult.arity.get());
        AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
        return new Result(returnArity, resultExpr);
    }

    @Override
    public Result visit(AlloyUnionExpr binExpr) {
        return binaryEqualArityArgsSameOut(binExpr);
    }

    @Override
    public Result visit(AlloyUntilExpr binExpr) {
        return binaryBooleanInOut(binExpr);
    }

    // Unary -----------------------------------------

    // helper functions
    Result unaryBooleanInOut(AlloyUnaryExpr unaryExpr) {
        Result subResult = this.visit(unaryExpr.sub);
        // throws an error
        noArgArities(subResult);
        // must have Boolean arity args
        if (!subResult.arity.equals(ONE_ARITY) && !subResult.arity.equals(UNKNOWN_ARITY)) {
            throw AlloyModelError.mustBeFormula(unaryExpr.sub.pos, unaryExpr.sub.toString());
        }
        return new Result(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
    }

    Result arityTwoInAndOut(AlloyUnaryExpr unaryExpr) {
        Result subResult = this.visit(unaryExpr.sub);
        // throws an error
        noArgArities(subResult);
        if (!subResult.arity.equals(TWO_ARITY))
            throw AlloyModelError.mustBeBinary(unaryExpr.sub.pos, unaryExpr.sub.toString());
        return new Result(TWO_ARITY, unaryExpr.rebuild(subResult.exp));
    }

    // unary visitors

    @Override
    public Result visit(AlloyUnaryExpr unaryExpr) {
        throw AlloyModelImplError.shouldNotReach();
    }

    @Override
    public Result visit(AlloyAfterExpr unaryExpr) {
        return unaryBooleanInOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyAlwaysExpr unaryExpr) {
        return unaryBooleanInOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyBeforeExpr unaryExpr) {
        return unaryBooleanInOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyEventuallyExpr unaryExpr) {
        return unaryBooleanInOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyHistoricallyExpr unaryExpr) {
        return unaryBooleanInOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyNegExpr unaryExpr) {
        return unaryBooleanInOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyCardExpr unaryExpr) {
        // #R
        Result subResult = this.visit(unaryExpr.sub);
        // throws an error
        noArgArities(subResult);
        return new Result(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
    }

    @Override
    public Result visit(AlloyNumIntExpr unaryExpr) {
        // int[1]: turns {1} into number 1
        // TODO: not certain about this one
        Result subResult = this.visit(unaryExpr.sub);
        // throws an error
        noArgArities(subResult);
        if (subResult.arity.equals(ONE_ARITY))
            return new Result(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
        else throw AlloyModelError.mustBeUnary(unaryExpr.sub.pos, unaryExpr.sub.toString());
    }

    @Override
    public Result visit(AlloyNumSumExpr unaryExpr) {
        // sum { x | }
        // TODO: not certain about this one
        Result subResult = this.visit(unaryExpr.sub);
        // throws an error
        noArgArities(subResult);
        if (subResult.arity.equals(ONE_ARITY))
            return new Result(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
        else throw AlloyModelError.mustBeUnary(unaryExpr.sub.pos, unaryExpr.sub.toString());
    }

    @Override
    public Result visit(AlloyOnceExpr unaryExpr) {
        return unaryBooleanInOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyPrimeExpr unaryExpr) {
        // R'
        Result subResult = this.visit(unaryExpr.sub);
        // throws an error
        noArgArities(subResult);
        return new Result(subResult.arity, unaryExpr.rebuild(subResult.exp));
    }

    @Override
    public Result visit(AlloyQtExpr unaryExpr) {
        // one X
        Result subResult = this.visit(unaryExpr.sub);
        // throws an error
        noArgArities(subResult);
        // seq is weird
        if (isSeq(unaryExpr)) {
            notUnknown(subResult);
            return new Result(
                    Optional.of(subResult.arity.get() + 1), unaryExpr.rebuild(subResult.exp));
        }
        return new Result(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
    }

    @Override
    public Result visit(AlloyReflTransClosExpr unaryExpr) {
        // *X
        return arityTwoInAndOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyTransClosExpr unaryExpr) {
        // ^X
        return arityTwoInAndOut(unaryExpr);
    }

    @Override
    public Result visit(AlloyTransExpr unaryExpr) {
        // ~X
        return arityTwoInAndOut(unaryExpr);
    }

    // expr.misc ------------------------

    @Override
    public Result visit(AlloyBlock block) {
        List<AlloyExpr> newExprs = emptyList();
        if (block.exprs.size() == 1) {
            // if it is a block of size 1 then it could be a singleton set expr
            Result oneR = this.visit(block.exprs.get(0));
            noArgArities(oneR);
            return new Result(oneR.arity, new AlloyBlock(block.pos, List.of(oneR.exp)));
        } else {
            for (AlloyExpr e : block.exprs) {
                Result r = this.visit(e);
                noArgArities(r);
                // let it pass if it is UNKNOWN_ARITY
                if (!r.arity.equals(UNKNOWN_ARITY) && !r.arity.equals(ONE_ARITY)) {
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
        AlloyExpr resultExpr =
                new AlloyBracketExpr(
                        bracketExpr.pos, exprResult.exp, mapBy(exprsResult, r -> r.exp));

        notUnknown(exprResult);
        if (exprResult.argArities.isEmpty()) {
            // exprResult is not a pred/fun call that needs args so
            // this is just a regular join
            // interpreted as c.(b.(a.p))
            Optional<Integer> rightArity = exprResult.arity;
            for (Result argR : exprsResult) {
                notUnknown(argR);
                noArgArities(argR);
                Optional<Integer> leftArity = argR.arity;
                if (leftArity.equals(UNKNOWN_ARITY)) {
                    throw AlloyModelError.unknownArity(argR.exp.pos, argR.exp.toString());
                } else {
                    rightArity = Optional.of(leftArity.get() + rightArity.get() - 2);
                }
            }
            return new Result(rightArity, resultExpr);
        } else if (exprsResult.size() > exprResult.argArities.size()) {
            // too many args for the pred/fun
            // can be smaller  c.b.p[a] is okay
            throw AlloyModelError.wrongNumberArgs(
                    bracketExpr.pos,
                    bracketExpr.toString(),
                    exprResult.argArities.size(),
                    exprsResult.size());
        } else {
            // p[a,b,c]
            // walk over expected argArities and exprsResults together
            Integer i = 0;
            for (Optional<Integer> argArity : exprResult.argArities) {
                noArgArities(exprsResult.get(i));
                notUnknown(exprsResult.get(i));
                // not possible for argArity to be UNKNOWN
                if (argArity.equals(exprsResult.get(i).arity)) {
                    i++;
                } else {
                    throw AlloyModelError.arityMismatchPredFunCall(
                            exprsResult.get(i).exp.pos,
                            bracketExpr.expr.toString(),
                            exprsResult.get(i).exp.toString(),
                            argArity.get(),
                            exprsResult.get(i).arity.get());
                }
            }
            if (i.equals(exprsResult.size())) {
                // got all the needed args
                return new Result(exprResult.arity, resultExpr);
            } else {
                // still waiting for some args
                return new Result(
                        lastn(exprResult.argArities, exprsResult.size() - i),
                        exprResult.arity,
                        resultExpr);
            }
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
        notUnknown(typeResult);
        // TODO: is x: <emptymul> "seq A" allowed?
        if (declExpr.mul.isEmpty()) {
            // setting default
            if (typeArity.equals(ONE_ARITY)) {
                // default: if arity of declExpr is ONE
                // then mul is ONE
                newDecl = declExpr.rebuild(AlloyQtEnum.ONE, newExpr);
            } else {
                // default: if arity of declExpr is ONE
                // then mul is SET
                newDecl = declExpr.rebuild(AlloyQtEnum.SET, newExpr);
            }
        } else {
            // mul has already been set
            newDecl = declExpr.rebuild(newExpr);
        }
        if (isSeqDecl(declExpr) || isSeq(newExpr)) {
            // seq X is really Int -> X
            typeArity = Optional.of(typeArity.get() + 1);
        }
        // TODO: checking with Jack is mul of SEQ is empty?
        if (!newDecl.mul.isEmpty()) {
            // TODO: not sure what the purpose of this is?
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

    // expr.var ----------------------------

    @Override
    public Result visit(AlloyVarExpr varExpr) {
        if (varExpr instanceof AlloyQnameExpr) {
            String varName = varExpr.getName();
            // throws an error if not found
            Optional<Integer> arity = this.symbolArity(varName);
            if (arity.isPresent()) {
                return new Result(arity, varExpr);
            } else {
                Optional<Integer> returnArity = this.predFunReturnArity.apply(varName);
                if (!returnArity.isPresent()) {
                    // conservative: we haven't found the symbol
                    // so we'll see how much works with UNKNOWN_ARITY
                    return new Result(UNKNOWN_ARITY, varExpr);
                } else {
                    List<Optional<Integer>> argsArities = this.predFunArgsArities.apply(varName);
                    return new Result(argsArities, returnArity, varExpr);
                }
            }
        } else {
            // other AlloyVarExpr types
            // System.out.println(varExpr.toString());
            return switch (varExpr) {
                case AlloyIdenExpr ignored -> new Result(TWO_ARITY, varExpr);
                case AlloyFunNextExpr q -> new Result(TWO_ARITY, varExpr);
                case AlloySumExpr q -> new Result(UNKNOWN_ARITY, varExpr); // can be any arity
                // most vars/literals are arity 1
                // TODO: fix this!
                default -> new Result(ONE_ARITY, varExpr);
            };
        }
    }
}
