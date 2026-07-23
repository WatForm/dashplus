/*
    This is the class where the resolve functions are defined.
    These functions are passed to parent class through super().

    There are four parts to this file:
    1) init
    2) resolve used by all SM classes (this is top-level call)
    3) definitions of resolve1 and resolve2 (which are passed as args in super() of resolve)
    4) visitor over expressions to calculate and set multiplicities

*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.alloymodel.ResolveInfo.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.Reporter.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.AssumptionError;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Reporter;
import java.util.*;

public class SMResolve extends SMCmds {

    // 1) init

    protected SMResolve() {}

    protected SMResolve(SMResolve other) {
        super(other);
        // no state to copy here
    }

    public void debug() {
        System.out.println(this.toString());
        debugSMSigs();
        debugSMFields();
        debugSMPredFuns();
        debugSMConstraints();
        debugSMCmds();
    }

    // 2) resolve -- this is top-level resolve in all classes

    public void resolve() {
        // order here matters
        this.resolveSMSigs(); // includes resolving sigs passed to imports
        this.resolveSMFields(this::resolve1);
        this.resolveSMPredFuns(this::resolve1, this::resolve2);
        this.resolveSMConstraints(this::resolve2);
        this.resolveSMCmds(this::resolve2);
    }

    // 3) definitions of resolve1 and resolve2 -----------------------------------

    // this state is only used for calculation of resolve1 and resolve2

    // parent signature for resolving field bounding expressions
    // only has a value when checking a bounding expression
    private Optional<Qname> sigParentOfField = Optional.empty();

    // private String nameSpace = AlloyStrings.THIS;

    private Boolean usePredFun = false;
    private String nameSpace;

    // depends only on sigs/fields (not pred/funs)
    // returns both an expr and an arity
    // used for bounding expressions and args and return types

    public ResolveInfo resolve1(AlloyExpr e, String nameSpace, Optional<String> sigParentOfField) {
        if (sigParentOfField.isPresent())
            this.sigParentOfField = Optional.of(nameSpaceQname(nameSpace, sigParentOfField.get()));
        else this.sigParentOfField = Optional.empty();
        this.nameSpace = nameSpace;
        this.usePredFun = false;
        return new ResolveVis().visit(e);
    }

    // depends on sigs/fields/pred/fun
    // most places this only needs to return an expression, but in resolving predfun body
    // it also needs to check the arity of the body against the arity of the return type
    // so it needs the arity returned
    public ResolveInfo resolve2(AlloyExpr e, String nameSpace, List<AlloyDecl> args) {
        this.sigParentOfField = Optional.empty();
        this.nameSpace = nameSpace;
        this.usePredFun = true;

        ResolveVis resolveVis = new ResolveVis();
        for (AlloyDecl arg : args) {
            for (AlloyDecl d : arg.expand()) {
                ResolveInfo dResult = resolveVis.visit(d.expr);
                resolveVis.localPush(nameSpaceQname(nameSpace, d.getName()), dResult.arity);
            }
        }
        return resolveVis.visit(e);
        // don't need to take local vars off the stack b/c visitor ends here
    }

    class ResolveVis implements AlloyExprVis<ResolveInfo> {
        /*

        4) Expression visitor -------------------------------------------

        Calculate arities and
        set default multiplicities in any expr
        * every visit function returns a Result that contains both an arity and an expr with mul defaults set within it; it may include a list of arities for arguments to a pred/fun
        * if there are arities errors or we can't calc arity enough to figure out mul defaults, an AlloyModelError exception is thrown

        a) Calculate arities
        * a certain about of UNKNOWN is tolerated, but in the future we may want to remove this
        so errors are only thrown if we NEED to know that arity to set mul defaults.
        * Booleans are treated as arity 1 (no typechecking is done) so that predicates can be processed as both p[a,b,c] and c.b.a.p

        b) Mul Defaults:
        * if an arrow doesn't have a mul at an end, it becomes SET
        * in a decl, if expr is unary and no mul, mul becomes ONE
        * in a decl, if expr is not unary and no mul, mul becomes SET and issues a Warning


        */
        ResolveVis() {
            this.localArities = new ArrayDeque<>();
        }

        // need a context of arities for let expressions, quantified variables, etc.
        // needs to be a stack to pop vars on and off
        private Deque<Map.Entry<Qname, Optional<Integer>>> localArities;

        // namespace for these is THIS_NAMESPACE
        private void localPush(Qname qname, Optional<Integer> value) {
            localArities.push(new AbstractMap.SimpleEntry<>(qname, value));
        }

        private void localPop() {
            localArities.pop();
        }

        private Optional<Integer> localLookup(Qname qname) {
            for (Map.Entry<Qname, Optional<Integer>> entry : localArities) {
                if (entry.getKey().equals(qname)) {
                    return entry.getValue(); // first match = most recent
                }
            }
            return Optional.empty();
        }

        // the following are used outside this arity checker for fun/pred paragraphs
        // for use by a predicate or function paragraphs in Alloy
        // it has local decls
        public void localEnvPush(List<AlloyDecl> decls) {
            for (AlloyDecl d : decls) {
                ResolveInfo dResult = this.visit(d.expr);
                localPush(nameSpaceQname(SMResolve.this.nameSpace, d.getName()), dResult.arity);
            }
        }

        public void localEnvPop(List<AlloyDecl> decls) {
            for (AlloyDecl d : decls) {
                localPop();
            }
        }

        // --------------

        // helper functions for conditions that should throw errors in arity checking
        private void notUnknown(ResolveInfo r) {
            if (r.arity.equals(UNKNOWN_ARITY)) {
                throw AlloyModelError.unknownArity(r.exp.pos, r.exp.toString());
            }
        }

        private void noArgArities(ResolveInfo leftResult, ResolveInfo rightResult) {
            if (!leftResult.argArities.isEmpty() || (!rightResult.argArities.isEmpty()))
                throw AlloyModelError.mustBeDotOrBoxJoin(
                        leftResult.exp.pos, leftResult.exp.toString());
        }

        private void noArgArities(ResolveInfo result) {
            if (!result.argArities.isEmpty())
                throw AlloyModelError.mustBeDotOrBoxJoin(result.exp.pos, result.exp.toString());
        }

        private void equalArities(ResolveInfo leftResult, ResolveInfo rightResult) {
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
        public ResolveInfo visit(DashRef dashRef) {
            // TODO: when we resolve expressions in Dash
            throw ImplementationError.notSupported(
                    "dashref inside ArityVis yet: " + dashRef.toString());
        }

        // expr.binary ----------------------------------------

        // helper functions for binary expressions
        ResolveInfo binaryBooleanInOut(AlloyBinaryExpr binExpr) {
            // KENG TODO: if this is a x <: f, we have to look to see if x resolves f
            // look for x is an AlloyQnameExpr and fieldQnameMatches(Qname[namespace x, name x, name
            // f])
            // to see if there is a possible match
            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);

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
            return new ResolveInfo(ONE_ARITY, resultExpr);
        }

        ResolveInfo binaryEqualArityArgsBooleanOut(AlloyBinaryExpr binExpr) {
            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);

            // throws an error
            noArgArities(leftResult, rightResult);
            equalArities(leftResult, rightResult);
            AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
            return new ResolveInfo(ONE_ARITY, resultExpr);
        }

        ResolveInfo binaryEqualArityArgsSameOut(AlloyBinaryExpr binExpr) {
            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);

            // throws an error
            noArgArities(leftResult, rightResult);
            equalArities(leftResult, rightResult);
            AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
            return new ResolveInfo(leftResult.arity, resultExpr);
        }

        ResolveInfo binaryNumOp(AlloyBinaryExpr binExpr) {
            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);
            AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
            if (!leftResult.arity.equals(ONE_ARITY))
                throw AlloyModelError.mustBeUnary(binExpr.left.pos, binExpr.left.toString());
            else if (!rightResult.arity.equals(ONE_ARITY))
                throw AlloyModelError.mustBeUnary(binExpr.right.pos, binExpr.right.toString());
            else return new ResolveInfo(ONE_ARITY, resultExpr);
        }

        // binary expression visitors

        @Override
        public ResolveInfo visit(AlloyBinaryExpr binExpr) {
            // System.out.println(binExpr.getClass().toString());
            throw AlloyModelImplError.shouldNotReach();
        }

        @Override
        public ResolveInfo visit(AlloyAndExpr binExpr) {
            return binaryBooleanInOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyArrowExpr binExpr) {
            // this is the only binExpr that needs default multiplicities added
            // if there is no mul provided, default is SET regardless of arity of args

            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);

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
                return new ResolveInfo(resultExp);
            } else if (rightResult.arity.equals(UNKNOWN_ARITY)) {
                return new ResolveInfo(resultExp);
            } else {
                return new ResolveInfo(
                        Optional.of(leftResult.arity.get() + rightResult.arity.get()), resultExp);
            }
        }

        @Override
        public ResolveInfo visit(AlloyCmpExpr binExpr) {
            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);

            // throws an error
            noArgArities(leftResult, rightResult);
            AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
            if (binExpr.comp.equals(AlloyCmpExpr.Comp.IN)) {
                // subset
                equalArities(leftResult, rightResult);
                return new ResolveInfo(ONE_ARITY, resultExpr); // Boolean
            } else {
                // must be numbers
                if (!leftResult.arity.equals(ONE_ARITY))
                    throw AlloyModelError.mustBeUnary(binExpr.left.pos, binExpr.left.toString());
                else if (!rightResult.arity.equals(ONE_ARITY))
                    throw AlloyModelError.mustBeUnary(binExpr.right.pos, binExpr.right.toString());
                else return new ResolveInfo(ONE_ARITY, resultExpr); // Boolean
            }
        }

        @Override
        public ResolveInfo visit(AlloyDiffExpr binExpr) {
            // R - S
            return binaryEqualArityArgsSameOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyDomRestrExpr binExpr) {
            // s <: r
            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);
            // throws an error
            noArgArities(leftResult, rightResult);
            if (!leftResult.arity.equals(ONE_ARITY)) {
                throw AlloyModelError.mustBeUnary(binExpr.left.pos, binExpr.left.toString());
            }
            Optional<Integer> returnArity = Optional.of(rightResult.arity.get());
            AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
            return new ResolveInfo(returnArity, resultExpr);
        }

        @Override
        public ResolveInfo visit(AlloyDotExpr binExpr) {
            // a.b
            // b could be a pred/fun call means b[a]
            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);
            AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
            if (!leftResult.argArities.isEmpty())
                throw AlloyModelError.missingArgsToPredFunCall(
                        binExpr.left.pos, binExpr.left.toString());
            // KENG TODO: somewhere in here perhaps to check against return type of pred/fun if
            // it is a pred/fun call
            if (rightResult.argArities.isEmpty()) {
                // b is not a pred/fun call
                notUnknown(leftResult);
                notUnknown(rightResult);
                Optional<Integer> returnArity =
                        Optional.of(leftResult.arity.get() + rightResult.arity.get() - 2);
                return new ResolveInfo(returnArity, resultExpr);
            } else {
                // a.b where b is a fun or pred so "a" is the arg to the fun/pred
                if (rightResult.argArities.get(0).isPresent()) {
                    if (leftResult.arity.isPresent()) {
                        if (leftResult.arity.equals(rightResult.argArities.get(0))) {
                            // go up to look for another argument if needed
                            return new ResolveInfo(
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
                        throw AlloyModelError.unknownArity(
                                binExpr.left.pos, binExpr.left.toString());
                    }
                } else {
                    // lack of arity for an arg
                    // this should have been caught when resolving the PredFunTable
                    throw AlloyModelImplError.shouldNotReach();
                }
            }
        }

        @Override
        public ResolveInfo visit(AlloyEqualsExpr binExpr) {
            return binaryEqualArityArgsBooleanOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyFunAddExpr binExpr) {
            // I think this is a + b
            return binaryNumOp(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyFunDivExpr binExpr) {
            // I think this is a / b
            return binaryNumOp(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyFunMulExpr binExpr) {
            // I think this is a * b
            return binaryNumOp(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyFunRemExpr binExpr) {
            // I think this is a % b
            return binaryNumOp(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyFunSubExpr binExpr) {
            // I think this is a - b
            return binaryNumOp(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyIffExpr binExpr) {
            return binaryBooleanInOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyImpliesExpr binExpr) {
            return binaryBooleanInOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyIntersExpr binExpr) {
            return binaryEqualArityArgsSameOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyNotEqualsExpr binExpr) {
            return binaryEqualArityArgsBooleanOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyOrExpr binExpr) {
            return binaryBooleanInOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyRelOvrdExpr binExpr) {
            return binaryEqualArityArgsSameOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyReleasesExpr binExpr) {
            return binaryBooleanInOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyShAExpr binExpr) {
            throw ImplementationError.notSupported(
                    "AlloyShA inside ArityVis yet: " + binExpr.toString());
        }

        @Override
        public ResolveInfo visit(AlloyShLExpr binExpr) {
            throw ImplementationError.notSupported(
                    "AlloyShL inside ArityVis yet: " + binExpr.toString());
        }

        @Override
        public ResolveInfo visit(AlloyShRExpr binExpr) {
            throw ImplementationError.notSupported(
                    "AlloyShR inside ArityVis yet: " + binExpr.toString());
        }

        @Override
        public ResolveInfo visit(AlloySinceExpr binExpr) {
            return binaryBooleanInOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyRngRestrExpr binExpr) {
            // r :> s
            ResolveInfo leftResult = this.visit(binExpr.left);
            ResolveInfo rightResult = this.visit(binExpr.right);
            // throws an error
            noArgArities(leftResult, rightResult);
            if (!rightResult.arity.equals(ONE_ARITY)) {
                throw AlloyModelError.mustBeUnary(binExpr.left.pos, binExpr.left.toString());
            }
            Optional<Integer> returnArity = Optional.of(leftResult.arity.get());
            AlloyExpr resultExpr = binExpr.rebuild(leftResult.exp, rightResult.exp);
            return new ResolveInfo(returnArity, resultExpr);
        }

        @Override
        public ResolveInfo visit(AlloyUnionExpr binExpr) {
            return binaryEqualArityArgsSameOut(binExpr);
        }

        @Override
        public ResolveInfo visit(AlloyUntilExpr binExpr) {
            return binaryBooleanInOut(binExpr);
        }

        // Unary -----------------------------------------

        // helper functions
        ResolveInfo unaryBooleanInOut(AlloyUnaryExpr unaryExpr) {
            ResolveInfo subResult = this.visit(unaryExpr.sub);
            // throws an error
            noArgArities(subResult);
            // must have Boolean arity args
            if (!subResult.arity.equals(ONE_ARITY) && !subResult.arity.equals(UNKNOWN_ARITY)) {
                throw AlloyModelError.mustBeFormula(unaryExpr.sub.pos, unaryExpr.sub.toString());
            }
            return new ResolveInfo(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
        }

        ResolveInfo arityTwoInAndOut(AlloyUnaryExpr unaryExpr) {
            ResolveInfo subResult = this.visit(unaryExpr.sub);
            // throws an error
            noArgArities(subResult);
            if (!subResult.arity.equals(TWO_ARITY))
                throw AlloyModelError.mustBeBinary(unaryExpr.sub.pos, unaryExpr.sub.toString());
            return new ResolveInfo(TWO_ARITY, unaryExpr.rebuild(subResult.exp));
        }

        // unary visitors

        @Override
        public ResolveInfo visit(AlloyUnaryExpr unaryExpr) {
            throw AlloyModelImplError.shouldNotReach();
        }

        @Override
        public ResolveInfo visit(AlloyAfterExpr unaryExpr) {
            return unaryBooleanInOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyAlwaysExpr unaryExpr) {
            return unaryBooleanInOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyBeforeExpr unaryExpr) {
            return unaryBooleanInOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyEventuallyExpr unaryExpr) {
            return unaryBooleanInOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyHistoricallyExpr unaryExpr) {
            return unaryBooleanInOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyNegExpr unaryExpr) {
            return unaryBooleanInOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyCardExpr unaryExpr) {
            // #R
            ResolveInfo subResult = this.visit(unaryExpr.sub);
            // throws an error
            noArgArities(subResult);
            return new ResolveInfo(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
        }

        @Override
        public ResolveInfo visit(AlloyNumIntExpr unaryExpr) {
            // int[1]: turns {1} into number 1
            // TODO: not certain about this one
            ResolveInfo subResult = this.visit(unaryExpr.sub);
            // throws an error
            noArgArities(subResult);
            if (subResult.arity.equals(ONE_ARITY))
                return new ResolveInfo(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
            else throw AlloyModelError.mustBeUnary(unaryExpr.sub.pos, unaryExpr.sub.toString());
        }

        @Override
        public ResolveInfo visit(AlloyNumSumExpr unaryExpr) {
            // sum { x | }
            // TODO: not certain about this one
            ResolveInfo subResult = this.visit(unaryExpr.sub);
            // throws an error
            noArgArities(subResult);
            if (subResult.arity.equals(ONE_ARITY))
                return new ResolveInfo(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
            else throw AlloyModelError.mustBeUnary(unaryExpr.sub.pos, unaryExpr.sub.toString());
        }

        @Override
        public ResolveInfo visit(AlloyOnceExpr unaryExpr) {
            return unaryBooleanInOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyPrimeExpr unaryExpr) {
            // R'
            ResolveInfo subResult = this.visit(unaryExpr.sub);
            // throws an error
            noArgArities(subResult);
            return new ResolveInfo(subResult.arity, unaryExpr.rebuild(subResult.exp));
        }

        @Override
        public ResolveInfo visit(AlloyQtExpr unaryExpr) {
            // one X
            ResolveInfo subResult = this.visit(unaryExpr.sub);
            // throws an error
            noArgArities(subResult);
            // seq is weird
            if (isSeq(unaryExpr)) {
                notUnknown(subResult);
                return new ResolveInfo(
                        Optional.of(subResult.arity.get() + 1), unaryExpr.rebuild(subResult.exp));
            }
            return new ResolveInfo(ONE_ARITY, unaryExpr.rebuild(subResult.exp));
        }

        @Override
        public ResolveInfo visit(AlloyReflTransClosExpr unaryExpr) {
            // *X
            return arityTwoInAndOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyTransClosExpr unaryExpr) {
            // ^X
            return arityTwoInAndOut(unaryExpr);
        }

        @Override
        public ResolveInfo visit(AlloyTransExpr unaryExpr) {
            // ~X
            return arityTwoInAndOut(unaryExpr);
        }

        // expr.misc ------------------------

        @Override
        public ResolveInfo visit(AlloyBlock block) {
            List<AlloyExpr> newExprs = emptyList();
            if (block.exprs.size() == 1) {
                // if it is a block of size 1 then it could be a singleton set expr
                ResolveInfo oneR = this.visit(block.exprs.get(0));
                noArgArities(oneR);
                return new ResolveInfo(oneR.arity, new AlloyBlock(block.pos, List.of(oneR.exp)));
            } else {
                for (AlloyExpr e : block.exprs) {
                    ResolveInfo r = this.visit(e);
                    noArgArities(r);
                    // let it pass if it is UNKNOWN_ARITY
                    if (!r.arity.equals(UNKNOWN_ARITY) && !r.arity.equals(ONE_ARITY)) {
                        throw AlloyModelError.mustBeFormula(e.pos, e.toString());
                    }
                    newExprs.add(r.exp);
                }
                return new ResolveInfo(ONE_ARITY, new AlloyBlock(block.pos, newExprs));
            }
        }

        @Override
        public ResolveInfo visit(AlloyBracketExpr bracketExpr) {
            // p[a,b,c]
            ResolveInfo exprResult = visit(bracketExpr.expr);
            List<ResolveInfo> exprsResult = mapBy(bracketExpr.exprs, i -> this.visit(i));
            AlloyExpr resultExpr =
                    new AlloyBracketExpr(
                            bracketExpr.pos, exprResult.exp, mapBy(exprsResult, r -> r.exp));

            notUnknown(exprResult);
            if (exprResult.argArities.isEmpty()) {
                // exprResult is not a pred/fun call that needs args so
                // this is just a regular join
                // interpreted as c.(b.(a.p))
                Optional<Integer> rightArity = exprResult.arity;
                for (ResolveInfo argR : exprsResult) {
                    notUnknown(argR);
                    noArgArities(argR);
                    Optional<Integer> leftArity = argR.arity;
                    if (leftArity.equals(UNKNOWN_ARITY)) {
                        throw AlloyModelError.unknownArity(argR.exp.pos, argR.exp.toString());
                    } else {
                        rightArity = Optional.of(leftArity.get() + rightArity.get() - 2);
                    }
                }
                return new ResolveInfo(rightArity, resultExpr);
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
                    // KENG TODO: this might be the point to check against the expected returntype
                    // of
                    // the pred/fun
                    return new ResolveInfo(exprResult.arity, resultExpr);
                } else {
                    // still waiting for some args
                    return new ResolveInfo(
                            lastn(exprResult.argArities, exprsResult.size() - i),
                            exprResult.arity,
                            resultExpr);
                }
            }
        }

        @Override
        public ResolveInfo visit(AlloyCphExpr comprehensionExpr) {
            // {x:X, y: Y} always means {x:one X, y: one Y}
            // no other multiplicities are allowed in a set comprehension Expr
            // each decl must be unary
            Integer declsArity = 0;
            List<AlloyDecl> newDecls = new ArrayList<AlloyDecl>();
            // these decls can never be an empty list
            for (AlloyDecl d : comprehensionExpr.decls) {
                ResolveInfo dResult = this.visit(d.expr);
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
                localPush(nameSpaceQname(SMResolve.this.nameSpace, d.getName()), dResult.arity);
            }

            AlloyExpr bodyExpr;
            if (comprehensionExpr.body.isPresent()) {

                ResolveInfo bodyResult = this.visit(comprehensionExpr.body.get());
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
            return new ResolveInfo(
                    Optional.of(declsArity),
                    new AlloyCphExpr(comprehensionExpr.pos, newDecls, bodyExpr));
        }

        @Override
        public ResolveInfo visit(AlloyDecl declExpr) {
            ResolveInfo typeResult = this.visit(declExpr.expr);
            Optional<Integer> typeArity = typeResult.arity;
            AlloyExpr newExpr = typeResult.exp;
            AlloyDecl newDecl = null;
            notUnknown(typeResult);
            // x: <emptymul> "seq A" is allowed
            if (declExpr.mul.isEmpty() && !isSeq(newExpr)) {
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
                // mul has already been set or its newExpr is seq
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

            return new ResolveInfo(typeArity, newDecl);
        }

        @Override
        public ResolveInfo visit(AlloyIteExpr iteExpr) {

            ResolveInfo condResult = this.visit(iteExpr.cond);
            ResolveInfo conseqResult = this.visit(iteExpr.conseq);
            ResolveInfo altResult = this.visit(iteExpr.alt);
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
            return new ResolveInfo(
                    returnArity, iteExpr.rebuild(condResult.exp, conseqResult.exp, altResult.exp));
        }

        @Override
        public ResolveInfo visit(AlloyLetExpr letExpr) {

            List<AlloyLetExpr.AlloyLetAsn> newAsns = new ArrayList<AlloyLetExpr.AlloyLetAsn>();
            for (AlloyLetExpr.AlloyLetAsn l : letExpr.asns) {
                ResolveInfo lResult = this.visit(l.expr);
                newAsns.add(new AlloyLetExpr.AlloyLetAsn(l.pos, l.qname, lResult.exp));
                localPush(nameSpaceQname(SMResolve.this.nameSpace, l.getName()), lResult.arity);
            }
            ResolveInfo bodyResult = this.visit(letExpr.body);
            // take them off the stack
            for (AlloyLetExpr.AlloyLetAsn l : letExpr.asns) {
                localPop();
            }
            return new ResolveInfo(
                    bodyResult.arity, new AlloyLetExpr(letExpr.pos, newAsns, bodyResult.exp));
        }

        @Override
        public ResolveInfo visit(AlloyQuantificationExpr quantificationExpr) {

            List<AlloyDecl> newDecls = new ArrayList<AlloyDecl>();
            for (AlloyDecl ds : quantificationExpr.decls) {
                for (AlloyDecl d : ds.expand()) {
                    ResolveInfo dResult = this.visit(d.expr);
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
                    localPush(nameSpaceQname(SMResolve.this.nameSpace, d.getName()), dResult.arity);
                }
            }

            ResolveInfo bodyResult = this.visit(quantificationExpr.body);
            if (!bodyResult.arity.equals(UNKNOWN_ARITY) && !bodyResult.arity.equals(ONE_ARITY))
                throw AlloyModelError.mustBeFormula(
                        quantificationExpr.pos, quantificationExpr.body.toString());

            // take them off the stack
            for (AlloyDecl ds : quantificationExpr.decls) {
                for (AlloyDecl d : ds.expand()) {
                    localPop();
                }
            }
            return new ResolveInfo(
                    ONE_ARITY,
                    new AlloyQuantificationExpr(
                            quantificationExpr.pos,
                            quantificationExpr.quant,
                            newDecls,
                            bodyResult.exp));
        }

        @Override
        public ResolveInfo visit(AlloyParenExpr parenExpr) {
            ResolveInfo subResult = visit(parenExpr.sub);
            return new ResolveInfo(
                    subResult.arity, new AlloyParenExpr(parenExpr.pos, subResult.exp));
        }

        // expr.var ----------------------------

        @Override
        public ResolveInfo visit(AlloyVarExpr varExpr) {
            if (varExpr instanceof AlloyQnameExpr) {
                Qname chosen;
                // KENG: revisions here
                // System.out.println("looking up1: " + varExpr.toString());
                Optional<Integer> x = localLookup(thisQname(varExpr.getName()));
                if (x.isPresent()) return new ResolveInfo(x, varExpr);
                // this qname may have UNKNOWN_NAMESPACE in it and should only be used for lookups
                Qname qname = unknownQname(varExpr.getName());
                // KENG TODO: sigs don't have priority over fields in disambiguation so order of ite
                // needs fixing
                // System.out.println("looking up2: " + varExpr.toString());
                if (SMResolve.this.isSig(qname)) {
                    // System.out.println("looking up3: " + varExpr.toString());
                    // KENG NOTE: I'm picking one for now
                    chosen = SMResolve.this.sigQnameMatches(qname).get(0);
                    // KENG TODO may be multiple matches in different namespaces
                    // for now I'm just saying the arity is 1
                    return new ResolveInfo(
                            Optional.of(1), chosen.toAlloyExpr(varExpr.pos, Kind.SIG));

                } else if (SMResolve.this.isField(qname)) {
                    // KENG NOTE: I'm picking one for now
                    chosen = SMResolve.this.fieldQnameMatches(qname).get(0);

                    if (SMResolve.this.sigParentOfField.isPresent()) {
                        // we are checking a bounding expression of a field
                        // any field used must be within this sig
                        // it is allowed but the arity is not -1 the arity returned from the
                        // fieldTable
                        // the isFieldResolved makes us disallow use before declare of fields in the
                        // same sig
                        if (!SMResolve.this.isFieldResolved(chosen)) {
                            throw AlloyModelError.unknownName(varExpr.pos, varExpr.toString());
                        } else if (SMResolve.this.sigParentOfField.get().equals(chosen.sigParent)) {
                            throw AlloyModelError.cannotRefFieldInBoundingExprOutsideOfItsSig(
                                    varExpr.getName(),
                                    SMResolve.this.sigParentOfField.get().fullName());
                        } else {
                            // if symbol has same parent sig as the field we are checking
                            // implicitly the sigParent is already dot joined to the fieldName so we
                            // subtract 1
                            return new ResolveInfo(
                                    SMResolve.this.fieldArity(chosen).map(b -> b - 1),
                                    chosen.toAlloyExpr(varExpr.pos, Kind.FIELD));
                        }
                    } else {
                        // System.out.println("looking up5: " + varExpr.toString());
                        // sigParent is absent meaning we are not checking a bounding
                        // expression of a field
                        // KENG NOTE: I'm picking one for now
                        chosen = SMResolve.this.fieldQnameMatches(qname).get(0);
                        return new ResolveInfo(
                                SMResolve.this.fieldArity(chosen),
                                chosen.toAlloyExpr(varExpr.pos, Kind.FIELD));
                    }
                    /*
                    } else if (Builtins.isBuiltin(qname)) {
                        // TODO: this will change once we can read imports
                        // case should be removed because Alloy built-ins seem to be specific AlloyVarExpr
                        return new ResolveInfo(Optional.of(Builtins.builtinArity(qname)), varExpr);
                    */
                } else if (SMResolve.this.usePredFun && (SMResolve.this.isPredFun(qname))) {
                    // System.out.println("looking up6: " + varExpr.toString());
                    // KENG NOTE: I'm picking one for now
                    chosen = SMResolve.this.predFunQnameMatches(qname).get(0);
                    Optional<Integer> returnArity = SMResolve.this.predFunReturnArity(chosen);
                    if (returnArity.isPresent()) {
                        List<Optional<Integer>> argsArities =
                                SMResolve.this.predFunArgArities(chosen);
                        return new ResolveInfo(
                                argsArities,
                                returnArity,
                                chosen.toAlloyExpr(varExpr.pos, Kind.PREDFUN));
                    } else {
                        throw AlloyModelError.unknownName(varExpr.pos, varExpr.toString());
                    }
                } else {
                    // System.out.println("looking up7: " + varExpr.toString());
                    // System.out.println("usePredFun: " + usePredFun);
                    // System.out.println("isPredFun: " + SMResolve.this.isPredFun(qname));
                    throw AlloyModelError.unknownName(varExpr.pos, varExpr.toString());
                }
            } else {
                // other AlloyVarExpr types
                return switch (varExpr) {
                    case AlloyIdenExpr ignored -> new ResolveInfo(TWO_ARITY, varExpr);
                    case AlloyUnivExpr ignored -> new ResolveInfo(ONE_ARITY, varExpr);
                    case AlloyNoneExpr ignored -> new ResolveInfo(ONE_ARITY, varExpr);
                    case AlloyFunNextExpr q -> new ResolveInfo(TWO_ARITY, varExpr);
                    case AlloyAtNameExpr q ->
                            throw AssumptionError.atNotAllowed(varExpr.pos, varExpr.toString());
                    case AlloyThisExpr q ->
                            throw AssumptionError.thisNotAllowed(varExpr.pos, varExpr.toString());
                    case AlloyPredTotOrdExpr q ->
                            new ResolveInfo(
                                    List.of(ONE_ARITY, ONE_ARITY, TWO_ARITY), ONE_ARITY, varExpr);
                    // TODO: fix this! it does not cover enough cases
                    default -> {
                        System.out.println(varExpr.toString());
                        throw AlloyModelImplError.shouldNotReach();
                    }
                };
            }
        }
    }
}
