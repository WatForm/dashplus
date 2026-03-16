package ca.uwaterloo.watform.dashtotla;

import java.util.ArrayList;
import java.util.List;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class NextDefnD2T extends InitDefnD2T {

	public NextDefnD2T(DashModel dashModel, TlaModel tlaModel, boolean verbose, boolean debug) {
		super(dashModel, tlaModel, verbose, debug);
	}

	protected void translateNextDefn()
	{
		List<TlaExp> exps = new ArrayList<>();
        exps.add(VALID_PRIMED());

		// single env input assumption
        if (dashModel.hasEvents()) exps.add(SINGLE_ENV_INPUT());

        exps.add(SMALL_STEP());

        tlaModel.addDefn(
                // _Next = _type_OK /\ _small_step
                TlaDefn(NEXT, repeatedAnd(exps)));

        /*
        Note: due to a quirk of TLC, the valid_primed must appear before SMALL_STEP, since the membership of the variable in a set has to be the first clause in a list, despite the order supposed to be semantically irrelevant
        */
	}
	
}
