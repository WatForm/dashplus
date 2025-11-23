package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusForAll extends TLAPlusQuantificationOperator {
    public TLAPlusForAll(
            TLAPlusVariable variable, TLAPlusExpression set, TLAPlusExpression expression) {
        super(variable, set, expression, TLAPlusOperator.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusQuantificationOperator.predicateSnippetCore(this, TLAPlusStrings.FOR_ALL);
    }
}
