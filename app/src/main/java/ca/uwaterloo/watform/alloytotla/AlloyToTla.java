package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class AlloyToTla {

    public static void translate(
            AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        StdLibDefns.translate(alloyModel, tlaModel);

        Logger log = CustomLoggerFactory.make("AlloyToTla", debug);
        log.info("top-level Alloy to Tla translate called, translating to " + tlaModel.name);

        tlaModel.addComment("Translation macros", verbose);
        Boilerplate.translate(alloyModel, tlaModel);

        SigConsts.translate(alloyModel, tlaModel);
        SigVars.translate(alloyModel, tlaModel);
        FieldVars.translate(alloyModel, tlaModel);

        tlaModel.addComment(
                "topological sort on signatures: " + alloyModel.topoSortedSigs(), verbose);
        SigHierarchy.translate(alloyModel, tlaModel);

        tlaModel.addComment("signature constraints", verbose);
        SigConstraints.translate(alloyModel, tlaModel);

        tlaModel.addComment("facts", verbose);
        Facts.translate(alloyModel, tlaModel, verbose);

        tlaModel.addComment("INIT relation", verbose);
        InitDefn.translate(alloyModel, tlaModel);

        tlaModel.addComment("NEXT relation", verbose);
        NextDefn.translate(alloyModel, tlaModel);

        log.info("translation complete for " + tlaModel.name);
    }

    public static TlaModel translate(
            AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {

        Logger l = CustomLoggerFactory.make("AlloyToTla", debug);

        l.info("making TLA model with name: " + moduleName);

        TlaModel tlaModel = new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));

        translate(alloyModel, tlaModel, verbose, debug);

        return tlaModel;
    }
}
