package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory;
// import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DSL;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.dashtoalloy.ExprTranslatorVis;
import ca.uwaterloo.watform.utils.GeneralUtil;
import ca.uwaterloo.watform.utils.Pos;
import java.util.*;

public class PredicateAbstraction {

    public DashModel origModel;
    public DashModel absModel;
    public int cmdnum;
    public String abvNamePre = "B";
    public String cafDepPredPre = "caf_dep_";
    protected AlloyModel concModelTrunc; // queryModel
    protected AlloyModel origModelD2A; // not reqd, should not be used
    protected ExprTranslatorVis exprTranslator;
    protected DSL dsl;

    // ABV: Abstract Boolean Variable, CAF: Concrete Atomic Formula
    public HashMap<String, AlloyExpr> ABVNameExprMap = new HashMap<String, AlloyExpr>();
    public List<String> envABVs = new ArrayList<>();

    public PredicateAbstraction(DashModel concreteModel) {
        this.origModel = concreteModel;
        this.exprTranslator = new ExprTranslatorVis(concreteModel);
        this.dsl = new DSL(false);
        this.cmdnum = 0;
    }

    public PredicateAbstraction(DashModel concreteModel, int n) {
        this.origModel = concreteModel;
        this.exprTranslator = new ExprTranslatorVis(concreteModel);
        this.dsl = new DSL(false);
        this.cmdnum = n;
    }

    public void createABVmap() {
        // for every init, invariant, pred, guard, and action in origModel, break it down by !, &,
        // |, =>, <=>
        // add each subexp to a set and populate ABVNameExprMap with B0:exp0, B1:exp1, etc.
        Set<AlloyExpr> preds = new HashSet<AlloyExpr>();
        for (AlloyExpr e : origModel.initsR()) {
            preds.addAll((new AlloyExprDecomposer()).decompose(e));
        }
        for (AlloyExpr e : origModel.invsR()) {
            preds.addAll((new AlloyExprDecomposer()).decompose(e));
        }
        List<String> allTransNames = origModel.allTransNames();
        for (String tfqn : allTransNames) {
            preds.addAll((new AlloyExprDecomposer()).decompose(origModel.whenR(tfqn)));
        }
        int ctr = 0;
        for (AlloyExpr e : preds) {
            String abvName = abvNamePre + Integer.toString(ctr);
            ctr++;
            ABVNameExprMap.put(abvName, e);
        }
    }

    public void addCAFDepInvs() {
        // adds CAF Dependency invariants to the abstract model
        int ctr = 0;
        List<List<AlloyExpr>> cafPowSet =
                GeneralUtil.getNonEmptySubsets(Set.copyOf(ABVNameExprMap.values()));
        List<List<AlloyExpr>> unsatList = new ArrayList<>();
        for (List<AlloyExpr> subset : cafPowSet) {
            List<List<AlloyExpr>> subCombos = PredAbsUtil.generatePolarityCombos(subset);
            for (List<AlloyExpr> combo : subCombos) {
                boolean uflag = false;
                for (List<AlloyExpr> u : unsatList) {
                    if (combo.containsAll(u)) {
                        uflag = true;
                        break;
                    }
                }
                if (!uflag) {
                    if (!PredAbsUtil.checkSAT(
                            GeneralUtil.listToSet(combo), concModelTrunc, origModel, false)) {
                        unsatList.add(combo);
                    }
                } else {
                    unsatList.add(combo);
                }
            }
        }
        HashMap<AlloyExpr, String> ABVReverseMap = new HashMap<>();
        ABVNameExprMap.forEach((k, v) -> ABVReverseMap.put(v, k));

        for (List<AlloyExpr> u : unsatList) {
            List<AlloyExpr> uVars =
                    GeneralUtil.mapBy(u, e -> AlloyExprFactory.AlloyVar(ABVReverseMap.get(e)));
            AlloyExpr invBody = AlloyExprFactory.AlloyNot(AlloyExprFactory.AlloyAndList(uVars));
            absModel.addInv(invBody);
        }
    }

    public AlloyExpr abstractAlloyExpr(AlloyExpr expr) { // createAbsAlloyExpr
        // Used to abstract inits, invs, and guards (AlloyExprs in the model that do not have primed
        // vars)
        /*
            pred query_i [s: __Snapshot] {
                expr
                caf_i / ! caf_i
            }
        */
        List<AlloyExpr> exprABVs = new ArrayList<>();
        Set<AlloyExpr> cmdBody = new HashSet<AlloyExpr>();
        cmdBody.add(expr);
        for (String vname : ABVNameExprMap.keySet()) {
            AlloyExpr caf = ABVNameExprMap.get(vname);
            String vfqn = DashFQN.fqn(origModel.rootName, vname);
            AlloyExpr v = new DashRef(DashStrings.DashRefKind.VAR, vfqn, GeneralUtil.emptyList());
            // DashRef.asAlloyVar ??;; new VarDashRef -- subclass of DashRef
            cmdBody.add(caf);
            if (!PredAbsUtil.checkSAT(
                    cmdBody, origModelD2A, origModel, false)) { // queryModel, no need for origmodel
                exprABVs.add(dsl.AlloyIsFalse(v));
            } else {
                cmdBody.remove(caf);
                cmdBody.add(AlloyExprFactory.AlloyNot(caf));
                if (!PredAbsUtil.checkSAT(cmdBody, origModelD2A, origModel, false)) {
                    exprABVs.add(dsl.AlloyIsTrue(v));
                }
            }
        }
        if (!exprABVs.isEmpty()) {
            return AlloyExprFactory.AlloyAndList(exprABVs);
        } else {
            return AlloyExprFactory.AlloyTrue();
        }
    }

