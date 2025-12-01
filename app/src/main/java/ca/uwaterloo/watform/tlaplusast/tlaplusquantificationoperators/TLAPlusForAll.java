package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusForAll extends TLAPlusQuantOp {
    public TLAPlusForAll(
            TLAPlusVar variable, TLAPlusExp set, TLAPlusExp expression) {
        super(variable, set, expression, TLAPlusOp.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusQuantOp.predicateSnippetCore(this, TLAPlusStrings.FOR_ALL);
    }
}
