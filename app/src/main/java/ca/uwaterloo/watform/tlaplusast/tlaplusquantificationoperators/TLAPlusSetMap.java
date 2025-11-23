package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusSetMap extends TLAPlusQuantificationOperator {

    public TLAPlusSetMap(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp, TLAPlusOperator.PrecedenceGroup.SAFE);
    }

    // {e: x \in S}
    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.SET_START
                + this.getTLASnippetOfChild(getExp())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.COLON
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(getV())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.SET_END;
    }
}
