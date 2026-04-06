package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class AlloyToTla extends StdLibsA2T {

    public static TlaModel getBlankModel(String moduleName) {
        return new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));
    }

    public AlloyToTla(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
    }

    public void translate() {
        addStdLibs();
        addSigConsts();
        addSigVars();
        addFieldVars();

        tlaModel.addComment("translation macros", verbose);
        addBoilerplate();

        tlaModel.addComment("signature hierarchy", verbose);
        addSigHierarchy();

        tlaModel.addComment("signature constraints", verbose);
        addSigConstraints();

        tlaModel.addComment("facts", verbose);
        addFacts();

        tlaModel.addComment("Init and Next", verbose);
        addInitDefn();
        addNextDefn();
    }
}
