package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class AlloyToTla {
    public static TlaModel translate(
            AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {

        TlaModel tlaModel = new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));

        tlaModel.addComment("Translation macros", verbose);
        Boilerplate.translate(tlaModel);

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

        System.out.println("----------------");

        Auxiliary.getAllSigNames(alloyModel)
                .forEach(
                        sn -> {
                            System.out.println("sig name: " + sn);
                            System.out.println(
                                    "top-level: " + Auxiliary.isTopLevelSig(sn, alloyModel));
                            System.out.println(
                                    "extends parent: "
                                            + Auxiliary.getExtendsParent(sn, alloyModel));
                            System.out.println(
                                    "in parents: " + Auxiliary.getInParents(sn, alloyModel));
                            System.out.println(
                                    "abstract: " + Auxiliary.isAbstractSig(sn, alloyModel));
                            System.out.println("one: " + Auxiliary.isOneSig(sn, alloyModel));
                            System.out.println("lone: " + Auxiliary.isLoneSig(sn, alloyModel));
                            System.out.println("some: " + Auxiliary.isSomeSig(sn, alloyModel));
                            System.out.println(
                                    "ancestors: " + Auxiliary.getAncestorsNames(sn, alloyModel));
                        });

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
