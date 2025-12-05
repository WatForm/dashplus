package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.TranslationStrings.*;

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
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TransitionDefinitions {
    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        // taken_<trans-name> == "taken_<transFQN>"
        transitions.forEach(x -> addTransitionTakenFormulae(x, tlaModel));

        // _enabled_<transFQN> == <body>
        tlaModel.addComment("parameterized formulae to check if transitions are enabled");
        transitions.forEach(x -> addTransitionIsEnabledFormula(x, dashModel, tlaModel));

        tlaModel.addComment("negation of disjunction of enabled-formulae");
        addNextIsStable(dashModel, tlaModel);

        // pre, post, and body
        transitions.forEach(
                transFQN -> {
                    tlaModel.addComment("Translation of transition " + transFQN);
                    addTransitionCompleteFormula(transFQN, dashModel, tlaModel);
                });
    }

    public static void addTransitionTakenFormulae(String transitionFQN, TlaModel tlaModel) {
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(TakenTransFormulaName(transitionFQN)),
                        new TlaLiteral(transitionFQN)));
    }

    public static List<TlaVar> isEnabledParams() {

        // this is subject to optimization, and is thus a separate function
        List<String> parameters = Arrays.asList(SCOPES_USED);
        return GeneralUtil.mapBy(parameters, v -> new TlaVar(parameterVariable(v)));
    }

    public static void addNextIsStable(DashModel dashModel, TlaModel tlaModel) {

        // _next_is_stable(args) = \/ enabled_after_step_ti(args) ...
        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(NEXT_IS_STABLE, isEnabledParams()),
                        new TlaNot(
                                repeatedOr(
                                        GeneralUtil.mapBy(
                                                transitions,
                                                t ->
                                                        new TlaAppl(
                                                                EnabledTransFormulaName(t),
                                                                isEnabledParams()))))));
    }

    public static void addTransitionPreFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {

        String sourceStateFQN = "standin"; // AuxiliaryDashAccessors.getSourceOfTrans(transitionFQN,
        // dashModel);  this doesn't work for whatever reason
        TlaExp conf_exp =
                new TlaNotEq(
                        new TlaIntersectionSet(
                                new TlaVar(CONF), new TlaAppl(tlaFQN(sourceStateFQN))),
                        NULL_SET);

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(PreTransFormulaName(transitionFQN)),
                        repeatedAnd(Arrays.asList(conf_exp))));

        // TODO add stuff
    }

    public static void addTransitionPostFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {

        TlaExp taken =
                new TlaEquals(
                        new TlaVar(TRANS_TAKEN), new TlaAppl(TakenTransFormulaName(transitionFQN)));
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(PostTransFormulaName(transitionFQN)),
                        repeatedAnd(Arrays.asList(taken))));

        // TODO add stuff
    }

    public static void addTransitionIsEnabledFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(EnabledTransFormulaName(transitionFQN), isEnabledParams()),
                        repeatedAnd(Arrays.asList())));
    }

    public static void addTransitionCompleteFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {

        addTransitionPreFormula(transitionFQN, dashModel, tlaModel);
        addTransitionPostFormula(transitionFQN, dashModel, tlaModel);

        // body = pre /\ post
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(tlaFQN(transitionFQN)),
                        new TlaAnd(
                                new TlaAppl(PreTransFormulaName(transitionFQN)),
                                new TlaAppl(PostTransFormulaName(transitionFQN)))));
    }
}
