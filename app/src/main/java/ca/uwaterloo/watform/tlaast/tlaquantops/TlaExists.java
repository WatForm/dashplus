package ca.uwaterloo.watform.tlaast.tlaquantops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.List;

public class TlaExists extends TlaQuantOp {

    /*
    \E v \in S : exp

    variable: v
    set: S  (can be an expression that evaluates to a set)
    expression: exp  (boolean expression)
    */

    public TlaExists(List<TlaQuantOpHead> heads, TlaExp expression) {
        super(heads, expression, TlaOperator.PrecedenceGroup.UNSAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaQuantOp.predicateSnippetCore(this, TlaStrings.EXISTS);
    }
}
