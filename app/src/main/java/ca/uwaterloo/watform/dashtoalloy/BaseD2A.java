/*

    This is the Base class of the translator.
    Everything else extends from this.
    It contains the DashModel dm, and any options needed
    throughout all parts of the translation.
*/

package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;

public class BaseD2A {

    protected DashModel dm; // input
    protected AlloyModel am = new AlloyModel(); // output
    protected boolean isElectrum = false;
    protected DSL dsl;
    protected ExprTranslatorVis exprTranslator;

    protected BaseD2A(DashModel dm, boolean isElectrum) {
        this.dm = dm;
        this.isElectrum = isElectrum;
        DSL dsl = new DSL(dm, isElectrum);
        this.exprTranslator = new ExprTranslatorVis(dm, isElectrum);
    }
}
