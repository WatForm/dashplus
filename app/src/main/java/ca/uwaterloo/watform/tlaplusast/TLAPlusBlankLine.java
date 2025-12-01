package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.ASTNode;

public class TLAPlusBlankLine extends ASTNode {

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append("\n");
        return;
    }
}
