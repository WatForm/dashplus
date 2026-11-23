package ca.uwaterloo.watform.alloyast.expr.unary;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyexprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

// int <expr>. Line 196, 197 in Dash.g4.
// See 4.8 Integers and Arithmetic in https://www.cin.ufpe.br/~if721/intranet/AlloyBook.pdf
// Given an expression e denoting a set of integer atoms, the integer-valued expression int e
// denotes the sum of the integer values of those atoms.

public final class AlloyNumIntExpr extends AlloyUnaryExpr {
  public AlloyNumIntExpr(Pos pos, AlloyExpr sub) {
    super(pos, sub, AlloyStrings.INT);
  }

  public AlloyNumIntExpr(AlloyExpr sub) {
    super(sub, AlloyStrings.INT);
  }

  /*
  @Override
  public void toString(StringBuilder sb, int indent) {
    sb.append(op);
    sb.append(AlloyStrings.SPACE);
    this.sub.toString(sb, indent);
  }
  */

  @Override
  public void pp(PrintContext pCtx) {
    pCtx.append(op + SPACE);
    pCtx.appendChild(this, this.sub);
  }

  @Override
  public <T> T accept(AlloyExprVis<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public AlloyNumIntExpr rebuild(AlloyExpr sub) {
    return new AlloyNumIntExpr(this.pos, sub);
  }

  @Override
  public int getPrec() {
    return AlloyExpr.NUMERIC_PREC;
  }
}
