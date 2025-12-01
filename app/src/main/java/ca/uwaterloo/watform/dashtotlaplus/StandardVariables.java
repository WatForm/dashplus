package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

class StandardVariables {

    // this class adds standard variables that are part of every translation

    public static void standardVariables(DashModel dashModel, TLAPlusModel TLAPlusModel) {

        // VARIABLES _conf, _events, _trans_taken, _scopes_used, _stable
        TLAPlusModel.addVariable(Common.getConf());
        TLAPlusModel.addVariable(Common.getEvents());
        TLAPlusModel.addVariable(Common.getTransTaken());
        TLAPlusModel.addVariable(Common.getScopesUsed());
        TLAPlusModel.addVariable(Common.getStable());
    }
}
