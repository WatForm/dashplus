package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.SigScope.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.*;

public class CmdScopeProfile {
  // this object contains everything a translator
  // needs to know about limitations of scopes of a cmd
  // the process will populate a CmdScopeProfile

  // topLevel ones may include one, abstract and enum sigs if they are top-level sigs

  // explicitExtends includes only non-top-level, non-in, non-equals sigs
  // They can include one, lone, some sigs, abstract sigs
  // (Enum sigs are always top-level)
  // these are sigs that have limits forced on them beyond
  // the extends and abstract sig constraints

  // in and equal child sigs cannot have scope limits placed on them

  private HashMap<Qname, SigScope> topLevel;

  private HashMap<Qname, SigScope> explicitExtends;

  private Integer intScope;

  public CmdScopeProfile() {
    this.topLevel = new HashMap<>();
    this.explicitExtends = new HashMap<>();
  }

  public SigScope getTopLevelScope(Qname sigName) {
    if (!this.topLevel.keySet().contains(sigName)) {
      throw ImplementationError.shouldNotReach();
    }
    return this.topLevel.get(sigName);
  }

  // used for iterating over these
  // since we don't know what sigs will be in this list
  public List<Qname> allExplicitExtendsQnames() {
    return setToList(this.explicitExtends.keySet());
  }

  public SigScope getExplicitExtendsScope(String sigName) {
    if (!this.explicitExtends.keySet().contains(sigName)) {
      throw ImplementationError.shouldNotReach();
    }
    return this.explicitExtends.get(sigName);
  }

  public Integer getValue(Qname sigName) {
    if (this.topLevel.keySet().contains(sigName)) {
      return this.topLevel.get(sigName).getValue();
    } else if (this.explicitExtends.keySet().contains(sigName)) {
      return this.explicitExtends.get(sigName).getValue();
    } else {
      throw ImplementationError.shouldNotReach();
    }
  }

  public Integer intScope() {
    return this.intScope;
  }

  public void addTopLevel(Qname sigName, SigScope ss) {
    this.topLevel.put(sigName, ss);
  }

  public void addExplicitExtends(Qname sigName, SigScope ss) {
    this.explicitExtends.put(sigName, ss);
  }

  public void setIntScope(Integer value) {
    this.intScope = value;
  }

  // written by chatgpt
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("       CmdScopeProfile {\n");
    sb.append("         int scope: " + this.intScope + "\n");
    sb.append("         topLevel:\n");
    if (topLevel.isEmpty()) {
      sb.append("    <none>\n");
    } else {
      for (Map.Entry<Qname, SigScope> e : topLevel.entrySet()) {
        sb.append("           ")
            .append(e.getKey())
            .append(" -> ")
            .append(e.getValue())
            .append('\n');
      }
    }

    sb.append("         explicitExtends:\n");
    if (explicitExtends.isEmpty()) {
      sb.append("    <none>\n");
    } else {
      for (Map.Entry<Qname, SigScope> e : explicitExtends.entrySet()) {
        sb.append("           ")
            .append(e.getKey().toString())
            .append(" -> ")
            .append(e.getValue())
            .append('\n');
      }
    }

    sb.append("}");

    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    // written by ChatGPT
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CmdScopeProfile)) {
      return false;
    }

    CmdScopeProfile other = (CmdScopeProfile) obj;
    return Objects.equals(topLevel, other.topLevel)
        && Objects.equals(explicitExtends, other.explicitExtends);
  }

  @Override
  public int hashCode() {
    // written by ChatGPT
    return Objects.hash(topLevel, explicitExtends);
  }
}
