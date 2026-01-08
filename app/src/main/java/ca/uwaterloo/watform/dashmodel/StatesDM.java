/*  The StateTable maps:
	FullyQualStateName -> info about state

	It is populated during the initialize phase.
*/

// TODO: rename "s" argument to "sfqn" everywhere

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings.DefKind;
import ca.uwaterloo.watform.dashast.DashStrings.StateKind;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StatesDM extends TransDM {

    protected HashMap<String, StateEntry> st = new HashMap<String, StateEntry>();
    public String rootName;

    // this the maximum depth of parameters in the state hierarchy
    protected int maxDepthParams = 0;

    // this is in order of depth-first traversal of the tree
    // these parameters can be from different branches
    // of the state hierarchy
    protected List<DashParam> allParamsInOrder = new ArrayList<DashParam>();

    public StatesDM() {
        super();
    }

    public StatesDM(DashFile d) {
        super(d);
    }

    // individual state attributes

    private StateEntry getStateEntry(String s) {
        StateEntry answer = this.st.get(s);
        if (answer == null) {
            printStackTrace();
            throw new ImplementationError(
                    "Attempt to access State Entry using String \"" + s + "\"");
        }
        return answer;
    }

    protected void setRootName(String r) {
        this.rootName = r;
    }

    public Pos statePos(String s) {
        return (this.getStateEntry(s).pos);
    }

    public boolean isLeaf(String s) {
        return (this.getStateEntry(s).immChildren.isEmpty());
    }

    public boolean isOr(String s) {
        return (this.getStateEntry(s).kind == StateKind.OR);
    }

    public boolean isAnd(String s) {
        return (this.getStateEntry(s).kind == StateKind.AND);
    }

    public DashParam stateParam(String sfqn) {
        return this.st.get(sfqn).param;
    }

    public boolean stateHasParams(String sfqn) {
        return !this.st.get(sfqn).params.isEmpty();
    }

    public List<DashParam> stateParams(String sfqn) {
        return this.st.get(sfqn).params;
    }

    public boolean isDefault(String s) {
        return (this.getStateEntry(s).def == DefKind.DEFAULT);
    }

    public String parent(String sfqn) {
        return this.st.get(sfqn).parent;
    }

    public List<String> immChildren(String sfqn) {
        return this.st.get(sfqn).immChildren;
    }

    // general getters

    public List<String> allStateNames() {
        return new ArrayList<String>(this.st.keySet());
    }

    protected Boolean hasStates() {
        // this would be an error everywhere
        return !this.st.isEmpty();
    }

    public int maxDepthParams() {
        return this.maxDepthParams;
    }

    public List<DashParam> allParamsInOrder() {
        return this.allParamsInOrder;
    }

    protected Boolean containsState(String sfqn) {
        return this.st.containsKey(sfqn);
    }

    public boolean isRoot(String s) {
        return (s.equals(this.rootName));
    }

    public String rootName() {
        return this.rootName;
    }

    public boolean hasConcurrency() {
        return hasConcurrency(this.rootName);
    }

    public Boolean hasConcurrency(String s) {
        if (this.getStateEntry(s).kind == DashStrings.StateKind.AND) return true;
        else
            for (String k : this.getStateEntry(s).immChildren) {
                if (hasConcurrency(k)) return true;
            }
        return false;
    }

    public Boolean hasOnlyOneState() {
        return (allStateNames().size() == 1);
    }

    // complex getters

    /*
        Assumption: context is an ancestor of dest

        The param values of context do not have to match dest (but they will be subsets of the same set).

        The param values of context (from the scope) could be a set of param values or they could match dest
        (and therefore src of the trans as well). But they could be an ITE expression because of expressions
        used in src/dest.

        The dest param values must be singleton sets.
        Does not seem to be any room for syntactic simplifications in these expressions.
    */
    public List<DashRef> leafStatesEnteredInScope(DashRef context, DashRef dest) {
        List<DashRef> cR = prefixDashRefs(context);
        List<DashRef> dR = prefixDashRefs(dest);
        List<DashRef> r = new ArrayList<DashRef>(); // result
        int p = 0; // parameter value position
        List<AlloyExpr> xP = new ArrayList<AlloyExpr>(); // parameters carrying forward
        List<AlloyExpr> nP; // parameters for each addition
        AlloyExpr e1;
        AlloyExpr e2;
        for (int i = 0; i < cR.size(); i++) {
            DashRef c = cR.get(i);
            if (isAnd(c.name) && stateHasParams(c.name)) {
                nP = new ArrayList<AlloyExpr>(xP);
                e1 = lastElement(c.paramValues);
                e2 = dest.paramValues.get(p);
                if (!e1.equals(e2)) {
                    nP.add(new AlloyDiffExpr(dest.pos, e1, e2));
                    r.addAll(leafStatesEntered(new StateDashRef(c.name, nP)));
                } // if equal this is empty so don't include it
                xP.add(e2); // just e2 for next one
                p++;
            }
        }
        // we've dealt with all the side paths in cR of dR (including the last one in CR)
        // now deal with the rest of dR by looking at the side paths of the children
        // might be nothing in this loop if cR and dR are the same length
        for (int i = cR.size() - 1; i < dR.size() - 1; i++) {
            DashRef d = dR.get(i); // first one will be match the last of cR
            DashRef chOfDest = dR.get(i + 1);
            // System.out.println("d: "+d);
            if (isAnd(chOfDest.name)) {
                // has sisters
                if (stateHasParams(chOfDest.name)) {
                    // ones not on path to dest
                    nP = new ArrayList<AlloyExpr>(allButLast(chOfDest.paramValues));
                    // all param values
                    // make a var
                    e1 = new AlloyNameExpr(dest.pos, stateParam(chOfDest.name).stateName);
                    e2 = lastElement(chOfDest.paramValues);
                    if (!e1.equals(e2)) {
                        nP.add(new AlloyDiffExpr(dest.pos, e1, e2));
                        r.addAll(leafStatesEntered(new StateDashRef(chOfDest.name, nP)));
                    } // if equal this is empty so don't include it
                }
                // siblings
                /* removed code
                List<String> children = immChildren(d.name);
                List<String> andChildren =
                        children.stream().filter(c -> isAnd(c)).collect(Collectors.toList());
                andChildren.remove(chOfDest.name);
                */
                List<String> andChildren =
                        filterBy(immChildren(d.name), c -> isAnd(c) && !c.equals(chOfDest.name));
                // siblings
                for (String ch : andChildren) {
                    nP = new ArrayList<AlloyExpr>(d.paramValues);
                    if (stateHasParams(ch))
                        // add the entire param set
                        // make a Var
                        nP.add(new AlloyNameExpr(dest.pos, stateParam(ch).stateName));
                    r.addAll(leafStatesEntered(new StateDashRef(ch, nP)));
                }
            }
            // if its an OR state, just go on to the next one
        }
        r.addAll(leafStatesEntered(dest));
        return r;
    }

    public List<DashRef> rootLeafStatesEntered() {
        List<AlloyExpr> x = new ArrayList<AlloyExpr>();
        return leafStatesEntered(new StateDashRef(this.rootName, x));
    }

    // stuff about both states and trans

    public DashRef scope(String tfqn) {
        // create a DashRef for the scope of tfqn
        // this is not necessarily an AND scope
        DashRef src = fromR(tfqn);
        DashRef dest = gotoR(tfqn);
        String sc = DashFQN.longestCommonFQN(src.name, dest.name);
        // maxCommonParams is max number of params that could have in common
        // but they don't necessarily have the same values
        Integer maxCommonParams = stateParams(sc).size();
        List<AlloyExpr> scopeParams = new ArrayList<AlloyExpr>();
        AlloyExpr equals = null;
        AlloyExpr s = null;
        AlloyExpr d = null;
        for (int i = 0; i < maxCommonParams; i++) {
            s = src.paramValues.get(i);
            d = dest.paramValues.get(i);
            if (s.equals(d)) {
                // syntactically equal
                scopeParams.add(s);
            } else {
                equals = new AlloyEqualsExpr(s, d);
                scopeParams.add(
                        new AlloyIteExpr(
                                equals,
                                s,
                                ((DashParam) stateParams(sc).get(i)).paramVar())); // whole set
                for (int j = i + 1; j < maxCommonParams; j++) {
                    s = (DashRef) src.paramValues.get(j);
                    d = (DashRef) dest.paramValues.get(j);
                    if (s.equals(d)) {
                        // syntactically equal
                        scopeParams.add(s);
                    } else {
                        equals = new AlloyAndExpr(equals, new AlloyEqualsExpr(s, d));
                        scopeParams.add(
                                new AlloyIteExpr(
                                        equals,
                                        s,
                                        ((DashParam) stateParams(sc).get(j))
                                                .paramVar())); // whole set
                    }
                }
                break;
            }
        }
        return new StateDashRef(sc, scopeParams); // no pos possible
    }

    public DashRef concScope(String tfqn) {
        DashRef scope = scope(tfqn);
        List<DashRef> aP = prefixDashRefs(scope);
        List<DashRef> aPc = onlyConcPlusRoot(aP);
        return lastElement(aPc); // might be the scope
        // MKJ - what does "might" mean here?
        // aP and aPc are not descriptive names
    }

    public List<DashRef> exited(String tfqn) {
        // returns the list of states with params
        DashRef scope = scope(tfqn);
        return leafStatesExited(scope);
    }

    public List<DashRef> entered(String tfqn) {
        return leafStatesEnteredInScope(scope(tfqn), gotoR(tfqn));
    }

    public List<DashRef> onlyConcPlusRoot(List<DashRef> dr) {
        List<DashRef> c = emptyList();
        c.add(new StateDashRef(this.rootName, emptyList()));
        c.addAll(filterBy(dr, x -> isAnd(x.name)));
        return c;
    }

    public List<DashRef> ancesConcScopes(DashRef d) {
        // includes d if it is a conc state
        List<DashRef> aP = prefixDashRefs(d);
        List<DashRef> aPc = onlyConcPlusRoot(aP);
        return aPc;
    }

    public List<DashRef> scopesUsed(String tfqn) {
        // includes Root only if that is the only scope
        List<DashRef> aPc = ancesConcScopes(scope(tfqn));
        List<DashRef> r = new ArrayList<DashRef>();

        // System.out.println(aPc);
        if (aPc.size() == 1) {
            // scope must be the root
            // so add it
            r.add(aPc.get(0));
        } else {
            // don't put the root in unless
            // the root is the scope
            r = tail(aPc);
        }
        return r;
    }

    public List<DashRef> nonOrthogonalScopesOf(String tfqn) {
        List<DashRef> aPc = ancesConcScopes(concScope(tfqn));
        // always needs to include Root
        return aPc;
    }

    // private functions below; used to calculate above

    public List<String> allAnces(String sfqn) {
        // do not need to walk over tree for this operation; can just use FQNs
        // MKJ - earlier implementations of this function returned illegal strings which are not
        // state names, resulting in NullPointerExceptions when applied to getter functions
        // is there any benefit to avoiding a walk over the tree? The cost is minimal, and the code
        // is easier to read and maintain since it doesn't rely on the FQN notation. The code may be
        // harder to write but it's only written once
        // this fails when sfqn = "/root"

        // System.out.println("in allAnces");
        // System.out.println(sfqn);
        List<String> sfqnSplit = DashFQN.splitFQN(sfqn);
        List<String> x = new ArrayList<String>();
        // include the state itself (could be Root)
        if (sfqnSplit.size() > 0)
            for (int i = 0; i < sfqnSplit.size(); i++)
                x.add(DashFQN.fqn(sfqnSplit.subList(0, i + 1)));
        // if (x.contains("")) System.out.println(sfqn);
        // System.out.println(x);
        return x;
    }

    private List<String> prefixParamAnces(String sfqn) {
        return filterBy(allAnces(sfqn), s -> (isAnd(s) && stateHasParams(s)) || isRoot(s));
    }

    public String closestParamAnces(String sfqn) {
        // allAnces returns list from Root, ..., parentFQN on path
        // could also just walk back through parents
        List<String> allAnces = allAnces(sfqn);
        // allAnces.add(s);
        Collections.reverse(allAnces);

        String concAnces = null;
        // allAnces cannot be empty b/c must have Root in it
        for (String a : allAnces) {
            if (stateHasParams(a) || isRoot(a)) {
                concAnces = a;
                break;
            }
        }
        return concAnces; // might be null
    }

    private List<String> nonParamDesc(String sfqn) {
        // get all the descendants not WITHIN parameterized states
        // s is included
        // have to be careful to avoid duplicates
        List<String> desc = new ArrayList<String>();
        desc.add(sfqn); // could be Root or a conc state

        for (String c : immChildren(sfqn)) {
            if (isOr(c) || !stateHasParams(c)) desc.addAll(nonParamDesc(c));
        }
        return desc;
    }

    // region is the area within which the src name does not need to be FQN
    public List<String> region(String sfqn) {
        List<String> r = new ArrayList<String>();
        for (String s : prefixParamAnces(sfqn)) {
            r.addAll(nonParamDesc(s));
        }
        return r;
    }

    public List<String> defaults(String sfqn) {
        assert (!isLeaf(sfqn) || immChildren(sfqn).isEmpty());
        return filterBy(immChildren(sfqn), c -> isDefault(c));
    }

    private List<DashRef> leafStatesExited(DashRef s) {

        List<DashRef> r = new ArrayList<DashRef>();

        if (isLeaf(s.name)) {
            r.add(s);
            return r;
        } else {
            // exit everything below even if not currently in it
            for (String ch : immChildren(s.name)) {
                // exit all copies of the params
                List<AlloyExpr> newParamValues = new ArrayList<AlloyExpr>(s.paramValues);
                if (stateHasParams(ch)) newParamValues.add(new StateDashRef(ch, stateParams(ch)));
                r.addAll(leafStatesExited(new StateDashRef(ch, newParamValues)));
            }
            return r;
        }
    }

    public List<DashRef> leafStatesEntered(DashRef s) {
        List<DashRef> r = new ArrayList<DashRef>();
        if (isLeaf(s.name)) r.add(s);
        else {
            // enter every default below
            // if enter one c/p state enter all
            // might be one (if o) or many (if c/p)
            List<String> defaults = defaults(s.name);
            assert (defaults != null);
            for (String ch : defaults) {
                // System.out.println(ch);
                // enter all copies of the param if a parameterized state
                List<AlloyExpr> newParamValues = new ArrayList<AlloyExpr>(s.paramValues);
                if (stateHasParams(ch))
                    for (DashParam p:stateParams(ch))
                        newParamValues.add(new AlloyNameExpr(s.pos, p.paramSig));
                r.addAll(leafStatesEntered(new StateDashRef(ch, newParamValues)));
            }
        }
        return r;
    }

    public List<DashRef> prefixDashRefs(DashRef s) {
        // resulting order is ancestors to descendants
        // includes this DashRef itself at the end
        List<String> allPrefixFQNs = DashFQN.allPrefixes(s.name);
        List<DashRef> r = new ArrayList<DashRef>();
        int i = 0;
        for (String x : allPrefixFQNs) {
            if (isAnd(x) && stateHasParams(x)) {
                r.add(new StateDashRef(x, s.paramValues.subList(0, i + 1)));
                i++;
            } else r.add(new StateDashRef(x, s.paramValues.subList(0, i)));
        }
        assert (i == s.paramValues.size());
        return r;
    }

    public String stToString() {
        String s = new String("STATE TABLE\n");
        List<String> allStateNames = allStateNames();
        Collections.sort(allStateNames);
        for (String k : allStateNames) {
            s += " ----- \n";
            s += k + "\n";
            s += this.st.get(k).toString();
        }
        return s;
    }

    public void addState(Pos pos, String fqn) {
        assert (!fqn.isEmpty());
        if (this.st.containsKey(fqn)) {
            DashModelErrors.duplicateName(pos, "state", fqn);
        } else if (hasPrime(fqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, fqn);
        } else {
            this.st.put(fqn, null);
        }
    }

    public void addState(
            Pos pos,
            String sfqn,
            DashStrings.StateKind k,
            DashParam prm,
            List<DashParam> prms,
            DashStrings.DefKind def,
            String parent,
            List<String> iChildren) {
        assert (!sfqn.isEmpty());
        if (this.st.containsKey(sfqn)) {
            DashModelErrors.duplicateName(pos, "state", sfqn);
        } else if (hasPrime(sfqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, sfqn);
        } else {
            this.st.put(sfqn, new StateEntry(pos, k, prm, prms, def, parent, iChildren));
        }
    }

    private class StateEntry {

        // these are public so those with access to
        // element in state table can access them

        public final Pos pos;
        public final DashStrings.StateKind kind;

        // param of this state; may be null
        public final DashParam param;

        // empty if none
        public final List<DashParam> params;

        public final DashStrings.DefKind def;

        // these all both are FQNs to point to states in this StateTable
        public final String parent; // null if none
        public final List<String> immChildren; // empty if none

        public StateEntry(
                Pos p,
                DashStrings.StateKind k,
                DashParam prm,
                List<DashParam> prms,
                DashStrings.DefKind def,
                String parent,
                List<String> iChildren) {
            assert (p != null);
            assert (k != null);
            assert (prms != null); // could be empty
            assert (parent == null || !parent.isEmpty());
            assert (iChildren != null); // could be empty
            this.pos = p;
            this.kind = k;
            this.param = prm;
            this.params = prms;
            this.def = def;
            this.parent = parent;
            this.immChildren = iChildren;
        }

        public String toString() {
            String s = new String();
            s += "kind: " + this.kind + "\n";
            s += "param: " + NoneStringIfNeeded(this.param) + "\n";
            s += "params: " + NoneStringIfNeeded(this.params) + "\n";
            s += "default: " + this.def + "\n";
            s += "parent: " + NoneStringIfNeeded(this.parent) + "\n";
            s += "immChildren: " + NoneStringIfNeeded(this.immChildren) + "\n";
            return s;
        }
    }
}
