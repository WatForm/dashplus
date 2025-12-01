package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.TlaVar;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.Tla;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaIntersectionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaNotEq;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TransitionDefinitions {
    public static void transitionFormulae(DashModel dashModel, TlaModel tlaPlusModel) {

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        // taken_<trans-name> == "taken_<trans-fully-qualified-name"
        transitions.forEach(x -> makeTransitionTakenNameFormulae(x, tlaPlusModel));

        // pre, post, enabled and body
        transitions.forEach(x -> addTransitionCompleteFormula(x, dashModel, tlaPlusModel));

        // small-step and isEnabled
        addTransitionGeneralFormulae(dashModel, tlaPlusModel);
    }

    public static void makeTransitionTakenNameFormulae(
            String transitionFQN, TlaModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getTakenTransFormulaName(transitionFQN)),
                        new TlaLiteral(transitionFQN)));
    }

    public static List<TlaVar> enabledArgList() {
        return Arrays.asList(
                new TlaVar(TranslationStrings.getArg(TranslationStrings.CONF)),
                new TlaVar(TranslationStrings.getArg(TranslationStrings.SCOPE_USED)));
    }

    public static void addTransitionGeneralFormulae(DashModel dashModel, TlaModel tlaPlusModel) {

        tlaPlusModel.addBlankLine();
        tlaPlusModel.addComment("general formulae for transitions");

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.SOME_TRANSITION),
                        TranslationStrings.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TlaFormulaAppl(
                                                        TranslationStrings.getTransFormulaName(
                                                                t))))));

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.NEXT_IS_STABLE, enabledArgList()),
                        TranslationStrings.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TlaFormulaAppl(
                                                        TranslationStrings.getTransFormulaName(t),
                                                        GeneralUtil.mapBy(
                                                                enabledArgList(), u -> u))))));
    }

    public static void addTransitionPreFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaPlusModel) {

        String sourceStateFullQualifiedName =
                "standin"; // AuxiliaryDashAccessors.getSourceOfTrans(transitionFQN,
        // dashModel);  this doesn't work for whatever reason
        TlaExp conf_exp =
                new TlaNotEq(
                        new TlaIntersectionSet(
                                TranslationStrings.getConf(),
                                new TlaFormulaAppl(
                                        TranslationStrings.getStateFormulaName(
                                                sourceStateFullQualifiedName))),
                        TranslationStrings.getNullSet());

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getPreTransFormulaName(transitionFQN)),
                        TranslationStrings.repeatedAnd(Arrays.asList(conf_exp))));
    }

    public static void addTransitionPostFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaPlusModel) {

        TlaExp taken =
                new TlaEquals(
                        TranslationStrings.getTransTaken(),
                        new TlaFormulaAppl(
                                TranslationStrings.getTakenTransFormulaName(transitionFQN)));
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getPostTransFormulaName(transitionFQN)),
                        TranslationStrings.repeatedAnd(Arrays.asList(taken))));
    }

    public static void addTransitionIsEnabledFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getEnabledTransFormulaName(transitionFQN),
                                Arrays.asList(
                                        new TlaVar(
                                                TranslationStrings.getArg(TranslationStrings.CONF)),
                                        new TlaVar(
                                                TranslationStrings.getArg(
                                                        TranslationStrings.SCOPE_USED)))),
                        TranslationStrings.repeatedAnd(Arrays.asList())));
    }

    public static void addTransitionCompleteFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaPlusModel) {

        tlaPlusModel.addBlankLine();
        tlaPlusModel.addComment("Translation of transition " + transitionFQN);

        addTransitionPreFormula(transitionFQN, dashModel, tlaPlusModel);
        addTransitionPostFormula(transitionFQN, dashModel, tlaPlusModel);
        addTransitionIsEnabledFormula(transitionFQN, dashModel, tlaPlusModel);

        // body = pre /\ post
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.getTransFormulaName(transitionFQN)),
                        new Tla(
                                new TlaFormulaAppl(
                                        TranslationStrings.getPreTransFormulaName(transitionFQN)),
                                new TlaFormulaAppl(
                                        TranslationStrings.getPostTransFormulaName(
                                                transitionFQN)))));
    }
}
