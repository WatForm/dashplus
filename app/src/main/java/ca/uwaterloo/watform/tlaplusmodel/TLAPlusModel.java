package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.TLAPlusBlankLine;
import ca.uwaterloo.watform.tlaplusast.TLAPlusComment;
import ca.uwaterloo.watform.tlaplusast.TLAPlusConst;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.TLAPlusSimpleExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStandardLibraries;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVar;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.List;

public class TLAPlusModel {
    // top-level class to handle modules and associated configs
    public final String name;
    private TLAPlusModule module;
    private TLAPlusConfiguration cfg;

    public TLAPlusModel(
            String name, TLAPlusFormulaApplication init, TLAPlusFormulaApplication next) {
        this.name = name;
        this.module = new TLAPlusModule();
        this.cfg = new TLAPlusConfiguration(init, next);
    }

    public String moduleCode() {
        return this.module.code(this.name);
    }

    public String configCode() {
        return this.cfg.code();
    }

    private static boolean UniqueSimpleCheck(List<? extends TLAPlusSimpleExp> l) {
        return GeneralUtil.uniqueness(
                l, (u, v) -> u.toTLAPlusSnippetCore().equals(v.toTLAPlusSnippetCore()));
    }

    public boolean UniqueVariablesCheck() {
        return UniqueSimpleCheck(this.module.variables);
    }

    public boolean UniqueConstantsCheck() {
        return UniqueSimpleCheck(this.module.variables);
    }

    public void addSTL(TLAPlusStandardLibraries stl) {
        this.module.extended_libraries.add(stl);
    }

    public void addVariable(TLAPlusVar v) {
        this.module.variables.add(v);
    }

    public void addConstant(TLAPlusConst c) {
        this.module.constants.add(c);
    }

    public void addFormulaDefinition(TLAPlusFormulaDefn d) {
        this.module.body.add(d);
    }

    public void addComment(String c) {
        this.module.body.add(new TLAPlusComment(c));
    }

    public void addBlankLine() {
        this.module.body.add(new TLAPlusBlankLine());
    }
}
