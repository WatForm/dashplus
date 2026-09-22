package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyEqualsExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyIntersExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyUnionExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyNumIntExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyNumSumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import org.junit.jupiter.api.Test;

public class EvaluatorIntegerAggregationTest {
  private static FormulaEvaluator evaluator() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("integer-aggregation-test.als");
    model.resolve();
    return new FormulaEvaluator(new EvaluationTable(new Instance(xml), model), false);
  }

  private static AlloyExpr integers(int first, int second) {
    return new AlloyUnionExpr(new AlloyNumExpr(first), new AlloyNumExpr(second));
  }

  @Test
  public void intCastAndUnarySumBothAggregateIntegerSets() {
    AlloyExpr values = integers(1, 2);
    AlloyExpr intCast = new AlloyNumIntExpr(values);
    AlloyExpr sum = new AlloyNumSumExpr(values);

    assertEquals(TRUE, new AlloyEqualsExpr(intCast, new AlloyNumExpr(3)).accept(evaluator()));
    assertEquals(TRUE, new AlloyEqualsExpr(sum, new AlloyNumExpr(3)).accept(evaluator()));
  }

  @Test
  public void emptyIntegerSetSumsToZero() {
    AlloyExpr intCast = new AlloyNumIntExpr(new AlloyNoneExpr());
    AlloyExpr sum = new AlloyNumSumExpr(new AlloyNoneExpr());

    assertEquals(TRUE, new AlloyEqualsExpr(intCast, new AlloyNumExpr(0)).accept(evaluator()));
    assertEquals(TRUE, new AlloyEqualsExpr(sum, new AlloyNumExpr(0)).accept(evaluator()));
  }

  @Test
  public void unspecifiedInputProducesAnUnspecifiedSum() {
    AlloyExpr values = integers(1, 8);
    AlloyExpr unspecified = new AlloyIntersExpr(values, values);
    AlloyExpr sum = new AlloyNumSumExpr(unspecified);

    assertEquals(UNKNOWN, new AlloyEqualsExpr(sum, new AlloyNumExpr(1)).accept(evaluator()));
  }
}
