package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;

class StandardVariables {

    // this class adds standard variables that are part of every translation

    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // VARIABLES _conf, _events, _trans_taken, _scopes_used, _stable, _ct
        Arrays.asList(CONF, EVENTS, TRANS_TAKEN, SCOPE_USED, STABLE, CT)
                .forEach(v -> tlaModel.addVariable(new TlaVar(v)));

        // this is subject to optimizations, and should remain its own function
    }
}
