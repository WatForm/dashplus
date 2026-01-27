/*

    This is the Base class of the translator.
    Everything else extends from this.
    It contains the DashModel dm, and any options needed
    throughout all parts of the translation.
*/

package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.*;

public class BaseD2A {

    public enum TranslateOutput {
        ELECTRUM,
        TRACES,
        TCMC
    }

    protected DashModel dm; // input
    protected AlloyModel am = new AlloyModel(); // output
    protected boolean isElectrum = false;
    protected boolean isTraces = false;
    protected boolean isTcmc = false;
    protected DSL dsl;
    protected ExprTranslatorVis exprTranslator;

    protected BaseD2A(DashModel dm, TranslateOutput opt) {
        this.dm = dm;

        switch (opt) {
            case TranslateOutput.ELECTRUM:
                this.isElectrum = true;
                break;
            case TranslateOutput.TRACES:
                this.isTraces = true;
                break;
            case TranslateOutput.TCMC:
                this.isTcmc = true;
                break;
        }

        this.isElectrum = isElectrum;
        this.dsl = new DSL(dm, isElectrum);
        this.exprTranslator = new ExprTranslatorVis(dm, isElectrum);
    }

    protected AlloyExpr translateExpr(AlloyExpr expr) {
        return this.exprTranslator.translateExpr(expr);
    }

    protected List<DashRef> collectDashRefs(AlloyExpr expr) {
        // has to be a new one every time
        // to empty out collector variable inside object
        return new CollectDashRefVis().visit(expr);
    }
}
