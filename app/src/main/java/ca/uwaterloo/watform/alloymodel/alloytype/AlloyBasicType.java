package ca.uwaterloo.watform.alloymodel.alloytype;

import static ca.uwaterloo.watform.alloymodel.alloytype.AlloyUnionType.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import java.util.*;

public class AlloyBasicType {
  private static int varCount = 0;
  private static Map<String, String> varValues = new HashMap();
  private List<String> type;

  // constructors
  AlloyBasicType() {
    this.type = emptyList();
  }

  public AlloyBasicType(String s) {
    // iden is a special case; makes a variable and
    // forms a relation from that variable to that variable
    if (s == VARIABLE_TYPE_STR) {
      this.type = new ArrayList<>(List.of("[VARIABLE-TYPE_NUMBER-" + varCount + "]"));
      ++varCount;
    } else if (s == IDEN_TYPE_STR) {
      this.type = new ArrayList<>(List.of("[VARIABLE-TYPE_NUMBER-" + varCount + "]"));
      this.extend(this);
      ++varCount;
    } else {
      this.type = new ArrayList<>(List.of(s));
    }
  }

  public AlloyBasicType(List<String> type) {
    this.type = type;
  }

  // used for building the type
  void addType(String newType) {
    if (newType == VARIABLE_TYPE_STR) {
      this.type.add("[VARIABLE-TYPE_NUMBER-" + varCount + "]");
      ++varCount;
    } else if (newType == IDEN_TYPE_STR) {
      this.type.add("[VARIABLE-TYPE_NUMBER-" + varCount + "]");
      this.type.add("[VARIABLE-TYPE_NUMBER-" + varCount + "]");
      ++varCount;
    } else {
      this.type.add(newType);
    }
  }

  public AlloyBasicType copy() {
    return new AlloyBasicType(new ArrayList<>(this.type));
  }

  public AlloyBasicType replaceAll(String from, String to) {
    if (from.equals(to)) return this.copy();
    AlloyBasicType res = new AlloyBasicType();
    for (String s : this.type) {
      if (s.equals(from)) res.addType(to);
      else res.addType(s);
    }
    return res;
  }

  // accessors
  boolean isEmpty() {
    return this.type.size() == 0;
  }

  public int getArity() {
    // arity is the number of elements in type
    return this.type.size();
  }

  public String first() {
    if (this.isEmpty()) throw AlloyTypeError.operationOnEmptyType("first");
    return this.type.get(0);
  }

  public String last() {
    if (this.isEmpty()) throw AlloyTypeError.operationOnEmptyType("last");
    return this.type.get(this.type.size() - 1);
  }

  public boolean isVariableType() {
    if (this.isEmpty()) throw AlloyTypeError.operationOnEmptyType("isVariableType");
    if (this.type.size() != 1) return false;
    return isVariableTypeString(this.type.get(0));
  }

  public boolean isAvailableVariableType() {
    if (this.isEmpty()) throw AlloyTypeError.operationOnEmptyType("isAvailableVariableType");
    if (this.type.size() != 1) return false;
    return isAvailableVariableTypeString(this.type.get(0));
  }

  // utilities
  static boolean isVariableTypeString(String t) {
    return t.matches("^\\[VARIABLE_TYPE_\\d+\\]$");
  }

  static boolean isAvailableVariableTypeString(String t) {
    if (!isVariableTypeString(t)) return false;
    return varValues.get(t) != null;
  }

  private static boolean matches(String t1, String t2) {
    if (t1.equals(UNIV_TYPE_STR) || t2.equals(UNIV_TYPE_STR)) return true;
    if (isVariableTypeString(t1) || isVariableTypeString(t2)) return true;
    return t1.equals(t2);
  }

  private static String restrict(String t1, String t2) {
    if (t1.equals(t2)) return t1;
    // if either is univ, return the other one
    if (t1.equals(UNIV_TYPE_STR)) return t2;
    if (t2.equals(UNIV_TYPE_STR)) return t1;
    // note: we assume they are not both variables
    if (isVariableTypeString(t1)) return t2;
    if (isVariableTypeString(t2)) return t1;
    return INVALID_TYPE_STR;
  }

  // type computations
  public Optional<AlloyBasicType> omitFirst() {
    if (this.isEmpty()) throw AlloyTypeError.operationOnEmptyType("omitFirst");
    else if (this.getArity() == 1) return Optional.empty();
    return Optional.of(new AlloyBasicType(this.type.subList(1, this.type.size())));
  }

  public Optional<AlloyBasicType> omitLast() {
    if (this.isEmpty()) throw AlloyTypeError.operationOnEmptyType("omitLast");
    else if (this.getArity() == 1) return Optional.empty();
    return Optional.of(new AlloyBasicType(allButLast(this.type)));
  }

