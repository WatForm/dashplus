package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.utils.*;
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
        // could precalculate this
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

    public List<String> getAllNames() {
        // vars plus buffers
        List<String> x = vt.keySet();
        x.addAll(bt.keySet());
        return x;
    }
}
