package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

public class PredFunA2T extends NextA2T {
  public PredFunA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
    super(alloyModel, verbose, debug);
  }

  protected void addPredicatesFunctions(TlaModel tlaModel) {

    tlaModel.addComment("Predicates and functions", verbose);

    for (var p : alloyModel.allPreds()) 
    {

      // TlaExp body = translateSnippet(p.block);
      // List<TlaVar> args = mapBy(p.arguments, decl -> TlaVar(decl.getName()));
      // TlaDefn defn = new TlaDefn(new TlaDecl(tlaQname(p), args), body);
      // tlaModel.addDefn(defn);
      log("predicate detected: " + p.fullName());
      // log(p.toString() + "\n↓\n" + defn.toTLAPlusSnippetCore());
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
