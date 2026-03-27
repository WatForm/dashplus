package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyNegExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashast.dashref.VarDashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.CollectDashRefVis;
import ca.uwaterloo.watform.dashtoalloy.DSL;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.dashtoalloy.ExprTranslatorVis;
import ca.uwaterloo.watform.parser.Parser;
import ca.uwaterloo.watform.utils.Pos;
import java.io.*;
import java.util.*;

public class PredicateAbstraction {

    public DashModel concreteModel;
    public DashModel absModel;
    // public int cmdnum;
    public AlloyCmdPara cmd;
    protected AlloyCmdPara.CommandDecl.Scope scope;
    public String abvNamePre = "B";
    public String cafDepPredPre = "caf_dep_";
    protected AlloyModel queryModel; // queryModel
    protected ExprTranslatorVis exprTranslator;
    protected DSL dsl;
    protected CollectDashRefVis dashRefCollector = new CollectDashRefVis();

    // ABV: Abstract Boolean Variable, CAF: Concrete Atomic Formula
    public HashMap<String, AlloyExpr> ABVNameCAFTransMap = new HashMap<String, AlloyExpr>();
    protected HashMap<String, AlloyExpr> ABVDashCAFMap = new HashMap<>();

    public List<String> envABVs = new ArrayList<>();

    public PredicateAbstraction(DashModel concreteModel) {
        this.concreteModel = concreteModel;
        this.exprTranslator = new ExprTranslatorVis(concreteModel);
        this.dsl = new DSL(false);
        String defaultcmd = "run {} for 4";
        this.scope = Parser.parseCmd(defaultcmd).cmdDecls.get(0).scope.orElse(null);
        if (this.scope == null)
            System.out.println("Null scope for default cmd in PredicateAbstraction().");
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
        // for every init, invariant, pred, guard, and action in concreteModel,
        // break it down by !, &, |, =>, <=>
        // add each subexp to a set and
        // populate ABVNameCAFTransMap with B0:exp0, B1:exp1, etc.
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
        if (preds.contains(emptySet())) {
            preds.remove(emptySet());
        }
        int ctr = 0;
        for (AlloyExpr e : preds) {
            String abvName = abvNamePre + Integer.toString(ctr);
            ctr++;
            ABVDashCAFMap.put(abvName, e);
            ABVNameCAFTransMap.put(abvName, exprTranslator.translateExpr(e));
        }

        System.out.println("\nIn createABVMap(): ABV map created:");
        for (String k : ABVNameCAFTransMap.keySet()) {
            AlloyExpr v = ABVNameCAFTransMap.get(k);
            System.out.println(k + " : " + v.toString());
        }
        System.out.println("*********");
    }

