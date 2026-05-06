package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class StdLibsA2T extends SigVarsA2T {

    public StdLibsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected void addStdLibs(TlaModel tlaModel) {
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_FiniteSets));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Integers));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Sequences));
    }
}
