package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaStdLibs;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;

public class StandardLibraries {
    public static void addStandardLibraries(DashModel dashModel, TlaModel tlaPlusModel) {

        // EXTENDS FiniteSets, Integers
        tlaPlusModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_FiniteSets));
        tlaPlusModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Integers));
    }
}
