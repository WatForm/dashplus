package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaAppl;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class AlloyToTla extends StdLibsA2T {

    public static TlaModel getBlankModel(String moduleName) {
        return new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));
    }

    public AlloyToTla(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    public List<TlaModel> translate(String baseName) {

        List<AlloyCmdPara> paras = alloyModel.allCmdParas();

        List<AlloyCmdPara.CommandDecl> cmdDecls = new ArrayList<>();
        for (var p : paras) {
            cmdDecls.addAll(p.cmdDecls);
        }

        List<TlaModel> tlaModels = new ArrayList<>();

        int ct = 0;
        for (var cmdDecl : cmdDecls) {
            String name = ct == 0 ? baseName : baseName + ct;
            TlaModel tlaModel = getBlankModel(name);
            translate(tlaModel, cmdDecl);
            tlaModels.add(tlaModel);
            ct += 1;
        }

        return tlaModels;
    }

    public void translate(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

        l.info(TlaAppl("test").toTLAPlusSnippetCore());

        addStdLibs(tlaModel);
        addSigConsts(tlaModel);
        addSigVars(tlaModel);
        addFieldVars(tlaModel);

        tlaModel.addComment("translation macros", verbose);
        addBoilerplate(tlaModel);

        tlaModel.addComment("standard alloy modules", verbose);
        orderingModule(tlaModel);

        tlaModel.addComment("Predicates and functions", verbose);
        addPredicatesFunctions(tlaModel);

        tlaModel.addComment("signature hierarchy", verbose);
        addSigHierarchy(tlaModel);

        tlaModel.addComment("field types", verbose);
        addFieldTypes(tlaModel);

        tlaModel.addComment("signature constraints", verbose);
        addSigConstraints(tlaModel);

        // tlaModel.addComment("scope constraints", verbose);
        // addScopeConstraints(tlaModel, cmdDecl);

        tlaModel.addComment("facts", verbose);
        addFacts(tlaModel);

        tlaModel.addComment("Init and Next", verbose);
        addInitDefn(tlaModel, cmdDecl);
        addNextDefn(tlaModel);
    }
}
