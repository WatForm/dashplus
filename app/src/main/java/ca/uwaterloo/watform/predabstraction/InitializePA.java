package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.dashtoalloy.D2AStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
// import ca.uwaterloo.watform.alloyast.expr.binary.AlloyDotExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyAssertPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.dashref.VarDashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.D2AStrings;
import ca.uwaterloo.watform.dashtoalloy.DSL;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.dashtoalloy.ExprTranslatorVis;
import ca.uwaterloo.watform.parser.Parser;
import java.io.*;
import java.util.*;

public class InitializePA {

    public DashModel concreteModel;
    public AlloyCmdPara.CommandDecl cmdDecl;
    public String abvNamePre = "B";
    // public String cafDepPredPre = "caf_dep_";
    public String bPredPrefix = "CAF_";
    protected AlloyCmdPara.CommandDecl.Scope scope;
    protected ExprTranslatorVis exprTranslator;
    protected DSL dsl = new DSL(false);
    protected AlloyModel queryModel;

    // ABV: Abstract Boolean Variable, CAF: Concrete Atomic Formula
    // ABVNameCAFTransMap = {"B0": translated_caf0, ...}
    public HashMap<String, AlloyExpr> ABVNameCAFTransMap = new HashMap<>();
    protected HashMap<String, AlloyExpr> untranslatedCAFMap = new HashMap<>();

    // discardedCAFMap stores all the candidate formulas that are not added
    // as a CAF because it is logically equivalent to another CAF or its negation
    protected HashMap<AlloyExpr, AlloyExpr> discardedCAFMap = new HashMap<>();
    protected HashMap<AlloyExpr, AlloyExpr> untransDiscardedCAFMap = new HashMap<>();

    // propPreds stores the formulas extracted from the property
    // these contain the only CAFs that are already translated to Alloy
    protected Set<AlloyExpr> propPreds = new HashSet<>();

    public InitializePA(DashModel input) {
        this.concreteModel = input;
        this.exprTranslator = new ExprTranslatorVis(this.concreteModel);
        String defaultcmdDecl = "run {} for 4";
        this.cmdDecl = null;
        this.scope = Parser.parseCmdDecl(defaultcmdDecl).scope.orElse(null);
        if (this.scope == null) {
            System.out.println("Null scope for default cmd in InitializePA().");
        }

        DashToAlloy d2a = new DashToAlloy(concreteModel);
        this.queryModel = d2a.translateVarBufferSigsOnly();
    }

    public InitializePA(DashModel input, int n) {
        this.concreteModel = input;
        this.exprTranslator = new ExprTranslatorVis(this.concreteModel);
        this.cmdDecl = concreteModel.getCmdNum(n).orElse(null);
        this.scope = this.cmdDecl.scope.orElse(null);

        DashToAlloy d2a = new DashToAlloy(concreteModel);
        this.queryModel = d2a.translateVarBufferSigsOnly();
    }

