package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.Boilerplate.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyCardExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyQtExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyUnivExpr;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlaast.TlaVar;

public class ExprTranslate {

    private static final TlaExp ERROR = new TlaVar("THIS_IS_NOT_SUPPORTED");

    public static TlaExp translate(AlloyExpr exp) {

        // sigRef
        if (exp instanceof AlloySigRefExpr) {
            return translateAlloySigRefExp((AlloySigRefExpr) exp);
        }

        // unary ops
        if (exp instanceof AlloyUnaryExpr) {
            return translateAlloyUnaryExpr((AlloyUnaryExpr) exp);
        }
        if (exp instanceof AlloyQtExpr) {
            return translateAlloyQtExpr((AlloyQtExpr) exp);
        }

        return ERROR;
    }

    public static TlaExp translateAlloyUnaryExpr(AlloyUnaryExpr exp) {
        if (exp instanceof AlloyQtExpr) return translateAlloyQtExpr((AlloyQtExpr) exp);

        if (exp instanceof AlloyCardExpr) return TlaStdLibs.Cardinality(translate(exp));
        return ERROR;
    }

    public static TlaExp translateAlloyQtExpr(AlloyQtExpr exp) {

        if (exp.qt == AlloyQtExpr.Quant.SOME) return _SOME(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.LONE) return _SOME(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.ONE) return _ONE(translate(exp.sub));
        if (exp.qt == AlloyQtExpr.Quant.NO) return _NO(translate(exp.sub));
        return ERROR;
    }

    public static TlaExp translateAlloySigRefExp(AlloySigRefExpr exp) {
        if (exp instanceof AlloyQnameExpr) {
            String label = ((AlloyQnameExpr) exp).label;
            return TlaVar(label);
        }
        if (exp instanceof AlloyUnivExpr) {
            return _UNIV();
        }
        if (exp instanceof AlloyNoneExpr) {
            return _NONE();
        }
        return ERROR;
    }
}
