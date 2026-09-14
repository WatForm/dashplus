package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyexprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

// the _use_ of Int in an AlloyExpr (usually an AlloyDecl)
// Int is a sig

// can also be Int[arg] (used in a unary expression) to convert a number into an Alloy Int atom
public final class AlloySigIntExpr extends AlloyVarExpr
    implements AlloySigRefExpr, AlloyScopableExpr {
  public AlloySigIntExpr(Pos pos) {
    super(pos, AlloyStrings.SIGINT);
  }

  public AlloySigIntExpr() {
    super(AlloyStrings.SIGINT);
  }

  @Override
  public <T> T accept(AlloyExprVis<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public AlloySigIntExpr rebuild(String label) {
    return new AlloySigIntExpr(this.pos);
  }
}
