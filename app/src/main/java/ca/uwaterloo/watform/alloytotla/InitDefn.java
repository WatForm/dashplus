package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.SIG_SETS_UNPRIMED;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.repeatedAnd;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.ALL_SIG_CONSTRAINTS;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.INIT;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.SIG_SETS_UNPRIMED;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaAppl;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDefn;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;
import java.util.List;

public class InitDefn {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<TlaAppl> exps =
                Arrays.asList(TlaAppl(SIG_SETS_UNPRIMED), TlaAppl(ALL_SIG_CONSTRAINTS));
        tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
    }
}
