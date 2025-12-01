package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStandardLibraries;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

public class StandardLibraries {
    public static void addStandardLibraries(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addSTL(
                new TLAPlusStandardLibraries(TLAPlusStandardLibraries.LIBRARIES.STL_FiniteSets));
        tlaPlusModel.addSTL(
                new TLAPlusStandardLibraries(TLAPlusStandardLibraries.LIBRARIES.STL_Integers));
    }
}
