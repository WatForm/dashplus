package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class AlloyToTla {
    public static TlaModel translate(
            AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {

        TlaModel tlaModel = new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));

        TempSigTable t = new TempSigTable();

        SigVars.translate(alloyModel, tlaModel, t);
        SigVarConf.translate(alloyModel, tlaModel, t);

        

        InitDefn.translate(alloyModel, tlaModel, t);
        NextDefn.translate(alloyModel, tlaModel, t);

        return tlaModel;
    }
}
