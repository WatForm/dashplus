package ca.uwaterloo.watform.dashtotlaplus;

import static ca.uwaterloo.watform.dashtotlaplus.TranslationStrings.*;

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
    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        List<String> names = dashModel.st.getAllNames();

        depthSort(names); // sorts it based on depth, thus all ancestors lie to the left and all
        // descendants lie to the right, for every state

        names.reversed()
                .forEach(
                        x -> {
                            if (dashModel.st.isLeaf(x)) LeafStateDefinition(x, tlaModel);
                            else nonLeafStateDefinition(x, dashModel, tlaModel);
                        });
    }

    public static void depthSort(List<String> stateFQNs) {
        Collections.sort(
                stateFQNs,
                (a, b) ->
                        GeneralUtil.occurrences(a, QUALIFIER)
                                - GeneralUtil.occurrences(b, QUALIFIER));
    }

    public static void LeafStateDefinition(String stateFQN, TlaModel tlaModel) {

        // <state-formula-name> == {"<state FQN>"}
        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(getStateFormulaName(stateFQN)),
                        new TlaSet(Arrays.asList(new TlaLiteral(stateFQN)))));
    }

    public static void nonLeafStateDefinition(
            String stateFQN, DashModel dashModel, TlaModel tlaModel) {

        // <state-formula-name> = <child1-formula-name> union <child2-formula-name> ...

        List<TlaFormulaAppl> childStateFormulae =
                GeneralUtil.mapBy(
                        AuxiliaryDashAccessors.getChildStateNames(stateFQN, dashModel),
                        x -> new TlaFormulaAppl(getStateFormulaName(x)));

        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(getStateFormulaName(stateFQN)),
                        repeatedUnion(childStateFormulae)));
    }
}
