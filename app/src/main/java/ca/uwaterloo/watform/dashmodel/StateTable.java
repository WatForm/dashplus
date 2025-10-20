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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StateTable {

    private HashMap<String, StateElement> st;
    public String root;

    // these get added to
    private List<AlloyExpr> inits = new ArrayList<AlloyExpr>();
    private List<AlloyExpr> invs = new ArrayList<AlloyExpr>();
    private List<DashParam> allParamsInOrder = new ArrayList<DashParam>();

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

    public void add(String fqn) {
        assert (!fqn.isEmpty());
        if (!st.containsKey(fqn)) st.put(fqn, null);
    }

    public boolean add(
            String fqn,
            DashStrings.StateKind k,
            List<DashParam> prms,
            DashStrings.DefKind def,
            String parent,
            List<String> iChildren,
            List<DashInv> invL,
            List<DashInit> initL) {
        if (st.containsKey(fqn)) return false;
        else if (hasPrime(fqn)) {
            DashModelErrors.nameShouldNotBePrimed(fqn);
            return false;
        } else {
            st.put(fqn, new StateElement(k, prms, def, parent, iChildren, invL, initL));
            return true;
        }
    }

    public void addToParamsList(DashParam p) {
        allParamsInOrder.add(p);
    }

    public void addInit(AlloyExpr exp) {
        inits.add(exp);
    }

    public void addInv(AlloyExpr exp) {
        invs.add(exp);
    }

    public void setDefault(String s) {
        st.get(s).def = DashStrings.DefKind.DEFAULT;
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

    // individual getters
    public DashStrings.StateKind getKind(String s) {
        return st.get(s).kind;
    }

    public DefKind getDef(String s) {
        return st.get(s).def;
    }

    public List<DashParam> getParams(String s) {
        return st.get(s).params;
    }

    public String getParent(String child) {
        return st.get(child).parent; // could be null if root
    }

    public List<String> getImmChildren(String parent) {
        return st.get(parent).immChildren;
    }

    public List<DashInit> getOrigInits(String sfqn) {
        return st.get(sfqn).origInits;
    }

    public List<DashInv> getOrigInvs(String sfqn) {
        return st.get(sfqn).origInvs;
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
        for (String c : getImmChildren(s)) {
            if (getParams(c) != null) if (max < getParams(c).size()) max = getParams(c).size();
            if (max < getMaxDepthParams(c)) max = getMaxDepthParams(c);
        }
        return max;
    }

    public List<String> getDefaults(String s) {
        assert (!isLeaf(s) || getImmChildren(s).isEmpty());
        return getImmChildren(s).stream().filter(c -> isDefault(c)).collect(Collectors.toList());
    }

    public List<DashRef> getLeafStatesExited(DashRef s) {
        List<DashRef> r = new ArrayList<DashRef>();
        // System.out.println("exiting" + s.toString());
        if (isLeaf(s.getName())) {
            r.add(s);
            return r;
        } else {
            // exit everything below even if not currently in it
            for (String ch : getImmChildren(s.getName())) {
                // exit all copies of the params
                List<AlloyExpr> newParamValues = new ArrayList<AlloyExpr>(s.getParamValues());
                if (hasParam(ch)) newParamValues.add(new AlloyNameExpr(s.pos, getParam(ch)));
                r.addAll(getLeafStatesExited(new StateDashRef(ch, newParamValues)));
            }
            return r;
        }
    }

    public List<DashRef> getLeafStatesEntered(DashRef s) {
        List<DashRef> r = new ArrayList<DashRef>();
        if (isLeaf(s.getName())) r.add(s);
        else {
            // enter every default below
            // if enter one c/p state enter all
            // might be one (if o) or many (if c/p)
            List<String> defaults = getDefaults(s.getName());
            assert (defaults != null);
            for (String ch : defaults) {
                // System.out.println(ch);
                // enter all copies of the param if a parameterized state
                List<AlloyExpr> newParamValues = new ArrayList<AlloyExpr>(s.getParamValues());
                if (hasParam(ch)) newParamValues.add(new AlloyNameExpr(s.pos, getParam(ch)));
                r.addAll(getLeafStatesEntered(new StateDashRef(ch, newParamValues)));
            }
        }
        return r;
    }

    public List<DashRef> allPrefixDashRefs(DashRef s) {
        // resulting order is ancestors to descendants
        // includes this DashRef itself at the end
        List<String> allPrefixFQNs = DashFQN.allPrefixes(s.getName());
        List<DashRef> r = new ArrayList<DashRef>();
        int i = 0;
        for (String x : allPrefixFQNs) {
            if (isAnd(x) && hasParam(x)) {
                r.add(new StateDashRef(x, s.getParamValues().subList(0, i + 1)));
                i++;
            } else r.add(new StateDashRef(x, s.getParamValues().subList(0, i)));
        }
        assert (i == s.getParamValues().size());
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
            if (isAnd(c.getName()) && hasParam(c.getName())) {
                nP = new ArrayList<AlloyExpr>(xP);
                e1 = lastElement(c.getParamValues());
                e2 = dest.getParamValues().get(p);
                if (!e1.equals(e2)) {
                    nP.add(new AlloyDiffExpr(dest.pos, e1, e2));
                    r.addAll(getLeafStatesEntered(new StateDashRef(c.getName(), nP)));
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
            if (isAnd(chOfDest.getName())) {
                // has sisters
                if (hasParam(chOfDest.getName())) {
                    // ones not on path to dest
                    nP = new ArrayList<AlloyExpr>(allButLast(chOfDest.getParamValues()));
                    // all param values
                    e1 = new AlloyNameExpr(dest.pos, getParam(chOfDest.getName()));
                    e2 = lastElement(chOfDest.getParamValues());
                    if (!e1.equals(e2)) {
                        nP.add(new AlloyDiffExpr(dest.pos, e1, e2));
                        r.addAll(getLeafStatesEntered(new StateDashRef(chOfDest.getName(), nP)));
                    } // if equal this is empty so don't include it
                }
                // siblings
                List<String> children = getImmChildren(d.getName());
                List<String> andChildren =
                        children.stream().filter(c -> isAnd(c)).collect(Collectors.toList());
                andChildren.remove(chOfDest.getName());
                // siblings
                for (String ch : andChildren) {
                    nP = new ArrayList<AlloyExpr>(d.getParamValues());
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
