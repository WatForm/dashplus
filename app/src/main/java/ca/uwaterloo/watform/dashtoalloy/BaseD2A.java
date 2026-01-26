/*

    This is the Base class of the translator.
    Everything else extends from this.
    It contains the DashModel dm, and any options needed
    throughout all parts of the translation.
*/

package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import java.util.*;

public class BaseD2A {

    protected DashModel dm; // input
    protected AlloyModel am = new AlloyModel(); // output
    protected boolean isElectrum = false;
    protected boolean isTraces = true;
    protected boolean isTcmc = false;
    protected DSL dsl;
    protected ExprTranslatorVis exprTranslator;
    protected CollectDashRefVis collectDashRef;

    protected BaseD2A(DashModel dm, boolean isElectrum) {
        this.dm = dm;
        this.isElectrum = isElectrum;
        this.dsl = new DSL(dm, isElectrum);
        this.exprTranslator = new ExprTranslatorVis(dm, isElectrum);
    }

    protected AlloyExpr translateExpr(AlloyExpr expr) {
        return this.exprTranslator.translateExpr(expr);
    }

    protected List<DashRef> collectDashRefs(AlloyExpr expr) {
        return this.collectDashRef.visit(expr);
    }
}
