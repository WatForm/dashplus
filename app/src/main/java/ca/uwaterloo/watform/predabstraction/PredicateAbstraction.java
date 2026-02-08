package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyNegExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.dashref.VarDashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DSL;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.dashtoalloy.ExprTranslatorVis;
import ca.uwaterloo.watform.utils.Pos;
import java.util.*;

public class PredicateAbstraction {

    public DashModel concreteModel;
    public DashModel absModel;
    // public int cmdnum;
    public AlloyCmdPara cmd;
    private AlloyCmdPara.CommandDecl.Scope scope;
    public String abvNamePre = "B";
    public String cafDepPredPre = "caf_dep_";
    protected AlloyModel queryModel; // queryModel
    protected ExprTranslatorVis exprTranslator;
    protected DSL dsl;

    // ABV: Abstract Boolean Variable, CAF: Concrete Atomic Formula
    public HashMap<String, AlloyExpr> ABVNameCAFMap = new HashMap<String, AlloyExpr>();
    public List<String> envABVs = new ArrayList<>();

    public PredicateAbstraction(DashModel concreteModel) {
        this.concreteModel = concreteModel;
        this.exprTranslator = new ExprTranslatorVis(concreteModel);
        this.dsl = new DSL(false);
        // this.cmdnum = 0;
    }

    public PredicateAbstraction(DashModel concreteModel, int n) {
        this.concreteModel = concreteModel;
        this.exprTranslator = new ExprTranslatorVis(concreteModel);
        this.dsl = new DSL(false);
        // this.cmdnum = n;
        this.cmd = concreteModel.getCmdNum(n).orElse(null);
        this.scope = cmd.cmdDecls.get(0).scope.orElse(null);
    }

    public void createABVmap() {
        // for every init, invariant, pred, guard, and action in concreteModel, break it down by !,
        // &,
        // |, =>, <=>
        // add each subexp to a set and populate ABVNameCAFMap with B0:exp0, B1:exp1, etc.
        Set<AlloyExpr> preds = new HashSet<AlloyExpr>();
        for (AlloyExpr e : concreteModel.initsR()) {
            preds.addAll((new AlloyExprDecomposer()).decompose(e));
        }
        for (AlloyExpr e : concreteModel.invsR()) {
            preds.addAll((new AlloyExprDecomposer()).decompose(e));
        }
        List<String> allTransNames = concreteModel.allTransNames();
        for (String tfqn : allTransNames) {
            preds.addAll((new AlloyExprDecomposer()).decompose(concreteModel.whenR(tfqn)));
        }
        int ctr = 0;
        for (AlloyExpr e : preds) {
            String abvName = abvNamePre + Integer.toString(ctr);
            ctr++;
            ABVNameCAFMap.put(abvName, e);
        }
    }