    public AlloyExpr abstractTransDo(String tfqn) {
        // used to abstract transition actions that may have primed vars in them
        AlloyExpr guard = exprTranslator.translateExpr(origModel.whenR(tfqn));
        AlloyExpr action = exprTranslator.translateExpr(origModel.doR(tfqn));
        Set<AlloyExpr> cmdBody = new HashSet<AlloyExpr>();
        cmdBody.add(guard);
        cmdBody.add(action);
        List<AlloyExpr> exprABVs = new ArrayList<>();
        for (String vname : ABVNameExprMap.keySet()) {
            AlloyExpr caf = ABVNameExprMap.get(vname);
            String vfqn = DashFQN.fqn(origModel.rootName, vname);
            AlloyExpr v = new DashRef(DashStrings.DashRefKind.VAR, vfqn, GeneralUtil.emptyList());
            cmdBody.add(caf);
            if (!PredAbsUtil.checkSAT(cmdBody, origModelD2A, origModel, true)) {
                exprABVs.add(dsl.AlloyIsFalse(AlloyExprFactory.AlloyPrime(v)));
                envABVs.add(vname);
            } else {
                cmdBody.remove(caf);
                cmdBody.add(AlloyExprFactory.AlloyNot(caf));
                if (!PredAbsUtil.checkSAT(cmdBody, origModelD2A, origModel, true)) {
                    exprABVs.add(dsl.AlloyIsTrue(AlloyExprFactory.AlloyPrime(v)));
                    envABVs.add(vname);
                } else {
                    exprABVs.add(
                            AlloyExprFactory.AlloyOr(
                                    dsl.AlloyIsFalse(AlloyExprFactory.AlloyPrime(v)),
                                    dsl.AlloyIsTrue(AlloyExprFactory.AlloyPrime(v))));
                }
            }
        }
        if (!exprABVs.isEmpty()) {
            return AlloyExprFactory.AlloyAndList(exprABVs);
        } else {
            return AlloyExprFactory.AlloyTrue();
        }
    }

    /*
    Assumptions:
          - All commands in the .dsh file that refer to a property for m/c must be in ACTL*, meaning `check`
          - All property commands must be of the form `check propName for ___` or `check {propName} for ___` (but not `check {x > 5} for ___`)
          - origModel is not parameterized
      */

    public DashModel createAbstractModel() {
        createABVmap();
        DashToAlloy d2a = new DashToAlloy(origModel);

        // taking out the dash model to not check reachability
        concModelTrunc = d2a.translateVarBufferSigsOnly();

        absModel = new DashModel();
        absModel.cloneStateTableOf(origModel);
        absModel.cloneEventTableOf(origModel);

        for (String vname : ABVNameExprMap.keySet()) {
            String vfqn = DashFQN.fqn(origModel.rootName, vname);
            if (envABVs.contains(vname)) {
                absModel.addVar(
                        vfqn,
                        DashStrings.IntEnvKind.ENV,
                        GeneralUtil.emptyList(),
                        AlloyExprFactory.AlloyVar(AlloyStrings.boolName));
            } else {
                absModel.addVar(
                        vfqn,
                        DashStrings.IntEnvKind.INT,
                        GeneralUtil.emptyList(),
                        AlloyExprFactory.AlloyVar(AlloyStrings.boolName));
            }
        }
        addCAFDepInvs();

        // full Alloy translation of origModel reqd for abstracting guards, actions, inits, and invs
        // origModelD2A = d2a.translate();

        for (AlloyExpr init : origModel.initsR()) {
            absModel.addInit(abstractAlloyExpr(init));
        }

        for (AlloyExpr inv : origModel.invsR()) {
            absModel.addInv(abstractAlloyExpr(inv));
        }

        for (String tfqn : origModel.allTransNames()) {
            AlloyExpr absWhenR = abstractAlloyExpr(origModel.whenR(tfqn));
            AlloyExpr absDoR = abstractTransDo(tfqn);
            absModel.addTrans(
                    Pos.UNKNOWN,
                    tfqn,
                    GeneralUtil.emptyList(),
                    origModel.fromR(tfqn),
                    origModel.onR(tfqn),
                    absWhenR,
                    origModel.gotoR(tfqn),
                    origModel.sendR(tfqn),
                    absDoR);
        }

        return absModel;
    }
}
