package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusSetFilter extends TLAPlusQuantificationOperator {

    public TLAPlusSetFilter(
            TLAPlusVariable variable, TLAPlusExpression set, TLAPlusExpression expression) {
        super(variable, set, expression, TLAPlusOperator.PrecedenceGroup.SAFE);
    }

    // {x \in S: P(x)}
    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.SET_START
                + this.getTLASnippetOfChild(this.variable)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.set)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.COLON
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.expression)
                + TLAPlusStrings.SET_END;
    }
}
