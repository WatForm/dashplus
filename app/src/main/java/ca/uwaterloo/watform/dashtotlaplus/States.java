package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDeclaration;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefinition;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModule;
import java.util.ArrayList;
import java.util.List;

public class States {

    public static void translateStates(Temp model, TLAPlusModule tm) {

        tm.addVariable(Util.getConf());

        for (Temp.State s : model.leafStates) {
            tm.addFormulaDefinition(leafStateFormula(s));
        }
        // TODO toposort
        for (Temp.OR_State s : model.ORStates) {
            tm.addFormulaDefinition(ORStateFormula(s));
        }
        for (Temp.AND_State s : model.ANDStates) {
            tm.addFormulaDefinition(ANDStateFormula(s));
        }

        tm.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Util.SET_CONF),
                        new TLAPlusEquals(Util.getConf(), Util.getNullSet())));
    }

    public static TLAPlusFormulaDefinition leafStateFormula(Temp.State s) {
        List<TLAPlusExpression> e = new ArrayList<>();
        e.add(new TLAPlusStringLiteral(getState(s)));

        return new TLAPlusFormulaDefinition(
                new TLAPlusFormulaDeclaration(getState(s)), new TLAPlusSet(e));
    }

    public static TLAPlusExpression constructStateSet(List<Temp.State> states) {

        if (states.size() == 0) return Util.getNullSet();

        TLAPlusExpression top = new TLAPlusFormulaApplication(getState(states.get(0)));
        for (int i = 1; i < states.size(); i++) {
            Temp.State s = states.get(i);
            TLAPlusExpression new_top =
                    new TLAPlusUnionSet(top, new TLAPlusFormulaApplication(getState(s)));
            top = new_top;
        }
        return top;
    }

    public static TLAPlusFormulaDefinition ORStateFormula(Temp.OR_State s) {
        return new TLAPlusFormulaDefinition(
                new TLAPlusFormulaDeclaration(getState(s)), constructStateSet(s.child_states));
    }

    public static TLAPlusFormulaDefinition ANDStateFormula(Temp.AND_State s) {
        return new TLAPlusFormulaDefinition(
                new TLAPlusFormulaDeclaration(getState(s)), constructStateSet(s.child_states));
    }

    public static String getState(Temp.State s) {
        return "state_" + s.name;
    }
}
