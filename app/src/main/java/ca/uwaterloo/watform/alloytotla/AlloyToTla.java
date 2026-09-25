package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.tlamodel.TreeShaker;
import java.time.LocalDateTime;

public class AlloyToTla extends StdLibsA2T {

  public AlloyToTla(
      AlloyModel alloyModel,
      Scheme scheme,
      Optimization optimization,
      boolean verbose,
      boolean debug) {
    super(alloyModel, scheme, optimization, verbose, debug);
  }

  public static TlaModel getBlankModel(String moduleName) {
    return new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));
  }

  public TlaModel translate(String baseName, int cmdNum) {

    var answer = new TlaModel(baseName, new TlaAppl(INIT), new TlaAppl(NEXT));
    translate(answer, cmdNum);

    if (optimization.syntacticTreeShaking()) return TreeShaker.removeUnusedDefns(answer);

    return answer;
  }

  public void translate(TlaModel tlaModel, int cmdNum) {

    tlaModel.addComment("Translated at " + LocalDateTime.now().toString(), true);
    addStdLibsTla(tlaModel);
    addSigVars(tlaModel);
    addFieldVars(tlaModel);
    addBoilerplate(tlaModel);
    // addStdLibsAlloy(tlaModel, cmdDecl);
    addPredicatesFunctions(tlaModel);
    addSigHierarchy(tlaModel);
    addFieldTypeConstraints(tlaModel);
    addSigConstraints(tlaModel);
    // addFacts(tlaModel);
    addCommand(tlaModel, cmdNum);
    addScopes(tlaModel, cmdNum);
    addInitDefn(tlaModel);
    addNextDefn(tlaModel);
  }
}
