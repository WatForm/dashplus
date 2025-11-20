package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDeclaration;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefinition;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModule;
import java.util.ArrayList;
import java.util.List;

public class States {

    public static void translateStates(Temp model, TLAPlusModule tm) {

        System.out.println("translate states called");

        tm.addVariable(getConf());

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
    }

    public static TLAPlusVariable getConf() {
        return new TLAPlusVariable("_conf");
    }

    public static TLAPlusFormulaDefinition leafStateFormula(Temp.State s) {
        List<TLAPlusExpression> e = new ArrayList<>();
        e.add(new TLAPlusStringLiteral(s.name));

        return new TLAPlusFormulaDefinition(
                new TLAPlusFormulaDeclaration(s.name), new TLAPlusSet(e));
    }

    public static TLAPlusExpression constructStateSet(List<Temp.State> states) {
        TLAPlusExpression top = new TLAPlusSet(new ArrayList<>()); // null set
        for (Temp.State s_ : states) {
            TLAPlusExpression new_top =
                    new TLAPlusUnionSet(top, new TLAPlusFormulaApplication(s_.name));
            top = new_top;
        }
        return top;
    }

    public static TLAPlusFormulaDefinition ORStateFormula(Temp.OR_State s) {
        return new TLAPlusFormulaDefinition(
                new TLAPlusFormulaDeclaration(s.name), constructStateSet(s.child_states));
    }

    public static TLAPlusFormulaDefinition ANDStateFormula(Temp.AND_State s) {
        return new TLAPlusFormulaDefinition(
                new TLAPlusFormulaDeclaration(s.name), constructStateSet(s.child_states));
    }
}
