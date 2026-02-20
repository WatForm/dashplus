package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyAssertPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyEnumPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFunPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyImportPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyMacroPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPara.AlloyId;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.PrintContext;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AlloyModelInitialize {

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

    public AlloyModelInitialize() {
        this(new AlloyFile(Collections.emptyList()));
    }

    protected AlloyModelInitialize(AlloyModelInitialize other) {
        this.modules = other.modules.copy();
        this.imports = other.imports.copy();
        this.macros = other.macros.copy();
        this.sigs = other.sigs.copy();
        this.enums = other.enums.copy();
        this.facts = other.facts.copy();
        this.funs = other.funs.copy();
        this.preds = other.preds.copy();
        this.asserts = other.asserts.copy();
        this.commands = other.commands.copy();
    }

    public AlloyModelInitialize copy() {
        return new AlloyModelInitialize(this);
    }

    public AlloyModelInitialize(AlloyFile alloyFile) {
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

    /** Retrieve the nth para of in this table useful for commands */
    public Optional<AlloyCmdPara> getCmdNum(int n) {
        List<AlloyCmdPara> cmdParas = this.getParas(AlloyCmdPara.class);
        if (n < 0 || n >= cmdParas.size()) return Optional.empty();
        else return Optional.of(cmdParas.get(n));
    }

    @Override
    public String toString() {
        return this.toString(true);
    }

    public String toStringNoCmds() {
        return this.toString(false);
    }

    public String toString(boolean withCmds) {
        StringWriter sw = new StringWriter();
        PrintContext pCtx = new PrintContext(sw);

        List<AlloyPara> allParas = new ArrayList<AlloyPara>();

        // first two must come before the rest
        allParas.addAll(this.modules.getAllParas());
        allParas.addAll(this.imports.getAllParas());

        allParas.addAll(this.enums.getAllParas());
        allParas.addAll(this.sigs.getAllParas());
        allParas.addAll(this.macros.getAllParas());
        allParas.addAll(this.funs.getAllParas());
        allParas.addAll(this.preds.getAllParas());
        allParas.addAll(this.facts.getAllParas());
        allParas.addAll(this.asserts.getAllParas());
        if (withCmds) allParas.addAll(this.commands.getAllParas());

        AlloyFile newAlloyFile = new AlloyFile(allParas);
        newAlloyFile.ppNewBlock(pCtx);
        return sw.toString();
    }

    @SuppressWarnings("unchecked")
    protected <T extends AlloyPara> AlloyModelTable<T> patternMatch(Class<T> typeToken) {
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
