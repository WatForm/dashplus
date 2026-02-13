/*
    A place to start with DashModels (DM).

    Contains some static functions about what
    is supported in expressions or not.
*/

package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFile;

public class BaseDM extends AlloyModel {

    public BaseDM() {
        super(null);
    }

    public BaseDM(DashFile d) {
        // addModelSatCmd
        super((AlloyFile) d);
    }

    public static boolean supportedBuiltinVarExpr(AlloyVarExpr varExpr) {
        // these aren't supported in Dash
        return !(
        // this aren't supported in Dash Expr
        varExpr instanceof AlloyAtNameExpr
                || varExpr instanceof AlloyNameExpr
                || varExpr instanceof AlloyThisExpr);
    }

    public static boolean supportedUnaryExpr(AlloyUnaryExpr unaryExpr) {
        // these aren't supported in Dash
        return !(unaryExpr instanceof AlloyAfterExpr
                || unaryExpr instanceof AlloyAlwaysExpr
                || unaryExpr instanceof AlloyBeforeExpr
                || unaryExpr instanceof AlloyEventuallyExpr
                || unaryExpr instanceof AlloyHistoricallyExpr
                || unaryExpr instanceof AlloyOnceExpr);
    }

    public static boolean supportedBinaryExpr(AlloyBinaryExpr binExpr) {
        // these aren't supported in Dash
        return !(binExpr instanceof AlloyReleasesExpr
                || binExpr instanceof AlloySinceExpr
                || binExpr instanceof AlloyUntilExpr);
    }
}
