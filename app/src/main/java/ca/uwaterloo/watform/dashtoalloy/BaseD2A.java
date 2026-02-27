/*

    This is the Base class of the translator.
    Everything else extends from this.
    It contains the DashModel dm, and any options needed
    throughout all parts of the translation.
*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.*;

public class BaseD2A {

    public enum Options {
        electrum,
        traces,
        tcmc,
        nothing
    }

    protected DashModel dm; // input
    protected AlloyModel am = new AlloyModel(); // output
    protected boolean isElectrum = false;
    protected boolean isTraces = false;
    protected boolean isTcmc = false;
    protected DSL dsl;

    protected BaseD2A(DashModel dm, Options opt) {
        this.dm = dm;

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
            default:
                throw ImplementationError.shouldNotReach();
        }
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
