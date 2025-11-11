package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.GeneralUtil;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public final class DashFile extends AlloyFile {
    public String filename = "";
    public final DashParagraph stateRoot;

    public DashFile(Pos pos, List<AlloyParagraph> paragraphs) {
        super(
                pos,
                GeneralUtil.filterBy(
                        paragraphs, alloyParagraph -> !(alloyParagraph instanceof DashParagraph)));
        List<DashParagraph> dashParagraphs =
                GeneralUtil.extractItemsOfClass(paragraphs, DashParagraph.class);
        if (1 != dashParagraphs.size()) {
            throw DashCtorError.exactlyOneStateRoot();
        }
        this.stateRoot = dashParagraphs.get(0);
    }

    public DashFile(List<AlloyParagraph> paragraphs) {
        this(Pos.UNKNOWN, paragraphs);
    }

    public List<AlloyParagraph> getAlloyParagraphs() {
        return super.paragraphs;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(sb, indent);
        this.stateRoot.toString(sb, indent);
    }
}
