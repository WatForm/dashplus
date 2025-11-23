package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusForAll extends TLAPlusQuantificationOperator {
    public TLAPlusForAll(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp, TLAPlusOperator.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.FOR_ALL
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.getV());
    }
}
