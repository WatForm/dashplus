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
        System.out.println("In refineAbsModel().");
        assert (isCEValid == false);
        System.out.println("In refineAbsModel(): asserted that isCEValid is false.");
        String tfqnSpurious = spuriousTFQN.orElse(null);
        String spuriousTransSrc = spuriousSnapName.orElse(null);

        if (spuriousTransSrc != null) {

            // Construct the conjunction of B's that should be negated and conjuncted with guard of
            // spurious transition
            // From spurious abstract counterexample, take source of spurious transition and
            // conjunct
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

            AlloyExpr refineBody = AlloyNot(AlloyAndList(abvConjList));

            if (tfqnSpurious != null) {
                System.out.println("Refined the guard of " + tfqnSpurious);
                AlloyExpr origAbsGuard = absModel.whenR(tfqnSpurious);
                AlloyExpr refinedGuard = AlloyAnd(origAbsGuard, refineBody);
                absModel.setWhenR(tfqnSpurious, refinedGuard);
            } else {
                System.out.println("Refined the init of the abstract model.");
                absModel.addInit(refineBody);
            }
        }
    }
}
