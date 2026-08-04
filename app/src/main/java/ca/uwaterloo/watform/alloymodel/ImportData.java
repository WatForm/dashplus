package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class ImportData {
  public Pos pos;
  public String importedModule;
  public List<Qname> sigParamValues;

  public ImportData(Pos p, String importedModule, List<Qname> sigParamValues) {
    this.pos = p;
    this.importedModule = importedModule;
    this.sigParamValues = sigParamValues;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("");

    sb.append(this.importedModule).append("(");
    for (Qname qname : this.sigParamValues) {
      sb.append(qname.toString());
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ImportData other)) {
      return false;
    }

    return this.importedModule.equals(other.importedModule)
        && this.sigParamValues.equals(other.sigParamValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pos, importedModule, sigParamValues);
  }
}
