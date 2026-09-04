package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class FactsA2T extends CustomModulesA2T {
	protected void addFacts(TlaModel tlaModel) {

    tlaModel.addComment("facts", verbose);

    List<String> factNames = new ArrayList<>();
    List<String> comments = new ArrayList<>();
    List<AlloyFactPara> factParas = alloyModel.allFactParas();

    for (var fp : factParas) {
      String factName = generateFactName();
      factNames.add(factName);
      fp.qname.ifPresent(n -> comments.add(factName + " -> " + n));
      fp.strLit.ifPresent(str -> comments.add(factName + " -> " + str));

      tlaModel.addDefn(TlaDefn(factName, translateSnippet(fp.block)));
    }

    tlaModel.addDefn(TlaDefn(ALL_FACTS, repeatedAnd(mapBy(factNames, fn -> TlaAppl(fn)))));

    comments.forEach(c -> tlaModel.addComment(c, verbose));
  }
}
