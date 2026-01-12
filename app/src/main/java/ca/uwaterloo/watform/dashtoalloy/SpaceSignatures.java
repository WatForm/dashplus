package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;

public class SpaceSignatures {

    DashModel dm; // input
    AlloyModel am = new AlloyModel(); // output

    protected SpaceSignatures(DashModel dm) {
        this.dm = dm;
    }

    protected void addSpaceSignatures() {}
}