    public void addCAFDepInvs() {
        try {
            // adds CAF Dependency invariants to the abstract model
            Set<Set<AlloyExpr>> cafClusters = createCAFClusters();
            List<List<AlloyExpr>> unsatList = new ArrayList<>();

            for (Set<AlloyExpr> cluster : cafClusters) {

                List<List<AlloyExpr>> cafPowSet = getNonEmptySubsets(cluster);
                for (List<AlloyExpr> subset : cafPowSet) {
                    if (subset.size() <= 4) {
                        List<List<AlloyExpr>> subCombos =
                                PredAbsUtil.generatePolarityCombos(subset);
                        for (List<AlloyExpr> combo : subCombos) {
                            boolean uflag = false;
                            for (List<AlloyExpr> u : unsatList) {
                                if (combo.containsAll(u)) {
                                    uflag = true;
                                    unsatList.add(combo);
                                    break;
                                }
                            }

                            if (!uflag) {
                                if (!PredAbsUtil.checkSAT(
                                        listToSet(combo), queryModel, false, scope)) {
                                    unsatList.add(combo);
                                    System.out.println(
                                            "In addCAFDepInvs(): CAF Dependency Check, added UNSAT combo");
                                }
                            }
                        }
                    } else {
                        System.out.println("Stopped at subset size 4");
                        break;
                    }
                }
            }
            // Create a conjunction of all the ABVs corresponding to CAFs in unsatList
            // Negate the conjunction and add to absModel as invariant
            // {p0, !p2, p4} in unsatList =>
            // invariant: !(B0.boolean/isTrue && B2.boolean/isFalse && B4.boolean/isTrue)

            HashMap<AlloyExpr, String> ABVReverseMap = new HashMap<>();
            ABVNameCAFTransMap.forEach((k, v) -> ABVReverseMap.put(v, k));

            int i = 0;
            System.out.println(
                    "\nIn addCAFDepInvs(): Number of CAFs: " + ABVNameCAFTransMap.values().size());
            System.out.println("In addCAFDepInvs(): Number of UNSAT combos: " + unsatList.size());

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

                i += 1;
                System.out.println("Invariant" + Integer.toString(i) + ": " + invBody.toString());
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public AlloyExpr createAbsExpr(AlloyExpr expr) {
        // Used to abstract inits, invs, and guards (AlloyExprs in the model that do not have primed
        // vars)
        /*
            pred query_i [s: __Snapshot] {
                expr
                caf_i / !caf_i
            }
        */
        List<AlloyExpr> exprABVs = new ArrayList<>();
        Set<AlloyExpr> cmdBody = new HashSet<AlloyExpr>();
        AlloyExpr tExpr = exprTranslator.translateExpr(expr);
        cmdBody.add(tExpr);
        for (String vname : ABVNameCAFTransMap.keySet()) {
            AlloyExpr caf = ABVNameCAFTransMap.get(vname);
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
        /*
            pred query_i [s: __Snapshot] {
                trans_guard
                trans_action
                caf_i / !caf_i
            }
        */
        AlloyExpr concGuard = concreteModel.whenR(tfqn);
        AlloyExpr concAction = concreteModel.doR(tfqn);
        AlloyExpr action = exprTranslator.translateExpr(concAction);
        Set<AlloyExpr> cmdBody = new HashSet<AlloyExpr>();
        if (concGuard != null) {
            AlloyExpr guard = exprTranslator.translateExpr(concGuard);
            cmdBody.add(guard);
        }
        cmdBody.add(action);
        List<AlloyExpr> exprABVs = new ArrayList<>();
        for (String vname : ABVNameCAFTransMap.keySet()) {
            AlloyExpr caf = ABVNameCAFTransMap.get(vname);
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
        // adds all the B0,... Bn as boolean variables to the root state of absModel
        for (String vname : ABVNameCAFTransMap.keySet()) {
            String vfqn = DashFQN.fqn(concreteModel.rootName(), vname);
            if (envABVs.contains(vname)) {
                absModel.addVar(
                        vfqn,
                        DashStrings.IntEnvKind.ENV,
                        emptyList(),
                        AlloyQtEnum.ONE,
                        AlloyVar(AlloyStrings.boolName));
            } else {
                absModel.addVar(
                        vfqn,
                        DashStrings.IntEnvKind.INT,
                        emptyList(),
                        AlloyQtEnum.ONE,
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
        printTransTableOfConcModel();
        createABVmap();
        DashToAlloy d2a = new DashToAlloy(concreteModel);

        // taking out the dash model to not check reachability
        queryModel = d2a.translateVarBufferSigsOnly();

        absModel = new DashModel();
        absModel.cloneStateTableOf(concreteModel);
        absModel.cloneEventTableOf(concreteModel);

        addABVsToAbsModel();
        System.out.println("\nIn createAbstractModel(): ABVs added to abstract model.");

        // addCAFDepInvs();
        // System.out.println("\nIn createAbstractModel():CAF Dependency invariants added.");

        for (AlloyExpr init : concreteModel.initsR()) {
            absModel.addInit(createAbsExpr(init));
        }

        System.out.println("\nIn createAbstractModel(): abstract init added.");

        for (AlloyExpr inv : concreteModel.invsR()) {
            absModel.addInv(createAbsExpr(inv));
        }

        System.out.println("\nIn createAbstractModel(): abstract invariant added.");

        for (String tfqn : concreteModel.allTransNames()) {
            AlloyExpr guard = concreteModel.whenR(tfqn);
            AlloyExpr action = concreteModel.doR(tfqn);
            AlloyExpr absWhenR;
            AlloyExpr absDoR;
            if (guard != null) {
                absWhenR = createAbsExpr(concreteModel.whenR(tfqn));
                System.out.println(
                        "\nIn createAbstractModel(): transition " + tfqn + " guard abstracted.");
            } else {
                absWhenR = null;
            }
            if (action != null) {
                absDoR = createAbsTransDo(tfqn);
                System.out.println(
                        "\nIn createAbstractModel(): transition " + tfqn + " action abstracted.");
            } else {
                absDoR = null;
            }
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

        System.out.println("\nIn createAbstractModel(), Abstract model:\n");
        DashToAlloy absd2a = new DashToAlloy(absModel);
        AlloyModel absAlloy = absd2a.translate();
        System.out.println(absAlloy.toString());

        return absModel;
    }

    private AlloyExpr getVarDashRef(String vname) {
        String vfqn = DashFQN.fqn(concreteModel.rootName(), vname);
        return new VarDashRef(vfqn, emptyList());
    }

    public String getQueryModelString() {
        if (queryModel != null) {
            return queryModel.toString();
        } else {
            return "";
        }
    }

    protected void addCAFPredstoConcrete() {
        for (String vname : ABVNameCAFTransMap.keySet()) {
            AlloyExpr caf = ABVNameCAFTransMap.get(vname);
            String predname = "caf_" + vname;
            concreteModel.addPred(predname, dsl.curDecls(), List.of(caf));
            // queryModel.addPred(predname, dsl.curDecls(), List.of(caf));
        }
    }

    public Set<Set<AlloyExpr>> createCAFClusters() {
        // returning empty dashrefs because CAF is not a DashRef; do clustering before translation
        HashMap<String, Set<DashRef>> exprToDashRefs = new HashMap<>();
        for (String cafName : ABVDashCAFMap.keySet()) {
            AlloyExpr caf = ABVDashCAFMap.get(cafName);
            Set<DashRef> dashRefs = dashRefCollector.collect(caf);
            exprToDashRefs.put(cafName, dashRefs);
            System.out.print(caf.toString() + " : { ");
            for (DashRef dr : dashRefs) {
                System.out.print(dr.name + ", ");
            }
            System.out.print(" }\n");
        }

        HashMap<String, Set<String>> cafGraph = new HashMap<>();

        for (String e : exprToDashRefs.keySet()) {
            cafGraph.put(e, new HashSet<>());
        }

        List<String> cafNames = new ArrayList<>(exprToDashRefs.keySet());

        for (int i = 0; i < ABVDashCAFMap.size(); i++) {
            for (int j = i + 1; j < ABVDashCAFMap.size(); j++) {
                String v1 = cafNames.get(i);
                String v2 = cafNames.get(j);
                AlloyExpr e1 = ABVDashCAFMap.get(v1);
                AlloyExpr e2 = ABVDashCAFMap.get(v2);

                Set<DashRef> s1 = exprToDashRefs.get(e1);
                Set<DashRef> s2 = exprToDashRefs.get(e2);

                if (s1 != null && s2 != null) {
                    if (!Collections.disjoint(s1, s2)) {
                        cafGraph.get(v1).add(v2);
                        cafGraph.get(v2).add(v1);
                    }
                }
            }
        }

        Set<String> visited = new HashSet<>();
        Set<Set<AlloyExpr>> result = new HashSet<>();

        for (String cafName : cafGraph.keySet()) {
            if (!visited.contains(cafName)) {
                Set<AlloyExpr> component = new HashSet<>();
                Deque<String> stack = new ArrayDeque<>();
                stack.push(cafName);

                while (!stack.isEmpty()) {
                    String curr = stack.pop();
                    if (visited.add(curr)) {
                        component.add(ABVDashCAFMap.get(curr));
                        stack.addAll(cafGraph.get(curr));
                    }
                }
                result.add(component);
            }
        }

        int ctr = 1;
        for (Set<AlloyExpr> cluster : result) {
            System.out.print("Cluster " + ctr + ": { ");
            for (AlloyExpr e : cluster) {
                System.out.print(e.toString() + ", ");
            }
            System.out.print(" }\n");
            ctr++;
        }

        return result;
    }

    private void printTransTableOfConcModel() {
        System.out.println("CONCRETE MODEL\n*********\n");
        System.out.println(concreteModel.ttToString());
    }

    // private void nextSnapshotVarsSame() {

    // }
}
