package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaStdLibs;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;

public class StandardLibraries {
    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // EXTENDS FiniteSets, Integers
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_FiniteSets));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Integers));
    }
}
