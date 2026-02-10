package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.ArrayList;
import java.util.List;

public class Auxiliary {

    public static String getAncestorName(String sigName, AlloyModel am) {
        List<AlloySigPara> topLevelSigParas =
                filterBy(am.getParas(AlloySigPara.class), sp -> sp.isTopLevel());

        AlloySigPara p = topLevelSigParas.get(0);

        return "";
    }

    public static List<String> getAllSigNames(AlloyModel am) {

        List<String> answer = new ArrayList<>();

        return mapBy(am.getParas(AlloySigPara.class), sigPara -> sigPara.qnames.get(0).toString());
    }

    public static List<String> getTopLevelSigNames(AlloyModel am) {
        return mapBy(
                filterBy(am.getParas(AlloySigPara.class), sigPara -> sigPara.isTopLevel()),
                sigPara -> sigPara.qnames.get(0).toString());
    }
}
