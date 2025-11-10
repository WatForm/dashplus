/*
    This visitor over expressions takes an AlloyExpr
    (which include DashParam's and DashRef's)
    and figures out the full Dash reference to
    variable names that are Dash dynamic variables. It
    does this by:

    1) replacing a name with its FQN; a "name" used
    on input is allowed to be an unambiguous suffix
    of an FQN; this involves searching for possible
    matching names in a region.

    2) adding in all the parameter values or checking that there are an appropriate number of parameter values if they already exist

    3) replacing "thisState" with a DashParam

    It leaves a primed variable as PRIME(DashRef ...).

    This exprRefResolver is called for the:
    - stateTable resolving for inits and invariants
    - varTable for resolving type of dynamic variables (where it resolves only the name, not the parameters )
    - transTable to resolve all parts of transitions.
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class ExprRefResolverVis implements DashExprVis<AlloyExpr> {

    private DashStrings.DashRefKind defaultKind = DashStrings.DashRefKind.VAR;

    private StateTable st;
    private TransTable tt;
    private EventTable et;
    private VarTable vt;
    private BufferTable bt;
    private DashPredTable pt;
    private DashStrings.DashRefKind kind = defaultKind;
    private boolean primeOk;
    private boolean primeOkInPrmExprs;
    private boolean thisOk;
    private String sfqn; // of state Expr itself or is parent of Expr

    public ExprRefResolverVis(
            StateTable st,
            TransTable tt,
            EventTable et,
            VarTable vt,
            BufferTable bt,
            DashPredTable pt) {
        st = st;
        et = et;
        vt = vt;
        bt = bt;
        pt = pt;
        // default values
        sfqn = "";
        kind = DashStrings.DashRefKind.VAR;
        primeOk = false;
        primeOkInPrmExprs = false;
        thisOk = true;
        // tables are not modified by may be modified
        // by calling functions in between calls to revolveExpr
    }

    private void inputChecks(AlloyExpr expr, String sfqn) {
        assert (sfqn != "");
        assert (expr != null);
    }

    // top-level calls

    public AlloyExpr resolveVar(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference in an expression
        // means we are looking for variables
        inputChecks(expr, sfqn);
        sfqn = sfqn;
        assert (kind == DashStrings.DashRefKind.VAR);
        return visit(expr);
    }

    public AlloyExpr resolveVarPrimesOkAnywhere(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference in an expression
        // means we are looking for variables
        inputChecks(expr, sfqn);
        assert (kind == DashStrings.DashRefKind.VAR);
        sfqn = sfqn;
        primeOk = true;
        primeOkInPrmExprs = true;
        AlloyExpr x = visit(expr);
        primeOk = false;
        primeOkInPrmExprs = false;
        return x;
    }

    public DashRef resolveState(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference to a State
        // we know this returns a DashRef rather than a more general AlloyExpr
        inputChecks(expr, sfqn);
        sfqn = sfqn;
        kind = kind.STATE;
        DashRef x = (DashRef) visit(expr);
        kind = defaultKind;
        return x;
    }

    public DashRef resolveEvent(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference to an event
        // we know this returns a DashRef rather than a more general AlloyExpr
        inputChecks(expr, sfqn);
        sfqn = sfqn;
        kind = kind.EVENT;
        DashRef x = (DashRef) visit(expr);
        kind = defaultKind;
        return x;
    }

    public DashRef resolveEventPrimesOkInPrmExprs(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference to an event
        // we know this returns a DashRef rather than a more general AlloyExpr
        inputChecks(expr, sfqn);
        sfqn = sfqn;
        kind = kind.EVENT;
        primeOkInPrmExprs = true;
        DashRef x = (DashRef) visit(expr);
        kind = defaultKind;
        primeOkInPrmExprs = false;
        return x;
    }

    // private functions

    private AlloyExpr resolve(AlloyVarExpr varExpr, List<? extends AlloyExpr> v_params) {
        // NADTODO buffers
        // NADTODO check on predicates
        /*
            This purpose of this function is to take a varExpr and possibly empty list of parameter values, and flesh out the reference to a FQN with all parameters values.

            Parameter values have to be determined from context, which is why sfqn is an argument to resolving.

            References to the parameter values of the current context (whether from a thisState or from lack of parameters) become DashParam(statename, parameter sig) within the expression.
        */
        // v is the name, v_params as the possibly empty set
        // of resolved param expr (which could include DashParams)
        // but it could still need to be added to

        String v = varExpr.label;
        List<? extends AlloyExpr> full_v_params = new ArrayList<AlloyExpr>();

        // first find the possible matches in the table for this
        // name v (could be state/event/var/buffer)
        List<String> matches;
        if (v_params.isEmpty()) {
            // if no param expr, then must be within region
            // that has same params
            matches = findMatchesInRegion(v);
        } else {
            // if it has params expr, params could be suffix of any var params
            // and later we check it has the right number of params
            matches = findMatches(v);
        }

        if (matches.isEmpty()) {
            if (!v_params.isEmpty()) unknownElementWithParamsError(varExpr);
            else unknownError(varExpr);
            return null;
        }

        String m = chooseMatch(matches);
        if (m == null) {
            ambiguousRefError(varExpr);
            return null;
        }
        // something defined as a predicate with the Dash module
        // TODO: check on this logic
        if (pt.contains(m)) {
            // best match is a predicate name
            // has to be treated a little differently
            // because does not have params and have to put its exp
            // directly in place unlike a DashRef
            // resolve the predicate value in place and add it in place
            DashStrings.DashRefKind kindTemp = kind;
            kind = kind.VAR;
            AlloyExpr ret = visit(pt.get(m).exp);
            kind = kindTemp;
            return ret;
        }

        // now m is one match from var/state/event table
        List<DashParam> mParams;
        if (kind == kind.STATE) mParams = st.get(m).params;
        else if (kind == kind.EVENT) mParams = et.get(m).params;
        else mParams = vt.get(m).params;

        // parameters from enclosing state of this element
        List<DashParam> sfqn_params = st.get(sfqn).params;
        if (v_params.isEmpty()) {
            // did not have any parameter values in use
            // must have same param values as sfqn b/c in same region
            if (mParams.size() > sfqn_params.size()) {
                // thing found by getRegion as match does
                // not have the same parameter values
                wrongNumberParamsError(varExpr);
                return null;
            } else {
                // this element might only use a subset of the
                // parameters of sfqn
                // could be a subset of param values
                // TODO: don't understand this case
                full_v_params = sfqn_params.subList(0, mParams.size());
            }
        } else if (mParams.size() != v_params.size()) {
            // came with parameters so must be right number
            // TODO could paramValues b less than mParams????
            // and paramValues be a suffix of mParams???
            // since the fqn name can be a suffix
            wrongNumberParamsError(varExpr);
            return null;
        } else {
            // it was used with the right number of parameters
            full_v_params = v_params;
        }

        if (kind == DashStrings.DashRefKind.STATE)
            return new StateDashRef(varExpr.pos, m, full_v_params);
        else if (kind == DashStrings.DashRefKind.EVENT)
            return new EventDashRef(varExpr.pos, m, full_v_params);
        else return new VarDashRef(varExpr.pos, m, full_v_params);
    }

    private List<String> findMatches(String name) {
        // set up names that we are searching for
        // from appropiate 'type' of element
        // for this function, match could be anywhere in Dash model
        List<String> region = new ArrayList<String>();
        if (kind == DashStrings.DashRefKind.STATE) region.addAll(st.getAllNames());
        else if (kind == DashStrings.DashRefKind.EVENT) region.addAll(et.getAllNames());
        else if (kind == DashStrings.DashRefKind.VAR) {
            region.addAll(vt.getAllVarNames());
            region.addAll(bt.getAllBufferNames());
            region.addAll(pt.getAllNames());
        }
        return compareNames(name, region);
    }

    private List<String> findMatchesInRegion(String name) {
        // set up names that we are searching for
        // from appropiate 'type' of element
        // for this function, match can only be within enclosing sfqn
        List<String> region = new ArrayList<String>();

        if (kind == DashStrings.DashRefKind.STATE) region = st.getRegion(sfqn);
        else if (kind == DashStrings.DashRefKind.EVENT) {
            // get all the events within these regions
            for (String x : st.getRegion(sfqn)) region.addAll(et.getEventsWithinState(x));
        } else if (kind == DashStrings.DashRefKind.VAR) {
            for (String x : st.getRegion(sfqn)) {
                region.addAll(vt.getVarsOfState(x));
                region.addAll(bt.getBuffersOfState(x));
            }
            region.addAll(pt.getAllNames());
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
        return binExpr.rebuild(visit(binExpr.left), visit(binExpr.right));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
        if (unaryExpr.op == AlloyStrings.PRIME) {
            // can only apply a prime to a var
            // this should be not allowed in parsing
            assert (unaryExpr.sub instanceof AlloyVarExpr);
            if (!primeOk) {
                noPrimedVarsError(unaryExpr);
            }
        }

        AlloyExpr newExpr = visit(unaryExpr.sub);

        if (unaryExpr.op == AlloyStrings.PRIME) {
            // if it is primed, the returned value
            // must be a DashRef (no other kind of
            // value can be primed)
            if (!(newExpr instanceof DashRef)) cantPrimeNonDynamicVarError(unaryExpr);
            else if (vt.isInternal(((DashRef) newExpr).name)) cantPrimeExternalVarError(newExpr);
        }
        // can't use a withSub here
        // because this is a parent class
        return unaryExpr.rebuild(newExpr);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyVarExpr varExpr) {
        // var that came with no param values
        String v = varExpr.label;
        // these should only be DashParams in this function

        if (thisOk && v.startsWith(AlloyStrings.THIS)) {
            // thisSname gets replaced with DashParam (sfqn, param)
            String thisstate = v.substring(AlloyStrings.THIS.length(), v.length());

            // have to change kind to search for to STATE
            DashStrings.DashRefKind kindTemp = kind;
            kind = DashStrings.DashRefKind.STATE;
            List<String> matches = findMatchesInRegion(thisstate);
            kind = kindTemp;

            String firstMatch = matches.get(0);
            if (matches.size() == 1 && st.hasParam(firstMatch)) {
                // parameters of a state are DashParam's
                // parameters used within an expression are Expr
                List<DashParam> ps = st.get(firstMatch).params;
                if (ps.size() == 1)
                    // any "thisState" use must be for a state with only one param
                    // this is already an Expr
                    return ps.get(0);
                else ambiguousUseOfThisError(varExpr);
            } else if (matches.size() == 1 && !st.hasParam(firstMatch))
                nonParamUseOfThisError(varExpr);
            else if (matches.size() > 1) ambiguousUseOfThisError(varExpr);
        }
        /* else we carry on with it as a regular var name with no params yet */
        // v_params is empty
        List<AlloyExpr> v_params = new ArrayList<AlloyExpr>();
        return resolve(varExpr, v_params);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyBlock block) {
        return new AlloyBlock(block.pos, mapBy(block.exprs, i -> visit(i)));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyBracketExpr bracketExpr) {
        AlloyExpr y = visit(bracketExpr.expr);
        List<AlloyExpr> x = mapBy(bracketExpr.exprs, i -> visit(i));
        return new AlloyBracketExpr(bracketExpr.pos, y, x);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyComprehensionExpr comprehensionExpr) {
        List<AlloyDecl> decls = mapBy(comprehensionExpr.decls, i -> (AlloyDecl) visit(i));
        AlloyExpr body = comprehensionExpr.body.map(value -> visit(value)).orElse(null);

        return new AlloyComprehensionExpr(comprehensionExpr.pos, decls, body);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyIteExpr iteExpr) {
        return new AlloyIteExpr(
                iteExpr.pos, visit(iteExpr.cond), visit(iteExpr.conseq), visit(iteExpr.alt));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyLetExpr letExpr) {
        // NADTODO: rule out var names that are bound
        List<AlloyLetExpr.AlloyLetAsn> asns = letExpr.asns;
        List<AlloyLetExpr.AlloyLetAsn> newAsns =
                mapBy(asns, i -> new AlloyLetExpr.AlloyLetAsn(i.pos, i.qname, visit(i.expr)));
        return new AlloyLetExpr(letExpr.pos, newAsns, visit(letExpr.body));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyQuantificationExpr quantificationExpr) {
        // NADTODO: rule out var names that are bound
        return new AlloyQuantificationExpr(
                quantificationExpr.pos,
                quantificationExpr.quant,
                mapBy(quantificationExpr.decls, i -> (AlloyDecl) visit(i)),
                visit(quantificationExpr.body));
    }

    @Override
    public AlloyExpr visit(AlloyDecl decl) {
        return decl.withExpr(visit(decl.expr));
    }

    public AlloyExpr visit(AlloyParenExpr parenExpr) {
        return new AlloyParenExpr(parenExpr.pos, visit(parenExpr.sub));
    }
    ;

    @Override
    public AlloyExpr visit(DashRef dashRef) {
        // can exist in parsing from x[a,b] or x[a,b]/v
        // name might not be fully resolved
        DashStrings.DashRefKind tempKind = kind;
        kind = dashRef.kind;
        AlloyExpr newExpr =
                resolve(
                        new AlloyQnameExpr(dashRef.pos, dashRef.name),
                        mapBy(dashRef.paramValues, i -> visit(i)));
        assert (newExpr instanceof DashRef);
        kind = tempKind;
        return newExpr;
    }
    ;

    @Override
    public AlloyExpr visit(DashParam dashParam) {
        return dashParam;
    }

    // errors methods cannot be grouped in a subclass
    // or be static because they reference attributes
    // of the class

    private void unknownError(AlloyExpr expr) {
        if (kind == DashStrings.DashRefKind.STATE)
            throw new Reporter.ErrorUser(expr.pos, "state does not exist: " + expr.toString());
        else if (kind == DashStrings.DashRefKind.EVENT)
            throw new Reporter.ErrorUser(expr.pos, "event does not exist: " + expr.toString());
        else throw new Reporter.ErrorUser(expr.pos, "variable does not exist: " + expr.toString());
    }

    private void wrongNumberParamsError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, "Incorrect number of parameters: " + expr.toString());
    }

    private void ambiguousRefError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(expr.pos, " Name not unique: " + expr.toString());
    }

    private void unknownElementWithParamsError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, " " + "Unknown Dash element with params: " + expr.toString());
    }

    private void cantPrimeNonVarError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, " " + " Non-var/buffer cannot be primed: " + expr.toString());
    }

    private void unknownSrcDestError(String x, String t, String tfqn) {
        throw new Reporter.ErrorUser(
                "Src/Dest of trans is unknown: " + "trans " + tfqn + " " + t + " " + x);
    }

    private void cantPrimeExternalVarError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, " Internal var/buffer cannot be primed: " + expr.toString());
    }

    private void noPrimedVarsError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, "Primed variables are not allowed in: " + expr.toString());
    }

    private void cantPrimeNonDynamicVarError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos,
                "Cannot prime something that is not a dynamic variable: " + expr.toString());
    }

    private void ambiguousUseOfThisError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(expr.pos, "Ambiguous use of 'this' " + expr.toString());
    }

    private void nonParamUseOfThisError(AlloyExpr expr) {
        throw new Reporter.ErrorUser(
                expr.pos, " 'this' must refer to a parametrized state: " + expr.toString());
    }
}
