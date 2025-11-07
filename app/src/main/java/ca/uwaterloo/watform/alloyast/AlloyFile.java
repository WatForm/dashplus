package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public class AlloyFile extends AlloyASTNode {
    public String filename = "";
    public final List<AlloyParagraph> paragraphs;

    public AlloyFile(Pos pos, List<AlloyParagraph> paragraphs) {
        super(pos);
        this.paragraphs = Collections.unmodifiableList(paragraphs);
    }

    public AlloyFile(List<AlloyParagraph> paragraphs) {
        this(Pos.UNKNOWN, paragraphs);
    }

    public AlloyFile(AlloyParagraph paragraph) {
        this(Pos.UNKNOWN, Collections.singletonList(paragraph));
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        for (AlloyParagraph p : paragraphs) {
            sb.append(AlloyStrings.TAB.repeat(indent));
            p.toString(sb, indent);
            sb.append(AlloyStrings.NEWLINE + AlloyStrings.NEWLINE);
        }
    }
}
