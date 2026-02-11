package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara.Qual;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Auxiliary {

    public static String getQname(AlloySigPara asp) {
        return asp.qnames.get(0).toString();
    }

    public static List<AlloySigPara> getAllSigParas(AlloyModel am) {
        return am.getParas(AlloySigPara.class);
    }

    public static AlloySigPara getSigParaWithName(String signame, AlloyModel am) {
        return filterBy(getAllSigParas(am), s -> signame.equals(getQname(s))).get(0);
    }

    public static boolean isTopLevelSig(String signame, AlloyModel am) {
        return getSigParaWithName(signame, am).isTopLevel();
    }

    public static boolean isAbstractSig(String signame, AlloyModel am) {
        return getSigParaWithName(signame, am).quals.contains(Qual.ABSTRACT);
    }

    public static boolean isOneSig(String signame, AlloyModel am) {
        return getSigParaWithName(signame, am).quals.contains(Qual.ONE);
    }

    public static boolean isSomeSig(String signame, AlloyModel am) {
        return getSigParaWithName(signame, am).quals.contains(Qual.SOME);
    }

    public static boolean isLoneSig(String signame, AlloyModel am) {
        return getSigParaWithName(signame, am).quals.contains(Qual.LONE);
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
        AtomicReference<String> answer = new AtomicReference<>(null);

        getSigParaWithName(signame, am)
                .rel
                .ifPresent(
                        x -> {
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

    public static List<String> getParentNames(String signame, AlloyModel am) {
        String extendsParent = getExtendsParent(signame, am);
        List<String> inParents = getInParents(signame, am);
        if (extendsParent != null) inParents.add(extendsParent);
        return inParents;
    }

    public static List<String> getExtendsChildNames(String signame, AlloyModel am) {
        // has to be signame.equals, not the other way around, because the result could be null
        return filterBy(getAllSigNames(am), s -> signame.equals(getExtendsParent(s, am)));
    }

    public static List<String> getAncestorsNames(String sigName, AlloyModel am) {
        if (isTopLevelSig(sigName, am)) return Arrays.asList(sigName);

        List<String> ancestors = new ArrayList<>();

        // recursive step
        getParentNames(sigName, am).forEach(sn -> ancestors.addAll(getAncestorsNames(sn, am)));

        // cleanup
        List<String> answer = new ArrayList<>();
        ancestors.forEach(
                sn -> {
                    if (!answer.contains(sn)) answer.add(sn);
                });
        return answer;
    }

    public static List<String> getFieldNames(String signame, AlloyModel am) {
        return mapBy(
                getSigParaWithName(signame, am).fields, flist -> flist.qnames.get(0).toString());
    }
}
