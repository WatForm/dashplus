package ca.uwaterloo.watform.alloyast;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import ca.uwaterloo.watform.dashast.DashPara;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public class AlloyFile extends AlloyASTNode {
    public String filename = "";
    public final List<AlloyPara> paras;

    public AlloyFile(Pos pos, List<AlloyPara> paragraphs) {
        super(pos);
        this.paras = Collections.unmodifiableList(paragraphs);

        List<AlloyModulePara> modules = extractItemsOfClass(paragraphs, AlloyModulePara.class);

        if (modules.size() > 1) {
            throw AlloyCtorError.moduleIsUnique(modules.get(0).pos, modules.get(1).pos);
        }

        boolean noMoreModule = false;
        for (AlloyPara alloyPara : this.paras) {
            if (noMoreModule && alloyPara instanceof AlloyModulePara) {
                throw AlloyCtorError.moduleIsAtTop(alloyPara.pos);
            }
            if (!(alloyPara instanceof AlloyImportPara)
                    && !(alloyPara instanceof AlloyModulePara)) {
                noMoreModule = true;
            }
        }

        List<DashPara> dashParas = extractItemsOfClass(this.paras, DashPara.class);
        if (!dashParas.isEmpty()) {
            throw AlloyASTImplError.dashParaInAlloyFile(dashParas.get(0).pos);
        }
    }

    public AlloyFile(List<AlloyPara> paragraphs) {
        this(Pos.UNKNOWN, paragraphs);
    }

    public AlloyFile(AlloyPara paragraph) {
        this(Pos.UNKNOWN, Collections.singletonList(paragraph));
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        for (AlloyPara p : this.paras) {
            sb.append(AlloyStrings.TAB.repeat(indent));
            p.toString(sb, indent);
            sb.append(AlloyStrings.NEWLINE + AlloyStrings.NEWLINE);
        }
    }
}
