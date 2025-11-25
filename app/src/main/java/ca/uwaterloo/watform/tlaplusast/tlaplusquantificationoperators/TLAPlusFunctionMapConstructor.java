package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusFunctionMapConstructor extends TLAPlusQuantificationOperator {
    public TLAPlusFunctionMapConstructor(
            TLAPlusVariable variable, TLAPlusExpression set, TLAPlusExpression expression) {
        super(variable, set, expression, TLAPlusOperator.PrecedenceGroup.SAFE);
    }

    // [x \in S |-> e]
    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.SQUARE_BRACKET_OPEN
                + this.getTLASnippetOfChild(this.variable)
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.set)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.MAP
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.expression)
                + TLAPlusStrings.SPACE;
    }
}
