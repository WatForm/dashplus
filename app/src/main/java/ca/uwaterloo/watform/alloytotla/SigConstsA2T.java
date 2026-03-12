package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaStringLiteral;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class SigConstsA2T extends BoilerplateA2T {

    public SigConstsA2T(AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {
        super(alloyModel, moduleName, verbose, debug);
        translate();
    }

    public SigConstsA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
        translate();
    }

    public void translate() {
        for (String name : alloyModel.topLevelSigs()) {
            makeSigSet(name, 4);
        }
    }

    public void makeSigSet(String sigName, int n) {
        // S_set = {"S$0","S$1","S$2"...}
        List<TlaStringLiteral> strings = new ArrayList<>();
        for (int i = 0; i < n; i++) strings.add(TlaStringLiteral(sigSetString(sigName, i)));

        tlaModel.addConst(TlaConst(sigSet(sigName)), TlaSet(strings));
    }

    public String sigSetString(String sigName, int i) {
        return sigName + AlloyToTlaStrings.DOLLAR + i;
    }
}
