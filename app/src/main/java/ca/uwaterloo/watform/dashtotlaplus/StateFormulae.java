package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDeclaration;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefinition;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StateFormulae {
    // this class adds in formulae for every state in the dash model
    // leaf states are singleton sets that contain the fully qualified names as strings
    // non-leaf states are the union of the leaf states they contain
    public static void stateFormulae(DashModel dashModel, TLAPlusModel tlaPlusModel) {

        List<String> names = dashModel.st.getAllNames();

        depthSort(names); // sorts it based on depth, thus all ancestors lie to the right and all
        // descendants lie to the left, for every state

        for (String s : names.reversed()) makeStateFormula(s, dashModel, tlaPlusModel);
    }

    public static void depthSort(List<String> stateNames) {
        Collections.sort(
                stateNames,
                (a, b) ->
                        GeneralUtil.occurrences(a, Common.QUALIFIER)
                                - GeneralUtil.occurrences(b, Common.QUALIFIER));
    }

    public static void makeStateFormula(String s, DashModel dashModel, TLAPlusModel tlaPlusModel) {
        if (dashModel.st.isLeaf(s)) makeLeafStateFormula(s, tlaPlusModel);
        else makeNonLeafStateFormula(s, dashModel, tlaPlusModel);
    }

    public static void makeLeafStateFormula(String s, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.module.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.getStateFormulaName(s)),
                        new TLAPlusSet(Arrays.asList(new TLAPlusStringLiteral(s)))));
    }

    public static void makeNonLeafStateFormula(
            String s, DashModel dashModel, TLAPlusModel tlaPlusModel) {

        List<TLAPlusFormulaApplication> childStateFormulae =
                GeneralUtil.mapBy(
                        AuxiliaryDashAccessors.getChildStateNames(s, dashModel),
                        x -> new TLAPlusFormulaApplication(Common.getStateFormulaName(x)));

        tlaPlusModel.module.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.getStateFormulaName(s)),
                        Common.repeatedUnion(childStateFormulae)));
    }
}
