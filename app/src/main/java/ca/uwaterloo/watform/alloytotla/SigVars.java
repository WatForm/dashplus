package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

public class SigVars {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {
        List<String> names =
                mapBy(
                        alloyModel.getParas(AlloySigPara.class),
                        sigPara -> sigPara.qnames.get(0).toString());

        for (String sigName : names) tlaModel.addVar(TlaVar(sigName));
    }
}
