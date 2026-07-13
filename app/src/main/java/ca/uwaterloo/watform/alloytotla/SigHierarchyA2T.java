package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

public class SigHierarchyA2T extends SigConstraintsA2T {

    /*
    let S be a non-top-level sig
    if S has parents P1 P2 ...
    we get
    S \in SUBSET (P1 \\union P2 ...)
    these clauses are all joined
    to do this, the set of all sigs needs to be topologically sorted
    */

    public SigHierarchyA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected void addSigHierarchy(TlaModel tlaModel) {

        tlaModel.addComment("signature hierarchy", verbose);

        List<String> sortedSigs = alloyModel.topoSortedSigs();
        List<String> sortedNonTopLevelSigs =
                filterBy(sortedSigs, s -> !alloyModel.isTopLevelSig(s));

        log("toposorted non-top-level sigs: " + sortedNonTopLevelSigs);
        for (var s : sortedNonTopLevelSigs) {
            log("sig " + s + " has parents: " + alloyModel.allParents(s));
        }

        var sigSetClausesUnprimed =
                repeatedAnd(mapBy(sortedNonTopLevelSigs, sn -> sigSetClauseNonTopLevel(sn, false)));
        var sigSetClausesPrimed =
                repeatedAnd(mapBy(sortedNonTopLevelSigs, sn -> sigSetClauseNonTopLevel(sn, true)));

        tlaModel.addDefn(TlaDefn(SIG_SETS_UNPRIMED, sigSetClausesUnprimed));

        tlaModel.addDefn(TlaDefn(SIG_SETS_PRIMED, sigSetClausesPrimed));

        l.info(dump());
    }

    private TlaExp sigSetClauseNonTopLevel(String signame, boolean primed) {

        TlaExp v = primed ? TlaVar(signame).PRIME() : TlaVar(signame);
        List<TlaExp> parents =
                mapBy(alloyModel.allParents(signame), p -> primed ? TlaVar(p).PRIME() : TlaVar(p));
        return v.IN(TlaSubsetUnary(repeatedUnion(parents)));
    }
}
