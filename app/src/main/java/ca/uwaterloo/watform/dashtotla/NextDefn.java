package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaAnd;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class NextDefn {

    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // _Next = _type_OK /\ _small_step
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(NEXT),
                        new TlaAnd(new TlaAppl(TYPE_OK), new TlaAppl(SMALL_STEP))));

        /*
        Note: due to a quirk of TLC, the TYPE_OK must appear before SMALL_STEP, since the membership of the variable in a set has to be the first clause in a list, despite the order supposed to be semantically irrelevant
        */
    }
}