    public void createABVMap() {
        // for every init, invariant, pred, guard, and action in concreteModel,
        // break it down by !, &, |, =>, <=>
        // add each subexp to a set and
        // populate ABVNameCAFTransMap with B0:exp0, B1:exp1, etc.
        Set<AlloyExpr> preds = new HashSet<AlloyExpr>();
        AlloyExprDecomposer exprDecomp = new AlloyExprDecomposer();

        AlloyExpr cmdBody = getCmdBodyExpr();
        this.propPreds = new HashSet<>();
        if (cmdBody != null) {
            propPreds.addAll(exprDecomp.decompose(cmdBody));
            preds.addAll(propPreds);
        }

        Set<AlloyExpr> initPreds = new HashSet<>();
        for (AlloyExpr e : concreteModel.initsR()) {
            initPreds.addAll(exprDecomp.decompose(e));
            preds.addAll(initPreds);
        }

        Set<AlloyExpr> invPreds = new HashSet<>();
        for (AlloyExpr e : concreteModel.invsR()) {
            invPreds.addAll(exprDecomp.decompose(e));
            preds.addAll(initPreds);
        }
        Set<AlloyExpr> guardPreds = new HashSet<>();
        List<String> allTransNames = concreteModel.allTransNames();
        for (String tfqn : allTransNames) {
            guardPreds.addAll(exprDecomp.decompose(concreteModel.whenR(tfqn)));
            preds.addAll(guardPreds);
        }

        HashMap<AlloyExpr, AlloyExpr> translatedPreds = new HashMap<>();

        // remove any formulas that contain conf, taken, events
        for (AlloyExpr e : preds) {
            String eStr = e.toString();
            if (eStr.contains(confName)
                    || eStr.contains(transTakenName)
                    || eStr.contains(eventsName)) {
                continue;
            }
            translatedPreds.put(exprTranslator.translateExpr(e), e);
        }

        List<AlloyExpr> predList = new ArrayList<AlloyExpr>(translatedPreds.keySet());
        Set<AlloyExpr> CAFs = new HashSet<>(translatedPreds.keySet());

        System.out.println("List of potential CAFs of size " + CAFs.size() + ":");
        for (AlloyExpr e : CAFs) {
            System.out.println(e.toString());
        }

        // Determine the final set of CAFs by checking every pair and seeing if they are
        // logically equiv to each other or their negations
        for (int i = 0; i < predList.size() - 1; i++) {
            for (int j = i + 1; j < predList.size(); j++) {
                VarNameCollector vc = new VarNameCollector();
                AlloyExpr e1 = predList.get(i);
                AlloyExpr e2 = predList.get(j);
                if (Collections.disjoint(vc.getVarNames(e1), vc.getVarNames(e2))) {
                    continue;
                }
                AlloyExpr iff = AlloyNot(AlloyIff(e1, e2));
                AlloyExpr iff2 = AlloyNot(AlloyIff(AlloyNot(e1), e2));
                if (!PredAbsUtil.checkSAT(Set.of(iff), queryModel, false, this.scope)) {
                    discardedCAFMap.put(e2, e1);
                    untransDiscardedCAFMap.put(translatedPreds.get(e2), translatedPreds.get(e1));
                    CAFs.remove(e2);
                } else if (!PredAbsUtil.checkSAT(Set.of(iff2), queryModel, false, this.scope)) {
                    discardedCAFMap.put(e2, AlloyNot(e1));
                    untransDiscardedCAFMap.put(
                            translatedPreds.get(e2), AlloyNot(translatedPreds.get(e1)));
                    CAFs.remove(e2);
                }
            }
        }

        int ctr = 0;

        // populate ABVNameCAFTransMap and untranslatedCAFMap
        for (AlloyExpr e : CAFs) {
            String abvName = abvNamePre + Integer.toString(ctr);
            ctr++;
            ABVNameCAFTransMap.put(abvName, e);
            if (translatedPreds.containsKey(e)) {
                untranslatedCAFMap.put(abvName, translatedPreds.get(e));
            }
        }

        System.out.println("\nDiscarded CAF map created:");
        for (AlloyExpr k : this.discardedCAFMap.keySet()) {
            AlloyExpr v = this.discardedCAFMap.get(k);
            System.out.println(k.toString() + " : " + v.toString());
        }
        System.out.println("*********");
        // adds Alloy Preds of the form
        // pred CAF_Bi[s: __Snapshot] {translated_caf_i}
        addCAFPreds();
    }

