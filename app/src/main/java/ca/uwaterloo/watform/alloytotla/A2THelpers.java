package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.tlaast.TlaAppl;

public class A2THelpers {
  public static String sigConstraint(String sigName) {
    return sigName + SIG_CONSTRAINT_SUFFIX;
  }

  public static String unnamedFact(int n) {
    return UNNAMED_FACT_PREFIX + n;
  }

  public static TlaAppl SIG_SETS_PRIMED() {
    return TlaAppl(SIG_SETS_PRIMED);
  }

  public static TlaAppl SIG_SETS_UNPRIMED() {
    return TlaAppl(SIG_SETS_UNPRIMED);
  }

  public static String tlaQnameSig(Qname qname) {
    return qname.nameSpace.replace("/", "_") + "_" + qname.name;
  }
}
