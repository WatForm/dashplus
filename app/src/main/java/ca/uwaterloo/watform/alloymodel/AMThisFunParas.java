/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.AlloyFunPara;
import java.util.*;

public class AMThisFunParas extends AMThisPredParas {

    // these have a name (see AlloyEnumPara.getId())
    // so we could put a table here to lookup by name
    protected List<AlloyFunPara> funs = emptyList();

    protected AMThisFunParas() {}

    protected AMThisFunParas(AMThisFunParas other) {
        super(other);
        this.funs = new ArrayList<AlloyFunPara>(other.funs);
    }

    // for adding via API or in init
    public void addSMPara(AlloyFunPara funPara, String nameSpace) {
        this.createFun(
                funPara.pos,
                nameSpaceQname(nameSpace, funPara.getName()),
                flatten(mapBy(funPara.arguments, decl -> decl.expand())),
                funPara.sub,
                funPara.block);
    }

    /*
    protected void addPara(AlloyFunPara funPara, String nameSpace) {
        this.addSMPara(funPara, nameSpace);
        this.funs.add(funPara);
    }
    */

    public void addPara(AlloyFunPara funPara) {
        this.addSMPara(funPara, THIS_NAMESPACE);
        this.funs.add(funPara);
    }

    public List<AlloyFunPara> allFunParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyFunPara>(this.funs);
    }
}
