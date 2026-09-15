package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.*;
import java.util.ArrayList;
import java.util.List;

public class FactsA2T extends CustomModulesA2T {
  public FactsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
    super(alloyModel, verbose, debug);
  }

  protected void addFacts(TlaModel tlaModel) {

    tlaModel.addComment("facts", verbose);

    List<String> factNames = new ArrayList<>();
    List<String> comments = new ArrayList<>();

    for (var f : alloyModel.allFactParas()) {
      /*
      String factName = generateFactName();
      factNames.add(factName);
      fp.qname.ifPresent(n -> comments.add(factName + " -> " + n));
      fp.strLit.ifPresent(str -> comments.add(factName + " -> " + str));

      tlaModel.addDefn(TlaDefn(factName, translateSnippet(fp.block)));
      */
    }

    tlaModel.addDefn(TlaDefn(ALL_FACTS, repeatedAnd(mapBy(factNames, fn -> TlaAppl(fn)))));

    comments.forEach(c -> tlaModel.addComment(c, verbose));
  }
}
