/*
   All the getters/setters to handle sigs.

   On initialization:
    - initializes sigTable
    - adds sig children
    - checks for cycles in sig (in and extends relations) and field declarations
    - calculates arity of fields

    Terminology
    sigName, fieldName = String that is name of sig or field
    sigPara, field = sigParagraph or AlloyDecl that is a field
    sigNames, fieldNames = string list of sig names or field names
    allSigs = string list of all sig names
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class AMSigs extends AMEnums {

    protected List<AlloySigPara> sigs = emptyList();

    protected AMSigs(AMSigs other) {
        super(other);
        this.sigs = new ArrayList<AlloySigPara>(other.sigs);
        // everything has been added to the SigTable and FieldTable because
        // of the super
    }

    protected AMSigs(AlloyFile alloyFile) {
        super(alloyFile);
        // these have not been added to the SigTable and FieldTable
        this.addSigParas(extractItemsOfClass(alloyFile.paras, AlloySigPara.class));
    }

    protected void resolve() {
        // this does all field checks
        super.resolve();
        // must be done after allFieldChecks
        // walks over the fields and blocks of a sig Para
        // adds to this.sigs
        List<AlloySigPara> newSigs = emptyList();
        // won't be any multi-sigs in this.sigs
        for (AlloySigPara sigPara : this.sigs) {
            /*
            AlloySigPara newSigPara =
                    sig.rebuild(
                            mapBy(sig.fields, f -> ((AlloyDecl) this.setMul(f))),
                            ((AlloyBlock) sig.block.map(b -> this.setMul(b)).orElse(null)));
            */
            AlloySigPara newSigPara = (AlloySigPara) this.setMul(sigPara);
            // adds them one by one after setting defaults
            newSigs.add(newSigPara);
        }
        this.sigs = newSigs;
    }

    // used by API
    public void addSigPara(AlloySigPara sigPara) {
        // System.out.println("Adding:");
        // System.out.println(sigPara);
        this.addSigParas(List.of(sigPara));
    }

    // for bulk adding sigParas
    private void addSigParas(List<AlloySigPara> sigParas) {
        // System.out.println(sigParas);
        for (AlloySigPara multi_sig : sigParas) {
            for (AlloySigPara sigPara : multi_sig.expand()) {
                // System.out.println("one para:");
                // System.out.println(sigPara);
                this.addToSigTable(
                        sigPara); // this can handle multi_sig para, even if we aren't asking it to
                this.sigs.add(sigPara);
            }
        }
    }

    // commonly used creation of sigs

    // adding: sig "n" {}
    public void addSig(String sigName) {
        this.addSigPara(new AlloySigPara(sigName));
    }

    // adding: abstract sig "n" {}
    public void addAbstractSig(String sigName) {
        this.addSigPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ABSTRACT),
                        List.of(new AlloyQnameExpr(sigName)),
                        null,
                        Collections.emptyList(),
                        null));
    }

    // adding: abstract sig "child" extends "parent" {}
    public void addAbstractExtendsSig(String childName, String parentName) {
        this.addSigPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ABSTRACT),
                        List.of(new AlloyQnameExpr(childName)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parentName)),
                        Collections.emptyList(),
                        null));
    }

    // adding: one sig child extends parent {}
    public void addOneExtendsSig(String childName, String parentName) {
        this.addSigPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ONE),
                        List.of(new AlloyQnameExpr(childName)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parentName)),
                        Collections.emptyList(),
                        null));
    }

    // adding: sig child extends parent {}
    public void addExtendsSig(String childName, String parentName) {
        this.addSigPara(
                new AlloySigPara(
                        Collections.emptyList(),
                        List.of(new AlloyQnameExpr(childName)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parentName)),
                        Collections.emptyList(),
                        null));
    }

    // adding: sig child in parent {}
    public void addInSig(String childName, List<AlloySigRefExpr> parentExprs) {
        this.addSigPara(
                new AlloySigPara(
                        Collections.emptyList(),
                        List.of(new AlloyQnameExpr(childName)),
                        new AlloySigPara.In(parentExprs),
                        Collections.emptyList(),
                        null));
    }

    public List<AlloySigPara> allSigParas() {
        return new ArrayList<AlloySigPara>(this.sigs);
    }
}
