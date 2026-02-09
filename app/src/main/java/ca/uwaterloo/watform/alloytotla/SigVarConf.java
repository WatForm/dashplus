package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class SigVarConf {

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel, TempSigTable t) 
    {
        for(TempSigTable.Sig sig : t.listSigs)
        {
            makeSigSet(sig.name, alloyModel, tlaModel);
        }
    }

    public static void makeSigSet(String sigName, AlloyModel alloyModel, TlaModel tlaModel) {
        // S_set = {"S$0","S$1","S$2"...}

        

    }
}
