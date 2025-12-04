package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.utils.ASTNode;

public class TlaBlankLine extends ASTNode {

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append("\n");
        return;
    }
}
