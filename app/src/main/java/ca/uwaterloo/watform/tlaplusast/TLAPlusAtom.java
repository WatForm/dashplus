package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.*;

abstract class TLAPlusAtom extends ASTNode {
    private final String value;

    protected TLAPlusAtom(String value) {
        this.value = value;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {

        sb.append(this.value);
    }
}
