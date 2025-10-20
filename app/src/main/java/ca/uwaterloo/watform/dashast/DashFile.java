package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.CommonStrings;
import ca.uwaterloo.watform.utils.Pos;
import java.util.Collections;
import java.util.List;

public final class DashFile extends ASTNode {
    public String filename = "";
    public final List<DashParagraph> paragraphs;

    public DashFile(Pos pos, List<DashParagraph> paragraphs) {
        super(pos);
        this.paragraphs = Collections.unmodifiableList(paragraphs);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        for (DashParagraph p : paragraphs) {
            p.toString(sb, indent);
            sb.append(CommonStrings.NEWLINE + CommonStrings.NEWLINE);
        }
    }
}
