package ca.uwaterloo.watform.tlaast.tlaquantops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaExists extends TlaQuantOp {
    public TlaExists(TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaQuantOp.predicateSnippetCore(this, TlaStrings.EXISTS);
    }
}
