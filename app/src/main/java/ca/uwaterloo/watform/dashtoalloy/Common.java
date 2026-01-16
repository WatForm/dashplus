/*
    This is a DSL for translated expressions.
*/

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

public class Common extends Base {

    protected Common(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public AlloyQnameExpr AlloyVar(String s) {
        return new AlloyQnameExpr(s);
    }

    public AlloyDecl AlloyDecl(String s, AlloyExpr expr) {
        return new AlloyDecl(AlloyVar(s), expr);
    }

    // common vars

    public AlloyExpr AlloyOneBool() {
        return new AlloyQtExpr(AlloyQtExpr.Quant.ONE, AlloyVar(DashStrings.boolName));
    }

    // s
    public AlloyQnameExpr curVar() {
        return AlloyVar(DashStrings.curName);
    }

    // snext
    public AlloyQnameExpr nextVar() {
        return AlloyVar(DashStrings.nextName);
    }

    public AlloyQnameExpr bufferIndexVar(int i) {
        return AlloyVar(DashStrings.bufferIndexName + i);
    }

    // Electrum only
    // e'
    public AlloyUnaryExpr primedVarExpr(AlloyQnameExpr e) {
        assert (this.isElectrum);
        return new AlloyPrimeExpr(e);
    }

    // (s.name).e
    public AlloyExpr curJoinExpr(AlloyQnameExpr e) {
        if (this.isElectrum) return e;
        else return new AlloyDotExpr(curVar(), e);
    }

    // snext.name
    public AlloyExpr nextJoinExpr(AlloyQnameExpr e) {
        if (this.isElectrum) {
            return primedVarExpr(e);
        } else {
            return new AlloyDotExpr(nextVar(), e);
        }
    }

    // AlloyVar(sl(0)) -> (AlloyVar(sl(1)) -> AlloyVar(sl(2)))
    public AlloyExpr ArrowExprFromStringList(List<String> sl) {
        assert (sl != null && !sl.isEmpty());
        Collections.reverse(sl);
        AlloyExpr o = AlloyVar(sl.get(0));
        for (String s : sl.subList(1, sl.size())) {
            o = new AlloyArrowExpr(new AlloyQnameExpr(s), o);
        }
        return o;
    }

    // eList(0) -> (eList(1) -> eList(2))
    public AlloyExpr ArrowExprFromExprList(List<AlloyExpr> eList) {
        assert (eList != null);
        Collections.reverse(eList);
        AlloyExpr o = eList.get(0);
        for (AlloyExpr e : eList.subList(1, eList.size())) {
            o = new AlloyArrowExpr(e, o);
        }
        return o;
    }

    public AlloyExpr AlloyJoin(AlloyExpr left, AlloyExpr right) {
        return new AlloyDotExpr(left, right);
    }

    // elist(0).elist(1).elist(2)
    public AlloyExpr AlloyJoinFromExprList(List<AlloyExpr> elist) {
        assert (elist != null);
        Collections.reverse(elist);
        AlloyExpr ret = elist.get(0);
        for (AlloyExpr el : elist.subList(1, elist.size())) {
            ret = new AlloyDotExpr(el, ret);
        }
        return ret;
    }

    // [ AlloyVar(vList(0)), AlloyVar(vList(1)), ...]
    public List<AlloyQnameExpr> AlloyVarList(List<String> vList) {
        List<AlloyQnameExpr> retList = new ArrayList<AlloyQnameExpr>();
        for (String v : vList) {
            retList.add(AlloyVar(v));
        }
        return retList;
    }

    // adding: sig "n" {}
    public void addSig(String n) {
        this.am.addPara(new AlloySigPara(n));
    }

    // adding: abstract sig "n" {}
    public void addAbstractSig(String n) {
        this.am.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ABSTRACT),
                        List.of(new AlloyQnameExpr(n)),
                        null,
                        Collections.emptyList(),
                        null));
    }

    // adding: abstract sig "child" extends "parent" {}
    public void addAbstractExtendsSig(String child, String parent) {
        this.am.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ABSTRACT),
                        List.of(new AlloyQnameExpr(child)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parent)),
                        Collections.emptyList(),
                        null));
    }

    // adding: one sig child extends parent {}
    public void addOneExtendsSig(String child, String parent) {
        this.am.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ONE),
                        List.of(new AlloyQnameExpr(child)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parent)),
                        Collections.emptyList(),
                        null));
    }

    // adding: sig child extends parent {}
    public void addExtendsSig(String child, String parent) {
        this.am.addPara(
                new AlloySigPara(
                        Collections.emptyList(),
                        List.of(new AlloyQnameExpr(child)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parent)),
                        Collections.emptyList(),
                        null));
    }
}
