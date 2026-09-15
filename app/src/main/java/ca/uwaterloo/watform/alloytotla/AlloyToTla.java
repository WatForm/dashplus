package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class AlloyToTla extends StdLibsA2T {

  public AlloyToTla(AlloyModel alloyModel, boolean verbose, boolean debug) {
    super(alloyModel, verbose, debug);
  }

  public static TlaModel getBlankModel(String moduleName) {
    return new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));
  }

  public TlaModel translate(String baseName, int cmdNum) {

    // List<AlloyCmdPara> paras = alloyModel.allCmdParas();

    // List<AlloyCmdPara.CommandDecl> cmdDecls = new ArrayList<>();
    // for (var p : paras) {
    //   cmdDecls.addAll(p.cmdDecls);
    // }

    // List<TlaModel> tlaModels = new ArrayList<>();

    // int ct = 0;
    // for (var cmdDecl : cmdDecls) {
    //   String name = ct == 0 ? baseName : baseName + ct;
    //   TlaModel tlaModel = getBlankModel(name);
    //   translate(tlaModel, cmdDecl);
    //   tlaModels.add(tlaModel);
    //   ct += 1;
    // }

    var answer = new TlaModel(baseName, new TlaAppl(INIT), new TlaAppl(NEXT));
    translate(answer, cmdNum);

    return answer;
  }

  public void translate(TlaModel tlaModel, int cmdNum) {

    // l.info("chosen command scope profile " + alloyModel.getCmdScopeProfile(cmdNum).toString());
    l.info("chosen command body: " + alloyModel.getCmdFormula(cmdNum).toString());

    addStdLibsTla(tlaModel);
    addSigVars(tlaModel);
    addFieldVars(tlaModel);
    // addBoilerplate(tlaModel);
    // addStdLibsAlloy(tlaModel, cmdDecl);
    // addPredicatesFunctions(tlaModel);
    addSigHierarchy(tlaModel);
    // addFieldTypes(tlaModel);
    addSigConstraints(tlaModel);
    // addFacts(tlaModel);
    // addCommand(tlaModel, cmdDecl);
    addInitDefn(tlaModel);
    addNextDefn(tlaModel);
  }
}
