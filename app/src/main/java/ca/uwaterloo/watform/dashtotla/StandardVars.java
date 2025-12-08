package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;

class StandardVars {

    // this class adds standard variables that are part of every translation

    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // VARIABLES _conf, _trans_taken, _scopes_used, _stable, _ct
        Arrays.asList(CONF, TRANS_TAKEN, SCOPES_USED, STABLE, CT)
                .forEach(v -> tlaModel.addVariable(new TlaVar(v)));

        // this is subject to optimizations, and should remain its own function

        // _conf - stores the leaf states of the snapshot
        // _trans_taken - does not affect model execution, stored the transition taken to get to the
        // current snapshot, used for easy interpretation of TLC traces
        // _scopes_used - stores scopes (currently the same as leaf states), used to implement
        // big-step semantics
        // _stable - boolean variable, true if the current snapshot is stable

    }
}
