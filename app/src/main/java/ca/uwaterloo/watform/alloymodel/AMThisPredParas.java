/*

*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import java.util.*;

public class AMThisPredParas extends AMThisSigParas {

    // these have a name (see AlloyEnumPara.getId())
    // so we could put a table here to lookup by name
    protected List<AlloyPredPara> preds = emptyList();

    protected AMThisPredParas() {}

    protected AMThisPredParas(AMThisPredParas other) {
        super(other);
        this.preds = new ArrayList<AlloyPredPara>(other.preds);
    }

    // for adding via API or in init
    public void addSMPara(AlloyPredPara predPara, String nameSpace) {
        // one decl can be a,b,c:X
        this.createPred(
                predPara.pos,
                nameSpaceQname(nameSpace, predPara.getName()),
                flatten(mapBy(predPara.arguments, decl -> decl.expand())),
                predPara.block);
    }

    /*
    private void addPara(AlloyPredPara predPara, String nameSpace) {
        addSMPara(predPara, nameSpace);
        this.preds.add(predPara);
    }
    */

    public void addPara(AlloyPredPara predPara) {
        addSMPara(predPara, THIS_NAMESPACE);
        this.preds.add(predPara);
    }

    public List<AlloyPredPara> allPredParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyPredPara>(this.preds);
    }

    // API calls -- all will be in this namespace

    public void addPred(String name, List<AlloyDecl> decls, List<AlloyExpr> eList) {
        this.addPara(new AlloyPredPara(new AlloyQnameExpr(name), decls, new AlloyBlock(eList)));
    }
}
