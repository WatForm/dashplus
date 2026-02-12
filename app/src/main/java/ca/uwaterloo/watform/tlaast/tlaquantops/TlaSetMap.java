package ca.uwaterloo.watform.tlaast.tlaquantops;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaast.TlaVar;

public class TlaSetMap extends TlaQuantOp {

    /*

    {exp : v \in S}

    variable: v
    set: S  (can be an expression that evaluates to a set)
    expression: exp (usually written in terms of v)

    used to construct a set by applying a filter to another set

    */

    public TlaSetMap(TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.SAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SET_START
                + this.getTLASnippetOfChild(this.expression)
                + TlaStrings.SPACE
                + TlaStrings.COLON
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.variable)
                + TlaStrings.SPACE
                + TlaStrings.SET_IN
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.set)
                + TlaStrings.SET_END;
    }
}
