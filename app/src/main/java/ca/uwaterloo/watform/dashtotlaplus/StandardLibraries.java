package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStdLibs;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

public class StandardLibraries {
    public static void addStandardLibraries(DashModel dashModel, TLAPlusModel tlaPlusModel) {

		// EXTENDS FiniteSets, Integers 
        tlaPlusModel.addSTL(
                new TLAPlusStdLibs(TLAPlusStdLibs.LIBRARIES.STL_FiniteSets));
        tlaPlusModel.addSTL(
                new TLAPlusStdLibs(TLAPlusStdLibs.LIBRARIES.STL_Integers));
    }
}
