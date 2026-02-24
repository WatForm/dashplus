package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.fieldVar;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class FieldVars {

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {
        alloyModel
                .allSigs()
                .forEach(
                        sn ->
                                alloyModel
                                        .fieldNames(sn)
                                        .forEach(fn -> tlaModel.addVar(fieldVar(fn, sn))));
    }
}
