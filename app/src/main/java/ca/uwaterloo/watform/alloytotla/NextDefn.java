package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.fieldVar;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.NEXT;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDefn;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaUnchanged;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class NextDefn {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<TlaVar> unchanged = new ArrayList<>();

        Auxiliary.getAllSigNames(alloyModel).forEach(sigName -> unchanged.add(new TlaVar(sigName)));

        Auxiliary.getAllSigNames(alloyModel)
                .forEach(
                        sn ->
                                Auxiliary.getFieldNames(sn, alloyModel)
                                        .forEach(fn -> unchanged.add(fieldVar(fn, sn))));

        tlaModel.addDefn(TlaDefn(NEXT, TlaUnchanged(unchanged)));
    }
}
