package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class NextDefnA2T extends InitDefnA2T {

    public NextDefnA2T(AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {
        super(alloyModel, moduleName, verbose, debug);
        translate();
    }

    public NextDefnA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
        translate();
    }

    public void translate() {
        List<TlaVar> unchanged = new ArrayList<>();

        alloyModel.allSigs().forEach(sigName -> unchanged.add(new TlaVar(sigName)));

        alloyModel
                .allSigs()
                .forEach(
                        sn ->
                                alloyModel
                                        .fieldNames(sn)
                                        .forEach(fn -> unchanged.add(fieldVar(fn, sn))));

        tlaModel.addDefn(TlaDefn(NEXT, TlaUnchanged(unchanged)));
    }
}
