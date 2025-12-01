package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusNot extends TLAPlusUnaryOp {
    public TLAPlusNot(TLAPlusExp operand) {
        super(operand, TLAPlusOp.PrecedenceGroup.NOT);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.NOT + this.getTLASnippetOfChild(this.operand);
    }
}
