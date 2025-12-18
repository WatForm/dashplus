package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
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
    private final AlloyModelTable<AlloySigPara> sigs;
    private final AlloyModelTable<AlloyEnumPara> enums;
    private final AlloyModelTable<AlloyFactPara> facts;
    private final AlloyModelTable<AlloyFunPara> funs;
    private final AlloyModelTable<AlloyPredPara> preds;
    private final AlloyModelTable<AlloyAssertPara> asserts;
    private final AlloyModelTable<AlloyCmdPara> commands;

    private final List<AlloyPara> additionalParas;

    public AlloyModel() {
        this(new AlloyFile(Collections.emptyList()));
    }

    public AlloyModel(AlloyFile alloyFile) {
        this.alloyFile = alloyFile;
        this.modules = new AlloyModelTable<>(alloyFile, AlloyModulePara.class);
        this.imports = new AlloyModelTable<>(alloyFile, AlloyImportPara.class);
        this.macros = new AlloyModelTable<>(alloyFile, AlloyMacroPara.class);
        this.sigs = new AlloyModelTable<>(alloyFile, AlloySigPara.class);
        this.enums = new AlloyModelTable<>(alloyFile, AlloyEnumPara.class);
        this.facts = new AlloyModelTable<>(alloyFile, AlloyFactPara.class);
        this.funs = new AlloyModelTable<>(alloyFile, AlloyFunPara.class);
        this.preds = new AlloyModelTable<>(alloyFile, AlloyPredPara.class);
        this.asserts = new AlloyModelTable<>(alloyFile, AlloyAssertPara.class);
        this.commands = new AlloyModelTable<>(alloyFile, AlloyCmdPara.class);

        this.additionalParas = new ArrayList<>();
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
    }

    public <T extends AlloyPara> List<T> getParas(Class<T> typeToken) {
        return this.patternMatch(typeToken).getAllParas();
    }

    public <T extends AlloyPara> T getPara(Class<T> typeToken, String name) {
        return this.patternMatch(typeToken).getPara(name);
    }

    private void toPrettyString(PrintContext pCtx) {
        this.alloyFile.ppNewBlock(pCtx);
        // create a new AlloyFile, so I can reuse the AlloyFile.toString
        AlloyFile newAlloyFile = new AlloyFile(this.additionalParas);
        newAlloyFile.ppNewBlock(pCtx);
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintContext pCtx = new PrintContext(sw);
        toPrettyString(pCtx);
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
}
