package ca.uwaterloo.watform.alloytotla;


import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.*;
import java.util.*;

public class InitDefn {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<TlaAppl> exps =
                Arrays.asList(
                        TlaAppl(SIG_SETS_UNPRIMED),
                        TlaAppl(ALL_SIG_CONSTRAINTS),
                        TlaAppl(ALL_FACTS));
        tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
    }
}
