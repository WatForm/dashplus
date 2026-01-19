package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

import static ca.uwaterloo.watform.dashtoalloy.AlloyHelper.*;

public class SpaceSignaturesD2A extends AlloyInterfaceD2A {

    protected SpaceSignaturesD2A(DashModel dm, boolean isElectrum) {
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

    protected void addSpaceSignatures() {

        if (!this.dm.hasOnlyOneState()) {
            // abstract sig Statelabel {}
            this.addAbstractSig(DashStrings.stateLabelName);
            // abstract sig Root extends StateLabel {}
            this.addAbstractExtendsSig(this.dm.rootName(), DashStrings.stateLabelName);
        }
        if (this.dm.hasConcurrency()) {
            // abstract sig Scopes {}
            this.addAbstractSig(DashStrings.scopeLabelName);
            // one sig Root extends Scopes {}
            this.addOneExtendsSig(
                    this.dm.rootName() + DashStrings.scopeSuffix, DashStrings.scopeLabelName);
        }
        recurseCreateStateSpaceSigs(this.dm.rootName());

        // abstract sig TransLabel {}
        this.addAbstractSig(DashStrings.transitionLabelName);
        // add all transitions as one sig extensions of TransLabel
        for (String t : this.dm.allTransNames()) {
            // one sig tfqn extends TransLabel {}
            this.addOneExtendsSig(DashFQN.translateFQN(t), DashStrings.transitionLabelName);
        }

        if (!this.isElectrum && this.dm.maxDepthParams() != 0) {
            // if this model has parametrized components
            // abstract sig Identifiers {}
            this.addAbstractSig(DashStrings.identifierName);
            for (String s : mapBy(this.dm.allParamsInOrder(), i -> i.paramSig))
                // sig param extends Identifiers {}
                this.addExtendsSig(s, DashStrings.identifierName);
            // the alternative would be for conf1, etc to be fields in
            // sig Identifiers, but the creation of conf1, etc is in
            // SnapshotSignatures
        }

        // events ----------------------
        if (this.dm.hasEvents()) {
            // abstract sig Events {}
            this.addAbstractSig(DashStrings.allEventsName);
            if (this.dm.hasIntEvents()) {
                // abstract sig IntEvents extends Events {}
                this.addAbstractExtendsSig(
                        DashStrings.allInternalEventsName, DashStrings.allEventsName);
                for (String e : this.dm.allIntEvents()) {
                    // sig e extends IntEvents {}
                    this.addOneExtendsSig(
                            DashFQN.translateFQN(e), DashStrings.allInternalEventsName);
                }
            }
            if (this.dm.hasEnvEvents()) {
                // abstract sig EnvEvents extends Events {}
                this.addAbstractExtendsSig(
                        DashStrings.allEnvironmentalEventsName, DashStrings.allEventsName);
                for (String e : this.dm.allEnvEvents()) {
                    // sig e extends EnvEvents {}
                    this.addOneExtendsSig(
                            DashFQN.translateFQN(e), DashStrings.allEnvironmentalEventsName);
                }
            }
        }

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
                        this.addSig(DashStrings.bufferIndexName + this.dm.bufferIndex(b));
                } else
                    // sig BufIndex5 {}
                    this.addSig(DashStrings.bufferIndexName + this.dm.bufferIndex(b));
            }
        }
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
                        DashFQN.translateFQN(child) + DashStrings.scopeSuffix,
                        DashStrings.scopeLabelName);
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
}
