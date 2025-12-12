package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmallStepDefn {
    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxDashAccessors.getTransitionNames(dashModel);

        tlaModel.addDefn(
                // _some_transition == \/ <ti> ...
                TlaDefn(SOME_TRANSITION, repeatedOr(mapBy(transitions, t -> TlaAppl(tlaFQN(t))))));

        tlaModel.addDefn(
                // _some_pre_transition == \/ <pre_ti> ...
                TlaDefn(
                        SOME_PRE_TRANSITION,
                        repeatedOr(mapBy(transitions, t -> TlaAppl(preTransTlaFQN(t))))));

        StutterDefn(varNames, tlaModel, dashModel);

        tlaModel.addDefn(
                // _small_step == _some_transition \/ (_stutter /\ ~_some_pre_transition)
                TlaDefn(
                        SMALL_STEP,
                        SOME_TRANSITION().OR(STUTTER().AND(TlaNot(SOME_PRE_TRANSITION())))));

        /* this is what it would look like without helper functions
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SMALL_STEP),
                        new TlaOr(
                                new TlaAppl(SOME_TRANSITION),
                                new TlaAnd(
                                        new TlaAppl(STUTTER),
                                        new TlaNot(new TlaAppl(SOME_PRE_TRANSITION))))));
        */
    }

    public static void StutterDefn(List<String> varNames, TlaModel tlaModel, DashModel dashModel) {
        List<TlaExp> expressions = new ArrayList<>();

        if (varNames.contains(TRANS_TAKEN))
            expressions.add(
                    // _trans_taken' = _none_transition
                    TRANS_TAKEN().PRIME().EQUALS(NONE_TRANSITION()));

        List<String> unchangedVars =
                filterBy(Arrays.asList(CONF, STABLE, SCOPES_USED), v -> varNames.contains(v));

        if (unchangedVars.size() != 0)
            expressions.add(
                    // UNCHANGED <<_conf,_stable,_scope_used>>
                    TlaUnchanged(mapBy(unchangedVars, v -> TlaVar(v))));

        tlaModel.addDefn(TlaDefn(STUTTER, repeatedAnd(expressions)));
    }
}
