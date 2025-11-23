package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusExists extends TLAPlusQuantificationOperator {
    public TLAPlusExists(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp, TLAPlusOperator.PrecedenceGroup.PREDICATE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.EXISTS
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.getV());
    }
}
