package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusFunctionMapConstructor extends TLAPlusQuantificationOperator {
    public TLAPlusFunctionMapConstructor(
            TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp, TLAPlusOperator.PrecedenceGroup.SAFE);
    }

    // [x \in S |-> e]
    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.SQUARE_BRACKET_OPEN
                + this.getTLASnippetOfChild(getV())
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(getSet())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.MAP
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(getExp())
                + TLAPlusStrings.SPACE;
        // TODO fix this
    }
}