  public Optional<AlloyBasicType> compose(AlloyBasicType o) {
    if (this.isEmpty() || o.isEmpty()) throw AlloyTypeError.operationOnEmptyType("compose");

    // return nothing if first and last atoms do not match
    if (!matches(this.last(), o.first())) return Optional.empty();

    Optional<AlloyBasicType> dom = this.omitLast();
    Optional<AlloyBasicType> rng = o.omitFirst();
    // return nothing if either side was empty
    if (!dom.isPresent() || !rng.isPresent()) return Optional.empty();
    return Optional.of(dom.get().extend(rng.get()));
  }

  public Optional<AlloyBasicType> domainRestrict(AlloyBasicType o) {
    if (this.isEmpty() || o.isEmpty()) throw AlloyTypeError.operationOnEmptyType("domainRestrict");

    // return nothing if no match
    if (this.type.size() == 0 || o.type.size() != 1) return Optional.empty();
    if (!matches(o.first(), this.first())) return Optional.empty();

    String newType = restrict(this.first(), o.first());
    AlloyBasicType res = this.copy();
    res.type.set(0, newType);
    if (isAvailableVariableTypeString(this.first())) {
      res.replaceAll(this.first(), newType);
    }
    if (isAvailableVariableTypeString(o.first())) {
      res.replaceAll(o.first(), newType);
    }
    return Optional.of(res);
  }

  public AlloyBasicType duplicate() {
    if (this.isEmpty()) throw AlloyTypeError.operationOnEmptyType("duplicate");
    // basically, extend with self
    return this.extend(this);
  }

  public AlloyBasicType extend(AlloyBasicType o) {
    if (this.isEmpty() || o.isEmpty()) throw AlloyTypeError.operationOnEmptyType("extend");
    AlloyBasicType res = this.copy();
    // add elem(s)
    for (String s : o.type) {
      res.addType(s);
    }
    return res;
  }

  public boolean matches(AlloyBasicType o) {
    if (this.isEmpty() || o.isEmpty()) throw AlloyTypeError.operationOnEmptyType("matches");
    else if (this.getArity() != o.getArity()) return false;
    for (int i = 0; i < this.type.size(); ++i) {
      if (!matches(this.type.get(i), o.type.get(i))) return false;
    }
    return true;
  }

  public Optional<AlloyBasicType> rangeRestrict(AlloyBasicType o) {
    // return nothing if no match
    if (this.type.size() == 0 || o.type.size() != 1) return Optional.empty();
    if (!matches(this.first(), o.first())) return Optional.empty();
    String newType = restrict(this.last(), o.last());

    AlloyBasicType res = this.copy();
    res.type.set(res.type.size() - 1, newType);
    if (isAvailableVariableTypeString(this.last())) {
      res.replaceAll(this.last(), newType);
    }
    if (isAvailableVariableTypeString(o.last())) {
      res.replaceAll(o.last(), newType);
    }
    return Optional.of(res);
  }

  public Optional<AlloyBasicType> restrict(AlloyBasicType o) {
    // must have same arity
    if (this.getArity() != o.getArity()) return Optional.empty();

    Map<String, String> tempVarValues = new HashMap();

    AlloyBasicType res = new AlloyBasicType();
    for (int i = 0; i < this.type.size(); ++i) {
      String thisType = this.type.get(i);
      String oType = o.type.get(i);
      if (tempVarValues.containsKey(thisType)) thisType = tempVarValues.get(thisType);
      if (tempVarValues.containsKey(oType)) oType = tempVarValues.get(oType);
      if (!matches(thisType, oType)) return Optional.empty();
      String restrict = restrict(thisType, oType);
      // TODO: replacement should probably happen after we confirm this is indeed a valid
      // restriction
      // restrict defers to t2, so check thisType first
      if (isAvailableVariableTypeString(thisType)) {
        tempVarValues.put(thisType, restrict);
        thisType.replaceAll(thisType, restrict);
      } else if (isAvailableVariableTypeString(oType)) {
        tempVarValues.put(oType, restrict);
        oType.replaceAll(oType, restrict);
      }
      res.addType(restrict(this.type.get(i), o.type.get(i)));
    }
    return Optional.of(res);
  }

  public AlloyBasicType reverse() {
    AlloyBasicType res = new AlloyBasicType();
    for (int i = this.type.size() - 1; i >= 0; --i) {
      res.addType(this.type.get(i));
    }
    return res;
  }

  // overriding default methods
  @Override
  public String toString() {
    // String types separated by commas, with enclosing round braces
    return "(" + String.join(", ", this.type) + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AlloyBasicType)) return false;
    AlloyBasicType other = (AlloyBasicType) o;

    return this.type.equals(other.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }
}
