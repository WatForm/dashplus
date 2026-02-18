/*
    This visitor over expressions takes an AlloyExpr
    (which include DashRefs but not DashParam)
    and figures out the full Dash reference to
    variable names that are Dash dynamic variables. It
    does this by:

    1) replacing a name with its FQN; a "name" used
    on input is allowed to be an unambiguous suffix
    of an FQN; this involves searching for possible
    matching names in a region.

    2) adding in all the parameter values or checking that there are an appropriate number of parameter values if they already exist

    3) replacing "thisState" with a var expression of single parameter

    It leaves a primed variable as DashRef(... isNext=true)

    This exprRefResolver is called for the:
    - stateTable resolving for inits and invariants
    - varTable for resolving type of dynamic variables (where it resolves only the name, not the parameters )
    - transTable to resolve all parts of transitions.

    Note that "Next" in this file means "primed".

    NADTODO: check flags !
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashFQN.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.DashStrings.DashRefKind;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ResolverVisDM extends InitializeDM implements AlloyExprVis<AlloyExpr> {

    // these bits of state are used throughout the
    // visit functions with the same values
    private DashStrings.DashRefKind kind;
    private boolean nextOk;
    private boolean nextOkInPrmExprs;
    private boolean thisOk;
    // of state Expr itself or is parent of Expr
    // needed for context of expr being resolved
    private String sfqn;

    // bound variables of quantifiers
    private Deque<List<String>> scope = new ArrayDeque<>();

    public ResolverVisDM() {
        super();
    }

    public ResolverVisDM(DashFile d) {
        super(d);
    }

    private void inputChecks(AlloyExpr expr, String sfqn) {
        assert (sfqn != "");
        assert (expr != null);
    }

    private boolean isBoundVar(String s) {
        return scope.stream().anyMatch(list -> list.contains(s));
    }

    // top-level calls

    protected AlloyExpr resolveVar(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference in an expression
        // means we are looking for variables
        inputChecks(expr, sfqn);
        this.sfqn = sfqn;
        this.kind = DashStrings.DashRefKind.VAR;
        this.nextOk = false;
        this.nextOkInPrmExprs = false;
        this.thisOk = true;
        return this.visit(expr);
    }

    protected AlloyExpr resolveVarNextsOkAnywhere(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference in an expression
        // means we are looking for variables
        inputChecks(expr, sfqn);
        this.sfqn = sfqn;
        this.kind = DashStrings.DashRefKind.VAR;
        this.nextOk = true;
        this.nextOkInPrmExprs = true;
        this.thisOk = true;
        return this.visit(expr);
    }

    protected DashRef resolveState(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference to a State
        // we know this returns a DashRef rather than a more general AlloyExpr

        inputChecks(expr, sfqn);
        this.sfqn = sfqn;
        this.kind = DashRefKind.STATE;
        this.nextOk = false;
        this.nextOkInPrmExprs = false;
        this.thisOk = true;
        // System.out.println(expr.getClass());
        // expr is a DashRef
        // visit is a function that expects an AlloyExpr
        // calling this with a DashRef is fine because Liskov substitution
        // However, this leads to a methodShouldNotBeCalled error
        // return (DashRef) visit(expr); - this is wrong
        return (DashRef) this.visit(expr); // this is right
    }

    protected DashRef resolveEvent(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference to an event
        // we know this returns a DashRef rather than a more general AlloyExpr
        inputChecks(expr, sfqn);
        this.sfqn = sfqn;
        this.kind = DashRefKind.EVENT;
        this.nextOk = false;
        this.nextOkInPrmExprs = false;
        this.thisOk = true;
        return (DashRef) visit(expr);
    }

    protected DashRef resolveEventNextsOkInPrmExprs(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference to an event
        // we know this returns a DashRef rather than a more general AlloyExpr
        inputChecks(expr, sfqn);
        this.sfqn = sfqn;
        this.kind = DashRefKind.EVENT;
        this.nextOk = true;
        this.nextOkInPrmExprs = true;
        this.thisOk = true;
        return (DashRef) this.visit(expr);
    }

    // private functions

    private AlloyExpr resolve(AlloyVarExpr varExpr, List<? extends AlloyExpr> v_param_vals) {
        // NADTODO buffers
        // NADTODO check on predicates
        /*
            This purpose of this function is to take a varExpr (name) and possibly empty list of parameter values, and flesh out the reference to a FQN with all parameters values.

            Parameter values have to be determined from context, which is why sfqn is an argument to resolving.

            References to the parameter values of the current context (whether from a thisState or from lack of parameters) become AlloyQnameExpr (see DashParam.asAlloyVar()).
        */
        // v is the name,
        // v_params as the possibly empty set
        // of _resolved_ param expr
        // but it could still need to be added to

        assert (varExpr instanceof AlloyQnameExpr);

        String v = varExpr.label;

        // first find the possible matches in the table for this
        // name v (could be state/event/var/buffer)
        List<String> matches;
        if (v_param_vals.isEmpty()) {

            // if no param expr, then must be within region
            // that has same params

            // if already a sig in Alloy part of model
            // can't declare it again
            // replace this when jack writes a method in AlloyModel for this
            if (
            /* TODO: add this back? this.containsId(v) || */ this.isBoundVar(v)) {
                return varExpr;
            }
            // otherwise, look for matches in Dash model var decls
            matches = findMatchesInRegion(v);
        } else {
            // if it has params expr, params could be suffix of any var params
            // and later we check it has the right number of params
            matches = findMatches(v);
        }

        if (matches.isEmpty()) {
            if (!v_param_vals.isEmpty()) unknownElementWithParamsError(varExpr);
            else return varExpr; /* TODO: add this back? unknownError(varExpr);*/
            return null;
        }

        String m = chooseMatch(matches);
        if (m == null) {
            ambiguousRefError(varExpr);
            return null;
        }
        // something defined as a predicate with the Dash module
        // TODO: check on this logic
        if (containsPred(m)) {
            // best match is a predicate name
            // has to be treated a little differently
            // because does not have params and have to put its exp
            // directly in place unlike a DashRef
            // resolve the predicate value in place and add it in place
            DashStrings.DashRefKind kindTemp = kind;
            this.kind = DashRefKind.VAR;
            AlloyExpr ret = visit(predExp(m));
            this.kind = kindTemp;
            return ret;
        }

        // now m is one match from var/state/event table
        List<DashParam> m_params;
        if (kind == DashRefKind.STATE) m_params = stateParams(m);
        else if (kind == DashRefKind.EVENT) m_params = eventParams(m);
        else m_params = varParams(m);

        // parameters from enclosing state of this element
        List<? extends AlloyExpr> sfqn_param_vals = mapBy(stateParams(sfqn), x -> x.asAlloyVar());
        // these must be values p_Statename
        List<? extends AlloyExpr> m_param_vals = mapBy(m_params, x -> x.asAlloyVar());
        List<? extends AlloyExpr> final_param_vals = new ArrayList<AlloyExpr>();

        // v_param_vals is the parameter expressions (as Expr) provided
        // sfqn_param_vals is the parameter expressions (as Vars) for everything within this sfqn
        // m_param_vals is the parameter expressions (as Vars) for one possible name match, m
        // final_param_vals is the final set of parameter expressions for this var

        if (v_param_vals.isEmpty()) {
            // did not have any parameter values in use
            // must have same param values as sfqn b/c in same region
            if (m_param_vals.size() > sfqn_param_vals.size()) {
                // thing found by getRegion as match does
                // not have the same parameter values
                wrongNumberParamsError(varExpr);
                return null;
            } else {
                // this element might only use a subset of the
                // parameters of sfqn b/c it actually is from somewhere else
                // could be a subset of param values
                final_param_vals = sfqn_param_vals.subList(0, m_param_vals.size());
            }
        } else if (m_param_vals.size() != v_param_vals.size()) {
            // came with parameters so must be right number
            // TODO could paramValues b less than mParams????
            // and paramValues be a suffix of mParams???
            // since the fqn name can be a suffix
            wrongNumberParamsError(varExpr);
            return null;
        } else {
            // it was used with the right number of parameters
            final_param_vals = v_param_vals;
        }

        if (kind == DashStrings.DashRefKind.STATE)
            return new StateDashRef(varExpr.pos, m, final_param_vals);
        else if (kind == DashStrings.DashRefKind.EVENT)
            return new EventDashRef(varExpr.pos, m, final_param_vals);
        else return new VarDashRef(varExpr.pos, m, final_param_vals);
    }

    private List<String> findMatches(String name) {
        // set up names that we are searching for
        // from appropriate 'type' of element
        // for this function, match could be anywhere in Dash model
        List<String> region = new ArrayList<String>();
        if (kind == DashStrings.DashRefKind.STATE) region.addAll(this.allStateNames());
        else if (kind == DashStrings.DashRefKind.EVENT) region.addAll(allEventNames());
        else if (kind == DashStrings.DashRefKind.VAR) {
            region.addAll(allVarNames());
            region.addAll(allBufferNames());
            region.addAll(allPredNames());
        }
        return compareNames(name, region);
    }

    private List<String> findMatchesInRegion(String name) {
        // set up names that we are searching for
        // from appropriate 'type' of element
        // for this function, match can only be within enclosing sfqn
        List<String> region = new ArrayList<String>();
        if (kind == DashStrings.DashRefKind.STATE) region = region(sfqn);
        else if (kind == DashStrings.DashRefKind.EVENT) {
            // get all the events within these regions
            for (String x : region(sfqn)) {
                region.addAll(eventsWithinState(x));
            }
        } else if (kind == DashStrings.DashRefKind.VAR) {
            for (String x : region(sfqn)) {
                region.addAll(varsOfState(x));
                region.addAll(buffersOfState(x));
            }
            region.addAll(allPredNames());
        }
        return compareNames(name, region);
    }

    private List<String> compareNames(String name, List<String> region) {
        // region is a list of possible matches
        // 'name' is the name of the element to search for
        // it could be a suffix of the fqn in the region list
        // may return more than one possible match
        List<String> matches = new ArrayList<String>();
        for (String x : region)
            // FQN suffix e.g., A/B/C matches B/C
            if (DashFQN.suffix(x, name)) matches.add(x);
        return matches;
    }

    private String chooseMatch(List<String> matches) {
        // get highest rank match based on sfqn
        // if two have same rank, then ambiguous
        int longestCommonPrefix = 0;
        String bestmatch = "";
        Boolean multipleBestMatches = false;
        for (String s : matches) {
            if (DashFQN.commonPrefixLength(sfqn, s) > longestCommonPrefix) {
                longestCommonPrefix = DashFQN.commonPrefixLength(sfqn, s);
                bestmatch = s;
                multipleBestMatches = false;
            } else if (DashFQN.commonPrefixLength(sfqn, s) == longestCommonPrefix) {
                multipleBestMatches = true;
            }
        }
        if (!multipleBestMatches && longestCommonPrefix > 0) {
            return bestmatch;
        } else {
            return null;
        }
    }

    // visitor instance

    @Override
    public AlloyExpr visit(AlloyBinaryExpr binExpr) {
        // can't use a withLeft, withRight here
        // because this is a parent class
        if (!this.supportedBinaryExpr(binExpr))
            DashModelErrors.unsupportedExpr(
                    binExpr.pos, binExpr.getClass().getSimpleName(), binExpr.toString());
        return binExpr.rebuild(this.visit(binExpr.left), this.visit(binExpr.right));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
        if (unaryExpr.op == AlloyStrings.PRIME) {
            // can only apply a prime to a var
            // this should be not allowed in parsing
            assert (unaryExpr.sub instanceof AlloyQnameExpr || unaryExpr.sub instanceof VarDashRef);
            if (!this.nextOk) {
                noNextVarsError(unaryExpr);
            }
            AlloyExpr newExpr = this.visit(unaryExpr.sub);

            // if it is primed, the returned value
            // must be an internal VarDashRef (no other kind of
            // value can be primed)
            if (!(newExpr instanceof DashRef)) cantNextNonDynamicVarError(unaryExpr);
            else if (isEnvVar(((DashRef) newExpr).name)) cantNextEnvVarError(newExpr);
            // return DashRef(..., isNext)
            return ((VarDashRef) newExpr).makeNext();
        }
        if (!this.supportedUnaryExpr(unaryExpr))
            // throw exception
            DashModelErrors.unsupportedExpr(
                    unaryExpr.pos, unaryExpr.getClass().getSimpleName(), unaryExpr.toString());

        // otherwise we visit the sub expression
        // can't use a withSub here
        // because this is a parent class
        AlloyExpr newExpr = this.visit(unaryExpr.sub);
        return unaryExpr.rebuild(newExpr);
    }

    @Override
    public AlloyExpr visit(AlloyVarExpr varExpr) {

        if (!(varExpr instanceof AlloyQnameExpr)) {
            if (this.supportedBuiltinVarExpr(varExpr)) return varExpr;
            if (!(varExpr instanceof AlloyQnameExpr))
                // throws exception
                DashModelErrors.unsupportedExpr(
                        varExpr.pos, varExpr.getClass().getSimpleName(), varExpr.toString());
        }

        // var that came with no param values
        String v = varExpr.label;

        if (thisOk && v.startsWith(AlloyStrings.THIS)) {
            // thisSname gets replaced with var of DashParam (sfqn, param)
            String thisstate = v.substring(AlloyStrings.THIS.length(), v.length());

            // have to change kind to search for to STATE
            DashStrings.DashRefKind kindTemp = kind;
            this.kind = DashStrings.DashRefKind.STATE;
            List<String> matches = findMatchesInRegion(thisstate);
            this.kind = kindTemp;

            String firstMatch = matches.get(0);
            if (matches.size() == 1 && stateHasParams(firstMatch)) {
                // parameters of a state are DashParam's
                // parameters used within an expression are Expr
                List<DashParam> ps = stateParams(firstMatch);
                if (ps.size() == 1)
                    // any "thisState" use must be for a state with only one param
                    // this is already an Expr
                    return ps.get(0).asAlloyVar();
                else ambiguousUseOfThisError(varExpr);
            } else if (matches.size() == 1 && !stateHasParams(firstMatch))
                nonParamUseOfThisError(varExpr);
            else if (matches.size() > 1) ambiguousUseOfThisError(varExpr);
        }
        /* else we carry on with it as a regular var name with no params yet */
        // v_params is empty
        List<? extends AlloyExpr> v_params = new ArrayList<AlloyExpr>();
        return resolve(varExpr, v_params);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyBlock block) {
        return new AlloyBlock(block.pos, mapBy(block.exprs, i -> this.visit(i)));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyBracketExpr bracketExpr) {
        AlloyExpr y = visit(bracketExpr.expr);
        List<AlloyExpr> x = mapBy(bracketExpr.exprs, i -> this.visit(i));
        return new AlloyBracketExpr(bracketExpr.pos, y, x);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyCphExpr comprehensionExpr) {
        List<AlloyDecl> decls = mapBy(comprehensionExpr.decls, i -> (AlloyDecl) this.visit(i));
        AlloyExpr body = comprehensionExpr.body.map(value -> this.visit(value)).orElse(null);

        return new AlloyCphExpr(comprehensionExpr.pos, decls, body);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyIteExpr iteExpr) {
        return new AlloyIteExpr(
                iteExpr.pos,
                this.visit(iteExpr.cond),
                this.visit(iteExpr.conseq),
                this.visit(iteExpr.alt));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyLetExpr letExpr) {
        // NADTODO: rule out var names that are bound
        List<AlloyLetExpr.AlloyLetAsn> asns = letExpr.asns;
        List<AlloyLetExpr.AlloyLetAsn> newAsns =
                mapBy(asns, i -> new AlloyLetExpr.AlloyLetAsn(i.pos, i.qname, this.visit(i.expr)));
        return new AlloyLetExpr(letExpr.pos, newAsns, this.visit(letExpr.body));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyQuantificationExpr quantificationExpr) {
        // rule out var names that are bound
        List<String> boundVars = new ArrayList<>();
        for (AlloyDecl d : quantificationExpr.decls)
            for (AlloyQnameExpr q : d.qnames) boundVars.add(q.label);
        scope.push(boundVars);
        AlloyQuantificationExpr x =
                new AlloyQuantificationExpr(
                        quantificationExpr.pos,
                        quantificationExpr.quant,
                        mapBy(quantificationExpr.decls, i -> (AlloyDecl) this.visit(i)),
                        this.visit(quantificationExpr.body));
        scope.pop();
        return x;
    }

    @Override
    public AlloyExpr visit(AlloyDecl decl) {
        return decl.withExpr(this.visit(decl.expr));
    }

    public AlloyExpr visit(AlloyParenExpr parenExpr) {
        return new AlloyParenExpr(parenExpr.pos, this.visit(parenExpr.sub));
    }
    ;

    @Override
    public AlloyExpr visit(DashRef dashRef) {
        // can exist in parsing from x[a,b] or x[a,b]/v
        // name might not be fully resolved
        DashStrings.DashRefKind tempKind = kind;
        List<AlloyExpr> resolvedParamValues = new ArrayList<AlloyExpr>();
        for (AlloyExpr p : dashRef.paramValues) {
            // anything in a param value is a variable
            this.kind = DashStrings.DashRefKind.VAR;
            resolvedParamValues.add(this.visit(p));
            this.kind = tempKind;
        }
        this.kind = dashRef.kind();
        AlloyExpr newExpr =
                resolve(new AlloyQnameExpr(dashRef.pos, dashRef.name), resolvedParamValues);
        assert (newExpr instanceof DashRef);
        this.kind = tempKind;
        return newExpr;
    }
    ;

    @Override
    public AlloyExpr visit(DashParam dashParam) {
        throw ImplementationError.methodShouldNotBeCalled(
                "there should not be any DashParams in the parsed input");
    }

    // errors methods cannot be grouped in a subclass
    // or be static because they reference attributes
    // of the class

    public void unknownError(AlloyExpr expr) {
        if (kind == DashStrings.DashRefKind.STATE)
            throw new Reporter.ErrorUser(expr.pos, "state does not exist: " + expr.toString());
        else if (kind == DashStrings.DashRefKind.EVENT)
            throw new Reporter.ErrorUser(expr.pos, "event does not exist: " + expr.toString());
        else throw new Reporter.ErrorUser(expr.pos, "variable does not exist: " + expr.toString());
    }

    public void wrongNumberParamsError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, "Incorrect number of parameters: " + expr.toString());
    }

    public void ambiguousRefError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(expr.pos, " Name not unique: " + expr.toString());
    }

    public void unknownElementWithParamsError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, " " + "Unknown Dash element with params: " + expr.toString());
    }

    public void cantNextNonVarError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, " " + " Non-var/buffer cannot be primed: " + expr.toString());
    }

    public void unknownSrcDestError(String x, String t, String tfqn) {
        throw new Reporter.ErrorUser(
                "Src/Dest of trans is unknown: " + "trans " + tfqn + " " + t + " " + x);
    }

    public void cantNextEnvVarError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, " Env var/buffer cannot be primed: " + expr.toString());
    }

    public void noNextVarsError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, "Primed variables are not allowed in: " + expr.toString());
    }

    public void cantNextNonDynamicVarError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos,
                "Cannot prime something that is not a dynamic variable: " + expr.toString());
    }

    public void ambiguousUseOfThisError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(expr.pos, "Ambiguous use of 'this' " + expr.toString());
    }

    public void nonParamUseOfThisError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, " 'this' must refer to a parametrized state: " + expr.toString());
    }
}
