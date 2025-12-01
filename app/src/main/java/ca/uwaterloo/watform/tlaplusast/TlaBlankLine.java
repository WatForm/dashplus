package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.ASTNode;

public class TlaBlankLine extends ASTNode {

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append("\n");
        return;
    }
}
