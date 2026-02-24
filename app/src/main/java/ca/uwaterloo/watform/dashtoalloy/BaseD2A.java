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

    public enum Options {
        electrum,
        traces,
        tcmc
    }

    protected DashModel dm; // input
    protected AlloyModel am = new AlloyModel(); // output
    protected boolean isElectrum = false;
    protected boolean isTraces = false;
    protected boolean isTcmc = false;
    protected DSL dsl;

    protected BaseD2A(DashModel dm, Options opt) {
        this.dm = dm;

        System.out.println(opt);
        switch (opt) {
            case Options.electrum:
                this.isElectrum = true;
                break;
            case Options.traces:
                this.isTraces = true;
                break;
            case Options.tcmc:
                this.isTcmc = true;
                break;
        }
        System.out.println("isElectrum: " + this.isElectrum);
        System.out.println("isTraces: " + this.isTraces);
        System.out.println("isTcmc: " + this.isTcmc);
        this.dsl = new DSL(isElectrum);
    }

    protected AlloyExpr translateExpr(AlloyExpr expr) {
        return new ExprTranslatorVis(dm, isElectrum).translateExpr(expr);
    }

    protected AlloyExpr translateExprOnlyGetName(AlloyExpr expr) {
        return new ExprTranslatorVis(dm, isElectrum).translateExpr(expr, true);
    }

    protected Set<DashRef> collectDashRefs(AlloyExpr expr) {
        return new CollectDashRefVis().collect(expr);
    }
}
