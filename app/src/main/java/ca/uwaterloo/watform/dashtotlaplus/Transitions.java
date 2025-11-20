package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusIntersectionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusNotEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModule;
import java.util.ArrayList;

public class Transitions {
    public static void translateTransitions(Temp t, TLAPlusModule tm) {
        // pre-condition: from state in conf
        // post-condition: conf' = conf union to state
    }

    public static TLAPlusExpression stateInConf(Temp.State s) {
        // conf intersection state not equals phi
        TLAPlusFormulaApplication state = new TLAPlusFormulaApplication(s.name);
        TLAPlusSet emptySet = new TLAPlusSet(new ArrayList<>());
        TLAPlusExpression lhs = new TLAPlusIntersectionSet(States.getConf(), state);
        return new TLAPlusNotEquals(lhs, emptySet);
    }
}
