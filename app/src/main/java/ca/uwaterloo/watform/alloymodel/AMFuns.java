/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFunPara;
import java.util.*;

public class AMFuns extends AMPreds {

    // these have a name (see AlloyEnumPara.getId())
    // so we could put a table here to lookup by name
    protected List<AlloyFunPara> funs = emptyList();

    protected AMFuns(AMFuns other) {
        super(other);
        this.funs = new ArrayList<AlloyFunPara>(other.funs);
    }

    protected void resolve() {
        super.resolve();
        List<AlloyFunPara> newFuns = emptyList();
        for (AlloyFunPara funPara : this.funs) {
            // fun arguments need to be added to symbol table
            /*
            this.localEnvPush(funPara.arguments);
            AlloyFunPara newFunPara =
                    funPara.rebuild(
                            mapBy(funPara.arguments, a -> ((AlloyDecl) this.setMul(a))),
                            this.setMul(funPara.sub),
                            ((AlloyBlock) this.setMul(funPara.block)));
            this.localEnvPop(funPara.arguments);
            */
            AlloyFunPara newFunPara = (AlloyFunPara) this.setMul(funPara.arguments, funPara);
            newFuns.add(newFunPara);
        }
        this.funs = newFuns;
    }

    protected AMFuns(AlloyFile alloyFile) {
        super(alloyFile);
        this.funs = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyFunPara.class).forEach(p -> this.addFunPara(p));
    }

    // used by API also
    private void addFunPara(AlloyFunPara funPara) {
        this.addFunToPredFunTable(funPara);
        this.funs.add(funPara);
    }

    public List<AlloyFunPara> allFunParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyFunPara>(this.funs);
    }
}
