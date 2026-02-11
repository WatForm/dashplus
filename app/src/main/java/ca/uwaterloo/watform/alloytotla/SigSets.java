package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class SigSets {

    // links each signature variable to the type of the top-level ancestor set
    // let S be a sig with top-level ancestors A1, A2, A3 ...

    /*
    sig_set_unprimed == S \in SUBSET (A1_set \\union A2_set \\union A3_set) ...
    					/\ (and so on for each sig)

    same for sig_set_primed
    */
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {
        tlaModel.addDefn(
                TlaDefn(
                        SIG_SETS_UNPRIMED,
                        repeatedAnd(
                                mapBy(
                                        Auxiliary.getAllSigNames(alloyModel),
                                        sn -> sigSetClause(sn, alloyModel, false)))));

        tlaModel.addDefn(
                TlaDefn(
                        SIG_SETS_PRIMED,
                        repeatedAnd(
                                mapBy(
                                        Auxiliary.getAllSigNames(alloyModel),
                                        sn -> sigSetClause(sn, alloyModel, true)))));
    }

    private static TlaExp sigSetClause(String sn, AlloyModel alloyModel, boolean primed) {
        TlaExp v = primed ? TlaVar(sn).PRIME() : TlaVar(sn);
        return v.IN(
                TlaSubsetUnary(
                        repeatedUnion(
                                mapBy(
                                        Auxiliary.getAncestorsNames(sn, alloyModel),
                                        asn -> TlaConst(sigSet(asn))))));
    }
}
