package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StateDefns {
    // this class adds in formulae for every state in the dash model
    // leaf states are singleton sets that contain the fully qualified names as strings
    // non-leaf states are the union of the leaf states they contain
    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        if (!varNames.contains(CONF)) return;
        List<String> stateFQNs = dashModel.st.getAllNames();

        depthSort(stateFQNs); // sorts it based on depth, thus all ancestors lie to the left and all
        // descendants lie to the right, for every state

        stateFQNs
                .reversed()
                .forEach(
                        stateFQN -> {
                            if (dashModel.st.isLeaf(stateFQN))
                                LeafStateDefinition(stateFQN, tlaModel);
                            else nonLeafStateDefinition(stateFQN, dashModel, tlaModel);
                        });
    }

    public static void depthSort(List<String> stateFQNs) {
        Collections.sort(
                stateFQNs, (a, b) -> occurrences(a, QUALIFIER) - occurrences(b, QUALIFIER));
    }

    public static void LeafStateDefinition(String stateFQN, TlaModel tlaModel) {

        // <state-formula-name> == {"<state FQN>"}
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(tlaFQN(stateFQN)),
                        TlaSet(Arrays.asList(TlaStringLiteral(stateFQN)))));
    }

    public static void nonLeafStateDefinition(
            String stateFQN, DashModel dashModel, TlaModel tlaModel) {

        // <state-formula-name> = <child1-formula-name> union <child2-formula-name> ...

        List<TlaAppl> childStateFormulae =
                mapBy(
                        AuxDashAccessors.getChildStateNames(stateFQN, dashModel),
                        x -> TlaAppl(tlaFQN(x)));

        tlaModel.addDefn(TlaDefn(TlaDecl(tlaFQN(stateFQN)), repeatedUnion(childStateFormulae)));
    }
}
