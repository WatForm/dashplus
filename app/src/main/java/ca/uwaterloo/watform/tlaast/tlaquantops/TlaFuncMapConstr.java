package ca.uwaterloo.watform.tlaast.tlaquantops;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaast.TlaVar;

public class TlaFuncMapConstr extends TlaQuantOp {

    /*
    [v \in S |-> exp]

    variable: v
    set: S  (can be an expression that evaluates to a set)
    expression: exp 

    used to construct another function by applying a map to a function
    */

    public TlaFuncMapConstr(TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.SAFE);
    }

    
    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SQUARE_BRACKET_OPEN
                + this.getTLASnippetOfChild(this.variable)
                + TlaStrings.IN
                + TlaStrings.SPACE
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.set)
                + TlaStrings.SPACE
                + TlaStrings.MAP
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.expression)
                + TlaStrings.SPACE;
    }
}
