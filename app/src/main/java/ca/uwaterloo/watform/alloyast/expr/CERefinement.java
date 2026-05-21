package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.io.*;
import java.util.*;

public class CERefinement extends CEValidation {

    public CERefinement(DashModel input) {
        super(input);
    }

    public CERefinement(DashModel input, int n) {
        super(input, n);
    }

    public void refineAbsModel() {
        assert (super.isCEValid == false);

        String tfqnSpurious = spuriousTFQN.orElse(null);
        assert (tfqnSpurious != null);

        String spuriousTransSrc = spuriousSnapName.orElse(null);
        assert (spuriousTransSrc != null);

        AlloyExpr origAbsGuard = absModel.whenR(tfqnSpurious);

        // Construct the conjunction of B's that should be negated and conjuncted with guard of
        // spurious transition
        // From spurious abstract counterexample, take source of spurious transition and conjunct
        // all of its B values

        List<AlloyExpr> abvConjList = new ArrayList<>();
        HashMap<String, String> varVals = snapshotVarVals.get(spuriousTransSrc);
        for (String v : varVals.keySet()) {
            AlloyExpr abv = getVarDashRef(getABVfqn(v));
            if (varVals.get(v).contains("True")) {
                abvConjList.add(dsl.AlloyIsTrue(abv));
            } else {
                abvConjList.add(dsl.AlloyIsFalse(abv));
            }
        }

        AlloyExpr refinedGuard = AlloyAnd(origAbsGuard, AlloyNot(AlloyAndList(abvConjList)));
        absModel.setWhenR(tfqnSpurious, refinedGuard);
    }
}
