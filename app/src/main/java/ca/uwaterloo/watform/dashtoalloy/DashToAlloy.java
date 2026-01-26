// usage: AlloyModel am = new DashToAlloy(dm, isElectrum).translate()

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
        this.addSpaceSigs();
        this.addSnapshotSig();
        this.addInit();
        this.addInvs();
        return this.am;
    }

    public AlloyModel translateVarBufferSigsOnly() {
        this.am = dm.copy();
        this.addParamSpaceSigs();
        this.varsBuffersOnlySnapshotSig();
        return this.am;
    }
}
