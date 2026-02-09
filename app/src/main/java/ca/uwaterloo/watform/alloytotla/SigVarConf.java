package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.filterBy;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaast.tlaliterals.*;
import ca.uwaterloo.watform.tlaast.tlanaryops.*;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class SigVarConf {

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<String> names =
                mapBy(
                        filterBy(
                                alloyModel.getParas(AlloySigPara.class),
                                sigPara -> sigPara.isTopLevel()),
                        sigPara -> sigPara.qnames.get(0).toString());

        for (String name : names) {
            makeSigSet(name, alloyModel, tlaModel, 4);
        }
    }

    public static void makeSigSet(String sigName, AlloyModel alloyModel, TlaModel tlaModel, int n) {
        // S_set = {"S$0","S$1","S$2"...}
        List<TlaStringLiteral> strings = new ArrayList<>();
        for (int i = 0; i < n; i++) strings.add(TlaStringLiteral(sigSetString(sigName, i)));

        tlaModel.addConst(TlaConst(sigName), TlaSet(strings));
    }

    public static String sigSetString(String sigName, int i) {
        return sigName + AlloyToTlaStrings.DOLLAR + i;
    }
}
