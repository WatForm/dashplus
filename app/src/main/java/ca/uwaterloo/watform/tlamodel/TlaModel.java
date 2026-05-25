package ca.uwaterloo.watform.tlamodel;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaComment;
import ca.uwaterloo.watform.tlaast.TlaConst;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlaast.TlaTypes;
import ca.uwaterloo.watform.tlaast.TlaVar;

public class TlaModel {
    // top-level class to handle modules and associated configs
    public final String name;
    private TlaModule module;
    private TlaConfig cfg;

    public TlaModel(String name, TlaAppl init, TlaAppl next) {
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

    public void addSTL(TlaStdLibs stl) {
        this.module.extended_libraries.add(stl);
    }

    public void addVar(TlaVar v, TlaTypes.Type t) {
        this.module.variables.add(new TlaModule.TlaVarDecl(v, t));
    }

    public void addConst(TlaConst c, TlaExp value) {
        this.module.constants.add(c);
        this.cfg.constants.add(c.EQUALS(value));
    }

    public void addDefn(TlaDefn d) {
        this.module.body.add(d);
    }

    public void addInvariant(TlaAppl d) {
        this.cfg.invariants.add(d);
    }

    public void addComment(String c, boolean verbose) {
        if (verbose) this.module.body.add(new TlaComment(c));
    }
}
