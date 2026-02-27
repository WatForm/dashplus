/*
    All functions here are static
*/

package ca.uwaterloo.watform.alloyast.expr;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import java.util.ArrayList;
import java.util.List;

public class AlloyExprFactory {

    private static Boolean optimizationsOn = true;

    // left set -> set right
    public static AlloyExpr AlloyArrow(AlloyExpr left, AlloyExpr right) {
        return new AlloyArrowExpr(left, right);
    }

    // eList(0) set -> set (eList(1) set -> set eList(2))
    // if eList.size() == 1, it will have the quant of eList.get(0)
    public static AlloyExpr AlloyArrowExprList(List<AlloyExpr> eList) {
        assert (eList != null);
        List<AlloyExpr> reversed = reverse(eList);
        AlloyExpr o = reversed.get(0);
        for (AlloyExpr e : reversed.subList(1, reversed.size())) {
            o = new AlloyArrowExpr(e, o);
        }
        return o;
    }

    // left.right
    public static AlloyExpr AlloyJoin(AlloyExpr left, AlloyExpr right) {
        return new AlloyDotExpr(left, right);
    }

    // elist(0).(elist(1).elist(2))
    public static AlloyExpr AlloyJoinList(List<AlloyExpr> elist) {
        assert (elist != null);
        List<AlloyExpr> reversed = reverse(elist);
        AlloyExpr ret = reversed.get(0);
        for (AlloyExpr el : reversed.subList(1, reversed.size())) {
            ret = new AlloyDotExpr(el, ret);
        }
        return ret;
    }

    // elist(0).(elist(1).expr)
    public static AlloyExpr AlloyJoinList(List<AlloyExpr> elist, AlloyExpr expr) {
        assert (elist != null);
        List<AlloyExpr> reversed = reverse(elist);
        AlloyExpr ret = reversed.get(0);
        for (AlloyExpr el : reversed.subList(1, reversed.size())) {
            ret = new AlloyDotExpr(el, ret);
        }
        ret = new AlloyDotExpr(ret, expr);
        return ret;
    }

    // left and right
    public static AlloyExpr AlloyAnd(AlloyExpr left, AlloyExpr right) {
        if (optimizationsOn && left.equals(AlloyTrueCond())) return right;
        if (optimizationsOn && right.equals(AlloyTrueCond())) return left;
        return new AlloyAndExpr(left, right);
    }

    // left or right
    public static AlloyExpr AlloyOr(AlloyExpr left, AlloyExpr right) {
        if (optimizationsOn && left.equals(AlloyFalseCond())) return right;
        if (optimizationsOn && right.equals(AlloyFalseCond())) return left;
        return new AlloyOrExpr(left, right);
    }

    // left implies right
    public static AlloyExpr AlloyImplies(AlloyExpr left, AlloyExpr right) {
        return new AlloyImpliesExpr(left, right);
    }

    public static AlloyExpr AlloyIff(AlloyExpr left, AlloyExpr right) {
        return new AlloyIffExpr(left, right);
    }

    public static AlloyExpr AlloyAndList(List<AlloyExpr> elist) {
        // does simplifications
        // how get back a real true expr rather than just boolean value at the e3nd??
        if (elist.isEmpty()) return AlloyTrueCond();
        AlloyExpr ret = elist.get(0);
        for (AlloyExpr el : elist.subList(1, elist.size())) {
            ret = AlloyAnd(ret, el);
        }
        return ret;
    }

