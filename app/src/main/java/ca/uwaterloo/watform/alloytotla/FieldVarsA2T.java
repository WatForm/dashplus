package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class FieldVarsA2T extends FactsA2T {

    public FieldVarsA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
    }

    protected void addFieldVars() {
        alloyModel
                .allSigs()
                .forEach(
                        sn -> alloyModel.fieldNames(sn).forEach(fn -> tlaModel.addVar(TlaVar(fn))));
    }
}
