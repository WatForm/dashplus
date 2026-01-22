/*
    This is a DSL (common subexpressions) 
    for the Dash to Alloy translator.
*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.dashtoalloy.AlloyHelper.*;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.exprvisitor.ContainsVarExprVis;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

public class DSL {

    DashModel dm; // input
    boolean isElectrum = false;

    protected DSL(DashModel dm, boolean isElectrum) {
        this.dm = dm;
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
        List<AlloyExpr> o = new ArrayList<AlloyExpr>();
        if (!this.isElectrum) {
            o.add(curVar());
            o.add(nextVar());
        }
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

    // intEvents
    public AlloyExpr allIntEventsVar() {
        return AlloyVar(D2AStrings.allIntEventsName);
    }

    // Electrum only
    // e'
    public AlloyUnaryExpr primedVarExpr(AlloyQnameExpr e) {
        assert (this.isElectrum);
        return new AlloyPrimeExpr(e);
    }


    // ----------------------------

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

    //s.scopeUsed1
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
        //return createEquals(curJoinExpr(stable()),createFalse());
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
        List<AlloyExpr> elist = new ArrayList<AlloyExpr>();
        elist.add(e);
        return AlloyPredCall(D2AStrings.isTrue,elist);
    }
    public AlloyExpr AlloyIsFalse(AlloyExpr e) {
        List<AlloyExpr> elist = new ArrayList<AlloyExpr>();
        elist.add(e);
        return AlloyPredCall(D2AStrings.isFalse,elist);
    }

    // decls ---------------------------
    // s:Snapshot
    public AlloyDecl curDecl() {
        return new AlloyDecl(D2AStrings.curName, D2AStrings.snapshotName);
    }

    // snext:Snapshot
    public AlloyDecl nextDecl() {
        return (AlloyDecl) new AlloyDecl(D2AStrings.nextName, D2AStrings.snapshotName);
    }

    // [s:Snapshot]
    public List<AlloyDecl> curDecls() {
        List<AlloyDecl> o = new ArrayList<AlloyDecl>();
        if (!this.isElectrum) o.add(curDecl());
        return o;
    }

    // [snext:Snapshot]
    public List<AlloyDecl> nextDecls() {
        List<AlloyDecl> o = new ArrayList<AlloyDecl>();
        if (!this.isElectrum) o.add(nextDecl());
        return o;
    }

    // [s:Snapshot, snext:Snapshot]
    public List<AlloyDecl> curNextDecls() {
        List<AlloyDecl> o = new ArrayList<AlloyDecl>();
        if (!this.isElectrum) {
            o.add(curDecl());
            o.add(nextDecl());
        }
        return o;
    }

    public AlloyExpr DashRefToArrow(DashRef e) {
        List<AlloyExpr> ll = new ArrayList<AlloyExpr>(e.paramValues);
        Collections.reverse(ll);
        ll.add(AlloyVar(DashFQN.translateFQN(e.name)));
        return AlloyArrowExprList(ll);
    }

    // none -> none -> none
    public AlloyExpr noneArrow(int i) {
        if (i==0) 
            return AlloyNone();
        else {
            return AlloyArrowExprList(Collections.nCopies(i+1,AlloyNone()));
        }
    }

    public boolean containsVar(AlloyExpr expr, AlloyQnameExpr varToFind) {
        return new ContainsVarExprVis(varToFind).visit(expr);
    }

    public AlloyExpr AlloyPredCall(String predName, List<AlloyExpr> exprList) {
        return new AlloyBracketExpr(AlloyVar(predName), exprList);
    }



}
