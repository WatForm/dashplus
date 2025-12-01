package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVar;

public class TLAPlusFunctionMapConstructor extends TLAPlusQuantificationOperator {
    public TLAPlusFunctionMapConstructor(
            TLAPlusVar variable, TLAPlusExp set, TLAPlusExp expression) {
        super(variable, set, expression, TLAPlusOp.PrecedenceGroup.SAFE);
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
