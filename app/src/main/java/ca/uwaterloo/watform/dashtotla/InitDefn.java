package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class InitDefn {
    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        // stable = TRUE
        TlaExp stableExp = TlaEquals(TlaVar(STABLE), TlaTrue());

        // _trans_taken = _none_transition
        TlaExp transTakenExp = TlaEquals(TlaVar(TRANS_TAKEN), TlaAppl(NONE_TRANSITION));

        // scopes_used = {}
        TlaExp scopesUsedExp = TlaEquals(TlaVar(SCOPES_USED), TlaNullSet());

        // events = {}
        TlaExp eventsExp = TlaEquals(TlaVar(EVENTS), TlaNullSet());

        // conf = {<initial states>}

        TlaExp confExp =
                TlaEquals(
                        TlaVar(CONF),
                        repeatedUnion(
                                mapBy(
                                        AuxDashAccessors.initialEntered(dashModel),
                                        sFQN -> TlaAppl(tlaFQN(sFQN)))));

        List<TlaExp> expressions = new ArrayList<>();

        if (varNames.contains(CONF)) expressions.add(confExp);
        if (varNames.contains(SCOPES_USED)) expressions.add(scopesUsedExp);
        if (varNames.contains(STABLE)) expressions.add(stableExp);
        if (varNames.contains(TRANS_TAKEN)) expressions.add(transTakenExp);
        if (varNames.contains(EVENTS)) expressions.add(eventsExp);

        tlaModel.addDefn(TlaDefn(TlaDecl(INIT), repeatedAnd(expressions)));
    }
}
