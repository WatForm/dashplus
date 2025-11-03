package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.CommonStrings;
import ca.uwaterloo.watform.utils.GeneralUtil;
import ca.uwaterloo.watform.utils.Pos;
import java.util.Collections;
import java.util.List;

public final class DashFile extends AlloyFile {
    public String filename = "";
    public final List<DashParagraph> paragraphs;

    public DashFile(Pos pos, List<AlloyParagraph> paragraphs) {
        super(pos, GeneralUtil.extractItemsOfClass(paragraphs, AlloyParagraph.class));
        this.paragraphs =
                Collections.unmodifiableList(
                        GeneralUtil.extractItemsOfClass(paragraphs, DashParagraph.class));
    }

    public DashFile(List<AlloyParagraph> paragraphs) {
        this(Pos.UNKNOWN, paragraphs);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        for (AlloyParagraph p : super.paragraphs) {
            p.toString(sb, indent);
            sb.append(CommonStrings.NEWLINE + CommonStrings.NEWLINE);
        }
    }
}
