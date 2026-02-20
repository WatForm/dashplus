/*
    AlloyModel has paragraphs from an AlloyFile
    and "additional paragraphs"

    Printing is done by printing the original AlloyFile
    and then adding the additional paragraphs to a
    new AlloyFile and printing it.

    TODO: we probably need to make AlloyModel just print
    its own paragraphs directly with no difference between
    original and additional ones.

*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import java.util.Collections;
import java.util.List;

public class AlloyModel extends AlloyModelResolve {

    public AlloyModel() {
        super(new AlloyFile(Collections.emptyList()));
    }

    private AlloyModel(AlloyModel other) {
        super(other);
    }

    public AlloyModel copy() {
        return new AlloyModel(this);
    }

    public AlloyModel(AlloyFile alloyFile) {
        super(alloyFile);
    }

    /**
     * Adds a new paragraph to the model *after* initial construction. This method sorts the
     * paragraph into the correct type-safe table.
     *
     * @param alloyPara The paragraph to add.
     */
    public void addPara(AlloyPara alloyPara) {
        if (alloyPara == null) return;
        AlloyModelTable<?> table = this.patternMatch(alloyPara.getClass());
        @SuppressWarnings("unchecked")
        AlloyModelTable<AlloyPara> castedTable = (AlloyModelTable<AlloyPara>) table;
        castedTable.addPara(alloyPara);
    }

    // adding: sig "n" {}
    public void addSig(String n) {
        this.addPara(new AlloySigPara(n));
    }

    // adding: abstract sig "n" {}
    public void addAbstractSig(String n) {
        this.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ABSTRACT),
                        List.of(new AlloyQnameExpr(n)),
                        null,
                        Collections.emptyList(),
                        null));
    }

    // adding: abstract sig "child" extends "parent" {}
    public void addAbstractExtendsSig(String child, String parent) {
        this.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ABSTRACT),
                        List.of(new AlloyQnameExpr(child)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parent)),
                        Collections.emptyList(),
                        null));
    }

    // adding: one sig child extends parent {}
    public void addOneExtendsSig(String child, String parent) {
        this.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ONE),
                        List.of(new AlloyQnameExpr(child)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parent)),
                        Collections.emptyList(),
                        null));
    }

    // adding: sig child extends parent {}
    public void addExtendsSig(String child, String parent) {
        this.addPara(
                new AlloySigPara(
                        Collections.emptyList(),
                        List.of(new AlloyQnameExpr(child)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parent)),
                        Collections.emptyList(),
                        null));
    }

    public void addPred(String name, List<AlloyDecl> decls, List<AlloyExpr> eList) {
        this.addPara(new AlloyPredPara(new AlloyQnameExpr(name), decls, new AlloyBlock(eList)));
    }

    public void addFact(String name, List<AlloyExpr> eList) {
        this.addPara(new AlloyFactPara(new AlloyQnameExpr(name), new AlloyBlock(eList)));
    }

    public void addImport(List<String> names, String sigName, String asName) {
        this.addPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        List.of(new AlloyQnameExpr(sigName)),
                        new AlloyQnameExpr(asName)));
    }

    public void addImport(List<String> names) {
        this.addPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        emptyList(),
                        null));
    }
}
