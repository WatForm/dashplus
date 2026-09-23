/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.parser.AlloyParser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.paravisitor.TestAndReplaceExprParaVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

// must come after everything else b/c it uses cmd, fact, etc add methods
public class AMThisImportParas extends AMThisCmdParas {

  // public interface

  public void addPara(AlloyImportPara importPara) {
    this.addSMPara(importPara, THIS_NAMESPACE);
    this.imports.add(importPara);
  }

  public List<AlloyImportPara> allImportParas() {
    // just to be safe, make a copy
    return new ArrayList<AlloyImportPara>(this.imports);
  }

  public void addUtilBooleanImport() {
    this.addUtilImport(AlloyStrings.booleanName);
  }

  public void addUtilIntegerImport() {
    this.addUtilImport(AlloyStrings.integerName);
  }

  // import util/name
  public void addUtilImport(String name) {
    this.addPara(simpleUtilImportPara(name));
  }

  // import util/name[sigName] as asName
  public void addUtilImport(String name, String sigName, String asName) {
    String fileName = AlloyStrings.utilName + "/" + name;
    this.addPara(
        new AlloyImportPara(
            false,
            new AlloyQnameExpr(
                List.of(new AlloyNameExpr(AlloyStrings.utilName), new AlloyNameExpr(name))),
            List.of(new AlloyQnameExpr(sigName)),
            new AlloyQnameExpr(asName),
            alloyParseUtilFile(Pos.UNKNOWN, fileName)));
  }

  // import util/name[sigName]
  public void addUtilImport(String name, String sigName) {
    String fileName = AlloyStrings.utilName + "/" + name;
    this.addPara(
        new AlloyImportPara(
            false,
            new AlloyQnameExpr(
                List.of(new AlloyNameExpr(AlloyStrings.utilName), new AlloyNameExpr(name))),
            List.of(new AlloyQnameExpr(sigName)),
            null,
            alloyParseUtilFile(Pos.UNKNOWN, fileName)));
  }

  // import util/name[sigName1, sigName2]
  public void addUtilImport(String name, List<AlloySigRefExpr> sigNames) {
    String fileName = AlloyStrings.utilName + "/" + name;
    this.addPara(
        new AlloyImportPara(
            false,
            new AlloyQnameExpr(
                List.of(new AlloyNameExpr(AlloyStrings.utilName), new AlloyNameExpr(name))),
            sigNames,
            null,
            alloyParseUtilFile(Pos.UNKNOWN, fileName)));
  }

  // end public interface ----------------------

  // importParas never have names
  protected List<AlloyImportPara> imports = emptyList();

  public AMThisImportParas() {}

  protected AMThisImportParas(AMThisImportParas other) {
    super(other);
    // does not recursively load
    this.imports = new ArrayList<AlloyImportPara>(other.imports);
  }

  private Boolean testForThis(AlloyExpr q) {
    return (q instanceof AlloyQnameExpr
        && ((AlloyQnameExpr) q).vars.size() == 2
        && ((AlloyQnameExpr) q).vars.get(0) instanceof AlloyThisExpr);
  }

  private AlloyQnameExpr replaceThis(AlloyExpr q, String nameSpace) {
    return AlloyVar(
        q.pos,
        List.of(nameSpace, ((AlloyQnameExpr) q).vars.get(1).getName()),
        ((AlloyQnameExpr) q).kind);
  }

  // private String importNameSpace;

  protected AlloyImportPara simpleUtilImportPara(String name) {
    String fileName = AlloyStrings.utilName + "/" + name;
    return new AlloyImportPara(
        false,
        new AlloyQnameExpr(
            List.of(new AlloyNameExpr(AlloyStrings.utilName), new AlloyNameExpr(name))),
        emptyList(),
        null,
        alloyParseUtilFile(Pos.UNKNOWN, fileName));
  }

