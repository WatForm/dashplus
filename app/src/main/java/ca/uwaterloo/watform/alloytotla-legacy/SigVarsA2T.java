package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaTypes;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class SigVarsA2T extends SigHierarchyA2T {

<<<<<<< HEAD
    public SigVarsA2T(AlloyModel alloyModel, boolean verbose, boolean debug, Optimization optimization) {
        super(alloyModel, verbose, debug, optimization);
=======
  public SigVarsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
    super(alloyModel, verbose, debug);
  }

  protected void addSigVars(TlaModel tlaModel) {

    for (var sigName : alloyModel.allSigs()) {
      tlaModel.addVar(TlaVar(sigName), TlaTypes.Set(TlaTypes.Seq(TlaTypes.Str())));
      log("translated sig " + sigName + " into a VARIABLE");
>>>>>>> 241b219 (Generalized build to create multiple tools from same repo.)
    }

    l.info(dump());
  }
}
