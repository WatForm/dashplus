package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaAppl;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class AlloyToTla extends StdLibsTlaA2T {

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

        l.info("chosen command: " + cmdDecl.toString());

        addStdLibsTla(tlaModel);
        addSigVars(tlaModel);
        addFieldVars(tlaModel);
        addBoilerplate(tlaModel);
        addStdLibsAlloy(tlaModel, cmdDecl);
        addPredicatesFunctions(tlaModel);
        addSigHierarchy(tlaModel);
        addFieldTypes(tlaModel);
        addSigConstraints(tlaModel);
        addFacts(tlaModel);
        addCommand(tlaModel, cmdDecl);
        addInitDefn(tlaModel, cmdDecl);
        addNextDefn(tlaModel);
    }
}
