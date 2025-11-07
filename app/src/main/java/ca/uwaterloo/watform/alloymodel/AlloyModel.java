package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import java.util.ArrayList;
import java.util.List;

public final class AlloyModel {
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

    public void toString(StringBuilder sb, int indent) {
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

    /**
     * @return The table containing all `module` paragraphs.
     */
    public AlloyModelTable<AlloyModulePara> getModules() {
        return modules;
    }

    /**
     * @return The table containing all `open` paragraphs.
     */
    public AlloyModelTable<AlloyImportPara> getImports() {
        return imports;
    }

    /**
     * @return The table containing all `macro` paragraphs.
     */
    public AlloyModelTable<AlloyMacroPara> getMacros() {
        return macros;
    }

    /**
     * @return The table containing all `sig` paragraphs.
     */
    public AlloyModelTable<AlloySigPara> getSigs() {
        return sigs;
    }

    /**
     * @return The table containing all `enum` paragraphs.
     */
    public AlloyModelTable<AlloyEnumPara> getEnums() {
        return enums;
    }

    /**
     * @return The table containing all `fact` paragraphs (named and unnamed).
     */
    public AlloyModelTable<AlloyFactPara> getFacts() {
        return facts;
    }

    /**
     * @return The table containing all `fun` paragraphs.
     */
    public AlloyModelTable<AlloyFunPara> getFuns() {
        return funs;
    }

    /**
     * @return The table containing all `pred` paragraphs.
     */
    public AlloyModelTable<AlloyPredPara> getPreds() {
        return preds;
    }

    /**
     * @return The table containing all `assert` paragraphs (named and unnamed).
     */
    public AlloyModelTable<AlloyAssertPara> getAsserts() {
        return asserts;
    }

    /**
     * @return The table containing all `run` and `check` commands.
     */
    public AlloyModelTable<AlloyCmdPara> getCommands() {
        return commands;
    }
}
