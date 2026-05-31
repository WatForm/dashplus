package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.exprvisitor.ReplaceExprVis;
import java.io.*;
import java.util.*;

public class CERefinement extends CEValidation {

    protected int numRefines = 0;
    protected Set<AlloyExpr> refinementFacts = new HashSet<>();

    public CERefinement(DashModel input) {
        super(input);
    }

    public CERefinement(DashModel input, int n) {
        super(input, n);
    }

    public void refineAbsModel() {
        if (spuriousSnapName != null) {
            System.out.println(
                    "In refineAbsModel(): the source of the spurious transition is "
                            + spuriousSnapName);
            System.out.println(
                    "In refineAbsModel(): FQN of the spurious transition is " + spuriousTFQN);
            // Construct the conjunction of B's that should be negated and conjuncted with guard of
            // spurious transition
            // From spurious abstract counterexample, take source of spurious transition and
            // conjunct
            // all of its B values

            List<AlloyExpr> abvConjList = new ArrayList<>();
            HashMap<String, String> varVals = snapshotVarVals.get(spuriousSnapName);
            for (String v : varVals.keySet()) {
                AlloyExpr abv = getVarDashRef(v);
                if (varVals.get(v).contains("True")) {
                    abvConjList.add(dsl.AlloyIsTrue(abv));
                } else {
                    abvConjList.add(dsl.AlloyIsFalse(abv));
                }
            }

            AlloyExpr refineBody = AlloyNot(AlloyAndList(abvConjList));

            if (spuriousTFQN != null) {
                AlloyExpr origAbsAct = absModel.doR(spuriousTFQN);
                AlloyExpr actConstraints =
                        (new ReplaceExprVis(
                                        AbstractBuildPA::isVarDashRef, AbstractBuildPA::makeNext))
                                .visit(refineBody);
                AlloyExpr refinedAct = AlloyAnd(origAbsAct, actConstraints);
                absModel.setDoR(spuriousTFQN, refinedAct);
                System.out.println("Refined the action of " + spuriousTFQN);

                refineBody = exprTranslator.translateExpr(refineBody);
                AlloyExpr takenExpr =
                        AlloyIn(AlloyVar(DashFQN.translateFQN(spuriousTFQN)), dsl.curTransTaken(0));
                refineBody = AlloyImplies(takenExpr, refineBody);
                List<AlloyDecl> decls = dsl.emptyDeclList();
                decls.add(dsl.curDecl());
                refineBody = AlloyAllVars(decls, refineBody);
                if (!refinementFacts.contains(refineBody)) {
                    absModel.addFact("refinement_" + String.valueOf(numRefines), refineBody);
                    System.out.println("In refineAbsModel: added a refinement fact.");
                    this.numRefines++;
                }
                System.out.println("Refined Model:\n\n" + absModel.toString());
            }
        } else {
            System.out.println(
                    "In refineAbsModel(): the source of the spurious transition is null.");
        }
    }
}
