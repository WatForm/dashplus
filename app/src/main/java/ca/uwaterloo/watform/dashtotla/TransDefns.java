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
import ca.uwaterloo.watform.tlaast.tlabinops.TlaIntersectionSet;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaNotEq;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaNot;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransDefns {
    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxDashAccessors.getTransitionNames(dashModel);

        // taken_<trans-name> == "taken_<transFQN>"
        if (varNames.contains(TRANS_TAKEN)) transitions.forEach(x -> TransTakenDefn(x, tlaModel));

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
        tlaModel.addDefn(
                new TlaDefn(new TlaDecl(takenTransTlaFQN(transFQN)), new TlaLiteral(transFQN)));
    }

    public static List<TlaVar> isEnabledParams() {

        // this is subject to optimization, and is thus a separate function
        List<String> parameters = Arrays.asList(SCOPES_USED);
        return mapBy(parameters, v -> new TlaVar(parameterVariable(v)));
    }

    public static void NextIsStableDefn(DashModel dashModel, TlaModel tlaModel) {

        // _next_is_stable(args) = \/ enabled_after_step_ti(args) ...
        List<String> transitions = AuxDashAccessors.getTransitionNames(dashModel);
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(NEXT_IS_STABLE, isEnabledParams()),
                        new TlaNot(
                                repeatedOr(
                                        mapBy(
                                                transitions,
                                                t ->
                                                        new TlaAppl(
                                                                enabledTransTlaFQN(t),
                                                                isEnabledParams()))))));
    }

    public static void PreTransDefn(
            String transFQN, List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        String sourceStateFQN = "stand-in"; // AuxiliaryDashAccessors.getSourceOfTrans(transFQN,
        // dashModel);  this doesn't work for whatever reason
        TlaExp conf_exp =
                new TlaNotEq(
                        new TlaIntersectionSet(
                                new TlaVar(CONF), new TlaAppl(tlaFQN(sourceStateFQN))),
                        NULL_SET);

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(preTransTlaFQN(transFQN)),
                        repeatedAnd(Arrays.asList(conf_exp))));

        // TODO add stuff
    }

    public static void PostTransDefn(
            String transFQN, List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        TlaExp taken =
                new TlaEquals(new TlaVar(TRANS_TAKEN), new TlaAppl(takenTransTlaFQN(transFQN)));

        List<TlaExp> expressions = new ArrayList<>();
        expressions.add(taken);

        tlaModel.addDefn(
                new TlaDefn(new TlaDecl(postTransTlaFQN(transFQN)), repeatedAnd(expressions)));

        // TODO add stuff
    }

    public static void TransIsEnabledDefn(String transFQN, DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(enabledTransTlaFQN(transFQN), isEnabledParams()),
                        repeatedAnd(Arrays.asList())));
    }

    public static void TransDefn(
            String transFQN, List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        PreTransDefn(transFQN, varNames, dashModel, tlaModel);
        PostTransDefn(transFQN, varNames, dashModel, tlaModel);

        // body = pre /\ post
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(tlaFQN(transFQN)),
                        new TlaAnd(
                                new TlaAppl(preTransTlaFQN(transFQN)),
                                new TlaAppl(postTransTlaFQN(transFQN)))));
    }
}
