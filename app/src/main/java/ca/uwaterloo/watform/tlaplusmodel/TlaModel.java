package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.TlaBlankLine;
import ca.uwaterloo.watform.tlaplusast.TlaComment;
import ca.uwaterloo.watform.tlaplusast.TlaConst;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaplusast.TlaStdLibs;
import ca.uwaterloo.watform.tlaplusast.TlaVar;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.List;

public class TlaModel {
    // top-level class to handle modules and associated configs
    public final String name;
    private TlaModule module;
    private TlaConfig cfg;

    public TlaModel(String name, TlaFormulaAppl init, TlaFormulaAppl next) {
        this.name = name;
        this.module = new TlaModule();
        this.cfg = new TlaConfig(init, next);
    }

    public String moduleCode() {
        return this.module.code(this.name);
    }

    public String configCode() {
        return this.cfg.code();
    }

    private static boolean UniqueSimpleCheck(List<? extends TlaSimpleExp> l) {
        return GeneralUtil.uniqueness(
                l, (u, v) -> u.toTLAPlusSnippetCore().equals(v.toTLAPlusSnippetCore()));
    }

    public boolean UniqueVariablesCheck() {
        return UniqueSimpleCheck(this.module.variables);
    }

    public boolean UniqueConstantsCheck() {
        return UniqueSimpleCheck(this.module.variables);
    }

    public void addSTL(TlaStdLibs stl) {
        this.module.extended_libraries.add(stl);
    }

    public void addVariable(TlaVar v) {
        this.module.variables.add(v);
    }

    public void addConstant(TlaConst c) {
        this.module.constants.add(c);
    }

    public void addFormulaDefinition(TlaFormulaDefn d) {
        this.module.body.add(d);
    }

    public void addComment(String c) {
        this.module.body.add(new TlaComment(c));
    }

    public void addBlankLine() {
        this.module.body.add(new TlaBlankLine());
    }
}
