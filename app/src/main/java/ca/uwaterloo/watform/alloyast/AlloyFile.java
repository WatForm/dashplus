package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import ca.uwaterloo.watform.dashast.DashParagraph;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public class AlloyFile extends AlloyASTNode {
    public String filename = "";
    public final List<AlloyParagraph> paragraphs;

    public AlloyFile(Pos pos, List<AlloyParagraph> paragraphs) {
        super(pos);
        this.paragraphs = Collections.unmodifiableList(paragraphs);

        List<AlloyModulePara> modules =
                GeneralUtil.extractItemsOfClass(paragraphs, AlloyModulePara.class);

        if (modules.size() > 1) {
            throw AlloyCtorError.moduleIsUnique(modules.get(0).pos, modules.get(1).pos);
        }

        boolean noMoreModule = false;
        for (AlloyParagraph alloyParagraph : this.paragraphs) {
            if (noMoreModule && alloyParagraph instanceof AlloyModulePara) {
                throw AlloyCtorError.moduleIsAtTop(alloyParagraph.pos);
            }
            if (!(alloyParagraph instanceof AlloyImportPara)
                    && !(alloyParagraph instanceof AlloyModulePara)) {
                noMoreModule = true;
            }
        }

        List<DashParagraph> dashParagraphs =
                GeneralUtil.extractItemsOfClass(this.paragraphs, DashParagraph.class);
        if (!dashParagraphs.isEmpty()) {
            throw AlloyASTImplError.dashParagraphInAlloyFile(dashParagraphs.get(0).pos);
        }
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
