/*
    This is a DSL (common subexpressions)
    for the Dash to Alloy translator.

    A few of these expressions depend on whether
    we are translating to Electrum or not, thus
    a bit of state is needed.

*/

package ca.uwaterloo.watform.dashtoalloy;

// Factory method
import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.*;
import ca.uwaterloo.watform.exprvisitor.ContainsVarExprVis;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DSL {

    boolean isElectrum = false;

    public DSL(boolean isElectrum) {
        this.isElectrum = isElectrum;
    }

    public String nameNum(String s, Integer i) {
        return s + Integer.toString(i);
    }

    // s
    public AlloyQnameExpr curVar() {
        return AlloyVar(D2AStrings.curName);
    }

    // snext
    public AlloyQnameExpr nextVar() {
        return AlloyVar(D2AStrings.nextName);
    }

    // [s,snext]
    public List<AlloyExpr> curNextVars() {
        List<AlloyExpr> o = this.emptyExprList();
        if (!this.isElectrum) {
            o.add(curVar());
            o.add(nextVar());
        }
        return o;
    }

    // [p0_AID,p1_AID,...]
    public List<AlloyExpr> paramVars(List<DashParam> prs) {
        List<AlloyExpr> o = this.emptyExprList();
        for (DashParam p : prs) o.add(p.asAlloyVar());
        return o;
    }

    // [s, p1, p2, ...]
    public List<AlloyExpr> curParamVars(List<DashParam> prs) {
        List<AlloyExpr> o = this.emptyExprList();
        o.add(curVar());
        o.addAll(paramVars(prs));
        return o;
    }

    // [s', p1, p2, ...]
    public List<AlloyExpr> nextParamVars(List<DashParam> prs) {
        List<AlloyExpr> o = this.emptyExprList();
        o.add(nextVar());
        o.addAll(paramVars(prs));
        return o;
    }

    // [s,s',  p1,p2,...]
    public List<AlloyExpr> curNextParamVars(List<DashParam> prs) {
        List<AlloyExpr> o = new ArrayList<AlloyExpr>(curNextVars());
        o.addAll(paramVars(prs));
        return o;
    }

    // scopesUsed1
    public AlloyQnameExpr scopesUsedVar(int size) {
        return AlloyVar(D2AStrings.scopesUsedName + size);
    }

    // scope1
    public AlloyQnameExpr scopeVar(int size) {
        return AlloyVar(D2AStrings.scopeName + size);
    }

    public AlloyQnameExpr genEventVar(int size) {
        return AlloyVar(D2AStrings.genEventName + size);
    }

    // conf1
    public AlloyQnameExpr confVar(int size) {
        return AlloyVar(D2AStrings.confName + size);
    }

    // events1
    public AlloyQnameExpr eventsVar(int size) {
        return AlloyVar(D2AStrings.eventsName + size);
    }

    // transTaken1
    public AlloyQnameExpr transTakenVar(int size) {
        return AlloyVar(D2AStrings.transTakenName + size);
    }

    // bufIdex0
    public AlloyQnameExpr bufferIndexVar(int i) {
        return AlloyVar(D2AStrings.bufferIndexName + i);
    }

    // stable
    public AlloyQnameExpr stable() {
        return AlloyVar(D2AStrings.stableName);
    }

    // events
    public AlloyExpr allEventsVar() {
        return AlloyVar(D2AStrings.allEventsName);
    }

    // intEvents
    public AlloyExpr allIntEventsVar() {
        return AlloyVar(D2AStrings.allIntEventsName);
    }

    // intEvents
    public AlloyExpr allEnvEventsVar() {
        return AlloyVar(D2AStrings.allEnvEventsName);
    }

    // Electrum only
    // e'
    public AlloyUnaryExpr primedVarExpr(AlloyQnameExpr e) {
        assert (this.isElectrum);
        return new AlloyPrimeExpr(e);
    }

    // ----------------------------

    // (s.name).e
    public AlloyExpr curJoinExpr(AlloyExpr e) {
        if (this.isElectrum) return e;
        else return new AlloyDotExpr(curVar(), e);
    }

    // snext.name
    public AlloyExpr nextJoinExpr(AlloyExpr e) {
        if (this.isElectrum && e instanceof AlloyQnameExpr) {
            return primedVarExpr((AlloyQnameExpr) e);
        } else {
            return new AlloyDotExpr(nextVar(), e);
        }
    }

    // s.conf1
    public AlloyExpr curConf(int size) {
        return curJoinExpr(confVar(size));
    }

    // snext.conf1
    public AlloyExpr nextConf(int size) {
        return nextJoinExpr(confVar(size));
    }

    // s.events1
    public AlloyExpr curEvents(int size) {
        return curJoinExpr(eventsVar(size));
    }

    // snext.events1
    public AlloyExpr nextEvents(int size) {
        return nextJoinExpr(eventsVar(size));
    }

    // s.scopeUsed1
    public AlloyExpr curScopesUsed(int size) {
        return curJoinExpr(scopesUsedVar(size));
    }

    // snext.scopesUsed1
    public AlloyExpr nextScopesUsed(int size) {
        return nextJoinExpr(scopesUsedVar(size));
    }

    // s.transTaken1
    public AlloyExpr curTransTaken(int size) {
        return curJoinExpr(transTakenVar(size));
    }

    // snext.transTaken1
    public AlloyExpr nextTransTaken(int size) {
        return nextJoinExpr(transTakenVar(size));
    }

    // s.stable
    public AlloyExpr curStable() {
        return curJoinExpr(stable());
    }

    // snext.stable
    public AlloyExpr nextStable() {
        return nextJoinExpr(stable());
    }

    // ------------------------------------

    // s.stable == boolean/True
    public AlloyExpr curStableTrue() {
        return AlloyIsTrue(curJoinExpr(stable()));
    }

    // s.stable == boolean/False
    public AlloyExpr curStableFalse() {
        return AlloyIsFalse(curJoinExpr(stable()));
        // return createEquals(curJoinExpr(stable()),createFalse());
    }

    // snext.stable == boolean/True
    public AlloyExpr nextStableTrue() {
        return AlloyIsTrue(nextJoinExpr(stable()));
    }

    // snext.stable == boolean/False
    public AlloyExpr nextStableFalse() {
        return AlloyIsFalse(nextJoinExpr(stable()));
    }

    // -------------------------------------
    // use the library functions isTrue/isFalse to say
    // a value must be true/false
    public AlloyExpr AlloyIsTrue(AlloyExpr e) {
        List<AlloyExpr> elist = this.emptyExprList();
        elist.add(e);
        return AlloyPredCall(AlloyStrings.isTrue, elist);
    }

    public AlloyExpr AlloyIsFalse(AlloyExpr e) {
        List<AlloyExpr> elist = this.emptyExprList();
        elist.add(e);
        return AlloyPredCall(AlloyStrings.isFalse, elist);
    }

    // decls ---------------------------
    // s:Snapshot
    public AlloyDecl curDecl() {
        return new AlloyDecl(D2AStrings.curName, D2AStrings.snapshotName);
    }

    // snext:Snapshot
    public AlloyDecl nextDecl() {
        return new AlloyDecl(D2AStrings.nextName, D2AStrings.snapshotName);
    }

    // [s:Snapshot]
    public List<AlloyDecl> curDecls() {
        List<AlloyDecl> o = this.emptyDeclList();
        if (!this.isElectrum) o.add(curDecl());
        return o;
    }

    // [snext:Snapshot]
    public List<AlloyDecl> nextDecls() {
        List<AlloyDecl> o = this.emptyDeclList();
        if (!this.isElectrum) o.add(nextDecl());
        return o;
    }

    // [s:Snapshot, snext:Snapshot]
    public List<AlloyDecl> curNextDecls() {
        List<AlloyDecl> o = this.emptyDeclList();
        if (!this.isElectrum) {
            o.add(curDecl());
            o.add(nextDecl());
        }
        return o;
    }

    // [p0:P0, p1:P1, ...]
    public List<AlloyDecl> paramDecls(List<DashParam> prs) {
        List<AlloyDecl> o = this.emptyDeclList();
        for (DashParam p : prs) o.add(p.asAlloyDecl());
        return o;
    }

    // s:Snapshot, p0:P0, p1:P1, ...]
    public List<AlloyDecl> curParamsDecls(List<DashParam> prs) {
        List<AlloyDecl> o = this.emptyDeclList();
        o.addAll(this.curDecls());
        o.addAll(this.paramDecls(prs));
        return o;
    }

    // s:Snapshot, s':Snapshot, p0:P0, p1:P1, ...]
    public List<AlloyDecl> curNextParamsDecls(List<DashParam> prs) {
        List<AlloyDecl> o = this.emptyDeclList();
        o.addAll(this.curNextDecls());
        o.addAll(this.paramDecls(prs));
        return o;
    }

    public AlloyDecl scopeDecl(int i) {
        List<String> cop = Collections.nCopies(i, D2AStrings.identifierName);
        return AlloyDeclArrowStringList(
                D2AStrings.scopeName + i, newListWithOneMore(cop, D2AStrings.scopeLabelName));
    }

    public AlloyDecl genEventDecl(int i) {
        if (i == 0) return new AlloyDecl(D2AStrings.genEventName + i, allEventsVar());
        else {
            List<String> cop = Collections.nCopies(i, D2AStrings.identifierName);
            return AlloyDeclArrowStringList(
                    D2AStrings.genEventName + i, newListWithOneMore(cop, D2AStrings.allEventsName));
        }
    }

    // -----------------------------------------------

    // none -> none -> none
    public AlloyExpr noneArrow(int i) {
        if (i == 0) return AlloyNone();
        else {
            return AlloyArrowExprList(Collections.nCopies(i + 1, AlloyNone()));
        }
    }

    public boolean containsVar(AlloyExpr expr, AlloyQnameExpr varToFind) {
        return new ContainsVarExprVis(varToFind).visit(expr);
    }

    public boolean containsVar(List<AlloyExpr> exprs, AlloyQnameExpr varToFind) {
        ContainsVarExprVis cvis = new ContainsVarExprVis(varToFind);
        return someTrue(mapBy(exprs, e -> cvis.visit(e)));
    }

    public AlloyExpr AlloyPredCall(String predName, List<AlloyExpr> exprList) {
        return new AlloyBracketExpr(AlloyVar(predName), exprList);
    }

    public AlloyExpr AlloyBool() {
        return AlloyVar(AlloyStrings.boolName);
    }

    public DashRef asScope(DashRef e) {
        assert (e instanceof StateDashRef);
        return new StateDashRef(e.name + D2AStrings.scopeSuffix, e.paramValues);
    }

    public List<AlloyDecl> emptyDeclList() {
        return new ArrayList<AlloyDecl>();
    }

    public List<AlloyExpr> emptyExprList() {
        return new ArrayList<AlloyExpr>();
    }

    public AlloyExpr RangeResLevel(AlloyExpr e1, AlloyExpr e2, Integer level) {
        // e1 has arity = level
        if (level == 0)
            // e1 inter e2
            return AlloyInter(e1, e2);
        else
            // e1 :> e2
            return AlloyRangeRes(e1, e2);
    }

    // Creates either
    // (list of length one) AlloyDecl(name, Quant.SET, AlloyVar(sl.get(0)))
    // or
    // (list of length > 1) AlloyVar(sl(0)) set -> set ( AlloyVar(sl(1)) set -> set AlloyVar(sl(2))
    // )
    public static AlloyDecl AlloyDeclArrowStringList(String name, List<String> sl) {
        assert (name != null && name != "" && sl != null && !sl.isEmpty());
        List<String> reversed = reverse(sl);
        AlloyExpr o = AlloyVar(reversed.get(0));
        if (sl.size() == 1) {
            return new AlloyDecl(name, AlloyDecl.Quant.SET, AlloyVar(sl.get(0)));
        } else {
            for (String s : reversed.subList(1, reversed.size())) {
                // by default this is A set -> set B
                o = new AlloyArrowExpr(AlloyVar(s), o);
            }
            return AlloyDecl(name, o);
        }
    }
}
