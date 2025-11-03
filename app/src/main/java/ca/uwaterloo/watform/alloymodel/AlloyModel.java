package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlloyModel {
    private List<AlloyModulePara> modules = new ArrayList<>();
    private List<AlloyImportPara> imports = new ArrayList<>();
    private List<AlloyMacroPara> macros = new ArrayList<>();
    private List<AlloySigPara> sigs = new ArrayList<>();
    private List<AlloyEnumPara> enums = new ArrayList<>();
    private List<AlloyFactPara> facts = new ArrayList<>();
    private List<AlloyFunPara> funs = new ArrayList<>();
    private List<AlloyPredPara> preds = new ArrayList<>();
    private List<AlloyAssertPara> asserts = new ArrayList<>();
    private List<AlloyCmdPara> cmds = new ArrayList<>();

    public AlloyModel(AlloyFile alloyFile) {
        this.modules = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyModulePara.class);
        this.imports = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyImportPara.class);
        this.macros = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyMacroPara.class);
        this.sigs = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloySigPara.class);
        this.enums = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyEnumPara.class);
        this.facts = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyFactPara.class);
        this.funs = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyFunPara.class);
        this.preds = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyPredPara.class);
        this.asserts = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyAssertPara.class);
        this.cmds = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloyCmdPara.class);
    }

    // --- GETTERS (Returns a read-only view) ---

    public List<AlloyModulePara> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<AlloyImportPara> getImports() {
        return Collections.unmodifiableList(imports);
    }

    public List<AlloyMacroPara> getMacros() {
        return Collections.unmodifiableList(macros);
    }

    public List<AlloySigPara> getSigs() {
        return Collections.unmodifiableList(sigs);
    }

    public List<AlloyEnumPara> getEnums() {
        return Collections.unmodifiableList(enums);
    }

    public List<AlloyFactPara> getFacts() {
        return Collections.unmodifiableList(facts);
    }

    public List<AlloyFunPara> getFuns() {
        return Collections.unmodifiableList(funs);
    }

    public List<AlloyPredPara> getPreds() {
        return Collections.unmodifiableList(preds);
    }

    public List<AlloyAssertPara> getAsserts() {
        return Collections.unmodifiableList(asserts);
    }

    public List<AlloyCmdPara> getCmds() {
        return Collections.unmodifiableList(cmds);
    }
}
