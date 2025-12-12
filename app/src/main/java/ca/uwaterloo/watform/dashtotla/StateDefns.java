package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Collections;
import java.util.List;

public class StateDefns {
    // this class adds in formulae for every state in the dash model
    // leaf states are singleton sets that contain the fully qualified names as strings
    // non-leaf states are the union of the leaf states they contain
    public static void translate(List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        if (!vars.contains(CONF)) return;
        List<String> stateFQNs =
                AuxDashAccessors.getAllStateNames(dashModel); // dashModel.st.getAllNames();

        depthSort(stateFQNs);
        /* sorts it based on depth, thus all ancestors lie to the left and all descendants lie to the right, for every state */

        stateFQNs
                .reversed()
                .forEach(
                        stateFQN -> {
                            if (AuxDashAccessors.isLeaf(stateFQN, dashModel))
                                LeafStateDefn(stateFQN, tlaModel);
                            else nonLeafStateDefn(stateFQN, dashModel, tlaModel);
                        });
    }

    public static void depthSort(List<String> stateFQNs) {
        Collections.sort(
                stateFQNs, (a, b) -> occurrences(a, QUALIFIER) - occurrences(b, QUALIFIER));
    }

    public static void LeafStateDefn(String stateFQN, TlaModel tlaModel) {

        tlaModel.addDefn(
                // <state-formula-name> == {"<state FQN>"}
                TlaDefn(tlaFQN(stateFQN), TlaSet(TlaStringLiteral(stateFQN))));
    }

    public static void nonLeafStateDefn(String stateFQN, DashModel dashModel, TlaModel tlaModel) {

        List<TlaAppl> childStates =
                mapBy(
                        AuxDashAccessors.getChildStateNames(stateFQN, dashModel),
                        s -> TlaAppl(tlaFQN(s)));

        tlaModel.addDefn(
                // <state-formula-name> = union <child_i-formula-name>...
                TlaDefn(tlaFQN(stateFQN), repeatedUnion(childStates)));
    }
}
