package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaEquals;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaTrue;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class InitDefn {
    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        // stable = TRUE
        TlaExp stableExp = new TlaEquals(new TlaVar(STABLE), new TlaTrue());

        // trans_taken = {}
        TlaExp transTakenExp =
                new TlaEquals(new TlaVar(TRANS_TAKEN), new TlaAppl(NONE_TRANSITION));

        // scopes_used = {}
        TlaExp scopesUsedExp = new TlaEquals(new TlaVar(SCOPES_USED), NULL_SET);

        // events = {}
        TlaExp eventsExp = new TlaEquals(new TlaVar(EVENTS), NULL_SET);

        // conf = {<initial states>}

        TlaExp confExp =
                new TlaEquals(
                        new TlaVar(CONF),
                        repeatedUnion(
                                mapBy(
                                        AuxDashAccessors.initialEntered(dashModel),
                                        sFQN -> new TlaAppl(tlaFQN(sFQN)))));

        List<TlaExp> expressions = new ArrayList<>();
        if (varNames.contains(CONF)) expressions.add(confExp);
        if (varNames.contains(SCOPES_USED)) expressions.add(scopesUsedExp);
        if (varNames.contains(STABLE)) expressions.add(stableExp);
        if (varNames.contains(TRANS_TAKEN)) expressions.add(transTakenExp);
        if (varNames.contains(EVENTS)) expressions.add(eventsExp);

        tlaModel.addDefn(new TlaDefn(new TlaDecl(INIT), repeatedAnd(expressions)));
    }
}