    public static AlloyExpr AlloyOrList(List<AlloyExpr> elist) {
        // does simplifications
        // how get back a real true expr rather than just boolean value at the e3nd??
        if (elist.isEmpty()) return AlloyFalseCond();
        AlloyExpr ret = elist.get(0);
        for (AlloyExpr el : elist.subList(1, elist.size())) {
            ret = AlloyOr(ret, el);
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

    public static AlloyExpr AlloyEqual(AlloyExpr left, AlloyExpr right) {
        if (optimizationsOn && left.equals(right)) return AlloyTrueCond();
        else return new AlloyEqualsExpr(left, right);
    }

    // a condition that is always true (rather than a value True)
    // can't call createEquals here because that causes an infinite loop
    // as createEquals has an optimization that calls createTrueCond
    public static AlloyExpr AlloyTrueCond() {
        return new AlloyEqualsExpr(AlloyTrue(), AlloyTrue());
    }

    public static AlloyExpr AlloyFalseCond() {
        return new AlloyEqualsExpr(AlloyTrue(), AlloyFalse());
    }

    public static AlloyExpr AlloyNot(AlloyExpr expr) {
        return new AlloyNegExpr(expr);
    }

    // left + right
    public static AlloyExpr AlloyUnion(AlloyExpr left, AlloyExpr right) {
        return new AlloyUnionExpr(left, right);
    }

    // left - right
    public static AlloyExpr AlloyDiff(AlloyExpr left, AlloyExpr right) {
        return new AlloyDiffExpr(left, right);
    }

    // e0 + e1 + e2
    public static AlloyExpr AlloyUnionList(List<AlloyExpr> elist) {
        AlloyExpr ret = null;
        assert (elist != null);
        ret = elist.get(0);
        for (AlloyExpr el : elist.subList(1, elist.size())) {
            ret = new AlloyUnionExpr(ret, el);
        }
        return ret;
    }

    // left :> right
    public static AlloyExpr AlloyInter(AlloyExpr left, AlloyExpr right) {
        return new AlloyIntersExpr(left, right);
    }

    // left :> right
    public static AlloyExpr AlloyRangeRes(AlloyExpr left, AlloyExpr right) {
        return new AlloyRngRestrExpr(left, right);
    }

    // left <: right
    public static AlloyExpr AlloyDomainRes(AlloyExpr left, AlloyExpr right) {
        return new AlloyDomRestrExpr(left, right);
    }

    // all decls sub
    public static AlloyExpr AlloyAllVars(List<AlloyDecl> decls, AlloyExpr sub) {
        return new AlloyQuantificationExpr(AlloyQuantificationExpr.Quant.ALL, decls, sub);
    }

    // some decls sub
    public static AlloyExpr AlloySomeVars(List<AlloyDecl> decls, AlloyExpr sub) {
        return new AlloyQuantificationExpr(AlloyQuantificationExpr.Quant.SOME, decls, sub);
    }

    // all sub
    public static AlloyExpr AlloySome(AlloyExpr sub) {
        return new AlloyQtExpr(AlloyQtEnum.SOME, sub);
    }

    // lone sub
    public static AlloyExpr AlloyLone(AlloyExpr sub) {
        return new AlloyQtExpr(AlloyQtEnum.LONE, sub);
    }

    // no sub
    public static AlloyExpr AlloyNo(AlloyExpr sub) {
        return new AlloyQtExpr(AlloyQtEnum.NO, sub);
    }

    // set sub
    public static AlloyExpr AlloySet(AlloyExpr sub) {
        return new AlloyQtExpr(AlloyQtEnum.SET, sub);
    }

    // vars ----------------------------

    public static AlloyQnameExpr AlloyVar(String s) {
        return new AlloyQnameExpr(s);
    }

    // none
    public static AlloyNoneExpr AlloyNone() {
        return new AlloyNoneExpr();
    }

    // the value true
    public static AlloyQnameExpr AlloyTrue() {
        return AlloyVar(AlloyStrings.trueName);
    }

    // the value false
    public static AlloyQnameExpr AlloyFalse() {
        return AlloyVar(AlloyStrings.falseName);
    }

    public static boolean isVar(AlloyExpr expr) {
        return expr instanceof AlloyQnameExpr;
    }

    // decls -----------

    public static AlloyDecl AlloyDecl(String s, AlloyQtEnum mul, AlloyExpr expr) {
        return new AlloyDecl(AlloyVar(s), mul, expr);
    }

    // e.g., decl x:expr within all x:expr | ...
    // should be all s: one expr | ...
    // always gets multiplicity of ONE
    public static AlloyDecl AlloyQuantVar(String x, AlloyExpr expr) {
        return new AlloyDecl(AlloyVar(x), AlloyQtEnum.ONE, expr);
    }

    // e.g., decl x:expr within pred YY [x: expr]
    // should be pred YY [x: one expr]
    // always gets multiplicity of ONE
    public static AlloyDecl AlloyPredArg(String x, AlloyExpr expr) {
        return new AlloyDecl(AlloyVar(x), AlloyQtEnum.ONE, expr);
    }

    public static AlloyExpr AlloyPredCall(String name, List<AlloyExpr> elist) {
        return new AlloyBracketExpr(new AlloyQnameExpr(name), elist);
    }

    public static AlloyExpr AlloyIn(AlloyExpr left, AlloyExpr right) {
        return new AlloyCmpExpr(left, false, AlloyCmpExpr.Comp.IN, right);
    }

    public static AlloyExpr AlloyIte(AlloyExpr cond, AlloyExpr conseq, AlloyExpr alt) {
        if (cond.equals(AlloyTrueCond())) return conseq;
        else if (cond.equals(AlloyFalseCond())) return alt;
        else if (conseq.equals(alt)) return conseq;
        else return new AlloyIteExpr(cond, conseq, alt);
    }
}
