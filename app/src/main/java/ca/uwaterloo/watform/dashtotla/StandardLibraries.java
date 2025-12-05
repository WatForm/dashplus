package ca.uwaterloo.watform.dashtotla;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class StandardLibraries {
    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // FiniteSets is used for Cardinality and Integers
        // EXTENDS FiniteSets, Integers
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_FiniteSets));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Integers));
    }
}
