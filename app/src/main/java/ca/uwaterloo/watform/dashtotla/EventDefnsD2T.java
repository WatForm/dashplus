package ca.uwaterloo.watform.dashtotla;

import java.util.List;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

public class EventDefnsD2T extends BaseD2T {

	public EventDefnsD2T(DashModel dashModel, TlaModel tlaModel, boolean verbose, boolean debug) {
		super(dashModel, tlaModel, verbose, debug);
	}

	protected void translateEventDefns()
	{
		if (!dashModel.hasEvents()) return;

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
