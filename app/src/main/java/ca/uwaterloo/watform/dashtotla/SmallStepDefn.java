package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaSubsetUnary;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class SmallStepDefn {
    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        List<String> transFQNs = dashModel.allTransNames();
        List<TlaAppl> preTransitions = mapBy(transFQNs, tFQN -> TlaAppl(preTransTlaFQN(tFQN)));
        List<TlaAppl> transitions = mapBy(transFQNs, tFQN -> TlaAppl(tlaFQN(tFQN)));

        tlaModel.addDefn(
                // _some_transition == \/ <ti> ...
                TlaDefn(SOME_TRANSITION, repeatedOr(transitions)));

        tlaModel.addDefn(
                // _some_pre_transition == \/ <pre_ti> ...
                TlaDefn(SOME_PRE_TRANSITION, repeatedOr(preTransitions)));

        StutterDefn(tlaModel, dashModel);

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

    public static void StutterDefn(TlaModel tlaModel, DashModel dashModel) {
        List<TlaExp> expressions = new ArrayList<>();

        expressions.add(
                // _trans_taken' = _none_transition
                TRANS_TAKEN().PRIME().EQUALS(NONE_TRANSITION()));

        if (dashModel.hasEvents())
            expressions.add(
                    // _events' \in SUBSET _all_internal_events
                    EVENTS().PRIME().IN(new TlaSubsetUnary(INTERNAL_EVENTS())));

        List<TlaVar> unchangedVars = new ArrayList<>();
        if (dashModel.hasConcurrency()) {
            unchangedVars.add(TlaVar(STABLE));
            unchangedVars.add(TlaVar(SCOPES_USED));
        }
        if (!dashModel.hasOnlyOneState()) unchangedVars.add(TlaVar(CONF));

        if (unchangedVars.size() != 0)
            expressions.add(
                    // UNCHANGED <<_conf,_stable,_scope_used>>
                    TlaUnchanged(unchangedVars));

        tlaModel.addDefn(TlaDefn(STUTTER, repeatedAnd(expressions)));
    }
}
