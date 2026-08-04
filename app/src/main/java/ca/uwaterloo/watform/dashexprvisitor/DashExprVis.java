package ca.uwaterloo.watform.dashexprvisitor;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyexprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.dashast.dashref.DashRef;

public interface DashExprVis<T> extends AlloyExprVis<T> {

  @Override
  public default T visit(AlloyExpr expr) {
    // if (expr instanceof DashRef) throw ImplementationError.shouldNotReach();
    // System.out.println(this.getClass().getName());
    return expr.accept(this);
  }

  T visit(DashRef dashRef);
}
