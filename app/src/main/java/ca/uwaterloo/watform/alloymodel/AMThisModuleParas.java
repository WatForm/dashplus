/*
    Storage and special functionality for module paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import java.util.*;

public class AMThisModuleParas extends AMThisImportParas {

    private List<AlloyModulePara> modules = emptyList();

    public AMThisModuleParas() {}

    protected AMThisModuleParas(AMThisModuleParas other) {
        super(other);
        this.modules = new ArrayList<AlloyModulePara>(other.modules);
    }

    // not called via API to AlloyModel
    protected void addPara(AlloyModulePara modulePara) {
        // no need to set default multiplicities
        // can't be more than one modulePara
        if (this.modules.size() == 1) {
            throw moduleMustBeUnique(this.modules.get(0).pos, this.modules.get(0).pos);
        }
        this.modules.add(modulePara);
    }

    public Optional<AlloyQnameExpr> getModuleName() {
        if (this.modules.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(this.modules.get(0).qname);
        }
    }

    public List<AlloyModulePara> allModuleParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyModulePara>(this.modules);
    }
}
