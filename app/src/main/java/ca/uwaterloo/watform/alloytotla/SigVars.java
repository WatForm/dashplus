package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class SigVars {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        for (String sigName : alloyModel.allSigs()) tlaModel.addVar(TlaVar(sigName));
    }
}
