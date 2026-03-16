package ca.uwaterloo.watform.dashtotla;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import java.util.ArrayList;
import java.util.List;

public class StandardVarsD2T extends SmallStepDefnD2T {

	public StandardVarsD2T(DashModel dashModel, TlaModel tlaModel, boolean verbose, boolean debug) {
		super(dashModel, tlaModel, verbose, debug);
	}

	protected void translateStandardVars()
	{
		// this is subject to optimizations, and should remain its own function

        // _conf - stores the leaf states of the snapshot
        // _trans_taken - does not affect model execution, stored the transition taken to get to the
        // current snapshot, used for easy interpretation of TLC traces
        // _scopes_used - stores scopes (currently the same as leaf states), used to implement
        // big-step semantics
        // _stable - boolean variable, true if the current snapshot is stable

        List<String> vars = new ArrayList<>();

        vars.add(TRANS_TAKEN);
        if (!dashModel.hasOnlyOneState()) vars.add(CONF);
        if (dashModel.hasConcurrency()) {
            vars.add(SCOPES_USED);
            vars.add(STABLE);
        }
        if (dashModel.hasEvents()) vars.add(EVENTS);

        // VARIABLES _conf, _trans_taken, _scopes_used, _stable
        vars.forEach(v -> tlaModel.addVar(TlaVar(v)));
	}
	
}
