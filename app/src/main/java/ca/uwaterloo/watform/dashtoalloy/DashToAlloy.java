// usage: AlloyModel am = new DashToAlloy(dm, isElectrum).translate()

package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;

public class DashToAlloy extends SpaceSignatures {

    public DashToAlloy(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }
    public DashToAlloy(DashModel dm) {
        super(dm, false);
    }

    public AlloyModel translate() {
        this.am = new AlloyModel();
        // TODO: copy all Alloy stuff from dm into am
        this.addSpaceSignatures();
        return this.am;
    }
}
