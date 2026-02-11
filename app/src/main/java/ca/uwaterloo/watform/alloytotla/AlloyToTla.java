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

        alloyModel
                .getParas(AlloySigPara.class)
                .forEach(
                        v -> {
                            System.out.println("qnames" + v.qnames.toString());
                            System.out.println("rel:" + v.rel.toString());
                            v.rel.ifPresent(
                                    x -> System.out.println(((AlloySigPara.In) x).sigRefs.get(0)));
                            System.out.println("block:" + v.block.toString());
                            System.out.println("fields:" + v.fields.toString());
                            System.out.println("quals:" + v.quals.toString());

                            Auxiliary.getExtendsParent(v.qnames.get(0).toString(), alloyModel);
                        });

        SigVars.translate(alloyModel, tlaModel);
        SigVarConf.translate(alloyModel, tlaModel);

        InitDefn.translate(alloyModel, tlaModel);
        NextDefn.translate(alloyModel, tlaModel);

        return tlaModel;
    }
}
