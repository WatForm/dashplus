package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;


import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class AlloyToTla extends BoilerplateA2T {

  public static TlaModel getBlankModel(String moduleName) {
    return new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));
  }

  public AlloyToTla(AlloyModel alloyModel, boolean verbose, boolean debug) {
    super(alloyModel, verbose, debug);
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

    return new TlaModel(baseName, new TlaAppl(INIT), new TlaAppl(NEXT));
  }

  public void translate(TlaModel tlaModel, int cmdNum) {

	// l.info("chosen command scope profile " + alloyModel.getCmdScopeProfile(cmdNum).toString());
    l.info("chosen command body: " + alloyModel.getCmdFormula(cmdNum).toString());

    // addStdLibsTla(tlaModel);
    // addSigVars(tlaModel);
    // addFieldVars(tlaModel);
    // addBoilerplate(tlaModel);
    // addStdLibsAlloy(tlaModel, cmdDecl);
    // addPredicatesFunctions(tlaModel);
    // addSigHierarchy(tlaModel);
    // addFieldTypes(tlaModel);
    // addSigConstraints(tlaModel);
    // addFacts(tlaModel);
    // addCommand(tlaModel, cmdDecl);
    // addInitDefn(tlaModel, cmdDecl);
    // addNextDefn(tlaModel);
  }
}