    // gets the body of the predicate/assert that is being run by a cmd
    private AlloyExpr getCmdBodyExpr() throws AlloyCtorError {
        if (this.cmdDecl == null) {
            System.out.println("In getCmdBodyExpr(): Command does not exist.");
            return null;
        } else {
            try {
                String pname = PredAbsUtil.getPredNameFromCmd(this.cmdDecl);
                if (pname == null) { // in case of commands like "check/run {expr} for..."
                    AlloyBlock block = PredAbsUtil.getFormulaFromCmd(this.cmdDecl);
                    if (block != null) {
                        return (AlloyExpr) block;
                    } else { // should not happen: command has neither a valid pred/assert nor exprs
                        throw AlloyCtorError.xorFields(
                                this.cmdDecl.pos,
                                "invoQname",
                                "constrBlock",
                                "AlloyCmdPara.CommandDecl");
                    }
                } else { // when cmd is like "run/check pname for ..."
                    AlloyPredPara pred = concreteModel.getPredPara(pname);
                    if (pred != null) { // if cmd is run predname for ...
                        return pred.block;
                    } else { // if cmd is check ass for ...
                        AlloyAssertPara ass = concreteModel.getAssertPara(pname);
                        if (ass != null) return ass.block;
                        else {
                            System.out.println("Tried to find \"" + pname + "\" and failed.");
                            System.out.println(
                                    "Command is trying to access a pred or assert that does not exist.");
                            return null;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Unable to get cmd body expr.");
                e.printStackTrace();
                return null;
            }
        }
    }

    // function to go from "Bi" to its corresponding VarDashRefs
    protected AlloyExpr getVarDashRef(String vname) {
        String vfqn = DashFQN.fqn(concreteModel.rootName(), vname);
        return new VarDashRef(vfqn, emptyList());
    }

    // returns a list of implications that get added as Dash Invs
    // for every pair of CAFs, fi and fj, check validity of:
    // fi => fj, fi => !fj, fj => fi, !fj => fi
    protected List<AlloyExpr> getCAFDepInvs() {
        // Map the CAFs to the VarDashRef of the B
        HashMap<AlloyExpr, AlloyExpr> cafMap = new HashMap<>();
        for (Map.Entry<String, AlloyExpr> entry : ABVNameCAFTransMap.entrySet()) {
            cafMap.put(entry.getValue(), getVarDashRef(entry.getKey()));
        }

        List<AlloyExpr> retList = new ArrayList<>();
        for (int i = 0; i < ABVNameCAFTransMap.size() - 1; i++) {
            for (int j = i + 1; j < ABVNameCAFTransMap.size(); j++) {
                VarNameCollector vc = new VarNameCollector();
                String iname = abvNamePre + String.valueOf(i);
                String jname = abvNamePre + String.valueOf(j);
                AlloyExpr caf_i = AlloyPredCall(bPredPrefix + iname, List.of(dsl.curVar()));
                AlloyExpr caf_j = AlloyPredCall(bPredPrefix + jname, List.of(dsl.curVar()));

                // if caf_i and caf_j has no vars in common, skip this pair
                if (Collections.disjoint(
                        vc.getVarNames(ABVNameCAFTransMap.get(iname)),
                        vc.getVarNames(ABVNameCAFTransMap.get(jname)))) {
                    continue;
                }

                AlloyExpr i_imp_j = AlloyNot(AlloyImplies(caf_i, caf_j));
                AlloyExpr i_imp_not_j = AlloyNot(AlloyImplies(caf_i, AlloyNot(caf_j)));
                AlloyExpr j_imp_i = AlloyNot(AlloyImplies(caf_j, caf_i));
                AlloyExpr not_j_imp_i = AlloyNot(AlloyImplies(AlloyNot(caf_j), caf_i));
                if (!PredAbsUtil.checkSAT(Set.of(i_imp_j), queryModel, false, this.scope)) {
                    retList.add(
                            AlloyImplies(
                                    dsl.AlloyIsTrue(getVarDashRef(iname)),
                                    dsl.AlloyIsTrue(getVarDashRef(jname))));
                }
                if (!PredAbsUtil.checkSAT(Set.of(i_imp_not_j), queryModel, false, this.scope)) {
                    retList.add(
                            AlloyImplies(
                                    dsl.AlloyIsTrue(getVarDashRef(iname)),
                                    dsl.AlloyIsFalse(getVarDashRef(jname))));
                }
                if (!PredAbsUtil.checkSAT(Set.of(j_imp_i), queryModel, false, this.scope)) {
                    retList.add(
                            AlloyImplies(
                                    dsl.AlloyIsTrue(getVarDashRef(jname)),
                                    dsl.AlloyIsTrue(getVarDashRef(iname))));
                }
                if (!PredAbsUtil.checkSAT(Set.of(not_j_imp_i), queryModel, false, this.scope)) {
                    retList.add(
                            AlloyImplies(
                                    dsl.AlloyIsFalse(getVarDashRef(jname)),
                                    dsl.AlloyIsTrue(getVarDashRef(iname))));
                }
            }
        }
        // System.out.println("In getCAFDepInvs, invList: " + retList.toString());
        return retList;
    }

    private void addCAFPreds() {
        for (String v : ABVNameCAFTransMap.keySet()) {
            AlloyExpr caf = ABVNameCAFTransMap.get(v);
            queryModel.addPredPara(
                    new AlloyPredPara(
                            new AlloyQnameExpr(bPredPrefix + v),
                            dsl.curDecls(),
                            new AlloyBlock(List.of(caf))));
        }
    }

    // functions for ReplaceExprVis

    public static Boolean isVarDashRef(AlloyExpr e) {
        return (e instanceof VarDashRef);
    }

    public static AlloyExpr makeNext(AlloyExpr e) {
        if (e instanceof VarDashRef) {
            return ((VarDashRef) e).makeNext();
        } else {
            return e;
        }
    }

    public static Boolean hasCurName(AlloyExpr e) {
        if (e instanceof AlloyVarExpr) {
            return D2AStrings.curName.equals(((AlloyVarExpr) e).label);
        } else {
            return false;
        }
    }

    public static AlloyExpr makeNextName(AlloyExpr e) {
        DSL dsl_local = new DSL(false);
        if (hasCurName(e)) {
            return dsl_local.nextVar();
        } else {
            return e;
        }
    }

    // public static AlloyExpr untranslate(AlloyExpr e) {
    //     if(e instanceof AlloyDotExpr) {
    //         AlloyDotExpr dotE = (AlloyDotExpr) e;
    //         if(dotE.left == ((AlloyExpr) dsl.curVar())) {

    //         }
    //     }
    // }
}
