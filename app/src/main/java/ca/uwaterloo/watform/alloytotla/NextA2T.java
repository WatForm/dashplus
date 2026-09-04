package ca.uwaterloo.watform.alloytotla;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class NextA2T extends FieldsA2T {
	protected void addNextDefn(TlaModel tlaModel) {

    tlaModel.addComment("Next", verbose);

    List<TlaVar> unchanged = new ArrayList<>();

    for (var s : alloyModel.allSigs()) unchanged.add(TlaVar(s));
    for (var f : alloyModel.allFields()) unchanged.add(TlaVar(f));

    tlaModel.addDefn(TlaDefn(NEXT, TlaUnchanged(unchanged)));
  }
}
