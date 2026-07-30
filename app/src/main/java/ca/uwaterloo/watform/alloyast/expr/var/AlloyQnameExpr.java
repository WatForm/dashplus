package ca.uwaterloo.watform.alloyast.expr.var;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyDomRestrExpr;
import ca.uwaterloo.watform.alloyexprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;
import java.util.stream.Collectors;

public final class AlloyQnameExpr extends AlloyVarExpr
    implements AlloySigRefExpr, AlloyScopableExpr {
  public final List<AlloyVarExpr> vars;
  public final Kind kind;

  public AlloyQnameExpr(Pos pos, List<? extends AlloyVarExpr> vars, Kind k) {

    // this makes label be the concatenation of all vars with a SLASH
    super(pos, vars.stream().map(v -> v.label).collect(Collectors.joining(SLASH)));

    this.vars = Collections.unmodifiableList(vars);
    this.kind = k;
    if (!vars.isEmpty()) {
      if (!(vars.getFirst() instanceof AlloyNameExpr)
          && !(vars.getFirst() instanceof AlloySeqExpr)
          && !(vars.getFirst() instanceof AlloyThisExpr)) {
        throw AlloyCtorError.qnameFirstMustBeNameThisOrSeq(pos);
      }
      for (int i = 1; i < vars.size(); i++) {
        if (!(vars.get(i) instanceof AlloyNameExpr)) {
          throw AlloyCtorError.qnameTailIsAllName(pos);
        }
      }
    }

    reqNonNull(nullField(pos, this), this.vars, k);
  }

  public AlloyQnameExpr(Pos p, List<? extends AlloyVarExpr> vars) {
    this(p, vars, Kind.UNKNOWN_KIND);
  }

  public AlloyQnameExpr(List<? extends AlloyVarExpr> vars) {
    this(Pos.UNKNOWN, vars, Kind.UNKNOWN_KIND);
  }

  public AlloyQnameExpr(Pos pos, AlloyVarExpr var) {
    this(pos, Collections.unmodifiableList(Collections.singletonList(var)), Kind.UNKNOWN_KIND);
  }

  public AlloyQnameExpr(AlloyVarExpr var) {
    this(
        Pos.UNKNOWN,
        Collections.unmodifiableList(Collections.singletonList(var)),
        Kind.UNKNOWN_KIND);
  }

  public AlloyQnameExpr(Pos pos, String label) {
    this(
        pos,
        Collections.unmodifiableList(Collections.singletonList(new AlloyNameExpr(label))),
        Kind.UNKNOWN_KIND);
  }

  public AlloyQnameExpr(String label) {
    this(
        Pos.UNKNOWN,
        Collections.unmodifiableList(Collections.singletonList(new AlloyNameExpr(label))),
        Kind.UNKNOWN_KIND);
  }

  public AlloyQnameExpr(List<? extends AlloyVarExpr> vars, Kind k) {
    this(Pos.UNKNOWN, vars, k);
  }

  @Override
  public <T> T accept(AlloyExprVis<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public AlloyQnameExpr rebuild(String label) {
    return new AlloyQnameExpr(this.pos, label);
  }

  @Override
  public void pp(PrintContext pCtx) {
    if (this.kind != Kind.FIELD) pCtx.append(label);
    else {
      // it is a resolved field
      assert (this.vars.size() == 3);
      // vars.get(0) is nameSpace
      // vars.get(1) is sigParent
      // vars.get(2) is field names

      AlloyDomRestrExpr expr =
          new AlloyDomRestrExpr(
              new AlloyQnameExpr(
                  Collections.unmodifiableList(List.of(this.vars.get(0), this.vars.get(1)))),
              new AlloyQnameExpr(Collections.unmodifiableList(List.of(this.vars.get(2)))));
      pCtx.append("(");
      expr.pp(pCtx);
      pCtx.append(")");
    }
  }
}
