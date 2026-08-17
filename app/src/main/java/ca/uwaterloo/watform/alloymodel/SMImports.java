/*
  Notes
  - module name is never used
    - top-level module nameSpace is always 'this'
    - nameSpaces of nested modules are always dependent on filenames
    (e.g., in `open X/Y/zz.als` - X/Y is a directory path -- the namespace is zz (filename), or an open$count if it has been imported before)
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.Reporter.*;

import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMImports extends SMSigs {

  // map from nameSpace to info about import that created that nameSpace
  protected HashMap<String, ImportData> importTable = new LinkedHashMap<>();
  // map from nameSpace to how man imports in that nameSpace so far
  // used for making open$X nameSpaces
  // count seems to start at 3
  protected HashMap<String, Integer> importCount = new LinkedHashMap<>();

  Integer STARTING_OPEN_VALUE = 3;

  protected SMImports() {
    this.importCount.put(THIS_NAMESPACE, STARTING_OPEN_VALUE);
  }

  protected SMImports(SMImports other) {
    super(other);
    this.importTable = new HashMap<>(other.importTable);
    this.importCount = new HashMap<>(other.importCount);
  }

  // returns the newNameSpace to be used for this import
  // possibly null if should not import
  // b/c !this.createSM or
  // b/c already imported
  protected String createImport(
      Pos pos,
      String parentNameSpace,
      String importName,
      Optional<AlloyQnameExpr> asQname,
      List<Qname> sigParamValues) {
    if (this.createSM) {
      String potentialNameSpace;
      String fileName = "";
      ImportData id = new ImportData(pos, importName, sigParamValues);
      if (asQname.isPresent()) {
        potentialNameSpace = asQname.get().getName();
        if (this.importTable.keySet().contains(potentialNameSpace)) {
          throw AlloyModelError.duplicateAlias(pos, potentialNameSpace);
        }
      } else {
        // System.out.println("parentNameSpace: " + parentNameSpace);
        if (importName.startsWith("util/") && sigParamValues.isEmpty()) {
          // don't import same non-parameterized util module multiple times
          potentialNameSpace = importName.substring(importName.lastIndexOf('/') + 1);
          // will issue a warning if imported twice, but not reimport it
        } else {
          String prefix = (parentNameSpace.equals(THIS_NAMESPACE)) ? "" : parentNameSpace;
          // return the whole name if no slash
          // importedFileName is the filename of the import used in the
          // open statement (NOT the module name of the import)
          fileName = importName.substring(importName.lastIndexOf('/') + 1);
          potentialNameSpace = prefix + fileName;
          // System.out.println("fileName: " + fileName);
          // System.out.println("prefix: " + prefix);
        }
      }

      // System.out.println("potential nameSpace: " + potentialNameSpace);

      String newNameSpace;
      if (!this.importTable.keySet().contains(potentialNameSpace)) {
        // aliases will always land here
        newNameSpace = potentialNameSpace;
        this.importTable.put(newNameSpace, id);
        this.importCount.put(newNameSpace, STARTING_OPEN_VALUE);
        // System.out.println("importName: " + importName);
        // System.out.println("nameSpace: " + newNameSpace);
        return newNameSpace;
      } else {

        if (this.importTable.get(potentialNameSpace).equals(id)) {
          new WarningUser(
              pos,
              importName
                  + " attempted to be loaded multiple times in namespace "
                  + parentNameSpace);
          return null; // tells function calling this to not reimport it
        } else {
          // potentialNameSpace is already in use so we have to create
          // a new name
          Integer currentCount = this.importCount.get(parentNameSpace);
          newNameSpace = "open$" + Integer.toString(currentCount) + "/" + fileName;
          this.importTable.put(newNameSpace, id);
          this.importCount.put(newNameSpace, STARTING_OPEN_VALUE);
          this.importCount.put(parentNameSpace, currentCount + 1);
          // System.out.println("importName: " + importName);
          // System.out.println("nameSpace: " + newNameSpace);
          return newNameSpace;
        }
      }
    } else {
      return null;
    }
  }

  protected void resolveSMImports() {
    // check values substituted on imports are okay
    // because o/w error will show up a weird place
    List<Qname> resolvedSigParamValues;
    for (String ns : importTable.keySet()) {
      ImportData id = this.importTable.get(ns);
      resolvedSigParamValues = emptyList();
      for (Qname sigName : id.sigParamValues) {
        // System.out.println("sigName: " + sigName);
        List<Qname> possibleMatches = this.sigQnameMatches(sigName);
        if (possibleMatches.size() != 1) {
          throw AlloyModelError.ambiguousSigRef(id.pos, sigName.toString());
        } else {
          // put resolved sig name in list
          resolvedSigParamValues.add(possibleMatches.get(0));
        }
      }
      id.sigParamValues = resolvedSigParamValues;
    }
  }

  public void debugSMImports() {
    // written by ChatGPT
    StringBuilder sb = new StringBuilder("SMImports:\n");

    for (Map.Entry<String, ImportData> entry : this.importTable.entrySet()) {
      sb.append("  ").append(entry.getKey()).append(" -> ");
      sb.append(entry.getValue().toString());
      sb.append("\n");
    }

    System.out.println(sb.toString() + "\n");
  }
}
