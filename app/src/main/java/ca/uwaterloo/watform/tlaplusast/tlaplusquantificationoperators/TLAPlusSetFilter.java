package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusSetFilter extends TLAPlusQuantificationOperator {

    public TLAPlusSetFilter(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp, TLAPlusOperator.PrecedenceGroup.SAFE);
    }

    // {x \in S: P(x)}
    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.SET_START
                + this.getTLASnippetOfChild(getV())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(getSet())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.COLON
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(getExp())
                + TLAPlusStrings.SET_END;
    }
}
