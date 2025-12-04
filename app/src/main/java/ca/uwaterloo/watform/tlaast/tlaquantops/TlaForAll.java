package ca.uwaterloo.watform.tlaast.tlaquantops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaForAll extends TlaQuantOp {
    public TlaForAll(TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaQuantOp.predicateSnippetCore(this, TlaStrings.FOR_ALL);
    }
}
