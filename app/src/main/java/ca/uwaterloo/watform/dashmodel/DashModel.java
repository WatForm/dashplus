package ca.uwaterloo.watform.dashmodel;

import java.util.List;

public class DashModel extends DashModelResolve {

    // accessor methods

    // across both vartable and buffertable
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
