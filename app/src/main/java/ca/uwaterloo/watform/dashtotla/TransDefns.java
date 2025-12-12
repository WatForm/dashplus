package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransDefns {
    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxDashAccessors.getTransitionNames(dashModel);

        if (varNames.contains(TRANS_TAKEN)) {

            transitions.forEach(
                    transFQN ->
                            tlaModel.addDefn(
                                    // taken_<trans-name> == "taken_<transFQN>"
                                    TlaDefn(
                                            takenTransTlaFQN(transFQN),
                                            TlaStringLiteral(transFQN))));

            tlaModel.addDefn(
                    // _none_transition == "[none]"
                    TlaDefn(NONE_TRANSITION, NONE_TRANSITION_LITERAL()));
        }

        if (varNames.contains(STABLE) && varNames.contains(SCOPES_USED)) {

            tlaModel.addComment("parameterized formulae to check if transitions are enabled");
            // _enabled_<transFQN> == <body>
            transitions.forEach(x -> TransIsEnabledDefn(x, dashModel, tlaModel));

            tlaModel.addComment("negation of disjunction of enabled-formulae");
            NextIsStableDefn(dashModel, tlaModel);
        }

        // pre, post, and body
        transitions.forEach(
                transFQN -> {
                    tlaModel.addComment("Translation of transition " + transFQN);
                    TransDefn(transFQN, varNames, dashModel, tlaModel);
                });
    }

    public static List<TlaVar> isEnabledParams() {

        // this is subject to optimization, and is thus a separate function
        List<String> parameters = Arrays.asList(SCOPES_USED);
        return mapBy(parameters, v -> TlaVar(paramVar(v)));
    }

    public static void NextIsStableDefn(DashModel dashModel, TlaModel tlaModel) {

        // _next_is_stable(args) = \/ enabled_after_step_ti(args) ...
        List<String> transitions = AuxDashAccessors.getTransitionNames(dashModel);
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(NEXT_IS_STABLE, isEnabledParams()),
                        TlaNot(
                                repeatedOr(
                                        mapBy(
                                                transitions,
                                                t ->
                                                        TlaAppl(
                                                                enabledTransTlaFQN(t),
                                                                isEnabledParams()))))));
    }

    public static void PreTransDefn(
            String transFQN, List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        TlaExp confExp = TlaTrue();

        List<TlaExp> expressions = new ArrayList<>();
        if (varNames.contains(CONF)) expressions.add(confExp);

        tlaModel.addDefn(TlaDefn(preTransTlaFQN(transFQN), repeatedAnd(expressions)));

        // TODO add stuff
    }

    public static void PostTransDefn(
            String transFQN, List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        TlaExp confExp = TlaUnchanged(Arrays.asList(TlaVar(CONF)));

        TlaExp scopesUsedExp = TlaUnchanged(Arrays.asList(TlaVar(SCOPES_USED)));

        TlaExp stableExp = TlaUnchanged(Arrays.asList(TlaVar(STABLE)));

        List<TlaExp> expressions = new ArrayList<>();

        if (varNames.contains(TRANS_TAKEN))
            expressions.add(
                    // _trans_taken' = <taken-trans-formula>
                    TRANS_TAKEN().PRIME().EQUALS(TlaAppl(takenTransTlaFQN(transFQN))));

        if (varNames.contains(CONF)) expressions.add(confExp);
        if (varNames.contains(SCOPES_USED)) expressions.add(scopesUsedExp);
        if (varNames.contains(STABLE)) expressions.add(stableExp);

        tlaModel.addDefn(TlaDefn(postTransTlaFQN(transFQN), repeatedAnd(expressions)));

        // TODO add stuff
    }

    public static void TransIsEnabledDefn(String transFQN, DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(enabledTransTlaFQN(transFQN), isEnabledParams()),
                        repeatedAnd(Arrays.asList())));
    }

    public static void TransDefn(
            String transFQN, List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        PreTransDefn(transFQN, varNames, dashModel, tlaModel);
        PostTransDefn(transFQN, varNames, dashModel, tlaModel);

        // body = pre /\ post
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(tlaFQN(transFQN)),
                        TlaAnd(
                                TlaAppl(preTransTlaFQN(transFQN)),
                                TlaAppl(postTransTlaFQN(transFQN)))));
    }
}
