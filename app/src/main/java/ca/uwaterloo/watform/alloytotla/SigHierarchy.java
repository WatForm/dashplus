package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

public class SigHierarchy {

    /*

    let S be a sig
    if S is top-level, we get
    S \in SUBSET S_set
    if S has parents P1 P2 ...
    S \in SUBSET (P1 \\union P2 ...)
    these clauses are all joined
    to do this, the set of all sigs needs to be topologically sorted


    */

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<String> sortedSigs = sortedSigs(alloyModel);

        tlaModel.addDefn(
                TlaDefn(
                        SIG_SETS_UNPRIMED,
                        repeatedAnd(mapBy(sortedSigs, sn -> sigSetClause(sn, alloyModel, false)))));

        tlaModel.addDefn(
                TlaDefn(
                        SIG_SETS_PRIMED,
                        repeatedAnd(mapBy(sortedSigs, sn -> sigSetClause(sn, alloyModel, true)))));
    }

    private static TlaExp sigSetClause(String sn, AlloyModel alloyModel, boolean primed) {
        TlaExp v = primed ? TlaVar(sn).PRIME() : TlaVar(sn);
        if (Auxiliary.isTopLevelSig(sn, alloyModel))
            return v.IN(TlaSubsetUnary(TlaConst(sigSet(sn))));
        else
            return v.IN(
                    TlaSubsetUnary(
                            repeatedUnion(
                                    mapBy(
                                            Auxiliary.getParentNames(sn, alloyModel),
                                            psn -> TlaVar(psn)))));
    }

    public static List<String> sortedSigs(AlloyModel alloyModel) {
        List<String> answer = Auxiliary.getTopLevelSigNames(alloyModel);

        /*
        algorithm:
        in each step:
        for all sigs S: if all parents of S are in answer and S is not, then S is added
        once no changes in answer's size is detected, the steps stop

        finally, the following property holds:
        for all sigs S: all of its parents lie before it in the list
        */

        int oldSize;
        do {
            oldSize = answer.size();
            Auxiliary.getAllSigNames(alloyModel)
                    .forEach(
                            sn -> {
                                if (answer.containsAll(Auxiliary.getParentNames(sn, alloyModel)))
                                    if (!answer.contains(sn)) answer.add(sn);
                            });
        } while (oldSize != answer.size());

        return answer;
    }
}
