package ca.uwaterloo.watform.tlaast.tlaquantops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaForAll extends TlaQuantOp {

    /*
    \A v \in S : exp

    variable: v
    set: S  (can be an expression that evaluates to a set)
    expression: exp  (boolean expression)
    */

    public TlaForAll(TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaQuantOp.predicateSnippetCore(this, TlaStrings.FOR_ALL);
    }
}
