package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.tlaliterals.*;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class SigVarConf {

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        for (String name : Auxiliary.getTopLevelSigNames(alloyModel)) {
            makeSigSet(name, alloyModel, tlaModel, 4);
        }
    }

    public static void makeSigSet(String sigName, AlloyModel alloyModel, TlaModel tlaModel, int n) {
        // S_set = {"S$0","S$1","S$2"...}
        List<TlaStringLiteral> strings = new ArrayList<>();
        for (int i = 0; i < n; i++) strings.add(TlaStringLiteral(sigSetString(sigName, i)));

        tlaModel.addConst(TlaConst(sigSet(sigName)), TlaSet(strings));
    }

    public static String sigSetString(String sigName, int i) {
        return sigName + AlloyToTlaStrings.DOLLAR + i;
    }
}
