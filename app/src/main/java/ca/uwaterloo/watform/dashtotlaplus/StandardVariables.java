package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

class StandardVariables {

    // this class adds standard variables that are part of every translation

    public static void standardVariables(DashModel dashModel, TLAPlusModel TLAPlusModel) {

        // VARIABLES _conf, _events, _trans_taken, _scopes_used, _stable
        TLAPlusModel.addVariable(TranslationStrings.getConf());
        TLAPlusModel.addVariable(TranslationStrings.getEvents());
        TLAPlusModel.addVariable(TranslationStrings.getTransTaken());
        TLAPlusModel.addVariable(TranslationStrings.getScopesUsed());
        TLAPlusModel.addVariable(TranslationStrings.getStable());
    }
}
