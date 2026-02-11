package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class StdLibDefns {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        // FiniteSets is used for Cardinality and Integers
        // EXTENDS FiniteSets, Integers
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_FiniteSets));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Integers));
    }
}
