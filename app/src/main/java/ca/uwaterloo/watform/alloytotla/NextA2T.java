package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class NextA2T extends InitA2T {
  
  

  public NextA2T(AlloyModel alloyModel, Optimization optimization, boolean verbose, boolean debug) {
    super(alloyModel, optimization, verbose, debug);
  }

  protected void addNextDefn(TlaModel tlaModel) {

    tlaModel.addComment("Next", verbose);

    List<TlaVar> unchanged = new ArrayList<>();

    for (var s : alloyModel.allSigQnames()) unchanged.add(TlaVar(tlaQname(s)));
    for (var f : alloyModel.allFieldQnames()) unchanged.add(TlaVar(tlaQname(f)));

    tlaModel.addDefn(TlaDefn(NEXT, TlaUnchanged(unchanged)));
  }
}
