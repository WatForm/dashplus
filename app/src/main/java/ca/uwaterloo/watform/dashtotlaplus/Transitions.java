package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDeclaration;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefinition;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusAnd;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusIntersectionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusNotEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusOrList;
import ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators.TLAPlusPrime;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModule;
import java.util.ArrayList;
import java.util.List;

public class Transitions {
    public static void translateTransitions(Temp t, TLAPlusModule tm) {

        for (Temp.Transition trans : t.transitions) {
            translateTransition(trans, tm);
        }

        List<TLAPlusExpression> transitionFormulaeApplications = new ArrayList<>();
        for (Temp.Transition trans : t.transitions)
            transitionFormulaeApplications.add(new TLAPlusFormulaApplication(trans.name));

        tm.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Util.TRANSITIONS),
                        new TLAPlusOrList(transitionFormulaeApplications)));
    }

    public static void translateTransition(Temp.Transition t, TLAPlusModule tm) {

        // pre-condition: from state in conf
        // post-condition: conf' = conf union to state

        String preCondition = "pre_" + t.name;
        String postCondition = "post_" + t.name;

        tm.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(preCondition), stateInConf(t.from)));

        tm.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(postCondition), updateConf(t.from)));

        tm.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(t.name),
                        new TLAPlusAnd(
                                new TLAPlusFormulaApplication(preCondition),
                                new TLAPlusFormulaApplication(postCondition))));
    }

    public static TLAPlusExpression updateConf(Temp.State s) {
        return new TLAPlusEquals(
                new TLAPlusPrime(Util.getConf()),
                new TLAPlusUnionSet(
                        Util.getConf(), new TLAPlusFormulaApplication(States.getState(s))));
    }

    public static TLAPlusExpression stateInConf(Temp.State s) {
        // conf intersection state not equals phi
        return new TLAPlusNotEquals(
                new TLAPlusIntersectionSet(
                        Util.getConf(), new TLAPlusFormulaApplication(States.getState(s))),
                Util.getNullSet());
    }
}
