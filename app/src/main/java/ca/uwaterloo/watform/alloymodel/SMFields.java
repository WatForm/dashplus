package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.paragraph.AlloyPara.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
// import static ca.uwaterloo.watform.parser.AlloyParser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMFields extends SMImports {

  // Qname is unique for fields
  // Linked is important here so we can get them out in the order
  // they were added
  private HashMap<Qname, FieldData> fieldTable = new LinkedHashMap<>();

  protected SMFields() {}

  protected SMFields(SMFields other) {
    super(other);
    this.fieldTable = new HashMap<>(other.fieldTable);
  }

  protected void createField(
      Pos p, String nameSpace, String fieldName, String sigParent, AlloyExpr fieldExpr) {
    if (this.createSM) {
      // System.out.println("adding field: " + fieldQname(nameSpace, sigParent,
      // fieldName).toString());
      reqNonNull(nullField(p, this), nameSpace, fieldName, sigParent, fieldExpr);
      assert (nameSpace != UNKNOWN_NAMESPACE);
      if (this.fieldTable.containsKey(fieldQname(nameSpace, sigParent, fieldName)))
        throw AlloyModelError.duplicateFieldNameInSig(p, fieldName);
      else
        this.fieldTable.put(fieldQname(nameSpace, sigParent, fieldName), new FieldData(fieldExpr));
    }
  }

  public List<Qname> allFieldQnames() {
    return setToList(this.fieldTable.keySet());
  }

  // does not depend on context
  // full qname may not be unknown
  private List<Qname> possibleMatches(Qname qname) {
    return fieldTable.keySet().stream()
        .filter(
            q ->
                q.name.equals(qname.name)
                    & (q.sigParent.equals(qname.sigParent) || qname.sigParent == null)
                    & (q.nameSpace.equals(qname.nameSpace)
                        || qname.nameSpace.equals(UNKNOWN_NAMESPACE)))
        .toList();
  }

  public List<Qname> fieldQnameMatches(Qname qname, String nameSpaceWithin) {
    // either matches exactly (which would mean only one match)
    // or could match on multiple of UNKNOWN_NAMESPACE
    // does not depend on context of nameSpaceWithin
    List<Qname> possibleMatches = possibleMatches(qname);

    // now let's look at contextWithin to disambiguate
    if (possibleMatches.size() > 1 && !nameSpaceWithin.equals(THIS_NAMESPACE)) {
      // favour currentNameSpace if currentNameSpace is not THIS_NAMESPACE
      List<Qname> nameSpaceWithinMatches =
          filterBy(possibleMatches, q -> q.nameSpace.equals(nameSpaceWithin));
      if (nameSpaceWithinMatches.size() == 0) {
        throw AlloyModelError.fieldNotRecognizedWithImport(qname.toString());
      } else {
        return nameSpaceWithinMatches;
      }
    } else {
      return possibleMatches;
    }
  }

  // used externally when not sure if qname is complete
  public Boolean isField(Qname qname, String nameSpaceWithin) {
    return !fieldQnameMatches(qname, nameSpaceWithin).isEmpty();
  }

  // sets arities in fieldTable and default mul in field Types
  protected void resolveSMFields(
      QuadFunction<AlloyExpr, String, Optional<String>, List<AlloyDecl>, ResolveInfo> resolve1) {

    for (Qname fieldQname : this.allFieldQnames()) {
      AlloyExpr fieldExpr = this.fieldTable.get(fieldQname).expr;
      // throws an error if it can't calculate it and set defaults
      ResolveInfo resolveInfo =
          resolve1.apply(
              this.fieldExpr(fieldQname, fieldQname.nameSpace),
              fieldQname.nameSpace,
              Optional.of(fieldQname.sigParent),
              emptyList());
      if (!resolveInfo.arity.isPresent()) {
        throw AlloyModelError.unknownArity(fieldExpr.pos, fieldExpr.toString());
      } else {
        // fields have +1 in arity than returned here b/c of sig
        int fieldArity = resolveInfo.arity.get() + 1;
        this.fieldTable.get(fieldQname).arity = Optional.of(fieldArity);
        this.fieldTable.get(fieldQname).expr = resolveInfo.exp;
        // KENG: also need to set this.fieldTable.get(fieldQname).product
      }
    }
  }

  // individual getters

  private void existsField(Qname fieldQname) {
    if (possibleMatches(fieldQname).size() != 1)
      throw AlloyModelImplError.tryingToAccessNonExistentField(fieldQname.toString());
  }

  public AlloyExpr fieldExpr(Qname fieldQname, String nameSpaceWithin) {
    existsField(fieldQname);
    return this.fieldTable.get(fieldQname).expr;
  }

  protected Optional<Integer> fieldArity(Qname fieldQname) {
    existsField(fieldQname);
    return this.fieldTable.get(fieldQname).arity;
  }

  public List<Qname> fieldProduct(Qname fieldQname) {
    existsField(fieldQname);
    return this.fieldTable.get(fieldQname).product;
  }

  protected Boolean isFieldResolved(Qname fieldQname) {
    existsField(fieldQname);
    return this.fieldTable.get(fieldQname).arity.isPresent();
  }

  /*
  public Boolean endsInOne() {
      // assert (this.isResolved);
      // TODO:
  }
  */

  public void debugSMFields() {
    StringBuilder sb = new StringBuilder("SMFields:\n");

    fieldTable.forEach(
        (qname, fieldData) ->
            sb.append("    ").append(qname).append(" -> ").append(fieldData).append('\n'));

    System.out.println(sb.toString() + "\n");
  }
}
