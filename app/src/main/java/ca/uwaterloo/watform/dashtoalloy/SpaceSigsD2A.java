package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyImportPara;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

// import ca.uwaterloo.watform.dashast.D2AStrings;

public class SpaceSigsD2A extends BaseD2A {

    protected SpaceSigsD2A(DashModel dm, Options opt) {
        super(dm, opt);
    }

    /*
        abstract sig Statelabel {}
        abstract sig Root extends StateLabel {}

        abstract sig Scopes {}
        one sig Root extends Scopes {}

        one sig childScope extends Scopes {}
        abstract child extends parent {}
        one child extends parent {}

        abstract sig TransLabel {}
        one sig tfqn extends TransLabel {}

        // assume user declares sig param1 {}, etc
        sig Identifiers in param1 + param2 + param3 {}
        fact {
            Identiers = parma1 + param2 + param3
        }

        abstract sig Events {}
        abstract sig IntEvents extends Events {}
        abstract sig EnvEvents extends Events {}
        sig e extends IntEvents {}
        sig e extends EnvEvents {}

        TODO: something about buffers
    */

    protected void addSpaceSigs() {

        this.addConfSpaceSigs();
        this.addTransSpaceSigs();
        this.addParamSpaceSigs();
        this.addEventSpaceSigs();
        this.addBufferSpaceSigs(); // Identifiers
        // no space sigs needs to be added for variables

    }

    private void addConfSpaceSigs() {

        if (!this.dm.hasOnlyOneState()) {
            // abstract sig Statelabel {}
            this.am.addAbstractSig(D2AStrings.stateLabelName);
            // abstract sig Root extends StateLabel {}
            this.am.addAbstractExtendsSig(this.dm.rootName(), D2AStrings.stateLabelName);
        }
        if (this.dm.hasConcurrency()) {
            // abstract sig Scopes {}
            this.am.addAbstractSig(D2AStrings.scopeLabelName);
            // one sig Root extends Scopes {}
            this.am.addOneExtendsSig(
                    this.dm.rootName() + D2AStrings.scopeSuffix, D2AStrings.scopeLabelName);
        }
        recurseCreateStateSpaceSigs(this.dm.rootName());
    }

    private void recurseCreateStateSpaceSigs(String parent) {
        for (String child : this.dm.immChildren(parent)) {
            // Root node can be used for both conf and scopesUsed
            // but o/w concurrent nodes are abstract for conf
            // and one sigs for scopesUsed

            // conf
            if (!this.dm.hasOnlyOneState()) {
                if (this.dm.isLeaf(child))
                    // one child extends parent {}
                    this.am.addOneExtendsSig(
                            DashFQN.translateFQN(child), DashFQN.translateFQN(parent));
                else {
                    // abstract child extends parent {}
                    this.am.addAbstractExtendsSig(
                            DashFQN.translateFQN(child), DashFQN.translateFQN(parent));
                }
            }
            // scopesUsed
            if (this.dm.hasConcurrency() && this.dm.isAnd(child))
                // one sig childScope extends Scopes {}
                this.am.addOneExtendsSig(
                        DashFQN.translateFQN(child) + D2AStrings.scopeSuffix,
                        D2AStrings.scopeLabelName);

            if (!this.dm.isLeaf(child)) recurseCreateStateSpaceSigs(child);
        }
    }

    private void addTransSpaceSigs() {
        // abstract sig TransLabel {}
        this.am.addAbstractSig(D2AStrings.transLabelName);
        // add all transitions as one sig extensions of TransLabel
        for (String t : this.dm.allTransNames()) {
            // one sig tfqn extends TransLabel {}
            this.am.addOneExtendsSig(DashFQN.translateFQN(t), D2AStrings.transLabelName);
        }
    }

    public void addParamSpaceSigs() {
        if (!this.isElectrum && this.dm.maxDepthParams() != 0) {
            // if this model has parametrized components

            AlloyExpr identifiersVar = AlloyVar(D2AStrings.identifierName);
            // the next two lines were very tricky to get the types for
            // sig Identifiers extends param1 + param2 + etc. {}
            this.am.addInSig(
                    D2AStrings.identifierName,
                    mapBy(this.dm.allParams(), x -> AlloyVar(x.paramSig)));
            // Identifiers = param1 + param2 + etc
            this.am.addFact(
                    D2AStrings.paramsFact,
                    AlloyEqual(
                            identifiersVar,
                            AlloyUnion(mapBy(this.dm.allParams(), x -> AlloyVar(x.paramSig)))));
        }
    }

    private void addEventSpaceSigs() {
        if (this.dm.hasEvents()) {
            // abstract sig Events {}
            this.am.addAbstractSig(D2AStrings.allEventsName);
            if (this.dm.hasIntEvents()) {
                // abstract sig IntEvents extends Events {}
                this.am.addAbstractExtendsSig(
                        D2AStrings.allIntEventsName, D2AStrings.allEventsName);
                for (String e : this.dm.allIntEvents()) {
                    // sig e extends IntEvents {}
                    this.am.addOneExtendsSig(DashFQN.translateFQN(e), D2AStrings.allIntEventsName);
                }
            }
            if (this.dm.hasEnvEvents()) {
                // abstract sig EnvEvents extends Events {}
                this.am.addAbstractExtendsSig(
                        D2AStrings.allEnvEventsName, D2AStrings.allEventsName);
                for (String e : this.dm.allEnvEvents()) {
                    // sig e extends EnvEvents {}
                    this.am.addOneExtendsSig(DashFQN.translateFQN(e), D2AStrings.allEnvEventsName);
                }
            }
        }
    }

    private void addBufferSpaceSigs() {
        // don't understand this comment???
        // abstract sig BufIdx {} if this model has buffers
        // if not Electrum or if this buffer would be grouped under a parameter in Electrum
        // declaration
        // o/w buffers have to be fields in sig BufIdx
        // and this functionality is within AddSnapshotSignatures

        // this code does not seem correct
        if (this.dm.hasBuffers()) {
            // buffers with parameters
            // every buffer has a different index
            // so just one decl per sig
            for (String bfqn : this.dm.allBufferNames()) {
                if (this.isElectrum) {
                    if (this.dm.bufferParams(bfqn).size() != 0)
                        // because buffer is declared under param
                        // o/w declared with buffer index in Snapshot stuff
                        // sig BufIdx0 {}
                        this.am.addSig(this.dsl.bufferIndexSig(this.dm.bufferIndex(bfqn)));
                } else
                    // sig BufIndex5 {}
                    this.am.addSig(this.dsl.bufferIndexSig(this.dm.bufferIndex(bfqn)));

                // import util/buffer[BufIdx, elem]
                this.am.addPara(
                        new AlloyImportPara(
                                false,
                                new AlloyQnameExpr(
                                        List.of(
                                                new AlloyNameExpr(D2AStrings.utilName),
                                                new AlloyNameExpr(
                                                        D2AStrings.utilBufferName))), // util/buffer
                                List.of(
                                        this.dsl.bufferIndexVar(this.dm.bufferIndex(bfqn)),
                                        AlloyVar(this.dm.bufferElement(bfqn))), // [BufIdx, elem]
                                null // no "as"
                                ));
            }
        }
    }
}
