package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaAnd;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaEquals;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaOr;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaUnchanged;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaNot;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaPrime;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmallStepDefn {
    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxDashAccessors.getTransitionNames(dashModel);

        // _some_transition = 
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SOME_TRANSITION),
                        repeatedOr(mapBy(transitions, t -> new TlaAppl(tlaFQN(t))))));

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SOME_PRE_TRANSITION),
                        repeatedOr(mapBy(transitions, t -> new TlaAppl(preTransTlaFQN(t))))));

        StutterDefn(varNames, tlaModel, dashModel);

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SMALL_STEP),
                        new TlaOr(
                                new TlaDecl(SOME_TRANSITION),
                                new TlaAnd(
                                        new TlaDecl(STUTTER),
                                        new TlaNot(new TlaDecl(SOME_PRE_TRANSITION))))));
    }

    public static void StutterDefn(List<String> varNames, TlaModel tlaModel, DashModel dashModel) {

        List<String> unchangedVars =
                filterBy(Arrays.asList(CONF, STABLE, SCOPES_USED), v -> varNames.contains(v));

        // UNCHANGED <<_conf,_stable,_scope_used>>
        TlaExp unchangedExp = new TlaUnchanged(mapBy(unchangedVars, v -> new TlaVar(v)));

        // _trans_taken' = _none_transition
        TlaExp transTakenExp =
                new TlaEquals(new TlaPrime(new TlaVar(TRANS_TAKEN)), new TlaAppl(NONE_TRANSITION));

        List<TlaExp> expressions = new ArrayList<>();
        if (varNames.contains(TRANS_TAKEN)) expressions.add(transTakenExp);
        if (unchangedVars.size() != 0) expressions.add(unchangedExp);

        tlaModel.addDefn(new TlaDefn(new TlaDecl(STUTTER), repeatedAnd(expressions)));
    }
}
