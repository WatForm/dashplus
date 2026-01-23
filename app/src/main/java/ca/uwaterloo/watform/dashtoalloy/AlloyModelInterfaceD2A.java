/*
    These function help create parts of an AlloyModel
*/

package ca.uwaterloo.watform.dashtoalloy;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFunPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
// import ca.uwaterloo.watform.dashast.D2AStrings;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.Collections;
import java.util.List;

public class AlloyModelInterfaceD2A extends BaseD2A {

    protected AlloyModelInterfaceD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
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

    public void addPred(String name, List<AlloyDecl> decls, List<AlloyExpr> eList) {
        this.am.addPara(
            new AlloyPredPara(
                new AlloyQnameExpr(name),
                decls,
                new AlloyBlock(eList))
            );
    }

    public void addFact(String name, List<AlloyExpr> eList) {
        this.am.addPara(
            new AlloyFunPara(
                new AlloyQnameExpr(name),
                new AlloyBlock(eList))
            );
    }
}
