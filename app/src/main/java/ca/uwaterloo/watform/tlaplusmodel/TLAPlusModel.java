package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;

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
}
