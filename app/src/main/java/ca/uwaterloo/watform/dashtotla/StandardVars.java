package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaTypes;
import ca.uwaterloo.watform.tlamodel.TlaModel;

class StandardVars {

    // this class adds standard variables that are part of every translation

    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // this is subject to optimizations, and should remain its own function

        // _conf - stores the leaf states of the snapshot
        // _trans_taken - does not affect model execution, stored the transition taken to get to the
        // current snapshot, used for easy interpretation of TLC traces
        // _scopes_used - stores scopes (currently the same as leaf states), used to implement
        // big-step semantics
        // _stable - boolean variable, true if the current snapshot is stable

        // VARIABLES _conf, _trans_taken, _scopes_used, _stable

        tlaModel.addVar(TlaVar(TRANS_TAKEN), TlaTypes.Str());
        if (!dashModel.hasOnlyOneState())
            tlaModel.addVar(TlaVar(CONF), TlaTypes.Set(TlaTypes.Str()));

        if (dashModel.hasConcurrency()) {
            tlaModel.addVar(TlaVar(SCOPES_USED), TlaTypes.Set(TlaTypes.Str()));
            tlaModel.addVar(TlaVar(STABLE), TlaTypes.Bool());
        }
        if (dashModel.hasEvents()) tlaModel.addVar(TlaVar(EVENTS), TlaTypes.Set(TlaTypes.Str()));
    }
}
