package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.D2AStrings;
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

    protected AlloyExpr getABVConj(String snap, boolean nextFlag) {
        List<AlloyExpr> abvConjList = new ArrayList<>();
        HashMap<String, String> varVals = snapshotVarVals.get(snap);
        if (varVals != null) {
            for (String v : varVals.keySet()) {
                VarDashRef abv = new VarDashRef(DashFQN.fqn(absModel.rootName(), v), emptyList());
                AlloyExpr bv = abv;
                if (nextFlag) {
                    bv = abv.makeNext();
                }
                if (varVals.get(v).contains("True")) {
                    abvConjList.add(dsl.AlloyIsTrue(bv));
                } else if (varVals.get(v).contains("False")) {
                    abvConjList.add(dsl.AlloyIsFalse(bv));
                }
            }
        }
        return exprTranslator.translateExpr(AlloyAndList(abvConjList));
    }

    public void refineAbsAlloy() {
        if (spuriousTFQN != null) {
            System.out.println("In refineAbsAlloy,\nspur trans: " + spuriousTFQN);
            System.out.println("source of spur trans: " + spuriousSrcSnapName);
            System.out.println("dest of spur trans: " + spuriousDestSnapName);

            List<AlloyExpr> refinePredBody = new ArrayList<>();

            AlloyExpr takenExpr =
                    AlloyIn(AlloyVar(DashFQN.translateFQN(spuriousTFQN)), dsl.nextTransTaken(0));
            refinePredBody.add(takenExpr);
            refinePredBody.add(getABVConj(spuriousSrcSnapName, false));
            refinePredBody.add(getABVConj(spuriousDestSnapName, true));
            AlloyExpr body = AlloyNot(AlloyAndList(refinePredBody));
            String refPredName = "refinement_" + String.valueOf(numRefines);
            absAlloy.addPred(refPredName, dsl.curNextDecls(), List.of(body));
            numRefines++;

            AlloyPredPara smallStepPred = absAlloy.getPredPara(D2AStrings.smallStepName);
            AlloyBlock ssBlock = smallStepPred.block;
            List<AlloyExpr> ssExprs = new ArrayList<>();
            if (numRefines == 0) {
                ssExprs.add(new AlloyBlock(ssBlock));
                ssExprs.add(AlloyPredCall(refPredName, dsl.curNextVars()));
            } else {
                ssExprs.addAll(ssBlock.exprs);
                ssExprs.add(AlloyPredCall(refPredName, dsl.curNextVars()));
            }
            absAlloy.removePredPara(D2AStrings.smallStepName);
            absAlloy.addPred(D2AStrings.smallStepName, dsl.curNextDecls(), ssExprs);
        } else {
            System.out.println("No spurious transition exists");
        }
    }

    public void refineAbsModel() {
        if (spuriousSrcSnapName != null) {
            System.out.println(
                    "In refineAbsModel(): the source of the spurious transition is "
                            + spuriousSrcSnapName);
            System.out.println(
                    "In refineAbsModel(): FQN of the spurious transition is " + spuriousTFQN);
            // Construct the conjunction of B's that should be negated and conjuncted with guard of
            // spurious transition
            // From spurious abstract counterexample, take source of spurious transition and
            // conjunct
            // all of its B values

            List<AlloyExpr> abvConjList = new ArrayList<>();
            HashMap<String, String> varVals = snapshotVarVals.get(spuriousSrcSnapName);
            for (String v : varVals.keySet()) {
                AlloyExpr abv = getVarDashRef(v);
                if (varVals.get(v).contains("True")) {
                    abvConjList.add(dsl.AlloyIsTrue(abv));
                } else if (varVals.get(v).contains("False")) {
                    abvConjList.add(dsl.AlloyIsFalse(abv));
                }
            }

            AlloyExpr refineBody = AlloyNot(AlloyAndList(abvConjList));

            if (spuriousTFQN != null) {
                if (this.isFirstFail) {
                    AlloyExpr actConstraints =
                            (new ReplaceExprVis(
                                            AbstractBuildPA::isVarDashRef,
                                            AbstractBuildPA::makeNext))
                                    .visit(refineBody);
                    AlloyExpr origAct = absModel.doR(spuriousTFQN);
                    if (origAct != null) {
                        absModel.setDoR(
                                spuriousTFQN, AlloyAnd(absModel.doR(spuriousTFQN), actConstraints));
                    } else {
                        absModel.setDoR(spuriousTFQN, actConstraints);
                    }
                    refineBody = exprTranslator.translateExpr(refineBody);
                    AlloyExpr takenExpr =
                            AlloyIn(
                                    AlloyVar(DashFQN.translateFQN(spuriousTFQN)),
                                    dsl.curTransTaken(0));
                    refineBody = AlloyImplies(takenExpr, refineBody);
                    List<AlloyDecl> decls = dsl.emptyDeclList();
                    decls.add(dsl.curDecl());
                    refineBody = AlloyAllVars(decls, refineBody);
                    if (!refinementFacts.contains(refineBody)) {
                        absModel.addFact("refinement_" + String.valueOf(numRefines), refineBody);
                        System.out.println("In refineAbsModel: added a refinement fact.");
                        this.numRefines++;
                    }
                } else {
                    AlloyExpr origAbsGuard = absModel.whenR(spuriousTFQN);

                    if (origAbsGuard != null) {
                        absModel.setWhenR(spuriousTFQN, AlloyAnd(origAbsGuard, refineBody));
                    } else {
                        absModel.setWhenR(spuriousTFQN, refineBody);
                    }
                    System.out.println("Refined the guard of " + spuriousTFQN);
                }

                // System.out.println("Refined Model:\n\n" + absModel.toString());
            }
        } else {
            System.out.println(
                    "In refineAbsModel(): the source of the spurious transition is null.");
        }
    }

    /*
    public void refineAbsModelOnlyGuards() {
        if (spuriousSrcSnapName != null) {
            System.out.println(
                    "In refineAbsModel(): the source of the spurious transition is "
                            + spuriousSrcSnapName);
            System.out.println(
                    "In refineAbsModel(): FQN of the spurious transition is " + spuriousTFQN);

            List<AlloyExpr> abvConjList = new ArrayList<>();
            HashMap<String, String> varVals = snapshotVarVals.get(spuriousSrcSnapName);
            for (String v : varVals.keySet()) {
                AlloyExpr abv = getVarDashRef(v);
                if (varVals.get(v).contains("True")) {
                    abvConjList.add(dsl.AlloyIsTrue(abv));
                } else if (varVals.get(v).contains("False")) {
                    abvConjList.add(dsl.AlloyIsFalse(abv));
                }
            }

            AlloyExpr refineBody = AlloyNot(AlloyAndList(abvConjList));

            if (spuriousTFQN != null) {
                AlloyExpr origGuard = absModel.whenR(spuriousTFQN);
                if (origGuard != null) {
                    absModel.setWhenR(spuriousTFQN, AlloyAnd(origGuard, refineBody));
                } else {
                    absModel.setWhenR(spuriousTFQN, refineBody);
                }
                System.out.println("Refined the guard of " + spuriousTFQN);

                // System.out.println("Refined Model:\n\n" + absModel.toString());
            }
        } else {
            System.out.println(
                    "In refineAbsModel(): the source of the spurious transition is null.");
        }
    }
    */
}
