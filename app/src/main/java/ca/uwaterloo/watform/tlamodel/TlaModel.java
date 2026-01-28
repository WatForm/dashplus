package ca.uwaterloo.watform.tlamodel;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaComment;
import ca.uwaterloo.watform.tlaast.TlaConst;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.List;

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

    private static boolean UniqueSimpleCheck(List<? extends TlaSimpleExp> l) {
        return GeneralUtil.uniqueness(
                l, (u, v) -> u.toTLAPlusSnippetCore().equals(v.toTLAPlusSnippetCore()));
    }

    public boolean UniqueVarsCheck() {
        return UniqueSimpleCheck(this.module.variables);
    }

    public boolean UniqueConstsCheck() {
        return UniqueSimpleCheck(this.module.variables);
    }

    public void addSTL(TlaStdLibs stl) {
        this.module.extended_libraries.add(stl);
    }

    public void addVar(TlaVar v) {
        this.module.variables.add(v);
    }

    public void addConst(TlaConst c) {
        this.module.constants.add(c);
    }

    public void addDefn(TlaDefn d) {
        this.module.body.add(d);
    }

    public void addComment(String c, boolean verbose) {
        if (verbose) this.module.body.add(new TlaComment(c));
    }
}
