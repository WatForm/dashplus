package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVar;

public class TLAPlusSetMap extends TLAPlusQuantOp {

    public TLAPlusSetMap(
            TLAPlusVar variable, TLAPlusExp set, TLAPlusExp expression) {
        super(variable, set, expression, TLAPlusOp.PrecedenceGroup.SAFE);
    }

    // {e: x \in S}
    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.SET_START
                + this.getTLASnippetOfChild(this.expression)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.COLON
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.variable)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.set)
                + TLAPlusStrings.SET_END;
    }
}
