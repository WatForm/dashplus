package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class AlloyToTla extends StdLibsA2T {

    public AlloyToTla(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
    }

    public void translate() {
        addBoilerplate();
        addSigConsts();
        addSigVars();
        addFieldVars();
        addSigHierarchy();
        addSigConstraints();
        addFacts();
        addInitDefn();
        addNextDefn();
    }
}
