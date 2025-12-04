package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.TlaVar;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaAnd;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaIntersectionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaNotEq;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TransitionDefinitions {
    public static void transitionFormulae(DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        // taken_<trans-name> == "taken_<trans-fully-qualified-name"
        transitions.forEach(x -> makeTransitionTakenNameFormulae(x, tlaModel));

        // pre, post, enabled and body
        transitions.forEach(x -> addTransitionCompleteFormula(x, dashModel, tlaModel));

        // small-step and isEnabled
        addTransitionGeneralFormulae(dashModel, tlaModel);
    }

    public static void makeTransitionTakenNameFormulae(String transitionFQN, TlaModel tlaModel) {
        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getTakenTransFormulaName(transitionFQN)),
                        new TlaLiteral(transitionFQN)));
    }

    public static List<TlaVar> enabledArgList() {
        return Arrays.asList(
                TranslationStrings.CONF.paramVar(), TranslationStrings.SCOPE_USED.paramVar());
    }

    public static void addTransitionGeneralFormulae(DashModel dashModel, TlaModel tlaModel) {

        tlaModel.addBlankLine();
        tlaModel.addComment("general formulae for transitions");

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.SOME_TRANSITION),
                        TranslationStrings.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TlaFormulaAppl(
                                                        TranslationStrings.getTransFormulaName(
                                                                t))))));

        tlaModel.addFormulaDefinition(
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
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {

        String sourceStateFullQualifiedName =
                "standin"; // AuxiliaryDashAccessors.getSourceOfTrans(transitionFQN,
        // dashModel);  this doesn't work for whatever reason
        TlaExp conf_exp =
                new TlaNotEq(
                        new TlaIntersectionSet(
                                TranslationStrings.CONF.globalVar(),
                                new TlaFormulaAppl(
                                        TranslationStrings.getStateFormulaName(
                                                sourceStateFullQualifiedName))),
                        TranslationStrings.NULL_SET);

        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getPreTransFormulaName(transitionFQN)),
                        TranslationStrings.repeatedAnd(Arrays.asList(conf_exp))));
    }

    public static void addTransitionPostFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {

        TlaExp taken =
                new TlaEquals(
                        TranslationStrings.TRANS_TAKEN.globalVar(),
                        new TlaFormulaAppl(
                                TranslationStrings.getTakenTransFormulaName(transitionFQN)));
        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getPostTransFormulaName(transitionFQN)),
                        TranslationStrings.repeatedAnd(Arrays.asList(taken))));
    }

    public static void addTransitionIsEnabledFormula(
            String transitionFQN, DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
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
        addTransitionIsEnabledFormula(transitionFQN, dashModel, tlaModel);

        // body = pre /\ post
        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.getTransFormulaName(transitionFQN)),
                        new TlaAnd(
                                new TlaFormulaAppl(
                                        TranslationStrings.getPreTransFormulaName(transitionFQN)),
                                new TlaFormulaAppl(
                                        TranslationStrings.getPostTransFormulaName(
                                                transitionFQN)))));
    }
}
