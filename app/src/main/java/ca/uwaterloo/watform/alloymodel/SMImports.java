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
  // Next import position in each namespace, used for Alloy's open$X aliases.
  // Every import occupies a position, including imports with explicit aliases.
  protected HashMap<String, Integer> importCount = new LinkedHashMap<>();

  protected SMImports() {
    // AlloyModel loads the implicit integer import through createImport itself.
    this.importCount.put(THIS_NAMESPACE, 1);
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
      int importPosition = this.importCount.get(parentNameSpace);
      this.importCount.put(parentNameSpace, importPosition + 1);
      String fileName = importName.substring(importName.lastIndexOf('/') + 1);
      String localNameSpace;
      ImportData id = new ImportData(pos, importName, sigParamValues, parentNameSpace);
      // this.debugSMImports();
      if (asQname.isPresent()) {
        localNameSpace = asQname.get().getName();
        String potentialNameSpace = qualifyImportNameSpace(parentNameSpace, localNameSpace);
        if (this.importTable.keySet().contains(potentialNameSpace)) {
          throw AlloyModelError.duplicateAlias(pos, localNameSpace);
        }
      } else {
        localNameSpace = fileName;
      }

      String potentialNameSpace = qualifyImportNameSpace(parentNameSpace, localNameSpace);

      // System.out.println("potential nameSpace: " + potentialNameSpace);

      String newNameSpace;
      if (!this.importTable.keySet().contains(potentialNameSpace)) {
        // aliases will always land here
        newNameSpace = potentialNameSpace;
        this.importTable.put(newNameSpace, id);
        // Nested modules also have an implicit integer import, but we do not
        // recursively load that built-in module into every namespace.
        this.importCount.put(newNameSpace, 2);
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
          newNameSpace =
              qualifyImportNameSpace(parentNameSpace, "open$" + importPosition);
          this.importTable.put(newNameSpace, id);
          this.importCount.put(newNameSpace, 2);
          // System.out.println("importName: " + importName);
          // System.out.println("nameSpace: " + newNameSpace);
          return newNameSpace;
        }
      }
    } else {
      return null;
    }
  }

  private static String qualifyImportNameSpace(String parentNameSpace, String localNameSpace) {
    return parentNameSpace.equals(THIS_NAMESPACE)
        ? localNameSpace
        : parentNameSpace + "/" + localNameSpace;
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
        List<Qname> possibleMatches = this.sigQnameMatches(sigName, id.parentNameSpace);
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
