package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class SigVarsA2T extends SigHierarchyA2T {

    public SigVarsA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
    }

    protected void addSigVars() {
        for (var sigName : alloyModel.allSigs()) {
            tlaModel.addVar(TlaVar(sigName));
        }
    }
}
