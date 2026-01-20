/*
    This is a DSL for translated expressions.

    This are all static.
*/

package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
//import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.uwaterloo.watform.dashtoalloy.AlloyHelper.*;

public class DSL {

    DashModel dm; // input
    boolean isElectrum = false;

    protected DSL(DashModel dm, boolean isElectrum) {
        this.dm = dm;
        this.isElectrum = isElectrum;
    }

    // s
    public AlloyQnameExpr curVar() {
        return AlloyVar(D2AStrings.curName);
    }

    // snext
    public AlloyQnameExpr nextVar() {
        return AlloyVar(D2AStrings.nextName);
    }

    //[s,s']
    public List<AlloyExpr> curNextVars() {
        List<AlloyExpr> o = new ArrayList<AlloyExpr>();
        if (!this.isElectrum) {
            o.add(curVar());
            o.add(nextVar());
        }
        return o;        
    }

    // bufIdex0
    public AlloyQnameExpr bufferIndexVar(int i) {
        return AlloyVar(D2AStrings.bufferIndexName + i);
    }

    // Electrum only
    // e'
    public AlloyUnaryExpr primedVarExpr(AlloyQnameExpr e) {
        assert (this.isElectrum);
        return new AlloyPrimeExpr(e);
    }

    // (s.name).e
    public AlloyExpr curJoinExpr(AlloyExpr e) {
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

    // stable ------------------------------------------
    public AlloyQnameExpr stable() {
        return AlloyVar(D2AStrings.stableName);
    }

    // s.stable
    public AlloyExpr curStable() {
        return curJoinExpr(stable());
    }

    // s'.stable
    public AlloyExpr nextStable() {
        return nextJoinExpr(stable());
    }

    // s.stable == boolean/True
   /* public AlloyExpr curStableTrue() {
        return createIsTrue(curJoinExpr(stable()));
    }

    // s.stable == boolean/False
    public AlloyExpr curStableFalse() {
        return createIsFalse(curJoinExpr(stable()));
        //return createEquals(curJoinExpr(stable()),createFalse());
    }

    // s'.stable == boolean/True
    public AlloyExpr nextStableTrue() {
        return createIsTrue(nextJoinExpr(stable()));
        //return createEquals(nextJoinExpr(stable()),createTrue());
    }

    // s'.stable == boolean/False
    public AlloyExpr nextStableFalse() {
        return createIsFalse(nextJoinExpr(stable()));
        //return createEquals(nextJoinExpr(stable()),createFalse());
    }*/

    // decls ---------------------------
    // s:Snapshot
    public AlloyDecl curDecl() {
        return new AlloyDecl(
            D2AStrings.curName, 
            D2AStrings.snapshotName);
    }
    
    // s':Snapshot
    public AlloyDecl nextDecl() {
        return (AlloyDecl) new AlloyDecl(
            D2AStrings.nextName, 
            D2AStrings.snapshotName);
    }

    // [s:Snapshot] 
    public List<AlloyDecl> curDecls() {
        List<AlloyDecl> o = new ArrayList<AlloyDecl>();
        if (!this.isElectrum)
            o.add(curDecl());
        return o;
    }
    // [snext:Snapshot]
    public List<AlloyDecl> nextDecls() {
        List<AlloyDecl> o = new ArrayList<AlloyDecl>();
        if (!this.isElectrum)
            o.add(nextDecl());
        return o;
    }

    // [s:Snapshot, s':Snapshot] 
    public List<AlloyDecl> curNextDecls() {
        List<AlloyDecl> o = new ArrayList<AlloyDecl>();
        if (!this.isElectrum) {
            o.add(curDecl());
            o.add(nextDecl());
        }
        return o;
    }

 


}
