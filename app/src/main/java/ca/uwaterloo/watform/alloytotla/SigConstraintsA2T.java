package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class SigConstraintsA2T extends CommandDefnA2T {

    public SigConstraintsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected void addSigConstraints(TlaModel tlaModel) {

        List<TlaAppl> explicitConstraints = new ArrayList<>();

        alloyModel
                .allSigParas()
                .forEach(
                        sigPara -> {
                            List<TlaExp> constraints = constraints(sigPara, alloyModel);
                            if (constraints.size() != 0) {
                                tlaModel.addDefn(
                                        TlaDefn(
                                                sigConstraint(sigPara.getName()),
                                                repeatedAnd(constraints)));
                                explicitConstraints.add(TlaAppl(sigConstraint(sigPara.getName())));
                            }
                        });

        tlaModel.addDefn(TlaDefn(ALL_SIG_CONSTRAINTS, repeatedAnd(explicitConstraints)));
    }

    private List<TlaExp> constraints(AlloySigPara sigPara, AlloyModel alloyModel) {
        List<TlaExp> constraints = new ArrayList<>();

        sigPara.block.ifPresent(
                b -> {
                    // universal quantification for facts
                    constraints.add(new AlloyToTlaExprVis().visit(b));
                });

        String sigName = sigPara.getName();
        if (alloyModel.isOneSig(sigName)) constraints.add(_ONE(TlaVar(sigName)));
        if (alloyModel.isLoneSig(sigName)) constraints.add(_LONE(TlaVar(sigName)));
        if (alloyModel.isSomeSig(sigName)) constraints.add(_SOME(TlaVar(sigName)));

        List<String> extendsChildNames = alloyModel.extendsChildren(sigName);

        // pairwise disjoint sets for sigs that extend the same sig
        int n = extendsChildNames.size();
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                TlaVar si = TlaVar(extendsChildNames.get(i));
                TlaVar sj = TlaVar(extendsChildNames.get(j));
                // Si \intersect Sj = {}  (i < j)
                constraints.add(si.INTERSECTION(sj).EQUALS(TlaNullSet()));
            }

        // abstract sigs
        if (alloyModel.isAbstractSig(sigName))
            constraints.add(
                    TlaVar(sigName)
                            .EQUALS(repeatedUnion(mapBy(extendsChildNames, ecn -> TlaVar(ecn)))));

        return constraints;
    }
}
