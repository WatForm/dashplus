package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusSimpleExpression;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.List;

public class TLAPlusModel {
    // top-level class to handle modules and associated configs
    private String name;
    public TLAPlusModule module;
    public TLAPlusConfiguration cfg;

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

    private static boolean UniqueSimpleCheck(List<? extends TLAPlusSimpleExpression> l) {
        return GeneralUtil.uniqueness(
                l, (u, v) -> u.toTLAPlusSnippetCore().equals(v.toTLAPlusSnippetCore()));
    }

    public boolean UniqueVariablesCheck() {
        return UniqueSimpleCheck(this.module.getVariables());
    }

    public boolean UniqueConstantsCheck() {
        return UniqueSimpleCheck(this.module.getConstants());
    }
}
