package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVar;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusAnd;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusIntersectionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusNotEq;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TransitionDefinitions {
    public static void transitionFormulae(DashModel dashModel, TLAPlusModel tlaPlusModel) {

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

		// taken_<trans-name> == "taken_<trans-fully-qualified-name"
        for (String s : transitions) makeTransitionTakenNameFormulae(s, tlaPlusModel);

		// pre, post, enabled and body
        for (String s : transitions) addTransitionCompleteFormula(s, dashModel, tlaPlusModel);

		// small-step and isEnabled
        addTransitionGeneralFormulae(dashModel, tlaPlusModel);
    }

    public static void makeTransitionTakenNameFormulae(
            String transitionFullyQualifiedName, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(
                                TranslationStrings.getTakenTransFormulaName(transitionFullyQualifiedName)),
                        new TLAPlusStringLiteral(transitionFullyQualifiedName)));
    }

    public static List<TLAPlusVar> enabledArgList() {
        return Arrays.asList(
                new TLAPlusVar(TranslationStrings.getArg(TranslationStrings.CONF)),
                new TLAPlusVar(TranslationStrings.getArg(TranslationStrings.SCOPE_USED)));
    }

    public static void addTransitionGeneralFormulae(
            DashModel dashModel, TLAPlusModel tlaPlusModel) {

        tlaPlusModel.addBlankLine();
        tlaPlusModel.addComment("general formulae for transitions");

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(TranslationStrings.SOME_TRANSITION),
                        TranslationStrings.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TLAPlusFormulaAppl(
                                                        TranslationStrings.getTransFormulaName(t))))));

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(TranslationStrings.NEXT_IS_STABLE, enabledArgList()),
                        TranslationStrings.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TLAPlusFormulaAppl(
                                                        TranslationStrings.getTransFormulaName(t),
                                                        GeneralUtil.mapBy(
                                                                enabledArgList(), u -> u))))));
    }

    public static void addTransitionPreFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TLAPlusModel tlaPlusModel) {

        String sourceStateFullQualifiedName =
                "standin"; // AuxiliaryDashAccessors.getSourceOfTrans(transitionFullyQualifiedName,
        // dashModel);  this doesn't work for whatever reason
        TLAPlusExp conf_exp =
                new TLAPlusNotEq(
                        new TLAPlusIntersectionSet(
                                TranslationStrings.getConf(),
                                new TLAPlusFormulaAppl(
                                        TranslationStrings.getStateFormulaName(sourceStateFullQualifiedName))),
                        TranslationStrings.getNullSet());

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(
                                TranslationStrings.getPreTransFormulaName(transitionFullyQualifiedName)),
                        TranslationStrings.repeatedAnd(Arrays.asList(conf_exp))));
    }

    public static void addTransitionPostFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TLAPlusModel tlaPlusModel) {

        TLAPlusExp taken =
                new TLAPlusEquals(
                        TranslationStrings.getTransTaken(),
                        new TLAPlusFormulaAppl(
                                TranslationStrings.getTakenTransFormulaName(transitionFullyQualifiedName)));
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(
                                TranslationStrings.getPostTransFormulaName(transitionFullyQualifiedName)),
                        TranslationStrings.repeatedAnd(Arrays.asList(taken))));
    }

    public static void addTransitionIsEnabledFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(
                                TranslationStrings.getEnabledTransFormulaName(transitionFullyQualifiedName),
                                Arrays.asList(
                                        new TLAPlusVar(TranslationStrings.getArg(TranslationStrings.CONF)),
                                        new TLAPlusVar(TranslationStrings.getArg(TranslationStrings.SCOPE_USED)))),
                        TranslationStrings.repeatedAnd(Arrays.asList())));
    }

    public static void addTransitionCompleteFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TLAPlusModel tlaPlusModel) {

        tlaPlusModel.addBlankLine();
        tlaPlusModel.addComment("Translation of transition " + transitionFullyQualifiedName);
        addTransitionPreFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);
        addTransitionPostFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);
        addTransitionIsEnabledFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);

		// body = pre /\ post
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(
                                TranslationStrings.getTransFormulaName(transitionFullyQualifiedName)),
                        new TLAPlusAnd(
                                new TLAPlusFormulaAppl(
                                        TranslationStrings.getPreTransFormulaName(
                                                transitionFullyQualifiedName)),
                                new TLAPlusFormulaAppl(
                                        TranslationStrings.getPostTransFormulaName(
                                                transitionFullyQualifiedName)))));
    }
}
