package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.tlaast.TlaTypes;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class FieldsA2T extends FactsA2T {

  public FieldsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
    super(alloyModel, verbose, debug);
  }

  public void addFieldVars(TlaModel tlaModel)
  {
    for (Qname field : alloyModel.allFieldQnames()) {
      String f = tlaQname(field);
      tlaModel.addVar(TlaVar(f), TlaTypes.Set(TlaTypes.Seq(TlaTypes.Str())));
      log("translated sig " + field.fullName() + " into a VARIABLE " + f);
    }
    l.info(dump());
  }

  // todo: complete rewrite with new qname system
}
