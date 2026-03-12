package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.SIG_SETS_UNPRIMED;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;
import java.util.List;

public class InitDefnA2T extends FactsA2T {

    public InitDefnA2T(AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {
        super(alloyModel, moduleName, verbose, debug);
        translate();
    }

    public InitDefnA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
        translate();
    }

    public void translate() {
        List<TlaAppl> exps =
                Arrays.asList(
                        TlaAppl(SIG_SETS_UNPRIMED),
                        TlaAppl(ALL_SIG_CONSTRAINTS),
                        TlaAppl(ALL_FACTS));
        tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
    }
}
