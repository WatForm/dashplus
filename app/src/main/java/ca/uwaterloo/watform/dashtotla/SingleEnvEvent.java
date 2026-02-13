package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.ENVIRONMENTAL_EVENTS;
import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.EVENTS;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloytotla.Boilerplate;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class SingleEnvEvent {

    public static void translate(DashModel dashModel, TlaModel tlaModel, boolean singleEnvInput) {

        if (!singleEnvInput) return;

        // add a formula:
        // _single_environmental_event == \A x \in S : \A y \in S : x = y
        // where S = _events \intersect _environmental_events
        // this formula is added only if relevant

        TlaExp body = TlaTrue();
        if (dashModel.hasEvents())
            body = Boilerplate._ONE(EVENTS().INTERSECTION(ENVIRONMENTAL_EVENTS()));

        tlaModel.addDefn(TlaDefn(SINGLE_ENV_INPUT, body));
    }
}
