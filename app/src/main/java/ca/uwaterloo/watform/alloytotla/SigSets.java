package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.repeatedAnd;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.repeatedUnion;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.sigSet;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.SIG_SETS_PRIMED;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaConst;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDefn;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class SigSets {

    // links each signature variable to the type of the top-level ancestor set
    // let S be a sig with top-level ancestors A1, A2, A3 ...

    /*
    sig_set_unprimed == S \in A1_set \\union A2_set \\union A3_set ...
    					/\ (and so on for each sig)

    same for sig_set_primed
    */
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {
        tlaModel.addDefn(
                TlaDefn(
                        SIG_SETS_PRIMED,
                        repeatedAnd(
                                mapBy(
                                        Auxiliary.getAllSigNames(alloyModel),
                                        sn ->
                                                TlaVar(sn)
                                                        .IN(
                                                                repeatedUnion(
                                                                        mapBy(
                                                                                Auxiliary
                                                                                        .getAncestorsNames(
                                                                                                sn,
                                                                                                alloyModel),
                                                                                asn ->
                                                                                        TlaConst(
                                                                                                sigSet(
                                                                                                        asn)))))))));

        tlaModel.addDefn(
                TlaDefn(
                        SIG_SETS_PRIMED,
                        repeatedAnd(
                                mapBy(
                                        Auxiliary.getAllSigNames(alloyModel),
                                        sn ->
                                                TlaVar(sn)
                                                        .PRIME()
                                                        .IN(
                                                                repeatedUnion(
                                                                        mapBy(
                                                                                Auxiliary
                                                                                        .getAncestorsNames(
                                                                                                sn,
                                                                                                alloyModel),
                                                                                asn ->
                                                                                        TlaConst(
                                                                                                sigSet(
                                                                                                        asn)))))))));
    }
}
