/*
    This visitor over Alloy expressions takes an Expr and sorts out any Alloy variable names that are Dash names used in it to:
    1) replace the names with their fully qualified name; a "name" used on input is allowed to be an unambiguous suffix of a fqn
    2) add in all the parameter values
    3) replace "thisState" with a DashParam
    4) turn PRIME(v) into v'
    and returns an Expr.

    It is called from the stateTable resolving for inits and invariants
    It is called from varTable for resolving type of dynamic variables (where it resolves only the name, not the parameters )
    It is called from transTable to resolve all parts of transitions.

    Variation points for these uses is in where to search for
    a name (st, EventTable, vt)

    Incoming Expr may also be a DashRef (from parsing for src/dest/on/send).

    Errors are given using "pos" of the incoming expression.
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class ExprRefResolverVis implements DashExprVis<AlloyExpr> {

    private Kind defaultKind = Kind.VAR;

    private StateTable st;
    private TransTable tt;
    private EventTable et;
    private VarTable vt;
    private BufferTable bt;
    private DashPredTable pt;
    private Kind kind = defaultKind;
    private boolean primeOk;
    private boolean primeOkInPrmExprs;
    private boolean thisOk;
    private String sfqn; // of state Expr itself or is parent of Expr

    public static enum Kind {
        STATE,
        VAR,
        EVENT
    }

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
        kind = Kind.VAR;
        primeOk = false;
        primeOkInPrmExprs = false;
        thisOk = true;
        // tables are not modified by may be modified
        // by calling functions in between calls to revolveExpr
    }

    // top-level calls

    public AlloyExpr resolveVar(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference in an expression
        // means we are looking for variables
        sfqn = sfqn;
        assert (kind == Kind.VAR);
        return visit(expr);
    }

    public AlloyExpr resolveVarPrimesOkAnywhere(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference in an expression
        // means we are looking for variables
        assert (kind == Kind.VAR);
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
        sfqn = sfqn;
        kind = kind.STATE;
        DashRef x = (DashRef) visit(expr);
        kind = defaultKind;
        return x;
    }

    public DashRef resolveEvent(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference to an event
        // we know this returns a DashRef rather than a more general AlloyExpr
        sfqn = sfqn;
        kind = kind.EVENT;
        DashRef x = (DashRef) visit(expr);
        kind = defaultKind;
        return x;
    }

    public DashRef resolveEventPrimesOkInPrmExprs(AlloyExpr expr, String sfqn) {
        // resolving something that is a reference to an event
        // we know this returns a DashRef rather than a more general AlloyExpr
        sfqn = sfqn;
        kind = kind.EVENT;
        primeOkInPrmExprs = true;
        DashRef x = (DashRef) visit(expr);
        kind = defaultKind;
        primeOkInPrmExprs = false;
        return x;
    }

    // private functions

    private AlloyExpr resolve(
            String v, List<? extends AlloyExpr> v_params, boolean isPrimed, Pos pos, Kind kind) {
        /*
            This purpose of this function is to take what we know about
            a variable references (primed or not, and included parsed DashRefs)
            and flesh out the reference to a fqn with all parameters values.

            Parameter values have to be determined from context, which is why sfqn is an argument to resolving.

            References to the parameter values of the current context (whether from a thisState or from lack of parameters) become DashParam(statename, parameter sig) within the expression.
        */
        // v is the name, v_params as the possibly empty set
        // of resolved param expr (which could include DashParams)
        // but it could still need to be added to

        if (isPrimed) {
            if (kind != kind.VAR) {
                Error.cantPrimeNonVar(pos, v);
                return null;
            } else if (!vt.isInternal(v)) {
                Error.cantPrimeExternalVar(pos, v);
                return null;
            }
        }

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

        String m = "";
        if (matches.isEmpty()) {
            if (kind == kind.STATE) {
                Error.unknownState(pos, v);
                return null;
            } else if (kind == kind.EVENT) {
                Error.unknownEvent(pos, v);
                return null;
            } else {
                // it's some var other than a dynamic variable or a predicate name
                if (!v_params.isEmpty()) {
                    Error.unknownElementWithParams(pos, v);
                }
                return null; // NADTODO new AlloyVarExpr(v);
            }
        } else {
            m = chooseMatch(matches);
            if (m == null) {
                Error.ambiguousRef(pos, v);
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
                Kind kindTemp = kind;
                kind = kind.VAR;
                AlloyExpr ret = visit(pt.get(m).exp);
                kind = kindTemp;
                return ret;
            }
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
                Error.wrongNumberParams(pos, v);
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
            Error.wrongNumberParams(pos, v);
            return null;
        } else {
            // it was used with the right number of parameters
            full_v_params = v_params;
        }

        if (isPrimed) m = m + PRIME;
        if (kind == Kind.STATE) return new StateDashRef(pos, m, full_v_params);
        else if (kind == Kind.EVENT) return new EventDashRef(pos, m, full_v_params);
        else return new VarDashRef(pos, m, full_v_params);
    }

    private List<String> findMatches(String name) {
        // set up names that we are searching for
        // from appropiate 'type' of element
        // for this function, match could be anywhere in Dash model
        List<String> region = new ArrayList<String>();
        if (kind == Kind.STATE) region.addAll(st.getAllNames());
        else if (kind == Kind.EVENT) region.addAll(et.getAllNames());
        else if (kind == Kind.VAR) {
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

        if (kind == Kind.STATE) region = st.getRegion(sfqn);
        else if (kind == Kind.EVENT) {
            // get all the events within these regions
            for (String x : st.getRegion(sfqn)) region.addAll(et.getEventsWithinState(x));
        } else if (kind == Kind.VAR) {
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
        // return new AlloyBinaryExpr(
        //         binExpr.pos, visit(binExpr.left), visit(binExpr.right), binExpr.op);
        return null;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
        // This could be a PRIME unary op
        if (unaryExpr.op == AlloyStrings.PRIME) {
            // can only apply a prime to a var
            // this should be not allowed in parsing
            assert (unaryExpr.sub instanceof AlloyVarExpr);
            if (!primeOk) {
                Error.noPrimedVars(unaryExpr.pos, unaryExpr.toString());
                return null;
            }
        }
        // can't use a withSub here
        // because this is a parent class
        return new AlloyUnaryExpr(unaryExpr.pos, visit(unaryExpr.sub), unaryExpr.op);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyVarExpr varExpr) {
        // NADTODO
        return null;
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
                mapBy(asns, i -> new AlloyLetExpr.AlloyLetAsn(i.pos, i.name, visit(i.expr)));
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
        // might have a prime on name
        // ASSUMPTION: due to the way DashRefs are parsed,
        // they are not turned into PrimeOp(name)
        return new DashRef(
                dashRef.pos, dashRef.kind, dashRef.name, mapBy(dashRef.paramValues, i -> visit(i)));
    }
    ;

    @Override
    public AlloyExpr visit(DashParam dashParam) {
        return dashParam;
    }

    private class Error {

        public static void wrongNumberParams(Pos pos, String expString) {
            throw new Reporter.ErrorUser(pos, "Incorrect number of parameters: " + expString);
        }

        public static void ambiguousRef(Pos pos, String expString) {
            throw new Reporter.ErrorUser(pos, " Name not unique: " + expString);
        }

        public static void unknownElementWithParams(Pos pos, String expString) {
            throw new Reporter.ErrorUser(
                    pos, " " + "Unknown Dash element with params: " + expString);
        }

        public static String cantPrimeNonVar(Pos pos, String expString) {
            throw new Reporter.ErrorUser(
                    pos, " " + " Non-var/buffer cannot be primed: " + expString);
        }

        public static void unknownSrcDest(String x, String t, String tfqn) {
            throw new Reporter.ErrorUser(
                    "Src/Dest of trans is unknown: " + "trans " + tfqn + " " + t + " " + x);
        }

        private static void unknown(Pos pos, String expString, String thing) {
            throw new Reporter.ErrorUser(pos, thing + " does not exist: " + expString);
        }

        public static void unknownState(Pos pos, String expString) {
            unknown(pos, expString, "state");
        }

        public static void unknownEvent(Pos pos, String expString) {
            unknown(pos, expString, "event");
        }

        public static String cantPrimeExternalVar(Pos pos, String expString) {
            throw new Reporter.ErrorUser(
                    pos, " Internal var/buffer cannot be primed: " + expString);
        }

        public static void noPrimedVars(Pos pos, String expString) {
            throw new Reporter.ErrorUser(pos, "Primed variables are not allowed in: " + expString);
        }
    }
}
