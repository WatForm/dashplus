/*
    Storage and special functionality for module paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.alloymodel.SigData.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import java.util.*;

public class AMThisModuleParas extends AMThisImportParas {

  private List<AlloyModulePara> modules = emptyList();

  public AMThisModuleParas() {}

  protected AMThisModuleParas(AMThisModuleParas other) {
    // System.out.println("here90");
    super(other);
    this.modules = new ArrayList<AlloyModulePara>(other.modules);
  }

  // not called via API to AlloyModel
  protected void addPara(AlloyModulePara modulePara) {
    // no need to set default multiplicities
    // can't be more than one modulePara

    // this will only be called at the top-level
    // o/w imports directly deal with the modulePara in the import and do substitutions
    // and thereafter ignore moduleParas in the imported file
    if (this.modules.size() == 1) {
      throw moduleMustBeUnique(modulePara.pos, modulePara.pos);
    }
    if (!modulePara.moduleArgs.isEmpty()) {
      // declare args as sigs
      for (AlloyModulePara.AlloyModuleArg modArg : modulePara.moduleArgs) {
        this.createSig(
            modulePara.pos, thisQname(modArg.qname.getName()), topLevelSigData(modulePara.pos));
      }
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
