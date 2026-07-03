/*
    Storage and special functionality for module paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import java.util.*;

public class AMModules extends AMFuns {

    private List<AlloyModulePara> modules = emptyList();

    protected AMModules(AMModules other) {
        super(other);
        this.modules = new ArrayList<AlloyModulePara>(other.modules);
    }

    public AMModules(AlloyFile alloyFile) {
        super(alloyFile);
        this.modules = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyModulePara.class)
                .forEach(p -> this.addModulePara(p));
    }

    protected void resolve() {
        super.resolve();
        // there's actually nothing to do here
        // but we'll call the visitor for consistency
        // with the other paras
        List<AlloyModulePara> newModules = emptyList();
        for (AlloyModulePara modulePara : this.modules) {
            AlloyModulePara newModulePara = (AlloyModulePara) this.setMul(modulePara);
            newModules.add(newModulePara);
        }
        this.modules = newModules;
    }

    private void addModulePara(AlloyModulePara modulePara) {
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
