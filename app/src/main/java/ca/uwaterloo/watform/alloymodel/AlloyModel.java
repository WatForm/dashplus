package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.utils.PrintContext;
import java.io.StringWriter;
import java.util.*;

public class AlloyModel extends AMThisModuleParas {

  // fullFileName is needed to write file for AAInterface
  // AA Interface needs to have a written to disk file in the correct place
  // for user-defined imports to work correctly
  // this is the top-level file only
  public String fullFileName = null;

  public AlloyModel(AlloyFile alloyFile) {
    this.fullFileName = alloyFile.fullFileName;
    assert (this.fullFileName != null && this.fullFileName != "");
    // add integer module as built-in
    // but don't add it to list of paragraphs of AM
    this.addSMPara(simpleUtilImportPara(AlloyStrings.integerName), THIS_NAMESPACE);
    for (AlloyPara alloyPara : alloyFile.paras) {
      // all are in THIS_NAMESPACE
      if (alloyPara instanceof AlloyEnumPara p) addPara(p);
      else if (alloyPara instanceof AlloySigPara p) addPara(p);
      else if (alloyPara instanceof AlloyPredPara p) addPara(p);
      else if (alloyPara instanceof AlloyFunPara p) addPara(p);
      else if (alloyPara instanceof AlloyFactPara p) addPara(p);
      else if (alloyPara instanceof AlloyAssertPara p) addPara(p);
      else if (alloyPara instanceof AlloyCmdPara p) addPara(p);
      else if (alloyPara instanceof AlloyImportPara p) addPara(p);
      else if (alloyPara instanceof AlloyModulePara p) addPara(p);
      else throw new AssertionError("Unknown AlloyPara subtype: " + alloyPara.getClass());
    }
  }

  public AlloyModel(String newFullFileName) {
    // can create an AlloyModel from scratch and give it a fileName
    // so imports will be relative to this fileName
    this.fullFileName = newFullFileName;
    this.addSMPara(simpleUtilImportPara(AlloyStrings.integerName), THIS_NAMESPACE);
    assert (this.fullFileName != null && this.fullFileName != "");
  }

  public AlloyModel(AlloyModel other) {
    super(other);
    this.fullFileName = other.fullFileName;
  }

  public AlloyModel copy() {
    return new AlloyModel(this);
  }

  public AlloyModel copyImportsAndSigs() {
    List<AlloyPara> paras = new ArrayList<AlloyPara>();
    paras.addAll(this.allModuleParas());
    paras.addAll(this.allImportParas());
    paras.addAll(this.allSigParas());
    AlloyFile af = new AlloyFile(paras, this.fullFileName);
    af.fullFileName = this.fullFileName;
    return new AlloyModel(af);
  }

  public List<AlloyPara> getAllParas(boolean withCmds) {
    List<AlloyPara> allParas = new ArrayList<AlloyPara>();

    // first two must come before the rest
    allParas.addAll(this.allModuleParas());
    allParas.addAll(this.allImportParas());

    allParas.addAll(this.allEnumParas());
    allParas.addAll(this.allSigParas());
    // allParas.addAll(this.allMacroParas());
    allParas.addAll(this.allFunParas());
    allParas.addAll(this.allPredParas());
    allParas.addAll(this.allFactParas());
    allParas.addAll(this.allAssertParas());
    if (withCmds) allParas.addAll(this.allCmdParas());
    return allParas;
  }

  private AlloyFile toAlloyFile(boolean withCmds) {
    return new AlloyFile(this.getAllParas(withCmds), this.fullFileName);
  }

  public AlloyFile toAlloyFile() {
    return new AlloyFile(this.getAllParas(true), this.fullFileName);
  }

  public AlloyFile toAlloyFileNoCmds() {
    return new AlloyFile(this.getAllParas(false), this.fullFileName);
  }

  private String toString(boolean withCmds) {
    StringWriter sw = new StringWriter();
    PrintContext pCtx = new PrintContext(sw);

    AlloyFile newAlloyFile = this.toAlloyFile(withCmds);
    newAlloyFile.ppNewBlock(pCtx);
    return sw.toString();
  }

  public String toString() {
    return this.toString(true);
  }

  public String toStringNoCmds() {
    return this.toString(false);
  }
}
