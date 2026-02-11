package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Auxiliary {

    public static String getQname(AlloySigPara asp) {
        return asp.qnames.get(0).toString();
    }

    public static List<AlloySigPara> getAllSigParas(AlloyModel am)
    {
        return am.getParas(AlloySigPara.class);
    }

    public static AlloySigPara getSigParaWithName(String signame, AlloyModel am)
    {
        return filterBy(getAllSigParas(am), s -> signame.equals(getQname(s))).get(0);
    }

    public static boolean isTopLevelSig(String signame, AlloyModel am)
    {
        return getSigParaWithName(signame, am).isTopLevel();
    }

    

    public static List<String> getAllSigNames(AlloyModel am) {

        return mapBy(getAllSigParas(am), sigPara -> getQname(sigPara));
    }

    public static List<String> getTopLevelSigNames(AlloyModel am) {
        return mapBy(
                filterBy(getAllSigParas(am), sigPara -> sigPara.isTopLevel()),
                sigPara -> getQname(sigPara));
    }


    public static String getExtendsParent(String signame, AlloyModel am) {
        AtomicReference<String> answer = new AtomicReference<>("");

        getSigParaWithName(signame, am).rel.ifPresent(x -> {
            if (x instanceof AlloySigPara.Extends)
                answer.set(((AlloySigPara.Extends) x).sigRef.toString());
        });

        return answer.get();
    }

    public static List<String> getInParents(String signame, AlloyModel am) {
        List<String> answer = new ArrayList<>();
        List<AlloySigPara> sps = am.getParas(AlloySigPara.class);
        for (AlloySigPara sp : sps) {
            if (sp.qnames.get(0).toString().equals(signame))
                sp.rel.ifPresent(
                        x -> {
                            if (x instanceof AlloySigPara.In)
                                ((AlloySigPara.In) x)
                                        .sigRefs.forEach(y -> answer.add(y.toString()));
                        });
        }
        return answer;
    }

    public static String getAncestorName(String sigName, AlloyModel am) {
        List<AlloySigPara> topLevelSigParas =
                filterBy(am.getParas(AlloySigPara.class), sp -> sp.isTopLevel());

        AlloySigPara p = topLevelSigParas.get(0);

        return "";
    }
    
}
