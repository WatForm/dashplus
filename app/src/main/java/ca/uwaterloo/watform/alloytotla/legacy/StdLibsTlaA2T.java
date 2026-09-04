package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class StdLibsTlaA2T extends StdLibsAlloyA2T {

    public StdLibsTlaA2T(AlloyModel alloyModel, boolean verbose, boolean debug, Optimization optimization) {
        super(alloyModel, verbose, debug, optimization);
    }

  protected void addStdLibsTla(TlaModel tlaModel) {
    tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_FiniteSets));
    tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Integers));
    tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Sequences));
  }
}
