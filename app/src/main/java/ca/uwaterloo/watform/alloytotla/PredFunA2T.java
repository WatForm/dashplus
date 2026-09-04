package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import java.util.List;

import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class PredFunA2T extends NextA2T {
	protected void addPredicatesFunctions(TlaModel tlaModel) {

    tlaModel.addComment("Predicates and functions", verbose);

    for (var p : alloyModel.allPredParas()) {
      TlaExp body = translateSnippet(p.block);
      List<TlaVar> args = mapBy(p.arguments, decl -> TlaVar(decl.getName()));
      TlaDefn defn = new TlaDefn(new TlaDecl(p.getName(), args), body);
      tlaModel.addDefn(defn);
      log("predicate detected: " + p.getName());
      log(p.toString() + "\n↓\n" + defn.toTLAPlusSnippetCore());
    }

    for (var p : alloyModel.allFunParas()) {
      TlaExp body = translateSnippet(p.block);
      List<TlaVar> args = mapBy(p.arguments, decl -> TlaVar(decl.getName()));
      TlaDefn defn = new TlaDefn(new TlaDecl(p.getName(), args), body);
      tlaModel.addDefn(defn);
      log("function detected: " + p.getName());
      log(p.toString() + "\n↓\n" + defn.toTLAPlusSnippetCore());
    }

    l.info(dump());
  }
}
