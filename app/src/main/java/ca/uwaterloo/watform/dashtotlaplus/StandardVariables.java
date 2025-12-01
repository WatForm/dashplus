package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

class StandardVariables {

    // this class adds standard variables that are part of every translation

    public static void standardVariables(DashModel dashModel, TLAPlusModel TLAPlusModel) {
        TLAPlusModel.addVariable(Common.getConf());
        TLAPlusModel.addVariable(Common.getEvents());
        TLAPlusModel.addVariable(Common.getTransTaken());
        TLAPlusModel.addVariable(Common.getScopesUsed());
    }
}
