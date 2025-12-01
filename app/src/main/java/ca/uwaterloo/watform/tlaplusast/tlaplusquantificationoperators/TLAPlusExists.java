package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusExists extends TLAPlusQuantOp {
    public TLAPlusExists(
            TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusQuantOp.predicateSnippetCore(this, TlaStrings.EXISTS);
    }
}
