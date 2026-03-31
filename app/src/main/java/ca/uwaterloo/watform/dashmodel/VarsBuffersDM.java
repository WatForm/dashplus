package ca.uwaterloo.watform.dashmodel;

// NADTODO: what if var and buffer have the same name!!!
// These are functions that combine vars and buffers
// but later DMs rely on these so they have to be earlier
// in the DM chain than DashModel

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.DashStrings.IntEnvKind;
import java.util.List;

public class VarsBuffersDM extends BuffersDM {

    protected VarsBuffersDM() {
        super();
    }

    protected VarsBuffersDM(DashFile d) {
        super(d);
    }

    public List<String> namesOfState(String sfqn) {
        List<String> x = varsOfState(sfqn);
        x.addAll(buffersOfState(sfqn));
        return x;
    }

    public List<String> varAndBufferNames() {
        // vars plus buffers
        List<String> x = allVarNames();
        x.addAll(allBufferNames());
        return x;
    }

    public boolean isInt(String fqn) {
        if (this.containsVar(fqn)) {
            return this.isIntVar(fqn);
        } else {
            assert (this.containsBuffer(fqn));
            return this.isIntBuffer(fqn);
        }
    }

    public boolean isEnv(String fqn) {
        if (this.containsVar(fqn)) {
            return this.isEnvVar(fqn);
        } else {
            assert (this.containsBuffer(fqn));
            return this.isEnvBuffer(fqn);
        }
    }

    public IntEnvKind kind(String fqn) {
        if (this.containsVar(fqn)) {
            return this.varKind(fqn);
        } else {
            assert (this.containsBuffer(fqn));
            return this.bufferKind(fqn);
        }
    }

    public List<DashParam> params(String fqn) {
        if (this.containsVar(fqn)) {
            return this.varParams(fqn);
        } else {
            assert (this.containsBuffer(fqn));
            return this.bufferParams(fqn);
        }
    }
}
