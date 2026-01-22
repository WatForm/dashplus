package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.dashtoalloy.AlloyHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

// import ca.uwaterloo.watform.dashast.D2AStrings;

public class SpaceSigsD2A extends AlloyModelInterfaceD2A {

    protected SpaceSigsD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
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

        abstract sig Identifiers {}
        sig param extends Identifiers {}

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
        // no space sigs needs to be added for variables
        
    }

    private void addConfSpaceSigs() {

        if (!this.dm.hasOnlyOneState()) {
            // abstract sig Statelabel {}
            this.addAbstractSig(D2AStrings.stateLabelName);
            // abstract sig Root extends StateLabel {}
            this.addAbstractExtendsSig(this.dm.rootName(), D2AStrings.stateLabelName);
        }
        if (this.dm.hasConcurrency()) {
            // abstract sig Scopes {}
            this.addAbstractSig(D2AStrings.scopeLabelName);
            // one sig Root extends Scopes {}
            this.addOneExtendsSig(
                    this.dm.rootName() + D2AStrings.scopeSuffix, D2AStrings.scopeLabelName);
        }
        recurseCreateStateSpaceSigs(this.dm.rootName());
    }

    private void recurseCreateStateSpaceSigs(String parent) {
        for (String child : this.dm.immChildren(parent)) {
            // for scopes Used
            // Root node can be used for both conf and scopesUsed
            // but o/w concurrent nodes are abstract for conf
            // and one sigs for scopesUsed
            if (this.dm.hasConcurrency() && this.dm.isAnd(child))
                // one sig childScope extends Scopes {}
                this.addOneExtendsSig(
                        DashFQN.translateFQN(child) + D2AStrings.scopeSuffix,
                        D2AStrings.scopeLabelName);
            // for conf
            if (!this.dm.hasOnlyOneState()) {
                if (this.dm.isLeaf(child))
                    // one child extends parent {}
                    this.addOneExtendsSig(
                            DashFQN.translateFQN(child), DashFQN.translateFQN(parent));
                else {
                    // abstract child extends parent {}
                    this.addAbstractExtendsSig(
                            DashFQN.translateFQN(child), DashFQN.translateFQN(parent));
                }
            }
            if (!this.dm.isLeaf(child)) recurseCreateStateSpaceSigs(child);
        }
    }

    private void addTransSpaceSigs() {
        // abstract sig TransLabel {}
        this.addAbstractSig(D2AStrings.transitionLabelName);
        // add all transitions as one sig extensions of TransLabel
        for (String t : this.dm.allTransNames()) {
            // one sig tfqn extends TransLabel {}
            this.addOneExtendsSig(DashFQN.translateFQN(t), D2AStrings.transitionLabelName);
        }
    }

    public void addParamSpaceSigs() {
        if (!this.isElectrum && this.dm.maxDepthParams() != 0) {
            // if this model has parametrized components
            // abstract sig Identifiers {}
            this.addAbstractSig(D2AStrings.identifierName);
            for (String s : mapBy(this.dm.allParamsInOrder(), i -> i.paramSig))
                // sig param extends Identifiers {}
                this.addExtendsSig(s, D2AStrings.identifierName);
            // the alternative would be for conf1, etc to be fields in
            // sig Identifiers, but the creation of conf1, etc is in
            // SnapshotSignatures
        }
    }

    private void addEventSpaceSigs() {
        if (this.dm.hasEvents()) {
            // abstract sig Events {}
            this.addAbstractSig(D2AStrings.allEventsName);
            if (this.dm.hasIntEvents()) {
                // abstract sig IntEvents extends Events {}
                this.addAbstractExtendsSig(
                        D2AStrings.allInternalEventsName, D2AStrings.allEventsName);
                for (String e : this.dm.allIntEvents()) {
                    // sig e extends IntEvents {}
                    this.addOneExtendsSig(
                            DashFQN.translateFQN(e), D2AStrings.allInternalEventsName);
                }
            }
            if (this.dm.hasEnvEvents()) {
                // abstract sig EnvEvents extends Events {}
                this.addAbstractExtendsSig(
                        D2AStrings.allEnvironmentalEventsName, D2AStrings.allEventsName);
                for (String e : this.dm.allEnvEvents()) {
                    // sig e extends EnvEvents {}
                    this.addOneExtendsSig(
                            DashFQN.translateFQN(e), D2AStrings.allEnvironmentalEventsName);
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
            List<String> allbfqns = this.dm.allBufferNames();
            for (String b : allbfqns) {
                if (this.isElectrum) {
                    if (this.dm.bufferParams(b).size() != 0)
                        // because buffer is declared under param
                        // o/w declared with buffer index in Snapshot stuff
                        // sig BufIdx0 {}
                        this.addSig(D2AStrings.bufferIndexName + this.dm.bufferIndex(b));
                } else
                    // sig BufIndex5 {}
                    this.addSig(D2AStrings.bufferIndexName + this.dm.bufferIndex(b));
            }
        }
    }

}
