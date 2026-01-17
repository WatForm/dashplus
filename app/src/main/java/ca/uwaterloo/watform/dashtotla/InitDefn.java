package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class InitDefn {
    public static void translate(List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        List<TlaExp> exps = new ArrayList<>();

        exps.add(TYPE_OK());

        if (vars.contains(CONF)) {
            List<TlaAppl> initialStates =
                    mapBy(
                            AuxDashAccessors.initialEntered(dashModel),
                            sFQN -> TlaAppl(tlaFQN(sFQN)));

            exps.add(
                    // conf = union <initial states>...
                    CONF().EQUALS(repeatedUnion(initialStates)));
        }

        if (vars.contains(STABLE))
            exps.add(
                    // stable = TRUE
                    STABLE().EQUALS(TRUE()));

        if (vars.contains(SCOPES_USED))
            exps.add(
                    // scopes_used = {}
                    SCOPES_USED().EQUALS(NULL_SET()));

        if (vars.contains(TRANS_TAKEN))
            exps.add(
                    // _trans_taken = _none_transition
                    TRANS_TAKEN().EQUALS(NONE_TRANSITION()));

        if (vars.contains(EVENTS))
            exps.add(
                    // _events \in SUBSET _environmental_events
                    EVENTS().IN(TlaSubsetUnary(ENVIRONMENTAL_EVENTS())));

        tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
    }
}
