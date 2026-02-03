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

import static ca.uwaterloo.watform.alloyast.paragraph.AlloyPara.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.dashast.DashPara;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlloyModel {
    private final AlloyFile alloyFile;
    private final AlloyModelTable<AlloyModulePara> modules;
    private final AlloyModelTable<AlloyImportPara> imports;
    private final AlloyModelTable<AlloyMacroPara> macros;
    private final AlloyModelSigTable sigs;
    private final AlloyModelTable<AlloyEnumPara> enums;
    private final AlloyModelTable<AlloyFactPara> facts;
    private final AlloyModelTable<AlloyFunPara> funs;
    private final AlloyModelTable<AlloyPredPara> preds;
    private final AlloyModelTable<AlloyAssertPara> asserts;
    private final AlloyModelTable<AlloyCmdPara> commands;

    // added NAD 2026-02-03
    private final List<AlloyPara> allParas;

    private final List<AlloyPara> additionalParas;

    public AlloyModel() {
        this(new AlloyFile(Collections.emptyList()));
    }

    private AlloyModel(AlloyModel other) {
        this.alloyFile =
                new AlloyFile(
                        other.alloyFile.pos,
                        filterBy(
                                other.alloyFile.paras,
                                alloyPara -> !(alloyPara instanceof DashPara)));
        this.modules = other.modules.copy();
        this.imports = other.imports;
        this.macros = other.macros.copy();
        this.sigs = other.sigs.copy();
        this.enums = other.enums.copy();
        this.facts = other.facts.copy();
        this.funs = other.funs.copy();
        this.preds = other.preds.copy();
        this.asserts = other.asserts.copy();
        this.commands = other.commands.copy();
        this.additionalParas = new ArrayList<>(other.additionalParas);

        // added NAD 2026-02-03
        // shallow copy of list
        this.allParas = new ArrayList<>(other.allParas);
    }

    public AlloyModel copy() {
        return new AlloyModel(this);
    }

    public AlloyModel(AlloyFile alloyFile) {
        this.alloyFile = alloyFile;
        this.modules = new AlloyModelTable<>(alloyFile, AlloyModulePara.class);
        this.imports = new AlloyModelTable<>(alloyFile, AlloyImportPara.class);
        this.macros = new AlloyModelTable<>(alloyFile, AlloyMacroPara.class);
        this.sigs = new AlloyModelSigTable(alloyFile);
        this.enums = new AlloyModelTable<>(alloyFile, AlloyEnumPara.class);
        this.facts = new AlloyModelTable<>(alloyFile, AlloyFactPara.class);
        this.funs = new AlloyModelTable<>(alloyFile, AlloyFunPara.class);
        this.preds = new AlloyModelTable<>(alloyFile, AlloyPredPara.class);
        this.asserts = new AlloyModelTable<>(alloyFile, AlloyAssertPara.class);
        this.commands = new AlloyModelTable<>(alloyFile, AlloyCmdPara.class);
        this.additionalParas = new ArrayList<>();

        // added NAD 2026-02-03
        // this may result in printing in a different order than input
        this.allParas = new ArrayList<>();
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyModulePara.class));
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyImportPara.class));
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyEnumPara.class));
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloySigPara.class));
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyMacroPara.class));
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyFactPara.class));
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyFunPara.class));
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyPredPara.class));
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyAssertPara.class));
        // put the run_cmd right at the beginning of the command
        // this is always the 0th command
        /// used to check model for satisfiability
        // run {}
        AlloyCmdPara run_cmd =
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                new AlloyBlock(), // empty AlloyBlock
                                null // provide no scopes -- will use default scopes
                                ));
        this.allParas.add(run_cmd);
        this.allParas.addAll(extractItemsOfClass(alloyFile.paras, AlloyCmdPara.class));
    }

    /**
     * Adds a new paragraph to the model *after* initial construction. This method sorts the
     * paragraph into the correct type-safe table. The table's 'addParagraph' method is responsible
     * for handling the 'additionalParas' list as a side-effect.
     *
     * @param alloyPara The paragraph to add.
     */
    public void addPara(AlloyPara alloyPara) {
        if (alloyPara == null) return;
        AlloyModelTable<?> table = this.patternMatch(alloyPara.getClass());
        @SuppressWarnings("unchecked")
        AlloyModelTable<AlloyPara> castedTable = (AlloyModelTable<AlloyPara>) table;
        castedTable.addPara(alloyPara, this.additionalParas);

        // added NAD 2026-02-03
        this.allParas.add(alloyPara);
    }

    public boolean containsId(String name) {
        return containsId(new AlloyId(name));
    }

    public boolean containsId(AlloyId alloyId) {
        return modules.contains(alloyId)
                || imports.contains(alloyId)
                || macros.contains(alloyId)
                || sigs.contains(alloyId)
                || sigs.containsField(alloyId.name)
                || enums.contains(alloyId)
                || facts.contains(alloyId)
                || funs.contains(alloyId)
                || preds.contains(alloyId)
                || asserts.contains(alloyId)
                || commands.contains(alloyId);
    }

    public <T extends AlloyPara> List<T> getParas(Class<T> typeToken) {
        return this.patternMatch(typeToken).getAllParas();
    }

    public <T extends AlloyPara> T getPara(Class<T> typeToken, String name) {
        return this.patternMatch(typeToken).getPara(name);
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintContext pCtx = new PrintContext(sw);
        // NAD commented out 2026-02-03: this.alloyFile.ppNewBlock(pCtx);
        // create a new AlloyFile, so I can reuse AlloyFile.toString
        // NAD commented out 2026-02-03:  AlloyFile newAlloyFile = new
        // AlloyFile(this.additionalParas);
        // NAD added 2026-02-03
        AlloyFile newAlloyFile = new AlloyFile(this.allParas);
        newAlloyFile.ppNewBlock(pCtx);
        return sw.toString();
    }

    @SuppressWarnings("unchecked")
    private <T extends AlloyPara> AlloyModelTable<T> patternMatch(Class<T> typeToken) {
        if (typeToken.equals(AlloyModulePara.class)) {
            return (AlloyModelTable<T>) modules;
        } else if (typeToken.equals(AlloyImportPara.class)) {
            return (AlloyModelTable<T>) imports;
        } else if (typeToken.equals(AlloyMacroPara.class)) {
            return (AlloyModelTable<T>) macros;
        } else if (typeToken.equals(AlloySigPara.class)) {
            return (AlloyModelTable<T>) sigs;
        } else if (typeToken.equals(AlloyEnumPara.class)) {
            return (AlloyModelTable<T>) enums;
        } else if (typeToken.equals(AlloyFactPara.class)) {
            return (AlloyModelTable<T>) facts;
        } else if (typeToken.equals(AlloyFunPara.class)) {
            return (AlloyModelTable<T>) funs;
        } else if (typeToken.equals(AlloyPredPara.class)) {
            return (AlloyModelTable<T>) preds;
        } else if (typeToken.equals(AlloyAssertPara.class)) {
            return (AlloyModelTable<T>) asserts;
        } else if (typeToken.equals(AlloyCmdPara.class)) {
            return (AlloyModelTable<T>) commands;
        }
        throw ImplementationError.missingCase("AlloyModel.patternMatch");
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
}
