package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.SMALL_STEP;
import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.VALID_PRIMED;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class NextDefn {

    public static void translate(DashModel dashModel, TlaModel tlaModel, boolean singleEnvInput) {

        List<TlaExp> exps = new ArrayList<>();
        exps.add(VALID_PRIMED());

        if (dashModel.hasEvents() && singleEnvInput) exps.add(SINGLE_ENV_INPUT());

        exps.add(SMALL_STEP());

        tlaModel.addDefn(
                // _Next = _type_OK /\ _small_step
                TlaDefn(NEXT, repeatedAnd(exps)));

        /*
        Note: due to a quirk of TLC, the valid_primed must appear before SMALL_STEP, since the membership of the variable in a set has to be the first clause in a list, despite the order supposed to be semantically irrelevant
        */
    }
}
