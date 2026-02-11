package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.SIG_SETS_UNPRIMED;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.INIT;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDefn;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class InitDefn {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {
        tlaModel.addDefn(TlaDefn(INIT, SIG_SETS_UNPRIMED()));
    }
}
