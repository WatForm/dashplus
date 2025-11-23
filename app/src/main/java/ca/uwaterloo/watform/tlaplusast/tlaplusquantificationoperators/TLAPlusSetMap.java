package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusSetMap extends TLAPlusQuantificationOperator {

    public TLAPlusSetMap(
            TLAPlusVariable variable, TLAPlusExpression set, TLAPlusExpression expression) {
        super(variable, set, expression, TLAPlusOperator.PrecedenceGroup.SAFE);
    }

    // {e: x \in S}
    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.SET_START
                + this.getTLASnippetOfChild(getExpression())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.COLON
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(getVariable())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.SET_END;
    }
}
