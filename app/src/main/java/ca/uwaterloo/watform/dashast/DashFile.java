package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public final class DashFile extends AlloyFile {
    public String filename = "";
    public final DashState stateRoot;

    public DashFile(Pos pos, List<AlloyPara> paragraphs) {
        super(pos, filterBy(paragraphs, alloyPara -> !(alloyPara instanceof DashPara)));
        List<DashPara> dashParas = extractItemsOfClass(paragraphs, DashPara.class);
        if (1 != dashParas.size()) {
            throw DashCtorError.exactlyOneStateRoot();
        }
        this.stateRoot = (DashState) dashParas.get(0);
    }

    public DashFile(List<AlloyPara> paragraphs) {
        this(Pos.UNKNOWN, paragraphs);
    }

    public List<AlloyPara> getAlloyParas() {
        return super.paras;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(sb, indent);
        this.stateRoot.toString(sb, indent);
    }
}
