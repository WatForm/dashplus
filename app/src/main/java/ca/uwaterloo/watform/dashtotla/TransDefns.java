package ca.uwaterloo.watform.dashtotla;

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

        // taken_<trans-name> == "taken_<transFQN>"
        if (varNames.contains(TRANS_TAKEN)) {
            transitions.forEach(x -> TransTakenDefn(x, tlaModel));

            // _none_transition = "[none]"
            tlaModel.addDefn(
                    TlaDefn(TlaDecl(NONE_TRANSITION), TlaStringLiteral(NONE_TRANSITION_LITERAL)));
        }

        if (varNames.contains(STABLE) && varNames.contains(SCOPES_USED)) {
            // _enabled_<transFQN> == <body>
            tlaModel.addComment("parameterized formulae to check if transitions are enabled");
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

    public static void TransTakenDefn(String transFQN, TlaModel tlaModel) {
        tlaModel.addDefn(TlaDefn(TlaDecl(takenTransTlaFQN(transFQN)), TlaStringLiteral(transFQN)));
    }

    public static List<TlaVar> isEnabledParams() {

        // this is subject to optimization, and is thus a separate function
        List<String> parameters = Arrays.asList(SCOPES_USED);
        return mapBy(parameters, v -> TlaVar(parameterVariable(v)));
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

        // String sourceStateFQN = "stand-in"; // AuxiliaryDashAccessors.getSourceOfTrans(transFQN,
        // dashModel);  this doesn't work for whatever reason
        // TlaExp conf_exp =
        //          TlaNotEq(
        //                  TlaIntersectionSet(
        //                          TlaVar(CONF),  TlaAppl(tlaFQN(sourceStateFQN))),
        //                 NULL_SET);

        TlaExp confExp = TlaTrue();

        List<TlaExp> expressions = new ArrayList<>();
        if (varNames.contains(CONF)) expressions.add(confExp);

        tlaModel.addDefn(TlaDefn(TlaDecl(preTransTlaFQN(transFQN)), repeatedAnd(expressions)));

        // TODO add stuff
    }

    public static void PostTransDefn(
            String transFQN, List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        // _trans_taken' = <taken-trans-formula>
        TlaExp transTakenExp =
                TlaEquals(TlaPrime(TlaVar(TRANS_TAKEN)), TlaAppl(takenTransTlaFQN(transFQN)));

        TlaExp confExp = TlaUnchanged(Arrays.asList(TlaVar(CONF)));

        TlaExp scopesUsedExp = TlaUnchanged(Arrays.asList(TlaVar(SCOPES_USED)));

        TlaExp stableExp = TlaUnchanged(Arrays.asList(TlaVar(STABLE)));

        List<TlaExp> expressions = new ArrayList<>();
        if (varNames.contains(TRANS_TAKEN)) expressions.add(transTakenExp);
        if (varNames.contains(CONF)) expressions.add(confExp);
        if (varNames.contains(SCOPES_USED)) expressions.add(scopesUsedExp);
        if (varNames.contains(STABLE)) expressions.add(stableExp);

        tlaModel.addDefn(TlaDefn(TlaDecl(postTransTlaFQN(transFQN)), repeatedAnd(expressions)));

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
