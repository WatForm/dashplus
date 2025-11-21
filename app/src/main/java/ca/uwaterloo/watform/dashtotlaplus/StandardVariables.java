package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

class StandardVariables {

    // this class adds standard variables that are part of every translation

    public static void standardVariables(DashModel dashModel, TLAPlusModel TLAPlusModel) {
        TLAPlusModel.module.addVariable(Common.getConf());
        TLAPlusModel.module.addVariable(Common.getEvents());
    }
}
