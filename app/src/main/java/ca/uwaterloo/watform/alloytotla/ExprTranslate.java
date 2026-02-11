package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.NULL_SET;
import static ca.uwaterloo.watform.alloytotla.Boilerplate.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaTrue;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyQtExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.tlaast.TlaExp;

public class ExprTranslate {
    public static TlaExp translate(AlloyExpr exp) {
        if (exp instanceof AlloyQtExpr) {
            return translateAlloyQtExpr((AlloyQtExpr) exp);
        } else if (exp instanceof AlloySigRefExpr) {
            return translateAlloySigRefExp((AlloySigRefExpr) exp);
        }
        return NULL_SET();
    }

    public static TlaExp translateAlloyQtExpr(AlloyQtExpr exp) {

        if (exp.qt == AlloyQtExpr.Quant.SOME) return _SOME(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.LONE) return _SOME(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.ONE) return _ONE(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.NO) return _NO(translate(exp.sub));
        return translate(exp.sub);
    }

    public static TlaExp translateAlloySigRefExp(AlloySigRefExpr exp) {
        if (exp instanceof AlloyQnameExpr) {
            String label = ((AlloyQnameExpr) exp).label;
            return TlaVar(label);
        }
        return TlaTrue();
    }
}
