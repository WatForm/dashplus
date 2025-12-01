package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDeclaration;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefinition;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusAnd;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusIntersectionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusNotEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class Transitions {
    public static void transitionFormulae(DashModel dashModel, TLAPlusModel tlaPlusModel) {

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        for (String s : transitions) makeTransitionTakenNameFormulae(s, tlaPlusModel);

        for (String s : transitions) addTransitionCompleteFormula(s, dashModel, tlaPlusModel);

        addTransitionGeneralFormulae(dashModel, tlaPlusModel);
    }

    public static void makeTransitionTakenNameFormulae(
            String transitionFullyQualifiedName, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(
                                Common.getTakenTransFormulaName(transitionFullyQualifiedName)),
                        new TLAPlusStringLiteral(transitionFullyQualifiedName)));
    }

    public static List<TLAPlusVariable> enabledArgList() {
        return Arrays.asList(
                new TLAPlusVariable(Common.getArg(Common.CONF)),
                new TLAPlusVariable(Common.getArg(Common.SCOPE_USED)));
    }

    public static void addTransitionGeneralFormulae(
            DashModel dashModel, TLAPlusModel tlaPlusModel) {

        tlaPlusModel.addBlankLine();
        tlaPlusModel.addComment("general formulae for transitions");

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.SOME_TRANSITION),
                        Common.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TLAPlusFormulaApplication(
                                                        Common.getTransFormulaName(t))))));

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.NEXT_IS_STABLE, enabledArgList()),
                        Common.repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions,
                                        t ->
                                                new TLAPlusFormulaApplication(
                                                        Common.getTransFormulaName(t),
                                                        GeneralUtil.mapBy(
                                                                enabledArgList(), u -> u))))));
    }

    public static void addTransitionPreFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TLAPlusModel tlaPlusModel) {

        String sourceStateFullQualifiedName =
                "standin"; // AuxiliaryDashAccessors.getSourceOfTrans(transitionFullyQualifiedName,
        // dashModel);  this doesn't work for whatever reason
        TLAPlusExpression conf_exp =
                new TLAPlusNotEquals(
                        new TLAPlusIntersectionSet(
                                Common.getConf(),
                                new TLAPlusFormulaApplication(
                                        Common.getStateFormulaName(sourceStateFullQualifiedName))),
                        Common.getNullSet());

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(
                                Common.getPreTransFormulaName(transitionFullyQualifiedName)),
                        Common.repeatedAnd(Arrays.asList(conf_exp))));
    }

    public static void addTransitionPostFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TLAPlusModel tlaPlusModel) {

        TLAPlusExpression taken =
                new TLAPlusEquals(
                        Common.getTransTaken(),
                        new TLAPlusFormulaApplication(
                                Common.getTakenTransFormulaName(transitionFullyQualifiedName)));
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(
                                Common.getPostTransFormulaName(transitionFullyQualifiedName)),
                        Common.repeatedAnd(Arrays.asList(taken))));
    }

    public static void addTransitionIsEnabledFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(
                                Common.getEnabledTransFormulaName(transitionFullyQualifiedName),
                                Arrays.asList(
                                        new TLAPlusVariable(Common.getArg(Common.CONF)),
                                        new TLAPlusVariable(Common.getArg(Common.SCOPE_USED)))),
                        Common.repeatedAnd(Arrays.asList())));
    }

    public static void addTransitionCompleteFormula(
            String transitionFullyQualifiedName, DashModel dashModel, TLAPlusModel tlaPlusModel) {

        tlaPlusModel.addBlankLine();
        tlaPlusModel.addComment("Translation of transition " + transitionFullyQualifiedName);
        addTransitionPreFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);
        addTransitionPostFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);
        addTransitionIsEnabledFormula(transitionFullyQualifiedName, dashModel, tlaPlusModel);

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(
                                Common.getTransFormulaName(transitionFullyQualifiedName)),
                        new TLAPlusAnd(
                                new TLAPlusFormulaApplication(
                                        Common.getPreTransFormulaName(
                                                transitionFullyQualifiedName)),
                                new TLAPlusFormulaApplication(
                                        Common.getPostTransFormulaName(
                                                transitionFullyQualifiedName)))));
    }
}
