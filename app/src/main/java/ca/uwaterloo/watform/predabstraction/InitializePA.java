package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyAssertPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
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
    public String cafDepPredPre = "caf_dep_";
    protected AlloyCmdPara.CommandDecl.Scope scope;
    protected ExprTranslatorVis exprTranslator;
    protected DSL dsl = new DSL(false);
    protected AlloyModel queryModel;

    // ABV: Abstract Boolean Variable, CAF: Concrete Atomic Formula
    public HashMap<String, AlloyExpr> ABVNameCAFTransMap = new HashMap<String, AlloyExpr>();

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
        if (cmdBody != null) {
            preds.addAll(exprDecomp.decompose(cmdBody));
        }
        for (AlloyExpr e : concreteModel.initsR()) {
            preds.addAll(exprDecomp.decompose(e));
        }
        for (AlloyExpr e : concreteModel.invsR()) {
            preds.addAll(exprDecomp.decompose(e));
        }
        List<String> allTransNames = concreteModel.allTransNames();
        for (String tfqn : allTransNames) {
            preds.addAll(exprDecomp.decompose(concreteModel.whenR(tfqn)));
        }
        if (preds.contains(emptySet())) {
            preds.remove(emptySet());
        }

        List<AlloyExpr> predList = setToList(preds);
        for (int i = 0; i < predList.size(); i++) {
            for (int j = i + 1; j < predList.size(); j++) {
                AlloyExpr e1 = predList.get(i);
                AlloyExpr e2 = predList.get(j);
                AlloyExpr iff = AlloyIff(e1, e2);
                AlloyExpr iff2 = AlloyIff(AlloyNot(e1), e2);
                if (PredAbsUtil.checkSAT(Set.of(iff), queryModel, false, this.scope)) {
                    preds.remove(e2);
                } else if (PredAbsUtil.checkSAT(Set.of(iff2), queryModel, false, this.scope)) {
                    preds.remove(e2);
                }
            }
        }

        int ctr = 0;
        Set<AlloyExpr> translatedPreds = new HashSet<>();

        for (AlloyExpr e : preds) {
            translatedPreds.add(exprTranslator.translateExpr(e));
            // ABVNameCAFTransMap.put(abvName, exprTranslator.translateExpr(e));
        }

        for (AlloyExpr e : translatedPreds) {
            String abvName = abvNamePre + Integer.toString(ctr);
            ctr++;
            ABVNameCAFTransMap.put(abvName, e);
        }
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
}
