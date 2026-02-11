package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.NULL_SET;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.repeatedAnd;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.repeatedUnion;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.sigConstraint;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.ALL_SIG_CONSTRAINTS;
import static ca.uwaterloo.watform.alloytotla.Boilerplate.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.*;

public class SigConstraints {
    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<TlaAppl> explicitConstraints = new ArrayList<>();

        Auxiliary.getAllSigNames(alloyModel)
                .forEach(
                        sig -> {
                            List<TlaExp> constraints = constraints(sig, alloyModel);
                            if (constraints.size() != 0) {
                                tlaModel.addDefn(
                                        TlaDefn(sigConstraint(sig), repeatedAnd(constraints)));
                                explicitConstraints.add(TlaAppl(sigConstraint(sig)));
                            }
                        });

        tlaModel.addDefn(TlaDefn(ALL_SIG_CONSTRAINTS, repeatedAnd(explicitConstraints)));
    }

    private static List<TlaExp> constraints(String sig, AlloyModel alloyModel) {
        List<TlaExp> constraints = new ArrayList<>();

        if (Auxiliary.isOneSig(sig, alloyModel)) constraints.add(_ONE(TlaVar(sig)));
        if (Auxiliary.isLoneSig(sig, alloyModel)) constraints.add(_LONE(TlaVar(sig)));
        if (Auxiliary.isSomeSig(sig, alloyModel)) constraints.add(_SOME(TlaVar(sig)));

        List<String> extendsChildNames = Auxiliary.getExtendsChildNames(sig, alloyModel);

        // pairwise disjoint sets for sigs that extend the same sig
        int n = extendsChildNames.size();
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                TlaVar si = TlaVar(extendsChildNames.get(i));
                TlaVar sj = TlaVar(extendsChildNames.get(j));
                // Si \intersect Sj = {}  (i < j)
                constraints.add(si.INTERSECTION(sj).EQUALS(NULL_SET()));
            }

        // abstract sigs
        if (Auxiliary.isAbstractSig(sig, alloyModel))
            constraints.add(
                    TlaVar(sig)
                            .EQUALS(repeatedUnion(mapBy(extendsChildNames, ecn -> TlaVar(ecn)))));

        return constraints;
    }
}
