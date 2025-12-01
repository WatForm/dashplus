package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;

public class TLAPlusFunctionDomain extends TLAPlusUnaryOp {

    public TLAPlusFunctionDomain(TLAPlusExp operand) {
        super(operand, TLAPlusOp.PrecedenceGroup.UNSAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.DOMAIN
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.operand)
                + TLAPlusStrings.PRIME;
    }
}
