package ca.uwaterloo.watform.tlamodel;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaast.SnowCatTypes.SCType;

public class TlaModel {
  // top-level class to handle modules and associated configs
  public final String name;
  public final TlaModule module;
  public final TlaConfig cfg;

  public TlaModel(String name, TlaAppl init, TlaAppl next) {
    this.name = name;
    this.module = new TlaModule();
    this.cfg = new TlaConfig(init, next);
  }

  public TlaAppl getInit() {
    return this.cfg.init;
  }

  public TlaAppl getNext() {
    return this.cfg.next;
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

  public void addVar(TlaVar v, SCType t) {
    this.module.variables.add(new TlaModule.TlaVarDecl(v, t));
  }

  public void addConst(TlaConst c, SCType t, TlaExp value) {
    this.module.constants.add(new TlaModule.TlaConstDecl(c, t));
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

  public void addComment(TlaComment c) {
    this.module.body.add(c);
  }
}