    public void addCAFDepInvs() {
        // adds CAF Dependency invariants to the abstract model
        int ctr = 0;
        List<List<AlloyExpr>> cafPowSet = getNonEmptySubsets(Set.copyOf(ABVNameCAFMap.values()));
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
                    if (!PredAbsUtil.checkSAT(listToSet(combo), queryModel, false, scope)) {
                        unsatList.add(combo);
                    }
                } else {
                    unsatList.add(combo);
                }
            }
        }
        // Create a conjunction of all the ABVs corresponding to CAFs in unsatList
        // Negate the conjunction and add to absModel as invariant
        // {p0, !p2, p4} in unsatList =>
        // invariant: !(B0.boolean/isTrue && B2.boolean/isFalse && B4.boolean/isTrue)

        HashMap<AlloyExpr, String> ABVReverseMap = new HashMap<>();
        ABVNameCAFMap.forEach((k, v) -> ABVReverseMap.put(v, k));

        for (List<AlloyExpr> u : unsatList) {
            // List<AlloyExpr> uVars = mapBy(u, e -> AlloyVar(ABVReverseMap.get(e)));
            List<AlloyExpr> uVars = new ArrayList<>();
            for (AlloyExpr e : u) {
                String vname;
                // if e is a negated expression, then the subexpr is the CAF and the
                // corresponding ABV must be false
                if (e instanceof AlloyNegExpr) {
                    vname = ABVReverseMap.get(((AlloyUnaryExpr) e).sub);
                    uVars.add(dsl.AlloyIsFalse(getVarDashRef(vname)));
                } else {
                    vname = ABVReverseMap.get(e);
                    uVars.add(dsl.AlloyIsTrue(getVarDashRef(vname)));
                }
            }
            AlloyExpr invBody = AlloyNot(AlloyAndList(uVars));
            absModel.addInv(invBody);
        }
    }

    public AlloyExpr createAbsExpr(AlloyExpr expr) {
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
        for (String vname : ABVNameCAFMap.keySet()) {
            AlloyExpr caf = ABVNameCAFMap.get(vname);
            AlloyExpr v = getVarDashRef(vname);
            // DashRef.asAlloyVar ??;; new VarDashRef -- subclass of DashRef
            cmdBody.add(caf);
            if (!PredAbsUtil.checkSAT(
                    cmdBody, queryModel, false, scope)) { // queryModel, no need for concreteModel
                exprABVs.add(dsl.AlloyIsFalse(v));
            } else {
                cmdBody.remove(caf);
                cmdBody.add(AlloyNot(caf));
                if (!PredAbsUtil.checkSAT(cmdBody, queryModel, false, scope)) {
                    exprABVs.add(dsl.AlloyIsTrue(v));
                }
            }
        }
        if (!exprABVs.isEmpty()) {
            return AlloyAndList(exprABVs);
        } else {
            return AlloyTrue();
        }
    }

    public AlloyExpr createAbsTransDo(String tfqn) {
        // used to abstract transition actions that may have primed vars in them
        AlloyExpr guard = exprTranslator.translateExpr(concreteModel.whenR(tfqn));
        AlloyExpr action = exprTranslator.translateExpr(concreteModel.doR(tfqn));
        Set<AlloyExpr> cmdBody = new HashSet<AlloyExpr>();
        cmdBody.add(guard);
        cmdBody.add(action);
        List<AlloyExpr> exprABVs = new ArrayList<>();
        for (String vname : ABVNameCAFMap.keySet()) {
            AlloyExpr caf = ABVNameCAFMap.get(vname);
            AlloyExpr v = ((VarDashRef) getVarDashRef(vname)).makeNext();
            cmdBody.add(caf);
            if (!PredAbsUtil.checkSAT(cmdBody, queryModel, true, scope)) {
                exprABVs.add(dsl.AlloyIsFalse(v));
                envABVs.add(vname);
            } else {
                cmdBody.remove(caf);
                cmdBody.add(AlloyNot(caf));
                if (!PredAbsUtil.checkSAT(cmdBody, queryModel, true, scope)) {
                    exprABVs.add(dsl.AlloyIsTrue(v));
                    envABVs.add(vname);
                } else {
                    exprABVs.add(AlloyOr(dsl.AlloyIsFalse(v), dsl.AlloyIsTrue(v)));
                }
            }
        }
        if (!exprABVs.isEmpty()) {
            return AlloyAndList(exprABVs);
        } else {
            return AlloyTrue();
        }
    }

    public void addABVsToAbsModel() {
        for (String vname : ABVNameCAFMap.keySet()) {
            String vfqn = DashFQN.fqn(concreteModel.rootName, vname);
            if (envABVs.contains(vname)) {
                absModel.addVar(
                        vfqn,
                        DashStrings.IntEnvKind.ENV,
                        emptyList(),
                        AlloyVar(AlloyStrings.boolName));
            } else {
                absModel.addVar(
                        vfqn,
                        DashStrings.IntEnvKind.INT,
                        emptyList(),
                        AlloyVar(AlloyStrings.boolName));
            }
        }
    }

    /*
    Assumptions:
          - All commands in the .dsh file that refer to a property for m/c must be in ACTL*, meaning `check`
          - All property commands must be of the form `check propName for ___` or `check {propName} for ___` (but not `check {x > 5} for ___`)
          - concreteModel is not parameterized
      */

    public DashModel createAbstractModel() {
        createABVmap();
        DashToAlloy d2a = new DashToAlloy(concreteModel);

        // taking out the dash model to not check reachability
        queryModel = d2a.translateVarBufferSigsOnly();

        absModel = new DashModel();
        absModel.cloneStateTableOf(concreteModel);
        absModel.cloneEventTableOf(concreteModel);

        addABVsToAbsModel();
        addCAFDepInvs();

        for (AlloyExpr init : concreteModel.initsR()) {
            absModel.addInit(createAbsExpr(init));
        }

        for (AlloyExpr inv : concreteModel.invsR()) {
            absModel.addInv(createAbsExpr(inv));
        }

        for (String tfqn : concreteModel.allTransNames()) {
            AlloyExpr absWhenR = createAbsExpr(concreteModel.whenR(tfqn));
            AlloyExpr absDoR = createAbsTransDo(tfqn);
            absModel.addTrans(
                    Pos.UNKNOWN,
                    tfqn,
                    emptyList(),
                    concreteModel.fromR(tfqn),
                    concreteModel.onR(tfqn),
                    absWhenR,
                    concreteModel.gotoR(tfqn),
                    concreteModel.sendR(tfqn),
                    absDoR);
        }

        return absModel;
    }

    private AlloyExpr getVarDashRef(String vname) {
        String vfqn = DashFQN.fqn(concreteModel.rootName, vname);
        return new VarDashRef(vfqn, emptyList());
    }
}
