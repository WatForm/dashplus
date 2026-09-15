package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import java.util.Arrays;

import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.tlaast.SnowCatTypes.SCType;
import ca.uwaterloo.watform.tlaast.*;

public class A2THelpers {
  public static String sigConstraint(Qname sigQname) {
    return tlaQname(sigQname) + SIG_CONSTRAINT_SUFFIX;
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

  public static String tlaQname(Qname qname) {
    return qname.nameSpace.replace("/", "_") + "_" + qname.name;
  }

  public static SCType elementType() {
    return SnowCatTypes.Seq(SnowCatTypes.Str());
  }

  public static SCType relationType() {
    return SnowCatTypes.Set(elementType());
  }

  public static SCType binaryElementType() {
    return SnowCatTypes.Tuple(Arrays.asList(SnowCatTypes.Str(),SnowCatTypes.Str()));
  }

  public static SCType binaryRelationType() {
    return SnowCatTypes.Set(binaryElementType());
  }



  
}
