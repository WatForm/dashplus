package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

public class EventDefns {
    public static void translate(List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        if (!vars.contains(EVENTS)) return;

        List<String> eventFQNs = dashModel.allEventNames();

        // _<event-name> == "<event-name>"
        eventFQNs.forEach(eFQN -> tlaModel.addDefn(TlaDefn(tlaFQN(eFQN), TlaStringLiteral(eFQN))));

        List<TlaAppl> envEvents = mapBy(dashModel.allEnvEvents(), eFQN -> TlaAppl(tlaFQN(eFQN)));

        // _environmental_events == {_<env-event-name-i>...}
        tlaModel.addDefn(TlaDefn(ENVIRONMENTAL_EVENTS, TlaSet(envEvents)));

        List<TlaAppl> intEvents = mapBy(dashModel.allIntEvents(), eFQN -> TlaAppl(tlaFQN(eFQN)));

        // _internal_events == {_<int-event-name-i>...}
        tlaModel.addDefn(TlaDefn(INTERNAL_EVENTS, TlaSet(intEvents)));
    }
}
