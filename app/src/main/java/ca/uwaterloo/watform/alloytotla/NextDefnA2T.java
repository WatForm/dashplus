package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class NextDefnA2T extends InitDefnA2T {

    public NextDefnA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected void addNextDefn(TlaModel tlaModel) {
        List<TlaVar> unchanged = new ArrayList<>();

        alloyModel.allSigs().forEach(sigName -> unchanged.add(TlaVar(sigName)));
        alloyModel.allFields().forEach(field -> unchanged.add(TlaVar(field)));

        tlaModel.addDefn(TlaDefn(NEXT, TlaUnchanged(unchanged)));
    }
}
