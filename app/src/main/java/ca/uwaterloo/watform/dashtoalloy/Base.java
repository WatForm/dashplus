/*

    This is the Base class of the translator.
    Everything else extends from this.
    It contains the DashModel dm, and any options needed
    throughout all parts of the translation.
*/

package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;

public class Base {

    DashModel dm; // input
    AlloyModel am = new AlloyModel(); // output
    boolean isElectrum = false;

    protected Base(DashModel dm, boolean isElectrum) {
        this.dm = dm;
        this.isElectrum = isElectrum;
    }
}
