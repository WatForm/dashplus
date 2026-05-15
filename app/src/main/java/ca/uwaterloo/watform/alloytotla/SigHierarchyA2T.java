package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.SIG_SETS_PRIMED;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.SIG_SETS_UNPRIMED;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

public class SigHierarchyA2T extends SigConstsA2T {

    /*
    let S be a sig
    if S is top-level, we get
    S \in SUBSET {<<x>> : x \in S_set}
    if S has parents P1 P2 ...
    S \in SUBSET (P1 \\union P2 ...)
    these clauses are all joined
    to do this, the set of all sigs needs to be topologically sorted
    */

    public SigHierarchyA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected void addSigHierarchy(TlaModel tlaModel) {
        List<String> sortedSigs = alloyModel.topoSortedSigs();
        List<String> sortedNonTopLevelSigs =
                filterBy(sortedSigs, s -> !alloyModel.isTopLevelSig(s));

        tlaModel.addDefn(
                TlaDefn(
                        SIG_SETS_UNPRIMED,
                        repeatedAnd(
                                mapBy(
                                        sortedNonTopLevelSigs,
                                        sn -> sigSetClauseNonTopLevel(sn, false)))));

        tlaModel.addDefn(
                TlaDefn(
                        SIG_SETS_PRIMED,
                        repeatedAnd(
                                mapBy(
                                        sortedNonTopLevelSigs,
                                        sn -> sigSetClauseNonTopLevel(sn, true)))));
    }

    private TlaExp sigSetClauseNonTopLevel(String signame, boolean primed) {

        l.info(signame + ":");
        l.info("extends parents: " + alloyModel.extendsParentOfSig(signame));
        l.info("in-parents: " + alloyModel.inParentsOfSig(signame));

        TlaExp v = primed ? TlaVar(signame).PRIME() : TlaVar(signame);
        List<TlaExp> parents =
                mapBy(
                        alloyModel.allParentsOfSig(signame),
                        p -> primed ? TlaVar(p).PRIME() : TlaVar(p));
        return v.IN(TlaSubsetUnary(repeatedUnion(parents)));
    }
}
