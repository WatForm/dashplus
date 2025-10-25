/*  The StateTable maps:
	FullyQualStateName -> info about state

	It is created during the resolveAll phase.
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyDiffExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StateTable {

    private HashMap<String, StateElement> st;
    private String tableName = "State";
    public String root;

    // these get added to
    public List<AlloyExpr> initsR = new ArrayList<AlloyExpr>();
    public List<AlloyExpr> invsR = new ArrayList<AlloyExpr>();
    public List<DashParam> allParamsInOrder = new ArrayList<DashParam>();

    public StateTable() {
        this.st = new HashMap<String, StateElement>();
    }

    public String toString() {
        String s = new String("STATE TABLE\n");
        for (String k : st.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += st.get(k).toString();
        }
        return s;
    }

    public void add(Pos pos, String fqn) {
        assert (!fqn.isEmpty());
        if (st.containsKey(fqn)) {
            DashModelErrors.duplicateName(pos, "state", fqn);
        } else if (hasPrime(fqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, fqn);
        } else {
            st.put(fqn, null);
        }
    }

    public void add(
            Pos pos,
            String fqn,
            DashStrings.StateKind k,
            List<DashParam> prms,
            DashStrings.DefKind def,
            String parent,
            List<String> iChildren,
            List<DashInv> invP,
            List<DashInit> initP) {
        assert (!fqn.isEmpty());
        if (st.containsKey(fqn)) {
            DashModelErrors.duplicateName(pos, "state", fqn);
        } else if (hasPrime(fqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, fqn);
        } else {
            st.put(fqn, new StateElement(k, prms, def, parent, iChildren, invP, initP));
        }
    }

    public void addToParamsList(DashParam p) {
        allParamsInOrder.add(p);
    }

    public void addInitR(AlloyExpr exp) {
        initsR.add(exp);
    }

    public void addInvR(AlloyExpr exp) {
        invsR.add(exp);
    }

    public void setDefault(String s) {
        st.get(s).def = DashStrings.DefKind.DEFAULT;
    }

    // so we can treat this as a table
    // to the outside world
    public StateElement get(String sfqn) {
        return st.get(sfqn);
    }

    public List<String> keySet() {
        return setToList(st.keySet());
    }

    public boolean isEmpty() {
        return st.isEmpty();
    }

    // testers
    public boolean contains(String sfqn) {
        return st.containsKey(sfqn);
    }

    public boolean isRoot(String s) {
        return (s.equals(root));
    }

    public boolean isLeaf(String s) {
        return (st.get(s).immChildren.isEmpty());
    }

    public boolean isOr(String s) {
        return (st.get(s).kind == StateKind.OR);
    }

    public boolean isAnd(String s) {
        return (st.get(s).kind == StateKind.AND);
    }

    public boolean isDefault(String s) {
        return (st.get(s).def == DefKind.DEFAULT);
    }

    public boolean hasParam(String s) {
        return !st.get(s).params.isEmpty();
    }

    public boolean hasConcurrency() {
        return hasConcurrency(root);
    }

    public Boolean hasConcurrency(String s) {
        if (st.get(s).kind == DashStrings.StateKind.AND) return true;
        else
            for (String k : st.get(s).immChildren) {
                if (hasConcurrency(k)) return true;
            }
        return false;
    }

    public Boolean hasOnlyOneState() {
        return (st.keySet().size() == 1);
    }

    // complex individual getters
    public String getParam(String s) {
        return (lastElement(st.get(s).params)).paramSig;
    }

    public List<String> getAllAnces(String fqn) {
        // do not need to walk over tree for this operation; can just use FQNs
        List<String> fqnSplit = DashFQN.splitFQN(fqn);
        List<String> x = new ArrayList<String>();
        // include the state itself (could be Root)
        if (fqnSplit.size() > 0)
            for (int i = 0; i < fqnSplit.size(); i++)
                x.add(DashFQN.fqn(fqnSplit.subList(0, i + 1)));
        return x;
    }

    public List<String> getAllPrefixParamAnces(String sfqn) {
        return getAllAnces(sfqn).stream()
                .filter(i -> (isAnd(i) && hasParam(i)) || isRoot(i))
                .collect(Collectors.toList());
    }

    public String getClosestParamAnces(String s) {
        // getAllAnces returns list from Root, ..., parentFQN on path
        // could also just walk back through parents
        List<String> allAnces = getAllAnces(s);
        // allAnces.add(s);
        Collections.reverse(allAnces);

        String concAnces = null;
        // allAnces cannot be empty b/c must have Root in it
        for (String a : allAnces) {
            if (hasParam(a) || isRoot(a)) {
                concAnces = a;
                break;
            }
        }
        return concAnces; // might be null
    }

    public List<String> getAllNonParamDesc(String s) {
        // get all the descendants not WITHIN parameterized states
        // s is included
        // have to be careful to avoid duplicates
        List<String> desc = new ArrayList<String>();
        desc.add(s); // could be Root or a conc state

        for (String c : st.get(s).immChildren) {
            // System.out.println("in getAllNonParamDesc: "+c);
            if (isOr(c) || !hasParam(c)) desc.addAll(getAllNonParamDesc(c));
        }
        return desc;
    }

    // region is the area within which the src name does not need to be FQN
    public List<String> getRegion(String sfqn) {
        List<String> r = new ArrayList<String>();
        for (String s : getAllPrefixParamAnces(sfqn)) {
            r.addAll(getAllNonParamDesc(s));
        }
        return r;
    }

    public int getMaxDepthParams(String s) {
        // TODO: check this - seems to be not getting to full depth
        int max = 0;
        for (String c : get(s).immChildren) {
            List<DashParam> params = get(c).params;
            if (params != null) if (max < params.size()) max = params.size();
            if (max < getMaxDepthParams(c)) max = getMaxDepthParams(c);
        }
        return max;
    }

    public List<String> getDefaults(String s) {
        assert (!isLeaf(s) || get(s).immChildren.isEmpty());
        return filterBy(get(s).immChildren, c -> isDefault(c));
    }

    public List<AlloyExpr> getLeafStatesExited(DashRef s) {
        List<AlloyExpr> r = new ArrayList<AlloyExpr>();
        // System.out.println("exiting" + s.toString());
        if (isLeaf(s.name)) {
            r.add(s);
            return r;
        } else {
            // exit everything below even if not currently in it
            for (String ch : get(s.name).immChildren) {
                // exit all copies of the params
                List<AlloyExpr> newParamValues = new ArrayList<AlloyExpr>(s.paramValues);
                if (hasParam(ch)) newParamValues.add(new StateDashRef(ch, get(ch).params));
                r.addAll(getLeafStatesExited(new StateDashRef(ch, newParamValues)));
            }
            return r;
        }
    }

    public List<DashRef> getLeafStatesEntered(DashRef s) {
        List<DashRef> r = new ArrayList<DashRef>();
        if (isLeaf(s.name)) r.add(s);
        else {
            // enter every default below
            // if enter one c/p state enter all
            // might be one (if o) or many (if c/p)
            List<String> defaults = getDefaults(s.name);
            assert (defaults != null);
            for (String ch : defaults) {
                // System.out.println(ch);
                // enter all copies of the param if a parameterized state
                List<AlloyExpr> newParamValues = new ArrayList<AlloyExpr>(s.paramValues);
                if (hasParam(ch)) newParamValues.add(new AlloyNameExpr(s.pos, getParam(ch)));
                r.addAll(getLeafStatesEntered(new StateDashRef(ch, newParamValues)));
            }
        }
        return r;
    }

    public List<DashRef> allPrefixDashRefs(DashRef s) {
        // resulting order is ancestors to descendants
        // includes this DashRef itself at the end
        List<String> allPrefixFQNs = DashFQN.allPrefixes(s.name);
        List<DashRef> r = new ArrayList<DashRef>();
        int i = 0;
        for (String x : allPrefixFQNs) {
            if (isAnd(x) && hasParam(x)) {
                r.add(new StateDashRef(x, s.paramValues.subList(0, i + 1)));
                i++;
            } else r.add(new StateDashRef(x, s.paramValues.subList(0, i)));
        }
        assert (i == s.paramValues.size());
        return r;
    }

    /*
    	Assumption: context is an ancestor of dest

    	The param values of context do not have to match dest (but they will be subsets of the same set).

    	The param values of context (from the scope) could be a set of param values or they could match dest
    	(and therefore src of the trans as well). But they could be an ITE expression because of expressions
    	used in src/dest.

    	The dest param values must be singleton sets.
    	Does not seem to be any room for syntactic simplifications in these expressions.
    */
    public List<DashRef> getLeafStatesEnteredInScope(DashRef context, DashRef dest) {
        List<DashRef> cR = allPrefixDashRefs(context);
        List<DashRef> dR = allPrefixDashRefs(dest);
        List<DashRef> r = new ArrayList<DashRef>(); // result
        int p = 0; // parameter value position
        List<AlloyExpr> xP = new ArrayList<AlloyExpr>(); // parameters carrying forward
        List<AlloyExpr> nP; // parameters for each addition
        AlloyExpr e1;
        AlloyExpr e2;
        for (int i = 0; i < cR.size(); i++) {
            DashRef c = cR.get(i);
            if (isAnd(c.name) && hasParam(c.name)) {
                nP = new ArrayList<AlloyExpr>(xP);
                e1 = lastElement(c.paramValues);
                e2 = dest.paramValues.get(p);
                if (!e1.equals(e2)) {
                    nP.add(new AlloyDiffExpr(dest.pos, e1, e2));
                    r.addAll(getLeafStatesEntered(new StateDashRef(c.name, nP)));
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
                if (hasParam(chOfDest.name)) {
                    // ones not on path to dest
                    nP = new ArrayList<AlloyExpr>(allButLast(chOfDest.paramValues));
                    // all param values
                    e1 = new AlloyNameExpr(dest.pos, getParam(chOfDest.name));
                    e2 = lastElement(chOfDest.paramValues);
                    if (!e1.equals(e2)) {
                        nP.add(new AlloyDiffExpr(dest.pos, e1, e2));
                        r.addAll(getLeafStatesEntered(new StateDashRef(chOfDest.name, nP)));
                    } // if equal this is empty so don't include it
                }
                // siblings
                List<String> children = get(d.name).immChildren;
                List<String> andChildren =
                        children.stream().filter(c -> isAnd(c)).collect(Collectors.toList());
                andChildren.remove(chOfDest.name);
                // siblings
                for (String ch : andChildren) {
                    nP = new ArrayList<AlloyExpr>(d.paramValues);
                    if (hasParam(ch))
                        // add the entire param set
                        nP.add(new AlloyNameExpr(dest.pos, getParam(ch)));
                    r.addAll(getLeafStatesEntered(new StateDashRef(ch, nP)));
                }
            }
            // if its an OR state, just go on to the next one
        }
        r.addAll(getLeafStatesEntered(dest));
        return r;
    }

    // group getters
    public List<String> getAllNames() {
        return new ArrayList<String>(st.keySet());
    }

    public int getMaxDepthParams() {
        return getMaxDepthParams(root);
    }

    public List<DashParam> getAllParamsInOrder() {
        return allParamsInOrder;
    }

    public List<DashRef> getRootLeafStatesEntered() {
        List<AlloyExpr> x = new ArrayList<AlloyExpr>();
        return getLeafStatesEntered(new StateDashRef(root, x));
    }
}
