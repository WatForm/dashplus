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

public class StateDefnsD2T extends StandardVarsD2T {

    public StateDefnsD2T(DashModel dashModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(dashModel, tlaModel, verbose, debug);
    }

    protected void translateStateDefns() {
        if (dashModel.hasOnlyOneState()) return;
        List<String> stateFQNs = dashModel.allStateNames(); // dashModel.st.getAllNames();

        depthSort(stateFQNs);
        /* sorts it based on depth, thus all ancestors lie to the left and all descendants lie to the right, for every state */

        stateFQNs
                .reversed()
                .forEach(
                        stateFQN -> {
                            if (dashModel.isLeaf(stateFQN)) LeafStateDefn(stateFQN);
                            else nonLeafStateDefn(stateFQN);
                        });
    }

    public static void depthSort(List<String> stateFQNs) {
        Collections.sort(
                stateFQNs, (a, b) -> occurrences(a, QUALIFIER) - occurrences(b, QUALIFIER));
    }

    private void LeafStateDefn(String stateFQN) {

        tlaModel.addDefn(
                // <state-formula-name> == {"<state FQN>"}
                TlaDefn(tlaFQN(stateFQN), TlaSet(TlaStringLiteral(stateFQN))));
    }

    private void nonLeafStateDefn(String stateFQN) {

        List<TlaAppl> childStates = mapBy(dashModel.immChildren(stateFQN), s -> TlaAppl(tlaFQN(s)));

        tlaModel.addDefn(
                // <state-formula-name> = union <child_i-formula-name>...
                TlaDefn(tlaFQN(stateFQN), repeatedUnion(childStates)));
    }
}
