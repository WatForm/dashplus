package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class FieldsA2T extends FactsA2T {

  public FieldsA2T(
      AlloyModel alloyModel,
      Scheme scheme,
      Optimization optimization,
      boolean verbose,
      boolean debug) {
    super(alloyModel, scheme, optimization, verbose, debug);
  }

  public void addFieldVars(TlaModel tlaModel) {

    for (Qname f : alloyModel.allFieldQnames()) {

      tlaModel.addVar(TlaVar(tlaQname(f)), relationType());
      log("translated field " + f.fullName() + " into a VARIABLE " + tlaQname(f));
    }
    l.info(dump());
  }

  public void addFieldTypeConstraints(TlaModel tlaModel) {
    for (Qname f : alloyModel.allFieldQnames()) {
      log("field expr of " + f.fullName() + " is " + alloyModel.fieldExpr(f));
      log("field product of " + f.fullName() + " is " + alloyModel.fieldProduct(f));
    }
    l.info(dump());
  }
}
