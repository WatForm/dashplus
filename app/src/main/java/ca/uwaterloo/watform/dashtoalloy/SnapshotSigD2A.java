package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyArrowExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyQtExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigIntExpr;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.Collections;
import java.util.List;

public class SnapshotSigD2A extends SpaceSigsD2A {

    protected SnapshotSigD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    protected void addSnapshotSig() {
        if (this.isElectrum) {
            this.addSnapshotSigElectrum();
        } else {
            this.addSnapshotSigTracesTcmc();
        }
    }

    private void addSnapshotSigTracesTcmc() {
        // traces/tcmc use sig Snapshot {} with fields

        List<AlloyDecl> decls = this.dsl.emptyDeclList();

        // scopesUsed0, scopesUsed1, etc, (need if have parameters)
        List<String> cop;
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            cop = Collections.nCopies(i, D2AStrings.identifierName);
            // scopesUsed0, scopeUsed1, etc
            if (dm.hasConcurrency()) {
                decls.add(
                        // scopesUsedi: p0 -> p1 -> p2 -> set Scopes
                        this.dsl.AlloyDeclArrowStringList(
                                this.dsl.nameNum(D2AStrings.scopesUsedName, i),
                                // p0 -> p1 -> p2 -> set Scopes
                                newListWithOneMore(
                                        // p0, p1, p2
                                        cop,
                                        // some Scopes
                                        // has to be "set", b/c default is "one"
                                        D2AStrings.scopeLabelName)));
            }
            // conf0, conf1, etc.
            if (!dm.hasOnlyOneState()) {
                decls.add(
                        this.dsl.AlloyDeclArrowStringList(
                                this.dsl.nameNum(D2AStrings.confName, i),
                                newListWithOneMore(cop, D2AStrings.stateLabelName)));
            }

            // transTaken1, etc.
            decls.add(
                    this.dsl.AlloyDeclArrowStringList(
                            this.dsl.nameNum(D2AStrings.transTakenName, i),
                            newListWithOneMore(cop, D2AStrings.transLabelName)));

            // events0, event1, etc.
            if (dm.hasEvents() && dm.hasEventsAti(i))
                decls.add(
                        this.dsl.AlloyDeclArrowStringList(
                                this.dsl.nameNum(D2AStrings.eventsName, i),
                                newListWithOneMore(cop, D2AStrings.allEventsName)));
        }

        // stable: one boolean;
        if (dm.hasConcurrency()) {
            decls.add(
                    new AlloyDecl(
                            D2AStrings.stableName, AlloyDecl.Quant.ONE, this.dsl.AlloyBool()));
        }

        decls.addAll(this.varFieldsTraces());
        decls.addAll(this.bufferFieldsTraces());

