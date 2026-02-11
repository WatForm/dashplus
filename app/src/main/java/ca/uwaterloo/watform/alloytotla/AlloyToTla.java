package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class AlloyToTla {
    public static TlaModel translate(
            AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {

        TlaModel tlaModel = new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));

        tlaModel.addComment("Translation macros", verbose);
        Boilerplate.translate(alloyModel, tlaModel);

        /*
        alloyModel
                .getParas(AlloySigPara.class)
                .forEach(
                        v -> {
                            System.out.println("qnames" + v.qnames.toString());
                            System.out.println("rel:" + v.rel.toString());
                            System.out.println("block:" + v.block.toString());
                            System.out.println("fields:" + v.fields.toString());
                            System.out.println("quals:" + v.quals.toString());
                        });
         */

        SigVars.translate(alloyModel, tlaModel);
        SigConsts.translate(alloyModel, tlaModel);

        tlaModel.addComment(
                "topological sort on signatures: " + SigHierarchy.sortedSigs(alloyModel), verbose);
        SigHierarchy.translate(alloyModel, tlaModel);

        tlaModel.addComment("signature constraints", verbose);
        Sigs.translate(alloyModel, tlaModel);

        tlaModel.addComment("INIT relation", verbose);
        InitDefn.translate(alloyModel, tlaModel);
        tlaModel.addComment("NEXT relation", verbose);
        NextDefn.translate(alloyModel, tlaModel);

        return tlaModel;
    }
}
