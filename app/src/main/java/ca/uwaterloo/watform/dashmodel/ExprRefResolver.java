/*
	This visitor over Alloy expressions takes an Expr and sorts out any Dash names used in it to:
	1) replace the names with their fully qualified
		- a "name" used is allowed to be an unambiguous suffix of a fqn
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

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class ExprRefResolver {

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

    public ExprRefResolver(
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

    // for internal calls to resolve
    private AlloyExpr resolve(AlloyExpr expr) {
        return null;
    }

    public AlloyExpr resolveVar(AlloyExpr expr, String sfqn) {
        sfqn = sfqn;
        assert (kind == Kind.VAR);
        return null;
    }

    public AlloyExpr resolveVarPrimesOkAnywhere(AlloyExpr expr, String sfqn) {
        assert (kind == Kind.VAR);
        sfqn = sfqn;
        primeOk = true;
        primeOkInPrmExprs = true;
        AlloyExpr x = null;
        primeOk = false;
        primeOkInPrmExprs = false;
        return x;
    }

    public DashRef resolveState(AlloyExpr expr, String sfqn) {
        sfqn = sfqn;
        kind = kind.STATE;
        DashRef x = null;
        kind = defaultKind;
        return x;
    }

    public DashRef resolveEvent(AlloyExpr expr, String sfqn) {
        sfqn = sfqn;
        kind = kind.EVENT;
        DashRef x = null;
        kind = defaultKind;
        return x;
    }

    public DashRef resolveEventPrimesOkInPrmExprs(AlloyExpr expr, String sfqn) {
        sfqn = sfqn;
        kind = kind.EVENT;
        primeOkInPrmExprs = true;
        DashRef x = null;
        kind = defaultKind;
        primeOkInPrmExprs = false;
        return x;
    }

    /*
      public Expr visit(AlloyExprVar exp) {
    // came with no param values
    String v = getVarName((ExprVar) exp);
    // these should only be DashParams in this function


    if (thisOk && v.startsWith(thisName)) {
    	// thisSname gets replaced with DashParam (sfqn, param)
    	String thisstate = v.substring(thisName.length(),v.length());

    	// have to change kind to search for to STATE
    	String kindTemp = kind;
    	kind = STATE;
    	List<String> matches = findMatchesInRegion(thisstate);
    	kind = kindTemp;

    	String firstMatch = matches.get(0);
    	if (matches.size() == 1 && st.hasParam(firstMatch)) {
    		// parameters of a state are DashParams
    		// parameters used within an expression are Expr
    		List<Expr> ps = mapBy(st.getParams(firstMatch), i -> ((Expr) i));
    		if (ps.size() == 1)
    			// any "thisState" use must be for a state with only one param
    			// this is already an Expr
    			return ps.get(0);
    		else
    			DashErrors.ambiguousUseOfThis(exp.pos,exp.toString());
    	} else if (matches.size() == 1 && !st.hasParam(firstMatch))
    		DashErrors.nonParamUseOfThis(exp.pos,exp.toString());
    	else if (matches.size() > 1)
    		DashErrors.ambiguousUseOfThis(exp.pos,exp.toString());
    }
    // Types are not expressions
    // so not sure how to resolve this
    // hopefully types only get set in the resolveAlloy phase
    // perhaps we could just copy the type?
    // not clear what to do with type
    if (exp.type() != Type.EMPTY) {
    	DashErrors.resolvingVarWithType(exp.pos,exp.toString());
    }
    // else we carry on with it as a regular var name with no params yet
    // v_params is empty
    List<Expr> v_params = new ArrayList<Expr>();
    return resolve(v,v_params,false, exp.pos);
      }

      public Expr visit(DashRef exp) {
      	// can exist in parsing from x[a,b] or x[a,b]/v
    // name might not be fully resolved
    // might have a prime on name
    // ASSUMPTION: due to the way DashRefs are parsed,
    // they are not turned into PrimeOp(name)

      	boolean isPrimed = false;
    String v = ((DashRef) exp).getName();
    if (hasPrime(v)) {
    	isPrimed = true;
    	v = removePrime(v);
    	if (!primeOk) {
    		DashErrors.noPrimedVars(exp.pos, exp.toString());
    		return null;
    	}
    }
    // have to recurse through param expressions
    List<Expr> v_params = mapBy( ((DashRef) exp).getParamValues(), i -> visitThis(i));
    return resolve(v,v_params,isPrimed, exp.pos);
      }

      public Expr visit(DashParam x) throws Err {
      	// should not exist before resolution
      	DashErrors.missingCase("Trying to resolve a DashParam");
      }
      */

    /*
       public Expr visit(ExprUnary exp) throws Err {
       	if (isPrimedVar(exp)) {
       		// exp is PrimeOp(name)
    		if (!primeOk) {
    			DashErrors.noPrimedVars(exp.pos, exp.toString()); return null;
    		}
       		List<Expr> v_params = new ArrayList<Expr>();
    		String v = getVarName((ExprVar) getSub(exp));
    		return resolve(v,v_params,true, exp.pos);
    	} else {
    		return ((ExprUnary) exp).op.make(
    			exp.pos,
    			visitThis(((ExprUnary) exp).sub));
    	}
       }
    */
    // private functions -----------------------------------------------

    /*
    	This purpose of this function is to take what we know about
    	a variable references (primed or not, and included parsed DashRefs)
    	and flesh out the reference to a fqn with all parameters values.

    	Parameter values have to be determined from context, which is why sfqn is an argument to resolving.

    	References to the parameter values of the current context (whether from a thisState or from lack of parameters) become DashParam(statename, parameter sig) within the expression.
    */
    private AlloyExpr resolve(
            String v, List<? extends AlloyExpr> v_params, boolean isPrimed, Pos pos, Kind kind) {

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
                AlloyExpr ret = resolve(pt.get(m).exp);
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

    // set up names that we are searching for
    // from appropiate 'type' of element
    // for this function, match could be anywhere in Dash model
    private List<String> findMatches(String name) {
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

    // set up names that we are searching for
    // from appropiate 'type' of element
    // for this function, match can only be within enclosing sfqn
    private List<String> findMatchesInRegion(String name) {

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

    // region is a list of possible matches
    // 'name' is the name of the element to search for
    // it could be a suffix of the fqn in the region list
    // may return more than one possible match
    private List<String> compareNames(String name, List<String> region) {
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

    private class Error {

        public static void wrongNumberParams(Pos pos, String expString) {
            throw new ErrorUser(pos + "Incorrect number of parameters: " + expString);
        }

        public static void ambiguousRef(Pos pos, String expString) {
            throw new ErrorUser(pos + " Name not unique: " + expString);
        }

        public static void unknownElementWithParams(Pos pos, String expString) {
            throw new ErrorUser(pos + " " + "Unknown Dash element with params: " + expString);
        }

        public static String cantPrimeNonVar(Pos pos, String expString) {
            throw new ErrorUser(pos + " " + " Non-var/buffer cannot be primed: " + expString);
        }

        public static void unknownSrcDest(String x, String t, String tfqn) {
            throw new ErrorUser(
                    "Src/Dest of trans is unknown: " + "trans " + tfqn + " " + t + " " + x);
        }

        private static void unknown(Pos pos, String expString, String thing) {
            throw new ErrorUser(pos + thing + " does not exist: " + expString);
        }

        public static void unknownState(Pos pos, String expString) {
            unknown(pos, expString, "state");
        }

        public static void unknownEvent(Pos pos, String expString) {
            unknown(pos, expString, "event");
        }

        public static String cantPrimeExternalVar(Pos pos, String expString) {
            throw new ErrorUser(pos + " Internal var/buffer cannot be primed: " + expString);
        }
    }
}