        // add the snapshot signature
        this.am.addPara(new AlloySigPara(AlloyVar(D2AStrings.snapshotName), decls));
        // System.out.println(new AlloySigPara(AlloyVar(D2AStrings.snapshotName), decls));
    }

    public void varsBuffersOnlySnapshotSig() {
        if (!this.isElectrum) {
            List<AlloyDecl> decls = this.dsl.emptyDeclList();
            decls.addAll(varFieldsTraces());
            decls.addAll(bufferFieldsTraces());
            // add the snapshot signature
            this.am.addPara(new AlloySigPara(AlloyVar(D2AStrings.snapshotName), decls));
        }
        // TODO add case for Electrum
    }

    private List<AlloyDecl> varFieldsTraces() {

        List<AlloyDecl> decls = this.dsl.emptyDeclList();
        for (String vfqn : dm.allVarNames()) {
            // [PID1, PID2]
            // could be empty
            List<AlloyExpr> arrowList = mapBy(dm.varParams(vfqn), i -> AlloyVar(i.paramSig));
            AlloyExpr varTyp = this.translateExpr(dm.varTyp(vfqn));
            AlloyDecl decl;

            // In Dash, a var decl x : one Y produces
            // DashVarDecls("x", "one Y",..)
            // where one Y is an AlloyQtExpr (mult=ONE, AlloyVar("Y"))
            // however,
            // a field in Alloy in a signature as in sig a { x: one Y },
            // is AlloyDecl("x", mul=ONE, AlloyVar("Y"))
            // i.e., in Dash parsing the var decl fully
            // separates the var name and AlloyExpr that follows,
            // but in Alloy, the multiplicity of the "type" is
            // built-in to the Decl

            if (varTyp instanceof AlloyArrowExpr) {
                // varType is "X m -> n Y"
                // in which case we can just tack it on the end
                // vfqn: PID set -> set (X m -> n Y)
                // and if there are no params, it is just
                // vfqn: X m -> n Y
                arrowList.add(this.translateExpr(dm.varTyp(vfqn)));
                decl = AlloyDecl(DashFQN.translateFQN(vfqn), AlloyArrowExprList(arrowList));

            } else if (varTyp instanceof AlloyQtExpr) {
                // x: one A

                // this is just doing a cast
                AlloyQtExpr qtExpr = (AlloyQtExpr) varTyp;

                // not sure how we would support anything else
                if (!(qtExpr.sub instanceof AlloyQnameExpr
                        || qtExpr.sub instanceof AlloySigIntExpr))
                    throw ImplementationError.notSupported(
                            qtExpr.sub.toString()
                                    + ", which is "
                                    + qtExpr.sub.getClass().getName());

                if (arrowList.isEmpty()) {
                    // no params
                    // resulting decl must have Quant built in to it
                    decl =
                            new AlloyDecl(
                                    DashFQN.translateFQN(vfqn),
                                    QtQuantToDeclQuant(qtExpr.qt),
                                    qtExpr.sub);
                } else {
                    // varType is "m X",
                    // so create decl that is
                    // PID set -> set (PID set -> m X)
                    AlloyExpr lastArrow =
                            new AlloyArrowExpr(
                                    lastElement(arrowList),
                                    AlloyArrowExpr.Mul.SET,
                                    QtQuantToArrowQuant(qtExpr.qt),
                                    qtExpr.sub);

                    arrowList = allButLast(arrowList);
                    arrowList.add(lastArrow);
                    decl = AlloyDecl(DashFQN.translateFQN(vfqn), AlloyArrowExprList(arrowList));
                }
            } else {
                throw ImplementationError.notSupported("varTyp is " + varTyp.getClass().getName());
            }
            decls.add(decl);
        }
        return decls;
    }

    // TODO: check for issues above in mul of buffer
    private List<AlloyDecl> bufferFieldsTraces() {

        List<AlloyDecl> decls = this.dsl.emptyDeclList();
        for (String bfqn : dm.allBufferNames()) {
            List<AlloyExpr> arrow_list = mapBy(dm.bufferParams(bfqn), i -> AlloyVar(i.paramSig));

            arrow_list.add(this.dsl.bufferIndexVar(dm.bufferIndex(bfqn)));
            arrow_list.add(AlloyVar(dm.bufferElement(bfqn)));

            decls.add(AlloyDecl(DashFQN.translateFQN(bfqn), AlloyArrowExprList(arrow_list)));
        }
        return decls;
    }

    private void addSnapshotSigElectrum() {
        /* TODO
                // if Electrum add var sigs
            // scopesUsed0, conf0, event0
            List<Decl> decls;

            if (dm.hasConcurrency())
                dm.addVarSigSimple(scopesUsedName+"0", createVar(stateLabelName));
            if (!dm.hasOnlyOneState())
                dm.addVarSigSimple(confName+"0", createVar(stateLabelName));
            dm.addVarSigSimple(transTakenName+"0", createVar(transitionLabelName));
            if (dm.hasEvents())
                d.addVarSigSimple(eventsName+"0", createVar(allEventsName));

            // sig Identifiers {
            //        conf1: StateLabel,
            //        scopesUsed1: StateLabel,
            //        events1: AllEvent,
            //        conf2: Identifiers -> StateLabel,
            //        scopesUsed2: Identifers -> StateLabel,
            //        etc
            // }
            if (dm.getMaxDepthParams() != 0) {
                List<Expr> cop;
                decls = new ArrayList<Decl>();
                for (int i = 1; i <= d.getMaxDepthParams(); i++) {
                    cop = new ArrayList<Expr> (Collections.nCopies(i-1,createVar(identifierName)));
                    // conf 1, etc.
                    if (!dm.hasOnlyOneState())
                        decls.add(
                            DeclExt.newVarDeclExt(
                                confName+Integer.toString(i),
                                createArrowExprList(DashUtilFcns.newListWith(cop, createSet(createVar(stateLabelName))))));

                    decls.add(
                        DeclExt.newVarDeclExt(
                            transTakenName+Integer.toString(i),
                            createArrowExprList(DashUtilFcns.newListWith(cop, createSet(createVar(transitionLabelName))))));
                    if (dm.hasConcurrency())
                        // scopesUsed 1, etc.
                        decls.add(
                            DeclExt.newVarDeclExt(
                                scopesUsedName+Integer.toString(i),
                                createArrowExprList(DashUtilFcns.newListWith(cop, createSet(createVar(stateLabelName))))));
                    if (dm.hasEvents() & dm.hasEventsAti(i))
                        // events 1, etc.
                        decls.add(
                            DeclExt.newVarDeclExt(
                                eventsName+Integer.toString(i),
                                createArrowExprList(DashUtilFcns.newListWith(cop, createSet(createVar(allEventsName))))));
                }
                dm.addSigWithDeclsSimple(identifierName, decls);
            }

            // stable: one boolean;
            if (dm.hasConcurrency()) {
                dm.addVarSigSimple(stableName, createVar(boolName));
            }
            // add vars and parameter sets --------------------------------------


            List<String> allvfqns = dm.getAllVarNames();
            List<String> allbfqns = dm.getAllBufferNames();

            // vfqns with no params and simple type
            // becomes var sig vfqn in var {}
            // no buffers in this case
            // this is tricky if one/lone/set modifiers on type ***
            List<String> allvfqnsNoParamsSimpleTyp = allvfqns.stream()
                .filter(i -> d.getVarBufferParams(i).size() == 0 && !isWeirdOne(i,d))
                .collect(Collectors.toList());
            for (String v: allvfqnsNoParamsSimpleTyp) {

                Expr typ = d.getVarType(v);
                if (isExprVar(typ)) {
                    dm.addVarSigSimple(translateFQN(v), ((ExprVar) translateExpr(typ,d,true)) );
                } else if (isExprSetOf(typ) && isExprVar(getSub(typ))) {
                    dm.addVarSigSimple(translateFQN(v), ((ExprVar)translateExpr(getSub(typ),d,true)) );
                } else if (isExprLoneOf(typ) && isExprVar(getSub(typ))) {
                    dm.addVarLoneSigSimple(translateFQN(v), ((ExprVar)translateExpr(getSub(typ),d,true)) );
                } else if (isExprOneOf(typ) && isExprVar(getSub(typ))) {
                    dm.addVarOneSigSimple(translateFQN(v), ((ExprVar) translateExpr(getSub(typ),d,true)) );
                } else {
                    TranslationToAlloyErrors.Unsupported(typ);
                }
            }

            // vfqns with no params and non-simple var types (weird ones)
            // becomes sig A { var vfqn: B }
            // but A has already been declared somewhere by the user
            // and we can't easily add a field to an existing signature in
            // Alloy module, so instead add one atom to model
            // one sig DshVars {
            //     v1: typ1
            // }
            // and have to deal with this case in translation to Alloy
            // no buffers in this case because they can be grouped with index
            List<String> allvfqnsNoParamsArrowTyp = allvfqns.stream()
                .filter(i -> Common.isWeirdOne(i,d))
                .collect(Collectors.toList());
            if (!allvfqnsNoParamsArrowTyp.isEmpty()) {
                decls = new ArrayList<Decl>();
                for (String v: allvfqnsNoParamsArrowTyp)
                    decls.add(DeclExt.newVarDeclExt(translateFQN(v), translateExpr(d.getVarType(v),d, true)));
                dm.addOneSigWithDeclsSimple(D2AStrings.variablesName, decls);
            }

            // vfqns with parameters P1, P2, P3
            // sig P1 {
            //      var v1: P2 -> P3 ->  typ1
            //      var buf: P2 -> P3 -> bufindex -> eltype
            // }
            // it is enough to look at state parameters to get all parameters
            List<String> allvfqnsWithThisFirstParam;
            List<String> allbfqnsWithThisFirstParam;
            List<Expr> plist;

            for (String prm: DashUtilFcns.listToSet(d.getAllParamsInOrder())) {

                // variables with parameters grouped with parameter
                allvfqnsWithThisFirstParam = allvfqns.stream()
                    // must be at least one parameter
                    .filter(i -> d.getVarBufferParams(i).size() != 0 && d.getVarBufferParams(i).get(0).equals(prm))
                    .collect(Collectors.toList());
                // construct decls -- might be none but still have to
                // create sig for this parameter
                decls = new ArrayList<Decl>();
                for (String v: allvfqnsWithThisFirstParam) {
                    if (dm.getVarBufferParams(v).size() == 1) {
                        decls.add(DeclExt.newVarDeclExt(translateFQN(v), translateExpr(d.getVarType(v),d, true)));
                    } else {
                        plist = createVarList(d.getVarBufferParams(v).subList(1, d.getVarBufferParams(v).size()-1));
                        plist.add(translateExpr(d.getVarType(v),d, true));
                        decls.add(DeclExt.newVarDeclExt(translateFQN(v),createArrowExprList(plist)));
                    }
                }
                // buffers with parameters grouped with parameter
                allbfqnsWithThisFirstParam = allbfqns.stream()
                    // must be at least one parameter
                    .filter(i -> dm.getVarBufferParams(i).size() != 0 && d.getVarBufferParams(i).get(0).equals(prm))
                    .collect(Collectors.toList());
                // construct decls -- might be none but still have to
                // create sig for this parameter
                for (String b: allbfqnsWithThisFirstParam) {
                    if (dm.getVarBufferParams(b).size() != 1)
                        plist = createVarList(d.getVarBufferParams(b).subList(1, d.getVarBufferParams(b).size()-1));
                    else
                        plist = new ArrayList<Expr>();
                    plist.add(bufferIndexVar(d.getBufferIndex(b)));
                    plist.add(createVar(d.getBufferElement(b)));
                    decls.add(DeclExt.newVarDeclExt(translateFQN(b),createArrowExprList(plist)));
                }
                dm.addSigExtendsWithDeclsSimple(prm, identifierName, decls);

            }

            // buffers with no parameters
            // grouped in buffer index introduction
            // every buffer has a different index
            // so just one decl per sig
            List<String> allbfqnsWithNoParams = allbfqns.stream()
                // must be at least one parameter
                .filter(i -> dm.getVarBufferParams(i).size() == 0 )
                .collect(Collectors.toList());
            for (String b: allbfqnsWithNoParams) {
                decls = new ArrayList<Decl>();
                decls.add(DeclExt.newVarDeclExt(
                            translateFQN(b),
                            createVar(d.getBufferElement(b))));
                dm.addSigWithDeclsSimple(
                    bufferIndexName + d.getBufferIndex(b),
                    decls);
            }
        */
    }
}
