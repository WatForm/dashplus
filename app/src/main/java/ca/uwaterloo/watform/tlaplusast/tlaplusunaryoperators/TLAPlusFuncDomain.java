package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;

public class TLAPlusFuncDomain extends TLAPlusUnaryOp {

    public TLAPlusFuncDomain(TLAPlusExp operand) {
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
