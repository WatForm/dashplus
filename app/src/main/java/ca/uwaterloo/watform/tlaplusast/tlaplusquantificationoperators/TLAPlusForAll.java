package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusForAll extends TLAPlusQuantOp {
    public TLAPlusForAll(
            TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusQuantOp.predicateSnippetCore(this, TlaStrings.FOR_ALL);
    }
}
