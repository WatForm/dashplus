package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TlaSet;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StateDefinitions {
    // this class adds in formulae for every state in the dash model
    // leaf states are singleton sets that contain the fully qualified names as strings
    // non-leaf states are the union of the leaf states they contain
    public static void stateFormulae(DashModel dashModel, TlaModel tlaPlusModel) {

        List<String> names = dashModel.st.getAllNames();

        depthSort(names); // sorts it based on depth, thus all ancestors lie to the right and all
        // descendants lie to the left, for every state

        for (String s : names.reversed()) makeStateFormula(s, dashModel, tlaPlusModel);
    }

    public static void depthSort(List<String> stateNames) {
        Collections.sort(
                stateNames,
                (a, b) ->
                        GeneralUtil.occurrences(a, TranslationStrings.QUALIFIER)
                                - GeneralUtil.occurrences(b, TranslationStrings.QUALIFIER));
    }

    public static void makeStateFormula(String s, DashModel dashModel, TlaModel tlaPlusModel) {
        if (dashModel.st.isLeaf(s)) makeLeafStateFormula(s, tlaPlusModel);
        else makeNonLeafStateFormula(s, dashModel, tlaPlusModel);
    }

    public static void makeLeafStateFormula(String s, TlaModel tlaPlusModel) {

        // <state-formula-name> == {"<state FQN"}
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.getStateFormulaName(s)),
                        new TlaSet(Arrays.asList(new TlaLiteral(s)))));
    }

    public static void makeNonLeafStateFormula(
            String s, DashModel dashModel, TlaModel tlaPlusModel) {

        // <state-formula-name> = <child1-formula-name> union <child2-formula-name> ...
        
        List<TlaFormulaAppl> childStateFormulae =
                GeneralUtil.mapBy(
                        AuxiliaryDashAccessors.getChildStateNames(s, dashModel),
                        x -> new TlaFormulaAppl(TranslationStrings.getStateFormulaName(x)));

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.getStateFormulaName(s)),
                        TranslationStrings.repeatedUnion(childStateFormulae)));
    }
}
