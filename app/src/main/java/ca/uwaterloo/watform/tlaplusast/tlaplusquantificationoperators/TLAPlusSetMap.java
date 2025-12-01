package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaOperator;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;
import ca.uwaterloo.watform.tlaplusast.TlaVar;

public class TLAPlusSetMap extends TLAPlusQuantOp {

    public TLAPlusSetMap(
            TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.SAFE);
    }

    // {e: x \in S}
    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SET_START
                + this.getTLASnippetOfChild(this.expression)
                + TlaStrings.SPACE
                + TlaStrings.COLON
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.variable)
                + TlaStrings.SPACE
                + TlaStrings.IN
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.set)
                + TlaStrings.SET_END;
    }
}
