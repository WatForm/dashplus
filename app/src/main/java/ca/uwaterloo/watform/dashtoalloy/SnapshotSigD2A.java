package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
// import ca.uwaterloo.watform.dashast.D2AStrings;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
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
                        AlloyDecl(
                                this.dsl.nameNum(D2AStrings.scopesUsedName, i),
                                // p0 -> p1 -> p2 -> set Scopes
                                AlloyArrowStringListEndInSet(
                                        newListWithOneMore(
                                                // p0, p1, p2
                                                cop,
                                                // some Scopes
                                                // has to be "set", b/c default is "one"
                                                D2AStrings.scopeLabelName))));
            }
            // conf0, conf1, etc.
            if (!dm.hasOnlyOneState())
                decls.add(
                        AlloyDecl(
                                this.dsl.nameNum(D2AStrings.confName, i),
                                AlloyArrowStringListEndInSet(
                                        newListWithOneMore(cop, D2AStrings.stateLabelName))));

            // transTaken1, etc.
            decls.add(
                    AlloyDecl(
                            this.dsl.nameNum(D2AStrings.transTakenName, i),
                            AlloyArrowStringListEndInSet(
                                    newListWithOneMore(cop, D2AStrings.transLabelName))));

            // events0, event1, etc.
            if (dm.hasEvents() && dm.hasEventsAti(i))
                decls.add(
                        AlloyDecl(
                                this.dsl.nameNum(D2AStrings.eventsName, i),
                                AlloyArrowStringListEndInSet(
                                        newListWithOneMore(cop, D2AStrings.allEventsName))));
        }

        // stable: one boolean;
        if (dm.hasConcurrency()) {
            decls.add(AlloyDecl(D2AStrings.stableName, this.dsl.AlloyOneBool()));
        }

        decls.addAll(this.varFieldsTraces());
        decls.addAll(this.bufferFieldsTraces());

        // add the snapshot signature
        this.am.addPara(new AlloySigPara(AlloyVar(D2AStrings.snapshotName), decls));
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
            List<AlloyExpr> arrow_list = mapBy(dm.varParams(vfqn), i -> AlloyVar(i.paramSig));
            arrow_list.add(this.translateExpr(dm.varTyp(vfqn)));

            // TOCHECK
            // var could be declared in Dash model with type "one X",
            // "some X", or "lone X"
            // or another arrow type
            // so we have to create Id1 -> Id2 -> (one v_type)
            // Not sure this works, might have to include multiplicity
            // in arrow type as in Id1 -> Id2 (-> one) v_type

            // Id1 -> Id2 -> Id3 -> varType
            decls.add(AlloyDecl(DashFQN.translateFQN(vfqn), AlloyArrowExprList(arrow_list)));
        }
        return decls;
    }

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
                //System.out.println(typ.getClass());
                //System.out.println(((ExprUnary) typ).op);
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
