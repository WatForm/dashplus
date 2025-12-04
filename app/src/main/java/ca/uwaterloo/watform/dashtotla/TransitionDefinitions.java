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
import ca.uwaterloo.watform.tlaast.tlabinops.TlaOr;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaFalse;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaNot;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TransitionDefinitions {
    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        // taken_<trans-name> == "taken_<trans-fully-qualified-name"
        transitions.forEach(x -> makeTransitionTakenNameFormulae(x, tlaModel));

        // enabled
        tlaModel.addBlankLine();
        transitions.forEach(x -> addTransitionIsEnabledFormula(x, dashModel, tlaModel));

        tlaModel.addBlankLine();
        addNextIsStable(dashModel, tlaModel);

        // pre, post, and body
        transitions.forEach(x -> addTransitionCompleteFormula(x, dashModel, tlaModel));

        // small-step and isEnabled
        addTransitionGeneralFormulae(dashModel, tlaModel);
    }

    public static void makeTransitionTakenNameFormulae(String transitionFQN, TlaModel tlaModel) {
        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(TranslationStrings.getTakenTransFormulaName(transitionFQN)),
                        new TlaLiteral(transitionFQN)));
    }

    public static List<TlaVar> enabledArgList() {

        // this is subject to optimization, and is thus a separate function
        List<String> parameters = Arrays.asList(SCOPE_USED);
        return GeneralUtil.mapBy(parameters, v -> new TlaVar(parameterVariable(v)));
    }

    public static void addNextIsStable(DashModel dashModel, TlaModel tlaModel) {

        // _next_is_stable(args) = \/ enabled_after_step_ti(args) ...
        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);
        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(NEXT_IS_STABLE, enabledArgList()),
                        TranslationStrings.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TlaAppl(
                                                        TranslationStrings.getTransFormulaName(t),
                                                        GeneralUtil.mapBy(
                                                                enabledArgList(), u -> u))))));
    }

    public static void addTransitionGeneralFormulae(DashModel dashModel, TlaModel tlaModel) {

        tlaModel.addBlankLine();
        tlaModel.addComment("general formulae for transitions");

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(SOME_TRANSITION),
                        TranslationStrings.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TlaAppl(
                                                        TranslationStrings.getTransFormulaName(
                                                                t))))));

        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(SOME_PRE_TRANSITION),
                        repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions, t -> new TlaAppl(getTransFormulaName(t))))));

        tlaModel.addFormulaDefinition(new TlaDefn(new TlaDecl(STUTTER), new TlaFalse()));

        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(SMALL_STEP),
                        new TlaOr(
                                new TlaDecl(SOME_TRANSITION),
                                new TlaAnd(
                                        new TlaDecl(STUTTER),
                                        new TlaNot(new TlaDecl(SOME_PRE_TRANSITION))))));
    }

    public static void addTransitionPreFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {

        String sourceStateFullQualifiedName =
                "standin"; // AuxiliaryDashAccessors.getSourceOfTrans(transitionFQN,
        // dashModel);  this doesn't work for whatever reason
        TlaExp conf_exp =
                new TlaNotEq(
                        new TlaIntersectionSet(
                                new TlaVar(CONF),
                                new TlaAppl(
                                        TranslationStrings.getStateFormulaName(
                                                sourceStateFullQualifiedName))),
                        TranslationStrings.NULL_SET);

        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(TranslationStrings.getPreTransFormulaName(transitionFQN)),
                        TranslationStrings.repeatedAnd(Arrays.asList(conf_exp))));
    }

    public static void addTransitionPostFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {

        TlaExp taken =
                new TlaEquals(
                        new TlaVar(TRANS_TAKEN),
                        new TlaAppl(TranslationStrings.getTakenTransFormulaName(transitionFQN)));
        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(TranslationStrings.getPostTransFormulaName(transitionFQN)),
                        TranslationStrings.repeatedAnd(Arrays.asList(taken))));
    }

    public static void addTransitionIsEnabledFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(
                                TranslationStrings.getEnabledTransFormulaName(transitionFQN),
                                enabledArgList()),
                        TranslationStrings.repeatedAnd(Arrays.asList())));
    }

    public static void addTransitionCompleteFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {

        tlaModel.addBlankLine();
        tlaModel.addComment("Translation of transition " + transitionFQN);

        addTransitionPreFormula(transitionFQN, dashModel, tlaModel);
        addTransitionPostFormula(transitionFQN, dashModel, tlaModel);

        // body = pre /\ post
        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(TranslationStrings.getTransFormulaName(transitionFQN)),
                        new TlaAnd(
                                new TlaAppl(
                                        TranslationStrings.getPreTransFormulaName(transitionFQN)),
                                new TlaAppl(
                                        TranslationStrings.getPostTransFormulaName(
                                                transitionFQN)))));
    }
}
