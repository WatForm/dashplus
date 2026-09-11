package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class NextA2T extends FieldsA2T {
  public NextA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
    super(alloyModel, verbose, debug);
  }

  protected void addNextDefn(TlaModel tlaModel) {

    tlaModel.addComment("Next", verbose);

    List<TlaVar> unchanged = new ArrayList<>();

    for (var s : alloyModel.allSigs()) unchanged.add(TlaVar(s));
    // for (var f : alloyModel.allFields()) unchanged.add(TlaVar(f));

    tlaModel.addDefn(TlaDefn(NEXT, TlaUnchanged(unchanged)));
  }
}
