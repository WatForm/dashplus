package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
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

        List<TlaExp> expressions = new ArrayList<>();

        // conf = {<initial states>}
        List<String> initialEnteredStates = AuxDashAccessors.initialEntered(dashModel);
        TlaExp confExp =
                CONF().EQUALS(
                                repeatedUnion(
                                        mapBy(
                                                initialEnteredStates,
                                                sFQN -> TlaAppl(tlaFQN(sFQN)))));
        if (varNames.contains(CONF)) expressions.add(confExp);

        if (varNames.contains(STABLE))
            expressions.add(
                    // stable = TRUE
                    STABLE().EQUALS(TRUE()));

        if (varNames.contains(SCOPES_USED))
            expressions.add(
                    // scopes_used = {}
                    SCOPES_USED().EQUALS(NULL_SET()));

        if (varNames.contains(TRANS_TAKEN))
            expressions.add(
                    // _trans_taken = _none_transition
                    TRANS_TAKEN().EQUALS(NONE_TRANSITION()));

        tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(expressions)));
    }
}
