package ca.uwaterloo.watform.tlaast.tlaquantops;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaast.TlaVar;

public class TlaSetFilter extends TlaQuantOp {

    /*
    
    {v \in S : exp}

    variable: v
    set: S  (can be an expression that evaluates to a set)
    expression: exp (boolean expression)

    used to construct a set by applying a filter to another set
    
    */

    public TlaSetFilter(TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.SAFE);
    }

    
    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SET_START
                + this.getTLASnippetOfChild(this.variable)
                + TlaStrings.SPACE
                + TlaStrings.IN
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.set)
                + TlaStrings.SPACE
                + TlaStrings.COLON
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.expression)
                + TlaStrings.SET_END;
    }
}
