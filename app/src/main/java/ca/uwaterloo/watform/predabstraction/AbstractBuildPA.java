package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyAssertPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.dashref.VarDashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.D2AStrings;
import ca.uwaterloo.watform.exprvisitor.ReplaceExprVis;
import ca.uwaterloo.watform.utils.Pos;
import java.io.*;
import java.util.*;

public class AbstractBuildPA extends InitializePA {

    public DashModel absModel;
    protected AlloyModel absAlloy;
    public int absCmdIdx;
    public boolean isAbsCmdCheck = true;

    public AbstractBuildPA(DashModel input) {
        super(input);
        absModel = new DashModel();
        absModel.addImport(List.of(AlloyStrings.utilName, AlloyStrings.booleanName));
        absModel.cloneStateTableOf(concreteModel);
        absModel.cloneEventTableOf(concreteModel);
    }

    public AbstractBuildPA(DashModel input, int n) {
        super(input, n);
        absModel = new DashModel();
        absModel.addImport(List.of(AlloyStrings.utilName, AlloyStrings.booleanName));
        absModel.cloneStateTableOf(concreteModel);
        absModel.cloneEventTableOf(concreteModel);
    }

    public AlloyExpr createAbsExpr(AlloyExpr expr, boolean cmdflag) {
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
            cmdBody.add(caf);
            if (!PredAbsUtil.checkSAT(cmdBody, queryModel, false, scope)) {
                if (cmdflag) {
                    exprABVs.add(exprTranslator.translateExpr(dsl.AlloyIsFalse(v)));
                } else {
                    exprABVs.add(dsl.AlloyIsFalse(v));
                }
            } else {
                cmdBody.remove(caf);
                cmdBody.add(AlloyNot(caf));
                if (!PredAbsUtil.checkSAT(cmdBody, queryModel, false, scope)) {
                    if (cmdflag) {
                        exprABVs.add(exprTranslator.translateExpr(dsl.AlloyIsTrue(v)));
                    } else {
                        exprABVs.add(dsl.AlloyIsTrue(v));
                    }
                }
            }
        }
        if (!exprABVs.isEmpty()) {
            return AlloyAndList(exprABVs);
        } else {
            return AlloyTrue();
        }
    }

    public AlloyExpr createAbsTransDo(String tfqn, List<String> intABVNames) {
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
            AlloyExpr cafNext =
                    (new ReplaceExprVis(AbstractBuildPA::isVarDashRef, AbstractBuildPA::makeNext))
                            .visit(caf);
            AlloyExpr v = ((VarDashRef) getVarDashRef(vname)).makeNext();
            cmdBody.add(caf);
            if (!PredAbsUtil.checkSAT(cmdBody, queryModel, true, scope)) {
                exprABVs.add(dsl.AlloyIsFalse(v));
                intABVNames.add(vname);
            } else {
                cmdBody.remove(caf);
                cmdBody.add(AlloyNot(caf));
                if (!PredAbsUtil.checkSAT(cmdBody, queryModel, true, scope)) {
                    exprABVs.add(dsl.AlloyIsTrue(v));
                    intABVNames.add(vname);
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

    public void addABVsToAbsModel(List<String> intABVNames) {
        // adds all the B0,... Bn as boolean variables to the root state of absModel
        for (String vname : ABVNameCAFTransMap.keySet()) {
            String vfqn = DashFQN.fqn(concreteModel.rootName(), vname);
            if (intABVNames.contains(vname)) {
                absModel.addVar(
                        vfqn,
                        DashStrings.IntEnvKind.INT,
                        emptyList(),
                        AlloyQtEnum.ONE,
                        AlloyVar(AlloyStrings.boolName));
            } else {
                absModel.addVar(
                        vfqn,
                        DashStrings.IntEnvKind.ENV,
                        emptyList(),
                        AlloyQtEnum.ONE,
                        AlloyVar(AlloyStrings.boolName));
            }
        }
    }

    private int addAbstractCmd() {
        if (this.cmd != null) {
            try {
                AlloyCmdPara.CommandDecl cmdDecl = this.cmd.cmdDecls.get(0);
                AlloyQnameExpr cmdBodyQname = cmdDecl.invoQname.orElse(null);
                String vname = cmdBodyQname.vars.get(0).label;
                CAF2ABVReplacer replacer =
                        new CAF2ABVReplacer(
                                ABVNameCAFTransMap, discardedCAFMap, concreteModel.rootName());

                List<AlloyCmdPara.CommandDecl.Scope.Typescope> ts = this.scope.typescopes;
                List<AlloyCmdPara.CommandDecl.Scope.Typescope> absTs = new ArrayList<>();
                for (AlloyCmdPara.CommandDecl.Scope.Typescope t : ts) {
                    if (t.scopableExpr instanceof AlloyQnameExpr) {
                        AlloyQnameExpr e = (AlloyQnameExpr) t.scopableExpr;
                        if (e.vars.get(0).label == D2AStrings.snapshotName) {
                            absTs.add(t);
                        }
                    }
                }
                AlloyCmdPara.CommandDecl.Scope absScope;
                if (absTs.isEmpty()) {
                    absScope = new AlloyCmdPara.CommandDecl.Scope(new AlloyNumExpr(8), emptyList());
                } else {
                    absScope = new AlloyCmdPara.CommandDecl.Scope(absTs);
                }

                if (cmdBodyQname == null) {
                    System.out.println("Unable to get cmd body expr: cmdBodyQname is null.");
                    return -1;
                }

                if (cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.CHECK) {
                    AlloyAssertPara p = this.concreteModel.getAssertPara(vname);
                    AlloyExpr absBody = replacer.replaceWithABVs((AlloyExpr) p.block);
                    AlloyAssertPara absP =
                            new AlloyAssertPara(AlloyVar(vname), (AlloyBlock) absBody);
                    this.absModel.addAssertPara(absP);
                    this.isAbsCmdCheck = true;
                    return PredAbsUtil.addCheckCmd(vname, this.absModel, absScope);
                } else {
                    AlloyPredPara p = this.concreteModel.getPredPara(vname);
                    AlloyExpr absBody = replacer.replaceWithABVs((AlloyExpr) p.block);
                    AlloyPredPara absP =
                            new AlloyPredPara(AlloyVar(vname), p.arguments, (AlloyBlock) absBody);
                    this.absModel.addPredPara(absP);
                    this.isAbsCmdCheck = false;
                    return PredAbsUtil.addRunCmd(vname, this.absModel, absScope);
                }
            } catch (Exception e) {
                System.out.println("Unable to get cmd body expr.");
                handleException(e);
                return -1;
            }
        }
        return -1;
    }

    public void createAbstractModel() {

        List<AlloyExpr> absInits = new ArrayList<>();
        if (concreteModel.initsR().size() > 0) {
            for (AlloyExpr init : concreteModel.initsR()) {
                absInits.add(createAbsExpr(init, false));
            }
        }

        List<AlloyExpr> absInvs = new ArrayList<>();
        if (concreteModel.invsR().size() > 0) {
            for (AlloyExpr inv : concreteModel.invsR()) {
                absInvs.add(createAbsExpr(inv, false));
            }
        }

        HashMap<String, AlloyExpr> absGuards = new HashMap<>();
        HashMap<String, AlloyExpr> absActions = new HashMap<>();
        List<String> intABVNames = new ArrayList<>();
        for (String tfqn : concreteModel.allTransNames()) {
            AlloyExpr guard = concreteModel.whenR(tfqn);
            AlloyExpr action = concreteModel.doR(tfqn);
            AlloyExpr absWhenR;
            AlloyExpr absDoR;
            if (guard != null) {
                absWhenR = createAbsExpr(concreteModel.whenR(tfqn), false);
            } else {
                absWhenR = null;
            }
            if (action != null) {
                absDoR = createAbsTransDo(tfqn, intABVNames);

            } else {
                absDoR = null;
            }
            absGuards.put(tfqn, absWhenR);
            absActions.put(tfqn, absDoR);
        }

        addABVsToAbsModel(intABVNames);
        absModel.addInit(new AlloyBlock(absInits));
        absModel.addInv(new AlloyBlock(absInvs));
        for (String tfqn : concreteModel.allTransNames()) {
            absModel.addTrans(
                    Pos.UNKNOWN,
                    tfqn,
                    emptyList(),
                    concreteModel.fromR(tfqn),
                    concreteModel.onR(tfqn),
                    absGuards.get(tfqn),
                    concreteModel.gotoR(tfqn),
                    concreteModel.sendR(tfqn),
                    absActions.get(tfqn));
        }

        this.absCmdIdx = addAbstractCmd();

        // if (this.absCmdIdx > 0) {
        //     System.out.println("Running abstract command.");
        //     AlloyInterface.executeCommand(absAlloy, cmdIdx);
        // }
    }

    protected AlloyExpr getVarDashRef(String vname) {
        String vfqn = DashFQN.fqn(concreteModel.rootName(), vname);
        return new VarDashRef(vfqn, emptyList());
    }

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
}
