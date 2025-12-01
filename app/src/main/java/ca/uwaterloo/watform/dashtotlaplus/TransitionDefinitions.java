package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.TlaVar;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusAnd;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusIntersectionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusNotEq;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TransitionDefinitions {
    public static void transitionFormulae(DashModel dashModel, TlaModel tlaPlusModel) {

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

		// taken_<trans-name> == "taken_<trans-fully-qualified-name"
        for (String s : transitions) makeTransitionTakenNameFormulae(s, tlaPlusModel);

		// pre, post, enabled and body
        for (String s : transitions) addTransitionCompleteFormula(s, dashModel, tlaPlusModel);

		// small-step and isEnabled
        addTransitionGeneralFormulae(dashModel, tlaPlusModel);
    }

    public static void makeTransitionTakenNameFormulae(
            String transitionFullyQualifiedName, TlaModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getTakenTransFormulaName(transitionFullyQualifiedName)),
                        new TLAPlusStringLiteral(transitionFullyQualifiedName)));
    }

    public static List<TlaVar> enabledArgList() {
        return Arrays.asList(
                new TlaVar(TranslationStrings.getArg(TranslationStrings.CONF)),
                new TlaVar(TranslationStrings.getArg(TranslationStrings.SCOPE_USED)));
    }

    public static void addTransitionGeneralFormulae(
            DashModel dashModel, TlaModel tlaPlusModel) {

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
                                                        TranslationStrings.getTransFormulaName(t))))));

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
            String transitionFullyQualifiedName, DashModel dashModel, TlaModel tlaPlusModel) {

        String sourceStateFullQualifiedName =
                "standin"; // AuxiliaryDashAccessors.getSourceOfTrans(transitionFullyQualifiedName,
        // dashModel);  this doesn't work for whatever reason
        TlaExp conf_exp =
                new TLAPlusNotEq(
                        new TLAPlusIntersectionSet(
                                TranslationStrings.getConf(),
                                new TlaFormulaAppl(
                                        TranslationStrings.getStateFormulaName(sourceStateFullQualifiedName))),
                        TranslationStrings.getNullSet());

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getPreTransFormulaName(transitionFullyQualifiedName)),
                        TranslationStrings.repeatedAnd(Arrays.asList(conf_exp))));
    }

    public static void addTransitionPostFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TlaModel tlaPlusModel) {

        TlaExp taken =
                new TLAPlusEquals(
                        TranslationStrings.getTransTaken(),
                        new TlaFormulaAppl(
                                TranslationStrings.getTakenTransFormulaName(transitionFullyQualifiedName)));
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getPostTransFormulaName(transitionFullyQualifiedName)),
                        TranslationStrings.repeatedAnd(Arrays.asList(taken))));
    }

    public static void addTransitionIsEnabledFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TlaModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getEnabledTransFormulaName(transitionFullyQualifiedName),
                                Arrays.asList(
                                        new TlaVar(TranslationStrings.getArg(TranslationStrings.CONF)),
                                        new TlaVar(TranslationStrings.getArg(TranslationStrings.SCOPE_USED)))),
                        TranslationStrings.repeatedAnd(Arrays.asList())));
    }

    public static void addTransitionCompleteFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TlaModel tlaPlusModel) {

        tlaPlusModel.addBlankLine();
        tlaPlusModel.addComment("Translation of transition " + transitionFullyQualifiedName);
        addTransitionPreFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);
        addTransitionPostFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);
        addTransitionIsEnabledFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);

		// body = pre /\ post
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(
                                TranslationStrings.getTransFormulaName(transitionFullyQualifiedName)),
                        new TLAPlusAnd(
                                new TlaFormulaAppl(
                                        TranslationStrings.getPreTransFormulaName(
                                                transitionFullyQualifiedName)),
                                new TlaFormulaAppl(
                                        TranslationStrings.getPostTransFormulaName(
                                                transitionFullyQualifiedName)))));
    }
}
