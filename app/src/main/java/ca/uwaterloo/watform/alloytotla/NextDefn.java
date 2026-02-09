package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.NEXT;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDefn;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaUnchanged;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class NextDefn {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {
        List<String> sigNames =
                mapBy(
                        alloyModel.getParas(AlloySigPara.class),
                        sigPara -> sigPara.qnames.get(0).toString());

        List<TlaVar> unchanged = new ArrayList<>();

        sigNames.forEach(sigName -> unchanged.add(new TlaVar(sigName)));

        tlaModel.addDefn(TlaDefn(NEXT, TlaUnchanged(unchanged)));
    }
}
