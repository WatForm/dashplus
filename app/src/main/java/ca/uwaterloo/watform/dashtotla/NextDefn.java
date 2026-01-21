package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class NextDefn {

    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        tlaModel.addDefn(
                // _Next = _type_OK /\ _small_step
                TlaDefn(NEXT, VALID_PRIMED().AND(SMALL_STEP())));

        /*
        Note: due to a quirk of TLC, the valid_primed must appear before SMALL_STEP, since the membership of the variable in a set has to be the first clause in a list, despite the order supposed to be semantically irrelevant
        */
    }
}