  protected void addSMPara(AlloyImportPara importPara, String parentNameSpace) {
    /*
        1) add in exactly ordered or non-ordered
        2) figure out nameSpace of import
        3) substitute arguments for parameters
        4) put in common model within the nameSpace of import
        Note: we do not put paras imported into the AMThis classes, which store only 'this's paras (which are used to create a string of the top-level file)
        This will recursively load any imported files from this importPara (these will have a nested namespace)
    */

    // System.out.println("SM importing: " + importPara.toString());

    // from `open name[A, B] as X` get [A,B]
    // We will check in resolve that these are either already
    // fully qualified or they belong to this namespace
    List<AlloySigRefExpr> valsToSubstitute = importPara.sigRefs;
    // to check in resolve that these are all resolved sigs

    // will be only one modPara in the importedFile
    // might be none!
    List<AlloyModulePara> paras =
        extractItemsOfClass(importPara.importedFile.paras, AlloyModulePara.class);
    Map<AlloyQnameExpr, AlloySigRefExpr> substMap = new HashMap<>();
    if (!paras.isEmpty()) {
      AlloyModulePara modPara = paras.get(0);
      // parameters to substitute for
      List<AlloyModulePara.AlloyModuleArg> modArgs = modPara.moduleArgs;
      if (modArgs.size() != valsToSubstitute.size()) {
        throw AlloyModelError.importArgsNumDoesntMatch(importPara.pos, importPara.toString());
      }

      for (int i = 0; i < modArgs.size(); i++) {
        substMap.put(modArgs.get(i).qname, valsToSubstitute.get(i));
      }
      // 1) this only makes sense of a imported module has a modulePara
      // set what is "isExactly"
      for (int i = 0; i < modArgs.size(); i++) {
        if (modArgs.get(i).isExactly) {
          if (importPara.qname.equals("util/ordering")) {
            // ordering module import is handled
            // specially so can produce error
            // in AMScopes about applying only
            // to top-level sigs
            // Assumption: ordering module has only one param
            this.createOrderedSigWithExactScope(unknownQname(valsToSubstitute.get(i).getName()));
          } else {
            this.createNonOrderedSigWithExactScope(unknownQname(valsToSubstitute.get(i).getName()));
          }
        }
      }

    } else {
      // definitely no args!
      if (valsToSubstitute.size() != 0) {
        throw AlloyModelError.importArgsNumDoesntMatch(importPara.pos, importPara.toString());
      }
    }
    // System.out.println("parentNameSpace: " + parentNameSpace);
    // System.out.println("importPara: " + importPara.toString());
    // System.out.println("valsToSubstitute: " + valsToSubstitute.toString());

    // 2) determine nameSpace of import
    String importNameSpace =
        this.createImport(
            importPara.pos,
            parentNameSpace, // parentNameSpace
            importPara.qname.getName(),
            importPara.asQname,
            mapBy(valsToSubstitute, n -> unknownQname(n.getName())));

    // stop if should not import anything either
    // b/c repeated import or
    // b/c not doing createSM
    if (importNameSpace == null) return;

    // add all the paragraphs in the namespace
    // 3)
    // replace modArg name with value to substitute in all paragraphs
    TestAndReplaceExprParaVis vis1 =
        new TestAndReplaceExprParaVis(
            e -> substMap.keySet().contains(e), e -> ((AlloyExpr) substMap.get(e)));
    TestAndReplaceExprParaVis vis2 =
        new TestAndReplaceExprParaVis(x -> testForThis(x), x -> replaceThis(x, importNameSpace));
    List<AlloyPara> importParas =
        extractItemsNotOfClass(importPara.importedFile.paras, AlloyCmdPara.class);
    importParas = extractItemsNotOfClass(importParas, AlloyModulePara.class);
    List<AlloyPara> newParas = emptyList();
    for (AlloyPara para : importParas) {
      newParas.add(vis2.visit(vis1.visit(para)));
    }
    // System.out.println(newParas);

    // 4) below this is actually adding stuff to the SM
    // System.out.println("importName:  " + importPara.qname.getName());
    // System.out.println("importNameSpace: " + importNameSpace);
    for (AlloyPara alloyPara : newParas) {
      // only added to SM (not AMThis)
      if (alloyPara instanceof AlloyEnumPara p) addSMPara(p, importNameSpace);
      else if (alloyPara instanceof AlloySigPara p) {
        // System.out.println(p);
        addSMPara(p, importNameSpace);
      } else if (alloyPara instanceof AlloyPredPara p) addSMPara(p, importNameSpace);
      else if (alloyPara instanceof AlloyFunPara p) addSMPara(p, importNameSpace);
      else if (alloyPara instanceof AlloyFactPara p) addSMPara(p, importNameSpace);
      else if (alloyPara instanceof AlloyAssertPara p) addSMPara(p, importNameSpace);
      // else if (alloyPara instanceof AlloyCmdPara p) addSMPara(p, importNameSpace);
      else if (alloyPara instanceof AlloyImportPara p)
        // this will cause a recursive call if nested imports
        addSMPara(p, importNameSpace);
      // else if (alloyPara instanceof AlloyModulePara p) addSMPara(p, importNameSpace);
      else {
        System.out.println(alloyPara);
        throw ImplementationError.shouldNotReach();
      }
    }
  }
}
