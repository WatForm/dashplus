package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class DashModelAccessors extends DashModelResolve {

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
}
