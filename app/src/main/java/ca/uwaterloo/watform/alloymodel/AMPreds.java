/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import java.util.*;

public class AMPreds extends AMSigs {

    // these have a name (see AlloyEnumPara.getId())
    // so we could put a table here to lookup by name
    protected List<AlloyPredPara> preds = emptyList();

    protected AMPreds(AMPreds other) {
        super(other);
        // info about preds already added to Symbol table in super(other)
        this.preds = new ArrayList<AlloyPredPara>(other.preds);
    }

    protected AMPreds(AlloyFile alloyFile) {
        super(alloyFile);
        this.preds = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyPredPara.class).forEach(p -> this.addPredPara(p));
    }

    protected void resolve() {
        super.resolve();
        // now update the para for mul and add to predPara list
        List<AlloyPredPara> newPreds = emptyList();
        for (AlloyPredPara p : this.preds) {
            this.localEnvPush(p.arguments);
            // System.out.println("Rebuilding: ");
            // System.out.println(p);
            AlloyPredPara newP =
                    p.rebuild(
                            mapBy(p.arguments, a -> ((AlloyDecl) this.setMul(a))),
                            ((AlloyBlock) this.setMul(p.block)));
            this.localEnvPop(p.arguments);
            newPreds.add(newP);
        }
        this.preds = new ArrayList<AlloyPredPara>(newPreds);
    }

    // for adding via API
    public void addPredPara(AlloyPredPara predPara) {

        this.addToPredTable(predPara);
        this.preds.add(predPara);
    }

    public List<AlloyPredPara> allPredParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyPredPara>(this.preds);
    }

    public void addPred(String name, List<AlloyDecl> decls, List<AlloyExpr> eList) {
        this.addPredPara(new AlloyPredPara(new AlloyQnameExpr(name), decls, new AlloyBlock(eList)));
    }

    public AlloyPredPara getPredPara(String name) {
        for (AlloyPredPara p : this.preds) {
            if (p.getName().equals(name)) return p;
        }
        return null;
    }
}
