package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaast.tlaplusnaryops.TlaSet;
import ca.uwaterloo.watform.tlamodel.TlaModel;
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
                new TlaDefn(
                        new TlaDecl(getStateFormulaName(stateFQN)),
                        new TlaSet(Arrays.asList(new TlaLiteral(stateFQN)))));
    }

    public static void nonLeafStateDefinition(
            String stateFQN, DashModel dashModel, TlaModel tlaModel) {

        // <state-formula-name> = <child1-formula-name> union <child2-formula-name> ...

        List<TlaAppl> childStateFormulae =
                GeneralUtil.mapBy(
                        AuxiliaryDashAccessors.getChildStateNames(stateFQN, dashModel),
                        x -> new TlaAppl(getStateFormulaName(x)));

        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(getStateFormulaName(stateFQN)),
                        repeatedUnion(childStateFormulae)));
    }
}
