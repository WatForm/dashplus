// usage: AlloyModel am = new DashToAlloy(dm, isElectrum).translate()

// predicates in Alloy added in bottom-up
// perhaps it should be in top-down order
// Alloy allows both

package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;

public class DashToAlloy extends StutterD2A {

    public DashToAlloy(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public DashToAlloy(DashModel dm) {
        super(dm, false);
    }

    public AlloyModel translate() {
        // copy all Alloy stuff from dm into am
        this.am = dm.copy();

        // state, transition, parameter, buffer spaces
        this.addSpaceSigs();

        this.addSnapshotSig();

        this.addInit();
        this.addInvs();

        for (String tfqn:this.dm.allTransNames()) {
            this.addTransPre(tfqn);
            if (this.dm.hasConcurrency()) {
                this.addTransIsEnabledAfterStep(tfqn);
            }
            this.addTransPost(tfqn);
            this.addTrans(tfqn);
        }
        // one of these for the whole model
        if (this.dm.hasConcurrency()) 
            this.addTestIfNextStable();

        this.addSmallStep();

        // add stutter?
        // this.addStutter();

        if (this.isTcmc) {
            // next two are required
            this.addTcmcFact();
            this.addAllSnapshotsDiffFact();

            // next ones can be used along with property checking
            this.addStrongNoStutter();
            // only useful for Tcmc
            this.addReachability();

            

        } else if (this.isTraces) {
            // next two are required
            this.addTracesFact();
            this.addAllSnapshotsDiffFact();
            // everything in traces is reachable b/c it
            // starts from initial state and only takes steps
            // it can reach

            // optional predicate for use in property checking
            this.addStrongNoStutter();

        } else if (this.isElectrum) {
            // this one is required
            // all snapshots are automatically different in electrum
            this.addElectrumFact();

        }
        
        // these predicates may be useful in any of the above
        this.addSingleEventInput();
        this.addCompleteBigSteps();
        this.addEnoughOps(); 
        return this.am;
    }

    public AlloyModel translateVarBufferSigsOnly() {
        this.am = dm.copy();
        this.addParamSpaceSigs();
        this.varsBuffersOnlySnapshotSig();
        return this.am;
    }
}
