package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class DashModelAccessors extends DashModelResolve {

    public DashModelAccessors(DashFile d) {
        super(d);
    }

    // accessor methods
    public boolean hasOnlyOneState() {
        return st.hasOnlyOneState();
    }

    public String getRootName() {
        if (st.root != null) return st.root;
        else throw new ImplementationError("no root state in DashModel");
    }

    public boolean hasBuffers() {
        return (!bt.isEmpty());
    }

    public int getMaxDepthParams() {
        return maxDepthParams;
    }

    public Boolean transAtThisParamDepth(int i) {
        if (i > maxDepthParams) throw new ImplementationError("beyond max trans depth");
        else return transAtThisParamDepth[i];
    }

    public List<String> getNamesOfState(String sfqn) {
        List<String> x = vt.getVarsOfState(sfqn);
        x.addAll(bt.getBuffersOfState(sfqn));
        return x;
    }

    public List<String> getVarBufferNames() {
        // vars plus buffers
        List<String> x = vt.keySet();
        x.addAll(bt.keySet());
        return x;
    }

    // stuff about both states and trans

    public DashRef getScope(String tfqn) {
        // create a DashRef for the scope of tfqn
        // this is not necessarily an AND scope
        DashRef src = tt.get(tfqn).fromR;
        DashRef dest = tt.get(tfqn).gotoR;
        String sc = DashFQN.longestCommonFQN(src.name, dest.name);
        // maxCommonParams is max number of params that could have in common
        // but they don't necessarily have the same values
        Integer maxCommonParams = st.get(sc).params.size();
        List<AlloyExpr> scopeParams = new ArrayList<AlloyExpr>();
        AlloyExpr equals = null;
        DashRef s = null;
        DashRef d = null;
        for (int i = 0; i < maxCommonParams; i++) {
            s = (DashRef) src.paramValues.get(i);
            d = (DashRef) dest.paramValues.get(i);
            if (s.name == d.name) {
                // syntactically equal
                scopeParams.add(s);
            } else {
                equals = new AlloyEqualsExpr(s, d);
                scopeParams.add(
                        new AlloyIteExpr(
                                equals,
                                s,
                                ((DashParam) st.get(sc).params.get(i)).paramVar())); // whole set
                for (int j = i + 1; j < maxCommonParams; j++) {
                    s = (DashRef) src.paramValues.get(j);
                    d = (DashRef) dest.paramValues.get(j);
                    if (s.name == d.name) {
                        // syntactically equal
                        scopeParams.add(s);
                    } else {
                        equals = new AlloyAndExpr(equals, new AlloyEqualsExpr(s, d));
                        scopeParams.add(
                                new AlloyIteExpr(
                                        equals,
                                        s,
                                        ((DashParam) st.get(sc).params.get(j))
                                                .paramVar())); // whole set
                    }
                }
                break;
            }
        }
        return new StateDashRef(sc, scopeParams); // no pos possible
    }

    public DashRef getConcScope(String tfqn) {
        DashRef scope = getScope(tfqn);
        List<DashRef> aP = st.allPrefixDashRefs(scope);
        List<DashRef> aPc = onlyConcPlusRoot(aP);
        return lastElement(aPc); // might be the scope
    }

    public List<DashRef> exited(String tfqn) {
        // returns the list of states with params
        DashRef scope = getScope(tfqn);
        return st.getLeafStatesExited(scope);
    }

    public List<DashRef> entered(String tfqn) {
        return st.getLeafStatesEnteredInScope(getScope(tfqn), tt.get(tfqn).gotoR);
    }

    public List<DashRef> onlyConcPlusRoot(List<DashRef> dr) {
        List<DashRef> c = emptyList();
        c.add(new StateDashRef(st.root, emptyList()));
        c.addAll(filterBy(dr, x -> st.isAnd(x.name)));
        return c;
    }

    public List<DashRef> ancesConcScopes(DashRef d) {
        // includes d if it is a conc state
        List<DashRef> aP = st.allPrefixDashRefs(d);
        List<DashRef> aPc = onlyConcPlusRoot(aP);
        return aPc;
    }

    public List<DashRef> scopesUsed(String tfqn) {
        // includes Root only if that is the only scope
        List<DashRef> aPc = ancesConcScopes(getScope(tfqn));
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
        List<DashRef> aPc = ancesConcScopes(getConcScope(tfqn));
        // always needs to include Root
        return aPc;
    }

    public void debug() {
        System.out.println(st.toString());
        System.out.println(tt.toString());
        System.out.println(et.toString());
        System.out.println(vt.toString());
    }

    public void debug(String tfqn) {
        System.out.println(st.toString());
        System.out.println(tt.toString());
        System.out.println(et.toString());
        System.out.println(vt.toString());
        for (String x : tt.keySet()) {
            // System.out.println(tfqn +" scope :" + getScope(x));
        }
        if (tfqn != null) {
            System.out.println("src " + tt.get(tfqn).fromR);
            System.out.println("dest " + tt.get(tfqn).gotoR);
            System.out.println("pre " + tt.get(tfqn).whenR);
            System.out.println("post " + tt.get(tfqn).doR);
            System.out.println("getScope " + getScope(tfqn));
            System.out.println(
                    "getClosestParamAnces: " + st.getClosestParamAnces(tt.get(tfqn).fromR.name));
            // System.out.println("getAllNonParamDesc: "
            // +getAllNonParamDesc(getClosestConcAnces(getTransSrc(tfqn).getName())));
            System.out.println(
                    "getRegion:" + "Root/S1/S2: " + st.getRegion(tt.get(tfqn).fromR.name));
            System.out.println("exited: " + exited(tfqn));
            System.out.println("entered" + st.getLeafStatesEntered(tt.get(tfqn).gotoR));
            System.out.println("enteredInScope" + entered(tfqn));
            System.out.println(
                    "allPrefixDashRefs of scope: " + st.allPrefixDashRefs(getScope(tfqn)));
            System.out.println("scopesUsed: " + scopesUsed(tfqn));
            System.out.println("nonOrthogonalScopes: " + nonOrthogonalScopesOf(tfqn));
        }
    }
}
