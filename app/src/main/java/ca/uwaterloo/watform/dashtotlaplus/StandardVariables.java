package ca.uwaterloo.watform.dashtotlaplus;

import static ca.uwaterloo.watform.dashtotlaplus.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import java.util.Arrays;

class StandardVariables {

    // this class adds standard variables that are part of every translation

    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // VARIABLES _conf, _events, _trans_taken, _scopes_used, _stable, _ct
        Arrays.asList(CONF, EVENTS, TRANS_TAKEN, SCOPE_USED, STABLE, CT)
                .forEach(v -> tlaModel.addVariable(v.globalVar()));

        // this is subject to optimizations, and should remain its own function
    }
}
