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
}
