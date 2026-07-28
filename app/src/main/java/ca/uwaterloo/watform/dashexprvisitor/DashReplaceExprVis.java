/*
    Apply test to each expression. If test is true, return result of "replace" function.
    Otherwise, spply visitors to subexpressions and return rebuild
	expressions.

	If nothing here is overwritten, it will return
	exactly the same expression.
*/

package ca.uwaterloo.watform.dashexprvisitor;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyexprvisitor.ReplaceExprVis;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import java.util.*;
import java.util.function.Function;

public class DashReplaceExprVis extends ReplaceExprVis {

  public DashReplaceExprVis(
      Function<AlloyExpr, Boolean> test, Function<AlloyExpr, AlloyExpr> replace) {
    super(test, replace);
  }

  public AlloyExpr visit(DashRef dashRef) {
    if (this.test.apply(dashRef)) return this.replace.apply(dashRef);
    return dashRef;
  }
}
