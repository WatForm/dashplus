package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.SIG_SETS_PRIMED;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.SIG_SETS_UNPRIMED;
import static ca.uwaterloo.watform.alloytotla.Boilerplate.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;
import java.util.List;

public class SigHierarchy {

    /*

    let S be a sig
    if S is top-level, we get
    S \in SUBSET {<<x>> : x \in S_set}
    if S has parents P1 P2 ...
    S \in SUBSET (P1 \\union P2 ...)
    these clauses are all joined
    to do this, the set of all sigs needs to be topologically sorted


    */

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<String> sortedSigs = alloyModel.topoSortedSigs();

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
        if (alloyModel.isTopLevelSig(sn))
            return v.IN(
                    TlaSubsetUnary(
                            TlaSetMap(
                                    TlaQuantOpHead(X(), TlaConst(sigSet(sn))),
                                    TlaTuple(Arrays.asList(X())))));
        else
            return v.IN(
                    TlaSubsetUnary(
                            repeatedUnion(
                                    mapBy(
                                            alloyModel.allParentsOfSig(sn),
                                            psn -> primed ? TlaVar(psn).PRIME() : TlaVar(psn)))));
    }
}
