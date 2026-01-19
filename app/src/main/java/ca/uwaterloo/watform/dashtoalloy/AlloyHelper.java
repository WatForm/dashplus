package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlloyHelper {

   public static AlloyQnameExpr AlloyVar(String s) {
        return new AlloyQnameExpr(s);
    }

    public static AlloyDecl AlloyDecl(String s, AlloyExpr expr) {
        return new AlloyDecl(AlloyVar(s), expr);
    }

    // use the library functions isTrue/isFalse to say
    // a value must be true/false
    /*public static AlloyExpr createIsTrue(AlloyExpr e) {
        List<AlloyExpr> elist = new ArrayList<AlloyExpr>();
        elist.add(e);
        return createPredCall(DashStrings.isTrue,elist);
    }
    public static AlloyExpr createIsFalse(AlloyExpr e) {
        List<Expr> elist = new ArrayList<Expr>();
        elist.add(e);
        return createPredCall(DashStrings.isFalse,elist);
    }*/

    // common vars

    public static AlloyExpr AlloyOneBool() {
        return new AlloyQtExpr(AlloyQtExpr.Quant.ONE, AlloyVar(DashStrings.boolName));
    }

       // --------------------------------
    // AlloyVar(sl(0)) -> (AlloyVar(sl(1)) -> AlloyVar(sl(2)))
    public static AlloyExpr ArrowExprFromStringList(List<String> sl) {
        assert (sl != null && !sl.isEmpty());
        Collections.reverse(sl);
        AlloyExpr o = AlloyVar(sl.get(0));
        for (String s : sl.subList(1, sl.size())) {
            o = new AlloyArrowExpr(new AlloyQnameExpr(s), o);
        }
        return o;
    }

    // eList(0) -> (eList(1) -> eList(2))
    public static AlloyExpr ArrowExprFromExprList(List<AlloyExpr> eList) {
        assert (eList != null);
        Collections.reverse(eList);
        AlloyExpr o = eList.get(0);
        for (AlloyExpr e : eList.subList(1, eList.size())) {
            o = new AlloyArrowExpr(e, o);
        }
        return o;
    }

    public static AlloyExpr AlloyJoin(AlloyExpr left, AlloyExpr right) {
        return new AlloyDotExpr(left, right);
    }

    // elist(0).elist(1).elist(2)
    public static AlloyExpr AlloyJoinFromExprList(List<AlloyExpr> elist) {
        assert (elist != null);
        Collections.reverse(elist);
        AlloyExpr ret = elist.get(0);
        for (AlloyExpr el : elist.subList(1, elist.size())) {
            ret = new AlloyDotExpr(el, ret);
        }
        return ret;
    }

    // [ AlloyVar(vList(0)), AlloyVar(vList(1)), ...]
    public static List<AlloyQnameExpr> AlloyVarList(List<String> vList) {
        List<AlloyQnameExpr> retList = new ArrayList<AlloyQnameExpr>();
        for (String v : vList) {
            retList.add(AlloyVar(v));
        }
        return retList;
    }
}