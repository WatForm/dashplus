package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.ImplementationError;
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

    private final List<AlloyParagraph> additionalParas;

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
     * @param alloyParagraph The paragraph to add.
     */
    public void addPara(AlloyParagraph alloyParagraph) {
        if (alloyParagraph == null) return;
        AlloyModelTable<?> table = this.patternMatch(alloyParagraph.getClass());
        @SuppressWarnings("unchecked")
        AlloyModelTable<AlloyParagraph> castedTable = (AlloyModelTable<AlloyParagraph>) table;
        castedTable.addParagraph(alloyParagraph, this.additionalParas);
    }

    public <T extends AlloyParagraph> List<T> getParas(Class<T> typeToken) {
        return this.patternMatch(typeToken).getAllParagraphs();
    }

    public <T extends AlloyParagraph> T getPara(Class<T> typeToken, String name) {
        return this.patternMatch(typeToken).getParagraph(name);
    }

    private void toString(StringBuilder sb, int indent) {
        this.alloyFile.toString(sb, indent);
        // create a new AlloyFile, so I can reuse the AlloyFile.toString
        AlloyFile newAlloyFile = new AlloyFile(this.additionalParas);
        newAlloyFile.toString(sb, indent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.toString(sb, 0);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private <T extends AlloyParagraph> AlloyModelTable<T> patternMatch(Class<T> typeToken) {
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
