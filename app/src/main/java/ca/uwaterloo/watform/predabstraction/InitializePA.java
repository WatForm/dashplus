package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.dashtoalloy.D2AStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyAssertPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.dashref.VarDashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DSL;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.dashtoalloy.ExprTranslatorVis;
import ca.uwaterloo.watform.parser.Parser;
import java.io.*;
import java.util.*;

public class InitializePA {

    public DashModel concreteModel;
    public AlloyCmdPara cmd;
    public String abvNamePre = "B";
    // public String cafDepPredPre = "caf_dep_";
    public String bPredPrefix = "CAF_";
    protected AlloyCmdPara.CommandDecl.Scope scope;
    protected ExprTranslatorVis exprTranslator;
    protected DSL dsl = new DSL(false);
    protected AlloyModel queryModel;

    // ABV: Abstract Boolean Variable, CAF: Concrete Atomic Formula
    public HashMap<String, AlloyExpr> ABVNameCAFTransMap = new HashMap<>();
    protected HashMap<String, AlloyExpr> untranslatedCAFMap = new HashMap<>();
    public HashMap<AlloyExpr, AlloyExpr> discardedCAFMap = new HashMap<>();
    protected Set<AlloyExpr> propPreds = new HashSet<>();
    ;

    public InitializePA(DashModel input) {
        this.concreteModel = input;
        this.exprTranslator = new ExprTranslatorVis(this.concreteModel);
        String defaultcmd = "run {} for 4";
        this.cmd = null;
        this.scope = Parser.parseCmd(defaultcmd).cmdDecls.get(0).scope.orElse(null);
        if (this.scope == null) {
            System.out.println("Null scope for default cmd in InitializePA().");
        }

        DashToAlloy d2a = new DashToAlloy(concreteModel);
        this.queryModel = d2a.translateVarBufferSigsOnly();
    }

    public InitializePA(DashModel input, int n) {
        this.concreteModel = input;
        this.exprTranslator = new ExprTranslatorVis(this.concreteModel);
        this.cmd = concreteModel.getCmdNum(n).orElse(null);
        this.scope = cmd.cmdDecls.get(0).scope.orElse(null);

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
        System.out.println("CAFs (size = " + String.valueOf(CAFs.size()) + "): " + CAFs.toString());
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
                    CAFs.remove(e2);
                } else if (!PredAbsUtil.checkSAT(Set.of(iff2), queryModel, false, this.scope)) {
                    discardedCAFMap.put(e2, AlloyNot(e1));
                    CAFs.remove(e2);
                }
            }
        }

        int ctr = 0;

        for (AlloyExpr e : CAFs) {
            String abvName = abvNamePre + Integer.toString(ctr);
            ctr++;
            ABVNameCAFTransMap.put(abvName, e);
            if (translatedPreds.containsKey(e)) {
                untranslatedCAFMap.put(abvName, translatedPreds.get(e));
            }
        }
        addCAFPreds();
    }

    private AlloyExpr getCmdBodyExpr() throws AlloyCtorError {
        if (this.cmd == null) {
            System.out.println("In getCmdBodyExpr(): Command does not exist.");
            return null;
        } else {
            try {
                String pname = PredAbsUtil.getPredNameFromCmd(cmd);
                if (pname == null) { // in case of commands like "check/run {expr} for..."
                    AlloyBlock block = PredAbsUtil.getFormulaFromCmd(cmd);
                    if (block != null) {
                        return (AlloyExpr) block;
                    } else { // should not happen: command has neither a valid pred/assert nor exprs
                        throw AlloyCtorError.xorFields(
                                cmd.pos, "invoQname", "constrBlock", "AlloyCmdPara.CommandDecl");
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

    protected AlloyExpr getVarDashRef(String vname) {
        String vfqn = DashFQN.fqn(concreteModel.rootName(), vname);
        return new VarDashRef(vfqn, emptyList());
    }

    protected List<AlloyExpr> getCAFDepInvs() {
        // List<AlloyExpr> cafList = new ArrayList<>(ABVNameCAFTransMap.values());
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
                // AlloyExpr caf_i = cafList.get(i);
                // AlloyExpr caf_j = cafList.get(j);
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
}
